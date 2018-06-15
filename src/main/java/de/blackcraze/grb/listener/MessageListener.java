package de.blackcraze.grb.listener;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.commands.FileProcessor;
import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.util.CommandUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.SelfUser;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.Scanner;

import static de.blackcraze.grb.commands.BaseCommand.getCommandClasses;
import static de.blackcraze.grb.util.CommandUtils.parseAction;

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
        if (StringUtils.isNotEmpty(message.getContentRaw())) {
            Optional<Scanner> scannerOptional = CommandUtils.commandParser(message);
            if (!scannerOptional.isPresent()) {
                return;
            }

            Scanner scanner = scannerOptional.get();

            Optional<String> actionOptional = parseAction(scanner);

            Optional<Class<BaseCommand>> commandClassOptional = getCommandClasses()
                    .stream()
                    .filter(aClass -> aClass.getSimpleName().equalsIgnoreCase(actionOptional.orElse("help")))
                    .findFirst();

            if (!commandClassOptional.isPresent()) {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                return;
            }

            System.out.printf("%s:%s%n", message.getAuthor().getName(), message.getContentRaw());
            try {
                Class<? extends BaseCommand> commandClass = commandClassOptional.get();
                BaseCommand fakeInstance = commandClass.getConstructor().newInstance();
                commandClass
                        .getMethod("run", Scanner.class, Message.class)
                        .invoke(fakeInstance, scanner, message);
            } catch (Exception e) {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                System.out.println(e.getClass().getName());
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
