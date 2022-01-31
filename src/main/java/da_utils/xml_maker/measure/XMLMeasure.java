package main.java.da_utils.xml_maker.measure;

import java.util.ArrayList;
import java.util.HashMap;

import main.java.da_utils.xml_maker.MXM;
import main.java.da_utils.xml_maker.clef.XMLClef;
import main.java.da_utils.xml_maker.key.XMLKey;
import main.java.da_utils.xml_maker.part_items.BarLine;
import main.java.da_utils.xml_maker.part_items.PartDirection;
import main.java.da_utils.xml_maker.rhythm.RhythmOption;
import main.java.da_utils.xml_maker.rhythm.RhythmOptionTemplate;
import main.java.da_utils.xml_maker.time_signature.XMLTimeSignatureZone;
import main.java.da_utils.xml_maker.voice_item.VoiceItem;

public class XMLMeasure {

	
	private int number;
	public boolean hasAttributes = false;	// if true, will add whichever attribute is true
	public boolean hasDivisions = false;
	public boolean hasKey = false;
	public boolean hasTime = false;
	public boolean hasClef = false;
	public XMLMeasure nextMeasure = null;
	public double start;
	public double end;
	public XMLTimeSignatureZone tsz;
	public HashMap<Integer, ArrayList<VoiceItem>> voiceItemMap = new HashMap<Integer, ArrayList<VoiceItem>>();
//	public ArrayList<VoiceItem> voiceItemList = new ArrayList<VoiceItem>();
	private XMLKey xmlKey;
	public XMLKey key;
	public XMLClef clef;
	public ArrayList<RhythmOption> rhythmOptionNotesList = new ArrayList<RhythmOption>();
	public ArrayList<RhythmOption> rhythmOptionRestsList = new ArrayList<RhythmOption>();

