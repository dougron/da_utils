package main.java.da_utils.ableton_live.ableton_device_control_utils.device_parameter_utils;
/*
 * default name and parameter number for a certain type of parameter used with monotonous regularity by
 * a variety of devices. This is for the new (August 2015) system of addressing parameters by theior names 
 * rather trhan their controler numbers
 */
public class DefaultDeviceParameter {

	public String name;
	public int index;
	public double offValue;
	public double min = 0.0;
	public double max = 127.0;
	
	public DefaultDeviceParameter(String name, int index, double offValue){
		this.name = name;
		this.index = index;
		this.offValue = offValue;
	}
}
