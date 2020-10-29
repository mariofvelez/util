package utility.math;

public class Vec3d {
	public float x, y, z;
	
	public static final Vec3d ZERO = new Vec3d(0f, 0f, 0f);
	public static final Vec3d X_POS = new Vec3d(1f, 0f, 0f);
	public static final Vec3d X_NEG = new Vec3d(-1f, 0f, 0f);
	public static final Vec3d Y_POS = new Vec3d(0f, 1f, 0f);
	public static final Vec3d Y_NEG = new Vec3d(0f, -1f, 0f);
	public static final Vec3d Z_POS = new Vec3d(0f, 0f, 1f);
	public static final Vec3d Z_NEG = new Vec3d(0f, 0f, -1f);
	
	public Vec3d()
	{
		x = 0f;
		y = 0f;
		z = 0f;
	}
	public Vec3d(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vec3d(Vec3d copy)
	{
		this.x = copy.x;
		this.y = copy.y;
		this.z = copy.z;
	}
	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ")";
	}
	public void print()
	{
		System.out.println(toString());
	}
	public void set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void set(Vec3d vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	public void negate()
	{
		x = -x;
		y = -y;
		z = -z;
	}
	public float length()
	{
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
	public void add(float x, float y, float z)
	{
		this.x += x;
		this.y += y;
		this.z += z;
	}
	public void add(Vec3d vec)
	{
		x += vec.x;
		y += vec.y;
		z += vec.z;
	}
	public static Vec3d add(Vec3d a, Vec3d b)
	{
		return new Vec3d(a.x + b.x, a.y + b.y, a.z + b.z);
	}
	public void subtract(float x, float y, float z)
	{
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}
	public void subtract(Vec3d vec)
	{
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
	}
	public static Vec3d subtract(Vec3d a, Vec3d b)
	{
		return new Vec3d(a.x - b.x, a.y - b.y, a.z - b.z);
	}
	public void mult(float x, float y, float z)
	{
		this.x *= x;
		this.y *= y;
		this.z *= z;
	}
	public void mult(Vec3d vec)
	{
		x *= vec.x;
		y *= vec.y;
		z *= vec.z;
	}
	public static Vec3d mult(Vec3d a, Vec3d b)
	{
		return new Vec3d(a.x * b.x, a.y * b.y, a.z * b.z);
	}
	public void mult(float scale)
	{
		x *= scale;
		y *= scale;
		z *= scale;
	}
	public static Vec3d mult(Vec3d a, float scale)
	{
		return new Vec3d(a.x * scale, a.y * scale, a.z * scale);
	}
	public float dist(Vec3d vec)
	{
		return (float) (Math.sqrt((x-vec.x)*(x-vec.x) + (y-vec.y)*(y-vec.y) + (z-vec.z)*(z-vec.z)));
	}
	public static float dist(Vec3d a, Vec3d b)
	{
		return (float) (Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y) + (a.z-b.z)*(a.z-b.z)));
	}
	public static float dist2(Vec3d a, Vec3d b)
	{
		return (a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y) + (a.z-b.z)*(a.z-b.z);
	}
	public float dotProduct(Vec3d vec)
	{
		return x*vec.x + y*vec.y + z*vec.z;
	}
	public static float dotProduct(Vec3d a, Vec3d b)
	{
		return a.x*b.x + a.y*b.y + a.z*b.z;
	}
	public Vec3d crossProduct(Vec3d vec)
	{
		Vec3d cross = new Vec3d(
				y*vec.z - z*vec.y,
				z*vec.x - x*vec.z,
				x*vec.y - y*vec.x
				);
		return cross;
	}
	public static Vec3d crossProduct(Vec3d a, Vec3d b)
	{
		Vec3d cross = new Vec3d(
				a.y*b.z - a.z*b.y,
				a.z*b.x - a.x*b.z,
				a.x*b.y - a.y*b.x
				);
		return cross;
	}
	public void normalize()
	{
		float length = length();
		x /= length;
		y /= length;
		z /= length;
	}
	public static Vec3d normalize(Vec3d vec)
	{
		float length = vec.length();
		return new Vec3d(vec.x / length, vec.y / length, vec.z / length);
	}
	public static Vec3d avg(Vec3d... vecs)
	{
		Vec3d avg = new Vec3d(0, 0, 0);
		for(int i = 0; i < vecs.length; i++)
			avg.add(vecs[i]);
		avg.mult(1f/vecs.length);
		return avg;
	}
}
