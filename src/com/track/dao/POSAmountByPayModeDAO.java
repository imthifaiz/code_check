package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HrClaim;
import com.track.db.object.HrEmpType;
import com.track.db.object.POSAmountByPayMode;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class POSAmountByPayModeDAO {
	public static String TABLE_HEADER = "POSAMOUNTBYPAYMODE";
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
	
	public List<POSAmountByPayMode>  getbyhdridandpaymodelist(String plant, int hdrid, String paymode)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<POSAmountByPayMode> pOSAmountByPayModeList=new ArrayList<POSAmountByPayMode>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE HDRID='"+hdrid+"' AND PAYMENTMODE='"+paymode+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   POSAmountByPayMode pOSAmountByPayMode=new POSAmountByPayMode();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, pOSAmountByPayMode);
	                   pOSAmountByPayModeList.add(pOSAmountByPayMode);
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
		return pOSAmountByPayModeList;
	}
	
	public List<POSAmountByPayMode>  getbyhdrilist(String plant, int hdrid)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<POSAmountByPayMode> pOSAmountByPayModeList=new ArrayList<POSAmountByPayMode>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE HDRID='"+hdrid+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   POSAmountByPayMode pOSAmountByPayMode=new POSAmountByPayMode();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, pOSAmountByPayMode);
	                   pOSAmountByPayModeList.add(pOSAmountByPayMode);
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
		return pOSAmountByPayModeList;
	}
	
	public List<POSAmountByPayMode>  getbyhdridandstatus(String plant, int hdrid, int status)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<POSAmountByPayMode> pOSAmountByPayModeList=new ArrayList<POSAmountByPayMode>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE HDRID='"+hdrid+"' AND RECIVEDSTATUS='"+status+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   POSAmountByPayMode pOSAmountByPayMode=new POSAmountByPayMode();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, pOSAmountByPayMode);
	                   pOSAmountByPayModeList.add(pOSAmountByPayMode);
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
		return pOSAmountByPayModeList;
	}
	
	public POSAmountByPayMode  getbyhdridandpaymode(String plant, int hdrid, String paymode)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    POSAmountByPayMode pOSAmountByPayMode=new POSAmountByPayMode();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT TOP 1 * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE HDRID='"+hdrid+"' AND PAYMENTMODE='"+paymode+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, pOSAmountByPayMode);
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
		return pOSAmountByPayMode;
	}
	
	public POSAmountByPayMode  getbypaymodeandmaxid(String plant, String paymode)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    POSAmountByPayMode pOSAmountByPayMode=new POSAmountByPayMode();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT TOP 1 * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PAYMENTMODE='"+paymode+"' AND RECIVEDSTATUS = '1' ORDER BY ID DESC";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, pOSAmountByPayMode);
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
		return pOSAmountByPayMode;
	}
	
	public POSAmountByPayMode  getbyid(String plant, int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    POSAmountByPayMode pOSAmountByPayMode=new POSAmountByPayMode();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID='"+id+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, pOSAmountByPayMode);
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
		return pOSAmountByPayMode;
	}
	
	public int updatepaymode(POSAmountByPayMode pOSAmountByPayMode, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ pOSAmountByPayMode.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+pOSAmountByPayMode.getPLANT()+"',"
					+ "HDRID='"+pOSAmountByPayMode.getHdrId()+"',"
					+ "CURRENCYID='"+pOSAmountByPayMode.getCurrencyId()+"',"
					+ "AMOUNT='"+pOSAmountByPayMode.getAmount()+"',"
					+ "BALANEAMOUNT='"+pOSAmountByPayMode.getBalanceAmount()+"',"
					+ "RECIVEDAMOUNT='"+pOSAmountByPayMode.getRecivedAmount()+"',"
					+ "PAYMENTMODE='"+pOSAmountByPayMode.getPaymentMode()+"',"
					+ "RECIVEDSTATUS='"+pOSAmountByPayMode.getRecivedStatus()+"',"
					+ "BANKINSTATUS='"+pOSAmountByPayMode.getBankinStatus()+"',"
					+ "BANKINID='"+pOSAmountByPayMode.getBankinId()+"',"
					+ "EXPENSES='"+pOSAmountByPayMode.getExpenses()+"',"
					+ "EXPENSESACCOUNT='"+pOSAmountByPayMode.getExpensesAccount()+"',"
					+ "RECEIVEDACCOUNT='"+pOSAmountByPayMode.getReceivedAccount()+"',"
					+ "POSDIFFAMOUNT='"+pOSAmountByPayMode.getPosDiffAmount()+"',"
					+ "POSDIFFACCOUNT='"+pOSAmountByPayMode.getPosDiffAccount()+"',"
					+ "UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+pOSAmountByPayMode.getID();
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
	
	public String getbymonthpaymode (String plant,String monthyear,String paymode) throws Exception {
	    java.sql.Connection con = null;
	    String amount = "";
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" SELECT ISNULL(SUM(isnull(AMOUNT,0)),0) AS AMOUNT  from ["+ plant +"_"+TABLE_HEADER+"]  where HDRID IN (SELECT ID FROM "
	    		+plant+ "_POSSHIFTAMOUNTHDR WHERE CAST(MONTH(CONVERT(DATETIME,CAST(SUBSTRING(SHIFTDATE, 7, 4) +'-'+SUBSTRING(SHIFTDATE, 4,2) +'-'+"
	    		+ " SUBSTRING(SHIFTDATE, 1,2) AS date))) AS VARCHAR(2)) + '-' + CAST(YEAR(CONVERT(DATETIME,CAST(SUBSTRING(SHIFTDATE, 7, 4)"
	    		+ " +'-'+SUBSTRING(SHIFTDATE, 4,2) +'-'+ SUBSTRING(SHIFTDATE, 1,2) AS date))) AS VARCHAR(4)) = '"+monthyear+"') AND PAYMENTMODE = '"+paymode+"'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        amount = (String) m.get("AMOUNT");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return amount;
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


}
