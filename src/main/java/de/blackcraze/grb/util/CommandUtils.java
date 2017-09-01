package de.blackcraze.grb.util;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.i18n.Resource;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.SelfUser;

import java.util.*;

public class CommandUtils {

    public static Map<String, Long> parseStocks(Scanner scanner, Locale responseLocale) {
        Map<String, Long> stocks = new HashMap<>();
        while (true) {
            Optional<String> stockName = parseStockName(scanner);
            Optional<Long> stockAmount = parseAmount(scanner);
            if (!stockName.isPresent() || !stockAmount.isPresent()) {
                break;
            }
            String stockIdentifier = Resource.getItemKey(stockName.get(), responseLocale);
            stocks.put(stockIdentifier, stockAmount.get());
        }
        return stocks;
    }

    public static Optional<String> parseStockName(Scanner scanner) {
        if (!scanner.hasNext()) {
            return Optional.empty();
        }
        StringBuilder stockName = new StringBuilder();
        stockName.append(scanner.next());
        while (scanner.hasNext() && !scanner.hasNextLong()) {
            stockName.append(" ");
            stockName.append(scanner.next());
        }
        return Optional.of(stockName.toString());
    }

    public static Optional<Long> parseAmount(Scanner scanner) {
        if (!scanner.hasNextLong()) {
            return Optional.empty();
        }
        return Optional.of(scanner.nextLong());
    }

    public static boolean botMentioned(Message message) {
        SelfUser selfUser = message.getJDA().getSelfUser();
        String prefix = BotConfig.getConfig(message.getGuild()).PREFIX;
        String messageStartWord = message.getContent().split(" ")[0];
        boolean prefixCheck = prefix.equalsIgnoreCase(messageStartWord);
        boolean mentionCheck = message.isMentioned(selfUser) && !message.mentionsEveryone();
        return prefixCheck || mentionCheck;
    }

    public static Optional<Scanner> commandParser(Message message) {
        if (!botMentioned(message)) {
            return Optional.empty();
        }
        Scanner scanner = new Scanner(message.getContent());
        String botPrefix = scanner.next();
        System.out.println("Mentioned with prefix: " + botPrefix);
        return Optional.of(scanner);
    }

    public static Optional<String> parseAction(Scanner scanner) {
        if(!scanner.hasNext()) {
            return Optional.empty();
        }
        return Optional.of(scanner.next());
    }

    public static Locale getResponseLocale(Message message) {
        return getResponseLocale(message.getTextChannel());
    }

    public static Locale getResponseLocale(Channel channel) {
        Guild guild = channel.getGuild();
        BotConfig.ServerConfig config = BotConfig.getConfig(guild);
        String langString = config.LANGUAGE;
        return new Locale(langString);
    }
}
