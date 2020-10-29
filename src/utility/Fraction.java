package utility;

import java.util.ArrayList;

public class Fraction {
	private int num;
	private int denom;
	
	public Fraction(int num, int denom)
	{
		this.num = num;
		this.denom = denom;
	}
	public void print()
	{
		System.out.println(num + "/" + denom);
	}
	public void mult(int n)
	{
		num *= n;
	}
	public Fraction mult(Fraction f)
	{
		int[] vars = f.vars();
		num *= vars[0];
		denom *= vars[1];
		return this;
	}
	public Fraction add(Fraction f)
	{
		int[] vars = f.vars();
		int cDenom = denom*vars[1];
		int num1 = num*vars[1];
		int num2 = vars[0]*denom;
		Fraction newF = new Fraction(num1 + num2, cDenom);
		return newF.simplify();
	}
	public int[] vars()
	{
		return new int[] {num, denom};
	}
	public boolean equals(Fraction f)
	{
		int[] vars = f.vars();
		return ((num == 0 && vars[0] == 0) || (num/(vars[0]+0.) == denom/(vars[1]+0.)));
	}
	public Fraction simplify() //simplifies the fraction
	{
		ArrayList<Integer> nums = MathUtils.getFactors(num);
		ArrayList<Integer> denoms = MathUtils.getFactors(denom);
		for(int i = 0; i < nums.size(); i++)
		{
			for(int j = 0; j < denoms.size(); j++)
			{
				if(nums.get(i) == denoms.get(j))
				{
					nums.remove(i);
					denoms.remove(j);
				}
			}
		}
		int newNum = 1;
		int newDenom = 1;
		for(int i : nums)
			newNum *= i;
		for(int i : denoms)
			newDenom *= i;
		num = newNum;
		denom = newDenom;
		return this;
	}

}
