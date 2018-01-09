package de.blackcraze.grb.commands;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.CommandUtils.parseGroupName;
import static de.blackcraze.grb.util.CommandUtils.parseStockName;
import static de.blackcraze.grb.util.CommandUtils.parseStocks;
import static de.blackcraze.grb.util.InjectorUtils.getMateDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeGroupDao;
import static de.blackcraze.grb.util.PrintUtils.prettyPrint;
import static de.blackcraze.grb.util.PrintUtils.prettyPrintMate;
import static de.blackcraze.grb.util.PrintUtils.prettyPrintStockTypes;
import static de.blackcraze.grb.util.PrintUtils.prettyPrintStocks;
import static org.bytedeco.javacpp.Pointer.availablePhysicalBytes;
import static org.bytedeco.javacpp.Pointer.formatBytes;
import static org.bytedeco.javacpp.Pointer.maxBytes;
import static org.bytedeco.javacpp.Pointer.maxPhysicalBytes;
import static org.bytedeco.javacpp.Pointer.physicalBytes;
import static org.bytedeco.javacpp.Pointer.totalBytes;
import static org.bytedeco.javacpp.Pointer.totalPhysicalBytes;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.Device;
import de.blackcraze.grb.model.PrintableTable;
import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.StockType;
import de.blackcraze.grb.model.entity.StockTypeGroup;
import de.blackcraze.grb.util.PrintUtils;
import de.blackcraze.grb.util.StockTypeComparator;
import de.blackcraze.grb.util.wagu.Block;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public final class Commands {

    private Commands() {}

    public static void info(Scanner scanner, Message message) {
        Speaker.say(message.getChannel(), Resource.getString("INFO", getResponseLocale(message)));
    }

    public static void ping(Scanner scanner, Message message) {
        Speaker.say(message.getChannel(), Resource.getString("PONG", getResponseLocale(message)));
    }

    public static void credits(Scanner scanner, Message message) {
        Speaker.say(message.getChannel(), Resource.getString("CDS", getResponseLocale(message)));
    }

    public static void userConfig(Scanner scanner, Message message) {
        Mate mate = getMateDao().getOrCreateMate(message, getResponseLocale(message));
        if (!scanner.hasNext()) {
            StringBuilder response = new StringBuilder();
            response.append("language: ");
            response.append(mate.getLanguage());
            response.append("\n");
            response.append("device: ");
            response.append(mate.getDevice());
            response.append(" [");
            Device[] devices = Device.values();
            for (int i = 0; i < devices.length; i++) {
                response.append(devices[i].name());
                if (i + 1 < devices.length) {
                    response.append(',');
                }
            }
            response.append("]\n");
            Speaker.sayCode(message.getChannel(), response.toString());
            return;
        }
        String config_action = scanner.next();
        if ("set".equalsIgnoreCase(config_action)) {
            String field = scanner.next();
            String value = scanner.next();
            if (Objects.isNull(field) || Objects.isNull(value)) {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                return;
            }
            try {
                if ("language".equalsIgnoreCase(field)) {
                    new Locale(value); // may throw an exception
                    mate.setLanguage(value);
                    getMateDao().update(mate);
                    message.addReaction(Speaker.Reaction.SUCCESS).queue();
                } else if ("device".equalsIgnoreCase(field)) {
                    Device dev = Device.valueOf(StringUtils.upperCase(value));
                    mate.setDevice(dev);
                    getMateDao().update(mate);
                    message.addReaction(Speaker.Reaction.SUCCESS).queue();
                } else {
                    throw new IllegalStateException();
                }
            } catch (IllegalArgumentException e) {
                // when setting an unknown device
                message.addReaction(Speaker.Reaction.FAILURE).queue();
            } catch (Exception e) {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                e.printStackTrace();
            }
        }
    }

    public static void config(Scanner scanner, Message message) {
        checkPublic(message);
        BotConfig.ServerConfig instance = BotConfig.getConfig();
        if (!scanner.hasNext()) {
            StringBuilder response = new StringBuilder();
            Field[] fields = BotConfig.ServerConfig.class.getDeclaredFields();

            for (Field field : fields) {
                Object value;
                try {
                    value = field.get(instance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
                response.append(field.getName());
                response.append(": ");
                response.append(value.toString());
                response.append("\n");
            }
            Speaker.sayCode(message.getChannel(), response.toString());
            return;
        }
        String config_action = scanner.next();
        if ("set".equalsIgnoreCase(config_action)) {
            String field = scanner.next();
            String value = scanner.next();
            if (Objects.isNull(field) || Objects.isNull(value)) {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                return;
            }
            try {
                Field declaredField =
                        BotConfig.ServerConfig.class.getDeclaredField(field.toUpperCase());
                assert String.class.equals(declaredField.getType());
                declaredField.set(instance, value);
                message.addReaction(Speaker.Reaction.SUCCESS).queue();
            } catch (Exception e) {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                e.printStackTrace();
            }
        }
    }

    private static void checkPublic(Message message) {
        if (!ChannelType.TEXT.equals(message.getChannelType())) {
            message.addReaction(Speaker.Reaction.FAILURE).queue();
            Speaker.say(message.getChannel(),
                    Resource.getString("PUBLIC_COMMAND_ONLY", getResponseLocale(message)));
            throw new IllegalStateException("public command only");
            // TODO MORE SOLID IMPLEMENTATION
        }
    }

    public static void shutdown(Scanner scanner, Message message) {
        checkPublic(message);
        System.exit(1);
    }

    public static void nativeStatus(Scanner scanner, Message message) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("JAVACPP:\n");
        buffer.append("memory tracked by deallocators: " + formatBytes(totalBytes()) + "\n");
        buffer.append("maximum memory allowed to be tracked: " + formatBytes(maxBytes()) + "\n");
        try {
            buffer.append(
                    "physical memory installed according to the operating system, or 0 if unknown: "
                            + formatBytes(totalPhysicalBytes()) + "\n");
            buffer.append("maximum physical memory that should be used: "
                    + formatBytes(maxPhysicalBytes()) + "\n");
            buffer.append(
                    "physical memory that is free according to the operating system, or 0 if unknown: "
                            + formatBytes(availablePhysicalBytes()) + "\n");
            buffer.append("physical memory currently used by the whole process, or 0 if unknown: "
                    + formatBytes(physicalBytes()) + "\n");
        } catch (UnsatisfiedLinkError e) {
            buffer.append("no physical Data Available");
        }
        Speaker.sayCode(message.getChannel(), buffer.toString());
    }

    public static void status(Scanner scanner, Message message) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("My Memory:\n\n");
        buffer.append("\n\n");
        for (MemPool pool : getPools()) {
            MemoryUsage usage = pool.getUsage();
            buffer.append(pool.getName()).append("\n");
            buffer.append("\tINIT:     ").append(FileUtils.byteCountToDisplaySize(usage.getInit()))
                    .append("\n");
            buffer.append("\tUSED:     ").append(FileUtils.byteCountToDisplaySize(usage.getUsed()))
                    .append("\n");
            buffer.append("\tCOMMITED: ")
                    .append(FileUtils.byteCountToDisplaySize(usage.getCommitted())).append("\n");
            buffer.append("\tMAX:      ").append(FileUtils.byteCountToDisplaySize(usage.getMax()))
                    .append("\n");
        }
        Speaker.sayCode(message.getChannel(), buffer.toString());
    }

    /**
    *
    */
    static class MemPool {

        private final String name;

        private final MemoryUsage usage;

        public MemPool(String name, MemoryUsage usage) {
            this.name = name;
            this.usage = usage;
        }

        /**
         * @return The name.
         */
        public String getName() {
            return name;
        }

        /**
         * @return The usage.
         */
        public MemoryUsage getUsage() {
            return usage;
        }
    }

    private static List<MemPool> getPools() {
        List<MemoryPoolMXBean> poolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        List<MemPool> result = new ArrayList<MemPool>(poolMXBeans.size() + 2);
        result.add(new MemPool("Heap", ManagementFactory.getMemoryMXBean().getHeapMemoryUsage()));
        result.add(new MemPool("Non-Heap",
                ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage()));
        for (MemoryPoolMXBean poolMXBean : poolMXBeans) {
            result.add(new MemPool(poolMXBean.getName(), poolMXBean.getUsage()));
        }
        return result;
    }

    public static void users(Scanner scanner, Message message) {
        List<Mate> mates = null;

        // Check if users has additional arguments
        if (scanner.hasNext()) {
            String action = scanner.next();
            switch (action) {
                case "delete":
                    checkPublic(message);
                    String memberName = scanner.next();
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

    public static void clear(Scanner scanner, Message message) {
        Optional<String> mateOrStockOptional = parseStockName(scanner);
        List<Mate> mates = null;
        String clearReaction = Speaker.Reaction.FAILURE;
        String mateOrStock = null;

        final Locale locale = getResponseLocale(message);
        Mate mate = getMateDao().getOrCreateMate(message, locale);
        if (!mateOrStockOptional.isPresent()) {
            // if no member was selected assume the user of the message.
            mates = Collections.singletonList(mate);
        } else {
            mateOrStock = mateOrStockOptional.get();
            if ("all".equalsIgnoreCase(mateOrStock)) {
                checkPublic(message);
                // select guild members
                mates = getMateDao().findByNameLike("%");
            } else {
                // select only given member with exact matching name.
                mates = getMateDao().findByName(mateOrStock);
                if (!mates.isEmpty()) {
                    checkPublic(message);
                }
            }
        }
        // Delete the stocks from defined members.
        // otherwise try the parameter as an Item.
        if (!mates.isEmpty()) {
            for (Mate aMate : mates) {
                getStockDao().deleteAll(aMate);
            }
            clearReaction = Speaker.Reaction.SUCCESS;
        } else {
            String stockIdentifier = Resource.getItemKey(mateOrStock, locale);
            Optional<StockType> stockType = getStockTypeDao().findByKey(stockIdentifier);
            // Try to delete the given name from stocks of current user.
            getStockDao().delete(mate, stockType.get());
            clearReaction = Speaker.Reaction.SUCCESS;
        }
        // Always response to a bot request.
        message.addReaction(clearReaction).queue();
    }

    public static void update(Scanner scanner, Message message) {
        Locale responseLocale = getResponseLocale(message);
        Map<String, Long> stocks = parseStocks(scanner, responseLocale);
        internalUpdate(message, responseLocale, stocks);
    }

    static void internalUpdate(Message message, Locale locale, Map<String, Long> stocks) {
        try {
            Mate mate = getMateDao().getOrCreateMate(message, getResponseLocale(message));
            List<String> unknownStocks = getMateDao().updateStocks(mate, stocks);
            if (stocks.size() > 0) {
                if (!unknownStocks.isEmpty()) {
                    Speaker.err(message,
                            String.format(Resource.getString("DO_NOT_KNOW_ABOUT", locale),
                                    unknownStocks.toString()));
                }
                if (unknownStocks.size() != stocks.size()) {
                    message.addReaction(Speaker.Reaction.SUCCESS).queue();
                }
            } else {
                Speaker.err(message, Resource.getString("RESOURCES_EMPTY", locale));
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.addReaction(Speaker.Reaction.FAILURE).queue();
            return;
        }
    }

    public static void checkTypes(Scanner scanner, Message message) {
        Optional<String> stockNameOptional = parseStockName(scanner);
        Locale locale = getResponseLocale(message);
        List<StockType> stocks = stockNameOptional.isPresent()
                ? getStockTypeDao().findByNameLike(stockNameOptional.get(), locale)
                : getStockTypeDao().findAll(locale);
        Speaker.sayCode(message.getChannel(), prettyPrintStockTypes(stocks, locale));
    }

    public static void group(Scanner scanner, Message message) {
        // create the user if it does not exist - prevent users to directly
        // message the bot that are not in the guild channel
        getMateDao().getOrCreateMate(message, getResponseLocale(message));
        if (scanner.hasNext()) {
            String subCommand = scanner.next();
            switch (subCommand) {
                case "create":
                    checkPublic(message);
                    groupCreate(scanner, message);
                    break;
                case "delete":
                    checkPublic(message);
                    groupDelete(scanner, message);
                    break;
                case "add":
                    checkPublic(message);
                    groupAdd(scanner, message);
                    break;
                case "remove":
                    checkPublic(message);
                    groupRemove(scanner, message);
                    break;
                case "list":
                    groupList(scanner, message);
                    break;
                default:
                    Speaker.err(message, Resource.getString("GROUP_SUBCOMMAND_UNKNOWN",
                            getResponseLocale(message)));
                    break;
            }
        } else {
            groupList(scanner, message);
        }
    }

    private static void groupCreate(Scanner scanner, Message message) {
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
    }

    private static void groupAdd(Scanner scanner, Message message) {
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
    }

    private static void groupRemove(Scanner scanner, Message message) {
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
    }

    private static Optional<StockTypeGroup> getTargetGroup(Scanner scanner, Message message,
            String operation) {
        Locale locale = getResponseLocale(message);
        if (scanner.hasNext()) {
            String groupName = scanner.next();
            Optional<StockTypeGroup> groupOpt = getStockTypeGroupDao().findByName(groupName);
            if (groupOpt.isPresent()) {
                return groupOpt;
            } else {
                Speaker.err(message, Resource.getString("GROUP_" + operation + "_UNKNOWN", locale));
            }
        } else {
            Speaker.err(message, Resource.getString("GROUP_" + operation + "_UNKNOWN", locale));
        }
        return Optional.empty();
    }

    private static List<StockType> getTargetStockTypes(Scanner scanner, Message message,
            String operation) {
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

    private static void groupDelete(Scanner scanner, Message message) {
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
    }

    private static void groupList(Scanner scanner, Message message) {
        Locale locale = getResponseLocale(message);
        List<String> groupNames = parseGroupName(scanner);
        List<StockTypeGroup> groups = Collections.emptyList();
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
                Collections.sort(types, new StockTypeComparator(locale));
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
    }

    public static void check(Scanner scanner, Message message) {
        Optional<String> nameOptional = parseStockName(scanner);
        MessageChannel channel = message.getChannel();
        Locale locale = getResponseLocale(message);
        if (!nameOptional.isPresent()) {
            Mate mate = getMateDao().getOrCreateMate(message, getResponseLocale(message));
            List<Mate> mates = Collections.singletonList(mate);
            Speaker.sayCode(channel, prettyPrintMate(mates, locale));
        } else {
            List<Mate> mates = getMateDao().findByNameLike(nameOptional.get());
            if (!mates.isEmpty()) {
                Speaker.sayCode(channel, prettyPrintMate(mates, locale));
                return;
            }
            final StockTypeComparator comp = new StockTypeComparator(locale);
            List<StockType> types = getStockTypeDao().findByNameLike(nameOptional.get(), locale);
            types.sort(comp);
            if (!types.isEmpty()) {
                Speaker.sayCode(channel, prettyPrintStocks(types, locale));
                return;
            }
            Optional<StockTypeGroup> groupOpt =
                    getStockTypeGroupDao().findByName(nameOptional.get());
            if (groupOpt.isPresent()) {
                List<StockType> groupTypes = groupOpt.get().getTypes();
                if (!groupTypes.isEmpty()) {
                    groupTypes.sort(comp);
                    Speaker.sayCode(channel, prettyPrintStocks(groupTypes, locale));
                } else {
                    String msg = String.format(Resource.getString("GROUP_EMPTY", locale),
                            nameOptional.get());
                    Speaker.say(channel, msg);
                }
            } else {
                Speaker.say(channel, Resource.getString("RESOURCE_AND_USER_UNKNOWN", locale));
            }
        }
    }

    public static void help(Scanner scanner, Message message) {
        Predicate<Method> filter = method -> Modifier.isPublic(method.getModifiers());
        String response = Arrays.stream(Commands.class.getDeclaredMethods()).filter(filter)
                .map(Method::getName).collect(Collectors.joining("\n"));
        Speaker.sayCode(message.getChannel(),
                Resource.getString("COMMANDS", getResponseLocale(message)) + "\n" + response);
    }

    public static void total(Scanner scanner, Message message) {
        Optional<String> nameOptional = parseStockName(scanner);
        List<StockType> types;
        Locale locale = getResponseLocale(message);
        if (!nameOptional.isPresent()) {
            types = getStockTypeDao().findAll(locale);
        } else {
            types = getStockTypeDao().findByNameLike(nameOptional.get(), locale);
            if (types.isEmpty()) {
                Optional<StockTypeGroup> groupOpt =
                        getStockTypeGroupDao().findByName(nameOptional.get());
                if (groupOpt.isPresent()) {
                    if (groupOpt.get().getTypes() == null || groupOpt.get().getTypes().isEmpty()) {
                        String msg = String.format(Resource.getString("GROUP_EMPTY", locale),
                                nameOptional.get());
                        Speaker.say(message.getChannel(), msg);
                        return;
                    } else {
                        types = groupOpt.get().getTypes();
                        types.sort(new StockTypeComparator(locale));
                    }
                } else {
                    Speaker.say(message.getChannel(),
                            Resource.getString("RESOURCE_UNKNOWN", locale));
                    return;
                }
            }
        }

        List<List<String>> rows = new ArrayList<>();
        for (StockType stockType : types) {
            long total = getStockDao().getTotalAmount(stockType);
            if (total > 0) {
                String localisedStockName = Resource.getItem(stockType.getName(), locale);
                rows.add(Arrays.asList(localisedStockName, String.format(locale, "%,d", total)));
            }
        }
        if (!rows.isEmpty()) {
            PrintableTable total_guild_resources = new PrintableTable(
                    Resource.getString("TOTAL_RESOURCES", locale), Collections.emptyList(),
                    Arrays.asList(Resource.getString("RAW_MATERIAL", locale),
                            Resource.getString("AMOUNT", locale)),
                    rows, Arrays.asList(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT));
            Speaker.sayCode(message.getChannel(), prettyPrint(total_guild_resources));
        } else {
            Speaker.say(message.getChannel(), Resource.getString("RESOURCES_EMPTY", locale));
        }
    }

}
