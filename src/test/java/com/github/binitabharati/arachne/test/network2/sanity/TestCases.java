package com.github.binitabharati.arachne.test.network2.sanity;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.binitabharati.arachne.routing.rip.model.AbstractRouteEntry;
import com.github.binitabharati.arachne.test.network2.sanity.JsonRouteEntry.JsonRouteEntry2;
import com.github.binitabharati.arachne.util.ArachU;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import junit.framework.AssertionFailedError;

public class TestCases {
	
public static final Logger logger = LoggerFactory.getLogger("junitLogger");//prints to arachne.log
	
	private String osName;
    private Properties arachneProp;
    private Properties jilapiProp;
    private Properties junitProp;
    private List<String> hostIpList;
    
    @Before
	public void initTestCaseData() throws Exception
	{
		
		arachneProp = new Properties();
        InputStream is = TestCases.class.getClassLoader().getResourceAsStream("arachne.properties");
        try {
            arachneProp.load(is);
            is.close();
            logger.debug("arachneProp = "+ArachU.getPropertyAsString(arachneProp));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
               
        jilapiProp = new Properties();
        is = TestCases.class.getClassLoader().getResourceAsStream("jilapi.properties");
        try {
            jilapiProp.load(is);
            is.close();
            logger.debug("jilapiProp = "+ArachU.getPropertyAsString(jilapiProp));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        junitProp = new Properties();
        is = TestCases.class.getClassLoader().getResourceAsStream("junit.properties");
        try {
        	junitProp.load(is);
            is.close();
            logger.debug("jilapiProp = "+ArachU.getPropertyAsString(junitProp));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        osName = ArachU.getOsName();
        hostIpList = new ArrayList<>();
        setIpAdddressList();
        logger.debug("");
        	
	}
    
    private void setIpAdddressList() throws Exception {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface iface = networkInterfaces.nextElement();
            String displayName = iface.getDisplayName();
            logger.debug("setIpAdddressList: iface = "+iface);
            logger.debug("setIpAdddressList: displayName = "+displayName);

            try {
                List<InterfaceAddress> ifaceAddress = iface.getInterfaceAddresses();
                for (InterfaceAddress interfaceAdd : ifaceAddress) {
                    logger.debug("setIpAdddressList: interfaceAdd = "+interfaceAdd);
                    InetAddress inetAddress = interfaceAdd.getAddress();
                    String ipAddress = inetAddress.getHostAddress();
                    logger.debug("setIpAdddressList: ipAddress = "+ipAddress);
                    short netMaskLength = interfaceAdd.getNetworkPrefixLength();
                    logger.debug("setIpAdddressList: netMaskLength = "+netMaskLength);
                    hostIpList.add(ipAddress);
                    logger.debug("setIpAdddressList: hostIpList after adding = "+hostIpList);


                }

                
                
            } catch (Exception e) {
                logger.error("init: exception "+e.getMessage(), e);
            }
        }
        logger.debug("setIpAdddressList: hostIpList final = "+hostIpList);

	}
	
	
	/**
	 * 
	 * @throws Exception
	 * Test case to test new route addition.
	 */
	
	
	private void compareRoutes(String expectedRouteJsonfile, String testCaseName) throws Exception {
		
		logger.debug("compareRoutes: invoked with testCaseName = "+testCaseName);
		Gson gson = new Gson();
		
		InputStream in = TestCases.class.getClassLoader().getResourceAsStream(expectedRouteJsonfile); 
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		JsonReader reader = new JsonReader(br);
		//Type listType = new TypeToken<ArrayList<AbstractRouteEntry>>() { }.getType();
		//List<AbstractRouteEntry> expectedRouteEntries = gson.fromJson(reader, listType);
		//fail("Not yet implemented");
		JsonRouteEntry jsonRe = gson.fromJson(reader, JsonRouteEntry.class);
		List<JsonRouteEntry2> entry1 = jsonRe.getEntries();
		for (JsonRouteEntry2 jsonRouteEntry2 : entry1) {
			if (hostIpList.contains(jsonRouteEntry2.getMcIp())) {
				List<AbstractRouteEntry> expectedRouteEntries = jsonRouteEntry2.getRouteEntryList();
				logger.debug(testCaseName + " compareRoutes: expectedRouteEntries = "+expectedRouteEntries);
				logger.debug("compareRoutes: expectedRouteEntries size = "+expectedRouteEntries.size());


				List<AbstractRouteEntry> actualRouteEntries = ArachU.getAbsoluteRoutes(arachneProp, jilapiProp, osName);
				logger.debug(testCaseName +"compareRoutes: actualRouteEntries = "+actualRouteEntries);
				logger.debug("compareRoutes: actualRouteEntries size = "+actualRouteEntries.size());
				
				//compare expected route and actual route entries.
				if (expectedRouteEntries.size() != actualRouteEntries.size()) {
						writeJunitTestResultsToFile(testCaseName + " -  FAILED - " + "Expected route table of size = "+expectedRouteEntries.size() + " but got "+actualRouteEntries.size());
						throw new AssertionFailedError("Expected route table of size = "+expectedRouteEntries.size() + " but got "+actualRouteEntries.size());
					
					
				}
				
				List<AbstractRouteEntry> copyExpectedRouteEntry = expectedRouteEntries.stream().map(eachRe -> {
					return new AbstractRouteEntry(eachRe.getDestinationNw(), eachRe.getGateway(), eachRe.getNetMask(), 
							eachRe.getMetric(), eachRe.getPort(), eachRe.getPublisherAddress());
				}).collect(Collectors.toList());
				logger.debug(testCaseName + "compareRoutes: copyExpectedRouteEntry = "+copyExpectedRouteEntry);

				
				List<AbstractRouteEntry> copyActualRouteEntry = actualRouteEntries.stream().map(eachRe -> {
					return new AbstractRouteEntry(eachRe.getDestinationNw(), eachRe.getGateway(), eachRe.getNetMask(), 
							eachRe.getMetric(), eachRe.getPort(), eachRe.getPublisherAddress());
				}).collect(Collectors.toList());
				logger.debug(testCaseName + "compareRoutes: copyActualRouteEntry = "+copyActualRouteEntry);

				
				//find missing expected route entries
				copyExpectedRouteEntry.removeAll(actualRouteEntries);		
				if (copyExpectedRouteEntry.size() > 0) {
						writeJunitTestResultsToFile(testCaseName + " -  FAILED - " + "Following route entries were expected, but NOT found! "+copyExpectedRouteEntry);
						throw new AssertionFailedError("Following route entries were expected, but NOT found! Expected - "+copyExpectedRouteEntry);
					
				}
				
				//find extra route entries
				copyActualRouteEntry.removeAll(expectedRouteEntries);
				if (copyActualRouteEntry.size() > 0) {
						writeJunitTestResultsToFile(testCaseName + " -  FAILED - " + "Following route entries were NOT expected, but were found! "+copyActualRouteEntry);
						throw new AssertionFailedError("Following route entries were NOT expected, but were found! "+copyActualRouteEntry);
					
				}
			}
		}
		
		writeJunitTestResultsToFile(testCaseName + " -  OK!");
		
	}
	
	private void emptyJunitTestResultsFile() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(new File("/home/vagrant/arachne/junitResults.log"));
		writer.print("");
		writer.close();
	}
	
	private void writeJunitTestResultsToFile(String results) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(new File("/home/vagrant/arachne/junitResults.log"));
		writer.print(results);
		writer.close();
	}


