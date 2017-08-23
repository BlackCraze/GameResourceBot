package de.blackcraze.grb.commands;

import net.dv8tion.jda.core.entities.TextChannel;

public class Speaker {

	public static class Reaction {
		public final static String SUCCESS = "✔";
		public final static String FAILURE = "✖";
	}

	public static void say(TextChannel textChannel, String text) {
		if (!textChannel.canTalk()) {
			System.err.printf("Can not talk in: %s/%s%n", textChannel.getGuild().getName(), textChannel.getName());
		}
		textChannel.sendMessage(text).queue();
	}

}
