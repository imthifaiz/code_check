package com.track.db.object;

public class HrPayrollDET {

	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="HDRID")
	private int HDRID;
	
	@DBTable(columnName ="LNNO")
	private int LNNO;
	
	@DBTable(columnName ="SALARYTYPE")
	private String SALARYTYPE;
	
	@DBTable(columnName ="AMOUNT")
	private Double AMOUNT;
	
	@DBTable(columnName ="AMOUNT_TYPE")
	private String AMOUNT_TYPE;
		
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

	public int getLNNO() {
		return LNNO;
	}

	public void setLNNO(int lNNO) {
		LNNO = lNNO;
	}

	public String getSALARYTYPE() {
		return SALARYTYPE;
	}

	public void setSALARYTYPE(String sALARYTYPE) {
		SALARYTYPE = sALARYTYPE;
	}

	public Double getAMOUNT() {
		return AMOUNT;
	}

	public void setAMOUNT(Double aMOUNT) {
		AMOUNT = aMOUNT;
	}

	public String getAMOUNT_TYPE() {
		return AMOUNT_TYPE;
	}

	public void setAMOUNT_TYPE(String aMOUNT_TYPE) {
		AMOUNT_TYPE = aMOUNT_TYPE;
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
		return "HrPayrollDET [PLANT=" + PLANT + ", ID=" + ID + ", HDRID=" + HDRID + ", LNNO=" + LNNO + ", SALARYTYPE="
				+ SALARYTYPE + ", AMOUNT=" + AMOUNT + ", AMOUNT_TYPE=" + AMOUNT_TYPE + ", CRAT=" + CRAT + ", CRBY="
				+ CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}
	
	
}
