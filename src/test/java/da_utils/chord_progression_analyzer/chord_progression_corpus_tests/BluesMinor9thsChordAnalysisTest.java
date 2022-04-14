package test.java.da_utils.chord_progression_analyzer.chord_progression_corpus_tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class BluesMinor9thsChordAnalysisTest {

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/BluesMinor9ths.chords.liveclip";
		String correctResult = "g:Gm9(im9) g:Gm9(im9) g:Gm9(im9) g:Gm9(im9) g:Cm9(ivm9) g:Cm9(ivm9) g:Gm9(im9) g:Gm9(im9) g:D#9(bII9/V) g:D7b9(V7b9) g:Gm9(im9) g:Am7b5(iim7b5) g:D7#9(V7#9) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
