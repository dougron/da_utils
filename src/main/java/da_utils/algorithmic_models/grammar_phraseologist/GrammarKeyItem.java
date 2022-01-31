package main.java.da_utils.algorithmic_models.grammar_phraseologist;

public class GrammarKeyItem {

	String name;
	int lengthConstraint;
	int comparator;
	
	public static final int EQUALS = 0;
	public static final int LESS_THAN = 1;
	public static final int GREATER_THAN = 2;
	public static final int EQUALS_OR_LESS_THAN = 3;
	public static final int EQUALS_OR_GREATER_THAN = 4;
	public static final int NOT_EQUALS = 5;
	
	public static final String[] comparatorArr = new String[]{"=", "<", ">", "=<", ">=", "!="};
	
	// thesre the language placeholder names options
	public static final String A = "A";
	public static final String PHRASE = "phrase";
	
	public GrammarKeyItem(String name, int len, int comp){
		this.name = name;
		this.lengthConstraint = len;
		this.comparator = comp;
	}
	
	public String toString(){
		return "GrammarKeyItem: " + name + " " + comparatorArr[comparator] + " " + lengthConstraint;
	}

	public boolean works(GrammarToken gt) {
		if (gt.name == name){
			switch (comparator){
			case EQUALS:	if (gt.length == lengthConstraint)return true; else return false;
			case LESS_THAN:	if (gt.length < lengthConstraint)return true; else return false;
			case GREATER_THAN:	if (gt.length > lengthConstraint)return true; else return false;
			case EQUALS_OR_LESS_THAN:	if (gt.length <= lengthConstraint)return true; else return false;
			case EQUALS_OR_GREATER_THAN:	if (gt.length >= lengthConstraint)return true; else return false;
			case NOT_EQUALS:	if (gt.length != lengthConstraint)return true; else return false;
			}
		}
		return false;
	}
}
