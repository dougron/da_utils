package main.java.da_utils.algorithmic_models.melody_segmenter;

public class TemperleysMetricalPosition {
	
	double[] profile;

	public TemperleysMetricalPosition(double length, int[] subdiv){
		
		profile = makeProfile(length, subdiv);
	}
	
	
	private double[] makeProfile(double topLevelLength, int[] subdiv) {
		double x = topLevelLength;
		double[] arr = new double[subdiv.length + 1];
		arr[subdiv.length] = x;
		int index = subdiv.length - 1;
		for (int i: subdiv){
			x = x / i;
			arr[index] = x;
			index--;
		}
		return arr;
	}


	public String profileToString() {
		String str = "";
		for (double d: profile){
			str += d + ", ";
		}
		return str;
	}


	public int[] makeMetricalPosArr(double pos) {
		int[] arr = new int[profile.length - 1];
		for (int i = 0; i < profile.length - 1; i++){
			arr[i] = (int)(pos % profile[i + 1] / profile[i] + 1);
		}
		return arr;
	}
}
