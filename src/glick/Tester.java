package glick;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.ForkJoinPool;

import glick.lib.BigComplex;
import glick.lib.Complex;
import glick.lib.DoubleDoubleComplex;
import resources.DoubleDouble;

public class Tester {
	double threshold = Launcher.threshold;
	BigDecimal thresholdHP = BigDecimal.valueOf(Launcher.threshold); // threshold doesn't really need infinite precision
																		// but a BigDecimal object is needed for
																		// calculations
	int limit = Launcher.limit; // number of iterations to run before stopping
	MathContext rMode = BigComplex.getRMode();
	ForkJoinPool commonPool = ForkJoinPool.commonPool();
	Long startTime;

	/**
	 * Test for just one complex number, returns the number of iterations it took to
	 * reach the threshold. Will be 0 if iteration limit is reached, and thus, is in
	 * the Mandelbrot set.
	 * 
	 * @param c The complex number to test.
	 * @return A long, telling the number of iterations taken to reach the
	 *         threshold.
	 * @deprecated use BasicTest class instead
	 */
	public int test1(Complex c) {
		Complex z = new Complex(0, 0);
		int result = 0;

		for (int i = 1; i <= limit; i++) {
			z.sqr();
			z.add(c);
			double value = z.abs();
			if (value >= threshold) {
				result = i;
				break;
			}
		}
		return result;
	}

