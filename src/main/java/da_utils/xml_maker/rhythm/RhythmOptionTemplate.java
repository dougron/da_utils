package main.java.da_utils.xml_maker.rhythm;

public class RhythmOptionTemplate {

	
	public int signatureNumerator;
	public int signatureDenominator;
	public RhythmOption[] rhythmOptionNoteArr;
	public double length;
	public RhythmOption[] rhythmOptionRestArr;

	public RhythmOptionTemplate(int signum, int sigdenom, double length, RhythmOption[] roNoteArr, RhythmOption[] roRestArr){
		this.signatureNumerator = signum;
		this.signatureDenominator = sigdenom;
		this.rhythmOptionNoteArr = roNoteArr;
		this.rhythmOptionRestArr = roRestArr;
		this.length = length;
	}
}
