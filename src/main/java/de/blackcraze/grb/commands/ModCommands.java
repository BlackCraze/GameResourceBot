package de.blackcraze.grb.commands;

import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.entity.StockType;
import net.dv8tion.jda.core.entities.Message;

import java.util.Objects;
import java.util.Optional;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.CommandUtils.parseStockName;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;

@SuppressWarnings("unused")
public class ModCommands {
    public static void newType(Message message) {
		String stockName = parseStockName(message.getContent(), true);
		if (!Objects.isNull(stockName) && !stockName.isEmpty()) {
			if (getStockTypeDao().findByName(stockName).isPresent()) {
				Speaker.err(message, Resource.getString("ALREADY_KNOW", getResponseLocale(message)));
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
			Optional<StockType> stockType = getStockTypeDao().findByName(stockName);
			if (stockType.isPresent()) {
				getStockTypeDao().delete(stockType.get());
				message.addReaction(Speaker.Reaction.SUCCESS).queue();
			} else {
				Speaker.err(message, Resource.getString("RESOURCE_UNKNOWN", getResponseLocale(message)));
			}
		}
    }
}
