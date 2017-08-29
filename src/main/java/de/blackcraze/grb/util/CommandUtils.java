package de.blackcraze.grb.util;

import de.blackcraze.grb.core.BotConfig;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.SelfUser;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CommandUtils {

    public static Map<String, Long> parseStocks(Message message) {
        String rows[] = message.getRawContent().split("\\r?\\n");
        Map<String, Long> stocks = new HashMap<>(rows.length);
        for (int i = 0; i < rows.length; i++) {
            boolean firstRow = i == 0;
            String row = rows[i];
            Long amount = parseAmount(row, firstRow);
            String name = parseStockName(row, firstRow);
            if (!Objects.isNull(amount) && !Objects.isNull(name)) {
                stocks.put(name, amount);
            }
        }
        return stocks;
    }

    public static String parseAction(Message message) {
        return parse(message.getContent(), 1, false);
    }

    public static String parseStockName(String row, boolean firstRow) {
        return parse(row, 0, firstRow);
    }

    public static Long parseAmount(String row, boolean firstRow) {
        try {
            return Long.valueOf(parse(row, 1, firstRow));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String parse(String row, int index) {
        return parse(row, index, false);
    }

    public static String parse(String row, int index, boolean firstRow) {
        index = firstRow ? index + 2 : index;
        String[] split = row.split(" ");
        if (index < split.length) {
            return split[index];
        }
        return null;
    }

    public static boolean botMentioned(Message message) {
        SelfUser selfUser = message.getJDA().getSelfUser();
        String prefix = BotConfig.getConfig(message.getGuild()).PREFIX;
        String messageStartWord = message.getContent().split(" ")[0];
        boolean prefixCheck = prefix.equalsIgnoreCase(messageStartWord);
        boolean mentionCheck = message.isMentioned(selfUser);
        return prefixCheck || mentionCheck;
    }

    public static Locale getResponseLocale(Message message) {
        Guild guild = message.getGuild();
        BotConfig.ServerConfig config = BotConfig.getConfig(guild);
        String langString = config.LANGUAGE;
        return new Locale(langString);
    }
}
