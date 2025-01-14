package com.track.db.object;

import java.math.BigDecimal;

public class ToDet {
	@DBTable(columnName ="PLANT")
	private String PLANT;
	
	@DBTable(columnName ="TONO")
	private String TONO;
	
	@DBTable(columnName ="TOLNNO")
	private int TOLNNO;
	
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
	
	@DBTable(columnName ="ASNMT")
	private String ASNMT;
	
	@DBTable(columnName ="QTYOR")
	private BigDecimal QTYOR;
	
	@DBTable(columnName ="QTYRC")
	private BigDecimal QTYRC;
	
	@DBTable(columnName ="QTYAC")
	private BigDecimal QTYAC;
	
	@DBTable(columnName ="QTYRJ")
	private String QTYRJ;
	
	@DBTable(columnName ="LOC")
	private String LOC;
	
	@DBTable(columnName ="WHID")
	private String WHID;
	
	@DBTable(columnName ="POLTYPE")
	private String POLTYPE;
	
	
	@DBTable(columnName ="QtyPick")
	private BigDecimal QtyPick;
	
	@DBTable(columnName ="UNITMO")
	private String UNITMO;
	
	@DBTable(columnName ="DELDATE")
	private String DELDATE;
	
	@DBTable(columnName ="COMMENT1")
	private String COMMENT1;
	
	@DBTable(columnName ="COMMENT2")
	private String COMMENT2;
	
	@DBTable(columnName ="CRAT")
	private String CRAT;
	
	@DBTable(columnName ="CRBY")
	private String CRBY;
	
	@DBTable(columnName ="UPAT")
	private String UPAT;
	
	@DBTable(columnName ="UPBY")
	private String UPBY;
	
	@DBTable(columnName ="RECSTAT")
	private String RECSTAT;
	
	@DBTable(columnName ="USERFLD1")
	private String USERFLD1;
	
	@DBTable(columnName ="USERFLD2")
	private String USERFLD2;
	
	@DBTable(columnName ="USERFLD3")
	private String USERFLD3;

	@DBTable(columnName ="USERFLD4")
	private String USERFLD4;
	
	@DBTable(columnName ="USERFLD5")
	private String USERFLD5;
	
	@DBTable(columnName ="USERFLD6")
	private String USERFLD6;
	
	@DBTable(columnName ="USERFLG1")
	private String USERFLG1;
	
	@DBTable(columnName ="USERFLG2")
	private String USERFLG2;
	
	@DBTable(columnName ="USERFLG3")
	private String USERFLG3;

	@DBTable(columnName ="USERFLG4")
	private String USERFLG4;
	
	@DBTable(columnName ="USERFLG5")
	private String USERFLG5;
	
	@DBTable(columnName ="USERFLG6")
	private String USERFLG6;
	
	@DBTable(columnName ="USERTIME1")
	private String USERTIME1;
	
	@DBTable(columnName ="USERTIME2")
	private String USERTIME2;
	
	@DBTable(columnName ="USERTIME3")
	private String USERTIME3;
	
	@DBTable(columnName ="USERDBL1")
	private String USERDBL1;
	
	@DBTable(columnName ="USERDBL2")
	private String USERDBL2;
	
	@DBTable(columnName ="USERDBL3")
	private String USERDBL3;

	@DBTable(columnName ="USERDBL4")
	private String USERDBL4;
	
	@DBTable(columnName ="USERDBL5")
	private String USERDBL5;
	
	@DBTable(columnName ="USERDBL6")
	private String USERDBL6;
	
	@DBTable(columnName ="UNITPRICE")
	private double UNITPRICE;
		
	@DBTable(columnName ="CURRENCYUSEQT")
	private double CURRENCYUSEQT;
	
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

