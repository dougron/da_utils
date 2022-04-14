package main.java.da_utils.static_chord_scale_dictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import main.java.da_utils.static_chord_scale_dictionary.CSD.XMLChordElement;



public class NotePattern {
	
	
	
	private Integer[] intervals;			//interval structure as from the original ChordScaleDictionary with smallest intervals first
	private String[] inversionOptions;		// old. use inversionHandlerOptions......
	private InversionHandler[] inversionHandlerOptions;		// index of the lowest note of the chord in the root position version of intervals will get the 
															// InversionHandler for that inversion from this Array.
	private int thisInversion;				// inversion that the intervals represent: e.g. 0, 3, 8 is a 1st inversion major chord, which then can figure out
											// that 0, 4, 7 is a root position. more informtion on inversion come from the inversionHandlerOptions
	private String name;
	private Integer[] rootPositionIntervals;
	private XMLChordElement[] xmlChordElements = new XMLChordElement[0];
	private int[] extendedChordToneIntervals = new int[] {};
	private ChordToneName[] chordToneNames;

	
	
	
	
	public NotePattern(String name, int[] intervals, int thisInversion, String[] inversionOptions)
	{
		// instantiator for testing only...........
		this.name = name;
		this.thisInversion = thisInversion;
		this.inversionOptions = inversionOptions;
		this.intervals = new Integer[intervals.length];
		for (int i = 0; i < intervals.length; i++){
			this.intervals[i] = (Integer)intervals[i];
		}
		this.rootPositionIntervals = makeRootPositionIntervals();
	}
	
	
	
	public NotePattern(String name, int[] intervals, int thisInversion, InversionHandler[] inversionHandlerOptions)
	{
		this.name = name;
		this.thisInversion = thisInversion;
		this.inversionHandlerOptions = inversionHandlerOptions;
		this.intervals = new Integer[intervals.length];
		for (int i = 0; i < intervals.length; i++){
			this.intervals[i] = (Integer)intervals[i];
		}
		this.rootPositionIntervals = makeRootPositionIntervals();
	}
	

	
	public NotePattern(
			String name, 
			int[] intervals, 
			int thisInversion, 
			InversionHandler[] inversionHandlerOptions,
			XMLChordElement[] xmlChordElements
			)
	{
		this.name = name;
		this.thisInversion = thisInversion;
		this.inversionHandlerOptions = inversionHandlerOptions;
		this.intervals = new Integer[intervals.length];
		for (int i = 0; i < intervals.length; i++){
			this.intervals[i] = (Integer)intervals[i];
		}
		this.rootPositionIntervals = makeRootPositionIntervals();
		this.xmlChordElements = xmlChordElements;
	}



	public NotePattern(
			String name, 
			int[] intervals, 
			int thisInversion, 
			InversionHandler[] inversionHandlerOptions,
			XMLChordElement[] xmlChordElements, 
			int[] extendedChordToneIntervals)
	{
		this.name = name;
		this.thisInversion = thisInversion;
		this.inversionHandlerOptions = inversionHandlerOptions;
		this.intervals = new Integer[intervals.length];
		for (int i = 0; i < intervals.length; i++){
			this.intervals[i] = (Integer)intervals[i];
		}
		this.rootPositionIntervals = makeRootPositionIntervals();
		this.xmlChordElements = xmlChordElements;
		this.extendedChordToneIntervals  = extendedChordToneIntervals;
	}
	
	
	
	public NotePattern(
			String name, 
			int[] intervals, 
			int thisInversion, 
			InversionHandler[] inversionHandlerOptions,
			XMLChordElement[] xmlChordElements, 
			int[] extendedChordToneIntervals,
			ChordToneName[] chordToneNames)
			
	{
		this.name = name;
		this.thisInversion = thisInversion;
		this.inversionHandlerOptions = inversionHandlerOptions;
		this.intervals = new Integer[intervals.length];
		for (int i = 0; i < intervals.length; i++){
			this.intervals[i] = (Integer)intervals[i];
		}
		this.rootPositionIntervals = makeRootPositionIntervals();
		this.xmlChordElements = xmlChordElements;
		this.extendedChordToneIntervals  = extendedChordToneIntervals;
		this.chordToneNames = chordToneNames; // this correlates to the equivalent index of the list of notes returned from CSD.getChordTones()
	}



