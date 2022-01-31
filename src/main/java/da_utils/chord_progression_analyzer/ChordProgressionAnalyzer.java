package main.java.da_utils.chord_progression_analyzer;

import java.util.ArrayList;
import java.util.Collections;
//import java.util.HashMap;

import main.java.da_utils.chord_progression_analyzer.chord_chunk.ChordChunkList;

/*
 * a new approach to the KeyScorer. uses the CSD rather than the ChordScaleDictionary
 * 
 * 
 */
		
public class ChordProgressionAnalyzer {

	
//	private LiveClip lc;
	private ChordChunkList ccl;
//	private ArrayList<CPAChordChunk> chunkList = new ArrayList<CPAChordChunk>();
	private static final String DEFAULT_REZ = "4n";
//	private HashMap<Integer, KeyAnalysis> analMap = new HashMap<Integer, KeyAnalysis>();
	private ArrayList<KeyAnalysis> analList = new ArrayList<KeyAnalysis>();


	public ChordProgressionAnalyzer(ChordChunkList ccl){
		init(ccl, DEFAULT_REZ);
	}
	public ChordProgressionAnalyzer(ChordChunkList ccl, String rez){
		init(ccl, rez);
	}
	private void init(ChordChunkList ccl, String rez){
		this.ccl = ccl;
		makeChordProgressionAnalysis();
	}

	private void makeChordProgressionAnalysis() {
		makeAnalList();
		KeyAnalysis bestKA = bestKeyAnalysis();
//		bestKA.dealWithXs();

	}

	public ArrayList<ChordInKeyObject> getBestChordAnalysis(){
		ArrayList<ChordInKeyObject> cikoList = bestKeyAnalysis().getAnalyzedChordInKeyObjects();
		Collections.sort(cikoList, ChordInKeyObject.positionComparator);
		return cikoList;
	}
	public String bestChordAnalysisToString(){
		String str = "bestChordAnalysis(): ";
		for (ChordInKeyObject ciko: getBestChordAnalysis()){
			str += " " + ciko.toStringWithKey();
		}
		return str;
	}
	private void makeAnalList() {
		analList.clear();
		for (int i = 0; i < 12; i++){
			analList.add(new KeyAnalysis(i, ccl));
		}
		Collections.sort(analList, KeyAnalysis.totalScoreComparator);
	}
	public int bestKeySignatureXML(){
		return analList.get(0).bestXMLkeySignature();
	}
	public KeyAnalysis bestKeyAnalysis(){
		return analList.get(0);
	}
	public String name(){
		return ccl.name();
	}
	public String toString(){
		String str = "ChordProgressionAnalyzer: " + ccl.name() + "--------------\n";
		for (KeyAnalysis ka: analList){
			str += addWhiteSpace("keyIndex: " + ka.keyIndex(), 12) + " " + ka.toString() + "\n";
		}
		return str;
	}
	private String addWhiteSpace(String str, int length) {
		for (int i = str.length(); i < length; i++){
			str += " ";
		}
		return str;
	}
	
}
