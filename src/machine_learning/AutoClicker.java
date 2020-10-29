package machine_learning;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.MouseEvent;

public class AutoClicker {

	public static void main(String[] args) throws AWTException {
		// TODO Auto-generated method stub
		Robot bot = new Robot();
		try {
			Thread.currentThread();
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long t1 = System.currentTimeMillis();
		while(System.currentTimeMillis()-t1 < 10000)
		{
			bot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
			bot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
		}
	}

}
