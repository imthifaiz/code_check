package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class MerchantBeanDAO extends BaseDAO{
	private boolean printQuery = MLoggerConstant.CUSTOMERBEANDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.CUSTOMERBEANDAO_PRINTPLANTMASTERLOG;
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
	private String tblName = "MERCHANTMST";
	
	public boolean insertIntoMerchant(Hashtable ht) throws Exception {
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
					+ tblName + "]" + " ("
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
	public boolean isExistsMerchant(String merchant, String plant)
	throws Exception {
PreparedStatement ps = null;
ResultSet rs = null;
boolean isExists = false;
Connection con = null;
try {
	con = DbBean.getConnection();
	String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + tblName
			+ "]" + " WHERE " + IConstants.MERCHANT_CODE + " = '"
			+ merchant.toUpperCase() + "'";
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
	public ArrayList getMerchantDetails(String aCustCode, String plant)
	throws Exception {
PreparedStatement ps = null;
ResultSet rs = null;
ArrayList arrCust = new ArrayList();
Connection con = null;
try {
	con = DbBean.getConnection();
	String sQry = "SELECT MERCHANTNO,CNAME,ADDR1,ADDR2,ADDR3,COUNTRY,ZIP,USERFLG1,LNAME,NAME,DESGINATION,TELNO,HPNO,FAX,EMAIL,REMARKS,ADDR4,ISACTIVE FROM "
			+ "["
			+ plant
			+ "_"
			+ tblName
			+ "]"
			+ " WHERE MERCHANTNO = '"
			+ aCustCode + "'";
	this.mLogger.query(this.printQuery, sQry);
	ps = con.prepareStatement(sQry);
	rs = ps.executeQuery();
	while (rs.next()) {
		arrCust.add(0, StrUtils.fString((String) rs.getString(1))); // merchantno
		arrCust.add(1, StrUtils.fString((String) rs.getString(2))); // cname
		arrCust.add(2, StrUtils.fString((String) rs.getString(3))); // Address1
		arrCust.add(3, StrUtils.fString((String) rs.getString(4))); // Address2
		arrCust.add(4, StrUtils.fString((String) rs.getString(5))); // Address3
		arrCust.add(5, StrUtils.fString((String) rs.getString(6))); // country
		arrCust.add(6, StrUtils.fString((String) rs.getString(7))); // zip
		arrCust.add(7, StrUtils.fString((String) rs.getString(8))); // userflg1
		arrCust.add(8, StrUtils.fString((String) rs.getString(9))); // LASTNAME
		arrCust.add(9, StrUtils.fString((String) rs.getString(10)));// name
		arrCust.add(10, StrUtils.fString((String) rs.getString(11)));// designation
		arrCust.add(11, StrUtils.fString((String) rs.getString(12)));// telno
		arrCust.add(12, StrUtils.fString((String) rs.getString(13)));// hpno
		arrCust.add(13, StrUtils.fString((String) rs.getString(14)));// fax
		arrCust.add(14, StrUtils.fString((String) rs.getString(15)));// email
		arrCust.add(15, StrUtils.fString((String) rs.getString(16)));// remarks
		arrCust.add(16, StrUtils.fString((String) rs.getString(17)));// address4
		arrCust.add(17, StrUtils.fString((String) rs.getString(18)));// address4

	}
} catch (Exception e) {
	this.mLogger.exception(this.printLog, "", e);
} finally {
	DbBean.closeConnection(con, ps);
}
return arrCust;
}
	public boolean updateMerchant(Hashtable htUpdate, Hashtable htCondition)
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
			+ tblName + "] " + sUpdate + sCondition;
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
	public ArrayList getMerchantListStartsWithName(String aCustName)
	throws Exception {
PreparedStatement ps = null;
ResultSet rs = null;
ArrayList arrList = new ArrayList();
Connection con = null;
try {
	con = DbBean.getConnection();
	String sQry = "SELECT MERCHANTNO,CNAME,LNAME "+tblName+"   WHERE CNAME LIKE '"
			+ aCustName + "%'";
	this.mLogger.query(this.printQuery, sQry);
	ps = con.prepareStatement(sQry);
	rs = ps.executeQuery();
	while (rs.next()) {
		ArrayList arrLine = new ArrayList();
		arrLine.add(0, StrUtils.fString((String) rs.getString(1))); // merchantno
		arrLine.add(1, StrUtils.fString((String) rs.getString(2))); // cname
		arrLine.add(2, StrUtils.fString((String) rs.getString(3))); // cname

		arrList.add(arrLine);
	}
} catch (Exception e) {
	this.mLogger.exception(this.printLog, "", e);
} finally {
	DbBean.closeConnection(con, ps);
}
return arrList;

}
	public ArrayList getMerchantListStartsWithName(String aCustName,
			String plant) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT MERCHANTNO,CNAME,LNAME,NAME,TELNO,DESGINATION,FAX,EMAIL,HPNO,ADDR1,ADDR2,ADDR3,ADDR4,COUNTRY,ZIP,REMARKS,ISACTIVE FROM "
					+ "["
					+ plant
					+ "_"
					+ tblName
					+ "]"
					+ " WHERE CNAME LIKE '" + aCustName + "%'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString((String) rs.getString(1))); // merchantno
				arrLine.add(1, StrUtils.fString((String) rs.getString(2))); // merchantname
				arrLine.add(2, StrUtils.fString((String) rs.getString(3))); // cname
				arrLine.add(3, StrUtils.fString((String) rs.getString(4))); // merchantdetailname
				arrLine.add(4, StrUtils.fString((String) rs.getString(5))); // telno
				arrLine.add(5, StrUtils.fString((String) rs.getString(6))); // designa
				arrLine.add(6, StrUtils.fString((String) rs.getString(7))); // fax
				arrLine.add(7, StrUtils.fString((String) rs.getString(8))); // email
				arrLine.add(8, StrUtils.fString((String) rs.getString(9))); // hpno
				arrLine.add(9, StrUtils.fString((String) rs.getString(10))); // addr1
				arrLine.add(10, StrUtils.fString((String) rs.getString(11))); // addr2
				arrLine.add(11, StrUtils.fString((String) rs.getString(12))); // addr3
				arrLine.add(12, StrUtils.fString((String) rs.getString(13))); // addr4
				arrLine.add(13, StrUtils.fString((String) rs.getString(14))); // cnty
				arrLine.add(14, StrUtils.fString((String) rs.getString(15))); // zip
				arrLine.add(15, StrUtils.fString((String) rs.getString(16))); // remarks
				arrLine.add(16, StrUtils.fString((String) rs.getString(17))); // isactive
				arrList.add(arrLine);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
	
	//Insert into Customer Returns Summary
	public boolean insertIntoCustretsumry(Hashtable ht) throws Exception {
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
					+ "CUSTRET_SUMMARY" + "]" + " ("
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
	//Merchant Report
	public ArrayList getMerchantList(String query, Hashtable ht) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
				+ " ["+ht.get("PLANT")+"_"+"CUSTRET_SUMMARY]");
		String conditon = "";
		String extCondtn="group by item, itemdesc, custname";
		String ordcondtn = "   order by item";
		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);

			}
			sql.append(extCondtn);
			sql.append(ordcondtn);
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



public ArrayList getMerchantList(String query, Hashtable ht,String fromdt,String todt) throws Exception {
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;
	StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
			+ " ["+ht.get("PLANT")+"_"+"CUSTRET_SUMMARY]");
	String conditon = "";
	String extCondtn="group by item, itemdesc, custname";
	String ordcondtn = "  order by item";
	try {
		sql.append(" WHERE qty>0 and ");
		if(fromdt.length()>0)
		sql.append(" EXPIREDATE > '"+DateUtils.getDateinyyyy_mm_dd(fromdt)+"'" );	
		if(todt.length()>0){
			if(fromdt.length()>0)
				sql.append(" and ");
			sql.append(" EXPIREDATE < '"+DateUtils.getDateinyyyy_mm_dd(todt)+"'"); 
			}	

		con = com.track.gates.DbBean.getConnection();
		if(fromdt.length()>0||todt.length()>0)
			sql.append("  and ");
		if (ht.size() > 0) {
			
			conditon = formCondition(ht);
			sql.append(conditon);

		}
		
		sql.append(extCondtn);
		sql.append(ordcondtn);
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

public ArrayList getMerchantListWithDateFormat(String query, Hashtable ht,String fromdt,String todt) throws Exception {
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;
	StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
			+ " ["+ht.get("PLANT")+"_"+"CUSTRET_SUMMARY]");
	String conditon = "";
	String extCondtn="group by item, itemdesc, custname";
	String ordcondtn = "  order by item";
	try {
		sql.append(" WHERE qty>0 and ");
		if(fromdt.length()>0)
			sql.append(" LTRIM(RTRIM(EXPIREDATE)) <> '' AND EXPIREDATE is not null AND convert(varchar,EXPIREDATE,103) >= convert(varchar,'" + fromdt.toUpperCase() + "',103) ");
		//sql.append(" EXPIREDATE > '"+DateUtils.getDateinyyyy_mm_dd(fromdt)+"'" );	
		if(todt.length()>0){
			if(fromdt.length()>0)
				sql.append(" and ");
			//sql.append(" EXPIREDATE < '"+DateUtils.getDateinyyyy_mm_dd(todt)+"'"); 
			sql.append(" LTRIM(RTRIM(EXPIREDATE)) <> '' AND EXPIREDATE is not null AND convert(varchar,EXPIREDATE,103) <= convert(varchar,'" + todt.toUpperCase() + "',103) ");
			}	

		con = com.track.gates.DbBean.getConnection();
		if(fromdt.length()>0||todt.length()>0)
			sql.append("  and ");
		if (ht.size() > 0) {
			
			conditon = formCondition(ht);
			sql.append(conditon);

		}
		
		sql.append(extCondtn);
		sql.append(ordcondtn);
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

public ArrayList getRevenueList(String query,Hashtable ht,String groupcondn,String fromdt,String todt) throws Exception {
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;
	StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
		+" "+" ["+ht.get("PLANT")+"_"+"SHIPHIS] a,"+ " ["+ht.get("PLANT")+"_"+"CUSTRET_SUMMARY] b");
	String conditon = "";String extCondtn="";
	if(groupcondn.length()>0)
	 extCondtn=groupcondn;
	String ordcondtn = "  order by a.item";
	try {
		sql.append(" WHERE  a.item=b.item and a.batch = b.serialno  ");
		if(fromdt.length()>0)
		sql.append(" and a.EXPIRYDAT > '"+DateUtils.getDateinyyyy_mm_dd(fromdt)+"'" );	
		if(todt.length()>0){
			if(fromdt.length()>0)
				sql.append(" and ");
			sql.append(" a.EXPIRYDAT < '"+DateUtils.getDateinyyyy_mm_dd(todt)+"'"); 
			}	

		con = com.track.gates.DbBean.getConnection();
		ht.remove("PLANT");
		if (ht.size() > 0) {
			sql.append(" and  ");
			conditon = formCondition(ht);
			sql.append(conditon);

		}
		
		sql.append(extCondtn);
		sql.append(ordcondtn);
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

}
