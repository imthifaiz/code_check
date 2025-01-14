package com.track.db.object;

public class FinTaxDetailedSummaryPojo {

	private String boxname;
	
	private String taxdate;
	
	private String transactiontype;
	
	private String transactionid;
	
	private String name;
	
	private String taxname;
	
	private String taxrate;
	
	private String netamount;
	
	private String amount;
	
	private String balance;
	
	private int box;

	public String getBoxname() {
		return boxname;
	}

	public void setBoxname(String boxname) {
		this.boxname = boxname;
	}

	public String getTaxdate() {
		return taxdate;
	}

	public void setTaxdate(String taxdate) {
		this.taxdate = taxdate;
	}

	public String getTransactiontype() {
		return transactiontype;
	}

	public void setTransactiontype(String transactiontype) {
		this.transactiontype = transactiontype;
	}

	public String getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaxname() {
		return taxname;
	}

	public void setTaxname(String taxname) {
		this.taxname = taxname;
	}

	public String getTaxrate() {
		return taxrate;
	}

	public void setTaxrate(String taxrate) {
		this.taxrate = taxrate;
	}

	public String getNetamount() {
		return netamount;
	}

	public void setNetamount(String netamount) {
		this.netamount = netamount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public int getBox() {
		return box;
	}

	public void setBox(int box) {
		this.box = box;
	}

	@Override
	public String toString() {
		return "FinTaxDetailedSummaryPojo [boxname=" + boxname + ", taxdate=" + taxdate + ", transactiontype="
				+ transactiontype + ", transactionid=" + transactionid + ", name=" + name + ", taxname=" + taxname
				+ ", taxrate=" + taxrate + ", netamount=" + netamount + ", amount=" + amount + ", balance=" + balance
				+ ", box=" + box + "]";
	}

	
}
