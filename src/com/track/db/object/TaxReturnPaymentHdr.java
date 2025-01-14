package com.track.db.object;

public class TaxReturnPaymentHdr {
	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="TAXHDR_ID")
	private int TAXHDR_ID;
	
	@DBTable(columnName ="STATUS")
	private String STATUS;
	
	@DBTable(columnName ="TAX_RETURN")
	private String TAX_RETURN;
	
	@DBTable(columnName ="TOTAL_TAXPAYABLE")
	private Double TOTAL_TAXPAYABLE=(double) 0;
	
	@DBTable(columnName ="TOTAL_TAXRECLAIMABLE")
	private Double TOTAL_TAXRECLAIMABLE=(double) 0;
	
	@DBTable(columnName ="BALANCEDUE")
	private Double BALANCEDUE=(double) 0;
	
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

	public int getTAXHDR_ID() {
		return TAXHDR_ID;
	}

	public void setTAXHDR_ID(int tAXHDR_ID) {
		TAXHDR_ID = tAXHDR_ID;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getTAX_RETURN() {
		return TAX_RETURN;
	}

	public void setTAX_RETURN(String tAX_RETURN) {
		TAX_RETURN = tAX_RETURN;
	}

	public Double getTOTAL_TAXPAYABLE() {
		return TOTAL_TAXPAYABLE;
	}

	public void setTOTAL_TAXPAYABLE(Double tOTAL_TAXPAYABLE) {
		TOTAL_TAXPAYABLE = tOTAL_TAXPAYABLE;
	}

	public Double getTOTAL_TAXRECLAIMABLE() {
		return TOTAL_TAXRECLAIMABLE;
	}

	public void setTOTAL_TAXRECLAIMABLE(Double tOTAL_TAXRECLAIMABLE) {
		TOTAL_TAXRECLAIMABLE = tOTAL_TAXRECLAIMABLE;
	}

	public Double getBALANCEDUE() {
		return BALANCEDUE;
	}

	public void setBALANCEDUE(Double bALANCEDUE) {
		BALANCEDUE = bALANCEDUE;
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

	
	
	
}
