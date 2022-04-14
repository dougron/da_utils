package main.java.da_utils.static_chord_scale_dictionary;

import java.util.ArrayList;

public class CSDTestCase 
{

	
	public String name;
	public ArrayList<Integer> noteList;
	private int[] noteArr;

	
	
	public CSDTestCase(String name, int[] noteArr)
	{
		this.name = name;
		this.noteArr = noteArr;
		noteList = new ArrayList<Integer>();
		for(int i: noteArr)
		{
			noteList.add(i);
		}
	}

	
	
	public CSDTestCase(String name, ArrayList<Integer> noteList) 
	{
		this.name = name;
		this.noteList = noteList;
	}
	
	
	
	public String noteListToString()
	{
		String str = "";
		for (Integer i: noteList)
		{
			str += i + ", ";
		}
		return str;
	}
}
