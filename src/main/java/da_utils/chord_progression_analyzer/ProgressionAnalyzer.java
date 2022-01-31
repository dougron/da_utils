package main.java.da_utils.chord_progression_analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.chord_progression_analyzer.ChordInKeyObject;
import main.java.da_utils.chord_progression_analyzer.chord_chunk.ChordChunk;
import main.java.da_utils.chord_progression_analyzer.chord_chunk.ChordChunkList;
import main.java.da_utils.combo_variables.DoubleAndString;

/*
 * shell for the ChordProgression analyzer
 */
public class ProgressionAnalyzer {

//	private LiveClip lc;
	private ChordChunkList ccl;
	private ArrayList<ChordInKeyObject> cikoList;
	



	private ArrayList<ChordProgressionAnalyzer> cpaList;
	public static String DEFAULT_REZ = "4n";
//	ChordScaleDictionary csd;

	
	
	public ProgressionAnalyzer(String path)
	{
		// this instantiator for testing only. ProgressionAnalyzer should not deal with LiveClip
		// directly at all.
		init(new ChordChunkList(DEFAULT_REZ, getLiveClip(path)));
	}
	
	
	
	public ProgressionAnalyzer(ChordChunkList ccl)
	{
		init(ccl);
	}
	
	
	
	public String toString()
	{
		String str = "ProgressionAnalysis-" + ccl.name() + "\n" + analysisToString();
		return str;
	}
	
	
	
	public String modeAnalysisToString() 
	{
		String str = "ModeAnalysis:-";
		for (ChordInKeyObject ciko: cikoList)
		{
			str +="\n" + ciko.toStringKeyChordAndFunction() + " " + ciko.mode().name;
		}
		return str;
	}
	
	
	
	public String analysisToString()
	{
		String str = "";
		for (ChordInKeyObject ciko: cikoList)
		{
			str += ciko.toStringKeyChordAndFunction() + " ";
		}
		return str;
	}
	
	
	
	public String dumpToString()
	{
		String str = "ProgressionAnalysis-" + ccl.name() + "\n";
		for (ChordProgressionAnalyzer cpa: cpaList)
		{
			str += "--------------------------\n" + cpa.toString() + "\n";
		}
		return str;
	}
	
	
	
	public LiveClip getClip()
	{
		return ccl.lc;
	}
	
	
	
	public String name()
	{
		return ccl.name();
	}
	
	
	
	public ArrayList<ChordInKeyObject> analysisChordInKeyObjectList() 
	{		
		return cikoList;
	}
	
	
	
	public ChordInKeyObject getPrevailingCIKO(double pos) 
	{
		while (pos < 0.0)
		{
			pos += ccl.getLength();
		}
		double actualPos = pos % ccl.getLength();
		for (ChordInKeyObject ciko: cikoList)
		{
			ChordChunk cc = ciko.cc;
			if (actualPos >= cc.position() && actualPos < cc.position() + cc.length())
			{
				return ciko;
			}
		}
		return null;
	}
	
	
	
	public ArrayList<DoubleAndString> getListOfChords()
	{
		ArrayList<DoubleAndString> list = new ArrayList<DoubleAndString>();
		for (ChordInKeyObject ciko: cikoList)
		{
			list.add(new DoubleAndString(ciko.position(), ciko.chordSymbol()));
		}
		return list;
	}
	
	
	
	public ArrayList<DoubleAndString> getListOfKeyChordSymbols()
	{
		ArrayList<DoubleAndString> list = new ArrayList<DoubleAndString>();
		for (ChordInKeyObject ciko: cikoList)
		{
			list.add(new DoubleAndString(ciko.position(), ciko.toStringKeyChordAndFunction()));
		}
		return list;
	}
	
	
	
