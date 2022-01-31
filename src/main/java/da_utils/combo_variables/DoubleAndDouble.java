/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package main.java.da_utils.combo_variables;

// line 3 "../DoubleAndDouble.ump"
public class DoubleAndDouble
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //DoubleAndDouble Attributes
  private double d1;
  private double d2;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public DoubleAndDouble(double aD1, double aD2)
  {
    d1 = aD1;
    d2 = aD2;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setD1(double aD1)
  {
    boolean wasSet = false;
    d1 = aD1;
    wasSet = true;
    return wasSet;
  }

  public boolean setD2(double aD2)
  {
    boolean wasSet = false;
    d2 = aD2;
    wasSet = true;
    return wasSet;
  }

  public double getD1()
  {
    return d1;
  }

  public double getD2()
  {
    return d2;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "d1" + ":" + getD1()+ "," +
            "d2" + ":" + getD2()+ "]";
  }
}