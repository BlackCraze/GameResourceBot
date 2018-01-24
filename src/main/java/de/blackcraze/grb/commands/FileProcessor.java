package de.blackcraze.grb.commands;

import static de.blackcraze.grb.util.CommandUtils.getMateDevice;
import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.PrintUtils.prettyPrint;
import static org.bytedeco.javacpp.Pointer.deallocateReferences;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.Device;
import de.blackcraze.grb.ocr.OCR;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Message.Attachment;

public class FileProcessor {

    public static void ocrImages(Message message) {
        Locale locale = getResponseLocale(message);
        Device device = getMateDevice(message);
        for (Attachment att : message.getAttachments()) {
            if (att.isImage()) {
                InputStream stream = null;
                try {
                    // check if the filename ends with png (only working image format)
                    if(!FilenameUtils.getExtension(att.getFileName()).equalsIgnoreCase("png")){
                        Speaker.err(message, String.format(Resource.getString("ONLY_PNG_IMAGES", locale)));
                        continue;
                    }
                    URLConnection conn = new URL(att.getProxyUrl()).openConnection();
                    conn.setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    conn.connect();

                    stream = new BufferedInputStream(conn.getInputStream());
                    Map<String, Long> stocks = OCR.getInstance().convertToStocks(stream, locale, device);

                    /* decide if the result is printed into the channel */
                    if ("on".equalsIgnoreCase(BotConfig.getConfig().OCR_RESULT)) {
                        Speaker.sayCode(message.getChannel(), prettyPrint(stocks, locale));
                    }

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

        /* try to delete the message containing the upload images */
        if ("on".equalsIgnoreCase(BotConfig.getConfig().DELETE_PICTURE_MESSAGE)
                && message.getChannelType().equals(ChannelType.TEXT)) {
            try {
                message.delete().queue();
            } catch (Exception e) {
                Speaker.err(message, "Cant delete messages here :( - " + e.getMessage());
                /* Why is this message hard-coded in one language? Look for new entry CANT_DELETE_MESSAGES in strings.xml. Translate to other strings files, as needed. */
            }
        }
    }

}
