package main.java.da_utils.static_chord_scale_dictionary;

import java.util.ArrayList;

public class ChordMatchList {

	public ArrayList<NotePatternAnalysis> coList;
	CSDTestCase tcase;
	
	public ChordMatchList(int[] noteArr){
		tcase = new CSDTestCase("", noteArr);
		coList = CSD.getChordOptions(tcase.noteList);
	}
	public ChordMatchList(ArrayList<Integer> noteList){
		tcase = new CSDTestCase("", noteList);
		coList = CSD.getChordOptions(tcase.noteList);
	}
	public ArrayList<Integer> bassSuggestions(){
		ArrayList<Integer> iList = new ArrayList<Integer>();
		for (NotePatternAnalysis npa: coList){
			iList.add(npa.rootNote());
		}		
		return iList;
	}
	public ArrayList<String> getTriadSuggestion(){
		ArrayList<String> triadList = new ArrayList<String>();
		for (NotePatternAnalysis npa: coList){
			if (npa instanceof NP_Triad && npa.intervals().length == 3){
				triadList.add(npa.name());
			}
		}
		
		return triadList;
	}
	public ArrayList<String> getQuartadSuggestion(){
		ArrayList<String> quartadList = new ArrayList<String>();
		for (NotePatternAnalysis npa: coList){
			if (npa instanceof NP_Triad && npa.intervals().length == 4){
				quartadList.add(npa.name());
			}
		}
		
		return quartadList;
	}
	
	
	
	public String toString(){
		String str = "ChordMatchList: " + tcase.noteListToString();
		
		for (NotePatternAnalysis npa: coList){
			str += "\n" + npa.toString();
		}
		return str;
	}
}