	public ArrayList<DoubleAndString> getListOfSimpleKeyChordSymbols()
	{
		// developed for the RepetitionAnalysis output
		ArrayList<DoubleAndString> list = new ArrayList<DoubleAndString>();
		for (ChordInKeyObject ciko: cikoList)
		{
			list.add(new DoubleAndString(ciko.position(), ciko.toStringKeyChordAndSimpleFunction()));
		}
		return list;
	}
	
	
	
// privates ---------------------------------------------------------------------------------------------
	
	
	private void init(ChordChunkList ccl)
	{
		this.ccl = ccl;
//		ccl = new ChordChunkList(DEFAULT_REZ, lc);
		ChordProgressionAnalyzer cpaOneKey = new ChordProgressionAnalyzer(ccl);
		if (cpaOneKey.bestKeyAnalysis().getXCount() == 0)
		{
			cikoList = cpaOneKey.bestKeyAnalysis().chordInKeyList;
			cpaList = new ArrayList<ChordProgressionAnalyzer>();
			cpaList.add(cpaOneKey);			
		} 
		else 
		{
			ArrayList<ChordChunkList> cclList = breakIntoAnalysisCells();
//			for (LiveClip lclc: clipList)
//			{
//				lclc.makeChordAnalysis("8n");
//				System.out.println("clip============================");
//				System.out.println(lclc.chunkArrayToString());
//			}			
			cpaList = makeCPAList(cclList);
			cikoList = makeCikoList(cpaList);
			doRepeatedChordCheck();
			doRelativeMajorOrMinorIslandTest();
			
		}
		assignQualityToKeyBasedOnOveralMajorMinorScores();		
		associateCikoListItemsWithChordChunks();
//		associateChordListItemsWithChordChunksAssociatedWithCikoList();
	}
	
	
	
	



	private void associateCikoListItemsWithChordChunks()
	{
		for (ChordInKeyObject ciko: cikoList)
		{
			ciko.associateThisCIKOWithChordChunk();
		}
		
	}



	private void assignQualityToKeyBasedOnOveralMajorMinorScores() 
	{
		HashMap<Integer, Integer> mmScoreMap = makeMajorMinorScoreMap();
		assignMajorOrMinorToCikoListItems(mmScoreMap);
		
	}
	
	
	
	private void assignMajorOrMinorToCikoListItems(HashMap<Integer, Integer> mmScoreMap) 
	{
//		for (Integer i: mmScoreMap.keySet())
//		{
//			System.out.println("keyIndex: " + i + " score=" + mmScoreMap.get(i));
//		}
		for (ChordInKeyObject ciko: cikoList)
		{
			int score = mmScoreMap.get(ciko.keyIndex);
			//System.out.println(ciko.keyIndex + " score=" + score);
			if (score >= 0)
			{
				ciko.isMajorKey = true;
			} 
			else 
			{
				ciko.isMajorKey = false;
			}
			if (ciko.unequivocallyLocalMajor)
			{
				ciko.isMajorKey = true;
			}
			if (ciko.unequivocallyLocalMinor)
			{
				ciko.isMajorKey = false;
			}
		}		
	}
	
	
	
	private HashMap<Integer, Integer> makeMajorMinorScoreMap()
	{
		HashMap<Integer, Integer> mmScoreMap = new HashMap<Integer, Integer>();
		for (ChordInKeyObject ciko: cikoList)
		{
			if (!mmScoreMap.containsKey(ciko.keyIndex))
			{
				mmScoreMap.put(ciko.keyIndex, 0);
			}
			int x = mmScoreMap.get(ciko.keyIndex);
			x += ciko.majorScore;
			x -= ciko.minorScore;
			mmScoreMap.put(ciko.keyIndex, x);
		}
		return mmScoreMap;
	}
	
	
	
	private void doRepeatedChordCheck() 
	{
		// checks for repeated chords that might have been analyzed in a different key
		// Black Orpheus bar 26 issue
		ArrayList<ChordInKeyObject> itemsToRemove = new ArrayList<ChordInKeyObject>();
		ArrayList<ChordInKeyObject> itemsToAdd = new ArrayList<ChordInKeyObject>();
		for (ChordInKeyObject ciko: cikoList)
		{
			if (ChordInKeyObject.sameBasicChord(ciko, ciko.next) && ciko.keyIndex != ciko.next.keyIndex)
			{
				ChordInKeyObject ciko2 = ciko.next;
				ChordInKeyObject ciko3 = ciko2.next;

				int rootMove_2to3 = (ciko3.rootIndex + 12 - ciko2.rootIndex) % 12;

				if (
						rootMove_2to3 == 5 
						&& (ciko2.cc.isMinorType() || ciko2.cc.isHalfDiminishedType())
						&& ciko3.cc.isDominantType())
				{
					// erm do nothing........
				} 
				else 
				{
					KeyAnalysis ka = new KeyAnalysis(ciko.keyIndex, ciko2, ccl.getLiveClip());
					if (ka.getXCount() == 0)
					{
						itemsToRemove.add(ciko2);
						for (ChordInKeyObject cccc: ka.chordInKeyList)
						{
							itemsToAdd.add(cccc);
						}
					}
				}
			}
		}
		removeAndReplaceInCikoList(itemsToRemove, itemsToAdd);		
	}
	
	
	
