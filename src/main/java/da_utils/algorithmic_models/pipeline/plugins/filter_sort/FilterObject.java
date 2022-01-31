package main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort;
public class FilterObject {

	public FilterSortInterface object;
	public String name;
	
	public FilterObject(String name, FilterSortInterface obj){
		this.name = name;
		object = obj;
	}

	public String toString(){
		return "FilterObject: " + name + " object class is " + object.getClass();
	}
}
