package main.java.da_utils.algorithmic_models.pipeline.plugins.bass;
//import com.cycling74.max.MaxObject;


import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.resource_objects.AccentTemplate;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;


public class PlugInBassFromRhythmBuffer extends Pluggable implements PipelinePlugIn{
	

	private double default_rhythmBuffer_loopLength = 32.0;
	private static final int[] loBassRegister = new int[]{36, 55};
	private static final int default_velocity = 100;
	private static final double default_noteLength = 0.25;
	private static final int[] dynamicVelocity = new int[]{40, 65, 90, 127}; 
	private int previousNote = 0;
//	private TwoBarRhythmBuffer rb;
//	private ChordForm form;
//	private ResourceObject ro;
	
	
	private void doParams(){
		setName("Ba_RB");
		setSortIndex(0);
		setInputSort(0);
		setZone(0);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInBassFromRhythmBuffer(){
		doParams();
//		this.ro = ro;
	}
	public PlugInBassFromRhythmBuffer(TwoBarRhythmBuffer rb, ChordForm form){
		doParams();
		
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
			pnl.length = ppa.cf.length();
			TwoBarRhythmBuffer rb = ppa.rb;
			double formLength = pnl.length;		//ro.cp.formLength;		
			ChordForm form = ppa.cf;

//			p.parent.post("PlugInBassFromRhythmBuffer() has this RhythmBuffer.......");
//			p.parent.post(rb.buffyToString());
//			p.parent.post("PlugInBassFromRhythmBuffer() has this Form.......");
//			p.parent.postSplit(form.toString(), "\n");

			addRhythmPositions(formLength, ppa, pnl);
			addChordAnalysis(pnl, form);
			addNotes(pnl);
			doVelocity(pnl, ppa);
		}
		
	}

	
// privates -----------------------------------------------------------------------------
	private void doVelocity(PipelineNoteList pnl, PlayPlugArgument ppa){		
		for (PipelineNoteObject pno: pnl.pnoList){
			pno.setFixedVelocity(dynamicVelocity[ppa.dynamic]);
		}
	}
	private void addNotes(PipelineNoteList pnl){
		for (PipelineNoteObject pno: pnl.pnoList){
			pno.addNote(closestNoteInRegister(pno, loBassRegister));
		}
	}
	private int closestNoteInRegister(PipelineNoteObject pno, int[] register){
		int note;
		if (pno.ciko != null){
			note = pno.ciko.rootIndex;
			while (note < register[0]){
				note += 12;
			}
			previousNote = note;
		} else {
			note = previousNote;
		}		
		return note;
	}
	
	private void addChordAnalysis(PipelineNoteList pnl, ChordForm form){
		for (PipelineNoteObject pno: pnl.pnoList){
//			pno.cao = form.getPrevailingChordAnalysisObject(pno.position, 0.0);		//0.0 - injectDelay, in this case 0.0
			pno.ciko = form.getPrevailingCIKO(pno.position, pnl);
		}
	}
	private void addRhythmPositions(double formLength, PlayPlugArgument ppa, PipelineNoteList pnl){
		if (ppa.at != null){
			addRhythmPositionsAT(formLength, ppa.at, pnl);
		} else if (ppa.rb != null){
			addRhythmPositionsRB(formLength, ppa.rb, pnl);
		} else {
			System.out.println("PlugInBassFromRhythmBuffer: neither ppa.at nor pp.rb present");
		}
	}
	private void addRhythmPositionsAT(double formLength, AccentTemplate at, PipelineNoteList pnl){
		double inc = 0.0;
		boolean flag = true;
		while (flag){
			for (LiveMidiNote lmn: at.clip.noteList){
				double pos = inc + lmn.position;
				if (pos >= formLength){
					flag = false;
				} else {
					pnl.addNoteObject(pos, true, true);
				}
			}
			
			inc += at.length;
		}
	}
	
	private void addRhythmPositionsRB(double formLength, TwoBarRhythmBuffer rb, PipelineNoteList pnl){
		// addds rhythm positions when a TwoBarRhythmBuffer is present
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
					pnl.addNoteObject(pos + inc, true, true);
				}
			}
			inc += 8.0;				// magic number = 2 bars, need to chenge when refactoring for other time signatures
		}
	}

}
