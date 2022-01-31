package main.java.da_utils.ableton_live.ableton_live_clip;
import java.util.Comparator;

import DataObjects.note_buffer.PlayedMidiNote;
import main.java.da_utils.static_chord_scale_dictionary.CSD;




public class LiveMidiNote {
	
	public int note;
	public double position;
	public double length;
	public int velocity;
	public int mute;
	
	public double positionOffset;
	public double lengthOffset;
	
	
	
	public LiveMidiNote(int n, double p, double l, int v, int m)
	{
		note = n;
		position = p;
		length = l;
		velocity = v;
		mute = m;
	}
	
	
	
	// this instantiator assumes that pos and len are unquantized and will assign quantised values
	// to position and length while retaining offset values
	// mute is currently assumed to be 0 as there is no reason for anything else
	public LiveMidiNote(int n, double p, double l, int v, String quant)
	{		
		note = n;
		position = makeQuantizedPosition(p, quant);
		length = makeQuantizedPosition(l, quant);
		velocity = v;
		mute = 0;			// unmuted note by default, cos i've never used a muted note as of 9 March 2015
	}
	
	
	
	public LiveMidiNote clone()
	{
		return new LiveMidiNote(note, position, length, velocity, mute);
	}
	
	
	
	public LiveMidiNote quantizedClone(String rez)
	{
		return new LiveMidiNote(
				note,
				makeQuantizedPosition(position, rez),
				makeQuantizedPosition(length, rez),
				velocity,
				mute);
	}
	
	
	
	public String toString()
	{
		String ret = "note " + note + " " + position + " " + length + " " + velocity + " " + mute;
		ret = ret + " " + CSD.noteName(note) + (note / 12 - 2);
		return ret;
	}
	
	
	
	public String absoluteNoteName()
	{
		return CSD.noteName(note) + (note / 12 - 2);
	}
	
	
	
	public String toLOMString()
	{
		return "" + note + " " + position + " " + length + " " + velocity + " " + mute;
	}
	
	
	
	// returns a PlayedMidiNote version of the note data. mostly for debugging....
	// third argument magic number is the channel parameter which for Ableton always ends as 1
	public PlayedMidiNote getPlayedMidiNote()
	{		
		return new PlayedMidiNote(note, velocity, 1);	
	}
	
	
	
	public static Comparator<LiveMidiNote> positionComparator = new Comparator<LiveMidiNote>()
	{
		public int compare(LiveMidiNote note1, LiveMidiNote note2)
		{
			if (note1.position < note2.position) return -1;
			if (note1.position > note2.position) return 1;
			return 0;
		}
	};
	
	
	
	public static Comparator<LiveMidiNote> posAndNoteComparator = new Comparator<LiveMidiNote>()
	{
		public int compare(LiveMidiNote note1, LiveMidiNote note2)
		{
			if (note1.position < note2.position) return -1;
			if (note1.position > note2.position) return 1;
			if (note1.note < note2.note) return -1;
			if (note1.note > note2.note) return 1;
			return 0;
		}
	};
	
	
	
	public boolean isSameAs(LiveMidiNote lmn)
	{
		// tests for similarity. considers note, position and length, NOT velocity

		if (note == lmn.note && position == lmn.position && length == lmn.length)
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}
	
	
	
	// tests for similarity. considers note, position, length and velocity
	// added velocity consideration for 2019 SelektaAnalysis to solve the paucity of hihat options in SortaSelekta
	// this may break something else
	public boolean isSameAsIncludingVelocity(LiveMidiNote lmn)
	{		
		if (note == lmn.note && position == lmn.position && length == lmn.length && velocity == lmn.velocity )
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}

	
	public double makeQuantizedPosition(double p, String quant)
	{
		int quantick = CSD.qmodelTickValue(quant);
		double quantDouble = quantick / 480.0;			// 480: the way to convert a tick value to a pos value 1 quarter = 480 ticks = 1.0 in terms of pos
		double zz = p + (quantDouble / 2.0);
		int x = (int)(zz / quantDouble);
		double y = x * quantDouble;
		return y;
	}
	
	
	
	public double quantizedPosition(String quant)
	{
		return makeQuantizedPosition(position, quant);
	}
	
	
	
	public double quantizedEndPoint(String quant)
	{
		// position of end point on timeline
		return makeQuantizedPosition(position + length, quant);
	}
	
	
	
	public String oneLineToString() 
	{
		// note, modnote, octave, position, length, velocity
		String str = "LiveMidiNote," + note + "," + note % 12 + "," + note/12 + "," + position + "," + length + "," + velocity;
		return str;
	}
	
	
	
	public static final LiveMidiNote NULL_NOTE = new LiveMidiNote(-1, 0, 0, 0, 0);
}
