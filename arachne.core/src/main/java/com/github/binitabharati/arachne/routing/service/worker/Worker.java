package com.github.binitabharati.arachne.routing.service.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author binita.bharati@gmail.com
 * Routing process thread workers.
 *
 */

public abstract class Worker implements Runnable, Thread.UncaughtExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(Worker.class);
    protected enum WorkerType {
        routeListener,
        routeSender,
        routeProcessor
    }
    
    protected WorkerType workerType;
    
    protected int id;
    
    /**
     * Restarts any worker thread on Exception.
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // TODO Auto-generated method stub
        logger.debug("uncaughtException: entered for "+t.getName());
        logger.error("uncaughtException: exception "+e.getMessage(), e);
        t.start();
    }
    

}