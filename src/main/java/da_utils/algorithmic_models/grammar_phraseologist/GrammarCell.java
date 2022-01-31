package main.java.da_utils.algorithmic_models.grammar_phraseologist;

public class GrammarCell {

	String name;
	int lengthNumerator;
	int lengthDenominator;
	
	public GrammarCell(String name, int numerator, int denominator){
		this.name = name;
		this.lengthNumerator = numerator;
		this.lengthDenominator = denominator;
	}
	
	public String toString(){
		return name + " " + lengthNumerator + "/" + lengthDenominator;
	}
}
