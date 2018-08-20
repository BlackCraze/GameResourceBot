package de.blackcraze.grb.commands.concrete;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Scanner;
import java.util.stream.Collectors;
import net.dv8tion.jda.core.entities.Message;

public class Help implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        Locale locale = getResponseLocale(message);
        if (!scanner.hasNext()) {
            Speaker.say(message.getChannel(), getHelpFromResource(locale, "GENERAL"));
        } else {
            String command = scanner.next();
            try {
                String helpText = getHelpFromCommand(message, command);
                if (helpText == null) {
                    helpText = getHelpFromResource(locale, command);
                }
                Speaker.say(message.getChannel(), helpText);
            } catch (Exception e) {
                Speaker.err(message, Resource.getError("NO_COMMAND", locale, command));
            }
        }

    }

    /**
     * @param locale the response locale
     * @param helpResourceKey
     * @return the help message text if it excists
     * @throws MissingResourceException if the message key does not exist in the help message bundle
     */
    private String getHelpFromResource(Locale locale, String helpResourceKey)
            throws MissingResourceException {
        String guessedResourceKey = Resource.guessHelpKey(helpResourceKey, locale);
        return Resource.getHelp(guessedResourceKey, locale,
                BotConfig.getConfig().PREFIX, "#" + BotConfig.getConfig().CHANNEL);
    }

    private String getHelpFromCommand(Message message, String command)
            throws InstantiationException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        String helpText = null;
        List<Class<BaseCommand>> commandClasses = BaseCommand.getCommandClasses().stream()
                .filter(cls -> cls.getSimpleName().equalsIgnoreCase(command))
                .collect(Collectors.toList());
        if (commandClasses.size() == 1) {
            BaseCommand fakeInstance = commandClasses.get(0).getConstructor().newInstance();
            helpText = (String) commandClasses.get(0).getMethod("help", Message.class)
                    .invoke(fakeInstance, message);
        }
        return helpText;
    }

    public String help(Message message) {
        return Resource.getHelp("GENERAL", getResponseLocale(message), BotConfig.getConfig().PREFIX,
                "#" + BotConfig.getConfig().CHANNEL);
    }

}
