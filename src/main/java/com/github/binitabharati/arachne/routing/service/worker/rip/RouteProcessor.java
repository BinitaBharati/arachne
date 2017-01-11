package com.github.binitabharati.arachne.routing.service.worker.rip;

import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.github.binitabharati.arachne.nwking.model.IpV4Address;
import com.github.binitabharati.arachne.routing.rip.model.AbstractRouteEntry;
import com.github.binitabharati.arachne.routing.service.worker.Worker;
import com.github.binitabharati.arachne.service.model.RouteEntry;
import com.github.binitabharati.arachne.util.ArachU;

/**
 * 
 * @author binita.bharati@gmail.com
 * The RIP RouteProcessor. 
 * 
 */

public class RouteProcessor extends Worker{
    
    public static final Logger logger = LoggerFactory.getLogger(RouteProcessor.class);

    
    private Properties arachneProp;
    private Properties jilapiProp;
    private String os;
    private Queue<DatagramPacket> store;
    private Queue<AbstractRouteEntry> absRouteEntry;
    boolean isSplitHorizon;
    private ConcurrentHashMap<String, String> routeProcessingMap;
    private List<IpV4Address> hostInterfaceIpList;
    
    public void addRouteEntry(AbstractRouteEntry routeEntry) {
        absRouteEntry.add(routeEntry);
    }
       
    public RouteProcessor(Properties arachneProp,Properties jilapiProp,  String os, Queue<DatagramPacket> store,
            Queue<AbstractRouteEntry> absRouteEntry, boolean isSplitHorizon,
            List<IpV4Address> hostInterfaceIpList) {
        this.workerType = Worker.WorkerType.routeProcessor;
        this.arachneProp = arachneProp;
        this.jilapiProp = jilapiProp;
        this.os = os;
        this.store = store;
        this.absRouteEntry = absRouteEntry;
        this.isSplitHorizon = isSplitHorizon;
        this.hostInterfaceIpList = hostInterfaceIpList;
        logger.debug("RouteProcessor = "+hostInterfaceIpList);

        this.routeProcessingMap = new ConcurrentHashMap<>();
    }
        
    

    public void run() {
        // TODO Auto-generated method stub
        try {
            String tName = Thread.currentThread().getName();
            byte[] oldData = null;
            String packetSender = null;
            while (true) {
                byte[] received = null;               
                DatagramPacket dp = store.poll();//non blocking call.
                if (dp != null) {
                    packetSender = dp.getAddress().toString();
                    boolean senderIsSameHost = false;
                    for (IpV4Address eachHostIp : hostInterfaceIpList) {
                        if (eachHostIp.getIpAddress().equals(packetSender.substring(1))) {
                            senderIsSameHost = true;
                        }
                    }
                    logger.debug("run: senderIsSameHost = "+senderIsSameHost);
                    if (!senderIsSameHost) {
                        received = dp.getData();
                        List<AbstractRouteEntry> rcvdRoutes = ArachU.decodeRoute(received, packetSender);                                     
                        //String receivedStr = new String(received);
                        logger.debug(tName + "run: rcdvRoutes = "+rcvdRoutes);
                        for (AbstractRouteEntry rcvdRouteEntry : rcvdRoutes) {
                            processRoute(absRouteEntry, rcvdRouteEntry, packetSender);
                        }
                    }
                    
                                                         
                 }                 
            }
        
        } catch(Exception ex){
            ex.printStackTrace();
        }
        
            
    }
    
    private List<AbstractRouteEntry> cloneRoute(List<AbstractRouteEntry> rcvdRoutes) {
        //clone the original rcvdRoutes
        List<AbstractRouteEntry> rcvdRoutesCopy = rcvdRoutes.stream().map(each -> {
            return new AbstractRouteEntry(each.getDestinationNw(), each.getGateway(), 
                    each.getNetMask(), each.getMetric(), each.getPort(), each.getPublisherAddress());
        }).collect(Collectors.toList());
        logger.debug("cloneRoute: rcvdRoutesCopy = "+rcvdRoutesCopy);        
        return rcvdRoutesCopy;
    }
    

    public boolean processRoute(Queue<AbstractRouteEntry> absRouteEntry, 
            AbstractRouteEntry rcvdRoute, String packetSender) throws Exception {
        logger.info("processRoute: entered with " + rcvdRoute + " and absRouteEntry = "+absRouteEntry
                +", packetSender = "+packetSender);
        String currentRoute = rcvdRoute.getDestinationNw() + rcvdRoute.getNetMask();
        boolean lock = false;
        while (!lock) {
            
            String prevVal = routeProcessingMap.putIfAbsent(currentRoute, currentRoute);
            boolean matchingRouteFound = false;
            if (prevVal == null) {
                lock = true; //got the lock to process this route.
                //am the only thread processing this route.
                for (AbstractRouteEntry each : absRouteEntry) {
                    String curDestinationNw = each.getDestinationNw();
                    String curNetMask = each.getNetMask();
                                       
                    if (rcvdRoute.getDestinationNw().equals(each.getDestinationNw()) &&
                         rcvdRoute.getNetMask().equals(each.getNetMask())) {
                        matchingRouteFound = true;
                        logger.info("processRoute: matching route found!");
                        if (Integer.parseInt(rcvdRoute.getMetric()) < Integer.parseInt(each.getMetric())) {    
                           //install new route , remove old one.                            
                            //both delete and insert should be done in a transaction.
                            //The edit to DB and memory should happen in a single transaction ideally
                            String addRouteInterface = ArachU.getRouteInterface(hostInterfaceIpList, packetSender.substring(1));
                            ArachU.editRoute(rcvdRoute, arachneProp, jilapiProp, os, addRouteInterface);                            
                            absRouteEntry.remove(each);//This will change the index of the route within list.Does that matter ? No!
                            absRouteEntry.add(rcvdRoute);
                            //return true;
                        }
                    }
                    if (matchingRouteFound) { //you are done.
                        break;
                    }
                    
                }
                if (!matchingRouteFound) {
                    //The insertion to DB and memory should happen in a single transaction ideally.
                    //First insert route into system.This command does not produce any input stream, but on error, may produce a error stream
                    //If error stream is produced the execNativeCommand will throw Exception.
                    String addRouteInterface = ArachU.getRouteInterface(hostInterfaceIpList, packetSender.substring(1));
                    ArachU.addRoute(rcvdRoute, arachneProp, jilapiProp, os, addRouteInterface);
                    absRouteEntry.add(rcvdRoute);
                }
                //remove entry from routeProcessingMap, so that other threads can get the lock.
                routeProcessingMap.remove(currentRoute);
            }  
            
        }
        
        return false;
    }


}
