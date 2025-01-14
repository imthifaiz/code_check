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
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class CurrencyDAO extends BaseDAO {
	public static String TABLE_NAME = "CurrencyMst";
	
	private boolean printQuery = MLoggerConstant.CUSTMSTDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.CUSTMSTDAO_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.CUSTMSTDAO_PRINTPLANTMASTERINFO;

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

	StrUtils _StrUtils = null;

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
 public CurrencyDAO ()
 {
	 
 }
 public boolean isExisit(Hashtable ht, String extCond) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "["+ht.get("PLANT")+"_"+TABLE_NAME+"]");
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
 
 public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "CURRENCYMST" + "]");
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

 public boolean insCurrencyMst(Hashtable ht) throws Exception {

		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration
						.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "["+ht.get("PLANT")+"_"+TABLE_NAME+"]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query);
			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}

		return insertFlag;
	}
 public ArrayList selectCurrencyMst(String query, Hashtable ht, String extCondi)
	throws Exception {

boolean flag = false;
ArrayList alData = new ArrayList();
java.sql.Connection con = null;

StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
		+"["+ht.get("PLANT")+"_"+ TABLE_NAME+"]");
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
	this.mLogger.exception(this.printLog, "", e);
	throw e;
} finally {
	if (con != null) {
		DbBean.closeConnection(con);
	}
}

