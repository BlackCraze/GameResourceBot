package de.blackcraze.grb.util;

import java.util.Comparator;
import java.util.Locale;

import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.entity.StockType;

public class StockTypeComparator implements Comparator<StockType> {

    private final Locale locale;

    public StockTypeComparator(Locale locale) {
        this.locale = locale;
    }

    @Override
    public int compare(StockType o1, StockType o2) {
        String o1Name = Resource.getItem(o1.getName(), this.locale);
        String o2Name = Resource.getItem(o2.getName(), this.locale);
        return o1Name.compareTo(o2Name);
    }

}
