package main.java.da_utils.chord_scale_dictionary.key_utils;
import java.util.ArrayList;

import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;
import main.java.da_utils.chord_scale_dictionary.datatypes.ChordFunction;



public class KeyArea {
	
	public int startIndex;
	public int endIndex;
	public int keyIndex;
	public boolean isPlaceholder;
	public Key key;
	public ArrayList<ChordFunction> cfList = new ArrayList<ChordFunction>();
	public ArrayList<ChordAnalysisObject> caoList = new ArrayList<ChordAnalysisObject>();

	public KeyArea(int startIndex, int keyIndex, Key key){
		this.startIndex = startIndex;
		this.keyIndex = keyIndex;
		this.key = key;
		this.isPlaceholder = false;
	}
	public KeyArea(){
		this.isPlaceholder = true;
	}
	
	public void addChordFunction(ChordFunction cf, ChordAnalysisObject cao){
		cfList.add(cf);
		caoList.add(cao);
	}
	public void end(int index){
		endIndex = index;
	}
	public String toString(){
		String ret = "";
		if (isPlaceholder){
			return "null KeyArea";
		} else {
			ret = ret +  "startIndex " + startIndex + " endIndex " + endIndex + " key  " + ChordScaleDictionary.noteName(keyIndex) + " ";
			for (ChordFunction cf: cfList){
				ret = ret + cf.simpleToString() + ", ";
			}
			return ret;
		}
	}
	public String[] toStringArray(int size){
		String[] strArr = new String[size + 1];
		strArr[0] = ChordScaleDictionary.noteName(keyIndex);
		for (int i = 0; i < startIndex; i++){
			strArr[i + 1] = "-";
		}
		for (int i = startIndex; i <= endIndex; i++){
			strArr[i + 1] = cfList.get(i - startIndex).name;
		}
		for (int i = endIndex + 2; i <= size; i++){
			strArr[i] = "-";
		}
		return strArr;
	}
	public int indexOfTonic(){
		int index = 0;
		for (ChordFunction cf: cfList){
			if (cf.isTonic){
				return index + startIndex;
			}
			index++;
		}
		return index + startIndex;
	}
}
