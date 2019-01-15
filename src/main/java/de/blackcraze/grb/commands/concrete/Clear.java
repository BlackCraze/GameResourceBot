package de.blackcraze.grb.commands.concrete;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.CommandUtils.parseStockName;
import static de.blackcraze.grb.util.InjectorUtils.getMateDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.StockType;
import net.dv8tion.jda.core.entities.Message;

public class Clear implements BaseCommand {

    private static final String OLDER = "older";

    public void run(Scanner scanner, Message message) {
        try {
            Optional<String> mateOrStockOptional = parseStockName(scanner);

            final Locale locale = getResponseLocale(message);
            Mate mate = getMateDao().getOrCreateMate(message, locale);
            if (!mateOrStockOptional.isPresent()) {
                // If no member was selected, assume the user of the message.
                List<Mate> mates = Collections.singletonList(mate);
                clearMates(message, mates);
            } else {
                if ("all".equalsIgnoreCase(mateOrStockOptional.get())) {
                    BaseCommand.checkPublic(message);
                    // Select guild members.
                    clearAll();
                } else if (StringUtils.startsWithIgnoreCase(mateOrStockOptional.get(), OLDER)) {
                    BaseCommand.checkPublic(message);
                    clearOld(message, mateOrStockOptional.get(), locale);
                } else {
                    // Select only given member with exactly matching name.
                    List<Mate> mates = getMateDao().findByName(mateOrStockOptional.get());
                    if (!mates.isEmpty()) {
                        // Delete the stocks from defined members.
                        clearMates(message, mates);
                    } else {
                        // ELSE try the parameter as an Item.
                        String stock = Resource.guessItemKey(mateOrStockOptional.get(), locale);
                        Optional<StockType> stockType = getStockTypeDao().findByKey(stock);
                        // Try to delete the given name from stocks of current user.
                        stockType.ifPresent(aStockType -> getStockDao().delete(mate, aStockType));
                    }
                }
            }
            // Always respond to a bot request.
            message.addReaction(Speaker.Reaction.SUCCESS).queue();
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            e.printStackTrace();
            message.addReaction(Speaker.Reaction.FAILURE).queue();
        }
    }

    private void clearOld(Message message, String time, Locale locale) {
        time = StringUtils.substringAfter(time, OLDER).trim();
        if (StringUtils.isBlank(time)) {
            String info = Resource.getError("CLEAR_OLD_MISSING_FORMAT", locale);
            Speaker.err(message, info);
            throw new IllegalArgumentException();
        } else if (time.matches("^\\d+\\s?h?$")) {
            if (time.matches("^\\d+$")) {
                String info = Resource.getError("CLEAR_OLD_MISSING_UNIT", locale, time, Resource.getHeader("HOURS", locale));
                Speaker.say(message.getChannel(), info);
            }
            int hours = Integer.valueOf(time.substring(0, time.length() - 1));
            LocalDateTime limit =
                    LocalDateTime.now().minusHours(hours).truncatedTo(ChronoUnit.SECONDS);
            getStockDao().deleteOlderThan(limit);
        } else {
            String errorMsg = Resource.getError("CLEAR_OLD_UNKNOWN_UNIT", locale);
            Speaker.err(message, errorMsg);
            throw new IllegalArgumentException();
        }
    }

    private void clearMates(Message message, List<Mate> mates) {
        for (Mate aMate : mates) {
            getStockDao().deleteAll(aMate);
        }
    }

    private void clearAll() {
        List<Mate> mates = getMateDao().findByNameLike("%");
        for (Mate aMate : mates) {
            getStockDao().deleteAll(aMate);
        }
    }

}
