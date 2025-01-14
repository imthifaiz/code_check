package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.PEPPOL_RECEIVED_DATA;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class PeppolReceivedDataDAO {


	public static String TABLE_HEADER = "PEPPOL_RECEIVED_DATA";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.PEPPOL_RECEIVED_DATADAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.PEPPOL_RECEIVED_DATADAO_PRINTPLANTMASTERLOG;
	
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
	
	
	public int addPeppolReceivedData(PEPPOL_RECEIVED_DATA PeppolReceivedData) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+PeppolReceivedData.getPLANT()+"_"+TABLE_HEADER+"] ([PLANT]" + 
					",[EVENT]" + 
					",[DOCID]" + 
					",[RECEIVEDAT]" + 
					",[INVOICEFILEURL]" + 
					",[EVIDENCEFILEURL]" + 
					",[BILLNO]" + 
					",[EXPIRESAT]" + 
					",[BILL_STATUS]" + 
					",[CRAT]" + 
					",[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, PeppolReceivedData.getPLANT());
				   ps.setString(2, PeppolReceivedData.getEVENT());
				   ps.setString(3, PeppolReceivedData.getDOCID());
				   ps.setString(4, PeppolReceivedData.getRECEIVEDAT());
				   ps.setString(5, PeppolReceivedData.getINVOICEFILEURL());
				   ps.setString(6, PeppolReceivedData.getEVIDENCEFILEURL());
				   ps.setString(7, PeppolReceivedData.getBILLNO());
				   ps.setString(8, PeppolReceivedData.getEXPIRESAT());
				   ps.setShort(9, PeppolReceivedData.getBILL_STATUS());
				   ps.setString(10, dateutils.getDate());
				   ps.setString(11, PeppolReceivedData.getCRBY());
				  
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
					   throw new SQLException("Creating Peppol Receive Data failed, no rows affected.");
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
	
	public int insertPeppolReceivedData(PEPPOL_RECEIVED_DATA PeppolReceivedData) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="INSERT INTO ["+PeppolReceivedData.getPLANT()+"_"+TABLE_HEADER+"] ([PLANT],[EVENT],[DOCID],[RECEIVEDAT],[INVOICEFILEURL],[EVIDENCEFILEURL],[BILLNO],[EXPIRESAT],"
					+ "[BILL_STATUS],[CRAT],[CRBY]) VALUES ('"+PeppolReceivedData.getPLANT()+"','"+PeppolReceivedData.getEVENT()+"','"+PeppolReceivedData.getDOCID()+"','"+PeppolReceivedData.getRECEIVEDAT()+"',"
					+ "'"+PeppolReceivedData.getINVOICEFILEURL()+"','"+PeppolReceivedData.getEVIDENCEFILEURL()+"','"+PeppolReceivedData.getBILLNO()+"','"+PeppolReceivedData.getEXPIRESAT()+"',"
					+ "'"+PeppolReceivedData.getBILL_STATUS()+"','"+PeppolReceivedData.getCRAT()+"','"+PeppolReceivedData.getCRBY()+"')";

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
					   throw new SQLException("Updating Peppol Receive Data failed, no rows affected.");
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
	
	public int updatePeppolReceivedData(PEPPOL_RECEIVED_DATA PeppolReceivedData, String user, int id) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+PeppolReceivedData.getPLANT()+"_"+TABLE_HEADER+"] SET PLANT='"+PeppolReceivedData.getPLANT()+"',DOCID='"+PeppolReceivedData.getDOCID()+"',"
					+ "EVENT='"+PeppolReceivedData.getEVENT()+"',BILL_STATUS='"+PeppolReceivedData.getBILL_STATUS()+"',EXPIRESAT='"+PeppolReceivedData.getEXPIRESAT()+"',"
					+ "BILLNO='"+PeppolReceivedData.getBILLNO()+"',EVIDENCEFILEURL='"+PeppolReceivedData.getEVIDENCEFILEURL()+"',INVOICEFILEURL='"+PeppolReceivedData.getINVOICEFILEURL()+"',"
					+ "RECEIVEDAT='"+PeppolReceivedData.getRECEIVEDAT()+"',"
					+ "UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+id;
			
			
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
					   throw new SQLException("Updating Peppol Receive Data failed, no rows affected.");
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
	
	public boolean DeletePeppolReceivedData(String plant, int id)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        String sQry = "DELETE FROM " + "["+plant+"_"+TABLE_HEADER+"]"
			                        + " WHERE DOCID ='"+id+"'";
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
	
	public List<PEPPOL_RECEIVED_DATA> getAllPeppolReceivedData(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<PEPPOL_RECEIVED_DATA> peppolReceivedList=new ArrayList<PEPPOL_RECEIVED_DATA>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT PLANT,ID,EVENT,DOCID,RECEIVEDAT,INVOICEFILEURL,EVIDENCEFILEURL,ISNULL(BILLNO,'') AS BILLNO,EXPIRESAT,BILL_STATUS,CRAT,CRBY,UPAT,UPBY FROM ["+plant+"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   PEPPOL_RECEIVED_DATA PeppolReceivedData=new PEPPOL_RECEIVED_DATA();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, PeppolReceivedData);
	                   peppolReceivedList.add(PeppolReceivedData);
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
		return peppolReceivedList;
	}
	
	public PEPPOL_RECEIVED_DATA getPeppolReceivedDataById(String plant,int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    PEPPOL_RECEIVED_DATA PeppolReceivedData=new PEPPOL_RECEIVED_DATA();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+plant+"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, PeppolReceivedData);
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
		return PeppolReceivedData;
	}
	

	public boolean IsPeppolReceivedData(int id) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+TABLE_HEADER+"] WHERE ID="+id;

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
	
	public boolean IsPeppolReceivedDataByDocid(String plant,String docid) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+plant+"_"+TABLE_HEADER+"] WHERE DOCID='"+docid+"'";

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
