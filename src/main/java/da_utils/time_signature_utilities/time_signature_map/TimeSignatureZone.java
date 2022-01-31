/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package main.java.da_utils.time_signature_utilities.time_signature_map;

import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

// line 109 "../TimeSignatureMap.ump"
public class TimeSignatureZone
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TimeSignatureZone Attributes
  private TimeSignature ts;
  private int barCount;
  private int startBar;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TimeSignatureZone(TimeSignature aTs, int aBarCount, int aGlobalStartPositionInBars)
  {
    ts = aTs;
    barCount = aBarCount;
    startBar = aGlobalStartPositionInBars;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTs(TimeSignature aTs)
  {
    boolean wasSet = false;
    ts = aTs;
    wasSet = true;
    return wasSet;
  }

  public boolean setBarCount(int aBarCount)
  {
    boolean wasSet = false;
    barCount = aBarCount;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartBar(int aStartBar)
  {
    boolean wasSet = false;
    startBar = aStartBar;
    wasSet = true;
    return wasSet;
  }

  public TimeSignature getTs()
  {
    return ts;
  }

  public int getBarCount()
  {
    return barCount;
  }

  public int getStartBar()
  {
    return startBar;
  }

  public void delete()
  {}

  // line 117 "../TimeSignatureMap.ump"
   public void addToBarCount(int i){
    barCount += i;
  }

  // line 121 "../TimeSignatureMap.ump"
   public int getNumerator(){
    return ts.getNumerator();
  }

  // line 125 "../TimeSignatureMap.ump"
   public int getDenominator(){
    return ts.getDenominator();
  }

  // line 129 "../TimeSignatureMap.ump"
   public String toString(){
    String str = "TimeSignatureZone: " + ts.getName() + " start=bar " + startBar + " for " + barCount + " bars";
		return str;
  }

}