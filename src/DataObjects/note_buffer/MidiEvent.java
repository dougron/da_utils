package DataObjects.note_buffer;

import LegacyStuff.globals;

public class MidiEvent implements Cloneable{
	public int timeStamp;
	//public int channelNumber;
	public int midi1;
	public int midi2;
	public int midi3;
	public int timeStampOffset;			// a variable used for converting to AnalysisEvent
								// not meant to be persistently of interest
	public globals glob = new globals();
	
	public MidiEvent(int ts, int m1, int m2, int m3){
		//channelNumber = ch;
		timeStamp = ts;
		midi1 = m1;
		midi2 = m2;
		midi3 = m3;	
	}
	public MidiEvent(int[] mel){
		//channelNumber = mel[0];
		timeStamp = mel[0];
		midi1 = mel[1];
		midi2 = mel[2];
		midi3 = mel[3];	
	}
	public MidiEvent(MidiEvent mev){
		//channelNumber = mev.channelNumber;
		timeStamp = mev.timeStamp;
		midi1 = mev.midi1;
		midi2 = mev.midi2;
		midi3 = mev.midi3;
	}
	

	public String eventType(){
		if (midi1 > 127 && midi1 < 144){
			return "noteOff";
		} else {if (midi1 > 143 && midi1 < 160){
			return "noteOn";
		} else {if (midi1 == 176){
			if (midi2 == 123){
				return "allNotesOff";
			} else {
				return "controller";
			}
			
		} else {
			return "currently not on the list";
		}
		}
	}
	}
	
	public int[] barpos(){
		// returns int[3]={barNo,beatNo,TickNo}
		int bar = timeStamp / glob.bart();
		int beat = (timeStamp % glob.bart()) / glob.beatick();
		int tick = timeStamp % glob.beatick();
		int[] x = {bar,beat,tick};
		return x;
	}
	
	public void setTimeStampFromBarBeatTick(int bar, int beat, int tick){
		timeStamp = (glob.bart() * bar) + (glob.beatick() * beat) + tick;
	}
	
	public String toString(){
		return eventType() + ": " + timeStamp + " / " + midi1 + "/" + midi2 + "/" + midi3;
	}
	public String stringForTextFile(){
		return timeStamp + "," + midi1 + "," + midi2 + "," + midi3;
	}
	
	public int note(){
		// returns midi2, assuming this MidiEvent is a note
		return midi2;
	}
	public int velocity(){
		return midi3;	// as with note() assumoing this is a midi note
	}
	public void setNote(int note){
		midi2 = note;
	}
	public void setClock(int clock){
		timeStamp = clock;
	}
	
	public MidiEvent copy(){
		return new MidiEvent(timeStamp, midi1, midi2, midi3);
	}
	public MidiEvent copyTimeStampAdjust(int inc){
		return new MidiEvent(timeStamp + inc, midi1, midi2, midi3);
	}
	public MidiEvent selfWithOffset(int offset){
		timeStampOffset = offset;
		return this;
	}
	public MidiEvent clone(){
		try{
			return (MidiEvent)super.clone();
		} catch (CloneNotSupportedException e){
			return null;
		}
	}

}
