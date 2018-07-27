package de.blackcraze.grb.commands.concrete;

import static org.bytedeco.javacpp.Pointer.availablePhysicalBytes;
import static org.bytedeco.javacpp.Pointer.formatBytes;
import static org.bytedeco.javacpp.Pointer.maxBytes;
import static org.bytedeco.javacpp.Pointer.maxPhysicalBytes;
import static org.bytedeco.javacpp.Pointer.physicalBytes;
import static org.bytedeco.javacpp.Pointer.totalBytes;
import static org.bytedeco.javacpp.Pointer.totalPhysicalBytes;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import java.util.Scanner;
import net.dv8tion.jda.core.entities.Message;

public class NativeStatus implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        StringBuilder sb = new StringBuilder();
        sb.apppend(Resource.getHeader("JAVACPP", locale));
        /* ORIGINAL VERSION OF PREVIOUS LINE BELOW
        sb.append("JAVACPP:\n"); */
        sb.append(Resource.getHeader("DEALLOCATORS", locale)).append(formatBytes(totalBytes()));
        /* ORIGINAL VERSION OF PREVIOUS LINE BELOW
        sb.append("memory tracked by deallocators: ").append(formatBytes(totalBytes()))
                .append("\n"); */
        sb.append(Resource.getHeader("MAX_MEM_TRACKED", locale)).append(formatBytes(maxBytes()))
                .append("\n");
        /* ORIGINAL VERSION OF PREVIOUS LINE BELOW
        sb.append("maximum memory allowed to be tracked: ").append(formatBytes(maxBytes()))
                .append("\n"); */
        try {
            sb.append(Resource.getHeader("PHYS_MEM_INSTALLED", locale))
                    .append(formatBytes(totalPhysicalBytes()));
            /* ORIGINAL VERSION OF PREVIOUS LINE BELOW
            sb.append(
                    "physical memory installed according to the operating system, or 0 if unknown: ")
                    .append(formatBytes(totalPhysicalBytes())).append("\n"); */
            sb.append(Resource.getHeader("MAX_PHYS_MEM", locale))
                    .append(formatBytes(maxPhysicalBytes()));
            /* ORIGINAL VERSION OF PREVIOUS LINE BELOW
            sb.append("maximum physical memory that should be used: ")
                    .append(formatBytes(maxPhysicalBytes())).append("\n"); */
            sb.append(Resource.getHeader("PHYS_MEM_FREE", locale))
                    .append(formatBytes(availablePhysicalBytes()));
            /* ORIGINAL VERSION OF PREVIOUS LINE BELOW
            sb.append(
                    "physical memory that is free according to the operating system, or 0 if unknown: ")
                    .append(formatBytes(availablePhysicalBytes())).append("\n"); */
            sb.append(Resource.getHeader("PHYS_MEM_WHOLE", locale))
                    .append(formatBytes(physicalBytes())).append("\n");
            /* ORIGINAL VERSION OF PREVIOUS LINE BELOW
            sb.append("physical memory currently used by the whole process, or 0 if unknown: ")
                    .append(formatBytes(physicalBytes())).append("\n"); */
        } catch (UnsatisfiedLinkError e) {
            sb.append(Resource.getError("NO_PHYS_DATA", locale));
        /* ORIGINAL VERSION OF PREVIOUS LINE BELOW
            sb.append("no physical Data Available"); */
        }
        Speaker.sayCode(message.getChannel(), sb.toString());
    }

}
