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
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.object.DoHdr;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DoHdrDAO extends BaseDAO {

	StrUtils _StrUtils = null;
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.DOHDRDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.DOHDRDAO_PRINTPLANTMASTERLOG;
	public static String TABLE_HEADER = "DOHDR";

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

	public DoHdrDAO() {
		_StrUtils = new StrUtils();
	}

	public static String TABLE_NAME = "DOHDR";
	public static String plant = "";

	public DoHdrDAO(String plant) {
//		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "DOHDR" + "]";
	}

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "DOHDR" + "]");
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
	public Map selectcustRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "CUSTMST" + "]");
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

	public Map selectRow(String query, Hashtable ht, String extCond)
			throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "DOHDR" + "]");
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				String conditon = formCondition(ht);
				sql.append(conditon);

				if (extCond.length() > 0)
					sql.append(" and " + extCond);
			}else {
				if (extCond.length() > 0)
					sql.append(" WHERE " + extCond);
			}

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
        
    public String getOrderTypeForDO(String aPlant, String aDono) throws Exception {
            String OrderType = "";
            Hashtable<String, String> ht = new Hashtable<>();
            ht.put("PLANT", aPlant);
            ht.put("DONO", aDono);
            String query = " case  ordertype when '' then 'OUTBOUND ORDER' else upper(isnull(ordertype,'')) end  AS  ORDERTYPE ";
            Map m = selectRow(query, ht);
            OrderType = (String) m.get("ORDERTYPE");
            return OrderType;
    }

	public boolean isExisit(String sql) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
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

	public boolean isExisit(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "["+ht.get("PLANT")+"_DOHDR]");
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

	public boolean isExisit(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "["+ht.get("PLANT")+"_DOHDR]");
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
	
	public ArrayList selectdnplDoHdr(String query, Hashtable ht)
			throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "CUSTMST" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.dono ='"
						+ ht.get("DONO") + "' and a.CustCode=b.custno";

				sql.append(conditon);

			}
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

	public ArrayList selectOutGoingDoHdr(String query, Hashtable ht)
			throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "CUSTMST" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.dono ='"
						+ ht.get("DONO") + "' and a.CustCode=b.custno";

				sql.append(conditon);

			}
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

	public ArrayList selectDoHdr(String query, Hashtable ht) throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOHDR" + "]");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				MLogger.log(0, "condition preisent stage 3");
				sql.append(" WHERE ");

				conditon = formCondition(ht);

				sql.append(conditon);

			}
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

	public ArrayList selectDoHdr(String query, Hashtable ht, String extCond)
			throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		
		String plant = (String) ht.get("PLANT");

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ plant + "_" + "DOHDR" + "] D ");
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
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;
	}

	public boolean insertDoHdr(Hashtable ht) throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "DOHDR" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Outbound order created already");
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}

	public boolean update(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + "DOHDR" + "]");
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

	public String getMaxDo(String Plant) throws Exception {
		String MaxDo = "";
		try {
			String query = " max(" + "DoNO" + ")  as DoNO";
			Hashtable ht = new Hashtable();

			ht.put("PLANT", Plant);
			Map m = selectRow(query, ht);
			MaxDo = (String) m.get("DoNO");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return MaxDo;

	}
	public String getCustCode(String Plant,String mobile) throws Exception {
		String MaxDo = "";
		try {
			String query = " CUSTNO ";
			Hashtable ht = new Hashtable();

			ht.put("PLANT", Plant);
			ht.put("USER_ID", mobile);
			Map m = selectcustRow(query, ht);
			MaxDo = (String) m.get("CUSTNO");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return MaxDo;

	}
	public String getNextOrder(String Plant) throws Exception {
		String MaxDo = "";
		try {
			String query = " isnull(max(" + "DoNO" + "),'')  as DoNO";
			Hashtable ht = new Hashtable();
			ht.put("PLANT", Plant);

			String extCond = " substring(DoNo,2,4)='" + DateUtils.getDateYYMM()
					+ "'";
			Map m = selectRow(query, ht, extCond);
			MaxDo = (String) m.get("DoNO");

			if (MaxDo.length() == 0 || MaxDo.equalsIgnoreCase(null)
					|| MaxDo == "") {
				MaxDo = DateUtils.getDateYYMM() + "00000";
			}
			String temp = MaxDo.replace('O', '0');

			int nextNum = Integer.parseInt(temp) + 1;
			MaxDo = String.valueOf(nextNum);

			if (MaxDo.length() < 9)
				MaxDo = "0" + MaxDo;
			MaxDo = "O" + MaxDo;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return MaxDo;
	}

	public boolean insertPckalloc(Hashtable ht) throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO PCKALLOC " + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Outbound order created already");
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}

	public Boolean removeOrder(String plant2, String dono) throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant2 + "_" + "DOHDR] WHERE DONO='"
					+ dono + "' AND PLANT='" + plant2 + "'";
			this.mLogger.query(this.printQuery, sql.toString());
			this.DeleteRow(connection, sql);
			return Boolean.valueOf(true);
		} catch (Exception e) {
			MLogger.log(0, "[ERROR] : " + e);
			return Boolean.valueOf(false);
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
	}
        
        
        
        

	public ArrayList selectDoHdrForOutBound(String query, Hashtable ht,
			String extCond) throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList<>();

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOHDR" + "] a, [" + ht.get("PLANT")
				+ "_DODET] b WHERE a.dono=b.dono AND a.PLANT='"
				+ ht.get("PLANT") + "' ");
//		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (extCond.length() > 0)
				sql.append(" " + extCond);
			sql.append(" " + " group by a.dono, a.CustName, a.collectiondate,a.status,a.Remark1 order by CAST((SUBSTRING(collectiondate, 7, 4) + SUBSTRING(collectiondate, 4, 2) + SUBSTRING(collectiondate, 1, 2)) AS date )desc");
			this.mLogger.query(this.printQuery, sql.toString());
            System.out.println(sql.toString());
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
	
	public ArrayList selectDoHdrForSalesIssue(String query, Hashtable ht,
			String extCond) throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList();

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOHDR" + "] a, [" + ht.get("PLANT")
				+ "_DODET] b WHERE a.dono=b.dono AND a.PLANT='"
				+ ht.get("PLANT") + "' ");
//		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (extCond.length() > 0)
				sql.append(" " + extCond);
			sql.append(" " + " group by a.dono, a.CustName, a.collectiondate,a.status,a.Remark1 order by CAST((SUBSTRING(collectiondate, 7, 4) + SUBSTRING(collectiondate, 4, 2) + SUBSTRING(collectiondate, 1, 2)) AS date )desc");
			this.mLogger.query(this.printQuery, sql.toString());
            System.out.println(sql.toString());
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

	public ArrayList OrderStatus( Hashtable ht,
			String extCond) throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList<Map<String, String>>();
		String orderno="",mobileno="",custcode="";
		java.sql.Connection con = null;
		orderno = (String)ht.get("DONO");
		mobileno = (String)ht.get("MOBILENO");
		if(mobileno.length()>0)
		{
     custcode = getCustCode((String)ht.get("PLANT"), mobileno);
		}
	StringBuffer sql = new StringBuffer(" SELECT TOP 1  DONO,case When STATUS='O' then 'PENDING' WHEN STATUS='N' then 'PENDING' When STATUS='C' then 'ORDER SUCCESSFUL'  ELSE status END STATUS,CustCode,CustName from " + "["
				+ ht.get("PLANT") + "_" + "DOHDR" + "] where PLANT='"
				+ ht.get("PLANT") + "' "+"AND ORDERTYPE='Mobile Order' ");
	//sql.append(" AND STATUS <>'C'");
		if(orderno.length()>0)
			sql.append(" AND DONO='"+orderno+"'");
		if(custcode.length()>0)
			sql.append(" AND CUSTCODE='"+custcode+"'");
		
//		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (extCond.length() > 0)
				sql.append(" " + extCond);
			
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
	
	
	public ArrayList selectCompanyDetails(String query, Hashtable ht,
			String extCond) throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList<>();

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from PLNTMST "
				+ " WHERE PLANT='"
				+ ht.get("PLANT") + "' ");
