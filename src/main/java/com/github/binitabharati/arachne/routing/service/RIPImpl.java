package com.github.binitabharati.arachne.routing.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.binitabharati.arachne.nwking.model.IpV4Address;
import com.github.binitabharati.arachne.routing.rip.model.AbstractRouteEntry;
import com.github.binitabharati.arachne.routing.service.worker.Worker;
import com.github.binitabharati.arachne.routing.service.worker.rip.MulticastListener;
import com.github.binitabharati.arachne.routing.service.worker.rip.MulticastSender;
import com.github.binitabharati.arachne.routing.service.worker.rip.RouteProcessor;
import com.github.binitabharati.arachne.service.model.RouteEntry;
import com.github.binitabharati.arachne.util.ArachU;
import com.github.binitabharati.jilapi.Jilapi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author binita.bharati@gmail.com
 * Implementation of the RIP protocol,
 *
 */


public class RIPImpl{
    
    public static final Logger logger = LoggerFactory.getLogger(RIPImpl.class);
    
    private Queue<AbstractRouteEntry> absRouteEntry;
    private Queue<DatagramPacket> store;
    
    private boolean splitHorizon;
    private Properties arachneProp;
    private Properties jilapiProp;
    private String osName;
    
    public RIPImpl(Properties arachneProp, Properties jilapiProp) {
        // TODO Auto-generated constructor stub
        //super(arachneProp, jilapiProp);
    	this.arachneProp = arachneProp;
    	this.jilapiProp = jilapiProp;
    	this.osName = ArachU.getOsName();
        store = new LinkedBlockingQueue<>();       
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
                store, absRouteEntry, splitHorizon, hostInterfaceIpList);
        
        Thread routeProcThread = new Thread(routeProcessor);        
        routeProcThread.setUncaughtExceptionHandler(routeProcessor);
        routeProcThread.start();
        
        /*Worker routeProcessor2 = new RouteProcessor(arachneProp, jilapiProp, osName,
                store, absRouteEntry, splitHorizon, hostInterfaceIpList);*/
        Thread routeProcThread2 = new Thread(routeProcessor);        
        routeProcThread2.setUncaughtExceptionHandler(routeProcessor);
        routeProcThread2.start();
        
        logger.debug("init: exiting");
        
    }

    

}
