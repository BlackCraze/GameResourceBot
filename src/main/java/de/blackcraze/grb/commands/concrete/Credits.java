package de.blackcraze.grb.commands.concrete;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import java.util.Scanner;
import net.dv8tion.jda.core.entities.Message;

public class Credits implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        Speaker.say(message.getChannel(), Resource.getString("CDS", getResponseLocale(message)));
    }

}
