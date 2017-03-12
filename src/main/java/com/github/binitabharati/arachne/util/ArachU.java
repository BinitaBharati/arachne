/**
 * @author binita.bharati@gmail.com
 */

package com.github.binitabharati.arachne.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.agrona.concurrent.UnsafeBuffer;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.binitabharati.arachne.nwking.model.IpV4Address;
import com.github.binitabharati.arachne.routing.rip.model.AbstractRouteEntry;
import com.github.binitabharati.arachne.routing.service.RIPImpl;
import com.github.binitabharati.arachne.routing.service.worker.rip.MulticastSender;
import com.github.binitabharati.arachne.sbe.stubs.MessageHeaderDecoder;
import com.github.binitabharati.arachne.sbe.stubs.MessageHeaderEncoder;
import com.github.binitabharati.arachne.sbe.stubs.RoutesDecoder;
import com.github.binitabharati.arachne.sbe.stubs.RoutesEncoder;
import com.github.binitabharati.arachne.service.model.RouteEntry;
import com.github.binitabharati.jilapi.Jilapi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author binita.bharati@gmail.com
 * Utility class.
 *
 */

public class ArachU {
    
   
    private static final Logger logger = LoggerFactory.getLogger(ArachU.class);
    
    public static Map<String, Integer> intfToListeningPortMap = new HashMap<>();
    
    //public static final int RIP_UDP_PORT = 522;
    //http://www.iana.org/assignments/multicast-addresses/multicast-addresses.xhtml
    /**
     * Host Extensions for IP Multicasting [RFC1112] specifies the extensions
    required of a host implementation of the Internet Protocol (IP) to
    support multicasting.  The multicast addresses are in the range
    224.0.0.0 through 239.255.255.255. Address assignments are listed below.

    The range of addresses between 224.0.0.0 and 224.0.0.255, inclusive,
    is reserved for the use of routing protocols and other low-level
    topology discovery or maintenance protocols, such as gateway discovery
    and group membership reporting.  Multicast routers should not forward
    any multicast datagram with destination addresses in this range,
    regardless of its TTL.
     */
    public static final String RIP_MULTICAST_GROUP = "228.5.6.7";
    
    /**
     * Refer to http://www.tcpipguide.com/free/t_RIPGeneralOperationMessagingandTimers-3.htm for detailed
     * info on RIP timers.
     * 
     * http://computernetworkingnotes.com/ccna-study-guide/rip-routing-information-protocol-explained.html
     */
    //Each router automatically publishes its routing table periodically to its peers.
    public static final int RIP_ROUTE_PUBLISH_INTERVAL_SECS = 30;
    
    //Once a route is installed, it does not remain valid forever. If within a specified duration
    //no more updates to the same entry arrives, then the route is considered stale
    //(metric marked as 16 which is unreachable) and marked for hold-down.    
    public static final int RIP_ROUTE_STALE_INTERVAL_SECS = 180;
    
    //This timer comes into picture after RIP_ROUTE_STALE_INTERVAL timer has kicked in.
    //Any updates received for a  stale route from a publisher address other then the
    //one from which the stale route was initially learnt, will be ignored.
    //If during the hold-down period, update is received from the initial
    //publisher for a better metric, then the route is removed from being stale.   
    public static final int RIP_ROUTE_HOLD_INTERVAL_SECS = 180;
    
    //Once a route has completed RIP_ROUTE_HOLD_INTERVAL, some time must pass, before it gets really
    //deleted from the routing table.If within that time, RIP response arrives with a better metric
    //for the now marked stale route, then the RIP_ROUTE_STALE_INTERVAL_SECS again gets reset to 0.
    //And the route entry gets updated with the new metric.
    //While, the route is waiting for actual deletion, the RIP response will send the metric
    //to the concerned network as 16 (unreachable).
    public static final int RIP_ROUTE_FLUSH_INTERVAL_SECS = 120;
    
    public static final String BYTE_STREAM_ENTITY_END = "EOE";
    
    public static final String BYTE_STREAM_FILE_END = "EOF";
    
    public static final String ACTIVE_ROUTING_PROTOCOLS = "active.routing.protocols";
    
    public static final String RIP_ROUTE_ENTRY_FILE = "RIP.abstract.route.entry";
    
    public static final String RIP_SPLIT_HORIZON = "RIP.split.horizon";
    
    public static final String WORKERS_MAX_THREAD_COUNT = "workers.max.thread.count";
    
    public static final int WORKERS_DEFAULT_THREAD_COUNT = 3;
    
    public static final String IPV4_SHOW_ROUTE_CMND_PREFIX = "ipV4RouteTable.os.";
    
