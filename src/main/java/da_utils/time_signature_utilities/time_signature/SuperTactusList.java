package main.java.da_utils.time_signature_utilities.time_signature;
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/


/**
 * wrapper for a list of SuperTactus instances to do things like cumulative split point lists and the like
 * potentially a misnomer as the SuperTactus will be applied to the tactus as well as this class catering 
 * for returning splitpoint lists for subtactus levels. 
 * 
 * for the level immidiately below the tactus, need to work out the common factor for the tactus. Generally this
 * will be 0.5 (for tactii of 1.0 or 1.5) but it occurs to me that cut common time needs to be catered for
 * and this has a tactus equal to 2.0, which means I should be catering for any tactus, ffs
 * 
 * maybe subtactus levels need to be explicitly instantiated. for now, I see any level below the immidiate 
 * subtactus as being half of the level above. I suppose a special case might emerge, but it might be fair to
 * assume that, for instance, the scope of time signatures in the Carmina Burana are an aberation as should be
 * ignored as common practice. Common time signatures are: x/4, x/8, 2/2, 4/2 (this last one came up during internet
 * research so I include it but I've never used it myself.
 * 
 * @author dougr
 *
 */

import java.util.*;

import main.java.da_utils.time_signature_utilities.greatest_common_factor_calculator.GreatestCommonFactor;

// line 1 "SuperTactusList.ump"
public class SuperTactusList implements SplitListSource
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //SuperTactusList Attributes
  private List<Double> splitList;

  //SuperTactusList Associations
  private List<SuperTactus> superTactuses;
  private double gcf;
  private double averageLength;
  
  //------------------------
  // HELPER VARIABLES
  //------------------------
  
  private boolean hasSplitList;
  private boolean hasGcf;
  private boolean hasAverageLength;




  

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SuperTactusList()
  {
    splitList = new ArrayList<Double>();
    superTactuses = new ArrayList<SuperTactus>();
    hasSplitList = false;
    hasGcf = false;
    hasAverageLength = false;
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
  
  
  /* Code from template attribute_GetMany */
  public Double getSplitList(int index)
  {
    Double aSplitList = splitList.get(index);
    return aSplitList;
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
  
  

  private void makeSplitList() {
	splitList.clear();
	Double pos = 0.0;
	for (SuperTactus st: superTactuses)
	{
		pos += st.getLengthInQuarters();
		splitList.add(pos);
	}
	
}
  
  
  public double getAverageLength()
  {
	  double count = 0.0;
	  double sum = 0.0;
	  if (!hasAverageLength)
	  {
		  for (SuperTactus st: getSuperTactuses())
		  {
			  sum += st.getLengthInQuarters();
			  count++;
		  }
		  averageLength = sum / count;
	  }
	  return averageLength;
  }



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
  
  
  
  /* Code from template association_GetMany */
  public SuperTactus getSuperTactus(int index)
  {
    SuperTactus aSuperTactus = superTactuses.get(index);
    return aSuperTactus;
  }
  
  

  public List<SuperTactus> getSuperTactuses()
  {
    List<SuperTactus> newSuperTactuses = Collections.unmodifiableList(superTactuses);
    return newSuperTactuses;
  }
  
  

  public int numberOfSuperTactuses()
  {
    int number = superTactuses.size();
    return number;
  }
  
  

  public boolean hasSuperTactuses()
  {
    boolean has = superTactuses.size() > 0;
    return has;
  }
  
  

  public int indexOfSuperTactus(SuperTactus aSuperTactus)
  {
    int index = superTactuses.indexOf(aSuperTactus);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSuperTactuses()
  {
    return 0;
  }
  
  
  
  /* Code from template association_AddUnidirectionalMany */
  public boolean addSuperTactus(SuperTactus aSuperTactus)
  {
    boolean wasAdded = false;
    if (superTactuses.contains(aSuperTactus)) { return false; }
    superTactuses.add(aSuperTactus);
    hasSplitList = false;
    hasAverageLength = false;
    wasAdded = true;
    return wasAdded;
  }
  
  

  public boolean removeSuperTactus(SuperTactus aSuperTactus)
  {
    boolean wasRemoved = false;
    if (superTactuses.contains(aSuperTactus))
    {
      superTactuses.remove(aSuperTactus);
      wasRemoved = true;
    }
    hasGcf = false;
    hasAverageLength = false;
    return wasRemoved;
  }
  
  
  
  /* Code from template association_AddIndexControlFunctions */
  public boolean addSuperTactusAt(SuperTactus aSuperTactus, int index)
  {  
    boolean wasAdded = false;
    if(addSuperTactus(aSuperTactus))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSuperTactuses()) { index = numberOfSuperTactuses() - 1; }
      superTactuses.remove(aSuperTactus);
      superTactuses.add(index, aSuperTactus);
      hasSplitList = false;
      hasAverageLength = false;
      wasAdded = true;
    }
    hasGcf = false;
    return wasAdded;
  }
  
  

  public boolean addOrMoveSuperTactusAt(SuperTactus aSuperTactus, int index)
  {
    boolean wasAdded = false;
    if(superTactuses.contains(aSuperTactus))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSuperTactuses()) { index = numberOfSuperTactuses() - 1; }
      superTactuses.remove(aSuperTactus);
      superTactuses.add(index, aSuperTactus);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSuperTactusAt(aSuperTactus, index);
    }
    hasGcf = false;
    hasAverageLength = false;
    return wasAdded;
  }
  
  

  public void delete()
  {
    superTactuses.clear();
  }
  


  public String toString()
  {
    String str = super.toString() + "\n[\nsplitList: ";
    for (Double d: getSplitList())
    {
    	str += d + ", ";
    }
    for (SuperTactus st: superTactuses)
    {
    	str += "\n" + st.toString();
    }
    str += "\ngcf=" + getGcf();
    str += "\navgLength=" + getAverageLength();
    str += "\n]";
    return str;
  }
  
  
  
  public static void main(String[] args)
  {
	  SuperTactusList stl = new SuperTactusList();
	  
	  SuperTactus st1 = new SuperTactus(1);
	  st1.addTactusLengthItem(1.0);
	  st1.addTactusLengthItem(1.0);
	  stl.addSuperTactus(st1);
	  
	  SuperTactus st2 = new SuperTactus(1);
	  st2.addTactusLengthItem(1.0);
	  st2.addTactusLengthItem(1.0);
	  stl.addSuperTactus(st2);
	  
	  System.out.println(stl.toString());
	  
	  List<SplitListSource> list = new ArrayList<SplitListSource>();
	  list.add(stl);
	SplitListGenerator slg = new SplitListGenerator(4.0,  0.25);
	System.out.println(slg.toString());
	
	list.add(slg);
	
	SplitListGenerator slg2 = new SplitListGenerator(4.0,  0.125);
	System.out.println(slg2.toString());
  }
}













