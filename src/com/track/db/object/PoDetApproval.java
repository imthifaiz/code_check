package com.track.db.object;

import java.math.BigDecimal;

public class PoDetApproval {
	
	@DBTable(columnName ="UKEY")   
	private String UKEY;

	@DBTable(columnName ="PLANT")   
	private String PLANT;

	@DBTable(columnName ="PONO")   
	private String PONO;

	@DBTable(columnName ="POLNNO")   
	private int POLNNO;

	@DBTable(columnName ="LNSTAT")   
	private String LNSTAT;

	@DBTable(columnName ="ITEM")   
	private String ITEM;

	@DBTable(columnName ="ItemDesc")   
	private String ItemDesc;

	@DBTable(columnName ="TRANDATE")   
	private String TRANDATE;

	@DBTable(columnName ="UNITCOST")   
	private double UNITCOST;

	@DBTable(columnName ="QTYOR")   
	private BigDecimal QTYOR;

	@DBTable(columnName ="QTYRC")   
	private BigDecimal QTYRC;

	@DBTable(columnName ="UNITMO")   
	private String UNITMO;

	@DBTable(columnName ="CRAT")   
	private String CRAT;

	@DBTable(columnName ="CRBY")   
	private String CRBY;

	@DBTable(columnName ="UPAT")   
	private String UPAT;

	@DBTable(columnName ="UPBY")   
	private String UPBY;

	@DBTable(columnName ="USERFLD1")   
	private String USERFLD1;

	@DBTable(columnName ="USERFLD2")   
	private String USERFLD2;

	@DBTable(columnName ="USERFLD3")   
	private String USERFLD3;

	@DBTable(columnName ="USERFLD4")   
	private String USERFLD4;
	
	@DBTable(columnName ="COMMENT1")   
	private String COMMENT1;

	@DBTable(columnName ="CURRENCYUSEQT")   
	private double CURRENCYUSEQT;

	@DBTable(columnName ="PRODGST")   
	private double PRODGST;

	@DBTable(columnName ="PRODUCTDELIVERYDATE")   
	private String PRODUCTDELIVERYDATE;
	
	@DBTable(columnName ="DISCOUNT")   
	private double DISCOUNT;
	
	@DBTable(columnName ="DISCOUNT_TYPE")   
	private String DISCOUNT_TYPE;
	
	@DBTable(columnName ="ACCOUNT_NAME")   
	private String ACCOUNT_NAME;
	
	@DBTable(columnName ="TAX_TYPE")   
	private String TAX_TYPE;
	
	@DBTable(columnName ="UNITCOST_AOD")   
	private double UNITCOST_AOD;
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="POESTNO")   
	private String POESTNO;

	@DBTable(columnName ="POESTLNNO")   
	private int POESTLNNO;

	public String getUKEY() {
		return UKEY;
	}

	public void setUKEY(String uKEY) {
		UKEY = uKEY;
	}

	public String getPLANT() {
		return PLANT;
	}

	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}

	public String getPONO() {
		return PONO;
	}

	public void setPONO(String pONO) {
		PONO = pONO;
	}

	public int getPOLNNO() {
		return POLNNO;
	}

	public void setPOLNNO(int pOLNNO) {
		POLNNO = pOLNNO;
	}

	public String getLNSTAT() {
		return LNSTAT;
	}

	public void setLNSTAT(String lNSTAT) {
		LNSTAT = lNSTAT;
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

	public double getUNITCOST() {
		return UNITCOST;
	}

	public void setUNITCOST(double uNITCOST) {
		UNITCOST = uNITCOST;
	}

	public BigDecimal getQTYOR() {
		return QTYOR;
	}

	public void setQTYOR(BigDecimal qTYOR) {
		QTYOR = qTYOR;
	}

	public BigDecimal getQTYRC() {
		return QTYRC;
	}

	public void setQTYRC(BigDecimal qTYRC) {
		QTYRC = qTYRC;
	}

	public String getUNITMO() {
		return UNITMO;
	}

	public void setUNITMO(String uNITMO) {
		UNITMO = uNITMO;
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

	public String getUSERFLD1() {
		return USERFLD1;
	}

	public void setUSERFLD1(String uSERFLD1) {
		USERFLD1 = uSERFLD1;
	}

	public String getUSERFLD2() {
		return USERFLD2;
	}

	public void setUSERFLD2(String uSERFLD2) {
		USERFLD2 = uSERFLD2;
	}

	public String getUSERFLD3() {
		return USERFLD3;
	}

	public void setUSERFLD3(String uSERFLD3) {
		USERFLD3 = uSERFLD3;
	}

	public String getUSERFLD4() {
		return USERFLD4;
	}

	public void setUSERFLD4(String uSERFLD4) {
		USERFLD4 = uSERFLD4;
	}

	public String getCOMMENT1() {
		return COMMENT1;
	}

	public void setCOMMENT1(String cOMMENT1) {
		COMMENT1 = cOMMENT1;
	}

	public double getCURRENCYUSEQT() {
		return CURRENCYUSEQT;
	}

	public void setCURRENCYUSEQT(double cURRENCYUSEQT) {
		CURRENCYUSEQT = cURRENCYUSEQT;
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

	public double getUNITCOST_AOD() {
		return UNITCOST_AOD;
	}

	public void setUNITCOST_AOD(double uNITCOST_AOD) {
		UNITCOST_AOD = uNITCOST_AOD;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getPOESTNO() {
		return POESTNO;
	}

	public void setPOESTNO(String pOESTNO) {
		POESTNO = pOESTNO;
	}

	public int getPOESTLNNO() {
		return POESTLNNO;
	}

	public void setPOESTLNNO(int pOESTLNNO) {
		POESTLNNO = pOESTLNNO;
	}

	@Override
	public String toString() {
		return "PoDetApproval [UKEY=" + UKEY + ", PLANT=" + PLANT + ", PONO=" + PONO + ", POLNNO=" + POLNNO
				+ ", LNSTAT=" + LNSTAT + ", ITEM=" + ITEM + ", ItemDesc=" + ItemDesc + ", TRANDATE=" + TRANDATE
				+ ", UNITCOST=" + UNITCOST + ", QTYOR=" + QTYOR + ", QTYRC=" + QTYRC + ", UNITMO=" + UNITMO + ", CRAT="
				+ CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + ", USERFLD1=" + USERFLD1
				+ ", USERFLD2=" + USERFLD2 + ", USERFLD3=" + USERFLD3 + ", USERFLD4=" + USERFLD4 + ", COMMENT1="
				+ COMMENT1 + ", CURRENCYUSEQT=" + CURRENCYUSEQT + ", PRODGST=" + PRODGST + ", PRODUCTDELIVERYDATE="
				+ PRODUCTDELIVERYDATE + ", DISCOUNT=" + DISCOUNT + ", DISCOUNT_TYPE=" + DISCOUNT_TYPE
				+ ", ACCOUNT_NAME=" + ACCOUNT_NAME + ", TAX_TYPE=" + TAX_TYPE + ", UNITCOST_AOD=" + UNITCOST_AOD
				+ ", ID=" + ID + ", POESTNO=" + POESTNO + ", POESTLNNO=" + POESTLNNO + "]";
	}
	
	

}
