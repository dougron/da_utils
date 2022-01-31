package ConsoleTests;

import acm.program.ConsoleProgram;
import main.java.da_utils.static_chord_scale_dictionary.CSD;
import main.java.da_utils.xml_maker.MXM;
import main.java.da_utils.xml_maker.key.XMLKey;
import main.java.da_utils.xml_maker.measure.XMLMeasure;
import main.java.da_utils.xml_maker.rhythm.RhythmOption;
import main.java.da_utils.xml_maker.time_signature.XMLTimeSignatureZone;

public class XMLMeasureRhythmOptionsConsoleTest extends ConsoleProgram {

	public void run(){
		setSize(700, 700);
		int[] ts = new int[]{5, 4};
		int measureNumber = 1;
		double start = 0.0;
		double end = start + CSD.getBarLength(ts[0], ts[1]);
		println("start=" + start + " end=" + end);
		XMLTimeSignatureZone tsz = getTimeSignatureZone(ts, 4);
		XMLKey key = MXM.xmlKeyMap.get(0);
		XMLMeasure xmlm = new XMLMeasure(measureNumber, start, end, tsz, key, MXM.getPreferredClefFromName(""));
		println(xmlm.toString());
		int count = 0;
		for (RhythmOption ro: xmlm.rhythmOptionNotesList){
			println(ro.toString());
			count++;
		}
		println("count=" + count);
		println("xmlm.rhythmOptionNotesList.size()=" + xmlm.rhythmOptionNotesList.size());

		count = 0;
		for (RhythmOption ro: xmlm.rhythmOptionRestsList){
			println(ro.toString());
			count++;
		}
		println("count=" + count);
		println("xmlm.rhythmOptionRestsList.size()=" + xmlm.rhythmOptionRestsList.size());

		print("done");
	}

	private XMLTimeSignatureZone getTimeSignatureZone(int[] ts, int i) {
		return new XMLTimeSignatureZone(ts[0], ts[1], i);

	}
}
