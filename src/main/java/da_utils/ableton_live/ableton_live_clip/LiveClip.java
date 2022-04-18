package main.java.da_utils.ableton_live.ableton_live_clip;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
/*
 * a class which stores note data for a clip in live
 * 
 * same as the Clip class except that LiveClip uses an ArrayList to store data
 * basically easier to code
 * 
 * as of 17May 2017, no longer contains chunkArray. The function of LiveClip is to
 * 	1.	store an arrayList of LiveMidiNote instances
 * 	2. 	return quantized versions of said noteList
 * 	3. 	may move all the scores to a wrapper class as well
 */
import java.util.List;

import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.ControllerClip;
import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.PitchBendClip;
import main.java.da_utils.combo_variables.IntAndString;
import main.java.da_utils.static_chord_scale_dictionary.CSD;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;



public class LiveClip {
	
	public ArrayList<LiveMidiNote> noteList = new ArrayList<LiveMidiNote>(); 
//	public ChordChunkArray chunkArray;
	public int currentWriteIndex;
	
	public double length = 4.0;
	public double loopStart = 0.0;					// negative values all mean 'not applicable'
	public double loopEnd = 4.0;
	public double startMarker = -1.0;
	public double endMarker = -1.0;
	public int signatureNumerator = -1;
	public int signatureDenominator = -1;
	public double offset = 0.0;						// to synchronise the start times of various length clips
	public int clip;
	public int track;
	public int clipObjectIndex = 0;
	public String name = "clip_name";
	
	private boolean hasSyncScore = false;
	private double syncScore = 0.0;
	private TimeSignature timeSignature;
//	private boolean hasChordAnalysis = false;
	private HashMap<String, ArrayList<LiveMidiNote>> noteListQuantMap = new HashMap<String, ArrayList<LiveMidiNote>>();
	private int minimum;
	private boolean hasMinMaxAvg = false;
	private int maximum;
	private int average;
	private int averageVelocity;
	private int lowVelocity;
	private int highVelocity;
	String chordsClipPath;
	boolean hasChordsClipPath = false;
	public PitchBendClip pb = null;
	public ArrayList<ControllerClip> cList = new ArrayList<ControllerClip>();

	
	public LiveClip(int t, int c){
		track = t;
		clip = c;
		setTimeSignature(TIMESIG_4_4);		// default
	}
	public LiveClip(){
		setTimeSignature(TIMESIG_4_4);		// default
	}
	public LiveClip(double len, double lStart, double lEnd, double startMark, double endMark, int sigNum, int sigDenom, double offset, int clip, int track, String name, int clipObjectIndex){
		length = len;
		loopStart = lStart;
		loopEnd = lEnd;
		startMarker = startMark;
		endMarker = endMark;
		signatureNumerator = sigNum;
		signatureDenominator = sigDenom;
		this.offset = offset;
		this.clip = clip;
		this.track = track;
		this.name = name;
		this.clipObjectIndex = clipObjectIndex;
	}
	public LiveClip(BufferedReader in){
		instantiateClipFromBufferedReader(in);
	}
	
