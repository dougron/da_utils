package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class BluesMinor7thsChordAnalysisTest {

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/BluesMinor7ths.chords.liveclip";
		String correctResult = "g:Gm7(im7) g:Gm7(im7) g:Gm7(im7) g:Gm7(im7) g:Cm7(ivm7) g:Cm7(ivm7) g:Gm7(im7) g:Gm7(im7) g:D#7(bII7/V) g:D7(V7) g:Gm7(im7) g:Am7b5(iim7b5) g:D7(V7) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
