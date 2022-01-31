/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package main.java.da_utils.time_signature_utilities.time_signature_map;
import java.util.*;

import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

/**
 * a subclass of TimeSignatureMap that is instantiated with a preset barCount and then filled by a generator
 */
// line 137 "../TimeSignatureMap.ump"
public class TSMFromGen extends TimeSignatureMap
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TSMFromGen Attributes
  private int barCount;
  private boolean isFull;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TSMFromGen(int aBarCount)
  {
    super();
    barCount = aBarCount;
    isFull = false;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }


  /**
   * internal int numberOfBars;
   * after constructor{
   * setBarCount(numberOfBars);
   * }
   */
  @Override
  // line 149 "../TimeSignatureMap.ump"
   public boolean addTimeSignature(TimeSignature aTimeSignature){
    boolean wasAdded = false;
	    if (!isFull) {
			wasAdded = super.addTimeSignature(aTimeSignature);
			if (numberOfTimeSignatures() == barCount) {
				isFull = true;
				makeTimeSignatureZones();
			} 
		}
		return wasAdded;
  }


  @Override
  // line 162 "../TimeSignatureMap.ump"
   public boolean addTimeSignatures(List<TimeSignature> aTimeSignatures){
    boolean wasAdded = false;
	    for (TimeSignature ts: aTimeSignatures) {
	    	wasAdded = false;
			if (!isFull) {
				wasAdded = addTimeSignature(ts);
				if (!wasAdded) break;
				
			} 
		}
		return wasAdded;
  }

}