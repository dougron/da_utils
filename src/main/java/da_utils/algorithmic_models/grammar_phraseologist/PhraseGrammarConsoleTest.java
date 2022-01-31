package main.java.da_utils.algorithmic_models.grammar_phraseologist;

import java.util.ArrayList;

import acm.program.ConsoleProgram;

public class PhraseGrammarConsoleTest extends ConsoleProgram {

	
	
	public void run(){
		setSize(700, 700);
		PhraseGrammar pg = makePhraseGrammar();
		println(pg.toString());
		
		GrammarToken[] seedArray = new GrammarToken[]{
//				new GrammarToken(GrammarKeyItem.PHRASE, 8),
				new GrammarToken(GrammarKeyItem.A, 16),
		};
		
		for (int i = 0; i < 20; i++) {
			ArrayList<Integer> list = pg.getPhraseStructure(i * 100, seedArray);
			String str = "";
			for (Integer I : list) {
				str += I + ", ";
			}
			println(str);
		}
	}

	private PhraseGrammar makePhraseGrammar() {
		PhraseGrammar pg = new PhraseGrammar();
		
		GrammarKeyItem gki3 = new GrammarKeyItem(GrammarKeyItem.A, 2, GrammarKeyItem.EQUALS_OR_LESS_THAN);
		GrammarItemList gil3 = new GrammarItemList();
		gil3.addItem(new GrammarItem(1.0, new GrammarCell[]{new GrammarCell(GrammarKeyItem.PHRASE, 1, 1)}));
		
		
		pg.add(gki3, gil3);
		
		GrammarKeyItem gki2 = new GrammarKeyItem(GrammarKeyItem.A, 7, GrammarKeyItem.EQUALS_OR_LESS_THAN);
		GrammarItemList gil2 = new GrammarItemList();
		gil2.addItem(new GrammarItem(0.33, new GrammarCell[]{new GrammarCell(GrammarKeyItem.PHRASE, 1, 1)}));
		gil2.addItem(new GrammarItem(0.33, new GrammarCell[]{new GrammarCell(GrammarKeyItem.A, 1, 1)}));
		gil2.addItem(new GrammarItem(0.33, new GrammarCell[]{
				new GrammarCell(GrammarKeyItem.A, 1, 2),
				new GrammarCell(GrammarKeyItem.A, 1, 2)}));
		
		pg.add(gki2, gil2);
		
		GrammarKeyItem gki4 = new GrammarKeyItem(GrammarKeyItem.A, 12, GrammarKeyItem.GREATER_THAN);
		GrammarItemList gil4 = new GrammarItemList();
//		gil1.addItem(new GrammarItem(0.25, new GrammarCell[]{new GrammarCell(GrammarKeyItem.PHRASE, 1, 1)}));
		gil4.addItem(new GrammarItem(0.25, new GrammarCell[]{
				new GrammarCell(GrammarKeyItem.A, 1, 2),
				new GrammarCell(GrammarKeyItem.A, 1, 2)}));

				
		pg.add(gki4, gil4);

		
		GrammarKeyItem gki1 = new GrammarKeyItem(GrammarKeyItem.A, 7, GrammarKeyItem.GREATER_THAN);
		GrammarItemList gil1 = new GrammarItemList();
//		gil1.addItem(new GrammarItem(0.25, new GrammarCell[]{new GrammarCell(GrammarKeyItem.PHRASE, 1, 1)}));
		gil1.addItem(new GrammarItem(0.25, new GrammarCell[]{
				new GrammarCell(GrammarKeyItem.A, 1, 2),
				new GrammarCell(GrammarKeyItem.A, 1, 2)}));
		gil1.addItem(new GrammarItem(0.1, new GrammarCell[]{
				new GrammarCell(GrammarKeyItem.A, 3, 8),
				new GrammarCell(GrammarKeyItem.A, 3, 8),
				new GrammarCell(GrammarKeyItem.A, 2, 8)}));
		gil1.addItem(new GrammarItem(0.25, new GrammarCell[]{
				new GrammarCell(GrammarKeyItem.A, 1, 3),
				new GrammarCell(GrammarKeyItem.A, 1, 3),
				new GrammarCell(GrammarKeyItem.A, 1, 3)}));
				
		pg.add(gki1, gil1);

		
		
		
		
		return pg;
	}
}