    public static final String MULTICAST_ROUTE_DESTN = "224.0.0.0";
    /**
     * http://www.regular-expressions.info/optional.htm
       http://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
     */
    private static final String IPv4_address_regex = 
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
    
    public static final String line_sep = System.getProperty("line.separator");
    
    static {
    	intfToListeningPortMap.put("192.168.10", 522);
    	intfToListeningPortMap.put("192.168.20", 523);
    	intfToListeningPortMap.put("192.168.30", 524);
    	intfToListeningPortMap.put("192.168.40", 525);
    	
    	flag = new AtomicBoolean(false);
    }
    
    private static AtomicBoolean flag;
   

    public static Process getProcess(String... command) throws Exception {
        
        Runtime rt = Runtime.getRuntime();
        return rt.exec(command);
        
       
    }
    
    /**
     * 
     * @param command - A var-arg input argument specifying the entire native command line.
     * Including command name, arguments, options
    * @return String
     * @throws IOException 
     * @throws InterruptedException 
     * 
     * Ref : http://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html
       This method should be restricted to be single threaded, as multiple parallel access/edits
       to the native command may not work in a clean manner.
     */
    public static String execNativeCommand(Properties prop, String[] command) throws Exception {
        String curThreadId = Thread.currentThread().getId() + "";
        List<String> cmndStr = Arrays.asList(command);
        logger.debug(curThreadId+ " execNativeCommand: entered with command = "+cmndStr);
        String resultJson = "";
        Runtime rt = Runtime.getRuntime();
        Process proc = null;
        int exitVal = -1;
        while(true) {
        	if (flag.compareAndSet(false, true)) {
                logger.debug(curThreadId+ " execNativeCommand: executing command = "+cmndStr);
           	 proc = rt.exec(command);
           	// any error message?
                StreamGobbler errorGobbler = new 
                    StreamGobbler(proc.getErrorStream(), "ERROR");            
                
                // any output?
                StreamGobbler outputGobbler = new 
                    StreamGobbler(proc.getInputStream(), "OUTPUT");
                    
                // kick them off
                errorGobbler.start();
                outputGobbler.start();
                
                errorGobbler.join();
                outputGobbler.join();
                
                // any error???
                exitVal = proc.waitFor();
                
                logger.info(curThreadId+ " execNativeCommand: cmnd = "+cmndStr + ", output = "+outputGobbler.getResult() + ", exitVal = "+exitVal);       
                if(exitVal != 0) {
                    if(errorGobbler.getResult() != null) {
                        throw new Exception("Command "+Arrays.asList(command) + " did not run normally. "+errorGobbler.getResult());
                    } else {
                        throw new Exception("Command "+Arrays.asList(command) + " did not run normally. ");
                    }
                    
                } else { //success
                    Jilapi jilapi = new Jilapi(prop, "ipV4RouteTable.os.linux");
                    if (outputGobbler.getResult() != null)//Not all commands return response. Eg , successful route add does not return any response.
                        if (outputGobbler.getResult() != null) {
                            resultJson = jilapi.parseCommand(outputGobbler.getResult());
                        }
                    
                    logger.info(curThreadId+ " execNativeCommand: Jilapi: resultJson = "+resultJson);
                }
                flag.compareAndSet(true, false);
                break;
           }
        }
        
        
              
        return resultJson;
    }
    
