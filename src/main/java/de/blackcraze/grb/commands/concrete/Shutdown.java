package de.blackcraze.grb.commands.concrete;

import de.blackcraze.grb.commands.BaseCommand;
import java.util.Scanner;
import net.dv8tion.jda.core.entities.Message;

public class Shutdown implements BaseCommand {
    // We're not displaying the "BYE_MSG" here? I know it's in ReadyListener.java, but still...
    public void run(Scanner scanner, Message message) {
        BaseCommand.checkPublic(message);
        System.exit(1);
    }

}
