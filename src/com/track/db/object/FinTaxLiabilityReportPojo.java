package com.track.db.object;

public class FinTaxLiabilityReportPojo {

	private String boxname;
	
	private String taxname;
	
	private double netamount;
	
	private double amount;

	public String getBoxname() {
		return boxname;
	}

	public void setBoxname(String boxname) {
		this.boxname = boxname;
	}

	public String getTaxname() {
		return taxname;
	}

	public void setTaxname(String taxname) {
		this.taxname = taxname;
	}

	public double getNetamount() {
		return netamount;
	}

	public void setNetamount(double netamount) {
		this.netamount = netamount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public FinTaxLiabilityReportPojo(String boxname, String taxname, double netamount, double amount) {
		super();
		this.boxname = boxname;
		this.taxname = taxname;
		this.netamount = netamount;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "FinTaxLiabilityReportPojo [boxname=" + boxname + ", taxname=" + taxname + ", netamount=" + netamount
				+ ", amount=" + amount + "]";
	}
	
	
	
}
