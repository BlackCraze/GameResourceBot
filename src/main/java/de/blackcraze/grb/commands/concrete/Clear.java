package de.blackcraze.grb.commands.concrete;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.StockType;
import net.dv8tion.jda.core.entities.Message;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.CommandUtils.parseStockName;
import static de.blackcraze.grb.util.InjectorUtils.getMateDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;

public class Clear implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        Optional<String> mateOrStockOptional = parseStockName(scanner);
        List<Mate> mates;
        String clearReaction;

        final Locale locale = getResponseLocale(message);
        Mate mate = getMateDao().getOrCreateMate(message, locale);
        if (!mateOrStockOptional.isPresent()) {
            // if no member was selected assume the user of the message.
            mates = Collections.singletonList(mate);
        } else {
            if ("all".equalsIgnoreCase(mateOrStockOptional.get())) {
                BaseCommand.checkPublic(message);
                // select guild members
                mates = getMateDao().findByNameLike("%");
            } else {
                // select only given member with exact matching name.
                mates = getMateDao().findByName(mateOrStockOptional.get());
                if (!mates.isEmpty()) {
                    BaseCommand.checkPublic(message);
                }
            }
        }
        // Delete the stocks from defined members.
        // otherwise try the parameter as an Item.
        if (!mates.isEmpty()) {
            for (Mate aMate : mates) {
                getStockDao().deleteAll(aMate);
            }
            clearReaction = Speaker.Reaction.SUCCESS;
        } else {
            String stockIdentifier = Resource.getItemKey(mateOrStockOptional.get(), locale);
            Optional<StockType> stockType = getStockTypeDao().findByKey(stockIdentifier);
            // Try to delete the given name from stocks of current user.
            stockType.ifPresent(aStockType -> getStockDao().delete(mate, aStockType));
            clearReaction = Speaker.Reaction.SUCCESS;
        }
        // Always response to a bot request.
        message.addReaction(clearReaction).queue();
    }

}
