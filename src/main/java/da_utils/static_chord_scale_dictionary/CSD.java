package main.java.da_utils.static_chord_scale_dictionary;
/*
 * replacement for the CHordScaleDictionary. Short name due to potential widespread use:)
 * this would remove the convoluted text file reading in the ChordScaleDictionary
 * and also attempt to make the user interaction easier to understand
 */



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import main.java.da_utils.combo_variables.IntAndString;


//import DataObjects.IntAndString;

public class CSD {

	
//	private static ArrayList<NotePattern> npList = new ArrayList<NotePattern>();

	public static List<NotePatternAnalysis> getChordOptions(List<Integer> noteList)
	{
		Integer[] noteSet = makeSetArray(noteList);					// noteList sorted, with unison doubling removed 
		Integer lowestNote = noteSet[0];
		Integer[] intervalList = makeIntervalList(noteSet);			// noteSet where lowest note (index 0) = 0
		Integer[] intervalSet = makeIntervalSet(intervalList);		// intervalList with octave doublings removed
		Integer[][] inversionOptions = getIntervalSetInversionOptions(intervalSet);
		
		ArrayList<NotePatternAnalysis> npaList = new ArrayList<NotePatternAnalysis>();
		for (NotePattern np: npArr ){
			NotePatternAnalysis npa = np.getNotePatternAnalysis(inversionOptions);
			if (npa instanceof NP_Interval){
				npa.setLowestNote(lowestNote);
				npa.setRootNote((intervalSet[npa.rootNoteIndex()] + lowestNote) % 12);
				npa.setIntervals(intervalSet);
			} else if (npa instanceof NP_Note){
				npa.setLowestNote(lowestNote);
			} else if (npa instanceof NP_Triad || npa instanceof NP_9thChord){
				npa.setLowestNote(lowestNote);
				npa.setRootNote((intervalSet[npa.rootNoteIndex()] + lowestNote) % 12);
				npa.setIntervals(np.getRootPositionIntervals());
				npa.setExtendedChordToneIntervals(np.getExtendedChordToneIntervals());
			} 
			if (npa != null){
				npaList.add(npa);
				//System.out.println("   " + npa.name());
			}
		}
		return npaList;
	}
	
	
	
	public static String noteName(int note)
	{
		return noteName(note, 0);	// default key of C.
	}
	
	
	
	public static String noteName(int note, int key)		// key -7 to 7 value for keysignature according to MusicXML lore
	{ 
		note = note % 12;
		switch (note){
		case 0: if (key > 6) return "B#"; else return "C";
		case 1: if (key > -3) return "C#"; else return "Db";
		case 2: return "D";
		case 3: if (key > -1) return "D#"; else return "Eb";
		case 4: if (key < -6) return "Fb"; else return "E";
		case 5: return "F";
		case 6: if (key > -4) return "F#"; else return "Gb";
		case 7: return "G";
		case 8: if (key > -2) return "G#"; else return "Ab";
		case 9: return "A";
		case 10: if (key > 0) return "A#"; else return "Bb";
		case 11: if (key > -5) return "B"; else return "Cb";
				
		}
		return null;
	}
	
	
	
	public static int getNoteIndexFromSymbol(String noteSymbol) 
	{
		switch (noteSymbol) {
		case "Cb":	return 11;
		case "C":	return 0;
		case "C#":	return 1;
		case "Db":	return 1;
		case "D":	return 2;
		case "D#":	return 3;
		case "Eb":	return 3;
		case "E":	return 4;
		case "E#":	return 5;
		case "Fb":	return 4;
		case "F":	return 5;
		case "F#":	return 6;
		case "Gb":	return 6;
		case "G":	return 7;
		case "G#":	return 8;
		case "Ab":	return 8;
		case "A":	return 9;
		case "A#":	return 10;
		case "Bb":	return 10;
		case "B":	return 11;
		case "B#":	return 0;
	
		}
		return -1;
	}
	
	
	
	public static String noteNameWithKeyIndex(int note, int keyIndex)		// keyIndex as in note name of root
	{
		return noteName(note, musicXMLKeyFromKeyIndex(keyIndex));
	}
	
	
	
	public static int musicXMLKeyFromKeyIndex(int keyIndex) 
	{
		return (keyIndex * 7 + 6) % 12 - 6;
	}
	
	
	
	public static int octave(int note)
	{
		return note / 12;
	}
	
	
	
	public static double quantResolution(String rez)
	{
		if (rez.equals("1n")) return 4.0;
		if (rez.equals("2n")) return 2.0;
		if (rez.equals("4n")) return 1.0;
		if (rez.equals("8n")) return 0.5;
		if (rez.equals("16n")) return 0.25;
		if (rez.equals("32n")) return 0.125;
		
		return 0.25;
	}
	
	
	
	public static int qmodelTickValue(String rez) 
	{
		if (rez.equals("1n")) return 1920;
		if (rez.equals("2n")) return 960;
		if (rez.equals("4n")) return 480;
		if (rez.equals("8n")) return 240;
		if (rez.equals("16n")) return 120;
		if (rez.equals("32n")) return 60;
		return 0;
	}
	
	
	
	public static double getBeatLength(int signatureDenominator)
	{
		if (signatureDenominator == 4) return 1.0;
		if (signatureDenominator == 2) return 2.0;
		if (signatureDenominator == 1) return 4.0;
		if (signatureDenominator == 8) return 0.5;
		if (signatureDenominator == 16) return 0.25;
		if (signatureDenominator == 32) return 0.125;
		return 1.0;
	}
	
	
	
