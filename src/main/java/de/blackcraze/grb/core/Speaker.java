package de.blackcraze.grb.core;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class Speaker {

    public static class Reaction {
        public final static String SUCCESS = "✔";
        public final static String FAILURE = "✖";       
    }

    public static void say(TextChannel textChannel, String text) {
        say(textChannel, text, "", "");
    }

    public static void say(TextChannel textChannel, List<String> texts) {
        texts.forEach(text -> say(textChannel, text));
    }

    public static void sayCode(TextChannel textChannel, List<String> texts) {
        texts.forEach(text -> sayCode(textChannel, text));
    }

    public static void sayCode(TextChannel textChannel, String text) {
        say(textChannel, text, "```dsconfig\n", "\n```");
    }

    private static void say(TextChannel textChannel, String text, String prefix, String suffix) {
        if (StringUtils.isEmpty(text)) {
            return;
        }
        if (!textChannel.canTalk()) {
            System.err.printf("Can not talk in: %s/%s%n", textChannel.getGuild().getName(), textChannel.getName());
        }
        StringTokenizer t = new StringTokenizer(text, "\n", true);
        StringBuffer b = new StringBuffer(prefix);
        b.append(suffix);
        while (t.hasMoreTokens()) {
            String token = t.nextToken();
            if (b.length() + token.length() <= 2000) {
                b.insert(b.length() - suffix.length(), token);
            } else {
                textChannel.sendMessage(b.toString()).queue();
                b = new StringBuffer(prefix);
                b.append(token);
                b.append(suffix);
            }
        }
        textChannel.sendMessage(b.toString()).queue();
    }

    public static void err(Message message, String text) {
        message.addReaction(Reaction.FAILURE).queue();
        say(message.getTextChannel(), text);
    }

}
