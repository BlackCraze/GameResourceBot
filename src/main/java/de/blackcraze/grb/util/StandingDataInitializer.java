package de.blackcraze.grb.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;

import com.google.inject.Injector;

import de.blackcraze.grb.dao.IStockTypeDao;
import de.blackcraze.grb.model.entity.StockType;

public class StandingDataInitializer {

	Injector in;

	@SuppressWarnings("unused")
	private StandingDataInitializer() {
	}

	public StandingDataInitializer(Injector injector) {
		this.in = injector;
	}

	public void initStockTypes() {
		Reader reader = null;
		CSVParser parser = null;
		try {
			InputStream resource = this.getClass().getClassLoader().getResourceAsStream("StockTypes.csv");
			reader = new InputStreamReader(new BOMInputStream(resource), "UTF-8");
			parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());

			List<StockType> stocks = getStockTypeDao().findAll();
			for (CSVRecord record : parser) {
				String name = record.get(0);
				String price = record.get(1);
				boolean exists = false;

				for (StockType stock : stocks) {
					if (stock.getName().equalsIgnoreCase(name)) {
						exists = true;
						break;
					}
				}

				if (!exists) {
					StockType type = new StockType();
					type.setName(name);
					type.setPrice(Long.valueOf(price));
					getStockTypeDao().save(type);
					System.out.println("created new stock type: " + type.getName());
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(parser);
		}

	}

	private IStockTypeDao getStockTypeDao() {
		return in.getInstance(IStockTypeDao.class);
	}

}
