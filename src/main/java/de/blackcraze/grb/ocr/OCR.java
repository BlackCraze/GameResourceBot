package de.blackcraze.grb.ocr;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.i18n.Resource;

public class OCR {

	/**
	 * preprocess with {@link Preprocessor}
	 *
	 * @throws IOException
	 */
	public static Map<String, Long> convertToStocks(List<Mat> extract, Locale locale) throws IOException {
		System.setProperty("jna.encoding", "UTF8");

		Map<String, Long> stocks = new HashMap<>((int) extract.size());

		for (int i = 0; i < extract.size(); i += 2) {
			String itemName = doOcr(extract.get(i), true);
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
			String value = doOcr(extract.get(i + 1), false);
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

	private static String doOcr(Mat mat, boolean header) throws IOException {
		File tmp = File.createTempFile("mat", ".png"); //TODO more efficient
		tmp.deleteOnExit();
		TessBaseAPI api = new TessBaseAPI();
		try {
			imwrite(tmp.getAbsolutePath(), mat);
			if (header) {
				api.SetVariable("tessedit_char_whitelist", "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ");
				api.SetPageSegMode(7);
			} else {
				api.SetVariable("tessedit_char_whitelist", "1234567890");
				api.SetPageSegMode(4);
			}
			int init = api.Init(BotConfig.TESS_DATA, "deu");
			if (init != 0) {
				System.err.println("could not init");
			}

			PIX image = pixRead(tmp.getAbsolutePath());
			api.SetImage(image);
			BytePointer outText = api.GetUTF8Text();
			String out = outText.getString();
			outText.deallocate();
			pixDestroy(image);
			return out;
		} finally {
			api.End();
			api.close();
			tmp.delete();
		}
	}

}
