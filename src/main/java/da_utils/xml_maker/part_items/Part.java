package main.java.da_utils.xml_maker.part_items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import DataObjects.incomplete_note_utils.FinalListNote;
import DataObjects.incomplete_note_utils.FinalListNoteAnnotation;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.static_chord_scale_dictionary.CSD;
import main.java.da_utils.xml_maker.MXM;
import main.java.da_utils.xml_maker.clef.XMLClef;
import main.java.da_utils.xml_maker.key.KeyMap;
import main.java.da_utils.xml_maker.measure.MeasureMap;
import main.java.da_utils.xml_maker.measure.XMLMeasure;
import main.java.da_utils.xml_maker.time_signature.XMLTimeSignatureZone;
import main.java.da_utils.xml_maker.voice_item.VoiceItem;

public class Part {

	
	
	private String name;
	private ArrayList<PartItem> itemList = new ArrayList<PartItem>();
	private static int partNumber = 1;
	private String ID = "P";
	public ArrayList<XMLMeasure> measureList = new ArrayList<XMLMeasure>();
	private int divisions = 1;
	private LiveClip lc;
	private XMLClef clef;
	private double beamingZoneSize = 2.0;

	public Part(String name){
		init(name);
	}
	public Part(String str, LiveClip lc) {
		init(str);
		addLiveCLip(lc);
	}
	public Part(String str, ArrayList<FinalListNote> finalNoteList) {
		init(str);
		addFinalNoteList(finalNoteList);
	}
	private void addFinalNoteList(ArrayList<FinalListNote> finalNoteList) {
		// this part not yet complete. Still no clarity how to deal with quantum notes
		// what is a quantum note, yo may ask?
		// a note where you no some information but not other e.g. its position in a sequence but
		// not its exact bar pos....... works for complete notes
		for (FinalListNote fln: finalNoteList){
			if (fln.hasNotes()){
				itemList.add(new PartNote(fln));
			}
			
			int annotationIncrement = MXM.ANNOTATION_BASELINE;
			for (FinalListNoteAnnotation flna: fln.annotationList()){
				if (flna.hasXOffset()){
					addTextDirection(flna.text(), fln.position(), MXM.PLACEMENT_BELOW, MXM.DEFAULT_ANNOTATION_TEXT_SIZE, annotationIncrement, flna.xOffset());
				} else {
					addTextDirection(flna.text(), fln.position(), MXM.PLACEMENT_BELOW, MXM.DEFAULT_ANNOTATION_TEXT_SIZE, annotationIncrement);
					
				}
				annotationIncrement += MXM.LAYERED_ANNOTATION_INCREMENT;
			}
		}
		//System.out.println("Part " + name + " has item count of " + itemList.size());
		Collections.sort(itemList, posAndHigherNoteComparator);
		
	}

	private void addLiveCLip(LiveClip lc) {
		this.lc = lc;
		
		addNotes();
		addBarlinesBasedOnLoopParameters();
		//System.out.println("Part " + name + " has item count of " + itemList.size());
		Collections.sort(itemList, posAndHigherNoteComparator);
	}
	private void addBarlinesBasedOnLoopParameters() {
		addLoopStart();
		addLoopEnd();
		
	}
	
