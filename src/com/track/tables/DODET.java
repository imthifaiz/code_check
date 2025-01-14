package com.track.tables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.sqlBean;
import com.track.util.MLogger;

public class DODET {
	private boolean printQuery = MLoggerConstant.DODET_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.DODET_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	String sPLANT = "SIS";
	String sDONO = "";
	String sDOLNNO = "";
	String sLNSTAT = "";
	String sITEM = "";
	String sITEMDESC="";
	String sCLASS = "";
	String sUSRKEY1 = "";
	String sUSRKEY2 = "";
	String sUSRKEY3 = "";
	String sATTRIB = "";
	String sASNMT = "";
	float fQTYOR;
	float fQTYIS;
	float fQTYAC;
	float fQTYRJ;
	double dPRICE;
	String sLOC = "";
	String sBATCH = "";
	String sWHID = "";
	String sPOLTYPE = "";
	String sUNITMO = "";
	String sDELDATE = "";
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
	String sREMARK = "";
	float fUSERDBL1;
	float fUSERDBL2;
	float fUSERDBL3;
	float fUSERDBL4;
	float fUSERDBL5;
	float fUSERDBL6;
	// start  added by Bruhan for estimate
	String sESTNO = "";
	String sESTLNNO = "";
	String sESTITEM = "";
	String sESTITEMDESC="";
	String sESTUNITMO = "";
	String sESTCOMMENT1 = "";
	float fESTQTYOR;
	double dESTPRICE;
	

	
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public String getPLANT() {
		return sPLANT;
	}
	
	public String getsESTNO() {
		return sESTNO;
	}

	public void setsESTNO(String sESTNO) {
		this.sESTNO = sESTNO;
	}

	public String getsESTLNNO() {
		return sESTLNNO;
	}

	public void setsESTLNNO(String sESTLNNO) {
		this.sESTLNNO = sESTLNNO;
	}

	public String getsESTITEM() {
		return sESTITEM;
	}

	public void setsESTITEM(String sESTITEM) {
		this.sESTITEM = sESTITEM;
	}

	public String getsESTITEMDESC() {
		return sESTITEMDESC;
	}

	public void setsESTITEMDESC(String sESTITEMDESC) {
		this.sESTITEMDESC = sESTITEMDESC;
	}

	public String getsESTUNITMO() {
		return sESTUNITMO;
	}

	public void setsESTUNITMO(String sESTUNITMO) {
		this.sESTUNITMO = sESTUNITMO;
	}

	public float getfESTQTYOR() {
		return fESTQTYOR;
	}

	public void setfESTQTYOR(float fESTQTYOR) {
		this.fESTQTYOR = fESTQTYOR;
	}

	public double getdESTPRICE() {
		return dESTPRICE;
	}

	public void setdESTPRICE(double dESTPRICE) {
		this.dESTPRICE = dESTPRICE;
	}

	public String getsESTCOMMENT1() {
		return sESTCOMMENT1;
	}

	public void setsESTCOMMENT1(String sESTCOMMENT1) {
		this.sESTCOMMENT1 = sESTCOMMENT1;
	}
	// end  added by Bruhan for estimate
	
	public String getDONO() {
		return sDONO;
	}

	public String getDOLNNO() {
		return sDOLNNO;
	}

	public String getLNSTAT() {
		return sLNSTAT;
	}

	public String getITEM() {
		return sITEM;
	}
	public String getITEMDESC() {
		return sITEMDESC;
	}

	public String getCLASS() {
		return sCLASS;
	}

	public String getUSRKEY1() {
		return sUSRKEY1;
	}

	public String getUSRKEY2() {
		return sUSRKEY2;
	}

	public String getUSRKEY3() {
		return sUSRKEY3;
	}

	public String getATTRIB() {
		return sATTRIB;
	}

	public String getASNMT() {
		return sASNMT;
	}

	public float getQTYOR() {
		return fQTYOR;
	}

	public float getQTYIS() {
		return fQTYIS;
	}

	public float getQTYAC() {
		return fQTYAC;
	}

	public float getQTYRJ() {
		return fQTYRJ;
	}
	public double getPRICE() {
		return dPRICE;
	}

	public String getLOC() {
		return sLOC;
	}

	public String getWHID() {
		return sWHID;
	}

	public String getPOLTYPE() {
		return sPOLTYPE;
	}

