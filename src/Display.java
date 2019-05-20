import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Choice;
//this class handles the display window that the game runs it
public class Display extends Canvas{
	String title;
	int width; 
	int height;
	Launcher launcher;
	JFrame frame;
	Graphics canvasG;
	
	String OS = System.getProperty("os.name");
	File userSaveFile;		//the file location with no numbering
	File saveFile;			//the actual file output with numbering
	int imageIndex=0;
	
	JPanel topBar;
	JLabel scaleDisplay;
	JLabel limitDisplay;
	JLabel centerXDisplay;
	JLabel centerYDisplay;
	JProgressBar progressBar;
	
	Font font = new Font("Arial Unicode MS", Font.PLAIN, 14);
	private JButton resetButton;
	private JButton highPrecisionBtn;
	private JButton lowPrecisionBtn;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem setSaveLocationBtn;
	private JButton exportImageBtn;
	private Choice resetChoice;
	JLabel timeEstimateDisplay;
	
 
	
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
		}
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void start() {
		frame = new JFrame(title);										//create the main window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			//exits the program when window is closed
		frame.addKeyListener(launcher.getKeyManager());					//add key manager to main window
		
		JFileChooser saveLocChooser = new JFileChooser();											//creates file chooser for exporting images
		saveLocChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);					//allows user to view both files and directories when browsing
		saveLocChooser.setDialogTitle("Save Location");												//set title of the bowser window
		saveLocChooser.setApproveButtonText("Select");												//set text on approve button
		FileNameExtensionFilter filter = new FileNameExtensionFilter ("PNG Images", "png");			//create a file filter to only allow the user to see png files
		saveLocChooser.setFileFilter(filter);														//apply that filter
		saveLocChooser.setSelectedFile(new File("image"));											//set the suggested file name 

		menuBar = new JMenuBar();						//create menu bar at the top of the window
		frame.setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");						//add file menu to the menu bar
		menuBar.add(mnFile);
		
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
		
		topBar = new JPanel();										//create top  bar
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{120, 71, 65, 107, 103, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 21, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
		gbc_centerXDisplay.insets = new Insets(0, 0, 5, 5);
		gbc_centerXDisplay.gridx = 1;
		gbc_centerXDisplay.gridy = 0;
		centerXDisplay.setToolTipText("Center X value");
		topBar.add(centerXDisplay, gbc_centerXDisplay);
		
		resetChoice = new Choice();												//choice drop down menu to select the resetMode
		GridBagConstraints gbc_choice = new GridBagConstraints();
		gbc_choice.insets = new Insets(0, 0, 0, 5);
		gbc_choice.gridx = 2;
		gbc_choice.gridy = 1;
		topBar.add(resetChoice, gbc_choice);
		resetChoice.add("Reset Scale Only");			//add all the choices to the resetChoice menu
		resetChoice.add("Reset Limit Only");
		resetChoice.add("Reset Position Only");
		resetChoice.add("Reset All");
		resetChoice.setFocusable(false);
			
		resetButton = new JButton("Reset");				//reset button, Reset some, or all settings of the graph, depends on the reset mode selected
		resetButton.setFocusable(false);
		resetButton.setToolTipText("Reset some, or all settings of the graph, depends on the reset mode selected");
		GridBagConstraints gbc_resetButton = new GridBagConstraints();
		gbc_resetButton.insets = new Insets(0, 0, 5, 5);
		gbc_resetButton.gridx = 2;
		gbc_resetButton.gridy = 0;
		topBar.add(resetButton, gbc_resetButton);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String resetMode = resetChoice.getSelectedItem();
				
				switch (resetMode) {				
					case "Reset Scale Only":
						launcher.setScale(1);
						break;
					case "Reset Limit Only":
						launcher.setLimit(150);
						break;
					case "Reset Position Only":
						launcher.setCenter(new Complex(0,0));
						break;
					case "Reset All":
						launcher.setCenter(new Complex(0,0));
						launcher.setScale(1);
						launcher.setLimit(150);
						break;
					default:
						System.out.println("RESET ERROR");
				}				
				launcher.refresh();
			}
		});
		
		centerYDisplay = new JLabel("Y = " + Launcher.center.getImag());			//JLabel to display the center y value
		centerYDisplay.setFont(font);
		GridBagConstraints gbc_centerYDisplay = new GridBagConstraints();
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
		
		highPrecisionBtn = new JButton("High Precision");				//button to enable high precision mode
		highPrecisionBtn.setToolTipText("Use Java's BigDecimal class in order to generate a graph with much more precice values, allows for a larger scale factor but icreases computation time significantly");
		highPrecisionBtn.setFocusable(false);
		GridBagConstraints gbc_highPrecisionBtn = new GridBagConstraints();
		gbc_highPrecisionBtn.insets = new Insets(0, 0, 0, 5);
		gbc_highPrecisionBtn.gridx = 3;
		gbc_highPrecisionBtn.gridy = 1;
		topBar.add(highPrecisionBtn, gbc_highPrecisionBtn);
		highPrecisionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lowPrecisionBtn.setEnabled(true);
				highPrecisionBtn.setEnabled(false);
				
				launcher.setHighPrecision(true);
				launcher.refresh();
			}
		});
		
		lowPrecisionBtn = new JButton("Low Precision");							//button to enable low precision mode
		lowPrecisionBtn.setToolTipText("Use 64 bit floating point numbers to calculate values, provides quick calculations. Good until 10^14");
		lowPrecisionBtn.setFocusable(false);
		lowPrecisionBtn.setEnabled(false);
		GridBagConstraints gbc_lowPrecisionBtn = new GridBagConstraints();
		gbc_lowPrecisionBtn.insets = new Insets(0, 0, 5, 5);
		gbc_lowPrecisionBtn.gridx = 3;
		gbc_lowPrecisionBtn.gridy = 0;
		topBar.add(lowPrecisionBtn, gbc_lowPrecisionBtn);
		lowPrecisionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lowPrecisionBtn.setEnabled(false);
				highPrecisionBtn.setEnabled(true);
				
				launcher.setHighPrecision(false);
				launcher.refresh();
			}
		});
		
		exportImageBtn = new JButton("Export Image");						//button to export the current buffered image 
		exportImageBtn.setToolTipText("Save the current graph as an image in the specified file location");
		GridBagConstraints gbc_btnExportImage = new GridBagConstraints();
		gbc_btnExportImage.anchor = GridBagConstraints.WEST;
		gbc_btnExportImage.insets = new Insets(0, 0, 5, 5);
		gbc_btnExportImage.gridx = 4;
		gbc_btnExportImage.gridy = 0;
		exportImageBtn.setEnabled(false);
		exportImageBtn.setFocusable(false);
		topBar.add(exportImageBtn, gbc_btnExportImage);
		exportImageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportImage();
			}
		});

		progressBar = new JProgressBar();				//progress bar to display 
		progressBar.setToolTipText("Computational progress, does not include drawing image to the screen");
		progressBar.setIndeterminate(false);
		progressBar.setStringPainted(true);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.insets = new Insets(0, 0, 0, 5);
		gbc_progressBar.fill = GridBagConstraints.VERTICAL;
		gbc_progressBar.anchor = GridBagConstraints.WEST;
		gbc_progressBar.gridx = 4;
		gbc_progressBar.gridy = 1;
		topBar.add(progressBar, gbc_progressBar);
		
		timeEstimateDisplay = new JLabel("");
		GridBagConstraints gbc_timeEstimateDisplay = new GridBagConstraints();
		gbc_timeEstimateDisplay.anchor = GridBagConstraints.WEST;
		gbc_timeEstimateDisplay.gridx = 5;
		gbc_timeEstimateDisplay.gridy = 1;
		topBar.add(timeEstimateDisplay, gbc_timeEstimateDisplay);
		timeEstimateDisplay.setToolTipText("Estimated amount of time remaining to finish the current calculation.");
		
		
		
		Canvas canvas = new Display(title,height,width,launcher);			//the canvas that will hold the buffered image
		
		canvas.setSize(width, height);		//set the canvas width and height
		canvas.setFocusable(false);
		canvas.addMouseListener(launcher.getMouseManager());
		frame.getContentPane().add(canvas);
		
		
		frame.pack();					//automatically set the width and height of the main frame window to fit everything
		frame.setVisible(true);			//make everything visible
		canvasG = canvas.getGraphics();		//save the canvas's graphics object for later use when redrawing the canvas
	}
	
	/**
	 * Update the display JLabels to reflect a change in the graph settings
	 */
	public void updateUI() {
		scaleDisplay.setText("Scale = " + launcher.getScale());
		limitDisplay.setText("Limit = " + launcher.getLimit());
		centerXDisplay.setText("X = "+launcher.center.real);
		centerYDisplay.setText("Y = "+launcher.center.imag);
		
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
	public void render(Graphics frameG) {				//this render method is called from the display class after the canvas is visible, or when repainting the canvas
		for (int x=0;x<Launcher.width;x++) {					//loop through all x and y values
			for (int y=0;y<Launcher.height;y++) {
	    		   
	    		   double result = Launcher.resultsArray[x][y];				//get the result of the complex number at each pixel location
	    		   if (result==0) {			//is in set
		    		   Launcher.buffImag.setRGB(x, y, Color.BLACK.getRGB());
	    		   }else { 
		    		  		//set the color by creating a HSB color, set the hue to be the result(number of iterations to reach threshold) divided by the limit (max number of iterations), 
	    			   Launcher.buffImag.setRGB(x, y, Color.getHSBColor((float)result/Launcher.limit, 1, 1).getRGB());		//set the pixel color of the buffered image accordingly
	    		   }								
	    	   }
	       }
		 frameG.drawImage(Launcher.buffImag, 0, 0, null);			//draw the buffered image to the canvas
	     
	       //TODO: create grid display
	       if(Launcher.enableGrid) {   
	       }
	}
	
	/**
	 * export the image to the specified location
	 */
	private void exportImage() {
		if(saveFile.exists()) {		//if that file already exists, increment the image index
			imageIndex++;		//increment index
			setSaveFile();		//update saveFile
			exportImage();		//try exporting again
			return;
		}
		try{
			ImageIO.write(Launcher.buffImag, "png", saveFile);		//save the image to the set location
		} 
		catch (IOException e){
			System.out.println(e);   
		} 
		//update the saveFile for the next image to export
		imageIndex++;		//increment index
		setSaveFile();		//update saveFile	
	}
	
	private void setSaveFile() {
		if(OS.equals("Linux")) {
			saveFile = new File(userSaveFile.getParent() + "/" + userSaveFile.getName() + "_" + String.format("%03d", imageIndex) + ".png");
		} else if(OS.equals("Windows 10")) {
			saveFile = new File(userSaveFile.getParent() + "\\" + userSaveFile.getName() + "_" + String.format("%03d", imageIndex) + ".png");
		}else {
			System.out.println("Unsupported OS: " + OS);
		}
	}
}