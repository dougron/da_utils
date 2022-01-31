/**
 * 
 */
package main.java.da_utils.time_signature_utilities.time_signature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
//import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import main.java.da_utils.combo_variables.DoubleAndDouble;
import main.java.da_utils.time_signature_utilities.time_signature_map.TSMFromGen;
import main.java.da_utils.time_signature_utilities.time_signature_map.TSMapInterface;
import main.java.da_utils.time_signature_utilities.time_signature_map_generator.TSGenInterface;

/**
 * Another attempt to clarify time signatures, this time by clearly capturing the tactus, and then subdividing further.
 * 
 * Tactii are captured as Doubles, and Integers indicate other hierarchies
 * 
 * For this implementation, a tactus can be 1.0 or 1.5 quarters long, implying a subdivision by 2 or 3 respectively
 * 
 * If no other hierarchies are indicated, areas are divided in two with the long bit first if the item count is odd
 * 
 * To work correctly there must be a layer of subdivision that divides the tactus into groups of 2 or 3. At this 
 * point I am not going to make this idiot proof within this class. This is to facilitate the 3rd beat in 3 note splitting
 * problem.
 * 
 * @author dougr
 *
 */
public class TimeSignature  implements TSGenInterface{
	
	


	// while the musicxml_maker can actually do 1024th notes, the below limits the number of split lists 
	// made by TimeSignature instances for practicality sake. 16 = 1/16th of a quarter(length=1.0) so therefore a 64th note
	// for full resolution this should be 256.
	public static int splitListResolution = 4;
	
	
	
	
	public static final TimeSignature FIVE_EIGHT_32 = new TimeSignature("5/8", 5, 8, 0.5, new Object[] {1, 1.5, 1.0});
	public static final TimeSignature SIX_EIGHT = new TimeSignature("6/8", 6, 8, 0.5, new Object[] {1, 1.5, 1, 1.5});
	public static final TimeSignature SEVEN_EIGHT_322 = new TimeSignature("7/8_322", 7, 8, 0.5, new Object[] {1, 1.5, 1, 1.0, 1.0});
	public static final TimeSignature SEVEN_EIGHT_223 = new TimeSignature("7/8_223", 7, 8, 0.5, new Object[] {1, 1.0, 1.0, 1, 1.5});
	public static final TimeSignature NINE_EIGHT_333 = new TimeSignature("9/8_333", 9, 8, 0.5, new Object[] {1, 1.5, 1, 1.5, 1, 1.5});
	// could do many variation of 9/8
	public static final TimeSignature TEN_EIGHT_3322 = new TimeSignature("10/8_3322", 10, 8, 0.5, 
			new Object[] {
					1, 2, 
					1.5, 
					2, 
					1.5, 
					1, 2,
					1.0, 
					2, 
					1.0});
	public static final TimeSignature ELEVEN_EIGHT_3332 = new TimeSignature("11/8_3322", 11, 8, 0.5, 
			new Object[] {
					1, 2, 
					1.5, 
					2, 
					1.5, 
					1, 2,
					1.5, 
					2, 
					1.0});
	public static final TimeSignature TWELVE_EIGHT = new TimeSignature("12/8", 12, 8, 0.5, 
			new Object[] {
					1, 2, 
					1.5, 
					2, 
					1.5, 
					1, 2,
					1.5, 
					2, 
					1.5});
	
	
	public static final TimeSignature TWO_FOUR = new TimeSignature("2/4", 2, 4, 0.5, new Object[] {1, 1.0, 1.0});
	public static final TimeSignature THREE_FOUR = new TimeSignature("3/4", 3, 4, 0.5, new Object[] {1, 1.0, 1.0, 1.0});
	public static final TimeSignature FOUR_FOUR = new TimeSignature("4/4", 4, 4, 0.5, new Object[] {1, 1.0, 1.0, 1, 1.0, 1.0});
	public static final TimeSignature TWO_TWO = new TimeSignature("2/2", 2, 2, 1.0, new Object[] {1, 1.0, 1.0, 1, 1.0, 1.0});
	public static final TimeSignature FIVE_FOUR = new TimeSignature("5/4", 5, 4, 0.5,
			new Object[] {
					1,
					1.0, 1.0, 1.0,
					1,
					1.0, 1.0,
			});
	public static final TimeSignature SIX_FOUR_33 = new TimeSignature("6/4_33", 6, 4, 0.5,
			new Object[] {
					1,
					1.0, 1.0, 1.0,
					1,
					1.0, 1.0, 1.0
			});
	public static final TimeSignature SIX_FOUR_222 = new TimeSignature("6/4_222", 6, 4, 0.5,
			new Object[] {
					1,
					1.0, 1.0, 
					1,
					1.0, 1.0, 
					1,
					1.0, 1.0,
			});
	public static final TimeSignature SEVEN_FOUR_322 = new TimeSignature("7/4_322", 7, 4, 0.5,
			new Object[] {
					1,
					1.0, 1.0, 1.0,
					1,
					1.0, 1.0, 
					1,
					1.0, 1.0,
			});
	public static final TimeSignature EIGHT_FOUR = new TimeSignature("8/4", 8, 4, 0.5,
			new Object[] {
					1, 2,
					1.0, 1.0,
					2,
					1.0, 1.0,
					1, 2,
					1.0, 1.0,
					2, 
					1.0, 1.0,
			});
	public static final TimeSignature THIRTEEN_FOUR_CLAPHAM = new TimeSignature("13/4", 13, 4, 0.5, new Object[] 
			{
					1, 2,
					1.5, 1.5, 1.0, 
					2,
					1.0, 1.0, 1.0, 
					1, 2,
					1.5, 1.5, 1.0, 
					2,
					1.0, 1.0
			});	
	public static final HashMap<String, TimeSignature> searchableMap = new HashMap<String, TimeSignature>()
			{{
				
				
				put("5/8", FIVE_EIGHT_32);
				put("6/8", SIX_EIGHT);
				put("7/8_322", SEVEN_EIGHT_322);
				put("7/8_223", SEVEN_EIGHT_223);
				put("9/8_333", NINE_EIGHT_333);
				put("10/8_3322", TEN_EIGHT_3322);
				put("11/8_3332", ELEVEN_EIGHT_3332);
				put("12/8", TWELVE_EIGHT);
				
				put("2/4", TWO_FOUR);
				put("3/4", THREE_FOUR);
				put("4/4", FOUR_FOUR);
				put("2/2", TWO_TWO);
				
				put("5/4", FIVE_FOUR);
				put("6/4_33", SIX_FOUR_33);
				put("6/4_222", SIX_FOUR_222);
				put("7/4_322", SEVEN_FOUR_322);
				put("8/4", EIGHT_FOUR);
				put("13/4", THIRTEEN_FOUR_CLAPHAM);
			}};
	
	
	//----------------------------------
	// MEMBER VARIABLES
	//----------------------------------
	
	
	private ArrayList<Double> tactus;
	private ArrayList<Double> tactusInQuarters;
	private boolean hasTactusInQuarters = false;
	private ArrayList<Double> subTactusInQuarters;
	private boolean hasSubTactusInQuarters = false;
	private ArrayList<Double> superTactusInQuarters;
	private boolean hasSuperTactusAsQuarters;
	protected TreeMap<Integer, ArrayList<SuperTactus>> superTacMap;
	private int numerator;
	private int denominator;
	private ArrayList<SplitListSource> splitLists;
	private double subTactusLength;
	private Object[] setupObjectArray;
	private Set<Double> absolutelyDoNotCrossList = new HashSet<Double>();
	// DoubleAndDouble.d1 = minimum gcf to pass, d2 = minimum length to pass
	private TreeMap<Double, DoubleAndDouble> map = new TreeMap<Double, DoubleAndDouble>();		// this relates to note splitting for musicxml_maker
	private HashMap<Double, Integer> strengthMap = new HashMap<Double, Integer>();	
	
