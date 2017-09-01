package de.blackcraze.grb.util;

import de.blackcraze.grb.model.entity.StockType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;

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
			System.out.println("initializing stock types: " + records.size());
			List<StockType> stocks = getStockTypeDao().findAll();

			for (CSVRecord record : records) {
				String name = record.get(0);
				String price = record.get(1);

				boolean stockAlreadyExists = stocks.stream()
						.anyMatch(stockType -> stockType.getName().equalsIgnoreCase(name));

				if (stockAlreadyExists) {
					break;
				}

				StockType type = new StockType();
				type.setName(name);
				type.setPrice(Long.valueOf(price));
				getStockTypeDao().save(type);
				System.out.println("Created new stock type: " + type.getName());
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(parser);
		}

	}

}
