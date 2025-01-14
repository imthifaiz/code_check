package com.track.db.object;

public class EmployeeLeaveDETpojo {
	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="EMPNOID")
	private int EMPNOID;
	
	@DBTable(columnName ="LEAVETYPEID")
	private int LEAVETYPEID;
	
	@DBTable(columnName ="LEAVETYPE")
	private String LEAVETYPE;
	
	@DBTable(columnName ="TOTALENTITLEMENT")
	private double TOTALENTITLEMENT;
	
	@DBTable(columnName ="LEAVEBALANCE")
	private double LEAVEBALANCE;
	
	@DBTable(columnName ="LEAVEYEAR")
	private String LEAVEYEAR;
	
	@DBTable(columnName ="NOTE")
	private String NOTE;
	
	@DBTable(columnName ="LOP")
	private int LOP;

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

	public String getLEAVETYPE() {
		return LEAVETYPE;
	}

	public void setLEAVETYPE(String lEAVETYPE) {
		LEAVETYPE = lEAVETYPE;
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

	public int getLOP() {
		return LOP;
	}

	public void setLOP(int lOP) {
		LOP = lOP;
	}

	@Override
	public String toString() {
		return "EmployeeLeaveDETpojo [PLANT=" + PLANT + ", ID=" + ID + ", EMPNOID=" + EMPNOID + ", LEAVETYPEID="
				+ LEAVETYPEID + ", LEAVETYPE=" + LEAVETYPE + ", TOTALENTITLEMENT=" + TOTALENTITLEMENT
				+ ", LEAVEBALANCE=" + LEAVEBALANCE + ", LEAVEYEAR=" + LEAVEYEAR + ", NOTE=" + NOTE + ", LOP=" + LOP
				+ "]";
	}

	
	

}
