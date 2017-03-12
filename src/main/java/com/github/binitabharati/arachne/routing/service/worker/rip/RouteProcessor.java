package com.github.binitabharati.arachne.routing.service.worker.rip;

import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
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
    private Map<String, String> routeProcessingMap;//thread safe ConcurrentHashMap
    private List<IpV4Address> hostInterfaceIpList;
    
    public void addRouteEntry(AbstractRouteEntry routeEntry) {
        absRouteEntry.add(routeEntry);
    }
       
    public RouteProcessor(Properties arachneProp,Properties jilapiProp,  String os, Queue<DatagramPacket> store,
            Queue<AbstractRouteEntry> absRouteEntry, boolean isSplitHorizon,
            List<IpV4Address> hostInterfaceIpList, Map<String, String> routeProcessingMap) {
        this.workerType = Worker.WorkerType.routeProcessor;
        this.arachneProp = arachneProp;
        this.jilapiProp = jilapiProp;
        this.os = os;
        this.store = store;
        this.absRouteEntry = absRouteEntry;
        this.isSplitHorizon = isSplitHorizon;
        this.hostInterfaceIpList = hostInterfaceIpList;
        logger.debug("RouteProcessor = "+hostInterfaceIpList);
        this.routeProcessingMap = routeProcessingMap;
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
                        logger.debug(tName + "run: receivedRoutes = "+rcvdRoutes);
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
    
    
    //http://www.9tut.com/rip-routing-protocol-tutorial, https://supportforums.cisco.com/discussion/12043571/confusion-rip-timers
    //Cisco supports a additional hold-down timer, but that is not described in the RFC.
    public boolean processRoute(Queue<AbstractRouteEntry> absRouteEntry, 
            AbstractRouteEntry rcvdRoute, String packetSender) throws Exception {
        String curThreadId = Thread.currentThread().getId() + "";
        logger.info(curThreadId +" processRoute: entered with " + rcvdRoute + " and absRouteEntry = "+absRouteEntry
                +", packetSender = "+packetSender);
        String currentRoute = rcvdRoute.getDestinationNw() + rcvdRoute.getNetMask();
        boolean lock = false;
	
        while (!lock) {
            
            String prevVal = routeProcessingMap.putIfAbsent(currentRoute, curThreadId);
	    //logger.info(curThreadId +"processRoute: prevVal = "+prevVal);
            boolean matchingRouteFound = false;
            if (prevVal == null) {
            	logger.debug(curThreadId+ " processRoute: got lock - absRouteEntry "+absRouteEntry);
            	logger.debug(curThreadId+ " processRoute: got lock - currentRoute "+currentRoute);
                lock = true; //got the lock to process this route.
                //am the only thread processing this route.
                for (AbstractRouteEntry each : absRouteEntry) {
                    String curDestinationNw = each.getDestinationNw();
                    String curNetMask = each.getNetMask();
                                       
                    if (rcvdRoute.getDestinationNw().equals(each.getDestinationNw()) &&
                         rcvdRoute.getNetMask().equals(each.getNetMask())) {
                        matchingRouteFound = true;
                        logger.debug(curThreadId+ " processRoute: matching route found for "+rcvdRoute);                      
                        logger.debug(curThreadId+ " processRoute: "+rcvdRoute.getDestinationNw() +" - metric1 "+rcvdRoute.getMetric() + ", metric2 = "+each.getMetric());
                        //logger.info(curThreadId+ " processRoute: "+rcvdRoute);
                        if (!skipRouteUpdates(each, packetSender)) {
                        	
                        		if (Integer.parseInt(rcvdRoute.getMetric() + 1) < Integer.parseInt(each.getMetric())) { 
                                	 //install new route , remove old one.                            
                                    //both delete and insert should be done in a transaction.
                                    //The edit to DB and memory should happen in a single transaction ideally
                                    String addRouteInterface = ArachU.getRouteInterface(hostInterfaceIpList, packetSender.substring(1));
                                    //Increment the receieved route metric by 1.
                                    /*rcvdRoute.setMetric(Integer.parseInt(rcvdRoute.getMetric()) + 1 + "");
                                    rcvdRoute.setPort(addRouteInterface);
                                    rcvdRoute.setPublisherAddress(packetSender);*/
                                    each.setMetric(Integer.parseInt(rcvdRoute.getMetric()) + 1 + "");
                                    each.setPort(addRouteInterface);
                                    each.setPublisherAddress(packetSender);
                                    ArachU.editRoute(each, arachneProp, jilapiProp, os); 
                                    
                                    //reset RIP stale timer
                            		each.setRouteInstalledTimeInNanoSecs(System.nanoTime());
                            		each.setRouteHoldDownTimeInNanoSecs(null);

                                    
                                } else if (rcvdRoute.getPublisherAddress().equals(each.getPublisherAddress())) {
                                	//no update in the route entry, just that the RIP stale timer needs to be reset.
                                	//reset RIP stale timer
                                    logger.debug(curThreadId+" processRoute: updating stale timer for  "+each);

                                    //reset RIP stale timer
                            		each.setRouteInstalledTimeInNanoSecs(System.nanoTime());
                            		each.setRouteHoldDownTimeInNanoSecs(null);
                                    logger.debug(curThreadId+" processRoute: after editing existing route "+absRouteEntry);
                                }
                        		                       		
                        	} else {
                                logger.debug(curThreadId+ " processRoute: skipping processing of installed route = "+each + " with publisher address = "+packetSender);
                                logger.debug(curThreadId+ " processRoute: skipping processing of received route = "+rcvdRoute);


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
                    //Increment the receieved route metric by 1.
                    rcvdRoute.setMetric(Integer.parseInt(rcvdRoute.getMetric()) + 1 + "");
                    rcvdRoute.setPort(addRouteInterface);
                    rcvdRoute.setPublisherAddress(packetSender);

                    ArachU.addRoute(rcvdRoute, arachneProp, jilapiProp, os);
                    logger.debug("processRoute: after adding new route entry for "+rcvdRoute.getDestinationNw());
                    rcvdRoute.setRouteInstalledTimeInNanoSecs(System.nanoTime());
                    //rcvdRoute.setRouteMarkedForDeletionTimeInNanoSecs(null);
                    absRouteEntry.add(rcvdRoute);
                    logger.debug("processRoute: after adding new route "+absRouteEntry);
                }
                //remove entry from routeProcessingMap, so that other threads can get the lock.
                routeProcessingMap.remove(currentRoute);
            }  
            
        }
        
        return false;
    }
    
    private boolean skipRouteUpdates(AbstractRouteEntry installedRoute, String rcvdRoutePublisherAddress) {
    	logger.debug("skipRouteUpdates: entered with "+installedRoute);
    	if (installedRoute.getPublisherAddress() != null && rcvdRoutePublisherAddress != null) {
    		if (installedRoute.getRouteHoldDownTimeInNanoSecs() != null) {
    			if (installedRoute.getPublisherAddress().equals(rcvdRoutePublisherAddress)) {
    				//installedRoute.setRouteHoldDownTimeInNanoSecs(null);
    				return false;
    			}
    			//installed route publisher adddress is not same as received route publisher address.
            	Long currentTimeInNanoSecs = System.nanoTime();
    			Long timeElapsedSinceRouteHoldDown = currentTimeInNanoSecs - installedRoute.getRouteHoldDownTimeInNanoSecs();
    			long elapsedTime = TimeUnit.SECONDS.convert(timeElapsedSinceRouteHoldDown, TimeUnit.NANOSECONDS);
    			if (elapsedTime < ArachU.RIP_ROUTE_HOLD_INTERVAL_SECS) {
    				return true;
    			}
    			return false;
            }
    		return false;
            
    	}
    	
    	return true;
    }


}
