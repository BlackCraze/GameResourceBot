package de.blackcraze.grb.commands.concrete;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import net.dv8tion.jda.api.entities.Message;

import java.util.Scanner;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;

public class Info implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        Speaker.say(message.getChannel(), Resource.getHelp("INFO", getResponseLocale(message)));
    }
}
