package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.NamingException;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONObject;

public class PrdClassDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.PRDCLASSDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.PRDCLASSDAO_PRINTPLANTMASTERLOG;

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

	public PrdClassDAO() {

	}

	public boolean insertPrdClsMst(Hashtable ht) throws Exception {
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
					+ "PRD_CLASS_MST" + "]" + "("
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

	public boolean isExists(Hashtable ht) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" 1 ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "PRD_CLASS_MST"
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

	public boolean updatePrdClsMst(Hashtable htUpdate, Hashtable htCondition)
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
					+ "PRD_CLASS_MST" + "]" + sUpdate + sCondition;
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

	public boolean deletePrdClsId(java.util.Hashtable ht) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "PRD_CLASS_MST"
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

	public ArrayList getPrdClsDetails(String prdcls, String plant, String cond)
			throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct prd_cls_id,prd_cls_desc,ISACTIVE,case when CATLOGPATH!='' then ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'../jsp/dist/img/NO_IMG.png') else '../jsp/dist/img/NO_IMG.png' end AS CATALOG from "
					+ "["
					+ plant
					+ "_"
					+ "prd_Class_mst] where prd_cls_id like '"
					+ prdcls
					+ "%' " + cond
					+ " ORDER BY prd_cls_id ";
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
	
	public boolean isAlreadyPOSCategoryExists(String aPRD_CLS_ID,String plant) throws Exception {
		Connection con = null;
		boolean isExists = false;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_POSCLOSINGSTOCKCATEGORY] WHERE "
                                        + "  PRD_CLS_ID ='"+aPRD_CLS_ID.toUpperCase()
                                        + "'";
			this.mLogger.query(this.printQuery, sQry);
			isExists = isExists(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return isExists;
	}
	
	public boolean deletePOSCategory(String plant) throws Exception {

		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + "[" + plant + "_POSCLOSINGSTOCKCATEGORY]";
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
	
	public boolean insertPOSCategory(Hashtable ht) throws Exception {
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_POSCLOSINGSTOCKCATEGORY]"
					+ "("
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
	
	 public boolean isExistPRDCAT(String prd_cls_id,String prd_cls_desc, String plant) throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			boolean isExists = false;
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "PRD_CLASS_MST" + "]" + " WHERE "
						+ IConstants.PRDCLSID + " = '"
						+ prd_cls_desc.toUpperCase() + "'";
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
	
public boolean updateCatryImage(String plant,String PRD_CLS_ID,String catalogPath) {
		
		Connection con = null;
		boolean insertedItemImage = false;
		try {
			con = DbBean.getConnection();
			String query = "update "+plant+"_prd_Class_mst set CATLOGPATH ='"+catalogPath+"' where PRD_CLS_ID ='"+PRD_CLS_ID+"'";
			
			this.mLogger.query(this.printQuery, query);
			PreparedStatement ps = con.prepareStatement(query);
			int iCnt = ps.executeUpdate();
			
			if (iCnt > 0)
				insertedItemImage = true;
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		
		return insertedItemImage;
	}


public JSONObject getprdclsName(String plant, String name) {
	JSONObject mainObj = new JSONObject();
	String query = "SELECT * FROM [" + plant + "_PRD_CLASS_MST] WHERE PRD_CLS_ID LIKE '%" + name + "%' OR PRD_CLS_DESC LIKE '%" + name + "%'";
	Connection con = null;
	try {
		con = DbBean.getConnection();
		Statement stmt = con.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		
		while (rset.next()) {
			
			mainObj.put("PRD_CLS_ID", rset.getString("PRD_CLS_ID"));
			mainObj.put("PRD_CLS_DESC", rset.getString("PRD_CLS_DESC"));
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

public JSONObject getprdclsNames(String plant, String name) {
	JSONObject mainObj = new JSONObject();
	String query = "SELECT * FROM [" + plant + "_PRD_CLASS_MST] WHERE PRD_CLS_ID LIKE '" + name + "%' OR PRD_CLS_DESC LIKE '" + name + "%'";
	Connection con = null;
	try {
		con = DbBean.getConnection();
		Statement stmt = con.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		
		while (rset.next()) {
			
			mainObj.put("PRD_CLS_ID", rset.getString("PRD_CLS_ID"));
			mainObj.put("PRD_CLS_DESC", rset.getString("PRD_CLS_DESC"));
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

public boolean isExistCat(String cat, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean isExists = false;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " + "[" +plant+"_PRD_CLASS_MST"
				+ "]" + " WHERE " + IConstants.PRDCLSID + " = '"
				+ cat.toUpperCase() + "'";
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

//imti added 
public ArrayList getprdclassDetail(String prdclass, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	ArrayList arrCust = new ArrayList();
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT PRD_CLS_ID,PRD_CLS_DESC,ISACTIVE FROM  "
				+ "["
				+ plant
				+ "_"
				+ "PRD_CLASS_MST"
				+ "]"
				+ " WHERE PRD_CLS_ID = '"
				+ prdclass + "' OR PRD_CLS_DESC = '"
				+ prdclass + "'";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			arrCust.add(0, StrUtils.fString(rs.getString(1))); // classid
			arrCust.add(1, StrUtils.fString(rs.getString(2))); // classdesc
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