	public void instantiateClipFromBufferedReader(BufferedReader in) {
		noteList.clear();
		try {
			while(true){
				String str = in.readLine();
				if (str == null) break;
				//println(str);
				String[] strArr = str.split(",");
				if (strArr[0].equals("LiveMidiNote")){
					int note = Integer.valueOf(strArr[1]);
					double pos = Double.valueOf(strArr[4]);
					double len = Double.valueOf(strArr[5]);
					int vel = Integer.valueOf(strArr[6]);
					addNote(note, pos, len, vel, 0);
				} else if (strArr[0].equals("length")){
					length = Double.valueOf(strArr[1]);
				} else if (strArr[0].equals("loopStart")){
					loopStart = Double.valueOf(strArr[1]);
				} else if (strArr[0].equals("loopEnd")){
					loopEnd = Double.valueOf(strArr[1]);
				} else if (strArr[0].equals("startMarker")){
					startMarker = Double.valueOf(strArr[1]);
				} else if (strArr[0].equals("endMarker")){
					endMarker = Double.valueOf(strArr[1]);
				} else if (strArr[0].equals("signatureNumerator")){
					signatureNumerator = Integer.valueOf(strArr[1]);
				} else if (strArr[0].equals("signatureDenominator")){
					signatureDenominator = Integer.valueOf(strArr[1]);
				} else if (strArr[0].equals("name")){
					name = strArr[1];
				} else if (strArr[0].equals("chordsClipPath")){
					chordsClipPath = strArr[1];
					hasChordsClipPath = true;
				}
			}	
			length = loopEnd - loopStart;
		} catch (Exception ex){
			
		}
		
	}
	public void sortNoteList(){
		Collections.sort(noteList, LiveMidiNote.posAndNoteComparator);
	}
	public ArrayList<LiveMidiNote> noteList(String rez){
		if (!noteListQuantMap.containsKey(rez)){
			makeNewQuantMapItem(rez);
		}
		return noteListQuantMap.get(rez);
	}
	
	
	
	public void addNote(int note, double pos, double len, int vel, int mute){
		noteList.add(new LiveMidiNote(note, pos, len, vel, mute));
		noteListQuantMap.clear();
	}
	
