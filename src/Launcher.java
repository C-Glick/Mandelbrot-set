import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Launcher {

	public static void main(String[] args) {
		Display display = new Display("title",500,500);
		Graphics g = display.initDisplay();
		
		
		//Complex a = new Complex(0,0.27);
		//Tester tester = new Tester();
		//System.out.println(a.abs());
		
		//System.out.println(tester.test1(a));
	}

}
