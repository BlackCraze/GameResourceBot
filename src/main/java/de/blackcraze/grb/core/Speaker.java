package de.blackcraze.grb.core;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;


public class Speaker {

    public static class Reaction {
        public final static String SUCCESS = "✅";
        public final static String FAILURE = "❌";
    }

    public static void say(MessageChannel channel, String text) {
        say(channel, text, "", "");
    }

    public static void say(MessageChannel channel, List<String> texts) {
        texts.forEach(text -> say(channel, text));
    }

    public static void sayCode(MessageChannel channel, List<String> texts) {
        texts.forEach(text -> sayCode(channel, text));
    }

    public static void sayCode(MessageChannel channel, String text) {
        say(channel, text, "```dsconfig\n", "\n```");
    }

    private static void say(MessageChannel channel, String text, String prefix, String suffix) {
        if (StringUtils.isEmpty(text)) {
            return;
        }
        StringTokenizer t = new StringTokenizer(text, "\n", true);
        StringBuffer b = new StringBuffer(prefix);
        b.append(suffix);
        while (t.hasMoreTokens()) {
            String token = t.nextToken();
            if (b.length() + token.length() <= 2000) {
                b.insert(b.length() - suffix.length(), token);
            } else {
                channel.sendMessage(b.toString()).queue();
                b = new StringBuffer(prefix);
                b.append(token);
                b.append(suffix);
            }
        }
        channel.sendMessage(b.toString()).queue();
    }

    public static void err(Message message, String text) {
        message.addReaction(Reaction.FAILURE).queue();
        say(message.getChannel(), text);
    }

}
