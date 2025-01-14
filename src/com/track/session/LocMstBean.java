package com.track.session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.track.constants.IConstants;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

@Deprecated
public class LocMstBean extends BaseBean implements SessionBean {
	private String TABLE_NAME = "LOCMST";
	SessionContext sessionContext;
	StrUtils _StrUtils = new StrUtils();

	public void ejbCreate() {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public void ejbRemove() {
	}

	public void setSessionContext(SessionContext sessionContext) {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		this.sessionContext = sessionContext;
	}

	public ArrayList select(String selectList, Hashtable ht, String extCond)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean flag = false;
		java.sql.Connection con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" SELECT " + selectList + " from "
				+ TABLE_NAME);
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
		return selectData(con, sql.toString());

	}

	public ArrayList getLocDetails(String selectList, Hashtable ht,
			String extCond) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean flag = false;

		java.sql.Connection con = null;
		ArrayList alResult = new ArrayList();
		StringBuffer sql = new StringBuffer(" SELECT " + selectList + " from "
				+ "[" + ht.get("PLANT") + "_" + TABLE_NAME + "]");

		try {
			con = com.track.gates.DbBean.getConnection();

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

			alResult = selectData(con, sql.toString());
		} catch (Exception e) {
			MLogger.log(-1, "getLocDetails() " + e.getMessage());
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

		return alResult;

	}

	public ArrayList getAllLocDetails(String selectList, Hashtable ht,
			String extCond) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean flag = false;

		java.sql.Connection con = null;
		ArrayList alResult = new ArrayList();
		StringBuffer sql = new StringBuffer(" SELECT " + selectList + " from "
				+ "[" + ht.get("PLANT") + "_" + TABLE_NAME + "]");

		try {
			con = com.track.gates.DbBean.getConnection();

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
			MLogger.log(0, "[SQL]\n " + sql);
			alResult = selectData(con, sql.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

		return alResult;

	}

	public ArrayList getLocList(String selectList, Hashtable ht, String loc)
			throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean flag = false;

		java.sql.Connection con = null;
		ArrayList alResult = new ArrayList();
		StringBuffer sql = new StringBuffer(" SELECT " + selectList + " from "
				+ "[" + ht.get("PLANT") + "_" + TABLE_NAME
				+ "] where loc like'" + loc + "%" + "'");

		try {
			con = com.track.gates.DbBean.getConnection();

			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" and ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}

			MLogger.log(0, "[SQL]\n " + sql);
			alResult = selectData(con, sql.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

		return alResult;

	}

	public boolean insertIntoLocMst(Hashtable ht) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean insertedCycleCount = false;

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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ TABLE_NAME + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			MLogger.log(0, "[SQL]\n " + query);

			insertedCycleCount = insertData(conn, query);

		} catch (Exception e) {
			MLogger
					.log(-1, "insertIntoLocMst() Exception :: "
							+ e.getMessage());
			throw e;
		} finally {
			DbBean.closeConnection(conn);
		}

		return insertedCycleCount;
	}

	public boolean updateLocMst(Hashtable htUpdate, Hashtable htCondition,
			String extCondition) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
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

			update = updateData(con, stmt);

		} catch (Exception e) {
			MLogger.log(-1, "############# Exceptin :: updateLocMst() : "
					+ e.getMessage());
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return update;
	}

	public boolean updateLocMst(String query, Hashtable htCondition,
			String extCondition) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
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

			MLogger.log(0, "[SQL]\n " + sql.toString());

			flag = updateData(con, sql.toString());

		} catch (Exception e) {
			MLogger.log(-1, "############# Exceptin :: updateLocMst() : "
					+ e.getMessage());
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return flag;
	}

	public boolean deleteLoc(String aCustno) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + TABLE_NAME + " WHERE "
					+ IConstants.LOC + "='" + aCustno.toUpperCase() + "'";
			ps = con.prepareStatement(sQry);
			MLogger.log(0, "[SQL]\n " + sQry);
			deleteItemMst = DeleteRow(con, sQry);

		} catch (Exception e) {
			MLogger.log(-1, "############# Exceptin :: updateLocMst() : "
					+ e.getMessage());
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return deleteItemMst;
	}

	public boolean deleteLoc(String aCustno, String plant) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + "[" + plant + "_" + TABLE_NAME + "]"
					+ " WHERE " + IConstants.LOC + "='" + aCustno.toUpperCase()
					+ "'";
			ps = con.prepareStatement(sQry);
			MLogger.log(0, "[SQL]\n " + sQry);
			deleteItemMst = DeleteRow(con, sQry);

		} catch (Exception e) {
			MLogger.log(-1, "############# Exceptin :: updateLocMst() : "
					+ e.getMessage());
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return deleteItemMst;
	}

	public boolean isLocExists(String aLoc) throws Exception {
		System.out
				.println(this.getClass()
						+ " This EJB will be remove  in next release. Please use the DAO Implementaion");
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		boolean isExists = false;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE "
					+ IConstants.CUSTOMER_CODE + " = '" + aLoc.toUpperCase()
					+ "'";
			MLogger.log(0, "[SQL]\n " + sQry);
			isExists = isExists(con, sQry);

		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}

}