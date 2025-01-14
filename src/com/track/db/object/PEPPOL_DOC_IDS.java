package com.track.db.object;

public class PEPPOL_DOC_IDS {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="DOC_ID")
	private String DOC_ID;
	
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
	
	public String getDOC_ID() {
		return DOC_ID;
	}
	
	public void setDOC_ID(String dOC_ID) {
		DOC_ID = dOC_ID;
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
		return "PEPPOL_DOC_IDS [PLANT=" + PLANT + ", ID=" + ID + ", DOC_ID=" + DOC_ID + ", CRAT=" + CRAT + ", CRBY=" + CRBY
				+ ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}

	
	
	
	

}
