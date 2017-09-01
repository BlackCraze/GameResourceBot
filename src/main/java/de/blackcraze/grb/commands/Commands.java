package de.blackcraze.grb.commands;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.CommandUtils.parseStockName;
import static de.blackcraze.grb.util.CommandUtils.parseStocks;
import static de.blackcraze.grb.util.InjectorUtils.getMateDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;
import static de.blackcraze.grb.util.PrintUtils.prettyPrint;
import static de.blackcraze.grb.util.PrintUtils.prettyPrintMate;
import static de.blackcraze.grb.util.PrintUtils.prettyPrintStockTypes;
import static de.blackcraze.grb.util.PrintUtils.prettyPrintStocks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.PrintableTable;
import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.StockType;
import de.blackcraze.grb.util.wagu.Block;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public final class Commands {

	private Commands() {
	}

	public static void ping(Scanner scanner, Message message) {
		Speaker.say(message.getTextChannel(), Resource.getString("PONG", getResponseLocale(message)));
	}

	public static void credits(Scanner scanner, Message message) {
		Speaker.say(message.getTextChannel(), Resource.getString("CDS", getResponseLocale(message)));
	}

	public static void config(Scanner scanner, Message message) {
		BotConfig.ServerConfig instance = BotConfig.getConfig(message.getGuild());
		if (!scanner.hasNext()) {
			StringBuilder response = new StringBuilder();
			response.append("```\n");
			Field[] fields = BotConfig.ServerConfig.class.getDeclaredFields();

			for (Field field : fields) {
				Object value;
				try {
					value = field.get(instance);
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
		String config_action = scanner.next();
		if ("set".equals(config_action.toLowerCase())) {
			String field = scanner.next();
			String value = scanner.next();
			if (Objects.isNull(field) || Objects.isNull(value)) {
				message.addReaction(Speaker.Reaction.FAILURE).queue();
				return;
			}
			try {
				Field declaredField = BotConfig.ServerConfig.class.getDeclaredField(field.toUpperCase());
				assert String.class.equals(declaredField.getType());
				declaredField.set(instance, value);
				message.addReaction(Speaker.Reaction.SUCCESS).queue();
			} catch (Exception e) {
				message.addReaction(Speaker.Reaction.FAILURE).queue();
				e.printStackTrace();
			}
		}
	}

	public static void status(Scanner scanner, Message message) {
		Runtime rt = Runtime.getRuntime();
		long total = rt.totalMemory();
		long free = rt.freeMemory();
		long used = total - free;

		String memConsume = String.format("~~ My Memory | Total:%,d | Used:%,d, Free:%,d", total, used, free);
		System.out.println(memConsume);
		Speaker.say(message.getTextChannel(), memConsume);
	}

	public static void update(Scanner scanner, Message message) {
		Locale responseLocale = getResponseLocale(message);
		Map<String, Long> stocks = parseStocks(scanner, responseLocale);
		List<String> unknownStocks;
		try {
			unknownStocks = getMateDao().updateStocks(getMateDao().getOrCreateMate(message.getAuthor()), stocks);
		} catch (Exception e) {
			e.printStackTrace();
			message.addReaction(Speaker.Reaction.FAILURE).queue();
			return;
		}
		if (stocks.size() > 0) {
			if (!unknownStocks.isEmpty()) {
				Speaker.err(message, String.format(Resource.getString("DO_NOT_KNOW_ABOUT", responseLocale),
						unknownStocks.toString()));
			}
			if (unknownStocks.size() != stocks.size()) {
				message.addReaction(Speaker.Reaction.SUCCESS).queue();
			}
		} else {
			Speaker.err(message, Resource.getString("RESOURCES_EMPTY", responseLocale));
		}
	}

	public static void checkTypes(Scanner scanner, Message message) {
		Optional<String> stockNameOptional = parseStockName(scanner);
		Locale locale = getResponseLocale(message);
		List<StockType> stocks = stockNameOptional.isPresent()
				? getStockTypeDao().findByNameLike(stockNameOptional.get(), locale) : getStockTypeDao().findAll();
		Speaker.say(message.getTextChannel(), prettyPrintStockTypes(stocks, locale));
	}

	public static void check(Scanner scanner, Message message) {
		Optional<String> mateOrStockOptional = parseStockName(scanner);
		TextChannel textChannel = message.getTextChannel();
		Locale locale = getResponseLocale(message);
		if (!mateOrStockOptional.isPresent()) {
			List<Mate> mates = Collections.singletonList(getMateDao().getOrCreateMate(message.getAuthor()));
			Speaker.say(textChannel, prettyPrintMate(mates, locale));
		} else {
			List<Mate> mates = getMateDao().findByNameLike(mateOrStockOptional.get());
			if (!mates.isEmpty()) {
				Speaker.say(textChannel, prettyPrintMate(mates, locale));
			}
			List<StockType> types = getStockTypeDao().findByNameLike(mateOrStockOptional.get(), locale);
			if (!types.isEmpty()) {
				Speaker.say(textChannel, prettyPrintStocks(types, locale));
			}
			if (types.isEmpty() && mates.isEmpty()) {
				Speaker.say(textChannel, Resource.getString("RESOURCE_AND_USER_UNKNOWN", locale));
			}
		}
	}

	public static void help(Scanner scanner, Message message) {
		String response = Arrays.stream(Commands.class.getDeclaredMethods()).map(Method::getName)
				.collect(Collectors.joining(//
						"\n  ", //
						"```\n" + Resource.getString("COMMANDS", getResponseLocale(message)) + "\n  ", //
						"```"));
		Speaker.say(message.getTextChannel(), response);
	}

	public static void total(Scanner scanner, Message message) {
		Optional<String> stockNameOptional = parseStockName(scanner);
		List<StockType> stockTypes;
		Locale locale = getResponseLocale(message);
		if (!stockNameOptional.isPresent()) {
			stockTypes = getStockTypeDao().findAll();
		} else {
			stockTypes = getStockTypeDao().findByNameLike(stockNameOptional.get(), locale);
			if (stockTypes.isEmpty()) {
				Speaker.say(message.getTextChannel(), Resource.getString("RESOURCE_UNKNOWN", locale));
				return;
			}
		}

		List<List<String>> rows = new ArrayList<>();
		for (StockType stockType : stockTypes) {
			long total = getStockDao().getTotalAmount(stockType);
			if (total > 0) {
				String localisedStockName = Resource.getItem(stockType.getName(), locale);
				rows.add(Arrays.asList(localisedStockName, String.format("%,d", total)));
			}
		}
		if (!rows.isEmpty()) {
			PrintableTable total_guild_resources = new PrintableTable(Resource.getString("TOTAL_RESOURCES", locale),
					Collections.emptyList(),
					Arrays.asList(Resource.getString("RAW_MATERIAL", locale), Resource.getString("AMOUNT", locale)),
					rows, Arrays.asList(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT));
			Speaker.say(message.getTextChannel(), prettyPrint(total_guild_resources));
		} else {
			Speaker.say(message.getTextChannel(), Resource.getString("RESOURCES_EMPTY", locale));
		}
	}

}
