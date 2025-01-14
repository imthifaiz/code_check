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
import com.track.util.StrUtils;

public class SODET {
	private boolean printQuery = MLoggerConstant.SODET_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.SODET_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	String sPLANT = "SIS";
	String sSONO = "";
	String sSOLNNO = "";
	String sLNSTAT = "";
	String sITEM = "";
	String sCLASS = "";
	String sATTRIB = "";
	String sASNMT = "";
	float fQTYOR;
	float fQTYRC;
	float fQTYAC;
	float fQTYRJ;
	String sLOC = "";
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
	float fUSERDBL1;
	float fUSERDBL2;
	float fUSERDBL3;
	float fUSERDBL4;
	float fUSERDBL5;
	float fUSERDBL6;

	public String getPLANT() {
		return sPLANT;
	}

	public String getSONO() {
		return sSONO;
	}

	public String getSOLNNO() {
		return sSOLNNO;
	}

	public String getLNSTAT() {
		return sLNSTAT;
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

	public String getASNMT() {
		return sASNMT;
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

	public void setSONO(String aSONO) {
		sSONO = aSONO;
	}

	public void setSOLNNO(String aSOLNNO) {
		sSOLNNO = aSOLNNO;
	}

	public void setLNSTAT(String aLNSTAT) {
		sLNSTAT = aLNSTAT;
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

	public void setASNMT(String aASNMT) {
		sASNMT = aASNMT;
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
	 * constructor method SODET sb - SQL Bean gn - Utilites
	 */
	public SODET() throws Exception {
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
	 * Insert Command - SODET
	 * 
	 * 
	 */
	public int insertSODET() throws Exception {
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
			sql = "Insert into SODET values ('" + sPLANT + "','" + sSONO
					+ "','" + sSOLNNO + "','" + sLNSTAT + "','" + sITEM + "','"
					+ sCLASS + "','" + sATTRIB + "','" + sASNMT + "'," + fQTYOR
					+ "," + fQTYRC + "," + fQTYAC + "," + fQTYRJ + ",'" + sLOC
					+ "','" + sWHID + "','" + sPOLTYPE + "','" + sUNITMO
					+ "','" + sDELDATE + "','" + sCOMMENT1 + "','" + sCOMMENT2
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

	/**
	 * Update Function - SODET
	 * 
	 * 
	 */
	public int updateSODET() throws Exception {
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
			sql = "Update SODET set PLANT= '" + sPLANT + "',SONO= '" + sSONO
					+ "',SOLNNO= '" + sSOLNNO + "',LNSTAT= '" + sLNSTAT
					+ "',ITEM= '" + sITEM + "',ASNMT= '" + sASNMT
					+ "',QTYOR = " + fQTYOR + ",QTYRC = " + fQTYRC
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
					+ "',USERTIME1 = '" + sUSERTIME1 + "',USERTIME2 = '"
					+ sUSERTIME2 + "',USERTIME3 = '" + sUSERTIME3
					+ "',USERDBL1 = " + fUSERDBL1 + ",USERDBL2 = " + fUSERDBL2
					+ ",USERDBL3 = " + fUSERDBL3 + ",USERDBL4 = " + fUSERDBL4
					+ ",USERDBL5 = " + fUSERDBL5 + ",USERDBL6 = " + fUSERDBL6
					+ " where ";
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
	 * Select FunctionSODET
	 * 
	 * 
	 */
	public void selectSODET() throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from SODET where PLANT = '" + sPLANT
					+ "' and SOLNNO = '" + sSOLNNO + "'and SONO = '" + sSONO
					+ "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sLNSTAT = rs.getString("LNSTAT");
				sITEM = rs.getString("ITEM");
				sCLASS = rs.getString("CLASS");
				sATTRIB = rs.getString("ATTRIB");
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

	/**
	 * Delete Function -- SODET
	 * 
	 * 
	 */
	public int deleteSODET() throws Exception {
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
			sql = "delete from SODET where";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String listSODET(String aSONO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		NumberFormat formatter = new DecimalFormat("#,##0.00#");
		try {
			Statement stmt = con.createStatement();
			/*
			 * if ( (aSONO.length() > 0)) { vecWhr.addElement("SONO = '" + aSONO
			 * + "'"); } int cnt = vecWhr.size(); if (cnt > 0) { where =
			 * "where "; for (int i = 0; i < cnt; i++) { where = where +
			 * vecWhr.elementAt(i).toString() + " "; if (i < cnt - 1) { where =
			 * where + " and "; } } }
			 */
			where = (aSONO.equalsIgnoreCase("")) ? "" : " WHERE SONO = '"
					+ aSONO + "'";
			q = "select sono,solnno,class,item,attrib,qtyor,qtyrc,qtyac,unitmo,deldate from sodet "
					+ where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String sono = rs.getString("SONO").trim();
				String solnno = rs.getString("SOLNNO").trim();
				String brand = rs.getString("CLASS").trim();
				ITEMMST itm = new ITEMMST();
				String item = rs.getString("ITEM").trim();
				String attrib = rs.getString("ATTRIB").trim();
				String desc = itm.getItemDesc(item);
				link = "<a href=\"" + "modiSODET.jsp?SONO=" + sono + "&SOLNNO="
						+ solnno + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"5%\">"
						+ link
						+ "<FONT COLOR=\"blue\">"
						+ solnno
						+ "</a></font>"
						+ "</td>"
						+ "<td width=\"8%\">"
						+ brand
						+ "</td>"
						+ "<td width=\"15%\">"
						+ sb.formatHTML(item)
						+ "</td>"
						+ "<td width=\"30%\">"
						+ sb.formatHTML(desc)
						+ "</td>"
						+ "<td width=\"8%\">"
						+ attrib
						+ "</td>"
						+ "<td width=\"10%\" align=\"right\">"
						+ sb.formatHTML(formatter.format(Double.parseDouble(rs
								.getString("QTYOR").trim())))
						+ "</td>"
						+ "<td width=\"10%\" align=\"center\">"
						+ sb.formatHTML(rs.getString("UNITMO").trim())
						+ "</td>"
						+ "<td width=\"15%\">"
						+ sb.formatHTML(gn.getDB2UserDate(rs.getString(
								"DELDATE").trim())) + "</td></tr>";

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String listDODET4DO(String aDONO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		ITEMMST im = new ITEMMST();

		try {
			Statement stmt = con.createStatement();
			q = "select dono,dolnno,item,class,attrib,lnstat,qtyor,qtyac,unitmo,qtyis from dodet where DONO= '"
					+ aDONO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String sono = rs.getString("DONO").trim();
				String solnno = rs.getString("DOLNNO").trim();
				String item = rs.getString("ITEM").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				String desc = im.getItemDesc(item);
				String stat = rs.getString("LNSTAT").trim();
				if (stat.equalsIgnoreCase("C")) {
					link = "";// "<a href=\"" + "modiDODET1.jsp?DONO=" + sono +
					// "&DOLNNO=" +
					// solnno + "\">";
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"5%\">"
							+ link
							+ "<FONT COLOR=\"blue\">"
							+ solnno
							+ "</font>"
							+ "</td>"
							+ "<td width=\"5%\">"
							+ sb.formatHTML(clas)
							+ "</td>"
							+ "<td width=\"20%\">"
							+ sb.formatHTML(item)
							+ "</td>"
							+ "<td width=\"20%\">"
							+ sb.formatHTML(desc)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(attrib)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("QTYOR").trim())
							+ "</td>"
							+
							// "<td width=\"10%\">" +
							// sb.formatHTML(rs.getString("QTYRC").trim()) +
							// "</td>" +
							"<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("QTYIS").trim())
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(im.getBaseUom(item.trim()))
							+ "</td></tr>";
				} else {
					link = "";// "<a href=\"" + "modiDODET1.jsp?DONO=" + sono +
					// "&DOLNNO=" +
					// solnno + "\">";
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"5%\">"
							+ link
							+ "<FONT COLOR=\"blue\">"
							+ solnno
							+ "</font>"
							+ "</td>"
							+ "<td width=\"5%\">"
							+ sb.formatHTML(clas)
							+ "</td>"
							+ "<td width=\"20%\">"
							+ sb.formatHTML(item)
							+ "</td>"
							+ "<td width=\"20%\">"
							+ sb.formatHTML(desc)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(attrib)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("QTYOR").trim())
							+ "</td>"
							+
							// "<td width=\"10%\">" +
							// sb.formatHTML(rs.getString("QTYIS").trim()) +
							// "</td>" +
							"<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("QTYIS").trim())
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(im.getBaseUom(item.trim()))
							+ "</td></tr>";

				}

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
			im = null;
		}
		// System.out.println(result);
		return result;
	}

	public String listSODET4DO(String aSONO, String aDONO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if ((aSONO.length() > 0)) {
				vecWhr.addElement("SONO = '" + aSONO + "'");
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

			q = "select sono,solnno,item,class,attrib,lnstat,qtyor,qtyrc,qtyac,unitmo from sodet "
					+ where + " and (QTYOR-QTYAC)>0";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			boolean found = true;
			INVMST inv = new INVMST();
			String bgcolor = "";
			while (rs.next()) {
				found = true;
				String sono = rs.getString("SONO").trim();
				String solnno = rs.getString("SOLNNO").trim();
				ITEMMST itm = new ITEMMST();
				String item = rs.getString("ITEM").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				String desc = itm.getItemDesc(item);
				String stat = rs.getString("LNSTAT").trim();
				found = inv.isQtyAvaliable(item, clas, attrib);
				if (!found) {
					bgcolor = "bgcolor='red'";
				} else {
					bgcolor = "";
					System.out.println("**** found");
				}
				if (stat.equalsIgnoreCase("C")) {
					result += "<tr valign=\"middle\" " + bgcolor + ">"
							+ "<td align=\"center\" width=\"5%\">" + solnno
							+ "</td>" + "<td width=\"5%\">"
							+ sb.formatHTML(clas) + "</td>"
							+ "<td width=\"20%\">" + sb.formatHTML(item)
							+ "</td>" + "<td width=\"20%\">"
							+ sb.formatHTML(desc) + "</td>"
							+ "<td width=\"10%\">" + sb.formatHTML(attrib)
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("QTYOR").trim())
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("QTYRC").trim())
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("QTYAC").trim())
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("UNITMO").trim())
							+ "</td></tr>";
				} else {
					link = "<a href=\"" + "modiDODET.jsp?DONO=" + aDONO
							+ "&SONO=" + sono + "&SOLNNO=" + solnno + "\">";
					result += "<tr valign=\"middle\" " + bgcolor + ">"
							+ "<td align=\"center\" width=\"5%\">" + link
							+ "<FONT COLOR=\"blue\">" + solnno + "</a></font>"
							+ "</td>" + "<td width=\"5%\">"
							+ sb.formatHTML(clas) + "</td>"
							+ "<td width=\"20%\">" + sb.formatHTML(item)
							+ "</td>" + "<td width=\"20%\">"
							+ sb.formatHTML(desc) + "</td>"
							+ "<td width=\"10%\">" + sb.formatHTML(attrib)
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("QTYOR").trim())
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("QTYRC").trim())
							+ "</td>" + "<td width=\"10%\">"
							+ sb.formatHTML(rs.getString("QTYAC").trim())
							+ "</td>" + "<td width=\"10%\">"
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

	public int deleteSODET(String aSONO, String aSOLNNO) throws Exception {
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
			sql = "delete from SODET where PLANT= '" + sPLANT + "' and SONO= '"
					+ aSONO + "' and SOLNNO= '" + aSOLNNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			DbBean.writeError("SODET", "deleteSODET()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String getUpdateSql() throws Exception {
		String sql = "";
		try {
			sql = "Update SODET set LNSTAT= '" + sLNSTAT + "',ITEM= '" + sITEM
					+ "',CLASS= '" + sCLASS + "',ATTRIB= '" + sATTRIB
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
					+ " where PLANT= '" + sPLANT + "' and SONO= '" + sSONO
					+ "' and SOLNNO= '" + sSOLNNO + "'";
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sql;
	}

	public String getUpdateSql4DO() throws Exception {
		String sql = "";
		sql = "Update SODET set QTYRC = " + fQTYRC + "+" + fQTYAC
				+ ",QTYAC = 0" + " where PLANT= '" + sPLANT + "' and SONO= '"
				+ sSONO + "' and SOLNNO= '" + sSOLNNO + "'";
		this.mLogger.query(this.printQuery, sql);
		return sql;
	}

	public String getUpdateSql(String sono, String lnno, float issQty)
			throws Exception {
		String sql = "";
		sql = "Update SODET set QTYRC = " + issQty + "" + " where PLANT= '"
				+ sPLANT + "' and SONO= '" + sono + "' and SOLNNO= '" + lnno
				+ "'";
		return sql;
	}

	public String getNextLineNo(String aSONO) throws Exception {
		String q = "";
		String result = "";
		int cnt = 0;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select max(cast(solnno as int))+1 from sodet where sono = '"
					+ aSONO + "'";
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

	public String getSalesItem(String aSONO) throws Exception {
		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			String q = "select distinct item from sodet where sono ='" + aSONO
					+ "'";
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

	public String getBrand(String aSONO) throws Exception {
		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			String q = "select distinct class from sodet where sono ='" + aSONO
					+ "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("CLASS").trim();
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

	public String getCategory(String aSONO) throws Exception {
		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			String q = "select distinct attrib from sodet where sono ='"
					+ aSONO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("ATTRIB").trim();
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

	public String getSO4DO(String aDONO) throws Exception {
		String selectStr = "";
		Connection con = getDBConnection();
		String val = "";

		try {

			Statement stmt = con.createStatement();
			String q = "select distinct (SONO) from dohdr where DONO ='"
					+ aDONO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			if (rs != null) {
				while (rs.next()) {
					val = rs.getString("SONO").trim();
				}
			}
		} // End of try
		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return val;
	}

	public boolean processDeliveryOrder(String aSONO, String aDONO,
			String aDELDATE, String aEnby) throws Exception {
		boolean res = false;
		ArrayList ar = new ArrayList();
		Vector vec = new Vector();
		DODET delOrd = new DODET();

		BATMST btb = new BATMST();
		PCKALLOC pcalloc = new PCKALLOC();
		INVMST im = new INVMST();
		String item = "", clas = "", attrib = "", lno = "", uom = "";
		float qty = 0;
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			String q = "select sono,solnno,item,class,attrib,unitmo,qtyor,qtyrc,qtyac,qtyrj from sodet where sono = '"
					+ aSONO + "' and QTYRC > 0";
			this.mLogger.query(this.printQuery, q);

			ResultSet rs = stmt.executeQuery(q);
			int cnt = 1;
			ar.add("delete from dodet where dono = '" + aDONO + "'");
			ar.add("delete from dohdr where dono ='" + aDONO + "'");
			ar.add("insert into dohdr(plant,dono,sono) values ('PLNT','"
					+ aDONO + "','" + aSONO + "')");
			if (rs != null) {
				String val;
				while (rs.next()) {
					lno = rs.getString("SOLNNO").trim();
					item = rs.getString("ITEM").trim();
					clas = rs.getString("CLASS").trim();
					attrib = rs.getString("ATTRIB").trim();
					uom = rs.getString("UNITMO").trim();
					qty = Float.parseFloat(rs.getString("QTYRC").trim());
					delOrd.setDONO(aDONO);
					delOrd.setDOLNNO(lno);
					delOrd.setDELDATE(aDELDATE);
					delOrd.setITEM(item);
					delOrd.setCLASS(clas);
					delOrd.setATTRIB(attrib);
					delOrd.setQTYOR(Float.parseFloat(rs.getString("QTYOR")
							.trim()));
					delOrd.setQTYIS(qty);
					delOrd.setQTYAC(qty);
					delOrd.setLNSTAT("N");
					delOrd.setCRBY(aEnby);
					delOrd.setCRAT(gn.getDateTime());
					ar.add(delOrd.getInsertSql());

					vec = im.getAllocatedItem(item, clas, attrib, qty);
					if (vec.size() > 0) {
						for (int i = 0; i < vec.size(); i++) {
							Vector tmpVec = new Vector();
							tmpVec = (Vector) vec.elementAt(i);
							pcalloc.setREFNO(aDONO);
							pcalloc.setREFLNNO(String.valueOf(cnt));
							pcalloc.setORDTYPE("D");
							pcalloc.setITEM(tmpVec.elementAt(0).toString());
							pcalloc.setCLASS(clas);
							pcalloc.setATTRIB(attrib);
							pcalloc.setBATCH(tmpVec.elementAt(1).toString());
							pcalloc.setBIN(tmpVec.elementAt(2).toString());
							pcalloc.setQTY(Float.parseFloat(tmpVec.elementAt(3)
									.toString()));
							pcalloc.setSTATUS("N");
							pcalloc.setUOM(uom);
							ar.add(pcalloc.getInsertSQL());
							btb.setBATCH(tmpVec.elementAt(1).toString());
							btb.setITEM(tmpVec.elementAt(0).toString());
							if (!(btb
									.isAvaliable(
											tmpVec.elementAt(1).toString(),
											tmpVec.elementAt(0).toString()))) {
								// System.out.println("@@@@@@@@@@@@@@i am inside btb available@@@@@@@@@@@@@");
								// System.out.println("BATCH : " +
								// btb.getBATCH());
								ar.add(btb.getInsertSql());
							}
							cnt++;
						}

					} else {

						pcalloc.setREFNO(aDONO);
						pcalloc.setREFLNNO(String.valueOf(cnt));
						pcalloc.setORDTYPE("D");
						pcalloc.setITEM(item);
						pcalloc.setCLASS(clas);
						pcalloc.setATTRIB(attrib);
						// pcalloc.setBATCH(tmpVec.elementAt(1).toString());
						pcalloc.setBIN("PROD");
						pcalloc.setQTY(qty);
						pcalloc.setSTATUS("N");
						pcalloc.setUOM(uom);
						ar.add(pcalloc.getInsertSQL());

					}
					ar.add("update sodet set qtyrc = 0,qtyac=qtyac+" + qty
							+ " where sono = '" + aSONO + "' and solnno=" + lno
							+ "");
				}

			}
			sb.setmLogger(mLogger);
			res = sb.insertBatchRecords(ar);
		} // End of try
		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(res);
		return res;
	}

	public String listSOReport(String aFRDT, String aTODT, String aCUST,
			String aSTATUS) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		SOHDR sh = new SOHDR();
		CUSTMST cm = new CUSTMST();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if (aFRDT.length() > 0) {
				vecWhr.addElement("a.CRAT > '" + aFRDT + "'");
			}
			if (aTODT.length() > 0) {
				vecWhr.addElement("a.CRAT < '" + aTODT + "'");
			}
			if (aCUST.length() > 0) {
				vecWhr.addElement("c.CUSTNO = '" + aCUST + "'");
			}
			if (aSTATUS.length() > 0) {
				vecWhr.addElement("a.ITEM = '" + aSTATUS + "'");
			}
			int cnt = vecWhr.size();
			if (cnt > 0) {
				where = "where a.sono = c.sono and a.item = b.item and ";
				for (int i = 0; i < cnt; i++) {
					where = where + vecWhr.elementAt(i).toString() + " ";
					if (i < cnt - 1) {
						where = where + " and ";
					}
				}
			}
			// q = "select sono,item,class,qtyor,qtyac from sodet " +where;
			q = "select a.sono,a.item,b.itemdesc,a.class,a.attrib,a.qtyor,a.qtyrc,c.custno,a.deldate from sodet a , itemmst b, "
					+ " sohdr c " + where;
			this.mLogger.query(this.printQuery, q);
			// a.sono = c.sono and a.item = b.item and a.CRAT > '20021201' and
			// a.CRAT < '20031222' and c.CUSTNO = 'C001' ";
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String sono = rs.getString("SONO").trim();
				String item = rs.getString("ITEM").trim();
				String itemdesc = rs.getString("ITEMDESC").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				String qtyor = rs.getString("QTYOR").trim();
				String qtyac = rs.getString("QTYRC").trim();
				float bal = Float.parseFloat(qtyor) - Float.parseFloat(qtyac);
				NumberFormat formatter = new DecimalFormat("#,##0.00#");
				String cust = rs.getString("CUSTNO").trim();
				String orddate = rs.getString("DELDATE").trim();
				link = "<a href=\"" + "createSO.jsp?SONO=" + sono + "\">";
				if (bal > 0) {
					result += "<tr valign=\"middle\">" +
					// "<td  width=\"10%\">"+ sono +"</td>"+
							"<td align=\"center\" width=\"10%\">"
							+ link
							+ "<FONT COLOR=\"blue\">"
							+ sono
							+ "</font></a>"
							+ "</td>"
							+ "<td width=\"15%\">"
							+ cm.getCustDesc(cust)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(item)
							+ "</td>"
							+ "<td width=\"20%\">"
							+ sb.formatHTML(itemdesc)
							+ "</td>"
							+ "<td width=\"5%\">"
							+ sb.formatHTML(clas)
							+ "</td>"
							+ "<td width=\"5%\">"
							+ sb.formatHTML(attrib)
							+ "</td>"
							+ "<td align=\"right\" width=\"7%\">"
							+ sb.formatHTML(formatter.format(Double
									.parseDouble(qtyor)))
							+ "</td>"
							+ "<td align=\"right\" width=\"10%\">"
							+ sb.formatHTML(formatter.format(Double
									.parseDouble(qtyac))) + "</td>"
							+ "<td align=\"right\" width=\"10%\">"
							+ sb.formatHTML(formatter.format(bal) + "")
							+ "</td>" + "<td width=\"10%\">"
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

	public String RptlistSOReport(String aFRDT, String aTODT, String aCUST,
			String aSTATUS) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		SOHDR sh = new SOHDR();
		CUSTMST cm = new CUSTMST();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if (aFRDT.length() > 0) {
				vecWhr.addElement("a.CRAT > '" + aFRDT + "'");
			}
			if (aTODT.length() > 0) {
				vecWhr.addElement("a.CRAT < '" + aTODT + "'");
			}
			if (aCUST.length() > 0) {
				vecWhr.addElement("c.CUSTNO = '" + aCUST + "'");
			}
			if (aSTATUS.length() > 0) {
				vecWhr.addElement("a.ITEM = '" + aSTATUS + "'");
			}
			int cnt = vecWhr.size();
			if (cnt > 0) {
				where = "where a.sono = c.sono and a.item = b.item and ";
				for (int i = 0; i < cnt; i++) {
					where = where + vecWhr.elementAt(i).toString() + " ";
					if (i < cnt - 1) {
						where = where + " and ";
					}
				}
			}
			// q = "select sono,item,class,qtyor,qtyac from sodet " +where;
			q = "select a.sono,a.item,b.itemdesc,a.class,a.qtyor,a.qtyrc,c.custno,a.deldate from sodet a , itemmst b, "
					+ " sohdr c " + where;
			this.mLogger.query(this.printQuery, q);
			// a.sono = c.sono and a.item = b.item and a.CRAT > '20021201' and
			// a.CRAT < '20031222' and c.CUSTNO = 'C001' ";
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String sono = rs.getString("SONO").trim();
				String item = rs.getString("ITEM").trim();
				String itemdesc = rs.getString("ITEMDESC").trim();
				String clas = rs.getString("CLASS").trim();
				String qtyor = rs.getString("QTYOR").trim();
				String qtyac = rs.getString("QTYRC").trim();
				float bal = Float.parseFloat(qtyor) - Float.parseFloat(qtyac);
				NumberFormat formatter = new DecimalFormat("#,##0.00#");
				String cust = rs.getString("CUSTNO").trim();
				String orddate = rs.getString("DELDATE").trim();
				if (bal > 0) {
					result += "<tr valign=\"middle\">" + "<td  width=\"10%\">"
							+ sono
							+ "</td>"
							+ "<td width=\"15%\">"
							+ cm.getCustDesc(cust)
							+ "</td>"
							+ "<td width=\"10%\">"
							+ sb.formatHTML(item)
							+ "</td>"
							+ "<td width=\"20%\">"
							+ sb.formatHTML(itemdesc)
							+ "</td>"
							+ "<td width=\"5%\">"
							+ sb.formatHTML(clas)
							+ "</td>"
							+ "<td align=\"right\" width=\"10%\">"
							+ sb.formatHTML(formatter.format(Double
									.parseDouble(qtyor)))
							+ "</td>"
							+ "<td align=\"right\" width=\"10%\">"
							+ sb.formatHTML(formatter.format(Double
									.parseDouble(qtyac))) + "</td>"
							+ "<td align=\"right\" width=\"10%\">"
							+ sb.formatHTML(formatter.format(bal) + "")
							+ "</td>" + "<td width=\"10%\">"
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

	public String listSOAllReport(String aFRDT, String aTODT, String aCUST,
			String aITEM) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		SOHDR sh = new SOHDR();
		CUSTMST cm = new CUSTMST();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if (aFRDT.length() > 0) {
				vecWhr.addElement("b.CRAT > '" + aFRDT + "'");
			}
			if (aTODT.length() > 0) {
				vecWhr.addElement("b.CRAT < '" + aTODT + "'");
			}
			if (aCUST.length() > 0) {
				vecWhr.addElement("a.CUSTNO = '" + aCUST + "'");
			}
			if (aITEM.length() > 0) {
				vecWhr.addElement("b.ITEM = '" + aITEM + "'");
			}
			int cnt = vecWhr.size();
			System.out.println(cnt);
			if (cnt > 0) {
				where = "where b.sono = a.sono and ";
				for (int i = 0; i < cnt; i++) {
					where = where + vecWhr.elementAt(i).toString() + " ";
					if (i < cnt - 1) {
						where = where + " and ";
					}
				}
			}
			q = "select a.SONO,a.CUSTNO,b.ITEM,b.CLASS, b.ATTRIB, b.QTYOR,b.DELDATE from sohdr a, sodet b "
					+ where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String sono = rs.getString("SONO").trim();
				ITEMMST itm = new ITEMMST();
				String item = rs.getString("ITEM").trim();
				String clas = rs.getString("CLASS").trim();
				String attrib = rs.getString("ATTRIB").trim();
				String qtyor = rs.getString("QTYOR").trim();
				NumberFormat formatter = new DecimalFormat("#,##0.00#");
				String desc = itm.getItemDesc(item);
				String deldate = rs.getString("DELDATE").trim();
				result += "<tr valign=\"middle\">" + "<td  width=\"10%\">"
						+ sono
						+ "</td>"
						+ "<td width=\"15%\">"
						+ cm.getCustDesc(sh.getCustomerCode(sono))
						+ "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(item)
						+ "</td>"
						+ "<td width=\"20%\">"
						+ sb.formatHTML(desc)
						+ "</td>"
						+ "<td width=\"5%\">"
						+ sb.formatHTML(clas)
						+ "</td>"
						+ "<td width=\"5%\">"
						+ sb.formatHTML(attrib)
						+ "</td>"
						+ "<td align=\"right\" width=\"10%\">"
						+ sb.formatHTML(formatter.format(Double
								.parseDouble(qtyor))) + "</td>"
						+ "<td width=\"10%\">"
						+ sb.formatHTML(gn.getDB2UserDate(deldate))
						+ "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public int updateSODET(String aSONO, String aSOLNNO, float qty)
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
			sql = "UPDATE SODET SET QTYOR = '" + qty + "' where PLANT= '"
					+ sPLANT + "' and SONO= '" + aSONO + "' and SOLNNO= '"
					+ aSOLNNO + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int updateSODET(String aSONO, String aSOLNNO, float qty,
			String deldate) throws Exception {
		String sql;
		Connection con = getDBConnection();
		ResultSet rs = null;
		StrUtils su = new StrUtils();
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = "UPDATE SODET SET QTYOR = '" + qty + "',DELDATE = '"
					+ su.getSQLDate(deldate) + "' where PLANT= '" + sPLANT
					+ "' and SONO= '" + aSONO + "' and SOLNNO= '" + aSOLNNO
					+ "'";
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int updateQTYRC(String aSONO) throws Exception {
		String sql;
		Connection con = getDBConnection();
		ResultSet rs = null;
		StrUtils su = new StrUtils();
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = "UPDATE SODET SET QTYRC =0 WHERE aSONO = '" + aSONO + "'";
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