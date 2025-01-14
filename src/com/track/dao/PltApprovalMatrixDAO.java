package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HrLeaveType;
import com.track.db.object.PltApprovalMatrix;
import com.track.gates.DbBean;
import com.track.gates.userBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class PltApprovalMatrixDAO {
	
	public static String TABLE_HEADER = "PLTAPPROVALMATRIX";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.PltApprovalMatrixDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.PltApprovalMatrixDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addPltApprovalMatrix(PltApprovalMatrix pltApprovalMatrix) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+TABLE_HEADER+"]([PLANT]" + 
					"           ,[APPROVALTYPE]" + 
					"           ,[ISCREATE]" +  
					"           ,[ISUPDATE]" +
					"           ,[ISDELETE]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, pltApprovalMatrix.getPLANT());
				   ps.setString(2, pltApprovalMatrix.getAPPROVALTYPE());
				   ps.setShort(3, pltApprovalMatrix.getISCREATE());
				   ps.setShort(4, pltApprovalMatrix.getISUPDATE());
				   ps.setShort(5, pltApprovalMatrix.getISDELETE());
				   ps.setString(6, dateutils.getDateTime());
				   ps.setString(7, pltApprovalMatrix.getCRBY());
				  
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
					   throw new SQLException("Creating plant apprival matrix failed, no rows affected.");
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
	
	public int updatePltApprovalMatrix(PltApprovalMatrix pltApprovalMatrix, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+TABLE_HEADER+"] SET PLANT='"+pltApprovalMatrix.getPLANT()+"',APPROVALTYPE='"+pltApprovalMatrix.getAPPROVALTYPE()+"',"
					+ "ISCREATE='"+pltApprovalMatrix.getISCREATE()+"',ISUPDATE='"+pltApprovalMatrix.getISUPDATE()+"',"
					+ "ISDELETE='"+pltApprovalMatrix.getISDELETE()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+pltApprovalMatrix.getID();
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
					   throw new SQLException("Updating plant apprival matrix  failed, no rows affected.");
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
	
	public boolean DeletePltApprovalMatrix(String plant)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" +TABLE_HEADER+"]"
			                        + " WHERE PLANT ='"+plant+"'";
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
	
	public PltApprovalMatrix getPltApprovalMatrixBbpltandapptype(String plant,String AppType)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    PltApprovalMatrix pltApprovalMatrix=new PltApprovalMatrix();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND APPROVALTYPE='"+AppType+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, pltApprovalMatrix);
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
		return pltApprovalMatrix;
	}
	
	public List<PltApprovalMatrix> getPltApprovalMatrixByplt(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<PltApprovalMatrix> PltApprovalMatrixList=new ArrayList<PltApprovalMatrix>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+TABLE_HEADER+"]  WHERE PLANT='"+plant+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   PltApprovalMatrix pltApprovalMatrix=new PltApprovalMatrix();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, pltApprovalMatrix);
	                   PltApprovalMatrixList.add(pltApprovalMatrix);
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
		return PltApprovalMatrixList;
	}
	
	public List<PltApprovalMatrix> getAllPltApprovalMatrix() throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<PltApprovalMatrix> PltApprovalMatrixList=new ArrayList<PltApprovalMatrix>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   PltApprovalMatrix pltApprovalMatrix=new PltApprovalMatrix();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, pltApprovalMatrix);
	                   PltApprovalMatrixList.add(pltApprovalMatrix);
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
		return PltApprovalMatrixList;
	}
	
	
	public boolean CheckApproval(String plant,String AppType,String data)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    boolean val = false;
	    PltApprovalMatrix pltApprovalMatrix=new PltApprovalMatrix();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+TABLE_HEADER+"] WHERE PLANT='"+plant+"' AND APPROVALTYPE='"+AppType+"'";
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, pltApprovalMatrix);
	                }   
			this.mLogger.query(this.printQuery, query);
			}
			
			if(data.equalsIgnoreCase("CREATE")) {
				if(pltApprovalMatrix.getISCREATE() == 1) {
					val = true;
				}
			}
			if(data.equalsIgnoreCase("UPDATE")) {
				if(pltApprovalMatrix.getISUPDATE() == 1) {
					val = true;
				}
			}
			if(data.equalsIgnoreCase("DELETE")) {
				if(pltApprovalMatrix.getISDELETE() == 1) {
					val = true;
				}
			}
			
		} catch (Exception e) {
			val=false;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return val;
	}
	
	public boolean CheckApprovalByUser(String plant, String AppType, String data, String user)
			throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		String query = "";
		boolean val = false;
		PltApprovalMatrix pltApprovalMatrix = new PltApprovalMatrix();
		userBean userBeandao = new userBean();
		try {

			boolean check = true;
			if (AppType.equalsIgnoreCase("PURCHASE")) {
				if (userBeandao.getUserIspurchaseApproval(user, plant)) {
					check = false;
				}
			}
			if (AppType.equalsIgnoreCase("PURCHASE RETURN")) {
				if (userBeandao.getUserIspurchaseReturnApproval(user, plant)) {
					check = false;
				}
			}
			if (AppType.equalsIgnoreCase("SALES")) {
				if (userBeandao.getUserIsSalesApproval(user, plant)) {
					check = false;
				}
			}
			if (AppType.equalsIgnoreCase("SALES RETURN")) {
				if (userBeandao.getUserIsSalesReturnApproval(user, plant)) {
					check = false;
				}
			}

			if (check) {

				connection = DbBean.getConnection();
				query = "SELECT * FROM [" + TABLE_HEADER + "] WHERE PLANT='" + plant + "' AND APPROVALTYPE='" + AppType
						+ "'";
				if (connection != null) {
					ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					ResultSet rst = ps.executeQuery();
					while (rst.next()) {
						ResultSetToObjectMap.loadResultSetIntoObject(rst, pltApprovalMatrix);
					}
					this.mLogger.query(this.printQuery, query);
				}

				if (data.equalsIgnoreCase("CREATE")) {
					if (pltApprovalMatrix.getISCREATE() == 1) {
						val = true;
					}
				}
				if (data.equalsIgnoreCase("UPDATE")) {
					if (pltApprovalMatrix.getISUPDATE() == 1) {
						val = true;
					}
				}
				if (data.equalsIgnoreCase("DELETE")) {
					if (pltApprovalMatrix.getISDELETE() == 1) {
						val = true;
					}
				}
			}

		} catch (Exception e) {
			val=false;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return val;
	}
	
	

}
