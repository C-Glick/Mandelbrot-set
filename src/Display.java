import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Node;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.imageio.*;
import javax.imageio.stream.*;
import javax.imageio.metadata.*;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
//this class handles the display window that the game runs it
public class Display extends Canvas{
	String title;
	int width; 
	int height;
	Launcher launcher;
	JFrame frame;
	private Timer resizeTimer;
	boolean autoResize=true;
	Canvas canvas;
	Graphics canvasG;
	
	boolean absoluteColorMode;
	Gradient gradient;
	double colorOffset;
	ColorCycle colorCycle;

	
	String OS = System.getProperty("os.name");
	File userSaveFile;		//the file location with no numbering
	File saveFile;			//the actual file output with numbering
	int imageIndex=0;
	File importImageFile;
	
	JPanel topBar;
	JLabel scaleDisplay;
	JLabel limitDisplay;
	JLabel centerXDisplay;
	JLabel centerYDisplay;
	JProgressBar progressBar;
	
	private ColorPicker picker;
	
	private Cursor cursor;
	
	Font font = new Font("Arial Unicode MS", Font.PLAIN, 14);
	private JMenuItem infinitePrecisionBtn;
	private JMenuItem lowPrecisionBtn;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem setSaveLocationBtn;
	private JMenuItem exportImageBtn;
	JLabel timeEstimateDisplay;
	private JMenu mnView;
	private JMenu mnReset;
	private JMenuItem mntmResetScaleOnly;
	private JMenuItem mntmResetPositionOnly;
	private JMenuItem mntmResetLimitOnly;
	private JMenuItem mntmResetAll;
	private JMenuItem mntmImportImage;
	private JMenu mnSetResolution;
	private JMenuItem mntmx;
	private JMenuItem mntmx_1;
	private JMenuItem mntmx_2;
	private JMenuItem mntmx_3;
	private JMenuItem mntmx_4;
	private JMenuItem mntmx_5;
	private JMenuItem mntmx_6;
	private JMenuItem mntmx_7;
	private JMenuItem mntmColorCycle;
	private JMenu mnColorCycling;
	private JMenuItem mntmStart;
	private JMenuItem mntmStop;
	private JMenu mnColoringMode;
	private JMenuItem mntmAbsoulteColoring;
	private JMenuItem mntmRelativeColoring;
	private JMenuItem highPrecisionBtn;
	
 
	
	Display(String title,int width, int height, Launcher launcher){
		this.title = title;
		this.height = height;
		this.width = width;
		this.launcher = launcher;
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error, look and feel not supported: " + e);
		}
		
		cursor = new Cursor(Cursor.HAND_CURSOR);
		
		absoluteColorMode=true;
		
		//picker = new ColorPicker();
		//picker.show();
		
		//create the default gradient
		gradient = new Gradient();
		
		gradient.addPoint(new GradientPoint(Color.RED, 0));
		gradient.addPoint(new GradientPoint(Color.ORANGE, 0.1f));
		gradient.addPoint(new GradientPoint(Color.YELLOW, 0.2f));
		gradient.addPoint(new GradientPoint(Color.GREEN, 0.3f));
		gradient.addPoint(new GradientPoint(Color.BLUE, 0.4f));
		gradient.addPoint(new GradientPoint(Color.RED, 0.5f));
		gradient.addPoint(new GradientPoint(Color.ORANGE, 0.6f));
		gradient.addPoint(new GradientPoint(Color.YELLOW, 0.7f));
		gradient.addPoint(new GradientPoint(Color.GREEN, 0.8f));
		gradient.addPoint(new GradientPoint(Color.BLUE, 0.9f));
		gradient.addPoint(new GradientPoint(Color.RED, 1));

