package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;
import acm.program.ConsoleProgram;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;
import main.java.da_utils.resource_objects.RandomNumberSequence;

public class PlugInSyncopateConsoleTest extends ConsoleProgram{
	
	public void run(){
		ChordScaleDictionary csd = new ChordScaleDictionary();
		double syncopationChance = 1.0;
		PlugInSyncopate ps = new PlugInSyncopate(new double[]{-0.5, 0.5}, new double[]{0.5}, syncopationChance);
		PipelineNoteList pnl = new PipelineNoteList(0);
		PipelineNoteObject pno = new PipelineNoteObject(0.0, true, true);
		pno.addNote(60);
		pnl.addNoteObject(pno);
		pnl.length = 4.0;
		PlayPlugArgument ppa = new PlayPlugArgument();
		ppa.rnd = new RandomNumberSequence(16, 5);
		System.out.println(pnl.toString());
		System.out.println("=======================================\n====================================");
		ps.process(pnl, ppa);
		System.out.println(pnl.toString());
	}

}