	public void addNote(LiveMidiNote lm){
		noteList.add(lm);
		noteListQuantMap.clear();
	}
	public void addNoteList(List<LiveMidiNote> lmnList){
		for (LiveMidiNote lmn: lmnList){
			noteList.add(lmn);
		}
		noteListQuantMap.clear();
	}
	public void appendClip(LiveClip lc){
		// adds contents onto the end of the clip. does not do controllers yet
		for (LiveMidiNote lmn: lc.noteList){
			LiveMidiNote newNote = lmn.clone();
			newNote.position += length;
			noteList.add(newNote);
		}
		length += lc.length;
		noteListQuantMap.clear();
	}
	public void setLength(double l){				// this has no direct impact on the length of the clip 
		length = l;									// loopStart and loopEnd are the important ones
	}
	public void setName(String n){
		name = n;
	}
	public void setLoopStart(double l){
		loopStart = l;
	}
	public void setLoopEnd(double l){
		loopEnd = l;
	}
	public void setStartMarker(double l){
		startMarker = l;
	}
	public void setEndMarker(double l){
		endMarker = l;
	}
	public void setSignNum(int l){
		signatureNumerator = l;
	}
	public void setSignDenom(int l){
		signatureDenominator = l;
	}
	public void setTimeSignature(int ts){
		switch (ts){
			case TIMESIG_4_4:	signatureNumerator = 4;
								signatureDenominator = 4;
								timeSignature = TimeSignature.FOUR_FOUR;
								break;
			case TIMESIG_3_4:	signatureNumerator = 3;
								signatureDenominator = 4;
								timeSignature = TimeSignature.THREE_FOUR;
								break;	
			case TIMESIG_2_2:	signatureNumerator = 2;
								signatureDenominator = 2;
								timeSignature = TimeSignature.TWO_TWO;
								break;
		}
	}
	public void setTimeSignature(TimeSignature ts) {
		timeSignature = ts;
	}
	public void makeChordAnalysis(){
		//turnClipDataIntoChunks();
		//analyzeChunks();
	}
//	public void makeChordAnalysis(String rez){
//		if (!hasChordAnalysis){
//			chunkArray = turnClipDataIntoChunks(rez);
//			hasChordAnalysis = true;
//			//analyzeChunks();
//		}
//		
//	}
//	public boolean hasChordAnalysis(){
//		return hasChordAnalysis;
//	}
	public int size(){
		return noteList.size();
	}
	public Iterator<LiveMidiNote> iterator(){
		return noteList.iterator();
	}
	public LiveClip clone(){
		LiveClip c = new LiveClip(track, clip);
		Iterator<LiveMidiNote> it = noteList.iterator();
		while (it.hasNext()){
			LiveMidiNote lmn = it.next();
			c.addNote(lmn.clone());
		}
		c.setSignDenom(signatureDenominator);
		c.setSignNum(signatureNumerator);
		c.setEndMarker(endMarker);
		c.setLength(length);
		c.setLoopEnd(loopEnd);
		c.setLoopStart(loopStart);
		c.setStartMarker(startMarker);
		c.name = name;
		return c; 
	}
	public void clear(){
		noteList.clear();
	}
	public LiveClip split(double start, double end){
		LiveClip lc = new LiveClip(track, clip);
		for (LiveMidiNote lmn: noteList){
			if (lmn.position >= start && lmn.position < end){
				lc.addNote(lmn.clone());
			}
		}
		return lc;
	}
	public LiveMidiNote lastNote(){
		if (noteList.size() == 0){
			return LiveMidiNote.NULL_NOTE;
		} else {
			return noteList.get(noteList.size() - 1);		// assumes noteList is sorted according to position
		}
		
	}


	
	public String toString(){
		String ret = "LiveClip contents\n";
		if (name != "")  {
			ret = ret + "name: " + name + " \n";
		} else {
			ret = ret + "no name!!\n";
		}
		for (LiveMidiNote l: noteList){
			ret = ret + "----" + l.toString() + "\n";
		}
		
		ret = ret + "track " + track + " clip: " + clip + "\n";
		ret = ret + "length:       " + length + "\n";
		ret = ret + "loopStart:    " + loopStart + "\n";
		ret = ret + "loopEnd:      " + loopEnd + "\n";
		ret = ret + "StartMarker:  " + startMarker + "\n";
		ret = ret + "endMarker:    " + endMarker + "\n";
		ret = ret + "timeSignature:  " + signatureNumerator + "/" + signatureDenominator  + "\n";
		if (hasChordsClipPath){
			ret += "chordsClipPath: " + chordsClipPath + "\n";
		} else {
			ret += "no chordsClipPath\n";
		}
		ret = ret + "barCount:    " + barCount();
		if (pb != null){
			ret += "\nhas pitchBendClip";
		}
		return ret;
	}
	public String lengthAndFormToString(){
		length = loopEnd - loopStart;
		double barLength = CSD.getBarLength(this.signatureNumerator, this.signatureDenominator);
		int barcount = (int)(length / barLength);
		String str = "length=" + length + " timeSignature:  " + signatureNumerator + "/" + signatureDenominator + " barCount=" + barcount;
		return str;
	}
	
// scoring methods for LiveClips' use in an AccentTemplate.
	public double syncopationScore(){
		// returns a weighted note to beat distance measure score for the clip as espoused by
		//Gomez, Rappaport, Melvin and Toussaint in 'Mathematical Measures of Syncopation'
		
		// currently only working for notelist where note length is also the distance to next note
		
		if (hasSyncScore){
			return syncScore;
		} else {
			double syncTotal = 0.0;
			for (LiveMidiNote lmn: noteList){
				//System.out.println(lmn.toString());
				double tx = getWNBM_Tx(lmn);
				//System.out.println(tx);
				double noteScore;
				if (tx == 0){
					noteScore = 0;
				} else {
					//System.out.println("(lmn.length % WNBM_BEAT) + lmn.length=" + ((lmn.length % WNBM_BEAT) + lmn.length));
					if ((lmn.position % WNBM_BEAT) + lmn.length <= WNBM_BEAT){
						noteScore = 1 / tx;
					} else {
						if ((lmn.position % WNBM_BEAT) + lmn.length <= WNBM_BEAT * 2){
							noteScore = 2 / tx;
						} else {
							noteScore = 1 / tx;
						}
					}
				}
				//System.out.println("noteScore=" + noteScore);
				syncTotal += noteScore;
			}
			hasSyncScore = true;
			syncScore = syncTotal;
			return syncTotal;
		}
		
	}
	private double getWNBM_Tx(LiveMidiNote lmn){
		double p = lmn.position - WNBM_BEAT;
		double lastpos = lmn.position;
		while (Math.abs(p) < Math.abs(lastpos)){
			//System.out.println("lastpos=" + lastpos + " p=" + p);
			lastpos = p;
			p -= WNBM_BEAT;
		}
		//System.out.println("T(x)=" + lastpos);
		return Math.abs(lastpos);
	}
	public String dlDistanceLengthString(){
		// makes string of comma separated token of note length for use in edit distance comparison
		String ret = "";
		for (int i = 0; i < noteList.size(); i++){
			LiveMidiNote lmn = noteList.get(i);
			ret += lmn.length;
			if (i < noteList.size() - 1){
				ret += DL_SEPERATOR;
			}
		}
		return ret;
	}
	public double rhythmicDensity(){
		// measures number of notes / length. currently only useful for monophonic clips such as LiveClips
		// in AccentTemplates
		return (double)noteList.size() / length;
	}
	public double rhythmicRegularity(){
		// sum of the difference in length of each note from the previous note / number of notes
		if (noteList.size() == 0){
			return 0;
		} else {
			double score = 0;
			LiveMidiNote previousNote = noteList.get(0);
			LiveMidiNote currentNote;
			LiveMidiNote nextNote;
			for (int i = 1; i < noteList.size(); i++){
				currentNote = noteList.get(i);
				if (i == noteList.size() - 1){
					nextNote = noteList.get(0).clone();
					nextNote.position += length;
				} else {
					nextNote = noteList.get(i + 1);
				}
				double currentLength = nextNote.position - currentNote.position;
				double previousLength = currentNote.position - previousNote.position;
				score += Math.abs(currentLength - previousLength);
				previousNote = currentNote;
			}
			return score / noteList.size();
		}
		
	}
	
