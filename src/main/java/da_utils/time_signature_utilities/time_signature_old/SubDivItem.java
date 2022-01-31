package main.java.da_utils.time_signature_utilities.time_signature_old;

import java.util.ArrayList;
import java.util.HashMap;

import main.java.da_utils.combo_variables.IntAndInt;

/**
 * class to represent hierarchical subdivisions in timesignatures
 * 
 * @author dougr
 *
 */
public class SubDivItem {
	
	//------------------------
	// STATIC VARIABLES
	//------------------------
	
	private static final String INDENT_STRING = "   ";
	
	//------------------------
	// MEMBER VARIABLES
	//------------------------

	//SubDivItem Attributes
	private int length;
	private int denominator;
	private int priority;
	private ArrayList<SubDivItem> sdiList = new ArrayList<SubDivItem>();
	private SubDivItem parent;
	
	//Helper Variables
	  private boolean canSetLength = false;
	  private boolean canSetDenominator = false;
	  private boolean canSetPriority = false;

	//------------------------
	// CONSTRUCTOR
	//------------------------

	public SubDivItem(int length, int denominator, int priority, SubDivItem[] sdiArr) {
		this.length = length;
		this.denominator = denominator;
		addToSDIList(sdiArr);
		this.priority = priority;
		parent = null;
	}
	
	public SubDivItem(int length, int denominator, int priority) {
		this.length = length;
		this.denominator = denominator;
		this.priority = priority;
		parent = null;
	}
	public SubDivItem() {
		canSetLength = true;
		canSetDenominator = true;
		canSetPriority = true;
		parent = null;
	}
	
	//------------------------
	// PRIVATES
	//------------------------
	
	private void addToSDIList(SubDivItem[] sdiArr) {
		for (SubDivItem sdi: sdiArr) {
			sdiList.add(sdi);
		}		
	}	
	
	//------------------------
	// INTERFACE
	//------------------------
	
	public void setParent(SubDivItem sdi) {
		parent = sdi;
	}
	
	public void addToSDIList(SubDivItem sdi) {
		sdiList.add(sdi);	
	}	
	
