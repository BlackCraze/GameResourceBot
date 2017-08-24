package de.blackcraze.grb.listener;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Configurable;
import org.apache.commons.lang3.StringUtils;

import com.google.inject.Injector;

import de.blackcraze.grb.commands.Speaker;
import de.blackcraze.grb.dao.IMateDao;
import de.blackcraze.grb.dao.IStockDao;
import de.blackcraze.grb.dao.IStockTypeDao;
import de.blackcraze.grb.model.PrintableTable;
import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.Stock;
import de.blackcraze.grb.model.entity.StockType;
import de.blackcraze.grb.util.PrintUtils;
import de.blackcraze.grb.util.Utils;
import de.blackcraze.grb.util.wagu.Block;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

	Injector in;

	List<Mate> users = new ArrayList<>();

	@SuppressWarnings("unused")
	private MessageListener() {
	}

	public MessageListener(Injector injector) {
		this.in = injector;
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		Message message = event.getMessage();
		if (!Utils.botMentioned(event)) {
			return;
		}
		String action = parseAction(message);

		TextChannel responseChannel = message.getTextChannel();

		switch (action.toLowerCase()) {
			case "deletetype":
				deleteType(message);
				break;
			case "newtype":
				newType(message);
				break;
			case "checktypes":
				checkTypes(message);
				break;
			case "update":
				update(message);
				break;
			case "check":
				check(message);
				break;
			case "idiot":
				ping(responseChannel);
				break;
			case "status":
				status(responseChannel);
				break;
			case "config":
				config(message);
				break;
			default:
				message.addReaction(Speaker.Reaction.FAILURE).queue();
				break;
		}
	}

	private void config(Message message) {
		String config_action = parse(message.getContent(), 2);
		if (config_action == null) {
			StringBuilder response = new StringBuilder();
			response.append("```\n");
			List<Field> fields = Arrays.stream(BotConfig.class.getDeclaredFields())
					.filter(field -> field.isAnnotationPresent(Configurable.class))
					.collect(Collectors.toList());

			for (Field field : fields) {
				Object value;
				try {
					value = field.get(null);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					continue;
				}
				response.append(field.getName());
				response.append(": ");
				response.append(value.toString());
				response.append("\n");
			}
			response.append("```");
			Speaker.say(message.getTextChannel(), response.toString());
			return;
		}
		if ("set".equals(config_action.toLowerCase())) {
			String field = parse(message.getContent(), 3);
			String value = parse(message.getContent(), 4);
			if (field != null && value != null) {
				try {
					Field declaredField = BotConfig.class.getDeclaredField(field);
					assert declaredField.isAnnotationPresent(Configurable.class);
					assert String.class.equals(declaredField.getType());
					declaredField.set(null, value);
					message.addReaction(Speaker.Reaction.SUCCESS).queue();
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		message.addReaction(Speaker.Reaction.FAILURE).queue();
	}

	private void status(TextChannel textChannel) {
		Runtime rt = Runtime.getRuntime();
		long total = rt.totalMemory();
		long free = rt.freeMemory();
		long used = total - free;

		String memConsume = String.format("~~ My Memory | Total:%,d | Used:%,d, Free:%,d", total, used, free);
		System.out.println(memConsume);
		Speaker.say(textChannel, memConsume);
	}

	private void ping(TextChannel textChannel) {
		Speaker.say(textChannel, "ich weiß :-(");
	}

	private void check(Message message) {
		String mateName = parseStockName(message.getContent(), true);
		List<Mate> mates;
		if (StringUtils.isEmpty(mateName)) {
            mates = Collections.singletonList(getOrCreateMate(message.getAuthor()));
            Speaker.say(message.getTextChannel(), prettyPrintMate(mates));
        } else {
            mates = getMateDao().findByNameLike(mateName);
            if (!mates.isEmpty()) {
                Speaker.say(message.getTextChannel(), prettyPrintMate(mates));
            }
            List<StockType> types = getStockTypeDao().findByNameLike(mateName);
            if (!types.isEmpty()) {
                Speaker.say(message.getTextChannel(), prettyPrintStocks(types));
            }
            if (types.isEmpty() && mates.isEmpty()) {
                Speaker.say(message.getTextChannel(),
                        "Ich kenne weder einen Benutzer noch eine Ressource mit diesem Namen =(");
            }
        }
	}

	private void update(Message message) {
		try {
            Map<String, Long> stocks = parseStocks(message);
            List<String> unknown = getMateDao().updateStocks(getOrCreateMate(message.getAuthor()), stocks);
            if (stocks.size() > 0) {
                if (!unknown.isEmpty()) {
                    Speaker.say(message.getTextChannel(),
                            "Das hier kenne ich nicht: " + Arrays.deepToString(unknown.toArray()));
                    message.addReaction(Speaker.Reaction.FAILURE).queue();
                }
                if (unknown.size() != stocks.size()) {
                    message.addReaction(Speaker.Reaction.SUCCESS).queue();
                }
            } else {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                Speaker.say(message.getTextChannel(), "Da steht nichts - was soll man denn da updaten!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.addReaction(Speaker.Reaction.FAILURE).queue();
        }
	}

	private void checkTypes(Message message) {
		Speaker.say(message.getTextChannel(), prettyPrintStockTypes(getStockTypeDao().findAll()));
	}

	private void newType(Message message) {
		String stockName = parseStockName(message.getContent(), true);
		if (stockName != null && !stockName.isEmpty()) {
            if (getStockTypeDao().findByName(stockName) != null) {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                Speaker.say(message.getTextChannel(), "Kenne ich schon");
            } else {
                StockType type = new StockType();
                type.setName(stockName);
                type.setPrice(0);
                getStockTypeDao().save(type);
                message.addReaction(Speaker.Reaction.SUCCESS).queue();
            }
        }
	}

	private void deleteType(Message message) {

		String stockName = parseStockName(message.getContent(), true);
		if (stockName != null && !stockName.isEmpty()) {
            StockType stockType = getStockTypeDao().findByName(stockName);
            if (stockType != null) {
                getStockTypeDao().delete(stockType);
                message.addReaction(Speaker.Reaction.SUCCESS).queue();
            } else {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                Speaker.say(message.getTextChannel(), "Kenne ich nicht");
            }
        }
	}

	private Mate getOrCreateMate(User author) {
		String discordId = author.getId();
		Mate mate = getMateDao().findByDiscord(discordId);
		if (mate == null) {
			mate = new Mate();
			mate.setDiscordId(discordId);
			mate.setName(author.getName());
			getMateDao().save(mate);
			mate = getMateDao().findByDiscord(mate.getDiscordId());
		}
		return mate;
	}

	private IStockTypeDao getStockTypeDao() {
		return in.getInstance(IStockTypeDao.class);
	}

	private IMateDao getMateDao() {
		return in.getInstance(IMateDao.class);
	}

	private IStockDao getStockDao() {
		return in.getInstance(IStockDao.class);
	}

	private String prettyPrintStockTypes(List<StockType> stockTypes) {
		StringBuilder b = new StringBuilder();
		b.append("```\n");
		if (stockTypes.isEmpty()) {
			b.append("Leider keine Daten vorhanden :-(");
		}
		for (StockType type : stockTypes) {
			b.append(type.getName());
			b.append("\n");
		}
		b.append("\n```\n");
		return b.toString();
	}

	private String prettyPrintMate(List<Mate> mates) {
		if (mates.isEmpty()) {
			return "Diesen Benutzer kenne ich nicht =(";
		}
		List<String> headers = Arrays.asList("Rohstoff", "Menge", "Gepflegt vor");
		List<Integer> aligns = Arrays.asList(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT, Block.DATA_MIDDLE_RIGHT);

		PrintableTable[] tables = new PrintableTable[mates.size()];
		for (int i = 0; i < mates.size(); i++) {
			Mate mate = mates.get(i);
			List<Stock> stocks = getStockDao().findStocksByMate(mate);
			List<List<String>> rows = new ArrayList<>(stocks.size());
			if (stocks.isEmpty()) {
				rows.add(Arrays.asList("-", "-", "-"));
			}
			for (Stock stock : stocks) {
				String name = stock.getType().getName();
				String amount = String.format("%,d", stock.getAmount());
				String updated = PrintUtils.getgetDiffFormatted(stock.getUpdated(), new Date());
				rows.add(Arrays.asList(name, amount, updated));
			}
			tables[i] = new PrintableTable(mate.getName(), Collections.emptyList(), headers, rows, aligns);
		}
		return PrintUtils.prettyPrint(tables);
	}

	private String prettyPrintStocks(List<StockType> stockTypes) {
		if (stockTypes.isEmpty()) {
			return "Diese Ressource kenne ich nicht =(";
		}
		List<String> headers = Arrays.asList("Benutzer", "Menge", "Gepflegt vor");
		List<Integer> aligns = Arrays.asList(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT, Block.DATA_MIDDLE_RIGHT);

		PrintableTable[] tables = new PrintableTable[stockTypes.size()];
		for (int i = 0; i < stockTypes.size(); i++) {
			StockType type = stockTypes.get(i);
			List<Stock> stocks = getStockDao().findStocksByType(type);
			List<List<String>> rows = new ArrayList<>(stocks.size());
			long sumAmount = 0;
			if (stocks.isEmpty()) {
				rows.add(Arrays.asList("-", "-", "-"));
			}
			for (Stock stock : stocks) {
				String name = stock.getMate().getName();
				String amount = String.format("%,d", stock.getAmount());
				String updated = PrintUtils.getgetDiffFormatted(stock.getUpdated(), new Date());
				rows.add(Arrays.asList(name, amount, updated));
				sumAmount += stock.getAmount();
			}
			List<String> summary = Arrays.asList(type.getName(), String.format("%,d", sumAmount), "");
			tables[i] = new PrintableTable(type.getName(), summary, headers, rows, aligns);
		}
		return PrintUtils.prettyPrint(tables);
	}

	private Map<String, Long> parseStocks(Message message) {
		String rows[] = message.getRawContent().split("\\r?\\n");
		Map<String, Long> stocks = new HashMap<>(rows.length);
		for (int i = 0; i < rows.length; i++) {
			boolean firstRow = i == 0;
			String row = rows[i];
			Long amount = parseAmount(row, firstRow);
			String name = parseStockName(row, firstRow);
			if (amount != null && name != null) {
				stocks.put(name, amount);
			}
		}
		return stocks;
	}

	private String parseAction(Message message) {
		return parse(message.getContent(), 1, false);
	}

	private String parseStockName(String row, boolean firstRow) {
		return parse(row, 0, firstRow);
	}

	private Long parseAmount(String row, boolean firstRow) {
		try {
			return Long.valueOf(parse(row, 1, firstRow));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private String parse(String row, int index) {
		return parse(row, index, false);
	}

	private String parse(String row, int index, boolean firstRow) {
		index = firstRow ? index + 2 : index;
		String[] split = StringUtils.split(row, " ");
		if (index < split.length) {
			return split[index];
		}
		return null;
	}

}
