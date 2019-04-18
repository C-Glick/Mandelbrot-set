import java.math.BigDecimal;
import java.math.MathContext;

import javax.swing.SwingWorker;

public class Tester{
	double threshold=Launcher.threshold;
	int limit=Launcher.limit;		//number of iterations to run before stopping
	MathContext rMode = ComplexLong.rMode;
	
	
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
		
		if(!Launcher.firstBoot) {
			//set progress bar data
			Launcher.display.progressBar.setMinimum(0);
			Launcher.display.progressBar.setMaximum(width*height);
		}

		
		//finds the complex numbers based on the pixel numbers
		//then tests those numbers 
		for(int x=0; x<width; x++) {
			double real= ((bottomRight.getReal()-topLeft.getReal())/width)*x+topLeft.getReal();
			
			for(int y=0;y<height;y++) {
				double imag= (((bottomRight.getImag()-topLeft.getImag())/height)*y+topLeft.getImag());
				result[x][y] = test1(new Complex(real,imag));
				if(!Launcher.firstBoot) {Launcher.display.progressBar.setValue((x*height)+y);}  //update progress bar

			}
			
		}
		return result;
	}
	
	//tests 3 and 4 are for high precision calculations
	
	/**
	 * The same as test1 but for High Precision.
	 * Tests if a single ComplexLong number is in the Mandelbrot set.
	 * If not returns the number of iterations taken to exceed the threshold.
	 * @param c The ComplexLong to test.
	 * @return A long telling the number of iterations taken to exceed the set threshold
	 * 		   returns 0 if the number is in the set (does not exceed the threshold within the set limit).
	 * @see test1
	 */
	public long test3(ComplexLong c) {
		BigDecimal thresholdHP = new BigDecimal(Double.toString(threshold));
		
		ComplexLong z = new ComplexLong(BigDecimal.ZERO,BigDecimal.ZERO);
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
	public long[][] test4(ComplexLong topLeftHP, ComplexLong bottomRightHP, BigDecimal widthHP, BigDecimal heightHP){
		int width = widthHP.intValue();
		int height = heightHP.intValue();
		
		long[][] result = new long[width][height];
		
		limit = Launcher.limit;
		
		if(!Launcher.firstBoot) {
			//set progress bar data
			Launcher.display.progressBar.setMinimum(0);
			Launcher.display.progressBar.setMaximum(width*height);
		}
		
		
		//finds the complex numbers based on the pixel numbers
		//then tests those numbers 
		for(int x=0; x<width; x++) {
			BigDecimal real = ((bottomRightHP.getReal().subtract(topLeftHP.getReal(),rMode)).divide(widthHP,rMode))
								.multiply(new BigDecimal(x),rMode).add(topLeftHP.getReal());
			
			
			for(int y=0; y<height; y++) {
				BigDecimal imag= ((bottomRightHP.getImag().subtract(topLeftHP.getImag(),rMode)).divide(heightHP,rMode))
								.multiply(new BigDecimal(y),rMode).add(topLeftHP.getImag(),rMode);
				result[x][y] = test3(new ComplexLong(real,imag));
				if(!Launcher.firstBoot) {Launcher.display.progressBar.setValue((x*height)+y);}  //update progress bar
			}
		}
		return result;
	}
	

}
