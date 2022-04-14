package test.java.da_utils.chord_progression_analyzer.chord_progression_corpus_tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class FourChordsChordAnalysisTest {

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/4chords.chords.liveclip";
		String correctResult = "A:AMaj(IMaj) A:EMaj(VMaj) A:F#min(vimin) A:DMaj(IVMaj) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
