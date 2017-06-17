package com.github.binitabharati.arachne.routing.service.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.binitabharati.arachne.util.ArachU;

/**
 * 
 * @author binita.bharati@gmail.com
 *
 */

public class WorkerFactory {
    
    private ExecutorService workerService;
    private Properties arachneProp;
    private Properties jilapiProp;
    
    private Map<String, List<Worker>> routingProtocolToWorkerMap;
    
    public WorkerFactory(Properties arachneProp, Properties jilapiProp) {
        this.arachneProp = arachneProp;
        this.jilapiProp = jilapiProp;
        
        String tmp = arachneProp.getProperty(ArachU.WORKERS_MAX_THREAD_COUNT);
        if (tmp != null && !tmp.equals("")) {
            workerService = Executors.newFixedThreadPool(Integer.parseInt(tmp.trim())); 
        } else {
            workerService = Executors.newFixedThreadPool(ArachU.WORKERS_DEFAULT_THREAD_COUNT);
        }
        
        String activeRoutingProtocols = arachneProp.getProperty(ArachU.ACTIVE_ROUTING_PROTOCOLS).trim();
        
        int noOfActiveRoutingProtocol = 1;
        if (activeRoutingProtocols.indexOf(",") != -1) {
            //count no of commas appearing in string
            int noOfCommas = activeRoutingProtocols.length() - activeRoutingProtocols.replaceAll(",", "").length();
            noOfActiveRoutingProtocol = noOfCommas + 1;
        }
        workerService = Executors.newFixedThreadPool(noOfActiveRoutingProtocol * 3); //Also, use the no of cores in calculation
              
    }
    
    /**
     * 
     */
   
    
    public WorkerFactory() { //Not thread safe constructor.
        if (routingProtocolToWorkerMap == null) {
            routingProtocolToWorkerMap = new HashMap<String, List<Worker>>();
        }
        
    }
       
    public void addWorker(String rps, Worker worker) {
        List<Worker> tmp = null;
        if (routingProtocolToWorkerMap.containsKey(rps)) {
                tmp = routingProtocolToWorkerMap.get(rps);              
        } else {
                tmp = new ArrayList<Worker>();           
        }
        tmp.add(worker);
        routingProtocolToWorkerMap.put(rps, tmp);
        
        workerService.submit(worker);
    }
    
    

}
