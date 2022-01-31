package main.java.da_utils.chord_progression_analyzer;

import main.java.da_utils.static_chord_scale_dictionary.ModeObject;

public class FunctionResult {

	
	public String degree;
	public String type;
	public int score;
	public int rootAdjust = 0;
	public int tonicScore;
	public int dominantScore;
	public int subDominantScore;
	public int majorScore = 0;
	public int minorScore = 0;
	public String degreeAddendum;
	public ModeObject mode;
	
	public FunctionResult(int score, String degree, String degreeAddendum, int rootAdjust, String chordType, 
			int tonicScore, int dominantScore, int subDominantScore, int majorScore, int minorScore, ModeObject mode){
		this.degree = degree;
		this.score = score;
		this.rootAdjust =rootAdjust;
		this.type = chordType;
		this.tonicScore = tonicScore;
		this.dominantScore = dominantScore;
		this.subDominantScore = subDominantScore;
		this.majorScore = majorScore;
		this.minorScore = minorScore;
		this.degreeAddendum = degreeAddendum;
		this.mode = mode;
	}
	public FunctionResult(int score, String degree, String degreeAddendum, String chordType,
			int tonicScore, int dominantScore, int subDominantScore, int majorScore, int minorScore, ModeObject mode){
		this.degree = degree;
		this.score = score;
		this.type = chordType;
		this.tonicScore = tonicScore;
		this.dominantScore = dominantScore;
		this.subDominantScore = subDominantScore;
		this.majorScore = majorScore;
		this.minorScore = minorScore;
		this.degreeAddendum = degreeAddendum;
		this.mode = mode;
	}
	public String functionName(){
		return degree + type + degreeAddendum;
	}
	public int threeChordsScore(){
		return tonicScore + dominantScore + subDominantScore;
	}
}
