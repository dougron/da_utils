package main.java.da_utils.static_chord_scale_dictionary;

import java.util.ArrayList;
import java.util.List;

import acm.program.ConsoleProgram;

public class StaticChordScaleDictionaryConsoleTest
{

	
	public StaticChordScaleDictionaryConsoleTest(){
//		testCSDNotePatternResponse();
//		testIntervalReturn();
//		testTriadReturn();
//		testOddTriadReturn();
//		testQuartadReturn();
//		test9thchordReturn();
//		testScalesAndModes();
		testChordSymbolDecoder();
		
	}
	private void testChordSymbolDecoder() {
		int[] intervals = CSD.getChordTones("A");
		for (int i: intervals) {
			System.out.println(i);
		}
	}
	private void testScalesAndModes() {
		ModeObject[] moArr = new ModeObject[]{
				CSD.IONIAN_MODE, CSD.DORIAN_MODE, CSD.PHRYGIAN_MODE, CSD.LYDIAN_MODE, 
				CSD.MIXOLYDIAN_MODE, CSD.AOELIAN_MODE, CSD.LOCRIAN_MODE,
				CSD.HARMONIC_MINOR_FIVE, CSD.HARMONIC_MINOR_ONE, CSD.MELODIC_MINOR_ONE,
				CSD.DORIAN_FLAT_TWO, CSD.LYDIAN_SHARP_FIVE, CSD.LYDIAN_DOMINANT, CSD.MIXOLYDIAN_FLAT_13,
				CSD.LOCIAN_NATURAL_9, CSD.ALTERED};
//		for (ModeObject mo: moArr){
//			System.out.println(mo.name);
//			int[] arr = CSD.getDiatonicModeDegrees(mo);
//			for (int i: arr){
//				System.out.println(i);
//			}
//		}
		for (ModeObject mo: moArr){
			System.out.println(mo.name);
			int[][] arr = CSD.getChromaticModeModel(mo);
			for (int[] ii: arr){
				System.out.println(ii[0] + ", " + ii[1]);
			}
		}
		
		
	}
	private void test9thchordReturn() {
		for (CSDTestCase tcase: make9thChordList()){
			List<NotePatternAnalysis> coList = CSD.getChordOptions(tcase.noteList);
			printTestCaseNotes(tcase);		
			System.out.println(coList.size() + " in coList");
			for (NotePatternAnalysis npa: coList){
				System.out.println(npa.toString());
			}
			System.out.println("----------------------------------------");
		}
		
	}
	private void testQuartadReturn() {
		for (CSDTestCase tcase: makeQuartadList()){
			List<NotePatternAnalysis> coList = CSD.getChordOptions(tcase.noteList);
			printTestCaseNotes(tcase);		
			System.out.println(coList.size() + " in coList");
			for (NotePatternAnalysis npa: coList){
				System.out.println(npa.toString());
			}
			System.out.println("----------------------------------------");
		}
		
	}
	private void testTriadReturn() {
		for (CSDTestCase tcase: makeTriadTestList()){
			List<NotePatternAnalysis> coList = CSD.getChordOptions(tcase.noteList);
			printTestCaseNotes(tcase);		
			System.out.println(coList.size() + " in coList");
			for (NotePatternAnalysis npa: coList){
				System.out.println(npa.toString());
			}
			System.out.println("----------------------------------------");
		}
		
	}
	private void testOddTriadReturn() {
		for (CSDTestCase tcase: makeOddTriadTestList()){
			List<NotePatternAnalysis> coList = CSD.getChordOptions(tcase.noteList);
			printTestCaseNotes(tcase);		
			System.out.println(coList.size() + " in coList");
			for (NotePatternAnalysis npa: coList){
				System.out.println(npa.toString());
			}
			System.out.println("----------------------------------------");
		}
		
	}
	private void testIntervalReturn() {
		for (CSDTestCase tcase: makeIntervalTestList()){
			List<NotePatternAnalysis> coList = CSD.getChordOptions(tcase.noteList);
			printTestCaseNotes(tcase);		
			System.out.println(coList.size() + " in coList");
			for (NotePatternAnalysis npa: coList){
				System.out.println(npa.toString());
			}
			System.out.println("----------------------------------------");
		}
		
	}
	private void printTestCaseNotes(CSDTestCase tcase) {
		String str = "notes: ";
		for (Integer i: tcase.noteList){
			str += CSD.noteName(i) + ", ";
		}
		System.out.println(str);
		
	}
	private void testCSDNotePatternResponse(){
		for (CSDTestCase tcase: makeList()){
			System.out.println(tcase.name);
			CSD.getChordOptions(tcase.noteList);
		}
	}
	private ArrayList<CSDTestCase> makeShortList() {
		
		ArrayList<CSDTestCase> caseList = new ArrayList<CSDTestCase>();
		caseList.add(new CSDTestCase("7b5-root", new int[]{50, 54, 56, 60}));
		caseList.add(new CSDTestCase("7b5-1st", new int[]{50, 52, 56, 58}));
		caseList.add(new CSDTestCase("7b5-2nd", new int[]{50, 54, 56, 60}));
		caseList.add(new CSDTestCase("7b5-3rd", new int[]{50, 52, 56, 58}));
		return caseList;
	}
	private ArrayList<CSDTestCase> makeTriadTestList() {
		ArrayList<CSDTestCase> caseList = new ArrayList<CSDTestCase>();
		caseList.add(new CSDTestCase("min-root", new int[]{50, 53, 57}));
		caseList.add(new CSDTestCase("min-1st", new int[]{50, 54, 59}));
		caseList.add(new CSDTestCase("min-2nd", new int[]{50, 55, 58}));
		caseList.add(new CSDTestCase("maj-root", new int[]{50, 54, 57}));
		caseList.add(new CSDTestCase("maj-1st", new int[]{50, 53, 58}));
		caseList.add(new CSDTestCase("maj-2nd", new int[]{50, 55, 59}));
		
		caseList.add(new CSDTestCase("dim-root", new int[]{50, 53, 56}));
		caseList.add(new CSDTestCase("dim-1st", new int[]{50, 53, 59}));
		caseList.add(new CSDTestCase("dim-2nd", new int[]{50, 56, 59}));
		
		caseList.add(new CSDTestCase("aug-root", new int[]{50, 54, 58}));
		
		caseList.add(new CSDTestCase("sus4-root", new int[]{50, 55, 57}));
		caseList.add(new CSDTestCase("sus4-1st", new int[]{50, 52, 57}));
		caseList.add(new CSDTestCase("sus4-2nd", new int[]{50, 55, 60}));
		return caseList;
	}
	private ArrayList<CSDTestCase> makeOddTriadTestList() {
		ArrayList<CSDTestCase> caseList = new ArrayList<CSDTestCase>();
		caseList.add(new CSDTestCase("Maj7no3-root", new int[]{50, 57, 61}));
		caseList.add(new CSDTestCase("Maj7no3-2st", new int[]{62, 57, 61}));
		caseList.add(new CSDTestCase("Maj7no3-3nd", new int[]{62, 69, 61}));
		
		caseList.add(new CSDTestCase("min7no3-root", new int[]{50, 57, 60}));
		caseList.add(new CSDTestCase("min7no3-2st", new int[]{62, 57, 60}));
		caseList.add(new CSDTestCase("min7no3-3nd", new int[]{62, 69, 60}));
		
		caseList.add(new CSDTestCase("Maj7no5-root", new int[]{50, 54, 61}));
		caseList.add(new CSDTestCase("Maj7no5-2st", new int[]{62, 54, 61}));
		caseList.add(new CSDTestCase("Maj7no5-3nd", new int[]{62, 66, 61}));
		
		caseList.add(new CSDTestCase("min7no5-root", new int[]{50, 53, 60}));
		caseList.add(new CSDTestCase("min7no5-2st", new int[]{62, 53, 60}));
		caseList.add(new CSDTestCase("min7no5-3nd", new int[]{62, 65, 60}));
		
		caseList.add(new CSDTestCase("7no5-root", new int[]{50, 54, 60}));
		caseList.add(new CSDTestCase("7no5-2st", new int[]{62, 54, 60}));
		caseList.add(new CSDTestCase("7no5-3nd", new int[]{62, 66, 60}));
		
		caseList.add(new CSDTestCase("mMaj7no5-root", new int[]{50, 53, 61}));
		caseList.add(new CSDTestCase("mMaj7no5-2st", new int[]{62, 53, 61}));
		caseList.add(new CSDTestCase("mMaj7no5-3nd", new int[]{62, 65, 61}));
		return caseList;
	}

