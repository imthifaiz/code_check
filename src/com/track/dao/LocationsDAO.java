package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class LocationsDAO extends BaseDAO {
	public static String TABLE_COUNTRY = "COUNTRYMASTER";
	public static String TABLE_STATE = "STATEMASTER";
	
	private boolean printQuery = MLoggerConstant.COUNTRYNCURRENCYDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.COUNTRYNCURRENCYDAO_PRINTPLANTMASTERLOG;

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
 public LocationsDAO ()
 {
	 
 }

 public boolean addMultipleCountries(List<Hashtable<String, String>> countryList, String plant) 
			throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {		
			/*Instantiate the list*/
			args = new ArrayList<String>();
			
			connection = DbBean.getConnection();
			
			for (Hashtable<String, String> country : countryList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = country.keys();
				for (int i = 0; i < country.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) country.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ TABLE_COUNTRY +"]  ("
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

 public boolean addMultipleStates(List<Hashtable<String, String>> stateList, String plant) 
			throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {		
			/*Instantiate the list*/
			args = new ArrayList<String>();
			
			connection = DbBean.getConnection();
			
			for (Hashtable<String, String> state : stateList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = state.keys();
				for (int i = 0; i < state.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) state.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ TABLE_STATE +"]  ("
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
 public int truncateCountry() throws Exception
	{
		int updatecount=0;
		Connection connection = null;
		PreparedStatement ps = null;
		String query="";
		try {			
				    
					connection = com.track.gates.DbBean.getConnection();
					query = "TRUNCATE TABLE "+TABLE_COUNTRY;

					if(connection != null){
						  /*Create  PreparedStatement object*/
						   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						   
					this.mLogger.query(this.printQuery, query);
					 ps.execute();
					
				}
		}catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				} finally {
					if (connection != null) {
						DbBean.closeConnection(connection);
					}
				}
		return updatecount;
	}	 
 public int truncateState() throws Exception
	{
		int updatecount=0;
		Connection connection = null;
		PreparedStatement ps = null;
		String query="";
		try {			
				    
					connection = com.track.gates.DbBean.getConnection();
					query = "TRUNCATE TABLE "+TABLE_STATE;

					if(connection != null){
						  /*Create  PreparedStatement object*/
						   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
						   
					this.mLogger.query(this.printQuery, query);
					 ps.execute();
					
				}
		}catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				} finally {
					if (connection != null) {
						DbBean.closeConnection(connection);
					}
				}
		return updatecount;
	}
 

public boolean isExistLoc(String loc, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean isExists = false;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " + "[" +plant+"_LOCMST"
				+ "]" + " WHERE " + IConstants.LOC + " = '"
				+ loc.toUpperCase() + "'";
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

}
