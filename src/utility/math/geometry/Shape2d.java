package utility.math.geometry;

import java.awt.Graphics2D;

import utility.math.Transform2d;
import utility.math.Vec2d;

public abstract class Shape2d {
	public float area;
	
	/**
	 * draws the shape to the Graphics2D object
	 * @param g2 - the object the shape is drawn on
	 * @param fill - whether or not to fill the object
	 */
	public abstract void debugDraw(Graphics2D g2, boolean fill);
	/**
	 * 
	 * @param point - the point to test
	 * @returns true if the point is inside the shape, false it it's not
	 */
	public abstract boolean intersects(Vec2d point);
	/**
	 * 
	 * @param ls - the ray to test
	 * @returns true if the line segment intersects, false if not
	 */
	public abstract boolean intersects(LineSegment ls);
	/**
	 * finds the bounds of the shape projected on a 1 dimensional axis
	 * @param axis - the axis that is projected on. It is inferred that the axis is normalized
	 * @param pos - the origin of the axis
	 * @returns the bounds of this shape on the axis, x being the lower bounds, and y being the upper bounds
	 */
	public abstract Vec2d projectedBounds(Vec2d axis, Vec2d pos);
	/**
	 * 
	 * @param point - the point to test the distance from
	 * @param inside - true to test if the point is inside, false if not
	 * @returns the vector from the point to the shape
	 */
	public abstract Vec2d distance(Vec2d point, boolean inside);
	/**
	 * moves the shape by a certain amount
	 * @param dist - the vector to move it by
	 */
	public abstract void move(Vec2d dist);
	/**
	 * projects the shape on a given transform
	 * @param tf2d - the transform the shape is on
	 * @returns a new shape transformed by this shape
	 */
	public abstract Shape2d projectTo(Transform2d tf2d);
}
