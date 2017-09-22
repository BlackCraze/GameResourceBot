package org.bytedeco.javacv;

import java.awt.image.BufferedImage;

import org.bytedeco.javacpp.opencv_core.Mat;

/**
 * Convenience class for performing various conversions between Mat, IplImage,
 * BufferedImage and Frame objects. Methods are synchronized because the
 * underlying JavaCV converters aren't safe for concurrent access.
 *
 * All created Frame, Mat, IplImages and BufferedImages are cloned internally
 * after creation so that their memory locations remain valid after the
 * converters which created them are garbage collected. This is safer for the
 * called, but may be slower.
 *
 * If performance is critical, use the *FrameConverter classes directly, after
 * reading about the image validity constraints (eg, images data is only valid
 * until next call to the converter).
 *
 * @see <a href="https://groups.google.com/forum/#!topic/javacv/sSgY9e-IDRA">
 *      Java2DFrameConverter crashes JVM</a>
 * @see FrameConverter
 *
 * @author Sam West, Joel Wong, Sep 2016.
 *
 */
public class Java2DFrameUtils {

	private static OpenCVFrameConverter.ToMat matConv = new OpenCVFrameConverter.ToMat();
	private static Java2DFrameConverter biConv = new Java2DFrameConverter();

	/**
	 * Clones (deep copies the data) of a {@link BufferedImage}. Necessary when
	 * converting to BufferedImages from JavaCV types to avoid re-using the same
	 * memory locations.
	 *
	 * @param source
	 * @return
	 */
	public static BufferedImage deepCopy(BufferedImage source) {
		return Java2DFrameConverter.cloneBufferedImage(source);
	}

	public synchronized static BufferedImage toBufferedImage(Mat src) {
		return deepCopy(biConv.getBufferedImage(matConv.convert(src).clone()));
	}
	
	public synchronized static Mat toMat(BufferedImage src){
        return matConv.convertToMat(biConv.convert(src)).clone();
    }
}