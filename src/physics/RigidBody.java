package physics;

import utility.math.Transform2d;
import utility.math.Vec2d;

public class RigidBody {
	//transform properties
	Transform2d transform;
	public float angle;
	
	//collision properties
	public Shape[] shapes;
	public AABB aabb;
	
	//collision physics properties
	public float density;
	public float friction;
	public float restitution;
	
	//mass properties
	public Vec2d center_of_mass;
	public float inertia;
	public float mass;
	
	//dynamic properties
	public boolean dynamic;
	public boolean fixedRotation;
	
	public RigidBody(Vec2d position)
	{
		transform = new Transform2d(position, new Vec2d(1f, 0f), new Vec2d(0f, 1f));
		angle = 0;
	}
	public void setShapes(Shape... shapes)
	{
		this.shapes = shapes;
	}
	public void setProperties(float density, float friction, float restitution)
	{
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;
		
		mass = 0;
		for(int i = 0; i < shapes.length; i++)
			mass += shapes[i].shape.area;
	}
	/**
	 * calculates the bounding box for the body. Collisions first check the bounding box, then check the individual shapes.
	 * This helps to speed up collision detection time.
	 */
	public void calculateAABB()
	{
		aabb.set(Float.MAX_VALUE, -Float.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE);
		for(int i = 0; i < shapes.length; i++)
		{
			float[] aabb1 = shapes[i].computeAABB(transform);
			if(aabb1[0] < aabb.min_x)
				aabb.min_x = aabb1[0];
			if(aabb1[1] > aabb.max_x)
				aabb.max_x = aabb1[1];
			if(aabb1[2] < aabb.min_y)
				aabb.min_y = aabb1[2];
			if(aabb1[3] > aabb.max_y)
				aabb.max_y = aabb1[3];
		}
	}
	public float yTrajectory(float t)
	{
		return t;
	}
	public float xTraectory(float t)
	{
		return t;
	}
}
