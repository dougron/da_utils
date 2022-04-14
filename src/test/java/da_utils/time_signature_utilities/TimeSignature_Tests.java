package test.java.da_utils.time_signature_utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

class TimeSignature_Tests
{

	@Test
	final void test()
	{
		TimeSignature ts = TimeSignature.FOUR_FOUR;
		
		assertEquals(0, ts.getStrengthOfPositionInQuarters(0.0));
	}
	
	
	
	@Test
	void given_4_4_time_then_3rd_beat_is_strength_1() throws Exception
	{
		TimeSignature ts = TimeSignature.FOUR_FOUR;
		assertEquals(1, ts.getStrengthOfPositionInQuarters(2.0));
	}
	
	
	@Test
	void given_4_4_time_then_off_8th_is_strength_4() throws Exception
	{
		TimeSignature ts = TimeSignature.FOUR_FOUR;
		assertEquals(4, ts.getStrengthOfPositionInQuarters(0.5));
		assertEquals(4, ts.getStrengthOfPositionInQuarters(1.5));
		assertEquals(4, ts.getStrengthOfPositionInQuarters(2.5));
		assertEquals(4, ts.getStrengthOfPositionInQuarters(3.5));
	}
	
	
	@Test
	void given_4_4_time_then_off_16th_is_strength_8() throws Exception
	{
		TimeSignature ts = TimeSignature.FOUR_FOUR;
		assertEquals(8, ts.getStrengthOfPositionInQuarters(0.25));
		assertEquals(8, ts.getStrengthOfPositionInQuarters(0.75));
		assertEquals(8, ts.getStrengthOfPositionInQuarters(1.25));
		assertEquals(8, ts.getStrengthOfPositionInQuarters(1.75));
		assertEquals(8, ts.getStrengthOfPositionInQuarters(2.25));
		assertEquals(8, ts.getStrengthOfPositionInQuarters(2.75));
		assertEquals(8, ts.getStrengthOfPositionInQuarters(3.25));
		assertEquals(8, ts.getStrengthOfPositionInQuarters(3.75));
	}
	
	
	@Test
	void given_4_4_time_then_inaccurate_position_in_quarters_returns_strength_of_nearest_beat() throws Exception
	{
		TimeSignature ts = TimeSignature.FOUR_FOUR;
		assertEquals(0, ts.getStrengthOfPositionInQuarters(0.1));
		assertEquals(8, ts.getStrengthOfPositionInQuarters(0.2));
	}
	
	
	@Test
	void given_4_4_time_then_triplets_return_correct_strength() throws Exception
	{
		TimeSignature ts = TimeSignature.FOUR_FOUR;
		assertEquals(8, ts.getStrengthOfPositionInQuarters(0.33));
		assertEquals(4, ts.getStrengthOfPositionInQuarters(0.66));
	}

	
	@Test
	void given_7_8_time_then_all_beats_return_correct_strength() throws Exception
	{
		TimeSignature ts = TimeSignature.SEVEN_EIGHT_322;
		assertEquals(0, ts.getStrengthOfPositionInQuarters(0.0));
		assertEquals(4, ts.getStrengthOfPositionInQuarters(0.5));
		assertEquals(2, ts.getStrengthOfPositionInQuarters(1.0));
		assertEquals(0, ts.getStrengthOfPositionInQuarters(1.5));
		assertEquals(4, ts.getStrengthOfPositionInQuarters(2.0));
		assertEquals(2, ts.getStrengthOfPositionInQuarters(2.5));
		assertEquals(4, ts.getStrengthOfPositionInQuarters(3.0));
		
		assertEquals(8, ts.getStrengthOfPositionInQuarters(0.25));
		assertEquals(8, ts.getStrengthOfPositionInQuarters(0.75));
		assertEquals(8, ts.getStrengthOfPositionInQuarters(1.25));
		assertEquals(8, ts.getStrengthOfPositionInQuarters(1.75));
		assertEquals(8, ts.getStrengthOfPositionInQuarters(2.25));
		assertEquals(8, ts.getStrengthOfPositionInQuarters(2.275));
		assertEquals(8, ts.getStrengthOfPositionInQuarters(3.25));
	}


	@Test
	void superTactusAsQuarters() throws Exception
	{
		TimeSignature ts = TimeSignature.THIRTEEN_FOUR_CLAPHAM;
		System.out.println(ts.toLongString());
	}
	
	
	@Test
	void given_4_4_time_then_tactus_in_quarters_is_0_1_2_3() throws Exception
	{
		TimeSignature ts = TimeSignature.FOUR_FOUR;
		Double[] tactii = ts.getTactusAsQuartersPositions();
		assertEquals(4, tactii.length);
	}
	
	
	@Test
	void given_4_4_time_when_positions_in_quarters_is_1_then_correct_list_is_returned() throws Exception {
		TimeSignature ts = TimeSignature.FOUR_FOUR;
		Double[] ssTactii = ts.getQuartersPositionNBelowSubtactus(1);
		assertEquals(16, ssTactii.length);
	}
	
	
	@Test
	void given_4_4_time_when_positions_in_quarters_is_2_then_correct_list_is_returned() throws Exception {
		TimeSignature ts = TimeSignature.FOUR_FOUR;
		Double[] ssTactii = ts.getQuartersPositionNBelowSubtactus(2);
		assertEquals(32, ssTactii.length);
	}
	
	
	@Test
	void given_4_4_time_when_positions_in_quarters_is_3_then_correct_list_is_returned() throws Exception {
		TimeSignature ts = TimeSignature.FOUR_FOUR;
		Double[] ssTactii = ts.getQuartersPositionNBelowSubtactus(3);
		assertEquals(64, ssTactii.length);
	}
	
	
	@Test
	void given_7_8_time_when_positions_in_quarters_is_1_then_correct_list_is_returned() throws Exception {
		TimeSignature ts = TimeSignature.SEVEN_EIGHT_322;
		Double[] ssTactii = ts.getQuartersPositionNBelowSubtactus(1);
		assertEquals(14, ssTactii.length);
	}
	
	
	@Test
	void given_7_8_time_when_positions_in_quarters_is_2_then_correct_list_is_returned() throws Exception {
		TimeSignature ts = TimeSignature.SEVEN_EIGHT_322;
		Double[] ssTactii = ts.getQuartersPositionNBelowSubtactus(2);
		assertEquals(28, ssTactii.length);
	}

}
