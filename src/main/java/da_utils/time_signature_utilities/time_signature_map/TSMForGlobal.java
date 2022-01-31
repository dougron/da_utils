/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package main.java.da_utils.time_signature_utilities.time_signature_map;
import java.util.*;

import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

/**
 * a subclass of TimeSignatureMap that creates a fixed length of default time signatures which can then 
 * be overwritten. Used for constructing global time signature map
 */
// line 177 "../TimeSignatureMap.ump"
public class TSMForGlobal extends TimeSignatureMap
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  public static final TimeSignature DEFAULT_TIME_SIGNATURE = new TimeSignature(4, 4, 0.5, new Object[]
		  {
				  1,
				  1.0, 1.0,
				  1,
				  1.0,1.0
		  });

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TSMForGlobal Attributes
  private int barCount;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TSMForGlobal(int aBarCount)
  {
    super();
    barCount = aBarCount;
    // line 187 "../TimeSignatureMap.ump"
    for (int i = 0; i < barCount; i++){
    			addTimeSignature(DEFAULT_TIME_SIGNATURE);
    		}
    		makeTimeSignatureZones();
    // END OF UMPLE AFTER INJECTION
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}