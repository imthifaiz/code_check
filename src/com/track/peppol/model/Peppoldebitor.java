package com.track.peppol.model;

import java.util.ArrayList;

public class Peppoldebitor {
	
	 	public int id;
	    public String debtor_uuid;
	    public String debtor_number;
	    public String client_debtor_number;
	    public int platform_id;
	    public int client_id;
	    public int client_number;
	    public String name;
	    public String legal_entity_trn;
	    public String legal_entity_affiliate;
	    public String debtor_type;
	    public String currency_code;
	    public String client_currency_code;
	    public String address;
	    public String zip_code;
	    public String city;
	    public String country_code;
	    public String language_code;
	    public String vat_code;
	    public String email;
	    public String phone_number;
	    public String debtor_reference;
	    public String preferred_channel;
	    public boolean is_attach_ubl;
	    public boolean is_attach_pdf;
	    public ArrayList<Object> bank_account;
	    public String department;
	    
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
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
		public String getClient_debtor_number() {
			return client_debtor_number;
		}
		public void setClient_debtor_number(String client_debtor_number) {
			this.client_debtor_number = client_debtor_number;
		}
		public int getPlatform_id() {
			return platform_id;
		}
		public void setPlatform_id(int platform_id) {
			this.platform_id = platform_id;
		}
		public int getClient_id() {
			return client_id;
		}
		public void setClient_id(int client_id) {
			this.client_id = client_id;
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
		public String getDebtor_type() {
			return debtor_type;
		}
		public void setDebtor_type(String debtor_type) {
			this.debtor_type = debtor_type;
		}
		public String getCurrency_code() {
			return currency_code;
		}
		public void setCurrency_code(String currency_code) {
			this.currency_code = currency_code;
		}
		public String getClient_currency_code() {
			return client_currency_code;
		}
		public void setClient_currency_code(String client_currency_code) {
			this.client_currency_code = client_currency_code;
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
		public String getVat_code() {
			return vat_code;
		}
		public void setVat_code(String vat_code) {
			this.vat_code = vat_code;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPhone_number() {
			return phone_number;
		}
		public void setPhone_number(String phone_number) {
			this.phone_number = phone_number;
		}
		public String getDebtor_reference() {
			return debtor_reference;
		}
		public void setDebtor_reference(String debtor_reference) {
			this.debtor_reference = debtor_reference;
		}
		public String getPreferred_channel() {
			return preferred_channel;
		}
		public void setPreferred_channel(String preferred_channel) {
			this.preferred_channel = preferred_channel;
		}
		public boolean isIs_attach_ubl() {
			return is_attach_ubl;
		}
		public void setIs_attach_ubl(boolean is_attach_ubl) {
			this.is_attach_ubl = is_attach_ubl;
		}
		public boolean isIs_attach_pdf() {
			return is_attach_pdf;
		}
		public void setIs_attach_pdf(boolean is_attach_pdf) {
			this.is_attach_pdf = is_attach_pdf;
		}
		public ArrayList<Object> getBank_account() {
			return bank_account;
		}
		public void setBank_account(ArrayList<Object> bank_account) {
			this.bank_account = bank_account;
		}
		public String getDepartment() {
			return department;
		}
		public void setDepartment(String department) {
			this.department = department;
		}
		
		@Override
		public String toString() {
			return "peppoldebitor [id=" + id + ", debtor_uuid=" + debtor_uuid + ", debtor_number=" + debtor_number
					+ ", client_debtor_number=" + client_debtor_number + ", platform_id=" + platform_id + ", client_id="
					+ client_id + ", client_number=" + client_number + ", name=" + name + ", legal_entity_trn="
					+ legal_entity_trn + ", legal_entity_affiliate=" + legal_entity_affiliate + ", debtor_type="
					+ debtor_type + ", currency_code=" + currency_code + ", client_currency_code="
					+ client_currency_code + ", address=" + address + ", zip_code=" + zip_code + ", city=" + city
					+ ", country_code=" + country_code + ", language_code=" + language_code + ", vat_code=" + vat_code
					+ ", email=" + email + ", phone_number=" + phone_number + ", debtor_reference=" + debtor_reference
					+ ", preferred_channel=" + preferred_channel + ", is_attach_ubl=" + is_attach_ubl
					+ ", is_attach_pdf=" + is_attach_pdf + ", bank_account=" + bank_account + ", department="
					+ department + "]";
		}
}
