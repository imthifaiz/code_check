package com.track.db.object;

public class PEPPOL_RECEIVED_DATA {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
		
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="EVENT")
	private String EVENT;
	
	@DBTable(columnName ="DOCID")
	private String DOCID;
	
	@DBTable(columnName ="RECEIVEDAT")
	private String RECEIVEDAT;
	
	@DBTable(columnName ="INVOICEFILEURL")
	private String INVOICEFILEURL;
	
	@DBTable(columnName ="EVIDENCEFILEURL")
	private String EVIDENCEFILEURL;
	
	@DBTable(columnName ="BILLNO")
	private String BILLNO;
	
	@DBTable(columnName ="EXPIRESAT")
	private String EXPIRESAT;
	
	@DBTable(columnName ="BILL_STATUS")
	private short BILL_STATUS;
		
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
	
	public String getDOCID() {
		return DOCID;
	}
	
	public void setDOCID(String dOCID) {
		DOCID = dOCID;
	}
	
	public String getEVENT() {
		return EVENT;
	}
	
	public void setEVENT(String eVENT) {
		EVENT = eVENT;
	}
	
	public String getRECEIVEDAT() {
		return RECEIVEDAT;
	}
	
	public void setRECEIVEDAT(String rECEIVEDAT) {
		RECEIVEDAT = rECEIVEDAT;
	}
	
	public String getINVOICEFILEURL() {
		return INVOICEFILEURL;
	}
	
	public void setINVOICEFILEURL(String iNVOICEFILEURL) {
		INVOICEFILEURL = iNVOICEFILEURL;
	}
	
	public String getEVIDENCEFILEURL() {
		return EVIDENCEFILEURL;
	}
	
	public void setEVIDENCEFILEURL(String eVIDENCEFILEURL) {
		EVIDENCEFILEURL = eVIDENCEFILEURL;
	}
	
	public String getBILLNO() {
		return BILLNO;
	}
	
	public void setBILLNO(String bILLNO) {
		BILLNO = bILLNO;
	}
	
	public String getEXPIRESAT() {
		return EXPIRESAT;
	}
	
	public void setEXPIRESAT(String eXPIRESAT) {
		EXPIRESAT = eXPIRESAT;
	}
	
	public short getBILL_STATUS() {
		return BILL_STATUS;
	}

	public void setBILL_STATUS(short bILL_STATUS) {
		BILL_STATUS = bILL_STATUS;
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
		return "PEPPOL_RECEIVED_DATA [PLANT=" + PLANT + ", ID=" + ID + ", DOCID=" + DOCID + ", EVENT="
				+ EVENT + ", RECEIVEDAT=" + RECEIVEDAT + ",EVIDENCEFILEURL=" 
				+ EVIDENCEFILEURL +",BILLNO=" + BILLNO +",BILL_STATUS=" + BILL_STATUS +",EXPIRESAT=" + EXPIRESAT + ", INVOICEFILEURL=" + INVOICEFILEURL + ","
				+ " CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + "]";
	}

	
	
	
	

}
