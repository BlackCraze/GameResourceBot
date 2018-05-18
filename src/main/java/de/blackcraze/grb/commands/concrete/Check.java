package de.blackcraze.grb.commands.concrete;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.CommandUtils.parseStockName;
import static de.blackcraze.grb.util.InjectorUtils.getMateDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeGroupDao;
import static de.blackcraze.grb.util.PrintUtils.prettyPrintMate;
import static de.blackcraze.grb.util.PrintUtils.prettyPrintStocks;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.StockType;
import de.blackcraze.grb.model.entity.StockTypeGroup;
import de.blackcraze.grb.util.StockTypeComparator;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class Check implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        Optional<String> nameOptional = parseStockName(scanner);
        MessageChannel channel = message.getChannel();
        Locale locale = getResponseLocale(message);
        if (!nameOptional.isPresent()) {
            Mate mate = getMateDao().getOrCreateMate(message, getResponseLocale(message));
            List<Mate> mates = Collections.singletonList(mate);
            Speaker.sayCode(channel, prettyPrintMate(mates, locale));
        } else {
            List<Mate> mates = getMateDao().findByNameLike(nameOptional.get());
            if (!mates.isEmpty()) {
                Speaker.sayCode(channel, prettyPrintMate(mates, locale));
                return;
            }
            final StockTypeComparator comp = new StockTypeComparator(locale);
            List<StockType> types = getStockTypeDao().findByNameLike(nameOptional.get(), locale);
            types.sort(comp);
            if (!types.isEmpty()) {
                Speaker.sayCode(channel, prettyPrintStocks(types, locale));
                return;
            }
            Optional<StockTypeGroup> groupOpt =
                    getStockTypeGroupDao().findByName(nameOptional.get());
            if (groupOpt.isPresent()) {
                List<StockType> groupTypes = groupOpt.get().getTypes();
                if (!groupTypes.isEmpty()) {
                    groupTypes.sort(comp);
                    Speaker.sayCode(channel, prettyPrintStocks(groupTypes, locale));
                } else {
                    String msg = String.format(Resource.getString("GROUP_EMPTY", locale),
                            nameOptional.get());
                    Speaker.say(channel, msg);
                }
            } else {
                Speaker.say(channel, Resource.getString("RESOURCE_AND_USER_UNKNOWN", locale));
            }
        }
    }

}
