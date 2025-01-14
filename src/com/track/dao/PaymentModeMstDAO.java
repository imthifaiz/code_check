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
import com.track.db.object.PaymentModeMst;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class PaymentModeMstDAO {

	
	public static String TABLE_HEADER = "FINPAYMENTMODEMST";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.PaymentModeMstDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.PaymentModeMstDAO_PRINTPLANTMASTERLOG;
	
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
	
	public boolean addPaymentModeMst(PaymentModeMst paymentModeMst) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ paymentModeMst.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[PAYMENTMODE]" + 
					"           ,[ISNOTEDITABLE]" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, paymentModeMst.getPLANT());
				   ps.setString(2, paymentModeMst.getPAYMENTMODE());
				   ps.setShort(3, paymentModeMst.getISNOTEDITABLE());
				   ps.setString(4, dateutils.getDate());
				   ps.setString(5, paymentModeMst.getCRBY());
				  
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					 /* ResultSet rs = ps.getGeneratedKeys(); 
					  if(rs.next()) { 
						  HdrId = rs.getInt(1);
					  }*/
					  flag = true; 
				   }
				   else
				   {
					   throw new SQLException("Creating Payment mode failed, no rows affected.");
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
	
	public List<PaymentModeMst> getAllPaymentModeMst(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<PaymentModeMst> PaymentModeMstList=new ArrayList<PaymentModeMst>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   PaymentModeMst paymentModeMst=new PaymentModeMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, paymentModeMst);
	                   PaymentModeMstList.add(paymentModeMst);
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
		return PaymentModeMstList;
	}
	
	public PaymentModeMst getPaymentModeMstById(String plant, int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    PaymentModeMst paymentModeMst=new PaymentModeMst();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, paymentModeMst);
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
		return paymentModeMst;
	}
	
	public int updateSalary(PaymentModeMst paymentModeMst, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ paymentModeMst.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+paymentModeMst.getPLANT()+"',PAYMENTMODE='"+paymentModeMst.getPAYMENTMODE()+"',ISNOTEDITABLE='"+paymentModeMst.getISNOTEDITABLE()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+paymentModeMst.getID();
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
					   throw new SQLException("Updating payment mode failed, no rows affected.");
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
	
	public boolean DeletePaymentModeMst(String plant, int id)
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

	public boolean IsPaymentModeMstExists(String plant, String paymentmode)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
	    HrEmpSalaryMst hrEmpSalary=new HrEmpSalaryMst();
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PAYMENTMODE='"+paymentmode+"'";

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

	public List<PaymentModeMst> IsPaymentModeMstlist(String plant, String paymentmode)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<PaymentModeMst> PaymentModeMstList=new ArrayList<PaymentModeMst>();
	    
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PAYMENTMODE LIKE '%"+paymentmode+"%'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   PaymentModeMst paymentModeMst=new PaymentModeMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, paymentModeMst);
	                   PaymentModeMstList.add(paymentModeMst);
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
		return PaymentModeMstList;
	}    

}
