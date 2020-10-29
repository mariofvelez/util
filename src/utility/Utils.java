package utility;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import user_interface.Field;
import utility.math.Vec2d;

public class Utils {
	
	public static int[] xPoints(double[][] a) // returns an array of the x positions of a button
	{
		int[] b = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			b[i] = (int) a[i][0];
		}
		return b;
	}
	public static int[] xPoints(float[][] a) // returns an array of the x positions of a button
	{
		int[] b = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			b[i] = (int) a[i][0];
		}
		return b;
	}
	public static int[] xPoints(Vec2d... vecs)
	{
		int[] points = new int[vecs.length];
		for(int i = 0; i < points.length; i++)
			points[i] = (int) vecs[i].x;
		return points;
	}
	public static int[] yPoints(Vec2d... vecs)
	{
		int[] points = new int[vecs.length];
		for(int i = 0; i < points.length; i++)
			points[i] = (int) vecs[i].y;
		return points;
	}
	public static int[] xPoints3D(double[][] a) // returns an array of the x positions of a button
	{
		int[] b = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			b[i] = (int) a[i][0];
		}
		return b;
	}
	public static float[] nPoints(float[][] a, int index)
	{
		float[] b = new float[a.length];
		for(int i = 0; i < a.length; i++)
			b[i] = a[i][index];
		return b;
	}

	public static int[] yPoints(double[][] a) // returns an array of the y positions of a button
	{
		int[] b = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			b[i] = (int) a[i][1];
		}
		return b;
	}
	public static int[] yPoints(float[][] a) // returns an array of the y positions of a button
	{
		int[] b = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			b[i] = (int) a[i][1];
		}
		return b;
	}
	public static int[] yPoints3D(double[][] a) // returns an array of the y positions of a button
	{
		int[] b = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			b[i] = (int) (a[i][1] - a[i][2]);
		}
		return b;
	}

	public static double min(double[] a) {
		double b = a[0];
		for (int i = 0; i < a.length; i++) {
			if (a[i] < b)
				b = a[i];
		}
		return b;
	}
	public static int indexOfMin(double[] a)
	{
		double b = a[0];
		int c = 0;
		for(int i = 1; i < a.length; i++)
		{
			if(a[i] < b)
			{
				b = a[i];
				c = i;
			}
		}
		return c;
	}
	public static int min(int[] a) {
		int b = a[0];
		for (int i = 1; i < a.length; i++) {
			if (a[i] < b)
				b = a[i];
		}
		return b;
	}

	public static double max(double[] a) {
		double b = a[0];
		for (int i = 1; i < a.length; i++) {
			if (a[i] > b)
				b = a[i];
		}
		return b;
	}
	public static int indexOfMax(double[] a)
	{
		double b = a[0];
		int c = 0;
		for(int i = 0; i < a.length; i++)
		{
			if(a[i] > b)
			{
				b = a[i];
				c = i;
			}
		}
		return c;
	}
	public static int max(int[] a) {
		int b = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] > b)
				b = a[i];
		}
		return b;
	}
	public static float min(float[] a) {
		float b = a[0];
		for (int i = 1; i < a.length; i++) {
			if (a[i] < b)
				b = a[i];
		}
		return b;
	}
	public static float max(float[] a) {
		float b = a[0];
		for (int i = 1; i < a.length; i++) {
			if (a[i] > b)
				b = a[i];
		}
		return b;
	}
	
	public static double avg(double... a) {
		double total = 0;
		for(double i : a)
			total += i;
		return total/a.length;
	}
	public static float avg(float... a) {
		float total = 0;
		for(float i : a)
			total += i;
		return total/a.length;
	}
	public static int avg(int... a) {
		int total = 0;
		for(int i : a)
			total += i;
		return total/a.length;
	}
	public static double[] flatten(double[][] a)
	{
		int len = a.length*a[0].length;
		double[] arr = new double[len];
		for(int i = 0; i < a.length; i++)
			for(int j = 0; j < a[i].length; j++)
				arr[i*a[i].length + j] = a[i][j];
		return arr;
	}
	/**
	 * 
	 * @param a - each array you want to combine
	 * @return a new array of the parameters combined together
	 */
	public static Object[] combine(Object[]... a)
	{
		int length = 0;
		for(int i = 0; i < a.length; i++)
			length += a[i].length;
		Object[] combined = new Object[length];
		int index = 0;
		for(int i = 0; i < a.length; i++)
		{
			combined[index] = a[i];
			index++;
		}
		return combined;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Object> T[] combineArrays(T[]... arrays)
	{
		int length = 0;
		for(int i = 0; i < arrays.length; i++)
			length += arrays[i].length;
		T[] combined = (T[]) new Object[length];
		int index = 0;
		for(int i = 0; i < arrays.length; i++)
		{
			combined[index] = (T) arrays[i];
			index++;
		}
		return combined;
	}
	
	public static float getSlope(Vec2d a, Vec2d b)
	{
		if(a.x == b.x)
			return Float.NaN;
		return b.y-a.y / b.x-a.x;
	}
	
	public static float getAngle(Vec2d a, Vec2d b)
	{
		return (float) Math.atan2(b.y-a.y, b.x-a.x);
	}
	
	public static Vec2d avg(Vec2d... vecs) {
		Vec2d avg;
		avg = vecs[0];
		for(int i = 1; i < vecs.length; i++)
			avg.add(vecs[i]);
		avg.set(avg.x/vecs.length, avg.y/vecs.length);
		return avg;
	}
	/**
	 * 
	 * @param n - an array of doubles
	 * @return a new array sorted least to greatest
	 */
	public static double[] selectionSort(double[] n)
	{
		double[] arr = n;
		int selected = 0;
		//int comparisons = 0;
		//int swaps = 0;
		for(int i = 0; i < arr.length; i++)
		{
			for(int j = selected; j < arr.length; j++)
			{
				if(arr[j] < arr[i])
				{
					double temp = arr[i];
					arr[i] = arr[j];
					arr[j] = temp;
					//swaps++;
				}
				//comparisons++;
			}
			selected++;
			//System.out.print(arr[i] + " ");
			
		}
		//System.out.println();
		//System.out.println("comparisons: " + comparisons);
		//System.out.println("swaps: " + swaps);
		return arr;
	}
	/**
	 * 
	 * @param n - the array of numbers
	 * @return a new sorted array using counting sort
	 */
	public static int[] countingSort(int[] n)
	{
		int arr[] = n;
		int[] counter = new int[max(arr) + 1];
		for(int i = 0; i < arr.length; i++)
			counter[arr[i]] ++;
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(int i = 0; i < counter.length; i++)
			for(int j = 0; j < counter[i]; j++)
				temp.add(i);
		for(int i = 0; i < n.length; i++)
			arr[i] = temp.get(i);
			//System.out.print(arr[i] + " ");
		//System.out.println();
		return arr;
	}
	public static int random(int seed)
	{
		int s0 = seed + 99;
		int s1 = seed;//seed*seed;//(int) (Math.pow(10, s.length()-1) - seed);
		s1 ^= s1<<23;
		s1 ^= s1>>17;
		s1 ^= s0;
		s1 ^= s0>>26;
		return s1 + s0;
	}
	public static int random(int seed, int seed2)
	{
		int s0 = seed;
		int s1 = seed2;//(int) (Math.pow(10, s.length()-1) - seed);
		s1 ^= s1<<23;
		s1 ^= s1>>17;
		s1 ^= s0;
		s1 ^= s0>>26;
		return s1 + s0;
	}
	public static double rand(double a, double b)
	{
		return Math.random()*(b-a) + a;
	}
	public static float rand(float a, float b)
	{
		return (float) Math.random()*(b-a) + a;
	}
	public static int randInt(int a, int b)
	{
		return (int) (Math.random()*(b-a)) + a;
	}
	/**
	 * 
	 * @param a - the number
	 * @return the number into a decimal number
	 */
	public static double toDecimal(int a)
	{
		double n = Math.abs(a);
		String s = Integer.toString(a);
		double n1 = (int) (Math.pow(10, s.length()));
		return n/n1;
//		while(n>=1)
//		{
//			n/=10;
//		}
//		n *= (10/9);
//		return n;
	}
	
	/**
	 * 
	 * @param timeStep - the seconds per frame
	 * @param steps - the amount of time (in time steps)
	 * @return the time (in hours, minutes, seconds, and millis) in a string
	 */
	public static String counterToString(float timeStep, float steps)
	{
		String str = "";
		float fps = 1/timeStep;
		int seconds = (int) (steps/fps);
		int millis = (int) (timeStep*steps*1000)%1000;
		int minutes = seconds/60;
		int hours = minutes/60;
		seconds %= 60;
		minutes %= 60;
		
		if(hours > 0)
			str += hours + " ";
		if(minutes < 10)
			str += "0" + minutes + " ";
		else
			str += minutes + " ";
		if(seconds < 10)
			str += "0" + seconds + " " + millis;
		else
			str += seconds + " " + millis;
		
		return str;
	}
	public static String counterToString(int millis)
	{
		String str = "";
		int seconds = millis/1000;
		int minutes = seconds/60;
		int hours = minutes/60;
		seconds %= 60;
		minutes %= 60;
		
		if(hours > 0)
			str += hours + " ";
		if(minutes < 10)
			str += "0" + minutes + " ";
		else
			str += minutes + " ";
		if(seconds < 10)
			str += "0" + seconds + " ";
		else
			str += seconds + " ";
		
		int mill = millis%1000;
		
		if(mill < 100)
			str += "0";
		if(mill < 10)
			str += "0";
		str += mill;
		
		return str;
	}
	public static int sign(float n)
	{
		if(n >= 0)
			return 1;
		else
			return -1;
	}
	public static Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = Field.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}
	public static double[] toArray(String str)
	{
		if(str.equals("null"))
			return null;
		ArrayList<Integer> index = new ArrayList<>();
		int commas = 0;
		for(int i = 0; i < str.length(); i++)
		{
			if(str.charAt(i) == ',')
			{
				commas++;
				index.add(i);
			}
		}
		double[] arr = new double[commas+1];
		arr[0] = Double.parseDouble(str.substring(1, index.get(0)));
		for(int i = 1; i < arr.length-1; i++)
			arr[i] = Double.parseDouble(str.substring(index.get(i-1)+2, index.get(i)));
		arr[arr.length-1] = Double.parseDouble(str.substring(index.get(arr.length-2)+2, str.length()-1));
		return arr;
	}
	
	public static void main(String[] args)
	{
		String str = "[5.00012, 3.2, 2.841119999, 1.15]";
		double[] arr = toArray(str);
		System.out.println(Arrays.toString(arr));
	}

}
