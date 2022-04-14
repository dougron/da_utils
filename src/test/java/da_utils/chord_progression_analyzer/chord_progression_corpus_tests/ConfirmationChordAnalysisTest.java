package test.java.da_utils.chord_progression_analyzer.chord_progression_corpus_tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class ConfirmationChordAnalysisTest {

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/Confirmation.chords.liveclip";
		String correctResult = "F:FMaj7(IMaj7) d:Em7b5(iim7b5) d:A7(V7) C:Dm7(iim7) C:G7(V7) Bb:Cm7(iim7) Bb:F7(V7) F:Bb7(IV7) g:Am7b5(iim7b5) g:D7(V7) F:G7(V7/V) F:Gm7(iim7) F:C7sus4(X) F:FMaj7(IMaj7) d:Em7b5(iim7b5) d:A7(V7) C:Dm7(iim7) C:G7(V7) Bb:Cm7(iim7) Bb:F7(V7) F:Bb7(IV7) g:Am7b5(iim7b5) g:D7(V7) g:Gm7(im7) F:C7sus4(X) F:FMaj7(IMaj7) Bb:Cm7(iim7) Bb:F7(V7) Bb:BbMaj7(IMaj7) Db:Ebm7(iim7) Db:Ab7(V7) Db:DbMaj7(IMaj7) F:Gm7(iim7) F:C7sus4(X) F:FMaj7(IMaj7) d:Em7b5(iim7b5) d:A7(V7) C:Dm7(iim7) C:G7(V7) Bb:Cm7(iim7) Bb:F7(V7) F:Bb7(IV7) g:Am7b5(iim7b5) g:D7(V7) g:Gm7(im7) F:C7sus4(X) F:FMaj7(IMaj7) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
