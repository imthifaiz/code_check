package com.track.tables;

// importing classes

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.sqlBean;
import com.track.util.MLogger;

public class RECVHIS {
	private boolean printQuery = MLoggerConstant.RECVHIS_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.RECVHIS_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	String sPLANT = "SIS";
	String sYEARVAL = "";
	String sRECID = "";
	String sLNNO = "";
	String sMOVTID = "";
	String sPONO = "";
	String sPSTDT = "";
	String sITEM = "";
	String sVENDNO = "";
	float fQTY;
	float fQTYPA;
	String sBATNO = "";
	String sVENDBAT = "";
	String sUOM = "";
	String sSERNO = "";
	String sSLED = "";
	String sCLASS = "";
	String sATTRIB = "";
	String sATTDESC = "";
	String sSTATUS = "";
	String sWHID = "";
	String sLOC = "";
	String sDEFBIN = "";
	String sASGMTNO = "";
	String sASGMTDESC = "";
	String sASGMTCAT = "";
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
	float fUSERDBL1;
	float fUSERDBL2;
	float fUSERDBL3;
	float fUSERDBL4;
	float fUSERDBL5;
	float fUSERDBL6;

	public String getPLANT() {
		return sPLANT;
	}

	public String getYEARVAL() {
		return sYEARVAL;
	}

	public String getRECID() {
		return sRECID;
	}

	public String getLNNO() {
		return sLNNO;
	}

	public String getMOVTID() {
		return sMOVTID;
	}

	public String getPONO() {
		return sPONO;
	}

	public String getPSTDT() {
		return sPSTDT;
	}

	public String getITEM() {
		return sITEM;
	}

	public String getVENDNO() {
		return sVENDNO;
	}

	public float getQTY() {
		return fQTY;
	}

	public float getQTYPA() {
		return fQTYPA;
	}

	public String getBATNO() {
		return sBATNO;
	}

	public String getVENDBAT() {
		return sVENDBAT;
	}

	public String getUOM() {
		return sUOM;
	}

	public String getSERNO() {
		return sSERNO;
	}

	public String getSLED() {
		return sSLED;
	}

	public String getSTATUS() {
		return sSTATUS;
	}

	public String getCLASS() {
		return sCLASS;
	}

	public String getATTRIB() {
		return sATTRIB;
	}

	public String getATTDESC() {
		return sATTDESC;
	}

	public String getWHID() {
		return sWHID;
	}

	public String getLOC() {
		return sLOC;
	}

	public String getDEFBIN() {
		return sDEFBIN;
	}

	public String getASGMTNO() {
		return sASGMTNO;
	}

	public String getASGMTDESC() {
		return sASGMTDESC;
	}

	public String getASGMTCAT() {
		return sASGMTCAT;
	}

	/*
	 * public String getCRAT() { return gn.getDB2UserDateTime(sCRAT); }
	 */

	public String getCRAT() {
		return sCRAT;
	}

	public String getCRBY() {
		return sCRBY;
	}

