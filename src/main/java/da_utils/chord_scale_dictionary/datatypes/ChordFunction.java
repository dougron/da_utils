package main.java.da_utils.chord_scale_dictionary.datatypes;

import main.java.da_utils.chord_scale_dictionary.key_utils.Key;

public class ChordFunction {

	public int keyIndex;
	public int degree;
	public String chordType;
	public String name;
	public Key key;
	public int primaryScore;
	public boolean isFunction;
	public boolean isTonic;
	
	
	public ChordFunction(int keyIndex, int degree, String chordType, String name, int prim, int tonic){
		this.keyIndex = keyIndex;
		this.degree = degree;
		this.chordType = chordType;
		this.name = name;
		this.primaryScore = prim;
		this.isFunction = true;
		if (tonic == 0){
			isTonic = false;
		} else {
			isTonic = true;
		}
	}
	public ChordFunction(){
		this.isFunction = false;
	}
	public void setKey(Key k){
		key = k;
	}
	public String toString(){
		if (isFunction){
			String keyString = "";
			if (key != null){
				keyString = " in " + key.toString();
			} 
			if (isTonic){
				keyString += " is Tonic";
			} else {
				keyString += " is not Tonic";
			}
			return chordType + " chord on degree " + degree + " function as " + name + keyString;
		} else {
			return "noFunction";
		}
		
	}
	public String simpleToString(){
		if (isFunction){
			return name;
		} else {
			return "-";
		}
	}
}