	public XMLMeasure(int number, double start, double end, XMLTimeSignatureZone tsz, XMLKey xmlKey, XMLClef clef){
		this.number = number;
//		hasAttributes = b;
		this.start = start;
		this.end = end;
		this.tsz = tsz;
		this.xmlKey = xmlKey;
		this.clef = clef;
		makeRhythmOptionsLists();
	}
	private void makeRhythmOptionsLists() {
		String rOptKey = tsz.beats + "/" + tsz.beatType;
		//System.out.println("XMLMeasure: looking for rOptKey=" + rOptKey + " in bar=" + number);
		if (rhythmOptionTemplateMap.keySet().contains(rOptKey)){
			double pos = 0.0;
			for (RhythmOptionTemplate rot: rhythmOptionTemplateMap.get(rOptKey)){
				for (RhythmOption ro: rot.rhythmOptionNoteArr){
					RhythmOption newro = new RhythmOption(ro.start + pos, ro.length, ro.appearance);
					//System.out.println(newro.toString());
					rhythmOptionNotesList.add(newro);
				}
				for (RhythmOption ro: rot.rhythmOptionRestArr){
					RhythmOption newro = new RhythmOption(ro.start + pos, ro.length, ro.appearance);
					rhythmOptionRestsList.add(newro);
				}
				pos += rot.length;
			}
		} else {
			System.out.println("XMLMeasure.makeRhythmOptionLists cannot handle timesignature");
		}
		
	}
	public void addMeasureToStringList(ArrayList<String> strList, int indent, int divisions){
		strList.add(MXM.getIndent(indent) + MXM.LB + MXM.MEASURE + MXM.NUMBER_EQUALS + MXM.INVC + number + MXM.INVC + MXM.RB);
		if (hasAttributes) addAttributesToStringList(strList, indent + MXM.indentIncrement, divisions);
		
		addMeasureContentToStringList(strList, indent + MXM.indentIncrement, divisions);

		strList.add(MXM.getIndent(indent + MXM.indentIncrement) + MXM.LB + MXM.BS + MXM.MEASURE + MXM.RB);
	}
	private void addMeasureContentToStringList(ArrayList<String> strList, int indent, int divisions) {
		//System.out.println("XMLMeasure.addMeasureToStringList Measure=" + number);
		boolean addBackup = false;
		int numberOfVoicesDealtWith = 0;
		int voiceIndex = 0;
		while (numberOfVoicesDealtWith < voiceItemMap.size()){
			if (voiceItemMap.containsKey(voiceIndex)){
				ArrayList<VoiceItem> viList = voiceItemMap.get(voiceIndex);
				if (addBackup) addBackup(strList, viList, indent, divisions);
				dealWithVoice(strList, indent, viList, divisions);
				numberOfVoicesDealtWith++;
				addBackup = true;
			}
			
			voiceIndex++;
		}
		
	}
	private void dealWithVoice(ArrayList<String> strList, int indent, ArrayList<VoiceItem> viList, int divisions) {
		for (VoiceItem vi: viList){
			vi.addVoiceItemContent(strList, indent, divisions, xmlKey, tsz);
		}
		
	}
	private void addBackup(ArrayList<String> strList, ArrayList<VoiceItem> viList, int indent, int divisions) {
		int backupValue = divisions * tsz.beats;	// currently always backs up one whole bar.....
		MXM.makeQuickEntry(strList, MXM.BACKUP, new String[][]{new String[]{MXM.DURATION, Integer.toString(backupValue)}}, new String[][]{}, indent);
		
	}
	public String toString(){
		String ret = "XMLMeasure: " + number + "\n";
		ret += tsz.toString() + "\n";
		ret += "start=" + start + " end=" + end + "\n";
		for (Integer key: voiceItemMap.keySet()){
			ArrayList<VoiceItem> viList = voiceItemMap.get(key);
			for (VoiceItem vi: viList){
				ret += "    " + vi.toString() + "\n";
			}
		}
		
		if (nextMeasure != null) ret += "\nnextMeasure=" + nextMeasure.number;
		return ret;
	}
	public void addVoiceItem(VoiceItem vi){
		// presupposes that the voices have been sorted out
		if (vi.start() >= start && vi.start() < end){
			RhythmOption ro = getLongestRhythmOption(vi);
			//System.out.println("is in Measure " + this.number + " rhythmOption " + ro.toString());
			VoiceItem newVI = new VoiceItem(vi);
			if (ro.length < vi.length()){
				VoiceItem passOnVI = new VoiceItem(vi);
				passOnVI.setStart(passOnVI.start() + ro.length);
				newVI.setEnd(newVI.start() + ro.length);
				newVI.hasStartTie = true;
				passOnVI.hasEndTie = true;
				newVI.appearance = ro.appearance;
				addNewVoiceItem(newVI);
				if (newVI.end() < end){
					addVoiceItem(passOnVI);
				} else {
					nextMeasure.addVoiceItem(passOnVI);
				}
				
			} else {
				newVI.appearance = ro.appearance;				
				if (newVI.voiceIndex == 1){
					addNewVoiceItem(newVI);
				} else {
					if (newVI.isRest && newVI.length() == length()){
						
					} else {
						addNewVoiceItem(newVI);
					}
				}
				
			}
		} else {
			//System.out.println("passing to measure " + nextMeasure.number);
			if (nextMeasure != null) nextMeasure.addVoiceItem(vi);			
		}
		
	}
	private double length() {
		
		return end - start;
	}
	public void addAllAttributes(){
		hasAttributes = true;	// if true, will add whichever attribute is true
		hasDivisions = true;
		hasKey = true;
		hasTime = true;
		hasClef = true;
	}
	public void addDirection(PartDirection pi) {
		if (pi.position() >= start && pi.position() < end){
			addPartDirectionToClosestVoiceItem(pi);
		} else {
			if (nextMeasure != null){
				nextMeasure.addDirection(pi);
			}
			
		}
		
	}
	private void addPartDirectionToClosestVoiceItem(PartDirection pi) {
		//System.out.println("XMLMeasure.addPartDirectionToClosestVoiceItem - adfding PartDirection:" + pi.toString() + " voiceItemMap.size=" + voiceItemMap.get(1).size());
		VoiceItem chosenVI = voiceItemMap.get(1).get(0);
		double distance = 1000;			// arbvitrarily large number
		double temp;
		for (VoiceItem vi: voiceItemMap.get(1)){
			temp = Math.abs(pi.position() - vi.start());
			if (temp < distance){
				distance = temp;
				chosenVI = vi;
			}
		}
		chosenVI.addPartItem(pi);
	}
	
