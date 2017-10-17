package de.blackcraze.grb.ocr;

import static org.bytedeco.javacpp.opencv_core.CV_8U;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.CV_8UC3;
import static org.bytedeco.javacpp.opencv_core.LINE_8;
import static org.bytedeco.javacpp.opencv_core.NORM_MINMAX;
import static org.bytedeco.javacpp.opencv_core.hconcat;
import static org.bytedeco.javacpp.opencv_core.inRange;
import static org.bytedeco.javacpp.opencv_core.normalize;
import static org.bytedeco.javacpp.opencv_core.vconcat;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.bytedeco.javacpp.opencv_imgproc.CHAIN_APPROX_SIMPLE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2HSV;
import static org.bytedeco.javacpp.opencv_imgproc.CV_DIST_L2;
import static org.bytedeco.javacpp.opencv_imgproc.CV_INTER_CUBIC;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY_INV;
import static org.bytedeco.javacpp.opencv_imgproc.RETR_CCOMP;
import static org.bytedeco.javacpp.opencv_imgproc.boundingRect;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.distanceTransform;
import static org.bytedeco.javacpp.opencv_imgproc.drawContours;
import static org.bytedeco.javacpp.opencv_imgproc.findContours;
import static org.bytedeco.javacpp.opencv_imgproc.resize;
import static org.bytedeco.javacpp.opencv_imgproc.threshold;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;

public class Preprocessor {

    private static final File TMP_FILE_DIR = new File("./target/tmp/");
    static {
        if (!TMP_FILE_DIR.exists()) {
            TMP_FILE_DIR.mkdirs();
        }
    }

    public static List<File> cropMasked(BufferedImage image) throws IOException {
        // now we iterate over the rows and remove the masked areas since the
        // text we want to read is underneath it
        WritableRaster raster = image.getRaster();
        List<Integer> rowsToRemove = new ArrayList<>();
        for (int yy = 0; yy < image.getHeight(); yy++) {
            int masked = 0;
            for (int xx = 0; xx < image.getWidth(); xx++) {
                if (matchColor(getPixelValue(raster, xx, yy), Color.MAGENTA)) {
                    masked++;
                }
            }
            float maskAmount = masked > 0 ? (float) masked / (float) image.getWidth() : 0;
            if (maskAmount > 0.02f) {
                rowsToRemove.add(yy);
            }
        }
        List<SubImage> subImages = extractSubimageBetweenMaskedRows(rowsToRemove, image.getHeight(), 50);
        List<File> result = new ArrayList<>(subImages.size() * 3);
        int i = 0;
        for (SubImage subImage : subImages) {
            BufferedImage cut = image.getSubimage(0, subImage.y, image.getWidth(), subImage.height);

            File tempFile;

            tempFile = File.createTempFile("subImage_" + i + "_c1", ".png", TMP_FILE_DIR);
            tempFile.deleteOnExit();
            ImageIO.write(cut.getSubimage(50, 0, 200, cut.getHeight()), "png", tempFile);
            result.add(tempFile);

            tempFile = File.createTempFile("subImage_" + i + "_c2", ".png", TMP_FILE_DIR);
            tempFile.deleteOnExit();
            ImageIO.write(cut.getSubimage(255, 0, 200, cut.getHeight()), "png", tempFile);
            result.add(tempFile);

            tempFile = File.createTempFile("subImage_" + i + "_c3", ".png", TMP_FILE_DIR);
            tempFile.deleteOnExit();
            ImageIO.write(cut.getSubimage(465, 0, 200, cut.getHeight()), "png", tempFile);
            result.add(tempFile);

            i++;
        }
        return result;
    }

    public static BufferedImage cropCenterScreen(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        // a fourth of screen wide is reserved for header stuff (level, coins,
        // gems ...)
        int headerHeight = width / 4;
        int footerHeight = width / 2;
        // a half of screen width is reserved for details stuff at the bottom
        // screen (sell bar, price and such stuff)
        return image.getSubimage(0, headerHeight, width, height - headerHeight - footerHeight);
    }

