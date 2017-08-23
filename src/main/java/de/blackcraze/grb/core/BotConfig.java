package de.blackcraze.grb.core;

import java.util.Optional;

public class BotConfig {

    static final String[] REQUIRED_ENV_VARS = {"GRB_DISCORD_TOKEN"};

    static final String DISCORD_TOKEN = System.getenv("GRB_DISCORD_TOKEN");
    public static final String CHANNEL = getEnv("CHANNEL", "statistik");
    public static final String PREFIX = getEnv("PREFIX", "dcbot");
    public static final String DATABASE_URL = getEnv("DATABASE_URL", "postgres://grb:grb@localhost:5432/grb");
    public static final String USE_SSL = getEnv("USE_SSL", "");

    private static String getEnv(String envVar, String defaultValue) {
        Optional<String> envValue = Optional.ofNullable(System.getenv(envVar));
        return envValue.orElse(defaultValue);
    }
}