	private ArrayList<CSDTestCase> makeIntervalTestList() {
		ArrayList<CSDTestCase> caseList = new ArrayList<CSDTestCase>();
		caseList.add(new CSDTestCase("m2", new int[]{1, 2}));
		caseList.add(new CSDTestCase("M2", new int[]{1, 3}));
		caseList.add(new CSDTestCase("m3", new int[]{1, 4}));
		caseList.add(new CSDTestCase("M3", new int[]{1, 5}));
		caseList.add(new CSDTestCase("p4", new int[]{1, 6}));
		caseList.add(new CSDTestCase("p5", new int[]{1, 8}));
		caseList.add(new CSDTestCase("m6th", new int[]{1, 9}));
		caseList.add(new CSDTestCase("M6th", new int[]{1, 10}));
		caseList.add(new CSDTestCase("m7th", new int[]{1, 11}));
		caseList.add(new CSDTestCase("M7th", new int[]{1, 12}));
		caseList.add(new CSDTestCase("octave", new int[]{1, 13}));
		caseList.add(new CSDTestCase("#4", new int[]{1, 7}));
		return caseList;
	}
	private ArrayList<CSDTestCase> makeList() {
		ArrayList<CSDTestCase> caseList = new ArrayList<CSDTestCase>();
		caseList.add(new CSDTestCase("m2", new int[]{1, 2}));
		caseList.add(new CSDTestCase("M2", new int[]{1, 3}));
		caseList.add(new CSDTestCase("m3", new int[]{1, 4}));
		caseList.add(new CSDTestCase("M3", new int[]{1, 5}));
		caseList.add(new CSDTestCase("p4", new int[]{1, 6}));
		caseList.add(new CSDTestCase("p5", new int[]{1, 8}));
		caseList.add(new CSDTestCase("m6th", new int[]{1, 9}));
		caseList.add(new CSDTestCase("M6th", new int[]{1, 10}));
		caseList.add(new CSDTestCase("m7th", new int[]{1, 11}));
		caseList.add(new CSDTestCase("M7th", new int[]{1, 12}));
		caseList.add(new CSDTestCase("octave", new int[]{1, 13}));
		caseList.add(new CSDTestCase("#4", new int[]{1, 7}));
		
		caseList.add(new CSDTestCase("min-root", new int[]{50, 53, 57}));
		caseList.add(new CSDTestCase("min-1st", new int[]{50, 54, 59}));
		caseList.add(new CSDTestCase("min-2nd", new int[]{50, 55, 58}));
		
		caseList.add(new CSDTestCase("maj-root", new int[]{50, 54, 57}));
		caseList.add(new CSDTestCase("maj-1st", new int[]{50, 53, 58}));
		caseList.add(new CSDTestCase("maj-2nd", new int[]{50, 55, 59}));
		
		caseList.add(new CSDTestCase("dim-root", new int[]{50, 53, 56}));
		caseList.add(new CSDTestCase("dim-1st", new int[]{50, 53, 59}));
		caseList.add(new CSDTestCase("dim-2nd", new int[]{50, 56, 59}));
		
		caseList.add(new CSDTestCase("aug-root", new int[]{50, 54, 58}));
		
		caseList.add(new CSDTestCase("sus4-root", new int[]{50, 55, 57}));
		caseList.add(new CSDTestCase("sus4-1st", new int[]{50, 52, 57}));
		caseList.add(new CSDTestCase("sus4-2nd", new int[]{50, 55, 60}));
		
		caseList.add(new CSDTestCase("min7-root", new int[]{50, 53, 57, 60}));
		caseList.add(new CSDTestCase("min7-1st", new int[]{50, 54, 57, 59}));
		caseList.add(new CSDTestCase("min7-2nd", new int[]{50, 53, 55, 58}));
		caseList.add(new CSDTestCase("min7-3rd", new int[]{50, 52, 55, 59}));
		
		caseList.add(new CSDTestCase("M7-root", new int[]{50, 54, 57, 61}));
		caseList.add(new CSDTestCase("M7-1st", new int[]{50, 53, 57, 58}));
		caseList.add(new CSDTestCase("M7-2nd", new int[]{50, 54, 55, 59}));
		caseList.add(new CSDTestCase("M7-3rd", new int[]{50, 51, 55, 58}));
		
		caseList.add(new CSDTestCase("M7#5-root", new int[]{50, 54, 58, 61}));
		caseList.add(new CSDTestCase("M7#5-1st", new int[]{50, 54, 57, 58}));
		caseList.add(new CSDTestCase("M7#5-2nd", new int[]{50, 53, 54, 58}));
		caseList.add(new CSDTestCase("M7#5-3rd", new int[]{50, 51, 55, 59}));
		
		caseList.add(new CSDTestCase("M7b5-root", new int[]{50, 54, 56, 61}));
		caseList.add(new CSDTestCase("M7b5-1st", new int[]{50, 52, 57, 58}));
		caseList.add(new CSDTestCase("M7b5-2nd", new int[]{50, 55, 56, 60}));
		caseList.add(new CSDTestCase("M7b5-3rd", new int[]{50, 51, 55, 57}));
		
		caseList.add(new CSDTestCase("7-root", new int[]{50, 54, 57, 60}));
		caseList.add(new CSDTestCase("7-1st", new int[]{50, 53, 56, 58}));
		caseList.add(new CSDTestCase("7-2nd", new int[]{50, 53, 55, 59}));
		caseList.add(new CSDTestCase("7-3rd", new int[]{50, 52, 56, 59}));
		
		caseList.add(new CSDTestCase("7#5-root", new int[]{50, 54, 58, 60}));
		caseList.add(new CSDTestCase("7#5-1st", new int[]{50, 54, 56, 58}));
		caseList.add(new CSDTestCase("7#5-2nd", new int[]{50, 52, 54, 58}));
		caseList.add(new CSDTestCase("7#5-3rd", new int[]{50, 52, 56, 60}));
		
		caseList.add(new CSDTestCase("7b5-root", new int[]{50, 54, 56, 60}));
		caseList.add(new CSDTestCase("7b5-1st", new int[]{50, 52, 56, 58}));
		caseList.add(new CSDTestCase("7b5-2nd", new int[]{50, 54, 56, 60}));
		caseList.add(new CSDTestCase("7b5-3rd", new int[]{50, 52, 56, 58}));
		
		caseList.add(new CSDTestCase("7sus4-root", new int[]{50, 55, 57, 60}));
		caseList.add(new CSDTestCase("7sus4-1st", new int[]{50, 52, 55, 57}));
		caseList.add(new CSDTestCase("7sus4-2nd", new int[]{50, 53, 55, 60}));
		caseList.add(new CSDTestCase("7sus4-3rd", new int[]{50, 52, 57, 59}));
		
		caseList.add(new CSDTestCase("m7b5-root", new int[]{50, 53, 56, 60}));
		caseList.add(new CSDTestCase("m7b5-1st", new int[]{50, 53, 57, 59}));
		caseList.add(new CSDTestCase("m7b5-2nd", new int[]{50, 54, 56, 59}));
		caseList.add(new CSDTestCase("m7b5-3rd", new int[]{50, 52, 55, 58}));
		
		caseList.add(new CSDTestCase("dim7", new int[]{50, 53, 56, 59}));
		
		caseList.add(new CSDTestCase("mMaj7-root", new int[]{50, 53, 57, 61}));
		caseList.add(new CSDTestCase("mMaj7-1st", new int[]{50, 54, 58, 59}));
		caseList.add(new CSDTestCase("mMaj7-2nd", new int[]{50, 54, 55, 58}));
		caseList.add(new CSDTestCase("mMaj7-3rd", new int[]{50, 51, 54, 58}));
		
		caseList.add(new CSDTestCase("Maj7no3-root", new int[]{50, 57, 61}));
		caseList.add(new CSDTestCase("Maj7no3-2st", new int[]{50, 54, 55}));
		caseList.add(new CSDTestCase("Maj7no3-3nd", new int[]{50, 51, 55}));
		
		
		//caseList.add(new CSDTestCase("mMaj7-3rd", new int[]{50, 51, 54, 58}));
		

		return caseList;
		
	}
	private ArrayList<CSDTestCase> makeQuartadList() {
		ArrayList<CSDTestCase> caseList = new ArrayList<CSDTestCase>();
//		caseList.add(new CSDTestCase("min7-root", new int[]{50, 53, 57, 60}));
//		caseList.add(new CSDTestCase("min7-1st", new int[]{50, 54, 57, 59}));
//		caseList.add(new CSDTestCase("min7-2nd", new int[]{50, 53, 55, 58}));
//		caseList.add(new CSDTestCase("min7-3rd", new int[]{50, 52, 55, 59}));
		
//		caseList.add(new CSDTestCase("m7b5-root", new int[]{50, 53, 56, 60}));
//		caseList.add(new CSDTestCase("m7b5-1st", new int[]{50, 53, 57, 59}));
//		caseList.add(new CSDTestCase("m7b5-2nd", new int[]{50, 54, 56, 59}));
//		caseList.add(new CSDTestCase("m7b5-3rd", new int[]{50, 52, 55, 58}));
		
//		caseList.add(new CSDTestCase("M7-root", new int[]{50, 54, 57, 61}));
//		caseList.add(new CSDTestCase("M7-1st", new int[]{50, 53, 57, 58}));
//		caseList.add(new CSDTestCase("M7-2nd", new int[]{50, 54, 55, 59}));
//		caseList.add(new CSDTestCase("M7-3rd", new int[]{50, 51, 55, 58}));
		
//		caseList.add(new CSDTestCase("M7#5-root", new int[]{50, 54, 58, 61}));
//		caseList.add(new CSDTestCase("M7#5-1st", new int[]{50, 54, 57, 58}));
//		caseList.add(new CSDTestCase("M7#5-2nd", new int[]{50, 53, 54, 58}));
//		caseList.add(new CSDTestCase("M7#5-3rd", new int[]{50, 51, 55, 59}));
//		
//		caseList.add(new CSDTestCase("M7b5-root", new int[]{50, 54, 56, 61}));
//		caseList.add(new CSDTestCase("M7b5-1st", new int[]{50, 52, 57, 58}));
//		caseList.add(new CSDTestCase("M7b5-2nd", new int[]{50, 55, 56, 60}));
//		caseList.add(new CSDTestCase("M7b5-3rd", new int[]{50, 51, 55, 57}));
		
//		caseList.add(new CSDTestCase("7-root", new int[]{50, 54, 57, 60}));
//		caseList.add(new CSDTestCase("7-1st", new int[]{50, 53, 56, 58}));
//		caseList.add(new CSDTestCase("7-2nd", new int[]{50, 53, 55, 59}));
//		caseList.add(new CSDTestCase("7-3rd", new int[]{50, 52, 56, 59}));
		
		caseList.add(new CSDTestCase("7#5-root", new int[]{50, 54, 58, 60}));
		caseList.add(new CSDTestCase("7#5-1st", new int[]{50, 54, 56, 58}));
		caseList.add(new CSDTestCase("7#5-2nd", new int[]{50, 52, 54, 58}));
		caseList.add(new CSDTestCase("7#5-3rd", new int[]{50, 52, 56, 60}));
				
//		caseList.add(new CSDTestCase("7sus4-root", new int[]{50, 55, 57, 60}));
//		caseList.add(new CSDTestCase("7sus4-1st", new int[]{50, 52, 55, 57}));
//		caseList.add(new CSDTestCase("7sus4-2nd", new int[]{50, 53, 55, 60}));
//		caseList.add(new CSDTestCase("7sus4-3rd", new int[]{50, 52, 57, 59}));
		
//		caseList.add(new CSDTestCase("mMaj7-root", new int[]{50, 53, 57, 61}));
//		caseList.add(new CSDTestCase("mMaj7-1st", new int[]{50, 54, 58, 59}));
//		caseList.add(new CSDTestCase("mMaj7-2nd", new int[]{50, 54, 55, 58}));
//		caseList.add(new CSDTestCase("mMaj7-3rd", new int[]{50, 51, 54, 58}));
		
//		caseList.add(new CSDTestCase("7b5-root", new int[]{50, 54, 56, 60}));
//		caseList.add(new CSDTestCase("7b5-1st", new int[]{50, 52, 56, 58}));
//		caseList.add(new CSDTestCase("7b5-2nd", new int[]{50, 54, 56, 60}));
//		caseList.add(new CSDTestCase("7b5-3rd", new int[]{50, 52, 56, 58}));

//		caseList.add(new CSDTestCase("dim7", new int[]{50, 53, 56, 59}));
		return caseList;
	}

