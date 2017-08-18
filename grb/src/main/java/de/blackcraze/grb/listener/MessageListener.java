package de.blackcraze.grb.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import net.dv8tion.jda.core.JDA;
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
		if (Utils.botMentioned(event)) {
			Mate mate = getOrCreateMate(message.getAuthor());
			String action = parseAction(message);
			JDA jda = event.getJDA();
			List<TextChannel> responseChannel = event.getGuild().getTextChannels();
			if ("deleteType".equalsIgnoreCase(action)) {
				String stockName = parseStockName(message.getContent(), true);
				if (stockName != null && !stockName.isEmpty()) {
					StockType stockType = getStockTypeDao().findByName(stockName);
					if (stockType != null) {
						getStockTypeDao().delete(stockType);
						Speaker.checkReaction(jda, message);
					} else {
						Speaker.errorReaction(jda, message);
						Speaker.say(responseChannel, "Kenne ich nicht");
					}
				}
			} else if ("newType".equalsIgnoreCase(action)) {
				String stockName = parseStockName(message.getContent(), true);
				if (stockName != null && !stockName.isEmpty()) {
					if (getStockTypeDao().findByName(stockName) != null) {
						Speaker.errorReaction(jda, message);
						Speaker.say(responseChannel, "Kenne ich schon");
					} else {
						StockType type = new StockType();
						type.setName(stockName);
						type.setPrice(0);
						getStockTypeDao().save(type);
						Speaker.checkReaction(jda, message);
					}
				}
			} else if ("checkTypes".equalsIgnoreCase(action)) {
				Speaker.say(responseChannel, prettyPrintStockTypes(getStockTypeDao().findAll()));
			} else if ("update".equalsIgnoreCase(action)) {
				try {
					Map<String, Long> stocks = parseStocks(message);
					List<String> unknown = getMateDao().updateStocks(mate, stocks);
					if (stocks.size() > 0) {
						if (!unknown.isEmpty()) {
							Speaker.say(responseChannel,
									"Das hier kenne ich nicht: " + Arrays.deepToString(unknown.toArray()));
							Speaker.errorReaction(jda, message);
						}
						if (unknown.size() != stocks.size()) {
							Speaker.checkReaction(jda, message);
						}
					} else {
						Speaker.errorReaction(jda, message);
						Speaker.say(responseChannel, "Da steht nichts - was soll man denn da updaten!");
					}
				} catch (Exception e) {
					e.printStackTrace();
					Speaker.errorReaction(jda, message);
				}
			} else if ("check".equalsIgnoreCase(action)) {
				String mateName = parseStockName(message.getContent(), true);
				List<Mate> mates;
				if (StringUtils.isEmpty(mateName)) {
					mates = Collections.singletonList(mate);
					Speaker.say(responseChannel, prettyPrintMate(mates));
				} else {
					mates = getMateDao().findByNameLike(mateName);
					if (!mates.isEmpty()) {
						Speaker.say(responseChannel, prettyPrintMate(mates));
					}
					List<StockType> types = getStockTypeDao().findByNameLike(mateName);
					if (!types.isEmpty()) {
						Speaker.say(responseChannel, prettyPrintStocks(types));
					}
					if (types.isEmpty() && mates.isEmpty()) {
						Speaker.say(responseChannel,
								"Ich kenne weder einen Benutzer noch eine Ressource mit diesem Namen =(");
					}
				}
			} else if ("IDIOT".equalsIgnoreCase(action)) {
				Speaker.say(responseChannel, "ich weiß :-(");
			} else {
				Speaker.errorReaction(jda, message);
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
		b.append("```css\n");
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
		} catch (Exception e) {
			return null;
		}
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
