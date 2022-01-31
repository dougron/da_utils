package Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class StellaChordAnalysisTest {

	

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/Stella.chords.liveclip";
		String correctResult = "e:F#m7b5(iim7b5) e:B7(V7) C:Dm7(iim7) C:G7(V7) F:Gm7(iim7) F:C7(V7) F:FMaj7(IMaj7) C:Bb7(bVII7) C:CMaj7(IMaj7) e:F#m7b5(iim7b5) e:B7(V7) e:Em7(im7) G:Cm7(ivm7) G:F7(bVII7) G:GMaj7(IMaj7) e:F#m7b5(iim7b5) e:B7(V7) a:Bm7b5(iim7b5) a:E7(V7) d:A7#5(V7#5) d:Dm7(im7) C:Bb7(bVII7) C:CMaj7(IMaj7) e:F#m7b5(iim7b5) e:B7(V7) d:Em7b5(iim7b5) d:A7(V7) C:Dm7b5(iim7b5) C:G7(V7) C:CMaj7(IMaj7) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
