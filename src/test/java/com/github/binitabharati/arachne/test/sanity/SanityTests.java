package com.github.binitabharati.arachne.test.sanity;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.binitabharati.arachne.routing.rip.model.AbstractRouteEntry;
import com.github.binitabharati.arachne.service.Main;
import com.github.binitabharati.arachne.service.model.RouteEntry;
import com.github.binitabharati.arachne.util.ArachU;

public class SanityTests {
	public static final Logger logger = LoggerFactory.getLogger(SanityTests.class);
	
	private String[] networkIps;
	private String osName;
    private Properties arachneProp;
    private Properties jilapiProp;
    //Why is there more than a single List<RouteEntry> for a single machine ?
    /* Becoz, we do not know the interface from which the route info could arrive.
     * Eg - router1 can get to know about network 192.168.30.0, netmask 255.255.255.0 from
     * either of its eth2 or eth3 interface.
     * 
     */
    private Map<String, List<List<RouteEntry>>> machineToRouteList;

	@Before
	public void initTestCaseData()
	{
		networkIps = new String[]{"192.168.10.12", "192.168.10.13", "192.168.10.11",
				"192.168.40.12", "192.168.20.13", "192.168.20.14", "192.168.20.12",
				"192.168.30.12","192.168.30.13"};
		
		arachneProp = new Properties();
        InputStream is = SanityTests.class.getClassLoader().getResourceAsStream("arachne.properties");
        try {
            arachneProp.load(is);
            is.close();
            logger.debug("arachneProp = "+ArachU.getPropertyAsString(arachneProp));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
               
        jilapiProp = new Properties();
        is = Main.class.getClassLoader().getResourceAsStream("jilapi.properties");
        try {
            jilapiProp.load(is);
            is.close();
            logger.debug("jilapiProp = "+ArachU.getPropertyAsString(jilapiProp));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        osName = ArachU.getOsName();
        
        machineToRouteList = new HashMap<>();
        List<List<RouteEntry>> absReTop = new ArrayList<>();
        
        List<RouteEntry> absReStatic = new ArrayList<>();
        absReStatic.add(new RouteEntry("0.0.0.0", "10.0.2.2", "0.0.0.0", "0", "eth0"));
        absReStatic.add(new RouteEntry("10.0.2.0", "0.0.0.0", "255.255.255.0", "0", "eth0"));
        absReStatic.add(new RouteEntry("224.0.0.0", "0.0.0.0", "224.0.0.0", "0", "eth1"));
        
        List<RouteEntry> absRe = new ArrayList<>(); 
        absRe.addAll(absReStatic);
        absRe.add(new RouteEntry("192.168.10.0", "0.0.0.0", "255.255.255.0", "0", "eth1"));
        absRe.add(new RouteEntry("192.168.20.0", "0.0.0.0", "255.255.255.0", "0", "eth1"));
        absRe.add(new RouteEntry("192.168.30.0", "0.0.0.0", "255.255.255.0", "0", "eth1"));
        absRe.add(new RouteEntry("192.168.40.0", "0.0.0.0", "255.255.255.0", "0", "eth1"));
        
        absReTop.add(absRe);
        machineToRouteList.put("192.168.10.12", absReTop);
        machineToRouteList.put("192.168.10.13", absReTop);
        
        absReTop = new ArrayList<>();
        absRe = new ArrayList<>();
        absRe.addAll(absReStatic);      
        absRe.add(new RouteEntry("192.168.10.0", "0.0.0.0", "255.255.255.0", "0", "eth1"));
        absRe.add(new RouteEntry("192.168.20.0", "0.0.0.0", "255.255.255.0", "0", "eth2"));
        absRe.add(new RouteEntry("192.168.30.0", "0.0.0.0", "255.255.255.0", "0", "eth2"));
        absRe.add(new RouteEntry("192.168.40.0", "0.0.0.0", "255.255.255.0", "0", "eth3"));        
        absReTop.add(absRe);
        
        absRe = new ArrayList<>();
        absRe.addAll(absReStatic);      
        absRe.add(new RouteEntry("192.168.10.0", "0.0.0.0", "255.255.255.0", "0", "eth1"));
        absRe.add(new RouteEntry("192.168.20.0", "0.0.0.0", "255.255.255.0", "0", "eth2"));
        absRe.add(new RouteEntry("192.168.30.0", "0.0.0.0", "255.255.255.0", "0", "eth3"));
        absRe.add(new RouteEntry("192.168.40.0", "0.0.0.0", "255.255.255.0", "0", "eth3"));        
        absReTop.add(absRe);
        
        machineToRouteList.put("192.168.10.11", absReTop);
        
        absReTop = new ArrayList<>();
        absRe = new ArrayList<>();
        absRe.addAll(absReStatic);      
        absRe.add(new RouteEntry("192.168.10.0", "0.0.0.0", "255.255.255.0", "0", "eth1"));
        absRe.add(new RouteEntry("192.168.20.0", "0.0.0.0", "255.255.255.0", "0", "eth1"));
        absRe.add(new RouteEntry("192.168.30.0", "0.0.0.0", "255.255.255.0", "0", "eth1"));
        absRe.add(new RouteEntry("192.168.40.0", "0.0.0.0", "255.255.255.0", "0", "eth1"));        
        absReTop.add(absRe);

		
	}
	
	@Test
	public void testRouteAddition() throws Exception {
		//fail("Not yet implemented");
		List<AbstractRouteEntry> absRouteEntry = ArachU.getAbsoluteRoutes(arachneProp, jilapiProp, osName);
		
	}

}
