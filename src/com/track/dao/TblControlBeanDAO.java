package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class TblControlBeanDAO extends BaseDAO {

	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.TBLCONTROLBEANDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.TBLCONTROLBEANDAO_PRINTPLANTMASTERLOG;

	public boolean isPrintQuery() {
		return printQuery;
	}

	public void setPrintQuery(boolean printQuery) {
		this.printQuery = printQuery;
	}

	public boolean isPrintLog() {
		return printLog;
	}

	public void setPrintLog(boolean printLog) {
		this.printLog = printLog;
	}

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	private String _tblName = "TBLCONTROL";

	/**
	 * method : insertIntoInvsmt(Hashtable ht) description : Insert the new
	 * record into Inventory master(INVMST)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean insertIntoTblControl(Hashtable ht) throws Exception {
		boolean insertTblControl = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key.toUpperCase() + ",";
				VALUES += "'" + value.toUpperCase() + "',";
			}

			String stmt = "INSERT INTO " + _tblName + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertTblControl = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return insertTblControl;
	}

	/**
	 * @method : getNextSeqNo(String aFunc)
	 * @description : get next sequence number for the given function name
	 * @param aFunc
	 * @return
	 * @throws Exception
	 */
	public String getNextSeqNo(String aFunc) throws Exception {
		String nextSeqno = "";
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		Connection con1 = null;
		Connection con2 = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con1 = DbBean.getConnection();
			con2 = DbBean.getConnection();

			String sQry2Update = "UPDATE " + _tblName
					+ " SET NXTSEQ = NXTSEQ+1 WHERE FUNC = '" + aFunc + "'";
			this.mLogger.query(this.printQuery, sQry2Update);
			ps1 = con1.prepareStatement(sQry2Update);
			int i = ps1.executeUpdate();
			if (i <= 0)
				return "";

			String sQry2 = "SELECT NXTSEQ FROM " + _tblName + " WHERE FUNC = '"
					+ aFunc + "'";
			this.mLogger.query(this.printQuery, sQry2);
			ps2 = con2.prepareStatement(sQry2);
			rs = ps2.executeQuery();
			while (rs.next()) {
				nextSeqno = StrUtils.fString((String) rs.getString(1));
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con1, ps1);
			DbBean.closeConnection(con2, ps2);
		}
		return nextSeqno;
	}

	public ArrayList getTblControlDetails(String aFunc) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT FUNC,DESCRIPTION,PREFIX,MINSEQ,MAXSEQ,NXTSEQ FROM TBLCONTROL WHERE FUNC = '"
					+ aFunc.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, StrUtils.fString((String) rs.getString(1)));
				arrCust.add(1, StrUtils.fString((String) rs.getString(2)));
				arrCust.add(2, StrUtils.fString((String) rs.getString(3)));
				arrCust.add(3, StrUtils.fString((String) rs.getString(4)));
				arrCust.add(4, StrUtils.fString((String) rs.getString(5)));
				arrCust.add(5, StrUtils.fString((String) rs.getString(6)));
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;

	}

	/**
	 * @method :
	 * @param aDesc
	 * @return
	 * @throws Exception
	 */
	public ArrayList getTblControlList(String aFunc) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT FUNC,DESCRIPTION FROM TBLCONTROL WHERE FUNC LIKE '%"
					+ aFunc.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString((String) rs.getString(1)));
				arrLine.add(1, StrUtils.fString((String) rs.getString(2)));
				arrList.add(arrLine);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;
	}

	/**
	 * 
	 * @param htUpdate
	 * @param htCondition
	 * @return
	 * @throws Exception
	 */
	public boolean updateTblControl(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {

		boolean update = false;
		PreparedStatement ps = null;

		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = StrUtils
						.fString((String) enumUpdate.nextElement());
				String value = StrUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "',";
			}

			// generate the update string
			Enumeration enumCondition = htUpdate.keys();
			for (int i = 0; i < htCondition.size(); i++) {
				String key = StrUtils.fString((String) enumCondition
						.nextElement());
				String value = StrUtils.fString((String) htCondition.get(key));
				sCondition += key + " = '" + value + "' AND ";
			}
			sUpdate = (sUpdate.length() > 0) ? " SET "
					+ sUpdate.substring(0, sUpdate.length() - 1) : "";
			sCondition = (sCondition.length() > 0) ? " WHERE  "
					+ sCondition.substring(0, sCondition.length() - 4) : "";
			String stmt = "UPDATE " + _tblName + sUpdate + sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				update = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return update;
	}

	/**
	 * 
	 * @param aFunc
	 * @return
	 * @throws Exception
	 */
	public boolean deleteTblControl(String aFunc) throws Exception {
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + _tblName + " WHERE FUNC ="
					+ aFunc.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				deleteItemMst = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return deleteItemMst;
	}

	public boolean isExistInTblControl(String aFunc) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM TBLCONTROL WHERE FUNC = '"
					+ aFunc + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					exists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return exists;

	}
}