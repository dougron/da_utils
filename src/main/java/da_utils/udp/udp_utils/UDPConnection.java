package main.java.da_utils.udp.udp_utils;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;

/*
 * localhost udp connection........
 */

public class UDPConnection {
	
	private int port;
	private String addrString = "192.168.1.3";

	public UDPConnection(int port){
		this.port = port;
	}
	public void sendUDPMessage(OSCMessMaker mm){
		try {
//			InetAddress address = Inet4Address.getLocalHost();
			InetAddress address = InetAddress.getByName("127.0.0.1");
//			InetAddress address = InetAddress.getByName("192.168.1.3");
//			System.out.println(address);
			byte[] oscMess = mm.getOSCMess();
//			for (byte b: oscMess) System.out.println(b);
			DatagramPacket dp = new DatagramPacket(oscMess, oscMess.length, address, port);
			DatagramSocket dsocket = new DatagramSocket();
			dsocket.send(dp);
			dsocket.close();
		} catch (Exception e){
			
		}
	}
	
}
