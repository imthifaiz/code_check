package com.track.db.object;

public class PeppolInboundData {

	private String event;
	private String docId;
	private String receivedAt;
	private String invoiceFileUrl;
	private String evidenceFileUrl;
	private String expiresAt;
	
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getReceivedAt() {
		return receivedAt;
	}
	public void setReceivedAt(String receivedAt) {
		this.receivedAt = receivedAt;
	}
	public String getInvoiceFileUrl() {
		return invoiceFileUrl;
	}
	public void setInvoiceFileUrl(String invoiceFileUrl) {
		this.invoiceFileUrl = invoiceFileUrl;
	}
	public String getEvidenceFileUrl() {
		return evidenceFileUrl;
	}
	public void setEvidenceFileUrl(String evidenceFileUrl) {
		this.evidenceFileUrl = evidenceFileUrl;
	}
	public String getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
	}
	
	@Override
	public String toString() {
		return "{\"event\":\"" + event + "\", \"docId\":\"" + docId + "\", \"receivedAt\":\"" + receivedAt
				+ "\", \"invoiceFileUrl\":\"" + invoiceFileUrl + "\", \"evidenceFileUrl\":\"" + evidenceFileUrl + "\", \"expiresAt\":\""
				+ expiresAt + "\"}";
	}
	
	
}
