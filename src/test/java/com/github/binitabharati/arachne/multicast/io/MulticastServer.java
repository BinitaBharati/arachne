package com.github.binitabharati.arachne.multicast.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.binitabharati.arachne.util.ArachU;

public class MulticastServer implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(MulticastServer.class);
	
	
	private MulticastSocket ms;
	private int multicastPort = 4445;
	private boolean moreQuotes = true;
	private BufferedReader in = null;
	private static String MULTICAST_GROUP = "237.11.0.1";
	
	public MulticastServer(String myIp)
	{
		try {
			ms = new MulticastSocket(multicastPort);
			InetAddress sendingInterface = InetAddress.getByName(myIp);
			ms.setInterface(sendingInterface);
			ms.joinGroup(InetAddress.getByName(MULTICAST_GROUP));
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
            in = new BufferedReader(new FileReader("one-liners.txt"));
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
            logger.error("Could not open quote file. Serving time instead.");
        }
	}

	public void run() {
		// TODO Auto-generated method stub
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	
    	logger.info("run: entered, going to sleep for few mins. Current time = "+dateFormat.format(new Date()));
    	
    	logger.info("run: entered, woke up from slumbers. Current time = "+dateFormat.format(new Date()));
    	
    	try
    	{
    		while (true) {
                byte[] buf = new byte[256];
 
                // construct quote
                String dString = null;
                if (in == null)
                    dString = new Date().toString();
                else
                    dString = getNextQuote();
                
                Random random = new Random();
                
                if(dString == null)
                	dString = "This is a random quote :) "+random.nextInt(1000);
                
                buf = dString.getBytes();
                
                InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
                ms.send(packet);
 
                try {
        			Thread.sleep(1*10*1000);
        		} catch (InterruptedException e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        			moreQuotes = false;
        		}
            }
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			moreQuotes = false;
		}
    	finally
    	{
    		ms.close();
    	}
        		
	}
	
	protected String getNextQuote() {
    	logger.info("getNextQuote: entered");
        String returnValue = null;

        try {
        	returnValue = in.readLine();
        	logger.info("getNextQuote: retValue = "+returnValue);
            if (returnValue == null) {
                in.close();
                moreQuotes = false;
                returnValue = "No more quotes. Goodbye.";
            }
        } catch (IOException e) {
            returnValue = "IOException occurred in server.";
        }
        return returnValue;
    }
	
	public static void main(String[] args) {
		MulticastServer tmp = new MulticastServer(args[0]);
		new Thread(tmp).start();
	}
	

}
