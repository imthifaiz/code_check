package com.track.db.object;

import java.util.List;

public class InvoicePojo {
	
	private InvoiceHdr invoicehdr;
	
	private List<InvoiceDet> invoicedet;

	public InvoiceHdr getInvoicehdr() {
		return invoicehdr;
	}

	public void setInvoicehdr(InvoiceHdr invoicehdr) {
		this.invoicehdr = invoicehdr;
	}

	public List<InvoiceDet> getInvoicedet() {
		return invoicedet;
	}

	public void setInvoicedet(List<InvoiceDet> invoicedet) {
		this.invoicedet = invoicedet;
	}

	@Override
	public String toString() {
		return "InvoicePojo [invoicehdr=" + invoicehdr + ", invoicedet=" + invoicedet + "]";
	}

	
	

}
