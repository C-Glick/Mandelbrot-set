import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
//this class handles the display window that the game runs it
public class Display extends Canvas{
	String title;
	int width; 
	int height;
	Launcher launcher;
	JFrame frame;
	Graphics g;
	
	JLabel scale;
	JToolBar toolBar;
	JProgressBar progressBar;

	
	Display(String title,int width, int height, Launcher launcher){
		this.title = title;
		this.height = height;
		this.width = width;
		this.launcher = launcher;
	}
	
	public void start() {
		frame = new JFrame(title);										//create the main window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			//exits the program when window is closed
		frame.addKeyListener(launcher.getKeyManager());					//add key manager to main window
		
		toolBar = new JToolBar();								//create top tool bar
		scale = new JLabel();									//create Jlabel for Scale
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		scale.setText("Scale = "+launcher.getScale());					//set the text for the scale
		toolBar.add(scale);
		toolBar.add(progressBar);
		
		
		Canvas canvas = new Display(title,height,width,launcher);
		canvas.setSize(width, height);
		canvas.setFocusable(false);
		frame.add(canvas);	
		frame.add(toolBar, BorderLayout.NORTH);
		frame.pack();
		frame.setVisible(true);
		g = canvas.getGraphics();
	}
	
	public void updateUI() {
		scale.setText("Scale = " + launcher.getScale());
		
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