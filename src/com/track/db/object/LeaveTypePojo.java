package com.track.db.object;

public class LeaveTypePojo {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
		
	@DBTable(columnName ="LEAVETYPE")
	private String LEAVETYPE;
		
	@DBTable(columnName ="TOTALENTITLEMENT")
	private Double TOTALENTITLEMENT;
		
	@DBTable(columnName ="CARRYFORWARD")
	private Double CARRYFORWARD;
		
	@DBTable(columnName ="EMPLOYEETYPEID")
	private int EMPLOYEETYPEID;
		
	@DBTable(columnName ="NOTE")
	private String NOTE;
	
	@DBTable(columnName ="EMPLOYEETYPE")
	private String EMPLOYEETYPE;
	
	@DBTable(columnName ="ISNOPAYLEAVE")
	private short ISNOPAYLEAVE;

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

	public String getLEAVETYPE() {
		return LEAVETYPE;
	}

	public void setLEAVETYPE(String lEAVETYPE) {
		LEAVETYPE = lEAVETYPE;
	}

	public Double getTOTALENTITLEMENT() {
		return TOTALENTITLEMENT;
	}

	public void setTOTALENTITLEMENT(Double tOTALENTITLEMENT) {
		TOTALENTITLEMENT = tOTALENTITLEMENT;
	}

	public Double getCARRYFORWARD() {
		return CARRYFORWARD;
	}

	public void setCARRYFORWARD(Double cARRYFORWARD) {
		CARRYFORWARD = cARRYFORWARD;
	}

	public int getEMPLOYEETYPEID() {
		return EMPLOYEETYPEID;
	}

	public void setEMPLOYEETYPEID(int eMPLOYEETYPEID) {
		EMPLOYEETYPEID = eMPLOYEETYPEID;
	}

	public String getNOTE() {
		return NOTE;
	}

	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}

	public String getEMPLOYEETYPE() {
		return EMPLOYEETYPE;
	}

	public void setEMPLOYEETYPE(String eMPLOYEETYPE) {
		EMPLOYEETYPE = eMPLOYEETYPE;
	}

	public short getISNOPAYLEAVE() {
		return ISNOPAYLEAVE;
	}

	public void setISNOPAYLEAVE(short iSNOPAYLEAVE) {
		ISNOPAYLEAVE = iSNOPAYLEAVE;
	}

	@Override
	public String toString() {
		return "LeaveTypePojo [PLANT=" + PLANT + ", ID=" + ID + ", LEAVETYPE=" + LEAVETYPE + ", TOTALENTITLEMENT="
				+ TOTALENTITLEMENT + ", CARRYFORWARD=" + CARRYFORWARD + ", EMPLOYEETYPEID=" + EMPLOYEETYPEID + ", NOTE="
				+ NOTE + ", EMPLOYEETYPE=" + EMPLOYEETYPE + ", ISNOPAYLEAVE=" + ISNOPAYLEAVE + "]";
	}
	
	

	
	
	
	
}
