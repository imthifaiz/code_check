package com.track.tables;

// importing classes

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.sqlBean;

public class WOHDR {
	sqlBean sb;
	Generator gn;
	String sPLANT = "SIS";
	String sWONO = "";
	String sCUSTNO = "";
	String sORDDATE = "";
	String sISSUEDATE = "";
	String sSHIPMOD = "";
	String sSHIPVIA = "";
	String sTERMS = "";
	String sPOTYPE = "";
	String sPOSTAT = "";
	float fTOTQTY;
	float fTOTWGT;
	String sCOMMENT1 = "";
	String sCOMMENT2 = "";
	String sLBLGROUP = "";
	int iLBLCAT;
	int iLBLTYPE;
	String sORDTRK1 = "";
	String sORDTRK2 = "";
	int iPRIORTY;
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

	public String getWONO() {
		return sWONO;
	}

	public String getCUSTNO() {
		return sCUSTNO;
	}

	public String getORDDATE() {
		return sORDDATE;
	}

	public String getISSUEDATE() {
		return sISSUEDATE;
	}

	public String getSHIPMOD() {
		return sSHIPMOD;
	}

	public String getSHIPVIA() {
		return sSHIPVIA;
	}

	public String getTERMS() {
		return sTERMS;
	}

	public String getPOTYPE() {
		return sPOTYPE;
	}

	public String getPOSTAT() {
		return sPOSTAT;
	}

	public float getTOTQTY() {
		return fTOTQTY;
	}

	public float getTOTWGT() {
		return fTOTWGT;
	}

	public String getCOMMENT1() {
		return sCOMMENT1;
	}

	public String getCOMMENT2() {
		return sCOMMENT2;
	}

	public String getLBLGROUP() {
		return sLBLGROUP;
	}

	public int getLBLCAT() {
		return iLBLCAT;
	}

	public int getLBLTYPE() {
		return iLBLTYPE;
	}

	public String getORDTRK1() {
		return sORDTRK1;
	}

	public String getORDTRK2() {
		return sORDTRK2;
	}

