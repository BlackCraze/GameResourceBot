package de.blackcraze.grb.ocr;

import static org.bytedeco.javacpp.opencv_core.CV_8U;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_core.NORM_MINMAX;
import static org.bytedeco.javacpp.opencv_core.hconcat;
import static org.bytedeco.javacpp.opencv_core.inRange;
import static org.bytedeco.javacpp.opencv_core.normalize;
import static org.bytedeco.javacpp.opencv_core.vconcat;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.bytedeco.javacpp.opencv_imgproc.CHAIN_APPROX_SIMPLE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2HSV;
import static org.bytedeco.javacpp.opencv_imgproc.CV_DIST_L2;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY_INV;
import static org.bytedeco.javacpp.opencv_imgproc.RETR_CCOMP;
import static org.bytedeco.javacpp.opencv_imgproc.boundingRect;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.distanceTransform;
import static org.bytedeco.javacpp.opencv_imgproc.drawContours;
import static org.bytedeco.javacpp.opencv_imgproc.erode;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;

public class Preprocessor {

    final static Mat MASK_TEXT_LOW = new Mat(new double[] { 0, 0, 170 });
    final static Mat MASK_TEXT_UP = new Mat(new double[] { 0, 255, 255 });

    final static Mat MASK_NUM_LOW = new Mat(new double[] { 25, 255, 50 });
    final static Mat MASK_NUM_UP = new Mat(new double[] { 50, 255, 255 });

    final static double TRESH_TEXT = 0.03d;
    final static int ERODE_TEXT_V = 11;
    final static int ERODE_TEXT_H = 60;

    final static double TRESH_NUM = 0.03d;
    final static int ERODE_NUM_V = 4;
    final static int ERODE_NUM_H = 3;

    final static int TRESH_TEXT_SRC_BW_LOW = 150;
    final static int TRESH_TEXT_SRC_BW_UP = 255;

