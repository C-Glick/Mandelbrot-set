package glick;

import java.util.concurrent.RecursiveAction;

import glick.lib.DoubleDoubleComplex;
import resources.DoubleDouble;

/**
 * The same as BasicTest but for high Precision. Tests if a single
 * DoubleDoubleComplex number is in the Mandelbrot set. If not returns the
 * number of iterations taken to exceed the threshold.
 * 
 * @see BasicTest
 */
public class HPTest extends RecursiveAction {
	private static final long serialVersionUID = 7179863406564669278L;

	static final double LOG_THRESHOLD = Math.log(Launcher.threshold);

	DoubleDoubleComplex c;
	int limit;
	DoubleDouble thresholdHP;
	int x;
	int y;

	/**
	 * The same as BasicTest but for high Precision. Tests if a single
	 * DoubleDoubleComplex number is in the Mandelbrot set. If not returns the
	 * number of iterations taken to exceed the threshold.
	 * 
	 * @see BasicTest
	 * @param c           The DoubleDoubleComplex to test.
	 * @param thresholdHP Threshold for the test, stops once the value grows higher
	 *                    than this (default is 2)
	 * @param x           The pixel x value
	 * @param y           The pixel y value
	 */
	HPTest(DoubleDoubleComplex c, DoubleDouble thresholdHP, int x, int y) {
		this.c = c;
		this.limit = Launcher.limit;
		this.thresholdHP = thresholdHP;
		this.x = x;
		this.y = y;
	}

	protected void compute() {
		DoubleDoubleComplex z = new DoubleDoubleComplex(DoubleDouble.valueOf(0), DoubleDouble.valueOf(0));
		double result = 0;
		int smoothingCount = 0;

		for (int i = 1; i <= limit; i++) {
			z.sqr();
			z.add(c);
			DoubleDouble value = z.abs();
			if (value.compareTo(thresholdHP) >= 0) {
				if (smoothingCount >= 2) {
					result = smoothShading(i, value.doubleValue());
					break;
				}
				smoothingCount++;
			}
		}
		Launcher.resultsArray[x][y] = result;
	}

	/**
	 * Calculates the number of iterations that a certain number should take to
	 * escape. Rather than 4 iterations, the smoothing formula may return 4.642,
	 * giving a more accurate result, therefore smoothing the graph out. Still
	 * requires the number of iterations taken to escape.
	 * 
	 * @param i    The number of iterations before Z >= threshold.
	 * @param zMag The absolute value (or magnitude) of z
	 * @return A double, the number of iterations it should for z take to escape the
	 *         threshold
	 */
	private double smoothShading(int i, double zMag) {
		double mu = i + 1 - Math.log(Math.log(zMag)) / LOG_THRESHOLD;
		return mu;
	}
}