	public NotePatternAnalysis getNotePatternAnalysis(Integer[][] noteArr) 
	{
		int index = 0;
		boolean hit = false;
		ArrayList<Integer> hitList = new ArrayList<Integer>();
		for (Integer[] iarr: noteArr){
			if (Arrays.equals(iarr, intervals)){
				hit = true;
				hitList.add(index);
			}
			index++;
		}
		if (hit){
			int inversionOptionIndex = getInversionOptionIndex(hitList);
			if (intervals.length == 1){
				return noteChordAnalysis();
			} else if (intervals.length == 2){
				return intervalChordAnalysis(inversionOptionIndex);
			} else if (intervals.length > 2 && intervals.length != 5){
				return triadChordAnalysis(inversionOptionIndex);
			} else if (intervals.length == 5){
				return ninthChordAnalysis(inversionOptionIndex);
			}
			return null;
//			return name + ":" + inversionOptions[inversionIndex];
		} else {
			return null;
		}
		
	}



	private int getInversionOptionIndex(ArrayList<Integer> hitList)
	{
		ArrayList<Integer> invList = new ArrayList<Integer>();
		for (Integer hitIndex: hitList){
			invList.add((thisInversion - hitIndex + intervals.length) % intervals.length);
		}
		int inversionOptionIndex = Collections.min(invList);
		return inversionOptionIndex;
	}

	
	
	public Integer[] getRootPositionIntervals() 
	{
		return rootPositionIntervals;
	}

	
	
	public String name() 
	{
		return name;
	}
	
	
	
	public XMLChordElement[] getXMLChordElements()
	{
		return xmlChordElements;
	}
	
	
	
	public int[] getExtendedChordToneIntervals()
	{
		return extendedChordToneIntervals;
	}
	
	
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("NotePattern: intervals=");
		for (Integer i: intervals)
		{
			sb.append(i + ",");
		}
		sb.append("\nthisInversion=" + thisInversion + "\n");
		for (XMLChordElement x: xmlChordElements)
		{
			sb.append(x + ",");
		}
		
		return sb.toString();
	}
	
	
// privates =================================================================== 
	
	
	private Integer[] makeRootPositionIntervals() 
	{
		Integer[] arr = new Integer[intervals.length];
		int inc = intervals.length - thisInversion;
		for (int i = 0; i < intervals.length; i++)
		{
			arr[i] = intervals[(i + inc) % intervals.length];
		}
		Integer one = arr[0];
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i] < one)
			{
				arr[i] += 12;
			}
			arr[i] -= one;
		}
		return arr;
	}

	
	
	private NotePatternAnalysis ninthChordAnalysis(int inversionOptionIndex) 
	{
		NotePatternAnalysis npa = new NP_9thChord();
		InversionHandler ih = inversionHandlerOptions[inversionOptionIndex];
		if (ih.useNotePatternName)
		{
			npa.setName(name);
		} 
		else 
		{
			npa.setName(ih.name);
		}
		npa.setInversionIndex(inversionHandlerOptions[inversionOptionIndex].inversionIndex);
		npa.setRootNoteIndex(ih.rootNoteIndex);
		return npa;
	}

	
	
	private NotePatternAnalysis triadChordAnalysis(int inversionOptionIndex) 
	{
		NotePatternAnalysis npa = new NP_Triad();
		InversionHandler ih = inversionHandlerOptions[inversionOptionIndex];
		if (ih.useNotePatternName)
		{
			npa.setName(name);
		} 
		else 
		{
			npa.setName(ih.name);
		}
		npa.setInversionIndex(inversionHandlerOptions[inversionOptionIndex].inversionIndex);
		npa.setRootNoteIndex(ih.rootNoteIndex);
		return npa;
	}

	
	
	private NotePatternAnalysis intervalChordAnalysis(int inversionOptionIndex) 
	{
		NotePatternAnalysis npa = new NP_Interval();
		if (inversionOptions == null)
		{
			InversionHandler ih = inversionHandlerOptions[inversionOptionIndex];
			npa.setName(ih.name);
			npa.setRootNoteIndex(ih.rootNoteIndex);
		} 
		else 
		{
			npa.setName(inversionOptions[inversionOptionIndex]);
		}		
		return npa;
	}
	
	

	private NotePatternAnalysis noteChordAnalysis() 
	{		
		return new NP_Note();
	}



	public ChordToneName[] getChordToneNames ()
	{
		return chordToneNames;
	}



}
