package glick.gui.videoExport;

import java.util.Comparator;

public class KeyPointComparator implements Comparator<KeyPoint> {

	@Override
	public int compare(KeyPoint key1, KeyPoint key2) {
		return key1.getFrame() - key2.getFrame();
	}

}
