package com.github.binitabharati.arachne.nwking.model;

import java.util.ArrayList;
import java.util.List;

public class RouteTable {
    
    private List<Object> routeEntries;
    
    public RouteTable(){
        this.routeEntries = new ArrayList<Object>();
    }

    public List<Object> getRouteEntries() {
        return routeEntries;
    }

    public void setRouteEntries(List<Object> routeEntries) {
        this.routeEntries = routeEntries;
    }
    
    public void addRoute(Object routeEntry){
        this.routeEntries.add(routeEntry);
    }
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return routeEntries + "";
    }

}
