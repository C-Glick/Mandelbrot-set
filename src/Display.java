import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
//this class handles the display window that the game runs it
public class Display extends Canvas{
	String title;
	int width; 
	int height;
	Launcher launcher;

	
	Display(String title,int width, int height, Launcher launcher){
		this.title = title;
		this.height = height;
		this.width = width;
		this.launcher = launcher;
	}
	
	public void start() {
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Canvas canvas = new Display(title,height,width,launcher);
		canvas.setSize(width, height);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}
	
	 public void paint(Graphics g) {
		 launcher.render(g);
	 }
}