package com.track.tables;

import java.io.BufferedReader;
import java.io.FileReader;
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
import com.track.util.StrUtils;

public class VENDMST implements Serializable {
	private boolean printQuery = MLoggerConstant.VENDMST_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.VENDMST_PRINTPLANTMASTERLOG;

	sqlBean sb;
	Generator gn;

	String sPLANT = "SIS";
	String sVENDNO = "";
	String sVENDDESC = "";
	String sCOMMENT1 = "";
	String sACTIVEF = "";
	String sVNAME = "";
	String sADDR1 = "";
	String sADDR2 = "";
	String sADDR3 = "";
	String sSTATE = "";
	String sCOUNTRY = "";
	String sZIP = "";
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

	public String getVENDNO() {
		return sVENDNO;
	}

	public String getVENDDESC() {
		return sVENDDESC;
	}

	public String getCOMMENT1() {
		return sCOMMENT1;
	}

	public String getACTIVEF() {
		return sACTIVEF;
	}

	public String getVNAME() {
		return sVNAME;
	}

	public String getADDR1() {
		return sADDR1;
	}

	public String getADDR2() {
		return sADDR2;
	}

	public String getADDR3() {
		return sADDR3;
	}

	public String getSTATE() {
		return sSTATE;
	}

	public String getCOUNTRY() {
		return sCOUNTRY;
	}