	/* Code from template attribute_SetImmutable */
	  public boolean setLength(int aLength)
	  {
	    boolean wasSet = false;
	    if (!canSetLength) { return false; }
	    canSetLength = false;
	    length = aLength;
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
	  
	  /* Code from template attribute_SetImmutable */
	  public boolean setPriority(int aPriority)
	  {
	    boolean wasSet = false;
	    if (!canSetPriority) { return false; }
	    canSetPriority = false;
	    priority = aPriority;
	    wasSet = true;
	    return wasSet;
	  }
	  
	  public SubDivItem getParent() {
		  return parent;
	  }
	  
	  

	  public int getLength()
	  {
	    return length;
	  }

	  public int getDenominator()
	  {
	    return denominator;
	  }

	  public int getPriority()
	  {
	    return priority;
	  }
	  
	  public SubDivItem[] getSubDivArr() {
		  SubDivItem[] newList = sdiList.toArray(new SubDivItem[sdiList.size()]);
		  return newList;
	  }
	  
	  public ArrayList<SubDivItem> getSubDivs(){
		  ArrayList<SubDivItem> list = new ArrayList<SubDivItem>();
		  if (hasSubDivs()) {
			  for (SubDivItem sdi: sdiList) {
				  list.addAll(sdi.getSubDivs());
			  }
		  } else {
			  list.add(this);
		  }
		  return list;
	  }

	  public boolean hasSubDivs() {
		if (sdiList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void delete()
	  {}


	  public String toString(){
		  return toString("");
	  }
	  
	  private String toString(String indent) {
		  String str = indent + "SubDivItem" + "["+
		            "length" + ":" + getLength()+ "," +
		            "denominator" + ":" + getDenominator()+ "," +
		            "priority" + ":" + getPriority()+ "]";
			  for (SubDivItem sdi: sdiList) {
				  str += "\n" + sdi.toString(INDENT_STRING + indent);
			  }
			  return str;
	  }
	  
	  // This is level 0 breakpoints
		public ArrayList<Double> getBreakpoints() {
			ArrayList<Double> list = new ArrayList<Double>();
			
			if (sdiList.size() > 0) {
				double pos = 0.0;
				for (SubDivItem sdi: getSubDivArr()) {
					for (double d: sdi.getBreakpoints()) {
						list.add(pos + d);
					}
					pos += sdi.getLengthInQuarters();
				}
			} else {
				list.add(getLengthInQuarters());
			}
			
			return list;
		}

		public double getLengthInQuarters() {
			return (double) length * 4.0 / denominator;
		
		}
		
		// this one returns all terminal (sdiList.size() == 0) items split more or less in half favouring the longer half in the beginning or equal splits
		
		public ArrayList<Double> getLevel1BreakPoints() {
			ArrayList<Double> list = new ArrayList<Double>();
			if (sdiList.size() == 0) {
				double d = Math.round((double)length / 2.0 + 0.49);
				double d2 = d * 4 / denominator;
				list.add(d2);
			} else {
				double pos = 0.0;
				for (SubDivItem sdi: sdiList) {
					for (Double dd: sdi.getLevel1BreakPoints()) {
						list.add(dd + pos);
					}					
					pos += sdi.getLengthInQuarters();
				}
			}
			
			return list;
		}
		
		// level 2 are all the denominators except the breakpoints. Level1 breakpoints included
		public ArrayList<Double> getLevel2BreakPoints() {
			ArrayList<Double> list = new ArrayList<Double>();
			if (sdiList.size() == 0) {
				double denomLength = 4.0 / denominator;
				for (int i = 1; i < length; i++) {
					list.add(denomLength * i);
				}
				
			} else {
				double pos = 0.0;
				for (SubDivItem sdi: sdiList) {
					for (Double dd: sdi.getLevel2BreakPoints()) {
						list.add(dd + pos);
					}					
					pos += sdi.getLengthInQuarters();
				}
			}
			
			return list;
		}
		
		
		// level 4 are all the denominators divided by 2 except the breakpoints. Level 2 and 3 breakpoints included
		// an optimization would be maybe to return lists without higher levels in
		public ArrayList<Double> getLevel3BreakPoints() {
			ArrayList<Double> list = new ArrayList<Double>();
			if (sdiList.size() == 0) {
				double denomLength = 4.0 / denominator / 2;
				for (double d = denomLength; d < getLengthInQuarters(); d += denomLength) {
					list.add(d);
				}
				
			} else {
				double pos = 0.0;
				for (SubDivItem sdi: sdiList) {
					for (Double dd: sdi.getLevel3BreakPoints()) {
						list.add(dd + pos);
					}					
					pos += sdi.getLengthInQuarters();
				}
			}
			
			return list;
		}		
		
		
		// 0 - breakpoints by subdivitem
		// 1 - divided subdiv items
		// 2 - denominator			quarter in denominator 4
		// 3 - denominator / 2		8th ditto
		// 4 - denominator / 4		16th ditto
		// 5 - denominator / 8		32nd ditto
		// 6 - denominator / 16		64th ditto
		// 7 - denominator / 32		128th ditto
		// 8 - denominator / 64		256th ditto
		// 9 - denominator / 128	512th ditto
		// 10 - denominator / 256	1028th ditto

		public ArrayList<Double> getBreakPointsByLevel(int i){
			if (i == 0) return getBreakpoints();
			if (i == 1) return getLevel1BreakPoints();
			return breakpointByLevelCalculation(i);
			
		}
		
		// calculates breakpoints from level. only supposed to work from level 2 upwards, so if you put in anything else, you take what you get.
		private ArrayList<Double> breakpointByLevelCalculation(int i) {
			ArrayList<Double> list = new ArrayList<Double>();
			double divisor = Math.pow(2.0, i - 2.0);
			if (sdiList.size() == 0) {
				double denomLength = 4.0 / denominator / divisor;
				for (double d = denomLength; d < getLengthInQuarters(); d += denomLength) {
					list.add(d);
				}
				
			} else {
				double pos = 0.0;
				for (SubDivItem sdi: sdiList) {
					for (Double dd: sdi.getBreakPointsByLevel(i)) {
						list.add(dd + pos);
					}					
					pos += sdi.getLengthInQuarters();
				}
			}
			
			return list;
		}
		
		
		// Clapham levels all levels of the SubDivItem
		public ArrayList<Double[]> claphamLevels() {
			ArrayList<Double[]> list = new ArrayList<Double[]>();
			Double[] arr = new Double[sdiList.size()];
			double pos = 0.0;
			for (int i = 0; i < sdiList.size(); i++) {
				arr[i] = pos + sdiList.get(i).getLengthInQuarters();
				pos += arr[i];
			}
			list.add(arr);
			pos = 0.0;
			ArrayList<Double> dList = new ArrayList<Double>();
			for (SubDivItem sdi: sdiList) {
				ArrayList<Double[]> zzz = sdi.claphamLevels();
				
				for (Double[] dd: zzz) {
					if (dd.length > 0) {
						for (int j = 0; j < dd.length; j++) {
							dList.add(dd[j] + pos);
						}						
					}
				}
				pos += sdi.getLengthInQuarters();
			}
			list.add(dList.toArray(new Double[dList.size()]));
			
			return list;
		}
		
		
		// not working yet.....................
		public HashMap<Integer, ArrayList<Double>> getLengths(int level)	// level indicates the level of the SubDivItem being called. Does not relate to the final level, where the tactus will be 0
		{
			HashMap<Integer, ArrayList<Double>> map = new HashMap<Integer, ArrayList<Double>>();
			ArrayList<Double> thisLength = new ArrayList<Double>();
			thisLength.add(getLengthInQuarters());
			map.put(level, thisLength);
			ArrayList<Double> nextLength = new ArrayList<Double>();
//			HashMap<Integer, ArrayList<Double>> tempMap = new HashMap<Integer, ArrayList<Double>>();
			
			if (sdiList.size() > 0) 
			{
				addSDIListToMap(level, map); 
			}
			else 
			{
				doLevel1SplitOnTerminalSDI(level, map);
			}
			return map;
		}

		/**
		 * @param level
		 * @param map
		 */
		private void doLevel1SplitOnTerminalSDI(int level, HashMap<Integer, ArrayList<Double>> map) {
			ArrayList<Double> forMap = new ArrayList<Double>();
			ArrayList<Double> list = getLevel1BreakPoints();
			Double pos = 0.0;
			Double max =0.0;
			for (Double d: list)
			{
				Double len = d - pos;
				forMap.add(d - pos);
				if (len > max) max = len;
				pos += d;
			}
			forMap.add(getLengthInQuarters() - pos);
			if (max > 1.5)
			{
//				subDivideListFurther..SubDivItem.class..SubDivItem.
			}
			map.put(level + 1, forMap);
		}

		/**
		 * @param level
		 * @param map
		 */
		private void addSDIListToMap(int level, HashMap<Integer, ArrayList<Double>> map) {
			for (SubDivItem sdi : sdiList) {
				HashMap<Integer, ArrayList<Double>> subMap = sdi.getLengths(level + 1);
				for (Integer key : subMap.keySet()) {
					if (map.containsKey(key)) {
						map.get(key).addAll(subMap.get(key));
					} else {
						map.put(key, subMap.get(key));
					}
				}
			}
		}

// --------------main -------------------------------------------------
		
	  

	public static void main(String[] args) {
//		  SubDivItem sdi = new SubDivItem(
//				  13, 4, 0,
//				  new SubDivItem[] {
//						  new SubDivItem(7, 4, 1,
//								  new SubDivItem[] {
//										  new SubDivItem(4, 4, 3),
//										  new SubDivItem(3, 4, 5)
//								  }),
//						  						  
//						  new SubDivItem(6, 4, 2,
//								  new SubDivItem[] {
//										  new SubDivItem(4, 4, 4),
//										  new SubDivItem(2, 4, 6)
//						  		})
//				  });
		SubDivItem sdi = new SubDivItem
				(13, 4, 0, new SubDivItem[] 
						{
						new SubDivItem(7, 4, 1, new SubDivItem[]
								{
								new SubDivItem(4, 4, 3, new SubDivItem[] 
										{
										new SubDivItem(3, 8, 7),
										new SubDivItem(3, 8, 10),
										new SubDivItem(2, 8, 12)
										}),
								new SubDivItem(3, 4, 5, new SubDivItem[]
										{
										new SubDivItem(2, 4, 9),
										new SubDivItem(1, 4, 13)		
										})
								}),
						new SubDivItem(6, 4, 2, new SubDivItem[]
								{
								new SubDivItem(4, 4, 4, new SubDivItem[] 
										{
										new SubDivItem(3, 8, 8),
										new SubDivItem(3, 8, 11),
										new SubDivItem(2, 8, 3)
												}),
								new SubDivItem(2, 4, 6)
								}),
						}
				);
		
		ArrayList<Double[]> list = sdi.claphamLevels();
		for (Double[] darr: list) {
			String str = "";
			for (Double d: darr) {
				str += d + ",";
			}
			System.out.println(str);
		}
		
//		  SubDivItem sdi = new SubDivItem(7, 4, 0, new SubDivItem[] {
//				  new SubDivItem(4, 4, 1),
//				  new SubDivItem(3, 4, 2)
//		  });
//		  SubDivItem sdi = new SubDivItem(7, 8, 0, new SubDivItem[] {
//				  new SubDivItem(3, 8, 1),
//				  new SubDivItem(2, 8, 2),
//				  new SubDivItem(2, 8, 2)
//		  });
//		  SubDivItem sdi = new SubDivItem(5, 8, 0);
		  
		  
//		  System.out.println(sdi.toString());
//		  for (Double d: sdi.getBreakpoints()) {
//			  System.out.println(d);
//		  }
//		  System.out.println("-------------");
//		  for (Double d: sdi.getLevel1BreakPoints()) {
//			  System.out.println(d);
//		  }
//		  System.out.println("-------------");
//		  for (Double d: sdi.getLevel2BreakPoints()) {
//			  System.out.println(d);
//		  }
//		  System.out.println("-------------");
//		  for (Double d: sdi.getLevel3BreakPoints()) {
//			  System.out.println(d);
//		  }
//		  System.out.println("-------------");
//		  for (int i = 0; i < 11; i++) {
//			  System.out.println("-------------");
//			  for (Double d: sdi.getBreakPointsByLevel(i)) {
//				  System.out.println(d);
//			  }
//		  }
	  }

	


}