	public int getPRIORTY() {
		return iPRIORTY;
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

	public void setWONO(String aWONO) {
		sWONO = aWONO;
	}

	public void setCUSTNO(String aCUSTNO) {
		sCUSTNO = aCUSTNO;
	}

	public void setORDDATE(String aORDDATE) {
		sORDDATE = aORDDATE;
	}

	public void setISSUEDATE(String aISSUEDATE) {
		sISSUEDATE = aISSUEDATE;
	}

	public void setSHIPMOD(String aSHIPMOD) {
		sSHIPMOD = aSHIPMOD;
	}

	public void setSHIPVIA(String aSHIPVIA) {
		sSHIPVIA = aSHIPVIA;
	}

	public void setTERMS(String aTERMS) {
		sTERMS = aTERMS;
	}

	public void setPOTYPE(String aPOTYPE) {
		sPOTYPE = aPOTYPE;
	}

	public void setPOSTAT(String aPOSTAT) {
		sPOSTAT = aPOSTAT;
	}

	public void setTOTQTY(float aTOTQTY) {
		fTOTQTY = aTOTQTY;
	}

	public void setTOTWGT(float aTOTWGT) {
		fTOTWGT = aTOTWGT;
	}

	public void setCOMMENT1(String aCOMMENT1) {
		sCOMMENT1 = aCOMMENT1;
	}

	public void setCOMMENT2(String aCOMMENT2) {
		sCOMMENT2 = aCOMMENT2;
	}

	public void setLBLGROUP(String aLBLGROUP) {
		sLBLGROUP = aLBLGROUP;
	}

	public void setLBLCAT(int aLBLCAT) {
		iLBLCAT = aLBLCAT;
	}

	public void setLBLTYPE(int aLBLTYPE) {
		iLBLTYPE = aLBLTYPE;
	}

	public void setORDTRK1(String aORDTRK1) {
		sORDTRK1 = aORDTRK1;
	}

	public void setORDTRK2(String aORDTRK2) {
		sORDTRK2 = aORDTRK2;
	}

	public void setPRIORTY(int aPRIORTY) {
		iPRIORTY = aPRIORTY;
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
	 * constructor method WOHDR sb - SQL Bean gn - Utilites
	 */
	public WOHDR() throws Exception {
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
	 * Insert Command - WOHDR
	 * 
	 * 
	 */
	public int insertWOHDR() throws Exception {
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
			sql = "Insert into WOHDR values ('" + sPLANT + "','" + sWONO
					+ "','" + sCUSTNO + "','" + sORDDATE + "','" + sISSUEDATE
					+ "','" + sSHIPMOD + "','" + sSHIPVIA + "','" + sTERMS
					+ "','" + sPOTYPE + "','" + sPOSTAT + "'," + fTOTQTY + ","
					+ fTOTWGT + ",'" + sCOMMENT1 + "','" + sCOMMENT2 + "','"
					+ sLBLGROUP + "'," + iLBLCAT + "," + iLBLTYPE + ",'"
					+ sORDTRK1 + "','" + sORDTRK2 + "'," + iPRIORTY + ",'"
					+ sCRAT + "','" + sCRBY + "','" + sUPAT + "','" + sUPBY
					+ "','" + sRECSTAT + "','" + sUSERFLD1 + "','" + sUSERFLD2
					+ "','" + sUSERFLD3 + "','" + sUSERFLD4 + "','" + sUSERFLD5
					+ "','" + sUSERFLD6 + "','" + sUSERFLG1 + "','" + sUSERFLG2
					+ "','" + sUSERFLG3 + "','" + sUSERFLG4 + "','" + sUSERFLG5
					+ "','" + sUSERFLG6 + "','" + sUSERTIME1 + "','"
					+ sUSERTIME2 + "','" + sUSERTIME3 + "'," + fUSERDBL1 + ","
					+ fUSERDBL2 + "," + fUSERDBL3 + "," + fUSERDBL4 + ","
					+ fUSERDBL5 + "," + fUSERDBL6 + ")";
			// System.out.println(sql);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			DbBean.writeError("WOHDR", "insertWOHDR()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	/**
	 * Update Function - WOHDR
	 * 
	 * 
	 */
	public int updateWOHDR() throws Exception {
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
			sql = "Update WOHDR set PLANT= '" + sPLANT + "',WONO= '" + sWONO
					+ "',CUSTNO= '" + sCUSTNO + "',ORDDATE= '" + sORDDATE
					+ "',ISSUEDATE= '" + sISSUEDATE + "',SHIPMOD= '" + sSHIPMOD
					+ "',SHIPVIA= '" + sSHIPVIA + "',TERMS= '" + sTERMS
					+ "',POTYPE= '" + sPOTYPE + "',POSTAT= '" + sPOSTAT
					+ "',TOTQTY = " + fTOTQTY + ",TOTWGT = " + fTOTWGT
					+ ",COMMENT1= '" + sCOMMENT1 + "',COMMENT2= '" + sCOMMENT2
					+ "',LBLGROUP= '" + sLBLGROUP + "',LBLCAT = " + iLBLCAT
					+ ",LBLTYPE = " + iLBLTYPE + ",ORDTRK1= '" + sORDTRK1
					+ "',ORDTRK2= '" + sORDTRK2 + "',PRIORTY = " + iPRIORTY
					+ ",CRAT = '" + sCRAT + "',CRBY= '" + sCRBY + "',UPAT = '"
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
					+ " where WONO = " + sWONO;
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			DbBean.writeError("WOHDR", "updateWOHDR()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int updateWOHDR(String wono, String custno, String origin,
			String issuedate, String orddate, String updby, String upddate)
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
			sql = "Update WOHDR set " + "',CUSTNO= '" + custno + "',ORDDATE= '"
					+ orddate + "',ISSUEDATE= '" + issuedate + "',USERFLD1= '"
					+ origin + "' ,UPAT = '" + upddate + "',UPBY= '" + updby
					+ "' where WONO = " + wono;
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			DbBean.writeError("WOHDR", "updateWOHDR()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	/**
	 * Select FunctionWOHDR
	 * 
	 * 
	 */
	public void selectWOHDR() throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from WOHDR where PLANT = '" + sPLANT
					+ "' and WONO = '" + sWONO + "'";
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sCUSTNO = rs.getString("CUSTNO");
				sORDDATE = rs.getString("ORDDATE");
				sISSUEDATE = rs.getString("ISSUEDATE");
				sSHIPMOD = rs.getString("SHIPMOD");
				sSHIPVIA = rs.getString("SHIPVIA");
				sTERMS = rs.getString("TERMS");
				sPOTYPE = rs.getString("WOTYPE");
				sPOSTAT = rs.getString("WOSTAT");
				fTOTQTY = rs.getFloat("TOTQTY");
				fTOTWGT = rs.getFloat("TOTWGT");
				sCOMMENT1 = rs.getString("COMMENT1");
				sCOMMENT2 = rs.getString("COMMENT2");
				sLBLGROUP = rs.getString("LBLGROUP");
				iLBLCAT = rs.getInt("LBLCAT");
				iLBLTYPE = rs.getInt("LBLTYPE");
				sORDTRK1 = rs.getString("ORDTRK1");
				sORDTRK2 = rs.getString("ORDTRK2");
				iPRIORTY = rs.getInt("PRIORTY");
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
			DbBean.writeError("WOHDR", "selectWOHDR()", e);
		} finally {
			DbBean.closeConnection(con);
		}
	}

	/**
	 * Delete Function -- WOHDR
	 * 
	 * 
	 */
	public int deleteWOHDR() throws Exception {
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
			sql = "delete from WOHDR where";
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			DbBean.writeError("WOHDR", "deleteWOHDR()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int countWOHDR(String aWONO) throws Exception {
		int res = 0;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) from WOHDR where PLANT= 'PLNT' and WONO= '"
					+ aWONO + "'";
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				res = rs.getInt(1);
			}
		} catch (Exception e) {
			DbBean.writeError("WOHDR", "countWOHDR()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return res;
	}

	public String getCustomerCode(String aWONO) throws Exception {
		String q = "";
		String custCode = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select custno from WOHDR where PLANT= 'PLNT' and WONO= '"
					+ aWONO + "'";
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				custCode = rs.getString("CUSTNO");
			}
		} catch (Exception e) {
			DbBean.writeError("ITEMMST", "selectITEMMST()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return custCode;
	}

	public String getDateRequired(String aWONO) throws Exception {
		String q = "";
		String ordDate = "";
		Connection con = null;
		ResultSet rs = null;
		try {
			con = getDBConnection();
			Statement stmt = con.createStatement();
			q = "select ORDDATE from WOHDR where PLANT= 'PLNT' and WONO= '"
					+ aWONO + "'";
			rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				ordDate = rs.getString("ORDDATE");
			}
		} catch (Exception e) {
			DbBean.writeError("POHDR", "getDateRequired()", e);
		} finally {
			rs.close();
			DbBean.closeConnection(con);
		}
		return ordDate;
	}

	public String listWO(String aWONO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = null;
		ResultSet rs = null;
		CUSTMST cm = new CUSTMST();
		try {
			con = getDBConnection();
			Statement stmt = con.createStatement();
			where = (aWONO.equalsIgnoreCase("")) ? "" : " WHERE WONO LIKE '%"
					+ aWONO + "%'";
			q = "select sono,custno,orddate,deldate from WOHDR" + where;
			rs = stmt.executeQuery(q);
			while (rs.next()) {
				String sono = rs.getString("WONO").trim();
				String custno = rs.getString("CUSTNO").trim();
				String orddate = sb.formatHTML(gn.getDB2UserDate(rs.getString(
						"ORDDATE").trim()));
				String ddate = sb.formatHTML(gn.getDB2UserDate(rs.getString(
						"ISSUEDATE").trim()));

				link = "<a href=\"javascript:window.opener.form.WONO.value="
						+ "'" + sono + "';"
						+ "window.opener.form.CUSTNO.value=" + "'" + custno
						+ "';" + "window.opener.form.ORDDATE.value=" + "'"
						+ orddate + "';";
				link += "window.opener.form.DODATE.value=" + "'" + ddate
						+ "';window.close();\">";

				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">" + link
						+ "<FONT COLOR=\"blue\">" + sono + "</a></font>"
						+ "</td>" + "<td width=\"40%\">"
						+ cm.getCustDesc(custno) + "</td>"
						+ "<td width=\"30%\">" + orddate + "</td></tr>";

			}
		} catch (Exception e) {
			DbBean.writeError("WODET", "listWODET1()", e);
		} finally {
			rs.close();
			cm = null;
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String listWO() throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = null;
		ResultSet rs = null;
		CUSTMST cm = new CUSTMST();
		try {
			con = getDBConnection();
			Statement stmt = con.createStatement();
			// q =
			// "select wono,orddate from WOHDR where (select count(*) from wodet where wostat not in 'C'";
			q = "SELECT WH.WONO,WH.ORDDATE FROM WOHDR WH WHERE (SELECT COUNT(*) FROM WODET WD WHERE WD.WONO=WH.WONO AND (WD.QTYOR-WD.QTYRC)>0)>0";
			rs = stmt.executeQuery(q);
			while (rs.next()) {
				String wono = rs.getString("WONO").trim();
				String orddate = sb.formatHTML(gn.getDB2UserDate(rs.getString(
						"ORDDATE").trim()));

				link = "<a href=\"javascript:window.opener.form.REFORDNO.value="
						+ "'" + wono + "';";
				link += "window.close();\">";

				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">" + link
						+ "<FONT COLOR=\"blue\">" + wono + "</a></font>"
						+ "</td><td width=\"40%\">" + orddate + "</td></tr>";

			}
		} catch (Exception e) {
			DbBean.writeError("WOHDR", "listWO()", e);
		} finally {
			cm = null;
			rs.close();
			DbBean.closeConnection(con);
		}
		return result;
	}

}
