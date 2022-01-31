package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;
import java.util.ArrayList;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.Embellishment;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.resource_objects.RandomNumberSequence;

/*
 * requires ppa.cf, ppa.cd and ppa.numberOfEmbellishments to work. (2019_04_26)
 */


public class PlugInSizeAndLengthContextEmbellisher extends Pluggable implements PipelinePlugIn{
	

	private int rndArrSize = 6;
	
	// this class assumes it will add notes before a guide tone so we do not use negative positions for preceding notes
	private double[] lengthOptions = new double[]{0.5, 1.0, 1.5, 2.0, 2.5, 3.0};
	private double[] lengthOptionChance = new double[]{0.1};
	private Embellishment[] longOptions;
	private Embellishment[] shortOptions;
	private double[] longOptionChance;
	private double[] shortOptionChance;
	
	
	private static final double guideToneAssumedLength = 4.0;		// so i don't actually have to calculate this for the subsequent note length comparison
	private static final double longIfLongerThanSubsequentNote = 1.0;	// must be more than this percent of subsequent note length to be LONG	
	private static final double absoluteLengthDivider = 2.9;		// longer than this is LONG, shorter is SHORT
	
	private void doParams(){
		setName("SLEmb");
		setSortIndex(50);
		setInputSort(0);
		setZone(2);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInSizeAndLengthContextEmbellisher(){
		doParams();
		longOptions = makeEmbellishmentOptions(new ED[]{new ED("d", 1), new ED("c", -1), new ED("c", 1)});
		longOptionChance = new double[]{0.1};
//		shortOptions = makeEmbellishmentOptions(new ED[]{new ED("s", 1), new ED("s", -1)});
		shortOptions = makeEmbellishmentOptions(new ED[]{new ED("d", 1), new ED("c", -1), new ED("c", 1)});
		shortOptionChance = new double[]{0.5};

	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		pnl.generatePrecedingSpaceValues();
		double[] rndLengthArr = makeRNDLengthArray(ppa.rnd, rndArrSize);
		double[] embellishmentChance = makeRNDLengthArray(ppa.rnd, rndArrSize);
		consolePrint("rndLengthArr", rndLengthArr);
		ArrayList<PipelineNoteObject> tempPNOList = new ArrayList<PipelineNoteObject>();

		for (PipelineNoteObject pno: pnl.pnoList){
//			int embellishmentChanceIndex = 0;
			double remaining = pno.precedingSpaceInNoteList;
			double[] tempLengthOptions = getTempLengthOptions(rndLengthArr, ppa.numberOfEmbellishments, remaining);
			int[] analysisArr = makeAnalysisArr(tempLengthOptions, pno, ppa.barlength);
			Embellishment[] embArr = getEmbellishmentArray(tempLengthOptions, pno, analysisArr, embellishmentChance);
			
			addPNOtoTemplist(embArr, pno, tempPNOList, tempLengthOptions, ppa, pnl.length);
			
//			System.out.println(pno.toString());
//			consolePrint("tempLengthOptions", tempLengthOptions);
//			consolePrint("analysis", analysisArr);
		}
		pnl.add(tempPNOList);
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

// privates ------------------------------------------------------------------------------
	private void addPNOtoTemplist(
			Embellishment[] embArr, 
			PipelineNoteObject pno, 
			ArrayList<PipelineNoteObject> tempPNOList, 
			double[] lengthArr, 
			PlayPlugArgument ppa,
			double melodyLength){
		
		PipelineNoteObject currentPNO = pno;
		for (int i = 0; i < embArr.length; i++){
			double thisPos = currentPNO.position - lengthArr[i];
			while (thisPos < 0) thisPos += melodyLength;
			PipelineNoteObject newPNO = new PipelineNoteObject(
					thisPos, 
					false, 
					true, 
					currentPNO);
			newPNO.addNote(embArr[i].getNote(currentPNO, ppa));
			newPNO.velocity = pno.velocity;
			tempPNOList.add(newPNO);
			currentPNO = newPNO;
		}
	}
	private Embellishment[] getEmbellishmentArray(
			double[] tempLengthOptions,
			PipelineNoteObject pno,
			int[] analysisArr,
			double[] embellishmentChance){
		Embellishment[] embArr = new Embellishment[tempLengthOptions.length];
		
		for (int i = 0; i < tempLengthOptions.length; i++){
			int embellishmentChanceIndex = 0;
			double newNotePos = pno.position - tempLengthOptions[i];
			Embellishment emb = getEmbellishment(analysisArr[i], embellishmentChance[embellishmentChanceIndex]);
			embArr[i] = emb;
			System.out.println("pos: " + newNotePos + " is " + emb.name());
			embellishmentChanceIndex++;
			if (embellishmentChanceIndex >= embellishmentChance.length) embellishmentChanceIndex = 0; 
		}
		return embArr;
	}
	private Embellishment getEmbellishment(int analysis, double chance){

		if (analysis == SHORT){
			return getChancyEmbellishment(shortOptions, shortOptionChance, chance);
		} else {
			return getChancyEmbellishment(longOptions, longOptionChance, chance);
		}
	}
	private int[] makeAnalysisArr(double[] lengthArr, PipelineNoteObject pno, double barLength){
		int[] anArr = new int[lengthArr.length];
		
		double movingPos = pno.position;
		double subsequentLength = guideToneAssumedLength;
		for (int i = 0; i < lengthArr.length; i++){
			int score = 0;
			movingPos -= lengthArr[i];
			double pos = movingPos;
			while (pos < 0.0) pos += barLength;
			score += lengthRelativeToSubsequentNote(Math.abs(lengthArr[i]), subsequentLength);
			score += absoluteLengthTest(Math.abs(lengthArr[i]));
			score += positionInBarTest(pos, barLength);
			if (score >= 2){
				anArr[i] = LONG;
			} else {
				anArr[i] = SHORT;
			}
			subsequentLength = Math.abs(lengthArr[i]);
		}
		
		return anArr;
	}
	private int positionInBarTest(double pos, double barLength){
		// basically all absolute quarter notes are LONG and everything else is SHORT
		if (pos % 1.0 > 0.0){
			return SHORT;
		} else {
			return LONG;
		}
	}
	private int absoluteLengthTest(double length){
		if (length > absoluteLengthDivider){
			return LONG;
		} else {
			return SHORT;
		}
	}
	private int lengthRelativeToSubsequentNote(double length, double subsequentLength){
		if (length > subsequentLength * longIfLongerThanSubsequentNote){
			return LONG;
		} else {
			return SHORT;
		}
	}
	
	private double[] getTempLengthOptions(double[] rndLengthArr, int arrLength, double lengthToFill){
		double[] tempOpt = new double[arrLength];
		int rndLengthArrIndex = 0;
		int optionLength = 0;
		for (int i = 0; i < arrLength; i++){
			double newLength = getChancyItem(lengthOptions, lengthOptionChance, rndLengthArr[rndLengthArrIndex]);
			lengthToFill -= newLength;
			if (lengthToFill <= 0.0){
				break;
			} else {
				tempOpt[i] = newLength;
				optionLength++;
				rndLengthArrIndex++;
				if (rndLengthArrIndex >= rndLengthArr.length) rndLengthArrIndex = 0;
			}
			
		}
		double[] finalOpt = new double[optionLength];
		for (int i = 0; i < finalOpt.length; i++){
			finalOpt[i] = tempOpt[i];
		}
		return finalOpt;
	}
	
	private double[] makeRNDLengthArray(RandomNumberSequence rnd, int size){
		double[] arr = new double[size];
		for (int i = 0; i < arr.length; i++){
			arr[i] = rnd.next();
		}
		return arr;
	}
	private void consolePrint(String header, double[] dArr){
		header += ": ";
		for (double d: dArr){
			header += String.format("%.3f", d);
			header += ", ";
		}
		System.out.println(header);
	}
	private void consolePrint(String header, int[] iArr){
		header += ": ";
		for (int i: iArr){
			header += i + ", ";
		}
		System.out.println(header);
	}
	public double getChancyItem(double[] optionArr, double[] chanceArr, double chance){
		double totalChance = getTotalChance(optionArr.length, chanceArr);
		chance = chance * totalChance;
		double currentChance = 0.0;
		int chanceIndex = 0;
		double finalOption = 0.0;
		for (double option: optionArr){
			currentChance += chanceArr[chanceIndex];
			chanceIndex++;
			if (chanceIndex >= chanceArr.length) chanceIndex = 0;
			if (chance <= currentChance){
				return option;
			} else {
				finalOption = option;
			}
		}
		return finalOption;
		
	}
	public Embellishment getChancyEmbellishment(Embellishment[] optionArr, double[] chanceArr, double chance){
		double totalChance = getTotalChance(optionArr.length, chanceArr);
		chance = chance * totalChance;
		double currentChance = 0.0;
		int chanceIndex = 0;
		Embellishment finalOption = optionArr[0];
		for (Embellishment option: optionArr){
			currentChance += chanceArr[chanceIndex];
			chanceIndex++;
			if (chanceIndex >= chanceArr.length) chanceIndex = 0;
			if (chance <= currentChance){
				return option;
			} else {
				finalOption = option;
			}
		}
		return finalOption;
		
	}
	private double getTotalChance(int arrLength, double[] chanceArr){
		int index = 0;
		double totalChance = 0.0;
		for (int i = 0; i < arrLength; i++){
			totalChance += chanceArr[index];
			index++;
			if (index >= chanceArr.length) index = 0;
		}
		return totalChance;
	}
	private Embellishment[] makeEmbellishmentOptions(ED[] edArr){
		Embellishment[] embArr = new Embellishment[edArr.length];
		for (int i = 0; i < edArr.length; i++){
			if (edArr[i].type.equals("s")){
				embArr[i] = new SemitoneEmbellishment(edArr[i].value);
			} else if (edArr[i].type.equals("d")){
				embArr[i] = new DiatonicEmbellishment(edArr[i].value);
			} else if (edArr[i].type.equals("c")){
				embArr[i] = new ChordToneEmbellishment(edArr[i].value);
			}
				
		}
		return embArr;
	}
	
	private static final int SHORT = 0;
	private static final int LONG = 1;

}
