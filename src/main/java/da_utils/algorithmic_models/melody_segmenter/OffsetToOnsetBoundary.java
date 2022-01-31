package main.java.da_utils.algorithmic_models.melody_segmenter;

import java.util.ArrayList;

public class OffsetToOnsetBoundary implements SegmentationModel {
	
	ArrayList<Double> boundaryList = new ArrayList<Double>();
	int roundingDecimals = 2;
	double firstNotePosition = 0.0;
	
	public OffsetToOnsetBoundary(){
		
	}
	public String xmlTag(){
		return "OOI";
	}

	public void instantiate(PhantomLiveClip plc) {
		makeBoundaryList(plc);
		if (plc.lc.noteList.size() > 0){
			firstNotePosition = plc.lc.noteList.get(0).position;
		}
	}

	private void makeBoundaryList(PhantomLiveClip plc) {
		for (int index = plc.phantomStartIndex; index < plc.phantomEndIndex; index++){
			double previousOOI = Math.round(plc.offsetToOnsetIntervalList.get(index - 1) * Math.pow(10, roundingDecimals)) / Math.pow(10, roundingDecimals);
			double currentOOI = Math.round(plc.offsetToOnsetIntervalList.get(index) * Math.pow(10, roundingDecimals)) / Math.pow(10, roundingDecimals);
			double nextOOI = Math.round(plc.offsetToOnsetIntervalList.get(index + 1) * Math.pow(10, roundingDecimals)) / Math.pow(10, roundingDecimals);
			if (currentOOI - previousOOI > 0 && nextOOI - currentOOI < 0){
				boundaryList.add(plc.noteList.get(index).position);
			}
		}
		
	}
	
	public String toString(){
		String str = "OffsetToOnsetPeakBoundary list: ";
		for (Double d: boundaryList){
			str += d + ", ";
		}
		return str;
	}

	@Override
	public ArrayList<Double> boundaryList() {
		return boundaryList;
	}
	@Override
	public double firstNotePosition() {
		return firstNotePosition;
	}

}
