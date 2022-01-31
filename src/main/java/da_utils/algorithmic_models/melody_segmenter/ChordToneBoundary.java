package main.java.da_utils.algorithmic_models.melody_segmenter;

import java.util.ArrayList;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;

public class ChordToneBoundary  implements SegmentationModel {


	private double firstNotePosition = 0.0;
	ArrayList<Double> boundaryList = new ArrayList<Double>();

	@Override
	public void instantiate(PhantomLiveClip plc) {
		makeBoundaryList(plc);
		if (plc.lc.noteList.size() > 0){
			firstNotePosition = plc.lc.noteList.get(0).position;
		}
		
	}

	private void makeBoundaryList(PhantomLiveClip plc) {
		if (plc.cf != null){
			for (int index = plc.phantomStartIndex; index < plc.phantomEndIndex; index++){
				LiveMidiNote lmn = plc.lc.noteList.get(index);
				//############### continue here
			}
		}
		
	}

	@Override
	public ArrayList<Double> boundaryList() {
		return boundaryList;
	}

	@Override
	public double firstNotePosition() {
		return firstNotePosition;
	}

	@Override
	public String xmlTag() {
		return "CT";
	}

}
