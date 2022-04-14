package test.java.da_utils.chord_progression_analyzer.chord_progression_corpus_tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class BluesTriadsChordAnalysisTest {


	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/BluesTriads.chords.liveclip";
		String correctResult = "G:GMaj(IMaj) G:GMaj(IMaj) G:GMaj(IMaj) G:GMaj(IMaj) G:CMaj(IVMaj) G:CMaj(IVMaj) G:GMaj(IMaj) G:GMaj(IMaj) G:DMaj(VMaj) G:CMaj(IVMaj) G:GMaj(IMaj) G:DMaj(VMaj) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
