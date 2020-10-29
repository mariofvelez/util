package physics;

import utility.math.Transform2d;
import utility.math.Vec2d;
import utility.math.geometry.Circle;

public class CircleShape extends Shape {
	Circle c_shape;
	
	/**
	 * I know, it seems redundant, just go with it
	 */
	public CircleShape(Circle shape)
	{
		super(shape);
		c_shape = shape;
	}
	public float[] computeAABB(Transform2d tf2d)
	{
		Vec2d center = tf2d.projectToTransform(c_shape.pos);
		return new float[] {
			center.x - c_shape.radius,
			center.x + c_shape.radius,
			center.y - c_shape.radius,
			center.y + c_shape.radius,
		};
	}

}
