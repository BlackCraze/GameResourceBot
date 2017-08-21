package de.blackcraze.grb.listener;

import java.util.List;

import com.google.inject.Injector;

import de.blackcraze.grb.commands.Speaker;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.ResumedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {

	private Injector in;

	@SuppressWarnings("unused")
	private ReadyListener() {
	}

	public ReadyListener(Injector injector) {
		this.in = injector;
	}

	@Override
	public void onReady(ReadyEvent event) {
		StringBuilder b = new StringBuilder();
		b.append("Ich laufe auf: \n");
		for (Guild g : event.getJDA().getGuilds()) {
			g.getTextChannelsByName("statistik", true);
			b.append(g.getName() + "\n");
			sayHelloInAllStats(g.getTextChannels());
		}
		System.out.println(b.toString());
	}

	@Override
	public void onResume(ResumedEvent event) {
		for (Guild guild : event.getJDA().getGuilds()) {
			sayHelloInAllStats(guild.getTextChannels());
		}
	}

	@Override
	public void onReconnect(ReconnectedEvent event) {
		for (Guild guild : event.getJDA().getGuilds()) {
			sayHelloInAllStats(guild.getTextChannels());
		}
	}

	private final void sayHelloInAllStats(List<TextChannel> channels) {
//		Speaker.say(channels, "Hallo - da bin ich wieder =)");
	}

}
