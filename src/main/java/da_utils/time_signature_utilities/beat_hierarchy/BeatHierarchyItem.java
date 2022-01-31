package main.java.da_utils.time_signature_utilities.beat_hierarchy;


import java.util.TreeMap;

/**
 * an item that is added to the BeatHierarchyLibrary, being the relative significance of the beats in a
 * certain time signature
 */

public class BeatHierarchyItem {
	
	private int signature_numerator;
	private int signature_denominator;
	private double length;
	private TreeMap<Double, Integer> weightMap;
	
	
	public BeatHierarchyItem(TreeMap<Double, Integer> map, int signature_numerator, int signature_denominator){
		this.weightMap = map;
		this.length = makeLength(signature_numerator, signature_denominator);
		this.signature_numerator = signature_numerator;
		this.signature_denominator = signature_denominator;
		
	}
	
	private double makeLength(int signature_numerator2, int signature_denominator2) {		
		return 4.0 / (double)signature_denominator2 * signature_numerator2;
	}
	public int signature_numerator(){
		return this.signature_numerator;
	}
	public int signature_denominator(){
		return this.signature_denominator;
	}
	public double length(){
		return length;
	}
	public Integer getWeighthing(double positionKey){
		if (weightMap.containsKey(positionKey)){
			return weightMap.get(positionKey);
		} else {
			return -1;
		}
	}
	public String toString(){
		String ret = "BeatHierarchyItem " + signature_numerator + "/" + signature_denominator;
		for (Double key: weightMap.keySet()){
			ret += "\n" + key + ":-  " + weightMap.get(key);
		}
		return ret;
	}
}
