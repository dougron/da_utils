package test.java.da_utils.chord_progression_analyzer.chord_progression_corpus_tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class HouseOfRisingSunChordAnalysisTest {

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/HouseOfRisingSun.chords.liveclip";
		String correctResult = "e:Emin(imin) e:GMaj(bIIIMaj) e:Amin(ivmin) e:CMaj(bVIMaj) e:Emin(imin) e:GMaj(bIIIMaj) e:B7(V7) e:B7(V7) e:Emin(imin) e:GMaj(bIIIMaj) e:Amin(ivmin) e:CMaj(bVIMaj) e:Emin(imin) e:B7(V7) e:Emin(imin) e:B7(V7) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