	/**
	 * Takes in a range of complex numbers and returns a 2D array of the number of
	 * iterations each took to reach the threshold
	 * 
	 * @param topLeft     The top left complex number
	 * @param bottomRight The bottom right complex number
	 * @param width       The pixel width of the window
	 * @param height      The pixel height of the window
	 * @return A 2D array of longs showing the number of iterations taken.
	 * @see test1
	 */
	public void test2(Complex topLeft, Complex bottomRight, int width, int height) {
		limit = Launcher.limit; // update the limit to be the same as it is in Launcher

		if (!Launcher.firstBoot) { // dont call progress bars on the first boot
			Launcher.display.waitCursor(true); // set cursor to the wait cursor to show processing
			// set progress bar data
			Launcher.display.progressBar.setMinimum(0);
			Launcher.display.progressBar.setMaximum(width * height);
			Launcher.display.progressBar.setValue(0);
		}

		// finds the complex numbers based on the pixel numbers
		// then tests those numbers

		double realFactor = ((topLeft.getReal() - bottomRight.getReal()) / (double) width);
		double imagFactor = ((topLeft.getImag() - bottomRight.getImag()) / (double) height);

		for (int x = 0; x < width; x++) {
			double real = topLeft.getReal() - realFactor * x;

			for (int y = 0; y < height; y++) {
				double imag = topLeft.getImag() - imagFactor * y;

				// bulb checking
				double q = Math.pow((real - 0.25), 2) + Math.pow(imag, 2);
				if (q * (q + (real - 0.25)) <= 0.25 * Math.pow(imag, 2)) {
					// if true number is in bulb so it has to be in the set (black)
					Launcher.resultsArray[x][y] = 0;
				} else {
					BasicTest task = new BasicTest(new Complex(real, imag), threshold, x, y); // create BasicTest object

					commonPool.submit(task); // submits BasicTest to pool to be executed later
				}
			}
		}
		while (!commonPool.isQuiescent()) { // loop until all test have finished executing
			if (!Launcher.firstBoot && !Launcher.isVideoExporting) {
				Launcher.display.progressBar.setValue(width * height - commonPool.getQueuedSubmissionCount());
			} // update progress bar
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (!Launcher.firstBoot && !Launcher.isVideoExporting) {
			Launcher.display.progressBar.setValue(width * height); // set the progress bar to 100%
			Launcher.display.waitCursor(false); // set to normal cursor
		}

	}

	// tests 3 and 4 are for infinite precision calculations

	/**
	 * The same as test1 but for infinite Precision. Tests if a single ComplexLong
	 * number is in the Mandelbrot set. If not returns the number of iterations
	 * taken to exceed the threshold.
	 * 
	 * @param c The ComplexLong to test.
	 * @return A long telling the number of iterations taken to exceed the set
	 *         threshold returns 0 if the number is in the set (does not exceed the
	 *         threshold within the set limit).
	 * @see test1
	 * @deprecated use HPTest class instead
	 */
	public int test3(BigComplex c) {
		BigComplex z = new BigComplex(BigDecimal.ZERO, BigDecimal.ZERO);
		int result = 0;

		for (int i = 1; i <= limit; i++) {
			z.sqr();
			z.add(c);
			BigDecimal value = z.abs();
			if (value.compareTo(thresholdHP) >= 0) {
				result = i;
				break;
			}
		}

		return result;
	}

	/**
	 * The same as Test2 but for infinite Precision. Takes in a range of complex
	 * numbers and returns a 2D array of the number of iterations each took to reach
	 * the threshold
	 * 
	 * @param topLeft     The top left complex number
	 * @param bottomRight The bottom right complex number
	 * @param width       The pixel width of the window
	 * @param height      The pixel height of the window
	 * @return A 2D array of longs showing the number of iterations taken.
	 * @see test2
	 */
	public void test4(BigComplex topLeftIP, BigComplex bottomRightIP, BigDecimal widthIP, BigDecimal heightIP) {
		startTime = System.currentTimeMillis();
		rMode = BigComplex.getRMode();

		int width = widthIP.intValue();
		int height = heightIP.intValue();
		limit = Launcher.limit;

		if (!Launcher.firstBoot) {
			Launcher.display.waitCursor(true); // set cursor to the wait cursor to show processing
			// set progress bar data
			Launcher.display.progressBar.setMinimum(0);
			Launcher.display.progressBar.setMaximum(width * height);
			Launcher.display.progressBar.setValue(0);

		}

		// finds the complex numbers based on the pixel numbers
		// then tests those numbers
		BigDecimal fourth = new BigDecimal("0.25");
		for (int x = 0; x < width; x++) {
			BigDecimal real = ((bottomRightIP.getReal().subtract(topLeftIP.getReal(), rMode)).divide(widthIP, rMode))
					.multiply(BigDecimal.valueOf(x), rMode).add(topLeftIP.getReal(), rMode);

			for (int y = 0; y < height; y++) {
				BigDecimal imag = ((bottomRightIP.getImag().subtract(topLeftIP.getImag(), rMode)).divide(heightIP,
						rMode)).multiply(BigDecimal.valueOf(y), rMode).add(topLeftIP.getImag(), rMode);

				// bulb checking
				BigDecimal q = (real.subtract(fourth)).pow(2).add(imag.pow(2));
				if (q.multiply(q.add(real.subtract(fourth))).compareTo(fourth.multiply(imag.pow(2))) <= 0) {
					// if true number is in bulb so it has to be in the set (black)
					Launcher.resultsArray[x][y] = 0;
				} else {
					IPTest task = new IPTest(new BigComplex(real, imag), thresholdHP, x, y);
					commonPool.submit(task);
				}
			}
		}

		while (!commonPool.isQuiescent()) {
			Long currentTime = System.currentTimeMillis();
			Long timeTaken = currentTime - startTime;
			if (!Launcher.firstBoot) {
				Launcher.display.progressBar.setValue(width * height - commonPool.getQueuedSubmissionCount());
			} // update progress bar

			// set time estimate (time taken / percent done) - time taken
			double timeEstimate = (((double) timeTaken / Launcher.display.progressBar.getPercentComplete() - timeTaken)
					/ 1000);
			double A = ((timeEstimate % 86400) % 3600);
			int min = (int) A / 60;
			int sec = (int) A % 60;
			Launcher.display.timeEstimateDisplay.setText(String.format("%02d", min) + ":" + String.format("%02d", sec));
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (!Launcher.firstBoot) {
			Launcher.display.progressBar.setValue(width * height); // set the progress bar to 100%
			Launcher.display.waitCursor(false); // set cursor to the hand cursor
		}
	}

	public void test5(DoubleDoubleComplex topLeftHP, DoubleDoubleComplex bottomRightHP, DoubleDouble widthHP,
			DoubleDouble heightHP) {
		startTime = System.currentTimeMillis();

		limit = Launcher.limit;
		int width = widthHP.intValue();
		int height = heightHP.intValue();
		DoubleDouble thresholdHP = new DoubleDouble(threshold);

		if (!Launcher.firstBoot) {
			Launcher.display.waitCursor(true); // set cursor to the wait cursor to show processing
			// set progress bar data
			Launcher.display.progressBar.setMinimum(0);
			Launcher.display.progressBar.setMaximum(width * height);
			Launcher.display.progressBar.setValue(0);
		}

		DoubleDouble realFactor = topLeftHP.getReal().subtract(bottomRightHP.getReal()).divide(widthHP);
		DoubleDouble imagFactor = topLeftHP.getImag().subtract(bottomRightHP.getImag()).divide(heightHP);

		for (int x = 0; x < width; x++) {
			DoubleDouble realHP = topLeftHP.getReal().subtract(realFactor.multiply(new DoubleDouble(x)));

			for (int y = 0; y < height; y++) {
				DoubleDouble imagHP = topLeftHP.getImag().subtract(imagFactor.multiply(new DoubleDouble(y)));

				// bulb checking
				// double q = Math.pow((real-0.25),2)+Math.pow(imag, 2);
				// if(q*(q+(real-0.25)) <= 0.25*Math.pow(imag, 2)) {
				// if true number is in bulb so it has to be in the set (black)
				// Launcher.resultsArray[x][y] = 0;
				// }else {
				HPTest task = new HPTest(new DoubleDoubleComplex(realHP, imagHP), thresholdHP, x, y); // create HPTest
																										// object

				commonPool.submit(task); // submits HPTest to pool to be executed later
				// }
			}
		}
		while (!commonPool.isQuiescent()) {
			Long currentTime = System.currentTimeMillis();
			Long timeTaken = currentTime - startTime;
			if (!Launcher.firstBoot) {
				Launcher.display.progressBar.setValue(width * height - commonPool.getQueuedSubmissionCount());
			} // update progress bar

			// set time estimate (time taken / percent done) - time taken
			double timeEstimate = (((double) timeTaken / Launcher.display.progressBar.getPercentComplete() - timeTaken)
					/ 1000);
			double A = ((timeEstimate % 86400) % 3600);
			int min = (int) A / 60;
			int sec = (int) A % 60;
			Launcher.display.timeEstimateDisplay.setText(String.format("%02d", min) + ":" + String.format("%02d", sec));
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (!Launcher.firstBoot) {
			Launcher.display.progressBar.setValue(width * height); // set the progress bar to 100%
			Launcher.display.waitCursor(false); // set cursor to the hand cursor
		}
	}
}
