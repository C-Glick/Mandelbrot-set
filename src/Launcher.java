import java.awt.Color;
import java.awt.Graphics;

public class Launcher {
	static String title = "The MandelBrot Set";
	static int width = 1000;
	static int height = 1000;
	static KeyManager keyManager;
	static Tester tester;
	static Display display;
	static double [][] array;
	static boolean enableGrid = false;
	
	static double threshold = 2;
	static int limit =150; 				//think of this as the resolution of the graph, the more the function is allowed to iterate, the more detailed the result
	static double scale = 1;
	static Complex center = new Complex (-0.79,0.15);		//the center of the window, determines the bounds of the window from this number
	
	static Complex topLeft;
	static Complex bottomRight;


	public static void main(String[] args) {
	
		Launcher launcher = new Launcher();
		tester = new Tester();
		keyManager = new KeyManager(launcher);
		keyManager.start();
		
		launcher.calculate(launcher);

		display = new Display(title,width,height,launcher);
		display.start();
		
	}
	
	public void calculate(Launcher launcher) {		//Calculates the graph to be drawn
		topLeft = new Complex(center.getReal()-(1/scale),center.getImag()-(1/scale));
		bottomRight = new Complex(center.getReal()+(1/scale),center.getImag()+(1/scale));
		
		launcher.setArray(tester.test2(topLeft, bottomRight, width, height));
	
	}

	public void render(Graphics g) {		//this render method is called from the display class after the canvas is visible
	       for (int x=0;x<width;x++) {
	    	   for (int y=0;y<height;y++) {
	    		   
	    		   double result = array[x][y];
	    		   if (result==0) {					//is in set
	    			   g.setColor(Color.BLACK);
	    		   }else { 
		    		   g.setColor(Color.getHSBColor((float)(result/limit), 1, 1));			//set the color with by creating a HSB color,
	    		   }
	    		   g.drawRect(x, y, 1, 1);										//set the pixel at x,y to the color
	    	   }
	       }
	     
	       //TODO: create grid display
	       if(enableGrid) {
	    	   
	       }
	}
	
	public void setArray(double [][] input) {
		Launcher.array = input;
	}
	public void setScale(double input) {
		Launcher.scale = input;
	}
	public void setCenter (Complex input) {
		Launcher.center = input;
	}
	
	
	public KeyManager getKeyManager() {
		return Launcher.keyManager;
	}
	public double getScale() {
		return Launcher.scale;
	}
	public Display getDisplay() {
		return Launcher.display;
	}
	public Tester getTester() {
		return Launcher.tester;
	}
}
