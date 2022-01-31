package main.java.da_utils.chord_progression_analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
//import java.util.ArrayList;
//import java.util.Iterator;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.chord_progression_analyzer.chord_chunk.ChordChunkList;
import main.java.da_utils.test_utils.TestData;
import main.java.da_utils.xml_maker.MXM;
import main.java.da_utils.xml_maker.MusicXMLMaker;
import main.java.da_utils.xml_maker.key.XMLKey;
import main.java.da_utils.xml_maker.key.XMLKeyZone;
import main.java.da_utils.xml_maker.time_signature.XMLTimeSignatureZone;


public class ProgressionAnalyzerConsoleTest{

	private String path = "D:/Documents/repos/ChordProgressionTestFiles/";

//	private String file = "BlackOrpheus.chords.liveclip";
//	private String file = "BlueBossa.chords.liveclip";
//	private String file = "Blue3Bossa.chords.liveclip";
//	private String file = "Stella.chords.liveclip";
//	private String file = "Hallelujah.chords.liveclip";
//	private String file = "TickleToe.chords.liveclip";
	private String file = "Confirmation.chords.liveclip";
//	private String file = "chordTest.liveclip";
//	private String file = "BluesInF.chords.liveclip";
//	private String file = "DolphinDance.chords.liveclip";
//	private String file = "IRememberClifford.chords.liveclip";
//	private String file = "JoySpring.chords.liveclip";
	
//	private String file = "4chords.chords.liveclip";
//	private String file = "Blues7ths.chords.liveclip";
//	private String file = "BluesMinor7ths.chords.liveclip";
//	private String file = "BluesMinorTriads.chords.liveclip";
//	private String file = "BluesTriads.chords.liveclip";
//	private String file = "Canon.chords.liveclip";
//	private String file = "DescendingFlamencoVariation.chords.liveclip";
//	private String file = "DescendingFlamenco.chords.liveclip";
//	private String file = "DooWop.chords.liveclip";
//	private String file = "GoodLove.chords.liveclip";
//	private String file = "GuitarGentlyWeeps.chords.liveclip";
//	private String file = "HouseOfRisingSun.chords.liveclip";
//	private String file = "BluesMinor9ths.chords.liveclip";
	
//	private String file = "OpaSupaInstrumental.chords.liveclip";
//	private String file = "GuitarWeeps4bars.chords.liveclip";
//	private String file = "LongDescendingFlamenco.chords.liveclip";

	
	private String outputPath = "D:/Documents/miscForBackup/chordProgressionAnalyzerOutput/";//cpaTestOutput.xml.chords.liveclip";

	public ProgressionAnalyzerConsoleTest()
	{
		path += file;

		LiveClip lc = getLiveClip(path);

		
		ProgressionAnalyzer pa = new ProgressionAnalyzer(path);

		System.out.println(pa.dumpToString());
		System.out.println(pa.toString());
		System.out.println(pa.modeAnalysisToString());
		
		for (double pos = 0.0; pos < 20.0; pos += 0.6)
		{
			System.out.println("pos=" + pos);
			System.out.println(pa.getPrevailingCIKO(pos).toStringKeyChordAndFunction());
		}
		
//		exportToXML(lc, pa);
	}



	private void exportToXML(LiveClip lc, ProgressionAnalyzer pa)
	{
		outputPath += file + ".musicxml";
		
		XMLKey xmlKey = MXM.xmlKeyMap.get(0);		// default key of C just for expedience
		MusicXMLMaker mxm = new MusicXMLMaker(xmlKey);
		int barCount = (int)((lc.loopEnd - lc.loopStart) / lc.signatureDenominator);
		mxm.measureMap.addNewTimeSignatureZone(new XMLTimeSignatureZone(lc.signatureNumerator, lc.signatureDenominator, barCount)); 
		mxm.keyMap.addNewKeyZone(new XMLKeyZone(xmlKey, barCount));
		mxm.addPart(pa.name(), lc);
		addChordSymbols(mxm, pa);
//		mxm.addPart("variation", outputClip);
		mxm.makeXML(outputPath);
	}
	
	
	
	private LiveClip getEbtoCmClip()
	{
		return TestData.liveClipForTestForm();
	}
	
	
	
	private LiveClip getBitOfLiveClip() 
	{
		LiveClip lc = new LiveClip(0, 0);
		lc.length = 24.0;
		lc.loopStart = 0.0;
		lc.loopEnd = 24.0;
		lc.addNote(40, 8.0, 4.0, 100, 0);
		lc.addNote(44, 8.0, 4.0, 100, 0);
		lc.addNote(47, 8.0, 4.0, 100, 0);
		lc.addNote(45, 12.0, 4.0, 100, 0);
		lc.addNote(49, 12.0, 4.0, 100, 0);
		lc.addNote(52, 12.0, 4.0, 100, 0);
//		lc.addNote(41, 8.0, 4.0, 100, 0);
//		lc.addNote(45, 8.0, 4.0, 100, 0);
//		lc.addNote(48, 8.0, 4.0, 100, 0);
		return lc;
	}
	
	
	
