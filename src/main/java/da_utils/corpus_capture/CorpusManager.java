package main.java.da_utils.corpus_capture;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;;

public class CorpusManager
{
	
	
	public static String defaultSavePath = "D:/Documents/algorithm data for googledrive/CorpusCapture2020/";
	private static HashMap<String, CorpusItem> corpusMap = new HashMap<String, CorpusItem>();
	private static boolean hasCorpusMap = false;

	
	
	public static CorpusItem getCorpusItem(String aSongName)
	{
//		iterateOverFilesInDefaultSavePath();
		if (!hasCorpusMap) corpusMap = makeCorpusMap();
		if (corpusMap.containsKey(aSongName))
		{
			return corpusMap.get(aSongName);
		}
		return null;
	}
	

	
	public static HashMap<String, CorpusItem> getCorpusMap()
	{
		return corpusMap;
	}



	private static HashMap<String, CorpusItem> makeCorpusMap()
	{
		HashMap<String, CorpusItem> map = new HashMap<String, CorpusItem>();
		ArrayList<File> fileList = new ArrayList<File>();
		File path = new File(defaultSavePath);
		compileListOfFiles(fileList, path);
		for (File file: fileList)
		{
			FileNameInfoObject fnio = new FileNameInfoObject(file.getName());
			if (!map.containsKey(fnio.getSongName())) map.put(fnio.getSongName(), new CorpusItem(fnio.getSongName()));
			switch (fnio.getFileType())
			{
			case LIVE_CLIP:	
				map.get(fnio.getSongName()).addLiveClip(fnio.getPartName(), getLiveClip(file));
				break;
			case METADATA:
				addMetaData(map.get(fnio.getSongName()), file);
			}
		}
		return map;
	}




	private static void addMetaData(CorpusItem aCorpusItem, File aMetaDataFile)
	{
		// TODO Auto-generated method stub
		
	}




	private static LiveClip getLiveClip(File aLiveClipFile)
	{
		try
		{
			LiveClip lc = new LiveClip(new BufferedReader(new FileReader(aLiveClipFile)));
			return lc;
		} 
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}




	private static void compileListOfFiles(ArrayList<File> theFileList, File aPath)
	{
		File[] files = aPath.listFiles();
		for (File file: files)
		{
			if (file.isDirectory())
			{
				compileListOfFiles(theFileList, file.getAbsoluteFile());
			}
			else
			{
				FileNameInfoObject fnio = new FileNameInfoObject(file.getName());
				if (fnio.getFileType() != FileNameInfoObject.FileType.NOT_USEFUL)
				{
					theFileList.add(file);
				}						
			}
		}		
	}




	private static void iterateOverFilesInDefaultSavePath()
	{
		File file = new File(defaultSavePath);
		File[] filePaths = file.listFiles();
		for (File path: filePaths)
		{
			
			if (path.isDirectory())
			{
				
			}
			System.out.println(getFileNameInfoObject(path.getName()).toString());
		}
	}




	private static FileNameInfoObject getFileNameInfoObject(String aFileName)
	{
		return new FileNameInfoObject(aFileName);
	}

}
