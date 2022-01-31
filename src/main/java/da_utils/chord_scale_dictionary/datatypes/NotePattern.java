package main.java.da_utils.chord_scale_dictionary.datatypes;
import java.util.ArrayList;
import java.util.Iterator;

/* wrapper for the notePattern data from the ChordScaleAnalyzer
 * 
 */
public class NotePattern {

	public int index;
	public String notePatternString;
	public Integer[] notePatternArr;
	public ArrayList<NotePatternChord> chordOptions = new ArrayList<NotePatternChord>();
	public static final NotePatternChord nullNotePatternChord = new NotePatternChord(0);	// 0 - arb value. is not used to instantiate null NotePatternChord
	
	public NotePattern(int ind, String np){
		index = ind;
		if (index > -1){
			notePatternString = np;
			makeNotePatternArr(np);
		}	
	}
	public NotePattern(int ind){
		index = -1;		// no other reason for there to be only one instantiating argument
	}
	public NotePatternChord simpleNotePatternChord(){
		if (chordOptions.size() == 0){
			return nullNotePatternChord;
		} else {
			return chordOptions.get(0);
		}
		
	}
	public String toString(){
		String ret;
		if (index == -1){
			ret = "null NotePattern";
		} else {
			ret = "index: " + index + " notePatternString: " + notePatternString + " notePatternArr: ";
			for (int i = 0; i < notePatternArr.length; i++){
				ret = ret + notePatternArr[i] + ", ";
			}
		}
		if (chordOptions.size() > 0){
			Iterator<NotePatternChord> it = chordOptions.iterator();
			while (it.hasNext()){
				ret = ret + "\n       chordOption: " + it.next().toString();
			}
		} else {
			//ret = ret + "\n       no attached chords";
		}
		
		return ret;
	}
	public String toStringShort(){
		if (index == -1){
			return "null NotePattern";
		} else {
			return index + ": " + notePatternString;
		}
	}
	private void makeNotePatternArr(String np){
		String[] npp = np.split("-");
		notePatternArr = new Integer[npp.length];
		for (int i = 0; i < npp.length; i++){
			notePatternArr[i] = Integer.parseInt(npp[i]);
		}
	}
}
