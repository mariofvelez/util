package physics;

import utility.math.Vec2d;

public class GravityType {
	public static final Vec2d standardAcc = new Vec2d(0.0f, -9.81f);
	
	public Vec2d StandardGravity(Object... inputs)
	{
		return standardAcc;
	}
}
