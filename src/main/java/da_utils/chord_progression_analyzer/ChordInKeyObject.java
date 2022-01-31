package main.java.da_utils.chord_progression_analyzer;

import java.util.ArrayList;
import java.util.Comparator;

import main.java.da_utils.chord_progression_analyzer.chord_chunk.ChordChunk;
import main.java.da_utils.static_chord_scale_dictionary.CSD;
import main.java.da_utils.static_chord_scale_dictionary.ModeObject;

public class ChordInKeyObject {

	
	public ChordChunk cc;
	public int keyIndex;
	public String chordType;
	public String functionString;
	public int score;
	public int rootIndex;
	public int threeChordScore;
	public String degree;
	public ChordInKeyObject next;
	public ChordInKeyObject previous;
	public boolean isMajorKey = true;
	public boolean unequivocallyLocalMajor = false;
	public boolean unequivocallyLocalMinor = false;
	public int majorScore;
	public int minorScore;
	private ModeObject mode;
	private int[] modChordTones;
	private int[] modScaleTones;
	private int[] modNonTones;
	private int[] diatonicTones;
	private int[] modExtendedChordTones;
	private boolean hasChordScaleNonToneArrays = false;
	public String degreeAddendum;;

	public ChordInKeyObject(
			ChordChunk cc, 
			int keyIndex, 
			String chordType, 
			String functionString, 
			int score, 					// relevance in key, lower is better
			int rootIndex, 
			int threeChordScore,			// presence as a tonic, dominant or subdominant, higher is better
			String degree,
			int majorScore,
			int minorScore,
			ModeObject mode, 
			String degreeAddendum
			)
	{
		this.cc = cc;
		this.keyIndex = keyIndex;
		this.chordType = chordType;
		this.functionString = functionString;
		this.score = score;
		this.rootIndex = rootIndex;
		this.threeChordScore = threeChordScore;
		this.degree = degree;
		this.majorScore = majorScore;
		this.minorScore = minorScore;
		this.mode = mode;
		this.degreeAddendum = degreeAddendum;
		
//		if (minorScore > majorScore){
//			isMajorKey = false;
//		}
	}
	
	
	
	public String toString()
	{
		return compensatedChordString() + "(" + functionString + ")" + "(" + score + "," + threeChordScore + ")";
	}
	
	
	
	public String toStringKeyChordAndFunction()
	{
		return majorOrMinorKey() + ":" + compensatedChordString()+ "(" + functionString + ")";
	}
	
	
	
	public String toStringKeyChordAndSimpleFunction()
	{
		return majorOrMinorKey() + ":" + compensatedChordString()+ "(" + degree + degreeAddendum + ")";
	}
	
	
	
	private String compensatedChordString()
	{
		return CSD.noteNameWithKeyIndex(rootIndex, keyIndex) + cc.chordTypeName();
	}
	
	
	
	public double position()
	{
		return cc.position();
	}
	
	
	
	public static Comparator<ChordInKeyObject> positionComparator = new Comparator<ChordInKeyObject>()
	{
		public int compare(ChordInKeyObject ciko1, ChordInKeyObject ciko2)
		{
			if (ciko1.position() < ciko2.position()) return -1;
			if (ciko1.position() > ciko2.position()) return 1;
			return 0;
		}
	};
	
	
	
	private String majorOrMinorKey()
	{
		String str = CSD.noteNameWithKeyIndex(keyIndex, keyIndex);
		if (!isMajorKey)
		{
//			System.out.println(str + " is minor");
			str = str.substring(0, 1).toLowerCase() + str.substring(1);
//			System.out.println("becomes " + str);
		}
		return str;
	}
	
	
	
	public String keyQuality()
	{
		// 'major' or 'minor'
		if (isMajorKey)
		{
			return CSD.MAJOR;
		} 
		else 
		{
			return CSD.MINOR;
		}
	}

	
	
	public String toStringWithKey() 
	{
		return majorOrMinorKey() + ":" + toString();
	}
	
	
	
	public static boolean sameBasicChord(ChordInKeyObject c1, ChordInKeyObject c2) 
	{
		if (
				c1.rootIndex == c2.rootIndex
				&& c1.cc.isDiminishedSeventhType() == c2.cc.isDiminishedSeventhType()
				&& c1.cc.isDiminishedType() == c2.cc.isDiminishedType()
				&& c1.cc.isDominantType() == c2.cc.isDominantType()
				&& c1.cc.isHalfDiminishedType() == c2.cc.isHalfDiminishedType()
				&& c1.cc.isMajorType() == c2.cc.isMajorType()
				&& c1.cc.isMinorType() == c2.cc.isMinorType()){
			return true;
		}
		return false;
	}
	
	
	
