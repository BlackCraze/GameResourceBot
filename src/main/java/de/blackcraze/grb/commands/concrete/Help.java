package de.blackcraze.grb.commands.concrete;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import net.dv8tion.jda.core.entities.Message;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;

public class Help implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        if (!scanner.hasNext()) {
            commandList(message);
        } else {
            String command = scanner.next();
            try {
                List<Class<BaseCommand>> commandClasses = BaseCommand.getCommandClasses()
                        .stream()
                        .filter(cls -> cls.getSimpleName().equalsIgnoreCase(command))
                        .collect(Collectors.toList());

                assert commandClasses.size() == 1;

                BaseCommand fakeInstance = commandClasses.get(0).getConstructor().newInstance();
                Locale locale = getResponseLocale(message);
                String helpText = (String) commandClasses.get(0).getMethod("help").invoke(fakeInstance, locale);
                Speaker.say(message.getChannel(), helpText);
            } catch (Exception e) {
                Speaker.err(message, "No such command `" + command + "`");
            }
        }
    }

    private void commandList(Message message) {
        String commandsList = BaseCommand.getCommandClasses()
                .stream()
                .map(Class::getSimpleName)
                .collect(Collectors.joining("\n"));
        Speaker.sayCode(message.getChannel(),
                Resource.getString("COMMANDS", getResponseLocale(message)) + "\n" + commandsList);
    }
}
