package main.java.da_utils.udp.udp_receiver;

/*
 * 
 */

import java.io.IOException;
import java.util.ArrayList;

import acm.program.ConsoleProgram;


public class UDPReceiverConsoleTest extends ConsoleProgram implements UDPReceiverParent{

	
	public void run(){
		setSize(700, 900);
		Thread t1 = new Thread(new UDPReceiver(this, 7802, 1000, 128));
		t1.start();
		
		try {
			System.in.read();
			System.out.println("Enter pressed....");
		} catch (IOException e){
			System.out.println(e);
		}
		System.out.println("ending...");
		
	}

	@Override
	public void udpMessageIn(String msg) {
		println("udpMessageIn: " + msg);
		
	}

	@Override
	public void consolePrint(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void udpMessageList(ArrayList<Object> oList) {
		// this is the one that must be called to make stuff happen
		// also will be specific to the project the UDPReceiver is being used for
		// Instruction below is commented out as it is Specific to the CombReComb project and 
		// thgis is now a generic version in DAUtils
		Instruction ins = new Instruction(oList);
		println(ins.toString());
	}


}



