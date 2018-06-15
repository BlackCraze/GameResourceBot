package de.blackcraze.grb.commands.concrete;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.CommandUtils.parseStockName;
import static de.blackcraze.grb.util.InjectorUtils.getStockDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeGroupDao;
import static de.blackcraze.grb.util.PrintUtils.prettyPrint;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.PrintableTable;
import de.blackcraze.grb.model.entity.StockType;
import de.blackcraze.grb.model.entity.StockTypeGroup;
import de.blackcraze.grb.util.StockTypeComparator;
import de.blackcraze.grb.util.wagu.Block;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;
import net.dv8tion.jda.core.entities.Message;

public class Total implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        Optional<String> nameOptional = parseStockName(scanner);
        List<StockType> types;
        Locale locale = getResponseLocale(message);
        if (!nameOptional.isPresent()) {
            types = getStockTypeDao().findAll(locale);
        } else {
            types = getStockTypeDao().findByNameLike(nameOptional.get(), locale);
            if (types.isEmpty()) {
                Optional<StockTypeGroup> groupOpt =
                        getStockTypeGroupDao().findByName(nameOptional.get());
                if (groupOpt.isPresent()) {
                    if (groupOpt.get().getTypes() == null || groupOpt.get().getTypes().isEmpty()) {
                        String msg = String.format(Resource.getString("GROUP_EMPTY", locale),
                                nameOptional.get());
                        Speaker.say(message.getChannel(), msg);
                        return;
                    } else {
                        types = groupOpt.get().getTypes();
                        types.sort(new StockTypeComparator(locale));
                    }
                } else {
                    Speaker.say(message.getChannel(),
                            Resource.getString("RESOURCE_UNKNOWN", locale));
                    return;
                }
            }
        }

        List<List<String>> rows = new ArrayList<>();
        for (StockType stockType : types) {
            long total = getStockDao().getTotalAmount(stockType);
            if (total > 0) {
                String localisedStockName = Resource.getItem(stockType.getName(), locale);
                rows.add(Arrays.asList(localisedStockName, String.format(locale, "%,d", total)));
            }
        }
        if (!rows.isEmpty()) {
            PrintableTable total_guild_resources = new PrintableTable(
                    Resource.getString("TOTAL_RESOURCES", locale), Collections.emptyList(),
                    Arrays.asList(Resource.getString("RAW_MATERIAL", locale),
                            Resource.getString("AMOUNT", locale)),
                    rows, Arrays.asList(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT));
            Speaker.sayCode(message.getChannel(), prettyPrint(total_guild_resources));
        } else {
            Speaker.say(message.getChannel(), Resource.getString("RESOURCES_EMPTY", locale));
        }
    }
}
