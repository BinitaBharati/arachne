package com.github.binitabharati.arachne.routing.rip.model;

import java.util.HashMap;
import java.util.Map;

import com.github.binitabharati.arachne.service.model.RouteEntry;

/**
 * 
 * @author binita.bharati@gmail.com
 * Abstraction of a route entry for the RIP protocol.
 * 
 *
 */

public class AbstractRouteEntry extends RouteEntry{
    
    //Property used for RIP split horizon feature.
    private String publisherAddress;
    
    private Long routeInstalledTimeInNanoSecs;//The time at which this route got installed/updated.Used wrt to the RIP stale route timer.
    private Long routeHoldDownTimeInNanoSecs;//The time at which the route was marked for hold down.Used wrt RIP hold down timer.
    private Long routeMarkedForDeletionTimeInNanoSecs;//The time at which the route was marked for deletion.Used wrt RIP flush timer.
    
    //Property used to mark a deleted route. randomAccessFile doesnt allow deletions.
    //private boolean deleted;
    
    public AbstractRouteEntry(String destinationNw, String gateway, String netmask, 
            String metric, String port, String publisherAddress) {
        this.destinationNw = destinationNw;
        this.gateway = gateway;
        this.netMask = netmask;
        this.metric = metric;
        this.port = port;
        this.publisherAddress = publisherAddress;
        //this.deleted = deleted;
    }

    public String getPublisherAddress() {
        return publisherAddress;
    }

    public void setPublisherAddress(String publisherAddress) {
        this.publisherAddress = publisherAddress;
    }
    
    public Long getRouteInstalledTimeInNanoSecs() {
		return routeInstalledTimeInNanoSecs;
	}

	public void setRouteInstalledTimeInNanoSecs(Long routeInstalledTimeInNanoSecs) {
		this.routeInstalledTimeInNanoSecs = routeInstalledTimeInNanoSecs;
	}
	
	
	public Long getRouteMarkedForDeletionTimeInNanoSecs() {
		return routeMarkedForDeletionTimeInNanoSecs;
	}

	public void setRouteMarkedForDeletionTimeInNanoSecs(Long routeMarkedForDeletionTimeInNanoSecs) {
		this.routeMarkedForDeletionTimeInNanoSecs = routeMarkedForDeletionTimeInNanoSecs;
	}
	
    public Long getRouteHoldDownTimeInNanoSecs() {
		return routeHoldDownTimeInNanoSecs;
	}

	public void setRouteHoldDownTimeInNanoSecs(Long routeHoldDownTimeInNanoSecs) {
		this.routeHoldDownTimeInNanoSecs = routeHoldDownTimeInNanoSecs;
	}

	@Override
    public String toString() {
        // TODO Auto-generated method stub
        return "{" + destinationNw + ";" + gateway + ";" + netMask + ";" + metric 
                + ";" + port + ";" + publisherAddress  + ";" + routeInstalledTimeInNanoSecs+";"
        + routeHoldDownTimeInNanoSecs +";" + routeMarkedForDeletionTimeInNanoSecs + "}";
    }
  
}
