package main.java.da_utils.chord_progression_analyzer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.chord_progression_analyzer.chord_chunk.ChordChunk;
import main.java.da_utils.chord_progression_analyzer.chord_chunk.ChordChunkList;
import main.java.da_utils.static_chord_scale_dictionary.CSD;
import main.java.da_utils.static_chord_scale_dictionary.ModeObject;

/*
 * key specific analysis of a chord progression
 * 
 * ChordAnalysisObject makes structural analysis of a group of notes as a chord. The KeyAnalyzer 
 * does chord function analysis, and resolves things like whether it is a m7 or Maj6, or a m7b5 
 * or min6. Therefore, any chord progression should undergo key analysis if, for instance, you 
 * have instances of the above chords and you are doing a bass generation algorithm (repeated 
 * in ChordAnalysisObject class header)
 */
public class KeyAnalysis {

	private int keyIndex;
	public ArrayList<ChordInKeyObject> chordInKeyList = new ArrayList<ChordInKeyObject>();
	public static final String XX = "X";
	private int totalScore = 0;
	private boolean hasTotalScore = false;
	private int majorScore = 0;
	private int minorScore = 0;
	private boolean hasThreeChordScore = false;;
	private int threeChordScore = 0;
	private ChordProgressionAnalyzer cpaX;
	
	
	
	public KeyAnalysis(int keyIndex, ChordChunkList ccl){
		init(keyIndex, ccl);
	}
	public KeyAnalysis(int keyIndex, ChordInKeyObject ciko, LiveClip lc){
		LiveClip tempClip = new LiveClip(0, 0);
		double lastEnd = 0.0;
		for (LiveMidiNote lmn: ciko.cc.noteList){
			tempClip.addNote(lmn.note, ciko.cc.position(), ciko.cc.length(), 100, 0);
			if (lastEnd < ciko.cc.position() + ciko.cc.length()) lastEnd = ciko.cc.position() + ciko.cc.length();
		}
		tempClip.loopStart = lc.loopStart;					// so that the ChordChunkList.cleanUpLiveClip does not erase the note data
		tempClip.loopEnd = lastEnd;						// ditto
		ChordChunkList ccl = new ChordChunkList(ProgressionAnalyzer.DEFAULT_REZ, tempClip);
		init(keyIndex, ccl);
	}
	private void init(int keyIndex, ChordChunkList ccl){
		this.keyIndex = keyIndex;
		makeAnalysis(ccl);
	}
	public int keyIndex(){
		return keyIndex;
	}
	public int bestXMLkeySignature(){
		if (minorScore > majorScore){
			return CSD.musicXMLKeyFromKeyIndex(keyIndex + 3);
		} else {
			return CSD.musicXMLKeyFromKeyIndex(keyIndex);
		}
	}
	
	private void makeAnalysis(ChordChunkList ccl) {
		//System.out.println("KeyAnalysis.makeAnalysis(lc): keyIndex=" + keyIndex + " " + CSD.noteName(keyIndex));
		Iterator<ChordChunk> it = ccl.chunkListIterator();
		hasTotalScore = false;
		hasThreeChordScore = false;
		majorScore = 0;
		minorScore = 0;
		while (it.hasNext()){
			ChordChunk cc = it.next();
			chordInKeyList.add(getChordFunction(cc));
		}
		doUnequivocalKeyTyping();
		//System.out.println("Xcount=" + getXCount());
		
	}
	
	private void doUnequivocalKeyTyping() {
		if (minorScore == 0 && majorScore > 0){
			for (ChordInKeyObject ciko: chordInKeyList){
				ciko.unequivocallyLocalMajor = true;
			}
		} else if (majorScore == 0 && minorScore > 0){
			for (ChordInKeyObject ciko: chordInKeyList){
				ciko.unequivocallyLocalMinor = true;
			}
		}
		
	}
	public int getXCount() {
		int count = 0;
		for (ChordInKeyObject ciko: chordInKeyList){
			if (ciko.degree.equals(XX)){
				count++;
			}
		}
		return count;
	}
	public ArrayList<ChordInKeyObject> getAnalyzedChordInKeyObjects(){
		ArrayList<ChordInKeyObject> cikoList = new ArrayList<ChordInKeyObject>();
		if (cpaX != null){
			cikoList.addAll(cpaX.bestKeyAnalysis().getAnalyzedChordInKeyObjects());
		}
		for (ChordInKeyObject ciko: chordInKeyList){
//			if (!ciko.degree.equals(XX)){
				cikoList.add(ciko);
//			}
		}
		return cikoList;
	}
//	public void dealWithXs() {
//		if (getXCount() > 0){
//			LiveClip lc = makeLiveClipOfXs();
//			cpaX = new ChordProgressionAnalyzer(lc);
//		}
//		
//	}
	private LiveClip makeLiveClipOfXs() {
		LiveClip lc = new LiveClip(0, 0);
		for (ChordInKeyObject ciko: chordInKeyList){
			if (ciko.degree.equals(KeyAnalysis.XX)){
				for (LiveMidiNote lmn: ciko.cc.noteList){
					lc.addNote(lmn.note, ciko.cc.position(), ciko.cc.length(), 100, 0); 	// 100 - arbitrary velocity, 0 - unmuted
				}
			}
		}
		return lc;
	}
	
