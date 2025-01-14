package com.track.db.object;

public class TaxReturnFillAdjust {

	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="TAXHDR_ID")
	private int TAXHDR_ID;
	
	@DBTable(columnName ="ADJUSTMENTDATE")
	private String ADJUSTMENTDATE;
	
	@DBTable(columnName ="REFERENCE")
	private String REFERENCE;
	
	@DBTable(columnName ="AMOUNT")
	private Double AMOUNT;
	
	@DBTable(columnName ="TAXAMOUNT")
	private Double TAXAMOUNT;
	
	@DBTable(columnName ="ACCOUNT_NAME")
	private String ACCOUNT_NAME;
	
	@DBTable(columnName ="REASON")
	private String REASON;

	public String getPLANT() {
		return PLANT;
	}

	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getTAXHDR_ID() {
		return TAXHDR_ID;
	}

	public void setTAXHDR_ID(int tAXHDR_ID) {
		TAXHDR_ID = tAXHDR_ID;
	}

	public String getADJUSTMENTDATE() {
		return ADJUSTMENTDATE;
	}

	public void setADJUSTMENTDATE(String aDJUSTMENTDATE) {
		ADJUSTMENTDATE = aDJUSTMENTDATE;
	}

	public String getREFERENCE() {
		return REFERENCE;
	}

	public void setREFERENCE(String rEFERENCE) {
		REFERENCE = rEFERENCE;
	}

	public Double getAMOUNT() {
		return AMOUNT;
	}

	public void setAMOUNT(Double aMOUNT) {
		AMOUNT = aMOUNT;
	}

	public Double getTAXAMOUNT() {
		return TAXAMOUNT;
	}

	public void setTAXAMOUNT(Double tAXAMOUNT) {
		TAXAMOUNT = tAXAMOUNT;
	}

	public String getACCOUNT_NAME() {
		return ACCOUNT_NAME;
	}

	public void setACCOUNT_NAME(String aCCOUNT_NAME) {
		ACCOUNT_NAME = aCCOUNT_NAME;
	}

	public String getREASON() {
		return REASON;
	}

	public void setREASON(String rEASON) {
		REASON = rEASON;
	}
	
	
}
