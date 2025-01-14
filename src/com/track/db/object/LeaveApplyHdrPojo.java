package com.track.db.object;

public class LeaveApplyHdrPojo {
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="EMPNOID")
	private int EMPNOID;
	
	@DBTable(columnName ="LEAVETYPEID")
	private int LEAVETYPEID;
	
	@DBTable(columnName ="LEAVETYPE")
	private String LEAVETYPE;

	@DBTable(columnName ="FROM_DATE")
	private String FROM_DATE;
	
	@DBTable(columnName ="TO_DATE")
	private String TO_DATE;
	
	@DBTable(columnName ="NUMBEROFDAYS")
	private double NUMBEROFDAYS;
	
	@DBTable(columnName ="STATUS")
	private String STATUS;
	
	@DBTable(columnName ="NOTES")
	private String NOTES;
	
	@DBTable(columnName ="REASON")
	private String REASON;
	
	@DBTable(columnName ="STATUSDATE")
	private String STATUSDATE;

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

	public int getLEAVETYPEID() {
		return LEAVETYPEID;
	}

	public void setLEAVETYPEID(int lEAVETYPEID) {
		LEAVETYPEID = lEAVETYPEID;
	}

	public String getLEAVETYPE() {
		return LEAVETYPE;
	}

	public void setLEAVETYPE(String lEAVETYPE) {
		LEAVETYPE = lEAVETYPE;
	}

	public String getFROM_DATE() {
		return FROM_DATE;
	}

	public void setFROM_DATE(String fROM_DATE) {
		FROM_DATE = fROM_DATE;
	}

	public String getTO_DATE() {
		return TO_DATE;
	}

	public void setTO_DATE(String tO_DATE) {
		TO_DATE = tO_DATE;
	}

	public double getNUMBEROFDAYS() {
		return NUMBEROFDAYS;
	}

	public void setNUMBEROFDAYS(double nUMBEROFDAYS) {
		NUMBEROFDAYS = nUMBEROFDAYS;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getNOTES() {
		return NOTES;
	}

	public void setNOTES(String nOTES) {
		NOTES = nOTES;
	}

	public String getREASON() {
		return REASON;
	}

	public void setREASON(String rEASON) {
		REASON = rEASON;
	}

	public String getSTATUSDATE() {
		return STATUSDATE;
	}

	public void setSTATUSDATE(String sTATUSDATE) {
		STATUSDATE = sTATUSDATE;
	}

	@Override
	public String toString() {
		return "LeaveApplyHdrPojo [ID=" + ID + ", EMPNOID=" + EMPNOID + ", LEAVETYPEID=" + LEAVETYPEID + ", LEAVETYPE="
				+ LEAVETYPE + ", FROM_DATE=" + FROM_DATE + ", TO_DATE=" + TO_DATE + ", NUMBEROFDAYS=" + NUMBEROFDAYS
				+ ", STATUS=" + STATUS + ", NOTES=" + NOTES + ", REASON=" + REASON + ", STATUSDATE=" + STATUSDATE + "]";
	}

	
	
	

}