	private void addLoopEnd() {
		if (lc.loopEnd > 0.0){		// loopStart couls be at 0.0, but if loopEnd is at zero, these barlines are ignored
			itemList.add(new BarLine(lc.loopEnd, BarLine.LIGHT_LIGHT));
		}		
	}
	private void addLoopStart() {
		if (lc.loopEnd > 0.0){		// loopStart couls be at 0.0, but if loopEnd is at zero, these barlines are ignored
			itemList.add(new BarLine(lc.loopStart, BarLine.LIGHT_LIGHT));
		}		
	}
	private void addNotes() {
		for (LiveMidiNote lmn: lc.noteList){
			itemList.add(new PartNote(lmn));
		}		
	}
	private void init(String name){
		this.name = name;
		ID += partNumber;
		partNumber++;
		clef = MXM.getPreferredClefFromName(name);
	}
	public String toString(){
		String ret = "Part:" + name + "\n";
		for (PartItem p: itemList){
			ret += p.toString() + " actualPos=" + p.actualPos() + "\n";
		}
		return ret;
	}
	public String name(){
		return name;
	}
	public void addNote(PartNote partNote) {
		itemList.add(partNote);
		Collections.sort(itemList, posAndHigherNoteComparator);
	}
	public void addPartItem(PartItem pi){
		itemList.add(pi);
		Collections.sort(itemList, posAndHigherNoteComparator);
	}
	public void addTextDirection(String text, double pos, int placement){
		addPartItem(new PartDirection(text, pos, placement));
	}
	public void addTextDirection(String text, double pos, int placement, int textSize){
		addPartItem(new PartDirection(text, pos, placement, textSize));
	}
	public void addTextDirection(String text, double pos, int placement, int textSize,
			int yoffset) {
		addPartItem(new PartDirection(text, pos, placement, textSize, yoffset));
		
	}
	private void addTextDirection(String text, double pos, int placement, int textSize,
			int yoffset, int xoffset) {
		addPartItem(new PartDirection(text, pos, placement, textSize, yoffset, xoffset));
		
	}
	
// comparators ---------------------------------------------------------------------------------
	public static Comparator<PartItem> positionComparator = new Comparator<PartItem>(){
		public int compare(PartItem p1, PartItem p2){
			if (p1.position() < p2.position()) return -1;
			if (p1.position() > p2.position()) return 1;
			return 0;
		}
	};
	public static Comparator<PartItem> posAndHigherNoteComparator = new Comparator<PartItem>(){
		public int compare(PartItem p1, PartItem p2){
			if (p1.position() < p2.position()) return -1;
			if (p1.position() > p2.position()) return 1;
			if (p1.note() > p2.note()) return -1;
			if (p1.note() < p2.note()) return 1;
			return 0;
		}
	};
	public static Comparator<PartItem> higherNoteComparator = new Comparator<PartItem>(){
		public int compare(PartItem p1, PartItem p2){
			if (p1.note() > p2.note()) return -1;
			if (p1.note() < p2.note()) return 1;
			return 0;
		}
	};
// ----------------------------------------------------------------------------------------------
	public void addScorePartToStringList(ArrayList<String> strList, int indent){
		strList.add(MXM.getIndent(indent) + MXM.LB + MXM.SCORE_PART + MXM.ID_EQUALS + MXM.INVC + ID + MXM.INVC + MXM.RB);
		addPartNameToStringList(strList, indent + MXM.indentIncrement);
		strList.add(MXM.getIndent(indent + MXM.indentIncrement) + MXM.LB + MXM.BS + MXM.SCORE_PART + MXM.RB);
	}
	private void addPartNameToStringList(ArrayList<String> strList, int indent) {
		strList.add(MXM.getIndent(indent) + MXM.LB + MXM.PART_NAME + MXM.RB + name + MXM.LB + MXM.BS + MXM.PART_NAME + MXM.RB);		
	}
	public double getMeasuresRequired() {
		double d = 0.0;
		for (PartItem pi: itemList){
			double end = pi.position() + pi.length();
			if (end > d){
				d = end;
			}
		}
		return d;
	}
	
