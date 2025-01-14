package com.track.peppol.model;

import java.util.List;

public class Invoiceevents {
	public int size;
    public int total;
    public int page;
    public List<Invoiceeventsline> results;
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public List<Invoiceeventsline> getResults() {
		return results;
	}
	public void setResults(List<Invoiceeventsline> results) {
		this.results = results;
	}
	@Override
	public String toString() {
		return "Invoiceevents [size=" + size + ", total=" + total + ", page=" + page + ", results=" + results + "]";
	}
    
    
}
