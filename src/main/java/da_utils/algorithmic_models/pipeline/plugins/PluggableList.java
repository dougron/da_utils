package main.java.da_utils.algorithmic_models.pipeline.plugins;

import java.util.ArrayList;

/*
 * wrapper of ArrayList<Pluggable> but with metadata
 */

public class PluggableList {
	
	public ArrayList<Pluggable> plugList = new ArrayList<Pluggable>();
	public int undoNumber;
	
	
	public PluggableList(int undoCount) {
		undoNumber = undoCount;
	}

	public void add(Pluggable p){
		plugList.add(p);
	}
	
	public void add(int index, Pluggable p){
		plugList.add(index, p);
	}
	
	public void addAll(PluggableList pl){
		plugList.addAll(pl.plugList);
	}
	
}
