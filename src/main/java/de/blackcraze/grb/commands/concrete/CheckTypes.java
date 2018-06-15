package de.blackcraze.grb.commands.concrete;

import static de.blackcraze.grb.util.CommandUtils.parseStockName;
import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;
import static de.blackcraze.grb.util.PrintUtils.prettyPrintStockTypes;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.model.entity.StockType;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;
import net.dv8tion.jda.core.entities.Message;

public class CheckTypes implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        Optional<String> stockNameOptional = parseStockName(scanner);
        Locale locale = getResponseLocale(message);
        List<StockType> stocks = stockNameOptional.isPresent()
                ? getStockTypeDao().findByNameLike(stockNameOptional.get(), locale)
                : getStockTypeDao().findAll(locale);
        Speaker.sayCode(message.getChannel(), prettyPrintStockTypes(stocks, locale));
    }

}
