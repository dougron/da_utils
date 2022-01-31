package main.java.da_utils.algorithmic_models.melody_segmenter;

import acm.program.ConsoleProgram;

public class TemperleysMetricalPositionConsoleTest extends ConsoleProgram {

	public void run(){
		setSize(700, 700);
		int signum = 4;
		int sigdenom = 4;
//		double[] profile = new double[]{0.25, 0.5, 1.0, 2.0, 4.0, 8.0};
		double topLevelLength = 8.0;
		int[] subdiv = new int[]{2, 2, 2, 2, 2};	// number of bars, beats in bar, subdivision of tactus, subdivision of subdivision of tactus
		// interestingly 4/4 is treated as a compound time signature of 2 bars of 2/4
		// this raises the question as to how to deal with for instance the 5/4 in take5 where
		// there is a (effectively a bar of 3/4 and 2/4)
		TemperleysMetricalPosition tmp = new TemperleysMetricalPosition(topLevelLength, subdiv);
		println("profile: " + tmp.profileToString());
		
		for (double d = 0.0; d < 21; d += 0.25){

			d = (int)(d * 100) / 100.0;
			
			int[] metricalPosArr = tmp.makeMetricalPosArr(d);
			String str = makeTabbedArrString(metricalPosArr);
			println(d + " := "  + str);
		}
	}

	private String makeTabbedArrString(int[] arr) {
		String str = "";
		for (int i: arr){
			str += "\t" + i;
		}
		return str;
	}

	
	
}
