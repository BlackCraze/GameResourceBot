package de.blackcraze.grb.commands;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.StockType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static de.blackcraze.grb.util.CommandUtils.*;
import static de.blackcraze.grb.util.InjectorUtils.*;
import static de.blackcraze.grb.util.PrintUtils.*;

@SuppressWarnings("unused")
public final class Commands {

	private Commands() {
	}


	public static void ping(Scanner scanner, Message message) {
		Speaker.say(message.getTextChannel(), Resource.getString("PONG", getResponseLocale(message)));
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
			unknownStocks = getMateDao().updateStocks(getOrCreateMate(message.getAuthor()), stocks);
		} catch (Exception e) {
			e.printStackTrace();
			message.addReaction(Speaker.Reaction.FAILURE).queue();
			return;
		}
		if (stocks.size() > 0) {
			if (!unknownStocks.isEmpty()) {
				Speaker.err(message, String.format(Resource.getString("DO_NOT_KNOW_ABOUT", responseLocale), unknownStocks.toString()));
			}
			if (unknownStocks.size() != stocks.size()) {
				message.addReaction(Speaker.Reaction.SUCCESS).queue();
			}
		} else {
			Speaker.err(message, Resource.getString("RESOURCES_EMPTY", responseLocale));
		}
	}

	public static void checkTypes(Scanner scanner, Message message) {
		Speaker.say(message.getTextChannel(), prettyPrintStockTypes(getStockTypeDao().findAll(), getResponseLocale(message)));
	}


	public static void check(Scanner scanner, Message message) {
		Optional<String> mateOrStockOptional = parseStockName(scanner);
		TextChannel textChannel = message.getTextChannel();
		if (!mateOrStockOptional.isPresent()) {
			List<Mate> mates = Collections.singletonList(getOrCreateMate(message.getAuthor()));
			Speaker.say(textChannel, prettyPrintMate(mates, getResponseLocale(message)));
		} else {
			List<Mate> mates = getMateDao().findByNameLike(mateOrStockOptional.get());
			if (!mates.isEmpty()) {
				Speaker.say(textChannel, prettyPrintMate(mates, getResponseLocale(message)));
			}
			List<StockType> types = getStockTypeDao().findByNameLike(mateOrStockOptional.get());
			if (!types.isEmpty()) {
				Speaker.say(textChannel, prettyPrintStocks(types, getResponseLocale(message)));
			}
			if (types.isEmpty() && mates.isEmpty()) {
				Speaker.say(textChannel,Resource.getString("RESOURCE_AND_USER_UNKNOWN", getResponseLocale(message)));
			}
		}
	}

	public static void help(Scanner scanner, Message message) {
		String response = Arrays.stream(Commands.class.getDeclaredMethods())
				.map(Method::getName)
				.collect(Collectors.joining(
						"\n  ",
						"```\n" + Resource.getString("COMMANDS", getResponseLocale(message)) + "\n  ",
						"```"));
		Speaker.say(message.getTextChannel(), response);
	}
}
