package utility.math.geometry;

import java.awt.Graphics;
import java.awt.Graphics2D;

import utility.Function;
import utility.ScreenManager;
import utility.math.Vec2d;

public class Intersection {
	public LineSegment a;
	public LineSegment b;
	
	public Vec2d point;
	public boolean intersected;
	
	public Intersection(LineSegment a, LineSegment b, Vec2d point, boolean intersected)
	{
		this.a = a;
		this.b = b;
		this.point = point;
		this.intersected = intersected;
	}
	
	public void draw(Graphics g, ScreenManager sm)
	{
		Graphics2D g2 = (Graphics2D)g;
		if(!intersected)
			return;
		float[] point = sm.toScreen(this.point.x, this.point.y);
		g2.fillOval((int) point[0]-5, (int) point[1]-5, 10, 10);
	}

}
