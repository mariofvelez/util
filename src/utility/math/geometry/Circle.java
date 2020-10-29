package utility.math.geometry;

import java.awt.Color;
import java.awt.Graphics2D;

import utility.MathUtils;
import utility.math.Transform2d;
import utility.math.Vec2d;

public class Circle extends Shape2d {
	public Vec2d pos;
	public float radius;
	float radius2;
	public float area;
	
	public Circle(Vec2d pos, float radius)
	{
		this.pos = pos;
		this.radius = radius;
		radius2 = radius*radius;
		area = MathUtils.pi*radius2;
	}
	public void debugDraw(Graphics2D g2, boolean fill)
	{
		if(fill)
		{
			g2.setColor(new Color(255, 127, 0, 100));
			g2.fillOval((int) (pos.x-radius), (int) (pos.y-radius), (int) (radius*2), (int) (radius*2));
		}
		g2.setColor(Color.BLACK);
		g2.drawOval((int) (pos.x-radius), (int) (pos.y-radius), (int) (radius*2), (int) (radius*2));
	}
	public boolean intersects(Vec2d point)
	{
		float dist_x = point.x - pos.x;
		float dist_y = point.y - pos.y;
		
		return dist_x*dist_x + dist_y*dist_y < radius2;
	}
	public boolean intersects(LineSegment ls)
	{
		Vec2d dir = Vec2d.subtract(ls.a, ls.b);
		float len = dir.length();
		dir.normalize();
		Vec2d normal = dir.leftNormal();
		Vec2d v = Vec2d.subtract(pos, ls.b);
		float along_dir = Vec2d.dotProduct(v, dir);
		float along_normal = Vec2d.dotProduct(v, normal);
		if((along_dir < len && along_dir >= 0 && Math.abs(along_normal) < radius) || intersects(ls.a) || intersects(ls.b))
			return true;
		return false;
	}
	public Vec2d projectedBounds(Vec2d axis, Vec2d pos)
	{
		Vec2d dir = Vec2d.subtract(this.pos, pos);
		float p = Vec2d.dotProduct(dir, axis);
		Vec2d v = new Vec2d(p-radius, p+radius);
		return v;
	}
	public Vec2d distance(Vec2d point, boolean inside)
	{
		Vec2d to_center = Vec2d.subtract(point, pos);
		to_center.normalize();
		float dist = Vec2d.subtract(point, pos).length() - radius;
		to_center.mult(dist);
		return to_center;
	}
	public void move(Vec2d dist)
	{
		pos.add(dist);
	}
	public Shape2d projectTo(Transform2d tf2d)
	{
		Circle proj = new Circle(tf2d.projectToTransform(pos), radius);
		return proj;
	}

}