	public String getPLANT() {
		return PLANT;
	}

	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}

	public String getTONO() {
		return TONO;
	}

	public void setTONO(String tONO) {
		TONO = tONO;
	}

	public int getTOLNNO() {
		return TOLNNO;
	}

	public void setTOLNNO(int tOLNNO) {
		TOLNNO = tOLNNO;
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

	public BigDecimal getQTYRC() {
		return QTYRC;
	}

	public void setQTYRC(BigDecimal qTYRC) {
		QTYRC = qTYRC;
	}

	public BigDecimal getQtyPick() {
		return QtyPick;
	}

	public void setQtyPick(BigDecimal qtyPick) {
		QtyPick = qtyPick;
	}
	/* QTYPICK TO CONTINUE */
	public BigDecimal getQTYAC() {
		return QTYAC;
	}

	public void setQTYAC(BigDecimal qTYAC) {
		QTYAC = qTYAC;
	}

	public String getQTYRJ() {
		return QTYRJ;
	}

	public void setQTYRJ(String qTYRJ) {
		QTYRJ = qTYRJ;
	}

	public String getLOC() {
		return LOC;
	}

	public void setLOC(String lOC) {
		LOC = lOC;
	}

	public String getWHID() {
		return WHID;
	}

	public void setWHID(String wHID) {
		WHID = wHID;
	}

	public String getPOLTYPE() {
		return POLTYPE;
	}

	public void setPOLTYPE(String pOLTYPE) {
		POLTYPE = pOLTYPE;
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
	
	public String getCOMMENT1() {
		return COMMENT1;
	}

	public void setCOMMENT1(String cOMMENT1) {
		COMMENT1 = cOMMENT1;
	}

	public String getCOMMENT2() {
		return COMMENT2;
	}

	public void setCOMMENT2(String cOMMENT2) {
		COMMENT2 = cOMMENT2;
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


	public String getRECSTAT() {
		return RECSTAT;
	}

	public void setRECSTAT(String rECSTAT) {
		RECSTAT = rECSTAT;
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

	public String getUSERFLD5() {
		return USERFLD5;
	}

	public void setUSERFLD5(String uSERFLD5) {
		USERFLD5 = uSERFLD5;
	}

	public String getUSERFLD6() {
		return USERFLD6;
	}

	public void setUSERFLD6(String uSERFLD6) {
		USERFLD6 = uSERFLD6;
	}

	public String getUSERFLG1() {
		return USERFLG1;
	}

	public void setUSERFLG1(String uSERFLG1) {
		USERFLG1 = uSERFLG1;
	}

	public String getUSERFLG2() {
		return USERFLG2;
	}

	public void setUSERFLG2(String uSERFLG2) {
		USERFLG2 = uSERFLG2;
	}

	public String getUSERFLG3() {
		return USERFLG3;
	}

	public void setUSERFLG3(String uSERFLG3) {
		USERFLG3 = uSERFLG3;
	}

	public String getUSERFLG4() {
		return USERFLG4;
	}

	public void setUSERFLG4(String uSERFLG4) {
		USERFLG4 = uSERFLG4;
	}

	public String getUSERFLG5() {
		return USERFLG5;
	}

	public void setUSERFLG5(String uSERFLG5) {
		USERFLG5 = uSERFLG5;
	}

	public String getUSERFLG6() {
		return USERFLG6;
	}

	public void setUSERFLG6(String uSERFLG6) {
		USERFLG6 = uSERFLG6;
	}
	
	public String getUSERTIME1() {
		return USERTIME1;
	}

	public void setUSERTIME1(String uSERTIME1) {
		USERTIME1 = uSERTIME1;
	}

	public String getUSERTIME2() {
		return USERTIME2;
	}

	public void setUSERTIME2(String uSERTIME2) {
		USERTIME2 = uSERTIME2;
	}

	public String getUSERTIME3() {
		return USERTIME3;
	}

	public void setUSERTIME3(String uSERTIME3) {
		USERTIME3 = uSERTIME3;
	}

	public String getUSERDBL1() {
		return USERDBL1;
	}

	public void setUSERDBL1(String uSERDBL1) {
		USERDBL1 = uSERDBL1;
	}

	public String getUSERDBL2() {
		return USERDBL2;
	}

	public void setUSERDBL2(String uSERDBL2) {
		USERDBL2 = uSERDBL2;
	}

	public String getUSERDBL3() {
		return USERDBL3;
	}

	public void setUSERDBL3(String uSERDBL3) {
		USERDBL3 = uSERDBL3;
	}

	public String getUSERDBL4() {
		return USERDBL4;
	}

	public void setUSERDBL4(String uSERDBL4) {
		USERDBL4 = uSERDBL4;
	}

	public String getUSERDBL5() {
		return USERDBL5;
	}

	public void setUSERDBL5(String uSERDBL5) {
		USERDBL5 = uSERDBL5;
	}

	public String getUSERDBL6() {
		return USERDBL6;
	}

	public void setUSERDBL6(String uSERDBL6) {
		USERDBL6 = uSERDBL6;
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

	public String toString() {
		return "ToDet [PLANT=" + PLANT + ", TONO=" + TONO + ", TOLNNO=" + TOLNNO + ", PickStatus=" + PickStatus
				+ ", LNSTAT=" + LNSTAT + ", ITEM=" + ITEM + ", ItemDesc=" + ItemDesc + ", TRANDATE=" + TRANDATE
				+  ", UNITPRICE=" + UNITPRICE + ", ASNMT=" + ASNMT + ", QTYOR=" + QTYOR
				+ ", QTYRC=" + QTYRC + ", QtyPick=" + QtyPick + ", QTYAC=" + QTYAC +  ", QTYRJ=" + QTYRJ 
				+  ", LOC=" + LOC +  ", WHID=" + WHID +  ", POLTYPE=" + POLTYPE + ", UNITMO=" + UNITMO 
				+ ", DELDATE=" + DELDATE +", COMMENT1=" + COMMENT1 + ", COMMENT2=" + COMMENT2  
				+ ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT + ", UPBY=" + UPBY + ", RECSTAT=" + RECSTAT 
				+  ", USERFLD1=" + USERFLD1
				+ ", USERFLD2=" + USERFLD2 + ", USERFLD3=" + USERFLD3 + ", USERFLD4=" + USERFLD4 
				+ ", USERFLD5=" + USERFLD5 +  ", USERFLD6=" + USERFLD6 +  ", USERFLG1=" + USERFLG1 
				+  ", USERFLG2=" + USERFLG2 +  ", USERFLG3=" + USERFLG3 +  ", USERFLG4=" + USERFLG4 
				+  ", USERFLG5=" + USERFLG5 +  ", USERFLG6=" + USERFLG6 +  ", USERTIME1=" + USERTIME1 
				+  ", USERTIME2=" + USERTIME2 +  ", USERTIME3=" + USERTIME3 +  ", USERDBL1=" + USERDBL1 
				+  ", USERDBL2=" + USERDBL2 +  ", USERDBL3=" + USERDBL3 +  ", USERDBL4=" + USERDBL4 
				+  ", USERDBL5=" + USERDBL5 +  ", USERDBL6=" + USERDBL6 + ", CURRENCYUSEQT=" + CURRENCYUSEQT 
				+", PRODGST=" + PRODGST + ", PRODUCTDELIVERYDATE="
				+ PRODUCTDELIVERYDATE + ", ACCOUNT_NAME=" + ACCOUNT_NAME + ", TAX_TYPE=" + TAX_TYPE + ", DISCOUNT="
				+ DISCOUNT + ", DISCOUNT_TYPE=" + DISCOUNT_TYPE + "]";
	}
}
