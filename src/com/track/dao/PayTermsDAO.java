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
import com.track.util.StrUtils;

public class PayTermsDAO extends BaseDAO   {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.PayTermsDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.PayTermsDAO_PRINTPLANTMASTERLOG;
	private String TABLE_NAME = "FINPAYMENTTERMS";

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
	
	public List selectRow(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> paytermsList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, PAYMENT_TERMS, NO_DAYS, CRAT, CRBY, UPAT, UPBY  FROM "
			+ "[" + ht.get("PLANT") + "_" + TABLE_NAME + "] WHERE PAYMENT_TERMS LIKE ? AND PLANT =? ";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add(ht.get("TERMS")+"%");
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			paytermsList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return paytermsList;
	}

	public boolean isExistPaytype(String paytype, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + "FINPAYMENTMODEMST"
					+ "]" + " WHERE " + IConstants.PAYMENTMODE + " = '"
					+ paytype.toUpperCase() + "'";
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
	
	public boolean isExistPayterms(String payterms, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + "FINPAYMENTTERMS"
					+ "]" + " WHERE " + IConstants.PAYMENT_TERMS + " = '"
					+ payterms.toUpperCase() + "'";
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
	
	public boolean addpaymentTerms(Hashtable ht, String plant)throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {
			connection = DbBean.getConnection();
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
			}
			query = "INSERT INTO ["+ plant + "_" + TABLE_NAME + "] ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			flag = execute_NonSelectQuery(ps, args);
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
}
