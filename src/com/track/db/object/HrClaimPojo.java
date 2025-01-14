package com.track.db.object;

public class HrClaimPojo {

	private String PLANT;
		
	private int ID;

	private int EMPNOID;
	
	private String EMPCODE;

	private String EMPNAME;

	private int CLAIMID;
	
	private String CLKEY;
	
	private String CLAIMNAME;

	private String DESCRIPTION;

	private String CLAIMDATE;

	private String FROM_PLACE;

	private String TO_PLACE;

	private double DISTANCE;

	private double AMOUNT;

	private String STATUS;
	
	private String REASON;
	
	private String STATUSDATE;

	private int REPORT_INCHARGE_ID;

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

	public String getEMPCODE() {
		return EMPCODE;
	}

	public void setEMPCODE(String eMPCODE) {
		EMPCODE = eMPCODE;
	}

	public String getEMPNAME() {
		return EMPNAME;
	}

	public void setEMPNAME(String eMPNAME) {
		EMPNAME = eMPNAME;
	}

	public int getCLAIMID() {
		return CLAIMID;
	}

	public void setCLAIMID(int cLAIMID) {
		CLAIMID = cLAIMID;
	}

	public String getCLKEY() {
		return CLKEY;
	}

	public void setCLKEY(String cLKEY) {
		CLKEY = cLKEY;
	}

	public String getCLAIMNAME() {
		return CLAIMNAME;
	}

	public void setCLAIMNAME(String cLAIMNAME) {
		CLAIMNAME = cLAIMNAME;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}

	public String getCLAIMDATE() {
		return CLAIMDATE;
	}

	public void setCLAIMDATE(String cLAIMDATE) {
		CLAIMDATE = cLAIMDATE;
	}

	public String getFROM_PLACE() {
		return FROM_PLACE;
	}

	public void setFROM_PLACE(String fROM_PLACE) {
		FROM_PLACE = fROM_PLACE;
	}

	public String getTO_PLACE() {
		return TO_PLACE;
	}

	public void setTO_PLACE(String tO_PLACE) {
		TO_PLACE = tO_PLACE;
	}

	public double getDISTANCE() {
		return DISTANCE;
	}

	public void setDISTANCE(double dISTANCE) {
		DISTANCE = dISTANCE;
	}

	public double getAMOUNT() {
		return AMOUNT;
	}

	public void setAMOUNT(double aMOUNT) {
		AMOUNT = aMOUNT;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
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

	public int getREPORT_INCHARGE_ID() {
		return REPORT_INCHARGE_ID;
	}

	public void setREPORT_INCHARGE_ID(int rEPORT_INCHARGE_ID) {
		REPORT_INCHARGE_ID = rEPORT_INCHARGE_ID;
	}

	@Override
	public String toString() {
		return "HrClaimPojo [PLANT=" + PLANT + ", ID=" + ID + ", EMPNOID=" + EMPNOID + ", EMPCODE=" + EMPCODE
				+ ", EMPNAME=" + EMPNAME + ", CLAIMID=" + CLAIMID + ", CLKEY=" + CLKEY + ", CLAIMNAME=" + CLAIMNAME
				+ ", DESCRIPTION=" + DESCRIPTION + ", CLAIMDATE=" + CLAIMDATE + ", FROM_PLACE=" + FROM_PLACE
				+ ", TO_PLACE=" + TO_PLACE + ", DISTANCE=" + DISTANCE + ", AMOUNT=" + AMOUNT + ", STATUS=" + STATUS
				+ ", REASON=" + REASON + ", STATUSDATE=" + STATUSDATE + ", REPORT_INCHARGE_ID=" + REPORT_INCHARGE_ID
				+ "]";
	}


}