    final static int TRESH_NUM_SRC_BW_LOW = 80;
    final static int TRESH_NUM_SRC_BW_UP = 255;

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
        double factor = 720d / image.getWidth();
        int i = 0;
        for (SubImage subImage : subImages) {
            BufferedImage cut = image.getSubimage(0, subImage.y, image.getWidth(), subImage.height);
            int scaledHeight = (int) (cut.getHeight() * factor);
            int scaledWidth = (int) (cut.getWidth() * factor);

            Image tmp = cut.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(scaledWidth, scaledHeight, cut.getType());
            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();

            File tempFile;

            tempFile = File.createTempFile("subImage_" + i + "_c1", ".png");
            tempFile.deleteOnExit();
            ImageIO.write(dimg.getSubimage(50, 0, 200, scaledHeight), "png", tempFile);
            result.add(tempFile);

            tempFile = File.createTempFile("subImage_" + i + "_c2", ".png");
            tempFile.deleteOnExit();
            ImageIO.write(dimg.getSubimage(255, 0, 200, scaledHeight), "png", tempFile);
            result.add(tempFile);

            tempFile = File.createTempFile("subImage_" + i + "_c3", ".png");
            tempFile.deleteOnExit();
            ImageIO.write(dimg.getSubimage(465, 0, 200, scaledHeight), "png", tempFile);
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
        return image.getSubimage(0, headerHeight, width, height - headerHeight - footerHeight + 10);
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
     */
    public static void maskIconRing(BufferedImage image) {
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
        boolean matchBlue = 0 <= pixels[0] && pixels[0] <= 50 && //
                0 <= pixels[1] && pixels[1] <= 240 && //
                180 <= pixels[2] && pixels[2] <= 255;
        return matchBlue;
    }

    static final Point getCenterPoint(Mat m) {
        Rect r = boundingRect(m);
        return new Point(r.x() + (r.width() / 2), r.y() + (r.height() / 2));
    }

    private static boolean debug() {
        return true;
    }

    private static void saveToDisk(Mat mat, String name) throws IOException {
        if (debug()) {
            String prefix = "./target/debug/";
            File output = new File(prefix + name + ".png");
            File dir = new File(prefix);
            if (!dir.exists()) {
                dir.mkdirs();
                System.out.println("DEBUG: " + dir.getAbsolutePath());
            }
            imwrite(output.getAbsolutePath(), mat);
        }
    }

    public static void saveToDisk(BufferedImage image, String name) throws IOException {
        if (debug()) {
            String prefix = "./target/debug/";
            File output = new File(prefix + name + ".png");
            File dir = new File(prefix);
            if (!dir.exists()) {
                dir.mkdirs();
                System.out.println("DEBUG: " + dir.getAbsolutePath());
            }
            ImageIO.write(image, "png", output);
        }
    }

    private static List<File> load(InputStream stream) throws IOException {
        BufferedImage image = ImageIO.read(stream);
        image = Preprocessor.cropCenterScreen(image);
        Preprocessor.maskIconRing(image);
        return Preprocessor.cropMasked(image);
    }

    private static File process(File srcFile, Mat maskLow, Mat maskUp, double thresh, int erodeV, int erodeH,
            int threshSrcBwLow, int threshSrcBwUp, boolean cropContour, String prefix) throws IOException {
        Mat dest = new Mat();
        Mat src = imread(srcFile.getAbsolutePath());
        cvtColor(src, dest, CV_BGR2HSV);
        inRange(dest, maskLow, maskUp, dest);
        threshold(dest, dest, 200, 255, CV_THRESH_BINARY_INV);
        distanceTransform(dest, dest, CV_DIST_L2, 3);
        // Normalize the distance image for range = {0.0, 1.0}
        // so we can visualize and threshold it
        normalize(dest.clone(), dest, 0, 1., NORM_MINMAX, -1, null);
        threshold(dest, dest, thresh, 1, CV_THRESH_BINARY);

        // Mat kernel1 = Mat.ones(erodeV, erodeH, CV_8UC1).asMat();
        // erode(dest, dest, kernel1);

        Mat dist_8u = new Mat();
        dest.convertTo(dist_8u, CV_8U);
        // Find total markers
        MatVector contours = new MatVector();
        Mat hierarchy = new Mat();
        int borderSize = 1;
        dist_8u = applyBorders(dist_8u, borderSize);
        findContours(dist_8u, contours, hierarchy, RETR_CCOMP, CHAIN_APPROX_SIMPLE);

        Mat mask = new Mat(dist_8u.size(), CV_8UC3, Scalar.BLACK);
        for (int i = 0; i < contours.size(); i++) {
            if (isValidContour(src, contours.get(i), borderSize)) {
                drawContours(mask, contours, i, Scalar.WHITE, -1, LINE_8, hierarchy, 0, null);
            }
        }
        saveToDisk(mask, prefix + "_01_mask");

        Mat crop = new Mat(dist_8u.size(), CV_8UC3, Scalar.WHITE);
        Mat cut = applyBorders(src, borderSize);
        cut.copyTo(crop, mask);
        saveToDisk(crop, prefix + "_02_crop");

        resize(crop, crop, new Size(crop.size().width() * 2, crop.size().height() * 2));
        cvtColor(crop, crop, CV_RGB2GRAY);
        threshold(crop, crop, threshSrcBwLow, threshSrcBwUp, CV_THRESH_BINARY_INV);

        File result = File.createTempFile(prefix, ".png");
        result.deleteOnExit();
        imwrite(result.getAbsolutePath(), crop);

        saveToDisk(crop, prefix + "_03_ocr");
        return result;
    }

    private static Mat applyBorders(Mat src, int borderSize) {

        Mat dest = new Mat();
        Mat borderH = new Mat(borderSize, src.cols(), src.type(), Scalar.WHITE);
        Mat borderV = new Mat(src.rows() + (borderSize * 2), borderSize, src.type(), Scalar.WHITE);

        MatVector order = new MatVector(3);
        order.put(borderH, src, borderH);
        vconcat(order, dest);

        order = new MatVector(3);
        order.put(borderV, dest, borderV);
        hconcat(order, dest);

        return dest;
    }

    public static Map<File, File> extract(InputStream imageStream) throws IOException {
        List<File> parts = load(imageStream);
        Map<File, File> result = new HashMap<>();
        int i = 0;
        for (File part : parts) {
            File text = process(part, MASK_TEXT_LOW, MASK_TEXT_UP, TRESH_TEXT, ERODE_TEXT_V, ERODE_TEXT_H,
                    TRESH_TEXT_SRC_BW_LOW, TRESH_TEXT_SRC_BW_UP, false, i + "_text");
            File number = process(part, MASK_NUM_LOW, MASK_NUM_UP, TRESH_NUM, ERODE_NUM_V, ERODE_NUM_H,
                    TRESH_NUM_SRC_BW_LOW, TRESH_NUM_SRC_BW_UP, true, i + "_num");
            result.put(text, number);
            i++;
            part.delete();
        }
        return result;
    }

    private static void deleteTempFiles(File part, List<File> texts, List<File> numbers) {
        part.delete();
        for (File file : numbers) {
            file.delete();
        }
        for (File file : texts) {
            file.delete();
        }
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

        //FIXME
        if (maskedRows.size() == 0) {
            result.add(new SubImage(0, imageHeight));
        } else if (maskedRows.get(maskedRows.size() - 1) == imageHeight) {
            // nothing to do
        } else {
            int lastValidRow = maskedRows.get(maskedRows.size() - 1) + 1;
            result.add(new SubImage(lastValidRow, imageHeight - lastValidRow));
        }
        return result;
    }

    private static boolean isValidContour(Mat src, Mat contour, int borderSize) {
        Rect bounds = boundingRect(contour);
        int width = bounds.width();
        int height = bounds.height();
        boolean wholeScreenX = src.cols() + (2 * borderSize) == width;
        boolean wholeScreenY = src.rows() + (2 * borderSize) == height;
        return !wholeScreenX || !wholeScreenY;
    }

}

class SubImage {

    public SubImage(int y, int height) {
        this.y = y;
        this.height = height;
    }

    final int y, height;

}
