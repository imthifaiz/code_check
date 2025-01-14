package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.object.FinProject;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class FinProjectDAO  extends BaseDAO {

	public static String TABLE_HEADER = "FINPROJECT";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.FinProjectDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.FinProjectDAO_PRINTPLANTMASTERLOG;
	
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
	
	
	public List<FinProject> getAllFinProject(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<FinProject> FinProjectList=new ArrayList<FinProject>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   FinProject finProject=new FinProject();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, finProject);
	                   FinProjectList.add(finProject);
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
		return FinProjectList;
	}
	
	public FinProject getFinProjectById(String plant, int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    FinProject finProject=new FinProject();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, finProject);
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
		return finProject;
	}
	
	public FinProject getFinProjectByProject(String plant, String project)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    FinProject finProject=new FinProject();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PROJECT="+project;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, finProject);
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
		return finProject;
	}


	public List<FinProject> dFinProjectlist(String plant, String projectname)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<FinProject> FinProjectList=new ArrayList<FinProject>();
	    
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PROJECT_NAME LIKE '%"+projectname+"%'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   FinProject finProject=new FinProject();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, finProject);
	                   FinProjectList.add(finProject);
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
		return FinProjectList;
	}    
	
	public boolean isExistFinProject(String project, String aCustomer, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ TABLE_HEADER + "]" + " WHERE CUSTNO = '"+aCustomer+"' AND PROJECT LIKE '%"+project+"%' ";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					isExists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}
	
	public boolean isExistFinProjectforPurchase(String project, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ TABLE_HEADER + "]" + " WHERE PROJECT LIKE '%"+project+"%' ";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					isExists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}
	
	public List<FinProject> dFinProjectbyprojectforPurchase(String project, String plant)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<FinProject> FinProjectList=new ArrayList<FinProject>();
	    
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PROJECT LIKE '%"+project+"%' ";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   FinProject finProject=new FinProject();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, finProject);
	                   FinProjectList.add(finProject);
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
		return FinProjectList;
	}
	
	public List<FinProject> dropFinProjectlist(String plant, String projectname, String custno)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<FinProject> FinProjectList=new ArrayList<FinProject>();
	    
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE CUSTNO = '"+custno+"' AND PROJECT_NAME LIKE '%"+projectname+"%'";
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   FinProject finProject=new FinProject();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, finProject);
	                   FinProjectList.add(finProject);
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
		return FinProjectList;
	}   
	

	public List<FinProject> dFinProjectlistwithStatus(String plant, String projectname, String expDate)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<FinProject> FinProjectList=new ArrayList<FinProject>();
	    
	    try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PROJECT_NAME LIKE '%"+projectname+"%' AND PROJECT_STATUS = 'OPEN' AND ( EXPIRY_DATE='' OR (SUBSTRING(EXPIRY_DATE,7,4) +SUBSTRING(EXPIRY_DATE,4,2)+SUBSTRING(EXPIRY_DATE,1,2)) >= (SUBSTRING('"+expDate+"',7,4) +SUBSTRING('"+expDate+"',4,2)+SUBSTRING('"+expDate+"',1,2)))";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   FinProject finProject=new FinProject();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, finProject);
	                   FinProjectList.add(finProject);
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
		return FinProjectList;
	}    
	
	public List<FinProject> dropFinProjectlistwithStatus(String plant, String projectname, String custno, String expDate)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<FinProject> FinProjectList=new ArrayList<FinProject>();
	    
	    try {	    
			connection = DbBean.getConnection();
//			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE CUSTNO = '"+custno+"' AND PROJECT_STATUS = 'OPEN' ";
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE CUSTNO = '"+custno+"' AND PROJECT_STATUS = 'OPEN' AND ( EXPIRY_DATE='' OR (SUBSTRING(EXPIRY_DATE,7,4) +SUBSTRING(EXPIRY_DATE,4,2)+SUBSTRING(EXPIRY_DATE,1,2)) >= (SUBSTRING('S"+expDate+"',7,4) +SUBSTRING('"+expDate+"',4,2)+SUBSTRING('"+expDate+"',1,2))) AND PROJECT_NAME LIKE '%"+projectname+"%'";
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   FinProject finProject=new FinProject();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, finProject);
	                   FinProjectList.add(finProject);
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
		return FinProjectList;
	}  
	
	public boolean insertFinProject(FinProject finProject) throws Exception {
		boolean insertFlag = false;
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+finProject.getPLANT()+"_"+TABLE_HEADER+"]" + 
					"           ([PLANT]" + 
					"           ,[CUSTNO]" + 
					"           ,[PROJECT]" + 
					"           ,[PROJECT_NAME]" + 
					"           ,[PROJECT_DATE]" + 
					"           ,[EXPIRY_DATE]" + 
					"           ,[PROJECT_STATUS]" + 
					"           ,[REFERENCE]" + 
					"           ,[ESTIMATE_COST]" + 
					"           ,[ESTIMATE_TIME]" + 
					"           ,[BILLING_OPTION]" + 
					"           ,[NOTE]" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]" + 
					"           ,[UPAT]" + 
					"           ,[MANDAY_HOUR]" + 
					"           ,[ISMANDAY_HOUR]" + 
					"           ,[UPBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
			   ps = connection.prepareStatement(query);
			   ps.setString(1, finProject.getPLANT());
			   ps.setString(2, finProject.getCUSTNO());
			   ps.setString(3, finProject.getPROJECT());
			   ps.setString(4, finProject.getPROJECT_NAME());
			   ps.setString(5, finProject.getPROJECT_DATE());
			   ps.setString(6, finProject.getEXPIRY_DATE());
			   ps.setString(7, finProject.getPROJECT_STATUS());
			   ps.setString(8, finProject.getREFERENCE());
			   ps.setString(9, finProject.getESTIMATE_COST());
			   ps.setString(10, finProject.getESTIMATE_TIME());
			   ps.setString(11, finProject.getBILLING_OPTION());
			   ps.setString(12, finProject.getNOTE());
			   ps.setString(13, finProject.getCRAT());
			   ps.setString(14, finProject.getCRBY());
			   ps.setString(15, finProject.getUPAT());
			   ps.setDouble(16, finProject.getMANDAY_HOUR());
			   ps.setShort(17, finProject.getISMANDAY_HOUR());
			   ps.setString(18, finProject.getUPBY());
			 
			  
			   int count=ps.executeUpdate();
			   if(count>0)
			   {
				   insertFlag = true;
			   }
			   else
			   {
				   throw new SQLException("Creating Project failed, no rows affected.");
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
		return insertFlag;
	}
	
	 public boolean updatefinProject(FinProject finProject) throws Exception {
			boolean updateFlag = false;
			boolean flag = false;
			int HdrId = 0;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
			try {	    
				connection = DbBean.getConnection();
				query = "UPDATE ["+finProject.getPLANT()+"_"+TABLE_HEADER+"] SET" + 
						"           [PLANT] = ?" + 
						"           ,[CUSTNO] = ?" + 
						"           ,[PROJECT] = ?" + 
						"           ,[PROJECT_NAME] = ?" + 
						"           ,[PROJECT_DATE] = ?" + 
						"           ,[EXPIRY_DATE] = ?" + 
						"           ,[PROJECT_STATUS] = ?" + 
						"           ,[REFERENCE] = ?" + 
						"           ,[ESTIMATE_COST] = ?" + 
						"           ,[ESTIMATE_TIME] = ?" + 
						"           ,[BILLING_OPTION] = ?" + 
						"           ,[NOTE] = ?" + 
						"           ,[CRAT] = ?" + 
						"           ,[CRBY] = ?" + 
						"           ,[UPAT] = ?" + 
						"           ,[MANDAY_HOUR] = ?" + 
						"           ,[ISMANDAY_HOUR] = ?" + 
						"           ,[UPBY] = ? WHERE [PROJECT] = ?";
					
						
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, finProject.getPLANT());
				   ps.setString(2, finProject.getCUSTNO());
				   ps.setString(3, finProject.getPROJECT());
				   ps.setString(4, finProject.getPROJECT_NAME());
				   ps.setString(5, finProject.getPROJECT_DATE());
				   ps.setString(6, finProject.getEXPIRY_DATE());
				   ps.setString(7, finProject.getPROJECT_STATUS());
				   ps.setString(8, finProject.getREFERENCE());
				   ps.setString(9, finProject.getESTIMATE_COST());
				   ps.setString(10, finProject.getESTIMATE_TIME());
				   ps.setString(11, finProject.getBILLING_OPTION());
				   ps.setString(12, finProject.getNOTE());
				   ps.setString(13, finProject.getCRAT());
				   ps.setString(14, finProject.getCRBY());
				   ps.setString(15, finProject.getUPAT());
				   ps.setDouble(16, finProject.getMANDAY_HOUR());
				   ps.setShort(17, finProject.getISMANDAY_HOUR());
				   ps.setString(18, finProject.getUPBY());
				  
				   ps.setString(19, finProject.getPROJECT());
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   updateFlag = true;
				   }
				   else
				   {
					   throw new SQLException("Creating Project failed, no rows affected.");
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
			return updateFlag;
		}
	 
		public boolean isExisit(Hashtable ht) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" ");
				sql.append(" FROM " + "["+ht.get("PLANT")+"_FINPROJECT]");
				sql.append(" WHERE  " + formCondition(ht));

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
		public boolean isExisit(Hashtable ht, String extCond) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" ");
				sql.append(" FROM " + "["+ht.get("PLANT")+"_FINPROJECT]");
				sql.append(" WHERE  " + formCondition(ht));

				if (extCond.length() > 0)
					sql.append(" and " + extCond);

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
		
		//imti delete
		public boolean deleteFinProject(String plant,String project)  throws Exception {
			boolean delete = false;
			java.sql.Connection con = null;
			try {
				con = DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" DELETE ");
				sql.append(" ");
				sql.append(" FROM " + "[" + plant + "_FINPROJECT]");
				sql.append(" WHERE PROJECT = '" +project+"'");
				this.mLogger.query(this.printQuery, sql.toString());
				delete = updateData(con, sql.toString());
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return delete;
		}
		//imti delete end
		
		public List<FinProject> getProject(Hashtable ht, String afrmDate, String atoDate,String viewstatus) throws Exception {
			   boolean flag = false;
			   int journalHdrId = 0;
			   Connection connection = null;
			   PreparedStatement ps = null;
			   String query = "", fields = "", dtCondStr = "";
			   List<FinProject> doHeaders = new ArrayList<FinProject>();
			   List<String> args = null;
			   try {
				   args = new ArrayList<String>();
				   connection = DbBean.getConnection();
				   query = "SELECT [PLANT]" + 
							"      ,[ID]" + 
							"      ,[CUSTNO]" + 
							"      ,[PROJECT]" + 
							"      ,[PROJECT_NAME] " + 
							"      ,[PROJECT_DATE]" + 
							"      ,[EXPIRY_DATE] " + 
							"      ,[PROJECT_STATUS] " + 
							"      ,[REFERENCE] " + 
							"      ,[ESTIMATE_COST]" + 
							"      ,[ESTIMATE_TIME]" + 
							"      ,[BILLING_OPTION] " + 
							"      ,[ISPERHOURWAGES] " + 
							"      ,[PERHOURWAGESCOST]" + 
							"      ,[NOTE] " + 
							"      ,[CRAT] " + 
							"      ,[CRBY] " + 
							"      ,[UPAT] " + 
							"      ,[UPBY] " + 
							"      ,ISNULL([MANDAY_HOUR],0) MANDAY_HOUR" + 
							"      ,ISNULL([ISMANDAY_HOUR],0) ISMANDAY_HOUR " + 
							"      ,ISNULL(PROJECT_STATUS,'Open') PROJECT_STATUS FROM ["+ ht.get("PLANT") +"_"+TABLE_HEADER+"] WHERE ";
				   Enumeration enum1 = ht.keys();
					for (int i = 0; i < ht.size(); i++) {
						String key = _StrUtils.fString((String) enum1.nextElement());
						String value = _StrUtils.fString((String) ht.get(key));
						fields += key + "=? AND ";
						args.add(value);
					}			
					query += fields.substring(0, fields.length() - 5);
					if(viewstatus.equalsIgnoreCase("ByProjectDate"))
						dtCondStr =" AND ISNULL(PROJECT_DATE,'')<>'' AND CAST((SUBSTRING(PROJECT_DATE, 7, 4) + '-' + SUBSTRING(PROJECT_DATE, 4, 2) + '-' + SUBSTRING(PROJECT_DATE, 1, 2)) AS date)";
					else
						dtCondStr =" AND ISNULL(EXPIRY_DATE,'')<>'' AND CAST((SUBSTRING(EXPIRY_DATE, 7, 4) + '-' + SUBSTRING(EXPIRY_DATE, 4, 2) + '-' + SUBSTRING(EXPIRY_DATE, 1, 2)) AS date)";
					if (afrmDate.length() > 0) {
						query +=  dtCondStr + "  >= '" + afrmDate + "' ";
		  				if (atoDate.length() > 0) {
		  					query += dtCondStr+ " <= '" + atoDate + "' ";
		  				}
					} else {
		  				if (atoDate.length() > 0) {
		  					query += dtCondStr+ " <= '" + atoDate + "' ";
		  				}
					}
					
					query += " ORDER BY  CAST((SUBSTRING(PROJECT_DATE,7,4) + '-' + SUBSTRING(PROJECT_DATE,4,2) + '-' + SUBSTRING(PROJECT_DATE,1,2))AS DATE) DESC,PROJECT DESC";
					ps = connection.prepareStatement(query);
					if(connection != null){
						int i = 1;
						for (Object arg : args) {         
						    if (arg instanceof Integer) {
						        ps.setInt(i++, (Integer) arg);
						    } else if (arg instanceof Long) {
						        ps.setLong(i++, (Long) arg);
						    } else if (arg instanceof Double) {
						        ps.setDouble(i++, (Double) arg);
						    } else if (arg instanceof Float) {
						        ps.setFloat(i++, (Float) arg);
						    } else {
						        ps.setNString(i++, (String) arg);
						    }
						}
					    /* Execute the Query */  
						ResultSet rst  = ps.executeQuery();
						while (rst.next()) {	
							FinProject finProject = new FinProject();
							ResultSetToObjectMap.loadResultSetIntoObject(rst, finProject);
							doHeaders.add(finProject);
						}
					}			
			   }catch (Exception e) {
				   this.mLogger.exception(this.printLog, "", e);
					throw e;
			   } finally {
					if (connection != null) {
						DbBean.closeConnection(connection);
					}
				}
			   return doHeaders;
			}
		
		public FinProject getprojectByIdname(String plant,String project)throws Exception {
			boolean flag = false;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
		    FinProject finProject = new FinProject();
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT [PLANT]" + 
						"      ,[ID]" + 
						"      ,[CUSTNO]" + 
						"      ,[PROJECT]" + 
						"      ,[PROJECT_NAME] " + 
						"      ,[PROJECT_DATE]" + 
						"      ,[EXPIRY_DATE] " + 
						"      ,[PROJECT_STATUS] " + 
						"      ,[REFERENCE] " + 
						"      ,[ESTIMATE_COST]" + 
						"      ,[ESTIMATE_TIME]" + 
						"      ,[BILLING_OPTION] " +
						"      ,[ISPERHOURWAGES] " + 
						"      ,[PERHOURWAGESCOST]" + 
						"      ,[NOTE] " + 
						"      ,[CRAT] " + 
						"      ,[CRBY] " + 
						"      ,[UPAT] " + 
						"      ,ISNULL([MANDAY_HOUR],0) MANDAY_HOUR" + 
						"      ,ISNULL([ISMANDAY_HOUR],0) ISMANDAY_HOUR " + 
						"      ,[UPBY] " + 
				   		"      ,ISNULL(PROJECT_STATUS,'Open') PROJECT_STATUS FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE "+
				   		" PROJECT=? AND PLANT=?";

				if(connection != null){
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   ps.setString(1, project);
					   ps.setString(2, plant);
					   ResultSet rst = ps.executeQuery();
					   while (rst.next()) {
		                    ResultSetToObjectMap.loadResultSetIntoObject(rst, finProject);
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
			return finProject;
		}
		public ArrayList selectFinProject(String query, Hashtable ht, String extCond)
				throws Exception {
//			boolean flag = false;
			ArrayList alData = new ArrayList();
			
			String plant = (String) ht.get("PLANT");

			java.sql.Connection con = null;

			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ plant + "_" + "FINPROJECT" + "] D ");
			String conditon = "";

			try {
				con = com.track.gates.DbBean.getConnection();

				if (ht.size() > 0) {

					sql.append(" WHERE ");

					conditon = formCondition(ht);

					sql.append(conditon);

				}
				if (extCond.length() > 0)
					sql.append(" " + extCond);
				this.mLogger.query(this.printQuery, sql.toString());

				alData = selectData(con, sql.toString());

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return alData;
		}
		

public boolean isExistProject(String project, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean isExists = false;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " + "[" +plant+"_FINPROJECT"
				+ "]" + " WHERE " + IConstants.PROJECTNAME + " = '"
				+ project.toUpperCase() + "'";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) > 0)
				isExists = true;
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return isExists;
}
	
}
