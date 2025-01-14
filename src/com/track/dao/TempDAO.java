package com.track.dao;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;


public class TempDAO extends BaseDAO {
	
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.TempDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.TempDAO_PRINTPLANTMASTERLOG;
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

	//public static String plant = "";
	//public static String TABLE_NAME = "RANDOM_SCAN_TEMP";
	
	
	
//Start code by Bruhan for ouboundorder pick issue(random) on 8 Apr 2013 
public boolean isExisittemp(Hashtable ht) throws Exception {
        this.mLogger.log(1, this.getClass() + " isExisit()");
        boolean flag = false;
        java.sql.Connection con = null;
        try {
                con = com.track.gates.DbBean.getConnection();
                StringBuffer sql = new StringBuffer(" SELECT ");
                sql.append("COUNT(*) ");
                sql.append(" ");
                sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "RANDOM_SCAN_TEMP" + "]");
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
	 
public boolean updatetemptable(String query, Hashtable htCondition,
				String extCond) throws Exception {
			boolean flag = false;
			String TABLE = "RANDOM_SCAN_TEMP";
			java.sql.Connection con = null;
			try {

				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" UPDATE " + "["
						+ htCondition.get("PLANT") + "_" + TABLE + "]");
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
				throw e;
			} finally {
				DbBean.closeConnection(con);
			}
			return flag;

		}
	 
public boolean inserttemp(Hashtable ht) throws Exception {
			boolean insertFlag = false;
			java.sql.Connection conn = null;
			try {
				String TABLE = "RANDOM_SCAN_TEMP";
				conn = DbBean.getConnection();
				String FIELDS = "", VALUES = "";
				Enumeration enum1 = ht.keys();
				for (int i = 0; i < ht.size(); i++) {
					String key = _StrUtils.fString((String) enum1.nextElement());
					String value = _StrUtils.fString((String) ht.get(key));
					FIELDS += key + ",";
					VALUES += "'" + value + "',";
				}
				String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_" + TABLE
						+ "]" + "(" + FIELDS.substring(0, FIELDS.length() - 1)
						+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1)
						+ ")";

				this.mLogger.query(this.printQuery, query);

				insertFlag = insertData(conn, query);
				//TABLE_NAME = "RANDOM_SCAN_TEMP";
			} catch (Exception e) {

				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (conn != null) {
					DbBean.closeConnection(conn);
				}
			}
			return insertFlag;
		}
	 
public ArrayList selecttemp(String query,Hashtable ht,String extCond)
		throws Exception {
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;

	StringBuffer sql = new StringBuffer(" SELECT"+query+"from " + "["
			+ ht.get("PLANT") + "_" + "RANDOM_SCAN_TEMP" + "] a ");
	String conditon = "";

	try {
		con = com.track.gates.DbBean.getConnection();
		if (ht.size() > 0) {
			sql.append(" WHERE ");
			conditon = formCondition(ht);
			sql.append(conditon);
			}
		if (extCond.length() != 0) {
			sql.append(extCond);
		}
		
		this.mLogger.query(this.printQuery, sql.toString());

		alData = selectData(con, sql.toString());
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return alData;

}
	 
public boolean deletetemp(Hashtable ht) throws Exception {
			boolean delete = false;
			java.sql.Connection con = null;
			try {
				con = DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" DELETE ");
				sql.append(" ");
				sql.append(" FROM " + "[" + ht.get("PLANT") + "_RANDOM_SCAN_TEMP]");
				sql.append(" WHERE " + formCondition(ht));
				this.setmLogger(mLogger);
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

	 
//End code by Bruhan for ouboundorder pick issue(random) on 8 Apr 2013



}
