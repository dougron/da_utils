package main.java.da_utils.algorithmic_models.pipeline.utilities;

public class RenderNumber {

	public static int renderCount = 0;
	
	
	public static int getRenderCount(){
		int count = renderCount;
		renderCount++;
		return count;
	}
}
