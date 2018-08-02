package de.blackcraze.grb.commands.concrete;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static org.bytedeco.javacpp.Pointer.availablePhysicalBytes;
import static org.bytedeco.javacpp.Pointer.formatBytes;
import static org.bytedeco.javacpp.Pointer.maxBytes;
import static org.bytedeco.javacpp.Pointer.maxPhysicalBytes;
import static org.bytedeco.javacpp.Pointer.physicalBytes;
import static org.bytedeco.javacpp.Pointer.totalBytes;
import static org.bytedeco.javacpp.Pointer.totalPhysicalBytes;

import java.util.Locale;
import java.util.Scanner;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import net.dv8tion.jda.core.entities.Message;

public class NativeStatus implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        StringBuilder sb = new StringBuilder();
        Locale locale = getResponseLocale(message);
        sb.append(Resource.getHeader("JAVACPP", locale));
        sb.append(Resource.getHeader("DEALLOCATORS", locale)).append(formatBytes(totalBytes()));
        sb.append(Resource.getHeader("MAX_MEM_TRACKED", locale)).append(formatBytes(maxBytes()))
                .append("\n");
        try {
            sb.append(Resource.getHeader("PHYS_MEM_INSTALLED", locale))
                    .append(formatBytes(totalPhysicalBytes()));
            sb.append(Resource.getHeader("MAX_PHYS_MEM", locale))
                    .append(formatBytes(maxPhysicalBytes()));
            sb.append(Resource.getHeader("PHYS_MEM_FREE", locale))
                    .append(formatBytes(availablePhysicalBytes()));
            sb.append(Resource.getHeader("PHYS_MEM_WHOLE", locale))
                    .append(formatBytes(physicalBytes())).append("\n");
        } catch (UnsatisfiedLinkError e) {
            sb.append(Resource.getError("NO_PHYS_DATA", locale));
        }
        Speaker.sayCode(message.getChannel(), sb.toString());
    }

}