	private void removeAndReplaceInCikoList(ArrayList<ChordInKeyObject> itemsToRemove,
			ArrayList<ChordInKeyObject> itemsToAdd) 
	{
		if (itemsToRemove.size() > 0)
		{
			cikoList.removeAll(itemsToRemove);
			cikoList.addAll(itemsToAdd);
			Collections.sort(cikoList, ChordInKeyObject.positionComparator);
			setNextAndPrevious(cikoList);
		}	
	}
	
	
	
	private void doRelativeMajorOrMinorIslandTest() 
	{
		// deals with a case of a single chord analyzed in a major or minor key surrounded
		// by its relative minor or major....  in response to the first chord of Black Orpheus which comes
		// up in D major rather than Bm...
		ArrayList<ChordInKeyObject> itemsToRemove = new ArrayList<ChordInKeyObject>();
		ArrayList<ChordInKeyObject> itemsToAdd = new ArrayList<ChordInKeyObject>();
		for (ChordInKeyObject ciko: cikoList)
		{
			int keyRelativeToPrevious = (ciko.previous.keyIndex + 12 - ciko.keyIndex) % 12;
			int keyRelativeToNext = (ciko.next.keyIndex + 12 - ciko.keyIndex) % 12;
			if (keyRelativeToNext == 3 && keyRelativeToPrevious == 3)
			{
				//System.out.println(ciko.chordString + " is a minor island between " + ciko.previous.chordString + " and " + ciko.next.chordString);
				KeyAnalysis ka = new KeyAnalysis((ciko.keyIndex + 3) % 12, ciko, ccl.lc);
				if (ka.getXCount() == 0)
				{
					itemsToRemove.add(ciko);
					for (ChordInKeyObject cccc: ka.chordInKeyList)
					{
						itemsToAdd.add(cccc);
					}
				}
			} 
			else if (keyRelativeToNext == 9 && keyRelativeToPrevious == 9)
			{
				//System.out.println(ciko.chordString + " is a major island between " + ciko.previous.chordString + " and " + ciko.next.chordString);
				KeyAnalysis ka = new KeyAnalysis((ciko.keyIndex + 12 - 3) % 12, ciko, ccl.lc);
				if (ka.getXCount() == 0)
				{
					itemsToRemove.add(ciko);
					for (ChordInKeyObject cccc: ka.chordInKeyList)
					{
						itemsToAdd.add(cccc);
					}
				}
			}
		}
		removeAndReplaceInCikoList(itemsToRemove, itemsToAdd);		
	}
	
	

	private LiveClip getLiveClip(String path)
	{
		LiveClip lc;
		try 
		{
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			lc = new LiveClip(br);
			return lc;
		} 
		catch (Exception ex)
		{
			System.out.println(ex.toString());
		}
		return null;
	}
	
	
	
	private ArrayList<ChordInKeyObject> makeCikoList(ArrayList<ChordProgressionAnalyzer> cpaList) 
	{
		ArrayList<ChordInKeyObject> cList = new ArrayList<ChordInKeyObject>();
		for (ChordProgressionAnalyzer cpa: cpaList)
		{
			cList.addAll(cpa.getBestChordAnalysis());
		}
		Collections.sort(cList, ChordInKeyObject.positionComparator);
		setNextAndPrevious(cList);
		return cList;
	}
	
	

