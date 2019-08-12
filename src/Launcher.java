import java.awt.Color;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.MathContext;

import resources.DoubleDouble;

public class Launcher {
	static GlobalHandler globalExceptionHandler = new GlobalHandler();
	static String title = "The MandelBrot Set";
	static int width = 800;
	static int height =400;
	static KeyManager keyManager;
	static MouseManager mouseManager;
	static Tester tester;
	static Display display;
	static double [][] resultsArray = new double[width][height];
	static Complex topLeft;
	static Complex bottomRight;
	boolean isUpdaterWorking;
	static boolean firstBoot = true;
	
	static BufferedImage buffImag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
	
	static boolean enableGrid = false;
	
	static double threshold = 2;							//threshold for the test, stops once the value grows higher than this (default is 2)
	static int limit =150; 									//think of this as the resolution of the graph, the more the function is allowed to iterate, the more detailed the result. This is the maximum number of iterations
	static double scale = 1;								//the scale of the graph, a higher number is more zoomed in
	static Complex center = new Complex (0,0);		//the center of the window, determines the bounds of the window from this number,

	public enum Precision{											//use an enumeration to define 3 possible precision states
		LOW_PRECISION, HIGH_PRECISION, INFINITE_PRECISION 
	}
	Precision precision = Precision.LOW_PRECISION;					//define the precision state and set it's default value
	
	/*static DoubleDoubleComplex topLeftHP;
	static DoubleDoubleComplex bottomRightHP;
	static DoubleDoubleComplex centerHP = new BigComplex(new BigDecimal(Double.toString(center.getReal())),new BigDecimal(Double.toString(center.getImag())));
	static DoubleDouble widthHP = new BigDecimal(width);
	static DoubleDouble heightHP = new BigDecimal(height);
	static DoubleDouble scaleHP = new BigDecimal(scale);
*/	
	static BigComplex topLeftIP;
	static BigComplex bottomRightIP;
	static BigComplex centerIP = new BigComplex(new BigDecimal(Double.toString(center.getReal())),new BigDecimal(Double.toString(center.getImag())));
	static BigDecimal widthIP = new BigDecimal(width);
	static BigDecimal heightIP = new BigDecimal(height);
	static BigDecimal scaleIP = new BigDecimal(scale);
	static MathContext rMode = BigComplex.rMode;
	
	
	
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(globalExceptionHandler);
	
		Launcher launcher = new Launcher();							//creates a new launcher object
		tester = new Tester();										//creates a new tester
		keyManager = new KeyManager(launcher);						//creates a the key listener
		mouseManager = new MouseManager (launcher);
		
		launcher.calculate(launcher);								//calculate the default values

