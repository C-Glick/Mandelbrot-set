import java.awt.Color;
import java.util.concurrent.RecursiveAction;


/**
 * Test for just one complex number, sets the resultsArray in the Launcher class to
 * the number of iterations it took to reach the threshold. Will be 0 if 
 * iteration limit is reached, and thus, is in the Mandelbrot set.  *
 */
public class BasicTest extends RecursiveAction{
	static final double LOG_THRESHOLD = Math.log(Launcher.threshold);
	
	Complex c;		
	int limit;		
	double threshold;		
	int x;		
	int y;		
	
	/**
	 *  Test for just one complex number, sets the resultsArray in the Launcher class to
	 *  the number of iterations it took to reach the threshold. Will be 0 if 
	 *  iteration limit is reached, and thus, is in the Mandelbrot set. 
	 * @param c The complex number to test
	 * @param threshold Threshold for the test, stops once the value grows higher than this (default is 2)
	 * @param x	The pixel x value
	 * @param y The pixel y value
	 */
	BasicTest (Complex c, double threshold, int x, int y){
		this.limit = Launcher.limit;
		this.threshold = threshold;
		this.c = c;
		this.x = x;
		this.y = y;
	}

	protected void compute() {
		Complex z = new Complex(0,0);
		double result=0;
		int smoothingCount=0;
		
		for (int i=1; i<=limit;	i++) {
			z.sqr();
			z.add(c);
			double value = z.abs();
			if (value>=threshold) {
				if(smoothingCount>=2) {					//iterate 3 more times to reduce the error when calculating the smoothing algorithm
					result = smoothShading(i,value);			//figure out what the required number of iterations should be (ex 3.52233)
					break;
				}
				smoothingCount++;
			}
		}
		Launcher.resultsArray[x][y] = result;
	}
	
	/**
	 * Calculates the number of iterations that a certain number should take to escape.
	 * Rather than 4 iterations, the smoothing formula may return 4.642, giving
	 * a more accurate result, therefore smoothing the graph out.
	 * Still requires the number of iterations taken to escape.
	 * @param i The number of iterations before Z >= threshold.
	 * @param zMag The absolute value (or magnitude) of z
	 * @return A double, the number of iterations it should for z take to escape the threshold
	 */
	private double smoothShading(int i, double zMag) {
		double mu = i + 1 - Math.log(Math.log(zMag)) / LOG_THRESHOLD;
		return mu;
	}
}
