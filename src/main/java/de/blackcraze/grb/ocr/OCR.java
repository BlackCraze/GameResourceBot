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

    private TessBaseAPI api;

    private static final OCR OBJ = new OCR();

    private OCR() {
        System.setProperty("jna.encoding", "UTF8");
    }

    public static OCR getInstance() {
        return OBJ;
    }

    /**
     * preprocess with {@link Preprocessor}
     *
     * @throws IOException
     */
    public Map<String, Long> convertToStocks(InputStream stream, Locale locale) throws IOException {
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

                String itemName = null;
                try {
                    itemName = doOcr(text, getTesseractForText());
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

                String value = doOcr(number, getTesseractForNumbers());
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

    public String doOcr(File tempFile, TessBaseAPI api) throws IOException {
        if (tempFile == null) {
            return null;
        }
        PIX image = pixRead(tempFile.getAbsolutePath());
        api.SetImage(image);
        BytePointer outText = api.GetUTF8Text();
        String out = outText.getString("UTF-8");
        outText.deallocate();
        pixDestroy(image);
        return out;
    }

    public TessBaseAPI getTesseractForText() {
        getTesseract();
        api.ReadConfigFile("./tessdata/configs/chars");
        return api;
    }

    public TessBaseAPI getTesseractForNumbers() {
        getTesseract();
        api.ReadConfigFile("./tessdata/configs/digits");
        return api;
    }

    private void getTesseract() {
        if (api == null) {
            api = new TessBaseAPI();
            int init = api.Init(BotConfig.TESS_DATA, "deu");
            if (init != 0) {
                throw new RuntimeException("Could not initialize tesseract.");
            }
        }
    }

}
