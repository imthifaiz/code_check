package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrPayrollAdditionMst;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class HrPayrollAdditionMstDAO  extends BaseDAO {

	public static String TABLE_HEADER = "HRPAYROLLADDITIONMST";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrPayrollAdditionMstDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrPayrollAdditionMstDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addpayrolladditionmst(HrPayrollAdditionMst payrolladditionmst) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ payrolladditionmst.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[ADDITION_NAME]" + 
					"           ,[ADDITION_DESCRIPTION]" + 
					"           ,[ISDEDUCTION]" +  
					"           ,[ISCLAIM]" +  
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, payrolladditionmst.getPLANT());
				   ps.setString(2, payrolladditionmst.getADDITION_NAME());
				   ps.setString(3, payrolladditionmst.getADDITION_DESCRIPTION());
				   ps.setShort(4, payrolladditionmst.getISDEDUCTION());
				   ps.setShort(5, payrolladditionmst.getISCLAIM());
				   ps.setString(6, DateUtils.getDateTime());
				   ps.setString(7, payrolladditionmst.getCRBY());
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
					   throw new SQLException("Creating payroll addition master failed, no rows affected.");
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
	
	public List<HrPayrollAdditionMst> getAllpayrolladditionmst(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollAdditionMst> HrPayrollAdditionMstList=new ArrayList<HrPayrollAdditionMst>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollAdditionMst hrPayrollAdditionMst=new HrPayrollAdditionMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollAdditionMst);
	                   HrPayrollAdditionMstList.add(hrPayrollAdditionMst);
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
		return HrPayrollAdditionMstList;
	}
	

	public HrPayrollAdditionMst getpayrolladditionmstById(String plant,int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrPayrollAdditionMst hrPayrollAdditionMst=new HrPayrollAdditionMst();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollAdditionMst);
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
		return hrPayrollAdditionMst;
	}
	
	public int updatepayrolladditionmst(HrPayrollAdditionMst payrolladditionmst,String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ payrolladditionmst.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+payrolladditionmst.getPLANT()+"',ADDITION_NAME='"+payrolladditionmst.getADDITION_NAME()+"',ADDITION_DESCRIPTION='"+payrolladditionmst.getADDITION_DESCRIPTION()+"',ISDEDUCTION='"+payrolladditionmst.getISDEDUCTION()+"',ISCLAIM='"+payrolladditionmst.getISCLAIM()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+payrolladditionmst.getID();
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
					   throw new SQLException("Updating payroll addition master failed, no rows affected.");
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
	
	public boolean Deletepayrolladditionmst(String plant,int id)
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
	
	public boolean ispayrolladditionmst(String plant, String addname) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ADDITION_NAME='"+addname+"'";

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
	
	public List<HrPayrollAdditionMst> payrolladditionmstbyname(String plant, String addname) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollAdditionMst> HrPayrollAdditionMstList=new ArrayList<HrPayrollAdditionMst>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ADDITION_NAME='"+addname+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollAdditionMst hrPayrollAdditionMst=new HrPayrollAdditionMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollAdditionMst);
	                   HrPayrollAdditionMstList.add(hrPayrollAdditionMst);
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
		return HrPayrollAdditionMstList;
	}
	
	public List<HrPayrollAdditionMst> payrolladditionmstdeduct(String plant, String addname) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollAdditionMst> HrPayrollAdditionMstList=new ArrayList<HrPayrollAdditionMst>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ISDEDUCTION = '1' AND ADDITION_NAME LIKE '%"+addname+"%'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollAdditionMst hrPayrollAdditionMst=new HrPayrollAdditionMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollAdditionMst);
	                   HrPayrollAdditionMstList.add(hrPayrollAdditionMst);
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
		return HrPayrollAdditionMstList;
	}
	
	
	public List<HrPayrollAdditionMst> payrolladditionmstclaim(String plant, String addname) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollAdditionMst> HrPayrollAdditionMstList=new ArrayList<HrPayrollAdditionMst>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ISCLAIM = '1' AND ADDITION_NAME LIKE '%"+addname+"%'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollAdditionMst hrPayrollAdditionMst=new HrPayrollAdditionMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollAdditionMst);
	                   HrPayrollAdditionMstList.add(hrPayrollAdditionMst);
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
		return HrPayrollAdditionMstList;
	}
	
	public List<HrPayrollAdditionMst> payrolladditionmstadd(String plant, String addname) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollAdditionMst> HrPayrollAdditionMstList=new ArrayList<HrPayrollAdditionMst>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ISDEDUCTION = '0' AND ISCLAIM = '0' AND ADDITION_NAME LIKE '%"+addname+"%'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollAdditionMst hrPayrollAdditionMst=new HrPayrollAdditionMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollAdditionMst);
	                   HrPayrollAdditionMstList.add(hrPayrollAdditionMst);
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
		return HrPayrollAdditionMstList;
	}
	
	public List<HrPayrollAdditionMst> payrolladditionmstall(String plant, String addname) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollAdditionMst> HrPayrollAdditionMstList=new ArrayList<HrPayrollAdditionMst>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PLANT = '"+plant+"' AND ADDITION_NAME LIKE '%"+addname+"%'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollAdditionMst hrPayrollAdditionMst=new HrPayrollAdditionMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollAdditionMst);
	                   HrPayrollAdditionMstList.add(hrPayrollAdditionMst);
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
		return HrPayrollAdditionMstList;
	}
	
	
	public String getclsimnamebyid(String aPlant, String id,String extCond) throws Exception {
		String name = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("ID", id);
		
		String query = " isnull(ADDITION_NAME,'') ADDITION_NAME ";
		Map m = selectRow(query, ht, extCond);
		name = (String) m.get("ADDITION_NAME");
		if (name == "" || name == null)
			name = "";
		return name;
	}
	
	public Map selectRow(String query, Hashtable ht, String extCond)
			throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" +TABLE_HEADER+ "]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
			this.mLogger.query(this.printQuery, sql.toString());

			map = getRowOfData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return map;
	}
}
