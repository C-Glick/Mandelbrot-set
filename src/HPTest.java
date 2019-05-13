import java.math.BigDecimal;
import java.util.concurrent.RecursiveAction;

/**
 * The same as BasicTest but for High Precision.
 * Tests if a single ComplexLong number is in the Mandelbrot set.
 * If not returns the number of iterations taken to exceed the threshold.
 * @see BasicTest
 */
public class HPTest extends RecursiveAction{
	
	ComplexLong c;
	int limit;
	BigDecimal thresholdHP;
	int x;
	int y;
	
	/**
	 * The same as BasicTest but for High Precision.
	 * Tests if a single ComplexLong number is in the Mandelbrot set.
	 * If not returns the number of iterations taken to exceed the threshold.
	 * @see BasicTest
	 * @param c The ComplexLong to test.
	 * @param thresholdHP Threshold for the test, stops once the value grows higher than this (default is 2)
	 * @param x The pixel x value
	 * @param y The pixel y value
	 */
	HPTest(ComplexLong c, BigDecimal thresholdHP, int x, int y){
		this.c = c;
		this.limit = Launcher.limit;
		this.thresholdHP = thresholdHP;
		this.x = x;
		this.y = y;
	}

	
	protected void compute() {
		ComplexLong z = new ComplexLong(BigDecimal.ZERO,BigDecimal.ZERO);
		int result= 0;
		
		for(int i=1; i<=limit; i++) {
			z.sqr();
			z.add(c);
			BigDecimal value = z.abs();
			if(value.compareTo(thresholdHP)>=0) {
				result = i;
				break;
			}
		}
		Launcher.resultsArray[x][y] = result;
	}
}
