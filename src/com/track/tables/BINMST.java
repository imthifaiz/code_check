package com.track.tables;

// importing classes

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

public class BINMST {
	private boolean printQuery = MLoggerConstant.BINMST_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.BINMST_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	String sPLANT = "SIS";
	String sBINNO = "";
	String sWHID = "";
	String sBINDESC = "";
	String sBAY = "";
	String sROW = "";
	String sAISLE = "";
	String sTIER = "";
	String sLENGTH = "";
	String sWIDTH = "";
	String sHEIGHT = "";
	String sUOM = "";
	String sVOLUME = "";
	String sAREA = "";
	String sMAXBYWGT = "";
	String sMAXBYQTY = "";
	String sMAXBYVOL = "";
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

	public String getBINNO() {
		return sBINNO;
	}

	public String getWHID() {
		return sWHID;
	}

	public String getBINDESC() {
		return sBINDESC;
	}

	public String getBAY() {
		return sBAY;
	}

	public String getROW() {
		return sROW;
	}

	public String getAISLE() {
		return sAISLE;
	}

	public String getTIER() {
		return sTIER;
	}

	public String getLENGTH() {
		return sLENGTH;
	}

	public String getWIDTH() {
		return sWIDTH;
	}

	public String getHEIGHT() {
		return sHEIGHT;
	}

	public String getUOM() {
		return sUOM;
	}

	public String getVOLUME() {
		return sVOLUME;
	}

	public String getAREA() {
		return sAREA;
	}

	public String getMAXBYWGT() {
		return sMAXBYWGT;
	}

	public String getMAXBYQTY() {
		return sMAXBYQTY;
	}

