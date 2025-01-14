package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.HrEmpType;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class FinCountryTaxTypeDAO {


	public static String TABLE_HEADER = "FINCOUNTRYTAXTYPE";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.FinCountryTaxTypeDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.FinCountryTaxTypeDAO_PRINTPLANTMASTERLOG;
	
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

	public List<FinCountryTaxType> getCountryTaxTypes(String configkey, String countycode, String taxtype) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<FinCountryTaxType> FinCountryTaxTypeList=new ArrayList<FinCountryTaxType>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+TABLE_HEADER+"] WHERE COUNTRY_CODE = '"+countycode+"' AND CONFIGKEY = '"+configkey+"' AND (TAXTYPE LIKE '%" + taxtype + "%') ORDER BY ID ASC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   FinCountryTaxType finCountryTaxType=new FinCountryTaxType();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, finCountryTaxType);
	                   FinCountryTaxTypeList.add(finCountryTaxType);
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
		return FinCountryTaxTypeList;
	}
	
	public FinCountryTaxType getCountryTaxTypesByid(int id) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    FinCountryTaxType finCountryTaxType=new FinCountryTaxType();
	    
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+TABLE_HEADER+"] WHERE ID = '"+id+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					  ResultSetToObjectMap.loadResultSetIntoObject(rst, finCountryTaxType);
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
		return finCountryTaxType;
	}
	
	

}