    /**
     * This is a try to mask the icon images from the game screen. Icons do
     * often have white or black in it (shadows and lights). This areas would be
     * left over with {@link #monochrome(int, int, WritableRaster)} and are
     * disturbing the ocr. But since the black and white parts are in the near
     * of colored areas (blue or red or green). We mask an area around the
     * colored pixels and overpaint the shadows and lights with it.
     *
     * @param image
     * @throws IOException
     */
    public static void maskIconRing(BufferedImage image) throws IOException {
        // resolution independent mask box size (720 pixels --> boxsize 10)
        WritableRaster raster = image.getRaster();
        int boxWidth = image.getWidth() / 100;
        for (int xx = 0; xx < image.getWidth(); xx++) {
            for (int yy = 0; yy < image.getHeight(); yy++) {
                int[] pixel = getPixelValue(raster, xx, yy);
                if (!matchColor(pixel, Color.MAGENTA) && matchIconRing(pixel)) {
                    paintOverArea(xx, yy, boxWidth, Color.MAGENTA, raster);
                }
            }
        }
    }

    /**
     * change an area of pixels around a pixel to a specified color
     */
    private static void paintOverArea(int centerX, int centerY, int areaSize, Color color, WritableRaster raster) {
        for (int x = centerX - areaSize; x < raster.getWidth() && x < centerX + areaSize; x++) {
            x = x < 0 ? 0 : x; // ArrayOutOfBound protection
            for (int y = centerY - areaSize; y < raster.getHeight() && y < centerY + areaSize; y++) {
                y = y < 0 ? 0 : y; // ArrayOutOfBound protection
                int[] strip = getPixelValue(raster, x, y);
                changeColor(strip, color);
                raster.setPixel(x, y, strip);
            }
        }
    }

    private static boolean matchColor(int[] pixel, Color... colors) {
        boolean match = false;
        for (Color color : colors) {
            match = pixel[0] == color.getRed() && pixel[1] == color.getGreen() && pixel[2] == color.getBlue();
            if (match) {
                break;
            }
        }
        return match;
    }

    private static void changeColor(int[] pixel, Color c) {
        pixel[0] = c.getRed();
        pixel[1] = c.getGreen();
        pixel[2] = c.getBlue();
    }

    private static int[] getPixelValue(WritableRaster raster, int xx, int yy) {
        return raster.getPixel(xx, yy, (int[]) null);
    }

    private static boolean matchIconRing(int[] pixels) {
        boolean blueAndroid = 0 <= pixels[0] && pixels[0] <= 50 && //
                0 <= pixels[1] && pixels[1] <= 240 && //
                180 <= pixels[2] && pixels[2] <= 255;
        boolean blueIphone = 0 <= pixels[0] && pixels[0] <= 120 && //
                180 <= pixels[1] && pixels[1] <= 255 && //
                180 <= pixels[2] && pixels[2] <= 255;
        return blueAndroid || blueIphone;
    }

    static final Point getCenterPoint(Mat m) {
        Rect r = boundingRect(m);
        return new Point(r.x() + (r.width() / 2), r.y() + (r.height() / 2));
    }

    public static boolean debug = false;

    private static boolean debug() {
        return debug;
    }

    private static void saveToDisk(Mat mat, String name) throws IOException {
        if (debug()) {
            File output = new File(TMP_FILE_DIR, name + ".png");
            System.out.println("debug file: " + output.getAbsolutePath());
            imwrite(output.getAbsolutePath(), mat);
        }
    }

    public static void saveToDisk(BufferedImage image, String name) throws IOException {
        if (debug()) {
            File output = new File(TMP_FILE_DIR, name + ".png");
            System.out.println("debug file: " + output.getAbsolutePath());
            ImageIO.write(image, "png", output);
        }
    }

    public static List<File> load(InputStream stream) throws IOException {
        BufferedImage image = ImageIO.read(stream);
        image = Preprocessor.resizeSource(image);
        image = Preprocessor.cropCenterScreen(image);
        Preprocessor.maskIconRing(image);
        return Preprocessor.cropMasked(image);
    }

    private static BufferedImage resizeSource(BufferedImage image) {
        double factor = 720d / image.getWidth();
        int scaledHeight = (int) (image.getHeight() * factor);
        int scaledWidth = (int) (image.getWidth() * factor);

        Image tmp = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        int type = image.getType();
        if (type == 0)
            type = 5; // hacky but does work

        BufferedImage dimg = new BufferedImage(scaledWidth, scaledHeight, type);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }

