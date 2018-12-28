import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
//this class handles the display window that the game runs it
public class Display {

	private String title;
	private int width, height;
	
	private JFrame frame;
	private Canvas canvas;
	private Graphics g;
	private BufferStrategy bs;
	
	public Display(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height =height;
		
		createDisplay();
	}
	
	//Initialize the display and all its parameters
	private void createDisplay() {
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		//create a new canvas for the graphics to be drawn on
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width,height));
		canvas.setMaximumSize(new Dimension(width,height));
		canvas.setMinimumSize(new Dimension(width,height));
		canvas.setFocusable(false);
		
		frame.add(canvas);
		frame.pack();				//packs the frame so the canvas fits appropriately
	}
	
	public Graphics initDisplay() {
		bs = this.getCanvas().getBufferStrategy();		//gets buffer strat from canvas, if non exists set it
		if(bs==null) {
			this.getCanvas().createBufferStrategy(3);
		}
		g = bs.getDrawGraphics();							//get the graphics drawer from the buffer strat
		return g;
	}
	public Canvas getCanvas() {
		return canvas;
	}
	public JFrame getFrame() {
		return frame;
	}
}
