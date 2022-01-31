package main.java.da_utils.chord_scale_dictionary.test;
import java.util.ArrayList;

import DataObjects.note_buffer.PlayedMidiNote;
import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;
import main.java.da_utils.chord_scale_dictionary.datatypes.NotePatternChord;


public class ChordAnalysisObjectTest{
	
// methods to test	
//	int []�easyChordData ()	�
//	int []�rootPositionIntervals ()	�
//	String modNoteListToString ()	�
//	String�inversionListToString ()	�
//	String�matchingNotePatternToString ()	�
//	String�noteListToString ()	�
//	String�noteListNoDuplicatesToString ()	�
//	String toString ()	�
//	String�rootListToString ()	�
//	String�slashChordToString ()	�
//	String�chordToString ()	�
//	String�chordTypeName ()	�
//	ArrayList< NotePatternChord >�chordOptions ()	�
//	int�simpleRoot ()	�
//	String�simpleChord ()
	
	
	public ChordAnalysisObjectTest(){

		ChordAnalysisObject cao = new ChordAnalysisObject(makePlayedList(liveNoteList()), liveNoteList() );
		System.out.println(cao.matchingNotePatternToString());
		System.out.println(cao.toString());
		System.out.println(cao.rootListToString());
		for (int i: cao.rootPositionIntervals()){
			System.out.println("--- " + i);
		}
		System.out.println("cao.chordToString()=" + cao.chordToString());
		System.out.println("======methods tests==============================");
		int[] ecd = cao.easyChordData ();
		System.out.println(makeIntArrayString("int []�easyChordData ():", ecd));
		int[] rpi = cao.rootPositionIntervals ();	
		System.out.println(makeIntArrayString("int []�rootPositionIntervals ():", rpi));
		System.out.println("String modNoteListToString ():" + cao.modNoteListToString ());
		System.out.println("String�inversionListToString ():" + cao.inversionListToString ());
		System.out.println("String�matchingNotePatternToString ():" + cao.matchingNotePatternToString ());
		System.out.println("String�noteListToString ():" + cao.noteListToString ());
		System.out.println("String�noteListNoDuplicatesToString ():" + cao.noteListNoDuplicatesToString ());
		System.out.println("String toString ():" + cao.toString ());
		System.out.println("String�rootListToString ():" + cao.rootListToString ());
		System.out.println("String�slashChordToString ():" + cao.slashChordToString ());
		System.out.println("String�chordToString ():" + cao.chordToString ());
		System.out.println("String�chordTypeName ():" + cao.chordTypeName ());
		System.out.println("int�simpleRoot ():" + cao.simpleRoot ());
		System.out.println("String�simpleChord ():" + cao.simpleChord ());
		System.out.println("ArrayList< NotePatternChord >�chordOptions ():");
		for (NotePatternChord npc: cao.chordOptions()){
			System.out.println(npc.toString());
		}

	}
	
	private String makeIntArrayString(String string, int[] ecd) {
		String str = string;
		for (int i: ecd){
			str += i + ",";
		}
		return str;
	}

	private ArrayList<PlayedMidiNote> playedNoteList(){
		ArrayList<PlayedMidiNote> pmn = new ArrayList<PlayedMidiNote>();
		pmn.add(new PlayedMidiNote(40, 100, 1));
		pmn.add(new PlayedMidiNote(44, 100, 1));
		pmn.add(new PlayedMidiNote(47, 100, 1));
//		pmn.add(new PlayedMidiNote(35, 100, 1));
//		pmn.add(new PlayedMidiNote(32, 100, 1));
		
		return pmn;
	}
	private ArrayList<LiveMidiNote> liveNoteList(){
		ArrayList<LiveMidiNote> lmn = new ArrayList<LiveMidiNote>();
		lmn.add(new LiveMidiNote(60, 4.0, 3.0, 100, 0));
		lmn.add(new LiveMidiNote(63, 4.0, 3.0, 100, 0));
		lmn.add(new LiveMidiNote(67, 4.0, 3.0, 100, 0));
		lmn.add(new LiveMidiNote(70, 4.0, 3.0, 100, 0));
		lmn.add(new LiveMidiNote(74, 4.0, 3.0, 100, 0));
		return lmn;

	}
	private ArrayList<PlayedMidiNote> makePlayedList(ArrayList<LiveMidiNote> lmList){
		ArrayList<PlayedMidiNote> pmn = new ArrayList<PlayedMidiNote>();
		for (LiveMidiNote lm: lmList){
			pmn.add(new PlayedMidiNote(lm.note, lm.velocity, 1));
		}
		return pmn;
	}
	
	public static void main (String[] args)
	{
		new ChordAnalysisObjectTest();
	}
}
