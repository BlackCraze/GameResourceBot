package de.blackcraze.grb.ocr;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.Java2DFrameUtils;

import de.blackcraze.grb.i18n.Resource;
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OCR {

    /**
     * preprocess with {@link Preprocessor}
     *
     * @throws IOException
     */
    public static Map<String, Long> convertToStocks(List<Mat> extract, Locale locale) throws TesseractException {
        System.setProperty("jna.encoding", "UTF8");

        Map<String, Long> stocks = new HashMap<>((int) extract.size());

        Tesseract num = getTeseract(false);
        Tesseract head = getTeseract(true);

        for (int i = 0; i < extract.size(); i += 2) {
            String itemName = doOcr(extract.get(i), head);
            itemName = StringUtils.strip(itemName.replace("\n", "").replace("\r", ""));
            String key;
            try {
                key = Resource.getItemKey(itemName, locale);
            } catch (Exception e) {
                // we add the clear text stock name with an identifier value
                // this way we can show the user an error message showing
                // errored type names
                stocks.put(itemName, Long.MIN_VALUE);
                continue;
            }
            String value = doOcr(extract.get(i + 1), num);
            String valueCorrected = StringUtils.replaceAll(value, "\\D", "");
            try {
                Long valueOf = Long.valueOf(valueCorrected);
                stocks.put(key, valueOf);
            } catch (NumberFormatException e) {
                System.err.println("could not convert to number: '" + value + "'");
                stocks.put(itemName + ": '" + value + "'", Long.MIN_VALUE);
            }
        }
        return stocks;
    }

    private static String doOcr(Mat value, Tesseract tess) throws TesseractException {
        return tess.doOCR(Java2DFrameUtils.toBufferedImage(value)); // FIXME
    }

    public static Tesseract getTeseract(boolean header) {
        Tesseract instance = new Tesseract();
        instance.setTessVariable("load_system_dawg", "F");
        instance.setTessVariable("load_freq_dawg", "F");
        instance.setTessVariable("user_words_suffix", "user-words");
        instance.setTessVariable("language_model_penalty_non_dict_word", "1");
        instance.setTessVariable("debug_file", "/dev/null");
        instance.setLanguage("deu");
        instance.setDatapath("./tessdata");
        if (header) {
            instance.setTessVariable("tessedit_char_whitelist", "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ");
            instance.setPageSegMode(TessPageSegMode.PSM_SINGLE_COLUMN);
        } else {
            instance.setTessVariable("tessedit_char_whitelist", "0123456789");
            instance.setPageSegMode(TessPageSegMode.PSM_SINGLE_LINE);
        }
        return instance;
    }

}
