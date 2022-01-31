package DataObjects.incomplete_note_utils;


/*
 * Class to wrap annotation information in the FinalListNote for use when rendering 
 * musicXML files with MusicXMLMaker
 * 
 */
public class FinalListNoteAnnotation {

	
	private String text;
	private int xOffset;
	private boolean hasXOffset;

	public FinalListNoteAnnotation(String str){
		this.text = str;
	}
	public FinalListNoteAnnotation(String str, int xOffset){
		this.text = str;
		this.xOffset = xOffset;
		this.hasXOffset = true;
	}
	public String text(){
		return text;
	}
	public boolean hasXOffset(){
		return hasXOffset;
	}
	public int xOffset(){
		return xOffset;
	}
}
