package main.java.da_utils.static_chord_scale_dictionary;

public class ModeObject {

	public int startNote;
	public int noteCount;
	public String keyMapIndex;
	public String name;
	private int[][] chromaticMode;	//[0] - degree name (characteristicall 1-7 in 7 note scale) [0] - chromatic adjustment -1,1 - chromatic raised or lowered note 0 = diatonic
	private int[] diatonicModeIntervals;
	
	
	public ModeObject(int startNote, int noteCount, String keyMapIndex, String name)
	{
		this.name = name;
		this.startNote = startNote;
		this.noteCount = noteCount;
		this.keyMapIndex = keyMapIndex;
		this.chromaticMode = CSD.getChromaticModeModel(this);
		this.diatonicModeIntervals = CSD.getDiatonicModeDegrees(this);
	}
	
	
	
	public int[][] getChromaticModeModel()
	{
		return chromaticMode;
	}
	
	
	public int[] getDiatonicIntervals()
	{
		return diatonicModeIntervals;
	}
}
