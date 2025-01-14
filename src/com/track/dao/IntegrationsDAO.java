package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class IntegrationsDAO extends BaseDAO {
	private String TABLE_NAME = "[SHOPIFY_CONFIG]";
	private String SHOPEE_TABLE_NAME = IDBConstants.SHOPEE_CONFIG_TABLE;
	
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.INTEGRATIONSDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.INTEGRATIONSDAO_PRINTPLANTMASTERLOG;
	
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
	
	public Map getShopifyConfigDetail(String plant) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		String query="";
		try {
			query = "SELECT [ID]" + 
					"      ,[PLANT]" + 
					"      ,[API_KEY]" + 
					"      ,[API_PASSWORD]" + 
					"      ,[DOMAIN_NAME]" + 
					"      ,[LOCATION]" + 
					"      ,[WEBHOOK_KEY]" + 
					"      ,[CRAT]" + 
					"      ,[CRBY]" + 
					"      ,[UPAT]" + 
					"      ,[UPBY]" + 
					"  FROM "+TABLE_NAME+" WHERE PLANT='"+plant+"'";
			con = com.track.gates.DbBean.getConnection();
			this.mLogger.query(this.printQuery, query);
			map = getRowOfData(con, query);
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
	
	public boolean insertShopifyConfig(Hashtable ht) throws Exception {
		boolean insert = false;
		PreparedStatement ps = null;
		Connection con = null;
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
			String stmt = "INSERT INTO " + TABLE_NAME + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insert = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return insert;
	}
	
	public boolean updateShopifyConfig(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + TABLE_NAME);
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
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;
	}
	
	public boolean getShopifyConfigCount(Hashtable ht, String extCond) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;int count=0;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" FROM "+TABLE_NAME+"");
			sql.append(" WHERE  " + formCondition(ht));
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
			this.mLogger.query(this.printLog, sql.toString());
			count = countRows(con, sql.toString());
			if(count>0)
				flag = true;
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
	
	public boolean insertData(Hashtable ht, String tableName) throws Exception {
		boolean insert = false;
		PreparedStatement ps = null;
		Connection con = null;
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
			String stmt = "INSERT INTO " + tableName + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insert = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return insert;
	}
	
	public boolean getConfigCount(Hashtable ht, String tableName, String extCond) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;int count=0;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" FROM "+tableName+"");
			sql.append(" WHERE  " + formCondition(ht));
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
			this.mLogger.query(this.printLog, sql.toString());
			count = countRows(con, sql.toString());
			if(count>0)
				flag = true;
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
	
	public boolean updateConfig(String query, String tableName, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + tableName);
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
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;
	}
	
	public Map<String, String> getShopeeConfigDetail(String plant) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		java.sql.Connection con = null;
		String query="";
		try {
			query = "SELECT [ID]" + 
					"      ,[PLANT]" + 
					"      ,[SHOP_ID]" + 
					"      ,[PARTNER_ID]" + 
					"      ,[ENVIRONMENT]" + 
					"      ,[API_KEY]" + 
					"      ,[CRAT]" + 
					"      ,[CRBY]" + 
					"      ,[UPAT]" + 
					"      ,[UPBY]" + 
					"  FROM "+SHOPEE_TABLE_NAME+" WHERE PLANT='"+plant+"'";
			con = com.track.gates.DbBean.getConnection();
			this.mLogger.query(this.printQuery, query);
			map = getRowOfData(con, query);
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
	
	public boolean getShopifyItemCount(String sku, String plant) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;int count=0;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" FROM ["+plant+"_SHOPIFY_ITEM]");
			sql.append(" WHERE SKU='"+sku+"' AND PLANT='"+plant+"'");
			this.mLogger.query(this.printLog, sql.toString());
			count = countRows(con, sql.toString());
			if(count>0)
				flag = true;
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
	
	public boolean getShopeeConfigCount(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;int count=0;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" FROM "+SHOPEE_TABLE_NAME+"");
			sql.append(" WHERE  " + formCondition(ht));
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
			this.mLogger.query(this.printLog, sql.toString());
			count = countRows(con, sql.toString());
			if(count>0)
				flag = true;
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
	
	public boolean getShopeeItemCount(String sku, String plant) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;int count=0;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" FROM ["+plant+"_SHOPEE_ITEM]");
			sql.append(" WHERE (ITEM_SKU='"+sku+"' OR VARIATION_SKU='"+sku+"') AND PLANT='"+plant+"'");
			this.mLogger.query(this.printLog, sql.toString());
			count = countRows(con, sql.toString());
			if(count>0)
				flag = true;
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
