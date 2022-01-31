package main.java.da_utils.algorithmic_models.melody_segmenter;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.static_chord_scale_dictionary.CSD;

public class PhantomNote extends LiveMidiNote {

	public PhantomNote(LiveMidiNote lmn, double offset){
		super(lmn.note, lmn.position - offset, lmn.length, lmn.velocity, lmn.mute);
	}
	public String toString(){
		String ret = "PhantomNote: note " + note + " " + position + " " + length + " " + velocity + " " + mute;
		ret = ret + " " + CSD.noteName(note) + (note / 12 - 2);
		return ret;
	}
}
