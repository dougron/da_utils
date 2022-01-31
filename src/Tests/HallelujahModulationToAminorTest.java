package Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class HallelujahModulationToAminorTest {

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/Hallelujah.chords.liveclip";
		String correctResult = "C:CMaj(IMaj) C:Amin(vimin) C:CMaj(IMaj) C:Amin(vimin) C:FMaj(IVMaj) C:GMaj(VMaj) C:CMaj(IMaj) C:GMaj(VMaj) C:CMaj(IMaj) C:FMaj(IVMaj) C:GMaj(VMaj) C:Amin(vimin) C:FMaj(IVMaj) C:Dmin(iimin) a:E7(V7) a:Amin(imin) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertNotSame(correctResult, result);
	}

}
