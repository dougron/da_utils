package main.java.da_utils.udp.udp_receiver;

import java.util.Set;

public class ThreadStackTrace
{

	public static void main (String[] args)
	{
		Set<Thread> threadSet
        = Thread.getAllStackTraces().keySet();
		
		for (Thread x : threadSet) {
            System.out.println(x.getName());
        }
	}

}
