package com.github.binitabharati.arachne.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.binitabharati.arachne.routing.service.RIPImpl;
import com.github.binitabharati.arachne.routing.service.worker.WorkerFactory;
import com.github.binitabharati.arachne.routing.service.worker.rip.MulticastListener;
import com.github.binitabharati.arachne.util.ArachU;

/**
 * 
 * @author binita
 * Refer: http://www.ibiblio.org/pub/Linux/docs/HOWTO/other-formats/html_single/Multicast-HOWTO.html#ss1.1
 * http://unixadminschool.com/blog/2014/03/rhel-what-is-multicast-and-how-to-configure-network-interface-with-multicast-address/#
 * https://fojta.wordpress.com/2014/09/24/troubleshooting-multicast-with-linux/
 * http://serverfault.com/questions/359536/what-does-this-linux-command-mean-route-add-net-224-0-0-0-netmask-240-0-0-0-et
 * route add -net 224.0.0.0 netmask 240.0.0.0 eth1
 * http://serverfault.com/questions/246508/how-is-the-mtu-is-65535-in-udp-but-ethernet-does-not-allow-frame-size-more-than
 * http://www.9tut.com/rip-routing-protocol-tutorial

 * UDP apps shud try to avoid fragmentation
 * https://notes.shichao.io/tcpv1/ch10/
 * 
 * sudo tcpdump -i eth1 udp  -v
 *
 */

public class Main {
    
    public static final Logger logger = LoggerFactory.getLogger(Main.class);
        
    public static void main(String[] args) throws Exception {
                 
        Properties arachneProp = new Properties();
        InputStream is = Main.class.getClassLoader().getResourceAsStream("arachne.properties");
        try {
            arachneProp.load(is);
            is.close();
            logger.debug("arachneProp = "+ArachU.getPropertyAsString(arachneProp));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
               
        Properties jilapiProp = new Properties();
        is = Main.class.getClassLoader().getResourceAsStream("jilapi.properties");
        try {
            jilapiProp.load(is);
            is.close();
            logger.debug("jilapiProp = "+ArachU.getPropertyAsString(jilapiProp));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String osName = ArachU.getOsName();
        
        String temp = arachneProp.getProperty(ArachU.ACTIVE_ROUTING_PROTOCOLS);
        List<String> activeRoutingProtocols = Arrays.asList(temp.split(","));
        for (String eachRP : activeRoutingProtocols) {
            if (eachRP.equalsIgnoreCase("RIP")) {
            	RIPImpl rps = new RIPImpl(arachneProp, jilapiProp);
               logger.debug("main: sleeping so that remote debugging can be enabled");
               //Thread.currentThread().sleep(1*60*1000);
               rps.init();
                
            }
        }
        
        logger.debug("main: exiting");
        
    }

}
