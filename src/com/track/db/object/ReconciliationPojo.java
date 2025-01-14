package com.track.db.object;

import java.util.Objects;

public class ReconciliationPojo {
	
	@DBTable(columnName ="ID")
	private int ID;

	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="DATE")
	private String DATE;
	
	@DBTable(columnName ="ACCOUNT")
	private String ACCOUNT;
	
	@DBTable(columnName ="VOUCHERTYPE")
	private String VOUCHERTYPE;
	
	@DBTable(columnName ="INSTRUMENTDATE")
	private String INSTRUMENTDATE;
	
	@DBTable(columnName ="DEBIT")
	private double DEBIT;
	
	@DBTable(columnName ="CREDIT")
	private double CREDIT;
	
	@DBTable(columnName ="BANKDATE")
	private String BANKDATE;
	
	@DBTable(columnName ="FILTERDATE")
	private String FILTERDATE;

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

	public String getDATE() {
		return DATE;
	}

	public void setDATE(String dATE) {
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

	public String getINSTRUMENTDATE() {
		return INSTRUMENTDATE;
	}

	public void setINSTRUMENTDATE(String iNSTRUMENTDATE) {
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

	public String getFILTERDATE() {
		return FILTERDATE;
	}

	public void setFILTERDATE(String fILTERDATE) {
		FILTERDATE = fILTERDATE;
	}

	@Override
	public String toString() {
		return "ReconciliationPojo [ID=" + ID + ", PLANT=" + PLANT + ", DATE=" + DATE + ", ACCOUNT=" + ACCOUNT
				+ ", VOUCHERTYPE=" + VOUCHERTYPE + ", INSTRUMENTDATE=" + INSTRUMENTDATE + ", DEBIT=" + DEBIT
				+ ", CREDIT=" + CREDIT + ", BANKDATE=" + BANKDATE + ", FILTERDATE=" + FILTERDATE + "]";
	}

	
	
	

}
