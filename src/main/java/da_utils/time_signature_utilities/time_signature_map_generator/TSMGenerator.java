package main.java.da_utils.time_signature_utilities.time_signature_map_generator;

import java.util.ArrayList;

import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;
import main.java.da_utils.time_signature_utilities.time_signature_map.TSMFromGen;
import main.java.da_utils.time_signature_utilities.time_signature_map.TSMapInterface;

public class TSMGenerator implements TSGenInterface{

//	private TSGenInterface[] tsArr;
	ArrayList<TSGenInterface> list = new ArrayList<TSGenInterface>();	// this can be a TimeSignature or a TSGenItem
	private int repeat = 1;		// number of times the list will be repeated  



	public TSMGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public TSMGenerator(TimeSignature[] tsArr, int repeat) {
		this.repeat = repeat;
		for (TimeSignature ts: tsArr) {
			list.add(ts);
		}
	}
	
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	
	public void addGenItem(TSGenInterface tsg) {
		list.add(tsg);
	}
	
	@Override
	public TSMapInterface getTimeSignatureMap(int barCount) {		// this is a top level request and will repeat the last item from the list on the last repeat until the barcount is complete
																	// this could be a TimeSignature or a TSGenItem
		TSMapInterface tsm = new TSMFromGen(barCount);		// 
		for (int rep = 0; rep < repeat; rep++) {
			for (int i = 0; i < list.size(); i++) {
				TSGenInterface tsg = list.get(i);
				if (rep == repeat - 1 && i == list.size() - 1) {
					if (tsg instanceof TimeSignature) {
						while (tsm.addTimeSignature((TimeSignature)tsg)) {}
					} else {
						TSMGenerator tsgi = (TSMGenerator)tsg;
						while (tsm.addTimeSignatures(tsgi.getTSList())) {}
					}
				} else {
					if (tsg instanceof TimeSignature) {
						if (!tsm.addTimeSignature((TimeSignature) tsg)) break;
					} else {
						TSMGenerator tsgi = (TSMGenerator) tsg;
						if (!tsm.addTimeSignatures(tsgi.getTSList())) break;
					}
				}
			} 
		}
		return tsm;
	}
	
	
	private ArrayList<TimeSignature> getTSList() {
		ArrayList<TimeSignature> list = new ArrayList<TimeSignature>();
		for (int rep = 0; rep < repeat; rep++) {
			for (TSGenInterface tsg: this.list) {
				if (tsg instanceof TimeSignature) {
					list.add((TimeSignature)tsg);
				} else {
					TSMGenerator tsgi = (TSMGenerator) tsg;
					list.addAll(tsgi.getTSList());
				}
			}
		}
		
		return list;
	}

	public static void main(String[] args) {
		TSMGenerator tsg = new TSMGenerator();
		tsg.addGenItem(TimeSignature.FOUR_FOUR);
		tsg.addGenItem(TimeSignature.THREE_FOUR);
		tsg.setRepeat(2);
		
		TSMGenerator tsgMaster = new TSMGenerator();
		tsgMaster.addGenItem(tsg);
		tsgMaster.addGenItem(TimeSignature.FIVE_EIGHT_32);
				
//		TimeSignature ts = TimeSignatureMapGenerator.FOUR_FOUR;
		
		TSMapInterface tsm = tsgMaster.getTimeSignatureMap(16);
		System.out.println(tsm.toString());
		
	}
	
}
