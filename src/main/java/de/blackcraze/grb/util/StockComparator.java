package de.blackcraze.grb.util;

import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.entity.Stock;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * sorts a list of stocks by the name of their localized stock type names
 *
 * @author blackcraze
 *
 */
public class StockComparator implements Comparator<Stock> {

    private final Collator collator;
    private final Locale locale;

    public StockComparator(Locale locale) {
        this.locale = locale;
        // for correct placement of german umlauts
        this.collator = Collator.getInstance(locale);
        this.collator.setStrength(Collator.SECONDARY);// a == A, a < Ä
    }

    @Override
    public int compare(Stock o1, Stock o2) {
        String o1Name = Resource.getItem(o1.getType().getName(), this.locale);
        String o2Name = Resource.getItem(o2.getType().getName(), this.locale);
        return this.collator.compare(o1Name, o2Name);
    }

}
