package com.github.binitabharati.arachne.multicast.io;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.junit.Test;

public class GetAllNwInterfaces {

    @Test
    public void test() throws Exception {
        //fail("Not yet implemented");
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            System.out.println(e.nextElement());
        }
        
    }
    
    @Test
    public void test2() throws Exception {
        InetAddress localhost = InetAddress.getLocalHost();
        InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
        if (allMyIps != null && allMyIps.length > 1) {
            for (int i = 0; i < allMyIps.length; i++) {
                System.out.println("    " + allMyIps[i]);
            }
          }
        
    }

}
