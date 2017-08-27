package de.blackcraze.grb.util;

import de.blackcraze.grb.core.BotConfig;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.SelfUser;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;
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

    public static boolean botMentioned(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        SelfUser selfUser = event.getJDA().getSelfUser();
        boolean prefixCheck = BotConfig.PREFIX.equalsIgnoreCase(message.getContent());
        boolean mentionCheck = message.isMentioned(selfUser);
        return prefixCheck || mentionCheck;
        // for (Role role : event.getMessage().getMentionedRoles()) {
        // if (role.getName().equalsIgnoreCase("bot")) {
        // return true;
        // }
        // }
        // return false;
    }
}
