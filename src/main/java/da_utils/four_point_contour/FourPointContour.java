package main.java.da_utils.four_point_contour;
/*
 * class that supplies infor for a straight line function with four break points
 * 
 * the coordinate space is 0. <= x <= 1. and -1. <= y <= 1.
 * 
 * start point is implicitly (0,0)
 * 
 * two middle points are defined as (x,y) and by default their x values are > 1 
 * which means they do not get taken into account
 * 
 * the end point has x = 1 and a variable y point
 * 
 * this class can represent various contours such as: up, down, up down, down up and updownup 
 * or down up down
 */
public class FourPointContour {
	
	private double[] start = new double[]{0, 0};
	private double[] point1 = new double[]{3, 0};
	private double[] point2 = new double[]{2, 0};
	private double[] end = new double[]{1, 0};
	
	private double[][] array = new double[][]{start, end, point1, point2};

	public FourPointContour()
	{}
	
	
	public FourPointContour(double x1, double y1, double x2, double y2, double endy)
	{
		point1[0] = x1;
		point1[1] = y1;
		point2[0] = x2;
		point2[1] = y2;
		end[1] = endy;
		sort();
	}
	
	
	public FourPointContour(double x1, double y1, double x2, double y2)
	{
		point1[0] = x1;
		point1[1] = y1;
		point2[0] = x2;
		point2[1] = y2;
		sort();
	}
	
	
	public FourPointContour(double x1, double y1, double endy)
	{
		point1[0] = x1;
		point1[1] = y1;
		end[1] = endy;
		sort();
	}
	
	
	
	public FourPointContour(double x1, double y1){
		point1[0] = x1;
		point1[1] = y1;
		sort();
	}
	
	
	
	public FourPointContour(double endy)
	{
		end[1] = endy;
		sort();
	}
	
	
	
	public FourPointContour(int preset)
	{
		if (preset == UP)
		{
			setUP();
		} 
		else if (preset == DOWN)
		{
			setDOWN();
		}
		else if (preset == UPDOWN)
		{
			setUPDOWN();
		}
		else if (preset == DOWNUP)
		{
			setDOWNUP();
		}
		else if (preset == STRAIGHT)
		{
			setSTRAIGHT();
		}
	}
	
	
	
	


	public void sort()
	{
		for (int i = 0; i < 2; i++)
		{
			for (int j = 1; j < array.length - 1; j++)
			{
				if (array[j][0] > array[j + 1][0])
				{
					double[] temp = array[j + 1];
					array[j + 1] = array[j];
					array[j] = temp;
				}
			}
		}
	}
	
	
	
	public double getValue(double d)
	{
		double rd = 0;
		for (int i = 1; i < array.length; i++)
		{
			double[] thisArray = array[i];
			double[] lastArray = array[i - 1];
			if (d > 0)
			{
				if (array[i][0] <=1)
				{
					if (d <= array[i][0])
					{
						
						double x1 = array[i - 1][0];
						double y1 = array[i - 1][1];
						double x2 = array[i][0];
						double y2 = array[i][1];
						rd = (y2 - y1) * (d - x1) / (x2 - x1) + y1;
						break;
					}
				}
			} 
			else 
			{
				break;
			}	
		}
		return rd;
	}
	
	
	
	public String toString()
	{
		String ret = "";
		for (double[] iarr: array)
		{
			ret = ret + iarr[0] + ", " + iarr[1] + "\n";
		}
		return ret;
	}
	
	
	
	public String toOneLinerString()
	{
		StringBuilder sb = new StringBuilder();
		for (double[] iarr: array)
		{
			if (iarr[0] <= 1.0)
			{
				sb.append(iarr[0] + "," + iarr[1] + " ; ");
			}
		}
		return sb.toString();
	}
	
	
	
	public String toStringForFile()
	{
		StringBuilder sb = new StringBuilder();
		for (double[] iarr: array)
		{
			sb.append(iarr[0] + ", " + iarr[1] + ";");
		}
		return sb.toString();
	}
	
	
	public double[][] getParametersForJSON()
	{
		return array;
	}
	
	
	
// privates -------------------------------------------------------
	
	private void setUP()
	{
		end[0] = 1;
		end[1] = 1;
	}
	
	
	
	private void setDOWN(){
		end[0] = 1;
		end[1] = -1;
	}
	
	
	
	private void setUPDOWN()
	{
		point1[0] = 0.5;
		point1[1] = 1;
		sort();
	}
	
	
	
	private void setDOWNUP()
	{
		point1[0] = 0.5;
		point1[1] = -1;
		sort();
	}
	
	
	
	private void setSTRAIGHT ()
	{
		// Actually don't change anything
		
	}
	
	
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int UPDOWN = 2;
	public static final int DOWNUP = 3;
	public static final int STRAIGHT = 4;
	
}
