package de.blackcraze.grb.commands.concrete;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.entity.Mate;
import net.dv8tion.jda.core.entities.Message;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.CommandUtils.parseStocks;
import static de.blackcraze.grb.util.InjectorUtils.getMateDao;

public class Update implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        Locale responseLocale = getResponseLocale(message);
        Map<String, Long> stocks = parseStocks(scanner, responseLocale);
        internalUpdate(message, responseLocale, stocks);
    }

    public static void internalUpdate(Message message, Locale locale, Map<String, Long> stocks) {
        try {
            Mate mate = getMateDao().getOrCreateMate(message, getResponseLocale(message));
            List<String> unknownStocks = getMateDao().updateStocks(mate, stocks);
            if (stocks.size() > 0) {
                if (!unknownStocks.isEmpty()) {
                    Speaker.err(message,
                            String.format(Resource.getString("DO_NOT_KNOW_ABOUT", locale),
                                    unknownStocks.toString()));
                }
                if (unknownStocks.size() != stocks.size()) {
                    message.addReaction(Speaker.Reaction.SUCCESS).queue();
                }
            } else {
                Speaker.err(message, Resource.getString("RESOURCES_EMPTY", locale));
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.addReaction(Speaker.Reaction.FAILURE).queue();
        }
    }

}
