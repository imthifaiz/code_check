package com.track.tables;

// importing classes

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.sqlBean;
import com.track.util.MLogger;

public class ITEMMST {
	private boolean printQuery = MLoggerConstant.ITEMMST_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.ITEMMST_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	String sPLANT = "SIS";
	String sITEM = "";
	String sITEMDESC = "";
	String sRANK = "";
	String sITEMTYPE = "";
	String sLENGTH = "";
	String sWIDTH = "";
	String sHEIGHT = "";
	String sVOLUME = "";
	String sAREA = "";
	String sISIZE = "";
	String sRORDLVL = "";
	String sSLIFEIND = "";
	String sTRKIND = "";
	String sQCIND = "";
	String sQUARIND = "";
	String sHAZITEM = "";
	int iSLIFE;
	int iQUARDAYS;
	String sSTKUOM = "";
	String sUNITWGT = "";
	String sCOMMENT1 = "";
	String sCOMMENT2 = "";
	String sCRAT = "";
	String sCRBY = "";
	String sUPAT = "";
	String sUPBY = "";
	String sRECSTAT = "";
	String sUSERFLD1 = "";
	String sUSERFLD2 = "";
	String sUSERFLD3 = "";
	String sUSERFLD4 = "";
	String sUSERFLD5 = "";
	String sUSERFLD6 = "";
	String sUSERFLG1 = "";
	String sUSERFLG2 = "";
	String sUSERFLG3 = "";
	String sUSERFLG4 = "";
	String sUSERFLG5 = "";
	String sUSERFLG6 = "";
	String sUSERTIME1 = "";
	String sUSERTIME2 = "";
	String sUSERTIME3 = "";
	String sNONSTOCKFLAG = "";//added by radhika
	//start code by radhika for pos to add batch on 4 dec 2013
	String sBATCH = "";
	int iBATCHID = -1;
	//end code by radhika for pos to add batch on 4 dec 2013
	float fUSERDBL1;
	float fUSERDBL2;
	float fUSERDBL3;
	float fUSERDBL4;
	float fUSERDBL5;
	float fUSERDBL6;
	float UNITPRICE;
	float MINSPRICE;
	float stkqty ;
	float DISCOUNT;
	float TotalPrice;
	float fGSTTAX;
	float fPRICEWITHTAX;
	float fTotalDiscountAmount=0;
	float fTotalDiscountSubTotal=0;
	float fTotalDiscountTotal=0;
	float fTotalDiscountTax=0;
	
	String empNo = "",refNo="",tranDate="",reasonCode="",remarks="",empName="",loc="",from_loc="",to_loc="";
	
	
	public String getEmpNo()
	{
		return empNo;
	}
	
	public void setEmpNo(String empno)
	{
		empNo = empno;
	}
	
	public String getEmpName()
	{
		return empName;
	}
	
	public void setEmpName(String ename)
	{
		empName = ename;
	}
	
	public String getRefNo()
	{
		return refNo;
	}
	
	public void setRefNo(String refno)
	{
		refNo = refno;
	}
	
	public String getTranDate()
	{
		return tranDate;
	}
	
	public void setTranDate(String tDate)
	{
		tranDate = tDate;
	}
	
	public String getReasonCode()
	{
		return reasonCode;
	}
	
	public void setReasonCode(String rCode)
	{
		reasonCode = rCode;
	}
	
	public String getRemarks()
	{
		return remarks;
	}
	
	public void setRemarks(String remark)
	{
		remarks = remark;
	}
	public String getLoc()
	{
		return loc;
	}
	
	public String getFromLoc()
	{
		return from_loc;
	}
	
	public String getToLoc()
	{
		return to_loc;
	}
	
	public void setFromLoc(String location)
	{
		from_loc = location;
	}
	
	public void setToLoc(String location)
	{
		to_loc = location;
	}
	
	public void setLoc(String location)
	{
		loc = location;
	}
	

	
	public float getTotalPrice() {
		return TotalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		TotalPrice = totalPrice;
	}

