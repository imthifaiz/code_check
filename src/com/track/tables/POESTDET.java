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

public class POESTDET {
	private boolean printQuery = MLoggerConstant.POESTDET_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.POESTDET_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	String sPLANT = "SIS";
	String sPOESTNO = "";
	String sPOESTLNNO = "";
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

	public String getPOESTNO() {
		return sPOESTNO;
	}

	public String getPOESTLNNO() {
		return sPOESTLNNO;
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

	public void setPOESTNO(String aPOESTNO) {
		sPOESTNO = aPOESTNO;
	}

	public void setPOESTLNNO(String aPOESTLNNO) {
		sPOESTLNNO = aPOESTLNNO;
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
	public POESTDET() throws Exception {
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
			sql = "Insert into POESTDET values ('" + sPLANT + "','" + sPOESTNO
					+ "','" + sPOESTLNNO + "','" + sLNSTAT + "','" + sITEM + "','"
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
			sql = "Update POESTDET set LNSTAT= '" + sLNSTAT + "',ITEM= '" + sITEM
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
					+ " where PLANT= '" + sPLANT + "' and POESTNO= '" + sPOESTNO
					+ "' and POESTLNNO= '" + sPOESTLNNO + "'";
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
			sql = "Update POESTDET set LNSTAT= '" + sLNSTAT + "',ITEM= '" + sITEM
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
					+ " where PLANT= '" + sPLANT + "' and POESTNO= '" + sPOESTNO
					+ "' and POESTLNNO= '" + sPOESTLNNO + "'";
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
			q = "select * from POESTDET where PLANT= '" + IConstants.PLANT_VAL
					+ "' and POESTNO= '" + sPOESTNO + "' and POESTLNNO= '" + sPOESTLNNO
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
			q = "select * from " + "[" + plant + "_" + "POESTDET" + "]"
					+ " where PLANT= '" + plant + "' and POESTNO= '" + sPOESTNO
					+ "' and POESTLNNO= '" + sPOESTLNNO + "'";

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

	public String listPODET(String aPOESTNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		NumberFormat formatter = new DecimalFormat("#,##0.00#");
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			if ((aPOESTNO.length() > 0)) {
				vecWhr.addElement("POESTNO = '" + aPOESTNO + "'");
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

			q = "select POESTNO,POESTLNNO,lnstat,item,userfld1 from POESTDET " + where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String POESTNO = rs.getString("POESTNO").trim();
				String POESTLNNO = rs.getString("POESTLNNO").trim();
				String item = rs.getString("item").trim();
				String desc = rs.getString("userfld1").trim();

				/*
				 * String action = request.getParameter("Submit").trim(); String
				 * POESTNO = request.getParameter("POESTNO"); String vendno =
				 * request.getParameter("VENDNO"); String deldate =
				 * request.getParameter("DELDATE"); String origin =
				 * request.getParameter("USERFLD1");
				 */
				// link = "<a href=\""+ "createPODET.jsp?POESTNO="+POESTNO+"\">";
				link = "<a href=\"" + "modiPODET.jsp?POESTNO=" + POESTNO + "&POESTLNNO="
						+ POESTLNNO + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">" + link
						+ "<FONT COLOR=\"blue\">" + POESTLNNO + "</a></font>"
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
	public String listPODET1(String aPOESTNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		NumberFormat formatter = new DecimalFormat("#,##0.00#");
		Connection con = getDBConnection();

		try {
			Statement stmt = con.createStatement();
			if ((aPOESTNO.length() > 0)) {
				vecWhr.addElement("POESTNO = '" + aPOESTNO + "'");
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

			q = "select POESTNO,POESTLNNO,item,qtyor,qtyrc,qtyac,unitmo from poestdet "
					+ where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String POESTNO = rs.getString("POESTNO").trim();
				String POESTLNNO = rs.getString("POESTLNNO").trim();
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
				link = "<a href=\"" + "modiPODET.jsp?POESTNO=" + POESTNO + "&POESTLNNO="
						+ POESTLNNO + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">"
						+ link
						+ "<FONT COLOR=\"blue\">"
						+ POESTLNNO
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
	public int deletePODET(String aPOESTNO, String aPOESTLNNO) throws Exception {
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
			sql = "delete from POESTDET where PLANT= '" + sPLANT + "' and POESTNO= '"
					+ aPOESTNO + "' and POESTLNNO= '" + aPOESTLNNO + "'";

			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int deletePODETML(String aPOESTNO, String aPOESTLNNO, String plant)
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
			sql = "delete from " + "[" + plant + "_" + "POESTDET" + "]"
					+ " where PLANT= '" + plant + "' and POESTNO= '" + aPOESTNO
					+ "' and POESTLNNO= '" + aPOESTLNNO + "'";

			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
            sql = "update  " + "[" + plant + "_" + "POESTDET" + "]" 
            + "SET POESTLNNO =POESTLNNO-1 where PLANT= '" + plant + "' and POESTNO= '" + aPOESTNO+ "'  and POESTLNNO > '" + aPOESTLNNO + "' ";
            sb.insertRecords(sql);
            
            sql = "update  " + "[" + plant + "_" + "RECVDET" + "]" 
            + "SET LNNO =LNNO-1 where PLANT= '" + plant + "' and POESTNO= '" + aPOESTNO+ "'  and CAST(LNNO as int) > '" + aPOESTLNNO + "' ";
            sb.insertRecords(sql);
            
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int deletePODET(String aPOESTNO) throws Exception {
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
			sql = "delete from POESTDET where PLANT= '" + sPLANT + "' and POESTNO= '"
					+ aPOESTNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int deletePODETML(String aPOESTNO, String plant) throws Exception {
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
			sql = "delete from " + "[" + plant + "_" + "POESTDET" + "]"
					+ " where PLANT= '" + plant + "' and POESTNO= '" + aPOESTNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public void processLoading(String filepath) throws Exception {
		StrUtils str = new StrUtils();
		Connection con = getDBConnection();
		con.setAutoCommit(false);
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		String line = null;
		while ((line = reader.readLine()) != null) {
			Vector vecStr = str.parseStringGetAll(line, ",");
			sPOESTNO = vecStr.elementAt(1).toString();
			sPOESTLNNO = vecStr.elementAt(3).toString();
			sITEM = vecStr.elementAt(10).toString();
			sCOMMENT1 = vecStr.elementAt(11).toString();
			fQTYOR = Float.parseFloat(vecStr.elementAt(18).toString());
			sUNITMO = vecStr.elementAt(20).toString();
			insertPODET();
		}
		con.commit();
		con.setAutoCommit(true);

	}

	/*
	 * con.setAutoCommit(false); PreparedStatement updateSales =
	 * con.prepareStatement(
	 * "UPDATE COFFEES SET SALES = ? WHERE COF_NAME LIKE ?");
	 * updateSales.setInt(1, 50); updateSales.setString(2, "Colombian");
	 * updateSales.executeUpdate(); PreparedStatement updateTotal =
	 * con.prepareStatement(
	 * "UPDATE COFFEES SET TOTAL = TOTAL + ? WHERE COF_NAME LIKE ?");
	 * updateTotal.setInt(1, 50); updateTotal.setString(2, "Colombian");
	 * updateTotal.executeUpdate(); con.commit(); con.setAutoCommit(true);
	 */

	public String listPODET4REC(String aPOESTNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if ((aPOESTNO.length() > 0)) {
				vecWhr.addElement("POESTNO = '" + aPOESTNO + "'");
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
			q = "select POESTNO,POESTLNNO,lnstat,item,qtyor,qtyrc,qtyac,unitmo from POESTDET "
					+ where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String POESTNO = rs.getString("POESTNO").trim();
				String POESTLNNO = rs.getString("POESTLNNO").trim();
				String lnstat = rs.getString("LNSTAT").trim();
				ITEMMST itm = new ITEMMST();
				String item = rs.getString("ITEM").trim();
				String qtyor = rs.getString("QTYOR").trim();
				String qtyrc = rs.getString("QTYRC").trim();
				float bal = Float.parseFloat(qtyor) - Float.parseFloat(qtyrc);
				String desc = itm.getItemDesc(item);
				link = "<a href=\"" + "recvQty.jsp?POESTNO=" + POESTNO + "&POESTLNNO="
						+ POESTLNNO + "\">";
				if (lnstat.equalsIgnoreCase("C")) {
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"15%\">" + POESTLNNO
							+ "</td>" + "<td width=\"15%\">"
							+ sb.formatHTML(item) + "</td>"
							+ "<td width=\"30%\">" + sb.formatHTML(desc)
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(qtyor) + "</td>"
							+ "<td width=\"10%\">" + sb.formatHTML(bal + "")
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("QTYAC").trim())
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("UNITMO").trim())
							+ "</td></tr>";

				} else {
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"15%\">"
							+ link
							+ "<FONT COLOR=\"blue\">"
							+ POESTLNNO
							+ "</font></a>"
							+ "</td>"
							+ "<td width=\"15%\">"
							+ sb.formatHTML(item)
							+ "</td>"
							+ "<td width=\"30%\">"
							+ sb.formatHTML(desc)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(qtyor)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(bal + "")
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("QTYAC").trim())
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("UNITMO").trim())
							+ "</td></tr>";
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

	public String listPdaPODET(String aPOESTNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if ((aPOESTNO.length() > 0)) {
				vecWhr.addElement("POESTNO = '" + aPOESTNO + "'");
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

			q = "select POESTNO,POESTLNNO,lnstat,item,qtyor,qtyrc,qtyac,unitmo from POESTDET "
					+ where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String POESTNO = rs.getString("POESTNO").trim();
				String POESTLNNO = rs.getString("POESTLNNO").trim();
				String lnstat = rs.getString("LNSTAT").trim();
				ITEMMST itm = new ITEMMST();
				String item = rs.getString("ITEM").trim();
				String qtyor = rs.getString("QTYOR").trim();
				String qtyrc = rs.getString("QTYRC").trim();
				float bal = Float.parseFloat(qtyor) - Float.parseFloat(qtyrc);
				String desc = itm.getItemDesc(item);
				link = "<a href=\"" + "pdaRecvQty.jsp?POESTNO=" + POESTNO
						+ "&POESTLNNO=" + POESTLNNO + "\">";
				if (lnstat.equalsIgnoreCase("C")) {
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"20%\">" + POESTLNNO
							+ "</td>" + "<td width=\"20%\">"
							+ sb.formatHTML(item) + "</td>"
							+ "<td width=\"60%\">" + sb.formatHTML(desc)
							+ "</td></tr>";

				} else {
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"20%\">" + link
							+ "<FONT COLOR=\"blue\">" + POESTLNNO + "</font></a>"
							+ "</td>" + "<td width=\"20%\">"
							+ sb.formatHTML(item) + "</td>"
							+ "<td width=\"60%\">" + sb.formatHTML(desc)
							+ "</td></tr>";
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

	public String listPOReport(String aFRDT, String aTODT, String aSUPP,
			String aSTATUS) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		POESTHDR ph = new POESTHDR();
		VENDMST vm = new VENDMST();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if (aFRDT.length() > 0) {
				vecWhr.addElement("a.CRAT > '" + aFRDT + "'");
			}
			if (aTODT.length() > 0) {
				vecWhr.addElement("a.CRAT < '" + aTODT + "'");
			}
			if (aSTATUS.length() > 0) {
				vecWhr.addElement("a.ITEM = '" + aSTATUS + "'");
			}
			if (aSUPP.length() > 0) {
				vecWhr.addElement("c.VENDNO = '" + aSUPP + "'");
			}
			int cnt = vecWhr.size();
			if (cnt > 0) {
				where = "where  a.POESTNO = c.POESTNO and a.item = b.item and ";
				for (int i = 0; i < cnt; i++) {
					where = where + vecWhr.elementAt(i).toString() + " ";
					if (i < cnt - 1) {
						where = where + " and ";
					}
				}
			}
			q = "select a.POESTNO,a.item,b.itemdesc,a.qtyor,a.qtyrc,c.vendno,c.deldate from POESTDET a , itemmst b, "
					+ " POESTHDR c " + where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String POESTNO = rs.getString("POESTNO").trim();
				ITEMMST itm = new ITEMMST();
				String item = rs.getString("ITEM").trim();
				String itemdesc = rs.getString("ITEMDESC").trim();
				String qtyor = rs.getString("QTYOR").trim();
				String qtyrc = rs.getString("QTYRC").trim();
				float bal = Float.parseFloat(qtyor) - Float.parseFloat(qtyrc);
				NumberFormat formatter = new DecimalFormat("#,##0.00#");
				String vend = rs.getString("VENDNO").trim();
				String vname = vm.getVendDesc(vend);
				String deldate = rs.getString("DELDATE").trim();
				link = "<a href=\"" + "createPO.jsp?POESTNO=" + POESTNO + "\">";
				if (bal > 0) {
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"10%\">"
							+ link
							+ "<FONT COLOR=\"blue\">"
							+ POESTNO
							+ "</font></a>"
							+ "</td>"
							+ "<td width=\"15%\">"
							+ vname
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(item)
							+ "</td>"
							+ "<td width=\"20%\">"
							+ sb.formatHTML(itemdesc)
							+ "</td>"
							+ "<td align=\"right\" width=\"10%\">"
							+ sb.formatHTML(formatter.format(Double
									.parseDouble(qtyor)))
							+ "</td>"
							+ "<td align=\"right\" width=\"10%\">"
							+ sb.formatHTML(formatter.format(Double
									.parseDouble(qtyrc)))
							+ "</td>"
							+ "<td align=\"right\" width=\"10%\">"
							+ sb.formatHTML(formatter.format(bal) + "")
							+ "</td>"
							+ "<td width=\"15%\">"
							+ sb.formatHTML(gn.getDB2UserDate(deldate))
							+ "</td></tr>";

				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String RptlistPOReport(String aFRDT, String aTODT, String aSUPP,
			String aSTATUS) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		POESTHDR ph = new POESTHDR();
		VENDMST vm = new VENDMST();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if (aFRDT.length() > 0) {
				vecWhr.addElement("a.CRAT > '" + aFRDT + "'");
			}
			if (aTODT.length() > 0) {
				vecWhr.addElement("a.CRAT < '" + aTODT + "'");
			}
			if (aSTATUS.length() > 0) {
				vecWhr.addElement("a.ITEM = '" + aSTATUS + "'");
			}
			if (aSUPP.length() > 0) {
				vecWhr.addElement("c.VENDNO = '" + aSUPP + "'");
			}
			int cnt = vecWhr.size();
			if (cnt > 0) {
				where = "where  a.POESTNO = c.POESTNO and a.item = b.item and ";
				for (int i = 0; i < cnt; i++) {
					where = where + vecWhr.elementAt(i).toString() + " ";
					if (i < cnt - 1) {
						where = where + " and ";
					}
				}
			}
			q = "select a.POESTNO,a.item,b.itemdesc,a.qtyor,a.qtyrc,c.vendno,c.orddate from POESTDET a , itemmst b, "
					+ " POESTHDR c " + where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String POESTNO = rs.getString("POESTNO").trim();
				ITEMMST itm = new ITEMMST();
				String item = rs.getString("ITEM").trim();
				String itemdesc = rs.getString("ITEMDESC").trim();
				String qtyor = rs.getString("QTYOR").trim();
				String qtyrc = rs.getString("QTYRC").trim();
				float bal = Float.parseFloat(qtyor) - Float.parseFloat(qtyrc);
				NumberFormat formatter = new DecimalFormat("#,##0.00#");
				String vend = rs.getString("VENDNO").trim();
				String vname = vm.getVendDesc(vend);
				String orddate = rs.getString("ORDDATE").trim();
				if (bal > 0) {
					result += "<tr valign=\"middle\">" +
					// "<td align=\"center\" width=\"10%\">"+"<FONT COLOR=\"blue\">"+
							// POESTNO +"</font>" +"</td>"+
							"<td width=\"10%\">"
							+ POESTNO
							+ "</td>"
							+ "<td width=\"15%\">"
							+ vname
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(item)
							+ "</td>"
							+ "<td width=\"20%\">"
							+ sb.formatHTML(itemdesc)
							+ "</td>"
							+ "<td align=\"right\" width=\"10%\">"
							+ sb.formatHTML(formatter.format(Double
									.parseDouble(qtyor)))
							+ "</td>"
							+ "<td align=\"right\" width=\"10%\">"
							+ sb.formatHTML(formatter.format(Double
									.parseDouble(qtyrc))) + "</td>"
							+ "<td align=\"right\" width=\"10%\">"
							+ sb.formatHTML(formatter.format(bal) + "")
							+ "</td>" + "<td width=\"15%\">"
							+ sb.formatHTML(gn.getDB2UserDate(orddate))
							+ "</td></tr>";

				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String getNextLineNo(String aPOESTNO) throws Exception {
		String q = "";
		String result = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select max(cast(POESTLNNO as int))+ 1 from POESTDET where POESTNO = '"
					+ aPOESTNO + "'";
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

	public String getNextLineNo(String aPOESTNO, String plant) throws Exception {
		String q = "";
		String result = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select max(cast(POESTLNNO as int))+ 1 from " + "[" + plant + "_"
					+ "POESTDET" + "]" + " where POESTNO = '" + aPOESTNO + "'";
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

	public String listPOAllReport(String aFRDT, String aTODT, String aSUPP,
			String aITEM) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		POESTHDR ph = new POESTHDR();
		VENDMST vm = new VENDMST();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if (aFRDT.length() > 0) {
				vecWhr.addElement("b.CRAT > '" + aFRDT + "'");
			}
			if (aTODT.length() > 0) {
				vecWhr.addElement("b.CRAT < '" + aTODT + "'");
			}
			if (aSUPP.length() > 0) {
				vecWhr.addElement("a.VENDNO = '" + aSUPP + "'");
			}
			if (aITEM.length() > 0) {
				vecWhr.addElement("b.ITEM = '" + aITEM + "'");
			}
			int cnt = vecWhr.size();
			if (cnt > 0) {
				where = "where a.POESTNO = b.POESTNO and ";
				for (int i = 0; i < cnt; i++) {
					where = where + vecWhr.elementAt(i).toString() + " ";
					if (i < cnt - 1) {
						where = where + " and ";
					}
				}
			}
			q = "select a.POESTNO,a.VENDNO,b.ITEM,b.QTYOR,a.ORDDATE from POESTHDR a, POESTDET b "
					+ where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String POESTNO = rs.getString("POESTNO").trim();
				ITEMMST itm = new ITEMMST();
				String item = rs.getString("ITEM").trim();
				String qtyor = rs.getString("QTYOR").trim();
				NumberFormat formatter = new DecimalFormat("#,##0.00#");
				String desc = itm.getItemDesc(item);
				result += "<tr valign=\"middle\">"
						+ "<td  width=\"10%\">"
						+ POESTNO
						+ "</td>"
						+ "<td width=\"20%\">"
						+ vm.getVendDesc(ph.getVendorCode(POESTNO))
						+ "</td>"
						+ "<td width=\"15%\">"
						+ sb.formatHTML(item)
						+ "</td>"
						+ "<td width=\"25%\">"
						+ sb.formatHTML(desc)
						+ "</td>"
						+ "<td align=\"right\" width=\"15%\">"
						+ sb.formatHTML(formatter.format(Double
								.parseDouble(qtyor)))
						+ "</td>"
						+ "<td width=\"15%\">"
						+ sb.formatHTML(gn.getDB2UserDate(ph
								.getDateRequired(POESTNO))) + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	/**
	 * Quantity Updation Function -- PODET
	 * 
	 * 
	 */
	public int updatePODET(String aPOESTNO, String aPOESTLNNO, float qty)
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
			sql = "UPDATE POESTDET SET QTYOR = " + qty + " WHERE PLANT= '"
					+ sPLANT + "' and POESTNO= '" + aPOESTNO + "' and POESTLNNO= '"
					+ aPOESTLNNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String listItems4MR(String POESTNO) throws Exception {
		String recStr = "";
		String link = "";
		String itm = "", itemdesc = "", uom = "", status = "";
		float qty = 0;
		String q = "select ITEM,UNITMO,LNSTAT,QTYOR from POESTDET where  POESTNO='"
				+ POESTNO + "'";
		// System.out.print("q = " +q);
		Connection con = getDBConnection();
		try {
			this.mLogger.query(this.printQuery, q);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			ITEMMST im = new ITEMMST();
			while (rs.next()) {
				itm = rs.getString("ITEM");
				itemdesc = im.getItemDesc(itm.trim());
				uom = rs.getString("UNITMO");
				status = rs.getString("LNSTAT");
				qty = rs.getFloat("QTYOR");
				link = "<a href=\"javascript:window.opener.form.ITEM.value="
						+ "'" + itm + "';"
						+ "window.opener.form.ITEMDESC.value=" + "'" + itemdesc
						+ "';" + "window.opener.form.QTYOR.value=" + "'" + qty
						+ "';" + "window.opener.form.STKUOM.value=" + "'" + uom
						+ "';" + "window.close();\">";
				recStr += "<tr bgcolor=\"#eeeeee\"><td>" + link + itm
						+ "</a></td><td>" + itemdesc + "</td><td>" + uom
						+ "</td><td>" + status + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return recStr;
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
}