package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;
import acm.program.ConsoleProgram;
import main.java.da_utils.algorithmic_models.pipeline.Pipe;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.melody.PlugInMelodyContourGuideToneEveryBar;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.ContourData;
import main.java.da_utils.resource_objects.RandomNumberSequence;
import main.java.da_utils.test_utils.TestData;

public class PlugInSizeAndLengthContextEmbellisherConsoleTest extends ConsoleProgram{

	public void run(){
		setSize(700, 900);
		PlugInSizeAndLengthContextEmbellisher plug = new PlugInSizeAndLengthContextEmbellisher();
		testChancyItem(plug);
	}
	private void testChancyItem(PlugInSizeAndLengthContextEmbellisher plug){
		double[] optionArr = new double[]{0.1, 0.2, 0.3};
		double[] chanceArr = new double[]{0.2};
		for (double chance = 0.0; chance < 1.0; chance += 0.1){
			double item = plug.getChancyItem(optionArr, chanceArr, chance);
			String chanceStr = String.format("%.2f", chance);
			println(chanceStr + ": " + item);
		}
		
		Pipe p = new Pipe("test");
		p.addPlugIn(new PlugInMelodyContourGuideToneEveryBar());
		println(p.toString());
		
		PlayPlugArgument ppa = makePPA();
		PipelineNoteList pnl = p.makeNoteList(ppa);
		println("--------------------------\n" + pnl.toString());
		
		p.addPlugIn(new PlugInSizeAndLengthContextEmbellisher());
		pnl = p.makeNoteList(ppa);
		println("--------------------------\n" + pnl.toString());
	}
	
	private PlayPlugArgument makePPA(){
		ChordForm cf = new ChordForm(TestData.chordProgressionAmGFE7());
		ContourData cd = new ContourData();
		RandomNumberSequence rnd = new RandomNumberSequence(16, 1);
		println(cd.toString());
		
		
		PlayPlugArgument ppa = new PlayPlugArgument();
		ppa.cf = cf;
		ppa.cd = cd;
		ppa.rnd = rnd;
		
		ppa.numberOfEmbellishments = 2;
		return ppa;
	}
}
