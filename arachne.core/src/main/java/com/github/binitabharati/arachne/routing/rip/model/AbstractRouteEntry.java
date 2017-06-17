package com.github.binitabharati.arachne.routing.rip.model;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.binitabharati.arachne.service.model.RouteEntry;

/**
 * 
 * @author binita.bharati@gmail.com
 * Abstraction of a route entry for the RIP protocol.
 * 
 *
 */

public class AbstractRouteEntry extends RouteEntry {
	
	public static final Logger logger = LoggerFactory.getLogger(AbstractRouteEntry.class);//prints to arachne.log

    
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
	
	/**
	 * This equals method is used only during Junit testing.
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		AbstractRouteEntry are = (AbstractRouteEntry)obj;
		logger.debug("equals: entered with "+are);
		if (this.getDestinationNw() != null && are.getDestinationNw() != null 
				&& this.getNetMask() != null && are.getNetMask() != null
				&& this.getMetric() != null && are.getMetric() != null
				&& this.getGateway() != null && are.getGateway() != null
				&& this.getPort() != null && are.getPort() != null) {
				logger.debug("network, mask ,metric, gateway and port is not null");
				if (this.getDestinationNw().equals(are.getDestinationNw()) && this.getNetMask().equals(are.getNetMask())
						&& this.getMetric().equals(are.getMetric()) && this.getGateway().equals(are.getGateway()) 
						&& this.getPort().equals(are.getPort())) {
					
					return true;
				}
			
			
			
		} else if (this.getDestinationNw() != null && are.getDestinationNw() != null && this.getNetMask() != null && are.getNetMask() != null
				&& this.getMetric() != null && are.getMetric() != null
				&& this.getPort() != null && are.getPort() != null) {
			logger.debug("network, mask , metric and port is not null");
			if (this.getDestinationNw().equals(are.getDestinationNw()) && this.getNetMask().equals(are.getNetMask())
					&& this.getMetric().equals(are.getMetric()) 
					&& this.getPort().equals(are.getPort()))  {
				return true;

			}
		}
		else if (this.getDestinationNw() != null && are.getDestinationNw() != null && this.getNetMask() != null && are.getNetMask() != null
				&& this.getMetric() != null && are.getMetric() != null
				&& this.getGateway() != null && are.getGateway() != null) {
			logger.debug("network, mask , gateway and metric is not null");
			if (this.getDestinationNw().equals(are.getDestinationNw()) && this.getNetMask().equals(are.getNetMask())
					&& this.getMetric().equals(are.getMetric()) 
					&& this.getGateway().equals(are.getGateway()))  {
				return true;

			}
		}
		else if (this.getDestinationNw() != null && are.getDestinationNw() != null && this.getNetMask() != null && are.getNetMask() != null) {
			logger.debug("network, mask is not null");
			if (this.getDestinationNw().equals(are.getDestinationNw()) && this.getNetMask().equals(are.getNetMask()))  {
				return true;

			}
		}
		
		return false;
	}
		
	/*@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new AbstractRouteEntry(this.getDestinationNw(), this.getGateway(), this.getNetMask(), 
				this.getMetric(), this.getPort(), this.getPublisherAddress());
	}*/
  
}
