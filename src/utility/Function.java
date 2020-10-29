package utility;

public interface Function {
	
	public double f(double x);
	
	public static Function relu = x -> {if(x < 0) return 0.0; return x;};
	public static Function squared = x -> x*x;
	public static Function sigmoid  = x -> 1d / (1+Math.exp(-x));
	public static Function dsigmoid = x -> x * (1-x);
	public static Function negate = x -> -x;

}
