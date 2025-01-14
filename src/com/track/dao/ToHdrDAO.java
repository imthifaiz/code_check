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
import com.track.db.object.ToHdr;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

public class ToHdrDAO extends BaseDAO {

	
	StrUtils _StrUtils = null;
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.TOHDRDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.TOHDRDAO_PRINTPLANTMASTERLOG;
	public static String TABLE_HEADER = "TOHDR";
	
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

	public ToHdrDAO() {
		_StrUtils = new StrUtils();
	}

	public static String TABLE_NAME = "TOHDR";
	public static String plant = "";

	public ToHdrDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "TOHDR" + "]";
	}

	public Map selectRow(String query, Hashtable ht) throws Exception {

		Map map = new HashMap();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "TOHDR" + "]");
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
					+ "[" + ht.get("PLANT") + "_" + "TOHDR" + "]");
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
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "TOHDR" + "]");
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
	
	public List<Map<String, String>> listConsignmentReverseGINO(String plant,String tono, String gino) throws Exception {
		Connection connection = null;
		List<Map<String, String>> returnOrderList = new ArrayList();
		PreparedStatement ps = null;
		List<String> args = null;
		String sQry = "";
		String condition = "";
		try {
			/*Instantiate the list*/
			args = new ArrayList<String>();			
			connection = DbBean.getConnection();
			
			if(tono.length()>0){
				condition += " and a.tono = ? ";
				args.add(tono);
			}
		 
			
			if(gino.length()>0){
				condition += " and a.GINO = ? ";
				args.add(gino);
			}  
			condition += " and a.plant = ? and  a.TONO like 'C%'";
			args.add(plant);
			
			

			sQry = "SELECT a.tono tono,tolno,isnull(a.GINO,'') as gino,a.cname, a.loc1 loc,a.item,a.itemdesc,"
					+ "isnull(ordqty,0) as qtyor,isnull(pickqty,0) as qtypick,batch,status,UNITMO,"
					+ "isnull((select ISNULL(QPUOM,1) from ["+plant+"_UOM] where UOM=UNITMO),1) UOMQTY  from "
					+ "["+plant+"_TO_PICK] a LEFT JOIN ["+plant+"_TODET] b ON a.TONO=b.TONO  "
					+ "where b.plant <> '' "
					+ "and b.TOLNNO=a.TOLNO and STATUS='o' or STATUS='C'  and b.ITEM=a.ITEM  "+ condition
					+ "group  by a.tono,tolno,a.GINO,pickqty,a.cname,loc1,a.item,a.itemdesc,ordqty,batch,status,"
					+ "UNITMO having isnull(sum(pickqty),0)>0 order by cast(tolno as int)";
			
			if(connection != null){
			   /*Create  PreparedStatement object*/
			   ps = connection.prepareStatement(sQry);
			    
			   this.mLogger.query(this.printQuery, sQry);
			    returnOrderList = selectData(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return returnOrderList;
	}

	public boolean isExisit(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM ["  + ht.get("PLANT") + "_" + "TOHDR" + "]");
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
	
	public String getCurrencyUseQT(String plant, String toNO) throws Exception {
		java.sql.Connection con = null;
		String getCurrencyUseQT = "";
		try {

			con = DbBean.getConnection();

			StringBuffer SqlQuery = new StringBuffer(
					" SELECT CURRENCYUSEQT FROM [" + plant + "_"
							+ "CURRENCYMST] where plant='" + plant
							+ "' AND CURRENCYID = (SELECT CURRENCYID from "
							+ plant + "_TOHDR WHERE TONO ='" + toNO + "')");

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

	public ArrayList selectTransferOrderHdrForPda(String query, Hashtable ht)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "TOHDR" + "] A,");
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
				+ ht.get("PLANT") + "_" + "TOHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "TO_ASSIGNEE_MASTER" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.tono ='"
						+ ht.get("TONO") + "' and a.CustCode=b.ASSIGNENO";

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

	public ArrayList selectConsignmentOrderHdr(String query, Hashtable ht)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "TOHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "CUSTMST" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.tono ='"
						+ ht.get("TONO") + "' and a.CustCode=b.CUSTNO";

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
	
	//copy
	public ArrayList selectConsignmentOrderGino(String query, Hashtable ht,String extCond)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "TOHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "CUSTMST" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

				sql.append(" WHERE ");
				if ( ht.get("TONO") != null) {
				conditon = "a.plant='" + ht.get("PLANT") + "' and a.tono ='"
						+ ht.get("TONO") + "' and a.CustCode=b.CUSTNO";
				
				

				}else {
					conditon = "a.plant='" + ht.get("PLANT") + "' and a.CustCode=b.CUSTNO";
						
				}
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
	
	public ArrayList selectToHdr(String query, Hashtable ht) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "TOHDR" + "]");
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

	public ArrayList selectToHdr(String query, Hashtable ht, String extCond)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "TOHDR" + "]");
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

	public ArrayList selectToHdrOpenNew(String query, Hashtable ht,
			String extCond) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "TOHDR" + "] a," + "["
				+ ht.get("PLANT") + "_"
				+ "TODET] b WHERE a.tono=b.tono AND a.PLANT='"
				+ ht.get("PLANT") + "' ");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();
			if (extCond.length() > 0)
				sql.append(" " + extCond);
			sql.append(" " + " group by a.tono, a.CustName, a.Remark1 ORDER BY a.tono desc");
			this.mLogger.query(this.printQuery, sql.toString());

			alData = selectData(con, sql.toString());

		} catch (Exception e) {
			MLogger.log(-1, "selectToHdr() Exception :: " + e.getMessage());
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return alData;
	}

	public boolean insertToHdr(Hashtable ht) throws Exception {
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

			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "TOHDR" + "]" + "("
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
					+ htCondition.get("PLANT") + "_" + "TOHDR" + "]");
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
			sQry.append(" from  " + "[" + plant + "_" + "tohdr" + "]");
			sQry.append(" where plant='" + plant + "' and tono = '" + orderno
					+ "'and status <> 'C'");

			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "TOHDR";
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return al;
	}

	public ArrayList selectToHdrReciving(String query, Hashtable ht,
			String extCond) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "TOHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "TO_ASSIGNEE_MASTER" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.tono like'"
						+ ht.get("TONO") + "%' and a.custname=b.assignename";

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
				+ ht.get("PLANT") + "_" + "TOHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "TO_ASSIGNEE_MASTER" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.tono ='"
						+ ht.get("TONO") + "' and a.custname=b.assignename";

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

	public Boolean removeOrder(String plant2, String tono) throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant2 + "_" + "TOHDR] WHERE TONO='"
					+ tono + "' AND PLANT='" + plant2 + "'";
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
					+ " from " + "[" + plant2 + "_" + "TOHDR" + "]");
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
	
	
	public ArrayList getTransferAssigneeDetailsByWMS(String plant,
			String orderno) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			StringBuffer sQry = new StringBuffer(
					"select a.tono,isnull(a.custname,'') as custname,");
			sQry
					.append(" isnull(name,'') contactname,isnull(b.telno,'') as telno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3 ");
			sQry.append(" from  " + "[" + plant + "_" + "tohdr" + "]" + " a,"
					+ "[" + plant + "_" + "to_assignee_master" + "]" + " b");
			sQry.append(" where a.plant='" + plant + "' and a.tono like '"
					+ orderno
					+ "%' and a.STATUS <> 'C'  and a.custname=b.assignename");

			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "TOHDR";
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
	
	//imthi
	public boolean updateReciptHeader(String query, Hashtable htCondition, String extCond)
    throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
		try {
			     con = com.track.gates.DbBean.getConnection();
			     StringBuffer sql = new StringBuffer(" UPDATE " + "["
			                     + htCondition.get("PLANT") + "_TRANSFER_RECIPT_HDR]");
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

	//navas
	public boolean updateTOReciptHeader(String query, Hashtable htCondition, String extCond)
	throws Exception {
	boolean flag = false;
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" UPDATE " + "["
				+ htCondition.get("PLANT") + "_TRANSFER_RECIPT_HDR_TO]");
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
	
	//imthi
	 public Map getReciptHeaderDetails(String plant,String orderType) throws Exception {

         MLogger.log(1, this.getClass() + " getTOReciptHeaderDetails()");
         Map m = new HashMap();
         java.sql.Connection con = null;
         String scondtn ="";
         try {
                 
                 con = DbBean.getConnection();
                 
                 StringBuffer sql = new StringBuffer("  SELECT  ");
                 sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(UENNO,'') AS UENNO,ISNULL(CUSTOMERUENNO,'') AS CUSTOMERUENNO,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(SONO,'') AS SONO,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE,ISNULL(ITEM,'') AS ITEM,ISNULL(PROJECT,'') AS PROJECT,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,");
                 sql.append("ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2,ISNULL(PRINTLOCSTOCK,'0') AS PRINTLOCSTOCK,ISNULL(PRINTBARCODE,'0') AS PRINTBARCODE,ISNULL(BRAND,'') BRAND,ISNULL(HSCODE,'') HSCODE,ISNULL(COO,'') COO,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EMPLOYEE,'') EMPLOYEE,ISNULL(SHIPTO,'') SHIPTO, ");
                 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(PRINTEMPLOYEE,'') PRINTEMPLOYEE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(PRINTWITHPROJECT,'0') AS PRINTWITHPROJECT,ISNULL(PRINTWITHUENNO,'0') AS PRINTWITHUENNO,ISNULL(PRINTWITHCUSTOMERUENNO,'0') AS PRINTWITHCUSTOMERUENNO,ISNULL(PRITNWITHHSCODE,'0') AS PRITNWITHHSCODE,");
                 sql.append("ISNULL(PRITNWITHCOO,'0') AS PRITNWITHCOO ,ISNULL(CAPTURESIGNATURE,'0') AS CAPTURESIGNATURE,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(UOM,'') AS UOM ,ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(PrintOrientation,'Landscape') AS PrintOrientation, ");
                 sql.append("ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER5,'') AS F5,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9,ISNULL(PRODUCTRATESARE,'') AS PRODUCTRATESARE,ISNULL(TERMSDETAILS,'') AS TERMSDETAILS,ISNULL(PRINTCUSTERMS,'0') AS PRINTCUSTERMS, ");
                 sql.append("ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM,ISNULL(RCBNO,'') AS RCBNO,ISNULL(PRINTWITHPRODUCTREMARKS,'0') AS PRINTWITHPRODUCTREMARKS,");
                 sql.append("ISNULL(PRINTWITHBRAND,'0') AS PRINTWITHBRAND,ISNULL(CUSTOMERRCBNO,'') AS CUSTOMERRCBNO,ISNULL(PREPAREDBY,'') AS PREPAREDBY,ISNULL(SELLER,'') AS SELLER,ISNULL(SELLERSIGNATURE,'') AS SELLERSIGNATURE,ISNULL(BUYER,'') AS BUYER,ISNULL(BUYERSIGNATURE,'') AS BUYERSIGNATURE, ");
                 sql.append("ISNULL(PRODUCTDELIVERYDATE,'') AS PRODUCTDELIVERYDATE,ISNULL(PRINTWITHPRODUCTDELIVERYDATE,'0') AS PRINTWITHPRODUCTDELIVERYDATE,ISNULL(PRINTWITHCOMPANYSIG,'0') AS PRINTWITHCOMPANYSIG,ISNULL(PRINTWITHCOMPANYSEAL,'0') AS PRINTWITHCOMPANYSEAL,ISNULL(PRINTWITHDELIVERYDATE,'0') AS PRINTWITHDELIVERYDATE ");
                 sql.append(" ");
                 sql.append(" FROM " + "[" + plant + "_" + "TRANSFER_RECIPT_HDR] where plant='"+plant+"'");
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
	 
	//navas
	 public Map getTOReciptHeaderDetails(String plant) throws Exception {

	 MLogger.log(1, this.getClass() + " getReciptHeaderDetails()");
	 Map m = new HashMap();
	 java.sql.Connection con = null;
	 String scondtn ="";
	 try {

	 con = DbBean.getConnection();

	 StringBuffer sql = new StringBuffer(" SELECT ");
	 sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
	 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(SONO,'') AS SONO,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE,ISNULL(ITEM,'') AS ITEM,ISNULL(PROJECT,'') AS PROJECT,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,");
	 sql.append("ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2,ISNULL(BRAND,'') BRAND,ISNULL(HSCODE,'') HSCODE,ISNULL(COO,'') COO,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EMPLOYEE,'') EMPLOYEE,ISNULL(SHIPTO,'') SHIPTO, ");
	 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(PRODUCTRATESARE,'') AS PRODUCTRATESARE,ISNULL(ADJUSTMENT,'') AS ADJUSTMENT,ISNULL(TOTAL,'') AS TOTAL,ISNULL(AMT,'') AS AMT, ISNULL(TAXAMOUNT,'') AS TAXAMOUNT,ISNULL(DISCOUNT,'') DISCOUNT,ISNULL(RATE,'') AS RATE ,ISNULL(TERMSDETAILS,'') AS TERMSDETAILS,ISNULL(TERMS,'') AS TERMS ,ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(PRINTEMPLOYEE,'') PRINTEMPLOYEE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(PRINTWITHPROJECT,'0') AS PRINTWITHPROJECT,ISNULL(PRITNWITHHSCODE,'0') AS PRITNWITHHSCODE,ISNULL(PRITNWITHCOO,'0') AS PRITNWITHCOO ,ISNULL(CAPTURESIGNATURE,'0') AS CAPTURESIGNATURE,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(UOM,'') AS UOM ,ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(PrintOrientation,'Landscape') AS PrintOrientation, ");
	 sql.append("ISNULL(FOOTER5,'') AS F5,ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9,ISNULL(PRINTWITHPRODUCT,'0') AS PRINTWITHPRODUCT,ISNULL(PRINTROUNDOFFTOTALWITHDECIMAL,'0') AS PRINTROUNDOFFTOTALWITHDECIMAL, ");
	 sql.append(" ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT, ISNULL(PRINTCUSTERMS,'0') AS PRINTCUSTERMS,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM,ISNULL(RCBNO,'') AS RCBNO,ISNULL(PRINTWITHPRODUCTREMARKS,'0') AS PRINTWITHPRODUCTREMARKS,ISNULL(PRINTWITHBRAND,'0') AS PRINTWITHBRAND,ISNULL(CUSTOMERRCBNO,'') AS CUSTOMERRCBNO,ISNULL(UENNO,'') AS UENNO,ISNULL(CUSTOMERUENNO,'') AS CUSTOMERUENNO,ISNULL(PREPAREDBY,'') AS PREPAREDBY,ISNULL(SELLER,'') AS SELLER,ISNULL(SELLERSIGNATURE,'') AS SELLERSIGNATURE,ISNULL(BUYER,'') AS BUYER,ISNULL(BUYERSIGNATURE,'') AS BUYERSIGNATURE, ");
	 sql.append(" ISNULL(PRODUCTDELIVERYDATE,'') AS PRODUCTDELIVERYDATE,ISNULL(PRINTWITHPRODUCTDELIVERYDATE,'0') AS PRINTWITHPRODUCTDELIVERYDATE,ISNULL(PRINTWITHCOMPANYSIG,'0') AS PRINTWITHCOMPANYSIG,ISNULL(PRINTWITHCOMPANYSEAL,'0') AS PRINTWITHCOMPANYSEAL,ISNULL(PRINTWITHUENNO,'0') AS PRINTWITHUENNO,ISNULL(PRINTWITHCUSTOMERUENNO,'0') AS PRINTWITHCUSTOMERUENNO,ISNULL(PRINTWITHDELIVERYDATE,'0') AS PRINTWITHDELIVERYDATE ");
	 sql.append(" ");
	 sql.append(" FROM " + "[" + plant + "_" + "TRANSFER_RECIPT_HDR_TO] where plant='"+plant+"'");

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
	 

	/* created by imthi&navas */
	 public boolean insertToHdr(ToHdr toHdr) throws Exception {
			boolean insertFlag = false;
			boolean flag = false;
			int HdrId = 0;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
			try {	    
				connection = DbBean.getConnection();
				query = "INSERT INTO ["+toHdr.getPLANT()+"_"+TABLE_HEADER+"]" + 
						"           ([PLANT]" + 
						"           ,[TONO]" + 
						"           ,[VENDNO]" + 
						"           ,[ORDDATE]" +
						"           ,[FROMWAREHOUSE]" + 
						"           ,[TOWAREHOUSE]" + 
						"           ,[DELDATE]" + 
						"           ,[STATUS]" + 
						"           ,[PickStaus]" + 
						"           ,[SHIPMOD]" + 
						"           ,[SHIPVIA]" + 
						"           ,[TERMS]" + 
						"           ,[TOTYPE]" + 
						"           ,[TOSTAT]" + 
						"           ,[TOTQTY]" +
						"           ,[TOTWGT]" +
						"           ,[COMMENT1]" + 
						"           ,[COMMENT2]" + 
						"           ,[LBLGROUP]" + 
						"           ,[LBLCAT]" + 
						"           ,[LBLTYPE]" + 
						"           ,[ORDTRK1]" + 
						"           ,[ORDTRK2]" + 
						"           ,[PRIORTY]" + 
						"           ,[CRAT]" + 
						"           ,[CRBY]" + 
						"           ,[UPAT]" + 
						"           ,[UPBY]" + 
						"           ,[RECSTAT]" + 
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
						"           ,[SCUST_NAME]" + 
						"           ,[SCNAME]" + 
						"           ,[SADDR1]" + 
						"           ,[SADDR2]" + 
						"           ,[SCITY]" + 
						"           ,[SCOUNTRY]" + 
						"           ,[SZIP]" + 
						"           ,[STELNO]" + 
						"           ,[USERFLG1]" + 
						"           ,[USERFLG2]" + 
						"           ,[USERFLG3]" + 
						"           ,[USERFLG4]" + 
						"           ,[USERFLG5]" + 
						"           ,[USERFLG6]" + 
						"           ,[USERTIME3]" + 
						"           ,[USERDBL1]" + 
						"           ,[USERDBL2]" + 
						"           ,[USERDBL3]" + 
						"           ,[USERDBL4]" + 
						"           ,[USERDBL5]" + 
						"           ,[USERDBL6]" + 
						"           ,[ORDERTYPE]" + 
						"           ,[SHIPPINGID]" + 
						"           ,[SHIPPINGCUSTOMER]" + 
						"           ,[CURRENCYID]" + 
						"           ,[DELIVERYDATE]" + 
						"           ,[CONSIGNMENT_GST]" + 
						"           ,[EMPNO]" + 
						"           ,[PAYMENT_STATUS]" + 
						"           ,[Remark3]" + 
						"           ,[ORDERDISCOUNT]" + 
						"           ,[SHIPPINGCOST]" + 
						"           ,[INCOTERMS]" + 
						"           ,[PAYMENTTYPE]" +
						"           ,[PRODUCTDELIVERYDATE]" + 
						"           ,[DELIVERYDATEFORMAT]" + 
						"           ,[APPROVESTATUS]" + 
						"           ,[SALES_LOCATION]" +
						"           ,[TAXTREATMENT]" +
						"           ,[ORDER_STATUS]" +
						"           ,[DISCOUNT]" +
						"           ,[DISCOUNT_TYPE]" +
						"           ,[ADJUSTMENT]" +
						"           ,[ITEM_RATES]" +
						"           ,[PROJECTID]" +
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
						"           ,[CURRENCYUSEQT]" +
						"           ,[ORDERDISCOUNTTYPE]" +
						"           ,[TAXID]" +
						"           ,[ISDISCOUNTTAX]" +
						"           ,[ISORDERDISCOUNTTAX]" +
						"           ,[ISSHIPPINGTAX]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, toHdr.getPLANT());
				   ps.setString(2, toHdr.getTONO());
				   ps.setString(3, toHdr.getVENDNO());
				   ps.setString(4, toHdr.getORDDATE());
				   ps.setString(5, toHdr.getFROMWAREHOUSE());
				   ps.setString(6, toHdr.getTOWAREHOUSE());
				   ps.setString(7, toHdr.getDELDATE());
				   ps.setString(8, toHdr.getSTATUS());
				   ps.setString(9, toHdr.getPickStaus());
				   ps.setString(10, toHdr.getSHIPMOD());
				   ps.setString(11, toHdr.getSHIPVIA());
				   ps.setString(12, toHdr.getTERMS());
				   ps.setString(13, toHdr.getTOTYPE());
				   ps.setString(14, toHdr.getTOSTAT());
				   ps.setString(15, toHdr.getTOTQTY());
				   ps.setString(16, toHdr.getTOTWGT());
				   ps.setString(17, toHdr.getCOMMENT1());
				   ps.setString(18, toHdr.getCOMMENT2());
				   ps.setString(19, toHdr.getLBLGROUP());
				   ps.setString(20, toHdr.getLBLCAT());
				   ps.setString(21, toHdr.getLBLTYPE());
				   ps.setString(22, toHdr.getORDTRK1());
				   ps.setString(23, toHdr.getORDTRK2());
				   ps.setString(24, toHdr.getPRIORTY());
				   ps.setString(25, toHdr.getCRAT());
				   ps.setString(26, toHdr.getCRBY());
				   ps.setString(27, toHdr.getUPAT());
				   ps.setString(28, toHdr.getUPBY());
				   ps.setString(29, toHdr.getRECSTAT());
				   ps.setString(30, toHdr.getCustCode());
				   ps.setString(31, toHdr.getCustName());
				   ps.setString(32, toHdr.getJobNum());
				   ps.setString(33, toHdr.getPersonInCharge());
				   ps.setString(34, toHdr.getContactNum());
				   ps.setString(35, toHdr.getAddress());
				   ps.setString(36, toHdr.getAddress2());
				   ps.setString(37, toHdr.getAddress3());
				   ps.setString(38, toHdr.getCollectionDate());
				   ps.setString(39, toHdr.getCollectionTime());
				   ps.setString(40, toHdr.getRemark1());
				   ps.setString(41, toHdr.getRemark2());
				   ps.setString(42, toHdr.getSCUST_NAME());
				   ps.setString(43, toHdr.getSCNAME());
				   ps.setString(44, toHdr.getSADDR1());
				   ps.setString(45, toHdr.getSADDR2());
				   ps.setString(46, toHdr.getSCITY());
				   ps.setString(47, toHdr.getSCOUNTRY());
				   ps.setString(48, toHdr.getSZIP());
				   ps.setString(49, toHdr.getSTELNO());
				   ps.setString(50, toHdr.getUSERFLG1());
				   ps.setString(51, toHdr.getUSERFLG2());
				   ps.setString(52, toHdr.getUSERFLG3());
				   ps.setString(53, toHdr.getUSERFLG4());
				   ps.setString(54, toHdr.getUSERFLG5());
				   ps.setString(55, toHdr.getUSERFLG6());
				   ps.setString(56, toHdr.getUSERTIME3());
				   ps.setString(57, toHdr.getUSERDBL1());
				   ps.setString(58, toHdr.getUSERDBL2());
				   ps.setString(59, toHdr.getUSERDBL3());
				   ps.setString(60, toHdr.getUSERDBL4());
				   ps.setString(61, toHdr.getUSERDBL5());
				   ps.setString(62, toHdr.getUSERDBL6());
				   ps.setString(63, toHdr.getORDERTYPE());
				   ps.setString(64, toHdr.getSHIPPINGID());
				   ps.setString(65, toHdr.getSHIPPINGCUSTOMER());
				   ps.setString(66, toHdr.getCURRENCYID());
				   ps.setString(67, toHdr.getDELIVERYDATE());
				   ps.setString(68, Double.toString(toHdr.getCONSIGNMENT_GST()));
				  
				   ps.setString(69, toHdr.getEMPNO());
				   ps.setString(70, toHdr.getPAYMENT_STATUS());
				   ps.setString(71, toHdr.getRemark3());
				   ps.setString(72, Double.toString(toHdr.getORDERDISCOUNT()));
				   ps.setString(73, Double.toString(toHdr.getSHIPPINGCOST()));
				   ps.setString(74, toHdr.getINCOTERMS());
				   ps.setString(75, toHdr.getPAYMENTTYPE());
				   ps.setString(76, toHdr.getPRODUCTDELIVERYDATE());
				   ps.setString(77, Short.toString(toHdr.getDELIVERYDATEFORMAT()));
				   ps.setString(78, toHdr.getAPPROVESTATUS());
				   ps.setString(79, toHdr.getSALES_LOCATION());
				   ps.setString(80, toHdr.getTAXTREATMENT());
				   ps.setString(81, toHdr.getORDER_STATUS());
				   ps.setString(82, Double.toString(toHdr.getDISCOUNT()));
				   ps.setString(83, toHdr.getDISCOUNT_TYPE());
				   ps.setString(84, Double.toString(toHdr.getADJUSTMENT()));
				   ps.setString(85, Short.toString(toHdr.getITEM_RATES()));
				   ps.setInt(86, toHdr.getPROJECTID());
				   ps.setString(87, toHdr.getSHIPCONTACTNAME());
					ps.setString(88, toHdr.getSHIPDESGINATION());
					ps.setString(89, toHdr.getSHIPWORKPHONE());
					ps.setString(90, toHdr.getSHIPHPNO());
					ps.setString(91, toHdr.getSHIPEMAIL());
					ps.setString(92, toHdr.getSHIPCOUNTRY());
					ps.setString(93, toHdr.getSHIPADDR1());
					ps.setString(94, toHdr.getSHIPADDR2());
					ps.setString(95, toHdr.getSHIPADDR3());
					ps.setString(96, toHdr.getSHIPADDR4());
					ps.setString(97, toHdr.getSHIPSTATE());
					ps.setString(98, toHdr.getSHIPZIP());
				   ps.setDouble(99, toHdr.getCURRENCYUSEQT());
				   ps.setString(100, toHdr.getORDERDISCOUNTTYPE());
				   ps.setInt(101, toHdr.getTAXID());
				   ps.setShort(102, toHdr.getISDISCOUNTTAX());
				   ps.setShort(103, toHdr.getISORDERDISCOUNTTAX());
				   ps.setShort(104, toHdr.getISSHIPPINGTAX());
				   
				  
		
				  
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   insertFlag = true;
				   }
				   else
				   {
					   throw new SQLException("Creating Consignment Order failed, no rows affected.");
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
	 public List<ToHdr> getToHdr(Hashtable ht, String afrmDate, String atoDate) throws Exception {
		   boolean flag = false;
		   int journalHdrId = 0;
		   Connection connection = null;
		   PreparedStatement ps = null;
		   String query = "", fields = "", dtCondStr = "";
		   List<ToHdr> doHeaders = new ArrayList<ToHdr>();
		   List<String> args = null;
		   try {
			   args = new ArrayList<String>();
			   connection = DbBean.getConnection();
			   query = "SELECT [PLANT]" + 
						"           ,[TONO]" + 
						"           ,[VENDNO]" + 
						"           ,[ORDDATE]" +
						"           ,[FROMWAREHOUSE]" + 
						"           ,[TOWAREHOUSE]" + 
						"           ,[DELDATE]" + 
						"           ,[STATUS]" + 
						"           ,[PickStaus]" + 
						"           ,[SHIPMOD]" + 
						"           ,[SHIPVIA]" + 
						"           ,[TERMS]" + 
						"           ,[TOTYPE]" + 
						"           ,[TOSTAT]" + 
						"           ,[TOTQTY]" +
						"           ,[TOTWGT]" +
						"           ,[COMMENT1]" + 
						"           ,[COMMENT2]" + 
						"           ,[LBLGROUP]" + 
						"           ,[LBLCAT]" + 
						"           ,[LBLTYPE]" + 
						"           ,[ORDTRK1]" + 
						"           ,[ORDTRK2]" + 
						"           ,[PRIORTY]" + 
						"           ,[CRAT]" + 
						"           ,[CRBY]" + 
						"           ,[UPAT]" + 
						"           ,[UPBY]" + 
						"           ,[RECSTAT]" + 
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
						"           ,[SCUST_NAME]" + 
						"           ,[SCNAME]" + 
						"           ,[SADDR1]" + 
						"           ,[SADDR2]" + 
						"           ,[SCITY]" + 
						"           ,[SCOUNTRY]" + 
						"           ,[SZIP]" + 
						"           ,[STELNO]" + 
						"           ,[USERFLG1]" + 
						"           ,[USERFLG2]" + 
						"           ,[USERFLG3]" + 
						"           ,[USERFLG4]" + 
						"           ,[USERFLG5]" + 
						"           ,[USERFLG6]" + 
						"           ,[USERTIME3]" + 
						"           ,[USERDBL1]" + 
						"           ,[USERDBL2]" + 
						"           ,[USERDBL3]" + 
						"           ,[USERDBL4]" + 
						"           ,[USERDBL5]" + 
						"           ,[USERDBL6]" + 
						"           ,[ORDERTYPE]" + 
						"           ,[SHIPPINGID]" + 
						"           ,[SHIPPINGCUSTOMER]" + 
						"           ,[CURRENCYID]" + 
						"           ,[DELIVERYDATE]" + 
						"           ,[CONSIGNMENT_GST]" + 
						"           ,[EMPNO]" + 
						"           ,[PAYMENT_STATUS]" + 
						"           ,[Remark3]" + 
						"           ,[ORDERDISCOUNT]" + 
						"           ,[SHIPPINGCOST]" + 
						"           ,[INCOTERMS]" + 
						"           ,[PAYMENTTYPE]" +
						"           ,[PRODUCTDELIVERYDATE]" + 
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
			   		"      ,ISNULL(ORDER_STATUS,'Open') ORDER_STATUS FROM ["+ ht.get("PLANT") +"_"+TABLE_HEADER+"] WHERE ";
			   Enumeration enum1 = ht.keys();
				for (int i = 0; i < ht.size(); i++) {
					String key = _StrUtils.fString((String) enum1.nextElement());
					String value = _StrUtils.fString((String) ht.get(key));
					fields += key + "=? AND ";
					args.add(value);
				}			
				query += fields.substring(0, fields.length() - 5);
				
				dtCondStr =" AND ISNULL(DELDATE,'')<>'' AND CAST((SUBSTRING(DELDATE, 7, 4) + '-' + SUBSTRING(DELDATE, 4, 2) + '-' + SUBSTRING(DELDATE, 1, 2)) AS date)";
				
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
				
				query += " ORDER BY CAST((SUBSTRING(DELDATE,7,4) + '-' + SUBSTRING(DELDATE,4,2) + '-' + SUBSTRING(DELDATE,1,2))AS DATE) DESC, TONO DESC";
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
						ToHdr tohdr = new ToHdr();
						ResultSetToObjectMap.loadResultSetIntoObject(rst, tohdr);
						doHeaders.add(tohdr);
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
	 
	 public ToHdr getToHdrById(String plant,String tono)throws Exception {
			boolean flag = false;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
		    ToHdr toHdr = new ToHdr();
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT [PLANT]" + 
						"           ,[TONO]" + 
						"           ,[VENDNO]" + 
						"           ,[ORDDATE]" +
						"           ,[FROMWAREHOUSE]" + 
						"           ,[TOWAREHOUSE]" + 
						"           ,[DELDATE]" + 
						"           ,[STATUS]" + 
						"           ,[PickStaus]" + 
						"           ,[SHIPMOD]" + 
						"           ,[SHIPVIA]" + 
						"           ,[TERMS]" + 
						"           ,[TOTYPE]" + 
						"           ,[TOSTAT]" + 
						"           ,[TOTQTY]" +
						"           ,[TOTWGT]" +
						"           ,[COMMENT1]" + 
						"           ,[COMMENT2]" + 
						"           ,[LBLGROUP]" + 
						"           ,[LBLCAT]" + 
						"           ,[LBLTYPE]" + 
						"           ,[ORDTRK1]" + 
						"           ,[ORDTRK2]" + 
						"           ,[PRIORTY]" + 
						"           ,[CRAT]" + 
						"           ,[CRBY]" + 
						"           ,[UPAT]" + 
						"           ,[UPBY]" + 
						"           ,[RECSTAT]" + 
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
						"           ,[SCUST_NAME]" + 
						"           ,[SCNAME]" + 
						"           ,[SADDR1]" + 
						"           ,[SADDR2]" + 
						"           ,[SCITY]" + 
						"           ,[SCOUNTRY]" + 
						"           ,[SZIP]" + 
						"           ,[STELNO]" + 
						"           ,[USERFLG1]" + 
						"           ,[USERFLG2]" + 
						"           ,[USERFLG3]" + 
						"           ,[USERFLG4]" + 
						"           ,[USERFLG5]" + 
						"           ,[USERFLG6]" + 
						"           ,[USERTIME3]" + 
						"           ,[USERDBL1]" + 
						"           ,[USERDBL2]" + 
						"           ,[USERDBL3]" + 
						"           ,[USERDBL4]" + 
						"           ,[USERDBL5]" + 
						"           ,[USERDBL6]" + 
						"           ,[ORDERTYPE]" + 
						"           ,[SHIPPINGID]" + 
						"           ,[SHIPPINGCUSTOMER]" + 
						"           ,[CURRENCYID]" + 
						"           ,[DELIVERYDATE]" + 
						"           ,[CONSIGNMENT_GST]" + 
						"           ,[EMPNO]" + 
						"           ,[PAYMENT_STATUS]" + 
						"           ,[Remark3]" + 
				   		"      ,CASE WHEN ISNULL(ORDERDISCOUNTTYPE,'0') = '%' THEN ORDERDISCOUNT ELSE (ORDERDISCOUNT * ISNULL(CURRENCYUSEQT,1))END AS ORDERDISCOUNT" +
				   		"      ,CASE WHEN ISNULL(DISCOUNT_TYPE,'0') = '%' THEN ISNULL([DISCOUNT], 0) ELSE (ISNULL([DISCOUNT], 0) * ISNULL(CURRENCYUSEQT,1))END AS DISCOUNT" +
				   		"      ,(ISNULL([ADJUSTMENT], 0) * ISNULL(CURRENCYUSEQT,1)) ADJUSTMENT" +
				   		"      ,(ISNULL([SHIPPINGCOST], 0) * ISNULL(CURRENCYUSEQT,1)) SHIPPINGCOST" +
				   		"      ,[INCOTERMS]" + 
				   		"      ,[PAYMENTTYPE]" + 
				   		"      ,[PRODUCTDELIVERYDATE]" + 
				   		"      ,ISNULL(DELIVERYDATEFORMAT,0) DELIVERYDATEFORMAT" + 
				   		"      ,[APPROVESTATUS]" + 
				   		"      ,[SALES_LOCATION]" + 
				   		"      ,[TAXTREATMENT]" + 
				   		"      ,[DISCOUNT]" +
						"      ,ISNULL([DISCOUNT_TYPE], '') DISCOUNT_TYPE" +
						"      ,ISNULL([ITEM_RATES], 0) ITEM_RATES" +
						"      ,ISNULL([CURRENCYUSEQT], 0) CURRENCYUSEQT" +
						"      ,ISNULL([ORDERDISCOUNTTYPE], '') ORDERDISCOUNTTYPE" +
						"      ,ISNULL([TAXID], 0) TAXID" +
						"      ,ISNULL([ISDISCOUNTTAX], 0) ISDISCOUNTTAX" +
						"      ,ISNULL([ISORDERDISCOUNTTAX], 0) ISORDERDISCOUNTTAX" +
						"      ,ISNULL([ISSHIPPINGTAX], 0) ISSHIPPINGTAX" +
						"      ,ISNULL([PROJECTID], '') PROJECTID" +
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
				   		"      ,ISNULL(ORDER_STATUS,'Open') ORDER_STATUS FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE "+
				   		" TONO=? AND PLANT=?";

				if(connection != null){
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   ps.setString(1, tono);
					   ps.setString(2, plant);
					   ResultSet rst = ps.executeQuery();
					   while (rst.next()) {
		                    ResultSetToObjectMap.loadResultSetIntoObject(rst, toHdr);
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
			return toHdr;
		}

	 
	   public boolean updateToHdr(ToHdr toHdr) throws Exception {
			boolean updateFlag = false;
			boolean flag = false;
			int HdrId = 0;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
			try {	    
				connection = DbBean.getConnection();
				query = "UPDATE ["+toHdr.getPLANT()+"_"+TABLE_HEADER+"] SET" + 
						"            [PLANT] = ?" + 
						"           ,[VENDNO] = ?" + 
						"           ,[ORDDATE] = ?" +
						"           ,[FROMWAREHOUSE] = ?" + 
						"           ,[TOWAREHOUSE] = ?" + 
						"           ,[DELDATE] = ?" + 
						"           ,[STATUS] = ?" + 
						"           ,[PickStaus] = ?" + 
						"           ,[SHIPMOD] = ?" + 
						"           ,[SHIPVIA] = ?" + 
						"           ,[TERMS] = ?" + 
						"           ,[TOTYPE] = ?" + 
						"           ,[TOSTAT] = ?" + 
						"           ,[TOTQTY] = ?" +
						"           ,[TOTWGT] = ?" +
						"           ,[COMMENT1] = ?" + 
						"           ,[COMMENT2] = ?" + 
						"           ,[LBLGROUP] = ?" + 
						"           ,[LBLCAT] = ?" + 
						"           ,[LBLTYPE] = ?" + 
						"           ,[ORDTRK1] = ?" + 
						"           ,[ORDTRK2] = ?" + 
						"           ,[PRIORTY] = ?" + 
						"           ,[CRAT] = ?" + 
						"           ,[CRBY] = ?" + 
						"           ,[UPAT] = ?" + 
						"           ,[UPBY] = ?" + 
						"           ,[RECSTAT] = ?" + 
						"           ,[CustCode] = ?" + 
						"           ,[CustName] = ?" + 
						"           ,[JobNum] = ?" + 
						"           ,[PersonInCharge] = ?" + 
						"           ,[contactNum] = ?" + 
						"           ,[Address] = ?" + 
						"           ,[Address2] = ?" + 
						"           ,[Address3] = ?" + 
						"           ,[CollectionDate] = ?" + 
						"           ,[CollectionTime] = ?" + 
						"           ,[Remark1] = ?" + 
						"           ,[Remark2] = ?" + 
						"           ,[SCUST_NAME] = ?" + 
						"           ,[SCNAME] = ?" + 
						"           ,[SADDR1] = ?" + 
						"           ,[SADDR2] = ?" + 
						"           ,[SCITY] = ?" + 
						"           ,[SCOUNTRY] = ?" + 
						"           ,[SZIP] = ?" + 
						"           ,[STELNO] = ?" + 
						"           ,[USERFLG1] = ?" + 
						"           ,[USERFLG2] = ?" + 
						"           ,[USERFLG3] = ?" + 
						"           ,[USERFLG4] = ?" + 
						"           ,[USERFLG5] = ?" + 
						"           ,[USERFLG6] = ?" + 
						"           ,[USERTIME3] = ?" + 
						"           ,[USERDBL1] = ?" + 
						"           ,[USERDBL2] = ?" + 
						"           ,[USERDBL3] = ?" + 
						"           ,[USERDBL4] = ?" + 
						"           ,[USERDBL5] = ?" + 
						"           ,[USERDBL6] = ?" + 
						"           ,[ORDERTYPE] = ?" + 
						"           ,[SHIPPINGID] = ?" + 
						"           ,[SHIPPINGCUSTOMER] = ?" + 
						"           ,[CURRENCYID] = ?" + 
						"           ,[DELIVERYDATE] = ?" + 
						"           ,[CONSIGNMENT_GST] = ?" + 
						"           ,[EMPNO] = ?" + 
						"           ,[PAYMENT_STATUS] = ?" + 
						"           ,[Remark3] = ?" + 
						"           ,[ORDERDISCOUNT] = ?" + 
						"           ,[SHIPPINGCOST] = ?" + 
						"           ,[INCOTERMS] = ?" + 
						"           ,[PAYMENTTYPE] = ?" +
						"           ,[PRODUCTDELIVERYDATE] = ?" + 
						"           ,[DELIVERYDATEFORMAT] = ?" + 
						"           ,[APPROVESTATUS] = ?" + 
						"           ,[SALES_LOCATION] = ?" +
						"           ,[TAXTREATMENT] = ?" +
						"           ,[ORDER_STATUS] = ?" +
						"           ,[DISCOUNT] = ?" +
						"           ,[DISCOUNT_TYPE] = ?" +
						"           ,[ADJUSTMENT] = ?" +
						"           ,[ITEM_RATES] = ?" +
						"           ,[PROJECTID] = ?" +
						"			,[SHIPCONTACTNAME] = ?" +
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
						"           ,[CURRENCYUSEQT] = ?" +
						"           ,[ORDERDISCOUNTTYPE] = ?" +
						"           ,[TAXID] = ?" +
						"           ,[ISDISCOUNTTAX] = ?" +
						"           ,[ISORDERDISCOUNTTAX] = ?" +
						"           ,[ISSHIPPINGTAX] = ? WHERE [TONO] = ?";

				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, toHdr.getPLANT());
				   ps.setString(2, toHdr.getVENDNO());
				   ps.setString(3, toHdr.getORDDATE());
				   ps.setString(4, toHdr.getFROMWAREHOUSE());
				   ps.setString(5, toHdr.getTOWAREHOUSE());
				   ps.setString(6, toHdr.getDELDATE());
				   ps.setString(7, toHdr.getSTATUS());
				   ps.setString(8, toHdr.getPickStaus());
				   ps.setString(9, toHdr.getSHIPMOD());
				   ps.setString(10, toHdr.getSHIPVIA());
				   ps.setString(11, toHdr.getTERMS());
				   ps.setString(12, toHdr.getTOTYPE());
				   ps.setString(13, toHdr.getTOSTAT());
				   ps.setString(14, toHdr.getTOTQTY());
				   ps.setString(15, toHdr.getTOTWGT());
				   ps.setString(16, toHdr.getCOMMENT1());
				   ps.setString(17, toHdr.getCOMMENT2());
				   ps.setString(18, toHdr.getLBLGROUP());
				   ps.setString(19, toHdr.getLBLCAT());
				   ps.setString(20, toHdr.getLBLTYPE());
				   ps.setString(21, toHdr.getORDTRK1());
				   ps.setString(22, toHdr.getORDTRK2());
				   ps.setString(23, toHdr.getPRIORTY());
				   ps.setString(24, toHdr.getCRAT());
				   ps.setString(25, toHdr.getCRBY());
				   ps.setString(26, toHdr.getUPAT());
				   ps.setString(27, toHdr.getUPBY());
				   ps.setString(28, toHdr.getRECSTAT());
				   ps.setString(29, toHdr.getCustCode());
				   ps.setString(30, toHdr.getCustName());
				   ps.setString(31, toHdr.getJobNum());
				   ps.setString(32, toHdr.getPersonInCharge());
				   ps.setString(33, toHdr.getContactNum());
				   ps.setString(34, toHdr.getAddress());
				   ps.setString(35, toHdr.getAddress2());
				   ps.setString(36, toHdr.getAddress3());
				   ps.setString(37, toHdr.getCollectionDate());
				   ps.setString(38, toHdr.getCollectionTime());
				   ps.setString(39, toHdr.getRemark1());
				   ps.setString(40, toHdr.getRemark2());
				   ps.setString(41, toHdr.getSCUST_NAME());
				   ps.setString(42, toHdr.getSCNAME());
				   ps.setString(43, toHdr.getSADDR1());
				   ps.setString(44, toHdr.getSADDR2());
				   ps.setString(45, toHdr.getSCITY());
				   ps.setString(46, toHdr.getSCOUNTRY());
				   ps.setString(47, toHdr.getSZIP());
				   ps.setString(48, toHdr.getSTELNO());
				   ps.setString(49, toHdr.getUSERFLG1());
				   ps.setString(50, toHdr.getUSERFLG2());
				   ps.setString(51, toHdr.getUSERFLG3());
				   ps.setString(52, toHdr.getUSERFLG4());
				   ps.setString(53, toHdr.getUSERFLG5());
				   ps.setString(54, toHdr.getUSERFLG6());
				   ps.setString(55, toHdr.getUSERTIME3());
				   ps.setString(56, toHdr.getUSERDBL1());
				   ps.setString(57, toHdr.getUSERDBL2());
				   ps.setString(58, toHdr.getUSERDBL3());
				   ps.setString(59, toHdr.getUSERDBL4());
				   ps.setString(60, toHdr.getUSERDBL5());
				   ps.setString(61, toHdr.getUSERDBL6());
				   ps.setString(62, toHdr.getORDERTYPE());
				   ps.setString(63, toHdr.getSHIPPINGID());
				   ps.setString(64, toHdr.getSHIPPINGCUSTOMER());
				   ps.setString(65, toHdr.getCURRENCYID());
				   ps.setString(66, toHdr.getDELIVERYDATE());
				   ps.setString(67, Double.toString(toHdr.getCONSIGNMENT_GST()));
				   ps.setString(68, toHdr.getEMPNO());
				   ps.setString(69, toHdr.getPAYMENT_STATUS());
				   ps.setString(70, toHdr.getRemark3());
				   ps.setString(71, Double.toString(toHdr.getORDERDISCOUNT()));
				   ps.setString(72, Double.toString(toHdr.getSHIPPINGCOST()));
				   ps.setString(73, toHdr.getINCOTERMS());
				   ps.setString(74, toHdr.getPAYMENTTYPE());
				   ps.setString(75, toHdr.getPRODUCTDELIVERYDATE());
				   ps.setString(76, Short.toString(toHdr.getDELIVERYDATEFORMAT()));
				   ps.setString(77, toHdr.getAPPROVESTATUS());
				   ps.setString(78, toHdr.getSALES_LOCATION());
				   ps.setString(79, toHdr.getTAXTREATMENT());
				   ps.setString(80, toHdr.getORDER_STATUS());
				   ps.setString(81, Double.toString(toHdr.getDISCOUNT()));
				   ps.setString(82, toHdr.getDISCOUNT_TYPE());
				   ps.setString(83, Double.toString(toHdr.getADJUSTMENT()));
				   ps.setString(84, Short.toString(toHdr.getITEM_RATES()));
				   ps.setInt(85, toHdr.getPROJECTID());
				   ps.setString(86, toHdr.getSHIPCONTACTNAME());
					ps.setString(87, toHdr.getSHIPDESGINATION());
					ps.setString(88, toHdr.getSHIPWORKPHONE());
					ps.setString(89, toHdr.getSHIPHPNO());
					ps.setString(90, toHdr.getSHIPEMAIL());
					ps.setString(91, toHdr.getSHIPCOUNTRY());
					ps.setString(92, toHdr.getSHIPADDR1());
					ps.setString(93, toHdr.getSHIPADDR2());
					ps.setString(94, toHdr.getSHIPADDR3());
					ps.setString(95, toHdr.getSHIPADDR4());
					ps.setString(96, toHdr.getSHIPSTATE());
					ps.setString(97, toHdr.getSHIPZIP());
				   ps.setDouble(98, toHdr.getCURRENCYUSEQT());
				   ps.setString(99, toHdr.getORDERDISCOUNTTYPE());
				   ps.setInt(100, toHdr.getTAXID());
				   ps.setShort(101, toHdr.getISDISCOUNTTAX());
				   ps.setShort(102, toHdr.getISORDERDISCOUNTTAX());
				   ps.setShort(103, toHdr.getISSHIPPINGTAX());
				   ps.setString(104, toHdr.getTONO());
				   
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   updateFlag = true;
				   }
				   else
				   {
					   throw new SQLException("Creating Consignment Order failed, no rows affected.");
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
	   
	   public Map getTOReciptInvoiceHeaderDetails(String plant,String orderType) throws Exception {
	         MLogger.log(1, this.getClass() + " getPOSReciptHeaderDetails()");
	         Map m = new HashMap();
	         java.sql.Connection con = null;
//	         String scondtn ="";
	         try {                 
	                 con = DbBean.getConnection();
	                 StringBuffer sql = new StringBuffer("  SELECT  ");
	                 sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
	                 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(TERMS,'') AS TERMS ,ISNULL(TERMSDETAILS,'') AS TERMSDETAILS,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM,ISNULL(PROJECT,'') AS PROJECT,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,ISNULL(PRINTCUSTERMS,'0') AS PRINTCUSTERMS,ISNULL(PCUSREMARKS,'0') AS PCUSREMARKS,");
	                 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(RATE,'') AS RATE ,ISNULL(TAXAMOUNT,'') AS TAXAMOUNT,ISNULL(AMT,'') AS AMT, ");
	                 sql.append("ISNULL(SUBTOTAL,'') AS SUBTOTAL,ISNULL(TOTALTAX,'') AS TOTALTAX,'Total With Tax' AS TOTAL,ISNULL(UOM,'') AS UOM, ");
	                 sql.append("ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2 ,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(FOOTER5,'') AS F5, ");
	                 sql.append("ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9,ISNULL(PRODUCTRATESARE,'') AS PRODUCTRATESARE, ");
	                 sql.append("ISNULL(CONTAINER,'') Container,ISNULL(DisplayContainer,'0') AS DisplayContainer,ISNULL(ADJUSTMENT,'') AS ADJUSTMENT, ");
	                 sql.append("ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2,ISNULL(BRAND,'') BRAND,ISNULL(HSCODE,'') HSCODE,ISNULL(COO,'') COO,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EMPLOYEE,'') EMPLOYEE,ISNULL(SHIPTO,'') SHIPTO, ");
	                 sql.append("ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(PRINTEMPLOYEE,'') PRINTEMPLOYEE,ISNULL(PrintOrientation,'Landscape') PrintOrientation, ");
	                 sql.append(" ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM,ISNULL(PRITNWITHHSCODE,'0') AS PRITNWITHHSCODE,ISNULL(PRINTWITHPROJECT,'0') AS PRINTWITHPROJECT,ISNULL(PRITNWITHCOO,'0') AS PRITNWITHCOO,ISNULL(RCBNO,'') AS RCBNO,ISNULL( PRINTWITHPRODUCTREMARKS,'0') AS PRINTWITHPRODUCTREMARKS,ISNULL( PRINTWITHBRAND,'0') AS PRINTWITHBRAND,ISNULL( CUSTOMERRCBNO,'') AS CUSTOMERRCBNO,ISNULL( TOTALAFTERDISCOUNT,'') AS TOTALAFTERDISCOUNT, ");
	                 sql.append(" ISNULL(PRINTPACKINGLIST,'0') AS PRINTPACKINGLIST,ISNULL(PRINTDELIVERYNOTE,'0') AS PRINTDELIVERYNOTE,ISNULL(PREPAREDBY,'') AS PREPAREDBY,ISNULL(SELLER,'') AS SELLER,ISNULL(SELLERSIGNATURE,'') AS SELLERSIGNATURE,ISNULL(BUYER,'') AS BUYER,ISNULL(BUYERSIGNATURE,'') AS BUYERSIGNATURE,ISNULL(ROUNDOFFTOTALWITHDECIMAL,'') AS ROUNDOFFTOTALWITHDECIMAL,ISNULL(PRINTROUNDOFFTOTALWITHDECIMAL,'0') AS PRINTROUNDOFFTOTALWITHDECIMAL,ISNULL(PRINTWITHPRODUCT,'0') AS PRINTWITHPRODUCT,ISNULL(PRINTWITHDISCOUNT,'0') AS PRINTWITHDISCOUNT,ISNULL(DISCOUNT,'') DISCOUNT,ISNULL(NETRATE,'') NETRATE, ");
	                 sql.append(" ISNULL(PRODUCTDELIVERYDATE,'') AS PRODUCTDELIVERYDATE,ISNULL(PRINTWITHPRODUCTDELIVERYDATE,'0') AS PRINTWITHPRODUCTDELIVERYDATE,ISNULL(PRINTWITHDELIVERYDATE,'0') AS PRINTWITHDELIVERYDATE,ISNULL(SHOWPREVIOUSPURCHASECOST,'0') AS SHOWPREVIOUSPURCHASECOST,ISNULL(SHOWPREVIOUSSALESCOST,'0') AS SHOWPREVIOUSSALESCOST,ISNULL(CALCULATETAXWITHSHIPPINGCOST,'0') AS CALCULATETAXWITHSHIPPINGCOST ");
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
	   
	   public String getOBDiscountSelectedItemByCustomer(String aPlant, String custcode,String aItem,String aType) throws Exception {
	       java.sql.Connection con = null;
	       String OBDiscount= "";
	       String CustCode="";
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
	   
	   public String getUnitCostBasedOnCurIDSelectedForOrder(String aPlant, String toNO,String aItem) throws Exception {
           java.sql.Connection con = null;
           String UnitCostForSelCurrency = "";
           try {
                   
                   con = DbBean.getConnection();
          

           StringBuffer SqlQuery = new StringBuffer(" select CAST(isnull(UnitPrice,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_TOHDR WHERE TONO ='"+toNO+"')) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY  + ")) AS UNITCOST ");
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
	   
	   public String getUnitCostBasedOnCurIDSelectedForOrderWTC(String aPlant, String toNO,String aItem) throws Exception {
	       java.sql.Connection con = null;
	       String UnitCostForSelCurrency = "";
	       try {
	               
	               con = DbBean.getConnection();
	      

	       StringBuffer SqlQuery = new StringBuffer(" select isnull(UnitPrice,0) *isnull((SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_TOHDR WHERE TONO ='"+toNO+"')),0) AS UNITCOST ");
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
	   
	// START consignment jsp page
	   public int Salescount(String plant, String afrmDate, String atoDate)
				throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			int Salescount = 0;
			String sCondition = "";
			String dtCondStr =    " AND (SUBSTRING(DELDATE, 7, 4) + '-' + SUBSTRING(DELDATE, 4, 2) + '-' + SUBSTRING(DELDATE, 1, 2))";
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
						+ "TOHDR" + "]" + " WHERE " + IConstants.PLANT
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

	 // ENDS create jsp
	   
	   
	   public String getOrderTypeForTO(String aPlant, String aTono) throws Exception {
           String OrderType = "";
           Hashtable<String, String> ht = new Hashtable<>();
           ht.put("PLANT", aPlant);
           ht.put("TONO", aTono);
           String query = " case  ordertype when '' then 'CONSIGNMENT ORDER' else upper(isnull(ordertype,'')) end  AS  ORDERTYPE ";
           Map m = selectRow(query, ht);
           OrderType = (String) m.get("ORDERTYPE");
           return OrderType;
   }
}
