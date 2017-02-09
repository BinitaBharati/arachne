package com.github.binitabharati.arachne.routing.service.worker.rip;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.binitabharati.arachne.routing.rip.model.AbstractRouteEntry;
import com.github.binitabharati.arachne.routing.service.worker.Worker;
import com.github.binitabharati.arachne.service.model.RouteEntry;
import com.github.binitabharati.arachne.util.ArachU;

/**
 * 
 * @author binita.bharati@gmail.com
 * The route clean up worker. This will clear stale routes in a 2 stage process:
 * - Mark stale routes - ie routes for which no update has arrived for x secs.
 * - Mark in-hold-down routes - by setting metric of the route to 16,
 *   The updated route with metric 16 (unreachable in RIP termonology) is broadcasted
 *   in RIP responses.
 * - A hold-down-timer of y secs is started.
 * - During hold down, if any updates arrive for that route from publisher address other
 *   than the original publisher, then that update should be ignored. But, if a better
 *   metric is published by the original publisher address, withn hold down, then that
 *   route is updated and the stale time is reset.
 * - After hold down expires, the flush timer is started. This waits for z secs
 *   to receive any route updates. If z secs expire, then that route is deleted from the
 *   route table completely.
 *   
 *   There will be only 1 instance of this worker thread.
 *
 */

public class CleanupWorker extends Worker{
	
    public static final Logger logger = LoggerFactory.getLogger(CleanupWorker.class);

	
	private Properties arachneProp;
    private Properties jilapiProp;
    private String os;
    private Queue<AbstractRouteEntry> absRouteEntry;//Thread safe - ConcurrentLinkedQueue
    private Map<String, String> routeProcessingMap;//thread safe ConcurrentHashMap
    
    public CleanupWorker(Properties arachneProp, Properties jilapiProp, 
    		Queue<AbstractRouteEntry> absRouteEntry, Map<String, String> routeProcessingMap, String os) {
    	this.absRouteEntry = absRouteEntry;
    	this.routeProcessingMap = routeProcessingMap;
    	this.os = os;
    	this.arachneProp = arachneProp;
    	this.jilapiProp = jilapiProp;
    }

	public void run() {
		// TODO Auto-generated method stub
        String curThreadId = Thread.currentThread().getId() + "";
		while (true) {
			logger.debug(curThreadId+ " run: absRouteEntry = "+absRouteEntry);
			for (AbstractRouteEntry entry : absRouteEntry) {
				if (entry != null) {
					if (entry.getRouteInstalledTimeInNanoSecs() != null ) {
						if ( entry.getRouteHoldDownTimeInNanoSecs() == null ) {
							//route is just installed.
							Long currentTimeInNanoSecs = System.nanoTime();
							Long timeElapsedSinceRouteInstall = currentTimeInNanoSecs - entry.getRouteInstalledTimeInNanoSecs();
							long elapsedTime = TimeUnit.SECONDS.convert(timeElapsedSinceRouteInstall, TimeUnit.NANOSECONDS);
							if (elapsedTime >= ArachU.RIP_ROUTE_STALE_INTERVAL_SECS) {
								logger.debug(curThreadId+ " run: handling route = "+entry + ", with stale elapsedTime = "+elapsedTime);
								String currentRoute = entry.getDestinationNw() + entry.getNetMask();								
								boolean lock = false;
								while (!lock) {								
									String prevVal = routeProcessingMap.putIfAbsent(currentRoute, curThreadId);
									if (prevVal == null) { //This thread holds exclusive rights wrt this route now
                                        logger.info(curThreadId+ " run: got lock - entry "+entry);
										//set metric of this route to 16.
										lock = true;
										entry.setMetric(16+"");//unreachable
				                        try {
											logger.debug(curThreadId + "run: setting route = "+entry + "to unreachable");
											ArachU.editRoute(entry, arachneProp, jilapiProp, os);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}  
				                        //start hold down timer
				                        entry.setRouteHoldDownTimeInNanoSecs(System.nanoTime());
				                        //release lock
										routeProcessingMap.remove(currentRoute);
									}
								}
								
								
							}
						} else { //route is already in hold down time.
							if (entry.getRouteMarkedForDeletionTimeInNanoSecs() == null) {
								Long currentTimeInNanoSecs = System.nanoTime();
								Long timeElapsedSinceRouteHoldDown = currentTimeInNanoSecs - entry.getRouteHoldDownTimeInNanoSecs();
								long elapsedTime = TimeUnit.SECONDS.convert(timeElapsedSinceRouteHoldDown, TimeUnit.NANOSECONDS);
								if (elapsedTime >= ArachU.RIP_ROUTE_HOLD_INTERVAL_SECS) {
									logger.debug(curThreadId + "run: handling marked for hold down route = "+entry + ", elapsedTime = "+elapsedTime);
									String currentRoute = entry.getDestinationNw() + entry.getNetMask();									
									boolean lock = false;
									while (!lock) {		
										String prevVal = routeProcessingMap.putIfAbsent(currentRoute, currentRoute);
										if (prevVal == null) { //This thread holds exclusive rights wrt this route now
											lock = true;
											//Set the route flush timer
											entry.setRouteMarkedForDeletionTimeInNanoSecs(System.nanoTime());
											logger.debug(curThreadId + "run: after marking holdown , route table= "+absRouteEntry );
											routeProcessingMap.remove(currentRoute);
										} 
									}
									
									
								}
							} else { //route is marked for deletion already.
								Long currentTimeInNanoSecs = System.nanoTime();
								Long timeElapsedSinceRouteMarkedForDelete = currentTimeInNanoSecs - entry.getRouteMarkedForDeletionTimeInNanoSecs();
								long elapsedTime = TimeUnit.SECONDS.convert(timeElapsedSinceRouteMarkedForDelete, TimeUnit.NANOSECONDS);
								if (elapsedTime >= ArachU.RIP_ROUTE_FLUSH_INTERVAL_SECS) {	
									logger.debug(curThreadId + "run: handling marked for delete route = "+entry + ", elapsedTime = "+elapsedTime);
									String currentRoute = entry.getDestinationNw() + entry.getNetMask();									
									boolean lock = false;
									while (!lock) {								
										String prevVal = routeProcessingMap.putIfAbsent(currentRoute, currentRoute);
										if (prevVal == null) { //This thread holds exclusive rights wrt this route now
											//delete this route totally.
											lock = true;
											
											try {
												logger.debug(curThreadId + " run: deleting route = "+entry + "as no updates have arrived!");
												ArachU.deleteRoute(entry, arachneProp, jilapiProp, os);
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											absRouteEntry.remove(entry);
											logger.debug(curThreadId + "run: after deleting route, absRouteEntry = "+absRouteEntry);
											routeProcessingMap.remove(currentRoute);
											//test start
											List<RouteEntry> myRoutes;
											try {
												myRoutes = ArachU.getRoutes(arachneProp, jilapiProp, os);
												logger.info("run: myRoutes after deleting = "+myRoutes);
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
								            
											//test end
										} 
									}
								}
								
							}
							
							
						}
						
					}
				}
			}
			try {
		        Thread.sleep(30*1000);
			} catch (Exception ex){
				ex.printStackTrace();
			}

	
			
		}
		
		
	}

}