	public String getMAXBYVOL() {
		return sMAXBYVOL;
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

	public void setBINNO(String aBINNO) {
		sBINNO = aBINNO;
	}

	public void setWHID(String aWHID) {
		sWHID = aWHID;
	}

	public void setBINDESC(String aBINDESC) {
		sBINDESC = aBINDESC;
	}

	public void setBAY(String aBAY) {
		sBAY = aBAY;
	}

	public void setROW(String aROW) {
		sROW = aROW;
	}

	public void setAISLE(String aAISLE) {
		sAISLE = aAISLE;
	}

	public void setTIER(String aTIER) {
		sTIER = aTIER;
	}

	public void setLENGTH(String aLENGTH) {
		sLENGTH = aLENGTH;
	}

	public void setWIDTH(String aWIDTH) {
		sWIDTH = aWIDTH;
	}

	public void setHEIGHT(String aHEIGHT) {
		sHEIGHT = aHEIGHT;
	}

	public void setUOM(String aUOM) {
		sUOM = aUOM;
	}

	public void setVOLUME(String aVOLUME) {
		sVOLUME = aVOLUME;
	}

	public void setAREA(String aAREA) {
		sAREA = aAREA;
	}

	public void setMAXBYWGT(String aMAXBYWGT) {
		sMAXBYWGT = aMAXBYWGT;
	}

	public void setMAXBYQTY(String aMAXBYQTY) {
		sMAXBYQTY = aMAXBYQTY;
	}

	public void setMAXBYVOL(String aMAXBYVOL) {
		sMAXBYVOL = aMAXBYVOL;
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
	 * constructor method BINMST sb - SQL Bean gn - Utilites
	 */
	public BINMST() throws Exception {
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
	 * Insert Command - BINMST
	 * 
	 * 
	 */
	public int insertBINMST() throws Exception {
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
			sql = "Insert into BINMST values ('" + sPLANT + "','" + sWHID
					+ "','" + sBINNO + "','" + sBINDESC + "','" + sBAY + "','"
					+ sROW + "','" + sAISLE + "','" + sTIER + "','" + sLENGTH
					+ "','" + sWIDTH + "','" + sHEIGHT + "','" + sUOM + "','"
					+ sVOLUME + "','" + sAREA + "','" + sMAXBYWGT + "','"
					+ sMAXBYQTY + "','" + sMAXBYVOL + "','" + sCRAT + "','"
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
	 * Update Function - BINMST
	 * 
	 * 
	 */
	public int updateBINMST() throws Exception {
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
			sql = "Update BINMST set BINDESC= '" + sBINDESC + "',BAY= '" + sBAY
					+ "',ROW= '" + sROW + "',AISLE= '" + sAISLE + "',TIER= '"
					+ sTIER + "',LENGTH= '" + sLENGTH + "',WIDTH= '" + sWIDTH
					+ "',HEIGHT= '" + sHEIGHT + "',UOM= '" + sUOM
					+ "',VOLUME= '" + sVOLUME + "',AREA= '" + sAREA
					+ "',MAXBYWGT= '" + sMAXBYWGT + "',MAXBYQTY= '" + sMAXBYQTY
					+ "',MAXBYVOL= '" + sMAXBYVOL + "',CRAT = '" + sCRAT
					+ "',CRBY= '" + sCRBY + "',UPAT = '" + sUPAT + "',UPBY= '"
					+ sUPBY + "',RECSTAT= '" + sRECSTAT + "',USERFLD1= '"
					+ sUSERFLD1 + "',USERFLD2= '" + sUSERFLD2 + "',USERFLD3= '"
					+ sUSERFLD3 + "',USERFLD4= '" + sUSERFLD4 + "',USERFLD5= '"
					+ sUSERFLD5 + "',USERFLD6= '" + sUSERFLD6 + "',USERFLG1= '"
					+ sUSERFLG1 + "',USERFLG2= '" + sUSERFLG2 + "',USERFLG3= '"
					+ sUSERFLG3 + "',USERFLG4= '" + sUSERFLG4 + "',USERFLG5= '"
					+ sUSERFLG5 + "',USERFLG6= '" + sUSERFLG6
					+ "',USERTIME1 = '" + sUSERTIME1 + "',USERTIME2 = '"
					+ sUSERTIME2 + "',USERTIME3 = '" + sUSERTIME3
					+ "',USERDBL1 = " + fUSERDBL1 + ",USERDBL2 = " + fUSERDBL2
					+ ",USERDBL3 = " + fUSERDBL3 + ",USERDBL4 = " + fUSERDBL4
					+ ",USERDBL5 = " + fUSERDBL5 + ",USERDBL6 = " + fUSERDBL6
					+ " where PLANT= '" + sPLANT + "' and BINNO= '" + sBINNO
					+ "' and WHID= '" + sWHID + "'";
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
	 * Select FunctionBINMST
	 * 
	 * 
	 */
	public void selectBINMST() throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from BINMST where PLANT= '" + sPLANT
					+ "' and BINNO= '" + sBINNO + "' and WHID= '" + sWHID + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sBINDESC = rs.getString("BINDESC");
				sBAY = rs.getString("BAY");
				sROW = rs.getString("ROW");
				sAISLE = rs.getString("AISLE");
				sTIER = rs.getString("TIER");
				sLENGTH = rs.getString("LENGTH");
				sWIDTH = rs.getString("WIDTH");
				sHEIGHT = rs.getString("HEIGHT");
				sUOM = rs.getString("UOM");
				sVOLUME = rs.getString("VOLUME");
				sAREA = rs.getString("AREA");
				sMAXBYWGT = rs.getString("MAXBYWGT");
				sMAXBYQTY = rs.getString("MAXBYQTY");
				sMAXBYVOL = rs.getString("MAXBYVOL");
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
			DbBean.writeError("BINMST", "selectBINMST()", e);
		} finally {
			DbBean.closeConnection(con);
		}
	}

	public boolean isAvaliable(String aBIN) throws Exception {
		boolean result = false;
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select count(*) from BINMST where PLANT= 'SIS' and BINNO= '"
					+ aBIN + "'";
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

	/**
	 * Delete Function -- BINMST
	 * 
	 * 
	 */
	public int deleteBINMST() throws Exception {
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
			sql = "delete from BINMST where PLANT= '" + sPLANT
					+ "' and BINNO= '" + sBINNO + "' and WHID= '" + sWHID + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String getCmbBINNO() throws Exception {

		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();

			String q = "select distinct BINNO from binmst";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("BINNO").trim();
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

	public String listBINMST(String aPLANT, String aBINNO, String aWHID)
			throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if ((aPLANT.length() > 0)) {
				vecWhr.addElement("PLANT = '" + aPLANT + "'");
			}
			if ((aBINNO.length() > 0)) {
				vecWhr.addElement("BINNO = '" + aBINNO + "'");
			}
			if ((aWHID.length() > 0)) {
				vecWhr.addElement("WHID = '" + aWHID + "'");
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
			// System.out.println(where);
			q = "select plant,whid,binno,bindesc from binmst " + where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String plnt = rs.getString("PLANT").trim();
				String whid = rs.getString("WHID").trim();
				String bin = rs.getString("BINNO").trim();
				link = "<a href=\"" + "binMastCH.jsp?PLANT=" + plnt + "&WHID="
						+ whid + "&BINNO=" + bin + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"50%\">" + link
						+ "<FONT COLOR=\"blue\">" + whid + " - " + bin
						+ "</font></a>" + "</td>" + "<td width=\"50%\">"
						+ rs.getString("BINDESC").trim() + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
		return result;
	}

	public String listAllBINMST(String aBINNO) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			/*
			 * where PLANT= '" + sPLANT + "' and CLASS= '" + sCLASS + "'";
			 */
			q = "select plant,whid,binno,bindesc from binmst where binno like '"
					+ aBINNO + "%'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String plnt = rs.getString("PLANT").trim();
				String whid = rs.getString("WHID").trim();
				String bin = rs.getString("BINNO").trim();
				link = "<a href=\"" + "binMastCH.jsp?PLANT=" + plnt + "&WHID="
						+ whid + "&BINNO=" + bin + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"50%\">" + link
						+ "<FONT COLOR=\"blue\">" + bin + "</FONT></a>"
						+ "</td>" + "<td width=\"50%\">"
						+ rs.getString("BINDESC").trim() + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public boolean GenerateBin(String WH, String Bin, int SRow, int FRow,
			int SBin, int DBin, String Prefix, String Suffix) throws Exception {
		boolean res = false;
		for (int m = SRow; m <= FRow; m++) {
			for (int i = SBin; i <= DBin; i++) {
				sWHID = WH;
				NumberFormat formatter = new DecimalFormat("00");
				String s1 = formatter.format(i);
				String s2 = formatter.format(m);
				sBINNO = Prefix + Bin + s2 + "-" + s1;
				sBINDESC = "BIN - " + Bin + " - AISLE - " + s2 + "-"
						+ "TIER - " + s1;
				insertBINMST();
			}
		}
		res = true;
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