	//Helper Variables
	  private boolean canSetNumerator;
	  private boolean canSetDenominator;


	private String name;


	


	


	
	
	
	//----------------------------------
	// CONSTRUCTOR
	//----------------------------------
	
	
	// subtactus length is the unit below tactus, so subtactus for 4/4 is 0.5 
	// (i.e half of 1.0 which is the tactus) This is to cater for cut common time 
	// where the tactus is actually 2.0
	public TimeSignature(int aNumerator, int aDenominator, double aSubTactusLength, Object[] arr)	
	{
		name = aNumerator + "/" + aDenominator + "_custom";
		initialize(aNumerator, aDenominator, aSubTactusLength, arr);
	}
	
	
	
	// private constructor is for declaring TimeSignatures with names that appear in the searchableMap
	// custom time signatures will not appear in this map and therefore nee more complex parameterObjectArrays when 
	// saved to file.
	// subtactus length is the unit below tactus, so subtactus for 4/4 is 0.5 
	// (i.e half of 1.0 which is the tactus) This is to cater for cut common time 
	// where the tactus is actually 2.0
	private TimeSignature(String aName, int aNumerator, int aDenominator, double aSubTactusLength, Object[] arr)	
	{
		name = aName;
		initialize(aNumerator, aDenominator, aSubTactusLength, arr);
	}

	

