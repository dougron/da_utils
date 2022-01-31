package main.java.da_utils.xml_maker.voice_item;

import java.util.ArrayList;

import main.java.da_utils.combo_variables.IntAndString;
import main.java.da_utils.combo_variables.StringPair;
import main.java.da_utils.xml_maker.MXM;
import main.java.da_utils.xml_maker.key.XMLKey;
import main.java.da_utils.xml_maker.note.XMLNoteAppearance;
import main.java.da_utils.xml_maker.part_items.Part;
import main.java.da_utils.xml_maker.part_items.PartItem;
import main.java.da_utils.xml_maker.time_signature.XMLTimeSignatureZone;

public class VoiceItem {

	private double start;
	private double end;
	public ArrayList<PartItem> itemList = new ArrayList<PartItem>();
	public boolean isRest = false;
	public int voiceIndex = 0;
	public boolean hasEndTie = false;
	public boolean hasStartTie = false;
	public XMLNoteAppearance appearance;
	public double quantValue = 0.25;				// default quantize to nearest 16th
	public boolean startBeam = false;
	public boolean endBeam = false;
	public boolean continueBeam = false;
	
	
	public VoiceItem(double start, double end, PartItem pi, int i){
		this.voiceIndex = i;
		this.start = start;
		this.end = end;
		itemList.add(pi);
	}
	public VoiceItem(double start, double end, int i){
		this.voiceIndex = i;
		this.start = start;
		this.end = end;
		isRest = true;
	}
	public VoiceItem(VoiceItem vi){
		// makes deepycopy
		this.start = vi.start;
		this.end = vi.end;
		this.isRest = vi.isRest;
		this.voiceIndex = vi.voiceIndex;
		this.hasEndTie = vi.hasEndTie;
		this.hasStartTie = vi.hasStartTie;
		for (PartItem pi: vi.itemList){
			itemList.add(pi);
		}
		this.startBeam = vi.startBeam;
		this.endBeam = vi.endBeam;
		
	}
	public void addPartItem(PartItem pi) {
		itemList.add(pi);
		
	}
	public String toString(){
		String ret = "VoiceItem start=" + start() + " end=" + end() + " voice=" + voiceIndex  + "\n";
		if (appearance != null){
			ret += appearance.toString();
		}
		ret += "\n";
		if (isRest){
			ret += "    isRest";
		} else {
			for (PartItem pi: itemList){
				ret += "    " + pi.toString();
			}
		}
		return ret;
	}
	public double length(){
		return end() - start();
	}
	public double start(){
		return ((double)((int)(start / quantValue + 0.5))) * quantValue;
	}
	public double end(){
		return ((double)((int)(end / quantValue + 0.5))) * quantValue;
	}
	public void setStart(double d){
		this.start = d;
	}
	public void setEnd(double d){
		this.end = d;
	}
	public void addVoiceItemContent(ArrayList<String> strList, int indent, int divisions, XMLKey xmlKey, XMLTimeSignatureZone tsz) {
		addDirections(strList, indent);		
		addBarLines(strList, indent);
		if (isRest){
			addNoteHeader(strList, indent);
			addRestEntries(strList, indent + MXM.indentIncrement, divisions, tsz);
			addNoteFooter(strList, indent);
		} else {
			boolean addChordIndication = false;
			boolean addArticulation = true;
			for (PartItem pi: itemList){
				if (pi.getType() == Part.noteType){
					addNoteHeader(strList, indent);
					if (addChordIndication) addChordIndication(strList, indent + MXM.indentIncrement);
					addChordIndication = true;
					addNoteEntries(strList, indent + MXM.indentIncrement, divisions, pi, xmlKey, addArticulation);
					addArticulation = false;
					addNoteFooter(strList, indent);
				}
				
			}
		}
		
	}
	private void addBarLines(ArrayList<String> strList, int indent) {
		for (PartItem pi: itemList){
			if (pi.getType() == Part.barLineType){
				pi.addToStrList(strList, indent);
			}
		}
		
	}
	private void addDirections(ArrayList<String> strList, int indent) {
		for (PartItem pi: itemList){
			if (pi.getType() == Part.directionType){
				pi.addToStrList(strList, indent);
			}
		}
		
	}
	private void addNoteFooter(ArrayList<String> strList, int indent) {
		MXM.addSingleEntry(strList, MXM.NOTE, indent + MXM.indentIncrement, true);
		
	}
	private void addNoteHeader(ArrayList<String> strList, int indent) {
		MXM.addSingleEntry(strList, MXM.NOTE, indent, false);
		
	}
	private void addNoteEntries(ArrayList<String> strList, int indent, int divisions, PartItem pi, XMLKey xmlKey, boolean addArticulation) {
		addPitch(strList, indent, divisions, pi, xmlKey);
		addDuration(strList, indent, divisions);
		addTie(strList, indent, pi);
		addVoice(strList, indent);
		appearance.addTypeToStringList(strList, indent);
		appearance.addDotCountToStringList(strList, indent);
//		addStem(strList, indent, pi);				// leaving this off for now
		addBeam(strList, indent);
		addNotations(strList, indent, pi, addArticulation);
	}

