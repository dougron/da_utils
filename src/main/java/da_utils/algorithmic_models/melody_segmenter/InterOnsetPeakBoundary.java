package main.java.da_utils.algorithmic_models.melody_segmenter;

import java.util.ArrayList;

public class InterOnsetPeakBoundary implements SegmentationModel {

	//private PhantomLiveClip plc;
	ArrayList<Double> boundaryList = new ArrayList<Double>();
	int roundingDecimals = 2;
	double firstNotePosition = 0.0;
	
	public InterOnsetPeakBoundary(){
		
	}
	public String xmlTag(){
		return "IOI";
	}

	public void instantiate(PhantomLiveClip plc){
		//this.plc = plc;
		makeBoundaryList(plc);
		if (plc.lc.noteList.size() > 0){
			firstNotePosition = plc.lc.noteList.get(0).position;
		}
		
	}
	

	private void makeBoundaryList(PhantomLiveClip plc) {
		for (int index = plc.phantomStartIndex; index < plc.phantomEndIndex; index++){
			double previousIOI = Math.round(plc.interOnsetIntervalList.get(index - 1) * Math.pow(10, roundingDecimals)) / Math.pow(10, roundingDecimals);
			double currentIOI = Math.round(plc.interOnsetIntervalList.get(index) * Math.pow(10, roundingDecimals)) / Math.pow(10, roundingDecimals);
			double nextIOI = Math.round(plc.interOnsetIntervalList.get(index + 1) * Math.pow(10, roundingDecimals)) / Math.pow(10, roundingDecimals);
			if (currentIOI - previousIOI > 0 && nextIOI - currentIOI < 0){
				boundaryList.add(plc.noteList.get(index).position);
			}
		}		
	}
	
	public String toString(){
		String str = "InterOnsetPeakBoundary list: ";
		for (Double d: boundaryList){
			str += d + ", ";
		}
		return str;
	}

	public ArrayList<Double> boundaryList() {
		
		return boundaryList;
	}

	@Override
	public double firstNotePosition() {
		return firstNotePosition;
	}
}
