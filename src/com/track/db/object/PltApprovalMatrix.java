package com.track.db.object;

public class PltApprovalMatrix {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
		
	@DBTable(columnName ="APPROVALTYPE")
	private String APPROVALTYPE;
		
	@DBTable(columnName ="ISCREATE")
	private Short ISCREATE;
	
	@DBTable(columnName ="ISUPDATE")
	private Short ISUPDATE;
	
	@DBTable(columnName ="ISDELETE")
	private Short ISDELETE;
		
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

	public String getAPPROVALTYPE() {
		return APPROVALTYPE;
	}

	public void setAPPROVALTYPE(String aPPROVALTYPE) {
		APPROVALTYPE = aPPROVALTYPE;
	}

	public Short getISCREATE() {
		return ISCREATE;
	}

	public void setISCREATE(Short iSCREATE) {
		ISCREATE = iSCREATE;
	}

	public Short getISUPDATE() {
		return ISUPDATE;
	}

	public void setISUPDATE(Short iSUPDATE) {
		ISUPDATE = iSUPDATE;
	}

	public Short getISDELETE() {
		return ISDELETE;
	}

	public void setISDELETE(Short iSDELETE) {
		ISDELETE = iSDELETE;
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
		return "PltApprovalMatrix [PLANT=" + PLANT + ", ID=" + ID + ", APPROVALTYPE=" + APPROVALTYPE + ", ISCREATE="
				+ ISCREATE + ", ISUPDATE=" + ISUPDATE + ", ISDELETE=" + ISDELETE + ", CRAT=" + CRAT + ", CRBY=" + CRBY
				+ ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}
	
	

}