//		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (extCond.length() > 0)
				sql.append(" " + extCond);
			//sql.append(" " + " group by a.dono, a.CustName, a.Remark1 ");
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
	
	public String getOrderType(String aPlant, String aDono) throws Exception {
		String OrderType = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("DONO", aDono);

		String query = " isnull(OrderType,'') as OrderType ";
		
		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			OrderType = (String) m.get("OrderType");
		} else {
			OrderType = "";
		}
		if (OrderType.equalsIgnoreCase(null) || OrderType.length() == 0) {
			OrderType = "";
		}
		return OrderType;
	}
	
	public String getCurrencyID(String aPlant, String aDono) throws Exception {
		String currencyid = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("DONO", aDono);

		String query = " isnull(Currencyid,'') as currencyid ";
		
		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			currencyid = (String) m.get("currencyid");
		} else {
			currencyid = "";
		}
		if (currencyid.equalsIgnoreCase(null) || currencyid.length() == 0) {
			currencyid = "";
		}
		return currencyid;
	}
	
	public ArrayList getOutBoundOrderCustamerDetailsByWMS(String plant,
			String orderno) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;

			StringBuffer sQry = new StringBuffer(
					"select a.dono,a.custname,");
			sQry
					.append(" isnull(b.name,'') contactname,isnull(b.telno,'') as telno,isnull(b.hpno,'') as hpno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3 ");
			sQry.append(" from  " + "[" + plant + "_" + "dohdr" + "]" + " a,"
					+ "[" + plant + "_" + "custmst" + "]" + " b");
			sQry
					.append(" where a.plant='" + plant + "' and a.dono = '"
							+ orderno
							+ "' and a.STATUS <> 'C' and a.custname=b.cname");

			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "DOHDR";
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

	 public Map getDOReciptHeaderDetails(String plant,String ordertype) throws Exception {

         MLogger.log(1, this.getClass() + " getDOSReciptHeaderDetails()");
         Map m = new HashMap();
         java.sql.Connection con = null;
//         String scondtn ="";
         try {
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer("  SELECT  ");
                 sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM,ISNULL(TERMSDETAILS,'') AS TERMSDETAILS,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE,ISNULL(PRITNWITHHSCODE,'0') AS PRITNWITHHSCODE,ISNULL(PRINTWITHUENNO,'0') AS PRINTWITHUENNO,ISNULL(PRINTWITHCUSTOMERUENNO,'0') AS PRINTWITHCUSTOMERUENNO,ISNULL(PRITNWITHCOO,'0') AS PRITNWITHCOO ,ISNULL(PRINTWITHREMARK1,'0') AS PRINTWITHREMARK1 ,ISNULL(PRINTWITHREMARK2,'0') AS PRINTWITHREMARK2 ,ISNULL(PRINTBARCODE,'0') AS PRINTBARCODE,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,ISNULL(PRINTCUSTERMS,'0') AS PRINTCUSTERMS,ISNULL(PCUSREMARKS,'0') AS PCUSREMARKS,");//
                 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(PROJECT,'') AS PROJECT,ISNULL(UOM,'') AS UOM , ");
                 sql.append("ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2 ,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(FOOTER5,'') AS F5, ");
                 sql.append("ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9,ISNULL(PRODUCTRATESARE,'') AS PRODUCTRATESARE, ");
                 sql.append("ISNULL(CONTAINER,'') Container,ISNULL(DisplayContainer,'0') AS DisplayContainer,ISNULL(PRINTWITHPROJECT,'0') AS PRINTWITHPROJECT,ISNULL(TRANSPORT_MODE,'') AS TRANSPORT_MODE,ISNULL(PRINTWITHTRANSPORT_MODE,'0') AS PRINTWITHTRANSPORT_MODE,ISNULL(PRINTWITHSHIPINGADD,'0') AS PRINTWITHSHIPINGADD,ISNULL(PrintLocStock,'0') AS PrintLocStock, ");
                 sql.append("ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2,ISNULL(BRAND,'') BRAND,ISNULL(HSCODE,'') HSCODE,ISNULL(COO,'') COO,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EMPLOYEE,'') EMPLOYEE,ISNULL(SHIPTO,'') SHIPTO, ");
                 sql.append("ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(PRINTEMPLOYEE,'') PRINTEMPLOYEE,ISNULL(PrintOrientation,'Landscape') PrintOrientation, ");
                 sql.append(" ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM,ISNULL(RCBNO,'') AS RCBNO,ISNULL(PRINTWITHPRODUCTREMARKS,'0') AS PRINTWITHPRODUCTREMARKS,ISNULL(PRINTWITHBRAND,'0') AS PRINTWITHBRAND,ISNULL(CUSTOMERRCBNO,'') AS CUSTOMERRCBNO,ISNULL(UENNO,'') AS UENNO,ISNULL(CUSTOMERUENNO,'') AS CUSTOMERUENNO,ISNULL(PREPAREDBY,'') AS PREPAREDBY,ISNULL(AUTOPOPUPORDERTYPE,'') AS AUTOPOPUPORDERTYPE,ISNULL(AUTOPOPUPDELAY,'0') AS AUTOPOPUPDELAY,ISNULL(ENABLEOUTLETAUTOPRINTPOPUP,'0') AS ENABLEOUTLETAUTOPRINTPOPUP,ISNULL(SELLER,'') AS SELLER,ISNULL(SELLERSIGNATURE,'') AS SELLERSIGNATURE,ISNULL(BUYER,'') AS BUYER,ISNULL(BUYERSIGNATURE,'') AS BUYERSIGNATURE,");
                 sql.append(" ISNULL(PRODUCTDELIVERYDATE,'') AS PRODUCTDELIVERYDATE,ISNULL(PRINTINCOTERM,'0') AS PRINTINCOTERM,ISNULL(PRINTWITHPRODUCTDELIVERYDATE,'0') AS PRINTWITHPRODUCTDELIVERYDATE,ISNULL(PRINTWITHCOMPANYSIG,'0') AS PRINTWITHCOMPANYSIG,ISNULL(PRINTWITHCOMPANYSEAL,'0') AS PRINTWITHCOMPANYSEAL,ISNULL(PRINTWITHDELIVERYDATE,'0') AS PRINTWITHDELIVERYDATE ");
                 
                 sql.append(" FROM " + "[" + plant + "_" + "OUTBOUND_RECIPT_HDR] where plant='"+plant+"'");
                 if(ordertype.length()>0)
                 sql.append(" and ORDERTYPE ='"+ordertype+"'");
          
                 this.mLogger.query(this.printQuery, sql.toString());
                 m = getRowOfData(con, sql.toString());

         } catch (Exception e) {

                 this.mLogger.exception(this.printLog, "", e);
                 throw e;
         } finally {
                 if (con != null) {
                         DbBean.closeConnection(con);
                 }
         }
         return m;

 }
	 
	 public Map getDOReciptHeaderDetailsDO(String plant,String ordertype) throws Exception {
         MLogger.log(1, this.getClass() + " getDOSReciptHeaderDetails()");
         Map m = new HashMap();
         java.sql.Connection con = null;
//         String scondtn ="";
         try {
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer("  SELECT  ");
                 sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(SONO,'') AS SONO,ISNULL(PROJECT,'') AS PROJECT,ISNULL(ITEM,'') AS ITEM,ISNULL(TERMSDETAILS,'') AS TERMSDETAILS,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE ,ISNULL(PRINTBARCODE,'0') AS PRINTBARCODE,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,ISNULL(PRINTCUSTERMS,'0') AS PRINTCUSTERMS,ISNULL(PCUSREMARKS,'0') AS PCUSREMARKS,");//
                 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(UOM,'') AS UOM,ISNULL(PRITNWITHHSCODE,'0') AS PRITNWITHHSCODE,ISNULL(PRITNWITHCOO,'0') AS PRITNWITHCOO,");
                 sql.append("ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2 ,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(FOOTER5,'') AS F5, ");
                 sql.append("ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9,ISNULL(GINO,'') AS GINO,ISNULL(GINODATE,'') AS GINODATE,ISNULL(PRODUCTRATESARE,'') AS PRODUCTRATESARE, ");
                 sql.append("ISNULL(CONTAINER,'') Container,ISNULL(DisplayContainer,'0') AS DisplayContainer,ISNULL(PrintLocStock,'0') AS PrintLocStock,ISNULL(PRINTWITHPROJECT,'0') AS PRINTWITHPROJECT,ISNULL(TRANSPORT_MODE,'') AS TRANSPORT_MODE,ISNULL(PRINTWITHTRANSPORT_MODE,'0') AS PRINTWITHTRANSPORT_MODE,ISNULL(PRINTWITHSHIPINGADD,'0') AS PRINTWITHSHIPINGADD,ISNULL(PRINTWITHUENNO,'0') AS PRINTWITHUENNO,ISNULL(PRINTWITHCUSTOMERUENNO,'0') AS PRINTWITHCUSTOMERUENNO,ISNULL(CAPTURESIGNATURE,'0') AS CAPTURESIGNATURE, ");
                 sql.append("ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2,ISNULL(BRAND,'') BRAND,ISNULL(HSCODE,'') HSCODE,ISNULL(COO,'') COO,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EMPLOYEE,'') EMPLOYEE,ISNULL(SHIPTO,'') SHIPTO, ");
                 sql.append("ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(PRINTEMPLOYEE,'') PRINTEMPLOYEE,ISNULL(DISPLAYSIGNATURE,'') DISPLAYSIGNATURE,ISNULL(PrintOrientation,'Landscape') PrintOrientation, ");
                 sql.append(" ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM,ISNULL(RCBNO,'') AS RCBNO,ISNULL(PRINTPACKINGLIST,'0') AS PRINTPACKINGLIST,ISNULL(PRINTDELIVERYNOTE,'0') AS PRINTDELIVERYNOTE,ISNULL(PRINTWITHPRODUCTREMARKS,'0') AS PRINTWITHPRODUCTREMARKS,ISNULL(PRINTWITHBRAND,'0') AS PRINTWITHBRAND,ISNULL(CUSTOMERRCBNO,'') AS CUSTOMERRCBNO,ISNULL(UENNO,'') AS UENNO,ISNULL(CUSTOMERUENNO,'') AS CUSTOMERUENNO,ISNULL(PREPAREDBY,'') AS PREPAREDBY,ISNULL(SELLER,'') AS SELLER,ISNULL(SELLERSIGNATURE,'') AS SELLERSIGNATURE,ISNULL(BUYER,'') AS BUYER,ISNULL(BUYERSIGNATURE,'') AS BUYERSIGNATURE, ");
                 sql.append(" ISNULL(PRODUCTDELIVERYDATE,'') AS PRODUCTDELIVERYDATE,ISNULL(PRINTWITHPRODUCTDELIVERYDATE,'0') AS PRINTWITHPRODUCTDELIVERYDATE,ISNULL(PRINTINCOTERM,'0') AS PRINTINCOTERM,ISNULL(PRINTWITHDELIVERYDATE,'0') AS PRINTWITHDELIVERYDATE,ISNULL(ISINVENTORYMINQTY,'0') AS ISINVENTORYMINQTY ");
                 sql.append(" FROM " + "[" + plant + "_" + "OUTBOUND_RECIPT_HDR_DO] where plant='"+plant+"'");
                 if(ordertype.length()>0)
                 sql.append(" and ORDERTYPE ='"+ordertype+"'");
          
                 this.mLogger.query(this.printQuery, sql.toString());
                 m = getRowOfData(con, sql.toString());

         } catch (Exception e) {

                 this.mLogger.exception(this.printLog, "", e);
                 throw e;
         } finally {
                 if (con != null) {
                         DbBean.closeConnection(con);
                 }
         }
         return m;

 }
	 	 public Map getDOReciptInvoiceHeaderDetails(String plant,String orderType) throws Exception {
         MLogger.log(1, this.getClass() + " getPOSReciptHeaderDetails()");
         Map m = new HashMap();
         java.sql.Connection con = null;
//         String scondtn ="";
         try {                 
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer("  SELECT  ");
                 sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(TERMS,'') AS TERMS ,ISNULL(TERMSDETAILS,'') AS TERMSDETAILS,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM,ISNULL(PROJECT,'') AS PROJECT,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,ISNULL(PRINTCUSTERMS,'0') AS PRINTCUSTERMS,ISNULL(PCUSREMARKS,'0') AS PCUSREMARKS,");
                 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(RATE,'') AS RATE ,ISNULL(TAXAMOUNT,'') AS TAXAMOUNT,ISNULL(AMT,'') AS AMT, ");
                 sql.append("ISNULL(SUBTOTAL,'') AS SUBTOTAL,ISNULL(TOTALTAX,'') AS TOTALTAX,ISNULL(TOTAL,'Total With Tax') AS TOTAL,ISNULL(UOM,'') AS UOM,ISNULL(REPLACEPREVIOUSSALESCOST,'0') AS REPLACEPREVIOUSSALESCOST,");
                 sql.append("ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2 ,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(FOOTER5,'') AS F5, ");
                 sql.append("ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9,ISNULL(PRODUCTRATESARE,'') AS PRODUCTRATESARE, ");
                 sql.append("ISNULL(CONTAINER,'') Container,ISNULL(DisplayContainer,'0') AS DisplayContainer,ISNULL(ADJUSTMENT,'') AS ADJUSTMENT,ISNULL(PAYMENTMADE,'') AS PAYMENTMADE,ISNULL(BALANCEDUE,'') AS BALANCEDUE,ISNULL(PRINTPAYMENTMADE,'0') AS PRINTPAYMENTMADE,ISNULL(PRINTBALANCEDUE,'0') AS PRINTBALANCEDUE,ISNULL(PRINTADJUSTMENT,'0') AS PRINTADJUSTMENT,ISNULL(PRINTSHIPPINGCOST,'0') AS PRINTSHIPPINGCOST,ISNULL(PRINTORDERDISCOUNT,'0') AS PRINTORDERDISCOUNT, ");
                 sql.append("ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2,ISNULL(BRAND,'') BRAND,ISNULL(HSCODE,'') HSCODE,ISNULL(COO,'') COO,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EMPLOYEE,'') EMPLOYEE,ISNULL(SHIPTO,'') SHIPTO, ");
                 sql.append("ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(PRINTEMPLOYEE,'') PRINTEMPLOYEE,ISNULL(PrintOrientation,'Landscape') PrintOrientation, ");
                 sql.append(" ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM,ISNULL(PRITNWITHHSCODE,'0') AS PRITNWITHHSCODE,ISNULL(PRINTWITHPROJECT,'0') AS PRINTWITHPROJECT,ISNULL(TRANSPORT_MODE,'') AS TRANSPORT_MODE,ISNULL(PRINTWITHTRANSPORT_MODE,'0') AS PRINTWITHTRANSPORT_MODE,ISNULL(PRINTWITHSHIPINGADD,'0') AS PRINTWITHSHIPINGADD,ISNULL(PRITNWITHCOO,'0') AS PRITNWITHCOO,ISNULL(PRINTBUYER,'0') AS PRINTBUYER,ISNULL(PRINTBUYERSIGN,'0') AS PRINTBUYERSIGN,ISNULL(RCBNO,'') AS RCBNO,ISNULL(UENNO,'') AS UENNO,ISNULL(CUSTOMERUENNO,'') AS CUSTOMERUENNO,ISNULL( PRINTWITHPRODUCTREMARKS,'0') AS PRINTWITHPRODUCTREMARKS,ISNULL( PRINTWITHBRAND,'0') AS PRINTWITHBRAND,ISNULL( CUSTOMERRCBNO,'') AS CUSTOMERRCBNO,ISNULL( TOTALAFTERDISCOUNT,'') AS TOTALAFTERDISCOUNT, ");
                 sql.append(" ISNULL(PRINTPACKINGLIST,'0') AS PRINTPACKINGLIST,ISNULL(PRINTDELIVERYNOTE,'0') AS PRINTDELIVERYNOTE,ISNULL(PREPAREDBY,'') AS PREPAREDBY,ISNULL(SELLER,'') AS SELLER,ISNULL(SELLERSIGNATURE,'') AS SELLERSIGNATURE,ISNULL(BUYER,'') AS BUYER,ISNULL(BUYERSIGNATURE,'') AS BUYERSIGNATURE,ISNULL(ROUNDOFFTOTALWITHDECIMAL,'') AS ROUNDOFFTOTALWITHDECIMAL,ISNULL(PRINTROUNDOFFTOTALWITHDECIMAL,'0') AS PRINTROUNDOFFTOTALWITHDECIMAL,ISNULL(PRINTWITHPRODUCT,'0') AS PRINTWITHPRODUCT,ISNULL(PRINTWITHDISCOUNT,'0') AS PRINTWITHDISCOUNT,ISNULL(PRINTINCOTERM,'0') AS PRINTINCOTERM,ISNULL(DISCOUNT,'') DISCOUNT,ISNULL(NETRATE,'') NETRATE, ");
                 sql.append(" ISNULL(PRODUCTDELIVERYDATE,'') AS PRODUCTDELIVERYDATE,ISNULL(PRINTWITHPRODUCTDELIVERYDATE,'0') AS PRINTWITHPRODUCTDELIVERYDATE,ISNULL(PRINTWITHUENNO,'0') AS PRINTWITHUENNO,ISNULL(PRINTWITHCOMPANYSIG,'0') AS PRINTWITHCOMPANYSIG,ISNULL(PRINTWITHCOMPANYSEAL,'0') AS PRINTWITHCOMPANYSEAL,ISNULL(PRINTWITHCUSTOMERUENNO,'0') AS PRINTWITHCUSTOMERUENNO,ISNULL(PRINTWITHDELIVERYDATE,'0') AS PRINTWITHDELIVERYDATE,ISNULL(SHOWPREVIOUSPURCHASECOST,'0') AS SHOWPREVIOUSPURCHASECOST,ISNULL(SHOWPREVIOUSSALESCOST,'0') AS SHOWPREVIOUSSALESCOST,ISNULL(CALCULATETAXWITHSHIPPINGCOST,'0') AS CALCULATETAXWITHSHIPPINGCOST ");
                 sql.append(" FROM " + "[" + plant + "_"+ "OUTBOUND_RECIPT_INVOICE_HDR] where plant='"+plant+"'");
                 if(orderType.length()>0)
                 sql.append(" and ORDERTYPE ='"+orderType+"'");
          
                 this.mLogger.query(this.printQuery, sql.toString());
                 m = getRowOfData(con, sql.toString());

         } catch (Exception e) {

                 this.mLogger.exception(this.printLog, "", e);
                 throw e;
         } finally {
                 if (con != null) {
                         DbBean.closeConnection(con);
                 }
         }
         return m;

 }
	 
	 public Map getDOReciptInvoiceHeaderDetailsDO(String plant,String orderType) throws Exception {

         MLogger.log(1, this.getClass() + " getPOSReciptHeaderDetails()");
         Map<String, String> m = new HashMap<>();
         java.sql.Connection con = null;
//         String scondtn ="";
         try {                 
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer("  SELECT  ");
                 sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(CUSTOMERHEADER,'') AS CUSTOMERHEADER ,ISNULL(COLLECTIONHEADER,'') AS COLLECTIONHEADER ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(TERMS,'') AS TERMS ,ISNULL(TERMSDETAILS,'') AS TERMSDETAILS,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE ,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,ISNULL(PRINTCUSTERMS,'0') AS PRINTCUSTERMS,ISNULL(PRINTBALANCEDUE,'0') AS PRINTBALANCEDUE,ISNULL(PCUSREMARKS,'0') AS PCUSREMARKS,");
                 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(RATE,'') AS RATE ,ISNULL(TAXAMOUNT,'') AS TAXAMOUNT,ISNULL(PROJECT,'') AS PROJECT,ISNULL(AMT,'') AS AMT,ISNULL(PRINTPAYMENTMADE,'0') AS PRINTPAYMENTMADE,ISNULL(PRINTADJUSTMENT,'0') AS PRINTADJUSTMENT,ISNULL(PRINTSHIPPINGCOST,'0') AS PRINTSHIPPINGCOST,ISNULL(PRINTORDERDISCOUNT,'0') AS PRINTORDERDISCOUNT,ISNULL(PRINTINCOTERM,'0') AS PRINTINCOTERM, ");
                 sql.append("ISNULL(SUBTOTAL,'') AS SUBTOTAL,ISNULL(TOTALTAX,'') AS TOTALTAX,ISNULL(TOTAL,'') AS TOTAL,ISNULL(UOM,'') AS UOM,ISNULL(ISAUTOINVOICE,0) ISAUTOINVOICE, ");
                 sql.append("ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2 ,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(FOOTER5,'') AS F5, ");
                 sql.append("ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9,ISNULL(FOOTER10,'') AS F10,ISNULL(FOOTER11,'') AS F11,ISNULL(FOOTER12,'') AS F12,ISNULL(FOOTER13,'') AS F13,ISNULL(PRODUCTRATESARE,'') AS PRODUCTRATESARE,ISNULL(ADJUSTMENT,'') AS ADJUSTMENT, ");
                 sql.append("ISNULL(CONTAINER,'') Container,ISNULL(DisplayContainer,'0') AS DisplayContainer,ISNULL(GINO,'') AS GINO,ISNULL(GINODATE,'') AS GINODATE, ");
                 sql.append("ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2,ISNULL(HSCODE,'') HSCODE,ISNULL(COO,'') COO,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EMPLOYEE,'') EMPLOYEE,ISNULL(SHIPTO,'') SHIPTO, ");
                 sql.append("ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(PRINTEMPLOYEE,'') PRINTEMPLOYEE,ISNULL(DISPLAYSIGNATURE,'') DISPLAYSIGNATURE,ISNULL(PrintOrientation,'Landscape') PrintOrientation,");
                 sql.append("ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST, ISNULL(INCOTERM,'') AS INCOTERM,ISNULL(PRITNWITHHSCODE,'0') AS PRITNWITHHSCODE,ISNULL(PRITNWITHCOO,'0') AS PRITNWITHCOO,ISNULL(RCBNO,'') AS RCBNO,ISNULL(PRINTWITHPRODUCTREMARKS,'0') AS PRINTWITHPRODUCTREMARKS,ISNULL(PRINTWITHBRAND,'0') AS PRINTWITHBRAND,ISNULL(INVOICENO,'') AS INVOICENO,ISNULL(CUSTOMERRCBNO,'') AS CUSTOMERRCBNO,ISNULL(UENNO,'') AS UENNO,ISNULL(CUSTOMERUENNO,'') AS CUSTOMERUENNO,ISNULL(TOTALAFTERDISCOUNT,'') AS TOTALAFTERDISCOUNT,ISNULL(BRAND,'') AS BRAND,ISNULL(TOTALDISCOUNT,'') AS TOTALDISCOUNT,ISNULL(TAX,'') AS TAX,ISNULL(INVOICEDATE,'') AS INVOICEDATE, ");
                 sql.append(" ISNULL(PRINTPACKINGLIST,'0') AS PRINTPACKINGLIST,ISNULL(PRINTWITHPROJECT,'0') AS PRINTWITHPROJECT,ISNULL(TRANSPORT_MODE,'') AS TRANSPORT_MODE,ISNULL(PRINTWITHTRANSPORT_MODE,'0') AS PRINTWITHTRANSPORT_MODE,ISNULL(PRINTWITHSHIPINGADD,'0') AS PRINTWITHSHIPINGADD,ISNULL(PRINTDELIVERYNOTE,'0') AS PRINTDELIVERYNOTE,ISNULL(PREPAREDBY,'') AS PREPAREDBY,ISNULL(SELLER,'') AS SELLER,ISNULL(SELLERSIGNATURE,'') AS SELLERSIGNATURE,ISNULL(BUYER,'') AS BUYER,ISNULL(BUYERSIGNATURE,'') AS BUYERSIGNATURE,ISNULL(PRINTWITHTAXINVOICE,'0') AS PRINTWITHTAXINVOICE,ISNULL(PRINTMULTILANG,'0') AS PRINTMULTILANG,ISNULL(CONTACTNAME,'') AS CONTACTNAME,ISNULL(EMAIL,'') AS EMAIL,ISNULL(FAX,'') AS FAX,ISNULL(TELEPHONE,'') AS TELEPHONE,ISNULL(HANDPHONE,'') AS HANDPHONE,ISNULL(ATTENTION,'') AS ATTENTION,ISNULL(QTYTOTAL,'') AS QTYTOTAL,ISNULL(REMARK3,'') AS REMARK3,ISNULL(FOOTERPAGE,'') AS FOOTERPAGE,ISNULL(FOOTEROF,'') AS FOOTEROF,ISNULL(CASHCUSTOMER,'') AS CASHCUSTOMER,ISNULL(ROUNDOFFTOTALWITHDECIMAL,'') AS ROUNDOFFTOTALWITHDECIMAL,ISNULL(PRINTROUNDOFFTOTALWITHDECIMAL,'0') AS PRINTROUNDOFFTOTALWITHDECIMAL,ISNULL(PRINTWITHPRODUCT,'0') AS PRINTWITHPRODUCT,ISNULL(PRINTWITHDISCOUNT,'0') AS PRINTWITHDISCOUNT,ISNULL(DISCOUNT,'') DISCOUNT,ISNULL(NETRATE,'') NETRATE, ");
                 sql.append(" ISNULL(PRODUCTDELIVERYDATE,'') AS PRODUCTDELIVERYDATE,ISNULL(PRINTWITHPRODUCTDELIVERYDATE,'0') AS PRINTWITHPRODUCTDELIVERYDATE,ISNULL(PRINTWITHCOMPANYSEAL,'0') AS PRINTWITHCOMPANYSEAL,ISNULL(PRINTWITHCOMPANYSIG,'0') AS PRINTWITHCOMPANYSIG,ISNULL(PRINTWITHCUSTNAMEADRRESS,'0') AS PRINTWITHCUSTNAMEADRRESS,ISNULL(DISPLAYSIGNATURE,'0') AS DISPLAYSIGNATURE,ISNULL(PRINTWITHDELIVERYDATE,'0') AS PRINTWITHDELIVERYDATE,ISNULL(PRINTWITHUENNO,'0') AS PRINTWITHUENNO,ISNULL(ISPRINTDEFAULT,'0') AS ISPRINTDEFAULT,ISNULL(PRINTWITHCUSTOMERUENNO,'0') AS PRINTWITHCUSTOMERUENNO,ISNULL(CALCULATETAXWITHSHIPPINGCOST,'0') AS CALCULATETAXWITHSHIPPINGCOST, ");
                 sql.append(" ISNULL(ADJUSTMENT,'') AS ADJUSTMENT,ISNULL(PAYMENTMADE,'') AS PAYMENTMADE,ISNULL(BALANCEDUE,'') AS BALANCEDUE,ISNULL(BRAND,'') AS BRAND,ISNULL(TOTALDISCOUNT,'') AS TOTALDISCOUNT,ISNULL(SHOWPREVIOUSINVOICECOST,'0') AS SHOWPREVIOUSINVOICECOST");
                 sql.append(" FROM " + "[" + plant + "_"+ "OUTBOUND_RECIPT_INVOICE_HDR_DO] where plant='"+plant+"'");
                 if(orderType.length()>0)
                 sql.append(" and ORDERTYPE ='"+orderType+"'");
          
                 this.mLogger.query(this.printQuery, sql.toString());
                 m = getRowOfData(con, sql.toString());

         } catch (Exception e) {

                 this.mLogger.exception(this.printLog, "", e);
                 throw e;
         } finally {
                 if (con != null) {
                         DbBean.closeConnection(con);
                 }
         }
         return m;

 }
	 
	 
		 public boolean updateDOReciptHeader(String query, Hashtable htCondition, String extCond)
	     throws Exception {
				boolean flag = false;
				java.sql.Connection con = null;
			try {
				     con = com.track.gates.DbBean.getConnection();
				     StringBuffer sql = new StringBuffer(" UPDATE " + "["
				                     + htCondition.get("PLANT") + "_OUTBOUND_RECIPT_HDR]");
				     sql.append(" ");
				     sql.append(query);
				     
				     sql.append(" WHERE ");
				     String conditon = formCondition(htCondition);
				     sql.append(conditon);
				     
				    
				
				     if (extCond.length() != 0) {
				             sql.append(extCond);
				     }
				 this.mLogger.query(this.printQuery, sql.toString());
				     flag = updateData(con, sql.toString());
				    
				
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

		 public boolean updateDOReciptHeaderDO(String query, Hashtable htCondition, String extCond)
			     throws Exception {
						boolean flag = false;
						java.sql.Connection con = null;
					try {
						     con = com.track.gates.DbBean.getConnection();
						     StringBuffer sql = new StringBuffer(" UPDATE " + "["
						                     + htCondition.get("PLANT") + "_OUTBOUND_RECIPT_HDR_DO]");
						     sql.append(" ");
						     sql.append(query);
						     
						     sql.append(" WHERE ");
						     String conditon = formCondition(htCondition);
						     sql.append(conditon);
						     
						    
						
						     if (extCond.length() != 0) {
						             sql.append(extCond);
						     }
						 this.mLogger.query(this.printQuery, sql.toString());
						     flag = updateData(con, sql.toString());
						    
						
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




	public boolean updateDOReciptInvoiceHeader(String query, Hashtable htCondition, String extCond)
	throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" UPDATE " + "["
		                + htCondition.get("PLANT") + "_OUTBOUND_RECIPT_INVOICE_HDR]");
		sql.append(" ");
		sql.append(query);
		
		sql.append(" WHERE ");
		String conditon = formCondition(htCondition);
		sql.append(conditon);
		
		
		
		if (extCond.length() != 0) {
		        sql.append(extCond);
		}
		this.mLogger.query(this.printQuery, sql.toString());
		flag = updateData(con, sql.toString());
		
		
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
		public boolean updateDOReciptInvoiceHeaderDO(String query, Hashtable htCondition, String extCond)
			throws Exception {
				boolean flag = false;
				java.sql.Connection con = null;
				try {
				con = com.track.gates.DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" UPDATE " + "["
				                + htCondition.get("PLANT") + "_OUTBOUND_RECIPT_INVOICE_HDR_DO]");
				sql.append(" ");
				sql.append(query);
				
				sql.append(" WHERE ");
				String conditon = formCondition(htCondition);
				sql.append(conditon);
				
				
				
				if (extCond.length() != 0) {
				        sql.append(extCond);
				}
				this.mLogger.query(this.printQuery, sql.toString());
				flag = updateData(con, sql.toString());
				
				
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
		
		
		public String getISINVENTORYMINQTY(String plant) throws Exception {
		    java.sql.Connection con = null;
		    String ISINVENTORYMINQTY = "";
		    try {
		            
		    con = DbBean.getConnection();
		    StringBuffer SqlQuery = new StringBuffer(" select isnull(ISINVENTORYMINQTY,'0') ISINVENTORYMINQTY from ["+plant+"_OUTBOUND_RECIPT_HDR_DO] where plant='"+plant+"' and ORDERTYPE = 'Outbound Order'") ;
		     
		    System.out.println(SqlQuery.toString());
		        Map m = this.getRowOfData(con, SqlQuery.toString());
		        ISINVENTORYMINQTY = (String) m.get("ISINVENTORYMINQTY");

		    } catch (Exception e) {
		            this.mLogger.exception(this.printLog, "", e);
		            throw e;
		    } finally {
		            if (con != null) {
		                    DbBean.closeConnection(con);
		            }
		    }
		    return ISINVENTORYMINQTY;
		}




        public ArrayList selectMobileEnquiryDoHdr(String query, Hashtable ht)
                        throws Exception {
//                boolean flag = false;
                ArrayList alData = new ArrayList();
                java.sql.Connection con = null;
                StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
                                + ht.get("PLANT") + "_" + "DOHDR" + "] A,");
                sql.append("[" + ht.get("PLANT") + "_" + "CUSTMST" + "] B");
                String conditon = "";

                try {
                        con = com.track.gates.DbBean.getConnection();
                        
                        
                    if (ht.size() > 0) {
                            sql.append(" WHERE ");

                            conditon = "a.plant='" + ht.get("PLANT") + "' and a.dono ='"
                                            + ht.get("DONO") + "'  and a.ordertype = '"+ht.get("ORDERTYPE")+"' and  a.CustCode=b.custno";

                            sql.append(conditon);

                    }
                      
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
        
        //Start code added by Bruhan for base currency on Aug 28th 2012.
      //By Bruhan to get unit price based on the Currency ID selected for OBOrder
        public String getUnitCostBasedOnCurIDSelectedForOrder(String aPlant, String doNO,String aItem) throws Exception {
            java.sql.Connection con = null;
            String UnitCostForSelCurrency = "";
            try {
                    
                    con = DbBean.getConnection();
           

            StringBuffer SqlQuery = new StringBuffer(" select CAST(isnull(UnitPrice,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_DOHDR WHERE DONO ='"+doNO+"')) AS DECIMAL(25," + DbBean.NOOFDECIMALPTSFORCURRENCY  + ")) AS UNITCOST ");
            SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

            
            System.out.println(SqlQuery.toString());
                Map m = this.getRowOfData(con, SqlQuery.toString());

                UnitCostForSelCurrency = (String) m.get("UNITCOST");

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            } finally {
                    if (con != null) {
                            DbBean.closeConnection(con);
                    }
            }
            return UnitCostForSelCurrency;
        }
		public String getminSellingUnitCostBasedOnCurIDSelectedForOrder(String aPlant, String doNO,String aItem) throws Exception {
            java.sql.Connection con = null;
            String UnitCostForSelCurrency = "";
            try {
                    
                    con = DbBean.getConnection();
           

            StringBuffer SqlQuery = new StringBuffer(" select CAST(isnull(MINSPRICE,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_DOHDR WHERE DONO ='"+doNO+"')) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY  + ")) AS UNITCOST ");
            SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

            
            System.out.println(SqlQuery.toString());
                Map m = this.getRowOfData(con, SqlQuery.toString());

                UnitCostForSelCurrency = (String) m.get("UNITCOST");

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            } finally {
                    if (con != null) {
                            DbBean.closeConnection(con);
                    }
            }
            return UnitCostForSelCurrency;
        }
		
		public String getminSellingUnitCostBasedOnCurIDSelectedForImportOrder(String aPlant, String currencyID) throws Exception {
            java.sql.Connection con = null;
            String UnitCostForSelCurrency = "";
            try {
            con = DbBean.getConnection();
            StringBuffer SqlQuery = new StringBuffer("SELECT CURRENCYUSEQT as UNITCOST  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"'");
           
            System.out.println(SqlQuery.toString());
            Map m = this.getRowOfData(con, SqlQuery.toString());

            UnitCostForSelCurrency = (String) m.get("UNITCOST");

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            } finally {
                    if (con != null) {
                            DbBean.closeConnection(con);
                    }
            }
            return UnitCostForSelCurrency;
        }

        public String getUnitCostCovertedTolocalCurrency(String aPlant, String doNO,String unitCost) throws Exception {
            java.sql.Connection con = null;
            String UnitCostForSelCurrency = "";
            try {
                    
            	con = DbBean.getConnection();
	            StringBuffer SqlQuery = new StringBuffer(" select cast ("+unitCost+" / CAST((SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_DOHDR WHERE DONO =R.DONO)) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) ");
	            SqlQuery.append(" aS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) AS UNITCOST FROM "+aPlant+"_DOHDR R WHERE DONO='"+doNO+"' ");


           // StringBuffer SqlQuery = new StringBuffer(" select cast ("+unitCost+" * CAST((SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_POHDR WHERE PONO =R.PONO)) AS DECIMAL(20,4))/ ");
            //SqlQuery.append(" (SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST] WHERE  CURRENCYID='"+DbBean.LOCAL_CURRENCY+"') aS DECIMAL(20,4)) AS UNITCOST FROM "+aPlant+"_POHDR R WHERE PONO='"+aPONO+"' ");
            System.out.println(SqlQuery.toString());
                Map m = this.getRowOfData(con, SqlQuery.toString());

                UnitCostForSelCurrency = (String) m.get("UNITCOST");

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            } finally {
                    if (con != null) {
                            DbBean.closeConnection(con);
                    }
            }
            return UnitCostForSelCurrency;
        }
        
	public String getCurrencyUseQT(String plant, String doNO) throws Exception {
		java.sql.Connection con = null;
		String getCurrencyUseQT = "";
		try {

			con = DbBean.getConnection();

			StringBuffer SqlQuery = new StringBuffer(
					" SELECT CURRENCYUSEQT FROM [" + plant + "_"
							+ "CURRENCYMST] where plant='" + plant
							+ "' AND CURRENCYID = (SELECT CURRENCYID from "
							+ plant + "_DOHDR WHERE DONO ='" + doNO + "')");

			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());

			getCurrencyUseQT = (String) m.get("CURRENCYUSEQT");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return getCurrencyUseQT;
	}
	 //End code added by Bruhan for base currency on Aug 28th 2012.
         
         //By Samatha on 20/09/2013
	  public String getUnitCostBasedOnCurIDSelected(String aPlant, String doNO,String dolnno,String aItem) throws Exception {
	      java.sql.Connection con = null;
	      String UnitCostForSelCurrency = "";
	      try {
	              
	              con = DbBean.getConnection();
	     

	      StringBuffer SqlQuery = new StringBuffer(" select CAST(UnitPrice *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_DOHDR WHERE DONO ='"+doNO+"')) AS DECIMAL(20,5)) AS UNITCOST ");
	      SqlQuery.append(" from "+aPlant+"_dodet where item='"+aItem+"' and dono ='"+doNO+"' and dolnno ='"+dolnno+"' ");

	      
	      System.out.println(SqlQuery.toString());
	          Map m = this.getRowOfData(con, SqlQuery.toString());

	          UnitCostForSelCurrency = (String) m.get("UNITCOST");

	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return UnitCostForSelCurrency;
	  }
	  
	  /*---Added by Bruhan on July 14 2014, Description:To get PDA Mobile Sales Outbound order list
		 *******   Modification History
		 * tohdr_update
		 * 
		 * 
		 */ 
	  public ArrayList selectPDAMobileSalesDoHdr_details(String query, Hashtable ht,
				String extCond) throws Exception {
//			boolean flag = false;
			ArrayList alData = new ArrayList();

			java.sql.Connection con = null;

			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ ht.get("PLANT") + "_" + "DOHDR" + "] " 
					+ " WHERE PLANT<>''  ");
			String conditon = "";

			try {
				con = com.track.gates.DbBean.getConnection();
				
				if (ht.size() > 0) {

					sql.append(" AND ");

					conditon = formCondition(ht);

					sql.append(conditon);

				}

				if (extCond.length() > 0)
					sql.append(" " + extCond);
				sql.append(" " + " group by dono,custname,jobnum,status ORDER BY dono desc");
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
	  public ArrayList selectOBIssueList(String query, Hashtable ht, String extCond)
				throws Exception {
//			boolean flag = false;
			ArrayList alData = new ArrayList();
			
			String plant = (String) ht.get("PLANT");

			java.sql.Connection con = null;

			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ plant + "_" + "SHIPHIS" + "] A");
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
				
				//sql.append("ORDER BY INVOICENO DESC");
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
       public String getOBDiscountSelectedItem(String aPlant, String doNO,String aItem,String aType) throws Exception {
           java.sql.Connection con = null;
           String OBDiscount= "";
//           String CustCode="";
           try {
           con = DbBean.getConnection();
//           CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
           String custcode =getCustomerCode(aPlant,doNO,aType);
            
           StringBuffer SqlQuery = new StringBuffer(" SELECT ISNULL(OBDISCOUNT,'0.00') OBDISCOUNT FROM ["+aPlant+"_MULTI_PRICE_MAPPING]  WHERE  ITEM='"+aItem+"' AND CUSTOMER_TYPE_ID IN ");
           SqlQuery.append(" (SELECT CUSTOMER_TYPE_ID FROM ["+aPlant+"_CUSTMST] WHERE CUSTNO  ='" + custcode + "')");
           System.out.println(SqlQuery.toString());
           Map m = this.getRowOfData(con, SqlQuery.toString());
           
           if(m.size()>0)
           {
	            OBDiscount=(String) m.get("OBDISCOUNT");
	            if(OBDiscount.equals("") || OBDiscount.equalsIgnoreCase(null))
	            {
	                OBDiscount="0";
	            }
	            else
	            {
	            	OBDiscount = (String) m.get("OBDISCOUNT");
	            }
           }

           } catch (Exception e) {
                   this.mLogger.exception(this.printLog, "", e);
                   
                   throw e;
           } finally {
                   if (con != null) {
                           DbBean.closeConnection(con);
                   }
           }
           return OBDiscount;
       }
           
       public String getCustomerCode(String plant, String dono,String aType) throws Exception {
 	      java.sql.Connection con = null;
 	      String custcode = "";
 	     String SqlQuery="";
 	      try {
 	      
 	       con = DbBean.getConnection();
 	       if(aType.equals("OUTBOUND"))
 	       {
 	           SqlQuery = " select isnull(custcode,'') custcode FROM ["+plant+"_DOHDR]  WHERE dono='"+dono+"'";
 	       }
 	       else if(aType.equals("ESTIMATE"))
 	       {
 	    	   SqlQuery = " select isnull(custcode,'') custcode FROM ["+plant+"_ESTHDR]  WHERE ESTNO='"+dono+"'";
 	       }
 	      else if(aType.equals("PDAOB"))
	       {
	    	   SqlQuery = " select isnull(custno,'') custcode FROM ["+plant+"_CUSTMST]  WHERE CNAME='"+dono+"'";
	       }
          System.out.println(SqlQuery.toString());
 	       Map m = this.getRowOfData(con, SqlQuery);
 	       custcode = (String) m.get("custcode");
 	       
 	      } catch (Exception e) {
 	              this.mLogger.exception(this.printLog, "", e);
 	              throw e;
 	      } finally {
 	              if (con != null) {
 	                      DbBean.closeConnection(con);
 	              }
 	      }
 	      return custcode;
 	  }
	 	
	
	 	
 public String getMobileSalesUnitCostBasedOnCurIDSelectedForOrder(String aPlant, String doNO,String aItem,String currency) throws Exception {
           java.sql.Connection con = null;
           String UnitCostForSelCurrency = "";
           try {
                   
                   con = DbBean.getConnection();
          

           StringBuffer SqlQuery = new StringBuffer(" select CAST(UnitPrice *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"') AS DECIMAL(20,2)) AS UNITCOST ");
           SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

           
           System.out.println(SqlQuery.toString());
               Map m = this.getRowOfData(con, SqlQuery.toString());

               UnitCostForSelCurrency = (String) m.get("UNITCOST");

           } catch (Exception e) {
                   this.mLogger.exception(this.printLog, "", e);
                   throw e;
           } finally {
                   if (con != null) {
                           DbBean.closeConnection(con);
                   }
           }
           return UnitCostForSelCurrency;
       }
	
   public ArrayList getSignatureOrderStatus(String query, Hashtable ht,
			String extCond) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["	+ ht.get("PLANT") + "_" + "DOHDR" + "]   where dono='"+ht.get("DONO")+"' ");
//		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (extCond.length() > 0)
				sql.append(" " + extCond);
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
	
   public ArrayList selectDoHdrNewOrdersByItem(String query, Hashtable ht,
           String extCond) throws Exception {
//   boolean flag = false;
   ArrayList alData = new ArrayList();
   java.sql.Connection con = null;

   StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
                   + ht.get("PLANT") + "_" + "DOHDR" + "] a, [" + ht.get("PLANT")
                   + "_" + "DODET" + "] b WHERE a.dono=b.dono AND a.PLANT='"+ ht.get("PLANT") + "' AND b.ITEM like'"+ ht.get("ITEM") + "%' ");

//   String conditon = "";

   try {
           con = com.track.gates.DbBean.getConnection();
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
   public String getUnitCostBasedOnCurIDSelectedForOrderWTC(String aPlant, String doNO,String aItem) throws Exception {
       java.sql.Connection con = null;
       String UnitCostForSelCurrency = "";
       try {
               
               con = DbBean.getConnection();
      

       StringBuffer SqlQuery = new StringBuffer(" select isnull(UnitPrice,0) *isnull((SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_DOHDR WHERE DONO ='"+doNO+"')),0) AS UNITCOST ");
       SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

       
       System.out.println(SqlQuery.toString());
           Map m = this.getRowOfData(con, SqlQuery.toString());

           UnitCostForSelCurrency = (String) m.get("UNITCOST");

       } catch (Exception e) {
               this.mLogger.exception(this.printLog, "", e);
               throw e;
       } finally {
               if (con != null) {
                       DbBean.closeConnection(con);
               }
       }
       return UnitCostForSelCurrency;
   }
   public List getOrderDetailsForInvoice(Hashtable ht) throws Exception {
   	java.sql.Connection con = null;
		List<Map<String, String>> ordersList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			
			query = "SELECT A.DONO,B.DOLNNO,(SELECT ISNULL(DISPLAY,'') DISPLAY FROM [" + ht.get("PLANT") + "_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID) CURRENCYID,"
					+"ISNULL(A.OUTBOUND_GST,0) OUTBOUND_GST, ISNULL(A.ORDERDISCOUNT,0) ORDERDISCOUNT, ISNULL(SHIPPINGCOST,0) SHIPPINGCOST,"
					+"B.ITEM,ISNULL(B.UNITPRICE,0)*ISNULL(B.CURRENCYUSEQT,0) UNITPRICE,B.UNITMO,B.QTYOR,C.CATLOGPATH,"
					+ "CAST(ISNULL(B.UNITPRICE,0) * ISNULL(B.CURRENCYUSEQT,0) AS DECIMAL(20,5)) AS CONVCOST"
					+" FROM [" + ht.get("PLANT") + "_DOHDR] A JOIN [" + ht.get("PLANT") + "_DODET] B ON A.DONO = B.DONO "
					+" JOIN [" + ht.get("PLANT") + "_ITEMMST] C ON B.ITEM = C.ITEM "
							+ "WHERE A.PLANT=? AND A.DONO=? ORDER BY DOLNNO";
			
			PreparedStatement ps = con.prepareStatement(query);  
			/*Storing all the query param argument in list sequentially*/
			args.add((String)ht.get("PLANT"));
			args.add((String) ht.get("DONO"));
			this.mLogger.query(this.printQuery, query);			
			ordersList = selectData(ps, args);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}		
		return ordersList;
   }
   
   public List getPreviousOrderDetails(Hashtable ht, String rows) throws Exception {
   	java.sql.Connection con = null;
		List<Map<String, String>> ordersList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="", condition = "";
		List<String> args = null;
		try {
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			
			/*Storing all the query param argument in list sequentially*/
			condition += " WHERE A.PLANT = ? ";
			args.add((String)ht.get("PLANT"));
			if(!((String)ht.get("ITEM")).equalsIgnoreCase("")) {
				condition += " AND B.ITEM = ? ";
				args.add((String)ht.get("ITEM"));
			}
			if(!((String)ht.get("CUSTCODE")).equalsIgnoreCase("")) {
				condition += " AND A.CustCode = ? ";
				args.add((String)ht.get("CUSTCODE"));
			}
			
			query = "SELECT TOP "+ rows +" a.DONO,a.CustCode,c.CNAME,ITEM,CollectionDate,UNITPRICE "
					+ "FROM [" + ht.get("PLANT") + "_DOHDR] a JOIN [" + ht.get("PLANT") + "_DODET] B ON a.DONO = b.DONO"
					+" JOIN [" + ht.get("PLANT") + "_CUSTMST] c on a.CustCode = c.CUSTNO "
					+ condition
					+" ORDER BY CAST((SUBSTRING(CollectionDate,7,4) + '-' + SUBSTRING(CollectionDate,4,2) + '-' + SUBSTRING(CollectionDate,1,2))AS DATE) DESC";

			PreparedStatement ps = con.prepareStatement(query);
			this.mLogger.query(this.printQuery, query);			
			ordersList = selectData(ps, args);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}		
		return ordersList;
   }
   
   public String getUnitCostSelectedForOrder(String aPlant, String doNO,String aItem, String DO_LN_NUM) throws Exception {
       java.sql.Connection con = null;
       String UnitCostForSelCurrency = "";
       try {
               
               con = DbBean.getConnection();
      

       StringBuffer SqlQuery = new StringBuffer("select isnull(unitprice,0) as perunitprice ");
       SqlQuery.append(" from "+aPlant+"_DODET where item='"+aItem+"' and  DONO = '"+doNO+"' AND PLANT = '"+aPlant+"' and dolnno = '"+DO_LN_NUM+"' ");

       
       System.out.println(SqlQuery.toString());
           Map m = this.getRowOfData(con, SqlQuery.toString());

           UnitCostForSelCurrency = (String) m.get("perunitprice");
           UnitCostForSelCurrency= Double.toString(StrUtils.RoundDB(Double.parseDouble(UnitCostForSelCurrency),7));
       } catch (Exception e) {
               this.mLogger.exception(this.printLog, "", e);
               throw e;
       } finally {
               if (con != null) {
                       DbBean.closeConnection(con);
               }
       }
       return UnitCostForSelCurrency;
   }
   
   public String getUnitCostSelectedForOrderWithoutLineno(String aPlant, String doNO,String aItem) throws Exception {
       java.sql.Connection con = null;
       String UnitCostForSelCurrency = "";
       try {               
    	   con = DbBean.getConnection();
	       StringBuffer SqlQuery = new StringBuffer("select isnull(unitprice,0) as perunitprice ");
	       SqlQuery.append(" from "+aPlant+"_DODET where item='"+aItem+"' and  DONO = '"+doNO+"' AND PLANT = '"+aPlant+"' ");

       System.out.println(SqlQuery.toString());
           Map m = this.getRowOfData(con, SqlQuery.toString());

           UnitCostForSelCurrency = (String) m.get("perunitprice");
           UnitCostForSelCurrency= Double.toString(StrUtils.RoundDB(Double.parseDouble(UnitCostForSelCurrency),7));
       } catch (Exception e) {
               this.mLogger.exception(this.printLog, "", e);
               throw e;
       } finally {
               if (con != null) {
                       DbBean.closeConnection(con);
               }
       }
       return UnitCostForSelCurrency;
   }

   public ArrayList selectForReport(String query) throws Exception {
//		boolean flag = false;
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
   
   public ArrayList getSalesOrderDeliveryDate(String plant, String numberOfDecimal) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = "SELECT A.DONO,CUSTNAME,CollectionDate,A.DELIVERYDATE,"
					+ "CONVERT(DATE, A.DELIVERYDATE, 103) AS DELIVERY_DATE, "
					+"SUM(CAST((UNITPRICE * QTYOR) AS DECIMAL(18,"+numberOfDecimal+")) + CAST(ISNULL((((UNITPRICE*OUTBOUND_GST)/100)*B.QTYOR),0)  AS DECIMAL(18,"+numberOfDecimal+"))) AS TOTAL_PRICE"
					+ " FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO  WHERE DELIVERYDATEFORMAT = '1' AND A.STATUS <> 'C' "
					+ "GROUP BY A.DONO,CUSTNAME,CollectionDate,A.DELIVERYDATE "
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
   
   public ArrayList getTopSalesItem(String plant, String fromDate, String toDate, String numberOfDecimal) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = "SELECT  TOP 5 B.ITEM,"
					+ " SUM(CAST((UNITPRICE * QTYOR) AS DECIMAL(18,"+numberOfDecimal+")) + CAST(ISNULL((((UNITPRICE*OUTBOUND_GST)/100)*B.QTYOR),0)  AS DECIMAL(18,"+numberOfDecimal+"))) AS TOTAL_PRICE "
					+ " FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO "
					+ " WHERE CONVERT(DATETIME, CollectionDate, 103) between '"+fromDate+"' and '"+toDate+"' "
					+ " GROUP BY B.ITEM "
					+ " ORDER BY TOTAL_PRICE DESC,B.ITEM";
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
   
   public ArrayList getSalesOrderExpiredDeliveryDate(String plant, String numberOfDecimal) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = "SELECT A.DONO,CUSTNAME,CollectionDate,A.DELIVERYDATE,"
					+ "CONVERT(DATE, A.DELIVERYDATE, 103) AS DELIVERY_DATE, "
					+"SUM(QTYOR) AS TOTAL_QTY"
					+ " FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO  WHERE DELIVERYDATEFORMAT = '1' AND CONVERT(DATE, A.DELIVERYDATE, 103) < GETDATE() AND A.STATUS <> 'C' "
					+ "GROUP BY A.DONO,CUSTNAME,CollectionDate,A.DELIVERYDATE "
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
   public Map selectSignRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "OUTBOUND_RECIPT_HDR_DO" + "]");
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
   public String getSignatureCheck(String aPlant, String aItem) throws Exception {
		String capturesignature = "";
		String aValue ="Outbound Order";
		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("ORDERTYPE", aValue);
		
		String query = " isnull(CAPTURESIGNATURE,'') as CAPTURESIGNATURE ";
		Map m = selectSignRow(query, ht);
		//Map m = selectSignRow( query,ht " (SELECT CAPTURESIGNATURE FROM ["+aPlant+"test_OUTBOUND_RECIPT_HDR_DO] WHERE plant='"+aPlant+"' AND ORDERTYPE = '"+aValue+"')");
		if (m.size() > 0) {
			capturesignature = (String) m.get("CAPTURESIGNATURE");
		} else {
			capturesignature = "";
		}
		if (capturesignature.equalsIgnoreCase(null) || capturesignature.length() == 0) {
			capturesignature = "";
		}
		return capturesignature;
	}
   
   public ArrayList getLastTranDoDetail(Hashtable ht) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		String plant = (String) ht.get("a.PLANT");
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer("SELECT TOP 1 * FROM ["+plant+"_DOHDR] a join ["+plant+"_DODET] b on a.DONO = b.DONO");
		String conditon = "";
		try {
			con = com.track.gates.DbBean.getConnection();
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			sql.append(" ORDER BY CollectionDate DESC");
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
   
   public String getUnitCostBasedOnCurIDSelectedForOrderByCurrency(String aPlant, String currencyID,String aItem) throws Exception {
       java.sql.Connection con = null;
       String UnitCostForSelCurrency = "";
       try {
               
               con = DbBean.getConnection();
      

       StringBuffer SqlQuery = new StringBuffer(" select CAST(isnull(UnitPrice,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"') AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY  + ")) AS UNITCOST ");
       SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

       
       System.out.println(SqlQuery.toString());
           Map m = this.getRowOfData(con, SqlQuery.toString());

           UnitCostForSelCurrency = (String) m.get("UNITCOST");

       } catch (Exception e) {
               this.mLogger.exception(this.printLog, "", e);
               throw e;
       } finally {
               if (con != null) {
                       DbBean.closeConnection(con);
               }
       }
       return UnitCostForSelCurrency;
   }
   
   public String getUnitCostsalesBasedOnCurIDSelectedForOrderByCurrency(String aPlant, String currencyID,String aItem) throws Exception {
	   java.sql.Connection con = null;
	   String UnitCostForSelCurrency = "";
	   try {
		   
		   con = DbBean.getConnection();
		   
		   
		   StringBuffer SqlQuery = new StringBuffer(" select CAST(isnull(COST,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"') AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY  + ")) AS UNITCOST ");
		   SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");
		   
		   
		   System.out.println(SqlQuery.toString());
		   Map m = this.getRowOfData(con, SqlQuery.toString());
		   
		   UnitCostForSelCurrency = (String) m.get("UNITCOST");
		   
	   } catch (Exception e) {
		   this.mLogger.exception(this.printLog, "", e);
		   throw e;
	   } finally {
		   if (con != null) {
			   DbBean.closeConnection(con);
		   }
	   }
	   return UnitCostForSelCurrency;
   }
   public String getUnitCostBasedOnCurIDSelectedForOrderWTCByCurrency(String aPlant, String currencyID,String aItem) throws Exception {
       java.sql.Connection con = null;
       String UnitCostForSelCurrency = "";
       try {
               
               con = DbBean.getConnection();
      

       StringBuffer SqlQuery = new StringBuffer(" select isnull(UnitPrice,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')) AS UNITCOST ");
       SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

       
       System.out.println(SqlQuery.toString());
           Map m = this.getRowOfData(con, SqlQuery.toString());

           UnitCostForSelCurrency = (String) m.get("UNITCOST");

       } catch (Exception e) {
               this.mLogger.exception(this.printLog, "", e);
               throw e;
       } finally {
               if (con != null) {
                       DbBean.closeConnection(con);
               }
       }
       return UnitCostForSelCurrency;
   }
   public String getminSellingUnitCostBasedOnCurIDSelectedForOrderByCurrency(String aPlant, String currencyID,String aItem) throws Exception {
       java.sql.Connection con = null;
       String UnitCostForSelCurrency = "";
       try {
               
               con = DbBean.getConnection();
      

       StringBuffer SqlQuery = new StringBuffer(" select CAST(isnull(MINSPRICE,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"') AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY  + ")) AS UNITCOST ");
       SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

       
       System.out.println(SqlQuery.toString());
           Map m = this.getRowOfData(con, SqlQuery.toString());

           UnitCostForSelCurrency = (String) m.get("UNITCOST");

       } catch (Exception e) {
               this.mLogger.exception(this.printLog, "", e);
               throw e;
       } finally {
               if (con != null) {
                       DbBean.closeConnection(con);
               }
       }
       return UnitCostForSelCurrency;
   }

   public String getOBDiscountSelectedItemByCustomer(String aPlant, String custcode,String aItem,String aType) throws Exception {
       java.sql.Connection con = null;
       String OBDiscount= "";
//       String CustCode="";
       try {
       con = DbBean.getConnection();
        
       StringBuffer SqlQuery = new StringBuffer(" SELECT ISNULL(OBDISCOUNT,'0.00') OBDISCOUNT FROM ["+aPlant+"_MULTI_PRICE_MAPPING]  WHERE  ITEM='"+aItem+"' AND CUSTOMER_TYPE_ID IN ");
       SqlQuery.append(" (SELECT CUSTOMER_TYPE_ID FROM ["+aPlant+"_CUSTMST] WHERE CUSTNO  ='" + custcode + "')");
       System.out.println(SqlQuery.toString());
       Map m = this.getRowOfData(con, SqlQuery.toString());
       
       if(m.size()>0)
       {
            OBDiscount=(String) m.get("OBDISCOUNT");
            if(OBDiscount.equals("") || OBDiscount.equalsIgnoreCase(null))
            {
                OBDiscount="0";
            }
            else
            {
            	OBDiscount = (String) m.get("OBDISCOUNT");
            }
       }

       } catch (Exception e) {
               this.mLogger.exception(this.printLog, "", e);
               
               throw e;
       } finally {
               if (con != null) {
                       DbBean.closeConnection(con);
               }
       }
       return OBDiscount;
   }
   
   public String getInvoiceConvertedUnitCostForProductByCurrency(String aPlant, String currencyID,String aItem,String aUnitPrice) throws Exception {
       java.sql.Connection con = null;
       String UnitCostForSelCurrency = "";
       try {
               
               con = DbBean.getConnection();
      

       StringBuffer SqlQuery = new StringBuffer(" select CAST(isnull("+aUnitPrice+",0) *CAST((SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"') AS float) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY  + ")) AS UNITCOST ");
       SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

       
       System.out.println(SqlQuery.toString());
           Map m = this.getRowOfData(con, SqlQuery.toString());

           UnitCostForSelCurrency = (String) m.get("UNITCOST");

       } catch (Exception e) {
               this.mLogger.exception(this.printLog, "", e);
               throw e;
       } finally {
               if (con != null) {
                       DbBean.closeConnection(con);
               }
       }
       return UnitCostForSelCurrency;
   }
   
   public String getInvoiceConvertedUnitCostForProductWTC(String aPlant, String doNO,String aItem,String aUnitPrice) throws Exception {
       java.sql.Connection con = null;
       String UnitCostForSelCurrency = "";
       try {
               
               con = DbBean.getConnection();
      

       StringBuffer SqlQuery = new StringBuffer(" select CAST( isnull("+aUnitPrice+",0) AS FLOAT)  *isnull((SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_DOHDR WHERE DONO ='"+doNO+"')),0) AS UNITCOST ");
       SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

       
       System.out.println(SqlQuery.toString());
           Map m = this.getRowOfData(con, SqlQuery.toString());

           UnitCostForSelCurrency = (String) m.get("UNITCOST");

       } catch (Exception e) {
               this.mLogger.exception(this.printLog, "", e);
               throw e;
       } finally {
               if (con != null) {
                       DbBean.closeConnection(con);
               }
       }
       return UnitCostForSelCurrency;
   }
   
   public boolean insertDoHdr(DoHdr doHdr) throws Exception {
		boolean insertFlag = false;
//		boolean flag = false;
//		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+doHdr.getPLANT()+"_"+TABLE_HEADER+"]" + 
					"           ([PLANT]" + 
					"           ,[DONO]" + 
					"           ,[ORDERTYPE]" + 
					"           ,[DELDATE]" + 
					"           ,[STATUS]" + 
					"           ,[PickStaus]" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]" + 
					"           ,[CustCode]" + 
					"           ,[CustName]" + 
					"           ,[JobNum]" + 
					"           ,[PersonInCharge]" + 
					"           ,[contactNum]" + 
					"           ,[Address]" + 
					"           ,[Address2]" + 
					"           ,[Address3]" + 
					"           ,[CollectionDate]" + 
					"           ,[CollectionTime]" + 
					"           ,[Remark1]" + 
					"           ,[Remark2]" + 
					"           ,[SHIPPINGID]" + 
					"           ,[SHIPPINGCUSTOMER]" + 
					"           ,[CURRENCYID]" + 
					"           ,[DELIVERYDATE]" + 
					"           ,[TIMESLOTS]" + 
					"           ,[OUTBOUND_GST]" + 
					"           ,[STATUS_ID]" + 
					"           ,[EMPNO]" + 
					"           ,[ESTNO]" + 
					"           ,[Remark3]" + 
					"           ,[ORDERDISCOUNT]" + 
					"           ,[SHIPPINGCOST]" + 
					"           ,[INCOTERMS]" + 
					"           ,[PAYMENTTYPE]" + 
					"           ,[DELIVERYDATEFORMAT]" + 
					"           ,[APPROVESTATUS]" + 
					"           ,[SALES_LOCATION]" + 
					"           ,[ORDER_STATUS]" +
					"           ,[DISCOUNT]" +
					"           ,[DISCOUNT_TYPE]" +
					"           ,[ADJUSTMENT]" +
					"           ,[ITEM_RATES]" +
					"           ,[CURRENCYUSEQT]" +
					"           ,[ORDERDISCOUNTTYPE]" +
					"           ,[TAXID]" +
					"           ,[ISDISCOUNTTAX]" +
					"           ,[ISORDERDISCOUNTTAX]" +
					"           ,[ISSHIPPINGTAX]" +
					"           ,[PROJECTID]" +
					"           ,[TRANSPORTID]" +
					"           ,[PAYMENT_TERMS]" +
					"           ,[SHIPCONTACTNAME]" +
					"           ,[SHIPDESGINATION]" +
					"           ,[SHIPWORKPHONE]" +
					"           ,[SHIPHPNO]" +
					"           ,[SHIPEMAIL]" +
					"           ,[SHIPCOUNTRY]" +
					"           ,[SHIPADDR1]" +
					"           ,[SHIPADDR2]" +
					"           ,[SHIPADDR3]" +
					"           ,[SHIPADDR4]" +
					"           ,[SHIPSTATE]" +
					"           ,[SHIPZIP]" +
					"           ,[APP_CUST_ORDER_STATUS]" +
					"           ,[UKEY]" +
					"           ,[REASON]" +
					"           ,[ISFROMPEPPOL]" +
					"           ,[SALESORDERUUID]" +
					"           ,[ISINVOICED]" +
					"           ,[INVOICEDID]" +
					"           ,[TAXTREATMENT]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
			   ps = connection.prepareStatement(query);
			   ps.setString(1, doHdr.getPLANT());
			   ps.setString(2, doHdr.getDONO());
			   ps.setString(3, doHdr.getORDERTYPE());
			   ps.setString(4, doHdr.getDELDATE());
			   ps.setString(5, doHdr.getSTATUS());
			   ps.setString(6, doHdr.getPickStaus());
			   ps.setString(7, doHdr.getCRAT());
			   ps.setString(8, doHdr.getCRBY());
			   ps.setString(9, doHdr.getCustCode());
			   ps.setString(10, doHdr.getCustName());
			   ps.setString(11, doHdr.getJobNum());
			   ps.setString(12, doHdr.getPersonInCharge());
			   ps.setString(13, doHdr.getContactNum());
			   ps.setString(14, doHdr.getAddress());
			   ps.setString(15, doHdr.getAddress2());
			   ps.setString(16, doHdr.getAddress3());
			   ps.setString(17, doHdr.getCollectionDate());
			   ps.setString(18, doHdr.getCollectionTime());
			   ps.setString(19, doHdr.getRemark1());
			   ps.setString(20, doHdr.getRemark2());
			   ps.setString(21, doHdr.getSHIPPINGID());
			   ps.setString(22, doHdr.getSHIPPINGCUSTOMER());
			   ps.setString(23, doHdr.getCURRENCYID());
			   ps.setString(24, doHdr.getDELIVERYDATE());
			   ps.setString(25, doHdr.getTIMESLOTS());
			   ps.setString(26, Double.toString(doHdr.getOUTBOUND_GST()));
			   ps.setString(27, doHdr.getSTATUS_ID());
			   ps.setString(28, doHdr.getEMPNO());
			   ps.setString(29, doHdr.getESTNO());
			   ps.setString(30, doHdr.getRemark3());
			   ps.setString(31, Double.toString(doHdr.getORDERDISCOUNT()));
			   ps.setString(32, Double.toString(doHdr.getSHIPPINGCOST()));
			   ps.setString(33, doHdr.getINCOTERMS());
			   ps.setString(34, doHdr.getPAYMENTTYPE());
			   ps.setString(35, Short.toString(doHdr.getDELIVERYDATEFORMAT()));
			   ps.setString(36, doHdr.getAPPROVESTATUS());
			   ps.setString(37, doHdr.getSALES_LOCATION());
			   ps.setString(38, doHdr.getORDER_STATUS());
			   ps.setString(39, Double.toString(doHdr.getDISCOUNT()));
			   ps.setString(40, doHdr.getDISCOUNT_TYPE());
			   ps.setString(41, Double.toString(doHdr.getADJUSTMENT()));
			   ps.setString(42, Short.toString(doHdr.getITEM_RATES()));
			   ps.setDouble(43, doHdr.getCURRENCYUSEQT());
			   ps.setString(44, doHdr.getORDERDISCOUNTTYPE());
			   ps.setInt(45, doHdr.getTAXID());
			   ps.setShort(46, doHdr.getISDISCOUNTTAX());
			   ps.setShort(47, doHdr.getISORDERDISCOUNTTAX());
			   ps.setShort(48, doHdr.getISSHIPPINGTAX());
			   ps.setInt(49, doHdr.getPROJECTID());
			   ps.setInt(50, doHdr.getTRANSPORTID());
			   ps.setString(51, doHdr.getPAYMENT_TERMS());
			   ps.setString(52, doHdr.getSHIPCONTACTNAME());
				ps.setString(53, doHdr.getSHIPDESGINATION());
				ps.setString(54, doHdr.getSHIPWORKPHONE());
				ps.setString(55, doHdr.getSHIPHPNO());
				ps.setString(56, doHdr.getSHIPEMAIL());
				ps.setString(57, doHdr.getSHIPCOUNTRY());
				ps.setString(58, doHdr.getSHIPADDR1());
				ps.setString(59, doHdr.getSHIPADDR2());
				ps.setString(60, doHdr.getSHIPADDR3());
				ps.setString(61, doHdr.getSHIPADDR4());
				ps.setString(62, doHdr.getSHIPSTATE());
				ps.setString(63, doHdr.getSHIPZIP());
				ps.setString(64, doHdr.getAPP_CUST_ORDER_STATUS());
				ps.setString(65, doHdr.getUKEY());
				ps.setString(66, doHdr.getREASON());
				ps.setShort(67, doHdr.getISFROMPEPPOL());
				ps.setString(68, doHdr.getSALESORDERUUID());
				ps.setShort(69, doHdr.getISINVOICED());
				ps.setString(70, doHdr.getINVOICEDID());
			    ps.setString(71, doHdr.getTAXTREATMENT());
			  
			   int count=ps.executeUpdate();
			   if(count>0)
			   {
				   insertFlag = true;
			   }
			   else
			   {
				   throw new SQLException("Creating Sales Order failed, no rows affected.");
			   }
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
		return insertFlag;
	}
   
   public int addDoHdrReturnKey(DoHdr doHdr) throws Exception {
		int dohdrkey = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+doHdr.getPLANT()+"_"+TABLE_HEADER+"]" + 
					"           ([PLANT]" + 
					"           ,[DONO]" + 
					"           ,[ORDERTYPE]" + 
					"           ,[DELDATE]" + 
					"           ,[STATUS]" + 
					"           ,[PickStaus]" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]" + 
					"           ,[CustCode]" + 
					"           ,[CustName]" + 
					"           ,[JobNum]" + 
					"           ,[PersonInCharge]" + 
					"           ,[contactNum]" + 
					"           ,[Address]" + 
					"           ,[Address2]" + 
					"           ,[Address3]" + 
					"           ,[CollectionDate]" + 
					"           ,[CollectionTime]" + 
					"           ,[Remark1]" + 
					"           ,[Remark2]" + 
					"           ,[SHIPPINGID]" + 
					"           ,[SHIPPINGCUSTOMER]" + 
					"           ,[CURRENCYID]" + 
					"           ,[DELIVERYDATE]" + 
					"           ,[TIMESLOTS]" + 
					"           ,[OUTBOUND_GST]" + 
					"           ,[STATUS_ID]" + 
					"           ,[EMPNO]" + 
					"           ,[ESTNO]" + 
					"           ,[Remark3]" + 
					"           ,[ORDERDISCOUNT]" + 
					"           ,[SHIPPINGCOST]" + 
					"           ,[INCOTERMS]" + 
					"           ,[PAYMENTTYPE]" + 
					"           ,[DELIVERYDATEFORMAT]" + 
					"           ,[APPROVESTATUS]" + 
					"           ,[SALES_LOCATION]" + 
					"           ,[ORDER_STATUS]" +
					"           ,[DISCOUNT]" +
					"           ,[DISCOUNT_TYPE]" +
					"           ,[ADJUSTMENT]" +
					"           ,[ITEM_RATES]" +
					"           ,[CURRENCYUSEQT]" +
					"           ,[ORDERDISCOUNTTYPE]" +
					"           ,[TAXID]" +
					"           ,[ISDISCOUNTTAX]" +
					"           ,[ISORDERDISCOUNTTAX]" +
					"           ,[ISSHIPPINGTAX]" +
					"           ,[PROJECTID]" +
					"           ,[TRANSPORTID]" +
					"           ,[PAYMENT_TERMS]" +
					"           ,[SHIPCONTACTNAME]" +
					"           ,[SHIPDESGINATION]" +
					"           ,[SHIPWORKPHONE]" +
					"           ,[SHIPHPNO]" +
					"           ,[SHIPEMAIL]" +
					"           ,[SHIPCOUNTRY]" +
					"           ,[SHIPADDR1]" +
					"           ,[SHIPADDR2]" +
					"           ,[SHIPADDR3]" +
					"           ,[SHIPADDR4]" +
					"           ,[SHIPSTATE]" +
					"           ,[SHIPZIP]" +
					"           ,[APP_CUST_ORDER_STATUS]" +
					"           ,[UKEY]" +
					"           ,[REASON]" +
					"           ,[ISFROMPEPPOL]" +
					"           ,[SALESORDERUUID]" +
					"           ,[ISINVOICED]" +
					"           ,[INVOICEDID]" +
					"           ,[TAXTREATMENT]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			   ps.setString(1, doHdr.getPLANT());
			   ps.setString(2, doHdr.getDONO());
			   ps.setString(3, doHdr.getORDERTYPE());
			   ps.setString(4, doHdr.getDELDATE());
			   ps.setString(5, doHdr.getSTATUS());
			   ps.setString(6, doHdr.getPickStaus());
			   ps.setString(7, doHdr.getCRAT());
			   ps.setString(8, doHdr.getCRBY());
			   ps.setString(9, doHdr.getCustCode());
			   ps.setString(10, doHdr.getCustName());
			   ps.setString(11, doHdr.getJobNum());
			   ps.setString(12, doHdr.getPersonInCharge());
			   ps.setString(13, doHdr.getContactNum());
			   ps.setString(14, doHdr.getAddress());
			   ps.setString(15, doHdr.getAddress2());
			   ps.setString(16, doHdr.getAddress3());
			   ps.setString(17, doHdr.getCollectionDate());
			   ps.setString(18, doHdr.getCollectionTime());
			   ps.setString(19, doHdr.getRemark1());
			   ps.setString(20, doHdr.getRemark2());
			   ps.setString(21, doHdr.getSHIPPINGID());
			   ps.setString(22, doHdr.getSHIPPINGCUSTOMER());
			   ps.setString(23, doHdr.getCURRENCYID());
			   ps.setString(24, doHdr.getDELIVERYDATE());
			   ps.setString(25, doHdr.getTIMESLOTS());
			   ps.setString(26, Double.toString(doHdr.getOUTBOUND_GST()));
			   ps.setString(27, doHdr.getSTATUS_ID());
			   ps.setString(28, doHdr.getEMPNO());
			   ps.setString(29, doHdr.getESTNO());
			   ps.setString(30, doHdr.getRemark3());
			   ps.setString(31, Double.toString(doHdr.getORDERDISCOUNT()));
			   ps.setString(32, Double.toString(doHdr.getSHIPPINGCOST()));
			   ps.setString(33, doHdr.getINCOTERMS());
			   ps.setString(34, doHdr.getPAYMENTTYPE());
			   ps.setString(35, Short.toString(doHdr.getDELIVERYDATEFORMAT()));
			   ps.setString(36, doHdr.getAPPROVESTATUS());
			   ps.setString(37, doHdr.getSALES_LOCATION());
			   ps.setString(38, doHdr.getORDER_STATUS());
			   ps.setString(39, Double.toString(doHdr.getDISCOUNT()));
			   ps.setString(40, doHdr.getDISCOUNT_TYPE());
			   ps.setString(41, Double.toString(doHdr.getADJUSTMENT()));
			   ps.setString(42, Short.toString(doHdr.getITEM_RATES()));
			   ps.setDouble(43, doHdr.getCURRENCYUSEQT());
			   ps.setString(44, doHdr.getORDERDISCOUNTTYPE());
			   ps.setInt(45, doHdr.getTAXID());
			   ps.setShort(46, doHdr.getISDISCOUNTTAX());
			   ps.setShort(47, doHdr.getISORDERDISCOUNTTAX());
			   ps.setShort(48, doHdr.getISSHIPPINGTAX());
			   ps.setInt(49, doHdr.getPROJECTID());
			   ps.setInt(50, doHdr.getTRANSPORTID());
			   ps.setString(51, doHdr.getPAYMENT_TERMS());
			   ps.setString(52, doHdr.getSHIPCONTACTNAME());
				ps.setString(53, doHdr.getSHIPDESGINATION());
				ps.setString(54, doHdr.getSHIPWORKPHONE());
				ps.setString(55, doHdr.getSHIPHPNO());
				ps.setString(56, doHdr.getSHIPEMAIL());
				ps.setString(57, doHdr.getSHIPCOUNTRY());
				ps.setString(58, doHdr.getSHIPADDR1());
				ps.setString(59, doHdr.getSHIPADDR2());
				ps.setString(60, doHdr.getSHIPADDR3());
				ps.setString(61, doHdr.getSHIPADDR4());
				ps.setString(62, doHdr.getSHIPSTATE());
				ps.setString(63, doHdr.getSHIPZIP());
				ps.setString(64, doHdr.getAPP_CUST_ORDER_STATUS());
				ps.setString(65, doHdr.getUKEY());
				ps.setString(66, doHdr.getREASON());
				ps.setShort(67, doHdr.getISFROMPEPPOL());
				ps.setString(68, doHdr.getSALESORDERUUID());
				ps.setShort(69, doHdr.getISINVOICED());
				ps.setString(70, doHdr.getINVOICEDID());
			    ps.setString(71, doHdr.getTAXTREATMENT());
			    
			    int count=ps.executeUpdate();
			  
			   ResultSet rs = ps.getGeneratedKeys(); 
 			   if (rs.next()){ 
					  dohdrkey = rs.getInt(1); 
				}
 			   
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
		return dohdrkey;
   }
   
   public List<DoHdr> getDoHdr(Hashtable ht, String afrmDate, String atoDate) throws Exception {
//	   boolean flag = false;
//	   int journalHdrId = 0;
	   Connection connection = null;
	   PreparedStatement ps = null;
	   String query = "", fields = "", dtCondStr = "";
	   List<DoHdr> doHeaders = new ArrayList<DoHdr>();
	   List<String> args = null;
	   try {
		   args = new ArrayList<String>();
		   connection = DbBean.getConnection();
		   query = "SELECT [PLANT]" + 
		   		"      ,[DONO]" + 
		   		"      ,[VENDNO]" + 
		   		"      ,[ORDDATE]" + 
		   		"      ,[ORDERTYPE]" + 
		   		"      ,[DELDATE]" + 
		   		"      ,[STATUS]" + 
		   		"      ,[PickStaus]" + 
		   		"      ,[CRAT]" + 
		   		"      ,[CRBY]" + 
		   		"      ,[UPAT]" + 
		   		"      ,[UPBY]" + 
		   		"      ,[RECSTAT]" + 
		   		"      ,[CustCode]" + 
		   		"      ,[CustName]" + 
		   		"      ,[JobNum]" + 
		   		"      ,[PersonInCharge]" + 
		   		"      ,[contactNum]" + 
		   		"      ,[Address]" + 
		   		"      ,[Address2]" + 
		   		"      ,[Address3]" + 
		   		"      ,[CollectionDate]" + 
		   		"      ,[CollectionTime]" + 
		   		"      ,[Remark1]" + 
		   		"      ,[Remark2]" + 
		   		"      ,[SHIPPINGID]" + 
		   		"      ,[SHIPPINGCUSTOMER]" + 
		   		"      ,[CURRENCYID]" + 
		   		"      ,ISNULL([DELIVERYDATE],'') DELIVERYDATE" + 
		   		"      ,[TIMESLOTS]" + 
		   		"      ,[OUTBOUND_GST]" + 
		   		"      ,[STATUS_ID]" + 
		   		"      ,[EMPNO]" + 
		   		"      ,ISNULL([ESTNO], '') ESTNO" + 
		   		"      ,[PAYMENT_STATUS]" + 
		   		"      ,[Remark3]" + 
		   		"      ,[ORDERDISCOUNT]" + 
		   		"      ,[SHIPPINGCOST]" + 
		   		"      ,[INCOTERMS]" + 
		   		"      ,[DNPLREMARKS]" + 
		   		"      ,[PAYMENTTYPE]" + 
		   		"      ,ISNULL(DELIVERYDATEFORMAT,0) DELIVERYDATEFORMAT" + 
		   		"      ,[APPROVESTATUS]" + 
		   		"      ,[SALES_LOCATION]" + 
		   		"      ,[TAXTREATMENT]" + 
		   		"	   ,ISNULL([DISCOUNT], 0) DISCOUNT" +
				"      ,ISNULL([DISCOUNT_TYPE], '') DISCOUNT_TYPE" +
				"      ,ISNULL([ADJUSTMENT], 0) ADJUSTMENT" +
				"      ,ISNULL([ITEM_RATES], 0) ITEM_RATES" +
				"      ,ISNULL([CURRENCYUSEQT], 0) CURRENCYUSEQT" +
				"      ,ISNULL([ORDERDISCOUNTTYPE], '') ORDERDISCOUNTTYPE" +
				"      ,ISNULL([TAXID], 0) TAXID" +
				"      ,ISNULL([ISDISCOUNTTAX], 0) ISDISCOUNTTAX" +
				"      ,ISNULL([ISORDERDISCOUNTTAX], 0) ISORDERDISCOUNTTAX" +
				"      ,ISNULL([ISSHIPPINGTAX], 0) ISSHIPPINGTAX" +
				"      ,ISNULL([PROJECTID], '') PROJECTID" +
				"      ,ISNULL([TRANSPORTID], '') TRANSPORTID" +
				"      ,ISNULL([PAYMENT_TERMS], '') PAYMENT_TERMS" +
				"      ,[SHIPCONTACTNAME]" +
				"           ,[SHIPDESGINATION]" +
				"           ,[SHIPWORKPHONE]" +
				"           ,[SHIPHPNO]" +
				"           ,[SHIPEMAIL]" +
				"           ,[SHIPCOUNTRY]" +
				"           ,[SHIPADDR1]" +
				"           ,[SHIPADDR2]" +
				"           ,[SHIPADDR3]" +
				"           ,[SHIPADDR4]" +
				"           ,[SHIPSTATE]" +
				"           ,[SHIPZIP]" +
				"           ,ISNULL(APP_CUST_ORDER_STATUS,'Pending') APP_CUST_ORDER_STATUS" +
				"      ,ISNULL([ID], '') ID" +
				"      ,ISNULL([APP_CUST_ORDER_STATUS], '') APP_CUST_ORDER_STATUS" +
				"      ,ISNULL([REASON], '') REASON" +
				"      ,ISNULL([UKEY], '') UKEY" +
		   		"      ,ISNULL(ORDER_STATUS,'Open') ORDER_STATUS FROM ["+ ht.get("PLANT") +"_"+TABLE_HEADER+"] WHERE ";
		   Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				fields += key + "=? AND ";
				args.add(value);
			}			
			query += fields.substring(0, fields.length() - 5);
			
			dtCondStr =" AND ISNULL(DELDATE,'')<>'' AND CAST((SUBSTRING(DELDATE, 7, 4) + '-' + SUBSTRING(DELDATE, 4, 2) + '-' + SUBSTRING(DELDATE, 1, 2)) AS date) ";
			
			if (afrmDate.length() > 0) {
				query +=  dtCondStr + "  >= '" + afrmDate + "' ";
  				if (atoDate.length() > 0) {
  					query += dtCondStr+ " <= '" + atoDate + "' ";
  				}
			} else {
  				if (atoDate.length() > 0) {
  					query += dtCondStr+ " <= '" + atoDate + "' ";
  				}
			}
			
			query += " ORDER BY CAST((SUBSTRING(DELDATE,7,4) + '-' + SUBSTRING(DELDATE,4,2) + '-' + SUBSTRING(DELDATE,1,2))AS DATE) DESC, DONO DESC";
			ps = connection.prepareStatement(query);
			if(connection != null){
				int i = 1;
				for (Object arg : args) {         
				    if (arg instanceof Integer) {
				        ps.setInt(i++, (Integer) arg);
				    } else if (arg instanceof Long) {
				        ps.setLong(i++, (Long) arg);
				    } else if (arg instanceof Double) {
				        ps.setDouble(i++, (Double) arg);
				    } else if (arg instanceof Float) {
				        ps.setFloat(i++, (Float) arg);
				    } else {
				        ps.setNString(i++, (String) arg);
				    }
				}
			    /* Execute the Query */  
				ResultSet rst  = ps.executeQuery();
				while (rst.next()) {	
					DoHdr dohdr = new DoHdr();
					ResultSetToObjectMap.loadResultSetIntoObject(rst, dohdr);
					doHeaders.add(dohdr);
				}
			}			
	   }catch (Exception e) {
		   this.mLogger.exception(this.printLog, "", e);
			throw e;
	   } finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
	   return doHeaders;
	}

   public List<DoHdr> getDoHdrSummaryByApprove(Hashtable ht, String afrmDate, String atoDate, String astatus, String ispos) throws Exception {//Sales Approve 09.22 - Azees
//	   boolean flag = false;
//	   int journalHdrId = 0;
	   Connection connection = null;
	   PreparedStatement ps = null;
	   String query = "", fields = "", dtCondStr = "";
	   List<DoHdr> doHeaders = new ArrayList<DoHdr>();
	   List<String> args = null;
	   try {
		   args = new ArrayList<String>();
		   connection = DbBean.getConnection();
		   query = "SELECT [PLANT]" + 
				   "      ,[DONO]" + 
				   "      ,[VENDNO]" + 
				   "      ,[ORDDATE]" + 
				   "      ,[ORDERTYPE]" + 
				   "      ,[DELDATE]" + 
				   "      ,[STATUS]" + 
				   "      ,[PickStaus]" + 
				   "      ,[CRAT]" + 
				   "      ,[CRBY]" + 
				   "      ,[UPAT]" + 
				   "      ,[UPBY]" + 
				   "      ,[RECSTAT]" + 
				   "      ,[CustCode]" + 
				   "      ,[CustName]" + 
				   "      ,[JobNum]" + 
				   "      ,[PersonInCharge]" + 
				   "      ,[contactNum]" + 
				   "      ,[Address]" + 
				   "      ,[Address2]" + 
				   "      ,[Address3]" + 
				   "      ,[CollectionDate]" + 
				   "      ,[CollectionTime]" + 
				   "      ,[Remark1]" + 
				   "      ,[Remark2]" + 
				   "      ,[SHIPPINGID]" + 
				   "      ,[SHIPPINGCUSTOMER]" + 
				   "      ,[CURRENCYID]" + 
				   "      ,ISNULL([DELIVERYDATE],'') DELIVERYDATE" + 
				   "      ,[TIMESLOTS]" + 
				   "      ,[OUTBOUND_GST]" + 
				   "      ,[STATUS_ID]" + 
				   "      ,[EMPNO]" + 
				   "      ,ISNULL([ESTNO], '') ESTNO" + 
				   "      ,[PAYMENT_STATUS]" + 
				   "      ,[Remark3]" + 
				   "      ,[ORDERDISCOUNT]" + 
				   "      ,[SHIPPINGCOST]" + 
				   "      ,[INCOTERMS]" + 
				   "      ,[DNPLREMARKS]" + 
				   "      ,[PAYMENTTYPE]" + 
				   "      ,ISNULL(DELIVERYDATEFORMAT,0) DELIVERYDATEFORMAT" + 
				   "      ,[APPROVESTATUS]" + 
				   "      ,[SALES_LOCATION]" + 
				   "      ,[TAXTREATMENT]" + 
				   "	   ,ISNULL([DISCOUNT], 0) DISCOUNT" +
				   "      ,ISNULL([DISCOUNT_TYPE], '') DISCOUNT_TYPE" +
				   "      ,ISNULL([ADJUSTMENT], 0) ADJUSTMENT" +
				   "      ,ISNULL([ITEM_RATES], 0) ITEM_RATES" +
				   "      ,ISNULL([CURRENCYUSEQT], 0) CURRENCYUSEQT" +
				   "      ,ISNULL([ORDERDISCOUNTTYPE], '') ORDERDISCOUNTTYPE" +
				   "      ,ISNULL([TAXID], 0) TAXID" +
				   "      ,ISNULL([ISDISCOUNTTAX], 0) ISDISCOUNTTAX" +
				   "      ,ISNULL([ISORDERDISCOUNTTAX], 0) ISORDERDISCOUNTTAX" +
				   "      ,ISNULL([ISSHIPPINGTAX], 0) ISSHIPPINGTAX" +
				   "      ,ISNULL([PROJECTID], '') PROJECTID" +
				   "      ,ISNULL([TRANSPORTID], '') TRANSPORTID" +
				   "      ,ISNULL([PAYMENT_TERMS], '') PAYMENT_TERMS" +
				   "      ,[SHIPCONTACTNAME]" +
				   "           ,[SHIPDESGINATION]" +
				   "           ,[SHIPWORKPHONE]" +
				   "           ,[SHIPHPNO]" +
				   "           ,[SHIPEMAIL]" +
				   "           ,[SHIPCOUNTRY]" +
				   "           ,[SHIPADDR1]" +
				   "           ,[SHIPADDR2]" +
				   "           ,[SHIPADDR3]" +
				   "           ,[SHIPADDR4]" +
				   "           ,[SHIPSTATE]" +
				   "           ,[SHIPZIP]" +
				   "           ,ISNULL(APP_CUST_ORDER_STATUS,'Pending') APP_CUST_ORDER_STATUS" +
				   "      ,ISNULL([ID], '') ID" +
				   "      ,ISNULL([APP_CUST_ORDER_STATUS], '') APP_CUST_ORDER_STATUS" +
				   "      ,ISNULL([REASON], '') REASON" +
				   "      ,ISNULL([UKEY], '') UKEY" +
				   "      ,ISNULL([ISFROMPEPPOL], 0) ISFROMPEPPOL" +
				   "      ,ISNULL([SALESORDERUUID], '') SALESORDERUUID" +
				   "      ,ISNULL([ISINVOICED], 0) ISINVOICED" +
				   "      ,ISNULL([INVOICEDID], '') INVOICEDID" +
				   "      ,ISNULL(ORDER_STATUS,'Open') ORDER_STATUS FROM ["+ ht.get("PLANT") +"_"+TABLE_HEADER+"] WHERE ";
		   Enumeration enum1 = ht.keys();
		   for (int i = 0; i < ht.size(); i++) {
			   String key = StrUtils.fString((String) enum1.nextElement());
			   String value = StrUtils.fString((String) ht.get(key));
			   fields += key + "=? AND ";
			   args.add(value);
		   }			
		   query += fields.substring(0, fields.length() - 5);
		   
		   	if(ispos.equalsIgnoreCase("3"))
		   		query +=  " AND ORDERTYPE = 'POS' " ;
		   	else if(ispos.equalsIgnoreCase("2"))
		   		query +=  " AND ORDERTYPE != 'POS' " ;
		   
		   dtCondStr =" AND ISNULL(DELDATE,'')<>'' AND CAST((SUBSTRING(DELDATE, 7, 4) + '-' + SUBSTRING(DELDATE, 4, 2) + '-' + SUBSTRING(DELDATE, 1, 2)) AS date) ";
		   
		   if (afrmDate.length() > 0) {
			   query +=  dtCondStr + "  >= '" + afrmDate + "' ";
			   if (atoDate.length() > 0) {
				   query += dtCondStr+ " <= '" + atoDate + "' ";
			   }
		   } else {
			   if (atoDate.length() > 0) {
				   query += dtCondStr+ " <= '" + atoDate + "' ";
			   }
		   }
		   query += astatus;
		   query += " ORDER BY CAST((SUBSTRING(DELDATE,7,4) + '-' + SUBSTRING(DELDATE,4,2) + '-' + SUBSTRING(DELDATE,1,2))AS DATE) DESC, DONO DESC";
		   ps = connection.prepareStatement(query);
		   if(connection != null){
			   int i = 1;
			   for (Object arg : args) {         
				   if (arg instanceof Integer) {
					   ps.setInt(i++, (Integer) arg);
				   } else if (arg instanceof Long) {
					   ps.setLong(i++, (Long) arg);
				   } else if (arg instanceof Double) {
					   ps.setDouble(i++, (Double) arg);
				   } else if (arg instanceof Float) {
					   ps.setFloat(i++, (Float) arg);
				   } else {
					   ps.setNString(i++, (String) arg);
				   }
			   }
			   /* Execute the Query */  
			   ResultSet rst  = ps.executeQuery();
			   while (rst.next()) {	
				   DoHdr dohdr = new DoHdr();
				   ResultSetToObjectMap.loadResultSetIntoObject(rst, dohdr);
				   doHeaders.add(dohdr);
			   }
		   }			
	   }catch (Exception e) {
		   this.mLogger.exception(this.printLog, "", e);
		   throw e;
	   } finally {
		   if (connection != null) {
			   DbBean.closeConnection(connection);
		   }
	   }
	   return doHeaders;
   }
   
   public DoHdr getDoHdrById(String plant,String dono)throws Exception {
//		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    DoHdr doHdr = new DoHdr();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT [PLANT]" + 
			   		"      ,[DONO]" + 
			   		"      ,[VENDNO]" + 
			   		"      ,[ORDDATE]" + 
			   		"      ,ISNULL([ORDERTYPE], '') ORDERTYPE" + 
			   		"      ,[DELDATE]" + 
			   		"      ,[STATUS]" + 
			   		"      ,[PickStaus]" + 
			   		"      ,[CRAT]" + 
			   		"      ,[CRBY]" + 
			   		"      ,[UPAT]" + 
			   		"      ,[UPBY]" + 
			   		"      ,[RECSTAT]" + 
			   		"      ,[CustCode]" + 
			   		"      ,[CustName]" + 
			   		"      ,ISNULL([JobNum], '') JobNum" + 
			   		"      ,[PersonInCharge]" + 
			   		"      ,[contactNum]" + 
			   		"      ,[Address]" + 
			   		"      ,[Address2]" + 
			   		"      ,[Address3]" + 
			   		"      ,[CollectionDate]" + 
			   		"      ,[CollectionTime]" + 
			   		"      ,[Remark1]" + 
			   		"      ,[Remark2]" + 
			   		"      ,[SHIPPINGID]" + 
			   		"      ,[SHIPPINGCUSTOMER]" + 
			   		"      ,[CURRENCYID]" + 
			   		"      ,[DELIVERYDATE]" + 
			   		"      ,[TIMESLOTS]" + 
			   		"      ,[OUTBOUND_GST]" + 
			   		"      ,[STATUS_ID]" + 
			   		"      ,[EMPNO]" + 
			   		"      ,ISNULL([ESTNO], '') ESTNO" + 
			   		"      ,[PAYMENT_STATUS]" + 
			   		"      ,[Remark3]" + 
			   		"      ,CASE WHEN ISNULL(ORDERDISCOUNTTYPE,'0') = '%' THEN ORDERDISCOUNT ELSE (ORDERDISCOUNT * ISNULL(CURRENCYUSEQT,1))END AS ORDERDISCOUNT" +
			   		"      ,CASE WHEN ISNULL(DISCOUNT_TYPE,'0') = '%' THEN ISNULL([DISCOUNT], 0) ELSE (ISNULL([DISCOUNT], 0) * ISNULL(CURRENCYUSEQT,1))END AS DISCOUNT" +
			   		"      ,(ISNULL([ADJUSTMENT], 0) * ISNULL(CURRENCYUSEQT,1)) ADJUSTMENT" +
			   		"      ,(ISNULL([SHIPPINGCOST], 0) * ISNULL(CURRENCYUSEQT,1)) SHIPPINGCOST" +
			   		"      ,[INCOTERMS]" + 
			   		"      ,[DNPLREMARKS]" + 
			   		"      ,[PAYMENTTYPE]" + 
			   		"      ,ISNULL(DELIVERYDATEFORMAT,0) DELIVERYDATEFORMAT" + 
			   		"      ,[APPROVESTATUS]" + 
			   		"      ,[SALES_LOCATION]" + 
			   		"      ,[TAXTREATMENT]" + 
					"      ,ISNULL([DISCOUNT_TYPE], '') DISCOUNT_TYPE" +
					"      ,ISNULL([ITEM_RATES], 0) ITEM_RATES" +
					"      ,ISNULL([CURRENCYUSEQT], 0) CURRENCYUSEQT" +
					"      ,ISNULL([ORDERDISCOUNTTYPE], '') ORDERDISCOUNTTYPE" +
					"      ,ISNULL([TAXID], 0) TAXID" +
					"      ,ISNULL([ISDISCOUNTTAX], 0) ISDISCOUNTTAX" +
					"      ,ISNULL([ISORDERDISCOUNTTAX], 0) ISORDERDISCOUNTTAX" +
					"      ,ISNULL([ISSHIPPINGTAX], 0) ISSHIPPINGTAX" +
					"      ,ISNULL([PROJECTID], '') PROJECTID" +
					"      ,ISNULL([TRANSPORTID], '') TRANSPORTID" +
					"      ,ISNULL([PAYMENT_TERMS], '') PAYMENT_TERMS" +
					"      ,ISNULL([SHIPCONTACTNAME], '') SHIPCONTACTNAME" +
					"      ,ISNULL([SHIPDESGINATION], '') SHIPDESGINATION" +
					"      ,ISNULL([SHIPWORKPHONE], '') SHIPWORKPHONE" +
					"      ,ISNULL([SHIPHPNO], '') SHIPHPNO" +
					"      ,ISNULL([SHIPEMAIL], '') SHIPEMAIL" +
					"      ,ISNULL([SHIPCOUNTRY], '') SHIPCOUNTRY" +
					"      ,ISNULL([SHIPADDR1], '') SHIPADDR1" +
					"      ,ISNULL([SHIPADDR2], '') SHIPADDR2" +
					"      ,ISNULL([SHIPADDR3], '') SHIPADDR3" +
					"      ,ISNULL([SHIPADDR4], '') SHIPADDR4" +
					"      ,ISNULL([SHIPSTATE], '') SHIPSTATE" +
					"      ,ISNULL([SHIPZIP], '') SHIPZIP" +
					"      ,ISNULL([ID], '') ID" +
					"      ,ISNULL([REASON], '') REASON" +
					"      ,ISNULL([UKEY], '') UKEY" +
					"      ,ISNULL([ISFROMPEPPOL], 0) ISFROMPEPPOL" +
					"      ,ISNULL([SALESORDERUUID], '') SALESORDERUUID" +
					"      ,ISNULL([ISINVOICED], 0) ISINVOICED" +
					"      ,ISNULL([INVOICEDID], '') INVOICEDID" +
			   		"      ,ISNULL(ORDER_STATUS,'Open') ORDER_STATUS,ISNULL(APP_CUST_ORDER_STATUS ,'Pending') APP_CUST_ORDER_STATUS  FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE "+
			   		" DONO=? AND PLANT=?";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, dono);
				   ps.setString(2, plant);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, doHdr);
	                }   
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
		return doHdr;
	}
   
   public boolean updateDoHdr(DoHdr doHdr) throws Exception {
		boolean updateFlag = false;
//		boolean flag = false;
//		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "UPDATE ["+doHdr.getPLANT()+"_"+TABLE_HEADER+"] SET" + 
					"           [PLANT] = ?" + 
					"           ,[ORDERTYPE] = ?" + 
					"           ,[STATUS] = ?" + 
					"           ,[PickStaus] = ?" + 
					"           ,[UPAT] = ?" + 
					"           ,[UPBY] = ?" + 
					"           ,[JobNum] = ?" +   
					"           ,[Remark1] = ?" + 
					"           ,[Remark2] = ?" + 
					"           ,[SHIPPINGID] = ?" + 
					"           ,[SHIPPINGCUSTOMER] = ?" + 
					"           ,[CURRENCYID] = ?" + 
					"           ,[DELIVERYDATE] = ?" + 
					"           ,[TIMESLOTS] = ?" + 
					"           ,[OUTBOUND_GST] = ?" + 
					"           ,[STATUS_ID] = ?" + 
					"           ,[EMPNO] = ?" + 
					"           ,[ESTNO] = ?" + 
					"           ,[Remark3] = ?" + 
					"           ,[ORDERDISCOUNT] = ?" + 
					"           ,[SHIPPINGCOST] = ?" + 
					"           ,[INCOTERMS] = ?" + 
					"           ,[PAYMENTTYPE] = ?" + 
					"           ,[DELIVERYDATEFORMAT] = ?" + 
					"           ,[APPROVESTATUS] = ?" + 
					"           ,[SALES_LOCATION] = ?" + 
					"           ,[ORDER_STATUS] = ?" +
					"           ,[DISCOUNT] = ?" +
					"           ,[DISCOUNT_TYPE] = ?" +
					"           ,[ADJUSTMENT] = ?" +
					"      		,[ITEM_RATES] = ?" +
					"      		,[CURRENCYUSEQT] = ?" +
					"      		,[ORDERDISCOUNTTYPE] = ?" +
					"      		,[TAXID] = ?" +
					"      		,[ISDISCOUNTTAX] = ?" +
					"      		,[ISORDERDISCOUNTTAX] = ?" +
					"      		,[ISSHIPPINGTAX] = ?" +
					"      		,[PROJECTID] = ?" +
					"      		,[TRANSPORTID] = ?" +
					"      		,[PAYMENT_TERMS] = ?" +
					"           ,[SHIPCONTACTNAME] = ?" +
					"           ,[SHIPDESGINATION] = ?" +
					"           ,[SHIPWORKPHONE] = ?" +
					"           ,[SHIPHPNO] = ?" +
					"           ,[SHIPEMAIL] = ?" +
					"           ,[SHIPCOUNTRY] = ?" +
					"           ,[SHIPADDR1] = ?" +
					"           ,[SHIPADDR2] = ?" +
					"           ,[SHIPADDR3] = ?" +
					"           ,[SHIPADDR4] = ?" +
					"           ,[SHIPSTATE] = ?" +
					"           ,[SHIPZIP] = ?" +
					"           ,[APP_CUST_ORDER_STATUS] = ?" +
					"           ,[UKEY] = ?" +
					"           ,[REASON] = ?" +
					"           ,[ISFROMPEPPOL] = ?" +
					"           ,[SALESORDERUUID] = ?" +
					"           ,[ISINVOICED] = ?" +
					"           ,[INVOICEDID] = ?" +
					"           ,[TAXTREATMENT] = ? WHERE [DONO] = ?";

			if(connection != null){
			   ps = connection.prepareStatement(query);
			   ps.setString(1, doHdr.getPLANT());
			   ps.setString(2, doHdr.getORDERTYPE());
			   ps.setString(3, doHdr.getSTATUS());
			   ps.setString(4, doHdr.getPickStaus());
			   ps.setString(5, doHdr.getUPAT());
			   ps.setString(6, doHdr.getUPBY());
			   ps.setString(7, doHdr.getJobNum());
			   ps.setString(8, doHdr.getRemark1());
			   ps.setString(9, doHdr.getRemark2());
			   ps.setString(10, doHdr.getSHIPPINGID());
			   ps.setString(11, doHdr.getSHIPPINGCUSTOMER());
			   ps.setString(12, doHdr.getCURRENCYID());
			   ps.setString(13, doHdr.getDELIVERYDATE());
			   ps.setString(14, doHdr.getTIMESLOTS());
			   ps.setString(15, Double.toString(doHdr.getOUTBOUND_GST()));
			   ps.setString(16, doHdr.getSTATUS_ID());
			   ps.setString(17, doHdr.getEMPNO());
			   ps.setString(18, doHdr.getESTNO());
			   ps.setString(19, doHdr.getRemark3());
			   ps.setString(20, Double.toString(doHdr.getORDERDISCOUNT()));
			   ps.setString(21, Double.toString(doHdr.getSHIPPINGCOST()));
			   ps.setString(22, doHdr.getINCOTERMS());
			   ps.setString(23, doHdr.getPAYMENTTYPE());
			   ps.setString(24, Short.toString(doHdr.getDELIVERYDATEFORMAT()));
			   ps.setString(25, doHdr.getAPPROVESTATUS());
			   ps.setString(26, doHdr.getSALES_LOCATION());
			   ps.setString(27, doHdr.getORDER_STATUS());
			   ps.setString(28, Double.toString(doHdr.getDISCOUNT()));
			   ps.setString(29, doHdr.getDISCOUNT_TYPE());
			   ps.setString(30, Double.toString(doHdr.getADJUSTMENT()));
			   ps.setString(31, Short.toString(doHdr.getITEM_RATES()));
			   ps.setDouble(32, doHdr.getCURRENCYUSEQT());
			   ps.setString(33, doHdr.getORDERDISCOUNTTYPE());
			   ps.setInt(34, doHdr.getTAXID());
			   ps.setShort(35, doHdr.getISDISCOUNTTAX());
			   ps.setShort(36, doHdr.getISORDERDISCOUNTTAX());
			   ps.setShort(37, doHdr.getISSHIPPINGTAX());
			   ps.setInt(38, doHdr.getPROJECTID());
			   ps.setInt(39, doHdr.getTRANSPORTID());
			   ps.setString(40, doHdr.getPAYMENT_TERMS());
			   ps.setString(41, doHdr.getSHIPCONTACTNAME());
				ps.setString(42, doHdr.getSHIPDESGINATION());
				ps.setString(43, doHdr.getSHIPWORKPHONE());
				ps.setString(44, doHdr.getSHIPHPNO());
				ps.setString(45, doHdr.getSHIPEMAIL());
				ps.setString(46, doHdr.getSHIPCOUNTRY());
				ps.setString(47, doHdr.getSHIPADDR1());
				ps.setString(48, doHdr.getSHIPADDR2());
				ps.setString(49, doHdr.getSHIPADDR3());
				ps.setString(50, doHdr.getSHIPADDR4());
				ps.setString(51, doHdr.getSHIPSTATE());
				ps.setString(52, doHdr.getSHIPZIP());
				ps.setString(53, doHdr.getAPP_CUST_ORDER_STATUS());
				ps.setString(54, doHdr.getUKEY());
				ps.setString(55, doHdr.getREASON());
				ps.setShort(56, doHdr.getISFROMPEPPOL());
				ps.setString(57, doHdr.getSALESORDERUUID());
				ps.setShort(58, doHdr.getISINVOICED());
				ps.setString(59, doHdr.getINVOICEDID());
			    ps.setString(60, doHdr.getTAXTREATMENT());
			  
			   ps.setString(61, doHdr.getDONO());
			   int count=ps.executeUpdate();
			   if(count>0)
			   {
				   updateFlag = true;
			   }
			   else
			   {
				   throw new SQLException("Creating Sales Order failed, no rows affected.");
			   }
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
		return updateFlag;
	}
//   RESVI START
   public int Salescount(String plant, String afrmDate, String atoDate)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int Salescount = 0;
		String sCondition = "";
		String dtCondStr =    " AND (SUBSTRING(CollectionDate, 7, 4) + '-' + SUBSTRING(CollectionDate, 4, 2) + '-' + SUBSTRING(CollectionDate, 1, 2))";
		if (afrmDate.length() > 0) {
         	sCondition = " " + dtCondStr +"  >= '" + afrmDate
         	+ "'  ";
         	if (atoDate.length() > 0) {
         		sCondition = sCondition + " " + dtCondStr +" <= '" + atoDate
         		+ "'  ";
         	}
         }
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ "DOHDR" + "]" + " WHERE " + IConstants.PLANT
					+ " = '" + plant.toUpperCase() + "'"+ sCondition;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Salescount = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return Salescount;
	}

 //RESVI ENDS
   
   public ArrayList getOrderNoForOrderIssue(Hashtable ht) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		String query = "", plant = "";//conditon = "", 
		try {
			con = com.track.gates.DbBean.getConnection();
			plant = (String)ht.get("PLANT");
			query = "SELECT * FROM (" + 
					"SELECT DONO, CustCode, CollectionDate AS ORDER_DATE FROM ["+plant+"_DOHDR] " + 
					"WHERE PLANT = '"+plant+"'";
					if(((String)ht.get("ORDNO")).length()>0) {
						query += " AND DONO LIKE '%"+(String)ht.get("ORDNO")+"%'";
					}
					if(((String)ht.get("CUSTNO")).length()>0) {
						query += " AND CUSTCODE = '"+(String)ht.get("CUSTNO")+"'";
					}
			query += " UNION " + 
					"SELECT INVOICE, CUSTNO, INVOICE_DATE AS ORDER_DATE FROM ["+plant+"_FININVOICEHDR] " + 
					
					"WHERE DONO='' AND GINO<>'' AND INVOICE LIKE '%%' ";
					if(((String)ht.get("ORDNO")).length()>0) {
						query += " AND INVOICE LIKE '%"+(String)ht.get("ORDNO")+"%'";
					}
					if(((String)ht.get("CUSTNO")).length()>0) {
						query += " AND CUSTNO = '"+(String)ht.get("CUSTNO")+"'";
					}
			query += ") A ORDER BY ISNULL(CAST((SUBSTRING(ORDER_DATE,7,4) "
					+ "+ SUBSTRING(ORDER_DATE,4,2) + SUBSTRING(ORDER_DATE,1,2)) AS date),'') DESC ";
			alData = selectData(con, query.toString());
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
   
 //Author: Azees  Create date: August 08,2021  Description: Show only order no.
   public ArrayList getOrderNoForOrderReverse(Hashtable ht) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		String query = "", plant = "";//conditon = "", 
		try {
			con = com.track.gates.DbBean.getConnection();
			plant = (String)ht.get("PLANT");
			query = "SELECT * FROM (" + 
					"SELECT DONO, CustCode, CollectionDate AS ORDER_DATE FROM ["+plant+"_DOHDR] " + 
					"WHERE PLANT = '"+plant+"'";
					if(((String)ht.get("ORDNO")).length()>0) {
						query += " AND DONO LIKE '%"+(String)ht.get("ORDNO")+"%'";
					}
					if(((String)ht.get("CUSTNO")).length()>0) {
						query += " AND CUSTCODE = '"+(String)ht.get("CUSTNO")+"'";
					}
			
			query += ") A ORDER BY ISNULL(CAST((SUBSTRING(ORDER_DATE,7,4) "
					+ "+ SUBSTRING(ORDER_DATE,4,2) + SUBSTRING(ORDER_DATE,1,2)) AS date),'') DESC ";
			alData = selectData(con, query.toString());
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
   
   public ArrayList selectItemForPDA(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(query);
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();
				sql.append("");
				conditon = formCondition(ht);
				sql.append(conditon);
				this.mLogger.query(this.printQuery, sql.toString());
				alData = selectData(con, sql.toString());
				
				
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;

	}
   
   public String getAverageUnitCost(String aPlant, String uom,String aItem) throws Exception {
       java.sql.Connection con = null;
       String UnitCostForSelCurrency = "";
       try {
    	   if(uom.equalsIgnoreCase(""))
    		   uom = " (SELECT PURCHASEUOM FROM "+aPlant+"_ITEMMST WHERE ITEM = '"+aItem+"') ";
    	   else
    		   uom = "'"+uom+"'";
    	   
               con = DbBean.getConnection();
               String currency=new PlantMstDAO().getBaseCurrency(aPlant);

       StringBuffer SqlQuery = new StringBuffer("SELECT ISNULL((CASE WHEN  (SELECT COUNT(CURRENCYID) FROM "+aPlant+"_RECVDET R WHERE ITEM='"+aItem+"' AND CURRENCYID IS NOT NULL AND tran_type \n" +
   			"IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING') )>0 THEN  \n" +
   			"(Select ISNULL(CAST(ISNULL(SUM(CASE WHEN A.TRAN_TYPE='GOODSRECEIPTWITHBATCH' THEN 0 \n" +
   			"ELSE A.UNITCOST END),0)/SUM(VC) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from  \n" +
   			"(select TRAN_TYPE,RECQTY VC,CASE WHEN TRAN_TYPE IN ('INVENTORYUPLOAD','DE-KITTING','KITTING') THEN\n" +
   			"(isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM "+aPlant+"_CURRENCYMST WHERE  \n" +
   			"CURRENCYID=ISNULL(R.CURRENCYID,'"+currency+"')),0)*R.RECQTY) ELSE CAST( (CAST(ISNULL(\n" +
   			"ISNULL((select ISNULL(QPUOM,1) from "+aPlant+"_UOM where UOM=''),1) * ( ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM "+aPlant+"_CURRENCYMST WHERE  \n" +
   			"CURRENCYID=ISNULL(P.CURRENCYID,'"+currency+"')),0)+ ISNULL((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM "+aPlant+"_RECVDET c left join "+aPlant+"_FINBILLHDR d on\n" +
   			"c.PONO = d.PONO and c.GRNO = d.GRNO left join "+aPlant+"_FINBILLDET e on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and e.ITEM = '"+aItem+"' \n" +
   			"OR c.TRAN_TYPE='GOODSRECEIPTWITHBATCH' AND c.item = '"+aItem+"'),0) +  (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT  FROM "+aPlant+"_CURRENCYMST WHERE  \n" +
   			"CURRENCYID=ISNULL(P.CURRENCYID,'"+currency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+ CASE WHEN (SELECT SUM(LANDED_COST) FROM "+aPlant+"_FINBILLHDR c join \n" +
   			""+aPlant+"_FINBILLDET d  ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select \n" +
   			"SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from "+aPlant+"_podet s where s.pono=R.pono),0)),0))/100))/ (SELECT CURRENCYUSEQT  FROM "+aPlant+"_CURRENCYMST WHERE \n" +
   			"CURRENCYID=ISNULL(P.CURRENCYID,'"+currency+"'))),0) / (ISNULL((select ISNULL(QPUOM,1) from "+aPlant+"_UOM where UOM=S.UNITMO),1))) ,0) \n" +
   			"*(SELECT CURRENCYUSEQT FROM "+aPlant+"_CURRENCYMST    WHERE  CURRENCYID='"+currency+"')*(SELECT CURRENCYUSEQT  FROM "+aPlant+"_CURRENCYMST    \n" +
   			"WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+currency+"')) AS DECIMAL(20,5)) )  * CAST((SELECT CURRENCYUSEQT  \n" +
   			"FROM "+aPlant+"_CURRENCYMST WHERE  CURRENCYID='"+currency+"')AS DECIMAL(20,5))  / CAST((SELECT CURRENCYUSEQT \n" +
   			"FROM "+aPlant+"_CURRENCYMST WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+currency+"')) AS DECIMAL(20,5))   \n" +
   			"* RECQTY AS DECIMAL(20,5)) END AS UNITCOST from "+aPlant+"_RECVDET R LEFT JOIN "+aPlant+"_POHDR P ON R.PONO = P.PONO \n"
   			+" LEFT JOIN "+aPlant+"_podet s ON s.pono=R.pono AND R.item=s.item"
   			+" where R.item ='"+aItem+"' AND ISNULL(R.UNITCOST,0) != 0 AND tran_type IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING')    ) A)   \n" +
   			" ELSE (SELECT CASE WHEN (SELECT COUNT(*) FROM "+aPlant+"_RECVDET WHERE ITEM='"+aItem+"' AND tran_type IN('INVENTORYUPLOAD','DE-KITTING','KITTING') )>0\n" +
   			" THEN (SELECT SUM(UNITCOST) FROM "+aPlant+"_RECVDET C where item = '"+aItem+"' \n" +
   			" AND ISNULL(C.UNITCOST,0) != 0 AND tran_type IN('INVENTORYUPLOAD','DE-KITTING','KITTING')) ELSE \n" +
   			" CAST(((SELECT M.COST / ISNULL((select ISNULL(QPUOM,1) from "+aPlant+"_UOM where UOM=M.SALESUOM),1) FROM "+aPlant+"_ITEMMST M WHERE M.ITEM = '"+aItem+"')*(SELECT CURRENCYUSEQT  FROM\n" +
   			" "+aPlant+"_CURRENCYMST WHERE  CURRENCYID='"+currency+"')) AS DECIMAL(20,5))   END) END),0) "
   			+ " * (ISNULL((select ISNULL(QPUOM,1) from "+aPlant+"_UOM where UOM="+uom+"),1)) "
   			+ "AS AVERAGE_COST");


       
       System.out.println(SqlQuery.toString());
           Map m = this.getRowOfData(con, SqlQuery.toString());

           UnitCostForSelCurrency = (String) m.get("AVERAGE_COST");

       } catch (Exception e) {
               this.mLogger.exception(this.printLog, "", e);
               throw e;
       } finally {
               if (con != null) {
                       DbBean.closeConnection(con);
               }
       }
       return UnitCostForSelCurrency;
   }
   public boolean insertPrintOrder(Hashtable ht) throws Exception {
		boolean insertFlag = false;
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_PRINTORDER]" + "("
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
   
   public boolean isExistPrintOrder(String status,String userId,String plant,String OrderType)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" +plant+"_PRINTORDER"
					+ "]" + " WHERE ORDERTYPE ='"+OrderType+"' AND USERID ='"+userId+"' AND STATUS='"
					+ status.toUpperCase() + "'";
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
   
   public ArrayList getPendingPrintOrder(Hashtable ht) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		String plant = (String) ht.get("PLANT");
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer("SELECT ORDERNO FROM ["+plant+"_PRINTORDER] a");
		String conditon = "";
		try {
			con = com.track.gates.DbBean.getConnection();
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			sql.append(" ORDER BY CRAT DESC");
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
   
   public boolean updatePendingPrintOrder(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + "PRINTORDER" + "]");
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
   
   
   public ArrayList selectPosSalesOld(String plant,String posdate,String outlet, String terminal) throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer("SELECT D.ORDDATE,D.OUTLET,D.TERMINAL,"
				+ "(SUM((D.TOTAL_PRICE) - (CASE WHEN ISNULL(D.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((D.TOTAL_PRICE)/100)*D.DISCOUNT, 0) ELSE ISNULL(D.DISCOUNT, 0) END) ) - SUM(D.EXPRICE)) AS TOTALPRICE,"
				+ "(SELECT TOP 1 O.OUTLET_NAME FROM "+plant+"_POSOUTLETS AS O WHERE O.OUTLET=D.OUTLET) AS OUTLETNAME,"
				+ "(SELECT TOP 1 T.TERMINAL_NAME FROM "+plant+"_POSOUTLETSTERMINALS AS T WHERE T.OUTLET=D.OUTLET AND T.TERMINAL=D.TERMINAL) AS TERMINALNAME,"
				+ "(SUM(D.UNITCOST)-SUM(D.EXCOST)) AS TOTALCOST "
				/*+ "(SUM((D.TOTAL_PRICE) - (CASE WHEN ISNULL(D.DISCOUNT_TYPE,'%') = '%' THEN "
				+ "ISNULL(((D.TOTAL_PRICE)/100)*D.DISCOUNT, 0) ELSE ISNULL(D.DISCOUNT, 0) END) )) AS TOTALPRICE,"
				+ "SUM(D.UNITCOST) AS TOTALCOST "*/
				+ "FROM (SELECT A.ORDDATE,A.OUTLET,A.TERMINAL,A.DISCOUNT_TYPE,A.DISCOUNT,SUM((B.UNITCOST * B.QTYOR)) AS UNITCOST,"
				+ "isnull((SELECT ISNULL(SUM(ISNULL(IT.COST,0)*PE.QTY),0) AS EXCOST FROM "+plant+"_POSEXCHANGEDET PE JOIN "+plant+"_ITEMMST AS IT ON IT.ITEM=PE.ITEM  WHERE PE.EXDONO = A.DONO),0) EXCOST,"
				//+ "ISNULL(SUM(ISNULL(IT.COST,0)*PE.QTY),0) AS EXCOST,"
				+ "isnull((SELECT (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0) ELSE (ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0)) + (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)) - (((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY))/(100+A.OUTBOUND_GST))*100) ELSE ((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)/100)* A.OUTBOUND_GST) END) END) AS EXPRICE FROM "+plant+"_POSEXCHANGEDET PE WHERE PE.EXDONO = A.DONO),0) EXPRICE,"
				//+ "(CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0) ELSE (ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0)) + (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)) - (((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY))/(100+A.OUTBOUND_GST))*100) ELSE ((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)/100)* A.OUTBOUND_GST) END) END) AS EXPRICE,"
				+ "(CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * B.QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) ELSE (SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * B.QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) + (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) - (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/(100+A.OUTBOUND_GST))*100) ELSE (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100)* A.OUTBOUND_GST) END ) END) AS TOTAL_PRICE "
				/*+ "(SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN "
				+ "ISNULL(((B.UNITPRICE * B.QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) AS TOTAL_PRICE "*/
				+ "FROM "+plant+"_DOHDR A JOIN "+plant+"_DODET B ON A.DONO=B.DONO WHERE "
				//+ "LEFT JOIN "+plant+"_POSEXCHANGEDET AS PE ON PE.EXDONO = A.DONO LEFT JOIN "+plant+"_ITEMMST AS IT ON IT.ITEM=PE.ITEM WHERE "
				+ "A.ORDER_STATUS != 'DRAFT' AND A.ORDER_STATUS = 'PROCESSED' AND A.ORDDATE ='"+posdate+"' AND A.OUTLET ='"+outlet+"' AND A.TERMINAL ='"+terminal+"' GROUP BY A.DONO,A.ORDDATE,A.OUTLET,A.TERMINAL,A.OUTBOUND_GST,A.DISCOUNT_TYPE,A.DISCOUNT,A.ITEM_RATES) AS D GROUP BY "
				+ "D.ORDDATE,D.OUTLET,D.TERMINAL");
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
   
   
   public ArrayList selectPosSales(String plant,String posdate,String outlet, String terminal) throws Exception {
	   ArrayList<Map<String, String>> alData = new ArrayList<>();
	   java.sql.Connection con = null;
	   StringBuffer sql = new StringBuffer("WITH ExchangeCosts AS (SELECT PE.EXDONO,SUM(ISNULL(IT.COST, 0) * PE.QTY) AS EXCOST,SUM(ISNULL(PE.UNITPRICE, 0) * PE.QTY) AS EXPRICE "
			   	+ "FROM "+plant+"_POSEXCHANGEDET PE JOIN "+plant+"_ITEMMST AS IT ON IT.ITEM = PE.ITEM GROUP BY PE.EXDONO ), "
			   	+ "OrderDetails AS ( SELECT A.DONO,A.ORDDATE,A.OUTLET,A.TERMINAL,A.OUTBOUND_GST,A.DISCOUNT_TYPE,A.DISCOUNT,A.ITEM_RATES,SUM(B.UNITCOST * B.QTYOR) AS UNITCOST,SUM(B.UNITPRICE * B.QTYOR) AS TOTAL_PRICE, "
			   	+ "CASE WHEN ISNULL(A.ITEM_RATES, 1) = 1 THEN SUM((B.UNITPRICE * B.QTYOR) - (CASE  WHEN ISNULL(B.DISCOUNT_TYPE, '%') = '%' THEN ISNULL(((B.UNITPRICE * B.QTYOR) / 100) * B.DISCOUNT, 0)  ELSE ISNULL(B.DISCOUNT, 0) END)) "
			   	+ "ELSE SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE, '%') = '%' THEN ISNULL(((B.UNITPRICE * B.QTYOR) / 100) * B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END)) + (CASE WHEN ISNULL(A.ITEM_RATES, 1) = 1 THEN SUM((B.UNITPRICE * QTYOR) -  "
			   	+ "(CASE WHEN ISNULL(B.DISCOUNT_TYPE, '%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR) / 100) * B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END)) - (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE, '%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR) / 100) * B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) "
				+ "END))) / (100 + A.OUTBOUND_GST)) * 100)ELSE (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE, '%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR) / 100) * B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END))) / 100) * A.OUTBOUND_GST) END) END AS TOTAL_DISCOUNT "
				+ "FROM "+plant+"_DOHDR A JOIN "+plant+"_DODET B ON A.DONO = B.DONO WHERE A.ORDER_STATUS = 'PROCESSED'  AND A.ORDDATE ='"+posdate+"' AND A.OUTLET ='"+outlet+"' AND A.TERMINAL ='"+terminal+"' "
				+ "GROUP BY A.DONO, A.ORDDATE, A.OUTLET, A.TERMINAL, A.OUTBOUND_GST, A.DISCOUNT_TYPE, A.DISCOUNT, A.ITEM_RATES ) "
				+ "SELECT D.ORDDATE,D.OUTLET,D.TERMINAL,COALESCE(SUM(D.TOTAL_DISCOUNT), 0) - COALESCE(SUM(E.EXPRICE), 0) AS TOTALPRICE,(SELECT TOP 1 O.OUTLET_NAME FROM "+plant+"_POSOUTLETS AS O WHERE O.OUTLET = D.OUTLET) AS OUTLETNAME,  "
				+ "(SELECT TOP 1 T.TERMINAL_NAME FROM "+plant+"_POSOUTLETSTERMINALS AS T WHERE T.OUTLET = D.OUTLET AND T.TERMINAL = D.TERMINAL) AS TERMINALNAME,COALESCE(SUM(D.UNITCOST), 0) - COALESCE(SUM(E.EXCOST), 0) AS TOTALCOST "
				+ "FROM OrderDetails D LEFT JOIN ExchangeCosts E ON D.DONO = E.EXDONO GROUP BY D.ORDDATE, D.OUTLET, D.TERMINAL;");
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
   
   public String getstarttime(String plant,String posdate,String outlet, String terminal) throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		String intime = "";
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer("SELECT A.* FROM "+plant+"_POSTILLMONEY AS A WHERE A.TMDATE ='"+posdate+"' AND A.OUTLET ='"+outlet+"' AND A.TERMINAL ='"+terminal+"' ORDER BY ID DESC");
		try {
			con = com.track.gates.DbBean.getConnection();
			this.mLogger.query(this.printQuery, sql.toString());
			alData = selectData(con, sql.toString());
			
			if(alData.size() > 0) {
				String cratintime = alData.get(0).get("CRAT");
				intime = new DateUtils().getDB2Time(cratintime);
			}else {
				intime = "00:00";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return intime;
	}
   
   public String getclosingtime(String plant,String posdate,String outlet, String terminal) throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		String intime = "";
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer("SELECT A.* FROM "+plant+"_POSSHIFTCLOSEHDR AS A WHERE A.SHIFTDATE ='"+posdate+"' AND A.OUTLET ='"+outlet+"' AND A.TERMINAL ='"+terminal+"' ORDER BY ID DESC");
		try {
			con = com.track.gates.DbBean.getConnection();
			this.mLogger.query(this.printQuery, sql.toString());
			alData = selectData(con, sql.toString());
			
			if(alData.size() > 0) {
				String cratintime = alData.get(0).get("CRAT");
				intime = new DateUtils().getDB2Time(cratintime);
			}else {
				intime = "00:00";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return intime;
	}
   
   
   public List<DoHdr> getDoHdrPeppol(String plant) throws Exception {
//	   boolean flag = false;
//	   int journalHdrId = 0;
	   Connection connection = null;
	   PreparedStatement ps = null;
	   String query = "", fields = "", dtCondStr = "";
	   List<DoHdr> doHeaders = new ArrayList<DoHdr>();
	   List<String> args = null;
	   try {
		   args = new ArrayList<String>();
		   connection = DbBean.getConnection();
		   query = "SELECT [PLANT]" + 
		   		"      ,[DONO]" + 
		   		"      ,[VENDNO]" + 
		   		"      ,[ORDDATE]" + 
		   		"      ,[ORDERTYPE]" + 
		   		"      ,[DELDATE]" + 
		   		"      ,[STATUS]" + 
		   		"      ,[PickStaus]" + 
		   		"      ,[CRAT]" + 
		   		"      ,[CRBY]" + 
		   		"      ,[UPAT]" + 
		   		"      ,[UPBY]" + 
		   		"      ,[RECSTAT]" + 
		   		"      ,[CustCode]" + 
		   		"      ,[CustName]" + 
		   		"      ,[JobNum]" + 
		   		"      ,[PersonInCharge]" + 
		   		"      ,[contactNum]" + 
		   		"      ,[Address]" + 
		   		"      ,[Address2]" + 
		   		"      ,[Address3]" + 
		   		"      ,[CollectionDate]" + 
		   		"      ,[CollectionTime]" + 
		   		"      ,[Remark1]" + 
		   		"      ,[Remark2]" + 
		   		"      ,[SHIPPINGID]" + 
		   		"      ,[SHIPPINGCUSTOMER]" + 
		   		"      ,[CURRENCYID]" + 
		   		"      ,ISNULL([DELIVERYDATE],'') DELIVERYDATE" + 
		   		"      ,[TIMESLOTS]" + 
		   		"      ,[OUTBOUND_GST]" + 
		   		"      ,[STATUS_ID]" + 
		   		"      ,[EMPNO]" + 
		   		"      ,ISNULL([ESTNO], '') ESTNO" + 
		   		"      ,[PAYMENT_STATUS]" + 
		   		"      ,[Remark3]" + 
		   		"      ,[ORDERDISCOUNT]" + 
		   		"      ,[SHIPPINGCOST]" + 
		   		"      ,[INCOTERMS]" + 
		   		"      ,[DNPLREMARKS]" + 
		   		"      ,[PAYMENTTYPE]" + 
		   		"      ,ISNULL(DELIVERYDATEFORMAT,0) DELIVERYDATEFORMAT" + 
		   		"      ,[APPROVESTATUS]" + 
		   		"      ,[SALES_LOCATION]" + 
		   		"      ,[TAXTREATMENT]" + 
		   		"	   ,ISNULL([DISCOUNT], 0) DISCOUNT" +
				"      ,ISNULL([DISCOUNT_TYPE], '') DISCOUNT_TYPE" +
				"      ,ISNULL([ADJUSTMENT], 0) ADJUSTMENT" +
				"      ,ISNULL([ITEM_RATES], 0) ITEM_RATES" +
				"      ,ISNULL([CURRENCYUSEQT], 0) CURRENCYUSEQT" +
				"      ,ISNULL([ORDERDISCOUNTTYPE], '') ORDERDISCOUNTTYPE" +
				"      ,ISNULL([TAXID], 0) TAXID" +
				"      ,ISNULL([ISDISCOUNTTAX], 0) ISDISCOUNTTAX" +
				"      ,ISNULL([ISORDERDISCOUNTTAX], 0) ISORDERDISCOUNTTAX" +
				"      ,ISNULL([ISSHIPPINGTAX], 0) ISSHIPPINGTAX" +
				"      ,ISNULL([PROJECTID], '') PROJECTID" +
				"      ,ISNULL([TRANSPORTID], '') TRANSPORTID" +
				"      ,ISNULL([PAYMENT_TERMS], '') PAYMENT_TERMS" +
				"      ,[SHIPCONTACTNAME]" +
				"           ,[SHIPDESGINATION]" +
				"           ,[SHIPWORKPHONE]" +
				"           ,[SHIPHPNO]" +
				"           ,[SHIPEMAIL]" +
				"           ,[SHIPCOUNTRY]" +
				"           ,[SHIPADDR1]" +
				"           ,[SHIPADDR2]" +
				"           ,[SHIPADDR3]" +
				"           ,[SHIPADDR4]" +
				"           ,[SHIPSTATE]" +
				"           ,[SHIPZIP]" +
				"           ,ISNULL(APP_CUST_ORDER_STATUS,'Pending') APP_CUST_ORDER_STATUS" +
				"      ,ISNULL([ID], '') ID" +
				"      ,ISNULL([APP_CUST_ORDER_STATUS], '') APP_CUST_ORDER_STATUS" +
				"      ,ISNULL([REASON], '') REASON" +
				"      ,ISNULL([UKEY], '') UKEY" +
				"      ,ISNULL([ISFROMPEPPOL], 0) ISFROMPEPPOL" +
				"      ,ISNULL([SALESORDERUUID], '') SALESORDERUUID" +
				"      ,ISNULL([ISINVOICED], 0) ISINVOICED" +
				"      ,ISNULL([INVOICEDID], '') INVOICEDID" +
		   		"      ,ISNULL(ORDER_STATUS,'Open') ORDER_STATUS FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ISNULL(ISFROMPEPPOL,0) = 1";
		  
			/*dtCondStr =" AND ISNULL(DELDATE,'')<>'' AND CAST((SUBSTRING(DELDATE, 7, 4) + '-' + SUBSTRING(DELDATE, 4, 2) + '-' + SUBSTRING(DELDATE, 1, 2)) AS date) ";
			
			if (afrmDate.length() > 0) {
				query +=  dtCondStr + "  >= '" + afrmDate + "' ";
  				if (atoDate.length() > 0) {
  					query += dtCondStr+ " <= '" + atoDate + "' ";
  				}
			} else {
  				if (atoDate.length() > 0) {
  					query += dtCondStr+ " <= '" + atoDate + "' ";
  				}
			}
			
			query += " ORDER BY CAST((SUBSTRING(DELDATE,7,4) + '-' + SUBSTRING(DELDATE,4,2) + '-' + SUBSTRING(DELDATE,1,2))AS DATE) DESC, DONO DESC";*/
		   
		   query += " ORDER BY ID DESC";
		   
			ps = connection.prepareStatement(query);
			if(connection != null){
				int i = 1;
				for (Object arg : args) {         
				    if (arg instanceof Integer) {
				        ps.setInt(i++, (Integer) arg);
				    } else if (arg instanceof Long) {
				        ps.setLong(i++, (Long) arg);
				    } else if (arg instanceof Double) {
				        ps.setDouble(i++, (Double) arg);
				    } else if (arg instanceof Float) {
				        ps.setFloat(i++, (Float) arg);
				    } else {
				        ps.setNString(i++, (String) arg);
				    }
				}
			    /* Execute the Query */  
				ResultSet rst  = ps.executeQuery();
				while (rst.next()) {	
					DoHdr dohdr = new DoHdr();
					ResultSetToObjectMap.loadResultSetIntoObject(rst, dohdr);
					doHeaders.add(dohdr);
				}
			}			
	   }catch (Exception e) {
		   this.mLogger.exception(this.printLog, "", e);
			throw e;
	   } finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
	   return doHeaders;
	}
   
   public String getActualDiscoutForInvoice(String plant, String dono) throws Exception {
	      java.sql.Connection con = null;
	      String orderdiscount = "";
	     String SqlQuery="";
	      try {
	        con = DbBean.getConnection();
	        SqlQuery = "SELECT (a.ORDERDISCOUNT - ( SELECT ISNULL(SUM(CAST(ISNULL(b.ORDER_DISCOUNT,'0') AS float)),0) FROM  ["+plant+"_FININVOICEHDR] b"
	        		+ " WHERE b.DONO = a.DONO) ) AS ORDERDISCOUNT FROM ["+plant+"_DOHDR] a " + 
	        		"WHERE a.DONO = '"+dono+"' GROUP BY DONO, ORDERDISCOUNT";
	        System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery);
	       orderdiscount = (String) m.get("ORDERDISCOUNT");
	       
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return orderdiscount;
	  }
   
   
}

