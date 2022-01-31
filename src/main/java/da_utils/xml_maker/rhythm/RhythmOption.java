package main.java.da_utils.xml_maker.rhythm;

import main.java.da_utils.xml_maker.note.XMLNoteAppearance;

/*
 * a class to wrap data which helps to format notes into an acceptable appearance in terms of how to tie notes together
 */
public class RhythmOption {

	
	
	public double start;
	public double length;
	public XMLNoteAppearance appearance;

	public RhythmOption(double start, double length, XMLNoteAppearance xna){
		this.start = start;
		this.length = length;
		this.appearance = xna;
	}
	public String toString(){
		String ret = "RhythmOption: start=" + start + " length=" + length + " " + appearance.toString();
		return ret;
	}
}
