package com.track.tables;

// importing classes

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.sqlBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class PCKALLOC {
	private boolean printQuery = MLoggerConstant.PCKALLOC_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.PCKALLOC_PRINTPLANTMASTERLOG;
	private final boolean DEBUG = true;
	sqlBean sb;
	Generator gn;
	StrUtils _strUtils = null;
	String sPLANT = "SIS";
	String sREFNO = "";
	String sREFLNNO = "";
	String sBIN = "";
	String sBATCH = "";
	String sORDTYPE = "";
	String sITEM = "";
	String sCLASS = "";
	String sATTRIB = "";
	float fQTY;
	float fPCKQTY;
	String sUOM = "";
	String sPCKBIN = "";
	String sSTATUS = "";
	String sDRPLOC = "";
	String sLPLTNO = "";
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

	public String getREFNO() {
		return sREFNO;
	}

	public String getREFLNNO() {
		return sREFLNNO;
	}

	public String getBIN() {
		return sBIN;
	}

	public String getBATCH() {
		return sBATCH;
	}

	public String getORDTYPE() {
		return sORDTYPE;
	}

	public String getITEM() {
		return sITEM;
	}

	public String getCLASS() {
		return sCLASS;
	}

	public String getATTRIB() {
		return sATTRIB;
	}

	public float getQTY() {
		return fQTY;
	}

	public float getPCKQTY() {
		return fPCKQTY;
	}

	public String getUOM() {
		return sUOM;
	}

	public String getPCKBIN() {
		return sPCKBIN;
	}

	public String getSTATUS() {
		return sSTATUS;
	}

	public String getDRPLOC() {
		return sDRPLOC;
	}

	public String getLPLTNO() {
		return sLPLTNO;
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

	public void setREFNO(String aREFNO) {
		sREFNO = aREFNO;
	}

	public void setREFLNNO(String aREFLNNO) {
		sREFLNNO = aREFLNNO;
	}

	public void setBIN(String aBIN) {
		sBIN = aBIN;
	}

	public void setBATCH(String aBATCH) {
		sBATCH = aBATCH;
	}

	public void setORDTYPE(String aORDTYPE) {
		sORDTYPE = aORDTYPE;
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

	public void setQTY(float aQTY) {
		fQTY = aQTY;
	}

	public void setPCKQTY(float aPCKQTY) {
		fPCKQTY = aPCKQTY;
	}

	public void setUOM(String aUOM) {
		sUOM = aUOM;
	}

	public void setPCKBIN(String aPCKBIN) {
		sPCKBIN = aPCKBIN;
	}

	public void setSTATUS(String aSTATUS) {
		sSTATUS = aSTATUS;
	}

	public void setDRPLOC(String aDRPLOC) {
		sDRPLOC = aDRPLOC;
	}

	public void setLPLTNO(String aLPLTNO) {
		sLPLTNO = aLPLTNO;
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
	 * constructor method PCKALLOC sb - SQL Bean gn - Utilites
	 */
	public PCKALLOC() throws Exception {
		sb = new sqlBean();
		gn = new Generator();
		_strUtils = new StrUtils();
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
	 * Insert Command - PCKALLOC
	 * 
	 * 
	 */
	public int insertPCKALLOC() throws Exception {
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
			sql = "Insert into PCKALLOC values ('" + sPLANT + "','" + sREFNO
					+ "','" + sREFLNNO + "','" + sBIN + "','" + sBATCH + "','"
					+ sORDTYPE + "','" + sITEM + "','" + sCLASS + "','"
					+ sATTRIB + "'," + fQTY + "," + fPCKQTY + ",'" + sUOM
					+ "','" + sPCKBIN + "','" + sSTATUS + "','" + sDRPLOC
					+ "','" + sLPLTNO + "','" + sCRAT + "','" + sCRBY + "','"
					+ sUPAT + "','" + sUPBY + "','" + sRECSTAT + "','"
					+ sUSERFLD1 + "','" + sUSERFLD2 + "','" + sUSERFLD3 + "','"
					+ sUSERFLD4 + "','" + sUSERFLD5 + "','" + sUSERFLD6 + "','"
					+ sUSERFLG1 + "','" + sUSERFLG2 + "','" + sUSERFLG3 + "','"
					+ sUSERFLG4 + "','" + sUSERFLG5 + "','" + sUSERFLG6 + "','"
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
		return returnCode;
	}

	public String getInsertSQL() throws Exception {
		String sql;

		sql = "Insert into PCKALLOC values ('" + sPLANT + "','" + sREFNO
				+ "','" + sREFLNNO + "','" + sBIN + "','" + sBATCH + "','"
				+ sORDTYPE + "','" + sITEM + "','" + sCLASS + "','" + sATTRIB
				+ "'," + fQTY + "," + fPCKQTY + ",'" + sUOM + "','" + sPCKBIN
				+ "','" + sSTATUS + "','" + sDRPLOC + "','" + sLPLTNO + "','"
				+ sCRAT + "','" + sCRBY + "','" + sUPAT + "','" + sUPBY + "','"
				+ sRECSTAT + "','" + sUSERFLD1 + "','" + sUSERFLD2 + "','"
				+ sUSERFLD3 + "','" + sUSERFLD4 + "','" + sUSERFLD5 + "','"
				+ sUSERFLD6 + "','" + sUSERFLG1 + "','" + sUSERFLG2 + "','"
				+ sUSERFLG3 + "','" + sUSERFLG4 + "','" + sUSERFLG5 + "','"
				+ sUSERFLG6 + "','" + sUSERTIME1 + "','" + sUSERTIME2 + "','"
				+ sUSERTIME3 + "'," + fUSERDBL1 + "," + fUSERDBL2 + ","
				+ fUSERDBL3 + "," + fUSERDBL4 + "," + fUSERDBL5 + ","
				+ fUSERDBL6 + ")";
		this.mLogger.query(this.printQuery, sql);
		return sql;
	}

	/**
	 * Update Function - PCKALLOC
	 * 
	 * 
	 */
	public int updatePCKALLOC() throws Exception {
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
			sql = "Update PCKALLOC set ORDTYPE= '" + sORDTYPE + "',ITEM= '"
					+ sITEM + "',CLASS= '" + sCLASS + "',ATTRIB= '" + sATTRIB
					+ "',QTY = " + fQTY + ",PCKQTY = " + fPCKQTY + ",UOM= '"
					+ sUOM + "',PCKBIN= '" + sPCKBIN + "',STATUS= '" + sSTATUS
					+ "',DRPLOC= '" + sDRPLOC + "',LPLTNO= '" + sLPLTNO
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
					+ " where PLANT= '" + sPLANT + "' and REFNO= '" + sREFNO
					+ "' and REFLNNO= '" + sREFLNNO + "' and BIN= '" + sBIN
					+ "' and BATCH= '" + sBATCH + "'";
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
	 * Select FunctionPCKALLOC
	 * 
	 * 
	 */
	public void selectPCKALLOC() throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from PCKALLOC where PLANT= '" + sPLANT
					+ "' and REFNO= '" + sREFNO + "' and REFLNNO= '" + sREFLNNO
					+ "' and BIN= '" + sBIN + "' and BATCH= '" + sBATCH + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sORDTYPE = rs.getString("ORDTYPE");
				sITEM = rs.getString("ITEM");
				sCLASS = rs.getString("CLASS");
				sATTRIB = rs.getString("ATTRIB");
				fQTY = rs.getFloat("QTY");
				fPCKQTY = rs.getFloat("PCKQTY");
				sUOM = rs.getString("UOM");
				sPCKBIN = rs.getString("PCKBIN");
				sSTATUS = rs.getString("STATUS");
				sDRPLOC = rs.getString("DRPLOC");
				sLPLTNO = rs.getString("LPLTNO");
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
	 * Delete Function -- PCKALLOC
	 * 
	 * 
	 */
	public int deletePCKALLOC() throws Exception {
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
			sql = "delete from PCKALLOC where PLANT= '" + sPLANT
					+ "' and REFNO= '" + sREFNO + "' and REFLNNO= '" + sREFLNNO
					+ "' and BIN= '" + sBIN + "' and BATCH= '" + sBATCH + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String getNextReflnno(String aRefNo) throws Exception {
		String recId = "";
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select max(reflnno)+1 from pckalloc where Plant='PLNT' and REFNO = '"
					+ aRefNo + "'";
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

	public String getInsertSql() throws Exception {
		String sql = "";
		sql = "Insert into PCKALLOC values ('" + sPLANT + "','" + sREFNO
				+ "','" + sREFLNNO + "','" + sBIN + "','" + sBATCH + "','"
				+ sORDTYPE + "','" + sITEM + "','" + sCLASS + "','" + sATTRIB
				+ "'," + fQTY + "," + fPCKQTY + ",'" + sUOM + "','" + sPCKBIN
				+ "','" + sSTATUS + "','" + sDRPLOC + "','" + sLPLTNO + "','"
				+ sCRAT + "','" + sCRBY + "','" + sUPAT + "','" + sUPBY + "','"
				+ sRECSTAT + "','" + sUSERFLD1 + "','" + sUSERFLD2 + "','"
				+ sUSERFLD3 + "','" + sUSERFLD4 + "','" + sUSERFLD5 + "','"
				+ sUSERFLD6 + "','" + sUSERFLG1 + "','" + sUSERFLG2 + "','"
				+ sUSERFLG3 + "','" + sUSERFLG4 + "','" + sUSERFLG5 + "','"
				+ sUSERFLG6 + "','" + sUSERTIME1 + "','" + sUSERTIME2 + "','"
				+ sUSERTIME3 + "'," + fUSERDBL1 + "," + fUSERDBL2 + ","
				+ fUSERDBL3 + "," + fUSERDBL4 + "," + fUSERDBL5 + ","
				+ fUSERDBL6 + ")";
		return sql;
	}

	public String getNextDono(String ordtype) throws Exception {
		String recId = "", res = "";
		String q = "";
		Connection con = getDBConnection();
		q = "select max(reflnno) from pckalloc where Plant='PLNT' and ORDTYPE = '"
				+ ordtype + "'";
		this.mLogger.query(this.printQuery, q);

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				recId = rs.getString(1);
				if (recId != null) {
					recId = recId.replace(ordtype.charAt(0), '0');
					StringBuffer s = new StringBuffer(recId);
					s.deleteCharAt(0);
					// System.out.println(s);
					n = Integer.parseInt(recId) + 1;
					// System.out.println("1" + n);
					NumberFormat formatter = new DecimalFormat("0000");
					res = ordtype + formatter.format(n);
					// System.out.println("2" + res);
				} else {
					res = "D0001";

				}

				recId = rs.getString(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return res;
	}

	public boolean processAllocation(String sbin, String dbin, float qty)
			throws Exception {
		INVMST iv = new INVMST();
		String sql;
		ArrayList al = new ArrayList();
		boolean retBool = false;
		int ret1 = 1;
		int ret2 = 1;
		ResultSet rs = null;
		try {
			if (iv.isAvaliable(sITEM, sCLASS, sATTRIB, sBATCH, sSTATUS, dbin)) {
				System.out.println(getInsertSql());
				// select QTY from INVMST where PLANT= 'PLNT' and REFNO='REFNO'
				// and BIN = 'BIN' and BATCH = 'BATCH' and ITEM = '' and CLASS =
				// '' and ATTRIB''
				// ITEM= '" "' and CLASS= '" + clas + "' and ATTRIB= '" + attrib
				// +

				// Insert into Pckalloc
				// sql = "update invmst set qty = qty + " + qty +
				// " where PLANT= 'PLNT' and ITEM= '"+sITEM+"' and CLASS= '"+sCLASS+"' and ATTRIB= '"+sATTRIB+"' and BATCH= '"+sBATCH+"' and BINNO= '"+dbin+"' and STATUS= '"+sSTATUS+"'";
				// al.add(sql);
				// Update qty Pckalloc
				// sql = "update invmst set qty = qty - " + qty +
				// " where PLANT= 'PLNT' and ITEM= '"+sITEM+"' and CLASS= '"+sCLASS+"' and ATTRIB= '"+sATTRIB+"' and BATCH= '"+sBATCH+"' and BINNO= '"+sbin+"' and STATUS= '"+sSTATUS+"'";
				// al.add(sql);
				// retBool = sb.insertBatchRecords(al);

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return retBool;
	}

	public String listPicking(String aREFNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select item,batch,bin,class,attrib,qty,pckqty,status from pckalloc where qty > 0 and REFNO = '"
					+ aREFNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String item = rs.getString("ITEM").trim();
				String batch = rs.getString("BATCH").trim();
				String bin = rs.getString("BIN").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				String qty = rs.getString("QTY").trim();
				String pckqty = rs.getString("PCKQTY").trim();
				float qtyBal = Float.parseFloat(qty);
				float qtyPck = Float.parseFloat(pckqty);
				ITEMMST itm = new ITEMMST();
				String desc = itm.getItemDesc(item);
				String stat = rs.getString("STATUS").trim();

				if (qtyPck >= qtyBal) {
					result += "<tr valign=\"middle\">" + "<td align='center'>"
							+ item + "</td>" + "<td align='center'>&nbsp;"
							+ sb.formatHTML(clas) + "</td>"
							+ "<td align='center'>&nbsp;"
							+ sb.formatHTML(attrib) + "</td>"
							+ "<td align='center'>&nbsp;"
							+ sb.formatHTML(batch) + "</td>"
							+ "<td align='right'>&nbsp;" + sb.formatHTML(qty)
							+ "</td>" + "<td align='right'>&nbsp;"
							+ sb.formatHTML(pckqty) + "</td></tr>";
				} else {
					link = "<a href=\"" + "pickQty.jsp?ITEM=" + item
							+ "&BATCH=" + batch + "&BINNO=" + bin + "&CLASS="
							+ clas + "&ATTRIB=" + attrib + "&REFNO=" + aREFNO
							+ "&QTY=" + qty + "\">";
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\">" + link
							+ "<FONT COLOR=\"blue\" align='center'>" + item
							+ "</FONT></td>" + "<td align='center'>&nbsp;"
							+ sb.formatHTML(clas) + "</td>"
							+ "<td align='center'>&nbsp;"
							+ sb.formatHTML(attrib) + "</td>"
							+ "<td align='center'>&nbsp;"
							+ sb.formatHTML(batch) + "</td>"
							+ "<td align='right'>&nbsp;" + sb.formatHTML(qty)
							+ "</td>" + "<td align='right'>&nbsp;"
							+ sb.formatHTML(pckqty) + "</td></tr>";

				}

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String listPicking4GI(String aREFNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select item,batch,bin,class,attrib,qty,pckqty,status from pckalloc where qty > 0 and REFNO = '"
					+ aREFNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String item = rs.getString("ITEM").trim();
				String batch = rs.getString("BATCH").trim();
				String bin = rs.getString("BIN").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				String qty = rs.getString("QTY").trim();
				String pckqty = rs.getString("PCKQTY").trim();
				float qtyBal = Float.parseFloat(qty);
				float qtyPck = Float.parseFloat(pckqty);
				ITEMMST itm = new ITEMMST();
				String desc = itm.getItemDesc(item);
				String stat = rs.getString("STATUS").trim();

				if (qtyPck >= qtyBal) {
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"50%\">" + item
							+ "</td>" + "<td width=\"20%\">"
							+ sb.formatHTML(qty) + "</td>"
							+ "<td width=\"20%\">" + sb.formatHTML(pckqty)
							+ "</td>" + "<td width=\"30%\">"
							+ sb.formatHTML(stat) + "</td></tr>";
				} else {
					link = "<a href=\"" + "pickQty.jsp?ITEM=" + item
							+ "&BATCH=" + batch + "&BINNO=" + bin + "&CLASS="
							+ clas + "&ATTRIB=" + attrib + "&REFNO=" + aREFNO
							+ "&QTY=" + qty + "\">";
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"50%\">" + link
							+ "<FONT COLOR=\"blue\">" + item + "</FONT></td>"
							+ "<td width=\"20%\">" + sb.formatHTML(qty)
							+ "</td>" + "<td width=\"20%\">"
							+ sb.formatHTML(pckqty) + "</td>"
							+ "<td width=\"30%\">" + sb.formatHTML(stat)
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

	public int getCount(String refno) throws Exception {
		int count = 0;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) as totalrecs from PCKALLOC where PLANT= 'PLNT' and REFNO= '"
					+ refno + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			if (rs.next()) {
				count = rs.getInt("totalrecs");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}

		return count;
	}

	public String listPdaPicking(String aREFNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select item,batch,bin,class,attrib,qty,pckqty,status from pckalloc where qty > 0 and REFNO = '"
					+ aREFNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			while (rs.next()) {
				String item = rs.getString("ITEM").trim();
				String batch = rs.getString("BATCH").trim();
				String bin = rs.getString("BIN").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				String qty = rs.getString("QTY").trim();
				String pckqty = rs.getString("PCKQTY").trim();
				float qtyBal = Float.parseFloat(qty);
				float qtyPck = Float.parseFloat(pckqty);
				ITEMMST itm = new ITEMMST();
				String desc = itm.getItemDesc(item);
				String stat = rs.getString("STATUS").trim();

				if (qtyPck >= qtyBal) {
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"30%\">" + item
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(qty) + "</td>"
							+ "<td width=\"10%\">" + sb.formatHTML(pckqty)
							+ "</td></tr>";
				} else {
					link = "<a href=\"" + "pdapickQty.jsp?ITEM=" + item
							+ "&BATCH=" + batch + "&BINNO=" + bin + "&CLASS="
							+ clas + "&ATTRIB=" + attrib + "&REFNO=" + aREFNO
							+ "&QTY=" + qty + "\">";
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"30%\">" + link
							+ "<FONT COLOR=\"blue\">" + item + "</FONT></td>"
							+ "<td width=\"10%\">" + sb.formatHTML(qty)
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(pckqty) + "</td></tr>";

				}

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	// select distinct refno ,usertime1 from pckalloc where usertime1 between
	// '20031111' and '20031112'

	public String listPCKALLOC(String aFROMDATE, String aTODATE, String aREFNO)
			throws Exception {
		String q = "";
		String link = "";
		String result = "", where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			if ((aFROMDATE.length() > 0) && (aTODATE.length() > 0)) {
				vecWhr.addElement(" USERTIME1 BETWEEN  '" + aFROMDATE
						+ "' AND '" + aTODATE + "'");
			}
			if ((aREFNO.length() > 0)) {
				vecWhr.addElement(" REFNO = '" + aREFNO + "'");
			}
			int cnt = vecWhr.size();
			if (cnt > 0) {
				where = "where status = 'N' and ";
				for (int i = 0; i < cnt; i++) {
					where = where + vecWhr.elementAt(i).toString() + " ";
					if (i < cnt - 1) {
						where = where + " and ";
					}
				}
			}
			Statement stmt = con.createStatement();
			q = "select distinct refno ,status, usertime1 from pckalloc "
					+ where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String refno = rs.getString("REFNO").trim();
				String stat = rs.getString("STATUS").trim();
				String deldate = rs.getString("USERTIME1").trim();
				link = "<a href=\"" + "pickDet.jsp?ORDNO=" + refno + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"40%\">" + link
						+ "<FONT COLOR=\"blue\">" + refno + "</FONT></td>"
						+ "<td width=\"40%\">"
						+ sb.formatHTML(gn.getDB2UserDate(deldate)) + "</td>"
						+ "<td width=\"20%\">" + sb.formatHTML(stat)
						+ "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String listPCKALLOC4GI(String aFROMDATE, String aTODATE,
			String aREFNO) throws Exception {
		String q = "";
		String link = "";
		String result = "", where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			if ((aFROMDATE.length() > 0) && (aTODATE.length() > 0)) {
				vecWhr.addElement(" USERTIME1 BETWEEN  '" + aFROMDATE
						+ "' AND '" + aTODATE + "'");
			}
			if ((aREFNO.length() > 0)) {
				vecWhr.addElement(" REFNO = '" + aREFNO + "'");
			}
			int cnt = vecWhr.size();
			if (cnt > 0) {
				where = "where status = 'S' AND ";
				for (int i = 0; i < cnt; i++) {
					where = where + vecWhr.elementAt(i).toString() + " ";
					if (i < cnt - 1) {
						where = where + " and ";
					}
				}
			}
			Statement stmt = con.createStatement();
			q = "select distinct refno ,status, usertime1 from pckalloc "
					+ where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String refno = rs.getString("REFNO").trim();
				String stat = rs.getString("STATUS").trim();
				String deldate = rs.getString("USERTIME1").trim();
				link = "<a href=\"" + "issueDet.jsp?ORDNO=" + refno + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"40%\">" + link
						+ "<FONT COLOR=\"blue\">" + refno + "</FONT></td>"
						+ "<td width=\"40%\">"
						+ sb.formatHTML(gn.getDB2UserDate(deldate)) + "</td>"
						+ "<td width=\"20%\">" + sb.formatHTML(stat)
						+ "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String listPdaPCKALLOC(String aFROMDATE, String aTODATE,
			String aREFNO) throws Exception {
		String q = "";
		String link = "";
		String result = "", where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			if ((aFROMDATE.length() > 0) && (aTODATE.length() > 0)) {
				vecWhr.addElement(" USERTIME1 BETWEEN  '" + aFROMDATE
						+ "' AND '" + aTODATE + "'");
			}
			if ((aREFNO.length() > 0)) {
				vecWhr.addElement(" REFNO = '" + aREFNO + "'");
			}
			int cnt = vecWhr.size();
			if (cnt > 0) {
				where = "where status = 'N' and ";
				for (int i = 0; i < cnt; i++) {
					where = where + vecWhr.elementAt(i).toString() + " ";
					if (i < cnt - 1) {
						where = where + " and ";
					}
				}
			}
			Statement stmt = con.createStatement();
			q = "select distinct refno ,status, usertime1 from pckalloc "
					+ where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String refno = rs.getString("REFNO").trim();
				String stat = rs.getString("STATUS").trim();
				String deldate = rs.getString("USERTIME1").trim();
				link = "<a href=\"" + "pdapickDet.jsp?ORDNO=" + refno + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"40%\">" + link
						+ "<FONT COLOR=\"blue\">" + refno + "</FONT></td>"
						+ "<td width=\"40%\">"
						+ sb.formatHTML(gn.getDB2UserDate(deldate)) + "</td>"
						+ "<td width=\"20%\">" + sb.formatHTML(stat)
						+ "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String listPicking4Alloc(String aREFNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select item,batch,bin,class,attrib,qty,pckqty,status from pckalloc where qty > 0 and REFNO = '"
					+ aREFNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String item = rs.getString("ITEM").trim();
				String batch = rs.getString("BATCH").trim();
				String bin = rs.getString("BIN").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				String qty = rs.getString("QTY").trim();
				String pckqty = rs.getString("PCKQTY").trim();
				float qtyBal = Float.parseFloat(qty);
				float qtyPck = Float.parseFloat(pckqty);
				ITEMMST itm = new ITEMMST();
				String desc = itm.getItemDesc(item);
				String stat = rs.getString("STATUS").trim();
				if (qtyPck > qtyBal) {
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"20%\">" + item
							+ "</td>" + "<td width=\"30%\">"
							+ sb.formatHTML(desc) + "</td>"
							+ "<td width=\"10%\">" + sb.formatHTML(clas)
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(attrib) + "</td>"
							+ "<td width=\"10%\">" + sb.formatHTML(batch)
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(bin) + "</td>"
							+ "<td width=\"10%\">" + sb.formatHTML(qty)
							+ "</td></tr>";
				} else {
					link = "<a href=\"" + "pickQty.jsp?ITEM=" + item
							+ "&BATCH=" + batch + "&BINNO=" + bin + "&CLASS="
							+ clas + "&ATTRIB=" + attrib + "&REFNO=" + aREFNO
							+ "&QTY=" + qty + "\">";
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"20%\">"
							+ link
							+ "<FONT COLOR=\"blue\">"
							+ item
							+ "</FONT></td>"
							+ "<td width=\"30%\">"
							+ sb.formatHTML(desc)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(clas)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(attrib)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(batch)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(bin)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(qty)
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

	public String listPCKRefNo(String aREFNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select distinct(refno) from pckalloc where qty > 0 and REFNO like '%%"
					+ aREFNO + "%%'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String refno = rs.getString("refno").trim();
				result += "<tr><td>" + refno + "</td></tr>";
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public boolean isAvaliable(String refno) throws Exception {
		boolean result = false;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) from PCKALLOC where PLANT= 'PLNT' and REFNO= '"
					+ refno + "'";
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

	public boolean isAvaliable(String refno, String item, String clas,
			String attrib, String brand, String binno) throws Exception {
		boolean result = false;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) from PCKALLOC where PLANT= 'PLNT' and REFNO= '"
					+ refno + "'";
			q = (item.equalsIgnoreCase("")) ? q : q + " AND ITEM = '" + item
					+ "'";
			q = (clas.equalsIgnoreCase("")) ? q : q + " AND CLASS = '" + clas
					+ "'";
			q = (attrib.equalsIgnoreCase("")) ? q : q + " AND ATTRIB = '"
					+ attrib + "'";
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

	/*
	 * to prepare the javascript with list ...
	 */
	public String prepareListScript(String aREFNO, String lineno,
			String itemno, String batchno, String binno) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		int count = 0;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select item,reflnno,batch,bin,class,attrib from pckalloc where (QTY-PCKQTY) > 0 and REFNO = '"
					+ aREFNO + "'";

			q = (lineno.equalsIgnoreCase("")) ? q : q + " AND reflnno = '"
					+ lineno + "'";
			q = (itemno.equalsIgnoreCase("")) ? q : q + " AND item = '"
					+ itemno + "'";
			q = (batchno.equalsIgnoreCase("")) ? q : q + " AND batch = '"
					+ batchno + "'";
			q = (binno.equalsIgnoreCase("")) ? q : q + " AND bin = '" + binno
					+ "'";

			q += " AND status IN ('N','S') ";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);

			while (rs.next()) {

				// var lineno= new Array();
				// var item = new Array();
				// var category = new Array();
				// var brand = new Array();
				// var bin = new Array();
				// var batch = new Array();

				String item = rs.getString("ITEM").trim();
				String lno = rs.getString("REFLNNO").trim();
				String batch = rs.getString("BATCH").trim();
				String bin = rs.getString("BIN").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				ITEMMST itm = new ITEMMST();
				String desc = itm.getItemDesc(item);
				result += "lineno[" + count + "] = " + lno + ";";
				result += "item[" + count + "] = '" + item + "';";
				result += "category[" + count + "] = '" + clas + "';";
				result += "brand[" + count + "] = '" + attrib + "';";
				result += "bin[" + count + "] = '" + bin + "';";
				result += "batch[" + count + "] = '" + batch + "';";
				System.out.print(result);
				count++;
			}
			result += "document.form.total.value=" + (count - 1) + ";";
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public boolean checkBatchStatus(String refno, String item, String batch)
			throws Exception {
		boolean status = false;
		String q = "";
		int count = 0;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select batch from pckalloc where qty > 0 and REFNO = '"
					+ refno + "' AND item = '" + item + "' AND batch = '"
					+ batch + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				count++;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		status = (count > 0) ? true : false;
		return status;
	}

	public boolean checkBinStatus(String refno, String item, String clas,
			String attrib, String bin) throws Exception {
		boolean status = false;
		String q = "";
		int count = 0;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select batch from pckalloc where qty > 0 AND REFNO = '"
					+ refno + "'";
			q += (item.equalsIgnoreCase("")) ? "" : "  AND ITEM = '" + item
					+ "' ";
			q += (clas.equalsIgnoreCase("")) ? "" : "  AND CLASS = '" + clas
					+ "'";
			q += (attrib.equalsIgnoreCase("")) ? "" : "  AND ATTRIB = '"
					+ attrib + "'";
			q += (bin.equalsIgnoreCase("")) ? "" : "  AND BIN = '" + bin + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				count++;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		status = (count > 0) ? true : false;
		return status;
	}

	public float getQty(String refno, String item, String clas, String attrib,
			String bin) throws Exception {
		String q = "";
		float qty = 0;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select (QTY-PCKQTY) as QTY1 from pckalloc where qty > 0 and REFNO = '"
					+ refno
					+ "' AND item = '"
					+ item
					+ "' AND CLASS = '"
					+ clas
					+ "' AND ATTRIB='"
					+ attrib
					+ "' AND BIN = '"
					+ bin
					+ "' ";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				qty = rs.getFloat("QTY1");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null)
				DbBean.closeConnection(con);
		}
		return qty;
	}

	public float getQty(String refno, String lno) throws Exception {
		String q = "";
		float qty = 0;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select (QTY-PCKQTY) as QTY1 from pckalloc where qty > 0 and REFNO = '"
					+ refno
					+ "' AND refno = '"
					+ refno
					+ "' AND REFLNNO = '"
					+ lno + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				qty = rs.getFloat("QTY1");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null)
				DbBean.closeConnection(con);
		}
		return qty;
	}

	public float getPckQty(String refno, String item, String clas,
			String attrib, String bin) throws Exception {
		String q = "";
		float qty = 0;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select PCKQTY from pckalloc where   REFNO = '" + refno
					+ "' AND item = '" + item + "' AND CLASS = '" + clas
					+ "' AND ATTRIB='" + attrib + "' AND BIN = '" + bin + "' ";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				qty = rs.getFloat("PCKQTY");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return qty;
	}

	public float getPckQty(String refno, String lno) throws Exception {
		String q = "";
		float qty = 0;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select PCKQTY from pckalloc where   REFNO = '" + refno
					+ "' AND refno = '" + refno + "' AND REFLNNO = '" + lno
					+ "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				qty = rs.getFloat("PCKQTY");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return qty;
	}

	public String getActiveOrders() throws Exception {
		String q = "";
		String result = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select distinct(refno) from pckalloc where status in('n','s') and refno <> '' and (qty-pckqty) >0 order by refno";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String pono = rs.getString("refno").trim();
				result += "<option value=" + pono + ">" + pono + "</option>";

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// //System.out.println(result);
		return result;
	}

	// listPicking
	/***
	 * XML FILES GENERATION FOR PDA FILES
	 */
	public String getPOItemsXML(String plant, String pono, int polnno) {

		String rowRec = "";
		ResultSet rs = null;
		PreparedStatement ps = null;
		XMLUtils xu = new XMLUtils();
		Connection conn = null;
		try {
			conn = DbBean.getConnection();
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		try {
			String q = "select pd.reflnno,pd.item,im.itemdesc,pd.qty,pd.pckqty,pd.batch,pd.bin,pd.uom,pd.status from PCKALLOC pd,itemmst im where pd.item = im.item and pd.PLANT = ? and pd.REFNO = ? and pd.REFLNNO = ? and pd.STATUS NOT IN(?)";
			this.mLogger.query(this.printQuery, q);
			ps = conn.prepareStatement(q);
			ps.setString(1, plant);
			ps.setString(2, pono);
			ps.setInt(3, polnno);
			ps.setString(4, "S");
			rs = ps.executeQuery();
			int i = 0;
			String lineRec = "";
			while (rs.next()) {
				i++;
				try {
					System.out.println("adding" + i);
					lineRec += xu.getStartNode("record"); // line no
					lineRec += xu.getXMLNode("lno", rs.getString(1)); // line no
					lineRec += xu.getXMLNode("icode", rs.getString(2)); // item
					// code
					lineRec += xu.getXMLNode("idesc", rs.getString(3)); // item
					// desc
					lineRec += xu.getXMLNode("qty_ord", rs.getString(4)); // qty
					// ordered
					lineRec += xu.getXMLNode("qty_recv", rs.getString(5)); // qty
					// picked
					lineRec += xu.getXMLNode("batch", rs.getString(6)); // batch
					lineRec += xu.getXMLNode("bin", rs.getString(7)); // bin
					lineRec += xu.getXMLNode("uom", rs.getString(8)); // uom
					lineRec += xu.getXMLNode("status", rs.getString(9)); // uom
					lineRec += xu.getEndNode("record");
					lineRec.trim();
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
				}
				rowRec = xu.getXMLHeader();
				rowRec += xu.getStartNode("items");
				// rowRec += xu.getXMLNode("total",String.valueOf(i));
				rowRec += lineRec;
				rowRec += xu.getEndNode("items");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			DbBean.closeConnection(conn, ps);
		}
		return rowRec.trim();
	}

	public boolean getPOStatus(String plant, String pono) {

		boolean isFound = false;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();
			// conn = cm.getConnection("access");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		try {
			ps = conn
					.prepareStatement("select * from PCKALLOC where PLANT=? and REFNO=?");
			ps.setString(1, plant);
			ps.setString(2, pono);
			rs = ps.executeQuery();
			if (rs.next()) {
				isFound = true;

			} else {

				isFound = false;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			DbBean.closeConnection(conn, ps);
			// cm.freeConnection("access",conn);
		}
		return isFound;
	}

	public String getPOItemNosXML(String plant, String pono) {

		String rowRec = "";
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = null;
		XMLUtils xu = new XMLUtils();
		try {
			conn = DbBean.getConnection();
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		try {
			ps = conn
					.prepareStatement("select reflnno from pckalloc where PLANT=? and REFNO=? AND STATUS NOT IN('S') ");
			ps.setString(1, plant);
			ps.setString(2, pono);
			rs = ps.executeQuery();
			int i = 0;
			String lineRec = "";
			while (rs.next()) {
				i++;
				try {
					lineRec += rs.getString(1) + ","; // line no
					lineRec.trim();
				} catch (Exception e) {
					System.out.println("error adding:" + e.getCause());
				}
				rowRec = xu.getXMLMessage(0, lineRec.substring(0, lineRec
						.lastIndexOf(",")));
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			DbBean.closeConnection(conn, ps);
		}
		return rowRec.trim();
	}

	public void processPicking(String plant, String pono, int poline,
			float qtyord, float qtybal, float qtyrecv) throws Exception {

		PreparedStatement ps = null;
		Connection conn = null;
		String status = "";

		status = (qtyrecv == qtybal) ? "S" : "N";

		try {
			conn = DbBean.getConnection();
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Error");
		}
		try {
			ps = conn
					.prepareStatement("update PCKALLOC SET PCKQTY=PCKQTY+?,STATUS=? where PLANT=? and REFNO=? and REFLNNO =?");
			ps.setFloat(1, qtyrecv);
			ps.setString(2, status);
			ps.setString(3, plant);
			ps.setString(4, pono);
			ps.setInt(5, poline);
			ps.execute();
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Error");
		}

		finally {
			try {
				DbBean.closeConnection(conn, ps);
			} catch (Exception e) {
			}
		}
	}

	public boolean insertIntoPCKALLOC(Hashtable ht) throws Exception {
		boolean insertRecvHis = false;
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _strUtils.fString((String) enum1.nextElement());
				String value = _strUtils.fString((String) ht.get(key));
				FIELDS += key.toUpperCase() + ",";
				VALUES += "'" + value.toUpperCase() + "',";
			}
			String stmt = "INSERT INTO PCKALLOC ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			// System.out.println(stmt);
			ps = conn.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertRecvHis = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(conn, ps);
		}
		return insertRecvHis;
	}

	public ArrayList getPCKList(String aDono, String aCustid, String aModel,
			String aManNo, String aSerialNo) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		String sCondition = "";
		String sQryFields = "";
		ArrayList listQryResult = new ArrayList();
		ArrayList fieldsList = new ArrayList();

		try {
			Hashtable ht = new Hashtable();
			if (_strUtils.fString(aDono).length() > 0)
				ht.put("REFNO", aDono);
			if (_strUtils.fString(aCustid).length() > 0)
				ht.put("USRKEY1", aCustid);
			if (_strUtils.fString(aModel).length() > 0)
				ht.put("USRKEY2", aModel);
			if (_strUtils.fString(aManNo).length() > 0)
				ht.put("USRKEY3", aManNo);
			if (_strUtils.fString(aSerialNo).length() > 0)
				ht.put("USRKEY4", aSerialNo);

			fieldsList.add("REFNO");
			fieldsList.add("USRKEY1");
			fieldsList.add("USRKEY2");
			fieldsList.add("USRKEY3");
			fieldsList.add("USRKEY4");

			con = DbBean.getConnection();

			// generate the condition string
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _strUtils.fString((String) enum1.nextElement());
				String value = _strUtils.fString((String) ht.get(key));
				sCondition = sCondition + key + " = '" + value + "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";

			// get qry fields
			for (int i = 0; i < fieldsList.size(); i++) {
				sQryFields = sQryFields
						+ _strUtils.fString((String) fieldsList.get(i)) + ",";
			}
			sQryFields = (sQryFields.length() > 0) ? sQryFields.substring(0,
					sQryFields.length() - 1) : "";
			String stmt = "SELECT " + sQryFields + " FROM PCKALLOC WHERE "
					+ sCondition;
			ps = con.prepareStatement(stmt);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, _strUtils.fString(rs.getString("REFNO")));
				arrLine.add(1, _strUtils.fString(rs.getString("USRKEY1")));
				arrLine.add(2, _strUtils.fString(rs.getString("USRKEY2")));
				arrLine.add(3, _strUtils.fString(rs.getString("USRKEY3")));
				arrLine.add(4, _strUtils.fString(rs.getString("USRKEY4")));
				listQryResult.add(arrLine);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQryResult;
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
}