	public static double getBarLength(int signatureNumerator, int signatureDenominator) 
	{
		return signatureNumerator * getBeatLength(signatureDenominator);
	};
	

	public static IntAndString getNoteAndAlterationFromSymbol(String aSymbol)
	{
		int alter = 0;
		String note = "";
		if (aSymbol.length() == 1)
		{
			if (arrContains(basicNoteNames, aSymbol))
			{
				note = aSymbol;
			}
		} 
		else if (aSymbol.length() == 2)
		{
			String[] arr = aSymbol.split("");
			if (arr[1].equals("b")) alter = -1;
			if (arr[1].equals("#")) alter = 1;
			note = arr[0];
		}
		else if (aSymbol.length() == 3)
		{
			String[] arr = aSymbol.split("");
			if (arr[1].equals("b") && arr[2].equals("b")) alter = -2;
			if (arr[1].equals("#") && arr[1].equals("#")) alter = 2;
			note = arr[0];
		}
		return new IntAndString(alter, note);
	}
	

// privates ---------------------------------------------------------------------------------------------
	
	
	private static boolean arrContains(String[] arr, String aSymbol)
	{
		for (String str: arr)
		{
			if (str.equals(aSymbol)) return true;
		}
		return false;
	}



	private static Integer[][] getIntervalSetInversionOptions(Integer[] intervalSet) 
	{
		ArrayList<Integer[]> aList = new ArrayList<Integer[]>();
		Integer[] temp = new Integer[intervalSet.length];
		for (int i = 0; i < intervalSet.length; i++)
		{
			temp[i] = intervalSet[i];
		}
		aList.add(temp);
		for (int i = 1; i < intervalSet.length; i++)
		{
			Integer[] nutemp = new Integer[intervalSet.length];
			Integer dec = temp[1];
			nutemp[intervalSet.length - 1] = temp[0] + 12 - dec;
			for (int j = 1; j < intervalSet.length; j++)
			{
				nutemp [j - 1] = temp[j] - dec;
			}
			aList.add(nutemp);
			temp = nutemp;
		}
		return aList.toArray(new Integer[aList.size()][]);
	}

	
	
	private static Integer[] makeIntervalSet(Integer[] intervalList) 
	{
		TreeSet<Integer> set = new TreeSet<Integer>();
		for (Integer i: intervalList)
		{
			set.add(i % 12);
		}
		return set.toArray(new Integer[set.size()]);
	}

	
	
	private static Integer[] makeIntervalList(Integer[] noteSet) 
	{
		Integer[] arr = new Integer[noteSet.length];
		Integer lowestNote = noteSet[0];
		for (int i = 0; i < noteSet.length; i++)
		{
			arr[i] = noteSet[i] - lowestNote;
		}
		return arr;
	}

	
	
	private static Integer[] makeSetArray(List<Integer> noteList) 
	{
		TreeSet<Integer> noteSet = new TreeSet<Integer>();
		for (Integer i: noteList)
		{
			noteSet.add(i);
		}
		return noteSet.toArray(new Integer[noteSet.size()]);
	}
	
	
	
	public static String inversionName(int index)
	{
		if (index > -1 && index < 5)
		{
			return inversionNameArr[index];
		}
		return "no inversion name";
	}
	
	
	
	public static final int IRRELEVANT_INVERSION_INDEX = -1;
	public static final int ROOT_POSITION_INDEX = 0;
	public static final int FIRST_INVERSION_INDEX = 1;
	public static final int SECOND_INVERSION_INDEX = 2;
	public static final int THIRD_INVERSION_INDEX = 3;
	public static final int FOURTH_INVERSION_INDEX = 4;
	
	
	public static final String ROOT_POSITION = "root position";
	public static final String FIRST_INVERSION = "1st inversion";
	public static final String SECOND_INVERSION = "2nd inversion";
	public static final String THIRD_INVERSION = "3rd inversion";
	public static final String FOURTH_INVERSION = "4th inversion";
	
	public static final String[] inversionNameArr = new String[]{ROOT_POSITION, FIRST_INVERSION, SECOND_INVERSION, THIRD_INVERSION, FOURTH_INVERSION};
	
	public static enum XMLChordElement {
		MAJOR, MINOR, DIMINISHED, AUGMENTED, SUS_4, SUS_2, DOMINANT, MAJOR_7, MAJOR_6, MINOR_7, MINOR_6, MAJOR_MINOR, HALF_DIMINISHED, DIMINISHED_7, 
		AUGMENTED_5, DIMINISHED_5, ADD_7, NINE, FLAT_NINE, SHARP_NINE, ELEVEN, SHARP_ELEVEN, THIRTEEN, FLAT_THIRTEEN};
	
