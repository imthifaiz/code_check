package com.track.db.object;

public class EmployeeLeaveDET {

	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="EMPNOID")
	private int EMPNOID;
	
	@DBTable(columnName ="LEAVETYPEID")
	private int LEAVETYPEID;
	
	@DBTable(columnName ="TOTALENTITLEMENT")
	private double TOTALENTITLEMENT;
	
	@DBTable(columnName ="LEAVEBALANCE")
	private double LEAVEBALANCE;
	
	@DBTable(columnName ="LEAVEYEAR")
	private String LEAVEYEAR;
	
	@DBTable(columnName ="NOTE")
	private String NOTE;
		
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

	public double getTOTALENTITLEMENT() {
		return TOTALENTITLEMENT;
	}

	public void setTOTALENTITLEMENT(double tOTALENTITLEMENT) {
		TOTALENTITLEMENT = tOTALENTITLEMENT;
	}

	public double getLEAVEBALANCE() {
		return LEAVEBALANCE;
	}

	public void setLEAVEBALANCE(double lEAVEBALANCE) {
		LEAVEBALANCE = lEAVEBALANCE;
	}

	public String getLEAVEYEAR() {
		return LEAVEYEAR;
	}

	public void setLEAVEYEAR(String lEAVEYEAR) {
		LEAVEYEAR = lEAVEYEAR;
	}

	public String getNOTE() {
		return NOTE;
	}

	public void setNOTE(String nOTE) {
		NOTE = nOTE;
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
		return "EmployeeLeaveDET [PLANT=" + PLANT + ", ID=" + ID + ", EMPNOID=" + EMPNOID + ", LEAVETYPEID="
				+ LEAVETYPEID + ", TOTALENTITLEMENT=" + TOTALENTITLEMENT + ", LEAVEBALANCE=" + LEAVEBALANCE
				+ ", LEAVEYEAR=" + LEAVEYEAR + ", NOTE=" + NOTE + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT
				+ ", UPBY=" + UPBY + "]";
	}
	
	
	
}
