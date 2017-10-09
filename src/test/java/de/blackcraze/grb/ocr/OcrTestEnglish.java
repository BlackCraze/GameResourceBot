package de.blackcraze.grb.ocr;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import junit.framework.Assert;

public class OcrTestEnglish {

	@Test
	public void testEnglish1() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_eng_01.png");
		Map<String, Long> stocks = OCR.convertToStocks(stream, Locale.ENGLISH);
		Assert.assertTrue(stocks.keySet().size() == 9);
		Assert.assertEquals(Long.valueOf(11227), stocks.get("OIL"));
		Assert.assertEquals(Long.valueOf(98), stocks.get("URANIUM"));
		Assert.assertEquals(Long.valueOf(8955), stocks.get("IRON_BAR"));
		Assert.assertEquals(Long.valueOf(5168), stocks.get("ALUMINIUM_BAR"));
		Assert.assertEquals(Long.valueOf(278), stocks.get("POLISHED_AMBER"));
		Assert.assertEquals(Long.valueOf(7357), stocks.get("SULPHUR"));
		Assert.assertEquals(Long.valueOf(964), stocks.get("SODIUM"));
		Assert.assertEquals(Long.valueOf(36898), stocks.get("SILICON"));
		Assert.assertEquals(Long.valueOf(3619), stocks.get("POLISHED_EMERALD"));
	}

	@Test
	public void testEnglish2() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_eng_02.png");
		Map<String, Long> stocks = OCR.convertToStocks(stream, Locale.ENGLISH);
		Assert.assertTrue(stocks.keySet().size() == 9);
		Assert.assertEquals(Long.valueOf(554247), stocks.get("GOLD"));
		Assert.assertEquals(Long.valueOf(301793), stocks.get("AMBER"));
		Assert.assertEquals(Long.valueOf(105229), stocks.get("COAL"));
		Assert.assertEquals(Long.valueOf(205888), stocks.get("COPPER"));
		Assert.assertEquals(Long.valueOf(435730), stocks.get("IRON"));
		Assert.assertEquals(Long.valueOf(110844), stocks.get("WATER"));
		Assert.assertEquals(Long.valueOf(218475), stocks.get("SILVER"));
		Assert.assertEquals(Long.valueOf(301793), stocks.get("AMBER"));
		Assert.assertEquals(Long.valueOf(2242), stocks.get("SEED"));
	}

	@Test
	public void testEnglish3() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_eng_03.png");
		Map<String, Long> stocks = OCR.convertToStocks(stream, Locale.ENGLISH);
		Assert.assertTrue(stocks.keySet().size() == 9);
		Assert.assertEquals(Long.valueOf(432215), stocks.get("EMERALD"));
		Assert.assertEquals(Long.valueOf(484), stocks.get("PLATINUM"));
		Assert.assertEquals(Long.valueOf(308158), stocks.get("TOPAZ"));
		Assert.assertEquals(Long.valueOf(336251), stocks.get("RUBY"));
		Assert.assertEquals(Long.valueOf(54821), stocks.get("SAPPHIRE"));
		Assert.assertEquals(Long.valueOf(1165), stocks.get("AMETHYST"));
		Assert.assertEquals(Long.valueOf(255956), stocks.get("DIAMOND"));
		Assert.assertEquals(Long.valueOf(7409), stocks.get("TITANIUM_ORE"));
		Assert.assertEquals(Long.valueOf(208), stocks.get("ALEXANDRITE"));
	}

	@Test
	public void test_screens_fire_b1() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_b1.png");
		Map<String, Long> stocks = OCR.convertToStocks(stream, Locale.ENGLISH);
		Assert.assertTrue(stocks.keySet().size() == 9);
		Assert.assertEquals(Long.valueOf(33766), stocks.get("COPPER_BAR"));
		Assert.assertEquals(Long.valueOf(32340), stocks.get("IRON_BAR"));
		Assert.assertEquals(Long.valueOf(34410), stocks.get("ALUMINIUM_BAR"));
		Assert.assertEquals(Long.valueOf(7150), stocks.get("STEEL_BAR"));
		Assert.assertEquals(Long.valueOf(9056), stocks.get("SILVER_BAR"));
		Assert.assertEquals(Long.valueOf(8554), stocks.get("GOLD_BAR"));
		Assert.assertEquals(Long.valueOf(2902), stocks.get("GLASS_FLASK"));
		Assert.assertEquals(Long.valueOf(3108), stocks.get("STEEL_PLATE"));
		Assert.assertEquals(Long.valueOf(285), stocks.get("TITANIUM_BAR"));
	}

	@Test
	public void test_screens_fire_b2() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_b2.png");
		Map<String, Long> stocks = OCR.convertToStocks(stream, Locale.ENGLISH);
		Assert.assertTrue(stocks.keySet().size() == 9);
		Assert.assertEquals(Long.valueOf(391), stocks.get("DIAMOND_CUTTER"));
		Assert.assertEquals(Long.valueOf(434), stocks.get("MOTHERBOARD"));
		Assert.assertEquals(Long.valueOf(76113), stocks.get("COPPER_NAIL"));
		Assert.assertEquals(Long.valueOf(32324), stocks.get("AMBER_INSULATION"));
		Assert.assertEquals(Long.valueOf(143), stocks.get("SOLAR_PANEL"));
		Assert.assertEquals(Long.valueOf(21911), stocks.get("GRAPHITE"));
		Assert.assertEquals(Long.valueOf(98), stocks.get("GREEN_LASER"));
		Assert.assertEquals(Long.valueOf(24893), stocks.get("COPPER_WIRE"));
		Assert.assertEquals(Long.valueOf(4647), stocks.get("INSULATED_WIRE"));
	}

	@Test
	public void test_screens_fire_b3() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_b3.png");
		Map<String, Long> stocks = OCR.convertToStocks(stream, Locale.ENGLISH);
		Assert.assertTrue(stocks.keySet().size() == 9);
		Assert.assertEquals(Long.valueOf(3266), stocks.get("AMBER_CHARGER"));
		Assert.assertEquals(Long.valueOf(7418), stocks.get("ALUMINIUM_BOTTLE"));
		Assert.assertEquals(Long.valueOf(62), stocks.get("BOMB"));
		Assert.assertEquals(Long.valueOf(5), stocks.get("GEAR"));
		Assert.assertEquals(Long.valueOf(1013), stocks.get("BATTERY"));
		Assert.assertEquals(Long.valueOf(258), stocks.get("LAMP"));
		Assert.assertEquals(Long.valueOf(30), stocks.get("ACCUMULATOR"));
		Assert.assertEquals(Long.valueOf(43), stocks.get("SOLID_PROPELLANT"));
		Assert.assertEquals(Long.valueOf(217), stocks.get("CIRCUIT"));
	}

	@Test
	public void test_screens_fire_a1() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_a1.png");
		Map<String, Long> stocks = OCR.convertToStocks(stream, Locale.ENGLISH);
		Assert.assertTrue(stocks.keySet().size() == 9);
		Assert.assertEquals(Long.valueOf(149941), stocks.get("COAL"));
		Assert.assertEquals(Long.valueOf(218279), stocks.get("COPPER"));
		Assert.assertEquals(Long.valueOf(482480), stocks.get("IRON"));
		Assert.assertEquals(Long.valueOf(329736), stocks.get("AMBER"));
		Assert.assertEquals(Long.valueOf(721501), stocks.get("ALUMINIUM"));
		Assert.assertEquals(Long.valueOf(237747), stocks.get("SILVER"));
		Assert.assertEquals(Long.valueOf(579032), stocks.get("GOLD"));
		Assert.assertEquals(Long.valueOf(451145), stocks.get("EMERALD"));
		Assert.assertEquals(Long.valueOf(484), stocks.get("PLATINUM"));
	}

	@Test
	public void test_screens_fire_a2() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_a2.png");
		Map<String, Long> stocks = OCR.convertToStocks(stream, Locale.ENGLISH);
		Assert.assertTrue(stocks.keySet().size() == 8);
		Assert.assertEquals(Long.valueOf(309595), stocks.get("TOPAZ"));
		Assert.assertEquals(Long.valueOf(340464), stocks.get("RUBY"));
		Assert.assertEquals(Long.valueOf(54821), stocks.get("SAPPHIRE"));
		Assert.assertEquals(Long.valueOf(1165), stocks.get("AMETHYST"));
		Assert.assertEquals(Long.valueOf(244298), stocks.get("DIAMOND"));
		Assert.assertEquals(Long.valueOf(7409), stocks.get("TITANIUM_ORE"));
		Assert.assertEquals(Long.valueOf(208), stocks.get("ALEXANDRITE"));
		Assert.assertEquals(Long.valueOf(10203), stocks.get("URANIUM"));
	}

	@Test
	public void test_screens_fire_c1() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_c1.png");
		Map<String, Long> stocks = OCR.convertToStocks(stream, Locale.ENGLISH);
		Assert.assertTrue(stocks.keySet().size() == 9);
		Assert.assertEquals(Long.valueOf(112959), stocks.get("WATER"));
		Assert.assertEquals(Long.valueOf(8444), stocks.get("OIL"));
		Assert.assertEquals(Long.valueOf(5690), stocks.get("SULPHUR"));
		Assert.assertEquals(Long.valueOf(22860), stocks.get("SILICON"));
		Assert.assertEquals(Long.valueOf(16), stocks.get("SODIUM"));
		Assert.assertEquals(Long.valueOf(32), stocks.get("HYDROGEN"));
		Assert.assertEquals(Long.valueOf(13277), stocks.get("GLASS"));
		Assert.assertEquals(Long.valueOf(1437), stocks.get("OXYGEN"));
		Assert.assertEquals(Long.valueOf(181), stocks.get("CLEAN_WATER"));
	}

	@Test
	public void test_screens_fire_c2() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_c2.png");
		Map<String, Long> stocks = OCR.convertToStocks(stream, Locale.ENGLISH);
		Assert.assertTrue(stocks.keySet().size() == 9);
		Assert.assertEquals(Long.valueOf(13277), stocks.get("GLASS"));
		Assert.assertEquals(Long.valueOf(1437), stocks.get("OXYGEN"));
		Assert.assertEquals(Long.valueOf(181), stocks.get("CLEAN_WATER"));
		Assert.assertEquals(Long.valueOf(125), stocks.get("SULPHURIC_ACID"));
		Assert.assertEquals(Long.valueOf(4864), stocks.get("RUBBER"));
		Assert.assertEquals(Long.valueOf(115), stocks.get("REFINED_OIL"));
		Assert.assertEquals(Long.valueOf(341), stocks.get("DIETHYL_ETHER"));
		Assert.assertEquals(Long.valueOf(610), stocks.get("URANIUM_ROD"));
		Assert.assertEquals(Long.valueOf(26), stocks.get("PLASTIC"));
	}

	@Test
	public void test_screens_fire_d1() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_d1.png");
		Map<String, Long> stocks = OCR.convertToStocks(stream, Locale.ENGLISH);
		Assert.assertTrue(stocks.keySet().size() == 9);
		Assert.assertEquals(Long.valueOf(4226), stocks.get("POLISHED_AMBER"));
		Assert.assertEquals(Long.valueOf(29348), stocks.get("POLISHED_EMERALD"));
		Assert.assertEquals(Long.valueOf(5630), stocks.get("POLISHED_TOPAZ"));
		Assert.assertEquals(Long.valueOf(6372), stocks.get("POLISHED_SAPPHIRE"));
		Assert.assertEquals(Long.valueOf(15109), stocks.get("POLISHED_RUBY"));
		Assert.assertEquals(Long.valueOf(6863), stocks.get("POLISHED_AMETHYST"));
		Assert.assertEquals(Long.valueOf(2218), stocks.get("POLISHED_ALEXANDRITE"));
		Assert.assertEquals(Long.valueOf(35675), stocks.get("POLISHED_DIAMOND"));
		Assert.assertEquals(Long.valueOf(1490), stocks.get("MAYAN_CALENDAR"));
	}

	@Test
	public void test_screens_fire_d2() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_d2.png");
		Map<String, Long> stocks = OCR.convertToStocks(stream, Locale.ENGLISH);
		Assert.assertTrue(stocks.keySet().size() == 7);
		Assert.assertEquals(Long.valueOf(6372), stocks.get("POLISHED_SAPPHIRE"));
		Assert.assertEquals(Long.valueOf(15109), stocks.get("POLISHED_RUBY"));
		Assert.assertEquals(Long.valueOf(6863), stocks.get("POLISHED_AMETHYST"));
		Assert.assertEquals(Long.valueOf(2218), stocks.get("POLISHED_ALEXANDRITE"));
		Assert.assertEquals(Long.valueOf(35676), stocks.get("POLISHED_DIAMOND"));
		Assert.assertEquals(Long.valueOf(1490), stocks.get("MAYAN_CALENDAR"));
		Assert.assertEquals(Long.valueOf(40), stocks.get("HAIR_COMB"));
	}

	@Test
	public void test_screens_fire_e1() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_e1.png");
		try {
			OCR.convertToStocks(stream, Locale.ENGLISH);
			Assert.fail("Exception expected");
		} catch (IllegalStateException e) {
			Assert.assertTrue(true);
		}
	}

}
