package de.blackcraze.grb.commands.concrete;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;
import static de.blackcraze.grb.util.InjectorUtils.getMateDao;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.model.Device;
import de.blackcraze.grb.model.entity.Mate;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;

public class UserConfig implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        Mate mate = getMateDao().getOrCreateMate(message, getResponseLocale(message));
        if (!scanner.hasNext()) {
            StringBuilder response = new StringBuilder();
            response.append("language: ");
            response.append(mate.getLanguage());
            response.append("\n");
            response.append("device: ");
            response.append(mate.getDevice());
            response.append(" [");
            Device[] devices = Device.values();
            for (int i = 0; i < devices.length; i++) {
                response.append(devices[i].name());
                if (i + 1 < devices.length) {
                    response.append(',');
                }
            }
            response.append("]\n");
            Speaker.sayCode(message.getChannel(), response.toString());
            return;
        }
        String config_action = scanner.next();
        if ("set".equalsIgnoreCase(config_action)) {
            String field = scanner.next();
            String value = scanner.next();
            if (Objects.isNull(field) || Objects.isNull(value)) {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                return;
            }
            try {
                if ("language".equalsIgnoreCase(field)) {
                    new Locale(value); // may throw an exception
                    mate.setLanguage(value);
                    getMateDao().update(mate);
                    message.addReaction(Speaker.Reaction.SUCCESS).queue();
                } else if ("device".equalsIgnoreCase(field)) {
                    Device dev = Device.valueOf(StringUtils.upperCase(value));
                    mate.setDevice(dev);
                    getMateDao().update(mate);
                    message.addReaction(Speaker.Reaction.SUCCESS).queue();
                } else {
                    throw new IllegalStateException();
                }
            } catch (IllegalArgumentException e) {
                // when setting an unknown device
                message.addReaction(Speaker.Reaction.FAILURE).queue();
            } catch (Exception e) {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                e.printStackTrace();
            }
        }
    }

}
