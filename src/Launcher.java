import java.awt.Font;
import java.math.BigDecimal;

public class Launcher {
	static String title = "The MandelBrot Set";
	static int width = 1000;
	static int height = 1000;
	static KeyManager keyManager;
	static MouseManager mouseManager;
	static Tester tester;
	static Display display;
	static long [][] resultsArray;
	static Complex topLeft;
	static Complex bottomRight;
	boolean isUpdaterWorking;
	
	boolean highPrecision = false;			//high precision variables
	static ComplexLong topLeftHP;
	static ComplexLong bottomRightHP;
	static ComplexLong centerHP = new ComplexLong(new BigDecimal(-1),new BigDecimal(-1));
	
	static boolean enableGrid = false;
	
	static double threshold = 2;							//threshold for the test, stops once the value grows higher than this (default is 2)
	static int limit =150; 									//think of this as the resolution of the graph, the more the function is allowed to iterate, the more detailed the result. This is the maximum number of iterations
	static double scale = 1;								//the scale of the graph, a higher number is more zoomed in
	static Complex center = new Complex (-0.79,0.15);		//the center of the window, determines the bounds of the window from this number,

	public static void main(String[] args) {
	
		Launcher launcher = new Launcher();							//creates a new launcher object
		tester = new Tester();										//creates a new tester
		keyManager = new KeyManager(launcher);						//creates a the key listener
		mouseManager = new MouseManager (launcher);
		
		launcher.calculate(launcher);								//calculate the default values

		display = new Display(title,width,height,launcher);			//create a new display
		display.start();											//run setup on display and make it visible
		
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
		if(!highPrecision) {																				//low precision
			topLeft = new Complex(center.getReal()-(1/scale),center.getImag()-(1/scale));					//Determine the complex numbers that correspond to the topLeft and bottom right pixels of the window
			bottomRight = new Complex(center.getReal()+(1/scale),center.getImag()+(1/scale));
			
			launcher.setResultsArray(tester.test2(topLeft, bottomRight, width, height));						//tell the tester object to run test #2 given the topLeft and bottomRight Complex numbers
																												//then saves the results to resultsArray
		}else {																								//high precision
			BigDecimal inverseScale = (new BigDecimal("1").divide(new BigDecimal(Double.toString(scale))));
			
			topLeftHP = new ComplexLong(centerHP.getReal().subtract(inverseScale),centerHP.getImag().subtract(inverseScale));
			bottomRightHP = new ComplexLong (centerHP.getReal().add(inverseScale),centerHP.getImag().add(inverseScale));
			launcher.setResultsArray(tester.test4(topLeftHP, bottomRightHP, width, height));
			
		}
	}
	
	public void setCenter(int x, int y) {
		Complex complex = new Complex(((bottomRight.getReal()-topLeft.getReal())/width)*x+topLeft.getReal(),
				(((bottomRight.getImag()-topLeft.getImag())/height)*y+topLeft.getImag()));
		setCenter(complex);
	}
	
	public void setResultsArray(long [][] input) {
		Launcher.resultsArray = input;
	}
	public void setScale(double input) {
		Launcher.scale = input;
		System.out.println("new scale: " + Launcher.scale);
	}
	public void setCenter (Complex input) {
		Launcher.center = input;
		Launcher.centerHP = new ComplexLong(new BigDecimal(input.getReal()),new BigDecimal(input.getImag()));
	}
	public void setLimit (int input) {
		Launcher.limit = input;
		System.out.println("New limit: " + Launcher.limit );
	}
	public void setHighPrecision(boolean input) {
		highPrecision = input;
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
