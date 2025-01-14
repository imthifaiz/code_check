package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.POSAmountByPayMode;
import com.track.db.object.POSShiftAmountHdr;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class POSShiftAmountHdrDAO {
	public static String TABLE_HEADER = "POSSHIFTAMOUNTHDR";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrEmpTypeDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrEmpTypeDAO_PRINTPLANTMASTERLOG;
	
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
	
	public List<POSShiftAmountHdr>  getbyotds(String plant, String status, String outlet,String terminal,String fromdate,String todate)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<POSShiftAmountHdr> pOSShiftAmountHdrList=new ArrayList<POSShiftAmountHdr>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"'";
			
			if(status.length() > 0) {
				query += " AND PAYRECIVEDSTATUS='"+status+"'";
			}
			if(outlet.length() > 0) {
				query += " AND OUTLET='"+outlet+"'";
			}
			if(terminal.length() > 0) {
				query += " AND TERMINAL='"+terminal+"'";
			}
			if(fromdate.length() > 0) {
				query += " AND CONVERT(DATETIME, SHIFTDATE, 103)  >= CONVERT(DATETIME,'"+fromdate+"', 103)";
			}
			if(todate.length() > 0) {
				query += " AND CONVERT(DATETIME, SHIFTDATE, 103)  <= CONVERT(DATETIME,'"+todate+"', 103)";
			}

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   POSShiftAmountHdr pOSShiftAmountHdr=new POSShiftAmountHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, pOSShiftAmountHdr);
	                   pOSShiftAmountHdrList.add(pOSShiftAmountHdr);
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
		return pOSShiftAmountHdrList;
	}
	
	
	public POSShiftAmountHdr  getbyid(String plant, int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    POSShiftAmountHdr pOSShiftAmountHdr=new POSShiftAmountHdr();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID='"+id+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   ResultSetToObjectMap.loadResultSetIntoObject(rst, pOSShiftAmountHdr);
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
		return pOSShiftAmountHdr;
	}
	
	public int updatebyid(POSShiftAmountHdr pOSShiftAmountHdr, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ pOSShiftAmountHdr.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+pOSShiftAmountHdr.getPLANT()+"',"
					+ "SHIFTID='"+pOSShiftAmountHdr.getShiftId()+"',"
					+ "OUTLET='"+pOSShiftAmountHdr.getOutlet()+"',"
					+ "TERMINAL='"+pOSShiftAmountHdr.getTerminal()+"',"
					+ "SHIFTDATE='"+pOSShiftAmountHdr.getShiftDate()+"',"
					+ "SHIFTTIME='"+pOSShiftAmountHdr.getShiftTime()+"',"
					+ "EMPLOYEE_ID='"+pOSShiftAmountHdr.getEmployeeID()+"',"
					+ "TOTALSALES='"+pOSShiftAmountHdr.getTotalSales()+"',"
					+ "TOTALRECEIVEDAMOUNT='"+pOSShiftAmountHdr.getTotalReceivedAmount()+"',"
					+ "PAYSTATUS='"+pOSShiftAmountHdr.getPayStatus()+"',"
					+ "PAYRECIVEDSTATUS='"+pOSShiftAmountHdr.getPayReciveStatus()+"',"
					+ "EXPENSE='"+pOSShiftAmountHdr.getExpenses()+"',"
					+ "DRAWERAMOUNT='"+pOSShiftAmountHdr.getDrawerAmount()+"',"
					+ "UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+pOSShiftAmountHdr.getID();
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
					   throw new SQLException("Updating shiftbypaymode failed, no rows affected.");
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
	
	public List<POSShiftAmountHdr>  getbyotdsmonth(String plant)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<POSShiftAmountHdr> pOSShiftAmountHdrList=new ArrayList<POSShiftAmountHdr>();
		try {	    
			connection = DbBean.getConnection();
			//query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"'";
			
			/*query = "SELECT 0 AS ID,0 AS SHIFTID,PLANT,'' AS SHIFTTIME,'' AS EMPLOYEE_ID,0 AS PAYSTATUS,"
					+ "0 AS PAYRECIVEDSTATUS,SUM(ISNULL(EXPENSE,0)) AS EXPENSE,SUM(ISNULL(DRAWERAMOUNT,0)) AS DRAWERAMOUNT,'' AS CRAT,'' AS CRBY,'' AS UPAT,'' AS UPBY,OUTLET,"
					+ "TERMINAL,CAST(MONTH(CONVERT(DATETIME,CAST(SUBSTRING(SHIFTDATE, 7, 4) +'-'+SUBSTRING(SHIFTDATE, 4,2) +'-'+"
					+ "SUBSTRING(SHIFTDATE, 1,2) AS date))) AS VARCHAR(2)) + '-' + CAST(YEAR(CONVERT(DATETIME,CAST(SUBSTRING(SHIFTDATE, 7, 4)+'-'+"
					+ "SUBSTRING(SHIFTDATE, 4,2) +'-'+ SUBSTRING(SHIFTDATE, 1,2) AS date))) AS VARCHAR(4)) AS SHIFTDATE,"
					+ "SUM(ISNULL(TOTALSALES,0)) AS TOTALSALES,SUM(ISNULL(TOTALRECEIVEDAMOUNT,0)) AS TOTALRECEIVEDAMOUNT,CAST(YEAR(CONVERT(DATETIME,CAST(SUBSTRING(SHIFTDATE, 7, 4)+'-'+SUBSTRING(SHIFTDATE, 4,2) +'-'+ SUBSTRING(SHIFTDATE, 1,2) AS date))) AS VARCHAR(4)) + CAST(MONTH(CONVERT(DATETIME,CAST(SUBSTRING(SHIFTDATE, 7, 4) +'-'+SUBSTRING(SHIFTDATE, 4,2) +'-'+SUBSTRING(SHIFTDATE, 1,2) AS date))) AS VARCHAR(2)) AS SHIFTTIME "
					+ "FROM "+ plant +"_"+TABLE_HEADER+" GROUP BY CAST(MONTH(CONVERT(DATETIME,CAST(SUBSTRING(SHIFTDATE, 7, 4) +'-'+"
					+ "SUBSTRING(SHIFTDATE, 4,2) +'-'+ SUBSTRING(SHIFTDATE, 1,2) AS date))) AS VARCHAR(2)) + '-' + CAST(YEAR(CONVERT(DATETIME,"
					+ "CAST(SUBSTRING(SHIFTDATE, 7, 4) +'-'+SUBSTRING(SHIFTDATE, 4,2) +'-'+ SUBSTRING(SHIFTDATE,1,2) AS date))) AS VARCHAR(4)),"
					+ "OUTLET,TERMINAL,PLANT,CAST(YEAR(CONVERT(DATETIME,CAST(SUBSTRING(SHIFTDATE, 7, 4)+'-'+SUBSTRING(SHIFTDATE, 4,2) +'-'+ SUBSTRING(SHIFTDATE, 1,2) AS date))) AS VARCHAR(4)) + CAST(MONTH(CONVERT(DATETIME,CAST(SUBSTRING(SHIFTDATE, 7, 4) +'-'+SUBSTRING(SHIFTDATE, 4,2) +'-'+SUBSTRING(SHIFTDATE, 1,2) AS date))) AS VARCHAR(2)) ORDER BY CAST(YEAR(CONVERT(DATETIME,CAST(SUBSTRING(SHIFTDATE, 7, 4)+'-'+SUBSTRING(SHIFTDATE, 4,2) +'-'+ SUBSTRING(SHIFTDATE, 1,2) AS date))) AS VARCHAR(4)) + CAST(MONTH(CONVERT(DATETIME,CAST(SUBSTRING(SHIFTDATE, 7, 4) +'-'+SUBSTRING(SHIFTDATE, 4,2) +'-'+SUBSTRING(SHIFTDATE, 1,2) AS date))) AS VARCHAR(2)) DESC";*/
			

			query = "SELECT 0 AS ID,0 AS SHIFTID,PLANT,'' AS SHIFTTIME,'' AS EMPLOYEE_ID,0 AS PAYSTATUS,0 AS PAYRECIVEDSTATUS,SUM(ISNULL(EXPENSE,0)) AS EXPENSE,"
					+ "SUM(ISNULL(DRAWERAMOUNT,0)) AS DRAWERAMOUNT,'' AS CRAT,'' AS CRBY,'' AS UPAT,'' AS UPBY,OUTLET,TERMINAL,SUM(ISNULL(TOTALSALES,0)) AS TOTALSALES,"
					+ "SUM(ISNULL(TOTALRECEIVEDAMOUNT,0)) AS TOTALRECEIVEDAMOUNT,CAST((SUBSTRING(SHIFTDATE, 7,4) ) AS VARCHAR(4)) + '-' +CAST((SUBSTRING(SHIFTDATE, 4,2) ) AS VARCHAR(2)) AS SHIFTDATE,"
					+ "CAST((SUBSTRING(SHIFTDATE, 7,4) ) AS VARCHAR(4)) + CAST((SUBSTRING(SHIFTDATE, 4,2) ) AS VARCHAR(2)) AS SHIFTTIME FROM "+ plant +"_"+TABLE_HEADER+" GROUP BY "
					+ "CAST((SUBSTRING(SHIFTDATE, 7,4) ) AS VARCHAR(4)) + '-' +CAST((SUBSTRING(SHIFTDATE, 4,2) ) AS VARCHAR(2)),CAST((SUBSTRING(SHIFTDATE, 7,4) ) AS VARCHAR(4)) + "
					+ "CAST((SUBSTRING(SHIFTDATE, 4,2) ) AS VARCHAR(2)),OUTLET,TERMINAL,PLANT ORDER BY CAST((SUBSTRING(SHIFTDATE, 7,4) ) AS VARCHAR(4)) + CAST((SUBSTRING(SHIFTDATE, 4,2) ) AS VARCHAR(2)) DESC";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   POSShiftAmountHdr pOSShiftAmountHdr=new POSShiftAmountHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, pOSShiftAmountHdr);
	                   pOSShiftAmountHdrList.add(pOSShiftAmountHdr);
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
		return pOSShiftAmountHdrList;
	}
	

}
