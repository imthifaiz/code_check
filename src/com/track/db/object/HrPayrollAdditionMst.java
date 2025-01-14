package com.track.db.object;

public class HrPayrollAdditionMst {

	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="ADDITION_NAME")
	private String ADDITION_NAME;
	
	@DBTable(columnName ="ADDITION_DESCRIPTION")
	private String ADDITION_DESCRIPTION;
	
	@DBTable(columnName ="ISDEDUCTION")
	private Short ISDEDUCTION;
	
	@DBTable(columnName ="ISCLAIM")
	private Short ISCLAIM;

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

	public String getADDITION_NAME() {
		return ADDITION_NAME;
	}

	public void setADDITION_NAME(String aDDITION_NAME) {
		ADDITION_NAME = aDDITION_NAME;
	}

	public String getADDITION_DESCRIPTION() {
		return ADDITION_DESCRIPTION;
	}

	public void setADDITION_DESCRIPTION(String aDDITION_DESCRIPTION) {
		ADDITION_DESCRIPTION = aDDITION_DESCRIPTION;
	}

	public Short getISDEDUCTION() {
		return ISDEDUCTION;
	}

	public void setISDEDUCTION(Short iSDEDUCTION) {
		ISDEDUCTION = iSDEDUCTION;
	}

	public Short getISCLAIM() {
		return ISCLAIM;
	}

	public void setISCLAIM(Short iSCLAIM) {
		ISCLAIM = iSCLAIM;
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
		return "HrPayrollAdditionMst [PLANT=" + PLANT + ", ID=" + ID + ", ADDITION_NAME=" + ADDITION_NAME
				+ ", ADDITION_DESCRIPTION=" + ADDITION_DESCRIPTION + ", ISDEDUCTION=" + ISDEDUCTION + ", ISCLAIM="
				+ ISCLAIM + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}
	

	
}
