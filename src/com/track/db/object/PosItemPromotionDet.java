package com.track.db.object;

import java.math.BigDecimal;

public class PosItemPromotionDet {

	@DBTable(columnName ="PLANT")   
	private String PLANT;
	
	@DBTable(columnName ="ID")
	private int ID;

	@DBTable(columnName ="LNNO")   
	private int LNNO;
	
	@DBTable(columnName ="HDRID")   
	private int HDRID;
	
	@DBTable(columnName ="BUY_ITEM")   
	private String BUY_ITEM;

	@DBTable(columnName ="BUY_QTY")   
	private String BUY_QTY;
	
	@DBTable(columnName ="GET_ITEM")   
	private String GET_ITEM;
	
	@DBTable(columnName ="GET_QTY")   
	private BigDecimal GET_QTY;

	@DBTable(columnName ="PROMOTION_TYPE")   
	private String PROMOTION_TYPE;
	
	@DBTable(columnName ="PROMOTION")   
	private BigDecimal PROMOTION;
	
	@DBTable(columnName ="LIMIT_OF_USAGE") 
	private double LIMIT_OF_USAGE;

	@DBTable(columnName ="CRAT")   
	private String CRAT;

	@DBTable(columnName ="CRBY")   
	private String CRBY;

	@DBTable(columnName ="UPAT")   
	private String UPAT;

	@DBTable(columnName ="UPBY")   
	private String UPBY;
	
	@DBTable(columnName ="UNITPRICE")   
	private double UNITPRICE;

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
	
	public int getLNNO() {
		return LNNO;
	}

	public void setLNNO(int lNNO) {
		LNNO = lNNO;
	}
	
	public int getHDRID() {
		return HDRID;
	}
	
	public void setHDRID(int hDRID) {
		HDRID = hDRID;
	}

	public String getBUY_ITEM() {
		return BUY_ITEM;
	}

	public void setBUY_ITEM(String bUY_ITEM) {
		BUY_ITEM = bUY_ITEM;
	}
	
	public String getBUY_QTY() {
		return BUY_QTY;
	}

	public void setBUY_QTY(String bUY_QTY) {
		BUY_QTY = bUY_QTY;
	}
	
	public String getGET_ITEM() {
		return GET_ITEM;
	}
	
	public void setGET_ITEM(String gET_ITEM) {
		GET_ITEM = gET_ITEM;
	}
	
	public BigDecimal getGET_QTY() {
		return GET_QTY;
	}
	
	public void setGET_QTY(BigDecimal gET_QTY) {
		GET_QTY = gET_QTY;
	}


	public String getPROMOTION_TYPE() {
		return PROMOTION_TYPE;
	}

	public void setPROMOTION_TYPE(String pROMOTION_TYPE) {
		PROMOTION_TYPE = pROMOTION_TYPE;
	}

	public BigDecimal getPROMOTION() {
		return PROMOTION;
	}

	public void setPROMOTION(BigDecimal pROMOTION) {
		PROMOTION = pROMOTION;
	}
	
	public double getLIMIT_OF_USAGE() {
		return LIMIT_OF_USAGE;
	}

	public void setLIMIT_OF_USAGE(double lIMIT_OF_USAGE) {
		LIMIT_OF_USAGE = lIMIT_OF_USAGE;
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
	
	public double getUNITPRICE() {
		return UNITPRICE;
	}
	
	public void setUNITPRICE(double uNITPRICE) {
		UNITPRICE = uNITPRICE;
	}
	
	@Override
	public String toString() {
		return "PosItemPromotionDet [PLANT=" + PLANT + ", ID=" + ID + ", LNNO=" + LNNO + ", HDRID="
				+ HDRID + ", BUY_ITEM=" + BUY_ITEM + ", BUY_QTY=" + BUY_QTY + ", GET_ITEM=" + GET_ITEM + ", GET_QTY="
				+ GET_QTY + ", PROMOTION_TYPE=" + PROMOTION_TYPE + ", PROMOTION=" + PROMOTION+ ", LIMIT_OF_USAGE=" + LIMIT_OF_USAGE + ", CRAT=" + CRAT
				+ ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + ", UNITPRICE=" + UNITPRICE + "]";
	}
	
	

	
}
