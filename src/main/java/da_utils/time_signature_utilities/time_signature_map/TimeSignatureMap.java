	/*CODE CAN BE EDITED - Umple no longer used*/
	/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/
	  

	package main.java.da_utils.time_signature_utilities.time_signature_map;
	import java.util.*;

import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;
	
	
	
	/**
	 * holds a map of time signatures, as a list of TimeSignatures where the index is the bar number, and as a 
	 * list of TimeSignatureZones which are a zone of x bars of one timesignature, which is more suited to score representation (supposedly)
	 * adding (and removing) of TimeSignatures is not meant to be used for a direct edit process by the user. If the user
	 * intervenes and edits the contents of the TimeSignatureMap a new tsm is created. (I think, 9 June 2020)
	 */
	
	
	// line 11 "../TimeSignatureMap.ump"
	public class TimeSignatureMap implements TSMapInterface
	{
	
		
	  //------------------------
	  // MEMBER VARIABLES
	  //------------------------
	
		
	  //TimeSignatureMap Attributes
	
		
	  
	  private TimeSignature lastTS;
	  private List<TimeSignature> timeSignatures;
	  private List<TimeSignatureZone> timeSignatureZones;
	  private List<Double> barStartInQuarters;
	
	  
	  //------------------------
	  // CONSTRUCTOR
	  //------------------------
	
	  
	  
	  public TimeSignatureMap()
	  {
	    timeSignatures = new ArrayList<TimeSignature>();
	    timeSignatureZones = new ArrayList<TimeSignatureZone>();
	    barStartInQuarters = new ArrayList<Double>();
	  }
	  
	  
	
	  //------------------------
	  // INTERFACE
	  //------------------------
	  
	  

	  @Override
	  public boolean addTimeSignature(TimeSignature aTimeSignature)
	  {
	    boolean wasAdded = false;
	    wasAdded = timeSignatures.add(aTimeSignature);
	    // line 22 "../TimeSignatureMap.ump"
	    makeTimeSignatureZones();
	    		makeBarStartList();
	    // END OF UMPLE AFTER INJECTION
	    return wasAdded;
	  }
	
	  
	  
	  @Override
	  public boolean removeTimeSignature(TimeSignature aTimeSignature)
	  {
	    boolean wasRemoved = false;
	    wasRemoved = timeSignatures.remove(aTimeSignature);
	    return wasRemoved;
	  }
	  
	  
	  
	  /* Code from template attribute_GetMany */
	  @Override
	  public TimeSignature getTimeSignature(int index)
	  {
	    TimeSignature aTimeSignature = timeSignatures.get(index);
	    return aTimeSignature;
	  }
	
	  
	  
	  @Override
	  public TimeSignature[] getTimeSignatures()
	  {
	    TimeSignature[] newTimeSignatures = timeSignatures.toArray(new TimeSignature[timeSignatures.size()]);
	    return newTimeSignatures;
	  }
	
	  
	  
	  @Override
	  public int numberOfTimeSignatures()
	  {
	    int number = timeSignatures.size();
	    return number;
	  }
	
	  
	  
//	  @Override
//	  public boolean hasTimeSignatures()
//	  {
//	    boolean has = timeSignatures.size() > 0;
//	    return has;
//	  }
	
	  
	  
//	  @Override
//	  public int indexOfTimeSignature(TimeSignature aTimeSignature)
//	  {
//	    int index = timeSignatures.indexOf(aTimeSignature);
//	    return index;
//	  }


	  
	  @Override
	  public TimeSignatureZone getTimeSignatureZone(int index)
	  {
	    TimeSignatureZone aTimeSignatureZone = timeSignatureZones.get(index);
	    return aTimeSignatureZone;
	  }
	
	  
	  
	  @Override
	  public TimeSignatureZone[] getTimeSignatureZones()
	  {
	    TimeSignatureZone[] newTimeSignatureZones = timeSignatureZones.toArray(new TimeSignatureZone[timeSignatureZones.size()]);
	    return newTimeSignatureZones;
	  }
	
	  
	  
	  @Override
	  public int numberOfTimeSignatureZones()
	  {
	    int number = timeSignatureZones.size();
	    return number;
	  }
	
	  
	  
//	  @Override
//	  public boolean hasTimeSignatureZones()
//	  {
//	    boolean has = timeSignatureZones.size() > 0;
//	    return has;
//	  }
//	
	  
	  
//	  @Override
//	  public int indexOfTimeSignatureZone(TimeSignatureZone aTimeSignatureZone)
//	  {
//	    int index = timeSignatureZones.indexOf(aTimeSignatureZone);
//	    return index;
//	  }
	  
	  
	  
	  @Override
	  public Double getBarStartInQuarter(int index)
	  {
	    Double aBarStartInQuarter = barStartInQuarters.get(index);
	    return aBarStartInQuarter;
	  }
	
	  
	  
	  @Override
	  public Double[] getBarStartInQuarters()
	  {
	    Double[] newBarStartInQuarters = barStartInQuarters.toArray(new Double[barStartInQuarters.size()]);
	    return newBarStartInQuarters;
	  }
	
	  
	  
//	  @Override
//	  public int numberOfBarStartInQuarters()
//	  {
//	    int number = barStartInQuarters.size();
//	    return number;
//	  }
	
	  
	  
//	  @Override
//	  public boolean hasBarStartInQuarters()
//	  {
//	    boolean has = barStartInQuarters.size() > 0;
//	    return has;
//	  }
	
	  
	  
//	  @Override
//	  public int indexOfBarStartInQuarter(Double aBarStartInQuarter)
//	  {
//	    int index = barStartInQuarters.indexOf(aBarStartInQuarter);
//	    return index;
//	  }
//	
	  
	  
	  @Override
	  public void delete()
	  {}
	
	  
	  
	  // line 27 "../TimeSignatureMap.ump"
	   @Override
	   public double getLengthInQuarters(){
	    return barStartInQuarters.get(barStartInQuarters.size() - 1);
	  }
	
	   
	   
	  // line 32 "../TimeSignatureMap.ump"
	   @Override
	   public String toString(){
	    String str = "TimeSignatureMap: numberOfTimeSignatures()=" + numberOfTimeSignatures() + "\n";
			for (TimeSignature ts: timeSignatures) {
				str += ts.getName() + ",";
			}
			
			for (TimeSignatureZone tsz: timeSignatureZones) {
				str += "\n" + tsz.toString();
			}
			return str;
	  }
	
	   
	   
	  // line 45 "../TimeSignatureMap.ump"
	   @Override
	   public boolean addTimeSignatures(List<TimeSignature> aTimeSignatures){
	    boolean wasAdded = false;
		    for (TimeSignature ts: aTimeSignatures) {
		    	wasAdded = false;
				wasAdded = addTimeSignature(ts);
			}
			return wasAdded;
	  }
	
	   
	   
	  // line 54 "../TimeSignatureMap.ump"
	   @Override
	   public boolean addTimeSignature(TimeSignature aTimeSignature, int index){
	    boolean wasAdded = false;
	    	TimeSignature ts = timeSignatures.get(index);
		    if (ts == timeSignatures.set(index, aTimeSignature)) {
		    	wasAdded = true;
		    } else {
		    	wasAdded = false;
		    }
		    makeTimeSignatureZones();
		    makeBarStartList();
		    return wasAdded;
	
	  }
	
	   

	   @Override
	   public void makeTimeSignatureZones()
	   {
			 TimeSignature lastTs = null;
			 TimeSignatureZone tsz = null;
			 timeSignatureZones.clear();
			 int barCount = 0;
			 for (TimeSignature ts: timeSignatures) 
			 {
				  if (!ts.isSameAs(lastTs) || tsz == null) 
				  {
					  tsz = new TimeSignatureZone(ts, 1, barCount);
					  lastTs = ts;
					  timeSignatureZones.add(tsz);
				  } 
					  else 
					  {
					  tsz.addToBarCount(1);
				  }
				  barCount++;
		  	}
	   	}
	
	   
	
	   private void makeBarStartList(){
	    double pos = 0.0;
			barStartInQuarters.clear();
			for (TimeSignature ts: timeSignatures) {
				barStartInQuarters.add(pos);
				pos += ts.getLengthInQuarters();
			}
			barStartInQuarters.add(pos);	// this is not a bar start actually but is there just in case if a barIndex + 1 is required to get an end point for a bar
	  }
	
	   
	
	  /**
	   * gets excerpt from TimeSignatureMap. will modulo to start to within the length of TimeSignatures and wrap when loading
	   */
	   @Override
	public TSMapInterface getTimeSignatureMap(int start, int length){
	    TSMapInterface tsm = new TimeSignatureMap();
		   start = start % numberOfTimeSignatures();
		   for (int i = 0; i < length; i++) {
			   tsm.addTimeSignature(timeSignatures.get((start + i) % numberOfTimeSignatures()));
		   }
		 return tsm;
	  }
	
	}