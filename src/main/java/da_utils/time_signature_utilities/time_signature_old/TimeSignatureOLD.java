package main.java.da_utils.time_signature_utilities.time_signature_old;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.time_signature_utilities.beat_hierarchy.BeatHierarchyItem;
import main.java.da_utils.time_signature_utilities.beat_hierarchy.BeatHierarchyLibrary;
import main.java.da_utils.time_signature_utilities.time_signature_map.TSMFromGen;
import main.java.da_utils.time_signature_utilities.time_signature_map.TSMapInterface;
import main.java.da_utils.time_signature_utilities.time_signature_map_generator.TSGenInterface;

/**
 * Inline with the idea that subdiv info MUST be captured for all time signatures, this class captures
 * the numerator, denominator and the subdivision info.
 * 
 * subdivision is all about dividing the timesignature into
 */
public class TimeSignatureOLD implements TSGenInterface{
	
	// static TimeSignatures for testing purposes.....
	
	public static TimeSignatureOLD FOUR_FOUR = new TimeSignatureOLD(4, 4, new int[] {2, 2});
	//public static TimeSignature FOUR_FOUR_GOEMA = new TimeSignature(4, 4, );	// requires subdivisions in 8ths or write as 8/8
	public static TimeSignatureOLD THREE_FOUR = new TimeSignatureOLD(3, 4, new int[] {3});
	//public static TimeSignature TWO_FOUR = new TimeSignature(2, 4);
	//public static TimeSignature FIVE_FOUR_32 = new TimeSignature(5, 4, new int[] {3, 2});
	//public static TimeSignature FOUR_FOUR_23 = new TimeSignature(5, 4, new int[] {2, 3});
	//
	public static TimeSignatureOLD FIVE_EIGHT_32 = new TimeSignatureOLD(5, 8, new int[] {3, 2});
	//public static TimeSignature FIVE_EIGHT_23 = new TimeSignature(5, 8, new int[] {2, 3});
	//
	//public static TimeSignature SEVEN_EIGHT_322 = new TimeSignature(7, 8, new int[] {3, 2, 2});
	//public static TimeSignature SEVEN_EIGHT_223 = new TimeSignature(7, 8, new int[] {2, 2, 3});
	//public static TimeSignature SEVEN_EIGHT_232 = new TimeSignature(7, 8, new int[] {2, 3, 2});
	//
	//public static TimeSignature EIGHT_EIGHT_332 = new TimeSignature(8, 8, new int[] {3, 3, 2});
	public static TimeSignatureOLD THIRTEEN_FOUR_CLAPHAM = new TimeSignatureOLD(13, 4, 
			new SubDivItem(13, 4, 0, new SubDivItem[] {
					new SubDivItem(7, 4, 1, new SubDivItem[] {
							new SubDivItem(4, 4, 3, new SubDivItem[] {
									new SubDivItem(2, 4, 7),
									new SubDivItem(2, 4, 9)
							}),
							new SubDivItem(3, 4, 5)
					}),
					new SubDivItem(6, 4, 2, new SubDivItem[] {
							new SubDivItem(4, 4, 4, new SubDivItem[] {
									new SubDivItem(2, 4, 8),
									new SubDivItem(2, 4, 10)
							}),
							new SubDivItem(2, 4, 6)
					})
			})
			);
	public static TimeSignatureOLD THIRTEEN_FOUR_CLAPHAM_CONSISTENT_SUBDIV_DEPTH = new TimeSignatureOLD(13, 4, 
			new SubDivItem(13, 4, 0, new SubDivItem[] {
					new SubDivItem(7, 4, 1, new SubDivItem[] {
							new SubDivItem(4, 4, 3),
							new SubDivItem(3, 4, 5)
					}),
					new SubDivItem(6, 4, 2, new SubDivItem[] {
							new SubDivItem(4, 4, 4),
							new SubDivItem(2, 4, 6)
					})
			})
			);

	
	//------------------------
	// MEMBER VARIABLES
	//------------------------

	int numerator;
	int denominator;
	private int[] subdivision;	// for now, we are only expecting 2,3 or 4 in here, also a max of 4 subdivisions
	private boolean hasSubdivisions = false;
//	private TreeMap<Double, Integer> weightMap;
//	private boolean hasWeightMap = false;
	private BeatHierarchyLibrary bhl = new BeatHierarchyLibrary();
	private SubDivItem sdi = null;
	
	 //Helper Variables
	  private boolean canSetSubDivItem;
	  private boolean canSetNumerator;
	  private boolean canSetDenominator;
	

	//------------------------
	// CONSTRUCTOR
	//------------------------
	
	public TimeSignatureOLD(int numerator, int denominator, int[] subdivision){
		this.numerator = numerator;
		this.denominator = denominator;
		this.subdivision = subdivision;
		hasSubdivisions = true;
	}
	
	public TimeSignatureOLD(int numerator, int denominator, SubDivItem sdi) {
		this.numerator = numerator;
		this.denominator = denominator;
		this.sdi = sdi;
	}
	