		display = new Display(title,width,height,launcher);			//create a new display
		display.start();											//run setup on display and make it visible
		firstBoot=false;
	}
	
	public void refresh() {
		if(!isUpdaterWorking) {
			Updater updater = new Updater(this);
			updater.start();
			}
	}
	
	
	/**
	 * Calculate the graph to be drawn from the values set in the launcher class.
	 * @param launcher The launcher with the desired values.
	 */
	public void calculate(Launcher launcher) {
		switch (precision) {
			case LOW_PRECISION:
				double halfWidth = ((double)width/(scale*(double)512));
				double halfHeight = ((double)height/(scale*(double)512));
				
				topLeft = new Complex(center.getReal()-halfWidth, center.getImag()+halfHeight);					//Determine the complex numbers that correspond to the topLeft and bottom right pixels of the window
				bottomRight = new Complex(center.getReal()+halfWidth, center.getImag()-halfHeight);				//scale is multiplied by 512 to give good initial size
																												
				tester.test2(topLeft, bottomRight, width, height);												//tell the tester object to run test #2 given the details about the graph
				break;																							//the results will be saved into the resultsArray as each thread completes a calculation
			case HIGH_PRECISION:
				break;
				
				
				
				
			case INFINITE_PRECISION:
				BigDecimal correctedScale = scaleIP.multiply(new BigDecimal("512"));
				
				topLeftIP = new BigComplex(centerIP.getReal().subtract(widthIP.divide(correctedScale,rMode),rMode),centerIP.getImag().add(heightIP.divide(correctedScale,rMode),rMode));
				bottomRightIP = new BigComplex (centerIP.getReal().add(widthIP.divide(correctedScale,rMode),rMode),centerIP.getImag().subtract(heightIP.divide(correctedScale,rMode),rMode));
				
				tester.test4(topLeftIP, bottomRightIP, widthIP, heightIP);
				break;
		}
	}
	
	
	public void setResultsArray(double [][] input) {
		Launcher.resultsArray = input;
	}
	public void setScale(double input) {
		Launcher.scale = input;
		System.out.println("new scale: " + Launcher.scale);
	}
	public void setScaleIP(BigDecimal input) {
		Launcher.scaleIP = input;
	}
	
	/**
	 * Set center based on the X and Y values of the image *NOT the graph*
	 * 0,0 is the upper right hand corner.
	 * @param x the images x value to be the center
	 * @param y the images y value to be the center
	 */
	public void setCenter(int x, int y) {
		Complex complex = new Complex(((bottomRight.getReal()-topLeft.getReal())/width)*x+topLeft.getReal(),
				(((bottomRight.getImag()-topLeft.getImag())/height)*y+topLeft.getImag()));
		setCenter(complex);
	}
	
	public void setCenter (Complex input) {
		Launcher.center = input;
		Launcher.centerIP = new BigComplex(new BigDecimal(Double.toString(input.getReal())),new BigDecimal(Double.toString(input.getImag())));
	}
	public void setCenterIP (BigComplex input) {
		Launcher.centerIP = input;
		
	}
	
	public void setCenterIP (int x, int y) {		
		BigComplex complexLong = new BigComplex(((bottomRightIP.getReal().subtract(topLeftIP.getReal())).divide(widthIP))
									.multiply(new BigDecimal(x)).add(topLeftIP.getReal()),
								((bottomRightIP.getImag().subtract(topLeftIP.getImag())).divide(heightIP))
										.multiply(new BigDecimal(y)).add(topLeftIP.getImag()));
		
		setCenterIP(complexLong);
	}
	
	public void setLimit (int input) {
		Launcher.limit = input;
		System.out.println("New limit: " + Launcher.limit );
	}
	
	/**
	 * For low precision pass in "LP", high precision = "HP", infinite precision = "IP"
	 * @param newPrecision
	 */
	public void setPrecision(Precision newPrecision) {
		switch (precision){
		case LOW_PRECISION:
			switch (newPrecision){
			case HIGH_PRECISION:
				break;
			case INFINITE_PRECISION:
				precision = Precision.INFINITE_PRECISION;
				centerIP = new BigComplex(new BigDecimal(center.getReal()),new BigDecimal(center.getImag()));
				widthIP = new BigDecimal(width);
				heightIP = new BigDecimal(height);
				scaleIP = new BigDecimal(scale);
				break;
			}
			break;
		case HIGH_PRECISION:
			switch (newPrecision){
			case LOW_PRECISION:
				break;
			case INFINITE_PRECISION:
				break;
			}
			break;
		case INFINITE_PRECISION:
			switch (newPrecision){
			case LOW_PRECISION:
				precision = Precision.LOW_PRECISION;
				scale = scaleIP.doubleValue();
				center = new Complex(centerIP.real.doubleValue(), centerIP.imag.doubleValue());
				break;
			case HIGH_PRECISION:
				break;
			}
	
		}
		System.out.println("Precision set to "+newPrecision);
	}
	
	public KeyManager getKeyManager() {
		return Launcher.keyManager;
	}
	public MouseManager getMouseManager() {
		return Launcher.mouseManager;
	}
	public double getScale() {
		return Launcher.scale;
	}
	public BigDecimal getScaleHP() {
		return Launcher.scaleIP;
	}
	public int getLimit() {
		return Launcher.limit;
	}
	public Display getDisplay() {
		return Launcher.display;
	}
	public Tester getTester() {
		return Launcher.tester;
	}
}
