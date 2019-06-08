import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

public class GlobalHandler implements Thread.UncaughtExceptionHandler {

	public void uncaughtException(Thread t, Throwable e) {
		//converting stack trace into a string
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		
		JOptionPane.showMessageDialog(null, "Error: "+e +"\n" + sw.toString());
		 e.printStackTrace();
	}

	
}
