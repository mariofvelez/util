package physics;

import java.util.ArrayList;
import java.util.function.Function;

import utility.math.Vec2d;

public class World {
	public ArrayList<RigidBody> bodies; //all the bodies in the world
	public ArrayList<CollisionNode> nodes; //all the possible collisions between bodies
	public ArrayList<CollisionNode> c_nodes; //all the active collisions between bodies
	public ArrayList<CollisionGroup> c_groups; //all groups for collisions
	Function<?, Vec2d> gravity; //gravity is a vector field :O
	
	public World(Function<?, Vec2d> gravity)
	{
		GravityType gType = new GravityType();
		this.gravity = gType::StandardGravity;
		
		bodies = new ArrayList<RigidBody>();
		nodes = new ArrayList<CollisionNode>();
		c_nodes = new ArrayList<CollisionNode>();
		c_groups = new ArrayList<CollisionGroup>();
	}
	public void removeCollision(RigidBody a, RigidBody b)
	{
		for(int i = 0; i < c_nodes.size(); i++)
		{
			if(c_nodes.get(i).contains(a) && c_nodes.get(i).contains(b))
			{
				c_nodes.remove(i);
				return;
			}
		}
	}
	public void addCollision(RigidBody a, RigidBody b)
	{
		CollisionNode c_node = null;
		for(int i = 0; i < nodes.size(); i++)
		{
			if(nodes.get(i).contains(a) && nodes.get(i).contains(b))
			{
				c_node = nodes.get(i);
			}
		}
		if(!c_nodes.contains(c_node))
			c_nodes.add(c_node);
	}
	public Vec2d gravity()
	{
		return gravity.apply(null);
	}
}
