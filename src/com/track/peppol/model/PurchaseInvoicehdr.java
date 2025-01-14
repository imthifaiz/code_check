package com.track.peppol.model;

import java.util.List;

public class PurchaseInvoicehdr {
	public int id;
    public String purchase_invoice_uuid;
    public String purchase_invoice_number;
    public String client_creditor_number;
    public String creditor_number;
    public int client_number;
    public int platform_id;
    public double amount;
    public String currency_code;
    public String purchase_invoice_date;
    public String purchase_invoice_due_date;
    public String client_creditor_purchase_invoice_number;
    public String creditor_currency_code;
    public String status;
    public String accounting_status;
    public String source;
    public double open_balance;
    public double amount_paid;
    public String original_invoice_number;
    public String original_invoice_date;
    public String delivery_channel;
    public String po_number;
    public String type;
    public int client_id;
    public String client_currency_code;
    public int creditor_id;
    public double exclusive_amount;
    public String amount_in_creditor_currency;
    public String amount_in_client_currency;
    public String vat_amount_in_creditor_currency;
    public String vat_amount_in_client_currency;
    public String exclusive_amount_in_creditor_currency;
    public String exclusive_amount_in_client_currency;
    public String payment_date;
    public String last_payment_date;
    public String payment_status;
    public String payment_reference;
    public String created_at;
    public String last_updated_at;
    public List<PurchaseInvoiceLine> invoice_lines;
    public String original_document_type;
    public String supply_date;
    public String payment_term;
    public double vat_amount;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPurchase_invoice_uuid() {
		return purchase_invoice_uuid;
	}
	public void setPurchase_invoice_uuid(String purchase_invoice_uuid) {
		this.purchase_invoice_uuid = purchase_invoice_uuid;
	}
	public String getPurchase_invoice_number() {
		return purchase_invoice_number;
	}
	public void setPurchase_invoice_number(String purchase_invoice_number) {
		this.purchase_invoice_number = purchase_invoice_number;
	}
	public String getClient_creditor_number() {
		return client_creditor_number;
	}
	public void setClient_creditor_number(String client_creditor_number) {
		this.client_creditor_number = client_creditor_number;
	}
	public String getCreditor_number() {
		return creditor_number;
	}
	public void setCreditor_number(String creditor_number) {
		this.creditor_number = creditor_number;
	}
	public int getClient_number() {
		return client_number;
	}
	public void setClient_number(int client_number) {
		this.client_number = client_number;
	}
	public int getPlatform_id() {
		return platform_id;
	}
	public void setPlatform_id(int platform_id) {
		this.platform_id = platform_id;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getCurrency_code() {
		return currency_code;
	}
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
	public String getPurchase_invoice_date() {
		return purchase_invoice_date;
	}
	public void setPurchase_invoice_date(String purchase_invoice_date) {
		this.purchase_invoice_date = purchase_invoice_date;
	}
	public String getPurchase_invoice_due_date() {
		return purchase_invoice_due_date;
	}
	public void setPurchase_invoice_due_date(String purchase_invoice_due_date) {
		this.purchase_invoice_due_date = purchase_invoice_due_date;
	}
	public String getClient_creditor_purchase_invoice_number() {
		return client_creditor_purchase_invoice_number;
	}
	public void setClient_creditor_purchase_invoice_number(String client_creditor_purchase_invoice_number) {
		this.client_creditor_purchase_invoice_number = client_creditor_purchase_invoice_number;
	}
	public String getCreditor_currency_code() {
		return creditor_currency_code;
	}
	public void setCreditor_currency_code(String creditor_currency_code) {
		this.creditor_currency_code = creditor_currency_code;
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
	public double getAmount_paid() {
		return amount_paid;
	}
	public void setAmount_paid(double amount_paid) {
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
	public String getPo_number() {
		return po_number;
	}
	public void setPo_number(String po_number) {
		this.po_number = po_number;
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
	public String getClient_currency_code() {
		return client_currency_code;
	}
	public void setClient_currency_code(String client_currency_code) {
		this.client_currency_code = client_currency_code;
	}
	public int getCreditor_id() {
		return creditor_id;
	}
	public void setCreditor_id(int creditor_id) {
		this.creditor_id = creditor_id;
	}
	public double getExclusive_amount() {
		return exclusive_amount;
	}
	public void setExclusive_amount(double exclusive_amount) {
		this.exclusive_amount = exclusive_amount;
	}
	public String getAmount_in_creditor_currency() {
		return amount_in_creditor_currency;
	}
	public void setAmount_in_creditor_currency(String amount_in_creditor_currency) {
		this.amount_in_creditor_currency = amount_in_creditor_currency;
	}
	public String getAmount_in_client_currency() {
		return amount_in_client_currency;
	}
	public void setAmount_in_client_currency(String amount_in_client_currency) {
		this.amount_in_client_currency = amount_in_client_currency;
	}
	public String getVat_amount_in_creditor_currency() {
		return vat_amount_in_creditor_currency;
	}
	public void setVat_amount_in_creditor_currency(String vat_amount_in_creditor_currency) {
		this.vat_amount_in_creditor_currency = vat_amount_in_creditor_currency;
	}
	public String getVat_amount_in_client_currency() {
		return vat_amount_in_client_currency;
	}
	public void setVat_amount_in_client_currency(String vat_amount_in_client_currency) {
		this.vat_amount_in_client_currency = vat_amount_in_client_currency;
	}
	public String getExclusive_amount_in_creditor_currency() {
		return exclusive_amount_in_creditor_currency;
	}
	public void setExclusive_amount_in_creditor_currency(String exclusive_amount_in_creditor_currency) {
		this.exclusive_amount_in_creditor_currency = exclusive_amount_in_creditor_currency;
	}
	public String getExclusive_amount_in_client_currency() {
		return exclusive_amount_in_client_currency;
	}
	public void setExclusive_amount_in_client_currency(String exclusive_amount_in_client_currency) {
		this.exclusive_amount_in_client_currency = exclusive_amount_in_client_currency;
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
	public List<PurchaseInvoiceLine> getInvoice_lines() {
		return invoice_lines;
	}
	public void setInvoice_lines(List<PurchaseInvoiceLine> invoice_lines) {
		this.invoice_lines = invoice_lines;
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
		return "PurchaseInvoicehdr [id=" + id + ", purchase_invoice_uuid=" + purchase_invoice_uuid
				+ ", purchase_invoice_number=" + purchase_invoice_number + ", client_creditor_number="
				+ client_creditor_number + ", creditor_number=" + creditor_number + ", client_number=" + client_number
				+ ", platform_id=" + platform_id + ", amount=" + amount + ", currency_code=" + currency_code
				+ ", purchase_invoice_date=" + purchase_invoice_date + ", purchase_invoice_due_date="
				+ purchase_invoice_due_date + ", client_creditor_purchase_invoice_number="
				+ client_creditor_purchase_invoice_number + ", creditor_currency_code=" + creditor_currency_code
				+ ", status=" + status + ", accounting_status=" + accounting_status + ", source=" + source
				+ ", open_balance=" + open_balance + ", amount_paid=" + amount_paid + ", original_invoice_number="
				+ original_invoice_number + ", original_invoice_date=" + original_invoice_date + ", delivery_channel="
				+ delivery_channel + ", po_number=" + po_number + ", type=" + type + ", client_id=" + client_id
				+ ", client_currency_code=" + client_currency_code + ", creditor_id=" + creditor_id
				+ ", exclusive_amount=" + exclusive_amount + ", amount_in_creditor_currency="
				+ amount_in_creditor_currency + ", amount_in_client_currency=" + amount_in_client_currency
				+ ", vat_amount_in_creditor_currency=" + vat_amount_in_creditor_currency
				+ ", vat_amount_in_client_currency=" + vat_amount_in_client_currency
				+ ", exclusive_amount_in_creditor_currency=" + exclusive_amount_in_creditor_currency
				+ ", exclusive_amount_in_client_currency=" + exclusive_amount_in_client_currency + ", payment_date="
				+ payment_date + ", last_payment_date=" + last_payment_date + ", payment_status=" + payment_status
				+ ", payment_reference=" + payment_reference + ", created_at=" + created_at + ", last_updated_at="
				+ last_updated_at + ", invoice_lines=" + invoice_lines + ", original_document_type="
				+ original_document_type + ", supply_date=" + supply_date + ", payment_term=" + payment_term
				+ ", vat_amount=" + vat_amount + "]";
	}
    
    
}
