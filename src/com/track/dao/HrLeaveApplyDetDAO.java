package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.BlockingDates;
import com.track.db.object.HrLeaveApplyDet;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class HrLeaveApplyDetDAO {
	public static String TABLE_HEADER = "HRLEAVEAPPLYDET";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrLeaveApplyDetDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrLeaveApplyDetDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addHrLeaveApplyDet(HrLeaveApplyDet HrLeaveApplyDet) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ HrLeaveApplyDet.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[EMPNOID]" + 
					"           ,[LEAVEHDRID]" +  
					"           ,[LEAVE_DATE]" +
					"           ,[PREPOSTLUNCH]" +
					"           ,[PREPOSTLUNCHTYPE]" +
					"           ,[STATUS]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, HrLeaveApplyDet.getPLANT());
				   ps.setInt(2, HrLeaveApplyDet.getEMPNOID());
				   ps.setInt(3, HrLeaveApplyDet.getLEAVEHDRID());
				   ps.setString(4, HrLeaveApplyDet.getLEAVE_DATE());
				   ps.setString(5, HrLeaveApplyDet.getPREPOSTLUNCH());
				   ps.setString(6, HrLeaveApplyDet.getPREPOSTLUNCHTYPE());
				   ps.setString(7, HrLeaveApplyDet.getSTATUS());
				   ps.setString(8, dateutils.getDate());
				   ps.setString(9, HrLeaveApplyDet.getCRBY());
				  
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
					   throw new SQLException("Creating Leave apply Det failed, no rows affected.");
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
	
	public List<HrLeaveApplyDet> getAllHrLeaveApplyDet(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrLeaveApplyDet> HrLeaveApplyDetlist=new ArrayList<HrLeaveApplyDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveApplyDet hrLeaveApplyDet=new HrLeaveApplyDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyDet);
	                   HrLeaveApplyDetlist.add(hrLeaveApplyDet);
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
		return HrLeaveApplyDetlist;
	}
	
	public HrLeaveApplyDet getHrLeaveApplyDetById(String plant, int id) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrLeaveApplyDet hrLeaveApplyDet=new HrLeaveApplyDet();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyDet);
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
		return hrLeaveApplyDet;
	}
	
	
	public int updateHrLeaveApplyDet(HrLeaveApplyDet HrLeaveApplyDet, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ HrLeaveApplyDet.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+HrLeaveApplyDet.getPLANT()+"',EMPNOID='"+HrLeaveApplyDet.getEMPNOID()+"',"
					+ "LEAVEHDRID='"+HrLeaveApplyDet.getLEAVEHDRID()+"',LEAVE_DATE='"+HrLeaveApplyDet.getLEAVE_DATE()+"',"
					+ "PREPOSTLUNCH='"+HrLeaveApplyDet.getPREPOSTLUNCH()+"',PREPOSTLUNCHTYPE='"+HrLeaveApplyDet.getPREPOSTLUNCHTYPE()+"',"
					+ "STATUS='"+HrLeaveApplyDet.getSTATUS()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+HrLeaveApplyDet.getID();
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
					   throw new SQLException("Updating leave type det failed, no rows affected.");
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
	
	public boolean DeleteHrLeaveApplyDet(String plant, int id) 
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant +"_"+TABLE_HEADER+"]"
			                        + " WHERE ID ='"+id+"'";
			        this.mLogger.query(this.printQuery, sQry);
			        ps = con.prepareStatement(sQry);
			        int iCnt = ps.executeUpdate();
			        if (iCnt > 0) {
			        	deletestatus = true;
			        }
			} catch (Exception e) {
			        this.mLogger.exception(this.printLog, "", e);
			} finally {
			        DbBean.closeConnection(con, ps);
			}
			
			return deletestatus;
 	}
	
	public List<HrLeaveApplyDet> getHrLeaveApplyDetbyEmpid(String plant, int empnoid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrLeaveApplyDet> HrLeaveApplyDetlist=new ArrayList<HrLeaveApplyDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND EMPNOID ="+empnoid;
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveApplyDet hrLeaveApplyDet=new HrLeaveApplyDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyDet);
	                   HrLeaveApplyDetlist.add(hrLeaveApplyDet);
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
		return HrLeaveApplyDetlist;
	}
	
	public List<HrLeaveApplyDet> getHrLeaveApplyDetbyhdrid(String plant, int hdrid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrLeaveApplyDet> HrLeaveApplyDetlist=new ArrayList<HrLeaveApplyDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND LEAVEHDRID ="+hdrid;
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveApplyDet hrLeaveApplyDet=new HrLeaveApplyDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyDet);
	                   HrLeaveApplyDetlist.add(hrLeaveApplyDet);
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
		return HrLeaveApplyDetlist;
	}
	
	public List<HrLeaveApplyDet> getHrLeaveApplyDetbyEmpidAndDate(String plant, int empnoid, String fromdate, String todate) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrLeaveApplyDet> HrLeaveApplyDetlist=new ArrayList<HrLeaveApplyDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND EMPNOID ='"+empnoid+"' AND LEAVE_DATE BETWEEN '"+fromdate+"' AND '"+todate+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveApplyDet hrLeaveApplyDet=new HrLeaveApplyDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyDet);
	                   HrLeaveApplyDetlist.add(hrLeaveApplyDet);
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
		return HrLeaveApplyDetlist;
	}
	
	public List<HrLeaveApplyDet> getHrLeaveApplyDetbyEmpidfullday(String plant, int empnoid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrLeaveApplyDet> HrLeaveApplyDetlist=new ArrayList<HrLeaveApplyDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND EMPNOID ="+empnoid+" AND PREPOSTLUNCHTYPE = 'Fullday' AND (STATUS = 'Pending' OR STATUS = 'Approved')";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveApplyDet hrLeaveApplyDet=new HrLeaveApplyDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyDet);
	                   HrLeaveApplyDetlist.add(hrLeaveApplyDet);
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
		return HrLeaveApplyDetlist;
	}
	
	public List<HrLeaveApplyDet> getHrLeaveApplyDetbyEmpiddate(String plant, int empnoid, String leavedate) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrLeaveApplyDet> HrLeaveApplyDetlist=new ArrayList<HrLeaveApplyDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND EMPNOID ="+empnoid+" AND LEAVE_DATE ='"+leavedate+"' AND (STATUS = 'Pending' OR STATUS = 'Approved')";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveApplyDet hrLeaveApplyDet=new HrLeaveApplyDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyDet);
	                   HrLeaveApplyDetlist.add(hrLeaveApplyDet);
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
		return HrLeaveApplyDetlist;
	}
	
	public List<BlockingDates> getHrLeaveApplyDetbyEmpidTwohalfdays(String plant, int empnoid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<BlockingDates> BlockingDatesList=new ArrayList<BlockingDates>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			//query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND EMPNOID ="+empnoid+" AND PREPOSTLUNCHTYPE = 'Fullday'";
			query = "SELECT LEAVE_DATE FROM "+"["+plant+"_HRLEAVEAPPLYDET] WHERE PLANT='"+plant+"' AND (STATUS = 'Pending' OR STATUS = 'Approved') AND EMPNOID ="+empnoid+" GROUP BY LEAVE_DATE HAVING COUNT(LEAVE_DATE) > 1";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   BlockingDates BlockingDates = new BlockingDates();
					   BlockingDates.setBLOCKING_DATE(rst.getString(1));
					   BlockingDatesList.add(BlockingDates);
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
		return BlockingDatesList;
	}
	
	public List<HrLeaveApplyDet> getHrLeaveApplyDetbyEmpidAndDateApproved(String plant, int empnoid, String fromdate, String todate) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrLeaveApplyDet> HrLeaveApplyDetlist=new ArrayList<HrLeaveApplyDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			/*query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND EMPNOID ='"+empnoid+"' AND STATUS = 'Approved' AND LEAVE_DATE BETWEEN '"+fromdate+"' AND '"+todate+"'";*/
			
			query = " SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND EMPNOID ='"+empnoid+"' AND STATUS = 'Approved' AND (SUBSTRING(LEAVE_DATE,7,4) +SUBSTRING(LEAVE_DATE,4,2)+SUBSTRING(LEAVE_DATE,1,2)) >= (SUBSTRING('"+fromdate+"',7,4) +SUBSTRING('"+fromdate+"',4,2)+SUBSTRING('"+fromdate+"',1,2)) AND (SUBSTRING(LEAVE_DATE,7,4) +SUBSTRING(LEAVE_DATE,4,2)+SUBSTRING(LEAVE_DATE,1,2)) <= (SUBSTRING('"+todate+"',7,4) +SUBSTRING('"+todate+"',4,2)+SUBSTRING('"+todate+"',1,2))";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveApplyDet hrLeaveApplyDet=new HrLeaveApplyDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyDet);
	                   HrLeaveApplyDetlist.add(hrLeaveApplyDet);
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
		return HrLeaveApplyDetlist;
	}

}