		//set the color offset to 0 to start
		colorOffset=0;
		colorCycle = new ColorCycle(0.005, this);
		
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void start() {
		frame = new JFrame(title);										//create the main window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			//exits the program when window is closed
		frame.setBackground(Color.BLACK);
		frame.addKeyListener(launcher.getKeyManager());					//add key manager to main window
		frame.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
		    	if(!autoResize) {		//prevents resize loop when using set resolutions
		    		resizeTimer.restart();
		    	}else {
		    		autoResize=false;	//when and auto resize causes this function, set autoResize to false to allow for manual resizing in the future
		    	}
		    }
		});
		
		resizeTimer = new Timer(300, new ActionListener() {
    	    public void actionPerformed(ActionEvent evt) {
    	        System.out.println("resize");
 		       	manualResizeGraph();

    	    }
    	});
    	resizeTimer.setRepeats(false);
		
		
		menuBar = new JMenuBar();						//create menu bar at the top of the window
		frame.setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");						//add file menu to the menu bar
		menuBar.add(mnFile);
		
		JFileChooser saveLocChooser = new JFileChooser();											//creates file chooser for exporting images
		saveLocChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);					//allows user to view both files and directories when browsing
		saveLocChooser.setDialogTitle("Save Location");												//set title of the bowser window
		saveLocChooser.setApproveButtonText("Select");												//set text on approve button
		FileNameExtensionFilter filter = new FileNameExtensionFilter ("PNG Images", "png");			//create a file filter to only allow the user to see png files
		saveLocChooser.setFileFilter(filter);														//apply that filter
		saveLocChooser.setSelectedFile(new File("image"));											//set the suggested file name 
		
		setSaveLocationBtn = new JMenuItem("Set Save Location");			//create button in the file menu to set the save location
		setSaveLocationBtn.setToolTipText("Set the location and file name that images are saved to");
		setSaveLocationBtn.addActionListener(new ActionListener() {			//what happens when the button is pressed
			public void actionPerformed(ActionEvent e) {
				if(saveLocChooser.showOpenDialog(frame)==0) {				//open the file browser window, if it exits with a selected path continue
					userSaveFile = saveLocChooser.getSelectedFile();		//set the userSaveFile to whatever the browser was, the userSaveFile does not have the numbers in the title
					imageIndex = 0;																				//set the image index back to 0
					setSaveFile();
					exportImageBtn.setEnabled(true);															//enable the export image button to be pressed
					System.out.println("save path: " + saveFile.getAbsolutePath());
					System.out.println("parent path: "+ saveFile.getParent());
				}
			}
		});
		mnFile.add(setSaveLocationBtn);			//add the button to the file menu
		exportImageBtn = new JMenuItem("Export Image");						//button to export the current buffered image 
		exportImageBtn.setToolTipText("Save the current graph as an image in the specified file location");
		exportImageBtn.setEnabled(false);
		exportImageBtn.setFocusable(false);
		mnFile.add(exportImageBtn);
		exportImageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportImage();
			}
		});
		
		JFileChooser importLocChooser = new JFileChooser();
		importLocChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);					//allows user to view both files and directories when browsing
		importLocChooser.setDialogTitle("Import Image");												//set title of the bowser window
		importLocChooser.setApproveButtonText("Open");												//set text on approve button
		importLocChooser.setFileFilter(filter);
		
		mntmImportImage = new JMenuItem("Import Image");
		mntmImportImage.setToolTipText("Set the graph settings to match settings of the imported image.");
		mntmImportImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(importLocChooser.showOpenDialog(frame)==0) {				//open the file browser window, if it exits with a selected path continue
					importImageFile = importLocChooser.getSelectedFile();
					importImage();
				}
			}
		});
		mnFile.add(mntmImportImage);
		
		mnView = new JMenu("View");
		menuBar.add(mnView);
		
		lowPrecisionBtn = new JMenuItem("Low Precision");							//button to enable low precision mode
		lowPrecisionBtn.setToolTipText("Use 64 bit floating point numbers to calculate values, provides quick calculations. Good until 10^14");
		lowPrecisionBtn.setFocusable(false);
		lowPrecisionBtn.setEnabled(false);
		lowPrecisionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lowPrecisionBtn.setEnabled(false);
				infinitePrecisionBtn.setEnabled(true);
				highPrecisionBtn.setEnabled(true);
				
				launcher.setPrecision(Launcher.Precision.LOW_PRECISION);
				launcher.refresh();
			}
		});
		mnView.add(lowPrecisionBtn);
		
		highPrecisionBtn = new JMenuItem("High Precision");
		mnView.add(highPrecisionBtn);
		highPrecisionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				highPrecisionBtn.setEnabled(false);
				lowPrecisionBtn.setEnabled(true);
				infinitePrecisionBtn.setEnabled(true);
				
				launcher.setPrecision(Launcher.Precision.HIGH_PRECISION);
				launcher.refresh();
			}
		});
		
		
		infinitePrecisionBtn = new JMenuItem("Infinite Precision");				//button to enable infinite precision mode
		infinitePrecisionBtn.setToolTipText("Use Java's BigDecimal class in order to generate a graph with much more precice values, allows for a larger scale factor but icreases computation time significantly");
		infinitePrecisionBtn.setFocusable(false);
		mnView.add(infinitePrecisionBtn);
		infinitePrecisionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				infinitePrecisionBtn.setEnabled(false);
				lowPrecisionBtn.setEnabled(true);
				highPrecisionBtn.setEnabled(true);
				
				launcher.setPrecision(Launcher.Precision.INFINITE_PRECISION);
				launcher.refresh();
			}
		});

		
		mnReset = new JMenu("Reset");
		mnView.add(mnReset);
		
		mntmResetScaleOnly = new JMenuItem("Reset scale only");
		mntmResetScaleOnly.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.setScale(1);
				launcher.refresh();
			}
		});
		mnReset.add(mntmResetScaleOnly);
		
		mntmResetPositionOnly = new JMenuItem("Reset position only");
		mntmResetPositionOnly.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (launcher.precision) {
				case LOW_PRECISION:
					launcher.setCenter(new Complex(0,0));
					break;
				case HIGH_PRECISION:
					break;
				case INFINITE_PRECISION:
					launcher.setCenterIP(new BigComplex(BigDecimal.ZERO,BigDecimal.ZERO));
					break;
				}
				launcher.refresh();
			}
		});
		mnReset.add(mntmResetPositionOnly);
		
		mntmResetLimitOnly = new JMenuItem("Reset limit only");
		mntmResetLimitOnly.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.setLimit(150);
				launcher.refresh();
			}
		});
		mnReset.add(mntmResetLimitOnly);
		
		mntmResetAll = new JMenuItem("Reset all");
		mntmResetAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.setScale(1);
				switch (launcher.precision) {
				case LOW_PRECISION:
					launcher.setCenter(new Complex(0,0));
					break;
				case HIGH_PRECISION:
					break;
				case INFINITE_PRECISION:
					launcher.setCenterIP(new BigComplex(BigDecimal.ZERO,BigDecimal.ZERO));
					break;
				}
				launcher.setLimit(150);
				launcher.refresh();
			}
		});
		mnReset.add(mntmResetAll);
		
		mnSetResolution = new JMenu("Set Resolution");
		mnView.add(mnSetResolution);
		mnSetResolution.setToolTipText("Set the resolution of the graph.");
		
		mntmx = new JMenuItem("256 x 144");
		mntmx.setToolTipText("114p");
		mnSetResolution.add(mntmx);
		mntmx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resizeGraph(256,144);
			}
		});
		
		
		mntmx_1 = new JMenuItem("426 x 240");
		mntmx_1.setToolTipText("240p");
		mnSetResolution.add(mntmx_1);
		mntmx_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resizeGraph(426,240);
			}
		});
		
		mntmx_2 = new JMenuItem("640 x 360");
		mntmx_2.setToolTipText("360p");
		mnSetResolution.add(mntmx_2);
		mntmx_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resizeGraph(640,360);
			}
		});
		
		mntmx_3 = new JMenuItem("854 x 480");
		mntmx_3.setToolTipText("480p");
		mnSetResolution.add(mntmx_3);
		mntmx_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resizeGraph(854,480);
			}
		});
		
		mntmx_4 = new JMenuItem("1280 x 720");
		mntmx_4.setToolTipText("720p");
		mnSetResolution.add(mntmx_4);
		mntmx_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resizeGraph(1280,720);
			}
		});
		
		mntmx_5 = new JMenuItem("1920 x 1080");
		mntmx_5.setToolTipText("1080p");
		mnSetResolution.add(mntmx_5);
		mntmx_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resizeGraph(1920,1080);
			}
		});
		
		mntmx_6 = new JMenuItem("2560 x 1440");
		mntmx_6.setToolTipText("1440p");
		mnSetResolution.add(mntmx_6);
		mntmx_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resizeGraph(2560,1440);
			}
		});
		
		mntmx_7 = new JMenuItem("3840 x 2160");
		mntmx_7.setToolTipText("4k");
		mnSetResolution.add(mntmx_7);
		mntmx_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resizeGraph(3840,2160);
			}
		});
		
		mnColorCycling = new JMenu("Color cycling");
		mnColorCycling.setToolTipText("Cycle the color gradient to create animation");
		mnView.add(mnColorCycling);
		
		mntmStart = new JMenuItem("Start");
		mntmStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorCycle.startCycle();
				mntmStart.setEnabled(false);
				mntmStop.setEnabled(true);
			}
		});
		mnColorCycling.add(mntmStart);
		
		mntmStop = new JMenuItem("Stop");
		mntmStop.setEnabled(false);
		mntmStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorCycle.stopCycle();
				mntmStop.setEnabled(false);
				mntmStart.setEnabled(true);
			}
		});
		mnColorCycling.add(mntmStop);
		
		mnColoringMode = new JMenu("Coloring Mode");
		mnView.add(mnColoringMode);
		
		mntmAbsoulteColoring = new JMenuItem("Absolute coloring");
		mntmAbsoulteColoring.setEnabled(false);
		mntmAbsoulteColoring.setToolTipText("Colors each point based only on the number of iterations. Colors will remain the same as limit changes");
		mnColoringMode.add(mntmAbsoulteColoring);
		mntmAbsoulteColoring.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				absoluteColorMode=true;
				mntmRelativeColoring.setEnabled(true);
				mntmAbsoulteColoring.setEnabled(false);
				repaint();
			}
		});
		
		mntmRelativeColoring = new JMenuItem("Relative coloring ");
		mntmRelativeColoring.setEnabled(true);
		mntmRelativeColoring.setToolTipText("Colors each point based on how close it got to reaching the limit."
				+ " Therefore colors will change as the limit changes.");
		mnColoringMode.add(mntmRelativeColoring);
		mntmRelativeColoring.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				absoluteColorMode=false;
				mntmAbsoulteColoring.setEnabled(true);
				mntmRelativeColoring.setEnabled(false);
				repaint();
			}
		});
		
		
		
		
		
		topBar = new JPanel();										//create top  bar
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{79, 68, 157, 47, 68, 0};
		gbl_panel.rowHeights = new int[]{0, 21, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		topBar.setLayout(gbl_panel);
		frame.getContentPane().add(topBar, BorderLayout.NORTH);
		
		scaleDisplay = new JLabel();											//create JLabel for displaying the current scale value
		scaleDisplay.setText("Scale = "+launcher.getScale());					//set the text for the scale
		scaleDisplay.setToolTipText("The current scaling factor of the graph");
		scaleDisplay.setFont(font);
		GridBagConstraints gbc_scale = new GridBagConstraints();
		gbc_scale.anchor = GridBagConstraints.WEST;
		gbc_scale.insets = new Insets(0, 0, 5, 5);
		gbc_scale.gridx = 0;
		gbc_scale.gridy = 0;
		topBar.add(scaleDisplay, gbc_scale);
		
		centerXDisplay = new JLabel("X = " + Launcher.center.getReal());		//create JLabel for displaying the current x value
		centerXDisplay.setFont(font);
		GridBagConstraints gbc_centerXDisplay = new GridBagConstraints();
		gbc_centerXDisplay.anchor = GridBagConstraints.WEST;
		gbc_centerXDisplay.insets = new Insets(0, 0, 5, 5);
		gbc_centerXDisplay.gridx = 1;
		gbc_centerXDisplay.gridy = 0;
		centerXDisplay.setToolTipText("Center X value");
		topBar.add(centerXDisplay, gbc_centerXDisplay);	
		
		centerYDisplay = new JLabel("Y = " + Launcher.center.getImag());			//JLabel to display the center y value
		centerYDisplay.setFont(font);
		GridBagConstraints gbc_centerYDisplay = new GridBagConstraints();
		gbc_centerYDisplay.anchor = GridBagConstraints.WEST;
		gbc_centerYDisplay.insets = new Insets(0, 0, 0, 5);
		gbc_centerYDisplay.gridx = 1;
		gbc_centerYDisplay.gridy = 1;
		centerYDisplay.setToolTipText("Center Y value");
		topBar.add(centerYDisplay, gbc_centerYDisplay);
		
		limitDisplay = new JLabel("Limit = " + launcher.getLimit());				//JLabel to display the current limit value
		limitDisplay.setFont(font);
		limitDisplay.setToolTipText("The current maximum number of iterations allowed");
		GridBagConstraints gbc_limitDisplay= new GridBagConstraints();
		gbc_limitDisplay.anchor = GridBagConstraints.WEST;
		gbc_limitDisplay.insets = new Insets(0, 0, 0, 5);
		gbc_limitDisplay.gridx = 0;
		gbc_limitDisplay.gridy = 1;
		topBar.add(limitDisplay, gbc_limitDisplay);
		
		progressBar = new JProgressBar();				//progress bar to display 
		progressBar.setToolTipText("Computational progress, does not include drawing image to the screen");
		progressBar.setIndeterminate(false);
		progressBar.setStringPainted(true);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.anchor = GridBagConstraints.WEST;
		gbc_progressBar.insets = new Insets(0, 0, 0, 5);
		gbc_progressBar.fill = GridBagConstraints.VERTICAL;
		gbc_progressBar.gridx = 2;
		gbc_progressBar.gridy = 1;
		topBar.add(progressBar, gbc_progressBar);
		
		timeEstimateDisplay = new JLabel("");
		GridBagConstraints gbc_timeEstimateDisplay = new GridBagConstraints();
		gbc_timeEstimateDisplay.insets = new Insets(0, 0, 0, 5);
		gbc_timeEstimateDisplay.anchor = GridBagConstraints.WEST;
		gbc_timeEstimateDisplay.gridx = 3;
		gbc_timeEstimateDisplay.gridy = 1;
		topBar.add(timeEstimateDisplay, gbc_timeEstimateDisplay);
		timeEstimateDisplay.setToolTipText("Estimated amount of time remaining to finish the current calculation.");
		
		canvas = new Display(title,height,width,launcher);			//the canvas that will hold the buffered image
		canvas.setSize(width, height);		//set the canvas width and height
		canvas.setFocusable(false);
		canvas.addMouseListener(launcher.getMouseManager());
		canvas.setCursor(cursor);
		canvas.setBackground(Color.BLACK);
		frame.getContentPane().add(canvas);
		
		
		frame.pack();							//automatically set the width and height of the main frame window to fit everything
		frame.setLocationRelativeTo(null);		//center window
		frame.setVisible(true);					//make everything visible
		canvasG = canvas.getGraphics();			//save the canvas's graphics object for later use when redrawing the canvas
	}
	
	public void setColorOffset(double value) {
		if(value>1||value<0) {
			return;
		}
		colorOffset = value;
	}
	
	/**
	 * Update the display JLabels to reflect a change in the graph settings
	 */
	public void updateUI() {
		switch (launcher.precision) {
		case LOW_PRECISION:
			scaleDisplay.setText("Scale = " + launcher.getScale());
			limitDisplay.setText("Limit = " + launcher.getLimit());
			centerXDisplay.setText("X = "+Launcher.center.real);
			centerYDisplay.setText("Y = "+Launcher.center.imag);
			break;
		case HIGH_PRECISION:
			scaleDisplay.setText("Scale = " + Launcher.scaleHP);
			limitDisplay.setText("Limit = " + Launcher.limit);
			centerXDisplay.setText("X = "+Launcher.centerHP.real);
			centerYDisplay.setText("Y = "+Launcher.centerHP.imag);
			break;
		case INFINITE_PRECISION:
			scaleDisplay.setText("Scale = " + Launcher.scaleIP);
			limitDisplay.setText("Limit = " + Launcher.limit);
			centerXDisplay.setText("X = "+Launcher.centerIP.real);
			centerYDisplay.setText("Y = "+Launcher.centerIP.imag);
			break;
		}
		
		
	}
	/**
	 * Change the cursor that is displayed over the graph.
	 * @param wait a boolean, true shows Cursor.WAIT_CURSOR 
	 * while false shows Cursor.HAND_CURSOR.
	 */
	public void waitCursor(boolean wait) {
		if(wait) {
			cursor = new Cursor(Cursor.WAIT_CURSOR);
			canvas.setCursor(cursor);
		}else {
			cursor = new Cursor(Cursor.HAND_CURSOR);
			canvas.setCursor(cursor);
		}
	}
	
	/**
	 * Paint the canvas
	 */
	 public void paint(Graphics g) {
		 launcher.getDisplay().render(g);
		 
	 }
	 /**
	  * custom repaint method
	  */
	 public void repaint() {
		 if(canvasG==null) {
			 System.out.println("must call paint method before repaint");
			 return;
		 }
		 render(canvasG);
	 }

	/** Takes the current values in the resultsArray, converts the result to a
	 * 	color based on the number of iterations taken to reach the limit, then sets
	 * 	the pixel color at that location accordingly.
	 * 
	 * @param canvasG The graphics object used to draw to the canvas, comes from the display window.
	 */
	private void render(Graphics frameG) {				//this render method is called from the display class after the canvas is visible, or when repainting the canvas
		for (int x=0;x<Launcher.width;x++) {					//loop through all x and y values
			for (int y=0;y<Launcher.height;y++) {
	    		   
	    		   double result = Launcher.resultsArray[x][y];				//get the result of the complex number at each pixel location
	    		   if (result==0) {			//is in set
		    		   Launcher.buffImag.setRGB(x, y, Color.BLACK.getRGB());
	    		   }else if(absoluteColorMode) { 
		    		  		//set the color by creating a HSB color, set the hue to be the result(number of iterations to reach threshold) divided by 100, 
	    			  		//set the pixel color of the buffered image accordingly
	    			   Launcher.buffImag.setRGB(x, y, gradient.getColor((float)result/200 + colorOffset).getRGB());		//set the pixel color of the buffered image accordingly
	    		   }else {
			    			 //set the color by creating a HSB color, set the hue to be the result(number of iterations to reach threshold) divided by the limit (max number of iterations), 
		   			  		//set the pixel color of the buffered image accordingly
		   			   Launcher.buffImag.setRGB(x, y, gradient.getColor((float)result/Launcher.limit + colorOffset).getRGB());		//set the pixel color of the buffered image accordingly
	    		   }
	    	   }
	       }
		 frameG.drawImage(Launcher.buffImag, 0, 0, null);			//draw the buffered image to the canvas
	     
	       //TODO: create grid display
	       if(Launcher.enableGrid) {   
	       }
	}
	
	public void resizeGraph(int newX, int newY) {
		//clear graph
		autoResize = true;		//stop the resize action listener from taking action
		boolean resumeColorCycle = false;
		if(colorCycle.running) {
			colorCycle.stopCycle();		//stop color cycling
			resumeColorCycle = true;
		}
		
		for (int x=0;x<Launcher.width;x++) {					//loop through all x and y values
			for (int y=0;y<Launcher.height;y++) {
		    		   Launcher.resultsArray[x][y] = 0;
		   }								
		}
		
		Launcher.width = newX;
		Launcher.height = newY;
		Launcher.resultsArray = new double[newX][newY];
		Launcher.buffImag = new BufferedImage(newX, newY, BufferedImage.TYPE_INT_RGB);
		
		canvas.setSize(newX, newY);		
		//if shrinking, force frame to fit the canvas rather than the GUI
		if(frame.getWidth()>newX) {
			frame.setSize(newX+15, newY+topBar.getHeight()+menuBar.getHeight()+38);
		}else {
			frame.pack();
		}
		
		launcher.refresh();
		if(resumeColorCycle) {		//start color cycling again
			colorCycle.startCycle();
		}
		
	}
	
	private void manualResizeGraph () {
		boolean resumeColorCycle = false;
		if(colorCycle.running) {
			colorCycle.stopCycle();		//stop color cycling
			resumeColorCycle = true;
		}
		
		//clear graph
		for (int x=0;x<Launcher.width;x++) {					//loop through all x and y values
			for (int y=0;y<Launcher.height;y++) {
		    		   Launcher.resultsArray[x][y] = 0;
		   }								
		}
		
		int newX = canvas.getWidth();
		int newY = canvas.getHeight();
		Launcher.width = newX;
		Launcher.height = newY;
		Launcher.resultsArray = new double[newX][newY];
		Launcher.buffImag = new BufferedImage(newX, newY, BufferedImage.TYPE_INT_RGB);
		
		launcher.refresh();
		
		if(resumeColorCycle) {		//start color cycling again	
			colorCycle.startCycle();
		}
	}
	
	/**
	 * export the image to the specified location
	 */
	private void exportImage() {
		
		/*
		 * Metadata structure is as follows:
		 * Root(src)
		 * |
		 * ---->javax_imageio_png_1.0
		 * |
		 * .
		 * .
		 * .
		 * |
		 * ---->tEXt
		 * 		|
		 * 		--->tEXtEntry
		 * 		|	|
		 * 		|	--->keyword = "centerX" , "centerY", "scale", "limit", or "threshold"
		 * 		|	|
		 * 		|	--->value = some double converted to a string 
		 *		|
		 *		--->tEXtEntry
		 *		.	|
		 * 		.	--->keyword = "centerX" , "centerY", "scale", "limit", or "threshold"
		 * 		.	|
		 * 			--->value = some double converted to a string 
		 *			
		 * there is a text entry node for each value of the graph,
		 * create the tEXt node and all its children first, then merge it with the already existing (src) metadata 
		 */
		
		if(saveFile.exists()) {		//if that file already exists, increment the image index
			imageIndex++;		//increment index
			setSaveFile();		//update saveFile
			exportImage();		//try exporting again
			return;
		}
		try{
			ImageWriter writer = ImageIO.getImageWritersByFormatName("PNG").next();		//get the image writer for PNG images
			
			ImageOutputStream ios = ImageIO.createImageOutputStream(saveFile);			//create the output stream to the saveFile location
			writer.setOutput(ios);														//set the writer to use the previously created output stream
			
			ImageWriteParam param = writer.getDefaultWriteParam();						//get the default parameters for the writer
			ImageTypeSpecifier type = new ImageTypeSpecifier(Launcher.buffImag);		//get the image type from the buffered image
			
			IIOMetadata imgMetadata = writer.getDefaultImageMetadata(type, param);		//get the default metadata for that image type and writer parameters
			
			imgMetadata = upgradeMetadata(imgMetadata);									//upgrade the metadata to save the graph settings
			
			IIOImage iio_img = new IIOImage(Launcher.buffImag, null, imgMetadata);		//create an IIOImage which encapsulates the buffered image, any thumbnails (not being used), and the appropriate metadata
			writer.write(iio_img);														//save the IIOImage to disk
			ios.flush();																//clean up and close down the output stream
			ios.close();
					
			//update the saveFile for the next image to export
			imageIndex++;		//increment index
			setSaveFile();		//update saveFile	
		} 
		catch (IOException e){
			System.out.println("Error, image failed to export: " + e);
			JOptionPane.showMessageDialog(null, "Error, image failed to export: " + e);

		} 
		
	}
	
	/**
	 * set the save file location, based on the userSaveFile and the imageIndex.
	 */
	private void setSaveFile() {
		if(OS.equals("Linux")) {
			saveFile = new File(userSaveFile.getParent() + "/" + userSaveFile.getName() + "_" + String.format("%03d", imageIndex) + ".png");
		} else if(OS.equals("Windows 10")) {
			saveFile = new File(userSaveFile.getParent() + "\\" + userSaveFile.getName() + "_" + String.format("%03d", imageIndex) + ".png");
		}else {
			System.out.println("Unsupported OS: " + OS);
			JOptionPane.showMessageDialog(null, "Error, OS(" + OS + ") not supported for file exportion.");
		}
	}
	
	/**
	 * Upgrade the provided meta data to include the current graph settings
	 * as text entries nodes under the text node.
	 * @param src The root metadata
	 * @return The upgraded metadata
	 */
	private IIOMetadata upgradeMetadata(IIOMetadata src) {		
		String format = src.getNativeMetadataFormatName();				//get the format of the source metadata
		System.out.println("Native format: " + format);	
        Node root = src.getAsTree(format);								//get the root node of the source metadata

        // add node
        Node n = lookupChildNode(root, "tEXt");							//look to see if the tEXt node already exists
        if (n == null) {												//if not create it
        	System.out.println("Appending new node...");
            Node textNode = setTextNode();								//create the text node and add the graph data to its children
            root.appendChild(textNode);									//append the text node to the root node
        }

        System.out.println("Upgraded metadata tree:");

        System.out.println("Merging metadata...");
        try {     	
            src.mergeTree(format, root);								//merge the original metadata with the updated metadata
        } catch (IIOInvalidTreeException e) {
            JOptionPane.showMessageDialog(null, "Error, could not merge previous metadata tree with updated tree: "+ e);
        }
        return src;														//return the updated metadata
    }
	
	/**
	 * Look to see if a certain node exists in a metadata tree.
	 * @param root	The root location to start looking.
	 * @param name	The name of the node.
	 * @return The found node, null if node not found.
	 */
	private Node lookupChildNode(Node root, String name) {
		Node n = root.getFirstChild();								//get the first child of the root node
		while (n != null && !name.equals(n.getNodeName())) {		//if this child exists and is not the node we are looking for: 
			n = n.getNextSibling();									//get the next child and run the test again
        }
        return n;													//return the found node
    }
	
	/**
	 * Sets the "tEXt" node of the metadata to reflect the graph's current
	 * settings. Each value is saved as a "tEXtEntry" node with a corresponding
	 * keyword and value.
	 * @return The updated text node.
	 */
	private IIOMetadataNode setTextNode() {
		//create text node (based on the required PNG metadata format)
		IIOMetadataNode textNode = new IIOMetadataNode("tEXt");
		
		//create multiple text entry nodes named "tEXtEntry", each node holds a keyword (name) and a value
		IIOMetadataNode centerXNode = new IIOMetadataNode("tEXtEntry");
		centerXNode.setAttribute("keyword", "centerX");
		centerXNode.setAttribute("value", Double.toString(Launcher.center.getReal()));
		
		IIOMetadataNode centerYNode = new IIOMetadataNode("tEXtEntry");
		centerYNode.setAttribute("keyword", "centerY");
    	centerYNode.setAttribute("value", Double.toString(Launcher.center.getImag()));
    	
		IIOMetadataNode scaleNode = new IIOMetadataNode("tEXtEntry");
		scaleNode.setAttribute("keyword", "scale");
		scaleNode.setAttribute("value", Double.toString(Launcher.scale));
		
		IIOMetadataNode limitNode = new IIOMetadataNode("tEXtEntry");
		limitNode.setAttribute("keyword", "limit");
		limitNode.setAttribute("value", Double.toString(Launcher.limit));
		
		IIOMetadataNode thresholdNode = new IIOMetadataNode("tEXtEntry");		
		thresholdNode.setAttribute("keyword", "threshold");
		thresholdNode.setAttribute("value", Double.toString(Launcher.threshold));
		        
        //append the all the data nodes to the textNode
        textNode.appendChild(centerXNode);
        textNode.appendChild(centerYNode);
        textNode.appendChild(scaleNode);
        textNode.appendChild(limitNode);
        textNode.appendChild(thresholdNode);
        
        //return the now updated text node
        return textNode;
    }
	
	/**
	 * Read the importImageFile's metadata and set the current graph to reflect
	 * those settings.
	 */
	private void importImage() {
		Double tempX=null;			//temporary center x and y values
		Double tempY=null;
		
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(importImageFile);			//image input stream to read the file
			Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);				//get a list of readers from the input stream
			ImageReader reader = imageReaders.next();										//get the first reader from the stream
			reader.setInput(iis);															//set the input of the reader to the input stream
			IIOMetadata metadata = reader.getImageMetadata(0);								//get the metadata of the image from the reader
			Node root = metadata.getAsTree(metadata.getNativeMetadataFormatName());			//the the metadata in node format 
			Node textNode = lookupChildNode(root, "tEXt");									//the the text node of the metadata
			IIOMetadataNode n = (IIOMetadataNode) textNode.getFirstChild();					//get the first child of the text node
			while(n!=null) {																//while there the child exists:
				String keyword = n.getAttribute("keyword");									//get the value of the keyword attribute
				double value = Double.parseDouble(n.getAttribute("value"));					//get the value of the value attribute
				System.out.println(keyword +"= "+value);
				
				switch (keyword) {															//set the current graph settings based on the node's keyword and value
				case "centerX":
					if(tempY!=null) {
						launcher.setCenter(new Complex(value,tempY));
					}else {tempX=value;}
					break;
				case "centerY":
					if(tempX!=null) {
						launcher.setCenter(new Complex(tempX,value));
					}else {tempY=value;}
					break;
				case "scale":
					launcher.setScale(value);
					break;
				case "limit":
					launcher.setLimit((int)value);
					break;
				case "threshold":
					//TODO: create, setThreshold method in launcher class
					break;
				default:
					System.out.println("ERROR: unknown attribute: "+keyword+"= "+value);
					JOptionPane.showMessageDialog(null, "Error importing image, unknown attribute: "+ keyword + "= "+value );
				}
				n= (IIOMetadataNode) n.getNextSibling();								//get the next sibling under the text node
			}
			iis.flush();				//clean up the input stream
			iis.close();
			launcher.refresh();			//refresh/update the graph
			
		} catch (IOException e) {
			System.out.println("ERROR: import image could not be found" + e);
			JOptionPane.showMessageDialog(null, "Error, import image could not be found" +e);
		}
	}
}