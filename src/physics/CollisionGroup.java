package physics;

import java.util.ArrayList;

public class CollisionGroup {
	ArrayList<RigidBody> bodies;
	boolean exclusive;
	
	public CollisionGroup(boolean exclusive)
	{
		bodies = new ArrayList<RigidBody>();
		this.exclusive = exclusive;
	}
}
