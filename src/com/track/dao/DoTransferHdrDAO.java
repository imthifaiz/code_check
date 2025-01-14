package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.db.object.DoHdr;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class DoTransferHdrDAO extends BaseDAO {

	StrUtils _StrUtils = null;
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.DOTRANSFERHDRDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.DOTRANSFERHDRDAO_PRINTPLANTMASTERLOG;
	public static String TABLE_HEADER = "DOTRANSFERHDR";

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

	public DoTransferHdrDAO() {
		_StrUtils = new StrUtils();
	}

	public static String TABLE_NAME = "DOTRANSFERHDR";
	public static String plant = "";

	public DoTransferHdrDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "DOTRANSFERHDR" + "]";
	}

	public Map selectRow(String query, Hashtable ht) throws Exception {

		Map map = new HashMap();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "DOTRANSFERHDR" + "]");
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
					+ "[" + ht.get("PLANT") + "_" + "DOTRANSFERHDR" + "]");
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
			DbBean.closeConnection(con);
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
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "DOTRANSFERHDR" + "]");
			sql.append(" WHERE  " + formCondition(ht));

			this.mLogger.query(this.printQuery, sql.toString());

			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
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
			sql.append(" FROM " + TABLE_NAME);
			sql.append(" WHERE  " + formCondition(ht));

			if (extCond.length() > 0)
				sql.append(" and " + extCond);

			this.mLogger.query(this.printQuery, sql.toString());

			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return flag;

	}

	public ArrayList selectTransferOrderHdrForPda(String query, Hashtable ht)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOTRANSFERHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "TO_ASSIGNEE_MASTER" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.tono ='"
						+ ht.get("TONO") + "' and a.custname=b.ASSIGNENAME";

				sql.append(conditon);

			}
			this.mLogger.query(this.printQuery, sql.toString());

			alData = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return alData;
	}

	public ArrayList selectTransferOrderHdr(String query, Hashtable ht)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOTRANSFERHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "TO_ASSIGNEE_MASTER" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.tono ='"
						+ ht.get("TONO") + "' and a.custname=b.ASSIGNENAME";

				sql.append(conditon);

			}
			this.mLogger.query(this.printQuery, sql.toString());

			alData = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return alData;
	}

	public ArrayList selectDOTRANSFERHDR(String query, Hashtable ht) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOTRANSFERHDR" + "]");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

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
			DbBean.closeConnection(con);
		}
		return alData;
	}

	public ArrayList selectDOTRANSFERHDR(String query, Hashtable ht, String extCond)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOTRANSFERHDR" + "]");
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
			DbBean.closeConnection(con);
		}
		return alData;
	}

	public ArrayList selectDOTRANSFERHDROpenNew(String query, Hashtable ht,
			String extCond) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOTRANSFERHDR" + "] a," + "["
				+ ht.get("PLANT") + "_"
				+ "TODET ] b WHERE a.tono=b.tono AND a.PLANT='"
				+ ht.get("PLANT") + "' ");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();
			if (extCond.length() > 0)
				sql.append(" " + extCond);
			sql.append(" " + " group by a.tono, a.CustName, a.Remark1 ");
			this.mLogger.query(this.printQuery, sql.toString());

			alData = selectData(con, sql.toString());

		} catch (Exception e) {
			MLogger.log(-1, "selectDOTRANSFERHDR() Exception :: " + e.getMessage());
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return alData;
	}

	public boolean insertDOTRANSFERHDR(Hashtable ht) throws Exception {
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
System.out.println("");
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "DOTRANSFERHDR" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Transfer order created already");
		} finally {
			DbBean.closeConnection(conn);
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
					+ htCondition.get("PLANT") + "_" + "DOTRANSFERHDR" + "]");
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
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;
	}

	public String getMaxDo(String Plant) throws Exception {

		String MaxDo = "";
		try {
			String query = " max(" + "DoTO" + ")  as DoTO";

			Hashtable ht = new Hashtable();

			ht.put("PLANT", Plant);

			Map m = selectRow(query, ht);
			MaxDo = (String) m.get("ToNO");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return MaxDo;

	}

	public String getNextOrder(String Plant) throws Exception {
		String MaxTo = "";
		try {
			String query = " isnull(max(" + "ToNO" + "),'')  as ToNO";
			Hashtable ht = new Hashtable();

			ht.put("PLANT", Plant);

			String extCond = " substring(ToNo,2,4)='" + DateUtils.getDateYYMM()
					+ "'";
			Map m = selectRow(query, ht, extCond);
			MaxTo = (String) m.get("ToNO");

			if (MaxTo.length() == 0 || MaxTo.equalsIgnoreCase(null)
					|| MaxTo == "") {
				MaxTo = DateUtils.getDateYYMM() + "00000";
			}
			String temp = MaxTo.replace('T', '0');
			int nextNum = Integer.parseInt(temp) + 1;
			MaxTo = String.valueOf(nextNum);
			if (MaxTo.length() < 9)
				MaxTo = "T" + MaxTo;
			MaxTo = "T" + MaxTo;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return MaxTo;
	}

	public ArrayList getTransferOrderLocDetailsByWms(String plant,
			String orderno) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			StringBuffer sQry = new StringBuffer(
					"select fromwarehouse,towarehouse ");
			sQry.append(" from  " + "[" + plant + "_" + "DOTRANSFERHDR" + "]");
			sQry.append(" where plant='" + plant + "' and tono = '" + orderno
					+ "'and status <> 'C'");

			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "DOTRANSFERHDR";
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return al;
	}

	public ArrayList selectDOTRANSFERHDRReciving(String query, Hashtable ht,
			String extCond) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOTRANSFERHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "TO_ASSIGNEE_MASTER" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.tono like'"
						+ ht.get("DONO") + "%' and a.custname=b.assignename";

				sql.append(conditon);

			}
			extCond = "";
			if (extCond.length() > 0)
				sql.append(" " + extCond);
			this.mLogger.query(this.printQuery, sql.toString());

			alData = selectData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return alData;
	}

	public ArrayList selectAssigneeHdrReciving(String query, Hashtable ht,
			String extCond) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOTRANSFERHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "TO_ASSIGNEE_MASTER" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.tono ='"
						+ ht.get("DONO") + "' and a.custname=b.assignename";

				sql.append(conditon);

			}
			extCond = "";
			if (extCond.length() > 0)
				sql.append(" " + extCond);
			this.mLogger.query(this.printQuery, sql.toString());

			alData = selectData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return alData;
	}

	public Boolean removeOrder(String plant2, String dono) throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant2 + "_" + "DOTRANSFERHDR] WHERE DONO='"
					+ dono + "' AND PLANT='" + plant2 + "'";
			this.mLogger.query(this.printQuery, sql.toString());
			this.DeleteRow(connection, sql);
			return Boolean.valueOf(true);
		} catch (Exception e) {
			MLogger.log(0, "[ERROR] : " + e);
			return Boolean.valueOf(false);
		} finally {
			DbBean.closeConnection(connection);
		}
	}

	public String getLocation(String plant2, String tono,
			String locaionColomString) throws Exception {
		String location = "";

		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + locaionColomString
					+ " from " + "[" + plant2 + "_" + "DOTRANSFERHDR" + "]");
			sql.append(" WHERE PLANT='" + plant2 + "' and TONO='" + tono + "'");

			this.mLogger.query(this.printQuery, sql.toString());
			Map map = getRowOfData(con, sql.toString());
			location = (String) map.get(locaionColomString);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return location;
	}
		
	public ArrayList selectOutGoingDoHdr(String query, Hashtable ht)
	throws Exception {
boolean flag = false;
ArrayList alData = new ArrayList();
java.sql.Connection con = null;
StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
		+ ht.get("PLANT") + "_" + "DOTRANSFERHDR" + "] A,");
