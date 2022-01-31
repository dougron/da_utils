package DataObjects.incomplete_note_utils;
import java.util.ArrayList;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.resource_objects.ChordForm;



/*
 * note object used in the repetition analysis algorithm. Has all sorts of possibilities 
 * like being incomplete in terms of notes and velocity but can still generate an
 * understandable score as to the state of incompleteness
 */
public class FinalListNote {
	
	
	
	private ArrayList<Integer> noteList = new ArrayList<Integer>();
	private double duration = 0.0;
	private int velocity = 0;
	private double position;
	private double durationModel = 0.0;		// range 0.0 - 1.0, staccato = 0.0, legato = 1.0, tenuto anywhere in between
	private String default_staccato = "8n";	// default length of staccato
	private boolean hasDurationModel = false;	// only uses durationModel if this is true.
	private FinalListNote nextNote = null;
	private int pitchFeatureID = -1;  // no such setting
	private FinalListNote previousNote = null;
	private Object pci;
	private ArrayList<FinalListNoteAnnotation> annotationList = new ArrayList<FinalListNoteAnnotation>();
	private int articulation = NO_ARTICULATION;
	
	public FinalListNote(double pos){
		position = pos;
	}
	public boolean hasNotes(){
		if (noteList.size() > 0){
			return true;
		} else {
			return false;
		}
	}
	public boolean hasDuration(){
		if (duration == 0.0){
			return false;
		} else {
			return true;
		}
	}
	public boolean hasDurationModel(){
		return hasDurationModel;
	}
	
	public boolean hasVelocity(){
		if (velocity == 0){
			return false;
		} else {
			return true;
		}
	}
	public boolean isComplete(){
		if (hasNotes() && hasDuration() && hasVelocity()){
			return true;
		} else {
			return false;
		}
	}
	public ArrayList<LiveMidiNote> getNoteList(){
		ArrayList<LiveMidiNote> lmnList = new ArrayList<LiveMidiNote>();
		for (Integer note: noteList){
			lmnList.add(new LiveMidiNote((int)note, position, duration, velocity, 0));
		}
		return lmnList;
	}
	public String toString(){
		String str = " FinalListNote pos=" + position + " notes:- ";
		if (noteList.size() == 0){
			str += "no notes";
		} else {
			for (int i: noteList){
				str += i + ",";
			}
		}
		str += " velocity=" + velocity;
		str += " duration=" + duration;
		str += " durationModel=" + durationModel + " hasDurModel=" + hasDurationModel;
		if (nextNote == null){
			str += " nextNote not set.";
		} else {
			str += " nextNote pos=" + nextNote.position();
		}
		if (previousNote == null){
			str += " previousNote not set";
		} else {
			str += " previousNote pos=" + previousNote.position();
		}
		return str;
		
	}
	public double position() {

		return position;
	}
//	public double length(){
//		return duration;
//	}
	public double duration(){
		return duration;
	}
	public double endPosition(){
		return position + duration;
	}
	public int topNote(){
		if (noteList.size() == 0){
			return 0;
		} else {
			return noteList.get(noteList.size() - 1);
		}
		
	}
	public double durationModel(){
		return durationModel;
	}
	public void addNote(int n){
		noteList.add(n);
	}
	public void setVelocity(int i) {
		velocity = i;
		
	}
	public void setDuration(double dur) {
		duration = dur;
		
	}
	public void setDurationModel(double d){
		durationModel = d;
		hasDurationModel = true;
	}
	
	public void setNextNote(FinalListNote fln){
		nextNote = fln;
	}
	public FinalListNote next(){
		return nextNote;
	}
	
	public void setPitchFeatureID(int featureID) {
		pitchFeatureID  = featureID;
		
	}
	public int pitchFeatureID(){
		return pitchFeatureID;
	}
	public void setPrevious(FinalListNote previousFLN) {
		previousNote  = previousFLN;
		
	}
	public FinalListNote previous(){
		return previousNote;
	}
	public void clearNoteList(){
		noteList.clear();
	}
	public void setPitchConstructor(Object pci) {
		this.pci = pci;
		
	}
	public Object getPitchConstructor(){
		return pci;
	}
	public void setArticulation(int i){
		articulation = i;
	}
	public int articulation(){
		return articulation;
	}
	public void addAnnotation(String str){
		annotationList.add(new FinalListNoteAnnotation(str));
	}
	public void addAnnotation(String str, int xOffset){
		annotationList.add(new FinalListNoteAnnotation(str, xOffset));
	}
	public ArrayList<FinalListNoteAnnotation> annotationList(){
		return annotationList;
	}
	public int velocity(){
		return velocity;
	}
	
	public static final int NO_ARTICULATION = 0;
	public static final int STACCATO_ARTICULATION = 1;
	public static final int ACCENT_ARTICULATION = 2;
	public static final int PORTATO_ARTICULATION = 3;
	public static final int TENUTO_ARTICULATION = 4;
	public static final int FERMATA_ARTICULATION = 5;
	public static final int MARCATO_ARTICULATION = 5;

	public void changePositionToOutputFormRange(ChordForm outputCF) {
		while (position < 0) position += outputCF.totalLength();
		while (position > outputCF.totalLength()) position -= outputCF.totalLength();
		
	}

	
	
}
