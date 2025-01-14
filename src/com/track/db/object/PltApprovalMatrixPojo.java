package com.track.db.object;

public class PltApprovalMatrixPojo {
	
	private int ID;
	private String PLANT;
	private String CNAME;
	private String APPROVALTYPE;		
	private Short ISCREATE;
	private Short ISUPDATE;
	private Short ISDELETE;
	private String CRAT;
	private String CRBY;
	private String UPAT;
	private String UPBY;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getPLANT() {
		return PLANT;
	}
	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}
	public String getCNAME() {
		return CNAME;
	}
	public void setCNAME(String cNAME) {
		CNAME = cNAME;
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
		return "PltApprovalMatrixPojo [ID=" + ID + ", PLANT=" + PLANT + ", CNAME=" + CNAME + ", APPROVALTYPE="
				+ APPROVALTYPE + ", ISCREATE=" + ISCREATE + ", ISUPDATE=" + ISUPDATE + ", ISDELETE=" + ISDELETE
				+ ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}
	

}
