package utility;

import java.util.ArrayList;

public class MathUtils {
	public static final float pi = (float) Math.PI;
	public static final float twoPi = pi*2;
	public static final float halfPi = pi/2;
	private static final int accuracy = 1000000;
	private static float[] sines = new float[accuracy];
	private static float[] cosines = new float[accuracy];
	private static long[] factorial = new long[22];
	
	private static double trigDiff = accuracy/twoPi;
	
	public MathUtils()
	{
		for(int i = 0; i < factorial.length; i++)
		{
			factorial[i] = factorial(i);
		}
		for(int i = 0; i < sines.length; i++)
		{
			float index = twoPi/accuracy;
			index *= i;
			sines[i] = tempSin(index);
			//System.out.println(i + " " + sines[i]);
		}
		for(int i = 0; i < cosines.length; i++)
		{
			float index = twoPi/accuracy;
			index *= i;
			cosines[i] = tempCos(index);
		}
	}
	private int distance(int acc, double x)
	{
		int distance = 900;
		int dx = distance/acc;
		return (int) (x*dx);
	}
	public static float sin(float num)
	{
		if(num < 0)
			return sin(num + twoPi);
		return sines[(int) (num%twoPi * trigDiff)];
	}
	public static float tempSin(double x)
	{
		final double b = x%pi;
		double a = 0;
		for(int n = 1, c = 1; n < 20; n+=2, c *= -1)
		{
			double temp = pow(b, n)/factorial[n];
			temp *= c;
			a += temp;
		}
		if(x%twoPi > pi)
			a *= -1;
		return (float) a;
	}
	public double floor(double a)
	{
		return (double) ((int) a);
	}
	public static float cos(float num)
	{
		if(num < 0)
			return cos(num + twoPi);
		return cosines[(int) (num%twoPi * trigDiff)];
	}
	public static float tempCos(float x)
	{
		return tempSin(x + halfPi);
	}
	public static float tan(float num)
	{
		return sin(num)/cos(num);
	}
	public static float csc(float num)
	{
		return 1/sin(num);
	}
	public static float sec(float num)
	{
		return 1/cos(num);
	}
	public static float cot(float num)
	{
		return cos(num)/sin(num);
	}
	public static long factorial(long n)
	{
		if(n < 2)
			return 1;
		return n*factorial(n-1);
	}
	public static int alt(long i) //returns -1 or 1
	{
		byte k = 1;
		for(long j = 0; j < i; j++)
		{
			k *= -1;
		}
		return k;
	}
	public static double pow(double a, int b)
	{
		if(b < 1)
			return 1;
		return a*pow(a, b-1);
	}
	public static ArrayList<Integer> getFactors(int a)
	{
		ArrayList<Integer> factors = new ArrayList<Integer>();
		int b = a;
		for(int i = 2; b > 2; i++)
		{
			if(b%i == 0)
			{
				factors.add(i);
				b /= i;
				i = 2;
			}
		}
		if(b != 1)
			factors.add(b);
		return factors;
	}
}
