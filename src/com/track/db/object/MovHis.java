package com.track.db.object;

import java.math.BigDecimal;

public class MovHis {

	@DBTable(columnName ="PLANT")     
	private String PLANT;

	@DBTable(columnName ="DIRTYPE")     
	private String DIRTYPE;

	@DBTable(columnName ="RECID")     
	private String RECID;

	@DBTable(columnName ="LNNO")     
	private String LNNO;

	@DBTable(columnName ="MOVTID")     
	private String MOVTID;

	@DBTable(columnName ="JOBNUM")     
	private String JOBNUM;

	@DBTable(columnName ="ORDNUM")     
	private String ORDNUM;

	@DBTable(columnName ="CUSTNO")     
	private String CUSTNO;

	@DBTable(columnName ="CUSTOMER")     
	private String CUSTOMER;

	@DBTable(columnName ="TRANDATE")     
	private String TRANDATE;

	@DBTable(columnName ="PONO")     
	private String PONO;

	@DBTable(columnName ="PSTDT")     
	private String PSTDT;

	@DBTable(columnName ="ITEM")     
	private String ITEM;

	@DBTable(columnName ="CLASS")     
	private String CLASS;

	@DBTable(columnName ="ATTRIB")     
	private String ATTRIB;

	@DBTable(columnName ="ATTDESC")     
	private String ATTDESC;

	@DBTable(columnName ="VENDNO")     
	private String VENDNO;

	@DBTable(columnName ="QTY")     
	private BigDecimal QTY;

	@DBTable(columnName ="QTYPA")     
	private BigDecimal QTYPA;

	@DBTable(columnName ="BATNO")     
	private String BATNO;

	@DBTable(columnName ="VENDBAT")     
	private String VENDBAT;

	@DBTable(columnName ="UOM")     
	private String UOM;

	@DBTable(columnName ="SERNO")     
	private String SERNO;

	@DBTable(columnName ="SLED")     
	private String SLED;

	@DBTable(columnName ="STATUS")     
	private String STATUS;

	@DBTable(columnName ="WHID")     
	private String WHID;

	@DBTable(columnName ="LOC")     
	private String LOC;

	@DBTable(columnName ="DEFBIN")     
	private String DEFBIN;

	@DBTable(columnName ="ASGMTNO")     
	private String ASGMTNO;

	@DBTable(columnName ="ASGMTDESC")     
	private String ASGMTDESC;

	@DBTable(columnName ="ASGMTCAT")     
	private String ASGMTCAT;

	@DBTable(columnName ="CRAT")     
	private String CRAT;

	@DBTable(columnName ="CRBY")     
	private String CRBY;

	@DBTable(columnName ="UPAT")     
	private String UPAT;

	@DBTable(columnName ="UPBY")     
	private String UPBY;

	@DBTable(columnName ="REMARKS")     
	private String REMARKS;

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
	private double USERDBL1;

	@DBTable(columnName ="USERDBL2")     
	private double USERDBL2;

	@DBTable(columnName ="USERDBL3")     
	private double USERDBL3;

	@DBTable(columnName ="USERDBL4")     
	private double USERDBL4;

	@DBTable(columnName ="USERDBL5")     
	private double USERDBL5;

	@DBTable(columnName ="USERDBL6")     
	private double USERDBL6;
	
	@DBTable(columnName ="ID")
	private int ID;