	private void initialize(int aNumerator, int aDenominator, double aSubTactusLength, Object[] arr)
	{
		numerator = aNumerator;
		denominator = aDenominator;
		subTactusLength = aSubTactusLength;
		setupObjectArray = arr;
		makeTactus(arr);
		superTacMap = new TreeMap<Integer, ArrayList<SuperTactus>>();
		makeSuperTacMap(arr);
		splitLists = makeSplitLists();
		makeMap();
		strengthMap = makeStrengthMap();
	}
	
	
	//--------------------------------
	// PRIVATE METHODS
	//--------------------------------
	

// %%%%%%%%%%%%% crossing map criteria construction methods %%%%%%%%%%%%%%%%%%%%%%%%
	
	
	private HashMap<Double, Integer> makeStrengthMap()
	{
		HashMap<Double, Integer> strengthMap = new HashMap<Double, Integer>();
		for (Double key: map.keySet())
		{
			int strength = 0;
			if (map.get(key).getD2() < 999.0) {
				strength = (int)(1 / map.get(key).getD1());
			}
			strengthMap.put(key, strength);
		}
		return strengthMap;		
	}


	private void makeMap() 
	{
		makeSuperTactusItemsForMap();
		makeTactusItemsForMap();
		do3rdbeatInGroupOfEven3sAdjustment();
		makeSubTactusItemsForMap();
		makeSubSubTactusItemsForMap();
	}
	
	
	
	private void do3rdbeatInGroupOfEven3sAdjustment()
	{
		doSubTactusOf3();
		doEntireTactusOf3();
		doTactusItemOf3SubTactii();
	}
	

	
	private void doEntireTactusOf3()
	{
		if (tactus.size() == 3)
		{
			// check they are equal length
			Double test = tactus.get(0);
			boolean b = true;
			for (Double d: tactus)
			{
				if (d != test) b = false;
			}
			if (b)
			{
				double mapPosition = test + test;
				map.put(mapPosition, new DoubleAndDouble(test, test + test));
			}
		}
		
	}


	
	private void doTactusItemOf3SubTactii()
	{
		double position = 0.0;
		for (Double tac: tactus)
		{
			if (tac / subTactusLength == 3.0)
			{
				double mapPosition = position + subTactusLength + subTactusLength;
				map.put(mapPosition, new DoubleAndDouble(subTactusLength, subTactusLength + subTactusLength));
			}
			position += tac;
		}
		
	}


	
	private void doSubTactusOf3()
	{
		
		for (Integer key: superTacMap.keySet())
		{
			double pos = 0.0;
			for (SuperTactus st: superTacMap.get(key))
			{
				if (st.getTactusCount() == 3)
				{
					// check they are equal length
					ArrayList<Double> list = st.getTactii();
					Double test = list.get(0);
					boolean b = true;
					for (Double d: list)
					{
						if (d != test) b = false;
					}
					if (b)
					{
						double mapPosition = pos + test + test;
						map.put(mapPosition, new DoubleAndDouble(test, test + test));
					}
				}
				pos += st.getLengthInQuarters();
			}
			
		}
		
	}


	
	private void makeSubSubTactusItemsForMap()
	{
		double len = getSubTactusLength() / 2.0;
		while (len >= 1.0 / splitListResolution)
		{
			for (double pos = len; pos < getLengthInQuarters(); pos += len * 2)
			{
				if (!map.containsKey(pos))
				{
					map.put(pos, new DoubleAndDouble(len / 2.0, len));
				}
			}
			len /= 2.0;
		}		
	}



