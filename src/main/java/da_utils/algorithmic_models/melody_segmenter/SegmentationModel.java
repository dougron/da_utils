package main.java.da_utils.algorithmic_models.melody_segmenter;

import java.util.ArrayList;

public interface SegmentationModel {

	public void instantiate(PhantomLiveClip plc);
	public ArrayList<Double> boundaryList();
	public double firstNotePosition();
	public String xmlTag();
}