	public void addPartToStringList(ArrayList<String> strList, int indent){
		strList.add(MXM.getIndent(indent) + MXM.LB + MXM.PART + MXM.ID_EQUALS + MXM.INVC + ID + MXM.INVC + MXM.RB);
		
		addMeasuresToStringList(strList, indent + MXM.indentIncrement, divisions);
		strList.add(MXM.getIndent(indent + MXM.indentIncrement) + MXM.LB + MXM.BS + MXM.PART + MXM.RB);
	}
	private void addMeasuresToStringList(ArrayList<String> strList, int indent, int divisions) {
		for (XMLMeasure xm: measureList){
			xm.addMeasureToStringList(strList, indent, divisions);
		}
		
	}
	public void makeMeasures(MeasureMap measureMap, KeyMap keyMap) {
		measureList.clear();
		
		ArrayList<ArrayList<VoiceItem>> voiceItemList = makeVoices(measureMap);
		
//		doBeaming(voiceItemList);
		//System.out.println("there are " + voiceItemList.size() + " voices required ");
		
		makeXMLMeasureItems(measureMap, keyMap);
		
		//System.out.println("there are " + measureList.size() + " measures");
		addVoicesToMeasures(voiceItemList);
		addDirectionItemsToMeasures();
		doMeasureListBeaming();
		addBarlinesToMeasures();
		// addKeysSignatureToMeasures ????
	}
	private void doMeasureListBeaming() {
		for (XMLMeasure xmlm: measureList){
			//System.out.println("=========================\n" + xmlm.toString());
//			for (double d = xmlm.start; d < xmlm.end; d += 0.5){
//				
//				System.out.println(d + ": beamZone=" + beamZone(xmlm, d));
//			}
			for (Integer voiceIndex: xmlm.voiceItemMap.keySet()){
				ArrayList<VoiceItem> viList = xmlm.voiceItemMap.get(voiceIndex);
				boolean isBeaming = false;
				for (int i = 0; i < viList.size(); i++){
					VoiceItem vi = viList.get(i);
					VoiceItem previousVI = null;
					VoiceItem nextVI = null;
					if (i > 0) previousVI = viList.get(i - 1);
					if (i < viList.size() - 1) nextVI = viList.get(i + 1);
					if (!isBeaming){
						if (vi.isEighthLength() && !vi.isRest){
							if (nextVI != null && nextVI.isEighthLength()
									&& !nextVI.isRest
									&& MXM.closeEnough(nextVI.start() - 0.5, vi.start(), 0.01) 
									&& beamZone(xmlm, vi.start()) == beamZone(xmlm, nextVI.start())){
								vi.startBeam = true;
								isBeaming = true;
							}
						}
					} else {
						if (nextVI != null 
								&& nextVI.isEighthLength() 
								&& !nextVI.isRest 
								&& MXM.closeEnough(previousVI.start(), vi.start() - 0.5, 0.01)){
							if (beamZone(xmlm, vi.start()) == beamZone(xmlm, nextVI.start())){
								vi.continueBeam = true;
								
							} else {
								vi.endBeam = true;
								isBeaming = false;
							}
						
						} else {
							vi.endBeam = true;
							isBeaming = false;
						}
					}
				}
			}
		}
		
	}
	
