package de.blackcraze.grb.commands;

import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.entity.StockType;
import net.dv8tion.jda.core.entities.Message;

import java.util.Optional;
import java.util.Scanner;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.CommandUtils.parseStockName;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;

@SuppressWarnings("unused")
public class ModCommands {
    public static void newType(Scanner scanner, Message message) {
		Optional<String> stockName = parseStockName(scanner);
		if (stockName.isPresent()) {
			if (getStockTypeDao().findByName(stockName.get()).isPresent()) {
				Speaker.err(message, Resource.getString("ALREADY_KNOW", getResponseLocale(message)));
			} else {
				StockType type = new StockType();
				type.setName(stockName.get());
				type.setPrice(0);
				getStockTypeDao().save(type);
				message.addReaction(Speaker.Reaction.SUCCESS).queue();
			}
		}
    }

    public static void deleteType(Scanner scanner, Message message) {

		Optional<String> stockName = parseStockName(scanner);
		if (stockName.isPresent()) {
			Optional<StockType> stockType = getStockTypeDao().findByName(stockName.get());
			if (stockType.isPresent()) {
				getStockTypeDao().delete(stockType.get());
				message.addReaction(Speaker.Reaction.SUCCESS).queue();
			} else {
				Speaker.err(message, Resource.getString("RESOURCE_UNKNOWN", getResponseLocale(message)));
			}
		}
    }
}