	private void makeSubTactusItemsForMap()
	{
		for (double pos = getSubTactusLength(); pos < getLengthInQuarters(); pos += getSubTactusLength())
		{
			if (!map.containsKey(pos))
			{
				map.put(pos, new DoubleAndDouble(getSubTactusLength() / 2.0, getSubTactusLength()));
			}
		}
		
	}



	private void makeTactusItemsForMap()
	{
		double pos = 0.0;
		double previousLength = 0.0;
		Double[] tactus = getTactus();
		for (int i = 0; i < tactus.length; i++)
		{
			double tactusLength = tactus[i];
			if (tactusLength == previousLength)
			{
				if (!map.containsKey(pos))
				{
					map.put(pos, new DoubleAndDouble(tactusLength / 2.0, tactusLength));
				}
			} 
			else
			{
				if (!map.containsKey(pos))
				{
					map.put(pos, new DoubleAndDouble(1000.0, 1000.0)); // 1000.0 - big enough to fail any test.
				}
			}
			pos += tactusLength;
			previousLength = tactusLength;
		}
		
	}



	private void makeSuperTactusItemsForMap() 
	{
		for (Integer key: superTacMap.keySet())
		{
//			System.out.println(superTacMap.get(key).toString());
			ArrayList<SuperTactus> list = superTacMap.get(key);
			double pos = 0.0;
			for (int i = 0; i < list.size(); i++)
			{
				SuperTactus st = list.get(i);
				SuperTactus next = null;
				if (i < list.size() - 1)
				{
					next = list.get(i + 1);
				}
				pos += st.getLengthInQuarters();
				if (next == null)
				{
					if (!map.containsKey(pos))
					{
						map.put(pos, new DoubleAndDouble(1000.0, 1000.0)); // 1000.0 - big enough to fail any test.
					}
				} else
				{
					if (st.getLengthInQuarters() != next.getLengthInQuarters())
					{
						if (!map.containsKey(pos))
						{
							map.put(pos, new DoubleAndDouble(1000.0, 1000.0)); // 1000.0 - big enough to fail any test.
						}
					} else
					{
						if (!map.containsKey(pos))
						{
							map.put(pos, new DoubleAndDouble(st.getLengthInQuarters() / 2.0, st.getLengthInQuarters()));
						}
					} 
				}
			}
		}
	}
	

	
// %%%%%%%%%%%%% Split list construction methods %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	

	
	
	private ArrayList<SplitListSource> makeSplitLists() 
	{
		ArrayList<SplitListSource> list = new ArrayList<SplitListSource>();
		
		addSuperTactuses(list);
		
		SuperTactusList stl1 = addTactus(list);
		
		addSemiSubTactus(list, stl1);
		
		addSubTactus(list, stl1);
		
		addSubSubTactuses(list);
		
		return list;
	}


	
	// divides any tactus that contains 3 or more relationship with the subTactusLength in half,
	// quantized to the subTactusLength, and putting the longer part first if the division is uneven
	
	private void addSemiSubTactus(ArrayList<SplitListSource> list, SuperTactusList stl) 
	{
		double pos = 0.0;
		SuperTactusList stl2 = new SuperTactusList();
		for (SuperTactus st: stl.getSuperTactuses())
		{
			double liq = st.getLengthInQuarters();
			if (liq / subTactusLength >= 3.0)
			{
				double div = Math.round(liq / subTactusLength / 2.0) * subTactusLength;
				SuperTactus stnew = new SuperTactus(0);	// 0, the 'level' argument for SuperTactus is potentially meaningless, may be removed
				stnew.addTactusLengthItem(div + pos);
				stl2.addSuperTactus(stnew);		
				pos = st.getLengthInQuarters() - div;
			} 
			else 
			{
				pos += st.getLengthInQuarters();
			}
		}
		if (stl2.numberOfSuperTactuses() > 0) list.add(stl2);
	}