    private static File process(File srcFile, Mat maskLow, Mat maskUp, double thresh, int threshSrcBwLow,
            int threshSrcBwUp, int distTransformThreshMode, String prefix) throws IOException {
        Mat src = imread(srcFile.getAbsolutePath());
        Mat crop = process(src, maskLow, maskUp, thresh, threshSrcBwLow, threshSrcBwUp, distTransformThreshMode,
                prefix);
        File result = null;
        if (crop != null) {
            result = File.createTempFile(prefix, ".png", TMP_FILE_DIR);
            result.deleteOnExit();
            imwrite(result.getAbsolutePath(), crop);
            crop.close();
        }
        src.close();
        return result;
    }

    public static Mat process(Mat src, Mat maskLow, Mat maskUp, double thresh, int threshSrcBwLow, int threshSrcBwUp,
            int distTransformThreshMode, String prefix) throws IOException {
        int borderSize = 1;
        Mat colorFilter = src.clone();
        cvtColor(colorFilter, colorFilter, CV_BGR2HSV);
        saveToDisk(colorFilter, prefix + "_02_brgh");
        inRange(colorFilter, maskLow, maskUp, colorFilter);
        saveToDisk(colorFilter, prefix + "_03_inRange");
        threshold(colorFilter, colorFilter, 150, 255, distTransformThreshMode);
        saveToDisk(colorFilter, prefix + "_04_thresh");
        distanceTransform(colorFilter, colorFilter, CV_DIST_L2, 3);
        // Normalize the distance image for range = {0.0, 1.0}
        // so we can visualize and threshold it
        normalize(colorFilter, colorFilter, 0, 1., NORM_MINMAX, -1, null);
        threshold(colorFilter, colorFilter, thresh, 1, CV_THRESH_BINARY);

        Mat dist_8u = new Mat();
        colorFilter.convertTo(dist_8u, CV_8U);
        colorFilter.close();
        // Find total markers
        MatVector contours = new MatVector();
        Mat hierarchy = new Mat();
        dist_8u = applyBorders(dist_8u, borderSize, Scalar.WHITE); // to prevent
                                                                   // connect
                                                                   // the
        // texts connected to the
        // edge
        findContours(dist_8u, contours, hierarchy, RETR_CCOMP, CHAIN_APPROX_SIMPLE);

        Mat charMask = new Mat(dist_8u.size(), CV_8U, Scalar.BLACK);
        int minX = 9999999, minY = 9999999; // FIXME
        int maxX = 0, maxY = 0;
        int validContours = 0;
        for (int i = 0; i < contours.size(); i++) {
            Mat contour = contours.get(i);
            if (isValidContour(dist_8u, contour)) {
                drawContours(charMask, contours, i, Scalar.WHITE, -1, LINE_8, hierarchy, 0, null);
                Rect bounds = boundingRect(contour);
                int boundsMaxX = bounds.x() + bounds.width() - 1;
                int boundsMaxY = bounds.y() + bounds.height() - 1;
                maxX = boundsMaxX > maxX ? boundsMaxX : maxX;
                maxY = boundsMaxY > maxY ? boundsMaxY : maxY;
                minX = bounds.x() < minX ? bounds.x() : minX;
                minY = bounds.y() < minY ? bounds.y() : minY;
                bounds.close();
                validContours++;
            }
            contour.close();
        }
        if (debug()) {
            System.out.println("found contours: " + contours.size());
            System.out.println("valid contours: " + validContours);
        }
        contours.close();
        dist_8u.close();
        hierarchy.close();
        if (validContours == 0) {
            charMask.close();
            return null;// empty part
        }
        Rect cutRect = new Rect(minX, minY, maxX - minX, maxY - minY);
        saveToDisk(charMask, prefix + "_05_contours");

        Mat crop = new Mat(charMask.size(), CV_8UC3, Scalar.BLACK);
        applyBorders(src, borderSize, Scalar.BLACK).copyTo(crop, charMask);
        crop = crop.apply(cutRect);
        charMask.close();
        cutRect.close();
        saveToDisk(crop, prefix + "_06_crop");

        Mat result = new Mat(crop.size(), CV_8UC1);
        cvtColor(crop, result, CV_BGR2GRAY);
        resize(result, result, new Size(result.size().width() * 4, result.size().height() * 4), 0, 0, CV_INTER_CUBIC);
        crop.close();
        saveToDisk(result, prefix + "_07_scale");
        threshold(result, result, threshSrcBwLow, threshSrcBwUp, CV_THRESH_BINARY_INV);
        saveToDisk(result, prefix + "_08_ocr");
        return result;
    }

