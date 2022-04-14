package main.java.da_utils.static_chord_scale_dictionary;

import java.util.List;

/*
 * asimilar to the ChordAnalysisObject, but using the StaticChordScaleDictionary
 * as its repository of knowledge. Partners with a ScaleAnalysis class....
 * as canbe seen this was never completed, its role taken by the DataObjects.ChordChunk
 */
public class ChordAnalysis {
	
	
	private List<Integer> originalList;

	
	public ChordAnalysis(List<Integer> noteList)
	{
		this.originalList = noteList;
	}
}
