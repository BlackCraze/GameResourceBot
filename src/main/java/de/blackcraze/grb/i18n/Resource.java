package de.blackcraze.grb.i18n;

import de.blackcraze.grb.core.BotConfig;

import java.util.ResourceBundle;

public class Resource {
    public static String getString(String key) {
        String baseName = "strings";
        ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, BotConfig.getLocale(), new XMLResourceBundleControl());
        if (!resourceBundle.containsKey(key)) {
            System.err.printf("Can't find key %s in locale %s%n", key, BotConfig.getLocale().toLanguageTag());
        }
        return resourceBundle.getString(key);
    }

}
