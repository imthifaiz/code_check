package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HrEmpSalaryMst;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class HrEmpSalaryDAO {
	
	public static String TABLE_HEADER = "HREMPSALARYMST";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrEmpSalaryDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrEmpSalaryDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addSalary(HrEmpSalaryMst empSalary) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ empSalary.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[SALARYTYPE]" + 
					"           ,[IsActive]" + 
					"           ,[ISPAYROLL_BY_BASIC_SALARY]" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?)";
			

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, empSalary.getPLANT());
				   ps.setString(2, empSalary.getSALARYTYPE());
				   ps.setString(3, empSalary.getIsActive());
				   ps.setInt(4, empSalary.getISPAYROLL_BY_BASIC_SALARY());
				   ps.setString(5, dateutils.getDate());
				   ps.setString(6, empSalary.getCRBY());
				  
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
					   throw new SQLException("Creating Salary failed, no rows affected.");
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
	
	public List<HrEmpSalaryMst> getAllSalary(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrEmpSalaryMst> HrEmpSalaryList=new ArrayList<HrEmpSalaryMst>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrEmpSalaryMst hrEmpSalary=new HrEmpSalaryMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpSalary);
	                   HrEmpSalaryList.add(hrEmpSalary);
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
		return HrEmpSalaryList;
	}
	
	public HrEmpSalaryMst getSalaryById(String plant, int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrEmpSalaryMst hrEmpSalary=new HrEmpSalaryMst();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpSalary);
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
		return hrEmpSalary;
	}
	
	public int updateSalary(HrEmpSalaryMst empSalary, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ empSalary.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+empSalary.getPLANT()+"',SALARYTYPE='"+empSalary.getSALARYTYPE()+"',IsActive='"+empSalary.getIsActive()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"',ISPAYROLL_BY_BASIC_SALARY='"+empSalary.getISPAYROLL_BY_BASIC_SALARY()+"' WHERE ID="+empSalary.getID();
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
					   throw new SQLException("Updating employee type failed, no rows affected.");
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
	
	public boolean DeleteSalary(String plant, int id)
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

	public boolean IsSalaryExists(String plant, String empSalary)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
	    HrEmpSalaryMst hrEmpSalary=new HrEmpSalaryMst();
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE SALARYTYPE='"+empSalary+"'";

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

	public List<HrEmpSalaryMst> IsSalarylist(String plant, String empSalary)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrEmpSalaryMst> HrEmpSalaryList=new ArrayList<HrEmpSalaryMst>();
	    
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE SALARYTYPE LIKE '%"+empSalary+"%'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrEmpSalaryMst hrEmpType=new HrEmpSalaryMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpType);
	                   HrEmpSalaryList.add(hrEmpType);
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
		return HrEmpSalaryList;
	}
	
	public List<HrEmpSalaryMst> IsSalarylistdropdown(String plant, String empSalary)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrEmpSalaryMst> HrEmpSalaryList=new ArrayList<HrEmpSalaryMst>();
	    
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE IsActive = 'Y' AND SALARYTYPE LIKE '%"+empSalary+"%'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrEmpSalaryMst hrEmpType=new HrEmpSalaryMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpType);
	                   HrEmpSalaryList.add(hrEmpType);
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
		return HrEmpSalaryList;
	}

	    
}

