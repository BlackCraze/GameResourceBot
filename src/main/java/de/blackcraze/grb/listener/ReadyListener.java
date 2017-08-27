package de.blackcraze.grb.listener;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Speaker;
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
		StringBuilder b = new StringBuilder();
		b.append("Ich laufe auf: \n");
		for (Guild g : event.getJDA().getGuilds()) {
			b.append(g.getName()).append("\n");
			for(TextChannel channel : g.getTextChannelsByName(BotConfig.CHANNEL, true)) {
				sayHello(channel);
			}
		}
		System.out.println(b.toString());
	}

	@Override
	public void onResume(ResumedEvent event) {
		for (Guild guild : event.getJDA().getGuilds()) {
			for(TextChannel channel : guild.getTextChannelsByName(BotConfig.CHANNEL, true)) {
				sayHello(channel);
			}
		}
	}

	@Override
	public void onReconnect(ReconnectedEvent event) {
		for (Guild guild : event.getJDA().getGuilds()) {
			for(TextChannel channel : guild.getTextChannelsByName(BotConfig.CHANNEL, true)) {
				sayHello(channel);
			}		}
	}

	private void sayHello(TextChannel channel) {
		Speaker.say(channel, "Listening with prefix " + BotConfig.PREFIX);
	}

}