	private void addBeam(ArrayList<String> strList, int indent) {
		if (startBeam){
			MXM.addSingleEntryWithAttributesAndValue(
					strList, 
					MXM.BEAM, 
					new StringPair[]{new StringPair(MXM.NUMBER, "1")}, 
					MXM.BEGIN, indent + MXM.indentIncrement);
		}
		if (endBeam){
			MXM.addSingleEntryWithAttributesAndValue(
					strList, 
					MXM.BEAM, 
					new StringPair[]{new StringPair(MXM.NUMBER, "1")}, 
					MXM.END, indent + MXM.indentIncrement);
		}
		if (continueBeam){
			MXM.addSingleEntryWithAttributesAndValue(
					strList, 
					MXM.BEAM, 
					new StringPair[]{new StringPair(MXM.NUMBER, "1")}, 
					MXM.CONTINUE, indent + MXM.indentIncrement);
		}
	}
	private void addNotations(ArrayList<String> strList, int indent, PartItem pi, boolean addArticulation) {
		if (hasStartTie || hasEndTie || (pi.articulation() > 0 && addArticulation)){
			MXM.addSingleEntry(strList, MXM.NOTATIONS, indent, false);
			if (hasEndTie){
				MXM.addSingleEntryWithAttributesAndEndSlash(strList, MXM.TIED, 
						new String[][]{new String[]{MXM.TYPE, MXM.STOP}}, indent + MXM.indentIncrement);
			}
			if (hasStartTie){
				MXM.addSingleEntryWithAttributesAndEndSlash(strList, MXM.TIED, 
						new String[][]{new String[]{MXM.TYPE, MXM.START}}, indent + MXM.indentIncrement);
			}
			if (pi.articulation() > 0 && addArticulation && hasEndTie == false){
				MXM.addArticulationTypeEntry(strList, MXM.ARTICULATIONS, MXM.articluationArray[pi.articulation()], indent + MXM.indentIncrement);
			}
			
			MXM.addSingleEntry(strList, MXM.NOTATIONS, indent + MXM.indentIncrement, true);
		}
		
	}
	private void addTie(ArrayList<String> strList, int indent, PartItem pi) {
		if (hasEndTie){
			MXM.addSingleEntryWithAttributesAndEndSlash(strList, MXM.TIE, 
					new String[][]{new String[]{MXM.TYPE, MXM.STOP}}, indent);
		}
		if (hasStartTie){
			MXM.addSingleEntryWithAttributesAndEndSlash(strList, MXM.TIE, 
					new String[][]{new String[]{MXM.TYPE, MXM.START}}, indent);
		}
		
		
	}
	private void addPitch(ArrayList<String> strList, int indent, int divisions, PartItem pi, XMLKey xmlKey) {
//		System.out.println(xmlKey.toString());
		IntAndString noteData = xmlKey.getXMLNote(pi.note());
		MXM.makeQuickEntry(
				strList, 
				MXM.PITCH, 
				new String[][]{
					new String[]{MXM.STEP, noteData.str},
					new String[]{MXM.ALTER, Integer.toString(noteData.i)},
					new String[]{MXM.OCTAVE, Integer.toString(xmlKey.getOctave(pi.note()))}
				}, 
				new String[][]{}, 	
				indent);
		
	}
	private void addRestEntries(ArrayList<String> strList, int indent, int divisions, XMLTimeSignatureZone tsz) {
		if (length() < tsz.lengthInQuarters()){
			MXM.addSingleEntryWithEndSlash(strList, MXM.REST, indent);
		} else {
			MXM.addSingleEntryWithAttributesAndEndSlash(
					strList, 
					MXM.REST,
					new String[][]{
						new String[]{MXM.MEASURE, MXM.YES}					
					},
					indent);
		}
		
		addDuration(strList, indent, divisions);
		appearance.addDotCountToStringList(strList, indent);
		addVoice(strList, indent);
		if (length() < tsz.lengthInQuarters()){
			// leave out the <type> indication for whole bar rests allows them to appear centred in musescore
			appearance.addTypeToStringList(strList, indent);
		}
		
	}
	private void addVoice(ArrayList<String> strList, int indent) {
		MXM.addOneLiner(strList, MXM.VOICE, Integer.toString(voiceIndex), indent);
		
	}
	private void addDuration(ArrayList<String> strList, int indent, int divisions) {
		int duration = (int)(length() * divisions);
		MXM.addOneLiner(strList, MXM.DURATION, Integer.toString(duration), indent);
		
	}
	private void addChordIndication(ArrayList<String> strList, int indent) {
		MXM.addSingleEntry(strList, MXM.CHORD + MXM.BS, indent, false);		
	}
	public boolean isOnStartEighth() {
		if (MXM.closeEnough(start % 1.0, 0.0, 0.01)){
			return true;
		} else {
			return false;
		}
	}
	public boolean isEighthLength(){
		if (MXM.closeEnough(end - start, 0.5, 0.01)){
			return true;
		} else {
			return false;
		}
	}
	
	
}
