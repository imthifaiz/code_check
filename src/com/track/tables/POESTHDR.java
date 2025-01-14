package com.track.tables;

// importing classes

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.sqlBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class POESTHDR {
	private boolean printQuery = MLoggerConstant.POESTHDR_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.POESTHDR_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	String sPLANT = "COM";
	String sPOESTNO = "";
	String sVENDNO = "";
	String sORDDATE = "";
	String sDELDATE = "";
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

	public String getPOESTNO() {
		return sPOESTNO;
	}

	public String getVENDNO() {
		return sVENDNO;
	}

	public String getORDDATE() {
		return gn.getDB2UserDate(sORDDATE);
	}

	public String getDELDATE() {
		return gn.getDB2UserDate(sDELDATE);
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

	public void setPOESTNO(String aPOESTNO) {
		sPOESTNO = aPOESTNO;
	}

	public void setVENDNO(String aVENDNO) {
		sVENDNO = aVENDNO;
	}

	public void setORDDATE(String aORDDATE) {
		sORDDATE = aORDDATE;
	}

	public void setDELDATE(String aDELDATE) {
		sDELDATE = aDELDATE;
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
	 * constructor method POESTHDR$ sb - SQL Bean gn - Utilites
	 */
	public POESTHDR() throws Exception {
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
	 * Insert Command - POESTHDR
	 * 
	 * 
	 */
	public int insertPOHDR() throws Exception {
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
			sql = "Insert into POESTHDR values ('" + sPLANT + "','" + sPOESTNO
					+ "','" + sVENDNO + "','" + sORDDATE + "','" + sDELDATE
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
	 * Update Function - POESTHDR
	 * 
	 * 
	 */
	public int updatePOHDR() throws Exception {
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
			sql = "Update POESTHDR set VENDNO= '" + sVENDNO + "',ORDDATE= '"
					+ sORDDATE + "',DELDATE= '" + sDELDATE + "',SHIPMOD= '"
					+ sSHIPMOD + "',SHIPVIA= '" + sSHIPVIA + "',TERMS= '"
					+ sTERMS + "',POTYPE= '" + sPOTYPE + "',POSTAT= '"
					+ sPOSTAT + "',TOTQTY = " + fTOTQTY + ",TOTWGT = "
					+ fTOTWGT + ",COMMENT1= '" + sCOMMENT1 + "',COMMENT2= '"
					+ sCOMMENT2 + "',LBLGROUP= '" + sLBLGROUP + "',LBLCAT = "
					+ iLBLCAT + ",LBLTYPE = " + iLBLTYPE + ",ORDTRK1= '"
					+ sORDTRK1 + "',ORDTRK2= '" + sORDTRK2 + "',PRIORTY = "
					+ iPRIORTY + ",CRAT = '" + sCRAT + "',CRBY= '" + sCRBY
					+ "',UPAT = '" + sUPAT + "',UPBY= '" + sUPBY
					+ "',RECSTAT= '" + sRECSTAT + "',USERFLD1= '" + sUSERFLD1
					+ "',USERFLD2= '" + sUSERFLD2 + "',USERFLD3= '" + sUSERFLD3
					+ "',USERFLD4= '" + sUSERFLD4 + "',USERFLD5= '" + sUSERFLD5
					+ "',USERFLD6= '" + sUSERFLD6 + "',USERFLG1= '" + sUSERFLG1
					+ "',USERFLG2= '" + sUSERFLG2 + "',USERFLG3= '" + sUSERFLG3
					+ "',USERFLG4= '" + sUSERFLG4 + "',USERFLG5= '" + sUSERFLG5
					+ "',USERFLG6= '" + sUSERFLG6 + "',USERTIME1 = '"
					+ sUSERTIME1 + "',USERTIME2 =' " + sUSERTIME2
					+ "',USERTIME3 = '" + sUSERTIME3 + "',USERDBL1 = "
					+ fUSERDBL1 + ",USERDBL2 = " + fUSERDBL2 + ",USERDBL3 = "
					+ fUSERDBL3 + ",USERDBL4 = " + fUSERDBL4 + ",USERDBL5 = "
					+ fUSERDBL5 + ",USERDBL6 = " + fUSERDBL6
					+ " where PLANT= '" + sPLANT + "' and POESTNO= '" + sPOESTNO
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
	 * Select FunctionPOHDR
	 * 
	 * 
	 */
	public void selectPOHDR() throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from POESTHDR where PLANT= '" + IConstants.PLANT_VAL
					+ "' and POESTNO= '" + sPOESTNO + "'";

			this.mLogger.query(this.printQuery, q);

			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sVENDNO = rs.getString("VENDNO");
				sORDDATE = rs.getString("ORDDATE");
				sDELDATE = rs.getString("DELDATE");
				sSHIPMOD = rs.getString("SHIPMOD");
				sSHIPVIA = rs.getString("SHIPVIA");
				sTERMS = rs.getString("TERMS");
				sPOTYPE = rs.getString("POTYPE");
				sPOSTAT = rs.getString("POSTAT");
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
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
	}

	public int countPOESTHDR(String aPOESTNO) throws Exception {

		int res = 0;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) from POESTHDR where PLANT= '"
					+ IConstants.PLANT_VAL + "' and POESTNO= '" + aPOESTNO + "'";

			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				res = rs.getInt(1);
			}
		} catch (Exception e) {
			DbBean.writeError("POESTHDR", "countPOESTHDR()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return res;
	}

	/**
	 * Delete Function -- POESTHDR
	 * 
	 * 
	 */
	public int deletePOHDR(String aPOESTNO) throws Exception {
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
			sql = "delete from POESTHDR where PLANT= 'COM' and POESTNO= '" + aPOESTNO
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

	public int deletePOHDRML(String aPOESTNO, String plant) throws Exception {
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
			sql = "delete from " + "[" + plant + "_" + "POESTHDR" + "]"
					+ " where PLANT= 'COM' and POESTNO= '" + aPOESTNO + "'";
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
			sVENDNO = vecStr.elementAt(4).toString();
			sCRBY = vecStr.elementAt(80).toString();
			sORDDATE = gn.parseDate(vecStr.elementAt(6).toString());
			sDELDATE = gn.parseDate(vecStr.elementAt(79).toString());
			insertPOHDR();
		}

	}

	public String listPOHDR(String aPOESTNO, String aVENDNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if ((aPOESTNO.length() > 0)) {
				vecWhr.addElement("POESTNO like '" + aPOESTNO + "%'");
			}
			if ((aVENDNO.length() > 0)) {
				vecWhr.addElement("VENDNO like '" + aVENDNO + "%'");
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
			q = "select POESTNO,vendno,orddate,crby,deldate from POESTHDR " + where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String POESTNO = rs.getString("POESTNO").trim();
				link = "<a href=\"" + "recvDet.jsp?POESTNO=" + POESTNO + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">"
						+ link
						+ "<FONT COLOR=\"blue\">"
						+ POESTNO
						+ "</font></a>"
						+ "</td>"
						+ "<td width=\"20%\">"
						+ sb.formatHTML(rs.getString("VENDNO").trim())
						+ "</td>"
						+ "<td width=\"20%\">"
						+ sb.formatHTML(gn.getDB2UserDate(rs.getString(
								"ORDDATE").trim()))
						+ "</td>"
						+ "<td width=\"20%\">"
						+ sb.formatHTML(rs.getString("CRBY").trim())
						+ "</td>"
						+ "<td width=\"20%\">"
						+ sb.formatHTML(gn.getDB2UserDate(rs.getString(
								"DELDATE").trim())) + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// //System.out.println(result);
		return result;
	}

	public int processPO(String aPOESTNO) throws Exception {
		String sql1, sql2;
		Connection con = getDBConnection();
		int returnCode = 1;
		int ret1 = 1;
		int ret2 = 1;
		con.setAutoCommit(false);
		ResultSet rs = null;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql1 = "INSERT POESTHDR SELECT * FROM POESTHDR  WHERE POESTNO = '" + aPOESTNO
					+ "'";
			sql2 = "INSERT POESTDET SELECT * FROM POESTDET  WHERE POESTNO = '" + aPOESTNO
					+ "'";
			sb.setmLogger(mLogger);
			ret1 = sb.insertRecords(sql1);
			ret2 = sb.insertRecords(sql2);
			if ((ret1 == 1) == (ret2 == 1)) {
				con.commit();
			} else {
				con.rollback();
				returnCode = 0;
			}

		} catch (Exception e) {
			con.rollback();
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			con.setAutoCommit(true);
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public boolean isAvaliable(String aPOESTNO) throws Exception {
		boolean result = false;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) from POESTHDR where PLANT= 'COM' and POESTNO '"
					+ aPOESTNO + "'";
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

	public String getVendorCode(String aPOESTNO) throws Exception {
		String q = "";
		String vendCode = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select vendno from POESTHDR where PLANT= 'COM' and POESTNO= '"
					+ aPOESTNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				vendCode = rs.getString("VENDNO");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return vendCode;
	}

	public String getDateRequired(String aPOESTNO) throws Exception {
		String q = "";
		String delDate = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select DELDATE from POESTHDR where PLANT= 'COM' and POESTNO= '"
					+ aPOESTNO + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				delDate = rs.getString("DELDATE");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return delDate;
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
}