	public String getPLANT() {
		return PLANT;
	}

	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}

	public String getDIRTYPE() {
		return DIRTYPE;
	}

	public void setDIRTYPE(String dIRTYPE) {
		DIRTYPE = dIRTYPE;
	}

	public String getRECID() {
		return RECID;
	}

	public void setRECID(String rECID) {
		RECID = rECID;
	}

	public String getLNNO() {
		return LNNO;
	}

	public void setLNNO(String lNNO) {
		LNNO = lNNO;
	}

	public String getMOVTID() {
		return MOVTID;
	}

	public void setMOVTID(String mOVTID) {
		MOVTID = mOVTID;
	}

	public String getJOBNUM() {
		return JOBNUM;
	}

	public void setJOBNUM(String jOBNUM) {
		JOBNUM = jOBNUM;
	}

	public String getORDNUM() {
		return ORDNUM;
	}

	public void setORDNUM(String oRDNUM) {
		ORDNUM = oRDNUM;
	}

	public String getCUSTNO() {
		return CUSTNO;
	}

	public void setCUSTNO(String cUSTNO) {
		CUSTNO = cUSTNO;
	}

	public String getCUSTOMER() {
		return CUSTOMER;
	}

	public void setCUSTOMER(String cUSTOMER) {
		CUSTOMER = cUSTOMER;
	}

	public String getTRANDATE() {
		return TRANDATE;
	}

	public void setTRANDATE(String tRANDATE) {
		TRANDATE = tRANDATE;
	}

	public String getPONO() {
		return PONO;
	}

	public void setPONO(String pONO) {
		PONO = pONO;
	}

	public String getPSTDT() {
		return PSTDT;
	}

	public void setPSTDT(String pSTDT) {
		PSTDT = pSTDT;
	}

	public String getITEM() {
		return ITEM;
	}

	public void setITEM(String iTEM) {
		ITEM = iTEM;
	}

	public String getCLASS() {
		return CLASS;
	}

	public void setCLASS(String cLASS) {
		CLASS = cLASS;
	}

	public String getATTRIB() {
		return ATTRIB;
	}

	public void setATTRIB(String aTTRIB) {
		ATTRIB = aTTRIB;
	}

	public String getATTDESC() {
		return ATTDESC;
	}

	public void setATTDESC(String aTTDESC) {
		ATTDESC = aTTDESC;
	}

	public String getVENDNO() {
		return VENDNO;
	}

	public void setVENDNO(String vENDNO) {
		VENDNO = vENDNO;
	}

	public BigDecimal getQTY() {
		return QTY;
	}

	public void setQTY(BigDecimal qTY) {
		QTY = qTY;
	}

	public BigDecimal getQTYPA() {
		return QTYPA;
	}

	public void setQTYPA(BigDecimal qTYPA) {
		QTYPA = qTYPA;
	}

	public String getBATNO() {
		return BATNO;
	}

	public void setBATNO(String bATNO) {
		BATNO = bATNO;
	}

	public String getVENDBAT() {
		return VENDBAT;
	}

	public void setVENDBAT(String vENDBAT) {
		VENDBAT = vENDBAT;
	}

	public String getUOM() {
		return UOM;
	}

	public void setUOM(String uOM) {
		UOM = uOM;
	}

	public String getSERNO() {
		return SERNO;
	}

	public void setSERNO(String sERNO) {
		SERNO = sERNO;
	}

	public String getSLED() {
		return SLED;
	}

	public void setSLED(String sLED) {
		SLED = sLED;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getWHID() {
		return WHID;
	}

	public void setWHID(String wHID) {
		WHID = wHID;
	}

	public String getLOC() {
		return LOC;
	}

	public void setLOC(String lOC) {
		LOC = lOC;
	}

	public String getDEFBIN() {
		return DEFBIN;
	}

	public void setDEFBIN(String dEFBIN) {
		DEFBIN = dEFBIN;
	}

	public String getASGMTNO() {
		return ASGMTNO;
	}

	public void setASGMTNO(String aSGMTNO) {
		ASGMTNO = aSGMTNO;
	}

	public String getASGMTDESC() {
		return ASGMTDESC;
	}

	public void setASGMTDESC(String aSGMTDESC) {
		ASGMTDESC = aSGMTDESC;
	}

	public String getASGMTCAT() {
		return ASGMTCAT;
	}

	public void setASGMTCAT(String aSGMTCAT) {
		ASGMTCAT = aSGMTCAT;
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

	public String getREMARKS() {
		return REMARKS;
	}

	public void setREMARKS(String rEMARKS) {
		REMARKS = rEMARKS;
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

	public double getUSERDBL1() {
		return USERDBL1;
	}

	public void setUSERDBL1(double uSERDBL1) {
		USERDBL1 = uSERDBL1;
	}

	public double getUSERDBL2() {
		return USERDBL2;
	}

	public void setUSERDBL2(double uSERDBL2) {
		USERDBL2 = uSERDBL2;
	}

	public double getUSERDBL3() {
		return USERDBL3;
	}

	public void setUSERDBL3(double uSERDBL3) {
		USERDBL3 = uSERDBL3;
	}

	public double getUSERDBL4() {
		return USERDBL4;
	}

	public void setUSERDBL4(double uSERDBL4) {
		USERDBL4 = uSERDBL4;
	}

	public double getUSERDBL5() {
		return USERDBL5;
	}

	public void setUSERDBL5(double uSERDBL5) {
		USERDBL5 = uSERDBL5;
	}

	public double getUSERDBL6() {
		return USERDBL6;
	}

	public void setUSERDBL6(double uSERDBL6) {
		USERDBL6 = uSERDBL6;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	@Override
	public String toString() {
		return "MovHis [PLANT=" + PLANT + ", DIRTYPE=" + DIRTYPE + ", RECID=" + RECID + ", LNNO=" + LNNO + ", MOVTID="
				+ MOVTID + ", JOBNUM=" + JOBNUM + ", ORDNUM=" + ORDNUM + ", CUSTNO=" + CUSTNO + ", CUSTOMER=" + CUSTOMER
				+ ", TRANDATE=" + TRANDATE + ", PONO=" + PONO + ", PSTDT=" + PSTDT + ", ITEM=" + ITEM + ", CLASS="
				+ CLASS + ", ATTRIB=" + ATTRIB + ", ATTDESC=" + ATTDESC + ", VENDNO=" + VENDNO + ", BATNO=" + BATNO
				+ ", VENDBAT=" + VENDBAT + ", UOM=" + UOM + ", SERNO=" + SERNO + ", SLED=" + SLED + ", STATUS=" + STATUS
				+ ", WHID=" + WHID + ", LOC=" + LOC + ", DEFBIN=" + DEFBIN + ", ASGMTNO=" + ASGMTNO + ", ASGMTDESC="
				+ ASGMTDESC + ", ASGMTCAT=" + ASGMTCAT + ", CRAT=" + CRAT + ", CRBY=" + CRBY + ", UPAT=" + UPAT
				+ ", UPBY=" + UPBY + ", REMARKS=" + REMARKS + ", RECSTAT=" + RECSTAT + ", USERFLD1=" + USERFLD1
				+ ", USERFLD2=" + USERFLD2 + ", USERFLD3=" + USERFLD3 + ", USERFLD4=" + USERFLD4 + ", USERFLD5="
				+ USERFLD5 + ", USERFLD6=" + USERFLD6 + ", USERFLG1=" + USERFLG1 + ", USERFLG2=" + USERFLG2
				+ ", USERFLG3=" + USERFLG3 + ", USERFLG4=" + USERFLG4 + ", USERFLG5=" + USERFLG5 + ", USERFLG6="
				+ USERFLG6 + ", USERTIME1=" + USERTIME1 + ", USERTIME2=" + USERTIME2 + ", USERTIME3=" + USERTIME3
				+ ", USERDBL1=" + USERDBL1 + ", USERDBL2=" + USERDBL2 + ", USERDBL3=" + USERDBL3 + ", USERDBL4="
				+ USERDBL4 + ", USERDBL5=" + USERDBL5 + ", USERDBL6=" + USERDBL6 + ",ID = "+ID+"]";
	}

	
	
}
