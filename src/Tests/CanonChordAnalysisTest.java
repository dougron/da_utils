package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class CanonChordAnalysisTest {

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/Canon.chords.liveclip";
		String correctResult = "G:GMaj(IMaj) G:DMaj(VMaj) G:Emin(vimin) G:Bmin(iiimin) G:CMaj(IVMaj) G:GMaj(IMaj) G:CMaj(IVMaj) G:DMaj(VMaj) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
