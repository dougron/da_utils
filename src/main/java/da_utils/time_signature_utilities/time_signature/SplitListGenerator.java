package main.java.da_utils.time_signature_utilities.time_signature;

/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/


import java.util.*;

import main.java.da_utils.time_signature_utilities.greatest_common_factor_calculator.GreatestCommonFactor;

// line 9 "SuperTactusList.ump"
public class SplitListGenerator implements SplitListSource
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //SplitListGenerator Attributes
  private double length;
  private double increment;
  private List<Double> splitList;
  private double gcf;

  //SplitListGenerator Associations
//  private List<SplitListSource> parentList;
  
  //------------------------
  // HELPER VARIABLES
  //------------------------
  
  private boolean hasSplitList;
  private boolean hasGcf;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SplitListGenerator(double aLength, double aIncrement)
  {
    length = aLength;
    increment = aIncrement;
    splitList = new ArrayList<Double>();
//    parentList = aList;
    hasSplitList = false;
  }

  //------------------------
  // INTERFACE
  //------------------------
  
  
  public double getGcf()
  {
	  if (!hasGcf)
	  {
		  gcf = GreatestCommonFactor.gcf(getSplitList().toArray(new Double[numberOfSplitList()]));
		  hasGcf = true;
	  }
	  return gcf;
  }

  	public double getLength()
  	{
  		return length;
  	}
  	
  	
  	// this is not strictly accurate, but takes into account splits that have been removed due to existing on a higher plane
  	@Override
	public double getAverageLength() 
  	{	
		return increment;
	}
  

  	public double getIncrement()
  	{
  		return increment;
  	}
  	
 

  	public List<Double> getSplitList()
  	{
  		if (!hasSplitList)
    	{
    	makeSplitList();
    	hasSplitList = true;
    	}
  		return splitList;
  	}
  	
  

  	private void makeSplitList() 
  	{
//		Set<Double> avoidPoints = makeAvoidPoints();
		splitList.clear();
		for (Double d = increment; d <= length; d += increment * 2)	// we don't start on 0.0 cos we never need to split a note there
																	// * 2 to avoid duplicating higher level splits which could go an being assesed till they succeeded
		{
			splitList.add(d);
		}
  	}
  	
  	

//	private Set<Double> makeAvoidPoints() {
//		Set<Double> set = new TreeSet<Double>();
//		for (SplitListSource sls: parentList)
//		{
//			for (Double d: sls.getSplitList())
//			{
//				set.add(d);
//			}
//		}
//		return set;
//	}

	
	
	public int numberOfSplitList()
	{
		int number = splitList.size();
		return number;
	}
  

	
	public boolean hasSplitList()
	{
		boolean has = splitList.size() > 0;
		return has;
	}
  
	

	public int indexOfSplitList(Double aSplitList)
	{
		int index = splitList.indexOf(aSplitList);
		return index;
	}
  
	

	public void delete()
	{
	}
	


	public String toString()
	{
		String str = super.toString() + "\n["+
            "\nlength" + ":" + getLength()+ "," +
            "increment" + ":" + getIncrement();
		str += "\nsplitList: ";
		for (Double d: getSplitList())
		{
			str += d + ", ";
		}	
		str += "\ngcf=" + getGcf();
		str += "\navgLength=" + getAverageLength();
		str += "\n]";
		return str;
	}

	
}