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
        /* BLACKCRAZE: I created headers for i18n here because this is a command that any user CAN use, even if they
            probably won't. Instead of using %d within the strings, I kept your "append" format. Because of this,
            several of the strings BEGIN with %n. My intent was to reduce clutter from the .append("\n") code.
            If that won't work, then, obviously, you'll want to fix it.
            Alternatively, your original code is here, in case you think we shouldn't bother with this one.
            I still maintain my opinion, though, that we should translate anything that isn't exclusively in the logs.
         */
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
        /* ORIGINAL VERSION OF PREVIOUS 9 LINES (WITH MINOR CORRECTIONS) BELOW
        sb.append("JAVACPP:\n");
        sb.append("Memory tracked by deallocators: ").append(formatBytes(totalBytes()))
                .append("\n");
        sb.append("Maximum memory allowed to be tracked: ").append(formatBytes(maxBytes()))
                .append("\n");
        try {
            sb.append(
                    "Physical memory installed, as reported by operating system (0 if unknown): ")
                    .append(formatBytes(totalPhysicalBytes())).append("\n");
            sb.append("Maximum physical memory that should be used: ")
                    .append(formatBytes(maxPhysicalBytes())).append("\n");
            sb.append(
                    "Physical memory free, as reported by operating system (0 if unknown): ")
                    .append(formatBytes(availablePhysicalBytes())).append("\n");
            sb.append("Physical memory currently used by whole process (0 if unknown): ")
                    .append(formatBytes(physicalBytes())).append("\n");
        } catch (UnsatisfiedLinkError e) {
            sb.append("No physical data available.");
        */
        }
        Speaker.sayCode(message.getChannel(), sb.toString());
    }

}
