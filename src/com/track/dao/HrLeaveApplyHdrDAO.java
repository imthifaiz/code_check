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
import com.track.db.object.HrLeaveApplyAttachment;
import com.track.db.object.HrLeaveApplyHdr;
import com.track.db.object.HrLeaveType;
import com.track.db.object.LeaveApplyHdrPojo;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class HrLeaveApplyHdrDAO {

	public static String TABLE_HEADER = "HRLEAVEAPPLYHDR";
	public static String TABLE_ATTACHMENT = "HRLEAVEAPPLYATTACHMENTS";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrLeaveApplyHdrDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrLeaveApplyHdrDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addHrLeaveApplyHdr(HrLeaveApplyHdr HrLeaveApplyHdr) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ HrLeaveApplyHdr.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[EMPNOID]" + 
					"           ,[LEAVETYPEID]" +  
					"           ,[REPORT_INCHARGE_ID]" +  
					"           ,[FROM_DATE]" +
					"           ,[TO_DATE]" +
					"           ,[NUMBEROFDAYS]" +
					"           ,[STATUS]" +
					"           ,[NOTES]" +
					"           ,[CRAT]" + 
					"           ,[UPAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, HrLeaveApplyHdr.getPLANT());
				   ps.setInt(2, HrLeaveApplyHdr.getEMPNOID());
				   ps.setInt(3, HrLeaveApplyHdr.getLEAVETYPEID());
				   ps.setInt(4, HrLeaveApplyHdr.getREPORT_INCHARGE_ID());
				   ps.setString(5, HrLeaveApplyHdr.getFROM_DATE());
				   ps.setString(6, HrLeaveApplyHdr.getTO_DATE());
				   ps.setDouble(7, HrLeaveApplyHdr.getNUMBEROFDAYS());
				   ps.setString(8, HrLeaveApplyHdr.getSTATUS());
				   ps.setString(9, HrLeaveApplyHdr.getNOTES());
				   ps.setString(10, dateutils.getDate());
				   ps.setString(11, dateutils.getDateTime());
				   ps.setString(12, HrLeaveApplyHdr.getCRBY());
				  
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
					   throw new SQLException("Creating Leave apply failed, no rows affected.");
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
	
	public List<HrLeaveApplyHdr> getAllHrLeaveApplyHdr(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrLeaveApplyHdr> HrLeaveApplyHdrlist=new ArrayList<HrLeaveApplyHdr>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveApplyHdr hrLeaveApplyHdr=new HrLeaveApplyHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyHdr);
	                   HrLeaveApplyHdrlist.add(hrLeaveApplyHdr);
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
		return HrLeaveApplyHdrlist;
	}
	
	public HrLeaveApplyHdr getHrLeaveApplyHdrById(String plant, int id) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrLeaveApplyHdr hrLeaveApplyHdr=new HrLeaveApplyHdr();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyHdr);
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
		return hrLeaveApplyHdr;
	}
	
	
	public int updateHrLeaveApplyHdr(HrLeaveApplyHdr HrLeaveApplyHdr, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ HrLeaveApplyHdr.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+HrLeaveApplyHdr.getPLANT()+"',EMPNOID='"+HrLeaveApplyHdr.getEMPNOID()+"',"
					+ "LEAVETYPEID='"+HrLeaveApplyHdr.getLEAVETYPEID()+"',FROM_DATE='"+HrLeaveApplyHdr.getFROM_DATE()+"',REPORT_INCHARGE_ID='"+HrLeaveApplyHdr.getREPORT_INCHARGE_ID()+"',"
					+ "TO_DATE='"+HrLeaveApplyHdr.getTO_DATE()+"',NUMBEROFDAYS='"+HrLeaveApplyHdr.getNUMBEROFDAYS()+"',NOTES='"+HrLeaveApplyHdr.getNOTES()+"',REASON='"+HrLeaveApplyHdr.getREASON()+"',"
					+ "STATUS='"+HrLeaveApplyHdr.getSTATUS()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+HrLeaveApplyHdr.getID();
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
	
	public boolean DeleteHrLeaveApplyHdr(String plant, int id) 
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
	
	public List<HrLeaveApplyHdr> getHrLeaveApplyHdrbyEmpid(String plant, int empnoid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrLeaveApplyHdr> HrLeaveApplyHdrlist=new ArrayList<HrLeaveApplyHdr>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND EMPNOID ="+empnoid;
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveApplyHdr hrLeaveApplyHdr=new HrLeaveApplyHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyHdr);
	                   HrLeaveApplyHdrlist.add(hrLeaveApplyHdr);
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
		return HrLeaveApplyHdrlist;
	}
	
	
	public int addHrLeaveApplyAttachment(HrLeaveApplyAttachment hrLeaveApplyAttachment) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ hrLeaveApplyAttachment.getPLANT() +"_"+TABLE_ATTACHMENT+"]([PLANT]" + 
					"           ,[LEAVEAPPLYHDRID]" + 
					"           ,[FileType]" +  
					"           ,[FileName]" +
					"           ,[FileSize]" +
					"           ,[FilePath]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, hrLeaveApplyAttachment.getPLANT());
				   ps.setInt(2, hrLeaveApplyAttachment.getLEAVEAPPLYHDRID());
				   ps.setString(3, hrLeaveApplyAttachment.getFileType());
				   ps.setString(4, hrLeaveApplyAttachment.getFileName());
				   ps.setInt(5, hrLeaveApplyAttachment.getFileSize());
				   ps.setString(6, hrLeaveApplyAttachment.getFilePath());
				   ps.setString(7, dateutils.getDate());
				   ps.setString(8, hrLeaveApplyAttachment.getCRBY());
				  
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
					   throw new SQLException("Creating Leave apply failed, no rows affected.");
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
	
	public HrLeaveApplyAttachment getHrLeaveApplyAttachmentById(String plant, int id) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrLeaveApplyAttachment hrLeaveApplyAttachment=new HrLeaveApplyAttachment();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_ATTACHMENT+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyAttachment);
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
		return hrLeaveApplyAttachment;
	}
	
	public boolean DeleteHrLeaveApplyAttachment(String plant, int id) 
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
	
	public List<HrLeaveApplyAttachment> getHrLeaveApplyAttachmentbyhdr(String plant, int hdrid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrLeaveApplyAttachment> hrLeaveApplyAttachmentlist=new ArrayList<HrLeaveApplyAttachment>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_ATTACHMENT+"] WHERE PLANT='"+plant+"' AND LEAVEAPPLYHDRID ="+hdrid;
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveApplyAttachment hrLeaveApplyAttachment=new HrLeaveApplyAttachment();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyAttachment);
	                   hrLeaveApplyAttachmentlist.add(hrLeaveApplyAttachment);
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
		return hrLeaveApplyAttachmentlist;
	}
	
	
	public List<LeaveApplyHdrPojo> getHrLeaveApplyHdrPOJObyEmpid(String plant, int empnoid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<LeaveApplyHdrPojo> LeaveApplyHdrPojolist=new ArrayList<LeaveApplyHdrPojo>();
	    HrLeaveTypeDAO HrLeaveTypeDAO =new  HrLeaveTypeDAO();
	    EmployeeLeaveDetDAO EmployeeLeaveDetDAO = new EmployeeLeaveDetDAO();
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT PLANT,ID,EMPNOID,LEAVETYPEID,REPORT_INCHARGE_ID,FROM_DATE,TO_DATE,NUMBEROFDAYS,STATUS,NOTES,CRAT,CRBY,UPAT,UPBY,ISNULL(REASON,'') AS REASON FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND EMPNOID ="+empnoid+"ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveApplyHdr hrLeaveApplyHdr=new HrLeaveApplyHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyHdr);
	                   LeaveApplyHdrPojo LeaveApplyHdrPojo = new LeaveApplyHdrPojo();
	                   LeaveApplyHdrPojo.setID(hrLeaveApplyHdr.getID());
	                   LeaveApplyHdrPojo.setEMPNOID(hrLeaveApplyHdr.getEMPNOID());
	                   LeaveApplyHdrPojo.setLEAVETYPEID(hrLeaveApplyHdr.getLEAVETYPEID());
	                   EmployeeLeaveDET empleavedet = EmployeeLeaveDetDAO.getEmployeeLeavedetById(plant,  hrLeaveApplyHdr.getLEAVETYPEID());
	                   HrLeaveType leavetype = HrLeaveTypeDAO.getLeavetypeById(plant, empleavedet.getLEAVETYPEID());
	                   LeaveApplyHdrPojo.setLEAVETYPE(leavetype.getLEAVETYPE());
	                   LeaveApplyHdrPojo.setFROM_DATE(hrLeaveApplyHdr.getFROM_DATE());
	                   LeaveApplyHdrPojo.setTO_DATE(hrLeaveApplyHdr.getTO_DATE());
	                   LeaveApplyHdrPojo.setNUMBEROFDAYS(hrLeaveApplyHdr.getNUMBEROFDAYS());
	                   LeaveApplyHdrPojo.setSTATUS(hrLeaveApplyHdr.getSTATUS());
	                   LeaveApplyHdrPojo.setNOTES(hrLeaveApplyHdr.getNOTES());
	                   LeaveApplyHdrPojo.setREASON(hrLeaveApplyHdr.getREASON());
	                   if(hrLeaveApplyHdr.getUPAT() != null) {
		                   if(hrLeaveApplyHdr.getUPAT().length() > 0) {
		                	   LeaveApplyHdrPojo.setSTATUSDATE(dateutils.getDB2UserDate_New(hrLeaveApplyHdr.getUPAT()));
		                   }else {
		                	   LeaveApplyHdrPojo.setSTATUSDATE("");
		                   }
	                   }else {
	                	   LeaveApplyHdrPojo.setSTATUSDATE("");
	                   }   
		                  
	                   LeaveApplyHdrPojolist.add(LeaveApplyHdrPojo);
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
		return LeaveApplyHdrPojolist;
	}
	
	public List<LeaveApplyHdrPojo> getHrLeaveApplyHdrPOJObyRepid(String plant, int repid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<LeaveApplyHdrPojo> LeaveApplyHdrPojolist=new ArrayList<LeaveApplyHdrPojo>();
	    HrLeaveTypeDAO HrLeaveTypeDAO =new  HrLeaveTypeDAO();
	    EmployeeLeaveDetDAO EmployeeLeaveDetDAO = new EmployeeLeaveDetDAO();
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND REPORT_INCHARGE_ID ="+repid+"ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveApplyHdr hrLeaveApplyHdr=new HrLeaveApplyHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveApplyHdr);
	                   LeaveApplyHdrPojo LeaveApplyHdrPojo = new LeaveApplyHdrPojo();
	                   LeaveApplyHdrPojo.setID(hrLeaveApplyHdr.getID());
	                   LeaveApplyHdrPojo.setEMPNOID(hrLeaveApplyHdr.getEMPNOID());
	                   LeaveApplyHdrPojo.setLEAVETYPEID(hrLeaveApplyHdr.getLEAVETYPEID());
	                   EmployeeLeaveDET empleavedet = EmployeeLeaveDetDAO.getEmployeeLeavedetById(plant,  hrLeaveApplyHdr.getLEAVETYPEID());
	                   HrLeaveType leavetype = HrLeaveTypeDAO.getLeavetypeById(plant, empleavedet.getLEAVETYPEID());
	                   LeaveApplyHdrPojo.setLEAVETYPE(leavetype.getLEAVETYPE());
	                   LeaveApplyHdrPojo.setFROM_DATE(hrLeaveApplyHdr.getFROM_DATE());
	                   LeaveApplyHdrPojo.setTO_DATE(hrLeaveApplyHdr.getTO_DATE());
	                   LeaveApplyHdrPojo.setNUMBEROFDAYS(hrLeaveApplyHdr.getNUMBEROFDAYS());
	                   LeaveApplyHdrPojo.setSTATUS(hrLeaveApplyHdr.getSTATUS());
	                   LeaveApplyHdrPojo.setNOTES(hrLeaveApplyHdr.getNOTES());
	                   if(hrLeaveApplyHdr.getUPAT() != null) {
		                   if(hrLeaveApplyHdr.getUPAT().length() > 0) {
		                	   LeaveApplyHdrPojo.setSTATUSDATE(dateutils.getDB2UserDate_New(hrLeaveApplyHdr.getUPAT()));
		                   }else {
		                	   LeaveApplyHdrPojo.setSTATUSDATE("");
		                   }
	                   }else {
	                	   LeaveApplyHdrPojo.setSTATUSDATE("");
	                   }   
	                  
	                   LeaveApplyHdrPojolist.add(LeaveApplyHdrPojo);
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
		return LeaveApplyHdrPojolist;
	}

}
