package main.java.da_utils.udp.udp_receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class UDPReceiver2 implements Runnable {
	
	
	private UDPReceiverParent parent;
	private int port;
	private int delay;
//	private Thread animator;
	byte[] buffer;
	DatagramSocket dsocket;
	DatagramPacket packet;
	private volatile boolean exit = false;
	
	
	
	public UDPReceiver2(UDPReceiverParent parent, int port, int delay, int maxMessageSize)
	{
		this.parent = parent;
		this.port = port;
		this.delay = delay;
		buffer = new byte[maxMessageSize]; //examle used 2048
		packet = new DatagramPacket(buffer, buffer.length);

		try {
			dsocket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void stop()
	{
		exit = true;
	}

	
	
	@Override
	public void run() 
	{
		long beforeTime, timeDiff, sleep;
		beforeTime = System.currentTimeMillis();


		while (true) 
		{		
		    cycle();
//		    repaint();
		    timeDiff = System.currentTimeMillis() - beforeTime;
		    sleep = delay  - timeDiff;
		
		    if (sleep < 0) 
		    {
		        sleep = 2;
		    }
		
		    try 
		    {
		        Thread.sleep(sleep);
		    } catch (InterruptedException e) 
		    {
		        System.out.println("Interrupted: " + e.getMessage());
		    }
		
		    beforeTime = System.currentTimeMillis();
		}
		
		
	}
	
	private void cycle() {
		 // Wait to receive a datagram
        try {
			dsocket.receive(packet);

			// Convert the contents to a string, and display them
			String msg = new String(buffer, 0, packet.getLength());
			parent.udpMessageIn(msg);
			
//			System.out.println("-----------------------------");
//			int x = 0;
//			String str = "";
//			for (byte b: buffer){
//				str += b + "\t";
//				x++;
//				if (x == 4){
//					x = 0;
//					System.out.println(str);
//					str = "";
//				}				
//			}
			
			ArrayList<Object> oList = UDPByteArrayDecoder.decodeUDPMessage(buffer);
			parent.udpMessageList(oList);
			
//			for (Object o: oList){
//				System.out.println(o);
////				parent.consolePrint("" + o);
//			}
//        System.out.println(packet.getAddress().getHostName() + ": "
//            + msg);

			// Reset the length of the packet before reusing it.
			packet.setLength(buffer.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

		// overridden stuff -----------------------------------------------------------
//		@Override
//		public void addNotify() {
//			super.addNotify();
//			animator = new Thread(this);
//			animator.start();
//		}
	
// main ==============================================================
	
	public static void main(String[] args){
		
	}

}
