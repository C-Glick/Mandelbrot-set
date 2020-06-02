package glick;

import glick.gui.videoExport.KeyPoint;
import glick.gui.videoExport.Timeline;

public class TestBench {

	public static void main(String[] args) {
		Timeline t = new Timeline();
		KeyPoint a = new KeyPoint(1, 1);

		t.add(a);

		t.getValue(2);
	}

}
