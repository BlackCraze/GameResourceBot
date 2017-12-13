package de.blackcraze.grb.util;

import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.model.entity.StockType;

public class StandingDataInitializer {

    public StandingDataInitializer() {
    }

    public void initStockTypes() {
        Reader reader = null;
        CSVParser parser = null;
        try {
            InputStream resource = this.getClass().getClassLoader().getResourceAsStream("StockTypes.csv");
            reader = new InputStreamReader(new BOMInputStream(resource), "UTF-8");
            parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
            List<CSVRecord> records = parser.getRecords();
            System.out.println("initializing stock types: " + records.size()); //replace hard-coded message with INIT_STOCK
            List<StockType> stocks = getStockTypeDao().findAll(new Locale(BotConfig.getConfig().LANGUAGE));

            for (CSVRecord record : records) {
                String name = record.get(0);
                String price = record.get(1);

                boolean stockAlreadyExists = stocks.stream()
                        .anyMatch(stockType -> stockType.getName().equalsIgnoreCase(name));

                if (stockAlreadyExists) {
                    continue;
                }

                StockType type = new StockType();
                type.setName(name);
                type.setPrice(Long.valueOf(price));
                getStockTypeDao().save(type);
                System.out.println("Created new stock type: " + type.getName()); //replace hard-coded message with CREATE_STOCK
            }

            for (StockType type : stocks) {
                boolean found = false;
                for (CSVRecord record : records) {
                    String name = record.get(0);
                    if (type.getName().equals(name)) {
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("Delete obsolete stock type: " + type.getName()); //replace hard-coded message with DELETE_STOCK
                    getStockTypeDao().delete(type);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(parser);
        }

    }

}
