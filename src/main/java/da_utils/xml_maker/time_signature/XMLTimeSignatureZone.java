package main.java.da_utils.xml_maker.time_signature;

public class XMLTimeSignatureZone {

	
	public int beats;				// numerator of timesignature
	public int beatType;				// denominator of timesignature
	public int barCount;				// barCount

	public XMLTimeSignatureZone(int beats, int beatType, int barCount){
		this.beats = beats;
		this.beatType = beatType;
		this.barCount = barCount;
	}
	public String beatsAsString(){
		return Integer.toString(beats);
	}
	public String beatTypeAsString(){
		return Integer.toString(beatType);
	}
	public String toString(){
		return "TimeSignatureZone " + beats + "/" + beatType + " bar count=" + barCount;
	}
	public double lengthInQuarters(){
		return (double)beats / beatType * 4.0;
	}
	public double zoneLength(){
		return lengthInQuarters() * barCount;
	}
}
