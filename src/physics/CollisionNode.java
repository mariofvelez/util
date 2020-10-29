package physics;

import utility.math.Vec2d;
import utility.math.geometry.Geometry;

public class CollisionNode {
	public RigidBody a;
	public RigidBody b;
	
	public Vec2d collisionNormal()
	{
		//FIXME - find the normal vector to move b out of a, return (0, 0) if not in collision
		Vec2d normal = new Vec2d(0f, 0f);
		if(a.aabb.intersects(b.aabb))
			for(int i = 0; i < a.shapes.length; i++)
				for(int j = 0; j < b.shapes.length; j++)
					normal.add(Geometry.intersectionNormal(a.shapes[i].shape.projectTo(a.transform),
														   b.shapes[j].shape.projectTo(b.transform)));
		return normal;
	}
	public void collide()
	{
		Vec2d normal = collisionNormal();
		if(normal.x == 0 && normal.y == 0)
			return;
		/*
		 * 1. get the perpendicular distance from the center of mass of each rigid body
		 * 2. find the torques with the friction using the normal force
		 * 3. use conservation of energy to find the 
		 */
	}
	public boolean contains(RigidBody body)
	{
		return body.equals(a) || body.equals(b);
	}
	
}
