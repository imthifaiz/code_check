package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HrDeductionHdr;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrLeaveType;
import com.track.db.object.LeaveTypePojo;
import com.track.gates.DbBean;
import com.track.service.HrEmpTypeService;
import com.track.serviceImplementation.HrEmpTypeServiceImpl;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class HrDeductionHdrDAO {
	

	public static String TABLE_HEADER = "HRDEDUCTIONHDR";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrDeductionHdrDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrDeductionHdrDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int adddeductionhdr(HrDeductionHdr deductionhdr) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ deductionhdr.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[EMPID]" + 
					"           ,[DEDUCTION_NAME]" +  
					"           ,[DEDUCTION_AMOUNT]" +
					"           ,[DEDUCTION_DUE]" +
					"           ,[DEDUCTION_DATE]" +
					"           ,[ISGRATUITY]" +
					"           ,[STATUS]" +
					"           ,[CRAT]" + 
					"           ,[MONTH]" + 
					"           ,[YEAR]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, deductionhdr.getPLANT());
				   ps.setInt(2, deductionhdr.getEMPID());
				   ps.setString(3, deductionhdr.getDEDUCTION_NAME());
				   ps.setDouble(4, deductionhdr.getDEDUCTION_AMOUNT());
				   ps.setDouble(5, deductionhdr.getDEDUCTION_DUE());
				   ps.setString(6, deductionhdr.getDEDUCTION_DATE());
				   ps.setShort(7, deductionhdr.getISGRATUITY());
				   ps.setString(8, deductionhdr.getSTATUS());
				   ps.setString(9, dateutils.getDate());
				   ps.setString(10, deductionhdr.getMONTH());
				   ps.setString(11, deductionhdr.getYEAR());
				   ps.setString(12, deductionhdr.getCRBY());
				  
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
					   throw new SQLException("Creating deduction failed, no rows affected.");
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
	
	public List<HrDeductionHdr> getAlldeductionhdr(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrDeductionHdr> HrDeductionHdrList=new ArrayList<HrDeductionHdr>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrDeductionHdr hrDeductionHdr=new HrDeductionHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrDeductionHdr);
	                   HrDeductionHdrList.add(hrDeductionHdr);
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
		return HrDeductionHdrList;
	}
	
	public HrDeductionHdr getdeductionhdrById(String plant,int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrDeductionHdr hrDeductionHdr=new HrDeductionHdr();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrDeductionHdr);
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
		return hrDeductionHdr;
	}
	
	
	public int updatedeductionhdr(HrDeductionHdr deductionhdr,String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ deductionhdr.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+deductionhdr.getPLANT()+"',EMPID='"+deductionhdr.getEMPID()+"',"
					+ "DEDUCTION_NAME='"+deductionhdr.getDEDUCTION_NAME()+"',DEDUCTION_AMOUNT='"+deductionhdr.getDEDUCTION_AMOUNT()+"',"
					+ "DEDUCTION_DUE='"+deductionhdr.getDEDUCTION_DUE()+"',DEDUCTION_DATE='"+deductionhdr.getDEDUCTION_DATE()+"',ISGRATUITY='"+deductionhdr.getISGRATUITY()+"',"
					+ "STATUS='"+deductionhdr.getSTATUS()+"',MONTH='"+deductionhdr.getMONTH()+"',YEAR='"+deductionhdr.getYEAR()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+deductionhdr.getID();
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
					   throw new SQLException("Updating deduction failed, no rows affected.");
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
	
	public boolean Deletedeductionhdr(String plant,int id)
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
	
	public List<HrDeductionHdr> getdeductionhdrempmonthyear(String plant,String empid, String month, String year) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrDeductionHdr> HrDeductionHdrList=new ArrayList<HrDeductionHdr>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE EMPID ='"+empid+"' AND MONTH ='"+month+"' AND YEAR ='"+year+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrDeductionHdr hrDeductionHdr=new HrDeductionHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrDeductionHdr);
	                   HrDeductionHdrList.add(hrDeductionHdr);
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
		return HrDeductionHdrList;
	}
	
	public List<HrDeductionHdr> getdeductionhdrempid(String plant,String empid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrDeductionHdr> HrDeductionHdrList=new ArrayList<HrDeductionHdr>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE EMPID ='"+empid+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrDeductionHdr hrDeductionHdr=new HrDeductionHdr();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrDeductionHdr);
	                   HrDeductionHdrList.add(hrDeductionHdr);
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
		return HrDeductionHdrList;
	}
	

	
	public HrDeductionHdr getdeductionhdrempmonthyearname(String plant,String empid, String month, String year,String dname) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrDeductionHdr hrDeductionHdr=new HrDeductionHdr();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE EMPID ='"+empid+"' AND MONTH ='"+month+"' AND DEDUCTION_NAME ='"+dname+"' AND YEAR ='"+year+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrDeductionHdr);
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
		return hrDeductionHdr;
	}

}
