package main.java.da_utils.algorithmic_models.pipeline.plugins.controllers;


import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.ControllerClip;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

public class PlugInSlowWah extends Pluggable implements PipelinePlugIn{
	
	
	
	private static final double keysLPFreqOffValue = 0;

	public String name = "SloWah";

	private double length = 4.0;
	
	private int controllerIndex;
	private String controllerName = "yyy";
	
	private double startValue = 0.1;
	private double highValue = 1.0;
	private double highPointPos = 2.0;
	
	
	private void doParams(){
		setName("SloWah");
		setSortIndex(50);
		setInputSort(0);
		setZone(2);
		setCanDouble(false);
		setActive(1);
	}
	public PlugInSlowWah(int controllerIndex, String name){
		doParams();
		this.controllerIndex = controllerIndex;
		this.controllerName = name;
	}
	public PlugInSlowWah(int controllerIndex){
		doParams();
		this.controllerIndex = controllerIndex;
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
//		if (pnl.pnoList.size() > 0){
			ControllerClip cc = new ControllerClip(ppa.clipObjectIndex, controllerIndex, length);
			cc.name = controllerName;
			if (active > 0){			
				cc.addBreakPoint(0.0, startValue);				// startpoint
				cc.addBreakPoint(highPointPos, highValue);		// midpoint
				cc.addBreakPoint(length, startValue);			// endpoint				
			} else {
				cc.setOff(keysLPFreqOffValue);
			}
			pnl.addControllerClip(cc);
//		}
		
		
	}

// privates ------------------------------------------------------------------------------
	

}
