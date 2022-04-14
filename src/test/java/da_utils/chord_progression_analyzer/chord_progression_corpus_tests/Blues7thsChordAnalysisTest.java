package test.java.da_utils.chord_progression_analyzer.chord_progression_corpus_tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class Blues7thsChordAnalysisTest {

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/Blues7ths.chords.liveclip";
		String correctResult = "F:F7(I7) F:F7(I7) F:F7(I7) F:F7(I7) F:Bb7(IV7) F:Bb7(IV7) F:F7(I7) F:F7(I7) F:C7(V7) F:Bb7(IV7) F:F7(I7) F:C7(V7) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
