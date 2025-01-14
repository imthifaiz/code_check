package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;


public class TaxSettingDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.TaxSettingDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.TaxSettingDAO_PRINTPLANTMASTERLOG;

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
	
	public int addTaxSetting(Hashtable ht, String plant,boolean isEdit,String taxid) throws Exception
	{
		boolean flag = false;
		int addTaxsettings = 0;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
	    String query = "",SETQUERY=" ";
		try {
			 /*Instantiate the list*/
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
				if(i<ht.size()-1)
				{
					SETQUERY+=key+"='"+value+"',";
				}
				else
				{
					SETQUERY+=key+"='"+value+"'";
				}
			}
			if(isEdit)
			{
				query = "UPDATE ["+ plant +"_FINTAXSETTINGS] SET "+SETQUERY+" WHERE ID="+taxid;

				if(connection != null){
					  /*Create  PreparedStatement object*/
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   
				this.mLogger.query(this.printQuery, query);
				addTaxsettings = ps.executeUpdate();
				}
			}
			else
			{
				query = "INSERT INTO ["+ plant +"_FINTAXSETTINGS] ("
						+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
						+ VALUES.substring(0, VALUES.length() - 1) + ")";

				if(connection != null){
					  /*Create  PreparedStatement object*/
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   
				this.mLogger.query(this.printQuery, query);
				addTaxsettings = execute_NonSelectQueryGetLastInsert(ps, args);
				}
			
			}
			query = "UPDATE [PLNTMST] SET RCBNO='"+ht.get("TAX")+"',TAXBYLABEL='"+ht.get("TAXBYLABEL")+"' WHERE PLANT='"+plant+"'";

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			addTaxsettings = ps.executeUpdate();
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return addTaxsettings;
	}
	
	public Map<String,String> getTaxSetting(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		Map<String, String> map = null;
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINTAXSETTINGS] "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			map = getRowOfData(con, query);		
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
	public Map<String,String> getTaxSettingFromPlanMast(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		Map<String, String> map = null;
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT RCBNO as TAX,TAXBYLABEL FROM [PLNTMST] "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			map = getRowOfData(con, query);		
			map.put("ISINTERNATIONALTRADE", "0");
			map.put("VATREGISTEREDON", "");
			map.put("RETURNFROM", "");
			map.put("REPORTINGPERIOD", "Monthly");
			
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
	
	public Map<String,String> IsTaxRegistered(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		Map<String, String> map = null;
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ISTAXREGISTRED FROM [PLNTMST] "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"'";
			PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			map = getRowOfData(con, query);		
			
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
	
}