    private static Mat applyBorders(Mat src, int borderSize, Scalar color) {

        Mat dest = new Mat();
        Mat borderH = new Mat(borderSize, src.cols(), src.type(), color);
        Mat borderV = new Mat(src.rows() + (borderSize * 2), borderSize, src.type(), color);

        MatVector order = new MatVector(3);
        order.put(borderH, src, borderH);
        vconcat(order, dest);

        order = new MatVector(3);
        order.put(borderV, dest, borderV);
        hconcat(order, dest);

        return dest;
    }

    public static File[] extract(File frame) throws IOException {
        Mat colorFilterTextLow = new Mat(new double[] { 0, 2, 0 });
        Mat colorFilterNumberLow = new Mat(new double[] { 255, 255, 255 });
        final double threshText = 0.04d;
        final int threshTextSrcLow = 200;
        final int threshTextSrcUp = 255;

        final double threshNum = 0.06d;
        Mat colorFilterNumLow = new Mat(new double[] { 25, 255, 50 });
        Mat colorFilterNumUp = new Mat(new double[] { 50, 255, 255 });
        final int threshNumSrcLow = 180;
        final int threshNumSrcUp = 255;

        String prefix = StringUtils.substringBeforeLast(frame.getName(), "_");

        File text = process(frame, colorFilterTextLow, colorFilterNumberLow, threshText, threshTextSrcLow,
                threshTextSrcUp, CV_THRESH_BINARY, prefix + "_text");
        File number = process(frame, colorFilterNumLow, colorFilterNumUp, threshNum, threshNumSrcLow, threshNumSrcUp,
                CV_THRESH_BINARY_INV, prefix + "_num");

        colorFilterTextLow.close();
        colorFilterNumberLow.close();
        colorFilterNumLow.close();
        colorFilterNumUp.close();

        return new File[] { text, number };
    }

    public static List<SubImage> extractSubimageBetweenMaskedRows(List<Integer> maskedRows, int imageHeight,
            int minSubImageHeight) {
        List<SubImage> result = new ArrayList<>();
        for (int i = 0; i < maskedRows.size(); i++) {
            int row = maskedRows.get(i);
            boolean isFirstIndex = i == 0;
            boolean isFirstRow = row == 0;
            boolean connectedToLastIndex = !isFirstIndex && row - 1 == maskedRows.get(i - 1);
            if (isFirstRow || connectedToLastIndex) {
                continue;
            }

            int lastValidRow = isFirstIndex ? 0 : maskedRows.get(i - 1) + 1;
            int height = row - lastValidRow;

            if (height >= minSubImageHeight) {
                result.add(new SubImage(lastValidRow, height));
            }
        }

        if (maskedRows.size() == 0) {
            if (imageHeight >= minSubImageHeight) {
                result.add(new SubImage(0, imageHeight));
            }
        } else if (maskedRows.get(maskedRows.size() - 1) == imageHeight) {
            // nothing to do
        } else {
            int lastValidRow = maskedRows.get(maskedRows.size() - 1) + 1;
            int height = imageHeight - lastValidRow;
            if (height >= minSubImageHeight) {
                result.add(new SubImage(lastValidRow, height));
            }
        }
        return result;
    }

    private static boolean isValidContour(Mat src, Mat contour) {
        Rect bounds = boundingRect(contour);
        int width = bounds.width();
        int height = bounds.height();
        boolean wholeScreenX = src.cols() <= width + 8;
        boolean wholeScreenY = src.rows() <= height + 8;
        boolean artifact = width < 15 && height < 15;
        return !(wholeScreenX && wholeScreenY) && !artifact;
    }

}

class SubImage {

    public SubImage(int y, int height) {
        this.y = y;
        this.height = height;
    }

    final int y, height;

}
