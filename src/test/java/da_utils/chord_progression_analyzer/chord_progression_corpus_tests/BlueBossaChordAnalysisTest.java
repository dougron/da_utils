package test.java.da_utils.chord_progression_analyzer.chord_progression_corpus_tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class BlueBossaChordAnalysisTest {

	

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/BlueBossa.chords.liveclip";
		String correctResult = "c:Cm7(im7) c:Fm7(ivm7) c:Dm7b5(iim7b5) c:G7(V7) c:Cm7(im7) Db:Ebm7(iim7) Db:Ab7(V7) Db:DbMaj7(IMaj7) c:Dm7b5(iim7b5) c:G7(V7) c:Cm7(im7) c:Dm7b5(iim7b5) c:G7(V7) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
