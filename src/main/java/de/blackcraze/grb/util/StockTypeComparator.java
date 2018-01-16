package de.blackcraze.grb.util;

import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.entity.StockType;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class StockTypeComparator implements Comparator<StockType> {

    private final Locale locale;
    private final Collator collator;

    public StockTypeComparator(Locale locale) {
        this.locale = locale;
        this.collator = Collator.getInstance(locale);
        this.collator.setStrength(Collator.SECONDARY);// a == A, a < Ä
    }

    @Override
    public int compare(StockType o1, StockType o2) {
        String o1Name = Resource.getItem(o1.getName(), this.locale);
        String o2Name = Resource.getItem(o2.getName(), this.locale);
        return this.collator.compare(o1Name, o2Name);
    }

}
