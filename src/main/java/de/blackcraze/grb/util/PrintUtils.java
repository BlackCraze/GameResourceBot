package de.blackcraze.grb.util;

import static de.blackcraze.grb.util.InjectorUtils.getStockDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.joda.time.Duration;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.PrintableTable;
import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.Stock;
import de.blackcraze.grb.model.entity.StockType;
import de.blackcraze.grb.util.wagu.Block;
import de.blackcraze.grb.util.wagu.Board;
import de.blackcraze.grb.util.wagu.Table;

public final class PrintUtils {

    private PrintUtils() {}

    public static List<String> prettyPrint(PrintableTable... stocks) {
        List<String> returnHandle = new ArrayList<>(stocks.length);
        for (PrintableTable stock : stocks) {
            if (stock != null) {
                int boardWidth = stock.getWidth();
                int headerWidth = boardWidth - 2; // outer borders
                StringBuilder b = new StringBuilder();
                Board board = new Board(boardWidth);
                // board.showBlockIndex(true); // DEBUG
                Block header = new Block(board, headerWidth, 1, stock.getHeader());
                header.setDataAlign(Block.DATA_MIDDLE_LEFT);
                board.setInitialBlock(header);

                Table table = new Table(board, boardWidth, stock.getTitles(), stock.getRows(),
                        stock.getWidths(), stock.getAligns());
                board.appendTableTo(0, Board.APPEND_BELOW, table);

                for (int i = 0; i < stock.getFooter().size(); i++) {
                    Block footer =
                            new Block(board, stock.getWidths().get(i), 1, stock.getFooter().get(i));
                    footer.setDataAlign(stock.getAligns().get(i));
                    if (i == 0) {
                        header.getMostBelowBlock().setBelowBlock(footer);
                    } else {
                        header.getMostBelowBlock().getMostRightBlock().setRightBlock(footer);
                    }
                }

                board.build();
                b.append(board.getPreview());
                returnHandle.add(b.toString());
            }
        }
        return returnHandle;
    }

    public static List<String> prettyPrint(Map<String, Long> stocks, Locale locale) {
        StringBuilder b = new StringBuilder();
        for (Entry<String, Long> entry : stocks.entrySet()) {
            if (Long.MIN_VALUE != entry.getValue()) {
                // in this case it is the misspelled name see
                // de.blackcraze.grb.util.CommandUtils.parseStocks(Scanner,
                // Locale)
                String localisedStockName = Resource.getItem(entry.getKey(), locale);
                b.append(localisedStockName);
                b.append(": ");
                b.append(String.format(locale, "%,d", entry.getValue()));
                b.append("\n");
            }
        }
        return Collections.singletonList(b.toString());
    }

    public static String getDiffFormatted(Date from, Date to) {
        Duration duration = new Duration(to.getTime() - from.getTime()); // in
                                                                         // milliseconds
        PeriodFormatter formatter = new PeriodFormatterBuilder().printZeroNever()//
                .appendWeeks().appendSuffix("w").appendSeparator(" ")//
                .appendDays().appendSuffix("d").appendSeparator(" ")//
                .appendHours().appendSuffix("h").appendSeparator(" ")//
                .appendMinutes().appendSuffix("m").appendSeparator(" ")//
                .appendSeconds().appendSuffix("s")//
                .toFormatter();
        String fullTimeAgo = formatter.print(duration.toPeriod(PeriodType.yearMonthDayTime()));
        return Arrays.stream(fullTimeAgo.split(" ")).limit(2).collect(Collectors.joining(" "));
    }

    public static List<String> prettyPrintStocks(List<StockType> stockTypes, Locale locale) {
        if (stockTypes.isEmpty()) {
            return Collections.singletonList(Resource.getString("RESOURCE_UNKNOWN", locale));
        }
        List<String> headers = Arrays.asList(Resource.getString("USER", locale),
                Resource.getString("AMOUNT", locale), Resource.getString("UPDATED", locale));
        List<Integer> aligns = Arrays.asList(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT,
                Block.DATA_MIDDLE_RIGHT);

        PrintableTable[] tables = new PrintableTable[stockTypes.size()];
        for (int i = 0; i < stockTypes.size(); i++) {
            StockType type = stockTypes.get(i);
            List<Stock> stocks = getStockDao().findStocksByType(type);
            List<List<String>> rows = new ArrayList<>(stocks.size());
            long sumAmount = 0;
            if (stocks.isEmpty()) {
                rows.add(Arrays.asList("-", "-", "-"));
            }
            for (Stock stock : stocks) {
                String mateName = stock.getMate().getName();
                String amount = String.format(locale, "%,d", stock.getAmount());
                String updated = PrintUtils.getDiffFormatted(stock.getUpdated(), new Date());
                rows.add(Arrays.asList(mateName, amount, updated));
                sumAmount += stock.getAmount();
            }
            String resourceKey = type.getName();
            String resourceName = Resource.getItem(resourceKey, locale);
            List<String> summary =
                    Arrays.asList(resourceName, String.format(locale, "%,d", sumAmount), "");
            tables[i] = new PrintableTable(resourceName, summary, headers, rows, aligns);
        }
        return PrintUtils.prettyPrint(tables);
    }

    public static String prettyPrintStockTypes(List<StockType> stockTypes, Locale responseLocale) {
        StringBuilder b = new StringBuilder();
        if (stockTypes.isEmpty()) {
            b.append(Resource.getString("NO_DATA", responseLocale));
        }
        for (StockType type : stockTypes) {
            String resourceKey = type.getName();
            String resourceName = Resource.getItem(resourceKey, responseLocale);
            b.append(resourceName);
            b.append("\n");
        }
        return b.toString();
    }

    public static List<String> prettyPrintMate(List<Mate> mates, Locale locale) {
        if (mates.isEmpty()) {
            return Collections.singletonList(Resource.getString("USER_UNKNOWN", locale));
        }
        List<String> headers = Arrays.asList(Resource.getString("RAW_MATERIAL", locale),
                Resource.getString("AMOUNT", locale), Resource.getString("UPDATED", locale));
        List<Integer> aligns = Arrays.asList(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT,
                Block.DATA_MIDDLE_RIGHT);

        PrintableTable[] tables = new PrintableTable[mates.size()];
        StockComparator comp = new StockComparator(locale);
        for (int i = 0; i < mates.size(); i++) {
            Mate mate = mates.get(i);
            List<Stock> stocks = getStockDao().findStocksByMate(mate);
            stocks.sort(comp);
            List<List<String>> rows = new ArrayList<>(stocks.size());
            for (Stock stock : stocks) {
                String resourceKey = stock.getType().getName();
                String resourceName = Resource.getItem(resourceKey, locale);
                String amount = String.format(locale, "%,d", stock.getAmount());
                String updated = PrintUtils.getDiffFormatted(stock.getUpdated(), new Date());
                rows.add(Arrays.asList(resourceName, amount, updated));
            }
            if (stocks.isEmpty()) {
                rows.add(Arrays.asList("-", "-", "-"));
            }
            tables[i] = new PrintableTable(mate.getName(), Collections.emptyList(), headers, rows,
                    aligns);
        }
        return PrintUtils.prettyPrint(tables);
    }
}
