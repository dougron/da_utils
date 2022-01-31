package main.java.da_utils.resource_objects.chance_item;
import java.util.Comparator;

public class ChanceItem {
	
	public double chance;
	public double item;

	public ChanceItem(double item, double chance){
		this.item = item;
		this.chance = chance;
	}
	public static Comparator<ChanceItem> longestToShortestItemComparator = new Comparator<ChanceItem>(){
		public int compare(ChanceItem ci1, ChanceItem ci2){
			if (ci1.item < ci2.item) return 1;
			if (ci1.item > ci2.item) return -1;
			return 0;
		}
	};
}
