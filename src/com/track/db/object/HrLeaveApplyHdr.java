package com.track.db.object;

public class HrLeaveApplyHdr {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="EMPNOID")
	private int EMPNOID;
	
	@DBTable(columnName ="LEAVETYPEID")
	private int LEAVETYPEID;
	
	@DBTable(columnName ="REPORT_INCHARGE_ID")
	private int REPORT_INCHARGE_ID;
	
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

	public int getLEAVETYPEID() {
		return LEAVETYPEID;
	}

	public void setLEAVETYPEID(int lEAVETYPEID) {
		LEAVETYPEID = lEAVETYPEID;
	}

	public int getREPORT_INCHARGE_ID() {
		return REPORT_INCHARGE_ID;
	}

	public void setREPORT_INCHARGE_ID(int rEPORT_INCHARGE_ID) {
		REPORT_INCHARGE_ID = rEPORT_INCHARGE_ID;
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
		return "HrLeaveApplyHdr [PLANT=" + PLANT + ", ID=" + ID + ", EMPNOID=" + EMPNOID + ", LEAVETYPEID="
				+ LEAVETYPEID + ", REPORT_INCHARGE_ID=" + REPORT_INCHARGE_ID + ", FROM_DATE=" + FROM_DATE + ", TO_DATE="
				+ TO_DATE + ", NUMBEROFDAYS=" + NUMBEROFDAYS + ", STATUS=" + STATUS + ", NOTES=" + NOTES + ", REASON="
				+ REASON + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}

	
	

}
