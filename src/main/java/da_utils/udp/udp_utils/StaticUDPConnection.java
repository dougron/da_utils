package main.java.da_utils.udp.udp_utils;

public class StaticUDPConnection{
	
	
	private static UDPConnection conn;
	

	public StaticUDPConnection(int port)
	{
		if (conn == null){
			conn = new UDPConnection(port);
		}
	}
	
	
	public void sendUDPMessage(main.java.da_utils.udp.udp_utils.OSCMessMaker mess)
	{
		conn.sendUDPMessage(mess);
	}
}
