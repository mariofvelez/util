package physics;

import utility.math.Vec2d;

public class AABB {
	public float min_x;
	public float max_x;
	public float min_y;
	public float max_y;
	
	public AABB(float min_x, float max_x, float min_y, float max_y)
	{
		this.min_x = min_x;
		this.max_x = max_x;
		this.min_y = min_y;
		this.max_y = max_y;
	}
	public void set(float min_x, float max_x, float min_y, float max_y)
	{
		this.min_x = min_x;
		this.max_x = max_x;
		this.min_y = min_y;
		this.max_y = max_y;
	}
	public void move(float x, float y)
	{
		min_x += x;
		max_x += x;
		min_y += y;
		max_y += y;
	}
	public boolean intersects(Vec2d point)
	{
		return point.x > min_x && point.x < max_x && point.y > min_y && point.y < max_y;
	}
	public boolean intersects(AABB other)
	{
		return max_x > other.min_x && min_x < other.max_x &&
				max_y > other.min_y && min_y < other.max_y;
	}
	public boolean moveOut(AABB other)
	{
		/*
		 * 
		 */
		if(!intersects(other))
			return false;
		
		float right = other.min_x - max_x; // distance between each side
		float left = min_x - other.max_x;
		float up = other.min_y - max_y;
		float down = min_y - other.max_y;
		
		float lr = right > left? -right : left;
		float ud = up > down? up : -down;
		
		if(Math.abs(lr) < Math.abs(ud))
			move(lr, 0);
		else
			move(0, ud);
		return true;
	}
	public Vec2d[] getBoxCoordinates()
	{
		return new Vec2d[] {new Vec2d(min_x, max_y), new Vec2d(max_x, max_y), new Vec2d(max_x, min_y), new Vec2d(min_x, min_y)};
	}
}
