package com.track.db.object;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class LedgerDetailsWithId{
	
	@DBTable(columnName ="ACCOUNT_ID")
	private int ACCOUNT_ID;

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
	
	@DBTable(columnName ="BALANCEAMT")
	private String BALANCEAMT;
	
	@DBTable(columnName ="RECONCILIATION")
	private short RECONCILIATION;

	public int getACCOUNT_ID() {
		return ACCOUNT_ID;
	}

	public void setACCOUNT_ID(int aCCOUNT_ID) {
		ACCOUNT_ID = aCCOUNT_ID;
	}

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

	public String getBALANCEAMT() {
		return BALANCEAMT;
	}

	public void setBALANCEAMT(String bALANCEAMT) {
		BALANCEAMT = bALANCEAMT;
	}

	public int getRECONCILIATION() {
		return RECONCILIATION;
	}

	public void setRECONCILIATION(short rECONCILIATION) {
		RECONCILIATION = rECONCILIATION;
	}

	@Override
	public String toString() {
		return "LedgerDetailsWithId [ACCOUNT_ID=" + ACCOUNT_ID + ", JOURNALHDRID=" + JOURNALHDRID + ", DATE=" + DATE
				+ ", ACCOUNT=" + ACCOUNT + ", TRANSACTION_DETAILS=" + TRANSACTION_DETAILS + ", TRANSACTION_TYPE="
				+ TRANSACTION_TYPE + ", TRANSACTION_ID=" + TRANSACTION_ID + ", REFERENCE=" + REFERENCE + ", DEBIT="
				+ DEBIT + ", CREDIT=" + CREDIT + ", BALANCEAMT=" + BALANCEAMT + ", RECONCILIATION=" + RECONCILIATION
				+ "]";
	}
	
	
	
	
}
