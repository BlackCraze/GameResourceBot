package de.blackcraze.grb.commands;

import de.blackcraze.grb.core.BotConfig;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;
import java.util.Optional;

public class Speaker {

	public static class Reaction {
		public final static String SUCCESS = "✔";
		public final static String FAILURE = "✖";
	}

	public static void say(List<TextChannel> channels, String text) {
		Optional<TextChannel> optionalChannel = channels.stream()
				.filter(channel -> BotConfig.CHANNEL.equalsIgnoreCase(channel.getName()))
				.findFirst();
		if (!optionalChannel.isPresent()) {
			System.err.printf("No channel found matching name:%s%n", BotConfig.CHANNEL);
			return;
		}
		TextChannel textChannel = optionalChannel.get();
		say(textChannel, text);
	}

	public static void say(TextChannel textChannel, String text) {
		if (!textChannel.canTalk()) {
			System.err.printf("Can not talk in: %s/%s%n", textChannel.getGuild().getName(), textChannel.getName());
		}
		textChannel.sendMessage(text).queue();
	}

}
