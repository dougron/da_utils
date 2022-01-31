package main.java.da_utils.udp.udp_receiver;

import java.util.ArrayList;

/*
 * decodes UDP message
 */

public class UDPByteArrayDecoder {
	
	
	public static ArrayList<Object> decodeUDPMessage(byte[] barr){
		ArrayList<Object> list = new ArrayList<Object>();
		
		for (TypedByteList tbl: getByteList(barr)){
			list.add(tbl.getAsObject());
		}
		
		return list;
	}
	
	
	
	public static ArrayList<TypedByteList> getByteList(byte[] barr){
		int x = 0;
		boolean headerLoading = true;
		boolean typeListLoading = false;
		boolean typeListWaiting = false;
		boolean nextIsWaiting = false;
		boolean nextIsLoading = false;
		boolean readyForEnd = false;
		ArrayList<TypedByteList> byteList = new ArrayList<TypedByteList>();
		TypedByteList list = new TypedByteList();
		ArrayList<Byte> typeList = new ArrayList<Byte>();
		int typeListIndex = 0;
		int nextLoadingType = -1;
		int loadingCount = 0;
		
		
		
		for (byte b: barr){
			if (headerLoading){
				if (b == 0){
					headerLoading = false;
					typeListWaiting = true;
					byteList.add(list);
					list = new TypedByteList();
				} else {
					list.add(b);
				}
				
			} else if (typeListLoading){
				if (b == 0){
					typeListLoading = false;
					nextIsWaiting = true;
				} else {
					if (b != 44) typeList.add(b);
				}
			} else if (nextIsLoading){
				
				switch (nextLoadingType){
				case TypedByteList.isString:	
					if (b == 0){
						nextIsLoading = false;
						nextIsWaiting = true;
						byteList.add(list);
						list = new TypedByteList();
					} else {
						list.add(b);
					}
					break;
				case TypedByteList.isInt:	
//					list.add(b);
					if (loadingCount == 3){
						list.add(b);
						nextIsLoading = false;
						nextIsWaiting = true;
						byteList.add(list);
						list = new TypedByteList();
						loadingCount = 0;
					} else {
						list.add(b);
						loadingCount++;
					}
					break;
				case TypedByteList.isFloat:	
//					list.add(b);
					if (loadingCount == 3){
						list.add(b);
						nextIsLoading = false;
						nextIsWaiting = true;
						byteList.add(list);
						list = new TypedByteList();
						loadingCount = 0;
					} else {
						list.add(b);
						loadingCount++;
					}
					break;
				}
			}
			
			
			x++;
			if (x == 4){
				x = 0;
				if (typeListWaiting){
					typeListWaiting = false;
					typeListLoading = true;
				} else if (nextIsWaiting){
					nextIsWaiting = false;
					nextIsLoading = true;
					if (typeListIndex < typeList.size()){
						nextLoadingType = getNextLoadingType(typeList, typeListIndex);
						list.type = nextLoadingType;
						typeListIndex++;
					} else  {
						readyForEnd = true;
//						byteList.add(list);
					}
				}
			}
			if (readyForEnd) break;
		}
//		for (Byte b: typeList){
//			System.out.println(b);
//		}
		return byteList;
	}
	
	private static int getNextLoadingType(ArrayList<Byte> typeList, int typeListIndex) {
		if (typeList.get(typeListIndex) == 115) return TypedByteList.isString;
		if (typeList.get(typeListIndex) == 105) return TypedByteList.isInt;
		if (typeList.get(typeListIndex) == 102) return TypedByteList.isFloat;
		return -1;
	}

	public static void main(String[] args){
		String msg = "poopy doopy 123 23.456";
		// poopy doooopy zzzzz 1238 123.456 hi oh
		
		// 115 = string
		// 105 = int
		// 102 = float
		byte[] barr = new byte[]{
				// header ---- 
				112,	111,	111,	112,	
				121,	0,	0,	0	,
				// typeList -----
				44	,115,	115,	105	,
				102	,115,	115,	0	,
				// string ----
				100	,111,	111,	111	,
				111	,112,	121,	0	,
				// string -----
				122	,122,	122,	122	,
				122	,0,	0,	0	,
				// int ------- 
				0	,0,	4,	-42	,
				// float ------------
				66	,-10	,-23,	121	,
				// string --------
				104	,105	,0,	0	,
				// string ---------
				111	,104	,0,	0	,
				0	,0,	0	,0	,
				0	,0,	0	,0	,
				0	,0,	0	,0	,
				0	,0,	0	,0	,
				0	,0,	0	,0	,
				0	,0,	0	,0	,
				0	,0,	0	,0	,
				0	,0,	0	,0	,
		};
		
		for (TypedByteList list: UDPByteArrayDecoder.getByteList(barr)){
//			String str = "";
//			for (Byte b: list){
//				str += b + "\t";
//			}
			System.out.println(list.toString());
		}
		for (Object o: UDPByteArrayDecoder.decodeUDPMessage(barr)){
			System.out.println(o);
		}
		
	}

}
