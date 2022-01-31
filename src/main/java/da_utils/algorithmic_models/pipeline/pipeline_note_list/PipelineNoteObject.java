package main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list;
import java.util.ArrayList;
import java.util.Comparator;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.chord_progression_analyzer.ChordInKeyObject;
import main.java.da_utils.chord_progression_analyzer.chord_chunk.ChordChunk;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;



/*
 * wrapper for a data relating to a rhythm position in the PipeLineNoteList class
 */
public class PipelineNoteObject {
	
	public double position;
	
	public String descriptor = "null descriptor";
	public ChordAnalysisObject cao;
	public int contourValue;
	public double contourDoubleValue;				// for the melody generation plugins.....
	public ArrayList<LiveMidiNote> noteList = new ArrayList<LiveMidiNote>();
	public PipelineNoteObject pnoEmbellishing;			// this is the PipeleinNoteObject that this instance is embellishing. significant for rendering noteList.....
	public Embellishment embellishmentType;
	public int velocityModel = 0;				// 0 - all velocity the same (velocity variable), other models still to come
	public int velocity;
	public double legatoModel = 0.0;			// 0.0 = default_length anything elae is a percentage of the distnace to the next note
	
	private double default_length = 0.25;
	public double length = default_length;
	
	public boolean isGuideTone;
	public boolean isEmbellishable;
	public boolean isEndNote;
	public boolean notesAreUpToDate = false;
	public double precedingSpaceInNoteList = 0.0;

	public ChordInKeyObject ciko;

	public ChordChunk cc;

	
	public PipelineNoteObject(double pos, boolean guideTone, boolean embellish){
		position = pos;
		isGuideTone = guideTone;
		isEmbellishable = embellish;
	}
	public PipelineNoteObject(double pos, boolean guideTone, boolean embellish, String descrip){
		position = pos;
		isGuideTone = guideTone;
		isEmbellishable = embellish;
		descriptor = descrip;
	}
	public PipelineNoteObject(double pos, boolean guideTone, boolean embellish, PipelineNoteObject embellishing){
		position = pos;
		isGuideTone = guideTone;
		isEmbellishable = embellish;
		pnoEmbellishing = embellishing;
	}
	
	public static Comparator<PipelineNoteObject> positionComparator = new Comparator<PipelineNoteObject>(){
		public int compare(PipelineNoteObject note1, PipelineNoteObject note2){
			if (note1.position < note2.position) return -1;
			if (note1.position > note2.position) return 1;
			return 0;
		}
	};
	public String toString(){
		String ret = "PipeLineNoteObject: " + position + ", " + length + " isGuideTone: " + isGuideTone + " isEmbellishable: " + isEmbellishable + " " + descriptor + "\n";
		if (cao == null){
			ret = ret + "      no prevailing chord";
		} else {
			ret = ret + "       prevailing chord: " + cao.chordToString();
		}

		ret = ret + " contourValue: " + contourValue + " contourDoubleValue: " + contourDoubleValue + " velocity: " + velocity + "\n";
//		String l = " null length";
//		if (length != null) l = "" + length;
		ret = ret + "       legatoModel: " + legatoModel + " length: " + length + "\n";
		ret = ret + "       noteList: "; 
		for (LiveMidiNote lmn: noteList){
			ret = ret + lmn.toString() + ", ";
		}
		if (embellishmentType == null){
			ret = ret + " no embellishmentType";
		} else {
			ret = ret + " " + embellishmentType.name();
		}
		ret = ret + "\n       preceding space: " + precedingSpaceInNoteList + "\n";;
 
		return ret;
	}
	public String posAndLengthToString(){
		String ret = "PipeLineNoteObject: " + position + ", " + length + " isGuideTone: " + isGuideTone + " isEmbellishable: " + isEmbellishable + " " + descriptor + "\n";
		if (cao == null){
			ret = ret + "      no prevailing chord";
		} else {
			ret = ret + "       prevailing chord: " + cao.chordToString();
		}
		return ret;
	}
	public void addNote(int i){
		if (notARepeat(i)){
			noteList.add(new LiveMidiNote(i, position, default_length, 0, 0));
		}
		
	}
	public void addNote(ArrayList<Integer> nList){
		for (Integer i: nList){
			addNote(i);
		}
	}
	public void setFixedVelocity(int v){
		velocityModel = 0;
		velocity = v;
		for(LiveMidiNote lmn: noteList){
			lmn.velocity = velocity;
		}
	}
	public void setFixedLength(double legatoModel, double distance){
		this.legatoModel = legatoModel;
		length = distance;
		for (LiveMidiNote lmn: noteList){
			lmn.length = length;
		}		
	}
	public void setLengthsToLegatoModel(double legatoModel){
		this.legatoModel = legatoModel;
		setLengthsToLegatoModel();
	}
	public void setLengthsToLegatoModel(){
		double templength = default_length;
		if (legatoModel > 0.0){
			templength = length * legatoModel;
		}
		for (LiveMidiNote lmn: noteList){
			lmn.length = templength;
		}
	}
	public double averageNoteValue(){
		double d = 0.0;
		for (LiveMidiNote lmn: noteList){
			d += (double)lmn.note;
		}
		return d / noteList.size();
	}
	public void setPosition(double d){
		// changes position and updates items in the noteList as well
		position = d;
		for (LiveMidiNote lmn: noteList){
			lmn.position = d;
		}
	}
	public boolean isSameAs(PipelineNoteObject pno){
		// currently tests on the contents of the noteList
		if (noteList.size() != pno.noteList.size()){
			return false;
		} else {
			for (int i = 0; i < noteList.size(); i++){
				if (!noteList.get(i).isSameAs(pno.noteList.get(i))){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isSameAsIncludingVelocity(PipelineNoteObject pno){
		// currently tests on the contents of the noteList
		if (noteList.size() != pno.noteList.size()){
			return false;
		} else {
			for (int i = 0; i < noteList.size(); i++){
				if (!noteList.get(i).isSameAsIncludingVelocity(pno.noteList.get(i))){
					return false;
				}
			}
		}
		return true;
	}
// ---privates-------------------------------------------------------------------------------
	private boolean notARepeat(int i){
		for (LiveMidiNote lmn: noteList){
			if (lmn.note == i){
				return false;
			}
		}
		return true;
	}
}











