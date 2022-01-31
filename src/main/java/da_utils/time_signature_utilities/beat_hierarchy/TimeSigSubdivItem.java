package main.java.da_utils.time_signature_utilities.beat_hierarchy;

import java.util.Comparator;

public class TimeSigSubdivItem {

	
	public double startPos;
	public int length;
	public int weight;

	public TimeSigSubdivItem(double startPos, int length){
		this.startPos = startPos;
		this.length = length;
//		this.weight = new int[length];
//		this.weight[0] = weight;
	}
	public void setWeight(int weight){
		this.weight = weight;
	}
	public static Comparator<TimeSigSubdivItem> weightComparator = new Comparator<TimeSigSubdivItem>(){
		public int compare(TimeSigSubdivItem item1, TimeSigSubdivItem item2){
			if (item1.weight < item2.weight) return -1;
			if (item1.weight > item2.weight) return 1;
			return 0;
		}
	};
	public String toString(){
		String ret = "TimeSigSubdivItem:-----------------------\n";
		ret += "startPos=" + startPos + " length=" + length + " weight=" + weight;
		
		return ret;
	}
}
