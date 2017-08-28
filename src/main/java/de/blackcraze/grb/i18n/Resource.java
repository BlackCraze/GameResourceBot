package de.blackcraze.grb.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class Resource {

    private Resource() {

    }

    public static String getString(String key, Locale locale) {
        String baseName = "strings";
        ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, locale, new XMLResourceBundleControl());
        if (!resourceBundle.containsKey(key)) {
            System.err.printf("Can't find key %s in locale %s%n", key, locale.toLanguageTag());
        }
        return resourceBundle.getString(key);
    }

}
