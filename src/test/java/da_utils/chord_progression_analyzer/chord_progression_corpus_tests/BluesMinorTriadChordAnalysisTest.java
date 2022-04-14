package test.java.da_utils.chord_progression_analyzer.chord_progression_corpus_tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class BluesMinorTriadChordAnalysisTest {

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/BluesMinorTriads.chords.liveclip";
		String correctResult = "a:Amin(imin) a:Amin(imin) a:Amin(imin) a:Amin(imin) a:Dmin(ivmin) a:Dmin(ivmin) a:Amin(imin) a:Amin(imin) a:FMaj(bVIMaj) a:EMaj(VMaj) a:Amin(imin) a:EMaj(VMaj) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
