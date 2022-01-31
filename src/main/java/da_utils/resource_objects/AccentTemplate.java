package main.java.da_utils.resource_objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;




import DLEditDistance.DLEditDistance;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterSortInterface;
import main.java.da_utils.resource_objects.chance_item.ChanceItem;

public class AccentTemplate  implements FilterSortInterface{

	public ArrayList<ChanceItem> chanceItemList = new ArrayList<ChanceItem>();
	public RandomNumberSequence rnd = new RandomNumberSequence(16, 1);
	public double length;
	public LiveClip clip;
	private static DLEditDistance dled = new DLEditDistance();
	private ArrayList<String> commentList = new ArrayList<String>(); 
	public String name = "";
	
	public int tempSimilScore = 0;
	
	
	public AccentTemplate(){
	
	}
	public AccentTemplate(LiveClip clip){
		this.clip = clip;
		this.length = clip.length;
		makeName();
	}
	public AccentTemplate(RandomNumberSequence rnd, double length){
		this.rnd = rnd;
		this.length = length;
	}
	public void makeNewRandomSequence(int length, int seed){
		rnd = new RandomNumberSequence(length, seed);
	}
	public void setLength(double l){
		length = l;
	}
	public void makeTemplate(){
		double endPos = 0;
		rnd.reset();
		clip = new LiveClip(0, 0);
		while (true){
			double len = getNextItem();
			if (endPos + len >= length){
				len -= endPos + len - length;
				clip.addNote(0, endPos, len, 100, 0);
				break;
			}
			clip.addNote(0, endPos, len, 100, 0);
			endPos += len;
		}
	}
	public void addChanceItem(double item, double chance){
		if (chanceItemList == null){
			chanceItemList = new ArrayList<ChanceItem>();
		}
		chanceItemList.add(new ChanceItem(item, chance));
		Collections.sort(chanceItemList, ChanceItem.longestToShortestItemComparator);
	}
	public void clear(){
		chanceItemList.clear();
	}
	public boolean isSameAs(AccentTemplate testAT){
		if (clip.noteList.size() != testAT.clip.noteList.size()){
			return false;
		}
		for (int i = 0; i < clip.noteList.size(); i++){
			LiveMidiNote thisNote = clip.noteList.get(i);
			LiveMidiNote thatNote = testAT.clip.noteList.get(i);
			if (thisNote.position == thatNote.position && thisNote.length == thatNote.length){
				
			} else {
				return false;
			}
		}
		return true;
	}
	public String singleLineToString(){
		String ret = "opt:";
		for (ChanceItem ci: chanceItemList){
			ret += ci.item + ",";
		}
		ret += " clip:";
		for (LiveMidiNote lmn: clip.noteList){
			ret += lmn.length + "/";
		}
		ret += " sync=" + syncopationScore();
		ret += " rDens=" + clip.rhythmicDensity();
		ret += " regul=" + clip.rhythmicRegularity();
		return ret;
	}
	public String lengthsToString(){
		String ret = "";
		for (LiveMidiNote lmn: clip.noteList){
			ret += lmn.length + "/";
		}
		ret += name;
		return ret;
	}
	public String singleLineToString(AccentTemplate atSimil){
		String ret = singleLineToString();
		setTempSimilScore(atSimil);
		ret += " simil=" + tempSimilScore;
		return ret;
	}
	public void setTempSimilScore(AccentTemplate atSimil){
		tempSimilScore = dled.getShortestEditDistance(clip.dlDistanceLengthString(), atSimil.clip.dlDistanceLengthString(), DLEditDistance.SEPARATOR);
	}
	public double syncopationScore(){
		return clip.syncopationScore();
	}
	
	public static Comparator<AccentTemplate> syncScoreComparator = new Comparator<AccentTemplate>(){
		public int compare(AccentTemplate at1, AccentTemplate at2){
			if (at1.syncopationScore() < at2.syncopationScore()) return -1;
			if (at1.syncopationScore() > at2.syncopationScore()) return 1;
			return 0;
		}
	};
	public static Comparator<AccentTemplate> rhythmicDensityComparator = new Comparator<AccentTemplate>(){
		public int compare(AccentTemplate at1, AccentTemplate at2){
			double at1Score = at1.clip.rhythmicDensity();
			double at2Score = at2.clip.rhythmicDensity();
			if (at1Score < at2Score) return -1;
			if (at1Score > at2Score) return 1;
			return 0;
		}
	};
	public static Comparator<AccentTemplate> rhythmicRegularityComparator = new Comparator<AccentTemplate>(){
		public int compare(AccentTemplate at1, AccentTemplate at2){
			double at1Score = at1.clip.rhythmicRegularity();
			double at2Score = at2.clip.rhythmicRegularity();
			if (at1Score < at2Score) return -1;
			if (at1Score > at2Score) return 1;
			return 0;
		}
	};
	public static Comparator<AccentTemplate> similScoreComparator = new Comparator<AccentTemplate>(){
		public int compare(AccentTemplate at1, AccentTemplate at2){
			if (at1.tempSimilScore < at2.tempSimilScore) return -1;
			if (at1.tempSimilScore > at2.tempSimilScore) return 1;
			return 0;
		}
	};
	public static Comparator<AccentTemplate> syncThenSimilScoreComparator = new Comparator<AccentTemplate>(){
		public int compare(AccentTemplate at1, AccentTemplate at2){
			if (at1.syncopationScore() < at2.syncopationScore()) return -1;
			if (at1.syncopationScore() > at2.syncopationScore()) return 1;
			if (at1.tempSimilScore < at2.tempSimilScore) return -1;
			if (at1.tempSimilScore > at2.tempSimilScore) return 1;
			return 0;
		}
	};
	
