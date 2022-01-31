package main.java.da_utils.algorithmic_models.pipeline.plugins.keys;
import java.util.ArrayList;
import java.util.Iterator;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.Embellishment;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.resource_objects.AccentTemplate;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;
import LegacyStuff.DAGlobals;


public class PlugInKeysFromInterlockBuffer extends Pluggable implements PipelinePlugIn{
	

	private static final int default_velocity = 100;
	private static final double default_noteLength = 0.25;

	private static final int centreOfGravityNote = 48;
	private static double default_legato_model = 0.0;
//	private ChordForm form;
//	private ResourceObject ro;
	
	private void doParams(){
		setName("Keys_IB");
		setSortIndex(10);
		setInputSort(0);
		setZone(0);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInKeysFromInterlockBuffer(){
		doParams();
//		this.ro = ro;
	}

	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
			pnl.length = ppa.cf.length();			// magic 4.0 is numerator of time signature. When cp does time signature this will emanate from there
//			TwoBarRhythmBuffer rb = ppa.rb;
//			double formLength = ro.cp.formLength;		
//			ChordForm form = ro.cp.getChordForm();

			addRhythmPositions(pnl.length, ppa.rb, pnl, ppa);
			doLengths(pnl);			
			addNotes(pnl);
//			doDefaultLengths(pnl);
			doVelocity(pnl, ppa);
		}
		
	}

// privates -----------------------------------------------------------------------------
	private void doVelocity(PipelineNoteList pnl, PlayPlugArgument ppa){		
		for (PipelineNoteObject pno: pnl.pnoList){
			pno.setFixedVelocity(DAGlobals.keysDynamic[ppa.dynamic]);
			pno.setLengthsToLegatoModel(default_legato_model);
		}
	}
	private void addNotes(PipelineNoteList pnl){
		for (PipelineNoteObject pno: pnl.pnoList){
			addClosestNotesToCentre(pno, centreOfGravityNote);
		}
	}
	private void addClosestNotesToCentre(PipelineNoteObject pno, int centre){
		for (int n: pno.ciko.getModChordTones()){
			int note = n % 12;
			int dist = 1000;
			for (int i = 0; i < 12; i++){		// 12 octaves.......
				if (Math.abs(centre - note) < dist){
					dist = Math.abs(centre - note);
					note += 12;
				} else {
					pno.addNote(note);
					break;
				}
			}
		}
	}
	private void doLengths(PipelineNoteList pnl){
		for (int i = 0; i < pnl.pnoList.size() - 1; i++){
			pnl.pnoList.get(i).length = pnl.pnoList.get(i + 1).position - pnl.pnoList.get(i).position;
		}
		pnl.pnoList.get(pnl.pnoList.size() - 1).length = pnl.length - pnl.pnoList.get(pnl.pnoList.size() - 1).position;
	}
	private void doDefaultLengths(PipelineNoteList pnl){
		for (PipelineNoteObject pno: pnl.pnoList){
			pno.setLengthsToLegatoModel(default_legato_model);
		}
	}
		
	private void addRhythmPositions(double formLength, TwoBarRhythmBuffer rb, PipelineNoteList pnl, PlayPlugArgument ppa){
		double inc = 0.0;
		boolean flag = true;
		while (flag){
			for (int i = 0; i < rb.buffy.length; i++){
				double pos = (double)i * 0.25;
				if (pos + inc >= formLength) {
					flag = false;
					break;
				}
				if (rb.buffy[i] == 1 && flag){
					PipelineNoteObject pno = new PipelineNoteObject(pos + inc, true, true);
					pno.ciko = ppa.cf.getPrevailingCIKO(pos + inc);	//0.0 = inject delay. not relevant as any other number in this case
					pnl.addNoteObject(pno);
				}
			}
			inc += 8.0;				// magic number = 2 bars, need to chenge when refactoring for other time signatures
		}
	}
	
	
}

