package de.blackcraze.grb.listener;

import static de.blackcraze.grb.util.CommandUtils.parseAction;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import de.blackcraze.grb.commands.Commands;
import de.blackcraze.grb.commands.FileProcessor;
import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.util.CommandUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.SelfUser;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    public MessageListener() {
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (isFromMyself(event)) {
            return; // cancel on own messages
        }
        Message message = event.getMessage();
        String listeningChannel = BotConfig.getConfig().CHANNEL;
        String messageChannel = message.getChannel().getName();
        if (!listeningChannel.equalsIgnoreCase(messageChannel)) {
            return;
        }
        handleMessage(message);
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if (isFromMyself(event)) {
            return; // cancel on own messages
        }
        handleMessage(event.getMessage());
    }

    private void handleMessage(Message message) {
        if (StringUtils.isNotEmpty(message.getContent())) {
            Optional<Scanner> scannerOptional = CommandUtils.commandParser(message);
            if (!scannerOptional.isPresent()) {
                return;
            }

            Scanner scanner = scannerOptional.get();

            Optional<String> actionOptional = parseAction(scanner);

            Optional<Method> methodOptional = Arrays.stream(Commands.class.getDeclaredMethods())
                    .filter(aMethod -> aMethod.getName().equalsIgnoreCase(actionOptional.orElse("help"))).findFirst();

            if (!methodOptional.isPresent()) {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                return;
            }

            System.out.printf("%s:%s%n", message.getAuthor().getName(), message.getContent());
            try {
                methodOptional.get().invoke(null, scanner, message);
            } catch (Exception e) {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                e.printStackTrace();
                return;
            }
        }
        if (!message.getAttachments().isEmpty()) {
            FileProcessor.ocrImages(message);
        }
    }

    private boolean isFromMyself(PrivateMessageReceivedEvent event) {
        SelfUser selfUser = event.getJDA().getSelfUser();
        return event.getAuthor().getId().equals(selfUser.getId());
    }

    private boolean isFromMyself(GuildMessageReceivedEvent event) {
        SelfUser selfUser = event.getJDA().getSelfUser();
        return event.getAuthor().getId().equals(selfUser.getId());
    }

}