	private int beamZone(XMLMeasure m, double pos){
		return (int)((pos - m.start) / beamingZoneSize);
	}
	private void addBarlinesToMeasures() {
		for (PartItem pi: itemList){
			if (pi.getType() == barLineType){
				addBarLineToMeasures(pi);
			}
		}
		
	}
	private void addBarLineToMeasures(PartItem pi) {
		if (measureList.size() > 0){
			measureList.get(0).addBarLine((BarLine)pi);
		}
		
	}
	private void doBeaming(ArrayList<ArrayList<VoiceItem>> voiceItemList) {
		VoiceItem previousVI = null;
		for (ArrayList<VoiceItem> vList: voiceItemList){
			previousVI = null;
			for (VoiceItem vi: vList){
				//System.out.println(vi.toString());
				if (vi.isOnStartEighth() && vi.isEighthLength()){
					//System.out.println("found viable item to start beaming");
					previousVI = vi;
				} else {
					if (previousVI != null && !vi.isRest){
					
						if (MXM.closeEnough(previousVI.start(), vi.start() - 0.5, 0.01)){
							previousVI.startBeam = true;
							vi.endBeam = true;
							previousVI = null;
						}
					}
				}
			}
		}
		
	}
	private void addDirectionItemsToMeasures() {
		for (PartItem pi: itemList){
			if (pi.getType() == directionType){
				addDirectionToMeasures(pi);
			}
		}
		
	}
	private void addDirectionToMeasures(PartItem pi) {
		if (measureList.size() > 0){
			measureList.get(0).addDirection((PartDirection)pi);
		}
		
	}
	private void addVoicesToMeasures(ArrayList<ArrayList<VoiceItem>> voiceItemList) {
		if (measureList.size() > 0){
			XMLMeasure xm = measureList.get(0);
			for (ArrayList<VoiceItem> viList: voiceItemList){
				
				for (VoiceItem vi: viList){
					//System.out.println("\nVoiceItem to Measures: -------------------");
					//System.out.println(vi.toString());
					xm.addVoiceItem(vi);
				}
			}
		}
		
		
	}
	private void makeXMLMeasureItems(MeasureMap measureMap, KeyMap keyMap){
		boolean b = true;
		boolean isFirstMeasure = true;
		XMLMeasure previousMeasure = null;
		int barCount = 1;
		double beatCount = 0.0;
		for (XMLTimeSignatureZone tsz: measureMap.tsZoneList){
			b = true;
			double barLength = CSD.getBarLength(tsz.beats, tsz.beatType);
			for (int i = 0; i < tsz.barCount; i++){
				
				XMLMeasure xm = new XMLMeasure(barCount, beatCount, beatCount + barLength, tsz, keyMap.getXMLKeyZone(barCount), clef);
				
				if (!isFirstMeasure){
					previousMeasure.nextMeasure = xm;
					
				} else {
					xm.key = keyMap.getXMLKeyZone(1);		// 1 - first measure
					xm.addAllAttributes();
				}
				if (keyMap.restateKey(barCount)){
					xm.hasAttributes = true;
					xm.hasClef = false;
					xm.hasTime = false;
					xm.hasKey = true;
					xm.key = keyMap.getXMLKeyZone(barCount);
				}
				isFirstMeasure = false;
				measureList.add(xm);
				previousMeasure = xm;
				b = false;
				barCount++;
				beatCount += barLength;
			}		
		}
//		for (XMLMeasure xm: measureList){
//			System.out.println(xm.toString());
//		}
	}
	public void setDivisionsValue(int divValue) {
		this.divisions  = divValue;
		
	}
	public ArrayList<ArrayList<VoiceItem>> makeVoices(MeasureMap measureMap){
		int voiceIndex = 0;
		ArrayList<ArrayList<PartItem>> voiceList = makeVoiceList();
		ArrayList<ArrayList<VoiceItem>> voiceItemList = makeVoiceItemList(voiceList, measureMap);
//		for (int i = 0; i < voiceList.size(); i++){
//			ArrayList<PartItem> pList = voiceList.get(i);
//			// continue here #############################################################################
//		
//		}
		return voiceItemList;
		
	}
	private ArrayList<ArrayList<VoiceItem>> makeVoiceItemList(ArrayList<ArrayList<PartItem>> voiceList, MeasureMap measureMap) {
		ArrayList<ArrayList<VoiceItem>> bigVIList = new ArrayList<ArrayList<VoiceItem>>();
		
		if (voiceList.size() == 1 && voiceList.get(0).size() == 0){
			addFullLengthRest(measureMap, bigVIList);
		} else {
			addNotesAndStuffAsUsual(voiceList, bigVIList, measureMap);
		}
		
//		systemOutBigVIList(bigVIList);
		return bigVIList;
	}
	private void addFullLengthRest(MeasureMap measureMap, ArrayList<ArrayList<VoiceItem>> bigVIList) {
		ArrayList<VoiceItem> viList = new ArrayList<VoiceItem>();
		viList.add(new VoiceItem(0.0, measureMap.endOfFile(), 1));   	// new rest strarting at 0.0 and the full length of the peice.
		bigVIList.add(viList);
	}
	private void addNotesAndStuffAsUsual(ArrayList<ArrayList<PartItem>> voiceList, ArrayList<ArrayList<VoiceItem>> bigVIList, MeasureMap measureMap) {
		double endOfFile = measureMap.endOfFile();		//will need to change with various time signatures, but currently assumes 4 beats in the bar
		int voice = 1;
		double counter = 0.0; 
		VoiceItem previousVoiceItem; 
		for (ArrayList<PartItem> piList: voiceList){
			counter = 0.0;
			previousVoiceItem = null;
			ArrayList<VoiceItem> viList = new ArrayList<VoiceItem>();
			Collections.sort(piList, Part.positionComparator);
			if (piList.size() > 0){
//				PartItem pi = piList.get(0);
//				if (pi.position() > 0.0){
//					viList.add(new VoiceItem(0.0, pi.position()));
//					counter = pi.position();
//				}
				for (PartItem pi: piList){
					//System.out.println(pi.toString() + " counter=" + counter);
					if (pi.position() + pi.length() <= endOfFile){
						if (pi.position() > counter){
							//System.out.println("preceding rest created start=" + counter + " end=" + pi.position());
							addPrecedingRest(counter, pi.position(), voice, viList);
							counter = pi.position();
						}
						if (previousVoiceItem == null){
							//System.out.println("voiceItem created start=" + pi.position() + " end=" + (pi.position() + pi.length()));
							VoiceItem vi = new VoiceItem(pi.position(), pi.position() + pi.length(), pi, voice);
							viList.add(vi);
							counter = pi.position() + pi.length();
							previousVoiceItem = vi;
						} else {
							if (previousVoiceItem.start() == pi.position() && previousVoiceItem.end() == pi.position() + pi.length()){
								//System.out.println(pi.toString() + " added to previous VoiceItem");
								previousVoiceItem.addPartItem(pi);
							} else {
								//System.out.println("voiceItem created start=" + pi.position() + " end=" + (pi.position() + pi.length()));
								VoiceItem vi = new VoiceItem(pi.position(), pi.position() + pi.length(), pi, voice);
								viList.add(vi);
								counter = pi.position() + pi.length();
								previousVoiceItem = vi;
							}
						}
					}
					
					
				}
				if (counter < endOfFile){
					//System.out.println("final rest created start=" + counter + " end=" + endOfFile);
					viList.add(new VoiceItem(counter, endOfFile, voice));
				}
			}
			
			
			bigVIList.add(viList);
			voice++;
		}
		
	}
	private void addPrecedingRest(double counter, double position, int voice, ArrayList<VoiceItem> viList) {
		viList.add(new VoiceItem(counter, position, voice));
		
	}
	private void systemOutBigVIList(ArrayList<ArrayList<VoiceItem>> bigVIList) {
		for (ArrayList<VoiceItem> viList: bigVIList){
			for (VoiceItem vi: viList){
				System.out.println(vi.toString());
			}
		}
		
	}
	private ArrayList<ArrayList<PartItem>> makeVoiceList() {
		// this splits the partItems into voices so there are no overlaps in one voice with different start and end times
		// also only does PartNotes
		ArrayList<ArrayList<PartItem>> voiceList = new ArrayList<ArrayList<PartItem>>();
		int currentIndex = 0;
		ArrayList<PartItem> pList;
		Collections.sort(itemList, higherNoteComparator);
		voiceList.add(new ArrayList<PartItem>());
		boolean flag = false;
		for (PartItem pi: itemList){
			currentIndex = 0;
			pList = voiceList.get(currentIndex);
			if (pi.getType() == Part.noteType){
				flag = false;
				while (!flag){
					if (noOverLaps(pList, pi)){
						pList.add(pi);
						flag = true;
					} else {
						currentIndex++;
						if (currentIndex >= voiceList.size()){
							voiceList.add(new ArrayList<PartItem>());
						}
						pList = voiceList.get(currentIndex);
					}
				}
				
			} 
		}
		//systemoutVoiceList(voiceList);
		return voiceList;
	}
	private void systemoutVoiceList(ArrayList<ArrayList<PartItem>> voiceList) {
		for (int i = 0; i < voiceList.size(); i++){
			ArrayList<PartItem> pList = voiceList.get(i);
			System.out.println("voice index=" + i + " size=" + pList.size());
			for (PartItem pi: pList){
				System.out.println(pi.toString());
			}
		}
		
	}
	private boolean noOverLaps(ArrayList<PartItem> pList, PartItem pi) {
		for (PartItem listItem: pList){
			if (overlaps(listItem, pi)){
				return false;
			}
		}
		return true;
	}
	
// PartItem types.........................................................................
	


	private boolean overlaps(PartItem listItem, PartItem pi) {
		double piPosPlus = pi.position() + pi.length();
		double listPos = listItem.position();
		double piPos =  pi.position();
		double listPosPlus = listItem.position() + listItem.length();
		
		if ((piPos < listPos && piPosPlus > listPos) || (piPos < listPosPlus && piPosPlus > listPosPlus)){
			return true;
		}
//		if (pi.position() + pi.length() <= listItem.position() || pi.position() >= listItem.position() + listItem.length()){
//			return false;
//		}
		return false;
	}

	public static final int noteType = 0;
	public static final int directionType = 1;
	public static final int barLineType = 2;

	
}
