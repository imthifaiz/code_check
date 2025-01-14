package com.track.tables;

// importing classes

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.sqlBean;
import com.track.util.MLogger;

public class PLNTMST {
	private boolean printQuery = MLoggerConstant.PLNTMST_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.PLNTMST_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	String sPLANT = "";
	String sPLNTDESC = "";
	Date dCRAT;
	String sCRBY = "";
	Date dUPAT;
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
	Date dUSERTIME1;
	Date dUSERTIME2;
	Date dUSERTIME3;
	float fUSERDBL1;
	float fUSERDBL2;
	float fUSERDBL3;
	float fUSERDBL4;
	float fUSERDBL5;
	float fUSERDBL6;

	public String getPLANT() {
		return sPLANT;
	}

	public String getPLNTDESC() {
		return sPLNTDESC;
	}

	public Date getCRAT() {
		return dCRAT;
	}

	public String getCRBY() {
		return sCRBY;
	}

	public Date getUPAT() {
		return dUPAT;
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

	public Date getUSERTIME1() {
		return dUSERTIME1;
	}

	public Date getUSERTIME2() {
		return dUSERTIME2;
	}

	public Date getUSERTIME3() {
		return dUSERTIME3;
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

	public void setPLNTDESC(String aPLNTDESC) {
		sPLNTDESC = aPLNTDESC;
	}

	public void setCRAT(Date aCRAT) {
		dCRAT = aCRAT;
	}

	public void setCRBY(String aCRBY) {
		sCRBY = aCRBY;
	}

	public void setUPAT(Date aUPAT) {
		dUPAT = aUPAT;
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

	public void setUSERTIME1(Date aUSERTIME1) {
		dUSERTIME1 = aUSERTIME1;
	}

	public void setUSERTIME2(Date aUSERTIME2) {
		dUSERTIME2 = aUSERTIME2;
	}

	public void setUSERTIME3(Date aUSERTIME3) {
		dUSERTIME3 = aUSERTIME3;
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
	 * constructor method PLNTMST sb - SQL Bean gn - Utilites
	 */
	public PLNTMST() throws Exception {
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
	 * Insert Command - PLNTMST
	 * 
	 * 
	 */
	public int insertPLNTMST() throws Exception {
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
			sql = "Insert into PLNTMST values ('" + sPLANT + "','" + sPLNTDESC
					+ "'," + dCRAT + ",'" + sCRBY + "'," + dUPAT + ",'" + sUPBY
					+ "','" + sRECSTAT + "','" + sUSERFLD1 + "','" + sUSERFLD2
					+ "','" + sUSERFLD3 + "','" + sUSERFLD4 + "','" + sUSERFLD5
					+ "','" + sUSERFLD6 + "','" + sUSERFLG1 + "','" + sUSERFLG2
					+ "','" + sUSERFLG3 + "','" + sUSERFLG4 + "','" + sUSERFLG5
					+ "','" + sUSERFLG6 + "'," + dUSERTIME1 + "," + dUSERTIME2
					+ "," + dUSERTIME3 + "," + fUSERDBL1 + "," + fUSERDBL2
					+ "," + fUSERDBL3 + "," + fUSERDBL4 + "," + fUSERDBL5 + ","
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
	 * Update Function - PLNTMST
	 * 
	 * 
	 */
	public int updatePLNTMST() throws Exception {
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
			sql = "Update PLNTMST set PLNTDESC= '" + sPLNTDESC + "',CRAT = "
					+ dCRAT + ",CRBY= '" + sCRBY + "',UPAT = " + dUPAT
					+ ",UPBY= '" + sUPBY + "',RECSTAT= '" + sRECSTAT
					+ "',USERFLD1= '" + sUSERFLD1 + "',USERFLD2= '" + sUSERFLD2
					+ "',USERFLD3= '" + sUSERFLD3 + "',USERFLD4= '" + sUSERFLD4
					+ "',USERFLD5= '" + sUSERFLD5 + "',USERFLD6= '" + sUSERFLD6
					+ "',USERFLG1= '" + sUSERFLG1 + "',USERFLG2= '" + sUSERFLG2
					+ "',USERFLG3= '" + sUSERFLG3 + "',USERFLG4= '" + sUSERFLG4
					+ "',USERFLG5= '" + sUSERFLG5 + "',USERFLG6= '" + sUSERFLG6
					+ "',USERTIME1 = " + dUSERTIME1 + ",USERTIME2 = "
					+ dUSERTIME2 + ",USERTIME3 = " + dUSERTIME3
					+ ",USERDBL1 = " + fUSERDBL1 + ",USERDBL2 = " + fUSERDBL2
					+ ",USERDBL3 = " + fUSERDBL3 + ",USERDBL4 = " + fUSERDBL4
					+ ",USERDBL5 = " + fUSERDBL5 + ",USERDBL6 = " + fUSERDBL6
					+ " where PLANT= '" + sPLANT + "'";
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
	 * Select FunctionPLNTMST
	 * 
	 * 
	 */
	public void selectPLNTMST() throws Exception {
		String q = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from PLNTMST where PLANT= '" + sPLANT + "'";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sPLNTDESC = rs.getString("PLNTDESC");
				dCRAT = rs.getDate("CRAT");
				sCRBY = rs.getString("CRBY");
				dUPAT = rs.getDate("UPAT");
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
				dUSERTIME1 = rs.getDate("USERTIME1");
				dUSERTIME2 = rs.getDate("USERTIME2");
				dUSERTIME3 = rs.getDate("USERTIME3");
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
	 * Delete Function -- PLNTMST
	 * 
	 * 
	 */
	public int deletePLNTMST() throws Exception {
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
			sql = "delete from PLNTMST where PLANT= '" + sPLANT + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public String getCmbPlant() throws Exception {

		String selectStr = "";
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();

			String q = "select distinct plant from plntmst";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("PLANT").trim();
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

	public String listPLNTMST(String aPLANT) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		Vector vecWhr = new Vector();
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			if ((aPLANT.length() > 0)) {
				vecWhr.addElement("PLANT= '" + aPLANT + "'");
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
			q = "select plant,plntdesc from plntmst " + where;
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String plnt = rs.getString("PLANT").trim();
				link = "<a href=\"" + "plantMastCH.jsp?PLANT=" + plnt + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"40%\">" + link + plnt
						+ "</a>" + "</td>" + "<td width=\"60%\">"
						+ rs.getString("PLNTDESC").trim() + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		// System.out.println(result);
		return result;
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

}