sql.append("[" + ht.get("PLANT") + "_" + "CUSTMST" + "] B");
String conditon = "";

try {
	con = com.track.gates.DbBean.getConnection();

	if (ht.size() > 0) {
		sql.append(" WHERE ");

		conditon = "a.plant='" + ht.get("PLANT") + "' and a.status<>'C' and a.dono ='"
				+ ht.get("DONO") + "' and a.custname=b.cname";

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
	
	 public ArrayList selectDoTransferHdr(String query, Hashtable ht)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOTRANSFERHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "CUSTMST" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.status<>'C' and a.dono ='"
						+ ht.get("DONO") + "' and a.custname=b.cname";

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
	 
	 public ArrayList selectDoHdrForOutBound(String query, Hashtable ht,
				String extCond) throws Exception {
			boolean flag = false;
			ArrayList alData = new ArrayList();

			java.sql.Connection con = null;

			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ ht.get("PLANT") + "_" + "DOTRANSFERHDR" + "] a, [" + ht.get("PLANT")
					+ "_DODET] b WHERE a.dono=b.dono AND a.STATUS <> 'C' AND a.PLANT='"
					+ ht.get("PLANT") + "' ");
			String conditon = "";

			try {
				con = com.track.gates.DbBean.getConnection();

				if (extCond.length() > 0)
					sql.append(" " + extCond);
				sql.append(" " + " group by a.dono, a.CustName, a.Remark1 ");
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
	 public ArrayList selectDoTransferHdrForOutBound(String query, Hashtable ht,
				String extCond) throws Exception {
			boolean flag = false;
			ArrayList alData = new ArrayList();

			java.sql.Connection con = null;

			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ ht.get("PLANT") + "_" + "DOTRANSFERHDR" + "] a, [" + ht.get("PLANT")
					+ "_DOTRANSFERDET] b WHERE a.dono=b.dono AND a.STATUS <> 'C' AND a.PLANT='"
					+ ht.get("PLANT") + "' ");
			String conditon = "";
			
			System.out.println(sql.toString());
			try {
				con = com.track.gates.DbBean.getConnection();

				if (extCond.length() > 0)
					sql.append(" " + extCond);
				sql.append(" " + " group by a.dono, a.CustName, a.Remark1 ");
				sql.append(" " + " order by a.dono desc ");
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
	 
	 public boolean insertDoTransferHdr(DoHdr doHdr) throws Exception {
			boolean insertFlag = false;
			boolean flag = false;
			int HdrId = 0;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
			try {	    
				connection = DbBean.getConnection();
				query = "INSERT INTO [dbo].["+doHdr.getPLANT()+"_"+TABLE_HEADER+"]" + 
						"           ([PLANT]" + 
						"           ,[DONO]" + 
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
						"           ,[DELIVERYDATE]" + 
						"           ,[TIMESLOTS]" + 
						"           ,[OUTBOUND_GST]" + 
						"           ,[EMPNO]" + 
						"           ,[ESTNO]" + 
						"           ,[Remark3]" + 
						"           ,[ORDERDISCOUNT]" + 
						"           ,[SHIPPINGCOST]" + 
						"           ,[INCOTERMS]" + 
						"           ,[PAYMENTTYPE]" + 
						"           ,[DELIVERYDATEFORMAT]" + 
						"           ,[SALES_LOCATION]" + 
						"           ,[ORDER_STATUS]" + 
						"           ,[DISCOUNT]" +
						"           ,[DISCOUNT_TYPE]" +
						"      		,[ADJUSTMENT]" +
						"           ,[TAXTREATMENT]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, doHdr.getPLANT());
				   ps.setString(2, doHdr.getDONO());
				   ps.setString(3, doHdr.getDELDATE());
				   ps.setString(4, doHdr.getSTATUS());
				   ps.setString(5, doHdr.getPickStaus());
				   ps.setString(6, doHdr.getCRAT());
				   ps.setString(7, doHdr.getCRBY());
				   ps.setString(8, doHdr.getCustCode());
				   ps.setString(9, doHdr.getCustName());
				   ps.setString(10, doHdr.getJobNum());
				   ps.setString(11, doHdr.getPersonInCharge());
				   ps.setString(12, doHdr.getContactNum());
				   ps.setString(13, doHdr.getAddress());
				   ps.setString(14, doHdr.getAddress2());
				   ps.setString(15, doHdr.getAddress3());
				   ps.setString(16, doHdr.getCollectionDate());
				   ps.setString(17, doHdr.getCollectionTime());
				   ps.setString(18, doHdr.getRemark1());
				   ps.setString(19, doHdr.getRemark2());
				   ps.setString(20, doHdr.getSHIPPINGID());
				   ps.setString(21, doHdr.getSHIPPINGCUSTOMER());
				   ps.setString(22, doHdr.getDELIVERYDATE());
				   ps.setString(23, doHdr.getTIMESLOTS());
				   ps.setString(24, Double.toString(doHdr.getOUTBOUND_GST()));
				   ps.setString(25, doHdr.getEMPNO());
				   ps.setString(26, "");
				   ps.setString(27, doHdr.getRemark3());
				   ps.setString(28, Double.toString(doHdr.getORDERDISCOUNT()));
				   ps.setString(29, Double.toString(doHdr.getSHIPPINGCOST()));
				   ps.setString(30, doHdr.getINCOTERMS());
				   ps.setString(31, doHdr.getPAYMENTTYPE());
				   ps.setString(32, Short.toString(doHdr.getDELIVERYDATEFORMAT()));
				   ps.setString(33, doHdr.getSALES_LOCATION());
				   ps.setString(34, doHdr.getORDER_STATUS());
				   ps.setString(35, Double.toString(doHdr.getDISCOUNT()));
				   ps.setString(36, doHdr.getDISCOUNT_TYPE());
				   ps.setString(37, Double.toString(doHdr.getADJUSTMENT()));
				   ps.setString(38, doHdr.getTAXTREATMENT());
				  
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
	 
	 public boolean updateDoTransferHdr(DoHdr doHdr) throws Exception {
			boolean updateFlag = false;
			boolean flag = false;
			int HdrId = 0;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
			try {	    
				connection = DbBean.getConnection();
				query = "UPDATE ["+doHdr.getPLANT()+"_"+TABLE_HEADER+"] SET " + 
						"           [PLANT] = ?" +  
						"           ,[STATUS] = ?" + 
						"           ,[PickStaus] = ?" + 
						"           ,[UPAT] = ?" + 
						"           ,[UPBY] = ?" +  
						"           ,[JobNum] = ?" + 
						"           ,[Remark1] = ?" + 
						"           ,[Remark2] = ?" + 
						"           ,[SHIPPINGID] = ?" + 
						"           ,[SHIPPINGCUSTOMER] = ?" + 
						"           ,[DELIVERYDATE] = ?" + 
						"           ,[TIMESLOTS] = ?" + 
						"           ,[OUTBOUND_GST] = ?" + 
						"           ,[EMPNO] = ?" + 
						"           ,[ESTNO] = ?" + 
						"           ,[Remark3] = ?" + 
						"           ,[ORDERDISCOUNT] = ?" + 
						"           ,[SHIPPINGCOST] = ?" + 
						"           ,[INCOTERMS] = ?" + 
						"           ,[PAYMENTTYPE] = ?" + 
						"           ,[DELIVERYDATEFORMAT] = ?" + 
						"           ,[SALES_LOCATION] = ?" + 
						"           ,[ORDER_STATUS] = ?" + 
						"           ,[DISCOUNT] = ?" +
						"           ,[DISCOUNT_TYPE] = ?" +
						"      		,[ADJUSTMENT] = ?" +
						"           ,[TAXTREATMENT] = ? WHERE DONO = ?";

				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, doHdr.getPLANT());
				   ps.setString(2, doHdr.getSTATUS());
				   ps.setString(3, doHdr.getPickStaus());
				   ps.setString(4, doHdr.getUPAT());
				   ps.setString(5, doHdr.getUPBY());
				   ps.setString(6, doHdr.getJobNum());
				   ps.setString(7, doHdr.getRemark1());
				   ps.setString(8, doHdr.getRemark2());
				   ps.setString(9, doHdr.getSHIPPINGID());
				   ps.setString(10, doHdr.getSHIPPINGCUSTOMER());
				   ps.setString(11, doHdr.getDELIVERYDATE());
				   ps.setString(12, doHdr.getTIMESLOTS());
				   ps.setString(13, Double.toString(doHdr.getOUTBOUND_GST()));
				   ps.setString(14, doHdr.getEMPNO());
				   ps.setString(15, "");
				   ps.setString(16, doHdr.getRemark3());
				   ps.setString(17, Double.toString(doHdr.getORDERDISCOUNT()));
				   ps.setString(18, Double.toString(doHdr.getSHIPPINGCOST()));
				   ps.setString(19, doHdr.getINCOTERMS());
				   ps.setString(20, doHdr.getPAYMENTTYPE());
				   ps.setString(21, Short.toString(doHdr.getDELIVERYDATEFORMAT()));
				   ps.setString(22, doHdr.getSALES_LOCATION());
				   ps.setString(23, doHdr.getORDER_STATUS());
				   ps.setString(24, Double.toString(doHdr.getDISCOUNT()));
				   ps.setString(25, doHdr.getDISCOUNT_TYPE());
				   ps.setString(26, Double.toString(doHdr.getADJUSTMENT()));
				   ps.setString(27, doHdr.getTAXTREATMENT());
				   
				   ps.setString(28, doHdr.getDONO());
				  
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



}
