package user_interface;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

public class UIScreen implements MouseListener {
	public boolean on;
	
	public Graphics2D gmain;
	public BufferStrategy bmain;
	public Graphics2D[] bufferGraphics;
	public BufferStrategy[] bufferStrategies;
	
	public Button[] buttons;
	
	/**
	 * 
	 * @param numButtons - the number of buttons for this screen
	 * @param numGraphics - the number of graphics to show on the screen, not including the preset main one
	 */
	public UIScreen(int numButtons, int numGraphics)
	{
		on = false;
		bufferGraphics = new Graphics2D[numGraphics];
		bufferStrategies = new BufferStrategy[numGraphics];
		
		for(int i = 0; i < bufferGraphics.length; i++)
			bufferGraphics[i] = null;
		for(int i = 0; i < bufferStrategies.length; i++)
			bufferStrategies[i] = null;
		
		buttons = new Button[numButtons];
	}
	
	/**
	 * 
	 * @param button - the button that is added to the screen
	 * @returns if the button was added. Will not add another button if all button slots were filled
	 */
	public boolean addButton(Button button)
	{
		for(int i = 0; i < buttons.length; i++)
		{
			if(buttons[i] == null)
			{
				buttons[i] = button;
				buttons[i].screen = this;
				return true;
			}
		}
		return false;
	}
	
	public void draw()
	{
		Image img = JoyconSetupScreen.jcImage;
		//gmain.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
		for(Button button : buttons)
		{
			button.draw(gmain);
			System.out.println("drawn");
		}
	}
	
	public void step()
	{
		
	}
	/**
	 * What the screen does upon opening
	 */
	public void start()
	{
		
	}
	/**
	 * what the screen does upon closing
	 */
	public void close()
	{
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int[] xy = {e.getX(), e.getY()};
		for(Button button : buttons)
			if(button.contains(xy))
				button.select();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
