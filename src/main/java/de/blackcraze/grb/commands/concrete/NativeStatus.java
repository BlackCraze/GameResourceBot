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
        sb.append("JAVACPP:\n");
        sb.append("memory tracked by deallocators: ").append(formatBytes(totalBytes()))
                .append("\n");
        sb.append("maximum memory allowed to be tracked: ").append(formatBytes(maxBytes()))
                .append("\n");
        try {
            sb.append(
                    "physical memory installed according to the operating system, or 0 if unknown: ")
                    .append(formatBytes(totalPhysicalBytes())).append("\n");
            sb.append("maximum physical memory that should be used: ")
                    .append(formatBytes(maxPhysicalBytes())).append("\n");
            sb.append(
                    "physical memory that is free according to the operating system, or 0 if unknown: ")
                    .append(formatBytes(availablePhysicalBytes())).append("\n");
            sb.append("physical memory currently used by the whole process, or 0 if unknown: ")
                    .append(formatBytes(physicalBytes())).append("\n");
        } catch (UnsatisfiedLinkError e) {
            sb.append("no physical Data Available");
        }
        Speaker.sayCode(message.getChannel(), sb.toString());
    }

}
