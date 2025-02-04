package com.track.db.object;

public class CashFlow {
	
	private int id;

	private String main_account;
	
	private String account_type;
	
	private String extended_type;
	
	private int account_id;
	
	private String account_name;
	
	private Double net_credit;
	
	private Double net_debit;
	
	private Double total;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMain_account() {
		return main_account;
	}

	public void setMain_account(String main_account) {
		this.main_account = main_account;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

	public String getExtended_type() {
		return extended_type;
	}

	public void setExtended_type(String extended_type) {
		this.extended_type = extended_type;
	}

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public Double getNet_credit() {
		return net_credit;
	}

	public void setNet_credit(Double net_credit) {
		this.net_credit = net_credit;
	}

	public Double getNet_debit() {
		return net_debit;
	}

	public void setNet_debit(Double net_debit) {
		this.net_debit = net_debit;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "CashFlow [id=" + id + ", main_account=" + main_account + ", account_type=" + account_type
				+ ", extended_type=" + extended_type + ", account_id=" + account_id + ", account_name=" + account_name
				+ ", net_credit=" + net_credit + ", net_debit=" + net_debit + ", total=" + total + "]";
	}
	
	
}
