package user_interface;

import java.util.ArrayList;
import java.util.Map;

import org.joyconLib.Joycon;
import org.joyconLib.JoyconConstant;
import org.joyconLib.JoyconEvent;
import org.joyconLib.JoyconListener;

public class JoyconAgent implements JoyconListener {
	public Joycon left;
	public Joycon right;
	
	ArrayList<String> buttonsDown;
	float[] joystickLeft;
	float[] joystickRight;
	
	int joyconState;
	
	public JoyconAgent(int joyconState, Joycon left, Joycon right)
	{
		buttonsDown = new ArrayList<String>();
		if(left != null)
			left.setListener(this);
		if(right != null)
			right.setListener(this);
		this.joyconState = joyconState;
	}
	

	@Override
	public void handleNewInput(JoyconEvent e) {
		for (Map.Entry<String, Boolean> entry : e.getNewInputs().entrySet()) {
            if(entry.getValue())
            	buttonsDown.add(entry.getKey());
            else
            	buttonsDown.remove(entry.getKey());
        }
		if(e.joycon.equals(left))
		{
			
		}
		if(e.joycon.equals(right))
		{
			
		}
	}
	public float[] getJoysticValues()
	{
		//FIXME - make sure all values are correct
		float[] values = new float[2];
		switch(joyconState)
		{
		case JoyconState.LEFT_STATE:
			values[0] = joystickLeft[1];
			values[1] = joystickLeft[0];
			break;
		case JoyconState.RIGHT_STATE:
			values[0] = joystickRight[1];
			values[1] = -joystickRight[0];
			break;
		case JoyconState.DUAL_STATE:
			values[0] = joystickLeft[0];
			values[1] = joystickLeft[1];
		}
		return values;
	}
	public boolean[] getArrows()
	{
		boolean[] values = new boolean[4];
		switch(joyconState)
		{
		case JoyconState.LEFT_STATE:
			
		}
		return null;
	}

}
enum JoyconState {;
	public static final int LEFT_STATE = 0;
	public static final int RIGHT_STATE = 1;
	public static final int DUAL_STATE = 2;
}
