package test.java.da_utils.static_chord_scale_dictionary;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.BooleanSupplier;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.java.da_utils.static_chord_scale_dictionary.CSD;

class CSDChordSymbol_Tests
{

		
	@Nested
	class given_chord_symbol
	{
		@Test
		void _C_then_getChordTones_returns_correct_chord_tones() throws Exception
		{
			int[] chordTones = CSD.getChordTones("C");
			assertEquals(3, chordTones.length);
			assertTrue(containsItem(chordTones, 0));
			assertTrue(containsItem(chordTones, 4));
			assertTrue(containsItem(chordTones, 7));
			assertFalse(containsItem(chordTones, 1));
			assertFalse(containsItem(chordTones, 2));
			assertFalse(containsItem(chordTones, 3));
			assertFalse(containsItem(chordTones, 5));
			assertFalse(containsItem(chordTones, 6));
			assertFalse(containsItem(chordTones, 8));
			assertFalse(containsItem(chordTones, 9));
			assertFalse(containsItem(chordTones, 10));
			assertFalse(containsItem(chordTones, 11));
		}


		@Test
		void _CMaj7_then_getChordTones_returns_correct_chord_tones() throws Exception
		{
			int[] chordTones = CSD.getChordTones("CMaj7");
			assertEquals(4, chordTones.length);
			assertTrue(containsItem(chordTones, 0));
			assertTrue(containsItem(chordTones, 4));
			assertTrue(containsItem(chordTones, 7));
			assertTrue(containsItem(chordTones, 11));
			assertFalse(containsItem(chordTones, 1));
			assertFalse(containsItem(chordTones, 2));
			assertFalse(containsItem(chordTones, 3));
			assertFalse(containsItem(chordTones, 5));
			assertFalse(containsItem(chordTones, 6));
			assertFalse(containsItem(chordTones, 8));
			assertFalse(containsItem(chordTones, 9));
			assertFalse(containsItem(chordTones, 10));
		}
		
		
		@Test
		void _C_then_getChordTones_returns_chord_tones_sorted_from_root() throws Exception
		{
			int[] chordTones = CSD.getChordTones("C");
			assertEquals(0, chordTones[0]);
			assertEquals(4, chordTones[1]);
			assertEquals(7, chordTones[2]);
		}
		
		
		@Test
		void _G_then_getChordTones_returns_chord_tones_sorted_from_root() throws Exception
		{
			int[] chordTones = CSD.getChordTones("G");
			assertEquals(7, chordTones[0]);
			assertEquals(11, chordTones[1]);
			assertEquals(2, chordTones[2]);
		}
		
		
		@Test
		void _Am9_then_getChordTones_returns_chord_tones_sorted_from_root () throws Exception
		{
			// not actually in order, as 9th sits between root and 3rd
			int[] chordTones = CSD.getChordTones("Am9");
			assertEquals(9, chordTones[0]);
			assertEquals(11, chordTones[1]);
			assertEquals(0, chordTones[2]);
			assertEquals(4, chordTones[3]);
			assertEquals(7, chordTones[4]);

		}
	}
	
	
	
	
// privates  ------------------------------------------------------------------------------
	
	
	private boolean containsItem(int[] arr, int i)
	{
		for (int x: arr) if (x == i) return true;
		return false;
	}

}