	public String getUNITMO() {
		return sUNITMO;
	}

	public String getDELDATE() {
		return sDELDATE;
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

	public String getBATCH() {
		return sBATCH;
	}

	public String getREMARK() {
		return sREMARK;
	}

	public void setPLANT(String aPLANT) {
		sPLANT = aPLANT;
	}

	public void setDONO(String aDONO) {
		sDONO = aDONO;
	}

	public void setDOLNNO(String aDOLNNO) {
		sDOLNNO = aDOLNNO;
	}

	public void setLNSTAT(String aLNSTAT) {
		sLNSTAT = aLNSTAT;
	}

	public void setITEM(String aITEM) {
		sITEM = aITEM;
	}
	public void setITEMDESC(String aITEMDESC) {
		sITEMDESC = aITEMDESC;
	}

	public void setCLASS(String aCLASS) {
		sCLASS = aCLASS;
	}

	public void setUSRKEY1(String aUSRKEY1) {
		sUSRKEY1 = aUSRKEY1;
	}

	public void setUSRKEY2(String aUSRKEY2) {
		sUSRKEY2 = aUSRKEY2;
	}

	public void setUSRKEY3(String aUSRKEY3) {
		sUSRKEY3 = aUSRKEY3;
	}

	public void setATTRIB(String aATTRIB) {
		sATTRIB = aATTRIB;
	}

	public void setASNMT(String aASNMT) {
		sASNMT = aASNMT;
	}

	public void setQTYOR(float aQTYOR) {
		fQTYOR = aQTYOR;
	}

	public void setQTYIS(float aQTYIS) {
		fQTYIS = aQTYIS;
	}

	public void setQTYAC(float aQTYAC) {
		fQTYAC = aQTYAC;
	}

	public void setQTYRJ(float aQTYRJ) {
		fQTYRJ = aQTYRJ;
	}
	public void setPRICE(double aPRICE) {
		dPRICE = aPRICE;
	}

	public void setLOC(String aLOC) {
		sLOC = aLOC;
	}

	public void setWHID(String aWHID) {
		sWHID = aWHID;
	}

	public void setPOLTYPE(String aPOLTYPE) {
		sPOLTYPE = aPOLTYPE;
	}

	public void setUNITMO(String aUNITMO) {
		sUNITMO = aUNITMO;
	}

	public void setDELDATE(String aDELDATE) {
		sDELDATE = aDELDATE;
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

	public void setREMARK(String aREMARK) {
		sREMARK = aREMARK;
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

	public void setBATCH(String aBATCH) {
		sBATCH = aBATCH;
	}

	/**
	 * constructor method DODET sb - SQL Bean gn - Utilites
	 */
	public DODET() throws Exception {
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

	/**
	 * Insert Command - DODET
	 * 
	 * 
	 */
	public int insertDODET() throws Exception {
		String sql;
		Connection con = getDBConnection();
//		ResultSet rs = null;
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sb.setmLogger(mLogger);
			sql = "Insert into " + sPLANT + "_DODET values ('" + sPLANT + "','" + sDONO
					+ "','" + sDOLNNO + "','" + sLNSTAT + "','" + sITEM + "','"
					+ sUSRKEY1 + "','" + sUSRKEY2 + "','" + sUSRKEY3 + "','"
					+ sASNMT + "'," + fQTYOR + "," + fQTYIS + "," + fQTYAC
					+ "," + fQTYRJ + ",'" + sLOC + "','" + sWHID + "','"
					+ sPOLTYPE + "','" + sUNITMO + "','" + sDELDATE + "','"
					+ sCOMMENT1 + "','" + sCOMMENT2 + "','" + sCRAT + "','"
					+ sCRBY + "','" + sUPAT + "','" + sUPBY + "','" + sRECSTAT
					+ "','" + sUSERFLD1 + "','" + sUSERFLD2 + "','" + sUSERFLD3
					+ "','" + sUSERFLD4 + "','" + sUSERFLD5 + "','" + sUSERFLD6
					+ "','" + sUSERFLG1 + "','" + sUSERFLG2 + "','" + sUSERFLG3
					+ "','" + sUSERFLG4 + "','" + sUSERFLG5 + "','" + sUSERFLG6
					+ "','" + sUSERTIME1 + "','" + sUSERTIME2 + "','"
					+ sUSERTIME3 + "'," + fUSERDBL1 + "," + fUSERDBL2 + ","
					+ fUSERDBL3 + "," + fUSERDBL4 + "," + fUSERDBL5 + ","
					+ fUSERDBL6 + ")";
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	/**
	 * Update Function - DODET
	 * 
	 * 
	 */
	public int updateDODET() throws Exception {
		String sql;
		Connection con = getDBConnection();
//		ResultSet rs = null;
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = "Update " + sPLANT + "_DODET set LNSTAT= '" + sLNSTAT + "',ITEM= '" + sITEM
					+ "',USRKEY1= '" + sUSRKEY1 + "',USRKEY2= '" + sUSRKEY2
					+ "',USRKEY3= '" + sUSRKEY3 + "',ASNMT= '" + sASNMT
					+ "',QTYOR = " + fQTYOR + ",QTYIS = " + fQTYIS
					+ ",QTYAC = " + fQTYAC + ",QTYRJ = " + fQTYRJ + ",LOC= '"
					+ sLOC + "',WHID= '" + sWHID + "',POLTYPE= '" + sPOLTYPE
					+ "',UNITMO= '" + sUNITMO + "',DELDATE= '" + sDELDATE
					+ "',COMMENT1= '" + sCOMMENT1 + "',COMMENT2= '" + sCOMMENT2
					+ "',CRAT= '" + sCRAT + "',CRBY= '" + sCRBY + "',UPAT= '"
					+ sUPAT + "',UPBY= '" + sUPBY + "',RECSTAT= '" + sRECSTAT
					+ "',USERFLD1= '" + sUSERFLD1 + "',USERFLD2= '" + sUSERFLD2
					+ "',USERFLD3= '" + sUSERFLD3 + "',USERFLD4= '" + sUSERFLD4
					+ "',USERFLD5= '" + sUSERFLD5 + "',USERFLD6= '" + sUSERFLD6
					+ "',USERFLG1= '" + sUSERFLG1 + "',USERFLG2= '" + sUSERFLG2
					+ "',USERFLG3= '" + sUSERFLG3 + "',USERFLG4= '" + sUSERFLG4
					+ "',USERFLG5= '" + sUSERFLG5 + "',USERFLG6= '" + sUSERFLG6
					+ "',USERTIME1= '" + sUSERTIME1 + "',USERTIME2= '"
					+ sUSERTIME2 + "',USERTIME3= '" + sUSERTIME3
					+ "',USERDBL1 = " + fUSERDBL1 + ",USERDBL2 = " + fUSERDBL2
					+ ",USERDBL3 = " + fUSERDBL3 + ",USERDBL4 = " + fUSERDBL4
					+ ",USERDBL5 = " + fUSERDBL5 + ",USERDBL6 = " + fUSERDBL6
					+ " where PLANT= '" + sPLANT + "' and DONO= '" + sDONO
					+ "' and DOLNNO= '" + sDOLNNO + "'";
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
	 * Select FunctionDODET
	 * 
	 * 
	 */
	public void selectDODET() throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from " + sPLANT + "_DODET where PLANT= '" + sPLANT + "' and DONO= '"
					+ sDONO + "' and DOLNNO= '" + sDOLNNO + "'";

			sb.setmLogger(mLogger);
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
//			int n = 1;
			while (rs.next()) {
				sLNSTAT = rs.getString("LNSTAT");
				sITEM = rs.getString("ITEM");
				sUSRKEY1 = rs.getString("USRKEY1");
				sUSRKEY2 = rs.getString("USERKE2");
				sUSRKEY3 = rs.getString("USRKEY3");
				sASNMT = rs.getString("ASNMT");
				fQTYOR = rs.getFloat("QTYOR");
				fQTYIS = rs.getFloat("QTYIS");
				fQTYAC = rs.getFloat("QTYAC");
				fQTYRJ = rs.getFloat("QTYRJ");
				sLOC = rs.getString("LOC");
				sWHID = rs.getString("WHID");
				sPOLTYPE = rs.getString("POLTYPE");
				sUNITMO = rs.getString("UNITMO");
				sDELDATE = rs.getString("DELDATE");
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

	public void selectDODET(String plant) throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from " + plant + "_DODET where PLANT= '" + plant + "' and DONO= '"
					+ sDONO + "' and DOLNNO= '" + sDOLNNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
//			int n = 1;
			while (rs.next()) {
				sLNSTAT = rs.getString("LNSTAT");
				sITEM = rs.getString("ITEM");
				//TODO:Ravindra:Discuss with bruhan
//				sUSRKEY1 = rs.getString("USRKEY1");
//				sUSRKEY2 = rs.getString("USERKE2");
//				sUSRKEY3 = rs.getString("USRKEY3");
				sASNMT = rs.getString("ASNMT");
				fQTYOR = rs.getFloat("QTYOR");
				fQTYIS = rs.getFloat("QTYIS");
				fQTYAC = rs.getFloat("QTYAC");
				fQTYRJ = rs.getFloat("QTYRJ");
				sLOC = rs.getString("LOC");
				sWHID = rs.getString("WHID");
				sPOLTYPE = rs.getString("POLTYPE");
				sUNITMO = rs.getString("UNITMO");
				sDELDATE = rs.getString("DELDATE");
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
	 * Delete Function -- DODET
	 * 
	 * 
	 */
	public int deleteDODET() throws Exception {
		String sql;
		Connection con = getDBConnection();
//		ResultSet rs = null;
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = "delete from " + sPLANT + "_DODET where PLANT= '" + sPLANT + "' and DONO= '"
					+ sDONO + "' and DOLNNO= '" + sDOLNNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int deleteDODET(String aDONO, String aDOLNNO) throws Exception {
		String sql;
		Connection con = getDBConnection();
//		ResultSet rs = null;
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = "delete from DODET where PLANT= 'sis' and DONO= '" + aDONO
					+ "' and DOLNNO= '" + aDOLNNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int deleteDODET(String aDONO, String aDOLNNO, String plant)
			throws Exception {
		String sql;
		Connection con = getDBConnection();
//		ResultSet rs = null;
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = "delete from [" + plant + "_" + "DODET] where PLANT= '"
					+ plant + "'" + "  and DONO= '" + aDONO + "' and DOLNNO= '"
					+ aDOLNNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int deleteDODET(String aDONO) throws Exception {
		String sql;
		Connection con = getDBConnection();
//		ResultSet rs = null;
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = "delete from DODET where PLANT= 'COM' and DONO= '" + aDONO
					+ "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String getInsertSql() throws Exception {
		String sql;
//		int returnCode = 1;
		sql = "Insert into " + sPLANT + "_DODET values ('" + sPLANT + "','" + sDONO + "','"
				+ sDOLNNO + "','" + sLNSTAT + "','" + sITEM + "','" + sCLASS
				+ "','" + sATTRIB + "','" + sASNMT + "'," + fQTYOR + ","
				+ fQTYIS + "," + fQTYAC + "," + fQTYRJ + ",'" + sLOC + "','"
				+ sWHID + "','" + sPOLTYPE + "','" + sUNITMO + "','" + sDELDATE
				+ "','" + sCOMMENT1 + "','" + sCOMMENT2 + "','" + sCRAT + "','"
				+ sCRBY + "','" + sUPAT + "','" + sUPBY + "','" + sRECSTAT
				+ "','" + sUSERFLD1 + "','" + sUSERFLD2 + "','" + sUSERFLD3
				+ "','" + sUSERFLD4 + "','" + sUSERFLD5 + "','" + sUSERFLD6
				+ "','" + sUSERFLG1 + "','" + sUSERFLG2 + "','" + sUSERFLG3
				+ "','" + sUSERFLG4 + "','" + sUSERFLG5 + "','" + sUSERFLG6
				+ "','" + sUSERTIME1 + "','" + sUSERTIME2 + "','" + sUSERTIME3
				+ "'," + fUSERDBL1 + "," + fUSERDBL2 + "," + fUSERDBL3 + ","
				+ fUSERDBL4 + "," + fUSERDBL5 + "," + fUSERDBL6 + ")";
		return sql;
	}

	public String listDODET(String aDONO) throws Exception {

		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
//		NumberFormat formatter = new DecimalFormat("#,##0.00#");
		Connection con = null;
		try {
			con = getDBConnection();
			Statement stmt = con.createStatement();
			if ((aDONO.length() > 0)) {
				vecWhr.addElement("DONO = '" + aDONO + "'");
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

			q = "select dono,dolnno,item,userfld1 from dodet " + where;
			sb.setmLogger(mLogger);
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String dono = rs.getString("DONO").trim();
				String dolnno = rs.getString("DOLNNO").trim();
				ITEMMST itm = new ITEMMST();
				System.out.println("stage 1: ");
				String item = rs.getString("ITEM").trim();
				System.out.println("stage 2: ");
				String desc = itm.getItemDesc(item);
				System.out.println("stage 3: ");

				
				link = "<a href=\"" + "modiSODET.jsp?DONO=" + dono + "&DOLNNO="
						+ dolnno + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">" + link
						+ "<FONT COLOR=\"blue\">" + dolnno + "</a></font>"
						+ "</td>" + "<td width=\"20%\">" + sqlBean.formatHTML(item)
						+ "</td>" + "<td width=\"40%\">" + sqlBean.formatHTML(desc)
						+ "</td>" +

						"</tr>";
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		System.out.println(result);
		MLogger.log(-1, this.getClass() + " listDODET()");
		return result;
	}

	public String listGoodsIssue(String sREFNO) throws Exception {
		String q = "";
		String result = "";
//		String where = "";
//		Vector vecWhr = new Vector();
		String link = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select item,class,attrib,qtyac from DODET where dono ='"
					+ sREFNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int cnt = 1;
			while (rs.next()) {
				Vector issVec = new Vector();
				INVMST inv = new INVMST();
				ITEMMST im = new ITEMMST();
				PCKALLOC pck = new PCKALLOC();
				String it = rs.getString("ITEM").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				String qt = rs.getString("QTYAC").trim();
				String item = "";
				String desc = "";
				String batch = "";
				String bin = "";
				String qty = "";
				issVec = inv.getAllocatedItem(it, clas, attrib, Float
						.parseFloat(qt));
				if (issVec.size() > 0) {
					for (int i = 0; i < issVec.size(); i++) {
						Vector myVec = (Vector) issVec.elementAt(i);
						item = myVec.elementAt(0).toString();
						desc = im.getItemDesc(item);
						batch = myVec.elementAt(1).toString();
						bin = myVec.elementAt(2).toString();
						qty = myVec.elementAt(3).toString();
						pck.setREFNO(sREFNO);
						pck.setREFLNNO(cnt + "");
						pck.setBIN(bin);
						pck.setBATCH(batch);
						pck.setORDTYPE("PR");
						pck.setITEM(item);
						pck.setQTY(Float.parseFloat(qty));
						pck.setSTATUS("N");
						pck.insertPCKALLOC();
						link = "<a href=\"" + "pickQty.jsp?ITEM=" + item
								+ "&BATCH=" + batch + "&BINNO=" + bin + "&QTY="
								+ qty + "\">";
						result += "<tr valign=\"middle\">"
								+ "<td align=\"center\" width=\"20%\">" + link
								+ "<FONT COLOR=\"blue\">" + item
								+ "</FONT></td>" + "<td width=\"40%\">"
								+ sqlBean.formatHTML(desc) + "</td>"
								+ "<td width=\"15%\">" + sqlBean.formatHTML(batch)
								+ "</td>" + "<td width=\"15%\">"
								+ sqlBean.formatHTML(bin) + "</td>"
								+ "<td width=\"10%\">" + sqlBean.formatHTML(qty)
								+ "</td></tr>";
						cnt = cnt + 1;
					}
				} else {
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"20%\">" + link
							+ "<FONT COLOR=\"blue\">" + it + "</FONT></td>"
							+ "<td width=\"40%\">" + im.getItemDesc(it)
							+ "</td>" + "<td width=\"15%\">" + "OUT" + "</td>"
							+ "<td width=\"15%\"> OF </td>"
							+ "<td width=\"10%\">  STOCK  </td></tr>";
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		System.out.println(result);
		return result;
	}

	public String listGoodsIssue4Alloc(String sREFNO) throws Exception {
		String q = "";
		String result = "";
//		String where = "";
//		Vector vecWhr = new Vector();
		String link = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select item,class,attrib,qtyac,deldate from DODET where dono ='"
					+ sREFNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int cnt = 1;
			while (rs.next()) {
				Vector issVec = new Vector();
				INVMST inv = new INVMST();

				ITEMMST im = new ITEMMST();
				PCKALLOC pck = new PCKALLOC();
				String it = rs.getString("ITEM").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				String qt = rs.getString("QTYAC").trim();
				String dt = rs.getString("DELDATE").trim();
				String item = "";
				String desc = "";
				String batch = "";
				String bin = "";
				String qty = "";
				issVec = inv.getAllocatedItem(it, clas, attrib, Float
						.parseFloat(qt));
				if (issVec.size() > 0) {
					for (int i = 0; i < issVec.size(); i++) {
						Vector myVec = (Vector) issVec.elementAt(i);
						item = myVec.elementAt(0).toString();
						desc = im.getItemDesc(item);
						batch = myVec.elementAt(1).toString();
						bin = myVec.elementAt(2).toString();
						qty = myVec.elementAt(3).toString();
						pck.setREFNO(sREFNO);
						pck.setREFLNNO(cnt + "");
						pck.setBIN(bin);
						pck.setCLASS(clas);
						pck.setATTRIB(attrib);
						pck.setBATCH(batch);
						pck.setORDTYPE("PR");
						pck.setITEM(item);
						pck.setQTY(Float.parseFloat(qty));
						pck.setUSERTIME1(dt);
						pck.setSTATUS("N");
						pck.insertPCKALLOC();
						link = "<a href=\"" + "pickQtyMannual.jsp?ITEM=" + item
								+ "&BATCH=" + batch + "&BINNO=" + bin
								+ "&CLASS=" + clas + "&ATTRIB=" + attrib
								+ "&REFLNNO=" + cnt + "&REFNO=" + sREFNO
								+ "&QTY=" + qty + "\">";
						result += "<tr valign=\"middle\">"
								+ "<td align=\"center\" width=\"20%\">"
								+ link
								+ "<FONT COLOR=\"blue\">"
								+ item
								+ "</FONT></td>"
								+ "<td width=\"30%\">"
								+ sqlBean.formatHTML(desc)
								+ "</td>"
								+ "<td width=\"10%\">"
								+ sqlBean.formatHTML(clas)
								+ "</td>"
								+ "<td width=\"10%\">"
								+ sqlBean.formatHTML(attrib)
								+ "</td>"
								+ "<td width=\"10%\">"
								+ sqlBean.formatHTML(batch)
								+ "</td>"
								+ "<td width=\"10%\">"
								+ sqlBean.formatHTML(bin)
								+ "</td>"
								+ "<td width=\"10%\">"
								+ sqlBean.formatHTML(qty)
								+ "</td></tr>";
						cnt = cnt + 1;
					}
				} else {
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"20%\">" + link
							+ "<FONT COLOR=\"blue\">" + it + "</FONT></td>"
							+ "<td width=\"30%\">" + im.getItemDesc(it)
							+ "</td>" + "<td width=\"5%\">" + "&nbsp" + "</td>"
							+ "<td width=\"5%\">" + "&nbsp" + "</td>"
							+ "<td width=\"15%\">" + "OUT" + "</td>"
							+ "<td width=\"15%\"> OF </td>"
							+ "<td width=\"10%\">  STOCK  </td></tr>";
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		System.out.println(result);
		return result;
	}

	public String getNextLineNo(String aDONO, String aPlant) throws Exception {
		String q = "";
		String result = "";
//		int cnt = 0;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select max(cast(dolnno as int))+1 from " + "[" + aPlant + "_"
					+ "dodet" + "]" + " where dono = '" + aDONO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				result = rs.getString(1);
				if (result == null) {
					result = "1";
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String getNextLineNo(String aDONO) throws Exception {
		String q = "";
		String result = "";
//		int cnt = 0;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select max(cast(dolnno as int))+1 from " + "[" + "dodet" + "]"
					+ " where dono = '" + aDONO + "'";
			System.out.println(q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				result = rs.getString(1);
				if (result == null) {
					result = "1";
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String getNextDono(String ordtype) throws Exception {
		String recId = "", res = "";
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select max(dono) from dohdr ";
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				recId = rs.getString(1);
				if (recId != null) {
					recId = recId.replace(ordtype.charAt(0), '0');
					StringBuffer s = new StringBuffer(recId);
					s.deleteCharAt(0);
					n = Integer.parseInt(recId) + 1;
					System.out.println("AFTER " + n);
					NumberFormat formatter = new DecimalFormat("0000");
					res = ordtype + formatter.format(n);
				} else {
					res = "D0001";
				}
				// recId = rs.getString(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return res;
	}

	public String getUpdateSql() throws Exception {
		String sql;
		sql = "Update DODET set LNSTAT= '" + sLNSTAT + "',ITEM= '" + sITEM
				+ "',CLASS= '" + sCLASS + "',ATTRIB= '" + sATTRIB
				+ "',ASNMT= '" + sASNMT + "',QTYOR = " + fQTYOR + ",QTYIS = "
				+ fQTYIS + ",QTYAC = " + fQTYAC + ",QTYRJ = " + fQTYRJ
				+ ",LOC= '" + sLOC + "',WHID= '" + sWHID + "',POLTYPE= '"
				+ sPOLTYPE + "',UNITMO= '" + sUNITMO + "',DELDATE= '"
				+ sDELDATE + "',COMMENT1= '" + sCOMMENT1 + "',COMMENT2= '"
				+ sCOMMENT2 + "',CRAT= '" + sCRAT + "',CRBY= '" + sCRBY
				+ "',UPAT= '" + sUPAT + "',UPBY= '" + sUPBY + "',RECSTAT= '"
				+ sRECSTAT + "',USERFLD1= '" + sUSERFLD1 + "',USERFLD2= '"
				+ sUSERFLD2 + "',USERFLD3= '" + sUSERFLD3 + "',USERFLD4= '"
				+ sUSERFLD4 + "',USERFLD5= '" + sUSERFLD5 + "',USERFLD6= '"
				+ sUSERFLD6 + "',USERFLG1= '" + sUSERFLG1 + "',USERFLG2= '"
				+ sUSERFLG2 + "',USERFLG3= '" + sUSERFLG3 + "',USERFLG4= '"
				+ sUSERFLG4 + "',USERFLG5= '" + sUSERFLG5 + "',USERFLG6= '"
				+ sUSERFLG6 + "',USERTIME1= '" + sUSERTIME1 + "',USERTIME2= '"
				+ sUSERTIME2 + "',USERTIME3= '" + sUSERTIME3 + "',USERDBL1 = "
				+ fUSERDBL1 + ",USERDBL2 = " + fUSERDBL2 + ",USERDBL3 = "
				+ fUSERDBL3 + ",USERDBL4 = " + fUSERDBL4 + ",USERDBL5 = "
				+ fUSERDBL5 + ",USERDBL6 = " + fUSERDBL6 + " where PLANT= '"
				+ sPLANT + "' and DONO= '" + sDONO + "' and DOLNNO= '"
				+ sDOLNNO + "'";
		return sql;
	}

	public int updateDODET(String aDONO, String aDOLNNO, float qty,
			String deldate) throws Exception {
		String sql;
		Connection con = getDBConnection();
//		ResultSet rs = null;
//		StrUtils su = new StrUtils();
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = "UPDATE DODET SET QTYOR = " + qty + ",USERTIME1 = '"
					+ deldate + "' where PLANT= 'COM' and DONO= '" + aDONO
					+ "' and DOLNNO= '" + aDOLNNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String getUpdateSql(String dono, String lnno, float issQty)
			throws Exception {
		String sql = "";
		sql = "Update DODET set QTYIS =  " + issQty + ",QTYAC = 0"
				+ " where PLANT= '" + sPLANT + "' and DONO= '" + dono
				+ "' and DOLNNO= '" + lnno + "'";
		return sql;
	}

	public String getActiveDOs() throws Exception {
		String q = "";
//		String link = "";
		String result = "";
//		String where = "";
//		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
//		CUSTMST cm = new CUSTMST();
		try {
			Statement stmt = con.createStatement();
			q = "SELECT DONO FROM DOHDR ";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				result += "<option value=\"" + rs.getString("DONO") + "\" >"
						+ rs.getString("DONO") + "</option>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String getNextLoanOrderLineNo(String aOrdno, String aPlant)
			throws Exception {
		String q = "";
		String result = "";
//		int cnt = 0;
		Connection con = com.track.gates.DbBean.getConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select max(cast(ordlnno as int))+1 from " + "[" + aPlant + "_"
					+ "LOANDET" + "]" + " where ORDNO = '" + aOrdno + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				result = rs.getString(1);
				if (result == null) {
					result = "1";
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}
	
	public String getEstNextLineNo(String aESTNO, String aPlant) throws Exception {
		String q = "";
		String result = "";
//		int cnt = 0;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select max(cast(estlnno as int))+1 from " + "[" + aPlant + "_"
					+ "estdet" + "]" + " where estno = '" + aESTNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				result = rs.getString(1);
				if (result == null) {
					result = "1";
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}	
}
