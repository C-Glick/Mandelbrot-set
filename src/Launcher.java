import java.awt.Color;
import java.awt.Graphics;

public class Launcher {
	static String title = "The MandelBrot Set";
	static int width = 1000;
	static int height = 1000;
	static double [][] array;
	static boolean enableGrid = false;
	
	static double threshold = 2;
	static int limit =100; 				//think of this as the resolution of the graph, the more the function is allowed to iterate, the more detailed the result
	static double scale = 500;
	static Complex center = new Complex (-0.79,0.15);		//the center of the window, determines the bounds of the window from this number
	
	static Complex topLeft = new Complex(center.getReal()-(1/scale),center.getImag()-(1/scale));
	static Complex bottomRight = new Complex(center.getReal()+(1/scale),center.getImag()+(1/scale));


	public static void main(String[] args) {
	
		Launcher launcher = new Launcher();
		Tester tester = new Tester();
		
		
		launcher.setArray(tester.test2(topLeft, bottomRight, width, height));
		Display display = new Display(title,width,height,launcher);
		display.start();
		
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
}
