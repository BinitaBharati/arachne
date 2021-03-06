package com.github.binitabharati.arachne.routing.service.nio.worker.rip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.binitabharati.arachne.routing.service.RIPImpl;
import com.github.binitabharati.arachne.routing.service.worker.Worker;
import com.github.binitabharati.arachne.util.ArachU;

/**
 * 
 * @author binita.bharati@gmail.com
 * The Listener for RIP multicasts.Based on NIO APIs.
 *
 */

public class MulticastListener extends Worker {
    
    public static final Logger logger = LoggerFactory.getLogger(MulticastListener.class);
    
    private String type = "MulticastListener";
    private int port;
    private InetAddress multicastGrp;
    private String osName;
    private Properties prop;
    private Queue<DatagramPacket> store;
      
    private ExecutorService excService;
    
    
   
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MulticastListener(Properties prop, String osName, 
            String multicastGrpStr, Queue<DatagramPacket> store) throws Exception {
        this.workerType = Worker.WorkerType.routeListener;
        this.osName = osName;
        this.prop = prop;
        //this.port = port;        
        this.store = store;       
        this.multicastGrp = InetAddress.getByName(multicastGrpStr);     
        //multicastSocket = new MulticastSocket(port);
        //stop receiving packets from loopback, does not work!
        //multicastSocket.setLoopbackMode(true);
        this.excService = Executors.newFixedThreadPool(3);
        
        
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface iface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = iface.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            	String ipAddress = inetAddress.getHostAddress();
                List<String> filteredIntfPrefixList = ArachU.intfToListeningPortMap.keySet().stream().filter(eachIntfPrefix -> {
                	if (ipAddress.startsWith(eachIntfPrefix)) {
                		return true;
                	} else {
                		return false;
                	}
                }).collect(Collectors.toList());
                logger.debug("MulticastListener: filteredIntfPrefixList = "+filteredIntfPrefixList);
                for (String eachFilteredIntfPrefix : filteredIntfPrefixList) {
                	if (ArachU.intfToListeningPortMap.keySet().contains(eachFilteredIntfPrefix)) {
                    			logger.debug("MulticastListener: starting ListenerWorker for port prefix = "+eachFilteredIntfPrefix);
                    		    ListenerWorkers listenerWorker = new ListenerWorkers(ArachU.intfToListeningPortMap.get(eachFilteredIntfPrefix));
                    		    excService.submit(listenerWorker);
                                                                                               
                    }
				}
                
                
            }
            
            
        }
        
              
    }
    
    public void run() {
        // TODO Auto-generated method stub
        
    }
    
    class ListenerWorkers implements Runnable {
    	private Integer listenerPort;
    	private MulticastSocket multicastSocket;
    	
    	public ListenerWorkers(Integer listenerPort) {
    		logger.debug("ListenerWorkers: constructor entered for "+listenerPort);
    		this.listenerPort = listenerPort;
    		try {
				this.multicastSocket = new MulticastSocket(listenerPort);
				//multicastSocket.setNetworkInterface(iface);
				this.multicastSocket.joinGroup(multicastGrp);

    		}
            catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
		@Override
		public void run() {
			long tId = Thread.currentThread().getId();
    		logger.debug(tId + " ListenerWorkers -> run: entered for port "+listenerPort);
			// TODO Auto-generated method stub
			while (true) {
				DatagramPacket packet = new DatagramPacket(new byte[4096], 4096);
				try {
					multicastSocket.receive(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(tId + " ListenerWorkers -> run: caught IOexception while awaiting to receive");
					e.printStackTrace();
				}
                logger.debug(tId + " ListenerWorkers -> run: Got packet123 " + 
                        Arrays.toString(packet.getData()) + " from port = "+listenerPort);              
                boolean added = store.offer(packet);//offer can not block, as I have not set any capacity to the underlying LinkedList
                packet = new DatagramPacket(new byte[4096], 4096);
			}
			
			
		}
    	
    }

   

}
