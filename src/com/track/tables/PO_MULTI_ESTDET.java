package com.track.tables;

// importing classes

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.sqlBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class PO_MULTI_ESTDET {
	private boolean printQuery = MLoggerConstant.PO_MULTI_ESTDET_PRINTPLANTMASTERLOG;
	private boolean printLog = MLoggerConstant.PO_MULTI_ESTDET_PRINTPLANTMASTERQUERY;
	sqlBean sb;
	Generator gn;
	String sPLANT = "SIS";
	String sPOMULTIESTNO = "";
	String sPOMULTIESTLNNO = "";
	String sLNSTAT = "";
	String sITEM = "";
	String sASNMT = "";
	float fQTYOR;
	float fQTYRC;
	float fQTYAC;
	float fQTYRJ;
        double dCOST;
	String sLOC = "";
	String sWHID = "";
	String sPOLTYPE = "";
	String sUNITMO = "";
	String sDELDATE = "";
        String sITEMDESC = "";
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
	float fUSERDBL1;
	float fUSERDBL2;
	float fUSERDBL3;
	float fUSERDBL4;
	float fUSERDBL5;
	float fUSERDBL6;

	public String getPLANT() {
		return sPLANT;
	}

	public String getPOMULTIESTNO() {
		return sPOMULTIESTNO;
	}

	public String getPOMULTIESTLNNO() {
		return sPOMULTIESTLNNO;
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

	public String getASNMT() {
		return sASNMT;
	}

        public double getCOST() {
                return dCOST;
        }

	public float getQTYOR() {
		return fQTYOR;
	}

	public float getQTYRC() {
		return fQTYRC;
	}

	public float getQTYAC() {
		return fQTYAC;
	}

	public float getQTYRJ() {
		return fQTYRJ;
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

	public void setPLANT(String aPLANT) {
		sPLANT = aPLANT;
	}

	public void setPOMULTIESTNO(String aPOMULTIESTNO) {
		sPOMULTIESTNO = aPOMULTIESTNO;
	}

	public void setPOLNNO(String aPOMULTIESTLNNO) {
		sPOMULTIESTLNNO = aPOMULTIESTLNNO;
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


	public void setASNMT(String aASNMT) {
		sASNMT = aASNMT;
	}

        public void setCOST(double aCOST) {
                dCOST = aCOST;
        }
	public void setQTYOR(float aQTYOR) {
		fQTYOR = aQTYOR;
	}

	public void setQTYRC(float aQTYRC) {
		fQTYRC = aQTYRC;
	}

	public void setQTYAC(float aQTYAC) {
		fQTYAC = aQTYAC;
	}

	public void setQTYRJ(float aQTYRJ) {
		fQTYRJ = aQTYRJ;
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
	 * constructor method PODET sb - SQL Bean gn - Utilites
	 */
	public PO_MULTI_ESTDET() throws Exception {
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
	 * Insert Command - PODET
	 * 
	 * 
	 */
	public int insertPODET() throws Exception {
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
			sql = "Insert into PODET values ('" + sPLANT + "','" + sPOMULTIESTNO
					+ "','" + sPOMULTIESTLNNO+ "','" + sLNSTAT + "','" + sITEM + "','"
					+ sASNMT + "'," + fQTYOR + "," + fQTYRC + "," + fQTYAC
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
	 * Update Function - PODET
	 * 
	 * 
	 */
	public int updatePODET() throws Exception {
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
			sql = "Update PODET set LNSTAT= '" + sLNSTAT + "',ITEM= '" + sITEM
					+ "',ASNMT= '" + sASNMT + "',QTYOR = " + fQTYOR
					+ ",QTYRC = " + fQTYRC + ",QTYAC = " + fQTYAC + ",QTYRJ = "
					+ fQTYRJ + ",LOC= '" + sLOC + "',WHID= '" + sWHID
					+ "',POLTYPE= '" + sPOLTYPE + "',UNITMO= '" + sUNITMO
					+ "',DELDATE= '" + sDELDATE + "',COMMENT1= '" + sCOMMENT1
					+ "',COMMENT2= '" + sCOMMENT2 + "',UPAT = '" + sUPAT
					+ "',UPBY= '" + sUPBY + "',RECSTAT= '" + sRECSTAT
					+ "',USERFLD1= '" + sUSERFLD1 + "',USERFLD2= '" + sUSERFLD2
					+ "',USERFLD3= '" + sUSERFLD3 + "',USERFLD4= '" + sUSERFLD4
					+ "',USERFLD5= '" + sUSERFLD5 + "',USERFLD6= '" + sUSERFLD6
					+ "',USERFLG1= '" + sUSERFLG1 + "',USERFLG2= '" + sUSERFLG2
					+ "',USERFLG3= '" + sUSERFLG3 + "',USERFLG4= '" + sUSERFLG4
					+ "',USERFLG5= '" + sUSERFLG5 + "',USERFLG6= '" + sUSERFLG6
					+ "',USERTIME1 = '" + sUSERTIME1 + "',USERTIME2 = '"
					+ sUSERTIME2 + "',USERTIME3 ='" + sUSERTIME3
					+ "',USERDBL1 = " + fUSERDBL1 + ",USERDBL2 = " + fUSERDBL2
					+ ",USERDBL3 = " + fUSERDBL3 + ",USERDBL4 = " + fUSERDBL4
					+ ",USERDBL5 = " + fUSERDBL5 + ",USERDBL6 = " + fUSERDBL6
					+ " where PLANT= '" + sPLANT + "' and PONO= '" + sPOMULTIESTNO
					+ "' and POLNNO= '" + sPOMULTIESTLNNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String getUpdateSql() throws Exception {
		String sql = "";
		try {
			sql = "Update PODET set LNSTAT= '" + sLNSTAT + "',ITEM= '" + sITEM
					+ "',ASNMT= '" + sASNMT + "',QTYOR = " + fQTYOR
					+ ",QTYRC = " + fQTYRC + ",QTYAC = " + fQTYAC + ",QTYRJ = "
					+ fQTYRJ + ",LOC= '" + sLOC + "',WHID= '" + sWHID
					+ "',POLTYPE= '" + sPOLTYPE + "',UNITMO= '" + sUNITMO
					+ "',DELDATE= '" + sDELDATE + "',COMMENT1= '" + sCOMMENT1
					+ "',COMMENT2= '" + sCOMMENT2 + "',UPAT = '" + sUPAT
					+ "',UPBY= '" + sUPBY + "',RECSTAT= '" + sRECSTAT
					+ "',USERFLD1= '" + sUSERFLD1 + "',USERFLD2= '" + sUSERFLD2
					+ "',USERFLD3= '" + sUSERFLD3 + "',USERFLD4= '" + sUSERFLD4
					+ "',USERFLD5= '" + sUSERFLD5 + "',USERFLD6= '" + sUSERFLD6
					+ "',USERFLG1= '" + sUSERFLG1 + "',USERFLG2= '" + sUSERFLG2
					+ "',USERFLG3= '" + sUSERFLG3 + "',USERFLG4= '" + sUSERFLG4
					+ "',USERFLG5= '" + sUSERFLG5 + "',USERFLG6= '" + sUSERFLG6
					+ "',USERTIME1 = '" + sUSERTIME1 + "',USERTIME2 = '"
					+ sUSERTIME2 + "',USERTIME3 ='" + sUSERTIME3
					+ "',USERDBL1 = " + fUSERDBL1 + ",USERDBL2 = " + fUSERDBL2
					+ ",USERDBL3 = " + fUSERDBL3 + ",USERDBL4 = " + fUSERDBL4
					+ ",USERDBL5 = " + fUSERDBL5 + ",USERDBL6 = " + fUSERDBL6
					+ " where PLANT= '" + sPLANT + "' and PONO= '" + sPOMULTIESTNO
					+ "' and POLNNO= '" + sPOMULTIESTLNNO + "'";
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sql;
	}

	/**
	 * Select FunctionPODET
	 * 
	 * 
	 */
	public void selectPODET() throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from PODET where PLANT= '" + IConstants.PLANT_VAL
					+ "' and POMULTIESTNO= '" + sPOMULTIESTNO + "' and POMULTIESTLNNO= '" + sPOMULTIESTLNNO
					+ "'";

			this.mLogger.query(this.printQuery, q);

			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sLNSTAT = rs.getString("LNSTAT");
				sITEM = rs.getString("ITEM");
				sASNMT = rs.getString("ASNMT");
				fQTYOR = rs.getFloat("QTYOR");
				fQTYRC = rs.getFloat("QTYRC");
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

	public void selectPODET(String plant) throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from " + "[" + plant + "_" + "PO_MULTI_ESTDET" + "]"
					+ " where PLANT= '" + plant + "' and POMULTIESTNO= '" + sPOMULTIESTNO
					+ "' and POMULTIESTLNNO= '" + sPOMULTIESTLNNO + "'";

			this.mLogger.query(this.printQuery, q);

			ResultSet rs = stmt.executeQuery(q);
//			int n = 1;
			while (rs.next()) {
				sLNSTAT = rs.getString("LNSTAT");
				sITEM = rs.getString("ITEM");
				sASNMT = rs.getString("ASNMT");
				fQTYOR = rs.getFloat("QTYOR");
				fQTYRC = rs.getFloat("QTYRC");
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

	public String listPODET(String aPOMULTIESTNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		NumberFormat formatter = new DecimalFormat("#,##0.00#");
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			if ((aPOMULTIESTNO.length() > 0)) {
				vecWhr.addElement("POMULTIESTNO = '" + aPOMULTIESTNO + "'");
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

			q = "select POMULTIESTNO,POMULTIESTLNNO,lnstat,item,userfld1 from MultiPoEstDet " + where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String POMULTIESTNO = rs.getString("POMULTIESTNO").trim();
				String POMULTIESTLNNO = rs.getString("POMULTIESTLNNO").trim();
				String item = rs.getString("item").trim();
				String desc = rs.getString("userfld1").trim();

				/*
				 * String action = request.getParameter("Submit").trim(); String
				 * pono = request.getParameter("PONO"); String vendno =
				 * request.getParameter("VENDNO"); String deldate =
				 * request.getParameter("DELDATE"); String origin =
				 * request.getParameter("USERFLD1");
				 */
				// link = "<a href=\""+ "createPODET.jsp?PONO="+pono+"\">";
				link = "<a href=\"" + "modiPODET.jsp?POMULTIESTNO=" + POMULTIESTNO + "&POMULTIESTLNNO="
						+ POMULTIESTLNNO + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">" + link
						+ "<FONT COLOR=\"blue\">" + POMULTIESTLNNO + "</a></font>"
						+ "</td>" + "<td width=\"20%\">" + sb.formatHTML(item)
						+ "</td>" + "<td width=\"40%\">" + sb.formatHTML(desc)
						+ "</td>" + "<td width=\"40%\">" + "" + "</td>"
						+ "</tr>";

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		System.out.println(result);
		return result;
	}

	//
	public String listPODET1(String aPOMULTIESTNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		NumberFormat formatter = new DecimalFormat("#,##0.00#");
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			if ((aPOMULTIESTNO.length() > 0)) {
				vecWhr.addElement("POMULTIESTNO = '" + aPOMULTIESTNO + "'");
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

			q = "select POMULTIESTNO,POMULTIESTLNNO,item,qtyor,qtyrc,qtyac,unitmo from podet "
					+ where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String POMULTIESTNO = rs.getString("POMULTIESTNO").trim();
				String POMULTIESTLNNO = rs.getString("POMULTIESTLNNO").trim();
				ITEMMST itm = new ITEMMST();
				String item = rs.getString("ITEM").trim();
				String desc = itm.getItemDesc(item);
				/*
				 * String action = request.getParameter("Submit").trim(); String
				 * pono = request.getParameter("PONO"); String vendno =
				 * request.getParameter("VENDNO"); String deldate =
				 * request.getParameter("DELDATE"); String origin =
				 * request.getParameter("USERFLD1");
				 */
				// link = "<a href=\""+ "createPODET.jsp?PONO="+pono+"\">";
				link = "<a href=\"" + "modiPODET.jsp?POMULTIESTNO=" + POMULTIESTNO + "&POMULTIESTLNNO="
						+ POMULTIESTNO + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">"
						+ link
						+ "<FONT COLOR=\"blue\">"
						+ POMULTIESTLNNO
						+ "</a></font>"
						+ "</td>"
						+ "<td width=\"20%\">"
						+ sb.formatHTML(item)
						+ "</td>"
						+ "<td width=\"40%\">"
						+ sb.formatHTML(desc)
						+ "</td>"
						+ "<td width=\"10%\" align=\"right\"> "
						+ sb.formatHTML(formatter.format(Double.parseDouble(rs
								.getString("QTYOR").trim()))) + "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(rs.getString("UNITMO").trim())
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

	//

	/**
	 * Delete Function -- PODET
	 * 
	 * 
	 */
	public int deletePODET(String aPOMULTIESTNO, String aPOMULTIESTLNNO) throws Exception {
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
			sql = "delete from PO_MULTI_ESTDET where PLANT= '" + sPLANT + "' and POMULTIESTNO= '"
					+ aPOMULTIESTNO + "' and POMULTIESTLNNO= '" + aPOMULTIESTLNNO + "'";

			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int deletePODETML(String aPOMULTIESTNO, String aPOMULTIESTLNNO, String plant)
			throws Exception {
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
			sql = "delete from " + "[" + plant + "_" + "PO_MULTI_ESTDET" + "]"
					+ " where PLANT= '" + plant + "' and POMULTIESTNO= '" + aPOMULTIESTNO
					+ "' and POMULTIESTLNNO= '" + aPOMULTIESTLNNO + "'";

			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
            sql = "update  " + "[" + plant + "_" + "PO_MULTI_ESTDET" + "]" 
            + "SET POMULTIESTLNNO =POMULTIESTLNNO-1 where PLANT= '" + plant + "' and POMULTIESTNO= '" + aPOMULTIESTNO+ "'  and POMULTIESTLNNO > '" + aPOMULTIESTLNNO + "' ";
            sb.insertRecords(sql);
            
            sql = "update  " + "[" + plant + "_" + "RECVDET" + "]" 
            + "SET LNNO =LNNO-1 where PLANT= '" + plant + "' and POMULTIESTNO= '" + aPOMULTIESTNO+ "'  and CAST(LNNO as int) > '" + aPOMULTIESTLNNO + "' ";
            sb.insertRecords(sql);
            
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int deletePODET(String aPOMULTIESTNO) throws Exception {
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
			sql = "delete from PO_MULTI_ESTDET where PLANT= '" + sPLANT + "' and POMULTIESTNO= '"
					+ aPOMULTIESTNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int deletePODETML(String aPOMULTIESTNO, String plant) throws Exception {
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
			sql = "delete from " + "[" + plant + "_" + "PO_MULTI_ESTDET" + "]"
					+ " where PLANT= '" + plant + "' and POMULTIESTNO= '" + aPOMULTIESTNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}














	
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
}