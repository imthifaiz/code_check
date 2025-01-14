package com.track.peppol.model;

import java.util.List;

public class PeppolSalesInvoice {
	public int id;
    public String sales_invoice_uuid;
    public String sales_invoice_number;
    public String client_uuid;
    public String client_debtor_number;
    public String debtor_uuid;
    public String debtor_number;
    public int client_number;
    public double amount;
    public int platform_id;
    public String currency_code;
    public String debtor_currency_code;
    public String sales_invoice_date;
    public String sales_invoice_due_date;
    public String client_debtor_invoice_number;
    public String amount_in_debtor_currency;
    public String amount_in_client_currency;
    public String vat_amount_in_debtor_currency;
    public String vat_amount_in_client_currency;
    public String client_currency_code;
    public String status;
    public String accounting_status;
    public String source;
    public double open_balance;
    public int amount_paid;
    public String original_invoice_number;
    public String original_invoice_date;
    public String delivery_channel;
    public String type;
    public int client_id;
    public String payment_date;
    public String last_payment_date;
    public String payment_status;
    public String payment_reference;
    public String filename;
    public int debtor_id;
    public String vat_code;
    public double exclusive_amount;
    public String exclusive_amount_in_debtor_currency;
    public String exclusive_amount_in_client_currency;
    public String status_change_date;
    public boolean disputed;
    public String created_at;
    public String last_updated_at;
    public List<PeppolInvoiceLine> invoice_lines;
    public int total_discounts;
    public String original_invoices;
    public String po_number;
    public String buyer_reference;
    public String customer_reference;
    public String supplier_reference;
    public String contract_number;
    public String original_document_type;
    public String supply_date;
    public String comments;
    public String payment_term;
    public double vat_amount;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSales_invoice_uuid() {
		return sales_invoice_uuid;
	}
	public void setSales_invoice_uuid(String sales_invoice_uuid) {
		this.sales_invoice_uuid = sales_invoice_uuid;
	}
	public String getSales_invoice_number() {
		return sales_invoice_number;
	}
	public void setSales_invoice_number(String sales_invoice_number) {
		this.sales_invoice_number = sales_invoice_number;
	}
	public String getClient_uuid() {
		return client_uuid;
	}
	public void setClient_uuid(String client_uuid) {
		this.client_uuid = client_uuid;
	}
	public String getClient_debtor_number() {
		return client_debtor_number;
	}
	public void setClient_debtor_number(String client_debtor_number) {
		this.client_debtor_number = client_debtor_number;
	}
	public String getDebtor_uuid() {
		return debtor_uuid;
	}
	public void setDebtor_uuid(String debtor_uuid) {
		this.debtor_uuid = debtor_uuid;
	}
	public String getDebtor_number() {
		return debtor_number;
	}
	public void setDebtor_number(String debtor_number) {
		this.debtor_number = debtor_number;
	}
	public int getClient_number() {
		return client_number;
	}
	public void setClient_number(int client_number) {
		this.client_number = client_number;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getPlatform_id() {
		return platform_id;
	}
	public void setPlatform_id(int platform_id) {
		this.platform_id = platform_id;
	}
	public String getCurrency_code() {
		return currency_code;
	}
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
	public String getDebtor_currency_code() {
		return debtor_currency_code;
	}
	public void setDebtor_currency_code(String debtor_currency_code) {
		this.debtor_currency_code = debtor_currency_code;
	}
	public String getSales_invoice_date() {
		return sales_invoice_date;
	}
	public void setSales_invoice_date(String sales_invoice_date) {
		this.sales_invoice_date = sales_invoice_date;
	}
	public String getSales_invoice_due_date() {
		return sales_invoice_due_date;
	}
	public void setSales_invoice_due_date(String sales_invoice_due_date) {
		this.sales_invoice_due_date = sales_invoice_due_date;
	}
	public String getClient_debtor_invoice_number() {
		return client_debtor_invoice_number;
	}
	public void setClient_debtor_invoice_number(String client_debtor_invoice_number) {
		this.client_debtor_invoice_number = client_debtor_invoice_number;
	}
	public String getAmount_in_debtor_currency() {
		return amount_in_debtor_currency;
	}
	public void setAmount_in_debtor_currency(String amount_in_debtor_currency) {
		this.amount_in_debtor_currency = amount_in_debtor_currency;
	}
	public String getAmount_in_client_currency() {
		return amount_in_client_currency;
	}
	public void setAmount_in_client_currency(String amount_in_client_currency) {
		this.amount_in_client_currency = amount_in_client_currency;
	}
	public String getVat_amount_in_debtor_currency() {
		return vat_amount_in_debtor_currency;
	}
	public void setVat_amount_in_debtor_currency(String vat_amount_in_debtor_currency) {
		this.vat_amount_in_debtor_currency = vat_amount_in_debtor_currency;
	}
	public String getVat_amount_in_client_currency() {
		return vat_amount_in_client_currency;
	}
	public void setVat_amount_in_client_currency(String vat_amount_in_client_currency) {
		this.vat_amount_in_client_currency = vat_amount_in_client_currency;
	}
	public String getClient_currency_code() {
		return client_currency_code;
	}
	public void setClient_currency_code(String client_currency_code) {
		this.client_currency_code = client_currency_code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAccounting_status() {
		return accounting_status;
	}
	public void setAccounting_status(String accounting_status) {
		this.accounting_status = accounting_status;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public double getOpen_balance() {
		return open_balance;
	}
	public void setOpen_balance(double open_balance) {
		this.open_balance = open_balance;
	}
	public int getAmount_paid() {
		return amount_paid;
	}
	public void setAmount_paid(int amount_paid) {
		this.amount_paid = amount_paid;
	}
	public String getOriginal_invoice_number() {
		return original_invoice_number;
	}
	public void setOriginal_invoice_number(String original_invoice_number) {
		this.original_invoice_number = original_invoice_number;
	}
	public String getOriginal_invoice_date() {
		return original_invoice_date;
	}
	public void setOriginal_invoice_date(String original_invoice_date) {
		this.original_invoice_date = original_invoice_date;
	}
	public String getDelivery_channel() {
		return delivery_channel;
	}
	public void setDelivery_channel(String delivery_channel) {
		this.delivery_channel = delivery_channel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getClient_id() {
		return client_id;
	}
	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}
	public String getPayment_date() {
		return payment_date;
	}
	public void setPayment_date(String payment_date) {
		this.payment_date = payment_date;
	}
	public String getLast_payment_date() {
		return last_payment_date;
	}
	public void setLast_payment_date(String last_payment_date) {
		this.last_payment_date = last_payment_date;
	}
	public String getPayment_status() {
		return payment_status;
	}
	public void setPayment_status(String payment_status) {
		this.payment_status = payment_status;
	}
	public String getPayment_reference() {
		return payment_reference;
	}
	public void setPayment_reference(String payment_reference) {
		this.payment_reference = payment_reference;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getDebtor_id() {
		return debtor_id;
	}
	public void setDebtor_id(int debtor_id) {
		this.debtor_id = debtor_id;
	}
	public String getVat_code() {
		return vat_code;
	}
	public void setVat_code(String vat_code) {
		this.vat_code = vat_code;
	}
	public double getExclusive_amount() {
		return exclusive_amount;
	}
	public void setExclusive_amount(double exclusive_amount) {
		this.exclusive_amount = exclusive_amount;
	}
	public String getExclusive_amount_in_debtor_currency() {
		return exclusive_amount_in_debtor_currency;
	}
	public void setExclusive_amount_in_debtor_currency(String exclusive_amount_in_debtor_currency) {
		this.exclusive_amount_in_debtor_currency = exclusive_amount_in_debtor_currency;
	}
	public String getExclusive_amount_in_client_currency() {
		return exclusive_amount_in_client_currency;
	}
	public void setExclusive_amount_in_client_currency(String exclusive_amount_in_client_currency) {
		this.exclusive_amount_in_client_currency = exclusive_amount_in_client_currency;
	}
	public String getStatus_change_date() {
		return status_change_date;
	}
	public void setStatus_change_date(String status_change_date) {
		this.status_change_date = status_change_date;
	}
	public boolean isDisputed() {
		return disputed;
	}
	public void setDisputed(boolean disputed) {
		this.disputed = disputed;
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
	public List<PeppolInvoiceLine> getInvoice_lines() {
		return invoice_lines;
	}
	public void setInvoice_lines(List<PeppolInvoiceLine> invoice_lines) {
		this.invoice_lines = invoice_lines;
	}
	public int getTotal_discounts() {
		return total_discounts;
	}
	public void setTotal_discounts(int total_discounts) {
		this.total_discounts = total_discounts;
	}
	public String getOriginal_invoices() {
		return original_invoices;
	}
	public void setOriginal_invoices(String original_invoices) {
		this.original_invoices = original_invoices;
	}
	public String getPo_number() {
		return po_number;
	}
	public void setPo_number(String po_number) {
		this.po_number = po_number;
	}
	public String getBuyer_reference() {
		return buyer_reference;
	}
	public void setBuyer_reference(String buyer_reference) {
		this.buyer_reference = buyer_reference;
	}
	public String getCustomer_reference() {
		return customer_reference;
	}
	public void setCustomer_reference(String customer_reference) {
		this.customer_reference = customer_reference;
	}
	public String getSupplier_reference() {
		return supplier_reference;
	}
	public void setSupplier_reference(String supplier_reference) {
		this.supplier_reference = supplier_reference;
	}
	public String getContract_number() {
		return contract_number;
	}
	public void setContract_number(String contract_number) {
		this.contract_number = contract_number;
	}
	public String getOriginal_document_type() {
		return original_document_type;
	}
	public void setOriginal_document_type(String original_document_type) {
		this.original_document_type = original_document_type;
	}
	public String getSupply_date() {
		return supply_date;
	}
	public void setSupply_date(String supply_date) {
		this.supply_date = supply_date;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getPayment_term() {
		return payment_term;
	}
	public void setPayment_term(String payment_term) {
		this.payment_term = payment_term;
	}
	public double getVat_amount() {
		return vat_amount;
	}
	public void setVat_amount(double vat_amount) {
		this.vat_amount = vat_amount;
	}
	@Override
	public String toString() {
		return "PeppolSalesInvoice [id=" + id + ", sales_invoice_uuid=" + sales_invoice_uuid + ", sales_invoice_number="
				+ sales_invoice_number + ", client_uuid=" + client_uuid + ", client_debtor_number="
				+ client_debtor_number + ", debtor_uuid=" + debtor_uuid + ", debtor_number=" + debtor_number
				+ ", client_number=" + client_number + ", amount=" + amount + ", platform_id=" + platform_id
				+ ", currency_code=" + currency_code + ", debtor_currency_code=" + debtor_currency_code
				+ ", sales_invoice_date=" + sales_invoice_date + ", sales_invoice_due_date=" + sales_invoice_due_date
				+ ", client_debtor_invoice_number=" + client_debtor_invoice_number + ", amount_in_debtor_currency="
				+ amount_in_debtor_currency + ", amount_in_client_currency=" + amount_in_client_currency
				+ ", vat_amount_in_debtor_currency=" + vat_amount_in_debtor_currency
				+ ", vat_amount_in_client_currency=" + vat_amount_in_client_currency + ", client_currency_code="
				+ client_currency_code + ", status=" + status + ", accounting_status=" + accounting_status + ", source="
				+ source + ", open_balance=" + open_balance + ", amount_paid=" + amount_paid
				+ ", original_invoice_number=" + original_invoice_number + ", original_invoice_date="
				+ original_invoice_date + ", delivery_channel=" + delivery_channel + ", type=" + type + ", client_id="
				+ client_id + ", payment_date=" + payment_date + ", last_payment_date=" + last_payment_date
				+ ", payment_status=" + payment_status + ", payment_reference=" + payment_reference + ", filename="
				+ filename + ", debtor_id=" + debtor_id + ", vat_code=" + vat_code + ", exclusive_amount="
				+ exclusive_amount + ", exclusive_amount_in_debtor_currency=" + exclusive_amount_in_debtor_currency
				+ ", exclusive_amount_in_client_currency=" + exclusive_amount_in_client_currency
				+ ", status_change_date=" + status_change_date + ", disputed=" + disputed + ", created_at=" + created_at
				+ ", last_updated_at=" + last_updated_at + ", invoice_lines=" + invoice_lines + ", total_discounts="
				+ total_discounts + ", original_invoices=" + original_invoices + ", po_number=" + po_number
				+ ", buyer_reference=" + buyer_reference + ", customer_reference=" + customer_reference
				+ ", supplier_reference=" + supplier_reference + ", contract_number=" + contract_number
				+ ", original_document_type=" + original_document_type + ", supply_date=" + supply_date + ", comments="
				+ comments + ", payment_term=" + payment_term + ", vat_amount=" + vat_amount + "]";
	}
    
    
}
