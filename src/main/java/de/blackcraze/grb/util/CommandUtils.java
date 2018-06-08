package de.blackcraze.grb.util;

import static de.blackcraze.grb.util.InjectorUtils.getMateDao;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.Device;
import de.blackcraze.grb.model.entity.Mate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.SelfUser;
import org.apache.commons.lang3.StringUtils;

public class CommandUtils {

    public static Map<String, Long> parseStocks(Scanner scanner, Locale responseLocale) {
        Map<String, Long> stocks = new HashMap<>();
        while (true) {
            Optional<String> stockName = parseStockName(scanner);
            Optional<Long> stockAmount = parseAmount(scanner);
            if (!stockName.isPresent() || !stockAmount.isPresent()) {
                break;
            }
            try {
                String stockIdentifier = Resource.getItemKey(stockName.get(), responseLocale);
                stocks.put(stockIdentifier, stockAmount.get());
            } catch (Exception e) {
                // we add the clear text stock name with an identifier value
                // this way we can show the user an error message showing
                // errored type names
                stocks.put(stockName.get(), Long.MIN_VALUE);
                System.err.println(e);
            }
        }
        return stocks;
    }

    public static List<String> parseGroupName(Scanner scanner) {
        List<String> result = new ArrayList<>();
        while (scanner.hasNext()) {
            result.add(scanner.next());
        }
        return result;
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
        String prefix = BotConfig.getConfig().PREFIX;
        String messageStartWord = message.getContentRaw().split(" ")[0];
        boolean prefixCheck = prefix.equalsIgnoreCase(messageStartWord);
        boolean mentionCheck = message.isMentioned(selfUser) && !message.mentionsEveryone();
        return prefixCheck || mentionCheck;
    }

    public static Optional<Scanner> commandParser(Message message) {
        boolean pm = message.getChannelType().equals(ChannelType.PRIVATE);
        if (!botMentioned(message) && !pm) {
            return Optional.empty();
        }
        Scanner scanner = new Scanner(message.getContentRaw());
        if (pm) {
            // skip the bot prefix if used
            if (scanner.hasNext(BotConfig.getConfig().PREFIX)) {
                scanner.next();
            }
            System.out.println("Mentioned with prefix: " + "private message");
        } else {
            String botPrefix = scanner.next();
            System.out.println("Mentioned with prefix: " + botPrefix);
        }
        return Optional.of(scanner);
    }

    public static Optional<String> parseAction(Scanner scanner) {
        if (!scanner.hasNext()) {
            return Optional.empty();
        }
        return Optional.of(scanner.next());
    }

    public static Locale getResponseLocale(Message message) {
        Locale channelLocale = getDefaultLocale();
        Mate mate = getMateDao().getOrCreateMate(message, channelLocale);
        if (mate != null && !StringUtils.isEmpty(mate.getLanguage())) {
            return new Locale(mate.getLanguage());
        }
        return channelLocale;
    }

    public static Device getMateDevice(Message message) {
        Mate mate = getMateDao().getOrCreateMate(message, getDefaultLocale());
        return mate.getDevice();
    }

    public static Locale getDefaultLocale() {
        try {
            return new Locale(BotConfig.getConfig().LANGUAGE);
        } catch (Exception e) {
            return Locale.ENGLISH;
        }
    }

}
