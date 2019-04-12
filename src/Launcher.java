import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Launcher {
	static String title = "The MandelBrot Set";
	static int width = 1000;
	static int height = 1000;
	static KeyManager keyManager;
	static Tester tester;
	static Display display;
	static double [][] resultsArray;
	static boolean enableGrid = false;
	
	static double threshold = 2;							//threshold for the test, stops once the value grows higher than this (default is 2)
	static int limit =150; 									//think of this as the resolution of the graph, the more the function is allowed to iterate, the more detailed the result. This is the maximum number of iterations
	static double scale = 1;								//the scale of the graph, a higher number is more zoomed in
	static Complex center = new Complex (-0.79,0.15);		//the center of the window, determines the bounds of the window from this number,
	
	static Complex topLeft;
	static Complex bottomRight;


	public static void main(String[] args) {
	
		Launcher launcher = new Launcher();							//creates a new launcher object
		tester = new Tester();										//creates a new tester
		keyManager = new KeyManager(launcher);						//creates a the key listener
		
		launcher.calculate(launcher);								//calculate the default values

		display = new Display(title,width,height,launcher);			//create a new display
		display.start();											//run setup on display and make it visible
		
	}
	
	public void refresh() {
		calculate(this);
		getDisplay().repaint();
	}
	
	
	/**
	 * Calculate the graph to be drawn from the values set in the launcher class.
	 * @param launcher The launcher with the desired values.
	 */
	public void calculate(Launcher launcher) {
		/*if(limit/scale < 1) {
			setLimit(getLimit()*5);
			System.out.println("adjust limit,"+ getLimit() );
		}
		*/
		
		topLeft = new Complex(center.getReal()-(1/scale),center.getImag()-(1/scale));					//Determine the complex numbers that correspond to the topLeft and bottom right pixels of the window
		bottomRight = new Complex(center.getReal()+(1/scale),center.getImag()+(1/scale));
		
		launcher.setResultsArray(tester.test2(topLeft, bottomRight, width, height));						//tell the tester object to run test #2 given the topLeft and bottomRight Complex numbers
																											//then saves the results to resultsArray
	}
	/** Takes the current values in the resultsArray, converts the result to a
	 * 	color based on the number of iterations taken to reach the limit, then sets
	 * 	the pixel color at that location accordingly.
	 * 
	 * @param g The graphics object used to draw to the canvas, comes from the display window.
	 */
	public void render(Graphics g) {				//this render method is called from the display class after the canvas is visible, or when repainting the canvas
	       for (int x=0;x<width;x++) {					//loop through all x and y values
	    	   for (int y=0;y<height;y++) {
	    		   
	    		   double result = resultsArray[x][y];				//get the result of the complex number at each pixel location
	    		   if (result==0) {			//is in set
	    			   g.setColor(Color.BLACK);
	    		   }else { 
		    		   g.setColor(Color.getHSBColor((float)(result/limit), 1, 1));			//set the color with by creating a HSB color, set the hue to be the result(number of iterations to reach threshold) divided by the limit (max number of iterations), 
	    		   }
	    		   g.drawRect(x, y, 1, 1);										//set the pixel at x,y to the color
	    		   //TODO: is there a faster way to set pixel colors?
	    	   }
	       }
	       
	     
	       //TODO: create grid display
	       if(enableGrid) {
	    	   
	       }
	}
	
	public void setResultsArray(double [][] input) {
		Launcher.resultsArray = input;
	}
	public void setScale(double input) {
		Launcher.scale = input;
		System.out.println("new scale: " + Launcher.scale);
	}
	public void setCenter (Complex input) {
		Launcher.center = input;
	}
	public void setLimit (int input) {
		Launcher.limit = input;
		System.out.println("New limit: " + Launcher.limit );
	}
	
	public KeyManager getKeyManager() {
		return Launcher.keyManager;
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
