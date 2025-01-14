package com.track.dao;

import java.sql.Connection;
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

public class StockTakeDAO extends BaseDAO {
	public static final String TABLE_NAME = "STKTAKE";

	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.STOCKTAKEDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.STOCKTAKEDAO_PRINTPLANTMASTERLOG;

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

	public StockTakeDAO() {
		StrUtils _StrUtils = new StrUtils();
	}

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ TABLE_NAME);
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			this.mLogger.query(this.printQuery, sql.toString());

			map = getRowOfData(con, sql.toString());

		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return map;
	}

	public String getItemLoc(String aPlant, String aItem) throws Exception {
		String itemLoc = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("item", aItem);

		String query = " loc ";

		Map m = selectRow(query, ht);

		itemLoc = (String) m.get("loc");
		return itemLoc;
	}

	public ArrayList selectInvMst(String query, Hashtable ht) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
				+ TABLE_NAME);
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			this.mLogger.query(this.printQuery, sql.toString());
			alData = selectData(con, sql.toString());
		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;
	}

	public ArrayList selectInvMst(String query, Hashtable ht, String extCondi)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
				+ TABLE_NAME);
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
			MLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;
	}

	public ArrayList selectInvMstDetails(String query, Hashtable ht,
			String extCondi) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "+ht.get(IDBConstants.PLANT)+"_"
				+ TABLE_NAME);
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
			MLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;
	}

	public boolean isExisit(Hashtable<String, String> ht, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + ht.get("PLANT") + "_" + TABLE_NAME);
			sql.append(" WHERE  " + formCondition(ht));

			if (extCond.length() > 0)
				sql.append(" and " + extCond);

			this.mLogger.query(this.printQuery, sql.toString());

			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}

	public boolean isExisit(String sql) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			this.mLogger.query(this.printQuery, sql.toString());
			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}

	public boolean insertInvMst(Hashtable ht) throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _StrUtils.fString((String) enum1.nextElement());
				String value = _StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + TABLE_NAME + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}

	public boolean update(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ (String) htCondition.get("PLANT") + "_" + TABLE_NAME
					+ "] ");
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
			MLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return flag;
	}

	public boolean insertStkTake(Hashtable ht) throws Exception {
		boolean insertRecvHis = false;
		java.sql.Connection con = null;
		try {

			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _StrUtils.fString((String) enum1.nextElement());
				String value = _StrUtils.fString((String) ht.get(key));
				FIELDS += key.toUpperCase() + ",";
				VALUES += "'" + value.toUpperCase() + "',";
			}
			String sql = "INSERT INTO " + "[" + (String) ht.get("PLANT") + "_"
					+ TABLE_NAME + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1)
					+ ", WHID) VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ", 'NO')";
			this.mLogger.query(this.printQuery, sql);

			insertRecvHis = insertData(con, sql.toString());

		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return insertRecvHis;
	}

	public boolean deleteStkTake() throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + TABLE_NAME);
			this.mLogger.query(this.printQuery, sql.toString());
			delete = updateData(con, sql.toString());
		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return delete;
	}

	public boolean deleteStkTake(String plant) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + plant + "_" + TABLE_NAME);
			this.mLogger.query(this.printQuery, sql.toString());
			delete = updateData(con, sql.toString());
		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return delete;
	}

	public boolean deleteStkTakeDetail(HashMap<String, String> inputHashMap)
			throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			HashMap<String, String> inputHashMapForDelete = new HashMap<String, String>();
			if (inputHashMap.containsKey("LOC")) {
				inputHashMapForDelete.put("LOC", inputHashMap.get("LOC"));
			}
			if (inputHashMap.containsKey("BATCH")) {
				inputHashMapForDelete.put("BATCH", inputHashMap.get("BATCH"));
			}
			if (inputHashMap.containsKey("QTY")) {
				inputHashMapForDelete.put("QTY", inputHashMap.get("QTY"));
			}
			if (inputHashMap.containsKey("PLANT")) {
				inputHashMapForDelete.put("PLANT", inputHashMap.get("PLANT"));
			}
			if (inputHashMap.containsKey("ITEM")) {
				inputHashMapForDelete.put("ITEM", inputHashMap.get("ITEM"));
			}
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + inputHashMap.get("PLANT") + "_"
					+ TABLE_NAME + "]");
			if (inputHashMap.size() > 0) {
				sql.append(" WHERE " + formCondition(inputHashMapForDelete));
			}
			this.mLogger.query(this.printQuery, sql.toString());
			delete = updateData(con, sql.toString());
		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return delete;
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

	@SuppressWarnings("all")
	public ArrayList<Map<String, String>> getAllStockTakeDetails(
			HashMap<String, String> inputHashMap) throws Exception {
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		Connection con = null;
		String SQL = "SELECT ITEM, LOC, BATCH, QTY FROM ["
				+ inputHashMap.get("PLANT") + "_STKTAKE] WHERE PLANT='"
				+ inputHashMap.get("PLANT") + "'";

		try {
			con = DbBean.getConnection();
			this.mLogger.query(this.printQuery, SQL.toString());
			alData = selectData(con, SQL.toString());
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
	
	  public boolean deleteStkTake(String plant, int id)
		        throws Exception {
				boolean deleteStkTake = false;
				PreparedStatement ps = null;
				Connection con = null;
				try {
				        con = DbBean.getConnection();
				        String sQry = "DELETE FROM " + "[" + plant +"_STKTAKE]"
				                        + " WHERE ID ='"+id+"'";
				        this.mLogger.query(this.printQuery, sQry);
				        ps = con.prepareStatement(sQry);
				        int iCnt = ps.executeUpdate();
				        if (iCnt > 0) {
				        	deleteStkTake = true;
				        }
				} catch (Exception e) {
				        this.mLogger.exception(this.printLog, "", e);
				} finally {
				        DbBean.closeConnection(con, ps);
				}
				return deleteStkTake;
	 	}
}