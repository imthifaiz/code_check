package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.object.DoDet;
import com.track.gates.DbBean;
import com.track.service.DoDetService;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class LocTypeTwoDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.LocTypeTwoDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.LocTypeTwoDAO_PRINTPLANTMASTERLOG;

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

	public LocTypeTwoDAO() {

	}

	public boolean insertIntoLocTypeMst(Hashtable ht) throws Exception {
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
					+ "LOC_TYPE_MST" + "]" + "("
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


	/* ********** Modification History *************************************
	 *  Bruhan on July 9 2014, Description:To change SELECT 1 To SELECT COUNT(*)
	 * 
	 * 
	 */
	public boolean isExists(Hashtable ht) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();

			// query
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "LOC_TYPE_MST"
					+ "]");
			sql.append(" WHERE  " + formCondition(ht));
			this.mLogger.query(this.printQuery, sql.toString());
			System.out.println(sql.toString());
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

	public boolean updateLocTypeMst(Hashtable htUpdate, Hashtable htCondition)
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
					+ "LOC_TYPE_MST" + "]" + sUpdate + sCondition;
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

	public boolean deleteLocTypeId(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deletePrdId()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "LOC_TYPE_MST"
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

	public ArrayList getLocTypeDetails(String loctypeid, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct LOC_TYPE_ID,LOC_TYPE_DESC,ISACTIVE from "
					+ "["
					+ plant
					+ "_"
					+ "LOC_TYPE_MST] where LOC_TYPE_ID like '"
					+ loctypeid
					+ "%'   OR LOC_TYPE_DESC like '"+loctypeid+"%'" + extraCon
					+ " ORDER BY LOC_TYPE_ID ";
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
	public String getLocTypeDesc(String aPlant, String loctype) throws Exception {
		String loctypeDesc = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("LOC_TYPE_ID", loctype);
		String query = " isnull(loc_type_desc,'') loctypedesc ";
		Map m = selectRow(query, ht);
		loctypeDesc = StrUtils.fString((String) m.get("loctypedesc"));
		return loctypeDesc;
	}
	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from  ["
					+ (String) ht.get("PLANT") + "_LOC_TYPE_MST]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
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
	//imti refer frm itemMstDAO
	public int Loc_count(String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int Loc_count = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+  "LOCMST" + "]" + " WHERE " + IConstants.PLANT
					+ " = '" + plant.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Loc_count = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return Loc_count;
	}
	//imti end of the code

}