	public float getDISCOUNT() {
		return DISCOUNT;
	}

	public void setDISCOUNT(float dISCOUNT) {
		DISCOUNT = dISCOUNT;
	}

	public float getUNITPRICE() {
		return UNITPRICE;
	}

	public void setUNITPRICE(float uNITPRICE) {
		UNITPRICE = uNITPRICE;
	}
	public float getMINSPRICE(){
		return MINSPRICE;
	}
	public void setMINSPRICE(float mINSPRICE){
		MINSPRICE = mINSPRICE;
	}

	public float getStkqty() {
		return stkqty;
	}

	public void setStkqty(float stkqty) {
		this.stkqty = stkqty;
	}

	public String getPLANT() {
		return sPLANT;
	}

	public String getITEM() {
		return sITEM;
	}
	// added by radhika
	public String getNONSTOCKFLAG(){
		return sNONSTOCKFLAG;
	}
	//end
	public String getITEMDESC() {
		return sITEMDESC;
	}

	public String getRANK() {
		return sRANK;
	}

	public String getITEMTYPE() {
		return sITEMTYPE;
	}

	public String getLENGTH() {
		return sLENGTH;
	}

	public String getWIDTH() {
		return sWIDTH;
	}

	public String getHEIGHT() {
		return sHEIGHT;
	}

	public String getVOLUME() {
		return sVOLUME;
	}

	public String getAREA() {
		return sAREA;
	}

	public String getISIZE() {
		return sISIZE;
	}

	public String getRORDLVL() {
		return sRORDLVL;
	}

	public String getSLIFEIND() {
		return sSLIFEIND;
	}

	public String getTRKIND() {
		return sTRKIND;
	}

	public String getQCIND() {
		return sQCIND;
	}

	public String getQUARIND() {
		return sQUARIND;
	}

	public String getHAZITEM() {
		return sHAZITEM;
	}

	public int getSLIFE() {
		return iSLIFE;
	}

	public int getQUARDAYS() {
		return iQUARDAYS;
	}

	public String getSTKUOM() {
		return sSTKUOM;
	}

	public String getUNITWGT() {
		return sUNITWGT;
	}

	public String getCOMMENT1() {
		return sCOMMENT1;
	}

	public String getCOMMENT2() {
		return sCOMMENT2;
	}

	public String getCRAT() {
		return sCRAT;
	}

	public String getCRBY() {
		return sCRBY;
	}

	public String getUPAT() {
		return sUPAT;
	}

	public String getUPBY() {
		return sUPBY;
	}

	public String getRECSTAT() {
		return sRECSTAT;
	}

	public String getUSERFLD1() {
		return sUSERFLD1;
	}

	public String getUSERFLD2() {
		return sUSERFLD2;
	}

	public String getUSERFLD3() {
		return sUSERFLD3;
	}

	public String getUSERFLD4() {
		return sUSERFLD4;
	}

	public String getUSERFLD5() {
		return sUSERFLD5;
	}

	public String getUSERFLD6() {
		return sUSERFLD6;
	}

	public String getUSERFLG1() {
		return sUSERFLG1;
	}

	public String getUSERFLG2() {
		return sUSERFLG2;
	}

	public String getUSERFLG3() {
		return sUSERFLG3;
	}

	public String getUSERFLG4() {
		return sUSERFLG4;
	}

	public String getUSERFLG5() {
		return sUSERFLG5;
	}

	public String getUSERFLG6() {
		return sUSERFLG6;
	}

	public String getUSERTIME1() {
		return sUSERTIME1;
	}

	public String getUSERTIME2() {
		return sUSERTIME2;
	}

	public String getUSERTIME3() {
		return sUSERTIME3;
	}

	public float getUSERDBL1() {
		return fUSERDBL1;
	}

	public float getUSERDBL2() {
		return fUSERDBL2;
	}

	public float getUSERDBL3() {
		return fUSERDBL3;
	}

