package de.blackcraze.grb.commands.concrete;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.PrintableTable;
import de.blackcraze.grb.model.entity.StockType;
import de.blackcraze.grb.model.entity.StockTypeGroup;
import de.blackcraze.grb.util.PrintUtils;
import de.blackcraze.grb.util.StockTypeComparator;
import de.blackcraze.grb.util.wagu.Block;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.CommandUtils.parseGroupName;
import static de.blackcraze.grb.util.InjectorUtils.getMateDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeGroupDao;

public class Group implements BaseCommand {

    public void run(Scanner scanner, Message message) {
        // create the user if it does not exist - prevent users to directly
        // message the bot that are not in the guild channel
        getMateDao().getOrCreateMate(message, getResponseLocale(message));
        if (scanner.hasNext()) {
            String subCommand = scanner.next();
            switch (subCommand) {
            case "create":
                BaseCommand.checkPublic(message);
                groupCreate.run(scanner, message);
                break;
            case "delete":
                BaseCommand.checkPublic(message);
                groupDelete.run(scanner, message);
                break;
            case "add":
                BaseCommand.checkPublic(message);
                groupAdd.run(scanner, message);
                break;
            case "remove":
                BaseCommand.checkPublic(message);
                groupRemove.run(scanner, message);
                break;
            case "rename":
                groupRename.run(scanner, message);
                BaseCommand.checkPublic(message);
                break;
            case "list":
                groupList.run(scanner, message);
                break;
            default:
                Speaker.err(message,
                        Resource.getString("GROUP_SUBCOMMAND_UNKNOWN", getResponseLocale(message)));
                break;
            }
        } else {
            groupList.run(scanner, message);
        }
    }

    public static final BaseCommand groupCreate = (Scanner scanner, Message message) -> {
        List<String> groupNames = parseGroupName(scanner);
        List<String> inUse = new ArrayList<>();
        for (String groupName : groupNames) {
            Optional<StockTypeGroup> groupOpt = getStockTypeGroupDao().findByName(groupName);
            if (groupOpt.isPresent()) {
                inUse.add(groupName);
            } else {
                StockTypeGroup group = new StockTypeGroup();
                group.setName(groupName);
                getStockTypeGroupDao().save(group);
                message.addReaction(Speaker.Reaction.SUCCESS).queue();
            }
        }
        if (groupNames.isEmpty()) {
            Speaker.err(message,
                    Resource.getString("GROUP_CREATE_UNKNOWN", getResponseLocale(message)));
        }
        if (!inUse.isEmpty()) {
            String msg = String.format(
                    Resource.getString("GROUP_CREATE_IN_USE", getResponseLocale(message)),
                    inUse.toString());
            Speaker.err(message, msg);
        }
    };

    public static final BaseCommand groupAdd = (Scanner scanner, Message message) -> {
        Optional<StockTypeGroup> groupOpt = getTargetGroup(scanner, message, "ADD");
        if (groupOpt.isPresent()) {
            List<StockType> stockTypes = getTargetStockTypes(scanner, message, "ADD");
            StockTypeGroup group = groupOpt.get();
            if (!stockTypes.isEmpty()) {
                List<StockType> types = group.getTypes();
                if (types == null) {
                    types = new ArrayList<>();
                    group.setTypes(types);
                }
                types.addAll(stockTypes);
                // removing doubles
                types = new ArrayList<>(new HashSet<>(types));
                group.setTypes(types);
                getStockTypeGroupDao().update(group);
                message.addReaction(Speaker.Reaction.SUCCESS).queue();
            }
        }
    };
    public static final BaseCommand groupRemove = (Scanner scanner, Message message) -> {
        Optional<StockTypeGroup> groupOpt = getTargetGroup(scanner, message, "REMOVE");
        if (groupOpt.isPresent()) {
            List<StockType> stockTypes = getTargetStockTypes(scanner, message, "REMOVE");
            StockTypeGroup group = groupOpt.get();
            if (!stockTypes.isEmpty()) {
                List<StockType> types = group.getTypes();
                if (types == null) {
                    group.setTypes(new ArrayList<>());
                }
                group.getTypes().removeAll(stockTypes);
                getStockTypeGroupDao().update(group);
                message.addReaction(Speaker.Reaction.SUCCESS).queue();
            }
        }
    };
    public static final BaseCommand groupDelete = (Scanner scanner, Message message) -> {
        List<String> groupNames = parseGroupName(scanner);
        List<String> unknown = new ArrayList<>();
        for (String groupName : groupNames) {
            Optional<StockTypeGroup> group = getStockTypeGroupDao().findByName(groupName);
            if (group.isPresent()) {
                getStockTypeGroupDao().delete(group.get());
                message.addReaction(Speaker.Reaction.SUCCESS).queue();
            } else {
                unknown.add(groupName);
            }
        }
        if (groupNames.isEmpty()) {
            Speaker.err(message,
                    Resource.getString("GROUP_DELETE_UNKNOWN", getResponseLocale(message)));
        }
        if (!unknown.isEmpty()) {
            String msg = String.format(
                    Resource.getString("GROUP_DELETE_UNKNOWN", getResponseLocale(message)),
                    unknown.toString());
            Speaker.err(message, msg);
        }
    };
    public static final BaseCommand groupRename = (Scanner scanner, Message message) -> {
        Locale locale = getResponseLocale(message);
        Optional<StockTypeGroup> groupOpt = getTargetGroup(scanner, message, "RENAME");
        if (groupOpt.isPresent()) {
            if (scanner.hasNext()) {
                String groupName = scanner.next();
                Optional<StockTypeGroup> newName = getStockTypeGroupDao().findByName(groupName);
                if (newName.isPresent()) {
                    String msg = String.format(
                            /* TODO maybe a standalone error message? */
                            Resource.getString("GROUP_CREATE_IN_USE", locale), groupName);
                    Speaker.err(message, msg);
                } else {
                    StockTypeGroup group = groupOpt.get();
                    group.setName(groupName);
                    getStockTypeGroupDao().update(group);
                    message.addReaction(Speaker.Reaction.SUCCESS).queue();
                }
            } else {
                Speaker.err(message, Resource.getString("GROUP_RENAME_UNKNOWN", locale));
            }
        }
    };

