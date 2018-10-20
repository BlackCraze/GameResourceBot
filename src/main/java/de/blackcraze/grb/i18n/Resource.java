package de.blackcraze.grb.i18n;

import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.lang3.StringUtils;
import com.sksamuel.diffpatch.DiffMatchPatch;
import com.sksamuel.diffpatch.DiffMatchPatch.Diff;

public class Resource {

    private Resource() {

    }

    public static String guessItemKey(String item, Locale locale) {
        ResourceBundle resourceBundle =
                ResourceBundle.getBundle("items", locale, new ResourceBundleControl());

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
            throw new RuntimeException(String.format("Can't find %s in %s %s.", item, "items",
                    locale.toLanguageTag()));
        }
    }

    public static String guessHelpKey(String key, Locale locale) {
        ResourceBundle resourceBundle =
                ResourceBundle.getBundle("help", locale, new ResourceBundleControl());
        String keyTyped = StringUtils.upperCase(key);
        String bestMatch = null;
        int diffScore = Integer.MAX_VALUE;

        for (String helpKey : resourceBundle.keySet()) {
            if (keyTyped.equals(helpKey)) {
                return helpKey;
            } else {
                int score = compareFuzzy(helpKey, keyTyped);
                if (score < diffScore) {
                    diffScore = score;
                    bestMatch = helpKey;
                }
            }
        }
        if (scoreIsGood(keyTyped, diffScore)) {
            return bestMatch;
        } else {
            return null;
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

    public static String getError(String key, Locale locale, Object... args) {
        return getResource(key, locale, "errors", args);
    }

    public static String getHeader(String key, Locale locale, Object... args) {
        return getResource(key, locale, "headers", args);
    }

    public static String getHelp(String key, Locale locale, Object... args) {
        return getResource(key, locale, "help", args);
    }

    public static String getInfo(String key, Locale locale, Object... args) {
        return getResource(key, locale, "inform", args);
    }

    private static String getResource(String key, Locale locale, String baseName, Object... args) {
        ResourceBundle resourceBundle =
                ResourceBundle.getBundle(baseName, locale, new ResourceBundleControl());
        String string = resourceBundle.getString(key);
        if (args != null && args.length > 0) {
            return String.format(string, args);
        } else {
            return string;
        }
    }

}
