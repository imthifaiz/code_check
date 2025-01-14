package com.track.db.object;

import java.math.BigDecimal;

public class DoDetApproval {
	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="DONO")
	private String DONO;
	
	@DBTable(columnName ="DOLNNO")
	private int DOLNNO;
	
	@DBTable(columnName ="PickStatus")
	private String PickStatus;
	
	@DBTable(columnName ="LNSTAT")
	private String LNSTAT;
	
	@DBTable(columnName ="ITEM")
	private String ITEM;
	
	@DBTable(columnName ="ItemDesc")
	private String ItemDesc;
	
	@DBTable(columnName ="TRANDATE")
	private String TRANDATE;
	
	@DBTable(columnName ="TRANTYPE")
	private String TRANTYPE;
	
	@DBTable(columnName ="UNITPRICE")
	private double UNITPRICE;
	
	@DBTable(columnName ="ASNMT")
	private String ASNMT;
	
	@DBTable(columnName ="QTYOR")
	private BigDecimal QTYOR;
	
	@DBTable(columnName ="QTYIS")
	private BigDecimal QTYIS;
	
	@DBTable(columnName ="QtyPick")
	private BigDecimal QtyPick;
	
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
	
	@DBTable(columnName ="USERFLD1")
	private String USERFLD1;
	
	@DBTable(columnName ="USERFLD2")
	private String USERFLD2;

	@DBTable(columnName ="USERFLD3")
	private String USERFLD3;
	
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
	
	@DBTable(columnName ="UNITCOST")
	private double UNITCOST;
	
	@DBTable(columnName ="ADDONTYPE")
	private String ADDONTYPE;
	
	@DBTable(columnName ="ADDONAMOUNT")
	private double ADDONAMOUNT;
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="UKEY") 
	private String UKEY;

	public String getPLANT() {
		return PLANT;
	}

	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}

	public String getDONO() {
		return DONO;
	}

	public void setDONO(String dONO) {
		DONO = dONO;
	}

	public int getDOLNNO() {
		return DOLNNO;
	}

	public void setDOLNNO(int dOLNNO) {
		DOLNNO = dOLNNO;
	}

	public String getPickStatus() {
		return PickStatus;
	}

	public void setPickStatus(String pickStatus) {
		PickStatus = pickStatus;
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

	public String getTRANTYPE() {
		return TRANTYPE;
	}

	public void setTRANTYPE(String tRANTYPE) {
		TRANTYPE = tRANTYPE;
	}

	public double getUNITPRICE() {
		return UNITPRICE;
	}

	public void setUNITPRICE(double uNITPRICE) {
		UNITPRICE = uNITPRICE;
	}

	public String getASNMT() {
		return ASNMT;
	}

	public void setASNMT(String aSNMT) {
		ASNMT = aSNMT;
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

	public BigDecimal getQtyPick() {
		return QtyPick;
	}

	public void setQtyPick(BigDecimal qtyPick) {
		QtyPick = qtyPick;
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

	public double getUNITCOST() {
		return UNITCOST;
	}

	public void setUNITCOST(double uNITCOST) {
		UNITCOST = uNITCOST;
	}

	public String getADDONTYPE() {
		return ADDONTYPE;
	}

	public void setADDONTYPE(String aDDONTYPE) {
		ADDONTYPE = aDDONTYPE;
	}

	public double getADDONAMOUNT() {
		return ADDONAMOUNT;
	}

	public void setADDONAMOUNT(double aDDONAMOUNT) {
		ADDONAMOUNT = aDDONAMOUNT;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public String getUKEY() {
		return UKEY;
	}

	public void setUKEY(String uKEY) {
		UKEY = uKEY;
	}

	@Override
	public String toString() {
		return "DoDetApproval [UKEY=" + UKEY + ",PLANT=" + PLANT + ", DONO=" + DONO + ", DOLNNO=" + DOLNNO + ", PickStatus=" + PickStatus
				+ ", LNSTAT=" + LNSTAT + ", ITEM=" + ITEM + ", ItemDesc=" + ItemDesc + ", TRANDATE=" + TRANDATE
				+ ", TRANTYPE=" + TRANTYPE + ", UNITPRICE=" + UNITPRICE + ", ASNMT=" + ASNMT + ", QTYOR=" + QTYOR
				+ ", QTYIS=" + QTYIS + ", QtyPick=" + QtyPick + ", UNITMO=" + UNITMO + ", DELDATE=" + DELDATE
				+ ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + ", USERFLD1=" + USERFLD1
				+ ", USERFLD2=" + USERFLD2 + ", USERFLD3=" + USERFLD3 + ", CURRENCYUSEQT=" + CURRENCYUSEQT + ", ESTNO="
				+ ESTNO + ", ESTLNNO=" + ESTLNNO + ", PRODGST=" + PRODGST + ", PRODUCTDELIVERYDATE="
				+ PRODUCTDELIVERYDATE + ", ACCOUNT_NAME=" + ACCOUNT_NAME + ", TAX_TYPE=" + TAX_TYPE + ", DISCOUNT="
				+ DISCOUNT + ", DISCOUNT_TYPE=" + DISCOUNT_TYPE + ", UNITCOST=" + UNITCOST + ", ADDONTYPE=" + ADDONTYPE
				+ ", ADDONAMOUNT=" + ADDONAMOUNT + ", ID=" + ID + "]";
	}

	
	
}