	private LiveClip V7()
	{
		LiveClip lc = new LiveClip(0, 0);
		lc.length = 4.0;
		lc.loopStart = 0.0;
		lc.loopEnd = 4.0;
		lc.addNote(40, 0.0, 4.0, 100, 0);
		lc.addNote(44, 0.0, 4.0, 100, 0);
		lc.addNote(47, 0.0, 4.0, 100, 0);
		lc.addNote(50, 0.0, 4.0, 100, 0);
//		lc.addNote(49, 12.0, 4.0, 100, 0);
//		lc.addNote(52, 12.0, 4.0, 100, 0);
//		lc.addNote(41, 8.0, 4.0, 100, 0);
//		lc.addNote(45, 8.0, 4.0, 100, 0);
//		lc.addNote(48, 8.0, 4.0, 100, 0);
		return lc;
	}
	
	
	
	private void addChordSymbols(MusicXMLMaker mxm, ProgressionAnalyzer pa) 
	{
//		mxm.addTextDirection(cpa.name(), CSD.noteName(cpa.bestKeyAnalysis().keyIndex()), 0.0, MXM.PLACEMENT_ABOVE);
		for (ChordInKeyObject ciko: pa.analysisChordInKeyObjectList())
		{
			double position = ciko.position();
			mxm.addTextDirection(pa.name(), ciko.toStringKeyChordAndFunction(), position, MXM.PLACEMENT_ABOVE);
		}
		
	}
	public LiveClip getE7AmClip()
	{
		LiveClip lc = new LiveClip(0, 0);
		lc.length = 8.0;
		lc.startMarker = 0.0;
		lc.endMarker = 8.0;
		lc.addNote(52, 0.0, 4.0, 100, 0);
		lc.addNote(56, 0.0, 4.0, 100, 0);
		lc.addNote(59, 0.0, 4.0, 100, 0);
		lc.addNote(62, 0.0, 4.0, 100, 0);
		lc.addNote(57, 4.0, 4.0, 100, 0);
		lc.addNote(60, 4.0, 4.0, 100, 0);
		lc.addNote(64, 4.0, 4.0, 100, 0);

		return lc;
	}
	
	
	
	public LiveClip getSimpleLiveClip()
	{
		LiveClip lc = new LiveClip(0, 0);
		lc.length = 12.0;
		lc.startMarker = 0.0;
		lc.endMarker = 12.0;
		lc.addNote(40, 0.0, 4.0, 100, 0);
		lc.addNote(44, 0.0, 4.0, 100, 0);
		lc.addNote(47, 0.0, 4.0, 100, 0);
		lc.addNote(45, 4.0, 4.0, 100, 0);
		lc.addNote(49, 4.0, 4.0, 100, 0);
		lc.addNote(52, 4.0, 4.0, 100, 0);
		lc.addNote(41, 8.0, 4.0, 100, 0);
		lc.addNote(45, 8.0, 4.0, 100, 0);
		lc.addNote(48, 8.0, 4.0, 100, 0);
		return lc;
	}
	
	
	
	public LiveClip getFourChordsLiveClip()
	{
		LiveClip lc = new LiveClip(0, 0);
		lc.length = 16.0;
		lc.startMarker = 0.0;
		lc.endMarker = 16.0;
		lc.addNote(48, 0.0, 4.0, 100, 0);
		lc.addNote(52, 0.0, 4.0, 100, 0);
		lc.addNote(55, 0.0, 4.0, 100, 0);
		lc.addNote(47, 4.0, 4.0, 100, 0);
		lc.addNote(50, 4.0, 4.0, 100, 0);
		lc.addNote(55, 4.0, 4.0, 100, 0);
		lc.addNote(48, 8.0, 4.0, 100, 0);
		lc.addNote(52, 8.0, 4.0, 100, 0);
		lc.addNote(57, 8.0, 4.0, 100, 0);
		lc.addNote(48, 12.0, 4.0, 100, 0);
		lc.addNote(53, 12.0, 4.0, 100, 0);
		lc.addNote(57, 12.0, 4.0, 100, 0);
		return lc;
	}
	
	
	
	public LiveClip getLiveClip(String aPath)
	{
		LiveClip lc;
		try 
		{
			FileReader fr = new FileReader(aPath);
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
	
	
	
	public static void main(String[] args)
	{
		ProgressionAnalyzerConsoleTest test = new ProgressionAnalyzerConsoleTest();
	}
}
