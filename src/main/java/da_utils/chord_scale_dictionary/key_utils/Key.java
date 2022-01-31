package main.java.da_utils.chord_scale_dictionary.key_utils;
import java.util.ArrayList;

import main.java.da_utils.chord_scale_dictionary.datatypes.ChordFunction;
import main.java.da_utils.chord_scale_dictionary.datatypes.NotePatternScale;

/*
 * wraps data related to key types (major, minor, and possibly some weird ones)
 */


public class Key {
	
	public int index;
	public String name;
	public ArrayList<ChordFunction> cfList = new ArrayList<ChordFunction>();
	public NotePatternScale scale;
	
	public Key(int index, String name, NotePatternScale nps){
		this.index = index;
		this.name = name;
		this.scale = nps;
	}
	public void addChordFunction(ChordFunction cf){
		cfList.add(cf);
	}
	public String toString(){
		return name + " key(index = " + index + ") uses scale: " + scale.toString();
	}

}
