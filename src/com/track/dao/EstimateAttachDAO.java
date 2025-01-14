package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class EstimateAttachDAO  extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.EstimateAttachDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.EstimateAttachDAO_PRINTPLANTMASTERLOG;

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
	
	public boolean addsalesAttachments(List<Hashtable<String, String>>  salesAttachmentList, String plant)throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {		
			/*Instantiate the list*/
			args = new ArrayList<String>();
			
			connection = DbBean.getConnection();
			
			for (Hashtable<String, String> empDetInfo : salesAttachmentList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = empDetInfo.keys();
				for (int i = 0; i < empDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) empDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ plant +"_ESTIMATEORDERATTACHMENTS]  ("
				+ FIELDS.substring(0, FIELDS.length() - 1)
				+ ") VALUES ("
				+ VALUES.substring(0, VALUES.length() - 1) + ")";
				
				if(connection != null){
					/*Create  PreparedStatement object*/
					ps = connection.prepareStatement(query);
					this.mLogger.query(this.printQuery, query);
					flag = execute_NonSelectQuery(ps, args);
					if(flag){
						args.clear();
					}
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
		return flag;
	}
	
	
	public List getsalesAttachByESTNO(String plant, String eid) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> empAttachList = new ArrayList<>();
		Map<String, String> map = null;
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "SELECT * FROM"
					+ "[" + plant + "_ESTIMATEORDERATTACHMENTS] WHERE ESTNO = ? AND PLANT =? ";
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add(eid);
			args.add(plant);

			// this.mLogger.query(this.printQuery, query);

			empAttachList = selectData(ps, args);

		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return empAttachList;
	}
	
	public List getsalesAttachById(String plant, String eid) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> empAttachList = new ArrayList<>();
		Map<String, String> map = null;
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "SELECT * FROM"
					+ "[" + plant + "_ESTIMATEORDERATTACHMENTS] WHERE ID = ? AND PLANT =? ";
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add(eid);
			args.add(plant);

			// this.mLogger.query(this.printQuery, query);

			empAttachList = selectData(ps, args);

		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return empAttachList;
	}
	
	 public int deletesalesAttachByPrimId(String plant, String eid) throws Exception {
			java.sql.Connection con = null;
			Map<String, String> map = null;
			String query="";
			int count=0;
			try {			
			    
				con = com.track.gates.DbBean.getConnection();
				query="DELETE FROM " + "[" + plant+ "_ESTIMATEORDERATTACHMENTS] "
						+ "WHERE ID = ? AND PLANT = ?";
				PreparedStatement ps = con.prepareStatement(query);  
				
				/*Storing all the query param argument in list squentially*/
				ps.setString(1, eid);
				ps.setString(2, plant);	
				
				//this.mLogger.query(this.printQuery, query);	
				count=ps.executeUpdate();
				
			} catch (Exception e) {
				//this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return count;
		}
	 public int deletesalesAttachByNo(String plant, String eid) throws Exception {
			java.sql.Connection con = null;
			Map<String, String> map = null;
			String query="";
			int count=0;
			try {			
			    
				con = com.track.gates.DbBean.getConnection();
				query="DELETE FROM " + "[" + plant+ "_ESTIMATEORDERATTACHMENTS] "
						+ "WHERE ESTNO = ? AND PLANT = ?";
				PreparedStatement ps = con.prepareStatement(query);  
				
				/*Storing all the query param argument in list squentially*/
				ps.setString(1, eid);
				ps.setString(2, plant);	
				
				//this.mLogger.query(this.printQuery, query);	
				count=ps.executeUpdate();
				
			} catch (Exception e) {
				//this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return count;
		}

}
