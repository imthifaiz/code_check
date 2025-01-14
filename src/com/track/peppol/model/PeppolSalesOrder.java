package com.track.peppol.model;

import java.util.List;

public class PeppolSalesOrder {
	 public String created_by;
	    public String last_updated_by;
	    public int id;
	    public String sales_order_uuid;
	    public String sales_order_number;
	    public String po_number;
	    public int debtor_id;
	    public int client_id;
	    public String sales_order_date;
	    public String expected_delivery_date;
	    public String created_at;
	    public String last_updated_at;
	    public String status;
	    public List<PeppolSalesOrderLineItem> sales_order_lines;
	    public double amount;
		public String getCreated_by() {
			return created_by;
		}
		public void setCreated_by(String created_by) {
			this.created_by = created_by;
		}
		public String getLast_updated_by() {
			return last_updated_by;
		}
		public void setLast_updated_by(String last_updated_by) {
			this.last_updated_by = last_updated_by;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getSales_order_uuid() {
			return sales_order_uuid;
		}
		public void setSales_order_uuid(String sales_order_uuid) {
			this.sales_order_uuid = sales_order_uuid;
		}
		public String getSales_order_number() {
			return sales_order_number;
		}
		public void setSales_order_number(String sales_order_number) {
			this.sales_order_number = sales_order_number;
		}
		public String getPo_number() {
			return po_number;
		}
		public void setPo_number(String po_number) {
			this.po_number = po_number;
		}
		public int getDebtor_id() {
			return debtor_id;
		}
		public void setDebtor_id(int debtor_id) {
			this.debtor_id = debtor_id;
		}
		public int getClient_id() {
			return client_id;
		}
		public void setClient_id(int client_id) {
			this.client_id = client_id;
		}
		public String getSales_order_date() {
			return sales_order_date;
		}
		public void setSales_order_date(String sales_order_date) {
			this.sales_order_date = sales_order_date;
		}
		public String getExpected_delivery_date() {
			return expected_delivery_date;
		}
		public void setExpected_delivery_date(String expected_delivery_date) {
			this.expected_delivery_date = expected_delivery_date;
		}
		public String getCreated_at() {
			return created_at;
		}
		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}
		public String getLast_updated_at() {
			return last_updated_at;
		}
		public void setLast_updated_at(String last_updated_at) {
			this.last_updated_at = last_updated_at;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public List<PeppolSalesOrderLineItem> getSales_order_lines() {
			return sales_order_lines;
		}
		public void setSales_order_lines(List<PeppolSalesOrderLineItem> sales_order_lines) {
			this.sales_order_lines = sales_order_lines;
		}
		public double getAmount() {
			return amount;
		}
		public void setAmount(double amount) {
			this.amount = amount;
		}
		@Override
		public String toString() {
			return "PeppolSalesOrder [created_by=" + created_by + ", last_updated_by=" + last_updated_by + ", id=" + id
					+ ", sales_order_uuid=" + sales_order_uuid + ", sales_order_number=" + sales_order_number
					+ ", po_number=" + po_number + ", debtor_id=" + debtor_id + ", client_id=" + client_id
					+ ", sales_order_date=" + sales_order_date + ", expected_delivery_date=" + expected_delivery_date
					+ ", created_at=" + created_at + ", last_updated_at=" + last_updated_at + ", status=" + status
					+ ", sales_order_lines=" + sales_order_lines + ", amount=" + amount + "]";
		}
	    
	    
}
