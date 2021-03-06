package de.blackcraze.grb.listener;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.util.CommandUtils;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDA.Status;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ResumedEvent;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {

    public ReadyListener() {}

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
            String listenChannel = BotConfig.getConfig().CHANNEL;
            for (TextChannel channel : guild.getTextChannelsByName(listenChannel, true)) {
                initialise(channel);
            }
        }
    }

    private void initialise(TextChannel channel) {
        Speaker.say(channel, Resource.getInfo("INIT", CommandUtils.getDefaultLocale(),
                BotConfig.getConfig().PREFIX));
    }

    /* Send a goodbye message when the shutting down event is triggered. */

    @Override
    public void onStatusChange(StatusChangeEvent event) {
        if (event.getNewStatus() == Status.SHUTTING_DOWN) {
            goodbyeServers(event.getJDA());
        }
    }

    private void goodbyeServers(JDA jda) {
        for (Guild guild : jda.getGuilds()) {
            String listenChannel = BotConfig.getConfig().CHANNEL;
            for (TextChannel channel : guild.getTextChannelsByName(listenChannel, true)) {
                goodbyeMessage(channel);
            }
        }
    }

    private void goodbyeMessage(TextChannel channel) {
        Speaker.say(channel, Resource.getInfo("BYE_MSG", CommandUtils.getDefaultLocale()));
    }
}