	public float getUSERDBL4() {
		return fUSERDBL4;
	}

	public float getUSERDBL5() {
		return fUSERDBL5;
	}

	public float getUSERDBL6() {
		return fUSERDBL6;
	}
	
	public float getGSTTAX() {
		return fGSTTAX;
	}
	
	
	public float getPRICEWITHTAX() {
		return fPRICEWITHTAX;
	}
	
	public float getTOTALDISCOUNTAMOUNT() {
		return fTotalDiscountAmount;
	}
	
	public float getTOTALDISCOUNTTOTAL() {
		return fTotalDiscountTotal;
	}
	
	public float getTOTALDISCOUNTSUBTOTAL() {
		return fTotalDiscountSubTotal;
	}
	
	public float getTOTALDISCOUNTTAX() {
		return fTotalDiscountTax;
	}
	
	//start code by radhika for pos to add batch on 4 dec 2013
	public String getBATCH() {
		return sBATCH;
	}
	//end code by radhika for pos to add batch on 4 dec 2013
	public int getBATCHID() {
		return iBATCHID;
	}
	
	public void setPLANT(String aPLANT) {
		sPLANT = aPLANT;
	}

	public void setITEM(String aITEM) {
		sITEM = aITEM;
	}

	public void setITEMDESC(String aITEMDESC) {
		sITEMDESC = aITEMDESC;
	}
	// added by radhika
	public void setNONSTOCKFLAG(String aNONSTOCKFLAG){
		sNONSTOCKFLAG = aNONSTOCKFLAG;
	}
	// ended
	public void setRANK(String aRANK) {
		sRANK = aRANK;
	}

	public void setITEMTYPE(String aITEMTYPE) {
		sITEMTYPE = aITEMTYPE;
	}

	public void setLENGTH(String aLENGTH) {
		sLENGTH = aLENGTH;
	}

	public void setWIDTH(String aWIDTH) {
		sWIDTH = aWIDTH;
	}

	public void setHEIGHT(String aHEIGHT) {
		sHEIGHT = aHEIGHT;
	}

	public void setVOLUME(String aVOLUME) {
		sVOLUME = aVOLUME;
	}

	public void setAREA(String aAREA) {
		sAREA = aAREA;
	}

	public void setISIZE(String aISIZE) {
		sISIZE = aISIZE;
	}

	public void setRORDLVL(String aRORDLVL) {
		sRORDLVL = aRORDLVL;
	}

	public void setSLIFEIND(String aSLIFEIND) {
		sSLIFEIND = aSLIFEIND;
	}

	public void setTRKIND(String aTRKIND) {
		sTRKIND = aTRKIND;
	}

	public void setQCIND(String aQCIND) {
		sQCIND = aQCIND;
	}

	public void setQUARIND(String aQUARIND) {
		sQUARIND = aQUARIND;
	}

	public void setHAZITEM(String aHAZITEM) {
		sHAZITEM = aHAZITEM;
	}

	public void setSLIFE(int aSLIFE) {
		iSLIFE = aSLIFE;
	}

	public void setQUARDAYS(int aQUARDAYS) {
		iQUARDAYS = aQUARDAYS;
	}

	public void setSTKUOM(String aSTKUOM) {
		sSTKUOM = aSTKUOM;
	}

	public void setUNITWGT(String aUNITWGT) {
		sUNITWGT = aUNITWGT;
	}

	public void setCOMMENT1(String aCOMMENT1) {
		sCOMMENT1 = aCOMMENT1;
	}

	public void setCOMMENT2(String aCOMMENT2) {
		sCOMMENT2 = aCOMMENT2;
	}

	public void setCRAT(String aCRAT) {
		sCRAT = aCRAT;
	}

	public void setCRBY(String aCRBY) {
		sCRBY = aCRBY;
	}

	public void setUPAT(String aUPAT) {
		sUPAT = aUPAT;
	}

	public void setUPBY(String aUPBY) {
		sUPBY = aUPBY;
	}

