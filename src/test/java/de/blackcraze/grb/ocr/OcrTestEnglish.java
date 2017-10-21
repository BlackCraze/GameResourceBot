package de.blackcraze.grb.ocr;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import static de.blackcraze.grb.ocr.ItemStatics.*;

import junit.framework.Assert;

public class OcrTestEnglish {

	@Test
	public void testEnglish1() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_eng_01.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(11227), stocks.get(OIL));
		Assert.assertEquals(Long.valueOf(98), stocks.get(URANIUM));
		Assert.assertEquals(Long.valueOf(8955), stocks.get(IRON_BAR));
		Assert.assertEquals(Long.valueOf(5168), stocks.get(ALUMINIUM_BAR));
		Assert.assertEquals(Long.valueOf(278), stocks.get(POLISHED_AMBER));
		Assert.assertEquals(Long.valueOf(7357), stocks.get(SULFUR));
		Assert.assertEquals(Long.valueOf(964), stocks.get(SODIUM));
		Assert.assertEquals(Long.valueOf(36898), stocks.get(SILICON));
		Assert.assertEquals(Long.valueOf(3619), stocks.get(POLISHED_EMERALD));
	}

	@Test
	public void testEnglish2() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_eng_02.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(554247), stocks.get(GOLD));
		Assert.assertEquals(Long.valueOf(301793), stocks.get(AMBER));
		Assert.assertEquals(Long.valueOf(105229), stocks.get(COAL));
		Assert.assertEquals(Long.valueOf(205888), stocks.get(COPPER));
		Assert.assertEquals(Long.valueOf(435730), stocks.get(IRON));
		Assert.assertEquals(Long.valueOf(110844), stocks.get(WATER));
		Assert.assertEquals(Long.valueOf(218475), stocks.get(SILVER));
		Assert.assertEquals(Long.valueOf(301793), stocks.get(AMBER));
		Assert.assertEquals(Long.valueOf(2242), stocks.get(TREE_SEED));
	}

	@Test
	public void testEnglish3() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_eng_03.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(432215), stocks.get(EMERALD));
		Assert.assertEquals(Long.valueOf(484), stocks.get(PLATINUM));
		Assert.assertEquals(Long.valueOf(308158), stocks.get(TOPAZ));
		Assert.assertEquals(Long.valueOf(336251), stocks.get(RUBY));
		Assert.assertEquals(Long.valueOf(54821), stocks.get(SAPPHIRE));
		Assert.assertEquals(Long.valueOf(1165), stocks.get(AMETHYST));
		Assert.assertEquals(Long.valueOf(255956), stocks.get(DIAMOND));
		Assert.assertEquals(Long.valueOf(7409), stocks.get(TITANIUM_ORE));
		Assert.assertEquals(Long.valueOf(208), stocks.get(ALEXANDRITE));
	}

	@Test
	public void test_screens_fire_b1() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_b1.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(33766), stocks.get(COPPER_BAR));
		Assert.assertEquals(Long.valueOf(32340), stocks.get(IRON_BAR));
		Assert.assertEquals(Long.valueOf(34410), stocks.get(ALUMINIUM_BAR));
		Assert.assertEquals(Long.valueOf(7150), stocks.get(STEEL_BAR));
		Assert.assertEquals(Long.valueOf(9056), stocks.get(SILVER_BAR));
		Assert.assertEquals(Long.valueOf(8554), stocks.get(GOLD_BAR));
		Assert.assertEquals(Long.valueOf(2902), stocks.get(GLASS_FLASK));
		Assert.assertEquals(Long.valueOf(3108), stocks.get(STEEL_PLATE));
		Assert.assertEquals(Long.valueOf(285), stocks.get(TITANIUM_BAR));
	}

	@Test
	public void test_screens_fire_b2() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_b2.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(391), stocks.get(DIAMOND_CUTTER));
		Assert.assertEquals(Long.valueOf(434), stocks.get(MOTHERBOARD));
		Assert.assertEquals(Long.valueOf(76113), stocks.get(COPPER_NAIL));
		Assert.assertEquals(Long.valueOf(32324), stocks.get(AMBER_INSULATION));
		Assert.assertEquals(Long.valueOf(143), stocks.get(SOLAR_PANEL));
		Assert.assertEquals(Long.valueOf(21911), stocks.get(GRAPHITE));
		Assert.assertEquals(Long.valueOf(98), stocks.get(GREEN_LASER));
		Assert.assertEquals(Long.valueOf(24893), stocks.get(COPPER_WIRE));
		Assert.assertEquals(Long.valueOf(4647), stocks.get(INSULATED_WIRE));
	}

	@Test
	public void test_screens_fire_b3() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_b3.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(3266), stocks.get(AMBER_CHARGER));
		Assert.assertEquals(Long.valueOf(7418), stocks.get(ALUMINIUM_BOTTLE));
		Assert.assertEquals(Long.valueOf(62), stocks.get(BOMB));
		Assert.assertEquals(Long.valueOf(5), stocks.get(GEAR));
		Assert.assertEquals(Long.valueOf(1013), stocks.get(BATTERY));
		Assert.assertEquals(Long.valueOf(258), stocks.get(LAMP));
		Assert.assertEquals(Long.valueOf(30), stocks.get(ItemStatics.ACCUMULATOR));
		Assert.assertEquals(Long.valueOf(43), stocks.get(SOLID_PROPELLANT));
		Assert.assertEquals(Long.valueOf(217), stocks.get(CIRCUIT));
	}

	@Test
	public void test_screens_fire_a1() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_a1.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(149941), stocks.get(COAL));
		Assert.assertEquals(Long.valueOf(218279), stocks.get(COPPER));
		Assert.assertEquals(Long.valueOf(482480), stocks.get(IRON));
		Assert.assertEquals(Long.valueOf(329736), stocks.get(AMBER));
		Assert.assertEquals(Long.valueOf(721501), stocks.get(ALUMINIUM));
		Assert.assertEquals(Long.valueOf(237747), stocks.get(SILVER));
		Assert.assertEquals(Long.valueOf(579032), stocks.get(GOLD));
		Assert.assertEquals(Long.valueOf(451145), stocks.get(EMERALD));
		Assert.assertEquals(Long.valueOf(484), stocks.get(PLATINUM));
	}

	@Test
	public void test_screens_fire_a2() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_a2.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(8,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(309595), stocks.get(TOPAZ));
		Assert.assertEquals(Long.valueOf(340464), stocks.get(RUBY));
		Assert.assertEquals(Long.valueOf(54821), stocks.get(SAPPHIRE));
		Assert.assertEquals(Long.valueOf(1165), stocks.get(AMETHYST));
		Assert.assertEquals(Long.valueOf(244298), stocks.get(DIAMOND));
		Assert.assertEquals(Long.valueOf(7409), stocks.get(TITANIUM_ORE));
		Assert.assertEquals(Long.valueOf(208), stocks.get(ALEXANDRITE));
		Assert.assertEquals(Long.valueOf(10203), stocks.get(URANIUM));
	}

	@Test
	public void test_screens_fire_c1() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_c1.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(112959), stocks.get(WATER));
		Assert.assertEquals(Long.valueOf(8444), stocks.get(OIL));
		Assert.assertEquals(Long.valueOf(5690), stocks.get(SULFUR));
		Assert.assertEquals(Long.valueOf(22860), stocks.get(SILICON));
		Assert.assertEquals(Long.valueOf(16), stocks.get(SODIUM));
		Assert.assertEquals(Long.valueOf(32), stocks.get(HYDROGEN));
		Assert.assertEquals(Long.valueOf(13277), stocks.get(GLASS));
		Assert.assertEquals(Long.valueOf(1437), stocks.get(OXYGEN));
		Assert.assertEquals(Long.valueOf(181), stocks.get(CLEAN_WATER));
	}

	@Test
	public void test_screens_fire_c2() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_c2.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(13277), stocks.get(GLASS));
		Assert.assertEquals(Long.valueOf(1437), stocks.get(OXYGEN));
		Assert.assertEquals(Long.valueOf(181), stocks.get(CLEAN_WATER));
		Assert.assertEquals(Long.valueOf(125), stocks.get(SULFURIC_ACID));
		Assert.assertEquals(Long.valueOf(4864), stocks.get(RUBBER));
		Assert.assertEquals(Long.valueOf(115), stocks.get(REFINED_OIL));
		Assert.assertEquals(Long.valueOf(341), stocks.get(DIETHYL_ETHER));
		Assert.assertTrue(stocks.keySet().contains(URANIUM_ROD));
		Assert.assertEquals(Long.valueOf(610), stocks.get(URANIUM_ROD));
		Assert.assertEquals(Long.valueOf(26), stocks.get(PLASTIC));
	}

	@Test
	public void test_screens_fire_d1() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_d1.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(4226), stocks.get(POLISHED_AMBER));
		Assert.assertEquals(Long.valueOf(29348), stocks.get(POLISHED_EMERALD));
		Assert.assertEquals(Long.valueOf(5630), stocks.get(POLISHED_TOPAZ));
		Assert.assertEquals(Long.valueOf(6372), stocks.get(POLISHED_SAPPHIRE));
		Assert.assertEquals(Long.valueOf(15109), stocks.get(POLISHED_RUBY));
		Assert.assertEquals(Long.valueOf(6863), stocks.get(POLISHED_AMETHYST));
		Assert.assertEquals(Long.valueOf(2218), stocks.get(POLISHED_ALEXANDRITE));
		Assert.assertEquals(Long.valueOf(35675), stocks.get(POLISHED_DIAMOND));
		Assert.assertEquals(Long.valueOf(1490), stocks.get(MAYAN_CALENDAR));
	}

	@Test
	public void test_screens_fire_d2() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("720_en_d2.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(7,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(6372), stocks.get(POLISHED_SAPPHIRE));
		Assert.assertEquals(Long.valueOf(15109), stocks.get(POLISHED_RUBY));
		Assert.assertEquals(Long.valueOf(6863), stocks.get(POLISHED_AMETHYST));
		Assert.assertEquals(Long.valueOf(2218), stocks.get(POLISHED_ALEXANDRITE));
		Assert.assertEquals(Long.valueOf(35676), stocks.get(POLISHED_DIAMOND));
		Assert.assertEquals(Long.valueOf(1490), stocks.get(MAYAN_CALENDAR));
		Assert.assertEquals(Long.valueOf(40), stocks.get(HAIR_COMB));
	}

	@Test
	public void en_720x1280_20171011193815() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("en_720x1280_2017-10-11-19-38-15.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(1054), stocks.get(AMBER_CHARGER));
		Assert.assertEquals(Long.valueOf(3943), stocks.get(ALUMINIUM_BOTTLE));
		Assert.assertEquals(Long.valueOf(56), stocks.get(BOMB));
		Assert.assertEquals(Long.valueOf(5), stocks.get(GEAR));
		Assert.assertEquals(Long.valueOf(5552), stocks.get(BATTERY));
		Assert.assertEquals(Long.valueOf(2938), stocks.get(LAMP));
		Assert.assertEquals(Long.valueOf(16), stocks.get(COMPRESSOR));
		Assert.assertEquals(Long.valueOf(61), stocks.get(ItemStatics.ACCUMULATOR));
		Assert.assertEquals(Long.valueOf(957), stocks.get(SOLID_PROPELLANT));
	}

	@Test
	public void en_720x1280_20171011193835() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("en_720x1280_2017-10-11-19-38-35.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(1644), stocks.get(HYDROGEN));
		Assert.assertEquals(Long.valueOf(4587), stocks.get(GLASS));
		Assert.assertEquals(Long.valueOf(4191), stocks.get(OXYGEN));
		Assert.assertEquals(Long.valueOf(2406), stocks.get(CLEAN_WATER));
		Assert.assertEquals(Long.valueOf(282), stocks.get(SULFURIC_ACID));
		Assert.assertEquals(Long.valueOf(29649), stocks.get(RUBBER));
		Assert.assertEquals(Long.valueOf(284), stocks.get(ETHANOL));
		Assert.assertEquals(Long.valueOf(421), stocks.get(LIQUID_NITROGEN));
		Assert.assertEquals(Long.valueOf(187), stocks.get(REFINED_OIL));
	}

	@Test
	public void en_720x1280_20171011193909() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("en_720x1280_2017-10-11-19-39-09.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(4226), stocks.get(POLISHED_AMBER));
		Assert.assertEquals(Long.valueOf(47511), stocks.get(POLISHED_EMERALD));
		Assert.assertEquals(Long.valueOf(7832), stocks.get(POLISHED_TOPAZ));
		Assert.assertEquals(Long.valueOf(10311), stocks.get(POLISHED_SAPPHIRE));
		Assert.assertEquals(Long.valueOf(31464), stocks.get(POLISHED_RUBY));
		Assert.assertEquals(Long.valueOf(7171), stocks.get(POLISHED_AMETHYST));
		Assert.assertEquals(Long.valueOf(2304), stocks.get(POLISHED_ALEXANDRITE));
		Assert.assertEquals(Long.valueOf(236), stocks.get(POLISHED_OBSIDIAN));
		Assert.assertEquals(Long.valueOf(19043), stocks.get(POLISHED_DIAMOND));
	}

	@Test
	public void en_720x1280_20171011193957() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("en_720x1280_2017-10-11-19-39-57.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(56128), stocks.get(SILICON));
		Assert.assertEquals(Long.valueOf(31), stocks.get(SODIUM));
		Assert.assertEquals(Long.valueOf(47513), stocks.get(POLISHED_EMERALD));
		Assert.assertEquals(Long.valueOf(18901), stocks.get(STEEL_BAR));
		Assert.assertEquals(Long.valueOf(25472), stocks.get(TREE));
		Assert.assertEquals(Long.valueOf(10281), stocks.get(SILVER_BAR));
		Assert.assertEquals(Long.valueOf(7832), stocks.get(POLISHED_TOPAZ));
		Assert.assertEquals(Long.valueOf(10311), stocks.get(POLISHED_SAPPHIRE));
		Assert.assertEquals(Long.valueOf(31465), stocks.get(POLISHED_RUBY));
	}

	@Test
	public void en_720x1280_20171011194637() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("en_720x1280_2017-10-11-19-46-37.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(5447), stocks.get(NITROGEN));
		Assert.assertEquals(Long.valueOf(1646), stocks.get(HYDROGEN));
		Assert.assertEquals(Long.valueOf(4587), stocks.get(GLASS));
		Assert.assertEquals(Long.valueOf(4192), stocks.get(OXYGEN));
		Assert.assertEquals(Long.valueOf(2407), stocks.get(CLEAN_WATER));
		Assert.assertEquals(Long.valueOf(60), stocks.get(GUNPOWDER));
		Assert.assertEquals(Long.valueOf(264), stocks.get(SULFURIC_ACID));
		Assert.assertEquals(Long.valueOf(29649), stocks.get(RUBBER));
		Assert.assertEquals(Long.valueOf(276), stocks.get(ETHANOL));
	}

	@Test
	public void en_720x1280_20171011195913() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("en_720x1280_2017-10-11-19-59-13.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(148243), stocks.get(DIAMOND));
		Assert.assertEquals(Long.valueOf(118917), stocks.get(TITANIUM_ORE));
		Assert.assertEquals(Long.valueOf(72689), stocks.get(ALEXANDRITE));
		Assert.assertEquals(Long.valueOf(1), stocks.get(HELIUM_3));
		Assert.assertEquals(Long.valueOf(250755), stocks.get(OBSIDIAN));
		Assert.assertEquals(Long.valueOf(14268), stocks.get(OIL));
		Assert.assertEquals(Long.valueOf(997), stocks.get(URANIUM));
		Assert.assertEquals(Long.valueOf(64590), stocks.get(COPPER_BAR));
		Assert.assertEquals(Long.valueOf(39266), stocks.get(IRON_BAR));
	}

	@Test
	public void en_720x1280_20171011201041() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("en_720x1280_2017-10-11-20-10-41.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(9,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(95460), stocks.get(WATER));
		Assert.assertEquals(Long.valueOf(14258), stocks.get(OIL));
		Assert.assertEquals(Long.valueOf(26462), stocks.get(SULFUR));
		Assert.assertEquals(Long.valueOf(56203), stocks.get(SILICON));
		Assert.assertEquals(Long.valueOf(11), stocks.get(SODIUM));
		Assert.assertEquals(Long.valueOf(100), stocks.get(TITANIUM));
		Assert.assertEquals(Long.valueOf(5465), stocks.get(NITROGEN));
		Assert.assertEquals(Long.valueOf(1644), stocks.get(HYDROGEN));
		Assert.assertEquals(Long.valueOf(4587), stocks.get(GLASS));
	}

	@Test
	public void en_720x1280_20171011201622() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("en_720x1280_2017-10-11-20-16-22.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(7,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(2304), stocks.get(POLISHED_ALEXANDRITE));
		Assert.assertEquals(Long.valueOf(236), stocks.get(POLISHED_OBSIDIAN));
		Assert.assertEquals(Long.valueOf(3), stocks.get(AMBER_BRACELET));
		Assert.assertEquals(Long.valueOf(19043), stocks.get(POLISHED_DIAMOND));
		Assert.assertEquals(Long.valueOf(1), stocks.get(EMERALD_RING));
		Assert.assertEquals(Long.valueOf(1490), stocks.get(MAYAN_CALENDAR));
		Assert.assertEquals(Long.valueOf(40), stocks.get(HAIR_COMB));
	}

	@Test
	public void en_720x1280_20171011205851() throws Exception {
		InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("en_720x1280_2017-10-11-20-58-51.png");
		Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
		Assert.assertEquals(8,stocks.keySet().size());
		Assert.assertEquals(Long.valueOf(2336), stocks.get(POLISHED_ALEXANDRITE));
		Assert.assertEquals(Long.valueOf(36), stocks.get(POLISHED_OBSIDIAN));
		Assert.assertEquals(Long.valueOf(3), stocks.get(AMBER_BRACELET));
		Assert.assertEquals(Long.valueOf(19043), stocks.get(POLISHED_DIAMOND));
		Assert.assertEquals(Long.valueOf(1), stocks.get(EMERALD_RING));
		Assert.assertEquals(Long.valueOf(1490), stocks.get(MAYAN_CALENDAR));
		Assert.assertEquals(Long.valueOf(5), stocks.get(OBSIDIAN_KNIFE));
		Assert.assertEquals(Long.valueOf(40), stocks.get(HAIR_COMB));
	}

	@Test
	public void en_720x1280_largeNumber() throws Exception {
	    InputStream stream = OcrTestEnglish.class.getClassLoader().getResourceAsStream("en_720x1280_largeNumber.png");
	    Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.ENGLISH);
	    Assert.assertEquals(9,stocks.keySet().size());
	    Assert.assertEquals(Long.valueOf(1145), stocks.get(MAYAN_CALENDAR));
	    Assert.assertEquals(Long.valueOf(29), stocks.get(LIQUID_NITROGEN));
	    Assert.assertEquals(Long.valueOf(46), stocks.get(OBSIDIAN_KNIFE));
	    Assert.assertEquals(Long.valueOf(497), stocks.get(HAIR_COMB));
	    Assert.assertEquals(Long.valueOf(1262061947), stocks.get(REFINED_OIL));
	    Assert.assertEquals(Long.valueOf(252), stocks.get(MOTHERBOARD));
	    Assert.assertEquals(Long.valueOf(396), stocks.get(URANIUM_ROD));
	    Assert.assertEquals(Long.valueOf(2026), stocks.get(PLASTIC));
	    Assert.assertEquals(Long.valueOf(7285), stocks.get(AMBER_CHARGER));
	}
}
