package main.java.da_utils.udp.udp_receiver;

import java.util.ArrayList;

public interface UDPReceiverParent {

	void udpMessageIn(String msg);
	void udpMessageList(ArrayList<Object> oList);

	void consolePrint(String o);

}
