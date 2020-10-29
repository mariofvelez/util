package user_interface;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import org.joyconLib.Joycon;
import org.joyconLib.JoyconConstant;
import org.joyconLib.JoyconEvent;
import org.joyconLib.JoyconListener;

import utility.Utils;

public class JoyconSetupScreen extends UIScreen implements JoyconListener {

	public Joycon[] joycons;
	public ArrayList<JoyconAgent> joyconAgents;
	
	private ArrayList<Joycon> tempJoycons; //temporary joy-cons stored before adding to agents
	private ArrayList<String> tempKeys; //corresponding key pressed of the temporary joy-con
	private ArrayList<Long> tempTimes; //the time the joy-con was pressed
	
	public static Image jcImage;
	public BufferedImage bjcImage;
	
	//left images
	public static Image left_top;
	Image left_bottom;
	Image left_front;
	Image left_back;
	Image left_side;
	
	//right images
	Image right_top;
	Image right_bottom;
	Image right_front;
	Image right_back;
	Image right_side;
	
	/**
	 * A screen that sets up the joy-cons into agents (players) for use
	 * @param exitMethod - the method to exit out of this screen
	 * @param joycons - all the joy-cons to be paired
	 * @throws Exception
	 */
	public JoyconSetupScreen(Method exitMethod, Joycon... joycons) throws Exception {
		super(2, 1);
		
		joyconAgents = new ArrayList<JoyconAgent>();
		
		tempJoycons = new ArrayList<Joycon>();
		tempKeys = new ArrayList<String>();
		tempTimes = new ArrayList<Long>();
		
		on = false;
		int[][] exitPos = {
				{10, 10},
				{110, 60}
		};
		int[][] resetPos = {
				{400, 500},
				{500, 550}
		};
		Button exitButton = new Button("exit", exitPos, exitMethod, null);
		Button resetButton = new Button("reset", resetPos, this.getClass().getMethod("reset"), this);
		this.addButton(exitButton);
		this.addButton(resetButton);
		
		this.joycons = joycons;
		for(int i = 0; i < joycons.length; i++)
			joycons[i].setListener(createListener());
		
		jcImage = Utils.getImage("Nintendo_Switch_Joy-Con_illustration.png");
		bjcImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		Graphics g2 = bjcImage.getGraphics();
		g2.clearRect(0, 0, 500, 500);
		ImageObserver o = null;
		g2.drawImage(jcImage, 0, 0, 500, 500, o);
		
		left_top = bjcImage.getSubimage(0, 0, 500, 500);
		
		//TODO - partition the jcImage to each sub image for drawing
	}
	private JoyconListener createListener()
	{
		return new JoyconListener() {
			public void handleNewInput(JoyconEvent je) {
                for (Map.Entry<String, Boolean> entry : je.getNewInputs().entrySet()) {
                    switch(entry.getKey())
                    {
                    case JoyconConstant.SL:
                    case JoyconConstant.SR:
                    case JoyconConstant.L:
                    case JoyconConstant.ZL:
                    case JoyconConstant.R:
                    case JoyconConstant.ZR:
                    	tempJoycons.add(je.joycon);
                    	tempKeys.add(entry.getKey());
                    	tempTimes.add(System.currentTimeMillis());
                    }
                }
                //Print to the console the position of the joystick
                //System.out.println("Joystick\tX: " + je.getHorizontal() + "\tY: " + je.getVertical());
                //System.out.println("Battery: " + je.getBattery());
            }
		};
	}
	
	public void reset()
	{
		//TODO - reset all the agents for re-connection
		/*
		 * reset all listeners to a new listener
		 * 
		 */
		tempJoycons.removeAll(tempJoycons);
		tempKeys.removeAll(tempKeys);
	}
	/**
	 * the time in milliseconds between button presses for a pair to be registered
	 */
	private static final int TIME_THRESH = 200;
	public void step()
	{
		//FIXME - make sure controllers are properly connected to their agents
		for(int i = 0; i < tempJoycons.size(); i++)
		{
			Joycon jc = tempJoycons.get(i);
			String key = tempKeys.get(i);
			Long time = tempTimes.get(i);
			
			for(int j = 0; j < tempJoycons.size(); j++)
			{
				Long time1 = tempTimes.get(j);
				if(i != j && Math.abs(time - time1) < TIME_THRESH)
				{
					Joycon jc1 = tempJoycons.get(j);
					String key1 = tempKeys.get(j);
					String temp = key+key1;
					
					boolean paired = false;
					//same joy-con
					if(jc.equals(jc1))
					{
						if(temp.contains("SR") && temp.contains("SL"))
						{
							if(jc.getProductId() == JoyconConstant.JOYCON_LEFT)
								paired = joyconAgents.add(new JoyconAgent(JoyconState.LEFT_STATE, jc, null));
							else if(jc.getProductId() == JoyconConstant.JOYCON_RIGHT)
								paired = joyconAgents.add(new JoyconAgent(JoyconState.RIGHT_STATE, null, jc));
						}
					}
					
					//different joy-con - left then right
					else if(jc.getProductId() == JoyconConstant.JOYCON_LEFT && jc1.getProductId() == JoyconConstant.JOYCON_RIGHT)
						if((key.equals("L") && key1.equals("R")) || (key.equals("ZL") && key1.equals("ZR"))) 
							paired = joyconAgents.add(new JoyconAgent(JoyconState.DUAL_STATE, jc, jc1));
					
					//different joy-con - right then left
					else if(jc.getProductId() == JoyconConstant.JOYCON_RIGHT && jc1.getProductId() == JoyconConstant.JOYCON_LEFT)
						if((key.equals("R") && key1.equals("L")) || (key.equals("ZR") && key1.equals("ZL")))
							paired = joyconAgents.add(new JoyconAgent(JoyconState.DUAL_STATE, jc, jc1));
					
					if(paired)
					{
						tempJoycons.remove(jc);
						tempJoycons.remove(jc1);
						tempKeys.remove(key);
						tempKeys.remove(key1);
						tempTimes.remove((Object) time);
						tempTimes.remove((Object) time1);
						i--;
						break;
					}
				}
			}
		}
//		try {
//			this.wait(100);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Override
	public void handleNewInput(JoyconEvent e) {
		
	}

}
