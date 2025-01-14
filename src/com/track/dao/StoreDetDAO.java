package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.StoreDet;
import com.track.db.object.StoreHdr;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class StoreDetDAO {
	
	public static String TABLE_HEADER = "STOREITEMDET";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.StoreDetDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.StoreDetDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int setStoreDet(StoreDet storeDet) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ storeDet.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[HDRID]" + 
					"           ,[DONO]" +  
					"           ,[IS_CHILD_ITEM]" +
					"           ,[PITEM]" +
					"           ,[PITEM_DESC]" +
					"           ,[ITEM]" +
					"           ,[ITEM_DESC]" +
					"           ,[UOM]" +
					"           ,[ORDER_QTY]" +
					"           ,[DEPARTMENT]" +
					"           ,[STATUS]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, storeDet.getPLANT());
				   ps.setInt(2, storeDet.getHDRID());
				   ps.setString(3, storeDet.getDONO());
				   ps.setShort(4, storeDet.getIS_CHILD_ITEM());
				   ps.setString(5, storeDet.getPITEM());
				   ps.setString(6, storeDet.getPITEM_DESC());
				   ps.setString(7, storeDet.getITEM());
				   ps.setString(8, storeDet.getITEM_DESC());
				   ps.setString(9, storeDet.getUOM());
				   ps.setDouble(10, storeDet.getORDER_QTY());
				   ps.setString(11, storeDet.getDEPARTMENT());
				   ps.setString(12, storeDet.getSTATUS());
				   ps.setString(13, dateutils.getDate());
				   ps.setString(14, storeDet.getCRBY());
				  
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
	
	public int updateStoreDet(StoreDet storeDet, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ storeDet.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+storeDet.getPLANT()+"',"
					+ "HDRID='"+storeDet.getHDRID()+"',"
					+ "DONO='"+storeDet.getDONO()+"',"
					+ "IS_CHILD_ITEM='"+storeDet.getIS_CHILD_ITEM()+"',"
					+ "PITEM='"+storeDet.getPITEM()+"',"
					+ "PITEM_DESC='"+storeDet.getPITEM_DESC()+"',"	
					+ "ITEM='"+storeDet.getITEM()+"',"
					+ "ITEM_DESC='"+storeDet.getITEM_DESC()+"',"
					+ "UOM='"+storeDet.getUOM()+"',"
					+ "ORDER_QTY='"+storeDet.getORDER_QTY()+"',"
					+ "DEPARTMENT='"+storeDet.getDEPARTMENT()+"',"
					+ "STATUS='"+storeDet.getSTATUS()+"',"
					+ "UPAT='"+dateutils.getDateTime()+"',"
					+ "UPBY='"+user+"' "
					+ "WHERE ID="+storeDet.getID();
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
	
	public List<StoreDet> getbyHdrid(int hdrid,String plt) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<StoreDet> StoreDetList=new ArrayList<StoreDet>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plt +"_"+TABLE_HEADER+"] WHERE HDRID='"+hdrid+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   StoreDet storeDet=new StoreDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, storeDet);
	                   StoreDetList.add(storeDet);
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
		return StoreDetList;
	}

}
