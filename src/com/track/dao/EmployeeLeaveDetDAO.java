package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.EmployeeLeaveDET;
import com.track.db.object.EmployeeLeaveDETpojo;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrLeaveType;
import com.track.db.object.LeaveTypePojo;
import com.track.gates.DbBean;
import com.track.service.HrEmpTypeService;
import com.track.serviceImplementation.HrEmpTypeServiceImpl;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class EmployeeLeaveDetDAO {
	public static String TABLE_HEADER = "HREMPLOYEELEAVEDET";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.EmployeeLeaveDetDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.EmployeeLeaveDetDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addEmployeeLeavedet(EmployeeLeaveDET EmployeeLeavedet) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ EmployeeLeavedet.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[EMPNOID]" + 
					"           ,[LEAVETYPEID]" +  
					"           ,[TOTALENTITLEMENT]" +
					"           ,[LEAVEBALANCE]" +
					"           ,[LEAVEYEAR]" +
					"           ,[NOTE]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, EmployeeLeavedet.getPLANT());
				   ps.setInt(2, EmployeeLeavedet.getEMPNOID());
				   ps.setInt(3, EmployeeLeavedet.getLEAVETYPEID());
				   ps.setDouble(4, EmployeeLeavedet.getTOTALENTITLEMENT());
				   ps.setDouble(5, EmployeeLeavedet.getLEAVEBALANCE());
				   ps.setString(6, EmployeeLeavedet.getLEAVEYEAR());
				   ps.setString(7, EmployeeLeavedet.getNOTE());
				   ps.setString(8, dateutils.getDate());
				   ps.setString(9, EmployeeLeavedet.getCRBY());
				  
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
					   throw new SQLException("Creating Leave type failed, no rows affected.");
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
	
	public boolean addEmployeeLeavedetboolean(EmployeeLeaveDET EmployeeLeavedet) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ EmployeeLeavedet.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[EMPNOID]" + 
					"           ,[LEAVETYPEID]" +  
					"           ,[TOTALENTITLEMENT]" +
					"           ,[LEAVEBALANCE]" +
					"           ,[LEAVEYEAR]" +
					"           ,[NOTE]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, EmployeeLeavedet.getPLANT());
				   ps.setInt(2, EmployeeLeavedet.getEMPNOID());
				   ps.setInt(3, EmployeeLeavedet.getLEAVETYPEID());
				   ps.setDouble(4, EmployeeLeavedet.getTOTALENTITLEMENT());
				   ps.setDouble(5, EmployeeLeavedet.getLEAVEBALANCE());
				   ps.setString(6, EmployeeLeavedet.getLEAVEYEAR());
				   ps.setString(7, EmployeeLeavedet.getNOTE());
				   ps.setString(8, dateutils.getDate());
				   ps.setString(9, EmployeeLeavedet.getCRBY());
				  
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   flag = true;
				   }
				   else
				   {
					   throw new SQLException("Creating Leave type failed, no rows affected.");
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
	
	public List<EmployeeLeaveDET> getAllEmployeeLeavedet(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<EmployeeLeaveDET> employeeLeaveDETlist=new ArrayList<EmployeeLeaveDET>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   EmployeeLeaveDET employeeLeaveDET=new EmployeeLeaveDET();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, employeeLeaveDET);
	                   employeeLeaveDETlist.add(employeeLeaveDET);
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
		return employeeLeaveDETlist;
	}
	
	public EmployeeLeaveDET getEmployeeLeavedetById(String plant, int id) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    EmployeeLeaveDET employeeLeaveDET=new EmployeeLeaveDET();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, employeeLeaveDET);
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
		return employeeLeaveDET;
	}
	
	
	public int updateEmployeeLeavedet(EmployeeLeaveDET EmployeeLeavedet, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ EmployeeLeavedet.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+EmployeeLeavedet.getPLANT()+"',EMPNOID='"+EmployeeLeavedet.getEMPNOID()+"',"
					+ "LEAVETYPEID='"+EmployeeLeavedet.getLEAVETYPEID()+"',TOTALENTITLEMENT='"+EmployeeLeavedet.getTOTALENTITLEMENT()+"',"
					+ "LEAVEBALANCE='"+EmployeeLeavedet.getLEAVEBALANCE()+"',LEAVEYEAR='"+EmployeeLeavedet.getLEAVEYEAR()+"',"
					+ "NOTE='"+EmployeeLeavedet.getNOTE()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+EmployeeLeavedet.getID();
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
					   throw new SQLException("Updating leave type failed, no rows affected.");
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
	
	public boolean DeleteEmployeeLeavedet(String plant, int id)
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
	
	public boolean IsEmployeeLeavedet(String plant, int leavetypeid, int empnoid, String leaveyear) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE LEAVETYPEID="+leavetypeid+" AND LEAVEYEAR='"+leaveyear+"' AND EMPNOID="+empnoid;

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
	
	public List<EmployeeLeaveDET> IsEmployeeLeavedetlist(String plant, int leavetypeid, int empnoid, String leaveyear) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<EmployeeLeaveDET> EmployeeLeaveDETlist=new ArrayList<EmployeeLeaveDET>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE LEAVETYPEID="+leavetypeid+" AND LEAVEYEAR='"+leaveyear+"' AND EMPNOID="+empnoid;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   EmployeeLeaveDET employeeLeaveDET=new EmployeeLeaveDET();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, employeeLeaveDET);
	                   EmployeeLeaveDETlist.add(employeeLeaveDET);
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
		return EmployeeLeaveDETlist;
	}
	
	public List<EmployeeLeaveDETpojo> EmployeeLeavedetlistpojo(String plant, int empnoid, String leaveyear) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<EmployeeLeaveDETpojo> EmployeeLeaveDETlist=new ArrayList<EmployeeLeaveDETpojo>();
	    HrLeaveTypeDAO hrLeaveTypeDAO = new HrLeaveTypeDAO();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE LEAVEYEAR='"+leaveyear+"' AND EMPNOID="+empnoid;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   EmployeeLeaveDET employeeLeaveDET=new EmployeeLeaveDET();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, employeeLeaveDET);
	                   HrLeaveType leavetypename = hrLeaveTypeDAO.getLeavetypeById(plant, employeeLeaveDET.getLEAVETYPEID());
	                   if(employeeLeaveDET.getLEAVEBALANCE() > 0) {	                  
	                   EmployeeLeaveDETpojo EmployeeLeaveDETpojo = new EmployeeLeaveDETpojo();
	                   EmployeeLeaveDETpojo.setPLANT(employeeLeaveDET.getPLANT());
	                   EmployeeLeaveDETpojo.setID(employeeLeaveDET.getID());
	                   EmployeeLeaveDETpojo.setEMPNOID(employeeLeaveDET.getEMPNOID());
	                   EmployeeLeaveDETpojo.setLEAVETYPE(leavetypename.getLEAVETYPE());
	                   EmployeeLeaveDETpojo.setLEAVETYPEID(employeeLeaveDET.getLEAVETYPEID());
	                   EmployeeLeaveDETpojo.setLEAVEYEAR(employeeLeaveDET.getLEAVEYEAR());
	                   EmployeeLeaveDETpojo.setTOTALENTITLEMENT(employeeLeaveDET.getTOTALENTITLEMENT());
	                   EmployeeLeaveDETpojo.setLEAVEBALANCE(employeeLeaveDET.getLEAVEBALANCE());
	                   EmployeeLeaveDETpojo.setNOTE(employeeLeaveDET.getNOTE());
	                   EmployeeLeaveDETpojo.setLOP(Integer.valueOf(leavetypename.getISNOPAYLEAVE()));
	                   EmployeeLeaveDETlist.add(EmployeeLeaveDETpojo);
	                   }else {
	                	   if(leavetypename.getISNOPAYLEAVE() != null){
	                		   
	                	   if(leavetypename.getISNOPAYLEAVE() == 1) {
	                		   EmployeeLeaveDETpojo EmployeeLeaveDETpojo = new EmployeeLeaveDETpojo();
	    	                   EmployeeLeaveDETpojo.setPLANT(employeeLeaveDET.getPLANT());
	    	                   EmployeeLeaveDETpojo.setID(employeeLeaveDET.getID());
	    	                   EmployeeLeaveDETpojo.setEMPNOID(employeeLeaveDET.getEMPNOID());
	    	                   EmployeeLeaveDETpojo.setLEAVETYPE(leavetypename.getLEAVETYPE());
	    	                   EmployeeLeaveDETpojo.setLEAVETYPEID(employeeLeaveDET.getLEAVETYPEID());
	    	                   EmployeeLeaveDETpojo.setLEAVEYEAR(employeeLeaveDET.getLEAVEYEAR());
	    	                   EmployeeLeaveDETpojo.setTOTALENTITLEMENT(employeeLeaveDET.getTOTALENTITLEMENT());
	    	                   EmployeeLeaveDETpojo.setLEAVEBALANCE(employeeLeaveDET.getLEAVEBALANCE());
	    	                   EmployeeLeaveDETpojo.setNOTE(employeeLeaveDET.getNOTE());
	    	                   EmployeeLeaveDETpojo.setLOP(Integer.valueOf(leavetypename.getISNOPAYLEAVE()));
	    	                   EmployeeLeaveDETlist.add(EmployeeLeaveDETpojo);
	                	   }
	                   }
	                   }
	                   
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
		return EmployeeLeaveDETlist;
	}
	
	public boolean isleavetypeExists(String id,String plant) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();

			// query
			StringBuffer sql = new StringBuffer("SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE LEAVETYPEID ='"+id+"'");
			this.mLogger.query(this.printQuery, sql.toString());
			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;
	}
	
	public boolean isExists(Connection conn, String sql) throws Exception {
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		boolean exists = false;
		try {
			pStmt = conn.prepareStatement(sql);
			rs = pStmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					exists = true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pStmt != null) {
					pStmt.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return exists;
	}
	
	
}
