package main.java.da_utils.algorithmic_models.pipeline.plugins.bass;
import com.cycling74.max.Atom;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.max_atom_utils.DougzAtomUtilities;
import main.java.da_utils.algorithmic_models.pipeline.Pipeline;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_parent.PipelineParent;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;
import main.java.da_utils.resource_objects.ResourceObject;


public class PlugInBassAddEmbellishmentOneConsoleTest extends ConsoleProgram implements PipelineParent{
	
	public ChordScaleDictionary csd = new ChordScaleDictionary();
	public ResourceObject ro = new ResourceObject();	//placeholder for PipelineParent compatibility
	public ResourceObject ro(){
		return ro;
	}	
	public void run(){
		setSize(700, 900);
		
		
		Pipeline p = new Pipeline(0, this);
		PlayPlugArgument ppa = new PlayPlugArgument();
		PlugInBassAddEmbellishmentOne plug = new PlugInBassAddEmbellishmentOne();
		for (int i = 0; i < 25; i++){
			println("iteration: " + i + " +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			PipelineNoteList pnl = makePipelineNoteList();
			println(pnl.toString());
			plug.process(pnl, ppa);
			println("===========================================================");
			println(pnl.toString());
			println("===========================================================");
			println(pnl.makeLiveClip().toString());
		}
		
	}
	public void sendPipelineMessage(Atom[] atArr){
		println(DougzAtomUtilities.atomArrToString(atArr));
	}
	
	public void consolePrint(String str){
		println(str);
	}
	public void postSplit(String str, String splitter){
		String[] strArr = str.split(splitter);
		for (String s: strArr){
			println(s);
		}
	}
	
	private PipelineNoteList makePipelineNoteList(){
		PipelineNoteList pnl = new PipelineNoteList(0);
		pnl.length = 16.0;
		double[] pos = new double[]{0.0, 4.0, 7.0, 13.0};
		for (double d: pos){
			pnl.addNoteObject(d, true, true);
		}
		for (PipelineNoteObject pno: pnl.pnoList){
			
			pno.addNote(64);
			pno.setFixedVelocity(100);
			
		}
		return pnl;
	}

}
