package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;
import java.util.Random;

import acm.program.ConsoleProgram;


public class TestDifferentWeightings extends ConsoleProgram{
	
	public void run(){
		setSize(700, 900);
		double[] weighting = new double[]{10, 10, 10, 5};
		Random rnd = new Random();
		for (int i =0; i < 20; i++){
			double d = rnd.nextDouble();
			double sum = 0.0;
			for (double dd: weighting){
				sum += dd;
			}
			d = d * sum;	
			print(d + ": ");
			int index = -1;
			while (d > 0){
				index++;
				d -= weighting[index];								
			}
			print(index + "\n");
		}
		
	}

}
