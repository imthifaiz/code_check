package com.track.db.object;

import java.util.List;

public class InvPaymentHeader {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
	

	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="CUSTNO")
	private String CUSTNO;
	
	@DBTable(columnName ="TRANSACTIONID")
	private String TRANSACTIONID;
	
	@DBTable(columnName ="AMOUNTRECEIVED")
	private Double AMOUNTRECEIVED=(double) 0;
	
	@DBTable(columnName ="AMOUNTUFP")
	private Double AMOUNTUFP=(double) 0;
	
	@DBTable(columnName ="AMOUNTREFUNDED")
	private Double AMOUNTREFUNDED=(double) 0;
	
	@DBTable(columnName ="RECEIVE_DATE")
	private String RECEIVE_DATE;
	
	@DBTable(columnName ="RECEIVE_MODE")
	private String RECEIVE_MODE;
	
	@DBTable(columnName ="DEPOSIT_TO")
	private String DEPOSIT_TO;
	
	@DBTable(columnName ="BANK_BRANCH")
	private String BANK_BRANCH;
	
	@DBTable(columnName ="BANK_CHARGE")
	private Double BANK_CHARGE=(double) 0;
	
	@DBTable(columnName ="CHECQUE_NO")
	private String CHECQUE_NO;
	
	@DBTable(columnName ="REFERENCE")
	private String REFERENCE;
	
	@DBTable(columnName ="NOTE")
	private String NOTE;
	
	@DBTable(columnName ="CRAT")
	private String CRAT;
	
	@DBTable(columnName ="CRBY")
	private String CRBY;
	
	@DBTable(columnName ="UPAT")
	private String UPAT;
	
	@DBTable(columnName ="UPBY")
	private String UPBY;
	
	@DBTable(columnName ="CREDITAPPLYKEY")
	private String CREDITAPPLYKEY;
	
	@DBTable(columnName ="ACCOUNT_NAME")
	private String ACCOUNT_NAME;
	
	@DBTable(columnName ="CHEQUE_DATE")
	private String CHEQUE_DATE;
	
	@DBTable(columnName ="ISPDCPROCESS")
	private short ISPDCPROCESS;
	
	@DBTable(columnName ="CURRENCYID")
	private String CURRENCYID;
	
	@DBTable(columnName ="CURRENCYUSEQT")
	private Double CURRENCYUSEQT=(double) 0;

	@DBTable(columnName ="CONV_AMOUNTRECEIVED")
	private String CONV_AMOUNTRECEIVED;
	
	@DBTable(columnName ="PROJECTID")
	private int PROJECTID;

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

	public String getCUSTNO() {
		return CUSTNO;
	}

	public void setCUSTNO(String cUSTNO) {
		CUSTNO = cUSTNO;
	}

	public String getTRANSACTIONID() {
		return TRANSACTIONID;
	}

	public void setTRANSACTIONID(String tRANSACTIONID) {
		TRANSACTIONID = tRANSACTIONID;
	}

	public Double getAMOUNTRECEIVED() {
		return AMOUNTRECEIVED;
	}

	public void setAMOUNTRECEIVED(Double aMOUNTRECEIVED) {
		AMOUNTRECEIVED = aMOUNTRECEIVED;
	}

	public Double getAMOUNTUFP() {
		return AMOUNTUFP;
	}

	public void setAMOUNTUFP(Double aMOUNTUFP) {
		AMOUNTUFP = aMOUNTUFP;
	}

	public Double getAMOUNTREFUNDED() {
		return AMOUNTREFUNDED;
	}

	public void setAMOUNTREFUNDED(Double aMOUNTREFUNDED) {
		AMOUNTREFUNDED = aMOUNTREFUNDED;
	}

	public String getRECEIVE_DATE() {
		return RECEIVE_DATE;
	}

	public void setRECEIVE_DATE(String rECEIVE_DATE) {
		RECEIVE_DATE = rECEIVE_DATE;
	}

	public String getRECEIVE_MODE() {
		return RECEIVE_MODE;
	}

	public void setRECEIVE_MODE(String rECEIVE_MODE) {
		RECEIVE_MODE = rECEIVE_MODE;
	}

	public String getDEPOSIT_TO() {
		return DEPOSIT_TO;
	}

	public void setDEPOSIT_TO(String dEPOSIT_TO) {
		DEPOSIT_TO = dEPOSIT_TO;
	}

	public String getBANK_BRANCH() {
		return BANK_BRANCH;
	}

	public void setBANK_BRANCH(String bANK_BRANCH) {
		BANK_BRANCH = bANK_BRANCH;
	}

	public Double getBANK_CHARGE() {
		return BANK_CHARGE;
	}

	public void setBANK_CHARGE(Double bANK_CHARGE) {
		BANK_CHARGE = bANK_CHARGE;
	}

	public String getCHECQUE_NO() {
		return CHECQUE_NO;
	}

	public void setCHECQUE_NO(String cHECQUE_NO) {
		CHECQUE_NO = cHECQUE_NO;
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

	public String getCREDITAPPLYKEY() {
		return CREDITAPPLYKEY;
	}

	public void setCREDITAPPLYKEY(String cREDITAPPLYKEY) {
		CREDITAPPLYKEY = cREDITAPPLYKEY;
	}

	public String getACCOUNT_NAME() {
		return ACCOUNT_NAME;
	}

	public void setACCOUNT_NAME(String aCCOUNT_NAME) {
		ACCOUNT_NAME = aCCOUNT_NAME;
	}

	public String getCHEQUE_DATE() {
		return CHEQUE_DATE;
	}

	public void setCHEQUE_DATE(String cHEQUE_DATE) {
		CHEQUE_DATE = cHEQUE_DATE;
	}

	public short getISPDCPROCESS() {
		return ISPDCPROCESS;
	}

	public void setISPDCPROCESS(short iSPDCPROCESS) {
		ISPDCPROCESS = iSPDCPROCESS;
	}

	public String getCURRENCYID() {
		return CURRENCYID;
	}

	public void setCURRENCYID(String cURRENCYID) {
		CURRENCYID = cURRENCYID;
	}

	public Double getCURRENCYUSEQT() {
		return CURRENCYUSEQT;
	}

	public void setCURRENCYUSEQT(Double cURRENCYUSEQT) {
		CURRENCYUSEQT = cURRENCYUSEQT;
	}

	public String getCONV_AMOUNTRECEIVED() {
		return CONV_AMOUNTRECEIVED;
	}

	public void setCONV_AMOUNTRECEIVED(String cONV_AMOUNTRECEIVED) {
		CONV_AMOUNTRECEIVED = cONV_AMOUNTRECEIVED;
	}

	public int getPROJECTID() {
		return PROJECTID;
	}

	public void setPROJECTID(int pROJECTID) {
		PROJECTID = pROJECTID;
	}

	@Override
	public String toString() {
		return "InvPaymentHeader [PLANT=" + PLANT + ", ID=" + ID + ", CUSTNO=" + CUSTNO + ", TRANSACTIONID="
				+ TRANSACTIONID + ", AMOUNTRECEIVED=" + AMOUNTRECEIVED + ", AMOUNTUFP=" + AMOUNTUFP
				+ ", AMOUNTREFUNDED=" + AMOUNTREFUNDED + ", RECEIVE_DATE=" + RECEIVE_DATE + ", RECEIVE_MODE="
				+ RECEIVE_MODE + ", DEPOSIT_TO=" + DEPOSIT_TO + ", BANK_BRANCH=" + BANK_BRANCH + ", BANK_CHARGE="
				+ BANK_CHARGE + ", CHECQUE_NO=" + CHECQUE_NO + ", REFERENCE=" + REFERENCE + ", NOTE=" + NOTE + ", CRAT="
				+ CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + ", CREDITAPPLYKEY=" + CREDITAPPLYKEY
				+ ", ACCOUNT_NAME=" + ACCOUNT_NAME + ", CHEQUE_DATE=" + CHEQUE_DATE + ", ISPDCPROCESS=" + ISPDCPROCESS
				+ ", CURRENCYID=" + CURRENCYID + ", CURRENCYUSEQT=" + CURRENCYUSEQT + ", CONV_AMOUNTRECEIVED="
				+ CONV_AMOUNTRECEIVED + ", PROJECTID=" + PROJECTID + "]";
	}
	
	
}
