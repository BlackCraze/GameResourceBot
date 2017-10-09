package de.blackcraze.grb.ocr;

import org.bytedeco.javacpp.Pointer;
import org.junit.Assert;

/**
 * 
 * @author blackcraze
 *
 */
public class PreprocessMemoryTest {

	public static void main(String[] args) {
		int i;
		for (i = 0; i < 100000; i++) {
			try {
				Preprocessor.extract(PreprocessMemoryTest.class.getClassLoader().getResourceAsStream("1080_de_01.png"));
				if (i % 100 == 0 || i == 0) {
					long maxPhysicalBytes = Pointer.maxPhysicalBytes();
					long physicalBytes = Pointer.physicalBytes();
					Runtime runtime = Runtime.getRuntime();
					long usedBytes = runtime.totalMemory() - runtime.freeMemory();
					System.out.printf("%d %d %d %d\n", i, maxPhysicalBytes, physicalBytes, usedBytes);

				}
			} catch (Throwable e) {
				Assert.fail("On Try: " + i);
			}
		}
		Assert.assertTrue(true);
	}

}
