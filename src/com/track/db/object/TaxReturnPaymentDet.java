package com.track.db.object;

public class TaxReturnPaymentDet {

	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="PAYMENTHDR_ID")
	private int PAYMENTHDR_ID;
	
	@DBTable(columnName ="TAX_RETURN")
	private String TAX_RETURN;
	
	@DBTable(columnName ="DATE")
	private String DATE;
	
	@DBTable(columnName ="PAID_THROUGH")
	private String PAID_THROUGH;
	
	@DBTable(columnName ="AMOUNT_PAID")
	private Double AMOUNT_PAID=(double) 0;
	
	@DBTable(columnName ="AMOUNT_RECLAIMED")
	private Double AMOUNT_RECLAIMED=(double) 0;
	
	@DBTable(columnName ="REFERENCE")
	private String REFERENCE;
	
	@DBTable(columnName ="DESCRIPTION")
	private String DESCRIPTION;
	
	@DBTable(columnName ="CRAT")
	private String CRAT;
	
	@DBTable(columnName ="CRBY")
	private String CRBY;
	
	@DBTable(columnName ="UPAT")
	private String UPAT;
	
	@DBTable(columnName ="UPBY")
	private String UPBY;

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

	public String getDATE() {
		return DATE;
	}

	public void setDATE(String dATE) {
		DATE = dATE;
	}

	public String getPAID_THROUGH() {
		return PAID_THROUGH;
	}

	public void setPAID_THROUGH(String pAID_THROUGH) {
		PAID_THROUGH = pAID_THROUGH;
	}

	public Double getAMOUNT_PAID() {
		return AMOUNT_PAID;
	}

	public void setAMOUNT_PAID(Double aMOUNT_PAID) {
		AMOUNT_PAID = aMOUNT_PAID;
	}

	public Double getAMOUNT_RECLAIMED() {
		return AMOUNT_RECLAIMED;
	}

	public void setAMOUNT_RECLAIMED(Double aMOUNT_RECLAIMED) {
		AMOUNT_RECLAIMED = aMOUNT_RECLAIMED;
	}

	public String getREFERENCE() {
		return REFERENCE;
	}

	public void setREFERENCE(String rEFERENCE) {
		REFERENCE = rEFERENCE;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}

	public int getPAYMENTHDR_ID() {
		return PAYMENTHDR_ID;
	}

	public void setPAYMENTHDR_ID(int pAYMENTHDR_ID) {
		PAYMENTHDR_ID = pAYMENTHDR_ID;
	}

	public String getTAX_RETURN() {
		return TAX_RETURN;
	}

	public void setTAX_RETURN(String tAX_RETURN) {
		TAX_RETURN = tAX_RETURN;
	}

	public String getCRAT() {
		return CRAT;
	}

	public void setCRAT(String cRAT) {
		CRAT = cRAT;
	}

	public String getCRBY() {
		return CRBY;
	}

	public void setCRBY(String cRBY) {
		CRBY = cRBY;
	}

	public String getUPAT() {
		return UPAT;
	}

	public void setUPAT(String uPAT) {
		UPAT = uPAT;
	}

	public String getUPBY() {
		return UPBY;
	}

	public void setUPBY(String uPBY) {
		UPBY = uPBY;
	}
	
	
	
}
