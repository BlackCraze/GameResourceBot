package de.blackcraze.grb.commands;

import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class Speaker {

	private static final String C_NAME = "statistik";

	public static void errorReaction(JDA jda, Message message) {
		reaction(jda, message, "✖");
	}

	public static void checkReaction(JDA jda, Message message) {
		reaction(jda, message, "✔");
	}

	public static void reaction(JDA jda, Message message, String string) {
		List<TextChannel> channels = jda.getTextChannelsByName(C_NAME, true);
		for (TextChannel channel : channels) {
			if (channel.canTalk()) {
				message.addReaction(string).queue();
				break;
			} else {
				System.err.println("can not talk in: " + channel.getGuild().getName() + "/" + channel.getName());
			}
		}
	}

	public static void say(List<TextChannel> channels, String text) {
		for (TextChannel channel : channels) {
			if (channel.getName().equalsIgnoreCase(C_NAME)) {
				if (channel.canTalk()) {
					channel.sendMessage(text).queue();
					break; // only say it once ;-)
				} else {
					System.err.println("can not talk in: " + channel.getGuild().getName() + "/" + channel.getName());
				}
			}
		}
	}

}
