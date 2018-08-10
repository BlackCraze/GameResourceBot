package de.blackcraze.grb.commands.concrete;

import static de.blackcraze.grb.util.CommandUtils.getDefaultLocale;
import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.InjectorUtils.getMateDao;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.PrintableTable;
import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.util.CommandUtils;
import de.blackcraze.grb.util.PrintUtils;
import de.blackcraze.grb.util.wagu.Block;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.apache.commons.lang3.StringUtils;

public class Users implements BaseCommand {

    private BaseCommand syncUser = (Scanner scanner, Message message) -> {
        if (!(message.getChannel() instanceof TextChannel)) {
            // I'm not sure if this ISE needs to be hard-coded or not. Plus, as you can see, I didn't save it. :'(
            throw new IllegalStateException(Resource.getError("ONLY_TEXT", CommandUtils.getResponseLocale(message)));
        }
        Locale locale = CommandUtils.getResponseLocale(message);
        Locale defaultLocale = getDefaultLocale();

        Function<Member, String> getName = m -> {
            return StringUtils.isBlank(m.getNickname()) ? m.getUser().getName() : m.getNickname();
        };

        // All users are stored in the bot's database.
        final List<Mate> storedMates = getMateDao().findAll();
        final List<String> storedIds =
                storedMates.stream().map(Mate::getDiscordId).collect(Collectors.toList());
        final List<Member> members = ((TextChannel) message.getChannel()).getMembers().stream()
                .filter(m -> !m.getUser().isBot()).collect(Collectors.toList());

        // Collect the users who are not in the channel anymore.
        List<Mate> toDelete = storedMates.stream()
                .filter(mate -> members.stream()
                        .noneMatch(m -> m.getUser().getId().equals(mate.getDiscordId())))
                .collect(Collectors.toList());
        // Delete collected users from the bot's database.
        toDelete.forEach(id -> getMateDao().delete(id));

        // Collect users who are in the channel but not in the bot's user database.
        List<Member> toCreate = members.stream()
                .filter(m -> !storedIds.contains(m.getUser().getId())).collect(Collectors.toList());
        // Create collected users in the bot's database.
        toCreate.stream().forEach(
                m -> getMateDao().createMate(defaultLocale, m.getUser().getId(), getName.apply(m)));

        // Display the result.
        String header = Resource.getHeader("USERS_SYNC_HEADER", locale);
        String action = Resource.getHeader("ACTION", locale);
        String user = Resource.getHeader("USER", locale);

        List<String> headers = Arrays.asList(new String[] {user, action});
        List<Integer> aligns =
                Arrays.asList(new Integer[] {Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT});
        List<List<String>> rows = new ArrayList<>();
        List<String> footer = Collections.emptyList();

        String add = Resource.getInfo("ADDED", locale);
        String remove = Resource.getInfo("DELETED", locale);
        toCreate.stream().map(m -> Arrays.asList(new String[] {getName.apply(m), add}))
                .forEach(row -> rows.add(row));
        toDelete.stream().map(m -> Arrays.asList(new String[] {m.getName(), remove}))
                .forEach(row -> rows.add(row));

        if (!rows.isEmpty()) {
            PrintableTable result = new PrintableTable(header, footer, headers, rows, aligns);
            Speaker.sayCode(message.getChannel(), PrintUtils.prettyPrint(result));
        } else {
            Speaker.sayCode(message.getChannel(), Resource.getInfo("UP_TO_DATE", locale));
        }
        message.addReaction(Speaker.Reaction.SUCCESS).queue();
    };


    private BaseCommand deleteUser = (Scanner scanner, Message message) -> {
        String memberName = scanner.nextLine().trim();
        List<Mate> mates = getMateDao().findByName(memberName);
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
    };

    public void run(Scanner scanner, Message message) {
        // Check if users has additional arguments
        if (scanner.hasNext()) {
            String action = scanner.next();
            switch (action) {
                case "sync":
                    BaseCommand.checkPublic(message);
                    syncUser.run(scanner, message);
                    break;
                case "delete":
                    BaseCommand.checkPublic(message);
                    deleteUser.run(scanner, message);
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
            String header = Resource.getHeader("USERS_LIST_HEADER", locale);
            header = String.format(header, rows.size());
            PrintableTable table = new PrintableTable(header, Collections.emptyList(),
                    Arrays.asList(Resource.getHeader("USER", locale),
                            Resource.getHeader("POPULATED", locale),
                            Resource.getHeader("OLDEST_STOCK", locale)),
                    rows, Arrays.asList(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT,
                            Block.DATA_MIDDLE_RIGHT));
            Speaker.sayCode(message.getChannel(), PrintUtils.prettyPrint(table));
        }
    }
}
