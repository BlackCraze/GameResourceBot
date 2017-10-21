package de.blackcraze.grb.core;

import net.dv8tion.jda.core.entities.Guild;

import java.util.*;

public class BotConfig {

    static final String[] REQUIRED_ENV_VARS = { "GRB_DISCORD_TOKEN" };

    static final String DISCORD_TOKEN = System.getenv("GRB_DISCORD_TOKEN");
    public static final String DATABASE_URL = getEnv("DATABASE_URL", "postgres://grb:grb@localhost:5432/grb");
    public static final String USE_SSL = getEnv("USE_SSL", "");
    public static final String TESS_DATA = getEnv("TESS_DATA", "./tessdata");

    private static Map<Guild, ServerConfig> servers = new HashMap<>();

    public static class ServerConfig {
        public String PREFIX = getEnv("PREFIX", "bot");
        public String CHANNEL = getEnv("CHANNEL", "statistik");
        public String LANGUAGE = getEnv("LANGUAGE", "en");
        public String OCR_RESULT = getEnv("OCR_RESULT", "on");
        public String DELETE_PICTURE_MESSAGE = getEnv("DELETE_PICTURE_MESSAGE", "off");
    }

    private static String getEnv(String envVar, String defaultValue) {
        Optional<String> envValue = Optional.ofNullable(System.getenv(envVar));
        return envValue.orElse(defaultValue);
    }

    public static ServerConfig getConfig(Guild guild) {
        if (!servers.containsKey(guild)) {
            servers.put(guild, new ServerConfig());
        }
        return servers.get(guild);
    }
}
