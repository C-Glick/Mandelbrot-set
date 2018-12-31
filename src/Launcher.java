import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferStrategy;


public class Launcher {
	static String title = "The MandelBrot Set";
	static int width = 1000;
	static int height = 1000;
	static double [][] array;
	
	static double threshold = 2;
	static int limit = 50; 				//think of this as the resolution of the graph, the more the function is allowed to iterate, the more detailed the result

	public static void main(String[] args) {
	
		Launcher launcher = new Launcher();
		Tester tester = new Tester();
		launcher.setArray(tester.test2(new Complex(-2,2), new Complex(2,-2), width, height));
		System.out.println(array);
		Display display = new Display(title,width,height,launcher);
		display.start();
		
		//Complex a = new Complex(0,0.27);
		//Tester tester = new Tester();
		//System.out.println(a.abs());
		
		//System.out.println(tester.test1(a));
	}

	public void render(Graphics g) {		//this render method is called from the display class after the canvas is visible
	       for (int x=0;x<width;x++) {
	    	   for (int y=0;y<height;y++) {
	    		   
	    		   double iterations = array[x][y];
	    		   if (iterations==0) {					//is in set
	    			   g.setColor(Color.BLACK);
	    		   }else {
	    			   
		    		   g.setColor(Color.getHSBColor((float)(iterations/limit), 1, 1));			//set the color with by creating a HSB color,
	    		   }
	    		   
	    		   g.drawRect(x, y, 1, 1);										//set the pixel at x,y to the color
	    	   }
	       }
	}
	
	public void setArray(double [][] input) {
		this.array = input;
	}
}
