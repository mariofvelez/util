package utility;

import java.util.ArrayList;

public final class StringUtils {
	
	public String getStringBetween(String string, char start, char end)
	{
		ArrayList<Integer> vals = new ArrayList<>(); //all temporary labels for each character in string
		int level = 0;
		int inside = 0;
		for(int i = 0; i < string.length(); i++)
		{
			char curr = string.charAt(i);
			if(curr == '(' || curr == '{' || curr == '[' || curr == '<')
			{
				level++;
				inside = curr;
				vals.add(level);
			}
			else if((inside == '(' && curr == inside+1) || (curr == inside+2))
			{
				inside = curr;
				vals.add(level);
				level--;
			}
			else
				vals.add(0);
				
		}
		return "";
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
	
}
