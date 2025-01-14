package com.track.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;

public class CustomerReturnDAO extends BaseDAO {
	public static String TABLE_NAME = "INVMST";
	public String plant = "";

	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.INVMSTDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.INVMSTDAO_PRINTPLANTMASTERLOG;

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

	 public CustomerReturnDAO() {
		// TODO Auto-generated constructor stub
	}

	 public ArrayList selectCustomerReturn(String query, Hashtable ht, String extCondi)
		throws Exception {
	boolean flag = false;
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;
	StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
			+ ht.get("PLANT") + "_" + "CUSTRET_SUMMARY" + "]");
	String conditon = "";

	try {
		con = com.track.gates.DbBean.getConnection();
		if (ht.size() > 0) {
			sql.append(" WHERE ");
			conditon = formCondition(ht);
			sql.append(conditon);
		}
		if (extCondi.length() > 0)
			sql.append(" and " + extCondi);
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
	 public ArrayList CustomerReturnSummary(String query, Hashtable ht, String fromdt,String todt,String extCondi)
		throws Exception {
	boolean flag = false;
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;
	StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
			+ ht.get("PLANT") + "_" + "CUSTRET_SUMMARY" + "]");
	String conditon = "";

	try {
		con = com.track.gates.DbBean.getConnection();
		if (ht.size() > 0) {
			sql.append(" WHERE ");
			conditon = formCondition(ht);
			sql.append(conditon);
		}
		if(fromdt.length()>0)
		{
			sql.append(" and " + " convert(datetime,(SUBSTRING(CRAT,7,2) + '/' + SUBSTRING(CRAT,5,2) + '/' + SUBSTRING(CRAT,1,4)), 103) >= convert(datetime,'"+fromdt+"', 103)");
		}
		if(todt.length()>0)
		{
			sql.append(" and " + " convert(datetime,(SUBSTRING(CRAT,7,2) + '/' + SUBSTRING(CRAT,5,2) + '/' + SUBSTRING(CRAT,1,4)), 103) <= convert(datetime,'"+todt+"', 103)");
		}	
		
		if (extCondi.length() > 0)
			sql.append(" and " + extCondi);
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
	 
	 public ArrayList selectItemDetails(String query, Hashtable ht,
				String extCond) throws Exception {

			ArrayList alData = new ArrayList();

			java.sql.Connection con = null;

		
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ ht.get("PLANT") + "_" + "ITEMMST" + "]");
			
			String conditon = "";
			

			try {
				con = com.track.gates.DbBean.getConnection();

				if (ht.size() > 0) {

					sql.append(" WHERE ");

					conditon = "plant='" + ht.get("PLANT") + "' and  item  like '"
							+ ht.get("ITEM") + "%' " +
									" and item in (select item from " + "["
									+ ht.get("PLANT") + "_" + "SHIPHIS" + "]" 
									+ " where status='C' and dono <> 'MISC-ISSUE') ";

					sql.append(conditon);

				}
				extCond = " ORDER BY item ";
				if (extCond.length() > 0)
					sql.append(" " + extCond);
				this.mLogger.query(this.printQuery, sql.toString());

				System.out.println(sql.toString());
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
	 
	 public ArrayList selectBatchDetails(String query, Hashtable ht,
				String extCond) throws Exception {

			ArrayList alData = new ArrayList();

			java.sql.Connection con = null;

			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ ht.get("PLANT") + "_" + "SHIPHIS" + "]");
			
			String conditon = "";

			try {
				con = com.track.gates.DbBean.getConnection();

				if (ht.size() > 0) {

					sql.append(" WHERE ");

					conditon = "plant='" + ht.get("PLANT") + "' and item  = '"
							+ ht.get("ITEM") + "' and batch like '"+ ht.get("BATCH") + "%' and dono = '"+ ht.get("DONO") + "' and status='C' ";

					sql.append(conditon);

				}
				extCond = "";
				if (extCond.length() > 0)
					sql.append(" " + extCond);
				this.mLogger.query(this.printQuery, sql.toString());
                System.out.println(sql.toString());
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
	 
	 
	 public ArrayList selectOrderNoDetails(String query, Hashtable ht,
				String extCond) throws Exception {

			ArrayList alData = new ArrayList();

			java.sql.Connection con = null;

			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ ht.get("PLANT") + "_" + "SHIPHIS" + "]");
			
			String conditon = "";

			try {
				con = com.track.gates.DbBean.getConnection();

				if (ht.size() > 0) {

					sql.append(" WHERE ");

					conditon = "plant='" + ht.get("PLANT") + "' and item  = '"
							+ ht.get("ITEM") + "%' and Batch like '"+ ht.get("BATCH") + "' and DONO = '"+ ht.get("ORDERNO") + "%' and status='C' and expirydat is not null ";

					sql.append(conditon);

				}
				extCond = "";
				if (extCond.length() > 0)
					sql.append(" " + extCond);
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
	 
	 public ArrayList selectReturnCustomerDetails(String query, Hashtable ht,
				String extCond) throws Exception {
			ArrayList alData = new ArrayList();
			java.sql.Connection con = null;

			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ ht.get("PLANT") + "_" + "DOHDR" + "]  AS dh, ["
					+ ht.get("PLANT") + "_" + "CUSTMST" + "]  AS cus "
					);
			String conditon = "";

			try {
				con = com.track.gates.DbBean.getConnection();

				if (ht.size() > 0) {

					sql.append(" WHERE ");

					conditon = " dh.DONO = '" + ht.get("DONO") + "' AND dh.CustCode = cus.CUSTNO ";

					sql.append(conditon);

				}
				if (extCond.length() > 0)
					sql.append(" and " + extCond);
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
}