	public void setRECSTAT(String aRECSTAT) {
		sRECSTAT = aRECSTAT;
	}

	public void setUSERFLD1(String aUSERFLD1) {
		sUSERFLD1 = aUSERFLD1;
	}

	public void setUSERFLD2(String aUSERFLD2) {
		sUSERFLD2 = aUSERFLD2;
	}

	public void setUSERFLD3(String aUSERFLD3) {
		sUSERFLD3 = aUSERFLD3;
	}

	public void setUSERFLD4(String aUSERFLD4) {
		sUSERFLD4 = aUSERFLD4;
	}

	public void setUSERFLD5(String aUSERFLD5) {
		sUSERFLD5 = aUSERFLD5;
	}

	public void setUSERFLD6(String aUSERFLD6) {
		sUSERFLD6 = aUSERFLD6;
	}

	public void setUSERFLG1(String aUSERFLG1) {
		sUSERFLG1 = aUSERFLG1;
	}

	public void setUSERFLG2(String aUSERFLG2) {
		sUSERFLG2 = aUSERFLG2;
	}

	public void setUSERFLG3(String aUSERFLG3) {
		sUSERFLG3 = aUSERFLG3;
	}

	public void setUSERFLG4(String aUSERFLG4) {
		sUSERFLG4 = aUSERFLG4;
	}

	public void setUSERFLG5(String aUSERFLG5) {
		sUSERFLG5 = aUSERFLG5;
	}

	public void setUSERFLG6(String aUSERFLG6) {
		sUSERFLG6 = aUSERFLG6;
	}

	public void setUSERTIME1(String aUSERTIME1) {
		sUSERTIME1 = aUSERTIME1;
	}

	public void setUSERTIME2(String aUSERTIME2) {
		sUSERTIME2 = aUSERTIME2;
	}

	public void setUSERTIME3(String aUSERTIME3) {
		sUSERTIME3 = aUSERTIME3;
	}

	public void setUSERDBL1(float aUSERDBL1) {
		fUSERDBL1 = aUSERDBL1;
	}

	public void setUSERDBL2(float aUSERDBL2) {
		fUSERDBL2 = aUSERDBL2;
	}

	public void setUSERDBL3(float aUSERDBL3) {
		fUSERDBL3 = aUSERDBL3;
	}

	public void setUSERDBL4(float aUSERDBL4) {
		fUSERDBL4 = aUSERDBL4;
	}

	public void setUSERDBL5(float aUSERDBL5) {
		fUSERDBL5 = aUSERDBL5;
	}

	public void setUSERDBL6(float aUSERDBL6) {
		fUSERDBL6 = aUSERDBL6;
	}
	
	public void setGSTTAX(float GSTTAX) {
		fGSTTAX = GSTTAX;
	}
	
	public void setPRICEWITHTAX(float PRICEWITHTAX) {
		fPRICEWITHTAX = PRICEWITHTAX;
	}
	
	public void setTOTALDISCOUNTAMOUNT(float TOTALDISCOUNTAMOUNT) {
		fTotalDiscountAmount = TOTALDISCOUNTAMOUNT;
	}
	
	public void setTOTALDISCOUNTTOTAL(float TOTALDISCOUNTTOTAL) {
		fTotalDiscountTotal = TOTALDISCOUNTTOTAL;
	}
	
	public void setTOTALDISCOUNTSUBTOTAL(float TOTALDISCOUNTSUBTOTAL) {
		fTotalDiscountSubTotal = TOTALDISCOUNTSUBTOTAL;
	}
	
	public void setTOTALDISCOUNTTAX(float TOTALDISCOUNTTAX) {
		fTotalDiscountTotal = TOTALDISCOUNTTAX;
	}
	
