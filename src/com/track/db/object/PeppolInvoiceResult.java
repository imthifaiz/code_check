package com.track.db.object;

public class PeppolInvoiceResult {
	
	private String status;
	
	private String docId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	@Override
	public String toString() {
		return "PeppolInvoiceResult [status=" + status + ", docId=" + docId + "]";
	}
	
	
}
