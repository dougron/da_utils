package main.java.da_utils.time_signature_utilities.time_signature_map_generator;

import java.util.ArrayList;

import main.java.da_utils.time_signature_utilities.time_signature_map.TSMapInterface;

/*
 * interface for TimeSignatureGenerator items. Implemented in TimeSignature, assumed to be 1 bar of a timesignature repeated once,
 * and TSGenItem which is a TSGenInterface repeated a certain number of times or ad infinitum
 */

public interface TSGenInterface {
	
	public TSMapInterface getTimeSignatureMap(int barCount);

//	public ArrayList<TimeSignature> getTSList();
	
	

}
