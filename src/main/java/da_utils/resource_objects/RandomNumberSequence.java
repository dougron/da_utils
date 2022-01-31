package main.java.da_utils.resource_objects;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.cycling74.max.MaxObject;


public class RandomNumberSequence {
	
	public ArrayList<Double> randList = new ArrayList<Double>();
	public int size;
	public int readIndex = 0;
	public Random rnd;
	
	public RandomNumberSequence(int s, int seed){
		rnd = new Random(seed);
		size = s;
		generateNewSequence();
	}
	
	
	public void generateNewSequence(){
		randList.clear();
		rnd.nextDouble();
		for (int i = 0; i < size; i++){
			randList.add(rnd.nextDouble());
		}
		readIndex = 0;
	}
	
	public double next(){
		if (readIndex >= size){
			readIndex = 0;
		}
		//MaxObject.post(readIndex + " list size=" + randList.size());
		double ret = randList.get(readIndex);
		readIndex++;
		return ret;
	}
	
	public void newSeed(int seed){
		rnd = new Random(seed);
	}
	
	public void reset(){
		readIndex = 0;
	}
	public String toString(){
		String ret = "RandomNumberSequence:\n";
		Iterator<Double> it = randList.iterator();
		while(it.hasNext()){
			ret = ret + it.next() + ", ";
		}
		return ret;
	}
	

}
