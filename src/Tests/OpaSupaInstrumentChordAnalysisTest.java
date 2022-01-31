package Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;

public class OpaSupaInstrumentChordAnalysisTest {

	

	@Test
	public void test() {
		String path = "D:/Documents/repos/ChordProgressionTestFiles/OpaSupaInstrumental.chords.liveclip";
		String correctResult = "d:Dmin(imin) d:Amin(vmin) d:Dmin(imin) d:EMaj(IIMaj) d:AMaj(VMaj) d:Dmin(imin) d:Amin(vmin) d:Dmin(imin) d:EMaj(IIMaj) d:AMaj(VMaj) d:Dmin(imin) d:Gmin(ivmin) d:Dmin(imin) d:A#Maj(bVIMaj) d:EMaj(IIMaj) d:AMaj(VMaj) d:Dmin(imin) d:Gmin(ivmin) d:Dmin(imin) d:A#Maj(bVIMaj) d:EMaj(IIMaj) d:AMaj(VMaj) ";
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);
		String result = pa.analysisToString();
		assertEquals(correctResult, result);
	}

}
