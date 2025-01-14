package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class BankDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.BANKDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.BANKDAO_PRINTPLANTMASTERLOG;

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

	public BankDAO() {

	}

	public boolean insertIntoBankMst(Hashtable<String, String> ht) throws Exception {
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
					+ "BANKMST" + "]" + "("
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
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "BANKMST"
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

	public boolean updateBankMst(Hashtable<String, String> htUpdate, Hashtable<String, String> htCondition)
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
					+ "BANKMST" + "]" + sUpdate + "WHERE " +sCondition;
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
	
	public ArrayList getToBankDetails(String aCustName,
			String plant) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			
			String sQry = "SELECT NAME,BRANCH,ISNULL(BRANCH_NAME,'') BRANCH_NAME,ISNULL(SWIFT_CODE,'') SWIFT_CODE,ISNULL(IFSC_CODE,'') IFSC_CODE,ISNULL(TELNO,'') TELNO,ISNULL(FAX,'') FAX,ISNULL(HPNO,'') HPNO,ISNULL(EMAIL,'') EMAIL,ISNULL(WEBSITE,'') WEBSITE,ISNULL(UNITNO,'') UNITNO,ISNULL(BUILDING,'') BUILDING,ISNULL(STREET,'') STREET,ISNULL(CITY,'')CITY,ISNULL(STATE,'') AS STATE,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ISNULL(CONTACT_PERSON,'') CONTACT_PERSON,ISNULL(NOTE,'') NOTE,ISNULL(ISACTIVE,'')ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ "BANKMST"
					+ "]"
					+ " WHERE NAME LIKE '" + aCustName + "%' ORDER BY BRANCH,NAME ";
			arrList = selectForReport(sQry, ht, "");
			
			this.mLogger.query(this.printQuery, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
	
	public ArrayList getToBankDetailsForSummary(String plant,String BANK_BRANCH,String BANK_BRANCH_CODE,String BANK_NAME) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;
		String extraCon="";
		if(BANK_NAME != "" && BANK_BRANCH!= ""){
			extraCon=" and NAME='"+BANK_NAME+"' and BRANCH_NAME='"+BANK_BRANCH+"'";
			}else if(BANK_NAME != "" && BANK_BRANCH == ""){
				extraCon=" and NAME='"+BANK_NAME+"'";
			}else if(BANK_NAME == "" && BANK_BRANCH != ""){
				extraCon=" and BRANCH_NAME='"+BANK_BRANCH+"'";	
			}
		try {
			con = DbBean.getConnection();
			
			String sQry = "select distinct BRANCH,BRANCH_NAME,NAME,IFSC_CODE,SWIFT_CODE,TELNO,FAX,EMAIL,WEBSITE,FACEBOOKID,TWITTERID,LINKEDINID,UNITNO,BUILDING,STREET,CITY,STATE,COUNTRY,ZIP,CONTACT_PERSON,HPNO,NOTE,ISACTIVE from "
					+ "["
					+ plant
					+ "_"
					+ "BANKMST] where BRANCH like '"
					+ BANK_BRANCH_CODE
					+ "%'   " + extraCon + " ORDER BY BRANCH ";
			arrList = selectForReport(sQry, ht, "");
			
			this.mLogger.query(this.printQuery, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
	
	public ArrayList<Map<String, String>> getBankDetails(String BANK_BRANCH_CODE, String plant,
			String extraCon, String bank_name,String bank_branch ) throws Exception {

		java.sql.Connection con = null;
		ArrayList<Map<String, String>> al = new ArrayList<Map<String, String>>();
		if(bank_name != "" && bank_branch!= ""){
		extraCon=" and NAME='"+bank_name+"' and BRANCH_NAME='"+bank_branch+"'";
		}else if(bank_name != "" && bank_branch == ""){
			extraCon=" and NAME='"+bank_name+"'";
		}else if(bank_name == "" && bank_branch != ""){
			extraCon=" and BRANCH_NAME='"+bank_branch+"'";	
		}
		try {
			con = com.track.gates.DbBean.getConnection();

			String sQry = "select distinct BRANCH,BRANCH_NAME,NAME,IFSC_CODE,SWIFT_CODE,TELNO,FAX,EMAIL,WEBSITE,FACEBOOKID,TWITTERID,LINKEDINID,UNITNO,BUILDING,STREET,CITY,STATE,COUNTRY,ZIP,CONTACT_PERSON,HPNO,NOTE,ISACTIVE from "
					+ "["
					+ plant
					+ "_"
					+ "BANKMST] where BRANCH like '"
					+ BANK_BRANCH_CODE
					+ "%'   " + extraCon + " ORDER BY BRANCH ";
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
	public boolean deleteBankMst(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deletePrdId()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "BANKMST"
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
	
	public ArrayList selectPoHdr(String query, Hashtable ht, String extCond)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "BANKMST" + "]");

		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

				sql.append(" WHERE ");

				conditon = formCondition(ht);

				sql.append(conditon);

			}
			if (extCond.length() > 0)
				sql.append(" " + extCond);
			this.mLogger.query(this.printQuery, sql.toString());

			alData = selectData(con, sql.toString());

		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;
	}
	
	public ArrayList selectForReport(String query, Hashtable ht, String extCond)
			throws Exception {
		boolean flag = false;
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
	

public boolean isExistBank(String bank, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean isExists = false;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " + "[" +plant+"_BANKMST"
				+ "]" + " WHERE " + IDBConstants.BANK + " = '"
				+ bank.toUpperCase() + "'";
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
}
