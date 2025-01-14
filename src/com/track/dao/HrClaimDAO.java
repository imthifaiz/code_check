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
import com.track.db.object.HrClaim;
import com.track.db.object.HrClaimPojo;
import com.track.db.object.HrClaimAttachment;
import com.track.db.object.HrLeaveApplyDet;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class HrClaimDAO {

	public static String TABLE_HEADER = "HRCLAIM";
	public static String TABLE_ATTACHMENT = "HRCLAIMATTACHMENTS";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrClaimDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrClaimDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addHrClaim(HrClaim hrClaim) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ hrClaim.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[EMPNOID]" + 
					"           ,[CLIAMID]" +  
					"           ,[DESCRIPTION]" +  
					"           ,[CLAIMDATE]" +
					"           ,[FROM_PLACE]" +
					"           ,[TO_PLACE]" +
					"           ,[DISTANCE]" +
					"           ,[AMOUNT]" +
					"           ,[STATUS]" +
					"           ,[REASON]" +
					"           ,[REPORT_INCHARGE_ID]" +
					"           ,[CRAT]" + 
					"           ,[CLKEY]" +  
					"           ,[UPAT]" +
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, hrClaim.getPLANT());
				   ps.setInt(2, hrClaim.getEMPNOID());
				   ps.setInt(3, hrClaim.getCLAIMID());
				   ps.setString(4, hrClaim.getDESCRIPTION());
				   ps.setString(5, hrClaim.getCLAIMDATE());
				   ps.setString(6, hrClaim.getFROM_PLACE());
				   ps.setString(7, hrClaim.getTO_PLACE());
				   ps.setDouble(8, hrClaim.getDISTANCE());
				   ps.setDouble(9, hrClaim.getAMOUNT());
				   ps.setString(10, hrClaim.getSTATUS());
				   ps.setString(11, hrClaim.getREASON());
				   ps.setInt(12, hrClaim.getREPORT_INCHARGE_ID());
				   ps.setString(13, dateutils.getDate());
				   ps.setString(14, hrClaim.getCLKEY());
				   ps.setString(15, dateutils.getDateTime());
				   ps.setString(16, hrClaim.getCRBY());
				  
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
					   throw new SQLException("Creating claim failed, no rows affected.");
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
	
	public List<HrClaim> getAllHrClaim(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrClaim> HrClaimlist=new ArrayList<HrClaim>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrClaim hrClaim=new HrClaim();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrClaim);
	                   HrClaimlist.add(hrClaim);
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
		return HrClaimlist;
	}
	
	public HrClaim getHrClaimById(String plant, int id) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrClaim hrClaim=new HrClaim();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrClaim);
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
		return hrClaim;
	}
	
	
	public int updateHrClaim (HrClaim hrClaim, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ hrClaim.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+hrClaim.getPLANT()+"',EMPNOID='"+hrClaim.getEMPNOID()+"',CLIAMID='"+hrClaim.getCLAIMID()+"',"
					+ "DESCRIPTION='"+hrClaim.getDESCRIPTION()+"',CLAIMDATE='"+hrClaim.getCLAIMDATE()+"',REASON='"+hrClaim.getREASON()+"',FROM_PLACE='"+hrClaim.getFROM_PLACE()+"',TO_PLACE='"+hrClaim.getTO_PLACE()+"',"
					+ "DISTANCE='"+hrClaim.getDISTANCE()+"',AMOUNT='"+hrClaim.getAMOUNT()+"',CLKEY='"+hrClaim.getCLKEY()+"',STATUS='"+hrClaim.getSTATUS()+"',REPORT_INCHARGE_ID='"+hrClaim.getREPORT_INCHARGE_ID()+"',"
					+ "UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+hrClaim.getID();
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
					   throw new SQLException("Updating claim failed, no rows affected.");
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
	
	public boolean DeleteHrClaim(String plant, int id) 
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
	
	public List<HrClaim> getHrClaimbyEmpid(String plant, int empnoid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrClaim> HrClaimlist=new ArrayList<HrClaim>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND EMPNOID ="+empnoid;
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrClaim hrClaim=new HrClaim();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrClaim);
	                   HrClaimlist.add(hrClaim);
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
		return HrClaimlist;
	}
	
	
	public List<HrClaimPojo> getHrClaimPojoObyEmpid(String plant, int empnoid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrClaimPojo> HrClaimPojolist=new ArrayList<HrClaimPojo>();
	    EmployeeDAO employeeDAO = new EmployeeDAO();
	    HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT PLANT,ID,EMPNOID,CLKEY,CLIAMID,DESCRIPTION,CLAIMDATE,ISNULL(REASON,'') AS REASON,FROM_PLACE,TO_PLACE,DISTANCE,AMOUNT,STATUS,REPORT_INCHARGE_ID,CRAT,CRBY,UPAT,UPBY FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND EMPNOID ="+empnoid+"ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrClaim HrClaim=new HrClaim();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, HrClaim);
	                   String empname = employeeDAO.getEmpnamebyid(HrClaim.getPLANT(),String.valueOf( HrClaim.getEMPNOID()), "");
	                   String cname = hrPayrollAdditionMstDAO.getclsimnamebyid(HrClaim.getPLANT(), String.valueOf( HrClaim.getCLAIMID()), "");
	                   HrClaimPojo hrClaimPojo = new HrClaimPojo();
	                   hrClaimPojo.setPLANT(HrClaim.getPLANT());
	                   hrClaimPojo.setID(HrClaim.getID());
	                   hrClaimPojo.setEMPNOID(HrClaim.getEMPNOID());
	                   hrClaimPojo.setEMPNAME(empname);
	                   hrClaimPojo.setCLAIMID(HrClaim.getCLAIMID());
	                   hrClaimPojo.setCLAIMNAME(cname);
	                   hrClaimPojo.setCLAIMDATE(HrClaim.getCLAIMDATE());
	                   hrClaimPojo.setFROM_PLACE(HrClaim.getFROM_PLACE());
	                   hrClaimPojo.setTO_PLACE(HrClaim.getTO_PLACE());
	                   hrClaimPojo.setDISTANCE(HrClaim.getDISTANCE());
	                   hrClaimPojo.setAMOUNT(HrClaim.getAMOUNT());
	                   hrClaimPojo.setSTATUS(HrClaim.getSTATUS());
	                   hrClaimPojo.setREASON(HrClaim.getREASON());
	                   hrClaimPojo.setREPORT_INCHARGE_ID(HrClaim.getREPORT_INCHARGE_ID());
	                   hrClaimPojo.setDESCRIPTION(HrClaim.getDESCRIPTION());
	                   hrClaimPojo.setCLKEY(HrClaim.getCLKEY());
	                   if(HrClaim.getUPAT() != null) {
		                   if(HrClaim.getUPAT().length() > 0) {
		                	   hrClaimPojo.setSTATUSDATE(dateutils.getDB2UserDate_New(HrClaim.getUPAT()));
		                   }else {
		                	   hrClaimPojo.setSTATUSDATE("");
		                   }
	                   }else {
	                	   hrClaimPojo.setSTATUSDATE("");
	                   }   
	                   HrClaimPojolist.add(hrClaimPojo);
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
		return HrClaimPojolist;
	}
	
	public List<HrClaimPojo> getHrClaimPojobyRepid(String plant, int repid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrClaimPojo> HrClaimPojolist=new ArrayList<HrClaimPojo>();
	    EmployeeDAO employeeDAO = new EmployeeDAO();
	    HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND REPORT_INCHARGE_ID ="+repid+"ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrClaim HrClaim=new HrClaim();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, HrClaim);
	                   String empname = employeeDAO.getEmpnamebyid(HrClaim.getPLANT(),String.valueOf(HrClaim.getEMPNOID()), "");
	                   String cname = hrPayrollAdditionMstDAO.getclsimnamebyid(HrClaim.getPLANT(), String.valueOf(HrClaim.getCLAIMID()), "");
	                   HrClaimPojo hrClaimPojo = new HrClaimPojo();
	                   hrClaimPojo.setPLANT(HrClaim.getPLANT());
	                   hrClaimPojo.setID(HrClaim.getID());
	                   hrClaimPojo.setEMPNOID(HrClaim.getEMPNOID());
	                   hrClaimPojo.setEMPNAME(empname);
	                   hrClaimPojo.setCLAIMID(HrClaim.getCLAIMID());
	                   hrClaimPojo.setCLAIMNAME(cname);
	                   hrClaimPojo.setCLAIMDATE(HrClaim.getCLAIMDATE());
	                   hrClaimPojo.setFROM_PLACE(HrClaim.getFROM_PLACE());
	                   hrClaimPojo.setTO_PLACE(HrClaim.getTO_PLACE());
	                   hrClaimPojo.setDISTANCE(HrClaim.getDISTANCE());
	                   hrClaimPojo.setAMOUNT(HrClaim.getAMOUNT());
	                   hrClaimPojo.setSTATUS(HrClaim.getSTATUS());
	                   hrClaimPojo.setREASON(HrClaim.getREASON());
	                   hrClaimPojo.setREPORT_INCHARGE_ID(HrClaim.getREPORT_INCHARGE_ID());
	                   hrClaimPojo.setDESCRIPTION(HrClaim.getDESCRIPTION());
	                   hrClaimPojo.setCLKEY(HrClaim.getCLKEY());
	                   if(HrClaim.getUPAT() != null) {
		                   if(HrClaim.getUPAT().length() > 0) {
		                	   hrClaimPojo.setSTATUSDATE(dateutils.getDB2UserDate_New(HrClaim.getUPAT()));
		                   }else {
		                	   hrClaimPojo.setSTATUSDATE("");
		                   }
	                   }else {
	                	   hrClaimPojo.setSTATUSDATE("");
	                   }  
	                   HrClaimPojolist.add(hrClaimPojo);
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
		return HrClaimPojolist;
	}
	
	public List<HrClaimPojo> getAllHrClaimPojo(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrClaimPojo> HrClaimPojolist=new ArrayList<HrClaimPojo>();
	    EmployeeDAO employeeDAO = new EmployeeDAO();
	    HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT PLANT,ID,EMPNOID,CLKEY,CLIAMID,DESCRIPTION,CLAIMDATE,ISNULL(REASON,'') AS REASON,FROM_PLACE,TO_PLACE,DISTANCE,AMOUNT,STATUS,REPORT_INCHARGE_ID,CRAT,CRBY,UPAT,UPBY FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrClaim HrClaim=new HrClaim();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, HrClaim);
	                   String empname = employeeDAO.getEmpnamebyid(HrClaim.getPLANT(),String.valueOf(HrClaim.getEMPNOID()), "");
	                   String empcode = employeeDAO.getEmpcode(HrClaim.getPLANT(),String.valueOf(HrClaim.getEMPNOID()), "");
	                   String cname = hrPayrollAdditionMstDAO.getclsimnamebyid(HrClaim.getPLANT(), String.valueOf(HrClaim.getCLAIMID()), "");
	                   HrClaimPojo hrClaimPojo = new HrClaimPojo();
	                   hrClaimPojo.setPLANT(HrClaim.getPLANT());
	                   hrClaimPojo.setID(HrClaim.getID());
	                   hrClaimPojo.setEMPNOID(HrClaim.getEMPNOID());
	                   hrClaimPojo.setEMPCODE(empcode);
	                   hrClaimPojo.setEMPNAME(empname);
	                   hrClaimPojo.setCLAIMID(HrClaim.getCLAIMID());
	                   hrClaimPojo.setCLAIMNAME(cname);
	                   hrClaimPojo.setCLAIMDATE(HrClaim.getCLAIMDATE());
	                   hrClaimPojo.setFROM_PLACE(HrClaim.getFROM_PLACE());
	                   hrClaimPojo.setTO_PLACE(HrClaim.getTO_PLACE());
	                   hrClaimPojo.setDISTANCE(HrClaim.getDISTANCE());
	                   hrClaimPojo.setAMOUNT(HrClaim.getAMOUNT());
	                   hrClaimPojo.setSTATUS(HrClaim.getSTATUS());
	                   hrClaimPojo.setCLKEY(HrClaim.getCLKEY());
	                   if(HrClaim.getREASON() == null || HrClaim.getREASON().equalsIgnoreCase("null")) {
	                	   hrClaimPojo.setREASON("");
	                   }else {
	                	   hrClaimPojo.setREASON(HrClaim.getREASON());
	                   }
	                   
	                   hrClaimPojo.setREPORT_INCHARGE_ID(HrClaim.getREPORT_INCHARGE_ID());
	                   hrClaimPojo.setDESCRIPTION(HrClaim.getDESCRIPTION());
	                   HrClaimPojolist.add(hrClaimPojo);
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
		return HrClaimPojolist;
	}
	
	public List<HrClaimPojo> getHrClaimPojoProcess(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrClaimPojo> HrClaimPojolist=new ArrayList<HrClaimPojo>();
	    EmployeeDAO employeeDAO = new EmployeeDAO();
	    HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT PLANT,ID,EMPNOID,CLKEY,CLIAMID,DESCRIPTION,CLAIMDATE,ISNULL(REASON,'') AS REASON,FROM_PLACE,TO_PLACE,DISTANCE,AMOUNT,STATUS,REPORT_INCHARGE_ID,CRAT,CRBY,UPAT,UPBY FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND STATUS = 'Approved' ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrClaim HrClaim=new HrClaim();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, HrClaim);
	                   String empname = employeeDAO.getEmpnamebyid(HrClaim.getPLANT(),String.valueOf(HrClaim.getEMPNOID()), "");
	                   String empcode = employeeDAO.getEmpcode(HrClaim.getPLANT(),String.valueOf(HrClaim.getEMPNOID()), "");
	                   String cname = hrPayrollAdditionMstDAO.getclsimnamebyid(HrClaim.getPLANT(), String.valueOf(HrClaim.getCLAIMID()), "");
	                   HrClaimPojo hrClaimPojo = new HrClaimPojo();
	                   hrClaimPojo.setPLANT(HrClaim.getPLANT());
	                   hrClaimPojo.setID(HrClaim.getID());
	                   hrClaimPojo.setEMPNOID(HrClaim.getEMPNOID());
	                   hrClaimPojo.setEMPCODE(empcode);
	                   hrClaimPojo.setEMPNAME(empname);
	                   hrClaimPojo.setCLAIMID(HrClaim.getCLAIMID());
	                   hrClaimPojo.setCLAIMNAME(cname);
	                   hrClaimPojo.setCLAIMDATE(HrClaim.getCLAIMDATE());
	                   hrClaimPojo.setFROM_PLACE(HrClaim.getFROM_PLACE());
	                   hrClaimPojo.setTO_PLACE(HrClaim.getTO_PLACE());
	                   hrClaimPojo.setDISTANCE(HrClaim.getDISTANCE());
	                   hrClaimPojo.setAMOUNT(HrClaim.getAMOUNT());
	                   hrClaimPojo.setSTATUS(HrClaim.getSTATUS());
	                   hrClaimPojo.setREASON(HrClaim.getREASON());
	                   hrClaimPojo.setREPORT_INCHARGE_ID(HrClaim.getREPORT_INCHARGE_ID());
	                   hrClaimPojo.setDESCRIPTION(HrClaim.getDESCRIPTION());
	                   hrClaimPojo.setCLKEY(HrClaim.getCLKEY());
	                   HrClaimPojolist.add(hrClaimPojo);
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
		return HrClaimPojolist;
	}
	
	public HrClaimPojo getAllHrClaimPojobyid(String plant, int id) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrClaimPojo hrClaimPojo = new HrClaimPojo();
	    EmployeeDAO employeeDAO = new EmployeeDAO();
	    HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT PLANT,ID,EMPNOID,CLKEY,CLIAMID,DESCRIPTION,CLAIMDATE,ISNULL(REASON,'') AS REASON,FROM_PLACE,TO_PLACE,DISTANCE,AMOUNT,STATUS,REPORT_INCHARGE_ID,CRAT,CRBY,UPAT,UPBY FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND ID="+id;
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrClaim HrClaim=new HrClaim();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, HrClaim);
	                   String empname = employeeDAO.getEmpnamebyid(HrClaim.getPLANT(),String.valueOf(HrClaim.getEMPNOID()), "");
	                   String empcode = employeeDAO.getEmpcode(HrClaim.getPLANT(),String.valueOf(HrClaim.getEMPNOID()), "");
	                   String cname = hrPayrollAdditionMstDAO.getclsimnamebyid(HrClaim.getPLANT(), String.valueOf(HrClaim.getCLAIMID()), "");
	                  
	                   hrClaimPojo.setPLANT(HrClaim.getPLANT());
	                   hrClaimPojo.setID(HrClaim.getID());
	                   hrClaimPojo.setEMPNOID(HrClaim.getEMPNOID());
	                   hrClaimPojo.setEMPCODE(empcode);
	                   hrClaimPojo.setEMPNAME(empname);
	                   hrClaimPojo.setCLAIMID(HrClaim.getCLAIMID());
	                   hrClaimPojo.setCLAIMNAME(cname);
	                   hrClaimPojo.setCLAIMDATE(HrClaim.getCLAIMDATE());
	                   hrClaimPojo.setFROM_PLACE(HrClaim.getFROM_PLACE());
	                   hrClaimPojo.setTO_PLACE(HrClaim.getTO_PLACE());
	                   hrClaimPojo.setDISTANCE(HrClaim.getDISTANCE());
	                   hrClaimPojo.setAMOUNT(HrClaim.getAMOUNT());
	                   hrClaimPojo.setSTATUS(HrClaim.getSTATUS());
	                   hrClaimPojo.setREASON(HrClaim.getREASON());
	                   hrClaimPojo.setREPORT_INCHARGE_ID(HrClaim.getREPORT_INCHARGE_ID());
	                   hrClaimPojo.setDESCRIPTION(HrClaim.getDESCRIPTION());
	                   hrClaimPojo.setCLKEY(HrClaim.getCLKEY());

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
		return hrClaimPojo;
	}
	
	public double getclaimamountbtdates(String plant, int empnoid, String fromdate, String todate) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    double amount = 0;
		try {	    
			connection = DbBean.getConnection();
			
			
			query = " SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND EMPNOID ='"+empnoid+"' AND STATUS = 'Processed' AND (SUBSTRING(CLAIMDATE,7,4) +SUBSTRING(CLAIMDATE,4,2)+SUBSTRING(CLAIMDATE,1,2)) >= (SUBSTRING('"+fromdate+"',7,4) +SUBSTRING('"+fromdate+"',4,2)+SUBSTRING('"+fromdate+"',1,2)) AND (SUBSTRING(CLAIMDATE,7,4) +SUBSTRING(CLAIMDATE,4,2)+SUBSTRING(CLAIMDATE,1,2)) <= (SUBSTRING('"+todate+"',7,4) +SUBSTRING('"+todate+"',4,2)+SUBSTRING('"+todate+"',1,2))";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrClaim hrClaim=new HrClaim();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrClaim);
	                   amount = amount + hrClaim.getAMOUNT();
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
		return amount;
	}
	
	
	
	public int addHrClaimAttachment(HrClaimAttachment hrClaimAttachment) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ hrClaimAttachment.getPLANT() +"_"+TABLE_ATTACHMENT+"]([PLANT]" + 
					"           ,[CLAIMKEY]" + 
					"           ,[FileType]" +  
					"           ,[FileName]" +
					"           ,[FileSize]" +
					"           ,[FilePath]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, hrClaimAttachment.getPLANT());
				   ps.setString(2, hrClaimAttachment.getCLAIMKEY());
				   ps.setString(3, hrClaimAttachment.getFileType());
				   ps.setString(4, hrClaimAttachment.getFileName());
				   ps.setInt(5, hrClaimAttachment.getFileSize());
				   ps.setString(6, hrClaimAttachment.getFilePath());
				   ps.setString(7, dateutils.getDate());
				   ps.setString(8, hrClaimAttachment.getCRBY());
				  
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
					   throw new SQLException("Creating claim failed, no rows affected.");
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
	
	public HrClaimAttachment getHrClaimAttachmentById(String plant, int id) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrClaimAttachment hrClaimAttachment=new HrClaimAttachment();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_ATTACHMENT+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrClaimAttachment);
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
		return hrClaimAttachment;
	}
	
	public boolean DeleteHrClaimAttachment(String plant, int id) 
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant +"_"+TABLE_ATTACHMENT+"]"
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
	
	public List<HrClaimAttachment> getHrClaimAttachmentbyhdr(String plant, String ckey) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrClaimAttachment> HrClaimAttachmentlist=new ArrayList<HrClaimAttachment>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_ATTACHMENT+"] WHERE PLANT='"+plant+"' AND CLAIMKEY ='"+ckey+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrClaimAttachment hrClaimAttachment=new HrClaimAttachment();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrClaimAttachment);
	                   HrClaimAttachmentlist.add(hrClaimAttachment);
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
		return HrClaimAttachmentlist;
	}
	

}
