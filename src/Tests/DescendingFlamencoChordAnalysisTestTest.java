package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class DescendingFlamencoChordAnalysisTestTest {

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/DescendingFlamenco.chords.liveclip";
		String correctResult = "c:Cmin(imin) c:BbMaj(bVIIMaj) c:G#Maj(bVIMaj) c:GMaj(VMaj) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