	private void addSubSubTactuses(ArrayList<SplitListSource> list) 
	{
		double len = subTactusLength / 2.0;
		while (len >= 1.0 / splitListResolution)
		{
			list.add(new SplitListGenerator(getLengthInQuarters(), len));
			len /= 2.0;
		}
	}



	private void addSubTactus(ArrayList<SplitListSource> list, SuperTactusList stl1) 
	{
		List<Double> tactusSplits = stl1.getSplitList();
		SuperTactusList stl2 = new SuperTactusList();
		double pos = 0.0;
		for (Double d = subTactusLength; d < getLengthInQuarters(); d += subTactusLength)
		{
//			if (!tactusSplits.contains(d))
//			{
				SuperTactus st = new SuperTactus(0);	// 0, the 'level' argument for SuperTactus is potentially meaningless, may be removed
				st.addTactusLengthItem(d - pos);
				stl2.addSuperTactus(st);
				pos = d;
//			}
			
		}
		list.add(stl2);
	}



	private SuperTactusList addTactus(ArrayList<SplitListSource> list) 
	{
		SuperTactusList stl1 = new SuperTactusList();
		for (Double d: tactus)
		{
			SuperTactus st = new SuperTactus(0);	// 0, the 'level' argument for SuperTactus is potentially meaningless, may be removed
			st.addTactusLengthItem(d);
			stl1.addSuperTactus(st);
		}
		list.add(stl1);
		return stl1;
	}


	
	private void addSuperTactuses(ArrayList<SplitListSource> list) 
	{
		
		for (Integer i: superTacMap.keySet())
		{
			SuperTactusList stl = new SuperTactusList();
			SuperTactus previousItem = null;
			double cross = 0.0;
			for (SuperTactus st: superTacMap.get(i))
			{
				if (previousItem != null)
				{
					if (previousItem.getLengthInQuarters() != st.getLengthInQuarters())
					{
						absolutelyDoNotCrossList.add(cross);
					}
				}
				stl.addSuperTactus(st);
				previousItem = st;
				cross += st.getLengthInQuarters();
			}
			list.add(stl);
		}
	}


	
	private void makeSuperTacMap(Object[] arr) 
	{
		TreeMap<Integer, SuperTactus> map = new TreeMap<Integer, SuperTactus>();
		for (Object o: arr)
		{
			if (o instanceof Integer)
			{
				loadCurrentSuperTactusIntoSuperTacMapAndMakeNewOne((Integer)o, map);
			}
			else if (o instanceof Double)
			{
				loadSuperTactusInstancesInMap((Double)o, map);
			}
		}
		for (Integer key: map.keySet())
		{
			loadSuperTacMapWithSuperTactusInstance(key, map.get(key));
		}
		
	}



	private void loadCurrentSuperTactusIntoSuperTacMapAndMakeNewOne(Integer key, TreeMap<Integer, SuperTactus> map) 
	{
		if (map.containsKey(key))
		{
			loadSuperTacMapWithSuperTactusInstance(key, map.get(key));
		}
		map.put(key,  new SuperTactus(key));
	}


	
	private void loadSuperTactusInstancesInMap(Double o, TreeMap<Integer, SuperTactus> map) 
	{
		for (Integer key: map.keySet())
		{
			map.get(key).addTactusLengthItem(o);
		}
		
	}


	
	private void loadSuperTacMapWithSuperTactusInstance(Integer key, SuperTactus aSuperTactus) 
	{
		if (!superTacMap.containsKey(key))
		{
			superTacMap.put(key,  new ArrayList<SuperTactus>());
		}
		superTacMap.get(key).add(aSuperTactus);
		
	}


	
	private void makeTactus(Object[] arr) {
		tactus = new ArrayList<Double>();
		for (Object o: arr)
		{
			if (o instanceof Double)
			{
				tactus.add((Double)o);
			}
		}
	}
	
	
	
	private ArrayList<Double> makeSuperTactusInQuarters()
	{
		ArrayList<Double> list = new ArrayList<Double>();
		double pos = 0.0;
		int key = getHighestSuperTacMapKey();
		ArrayList<SuperTactus> superTactii = superTacMap.get(key);
		if (superTactii == null) return list;
		for (SuperTactus st: superTactii)
		{
			list.add(pos);
			pos += st.getLengthInQuarters();
		}
		return list;
	}


	
	private int getHighestSuperTacMapKey()
	{
		int key = -10000;	// arbitrarily low
		for (Integer x: superTacMap.keySet())
		{
			if (x > key) key = x;
		}
		return key;
	}
	
	
	
