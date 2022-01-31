package main.java.da_utils.oddball_utils.octant_util;

public class OctantUtil {

	public static int getOctant(double centreX, double centreY, double x2, double y2){
		double dx = x2 - centreX;
		double dy = y2 - centreY;
		int one;
		int two;
		int three;
		int four;
		if (dx < dy / 2 * -1) one = 0; else one = 1;
		if (dx / 2 < dy * -1) two = 0; else two = 1;
		if (dx / 2 < dy) three = 0; else three = 1;
		if (dx < dy / 2) four = 0; else four = 1;
		return one + two + (three * 3) + (four * 4);
	}
	public static final int NORTH = 2;
	public static final int NORTH_EAST = 6;
	public static final int NORTH_WEST = 1;
	public static final int EAST = 9;
	public static final int WEST = 0;
	public static final int SOUTH_EAST = 8;
	public static final int SOUTH_WEST = 3;
	public static final int SOUTH = 7;
	
}
