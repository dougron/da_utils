package test.java.da_utils.chord_progression_analyzer.chord_progression_corpus_tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class TickleToeChordAnalysisTest {



	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/TickleToe.chords.liveclip";
		String correctResult = "c:Cm7(im7) c:G7(V7) c:Cm7(im7) c:G7(V7) c:Cm7(im7) f:C7(V7) f:Fmin(imin) f:C7(V7) f:Fmin(imin) f:C7(V7) f:Fmin(imin) Ab:Bbm7(iim7) Ab:Eb7(V7) Eb:Ab7(IV7) Eb:Ddim7(viidim7) Eb:EbMaj7(IMaj7) F:Gm7(iim7) F:C7(V7) Eb:F7(V7/V) Bb:Cm7(iim7) Bb:F7(V7) Eb:Bb7(V7) c:G7(V7) c:Cmin(imin) c:G7(V7) c:Cmin(imin) c:G7(V7) c:Cmin(imin) f:C7(V7) f:Fmin(imin) f:C7(V7) f:Fmin(imin) f:C7(V7) f:Fmin(imin) Ab:Bbm7(iim7) Ab:Eb7(V7) Eb:Ab7(IV7) Eb:Ebdim7(#ivdim7) Eb:EbMaj7(IMaj7) Eb:C7(V7/ii) Eb:F7(V7/V) Eb:Bb7(V7) Eb:EbMaj7(IMaj7) c:Dm7b5(iim7b5) c:G7(V7) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
