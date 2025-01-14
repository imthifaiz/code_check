package com.track.db.object;

public class HrLeaveApplyDet {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="EMPNOID")
	private int EMPNOID;
		
	@DBTable(columnName ="LEAVEHDRID")
	private int LEAVEHDRID;
		
	@DBTable(columnName ="LEAVE_DATE")
	private String LEAVE_DATE;
	
	@DBTable(columnName ="PREPOSTLUNCHTYPE")
	private String PREPOSTLUNCHTYPE;
	
	@DBTable(columnName ="PREPOSTLUNCH")
	private String PREPOSTLUNCH;
		
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

	public int getEMPNOID() {
		return EMPNOID;
	}

	public void setEMPNOID(int eMPNOID) {
		EMPNOID = eMPNOID;
	}

	public int getLEAVEHDRID() {
		return LEAVEHDRID;
	}

	public void setLEAVEHDRID(int lEAVEHDRID) {
		LEAVEHDRID = lEAVEHDRID;
	}

	public String getLEAVE_DATE() {
		return LEAVE_DATE;
	}

	public void setLEAVE_DATE(String lEAVE_DATE) {
		LEAVE_DATE = lEAVE_DATE;
	}

	public String getPREPOSTLUNCHTYPE() {
		return PREPOSTLUNCHTYPE;
	}

	public void setPREPOSTLUNCHTYPE(String pREPOSTLUNCHTYPE) {
		PREPOSTLUNCHTYPE = pREPOSTLUNCHTYPE;
	}

	public String getPREPOSTLUNCH() {
		return PREPOSTLUNCH;
	}

	public void setPREPOSTLUNCH(String pREPOSTLUNCH) {
		PREPOSTLUNCH = pREPOSTLUNCH;
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
		return "HrLeaveApplyDet [PLANT=" + PLANT + ", ID=" + ID + ", EMPNOID=" + EMPNOID + ", LEAVEHDRID=" + LEAVEHDRID
				+ ", LEAVE_DATE=" + LEAVE_DATE + ", PREPOSTLUNCHTYPE=" + PREPOSTLUNCHTYPE + ", PREPOSTLUNCH="
				+ PREPOSTLUNCH + ", STATUS=" + STATUS + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT
				+ ", UPBY=" + UPBY + "]";
	}

	
}
