package test.java.da_utils.static_chord_scale_dictionary;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import main.java.da_utils.static_chord_scale_dictionary.CSD;
import main.java.da_utils.static_chord_scale_dictionary.NotePatternAnalysis;

class CSDChordTone_Tests
{
	private static final Integer[] cMinor = new Integer[] {60, 63, 67};
	private static final Integer[] cMajor6 = new Integer[] {60, 64, 67, 69};
	private static final Integer[] cMajor7 = new Integer[] {60, 64, 67, 71};
	private static final Integer[] cMajor9 = new Integer[] {60, 64, 67, 71, 62};
	private static final Integer[] cMinor9 = new Integer[] {60, 63, 67, 70, 62};
	private static final Integer[] cMinor11 = new Integer[] {60, 63, 67, 70, 62, 65};

	@Test
	void when_chord_tones_of_C_minor_are_passed_then_one_option_for_C_minor_returned()
	{
		List<NotePatternAnalysis> 
		chordOptions 
		= CSD.getChordOptions(Arrays.asList(cMinor));
		
		assertThat(chordOptions.size()).isEqualTo(1);
		assertThat(chordOptions.get(0).chordSymbolToString()).isEqualTo("Cmin");
	}
	
	
	@Test
	void when_chord_tones_of_C_major6_are_passed_then_two_option_for_Cmaj6_andAm7_returned()
	{
		List<NotePatternAnalysis> 
		chordOptions 
		= CSD.getChordOptions(Arrays.asList(cMajor6));
		
		assertThat(chordOptions.size()).isEqualTo(2);
		assertThat(chordOptions.get(0).chordSymbolToString()).isEqualTo("Am7");
		assertThat(chordOptions.get(1).chordSymbolToString()).isEqualTo("CMaj6");
	}
	
	
	@Test
	void when_chord_tones_of_C_major7_are_passed_then_one_option_for_Cmaj7_returned()
	{
		List<NotePatternAnalysis> 
		chordOptions 
		= CSD.getChordOptions(Arrays.asList(cMajor7));
		
		assertThat(chordOptions.size()).isEqualTo(1);
		assertThat(chordOptions.get(0).chordSymbolToString()).isEqualTo("CMaj7");
	}
	
	
	@Test
	void when_chord_tones_of_C_major9_are_passed_then_one_option_for_Cmaj9_returned()
	{
		List<NotePatternAnalysis> 
		chordOptions 
		= CSD.getChordOptions(Arrays.asList(cMajor9));
		
		assertThat(chordOptions.size()).isEqualTo(1);
		assertThat(chordOptions.get(0).chordSymbolToString()).isEqualTo("CMaj9");
	}
	
	
	@Test
	void when_chord_tones_of_C_minor9_are_passed_then_one_option_for_Cmin9_returned()
	{
		List<NotePatternAnalysis> 
		chordOptions 
		= CSD.getChordOptions(Arrays.asList(cMinor9));
		
		assertThat(chordOptions.size()).isEqualTo(1);
		assertThat(chordOptions.get(0).chordSymbolToString()).isEqualTo("Cm9");
	}
	
	
	@Test
	void when_chord_tones_of_C_minor11_are_passed_then_one_option_for_Cmin11_returned()
	{
		List<NotePatternAnalysis> 
		chordOptions 
		= CSD.getChordOptions(Arrays.asList(cMinor11));
		
		assertThat(chordOptions.size()).isEqualTo(1);
		assertThat(chordOptions.get(0).chordSymbolToString()).isEqualTo("Cm11");
	}
	
	


}
