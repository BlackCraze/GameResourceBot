package de.blackcraze.grb.commands.concrete;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.core.Speaker;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Scanner;
import net.dv8tion.jda.core.entities.Message;

public class Config implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        BaseCommand.checkPublic(message);
        BotConfig.ServerConfig instance = BotConfig.getConfig();
        if (!scanner.hasNext()) {
            StringBuilder response = new StringBuilder();
            Field[] fields = BotConfig.ServerConfig.class.getDeclaredFields();

            for (Field field : fields) {
                Object value;
                try {
                    value = field.get(instance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
                response.append(field.getName());
                response.append(": ");
                response.append(value.toString());
                response.append("\n");
            }
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
                Field declaredField =
                        BotConfig.ServerConfig.class.getDeclaredField(field.toUpperCase());
                assert String.class.equals(declaredField.getType());
                declaredField.set(instance, value);
                message.addReaction(Speaker.Reaction.SUCCESS).queue();
            } catch (Exception e) {
                message.addReaction(Speaker.Reaction.FAILURE).queue();
                e.printStackTrace();
            }
        }
    }

}
