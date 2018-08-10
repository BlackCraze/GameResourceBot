package de.blackcraze.grb.commands;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.google.common.reflect.ClassPath;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;

public interface BaseCommand {

    void run(Scanner scanner, Message message);

    default String help(Message message) {
        // NEXT 3 LINES CREATED BY @DANGERCROW TO MAKE DESCRIPTIVE HELP SYSTEM WORK
        String className = this.getClass().getSimpleName();
        String key = className.toUpperCase();
        return String.format(Resource.getHelp(key, getResponseLocale(message)),
                BotConfig.getConfig().PREFIX, "#" + BotConfig.getConfig().CHANNEL);
    }

    static void checkPublic(Message message) {
        if (!ChannelType.TEXT.equals(message.getChannelType())) {
            Speaker.err(message,
                    String.format(
                            Resource.getError("PUBLIC_COMMAND_ONLY", getResponseLocale(message)),
                            "#" + BotConfig.getConfig().CHANNEL));
            throw new IllegalStateException("Public command only");
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
