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

import javax.naming.NamingException;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.object.CostofgoodsLanded;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CoaDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.CoaDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.CoaDAO_PRINTPLANTMASTERLOG;

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

	public boolean addAccount(Hashtable<String, String> ht, String plant, String TABLE_NAME) throws Exception {
		boolean accountCreated = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();
			connection = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration<String> enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				args.add(value);
				FIELDS += key + ",";
				VALUES += "?,";
			}
			query = "INSERT INTO [" + plant + "_"+TABLE_NAME+"] (" + FIELDS.substring(0, FIELDS.length() - 1)
					+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1) + ")";

			if (connection != null) {
				/* Create PreparedStatement object */
				ps = connection.prepareStatement(query);

				this.mLogger.query(this.printQuery, query);
				accountCreated = execute_NonSelectQuery(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return accountCreated;
	}
	public int addExtendedType(Hashtable<String, String> ht, String plant, String TABLE_NAME) throws Exception {
		boolean accountCreated = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		int createdId = 0;
		int count = 0;
		String query = "";
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();
			connection = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration<String> enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				args.add(value);
				FIELDS += key + ",";
				VALUES += "'"+value+"',";
			}
			query = "INSERT INTO [" + plant + "_"+TABLE_NAME+"] (" + FIELDS.substring(0, FIELDS.length() - 1)
					+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1) + ")";

			if (connection != null) {
				/* Create PreparedStatement object */
				ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

				this.mLogger.query(this.printQuery, query);
				count = ps.executeUpdate();
				ResultSet rs = ps.getGeneratedKeys();
				   if (rs.next()){
					   createdId = rs.getInt(1);
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
		return createdId;
	}
	public List<Map<String, String>> selectGroup(String plant, String group) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> groupList = new ArrayList<>();
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "SELECT GROUPS,TYPE, b.id as GROUPID  FROM " + "[" + plant + "_" + "finacctype] a join [" + plant + "_"
					+ "FINACCTYPEGROUP] b on (a.accgroupid = b.id) WHERE type LIKE ? AND  a.plant =? ";
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add(group + "%");
			args.add(plant);

			this.mLogger.query(this.printQuery, query);

			groupList = selectData(ps, args);
			System.out.println(groupList);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return groupList;
	}

	public List<Map<String, String>> selectGroupData(String plant, String group) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> groupList = new ArrayList<>();
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "SELECT GROUPS, b.id as GROUPID  FROM " + "[" + plant + "_" + "FINACCTYPEGROUP] b WHERE GROUPS LIKE ? AND  b.plant =? ";
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add(group + "%");
			args.add(plant);

			this.mLogger.query(this.printQuery, query);

			groupList = selectData(ps, args);
			System.out.println(groupList);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return groupList;
	}
	public List<Map<String, String>> selectAccountType(String plant) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> groupList = new ArrayList<>();
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "select a.PLANT,a.ID as id,ACCOUNTTYPE as text,b.MAINACCOUNT,b.ID as MAINACCID,ISNULL(a.ACCOUNT_CODE,'0') AS ACCOUNT_CODE,ISNULL(a.CODE,'') AS CODE from ["+plant+"_FINACCOUNTTYPE] a LEFT JOIN ["+plant+"_FINMAINACCOUNT] b ON b.ID=a.MAINACCOUNTID where a.plant=?";
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add(plant);

			this.mLogger.query(this.printQuery, query);

			groupList = selectData(ps, args);
			System.out.println(groupList);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return groupList;
	}
	
	public List<Map<String, String>> selectAccountDetailType(String plant, String group, String type) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> groupList = new ArrayList<>();
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "SELECT ACCOUNTDETAILTYPE, ID as ACCOUNTDETAILTYPE_ID, ISNULL(ACCOUNT_CODE,'0') AS ACCOUNT_CODE,ISNULL(CODE,'') AS CODE FROM " + "[" + plant + "_" + "FINACCOUNTDETAILTYPE] where ACCOUNTTYPEID=? AND plant =? AND ACCOUNTDETAILTYPE LIKE '"+group+"%'";
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add(type);
			args.add(plant);

			this.mLogger.query(this.printQuery, query);

			groupList = selectData(ps, args);
			System.out.println(groupList);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return groupList;
	}
	
	public List<Map<String, String>> selectSubAccountType(String plant, String group,String type) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> groupList = new ArrayList<>();
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "SELECT case when ISSUBACCOUNT=1 then 'Sub Account of ' + SUBACCOUNTNAME else ACCOUNTTYPE end ACCOUNTTYPE,ACCOUNT_NAME AS ACCOUNTNAME, b.ID,case when ISSUBACCOUNT=1 then ' '+ACCOUNT_NAME else ACCOUNT_NAME end ACCOUNT FROM " + "[" + plant + "_" + "FINCHARTOFACCOUNTS] b WHERE ACCOUNT_NAME LIKE ? AND  b.plant =? AND ACCOUNTTYPE =? ORDER BY ACCOUNT_NAME,ISSUBACCOUNT ";
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add(group + "%");
			args.add(plant);
            args.add(type);
			this.mLogger.query(this.printQuery, query);

			groupList = selectData(ps, args);
			System.out.println(groupList);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return groupList;
	}

	public List<Map<String, String>> selectSubAccountTypeList(String plant,String typequery) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> groupList = new ArrayList<>();
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "select a.ID as id,a.ACCOUNT_NAME as text,ISNULL(a.ACCOUNT_CODE,'') AS ACCOUNT_CODE,ISNULL(a.ISEXPENSEGST,0) AS ISEXPENSEGST,ISNULL(a.CODE,'0') AS CODE,ISNULL(d.ACCOUNT_CODE,'') AS ACCOUNTDET_CODE,d.ACCOUNTDETAILTYPE,a.ACCOUNTDETAILTYPE as DETAILID,b.ACCOUNTTYPE,a.ACCOUNTTYPE as ACCOUNTTYPEID,a.ISSUBACCOUNT,ISNULL(a.SUBACCOUNTNAME,'') SUBACCOUNTNAME,c.ID as MAINACCID FROM " + "[" + plant + "_" + "FINCHARTOFACCOUNTS] a left join " + "[" + plant + "_" +"FINACCOUNTTYPE] b on a.ACCOUNTTYPE=b.ID left join " + "[" + plant + "_" +"FINMAINACCOUNT] c on b.MAINACCOUNTID=c.ID join " + "[" + plant +"_"+"FINACCOUNTDETAILTYPE] d on d.ID=a.ACCOUNTDETAILTYPE where a.plant =? AND (a.ACCOUNT_NAME LIKE '%"+typequery+"%' OR a.ACCOUNT_CODE LIKE '%"+typequery+"%') ORDER BY a.ID";
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add(plant);

			this.mLogger.query(this.printQuery, query);

			groupList = selectData(ps, args);
			//System.out.println(groupList);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return groupList;
	}
	public List<Map<String, String>> selectSubAccountTypeByFilter(String whereQuery,String plant,String extCond) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> groupList = new ArrayList<>();
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "select a.ID as id,a.ACCOUNT_NAME as text,b.ACCOUNTTYPE,a.ISSUBACCOUNT,a.SUBACCOUNTNAME FROM " + "[" + plant + "_" + "FINCHARTOFACCOUNTS] a left join " + "[" + plant + "_" +"FINACCOUNTTYPE] b on a.ACCOUNTTYPE=b.ID where a.plant ='"+plant+"'";
			StringBuffer sql = new StringBuffer(query);
			String conditon = "";
			if(!whereQuery.isEmpty())
			{
				conditon = "AND " +whereQuery;
				sql.append(" " + conditon);
			}

			if (extCond.length() > 0) {
				sql.append("  ");

				sql.append(" " + extCond);
			}
			this.mLogger.query(this.printQuery, "Mysdsdsd"+sql.toString());

			groupList = selectData(con, sql.toString());
			System.out.println(groupList);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return groupList;
	}
	public JSONObject readTable(String plant) {
		JSONObject mainObj = new JSONObject();
		JSONArray dataArr = new JSONArray();
		String query = "select a.ID as accid,a.ACCOUNT_NAME as ACCOUNT_NAME,ISNULL(a.ACCOUNT_CODE,'') AS ACCOUNT_CODE,b.ACCOUNTTYPE as TYPE,c.ACCOUNTDETAILTYPE as ACCOUNTDETAILTYPE,a.OPENINGBALANCE as OPENINGBALANCE,a.ISSUBACCOUNT as ISSUBACCOUNT  from " + plant + "_FINCHARTOFACCOUNTS a left join " + "[" + plant + "_" +"FINACCOUNTTYPE] b on a.ACCOUNTTYPE=b.ID join " + "[" + plant + "_" +"FINACCOUNTDETAILTYPE] c on c.ID= a.ACCOUNTDETAILTYPE where a.plant ='"+plant+"' 	ORDER BY a.ACCOUNT_CODE ASC" ;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			while (rset.next()) {
				JSONObject dataObj = new JSONObject();

				dataObj.put("id", rset.getString("accid"));
				dataObj.put("account_name", rset.getString("ACCOUNT_NAME"));				
				dataObj.put("account_type", rset.getString("TYPE"));
				dataObj.put("account_det_type", rset.getString("ACCOUNTDETAILTYPE"));
				dataObj.put("account_balance", rset.getString("OPENINGBALANCE"));
				dataObj.put("issubaccount", rset.getString("ISSUBACCOUNT"));
				dataObj.put("account_code", rset.getString("ACCOUNT_CODE"));
				dataArr.add(dataObj);
			}

			mainObj.put("data", dataArr);
			mainObj.put("orders", dataArr);
		} catch (NamingException | SQLException e) {
			JSONObject dataObj = new JSONObject();
			dataObj.put("responseText", "failed");
			mainObj.put("data", dataObj);
			mainObj.put("orders", dataObj);
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return mainObj;
	}

	public boolean updateTable(String plant, String accountId, String accountName, String account_type,
			String account_det_type, String description, String account_is_sub, String account_subAcct,
			String account_balance, String account_balanceDate,String landedcost,String account_is_exp_gst) {
		//JSONObject mainObj = new JSONObject();
		boolean accountupdated = false;
		String query = "update " + plant + "_FINCHARTOFACCOUNTS set account_name = '" + accountName
				+ "', ACCOUNTDETAILTYPE = '" + account_det_type + "', ISSUBACCOUNT = '" + account_is_sub
				+ "', SUBACCOUNTNAME = '" + account_subAcct + "', OPENINGBALANCE = '" + account_balance + "', OPENINGBALANCEDATE = '" + account_balanceDate
				+ "', ACCOUNTTYPE = '" + account_type + "', description = '" + description + "',LANDEDCOSTCAL='"+landedcost+"',ISEXPENSEGST='"+account_is_exp_gst+"' where id = '"
				+ accountId + "'";
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			int recordsChanged = stmt.executeUpdate(query);
			//JSONObject dataObj = new JSONObject();
			if (recordsChanged >0) {
				accountupdated=true;
				/*dataObj.put("STATUS", "SUCCESS");
				dataObj.put("MESSAGE", "update success");
				mainObj.put("data", dataObj);*/
			}
			/*else {
				dataObj.put("STATUS", "FAIL");
				dataObj.put("MESSAGE", "record not found");
				mainObj.put("data", dataObj);
			}*/
			//mainObj.put("data", dataObj);
		} catch (NamingException | SQLException e) {
			/*JSONObject dataObj = new JSONObject();
			dataObj.put("STATUS", "FAIL");
			dataObj.put("MESSAGE", "update failed");
			mainObj.put("data", dataObj);*/
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return accountupdated;
	}

	public JSONObject readTableRecord(String plant, String id) {
		JSONObject mainObj = new JSONObject();
		JournalDAO journaldao = new JournalDAO();
		String query = "select a.ID,a.ACCOUNT_NAME,ISNULL(a.ACCOUNT_CODE,'') AS ACCOUNT_CODE,ISNULL(b.ACCOUNT_CODE,'') AS ACCOUNTDET_CODE,a.ACCOUNTDETAILTYPE as DETAILID,a.ACCOUNTTYPE,a.DESCRIPTION,a.ISSUBACCOUNT,ISNULL(a.ISEXPENSEGST,0) ISEXPENSEGST,a.SUBACCOUNTNAME,a.OPENINGBALANCE,a.OPENINGBALANCEDATE,b.ACCOUNTDETAILTYPE,ISNULL(a.LANDEDCOSTCAL,0) as LANDEDCOSTCAL,c.MAINACCOUNTID as MAINACCID from " + plant + "_FINCHARTOFACCOUNTS a join " + plant +"_FINACCOUNTDETAILTYPE b on b.ID=a.ACCOUNTDETAILTYPE join " + plant + "_FINACCOUNTTYPE c on c.ID=a.ACCOUNTTYPE where a.ID ='" + id + "'";
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			while (rset.next()) {
				JSONObject dataObj = new JSONObject();
				Double opbalnce = 0.0;
				try {
					opbalnce = journaldao.getcurretbalByChartOfAccountId(plant, rset.getString("ID"));
				} catch (Exception e) {
					opbalnce =0.0;
				}
				 
				dataObj.put("account_id", rset.getString("ID"));
				dataObj.put("account_balance", opbalnce);
				dataObj.put("account_name", rset.getString("ACCOUNT_NAME"));
				dataObj.put("account_det_id", rset.getString("DETAILID"));
				dataObj.put("account_det_type", rset.getString("ACCOUNTDET_CODE")+"  "+rset.getString("ACCOUNTDETAILTYPE"));
				dataObj.put("account_type", rset.getString("ACCOUNTTYPE"));
				dataObj.put("account_desc", rset.getString("DESCRIPTION"));
				dataObj.put("issub_account", rset.getString("ISSUBACCOUNT"));
				dataObj.put("isexp_gst", rset.getString("ISEXPENSEGST"));
				dataObj.put("sub_account", rset.getString("SUBACCOUNTNAME"));
				dataObj.put("balance", rset.getString("OPENINGBALANCE"));
				dataObj.put("balance_date", rset.getString("OPENINGBALANCEDATE"));
				dataObj.put("landedcostcal", rset.getString("LANDEDCOSTCAL"));
				dataObj.put("mainaccid", rset.getString("MAINACCID"));
				dataObj.put("account_code", rset.getString("ACCOUNT_CODE"));
				
				mainObj.put("results", dataObj);
			}

		} catch (NamingException | SQLException e) {
			JSONObject dataObj = new JSONObject();
			dataObj.put("responseText", "failed");
			mainObj.put("data", dataObj);
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return mainObj;
	}
	public JSONObject CoaFullRecord(String plant, String id) {
		JSONObject mainObj = new JSONObject();
		String query = "select a.ID,a.ACCOUNT_NAME,a.ACCOUNTDETAILTYPE as DETAILID,a.ACCOUNTTYPE,c.ACCOUNTTYPE as ACCOUNTTYPENAME,a.DESCRIPTION,a.ISSUBACCOUNT,a.SUBACCOUNTNAME,a.OPENINGBALANCE,a.OPENINGBALANCEDATE,b.ACCOUNTDETAILTYPE,ISNULL(a.LANDEDCOSTCAL,0) as LANDEDCOSTCAL,c.MAINACCOUNTID as MAINACCID,d.MAINACCOUNT as MAINACCNAME from " + plant + "_FINCHARTOFACCOUNTS a join " + plant +"_FINACCOUNTDETAILTYPE b on b.ID=a.ACCOUNTDETAILTYPE join " + plant + "_FINACCOUNTTYPE c on c.ID=a.ACCOUNTTYPE left join " + plant + "_FINMAINACCOUNT d on c.MAINACCOUNTID=d.ID where a.ID ='" + id + "'";
		this.mLogger.query(this.printQuery, query);
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			while (rset.next()) {
				JSONObject dataObj = new JSONObject();

				dataObj.put("account_id", rset.getString("ID"));
				dataObj.put("account_name", rset.getString("ACCOUNT_NAME"));
				dataObj.put("account_det_id", rset.getString("DETAILID"));
				dataObj.put("account_det_type", rset.getString("ACCOUNTDETAILTYPE"));
				dataObj.put("account_type", rset.getString("ACCOUNTTYPE"));
				dataObj.put("account_type_name", rset.getString("ACCOUNTTYPENAME"));
				dataObj.put("account_desc", rset.getString("DESCRIPTION"));
				dataObj.put("issub_account", rset.getString("ISSUBACCOUNT"));
				dataObj.put("sub_account", rset.getString("SUBACCOUNTNAME"));
				dataObj.put("balance", rset.getString("OPENINGBALANCE"));
				dataObj.put("balance_date", rset.getString("OPENINGBALANCEDATE"));
				dataObj.put("landedcostcal", rset.getString("LANDEDCOSTCAL"));
				dataObj.put("mainaccid", rset.getString("MAINACCID"));
				dataObj.put("mainaccname", rset.getString("MAINACCNAME"));
				mainObj.put("results", dataObj);
			}

		} catch (NamingException | SQLException e) {
			JSONObject dataObj = new JSONObject();
			dataObj.put("responseText", "failed");
			mainObj.put("data", dataObj);
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return mainObj;
	}
	public JSONObject readTableRecordByName(String plant, String name) {
		JSONObject mainObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		String query = "SELECT * FROM [" + plant + "_FINCHARTOFACCOUNTS] WHERE ACCOUNT_NAME LIKE '" + name + "%'";
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			while (rset.next()) {
				JSONObject dataObj = new JSONObject();
				dataObj.put("id", rset.getString("id"));
				dataObj.put("account_name", rset.getString("account_name"));
				//dataObj.put("account_code", rset.getString("account_code"));
				dataObj.put("account_type", rset.getString("ACCOUNTTYPE"));
				jsonArray.add(dataObj);
			}
		} catch (NamingException | SQLException e) {
			JSONObject dataObj = new JSONObject();
			dataObj.put("responseText", "failed");
			mainObj.put("data", dataObj);
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		mainObj.put("accounts",jsonArray);
		return mainObj;
	}
	public JSONObject getCOAByName(String plant, String name) {
		JSONObject mainObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject dataObj = new JSONObject();
		String query = "SELECT * FROM [" + plant + "_FINCHARTOFACCOUNTS] WHERE ACCOUNT_NAME = '" + name + "'";
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			while (rset.next()) {
				
				mainObj.put("id", rset.getString("id"));
				mainObj.put("account_name", rset.getString("account_name"));
				//dataObj.put("account_code", rset.getString("account_code"));
				mainObj.put("account_type", rset.getString("ACCOUNTTYPE"));
				mainObj.put("landed_type", rset.getString("LANDEDCOSTCAL"));
				//jsonArray.add(dataObj);
			}
		} catch (NamingException | SQLException e) {
			mainObj.put("responseText", "failed");
			//mainObj.put("data", mainObj);
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		//mainObj.put("accounts",jsonArray);
		//mainObj.put("data", dataObj);
		return mainObj;
	}
	
	public JSONObject getCOAByNameandsubid(String plant, String name, String sid) {
		JSONObject mainObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject dataObj = new JSONObject();
		String query = "SELECT * FROM [" + plant + "_FINCHARTOFACCOUNTS] WHERE SUBACCOUNTNAME= '"+ sid + "' AND ACCOUNT_NAME = '" + name + "'";
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			while (rset.next()) {
				
				mainObj.put("id", rset.getString("id"));
				mainObj.put("account_name", rset.getString("account_name"));
				//dataObj.put("account_code", rset.getString("account_code"));
				mainObj.put("account_type", rset.getString("ACCOUNTTYPE"));
				mainObj.put("landed_type", rset.getString("LANDEDCOSTCAL"));
				//jsonArray.add(dataObj);
			}
		} catch (NamingException | SQLException e) {
			mainObj.put("responseText", "failed");
			//mainObj.put("data", mainObj);
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		//mainObj.put("accounts",jsonArray);
		//mainObj.put("data", dataObj);
		return mainObj;
	}
	public boolean isExistsType(String Type,String Group, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + "FINACCTYPE] a join [" + plant + "_" + 
					"FINACCTYPEGROUP] b on (a.accgroupid = b.id) WHERE GROUPS= '"+ Group + "' AND TYPE= '"+Type+"'";
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
	
	public String GetaccountGroupId(String aGroup, String aPlant)
	{	
		String GroupId = "";
		try {		

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("GROUPS", aGroup);

		String query = " ID ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht,"FINACCTYPEGROUP",aPlant);

		GroupId = (String) m.get("ID");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return GroupId;
	}
	public Map selectRow(String query, Hashtable ht, String TABLE_NAME, String aPlant) throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+aPlant+"_"+ TABLE_NAME);
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
	public boolean isExists(Hashtable ht,String TABLE_NAME, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sQry = new StringBuffer("SELECT count(*) from "
					+plant+"_"+ TABLE_NAME);
			sQry.append(" WHERE ");
			String conditon = formCondition(ht);
			sQry.append(conditon);
			this.mLogger.query(this.printQuery, sQry.toString());
			ps = con.prepareStatement(sQry.toString());
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
	public ArrayList selectReport(String query, Hashtable ht, String extCond)
			throws Exception {
		boolean flag = false;
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(query);
			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" WHERE ");
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
	
	public boolean updateaccount(String plant, String accountId, String val) {
		//JSONObject mainObj = new JSONObject();
		boolean accountupdated = false;
		String query = "update " + plant + "_FINCHARTOFACCOUNTS "+val+" where id = '"
				+ accountId + "'";
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			int recordsChanged = stmt.executeUpdate(query);
			
			if (recordsChanged >0) {
				accountupdated=true;
				
			}
			
		} catch (NamingException | SQLException e) {

			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return accountupdated;
	}

	public String getSubAccName(String subaccountid, String aPlant)
	{	
		String accountName = "";
		try {		

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("id", subaccountid);

		String query = " ACCOUNT_NAME ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht,"FINCHARTOFACCOUNTS",aPlant);

		accountName = (String) m.get("ACCOUNT_NAME");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return accountName;
	}
	public String checkAccCount(String accName, String aPlant)
	{	
		String accountCount = "";
		try {		

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("ACCOUNT_NAME", accName);

		String query = " count(*) as countacc ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht,"FINCHARTOFACCOUNTS",aPlant);

		accountCount = (String) m.get("countacc");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return accountCount;
	}
	public String checkDetAccCount(String detName, String aPlant)
	{	
		String accountCount = "";
		try {		

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("ACCOUNTDETAILTYPE", detName);

		String query = " count(*) as countacc ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht,"FINACCOUNTDETAILTYPE",aPlant);

		accountCount = (String) m.get("countacc");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return accountCount;
	}
	public boolean deleteAccount(String accid, String aPlant)
	{	
		boolean accountDeleted = false;
		Connection con = null;
		
		try {		
			String query = "Delete from "+aPlant+"_FINCHARTOFACCOUNTS  where id = '"+ accid + "'";
			this.mLogger.query(this.printQuery, query);
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			int recordsChanged = stmt.executeUpdate(query);
			
			if (recordsChanged >0) {
				accountDeleted=true;
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return accountDeleted;
	}
	
	
	public boolean isbankbalance(String plant, String account_name) {
		String query = "select b.ACCOUNTDETAILTYPE from " + plant + "_FINCHARTOFACCOUNTS a join " + plant +"_FINACCOUNTDETAILTYPE b on b.ID=a.ACCOUNTDETAILTYPE where a.ACCOUNT_NAME ='" + account_name + "'";
		Connection con = null;
		boolean status = false;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			while (rset.next()) {
				String detailtype = rset.getString("ACCOUNTDETAILTYPE");
				detailtype = detailtype.trim();
				if(detailtype.equalsIgnoreCase("Bank Balances")) {
					status = true;
				}
			}

		} catch (NamingException | SQLException e) {
			status = false;
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return status;
	}
	
	public List<CostofgoodsLanded> getCOAByName(List<CostofgoodsLanded> account_name,String plant) {
		CostofgoodsLanded dbObj=null;
		List<CostofgoodsLanded> dbList=new ArrayList<>();
		StringBuffer accountNme=new StringBuffer();
		for(int i=0;i<account_name.size();i++) {
			accountNme.append("'").append(account_name.get(i).getAccount_name()).append("'").append(",");;
		}
		accountNme.deleteCharAt(accountNme.length() - 1);
		String query = "SELECT LANDEDCOSTCAL,ACCOUNT_NAME FROM [" + plant + "_FINCHARTOFACCOUNTS] WHERE ACCOUNT_NAME IN (" + accountNme.toString() + ")";
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			while (rset.next()) {
				dbObj=new CostofgoodsLanded();
				dbObj.setAccount_name(rset.getString("ACCOUNT_NAME"));
				dbObj.setLandedcostcal(rset.getString("LANDEDCOSTCAL"));
				dbList.add(dbObj);
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return dbList;
	}
	
	public boolean isExistsAccount(String Account ,String plant, String accid)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + "FINCHARTOFACCOUNTS] WHERE SUBACCOUNTNAME ='"+accid+"' AND ACCOUNT_NAME= '"+Account+"'";
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
	
	public List<String> getChartOfAccount(String aPlant)
	{	
		List<String> chartOfAccount=new ArrayList<>();
		Connection con = null;
		
		try {		
			String query = "Select ACCOUNT_NAME from "+aPlant+"_FINCHARTOFACCOUNTS ";
			this.mLogger.query(this.printQuery, query);
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			while (rset.next()) {
				chartOfAccount.add(rset.getString("ACCOUNT_NAME"));
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return chartOfAccount;
	}
	
	public String GetMainAccountId(String id, String aPlant)
	{	
		String mainId = "";
		try {		

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("ID", id);

		String query = " MAINACCOUNTID ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht,"FINACCOUNTTYPE",aPlant);

		mainId = (String) m.get("MAINACCOUNTID");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return mainId;
	}
	
	public boolean isExistsAccountCodeAG(String plant,String accountcode)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + "FINACCOUNTTYPE] WHERE PLANT ='"+plant+"' AND ACCOUNT_CODE= '"+accountcode+"'";
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

	public boolean isExistsAccountCodeAT(String plant,String accountcode)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + "FINACCOUNTDETAILTYPE] WHERE PLANT ='"+plant+"' AND ACCOUNT_CODE= '"+accountcode+"'";
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
	

	public boolean isExistsAccountCodeCOA(String plant,String accountcode)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + "FINCHARTOFACCOUNTS] WHERE PLANT ='"+plant+"' AND ACCOUNT_CODE= '"+accountcode+"'";
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
	
	public List<Map<String, String>> getMaxAccoutCode(String plant, String gid, String atid) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> groupList = new ArrayList<>();
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			//query = "SELECT ACCOUNT_CODE  FROM " + "[" + plant + "_" + "FINCHARTOFACCOUNTS] WHERE  PLANT ='"+plant+"' AND ACCOUNTTYPE= '"+gid+"' AND ACCOUNTDETAILTYPE= '"+atid+"' ORDER BY id DESC LIMIT 0, 1";
			//query = "SELECT * FROM" + "[" + plant + "_" + "FINCHARTOFACCOUNTS] WHERE id = (SELECT MAX(id) FROM" + "[" + plant + "_" + "FINCHARTOFACCOUNTS] WHERE  PLANT ='"+plant+"' AND ACCOUNTTYPE= '"+gid+"' AND ACCOUNTDETAILTYPE= '"+atid+"')";
			query = "SELECT MAX(cast(ISNULL(CODE,'0') as INT)) AS CODE FROM" + "[" + plant + "_" + "FINCHARTOFACCOUNTS] WHERE  PLANT ='"+plant+"' AND ACCOUNTTYPE= '"+gid+"' AND ACCOUNTDETAILTYPE= '"+atid+"'";
			PreparedStatement ps = con.prepareStatement(query);
			this.mLogger.query(this.printQuery, query);

			groupList = selectData(ps, args);
			System.out.println(groupList);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return groupList;
	}
	
	public List<Map<String, String>> getMaxAccoutDETCode(String plant, String gid) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> groupList = new ArrayList<>();
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			//query = "SELECT ACCOUNT_CODE  FROM " + "[" + plant + "_" + "FINCHARTOFACCOUNTS] WHERE  PLANT ='"+plant+"' AND ACCOUNTTYPE= '"+gid+"' AND ACCOUNTDETAILTYPE= '"+atid+"' ORDER BY id DESC LIMIT 0, 1";
			query = "SELECT MAX(cast(CODE as INT)) AS CODE FROM" + "[" + plant + "_" + "FINACCOUNTDETAILTYPE] WHERE  PLANT ='"+plant+"' AND ACCOUNTTYPEID= '"+gid+"'";
			PreparedStatement ps = con.prepareStatement(query);
			this.mLogger.query(this.printQuery, query);

			groupList = selectData(ps, args);
			System.out.println(groupList);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return groupList;
	}
	
	public String GetAccountCodeByName(String accname, String aPlant)
	{	
		String AccCode = "";
		try {		

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("ACCOUNT_NAME", accname);

		String query = " ISNULL(ACCOUNT_CODE,'0') AS ACCOUNT_CODE ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht,"FINCHARTOFACCOUNTS",aPlant);

		AccCode = (String) m.get("ACCOUNT_CODE");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return AccCode;
	}
	
	public List<Map<String, String>> getbysubaccount(String plant, String sid) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> groupList = new ArrayList<>();
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			//query = "SELECT ACCOUNT_CODE  FROM " + "[" + plant + "_" + "FINCHARTOFACCOUNTS] WHERE  PLANT ='"+plant+"' AND ACCOUNTTYPE= '"+gid+"' AND ACCOUNTDETAILTYPE= '"+atid+"' ORDER BY id DESC LIMIT 0, 1";
			query = "SELECT * FROM" + "[" + plant + "_" + "FINCHARTOFACCOUNTS] WHERE  PLANT ='"+plant+"' AND SUBACCOUNTNAME= '"+sid+"'";
			PreparedStatement ps = con.prepareStatement(query);
			this.mLogger.query(this.printQuery, query);

			groupList = selectData(ps, args);
			System.out.println(groupList);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return groupList;
	}
	
	public List<Map<String, String>> getMaxSubCode(String plant, String sid) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> groupList = new ArrayList<>();
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			//query = "SELECT ACCOUNT_CODE  FROM " + "[" + plant + "_" + "FINCHARTOFACCOUNTS] WHERE  PLANT ='"+plant+"' AND ACCOUNTTYPE= '"+gid+"' AND ACCOUNTDETAILTYPE= '"+atid+"' ORDER BY id DESC LIMIT 0, 1";
			//query = "SELECT * FROM" + "[" + plant + "_" + "FINCHARTOFACCOUNTS] WHERE id = (SELECT MAX(id) FROM" + "[" + plant + "_" + "FINCHARTOFACCOUNTS] WHERE  PLANT ='"+plant+"' AND ACCOUNTTYPE= '"+gid+"' AND ACCOUNTDETAILTYPE= '"+atid+"')";
			query = "SELECT MAX(ISNULL(SUB_CODE,'0')) AS CODE FROM" + "[" + plant + "_" + "FINCHARTOFACCOUNTS] WHERE  PLANT ='"+plant+"' AND SUBACCOUNTNAME= '"+sid+"'";
			PreparedStatement ps = con.prepareStatement(query);
			this.mLogger.query(this.printQuery, query);

			groupList = selectData(ps, args);
			System.out.println(groupList);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return groupList;
	}
	
	
	public String GetGCodeById(String id, String aPlant)
	{	
		String AccCode = "";
		try {		

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("ID", id);

		String query = " ISNULL(CODE,'0') AS CODE ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht,"FINACCOUNTTYPE",aPlant);

		AccCode = (String) m.get("CODE");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return AccCode;
	}
	
	public String GetDCodeById(String id, String aPlant)
	{	
		String AccCode = "";
		try {		

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("ID", id);

		String query = " ISNULL(CODE,'0') AS CODE ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht,"FINACCOUNTDETAILTYPE",aPlant);

		AccCode = (String) m.get("CODE");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return AccCode;
	}
	
	public String GetAccountCodeByID(String id, String aPlant)
	{	
		String AccCode = "";
		try {		

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("ID", id);

		String query = " ISNULL(ACCOUNT_CODE,'0') AS ACCOUNT_CODE ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht,"FINCHARTOFACCOUNTS",aPlant);

		AccCode = (String) m.get("ACCOUNT_CODE");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return AccCode;
	}
	
	public String GetAccountNameById(String id, String aPlant)
	{	
		String AccName = "";
		try {		

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("ID", id);

		String query = " ISNULL(ACCOUNT_NAME,'') AS ACCOUNT_NAME ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht,"FINCHARTOFACCOUNTS",aPlant);

		AccName = (String) m.get("ACCOUNT_NAME");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return AccName;
	}
	
	public boolean isExistAccount(String account, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" +plant+"_FINCHARTOFACCOUNTS"
					+ "]" + " WHERE ACCOUNT_NAME = '"
					+ account.toUpperCase() + "'";
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
	
	public boolean isExisitcoaAccount(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "["+ht.get("PLANT")+"_FINCHARTOFACCOUNTS]");
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

	public boolean updatecoaAccount(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + "FINCHARTOFACCOUNTS" + "]");
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
	
	public String GetIdByName(String accname, String aPlant)
	{	
		String AccCode = "";
		try {		

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("ACCOUNT_NAME", accname);

		String query = " ISNULL(ID,'0') AS ID ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht,"FINCHARTOFACCOUNTS",aPlant);

		AccCode = (String) m.get("ID");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return AccCode;
	}
}