	private void setNextAndPrevious(ArrayList<ChordInKeyObject> cList) 
	{
		for (int i = 0; i < cList.size(); i++)
		{
			if (i == 0){
				cList.get(i).previous = cList.get(cList.size() - 1);
			} 
			else 
			{
				cList.get(i).previous = cList.get(i - 1);
			}
			if (i == cList.size() - 1)
			{
				cList.get(i).next = cList.get(0);
 			} 
			else 
			{
 				cList.get(i).next = cList.get(i + 1);
 			}
		}		
	}
	
	
	
	private ArrayList<ChordProgressionAnalyzer> makeCPAList(ArrayList<ChordChunkList> cclList) 
	{
		ArrayList<ChordProgressionAnalyzer> cpaList = new ArrayList<ChordProgressionAnalyzer>();
		for (ChordChunkList ccl: cclList)
		{
			cpaList.add(new ChordProgressionAnalyzer(ccl));
		}
		return cpaList;
	}

	
	
	private ArrayList<ChordChunkList> breakIntoAnalysisCells() 
	{
		ChordChunkList baseCCL = new ChordChunkList(DEFAULT_REZ, "baseChunkList");
//		baseClip.name = "baseClip";
//		baseClip.loopEnd = lc.loopEnd;
		ArrayList<ChordChunkList> cclList = new ArrayList<ChordChunkList>();
		ChordChunk tempcc;
		
		for (int i = 0; i < ccl.size(); i++)
		{
			ChordChunk cc = ccl.chunkList.get(i);
			ChordChunk cc2 = cc.next();
			ChordChunk cc3 = cc2.next();
			ChordChunk cc4 = cc3.next();
			int rootMove_1to2 = (cc.next().rootModNote() + 12 - cc.rootModNote()) % 12;
			int rootMove_2to3 = (cc3.rootModNote() + 12 - cc2.rootModNote()) % 12;
			int rootMove_3to4 = (cc4.rootModNote() + 12 - cc3.rootModNote()) % 12;
			int add = 1;
			
			if (isolated_iiV(cc, cc2, cc3, cc4, rootMove_1to2, rootMove_2to3, rootMove_3to4)){
				add = 2;
			} else if (isolated_iibII(cc, cc2, cc3, cc4, rootMove_1to2, rootMove_2to3, rootMove_3to4)){
				add = 2;
			} else if (ii_V_I(cc, cc2, cc3, cc4, rootMove_1to2, rootMove_2to3, rootMove_3to4)){
				add = 3;
			} else if (ii_V_VI(cc, cc2, cc3, cc4, rootMove_1to2, rootMove_2to3, rootMove_3to4)){
				add = 3;
			} else if (upATone(cc, cc2, cc3, cc4, rootMove_1to2, rootMove_2to3, rootMove_3to4)){
				add = 2;
			} else if (loose_V_I(cc, cc2, cc3, cc4, rootMove_1to2, rootMove_2to3, rootMove_3to4)){
				add = 2;
			}
			
			if (add == 1)
			{
				baseCCL.addChunk(cc);
				//System.out.println(cc.chordToString() + " to baseClip");
			} 
			else 
			{
				ChordChunkList tempccl = new ChordChunkList(DEFAULT_REZ, cc.chordSymbol());
				for (int readIndex = 0; readIndex < add; readIndex++){
					if (i + readIndex < ccl.size()){
						tempcc = ccl.chunkList.get(i + readIndex);
						tempccl.addChunk(tempcc);
						//System.out.println(tempcc.toString());
						//System.out.println(tempcc.chordToString() + " to cell\n------------------------------");
					} 					
				}
				i += add - 1;
				cclList.add(tempccl);
			}
		}
		if (!(baseCCL.size() == 0))
		{
			cclList.add(baseCCL);
		}
		return cclList;
	}
	
	

	private boolean loose_V_I(ChordChunk cc, ChordChunk cc2, ChordChunk cc3,
			ChordChunk cc4, int rootMove_1to2, int rootMove_2to3, int rootMove_3to4) 
	{
		if (
				rootMove_1to2 == 5
				&& cc.isDominantType()
				&& (!cc2.isDominantType() && (cc2.isMajorType() || cc2.isMinorType()))
				)
		{
			return true;
		}
		return false;
	}
	
	
	
//	private void addChunkToChunkList(ChordChunkList ccl, ChordChunk cc) 
//	{
//		ccl.addChunk(cc);		
//	}

	
	
