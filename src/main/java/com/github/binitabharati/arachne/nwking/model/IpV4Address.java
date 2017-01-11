package com.github.binitabharati.arachne.nwking.model;

/**
 * 
 * @author binita.bharati@gmail.com
 *
 */

public class IpV4Address {
    
   private String displayName;
   private String ipAddress;
   private short netMask;
   private String networkAddress;
      
   public IpV4Address(String displayName, String ipAddress, short netMask) {
    super();
    this.displayName = displayName;
    this.ipAddress = ipAddress;
    this.netMask = netMask;
   }

    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public short getNetMask() {
        return netMask;
    }
    
    public void setNetMask(short netMask) {
        this.netMask = netMask;
    }
    
       
    public String getNetworkAddress() {
        return networkAddress;
    }

    public void setNetworkAddress(String networkAddress) {
        this.networkAddress = networkAddress;
    }
    
 
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
        public String toString() {
            // TODO Auto-generated method stub
            return "displayName = "+displayName +", ipAddress = "+ipAddress + ", netMask = "+netMask +", networkAddress = "+networkAddress;
        }
   

}
