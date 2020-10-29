package physics;

import utility.math.Transform2d;
import utility.math.Vec2d;
import utility.math.geometry.Polygon2d;

public class PolygonShape extends Shape {
	Polygon2d p_shape;
	
	//https://quest.cns.utexas.edu/student/instanceitempartresponses/create?instanceitempart=cuaip_690955254&response=16.9900&reload=1
	
	/**
	 * I know, it seems redundant
	 */
	public PolygonShape(Polygon2d shape)
	{
		super(shape);
		p_shape = shape;
	}
	public float[] computeAABB(Transform2d tf2d)
	{
		float[] aabb = {
				Float.MAX_VALUE,
				-Float.MAX_VALUE,
				Float.MAX_VALUE,
				-Float.MAX_VALUE
		};
		for(int i = 0; i < p_shape.vertices.length; i++)
		{
			Vec2d point = tf2d.projectToTransform(p_shape.vertices[i]);
			if(point.x < aabb[0])
				aabb[0] = point.x;
			if(point.x > aabb[1])
				aabb[1] = point.x;
			if(point.y < aabb[2])
				aabb[2] = point.y;
			if(point.y > aabb[3])
				aabb[3] = point.y;
		}
		return aabb;
		
	}
}
