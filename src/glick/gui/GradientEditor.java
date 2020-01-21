package glick.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GradientEditor extends JPanel {
	/** The list of control points */
	// private ArrayList list = new ArrayList();

	private Gradient gradient;
	/** The current selected control point */
	private GradientPoint selected;
	/** The polygon used for the markers */
	private Polygon poly = new Polygon();
	/** A button to add a control point */
	private JButton add = new JButton("Add");
	/** A button to edit a control point */
	private JButton edit = new JButton("Edit");
	/** A button to delete a control point */
	private JButton del = new JButton("Del");
	/** A button to apply the current gradient to the graph */
	private JButton apply = new JButton("apply");

	/** The x position of the gradient bar */
	private int x;
	/** The y position of the gradient bar */
	private int y;
	/** The width of the gradient bar */
	private int width;
	/** The height of the gradient bar */
	private int barHeight;

	/** The listeners that should be notified of changes to this emitter */
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

	/**
	 * Create a new editor for gradients
	 *
	 */
	public GradientEditor(Gradient gradient) {
		this.gradient = gradient;
		setLayout(null);

		add.setBounds(20, 70, 75, 20);
		add(add);
		edit.setBounds(100, 70, 75, 20);
		add(edit);
		del.setBounds(180, 70, 75, 20);
		add(del);
		apply.setBounds(80, 101, 115, 20);
		add(apply);

		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addPoint();
			}
		});
		del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delPoint();
			}
		});
		edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editPoint();
			}
		});
		apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyGradient();
			}
		});

		if (gradient.listOfPoints.size() <= 0) {
			// add default points if there are none
			gradient.addPoint(new GradientPoint(Color.white, 0));
			gradient.addPoint(new GradientPoint(Color.black, 1));
		}

		poly.addPoint(0, 0);
		poly.addPoint(5, 10);
		poly.addPoint(-5, 10);

		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				selectPoint(e.getX(), e.getY());
				repaint(0);

				if (e.getClickCount() == 2) {
					editPoint();
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				movePoint(e.getX(), e.getY());
				repaint(0);
			}

			public void mouseMoved(MouseEvent e) {
			}
		});
	}

	/**
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		Component[] components = getComponents();
		for (int i = 0; i < components.length; i++) {
			components[i].setEnabled(enabled);
		}
	}

	/**
	 * Add a listener that will be notified on change of this editor
	 * 
	 * @param listener The listener to be notified on change of this editor
	 */
	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove a listener from this editor. It will no longer be notified
	 * 
	 * @param listener The listener to be removed
	 */
	public void removeActionListener(ActionListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Fire an update to all listeners
	 */
	private void fireUpdate() {
		ActionEvent event = new ActionEvent(this, 0, "");
		for (int i = 0; i < listeners.size(); i++) {
			((ActionListener) listeners.get(i)).actionPerformed(event);
		}
	}

	/**
	 * Check if there is a control point at the specified mouse location
	 * 
	 * @param mx The mouse x coordinate
	 * @param my The mouse y coordinate
	 * @param pt The point to check against
	 * @return True if the mouse point coincides with the control point
	 */
	private boolean checkPoint(int mx, int my, GradientPoint pt) {
		int dx = (int) Math.abs((10 + (width * pt.position)) - mx);
		int dy = Math.abs((y + barHeight + 7) - my);

		if ((dx < 5) && (dy < 7)) {
			return true;
		}

		return false;
	}

	/**
	 * Add a new gradient point
	 */
	private void addPoint() {
		GradientPoint point = new GradientPoint(Color.white, 0.5f);
		gradient.addPoint(point);
		selected = point;

		repaint(0);
		fireUpdate();
	}

	/**
	 * Add a control point to the gradient
	 * 
	 * @param pos The position in the gradient (0 -> 1)
	 * @param col The color at the new control point
	 */
	public void addPoint(float pos, Color col) {
		GradientPoint point = new GradientPoint(col, pos);
		gradient.addPoint(point);
		repaint(0);
	}

	/**
	 * Edit the currently selected control point
	 *
	 */
	private void editPoint() {
		if (selected == null) {
			return;
		}
		Color col = JColorChooser.showDialog(this, "Select Color", selected.getColor());
		if (col != null) {
			selected.setColor(col);
			repaint(0);
			fireUpdate();
		}
	}

	/**
	 * Select the control point at the specified mouse coordinate
	 * 
	 * @param mx The mouse x coordinate
	 * @param my The mouse y coordinate
	 */
	private void selectPoint(int mx, int my) {
		if (!isEnabled()) {
			return;
		}

		for (int i = 0; i < gradient.listOfPoints.size(); i++) {
			if (checkPoint(mx, my, gradient.listOfPoints.get(i))) {
				selected = gradient.listOfPoints.get(i);
				return;
			}
		}

		selected = null;
	}

	/**
	 * Delete the currently selected point
	 */
	private void delPoint() {
		if (!isEnabled()) {
			return;
		}

		if (selected == null) {
			return;
		}
		if (gradient.listOfPoints.indexOf(selected) == 0) {
			return;
		}
		if (gradient.listOfPoints.indexOf(selected) == gradient.listOfPoints.size() - 1) {
			return;
		}

		gradient.removePoint(selected);
		repaint(0);
		fireUpdate();
	}

	/**
	 * Move the current point to the specified mouse location
	 * 
	 * @param mx The x coordinate of the mouse
	 * @param my The y coordinate of the mouse
	 */
	private void movePoint(int mx, int my) {
		if (!isEnabled()) {
			return;
		}

		if (selected == null) {
			return;
		}
		if (gradient.listOfPoints.indexOf(selected) == 0) {
			return;
		}
		if (gradient.listOfPoints.indexOf(selected) == gradient.listOfPoints.size() - 1) {
			return;
		}

		float newPos = (mx - 10) / (float) width;
		newPos = Math.min(1, newPos);
		newPos = Math.max(0, newPos);

		selected.setPosition(newPos);
		gradient.sortList();
		fireUpdate();
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g1d) {
		super.paintComponent(g1d);

		Graphics2D g = (Graphics2D) g1d;
		width = getWidth() - 30;
		x = 10;
		y = 20;
		barHeight = 25;

		// paint gradient
		for (int i = 0; i < gradient.listOfPoints.size() - 1; i++) {
			GradientPoint now = gradient.listOfPoints.get(i);
			GradientPoint next = gradient.listOfPoints.get(i + 1);

			int size = (int) ((next.getPosition() - now.getPosition()) * width);
			g.setPaint(new GradientPaint(x, y, now.getColor(), x + size, y, next.getColor()));
			g.fillRect(x, y, size + 1, barHeight);
			x += size;
		}

		// paint gradient point markers
		g.setColor(Color.black);
		g.drawRect(10, y, width, barHeight - 1);

		for (GradientPoint pt : gradient.listOfPoints) {
			g.translate(10 + (width * pt.getPosition()), y + barHeight);
			g.setColor(pt.getColor());
			g.fillPolygon(poly);
			g.setColor(Color.black);
			g.drawPolygon(poly);

			if (pt == selected) {
				g.drawLine(-5, 12, 5, 12);
			}
			g.translate(-10 - (width * pt.getPosition()), -y - barHeight);
		}
	}

	/**
	 * Set the starting color
	 * 
	 * @param col The color at the start of the gradient
	 */
	public void setStart(Color col) {
		gradient.listOfPoints.get(0).setColor(col);
		repaint(0);
	}

	/**
	 * Set the ending color
	 * 
	 * @param col The color at the end of the gradient
	 */
	public void setEnd(Color col) {
		gradient.listOfPoints.get(gradient.listOfPoints.size() - 1).setColor(col);
		repaint(0);
	}

	/**
	 * Remove all the control points from the gradient editor (this does not include
	 * start and end points)
	 */
	public void clearPoints() {
		for (int i = 1; i < gradient.listOfPoints.size() - 1; i++) {
			gradient.removePoint(gradient.listOfPoints.get(i));
		}

		repaint(0);
		fireUpdate();
	}

	private void applyGradient() {
		gradient.getDisplay().refresh();
		gradient.getDisplay().gradientFrame.setVisible(false);
	}
}