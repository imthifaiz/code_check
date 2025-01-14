/*CREATED BY NAVAS*/
/*dec7*/
package com.track.db.object;

import java.math.BigDecimal;

public class estDet {
	@DBTable(columnName ="PLANT")
	private String PLANT;
	

	
	@DBTable(columnName ="ITEM")
	private String ITEM;
	
	@DBTable(columnName ="ItemDesc")
	private String ItemDesc;
	
	@DBTable(columnName ="TRANDATE")
	private String TRANDATE;

	
	@DBTable(columnName ="UNITPRICE")
	private double UNITPRICE;
	
	@DBTable(columnName ="QTYOR")
	private BigDecimal QTYOR;
	
	@DBTable(columnName ="QTYIS")
	private BigDecimal QTYIS;
	
	
	@DBTable(columnName ="UNITMO")
	private String UNITMO;
	
	@DBTable(columnName ="DELDATE")
	private String DELDATE;
	
	@DBTable(columnName ="CRAT")
	private String CRAT;
	
	@DBTable(columnName ="CRBY")
	private String CRBY;
	
	@DBTable(columnName ="UPAT")
	private String UPAT;
	
	@DBTable(columnName ="UPBY")
	private String UPBY;
	
	
	
	@DBTable(columnName ="CURRENCYUSEQT")
	private double CURRENCYUSEQT;
	
	@DBTable(columnName ="ESTNO")
	private String ESTNO;
	
	@DBTable(columnName ="ESTLNNO")
	private int ESTLNNO;
	
	@DBTable(columnName ="PRODGST")
	private double PRODGST;
	
	@DBTable(columnName ="PRODUCTDELIVERYDATE")
	private String PRODUCTDELIVERYDATE;
	
	@DBTable(columnName ="ACCOUNT_NAME")
	private String ACCOUNT_NAME;
	
	@DBTable(columnName ="TAX_TYPE")
	private String TAX_TYPE;
	
	@DBTable(columnName ="DISCOUNT")
	private double DISCOUNT;
	
	@DBTable(columnName ="DISCOUNT_TYPE")
	private String DISCOUNT_TYPE;

	@DBTable(columnName ="COMMENT1")
	private String COMMENT1;

	@DBTable(columnName ="COMMENT2")
	private String COMMENT2;

	@DBTable(columnName ="STATUS")
	private String STATUS;

	public String getPLANT() {
		return PLANT;
	}

	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}




	public String getITEM() {
		return ITEM;
	}

	public void setITEM(String iTEM) {
		ITEM = iTEM;
	}

	public String getItemDesc() {
		return ItemDesc;
	}

	public void setItemDesc(String itemDesc) {
		ItemDesc = itemDesc;
	}

	public String getTRANDATE() {
		return TRANDATE;
	}

	public void setTRANDATE(String tRANDATE) {
		TRANDATE = tRANDATE;
	}

	
	public double getUNITPRICE() {
		return UNITPRICE;
	}

	public void setUNITPRICE(double uNITPRICE) {
		UNITPRICE = uNITPRICE;
	}

	public BigDecimal getQTYOR() {
		return QTYOR;
	}

	public void setQTYOR(BigDecimal qTYOR) {
		QTYOR = qTYOR;
	}

	public BigDecimal getQTYIS() {
		return QTYIS;
	}

	public void setQTYIS(BigDecimal qTYIS) {
		QTYIS = qTYIS;
	}


	public String getUNITMO() {
		return UNITMO;
	}

	public void setUNITMO(String uNITMO) {
		UNITMO = uNITMO;
	}

	public String getDELDATE() {
		return DELDATE;
	}

	public void setDELDATE(String dELDATE) {
		DELDATE = dELDATE;
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



	public double getCURRENCYUSEQT() {
		return CURRENCYUSEQT;
	}

	public void setCURRENCYUSEQT(double cURRENCYUSEQT) {
		CURRENCYUSEQT = cURRENCYUSEQT;
	}

	public String getESTNO() {
		return ESTNO;
	}

	public void setESTNO(String eSTNO) {
		ESTNO = eSTNO;
	}

	public int getESTLNNO() {
		return ESTLNNO;
	}

	public void setESTLNNO(int eSTLNNO) {
		ESTLNNO = eSTLNNO;
	}

	public double getPRODGST() {
		return PRODGST;
	}

	public void setPRODGST(double pRODGST) {
		PRODGST = pRODGST;
	}

	public String getPRODUCTDELIVERYDATE() {
		return PRODUCTDELIVERYDATE;
	}

	public void setPRODUCTDELIVERYDATE(String pRODUCTDELIVERYDATE) {
		PRODUCTDELIVERYDATE = pRODUCTDELIVERYDATE;
	}

	public String getACCOUNT_NAME() {
		return ACCOUNT_NAME;
	}

	public void setACCOUNT_NAME(String aCCOUNT_NAME) {
		ACCOUNT_NAME = aCCOUNT_NAME;
	}

	public String getTAX_TYPE() {
		return TAX_TYPE;
	}

	public void setTAX_TYPE(String tAX_TYPE) {
		TAX_TYPE = tAX_TYPE;
	}

	public double getDISCOUNT() {
		return DISCOUNT;
	}

	public void setDISCOUNT(double dISCOUNT) {
		DISCOUNT = dISCOUNT;
	}

	public String getDISCOUNT_TYPE() {
		return DISCOUNT_TYPE;
	}

	public void setDISCOUNT_TYPE(String dISCOUNT_TYPE) {
		DISCOUNT_TYPE = dISCOUNT_TYPE;
	}
	

	public void setCOMMENT1(String cOMMENT1) {
		COMMENT1 = cOMMENT1;
	}

	public String getCOMMENT1() {
		return COMMENT1;
	}
	
	public void setCOMMENT2(String cOMMENT2) {
		COMMENT2 = cOMMENT2;
	}

	public String getCOMMENT2() {
		return COMMENT2;
	}
	
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}


	public String getSTATUS() {
		return STATUS;
	}

	
	public String toString() {
		return "estDet [PLANT=" + PLANT + ", ESTNO=" + ESTNO + ", ESTLNNO=" + ESTLNNO 
				+ ", ITEM=" + ITEM + ", ItemDesc=" + ItemDesc + ", TRANDATE=" + TRANDATE
				+  ", UNITPRICE=" + UNITPRICE +  ", QTYOR=" + QTYOR
				+ ", QTYIS=" + QTYIS +  ", UNITMO=" + UNITMO + ", DELDATE=" + DELDATE
				+ ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY
				+ ", CURRENCYUSEQT=" + CURRENCYUSEQT + ", ESTNO="
				+ ESTNO + ", ESTLNNO=" + ESTLNNO + ", PRODGST=" + PRODGST + ", PRODUCTDELIVERYDATE="
				+ PRODUCTDELIVERYDATE + ", ACCOUNT_NAME=" + ACCOUNT_NAME + ", TAX_TYPE=" + TAX_TYPE + ", DISCOUNT="
				+ DISCOUNT + ", DISCOUNT_TYPE=" + DISCOUNT_TYPE + ", COMMENT1=" + COMMENT1 
				+ ", COMMENT2=" + COMMENT2 + ", STATUS=" + STATUS + "]";
	}

	
}
