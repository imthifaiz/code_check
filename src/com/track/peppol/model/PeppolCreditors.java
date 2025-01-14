package com.track.peppol.model;

import java.util.List;

public class PeppolCreditors {
	public int id;
    public String creditor_uuid;
    public int client_id;
    public String creditor_number;
    public String client_creditor_number;
    public int platform_id;
    public int client_number;
    public String name;
    public String legal_entity_trn;
    public String legal_entity_affiliate;
    public String address;
    public String zip_code;
    public String state;
    public String city;
    public String country_code;
    public String language_code;
    public String client_currency_code;
    public String creditor_type;
    public String currency_code;
    public String vat_code;
    public List<Object> bank_account;
    public String email;
    public String phone;
    public String creditor_reference;
    public String default_payment_after;
    public boolean trusted;
    public String peppol_id;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCreditor_uuid() {
		return creditor_uuid;
	}
	public void setCreditor_uuid(String creditor_uuid) {
		this.creditor_uuid = creditor_uuid;
	}
	public int getClient_id() {
		return client_id;
	}
	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}
	public String getCreditor_number() {
		return creditor_number;
	}
	public void setCreditor_number(String creditor_number) {
		this.creditor_number = creditor_number;
	}
	public String getClient_creditor_number() {
		return client_creditor_number;
	}
	public void setClient_creditor_number(String client_creditor_number) {
		this.client_creditor_number = client_creditor_number;
	}
	public int getPlatform_id() {
		return platform_id;
	}
	public void setPlatform_id(int platform_id) {
		this.platform_id = platform_id;
	}
	public int getClient_number() {
		return client_number;
	}
	public void setClient_number(int client_number) {
		this.client_number = client_number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLegal_entity_trn() {
		return legal_entity_trn;
	}
	public void setLegal_entity_trn(String legal_entity_trn) {
		this.legal_entity_trn = legal_entity_trn;
	}
	public String getLegal_entity_affiliate() {
		return legal_entity_affiliate;
	}
	public void setLegal_entity_affiliate(String legal_entity_affiliate) {
		this.legal_entity_affiliate = legal_entity_affiliate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip_code() {
		return zip_code;
	}
	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getLanguage_code() {
		return language_code;
	}
	public void setLanguage_code(String language_code) {
		this.language_code = language_code;
	}
	public String getClient_currency_code() {
		return client_currency_code;
	}
	public void setClient_currency_code(String client_currency_code) {
		this.client_currency_code = client_currency_code;
	}
	public String getCreditor_type() {
		return creditor_type;
	}
	public void setCreditor_type(String creditor_type) {
		this.creditor_type = creditor_type;
	}
	public String getCurrency_code() {
		return currency_code;
	}
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
	public String getVat_code() {
		return vat_code;
	}
	public void setVat_code(String vat_code) {
		this.vat_code = vat_code;
	}
	public List<Object> getBank_account() {
		return bank_account;
	}
	public void setBank_account(List<Object> bank_account) {
		this.bank_account = bank_account;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCreditor_reference() {
		return creditor_reference;
	}
	public void setCreditor_reference(String creditor_reference) {
		this.creditor_reference = creditor_reference;
	}
	public String getDefault_payment_after() {
		return default_payment_after;
	}
	public void setDefault_payment_after(String default_payment_after) {
		this.default_payment_after = default_payment_after;
	}
	public boolean isTrusted() {
		return trusted;
	}
	public void setTrusted(boolean trusted) {
		this.trusted = trusted;
	}
	public String getPeppol_id() {
		return peppol_id;
	}
	public void setPeppol_id(String peppol_id) {
		this.peppol_id = peppol_id;
	}
	
	@Override
	public String toString() {
		return "PeppolCreditors [id=" + id + ", creditor_uuid=" + creditor_uuid + ", client_id=" + client_id
				+ ", creditor_number=" + creditor_number + ", client_creditor_number=" + client_creditor_number
				+ ", platform_id=" + platform_id + ", client_number=" + client_number + ", name=" + name
				+ ", legal_entity_trn=" + legal_entity_trn + ", legal_entity_affiliate=" + legal_entity_affiliate
				+ ", address=" + address + ", zip_code=" + zip_code + ", state=" + state + ", city=" + city
				+ ", country_code=" + country_code + ", language_code=" + language_code + ", client_currency_code="
				+ client_currency_code + ", creditor_type=" + creditor_type + ", currency_code=" + currency_code
				+ ", vat_code=" + vat_code + ", bank_account=" + bank_account + ", email=" + email + ", phone=" + phone
				+ ", creditor_reference=" + creditor_reference + ", default_payment_after=" + default_payment_after
				+ ", trusted=" + trusted + ", peppol_id=" + peppol_id + "]";
	}
    
    
}
