package main.java.da_utils.algorithmic_models.melody_segmenter;

import java.util.ArrayList;

import main.java.da_utils.combo_variables.IntAndDouble;

public class LocalBoundaryDetectionModel implements SegmentationModel {
	
	private double stdevFilterCutoff = 0.9;
	private int localAverageExtent = 5;			// this is the number of items before and after the current event that contribute to the local average used in the stdev filter
	private double firstNotePosition = 0.0;
	ArrayList<Double> boundaryList = new ArrayList<Double>();
	ArrayList<Double> pitchScoreList;//= new ArrayList<Double>();
	ArrayList<Double> ioiScoreList;// = new ArrayList<Double>();
	ArrayList<Double> restScoreList;//= new ArrayList<Double>();
	ArrayList<Double> totalList;
	ArrayList<Double> cutoffList;
	int roundingDecimals = 2;
	double pitchWeighting = 0.25;
	double ioiWeighting = 0.5;
	double restWeighting = 0.25;
	String xmlTag = "LBDM";
	ArrayList<IntAndDouble> fullBoundaryList;

	
	
	public LocalBoundaryDetectionModel(){
		
	}
	public LocalBoundaryDetectionModel(String tag, double stdevCutoff){
		xmlTag = tag;
		stdevFilterCutoff = stdevCutoff;
	}
	public LocalBoundaryDetectionModel(String tag, double stdevCutoff, double[] weighting){ // weighting {pitch, ioi, rest}
		xmlTag = tag;
		stdevFilterCutoff = stdevCutoff;
		if (weighting.length == 3){
			pitchWeighting = weighting[0];
			ioiWeighting = weighting[1];
			restWeighting = weighting[2];
		}
	}
	@Override
	public void instantiate(PhantomLiveClip plc) {
		makeBoundaryList(plc);
		if (plc.lc.noteList.size() > 0){
			firstNotePosition = plc.lc.noteList.get(0).position;
		}

	}

	private void makeBoundaryList(PhantomLiveClip plc) {
		pitchScoreList = makePitchScoreList(plc);
		ioiScoreList = makeIOIScoreList(plc);
		restScoreList = makeRestScoreList(plc);
		totalList = makeTotalList();
		double stdevOfUnPhantomData = makeStDev(totalList, plc);
		cutoffList = makeCutoffList(stdevOfUnPhantomData, totalList);
		//fullBoundaryList = makeFullBoundaryList(plc);
		boundaryList = makeFilteredBoundaryList(plc);
		System.out.println(toString());
	}

	private ArrayList<Double> makeFilteredBoundaryList(PhantomLiveClip plc) { //Double {index, value}
		ArrayList<Double> list = new ArrayList<Double>();
		for (int index = plc.phantomStartIndex; index < plc.phantomEndIndex; index++){
			double previousLBDM = Math.round(totalList.get(index - 1) * Math.pow(10, roundingDecimals)) / Math.pow(10, roundingDecimals);
			double currentLBDM = Math.round(totalList.get(index) * Math.pow(10, roundingDecimals)) / Math.pow(10, roundingDecimals);
			double nextLBDM = Math.round(totalList.get(index + 1) * Math.pow(10, roundingDecimals)) / Math.pow(10, roundingDecimals);
			if (currentLBDM - previousLBDM > 0 && nextLBDM - currentLBDM < 0){
				System.out.println("peak detected at pos=" + plc.noteList.get(index).position);
				if (currentLBDM > cutoffList.get(index)){
					System.out.println("is greater than cutoff");
					list.add(plc.noteList.get(index).position);
				}
			}
		}
		return list;
	}
	private ArrayList<Double> makeCutoffList(double stDev, ArrayList<Double> tList) {
		ArrayList<Double> list = new ArrayList<Double>();
		
		for (int i = 0; i < tList.size(); i++){
			double localAvg = getLocalAverage(tList, i, localAverageExtent);
			list.add(localAvg + stDev * stdevFilterCutoff);
		}
		return list;
	}

