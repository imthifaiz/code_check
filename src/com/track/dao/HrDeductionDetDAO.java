package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HrDeductionDet;
import com.track.db.object.HrEmpType;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class HrDeductionDetDAO {

	public static String TABLE_HEADER = "HRDEDUCTIONDET";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrDeductionDetDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrDeductionDetDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int adddeductiondet(HrDeductionDet deductiondet) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ deductiondet.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[HDRID]" + 
					"           ,[LNNO]" +  
					"           ,[DUE_AMOUNT]" +
					"           ,[DUE_MONTH]" +
					"           ,[DUE_YEAR]" +
					"           ,[STATUS]" +
					"           ,[CRAT]" +
					"           ,[EMPID]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, deductiondet.getPLANT());
				   ps.setInt(2, deductiondet.getHDRID());
				   ps.setDouble(3, deductiondet.getLNNO());
				   ps.setDouble(4, deductiondet.getDUE_AMOUNT());
				   ps.setString(5, deductiondet.getDUE_MONTH());
				   ps.setString(6, deductiondet.getDUE_YEAR());
				   ps.setString(7, deductiondet.getSTATUS());
				   ps.setString(8, dateutils.getDate());
				   ps.setInt(9, deductiondet.getEMPID());
				   ps.setString(10, deductiondet.getCRBY());
				  
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
					   throw new SQLException("Creating deduction details failed, no rows affected.");
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
	
	public List<HrDeductionDet> getAlldeductiondet(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrDeductionDet> HrDeductionDetList=new ArrayList<HrDeductionDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrDeductionDet hrDeductionDet=new HrDeductionDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrDeductionDet);
	                   HrDeductionDetList.add(hrDeductionDet);
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
		return HrDeductionDetList;
	}
	
	public HrDeductionDet getdeductiondetById(String plant,int id) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrDeductionDet hrDeductionDet=new HrDeductionDet();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrDeductionDet);
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
		return hrDeductionDet;
	}
	
	
	
	public int updatedeductiondet(HrDeductionDet deductiondet,String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ deductiondet.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+deductiondet.getPLANT()+"',HDRID='"+deductiondet.getHDRID()+"',"
					+ "LNNO='"+deductiondet.getLNNO()+"',DUE_AMOUNT='"+deductiondet.getDUE_AMOUNT()+"',EMPID='"+deductiondet.getEMPID()+"',"
					+ "DUE_MONTH='"+deductiondet.getDUE_MONTH()+"',DUE_YEAR='"+deductiondet.getDUE_YEAR()+"',"
					+ "STATUS='"+deductiondet.getSTATUS()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+deductiondet.getID();
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
					   throw new SQLException("Updating deduction details failed, no rows affected.");
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
	
	public boolean Deletedeductiondet(String plant,int id)
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
	
	public List<HrDeductionDet> getdeductiondetbyempidmonthyear(String plant,String empid, String month, String year) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrDeductionDet> HrDeductionDetList=new ArrayList<HrDeductionDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE EMPID ='"+empid+"' AND DUE_MONTH ='"+month+"' AND DUE_YEAR ='"+year+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrDeductionDet hrDeductionDet=new HrDeductionDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrDeductionDet);
	                   HrDeductionDetList.add(hrDeductionDet);
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
		return HrDeductionDetList;
	}
	
	public List<HrDeductionDet> getAlldeductiondetbyhdrid(String plant, int hdrid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrDeductionDet> HrDeductionDetList=new ArrayList<HrDeductionDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE HDRID='"+hdrid+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrDeductionDet hrDeductionDet=new HrDeductionDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrDeductionDet);
	                   HrDeductionDetList.add(hrDeductionDet);
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
		return HrDeductionDetList;
	}
	

	
	public boolean Checkpocess(String plant,int hid)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
	    HrEmpType hrEmpType=new HrEmpType();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"]  WHERE HDRID ='"+hid+"' AND STATUS != 'Pending'";

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
	
}
