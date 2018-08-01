package de.blackcraze.grb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.blackcraze.grb.i18n.Resource;

public class PrintableTable {
    private static final String SPACER = " ";
    private final List<List<String>> rows;
    private final String header;
    private final List<Integer> aligns;
    private final List<String> titles, footer;
    private List<Integer> widths; // calculated on demand
    private int width = -1; // calculated on demand

    public PrintableTable(String header, List<String> footer, List<String> titles,
            List<List<String>> rows, List<Integer> aligns) {

        // integrity check
        for (List<String> row : rows) {
            if (row.size() != titles.size()
                    || (!footer.isEmpty() && footer.size() != titles.size())) {
                throw new IllegalArgumentException(
                        Resource.getError("COLUMN_MISMATCH", Locale.getDefault()));
            }
        }
        for (List<String> list : rows) {
            addSpaces(list);
        }
        addSpaces(footer);
        addSpaces(titles);
        this.header = SPACER + header;
        this.rows = rows;
        this.titles = titles;
        this.footer = footer;
        this.aligns = aligns;
    }

    private void addSpaces(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).startsWith(SPACER)) {
                list.set(i, SPACER + list.get(i) + SPACER);
            }
        }
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public List<String> getFooter() {
        return footer;
    }

    public String getHeader() {
        return header;
    }

    public List<Integer> getAligns() {
        return aligns;
    }

    public List<String> getTitles() {
        return titles;
    }

    public int getWidth() {
        if (width == -1) {
            width = getWidths().size() + 1; // coulumn borders
            for (Integer columnWidth : getWidths()) {
                width += columnWidth;
            }
        }
        return width;
    }

    public List<Integer> getWidths() {
        if (widths == null) {
            widths = new ArrayList<>(rows.get(0).size());
            for (String title : titles) {
                widths.add(title.length());
            }
            for (List<String> row : rows) {
                for (int i = 0; i < row.size(); i++) {
                    // adding spacer just because adding a space looks nice
                    int width = row.get(i).length();
                    if (width > widths.get(i)) {
                        widths.set(i, width);
                    }
                }
            }
            for (int i = 0; i < footer.size(); i++) {
                int width = footer.get(i).length();
                if (width > widths.get(i)) {
                    widths.set(i, width);
                }
            }
        }
        return widths;
    }
}
