package com.track.db.object;

import java.util.Date;

public class ReconciliationDatePojo {

	private int ID;
	
    private String PLANT;
	
	private Date DATE;
	
	private String ACCOUNT;
	
	private String VOUCHERTYPE;
	
	private Date INSTRUMENTDATE;
	
	private double DEBIT;
	
	private double CREDIT;
	
	private String BANKDATE;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getPLANT() {
		return PLANT;
	}

	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}

	public Date getDATE() {
		return DATE;
	}

	public void setDATE(Date dATE) {
		DATE = dATE;
	}

	public String getACCOUNT() {
		return ACCOUNT;
	}

	public void setACCOUNT(String aCCOUNT) {
		ACCOUNT = aCCOUNT;
	}

	public String getVOUCHERTYPE() {
		return VOUCHERTYPE;
	}

	public void setVOUCHERTYPE(String vOUCHERTYPE) {
		VOUCHERTYPE = vOUCHERTYPE;
	}

	public Date getINSTRUMENTDATE() {
		return INSTRUMENTDATE;
	}

	public void setINSTRUMENTDATE(Date iNSTRUMENTDATE) {
		INSTRUMENTDATE = iNSTRUMENTDATE;
	}

	public double getDEBIT() {
		return DEBIT;
	}

	public void setDEBIT(double dEBIT) {
		DEBIT = dEBIT;
	}

	public double getCREDIT() {
		return CREDIT;
	}

	public void setCREDIT(double cREDIT) {
		CREDIT = cREDIT;
	}

	public String getBANKDATE() {
		return BANKDATE;
	}

	public void setBANKDATE(String bANKDATE) {
		BANKDATE = bANKDATE;
	}

	@Override
	public String toString() {
		return "ReconciliationDatePojo [ID=" + ID + ", PLANT=" + PLANT + ", DATE=" + DATE + ", ACCOUNT=" + ACCOUNT
				+ ", VOUCHERTYPE=" + VOUCHERTYPE + ", INSTRUMENTDATE=" + INSTRUMENTDATE + ", DEBIT=" + DEBIT
				+ ", CREDIT=" + CREDIT + ", BANKDATE=" + BANKDATE + "]";
	}

	
	
	
}
