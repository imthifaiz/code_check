package com.track.dao;

import java.util.ArrayList;
import java.util.Hashtable;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;

public class AuditDAO  extends BaseDAO{
	public static String TABLE_NAME = "AUDIT";
	public static String plant = "";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.AUDITDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.AUDITDAO_PRINTPLANTMASTERLOG;
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

				public ArrayList selectForReport(String query, Hashtable ht, String extCond)
				throws Exception {
			boolean flag = false;
			ArrayList al = new ArrayList();
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();
				StringBuffer sql = new StringBuffer(query);
				String conditon = "";
			if (ht.size() > 0) {
				sql.append(" AND ");
			conditon = formCondition(ht);
			sql.append(" " + conditon);
			}
			
			if (extCond.length() > 0) {
				sql.append("  ");
			
			sql.append(" " + extCond);
				}
			
				this.mLogger.query(this.printQuery, sql.toString());
				al = selectData(con, sql.toString());
			} catch (Exception e) {
			
				this.mLogger.log(0, "" + e.getMessage());
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return al;
			}
}