	private ArrayList<CSDTestCase> make9thChordList() {
		ArrayList<CSDTestCase> caseList = new ArrayList<CSDTestCase>();
//		caseList.add(new CSDTestCase("min9-root", new int[]{50, 53, 57, 60, 64}));
//		caseList.add(new CSDTestCase("min9-1st", new int[]{50, 54, 57, 59, 61}));
//		caseList.add(new CSDTestCase("min9-2nd", new int[]{50, 53, 55, 57, 58}));
//		caseList.add(new CSDTestCase("min9-3rd", new int[]{50, 52, 54, 55, 59}));
//		caseList.add(new CSDTestCase("min9-4th", new int[]{50, 51, 55, 58, 60}));
		
//		caseList.add(new CSDTestCase("9-root", new int[]{50, 54, 57, 60, 64}));
//		caseList.add(new CSDTestCase("9-1st", new int[]{50, 53, 56, 58, 60}));
//		caseList.add(new CSDTestCase("9-2nd", new int[]{50, 53, 55, 57, 59}));
//		caseList.add(new CSDTestCase("9-3rd", new int[]{50, 52, 54, 56, 59}));
//		caseList.add(new CSDTestCase("9-4th", new int[]{50, 52, 55, 58, 60}));
		
//		caseList.add(new CSDTestCase("#9-root", new int[]{50, 54, 57, 60, 65}));
//		caseList.add(new CSDTestCase("#9-1st", new int[]{50, 53, 56, 58, 61}));
//		caseList.add(new CSDTestCase("#9-2nd", new int[]{50, 53, 55, 58, 59}));
//		caseList.add(new CSDTestCase("#9-3rd", new int[]{50, 52, 55, 56, 59}));
//		caseList.add(new CSDTestCase("#9-4th", new int[]{51, 52, 55, 58, 60}));
		
//		caseList.add(new CSDTestCase("b9-root", new int[]{50, 54, 57, 60, 63}));
//		caseList.add(new CSDTestCase("b9-1st", new int[]{50, 53, 56, 58, 59}));
//		caseList.add(new CSDTestCase("b9-2nd", new int[]{50, 53, 55, 56, 59}));
//		caseList.add(new CSDTestCase("b9-3rd", new int[]{50, 52, 53, 56, 59}));
//		caseList.add(new CSDTestCase("b9-4th", new int[]{49, 52, 55, 58, 60}));
		
//		caseList.add(new CSDTestCase("7#5#9-root", new int[]{50, 54, 58, 60, 65}));
//		caseList.add(new CSDTestCase("7#5#9-1st", new int[]{50, 54, 56, 58, 61}));
//		caseList.add(new CSDTestCase("7#5#9-2nd", new int[]{51, 53, 55, 58, 59}));
//		caseList.add(new CSDTestCase("7#5#9-3rd", new int[]{50, 52, 55, 56, 60}));
//		caseList.add(new CSDTestCase("7#5#9-4th", new int[]{51, 52, 56, 58, 60}));
		
//		caseList.add(new CSDTestCase("9#5-root", new int[]{50, 54, 58, 60, 64}));
//		caseList.add(new CSDTestCase("9#5-1st", new int[]{50, 54, 56, 58, 60}));
//		caseList.add(new CSDTestCase("9#5-2nd", new int[]{51, 53, 55, 57, 59}));
//		caseList.add(new CSDTestCase("9#5-3rd", new int[]{50, 52, 54, 56, 60}));
//		caseList.add(new CSDTestCase("9#5-4th", new int[]{50, 52, 56, 58, 60}));
		
//		caseList.add(new CSDTestCase("M9-root", new int[]{50, 54, 57, 61, 64}));
//		caseList.add(new CSDTestCase("M9-1st", new int[]{50, 53, 57, 58, 60}));
//		caseList.add(new CSDTestCase("M9-2nd", new int[]{50, 54, 55, 57, 59}));
//		caseList.add(new CSDTestCase("M9-3rd", new int[]{51, 52, 54, 56, 59}));
//		caseList.add(new CSDTestCase("M9-4th", new int[]{50, 52, 55, 59, 60}));
		
//		caseList.add(new CSDTestCase("M69-root", new int[]{50, 54, 57, 59, 64}));
//		caseList.add(new CSDTestCase("M69-1st", new int[]{50, 53, 55, 58, 60}));
//		caseList.add(new CSDTestCase("M69-2nd", new int[]{50, 52, 55, 57, 59}));
//		caseList.add(new CSDTestCase("M69-3rd", new int[]{49, 52, 54, 56, 59}));
//		caseList.add(new CSDTestCase("M69-4th", new int[]{50, 52, 55, 57, 60}));
		
//		caseList.add(new CSDTestCase("m69-root", new int[]{50, 53, 57, 59, 64}));
//		caseList.add(new CSDTestCase("m69-1st", new int[]{49, 53, 55, 58, 60}));
//		caseList.add(new CSDTestCase("m69-2nd", new int[]{50, 52, 55, 57, 58}));
//		caseList.add(new CSDTestCase("m69-3rd", new int[]{49, 52, 54, 55, 59}));
//		caseList.add(new CSDTestCase("m69-4th", new int[]{50, 51, 55, 57, 60}));
		
//		caseList.add(new CSDTestCase("9sus4-root", new int[]{50, 55, 57, 60, 64}));
//		caseList.add(new CSDTestCase("9sus4-1st", new int[]{51, 53, 56, 58, 60}));
//		caseList.add(new CSDTestCase("9sus4-2nd", new int[]{50, 53, 55, 57, 60}));
//		caseList.add(new CSDTestCase("9sus4-3rd", new int[]{50, 52, 54, 57, 59}));
//		caseList.add(new CSDTestCase("9sus4-4th", new int[]{50, 53, 55, 58, 60}));
		
//		caseList.add(new CSDTestCase("Maj9#5-root", new int[]{50, 54, 58, 61, 64}));
//		caseList.add(new CSDTestCase("Maj9#5-1st", new int[]{50, 54, 57, 58, 60}));
//		caseList.add(new CSDTestCase("Maj9#5-2nd", new int[]{51, 54, 55, 57, 59}));
//		caseList.add(new CSDTestCase("Maj9#5-3rd", new int[]{51, 52, 54, 56, 60}));
//		caseList.add(new CSDTestCase("Maj9#5-4th", new int[]{50, 52, 56, 59, 60}));
		
		caseList.add(new CSDTestCase("m9M7-root", new int[]{50, 53, 57, 61, 64}));
		caseList.add(new CSDTestCase("m9M7-1st", new int[]{50, 54, 58, 59, 61}));
		caseList.add(new CSDTestCase("m9M7-2nd", new int[]{50, 54, 55, 57, 58}));
		caseList.add(new CSDTestCase("m9M7-3rd", new int[]{51, 52, 54, 55, 59}));
		caseList.add(new CSDTestCase("m9M7-4th", new int[]{50, 51, 55, 59, 60}));
		return caseList;
	}
	
	
	public static void main(String[] args)
	{
		StaticChordScaleDictionaryConsoleTest x = new StaticChordScaleDictionaryConsoleTest();
	}
}
