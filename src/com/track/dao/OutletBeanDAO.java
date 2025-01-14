package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class OutletBeanDAO extends BaseDAO {
	private boolean printQuery = MLoggerConstant.OUTLETBEANDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.OUTLETBEANDAO_PRINTPLANTMASTERLOG;
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

	private String tblName = "POSOUTLETS";

	/**
	 * @method : insertIntoCustomer(Hashtable ht) description : Insert the new
	 *         record into CUSTMST
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	
	public Map selectRow(String query, Hashtable ht, String extCond)
			throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "POSOUTLETS" + "]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
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
	
	public Map selectRowterminal(String query, Hashtable ht, String extCond)
			throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "POSOUTLETSTERMINALS" + "]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
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
	public ArrayList getOutletListStartsWithName(String aCustName,
			String plant) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;	
		String ext="";
		
		ext="ORDER BY OUTLET_NAME ASC";
		
		try {
			con = DbBean.getConnection();

					String sQry = "SELECT OUTLET,OUTLET_NAME,ISNULL(CONTACT_PERSON,'') CONTACT_PERSON,ISNULL(DESGINATION,'')DESGINATION,ISNULL(ADD1,'') ADD1,ISNULL(ADD2,'') ADD2,ISNULL(ADD3,'') ADD3,ISNULL(ADD4,'') as ADD4,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(STATE,'')STATE,ISNULL(ZIP,'') AS ZIP,ISNULL(TELNO,'') TELNO,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(EMAIL,'') EMAIL,ISNULL(ISACTIVE,'')ISACTIVE  FROM "
					+ "["
					+ plant
					+ "_"
					+ "POSOUTLETS"
					+ "]"
					+ " WHERE OUTLET_NAME LIKE '" + aCustName + "%' or OUTLET LIKE '%" + aCustName + "%' ORDER BY OUTLET ASC ";
			arrList = selectForReport(sQry, ht, "");
			this.mLogger.query(this.printQuery, sQry); 
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
	public ArrayList getOutletListssWithName(String aCustName,
			String plant, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;		
		try {
			con = DbBean.getConnection();

					String sQry = "SELECT OUTLET,OUTLET_NAME,ISNULL(CONTACT_PERSON,'') CONTACT_PERSON,ISNULL(DESGINATION,'')DESGINATION,ISNULL(ADD1,'') ADD1,ISNULL(ADD2,'') ADD2,ISNULL(ADD3,'') ADD3,ISNULL(ADD4,'') as ADD4,ISNULL(COUNTRY,'')AS COUNTRY,ISNULL(STATE,'')STATE,ISNULL(ZIP,'') AS ZIP,ISNULL(TELNO,'') TELNO,ISNULL(HPNO,'')HPNO,ISNULL(FAX,'')FAX,ISNULL(EMAIL,'') EMAIL,ISNULL(ISACTIVE,'')ISACTIVE  FROM "
					+ "["
					+ plant
					+ "_"
					+ "POSOUTLETS"
					+ "]"
				//	+ " WHERE VNAME LIKE '" + aCustName + "%'"  + cond;
			+ " WHERE (OUTLET_NAME LIKE '" + aCustName + "%' or OUTLET LIKE '%" + aCustName + "%')"  + cond;
			arrList = selectForReport(sQry, ht, "");
			this.mLogger.query(this.printQuery, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
	public ArrayList getOutletDetails(String aCustCode, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT OUTLET,OUTLET_NAME,ADD1,ADD2,ADD3,COUNTRY,ZIP,ISNULL(CONTACT_PERSON,''),ISNULL(DESGINATION,''),ISNULL(TELNO,''),ISNULL(HPNO,''),ISNULL(FAX,''),ISNULL(EMAIL,''),ADD4,ISACTIVE,ISNULL(STATE,'') STATE,ISNULL((SELECT COUNTRY_CODE FROM "+ plant+"_countrymaster WHERE COUNTRYNAME=COUNTRY),'') COUNTRY_CODE,ISNULL(ISALLOWVARIANT,'') ISALLOWVARIANT,ISNULL(ALLOWVARIANT,'') ALLOWVARIANT FROM  "
					+ "["
					+ plant
					+ "_"
					+ "POSOUTLETS"
					+ "]"
					+ " WHERE OUTLET = '"
					+ aCustCode + "' OR OUTLET_NAME = '"
					+ aCustCode + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, StrUtils.fString(rs.getString(1))); // outletno
				arrCust.add(1, StrUtils.fString(rs.getString(2))); // outletname
				arrCust.add(2, StrUtils.fString(rs.getString(3))); // Address1
				arrCust.add(3, StrUtils.fString(rs.getString(4))); // Address2
				arrCust.add(4, StrUtils.fString(rs.getString(5))); // Address3
				arrCust.add(5, StrUtils.fString(rs.getString(6))); // country
				arrCust.add(6, StrUtils.fString(rs.getString(7))); // zip
				arrCust.add(7, StrUtils.fString(rs.getString(8))); // contactperson
				arrCust.add(8, StrUtils.fString(rs.getString(9))); // desg
				arrCust.add(9, StrUtils.fString(rs.getString(10))); // telno
				arrCust.add(10, StrUtils.fString(rs.getString(11))); // hpno
				arrCust.add(11, StrUtils.fString(rs.getString(12))); // fax
				arrCust.add(12, StrUtils.fString(rs.getString(13))); // email
				arrCust.add(13, StrUtils.fString(rs.getString(14)));// addr4
				arrCust.add(14, StrUtils.fString(rs.getString(15)));// isactive
				arrCust.add(15, StrUtils.fString(rs.getString(16)));// State
				arrCust.add(16, StrUtils.fString(rs.getString(17)));// sCountryCode
				arrCust.add(17, StrUtils.fString(rs.getString(18)));// sisAllowVariant
				arrCust.add(18, StrUtils.fString(rs.getString(19)));// sAllowVariant
				
        
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}
	public boolean isExistsOutlet(String aCustomer, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ "POSOUTLETS" + "]" + " WHERE " + IConstants.OUTLETS_CODE
					+ " = '" + aCustomer.toUpperCase() + "'" + " OR " +IConstants.OUTLET_NAME+ " = '"+ aCustomer.toUpperCase() + "'" ;
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
	 public boolean isExistsOutletName(String aoutletName, String plant)
             throws Exception {
     PreparedStatement ps = null;
     ResultSet rs = null;
     boolean isExists = false;
     Connection con = null;
     try {
             con = DbBean.getConnection();
             String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
                             + "POSOUTLETS" + "]" + " WHERE " + IConstants.OUTLET_NAME
                             + " = '" + aoutletName.toUpperCase() + "'";
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
	 public boolean insertIntoOutlet(Hashtable ht) throws Exception {
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
						+ "POSOUTLETS" + "]" + " ("
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
	 
	 public boolean updateOutlet(Hashtable htUpdate, Hashtable htCondition)
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
						+ "POSOUTLETS" + "] " + sUpdate + sCondition;
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
	 public boolean deleteOutlet(String aCustno, String plant) throws Exception {
			boolean deleteItemMst = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "DELETE FROM " + "[" + plant + "_" + "POSOUTLETS" + "]"
						+ " WHERE " + IConstants.OUTLETS_CODE + "='"
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
	 
	 public int Outletcount(String plant)
				throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			int Outletcount = 0;
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
						+ "POSOUTLETS" + "]" + " WHERE " + IConstants.PLANT
						+ " = '" + plant.toUpperCase() + "'";
				this.mLogger.query(this.printQuery, sQry);
				ps = con.prepareStatement(sQry);
				rs = ps.executeQuery();
				while (rs.next()) {
					Outletcount = rs.getInt(1);
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return Outletcount;
		}
	
	 public boolean isExistsOutlet(Hashtable ht, String extCond) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();
	         String TABLE_NAME="POSOUTLETS";
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
	 
	 public ArrayList getOutletTerminalListStartsWithName(String aCustName,
				String plant) throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList arrList = new ArrayList();
			Hashtable ht= new Hashtable();
			Connection con = null;	
			String ext="";
			
			ext="ORDER BY TERMINAL ASC";
			
			try {
				con = DbBean.getConnection();
				
				String sQry = "SELECT OUTLET,TERMINAL,TERMINAL_NAME,ISNULL(ISACTIVE,'')ISACTIVE  FROM "
						+ "["
						+ plant
						+ "_"
						+ "POSOUTLETSTERMINALS"
						+ "]"
						+ " WHERE TERMINAL LIKE '" + aCustName + "%'  ORDER BY TERMINAL ASC ";
				arrList = selectForReport(sQry, ht, "");
				this.mLogger.query(this.printQuery, sQry); 
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return arrList;
		}
	 
	 public ArrayList getOutletTerminalListssWithName(String Terminal,
				String plant, String cond) throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList arrList = new ArrayList();
			Hashtable ht= new Hashtable();
			Connection con = null;		
			try {
				con = DbBean.getConnection();
				
				String sQry = "SELECT OUTLET,TERMINAL,TERMINAL_NAME,ISNULL(ISACTIVE,'')ISACTIVE  FROM "
						+ "["
						+ plant
						+ "_"
						+ "POSOUTLETSTERMINALS"
						+ "]"
						//	+ " WHERE VNAME LIKE '" + aCustName + "%'"  + cond;
						+ " WHERE (TERMINAL_NAME LIKE '" + Terminal + "%' or TERMINAL LIKE '%" + Terminal + "%')"  + cond;
//						+ " WHERE (TERMINAL LIKE '" + Terminal + "%' )"  + cond;
				arrList = selectForReport(sQry, ht, "");
				this.mLogger.query(this.printQuery, sQry);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return arrList;
		}
	 
	 
	 
	 //imti added for POSREVENEW screen 
	 public ArrayList PaymentModeMstlist(String plant) throws Exception {
			
			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();
				boolean flag = false;
				String sQry = "select ISNULL(PAYMENTMODE,'') PAYMENTMODE from" + "["
						+ plant + "_" + "FINPAYMENTMODEMST" + "]"
						+ " where plant='" + plant + "'"				
				+ " ORDER BY PAYMENTMODE ";
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
	 
	 public ArrayList getOutletTerminalListsWithOutlet(String Outlet,
			 String plant, String cond) throws Exception {
		 PreparedStatement ps = null;
		 ResultSet rs = null;
		 ArrayList arrList = new ArrayList();
		 Hashtable ht= new Hashtable();
		 Connection con = null;		
		 try {
			 con = DbBean.getConnection();
			 
			 String sQry = "SELECT OUTLET,TERMINAL,TERMINAL_NAME,ISNULL(ISACTIVE,'')ISACTIVE  FROM "
					 + "["
					 + plant
					 + "_"
					 + "POSOUTLETSTERMINALS"
					 + "]"
					 + " WHERE (OUTLET LIKE '" + Outlet + "%')"  + cond;
			 arrList = selectForReport(sQry, ht, "");
			 this.mLogger.query(this.printQuery, sQry);
			 
		 } catch (Exception e) {
			 this.mLogger.exception(this.printLog, "", e);
		 } finally {
			 DbBean.closeConnection(con, ps);
		 }
		 return arrList;
	 }

		public ArrayList getOutletTerminalListssWithName(String Outlet,String Terminal,
				String plant, String cond) throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList arrList = new ArrayList();
			Hashtable ht= new Hashtable();
			Connection con = null;	
			String Extcond ="";
			if(Outlet.length()>0) {
				Extcond = " WHERE (O.OUTLET_NAME LIKE '" + Outlet + "' )";
			}
			if(Terminal.length()>0) {
				Extcond = " WHERE (P.TERMINAL_NAME LIKE '" + Terminal + "' )";
			}
			if(Outlet.length()>0 && Terminal.length()>0) {
				Extcond = " WHERE (O.OUTLET_NAME LIKE '" + Outlet + "' AND P.TERMINAL_NAME LIKE '" + Terminal + "'  )";
			}
			try {
				con = DbBean.getConnection();
				
				String sQry = "SELECT O.OUTLET_NAME,P.OUTLET,P.TERMINAL,P.TERMINAL_NAME,ISNULL(P.ISACTIVE,'')ISACTIVE  FROM "
						+ "["
						+ plant
						+ "_"
						+ "POSOUTLETSTERMINALS"
						+ "] P JOIN "
						+  plant +"_POSOUTLETS O ON P.OUTLET = O.OUTLET "
//						+ " WHERE (OUTLET LIKE '% " + Outlet + "%' or TERMINAL_NAME LIKE '%" + Terminal + "%')"  + cond;
						+  Extcond;
				arrList = selectForReport(sQry, ht, "");
				this.mLogger.query(this.printQuery, sQry);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return arrList;
		}
		

		public ArrayList getOutletTerminalDetails(String Terminal, String plant)
				throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList arrCust = new ArrayList();
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT TERMINAL,TERMINAL_NAME,OUTLET,ISACTIVE,DEVICE_NAME,ISNULL(DEVICE_STATUS,'0')DEVICE_STATUS,ISNULL((SELECT top 1 O.OUTLET_NAME FROM "+  plant +"_POSOUTLETS O WHERE A.OUTLET = O.OUTLET),'') OUTLET_NAME,ISNULL(TERMINAL_STARTTIME,'')TERMINAL_STARTTIME,ISNULL(TERMINAL_ENDTIME,'')TERMINAL_ENDTIME,ISNULL(ISALLDAYS,'')ISALLDAYS,ISNULL(FLOATAMOUNT,'0')FLOATAMOUNT,ISNULL(ISALLOWVARIANT,0) ISALLOWVARIANT,ISNULL(ALLOWVARIANT,0) ALLOWVARIANT FROM  "
						+ "["
						+ plant
						+ "_"
						+ "POSOUTLETSTERMINALS"
						+ "] A"
						+ " WHERE TERMINAL = '"
						+ Terminal + "' ";
				this.mLogger.query(this.printQuery, sQry);
				ps = con.prepareStatement(sQry);
				rs = ps.executeQuery();
				while (rs.next()) {
					arrCust.add(0, StrUtils.fString(rs.getString(1))); // terminal
					arrCust.add(1, StrUtils.fString(rs.getString(2))); // terminalname
					arrCust.add(2, StrUtils.fString(rs.getString(3))); // outlet
					arrCust.add(3, StrUtils.fString(rs.getString(4)));// isactive
					arrCust.add(4, StrUtils.fString(rs.getString(5)));// devicename
					arrCust.add(5, StrUtils.fString(rs.getString(6)));// devicestatus
					arrCust.add(6, StrUtils.fString(rs.getString(7)));// outletname
					arrCust.add(7, StrUtils.fString(rs.getString(8)));//starttime
					arrCust.add(8, StrUtils.fString(rs.getString(9)));// endtime
					arrCust.add(9, StrUtils.fString(rs.getString(10)));// isalldays
					arrCust.add(10, StrUtils.fString(rs.getString(11)));//FLOATAMOUNT
					arrCust.add(11, StrUtils.fString(rs.getString(12)));// sisAllowVariant
					arrCust.add(12, StrUtils.fString(rs.getString(13)));// sAllowVariant
					
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return arrCust;
		}
		
		public String getOutletname(String plant, String id)throws Exception {
			java.sql.Connection connection = null;
		    String query = "";
		    String agentname="";
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT OUTLET_NAME FROM ["+ plant +"_"+"POSOUTLETS"+"] WHERE OUTLET='"+ id +"'";

				if(connection != null){
					Map m = this.getRowOfData(connection, query.toString());

					agentname = (String) m.get("OUTLET_NAME");
				this.mLogger.query(this.printQuery, query);
				
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				
				throw e;
			} finally {
				if (connection != null) {
					DbBean.closeConnection(connection);
				}
			}
			return agentname;
		}
		
		public String getOutletnameNew(String plant, String id)throws Exception {
			java.sql.Connection connection = null;
			String query = "";
			String agentname="";
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT OUTLET_NAME FROM ["+ plant +"_"+"POSOUTLETS"+"] WHERE OUTLET in ("+ id +")";
				
				if(connection != null){
					Map m = this.getRowOfData(connection, query.toString());
					
					agentname = (String) m.get("OUTLET_NAME");
					this.mLogger.query(this.printQuery, query);
					
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				
				throw e;
			} finally {
				if (connection != null) {
					DbBean.closeConnection(connection);
				}
			}
			return agentname;
		}
		
		public ArrayList getOutletTerminalTimeDetails(String Terminal, String plant ,String day)
				throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList arrCust = new ArrayList();
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT TERMINAL,TERMINAL_LOGINTIME,TERMINAL_LOGOUTTIME,ISNULL(DAYS,'')DAYS FROM  "
						+ "["
						+ plant
						+ "_"
						+ "POSTERMINALS_LOGIN_LOGOUT"
						+ "] A"
						+ " WHERE TERMINAL = '" 
						+ Terminal + "' AND DAYS='"+ day +"'";
				this.mLogger.query(this.printQuery, sQry);
				ps = con.prepareStatement(sQry);
				rs = ps.executeQuery();
				while (rs.next()) {
					arrCust.add(0, StrUtils.fString(rs.getString(1))); // terminal
					arrCust.add(1, StrUtils.fString(rs.getString(2))); // login
					arrCust.add(2, StrUtils.fString(rs.getString(3))); // logout
					arrCust.add(3, StrUtils.fString(rs.getString(4)));// days
				
					
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return arrCust;
		}
		
		public boolean isExistsTerminal(String Terminal, String plant)
				throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			boolean isExists = false;
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
						+ "POSOUTLETSTERMINALS" + "]" + " WHERE " + IConstants.TERMINAL
						+ " = '" + Terminal.toUpperCase() + "'";
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
		
		public boolean isExistsTerminalName(String TerminalName, String plant)
				 throws Exception {
			 PreparedStatement ps = null;
			 ResultSet rs = null;
			 boolean isExists = false;
			 Connection con = null;
			 try {
				 con = DbBean.getConnection();
				 String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
						 + "POSOUTLETSTERMINALS" + "]" + " WHERE " + IConstants.TERMINAL_NAME
						 + " = '" + TerminalName.toUpperCase() + "'";
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
		
		 public boolean insertIntoOutletTerminal(Hashtable ht) throws Exception {
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
						 + "POSOUTLETSTERMINALS" + "]" + " ("
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
		 public boolean insertIntoOutletTerminalTime(Hashtable ht) throws Exception {
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
						 + "POSTERMINALS_LOGIN_LOGOUT" + "]" + " ("
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
		 
		 
		
		 
		 public boolean updateOutletTerminal(Hashtable htUpdate, Hashtable htCondition)
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
						 + "POSOUTLETSTERMINALS" + "] " + sUpdate + sCondition;
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
		 public boolean updateOutletTerminalTime(Hashtable htUpdate, Hashtable htCondition)
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
						 + "POSTERMINALS_LOGIN_LOGOUT" + "] " + sUpdate + sCondition;
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
		 
		 public boolean deleteOutletTerminal(String Terminal, String plant) throws Exception {
			 boolean deleteItemMst = false;
			 PreparedStatement ps = null;
			 Connection con = null;
			 try {
				 con = DbBean.getConnection();
				 String sQry = "DELETE FROM " + "[" + plant + "_" + "POSOUTLETSTERMINALS" + "]"
						 + " WHERE " + IConstants.TERMINAL + "='"
						 + Terminal.toUpperCase() + "'";
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
		 
		 public boolean isExistsOutletTerminals(Hashtable ht, String extCond) throws Exception {
			 boolean flag = false;
			 java.sql.Connection con = null;
			 try {
				 con = com.track.gates.DbBean.getConnection();
				 String TABLE_NAME="POSOUTLETSTERMINALS";
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
		 
		 public boolean isExistsdeptOutlet(String outlet, String dept,String plant) throws Exception {
				PreparedStatement ps = null;
				ResultSet rs = null;
				boolean isExists = false;
				Connection con = null;
				try {
					con = DbBean.getConnection();
					String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "POSOUTLETS_DEPT_CONFIG" + "]" + " WHERE "
							+ "OUTLET" + " = '"
							+ outlet + "'" + "AND PRD_DEPT_ID" + " = '"
							+ dept + "'";
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
		 public boolean isExistsclsOutlet(String outlet, String dept,String plant) throws Exception {
			 PreparedStatement ps = null;
			 ResultSet rs = null;
			 boolean isExists = false;
			 Connection con = null;
			 try {
				 con = DbBean.getConnection();
				 String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "POSOUTLETS_CATEGORY_CONFIG" + "]" + " WHERE "
						 + "OUTLET" + " = '"
						 + outlet + "'" + "AND PRD_CLS_ID" + " = '"
						 + dept + "'";
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
		 
		 public boolean isExistsPrdOutlet(String outlet, String prd,String plant) throws Exception {
			 PreparedStatement ps = null;
			 ResultSet rs = null;
			 boolean isExists = false;
			 Connection con = null;
			 try {
				 con = DbBean.getConnection();
				 String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "POSOUTLETS_ITEM_CONFIG" + "]" + " WHERE "
						 + "OUTLET" + " = '"
						 + outlet + "'" + "AND ITEM" + " = '"
						 + prd + "'";
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
		 
		 
		 public boolean insertPosDept(Hashtable ht) throws Exception {
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
							+ "POSOUTLETS_DEPT_CONFIG" + "]" + " ("
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
		 public boolean insertPosCls(Hashtable ht) throws Exception {
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
						 + "POSOUTLETS_CATEGORY_CONFIG" + "]" + " ("
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
		 
		 
		 public boolean insertPosPrd(Hashtable ht) throws Exception {
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
						 + "POSOUTLETS_ITEM_CONFIG" + "]" + " ("
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
		 
		 public ArrayList getPosDept(String plant,String extraCon) throws Exception {
				java.sql.Connection con = null;
				ArrayList al = new ArrayList();
				try {
					con = com.track.gates.DbBean.getConnection();
					boolean flag = false;
					StringBuffer sql = new StringBuffer("select ID,ISNULL(OUTLET,'') OUTLET,"
							+ "ISNULL(PRD_DEPT_ID,'') PRD_DEPT_ID,ISNULL((select top 1 OUTLET_NAME from "+plant+"_POSOUTLETS where OUTLET="+plant+"_POSOUTLETS_DEPT_CONFIG.OUTLET ),'')OUTNAME,"
							+ "ISNULL((select top 1 PRD_DEPT_DESC from "+plant+"_PRD_DEPT_MST where PRD_DEPT_ID="+plant+"_POSOUTLETS_DEPT_CONFIG.PRD_DEPT_ID ),'')PRDNAME "
							+ "from ["+plant+"_POSOUTLETS_DEPT_CONFIG] ");
					
					 if (extraCon.length() > 0)
						 sql.append(" where OUTLET in (select  OUTLET from "+plant+"_POSOUTLETS where OUTLET_NAME like '%"+extraCon+"%') order by ID");
					else
					  sql.append(" order by ID");
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
		 
//Thanzith 		 
		 public ArrayList getPoscls(String plant,String extraCon) throws Exception {
			 java.sql.Connection con = null;
			 ArrayList al = new ArrayList();
			 try {
				 con = com.track.gates.DbBean.getConnection();
				 boolean flag = false;
				 StringBuffer sql = new StringBuffer("select ID,ISNULL(OUTLET,'') OUTLET,"
						 + "ISNULL(PRD_CLS_ID,'') PRD_CLS_ID,ISNULL((select top 1 OUTLET_NAME from "+plant+"_POSOUTLETS where OUTLET="+plant+"_POSOUTLETS_CATEGORY_CONFIG.OUTLET ),'')OUTNAME,"
						 + "ISNULL((select top 1 PRD_CLS_DESC from "+plant+"_PRD_CLASS_MST where PRD_CLS_ID="+plant+"_POSOUTLETS_CATEGORY_CONFIG.PRD_CLS_ID ),'')PRDNAME "
						 + "from ["+plant+"_POSOUTLETS_CATEGORY_CONFIG] ");
				 
				 if (extraCon.length() > 0)
					 sql.append(" where OUTLET in (select  OUTLET from "+plant+"_POSOUTLETS where OUTLET_NAME like '%"+extraCon+"%') order by ID");
				 else
					 sql.append(" order by ID");
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
//end		 
		 public ArrayList getPosPrd(String plant,String extraCon) throws Exception {
			 java.sql.Connection con = null;
			 ArrayList al = new ArrayList();
			 try {
				 con = com.track.gates.DbBean.getConnection();
				 boolean flag = false;
				 StringBuffer sql = new StringBuffer("select ID,ISNULL(OUTLET,'') OUTLET,ISNULL(ITEM,'') ITEM,"
				 		+ "ISNULL((select top 1 OUTLET_NAME from "+plant+"_POSOUTLETS where OUTLET="+plant+"_POSOUTLETS_ITEM_CONFIG.OUTLET ),'')OUTNAME,"
				 		+ "ISNULL((select top 1 ITEMDESC from "+plant+"_ITEMMST where ITEM="+plant+"_POSOUTLETS_ITEM_CONFIG.ITEM ),'')ITEMDESC,"
				 		+ "ISNULL((select top 1 PRD_DEPT_ID from "+plant+"_ITEMMST where ITEM="+plant+"_POSOUTLETS_ITEM_CONFIG.ITEM ),'')PRDDEPT,"
				 		+ "ISNULL((select top 1 PRD_CLS_ID from "+plant+"_ITEMMST where ITEM="+plant+"_POSOUTLETS_ITEM_CONFIG.ITEM ),'')PRDCLS,"
				 		+ "ISNULL((select top 1 ITEMTYPE from "+plant+"_ITEMMST where ITEM="+plant+"_POSOUTLETS_ITEM_CONFIG.ITEM ),'')PRDTYPE,"
				 		+ "ISNULL((select top 1 PRD_TYPE_DESC from "+plant+"_PRD_TYPE_MST where PRD_TYPE_ID=(select top 1 ITEMTYPE from "+plant+"_ITEMMST where ITEM="+plant+"_POSOUTLETS_ITEM_CONFIG.ITEM )),'')TYPEDESC "
				 		+ "from ["+plant+"_POSOUTLETS_ITEM_CONFIG] ");
				 
				 if (extraCon.length() > 0)
					 sql.append(" where OUTLET in (select  OUTLET from "+plant+"_POSOUTLETS where OUTLET_NAME like '%"+extraCon+"%') order by ID");
				 else
					 sql.append(" order by ID");
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
		 
		//Commented for delete exist 
//			public boolean isdeptExists(int id,String plant) throws Exception {
//
//				boolean flag = false;
//				java.sql.Connection con = null;
//
//				try {
//					con = com.track.gates.DbBean.getConnection();
//
//					// query
//					StringBuffer sql = new StringBuffer("SELECT * FROM ["+ plant +"_POSOUTLETS_DEPT_CONFIG"+"] WHERE ID ='"+id+"'");
//					this.mLogger.query(this.printQuery, sql.toString());
//					flag = isExists(con, sql.toString());
//
//				} catch (Exception e) {
//					this.mLogger.exception(this.printLog, "", e);
//					throw e;
//				} finally {
//					if (con != null) {
//						DbBean.closeConnection(con);
//					}
//				}
//				return flag;
//			}
			
			public boolean DeleteDepttype(String plant, int id)
			        throws Exception {
					boolean deletestatus = false;
					PreparedStatement ps = null;
					Connection con = null;
					try {
					        con = DbBean.getConnection();
					        
					        
					        String sQry = "DELETE FROM " + "[" + plant +"_POSOUTLETS_DEPT_CONFIG"+"]"
					                        + " WHERE ID ='"+id+"'";
					        this.mLogger.query(this.printQuery, sQry);
					        ps = con.prepareStatement(sQry);
					        int iCnt = ps.executeUpdate();
					        if (iCnt > 0) {
					        	deletestatus = true;
					        }
					} catch (Exception e) {
					        this.mLogger.exception(this.printLog, "", e);
					} finally {
					        DbBean.closeConnection(con, ps);
					}
					
					return deletestatus;
		 	}
			
//Thanzith			
			public boolean DeleteClstype(String plant, int id)
					throws Exception {
				boolean deletestatus = false;
				PreparedStatement ps = null;
				Connection con = null;
				try {
					con = DbBean.getConnection();
					
					
					String sQry = "DELETE FROM " + "[" + plant +"_POSOUTLETS_CATEGORY_CONFIG"+"]"
							+ " WHERE ID ='"+id+"'";
					this.mLogger.query(this.printQuery, sQry);
					ps = con.prepareStatement(sQry);
					int iCnt = ps.executeUpdate();
					if (iCnt > 0) {
						deletestatus = true;
					}
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
				} finally {
					DbBean.closeConnection(con, ps);
				}
				
				return deletestatus;
			}
	//end		
			public boolean DeletePrdtype(String plant, int id)
					throws Exception {
				boolean deletestatus = false;
				PreparedStatement ps = null;
				Connection con = null;
				try {
					con = DbBean.getConnection();
					
					
					String sQry = "DELETE FROM " + "[" + plant +"_POSOUTLETS_ITEM_CONFIG"+"]"
							+ " WHERE ID ='"+id+"'";
					this.mLogger.query(this.printQuery, sQry);
					ps = con.prepareStatement(sQry);
					int iCnt = ps.executeUpdate();
					if (iCnt > 0) {
						deletestatus = true;
					}
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
				} finally {
					DbBean.closeConnection(con, ps);
				}
				
				return deletestatus;
			}
			

		public String getOutletid(String aPlant, String Outlet,String extCond) throws Exception {
			String posid = "";
		
			Hashtable ht = new Hashtable();
			ht.put("PLANT", aPlant);
			ht.put("OUTLET_NAME", Outlet);
			
			String query = "OUTLET";
			Map m = selectRow(query, ht, extCond);
			posid = (String) m.get("OUTLET");
			if (posid == "" || posid == null)
				posid = "";
			return posid;
		}
		
		public String getOutletname(String aPlant, String Outlet,String extCond) throws Exception {
			String outlet_name = "";
			
			Hashtable ht = new Hashtable();
			ht.put("PLANT", aPlant);
			ht.put("OUTLET", Outlet);
			
			String query = "OUTLET_NAME";
			Map m = selectRow(query, ht, extCond);
			outlet_name = (String) m.get("OUTLET_NAME");
			if (outlet_name == "" || outlet_name == null)
				outlet_name = "";
			return outlet_name;
		}
		
		
		public ArrayList getOutletByCompany(String  item, String plant,String extraCon) throws Exception {

			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();

				boolean flag = false;
				String sQry = "select PLANT,OUTLET,SALESUOM,UNITPRICE from POS_OUTLET_PRICE where PLANT ='"	+ plant + "' AND ITEM ='"	+ item + "' "+ extraCon;
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
		
		public ArrayList getOutletByCompany(String  item) throws Exception {
			
			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();
				
				boolean flag = false;
				String sQry = "select PLANT,OUTLET,SALESUOM,UNITPRICE from POS_OUTLET_PRICE where ITEM ='"	+ item + "' ";
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
		
		public ArrayList getOutletByCompany(String  item,String  plant) throws Exception {
			
			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();
				
				boolean flag = false;
				String sQry = "SELECT PLANT,OUTLET,SALESUOM,UNITPRICE FROM POS_OUTLET_PRICE WHERE ITEM ='"	+ item + "' "
						+ "AND PLANT in (SELECT CHILD_PLANT FROM [PARENT_CHILD_COMPANY_DET] WHERE PARENT_PLANT ='"	+ plant + "')";
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
		
		public ArrayList getOutletByEmp(String  item,String  plant,String  outlet) throws Exception {
			
			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();
				
				boolean flag = false;
				String sQry = "SELECT PLANT,OUTLET,SALESUOM,UNITPRICE FROM POS_OUTLET_PRICE WHERE ITEM ='"	+ item + "' AND OUTLET ='"	+ outlet + "' "
						+ "AND PLANT='"	+ plant + "' ";
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
		
public ArrayList getOutletByCompanyminmaxEmp(String  item,String  plant,String  outlet) throws Exception {
			
			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();
				
				boolean flag = false;
				String sQry = "SELECT PLANT,OUTLET,MINQTY,MAXQTY FROM POS_OUTLET_MINMAX WHERE ITEM ='"	+ item + "' AND OUTLET ='"	+ outlet + "' "
						+ "AND PLANT='"	+ plant + "' ";
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
public ArrayList getOutletByCompanyminmax(String  item,String  plant) throws Exception {
	
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		
		boolean flag = false;
		String sQry = "SELECT PLANT,OUTLET,MINQTY,MAXQTY FROM POS_OUTLET_MINMAX WHERE ITEM ='"	+ item + "' "
				+ "AND PLANT in (SELECT CHILD_PLANT FROM [PARENT_CHILD_COMPANY_DET] WHERE PARENT_PLANT ='"	+ plant + "')";
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
		
		public String getOutletTerminalname(String aPlant, String Outlet,String terminal) throws Exception {
			String outlet_name = "";
			
			Hashtable ht = new Hashtable();
			ht.put("PLANT", aPlant);
			ht.put("OUTLET", Outlet);
			ht.put("TERMINAL", terminal);
			
			String query = "TERMINAL_NAME";
			Map m = selectRowterminal(query, ht, "");
			outlet_name = (String) m.get("TERMINAL_NAME");
			if (outlet_name == "" || outlet_name == null)
				outlet_name = "";
			return outlet_name;
		}
		
		public ArrayList getalloutlet(String plant) throws Exception {

			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();

				boolean flag = false;
				String sQry = "select * from "+ plant +"_POSOUTLETS where PLANT ='"+ plant +"'";
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
		
		public ArrayList getterminalbyoutlet(String plant,String outlet) throws Exception {

			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();

				boolean flag = false;
				String sQry = "select * from "+ plant +"_POSOUTLETSTERMINALS where PLANT ='"+ plant +"' AND OUTLET ='"+ outlet +"'";
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
		
		public boolean isExistsOutletMinmax(String outlet, String item,String plant)
				throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			boolean isExists = false;
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT COUNT(*) FROM POS_OUTLET_MINMAX WHERE " + IConstants.OUTLETS_CODE + " = '" + outlet.toUpperCase() + "'" 
				+ " AND " +IConstants.ITEM+ " = '"+ item.toUpperCase() + "' AND "+IConstants.PLANT+" = '"+plant+"' " ;
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
		
		public boolean insertIntoOutletMinmax(Hashtable ht) throws Exception {
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
				String query = "INSERT INTO POS_OUTLET_MINMAX ("
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
		
		public boolean updateOutletMinmax(Hashtable htUpdate, Hashtable htCondition)
				throws Exception {
			boolean updateItemmst = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sUpdate = "", sCondition = "";

				// generate the condition string
				Enumeration enum1Update = htUpdate.keys();
				for (int i = 0; i < htUpdate.size(); i++) {
					String key = StrUtils.fString((String) enum1Update
							.nextElement());
					String value = StrUtils.fString((String) htUpdate.get(key));
					sUpdate += key.toUpperCase() + " = '" + value + "',";
				}

				// generate the update string
				Enumeration enum1Condition = htCondition.keys();
				for (int i = 0; i < htCondition.size(); i++) {
					String key = StrUtils.fString((String) enum1Condition
							.nextElement());
					String value = StrUtils.fString((String) htCondition.get(key));
					sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
							+ "' AND ";
				}
				sUpdate = (sUpdate.length() > 0) ? sUpdate.substring(0, sUpdate
						.length() - 1) : "";
				sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
						sCondition.length() - 4) : "";
				String sQry = "UPDATE POS_OUTLET_MINMAX  SET " + sUpdate + " WHERE "
						+ sCondition;

				this.mLogger.query(this.printQuery, sQry);

				ps = con.prepareStatement(sQry);
				int iCnt = ps.executeUpdate();
				if (iCnt > 0)
					updateItemmst = true;

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				DbBean.closeConnection(con, ps);
			}

			return updateItemmst;
		}
		
	
}