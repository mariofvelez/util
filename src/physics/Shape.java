package physics;

import utility.math.Transform2d;
import utility.math.Vec2d;
import utility.math.geometry.Shape2d;

public abstract class Shape {	
	Shape2d shape;
	
	public Shape(Shape2d shape)
	{
		this.shape = shape;
	}
	/**
	 * 
	 * @param tf2d - computes the AABB for a shape in this transform
	 * @returns the AABB values as an array
	 */
	public abstract float[] computeAABB(Transform2d tf2d);
}
