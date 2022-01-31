package main.java.da_utils.udp.udp_utils;


import acm.program.ConsoleProgram;


public class OSCMessMakerConsoleTest extends ConsoleProgram{
	
	public void run(){
		setSize(300, 700);
		UDPConnection conn = new UDPConnection(7800);
		OSCMessMaker mess = new OSCMessMaker();
		mess.addItem(3.5);
		
		conn.sendUDPMessage(mess);
		println(mess.toString());
	}
	


}
