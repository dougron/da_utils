package main.java.da_utils.resource_objects;

import acm.program.ConsoleProgram;

public class ContourDataConsoleTest extends ConsoleProgram {

	
	public void run(){
		setSize(700, 700);
		ContourData cd = new ContourData();
		cd.newData(0.5, 0.0, 1, ContourData.LOW_MIDPOINT);
		println(cd.name);
	}
}
