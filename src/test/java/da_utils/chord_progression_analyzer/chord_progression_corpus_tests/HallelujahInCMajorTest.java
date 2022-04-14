package test.java.da_utils.chord_progression_analyzer.chord_progression_corpus_tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class HallelujahInCMajorTest {

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/Hallelujah.chords.liveclip";
		String correctResult = "C:CMaj(IMaj) C:Amin(vimin) C:CMaj(IMaj) C:Amin(vimin) C:FMaj(IVMaj) C:GMaj(VMaj) C:CMaj(IMaj) C:GMaj(VMaj) C:CMaj(IMaj) C:FMaj(IVMaj) C:GMaj(VMaj) C:Amin(vimin) C:FMaj(IVMaj) C:Dmin(iimin) C:E7(V7/vi) C:Amin(vimin) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
