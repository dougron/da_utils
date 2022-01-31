package main.java.da_utils.algorithmic_models.grammar_phraseologist;

import java.util.ArrayList;

public class GrammarItemList {

	
	ArrayList<GrammarItem> giList = new ArrayList<GrammarItem>();
	double chanceTotal = 0.0;
	
	
	public GrammarItemList(GrammarItem[] giArr){
		for (GrammarItem gi: giArr){
			giList.add(gi);
		}
	}
	
	public GrammarItemList(){
		
	}
	
	public void addItem(GrammarItem gi){
		giList.add(gi);
		chanceTotal = remakeChancesAndTotal(giList);
	}
	
	private double remakeChancesAndTotal(ArrayList<GrammarItem> list) {
		double total = 0.0;
		for (GrammarItem gi: list){
			gi.low = total;
			total += gi.weighting;
			gi.high = total;
		}	
		return total;
	}
	
	public String toString(){
		String str = "GrammarItemList:\n";
		for (GrammarItem gi: giList){
			str += "   " + gi.toString() + "\n";
		}
		return str;
	}

	public GrammarToken[] getOption(GrammarToken gt, double rnd) {
		ArrayList<GrammarItem> list = new ArrayList<GrammarItem>();
		for (GrammarItem gi: giList){
			if (gi.calculatesWell(gt)){
				list.add(gi);
			}
		}
		double total = remakeChancesAndTotal(list);
		GrammarItem chosenGI = null;
		if (list.size() == 0){
//			chosenGI = null;
		} else {
			double x = rnd * total;
			for (GrammarItem gi: list){
				if (x > gi.low && x < gi.high){
					chosenGI = gi;
					break;
				}
			}
		}
		if (chosenGI == null){
			return new GrammarToken[]{gt};
		} else {
			GrammarToken[] arr = new GrammarToken[chosenGI.gcArr.length];
			for (int i = 0; i < arr.length; i++){
				GrammarCell gc = chosenGI.gcArr[i];
				arr[i] = new GrammarToken(gc.name, gt.length * gc.lengthNumerator / gc.lengthDenominator);
			}
			return arr;
		}
		
	}
}
