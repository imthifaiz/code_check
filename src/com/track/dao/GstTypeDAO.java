package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class GstTypeDAO extends BaseDAO {
	private String TABLE_NAME = "COMPANY_CONFIG";
	private boolean printQuery = MLoggerConstant.GSTTYPEDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.GSTTYPEDAO_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

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

	public ArrayList select(String selectList, Hashtable ht, String extCond)
			throws Exception {
		boolean flag = false;

		Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + selectList
					+ " from " + TABLE_NAME);
			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			if (extCond.length() > 0) {
				sql.append(" ");
				sql.append(extCond);
			}
			this.mLogger.query(this.printQuery, sql.toString());
			return selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

	}

	public ArrayList getGstTypeDetails(String selectList, Hashtable ht,
			String extCond) throws Exception {
		boolean flag = false;

		Connection con = null;
		ArrayList alResult = new ArrayList();
		StringBuffer sql = new StringBuffer(" SELECT " + selectList + " from "
				+ "[" + ht.get("PLANT") + "_" + TABLE_NAME + "]");

		try {
			con = DbBean.getConnection();

			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			if (extCond.length() > 0) {
				sql.append(" ");
				sql.append(extCond);
			}
			this.mLogger.query(this.printQuery, sql.toString());
			alResult = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

		return alResult;

	}
	
	public List queryGstType(String aGstType, String plant, String cond) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List listQty = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sCondition = "WHERE GSTTYPE LIKE  '" + aGstType.toUpperCase()
					+ "%'";
			String sQry = "SELECT GSTTYPE,GSTDESC,GSTPERCENTAGE,REMARKS FROM "
					+ "[" + plant + "_" + "COMPANY_CONFIG " + "]" + sCondition + cond
					+ " ORDER BY GSTTYPE ";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Vector lineVec = new Vector();
				lineVec.add(0, StrUtils.fString((String) rs.getString("GSTTYPE")));
				lineVec.add(1, StrUtils.fString((String) rs.getString("GSTDESC")));
				lineVec.add(2, StrUtils.fString((String) rs.getString("GSTPERCENTAGE")));
				lineVec.add(3, StrUtils.fString((String) rs.getString("REMARKS")));
				listQty.add(lineVec);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQty;
	}


	public ArrayList getAllGstTypeDetails(String selectList, Hashtable ht,
			String extCond) throws Exception {
		boolean flag = false;

		Connection con = null;
		ArrayList alResult = new ArrayList();
		StringBuffer sql = new StringBuffer(" SELECT " + selectList + " from "
				+ "[" + ht.get("PLANT") + "_" + TABLE_NAME + "]");

		try {
			con = DbBean.getConnection();

			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			if (extCond.length() > 0) {
				sql.append(" ");
				sql.append(extCond);
			}
			this.mLogger.query(this.printQuery, sql.toString());
			alResult = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

		return alResult;

	}

	public ArrayList getGstTypeList(String selectList, Hashtable ht, String gsttype)
			throws Exception {
		boolean flag = false;

		Connection con = null;
		ArrayList alResult = new ArrayList();
		StringBuffer sql = new StringBuffer(" SELECT " + selectList + " from "
				+ "[" + ht.get("PLANT") + "_" + TABLE_NAME
				+ "] where gsttype like'" + gsttype + "%" + "'");

		try {
			con = DbBean.getConnection();

			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" and ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}

			this.mLogger.query(this.printQuery, sql.toString());
			alResult = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

		return alResult;

	}

	public boolean insertIntoGstType(Hashtable ht) throws Exception {
		boolean insertedCycleCount = false;

		Connection conn = null;
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ TABLE_NAME + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query.toString());

			insertedCycleCount = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(conn);
		}

		return insertedCycleCount;
	}

	public boolean updateGstType(Hashtable htUpdate, Hashtable htCondition,
			String extCondition) throws Exception {

		boolean update = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = _StrUtils.fString((String) enumUpdate
						.nextElement());
				String value = _StrUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}

			Enumeration enumCondition = htUpdate.keys();
			for (int i = 0; i < htCondition.size(); i++) {
				String key = _StrUtils.fString((String) enumCondition
						.nextElement());
				String value = _StrUtils.fString((String) htCondition.get(key));
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sUpdate = (sUpdate.length() > 0) ? " SET "
					+ sUpdate.substring(0, sUpdate.length() - 1) : "";
			sCondition = (sCondition.length() > 0) ? " WHERE  "
					+ sCondition.substring(0, sCondition.length() - 4) : "";
			String stmt = "UPDATE " + "[" + htUpdate.get("PLANT") + "_"
					+ TABLE_NAME + "]" + sUpdate + sCondition;

			if (extCondition.length() > 0) {
				stmt = stmt + " and " + extCondition;
			}
			this.mLogger.query(this.printQuery, stmt);
			update = updateData(con, stmt);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return update;
	}

	public boolean updateGstType(String query, Hashtable htCondition,
			String extCondition) throws Exception {
		boolean flag = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + TABLE_NAME + "]");
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE  " + formCondition(htCondition));

			if (extCondition.length() != 0) {
				sql.append(extCondition);
			}

			this.mLogger.query(this.printQuery, sql.toString());

			flag = updateData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return flag;
	}

	public boolean deleteGstType(String aGstType) throws Exception {

		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + TABLE_NAME + " WHERE "
					+ IConstants.GST_TYPE + "='" + aGstType.toUpperCase() + "'";
			ps = con.prepareStatement(sQry);
			this.mLogger.query(this.printQuery, sQry);
			deleteItemMst = DeleteRow(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return deleteItemMst;
	}

	public boolean deleteGstType(String aGstType, String plant) throws Exception {

		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + "[" + plant + "_" + TABLE_NAME + "]"
					+ " WHERE " +  IConstants.GST_TYPE + "='" + aGstType.toUpperCase()
					+ "'";
			ps = con.prepareStatement(sQry);
			this.mLogger.query(this.printQuery, sQry);
			deleteItemMst = DeleteRow(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return deleteItemMst;
	}

	public boolean isGstTypeExists(String aGstType) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		boolean isExists = false;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE "
					+ IConstants.GST_TYPE + " = '" + aGstType.toUpperCase()
					+ "'";
			this.mLogger.query(this.printQuery, sQry);
			isExists = isExists(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}
	
	public ArrayList getGstTypeByWMS(String plant,String user) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
                        UserLocUtil userLocUtil = new UserLocUtil();
		        userLocUtil.setmLogger(mLogger);
		        String condAssignedLocforUser = " "; 
			condAssignedLocforUser =  userLocUtil.getUserLocAssigned(user,plant,"GSTTYPE");
                       
			String sQry = "select distinct(gsttype) as gsttype,gstdesc,gstpercentage from "
					+ "["
					+ plant
					+ "_"
					+ "company_config] where plant='"
					+ plant
					+ "' plant <> '' " +condAssignedLocforUser;
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
	
		public boolean isExisit(Hashtable ht, String extCond, String aPlant)
		throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();
	
				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" ");
				sql.append(" FROM " + "[" + aPlant + "_" + "COMPANY_CONFIG" + "]"); // "+"["+aPlant+"_"+"TBLCONTROL"+"]"
				sql.append(" WHERE  " + formCondition(ht));
	
				if (extCond.length() > 0)
					sql.append(" and " + extCond);
	
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