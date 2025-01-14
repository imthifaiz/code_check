package com.track.db.object;

public class PosPrd_BrandPromotionHdr {

	@DBTable(columnName ="PLANT") 
	private String PLANT;
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="PROMOTION_NAME")
	private String PROMOTION_NAME;
	
	@DBTable(columnName ="PROMOTION_DESC")   
	private String PROMOTION_DESC;
	
	@DBTable(columnName ="CUSTOMER_TYPE_ID") 
	private String CUSTOMER_TYPE_ID;

	@DBTable(columnName ="START_DATE") 
	private String START_DATE;

	@DBTable(columnName ="START_TIME") 
	private String START_TIME;

	@DBTable(columnName ="END_DATE") 
	private String END_DATE;

	@DBTable(columnName ="END_TIME") 
	private String END_TIME;

//	@DBTable(columnName ="LIMIT_OF_USAGE") 
//	private double LIMIT_OF_USAGE;

	@DBTable(columnName ="BY_VALUE") 
	private int BY_VALUE;
	
	@DBTable(columnName ="NOTES") 
	private String NOTES;

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
	
	@DBTable(columnName ="OUTLET")   
	private String OUTLET;

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

	public String getPROMOTION_NAME() {
		return PROMOTION_NAME;
	}

	public void setPROMOTION_NAME(String pROMOTION_NAME) {
		PROMOTION_NAME = pROMOTION_NAME;
	}

	public String getPROMOTION_DESC() {
		return PROMOTION_DESC;
	}

	public void setPROMOTION_DESC(String pROMOTION_DESC) {
		PROMOTION_DESC = pROMOTION_DESC;
	}
	
	public String getOUTLET() {
		return OUTLET;
	}
	
	public void setOUTLET(String OUTLETs) {
		OUTLET = OUTLETs;
	}

	public String getCUSTOMER_TYPE_ID() {
		return CUSTOMER_TYPE_ID;
	}

	public void setCUSTOMER_TYPE_ID(String cUSTOMER_TYPE_ID) {
		CUSTOMER_TYPE_ID = cUSTOMER_TYPE_ID;
	}

	public String getSTART_DATE() {
		return START_DATE;
	}

	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}

	public String getSTART_TIME() {
		return START_TIME;
	}

	public void setSTART_TIME(String sTART_TIME) {
		START_TIME = sTART_TIME;
	}

	public String getEND_DATE() {
		return END_DATE;
	}

	public void setEND_DATE(String eND_DATE) {
		END_DATE = eND_DATE;
	}

	public String getEND_TIME() {
		return END_TIME;
	}

	public void setEND_TIME(String eND_TIME) {
		END_TIME = eND_TIME;
	}
	
//	public double getLIMIT_OF_USAGE() {
//		return LIMIT_OF_USAGE;
//	}
//
//	public void setLIMIT_OF_USAGE(double lIMIT_OF_USAGE) {
//		LIMIT_OF_USAGE = lIMIT_OF_USAGE;
//	}

	public int getBY_VALUE() {
		return BY_VALUE;
	}

	public void setBY_VALUE(int bY_VALUE) {
		BY_VALUE = bY_VALUE;
	}

	public String getNOTES() {
		return NOTES;
	}

	public void setNOTES(String nOTES) {
		NOTES = nOTES;
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
		return "PosPrd_ClassPromotionHdr [PLANT=" + PLANT + ", ID=" + ID + ", PROMOTION_NAME=" + PROMOTION_NAME + ", PROMOTION_DESC=" + PROMOTION_DESC
				+ ", CUSTOMER_TYPE_ID=" + CUSTOMER_TYPE_ID + ", START_DATE=" + START_DATE + ", START_TIME=" + START_TIME + ", END_DATE=" + END_DATE + ", END_TIME=" + END_TIME
				+ ", BY_VALUE=" + BY_VALUE + ", NOTES=" + NOTES + ", IsActive=" + IsActive + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY +",OUTLET=" + OUTLET +"]";
		//		+ ", LIMIT_OF_USAGE=" + LIMIT_OF_USAGE + ", BY_VALUE=" + BY_VALUE + ", NOTES=" + NOTES + ", IsActive=" + IsActive + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY +"]";
	}


}
