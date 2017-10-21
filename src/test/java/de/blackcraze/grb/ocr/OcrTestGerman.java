package de.blackcraze.grb.ocr;

import static de.blackcraze.grb.ocr.ItemStatics.*;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import junit.framework.Assert;

public class OcrTestGerman {

    @Test
    public void test_de_01() throws Exception {
        InputStream stream = OcrTestEnglishIphone.class.getClassLoader().getResourceAsStream("1080_de_01.png");
        Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.GERMAN);
        Assert.assertEquals(9, stocks.keySet().size());
        Assert.assertEquals(Long.valueOf(4910), stocks.get(SODIUM));
        Assert.assertEquals(Long.valueOf(1440), stocks.get(SILICON));
        Assert.assertEquals(Long.valueOf(859), stocks.get(HYDROGEN));
        Assert.assertEquals(Long.valueOf(4446), stocks.get(GLASS));
        Assert.assertEquals(Long.valueOf(947), stocks.get(OXYGEN));
        Assert.assertEquals(Long.valueOf(66), stocks.get(CLEAN_WATER));
        Assert.assertEquals(Long.valueOf(380), stocks.get(GUNPOWDER));
        Assert.assertEquals(Long.valueOf(379), stocks.get(SULFURIC_ACID));
        Assert.assertEquals(Long.valueOf(58), stocks.get(RUBBER));
    }

    @Test
    public void test_de_03() throws Exception {
        InputStream stream = OcrTestEnglishIphone.class.getClassLoader().getResourceAsStream("720_de_03.png");
        Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.GERMAN);
        Assert.assertEquals(9, stocks.keySet().size());
        Assert.assertEquals(Long.valueOf(5168), stocks.get(DIAMOND_CUTTER));
        Assert.assertEquals(Long.valueOf(5000), stocks.get(MAYAN_CALENDAR));
        Assert.assertEquals(Long.valueOf(32), stocks.get(HAIR_COMB));
        Assert.assertEquals(Long.valueOf(17), stocks.get(REFINED_OIL));
        Assert.assertEquals(Long.valueOf(1211), stocks.get(URANIUM_ROD));
        Assert.assertEquals(Long.valueOf(10), stocks.get(DIETHYL_ETHER));
        Assert.assertEquals(Long.valueOf(2394), stocks.get(AMBER_CHARGER));
        Assert.assertEquals(Long.valueOf(13), stocks.get(CIRCUIT));
        Assert.assertEquals(Long.valueOf(1), stocks.get(BOMB));
    }

    @Test
    public void test_screens_bc_01() throws Exception {
        InputStream stream = OcrTestGerman.class.getClassLoader().getResourceAsStream("720_de_bc_01.png");
        Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.GERMAN);
        Assert.assertEquals(9, stocks.keySet().size());
        Assert.assertEquals(Long.valueOf(134633), stocks.get(COAL));
        Assert.assertEquals(Long.valueOf(3116596), stocks.get(COPPER));
        Assert.assertEquals(Long.valueOf(1054464), stocks.get(IRON));
        Assert.assertEquals(Long.valueOf(605859), stocks.get(AMBER));
        Assert.assertEquals(Long.valueOf(2023470), stocks.get(ALUMINIUM));
        Assert.assertEquals(Long.valueOf(31038), stocks.get(WATER));
        Assert.assertEquals(Long.valueOf(444604), stocks.get(SILVER));
        Assert.assertEquals(Long.valueOf(678042), stocks.get(GOLD));
        Assert.assertEquals(Long.valueOf(235), stocks.get(TREE_SEED));
    }

    @Test
    public void test_screens_bc_02() throws Exception {
        InputStream stream = OcrTestGerman.class.getClassLoader().getResourceAsStream("720_de_bc_02.png");
        Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.GERMAN);
        Assert.assertEquals(9, stocks.keySet().size());
        Assert.assertEquals(Long.valueOf(1484845), stocks.get(EMERALD));
        Assert.assertEquals(Long.valueOf(79481), stocks.get(PLATINUM));
        Assert.assertEquals(Long.valueOf(694903), stocks.get(TOPAZ));
        Assert.assertEquals(Long.valueOf(1134595), stocks.get(RUBY));
        Assert.assertEquals(Long.valueOf(223721), stocks.get(SAPPHIRE));
        Assert.assertEquals(Long.valueOf(954357), stocks.get(AMETHYST));
        Assert.assertEquals(Long.valueOf(89057), stocks.get(DIAMOND));
        Assert.assertEquals(Long.valueOf(535933), stocks.get(ALEXANDRITE));
        Assert.assertEquals(Long.valueOf(464842), stocks.get(TITANIUM_ORE));
    }

    @Test
    public void test_screens_bc_03() throws Exception {
        InputStream stream = OcrTestGerman.class.getClassLoader().getResourceAsStream("720_de_bc_03.png");
        Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.GERMAN);
        Assert.assertEquals(9, stocks.keySet().size());
        Assert.assertEquals(Long.valueOf(12991), stocks.get(OIL));
        Assert.assertEquals(Long.valueOf(64697), stocks.get(URANIUM));
        Assert.assertEquals(Long.valueOf(12197), stocks.get(COPPER_BAR));
        Assert.assertEquals(Long.valueOf(3686), stocks.get(IRON_BAR));
        Assert.assertEquals(Long.valueOf(11), stocks.get(ALUMINIUM_BAR));
        Assert.assertEquals(Long.valueOf(278), stocks.get(POLISHED_AMBER));
        Assert.assertEquals(Long.valueOf(6770), stocks.get(SULFUR));
        Assert.assertEquals(Long.valueOf(34), stocks.get(SODIUM));
        Assert.assertEquals(Long.valueOf(27353), stocks.get(SILICON));
    }

    @Test
    public void test_screens_bc_04() throws Exception {
        InputStream stream = OcrTestGerman.class.getClassLoader().getResourceAsStream("720_de_bc_04.png");
        Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.GERMAN);
        Assert.assertEquals(9, stocks.keySet().size());
        Assert.assertEquals(Long.valueOf(3619), stocks.get(POLISHED_EMERALD));
        Assert.assertEquals(Long.valueOf(8102), stocks.get(STEEL_BAR));
        Assert.assertEquals(Long.valueOf(1791), stocks.get(TREE));
        Assert.assertEquals(Long.valueOf(206), stocks.get(POLISHED_TOPAZ));
        Assert.assertEquals(Long.valueOf(300), stocks.get(POLISHED_SAPPHIRE));
        Assert.assertEquals(Long.valueOf(2009), stocks.get(GOLD_BAR));
        Assert.assertEquals(Long.valueOf(765), stocks.get(POLISHED_RUBY));
        Assert.assertEquals(Long.valueOf(6883), stocks.get(POLISHED_AMETHYST));
        Assert.assertEquals(Long.valueOf(2489), stocks.get(SILVER)); // SILVER_BAR
                                                                     // was
                                                                     // called
                                                                     // SILVER
                                                                     // once
                                                                     // upon a
                                                                     // time =)
    }

    @Test
    public void test_screens_bc_05() throws Exception {
        InputStream stream = OcrTestGerman.class.getClassLoader().getResourceAsStream("720_de_bc_05.png");
        Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.GERMAN);
        Assert.assertEquals(9, stocks.keySet().size());
        Assert.assertEquals(Long.valueOf(19575), stocks.get(TITANIUM));
        Assert.assertEquals(Long.valueOf(4311), stocks.get(POLISHED_ALEXANDRITE));
        Assert.assertEquals(Long.valueOf(5175), stocks.get(POLISHED_DIAMOND));
        Assert.assertEquals(Long.valueOf(1303), stocks.get(HYDROGEN));
        Assert.assertEquals(Long.valueOf(13), stocks.get(GLASS));
        Assert.assertEquals(Long.valueOf(1469), stocks.get(GLASS_FLASK));
        Assert.assertEquals(Long.valueOf(944), stocks.get(OXYGEN));
        Assert.assertEquals(Long.valueOf(848), stocks.get(LIANA_SEED));
        Assert.assertEquals(Long.valueOf(7381), stocks.get(GRAPE_SEED));
    }

    @Test
    public void test_screens_bc_06() throws Exception {
        InputStream stream = OcrTestGerman.class.getClassLoader().getResourceAsStream("720_de_bc_06.png");
        Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.GERMAN);
        Assert.assertEquals(9, stocks.keySet().size());
        Assert.assertEquals(Long.valueOf(447), stocks.get(CLEAN_WATER));
        Assert.assertEquals(Long.valueOf(484), stocks.get(STEEL_PLATE));
        Assert.assertEquals(Long.valueOf(54), stocks.get(SULFURIC_ACID));
        Assert.assertEquals(Long.valueOf(279), stocks.get(RUBBER));
        Assert.assertEquals(Long.valueOf(45), stocks.get(ETHANOL));
        Assert.assertEquals(Long.valueOf(5121), stocks.get(DIAMOND_CUTTER));
        Assert.assertEquals(Long.valueOf(5000), stocks.get(MAYAN_CALENDAR));
        Assert.assertEquals(Long.valueOf(1316), stocks.get(HAIR_COMB));
        Assert.assertEquals(Long.valueOf(17), stocks.get(REFINED_OIL));
    }

    @Test
    public void test_screens_bc_07() throws Exception {
        InputStream stream = OcrTestGerman.class.getClassLoader().getResourceAsStream("720_de_bc_07.png");
        Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.GERMAN);
        Assert.assertEquals(9, stocks.keySet().size());
        Assert.assertEquals(Long.valueOf(1282), stocks.get(URANIUM_ROD));
        Assert.assertEquals(Long.valueOf(263), stocks.get(DIETHYL_ETHER));
        Assert.assertEquals(Long.valueOf(2394), stocks.get(AMBER_CHARGER));
        Assert.assertEquals(Long.valueOf(800), stocks.get(CIRCUIT));
        Assert.assertEquals(Long.valueOf(800), stocks.get(BOMB));
        Assert.assertEquals(Long.valueOf(10740), stocks.get(GRAPHITE));
        Assert.assertEquals(Long.valueOf(35), stocks.get(GEAR));
        Assert.assertEquals(Long.valueOf(26), stocks.get(GREEN_LASER));
        Assert.assertEquals(Long.valueOf(176), stocks.get(SOLAR_PANEL));
    }

    @Test
    public void test_screens_bc_08() throws Exception {
        InputStream stream = OcrTestGerman.class.getClassLoader().getResourceAsStream("720_de_bc_08.png");
        Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.GERMAN);
        Assert.assertEquals(7, stocks.keySet().size());
        Assert.assertEquals(Long.valueOf(17224), stocks.get(COPPER_WIRE));
        Assert.assertEquals(Long.valueOf(1768), stocks.get(LAMP));
        Assert.assertEquals(Long.valueOf(5426), stocks.get(AMBER_INSULATION));
        Assert.assertEquals(Long.valueOf(22727), stocks.get(INSULATED_WIRE));
        Assert.assertEquals(Long.valueOf(211), stocks.get(ACCUMULATOR));
        Assert.assertEquals(Long.valueOf(5751), stocks.get(ALUMINIUM_BOTTLE));
        Assert.assertEquals(Long.valueOf(4693), stocks.get(BATTERY));
    }

   @Test
    public void test_screens_bc_09() throws Exception {
        InputStream stream = OcrTestGerman.class.getClassLoader().getResourceAsStream("720_de_bc_09.png");
        Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.GERMAN);
        Assert.assertEquals(9, stocks.keySet().size());
        Assert.assertEquals(Long.valueOf(4762), stocks.get(DIAMOND_CUTTER));
        Assert.assertEquals(Long.valueOf(2388), stocks.get(MAYAN_CALENDAR));
        Assert.assertEquals(Long.valueOf(57), stocks.get(LIQUID_NITROGEN));
        Assert.assertEquals(Long.valueOf(212), stocks.get(HAIR_COMB));
        Assert.assertEquals(Long.valueOf(1), stocks.get(REFINED_OIL));
        Assert.assertEquals(Long.valueOf(724), stocks.get(MOTHERBOARD));
        Assert.assertEquals(Long.valueOf(1782), stocks.get(URANIUM_ROD));
        Assert.assertEquals(Long.valueOf(243), stocks.get(DIETHYL_ETHER));
        Assert.assertEquals(Long.valueOf(8), stocks.get(PLASTIC));
    }

    @Test
    public void test_screens_luc_02() throws Exception {
        InputStream stream = OcrTestGerman.class.getClassLoader().getResourceAsStream("720_de_luc_02.png");
        Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, Locale.GERMAN);
        Assert.assertEquals(9, stocks.keySet().size());
        Assert.assertEquals(Long.valueOf(72933), stocks.get(COPPER_NAIL));
        Assert.assertEquals(Long.valueOf(301), stocks.get(CIRCUIT));
        Assert.assertEquals(Long.valueOf(16), stocks.get(GRAPHITE));
        Assert.assertEquals(Long.valueOf(2592), stocks.get(GEAR));
        Assert.assertEquals(Long.valueOf(3347), stocks.get(GREEN_LASER));
        Assert.assertEquals(Long.valueOf(53), stocks.get(SOLAR_PANEL));
        Assert.assertEquals(Long.valueOf(38618), stocks.get(COPPER_WIRE));
        Assert.assertEquals(Long.valueOf(1953), stocks.get(LAMP));
        Assert.assertEquals(Long.valueOf(11297), stocks.get(AMBER_INSULATION));
    }
}