	//start code by radhika for pos to add batch on 4 dec 2013
	public void setBATCH(String aBATCH) {
		sBATCH = aBATCH;
	}
	//end code by radhika for pos to add batch on 4 dec 2013
	public void setBATCHID(int aBATCHID) {
		iBATCHID = aBATCHID;
	}
	String sEXPIREDATE = "";
	public void setEXPIREDATE(String expDate) {
		sEXPIREDATE = expDate;
	}
	String locdesc="";
	public String getLocDesc()
	{
		return locdesc;
	}
	public void setLocDesc(String locationdesc)
	{
		locdesc = locationdesc;
	}
	String tolocdesc;
	public String getToLocDesc()
	{
		return tolocdesc;
	}
	public void setToLocDesc(String tolocationdesc)
	{
		tolocdesc = tolocationdesc;
	}
	/**
	 * constructor method ITEMMST sb - SQL Bean gn - Utilites
	 */
	
	public ITEMMST() throws Exception {
		sb = new sqlBean();
		gn = new Generator();
	}
	
	public String getEXPREDATE() {
		return sEXPIREDATE;
	}

	/**
	 * DBConnection() get connection pool from the database
	 * 
	 */
	public Connection getDBConnection() throws Exception {
		Connection con = DbBean.getConnection();
		return con;
	}

	/**
	 * Insert Command - ITEMMST
	 * 
	 * 
	 */
	public int insertITEMMST() throws Exception {
		String sql;
		Connection con = getDBConnection();
		ResultSet rs = null;
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = "Insert into ITEMMST values ('" + sPLANT + "','" + sITEM
					+ "','" + sITEMDESC + "','" + sRANK + "','" + sITEMTYPE
					+ "','" + sLENGTH + "','" + sWIDTH + "','" + sHEIGHT
					+ "','" + sVOLUME + "','" + sAREA + "','" + sISIZE + "','"
					+ sRORDLVL + "','" + sSLIFEIND + "','" + sTRKIND + "','"
					+ sQCIND + "','" + sQUARIND + "','" + sHAZITEM + "',"
					+ iSLIFE + "," + iQUARDAYS + ",'" + sSTKUOM + "','"
					+ sUNITWGT + "','" + sCOMMENT1 + "','" + sCOMMENT2 + "','"
					+ sCRAT + "','" + sCRBY + "','" + sUPAT + "','" + sUPBY
					+ "','" + sRECSTAT + "','" + sUSERFLD1 + "','" + sUSERFLD2
					+ "','" + sUSERFLD3 + "','" + sUSERFLD4 + "','" + sUSERFLD5
					+ "','" + sUSERFLD6 + "','" + sUSERFLG1 + "','" + sUSERFLG2
					+ "','" + sUSERFLG3 + "','" + sUSERFLG4 + "','" + sUSERFLG5
					+ "','" + sUSERFLG6 + "','" + sUSERTIME1 + "','"
					+ sUSERTIME2 + "','" + sUSERTIME3 + "'," + fUSERDBL1 + ","
					+ fUSERDBL2 + "," + fUSERDBL3 + "," + fUSERDBL4 + ","
					+ fUSERDBL5 + "," + fUSERDBL6 + ")";

			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	/**
	 * Update Function - ITEMMST
	 * 
	 * 
	 */
	public int updateITEMMST() throws Exception {
		String sql;
		Connection con = getDBConnection();
		ResultSet rs = null;
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = "Update ITEMMST set ITEMDESC= '" + sITEMDESC
					+ "',ITEMTYPE= '" + sITEMTYPE + "',LENGTH= '" + sLENGTH
					+ "',WIDTH= '" + sWIDTH + "',HEIGHT= '" + sHEIGHT
					+ "',VOLUME= '" + sVOLUME + "',AREA= '" + sAREA
					+ "',ISIZE= '" + sISIZE + "',RORDLVL= '" + sRORDLVL
					+ "',SLIFEIND= '" + sSLIFEIND + "',TRKIND= '" + sTRKIND
					+ "',QCIND= '" + sQCIND + "',QUARIND= '" + sQUARIND
					+ "',HAZITEM= '" + sHAZITEM + "',SLIFE = " + iSLIFE
					+ ",QUARDAYS = " + iQUARDAYS + ",STKUOM= '" + sSTKUOM
					+ "',UNITWGT= '" + sUNITWGT + "',COMMENT1= '" + sCOMMENT1
					+ "',COMMENT2= '" + sCOMMENT2 + "',CRAT= '" + sCRAT
					+ "',CRBY= '" + sCRBY + "',UPAT= '" + sUPAT + "',UPBY= '"
					+ sUPBY + "',RECSTAT= '" + sRECSTAT + "',USERFLD1= '"
					+ sUSERFLD1 + "',USERFLD2= '" + sUSERFLD2 + "',USERFLD3= '"
					+ sUSERFLD3 + "',USERFLD4= '" + sUSERFLD4 + "',USERFLD5= '"
					+ sUSERFLD5 + "',USERFLD6= '" + sUSERFLD6 + "',USERFLG1= '"
					+ sUSERFLG1 + "',USERFLG2= '" + sUSERFLG2 + "',USERFLG3= '"
					+ sUSERFLG3 + "',USERFLG4= '" + sUSERFLG4 + "',USERFLG5= '"
					+ sUSERFLG5 + "',USERFLG6= '" + sUSERFLG6
					+ "',USERTIME1= '" + sUSERTIME1 + "',USERTIME2= '"
					+ sUSERTIME2 + "',USERTIME3= '" + sUSERTIME3
					+ "',USERDBL1 = " + fUSERDBL1 + ",USERDBL2 = " + fUSERDBL2
					+ ",USERDBL3 = " + fUSERDBL3 + ",USERDBL4 = " + fUSERDBL4
					+ ",USERDBL5 = " + fUSERDBL5 + ",USERDBL6 = " + fUSERDBL6
					+ " where PLANT= '" + sPLANT + "' and ITEM= '" + sITEM
					+ "' and RANK= '" + sRANK + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	/*
	 * public String getItemDesc(String ItemCode) throws Exception{ sITEM =
	 * ItemCode; sITEMDESC = ""; selectITEMMST(); return getITEMDESC(); }
	 */

	public String getItemDesc(String ItemCode) throws Exception {

		String q = "";
		String itemDesc = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from ITEMMST where PLANT= 'SIS' and ITEM= '"
					+ ItemCode + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				itemDesc = rs.getString("ITEMDESC");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return itemDesc;
	}

	public String getBaseUom(String ItemCode) throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from ITEMMST where PLANT= 'COM' and ITEM= '"
					+ ItemCode + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sITEMDESC = rs.getString("STKUOM");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return sITEMDESC;
	}

	public String getProductionUom(String ItemCode) throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from ITEMMST where PLANT= 'COM' and ITEM= '"
					+ ItemCode + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sITEMDESC = rs.getString("USERFLD1");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return sITEMDESC;
	}

