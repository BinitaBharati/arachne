package com.github.binitabharati.arachne.test.network2.sanity;

import java.util.List;

import com.github.binitabharati.arachne.routing.rip.model.AbstractRouteEntry;

public class JsonRouteEntry {
	private List<JsonRouteEntry2> entries;
    
	public JsonRouteEntry(List<JsonRouteEntry2> entries) {
		this.entries = entries;
	}
		
	public List<JsonRouteEntry2> getEntries() {
		return entries;
	}


	public void setEntries(List<JsonRouteEntry2> entries) {
		this.entries = entries;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.entries.toString();
	}
	
	public class JsonRouteEntry2 {
		private String mcIp;
		private List<AbstractRouteEntry> routeEntryList;
		
		public JsonRouteEntry2(String mcIp, List<AbstractRouteEntry> routeEntryList) {
			this.mcIp = mcIp;
			this.routeEntryList = routeEntryList;
		}
		
				
		public String getMcIp() {
			return mcIp;
		}


		public void setMcIp(String mcIp) {
			this.mcIp = mcIp;
		}


		public List<AbstractRouteEntry> getRouteEntryList() {
			return routeEntryList;
		}


		public void setRouteEntryList(List<AbstractRouteEntry> routeEntryList) {
			this.routeEntryList = routeEntryList;
		}


		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "mcIp = "+mcIp + ", routeEntryList = "+routeEntryList;
		}
	}

}
