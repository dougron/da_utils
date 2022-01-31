package main.java.da_utils.xml_maker;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.xml_maker.key.XMLKeyZone;
import main.java.da_utils.xml_maker.measure.XMLMeasure;
import main.java.da_utils.xml_maker.part_items.Part;
import main.java.da_utils.xml_maker.part_items.PartNote;
import main.java.da_utils.xml_maker.time_signature.XMLTimeSignatureZone;

public class MusicXMLMakerConsoleTest extends ConsoleProgram {

	
	private static final String TPT = "tpt";
	private String path = "D:/_DALooperTXT/XMLMakerFiles/test.xml";

	public void run(){
		setSize(700, 700);
		MusicXMLMaker mx = new MusicXMLMaker(MXM.KEY_OF_C);
		mx.measureMap.addNewTimeSignatureZone(new XMLTimeSignatureZone(4, 4, 4)); 
//		mx.measureMap.addNewTimeSignatureZone(new TimeSignatureZone(3, 4, 4)); 
		mx.keyMap.addNewKeyZone(new XMLKeyZone(MXM.KEY_OF_C, 4));

		mx.addPart("tpt", make3rdLiveClip(new int[]{60, 57}, 0.0, 4, 0.25));
		mx.addPart("alto");
		
		mx.addTextDirection(TPT, "Pooooooopy", 4.0, MXM.PLACEMENT_ABOVE);
		
		mx.makeXML(path);
		for (Part p: mx.partList){
			println("part " + p.name());
			for (XMLMeasure m: p.measureList){
				println(m.toString());
				println("-----------");
			}
		}
		
	}

	private void addNotes(Part part) {
		for (double d = 0; d < 10; d += 1.0){
			part.addNote(new PartNote(new LiveMidiNote(60, d, 0.25, 100, 0)));
		}
		
	}
	private LiveClip make2ndTestClip(){
		LiveClip lc = new LiveClip(0, 0);
		lc.addNote(new LiveMidiNote(60, 0.0, 0.25, 100, 0));
		lc.addNote(new LiveMidiNote(60, 0.25, 0.25, 100, 0));
		lc.addNote(new LiveMidiNote(60, 0.5, 0.25, 100, 0));
		lc.addNote(new LiveMidiNote(60, 0.75, 0.25, 100, 0));
		
		lc.addNote(new LiveMidiNote(60, 1.0, 0.25, 100, 0));
		lc.addNote(new LiveMidiNote(60, 1.25, 0.25, 100, 0));
		lc.addNote(new LiveMidiNote(60, 1.5, 0.25, 100, 0));
		lc.addNote(new LiveMidiNote(60, 1.75, 0.25, 100, 0));
		
		lc.addNote(new LiveMidiNote(60, 2.0, 0.25, 100, 0));
		lc.addNote(new LiveMidiNote(60, 2.25, 0.25, 100, 0));
		lc.addNote(new LiveMidiNote(60, 2.5, 0.25, 100, 0));
		lc.addNote(new LiveMidiNote(60, 2.75, 0.25, 100, 0));
		
		lc.addNote(new LiveMidiNote(60, 3.0, 0.25, 100, 0));
		lc.addNote(new LiveMidiNote(60, 3.25, 0.25, 100, 0));
		lc.addNote(new LiveMidiNote(60, 3.5, 0.25, 100, 0));
		lc.addNote(new LiveMidiNote(60, 3.75, 0.25, 100, 0));
		return lc;
	}
	private LiveClip make3rdLiveClip(int[] notes, double start, int count, double posIncrement){
		LiveClip lc = new LiveClip(0, 0);
		lc.loopStart = 0.0;
		lc.loopEnd = 8.0;
		double pos = start;
		for (int i = 0; i < count; i++){
			for (int note: notes){
				LiveMidiNote lmn = new LiveMidiNote(note, pos, posIncrement, 100, 0);
				lc.addNote(lmn);
			}
			pos += posIncrement;
		}
		return lc;
	}
	private LiveClip makeTestClip(){
		LiveClip lc = new LiveClip(0, 0);
		lc.loopStart = 0.0;
		lc.loopEnd = 8.0;
		lc.addNote(new LiveMidiNote(60, 1.0, 2.0, 100, 0));
		lc.addNote(new LiveMidiNote(59, 3.0, 1.0, 100, 0));
		lc.addNote(new LiveMidiNote(57, 4.0, 3.0, 100, 0));
		lc.addNote(new LiveMidiNote(53, 4.0, 3.0, 100, 0));
		lc.addNote(new LiveMidiNote(57, 8.5, 5.5, 100, 0));
		lc.addNote(new LiveMidiNote(60, 12.0, 4.0, 100, 0));
		return lc;
	}
}
