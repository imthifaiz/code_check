package com.track.peppol.model;

public class Invoiceeventsline {
	public String invoice_uuid;
    public String key;
    public String type;
    public String time;
    public String message;
	public String getInvoice_uuid() {
		return invoice_uuid;
	}
	public void setInvoice_uuid(String invoice_uuid) {
		this.invoice_uuid = invoice_uuid;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "{\"invoice_uuid\":\"" + invoice_uuid + "\", \"key\":\"" + key + "\", \"type\":\"" + type + "\", \"time\":\"" + time
				+ "\", \"message\":\"" + message + "\"}";
	}
    
    
}
