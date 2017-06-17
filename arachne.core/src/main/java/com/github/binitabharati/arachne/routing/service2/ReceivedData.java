package com.github.binitabharati.arachne.routing.service2;

public class ReceivedData {
	
	private String sender;
	private byte[] data;
	
	public ReceivedData(String sender, byte[] data){
		this.sender = sender;
		this.data = data;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	

}
