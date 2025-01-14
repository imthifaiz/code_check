package com.track.peppol.model;

public class PeppolSalesOrderLineItem {
	public int id;
    public String sales_order_line_uuid;
    public String service_name;
    public String service_description;
    public int service_quantity;
    public double service_price;
    public String service_vat;
    public String service_unit_code;
    public String service_unit_description;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSales_order_line_uuid() {
		return sales_order_line_uuid;
	}
	public void setSales_order_line_uuid(String sales_order_line_uuid) {
		this.sales_order_line_uuid = sales_order_line_uuid;
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
	public String getService_vat() {
		return service_vat;
	}
	public void setService_vat(String service_vat) {
		this.service_vat = service_vat;
	}
	public String getService_unit_code() {
		return service_unit_code;
	}
	public void setService_unit_code(String service_unit_code) {
		this.service_unit_code = service_unit_code;
	}
	public String getService_unit_description() {
		return service_unit_description;
	}
	public void setService_unit_description(String service_unit_description) {
		this.service_unit_description = service_unit_description;
	}
	@Override
	public String toString() {
		return "PeppolSalesOrderLineItem [id=" + id + ", sales_order_line_uuid=" + sales_order_line_uuid
				+ ", service_name=" + service_name + ", service_description=" + service_description
				+ ", service_quantity=" + service_quantity + ", service_price=" + service_price + ", service_vat="
				+ service_vat + ", service_unit_code=" + service_unit_code + ", service_unit_description="
				+ service_unit_description + "]";
	}
    
    
}
