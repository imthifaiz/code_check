package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HrEmpSalaryDET;
import com.track.db.object.HrEmpSalaryMst;
import com.track.gates.DbBean;
import com.track.service.HrEmpSalaryService;
import com.track.serviceImplementation.HrEmpSalaryServiceImpl;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class HrEmpSalaryDetDAO {
	public static String TABLE_HEADER = "HREMPSALARYDET";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrEmpSalaryDetDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrEmpSalaryDetDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addSalarydet(HrEmpSalaryDET hrEmpSalaryDET) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ hrEmpSalaryDET.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[EMPNOID]" + 
					"           ,[SALARYTYPE]" +  
					"           ,[SALARY]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, hrEmpSalaryDET.getPLANT());
				   ps.setInt(2, hrEmpSalaryDET.getEMPNOID());
				   ps.setString(3, hrEmpSalaryDET.getSALARYTYPE());
				   ps.setDouble(4, hrEmpSalaryDET.getSALARY());
				   ps.setString(5, dateutils.getDate());
				   ps.setString(6, hrEmpSalaryDET.getCRBY());
				  
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
	
	public boolean addSalarydetboolean(HrEmpSalaryDET hrEmpSalaryDET) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ hrEmpSalaryDET.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[EMPNOID]" + 
					"           ,[SALARYTYPE]" +  
					"           ,[SALARY]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, hrEmpSalaryDET.getPLANT());
				   ps.setInt(2, hrEmpSalaryDET.getEMPNOID());
				   ps.setString(3, hrEmpSalaryDET.getSALARYTYPE());
				   ps.setDouble(4, hrEmpSalaryDET.getSALARY());
				   ps.setString(5, dateutils.getDate());
				   ps.setString(6, hrEmpSalaryDET.getCRBY());
				  
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   flag = true;
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
		return flag;
	}
	
	public List<HrEmpSalaryDET> getAllEmpSalarydet(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrEmpSalaryDET> hrEmpSalaryDETlist=new ArrayList<HrEmpSalaryDET>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrEmpSalaryDET hrEmpSalaryDET=new HrEmpSalaryDET();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpSalaryDET);
	                   hrEmpSalaryDETlist.add(hrEmpSalaryDET);
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
		return hrEmpSalaryDETlist;
	}
	
	public HrEmpSalaryDET getEmpSalarydetById(String plant, int id) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrEmpSalaryDET hrEmpSalaryDET=new HrEmpSalaryDET();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpSalaryDET);
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
		return hrEmpSalaryDET;
	}
	
	
	public int updateSalarydet(HrEmpSalaryDET hrEmpSalaryDET, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ hrEmpSalaryDET.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+hrEmpSalaryDET.getPLANT()+"',EMPNOID='"+hrEmpSalaryDET.getEMPNOID()+"',"
					+ "LEAVETYPEID='"+hrEmpSalaryDET.getSALARYTYPE()+"',TOTALENTITLEMENT='"+hrEmpSalaryDET.getSALARY()+"',"
					+"UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+hrEmpSalaryDET.getID();
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
					   throw new SQLException("Updating Salary failed, no rows affected.");
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
	
	public boolean DeleteSalarydet(String plant, int id)
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
	
	public boolean IsEmpSalarydet(String plant, String salarytype, int empnoid) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE SALARYTYPE='"+salarytype+"' AND EMPNOID="+empnoid;

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
	
	public List<HrEmpSalaryDET> IsEmpSalarydetlist(String plant, String salarytype, int empnoid) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrEmpSalaryDET> hrEmpSalaryDETlist=new ArrayList<HrEmpSalaryDET>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE SALARYTYPE LIKE '%"+salarytype+"%' AND EMPNOID="+empnoid;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrEmpSalaryDET hrEmpSalaryDET=new HrEmpSalaryDET();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpSalaryDET);
	                   hrEmpSalaryDETlist.add(hrEmpSalaryDET);
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
		return hrEmpSalaryDETlist;
	}
	
	public boolean DeleteEmpSalarydet(String plant, int empid)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant +"_"+TABLE_HEADER+"]"
			                        + " WHERE EMPNOID ='"+empid+"'";
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
	
	public List<HrEmpSalaryDET> EmpSalarydetlistbyempid(String plant, int empnoid) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrEmpSalaryDET> hrEmpSalaryDETlist=new ArrayList<HrEmpSalaryDET>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE EMPNOID="+empnoid;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrEmpSalaryDET hrEmpSalaryDET=new HrEmpSalaryDET();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpSalaryDET);
	                   hrEmpSalaryDETlist.add(hrEmpSalaryDET);
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
		return hrEmpSalaryDETlist;
	}
	
}
