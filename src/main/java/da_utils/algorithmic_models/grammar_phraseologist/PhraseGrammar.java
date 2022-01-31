package main.java.da_utils.algorithmic_models.grammar_phraseologist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/*
 * generates a phrase structure using a context free grammar and random choices.
 * IMPORTANT to note, the selection of which set of options to use proceeds in the sequence that 
 * the GrammarItemLists were added. so its probably best to put the LESS_THAN the lowest barcount item
 * first and then proceed to bigger bar counts as it will take the first one which fits the criteria.
 */

public class PhraseGrammar {

	HashMap<GrammarKeyItem, GrammarItemList> giMap = new HashMap<GrammarKeyItem, GrammarItemList>();
	ArrayList<GrammarKeyItem> gkiList = new ArrayList<GrammarKeyItem>();
	
	public PhraseGrammar(){
		
	}
	
	public void add(GrammarKeyItem gki, GrammarItemList gil){
		giMap.put(gki, gil);
		gkiList.add(gki);
	}
	
	public String toString(){
		String str = "PhraseGrammar:------------------\n";
		for (GrammarKeyItem gki: gkiList){
			
			str += gki.toString() + "\n";
			for (GrammarItem gi: giMap.get(gki).giList){
				str += "   " + gi.toString() + "\n";
			}
		}
		return str;
	}
	public ArrayList<Integer> getPhraseStructure(int seed, GrammarToken[] gtArr){
		ArrayList<Integer> list = new ArrayList<Integer>();
		ArrayList<GrammarToken> listOne = new ArrayList<GrammarToken>();
		ArrayList<GrammarToken> listTwo = new ArrayList<GrammarToken>();
		Random rnd = new Random(seed);
		for (GrammarToken gt: gtArr){
			listOne.add(gt);
		}
		boolean flag = true;
		
		while (flag) {
			if (noUnresolvedTokens(listOne)) {
				for (GrammarToken gt : listOne) {
					list.add(gt.length);
				}
				flag = false;
			} else {
				for (GrammarToken gt : listOne) {
					if (gt.name == GrammarKeyItem.PHRASE){
						listTwo.add(gt);
					} else {
						GrammarItemList gil = getGrammarItemListFromMap(gt);
						GrammarToken[] tokenArr = gil.getOption(gt, rnd.nextDouble());
						for (GrammarToken gtt : tokenArr) {
							listTwo.add(gtt);
						}
					}
					
				}
				listOne = listTwo;
				listTwo = new ArrayList<GrammarToken>();
			} 
		}
		return list;
	}

	private GrammarItemList getGrammarItemListFromMap(GrammarToken gt) {
		for (GrammarKeyItem gki: gkiList){
			if (gki.works(gt)){
				return giMap.get(gki);
			}
		}
		return null;
	}

	private boolean noUnresolvedTokens(ArrayList<GrammarToken> list) {
		for (GrammarToken gt: list){
			if (gt.name != GrammarKeyItem.PHRASE){
				return false;
			}
		}
		return true;
	}
}
