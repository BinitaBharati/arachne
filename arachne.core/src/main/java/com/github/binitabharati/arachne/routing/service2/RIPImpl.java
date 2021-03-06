package com.github.binitabharati.arachne.routing.service2;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.binitabharati.arachne.nwking.model.IpV4Address;
import com.github.binitabharati.arachne.routing.rip.model.AbstractRouteEntry;
import com.github.binitabharati.arachne.routing.service.worker.Worker;
import com.github.binitabharati.arachne.routing.service.worker.rip.CleanupWorker;
import com.github.binitabharati.arachne.routing.service.worker.rip.MulticastListener;
import com.github.binitabharati.arachne.routing.service.worker.rip.MulticastSender;
import com.github.binitabharati.arachne.routing.service.worker.rip.RouteProcessor;
import com.github.binitabharati.arachne.util.ArachU;

/**
 * 
 * @author binita.bharati@gmail.com
 * Implementation of the RIP protocol, underlying classes uses NIO2 APIs.
 *
 */

public class RIPImpl {

    
    public static final Logger logger = LoggerFactory.getLogger(RIPImpl.class);
    
    private Queue<AbstractRouteEntry> absRouteEntry;
    private Queue<ReceivedData> store;
    
    private boolean splitHorizon;
    private Properties arachneProp;
    private Properties jilapiProp;
    private String osName;
    
    private Map<String, String> routeProcessingMap;
    
    public RIPImpl(Properties arachneProp, Properties jilapiProp) {
        // TODO Auto-generated constructor stub
        //super(arachneProp, jilapiProp);
    	this.arachneProp = arachneProp;
    	this.jilapiProp = jilapiProp;
    	this.osName = ArachU.getOsName();
        store = new LinkedBlockingQueue<>(); 
        routeProcessingMap = new ConcurrentHashMap<>();
    }

    //@Override
    public void init() throws Exception {
        // TODO Auto-generated method stub
        logger.debug("init: entered");
        String tmp = arachneProp.getProperty(ArachU.RIP_SPLIT_HORIZON);
        if (tmp != null) {
            splitHorizon = Boolean.parseBoolean(tmp.trim());
            absRouteEntry = ArachU.initAbsoluteRoutes(arachneProp, jilapiProp, osName);
        }
        
        Map<String, MulticastSocket> dgSocketMap = new HashMap<String, MulticastSocket>();   
        
        Worker routeListener = new MulticastListener(arachneProp, osName,
                ArachU.RIP_MULTICAST_GROUP, store);
        Thread routeListenerThread = new Thread(routeListener);
        routeListenerThread.setUncaughtExceptionHandler(routeListener);
        routeListenerThread.start();
        

        Worker routeSender = new MulticastSender(arachneProp, jilapiProp, osName, 
                ArachU.RIP_MULTICAST_GROUP);
        Thread routePublisherThread = new Thread(routeSender);
        routePublisherThread.setUncaughtExceptionHandler(routeSender);
        routePublisherThread.start();
        
        List<IpV4Address> hostInterfaceIpList = new ArrayList<>();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface iface = networkInterfaces.nextElement();
            String displayName = iface.getDisplayName();
            logger.debug("init: iface = "+iface);
            logger.debug("init: displayName = "+displayName);

            try {
                List<InterfaceAddress> ifaceAddress = iface.getInterfaceAddresses();
                for (InterfaceAddress interfaceAdd : ifaceAddress) {
                    logger.debug("init: interfaceAdd = "+interfaceAdd);
                    InetAddress inetAddress = interfaceAdd.getAddress();
                    String ipAddress = inetAddress.getHostAddress();
                    logger.debug("init: ipAddress = "+ipAddress);
                    short netMaskLength = interfaceAdd.getNetworkPrefixLength();
                    logger.debug("init: netMaskLength = "+netMaskLength);
                    IpV4Address ipAddressObj = new IpV4Address(displayName, ipAddress, netMaskLength);
                    ipAddressObj.setNetworkAddress(ArachU.getNetworkAddress(ipAddress+"/"+netMaskLength));
                    hostInterfaceIpList.add(ipAddressObj);

                }

                
                
            } catch (Exception e) {
                logger.error("init: exception "+e.getMessage(), e);
            }
        }
        
        Worker routeProcessor = new RouteProcessor(arachneProp, jilapiProp, osName, 
                store, absRouteEntry, splitHorizon, hostInterfaceIpList, routeProcessingMap);
        
        Thread routeProcThread = new Thread(routeProcessor);        
        routeProcThread.setUncaughtExceptionHandler(routeProcessor);
        routeProcThread.start();
        
        /*Worker routeProcessor2 = new RouteProcessor(arachneProp, jilapiProp, osName,
                store, absRouteEntry, splitHorizon, hostInterfaceIpList);*/
        Thread routeProcThread2 = new Thread(routeProcessor);        
        routeProcThread2.setUncaughtExceptionHandler(routeProcessor);
        routeProcThread2.start();
        
        Worker routeCleaner = new CleanupWorker(arachneProp, jilapiProp, absRouteEntry, routeProcessingMap, osName);
        Thread routeCleanerThread = new Thread(routeCleaner);        
        routeCleanerThread.setUncaughtExceptionHandler(routeCleaner);
        routeCleanerThread.start();
        
        
        logger.debug("init: exiting");
        
    }

    



}
