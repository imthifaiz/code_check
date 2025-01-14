package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HrEmpType;
import com.track.db.object.ParentChildCmpDet;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class ParentChildCmpDetDAO extends BaseDAO{

	public static String TABLE_HEADER = "PARENT_CHILD_COMPANY_DET";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.ParentChildCmpDetDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.ParentChildCmpDetDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addParentChildCmpDet(ParentChildCmpDet parentChildCmpDet) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+TABLE_HEADER+"]([PARENT_PLANT]" + 
					"           ,[CHILD_PLANT]" +  
					"           ,[ISCHILD_AS_PARENT]" +  
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, parentChildCmpDet.getPARENT_PLANT());
				   ps.setString(2, parentChildCmpDet.getCHILD_PLANT());
				   ps.setShort(3, parentChildCmpDet.getISCHILD_AS_PARENT());
				   ps.setString(4, DateUtils.getDateTime());
				   ps.setString(5, parentChildCmpDet.getCRBY());
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
					   throw new SQLException("Creating parent child company det failed, no rows affected.");
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
	
	public List<ParentChildCmpDet> getAllParentChildCmpDet() throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<ParentChildCmpDet> ParentChildCmpDetList=new ArrayList<ParentChildCmpDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   ParentChildCmpDet parentChildCmpDet=new ParentChildCmpDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, parentChildCmpDet);
	                   ParentChildCmpDetList.add(parentChildCmpDet);
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
		return ParentChildCmpDetList;
	}
	
	public List<ParentChildCmpDet> getAllParentChildCmpDetdropdown(String parent, String child) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<ParentChildCmpDet> ParentChildCmpDetList=new ArrayList<ParentChildCmpDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+TABLE_HEADER+"] WHERE PARENT_PLANT ='"+parent+"' AND (CHILD_PLANT LIKE '%" + child + "%') ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   ParentChildCmpDet parentChildCmpDet=new ParentChildCmpDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, parentChildCmpDet);
	                   ParentChildCmpDetList.add(parentChildCmpDet);
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
		return ParentChildCmpDetList;
	}
	
	public ParentChildCmpDet getParentChildCmpDetById(int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    ParentChildCmpDet parentChildCmpDet=new ParentChildCmpDet();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, parentChildCmpDet);
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
		return parentChildCmpDet;
	}
	
	public boolean DeleteParentChildCmpDet(int id) 
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" +TABLE_HEADER+"]"
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
	
	public boolean DeleteParentChildCmpDetByParent(String parent) 
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" +TABLE_HEADER+"]"
			                        + " WHERE PARENT_PLANT ='"+parent+"'";
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
	
	public boolean IsParentChildCmpDet(String parent)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+TABLE_HEADER+"] WHERE PARENT_PLANT='"+parent+"'";

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
	
	public List<ParentChildCmpDet> getAllParentChildCmpDetPARENT() throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<ParentChildCmpDet> ParentChildCmpDetList=new ArrayList<ParentChildCmpDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = " SELECT PARENT_PLANT,0 AS ID,'0' AS CHILD_PLANT,'0' AS CRAT,'0' AS CRBY,'0' AS UPAT,'0' AS UPBY,CAST(0 as tinyint) AS ISCHILD_AS_PARENT FROM ["+TABLE_HEADER+"] GROUP BY PARENT_PLANT ORDER BY PARENT_PLANT ASC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   ParentChildCmpDet parentChildCmpDet=new ParentChildCmpDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, parentChildCmpDet);
	                   ParentChildCmpDetList.add(parentChildCmpDet);
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
		return ParentChildCmpDetList;
	}
	
	public List<ParentChildCmpDet> getbyparentforPEO(String parent) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<ParentChildCmpDet> ParentChildCmpDetList=new ArrayList<ParentChildCmpDet>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT A.* FROM ["+TABLE_HEADER+"] AS A LEFT JOIN PLNTMST AS B ON B.PLANT = A.CHILD_PLANT WHERE A.PARENT_PLANT='"+parent+"' AND ISNULL(B.ISAUTO_MINMAX_MULTIESTPO_BY_PARENT,0) = '1'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   ParentChildCmpDet parentChildCmpDet=new ParentChildCmpDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, parentChildCmpDet);
	                   ParentChildCmpDetList.add(parentChildCmpDet);
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
		return ParentChildCmpDetList;
	}
	
	public String getPARENT_CHILD(String plant) throws Exception {
		java.sql.Connection con = null;
		String parent_plant = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" SELECT isnull(PARENT_PLANT,'') PARENT_PLANT FROM [PARENT_CHILD_COMPANY_DET] WHERE PARENT_PLANT='"+plant+"'") ;
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			parent_plant = (String) m.get("PARENT_PLANT");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return parent_plant;
	}

	public String getPARENTBYCHILD(String plant) throws Exception {
		java.sql.Connection con = null;
		String parent_plant = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" SELECT isnull(PARENT_PLANT,'') PARENT_PLANT FROM [PARENT_CHILD_COMPANY_DET] WHERE CHILD_PLANT='"+plant+"'") ;
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			parent_plant = (String) m.get("PARENT_PLANT");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return parent_plant;
	}
	
	  
		public ArrayList getChildCompanyByparent(String plant) throws Exception {

			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();
				

				boolean flag = false;
				String sQry = "select CHILD_PLANT from [PARENT_CHILD_COMPANY_DET] where PARENT_PLANT ='"	+ plant + "' ";
				this.mLogger.query(this.printQuery, sQry);

				al = selectData(con, sQry);

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}

			return al;
		}
		
		
		public boolean getisChildAsParent(String plant) throws Exception {
		    java.sql.Connection con = null;
		    String child = "";
		    boolean ischildParent = false;
		    try {
		            
		    con = DbBean.getConnection();
		    StringBuffer SqlQuery = new StringBuffer(" SELECT ISCHILD_AS_PARENT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'") ;
		     
		    System.out.println(SqlQuery.toString());
		        Map m = this.getRowOfData(con, SqlQuery.toString());

		        child = (String) m.get("ISCHILD_AS_PARENT");
		        if(child==null) {
		        	child = "0";
		        	ischildParent = false;
		        }
		        
		        if(child.equalsIgnoreCase("1")) {
		        	ischildParent = true;
		        }

		    } catch (Exception e) {
		            this.mLogger.exception(this.printLog, "", e);
		            throw e;
		    } finally {
		            if (con != null) {
		                    DbBean.closeConnection(con);
		            }
		    }
		    return ischildParent;
		}
	
	
}
