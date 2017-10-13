package de.blackcraze.grb.ocr;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class SubImageExtractionTest {

    /*
     * 0 1 2 3 * 4 * 5 * 6 7 8 9 * 10 *
     **/
    @Test
    public void extractTillEnd() {
        List<Integer> rows = Arrays.asList(3, 4, 5, 9, 10, 11);
        List<SubImage> x = Preprocessor.extractSubimageBetweenMaskedRows(rows, 11, 1);
        Assert.assertEquals(2, x.size());
        Assert.assertEquals(0, x.get(0).y);
        Assert.assertEquals(3, x.get(0).height);
        Assert.assertEquals(6, x.get(1).y);
        Assert.assertEquals(3, x.get(1).height);
    }

    /*
     * 0 1 2 3 * 4 * 5 * 6 7 8 9 * 10 *
     **/
    @Test
    public void filterTheEnd() {
        List<Integer> rows = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        List<SubImage> x = Preprocessor.extractSubimageBetweenMaskedRows(rows, 10, 5);
        Assert.assertEquals(0, x.size());
    }

    @Test
    public void dontFilterTheEnd() {
        List<Integer> rows = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        List<SubImage> x = Preprocessor.extractSubimageBetweenMaskedRows(rows, 10, 2);
        Assert.assertEquals(1, x.size());
        Assert.assertEquals(8, x.get(0).y);
        Assert.assertEquals(2, x.get(0).height);
    }

    /*
     * 0 * 1 * 2 * 3 4 5 * 6 * 7 8 9 * 10 *
     */
    @Test
    public void extractFromStart() {
        List<Integer> rows = Arrays.asList(0, 1, 2, 5, 6, 9, 10, 11);
        List<SubImage> x = Preprocessor.extractSubimageBetweenMaskedRows(rows, 11, 1);
        Assert.assertEquals(2, x.size());
        Assert.assertEquals(3, x.get(0).y);
        Assert.assertEquals(2, x.get(0).height);
        Assert.assertEquals(7, x.get(1).y);
        Assert.assertEquals(2, x.get(1).height);
    }

    /*
     * 0 --> miss 1 * 2 --> miss 3 * 4 --> miss 5 * 6 * 7 8 9 * 10 *
     */
    @Test
    public void ignoreLowHeights() {
        List<Integer> rows = Arrays.asList(1, 3, 5, 6, 9, 10, 11);
        List<SubImage> x = Preprocessor.extractSubimageBetweenMaskedRows(rows, 11, 2);
        Assert.assertEquals(1, x.size());
        Assert.assertEquals(7, x.get(0).y);
        Assert.assertEquals(2, x.get(0).height);
    }

    /*
     * 0 * 1 * 2 * 3 * 4 * 5 * 6 * 7 8 9 10 11 12 13 14 15 16 17 18 19 20
     */
    @Test
    public void encounterImageHeight() {
        List<Integer> rows = Arrays.asList(0, 1, 2, 3, 4, 5, 6);
        List<SubImage> x = Preprocessor.extractSubimageBetweenMaskedRows(rows, 21, 1);
        Assert.assertEquals(1, x.size());
        Assert.assertEquals(7, x.get(0).y);
        Assert.assertEquals(14, x.get(0).height);
    }

    @Test
    public void noMask() {
        List<Integer> rows = Arrays.asList();
        List<SubImage> x = Preprocessor.extractSubimageBetweenMaskedRows(rows, 21, 1);
        Assert.assertEquals(1, x.size());
        Assert.assertEquals(0, x.get(0).y);
        Assert.assertEquals(21, x.get(0).height);
    }

    @Test
    public void fullMask() {
        List<Integer> rows = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
        List<SubImage> x = Preprocessor.extractSubimageBetweenMaskedRows(rows, 11, 1);
        Assert.assertEquals(0, x.size());
    }

}
