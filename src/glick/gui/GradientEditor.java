package glick.gui;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class GradientEditor extends JPanel implements ChangeListener {
	private static final long serialVersionUID = -7870220765634171353L;

	JFrame frame;

	/* ColorChooserDemo.java requires no other files. */
	protected JColorChooser tcc;

	public GradientEditor() {
		super(new BorderLayout());

		// Set up color chooser for setting text color
		tcc = new JColorChooser(Color.black);
		tcc.getSelectionModel().addChangeListener(this);
		tcc.setBorder(BorderFactory.createTitledBorder("Choose Text Color"));

		add(tcc, BorderLayout.PAGE_END);
	}

	public void stateChanged(ChangeEvent e) {
		Color newColor = tcc.getColor();
		System.out.println(newColor);
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event-dispatching thread.
	 */
	public void createAndShowGUI() {
		// Create and set up the window.
		frame = new JFrame("Gradient Editor");

		// Create and set up the content pane.
		JComponent newContentPane = new GradientEditor();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		// frame.pack();
		frame.setVisible(true);
	}

	public void show() {
		frame.setVisible(true);
	}

	public void hide() {
		frame.setVisible(false);
	}
}
