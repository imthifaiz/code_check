package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HrClaim;
import com.track.db.object.HrDeductionDet;
import com.track.db.object.POSAmountByPayMode;
import com.track.db.object.POSBankInByPayMode;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class POSBankInByPayModeDAO extends BaseDAO {
	public static String TABLE_HEADER = "POSBANKINBYPAYMODE";
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
	
	public int addBankInPaymode(POSBankInByPayMode pOSBankInByPayMode) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ pOSBankInByPayMode.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[BANKINDATE]" + 
					"           ,[PAYMENTMODE]" +  
					"           ,[BANKINAMOUNT]" +  
					"           ,[BANKINACCOUNT]" +
					"           ,[CHARGESPERCENTAGE]" +
					"           ,[CHARGESAMOUNT]" +
					"           ,[CHARGESACCOUNT]" +
					"           ,[HDRID]" +
					"           ,[CRAT]" +
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, pOSBankInByPayMode.getPLANT());
				   ps.setString(2, pOSBankInByPayMode.getBANKINDATE());
				   ps.setString(3, pOSBankInByPayMode.getPAYMENTMODE());
				   ps.setDouble(4, pOSBankInByPayMode.getBANKINAMOUNT());
				   ps.setString(5, pOSBankInByPayMode.getBANKINACCOUNT());
				   ps.setDouble(6, pOSBankInByPayMode.getCHARGESPERCENTAGE());
				   ps.setDouble(7, pOSBankInByPayMode.getCHARGESAMOUNT());
				   ps.setString(8, pOSBankInByPayMode.getCHARGESACCOUNT());
				   ps.setInt(9, pOSBankInByPayMode.getHDRID());
				   ps.setString(10, dateutils.getDateTime());
				   ps.setString(11, pOSBankInByPayMode.getCRBY());
				  
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
					   throw new SQLException("Creating claim failed, no rows affected.");
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
	
	public POSBankInByPayMode  getbypaymodeandmaxid(String plant, String paymode)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    POSBankInByPayMode pOSBankInByPayMode=new POSBankInByPayMode();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT TOP 1 * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PAYMENTMODE='"+paymode+"' ORDER BY ID DESC";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, pOSBankInByPayMode);
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
		return pOSBankInByPayMode;
	}
	
	
	public boolean  getbypaymodeandhdrid(String plant, String paymode,int hdrid)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean result=true;
	    String query = "";
	    List<POSBankInByPayMode> pOSBankInByPayModelist=new ArrayList<POSBankInByPayMode>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PAYMENTMODE='"+paymode+"' AND HDRID ='"+hdrid+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   POSBankInByPayMode pOSBankInByPayMode=new POSBankInByPayMode();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, pOSBankInByPayMode);
	                   pOSBankInByPayModelist.add(pOSBankInByPayMode);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			if(pOSBankInByPayModelist.size() > 0) {
				result = false;
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
		return result;
	}
	
	
	public ArrayList selectbankincash(String plant,String outlet,String terminal,String fromdate,String todate) throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		
		
		String query = "SELECT A.ID,B.OUTLET,B.TERMINAL,B.SHIFTDATE,B.EMPLOYEE_ID,A.PAYMENTMODE,A.RECIVEDAMOUNT,A.RECIVEDSTATUS FROM "
				+ ""+ plant +"_POSAMOUNTBYPAYMODE AS A LEFT JOIN "+ plant +"_POSSHIFTAMOUNTHDR AS B ON A.HDRID = B.ID "
				+ "WHERE A.PAYMENTMODE = 'CASH' AND A.RECIVEDSTATUS = 1 AND A.BANKINSTATUS = 0 AND A.RECIVEDAMOUNT != 0  AND ISNULL(B.PAYRECIVEDSTATUS,0) = 2";
		if(outlet.length() > 0) {
			query += " AND B.OUTLET='"+outlet+"'";
		}
		if(terminal.length() > 0) {
			query += " AND B.TERMINAL='"+terminal+"'";
		}
		if(fromdate.length() > 0) {
			query += " AND CONVERT(DATETIME, B.SHIFTDATE, 103)  >= CONVERT(DATETIME,'"+fromdate+"', 103)";
		}
		if(todate.length() > 0) {
			query += " AND CONVERT(DATETIME, B.SHIFTDATE, 103)  <= CONVERT(DATETIME,'"+todate+"', 103)";
		}
		query += " order by CAST((SUBSTRING(B.SHIFTDATE, 7, 4) + SUBSTRING(B.SHIFTDATE, 4, 2) + SUBSTRING(B.SHIFTDATE, 1, 2)) AS date) ";
		StringBuffer sql = new StringBuffer(query);

		try {
			con = com.track.gates.DbBean.getConnection();
			this.mLogger.query(this.printQuery, sql.toString());
			alData = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;
	}
	
}
