package de.blackcraze.grb.listener;

import de.blackcraze.grb.commands.Commands;
import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.util.CommandUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import static de.blackcraze.grb.util.CommandUtils.parseAction;

public class MessageListener extends ListenerAdapter {


	public MessageListener() {
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		Message message = event.getMessage();
		if (!BotConfig.CHANNEL.equalsIgnoreCase(message.getTextChannel().getName())) {
			return;
		}
		if (!CommandUtils.botMentioned(event)) {
			return;
		}
		String action = parseAction(message);

		Optional<Method> methodOptional = Arrays.stream(Commands.class.getDeclaredMethods())
				.filter(aMethod -> aMethod.getName().equalsIgnoreCase(action))
				.findFirst();

		if (!methodOptional.isPresent()) {
			message.addReaction(Speaker.Reaction.FAILURE).queue();
			return;
		}

		try {
			methodOptional.get().invoke(null, message);
		} catch (Exception e) {
			message.addReaction(Speaker.Reaction.FAILURE).queue();
			e.printStackTrace();
		}

	}

}
