package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class JoySpringChordAnalysisTest {

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/JoySpring.chords.liveclip";
		String correctResult = "G:GMaj7(IMaj7) G:Am7(iim7) G:D7(V7) G:GMaj7(IMaj7) Bb:Cm7(iim7) Bb:F7(V7) A:Bm7(iim7) A:A#7(bII7) G:Am7(iim7) G:D7(V7) G:GMaj7(IMaj7) Ab:Bbm7(iim7) Ab:Eb7(V7) Ab:AbMaj7(IMaj7) Ab:Bbm7(iim7) Ab:Eb7(V7) Ab:AbMaj7(IMaj7) B:C#m7(iim7) B:F#7(V7) Bb:Cm7(iim7) Bb:B7(bII7) Ab:Bbm7(iim7) Ab:Eb7(V7) Ab:AbMaj7(IMaj7) A:Bm7(iim7) A:E7(V7) A:AMaj7(IMaj7) G:Am7(iim7) G:D7(V7) G:GMaj7(IMaj7) F:Gm7(iim7) F:C7(V7) F:FMaj7(IMaj7) Ab:Bbm7(iim7) Ab:Eb7(V7) Ab:AbMaj7(IMaj7) G:Am7(iim7) G:D7(V7) G:GMaj7(IMaj7) G:Am7(iim7) G:D7(V7) G:GMaj7(IMaj7) Bb:Cm7(iim7) Bb:F7(V7) A:Bm7(iim7) A:A#7(bII7) G:Am7(iim7) G:D7(V7) G:GMaj7(IMaj7) G:Am7(iim7) G:D7(V7) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}


}
