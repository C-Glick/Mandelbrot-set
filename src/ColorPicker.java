import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import java.awt.BorderLayout;
import java.util.Hashtable;

public class ColorPicker {
	JFrame frame;
	
	ColorPicker(){
		frame = new JFrame();
		
		JSlider slider = new JSlider();
		slider.setMaximum(1000);
		slider.setValue(500);
		slider.setMajorTickSpacing(100);
		slider.setMinorTickSpacing(25);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		
		Hashtable labelTable = new Hashtable();
		labelTable.put( new Integer( 0 ), new JLabel("0.0") );
		labelTable.put( new Integer( 500 ), new JLabel("0.5") );
		labelTable.put( new Integer( 1000 ), new JLabel("1") );
		
		slider.setLabelTable(labelTable);
		
		frame.getContentPane().add(slider, BorderLayout.CENTER);
	}
	
	public void show(){
		frame.setVisible(true);
	}
	public void hide() {
		frame.setVisible(false);
	}
}