return alData;
}

 public ArrayList getCurrencyDetails(String aCustCode, String plant)
	throws Exception {
PreparedStatement ps = null;
ResultSet rs = null;
ArrayList arrCust = new ArrayList();
Connection con = null;
try {
	con = DbBean.getConnection();
	String sQry = "SELECT CURRENCYID,DESCRIPTION,DISPLAY,CURRENCYUSEQT,REMARK,isnull(STATUS,'') STATUS,ISACTIVE FROM "
			+ "["
			+ plant
			+ "_"
			+ "CURRENCYMST"
			+ "]"
			+ " WHERE CURRENCYID  like '"
			+ aCustCode +"%"+ "'";
	this.mLogger.query(this.printQuery, sQry);
	ps = con.prepareStatement(sQry);
	rs = ps.executeQuery();
	while (rs.next()) {
		ArrayList arrLine = new ArrayList();
		arrLine.add(0, StrUtils.fString((String) rs.getString(1))); // currencyid
		arrLine.add(1, StrUtils.fString((String) rs.getString(2))); //desc
		arrLine.add(2, StrUtils.fString((String) rs.getString(3))); // display
		arrLine.add(3, StrUtils.fString((String) rs.getString(4))); // curruseqt
		arrLine.add(4, StrUtils.fString((String) rs.getString(5))); // remark
		arrLine.add(5, StrUtils.fString((String) rs.getString(6))); // status
		arrLine.add(6, StrUtils.fString((String) rs.getString(7))); // isActive
		arrCust.add(arrLine);
	}
} catch (Exception e) {
	this.mLogger.exception(this.printLog, "", e);
} finally {
	DbBean.closeConnection(con, ps);
}
return arrCust;
}
 public ArrayList getCurencyByDisplay(String display, String plant)
	throws Exception {
PreparedStatement ps = null;
ResultSet rs = null;
ArrayList arrCust = new ArrayList();
Connection con = null;
try {
	con = DbBean.getConnection();
	String sQry = "SELECT CURRENCYID,DESCRIPTION,DISPLAY,CURRENCYUSEQT,REMARK,STATUS,ISACTIVE FROM "
			+ "["
			+ plant
			+ "_"
			+ "CURRENCYMST"
			+ "]";
			//+ " WHERE DISPLAY  like '"
			//+ display +"%"+ "'";
	this.mLogger.query(this.printQuery, sQry);
	ps = con.prepareStatement(sQry);
	rs = ps.executeQuery();
	while (rs.next()) {
		ArrayList arrLine = new ArrayList();
		arrLine.add(0, StrUtils.fString((String) rs.getString(1))); // currencyid
		arrLine.add(1, StrUtils.fString((String) rs.getString(2))); //desc
		arrLine.add(2, StrUtils.fString((String) rs.getString(3))); // display
		arrLine.add(3, StrUtils.fString((String) rs.getString(4))); // curruseqt
		arrLine.add(4, StrUtils.fString((String) rs.getString(5))); // remark
		arrLine.add(5, StrUtils.fString((String) rs.getString(6))); // status
		arrLine.add(6, StrUtils.fString((String) rs.getString(7))); // isActive
		arrCust.add(arrLine);
	}
} catch (Exception e) {
	this.mLogger.exception(this.printLog, "", e);
} finally {
	DbBean.closeConnection(con, ps);
}
return arrCust;
}


 
	public boolean updCurrencyMst(String query, Hashtable htCondition, String extCond)
			throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "["+htCondition.get("PLANT")+"_"+TABLE_NAME+"]");
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE ");
			String conditon = formCondition(htCondition);
			sql.append(conditon);

			if (extCond.length() != 0) {
				sql.append(extCond);
			}

			flag = updateData(con, sql.toString());
			this.mLogger.query(this.printQuery, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

		return flag;
	}
	public boolean updateCurrencyMst(Hashtable htUpdate, Hashtable htCondition)
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
					+ "CURRENCYMST" + "] " + sUpdate + sCondition;
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
	
	 public ArrayList getCurrencyList(String plant,String currencydisplay) throws Exception {

	        java.sql.Connection con = null;
	        ArrayList al = new ArrayList();
	        try {
	                con = com.track.gates.DbBean.getConnection();
	                UserLocUtil userLocUtil = new UserLocUtil();
	                userLocUtil.setmLogger(mLogger);
	                         
	               
	                String sQry = "select distinct currencyid as currencyid,description as desc1,display from "
	                                + "["
	                                + plant
	                                + "_"
	                                + "currencymst] where plant='"
	                                + plant
	                                + "' and display like '"+currencydisplay+"%' " 
	                                + " ORDER BY currencyid ";
	                this.mLogger.query(this.printQuery, sQry);
	                System.out.println(sQry);

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
	 

	 public ArrayList getCurrencyListWithcurrencySeq(String plant,String currencydisplay) throws Exception {

	        java.sql.Connection con = null;
	        ArrayList al = new ArrayList();
	        try {
	                con = com.track.gates.DbBean.getConnection();
	                UserLocUtil userLocUtil = new UserLocUtil();
	                userLocUtil.setmLogger(mLogger);
	                         
	               
	                String sQry = "select distinct currencyid as currencyid,description as desc1,display,CURRENCYUSEQT from "
	                                + "["
	                                + plant
	                                + "_"
	                                + "currencymst] where plant='"
	                                + plant
	                                + "' and display like '"+currencydisplay+"%' " 
	                                + " ORDER BY currencyid ";
	                this.mLogger.query(this.printQuery, sQry);
	                System.out.println(sQry);

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
	 
	 public String getCurrencyCode(String aPlant,String aCurrencyDisplay) throws Exception {

			String defaultcurrencyID = "SGD";
			String currencyID = "";

			Hashtable ht = new Hashtable();
			ht.put("PLANT", aPlant);
			ht.put("DISPLAY", aCurrencyDisplay);

			String query = " CURRENCYID ";
			this.mLogger.query(this.printQuery, query);
			Map m = selectRow(query, ht);

			currencyID  = (String) m.get("CURRENCYID");

			try {
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}

			return currencyID;
		}

/*  Created By Bruhan,June 19 2014, To get Mobile Order Currency details
	     *  ************Modification History*********************************
	     * 
*/
  public ArrayList selectmobileordercurrencydetails(String Plant,String display)
		throws Exception {
			
			ArrayList alData = new ArrayList();
			java.sql.Connection con = null;
			String sCondition1="";
			
			String sQry = "SELECT CURRENCYID,DESCRIPTION,DISPLAY,CURRENCYUSEQT,REMARK,STATUS,ISACTIVE FROM "
				+ "["
				+ Plant
				+ "_"
				+ "CURRENCYMST"
				+ "]";
			
			try {
				this.mLogger.query(this.printQuery,sQry.toString());
				con = com.track.gates.DbBean.getConnection();
				alData = selectData(con, sQry.toString());
			
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
  
  public ArrayList getfordropdown(String Plant,String display)
			throws Exception {
				
				ArrayList alData = new ArrayList();
				java.sql.Connection con = null;
				String sCondition1="";
				
				String sQry = "SELECT CURRENCYID,DESCRIPTION,DISPLAY,CURRENCYUSEQT,REMARK,STATUS,ISACTIVE FROM "
					+ "["
					+ Plant
					+ "_"
					+ "CURRENCYMST"
					+ "]";
				
				try {
					this.mLogger.query(this.printQuery,sQry.toString());
					con = com.track.gates.DbBean.getConnection();
					alData = selectData(con, sQry.toString());
				
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
  public ArrayList getcurrencydetails(String CURRENCY, String plant,
			String cond) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			String sQry = "select distinct CURRENCYID,DESCRIPTION,DISPLAY,CURRENCYUSEQT,ISACTIVE from "
					+ "[" + plant + "_" + "CURRENCYMST] where DISPLAY like '"
					+ CURRENCY + "%' " + cond
					+ " ORDER BY CURRENCYID ";
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

public boolean isExistCurrency(String currency, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean isExists = false;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + "CURRENCYMST"
				+ "]" + " WHERE " + IConstants.CURRENCYID + " = '"
				+ currency.toUpperCase() + "'";
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

public boolean isExistTax(String tax, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean isExists = false;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " + "["+ "FINCOUNTRYTAXTYPE"
				+ "]" + " WHERE " + IConstants.TAXTYPE + " = '"
				+ tax.toUpperCase() + "'";
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

public ArrayList getCountryCurrency(String plant,String currency) throws Exception {
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		boolean flag = false;
		String sQry = "SELECT CURRENCYUSEQT FROM "
				+""+plant+"_CURRENCYMST " 
				+ "WHERE CURRENCYID='"+currency+"' ";
	
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
		 

}
