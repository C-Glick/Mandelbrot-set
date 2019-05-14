import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Tester{
	double threshold=Launcher.threshold;
	BigDecimal thresholdHP = BigDecimal.valueOf(Launcher.threshold);		//threshold doesn't really need high precision but a BigDecimal object is needed for calculations
	int limit=Launcher.limit;		//number of iterations to run before stopping
	MathContext rMode = ComplexLong.rMode;
	ForkJoinPool commonPool = ForkJoinPool.commonPool();
	
	
	/**
	 * Test for just one complex number, returns the number of iterations it took to reach the threshold. Will be 0 if 
	 * iteration limit is reached, and thus, is in the Mandelbrot set. 
	 * @param c The complex number to test.
	 * @return A long, telling the number of iterations taken to reach the threshold.
	 * @deprecated use BasicTest class instead
	 */
	public int test1(Complex c) {										
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
	public void test2(Complex topLeft, Complex bottomRight,int width,int height){		
		limit = Launcher.limit;		//update the limit to be the same as it is in Launcher
		
		if(!Launcher.firstBoot) {	//dont call progress bars on the first boot
			//set progress bar data
			Launcher.display.progressBar.setMinimum(0);
			Launcher.display.progressBar.setMaximum(width*height);
			Launcher.display.progressBar.setValue(0);
		}

		
		//finds the complex numbers based on the pixel numbers
		//then tests those numbers 
		for(int x=0; x<width; x++) {
			double real= ((bottomRight.getReal()-topLeft.getReal())/width)*x+topLeft.getReal();
			
			for(int y=0;y<height;y++) {
				double imag= topLeft.getImag()-((topLeft.getImag()-bottomRight.getImag())/height)*y;
				
				//bulb checking
				double q = Math.pow((real-0.25),2)+Math.pow(imag, 2);
				if(q*(q+(real-0.25)) <= 0.25*Math.pow(imag, 2)) {
					//if true number is in bulb so it has to be in the set (black)
					Launcher.resultsArray[x][y] = 0;
				}else {
					BasicTest task = new BasicTest(new Complex(real,imag),threshold,x,y);		//create BasicTest object 
					
					commonPool.submit(task);														//submits BasicTest to pool to be executed later
				}
			}    	
		}
		while(!commonPool.isQuiescent()) {		//loop until all test have finished executing
			if(!Launcher.firstBoot) {Launcher.display.progressBar.setValue(width*height - commonPool.getQueuedSubmissionCount());}  //update progress bar
		}
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
	 * @deprecated use HPTest class instead
	 */
	public int test3(ComplexLong c) {
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
	public void test4(ComplexLong topLeftHP, ComplexLong bottomRightHP, BigDecimal widthHP, BigDecimal heightHP){
		int width = widthHP.intValue();
		int height = heightHP.intValue();		
		limit = Launcher.limit;
		
		if(!Launcher.firstBoot) {
			//set progress bar data
			Launcher.display.progressBar.setMinimum(0);
			Launcher.display.progressBar.setMaximum(width*height);
			Launcher.display.progressBar.setValue(0);

		}
		
		
		//finds the complex numbers based on the pixel numbers
		//then tests those numbers 
		BigDecimal fourth = new BigDecimal("0.25");
		for(int x=0; x<width; x++) {
			BigDecimal real = ((bottomRightHP.getReal().subtract(topLeftHP.getReal(),rMode)).divide(widthHP,rMode))
								.multiply(BigDecimal.valueOf(x),rMode).add(topLeftHP.getReal(),rMode);
			
			
			for(int y=0; y<height; y++) {
				BigDecimal imag= ((bottomRightHP.getImag().subtract(topLeftHP.getImag(),rMode)).divide(heightHP,rMode))
								.multiply(BigDecimal.valueOf(y),rMode).add(topLeftHP.getImag(),rMode);
				
				//bulb checking
				BigDecimal q = (real.subtract(fourth)).pow(2).add(imag.pow(2));
				if(q.multiply(q.add(real.subtract(fourth))).compareTo(fourth.multiply(imag.pow(2))) <=0 ) {
					//if true number is in bulb so it has to be in the set (black)
					Launcher.resultsArray[x][y]= 0;
				}else {
					HPTest task = new HPTest(new ComplexLong(real,imag),thresholdHP,x,y);
					commonPool.submit(task);
				}
			}
		}
		while(!commonPool.isQuiescent()) {
			if(!Launcher.firstBoot) {Launcher.display.progressBar.setValue(width*height - commonPool.getQueuedSubmissionCount());}  //update progress bar
		}
	}
}
