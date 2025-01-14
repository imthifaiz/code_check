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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class DNPLHdrDAO extends BaseDAO {

	StrUtils _StrUtils = null;
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.DNPLHDRDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.DNPLHDRDAO_PRINTPLANTMASTERLOG;

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

	public DNPLHdrDAO() {
		_StrUtils = new StrUtils();
	}

	public static String TABLE_NAME = "DNPLHDR";
	public static String plant = "";

	public DNPLHdrDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "DNPLHDR" + "]";
	}

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "DNPLHDR" + "]");
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
					+ "[" + ht.get("PLANT") + "_" + "DNPLHDR" + "]");
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
        
    public String getOrderTypeForDO(String aPlant, String aDono) throws Exception {
            String OrderType = "";
            Hashtable ht = new Hashtable();
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
			sql.append(" FROM " + "["+ht.get("PLANT")+"_DNPLHDR]");
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
			sql.append(" FROM " + "["+ht.get("PLANT")+"_DNPLHDR]");
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
	
	public ArrayList selectdnplDNPLHDR(String query, Hashtable ht)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DNPLHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "CUSTMST" + "] B,");
		sql.append("[" + ht.get("PLANT") + "_" + "SHIPPINGHDR" + "] S");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");

				//conditon = "a.plant='" + ht.get("PLANT") + "' and a.dono ='"
						//+ ht.get("DONO") + "' and a.invoiceno = '" + ht.get("INVOICENO") + "' and a.CustCode=b.custno";
				conditon = "a.plant='" + ht.get("PLANT") + "' and a.dono ='"
						+ ht.get("DONO") + "' and a.GINO = '" + ht.get("INVOICENO") + "' and a.CUSTNO=b.custno AND A.ID=S.DNPLID";

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

	public ArrayList selectOutGoingDNPLHDR(String query, Hashtable ht)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DNPLHDR" + "] A,");
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

	public ArrayList selectDNPLHDR(String query, Hashtable ht) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DNPLHDR" + "]");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				this.mLogger.log(0, "condition preisent stage 3");
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

	public ArrayList selectDNPLHDR(String query, Hashtable ht, String extCond)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		
		String plant = (String) ht.get("PLANT");

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ plant + "_" + "DNPLHDR" + "]");
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

	public int addDNPLHDR(Map ht, String plant)throws Exception {
		boolean flag = false;
		int dnplHdrId = 0;
		int count = 0;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
	    String query = "";
		try {
			 /*Instantiate the list*/
		    args = new ArrayList<String>();		    
			connection = DbBean.getConnection();			
			String FIELDS = "", VALUES = "";
			Iterator enum1 = ht.keySet().iterator();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.next());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				if ("".equals(value)) {
					VALUES += "NULL,";
				}else {
					VALUES += "'" + value + "',";
				}
			}
			query = "INSERT INTO ["+ plant +"_DNPLHDR] ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			//invoiceHdrId = execute_NonSelectQueryGetLastInsert(ps, args);
			count = ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			   if (rs.next()){
				   dnplHdrId = rs.getInt(1);
			   }
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return dnplHdrId;
	}

	
	public boolean insertDNPLHDR(Map ht) throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Iterator enum1 = ht.keySet().iterator();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.next());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				if ("".equals(value)) {
					VALUES += "NULL,";
				}else {
					VALUES += "'" + value + "',";
				}
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "DNPLHDR" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Delivery Note / Packing List created already");
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
					+ htCondition.get("PLANT") + "_" + "DNPLHDR" + "]");
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
				String key = _StrUtils.fString((String) enum1.nextElement());
				String value = _StrUtils.fString((String) ht.get(key));
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
			String sql = "DELETE FROM [" + plant2 + "_" + "DNPLHDR] WHERE DONO='"
					+ dono + "' AND PLANT='" + plant2 + "'";
			this.mLogger.query(this.printQuery, sql.toString());
			this.DeleteRow(connection, sql);
			return Boolean.valueOf(true);
		} catch (Exception e) {
			this.mLogger.log(0, "[ERROR] : " + e);
			return Boolean.valueOf(false);
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
	}
        
        
        
        

	public ArrayList selectDNPLHDRForOutBound(String query, Hashtable ht,
			String extCond) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DNPLHDR" + "] a, [" + ht.get("PLANT")
				+ "_DODET] b WHERE a.dono=b.dono AND a.PLANT='"
				+ ht.get("PLANT") + "' ");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (extCond.length() > 0)
				sql.append(" " + extCond);
			sql.append(" " + " group by a.dono, a.CustName, a.collectiondate,a.status,a.Remark1 ORDER BY a.dono desc");
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
		boolean flag = false;
		ArrayList alData = new ArrayList();
		String orderno="",mobileno="",custcode="";
		java.sql.Connection con = null;
		orderno = (String)ht.get("DONO");
		mobileno = (String)ht.get("MOBILENO");
		if(mobileno.length()>0)
		{
     custcode = getCustCode((String)ht.get("PLANT"), mobileno);
		}
	StringBuffer sql = new StringBuffer(" SELECT TOP 1  DONO,case When STATUS='O' then 'PENDING' WHEN STATUS='N' then 'PENDING' When STATUS='C' then 'ORDER SUCCESSFUL'  ELSE status END STATUS,CustCode,CustName from " + "["
				+ ht.get("PLANT") + "_" + "DNPLHDR" + "] where PLANT='"
				+ ht.get("PLANT") + "' "+"AND ORDERTYPE='Mobile Order' ");
	//sql.append(" AND STATUS <>'C'");
		if(orderno.length()>0)
			sql.append(" AND DONO='"+orderno+"'");
		if(custcode.length()>0)
			sql.append(" AND CUSTCODE='"+custcode+"'");
		
		String conditon = "";

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
		boolean flag = false;
		ArrayList alData = new ArrayList();

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from PLNTMST "
				+ " WHERE PLANT='"
				+ ht.get("PLANT") + "' ");
		String conditon = "";

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

			boolean flag = false;

			StringBuffer sQry = new StringBuffer(
					"select a.dono,a.custname,");
			sQry
					.append(" isnull(b.name,'') contactname,isnull(b.telno,'') as telno,isnull(b.hpno,'') as hpno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3 ");
			sQry.append(" from  " + "[" + plant + "_" + "DNPLHDR" + "]" + " a,"
					+ "[" + plant + "_" + "custmst" + "]" + " b");
			sQry
					.append(" where a.plant='" + plant + "' and a.dono = '"
							+ orderno
							+ "' and a.STATUS <> 'C' and a.custname=b.cname");

			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "DNPLHDR";
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
         String scondtn ="";
         try {
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer("  SELECT  ");
                 sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE,ISNULL(PRITNWITHHSCODE,'0') AS PRITNWITHHSCODE,ISNULL(PRITNWITHCOO,'0') AS PRITNWITHCOO ,ISNULL(PRINTBARCODE,'0') AS PRINTBARCODE,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,ISNULL(PCUSREMARKS,'0') AS PCUSREMARKS,");//
                 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(UOM,'') AS UOM , ");
                 sql.append("ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2 ,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(FOOTER5,'') AS F5, ");
                 sql.append("ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9, ");
                 sql.append("ISNULL(CONTAINER,'') Container,ISNULL(DisplayContainer,'0') AS DisplayContainer,ISNULL(PrintLocStock,'0') AS PrintLocStock, ");
                 sql.append("ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2,ISNULL(HSCODE,'') HSCODE,ISNULL(COO,'') COO,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EMPLOYEE,'') EMPLOYEE,ISNULL(SHIPTO,'') SHIPTO, ");
                 sql.append("ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(PRINTEMPLOYEE,'') PRINTEMPLOYEE,ISNULL(PrintOrientation,'Landscape') PrintOrientation, ");
                 sql.append(" ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM,ISNULL(RCBNO,'') AS RCBNO,ISNULL(PRINTWITHPRODUCTREMARKS,'0') AS PRINTWITHPRODUCTREMARKS,ISNULL(PRINTWITHBRAND,'0') AS PRINTWITHBRAND,ISNULL(CUSTOMERRCBNO,'') AS CUSTOMERRCBNO");
                 
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
         String scondtn ="";
         try {
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer("  SELECT  ");
                 sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE ,ISNULL(PRINTBARCODE,'0') AS PRINTBARCODE,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,ISNULL(PCUSREMARKS,'0') AS PCUSREMARKS,");//
                 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(UOM,'') AS UOM,ISNULL(PRITNWITHHSCODE,'0') AS PRITNWITHHSCODE,ISNULL(PRITNWITHCOO,'0') AS PRITNWITHCOO,");
                 sql.append("ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2 ,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(FOOTER5,'') AS F5, ");
                 sql.append("ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9, ");
                 sql.append("ISNULL(CONTAINER,'') Container,ISNULL(DisplayContainer,'0') AS DisplayContainer,ISNULL(PrintLocStock,'0') AS PrintLocStock, ");
                 sql.append("ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2,ISNULL(HSCODE,'') HSCODE,ISNULL(COO,'') COO,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EMPLOYEE,'') EMPLOYEE,ISNULL(SHIPTO,'') SHIPTO, ");
                 sql.append("ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(PRINTEMPLOYEE,'') PRINTEMPLOYEE,ISNULL(DISPLAYSIGNATURE,'') DISPLAYSIGNATURE,ISNULL(PrintOrientation,'Landscape') PrintOrientation, ");
                 sql.append(" ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM,ISNULL(RCBNO,'') AS RCBNO,ISNULL(PRINTPACKINGLIST,'0') AS PRINTPACKINGLIST,ISNULL(PRINTDELIVERYNOTE,'0') AS PRINTDELIVERYNOTE,ISNULL(PRINTWITHPRODUCTREMARKS,'0') AS PRINTWITHPRODUCTREMARKS,ISNULL(PRINTWITHBRAND,'0') AS PRINTWITHBRAND,ISNULL(CUSTOMERRCBNO,'') AS CUSTOMERRCBNO ");
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
         String scondtn ="";
         try {                 
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer("  SELECT  ");
                 sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(TERMS,'') AS TERMS ,ISNULL(TERMSDETAILS,'') AS TERMSDETAILS,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,ISNULL(PRINTCUSTERMS,'0') AS PRINTCUSTERMS,ISNULL(PCUSREMARKS,'0') AS PCUSREMARKS,");
                 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(RATE,'') AS RATE ,ISNULL(TAXAMOUNT,'') AS TAXAMOUNT,ISNULL(AMT,'') AS AMT, ");
                 sql.append("ISNULL(SUBTOTAL,'') AS SUBTOTAL,ISNULL(TOTALTAX,'') AS TOTALTAX,'Total With Tax' AS TOTAL,ISNULL(UOM,'') AS UOM, ");
                 sql.append("ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2 ,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(FOOTER5,'') AS F5, ");
                 sql.append("ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9, ");
                 sql.append("ISNULL(CONTAINER,'') Container,ISNULL(DisplayContainer,'0') AS DisplayContainer, ");
                 sql.append("ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2,ISNULL(HSCODE,'') HSCODE,ISNULL(COO,'') COO,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EMPLOYEE,'') EMPLOYEE,ISNULL(SHIPTO,'') SHIPTO, ");
                 sql.append("ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(PRINTEMPLOYEE,'') PRINTEMPLOYEE,ISNULL(PrintOrientation,'Landscape') PrintOrientation, ");
                 sql.append(" ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM,ISNULL(PRITNWITHHSCODE,'0') AS PRITNWITHHSCODE,ISNULL(PRITNWITHCOO,'0') AS PRITNWITHCOO,ISNULL(RCBNO,'') AS RCBNO,ISNULL( PRINTWITHPRODUCTREMARKS,'0') AS PRINTWITHPRODUCTREMARKS,ISNULL( PRINTWITHBRAND,'0') AS PRINTWITHBRAND,ISNULL( CUSTOMERRCBNO,'') AS CUSTOMERRCBNO,ISNULL( TOTALAFTERDISCOUNT,'') AS TOTALAFTERDISCOUNT ");
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
         Map m = new HashMap();
         java.sql.Connection con = null;
         String scondtn ="";
         try {                 
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer("  SELECT  ");
                 sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(TERMS,'') AS TERMS ,ISNULL(TERMSDETAILS,'') AS TERMSDETAILS,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE ,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,ISNULL(PRINTCUSTERMS,'0') AS PRINTCUSTERMS,ISNULL(PCUSREMARKS,'0') AS PCUSREMARKS,");
                 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(RATE,'') AS RATE ,ISNULL(TAXAMOUNT,'') AS TAXAMOUNT,ISNULL(AMT,'') AS AMT, ");
                 sql.append("ISNULL(SUBTOTAL,'') AS SUBTOTAL,ISNULL(TOTALTAX,'') AS TOTALTAX,'Total With Tax' AS TOTAL,ISNULL(UOM,'') AS UOM, ");
                 sql.append("ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2 ,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(FOOTER5,'') AS F5, ");
                 sql.append("ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9, ");
                 sql.append("ISNULL(CONTAINER,'') Container,ISNULL(DisplayContainer,'0') AS DisplayContainer, ");
                 sql.append("ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2,ISNULL(HSCODE,'') HSCODE,ISNULL(COO,'') COO,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EMPLOYEE,'') EMPLOYEE,ISNULL(SHIPTO,'') SHIPTO, ");
                 sql.append("ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(PRINTEMPLOYEE,'') PRINTEMPLOYEE,ISNULL(DISPLAYSIGNATURE,'') DISPLAYSIGNATURE,ISNULL(PrintOrientation,'Landscape') PrintOrientation,");
                 sql.append("ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM,ISNULL(PRITNWITHHSCODE,'0') AS PRITNWITHHSCODE,ISNULL(PRITNWITHCOO,'0') AS PRITNWITHCOO,ISNULL(RCBNO,'') AS RCBNO,ISNULL(PRINTWITHPRODUCTREMARKS,'0') AS PRINTWITHPRODUCTREMARKS,ISNULL(PRINTWITHBRAND,'0') AS PRINTWITHBRAND,ISNULL(INVOICENO,'') AS INVOICENO,ISNULL(CUSTOMERRCBNO,'') AS CUSTOMERRCBNO,ISNULL(TOTALAFTERDISCOUNT,'') AS TOTALAFTERDISCOUNT,ISNULL(INVOICEDATE,'') AS INVOICEDATE ");
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




        public ArrayList selectMobileEnquiryDNPLHDR(String query, Hashtable ht)
                        throws Exception {
                boolean flag = false;
                ArrayList alData = new ArrayList();
                java.sql.Connection con = null;
                StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
                                + ht.get("PLANT") + "_" + "DNPLHDR" + "] A,");
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
           

            StringBuffer SqlQuery = new StringBuffer(" select CAST(isnull(UnitPrice,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_DNPLHDR WHERE DONO ='"+doNO+"')) AS DECIMAL(20,2)) AS UNITCOST ");
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

        public String getUnitCostCovertedTolocalCurrency(String aPlant, String doNO,String unitCost) throws Exception {
            java.sql.Connection con = null;
            String UnitCostForSelCurrency = "";
            try {
                    
            	con = DbBean.getConnection();
	            StringBuffer SqlQuery = new StringBuffer(" select cast ("+unitCost+" / CAST((SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_DNPLHDR WHERE DONO =R.DONO)) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) ");
	            SqlQuery.append(" aS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) AS UNITCOST FROM "+aPlant+"_DNPLHDR R WHERE DONO='"+doNO+"' ");


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
							+ plant + "_DNPLHDR WHERE DONO ='" + doNO + "')");

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
	     

	      StringBuffer SqlQuery = new StringBuffer(" select CAST(UnitPrice *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_DNPLHDR WHERE DONO ='"+doNO+"')) AS DECIMAL(20,2)) AS UNITCOST ");
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
	  public ArrayList selectPDAMobileSalesDNPLHDR_details(String query, Hashtable ht,
				String extCond) throws Exception {
			boolean flag = false;
			ArrayList alData = new ArrayList();

			java.sql.Connection con = null;

			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ ht.get("PLANT") + "_" + "DNPLHDR" + "] " 
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
			boolean flag = false;
			ArrayList alData = new ArrayList();
			
			String plant = (String) ht.get("PLANT");

			java.sql.Connection con = null;

			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ plant + "_" + "SHIPHIS" + "]");
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
       public String getOBDiscountSelectedItem(String aPlant, String doNO,String aItem,String aType) throws Exception {
           java.sql.Connection con = null;
           String OBDiscount= "";
           String CustCode="";
           try {
           con = DbBean.getConnection();
           CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
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
 	           SqlQuery = " select isnull(custcode,'') custcode FROM ["+plant+"_DNPLHDR]  WHERE dono='"+dono+"'";
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
		boolean flag = false;
		ArrayList alData = new ArrayList();

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["	+ ht.get("PLANT") + "_" + "DNPLHDR" + "]   where dono='"+ht.get("DONO")+"' ");
		String conditon = "";

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
	
   public ArrayList selectDNPLHDRNewOrdersByItem(String query, Hashtable ht,
           String extCond) throws Exception {
   boolean flag = false;
   ArrayList alData = new ArrayList();
   java.sql.Connection con = null;

   StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
                   + ht.get("PLANT") + "_" + "DNPLHDR" + "] a, [" + ht.get("PLANT")
                   + "_" + "DODET" + "] b WHERE a.dono=b.dono AND a.PLANT='"+ ht.get("PLANT") + "' AND b.ITEM like'"+ ht.get("ITEM") + "%' ");

   String conditon = "";

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
   public boolean delete(java.util.Hashtable ht) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_DNPLHDR]");
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

	}


