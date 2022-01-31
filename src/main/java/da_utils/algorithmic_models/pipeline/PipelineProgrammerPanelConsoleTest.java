package main.java.da_utils.algorithmic_models.pipeline;
import com.cycling74.max.Atom;

import acm.program.ConsoleProgram;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_parent.PipelineParent;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestAddEmbellishment;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestAddTwoMoreVoicesPlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestLegatoPlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestMakeGuideTone;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestVelocityPlugIn;
import main.java.da_utils.resource_objects.ResourceObject;


public class PipelineProgrammerPanelConsoleTest extends ConsoleProgram implements PipelineParent{

	public ResourceObject ro = new ResourceObject();	//placeholder for PipelineParent compatibiolituy
	public void run(){
		setSize(900, 700);
		Pipeline p = new Pipeline(0, this);
		makePipeline(p);
		println(p.toString());
		
		p.openPanel();
		println("IT DID NOT EXIT BEFORE FINISHING");	
		println(p.toString());
	}
	private void makePipeline(Pipeline pl){
		pl.addPlugInOption(new TestMakeGuideTone());
		pl.addPlugInOption(new TestAddTwoMoreVoicesPlugIn());
		pl.addPlugInOption(new TestAddEmbellishment());
		pl.addPlugInOption(new TestLegatoPlugIn(new double[]{0.0, 1.0, 0.5}));
		pl.addPlugInOption(new TestVelocityPlugIn());
		
	}
// PipelineParent methods -----------------------------------------------------------------
	public void sendPipelineMessage(Atom[] atArr){}
	
	public void consolePrint(String str){}
	public void postSplit(String str, String splitter){}
	public ResourceObject ro(){
		return ro;
	}
}