	private ArrayList<Double> makeSubTactusInQuarters()
	{
		ArrayList<Double> list = new ArrayList<Double>();
		double pos = 0.0;
		while (pos < getLengthInQuarters())
		{
			list.add(pos);
			pos += getSubTactusLength();
		}
		hasSubTactusInQuarters = true;
		return list;
	}


	
	private ArrayList<Double> makeTactusInQuarters()
	{
		ArrayList<Double> list = new ArrayList<Double>();
		double pos = 0.0;
		for (Double d: tactus)
		{
			list.add(pos);
			pos += d;
		}
		hasTactusInQuarters = true;
		return list;
	}


		

	// only considers positions less than aPositionInQuarters + extra, which is chosen to make the 2nd of a triplet 
	// (pos 0.33) gravitate to 0.25, and a 3rd triplet (pos 0.667) gravitate towards 0.5 and there for higher strength than 
	// the 2nd triplet
	private double getClosestStrengthKey(double aPositionInQuarters)
	{
		double dist = 1000.0;	// arbitrarily large
		double tempDist;
		double key = -1;
		double extra = 0.07;
		for (double d: strengthMap.keySet())
		{
			if (d < aPositionInQuarters + extra)
			{
				tempDist = Math.abs(aPositionInQuarters - d);
				if (tempDist < dist)
				{
					dist = tempDist;
					key = d;
				} 
			}
		}
		return key;
	}

	//----------------------------------
	// INTERFACE
	//----------------------------------

	
	public int getNumerator() 
	{
		return numerator;
	}
	
	
	
	public int getDenominator() 
	{
		return denominator;
	}
	
	
	
	public boolean setNumerator(int aNumerator)
	{
		boolean wasSet = false;
		if (!canSetNumerator) { return false; }
		canSetNumerator = false;
		numerator = aNumerator;
		wasSet = true;
		return wasSet;
	}


	
	public boolean setDenominator(int aDenominator)
	{
		boolean wasSet = false;
		if (!canSetDenominator) { return false; }
		canSetDenominator = false;
		denominator = aDenominator;
		wasSet = true;
		return wasSet;
	}

	

	public String getName()
	{
		return name;
	}
	
	
	
	public Double[] getTactus() 
	{
		return tactus.toArray(new Double[tactus.size()]);	
	}
	
	
	
	public Double[] getSuperTactusAsQuartersPositions()
	{
		if (!hasSuperTactusAsQuarters) superTactusInQuarters = makeSuperTactusInQuarters();
		return superTactusInQuarters.toArray(new Double[superTactusInQuarters.size()]);	
	}

	
	

	public Double[] getTactusAsQuartersPositions()
	{
		if (!hasTactusInQuarters) tactusInQuarters = makeTactusInQuarters();
		return tactusInQuarters.toArray(new Double[tactusInQuarters.size()]);	
	}

	
	
	public Double[] getSubTactusAsQuartersPositions()
	{
		if (!hasSubTactusInQuarters) subTactusInQuarters = makeSubTactusInQuarters();
		return subTactusInQuarters.toArray(new Double[subTactusInQuarters.size()]);
	}
	

	

	public void setTactus(ArrayList<Double> tactus) 
	{
		this.tactus = tactus;
		hasTactusInQuarters = false;
		hasSubTactusInQuarters = false;
	}
	

	
	public double getLengthInQuarters() 
	{
		return numerator * 4.0 / denominator;
	}
	

	
	public double getSubTactusLength() 
	{
		return subTactusLength;
	}
	

	
	public Set<Double> getAbsolutelyDoNotCrossList()
	{
		return absolutelyDoNotCrossList;
	}
	

	
	@Override
	public TSMapInterface getTimeSignatureMap(int barCount) 
	{
		TSMapInterface tsm = new TSMFromGen(barCount);
		while (tsm.addTimeSignature(this)) {}
		return tsm;
	}
	
	
	
