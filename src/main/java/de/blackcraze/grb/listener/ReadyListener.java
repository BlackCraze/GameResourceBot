package de.blackcraze.grb.listener;

import java.util.stream.Collectors;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.util.CommandUtils;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.ResumedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {

	public ReadyListener() {
	}

	@Override
	public void onReady(ReadyEvent event) {
		System.out.println(event.getJDA().getGuilds().stream().map(Guild::getName)
				.collect(Collectors.joining(", ", "Listening on: ", "")));

		initialiseServers(event.getJDA());
	}

	@Override
	public void onResume(ResumedEvent event) {
		initialiseServers(event.getJDA());
	}

	@Override
	public void onReconnect(ReconnectedEvent event) {
		initialiseServers(event.getJDA());
	}

	private void initialiseServers(JDA jda) {
		for (Guild guild : jda.getGuilds()) {
			String listenChannel = BotConfig.getConfig(guild).CHANNEL;
			for (TextChannel channel : guild.getTextChannelsByName(listenChannel, true)) {
				initialise(channel);
			}
		}
	}

	private void initialise(TextChannel channel) {
		Speaker.say(channel, Resource.getString("INIT", CommandUtils.getResponseLocale(channel))
				+ "`" + BotConfig.getConfig(channel.getGuild()).PREFIX + "`");
	}

}
