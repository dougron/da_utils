package main.java.da_utils.algorithmic_models.grammar_phraseologist;

public class GrammarItem {
	
	
	GrammarCell[] gcArr;
	double weighting;
	double low;
	double high;
	
	public GrammarItem(double weighting, GrammarCell[] arr){
		gcArr = arr;
		this.weighting = weighting;
	}

	public String toString(){
		String str = "GrammarItem: ";
		for (GrammarCell gc: gcArr){
			str += "(" + gc.toString() + ")";
		}
		str += "\t" + weighting + " " + low + " " + high;
		return str;
	}

	public boolean calculatesWell(GrammarToken gt) {
		for (GrammarCell gc: gcArr){
			if (((double)gt.length * gc.lengthNumerator) % gc.lengthDenominator != 0){
				return false;
			}
		}
		return true;
	}
}
