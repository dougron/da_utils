/**
 * Classes for describing an arrangement of timesignatures. Also contains a generator and beat hierarchy classifier
 * <p>
 * There are 3 sections:
 * <ol>
 * <li>classes to describe TimeSignatures
 * <li>classes to describe and generate TimeSignatureMaps
 * <li>classes to analyze rhythm according to beat hierarchy
 * </ol>
 * 1)--------------------
 * 
 * The TimeSignature class describes a musical time signature. Contained within is a list of 
 * SubDivItems describing how the time signature is divided up for the purposes of identifying
 * accented beats and for beaming according to this in staff notation. Each SubDivItem has a priority value
 * to identify stronger and weaker subdivisions, for future excursions into beat hierarchy manipulations
 * 
 * The top level SubDivItem describes the full time signature and has priority 0 by convention, and then contains recursive 
 * levels of subdivision items
 * 
 * example 4/4: Top level SubDivItem is 4 beats with priority 0, next level is an item of 2 beats with priority 1(1st 2 beats of the bars) and
 * another item of two beats with priority 2 (last two beats of the bar). Of course a composer could split their bar up any way they like.
 * format is <numerator>/<denominator>_<priority>
 * <table>
  <tr>
    <td colspan="2" cellpadding="50" align="CENTER"> 4_4_0 </td>
  </tr>
  <tr>
    <td> 2/4_1 </td> <td> 2/4_2</td>
  </tr>
</table>

 * example 13/4, the Clapham Junction example (meaningful to probably 3 of 7 billion people in the world): 
 * <table>
 * <tr>
 * <td colspan="4" cellpadding="50" align="CENTER">13/4_0</td>
 * </tr>
 * <tr cellpadding="50" align="CENTER">
 * <td colspan="2">7/4_1</td><td colspan="2">6/4_2</td>
 * </tr>
 * <tr cellpadding="50" align="CENTER">
 * <td>4/4_3</td><td>3/4_5</td><td>4/4_4</td><td>2/4_6</td>
 * </tr>
 * </table>
 * 2)----------------------------------
 * 
 * A TimeSignatureMap describes timesignatures of a fixed length of bars as a list of TimeSignatures and as
 * a list of TimeSignatureZones.
 * 
 * TimeSignatureZones contain a TimeSignature and a bar count. Useful for staff notation.
 * 
 * To generate a TimeSignatureMap, use TSMGenerator and add further TSMGenerator or TimeSignatures and their
 * repeat information.
 * 
 * TimeSignatureMap can also be built by adding or inserting (or deleting) TimeSignatures.
 * 
 * TSMFromGen is a special case of a TimeSignatureMap and is used by TSGenItems to construct TimeSignatureMaps of a
 * set size.
 * 
 * TSMForGlobal is a sub class of TimeSignatureMap used to make a global TimeSignatureMap in a musical piece describe by
 * a hierarchy of Mu objects.
 * 
 * 3)------------------------------------
 * 
 * Still needs finalizing. BeatHierarchyItem and BeatHierarchyLibrary not currently used (July 2020)
 */



package main.java.da_utils.time_signature_utilities;