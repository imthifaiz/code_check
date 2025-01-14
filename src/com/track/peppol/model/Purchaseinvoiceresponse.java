package com.track.peppol.model;

public class Purchaseinvoiceresponse {
	public String purchase_invoices_uuid;

	public String getPurchase_invoices_uuid() {
		return purchase_invoices_uuid;
	}

	public void setPurchase_invoices_uuid(String purchase_invoices_uuid) {
		this.purchase_invoices_uuid = purchase_invoices_uuid;
	}

	@Override
	public String toString() {
		return "Purchaseinvoiceresponse [purchase_invoices_uuid=" + purchase_invoices_uuid + "]";
	}
	
	
}
