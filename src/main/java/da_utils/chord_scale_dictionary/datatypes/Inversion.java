package main.java.da_utils.chord_scale_dictionary.datatypes;
/*
 * wraps inversion data for the ChordScaleDictionary
 */
public class Inversion {

	public int index;
	public String name;
	
	public Inversion(int ind, String n){
		index = ind;
		name = n;
	}
	public Inversion(int ind){
		index = -1;
		name = "null Inversion";
	}
	public String toString(){
		String ret = "index: " + index;
		ret = ret + "  name: " + name;
		return ret;
	}
}
