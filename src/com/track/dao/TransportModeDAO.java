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
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;
@SuppressWarnings({"rawtypes", "unchecked"})
public class TransportModeDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.TRANSPORTMODE_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.TRANSPORTMODEDAO_PRINTPLANTMASTERLOG;

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

	public TransportModeDAO() {

	}

	public boolean insertIntoMst(Hashtable<String, String> ht) throws Exception {
		boolean insertedInv = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration<String> enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "TRANSPORT_MODE_MST" + "]" + "("
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
	
	public int insertIntoMstModal(Hashtable<String, String> ht) throws Exception {
		int insertedInv = 0;
		java.sql.Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {
			  args = new ArrayList<String>();		    
				connection = DbBean.getConnection();			
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				args.add(value);
				FIELDS += key + ",";
				VALUES += "?,";
			}
			 query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "TRANSPORT_MODE_MST" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			insertedInv = execute_NonSelectQueryGetLastInsert(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return insertedInv;
	}
	
	

	public boolean isExistTransport(String transport, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + "TRANSPORT_MODE_MST"
					+ "]" + " WHERE " + IConstants.TRANSPORT_MODE + " = '"
					+ transport.toUpperCase() + "'";
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
	
	public String getTransportModeById(String plant, int id)throws Exception {
		java.sql.Connection connection = null;
	    String query = "";
	    String transfername="";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT TRANSPORT_MODE FROM ["+ plant +"_"+"TRANSPORT_MODE_MST"+"] WHERE ID="+id;

			if(connection != null){
				Map m = this.getRowOfData(connection, query.toString());

				transfername = (String) m.get("TRANSPORT_MODE");
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
		return transfername;
	}

	public String getTransportModeByName(String plant, String transport)throws Exception {
		java.sql.Connection connection = null;
	    String query = "";
	    String transfername= "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT ID FROM ["+ plant +"_"+"TRANSPORT_MODE_MST"+"] WHERE TRANSPORT_MODE="+ "'" +transport +"'" ;

			if(connection != null){
				Map m = this.getRowOfData(connection, query.toString());

				transfername = (String) m.get("ID");
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
		return transfername;
	}

	
	public boolean isExists(Hashtable<String, String> ht) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();

			// query
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" 1 ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "TRANSPORT_MODE_MST"
					+ "]");
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

	public boolean updateTranModelMst(Hashtable<String, String> htUpdate, Hashtable<String, String> htCondition)
			throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";
			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = StrUtils
						.fString((String) enumUpdate.nextElement());
				String value = StrUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}
			sCondition = formCondition(htCondition);
			sUpdate = (sUpdate.length() > 0) ? " SET "
					+ sUpdate.substring(0, sUpdate.length() - 1) : "";
			

			String stmt = "UPDATE " + "[" + htCondition.get("PLANT") + "_"
					+ "TRANSPORT_MODE_MST" + "]" + sUpdate + "WHERE " +sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				update = true;

		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return update;
	}
	
	public ArrayList<Map<String, String>> getTranModelDetails(String transportModeId, String plant,
			String extraCon) throws Exception {
		java.sql.Connection con = null;
		ArrayList<Map<String, String>> al = new ArrayList<Map<String, String>>();
		try {
			con = com.track.gates.DbBean.getConnection();

			String sQry = "select distinct ID,TRANSPORT_MODE from "
					+ "["
					+ plant
					+ "_"
					+ "TRANSPORT_MODE_MST] where TRANSPORT_MODE like '%"
					+ transportModeId
					+ "%'   " + extraCon + " ORDER BY ID ";
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
	//start code by Bruhan to delete prod brand id on 22/jan/2014
	public boolean deleteTranModelMst(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deletePrdId()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "TRANSPORT_MODE_MST"
					+ "]");
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
	//End code by Bruhan to delete prod brand id on 22/jan/2014
	
	public Map selectRow(String query, Hashtable ht, String extCond)
			throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "TRANSPORT_MODE_MST" + "]");
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
	
	public Map<String, String> getRowOfData(Connection conn, String query) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;
		Map<String, String> map = new HashMap<>();

		try {

			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				map = new HashMap<>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

					map
							.put(rs.getMetaData().getColumnLabel(i), rs
									.getString(i));

				}

			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		return map;
	}
	
	public Boolean updateSeqNo(String aFunc, String plant) throws Exception {
		Boolean flag = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			// update the next seq no
			String sQry2Update = "UPDATE " + "[" + plant + "_" + "TBLCONTROL" + "]"
					+ " SET NXTSEQ = NXTSEQ+1 WHERE FUNC = '" + aFunc + "'";
			ps = con.prepareStatement(sQry2Update);
			int i = ps.executeUpdate();
			if (i <= 0) {
				flag = false;
				throw new Exception("Unable to update!");

			} else {
				flag = true;

			}

		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return flag;
	}
}
