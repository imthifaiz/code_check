package com.track.db.object;

public class HrEmpType {

	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
		
	@DBTable(columnName ="EMPLOYEETYPE")
	private String EMPLOYEETYPE;
		
	@DBTable(columnName ="EMPLOYEETYPEDESC")
	private String EMPLOYEETYPEDESC;
		
	@DBTable(columnName ="IsActive")
	private String IsActive;
		
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

	public String getEMPLOYEETYPE() {
		return EMPLOYEETYPE;
	}

	public void setEMPLOYEETYPE(String eMPLOYEETYPE) {
		EMPLOYEETYPE = eMPLOYEETYPE;
	}

	public String getEMPLOYEETYPEDESC() {
		return EMPLOYEETYPEDESC;
	}

	public void setEMPLOYEETYPEDESC(String eMPLOYEETYPEDESC) {
		EMPLOYEETYPEDESC = eMPLOYEETYPEDESC;
	}

	public String getIsActive() {
		return IsActive;
	}

	public void setIsActive(String isActive) {
		IsActive = isActive;
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
		return "HrEmpType [PLANT=" + PLANT + ", ID=" + ID + ", EMPLOYEETYPE=" + EMPLOYEETYPE + ", EMPLOYEETYPEDESC="
				+ EMPLOYEETYPEDESC + ", IsActive=" + IsActive + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT
				+ ", UPBY=" + UPBY + "]";
	}
	
	
}
