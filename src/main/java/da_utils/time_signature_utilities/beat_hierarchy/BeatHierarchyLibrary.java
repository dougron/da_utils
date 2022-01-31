package main.java.da_utils.time_signature_utilities.beat_hierarchy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


/**
 * Library of BeatHierarchyItems. Used so these items do not need to be regenerated constantly
 * 
 * Still requires testing. Can't remember its dependants
 * 
 * @author dougr
 *
 */
public class BeatHierarchyLibrary {

	private static HashMap<String, BeatHierarchyItem> bhMap = new HashMap<String, BeatHierarchyItem>();

	
	public BeatHierarchyLibrary(){
		
	}
	
	public static BeatHierarchyItem getBeatHierarchyItem(int signature_numerator, int signature_denominator, int[] subdivision){
		String key = makeKey(signature_numerator, signature_denominator);
		if (!bhMap.containsKey(key)){
			bhMap.put(key, new BeatHierarchyItem(weightingMap(subdivision), signature_numerator, signature_denominator));
		} 
		return bhMap.get(key);
	}
	public static BeatHierarchyItem getBeatHierarchyItem(int signature_numerator, int signature_denominator){
		// if you use this method for a complex time signature you take what you get, okay......
		String key = makeKey(signature_numerator, signature_denominator);
		int[] subdivision = new int[]{signature_numerator * 4 / signature_denominator}; //to cater for 2/2 
		if (!bhMap.containsKey(key)){
			bhMap.put(key, new BeatHierarchyItem(weightingMap(subdivision), signature_numerator, signature_denominator));
		} 
		return bhMap.get(key);
	}
	private static String makeKey(int signature_numerator, int signature_denominator) {
		String str = Integer.toString(signature_numerator) + "/" + Integer.toString(signature_denominator);

		return str;
	}
	public static TreeMap<Double, Integer> weightingMap(int[] subdivision){
		TreeMap<Double, Integer> wMap = new TreeMap<Double, Integer>();
		ArrayList<TimeSigSubdivItem> itemList = makeMainSubdivItems(subdivision); 
		itemList = addFourFourOffBeat(itemList);
		itemList = addPrimaryWeakQuarters(itemList);
		itemList = addSecondaryWeakBeats(itemList);
		itemList = addOffEighths(itemList);
		itemList = addOffSixteenths(itemList);
		
		setWeightings(itemList);
		
		for (TimeSigSubdivItem tssi: itemList){
			wMap.put(tssi.startPos, tssi.weight);
		}
	
		return wMap;
		
	}
	private static ArrayList<TimeSigSubdivItem> addOffSixteenths(ArrayList<TimeSigSubdivItem> itemList) {
		ArrayList<TimeSigSubdivItem> main = new ArrayList<TimeSigSubdivItem>();
		ArrayList<TimeSigSubdivItem> extra = new ArrayList<TimeSigSubdivItem>();
		for (TimeSigSubdivItem tssi: itemList){
			main.add(new TimeSigSubdivItem(tssi.startPos, 1));
			extra.add(new TimeSigSubdivItem(tssi.startPos + 0.25, 1));
		}
		main.addAll(extra);
		return main;
	}
	private static ArrayList<TimeSigSubdivItem> addOffEighths(ArrayList<TimeSigSubdivItem> itemList) {
		ArrayList<TimeSigSubdivItem> main = new ArrayList<TimeSigSubdivItem>();
		ArrayList<TimeSigSubdivItem> extra = new ArrayList<TimeSigSubdivItem>();
		for (TimeSigSubdivItem tssi: itemList){
			main.add(new TimeSigSubdivItem(tssi.startPos, 1));
			extra.add(new TimeSigSubdivItem(tssi.startPos + 0.5, 1));		// at this point the length is unimportant
		}
		main.addAll(extra);
		return main;
	}
	private static ArrayList<TimeSigSubdivItem> addSecondaryWeakBeats(ArrayList<TimeSigSubdivItem> itemList) {
		ArrayList<TimeSigSubdivItem> main = new ArrayList<TimeSigSubdivItem>();
		ArrayList<TimeSigSubdivItem> extra = new ArrayList<TimeSigSubdivItem>();
		for (TimeSigSubdivItem tssi: itemList){
			if (tssi.length > 1){
				main.add(new TimeSigSubdivItem(tssi.startPos, 1));
				extra.add(new TimeSigSubdivItem(tssi.startPos + 1.0, tssi.length - 1));
			} else {
				main.add(tssi);
			}
			
		}
		main.addAll(extra);
		return main;
	}
	private static ArrayList<TimeSigSubdivItem> addPrimaryWeakQuarters(ArrayList<TimeSigSubdivItem> itemList) {
		ArrayList<TimeSigSubdivItem> main = new ArrayList<TimeSigSubdivItem>();
		ArrayList<TimeSigSubdivItem> extra = new ArrayList<TimeSigSubdivItem>();
		for (TimeSigSubdivItem tssi: itemList){
			main.add(new TimeSigSubdivItem(tssi.startPos, 1));
			extra.add(new TimeSigSubdivItem(tssi.startPos + 1.0, tssi.length - 1));
		}
		main.addAll(extra);
		return main;
	}
	private static void setWeightings(ArrayList<TimeSigSubdivItem> itemList){
		int weight = 0;
		for (TimeSigSubdivItem tssi: itemList){
			tssi.setWeight(weight);
			weight++;
		}
	}
	private static ArrayList<TimeSigSubdivItem> addFourFourOffBeat(ArrayList<TimeSigSubdivItem> itemList) {
		ArrayList<TimeSigSubdivItem> main = new ArrayList<TimeSigSubdivItem>();
		ArrayList<TimeSigSubdivItem> extra = new ArrayList<TimeSigSubdivItem>();
		for (TimeSigSubdivItem tssi: itemList){
			if (tssi.length == 4){
				main.add(new TimeSigSubdivItem(tssi.startPos, 2));
				extra.add(new TimeSigSubdivItem(tssi.startPos + 2.0, 2));
			} else {
				main.add(tssi);
			}
		}
		main.addAll(extra);
		return main;
		
	}
	private static ArrayList<TimeSigSubdivItem> makeMainSubdivItems(int[] subdivision) {
		ArrayList<TimeSigSubdivItem> itemList = new ArrayList<TimeSigSubdivItem>();
		double subdivPos = 0.0;
		for (int subdiv: subdivision){
			itemList.add(new TimeSigSubdivItem(subdivPos, subdiv));
			subdivPos += subdiv;
		}
		ArrayList<TimeSigSubdivItem> mainSubdivList = sortAccordingToBeatHierarchy(itemList);

		return mainSubdivList;
	}
	private static ArrayList<TimeSigSubdivItem> sortAccordingToBeatHierarchy(ArrayList<TimeSigSubdivItem> sourceList){
		ArrayList<TimeSigSubdivItem[]> aList = new ArrayList<TimeSigSubdivItem[]>();
		ArrayList<TimeSigSubdivItem[]> bList = new ArrayList<TimeSigSubdivItem[]>();
		ArrayList<TimeSigSubdivItem[]> aList2 = new ArrayList<TimeSigSubdivItem[]>();
		ArrayList<TimeSigSubdivItem[]> bList2 = new ArrayList<TimeSigSubdivItem[]>();
		boolean lengthtest = true;
		aList.add(sourceList.toArray(new TimeSigSubdivItem[sourceList.size()]));
		while (lengthtest){
			lengthtest = false;
			for (TimeSigSubdivItem[] iarr: aList){
				if (iarr.length == 1){
					aList2.add(iarr);
				} else {
					TimeSigSubdivItem[][] iarrarr = splitArr(iarr);
					if (iarrarr != null){
						if (iarrarr[0].length > 1 || iarrarr[1].length > 1){
							lengthtest = true;
						}
						aList2.add(iarrarr[0]);
						bList2.add(iarrarr[1]);
					}
				}
			}
			for (TimeSigSubdivItem[] iarr: bList){
				if (iarr.length == 1){
					aList2.add(iarr);
				} else {
					TimeSigSubdivItem[][] iarrarr = splitArr(iarr);
					if (iarrarr != null){
						if (iarrarr[0].length > 1 || iarrarr[1].length > 1){
							lengthtest = true;
						}
						aList2.add(iarrarr[0]);
						bList2.add(iarrarr[1]);
					}
				}
			}
			aList = aList2;
			bList = bList2;
			aList2 = new ArrayList<TimeSigSubdivItem[]>();
			bList2 = new ArrayList<TimeSigSubdivItem[]>();
		}
		aList.addAll(bList);
		ArrayList<TimeSigSubdivItem> finalList = new ArrayList<TimeSigSubdivItem>();
		for (TimeSigSubdivItem[] iarr: aList){
			finalList.add(iarr[0]);
		}
		return finalList;
	}
	private static TimeSigSubdivItem[][] splitArr(TimeSigSubdivItem[] arr) {
		int splitPoint = arr.length / 2;
		
		if (splitPoint == 0){
			return null;
		} else {
			TimeSigSubdivItem[] part1 = new TimeSigSubdivItem[splitPoint];
			TimeSigSubdivItem[] part2 = new TimeSigSubdivItem[arr.length - splitPoint];
			System.arraycopy(arr, 0, part1, 0, splitPoint);
			System.arraycopy(arr, splitPoint, part2, 0, arr.length - splitPoint);
			return new TimeSigSubdivItem[][]{part1, part2};
		}
		
		
	}
}
