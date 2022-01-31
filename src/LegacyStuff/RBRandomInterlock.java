package LegacyStuff;
import java.util.ArrayList;

import main.java.da_utils.resource_objects.RandomNumberSequence;
import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;

/*
 * generates a random interlocking pattern after being passed a TwoBarRhythmBuffer
 */
public class RBRandomInterlock {
	
	RandomNumberSequence rnd = new RandomNumberSequence(16, 127);	// 16 - arbitrary size, 127 - random seed value

	public RBRandomInterlock(){
		
	}
	public TwoBarRhythmBuffer makeInterlock(TwoBarRhythmBuffer rb){
		TwoBarRhythmBuffer newRb = new TwoBarRhythmBuffer();
		ArrayList<Integer> indexOptions = new ArrayList<Integer>();
		boolean loading = false;
		int i = 0;
		while (true){
			if (rb.buffy[i % rb.buffy.length] > 0){
				if (!loading){
					loading = true;
				} else {
					if (indexOptions.size() > 0){
						newRb.buffy[makeRandomChoice(indexOptions)] = 1;
						indexOptions.clear();
						if (i != i % rb.buffy.length) break;
					}
				}								
			} else {
				if (loading){
					indexOptions.add(i % rb.buffy.length);
				}				
			}
			if (!loading && i != i % rb.buffy.length) break;
			i++;
		}
		return newRb;
	}

	private int makeRandomChoice(ArrayList<Integer> iList){
		int choice = (int)(rnd.next() * iList.size());
		return iList.get(choice);
	}
}
