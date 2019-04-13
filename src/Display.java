import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JMenu;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
//this class handles the display window that the game runs it
public class Display extends Canvas{
	String title;
	int width; 
	int height;
	Launcher launcher;
	JFrame frame;
	Graphics g;
	
	JPanel topBar;
	JLabel scaleDisplay;
	JLabel limitDisplay;
	JLabel centerXDisplay;
	JLabel centerYDisplay;
	JProgressBar progressBar;
	
	Font font = new Font("Arial Unicode MS", Font.PLAIN, 14);
	
 
	
	Display(String title,int width, int height, Launcher launcher){
		this.title = title;
		this.height = height;
		this.width = width;
		this.launcher = launcher;
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void start() {
		frame = new JFrame(title);										//create the main window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			//exits the program when window is closed
		frame.addKeyListener(launcher.getKeyManager());					//add key manager to main window
		
		topBar = new JPanel();										//create top  bar
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{120, 71, 103, 0};
		gbl_panel.rowHeights = new int[]{0, 21, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		topBar.setLayout(gbl_panel);
		frame.getContentPane().add(topBar, BorderLayout.NORTH);
		
		scaleDisplay = new JLabel();											//create JLabel for scaleDisplay
		scaleDisplay.setText("Scale = "+launcher.getScale());					//set the text for the scale
		scaleDisplay.setFont(font);
		GridBagConstraints gbc_scale = new GridBagConstraints();
		gbc_scale.anchor = GridBagConstraints.WEST;
		gbc_scale.insets = new Insets(0, 0, 5, 5);
		gbc_scale.gridx = 0;
		gbc_scale.gridy = 0;
		topBar.add(scaleDisplay, gbc_scale);
		
		centerXDisplay = new JLabel("X = " + Launcher.center.getReal());
		GridBagConstraints gbc_centerXDisplay = new GridBagConstraints();
		gbc_centerXDisplay.insets = new Insets(0, 0, 5, 5);
		gbc_centerXDisplay.gridx = 1;
		gbc_centerXDisplay.gridy = 0;
		topBar.add(centerXDisplay, gbc_centerXDisplay);
		
		centerYDisplay = new JLabel("Y = " + Launcher.center.getImag());
		GridBagConstraints gbc_centerYDisplay = new GridBagConstraints();
		gbc_centerYDisplay.insets = new Insets(0, 0, 0, 5);
		gbc_centerYDisplay.gridx = 1;
		gbc_centerYDisplay.gridy = 1;
		topBar.add(centerYDisplay, gbc_centerYDisplay);
		
		
		limitDisplay = new JLabel("Limit = " + launcher.getLimit());
		limitDisplay.setFont(font);
		GridBagConstraints gbc_limitDisplay= new GridBagConstraints();
		gbc_limitDisplay.anchor = GridBagConstraints.WEST;
		gbc_limitDisplay.insets = new Insets(0, 0, 0, 5);
		gbc_limitDisplay.gridx = 0;
		gbc_limitDisplay.gridy = 1;
		topBar.add(limitDisplay, gbc_limitDisplay);
		
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(false);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.VERTICAL;
		gbc_progressBar.anchor = GridBagConstraints.WEST;
		gbc_progressBar.gridx = 2;
		gbc_progressBar.gridy = 1;
		topBar.add(progressBar, gbc_progressBar);
		
		
		Canvas canvas = new Display(title,height,width,launcher);
		canvas.setSize(width, height);
		canvas.setFocusable(false);
		frame.getContentPane().add(canvas);	
		
		
		frame.pack();
		frame.setVisible(true);
		g = canvas.getGraphics();
	}
	
	public void updateUI() {
		scaleDisplay.setText("Scale = " + launcher.getScale());
		limitDisplay.setText("Limit = " + launcher.getLimit());
		
	}
	
	 public void paint(Graphics g) {
		 launcher.getDisplay().render(g);
	 }
	 /**
	  * custom repaint method
	  */
	 public void repaint() {
		 if(g==null) {
			 System.out.println("must call paint method before repaint");
			 return;
		 }
		 render(g);
	 }

	/** Takes the current values in the resultsArray, converts the result to a
	 * 	color based on the number of iterations taken to reach the limit, then sets
	 * 	the pixel color at that location accordingly.
	 * 
	 * @param g The graphics object used to draw to the canvas, comes from the display window.
	 */
	public void render(Graphics g) {				//this render method is called from the display class after the canvas is visible, or when repainting the canvas
	       for (int x=0;x<Launcher.width;x++) {					//loop through all x and y values
	    	   for (int y=0;y<Launcher.height;y++) {
	    		   
	    		   double result = Launcher.resultsArray[x][y];				//get the result of the complex number at each pixel location
	    		   if (result==0) {			//is in set
	    			   g.setColor(Color.BLACK);
	    		   }else { 
		    		   g.setColor(Color.getHSBColor((float)(result/Launcher.limit), 1, 1));			//set the color with by creating a HSB color, set the hue to be the result(number of iterations to reach threshold) divided by the limit (max number of iterations), 
	    		   }
	    		   g.drawRect(x, y, 1, 1);										//set the pixel at x,y to the color
	    		   //TODO: is there a faster way to set pixel colors?
	    	   }
	       }
	       
	     
	       //TODO: create grid display
	       if(Launcher.enableGrid) {
	    	   
	       }
	}
}