	public String chordSymbol()
	{
		// chordroot and type
		return CSD.noteName(rootIndex) + chordType;
	}
	
	
	
	public int[][] chromaticChordScale()
	{
		return mode.getChromaticModeModel();
	}
	
	
	
	public int chordToneInterval(int[] diatonicChordTone)
	{
		// this has problems due to incomplete description of chromatic notes in the chromaticChordScale()
		// content. DO NOT USE UNTIL I ERASE>>>>>>>>>>>>>>>>>>>>>
		boolean isDiatonic = false;
		if (diatonicChordTone[1] == 0)
		{
			isDiatonic = true;
		}
		int interval = 0;
		for (int[] arr: chromaticChordScale())
		{
			boolean ccsIsDiatonic = false;
			if (arr[1] == 0) ccsIsDiatonic = true;
			if (diatonicChordTone[0] == arr[0] && isDiatonic == ccsIsDiatonic)
			{
				return interval;
			}
			
			interval++;
		}
		return 0;
	}
	
	
	
	public int diatonicScaleToneInterval(int chordTone)
	{
		int interval = 0;
		for (int[] arr: chromaticChordScale())
		{
			if (chordTone == arr[0] && arr[1] == 0)
			{
				return interval;
			}			
			interval++;
		}
		return 0;
	}
	
	
	
	public int[] getModChordTones()
	{
		if (!hasChordScaleNonToneArrays)
		{
			makeChordScaleNonToneArrays();
		} 
		return modChordTones;
	}
	
	
	
	public int[] getModScaleTones()
	{
		if (!hasChordScaleNonToneArrays)
		{
			makeChordScaleNonToneArrays();
		} 
		return modScaleTones;
	}
	
	
	
	public int[] getModNonTones()
	{
		if (!hasChordScaleNonToneArrays)
		{
			makeChordScaleNonToneArrays();
		} 
		return modNonTones;
	}
	
	
	
	public int[] getModExtendedChordTones()
	{
		if (modExtendedChordTones == null) makeModExtendedChordTones();
		return modExtendedChordTones;
	}

	
	
	
	private void makeModExtendedChordTones()
	{
		int[] arr = new int[cc.getNotePatternAnalysis().extendedChordToneIntervals().length];
		for (int i = 0; i < arr.length; i++)
		{
			arr[i] = (cc.getNotePatternAnalysis().extendedChordToneIntervals()[i] + rootIndex) % 12;
		}
		modExtendedChordTones = arr;
	}



	public ModeObject mode()
	{
		return mode;
	}
	
	
	
	public void associateThisCIKOWithChordChunk()
	{
		cc.setAssociatedChordInKeyObject(this);
	}
	
	
	
	private void makeChordScaleNonToneArrays()
	{
		int[][] ccs = chromaticChordScale();
		int[] chordTones = cc.getNotePatternAnalysis().intervals();
		for (int i = 0; i < chordTones.length; i++)
		{
			chordTones[i] = chordTones[i] % 12;		// intervals() above returns 9ths 11th and 13ths as actual intervals not mod intervals
		}
		ArrayList<Integer> scaleList = new ArrayList<Integer>();
		ArrayList<Integer> nonList = new ArrayList<Integer>();
		
		for (int i = 0; i < ccs.length; i++)
		{
			int[] arr = ccs[i];
			if (!arrContainsInt(chordTones, i))
			{
				if (arr[1] == 0)
				{
					scaleList.add(i);
				} 
				else 
				{
					nonList.add(i);
				}
			}
		}
		modScaleTones = makeIntArr(scaleList);
		makeAddRootIndexAndMod(modScaleTones);
		modNonTones = makeIntArr(nonList);
		makeAddRootIndexAndMod(modNonTones);
		makeAddRootIndexAndMod(chordTones);
		modChordTones = chordTones;
		this.hasChordScaleNonToneArrays  = true;
	}
	
	
	
	private void makeAddRootIndexAndMod(int[] arr) 
	{
		for (int i = 0; i < arr.length; i++)
		{
			arr[i] = (arr[i] + rootIndex) % 12;
		}		
	}
	
	
	
	private int[] makeIntArr(ArrayList<Integer> list) 
	{
		int[] arr = new int[list.size()];
		for (int i = 0; i < arr.length; i++)
		{
			arr[i] = list.get(i);
		}
		return arr;
	}
	
	
	
	private boolean arrContainsInt(int[] arr, int i) 
	{
		for (int test: arr)
		{
			if (test == i)
			{
				return true;
			}
		}
		return false;
	}



	public int[] getChordToneIntervals()
	{
		return cc.getNotePatternAnalysis().intervals();
	}



	
}
