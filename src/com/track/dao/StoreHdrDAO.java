package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HrLeaveType;
import com.track.db.object.StoreHdr;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class StoreHdrDAO {


	public static String TABLE_HEADER = "STOREITEMHDR";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.StoreHdrDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.StoreHdrDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int setStoreHdr(StoreHdr storeHdr) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ storeHdr.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[ITEM]" + 
					"           ,[ITEM_DESC]" +  
					"           ,[UOM]" +
					"           ,[ORDER_QTY]" +
					"           ,[PROCESSED_QTY]" +
					"           ,[BALANCE_QTY]" +
					"           ,[DEPARTMENT]" +
					"           ,[STATUS]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, storeHdr.getPLANT());
				   ps.setString(2, storeHdr.getITEM());
				   ps.setString(3, storeHdr.getITEM_DESC());
				   ps.setString(4, storeHdr.getUOM());
				   ps.setDouble(5, storeHdr.getORDER_QTY());
				   ps.setDouble(6, storeHdr.getPROCESSED_QTY());
				   ps.setDouble(7, storeHdr.getBALANCE_QTY());
				   ps.setString(8, storeHdr.getDEPARTMENT());
				   ps.setString(9, storeHdr.getSTATUS());
				   ps.setString(10, dateutils.getDate());
				   ps.setString(11, storeHdr.getCRBY());
				  
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
					   throw new SQLException("Creating store item hdr failed, no rows affected.");
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
	
	public int updateStoreHdr(StoreHdr storeHdr, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ storeHdr.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+storeHdr.getPLANT()+"',ITEM='"+storeHdr.getITEM()+"',"
					+ "ITEM_DESC='"+storeHdr.getITEM_DESC()+"',UOM='"+storeHdr.getUOM()+"',"
					+ "ORDER_QTY='"+storeHdr.getORDER_QTY()+"',PROCESSED_QTY='"+storeHdr.getPROCESSED_QTY()+"',"
					+ "BALANCE_QTY='"+storeHdr.getBALANCE_QTY()+"',DEPARTMENT='"+storeHdr.getDEPARTMENT()+"',"
					+ "STATUS='"+storeHdr.getSTATUS()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+storeHdr.getID();
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
					   throw new SQLException("Updating store item hdr failed, no rows affected.");
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
	
	public boolean checkitem(String item,String lstatus,String plt) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plt +"_"+TABLE_HEADER+"] WHERE ITEM='"+item+"' AND STATUS='"+status+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   status = true;
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
		return status;
	}
	
	public StoreHdr getbyitemandstatus(String item,String status,String plt)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    StoreHdr storeHdr=new StoreHdr();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plt +"_"+TABLE_HEADER+"] WHERE ITEM='"+item+"' AND STATUS='"+status+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, storeHdr);
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
		return storeHdr;
	}
	
	public StoreHdr getbyId(Integer id,String plt)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    StoreHdr storeHdr=new StoreHdr();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plt +"_"+TABLE_HEADER+"] WHERE ID='"+id+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, storeHdr);
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
		return storeHdr;
	}
	
	public List<StoreHdr> getbystatus(String status,String plt) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<StoreHdr> StoreHdreList=new ArrayList<StoreHdr>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plt +"_"+TABLE_HEADER+"] WHERE STATUS='"+status+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   StoreHdr storeHdr=new StoreHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, storeHdr);
	                   StoreHdreList.add(storeHdr);
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
		return StoreHdreList;
	}
	
	public List<StoreHdr> getbydept(String dept,String plt)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<StoreHdr> StoreHdreList=new ArrayList<StoreHdr>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plt +"_"+TABLE_HEADER+"] WHERE DEPARTMENT='"+dept+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   StoreHdr storeHdr=new StoreHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, storeHdr);
	                   StoreHdreList.add(storeHdr);
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
		return StoreHdreList;
	}
	
	public List<StoreHdr> getbydeptandstatus(String dept,String status,String plt)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<StoreHdr> StoreHdreList=new ArrayList<StoreHdr>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plt +"_"+TABLE_HEADER+"] WHERE DEPARTMENT='"+dept+"' AND STATUS='"+status+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   StoreHdr storeHdr=new StoreHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, storeHdr);
	                   StoreHdreList.add(storeHdr);
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
		return StoreHdreList;
	}
	
}
