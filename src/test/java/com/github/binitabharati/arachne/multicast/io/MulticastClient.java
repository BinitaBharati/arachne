package com.github.binitabharati.arachne.multicast.io;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MulticastClient implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(MulticastClient.class);

	
	private MulticastSocket ms;
	private int multicastPort = 4446;
	private String multicastGrp = "237.0.0.1";
	
	public MulticastClient(String myIp)
	{
		try {
			ms = new MulticastSocket(multicastPort);
			InetAddress rcvngInterface = InetAddress.getByName(myIp);
			ms.setInterface(rcvngInterface);
			ms.joinGroup(InetAddress.getByName(multicastGrp));
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void run() {
		// TODO Auto-generated method stub
		
		while(true) {

		    byte[] buf = new byte[256];
		    DatagramPacket packet = new DatagramPacket(buf, buf.length);
	        try {
				ms.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        String received = new String(packet.getData(), 0, packet.getLength());
	        logger.info("MulticastClient: Received ->  " + received);
		}
		
	}
	
	public static void main(String[] args) {
		new Thread(new MulticastClient(args[0])).start();
	}

}
