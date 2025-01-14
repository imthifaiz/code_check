package com.track.db.object;

public class HrDeductionDet {

	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="HDRID")
	private int HDRID;
	
	@DBTable(columnName ="EMPID")
	private int EMPID;
	
	@DBTable(columnName ="LNNO")
	private int LNNO;
	
	@DBTable(columnName ="DUE_AMOUNT")
	private Double DUE_AMOUNT;
	
	@DBTable(columnName ="DUE_MONTH")
	private String DUE_MONTH;
	
	@DBTable(columnName ="DUE_YEAR")
	private String DUE_YEAR;
	
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

	public int getHDRID() {
		return HDRID;
	}

	public void setHDRID(int hDRID) {
		HDRID = hDRID;
	}

	public int getEMPID() {
		return EMPID;
	}

	public void setEMPID(int eMPID) {
		EMPID = eMPID;
	}

	public int getLNNO() {
		return LNNO;
	}

	public void setLNNO(int lNNO) {
		LNNO = lNNO;
	}

	public Double getDUE_AMOUNT() {
		return DUE_AMOUNT;
	}

	public void setDUE_AMOUNT(Double dUE_AMOUNT) {
		DUE_AMOUNT = dUE_AMOUNT;
	}

	public String getDUE_MONTH() {
		return DUE_MONTH;
	}

	public void setDUE_MONTH(String dUE_MONTH) {
		DUE_MONTH = dUE_MONTH;
	}

	public String getDUE_YEAR() {
		return DUE_YEAR;
	}

	public void setDUE_YEAR(String dUE_YEAR) {
		DUE_YEAR = dUE_YEAR;
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
		return "HrDeductionDet [PLANT=" + PLANT + ", ID=" + ID + ", HDRID=" + HDRID + ", EMPID=" + EMPID + ", LNNO="
				+ LNNO + ", DUE_AMOUNT=" + DUE_AMOUNT + ", DUE_MONTH=" + DUE_MONTH + ", DUE_YEAR=" + DUE_YEAR
				+ ", STATUS=" + STATUS + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY
				+ "]";
	}

}
