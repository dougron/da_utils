package main.java.da_utils.corpus_capture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;

class CorpusManager_Test
{

	@Test
	void when_selecting_item_by_name_then_returns_item_with_correct_name()
	{
		CorpusItem corpusItem = CorpusManager.getCorpusItem("Anthropology");
		for (CorpusItem ci: CorpusManager.getCorpusMap().values())
		{
			System.out.println(ci.toString());
		}
		assertEquals("Anthropology", corpusItem.getName());
	}
	
	
	@Test
	void when_selecting_item_by_name_then_returned_item_contains_valid_melody_liveclip_data() throws Exception
	{
		CorpusItem corpusItem = CorpusManager.getCorpusItem("Anthropology");
		LiveClip melody = corpusItem.getLiveClip("melody");
		assertTrue(melody instanceof LiveClip);
	}
	
	
	@Test
	void when_selecting_item_by_name_then_returned_item_returns_null_if_an_invalid_part_is_requested() throws Exception
	{
		CorpusItem corpusItem = CorpusManager.getCorpusItem("Anthropology");
		LiveClip melody = corpusItem.getLiveClip("poopy");
		assertTrue(melody == null);
	}

}
