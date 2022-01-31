package DataObjects.note_buffer;
//import ChordScaleDictionary.ChordScaleDictionary;

import main.java.da_utils.static_chord_scale_dictionary.CSD;

/*
 * wrapper for midi note data: note velocity and midi channel
 * 
 * this class will be wrapped in a TimedMidiNote class which would include 
 * playing position and length
 */
public class PlayedMidiNote {
	
	public int note;
	public int velocity;
	public int midiChannel;
	public int moduloNote;
	public int octave;
	public String noteName;
//	public ChordScaleDictionary csd = new ChordScaleDictionary();
	
	public PlayedMidiNote(int n, int v, int ch){
		note = n;
		velocity = v;
		midiChannel = ch;
		moduloNote = n % 12;
		octave = n / 12;
		noteName = CSD.noteName(moduloNote);
	}
	public String toString(){
		return "midiNote: note: " + note + ", vel: " + velocity + ", ch: " + midiChannel + "\n  " + noteName + " moduloNote: " + moduloNote + " octave: " + octave;
		
	}

}