	private static final NotePattern[] npArr = new NotePattern[]
			{
		new NotePattern("", new int[]{0}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler("note", IRRELEVANT_INVERSION_INDEX, 0)}),
		new NotePattern("", new int[]{0, 1}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler("m2nd", IRRELEVANT_INVERSION_INDEX, 1), new InversionHandler("M7th", IRRELEVANT_INVERSION_INDEX, 0)}),
		new NotePattern("", new int[]{0, 2}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler("M2nd", IRRELEVANT_INVERSION_INDEX, 1), new InversionHandler("m7th", IRRELEVANT_INVERSION_INDEX, 0)}),
		new NotePattern("", new int[]{0, 3}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler("m3rd", IRRELEVANT_INVERSION_INDEX, 0), new InversionHandler("M6th", IRRELEVANT_INVERSION_INDEX, 1)}),
		new NotePattern("", new int[]{0, 4}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler("M3rd", IRRELEVANT_INVERSION_INDEX, 0), new InversionHandler("m6th", IRRELEVANT_INVERSION_INDEX, 1)}),
		new NotePattern("", new int[]{0, 5}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler("p4", IRRELEVANT_INVERSION_INDEX, 1), new InversionHandler("p5", IRRELEVANT_INVERSION_INDEX, 0)}),
		new NotePattern("", new int[]{0, 6}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler("4th", IRRELEVANT_INVERSION_INDEX, 0)}),
		
		// triads
		new NotePattern(
				"min", 
				new int[]{0, 3, 7}, 
				ROOT_POSITION_INDEX, 
				new InversionHandler[]
						{
								new InversionHandler(ROOT_POSITION_INDEX, 0), 
								new InversionHandler(FIRST_INVERSION_INDEX, 2), 
								new InversionHandler(SECOND_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.MINOR},
				new int[] {2, 5, 9, 10},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.THIRD, ChordToneName.FIFTH}
				),
		new NotePattern(		// assumes major 7th #11 for extended chord tones
				"Maj", 
				new int[]{0, 3, 8}, 
				FIRST_INVERSION_INDEX, 
				new InversionHandler[]
						{
								new InversionHandler(ROOT_POSITION_INDEX, 0), 
								new InversionHandler(FIRST_INVERSION_INDEX, 2), 
								new InversionHandler(SECOND_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.MAJOR},
				new int[] {2, 6, 9, 11},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.THIRD, ChordToneName.FIFTH}
				),
		new NotePattern(		// assumes whole half diminished for extended chord tones
				"dim", 
				new int[]{0, 3, 6}, 
				ROOT_POSITION_INDEX, 
				new InversionHandler[]
						{
								new InversionHandler(ROOT_POSITION_INDEX, 0), 
								new InversionHandler(FIRST_INVERSION_INDEX, 2), 
								new InversionHandler(SECOND_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.DIMINISHED},
				new int[] {9, 2, 5, 8, 11},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.THIRD, ChordToneName.FIFTH}
				),
		new NotePattern(		// assumes whole tone scale for extended chord tones
				"aug", 
				new int[]{0, 4, 8}, 
				ROOT_POSITION_INDEX, 
				new InversionHandler[]
						{
								new InversionHandler(ROOT_POSITION_INDEX, 0), 
								new InversionHandler(FIRST_INVERSION_INDEX, 2), 
								new InversionHandler(SECOND_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.AUGMENTED},
				new int[] {2, 6, 10},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.THIRD, ChordToneName.FIFTH}
				),
		new NotePattern(		// assumes 2 and 6 for extended chord tones
				"sus4", 
				new int[]{0, 2, 7}, 
				FIRST_INVERSION_INDEX, 
				new InversionHandler[]
						{
								new InversionHandler(ROOT_POSITION_INDEX, 0), 
								new InversionHandler(FIRST_INVERSION_INDEX, 2), 
								new InversionHandler(SECOND_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.SUS_4},
				new int[] {2, 9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.FOURTH, ChordToneName.FIFTH}
				),
		
		// 7th chords with missing 3rd or 5th
		new NotePattern("m7no3", new int[]{0, 2, 9}, SECOND_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(SECOND_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1)}),
		new NotePattern("Maj7no3", new int[]{0, 1, 8}, SECOND_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(SECOND_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1)}),
		new NotePattern("7no3", new int[]{0, 2, 9}, SECOND_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(SECOND_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1)}),
		new NotePattern("mMaj7no3", new int[]{0, 1, 8}, SECOND_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(SECOND_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1)}),
		new NotePattern("m7b5no3", new int[]{0, 2, 8}, SECOND_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(SECOND_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1)}),
		
		new NotePattern("m7no5", new int[]{0, 2, 5}, SECOND_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(FIRST_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1)}),
		new NotePattern("Maj7no5", new int[]{0, 1, 5}, SECOND_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(FIRST_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1)}),
		new NotePattern("7no5", new int[]{0, 2, 6}, SECOND_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(FIRST_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1)}),
		new NotePattern("mMaj7no5", new int[]{0, 1, 4}, SECOND_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(FIRST_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1)}),
		new NotePattern("m7b5no5", new int[]{0, 2, 5}, SECOND_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(FIRST_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1)}),
		
		// duplicates requiring further context
		new NotePattern(
				"m7", 
				new int[]{0, 2, 5, 9}, 
				THIRD_INVERSION_INDEX, 
				new InversionHandler[]
						{
							new InversionHandler(ROOT_POSITION_INDEX, 0), 
							new InversionHandler(FIRST_INVERSION_INDEX, 3), 
							new InversionHandler(SECOND_INVERSION_INDEX, 2), 
							new InversionHandler(THIRD_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.MINOR_7},
				new int[] {2, 5, 9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}
				),
		new NotePattern(
				"Maj6", 
				new int[]{0, 2, 5, 9}, 
				SECOND_INVERSION_INDEX, 
				new InversionHandler[]
						{
							new InversionHandler(ROOT_POSITION_INDEX, 0), 
							new InversionHandler(FIRST_INVERSION_INDEX, 3), 
							new InversionHandler(SECOND_INVERSION_INDEX, 2), 
							new InversionHandler(THIRD_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.MAJOR_6},
				new int[] {2, 11},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SIXTH}
				),
		new NotePattern(
				"m7b5", 
				new int[]{0, 2, 5, 8}, 
				THIRD_INVERSION_INDEX, 
				new InversionHandler[]
						{
							new InversionHandler(ROOT_POSITION_INDEX, 0), 
							new InversionHandler(FIRST_INVERSION_INDEX, 3), 
							new InversionHandler(SECOND_INVERSION_INDEX, 2), 
							new InversionHandler(THIRD_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.HALF_DIMINISHED},
				new int[] {2, 5, 8},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}
				),
		new NotePattern(
				"m6", 
				new int[]{0, 2, 5, 8}, 
				SECOND_INVERSION_INDEX, 
				new InversionHandler[]
						{
								new InversionHandler(ROOT_POSITION_INDEX, 0), 
								new InversionHandler(FIRST_INVERSION_INDEX, 3), 
								new InversionHandler(SECOND_INVERSION_INDEX, 2), 
								new InversionHandler(THIRD_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.MINOR_6},
				new int[] {2, 5, 11},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SIXTH}
				),
		
		// symmetrical requiring further context - dealt with by duplicating the items and shifting the inversions around
		new NotePattern("7b5", new int[]{0, 2, 6, 8}, THIRD_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(FIRST_INVERSION_INDEX, 3), new InversionHandler(SECOND_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1)},
			new XMLChordElement[] {XMLChordElement.DOMINANT, XMLChordElement.DIMINISHED_5}),	// symmetrical. requires further context
		new NotePattern("7b5", new int[]{0, 2, 6, 8}, FIRST_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(SECOND_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1), new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(FIRST_INVERSION_INDEX, 3)},
				new XMLChordElement[] {XMLChordElement.DOMINANT, XMLChordElement.DIMINISHED_5}),	
		new NotePattern("dim7", new int[]{0, 3, 6, 9}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(FIRST_INVERSION_INDEX, 3), new InversionHandler(SECOND_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.DIMINISHED_7}),	// symmetrical.
		new NotePattern("dim7", new int[]{0, 3, 6, 9}, FIRST_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(FIRST_INVERSION_INDEX, 3), new InversionHandler(SECOND_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1), new InversionHandler(ROOT_POSITION_INDEX, 0)},
				new XMLChordElement[] {XMLChordElement.DIMINISHED_7}),
		new NotePattern("dim7", new int[]{0, 3, 6, 9}, SECOND_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(SECOND_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1), new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(FIRST_INVERSION_INDEX, 3)},
				new XMLChordElement[] {XMLChordElement.DIMINISHED_7}),
		new NotePattern("dim7", new int[]{0, 3, 6, 9}, SECOND_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(THIRD_INVERSION_INDEX, 1), new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(FIRST_INVERSION_INDEX, 3), new InversionHandler(SECOND_INVERSION_INDEX, 2)},
				new XMLChordElement[] {XMLChordElement.DIMINISHED_7}),
		
		
		//7th chords
		new NotePattern(
				"Maj7", 
				new int[]{0, 1, 5, 8}, 
				THIRD_INVERSION_INDEX, 
				new InversionHandler[]
						{
								new InversionHandler(ROOT_POSITION_INDEX, 0), 
								new InversionHandler(FIRST_INVERSION_INDEX, 3), 
								new InversionHandler(SECOND_INVERSION_INDEX, 2), 
								new InversionHandler(THIRD_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.MAJOR_7},
				new int[] {2, 6, 9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}
				),
		new NotePattern("7", 
				new int[]{0, 2, 6, 9}, 
				THIRD_INVERSION_INDEX, 
				new InversionHandler[]
						{
								new InversionHandler(ROOT_POSITION_INDEX, 0), 
								new InversionHandler(FIRST_INVERSION_INDEX, 3), 
								new InversionHandler(SECOND_INVERSION_INDEX, 2), 
								new InversionHandler(THIRD_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.DOMINANT},
				new int[] {1, 2, 3, 6, 8, 9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}
				),
		new NotePattern("7#5", new int[]{0, 2, 6, 10}, THIRD_INVERSION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), new InversionHandler(FIRST_INVERSION_INDEX, 3), new InversionHandler(SECOND_INVERSION_INDEX, 2), new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.DOMINANT}),	
		new NotePattern(
				"7sus4", 
				new int[]{0, 2, 5, 7}, 
				FIRST_INVERSION_INDEX, 
				new InversionHandler[]
						{
								new InversionHandler(ROOT_POSITION_INDEX, 0), 
								new InversionHandler(FIRST_INVERSION_INDEX, 3), 
								new InversionHandler(SECOND_INVERSION_INDEX, 2), 
								new InversionHandler(THIRD_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.SUS_4, XMLChordElement.ADD_7},
				new int[] {2, 9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}
				),
		new NotePattern(
				"mMaj7", 
				new int[]{0, 1, 4, 8}, 
				THIRD_INVERSION_INDEX, 
				new InversionHandler[]
						{
								new InversionHandler(ROOT_POSITION_INDEX, 0), 
								new InversionHandler(FIRST_INVERSION_INDEX, 3), 
								new InversionHandler(SECOND_INVERSION_INDEX, 2), 
								new InversionHandler(THIRD_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.MAJOR_MINOR},
				new int[] {2, 5, 9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}
				),
		new NotePattern(
				"Maj7#5", 
				new int[]{0, 1, 5, 9}, 
				THIRD_INVERSION_INDEX, 
				new InversionHandler[]
						{
								new InversionHandler(ROOT_POSITION_INDEX, 0), 
								new InversionHandler(FIRST_INVERSION_INDEX, 3), 
								new InversionHandler(SECOND_INVERSION_INDEX, 2), 
								new InversionHandler(THIRD_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.MAJOR_7, XMLChordElement.AUGMENTED_5},
				new int[] {2, 6},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}
				),
		new NotePattern("Maj7b5", new int[]{0, 1, 5, 7}, THIRD_INVERSION_INDEX, 
				new InversionHandler[]
						{
								new InversionHandler(ROOT_POSITION_INDEX, 0), 
								new InversionHandler(FIRST_INVERSION_INDEX, 3), 
								new InversionHandler(SECOND_INVERSION_INDEX, 2), 
								new InversionHandler(THIRD_INVERSION_INDEX, 1)
						},
				new XMLChordElement[] {XMLChordElement.MAJOR_7, XMLChordElement.DIMINISHED_5},
				new int[] {2, 9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}
				),		
		
		//9th chords
//		new NotePattern("m9", new int[]{0, 1, 5, 8, 10}, FOURTH_INVERSION_INDEX, new InversionHandler[]{
//				new InversionHandler(ROOT_POSITION_INDEX, 3), 	// 1st inversion
//				new InversionHandler(FIRST_INVERSION_INDEX, 2), // 2nd inversion
//				new InversionHandler(SECOND_INVERSION_INDEX, 1),	// 3rd inversion
//				new InversionHandler(THIRD_INVERSION_INDEX, 0),	// root position
//				new InversionHandler(FOURTH_INVERSION_INDEX, 4)}),	// 4th inversion
		new NotePattern("m9", new int[]{0, 2, 3, 7, 10}, ROOT_POSITION_INDEX, new InversionHandler[]{ // represent all chords from root and within one octave
				new InversionHandler(ROOT_POSITION_INDEX, 0), 		// sequence of InversionHandler items should be the same as the chord tones in the 2nd argument
				new InversionHandler(FOURTH_INVERSION_INDEX, 4), 
				new InversionHandler(FIRST_INVERSION_INDEX, 3),	
				new InversionHandler(SECOND_INVERSION_INDEX, 2),	
				new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.MINOR_7, XMLChordElement.NINE},
				new int[] {5, 9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.NINTH, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}			
				),	
		new NotePattern("9", new int[]{0, 2, 4, 7, 10}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), 	
				new InversionHandler(FOURTH_INVERSION_INDEX, 4), 
				new InversionHandler(FIRST_INVERSION_INDEX, 3),	
				new InversionHandler(SECOND_INVERSION_INDEX, 2),	
				new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.DOMINANT, XMLChordElement.NINE},
				new int[] {5, 9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.NINTH, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}	
				),	
		new NotePattern("7#9", new int[]{0, 3, 4, 7, 10}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), 	
				new InversionHandler(FOURTH_INVERSION_INDEX, 4), 
				new InversionHandler(FIRST_INVERSION_INDEX, 3),	
				new InversionHandler(SECOND_INVERSION_INDEX, 2),	
				new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.DOMINANT, XMLChordElement.SHARP_NINE},
				new int[] {5, 9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.NINTH, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}	
				),
		new NotePattern("7b9", new int[]{0, 1, 4, 7, 10}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), 	
				new InversionHandler(FOURTH_INVERSION_INDEX, 4), 
				new InversionHandler(FIRST_INVERSION_INDEX, 3),	
				new InversionHandler(SECOND_INVERSION_INDEX, 2),	
				new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.DOMINANT, XMLChordElement.FLAT_NINE},
				new int[] {6, 9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.NINTH, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}	
				),
		new NotePattern("7#5#9", new int[]{0, 3, 4, 8, 10}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), 	
				new InversionHandler(FOURTH_INVERSION_INDEX, 4), 
				new InversionHandler(FIRST_INVERSION_INDEX, 3),	
				new InversionHandler(SECOND_INVERSION_INDEX, 2),	
				new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.DOMINANT, XMLChordElement.AUGMENTED_5, XMLChordElement.SHARP_NINE},
				new int[] {6},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.NINTH, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}	
				),
		new NotePattern("9#5", new int[]{0, 2, 4, 8, 10}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), 	
				new InversionHandler(FOURTH_INVERSION_INDEX, 4), 
				new InversionHandler(FIRST_INVERSION_INDEX, 3),	
				new InversionHandler(SECOND_INVERSION_INDEX, 2),	
				new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.DOMINANT, XMLChordElement.AUGMENTED_5, XMLChordElement.NINE},
				new int[] {6},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.NINTH, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}	
				),
		new NotePattern("7#5b9", new int[]{0, 1, 4, 8, 10}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), 	
				new InversionHandler(FOURTH_INVERSION_INDEX, 4), 
				new InversionHandler(FIRST_INVERSION_INDEX, 3),	
				new InversionHandler(SECOND_INVERSION_INDEX, 2),	
				new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.DOMINANT, XMLChordElement.AUGMENTED_5, XMLChordElement.FLAT_NINE},
				new int[] {3, 6},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.NINTH, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}	
				),
		new NotePattern("Maj9", new int[]{0, 2, 4, 7, 11}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), 	
				new InversionHandler(FOURTH_INVERSION_INDEX, 4), 
				new InversionHandler(FIRST_INVERSION_INDEX, 3),	
				new InversionHandler(SECOND_INVERSION_INDEX, 2),	
				new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.MAJOR_7, XMLChordElement.NINE},
				new int[] {6, 9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.NINTH, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}	
				),
		new NotePattern("6/9", new int[]{0, 2, 4, 7, 9}, ROOT_POSITION_INDEX, new InversionHandler[]{		// same as 9sus4
				new InversionHandler(ROOT_POSITION_INDEX, 0), 	
				new InversionHandler(FOURTH_INVERSION_INDEX, 4), 
				new InversionHandler(FIRST_INVERSION_INDEX, 3),	
				new InversionHandler(SECOND_INVERSION_INDEX, 2),	
				new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.MAJOR_6, XMLChordElement.NINE},
				new int[] {6, 11},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.NINTH, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SIXTH}	
				),
		new NotePattern("m69", new int[]{0, 2, 3, 7, 9}, ROOT_POSITION_INDEX, new InversionHandler[]{
				new InversionHandler(ROOT_POSITION_INDEX, 0), 	
				new InversionHandler(FOURTH_INVERSION_INDEX, 4), 
				new InversionHandler(FIRST_INVERSION_INDEX, 3),	
				new InversionHandler(SECOND_INVERSION_INDEX, 2),	
				new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.MINOR_6, XMLChordElement.NINE},
				new int[] {5, 11},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.NINTH, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}	
				),
		new NotePattern("9sus4", new int[]{0, 2, 5, 7, 10}, ROOT_POSITION_INDEX, new InversionHandler[]{	
				new InversionHandler(ROOT_POSITION_INDEX, 0), 	
				new InversionHandler(FOURTH_INVERSION_INDEX, 4), 
				new InversionHandler(FIRST_INVERSION_INDEX, 3),	
				new InversionHandler(SECOND_INVERSION_INDEX, 2),	
				new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.SUS_4, XMLChordElement.ADD_7, XMLChordElement.NINE},
				new int[] {9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.NINTH, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}	
				),
		new NotePattern("Maj9#5", new int[]{0, 2, 4, 8, 11}, ROOT_POSITION_INDEX, new InversionHandler[]{	
				new InversionHandler(ROOT_POSITION_INDEX, 0), 	
				new InversionHandler(FOURTH_INVERSION_INDEX, 4), 
				new InversionHandler(FIRST_INVERSION_INDEX, 3),	
				new InversionHandler(SECOND_INVERSION_INDEX, 2),	
				new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.MAJOR_7, XMLChordElement.AUGMENTED_5, XMLChordElement.NINE},
				new int[] {6},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.NINTH, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}	
				),
		new NotePattern("m9M7", new int[]{0, 2, 3, 7, 11}, ROOT_POSITION_INDEX, new InversionHandler[]{	
				new InversionHandler(ROOT_POSITION_INDEX, 0), 	
				new InversionHandler(FOURTH_INVERSION_INDEX, 4), 
				new InversionHandler(FIRST_INVERSION_INDEX, 3),	
				new InversionHandler(SECOND_INVERSION_INDEX, 2),	
				new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.MAJOR_MINOR, XMLChordElement.NINE},
				new int[] {5, 9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.NINTH, ChordToneName.THIRD, ChordToneName.FIFTH, ChordToneName.SEVENTH}	
				),
		new NotePattern("m11", new int[]{0, 2, 3, 5, 7, 10}, ROOT_POSITION_INDEX, new InversionHandler[]{	
				new InversionHandler(ROOT_POSITION_INDEX, 0), 	
				new InversionHandler(FOURTH_INVERSION_INDEX, 5), 
				new InversionHandler(FIRST_INVERSION_INDEX, 4),	
				new InversionHandler(SECOND_INVERSION_INDEX, 2),	
				new InversionHandler(THIRD_INVERSION_INDEX, 1)},
				new XMLChordElement[] {XMLChordElement.MINOR_7, XMLChordElement.NINE, XMLChordElement.ELEVEN},
				new int[] {9},
				new ChordToneName[] {ChordToneName.ROOT, ChordToneName.NINTH, ChordToneName.THIRD, ChordToneName.ELEVENTH, ChordToneName.FIFTH, ChordToneName.SEVENTH}	
				),

	};
	
	
	private static final char SHARP_SIGN = '#';
	private static final char FLAT_SIGN = 'b';
	
	
	
	public static int[] getChordTones(String aChordSymbol) 
	{
		String[] str = splitChordSymbol(aChordSymbol);
		int rootIndex = getNoteIndexFromSymbol(str[0]);
		switch(str[1]) 
		{
			case "":	str[1] = "Maj";
						break;
			case "m":	str[1] = "min";
						break;
		}
		NotePattern np = getNotePatternFromName(str[1]);
		if (np == null)
		{
			return new int[0];
		} 
		else 
		{
			Integer[] intervals = np.getRootPositionIntervals();
			int[] ints = new int[intervals.length];
			for (int i = 0; i < intervals.length; i++) 
			{
				ints[i] = (intervals[i] + rootIndex) % 12;
			}
			return ints;		
		}
	}
	
	
	
	public static int[] getExtendedChordTones(String aChordSymbol)
	{
		String[] str = splitChordSymbol(aChordSymbol);
		int rootIndex = getNoteIndexFromSymbol(str[0]);
		switch(str[1]) 
		{
			case "":	str[1] = "Maj";
						break;
			case "m":	str[1] = "min";
						break;
		}
		NotePattern np = getNotePatternFromName(str[1]);
		if (np == null)
		{
			return new int[0];
		} 
		else 
		{
			int[] intervals = np.getExtendedChordToneIntervals();
			int[] ints = new int[intervals.length];
			for (int i = 0; i < intervals.length; i++) 
			{
				ints[i] = (intervals[i] + rootIndex) % 12;
			}
			return ints;		
		}
	}
	
	
	
	public static NotePattern getNotePatternFromChordSymbol(String aChordSymbol)
	{
		String[] str = splitChordSymbol(aChordSymbol);
		switch(str[1]) 
		{
			case "":	str[1] = "Maj";
						break;
			case "m":	str[1] = "min";
						break;
		}
		return getNotePatternFromName(str[1]);
	}
	
	
	public static NotePattern getNotePatternFromName(String string) 
	{
		for (NotePattern np: npArr) {
			if (np.name().equals(string)) return np;
		}
		return null;
	}

	
	
	private static String[] splitChordSymbol(String chordSymbol) 
	{
		String[] str = new String[2];
		if (chordSymbol.length() > 1) {
			if (chordSymbol.charAt(1) == SHARP_SIGN || chordSymbol.charAt(1) == FLAT_SIGN) 
			{
				str = splitStringAt(chordSymbol, 2);
			} 
			else 
			{
				str = splitStringAt(chordSymbol, 1);
			}			
		} 
		else 
		{
			str[0] = chordSymbol;
			str[1] = "";
		}
		return str;
	}

	
	
	private static String[] splitStringAt(String str, int i) 
	{
		String[] strArr = new String[2];
		strArr[0] = str.substring(0, i);
		if (str.length() > i) 
		{
			strArr[1] = str.substring(i, str.length());
		} else {
			strArr[1] = "";
		}		
		return strArr;
	}

	
	
	public static int[] getDiatonicModeDegrees(ModeObject mode)
	{
		if (keyMap.containsKey(mode.keyMapIndex))
		{
			Integer[][] iArr = keyMap.get(mode.keyMapIndex);
			int[] arr = new int[mode.noteCount];
			int count = 0;
			int startIndex = getKeyMapStartIndex(iArr, mode.startNote);
			int index = startIndex;
			while (count < mode.noteCount)
			{
				if (iArr[index % 12][1] == 0)
				{
					arr[count] = index - startIndex;
					
					count++;
				}
				index++;
			}
			return arr;
		} 
		else 
		{
			return new int[0];
		}		
	}
	
	
	
	public static int[][] getChromaticModeModel(ModeObject mode)
	{
		if (keyMap.containsKey(mode.keyMapIndex))
		{
			Integer[][] iArr = keyMap.get(mode.keyMapIndex);
			int[][] arr = new int[12][2];
			int count = 0;
			int startIndex = getKeyMapStartIndex(iArr, mode.startNote);
			int index = startIndex;
			int degree = 1;
			int degreeInArray = iArr[startIndex][0]; 
			while (count < 12)
			{
				if (iArr[index % 12][0] != degreeInArray)
				{
					degree++;
					degreeInArray = iArr[index % 12][0];
				}
				if (degree == mode.noteCount + 1)
				{
					arr[count][0] = mode.noteCount;	
					arr[count][1] = 1;
				} 
				else 
				{
					arr[count][0] = degree;	
					arr[count][1] = iArr[index % 12][1];
				}				
				count++;
				index++;
			}
			return arr;
		} 
		else 
		{
			return new int[0][0];
		}	
	}
	
	
	
	public static int getIntervalFromDiatonicKeyInfo(String keyType, int[] keyDegree)
	{
		// keyDegree is two values [0] = scale degree (1 - 7) [0] = 0 - diatonic, 1 - chromatic
		// i.e. {3,0} is the diatonic 3rd of a key, while {3,1} is the non diatonic third degree
		if (keyMap.containsKey(keyType))
		{
			int interval = 0;
			for (Integer[] arr: keyMap.get(keyType))
			{
				if (arr[0] == keyDegree[0] && arr[1] == keyDegree[1])
				{
					return interval;
				}				
				interval++;
			}
		} 
		else 
		{
			return 0;
		}
		return 0;
	}

	
	
	private static int getKeyMapStartIndex(Integer[][] iArr, int startNote) 
	{
		for (int i = 0; i < iArr.length; i++)
		{
			Integer[] arr = iArr[i];
			//System.out.println(arr[0] + ", " + arr[1]);
			if (arr[0] == startNote && arr[1] == 0)
			{
				return i;
			}
		}
		return 0;
	}


	
	private static final HashMap<String, Integer[][]> keyMap = new HashMap<String, Integer[][]>()
			{{
				put(MAJOR, new Integer[][]{
					new Integer[]{1, 0},		// root
					new Integer[]{2, -1},		// flat 2
					new Integer[]{2, 0},		// 2
					new Integer[]{3, -1},		// flat 3
					new Integer[]{3, 0},		// 3
					new Integer[]{4, 0},		// 4
					new Integer[]{4, 1},		// sharp 4
					new Integer[]{5, 0},		// 5
					new Integer[]{5, 1},		// sharp 5
					new Integer[]{6, 0},		// 6
					new Integer[]{7, -1},		// flat 7
					new Integer[]{7, 0}			// 7
					
				});
				put(MINOR, new Integer[][]{
					new Integer[]{1, 0},		// root
					new Integer[]{2, -1},		// flat 2
					new Integer[]{2, 0},		// 2
					new Integer[]{3, 0},		// flat 3
					new Integer[]{3, 1},		// 3
					new Integer[]{4, 0},		// 4
					new Integer[]{4, 1},		// sharp 4
					new Integer[]{5, 0},		// 5
					new Integer[]{6, 0},		// diatonic 6
					new Integer[]{6, 1},		// raized diatonic 6
					new Integer[]{7, 0},		// flat 7
					new Integer[]{7, 1}			// 7
					
				});
				put(HARMONIC_MINOR, new Integer[][]{
					new Integer[]{1, 0},		// root
					new Integer[]{2, -1},		// flat 2
					new Integer[]{2, 0},		// 2
					new Integer[]{3, 0},		// flat 3
					new Integer[]{3, 1},		// 3
					new Integer[]{4, 0},		// 4
					new Integer[]{4, 1},		// sharp 4
					new Integer[]{5, 0},		// 5
					new Integer[]{6, 0},		// diatonic 6
					new Integer[]{6, 1},		// raized diatonic 6
					new Integer[]{7, -1},		// flat 7
					new Integer[]{7, 0}			// 7
					
				});
				put(MELODIC_MINOR, new Integer[][]{
					new Integer[]{1, 0},		// root
					new Integer[]{2, -1},		// flat 2
					new Integer[]{2, 0},		// 2
					new Integer[]{3, 0},		// flat 3
					new Integer[]{3, 1},		// 3
					new Integer[]{4, 0},		// 4
					new Integer[]{4, 1},		// sharp 4
					new Integer[]{5, 0},		// 5
					new Integer[]{6, -1},		// sharp 5
					new Integer[]{6, 0},		// 6
					new Integer[]{7, -1},		// flat 7
					new Integer[]{7, 0}			// 7
					
				});
				put(CHROMATIC, new Integer[][]{
					new Integer[]{1, 0},		// root
					new Integer[]{2, 0},		// flat 2
					new Integer[]{3, 0},		// 2
					new Integer[]{4, 0},		// flat 3
					new Integer[]{5, 0},		// 3
					new Integer[]{6, 0},		// 4
					new Integer[]{7, 0},		// sharp 4
					new Integer[]{8, 0},		// 5
					new Integer[]{9, 0},		// sharp 5
					new Integer[]{10, 0},		// 6
					new Integer[]{11, 0},		// flat 7
					new Integer[]{12, 0}			// 7
					
				});
			}};
			// Scale dictionary stuff - 7 note scales.........................................
			
			public static final String MAJOR = "major";
			public static final String MINOR = "minor";
			public static final String HARMONIC_MINOR = "harmonic minor";
			public static final String MELODIC_MINOR = "melodic minor";
			public static final String CHROMATIC = "chromatic";
			
			public static final ModeObject IONIAN_MODE = new ModeObject(1, 7, MAJOR, "major");
			public static final ModeObject DORIAN_MODE = new ModeObject(2, 7, MAJOR, "dorian");
			public static final ModeObject PHRYGIAN_MODE = new ModeObject(3, 7, MAJOR, "phrygian");
			public static final ModeObject LYDIAN_MODE = new ModeObject(4, 7, MAJOR, "lydian");
			public static final ModeObject MIXOLYDIAN_MODE = new ModeObject(5, 7, MAJOR, "mixolydian");
			public static final ModeObject AOELIAN_MODE = new ModeObject(6, 7, MAJOR, "minor");
			public static final ModeObject LOCRIAN_MODE = new ModeObject(7, 7, MAJOR, "locrian");
			
			public static final ModeObject HARMONIC_MINOR_ONE = new ModeObject(1, 7, HARMONIC_MINOR, "harmonic minor");
			public static final ModeObject HARMONIC_MINOR_FIVE = new ModeObject(5, 7, HARMONIC_MINOR, "harmonic minor 5");
			
			public static final ModeObject MELODIC_MINOR_ONE = new ModeObject(1, 7, MELODIC_MINOR, "melodic minor");
			public static final ModeObject DORIAN_FLAT_TWO = new ModeObject(2, 7, MELODIC_MINOR, "dorian b2");
			public static final ModeObject LYDIAN_SHARP_FIVE = new ModeObject(3, 7, MELODIC_MINOR, "lydian #5");
			public static final ModeObject LYDIAN_DOMINANT = new ModeObject(4, 7, MELODIC_MINOR, "lydian dominant");
			public static final ModeObject MIXOLYDIAN_FLAT_13 = new ModeObject(5, 7, MELODIC_MINOR, "mixolydian b13");
			public static final ModeObject LOCIAN_NATURAL_9 = new ModeObject(6, 7, MELODIC_MINOR, "locrian nat9");
			public static final ModeObject ALTERED = new ModeObject(7, 7, MELODIC_MINOR, "altered");
			
			public static final ModeObject CHROMATIC_SCALE = new ModeObject(1, 12, CHROMATIC, "chromatic");

			public static String[] basicNoteNames = new String[] {"A","B","C","D","E","F","G"};


	public static void main(String[] args)
	{
		int xmlKey = -7;
		int[] arr = CSD.getDiatonicModeDegrees(CSD.IONIAN_MODE);
		List<Integer> list = new ArrayList<Integer>();
	
		int x = (xmlKey + 12) * 7;
		for (int i: arr)
		{
			list.add((i + x) % 12);
		}
		Collections.sort(list);
		for (Integer i: list)
		{
			System.out.println(i);
		}
	}



	

	
	
}
