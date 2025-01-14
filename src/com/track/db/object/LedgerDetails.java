package com.track.db.object;

public class LedgerDetails {
	
	@DBTable(columnName ="JOURNALHDRID")
	private int JOURNALHDRID;
	
	@DBTable(columnName ="JOURNAL_DATE")
	private String DATE;
	
	@DBTable(columnName ="ACCOUNT_NAME")
	private String ACCOUNT;
	
	@DBTable(columnName ="DESCRIPTION")
	private String TRANSACTION_DETAILS;
	
	@DBTable(columnName ="TRANSACTION_TYPE")
	private String TRANSACTION_TYPE;
	
	@DBTable(columnName ="TRANSACTION_ID")
	private String TRANSACTION_ID;
	
	@DBTable(columnName ="REFERENCE")
	private String REFERENCE;
	
	@DBTable(columnName ="DEBITS")
	private Double DEBIT;
	
	@DBTable(columnName ="CREDITS")
	private Double CREDIT;
	
	
	

	public int getJOURNALHDRID() {
		return JOURNALHDRID;
	}

	public void setJOURNALHDRID(int jOURNALHDRID) {
		JOURNALHDRID = jOURNALHDRID;
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

	public String getTRANSACTION_DETAILS() {
		return TRANSACTION_DETAILS;
	}

	public void setTRANSACTION_DETAILS(String tRANSACTION_DETAILS) {
		TRANSACTION_DETAILS = tRANSACTION_DETAILS;
	}

	public String getTRANSACTION_TYPE() {
		return TRANSACTION_TYPE;
	}

	public void setTRANSACTION_TYPE(String tRANSACTION_TYPE) {
		TRANSACTION_TYPE = tRANSACTION_TYPE;
	}

	public String getTRANSACTION_ID() {
		return TRANSACTION_ID;
	}

	public void setTRANSACTION_ID(String tRANSACTION_ID) {
		TRANSACTION_ID = tRANSACTION_ID;
	}

	public String getREFERENCE() {
		return REFERENCE;
	}

	public void setREFERENCE(String rEFERENCE) {
		REFERENCE = rEFERENCE;
	}

	public Double getDEBIT() {
		return DEBIT;
	}

	public void setDEBIT(Double dEBIT) {
		DEBIT = dEBIT;
	}

	public Double getCREDIT() {
		return CREDIT;
	}

	public void setCREDIT(Double cREDIT) {
		CREDIT = cREDIT;
	}

	
	

}
