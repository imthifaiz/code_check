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
import com.track.db.object.LeaveTypePojo;
import com.track.gates.DbBean;
import com.track.service.HrEmpTypeService;
import com.track.serviceImplementation.HrEmpTypeServiceImpl;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class HrLeaveTypeDAO {


	public static String TABLE_HEADER = "HRLEAVETYPE";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrLeaveTypeDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrLeaveTypeDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addLeavetype(HrLeaveType leavetype) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ leavetype.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[LEAVETYPE]" + 
					"           ,[TOTALENTITLEMENT]" +  
					"           ,[ISNOPAYLEAVE]" +
					"           ,[CARRYFORWARD]" +
					"           ,[EMPLOYEETYPEID]" +
					"           ,[NOTE]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, leavetype.getPLANT());
				   ps.setString(2, leavetype.getLEAVETYPE());
				   ps.setDouble(3, leavetype.getTOTALENTITLEMENT());
				   ps.setShort(4, leavetype.getISNOPAYLEAVE());
				   ps.setDouble(5, leavetype.getCARRYFORWARD());
				   ps.setInt(6, leavetype.getEMPLOYEETYPEID());
				   ps.setString(7, leavetype.getNOTE());
				   ps.setString(8, dateutils.getDate());
				   ps.setString(9, leavetype.getCRBY());
				  
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
	
	public List<HrLeaveType> getAllLeavetype(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrLeaveType> HrLeaveTypeList=new ArrayList<HrLeaveType>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveType hrLeaveType=new HrLeaveType();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveType);
	                   HrLeaveTypeList.add(hrLeaveType);
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
		return HrLeaveTypeList;
	}
	
	public HrLeaveType getLeavetypeById(String plant, int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrLeaveType hrLeaveType=new HrLeaveType();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveType);
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
		return hrLeaveType;
	}
	
	public LeaveTypePojo getLeavetypeByIdpojo(String plant, int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrEmpTypeService HrEmpTypeService = new HrEmpTypeServiceImpl();
	    HrLeaveType hrLeaveType=new HrLeaveType();
	    LeaveTypePojo leaveTypePojo = new LeaveTypePojo();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveType);
	                }   
				   
				   	HrEmpType hrEmpType = HrEmpTypeService.getEmployeetypeById(plant, hrLeaveType.getEMPLOYEETYPEID());
					leaveTypePojo.setPLANT(hrLeaveType.getPLANT());
					leaveTypePojo.setID(hrLeaveType.getID());
					leaveTypePojo.setLEAVETYPE(hrLeaveType.getLEAVETYPE());
					leaveTypePojo.setEMPLOYEETYPEID(hrLeaveType.getEMPLOYEETYPEID());;
					leaveTypePojo.setEMPLOYEETYPE(hrEmpType.getEMPLOYEETYPE());
					leaveTypePojo.setTOTALENTITLEMENT(hrLeaveType.getTOTALENTITLEMENT());
					leaveTypePojo.setCARRYFORWARD(hrLeaveType.getCARRYFORWARD());
					leaveTypePojo.setNOTE(hrLeaveType.getNOTE());
					leaveTypePojo.setISNOPAYLEAVE(hrLeaveType.getISNOPAYLEAVE());
					
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
		return leaveTypePojo;
	}
	
	public int updateLeavetype(HrLeaveType leavetype, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ leavetype.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+leavetype.getPLANT()+"',LEAVETYPE='"+leavetype.getLEAVETYPE()+"',"
					+ "TOTALENTITLEMENT='"+leavetype.getTOTALENTITLEMENT()+"',ISNOPAYLEAVE='"+leavetype.getISNOPAYLEAVE()+"',"
					+ "CARRYFORWARD='"+leavetype.getCARRYFORWARD()+"',EMPLOYEETYPEID='"+leavetype.getEMPLOYEETYPEID()+"',"
					+ "NOTE='"+leavetype.getNOTE()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+leavetype.getID();
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
	
	public boolean DeleteLeavetype(String plant, int id)
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
	
	public boolean IsLeavetype(String plant, String leavetype, int etid) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE LEAVETYPE='"+leavetype+"' AND EMPLOYEETYPEID="+etid;

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
	
	public List<HrLeaveType> IsLeavetypelist(String plant, String leavetype, int etid) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrLeaveType> HrLeaveTypeList=new ArrayList<HrLeaveType>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE LEAVETYPE='"+leavetype+"' AND EMPLOYEETYPEID="+etid;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveType hrLeaveType=new HrLeaveType();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveType);
	                   HrLeaveTypeList.add(hrLeaveType);
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
		return HrLeaveTypeList;
	}
	
	public List<HrLeaveType> IsLeavetypelistdropdown(String plant, String leavetype, int etid) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrLeaveType> HrLeaveTypeList=new ArrayList<HrLeaveType>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE  EMPLOYEETYPEID="+etid+" AND (LEAVETYPE LIKE '%" + leavetype +"%') ORDER BY ID DESC" ;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveType hrLeaveType=new HrLeaveType();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveType);
	                   HrLeaveTypeList.add(hrLeaveType);
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
		return HrLeaveTypeList;
	}
	
	
	public int getleavettypeidbyname(String plant, String leavetype, int etid) throws Exception {
		
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    int lvtid = 0;
	    List<HrLeaveType> HrLeaveTypeList=new ArrayList<HrLeaveType>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE LEAVETYPE='"+leavetype+"' AND EMPLOYEETYPEID="+etid;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrLeaveType hrLeaveType=new HrLeaveType();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrLeaveType);
	                   lvtid = hrLeaveType.getID();
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
		return lvtid;
	}
	
	public boolean isEmptypeExists(String id,String plant) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();

			// query
			StringBuffer sql = new StringBuffer("SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE EMPLOYEETYPEID ='"+id+"'");
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