	private void addNewVoiceItem(VoiceItem vi) {
		ArrayList<VoiceItem> viList;
		if (voiceItemMap.containsKey(vi.voiceIndex)){
			viList = voiceItemMap.get(vi.voiceIndex);
		} else {
			viList = new ArrayList<VoiceItem>();
			voiceItemMap.put(vi.voiceIndex, viList);
		}
		viList.add(vi);
		
	}
	private RhythmOption getLongestRhythmOption(VoiceItem vi) {
		ArrayList<RhythmOption> roList;
		if (vi.isRest){
			roList = rhythmOptionRestsList;
		} else {
			roList = rhythmOptionNotesList;
		}
		
//		double viStart = vi.start() - start;
//		double viLength = vi.length();
		
		for (RhythmOption ro: roList){
//			double roStart = ro.start;
//			double roLength = ro.length;
			if (ro.start == vi.start() - start && ro.length <= vi.length()){
//				System.out.println("Voice Item and most suitable RhythmOption:");
//				System.out.println(vi.toString());
//				System.out.println(ro.toString());
				return ro;
			}
		}
		return null;
	}
	private void addAttributesToStringList(ArrayList<String> strList, int indent, int divisions) {
		strList.add(MXM.getIndent(indent) + MXM.LB + MXM.ATTRIBUTES + MXM.RB);

		if (hasDivisions) addDivisions(strList, indent + MXM.indentIncrement, divisions);
		if (hasKey) addKey(strList, indent + MXM.indentIncrement);
		if (hasTime) addTime(strList, indent + MXM.indentIncrement);
		if (hasClef) addClef(strList, indent + MXM.indentIncrement);
		strList.add(MXM.getIndent(indent + MXM.indentIncrement) + MXM.LB + MXM.BS + MXM.ATTRIBUTES + MXM.RB);
	}
	private void addClef(ArrayList<String> strList, int indent) {
		MXM.makeQuickEntry(
				strList, 
				MXM.CLEF, 
				new String[][]{
					new String[]{MXM.SIGN,  clef.clef()}, 			// default behaviour for testing. needs expanding
					new String[]{MXM.LINE, clef.lineAsString()}},
				new String[][]{},
				indent);
		
	}
	private void addTime(ArrayList<String> strList, int indent) {
		MXM.makeQuickEntry(
				strList, 
				MXM.TIME, 
				new String[][]{
					new String[]{MXM.BEATS,  tsz.beatsAsString()}, 			// default behaviour, needs expanding
					new String[]{MXM.BEAT_TYPE, tsz.beatTypeAsString()}},
				new String[][]{},
				indent);
		
	}
	private void addKey(ArrayList<String> strList, int indent) {
		String keyEntry;
		if (key != null){
			keyEntry = Integer.toString(key.xmlKeyValue);
		} else {
			keyEntry = "0";			// default to key of C
		}
		
		MXM.makeQuickEntry(
				strList, 
				MXM.KEY, 
				new String[][]{
					new String[]{MXM.FIFTHS, keyEntry}}, 			
				new String[][]{},
				indent);
		
	}
	private void addDivisions(ArrayList<String> strList, int indent, int divisions) {
		MXM.addOneLiner(
				strList, 
				MXM.DIVISIONS, 
				"" + divisions,
				indent);
		
	}
	public void setHasAttributes(boolean b){
		hasAttributes = b;
	}
	private static final RhythmOptionTemplate rot44 = new RhythmOptionTemplate(4, 4, 4.0,
		new RhythmOption[]{		// this is for 4/4
			new RhythmOption(0.0, 4.0, MXM.WHOLE),
			new RhythmOption(0.0, 3.0, MXM.DOTTED_HALF),
			new RhythmOption(1.0, 3.0, MXM.DOTTED_HALF),
			new RhythmOption(0.0, 2.0, MXM.HALF),
			new RhythmOption(1.0, 2.0, MXM.HALF),
			new RhythmOption(2.0, 2.0, MXM.HALF),
			new RhythmOption(0.0, 1.5, MXM.DOTTED_QUARTER),
			new RhythmOption(0.5, 1.5, MXM.DOTTED_QUARTER),
			new RhythmOption(2.0, 1.5, MXM.DOTTED_QUARTER),
			new RhythmOption(2.5, 1.5, MXM.DOTTED_QUARTER),
			new RhythmOption(0.0, 1.0, MXM.QUARTER),
			new RhythmOption(0.5, 1.0, MXM.QUARTER),
			new RhythmOption(1.0, 1.0, MXM.QUARTER),
			new RhythmOption(2.0, 1.0, MXM.QUARTER),
			new RhythmOption(2.5, 1.0, MXM.QUARTER),
			new RhythmOption(3.0, 1.0, MXM.QUARTER),
			new RhythmOption(0.0, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(0.0, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(0.25, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(1.0, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(1.25, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(2.0, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(2.25, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(3.0, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(3.25, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(0.0, 0.5, MXM.EIGHTH),
			new RhythmOption(0.25, 0.5, MXM.EIGHTH),
			new RhythmOption(0.5, 0.5, MXM.EIGHTH),
			new RhythmOption(1.0, 0.5, MXM.EIGHTH),
			new RhythmOption(1.25, 0.5, MXM.EIGHTH),
			new RhythmOption(1.5, 0.5, MXM.EIGHTH),
			new RhythmOption(2.0, 0.5, MXM.EIGHTH),
			new RhythmOption(2.25, 0.5, MXM.EIGHTH),
			new RhythmOption(2.5, 0.5, MXM.EIGHTH),
			new RhythmOption(3.0, 0.5, MXM.EIGHTH),
			new RhythmOption(3.25, 0.5, MXM.EIGHTH),
			new RhythmOption(3.5, 0.5, MXM.EIGHTH),
			new RhythmOption(0.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.75, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.75, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.75, 0.25, MXM.SIXTEENTH),
			new RhythmOption(3.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(3.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(3.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(3.75, 0.25, MXM.SIXTEENTH),
	},
		new RhythmOption[]{		// this is for 4/4
			new RhythmOption(0.0, 4.0, MXM.WHOLE),
			new RhythmOption(0.0, 2.0, MXM.HALF),
			new RhythmOption(1.0, 2.0, MXM.HALF),
			new RhythmOption(2.0, 2.0, MXM.HALF),
			new RhythmOption(0.0, 1.0, MXM.QUARTER),
			new RhythmOption(1.0, 1.0, MXM.QUARTER),
			new RhythmOption(2.0, 1.0, MXM.QUARTER),
			new RhythmOption(3.0, 1.0, MXM.QUARTER),
			new RhythmOption(0.0, 0.5, MXM.EIGHTH),
			new RhythmOption(0.5, 0.5, MXM.EIGHTH),
			new RhythmOption(1.0, 0.5, MXM.EIGHTH),
			new RhythmOption(1.5, 0.5, MXM.EIGHTH),
			new RhythmOption(2.0, 0.5, MXM.EIGHTH),
			new RhythmOption(2.5, 0.5, MXM.EIGHTH),
			new RhythmOption(3.0, 0.5, MXM.EIGHTH),
			new RhythmOption(3.5, 0.5, MXM.EIGHTH),
			new RhythmOption(0.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.75, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.75, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.75, 0.25, MXM.SIXTEENTH),
			new RhythmOption(3.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(3.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(3.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(3.75, 0.25, MXM.SIXTEENTH),
	});
	private static final RhythmOptionTemplate rot34 = new RhythmOptionTemplate(3, 4, 3.0,
		new RhythmOption[]{		// this is for 3/4
			new RhythmOption(0.0, 3.0, MXM.DOTTED_HALF),
			new RhythmOption(0.0, 2.0, MXM.HALF),
			new RhythmOption(1.0, 2.0, MXM.HALF),
			new RhythmOption(0.0, 1.5, MXM.DOTTED_QUARTER),
			new RhythmOption(0.5, 1.5, MXM.DOTTED_QUARTER),
			new RhythmOption(1.0, 1.5, MXM.DOTTED_QUARTER),
			new RhythmOption(1.5, 1.5, MXM.DOTTED_QUARTER),
			new RhythmOption(0.0, 1.0, MXM.QUARTER),
			new RhythmOption(0.5, 1.0, MXM.QUARTER),
			new RhythmOption(1.0, 1.0, MXM.QUARTER),
			new RhythmOption(1.5, 1.0, MXM.QUARTER),
			new RhythmOption(2.0, 1.0, MXM.QUARTER),
			new RhythmOption(0.0, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(0.25, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(1.0, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(1.25, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(2.0, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(2.25, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(0.0, 0.5, MXM.EIGHTH),
			new RhythmOption(0.25, 0.5, MXM.EIGHTH),
			new RhythmOption(0.5, 0.5, MXM.EIGHTH),
			new RhythmOption(1.0, 0.5, MXM.EIGHTH),
			new RhythmOption(1.25, 0.5, MXM.EIGHTH),
			new RhythmOption(1.5, 0.5, MXM.EIGHTH),
			new RhythmOption(2.0, 0.5, MXM.EIGHTH),
			new RhythmOption(2.25, 0.5, MXM.EIGHTH),
			new RhythmOption(2.5, 0.5, MXM.EIGHTH),
			new RhythmOption(0.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.75, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.75, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.75, 0.25, MXM.SIXTEENTH),
	},
		new RhythmOption[]{		// this is for 3/4
			new RhythmOption(0.0, 2.0, MXM.HALF),
			new RhythmOption(1.0, 2.0, MXM.HALF),
			new RhythmOption(0.0, 1.0, MXM.QUARTER),
			new RhythmOption(1.0, 1.0, MXM.QUARTER),
			new RhythmOption(2.0, 1.0, MXM.QUARTER),
			new RhythmOption(0.0, 0.5, MXM.EIGHTH),
			new RhythmOption(0.5, 0.5, MXM.EIGHTH),
			new RhythmOption(1.0, 0.5, MXM.EIGHTH),
			new RhythmOption(1.5, 0.5, MXM.EIGHTH),
			new RhythmOption(2.0, 0.5, MXM.EIGHTH),
			new RhythmOption(2.5, 0.5, MXM.EIGHTH),
			new RhythmOption(0.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.75, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.75, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(2.75, 0.25, MXM.SIXTEENTH),
	});
	private static final RhythmOptionTemplate rot24 = new RhythmOptionTemplate(2, 4, 2.0,
		new RhythmOption[]{		// this is for 3/4
			new RhythmOption(0.0, 2.0, MXM.HALF),
			new RhythmOption(0.0, 1.5, MXM.DOTTED_QUARTER),
			new RhythmOption(0.5, 1.5, MXM.DOTTED_QUARTER),
			new RhythmOption(0.0, 1.0, MXM.QUARTER),
			new RhythmOption(0.5, 1.0, MXM.QUARTER),
			new RhythmOption(1.0, 1.0, MXM.QUARTER),
			new RhythmOption(0.0, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(0.25, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(1.0, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(1.25, 0.75, MXM.DOTTED_EIGHTH),
			new RhythmOption(0.0, 0.5, MXM.EIGHTH),
			new RhythmOption(0.25, 0.5, MXM.EIGHTH),
			new RhythmOption(0.5, 0.5, MXM.EIGHTH),
			new RhythmOption(1.0, 0.5, MXM.EIGHTH),
			new RhythmOption(1.25, 0.5, MXM.EIGHTH),
			new RhythmOption(1.5, 0.5, MXM.EIGHTH),
			new RhythmOption(0.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.75, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.75, 0.25, MXM.SIXTEENTH),
	},
		new RhythmOption[]{		// this is for 2/4
			new RhythmOption(0.0, 2.0, MXM.HALF),
			new RhythmOption(0.0, 1.0, MXM.QUARTER),
			new RhythmOption(1.0, 1.0, MXM.QUARTER),
			new RhythmOption(0.0, 0.5, MXM.EIGHTH),
			new RhythmOption(0.5, 0.5, MXM.EIGHTH),
			new RhythmOption(1.0, 0.5, MXM.EIGHTH),
			new RhythmOption(1.5, 0.5, MXM.EIGHTH),
			new RhythmOption(0.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(0.75, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.0, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.25, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.5, 0.25, MXM.SIXTEENTH),
			new RhythmOption(1.75, 0.25, MXM.SIXTEENTH),
	});
	private static final RhythmOptionTemplate rot48 = new RhythmOptionTemplate(4, 8, 2.0,
			new RhythmOption[]{		// this is for 4/8
				new RhythmOption(0.0, 2.0, MXM.HALF),
				new RhythmOption(0.0, 1.5, MXM.DOTTED_QUARTER),
				new RhythmOption(0.5, 1.5, MXM.DOTTED_QUARTER),
				new RhythmOption(0.0, 1.0, MXM.QUARTER),
				new RhythmOption(0.5, 1.0, MXM.QUARTER),
				new RhythmOption(1.0, 1.0, MXM.QUARTER),
				new RhythmOption(0.0, 0.75, MXM.DOTTED_EIGHTH),
				new RhythmOption(0.0, 0.75, MXM.DOTTED_EIGHTH),
				new RhythmOption(0.25, 0.75, MXM.DOTTED_EIGHTH),
				new RhythmOption(1.0, 0.75, MXM.DOTTED_EIGHTH),
				new RhythmOption(1.25, 0.75, MXM.DOTTED_EIGHTH),	
				new RhythmOption(0.0, 0.5, MXM.EIGHTH),
				new RhythmOption(0.25, 0.5, MXM.EIGHTH),
				new RhythmOption(0.5, 0.5, MXM.EIGHTH),
				new RhythmOption(1.0, 0.5, MXM.EIGHTH),
				new RhythmOption(1.25, 0.5, MXM.EIGHTH),
				new RhythmOption(1.5, 0.5, MXM.EIGHTH),				
				new RhythmOption(0.0, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.25, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.5, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.75, 0.25, MXM.SIXTEENTH),
				new RhythmOption(1.0, 0.25, MXM.SIXTEENTH),
				new RhythmOption(1.25, 0.25, MXM.SIXTEENTH),
				new RhythmOption(1.5, 0.25, MXM.SIXTEENTH),
				new RhythmOption(1.75, 0.25, MXM.SIXTEENTH),
				
		},
			new RhythmOption[]{		// this is for 4/8
				new RhythmOption(0.0, 2.0, MXM.HALF),
				new RhythmOption(0.0, 1.0, MXM.QUARTER),
				new RhythmOption(1.0, 1.0, MXM.QUARTER),
				new RhythmOption(0.0, 0.5, MXM.EIGHTH),
				new RhythmOption(0.5, 0.5, MXM.EIGHTH),
				new RhythmOption(1.0, 0.5, MXM.EIGHTH),
				new RhythmOption(1.5, 0.5, MXM.EIGHTH),				
				new RhythmOption(0.0, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.25, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.5, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.75, 0.25, MXM.SIXTEENTH),
				new RhythmOption(1.0, 0.25, MXM.SIXTEENTH),
				new RhythmOption(1.25, 0.25, MXM.SIXTEENTH),
				new RhythmOption(1.5, 0.25, MXM.SIXTEENTH),
				new RhythmOption(1.75, 0.25, MXM.SIXTEENTH),
				
		});
	private static final RhythmOptionTemplate rot38 = new RhythmOptionTemplate(3, 8, 1.5,
			new RhythmOption[]{		// this is for 3/8
				new RhythmOption(0.0, 1.5, MXM.DOTTED_QUARTER),
				new RhythmOption(0.0, 1.0, MXM.QUARTER),
				new RhythmOption(0.5, 1.0, MXM.QUARTER),
				new RhythmOption(0.0, 0.75, MXM.DOTTED_EIGHTH),
				new RhythmOption(0.25, 0.75, MXM.DOTTED_EIGHTH),
				new RhythmOption(0.5, 0.75, MXM.DOTTED_EIGHTH),
				new RhythmOption(0.75, 0.75, MXM.DOTTED_EIGHTH),
				new RhythmOption(1.0, 0.75, MXM.DOTTED_EIGHTH),	
				new RhythmOption(0.0, 0.5, MXM.EIGHTH),
				new RhythmOption(0.25, 0.5, MXM.EIGHTH),
				new RhythmOption(0.5, 0.5, MXM.EIGHTH),
				new RhythmOption(1.0, 0.5, MXM.EIGHTH),			
				new RhythmOption(0.0, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.25, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.5, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.75, 0.25, MXM.SIXTEENTH),
				new RhythmOption(1.0, 0.25, MXM.SIXTEENTH),
				new RhythmOption(1.25, 0.25, MXM.SIXTEENTH),

				
		},
			new RhythmOption[]{		// this is for 3/8
				new RhythmOption(0.0, 1.0, MXM.QUARTER),
				new RhythmOption(0.0, 0.5, MXM.EIGHTH),
				new RhythmOption(0.5, 0.5, MXM.EIGHTH),
				new RhythmOption(1.0, 0.5, MXM.EIGHTH),				
				new RhythmOption(0.0, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.25, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.5, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.75, 0.25, MXM.SIXTEENTH),
				new RhythmOption(1.0, 0.25, MXM.SIXTEENTH),
				new RhythmOption(1.25, 0.25, MXM.SIXTEENTH),
				
		});
	private static final RhythmOptionTemplate rot28 = new RhythmOptionTemplate(2, 8, 1.0,
			new RhythmOption[]{		// this is for 3/8

				new RhythmOption(0.0, 1.0, MXM.QUARTER),
				new RhythmOption(0.0, 0.75, MXM.DOTTED_EIGHTH),
				new RhythmOption(0.25, 0.75, MXM.DOTTED_EIGHTH),	
				new RhythmOption(0.0, 0.5, MXM.EIGHTH),
				new RhythmOption(0.25, 0.5, MXM.EIGHTH),
				new RhythmOption(0.5, 0.5, MXM.EIGHTH),			
				new RhythmOption(0.0, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.25, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.5, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.75, 0.25, MXM.SIXTEENTH),
				
		},
			new RhythmOption[]{		// this is for 3/8
				new RhythmOption(0.0, 1.0, MXM.QUARTER),
				new RhythmOption(0.0, 0.5, MXM.EIGHTH),
				new RhythmOption(0.5, 0.5, MXM.EIGHTH),			
				new RhythmOption(0.0, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.25, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.5, 0.25, MXM.SIXTEENTH),
				new RhythmOption(0.75, 0.25, MXM.SIXTEENTH),

				
		});
	public static HashMap<String, RhythmOptionTemplate[]> rhythmOptionTemplateMap = new HashMap<String, RhythmOptionTemplate[]>()
			{{
				put("2/4", new RhythmOptionTemplate[]{rot24});
				put("3/4", new RhythmOptionTemplate[]{rot34});
				put("4/4", new RhythmOptionTemplate[]{rot44});
				put("5/4", new RhythmOptionTemplate[]{rot34, rot24});
				put("6/4", new RhythmOptionTemplate[]{rot34, rot34});
				put("7/4", new RhythmOptionTemplate[]{rot34, rot44});
				put("8/4", new RhythmOptionTemplate[]{rot44, rot44});
				put("9/4", new RhythmOptionTemplate[]{rot34, rot34, rot34});
				put("10/4", new RhythmOptionTemplate[]{rot34, rot34, rot44});
				put("11/4", new RhythmOptionTemplate[]{rot34, rot34, rot34, rot24});
				put("12/4", new RhythmOptionTemplate[]{rot44, rot44, rot44});
				put("13/4", new RhythmOptionTemplate[]{rot44, rot34, rot44, rot24});
				put("14/4", new RhythmOptionTemplate[]{rot34, rot44, rot34, rot44});
				put("15/4", new RhythmOptionTemplate[]{rot44, rot44, rot44, rot34});
				put("16/4", new RhythmOptionTemplate[]{rot44, rot44, rot44, rot44});
				
				put("2/8", new RhythmOptionTemplate[]{rot28});
				put("3/8", new RhythmOptionTemplate[]{rot38});
				put("4/8", new RhythmOptionTemplate[]{rot48});
				put("5/8", new RhythmOptionTemplate[]{rot38, rot28});
				put("6/8", new RhythmOptionTemplate[]{rot38, rot38});
				put("7/8", new RhythmOptionTemplate[]{rot38, rot48});
				put("8/8", new RhythmOptionTemplate[]{rot44});
				put("9/8", new RhythmOptionTemplate[]{rot38, rot38, rot38});
				put("10/8", new RhythmOptionTemplate[]{rot38, rot38, rot48});
				put("11/8", new RhythmOptionTemplate[]{rot38, rot38, rot38, rot28});
				put("12/8", new RhythmOptionTemplate[]{rot38, rot38, rot38, rot38});
				put("13/8", new RhythmOptionTemplate[]{rot48, rot38, rot48, rot28});
				put("14/8", new RhythmOptionTemplate[]{rot38, rot48, rot38, rot48});
				put("15/8", new RhythmOptionTemplate[]{rot48, rot48, rot48, rot38});
				put("16/8", new RhythmOptionTemplate[]{rot44, rot44});
			}};

	public void addBarLine(BarLine pi) {
		if (pi.position() >= start && pi.position() < end){
			if (pi.position() == start){
				pi.customSetting(MXM.LOCATION, MXM.LEFT);
			} else {
				pi.customSetting(MXM.LOCATION, MXM.RIGHT);
			}
			addBarlineToClosestVoiceItem(pi);
		} else {
			if (nextMeasure != null){
				nextMeasure.addBarLine(pi);
			}
			
		}
		
	}
	private void addBarlineToClosestVoiceItem(BarLine pi) {
		VoiceItem chosenVI = voiceItemMap.get(1).get(0);
		
		chosenVI.addPartItem(pi);
	}
	
}
