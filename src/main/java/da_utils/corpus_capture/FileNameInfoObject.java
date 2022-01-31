package main.java.da_utils.corpus_capture;

public class FileNameInfoObject
{

	public enum FileType {LIVE_CLIP, METADATA, NOT_USEFUL};
	private FileType fileType = FileType.NOT_USEFUL;
	private String songName;
	private String partName;
	private boolean hasPartName;

	public FileNameInfoObject(String aFileName)
	{
		String[] arr = aFileName.split("\\.", 0);
		
//		System.out.println(aFileName);
//		for (String str: arr) System.out.println(str);
		
		if (arr.length == 3 && arr[2].equals("liveclip")) 
		{
			fileType = FileType.LIVE_CLIP;
			songName = arr[0];
			partName = arr[1];
			hasPartName = true;
		}	
		else if (arr.length == 2 && arr[1].equals("metadata"))
		{
			fileType = FileType.METADATA;
			songName = arr[0];
		}
	}
	
	
	
	public String getSongName()
	{
		return songName;
	}



	public String getPartName()
	{
		if (hasPartName) return partName; else return "";
	}



	public FileType getFileType()
	{
		return fileType;
	}
	
	
	
	public String toString()
	{
		return "songName=" + songName + " " + getPartName() + " " + getFileType();
	}
	
}
