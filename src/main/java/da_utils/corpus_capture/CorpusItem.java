package main.java.da_utils.corpus_capture;
import java.util.HashMap;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;


public class CorpusItem
{
	
	public static int DEFAULT_NOTE_FOR_PHRASE_START = 24;
	
	
	private String songName;
	private HashMap<String, LiveClip> clipMap = new HashMap<String, LiveClip>();
	private boolean hasMetaData;
	

	public CorpusItem(String aSongName)
	{
		songName = aSongName;
	}
	
	
	
	public String getName()
	{
		return songName;
	}



	public HashMap<String, LiveClip> getClipMap()
	{
		return clipMap;
	}



	public void addLiveClip(String aPartName, LiveClip aLiveClip)
	{
		clipMap.put(aPartName, aLiveClip);
	}
	
	
	
	public LiveClip getLiveClip(String aPartName)
	{
		if (clipMap.containsKey(aPartName)) return clipMap.get(aPartName);
		return null;
	}
	
	
	
	public boolean containsPart(String aPartName)
	{
		return clipMap.containsKey(aPartName);
	}
	
	
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("CorpusItem:" + songName);
		for (String key: clipMap.keySet())
		{
			sb.append("\n   " + key);
		}
		sb.append("\n   hasMetaData=" + hasMetaData);
		return sb.toString();
	}



	
}
