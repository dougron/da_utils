package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;
/*
 * Embellishment descriptor. short name to try and keep code dynamic. 
 * Describes:
 * sx	-	semitone embellishment, integer x discribing difference to the note to be embellished
 * dx	-	diatonic embellishment int x
 * cx	-	chord tone embellishment int x
 */
		
public class ED {
	
	public String type;
	public int value;
	
	public ED(String type, int value){
		this.type = type;
		this.value = value;
	}
	
	public String toString(){
		return "ED: " + type + " " + value;
	}

	public boolean isSameAs(ED ed) {
		if (ed.type.equals(type) && ed.value == value){
			return true;
		}
		return false;
	}

}
