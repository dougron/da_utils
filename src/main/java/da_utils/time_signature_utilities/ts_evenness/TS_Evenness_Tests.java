package main.java.da_utils.time_signature_utilities.ts_evenness;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

class TS_Evenness_Tests
{

	@Test
	void testName() throws Exception
	{
		Iterator<TimeSignature> tsit = TimeSignature.getPresetTimeSignatureIterator();
		while (tsit.hasNext())
		{
			TimeSignature ts = tsit.next();
			System.out.println(ts.getName() + "\t" + TS_Evenness.getTactusEvennessScore(ts) + "\t" + TS_Evenness.getSuperTactusEvennessScore(ts));
		}
	}

}
