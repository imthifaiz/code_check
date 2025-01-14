package com.track.dao;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.jms.Session;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class MovHisDAO extends BaseDAO {

	public static String TABLE_NAME = "MOVHIS";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.MOVHISDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.MOVHISDAO_PRINTPLANTMASTERLOG;

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

	public MovHisDAO() {
//		StrUtils _StrUtils = new StrUtils();
	}

	public MovHisDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "MOVHIS" + "]";
	}
	
	

	public boolean insertData(String sql) throws Exception {
		boolean flag = true;

		java.sql.Connection con = null;

		try {
			con = DbBean.getConnection();

			this.mLogger.query(this.printQuery, sql.toString());
			flag = insertData(con, sql.toString());
		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}
	
	
	public boolean insertIntoMovHis(Hashtable ht) throws Exception {
		boolean insertRecvHis = false;
		java.sql.Connection con = null;
		try {

			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());

				String value = StrUtils.fString(String.valueOf(ht.get(key)));
				FIELDS += key.toUpperCase() + ",";
				VALUES += "'" + value.toUpperCase() + "',";
			}
			String sql = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "MOVHIS" + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, sql);
			System.out.println("insertIntoMovHis"+sql);
			insertRecvHis = insertData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return insertRecvHis;
	}
	public boolean updateMovHis(String query, Hashtable htCondition, String extCond,
			String aPlant) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "[" + aPlant + "_"
					+ "MOVHIS" + "]");
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE ");

			String conditon = formCondition(htCondition);

			sql.append(conditon);

			if (extCond.length() != 0) {
				sql.append(extCond);
			}
			this.mLogger.query(this.printQuery, sql.toString());

			flag = updateData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return flag;

	}
	

	public boolean insertIntoMovHis(Hashtable ht, String plant)
			throws Exception {
		boolean insertRecvHis = false;
		java.sql.Connection con = null;
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
			String sql = "INSERT INTO " + "[" + plant + "MOVHIS" + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, sql);
			insertRecvHis = insertData(con, sql.toString());

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return insertRecvHis;
	}

	public boolean insertIntodefaultMovHis(Hashtable ht) throws Exception {
		boolean insertRecvHis = false;
		java.sql.Connection con = null;
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
			String sql = "INSERT INTO " + "[" + "MOVHIS" + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery,sql);
			insertRecvHis = insertData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return insertRecvHis;
	}

	public ArrayList selectForReport(String query, Hashtable ht)
			throws Exception {
//		boolean flag = false;
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
			this.mLogger.query(this.printQuery, sql.toString());
			al = selectData(con, sql.toString());
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

	public ArrayList selectForReport(String query, Hashtable ht, String extCond)
			throws Exception {
//		boolean flag = false;
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

			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return al;
	}
	public ArrayList selectMobileSalesForReport(String query,  String extCond)
			throws Exception {
//		boolean flag = false;
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(query);
//			String conditon = "";
			/*if (ht.size() > 0) {
				sql.append(" AND ");
				conditon = formCondition(ht);
				sql.append(" " + conditon);
			}*/

			if (extCond.length() > 0) {
				sql.append("  ");

				sql.append(" " + extCond);
			}

			this.mLogger.query(this.printQuery, sql.toString());
			al = selectData(con, sql.toString());
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
	
	public boolean isExisit(Hashtable ht, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "MOVHIS" + "]");
			sql.append(" WHERE  " + formCondition(ht));
			
			if (extCond.length() > 0) {
				sql.append(" AND ");

				sql.append(" " + extCond);
			}
			
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
}