	public ArrayList<SplitListSource> getSplitLists()
	{
		return splitLists;
	}
	

	
	public DoubleAndDouble getCrossingMapItem(Double positionInBar)
	{
		if (map.containsKey(positionInBar))
		{
			return map.get(positionInBar);
		}
		else
		{
			return map.get(map.lastKey());		// assumed to be the position of the end of the bar which should fail any tests as to whether a note can be joined over it.	
		}
	}
	
	
	
	public String toString()
	{
		return nameToString();
	}
	
	
	
	public String toStringForFile()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(numerator + ";" + denominator + ";" + subTactusLength + ";");
		for (int i = 0; i < setupObjectArray.length - 1; i++)
		{
			sb.append(setupObjectArray[i] + ",");
		}
		sb.append(setupObjectArray[setupObjectArray.length - 1]);		
		return sb.toString();
	}
	
	
	
	public String toLongString()
	{
		String str = "TactusTimeSignature: ";
		str += tactus.toString();
//		for (Integer key: superTacMap.keySet())
//		{
//			str += "\nlevel=" + key + "\n";
//			for (SuperTactus st: superTacMap.get(key))
//			{
//				str += st.toString() + "\n";
//			}
//		}
		str += "\nabsolutelyDoNotCrossList: ";
		for (Double d: absolutelyDoNotCrossList)
		{
			str += d + ", ";
		}
		str += "\nsplitLists:------";
		for (SplitListSource sls: splitLists)
		{
			str += "\n" + sls.toString();
		}
		
		str += "\nCrossing map criteria:-";
		for (Double pos: map.keySet())
		{
			str += "\npos=" + pos + ": " + map.get(pos).toString();
		}
		
		return str;
	}
	

	
	private String nameToString() 
	{
		return numerator + "/" + denominator;
	}
	

	
	public boolean isSameAs(TimeSignature ts) {
		if (ts != null && ts.numerator == numerator && ts.denominator == denominator) {
			return true;
		} else {
			return false;
		}
	}
	

	
	public TreeMap<Integer, ArrayList<SuperTactus>> getSuperTacMap()
	{
		return superTacMap;
	}
	
	
	
	public int getStrengthOfPositionInQuarters(double aPositionInQuarters)
	{
		if (strengthMap.containsKey(aPositionInQuarters))
		{
			return strengthMap.get(aPositionInQuarters);
		}
		else
		{
			double key = getClosestStrengthKey(aPositionInQuarters);
			return strengthMap.get(key);
		}
	}
	
	
	public static Iterator<TimeSignature> getPresetTimeSignatureIterator()
	{
		return searchableMap.values().iterator();
	}
	
	
	
	//---------------------------------------------
	// MAIN
	//---------------------------------------------
	


	public static void main(String[] args) {
//		TimeSignature tts = new TimeSignature(4, 4, 0.5, new Object[] 
//				{
//						1, 
//						1.0, 1.0, 
//						1, 
//						1.0, 1.0
//				});
//		TimeSignature tts = new TimeSignature(13, 4, 0.5, new Object[] 
//				{
//						1, 2, 3,
//						1.0, 1.0, 
//						3,
//						1.0, 1.0, 
//						2, 3,
//						1.0, 1.0, 
//						1.0, 
//						1, 2, 3,
//						1.0, 1.0, 
//						3,
//						1.0, 1.0, 
//						2, 3,
//						1.0, 1.0
//				});
		TimeSignature tts = new TimeSignature(13, 4, 0.5, new Object[] 
				{
						1, 2,
						1.5, 1.5, 1.0, 
						2,
						1.0, 1.0, 1.0, 
						1, 2,
						1.5, 1.5, 1.0, 
						2,
						1.0, 1.0
				});
//		TimeSignature tts = new TimeSignature(12, 8, 0.5, new Object[] 
//				{
//						1, 2,
//						1.5,
//						2,
//						1.0, 1.0,
//						1, 2,
//						1.5,
//						2,
//						1.0
//				});
		TimeSignature sevenEight = new TimeSignature(7, 8, 0.5, new Object[] {1, 1.5, 1, 1.0, 1.0});
		System.out.println(sevenEight.toString());
//		System.out.println(tts.toString());
//		TimeSignature fourFour = new TimeSignature(4, 4, 0.5, new Object[] {1, 1.0, 1.0, 1, 1.0, 1.0});
//		System.out.println(fourFour.toString());
//		System.out.println(TimeSignature.FOUR_FOUR.toString());
	}


	public static TimeSignature getTimeSignature(int aSignatureNumerator, int aSignatureDenominator)
	{
		String str = aSignatureNumerator + "/" + aSignatureDenominator;
		if (searchableMap.containsKey(str))
		{
			return searchableMap.get(str);
		}
		else
		{
			return null;
		}	
	}


	
	public Object[] getParameterObjectArray()
	{
		if (searchableMap.containsKey(getName()))
		{
			return new Object[] {getName()};
		}
		else
		{
			ArrayList<Object> list = new ArrayList<Object>();
			list.add("TimeSignature");
			list.add(getName());
			list.add(numerator);
			list.add(denominator);
			list.add(subTactusLength);
			for (Object o: setupObjectArray)
			{
				list.add(o);
			}
			return list.toArray(new Object[list.size()]);
		}
	}



	public Element getXMLElement(Document document)
	{
		Element element = document.createElement("time_signature");
		
		if (searchableMap.containsKey(name))
		{
			// save preset name
			element.setAttribute("preset", "true");
			Element name_element = document.createElement("name");
			name_element.appendChild(document.createTextNode(getName()));
			element.appendChild(name_element);
		}
		else
		{
			element.setAttribute("preset", "false");
			
			Element numerator_element = document.createElement("numerator");
			numerator_element.appendChild(document.createTextNode("" + numerator));
			element.appendChild(numerator_element);
			
			Element denominator_element = document.createElement("denominator");
			denominator_element.appendChild(document.createTextNode("" + denominator));
			element.appendChild(denominator_element);
			
			Element sub_tactus_length_element = document.createElement("sub_tactus_length");
			sub_tactus_length_element.appendChild(document.createTextNode("" + subTactusLength));
			element.appendChild(sub_tactus_length_element);
			
			Element setup_object_array = document.createElement("setup_object_array");
			for (Object o: setupObjectArray)
			{
				if (o instanceof Integer)
				{
					Element object_element = document.createElement("object");
					object_element.setAttribute("type", "int");
					object_element.appendChild(document.createTextNode("" + o));
					setup_object_array.appendChild(object_element);
				}
				else if (o instanceof Double)
				{
					Element object_element = document.createElement("object");
					object_element.setAttribute("type", "double");
					object_element.appendChild(document.createTextNode("" + o));
					setup_object_array.appendChild(object_element);
				}
			}
			element.appendChild(setup_object_array);
		}
		
		return element;
	}



	public static TimeSignature getTimeSignatureFromXMLElement(Element element)
	{
		String type = element.getAttribute("preset");
//		Element time_signature = (Element)element.getElementsByTagName("time_signature").item(0);
		switch (type)
		{
		case "true":
			String name_str = element.getElementsByTagName("name").item(0).getTextContent();
			return searchableMap.get(name_str);
		case "false":
			int num = Integer.parseInt(element.getElementsByTagName("numerator").item(0).getTextContent());
			int denom = Integer.parseInt(element.getElementsByTagName("denominator").item(0).getTextContent());
			double subtaclength = Double.parseDouble(element.getElementsByTagName("sub_tactus_length").item(0).getTextContent());
			Element setuparr = (Element)element.getElementsByTagName("setup_object_array").item(0);
			NodeList objList = setuparr.getElementsByTagName("object");
			Object[] objArr = new Object[objList.getLength()];
			for (int i = 0; i < objList.getLength(); i++)
			{
				Element obj_element = (Element)objList.item(i);
				String objType = obj_element.getAttribute("type");
				switch (objType)
				{
				case "int":
					objArr[i] = Integer.parseInt(obj_element.getTextContent());
					break;
				case "double":
					objArr[i] = Double.parseDouble(obj_element.getTextContent());
					break;
				}
			}
			return new TimeSignature(num, denom, subtaclength, objArr);
		}
		
		return null;
	}


	
	public Object[] getJSONEntry()
	{
		return new Object[] {numerator, denominator, subTactusLength, setupObjectArray};
	}

	
	


}
