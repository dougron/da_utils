package main.java.da_utils.time_signature_utilities.greatest_common_factor_calculator;

public class GreatestCommonFactor 
{

	// greatest common factor of two doubles
	
	public static double gcf(double a, double b)
	{
		double x, y;
		if (a > b)
		{
			x = a;
			y = b;
		}
		else 
		{
			x = b;
			y = a;
		}
		
		while (true)
		{
			if (y == 0) return x;
			double z = x % y;
			x = y;
			y = z;
		}
	}
	
	
	// greatest common factor of an array of doubles.....
	
	public static double gcf(double[] input)
	{
	    double result = input[0];
	    for(int i = 1; i < input.length; i++) result = gcf(result, input[i]);
	    return result;
	}
	
	
	// greatest common factor of an array of Doubles.....
	
		public static double gcf(Double[] input)
		{
		    if (input.length == 0) 
		    {
		    	return 0.0;
		    }
		    else
		    {
				double result = input[0];
				for (int i = 1; i < input.length; i++)
					result = gcf(result, input[i]);
				return result;
			}
		}
		
		
		public static double lcm(double a, double b)
		{
		    return a * (b / gcf(a, b));
		}
		
		public static Integer lcm(Integer a, Integer b)
		{
		    return (int)(a * (b / gcf(a, b)));
		}


		public static double lcm(double[] input)
		{
		    double result = input[0];
		    for(int i = 1; i < input.length; i++) result = lcm(result, input[i]);
		    return result;
		}
		
		public static double lcm(int[] input)
		{
		    double result = input[0];
		    for(int i = 1; i < input.length; i++) result = lcm(result, input[i]);
		    return result;
		}
	
		public static int lcm(Integer[] input)
		{
			Integer result = input[0];
		    for(int i = 1; i < input.length; i++) result = lcm(result, input[i]);
		    return result;
		}
	
	
	public static void main(String[] args) 
	{
		System.out.println(GreatestCommonFactor.gcf(0.5, 3.5));
//		System.out.println(GreatestCommonFactor.gcf(new double[] {254, 35434, 75424, 4336}));
		System.out.println(GreatestCommonFactor.lcm(new Integer[] {1, 2, 3,4, 5, 6, 7}));
	}


	

}
