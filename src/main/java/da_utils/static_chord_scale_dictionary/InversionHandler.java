package main.java.da_utils.static_chord_scale_dictionary;

public class InversionHandler {

	
	public String name;
	public String inversionName;
	public int inversionIndex = -1;
	public int rootNoteIndex;
	public boolean useNotePatternName = false;

	public InversionHandler(String name, int inversionIndex, int bassNoteIndex){
		this.name = name;
		this.inversionIndex = inversionIndex;
		this.rootNoteIndex = bassNoteIndex;
	}
	public InversionHandler(int inversionIndex, int bassNoteIndex){
		this.useNotePatternName = true;
		this.inversionIndex = inversionIndex;
		this.rootNoteIndex = bassNoteIndex;
	}
	public String inversionName(){
		if (inversionIndex == -1){
			return "irrelevant inversion";
		} else {
			return CSD.inversionName(inversionIndex)
;		}
	}
}
