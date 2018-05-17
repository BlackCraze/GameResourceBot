package de.blackcraze.grb.commands.concrete;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.PrintableTable;
import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.util.PrintUtils;
import de.blackcraze.grb.util.wagu.Block;
import net.dv8tion.jda.core.entities.Message;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.InjectorUtils.getMateDao;

public class Users implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        List<Mate> mates;

        // Check if users has additional arguments
        if (scanner.hasNext()) {
            String action = scanner.next();
            switch (action) {
                case "delete":
                    BaseCommand.checkPublic(message);
                    String memberName = scanner.nextLine().trim();
                    mates = getMateDao().findByName(memberName);
                    if (!mates.isEmpty()) {
                        // Finally delete the member.
                        for (Mate mate : mates) {
                            getMateDao().delete(mate);
                        }
                        message.addReaction(Speaker.Reaction.SUCCESS).queue();
                    } else {
                        // No Mate with given name.
                        message.addReaction(Speaker.Reaction.FAILURE).queue();
                    }
                    break;
                default:
                    // Wrong argument!
                    message.addReaction(Speaker.Reaction.FAILURE).queue();
                    break;
            }
        } else {
            // List Users
            Locale locale = getResponseLocale(message);
            List<List<String>> rows = getMateDao().listOrderByOldestStock(locale);
            String header = Resource.getString("USERS_LIST_HEADER", locale);
            header = String.format(header, rows.size());
            PrintableTable table = new PrintableTable(
                    header, Collections.emptyList(),
                    Arrays.asList(Resource.getString("USER", locale),
                            Resource.getString("POPULATED", locale),
                            Resource.getString("OLDEST_STOCK", locale)),
                    rows, Arrays.asList(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT,
                    Block.DATA_MIDDLE_RIGHT));
            Speaker.sayCode(message.getChannel(), PrintUtils.prettyPrint(table));
        }
    }
}
