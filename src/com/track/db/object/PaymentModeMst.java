package com.track.db.object;

public class PaymentModeMst {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
		
	@DBTable(columnName ="PAYMENTMODE")
	private String PAYMENTMODE;
	
	@DBTable(columnName ="ISNOTEDITABLE")
	private Short ISNOTEDITABLE;
	
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

	public String getPAYMENTMODE() {
		return PAYMENTMODE;
	}

	public void setPAYMENTMODE(String pAYMENTMODE) {
		PAYMENTMODE = pAYMENTMODE;
	}

	public Short getISNOTEDITABLE() {
		return ISNOTEDITABLE;
	}

	public void setISNOTEDITABLE(Short iSNOTEDITABLE) {
		ISNOTEDITABLE = iSNOTEDITABLE;
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
		return "PaymentModeMst [PLANT=" + PLANT + ", ID=" + ID + ", PAYMENTMODE=" + PAYMENTMODE + ", ISNOTEDITABLE="
				+ ISNOTEDITABLE + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}
	
	

}
