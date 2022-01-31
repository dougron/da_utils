package main.java.da_utils.time_signature_utilities.time_signature_map;

import java.util.Arrays;

import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;
import main.java.da_utils.time_signature_utilities.time_signature_map_generator.TSMGenerator;

public class TimeSignatureMapTest {
	
	static TimeSignature fourFour;
	static TimeSignature sevenEight;
	static TimeSignature fiveFour;

	public static void main(String[] nargs) {
		makeTimeSignatures();
		
//		TimeSignatureMap tsm = new TimeSignatureMap();
//		System.out.println(tsm.toString());
//		
//		tsm.addTimeSignature(fourFour);
//		System.out.println(tsm.toString());
//		
//		tsm.addTimeSignature(fourFour);
//		System.out.println(tsm.toString());
//		
//		tsm.addTimeSignature(sevenEight);
//		System.out.println(tsm.toString());
//		
//		tsm.addTimeSignatures(Arrays.asList(new TimeSignature[] {fiveFour, fiveFour, fourFour, fourFour}));
//		System.out.println(tsm.toString());
		
		// making 2 x 8 bars tsms and adding them together
		//1st one
		TSMGenerator tsg = new TSMGenerator();
		tsg.addGenItem(new TSMGenerator(new TimeSignature[] {fourFour, fourFour, fourFour, sevenEight}, 1));
		TSMapInterface tsm = tsg.getTimeSignatureMap(8);
		System.out.println(tsm.toString());
//		System.out.println(tsm.getClass());
		
		// 2nd one
		TSMGenerator tsg1 = new TSMGenerator();
		tsg1.addGenItem(new TSMGenerator(new TimeSignature[] {fourFour, fiveFour, sevenEight}, 1));
		TSMapInterface tsm1 = tsg1.getTimeSignatureMap(8);
		System.out.println(tsm1.toString());
		
		// add them together
		TSMapInterface tsmAdd = new TimeSignatureMap();
		tsmAdd.addTimeSignatures(Arrays.asList(tsm.getTimeSignatures()));
		tsmAdd.addTimeSignatures(Arrays.asList(tsm1.getTimeSignatures()));		
		System.out.println(tsmAdd.toString());
		
		// get excerpt
		TSMapInterface exTsm = tsmAdd.getTimeSignatureMap(14, 6);
		System.out.println(exTsm.toString());
	}
	
	private static void makeTimeSignatures() {
		fourFour = new TimeSignature(4, 4, 0.5, new Object[]
				{
						1,
						1.0, 1.0,
						1,
						1.0, 1.0
				});
		fiveFour = new TimeSignature(5, 4, 0.5, new Object[]
				{
						1, 2,
						1.0, 1.0, 
						2, 
						1.0,
						1, 2,
						1.0, 1.0
				});
		sevenEight = new TimeSignature(7, 8, 0.5, new Object[]
				{
						1,
				});
		
	}
}
