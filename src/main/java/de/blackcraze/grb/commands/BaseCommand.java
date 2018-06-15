package de.blackcraze.grb.commands;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;

import com.google.common.reflect.ClassPath;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;

public interface BaseCommand {

    void run(Scanner scanner, Message message);

    default String help(Message message) {
        return Resource.getHelp(this.getClass().getSimpleName(),
                getResponseLocale(message));
    }

    static void checkPublic(Message message) {
        if (!ChannelType.TEXT.equals(message.getChannelType())) {
            message.addReaction(Speaker.Reaction.FAILURE).queue();
            Speaker.say(message.getChannel(),
                    Resource.getString("PUBLIC_COMMAND_ONLY", getResponseLocale(message)));
            throw new IllegalStateException("public command only");
            // TODO MORE SOLID IMPLEMENTATION
        }
    }

    static Collection<Class<BaseCommand>> getCommandClasses() {
        ClassPath classPath;
        try {
            classPath = ClassPath.from(ClassLoader.getSystemClassLoader());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        Collection<Class<BaseCommand>> classes =
                classPath.getTopLevelClassesRecursive("de.blackcraze.grb.commands").stream()
                        .map(aClassInfo -> ((Class<BaseCommand>) aClassInfo.load()))
                        .collect(Collectors.toList());
        return classes.stream().filter(BaseCommand.class::isAssignableFrom)
                .collect(Collectors.toList());
    }
}
