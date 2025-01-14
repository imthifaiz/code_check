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
import com.track.db.object.HrDeductionHdr;
import com.track.db.object.HrEmpSalaryDET;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrLeaveType;
import com.track.db.object.HrPayrollAdditionMst;
import com.track.db.object.HrPayrollDET;
import com.track.db.object.LeaveTypePojo;
import com.track.gates.DbBean;
import com.track.service.HrEmpTypeService;
import com.track.serviceImplementation.HrEmpTypeServiceImpl;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class HrPayrollDETDAO {
	public static String TABLE_HEADER = "HRPAYROLLDET";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrPayrollDETDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrPayrollDETDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addpayrolldet(HrPayrollDET payrolldet) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ payrolldet.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[HDRID]" + 
					"           ,[LNNO]" +  
					"           ,[SALARYTYPE]" +
					"           ,[AMOUNT]" +
					"           ,[AMOUNT_TYPE]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, payrolldet.getPLANT());
				   ps.setInt(2, payrolldet.getHDRID());
				   ps.setInt(3, payrolldet.getLNNO());
				   ps.setString(4, payrolldet.getSALARYTYPE());
				   ps.setDouble(5, payrolldet.getAMOUNT());
				   ps.setString(6, payrolldet.getAMOUNT_TYPE());
				   ps.setString(7, dateutils.getDate());
				   ps.setString(8, payrolldet.getCRBY());
				  
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
					   throw new SQLException("Creating payroll details failed, no rows affected.");
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
	
	public List<HrPayrollDET> getAllpayrolldet(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollDET> HrPayrollDETList=new ArrayList<HrPayrollDET>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollDET hrPayrollDET=new HrPayrollDET();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollDET);
	                   HrPayrollDETList.add(hrPayrollDET);
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
		return HrPayrollDETList;
	}
	
	public HrPayrollDET getpayrolldetById(String plant,int id) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrPayrollDET hrPayrollDET=new HrPayrollDET();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollDET);
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
		return hrPayrollDET;
	}
	
	
	
	public int updatepayrolldet(HrPayrollDET payrolldet,String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ payrolldet.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+payrolldet.getPLANT()+"',"
					+ "HDRID='"+payrolldet.getHDRID()+"',LNNO='"+payrolldet.getLNNO()+"',"
					+ "SALARYTYPE='"+payrolldet.getSALARYTYPE()+"',AMOUNT='"+payrolldet.getAMOUNT()+"',"
					+ "AMOUNT_TYPE='"+payrolldet.getAMOUNT_TYPE()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+payrolldet.getID();
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
					   throw new SQLException("Updating payroll details failed, no rows affected.");
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
	
	public boolean Deletepayrolldet(String plant,int id)
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
	
	public boolean Ispayrolldet(String plant,String salarytype, int hdrid) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE SALARYTYPE='"+salarytype+"' AND HDRID='"+hdrid+"'";

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
	
	public HrPayrollDET payrolldetbyhdrisandtype(String plant,String salarytype, int hdrid) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrPayrollDET hrPayrollDET=new HrPayrollDET();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE SALARYTYPE='"+salarytype+"' AND HDRID='"+hdrid+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollDET);
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
		return hrPayrollDET;
	}
	
	public List<HrPayrollDET> getpayrolldetByhdrid(String plant,int hdrid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollDET> HrPayrollDETList=new ArrayList<HrPayrollDET>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE HDRID='"+hdrid+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollDET hrPayrollDET=new HrPayrollDET();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollDET);
	                   HrPayrollDETList.add(hrPayrollDET);
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
		return HrPayrollDETList;
	}
	
	
	public List<HrPayrollDET> getsalaryallowance(String plant,int hdrid, int empid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollDET> HrPayrollDETList=new ArrayList<HrPayrollDET>();
	    HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
		try {	    
			List<HrPayrollDET> hrPayrollDETlist = getpayrolldetByhdrid(plant, hdrid);
			List<HrEmpSalaryDET> hrEmpSalaryDETList = hrEmpSalaryDetDAO.EmpSalarydetlistbyempid(plant, empid);
			for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){
				for(HrEmpSalaryDET hrEmpSalaryDET:hrEmpSalaryDETList){
					if(hrEmpSalaryDET.getSALARYTYPE().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())){
						HrPayrollDET paydet = new HrPayrollDET();
						paydet.setID(hrPayrollDET.getID());
						paydet.setHDRID(hrPayrollDET.getHDRID());
						paydet.setSALARYTYPE(hrPayrollDET.getSALARYTYPE());
						paydet.setAMOUNT(hrPayrollDET.getAMOUNT());
						paydet.setAMOUNT_TYPE(hrPayrollDET.getAMOUNT_TYPE());
						HrPayrollDETList.add(paydet);
					}
				}
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} 
		return HrPayrollDETList;
	}
	
	public List<HrPayrollDET> getsalaryaddition(String plant,int hdrid, int empid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollDET> HrPayrollDETList=new ArrayList<HrPayrollDET>();
	    HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
		try {	    
			List<HrPayrollDET> hrPayrollDETlist = getpayrolldetByhdrid(plant, hdrid);
			List<HrPayrollAdditionMst> payaddmstadd = hrPayrollAdditionMstDAO.payrolladditionmstadd(plant, "");
			for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){
				for(HrPayrollAdditionMst hrPayrollAdditionMst:payaddmstadd){
					if(hrPayrollAdditionMst.getADDITION_NAME().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())){
						HrPayrollDET paydet = new HrPayrollDET();
						paydet.setID(hrPayrollDET.getID());
						paydet.setHDRID(hrPayrollDET.getHDRID());
						paydet.setSALARYTYPE(hrPayrollDET.getSALARYTYPE());
						paydet.setAMOUNT(hrPayrollDET.getAMOUNT());
						paydet.setAMOUNT_TYPE(hrPayrollDET.getAMOUNT_TYPE());
						HrPayrollDETList.add(paydet);
					}
				}
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} 
		return HrPayrollDETList;
	}
	
	public List<HrPayrollDET> getsalarydeduction(String plant,int hdrid, int empid,String month, String year) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollDET> HrPayrollDETList=new ArrayList<HrPayrollDET>();
	    HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
	    HrDeductionHdrDAO hrDeductionHdrDAO = new HrDeductionHdrDAO();
	    HrDeductionDetDAO hrDeductionDetDAO = new HrDeductionDetDAO();
		try {	    
			List<HrPayrollDET> hrPayrollDETlist = getpayrolldetByhdrid(plant, hdrid);
			List<HrPayrollAdditionMst> payaddmstdeduct = hrPayrollAdditionMstDAO.payrolladditionmstdeduct(plant, "");
			for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){
				for(HrPayrollAdditionMst hrPayrollAdditionMst:payaddmstdeduct){
					if(hrPayrollAdditionMst.getADDITION_NAME().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())){
						HrPayrollDET paydet = new HrPayrollDET();
						int hid=0;
						List<HrDeductionDet> hrDeductionDetlist = hrDeductionDetDAO.getdeductiondetbyempidmonthyear(plant, String.valueOf(empid), month, year);
						for (HrDeductionDet hrDeductionDet:hrDeductionDetlist) {
							HrDeductionHdr hrDeductionHdr = hrDeductionHdrDAO.getdeductionhdrById(plant, hrDeductionDet.getHDRID());
							if(hrDeductionHdr.getDEDUCTION_NAME().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())) {
								hid = hrDeductionDet.getHDRID();
							}
						}
						paydet.setID(hid);
						paydet.setHDRID(hrPayrollDET.getHDRID());
						paydet.setSALARYTYPE(hrPayrollDET.getSALARYTYPE());
						paydet.setAMOUNT(hrPayrollDET.getAMOUNT());
						paydet.setAMOUNT_TYPE(hrPayrollDET.getAMOUNT_TYPE());
						HrPayrollDETList.add(paydet);
					}
				}
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} 
		return HrPayrollDETList;
	}
	
	public boolean Ispayrolldetmaster(String plant,String salarytype) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE SALARYTYPE='"+salarytype+"'";

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
