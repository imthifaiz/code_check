package com.track.tables;

// importing classes

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.sqlBean;
import com.track.util.MLogger;

public class PCKDET {
	private boolean printQuery = MLoggerConstant.PCKDET_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.PCKDET_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	String sPLANT = "SIS";
	String sPCKNO = "";
	String sREFNO = "";
	String sREFLNNO = "";
	String sORDTYPE = "";
	String sITEM = "";
	String sCLASS = "";
	String sATTRIB = "";
	float fQTY;
	String sUOM = "";
	String sBATCH = "";
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
	boolean allowSave = true;

	public String getPLANT() {
		return sPLANT;
	}

	public String getPCKNO() {
		return sPCKNO;
	}

	public String getREFNO() {
		return sREFNO;
	}

	public String getREFLNNO() {
		return sREFLNNO;
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

	public String getUOM() {
		return sUOM;
	}

	public String getBATCH() {
		return sBATCH;
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

	public void setPCKNO(String aPCKNO) {
		sPCKNO = aPCKNO;
	}

	public void setREFNO(String aREFNO) {
		sREFNO = aREFNO;
	}

	public void setREFLNNO(String aREFLNNO) {
		sREFLNNO = aREFLNNO;
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

	public void setUOM(String aUOM) {
		sUOM = aUOM;
	}

	public void setBATCH(String aBATCH) {
		sBATCH = aBATCH;
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

	public boolean getAllowSave() {
		return allowSave;
	}

	public void setAllowSave(boolean allowSave) {
		allowSave = allowSave;
	}

	/**
	 * constructor method PCKDET$ sb - SQL Bean gn - Utilites
	 */
	public PCKDET() throws Exception {
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
	 * Insert Command - PCKDET$
	 * 
	 * 
	 */
	public int insertPCKDET() throws Exception {
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
			sql = "Insert into PCKDET values ('" + sPLANT + "','" + sPCKNO
					+ "','" + sREFNO + "','" + sREFLNNO + "','" + sORDTYPE
					+ "','" + sITEM + "','" + sCLASS + "','" + sATTRIB + "',"
					+ fQTY + ",'" + sUOM + "','" + sBATCH + "','" + sPCKBIN
					+ "','" + sSTATUS + "','" + sDRPLOC + "','" + sLPLTNO
					+ "','" + sCRAT + "','" + sCRBY + "','" + sUPAT + "','"
					+ sUPBY + "','" + sRECSTAT + "','" + sUSERFLD1 + "','"
					+ sUSERFLD2 + "','" + sUSERFLD3 + "','" + sUSERFLD4 + "','"
					+ sUSERFLD5 + "','" + sUSERFLD6 + "','" + sUSERFLG1 + "','"
					+ sUSERFLG2 + "','" + sUSERFLG3 + "','" + sUSERFLG4 + "','"
					+ sUSERFLG5 + "','" + sUSERFLG6 + "','" + sUSERTIME1
					+ "','" + sUSERTIME2 + "','" + sUSERTIME3 + "',"
					+ fUSERDBL1 + "," + fUSERDBL2 + "," + fUSERDBL3 + ","
					+ fUSERDBL4 + "," + fUSERDBL5 + "," + fUSERDBL6 + ")";
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
			sql = "Insert into PCKDET values ('" + sPLANT + "','" + sPCKNO
					+ "','" + sREFNO + "','" + sREFLNNO + "','" + sORDTYPE
					+ "','" + sITEM + "','" + sCLASS + "','" + sATTRIB + "',"
					+ fQTY + ",'" + sUOM + "','" + sBATCH + "','" + sPCKBIN
					+ "','" + sSTATUS + "','" + sDRPLOC + "','" + sLPLTNO
					+ "','" + sCRAT + "','" + sCRBY + "','" + sUPAT + "','"
					+ sUPBY + "','" + sRECSTAT + "','" + sUSERFLD1 + "','"
					+ sUSERFLD2 + "','" + sUSERFLD3 + "','" + sUSERFLD4 + "','"
					+ sUSERFLD5 + "','" + sUSERFLD6 + "','" + sUSERFLG1 + "','"
					+ sUSERFLG2 + "','" + sUSERFLG3 + "','" + sUSERFLG4 + "','"
					+ sUSERFLG5 + "','" + sUSERFLG6 + "','" + sUSERTIME1
					+ "','" + sUSERTIME2 + "','" + sUSERTIME3 + "',"
					+ fUSERDBL1 + "," + fUSERDBL2 + "," + fUSERDBL3 + ","
					+ fUSERDBL4 + "," + fUSERDBL5 + "," + fUSERDBL6 + ")";
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sql;
	}

	/**
	 * Update Function - PCKDET$
	 * 
	 * 
	 */
	public int updatePCKDET() throws Exception {
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
			sql = "Update PCKDET set ORDTYPE= '" + sORDTYPE + "',ITEM= '"
					+ sITEM + "',CLASS= '" + sCLASS + "',ATTRIB= '" + sATTRIB
					+ "',QTY = " + fQTY + ",UOM= '" + sUOM + "',BATCH= '"
					+ sBATCH + "',PCKBIN= '" + sPCKBIN + "',STATUS= '"
					+ sSTATUS + "',DRPLOC= '" + sDRPLOC + "',LPLTNO= '"
					+ sLPLTNO + "',CRAT= '" + sCRAT + "',CRBY= '" + sCRBY
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
					+ " where PLANT= '" + sPLANT + "' and PCKNO= '" + sPCKNO
					+ "' and REFNO= '" + sREFNO + "' and REFLNNO= '" + sREFLNNO
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

	public int updateQty(String aREFNO, String aREFLNNO, float fQTY)
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
			sql = "Update PCKDET set QTY = " + fQTY + " where  REFNO= '"
					+ aREFNO + "' and REFLNNO= '" + aREFLNNO + "'";
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
	 * Select FunctionPCKDET
	 * 
	 * 
	 */
	public void selectPCKDET() throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from PCKDET where PLANT= '" + sPLANT
					+ "' and PCKNO= '" + sPCKNO + "' and REFNO= '" + sREFNO
					+ "' and REFLNNO= '" + sREFLNNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sORDTYPE = rs.getString("ORDTYPE");
				sITEM = rs.getString("ITEM");
				fQTY = rs.getFloat("QTY");
				sUOM = rs.getString("UOM");
				sBATCH = rs.getString("BATCH");
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
	 * Delete Function -- PCKDET
	 * 
	 * 
	 */
	public int deletePCKDET() throws Exception {
		String sql;
		Connection con = getDBConnection();
		ResultSet rs = null;
		int returnCode = 1;
		// if (con == null) {
		// returnCode = 100; // Could not get Database Connection..Please check
		// the DSN, UserName and Password in the db_param file
		// }
		try {
			sql = "delete from PCKDET where PLANT= '" + sPLANT
					+ "' and PCKNO= '" + sPCKNO + "' and REFNO= '" + sREFNO
					+ "' and REFLNNO= '" + sREFLNNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			rs = null;
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String getDeleteSQL(String refno) throws Exception {
		String sql;
		sql = "delete from PCKDET where PLANT= '" + sPLANT + "'  and REFNO= '"
				+ refno + "'";
		return sql;
	}

	// String aITEM,String aQTY, String aUOM
	public String createWO(float prdQty, String deldate) throws Exception {
		String q = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		String link = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select clditem,cldclass,cldattrib,totqty,userfld1,userflg1 from bommst where paritem = '"
					+ sITEM
					+ "' and CLASS= '"
					+ sCLASS
					+ "' and ATTRIB= '"
					+ sATTRIB + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int cnt = 1;
			while (rs.next()) {
				String item = rs.getString("CLDITEM").trim();
				ITEMMST im = new ITEMMST();
				INVMST in = new INVMST();
				String desc = im.getItemDesc(item);
				String uom = im.getBaseUom(item);
				String cldclass = rs.getString("CLDCLASS").trim();
				String cldattrib = rs.getString("CLDATTRIB").trim();
				String cldqty = rs.getString("TOTQTY").trim();
				String parqty = rs.getString("USERFLD1").trim();
				String flag = rs.getString("USERFLG1").trim();
				fQTY = Float.parseFloat(cldqty) / Float.parseFloat(parqty)
						* prdQty;
				sITEM = item;
				sUOM = uom;
				sCLASS = cldclass;
				sATTRIB = cldattrib;
				sUSERFLG1 = flag;
				sREFLNNO = cnt + "";
				sUSERTIME1 = deldate;
				insertPCKDET();
				link = "<a href=\"" + "prAddBomItem.jsp?REFNO=" + sREFNO
						+ "&REFLNNO=" + cnt + "\">";
				if (flag.equalsIgnoreCase("N")) {
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"20%\">"
							+ link
							+ "<FONT COLOR=\"blue\">"
							+ cnt
							+ "</FONT></td>"
							+ "<td align=\"center\" width=\"20%\">"
							+ item
							+ "</td>"
							+ "<td width=\"30%\">"
							+ sb.formatHTML(desc)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(fQTY + "")
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(uom)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(in.getAvailQty(item, cldclass,
									cldattrib)
									+ "") + "</td></tr>";
				} else {
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"20%\">"
							+ cnt
							+ "</td>"
							+ "<td align=\"center\" width=\"20%\">"
							+ item
							+ "</td>"
							+ "<td width=\"30%\">"
							+ sb.formatHTML(desc)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(fQTY + "")
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(uom)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(in.getAvailQty(item, cldclass,
									cldattrib)
									+ "") + "</td></tr>";
				}

				cnt = cnt + 1;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
		return result;
	}

	public ArrayList getWorkOrderSQL(float prdQty, String deldate)
			throws Exception {
		String q = "";
		ArrayList result = new ArrayList();
		String where = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select clditem,cldclass,cldattrib,totqty,userfld1,userflg1 from bommst "
					+ " where paritem = '"
					+ sITEM
					+ "' and CLASS= '"
					+ sCLASS
					+ "' and ATTRIB= '" + sATTRIB + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int cnt = 1;
			while (rs.next()) {
				String item = rs.getString("CLDITEM").trim();
				ITEMMST im = new ITEMMST();
				String desc = im.getItemDesc(item);
				String uom = im.getBaseUom(item);
				String cldclass = rs.getString("CLDCLASS").trim();
				String cldattrib = rs.getString("CLDATTRIB").trim();
				String cldqty = rs.getString("TOTQTY").trim();
				String parqty = rs.getString("USERFLD1").trim();
				String flag = rs.getString("USERFLG1").trim();
				fQTY = Float.parseFloat(cldqty) / Float.parseFloat(parqty)
						* prdQty;
				sITEM = item;
				sUOM = uom;
				sCLASS = cldclass;
				sATTRIB = cldattrib;
				sUSERFLG1 = flag;
				sREFLNNO = cnt + "";
				sUSERTIME1 = deldate;
				result.add(getInsertSQL());
				cnt = cnt + 1;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
		return result;
	}

	public String listPCKDET(String sREFNO) throws Exception {
		String q = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		String link = "";
		String bcolor = "";
		Connection con = getDBConnection();
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			q = "select item,class,attrib,qty,uom,userflg1,reflnno from PCKDET where refno ='"
					+ sREFNO + "'";
			this.mLogger.query(this.printQuery, q);
			rs = stmt.executeQuery(q);
			int cnt = 1;
			while (rs.next()) {
				String item = rs.getString("ITEM").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				ITEMMST im = new ITEMMST();
				String desc = im.getItemDesc(item);
				INVMST inv = new INVMST();
				String qty = rs.getString("QTY").trim();

				bcolor = "";
				float curQty = 0;
				float qtyAvail = inv.getAvailQty(item, clas, attrib);
				try {
					curQty = (float) Integer.parseInt(qty.substring(0, qty
							.indexOf(".")));
					if (qtyAvail < curQty) {
						allowSave = false;
						bcolor = "bgcolor=red";
					}
				} catch (Exception e) {
				}
				String uom = rs.getString("UOM").trim();
				String flg = rs.getString("USERFLG1").trim();
				String lno = rs.getString("reflnno").trim();
				link = "<a href=\"" + "prModiBomItem.jsp?REFNO=" + sREFNO
						+ "&REFLNNO=" + lno + "\">";
				if (flg.equalsIgnoreCase("Y")) {
					result += "<tr valign=\"middle\" " + bcolor + ">"
							+ "<td align=\"center\" width=\"5%\">" + link
							+ "<FONT COLOR=\"blue\">" + cnt + "</FONT></td>"
							+ "<td  width=\"15%\">" + item + "</td>"
							+ "<td width=\"30%\">" + sb.formatHTML(desc)
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(clas) + "</td>"
							+ "<td width=\"10%\">" + sb.formatHTML(attrib)
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(qty) + "</td>"
							+ "<td width=\"10%\" align=center>"
							+ sb.formatHTML(uom) + "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(qtyAvail + "") + "</td></tr>";
				} else {
					result += "<tr valign=\"middle\" " + bcolor + ">"
							+ "<td align=\"center\" width=\"5%\">" + cnt
							+ "</td>" + "<td  width=\"15%\">" + item + "</td>"
							+ "<td width=\"30%\">" + sb.formatHTML(desc)
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(clas) + "</td>"
							+ "<td width=\"10%\">" + sb.formatHTML(attrib)
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(qty) + "</td>"
							+ "<td width=\"10%\" align=center>"
							+ sb.formatHTML(uom) + "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(qtyAvail + "") + "</td></tr>";

				}

				cnt = cnt + 1;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			stmt = null;
			rs = null;
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
		return result;
	}

	public String listGoodsIssue(String sREFNO) throws Exception {
		String q = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		String link = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select item,class,attrib,qty,usertime1 from PCKDET where refno ='"
					+ sREFNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int cnt = 1;
			while (rs.next()) {
				Vector issVec = new Vector();
				INVMST inv = new INVMST();
				ITEMMST im = new ITEMMST();
				BOMMST bm = new BOMMST();
				PRODMST pd = new PRODMST();
				pd.setORDNO(sREFNO);
				pd.selectPRODMST();
				String parItem = pd.getITEM();
				String parClass = pd.getCLASS();
				String parAttrib = pd.getATTRIB();
				// System.out.println("class"+parClass);
				// System.out.println("attrib"+parAttrib);
				PCKALLOC pck = new PCKALLOC();
				String it = rs.getString("ITEM").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				String qt = rs.getString("QTY").trim();
				String dt = rs.getString("USERTIME1").trim();
				float bomQty = bm.getParentQty(parItem, parClass, parAttrib);
				float unitQty = Float.parseFloat(qt);
				String item = "";
				String desc = "";
				String batch = "";
				String bin = "";
				String qty = "";
				// issVec = inv.getAllocatedItem(it,Float.parseFloat(qt));
				issVec = inv.getAllocatedItem(it, clas, attrib, unitQty);
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
						pck.setPCKBIN(bin);
						pck.setBATCH(batch);
						pck.setORDTYPE("PR");
						pck.setITEM(item);
						pck.setUSERTIME1(dt);
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
								+ sb.formatHTML(desc) + "</td>"
								+ "<td width=\"15%\">" + sb.formatHTML(batch)
								+ "</td>" + "<td width=\"15%\">"
								+ sb.formatHTML(bin) + "</td>"
								+ "<td width=\"10%\">" + sb.formatHTML(qty)
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
		// System.out.println(result);
		return result;
	}

	public int getNextREFLNNO(String aREFNO) throws Exception {
		int res = 0;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select max(cast(reflnno as int))+1 from PCKDET where REFNO = '"
					+ aREFNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				res = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return res;
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
}