	public TimeSignatureOLD() {
		// for reading from xml
	    canSetNumerator = true;
	    canSetDenominator = true;
	    canSetSubDivItem = true;
	}
	
	//------------------------
	// INTERFACE
	//------------------------
	
	
	  /* Code from template attribute_SetImmutable */
	  public boolean setNumerator(int aNumerator)
	  {
	    boolean wasSet = false;
	    if (!canSetNumerator) { return false; }
	    canSetNumerator = false;
	    numerator = aNumerator;
	    wasSet = true;
	    return wasSet;
	  }
	  
	  /* Code from template attribute_SetImmutable */
	  public boolean setDenominator(int aDenominator)
	  {
	    boolean wasSet = false;
	    if (!canSetDenominator) { return false; }
	    canSetDenominator = false;
	    denominator = aDenominator;
	    wasSet = true;
	    return wasSet;
	  }
	  
	  public boolean setSubDivItem(SubDivItem aSubDivItem)
	  {
	    boolean wasSet = false;
	    if (!canSetSubDivItem) { return false; }
	    canSetSubDivItem = false;
	    sdi = aSubDivItem;
	    wasSet = true;
	    return wasSet;
	  }
	
	public int getNumerator() {
		return numerator;
	}
	
	public int getDenominator() {
		return denominator;
	}
	
	public double barpos(LiveMidiNote lmn){
		// returns the position in the bar
		return lmn.position % (numerator / denominator) * 4;		// to cater for 2/2 time
	}
	
	public BeatHierarchyItem weightingMap(){
		return bhl.getBeatHierarchyItem(numerator, denominator, subdivision);
		
	}
	
	@Override
	public TSMapInterface getTimeSignatureMap(int barCount) {
		TSMapInterface tsm = new TSMFromGen(barCount);
		// commenting this line out breaks some functionality so this will not work correctly but this class is in line for full depreciation, actually total annihilation
//		while (tsm.addTimeSignature(this)) {}
		return tsm;
	}
	
	public boolean isSameAs(TimeSignatureOLD ts) {
		if (ts != null && ts.numerator == numerator && ts.denominator == denominator) {
			if (ts.hasSubdivisions && hasSubdivisions && ts.subdivision.length == subdivision.length) {
				for (int i = 0; i < subdivision.length; i++) {
					if (ts.subdivision[i] != subdivision[i]) {
						return false;
					}
				}
				return true;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	public double getLengthInQuarters() {
		return numerator * 4.0 / denominator;
	}
	
	public SubDivItem getSubDivItem(){
		return sdi;
	}
	
	public String toString() {
		String str = nameToString();
		if (hasSubdivisions) {
			str += " ";
			for (int i: subdivision) {
				str += i + ".";
			}
		}
		if (sdi != null) str += "\n" + sdi.toString();
		return str;
	}
	
	public String nameToString() {
		return numerator + "/" + denominator;
	}
	
	public ArrayList<Double> getBreakpoints() {
		return sdi.getBreakpoints();
	}
	
	// this one returns all terminal (sdiList.size() == 0) items split more or less in half favouring the longer half in the beginning or equal splits
	public ArrayList<Double> getLevel1BreakPoints() {
		return sdi.getLevel1BreakPoints();
	}
	
	public ArrayList<Double> getLevel2BreakPoints() {
		return sdi.getLevel2BreakPoints();
	}
	
	public ArrayList<Double> getLevel3BreakPoints() {
		return sdi.getLevel3BreakPoints();
	}
	
	public ArrayList<Double> getBreakPoints(int level){
		return sdi.getBreakPointsByLevel(level);
	}
	
	public HashMap<Integer, ArrayList<Double>> getLevels()
	{
		return sdi.getLengths(0);	// 0 is the starting index, each nested SubDivItem returns its information at a higher index
	}
	
	
	
	
	public static void main(String[] args) {
//		TimeSignature ts = new TimeSignature(13, 4, new SubDivItem(4, 4, 0, new SubDivItem[] {
//			new SubDivItem(7, 4, 1, new SubDivItem[] {
//					new SubDivItem(4, 4, 3),
//					new SubDivItem(3, 4, 5)
//			}),
//			new SubDivItem(6, 4, 2, new SubDivItem[] {
//					new SubDivItem(4, 4, 4),
//					new SubDivItem(2, 4, 6)
//			})
//		}));
//		System.out.println(THIRTEEN_FOUR_CLAPHAM.toString());
//		ArrayList<Double> list = THIRTEEN_FOUR_CLAPHAM.getBreakpoints();
//		for (Double d: list) {
//			System.out.println(d);
//		}
		HashMap<Integer, ArrayList<Double>> map = THIRTEEN_FOUR_CLAPHAM_CONSISTENT_SUBDIV_DEPTH.getLevels();
		for (Integer key: map.keySet())
		{
			String str = "key=" + key + ": ";
			for (Double d: map.get(key))
			{
				str += d + ",";
			}
			System.out.println(str);
		}
		
	}
	

}
