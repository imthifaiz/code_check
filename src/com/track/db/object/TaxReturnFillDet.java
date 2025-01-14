package com.track.db.object;

public class TaxReturnFillDet {

	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="COUNTRY_CODE")
	private String COUNTRY_CODE;
	
	@DBTable(columnName ="DATE")
	private String DATE;
	
	@DBTable(columnName ="TRANSACTION_TYPE")
	private String TRANSACTION_TYPE;
	
	@DBTable(columnName ="TAXHDR_ID")
	private int TAXHDR_ID;
	
	@DBTable(columnName ="TRANSACTION_ID")
	private String TRANSACTION_ID;
	
	@DBTable(columnName ="BOX")
	private String BOX;
	
	@DBTable(columnName ="TAXABLE_AMOUNT")
	private Double TAXABLE_AMOUNT=(double) 0;
	
	@DBTable(columnName ="TAX_AMOUNT")
	private Double TAX_AMOUNT=(double) 0;
	
	@DBTable(columnName ="ADJUSTMENTS")
	private Double ADJUSTMENTS=(double) 0;
	
	@DBTable(columnName ="ISTAXPREVIOUS")
	private boolean ISTAXPREVIOUS;
	
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

	public String getCOUNTRY_CODE() {
		return COUNTRY_CODE;
	}

	public void setCOUNTRY_CODE(String cOUNTRY_CODE) {
		COUNTRY_CODE = cOUNTRY_CODE;
	}

	
	public String getDATE() {
		return DATE;
	}

	public void setDATE(String dATE) {
		DATE = dATE;
	}

	public String getTRANSACTION_TYPE() {
		return TRANSACTION_TYPE;
	}

	public void setTRANSACTION_TYPE(String tRANSACTION_TYPE) {
		TRANSACTION_TYPE = tRANSACTION_TYPE;
	}

	public int getTAXHDR_ID() {
		return TAXHDR_ID;
	}

	public void setTAXHDR_ID(int tAXHDR_ID) {
		TAXHDR_ID = tAXHDR_ID;
	}

	public String getTRANSACTION_ID() {
		return TRANSACTION_ID;
	}

	public void setTRANSACTION_ID(String tRANSACTION_ID) {
		TRANSACTION_ID = tRANSACTION_ID;
	}

	public String getBOX() {
		return BOX;
	}

	public void setBOX(String bOX) {
		BOX = bOX;
	}

	public Double getTAXABLE_AMOUNT() {
		return TAXABLE_AMOUNT;
	}

	public void setTAXABLE_AMOUNT(Double tAXABLE_AMOUNT) {
		TAXABLE_AMOUNT = tAXABLE_AMOUNT;
	}

	public Double getTAX_AMOUNT() {
		return TAX_AMOUNT;
	}

	public void setTAX_AMOUNT(Double tAX_AMOUNT) {
		TAX_AMOUNT = tAX_AMOUNT;
	}

	public Double getADJUSTMENTS() {
		return ADJUSTMENTS;
	}

	public void setADJUSTMENTS(Double aDJUSTMENTS) {
		ADJUSTMENTS = aDJUSTMENTS;
	}

	public boolean isISTAXPREVIOUS() {
		return ISTAXPREVIOUS;
	}

	public void setISTAXPREVIOUS(boolean iSTAXPREVIOUS) {
		ISTAXPREVIOUS = iSTAXPREVIOUS;
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
