package com.track.db.object;

public class TaxReturnFillHdr {
	
	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="COUNTRY_CODE")
	private String COUNTRY_CODE;
	
	@DBTable(columnName ="STATUS")
	private String STATUS;
	
	@DBTable(columnName ="FROM_DATE")
	private String FROM_DATE;
	
	@DBTable(columnName ="TO_DATE")
	private String TO_DATE;
	
	@DBTable(columnName ="REPORTING_PERIOD")
	private String REPORTING_PERIOD;
	
	@DBTable(columnName ="FILED_ON")
	private String FILED_ON;
	
	@DBTable(columnName ="PAYMENTDUE_ON")
	private String PAYMENTDUE_ON;
	
	@DBTable(columnName ="TAX_BASIS")
	private String TAX_BASIS;
	
	@DBTable(columnName ="TOTAL_TAXPAYABLE")
	private Double TOTAL_TAXPAYABLE=(double) 0;
	
	@DBTable(columnName ="TOTAL_TAXRECLAIMABLE")
	private Double TOTAL_TAXRECLAIMABLE=(double) 0;
	
	@DBTable(columnName ="BALANCEDUE")
	private Double BALANCEDUE=(double) 0;
	
	@DBTable(columnName ="TAXPREVIOUSINCLUDE")
	private boolean TAXPREVIOUSINCLUDED;
	
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

	public String getCOUNTRY_CODE() {
		return COUNTRY_CODE;
	}

	public void setCOUNTRY_CODE(String cOUNTRY_CODE) {
		COUNTRY_CODE = cOUNTRY_CODE;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getFROM_DATE() {
		return FROM_DATE;
	}

	public void setFROM_DATE(String fROM_DATE) {
		FROM_DATE = fROM_DATE;
	}

	public String getTO_DATE() {
		return TO_DATE;
	}

	public void setTO_DATE(String tO_DATE) {
		TO_DATE = tO_DATE;
	}

	public String getREPORTING_PERIOD() {
		return REPORTING_PERIOD;
	}

	public void setREPORTING_PERIOD(String rEPORTING_PERIOD) {
		REPORTING_PERIOD = rEPORTING_PERIOD;
	}

	public String getFILED_ON() {
		return FILED_ON;
	}

	public void setFILED_ON(String fILED_ON) {
		FILED_ON = fILED_ON;
	}

	public String getPAYMENTDUE_ON() {
		return PAYMENTDUE_ON;
	}

	public void setPAYMENTDUE_ON(String pAYMENTDUE_ON) {
		PAYMENTDUE_ON = pAYMENTDUE_ON;
	}

	public String getTAX_BASIS() {
		return TAX_BASIS;
	}

	public void setTAX_BASIS(String tAX_BASIS) {
		TAX_BASIS = tAX_BASIS;
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

	public boolean isTAXPREVIOUSINCLUDED() {
		return TAXPREVIOUSINCLUDED;
	}

	public void setTAXPREVIOUSINCLUDED(boolean tAXPREVIOUSINCLUDED) {
		TAXPREVIOUSINCLUDED = tAXPREVIOUSINCLUDED;
	}
	
	

}
