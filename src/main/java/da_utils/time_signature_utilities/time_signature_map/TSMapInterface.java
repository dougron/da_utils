package main.java.da_utils.time_signature_utilities.time_signature_map;

import java.util.List;

import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public interface TSMapInterface
{

	//------------------------
	// INTERFACE
	//------------------------
	/* Code from template attribute_SetMany */
	boolean addTimeSignature(TimeSignature aTimeSignature);

	boolean removeTimeSignature(TimeSignature aTimeSignature);

	/* Code from template attribute_GetMany */
	TimeSignature getTimeSignature(int index);

	TimeSignature[] getTimeSignatures();

	int numberOfTimeSignatures();

//	boolean hasTimeSignatures();

//	int indexOfTimeSignature(TimeSignature aTimeSignature);

	/* Code from template attribute_GetMany */
	TimeSignatureZone getTimeSignatureZone(int index);

	TimeSignatureZone[] getTimeSignatureZones();

	int numberOfTimeSignatureZones();

//	boolean hasTimeSignatureZones();

//	int indexOfTimeSignatureZone(TimeSignatureZone aTimeSignatureZone);

	/* Code from template attribute_GetMany */
	Double getBarStartInQuarter(int index);

	Double[] getBarStartInQuarters();

//	int numberOfBarStartInQuarters();

//	boolean hasBarStartInQuarters();

//	int indexOfBarStartInQuarter(Double aBarStartInQuarter);

	void delete();

	// line 27 "../TimeSignatureMap.ump"
	double getLengthInQuarters();

	// line 32 "../TimeSignatureMap.ump"
	String toString();

	// line 45 "../TimeSignatureMap.ump"
	boolean addTimeSignatures(List<TimeSignature> aTimeSignatures);

	// line 54 "../TimeSignatureMap.ump"
	boolean addTimeSignature(TimeSignature aTimeSignature, int index);

	// line 68 "../TimeSignatureMap.ump"
	void makeTimeSignatureZones();

	/**
	   * gets excerpt from TimeSignatureMap. will modulo to start to within the length of TimeSignatures and wrap when loading
	   */
	// line 96 "../TimeSignatureMap.ump"
	TSMapInterface getTimeSignatureMap(int start, int length);

}