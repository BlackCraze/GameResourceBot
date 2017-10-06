package de.blackcraze.grb.ocr;

import static org.bytedeco.javacpp.opencv_core.CV_8U;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.LINE_4;
import static org.bytedeco.javacpp.opencv_core.NORM_MINMAX;
import static org.bytedeco.javacpp.opencv_core.hconcat;
import static org.bytedeco.javacpp.opencv_core.inRange;
import static org.bytedeco.javacpp.opencv_core.normalize;
import static org.bytedeco.javacpp.opencv_core.vconcat;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.bytedeco.javacpp.opencv_imgproc.CHAIN_APPROX_SIMPLE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2HSV;
import static org.bytedeco.javacpp.opencv_imgproc.CV_DIST_L2;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY_INV;
import static org.bytedeco.javacpp.opencv_imgproc.RETR_CCOMP;
import static org.bytedeco.javacpp.opencv_imgproc.boundingRect;
import static org.bytedeco.javacpp.opencv_imgproc.circle;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.distanceTransform;
import static org.bytedeco.javacpp.opencv_imgproc.drawContours;
import static org.bytedeco.javacpp.opencv_imgproc.erode;
import static org.bytedeco.javacpp.opencv_imgproc.findContours;
import static org.bytedeco.javacpp.opencv_imgproc.resize;
import static org.bytedeco.javacpp.opencv_imgproc.threshold;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacv.Java2DFrameUtils;

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

	public static BufferedImage cropMasked(BufferedImage image) {
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
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight() - rowsToRemove.size(),
				image.getType());
		for (int yy = 0, resultRow = 0; yy < image.getHeight(); yy++) {
			if (!rowsToRemove.contains(yy)) {
				int[] row = raster.getPixels(0, yy, image.getWidth(), 1, (int[]) null);
				result.getRaster().setPixels(0, resultRow, image.getWidth(), 1, row);
				resultRow++;
			}
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
		return false;
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

	private static MatVector load(InputStream stream) throws IOException {
		BufferedImage image = ImageIO.read(stream);
		image = Preprocessor.cropCenterScreen(image);
		Preprocessor.maskIconRing(image);
		image = Preprocessor.cropMasked(image);
		Mat src = Java2DFrameUtils.toMat(image);
		// Mat src = null; //FIXME
		double factor = 720d / image.getWidth();
		resize(src, src, new Size(720, Double.valueOf(image.getHeight() * factor).intValue()));
		Mat col1 = src.apply(new Rect(50, 0, 200, src.rows()));
		Mat col2 = src.apply(new Rect(255, 0, 200, src.rows()));
		Mat col3 = src.apply(new Rect(465, 0, 200, src.rows()));
		MatVector matVector = new MatVector(3);
		matVector.put(0, col1);
		matVector.put(1, col2);
		matVector.put(2, col3);
		return matVector;
	}

	private static List<Mat> process(Mat src, Mat maskLow, Mat maskUp, double thresh, int erodeV, int erodeH,
			int threshSrcBwLow, int threshSrcBwUp, boolean cropContour, String prefix) throws IOException {
		Mat dest = new Mat();
		cvtColor(src, dest, CV_BGR2HSV);
		inRange(dest, maskLow, maskUp, dest);
		threshold(dest, dest, 200, 255, CV_THRESH_BINARY_INV);
		distanceTransform(dest, dest, CV_DIST_L2, 3);
		// Normalize the distance image for range = {0.0, 1.0}
		// so we can visualize and threshold it
		normalize(dest, dest, 0, 1., NORM_MINMAX, -1, null);
		threshold(dest, dest, thresh, 1, CV_THRESH_BINARY);

		Mat kernel1 = Mat.ones(erodeV, erodeH, CV_8UC1).asMat();
		erode(dest, dest, kernel1);

		Mat dist_8u = new Mat();
		dest.convertTo(dist_8u, CV_8U);
		// Find total markers
		MatVector contours = new MatVector();
		Mat hierarchy = new Mat();
		int borderSize = 1;
		dist_8u = applyBorders(dist_8u, borderSize);
		findContours(dist_8u, contours, hierarchy, RETR_CCOMP, CHAIN_APPROX_SIMPLE);

		Mat mat = applyBorders(src.clone(), 1);
		List<Mat> result = new ArrayList<>((int) contours.size());

		// Draw the foreground markers
		if (debug()) {
			for (int i = 0; i < contours.size(); i++) {
				Mat contour = contours.get(i);
				if (isValidContour(src, contour, borderSize)) {
					drawContours(mat, contours, i, Scalar.CYAN, 2, LINE_4, hierarchy, 0, null);
					circle(mat, getCenterPoint(contour), 2, Scalar.RED, 2, LINE_4, 0);
				}
			}
			saveToDisk(mat, prefix + "_4_contours");
			mat = applyBorders(src.clone(), 1); // reset mat src
		}

		for (int i = 0; i < contours.size(); i++) {
			Mat contour = contours.get(i);
			if (isValidContour(src, contour, borderSize)) {

				// remove some white buffer from erode
				Rect rect = boundingRect(contour);
				if (cropContour) {
					rect.x(rect.x() + (erodeH / 2));
					rect.width(rect.width() - erodeH);
					rect.y(rect.y() + (erodeV / 2));
					rect.height(rect.height() - erodeV);
				}

				Mat cut = mat.apply(rect);
				resize(cut, cut, new Size(cut.size().width() * 2, cut.size().height() * 2));
				cvtColor(cut, cut, CV_RGB2GRAY);
				threshold(cut, cut, threshSrcBwLow, threshSrcBwUp, CV_THRESH_BINARY_INV);
				result.add(cut);
				saveToDisk(cut, prefix + "_5_cut_" + i);
			}
		}

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

	public static List<Mat> extract(InputStream imageStream) throws IOException {
		List<Mat> result = new ArrayList<>(24); // should not be more I think
		MatVector load = load(imageStream);
		for (int i = 0; i < 3; i++) {
			List<Mat> texts = process(load.get(i), MASK_TEXT_LOW, MASK_TEXT_UP, TRESH_TEXT, ERODE_TEXT_V, ERODE_TEXT_H,
					TRESH_TEXT_SRC_BW_LOW, TRESH_TEXT_SRC_BW_UP, false, "col_" + i + "_text");
			List<Mat> numbers = process(load.get(i), MASK_NUM_LOW, MASK_NUM_UP, TRESH_NUM, ERODE_NUM_V, ERODE_NUM_H,
					TRESH_NUM_SRC_BW_LOW, TRESH_NUM_SRC_BW_UP, true, "col_" + i + "_num");
			if (texts.size() != numbers.size()) {
				throw new IllegalStateException("could not extract headers and columns exactly");
			}
			for (int j = 0; j < texts.size(); j++) {
				result.add(texts.get(j));
				result.add(numbers.get(j));
			}
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
