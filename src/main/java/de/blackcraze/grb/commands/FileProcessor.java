package de.blackcraze.grb.commands;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.PrintUtils.prettyPrint;
import static org.bytedeco.javacpp.Pointer.deallocateReferences;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.ocr.OCR;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Message.Attachment;

public class FileProcessor {

	public static void ocrImages(Message message) {
		for (Attachment att : message.getAttachments()) {
			if (att.isImage()) {
				Locale locale = getResponseLocale(message);
				InputStream stream = null;
				try {
					URLConnection conn = new URL(att.getProxyUrl()).openConnection();
					conn.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
					conn.connect();

                    stream = new BufferedInputStream(conn.getInputStream());
					Map<String, Long> stocks = OCR.convertToStocks(stream, locale);
					Speaker.sayCode(message.getTextChannel(), prettyPrint(stocks, locale));
					Commands.internalUpdate(message, locale, stocks);
				} catch (Throwable e) {
					Speaker.err(message, String.format(Resource.getString("ERROR_UNKNOWN", locale), e.getMessage()));
					e.printStackTrace();
				} finally {
				    IOUtils.closeQuietly(stream);
					deallocateReferences();
				}
			}
		}
	}

}
