package test.java.da_utils.chord_progression_analyzer.chord_progression_corpus_tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class GuitarGentlyWeepsChordAnalysisTest {

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/GuitarGentlyWeeps.chords.liveclip";
		String correctResult = "a:Amin(imin) a:GMaj(bVIIMaj) a:DMaj(IVMaj) a:FMaj(bVIMaj) a:EMaj(VMaj) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
