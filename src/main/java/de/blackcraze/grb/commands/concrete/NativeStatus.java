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
        }
        // Next line added by PellaAndroid to keep nosy users away from this command.
        Speaker.say(message.getChannel(), Resource.getInfo("WHY_ARE_YOU_HERE", locale));
        Speaker.sayCode(message.getChannel(), sb.toString());
    }

}
