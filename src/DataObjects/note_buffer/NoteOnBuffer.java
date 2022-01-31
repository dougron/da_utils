package DataObjects.note_buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;




public class NoteOnBuffer {
	
	public ArrayList<PlayedMidiNote> noteBuffer = new ArrayList<PlayedMidiNote>();
	
	public static final int HOLD_MODE = 0;
	public static final int SUSTAIN_MODE = 1;
	
	private int mode = HOLD_MODE;	// 0 = hold_mode, 1 = sustain_mode
	
	public NoteOnBuffer(){
		
	}
	public void noteIn (int n, int v, int ch){
		if (v == 0){
			if (mode == 0){
				removeNoteFromBuffer(n);
			}			
		} else {
			addNoteToBuffer(n, v, ch);
		}
	}
	public void clear(){
		noteBuffer.clear();
	}
	public void setMode(int i){
		mode = i;
	}
	public void setMode(String s){
		if (s == "hold_mode"){
			mode = 0;
		} else if (s == "sustain_mode"){
			mode = 1;
		}
	}
	
	
	public String[] toStringArray(){
		String[] ts = new String[noteBuffer.size()];
		for (int i = 0; i < noteBuffer.size(); i++){
			ts[i] = noteBuffer.get(i).toString();
		}
		return ts;
	}
	
	public String toString(){
		String ts = "";
		for (int i = 0; i < noteBuffer.size(); i++){
			ts += noteBuffer.get(i).toString() + "\n";
		}
		return ts;
	}
	public Iterator<PlayedMidiNote> iterator(){
		return noteBuffer.iterator();
	}
	
	public int size(){
		return noteBuffer.size();
	}
	public MidiEvent getAsMev(int index){
		PlayedMidiNote p = noteBuffer.get(index);
		return new MidiEvent(0, p.note, p.velocity, p.midiChannel);
	}
	
	private void removeNoteFromBuffer(int note){
		for (int i = 0; i < noteBuffer.size(); i++){
			if (noteBuffer.get(i).note == note){
				noteBuffer.remove(i);
				break;
			}
		}
	}
	
	private void addNoteToBuffer(int n, int v, int ch){
		PlayedMidiNote midi = new PlayedMidiNote(n, v, ch);
		noteBuffer.add(midi);
	}

}
