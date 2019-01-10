package de.blackcraze.grb.ocr.i18n;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import de.blackcraze.grb.i18n.Resource;

public class ItemParseTest {

    @Test
    public void translateSomeItems() {
        Assert.assertEquals("Kohle", Resource.getItem("COAL", Locale.GERMAN));
        Assert.assertEquals("Coal", Resource.getItem("COAL", Locale.ENGLISH));
    }

}