	public void addComment(String str){
		commentList.add(str);
	}
	public String tabDelimitedToString(){
		String ret = "";
		for (ChanceItem ci: chanceItemList){
			ret += ci.item + ", ";
		}
		ret += "\t";
		for (LiveMidiNote lmn: clip.noteList){
			ret += lmn.length + ", ";
		}
		ret += "\t" + Double.toString(syncopationScore()).replace(".", ",");
		ret += "\t" + Double.toString(clip.rhythmicDensity()).replace(".", ",");
		ret += "\t" + Double.toString(clip.rhythmicRegularity()).replace(".", ",");
		ret += "\t" + tempSimilScore;
		
		ret += "\t" + clip.evenQuarterDurationScore();
		ret += "\t" + clip.oddQuarterDurationScore();
		ret += "\t" + clip.evenEighthsDurationScore();
		ret += "\t" + clip.oddEighthsDurationScore();
		ret += "\t" + clip.evenSixteenthsDurationScore();
		ret += "\t" + clip.oddSixteenthsDurationScore();
		
		ret += "\t" + clip.evenQuarterNotePosScore();
		ret += "\t" + clip.oddQuarterNotePosScore();
		ret += "\t" + clip.evenEighthsNotePosScore();
		ret += "\t" + clip.oddEighthsNotePosScore();
		ret += "\t" + clip.evenSixteenthsNotePosScore();
		ret += "\t" + clip.oddSixteenthsNotePosScore();
		
		ret += "\t";
		for(String comment: commentList){
			ret += comment + ", ";
		}
		return ret;
	}
	public String tabDelimitedColumnHeaders(){
		String ret = "";
		ret += "chanceItems";
		ret += "\tnoteLengths";
		ret += "\tsyncScore";
		ret += "\trhythmicDensity";
		ret += "\trhythmicRegularity";
		ret += "\tsimilarityScore";
		
		ret += "\tevenQuartersDuration";
		ret += "\toddQuartersDuration";
		ret += "\tevenEighthsDuration";
		ret += "\toddEighthsDuration";
		ret += "\tevenSixteenthsDuration";
		ret += "\toddSixteenthsDuration";
		
		ret += "\tevenQuartersPos";
		ret += "\toddQuartersPos";
		ret += "\tevenEighthsPos";
		ret += "\toddEighthsPos";
		ret += "\tevenSixteenthsPos";
		ret += "\toddSixteenthsPos";
		
		ret += "\tcomments";
		return ret;
	}
	public double getScoreFromIndex(int index){
		switch (index){
		case LiveClip.SYNC: return syncopationScore();
		case LiveClip.SIMIL: return tempSimilScore;
		case LiveClip.RDENS: return clip.rhythmicDensity();
		case LiveClip.REGUL: return clip.rhythmicRegularity();
			
		}
		return 0.0;
	}
	public FilterObject getFilterObject(){
		return new FilterObject(name, this);
	}
// FilterSortMethod --------------------------------------------------------
	public FilterSortInterface getFilteSortObject(){
		return this;
	}
	
// privates=====================================================================
	private void makeName(){
		name = "";
		for (LiveMidiNote lmn: clip.noteList){
			if (durationLetter.containsKey(lmn.length)){
				name += durationLetter.get(lmn.length);
			}
			
		}
	}
	private double getChanceTotal(){
		double chanceTotal = 0;
		for (ChanceItem ci: chanceItemList){
			chanceTotal += ci.chance;
		}
		return chanceTotal;
	}
	private double getNextItem(){
		double chanceTotal = getChanceTotal();
		double chance = rnd.next();
		double runningChanceTotal = 0.0;
		boolean flag = true;
//		MaxObject.post("----------------------------------------------------------\nchanceTotal=" + chanceTotal + " chance=" + chance);
		for (ChanceItem ci: chanceItemList){
			if (flag){
				runningChanceTotal = ci.chance;
				flag = false;
			}
//			MaxObject.post("ChanceItem: " + ci.item + ", " + ci.chance + " runningChanceTotal=" + runningChanceTotal);
			if (chance <= runningChanceTotal / chanceTotal){
				return ci.item;
			}
			runningChanceTotal += ci.chance;
		}
		return chanceItemList.get(chanceItemList.size() - 1).item;
	}
	
	private static final Map<Double, String> durationLetter = Collections.unmodifiableMap(
			new HashMap<Double, String>(){{
				put(0.25, "A");
				put(0.5, "B");
				put(0.75, "C");
				put(1.0, "D");
				put(1.25, "E");
				put(1.5, "F");
				put(1.75, "G");
				put(2.0, "H");
				put(2.25, "I");
				put(2.5, "J");
				put(2.75, "K");
				put(3.0, "L");
				put(3.25, "M");
				put(3.5, "N");
				put(3.75, "O");
				put(4.0, "P");
			}});
}