	public int evenQuarterDurationScore(){
		int count = 0;
		for (LiveMidiNote lmn: noteList){
			if (arrayContains(EVEN_QUARTERS, lmn.length)) count++;
		}
		return count;
	}
	public int oddQuarterDurationScore(){
		int count = 0;
		for (LiveMidiNote lmn: noteList){
			if (arrayContains(ODD_QUARTERS, lmn.length)) count++;
		}
		return count;
	}
	public int evenEighthsDurationScore(){
		int count = 0;
		for (LiveMidiNote lmn: noteList){
			if (arrayContains(EVEN_EIGHTHS, lmn.length)) count++;
		}
		return count;
	}
	public int oddEighthsDurationScore(){
		int count = 0;
		for (LiveMidiNote lmn: noteList){
			if (arrayContains(ODD_EIGHTHS, lmn.length)) count++;
		}
		return count;
	}
	public int evenSixteenthsDurationScore(){
		int count = 0;
		for (LiveMidiNote lmn: noteList){
			if (arrayContains(EVEN_SIXTEENTHS, lmn.length)) count++;
		}
		return count;
	}
	public int oddSixteenthsDurationScore(){
		int count = 0;
		for (LiveMidiNote lmn: noteList){
			if (arrayContains(ODD_SIXTEENTHS, lmn.length)) count++;
		}
		return count;
	}
	// notePos scores only cater for bars up to 4/4 in length
	public int evenQuarterNotePosScore(){
		int count = 0;
		for (LiveMidiNote lmn: noteList){
			if (arrayContains(EVEN_QUARTERS, lmn.position % POS_SCORE_DEFAULT_LENGTH)) count++;
		}
		return count;
	}
	public int oddQuarterNotePosScore(){
		int count = 0;
		for (LiveMidiNote lmn: noteList){
			if (arrayContains(ODD_QUARTERS, lmn.position % POS_SCORE_DEFAULT_LENGTH)) count++;
		}
		return count;
	}
	public int evenEighthsNotePosScore(){
		int count = 0;
		for (LiveMidiNote lmn: noteList){
			if (arrayContains(EVEN_EIGHTHS, lmn.position % POS_SCORE_DEFAULT_LENGTH)) count++;
		}
		return count;
	}
	public int oddEighthsNotePosScore(){
		int count = 0;
		for (LiveMidiNote lmn: noteList){
			if (arrayContains(ODD_EIGHTHS, lmn.position % POS_SCORE_DEFAULT_LENGTH)) count++;
		}
		return count;
	}
	public int evenSixteenthsNotePosScore(){
		int count = 0;
		for (LiveMidiNote lmn: noteList){
			if (arrayContains(EVEN_SIXTEENTHS, lmn.position % POS_SCORE_DEFAULT_LENGTH)) count++;
		}
		return count;
	}
	public int oddSixteenthsNotePosScore(){
		int count = 0;
		for (LiveMidiNote lmn: noteList){
			if (arrayContains(ODD_SIXTEENTHS, lmn.position % POS_SCORE_DEFAULT_LENGTH)) count++;
		}
		return count;
	}
	public boolean isSameAs(LiveClip lc){
		if (noteList.size() == lc.noteList.size()){
			sortNoteList();
			lc.sortNoteList();
			for (int i = 0; i < noteList.size(); i++){
				if (!noteList.get(i).isSameAs(lc.noteList.get(i))){
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
	public static LiveClip emptyClip(){
		if (emptyClip == null){
			emptyClip = new LiveClip(0, 0);
			emptyClip.name = "---";
		}
		return emptyClip;
	}
	public TimeSignature timeSignature(){
		if (timeSignature == null)
		{
			for (TimeSignature ts: TimeSignature.searchableMap.values())
			{
				if (ts.getNumerator() == signatureNumerator 
						&& ts.getDenominator() == signatureDenominator)
				{
					return ts;
				}
			}
			// if there is no matching preset timesignature it will return 4/4. 
			// if the time signature is custom, it is expected that care would 
			// have been taken to explicitly set it.
			// as of december 2021, a custom timesignature will not survive being
			// written to and read from a .liveclip file.
			return TimeSignature.FOUR_FOUR;	
		}
		return timeSignature;
	}
	public String getClipAsTextFile(){
		String str = "name," + name + "\n";
		str += "length," + length + "\n";
		str += "loopStart," + loopStart + "\n";
		str += "loopEnd," + loopEnd + "\n";
		str += "startMarker," + startMarker + "\n";
		str += "endMarker," + endMarker + "\n";
		str += "signatureNumerator," + signatureNumerator + "\n";
		str += "signatureDenominator," + signatureDenominator + "\n";
		if (hasChordsClipPath){
			str += "chordsClipPath," + chordsClipPath + "\n";
		}
		str += "type,note,modnote,octave,position,length,velocity\n";
//		Collections.sort(noteList, LiveMidiNote.positionComparator);
		sortNoteList();
		for (LiveMidiNote lmn: noteList){
			//System.out.println(lmn.oneLineToString());
			str += lmn.oneLineToString() + "\n";
		}
		return str;
	}
	public void transpose(int i){
		for (LiveMidiNote lmn: noteList){
			lmn.note += i;
			while (lmn.note < 0) lmn.note += 12;		// will make octave corrections
			while (lmn.note > 127) lmn.note -= 12;
		}
	}
	
// privates --------------------------------------------------
	private void makeNewQuantMapItem(String rez) {
		ArrayList<LiveMidiNote> list = new ArrayList<LiveMidiNote>();
		for (LiveMidiNote lmn: noteList){
			list.add(lmn.quantizedClone(rez));
		}
		Collections.sort(list, LiveMidiNote.posAndNoteComparator);
		noteListQuantMap.put(rez, list);
	}
	private boolean arrayContains(double[] arr, double d){
		for (double ditem: arr){
			if (d == ditem) return true;
		}
		return false;
	}
	
	private static LiveClip emptyClip;

	public static final int TIMESIG_4_4 = 0;
	public static final int TIMESIG_COMMON = 0;
	public static final int TIMESIG_3_4 = 1;
	public static final int TIMESIG_2_2 = 2;
	public static final int TIMESIG_CUT_COMMON = 2;
	
	private static final double WNBM_BEAT = 1.0;		// weighted note to beat measure value for beat.....
	private static final String DL_SEPERATOR = ",";
	
	// duration or position stuff. not handling triplets yet. 
	private static final double[] EVEN_QUARTERS = new double[]{0.0, 2.0};
	private static final double[] ODD_QUARTERS = new double[]{1.0, 3.0};
	private static final double[] EVEN_EIGHTHS = new double[]{0.0, 1.0, 2.0, 3.0};
	private static final double[] ODD_EIGHTHS = new double[]{0.5, 1,5, 2,5, 3,5};
	private static final double[] EVEN_SIXTEENTHS = new double[]{0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5};
	private static final double[] ODD_SIXTEENTHS = new double[]{0.25, 0.75, 1.25, 1.75, 2.25, 2.75, 3.25, 3.75};
	private static final double POS_SCORE_DEFAULT_LENGTH = 4.0;	// temp for use in pos score methods where time signature may be undefined

	// indices for scores................
	public static final int SYNC = 0;
	public static final int SIMIL = 1;
	public static final int RDENS = 2;
	public static final int REGUL = 3;
	
	public static final int EVEN_4_DURATION = 4;
	public static final int ODD_4_DURATION = 5;
	public static final int EVEN_8_DURATION = 6;
	public static final int ODD_8_DURATION = 7;
	public static final int EVEN_16_DURATION = 8;
	public static final int ODD_16_DURATION = 9;
	
	public static final int EVEN_4_POSITION = 10;
	public static final int ODD_4_POSITION = 11;
	public static final int EVEN_8_POSITION = 12;
	public static final int ODD_8_POSITION = 13;
	public static final int EVEN_16_POSITION = 14;
	public static final int ODD_16_POSITION = 15;
	
	public static final int ZONE_2_COUNT = 16;
	
	public static final HashMap<Integer,IntAndString> scoreNameObjectMap = new HashMap<Integer,IntAndString>(){{
			put(SYNC, new IntAndString(SYNC, "sync"));
			put(SIMIL, new IntAndString(SIMIL, "simil"));
			put(RDENS, new IntAndString(RDENS, "rDens"));
			put(REGUL, new IntAndString(REGUL, "regul"));
			put(EVEN_4_DURATION, new IntAndString(EVEN_4_DURATION, "even4dur"));
			put(ODD_4_DURATION, new IntAndString(ODD_4_DURATION, "odd4dur"));
			put(EVEN_8_DURATION, new IntAndString(EVEN_8_DURATION, "even8dur"));
			put(ODD_8_DURATION, new IntAndString(ODD_8_DURATION, "odd8dur"));
			put(EVEN_16_DURATION, new IntAndString(EVEN_16_DURATION, "even16dur"));
			put(ODD_16_DURATION, new IntAndString(ODD_16_DURATION, "odd16dur"));
			put(EVEN_4_POSITION, new IntAndString(EVEN_4_POSITION, "even4pos"));
			put(ODD_4_POSITION, new IntAndString(ODD_4_POSITION, "odd4pos"));
			put(EVEN_8_POSITION, new IntAndString(EVEN_8_POSITION, "even8pos"));
			put(ODD_8_POSITION, new IntAndString(ODD_8_POSITION, "odd8pos"));
			put(EVEN_16_POSITION ,new IntAndString(EVEN_16_POSITION, "even16pos"));
			put(ODD_16_POSITION, new IntAndString(ODD_16_POSITION, "odd16pos"));
			put(ZONE_2_COUNT, new IntAndString(ZONE_2_COUNT, "zone2count"));
			
	}};


	public LiveMidiNote firstNote() {
		if (noteList.size() > 0){
			return noteList.get(0);
		}
		return null;
	}
	public void makeIntoNewTimeSignature(int[] ts) {
		// changes the positions, lengths etc in the current time signature to a proportuional position
		// in the new ts. particluarly for changing time signature in the RepetitionAnalysis
		
		double d = (double)ts[0] / (double)ts[1];
		double factor = ts[0] * CSD.getBeatLength(ts[1]) / (signatureNumerator * CSD.getBeatLength(signatureDenominator));
		for (LiveMidiNote lmn: noteList){
			double newpos = lmn.position * factor;
			double newlen = lmn.length * factor;
			lmn.position = newpos;
			lmn.length = newlen;
		}
		loopStart = loopStart * factor;
		loopEnd = loopEnd * factor;
		length = loopEnd - loopStart;
		if (startMarker > -1) startMarker = startMarker * factor;
		if (endMarker > -1) endMarker = endMarker * factor;
		signatureNumerator = ts[0];
		signatureDenominator = ts[1];
		
	}
	public LiveClip cloneIntoNewTimeSignature(int[] ts){
		LiveClip lc = clone();
		lc.makeIntoNewTimeSignature(ts);
		return lc;
	}
	public int minimum(){
		if (!hasMinMaxAvg){
			getMinimumMaximumAverage();
		}
		return minimum;
	}
	public int maximum(){
		if (!hasMinMaxAvg){
			getMinimumMaximumAverage();
		}
		return maximum;
	}
	public int average(){
		if (!hasMinMaxAvg){
			getMinimumMaximumAverage();
		}
		return average;
	}
	public int centre(){
		if (!hasMinMaxAvg){
			getMinimumMaximumAverage();
		}
		return (maximum - minimum) / 2 + minimum;
	}
	public int averageVelocity(){
		if (!hasMinMaxAvg){
			getMinimumMaximumAverage();
		}
		return averageVelocity;
	}
	public int lowVelocity(){
		if (!hasMinMaxAvg){
			getMinimumMaximumAverage();
		}
		return lowVelocity;
	}
	public int highVelocity(){
		if (!hasMinMaxAvg){
			getMinimumMaximumAverage();
		}
		return highVelocity;
	}
	private void getMinimumMaximumAverage() {
		if (noteList.size() == 0){
			minimum = 0;
			maximum = 0;
			average = 0;
			averageVelocity = 0;
			lowVelocity = 0;
			highVelocity = 0;
		} else {
			minimum = 128;
			maximum = -1;
			int total = 0;
			int velocityTotal = 0;
			for (LiveMidiNote lmn: noteList){
				if (lmn.note > maximum) maximum = lmn.note;
				if (lmn.note < minimum) minimum = lmn.note;
				total += lmn.note;
				velocityTotal += lmn.velocity;
			}
			average = total / noteList.size();
			averageVelocity = velocityTotal / noteList.size();
			
			int averageDistanceFromVelocityAverage = 0;
			for (LiveMidiNote lmn: noteList){
				averageDistanceFromVelocityAverage += Math.abs(averageVelocity - lmn.velocity);
			}
			averageDistanceFromVelocityAverage /= noteList.size();
			lowVelocity = averageVelocity - averageDistanceFromVelocityAverage;
			highVelocity = averageVelocity + averageDistanceFromVelocityAverage;
		}
		
		hasMinMaxAvg = true;
		
	}
	public double getBarLengthInQuarters(){
		return (double)this.signatureNumerator / (double)this.signatureDenominator * 4.0;
	}
	public int barCount(){		// currently assume no half bars in the form, hence int. This may change, or may need rounding in case of floating point problems.....
		return (int)(length / getBarLengthInQuarters());
	}
	public int loopEndBarCount(){
		return (int)(loopEnd / getBarLengthInQuarters());
	}
	public void setChordsClipPath(String path) {
		chordsClipPath = path;
		hasChordsClipPath = true;
		
	}
	public String getChordsClipPath(){
		return chordsClipPath;
	}
	public boolean hasChordsClipPath(){
		return hasChordsClipPath;
	}
	public int getNotationKeySignature(){
		// default for now is key of C, 0
		return 0;
	}
}
