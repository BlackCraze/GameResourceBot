package de.blackcraze.grb.commands;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Configurable;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.StockType;
import de.blackcraze.grb.util.CommandUtils;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.blackcraze.grb.util.CommandUtils.parseStockName;
import static de.blackcraze.grb.util.CommandUtils.parseStocks;
import static de.blackcraze.grb.util.InjectorUtils.getMateDao;
import static de.blackcraze.grb.util.InjectorUtils.getOrCreateMate;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;
import static de.blackcraze.grb.util.PrintUtils.prettyPrintMate;
import static de.blackcraze.grb.util.PrintUtils.prettyPrintStockTypes;
import static de.blackcraze.grb.util.PrintUtils.prettyPrintStocks;

@SuppressWarnings("unused")
public final class Commands {

	private Commands() {
	}


	public static void ping(Message message) {
		Speaker.say(message.getTextChannel(), Resource.getString("PONG"));
	}

	public static void config(Message message) {
		String config_action = CommandUtils.parse(message.getContent(), 2);
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
			String field = CommandUtils.parse(message.getContent(), 3);
			String value = CommandUtils.parse(message.getContent(), 4);
			if (field == null || value == null) {
				message.addReaction(Speaker.Reaction.FAILURE).queue();
				return;
			}
			try {
				Field declaredField = BotConfig.class.getDeclaredField(field);
				assert declaredField.isAnnotationPresent(Configurable.class);
				assert String.class.equals(declaredField.getType());
				declaredField.set(null, value);
				message.addReaction(Speaker.Reaction.SUCCESS).queue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void status(Message message) {
		Runtime rt = Runtime.getRuntime();
		long total = rt.totalMemory();
		long free = rt.freeMemory();
		long used = total - free;

		String memConsume = String.format("~~ My Memory | Total:%,d | Used:%,d, Free:%,d", total, used, free);
		System.out.println(memConsume);
		Speaker.say(message.getTextChannel(), memConsume);
	}

	public static void update(Message message) {
		try {
			Map<String, Long> stocks = parseStocks(message);
			List<String> unknown = getMateDao().updateStocks(getOrCreateMate(message.getAuthor()), stocks);
			if (stocks.size() > 0) {
				if (!unknown.isEmpty()) {
					Speaker.err(message, Resource.getString("DO_NOT_KNOW_ABOUT"));
				}
				if (unknown.size() != stocks.size()) {
					message.addReaction(Speaker.Reaction.SUCCESS).queue();
				}
			} else {
				Speaker.err(message, Resource.getString("RESOURCES_EMPTY"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.addReaction(Speaker.Reaction.FAILURE).queue();
		}
	}

	public static void checkTypes(Message message) {
		Speaker.say(message.getTextChannel(), prettyPrintStockTypes(getStockTypeDao().findAll()));
	}

	public static void newType(Message message) {
		String stockName = parseStockName(message.getContent(), true);
		if (stockName != null && !stockName.isEmpty()) {
			if (getStockTypeDao().findByName(stockName) != null) {
				Speaker.err(message, Resource.getString("ALREADY_KNOW"));
			} else {
				StockType type = new StockType();
				type.setName(stockName);
				type.setPrice(0);
				getStockTypeDao().save(type);
				message.addReaction(Speaker.Reaction.SUCCESS).queue();
			}
		}
	}

	public static void deleteType(Message message) {

		String stockName = parseStockName(message.getContent(), true);
		if (stockName != null && !stockName.isEmpty()) {
			StockType stockType = getStockTypeDao().findByName(stockName);
			if (stockType != null) {
				getStockTypeDao().delete(stockType);
				message.addReaction(Speaker.Reaction.SUCCESS).queue();
			} else {
				Speaker.err(message, Resource.getString("RESOURCE_UNKNOWN"));
			}
		}
	}


	public static void check(Message message) {
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
				Speaker.say(message.getTextChannel(),Resource.getString("RESOURCE_AND_USER_UNKNOWN"));
			}
		}
	}
}
