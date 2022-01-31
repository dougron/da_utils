package main.java.da_utils.xml_maker.clef;

import main.java.da_utils.combo_variables.IntAndString;

public class XMLClef {

	public IntAndString data;
	
	public XMLClef(String clef, int line){
		data = new IntAndString(line, clef);
	}
	public String clef(){
		return data.str;
	}
	public String lineAsString(){
		return Integer.toString(data.i);
	}
}
