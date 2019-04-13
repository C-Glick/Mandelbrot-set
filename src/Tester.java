import java.math.BigDecimal;

import javax.swing.SwingWorker;

public class Tester{
	double threshold=Launcher.threshold;
	int limit=Launcher.limit;		//number of iterations to run before stopping
	
	
	/**
	 * Test for just one complex number, returns the number of iterations it took to reach the threshold. Will be 0 if 
	 * iteration limit is reached, and thus, is in the Mandelbrot set. 
	 * @param c The complex number to test.
	 * @return A long, telling the number of iterations taken to reach the threshold.
	 */
	public long test1(Complex c) {		
											
		Complex z = new Complex(0,0);
		long result=0;
		
		for (int i=1; i<=limit;	i++) {
			z.sqr();
			z.add(c);
			double value = z.abs();
			if (value>=threshold) {
				result = i;
				break;
			}
		}
		return result;
	}
	
	/**
	 * Takes in a range of complex numbers and returns a 2D array
	 * of the number of iterations each took to reach the threshold
	 * @param topLeft	The top left complex number
	 * @param bottomRight	The bottom right complex number
	 * @param width			The pixel width of the window
	 * @param height		The pixel height of the window
	 * @return	A 2D array of longs showing the number of iterations taken.
	 * @see test1
	 */
	public long[][] test2(Complex topLeft, Complex bottomRight,int width,int height){
		long[][] result = new long[width][height];
		
		limit = Launcher.limit;
		
		//finds the complex numbers based on the pixel numbers
		//then tests those numbers 
		for(int x=0; x<width; x++) {
			double real= ((bottomRight.getReal()-topLeft.getReal())/width)*x+topLeft.getReal();
			
			for(int y=0;y<height;y++) {
				double imag= -(((bottomRight.getImag()-topLeft.getImag())/height)*y+topLeft.getImag());
				result[x][y] = test1(new Complex(real,imag));
			}
			
		}
		return result;
	}
	
	//tests 3 and 4 are for high precision calculations
	
	public long test3(ComplexLong c) {
		BigDecimal thresholdHP = new BigDecimal(Double.toString(threshold));
		
		ComplexLong z = new ComplexLong(new BigDecimal(0),new BigDecimal(0));
		long result= 0;
		
		for(long i=1; i<=limit; i++) {
			z.sqr();
			z.add(c);
			BigDecimal value = z.abs();
			if(value.compareTo(thresholdHP)>=0) {
				result= i;
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * The same as Test2 but for High Precision.
	 * Takes in a range of complex numbers and returns a 2D array
	 * of the number of iterations each took to reach the threshold
	 * @param topLeft	The top left complex number
	 * @param bottomRight	The bottom right complex number
	 * @param width			The pixel width of the window
	 * @param height		The pixel height of the window
	 * @return	A 2D array of longs showing the number of iterations taken.
	 * @see test2
	 */
	public long[][] test4(ComplexLong topLeftHP, ComplexLong bottomRightHP, int width, int height){
		long[][] result = new long[width][height];
		BigDecimal widthHP = new BigDecimal(width);
		BigDecimal heightHP = new BigDecimal(height);
		
		limit = Launcher.limit;
		
		//finds the complex numbers based on the pixel numbers
		//then tests those numbers 
		for(int x=0; x<width; x++) {
			BigDecimal real = ((bottomRightHP.getReal().subtract(topLeftHP.getReal())).divide(widthHP))
								.multiply(topLeftHP.getReal().add(new BigDecimal(x)));
			
			for(int y=0; y<height; y++) {
				BigDecimal imag= (((bottomRightHP.getImag().subtract(topLeftHP.getImag())).divide(heightHP))
								.multiply(topLeftHP.getImag().add(new BigDecimal(y)))).negate();
				result[x][y] = test3(new ComplexLong(real,imag));
			}
		}
		return result;
	}
	

}
