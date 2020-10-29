package user_interface;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

/**
 * Manages all the screens of the window,
 * does not need to be instantiated because Window.start() already creates one
 * @author Mario Velez
 *
 */
public class UIManager implements Runnable{
	public int mousex;
	public int mousey;
	
	public ArrayList<UIScreen> screens;
	public UIScreen currScreen;
	
	public boolean setScreen;
	private UIScreen tempScreen = null;
	
	public static boolean running;
	public int frameRate;
	
	UIManager()
	{
		screens = new ArrayList<UIScreen>();
		frameRate = 0;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Long t1 = System.currentTimeMillis();
		running = true;
		while(running)
		{
			for(UIScreen screen : screens)
				if(screen.on)
					screen.step();
		}
		long t2 = System.currentTimeMillis();
		if(t1 != t2)
			frameRate = (int) (1000f / t2-t1);
		else
			frameRate = 0;
	}
	public void draw()
	{
		for(UIScreen screen : screens)
		{
			if(screen.on)
				if(screen.gmain != null && screen.bmain != null)
					screen.draw();
		}
	}
	/**
	 * Sets a screen to wait to be called by Field to be turned to
	 * @param screen - the screen to be set
	 */
	public void setScreen(UIScreen screen)
	{
		setScreen = true;
		tempScreen = screen;
		//Window.editThread.notify();
	}
	/**
	 * Sets the current screen to the one that's waiting to be set
	 */
	public void startSetScreen()
	{
		UIScreen screen = tempScreen;
		//removing other graphics from the canvas
		for(UIScreen screen1 : screens)
		{
			if(screen1.on)
			{
				screen1.gmain = null;
				screen1.bmain = null;
				for(int i = 0; i < screen1.bufferGraphics.length; i++)
				{
					screen1.bufferGraphics[i] = null;
					screen1.bufferStrategies[i] = null;
				}
				screen1.on = false;
				Window.edit.removeMouseListener(screen1);
				screen1.close();
			}
		}
		Field.bufferGraphics.removeAll(Field.bufferGraphics);
		Field.bufferStrategies.removeAll(Field.bufferStrategies);
		screen.on = true;
		//adding new graphics to the canvas
		if(screen.bmain == null)
		{
			Window.edit.createBufferStrategy(2);
			screen.bmain = Window.edit.getBufferStrategy();
			screen.gmain = (Graphics2D) screen.bmain.getDrawGraphics();
		}
		//if(screen.gmain == null)
			
		
		Field.bufferGraphics.add(screen.gmain);
		Field.bufferStrategies.add(screen.bmain);
		
		for(int i = 0; i < screen.bufferGraphics.length; i++)
		{	
			if (screen.bufferStrategies[i] == null)
			{
				Window.edit.createBufferStrategy(2);
				screen.bufferStrategies[i] = Window.edit.getBufferStrategy();
				screen.bufferGraphics[i] = (Graphics2D) screen.bufferStrategies[i].getDrawGraphics();
			}
			
			Field.bufferGraphics.add(screen.bufferGraphics[i]);
			Field.bufferStrategies.add(screen.bufferStrategies[i]);
		}
		Window.edit.addMouseListener(screen);
		screen.start();
		setScreen = false;
		tempScreen = null;
	}
	/**
	 * Adds a screen to this manager
	 * @param screen - the screen to be added
	 */
	public void createScreen(UIScreen screen)
	{
		screens.add(screen);
	}

}