	@Test
	public void test() {
		//fail("Not yet implemented");
		testRouteAddition();
		testRouteUnreachable();
		testStaleRouteDeletion();
	}
	
	private void testRouteAddition() {
		logger.debug("testRouteAddition: entered");
		
		try {
			compareRoutes("network2/test1ExpectedRoute.json", "TEST-ROUTE-ADDITION");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("testRouteAddition: exited");
	}
	
	/**
	 * Test case to test unreachable route.This TC shuts down router1, so that
	 * other machines can make their route entries learnt through router1 as unreachable (16)
	 * @throws Exception 
	 * @throws InterruptedException 
	 */
	private void testRouteUnreachable() {
		try {
			logger.debug("testRouteUnreachable: going to sleep for 30 secs");
			Thread.currentThread().sleep(30*1000);//sleep for some time, so that testRouteAddition result gets printed.
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long curThreadId = Thread.currentThread().getId();
		logger.debug(curThreadId +" - testUnreachableRoutes: entered");
		//emptyJunitTestResultsFile();

		//Need to shutdown m/c(s) as specified in the junit.properties file
		String shutDownMcIps = junitProp.getProperty("junit.test.shutdown.mcs");
		List<String> shutDownMcList = Arrays.asList(shutDownMcIps.split(","));
		logger.debug(curThreadId + " - testUnreachableRoutes: shutDownMcList = "+shutDownMcList);
		
		List<String> tmpList = hostIpList.stream().filter(eachIp -> {
			if (shutDownMcList.contains(eachIp)) {
				return true;
			} else {
				return false;
			}
		}).collect(Collectors.toList());
		
		logger.debug(curThreadId + " - testUnreachableRoutes: tmpList = "+tmpList);
		if (tmpList != null && tmpList.size() > 0) {
			String[] cmndAry = new String[]{"sudo", "poweroff"};
			String cmndPattern = arachneProp.getProperty("shutDown.os." + osName);		       
		       try {
				String json = ArachU.execNativeCommand(jilapiProp, cmndAry);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.debug(curThreadId + "- testUnreachableRoutes: oopsy = "+e.getMessage());
				e.printStackTrace();
			}
		}
		
		try {
			//RIP_ROUTE_STALE_INTERVAL_SECS = 180 secs. Sleep for interval more that that, so that stale routes get updated to unreachable metric.
			Thread.sleep(210*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			compareRoutes("network2/test2ExpectedRoute.json", "TEST-ROUTE-UNREACHABLE");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug(curThreadId + "- testUnreachableRoutes: exiting");
		
	}
	
	private void testStaleRouteDeletion() {
		long curThreadId = Thread.currentThread().getId();
		logger.debug(curThreadId +" - testStaleRouteDeletion: entered");
		try {
			//total route deletion time = stale_timer + hold_down_timer + flush_timer
			Thread.currentThread().sleep(500*1000);
			compareRoutes("network2/test3ExpectedRoute.json", "TEST-ROUTE-DELETION");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
