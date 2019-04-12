import java.awt.Canvas;
import java.awt.Graphics;

import javax.swing.JFrame;
//this class handles the display window that the game runs it
public class Display extends Canvas{
	String title;
	int width; 
	int height;
	Launcher launcher;
	JFrame frame;
	Graphics g;

	
	Display(String title,int width, int height, Launcher launcher){
		this.title = title;
		this.height = height;
		this.width = width;
		this.launcher = launcher;
	}
	
	public void start() {
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(launcher.getKeyManager());
		
		Canvas canvas = new Display(title,height,width,launcher);
		canvas.setSize(width, height);
		canvas.setFocusable(false);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
		g = canvas.getGraphics();
	}
	
	 public void paint(Graphics g) {
		 launcher.render(g);
	 }
	 /**
	  * custom repaint method
	  */
	 public void repaint() {
		 if(g==null) {
			 System.out.println("must call paint method before repaint");
			 return;
		 }
		 launcher.render(g);
	 }
}