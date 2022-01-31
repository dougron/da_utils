package main.java.da_utils.oddball_utils.octant_util;

import java.util.ArrayList;

import acm.program.ConsoleProgram;

public class OctantUtilConsoleTest extends ConsoleProgram{

	
	public void run(){
		setSize(700, 700);
		gridAttempt();
	}
	private void gridAttempt(){
		int[][] grid = new int[9][9];
		for (int y = 0; y < grid.length; y++){
			double ypos = y - 4.0;
			int[] dd = grid[y];
			for (int x = 0; x < dd.length; x++){
				double xpos = x - 4.0;
				
				dd[x] = OctantUtil.getOctant(0, 0, xpos, ypos);
			}
		}
		for (int[] dd: grid){
			for (int d: dd){
				print(d + ",");
			}
			print("\n");
		}
	}
	private void listAttempt(){
		ArrayList<Double[]> xyPair = new ArrayList<Double[]>();
		for (double x = -4; x < 5; x++){
			for (double y = -4; y < 5; y ++){
				Double[] dd = new Double[]{x, y};
				xyPair.add(dd);
			}
		}
		for (Double[] dd: xyPair){
			println(dd[0] + ", " + dd[1] + " - " + OctantUtil.getOctant(0, 0, dd[0], dd[1]));
		}
	}
}
