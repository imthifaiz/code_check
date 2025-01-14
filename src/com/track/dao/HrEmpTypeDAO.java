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
import com.track.db.object.Journal;
import com.track.db.object.JournalAttachment;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class HrEmpTypeDAO {

	public static String TABLE_HEADER = "HREMPTYPE";
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
	
	public int addEmployeetype(HrEmpType employeetype) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ employeetype.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[EMPLOYEETYPE]" + 
					"           ,[EMPLOYEETYPEDESC]" + 
					"           ,[IsActive]" +  
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, employeetype.getPLANT());
				   ps.setString(2, employeetype.getEMPLOYEETYPE());
				   ps.setString(3, employeetype.getEMPLOYEETYPEDESC());
				   ps.setString(4, employeetype.getIsActive());
				   ps.setString(5, DateUtils.getDateTime());
				   ps.setString(6, employeetype.getCRBY());
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
					   throw new SQLException("Creating employee type failed, no rows affected.");
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
	
	public List<HrEmpType> getAllEmployeetype(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrEmpType> HrEmpTypeList=new ArrayList<HrEmpType>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrEmpType hrEmpType=new HrEmpType();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpType);
	                   HrEmpTypeList.add(hrEmpType);
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
		return HrEmpTypeList;
	}
	
	public List<HrEmpType> getAllEmployeetypedropdown(String plant, String emptype) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrEmpType> HrEmpTypeList=new ArrayList<HrEmpType>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE (EMPLOYEETYPE LIKE '%" + emptype + "%') ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrEmpType hrEmpType=new HrEmpType();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpType);
	                   HrEmpTypeList.add(hrEmpType);
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
		return HrEmpTypeList;
	}
	
	public HrEmpType getEmployeetypeById(String plant, int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrEmpType hrEmpType=new HrEmpType();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpType);
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
		return hrEmpType;
	}
	
	public int updateEmployeetype(HrEmpType employeetype, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ employeetype.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+employeetype.getPLANT()+"',EMPLOYEETYPE='"+employeetype.getEMPLOYEETYPE()+"',EMPLOYEETYPEDESC='"+employeetype.getEMPLOYEETYPEDESC()+"',IsActive='"+employeetype.getIsActive()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+employeetype.getID();
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
	
	public boolean DeleteEmployeetype(String plant, int id)
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
	
	public boolean IsEmployeetype(String plant, String emptype)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
	    HrEmpType hrEmpType=new HrEmpType();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE EMPLOYEETYPE='"+emptype+"'";

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
	
	public List<HrEmpType>  IsEmployeetypelist(String plant, String emptype)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrEmpType> HrEmpTypeList=new ArrayList<HrEmpType>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE EMPLOYEETYPE='"+emptype+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrEmpType hrEmpType=new HrEmpType();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpType);
	                   HrEmpTypeList.add(hrEmpType);
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
		return HrEmpTypeList;
	}
	
	public String getEmployeetypeusingId(String plant, int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		String emptype="";
	    String query = "";
	    HrEmpType hrEmpType=new HrEmpType();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpType);
	                    emptype = hrEmpType.getEMPLOYEETYPE();
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
		return emptype;
	}
	
	public int getidbyemployetypee(String plant, String emptype) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    int id=0;
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE EMPLOYEETYPE='"+ emptype+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrEmpType hrEmpType=new HrEmpType();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpType);
	                   id = hrEmpType.getID();
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
		return id;
	}
	
}
