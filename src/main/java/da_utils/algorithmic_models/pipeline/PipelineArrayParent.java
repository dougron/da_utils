package main.java.da_utils.algorithmic_models.pipeline;
import com.cycling74.max.Atom;

import main.java.da_utils.resource_objects.ResourceObject;




public interface PipelineArrayParent {

	public void sendPipelineMessage(Atom[] atArr);
	
	public void consolePrint(String str);
	public void postSplit(String str, String splitter);
	public ResourceObject ro();

}
