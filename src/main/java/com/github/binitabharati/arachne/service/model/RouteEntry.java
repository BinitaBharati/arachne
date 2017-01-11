package com.github.binitabharati.arachne.service.model;

/**
 * 
 * @author binita.bharati@gmail.com
 * Generic Route Table entry definition.
 *
 */
public class RouteEntry extends Object {
    
    protected String destinationNw;
    protected String gateway;
    protected String netMask;
    protected String metric;
    protected String port;
    //Not present as part of the OS.Added for own house keeping.
    //private String publisherAddress;
    
    public RouteEntry() {
        
    }
    
    
    public RouteEntry(String destinationNw, String gateway, String netmask, 
            String metric, String port) {
        super();
        this.destinationNw = destinationNw;
        this.gateway = gateway;
        this.netMask = netmask;
        this.metric = metric;
        this.port = port;
        //this.publisherAddress = publisherAddress;
    }
    
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if (obj instanceof RouteEntry) {
            RouteEntry temp = (RouteEntry)obj;
            if (this.destinationNw != null && this.destinationNw.equals(temp.destinationNw)
                    && this.gateway != null && this.gateway.equals(temp.gateway)
                         && this.netMask != null && this.netMask.equals(temp.netMask)
                             && this.metric != null && this.metric.equals(temp.metric)
                                 //&& this.port != null && this.port.equals(temp.port)
                                 ) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return destinationNw.hashCode();
    }
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "{destinationNw = " + destinationNw + ", gateway = " + gateway 
                + ", netmask = " + netMask + ", metric = " + metric + ", port = " + port + "}";
                
    }


    public String getDestinationNw() {
        return destinationNw;
    }


    public void setDestinationNw(String destinationNw) {
        this.destinationNw = destinationNw;
    }


    public String getGateway() {
        return gateway;
    }


    public void setGateway(String gateway) {
        this.gateway = gateway;
    }


    public String getNetMask() {
        return netMask;
    }


    public void setNetMask(String netMask) {
        this.netMask = netMask;
    }


    public String getMetric() {
        return metric;
    }


    public void setMetric(String metric) {
        this.metric = metric;
    }


    public String getPort() {
        return port;
    }


    public void setPort(String port) {
        this.port = port;
    }
    

}


