package main.java.da_utils.algorithmic_models.pipeline.plugins.bass;
//import com.cycling74.max.MaxObject;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments.ED;
import main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments.PlugInAddEmbellishmentRhythm;
import main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments.PlugInAssignEmbellishment;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;


public class PlugInBassAddEmbellishmentOne extends Pluggable implements PipelinePlugIn{
	
	// name changed to "EmbOne" as this is a generic embellishment plugin that can be usede for anything
	
	private Pluggable[] plugArr; 
	
	private static int staticID = 0;
	private int ID;
	
	
	private void doParams(){
		setName("EmbOne");
		setSortIndex(0);
		setInputSort(0);
		setZone(1);
		setCanDouble(true);
		setActive(1);
	}
	
	public PlugInBassAddEmbellishmentOne(){
		doParams();
		plugArr = new Pluggable[]{
				new PlugInAddEmbellishmentRhythm(new double[]{-0.25, -0.5},
						new double[]{1.0, 3.0},
							1.0),
				new PlugInAssignEmbellishment(new ED[]{new ED("s", 0), new ED("s", -1), new ED("s", -5)},
						new double[]{1.0, 1.0, 1.0})			
		};
		makeID();
	}
	public PlugInBassAddEmbellishmentOne(
			double[] embellishmentRhythmOffset,
			double[] embellishmentRhythmOffsetChance,
			double chanceOfEmbellishment,
			ED[] edArr,
			double[] embellishmentDescriptorChance){
		doParams();
		//ED[] edArr = makeEDArr(embellishmentDescriptor);
		makePlugArr(embellishmentRhythmOffset, embellishmentRhythmOffsetChance, chanceOfEmbellishment, edArr, embellishmentDescriptorChance);
		makeID();
	}
	public PlugInBassAddEmbellishmentOne(
			double[] embellishmentRhythmOffset,
			double[] embellishmentRhythmOffsetChance,
			double chanceOfEmbellishment,
			ED[] edArr,
			double[] embellishmentDescriptorChance,
			String customName){
		doParams();
		makePlugArr(embellishmentRhythmOffset, embellishmentRhythmOffsetChance, chanceOfEmbellishment, edArr, embellishmentDescriptorChance);
		makeID();
		this.customName = customName;
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
//		MaxObject.post("PlugInBassAddEmbellishmentOne.process() with PipelineNoteList of size: " + pnl.pnoList.size());
//		postPNL(pnl);
		for (Pluggable ppi: plugArr){
			ppi.process(pnl, ppa);
//			MaxObject.post(ppi.name() + " just been run in PlugInBassAddEmbellishmentOne. results follow.....");
//			postPNL(pnl);
//			MaxObject.post("-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-");
		}
	}
	public String name(){
		return name + ID + customName;
	}
	@Override
	public String originalName(){
		return name();
	}
	

// privates -----------------------------------------------------------------------------
	private void makeID(){
		ID = staticID;
		staticID++;
	}
	private void makePlugArr(
			double[] embellishmentRhythmOffset,
			double[] embellishmentRhythmOffsetChance,
			double chanceOfEmbellishment,
			ED[] edArr,
			double[] embellishmentDescriptorChance){
		plugArr = new Pluggable[]{
				new PlugInAddEmbellishmentRhythm(
						embellishmentRhythmOffset,
						embellishmentRhythmOffsetChance,
						chanceOfEmbellishment),
				new PlugInAssignEmbellishment(
						edArr,
						embellishmentDescriptorChance)			
		};
	}
	private ED[] makeEDArr(Object[][] edObjArr){
		ED[] edArr = new ED[edObjArr.length];
		int index = 0;
		for (Object[] edObj: edObjArr){
			ED ed = new ED((String)edObj[0], (Integer)edObj[1]);
			edArr[index] = ed;
			index++;
		}
		return edArr;
	}
	
	private void postPNL(PipelineNoteList pnl){
//		MaxObject.post("PlugInBassAddEmbellishmentOne PipelineNoteList.toString -----------------");
		String[] splitPost = pnl.toString().split("\n");
		for (String str: splitPost){
//			MaxObject.post(str);
		}
		
	}
}
