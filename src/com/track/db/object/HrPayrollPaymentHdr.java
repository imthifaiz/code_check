package com.track.db.object;

public class HrPayrollPaymentHdr {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="AMOUNTPAID")
	private double AMOUNTPAID;
	
	@DBTable(columnName ="PAYMENT_DATE")
	private String PAYMENT_DATE;
	
	@DBTable(columnName ="PAYMENT_MODE")
	private String PAYMENT_MODE;
	
	@DBTable(columnName ="PAID_THROUGH")
	private String PAID_THROUGH;
	
	@DBTable(columnName ="REFERENCE")
	private String REFERENCE;
	
	@DBTable(columnName ="BANK_BRANCH")
	private String BANK_BRANCH;
	
	@DBTable(columnName ="AMOUNTUFP")
	private double AMOUNTUFP;
	
	@DBTable(columnName ="AMOUNTREFUNDED")
	private double AMOUNTREFUNDED;
	
	@DBTable(columnName ="BANK_CHARGE")
	private double BANK_CHARGE;
	
	@DBTable(columnName ="CHEQUE_NO")
	private String CHEQUE_NO;
	
	@DBTable(columnName ="PAYMENT_TYPE")
	private String PAYMENT_TYPE;
	
	@DBTable(columnName ="CHEQUE_AMOUNT")
	private double CHEQUE_AMOUNT;
	
	@DBTable(columnName ="ACCOUNT_NAME")
	private String ACCOUNT_NAME;
	
	@DBTable(columnName ="CHEQUE_DATE")
	private String CHEQUE_DATE;
	
	@DBTable(columnName ="ISPDCPROCESS")
	private Short ISPDCPROCESS;
	
	@DBTable(columnName ="CURRENCYID")
	private String CURRENCYID;
	
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

	public double getAMOUNTPAID() {
		return AMOUNTPAID;
	}

	public void setAMOUNTPAID(double aMOUNTPAID) {
		AMOUNTPAID = aMOUNTPAID;
	}

	public String getPAYMENT_DATE() {
		return PAYMENT_DATE;
	}

	public void setPAYMENT_DATE(String pAYMENT_DATE) {
		PAYMENT_DATE = pAYMENT_DATE;
	}

	public String getPAYMENT_MODE() {
		return PAYMENT_MODE;
	}

	public void setPAYMENT_MODE(String pAYMENT_MODE) {
		PAYMENT_MODE = pAYMENT_MODE;
	}

	public String getPAID_THROUGH() {
		return PAID_THROUGH;
	}

	public void setPAID_THROUGH(String pAID_THROUGH) {
		PAID_THROUGH = pAID_THROUGH;
	}

	public String getREFERENCE() {
		return REFERENCE;
	}

	public void setREFERENCE(String rEFERENCE) {
		REFERENCE = rEFERENCE;
	}

	public String getBANK_BRANCH() {
		return BANK_BRANCH;
	}

	public void setBANK_BRANCH(String bANK_BRANCH) {
		BANK_BRANCH = bANK_BRANCH;
	}

	public double getAMOUNTUFP() {
		return AMOUNTUFP;
	}

	public void setAMOUNTUFP(double aMOUNTUFP) {
		AMOUNTUFP = aMOUNTUFP;
	}

	public double getAMOUNTREFUNDED() {
		return AMOUNTREFUNDED;
	}

	public void setAMOUNTREFUNDED(double aMOUNTREFUNDED) {
		AMOUNTREFUNDED = aMOUNTREFUNDED;
	}

	public double getBANK_CHARGE() {
		return BANK_CHARGE;
	}

	public void setBANK_CHARGE(double bANK_CHARGE) {
		BANK_CHARGE = bANK_CHARGE;
	}

	public String getCHEQUE_NO() {
		return CHEQUE_NO;
	}

	public void setCHEQUE_NO(String cHEQUE_NO) {
		CHEQUE_NO = cHEQUE_NO;
	}

	public String getPAYMENT_TYPE() {
		return PAYMENT_TYPE;
	}

	public void setPAYMENT_TYPE(String pAYMENT_TYPE) {
		PAYMENT_TYPE = pAYMENT_TYPE;
	}

	public double getCHEQUE_AMOUNT() {
		return CHEQUE_AMOUNT;
	}

	public void setCHEQUE_AMOUNT(double cHEQUE_AMOUNT) {
		CHEQUE_AMOUNT = cHEQUE_AMOUNT;
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

	public Short getISPDCPROCESS() {
		return ISPDCPROCESS;
	}

	public void setISPDCPROCESS(Short iSPDCPROCESS) {
		ISPDCPROCESS = iSPDCPROCESS;
	}

	public String getCURRENCYID() {
		return CURRENCYID;
	}

	public void setCURRENCYID(String cURRENCYID) {
		CURRENCYID = cURRENCYID;
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

	@Override
	public String toString() {
		return "HrPayrollPaymentHdr [PLANT=" + PLANT + ", ID=" + ID + ", AMOUNTPAID=" + AMOUNTPAID + ", PAYMENT_DATE="
				+ PAYMENT_DATE + ", PAYMENT_MODE=" + PAYMENT_MODE + ", PAID_THROUGH=" + PAID_THROUGH + ", REFERENCE="
				+ REFERENCE + ", BANK_BRANCH=" + BANK_BRANCH + ", AMOUNTUFP=" + AMOUNTUFP + ", AMOUNTREFUNDED="
				+ AMOUNTREFUNDED + ", BANK_CHARGE=" + BANK_CHARGE + ", CHEQUE_NO=" + CHEQUE_NO + ", PAYMENT_TYPE="
				+ PAYMENT_TYPE + ", CHEQUE_AMOUNT=" + CHEQUE_AMOUNT + ", ACCOUNT_NAME=" + ACCOUNT_NAME
				+ ", CHEQUE_DATE=" + CHEQUE_DATE + ", ISPDCPROCESS=" + ISPDCPROCESS + ", CURRENCYID=" + CURRENCYID
				+ ", NOTE=" + NOTE + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}

	

}
