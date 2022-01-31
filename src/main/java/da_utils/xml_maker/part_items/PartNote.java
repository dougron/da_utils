package main.java.da_utils.xml_maker.part_items;

import java.util.ArrayList;

import DataObjects.incomplete_note_utils.FinalListNote;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;

public class PartNote implements PartItem{
	
	private LiveMidiNote lmn;
	public static double quantValue = 0.25;			// default 1/16 quantize
	public static int type = Part.noteType;
	private FinalListNote fln;
	private int basicType;

	public PartNote(LiveMidiNote lmn){
		this.lmn = lmn;
		basicType = LIVE_MIDI_NOTE;
	}

	public PartNote(FinalListNote fln) {
		// this aspect still incomplete - not sure how do deal with quantum notes yet
		this.fln = fln;
		basicType = FINAL_LIST_NOTE;
	}

	@Override
	public double position() {
		double d;
		if (basicType == LIVE_MIDI_NOTE){
			d = lmn.position;
		} else if (basicType == FINAL_LIST_NOTE){
			d = fln.position();
		} else {
			d = 0.0;
		}
		
		d = d / quantValue;
		int i = (int)(d + 0.5);
		return (double)i * quantValue;
	}

	@Override
	public double length() {
		// returns quantized length
		double d;
		if (basicType == LIVE_MIDI_NOTE){
			d = lmn.length;
		} else if (basicType == FINAL_LIST_NOTE){
			d = fln.duration();
		} else {
			d = 0.0;
		}
		return (double)((int)(d / quantValue + 0.5)) * quantValue;
	}

	@Override
	public double actualPos() {
		if (basicType == LIVE_MIDI_NOTE){
			return lmn.position;
		} else if (basicType == FINAL_LIST_NOTE){
			return fln.position();
		} else {
			return 0.0;
		}
	}

	@Override
	public int note() {
		if (basicType == LIVE_MIDI_NOTE){
			return lmn.note;
		} else if (basicType == FINAL_LIST_NOTE){
			return fln.topNote();
		} else {
			return 0;
		}
	}

	@Override
	public int getType() {
		return type;
	}
	public String toString(){
		if (basicType == LIVE_MIDI_NOTE){
			return "PartNote: " + lmn.note + ", " + lmn.position + ", " + lmn.length;
		} else if (basicType == FINAL_LIST_NOTE){
			return "PartNote: " + fln.topNote() + ", " + fln.position() + ", " + fln.duration();
		} else {
			return "PartNote of as yet undefined basicType.";
		}
	}

	@Override
	public void addToStrList(ArrayList<String> strList, int indent) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void customSetting(String settingName, int setting) {
		// no custom settings as yet
		
	}

	@Override
	public void customSetting(String settingName, String setting) {
		// no custom settings as yet
		
	}
	
// basicTypes -------------------------------------------------------------
	private static final int LIVE_MIDI_NOTE = 0;
	private static final int FINAL_LIST_NOTE = 1;

	@Override
	public int articulation() {
		if (basicType == FINAL_LIST_NOTE){
			return fln.articulation();
		} else {
			return 0;
		}
		
	}



}
