package de.blackcraze.grb.i18n;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class Resource {

    private Resource() {

    }

    public static String getItemKey(String item, Locale locale) {
        return getKey(item, locale, "items");
    }

    private static String getKey(String item, Locale locale, String baseName) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, locale, new XMLResourceBundleControl());
        Optional<String> keyOptional = resourceBundle.keySet().stream()
                .filter(key -> item.equalsIgnoreCase(resourceBundle.getString(key)))
                .findFirst();
        return keyOptional.orElseThrow(() ->
                new RuntimeException(String.format("Can't find %s in %s %s", item, baseName, locale.toLanguageTag())));
    }

    public static String getItem(String key, Locale locale) {
        return getResource(key, locale, "items");
    }

    public static String getString(String key, Locale locale) {
        return getResource(key, locale, "strings");
    }

    private static String getResource(String key, Locale locale, String baseName) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, locale, new XMLResourceBundleControl());
        if (!resourceBundle.containsKey(key)) {
            System.err.printf("Can't find key %s in locale %s%n", key, locale.toLanguageTag());
        }
        return resourceBundle.getString(key);
    }

}
