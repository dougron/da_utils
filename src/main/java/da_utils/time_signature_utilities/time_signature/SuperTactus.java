/**
 * 
 */
package main.java.da_utils.time_signature_utilities.time_signature;

import java.util.ArrayList;

/**
 * For use with the TactusTimeSignature. This wraps a group of tactii. The important thing we get out is the lengthInQuarters
 * 
 * @author dougr
 *
 */
public class SuperTactus {

	
	//------------------------
	// MEMBER VARIABLES
	//------------------------
	
	private int level;
	private ArrayList<Double> tactii;
	private double lengthInQuarters;
	
	//------------------------
	// CONSTRUCTOR
	//------------------------
	
	public SuperTactus(int level)
	{
		this.setLevel(level);
		tactii = new ArrayList<Double>();
		lengthInQuarters = 0.0;
	}

	
	public int getLevel() 
	{
		return level;
	}

	
	public void setLevel(int level) 
	{
		this.level = level;
	}

	
	public ArrayList<Double> getTactii() 
	{
		return tactii;
	}

	
	public int getTactusCount()
	{
		return tactii.size();
	}
	
	
	
	public void setTactii(ArrayList<Double> tactii) 
	{
		this.tactii = tactii;
	}

	
	public double getLengthInQuarters() 
	{
		return lengthInQuarters;
	}
	
	
	public void addTactusLengthItem(Double d) 
	{
		tactii.add(d);
		lengthInQuarters = makeLengthInQuarters();
	}

	
	private double makeLengthInQuarters() 
	{
		double sum = 0.0;
		for (Double d: tactii)
		{
			sum += d;
		}
		return sum;
	}
	
	public String toString()
	{
		String str = "SuperTactus: lengthInQuarters=" + lengthInQuarters + " ";
		for (Double d: tactii)
		{
			str += d + ",";
		}
		return str;
	}
}
