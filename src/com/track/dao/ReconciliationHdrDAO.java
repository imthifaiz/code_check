package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HrClaim;
import com.track.db.object.HrDeductionHdr;
import com.track.db.object.POSShiftAmountHdr;
import com.track.db.object.ReconciliationHdr;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class ReconciliationHdrDAO {
	
	public static String TABLE_HEADER = "FINRECONCILIATIONHDR";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.ReconciliationHdrDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.ReconciliationHdrDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int adddreconciliationHdr(ReconciliationHdr reconciliationHdr) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ reconciliationHdr.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[RDATE]" + 
					"           ,[CURRENCYID]" + 
					"           ,[RSTATUS]" +  
					"           ,[RMONTH]" +
					"           ,[RYEAR]" +
					"           ,[RACCOUNT]" +
					"           ,[BANKOPENBALANCE]" +
					"           ,[BANKCLOSEBALANCE]" +
					"           ,[OPENBALANCE]" +
					"           ,[CLOSEBALANCE]" +
					"           ,[DEPOSITS]" +
					"           ,[WITHDRAWL]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, reconciliationHdr.getPLANT());
				   ps.setString(2, reconciliationHdr.getRDATE());
				   ps.setString(3, reconciliationHdr.getCURRENCYID());
				   ps.setInt(4, reconciliationHdr.getRSTATUS());
				   ps.setInt(5, reconciliationHdr.getRMONTH());
				   ps.setInt(6, reconciliationHdr.getRYEAR());
				   ps.setString(7, reconciliationHdr.getRACCOUNT());
				   ps.setDouble(8, reconciliationHdr.getBANKOPENBALANCE());
				   ps.setDouble(9, reconciliationHdr.getBANKCLOSEBALANCE());
				   ps.setDouble(10, reconciliationHdr.getOPENBALANCE());
				   ps.setDouble(11, reconciliationHdr.getCLOSEBALANCE());
				   ps.setDouble(12, reconciliationHdr.getDEPOSITS());
				   ps.setDouble(13, reconciliationHdr.getWITHDRAWL());
				   ps.setString(14, dateutils.getDate());
				   ps.setString(15, reconciliationHdr.getCRBY());
				   
				  
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   ResultSet rs = ps.getGeneratedKeys();
		                if(rs.next())
		                {
		                	HdrId = rs.getInt(1);
		                    
		                }
				   }
				   else
				   {
					   throw new SQLException("Creating reconciliation failed, no rows affected.");
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
		return HdrId;
	}
	
	public int updateHdr (ReconciliationHdr reconHdr) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ reconHdr.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+reconHdr.getPLANT()+"',"
					+ "RDATE='"+reconHdr.getRDATE()+"',"
					+ "CURRENCYID='"+reconHdr.getCURRENCYID()+"',"
					+ "RSTATUS='"+reconHdr.getRSTATUS()+"',"
					+ "RMONTH='"+reconHdr.getRMONTH()+"',"
					+ "RYEAR='"+reconHdr.getRYEAR()+"',"
					+ "RACCOUNT='"+reconHdr.getRACCOUNT()+"',"
					+ "BANKOPENBALANCE='"+reconHdr.getBANKOPENBALANCE()+"',"
					+ "BANKCLOSEBALANCE='"+reconHdr.getBANKCLOSEBALANCE()+"',"
					+ "OPENBALANCE='"+reconHdr.getOPENBALANCE()+"',"
					+ "CLOSEBALANCE='"+reconHdr.getCLOSEBALANCE()+"',"
					+ "DEPOSITS='"+reconHdr.getDEPOSITS()+"',"
					+ "WITHDRAWL='"+reconHdr.getWITHDRAWL()+"',"
					+ "UPAT='"+dateutils.getDateTime()+"',UPBY='"+reconHdr.getUPBY()+"' WHERE ID="+reconHdr.getID();
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   ResultSet rs = ps.getGeneratedKeys();
		                if(rs.next())
		                {
		                	HdrId = rs.getInt(1);   
		                }  
				   }
				   else
				   {
					   throw new SQLException("Updating claim failed, no rows affected.");
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
		return HdrId;
	}
	
	public List<ReconciliationHdr> getAllReconciliationHdr(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<ReconciliationHdr> ReconciliationHdrList=new ArrayList<ReconciliationHdr>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   ReconciliationHdr reconciliationHdr=new ReconciliationHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, reconciliationHdr);
	                   ReconciliationHdrList.add(reconciliationHdr);
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
		return ReconciliationHdrList;
	}
	
	
	public List<ReconciliationHdr>  getallbyfilter(String plant, String account,String fromdate,String todate)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<ReconciliationHdr> ReconciliationHdrList=new ArrayList<ReconciliationHdr>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"'";
			
			if(account.length() > 0) {
				query += " AND RACCOUNT='"+account+"'";
			}
			if(fromdate.length() > 0) {
				query += " AND CONVERT(DATETIME, RDATE, 103)  >= CONVERT(DATETIME,'"+fromdate+"', 103)";
			}
			if(todate.length() > 0) {
				query += " AND CONVERT(DATETIME, RDATE, 103)  <= CONVERT(DATETIME,'"+todate+"', 103)";
			}

			query += " ORDER BY CAST(CONCAT((CASE WHEN (LEN(RMONTH) = 1) THEN CONCAT(RYEAR,'0') ELSE RYEAR END),RMONTH) AS INTEGER) DESC";
			
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   ReconciliationHdr reconciliationHdr=new ReconciliationHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, reconciliationHdr);
	                   ReconciliationHdrList.add(reconciliationHdr);
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
		return ReconciliationHdrList;
	}
	
	
	public ReconciliationHdr getByAccMonYear(String plant,String account,int month,int year) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    ReconciliationHdr reconciliationHdr=new ReconciliationHdr();
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT TOP 1 * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE RSTATUS != 2 AND RACCOUNT='"+account+"' AND RMONTH='"+month+"' AND RYEAR='"+year+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, reconciliationHdr);
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
		return reconciliationHdr;
	}
	
	public ReconciliationHdr getByID(String plant,String id) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    ReconciliationHdr reconciliationHdr=new ReconciliationHdr();
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID='"+id+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, reconciliationHdr);
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
		return reconciliationHdr;
	}
	
	public boolean getstatusByAccMonYear(String plant,String account,int month,int year) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<ReconciliationHdr> ReconciliationHdrList=new ArrayList<ReconciliationHdr>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE RSTATUS != 2 AND RACCOUNT='"+account+"' AND RMONTH='"+month+"' AND RYEAR='"+year+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   ReconciliationHdr reconciliationHdr=new ReconciliationHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, reconciliationHdr);
	                   ReconciliationHdrList.add(reconciliationHdr);
	                }   
			this.mLogger.query(this.printQuery, query);
			}
			
			if(ReconciliationHdrList.size() > 0) {
				flag = true;
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
	
	public boolean deleteById(int ID, String plant)throws Exception {	
		boolean flag = false;	
		Connection connection = null;	
		PreparedStatement ps = null;	
	    String query = "";	
		try {	    	
			connection = DbBean.getConnection();	
			query = "DELETE FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE [ID]=?";	
			if(connection != null){	
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);	
				   ps.setInt(1, ID);	
				   int count=ps.executeUpdate();	
				   if(count>0)	
				   {	
					   flag=true;	
				   }	
				   else	
				   {	
					   throw new SQLException("Delete journal failed, no rows affected.");	
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
		return flag;	
	}	

}