	private boolean upATone(ChordChunk cc, ChordChunk cc2, ChordChunk cc3,
			ChordChunk cc4, int rootMove_1to2, int rootMove_2to3, int rootMove_3to4) 
	{
		if (rootMove_1to2 == 2)
		{
			if (cc.isMajorType() && (cc2.isMajorType() || cc2.isMinorType()))
			{
				return true;
			}
		}
		return false;
	}
	
	
	
	private boolean ii_V_VI(ChordChunk cc, ChordChunk cc2, ChordChunk cc3, ChordChunk cc4, int rootMove_1to2, int rootMove_2to3, int rootMove_3to4) 
	{
		if (
				rootMove_1to2 == 5 
				&& (cc.isMinorType() || cc.isDiminishedType() || cc.isHalfDiminishedType())
				&& (cc2.isDominantType() || cc2.isMajorType())
				)
		{
			if (rootMove_2to3 == 2)
			{
				if (cc3.isMajorType())
				{
					return true;
				} 
				else if(cc3.isMinorType())
				{
					if (
							(cc4.isDominantType() || cc4.isMajorType())
							&& rootMove_3to4 == 5)
					{
						return false;
					} 
					else 
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
	private boolean ii_V_I(ChordChunk cc, ChordChunk cc2, ChordChunk cc3, ChordChunk cc4, int rootMove_1to2, int rootMove_2to3, int rootMove_3to4) 
	{
		if (
				rootMove_1to2 == 5 
				&& (cc.isMinorType() || cc.isDiminishedType() || cc.isHalfDiminishedType())
				&& (cc2.isDominantType() || cc2.isMajorType())
				)
		{
			if (rootMove_2to3 == 5)
			{
				if (cc3.isMajorType())
				{
					if (cc3.isDominantType()) return false; else return true;
				} 
				else if(cc3.isMinorType())
				{
					if (
							(cc4.isDominantType() || cc4.isMajorType())
							&& rootMove_3to4 == 5)
					{
						return false;
					} 
					else 
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
	private boolean isolated_iiV(ChordChunk cc, ChordChunk cc2, ChordChunk cc3,
			ChordChunk cc4, int rootMove_1to2, int rootMove_2to3, int rootMove_3to4) 
	{
		if (
				rootMove_1to2 == 5 
				&& (cc.isMinorType() || cc.isDiminishedType() || cc.isHalfDiminishedType())
				&& (cc2.isDominantType() || cc2.isMajorType())
				)
		{
			if (rootMove_2to3 == 5)
			{
				if (cc3.isMajorType())
				{
					if (cc3.isDominantType()) return true; else return false;					
				} 
				else if (
						(cc3.isMinorType() || cc3.isDiminishedType() || cc3.isHalfDiminishedType() || cc3.isDominantType())
						&& rootMove_3to4 == 5
						&& (cc4.isDominantType() || cc4.isMajorType())
						)
				{
					return true;
				}
			} 
			else if (rootMove_2to3 == 2) return false; else return true;			
		}
		return false;
	}
	
	
	
	private boolean isolated_iibII(ChordChunk cc, ChordChunk cc2, ChordChunk cc3,
			ChordChunk cc4, int rootMove_1to2, int rootMove_2to3, int rootMove_3to4) 
	{
		if (
				rootMove_1to2 == 11 
				&& (cc.isMinorType() || cc.isDiminishedType() || cc.isHalfDiminishedType())
				&& (cc2.isDominantType() || cc2.isMajorType())
				)
		{
			if (rootMove_2to3 == 11)
			{
				if (cc3.isMajorType())
				{
					if (cc3.isDominantType()) return false; else return true;					
				} 
				else if (
						(cc3.isMinorType() || cc3.isDiminishedType() || cc3.isHalfDiminishedType() || cc3.isDominantType())
						&& (rootMove_3to4 == 5 || rootMove_3to4 == 11)
						&& (cc4.isDominantType() || cc4.isMajorType())
						)
				{
					return true;
				}
			} 
			else 
			{
				return true;
			}
		}
		return false;
	}

	
	
}
