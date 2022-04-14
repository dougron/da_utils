package test.java.da_utils.chord_progression_analyzer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;
import main.java.da_utils.chord_progression_analyzer.chord_chunk.ChordChunkList;

class ProgressionAnalyzer_Test
{
	
	static ArrayList<Integer> Dm7;
	static ArrayList<Integer> G7;
	static ArrayList<Integer> CMaj7;
	static ArrayList<Integer> C;
	static ArrayList<Integer> Dm;
	
	
	@BeforeAll
	static void makeNoteLists()
	{
		Dm7 = makeNoteList(new int[] {50, 53, 57, 60});
		Dm = makeNoteList(new int[] {50, 53, 57});
		G7 = makeNoteList(new int[] {50, 53, 55, 59});
		CMaj7 = makeNoteList(new int[] {48, 52, 55, 59});
		C = makeNoteList(new int[] {48, 52, 55});
	}

	
	private static ArrayList<Integer> makeNoteList(int[] chordTones)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int pitch: chordTones)
		{
			list.add(pitch);
		}
		return list;
	}
	
	
	private LiveClip makeLiveClip(Object[] objects)
	{
		int index = 0;
		LiveClip lc = new LiveClip();
		double end = 0.0;
		while (true)
		{
			if (index >= objects.length) break;
			double position = (double)objects[index];
			double length = (double)objects[index + 1];
			end = position + length;
			ArrayList<Integer> pitchList = (ArrayList<Integer>)objects[index + 2];
			for (Integer pitch: pitchList)
			{
				lc.addNote(pitch, position, length, 64, 0);
			}
			index += 3;
		}
		lc.loopStart = 0.0;
		lc.loopEnd = end;
		lc.length = end;
		return lc;
	}

	
	@Test
	final void major_ii_V_I_returns_correct_function_string_for_all_chords()
	{
		LiveClip lc = makeLiveClip(new Object[] {0.0, 4.0, Dm7, 4.0, 4.0, G7, 8.0, 8.0, CMaj7});
		ChordChunkList ccl = new ChordChunkList("4n", lc);
		ProgressionAnalyzer pa = new ProgressionAnalyzer(ccl);
//		System.out.println(pa.toString());
		assertEquals("iim7", pa.getPrevailingCIKO(2.0).functionString);
		assertEquals("V7", pa.getPrevailingCIKO(5.0).functionString);
		assertEquals("IMaj7", pa.getPrevailingCIKO(8.0).functionString);
	}
	
	
	@Test
	final void major_ii_V_I_returns_correct_chord_tones_for_all_chords()
	{
		LiveClip lc = makeLiveClip(new Object[] {0.0, 4.0, Dm7, 4.0, 4.0, G7, 8.0, 8.0, CMaj7});
		ChordChunkList ccl = new ChordChunkList("4n", lc);
		ProgressionAnalyzer pa = new ProgressionAnalyzer(ccl);

		int[] chordTones = pa.getPrevailingCIKO(2.0).getModChordTones();
		assertEquals(2, chordTones[0]);
		assertEquals(5, chordTones[1]);
		assertEquals(9, chordTones[2]);
		assertEquals(0, chordTones[3]);
		
		chordTones = pa.getPrevailingCIKO(6.0).getModChordTones();
		assertEquals(7, chordTones[0]);
		assertEquals(11, chordTones[1]);
		assertEquals(2, chordTones[2]);
		assertEquals(5, chordTones[3]);
		
		chordTones = pa.getPrevailingCIKO(8.0).getModChordTones();
		assertEquals(0, chordTones[0]);
		assertEquals(4, chordTones[1]);
		assertEquals(7, chordTones[2]);
		assertEquals(11, chordTones[3]);
	}
	
	
	@Test
	final void major_ii_V_I_returns_correct_scale_tones_for_all_chords()
	{
		LiveClip lc = makeLiveClip(new Object[] {0.0, 4.0, Dm7, 4.0, 4.0, G7, 8.0, 8.0, CMaj7});
		ChordChunkList ccl = new ChordChunkList("4n", lc);
		ProgressionAnalyzer pa = new ProgressionAnalyzer(ccl);

		int[] chordTones = pa.getPrevailingCIKO(2.0).getModScaleTones();
		assertEquals(4, chordTones[0]);
		assertEquals(7, chordTones[1]);
		assertEquals(11, chordTones[2]);
		
		chordTones = pa.getPrevailingCIKO(6.0).getModScaleTones();
		assertEquals(9, chordTones[0]);
		assertEquals(0, chordTones[1]);
		assertEquals(4, chordTones[2]);
		
		chordTones = pa.getPrevailingCIKO(8.0).getModScaleTones();
		assertEquals(2, chordTones[0]);
		assertEquals(5, chordTones[1]);
		assertEquals(9, chordTones[2]);
	}
	
	
	
	@Test
	final void major_ii_V_I_returns_correct_interval_structure_for_chord_tones_for_all_chords()
	{
		LiveClip lc = makeLiveClip(new Object[] {0.0, 4.0, Dm7, 4.0, 4.0, G7, 8.0, 8.0, CMaj7});
		ChordChunkList ccl = new ChordChunkList("4n", lc);
		ProgressionAnalyzer pa = new ProgressionAnalyzer(ccl);

		int[] chordTones = pa.getPrevailingCIKO(2.0).getChordToneIntervals();
		assertEquals(0, chordTones[0]);
		assertEquals(3, chordTones[1]);
		assertEquals(7, chordTones[2]);
		assertEquals(10, chordTones[3]);
		
		chordTones = pa.getPrevailingCIKO(6.0).getChordToneIntervals();
		assertEquals(0, chordTones[0]);
		assertEquals(4, chordTones[1]);
		assertEquals(7, chordTones[2]);
		assertEquals(10, chordTones[3]);
		
		chordTones = pa.getPrevailingCIKO(8.0).getChordToneIntervals();
		assertEquals(0, chordTones[0]);
		assertEquals(4, chordTones[1]);
		assertEquals(7, chordTones[2]);
		assertEquals(11, chordTones[3]);
	}
	

	@Test
	final void major_ii_V_I_returns_correct_extended_chord_tones_for_all_chords()
	{
		LiveClip lc = makeLiveClip(new Object[] {0.0, 4.0, Dm7, 4.0, 4.0, G7, 8.0, 8.0, CMaj7});
		ChordChunkList ccl = new ChordChunkList("4n", lc);
		ProgressionAnalyzer pa = new ProgressionAnalyzer(ccl);

		int[] chordTones = pa.getPrevailingCIKO(2.0).getModExtendedChordTones();
		assertEquals(3, chordTones.length);
		assertEquals(4, chordTones[0]);
		assertEquals(7, chordTones[1]);
		assertEquals(11, chordTones[2]);
		
		chordTones = pa.getPrevailingCIKO(6.0).getModExtendedChordTones();
		assertEquals(6, chordTones.length);
		assertEquals(8, chordTones[0]);
		assertEquals(9, chordTones[1]);
		assertEquals(10, chordTones[2]);
		assertEquals(1, chordTones[3]);
		assertEquals(3, chordTones[4]);
		assertEquals(4, chordTones[5]);
		
		chordTones = pa.getPrevailingCIKO(8.0).getModExtendedChordTones();
		assertEquals(3, chordTones.length);
		assertEquals(2, chordTones[0]);
		assertEquals(6, chordTones[1]);
		assertEquals(9, chordTones[2]);
	}

	
	@Test
	final void single_major_chord_returns_correct_function_string()
	{
		LiveClip lc = makeLiveClip(new Object[] {0.0, 4.0, C});
		ChordChunkList ccl = new ChordChunkList("4n", lc);
		ProgressionAnalyzer pa = new ProgressionAnalyzer(ccl);
//		System.out.println(pa.toString());
		assertEquals("IMaj", pa.getPrevailingCIKO(2.0).functionString);
	}
	
	
	@Test
	final void single_minor_chord_returns_correct_function_string()
	{
		LiveClip lc = makeLiveClip(new Object[] {0.0, 4.0, Dm});
		ChordChunkList ccl = new ChordChunkList("4n", lc);
		ProgressionAnalyzer pa = new ProgressionAnalyzer(ccl);
//		System.out.println(pa.toString());
		assertEquals("imin", pa.getPrevailingCIKO(2.0).functionString);
	}
	
	
}
