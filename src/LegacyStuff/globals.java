package LegacyStuff;
import java.util.ArrayList;
import java.util.Iterator;

//import ChordScaleDictionary.ChordScaleDictionary;

/** class for the dalooper that declares all globals parameters
 * 
 * @author User
 *
 */
public class globals {
	// to make this thing work without passing the pointer around endlessly
	// make all relevant parameters as follows
	// public static int barticklength = blahdiblah;
	// access and adjust via methods and bob's your uncle
	public static final int TestVal = 42;	// this is for testing
	public static int barticklength = 1920;
	public static int beatsInBar = 4;
	



	
//	public static ChordScaleDictionary csd = new ChordScaleDictionary();
	
	public int bart(){
		return barticklength;
	}
	public int bib(){
		return beatsInBar;
	}
	public int beatick(){
		return barticklength / beatsInBar;
	}
	public String toString(){
		return "globals:- " + this;
	}
	

}
