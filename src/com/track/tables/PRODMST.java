package com.track.tables;

// importing classes

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Vector;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.sqlBean;
import com.track.util.MLogger;


public class PRODMST {
	private boolean printQuery = MLoggerConstant.PRODMST_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.PRODMST_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	String sPLANT = "SIS";
	String sORDNO = "";
	String sORDTYPE = "";
	String sLANGID = "";
	String sREFORDNO = "";
	String sITEM = "";
	String sCLASS = "";
	String sATTRIB = "";
	float fQTY;
	String sUOM = "";
	String sPRDSCHDT = "";
	String sACTSTHDT = "";
	String sPRPSCHDT = "";
	String sRELFLAG = "";
	String sSTATUS = "";
	String sPROCLINE = "";
	String sBATCH = "";
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

	public String getORDNO() {
		return sORDNO;
	}

	public String getORDTYPE() {
		return sORDTYPE;
	}

	public String getLANGID() {
		return sLANGID;
	}

	public String getREFORDNO() {
		return sREFORDNO;
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

	public String getUOM() {
		return sUOM;
	}

	public String getPRDSCHDT() {
		return sPRDSCHDT;
	}

	public String getACTSTHDT() {
		return sACTSTHDT;
	}

	public String getPRPSCHDT() {
		return sPRPSCHDT;
	}

	public String getRELFLAG() {
		return sRELFLAG;
	}

	public String getSTATUS() {
		return sSTATUS;
	}

	public String getPROCLINE() {
		return sPROCLINE;
	}

	public String getBATCH() {
		return sBATCH;
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

	public void setORDNO(String aORDNO) {
		sORDNO = aORDNO;
	}

	public void setORDTYPE(String aORDTYPE) {
		sORDTYPE = aORDTYPE;
	}

	public void setLANGID(String aLANGID) {
		sLANGID = aLANGID;
	}

	public void setREFORDNO(String aREFORDNO) {
		sREFORDNO = aREFORDNO;
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

	public void setUOM(String aUOM) {
		sUOM = aUOM;
	}

	public void setPRDSCHDT(String aPRDSCHDT) {
		sPRDSCHDT = aPRDSCHDT;
	}

	public void setACTSTHDT(String aACTSTHDT) {
		sACTSTHDT = aACTSTHDT;
	}

	public void setPRPSCHDT(String aPRPSCHDT) {
		sPRPSCHDT = aPRPSCHDT;
	}

	public void setRELFLAG(String aRELFLAG) {
		sRELFLAG = aRELFLAG;
	}

	public void setSTATUS(String aSTATUS) {
		sSTATUS = aSTATUS;
	}

	public void setPROCLINE(String aPROCLINE) {
		sPROCLINE = aPROCLINE;
	}

	public void setBATCH(String aBATCH) {
		sBATCH = aBATCH;
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
	 * constructor method PRODMST sb - SQL Bean gn - Utilites
	 */
	public PRODMST() throws Exception {
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
	 * Insert Command - PRODMST
	 * 
	 * 
	 */
	public int insertPRODMST() throws Exception {
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
			sql = "Insert into PRODMST values ('" + sPLANT + "','" + sORDNO
					+ "','" + sORDTYPE + "','" + sLANGID + "','" + sREFORDNO
					+ "','" + sITEM + "','" + sCLASS + "','" + sATTRIB + "',"
					+ fQTY + ",'" + sUOM + "','" + sPRDSCHDT + "','"
					+ sACTSTHDT + "','" + sPRPSCHDT + "','" + sRELFLAG + "','"
					+ sSTATUS + "','" + sPROCLINE + "','" + sBATCH + "','"
					+ sCRAT + "','" + sCRBY + "','" + sUPAT + "','" + sUPBY
					+ "','" + sRECSTAT + "','" + sUSERFLD1 + "','" + sUSERFLD2
					+ "','" + sUSERFLD3 + "','" + sUSERFLD4 + "','" + sUSERFLD5
					+ "','" + sUSERFLD6 + "','" + sUSERFLG1 + "','" + sUSERFLG2
					+ "','" + sUSERFLG3 + "','" + sUSERFLG4 + "','" + sUSERFLG5
					+ "','" + sUSERFLG6 + "','" + sUSERTIME1 + "','"
					+ sUSERTIME2 + "','" + sUSERTIME3 + "'," + fUSERDBL1 + ","
					+ fUSERDBL2 + "," + fUSERDBL3 + "," + fUSERDBL4 + ","
					+ fUSERDBL5 + "," + fUSERDBL6 + ")";
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
		String sql = "";
		try {
			sql = "Insert into PRODMST values ('" + sPLANT + "','" + sORDNO
					+ "','" + sORDTYPE + "','" + sLANGID + "','" + sREFORDNO
					+ "','" + sITEM + "','" + sCLASS + "','" + sATTRIB + "',"
					+ fQTY + ",'" + sUOM + "','" + sPRDSCHDT + "','"
					+ sACTSTHDT + "','" + sPRPSCHDT + "','" + sRELFLAG + "','"
					+ sSTATUS + "','" + sPROCLINE + "','" + sBATCH + "','"
					+ sCRAT + "','" + sCRBY + "','" + sUPAT + "','" + sUPBY
					+ "','" + sRECSTAT + "','" + sUSERFLD1 + "','" + sUSERFLD2
					+ "','" + sUSERFLD3 + "','" + sUSERFLD4 + "','" + sUSERFLD5
					+ "','" + sUSERFLD6 + "','" + sUSERFLG1 + "','" + sUSERFLG2
					+ "','" + sUSERFLG3 + "','" + sUSERFLG4 + "','" + sUSERFLG5
					+ "','" + sUSERFLG6 + "','" + sUSERTIME1 + "','"
					+ sUSERTIME2 + "','" + sUSERTIME3 + "'," + fUSERDBL1 + ","
					+ fUSERDBL2 + "," + fUSERDBL3 + "," + fUSERDBL4 + ","
					+ fUSERDBL5 + "," + fUSERDBL6 + ")";
		} catch (Exception e) {
			DbBean.writeError("PRODMST", "insertPRODMST()", e);
		}
		return sql;
	}

	/**
	 * Update Function - PRODMST
	 * 
	 * 
	 */
	public int updatePRODMST() throws Exception {
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
			sql = "Update PRODMST set ORDTYPE= '" + sORDTYPE + "',LANGID= '"
					+ sLANGID + "',REFORDNO= '" + sREFORDNO + "',ITEM= '"
					+ sITEM + "',CLASS='" + sCLASS + "',ATTRIB= '" + sATTRIB
					+ "',QTY = " + fQTY + ",UOM= '" + sUOM + "',PRDSCHDT= '"
					+ sPRDSCHDT + "',ACTSTHDT= '" + sACTSTHDT + "',PRPSCHDT= '"
					+ sPRPSCHDT + "',RELFLAG= '" + sRELFLAG + "',STATUS= '"
					+ sSTATUS + "',PROCLINE= '" + sPROCLINE + "',BATCH= '"
					+ sBATCH + "',CRAT= '" + sCRAT + "',CRBY= '" + sCRBY
					+ "',UPAT= '" + sUPAT + "',UPBY= '" + sUPBY
					+ "',RECSTAT= '" + sRECSTAT + "',USERFLD1= '" + sUSERFLD1
					+ "',USERFLD2= '" + sUSERFLD2 + "',USERFLD3= '" + sUSERFLD3
					+ "',USERFLD4= '" + sUSERFLD4 + "',USERFLD5= '" + sUSERFLD5
					+ "',USERFLD6= '" + sUSERFLD6 + "',USERFLG1= '" + sUSERFLG1
					+ "',USERFLG2= '" + sUSERFLG2 + "',USERFLG3= '" + sUSERFLG3
					+ "',USERFLG4= '" + sUSERFLG4 + "',USERFLG5= '" + sUSERFLG5
					+ "',USERFLG6= '" + sUSERFLG6 + "',USERTIME1= '"
					+ sUSERTIME1 + "',USERTIME2= '" + sUSERTIME2
					+ "',USERTIME3= '" + sUSERTIME3 + "',USERDBL1 = "
					+ fUSERDBL1 + ",USERDBL2 = " + fUSERDBL2 + ",USERDBL3 = "
					+ fUSERDBL3 + ",USERDBL4 = " + fUSERDBL4 + ",USERDBL5 = "
					+ fUSERDBL5 + ",USERDBL6 = " + fUSERDBL6
					+ " where PLANT= '" + sPLANT + "' and ORDNO= '" + sORDNO
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

	/**
	 * Select FunctionPRODMST
	 * 
	 * 
	 */
	public void selectPRODMST() throws Exception {
		String q = "";
		Connection con = getDBConnection();
		ResultSet rs = null;
		try {
			Statement stmt = con.createStatement();
			q = "select * from PRODMST where PLANT= '" + sPLANT
					+ "' and ORDNO= '" + sORDNO + "'";
			this.mLogger.query(this.printQuery, q);
			rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sORDTYPE = rs.getString("ORDTYPE");
				sLANGID = rs.getString("LANGID");
				sREFORDNO = rs.getString("REFORDNO");
				sITEM = rs.getString("ITEM");
				sCLASS = rs.getString("CLASS");
				sATTRIB = rs.getString("ATTRIB");
				fQTY = rs.getFloat("QTY");
				sUOM = rs.getString("UOM");
				sPRDSCHDT = rs.getString("PRDSCHDT");
				sACTSTHDT = rs.getString("ACTSTHDT");
				sPRPSCHDT = rs.getString("PRPSCHDT");
				sRELFLAG = rs.getString("RELFLAG");
				sSTATUS = rs.getString("STATUS");
				sPROCLINE = rs.getString("PROCLINE");
				sBATCH = rs.getString("BATCH");
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
			rs = null;
			DbBean.closeConnection(con);
		}
	}

	/**
	 * Delete Function -- PRODMST
	 * 
	 * 
	 */
	public int deletePRODMST() throws Exception {
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
			sql = "delete from PRODMST where PLANT= '" + sPLANT
					+ "' and ORDNO= '" + sORDNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			rs.close();
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String getDeleteSQL() throws Exception {
		String sql = "";
		try {
			sql = "delete from PRODMST where PLANT= '" + sPLANT
					+ "' and ORDNO= '" + sORDNO + "'";
		} catch (Exception e) {
			DbBean.writeError("PRODMST", "deletePRODMST()", e);
		}
		return sql;
	}

	public String listPRODMST(String aPRNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Connection con = getDBConnection();
		ResultSet rs = null;
		try {
			Statement stmt = con.createStatement();
			q = "select ordno,ordtype,refordno,prdschdt,actsthdt from prodmst where ordno like'"
					+ aPRNO + "%'";
			this.mLogger.query(this.printQuery, q);
			rs = stmt.executeQuery(q);
			while (rs.next()) {
				String ordno = rs.getString("ORDNO").trim();
				link = "<a href=\"" + "recvPrQty.jsp?ORDNO=" + ordno + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"25%\">" + link
						+ "<FONT COLOR=\"blue\">" + ordno + "</font></a>"
						+ "</td>" + "<td width=\"25%\">"
						+ rs.getString("ORDTYPE").trim() + "</td>"
						+ "<td width=\"25%\">"
						+ gn.getDB2UserDate(rs.getString("PRDSCHDT").trim())
						+ "</td>" + "<td width=\"25%\">"
						+ gn.getDB2UserDate(rs.getString("ACTSTHDT").trim())
						+ "</td></tr>";

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			rs.close();
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String listPRODMST() throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Connection con = getDBConnection();
		ResultSet rs = null;
		try {
			Statement stmt = con.createStatement();
			q = "select ordno,ordtype,refordno,prdschdt,actsthdt from prodmst ";
			this.mLogger.query(this.printQuery, q);
			rs = stmt.executeQuery(q);
			while (rs.next()) {
				String ordno = rs.getString("ORDNO").trim();
				link = "<a href='#' onClick='window.opener.ORDNO.value='"
						+ ordno + "';window.close();>";

				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"25%\">" + link
						+ "<FONT COLOR=\"blue\">" + ordno + "</font></a>"
						+ "</td>" + "<td width=\"25%\">"
						+ rs.getString("ORDTYPE").trim() + "</td>"
						+ "<td width=\"25%\">"
						+ gn.getDB2UserDate(rs.getString("PRDSCHDT").trim())
						+ "</td>" + "<td width=\"25%\">"
						+ gn.getDB2UserDate(rs.getString("ACTSTHDT").trim())
						+ "</td></tr>";

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			rs.close();
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String listPRODMST1() throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Connection con = getDBConnection();
		ResultSet rs = null;
		try {
			Statement stmt = con.createStatement();
			q = "select ordno,ordtype,refordno,prdschdt,actsthdt from prodmst ";
			this.mLogger.query(this.printQuery, q);
			rs = stmt.executeQuery(q);
			while (rs.next()) {
				String ordno = rs.getString("ORDNO").trim();
				link = "<a href='#' onClick=\"window.opener.form.ORDNO.value='"
						+ ordno + "';window.close();\">";

				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"25%\">" + link
						+ "<FONT COLOR=\"blue\">" + ordno + "</font></a>"
						+ "</td><td>"
						+ gn.getDB2UserDate(rs.getString("ACTSTHDT").trim())
						+ "</td></tr>";

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			rs.close();
			DbBean.closeConnection(con);
		}
		return result;
	}

	public int countPRODMST(String aORDNO) throws Exception {
		int res = 0;
		String q = "";
		Connection con = getDBConnection();
		ResultSet rs = null;
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) from PRODMST where PLANT= 'PLNT' and ORDNO= '"
					+ aORDNO + "'";
			this.mLogger.query(this.printQuery, q);
			rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				res = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			rs.close();
			DbBean.closeConnection(con);
		}
		return res;
	}

	public boolean isAvailable(String aORDNO) throws Exception {
		boolean res = false;
		int count = 0;
		String q = "";
		Connection con = getDBConnection();
		ResultSet rs = null;
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) from PRODMST where PLANT= 'PLNT'  and ORDNO= '"
					+ aORDNO + "'";
			this.mLogger.query(this.printQuery, q);
			rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				count = rs.getInt(1);
				if (count > 0) {
					res = true;
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			rs.close();
			DbBean.closeConnection(con);
		}
		return res;
	}

	public String getNextRecID(String aType) throws Exception {
		String recId = "", res = "";
		String q = "";
		Connection con = getDBConnection();
		ResultSet rs = null;
		try {
			Statement stmt = con.createStatement();
			q = "select max(ordno) from prodmst where ordno like '" + aType
					+ "%'";
			this.mLogger.query(this.printQuery, q);
			rs = stmt.executeQuery(q);
			int n = 0;
			while (rs.next()) {
				recId = rs.getString(1);
				if (recId != null) {
					recId = recId.replace(aType.charAt(0), '0');
					n = Integer.parseInt(recId) + 1;
					NumberFormat formatter = new DecimalFormat("0000");
					res = aType + formatter.format(n);
				} else {
					res = aType + "0001";
				}
			}
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
		} finally {
			rs.close();
			DbBean.closeConnection(con);
		}
		return res;
	}

	public int processPR(String aPRNO) throws Exception {
		String sql1, sql2, q, refno;
		String updateSQL, insertSQL, updInventorySQL;
		Vector vec = new Vector();
		refno = aPRNO;
		ArrayList al = new ArrayList();
		Connection con = getDBConnection();
		int returnCode = 1;
		int ret1 = 1;
		int ret2 = 1;
		ResultSet rs = null;
		BATMST btb = new BATMST();
		PCKALLOC pcalloc = new PCKALLOC();
		INVMST im = new INVMST();
		try {
			Statement stmt = con.createStatement();
			q = "select item,class,attrib,qty,uom,ordtype,batch,reflnno,userflg1 from PCKDET where refno ='"
					+ refno + "'";
			int cnt = 1;
			this.mLogger.query(this.printQuery, q);
			rs = stmt.executeQuery(q);
			while (rs.next()) {
				String item = rs.getString("ITEM").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				float qty = rs.getFloat("qty");
				String uom = rs.getString("uom").trim();
				String ordtype = rs.getString("ordtype");
				String batch = rs.getString("batch");
				String reflnno = rs.getString("reflnno");
				System.out.println("LIne %%% " + reflnno);
				vec = im.getAllocatedItem(item, clas, attrib, qty);
				for (int i = 0; i < vec.size(); i++) {
					Vector tmpVec = new Vector();
					tmpVec = (Vector) vec.elementAt(i);
					pcalloc.setREFNO(refno);
					pcalloc.setREFLNNO(String.valueOf(cnt));
					pcalloc.setORDTYPE(ordtype);
					pcalloc.setITEM(tmpVec.elementAt(0).toString());
					pcalloc.setCLASS(clas);
					pcalloc.setATTRIB(attrib);
					pcalloc.setBATCH(tmpVec.elementAt(1).toString());
					pcalloc.setBIN(tmpVec.elementAt(2).toString());
					pcalloc.setQTY(Float.parseFloat(tmpVec.elementAt(3)
							.toString()));
					pcalloc.setSTATUS("N");
					pcalloc.setUOM(uom);
					al.add(pcalloc.getInsertSQL());
					System.out.println("batch "
							+ tmpVec.elementAt(1).toString() + "\nitem : "
							+ tmpVec.elementAt(0).toString());
					btb.setBATCH(tmpVec.elementAt(1).toString());
					btb.setITEM(tmpVec.elementAt(0).toString());
					btb.setSLED(sPRDSCHDT);

					if (!(btb.isAvaliable(tmpVec.elementAt(1).toString(),
							tmpVec.elementAt(0).toString()))) {
						// System.out.println("@@@@@@@@@@@@@@i am inside btb available@@@@@@@@@@@@@");
						System.out.println("BATCH : " + btb.getBATCH());
						al.add(btb.getInsertSql());
					}

					cnt++;
				}
				// System.out.println("^^^^ inside while ^^^^");
				al.add(im.updateQtySQL(item, clas, attrib, qty));

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		try {
			// sql1 =
			// "INSERT PRODMST SELECT * FROM PRODMST  WHERE ORDNO = '"+aPRNO+"'";
			// sql2 =
			// "INSERT PCKDET  SELECT * FROM PCKDET  WHERE REFNO = '"+aPRNO+"'";

			// update production master table
			updateSQL = "UPDATE PRODMST SET STATUS = 'S' WHERE ORDNO = '"
					+ aPRNO + "'";
			al.add(updateSQL);

			// update inventory master table (INVMST)

			if (!(btb.isAvaliable(sBATCH, sITEM))) {
				btb.setBATCH(sBATCH);
				btb.setITEM(sITEM);
				btb.setSLED(sPRDSCHDT);
				// al.add(btb.getInsertSql());
			}
			// ret1 = sb.insertRecords(sql1);
			// ret2 = sb.insertRecords(sql2);
			sqlBean sb = new sqlBean();
			if (sb.insertBatchRecords(al)) {
				returnCode = 1;
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			rs.close();
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