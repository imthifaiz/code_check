package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrLeaveType;
import com.track.db.object.HrPayrollAddition;
import com.track.db.object.LeaveTypePojo;
import com.track.gates.DbBean;
import com.track.service.HrEmpTypeService;
import com.track.serviceImplementation.HrEmpTypeServiceImpl;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class HrPayrollAdditionDAO {

	public static String TABLE_HEADER = "HRPAYROLLADDITION";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrPayrollAdditionDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrPayrollAdditionDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addpayrolladdition(HrPayrollAddition payrolladdition) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ payrolladdition.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[EMPID]" + 
					"           ,[ADDITION_NAME]" +  
					"           ,[ADDITION_AMOUNT]" +
					"           ,[ADDITION_DATE]" +
					"           ,[MONTH]" +
					"           ,[YEAR]" +
					"           ,[STATUS]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, payrolladdition.getPLANT());
				   ps.setInt(2, payrolladdition.getEMPID());
				   ps.setString(3, payrolladdition.getADDITION_NAME());
				   ps.setDouble(4, payrolladdition.getADDITION_AMOUNT());
				   ps.setString(5, payrolladdition.getADDITION_DATE());
				   ps.setString(6, payrolladdition.getMONTH());
				   ps.setString(7, payrolladdition.getYEAR());
				   ps.setString(8, payrolladdition.getSTATUS());
				   ps.setString(9, dateutils.getDate());
				   ps.setString(10, payrolladdition.getCRBY());
				  
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
					   throw new SQLException("Creating payroll addition failed, no rows affected.");
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
	
	public List<HrPayrollAddition> getAllpayrolladdition(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollAddition> HrPayrollAdditionList=new ArrayList<HrPayrollAddition>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollAddition hrPayrollAddition=new HrPayrollAddition();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollAddition);
	                   HrPayrollAdditionList.add(hrPayrollAddition);
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
		return HrPayrollAdditionList;
	}
	
	public HrPayrollAddition getpayrolladditionById(String plant,int id) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrPayrollAddition hrPayrollAddition=new HrPayrollAddition();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollAddition);
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
		return hrPayrollAddition;
	}
	
	
	public int updatepayrolladdition(HrPayrollAddition payrolladdition,String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ payrolladdition.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+payrolladdition.getPLANT()+"',EMPID='"+payrolladdition.getEMPID()+"',"
					+ "ADDITION_NAME='"+payrolladdition.getADDITION_NAME()+"',ADDITION_AMOUNT='"+payrolladdition.getADDITION_AMOUNT()+"',"
					+ "ADDITION_DATE='"+payrolladdition.getADDITION_DATE()+"',MONTH='"+payrolladdition.getMONTH()+"',YEAR='"+payrolladdition.getYEAR()+"',"
					+ "STATUS='"+payrolladdition.getSTATUS()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+payrolladdition.getID();
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
					   throw new SQLException("Updating payroll addition failed, no rows affected.");
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
	
	public boolean Deletepayrolladdition(String plant,int id)
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
	
}
