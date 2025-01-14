package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.NamingException;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class VendMstDAO extends BaseDAO {
	public static String TABLE_NAME = "VENDMst";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.VENDMSTDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.VENDMSTDAO_PRINTPLANTMASTERLOG;

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

	public String plant = "";

	public VendMstDAO() {
		StrUtils _StrUtils = new StrUtils();
	}

	public VendMstDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "VENDMst" + "]";
	}

	public String getNextVendCode(String aPlant) throws Exception {
		String defaultCustCode = "S10000000";
		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);

		String query = " ISNULL(max(vendNo),'') as custCode ";

		Map m = selectRow(query, ht);

		custCode = (String) m.get("custCode");

		try {
			if (custCode == "" || custCode == null || custCode.length() <= 0
					|| custCode == "0") {
				return defaultCustCode;
			} else {
				custCode = custCode.replace('S', '0');
				int temp = Integer.parseInt(custCode);
				temp = temp + 1;
				custCode = "S" + String.valueOf(temp);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return custCode;
	}

	public Map selectRow(String query, Hashtable ht) throws Exception {

		Map map = new HashMap();

		java.sql.Connection con = null;

		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ TABLE_NAME);
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			this.mLogger.query(this.printQuery, sql.toString());

			map = getRowOfData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);

			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return map;
	}
	
	/**
	 * method : getCountItemmst(Hashtable ht) description : get the count of
	 * records in Itemmst for the given condition
	 * 
	 * @param : Hashtable ht
	 * @return : int - count
	 * @throws Exception
	 */
	public int getCountVendorMst(Hashtable ht) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iCntItemmst = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key + " = '" + value + "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String sQry = "SELECT COUNT(" + IConstants.VENDNO + ") FROM " + "["
					+ ht.get("PLANT") + "_VENDMST]" + " WHERE "
					+ sCondition;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCntItemmst = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCntItemmst;
	}
	
	public ArrayList getSupplierReport(Hashtable ht,String productDesc,int start,int end) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
	    String extCond = "";
	    String  fdate = "", tdate = "";
	    String custtypecond="";
		
		if (ht.size() > 0) {
			if (ht.get("CUSTTYPE") != null) {
				String custtype = ht.get("CUSTTYPE").toString();
				custtypecond = " AND VNAME in (select VNAME from "+ht.get("C.PLANT")+"_VENDMST where VEND_TYPE_ID like '"+custtype+"%')";
				ht.remove("CUSTTYPE");
			}
		}
		      if (productDesc.length() > 0 ) {
                    if (productDesc.indexOf("%") != -1) {
                            productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
                    }
                       extCond = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
                }
                
		                       
                String condition = "";
            	if (ht.size() > 0) {
   				  condition = formCondition(ht);
    			}
            	 String  sQryCondition = " WHERE ID >="+start+" and ID<="+end+" ";
                /*StringBuffer sql = new StringBuffer(" SELECT DISTINCT ITEM,(select isnull(ITEMDESC,'') from [" + ht.get("PLANT") +"_itemmst] where item=a.item)ITEMDESC,VNAME,'' AS CUSTCODE ");  
                sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "SHIPHIS" + "] A");
                sql.append("  WHERE  DONO<>'MISC-ISSUE' AND STATUS='C' AND " + condition  +  extCond +custtypecond);
               	sql.append(" GROUP BY ITEM,CNAME ");*/ 
               	
                StringBuffer sql= new StringBuffer("SELECT * FROM (SELECT (ROW_NUMBER() OVER ( ORDER BY I.ITEM)) AS ID,isnull(V.VENDNO,'') AS SUPPLIERID,isnull(V.VNAME,'') AS SUPPLIERNAME,isnull(I.ITEMDESC,'') AS ITEMDESCRIPTION,isnull(I.COST,'') AS COST,isnull(I.ITEM,'') AS ITEM,isnull(C.IBDISCOUNT,'') AS DISCOUNT ");
                sql.append(" FROM " + "["+ ht.get("C.PLANT") +"_MULTI_COST_MAPPING ] AS C JOIN ["+ ht.get("C.PLANT") +"_VENDMST ] AS V ON C.VENDNO = V.VENDNO JOIN ["+ ht.get("C.PLANT") +"_ITEMMST] AS I ON C.ITEM = I.ITEM  WHERE " + condition  +  extCond +custtypecond);
                sql.append(") A" +sQryCondition +"ORDER BY SUPPLIERID,ITEM");
		
		try {
			con = com.track.gates.DbBean.getConnection();
		
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
	
	public ArrayList getSupplierCount(Hashtable ht,String productDesc) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
	    String extCond = "";
	    String  fdate = "", tdate = "";
	    String custtypecond="";
		
		if (ht.size() > 0) {
			if (ht.get("CUSTTYPE") != null) {
				String custtype = ht.get("CUSTTYPE").toString();
				custtypecond = " AND VNAME in (select VNAME from "+ht.get("C.PLANT")+"_VENDMST where VENDNO like '"+custtype+"%')";
				ht.remove("CUSTTYPE");
			}
		}
		      if (productDesc.length() > 0 ) {
                    if (productDesc.indexOf("%") != -1) {
                            productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
                    }
                       extCond = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
                }
                
		                       
                String condition = "";
            	if (ht.size() > 0) {
   				  condition = formCondition(ht);
    			}
            
            
               	
                StringBuffer sql= new StringBuffer(" SELECT COUNT(*) AS RECORDS FROM (SELECT isnull(V.VENDNO,'') AS SUPPLIERID,isnull(V.VNAME,'') AS SUPPLIERNAME,isnull(I.ITEMDESC,'') AS ITEMDESCRIPTION,isnull(I.ITEM,'') AS ITEM,isnull(C.IBDISCOUNT,'') AS DISCOUNT ");
                sql.append(" FROM " + "["+ ht.get("C.PLANT") +"_MULTI_COST_MAPPING ] AS C JOIN ["+ ht.get("C.PLANT") +"_VENDMST ] AS V ON C.VENDNO = V.VENDNO JOIN ["+ ht.get("C.PLANT") +"_ITEMMST] AS I ON C.ITEM = I.ITEM  WHERE " + condition  +  extCond +custtypecond);
                sql.append(") A");
		
		try {
			con = com.track.gates.DbBean.getConnection();
		
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
	public ArrayList getSupplierReportforExcel(Hashtable ht,String productDesc) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
	    String extCond = "";
	    String  fdate = "", tdate = "";
	    String custtypecond="";
		
		if (ht.size() > 0) {
			if (ht.get("CUSTTYPE") != null) {
				String custtype = ht.get("CUSTTYPE").toString();
				custtypecond = " AND VNAME in (select VNAME from "+ht.get("C.PLANT")+"_VENDMST where VEND_TYPE_ID like '"+custtype+"%')";
				ht.remove("CUSTTYPE");
			}
		}
		
		      if (productDesc.length() > 0 ) {
                    if (productDesc.indexOf("%") != -1) {
                            productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
                    }
                       extCond = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
                }
                
		                       
                String condition = "";
            	if (ht.size() > 0) {
   				  condition = formCondition(ht);
    			}
            	
                StringBuffer sql= new StringBuffer("SELECT isnull(V.VENDNO,'') AS SUPPLIERID,isnull(V.VNAME,'') AS SUPPLIERNAME,isnull(I.ITEMDESC,'') AS ITEMDESCRIPTION,isnull(I.COST,'') AS COST,isnull(I.ITEM,'') AS ITEM,isnull(C.IBDISCOUNT,'') AS DISCOUNT ");
                sql.append(" FROM " + "["+ ht.get("C.PLANT") +"_MULTI_COST_MAPPING ] AS C JOIN ["+ ht.get("C.PLANT") +"_VENDMST ] AS V ON C.VENDNO = V.VENDNO JOIN ["+ ht.get("C.PLANT") +"_ITEMMST] AS I ON C.ITEM = I.ITEM  WHERE " + condition  +  extCond +custtypecond);
                sql.append("ORDER BY SUPPLIERID,ITEM");
		
		try {
			con = com.track.gates.DbBean.getConnection();
		
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
	
	public String getVendorNameByNo(Hashtable ht)
			throws Exception {
			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			String vendorName = "";
			try {
			con = DbBean.getConnection();
			String query = "SELECT VNAME FROM " + "[" + ht.get("PLANT") + "_VENDMST] "
			+ "WHERE VENDNO=? AND PLANT= ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1,(String) ht.get("VENDNO"));
			stmt.setString(2,(String) ht.get("PLANT"));
			ResultSet rs=stmt.executeQuery();
			while(rs.next()){
				vendorName = rs.getString("VNAME");
			}
			} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
			}finally {
			if (con != null) {
			DbBean.closeConnection(con);
			}
			}
			return vendorName;
			}
	
	public String getVendorbankByNo(Hashtable ht)
			throws Exception {
			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			String vendorName = "";
			try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(BANKNAME,'') BANKNAME FROM " + "[" + ht.get("PLANT") + "_VENDMST] "
			+ "WHERE VENDNO=? AND PLANT= ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1,(String) ht.get("VENDNO"));
			stmt.setString(2,(String) ht.get("PLANT"));
			ResultSet rs=stmt.executeQuery();
			while(rs.next()){
				vendorName = rs.getString("BANKNAME");
			}
			} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
			}finally {
			if (con != null) {
			DbBean.closeConnection(con);
			}
			}
			return vendorName;
			}
	
	public ArrayList getNewSuppliers(String plant) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = "SELECT TOP 5 VENDNO,VNAME,NAME,HPNO,EMAIL FROM ["+plant+"_VENDMST] "
					+ "WHERE ISNULL(CRAT,'') <> '' ORDER BY CAST((SUBSTRING(CRAT, 1, 4) + '-' +" + 
					" SUBSTRING(CRAT, 5, 2) + '-' + SUBSTRING(CRAT, 7, 2)) AS date) DESC";
			al = selectData(con, aQuery.toString());
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
	
	public ArrayList getTotalSupplier(String plant) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = "SELECT COUNT(*) AS TOTAL_SUPPLIERS FROM ["+plant+"_VENDMST]";
			al = selectData(con, aQuery.toString());
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
	
	public ArrayList getTopSupplier(String plant, String fromDate, String toDate, String numberOfDecimal) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = " SELECT TOP 5 A.CUSTCODE,A.CUSTNAME,SUM(CAST((UNITCOST * QTYOR) AS DECIMAL(18,"+numberOfDecimal+")) + CAST(ISNULL((((UNITCOST*INBOUND_GST)/100)*B.QTYOR),0)  AS DECIMAL(18,"+numberOfDecimal+"))) AS TOTAL_COST FROM  " + 
					" ["+plant+"_POHDR] A JOIN ["+plant+"_PODET]B ON A.PONO=B.PONO " + 
					" WHERE CONVERT(DATETIME, CollectionDate, 103) between '"+fromDate+"' and '"+toDate+"'"+
					" GROUP BY A.CUSTCODE,A.CUSTNAME ORDER BY TOTAL_COST DESC";
			al = selectData(con, aQuery.toString());
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
	
	public ArrayList selectForReport(String query) throws Exception {
		boolean flag = false;
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(query);
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
	
	public ArrayList getPurchaseDeliveryDate(String plant) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = "SELECT A.PONO,CUSTNAME,CollectionDate,A.DELDATE,"
					+ "CONVERT(DATE, A.DELDATE, 103) AS DELIVERY_DATE, "
					+"SUM(CAST((UNITCOST * QTYOR) AS DECIMAL(18,4)) + CAST(ISNULL((((UNITCOST*INBOUND_GST)/100)*B.QTYOR),0)  AS DECIMAL(18,4))) AS TOTAL_COST,"
					+"SUM(QTYOR) AS TOTAL_QTY"
					+ " FROM ["+plant+"_POHDR] A JOIN ["+plant+"_PODET] B ON A.PONO=B.PONO  WHERE DELIVERYDATEFORMAT = '1' AND A.STATUS <> 'C' "
					+ "GROUP BY A.PONO,CUSTNAME,CollectionDate,A.DELDATE "
					+ "ORDER BY CONVERT(DATE, COLLECTIONDATE, 103)";
			al = selectData(con, aQuery.toString());
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
	
	public ArrayList getExpiredPurchaseOrder(String plant) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = "SELECT A.PONO,CUSTNAME,CollectionDate,A.DELDATE,"
					+ "CONVERT(DATE, A.DELDATE, 103) AS DELIVERY_DATE, "
					+"SUM(CAST((UNITCOST * QTYOR) AS DECIMAL(18,4)) + CAST(ISNULL((((UNITCOST*INBOUND_GST)/100)*B.QTYOR),0)  AS DECIMAL(18,4))) AS TOTAL_COST"
					+ " FROM ["+plant+"_POHDR] A JOIN ["+plant+"_PODET] B ON A.PONO=B.PONO  WHERE DELIVERYDATEFORMAT = '1' AND CONVERT(DATE, A.DELDATE, 103) < GETDATE() "
					+ "GROUP BY A.PONO,CUSTNAME,CollectionDate,A.DELDATE "
					+ "ORDER BY CONVERT(DATE, COLLECTIONDATE, 103)";
			al = selectData(con, aQuery.toString());
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
	
	public JSONObject getVendorName(String plant, String name) {
		JSONObject mainObj = new JSONObject();
		String query = "SELECT ISNULL(TRANSPORTID,0) TRANSPORT,* FROM [" + plant + "_VENDMST] WHERE VENDNO = '" + name + "'";
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			while (rset.next()) {
				
				mainObj.put("VENDNO", rset.getString("VENDNO"));
				mainObj.put("VNAME", rset.getString("VNAME"));
				mainObj.put("NAME", rset.getString("NAME"));
				mainObj.put("TELNO", rset.getString("TELNO"));
				mainObj.put("ADDR1", rset.getString("ADDR1"));
				mainObj.put("ADDR2", rset.getString("ADDR2"));
				mainObj.put("ADDR3", rset.getString("ADDR3"));
				mainObj.put("TAXTREATMENT", rset.getString("TAXTREATMENT"));
				mainObj.put("CURRENCY_ID", rset.getString("CURRENCY_ID"));
				//mainObj.put("TRANSPORTID", rset.getString("TRANSPORTID"));
				mainObj.put("TRANSPORTID", rset.getString("TRANSPORT"));
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
	
	public String getVendorNoByName(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		String vendorNo = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT VENDNO FROM " + "[" + ht.get("PLANT") + "_VENDMST] "
			+ "WHERE VNAME=? AND PLANT= ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1,(String) ht.get("VNAME"));
			stmt.setString(2,(String) ht.get("PLANT"));
			ResultSet rs=stmt.executeQuery();
			while(rs.next()){
				vendorNo = rs.getString("VENDNO");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
			DbBean.closeConnection(con);
			}
		}
		return vendorNo;
	}
	
	public String gettaxtreatmentByNo(String plant,String vendno)
			throws Exception {
			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			String TAXTREATMENT = "";
			try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(TAXTREATMENT,'') AS TAXTREATMENT FROM " + "[" + plant + "_VENDMST] "
			+ "WHERE VENDNO=? AND PLANT= ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1,plant);
			stmt.setString(2,vendno);
			ResultSet rs=stmt.executeQuery();
			while(rs.next()){
				TAXTREATMENT = rs.getString("TAXTREATMENT");
			}
			} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
			}finally {
			if (con != null) {
			DbBean.closeConnection(con);
			}
			}
			return TAXTREATMENT;
			}
}