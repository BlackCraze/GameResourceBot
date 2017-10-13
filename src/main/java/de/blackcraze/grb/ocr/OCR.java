package de.blackcraze.grb.ocr;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.i18n.Resource;

public class OCR {

    private static final String ITEM_CHAR_FILTER = "[^ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜabcdefghijklmnopqrstuvwxyzäöü]";

    /**
     * preprocess with {@link Preprocessor}
     *
     * @throws IOException
     */
    public static Map<String, Long> convertToStocks(InputStream stream, Locale locale) throws IOException {
        Map<String, Long> stocks = new HashMap<>();
        List<File> frames = Preprocessor.load(stream);
        for (File frame : frames) {
            try {
                File[] pair = Preprocessor.extract(frame);
                File text = pair[0];
                File number = pair[1];

                if (text == null && number == null) {
                    // thats okay - empty fragment
                    // nothing to do
                    continue;
                }

                String itemName = doOcr(text, true);
                itemName = StringUtils.strip(StringUtils.replaceAll(itemName, ITEM_CHAR_FILTER, ""));
                try {
                    if (StringUtils.isEmpty(itemName)) {
                        continue;
                    }
                    itemName = Resource.getItemKey(itemName, locale);
                } catch (Exception e) {
                    // we add the clear text stock name with an identifier value
                    // this way we can show the user an error message showing
                    // errored type names
                    stocks.put(itemName, Long.MIN_VALUE);
                    continue;
                } finally {
                    if (text != null) {
                        text.delete();
                    }
                }

                String value = doOcr(number, false);
                String valueCorrected = StringUtils.replaceAll(value, "\\D", "");
                try {
                    Long valueOf = Long.valueOf(valueCorrected);
                    stocks.put(itemName, valueOf);
                } catch (NumberFormatException e) {
                    System.err.println("could not convert to number: '" + value + "'");
                    stocks.put(itemName + ": '" + value + "'", Long.MIN_VALUE);
                } finally {
                    if (number != null) {
                        number.delete();
                    }
                }
            } finally {
                if (frame != null) {
                    frame.delete();
                }
            }
        }
        return stocks;
    }

    private static String doOcr(File tempFile, boolean header) throws IOException {
        if (tempFile == null) {
            return null;
        }
        TessBaseAPI api = new TessBaseAPI();
        System.setProperty("jna.encoding", "UTF8");
        try {
            if (header) {
                api.SetVariable("tessedit_char_whitelist", "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ");
                api.SetPageSegMode(7);
            } else {
                api.SetVariable("tessedit_char_whitelist", "0123456789");
                api.SetPageSegMode(4);
            }
            int init = api.Init(BotConfig.TESS_DATA, "deu");
            if (init != 0) {
                throw new RuntimeException("Could not initialize tesseract.");
            }

            PIX image = pixRead(tempFile.getAbsolutePath());
            api.SetImage(image);
            BytePointer outText = api.GetUTF8Text();
            String out = outText.getString("UTF-8");
            outText.deallocate();
            pixDestroy(image);
            return out;
        } finally {
            api.End();
            api.close();
            tempFile.delete();
        }
    }

}
