package de.blackcraze.grb.commands;

import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;
import java.util.Optional;

public class Speaker {

	public static final String C_NAME = "statistik";

	public static class Reaction {
		public final static String SUCCESS = "✔";
		public final static String FAILURE = "✖";
	}

	public static void say(List<TextChannel> channels, String text) {
		Optional<TextChannel> optionalChannel = channels.stream()
				.filter(channel -> C_NAME.equalsIgnoreCase(channel.getName()))
				.findFirst();
		if (!optionalChannel.isPresent()) {
			System.err.printf("No channel found matching name:%s%n", C_NAME);
			return;
		}
		TextChannel textChannel = optionalChannel.get();
		if (!textChannel.canTalk()) {
			System.err.printf("Can not talk in: %s/%s%n", textChannel.getGuild().getName(), textChannel.getName());
		}
		textChannel.sendMessage(text).queue();
	}

}
