package main.java.da_utils.resource_objects;

import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterSortInterface;

/*
 * container for data from the contour panel in the max patch for DAPlay
 */
public class ContourData implements FilterSortInterface{
	
	public double midPoint = 0.5;
	public double endPoint = 0.0;
	public int phraseLength = 0;		// this is a multiple of the chord progression length. add 1. 
	public int highOrLow = 0;			// 0 = high midpoint, 1 = low midpoint
	private static int ID = 0;
	public String name;
	
	public ContourData(){
		name = contourName() + ID;
		ID++;
	}
	public void newData(double midPoint, double endPoint, int phraseLength, int highOrLow){
		this.midPoint = midPoint;
		this.endPoint = endPoint;
		this.phraseLength = phraseLength;
		this.highOrLow = highOrLow;
		name = contourName() + ID;
	}
	public String toString(){
		String ret = "ContourData:-----";
		ret = ret + "\nmidPoint:     " + midPoint;
		ret = ret + "\nendPoint:     " + endPoint;
		ret = ret + "\nphraseLength: " + phraseLength;
		ret = ret + "\nhighOrLow:    " + highOrLow;
		return ret + "\n";
	}
	public FilterObject getFilterObject(){
		return new FilterObject(name, this);
	}
	
// FilterSortMethod --------------------------------------------------------
	public FilterSortInterface getFilteSortObject(){
		return this;
	}
	
// privates ----------------------------------------------------------------
	
	private String contourName(){
		//System.out.println("highOrLow=" + highOrLow);
		String ret = "";
		if (highOrLow == HIGH_MIDPOINT){
			ret += "Up";
			if (midPoint < 1.0){
				ret += "Down";
			}
		} else {
			ret += "Down";
			if (midPoint < 1.0){
				ret += "Up";
			}
		}
		return ret;
	}
	
	public static final int HIGH_MIDPOINT = 0;
	public static final int LOW_MIDPOINT = 1;
}
