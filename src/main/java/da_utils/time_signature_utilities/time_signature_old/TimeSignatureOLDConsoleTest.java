package main.java.da_utils.time_signature_utilities.time_signature_old;

import java.util.HashMap;
import java.util.TreeMap;

import acm.program.ConsoleProgram;
import main.java.da_utils.time_signature_utilities.beat_hierarchy.BeatHierarchyItem;

public class TimeSignatureOLDConsoleTest extends ConsoleProgram {

	
	public void run(){
		setSize(700, 900);
		TimeSignatureOLD ts = new TimeSignatureOLD(11, 4, new int[]{4, 3, 4});
		long startTime = System.nanoTime();
		BeatHierarchyItem wMap = ts.weightingMap();
		long endTime = System.nanoTime();
		println(startTime + ", " + endTime + " elapsed time=" + (endTime - startTime));
		println(wMap.toString());
		startTime = System.nanoTime();
		wMap = ts.weightingMap();
		endTime = System.nanoTime();
		println("elapsed time=" + (endTime - startTime));
		startTime = System.nanoTime();
		wMap = ts.weightingMap();
		endTime = System.nanoTime();
		println("elapsed time=" + (endTime - startTime));
		startTime = System.nanoTime();
		wMap = ts.weightingMap();
		endTime = System.nanoTime();
		println("elapsed time=" + (endTime - startTime));
		startTime = System.nanoTime();
		wMap = ts.weightingMap();
		endTime = System.nanoTime();
		println("elapsed time=" + (endTime - startTime));
	}
}