	public boolean isBatch() throws Exception {
		boolean res = false;
		if (sTRKIND == "B") {
			res = true;
		}
		return res;
	}

	public boolean isSerial() throws Exception {
		boolean res = false;
		if (sTRKIND == "S") {
			res = true;
		}
		return res;
	}

	public boolean isQty() throws Exception {
		boolean res = false;
		if (sTRKIND == "Q") {
			res = true;
		}
		return res;
	}

	public boolean hasQC() throws Exception {
		boolean res = false;
		if (sQCIND == "Y") {
			res = true;
		}
		return res;
	}

	public boolean hasShelfLife() throws Exception {
		boolean res = false;
		if (sSLIFEIND == "Y") {
			res = true;
		}
		return res;
	}

	public boolean hasQUAR() throws Exception {
		boolean res = false;
		if (sQUARIND == "Y") {
			res = true;
		}
		return res;
	}

	public void selectITEMMST() throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from ITEMMST where PLANT= '" + sPLANT
					+ "' and ITEM= '" + sITEM + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sITEMDESC = rs.getString("ITEMDESC");
				sITEMTYPE = rs.getString("ITEMTYPE");
				sLENGTH = rs.getString("LENGTH");
				sWIDTH = rs.getString("WIDTH");
				sHEIGHT = rs.getString("HEIGHT");
				sVOLUME = rs.getString("VOLUME");
				sAREA = rs.getString("AREA");
				sISIZE = rs.getString("ISIZE");
				sRORDLVL = rs.getString("RORDLVL");
				sSLIFEIND = rs.getString("SLIFEIND");
				sTRKIND = rs.getString("TRKIND");
				sQCIND = rs.getString("QCIND");
				sQUARIND = rs.getString("QUARIND");
				sHAZITEM = rs.getString("HAZITEM");
				iSLIFE = rs.getInt("SLIFE");
				iQUARDAYS = rs.getInt("QUARDAYS");
				sSTKUOM = rs.getString("STKUOM");
				sUNITWGT = rs.getString("UNITWGT");
				sCOMMENT1 = rs.getString("COMMENT1");
				sCOMMENT2 = rs.getString("COMMENT2");
				sCRAT = rs.getString("CRAT");
				sCRBY = rs.getString("CRBY");
				sUPAT = rs.getString("UPAT");
				sUPBY = rs.getString("UPBY");
				sRECSTAT = rs.getString("RECSTAT");
				sUSERFLD1 = rs.getString("USERFLD1");
				sUSERFLD2 = rs.getString("USERFLD2");
				sUSERFLD3 = rs.getString("USERFLD3");
				sUSERFLD4 = rs.getString("USERFLD4");
				sUSERFLD5 = rs.getString("USERFLD5");
				sUSERFLD6 = rs.getString("USERFLD6");
				sUSERFLG1 = rs.getString("USERFLG1");
				sUSERFLG2 = rs.getString("USERFLG2");
				sUSERFLG3 = rs.getString("USERFLG3");
				sUSERFLG4 = rs.getString("USERFLG4");
				sUSERFLG5 = rs.getString("USERFLG5");
				sUSERFLG6 = rs.getString("USERFLG6");
				sUSERTIME1 = rs.getString("USERTIME1");
				sUSERTIME2 = rs.getString("USERTIME2");
				sUSERTIME3 = rs.getString("USERTIME3");
				fUSERDBL1 = rs.getFloat("USERDBL1");
				fUSERDBL2 = rs.getFloat("USERDBL2");
				fUSERDBL3 = rs.getFloat("USERDBL3");
				fUSERDBL4 = rs.getFloat("USERDBL4");
				fUSERDBL5 = rs.getFloat("USERDBL5");
				fUSERDBL6 = rs.getFloat("USERDBL6");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
	}

	/**
	 * Delete Function -- ITEMMST
	 * 
	 * 
	 */
	public int deleteITEMMST() throws Exception {
		String sql;
		Connection con = getDBConnection();
		ResultSet rs = null;
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = "delete from ITEMMST where PLANT= '" + sPLANT
					+ "' and ITEM= '" + sITEM + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String getCmbITEM() throws Exception {

		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();

			String q = "select distinct item from itemmst";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("ITEM").trim();
					selectStr += "<option value='" + val + "'>" + val
							+ "</option>";
				}

			}
		} // End of try
		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
	}

	public String listITEMMST(String aITEM) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if ((aITEM.length() > 0)) {
				vecWhr.addElement("ITEM like '" + aITEM + "%'");
			}
			int cnt = vecWhr.size();
			if (cnt > 0) {
				where = "where ";
				for (int i = 0; i < cnt; i++) {
					where = where + vecWhr.elementAt(i).toString() + " ";
					if (i < cnt - 1) {
						where = where + " and ";
					}
				}
			}
			q = "select plant,item,itemdesc,itemtype,stkuom from itemmst "
					+ where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String plnt = rs.getString("PLANT").trim();
				String item = rs.getString("ITEM").trim();
				link = "<a href=\"" + "itmMastCH.jsp?PLANT=" + plnt + "&ITEM="
						+ item + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">" + link
						+ "<FONT COLOR=\"blue\">" + item + "</font></a>"
						+ "</td>" + "<td width=\"30%\">"
						+ rs.getString("ITEMDESC").trim() + "</td>"
						+ "<td width=\"20%\">"
						+ rs.getString("ITEMTYPE").trim() + "</td>"
						+ "<td width=\"20%\">" + rs.getString("STKUOM").trim()
						+ "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String listITEMMST(String aITEM, String popup) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			where = (aITEM.trim().equalsIgnoreCase("")) ? ""
					: " where ITEM like '" + aITEM + "%'";
			q = "select item,itemdesc from itemmst " + where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String item = rs.getString("ITEM").trim();
				String itemdesc = rs.getString("ITEMDESC").trim();
				link = "<a href=\"javascript:window.opener.form.ITEM.value="
						+ "'" + item + "';" + "window.opener.form.DESC.value="
						+ "'" + itemdesc + "';window.close();\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"30%\">" + link
						+ "<FONT COLOR=\"blue\">" + item + "</font></a>"
						+ "</td>" + "<td width=\"70%\">" + itemdesc.trim()
						+ "</td>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String listITEMMST(String aITEM, String aDESC, String aPARQTY,
			String aPARITEM) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if ((aITEM.length() > 0)) {
				vecWhr.addElement("ITEM like '" + aITEM + "%'");
			}
			if ((aDESC.length() > 0)) {
				vecWhr.addElement("ITEMDESC like '" + aDESC + "%'");
			}
			int cnt = vecWhr.size();
			if (cnt > 0) {
				where = "where ";
				for (int i = 0; i < cnt; i++) {
					where = where + vecWhr.elementAt(i).toString() + " ";
					if (i < cnt - 1) {
						where = where + " and ";
					}
				}
			}

			q = "select plant,item,itemdesc,itemtype,stkuom from itemmst "
					+ where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String plnt = rs.getString("PLANT").trim();
				String item = rs.getString("ITEM").trim();
				// //System.out.println("=> Parent Item" + aPARITEM);
				// System.out.println("=> Item " + item);
				// System.out.println("=> In Item Master");

				link = "<a href=\"" + "bomAddItem.jsp?PLANT=" + plnt
						+ "&PARITEM=" + aPARITEM + "&PARQTY=" + aPARQTY
						+ "&ITEM=" + item + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">" + link
						+ "<FONT COLOR=\"blue\">" + item + "</FONT></a>"
						+ "</td>" + "<td width=\"30%\">"
						+ sb.formatHTML(rs.getString("ITEMDESC").trim())
						+ "</td>" + "<td width=\"20%\">"
						+ sb.formatHTML(rs.getString("ITEMTYPE").trim())
						+ "</td>" + "<td width=\"20%\">"
						+ sb.formatHTML(rs.getString("STKUOM").trim())
						+ "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
		return result;
	}

	/*
	 * public boolean isAvaliable(String item, String batch, String status,
	 * String dbin) throws Exception{ boolean result = false; String q = "";
	 * Connection con = getDBConnection(); try { Statement stmt =
	 * con.createStatement(); q =
	 * "select count(*) from INVMST where PLANT= 'COM' and ITEM= '" + item +
	 * "' and BATCH= '" + batch + "' and BINNO= '" + dbin + "' and STATUS= '" +
	 * status + "'"; ResultSet rs = stmt.executeQuery(q); int n = 1; while
	 * (rs.next()) { int m = rs.getInt(1); if (m > 0) { result = true; } } }
	 * catch (Exception e) { DbBean.writeError("VENDMST", "selectVENDMST()", e);
	 * } finally { DbBean.closeConnection(con); } return result; }
	 */
	public boolean isAvaliable(String item) throws Exception {
		boolean result = false;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) from ITEMMST where PLANT= 'COM' and ITEM= '"
					+ item + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				int m = rs.getInt(1);
				if (m > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
}