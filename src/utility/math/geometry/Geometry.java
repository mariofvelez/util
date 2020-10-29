package utility.math.geometry;

import utility.math.Vec2d;

public abstract class Geometry {
	/**
	 * 
	 * @param a
	 * @param b
	 * @returns the vector to move b outside of a
	 */
	public static Vec2d intersectionNormal(Shape2d a, Shape2d b)
	{
		if(a.getClass().equals(Circle.class))
			if(b.getClass().equals(Circle.class))
				return intersectionNormal((Circle) a, (Circle) b);
			else
				return intersectionNormal((Circle) a, (Polygon2d) b);
		else
			if(b.getClass().equals(Circle.class))
			{
				Vec2d normal =  intersectionNormal((Circle) b, (Polygon2d) a);
				normal.negate();
				return normal;
			}
			else
				return intersectionNormal((Polygon2d) a, (Polygon2d) b);
	}
	public static boolean intersected(Shape2d a, Shape2d b)
	{
		if(a.getClass().equals(Circle.class))
			if(b.getClass().equals(Circle.class))
				return intersected((Circle) a, (Circle) b);
			else
				return intersected((Circle) a, (Polygon2d) b);
		else
			if(b.getClass().equals(Circle.class))
				return intersected((Circle) b, (Polygon2d) a);
			else
				return intersected((Polygon2d) a, (Polygon2d) b);
	}
	/**
	 * 
	 * @param a - the circle
	 * @param b - the other circle
	 * @returns if the circles are in collision
	 */
	public static boolean intersected(Circle a, Circle b)
	{
		float dist_x = b.pos.x - a.pos.x;
		float dist_y = b.pos.y - a.pos.y;
		float r2 = a.radius + b.radius;
		return dist_x*dist_x + dist_y*dist_y < r2*r2;
	}
	/**
	 * 
	 * @param a - the circle
	 * @param b - the other circle
	 * @returns the vector to move b outside of a
	 */
	public static Vec2d intersectionNormal(Circle a, Circle b)
	{
		float dist_x = b.pos.x - a.pos.x;
		float dist_y = b.pos.y - a.pos.y;
		Vec2d normal = new Vec2d(dist_x, dist_y);
		normal.normalize();
		float dist2 = dist_x*dist_x + dist_y*dist_y;
		float r2 = a.radius + b.radius;
		if(dist2 < r2*r2)
		{
			float len = a.radius+b.radius - (float) (Math.sqrt(dist2));
			normal.mult(len);
			return normal;
		}
		return new Vec2d(0f, 0f);
			
	}
	/**
	 * 
	 * @param a - the circle
	 * @param b - the polygon
	 * @returns if the circle and polygon are in collision
	 */
	public static boolean intersected(Circle a, Polygon2d b)
	{
		boolean outside = false;
		float minV = Float.MAX_VALUE;
		Vec2d minN = new Vec2d(0f, 0f);
		for(int i = 0; i < b.edges.length; i++) //if closest voronoi is an edge
		{
			Vec2d edge = Vec2d.subtract(b.edges[i][1], b.edges[i][0]);
			float len = edge.length();
			Vec2d normal = edge.leftNormal();
			edge.normalize();
			normal.normalize();
			Vec2d apos = Vec2d.subtract(a.pos, b.edges[i][0]);
			float dist = Vec2d.dotProduct(normal, apos);
			float edist = Vec2d.dotProduct(edge, apos);
			edge.normalize();
			if(Math.abs(dist) < Math.abs(minV) && edist > 0 && edist < len)
			{
				minV = dist;
				minN = normal;
				minN.mult(-dist+a.radius);
			}
			if(dist > 0)
				outside = true;
		}
		for(int i = 0; i < b.vertices.length; i++) //if closest voronoi is a vertex
		{
			float dist = Vec2d.dist(a.pos, b.vertices[i]);
			if(outside && dist < Math.abs(minV))
			{
				minV = dist;
				minN = Vec2d.subtract(a.pos, b.vertices[i]);
				minN.normalize();
				minN.mult(-dist+a.radius);
			}
			
		}
		
		if(!outside || minV < a.radius)
		{
//			a.pos.add(Vec2d.mult(minN, 0.5f));
//			for(int i = 0; i < b.vertices.length; i++)
//				b.vertices[i].subtract(Vec2d.mult(minN, 0.5f));
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param a - the circle
	 * @param b - the polygon
	 * @returns the vector to move b outside of a
	 */
	public static Vec2d intersectionNormal(Circle a, Polygon2d b)
	{
		boolean outside = false;
		float minV = Float.MAX_VALUE;
		Vec2d minN = new Vec2d(0f, 0f);
		for(int i = 0; i < b.edges.length; i++) //if closest voronoi is an edge
		{
			Vec2d edge = Vec2d.subtract(b.edges[i][1], b.edges[i][0]);
			float len = edge.length();
			Vec2d normal = edge.leftNormal();
			edge.normalize();
			normal.normalize();
			Vec2d apos = Vec2d.subtract(a.pos, b.edges[i][0]);
			float dist = Vec2d.dotProduct(normal, apos);
			float edist = Vec2d.dotProduct(edge, apos);
			edge.normalize();
			if(Math.abs(dist) < Math.abs(minV) && edist > 0 && edist < len)
			{
				minV = dist;
				minN = normal;
				minN.mult(-dist+a.radius);
			}
			if(dist > 0)
				outside = true;
		}
		for(int i = 0; i < b.vertices.length; i++) //if closest voronoi is a vertex
		{
			float dist = Vec2d.dist(a.pos, b.vertices[i]);
			if(outside && dist < Math.abs(minV))
			{
				minV = dist;
				minN = Vec2d.subtract(a.pos, b.vertices[i]);
				minN.normalize();
				minN.mult(-dist+a.radius);
			}
			
		}
		
		if(!outside || minV < a.radius)
		{
			minN.negate();
			return minN;
		}
		return new Vec2d(0f, 0f);
	}
	/**
	 * 
	 * @param a - the polygon
	 * @param b - the other polygon
	 * @returns if the polygons are in collision
	 */
	public static boolean intersected(Polygon2d a, Polygon2d b)
	{
		for(int i = 0; i < b.vertices.length; i++)
			if(a.intersects(b.vertices[i]))
				return true;
		for(int i = 0; i < a.vertices.length; i++)
			if(b.intersects(a.vertices[i]))
				return true;
		return false;
	}
	public static Vec2d intersectionNormal(Polygon2d a, Polygon2d b)
	{
		float max_dist = 0;
		Vec2d max_dist_vector = new Vec2d(0, 0);
		for(int i = 0; i < b.vertices.length; i++)
		{
			Vec2d normal = a.distance(b.vertices[i], true);
			float dist = normal.length();
			if(dist > max_dist)
			{
				max_dist = dist;
				normal.negate();
				max_dist_vector = normal;
			}
		}
		for(int i = 0; i < a.vertices.length; i++)
		{
			Vec2d normal = b.distance(a.vertices[i], true);
			float dist = normal.length();
			if(dist > max_dist)
			{
				max_dist = dist;
				max_dist_vector = normal;
			}
		}
		return max_dist_vector;
	}
	/**
	 * 
	 * @param a - the polygon
	 * @param b - the other polygon
	 * @returns the vector to move b outside of a
	 */
	public static Vec2d intersectionNormal1(Polygon2d a, Polygon2d b)
	{
		if(!intersected(a, b))
			return Vec2d.ZERO;
		
		Polygon2d shape_a = a;
		Polygon2d shape_b = b;
		
		float overlap = Float.POSITIVE_INFINITY;
		Vec2d overlap_vec = new Vec2d(0, 0);
		
		for(int shape = 0; shape < 2; shape++)
		{
			if(shape==1)
			{
				shape_a = b;
				shape_b = a;
			}
			for(int i = 0; i < shape_a.edges.length; i++)
			{
				Vec2d axis = Vec2d.subtract(shape_a.edges[i][1], shape_a.edges[i][0]).leftNormal();
				float len = axis.length();
				axis.normalize();
				
				float min1 = axis.dotProduct(Vec2d.subtract(shape_a.vertices[0], shape_a.edges[0][0]));
				float max1 = min1;
				for(int j = 1; j < shape_a.vertices.length; j++)
				{
					float dp = axis.dotProduct(Vec2d.subtract(shape_a.vertices[j], shape_a.edges[j][0]));
					min1 = Math.min(min1, dp);
					max1 = Math.max(max1, dp);
				}
				float min2 = axis.dotProduct(Vec2d.subtract(shape_b.vertices[0], shape_a.edges[0][0]));;
				float max2 = min2;
				for(int j = 1; j < shape_b.vertices.length; j++)
				{
					float dp = axis.dotProduct(Vec2d.subtract(shape_b.vertices[j], shape_a.edges[j][0]));
					min2 = Math.min(min2, dp);
					max2 = Math.max(max2, dp);
				}
				float curr_overlap = Math.min(max1, max2)-Math.max(min1, min2);
				if(curr_overlap < overlap)
				{
					overlap = curr_overlap;
					overlap_vec = axis;
				}
//				if(max2-len < min1 || max1 < min2-len)
//					return Vec2d.ZERO;
			}
		}
		overlap_vec.normalize();
		overlap_vec.mult(overlap);
		return overlap_vec;
	}
	/**
	 * 
	 * @param a - the circle
	 * @param b - the other circle
	 * @returns the point(s) that the circles intersect at
	 */
	public Vec2d[] intersection(Circle a, Circle b)
	{
		//FIXME - finish the code
		/*
		 * 1: find the distance between the centers and call it d
		 * 2: if d is greater than both the radii, return an empty set
		 * 3: if d + the radius of the smaller one is less than the larger one, the smaller one is inside the larger one
		 * 	  return an empty set
		 * 4: if d is double the radius, interpolate between the centers 
		 */
		return new Vec2d[0];
	}
	/**
	 * 
	 * @param a - the circle
	 * @param b - the polygon
	 * @returns the point(s) that the circle and polygon intersect at
	 */
	public Vec2d[] intersection(Circle a, Polygon2d b)
	{
		//FIXME - finish the code
		/*
		 * 1: test all points of b for intersection with a
		 * 2: test all edges of b for intersection with a
		 */
		return new Vec2d[0];
	}
//	public Vec2d[] intersection(Polygon2d a, Polygon2d b)
//	{
//		int intersections = 0;
//		for(int i = 0; i < a.edges.length; i++)
//		{
//			for(int j = 0; j < b.edges.length; j++)
//			{
//				Vec2d poi = new Vec2d(0, 0);
//				
//				float bxax = b.edges[j][1].x - b.edges[j][0].x;
//				float byay = b.edges[j][1].y - b.edges[j][0].y;
//				float dycy = a.edges[i][1].x - a.edges[i][0].x;
//				float dxcx = a.edges[i][1].x - a.edges[i][0].x;
//				
//				float denom = dycy*bxax - dxcx*byay;
//				if(denom == 0)
//					continue;
//				
//				float axcx = b.edges[j][0].x - a.edges[i][0].x;
//				float aycy = b.edges[j][0].y - a.edges[i][0].x;
//			}
//		}
//		return new Vec2d[0];
//	}
}
