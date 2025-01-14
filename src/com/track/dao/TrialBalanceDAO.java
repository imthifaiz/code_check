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


public class TrialBalanceDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.TrialBalanceDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.TrialBalanceDAO_PRINTPLANTMASTERLOG;

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