package main.java.da_utils.xml_maker;

import java.util.ArrayList;

public class MusicXMLObject {

	public ArrayList<MusicXMLObject> mxList = new ArrayList<MusicXMLObject>();
	public String name;
	public int tab = 0;
	
	public MusicXMLObject(String str, int tab){
		name = str;
		this.tab = tab;
	}
	
	
	
	
	
}
