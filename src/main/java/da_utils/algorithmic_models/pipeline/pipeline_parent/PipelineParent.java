package main.java.da_utils.algorithmic_models.pipeline.pipeline_parent;
import com.cycling74.max.Atom;

import main.java.da_utils.resource_objects.ResourceObject;



public interface PipelineParent {

//	public void postit(String str);
//	public void out(Atom[] atomArr);
//	public TwoBarRhythmBuffer getRhythmBuffer();
//	public TwoBarRhythmBuffer getInterlockBuffer();
//	public ChordForm getChordForm();
//	public ChordProgrammer2 getChordProgrammer();
//	public double getFormLength();
//	public int getDynamicIndex();
	public void sendPipelineMessage(Atom[] atArr);
	
	public void consolePrint(String str);
	public void postSplit(String str, String splitter);
	public ResourceObject ro();
//	public int dynamic();
}
