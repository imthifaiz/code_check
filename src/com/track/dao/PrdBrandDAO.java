package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.NamingException;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONObject;

public class PrdBrandDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.PRDBRANDDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.PRDBRANDDAO_PRINTPLANTMASTERLOG;

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

	public PrdBrandDAO() {

	}

	public boolean insertIntoMst(Hashtable<String, String> ht) throws Exception {
		boolean insertedInv = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration<String> enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "PRD_BRAND_MST" + "]" + "("
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

	public boolean isExists(Hashtable<String, String> ht) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();

			// query
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" 1 ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "PRD_BRAND_MST"
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
	
	public boolean isExistPrdBrand(String prd_brand_id,String prd_brand_desc, String plant) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "PRD_BRAND_MST" + "]" + " WHERE "
					+ IConstants.PRDBRANDID + " = '"
					+ prd_brand_desc.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					isExists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}

	public boolean updatePrdBrandMst(Hashtable<String, String> htUpdate, Hashtable<String, String> htCondition)
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
			sCondition = formCondition(htCondition);
			sUpdate = (sUpdate.length() > 0) ? " SET "
					+ sUpdate.substring(0, sUpdate.length() - 1) : "";
			

			String stmt = "UPDATE " + "[" + htCondition.get("PLANT") + "_"
					+ "PRD_BRAND_MST" + "]" + sUpdate + "WHERE " +sCondition;
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
	
	public ArrayList<Map<String, String>> getPrdBrandDetails(String brandID, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList<Map<String, String>> al = new ArrayList<Map<String, String>>();
		try {
			con = com.track.gates.DbBean.getConnection();

			String sQry = "select distinct PRD_BRAND_ID,PRD_BRAND_DESC,ISACTIVE from "
					+ "["
					+ plant
					+ "_"
					+ "PRD_BRAND_MST] where PRD_BRAND_ID like '"
					+ brandID
					+ "%'   " + extraCon + " ORDER BY PRD_BRAND_ID ";
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
	//start code by Bruhan to delete prod brand id on 22/jan/2014
	public boolean deletePrdBrandMst(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deletePrdId()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "PRD_BRAND_MST"
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
	//End code by Bruhan to delete prod brand id on 22/jan/2014
	
	//Resvi Starts Code For ProductBrand on 21.10.21
public JSONObject getprdbrandName(String plant, String name) {
	JSONObject mainObj = new JSONObject();
	String query = "SELECT * FROM [" + plant + "_PRD_BRAND_MST] WHERE PRD_BRAND_ID LIKE '%" + name + "%' OR PRD_BRAND_DESC LIKE '%" + name + "%'";
	Connection con = null;
	try {
		con = DbBean.getConnection();
		Statement stmt = con.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		
		while (rset.next()) {
			
			mainObj.put("PRD_BRAND_ID", rset.getString("PRD_BRAND_ID"));
			mainObj.put("PRD_BRAND_DESC", rset.getString("PRD_BRAND_DESC"));
		}
	} catch (NamingException | SQLException e) {
		mainObj.put("responseText", "failed");
		e.printStackTrace();
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return mainObj;
}

public boolean isExistBrand(String brand, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean isExists = false;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " + "[" +plant+"_PRD_BRAND_MST"
				+ "]" + " WHERE " + IConstants.PRDBRANDID + " = '"
				+ brand.toUpperCase() + "'";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) > 0)
				isExists = true;
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return isExists;
}
	
//Resvi End Code For ProductBrand on 21.10.21

//imti added 
public ArrayList getprdbrandDetail(String prdbrand, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	ArrayList arrCust = new ArrayList();
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT PRD_BRAND_ID,PRD_BRAND_DESC,ISACTIVE FROM  "
				+ "["
				+ plant
				+ "_"
				+ "PRD_BRAND_MST"
				+ "]"
				+ " WHERE PRD_BRAND_ID = '"
				+ prdbrand + "' OR PRD_BRAND_DESC = '"
				+ prdbrand + "'";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			arrCust.add(0, StrUtils.fString(rs.getString(1))); // brandid
			arrCust.add(1, StrUtils.fString(rs.getString(2))); // branddesc
			arrCust.add(2, StrUtils.fString(rs.getString(3))); // isactive
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return arrCust;
}
	
}