	private ChordInKeyObject getChordFunction(ChordChunk cc) {


		int rootDegree = (cc.rootModNote() + 12 - keyIndex) % 12;
		String type = cc.chordTypeName();
		//System.out.println("root=" + cc.simpleRoot() + " degree=" + rootDegree + " name=" + type);

		FunctionResult funcResult = nullScoreAndFunction ;
		if (type.equals("Maj")){
			funcResult = do_Maj(rootDegree, type, cc);
		} else if(type.equals("min")){
			funcResult = do_min(rootDegree, type, cc);
		} else if(type.equals("m7") || type.equals("m9")){
			funcResult = do_m7(rootDegree, type, cc);
		} else if(type.equals("7") 
				|| type.equals("7#5") 
				|| type.equals("9")
				|| type.equals("7#9")
				|| type.equals("7b9")
				|| type.equals("7#5#9")
				|| type.equals("7#5b9")
				|| type.equals("9#5")){
			funcResult = do_7(rootDegree, type, cc);
//		} else if(type.equals("7#5")){
//			funcResult = do_7sharp5(rootDegree, type, cc);
		} else if (type.equals("m7b5")){
			funcResult = do_m7b5(rootDegree, type, cc);
		} else if (type.equals("Maj7")
				|| type.equals("Maj9")
				|| type.equals("6/9")
				|| type.equals("Maj7#5")
				|| type.equals("Maj7b5")
				|| type.equals("Maj9#5")){
			funcResult = do_Maj7(rootDegree, type, cc);
		} else if (type.equals("dim7")){
			funcResult = do_dim7(rootDegree, type, cc);
		}
		String chordType = cc.chordTypeName();
//		if (funcResult.rootAdjust > 0){
//			int root = (cc.rootModNote() + funcResult.rootAdjust) % 12;
//			chordSymbol = CSD.noteName(root) + funcResult.type;
//		}
		ChordInKeyObject ciko = new ChordInKeyObject(cc, keyIndex, 
				chordType, 
				funcResult.functionName(), 
				funcResult.score, 
				cc.rootModNote() + funcResult.rootAdjust,
				funcResult.threeChordsScore(),
				funcResult.degree,
				funcResult.majorScore,
				funcResult.minorScore,
				funcResult.mode,
				funcResult.degreeAddendum);
				
		return ciko;
	}
	private FunctionResult do_dim7(int rootDegree, String chordType, ChordChunk cc) {
		String degree = XX;
		int score = missScore;
		int tonicScore = 0;
		int dominantScore = 0;
		int subDominantScore = 0;
		int localMajorScore = 0;
		int localMinorScore = 0;
		String degreeAddendum = "";
		ModeObject mo = CSD.CHROMATIC_SCALE;
		// need diminished scales in CSD.......................................
		
		switch(rootDegree){
		case 0: degree = "#iv";
				score = primaryScore;
				break;
		case 2:	degree = "vii";
				score = primaryScore;
				break;
		case 3: degree = "#iv";
				score = primaryScore;
				break;
//		case 4: degree = "iii";
//				score = primaryScore;
//				break;
		case 5: degree = "vii";
				score = primaryScore;
				break;
		case 6: degree = "#iv";
				score = primaryScore;
				break;
//		case 7: degree = "V";
//				score = primaryScore;
//				dominantScore = HIGH_3CHORD_SCORE + 1;
//				break;
		case 8: degree = "vii";
				score = primaryScore;
				break;
		case 9: degree = "#iv";
				score = primaryScore;
				break;
//		case 10: degree = "bVI";
//				score = primaryScore;
	//			break;
		case 11: degree = "vii";
				score = primaryScore;
				break;
		}
		majorScore += localMajorScore;
		minorScore += localMinorScore;
		return new FunctionResult(score, degree, degreeAddendum, chordType, tonicScore, dominantScore, subDominantScore, localMajorScore, localMinorScore, mo);
	}
	private FunctionResult do_7sharp5(int rootDegree, String chordType, ChordChunk cc) {
		String degree = XX;
		int score = missScore;
		int tonicScore = 0;
		int dominantScore = 0;
		int subDominantScore = 0;
		int localMajorScore = 0;
		int localMinorScore = 0;
		String degreeAddendum = "";
		ModeObject mo = CSD.CHROMATIC_SCALE;
		
		switch(rootDegree){
//		case 0: degree = "i";
//				score = primaryScore;
//				break;
//		case 2:	degree = "ii";
//				score = primaryScore;
//				break;
//		case 4: degree = "iii";
//				score = primaryScore;
//				break;
//		case 5: degree = "iv";
//				score = primaryScore;
//				break;
		case 7: degree = "V";
				score = primaryScore;
				dominantScore = HIGH_3CHORD_SCORE + 1;
				mo = CSD.ALTERED;
				break;
//		case 9: degree = "bVI";
//				score = primaryScore;
//				break;
//		case 10: degree = "bVI";
//				score = primaryScore;
	//			break;
//		case 11: degree = "bVI";
//				score = primaryScore;
//				break;
		}
		majorScore += localMajorScore;
		minorScore += localMinorScore;
		return new FunctionResult(score, degree, degreeAddendum, chordType, tonicScore, dominantScore, subDominantScore, localMajorScore, localMinorScore, mo);

	}
	private FunctionResult do_m7b5(int rootDegree, String chordType, ChordChunk cc) {
		String degree = XX;
		String degreeAddendum = "";
		ModeObject mo = CSD.CHROMATIC_SCALE;
		
		int score = missScore;
		int tonicScore = 0;
		int dominantScore = 0;
		int subDominantScore = 0;
		int rootAdjust = 0;
		int localMajorScore = 0;
		int localMinorScore = 0;
		
		switch(rootDegree){

		case 2:	degree = "ii";
				score = primaryScore;
				subDominantScore = HIGH_3CHORD_SCORE;
				localMinorScore++;
				mo = CSD.LOCRIAN_MODE;
				break;
		case 9: degree = "i";
				score = primaryScore;
				rootAdjust = 3;
				chordType = "m6";
				localMinorScore++;
				mo = CSD.DORIAN_MODE;
				break;
		case 11: degree = "vii";
				score = primaryScore;
				dominantScore = MEDIUM_3CHORD_SCORE;
				localMajorScore++;
				mo = CSD.LOCRIAN_MODE;
				break;
		}
		majorScore += localMajorScore;
		minorScore += localMinorScore;
		return new FunctionResult(
				score, degree, degreeAddendum, 
				rootAdjust, chordType, 
				tonicScore, dominantScore, 
				subDominantScore, localMajorScore, localMinorScore, mo);
	}
	private FunctionResult do_7(int rootDegree, String chordType, ChordChunk cc) {
		String degree = XX;
		int score = missScore;
		int tonicScore = 0;
		int dominantScore = 0;
		int subDominantScore = 0;
		ChordChunk cc2 = cc.next();
		ChordChunk ccMinus1 = cc.previous();
//		ChordAnalysisObject cc4 = cc3.next();
		int rootMove_1to2 = (cc2.rootModNote() + 12 - cc.rootModNote()) % 12;
//		int rootMove_2to3 = (cc3.simpleRoot() + 12 - cc2.simpleRoot()) % 12;
//		int rootMove_3to4 = (cc4.simpleRoot() + 12 - cc3.simpleRoot()) % 12;
		int localMajorScore = 0;
		int localMinorScore = 0;
		String degreeAddendum = "";
		ModeObject mo = CSD.CHROMATIC_SCALE;
		
		switch(rootDegree){
		case 0: degree = "I";
				score = primaryScore;
				tonicScore = this.HIGH_3CHORD_SCORE;
				mo = CSD.MIXOLYDIAN_MODE;
				break;
		case 1:	degree = "bII";
				score = substituteDominantScore;
				mo = CSD.LYDIAN_DOMINANT;
				break;
			//}
		case 2:	//if (rootMove_1to2 == 5 || (rootMove_1to2 == 0 && cc2.isMinorType)){
				degree = "V";
				score = secondaryDominantScore;
				degreeAddendum = "/V";
				mo = CSD.MIXOLYDIAN_MODE;
				//}
				
				break;
		case 4: if (rootMove_1to2 == 1 && cc2.isMajorType()){
					degree = "III";
					score = secondaryDominantScore;
					localMajorScore++;
				} else if (rootMove_1to2 == 5 && cc2.isMinorType()){
					degree = "V";
					degreeAddendum = "/vi";
					score = secondaryDominantScore;
					localMajorScore++;
				}
				mo = CSD.HARMONIC_MINOR_FIVE;
				break;
		case 5: degree = "IV";
				score = primaryScore;
				mo = CSD.LYDIAN_DOMINANT;
				break;
		case 7: degree = "V";
				if (!cc.isSameTypeAndRootAs(ccMinus1)){		// repeated chords do not score extra					
					dominantScore = HIGH_3CHORD_SCORE + 1;
				} else if (cc == ccMinus1){		// only one chord
					dominantScore = HIGH_3CHORD_SCORE + 1;
				}
				score = primaryScore;
				
				//***************************************************************
				boolean sortedDueToChordStructure = false;
				if (cc.isDominantType()){
					if (cc.isNinthChord()){
						sortedDueToChordStructure = true;
						if (cc.isFlatNinth()){
							mo = CSD.HARMONIC_MINOR_FIVE;
							
						} else {
							mo = CSD.MIXOLYDIAN_MODE;
						}
						// need more code to deal with #9............
						if (rootMove_1to2 == 5){
							if (cc2.isMinorType()){
								localMinorScore++;
							}
							if (cc2.isMajorType()){
								localMajorScore++;
							}
						}
					}
				}
				//***************************************************************
				if (!sortedDueToChordStructure){
					if (rootMove_1to2 == 5 && cc2.isMinorType()) {
						localMinorScore++;
						mo = CSD.HARMONIC_MINOR_FIVE;
					}
					if (rootMove_1to2 == 5 && cc2.isMajorType()){
						localMajorScore++;
						mo = CSD.MIXOLYDIAN_MODE;
					}
				}
				
				break;
		case 8:	degree = "bII";
				degreeAddendum = "/V";
				score = substituteDominantScore;
				mo = CSD.LYDIAN_DOMINANT;
				break;
		case 9: degree = "V";
				degreeAddendum = "/ii";
				score = secondaryDominantScore;
				mo = CSD.MIXOLYDIAN_FLAT_13;
				break;
		case 10: degree = "bVII";
				score = primaryScore;
				dominantScore = MEDIUM_3CHORD_SCORE;
				if (rootMove_1to2 == 2 && cc2.isMinorType()) {
					localMinorScore++;
					mo = CSD.MIXOLYDIAN_MODE;
				}
				if (rootMove_1to2 == 2 && cc2.isMajorType()) {
					localMajorScore++;
					mo = CSD.LYDIAN_DOMINANT;
				}
				break;
//		case 11: degree = "bVI";
//				score = primaryScore;
//				break;
		}
		majorScore += localMajorScore;
		minorScore += localMinorScore;
		return new FunctionResult(score, degree, degreeAddendum, chordType, tonicScore, dominantScore, subDominantScore, localMajorScore, localMinorScore, mo);
	}

