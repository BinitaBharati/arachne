package com.github.binitabharati.arachne.routing.service.nio.worker.rip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.github.binitabharati.arachne.routing.service.worker.Worker;
import com.github.binitabharati.arachne.service.model.RouteEntry;
import com.github.binitabharati.arachne.util.ArachU;

/**
 * 
 * @author binita.bharati@gmail.com
 * The RIP multicast route sender.Based on NIO APIs.
 *
 */

public class MulticastSender extends Worker {
    
    public static final Logger logger = LoggerFactory.getLogger(MulticastSender.class);
    
    private String name = "MulticastSender";
    private static int port;
    private static long MULTICAST_SENDER_IDLE_TIME_SECS = 1;
    private InetAddress group;
    private String osName;
    private Properties arachneProp;
    private Properties jilapiProp;       
    private MulticastSocket dgSocket;

        
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MulticastSender(Properties arachneProp, Properties jilapiProp, String osName, 
           String multicastGrpStr) {
        this.workerType = Worker.WorkerType.routeSender;
        this.osName = osName;
        this.arachneProp = arachneProp;
        this.jilapiProp = jilapiProp;
        
        
            try {   
                    this.group = InetAddress.getByName(multicastGrpStr); 
                    dgSocket = new MulticastSocket();
            }
            catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                logger.info("constructor: exception "+e.getMessage());
                logger.error("constructor: exception "+e.getMessage(), e);
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.info("constructor: exception "+e.getMessage());
                logger.error("constructor: exception "+e.getMessage(), e);
                e.printStackTrace();
            }  
        
    }
    
   public void run() {
       // TODO Auto-generated method stub
       byte[] bt = new byte[100];
       byte index = 0;
       while(true) {
           try {
            List<RouteEntry> myRoutes = ArachU.getRoutes(arachneProp, jilapiProp, osName);
            logger.debug("run: myRoutes = "+myRoutes);
            //serialize with SBE
            byte[] buffer = ArachU.encodeRoute(myRoutes);

            /* The entire route table goes in a single DatagramPacket ? 
             * Yes, for now
             * 
             */
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
                     if (filteredIntfPrefixList != null && filteredIntfPrefixList.size() > 0) {
                    	  for (String eachFilteredIntfPrefix : filteredIntfPrefixList) {
                          	if (ArachU.intfToListeningPortMap.keySet().contains(eachFilteredIntfPrefix)) {
                            	Integer port = ArachU.intfToListeningPortMap.get(eachFilteredIntfPrefix);                         	
                                logger.debug("run: sending MC traffic over interface = "+eachFilteredIntfPrefix + " and port = "+port);
                                dgSocket.setNetworkInterface(iface);
                            	dgSocket.send(new DatagramPacket(buffer, buffer.length, group, port));
                            	
                          	}
                    	  }
                     }
                 }
            }
            Thread.sleep(ArachU.RIP_ROUTE_PUBLISH_INTERVAL_SECS*1000);
            
            
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            logger.error("run: exception "+e1.getMessage(), e1);
            e1.printStackTrace();
        }
          
       }
   }
   

}
