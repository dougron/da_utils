package test.java.da_utils.chord_progression_analyzer.chord_progression_corpus_tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class DescendingFlamencoVariationChordAnalysisTest {

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/DescendingFlamencoVariation.chords.liveclip";
		String correctResult = "F:Dmin(vimin) F:CMaj(VMaj) F:BbMaj(IVMaj) F:CMaj(VMaj) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}
}
