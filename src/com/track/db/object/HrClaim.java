package com.track.db.object;

public class HrClaim {

	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="EMPNOID")
	private int EMPNOID;
	
	@DBTable(columnName ="CLIAMID")
	private int CLAIMID;
	
	@DBTable(columnName ="CLKEY")
	private String CLKEY;
	
	@DBTable(columnName ="DESCRIPTION")
	private String DESCRIPTION;

	@DBTable(columnName ="CLAIMDATE")
	private String CLAIMDATE;
	
	@DBTable(columnName ="FROM_PLACE")
	private String FROM_PLACE;
	
	@DBTable(columnName ="TO_PLACE")
	private String TO_PLACE;
	
	@DBTable(columnName ="DISTANCE")
	private double DISTANCE;
	
	@DBTable(columnName ="AMOUNT")
	private double AMOUNT;
	
	@DBTable(columnName ="STATUS")
	private String STATUS;
	
	@DBTable(columnName ="REASON")
	private String REASON;
	
	@DBTable(columnName ="REPORT_INCHARGE_ID")
	private int REPORT_INCHARGE_ID;;
		
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

	public int getREPORT_INCHARGE_ID() {
		return REPORT_INCHARGE_ID;
	}

	public void setREPORT_INCHARGE_ID(int rEPORT_INCHARGE_ID) {
		REPORT_INCHARGE_ID = rEPORT_INCHARGE_ID;
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
		return "HrClaim [PLANT=" + PLANT + ", ID=" + ID + ", EMPNOID=" + EMPNOID + ", CLAIMID=" + CLAIMID + ", CLKEY="
				+ CLKEY + ", DESCRIPTION=" + DESCRIPTION + ", CLAIMDATE=" + CLAIMDATE + ", FROM_PLACE=" + FROM_PLACE
				+ ", TO_PLACE=" + TO_PLACE + ", DISTANCE=" + DISTANCE + ", AMOUNT=" + AMOUNT + ", STATUS=" + STATUS
				+ ", REASON=" + REASON + ", REPORT_INCHARGE_ID=" + REPORT_INCHARGE_ID + ", CRAT=" + CRAT + ", CRBY="
				+ CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}

	
	
}