    public static final BaseCommand groupList = (Scanner scanner, Message message) -> {
        Locale locale = getResponseLocale(message);
        List<String> groupNames = parseGroupName(scanner);
        List<StockTypeGroup> groups;
        if (groupNames.isEmpty()) {
            groups = getStockTypeGroupDao().findAll();
        } else {
            groups = getStockTypeGroupDao().findByNameLike(groupNames);
        }
        List<List<String>> rows = new ArrayList<>();
        for (StockTypeGroup stockTypeGroup : groups) {
            List<StockType> types = stockTypeGroup.getTypes();
            String amount = String.format(locale, "%,d", types != null ? types.size() : 0);
            rows.add(Arrays.asList(stockTypeGroup.getName(), amount));
            if (types != null) {
                types.sort(new StockTypeComparator(locale));
                for (Iterator<StockType> it2 = types.iterator(); it2.hasNext();) {
                    StockType stockType = it2.next();
                    String localisedStockName = Resource.getItem(stockType.getName(), locale);
                    String tree = it2.hasNext() ? "├─ " : "└─ ";
                    rows.add(Arrays.asList(tree + localisedStockName, " "));
                }
            }
        }
        if (rows.isEmpty()) {
            rows.add(Arrays.asList(" ", " "));
        }
        List<String> titles = Arrays.asList(Resource.getString("NAME", locale),
                Resource.getString("AMOUNT", locale));
        String header = Resource.getString("GROUP_LIST_HEADER", locale);
        List<Integer> aligns = Arrays.asList(Block.DATA_MIDDLE_LEFT, Block.DATA_BOTTOM_RIGHT);
        List<String> footer = Collections.emptyList();
        PrintableTable table = new PrintableTable(header, footer, titles, rows, aligns);
        Speaker.sayCode(message.getChannel(), PrintUtils.prettyPrint(table));
    };

    static Optional<StockTypeGroup> getTargetGroup(Scanner scanner, Message message,
            String operation) {
        Locale locale = getResponseLocale(message);
        if (scanner.hasNext()) {
            String groupName = scanner.next();
            Optional<StockTypeGroup> groupOpt = getStockTypeGroupDao().findByName(groupName);
            if (groupOpt.isPresent()) {
                return groupOpt;
            } else {
                // TODO
                // Standalone error message in case the user wanted to use group that does not exist
                Speaker.err(message, Resource.getString("GROUP_" + operation + "_UNKNOWN", locale));
            }
        } else {
            Speaker.err(message, Resource.getString("GROUP_" + operation + "_UNKNOWN", locale));
        }
        return Optional.empty();
    }

    static List<StockType> getTargetStockTypes(Scanner scanner, Message message, String operation) {
        Locale locale = getResponseLocale(message);
        List<StockType> stockTypes = new ArrayList<>();
        List<String> unknownStockTypes = new ArrayList<>();
        List<String> inputNames = new ArrayList<>();
        while (scanner.hasNext()) {
            String next = scanner.next();
            boolean groupStart = StringUtils.startsWithAny(next, "\"", "\'");
            if (!groupStart) {
                inputNames.add(next);
            } else {
                StringBuilder buffer = new StringBuilder(next);
                boolean groupEnd = false;
                while (scanner.hasNext() && !groupEnd) {
                    next = scanner.next();
                    groupEnd = StringUtils.endsWithAny(next, "\"", "\'");
                    buffer.append(" ");
                    buffer.append(next);
                }
                buffer.deleteCharAt(0);
                buffer.deleteCharAt(buffer.length() - 1);
                inputNames.add(buffer.toString());
            }
        }
        for (String inputName : inputNames) {
            try {
                // may throws IllegalArgumentException if there is no
                String key = Resource.getItemKey(inputName, locale);
                Optional<StockType> stockOpt = getStockTypeDao().findByKey(key);
                if (!stockOpt.isPresent()) {
                    unknownStockTypes.add(inputName);
                } else {
                    stockTypes.add(stockOpt.get());
                }
            } catch (Exception e) {
                unknownStockTypes.add(inputName);
            }
        }
        if (!unknownStockTypes.isEmpty()) {
            String msg = String.format(Resource.getString("GROUP_UNKNOWN_TYPE", locale),
                    unknownStockTypes.toString());
            Speaker.err(message, msg);
        }
        return stockTypes;
    }

}
