package com.track.db.object;

public class ClearingAgentTypeDET {

	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
	
	
	@DBTable(columnName ="CLEARING_AGENT_ID")
	private String CLEARING_AGENT_ID;
	
	@DBTable(columnName ="TRANSPORTID")
	private int TRANSPORTID;
	
	@DBTable(columnName ="CONTACTNAME")
	private String CONTACTNAME;
	
	@DBTable(columnName ="TELNO")
	private String TELNO;
	
	@DBTable(columnName ="EMAIL")
	private String EMAIL;
		
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

	/*
	 * public int getEMPNOID() { return EMPNOID; }
	 * 
	 * public void setEMPNOID(int eMPNOID) { EMPNOID = eMPNOID; }
	 */

	public String getCLEARING_AGENT_ID() {
		return CLEARING_AGENT_ID;
	}

	public void setCLEARING_AGENT_ID(String lEAVETYPEID) {
		CLEARING_AGENT_ID = lEAVETYPEID;
	}

	public int getTRANSPORTID() {
		return TRANSPORTID;
	}

	public void setTRANSPORTID(int tOTALENTITLEMENT) {
		TRANSPORTID = tOTALENTITLEMENT;
	}

	public String getCONTACTNAME() {
		return CONTACTNAME;
	}

	public void setCONTACTNAME(String lEAVEBALANCE) {
		CONTACTNAME = lEAVEBALANCE;
	}

	public String getTELNO() {
		return TELNO;
	}

	public void setTELNO(String lEAVEYEAR) {
		TELNO = lEAVEYEAR;
	}

	public String getEMAIL() {
		return EMAIL;
	}

	public void setEMAIL(String nOTE) {
		EMAIL = nOTE;
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
		return "ClearingAgentTypeDET [PLANT=" + PLANT + ", ID=" + ID + ", CLEARING_AGENT_ID="
				+ CLEARING_AGENT_ID + ", TRANSPORTID=" + TRANSPORTID + ", CONTACTNAME=" + CONTACTNAME
				+ ", TELNO=" + TELNO + ", EMAIL=" + EMAIL + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT
				+ ", UPBY=" + UPBY + "]";
	}
	
	
	
}
