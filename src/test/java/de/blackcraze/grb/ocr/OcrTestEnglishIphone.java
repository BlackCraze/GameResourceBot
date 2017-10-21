package de.blackcraze.grb.ocr;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import static de.blackcraze.grb.ocr.ItemStatics.*;

import junit.framework.Assert;

public class OcrTestEnglishIphone {

	@Test
	public void en_iphone7_750px_1334px_1710102230a() throws Exception {
		InputStream stream = OcrTestEnglishIphone.class.getClassLoader().getResourceAsStream("en_iphone7_750px_1334px_1710102230a.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(430), stocks.get(REFINED_OIL));
		Assert.assertEquals(Long.valueOf(2881), stocks.get(MOTHERBOARD));
		Assert.assertEquals(Long.valueOf(14), stocks.get(URANIUM_ROD));
		Assert.assertEquals(Long.valueOf(403), stocks.get(PLASTIC));
		Assert.assertEquals(Long.valueOf(686158), stocks.get(COPPER_NAIL));
		Assert.assertEquals(Long.valueOf(110), stocks.get(OPTIC_FIBER));
		Assert.assertEquals(Long.valueOf(75), stocks.get(SOLAR_PANEL));
		Assert.assertEquals(Long.valueOf(75247), stocks.get(GRAPHITE));
		Assert.assertEquals(Long.valueOf(27493), stocks.get(GREEN_LASER));
	}

	@Test
	public void en_iphone7_750px_1334px_1710102230b() throws Exception {
		InputStream stream = OcrTestEnglishIphone.class.getClassLoader().getResourceAsStream("en_iphone7_750px_1334px_1710102230b.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(3987), stocks.get(LIANA_SEED));
		Assert.assertEquals(Long.valueOf(10517), stocks.get(CLEAN_WATER));
		Assert.assertEquals(Long.valueOf(1677), stocks.get(GRAPE_SEED));
		Assert.assertEquals(Long.valueOf(2265), stocks.get(GRAPE));
		Assert.assertEquals(Long.valueOf(5247), stocks.get(LIANA));
		Assert.assertEquals(Long.valueOf(4427), stocks.get(STEEL_PLATE));
		Assert.assertEquals(Long.valueOf(2136), stocks.get(SULFURIC_ACID));
		Assert.assertEquals(Long.valueOf(3961), stocks.get(RUBBER));
		Assert.assertEquals(Long.valueOf(927), stocks.get(ETHANOL));
	}

	@Test
	public void en_iphone7_750px_1334px_1710102230c() throws Exception {
		InputStream stream = OcrTestEnglishIphone.class.getClassLoader().getResourceAsStream("en_iphone7_750px_1334px_1710102230c.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(2136), stocks.get(SULFURIC_ACID));
		Assert.assertEquals(Long.valueOf(3961), stocks.get(RUBBER));
		Assert.assertEquals(Long.valueOf(927), stocks.get(ETHANOL));
		Assert.assertEquals(Long.valueOf(99), stocks.get(DIAMOND_CUTTER));
		Assert.assertEquals(Long.valueOf(13), stocks.get(MAYAN_CALENDAR));
		Assert.assertEquals(Long.valueOf(171), stocks.get(LIQUID_NITROGEN));
		Assert.assertEquals(Long.valueOf(430), stocks.get(REFINED_OIL));
		Assert.assertEquals(Long.valueOf(2881), stocks.get(MOTHERBOARD));
		Assert.assertEquals(Long.valueOf(14), stocks.get(URANIUM_ROD));
	}

	@Test
	public void en_iphone7_750px_1334px_1710102230d() throws Exception {
		InputStream stream = OcrTestEnglishIphone.class.getClassLoader().getResourceAsStream("en_iphone7_750px_1334px_1710102230d.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(75), stocks.get(SOLAR_PANEL));
		Assert.assertEquals(Long.valueOf(75245), stocks.get(GRAPHITE));
		Assert.assertEquals(Long.valueOf(27493), stocks.get(GREEN_LASER));
		Assert.assertEquals(Long.valueOf(40252), stocks.get(COPPER_WIRE));
		Assert.assertEquals(Long.valueOf(25338), stocks.get(INSULATED_WIRE));
		Assert.assertEquals(Long.valueOf(4304), stocks.get(AMBER_CHARGER));
		Assert.assertEquals(Long.valueOf(7), stocks.get(ALUMINIUM_BOTTLE));
		Assert.assertEquals(Long.valueOf(40), stocks.get(GEAR));
		Assert.assertEquals(Long.valueOf(377), stocks.get(BATTERY));
	}

	@Test
	public void en_iphone7s_1242px_2208px_1710111915() throws Exception {
		InputStream stream = OcrTestEnglishIphone.class.getClassLoader().getResourceAsStream("en_iphone7s_1242px_2208px_1710111915.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(4), stocks.get(COAL));
		Assert.assertEquals(Long.valueOf(133982), stocks.get(COPPER));
		Assert.assertEquals(Long.valueOf(104860), stocks.get(IRON));
		Assert.assertEquals(Long.valueOf(2082), stocks.get(AMBER));
		Assert.assertEquals(Long.valueOf(1180), stocks.get(ALUMINIUM));
		Assert.assertEquals(Long.valueOf(4675), stocks.get(WATER));
		Assert.assertEquals(Long.valueOf(1), stocks.get(SILVER));
		Assert.assertEquals(Long.valueOf(685805), stocks.get(GOLD));
		Assert.assertEquals(Long.valueOf(561), stocks.get(TREE_SEED));
	}

}
