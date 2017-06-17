package com.github.binitabharati.arachne.routing.service.worker.rip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.binitabharati.arachne.routing.service.worker.Worker;
import com.github.binitabharati.arachne.routing.service2.ReceivedData;
import com.github.binitabharati.arachne.util.ArachU;

/**
 * 
 * @author binita.bharati@gmail.com
 * The Listener for RIP multicasts - Based on NIO2 APIs
 *
 */
public class MulticastListener extends Worker {
    
    public static final Logger logger = LoggerFactory.getLogger(MulticastListener.class);
    
    private String type = "MulticastListener2";
    private int port;
    private String osName;
    private Properties prop;
    private Queue<ReceivedData> store;
    private InetAddress multicastGrp;
      
    private ExecutorService excService;
    
    
   
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MulticastListener(Properties prop, String osName, 
            String multicastGrpStr, Queue<ReceivedData> store) throws Exception {
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
                    		    ListenerWorkers listenerWorker = new ListenerWorkers(ArachU.intfToListeningPortMap.get(eachFilteredIntfPrefix), iface);
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
    	private DatagramChannel multicastSocket;
    	
    	public ListenerWorkers(Integer listenerPort, NetworkInterface ni) {
    		logger.debug("ListenerWorkers: constructor entered for "+listenerPort);
    		this.listenerPort = listenerPort;
    		try {
				this.multicastSocket = DatagramChannel.open(StandardProtocolFamily.INET); 
				multicastSocket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				//multicastSocket.setNetworkInterface(iface);
				this.multicastSocket.bind(new InetSocketAddress(listenerPort));
				this.multicastSocket.join(multicastGrp, ni);
                

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
    		SocketAddress senderAddress = null;
			while (true) {
				ByteBuffer buffer = ByteBuffer.allocate(1048);
				try {
					 senderAddress = multicastSocket.receive(buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(tId + " ListenerWorkers -> run: caught IOexception while awaiting to receive");
					e.printStackTrace();
				}
				logger.debug(tId + " ListenerWorkers -> run: senderAddress "+senderAddress);
                if (senderAddress != null) {
                	buffer.flip();
                    int limits = buffer.limit();
                    byte bytes[] = new byte[limits];
                    buffer.get(bytes, 0, limits);                 
                    ReceivedData rd = new ReceivedData(senderAddress.toString(), bytes);
                    logger.debug(tId + " ListenerWorkers -> run: Got packet123 " + 
                            Arrays.toString(bytes) + " from port = "+listenerPort); 
                    boolean added = store.offer(rd);//offer can not block, as I have not set any capacity to the underlying LinkedList
                }
                
			}
			
			
		}
    	
    }

   

}
