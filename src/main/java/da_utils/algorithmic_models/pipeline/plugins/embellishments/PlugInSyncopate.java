package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;

import main.java.da_utils.algorithmic_models.pipeline.Pipe;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.bass.PlugInBassRootEveryBar;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.RandomNumberSequence;
import main.java.da_utils.test_utils.TestData;



public class PlugInSyncopate extends Pluggable implements PipelinePlugIn{


//	public Embellishment[] embOptionArr; 
	private double[] weighting;
	private double weightingSum;
	private double[] offset;
	private double syncopationChance = 1.0;

//	private double percentageOfEmbellishingVelocity = 0.8;		// if embellished note is a Guide tone
//	private ResourceObject ro;
	
	private void doParams(){
		setName("PlugInSyncopate");
		setSortIndex(20);
		setInputSort(0);
		setZone(1);
		setCanDouble(true);
		setActive(1);
	}
	
	public PlugInSyncopate(double[] offset, double[] weighting, double syncopationChance){
		doParams();
//		this.ro = ro;
//		sortOutWeighting(edArr.length, weighting);
		this.weighting = weighting;
		this.offset = offset;
		weightingSum = makeWeightingSum();
		this.syncopationChance = syncopationChance;
//		embOptionArr = makeEmbellishmentOptions(edArr);
		
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
//		MaxObject.post("PlugInAssignEmbellishment.process() called");
		if (active == 1){
			for (PipelineNoteObject pno: pnl.pnoList){
				double nextRND = ppa.rnd.next();
				if (nextRND <= syncopationChance){
					double chosenOffset = chooseOffset(ppa);
					if (chosenOffset != 0){
						double pos = pno.position + chosenOffset;
						if (pos < 0) pos = pos + pnl.length;
						if (!containsPosition(pos, pnl)){
							pno.setPosition(pos);
						}
					}
				}
			}
		}
	}
	
	public String name(){
		return name;
	}
	public String originalName(){
		return name();
	}
	public int sortIndex(){
		return sortIndex;
	}
	public int inputSort(){
		return inputSort;
	}
	public int zone(){
		return zone;
	}
	public boolean canDouble(){
		return canDouble;
	}
	public void setInputSort(int i){
		inputSort = i;
	}
	public int active(){
		return active;
	}
	public void setActive(int i){
		active = i;
	}
	public FilterObject getFilterObject(){
		return new FilterObject(originalName(), this);
	}
	
// privates ----------------------------------------------------------------------------------

	private boolean containsPosition(double pos, PipelineNoteList pnl){
		for (PipelineNoteObject pno: pnl.pnoList){

			if (pno.position == pos){
				return true;
			}
		}
		return false;
	}
	private double chooseOffset(PlayPlugArgument ppa){
		int chanceIndex = 0;
		double weightingRunningTotal = 0;
		for (double d: offset){
			double currentChance = weighting[chanceIndex];			// for debugging
			double nextRND = ppa.rnd.next();
			weightingRunningTotal += weighting[chanceIndex] / weightingSum;
			if (nextRND <= weightingRunningTotal){
				return d;
			}
			
			chanceIndex++;
			if (chanceIndex >= weighting.length) chanceIndex = 0;		// solve the problem if the arrays are not the same length
		}
		return 0;
	}
	

	private double makeWeightingSum(){
		// compensates for the possibility that weighting array is not as long as offset array, in which case it wraps around
		double sum = 0.0;
		int chanceIndex = 0;
		for (double dd: offset){
			sum += weighting[chanceIndex];
			chanceIndex++;
			if (chanceIndex >= weighting.length) chanceIndex = 0;		// solve the problem if the arrays are not the same length

		}
		return sum;
	}

	
	public static void main(String[] args){
		Pipe p = new Pipe("test");
		ChordForm cf = new ChordForm(TestData.chordProgressionAmGFE7());
		PlayPlugArgument ppa = new PlayPlugArgument();
		RandomNumberSequence rnd = new RandomNumberSequence(16, 0);
		ppa.cf = cf;
		ppa.rnd = rnd;
		
		p.addPlugIn(new PlugInBassRootEveryBar());
		
		PipelineNoteList pnl = p.makeNoteList(ppa);
		System.out.println(pnl.toString());
		
		p.addPlugIn(new PlugInSyncopate(new double[]{-0.5, 0.5}, new double[]{0.5, 0.5}, 1.0));
		pnl = p.makeNoteList(ppa);
		System.out.println(pnl.toString());
		System.out.println(p.toString());
	}
}
