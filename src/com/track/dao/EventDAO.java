package com.track.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class EventDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.EventDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.EventDAO_PRINTPLANTMASTERLOG;

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

	public EventDAO() {

	}

	public boolean insertIntoEventMst(Hashtable ht) throws Exception {
		boolean insertedInv = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "EVENT_MST" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			insertedInv = insertData(con, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return insertedInv;
	}

	public boolean isExists(Hashtable ht) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();

			// query
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" 1 ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "EVENT_MST"
					+ "]");
			sql.append(" WHERE  " + formCondition(ht));
			this.mLogger.query(this.printQuery, sql.toString());
			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;
	}

	public boolean updateEventMst(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = StrUtils
						.fString((String) enumUpdate.nextElement());
				String value = StrUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}

			Enumeration enumCondition = htCondition.keys();
			for (int i = 0; i < htCondition.size(); i++) {
				String key = StrUtils.fString((String) enumCondition
						.nextElement());
				String value = StrUtils.fString((String) htCondition.get(key));
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";

			}
			sUpdate = (sUpdate.length() > 0) ? " SET "
					+ sUpdate.substring(0, sUpdate.length() - 1) : "";
			sCondition = (sCondition.length() > 0) ? " WHERE  "
					+ sCondition.substring(0, sCondition.length() - 4) : "";

			String stmt = "UPDATE " + "[" + htCondition.get("PLANT") + "_"
					+ "EVENT_MST" + "]" + sUpdate + sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				update = true;

		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return update;
	}

	public boolean deleteEventID(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deletePrdId()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "EVENT_MST"
					+ "]");
			sql.append(" WHERE " + formCondition(ht));

			this.mLogger.query(this.printQuery, sql.toString());
			delete = updateData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return delete;
	}

	public ArrayList getEventDetails(String event, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct EVENT_ID,ISNULL(EVENT_DESC,'')EVENT_DESC,ISNULL(TYPE,'')TYPE,ISNULL(REMARKS,'')REMARKS from "
					+ "["
					+ plant
					+ "_"
					+ "EVENT_MST] where EVENT_ID like '"
					+ event
					+ "%'   " + extraCon
					+ " ORDER BY EVENT_ID ";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}
	public Map selectRow(String query, Hashtable ht, String extCond)
			throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "EVENT_MST" + "]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
			this.mLogger.query(this.printQuery, sql.toString());

			map = getRowOfData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return map;
	}
	/*
	public String getOperationseqDesc(String aPlant, String aoprseq) throws Exception {
		String OperationseqDesc = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put(IDBConstants.OPRSEQNUM, aoprseq);

		String query = " ISNULL(OPR_SEQ_DESC,'') as OPR_SEQ_DESC ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			OperationseqDesc = (String) m.get("OPR_SEQ_DESC");
		} else {
			OperationseqDesc = "";
		}
		if (OperationseqDesc.equalsIgnoreCase(null) || OperationseqDesc.length() == 0) {
			OperationseqDesc = "";
		}
		return OperationseqDesc;
	}
	
	public Map selectRow(String query, Hashtable ht, String extCondi)
			throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + (String) ht.get("PLANT") + "_EVENT_MST]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			if (extCondi.length() > 0)
				sql.append(" and " + extCondi);
			this.mLogger.query(this.printQuery, sql.toString());
			map = getRowOfData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return map;
	}*/

}
