package utility;

public class Line {
	float m;
	float b;
	
	public Line(float m, float b)
	{
		super();
		this.m = m;
		this.b = b;
	}
	public Line(float x, float y, float slope)
	{
		m = slope;
		b = y - slope*x;
	}
	public void set(float x, float y, float slope)
	{
		m = slope;
		b = y - slope*x;
	}
	public void setPos(float x, float y)
	{
		b = y - m*x;
	}
	
	public float f(float x)
	{
		return m*x + b;
	}
	public float getSlope(float x)
	{
		return m;
	}
	public float[] intersection(Line line)
	{
		if(m == line.m)
			return null;
		float[] ixy = new float[2];
		float diffB = line.b() - b;
		float diffM = m - line.getSlope(1);
		ixy[0] = diffB/diffM;
		ixy[1] = f(ixy[0]);
		return ixy;
	}
	public float b()
	{
		return b;
	}

}