    public static String preetyPrintJson(String uglyJson) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJson);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }
    
    public static <Output> List<Output> mapJsonArray(String actualJson, 
             Type arrayType) throws Exception {      
        Gson gson = new Gson();
        List<Output> retList = gson.fromJson(actualJson, arrayType);      
        return retList;
    }
    
    public static <Output> Output mapJson(String actualJson, 
             Class mapperKlass) throws Exception {
        Gson gson = new Gson();
        Output retObj = (Output)gson.fromJson(actualJson, mapperKlass);        
        return retObj;        
    }
   
   /**
    * 
    * @param input
    * @return - The index of the first valid IpV4 address in the input string
    */
   public static int validIPv4Address(String input)
   {
       Pattern pattern = Pattern.compile(IPv4_address_regex);
       Matcher matcher = pattern.matcher(input);
       
       while(matcher.find()) {
           System.out.println("Start index: " + matcher.start());
           System.out.println(" End index: " + matcher.end());
           System.out.println(" Found: " + matcher.group());
       }
       
       return -1;              
   }
   
   public static String getOsName() {
       String OS = System.getProperty("os.name").toLowerCase();
         
       if(OS.indexOf("win") >= 0)
           return "windows";
       else if (OS.indexOf("mac") >= 0)
           return "mac";
       else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0)
           return "linux";
       else if (OS.indexOf("sunos") >= 0)
           return "solaris";
       return null;

   }
 
   public static List<RouteEntry> getRoutes(Properties arachneProp, Properties jilapiProp, String osName) throws Exception {
       List<RouteEntry> routes = null;
       String[] cmnd = arachneProp.getProperty("ipV4RouteTable.os."+osName).split(" ");
       //String tmp = prop.getProperty("ipV4RouteTable.os."+osName);
       String resultJson = ArachU.execNativeCommand(jilapiProp, cmnd);
       if (resultJson != null) {
           if (resultJson.trim().startsWith("[")) { //json array
               Type listType = new TypeToken<ArrayList<RouteEntry>>() { }.getType();
               routes = ArachU.mapJsonArray(resultJson, listType);
           }
       }
       routes = routes.stream().filter(eachRouteEntry -> {
    	   if (!eachRouteEntry.getDestinationNw().equals(ArachU.MULTICAST_ROUTE_DESTN)) {
    		   return true;
    	   } else {
    		   return false;
    	   }
       }).collect(Collectors.toList());
       return routes;
   }
   
   public static String getRoutesJson(Properties arachneProp, Properties jilapiProp, String osName) throws Exception {
       String[] cmnd = arachneProp.getProperty("ipV4RouteTable.os."+osName).split(" ");
       //String tmp = prop.getProperty("ipV4RouteTable.os."+osName);
       String resultJson = ArachU.execNativeCommand(jilapiProp, cmnd);
       /*if (resultJson != null) {
           if (resultJson.trim().startsWith("[")) { //json array
               Type listType = new TypeToken<ArrayList<RouteEntry>>() { }.getType();
               routes = ArachU.mapJsonArray(resultJson, listType);
           }
       }*/
       return resultJson;
   }
   
   
   public static Queue<AbstractRouteEntry> loadAbstractRoutes(Properties arachneProp, Properties jilapiProp, String osName) throws Exception {
       Queue<AbstractRouteEntry> absRouteEntry = new ConcurrentLinkedQueue<AbstractRouteEntry>();
       /*File file = new File(arachneProp.getProperty(ArachU.RIP_ROUTE_ENTRY_FILE));
       if (file.exists()) {
           FileInputStream fis = new FileInputStream(file);
           
           
           //Construct BufferedReader from InputStreamReader
             BufferedReader br = new BufferedReader(new InputStreamReader(fis));
          
             String line = null;
             while ((line = br.readLine()) != null) {
                 //System.out.println(line);
                 String[] tmp = line.split(";");
                 AbstractRouteEntry are = new AbstractRouteEntry(tmp[0], tmp[1], tmp[2], tmp[3], 
                         tmp[4], tmp[5], Boolean.parseBoolean(tmp[6]));
                 boolean status = absRouteEntry.offer(are);               
             }
       } else {
           absRouteEntry = initAbsoluteRoutes(arachneProp, jilapiProp, osName);
                      
       }*/
       
       
       return initAbsoluteRoutes(arachneProp, jilapiProp, osName);
              
   }
   
   /**
    * Makes entry into sqlite ROUTE_ENTRY table for all OS route entries.Since, route entries are directly being made from the
    * OS route table, the publisherAdress field will be null, as OS does not store route publisher address.
    */
   public static Queue<AbstractRouteEntry> initAbsoluteRoutes(Properties arachneProp, Properties jilapiProp, String osName) throws Exception {
       logger.debug("initAbsoluteRoutes: entered with arachneProp = "+arachneProp + ", osName = "+osName);
       Queue<AbstractRouteEntry> absRouteEntry = new ConcurrentLinkedQueue<AbstractRouteEntry>();
       
       String[] cmnd = arachneProp.getProperty(ArachU.IPV4_SHOW_ROUTE_CMND_PREFIX + osName).split(" ");           
       String jsonResponse = ArachU.execNativeCommand(jilapiProp, cmnd);
       logger.debug("initAbsoluteRoutes: jsonResponse = "+jsonResponse);
       try {
           createTable();
       } catch(Exception ex){
           //Table already exists Exception is OK!
           logger.error("createTable exception", ex);
       }
       
       Gson gson = new Gson();
       Type listType = new TypeToken<ArrayList<RouteEntry>>() { }.getType();
       List<RouteEntry> routeList = gson.fromJson(jsonResponse, listType);
       //RouteEntry lastRouteEntry = routeList.get(routeList.size() - 1);
       
       for (RouteEntry each : routeList) {
           AbstractRouteEntry are = new AbstractRouteEntry(each.getDestinationNw(), each.getGateway(), 
                   each.getNetMask(), each.getMetric(), each.getPort(), null);
           logger.debug("getDBEntryId: handling routeEntry = "+are);
           absRouteEntry.offer(are);
           
           int entryId = getDBEntryId(are);
           if (entryId == -1) {
               //Not present in DB. Need to insert entry.
               insertDBRoute(arachneProp, jilapiProp, are, osName);
           }
           
          
           
           //fos.write(sb.toString().getBytes());
       }  
      /* fos.flush();
       fos.close();*/
       
       return absRouteEntry;
   }
   
   
   public static Queue<AbstractRouteEntry> installAbsoluteRoutes(Properties arachneProp, Properties jilapiProp, String osName) throws Exception {
       Queue<AbstractRouteEntry> absRouteEntry = loadAbstractRoutes(arachneProp, jilapiProp, osName);
       
       return absRouteEntry;
     }
   
   /**
    * Reads absolute routes from DB. Not reading from OS,m as OS route table may not have all the required
    * entries, like th epublishedAddress for RIP protocol.
    * @param arachneProp
    * @param jilapiProp
    * @param osName
    * @return
    * @throws Exception
    */
   public static List<AbstractRouteEntry> getAbsoluteRoutes(Properties arachneProp, Properties jilapiProp, String osName) throws Exception {
       logger.debug("getAbsoluteRoutes: entered with arachneProp = "+arachneProp + ", osName = "+osName);
       
       List<AbstractRouteEntry> absRouteEntry = new ArrayList<AbstractRouteEntry>();
       
       Connection c = null;
       PreparedStatement  stmt = null;
       int key = -1;
       try {
         c = getConnection();
         //c.setAutoCommit(false);
         logger.debug("getAbsoluteRoutes: Opened database successfully");

         String readSql = "SELECT * from ROUTE_ENTRY";
         stmt = c.prepareStatement(readSql);       
         
         ResultSet rs = stmt.executeQuery();
         
         while (rs.next()) {
        	 AbstractRouteEntry absRe = new AbstractRouteEntry(rs.getString("DESTINATION_NW"), rs.getString("GATEWAY"), rs.getString("NETMASK"), 
        			 rs.getString("METRIC"), rs.getString("PORT"), rs.getString("PUBLISHER_ADDRESS"));
        	 absRouteEntry.add(absRe);
         }

         stmt.close();
         //c.commit();
         //c.close();
       } catch ( Exception e ) {
         e.printStackTrace();
         //System.exit(0);
       }
       finally {
           if (c != null) {
               c.close();
           }
       }
       logger.debug("getAbsoluteRoutes: exiting with = "+absRouteEntry);
       return absRouteEntry;
       
   }
   
   /** Can routing tables have multiple entry for same destination ? 
    * Yes, of-course, thats why you have the metrics in routing table. 
    * No, you should not ideally have duplicate route entries for the same destination.
    * 
    * https://supportforums.cisco.com/discussion/11829446/multiple-entries-same-destination-show-ip-route 
    * http://linux-ip.net/html/routing-tables.html
    * http://www.czerno.com/default.asp?inc=/html/tcpip/ip_basics-routing_101-pg7.asp
    * 
    * http://superuser.com/questions/622750/does-the-order-of-entries-in-the-routing-table-matter
    * 
    * https://www.google.co.in/url?sa=t&rct=j&q=&esrc=s&source=web&cd=5&cad=rja&uact=8&ved=0ahUKEwiLuMzgvtbMAhVMNY8KHSTJAm4QFgg1MAQ&url=http%3A%2F%2Fupload.evilzone.org%2F%3Fpage%3Ddownload%26file%3DObS6omEXeUpcPbz47jb8NQgDQRSaWqyKl4TKHdGzkAu8ruPFjb&usg=AFQjCNGTZEztiRCkhr6fToF979iglb0gRw&sig2=Itb7lxnA3Ay2VpWGRfHwiQ
    * 
    * @param absRouteEntry
    * @param newEntry
    * @return
 * @throws Exception 
    */

   public static Connection getConnection() {
       Connection c = null;
       try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:test.db");
         
       } catch ( Exception e ) {
         logger.error("Exception while getting DB connection -- ", e);
         //System.exit(0);
       }
       logger.debug("Opened database successfully");
       return c;
   }
   
   public static void createTable() throws SQLException {
       Connection c = null;
       Statement stmt = null;
       try {
         Class.forName("org.sqlite.JDBC");
         c = getConnection();
         System.out.println("Opened database successfully");

         stmt = c.createStatement();
         String sql = "CREATE TABLE ROUTE_ENTRY " +
                      "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                      " DESTINATION_NW           TEXT    NOT NULL, " + 
                      " GATEWAY           TEXT    NOT NULL, " +
                      " NETMASK           TEXT    NOT NULL, " +
                      " METRIC            TEXT     NOT NULL, " + 
                      " PORT        TEXT     NOT NULL, " + 
                      " PUBLISHER_ADDRESS    TEXT) ";
         stmt.executeUpdate(sql);
         stmt.close();
         //c.close();
       } catch ( Exception e ) {
           logger.error("Exception while createTable -- ", e);
         //System.exit(0);
       } finally {
           if (c != null) {
               c.close();
           }
           
       }
       
       logger.debug("Table created successfully");
   }

   public static void insertDBRoute(Properties arachneProp, Properties jilapiProp, 
           AbstractRouteEntry are, String os) throws Exception {
       logger.debug("insertDBRoute: entered with "+are);
       
       Connection c = null;
       PreparedStatement  stmt = null;
       try {
         c = getConnection();
         //c.setAutoCommit(false);
         System.out.println("Opened database successfully");

         String sql = "INSERT INTO ROUTE_ENTRY (ID,DESTINATION_NW,GATEWAY,NETMASK,METRIC,PORT,PUBLISHER_ADDRESS) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ? );"; 
         stmt = c.prepareStatement(sql);
         //stmt.setInt(1, 1);  //https://www.sqlite.org/autoinc.html
         stmt.setString(2, are.getDestinationNw());
         stmt.setString(3, are.getGateway());
         stmt.setString(4, are.getNetMask());
         stmt.setString(5, are.getMetric());
         stmt.setString(6, are.getPort());
         stmt.setString(7, are.getPublisherAddress());
                   
         stmt.executeUpdate();

         stmt.close();
         //c.commit();
         //c.close();
       } catch ( Exception e ) {
         logger.error("Exception while inserting entry to DB", e);
         e.printStackTrace();
       }
       finally {
           if (c != null) {
               c.close();
           }
       }
   }
   
   public static void addRoute(AbstractRouteEntry are, Properties arachneProp, 
           Properties jilapiProp, String os) throws Exception {
	   String curThreadId = Thread.currentThread().getId() + "";
	   logger.debug(curThreadId + " addRoute: entered with "+are);
       //The insertion to DB and memory should happen in a single transaction ideally.
       //First insert route into system.This command does not produce any input stream, but on error, may produce a error stream
       //If error stream is produced the execNativeCommand will throw Exception.
       String cmndPattern = arachneProp.getProperty("ipV4RouteTable.insert.os." + os);
       MessageFormat mf = new MessageFormat(cmndPattern);
       String[] cmnd = mf.format(cmndPattern, new Object[]{are.getDestinationNw(), 
               are.getNetMask(), are.getPort(), are.getMetric(), are.getPublisherAddress().substring(1)}).split(" ");
       String json = execNativeCommand(jilapiProp, cmnd);
       
       
       //If execNativeCommand method is successful, then update DB.
       Connection c = null;
       PreparedStatement  stmt = null;
       try {  
    	     //Allow only one thread to add entry for a combination of destionationNw and netMask
    	     String lockStr = are.getDestinationNw() + are.getNetMask() + "".intern();
    	     //synchronized (lockStr) {
    	    	 c = getConnection();
                 System.out.println("Opened database successfully");
                 
                 //test start
                 /*List<AbstractRouteEntry> tmp1 = getAbsoluteRoutes(arachneProp, jilapiProp, os);
	    		 logger.debug(curThreadId + " addRoute: b4 adding = "+are + " DB looks as below :");
	    		 logger.debug(curThreadId + " addRoute: b4 adding = "+tmp1);*/
                 //test end
	    		 
    	    	 //check if the combination of destionationNw and netMask already exists.
    	    	 String readSql = "SELECT ID from ROUTE_ENTRY WHERE DESTINATION_NW = ? AND  NETMASK = ?";
    	    	 stmt = c.prepareStatement(readSql);
    	    	 stmt.setString(1, are.getDestinationNw());
    	    	 stmt.setString(2, are.getNetMask());
                 //c.setAutoCommit(false);
    	    	 int key = -1;
    	    	 ResultSet rs = stmt.executeQuery();
    	    	 while (rs.next()) {
    	             key  = rs.getInt("ID");
    	             break; //there should be only 1 entry for a particular DESTINATION_NW & NETMASK
    	         }
    	    	 
    	    	 if (key != -1) {
    	    		   logger.debug(curThreadId + " addRoute: route entry = "+are + " already exists!. Returning without adding.");

    	    		   return;
    	    	 }
    	        
                 String sql = "INSERT INTO ROUTE_ENTRY (ID,DESTINATION_NW,GATEWAY,NETMASK,METRIC,PORT,PUBLISHER_ADDRESS) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ? );"; 
                 stmt = c.prepareStatement(sql);
                 //stmt.setInt(1, 1);  //https://www.sqlite.org/autoinc.html
                 stmt.setString(2, are.getDestinationNw());
                 stmt.setString(3, are.getGateway());
                 stmt.setString(4, are.getNetMask());
                 stmt.setString(5, are.getMetric());
                 stmt.setString(6, are.getPort());
                 stmt.setString(7, are.getPublisherAddress());
                           
                 stmt.executeUpdate();

                 stmt.close();
                 
                 //test start
                 /*List<AbstractRouteEntry> tmp2 = getAbsoluteRoutes(arachneProp, jilapiProp, os);
	    		 logger.debug(curThreadId + " addRoute: after adding = "+are + " DB looks as below :");
	    		 logger.debug(curThreadId + " addRoute: after adding = "+tmp2);*/
                 //test end
                 //c.commit();
                 //c.close();
			//}
             
       } catch ( Exception e ) {
         e.printStackTrace();
         //System.exit(0);
       }
       finally {
           if (c != null) {
               c.close();
           }
       }
   }
   
   
   public static void editRoute(AbstractRouteEntry are, Properties arachneProp, 
           Properties jilapiProp, String os) throws Exception {
       String curThreadId = Thread.currentThread().getId() + "";
       logger.info(curThreadId+" editRoute: entered with os "+os);
       logger.info(curThreadId+ " editRoute: entered with "+are);
       //First delete and then insert route into system (there is no edit route command).This command does not produce any input stream, but on error, may produce a error stream
       //If error stream is produced the execNativeCommand will throw Exception.
       String cmndPattern = arachneProp.getProperty("ipV4RouteTable.delete.os." + os);
       MessageFormat mf = new MessageFormat(cmndPattern);
       String[] cmnd = mf.format(cmndPattern, new Object[]{are.getDestinationNw(), 
               are.getNetMask(), are.getPort()}).split(" ");
       logger.info(curThreadId+ " editRoute: delete command "+Arrays.asList(cmnd));
       String json = execNativeCommand(jilapiProp, cmnd);
       logger.info(curThreadId+ " editRoute: after delete, OS routes = "+getRoutes(arachneProp, jilapiProp, os));
       
       cmndPattern = arachneProp.getProperty("ipV4RouteTable.insert.os." + os);
       mf = new MessageFormat(cmndPattern);
       cmnd = mf.format(cmndPattern, new Object[]{are.getDestinationNw(),
               are.getNetMask(), are.getPort(), are.getMetric(), are.getPublisherAddress().substring(1)}).split(" ");
       logger.info(curThreadId+ " editRoute: insert command "+Arrays.asList(cmnd));
       json = execNativeCommand(jilapiProp, cmnd);
       logger.info(curThreadId+ " editRoute: after insert, OS routes = "+getRoutes(arachneProp, jilapiProp, os));
       
       
       //If execNativeCommand method is successful, then update DB.
       Connection c = null;
       PreparedStatement  stmt = null;
       try {
         c = getConnection();
         //c.setAutoCommit(false);
         System.out.println("Opened database successfully");
         
         //test start
         List<AbstractRouteEntry> tmp2 = getAbsoluteRoutes(arachneProp, jilapiProp, os);
		 logger.debug(curThreadId + " addRoute: b4 editing = "+are + " DB looks as below :");
		 logger.debug(curThreadId + " addRoute: b4 editing = "+tmp2);
         //test end
		 
         int id = getDBEntryId(are);
         logger.debug(curThreadId+ " editRoute: DB entry Id "+id);

         String sql = "UPDATE ROUTE_ENTRY SET GATEWAY = ?, METRIC = ?, PORT = ?, PUBLISHER_ADDRESS = ? where ID = ?";
         stmt = c.prepareStatement(sql);
         //stmt.setInt(1, 1);  //https://www.sqlite.org/autoinc.html
         stmt.setString(1, are.getGateway());
         stmt.setString(2, are.getMetric());
         stmt.setString(3, are.getPort());
         stmt.setString(4, are.getPublisherAddress());
         stmt.setInt(5, id);
        
         stmt.executeUpdate();

         stmt.close();
         
         //test start
         List<AbstractRouteEntry> tmp3 = getAbsoluteRoutes(arachneProp, jilapiProp, os);
		 logger.debug(curThreadId + " addRoute: after editing = "+are + " DB looks as below :");
		 logger.debug(curThreadId + " addRoute: after editing = "+tmp3);
         //test end
		 
         //c.commit();
         //c.close();
       } catch ( Exception e ) {
         e.printStackTrace();
         //System.exit(0);
       }
       finally {
           if (c != null) {
               c.close();
           }
       }
       //test binita start
       logger.info(curThreadId+ " editRoute: final OS routes = "+getRoutes(arachneProp, jilapiProp, os));
       //test binita end
   }
   
   public static void deleteRoute(AbstractRouteEntry are, Properties arachneProp, 
           Properties jilapiProp, String os) throws Exception {
	   String curThreadId = Thread.currentThread().getId() + "";
       logger.info(curThreadId+ " deleteRoute: entered with = "+are + "");
       logger.info(curThreadId+ " deleteRoute: entered with os = "+os);
     //First delete and then insert route into system (there is no edit route command).This command does not produce any input stream, but on error, may produce a error stream
       //If error stream is produced the execNativeCommand will throw Exception.
	   String cmndPattern = arachneProp.getProperty("ipV4RouteTable.delete.os." + os);
         logger.info("deleteRoute: cmndPattern  = "+cmndPattern );
       MessageFormat mf = new MessageFormat(cmndPattern);
       String[] cmnd = mf.format(cmndPattern, new Object[]{are.getDestinationNw(), 
               are.getNetMask(), are.getPort()}).split(" ");
       String json = execNativeCommand(jilapiProp, cmnd);
       
       Connection c = null;
       PreparedStatement  stmt = null;
       try {
         c = getConnection();
         //c.setAutoCommit(false);
         System.out.println("Opened database successfully");
         
         int id = getDBEntryId(are);

         String sql = "DELETE FROM ROUTE_ENTRY where ID = ?";
         stmt = c.prepareStatement(sql);
         //stmt.setInt(1, 1);  //https://www.sqlite.org/autoinc.html
         //stmt.setString(1, are.getMetric());
         stmt.setInt(1, id);
        
         stmt.executeUpdate();

         stmt.close();
         //c.commit();
         //c.close();
       } catch ( Exception e ) {
         e.printStackTrace();
         //System.exit(0);
       }
       finally {
           if (c != null) {
               c.close();
           }
       }
       //test binita start
       logger.info(curThreadId+ " deleteRoute: final OS routes = "+getRoutes(arachneProp, jilapiProp, os));
       //test binita end
   }
   
   public static int getDBEntryId(AbstractRouteEntry are) throws SQLException {
       Connection c = null;
       PreparedStatement  stmt = null;
       int key = -1;
       try {
         c = getConnection();
         //c.setAutoCommit(false);
         logger.debug("getDBEntryId:Opened database successfully");

         String readSql = "SELECT * from ROUTE_ENTRY WHERE DESTINATION_NW = ? and NETMASK = ?";
         stmt = c.prepareStatement(readSql);
         stmt.setString(1, are.getDestinationNw());
         stmt.setString(2, are.getNetMask());         
         
         ResultSet rs = stmt.executeQuery();
         logger.debug("getDBEntryId: got route entries for "+are);

         int rsCount = 0;
         while (rs.next()) {
             key  = rs.getInt("ID");
             //break; //there should be only 1 entry for a particular DESTINATION_NW & NETMASK
             rsCount++;
         }
         logger.debug("getDBEntryId: got route entries of size "+rsCount);

         stmt.close();
         //c.commit();
         //c.close();
       } catch ( Exception e ) {
         e.printStackTrace();
         //System.exit(0);
       }
       finally {
           if (c != null) {
               c.close();
           }
       }
       logger.debug("getDBEntryId: exiting with key = "+key);
       return key;
   }
   
   public static String getPropertyAsString(Properties prop) {    
       StringWriter writer = new StringWriter();
       prop.list(new PrintWriter(writer));
       return writer.getBuffer().toString();
     }
    
   
   public static byte[] serialize(Object obj) throws IOException {
       try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
           try(ObjectOutputStream o = new ObjectOutputStream(b)){
               o.writeObject(obj);
           }
           return b.toByteArray();
       }
   }

   public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
       try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
           try(ObjectInputStream o = new ObjectInputStream(b)){
               return o.readObject();
           }
       }
   }
   
   public static byte[] encodeRoute(List<RouteEntry> reList)
   {
       logger.debug("encodeRoute: entered with "+reList);
       MessageHeaderEncoder MESSAGE_HEADER_ENCODER = new MessageHeaderEncoder();
       RoutesEncoder routeEncoder = new RoutesEncoder();
       
       ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
       UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);
       int bufferOffset = 0;
       int encodingLength = 0;

       // Setup for encoding a message
       MESSAGE_HEADER_ENCODER
           .wrap(directBuffer, bufferOffset)
           .blockLength(routeEncoder.sbeBlockLength())
           .templateId(routeEncoder.sbeTemplateId())
           .schemaId(routeEncoder.sbeSchemaId())
           .version(routeEncoder.sbeSchemaVersion());

       bufferOffset += MESSAGE_HEADER_ENCODER.encodedLength();
       encodingLength += MESSAGE_HEADER_ENCODER.encodedLength();
       final int srcOffset = 0;

       routeEncoder.wrap(directBuffer, bufferOffset);
           

       // An exception will be raised if the string length is larger than can be encoded in the varDataEncoding length field
       // Please use a suitable schema type for varDataEncoding.length: uint8 <= 254, uint16 <= 65534
       RoutesEncoder.RouteEntriesEncoder re_enc = routeEncoder.routeEntriesCount(reList.size());
       
       for (RouteEntry routeEntry : reList) {
           re_enc
           .next()
           .destinationNw(routeEntry.getDestinationNw())
           .gateway(routeEntry.getGateway())
           .netMask(routeEntry.getNetMask())
           .metric(routeEntry.getMetric())         
           .port(routeEntry.getPort());
        
    }
       byte[] ret = byteBuffer.array();
       logger.debug("encodeRoute: exiting with byte ary of size"+ret.length);
       return ret;
   }
   
   public static List<AbstractRouteEntry> decodeRoute(byte[] input,String publisherAddress)
           throws Exception
       {
           logger.debug("decodeRoute: entering with "+publisherAddress + ", byteAry of size = "+input.length);

           List<AbstractRouteEntry> routeList = new ArrayList<>();
           
           //ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4096);
           UnsafeBuffer directBuffer = new UnsafeBuffer(input);
           
           MessageHeaderDecoder MESSAGE_HEADER_DECODER = new MessageHeaderDecoder();
           MESSAGE_HEADER_DECODER.wrap(directBuffer, 0);
           RoutesDecoder routesDecoder = new RoutesDecoder();


           final int actingBlockLength = MESSAGE_HEADER_DECODER.blockLength();
           final int schemaId = MESSAGE_HEADER_DECODER.schemaId();
           final int actingVersion = MESSAGE_HEADER_DECODER.version();

           routesDecoder.wrap(directBuffer, MESSAGE_HEADER_DECODER.encodedLength(), actingBlockLength, actingVersion);

           
           for (final RoutesDecoder.RouteEntriesDecoder re_dec : routesDecoder.routeEntries())
           {
               AbstractRouteEntry are = new AbstractRouteEntry(re_dec.destinationNw(), re_dec.gateway(), re_dec.netMask(), 
                       re_dec.metric(), re_dec.port(), publisherAddress);
               routeList.add(are);
               
           }
          
           logger.debug("decodeRoute: exiting with "+routeList);
           return routeList;
       }
   
   public static String getNetworkAddress(String ipAddressCidrNotation) {
       SubnetUtils ref = new SubnetUtils(ipAddressCidrNotation);
       SubnetInfo info = ref.getInfo();
       return info.getNetworkAddress();
      
   }
   
   public static String getRouteInterface(List<IpV4Address> ipAdresses,
           String routeSenderAddress) {
       
       for (IpV4Address ipV4Address : ipAdresses) {
           String networkAddress = ipV4Address.getNetworkAddress();
           String networkPrefix = networkAddress.substring(0, networkAddress.indexOf("0"));           
           if (routeSenderAddress.indexOf(networkPrefix) != -1)  {
               return ipV4Address.getDisplayName();
           }
    }
       return null;
   }
   
   
   
    public static void main(String[] args) throws Exception {
        //mapRoute(null);
        //execNativeCommand("route", "print");
        //validIPv4Address("Kernel IP routing tableDestination     Gateway         Genmask         Flags Metric Ref    Use Iface0.0.0.0         10.0.2.2        0.0.0.0         UG    0      0        0 eth010.0.2.0        0.0.0.0         255.255.255.0   U     0      0        0 eth0192.168.10.0    0.0.0.0         255.255.255.0   U     0      0        0 eth1");
        System.out.println(getNetworkAddress("192.168.10.12/24"));
    }
    
    
}
