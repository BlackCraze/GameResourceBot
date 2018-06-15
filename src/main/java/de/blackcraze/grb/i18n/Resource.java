package de.blackcraze.grb.i18n;

import com.sksamuel.diffpatch.DiffMatchPatch;
import com.sksamuel.diffpatch.DiffMatchPatch.Diff;
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;

public class Resource {

    private Resource() {

    }

    public static String getItemKey(String item, Locale locale) {
        return getKey(item, locale, "items");
    }

    private static String getKey(String item, Locale locale, String baseName) {
        ResourceBundle resourceBundle =
                ResourceBundle.getBundle(baseName, locale, new XMLResourceBundleControl());

        String itemTyped = correctItemName(item);
        String bestMatch = null;
        int diffScore = Integer.MAX_VALUE;

        for (String key : resourceBundle.keySet()) {
            String itemName = correctItemName(resourceBundle.getString(key));
            if (itemTyped.equals(itemName)) {
                return key;
            } else {
                int score = compareFuzzy(itemName, itemTyped);
                if (score < diffScore) {
                    diffScore = score;
                    bestMatch = key;
                }
            }
        }
        if (scoreIsGood(itemTyped, diffScore)) {
            return bestMatch;
        } else {
            throw new RuntimeException(String.format("Can't find %s in %s %s", item, baseName,
                    locale.toLanguageTag()));
        }
    }

    private static boolean scoreIsGood(String itemTyped, int diffScore) {
        return itemTyped.length() * 0.5 >= diffScore;
    }

    public static String correctItemName(String item) {
        return item.toUpperCase().replaceAll("-", "").replaceAll("\\s+", "").replace("Ü", "U")
                .replace("Ä", "A").replace("Ö", "O");
    }

    public static int compareFuzzy(String one, String another) {
        DiffMatchPatch matcher = new DiffMatchPatch();
        LinkedList<Diff> diffs = matcher.diff_main(one, another);
        return matcher.diff_levenshtein(diffs);
    }

    public static String getItem(String key, Locale locale) {
        return getResource(key, locale, "items");
    }

    public static String getString(String key, Locale locale) {
        return getResource(key, locale, "strings");
    }

    public static String getHelp(String key, Locale locale) {
        return getResource(key, locale, "help");
    }

    private static String getResource(String key, Locale locale, String baseName) {
        ResourceBundle resourceBundle =
                ResourceBundle.getBundle(baseName, locale, new XMLResourceBundleControl());
        if (!resourceBundle.containsKey(key)) {
            System.err.printf("Can't find key %s in locale %s%n", key, locale.toLanguageTag());
        }
        return resourceBundle.getString(key);
    }

}
