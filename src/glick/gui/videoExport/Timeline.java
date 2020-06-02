package glick.gui.videoExport;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

public class Timeline {
	// TreeSet holds all of the key points for this timeline and automatically sorts
	// them into increasing frame order
	TreeSet<KeyPoint> timeline;
	private KeyPoint current;
	private KeyPoint previous;
	private KeyPointComparator comparator;

	public Timeline() {
		comparator = new KeyPointComparator();
		timeline = new TreeSet<KeyPoint>(comparator);
	}

	public void add(KeyPoint keyPoint) {
		timeline.add(keyPoint);
	}

	public void add(double value, int frame) {
		add(new KeyPoint(value, frame));
	}

	public double getValue(int frame) {
		if (timeline.isEmpty() || frame < 0) {
			return 0;
		}

		KeyPoint key1 = null;
		KeyPoint key2 = null;
		Iterator<KeyPoint> iter = timeline.iterator();

		while (iter.hasNext()) {
			current = iter.next();
			if (current.getFrame() == frame) {
				return current.getValue();
			} else if (current.getFrame() > frame) {

				if (previous == null) {
					// requested value before first key point
					return current.getValue();
				}

				key1 = previous;
				key2 = current;
				break;
			} else {
				previous = current;
			}
		}

		// requested value after last key point
		if (key1 == null && key2 == null) {
			return timeline.last().getValue();
		}

		// TODO: cubic interpolation

		// linear interpolation between key1 and key2,
		return (key1.getValue() * (key2.getFrame() - frame) + key2.getValue() * (frame - key1.getFrame()))
				/ (key2.getFrame() - key1.getFrame());

	}

	@Override
	public String toString() {
		String s = "";
		boolean first = true;
		for (KeyPoint keyPoint : timeline) {
			if (first) {
				s += keyPoint.toString();
				first = false;
			} else {
				s += " | " + keyPoint.toString();
			}
		}
		return s;
	}

}
