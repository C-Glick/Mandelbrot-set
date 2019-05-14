import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
//this class handles the display window that the game runs it
public class Display extends Canvas{
	String title;
	int width; 
	int height;
	Launcher launcher;
	JFrame frame;
	Graphics g;
	
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
	
 
	
	Display(String title,int width, int height, Launcher launcher){
		this.title = title;
		this.height = height;
		this.width = width;
		this.launcher = launcher;
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
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
		
		JFileChooser saveLocChooser = new JFileChooser();
		saveLocChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		saveLocChooser.setDialogTitle("Save Location");
		saveLocChooser.setApproveButtonText("Select");
		FileNameExtensionFilter filter = new FileNameExtensionFilter ("PNG Images", "png");
		saveLocChooser.setFileFilter(filter);
		saveLocChooser.setSelectedFile(new File("image.png"));

		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		setSaveLocationBtn = new JMenuItem("Set Save Location");
		setSaveLocationBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(saveLocChooser.showSaveDialog(frame)==0) {
					userSaveFile = saveLocChooser.getSelectedFile();
					saveFile = new File(userSaveFile.getParent() + "\\000_" + userSaveFile.getName());
					imageIndex = 0;
					exportImageBtn.setEnabled(true);
					System.out.println("save path: " + saveFile.getAbsolutePath());
					System.out.println("parent path: "+ saveFile.getParent());
				}
			}
		});
		mnFile.add(setSaveLocationBtn);
		
		topBar = new JPanel();										//create top  bar
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{120, 71, 65, 107, 103, 0};
		gbl_panel.rowHeights = new int[]{0, 21, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
		centerXDisplay.setFont(font);
		GridBagConstraints gbc_centerXDisplay = new GridBagConstraints();
		gbc_centerXDisplay.insets = new Insets(0, 0, 5, 5);
		gbc_centerXDisplay.gridx = 1;
		gbc_centerXDisplay.gridy = 0;
		centerXDisplay.setToolTipText("Center X value");
		topBar.add(centerXDisplay, gbc_centerXDisplay);
			
		
		centerYDisplay = new JLabel("Y = " + Launcher.center.getImag());
		centerYDisplay.setFont(font);
		GridBagConstraints gbc_centerYDisplay = new GridBagConstraints();
		gbc_centerYDisplay.insets = new Insets(0, 0, 0, 5);
		gbc_centerYDisplay.gridx = 1;
		gbc_centerYDisplay.gridy = 1;
		centerYDisplay.setToolTipText("Center Y value");
		topBar.add(centerYDisplay, gbc_centerYDisplay);
		
		limitDisplay = new JLabel("Limit = " + launcher.getLimit());
		limitDisplay.setFont(font);
		GridBagConstraints gbc_limitDisplay= new GridBagConstraints();
		gbc_limitDisplay.anchor = GridBagConstraints.WEST;
		gbc_limitDisplay.insets = new Insets(0, 0, 0, 5);
		gbc_limitDisplay.gridx = 0;
		gbc_limitDisplay.gridy = 1;
		topBar.add(limitDisplay, gbc_limitDisplay);
		
		resetButton = new JButton("Reset");
		resetButton.setFocusable(false);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				launcher.setHighPrecision(false);
				launcher.setCenter(new Complex(0,0));
				launcher.setScale(1);
				launcher.setLimit(150);
				launcher.refresh();
			}
		});
		GridBagConstraints gbc_resetButton = new GridBagConstraints();
		gbc_resetButton.insets = new Insets(0, 0, 0, 5);
		gbc_resetButton.gridx = 2;
		gbc_resetButton.gridy = 1;
		topBar.add(resetButton, gbc_resetButton);
		
		highPrecisionBtn = new JButton("High Precision");
		highPrecisionBtn.setFocusable(false);
		highPrecisionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lowPrecisionBtn.setEnabled(true);
				highPrecisionBtn.setEnabled(false);
				
				launcher.setHighPrecision(true);
				launcher.refresh();
			}
		});
		GridBagConstraints gbc_highPrecisionBtn = new GridBagConstraints();
		gbc_highPrecisionBtn.insets = new Insets(0, 0, 0, 5);
		gbc_highPrecisionBtn.gridx = 3;
		gbc_highPrecisionBtn.gridy = 1;
		topBar.add(highPrecisionBtn, gbc_highPrecisionBtn);
		
		lowPrecisionBtn = new JButton("Low Precision");
		lowPrecisionBtn.setFocusable(false);
		lowPrecisionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lowPrecisionBtn.setEnabled(false);
				highPrecisionBtn.setEnabled(true);
				
				launcher.setHighPrecision(false);
				launcher.refresh();
			}
		});
		GridBagConstraints gbc_lowPrecisionBtn = new GridBagConstraints();
		gbc_lowPrecisionBtn.insets = new Insets(0, 0, 5, 5);
		gbc_lowPrecisionBtn.gridx = 3;
		gbc_lowPrecisionBtn.gridy = 0;
		topBar.add(lowPrecisionBtn, gbc_lowPrecisionBtn);
		
		exportImageBtn = new JButton("Export Image");
		exportImageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportImage();
			}
		});
		GridBagConstraints gbc_btnExportImage = new GridBagConstraints();
		gbc_btnExportImage.anchor = GridBagConstraints.WEST;
		gbc_btnExportImage.insets = new Insets(0, 0, 5, 0);
		gbc_btnExportImage.gridx = 4;
		gbc_btnExportImage.gridy = 0;
		exportImageBtn.setEnabled(false);
		exportImageBtn.setFocusable(false);
		topBar.add(exportImageBtn, gbc_btnExportImage);

		progressBar = new JProgressBar();
		progressBar.setIndeterminate(false);
		progressBar.setStringPainted(true);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.VERTICAL;
		gbc_progressBar.anchor = GridBagConstraints.WEST;
		gbc_progressBar.gridx = 4;
		gbc_progressBar.gridy = 1;
		topBar.add(progressBar, gbc_progressBar);
		
		
		Canvas canvas = new Display(title,height,width,launcher);
		BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		
		canvas.setSize(width, height);
		canvas.setFocusable(false);
		canvas.addMouseListener(launcher.getMouseManager());
		frame.getContentPane().add(canvas);
		
		
		//frame.pack();
		frame.setVisible(true);
		g = canvas.getGraphics();
	}
	
	public void updateUI() {
		scaleDisplay.setText("Scale = " + launcher.getScale());
		limitDisplay.setText("Limit = " + launcher.getLimit());
		centerXDisplay.setText("X = "+launcher.center.real);
		centerYDisplay.setText("Y = "+launcher.center.imag);
		
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
	public void render(Graphics frameG) {				//this render method is called from the display class after the canvas is visible, or when repainting the canvas
		for (int x=0;x<Launcher.width;x++) {					//loop through all x and y values
			for (int y=0;y<Launcher.height;y++) {
	    		   
	    		   int result = Launcher.resultsArray[x][y];				//get the result of the complex number at each pixel location
	    		   if (result==0) {			//is in set
		    		   Launcher.buffImag.setRGB(x, y, Color.BLACK.getRGB());
	    		   }else { 
		    		   //buffImagG.setColor(Color.getHSBColor(((float)result/Launcher.limit), 1, 1));			//set the color with by creating a HSB color, set the hue to be the result(number of iterations to reach threshold) divided by the limit (max number of iterations), 
	    			   Launcher.buffImag.setRGB(x, y, Color.getHSBColor((float)result/Launcher.limit, 1, 1).getRGB());
	    		   }								
	    	   }
	       }
		 frameG.drawImage(Launcher.buffImag, 0, 0, null);
	     
	       //TODO: create grid display
	       if(Launcher.enableGrid) {   
	       }
	}
	
	private void exportImage() {
		if(saveFile.exists()) {
			imageIndex++;
			 if (imageIndex<10) {	//images 0-9
		    	   saveFile = new File(userSaveFile.getParent() + "\\00" + imageIndex + "_" + userSaveFile.getName());
		       }else if(imageIndex>=10 && imageIndex<100) {  //images 10-99
		    	   saveFile = new File(userSaveFile.getParent() + "\\0" + imageIndex + "_" + userSaveFile.getName());
		       }else if(imageIndex>=100 && imageIndex<1000) {	//images 100-999
		    	   saveFile = new File(userSaveFile.getParent() + "\\" + imageIndex + "_" + userSaveFile.getName());
		       }
			exportImage();
			return;
		}
		
	       try{
	    	   ImageIO.write(Launcher.buffImag, "png", saveFile); 
	       } 
	       catch (IOException e){
	    	   System.out.println(e);
	    	   
	       }       
	       
	       imageIndex++;
	       if (imageIndex<10) {	//images 0-9
	    	   saveFile = new File(userSaveFile.getParent() + "\\00" + imageIndex + "_" + userSaveFile.getName());
	       }else if(imageIndex>=10 && imageIndex<100) {  //images 10-99
	    	   saveFile = new File(userSaveFile.getParent() + "\\0" + imageIndex + "_" + userSaveFile.getName());
	       }else if(imageIndex>=100 && imageIndex<1000) {	//images 100-999
	    	   saveFile = new File(userSaveFile.getParent() + "\\" + imageIndex + "_" + userSaveFile.getName());
	       }
	}
}