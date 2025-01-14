package com.track.tables;

// importing classes

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.sqlBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class CYCLECNT {
	private boolean printQuery = MLoggerConstant.CYCLECNT_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.CYCLECNT_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	String sPLANT = "SIS";
	String sITEM = "";
	String sCYCLESDATE;
	String sCYCLEEDATE;
	String sUsrFlg1 = "";
	String sUsrFlg2 = "";

	public CYCLECNT() {
		try {
			sb = new sqlBean();
			gn = new Generator();
		} catch (Exception e) {
		}
	}

	public String getPLANT() {
		return sPLANT;
	}

	public String getITEM() {
		return sITEM;
	}

	public String getCYCLESDATE() {
		return sCYCLESDATE;
	}

	public String getCYCLEEDATE() {
		return sCYCLEEDATE;
	}

	public String getUsrFlg1() {
		return sUsrFlg1;
	}

	public String getUsrFlg2() {
		return sUsrFlg2;
	}

	public void setPLANT(String aPLANT) {
		sPLANT = aPLANT;
	}

	public void setITEM(String aITEM) {
		sITEM = aITEM;
	}

	public void setCYCLESDATE(String aCYCLESDATE) {
		sCYCLESDATE = aCYCLESDATE;
	}

	public void setCYCLEEDATE(String aCYCLEEDATE) {
		sCYCLEEDATE = aCYCLEEDATE;
	}

	public void setUsrFlg1(String aUsrflg1) {
		sUsrFlg1 = aUsrflg1;
	}

	public void setUsrFlg2(String aUsrflg2) {
		sUsrFlg2 = aUsrflg2;
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
	 * Insert Command - CLASSMST
	 * 
	 * 
	 */
	public int insertCYCLECNT() throws Exception {
		System.out.println("Entering insertCYCLECNT");
		String sql;
		Connection con = null;
		int returnCode = 0;
		try {
			con = DbBean.getConnection();
			sql = "Insert into CYCLECNT (PLANT,ITEM,CYCLESDATE,CYCLEEDATE) values ('"
					+ sPLANT
					+ "','"
					+ sITEM
					+ "','"
					+ sCYCLESDATE
					+ "','"
					+ sCYCLEEDATE + "')";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			try {
				DbBean.closeConnection(con);
			} catch (Exception e) {
			}
		}
		return returnCode;
	}

	/**
	 * Update Function - CLASSMST
	 * 
	 * 
	 */
	public int updateCYCLECNT() throws Exception {
		System.out.println("inside updateCYCLECNT()");
		String sql;
		Connection con = getDBConnection();
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = "Update CYCLECNT set ITEM= '" + sITEM + "',CYCLESDATE= '"
					+ sCYCLESDATE + "',CYCLEEDATE= '" + sCYCLEEDATE + "' "
					+ " where PLANT= '" + sPLANT + "' and ITEM= '" + sITEM
					+ "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			try {
				DbBean.closeConnection(con);
			} catch (Exception e) {
				DbBean.writeError("CYCLECNT", "updateCYCLECNT()", e);
			}
		}
		return returnCode;
	}

	/**
	 * Select Function CYCLECNT
	 * 
	 * 
	 */
	public void selectCYCLECNT() throws Exception {
		String q = "";
		ResultSet rs = null;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select * from CYCLECNT where PLANT= '" + sPLANT
					+ "' and ITEM= '" + sITEM + "'";
			this.mLogger.query(this.printQuery, q);
			rs = stmt.executeQuery(q);
			int n = 1;
			while (rs.next()) {
				sITEM = rs.getString("ITEM");
				sCYCLESDATE = rs.getString("CYCLESDATE").substring(0, 11);
				sCYCLEEDATE = rs.getString("CYCLEEDATE").substring(0, 11);
			}
		} catch (Exception e) {
		}

	}

	public boolean isValidItem(String item) throws Exception {
		String q = "";
		ResultSet rs = null;
		Connection con = getDBConnection();
		boolean found = false;
		try {
			Statement stmt = con.createStatement();
			q = "select * from CYCLECNT where PLANT= '" + sPLANT
					+ "' and ITEM= '" + item + "'";
			this.mLogger.query(this.printQuery, q);
			rs = stmt.executeQuery(q);
			if (rs.next()) {
				found = true;
			} else {
				found = false;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			try {
				rs = null;
				DbBean.closeConnection(con);
			} catch (Exception e) {
			}

		}

		return found;
	}

	public String getCmbATTRIB() throws Exception {
		String selectStr = "";
		ResultSet rs = null;
		Connection con = getDBConnection();
		try {

			Statement stmt = con.createStatement();
			String q = "select ATTRIB,ATTDESC from CYCLECNT where attrib <> ''";
			this.mLogger.query(this.printQuery, q);
			rs = stmt.executeQuery(q);
			if (rs != null) {
				String val, val1;
				while (rs.next()) {
					val = rs.getString("ATTRIB").trim();
					val1 = rs.getString("ATTDESC").trim();
					selectStr += "<option value='" + val + "'>" + val
							+ "</option>";
				}
			}
		} // End of try
		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			try {
				rs = null;
				DbBean.closeConnection(con);
			} catch (Exception e) {
			}

		}
		return selectStr;
	}

	public String listCYCLECNT() throws Exception {
		String q = "";
		String link = "";
		String result = "";
		Connection con = getDBConnection();
		StrUtils su = new StrUtils();
		try {
			Statement stmt = con.createStatement();
			q = "select plant,item,itemdesc,cyclesdate,cycleedate from VIEW_CYCLECNT";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				String item = rs.getString("ITEM").trim();
				;
				link = "<a href=\"" + "ccUpdate.jsp?PLANT="
						+ rs.getString("PLANT") + "&ITEM=" + item + "\">";
				result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"20%\">"
						+ link
						+ "<FONT COLOR=\"blue\">"
						+ item
						+ "</font></a>"
						+ "</td>"
						+ "<td width=\"40%\">"
						+ rs.getString("ITEMDESC")
						+ "</td>"
						+ "<td width=\"15%\">"
						+ su.getUserDate(rs.getString("CYCLESDATE").substring(
								0, 11).trim(), "-", "/")
						+ "</td>"
						+ "<td width=\"15%\">"
						+ su.getUserDate(rs.getString("CYCLEEDATE").substring(
								0, 11).trim(), "-", "/") + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public String listCYCLECNT_XML() throws Exception {
		System.out.println(" before in here");
		String q = "";
		String link = "";
		String result = "";
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			java.util.Date dt = new java.util.Date();
			SimpleDateFormat dfVisualDate = new SimpleDateFormat("MM/dd/yyyy");
			String today = dfVisualDate.format(dt);
			q = "select plant,item,itemdesc from VIEW_CYCLECNT WHERE '" + today
					+ "' between cyclesdate and cycleedate";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			result = "<?xml version='1.0' encoding='utf-8'?>";
			result += "<items>";
			while (rs.next()) {
				result += "<itemdetails itemcode=\"" + rs.getString("item")
						+ "\">";
				result += "<itemdesc>" + rs.getString("itemdesc")
						+ "</itemdesc>";
				// result += "<cycledate>" + rs.getString("cycledate") +
				// "</cycledate>";
				result += "</itemdetails>";
			}
			result += "</items>";
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public int deleteCYCLECNT() throws Exception {
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
			sql = "delete from CYCLECNT where PLANT= '" + sPLANT
					+ "' and ITEM= '" + sITEM + "'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int updateCYCLECNTFlg2() throws Exception {

		String sql;
		Connection con = null;
		int returnCode = 0;
		try {
			con = DbBean.getConnection();
			sql = "update usrflg2 = 'C'";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			try {
				DbBean.closeConnection(con);
			} catch (Exception e) {
			}
		}
		return returnCode;
	}

	public boolean canUpdateCCInvenoty() throws Exception {
		boolean flg = false;
		Connection con = getDBConnection();
		try {
			Statement stmt = con.createStatement();
			java.util.Date dt = new java.util.Date();
			SimpleDateFormat dfVisualDate = new SimpleDateFormat("MM/dd/yyyy");
			String today = dfVisualDate.format(dt);
			String q = "SELECT DISTINCT(ITEM) FROM CYCLECNT where  '" + today
					+ "' " + "between cyclesdate and cycleedate and "
					+ "(ISNULL(usrflg2,'') = '' OR ISNULL(usrflg2,'') != 'C') ";
			this.mLogger.query(this.printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				flg = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return flg;
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
}