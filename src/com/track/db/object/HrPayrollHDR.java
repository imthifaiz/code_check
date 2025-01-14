package com.track.db.object;

public class HrPayrollHDR {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="EMPID")
	private int EMPID;
	
	@DBTable(columnName ="PAYROLL")
	private String PAYROLL;
	
	@DBTable(columnName ="FROMDATE")
	private String FROMDATE;
	
	@DBTable(columnName ="TODATE")
	private String TODATE;
	
	@DBTable(columnName ="MONTH")
	private String MONTH;
	
	@DBTable(columnName ="YEAR")
	private String YEAR;
	
	@DBTable(columnName ="PAYDAYS")
	private Double PAYDAYS;
	
	@DBTable(columnName ="TOTAL_AMOUNT")
	private Double TOTAL_AMOUNT;
	
	@DBTable(columnName ="PAYMENT_DATE")
	private String PAYMENT_DATE;
	
	@DBTable(columnName ="PAYMENT_MODE")
	private String PAYMENT_MODE;
	
	@DBTable(columnName ="PAID_THROUGH")
	private String PAID_THROUGH;
	
	@DBTable(columnName ="BANK_BRANCH")
	private String BANK_BRANCH;
	
	@DBTable(columnName ="CHECQUE_NO")
	private String CHECQUE_NO;
	
	@DBTable(columnName ="CHEQUE_DATE")
	private String CHEQUE_DATE;
	
	@DBTable(columnName ="CHEQUE_AMOUNT")
	private Double CHEQUE_AMOUNT;
	
	@DBTable(columnName ="REFERENCE")
	private String REFERENCE;
	
	@DBTable(columnName ="NOTE")
	private String NOTE;
	
	@DBTable(columnName ="STATUS")
	private String STATUS;
		
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

	public int getEMPID() {
		return EMPID;
	}

	public void setEMPID(int eMPID) {
		EMPID = eMPID;
	}

	public String getPAYROLL() {
		return PAYROLL;
	}

	public void setPAYROLL(String pAYROLL) {
		PAYROLL = pAYROLL;
	}

	public String getFROMDATE() {
		return FROMDATE;
	}

	public void setFROMDATE(String fROMDATE) {
		FROMDATE = fROMDATE;
	}

	public String getTODATE() {
		return TODATE;
	}

	public void setTODATE(String tODATE) {
		TODATE = tODATE;
	}

	public String getMONTH() {
		return MONTH;
	}

	public void setMONTH(String mONTH) {
		MONTH = mONTH;
	}

	public String getYEAR() {
		return YEAR;
	}

	public void setYEAR(String yEAR) {
		YEAR = yEAR;
	}

	public Double getPAYDAYS() {
		return PAYDAYS;
	}

	public void setPAYDAYS(Double pAYDAYS) {
		PAYDAYS = pAYDAYS;
	}

	public Double getTOTAL_AMOUNT() {
		return TOTAL_AMOUNT;
	}

	public void setTOTAL_AMOUNT(Double tOTAL_AMOUNT) {
		TOTAL_AMOUNT = tOTAL_AMOUNT;
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

	public String getBANK_BRANCH() {
		return BANK_BRANCH;
	}

	public void setBANK_BRANCH(String bANK_BRANCH) {
		BANK_BRANCH = bANK_BRANCH;
	}

	public String getCHECQUE_NO() {
		return CHECQUE_NO;
	}

	public void setCHECQUE_NO(String cHECQUE_NO) {
		CHECQUE_NO = cHECQUE_NO;
	}

	public String getCHEQUE_DATE() {
		return CHEQUE_DATE;
	}

	public void setCHEQUE_DATE(String cHEQUE_DATE) {
		CHEQUE_DATE = cHEQUE_DATE;
	}

	public Double getCHEQUE_AMOUNT() {
		return CHEQUE_AMOUNT;
	}

	public void setCHEQUE_AMOUNT(Double cHEQUE_AMOUNT) {
		CHEQUE_AMOUNT = cHEQUE_AMOUNT;
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

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
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
		return "HrPayrollHDR [PLANT=" + PLANT + ", ID=" + ID + ", EMPID=" + EMPID + ", PAYROLL=" + PAYROLL
				+ ", FROMDATE=" + FROMDATE + ", TODATE=" + TODATE + ", MONTH=" + MONTH + ", YEAR=" + YEAR + ", PAYDAYS="
				+ PAYDAYS + ", TOTAL_AMOUNT=" + TOTAL_AMOUNT + ", PAYMENT_DATE=" + PAYMENT_DATE + ", PAYMENT_MODE="
				+ PAYMENT_MODE + ", PAID_THROUGH=" + PAID_THROUGH + ", BANK_BRANCH=" + BANK_BRANCH + ", CHECQUE_NO="
				+ CHECQUE_NO + ", CHEQUE_DATE=" + CHEQUE_DATE + ", CHEQUE_AMOUNT=" + CHEQUE_AMOUNT + ", REFERENCE="
				+ REFERENCE + ", NOTE=" + NOTE + ", STATUS=" + STATUS + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT="
				+ UPAT + ", UPBY=" + UPBY + "]";
	}
	
	
}
