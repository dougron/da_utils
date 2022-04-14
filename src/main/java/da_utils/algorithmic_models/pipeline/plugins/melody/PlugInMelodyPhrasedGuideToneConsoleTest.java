package main.java.da_utils.algorithmic_models.pipeline.plugins.melody;
import acm.program.ConsoleProgram;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;
import main.java.da_utils.four_point_contour.FourPointContour;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.ContourPack;
import main.java.da_utils.resource_objects.RandomNumberSequence;
import main.java.da_utils.test_utils.TestData;
import acm.program.ConsoleProgram;

public class PlugInMelodyPhrasedGuideToneConsoleTest extends ConsoleProgram{

	
	public void run(){
		//setSize(900, 700);
		PlayPlugArgument ppa = new PlayPlugArgument();
		addContourPack(ppa);
		addChordForm(ppa);
		
		PipelinePlugIn ppi = new PlugInMelodyPhrasedGuideTone();
		ppi.process(new PipelineNoteList(0), ppa);
		
	}
	
// privates -------------------------------------------------------
	private void addChordForm(PlayPlugArgument ppa){
		ppa.cf = new ChordForm(TestData.liveClipForTestForm());
	}
	private void addContourPack(PlayPlugArgument ppa){
		ContourPack cp = new ContourPack(new FourPointContour(0.3, 1.0, -1.0));
		cp.addToContourList(new FourPointContour(FourPointContour.UP));
		cp.addToContourList(new FourPointContour(FourPointContour.DOWN));
		cp.addToContourList(new FourPointContour(FourPointContour.UPDOWN));
		cp.addToContourList(new FourPointContour(FourPointContour.DOWNUP));
		ppa.cp = cp;
	}
}
