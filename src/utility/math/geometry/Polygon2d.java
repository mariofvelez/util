package utility.math.geometry;

import java.awt.Color;
import java.awt.Graphics2D;

import utility.Utils;
import utility.math.Transform2d;
import utility.math.Vec2d;

public class Polygon2d extends Shape2d {
	public Vec2d[] vertices;
	public Vec2d[][] edges;
	
	public Polygon2d(Vec2d... vertices)
	{
		this.vertices = vertices;
		edges = new Vec2d[vertices.length][2];
		for(int i = 1; i < vertices.length; i++)
		{
			edges[i][0] = vertices[i-1];
			edges[i][1] = vertices[i];
		}
		edges[0][0] = vertices[vertices.length-1];
		edges[0][1] = vertices[0];
		//computeArea();
		//System.out.println("area: " + area);
	}
	public void debugDraw(Graphics2D g2, boolean fill)
	{
		int[] px = new int[vertices.length];
		for(int i = 0; i < px.length; i++)
			px[i] = (int) vertices[i].x;
		
		int[] py = new int[vertices.length];
		for(int i = 0; i < py.length; i++)
			py[i] = (int) vertices[i].y;
		
		if(fill)
		{
			g2.setColor(new Color(255, 127, 0, 100));
			g2.fillPolygon(px, py, vertices.length);
		}
		g2.setColor(Color.BLACK);
		g2.drawPolygon(px, py, vertices.length);
	}
	public boolean intersects(Vec2d point)
	{
		for(int i = 1; i < vertices.length; i++)
		{
			Vec2d normal = Vec2d.subtract(vertices[i], vertices[i-1]).leftNormal();
			normal.normalize();
			Vec2d rel_point = Vec2d.subtract(point, vertices[i-1]);
			float dist = Vec2d.dotProduct(normal, rel_point);
			if(dist > 0)
				return false;
		}
		Vec2d normal = Vec2d.subtract(vertices[0], vertices[vertices.length-1]).leftNormal();
		Vec2d rel_point = Vec2d.subtract(point, vertices[vertices.length-1]);
		float dist = Vec2d.dotProduct(normal, rel_point);
		if(dist > 0)
			return false;
		return true;
	}
	public boolean intersects(LineSegment ls)
	{
		for(int i = 0; i < edges.length; i++)
			if(ls.intersects(edges[i][0], edges[i][1]))
				return true;
		return false;
	}
	public float intersection(Vec2d point)
	{
		float min_dist = Float.MAX_VALUE;
		for(int i = 1; i < vertices.length; i++)
		{
			Vec2d normal = Vec2d.subtract(vertices[i], vertices[i-1]).leftNormal();
			normal.normalize();
			Vec2d rel_point = Vec2d.subtract(point, vertices[i-1]);
			float dist = Vec2d.dotProduct(normal, rel_point);
			if(dist > 0)
				return 1;
			if(dist < min_dist)
				min_dist = dist;
		}
		Vec2d normal = Vec2d.subtract(vertices[0], vertices[vertices.length-1]).leftNormal();
		Vec2d rel_point = Vec2d.subtract(point, vertices[vertices.length-1]);
		float dist = Vec2d.dotProduct(normal, rel_point);
		if(dist > 0)
			return 1;
		if(dist < min_dist)
			min_dist = dist;
		return min_dist;
	}
	private float area(Vec2d... points)
	{
		Vec2d base = Vec2d.subtract(points[1], points[0]);
		float base_len = base.length();
		Vec2d normal = base.rightNormal();
		normal.normalize();
		float height_len = Vec2d.dotProduct(normal, Vec2d.subtract(points[2], points[0]));
		return base_len*height_len / 2;
	}
	public float computeArea()
	{
		float area = 0;
		for(int i = 2; i < vertices.length; i++)
			area += area(vertices[0], vertices[i-1], vertices[i]);
		this.area = area;
		return area;
	}
	public Vec2d projectedBounds(Vec2d axis, Vec2d pos)
	{
		float[] p = new float[vertices.length];
		for(int i = 0; i < p.length; i++)
		{
			Vec2d dir = Vec2d.subtract(vertices[i], pos);
			p[i] = Vec2d.dotProduct(dir, axis);
		}
		return new Vec2d(Utils.min(p), Utils.max(p));
	}
	/**
	 * @param point - the point to measure the distance
	 * @return the vector from the border of the polygon to the point
	 */
	public Vec2d distance(Vec2d point, boolean inside)
	{
		boolean outside = false;
		float minV = Float.MAX_VALUE;
		Vec2d minN = new Vec2d(0f, 0f);
		for(int i = 0; i < edges.length; i++) //if closest voronoi is an edge
		{
			Vec2d edge = Vec2d.subtract(edges[i][1], edges[i][0]);
			float len = edge.length();
			Vec2d normal = edge.leftNormal();
			edge.normalize();
			normal.normalize();
			Vec2d apos = Vec2d.subtract(point, edges[i][0]);
			float dist = Vec2d.dotProduct(normal, apos);
			float edist = Vec2d.dotProduct(edge, apos);
			edge.normalize();
			if(Math.abs(dist) < Math.abs(minV) && edist > 0 && edist < len)
			{
				minV = dist;
				minN = normal;
				minN.mult(dist);
			}
			if(dist > 0)
				outside = true;
		}
		for(int i = 0; i < vertices.length; i++) //if closest voronoi is a vertex
		{
			float dist = Vec2d.dist(point, vertices[i]);
			if(outside && dist < Math.abs(minV))
			{
				minV = dist;
				minN = Vec2d.subtract(point, vertices[i]);
				minN.normalize();
				minN.mult(dist);
			}
			
		}
		if(inside)
		{
			if(!outside || minV < 0)
			{
				return minN;
			}
			return new Vec2d(0, 0);
		}
		else if(!outside || minV < 0)
		{
			return new Vec2d(0, 0);
		}
		return minN;
	}
	public void move(Vec2d dist)
	{
		for(int i = 0; i < vertices.length; i++)
			vertices[i].add(dist);
	}
	public Shape2d projectTo(Transform2d tf2d)
	{
		Vec2d[] verts = new Vec2d[vertices.length];
		for(int i = 0; i < verts.length; i++)
			verts[i] = tf2d.projectToTransform(vertices[i]);
		return new Polygon2d(verts);
	}

}
