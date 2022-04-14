package test.java.da_utils.chord_progression_analyzer.chord_progression_corpus_tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class Blue3BossaChordAnalysisTest {

	

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/Blue3Bossa.chords.liveclip";
		String correctResult = "d:Dm7(im7) d:Gm7(ivm7) d:Em7b5(iim7b5) d:A7(V7) d:Dm7(im7) Eb:Fm7(iim7) Eb:Bb7(V7) Eb:EbMaj7(IMaj7) Gb:Abm7(iim7) Gb:Db7(V7) Gb:GbMaj7(IMaj7) d:Em7b5(iim7b5) d:A7(V7) d:Dm7(im7) d:Em7b5(iim7b5) d:A7(V7) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
