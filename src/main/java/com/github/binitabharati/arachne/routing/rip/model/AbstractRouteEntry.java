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
    
    
  /*  public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }*/
    
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        String hash = destinationNw + netMask;
        return  hash.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        AbstractRouteEntry input = (AbstractRouteEntry)obj;
        if (this.destinationNw.equals(input.getDestinationNw()) && this.netMask.equals(input.getNetMask())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        //DO NOT change the format of this!! This is used to write into RIP route entry file.
        return "{" + destinationNw + ";" + gateway + ";" + netMask + ";" + metric 
                + ";" + port + ";" + publisherAddress  + "}";
    }
  
}
