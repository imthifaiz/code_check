package com.track.db.object;

import java.util.List;

public class JournalHeader {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="ID")
	private int ID;

	@DBTable(columnName ="JOURNAL_DATE")
	private String JOURNAL_DATE;
	
	@DBTable(columnName ="TRANSACTION_TYPE")
	private String TRANSACTION_TYPE;
	
	@DBTable(columnName ="TRANSACTION_ID")
	private String TRANSACTION_ID;
	
	@DBTable(columnName ="REFERENCE")
	private String REFERENCE="";
	
	@DBTable(columnName ="NOTE")
	private String NOTE="";
	
	@DBTable(columnName ="CURRENCYID")
	private String CURRENCYID;
	
	@DBTable(columnName ="JOURNAL_TYPE")
	private String JOURNAL_TYPE="Cash";
	
	@DBTable(columnName ="JOURNAL_STATUS")
	private String JOURNAL_STATUS;
	
	@DBTable(columnName ="SUB_TOTAL")
	private Double SUB_TOTAL=0.00;
	
	@DBTable(columnName ="TOTAL_AMOUNT")
	private Double TOTAL_AMOUNT;
	
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

	public String getJOURNAL_DATE() {
		return JOURNAL_DATE;
	}

	public void setJOURNAL_DATE(String jOURNAL_DATE) {
		JOURNAL_DATE = jOURNAL_DATE;
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

	public String getNOTE() {
		return NOTE;
	}

	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}

	public String getCURRENCYID() {
		return CURRENCYID;
	}

	public void setCURRENCYID(String cURRENCYID) {
		CURRENCYID = cURRENCYID;
	}

	public String getJOURNAL_TYPE() {
		return JOURNAL_TYPE;
	}

	public void setJOURNAL_TYPE(String jOURNAL_TYPE) {
		JOURNAL_TYPE = jOURNAL_TYPE;
	}

	public String getJOURNAL_STATUS() {
		return JOURNAL_STATUS;
	}

	public void setJOURNAL_STATUS(String jOURNAL_STATUS) {
		JOURNAL_STATUS = jOURNAL_STATUS;
	}

	public Double getSUB_TOTAL() {
		return SUB_TOTAL;
	}

	public void setSUB_TOTAL(Double sUB_TOTAL) {
		SUB_TOTAL = sUB_TOTAL;
	}

	public Double getTOTAL_AMOUNT() {
		return TOTAL_AMOUNT;
	}

	public void setTOTAL_AMOUNT(Double tOTAL_AMOUNT) {
		TOTAL_AMOUNT = tOTAL_AMOUNT;
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

	@Override
	public String toString() {
		return "JournalHeader [PLANT=" + PLANT + ", ID=" + ID + ", JOURNAL_DATE=" + JOURNAL_DATE + ", REFERENCE="
				+ REFERENCE + ", NOTE=" + NOTE + ", CURRENCYID=" + CURRENCYID + ", JOURNAL_TYPE=" + JOURNAL_TYPE
				+ ", JOURNAL_STATUS=" + JOURNAL_STATUS + ", SUB_TOTAL=" + SUB_TOTAL + ", TOTAL_AMOUNT=" + TOTAL_AMOUNT
				+ ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + ", TRANSACTION_TYPE=" + TRANSACTION_TYPE + "]";
	}
	
	
	
	
	
}
