package com.track.db.object;

public class HrLeaveType {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
		
	@DBTable(columnName ="LEAVETYPE")
	private String LEAVETYPE;
		
	@DBTable(columnName ="TOTALENTITLEMENT")
	private Double TOTALENTITLEMENT;
		
	@DBTable(columnName ="ISNOPAYLEAVE")
	private Short ISNOPAYLEAVE;
		
	@DBTable(columnName ="CARRYFORWARD")
	private Double CARRYFORWARD;
		
	@DBTable(columnName ="EMPLOYEETYPEID")
	private int EMPLOYEETYPEID;
		
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

	public Short getISNOPAYLEAVE() {
		return ISNOPAYLEAVE;
	}

	public void setISNOPAYLEAVE(Short iSNOPAYLEAVE) {
		ISNOPAYLEAVE = iSNOPAYLEAVE;
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
		return "HrLeaveType [PLANT=" + PLANT + ", ID=" + ID + ", LEAVETYPE=" + LEAVETYPE + ", TOTALENTITLEMENT="
				+ TOTALENTITLEMENT + ", ISNOPAYLEAVE=" + ISNOPAYLEAVE + ", CARRYFORWARD=" + CARRYFORWARD
				+ ", EMPLOYEETYPEID=" + EMPLOYEETYPEID + ", NOTE=" + NOTE + ", CRAT=" + CRAT + ", CRBY=" + CRBY
				+ ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}

	
	
	
	

}