	private FunctionResult do_m7(int rootDegree, String chordType, ChordChunk cc) {
		String degree = XX;
		int score = missScore;
		int tonicScore = 0;
		int dominantScore = 0;
		int subDominantScore = 0;
		int rootAdjust = 0;
		int localMajorScore = 0;
		int localMinorScore = 0;
		String degreeAddendum = "";
		ModeObject mo = CSD.CHROMATIC_SCALE;
		
		switch(rootDegree){
		case 0: degree = "i";
				score = primaryScore;
				tonicScore = this.HIGH_3CHORD_SCORE;
				localMinorScore++;
				mo = CSD.AOELIAN_MODE;
				break;
		case 2:	degree = "ii";
				score = primaryScore;
				subDominantScore = this.HIGH_3CHORD_SCORE;
				if (cc.next().isDominantType() && (cc.rootModNote() + 5) % 12 == cc.next().rootModNote()){
					subDominantScore += 1;
				}
				localMajorScore++;
				mo = CSD.DORIAN_MODE;
				break;
		case 4: degree = "iii";
				score = primaryScore;
				localMajorScore++;
				mo = CSD.PHRYGIAN_MODE;
				break;
		case 5: degree = "iv";
				score = primaryScore;
				subDominantScore = this.HIGH_3CHORD_SCORE;
				localMinorScore++;
				mo = CSD.DORIAN_MODE;
				break;
		case 9: degree = "vi";
				score = primaryScore;
				localMajorScore++;
				mo = CSD.AOELIAN_MODE;
				break;

		}
		majorScore += localMajorScore;
		minorScore += localMinorScore;
		return new FunctionResult(score, degree, degreeAddendum, rootAdjust, chordType, tonicScore, dominantScore, subDominantScore, localMajorScore, localMinorScore, mo);
	}
	private FunctionResult do_min(int rootDegree, String chordType, ChordChunk cc) {
		String degree = XX;
		int score = missScore;
		int tonicScore = 0;
		int dominantScore = 0;
		int subDominantScore = 0;
		int localMajorScore = 0;
		int localMinorScore = 0;
		String degreeAddendum = "";
		ModeObject mo = CSD.CHROMATIC_SCALE;
		
		switch(rootDegree){
		case 0: degree = "i";
				score = primaryScore;
				tonicScore = KeyAnalysis.HIGH_3CHORD_SCORE + 1;
				localMinorScore++;
				mo = CSD.AOELIAN_MODE;
				break;
		case 2:	degree = "ii";
				score = primaryScore;
				subDominantScore = KeyAnalysis.HIGH_3CHORD_SCORE;
				localMajorScore++;
				mo = CSD.DORIAN_MODE;
				break;
		case 4: degree = "iii";
				score = primaryScore;
				localMajorScore++;
				mo = CSD.PHRYGIAN_MODE;
				break;
		case 5: degree = "iv";
				score = primaryScore;
				subDominantScore = KeyAnalysis.HIGH_3CHORD_SCORE;
				localMinorScore++;
				mo = CSD.DORIAN_MODE;
				break;
		case 7: degree = "v";
				score = primaryScore;
				localMinorScore++;
				mo = CSD.PHRYGIAN_MODE;
				break;
		case 9: degree = "vi";
				score = primaryScore;
				localMajorScore++;
				mo = CSD.AOELIAN_MODE;
				break;
		}
		majorScore += localMajorScore;
		minorScore += localMinorScore;
		return new FunctionResult(score, degree, degreeAddendum, chordType, tonicScore, dominantScore, subDominantScore, localMajorScore, localMinorScore, mo);
	}
	private FunctionResult do_Maj(int rootDegree, String chordType, ChordChunk cc) {
		String degree = XX;
		int score = missScore;
		int tonicScore = 0;
		int dominantScore = 0;
		int subDominantScore = 0;
		int localMajorScore = 0;
		int localMinorScore = 0;
		String degreeAddendum = "";
		ChordChunk cc2 = cc.next();
		ChordChunk ccMinus1 = cc.previous();
//		ChordAnalysisObject cc4 = cc3.next();
		int rootMove_1to2 = (cc2.rootModNote() + 12 - cc.rootModNote()) % 12;
//		int rootMove_2to3 = (cc3.simpleRoot() + 12 - cc2.simpleRoot()) % 12;
//		int rootMove_3to4 = (cc4.simpleRoot() + 12 - cc3.simpleRoot()) % 12;
		
		ModeObject mo = CSD.CHROMATIC_SCALE;
		
		switch(rootDegree){
		case 0:	degree = "I";
				//System.out.println("KeyAnalysis.do_Maj case 0 default: major/minorScore=" + majorScore + "/" + minorScore + " keyIndex=" + keyIndex);
				score = primaryScore;
				tonicScore = this.HIGH_3CHORD_SCORE;
				localMajorScore++;
				//System.out.println("KeyAnalysis.do_Maj case 0 default: major/minorScore=" + majorScore + "/" + minorScore + " keyIndex=" + keyIndex);
				mo = CSD.IONIAN_MODE;
				break;
		case 2: degree = "II";
				if (majorScore > minorScore){
					mo = CSD.MIXOLYDIAN_MODE;
				} else {
					mo = CSD.HARMONIC_MINOR_FIVE;
				}
				break;
		case 3:	degree = "bIII";
				score = primaryScore;
				localMinorScore++;
				mo = CSD.IONIAN_MODE;
				break;
		case 5: degree = "IV";
				score = primaryScore;
				subDominantScore = this.HIGH_3CHORD_SCORE;			
				if (minorScore > majorScore){
					mo = CSD.MIXOLYDIAN_MODE;
				} else {
					mo = CSD.LYDIAN_MODE;
				}
				localMajorScore++;
				break;
		case 7: degree = "V";					// V or V7 does not score one way or another with major or minor
				score = primaryScore;
				dominantScore = this.HIGH_3CHORD_SCORE + 1;
				switch (rootMove_1to2){
					case 5:
						if (cc2.isMinorType()){
							localMinorScore++;
							mo = CSD.HARMONIC_MINOR_FIVE;
						} else if (cc2.isMajorType()){
							localMajorScore++;
							mo = CSD.MIXOLYDIAN_MODE;
						} else {
							if (majorScore > minorScore){
								mo = CSD.MIXOLYDIAN_MODE;
							} else {
								mo = CSD.HARMONIC_MINOR_FIVE;
							}
						}
						break;
					case 2:
						if (cc2.isMinorType()){
							localMajorScore++;
							mo = CSD.MIXOLYDIAN_MODE;
						} else {
							if (majorScore > minorScore){
								mo = CSD.MIXOLYDIAN_MODE;
							} else {
								mo = CSD.HARMONIC_MINOR_FIVE;
							}
						break;
					}
					default:
						//System.out.println("KeyAnalysis.do_Maj case 7 default: major/minorScore=" + majorScore + "/" + minorScore + " keyIndex=" + keyIndex);
						if (majorScore > minorScore){
							mo = CSD.MIXOLYDIAN_MODE;
						} else {
							mo = CSD.HARMONIC_MINOR_FIVE;
						}
						break;
					}
//				if (rootMove_1to2 == 5){
//					if (cc2.isMinorType()){
//						localMinorScore++;
//						mo = CSD.HARMONIC_MINOR_FIVE;
//					} else if (cc2.isMajorType()){
//						localMajorScore++;
//						mo = CSD.MIXOLYDIAN_MODE;
//					} else {
//						if (majorScore > minorScore){
//							mo = CSD.MIXOLYDIAN_MODE;
//						} else {
//							mo = CSD.HARMONIC_MINOR_FIVE;
//						}
//					}
	//			
	///			} else {
	//				System.out.println("localMajorScore=" + localMajorScore + " localMinorScore=" + localMinorScore);
	//				if (localMajorScore > localMinorScore){
	//					mo = CSD.MIXOLYDIAN_MODE;
	//				} else {
	//					mo = CSD.HARMONIC_MINOR_FIVE;
	//				}
				
//				if (rootMove_1to2 == 5 && cc2.isMinorType()) {
//					localMinorScore++;
//					mo = CSD.HARMONIC_MINOR_FIVE;
//				}
//				if (rootMove_1to2 == 5 && cc2.isMajorType()){
//					localMajorScore++;
//					mo = CSD.MIXOLYDIAN_MODE;
//				}
				
				
				break;
		case 8: degree = "bVI";
				score = primaryScore;
				localMinorScore++;
				mo = CSD.LYDIAN_MODE;
				break;
		case 9: if (rootMove_1to2 == 5){
				degree = "V";
				score = secondaryDominantScore;
				degreeAddendum = "/ii";
				mo = CSD.MIXOLYDIAN_FLAT_13;
		}
				break;
				
		case 10: degree = "bVII";
				score = primaryScore;
				localMinorScore++;
				mo = CSD.MIXOLYDIAN_MODE;
				if (rootMove_1to2 == 5 && cc2.isMajorType()){
					//localMajorScore++;
					mo = CSD.LYDIAN_DOMINANT;
				}
				if (majorScore > minorScore){
					//localMajorScore++;
					mo = CSD.LYDIAN_MODE;
				}
				break;
		}
		majorScore += localMajorScore;
		minorScore += localMinorScore;
		return new FunctionResult(score, degree, degreeAddendum, chordType, tonicScore, dominantScore, subDominantScore, localMajorScore, localMinorScore, mo);
		
	}
	private FunctionResult do_Maj7(int rootDegree, String chordType, ChordChunk cc) {
		String degree = XX;
		int score = missScore;
		int tonicScore = 0;
		int dominantScore = 0;
		int subDominantScore = 0;
		int localMajorScore = 0;
		int localMinorScore = 0;
		String degreeAddendum = "";
		ModeObject mo = CSD.CHROMATIC_SCALE;
		
		switch(rootDegree){
		case 0:	degree = "I";
				score = primaryScore;
				tonicScore = this.HIGH_3CHORD_SCORE;
				localMajorScore++;
				mo = CSD.IONIAN_MODE;
				break;
		case 3:	degree = "bIII";
				score = primaryScore;
				localMinorScore++;
				mo = CSD.IONIAN_MODE;
				break;
		case 5: degree = "IV";
				score = primaryScore;
				subDominantScore = this.HIGH_3CHORD_SCORE;
				localMajorScore++;
				mo = CSD.LYDIAN_MODE;
				break;
		case 8: degree = "bVI";
				score = primaryScore;
				localMinorScore++;
				mo = CSD.LYDIAN_MODE;
				break;

		}
		majorScore += localMajorScore;
		minorScore += localMinorScore;
		return new FunctionResult(score, degree, degreeAddendum, chordType, tonicScore, dominantScore, subDominantScore, localMajorScore, localMinorScore, mo);
		
	}
	public int score(){
		if (!hasTotalScore){
			totalScore = 0;
			for (ChordInKeyObject ciko: chordInKeyList){
				totalScore += ciko.score;
			}
			hasTotalScore = true;
		} 
		return totalScore;
	}
	public int threeChordScore(){
		if (!hasThreeChordScore){
			threeChordScore = 0;
			for (ChordInKeyObject ciko: chordInKeyList){
				threeChordScore += ciko.threeChordScore;
			}
			hasThreeChordScore = true;
		} 
		return threeChordScore;
	}
	public static Comparator<KeyAnalysis> totalScoreComparator = new Comparator<KeyAnalysis>(){
		public int compare(KeyAnalysis anal1, KeyAnalysis anal2){
			// prioritizes complete analyses. may break the tests
			if (anal1.getXCount() < anal2.getXCount()) return -1;
			if (anal1.getXCount() > anal2.getXCount()) return 1;
			
			
			if (anal1.score() < anal2.score()) return -1;
			if (anal1.score() > anal2.score()) return 1;
			if (anal1.threeChordScore() < anal2.threeChordScore) return 1;
			if (anal1.threeChordScore() > anal2.threeChordScore()) return -1;
			return 0;
		}
	};
	public String toString(){
		String str = addWhiteSpace("keyAnalysis: " + CSD.noteNameWithKeyIndex(keyIndex, keyIndex) + minorOrMajor(), 20);
		for (ChordInKeyObject ciko: chordInKeyList){
			str += " / " + addWhiteSpace(ciko.toString(), toStringLength);
		}
		str += " <XCount=" + getXCount() + " totalScore=" + score() + ",threeChordScore=" + threeChordScore();
		str += " majorScore=" + totalMajorScoreFromCikoList() + " minorScore=" + totalMinorScoreFromCikoList() + ">";
		return str;
	}
	private String addWhiteSpace(String str, int length) {
		for (int i = str.length(); i < length; i++){
			str += " ";
		}
		return str;
	}
	private String minorOrMajor() {
		if (minorScore > majorScore){
			return CSD.MINOR;
		} else {
			return CSD.MAJOR;
		}
	}
	private int totalMajorScoreFromCikoList(){
		int count = 0;
		for (ChordInKeyObject ciko: chordInKeyList){
			count += ciko.majorScore;
		}
		return count;
	}
	private int totalMinorScoreFromCikoList(){
		int count = 0;
		for (ChordInKeyObject ciko: chordInKeyList){
			count += ciko.minorScore;
		}
		return count;
	}
	
	private static final int toStringLength = 15;
	
	private static final int primaryScore = 0;
	private static final int secondaryDominantScore = 0;
	private static final int substituteDominantScore = 0;
	private static final int missScore = 10;
	private static final int HIGH_3CHORD_SCORE = 10;
	private static final int MEDIUM_3CHORD_SCORE = 5;
	private static final FunctionResult nullScoreAndFunction = new FunctionResult(missScore, XX, "", "", 0, 0, 0, 0, 0, CSD.CHROMATIC_SCALE);




	
}
