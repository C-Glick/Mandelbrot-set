import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferStrategy;


public class Launcher {
	static String title = "kill me";
	static int width = 500;
	static int height = 500;

	public static void main(String[] args) {
		Launcher launcher = new Launcher();
		Tester tester = new Tester();
		tester.test2(new Complex(-2,2), new Complex(2,-2), width, height);
		
	//	Display display = new Display(title,width,height,launcher);
	//	display.start();
		
		//Complex a = new Complex(0,0.27);
		//Tester tester = new Tester();
		//System.out.println(a.abs());
		
		//System.out.println(tester.test1(a));
	}

	public void render(Graphics g) {		//this render method is called from the display class after the canvas is visible
	       for (int x=0;x<width;x++) {
	    	   for (int y=0;y<height;y++) {
	    		   
	    		   g.setColor(Color.getHSBColor(1, 0, (float)x/width));			//set the color with by creating a HSB color,
	    		   g.drawRect(x, y, 1, 1);										//set the pixel at x,y to the color
	    	   }
	       }
	}
}
