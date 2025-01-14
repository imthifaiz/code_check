package com.track.db.object;

public class HrDeductionHdr {

	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="EMPID")
	private int EMPID;
	
	@DBTable(columnName ="DEDUCTION_NAME")
	private String DEDUCTION_NAME;
	
	@DBTable(columnName ="DEDUCTION_AMOUNT")
	private Double DEDUCTION_AMOUNT;
	
	@DBTable(columnName ="DEDUCTION_DUE")
	private Double DEDUCTION_DUE;
	
	@DBTable(columnName ="DEDUCTION_DATE")
	private String DEDUCTION_DATE;
	
	@DBTable(columnName ="MONTH")
	private String MONTH;
	
	@DBTable(columnName ="YEAR")
	private String YEAR;
	
	@DBTable(columnName ="ISGRATUITY")
	private Short ISGRATUITY;
	
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

	public String getDEDUCTION_NAME() {
		return DEDUCTION_NAME;
	}

	public void setDEDUCTION_NAME(String dEDUCTION_NAME) {
		DEDUCTION_NAME = dEDUCTION_NAME;
	}

	public Double getDEDUCTION_AMOUNT() {
		return DEDUCTION_AMOUNT;
	}

	public void setDEDUCTION_AMOUNT(Double dEDUCTION_AMOUNT) {
		DEDUCTION_AMOUNT = dEDUCTION_AMOUNT;
	}

	public Double getDEDUCTION_DUE() {
		return DEDUCTION_DUE;
	}

	public void setDEDUCTION_DUE(Double dEDUCTION_DUE) {
		DEDUCTION_DUE = dEDUCTION_DUE;
	}

	public String getDEDUCTION_DATE() {
		return DEDUCTION_DATE;
	}

	public void setDEDUCTION_DATE(String dEDUCTION_DATE) {
		DEDUCTION_DATE = dEDUCTION_DATE;
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

	public Short getISGRATUITY() {
		return ISGRATUITY;
	}

	public void setISGRATUITY(Short iSGRATUITY) {
		ISGRATUITY = iSGRATUITY;
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
		return "HrDeductionHdr [PLANT=" + PLANT + ", ID=" + ID + ", EMPID=" + EMPID + ", DEDUCTION_NAME="
				+ DEDUCTION_NAME + ", DEDUCTION_AMOUNT=" + DEDUCTION_AMOUNT + ", DEDUCTION_DUE=" + DEDUCTION_DUE
				+ ", DEDUCTION_DATE=" + DEDUCTION_DATE + ", MONTH=" + MONTH + ", YEAR=" + YEAR + ", ISGRATUITY="
				+ ISGRATUITY + ", STATUS=" + STATUS + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY="
				+ UPBY + "]";
	}

	
	
}
