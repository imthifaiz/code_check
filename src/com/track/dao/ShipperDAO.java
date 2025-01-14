package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class ShipperDAO extends BaseDAO {
	private boolean printQuery = MLoggerConstant.SHIPPERDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.SHIPPERDAO_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	StrUtils strUtils = new StrUtils();

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

	private String tblName = "FREIGHT_FORWARDERMST";

		
	
	public boolean isExistShipper(String aCustomer, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ "FREIGHT_FORWARDERMST" + "]" + " WHERE " + IConstants.FREIGHTFORWARDER_CODE
					+ " = '" + aCustomer.toUpperCase() + "'" + " OR " +IConstants.FREIGHTFORWARDER_NAME+ " = '"+ aCustomer.toUpperCase() + "'" ;
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
	
	public boolean isExistsShipper(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
         String TABLE_NAME="FREIGHT_FORWARDERMST";
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME
					+ "]");
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
	
	 public boolean isExistsShipperName(String avendName, String plant)
             throws Exception {
     PreparedStatement ps = null;
     ResultSet rs = null;
     boolean isExists = false;
     Connection con = null;
     try {
             con = DbBean.getConnection();
             String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
                             + "FREIGHT_FORWARDERMST" + "]" + " WHERE " + IConstants.FREIGHTFORWARDER_NAME
                             + " = '" + avendName.toUpperCase() + "'";
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
	
	public int Shippercount(String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int Vendorcount = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ "FREIGHT_FORWARDERMST" + "]" + " WHERE " + IConstants.PLANT
					+ " = '" + plant.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Vendorcount = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return Vendorcount;
	}
	
	public boolean updateShipper(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			// generate the condition string
			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = StrUtils
						.fString((String) enumUpdate.nextElement());
				String value = StrUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}

			// generate the update string
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
			String stmt = "UPDATE " + "[" + htUpdate.get("PLANT") + "_"
					+ "FREIGHT_FORWARDERMST" + "] " + sUpdate + sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				update = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return update;
	}
	
	public boolean deleteShipper(String aCustno, String plant) throws Exception {
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "DELETE FROM " + "[" + plant + "_" + "FREIGHT_FORWARDERMST" + "]"
					+ " WHERE " + IConstants.FREIGHTFORWARDER_CODE + "='"
					+ aCustno.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				deleteItemMst = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return deleteItemMst;
	}
	
	
	public ArrayList getShipperDetails(String aCustCode, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT FREIGHT_FORWARDERNO,FREIGHT_FORWARDERNAME,ISNULL(TRANSPORTID,'0')TRANSPORTID,ISNULL(LICENCENO,''),ISNULL(NAME,''),ISNULL(DESGINATION,''),ADDR1,ADDR2,ADDR3,ADDR4,ISNULL(STATE,'') STATE,COUNTRY,ZIP,ISNULL(WORKPHONE,'')WORKPHONE,ISNULL(HPNO,''),ISNULL(CUSTOMEREMAIL,'')CUSTOMEREMAIL,ISNULL(WEBSITE,'')WEBSITE,ISNULL(REMARKS,''),ISACTIVE,ISNULL((SELECT COUNTRY_CODE FROM "+ plant+"_countrymaster WHERE COUNTRYNAME=COUNTRY),'') COUNTRY_CODE FROM "
					+ "["
					+ plant
					+ "_"
					+ "FREIGHT_FORWARDERMST"
					+ "]"
					+ " WHERE FREIGHT_FORWARDERNO = '"
					+ aCustCode + "' OR FREIGHT_FORWARDERNAME = '"
					+ aCustCode + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, StrUtils.fString(rs.getString(1))); // no
				arrCust.add(1, StrUtils.fString(rs.getString(2))); // shipname
				arrCust.add(2, StrUtils.fString(rs.getString(3))); // transport
				arrCust.add(3, StrUtils.fString(rs.getString(4))); // licence
				arrCust.add(4, StrUtils.fString(rs.getString(5))); // name
				arrCust.add(5, StrUtils.fString(rs.getString(6))); // designation
				arrCust.add(6, StrUtils.fString(rs.getString(7))); // add1
				arrCust.add(7, StrUtils.fString(rs.getString(8))); // add2
				arrCust.add(8, StrUtils.fString(rs.getString(9))); // add3
				arrCust.add(9, StrUtils.fString(rs.getString(10))); // add4
				arrCust.add(10, StrUtils.fString(rs.getString(11))); // state
				arrCust.add(11, StrUtils.fString(rs.getString(12))); // country
				arrCust.add(12, StrUtils.fString(rs.getString(13))); // zip
				arrCust.add(13, StrUtils.fString(rs.getString(14))); // workphone
				arrCust.add(14, StrUtils.fString(rs.getString(15))); // HPNO
				arrCust.add(15, StrUtils.fString(rs.getString(16)));// Email
				arrCust.add(16, StrUtils.fString(rs.getString(17)));// WEBSITE
				arrCust.add(17, StrUtils.fString(rs.getString(18)));// REMARKS
				arrCust.add(18, StrUtils.fString(rs.getString(19)));// ISACTIVE
				arrCust.add(19, StrUtils.fString(rs.getString(20)));// CountryCode
				/*
				 * arrCust.add(19, StrUtils.fString(rs.getString(20)));// State arrCust.add(20,
				 * StrUtils.fString(rs.getString(21)));//RCBNO arrCust.add(21,
				 * StrUtils.fString(rs.getString(22)));//SUPPLIER TYPE arrCust.add(22,
				 * StrUtils.fString(rs.getString(23)));//CUSTOMER EMAIL arrCust.add(23,
				 * StrUtils.fString(rs.getString(24)));//WEBSITE arrCust.add(24,
				 * StrUtils.fString(rs.getString(25)));//FACEBOOK arrCust.add(25,
				 * StrUtils.fString(rs.getString(26)));//TWITTER arrCust.add(26,
				 * StrUtils.fString(rs.getString(27)));//LINKEDIN arrCust.add(27,
				 * StrUtils.fString(rs.getString(28)));//SKYPE arrCust.add(28,
				 * StrUtils.fString(rs.getString(29)));//OPENING BALANCE arrCust.add(29,
				 * StrUtils.fString(rs.getString(30)));//WORKPHONE arrCust.add(30,
				 * StrUtils.fString(rs.getString(31)));//TAXTREATMENT arrCust.add(31,
				 * StrUtils.fString(rs.getString(32)));//COUNTRY_CODE arrCust.add(32,
				 * StrUtils.fString(rs.getString(33)));//BANKNAME arrCust.add(33,
				 * StrUtils.fString(rs.getString(34)));//BRANCH arrCust.add(34,
				 * StrUtils.fString(rs.getString(35)));//IBAN arrCust.add(35,
				 * StrUtils.fString(rs.getString(36)));//BANKROUTINGCODE arrCust.add(36,
				 * StrUtils.fString(rs.getString(37)));//companyreg arrCust.add(37,
				 * StrUtils.fString(rs.getString(38)));//CURRENCY arrCust.add(38,
				 * StrUtils.fString(rs.getString(39)));//TRANSPORT arrCust.add(39,
				 * StrUtils.fString(rs.getString(40)));// PaymentTerms arrCust.add(40,
				 * StrUtils.fString(rs.getString(41)));// peppol arrCust.add(41,
				 * StrUtils.fString(rs.getString(42)));// peppolid
				 */        
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}
	
	public ArrayList getShipperListStartsWithName(String aCustName,
			String plant, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;	
		String ext="";
		if(cond.length()>0)
		{
		ext ="AND   '" + cond + "' ";
		}
		else {
		ext="ORDER BY FREIGHT_FORWARDERNAME ASC";
		}
		try {
			con = DbBean.getConnection();

					String sQry = "SELECT FREIGHT_FORWARDERNO,FREIGHT_FORWARDERNAME,ISNULL(TRANSPORTID,'0')TRANSPORTID,ISNULL(LICENCENO,'') LICENCENO,ISNULL(NAME,'') NAME,ISNULL(DESGINATION,'')DESGINATION,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(ADDR4,'') as ADDR4,ISNULL(STATE,'')STATE,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(WORKPHONE,'') AS WORKPHONE,ISNULL(HPNO,'')HPNO,ISNULL(CUSTOMEREMAIL,'') AS CUSTOMEREMAIL,ISNULL(WEBSITE,'') AS WEBSITE,ISNULL(REMARKS,'')REMARKS,ISNULL(IsActive,'')IsActive FROM "
					+ "["
					+ plant
					+ "_"
					+ "FREIGHT_FORWARDERMST"
					+ "]"
				//	+ " WHERE VNAME LIKE '" + aCustName + "%'"  + cond;
//			+ " WHERE (VNAME LIKE '" + aCustName + "%' or VENDNO LIKE '%" + aCustName + "%')"  + ext;
					+ " WHERE FREIGHT_FORWARDERNAME LIKE '" + aCustName + "%' or FREIGHT_FORWARDERNO LIKE '%" + aCustName + "%' ORDER BY FREIGHT_FORWARDERNAME ASC ";
			arrList = selectForReport(sQry, ht, "");
			this.mLogger.query(this.printQuery, sQry); 
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
	public ArrayList getShipperListssWithName(String aCustName,
			String plant, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;		
		try {
			con = DbBean.getConnection();

					String sQry = "SELECT FREIGHT_FORWARDERNO,FREIGHT_FORWARDERNAME,ISNULL(TRANSPORTID,'0')TRANSPORTID,ISNULL(LICENCENO,'') LICENCENO,ISNULL(NAME,'') NAME,ISNULL(DESGINATION,'')DESGINATION,ISNULL(ADDR1,'') ADDR1,ISNULL(ADDR2,'') ADDR2,ISNULL(ADDR3,'') ADDR3,ISNULL(ADDR4,'') as ADDR4,ISNULL(STATE,'')STATE,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(ZIP,'') AS ZIP,ISNULL(WORKPHONE,'') AS WORKPHONE,ISNULL(HPNO,'')HPNO,ISNULL(CUSTOMEREMAIL,'') AS CUSTOMEREMAIL,ISNULL(WEBSITE,'') AS WEBSITE,ISNULL(REMARKS,'')REMARKS,ISNULL(IsActive,'')IsActive FROM "
					+ "["
					+ plant
					+ "_"
					+ "FREIGHT_FORWARDERMST"
					+ "]"
				//	+ " WHERE VNAME LIKE '" + aCustName + "%'"  + cond;
			+ " WHERE (FREIGHT_FORWARDERNAME LIKE '" + aCustName + "%' or FREIGHT_FORWARDERNO LIKE '%" + aCustName + "%')"  + cond;
			arrList = selectForReport(sQry, ht, "");
			this.mLogger.query(this.printQuery, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

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

	
	public boolean insertIntoShipper(Hashtable ht) throws Exception {
		boolean insertedCust = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key.toUpperCase() + ",";
				VALUES += "'" + value + "',";
			}
			String sQry = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "FREIGHT_FORWARDERMST" + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedCust = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return insertedCust;
	}



}