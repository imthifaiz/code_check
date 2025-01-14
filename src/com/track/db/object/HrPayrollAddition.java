package com.track.db.object;

public class HrPayrollAddition {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="EMPID")
	private int EMPID;
	
	@DBTable(columnName ="ADDITION_NAME")
	private String ADDITION_NAME;
	
	@DBTable(columnName ="ADDITION_AMOUNT")
	private Double ADDITION_AMOUNT;
	
	@DBTable(columnName ="ADDITION_DATE")
	private String ADDITION_DATE;
	
	@DBTable(columnName ="MONTH")
	private String MONTH;
	
	@DBTable(columnName ="YEAR")
	private String YEAR;
	
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

	public String getADDITION_NAME() {
		return ADDITION_NAME;
	}

	public void setADDITION_NAME(String aDDITION_NAME) {
		ADDITION_NAME = aDDITION_NAME;
	}

	public Double getADDITION_AMOUNT() {
		return ADDITION_AMOUNT;
	}

	public void setADDITION_AMOUNT(Double aDDITION_AMOUNT) {
		ADDITION_AMOUNT = aDDITION_AMOUNT;
	}

	public String getADDITION_DATE() {
		return ADDITION_DATE;
	}

	public void setADDITION_DATE(String aDDITION_DATE) {
		ADDITION_DATE = aDDITION_DATE;
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
		return "HrPayrollAddition [PLANT=" + PLANT + ", ID=" + ID + ", EMPID=" + EMPID + ", ADDITION_NAME="
				+ ADDITION_NAME + ", ADDITION_AMOUNT=" + ADDITION_AMOUNT + ", ADDITION_DATE=" + ADDITION_DATE
				+ ", MONTH=" + MONTH + ", YEAR=" + YEAR + ", STATUS=" + STATUS + ", CRAT=" + CRAT + ", CRBY=" + CRBY
				+ ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}
	
	
}
