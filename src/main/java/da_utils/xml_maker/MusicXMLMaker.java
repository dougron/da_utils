package main.java.da_utils.xml_maker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * makes musicxml file for opening in sibelius or musescore
 * currently assumes 4/4 time until i have figured out others
 */

import java.util.ArrayList;

import DataObjects.incomplete_note_utils.FinalListNote;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.combo_variables.IntAndString;
import main.java.da_utils.xml_maker.key.KeyMap;
import main.java.da_utils.xml_maker.key.XMLKey;
import main.java.da_utils.xml_maker.measure.MeasureMap;
import main.java.da_utils.xml_maker.part_items.Part;
import main.java.da_utils.xml_maker.part_items.PartNote;

public class MusicXMLMaker {


	
	
	public MusicXMLObject mx;
	public ArrayList<Part> partList = new ArrayList<Part>();
	public MeasureMap measureMap = new MeasureMap();
	public double barLength = 4.0;
	public XMLKey xmlKey;
	public KeyMap keyMap = new KeyMap();
	private boolean hasDefaults = false;
	private int pageOrientation = MXM.PORTRAIT;
	
	
	
	public MusicXMLMaker(XMLKey xmlKey){
		this.xmlKey = xmlKey;
	}
	public MusicXMLMaker(int musicXMLKey){
		// range must be -6 to 6 inclusive
		this.xmlKey = MXM.xmlKeyMap.get(musicXMLKey);
	}
	public void addPart(String str){
		partList.add(new Part(str));
	}
	public void addPart(String str, LiveClip lc) {
		//System.out.println("new Part created: " + str + " with clip with " + lc.noteList.size() + " notes");
		partList.add(new Part(str, lc));
		
	}
	public void addPart(String str, ArrayList<FinalListNote> finalNoteList){
		// this part not yet complete. Still no clarity how to deal with quantum notes
		// what is a quantum note, yo may ask?
		// a note where you no some information but not other e.g. its position in a sequence but
		// not its exact bar pos.......
		partList.add(new Part(str, finalNoteList));
		
	}
	public void addTextDirection(String partName, String text, double pos, int placement){
		Part p = getPart(partName);
		if (p != null){
			p.addTextDirection(text, pos, placement);
		}
	}
	public void addTextDirection(String partName, String text, double pos, int placement, int textSize){
		Part p = getPart(partName);
		if (p != null){
			p.addTextDirection(text, pos, placement, textSize);
		}
	}
	public void addTextDirection(String partName, String text, double pos, int placement, int textSize, int yoffset){
		Part p = getPart(partName);
		if (p != null){
			p.addTextDirection(text, pos, placement, textSize, yoffset);
		}
	}
	public String toString(){
		String ret = "";
		for (Part p: partList){
			ret += p.toString();
		}
		return ret;
	}
	public void addLiveClipToPart(LiveClip lc, String partName){
		Part p = getPart(partName);
		if (p != null){
			for (LiveMidiNote lmn: lc.noteList){
				p.addNote(new PartNote(lmn));
			}
		}
	}
	public Part getPart(String str){
		for (Part p: partList){
			if (p.name().equals(str)){
				return p;
			}
		}
		return null;
	}
	public void makeXML(String path){
		try{
			File file = new File(path);
			if (!file.exists()) {
			     file.createNewFile();
			  }
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for (String str: makeStringList()){
//				System.out.println(str);				// this prints xml file contents to console
				bw.write(str);
				bw.newLine();
			}
			  
			bw.close();
		} catch (Exception ex){
			System.out.println(ex.toString());
			ex.printStackTrace();
		}
	}
	public void setLandscapePageOrientation(){
		pageOrientation = MXM.LANDSCAPE;
		hasDefaults = true;
	}
	
	
// private -------------------------------------------------------------
	private ArrayList<String> makeStringList() {
//		System.out.println("adding " + MXM.SCORE_PARTWISE);
		ArrayList<String> strList = new ArrayList<String>();
		int indent = 0;
		makeMeasures();
		setDivisionsValueForParts();
//		System.out.println("adding " + LB + SCORE_PARTWISE + RB);
		addHeaders(strList);
		strList.add(MXM.LB + MXM.SCORE_PARTWISE + MXM.VERSION_3 + MXM.RB);
		addScorePartwiseContents(strList, indent + MXM.indentIncrement);
//		System.out.println("adding " + getIndent(indent + indentIncrement) + LB + BS + SCORE_PARTWISE + RB);
		strList.add(MXM.getIndent(indent + MXM.indentIncrement) + MXM.LB + MXM.BS + MXM.SCORE_PARTWISE + MXM.RB);
		
//		systemOutStringList(strList);
		
		return strList;
	}
	private void systemOutStringList(ArrayList<String> strList) {
		for (String str: strList){
			System.out.println(str);
		}
		
	}
	private void setDivisionsValueForParts() {
		// this could be better. Currently it sets the divisions to the smallest value possible based on quantValue
		// as opposed to the smallest value neccesary for the actual content of the part.
		for (Part p: partList){
			p.setDivisionsValue((int)(1 / PartNote.quantValue));
		}
		
	}
	private void makeMeasures() {
		for (Part p: partList){
			p.makeMeasures(measureMap, keyMap);
		}
	
}
	private void addHeaders(ArrayList<String> strList) {
		strList.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strList.add("<!DOCTYPE score-partwise PUBLIC \"-//Recordare//DTD MusicXML 3.0 Partwise//EN\" \"http://www.musicxml.org/dtds/partwise.dtd\">");
		
	}
	private void addScorePartwiseContents(ArrayList<String> strList, int indent) {
		addWork(strList, indent);
		addIndentifaction(strList, indent);
		addDefaults(strList, indent);
		addCredit(strList, indent);
		addPartList(strList, indent);
		addParts(strList, indent);
		
	}
	private void addParts(ArrayList<String> strList, int indent) {
		// any notes beyond the measure count will be dropped
//		int measuresRequired = getMeasuresRequired();
//		if (measuresRequired > measureCount) measureCount = measuresRequired;
		for (Part p: partList){
			
			p.addPartToStringList(strList, indent);
		}
		
	}
//	private int getMeasuresRequired() {
//		double lastEvent = 0.0;
//		for (Part p: partList){
//			double partLastEvent = p.getMeasuresRequired();
//			if (partLastEvent > lastEvent) lastEvent = partLastEvent;
//		}
//		return (int)((lastEvent + barLength) / barLength);
//	}
	private void addPartList(ArrayList<String> strList, int indent) {
		strList.add(MXM.getIndent(indent) + MXM.LB + MXM.PART_LIST + MXM.RB);
		for (Part p: partList){
			p.addScorePartToStringList(strList, indent + MXM.indentIncrement);
		}
		strList.add(MXM.getIndent(indent + MXM.indentIncrement) + MXM.LB + MXM.BS + MXM.PART_LIST + MXM.RB);
		
	}

	private void addCredit(ArrayList<String> strList, int i) {
		// TODO Auto-generated method stub
		
	}
	private void addDefaults(ArrayList<String> strList, int indent) {
		if (hasDefaults ){
			strList.add(MXM.getIndent(indent) + MXM.LB + MXM.PAGE_LAYOUT + MXM.RB);
			double width  = MXM.A4_SHORT;
			double height = MXM.A4_LONG;
			 if (pageOrientation == MXM.LANDSCAPE){
				width = MXM.A4_LONG;
				height = MXM.A4_SHORT;
			}
			MXM.addOneLiner(strList, MXM.PAGE_WIDTH, "" + width, indent + MXM.indentIncrement);
			MXM.addOneLiner(strList, MXM.PAGE_HEIGHT, "" + height, indent + MXM.indentIncrement);
			strList.add(MXM.getIndent(indent + MXM.indentIncrement) + MXM.LB + MXM.BS + MXM.PAGE_LAYOUT + MXM.RB);
		}
		
		
	}
	private void addIndentifaction(ArrayList<String> strList, int i) {
		// TODO Auto-generated method stub
		
	}
	private void addWork(ArrayList<String> strList, int i) {
		// TODO Auto-generated method stub
		
	}
	
	
}
