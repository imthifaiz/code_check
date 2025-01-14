package com.track.peppol.model;

public class PeppolInvoiceLine {
	public int id;
    public String service_name;
    public String service_description;
    public int service_quantity;
    public double service_price;
    public int service_vat;
    public double service_subtotal;
    public double service_discount_perc;
    public double service_discount;
    public String service_unit;
    public String po_line_number;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getService_name() {
		return service_name;
	}
	public void setService_name(String service_name) {
		this.service_name = service_name;
	}
	public String getService_description() {
		return service_description;
	}
	public void setService_description(String service_description) {
		this.service_description = service_description;
	}
	public int getService_quantity() {
		return service_quantity;
	}
	public void setService_quantity(int service_quantity) {
		this.service_quantity = service_quantity;
	}
	public double getService_price() {
		return service_price;
	}
	public void setService_price(double service_price) {
		this.service_price = service_price;
	}
	public int getService_vat() {
		return service_vat;
	}
	public void setService_vat(int service_vat) {
		this.service_vat = service_vat;
	}
	public double getService_subtotal() {
		return service_subtotal;
	}
	public void setService_subtotal(double service_subtotal) {
		this.service_subtotal = service_subtotal;
	}
	public double getService_discount_perc() {
		return service_discount_perc;
	}
	public void setService_discount_perc(double service_discount_perc) {
		this.service_discount_perc = service_discount_perc;
	}
	public double getService_discount() {
		return service_discount;
	}
	public void setService_discount(double service_discount) {
		this.service_discount = service_discount;
	}
	public String getService_unit() {
		return service_unit;
	}
	public void setService_unit(String service_unit) {
		this.service_unit = service_unit;
	}
	public String getPo_line_number() {
		return po_line_number;
	}
	public void setPo_line_number(String po_line_number) {
		this.po_line_number = po_line_number;
	}
	@Override
	public String toString() {
		return "PeppolInvoiceLine [id=" + id + ", service_name=" + service_name + ", service_description="
				+ service_description + ", service_quantity=" + service_quantity + ", service_price=" + service_price
				+ ", service_vat=" + service_vat + ", service_subtotal=" + service_subtotal + ", service_discount_perc="
				+ service_discount_perc + ", service_discount=" + service_discount + ", service_unit=" + service_unit
				+ ", po_line_number=" + po_line_number + "]";
	}
    
    
}
