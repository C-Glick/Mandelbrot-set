import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.MathContext;

public class Launcher {
	static String title = "The MandelBrot Set";
	static int width = 500;
	static int height =500;
	static KeyManager keyManager;
	static MouseManager mouseManager;
	static Tester tester;
	static Display display;
	static int [][] resultsArray = new int[width][height];
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

	boolean highPrecision = false;			//high precision variables
	static ComplexLong topLeftHP;
	static ComplexLong bottomRightHP;
	static ComplexLong centerHP = new ComplexLong(new BigDecimal(Double.toString(center.getReal())),new BigDecimal(Double.toString(center.getImag())));
	static BigDecimal widthHP = new BigDecimal(width);
	static BigDecimal heightHP = new BigDecimal(height);
	static BigDecimal scaleHP = new BigDecimal(scale);
	static MathContext rMode = ComplexLong.rMode;
	
	
	
	public static void main(String[] args) {
	
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
		
		if(!highPrecision) { //low precision
			topLeft = new Complex(center.getReal()-(width/(scale*512)),center.getImag()+(height/(scale*512)));					//Determine the complex numbers that correspond to the topLeft and bottom right pixels of the window
			bottomRight = new Complex(center.getReal()+(width/(scale*512)),center.getImag()-(height/(scale*512)));				//scale is multiplied by 512 to give good initial size
			
			//launcher.setResultsArray(tester.test2(topLeft, bottomRight, width, height));						//tell the tester object to run test #2 given the topLeft and bottomRight Complex numbers
																												//then saves the results to resultsArray
			tester.test2(topLeft, bottomRight, width, height);
		}else { //high precision
			BigDecimal correctedScale = scaleHP.multiply(new BigDecimal("512"));
			
			topLeftHP = new ComplexLong(centerHP.getReal().subtract(widthHP.divide(correctedScale,rMode),rMode),centerHP.getImag().add(heightHP.divide(correctedScale,rMode),rMode));
			bottomRightHP = new ComplexLong (centerHP.getReal().add(widthHP.divide(correctedScale,rMode),rMode),centerHP.getImag().subtract(heightHP.divide(correctedScale,rMode),rMode));
			
			//launcher.setResultsArray(tester.test4(topLeftHP, bottomRightHP, widthHP, heightHP));
			tester.test4(topLeftHP, bottomRightHP, widthHP, heightHP);
		}
	}
	
	
	public void setResultsArray(int [][] input) {
		Launcher.resultsArray = input;
	}
	public void setScale(double input) {
		Launcher.scale = input;
		System.out.println("new scale: " + Launcher.scale);
	}
	public void setScaleHP(BigDecimal input) {
		Launcher.scaleHP = input;
	}
	
	public void setCenter(int x, int y) {
		Complex complex = new Complex(((bottomRight.getReal()-topLeft.getReal())/width)*x+topLeft.getReal(),
				(((bottomRight.getImag()-topLeft.getImag())/height)*y+topLeft.getImag()));
		setCenter(complex);
	}
	
	public void setCenter (Complex input) {
		Launcher.center = input;
		Launcher.centerHP = new ComplexLong(new BigDecimal(Double.toString(input.getReal())),new BigDecimal(Double.toString(input.getImag())));
	}
	public void setCenterHP (ComplexLong input) {
		Launcher.centerHP = input;
		
	}
	
	public void setCenterHP (int x, int y) {		
		ComplexLong complexLong = new ComplexLong(((bottomRightHP.getReal().subtract(topLeftHP.getReal())).divide(widthHP))
									.multiply(new BigDecimal(x)).add(topLeftHP.getReal()),
								((bottomRightHP.getImag().subtract(topLeftHP.getImag())).divide(heightHP))
										.multiply(new BigDecimal(y)).add(topLeftHP.getImag()));
		
		setCenterHP(complexLong);
		
	}
	
	public void setLimit (int input) {
		Launcher.limit = input;
		System.out.println("New limit: " + Launcher.limit );
	}
	public void setHighPrecision(boolean input) {
		highPrecision = input;
		if(input) {
			centerHP = new ComplexLong(new BigDecimal(Double.toString(center.getReal())),new BigDecimal(Double.toString(center.getImag())));
			widthHP = new BigDecimal(width);
			heightHP = new BigDecimal(height);
			scaleHP = new BigDecimal(scale);
		}else {
			scale = scaleHP.doubleValue();
			center = new Complex(centerHP.real.doubleValue(), centerHP.imag.doubleValue());
		}
		System.out.println("high precision set to "+input);
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
		return Launcher.scaleHP;
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
