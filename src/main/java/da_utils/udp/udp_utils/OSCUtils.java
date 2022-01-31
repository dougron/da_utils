package main.java.da_utils.udp.udp_utils;
import java.util.ArrayList;
/*
 * some utils for dealing with OSC formatted stuff.
 */
public class OSCUtils {

	public static ArrayList<OSCAtom> makeAtomList(byte[] barr){
//		ArrayList<OSCAtom> atList = new ArrayList<OSCAtom>();
		ArrayList<Byte> typeList = new ArrayList<Byte>();
		boolean foundTypeList = false;
		int typeListEndIndex = 0;
		int nextItemIndex = 0;
		for (int i = 0; i < barr.length; i++){
			//System.out.println(barr[i]);
			if (barr[i] == byte_comma){   		// looking for 1st comma, which indicates beginning of typelist
				foundTypeList = true;
			}
			if (foundTypeList){
				if (barr[i] == byte_s || barr[i] == byte_i || barr[i] == byte_f){
					typeList.add(barr[i]);
				} else if (barr[i] == 0){
					typeListEndIndex = i;
					break;
				}
			}
		}
		nextItemIndex = ((typeListEndIndex + 4) / 4) * 4;
//		println("typeList----------------------------");
//		for (byte b: typeList){
//			println(b);
//		}
//		println("typeListEndIndex: " + typeListEndIndex);
//		println("nextItemIndex: " + nextItemIndex);
		ArrayList<OSCAtom> atList = addAtomsToList(barr, nextItemIndex, typeList);
		 
		
		return atList;
	}
	private static ArrayList<OSCAtom> addAtomsToList(byte[] barr, int startIndex, ArrayList<Byte> typeList){
		ArrayList<OSCAtom> atList = new ArrayList<OSCAtom>();
		byte[] tempByteArr;
		for (byte type: typeList){
			if (type == byte_i){
				tempByteArr = new byte[4];
				for (int i = 0; i < 4; i++){
					tempByteArr[i] = barr[startIndex + i];
				}
				atList.add(makeIntOSCAtom(tempByteArr));
				startIndex += 4;
			} else if (type == byte_f){
				tempByteArr = new byte[4];
				for (int i = 0; i < 4; i++){
					tempByteArr[i] = barr[startIndex + i];
				}
				atList.add(makeFloatOSCAtom(tempByteArr));
				startIndex += 4;
			} else if (type == byte_s){
				boolean loop = true;
				int tIndex = startIndex;
				while(loop){
					if (barr[tIndex] == 0){
						loop = false;
					} else {
						tIndex++;
					}
				}
				tempByteArr = new byte[tIndex - startIndex];
				for (int i = 0; i < tempByteArr.length; i++){
					tempByteArr[i] = barr[startIndex + i];
				}
				String decoded = "";
				try {
					decoded = new String(tempByteArr, "UTF-8");
				} catch (Exception e){
					
				}
				atList.add(new OSCAtom(decoded));
				startIndex = ((startIndex + tempByteArr.length + 4) / 4) * 4;
			}
		}
		
		return atList;
	}
	private static OSCAtom makeFloatOSCAtom(byte[] tbar){
		//println("makeFloat-------------");
		//for (byte b: tbar){
		//	print(b + ",");
		//}
		//println("\n");
		float x = java.nio.ByteBuffer.wrap(tbar).getFloat();
		return new OSCAtom(x);
	}
	private static OSCAtom makeIntOSCAtom(byte[] tbar){
		//println("makeInt-------------");
		//for (byte b: tbar){
		//	print(b + ",");
		//}
		//println("\n");
		int x = java.nio.ByteBuffer.wrap(tbar).getInt();
		
//		int value = ((tbar[3] & 0xFF) << 0) |
//				((tbar[2] & 0xFF) << 8) |
//				((tbar[1] & 0xFF) << 16) |
//				((tbar[0] & 0xFF) << 32);
		return new OSCAtom(x);
	}
	
	private static final int byte_f = 102;
	private static final int byte_i = 105;
	private static final int byte_s = 115;
	private static final int byte_comma = 44;
}
