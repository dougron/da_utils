package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;

public class DrumStaticVariables {

	public static final int kikNote = 36;
	public static final int[] kikDynamic = new int[]{60, 84, 100, 127};		// always 4 items for dynamics
	public static final String kikDescriptor = "kik";
	
	public static final int snrNote = 38;
	public static final int rimNote = 37;
	public static final int[] snrDynamic = new int[]{60, 84, 100, 127};		// always 4 items for dynamics
	public static final String snrDescriptor = "snr";
	
	public static final int cHatNote = 42;
	public static final int footHatNote = 44;
	public static final int oHatNote = 46;
	public static final int[] hatDynamic = new int[]{40, 60, 84, 100};		// always 4 items for dynamics
	public static final String hatDescriptor = "hat";

	
	public static final int floorTomNote = 41;
	public static final int lowTomNote = 45;
	public static final int midTomNote = 48;
	public static final int hiTomNote = 50;
	public static final int[] tomDynamic = new int[]{60, 84, 100, 127};		// always 4 items for dynamics
	public static final String tomDescriptor = "tom";

	
	public static final int lowCrashNote = 49;
	public static final int hiCrashNote = 57;
	public static final int chinaNote = 52;
	public static final int splashNote = 55;
	public static final int[] crashDynamic = new int[]{60, 84, 100, 127};		// always 4 items for dynamics
	public static final String crashDescriptor = "crash";

	
	public static final int midRideNote = 51;
	public static final int edgeRideNote = 50;
	public static final int bellRideNote = 59;
	public static final int[] rideDynamic = new int[]{60, 84, 100, 127};		// always 4 items for dynamics
	public static final String rideDescriptor = "ride";
	
}
