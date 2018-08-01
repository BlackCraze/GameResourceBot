package de.blackcraze.grb.i18n;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;

import com.sksamuel.diffpatch.DiffMatchPatch;
import com.sksamuel.diffpatch.DiffMatchPatch.Diff;

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
            throw new RuntimeException(String.format(Resource.getError("CANT_FIND_KEY", locale),
                    item, baseName, locale.toLanguageTag()));
            /*
             * ORIGINAL VERSION OF PREVIOUS LINE BELOW throw new RuntimeException(String.format(
             * "Can't find %s in %s %s", item, baseName, locale.toLanguageTag()));
             */
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

    public static String getError(String key, Locale locale) {
        return getResource(key, locale, "errors");
    }

    public static String getHeader(String key, Locale locale) {
        return getResource(key, locale, "headers");
    }

    public static String getHelp(String key, Locale locale) {
        return getResource(key, locale, "help");
    }

    public static String getInfo(String key, Locale locale) {
        return getResource(key, locale, "inform");
    }

    private static String getResource(String key, Locale locale, String baseName) {
        ResourceBundle resourceBundle =
                ResourceBundle.getBundle(baseName, locale, new XMLResourceBundleControl());
//        Enumeration<String> keys = resourceBundle.getKeys();
//        System.out.println("RESOURCEBUNDLE: " + baseName);
//        while (keys.hasMoreElements()) {
//            String aKey = keys.nextElement();
//            System.out.println(aKey + ": " + resourceBundle.getString(aKey));
//        }
        return resourceBundle.getString(key);
    }

}
