package main.java.da_utils.xml_maker.key;

import main.java.da_utils.combo_variables.IntAndString;

public class XMLKey {

	public String name;
	public int xmlKeyValue;
	public IntAndString[] accidental;
	
	public XMLKey(String name, int xmlKeyValue, IntAndString[] acc){
		this.name = name;
		this.xmlKeyValue = xmlKeyValue;
		this.accidental = acc;
	}
	
	public IntAndString getXMLNote(int note){
		return accidental[note % 12];
	}
	public int getOctave(int note){
		if (xmlKeyValue == -6 && note % 12 == 11){		// solves problem of Cb appearing an octave too low in key of Gb
			return note / 12 + 1;
		} else {
			return note / 12;
		}
		
	}
	public String toString(){
		return "name=" + name + " xmlKeyValue=" + xmlKeyValue;
	}
}