	private double getLocalAverage(ArrayList<Double> tList, int index, int extent) {
		int count = 0;
		double tally = 0.0;
		for (int i = index - extent; i < index + extent + 1; i++){
			if (i > -1 && i < tList.size()){
				count++;
				tally += tList.get(i);
			}
		}
		return tally / count;
	}

	private double makeStDev(ArrayList<Double> tList, PhantomLiveClip plc) {
		double total = 0.0;
		for (int i = plc.phantomStartIndex; i < plc.phantomEndIndex; i++){
			total += tList.get(i);
		}
		double mean = total / (plc.phantomEndIndex - plc.phantomStartIndex);
		double difTotal = 0.0;
		for (int i = plc.phantomStartIndex; i < plc.phantomEndIndex; i++){
			difTotal += Math.pow(mean - tList.get(i), 2);
		}
		return Math.sqrt(difTotal / (plc.phantomEndIndex - plc.phantomStartIndex - 1));
	}

	private ArrayList<Double> makeTotalList() {
		ArrayList<Double> list = new ArrayList<Double>();
		
		for (int i = 0; i < pitchScoreList.size(); i++){
			list.add(pitchScoreList.get(i) * pitchWeighting + ioiScoreList.get(i) * ioiWeighting + restScoreList.get(i) * restWeighting);
		}
		return list;
	}

	private ArrayList<Double> makeRestScoreList(PhantomLiveClip plc) {
		ArrayList<Double> list = new ArrayList<Double>();
		
		list.add(0.0);
		for (int i = 1; i < plc.noteList.size() - 1; i++){
						
			double previous = Math.abs(plc.offsetToOnsetIntervalList.get(i - 1)) + 0.1;		// 0.1 circumvents divide by 0 errors
			double current = Math.abs(plc.offsetToOnsetIntervalList.get(i)) + 0.1;		// 0.1 circumvents divide by 0 errors
			double next = plc.offsetToOnsetIntervalList.get(i + 1) + 0.1;
			
			double strength = current * (r(previous, current) + r(current, next));
			list.add(strength);
		}
		list.add(0.0);
		return list;
	}

	private double r(double x, double y) {
		if (x == y){
			return 0;
		} else {
			return Math.abs(x - y) / (x + y);
		}
	}
	private ArrayList<Double> makeIOIScoreList(PhantomLiveClip plc) {
		ArrayList<Double> list = new ArrayList<Double>();
		
		list.add(0.0);
		for (int i = 1; i < plc.noteList.size() - 1; i++){
			double previous = Math.abs(plc.interOnsetIntervalList.get(i - 1)) + 0.1;		// 0.1 circumvents divide by 0 errors
			double current = Math.abs(plc.interOnsetIntervalList.get(i)) + 0.1;		// 0.1 circumvents divide by 0 errors
			double next = plc.interOnsetIntervalList.get(i + 1) + 0.1;
			
			double strength = current * (r(previous, current) + r(current, next));
			list.add(strength);
		}
		list.add(0.0);
		return list;
	}

	private ArrayList<Double> makePitchScoreList(PhantomLiveClip plc) {
		ArrayList<Double> list = new ArrayList<Double>();
		
		list.add(0.0);
		for (int i = 1; i < plc.noteList.size() - 1; i++){
			double previous = Math.abs(plc.pitchIntervalList.get(i - 1)) + 0.1;		// 0.1 circumvents divide by 0 errors
			double current = Math.abs(plc.pitchIntervalList.get(i)) + 0.1;		// 0.1 circumvents divide by 0 errors
			double next = plc.pitchIntervalList.get(i + 1) + 0.1;
			
			double strength = current * (r(previous, current) + r(current, next));
			list.add(strength);
		}
		list.add(0.0);
		return list;
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
		return xmlTag;
	}
	public String toString(){
		String str = "";
		for (int i = 0; i < totalList.size(); i++){
			str += totalList.get(i) + "\t" + cutoffList.get(i) + "\n";
		}
		return str;
	}

}
