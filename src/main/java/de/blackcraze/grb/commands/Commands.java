package de.blackcraze.grb.commands;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.CommandUtils.parseStockName;
import static de.blackcraze.grb.util.CommandUtils.parseStocks;
import static de.blackcraze.grb.util.InjectorUtils.getMateDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockDao;
import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.PrintableTable;
import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.StockType;
import de.blackcraze.grb.util.PrintUtils;
import de.blackcraze.grb.util.wagu.Block;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public final class Commands {

    private Commands() {
    }

    public static void ping(Scanner scanner, Message message) {
        Speaker.say(message.getTextChannel(), Resource.getString("PONG", getResponseLocale(message)));
    }

    public static void credits(Scanner scanner, Message message) {
        Speaker.say(message.getTextChannel(), Resource.getString("CDS", getResponseLocale(message)));
    }

    public static void userConfig(Scanner scanner, Message message) {
        Mate mate = getMateDao().getOrCreateMate(message.getMember(), getResponseLocale(message));
        if (!scanner.hasNext()) {
            StringBuilder response = new StringBuilder();
            response.append("language: ");
            response.append(mate.getLanguage());
            response.append("\n");
            Speaker.sayCode(message.getTextChannel(), response.toString());
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
                Locale locale = new Locale(value);
                if ("language".equalsIgnoreCase(field) && locale != null) {
                    mate.setLanguage(value);
                    getMateDao().update(mate);
                    message.addReaction(Speaker.Reaction.SUCCESS).queue();
                } else {
                    throw new IllegalStateException();
                }
            } catch (Exception e) {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                e.printStackTrace();
            }
        }
    }

    public static void config(Scanner scanner, Message message) {
        BotConfig.ServerConfig instance = BotConfig.getConfig(message.getGuild());
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
            Speaker.sayCode(message.getTextChannel(), response.toString());
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
                Field declaredField = BotConfig.ServerConfig.class.getDeclaredField(field.toUpperCase());
                assert String.class.equals(declaredField.getType());
                declaredField.set(instance, value);
                message.addReaction(Speaker.Reaction.SUCCESS).queue();
            } catch (Exception e) {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                e.printStackTrace();
            }
        }
    }

    public static void shutdown(Scanner scanner, Message message) {
        System.exit(1);
    }

    public static void nativeStatus(Scanner scanner, Message message) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("JAVACPP:\n");
        buffer.append("memory tracked by deallocators: " + formatBytes(totalBytes()) + "\n");
        buffer.append("maximum memory allowed to be tracked: " + formatBytes(maxBytes()) + "\n");
        try {
            buffer.append("physical memory installed according to the operating system, or 0 if unknown: "
                    + formatBytes(totalPhysicalBytes()) + "\n");
            buffer.append("maximum physical memory that should be used: " + formatBytes(maxPhysicalBytes()) + "\n");
            buffer.append("physical memory that is free according to the operating system, or 0 if unknown: "
                    + formatBytes(availablePhysicalBytes()) + "\n");
            buffer.append("physical memory currently used by the whole process, or 0 if unknown: "
                    + formatBytes(physicalBytes()) + "\n");
        } catch (UnsatisfiedLinkError e) {
            buffer.append("no physical Data Available");
        }
        Speaker.sayCode(message.getTextChannel(), buffer.toString());
    }

    public static void status(Scanner scanner, Message message) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("My Memory:\n\n");
        buffer.append("\n\n");
        for (MemPool pool : getPools()) {
            MemoryUsage usage = pool.getUsage();
            buffer.append(pool.getName()).append("\n");
            buffer.append("\tINIT:     ").append(FileUtils.byteCountToDisplaySize(usage.getInit())).append("\n");
            buffer.append("\tUSED:     ").append(FileUtils.byteCountToDisplaySize(usage.getUsed())).append("\n");
            buffer.append("\tCOMMITED: ").append(FileUtils.byteCountToDisplaySize(usage.getCommitted())).append("\n");
            buffer.append("\tMAX:      ").append(FileUtils.byteCountToDisplaySize(usage.getMax())).append("\n");
        }
        Speaker.sayCode(message.getTextChannel(), buffer.toString());
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
        result.add(new MemPool("Non-Heap", ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage()));
        for (MemoryPoolMXBean poolMXBean : poolMXBeans) {
            result.add(new MemPool(poolMXBean.getName(), poolMXBean.getUsage()));
        }
        return result;
    }

    public static void users(Scanner scanner, Message message) {
        List<List<String>> rows = getMateDao().listOrderByOldestStock();
        Locale locale = getResponseLocale(message);
        PrintableTable table = new PrintableTable(Resource.getString("USERS_LIST_HEADER", locale),
                Collections.emptyList(),
                Arrays.asList(Resource.getString("USER", locale), Resource.getString("POPULATED", locale),
                        Resource.getString("OLDEST_STOCK", locale)),
                rows, Arrays.asList(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT, Block.DATA_MIDDLE_RIGHT));
        Speaker.sayCode(message.getTextChannel(), PrintUtils.prettyPrint(table));
    }

    public static void clearMe(Scanner scanner, Message message) {
        Mate mate = getMateDao().getOrCreateMate(message.getMember(), getResponseLocale(message));
        getStockDao().deleteAll(mate);
        message.addReaction(Speaker.Reaction.SUCCESS).queue();
    }

    public static void update(Scanner scanner, Message message) {
        Locale responseLocale = getResponseLocale(message);
        Map<String, Long> stocks = parseStocks(scanner, responseLocale);
        internalUpdate(message, responseLocale, stocks);
    }

    static void internalUpdate(Message message, Locale responseLocale, Map<String, Long> stocks) {
        try {
            Mate mate = getMateDao().getOrCreateMate(message.getMember(), getResponseLocale(message));
            List<String> unknownStocks = getMateDao().updateStocks(mate, stocks);
            if (stocks.size() > 0) {
                if (!unknownStocks.isEmpty()) {
                    Speaker.err(message, String.format(Resource.getString("DO_NOT_KNOW_ABOUT", responseLocale),
                            unknownStocks.toString()));
                }
                if (unknownStocks.size() != stocks.size()) {
                    message.addReaction(Speaker.Reaction.SUCCESS).queue();
                }
            } else {
                Speaker.err(message, Resource.getString("RESOURCES_EMPTY", responseLocale));
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
                ? getStockTypeDao().findByNameLike(stockNameOptional.get(), locale) : getStockTypeDao().findAll();
        Speaker.sayCode(message.getTextChannel(), prettyPrintStockTypes(stocks, locale));
    }

    public static void check(Scanner scanner, Message message) {
        Optional<String> mateOrStockOptional = parseStockName(scanner);
        TextChannel textChannel = message.getTextChannel();
        Locale locale = getResponseLocale(message);
        if (!mateOrStockOptional.isPresent()) {
            Mate mate = getMateDao().getOrCreateMate(message.getMember(), getResponseLocale(message));
            List<Mate> mates = Collections.singletonList(mate);
            Speaker.sayCode(textChannel, prettyPrintMate(mates, locale));
        } else {
            List<Mate> mates = getMateDao().findByNameLike(mateOrStockOptional.get());
            if (!mates.isEmpty()) {
                Speaker.sayCode(textChannel, prettyPrintMate(mates, locale));
            }
            List<StockType> types = getStockTypeDao().findByNameLike(mateOrStockOptional.get(), locale);
            if (!types.isEmpty()) {
                Speaker.sayCode(textChannel, prettyPrintStocks(types, locale));
            }
            if (types.isEmpty() && mates.isEmpty()) {
                Speaker.say(textChannel, Resource.getString("RESOURCE_AND_USER_UNKNOWN", locale));
            }
        }
    }

    public static void help(Scanner scanner, Message message) {
        Predicate<Method> filter = method -> Modifier.isPublic(method.getModifiers());
        String response = Arrays.stream(Commands.class.getDeclaredMethods()).filter(filter).map(Method::getName)
                .collect(Collectors.joining("\n"));
        Speaker.sayCode(message.getTextChannel(),
                Resource.getString("COMMANDS", getResponseLocale(message)) + "\n" + response);
    }

    public static void total(Scanner scanner, Message message) {
        Optional<String> stockNameOptional = parseStockName(scanner);
        List<StockType> stockTypes;
        Locale locale = getResponseLocale(message);
        if (!stockNameOptional.isPresent()) {
            stockTypes = getStockTypeDao().findAll();
        } else {
            stockTypes = getStockTypeDao().findByNameLike(stockNameOptional.get(), locale);
            if (stockTypes.isEmpty()) {
                Speaker.say(message.getTextChannel(), Resource.getString("RESOURCE_UNKNOWN", locale));
                return;
            }
        }

        List<List<String>> rows = new ArrayList<>();
        for (StockType stockType : stockTypes) {
            long total = getStockDao().getTotalAmount(stockType);
            if (total > 0) {
                String localisedStockName = Resource.getItem(stockType.getName(), locale);
                rows.add(Arrays.asList(localisedStockName, String.format("%,d", total)));
            }
        }
        if (!rows.isEmpty()) {
            PrintableTable total_guild_resources = new PrintableTable(Resource.getString("TOTAL_RESOURCES", locale),
                    Collections.emptyList(),
                    Arrays.asList(Resource.getString("RAW_MATERIAL", locale), Resource.getString("AMOUNT", locale)),
                    rows, Arrays.asList(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT));
            Speaker.sayCode(message.getTextChannel(), prettyPrint(total_guild_resources));
        } else {
            Speaker.say(message.getTextChannel(), Resource.getString("RESOURCES_EMPTY", locale));
        }
    }

}
