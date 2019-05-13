import java.util.concurrent.RecursiveAction;

/**
 * Test for just one complex number, sets the resultsArray in the Launcher class to
 * the number of iterations it took to reach the threshold. Will be 0 if 
 * iteration limit is reached, and thus, is in the Mandelbrot set. 
 * @author The Pheonix
 *
 */
public class BasicTest extends RecursiveAction{
	
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
	 * @param limit The loop limit of the test
	 * @param threshold Threshold for the test, stops once the value grows higher than this (default is 2)
	 * @param x	The pixel x value
	 * @param y The pixel y value
	 */
	BasicTest (Complex c, int limit, double threshold, int x, int y){
		this.limit = limit;
		this.threshold = threshold;
		this.c = c;
		this.x = x;
		this.y = y;
	}

	@Override
	protected void compute() {
		Complex z = new Complex(0,0);
		int result=0;
		
		for (int i=1; i<=limit;	i++) {
			z.sqr();
			z.add(c);
			double value = z.abs();
			if (value>=threshold) {
				result = i;
				break;
			}
		}
		Launcher.resultsArray[x][y] = result;
		
	}

}