	public String getZIP() {
		return sZIP;
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

	public void setVENDNO(String aVENDNO) {
		sVENDNO = aVENDNO;
	}

	public void setVENDDESC(String aVENDDESC) {
		sVENDDESC = aVENDDESC;
	}

	public void setCOMMENT1(String aCOMMENT1) {
		sCOMMENT1 = aCOMMENT1;
	}

	public void setACTIVEF(String aACTIVEF) {
		sACTIVEF = aACTIVEF;
	}

	public void setVNAME(String aVNAME) {
		sVNAME = aVNAME;
	}

	public void setADDR1(String aADDR1) {
		sADDR1 = aADDR1;
	}

	public void setADDR2(String aADDR2) {
		sADDR2 = aADDR2;
	}

	public void setADDR3(String aADDR3) {
		sADDR3 = aADDR3;
	}

	public void setSTATE(String aSTATE) {
		sSTATE = aSTATE;
	}

	public void setCOUNTRY(String aCOUNTRY) {
		sCOUNTRY = aCOUNTRY;
	}

	public void setZIP(String aZIP) {
		sZIP = aZIP;
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
	 * constructor method VENDMST sb - SQL Bean gn - Utilites
	 */
	public VENDMST() throws Exception {
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
	 * Insert Command - VENDMST
	 * 
	 * 
	 */
	public int insertVENDMST() throws Exception {
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
			sql = "Insert into VENDMST values ('" + sPLANT + "','" + sVENDNO
					+ "','" + sVENDDESC + "','" + sCOMMENT1 + "','" + sACTIVEF
					+ "','" + sVNAME + "','" + sADDR1 + "','" + sADDR2 + "','"
					+ sADDR3 + "','" + sSTATE + "','" + sCOUNTRY + "','" + sZIP
					+ "','" + sCRAT + "','" + sCRBY + "','" + sUPAT + "','"
					+ sUPBY + "','" + sRECSTAT + "','" + sUSERFLD1 + "','"
					+ sUSERFLD2 + "','" + sUSERFLD3 + "','" + sUSERFLD4 + "','"
					+ sUSERFLD5 + "','" + sUSERFLD6 + "','" + sUSERFLG1 + "','"
					+ sUSERFLG2 + "','" + sUSERFLG3 + "','" + sUSERFLG4 + "','"
					+ sUSERFLG5 + "','" + sUSERFLG6 + "','" + sUSERTIME1
					+ "','" + sUSERTIME2 + "','" + sUSERTIME3 + "',"
					+ fUSERDBL1 + "," + fUSERDBL2 + "," + fUSERDBL3 + ","
					+ fUSERDBL4 + "," + fUSERDBL5 + "," + fUSERDBL6 + ")";
			returnCode = sb.insertRecords(sql);
			// System.out.println(sql);

		} catch (Exception e) {
			DbBean.writeError("VENDMST", "insertVENDMST()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	/**
	 * Update Function - VENDMST
	 * 
	 * 
	 */
	public int updateVENDMST() throws Exception {
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
			sql = "Update VENDMST set VENDDESC= '" + sVENDDESC
					+ "',COMMENT1= '" + sCOMMENT1 + "',ACTIVEF= '" + sACTIVEF
					+ "',VNAME= '" + sVNAME + "',ADDR1= '" + sADDR1
					+ "',ADDR2= '" + sADDR2 + "',ADDR3= '" + sADDR3
					+ "',STATE= '" + sSTATE + "',COUNTRY= '" + sCOUNTRY
					+ "',ZIP= '" + sZIP + "',CRAT = '" + sCRAT + "',CRBY= '"
					+ sCRBY + "',UPAT = '" + sUPAT + "',UPBY= '" + sUPBY
					+ "',RECSTAT= '" + sRECSTAT + "',USERFLD1= '" + sUSERFLD1
					+ "',USERFLD2= '" + sUSERFLD2 + "',USERFLD3= '" + sUSERFLD3
					+ "',USERFLD4= '" + sUSERFLD4 + "',USERFLD5= '" + sUSERFLD5
					+ "',USERFLD6= '" + sUSERFLD6 + "',USERFLG1= '" + sUSERFLG1
					+ "',USERFLG2= '" + sUSERFLG2 + "',USERFLG3= '" + sUSERFLG3
					+ "',USERFLG4= '" + sUSERFLG4 + "',USERFLG5= '" + sUSERFLG5
					+ "',USERFLG6= '" + sUSERFLG6 + "',USERTIME1 = '"
					+ sUSERTIME1 + "',USERTIME2 = '" + sUSERTIME2
					+ "',USERTIME3 = '" + sUSERTIME3 + "',USERDBL1 = "
					+ fUSERDBL1 + ",USERDBL2 = " + fUSERDBL2 + ",USERDBL3 = "
					+ fUSERDBL3 + ",USERDBL4 = " + fUSERDBL4 + ",USERDBL5 = "
					+ fUSERDBL5 + ",USERDBL6 = " + fUSERDBL6
					+ " where PLANT= '" + sPLANT + "' and VENDNO= '" + sVENDNO
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
	 * Select FunctionVENDMST
	 * 
	 * 
	 */
	public void selectVENDMST() throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from VENDMST where PLANT= 'COM' and VENDNO= '"
					+ sVENDNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sVENDDESC = rs.getString("VENDDESC");
				sCOMMENT1 = rs.getString("COMMENT1");
				sACTIVEF = rs.getString("ACTIVEF");
				sVNAME = rs.getString("VNAME");
				sADDR1 = rs.getString("ADDR1");
				sADDR2 = rs.getString("ADDR2");
				sADDR3 = rs.getString("ADDR3");
				sSTATE = rs.getString("STATE");
				sCOUNTRY = rs.getString("COUNTRY");
				sZIP = rs.getString("ZIP");
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
	 * Delete Function -- VENDMST
	 * 
	 * 
	 */
	public int deleteVENDMST() throws Exception {
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
			sql = "delete from VENDMST where PLANT= '" + sPLANT
					+ "' and VENDNO= '" + sVENDNO + "'";
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
	 * public boolean isAvaliable(String aPLANT, String aVENDNO) throws
	 * Exception{ boolean res = false; sPLANT = aPLANT; sVENDNO = aVENDNO;
	 * selectVENDMST(); return res; }
	 */
	public boolean isAvaliable(String aPLANT, String aVENDNO) throws Exception {
		boolean result = false;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) from VENDMST where PLANT= '" + sPLANT
					+ "' and VENDNO= '" + sVENDNO + "'";
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

	public void processLoading(String filepath) throws Exception {
		StrUtils str = new StrUtils();
		Connection con = getDBConnection();
		con.setAutoCommit(false);
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		String line = null;
		while ((line = reader.readLine()) != null) {
			Vector vecStr = str.parseStringGetAll(line, ",");
			sPLANT = "C1P1";
			sVENDNO = vecStr.elementAt(0).toString().trim();
			sVNAME = vecStr.elementAt(1).toString().trim();
			sADDR1 = vecStr.elementAt(3).toString().trim();
			sADDR2 = vecStr.elementAt(4).toString().trim();
			sADDR3 = vecStr.elementAt(5).toString().trim();
			if (isAvaliable(sPLANT, sVENDNO)) {
				sUPBY = "CR";
				// System.out.println("Update");
				updateVENDMST();
			} else {
				sCRBY = "CR";
				insertVENDMST();
			}
		}

	}

	public String listAllVENDMST(String aVendName) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select vendno,vname from vendmst where vname like '"
					+ aVendName + "%'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String vendno = rs.getString("VENDNO").trim();
				String vname = rs.getString("VNAME").trim();
				link = "<a href=\"" + "vendMastCH.jsp?VENDNO=" + vendno + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"30%\">" + link
						+ "<FONT COLOR=\"blue\">" + vendno + "</font></a>"
						+ "</td>" + "<td width=\"70%\">" + sb.formatHTML(vname)
						+ "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	//public Map getVenderDetails(String )
	
	public String popAllVENDMST(String aVendName) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = (aVendName.equals("")) ? "select vendno,vname from vendmst where vname like '%'"
					: "select vendno,vname from vendmst where vname like '"
							+ aVendName + "%'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String vendno = rs.getString("VENDNO").trim();
				String vname = rs.getString("VNAME").trim();
				link = "<a href=\"javascript:window.opener.form.VENDNO.value="
						+ "'" + vendno + "';"
						+ "window.opener.form.VNAME.value=" + "'" + vname + "'"
						+ ";window.close();\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"30%\">" + link
						+ "<FONT COLOR=\"blue\">" + vendno + "</font></a>"
						+ "</td>" + "<td width=\"70%\">" + sb.formatHTML(vname)
						+ "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String getVendDesc(String aVENDNO) throws Exception {
		String q = "";
		Connection con = DbBean.getConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from VENDMST where PLANT= 'COM' and VENDNO= '"
					+ aVENDNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sVNAME = rs.getString("VNAME");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return sVNAME;
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
}