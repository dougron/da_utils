package main.java.da_utils.algorithmic_models.melody_segmenter;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * takes an array of SegmentationModel and weightings and produces a final segmentation list
 * fyi - the 'batch' referred to is a batch of SegmentationModels not a batch of PhantomLiveClips
 */

public class BatchSegmenter {

	private double[] weighting;
	private ArrayList<Double> boundaryList;
	private boolean hasBoundaryList = false;
	private SegmentationModel[] segArr;
	private double cutoff;
	private PhantomLiveClip plc;

	public BatchSegmenter(SegmentationModel[] segArr, double[] weighting, double cutoff, PhantomLiveClip plc) {
		this.weighting = weighting;
		this.segArr = segArr;
		this.cutoff = cutoff;
		this.plc = plc;
		instantiateSegmentationModels(plc);
//		makeFinalBoundaryList(segArr, cutoff);
	}
	

	public HashMap<String, ArrayList<Double>> getBoundaryListMap(){
		HashMap<String, ArrayList<Double>> map = new HashMap<String, ArrayList<Double>>();
		for (SegmentationModel sm: segArr){
			map.put(sm.xmlTag(), sm.boundaryList());
		}
		
		
		return map;
	}
	public SegmentationModel[] segmentationModelArr(){
		return segArr;
	}

	public ArrayList<Double> boundaryList(){
		if (!hasBoundaryList){
			boundaryList = makeFinalBoundaryList(segArr, cutoff);
			hasBoundaryList = true;
		}
		return boundaryList;
	}
	public PhantomLiveClip plc(){
		return plc;
	}
	public double getWeighting(int index){
		return weighting[index % weighting.length];
	}
	
// privates ---------------------------------------------------------------------------
	
	
	private void instantiateSegmentationModels(PhantomLiveClip plc) {
		for(SegmentationModel sm: segArr){
			sm.instantiate(plc);
		}
		boundaryList = makeFinalBoundaryList(segArr, cutoff);
		hasBoundaryList = true;
	}
	private ArrayList<Double> makeFinalBoundaryList(SegmentationModel[] segArr, double cutoff) {
		HashMap<Double, Double> scoreMap = new HashMap<Double, Double>();
		ArrayList<Double> list = new ArrayList<Double>();
		
		int index = 0;
		double totalWeighting = 0.0;
		for (SegmentationModel sm: segArr){
			for (double d: sm.boundaryList()){
				if (!scoreMap.containsKey(d)){
					scoreMap.put(d, getWeighting(index));
				} else {
					scoreMap.put(d, scoreMap.get(d) + getWeighting(index));
				}
			}
			totalWeighting += getWeighting(index);
			index++;
		}
		for (Double key: scoreMap.keySet()){
			//scoreMap.put(key, scoreMap.get(key) / totalWeighting);
			System.out.println(key + ": " + scoreMap.get(key) + " / " + totalWeighting + " = " + scoreMap.get(key) / totalWeighting + " <> " + cutoff);
			if (scoreMap.get(key) / totalWeighting >= cutoff){
				list.add(key);
			}
		}
		return list;
	}


//	public static SegmentationModel[] fullListOfSegmentationModels() {
//		return new SegmentationModel[]{
//				new InterOnsetPeakBoundary(),
//				new OffsetToOnsetBoundary(),
//				new LocalBoundaryDetectionModel(),
//				new LocalBoundaryDetectionModel("LBDMraw", 0.0),
//		};
//	}
//	public static SegmentationModel[] fullListOfSegmentationModels() {
//		return new SegmentationModel[]{
//				new InterOnsetPeakBoundary(),
//				new OffsetToOnsetBoundary(),
//				new LocalBoundaryDetectionModel("LBDM 0.0", 0.0),
//				new LocalBoundaryDetectionModel("LBDM 0.25", 0.25),
//				new LocalBoundaryDetectionModel("LBDM 0.5", 0.5),
//				new LocalBoundaryDetectionModel("LBDM 0.75", 0.75),
//				new LocalBoundaryDetectionModel("LBDM 1.0", 1.0),
//				new LocalBoundaryDetectionModel("LBDM 1.25", 1.25),
//				new LocalBoundaryDetectionModel("LBDM 1.5", 1.5),
//				new LocalBoundaryDetectionModel("LBDM 1.75", 1.75),
//				new LocalBoundaryDetectionModel("LBDM 2.0", 2.0),
//		};
//	}
	public static SegmentationModel[] fullListOfSegmentationModels() {
		return new SegmentationModel[]{
				new InterOnsetPeakBoundary(),
				new OffsetToOnsetBoundary(),
				new LocalBoundaryDetectionModel("LBDM 0.0", 0.0, new double[]{0.0, 1.0, 0.0}),
				new LocalBoundaryDetectionModel("LBDM 0.25", 0.25, new double[]{0.0, 1.0, 0.0}),
				new LocalBoundaryDetectionModel("LBDM 0.5", 0.5, new double[]{0.0, 1.0, 0.0}),
				new LocalBoundaryDetectionModel("LBDM 0.75", 0.75, new double[]{0.0, 1.0, 0.0}),
				new LocalBoundaryDetectionModel("LBDM 1.0", 1.0, new double[]{0.0, 1.0, 0.0}),
				new LocalBoundaryDetectionModel("LBDM 1.25", 1.25, new double[]{0.0, 1.0, 0.0}),
				new LocalBoundaryDetectionModel("LBDM 1.5", 1.5, new double[]{0.0, 1.0, 0.0}),
				new LocalBoundaryDetectionModel("LBDM 1.75", 1.75, new double[]{0.0, 1.0, 0.0}),
				new LocalBoundaryDetectionModel("LBDM 2.0", 2.0, new double[]{0.0, 1.0, 0.0}),
		};
	}
	

}