	public String getUPAT() {
		return gn.getDB2UserDateTime(sUPAT);
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

	public void setPLANT(String aPLANT) {
		sPLANT = aPLANT;
	}

	public void setYEARVAL(String aYEARVAL) {
		sYEARVAL = aYEARVAL;
	}

	public void setRECID(String recid) {
		sRECID = recid;
	}

	public void setLNNO(String aLNNO) {
		sLNNO = aLNNO;
	}

	public void setMOVTID(String aMOVTID) {
		sMOVTID = aMOVTID;
	}

	public void setPONO(String aPONO) {
		sPONO = aPONO;
	}

	public void setPSTDT(String aPSTDT) {
		sPSTDT = aPSTDT;
	}

	public void setITEM(String aITEM) {
		sITEM = aITEM;
	}

	public void setCLASS(String aCLASS) {
		sCLASS = aCLASS;
	}

	public void setATTRIB(String aATTRIB) {
		sATTRIB = aATTRIB;
	}

	public void setATTDESC(String aATTDESC) {
		sATTDESC = aATTDESC;
	}

	public void setVENDNO(String aVENDNO) {
		sVENDNO = aVENDNO;
	}

	public void setQTY(float aQTY) {
		fQTY = aQTY;
	}

	public void setQTYPA(float aQTYPA) {
		fQTYPA = aQTYPA;
	}

	public void setBATNO(String aBATNO) {
		sBATNO = aBATNO;
	}

	public void setVENDBAT(String aVENDBAT) {
		sVENDBAT = aVENDBAT;
	}

	public void setUOM(String aUOM) {
		sUOM = aUOM;
	}

	public void setSERNO(String aSERNO) {
		sSERNO = aSERNO;
	}

	public void setSLED(String aSLED) {
		sSLED = aSLED;
	}

	public void setSTATUS(String aSTATUS) {
		sSTATUS = aSTATUS;
	}

	public void setWHID(String aWHID) {
		sWHID = aWHID;
	}

	public void setLOC(String aLOC) {
		sLOC = aLOC;
	}

	public void setDEFBIN(String aDEFBIN) {
		sDEFBIN = aDEFBIN;
	}

	public void setASGMTNO(String aASGMTNO) {
		sASGMTNO = aASGMTNO;
	}

	public void setASGMTDESC(String aASGMTDESC) {
		sASGMTDESC = aASGMTDESC;
	}

	public void setASGMTCAT(String aASGMTCAT) {
		sASGMTCAT = aASGMTCAT;
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

	/**
	 * constructor method RECVHIS sb - SQL Bean gn - Utilites
	 */
	public RECVHIS() throws Exception {
		sb = new sqlBean();
		gn = new Generator();
	}

	/**
	 * DBConnection() get connection pool from the database
	 * 
	 */
	public Connection getDBConnection() throws Exception {
		Connection con = DbBean.getConnection();
		return con;
	}

	//
	public String getNextRecID() throws Exception {
		String recId = "";
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select max(recid+1) from recvhis";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				recId = rs.getString(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return recId;
	}

	/**
	 * Insert Command - RECVHIS
	 * 
	 * 
	 */

	public int insertRECVHIS() throws Exception {
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
			sql = "Insert into RECVHIS values ('" + sPLANT + "','" + sYEARVAL
					+ "','" + sRECID + "','" + sLNNO + "','" + sMOVTID + "','"
					+ sPONO + "','" + sPSTDT + "','" + sITEM + "','" + sCLASS
					+ "','" + sATTRIB + "','" + sATTDESC + "','" + sVENDNO
					+ "'," + fQTY + "," + fQTYPA + ",'" + sBATNO + "','"
					+ sVENDBAT + "','" + sUOM + "','" + sSERNO + "','" + sSLED
					+ "','" + sSTATUS + "','" + sWHID + "','" + sLOC + "','"
					+ sDEFBIN + "','" + sASGMTNO + "','" + sASGMTDESC + "','"
					+ sASGMTCAT + "','" + sCRAT + "','" + sCRBY + "','" + sUPAT
					+ "','" + sUPBY + "','" + sRECSTAT + "','" + sUSERFLD1
					+ "','" + sUSERFLD2 + "','" + sUSERFLD3 + "','" + sUSERFLD4
					+ "','" + sUSERFLD5 + "','" + sUSERFLD6 + "','" + sUSERFLG1
					+ "','" + sUSERFLG2 + "','" + sUSERFLG3 + "','" + sUSERFLG4
					+ "','" + sUSERFLG5 + "','" + sUSERFLG6 + "','"
					+ sUSERTIME1 + "','" + sUSERTIME2 + "','" + sUSERTIME3
					+ "'," + fUSERDBL1 + "," + fUSERDBL2 + "," + fUSERDBL3
					+ "," + fUSERDBL4 + "," + fUSERDBL5 + "," + fUSERDBL6 + ")";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		if (returnCode == 1) {
			return returnCode;
		} else {
			throw new Exception("Could not able to update Receiving history");
		}
	}

	public String getInsertSql() throws Exception {
		String sql = "";
		try {
			sql = "Insert into RECVHIS values ('" + sPLANT + "','" + sYEARVAL
					+ "','" + sRECID + "','" + sLNNO + "','" + sMOVTID + "','"
					+ sPONO + "','" + sPSTDT + "','" + sITEM + "','" + sCLASS
					+ "','" + sATTRIB + "','" + sATTDESC + "','" + sVENDNO
					+ "'," + fQTY + "," + fQTYPA + ",'" + sBATNO + "','"
					+ sVENDBAT + "','" + sUOM + "','" + sSERNO + "','" + sSLED
					+ "','" + sSTATUS + "','" + sWHID + "','" + sLOC + "','"
					+ sDEFBIN + "','" + sASGMTNO + "','" + sASGMTDESC + "','"
					+ sASGMTCAT + "','" + sCRAT + "','" + sCRBY + "','" + sUPAT
					+ "','" + sUPBY + "','" + sRECSTAT + "','" + sUSERFLD1
					+ "','" + sUSERFLD2 + "','" + sUSERFLD3 + "','" + sUSERFLD4
					+ "','" + sUSERFLD5 + "','" + sUSERFLD6 + "','" + sUSERFLG1
					+ "','" + sUSERFLG2 + "','" + sUSERFLG3 + "','" + sUSERFLG4
					+ "','" + sUSERFLG5 + "','" + sUSERFLG6 + "','"
					+ sUSERTIME1 + "','" + sUSERTIME2 + "','" + sUSERTIME3
					+ "'," + fUSERDBL1 + "," + fUSERDBL2 + "," + fUSERDBL3
					+ "," + fUSERDBL4 + "," + fUSERDBL5 + "," + fUSERDBL6 + ")";
			this.mLogger.query(this.printQuery, sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sql;
	}

	/**
	 * Update Function - RECVHIS
	 * 
	 * 
	 */
	public int updateRECVHIS() throws Exception {
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
			sql = "Update RECVHIS set MOVTID= '" + sMOVTID + "',PONO= '"
					+ sPONO + "',PSTDT= '" + sPSTDT + "',ITEM= '" + sITEM
					+ "',VENDNO= '" + sVENDNO + "',QTY = " + fQTY + ",QTYPA = "
					+ fQTYPA + ",BATNO= '" + sBATNO + "',VENDBAT= '" + sVENDBAT
					+ "',UOM= '" + sUOM + "',SERNO= '" + sSERNO + "',SLED= '"
					+ sSLED + "',STATUS= '" + sSTATUS + "',WHID= '" + sWHID
					+ "',LOC= '" + sLOC + "',DEFBIN= '" + sDEFBIN
					+ "',ASGMTNO= '" + sASGMTNO + "',ASGMTDESC= '" + sASGMTDESC
					+ "',ASGMTCAT= '" + sASGMTCAT + "',CRAT= '" + sCRAT
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
					+ " where PLANT= '" + sPLANT + "' and YEARVAL= '"
					+ sYEARVAL + "' and RECID= '" + sRECID + "' and LNNO= '"
					+ sLNNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	/**
	 * Select FunctionRECVHIS
	 * 
	 * 
	 */
	public void selectRECVHIS(String aRECID) throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from RECVHIS where PLANT= '" + sPLANT
					+ "' and RECID= '" + aRECID + "'";
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sMOVTID = rs.getString("MOVTID");
				sPONO = rs.getString("PONO");
				sPSTDT = rs.getString("PSTDT");
				sITEM = rs.getString("ITEM");
				sCLASS = rs.getString("CLASS");
				sATTRIB = rs.getString("ATTRIB");
				sVENDNO = rs.getString("VENDNO");
				fQTY = rs.getFloat("QTY");
				fQTYPA = rs.getFloat("QTYPA");
				sBATNO = rs.getString("BATNO");
				sVENDBAT = rs.getString("VENDBAT");
				sUOM = rs.getString("UOM");
				sSERNO = rs.getString("SERNO");
				sSLED = rs.getString("SLED");
				sSTATUS = rs.getString("STATUS");
				sWHID = rs.getString("WHID");
				sLOC = rs.getString("LOC");
				sDEFBIN = rs.getString("DEFBIN");
				sASGMTNO = rs.getString("ASGMTNO");
				sASGMTDESC = rs.getString("ASGMTDESC");
				sASGMTCAT = rs.getString("ASGMTCAT");
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
	 * Delete Function -- RECVHIS
	 * 
	 * 
	 */
	public int deleteRECVHIS() throws Exception {
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
			sql = "delete from RECVHIS where PLANT= '" + sPLANT
					+ "' and YEARVAL= '" + sYEARVAL + "' and RECID= '" + sRECID
					+ "' and LNNO= '" + sLNNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			DbBean.writeError("RECVHIS", "deleteRECVHIS()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String listRECVHIS(String aCRAT) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			// System.out.println(where);
			q = "select recid,item,movtid,userflg2,pono,batno,userfld1 from recvhis where  status = 'Q' and CRAT like '"
					+ aCRAT + "%'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String recid = rs.getString("RECID").trim();
				String item = rs.getString("ITEM").trim();
				String movtid = rs.getString("MOVTID").trim();
				String stat = rs.getString("USERFLG2").trim();
				ITEMMST im = new ITEMMST();
				String desc = im.getItemDesc(item);
				if (stat.equalsIgnoreCase("C")) {
					if (movtid.equalsIgnoreCase("PUR")) {
						result += "<tr valign=\"middle\">"
								+ "<td align=\"center\" width=\"20%\"><FONT COLOR=\"blue\">"
								+ recid + "</font>" + "</td>"
								+ "<td width=\"20%\">" + sb.formatHTML(item)
								+ "</td>" + "<td width=\"30%\">"
								+ sb.formatHTML(desc) + "</td>"
								+ "<td width=\"15%\">"
								+ sb.formatHTML(rs.getString("PONO").trim())
								+ "</td>" + "<td width=\"15%\">"
								+ sb.formatHTML(rs.getString("BATNO").trim())
								+ "</td></tr>";

					} else {
						link = "<a href=\"" + "prodQA.jsp?RECID=" + recid
								+ "\">";
						result += "<tr valign=\"middle\">"
								+ "<td align=\"center\" width=\"20%\"><FONT COLOR=\"blue\">"
								+ recid + "</font>" + "</td>"
								+ "<td width=\"20%\">" + sb.formatHTML(item)
								+ "</td>" + "<td width=\"30%\">"
								+ sb.formatHTML(desc) + "</td>"
								+ "<td width=\"15%\">"
								+ sb.formatHTML(rs.getString("PONO").trim())
								+ "</td>" + "<td width=\"15%\">"
								+ sb.formatHTML(rs.getString("BATNO").trim())
								+ "</td></tr>";

					}

				} else {
					if (movtid.equalsIgnoreCase("PUR")) {
						link = "<a href=\"" + "QA.jsp?RECID=" + recid + "\">";
						result += "<tr valign=\"middle\">"
								+ "<td align=\"center\" width=\"20%\">" + link
								+ "<FONT COLOR=\"blue\">" + recid
								+ "</font></a>" + "</td>"
								+ "<td width=\"20%\">" + sb.formatHTML(item)
								+ "</td>" + "<td width=\"30%\">"
								+ sb.formatHTML(desc) + "</td>"
								+ "<td width=\"15%\">"
								+ sb.formatHTML(rs.getString("PONO").trim())
								+ "</td>" + "<td width=\"15%\">"
								+ sb.formatHTML(rs.getString("BATNO").trim())
								+ "</td></tr>";

					} else {
						link = "<a href=\"" + "prodQA.jsp?RECID=" + recid
								+ "\">";
						result += "<tr valign=\"middle\">"
								+ "<td align=\"center\" width=\"20%\">" + link
								+ "<FONT COLOR=\"blue\">" + recid
								+ "</font></a>" + "</td>"
								+ "<td width=\"20%\">" + sb.formatHTML(item)
								+ "</td>" + "<td width=\"30%\">"
								+ sb.formatHTML(desc) + "</td>"
								+ "<td width=\"15%\">"
								+ sb.formatHTML(rs.getString("PONO").trim())
								+ "</td>" + "<td width=\"15%\">"
								+ sb.formatHTML(rs.getString("BATNO").trim())
								+ "</td></tr>";

					}

				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
		return result;
	}

	public String listRECVHIS4RPT(String aCRAT) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select recid,item,qty,pono,batno,userfld1 from recvhis where MOVTID = 'PUR' and CRAT like '"
					+ aCRAT + "%'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String recid = rs.getString("RECID").trim();
				String item = rs.getString("ITEM").trim();
				ITEMMST im = new ITEMMST();
				String desc = im.getItemDesc(item);
				link = "<a href=\"" + "recvRpt.jsp?RECID=" + recid + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"15%\">" + link
						+ "<FONT COLOR=\"blue\">" + recid + "</font></a>"
						+ "</td>" + "<td width=\"15%\">" + sb.formatHTML(item)
						+ "</td>" + "<td width=\"30%\">" + sb.formatHTML(desc)
						+ "</td>" + "<td align=\"right\" width=\"10%\">"
						+ sb.formatHTML(rs.getString("QTY").trim()) + "</td>"
						+ "<td width=\"15%\">"
						+ sb.formatHTML(rs.getString("PONO").trim()) + "</td>"
						+ "<td width=\"15%\">"
						+ sb.formatHTML(rs.getString("BATNO").trim())
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

	public String listRECVHIS4PRD(String aCRAT) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			// System.out.println(where);
			q = "select recid,item,pono,batno,userfld1 from recvhis where MOVTID = 'PRD' and status = 'Q' and CRAT like '"
					+ aCRAT + "%'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String recid = rs.getString("RECID").trim();
				String item = rs.getString("ITEM").trim();
				ITEMMST im = new ITEMMST();
				String desc = im.getItemDesc(item);
				link = "<a href=\"" + "QA.jsp?RECID=" + recid + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">" + link
						+ "<FONT COLOR=\"blue\">" + recid + "</font></a>"
						+ "</td>" + "<td width=\"20%\">" + sb.formatHTML(item)
						+ "</td>" + "<td width=\"30%\">" + sb.formatHTML(desc)
						+ "</td>" + "<td width=\"15%\">"
						+ sb.formatHTML(rs.getString("PONO").trim()) + "</td>"
						+ "<td width=\"15%\">"
						+ sb.formatHTML(rs.getString("BATNO").trim())
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

	public String listRECVHIS4RPT(String aFRDT, String aTODT, String aVEND,
			String aSNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if (aFRDT.length() > 0) {
				vecWhr.addElement("CRAT > '" + aFRDT + "'");
			}
			if (aTODT.length() > 0) {
				vecWhr.addElement("CRAT < '" + aTODT + "'");
			}
			if (aVEND.length() > 0) {
				vecWhr.addElement("VENDNO = '" + aVEND + "'");
			}
			if (aSNO.length() > 0) {
				vecWhr.addElement("RECID = '" + aSNO + "'");
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
			// recid,pono,item,vendno,qty,batno,sled
			q = "select recid,item,vendno,pono,batno,qty,sled from recvhis "
					+ where; // MOVTID = 'PUR' and CRAT like '" + aCRAT +
			// "%'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String recid = rs.getString("RECID").trim();
				String item = rs.getString("ITEM").trim();
				ITEMMST im = new ITEMMST();
				String desc = im.getItemDesc(item);
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"10%\">"
						+ sb.formatHTML(recid)
						+ "</td>"
						+ "<td width=\"15%\">"
						+ sb.formatHTML(item)
						+ "</td>"
						+ "<td width=\"25%\">"
						+ sb.formatHTML(desc)
						+ "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(rs.getString("VENDNO").trim())
						+ "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(rs.getString("PONO").trim())
						+ "</td>"
						+ "<td width=\"15%\">"
						+ sb.formatHTML(rs.getString("BATNO").trim())
						+ "</td>"
						+ "<td width=\"15%\">"
						+ sb.formatHTML(gn.getDB2UserDate(rs.getString("SLED")
								.trim())) + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
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