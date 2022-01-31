package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.Embellishment;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;


public class PlugInEscapeNote extends Pluggable implements PipelinePlugIn{
	
	public Embellishment[] embOptionArr; 
	private double[] weighting;
	private double weightingSum;

	private double percentageOfEmbellishingVelocity = 0.8;		// if embellished note is a Guide tone
	private Embellishment embForUpContour;
	private Embellishment embForDownContour;
//	private ResourceObject ro;
	
	private void doParams(){
		setName("Note for EscapeTone");
		setSortIndex(20);
		setInputSort(0);
		setZone(1);
		setCanDouble(true);
		setActive(1);
	}
	
	public PlugInEscapeNote(ED embForUpContour, ED embForDownContour){
		doParams();
//		this.ro = ro;
		makeEmbellishments(embForUpContour, embForDownContour);		
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		for (int i = 0; i < pnl.pnoList.size(); i++){
			PipelineNoteObject pno = pnl.pnoList.get(i);			
			if (pno.isEmbellishable && pno.pnoEmbellishing != null){
				PipelineNoteObject nextpno = pnl.pnoList.get(0);
				PipelineNoteObject lastpno = pnl.pnoList.get(pnl.pnoList.size() - 1);
				if (i != pnl.pnoList.size() - 1){
					nextpno = pnl.pnoList.get(i + 1);
				} 
				if (i != 0){
					lastpno = pnl.pnoList.get(i - 1);
				}
				pno.embellishmentType = getEmbellishment(lastpno, nextpno);
				pno.addNote(pno.embellishmentType.getNote(pno.pnoEmbellishing, ppa));
				if (pno.pnoEmbellishing.isGuideTone){
					pno.setFixedVelocity((int)(pno.pnoEmbellishing.velocity * percentageOfEmbellishingVelocity));
				} else {
					pno.setFixedVelocity((int)(pno.pnoEmbellishing.velocity));
				}
				
			}
		}
	}
	

// privates ------------------------------------------------------------------
	private Embellishment getEmbellishment(PipelineNoteObject pno, PipelineNoteObject nextpno){
		if (pno.averageNoteValue() > nextpno.averageNoteValue()){
			return embForDownContour;
		}
		return embForUpContour;
	}
	
	private void makeEmbellishments(ED embForUpContour, ED embForDownContour){
		this.embForDownContour = selectEmbellishment(embForDownContour);
		this.embForUpContour = selectEmbellishment(embForUpContour);
	}
	private Embellishment selectEmbellishment(ED ed){
		if (ed.type.equals("s")){
			return new SemitoneEmbellishment(ed.value);
		} else if (ed.type.equals("d")){
			return new DiatonicEmbellishment(ed.value);
		} else if (ed.type.equals("c")){
			return new ChordToneEmbellishment(ed.value);
		}
		return new ChordToneEmbellishment(ed.value);
	}

}
