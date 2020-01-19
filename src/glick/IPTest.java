package glick;

import java.math.BigDecimal;
import java.util.concurrent.RecursiveAction;

import glick.lib.BigComplex;

/**
 * The same as BasicTest but for infinite Precision. Tests if a single
 * BigComplex number is in the Mandelbrot set. If not returns the number of
 * iterations taken to exceed the threshold.
 * 
 * @see BasicTest
 */
public class IPTest extends RecursiveAction {
	private static final long serialVersionUID = -7018745068832254807L;

	static final double LOG_THRESHOLD = Math.log(Launcher.threshold);

	BigComplex c;
	int limit;
	BigDecimal thresholdHP;
	int x;
	int y;

	/**
	 * The same as BasicTest but for infinite Precision. Tests if a single
	 * BigComplex number is in the Mandelbrot set. If not returns the number of
	 * iterations taken to exceed the threshold.
	 * 
	 * @see BasicTest
	 * @param c           The BigComplex to test.
	 * @param thresholdHP Threshold for the test, stops once the value grows higher
	 *                    than this (default is 2)
	 * @param x           The pixel x value
	 * @param y           The pixel y value
	 */
	IPTest(BigComplex c, BigDecimal thresholdHP, int x, int y) {
		this.c = c;
		this.limit = Launcher.limit;
		this.thresholdHP = thresholdHP;
		this.x = x;
		this.y = y;
	}

	protected void compute() {
		BigComplex z = new BigComplex(BigDecimal.ZERO, BigDecimal.ZERO);
		double result = 0;
		int smoothingCount = 0;

		for (int i = 1; i <= limit; i++) {
			z.sqr();
			z.add(c);
			BigDecimal value = z.abs();
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
