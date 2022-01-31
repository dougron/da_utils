package main.java.da_utils.chord_scale_dictionary.key_utils;
import java.util.ArrayList;

import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;
import main.java.da_utils.chord_scale_dictionary.datatypes.ChordFunction;
import main.java.da_utils.chord_scale_dictionary.test.KeyScorerConsoleTest;


/*
 * Receives ChordAnalysisObject and makes scores as to how it fits into the 
 * key that the class represents
 */
public class KeyScorer {
	
	public int keyIndex;
	public Key key;
	public ArrayList<ChordAnalysisObject> caoList = new ArrayList<ChordAnalysisObject>();
	public ArrayList<ChordFunction> cfList = new ArrayList<ChordFunction>();
	public ArrayList<Integer> primaryScore = new ArrayList<Integer>();
	public KeyScorerConsoleTest parent;
	public boolean debug = false;
	
	private int spacing = 10;

	public KeyScorer(int keyIndex, Key key){
		this.keyIndex = keyIndex;
		this.key = key;
	}
	public KeyScorer(int keyIndex, Key key, KeyScorerConsoleTest p){
		parent = p;
		debug = true;
		this.keyIndex = keyIndex;
		this.key = key;
	}
	
	public void addChordAnalysisObject(ChordAnalysisObject cao){
		caoList.add(cao);
		doScores(cao);
	}
	

	public String toString(){
		String ret = "Key: " + ChordScaleDictionary.noteName(keyIndex) + "\n";
		ret += "Chord:        ";
		for (ChordAnalysisObject cao: caoList){
			ret = ret + spacedChordName(cao.chordToString(), spacing);
		}
		ret = ret + "\nPrimaryScore: ";
		for (Integer i: primaryScore){
			ret = ret + spacedChordName(i + "", spacing);
		}
		return ret;
	}
	public String primaryScoreToString(){
		String keyName = spacedChordName(ChordScaleDictionary.noteName(keyIndex), 3);
		String ret = "\n" + keyName + " PrimaryScore: ";
		for (Integer i: primaryScore){
			ret = ret + spacedChordName(i + "", spacing);
		}
		return ret;
	}
	public String[] primaryScoreStringArray(){
		String[] ret = new String[primaryScore.size() + 1];
		ret[0] = spacedChordName(ChordScaleDictionary.noteName(keyIndex), 3) + " PrimaryScore:";
		int index = 1;
		for (Integer i: primaryScore){
			ret[index] = "" + i;
			index++;
		}
		return ret;
	}
	public String[] functionStringArray(){
		String[] ret = new String[primaryScore.size() + 1];
		ret[0] = spacedChordName(ChordScaleDictionary.noteName(keyIndex), 3) + " ChordFunction:";
		int index = 1;
		for (ChordFunction cf: cfList){
			ret[index] = cf.simpleToString();
			index++;
		}
		return ret;
	}
	public ArrayList<KeyArea> makeKeyAreas(){
		ArrayList<KeyArea> kaList = new ArrayList<KeyArea>();
		boolean newKeyArea = true;
		KeyArea ka = new KeyArea();
		for (int index = 0; index < cfList.size(); index++){
			ChordFunction cf = cfList.get(index);
			ChordAnalysisObject cao = caoList.get(index);
			if (cf.isFunction){
				if (newKeyArea){
					ka = new KeyArea(index, keyIndex, key);
					ka.addChordFunction(cf, cao);
					newKeyArea = false;
				} else {
					ka.addChordFunction(cf, cao);
				}
			} else {
				if (!newKeyArea){
					ka.end(index - 1);
					kaList.add(ka);
					newKeyArea = true;
				}
			}
		}
		if (!newKeyArea){
			ka.end(cfList.size() - 1);
			kaList.add(ka);
		}
		return kaList;
	}
	public boolean allInOneKey(){
		int total = 0;
		for (int i: primaryScore){
			total += i;
		}
		if (total == primaryScore.size()){
			return true;
		} else {
			return false;
		}
	}

	
// privates-------------------------------------------------------------------------------------------
	private void doScores(ChordAnalysisObject cao){
		boolean add = false;
		for (ChordFunction cf: key.cfList){
			if (debug) parent.consolePrint("------------------------------------");
			if (debug) parent.consolePrint("testing for: " + cf.toString());
			int degree = (keyIndex + cf.degree) % 12;
			if (debug) parent.consolePrint("degree: " + degree + " cao.simpleRoot(): " + cao.simpleRoot());
			if (debug) parent.consolePrint("cf.chordType: " + cf.chordType + " cao.simpleChord(): " + cao.simpleChord());
			if (degree == cao.simpleRoot() && cf.chordType.equals(cao.simpleChord())){
				primaryScore.add(cf.primaryScore);
				cfList.add(cf);
				if (debug) parent.consolePrint("******** WINNER **********");
				add = true;
			} 
		}
		if (!add){
			primaryScore.add(0);
			cfList.add(new ChordFunction());
		}
	}
	private String spacedChordName(String name, int length){
		int extra = length - name.length();
		String space = "";
		if (extra > 0){
			for (int i = 0; i < extra; i++){
				space += " ";
			}
		}
		return name + space;
	}
}
