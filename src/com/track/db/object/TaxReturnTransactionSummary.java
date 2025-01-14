package com.track.db.object;

public class TaxReturnTransactionSummary {

	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="DATE")
	private String DATE;
	
	@DBTable(columnName ="ENTRY")
	private String ENTRY;
	
	@DBTable(columnName ="TRANSACTION_TYPE")
	private String TRANSACTION_TYPE;
	
	@DBTable(columnName ="TAXABLE_AMOUNT")
	private Double TAXABLE_AMOUNT;
	
	@DBTable(columnName ="TAX_AMOUNT")
	private Double TAX_AMOUNT;

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

	public String getENTRY() {
		return ENTRY;
	}

	public void setENTRY(String eNTRY) {
		ENTRY = eNTRY;
	}

	public String getTRANSACTION_TYPE() {
		return TRANSACTION_TYPE;
	}

	public void setTRANSACTION_TYPE(String tRANSACTION_TYPE) {
		TRANSACTION_TYPE = tRANSACTION_TYPE;
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
	
	
	
}
