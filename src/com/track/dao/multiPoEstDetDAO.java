package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.object.CostofgoodsLanded;
import com.track.db.object.DoDet;
import com.track.db.object.HrEmpType;
import com.track.db.object.MultiPoEstHdr; //Resvi
import com.track.db.object.PoDet;
import com.track.db.object.MultiPoEstDetRemarks ;
import com.track.db.object.MultiPoEstDet; //Resvi
import com.track.db.util.PlantMstUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class multiPoEstDetDAO extends BaseDAO {

	public static String TABLE_NAME = "PO_MULTI_ESTDET";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.POMULTIDETDETDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.POMULTIDETDETDAO_PRINTPLANTMASTERLOG;

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

	public multiPoEstDetDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "PO_MULTI_ESTDET" + "]";
	}

	public multiPoEstDetDAO() {

	}

	public boolean isExisit(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_PO_MULTI_ESTDET]");
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

	public boolean isExisit(String query) throws Exception {
		boolean flag = false;

		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			this.mLogger.query(this.printQuery, query);

			flag = isExists(con, query);
			TABLE_NAME = "PO_MULTI_ESTDET";
		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return flag;

	}
    public boolean delete(Hashtable ht) throws Exception {
            boolean delete = false;
            java.sql.Connection con = null;
            try {
                    con = DbBean.getConnection();
                    StringBuffer sql = new StringBuffer(" DELETE ");
                    sql.append(" ");
                    sql.append(" FROM " + "[" + ht.get("PLANT") + "_PO_MULTI_ESTDET]");
                    sql.append(" WHERE " + formCondition(ht));
                    this.mLogger.query(true, sql.toString());
                    
                    delete = updateData(con, sql.toString());
                    
            } catch (Exception e) {
                    this.mLogger.exception(true, "", e);
                    throw e;
            } finally {
                    if (con != null) {
                            DbBean.closeConnection(con);
                    }
            }
            return delete;
    }
	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;
		try {
			TABLE_NAME="PO_MULTI_ESTDET";
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+"["+ht.get(IDBConstants.PLANT)+"_PO_MULTI_ESTDET]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			this.mLogger.query(this.printQuery, sql.toString());
			map = getRowOfData(con, sql.toString());
			TABLE_NAME="";
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
        
   

	public ArrayList selectPoDet(String query, Hashtable ht) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		TABLE_NAME = "PO_MULTI_ESTDET";
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
				+"["+ht.get(IDBConstants.PLANT)+"_"+ TABLE_NAME+"]");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			this.mLogger.query(this.printQuery, sql.toString());
			alData = selectData(con, sql.toString());
			TABLE_NAME = "PO_MULTI_ESTDET";
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

	public ArrayList selectPoDet(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_PO_MULTI_ESTDET]");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
			this.mLogger.query(this.printQuery, sql.toString());

			alData = selectData(con, sql.toString());
			TABLE_NAME = "PO_MULTI_ESTDET";
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
	public ArrayList selectPoDetForPage(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(query);
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
			this.mLogger.query(this.printQuery, sql.toString());

			alData = selectData(con, sql.toString());
			TABLE_NAME = "PO_MULTI_ESTDET";
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
	public ArrayList selectItemForPDA(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(query);
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
			this.mLogger.query(this.printQuery, sql.toString());

			alData = selectData(con, sql.toString());
			TABLE_NAME = "ITEMMST";
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
	public ArrayList selectPoDet(String query, Hashtable ht, String extCond,
			String plant) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ plant + "_" + "PO_MULTI_ESTDET" + "] a ");
		String conditon = "";
		try {
			con = com.track.gates.DbBean.getConnection();
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);

				sql.append(conditon);

			}
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
			this.mLogger.query(this.printQuery, sql.toString());

			alData = selectData(con, sql.toString());
			TABLE_NAME = "PO_MULTI_ESTDET";
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

	public ArrayList selectReversePoDet(String query, Hashtable ht,
			String extCond, String plant) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ plant + "_" + "PO_MULTI_ESTDET" + "]a,[" + plant + "_" + "RECVDET"
				+ "] b");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

				sql.append(" WHERE ");

				conditon = formCondition(ht);

				sql.append(conditon);

			}
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
			this.mLogger.query(this.printQuery, sql.toString());

			alData = selectData(con, sql.toString());
			TABLE_NAME = "PO_MULTI_ESTDET";
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

	public boolean insertPoDet(Hashtable ht) throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _StrUtils.fString((String) enum1.nextElement());
				String value = _StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_PO_MULTI_ESTDET ]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);
			TABLE_NAME = "PO_MULTI_ESTDET";
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}
	
	public Boolean removeOrderDetails(String plant, String POMULTIESTNO)
			throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant + "_" + "PO_MULTI_ESTDET] WHERE POMULTIESTNO='"
					+ POMULTIESTNO + "' AND PLANT='" + plant + "'";
			this.mLogger.query(this.printQuery, sql.toString());
			this.DeleteRow(connection, sql);
			return Boolean.valueOf(true);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			return Boolean.valueOf(false);
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
	}
	
	
	public boolean isExisitPoMultiRemarks(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_PO_MULTI_ESTDET_REMARKS]");
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
 
	
	  public boolean addPoDetRemarks(MultiPoEstDetRemarks  multiPoEstDetRemarks ) throws Exception {
	 		boolean insertFlag = false;
	 		boolean flag = false;
	 		int HdrId = 0;
	 		Connection connection = null;
	 		PreparedStatement ps = null;
	 	    String query = "";
	 		try {	    
	 			connection = DbBean.getConnection();
	 			query = "INSERT INTO ["+multiPoEstDetRemarks .getPLANT()+"_PO_MULTI_ESTDET_REMARKS]" + 
	 					"           ([PLANT]" + 
	 					"           ,[POMULTIESTNO]" + 
	 					"           ,[POMULTIESTLNNO]" + 
	 					"           ,[ITEM]" + 
	 					"           ,[REMARKS]" + 
	 					"           ,[CRAT]" +
	 					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?)";

	 			if(connection != null){
		 			ps = connection.prepareStatement(query);
		 			ps.setString(1, multiPoEstDetRemarks .getPLANT());
		 			ps.setString(2, multiPoEstDetRemarks.getPOMULTIESTNO());
		 			ps.setInt(3, multiPoEstDetRemarks.getPOMULTIESTLNNO());
		 			ps.setString(4, multiPoEstDetRemarks.getITEM());
		 			ps.setString(5, multiPoEstDetRemarks.getREMARKS());
		 			ps.setString(6, multiPoEstDetRemarks.getCRAT());
		 			ps.setString(7, multiPoEstDetRemarks.getCRBY());
		 			
		 			
	 			   int count=ps.executeUpdate();
	 			   if(count>0)
	 			   {
	 				   insertFlag = true;
	 			   }
	 			   else
	 			   {
	 				   throw new SQLException("Creating Purchase Order remarks failed, no rows affected.");
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
	  
		public boolean updatepo(String query, Hashtable htCondition, String extCond)
				throws Exception {
			boolean flag = false;
			String TABLE = "PO_MULTI_ESTDET";
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" UPDATE " + "["
						+ htCondition.get("PLANT") + "_" + TABLE + "]");
				sql.append(" ");
				sql.append(query);
				sql.append(" WHERE ");

				String conditon = formCondition(htCondition);

				sql.append(conditon);

				if (extCond.length() != 0) {
					sql.append(extCond);
				}
				this.mLogger.query(this.printQuery, sql.toString());

				flag = updateData(con, sql.toString());

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				DbBean.closeConnection(con);
			}
			return flag;
		  }
	
	  public boolean deletePoMultiRemarks(Hashtable ht) throws Exception {
	         boolean delete = false;
	         java.sql.Connection con = null;
	         try {
	                 con = DbBean.getConnection();
	                 StringBuffer sql = new StringBuffer(" DELETE ");
	                 sql.append(" ");
	                 sql.append(" FROM " + "[" + ht.get("PLANT") + "_PO_MULTI_ESTDET_REMARKS]");
	                 sql.append(" WHERE " + formCondition(ht));
	                 this.mLogger.query(true, sql.toString());
	                 
	                 delete = updateData(con, sql.toString());
	                 
	         } catch (Exception e) {
	                 this.mLogger.exception(true, "", e);
	                 throw e;
	         } finally {
	                 if (con != null) {
	                         DbBean.closeConnection(con);
	                 }
	         }
	         return delete;
	 }

	 public boolean updatePoDetRemarks(List<MultiPoEstDetRemarks> multiPoEstDetRemarkslist) throws Exception {
			boolean updateFlag = false;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
			try {
				connection = DbBean.getConnection();
				
				for(MultiPoEstDetRemarks multiPoEstDetRemarks : multiPoEstDetRemarkslist) {
					query = "UPDATE ["+multiPoEstDetRemarks.getPLANT()+"_PODET_REMARKS] SET " + 
							"           [PLANT] = ?" + 
							"           ,[POMULTIESTNO] = ?" +  
							"           ,[POMULTIESTLNNO] = ?" +  
							"           ,[ITEM] = ?" +  
							"           ,[REMARKS] = ?" +   
							"           ,[UPAT] = ?" +  
							"           ,[UPBY] = ?" +  
							"     WHERE POMULTIESTNO = ? AND POMULTIESTLNNO = ? ";
					if(connection != null){
					   ps = connection.prepareStatement(query);
					   ps.setString(1, multiPoEstDetRemarks.getPLANT());
			 			ps.setString(2, multiPoEstDetRemarks.getPOMULTIESTNO());
			 			ps.setInt(3, multiPoEstDetRemarks.getPOMULTIESTLNNO());
			 			ps.setString(4, multiPoEstDetRemarks.getITEM());
			 			ps.setString(5, multiPoEstDetRemarks.getREMARKS());
			 			ps.setString(6, multiPoEstDetRemarks.getUPAT());
			 			ps.setString(7, multiPoEstDetRemarks.getUPBY());
			 			ps.setString(8, multiPoEstDetRemarks.getPOMULTIESTNO());
			 			ps.setInt(9, multiPoEstDetRemarks.getPOMULTIESTLNNO());
			 			
			 			
					   int count=ps.executeUpdate();
					   if(count>0)
					   {
						   updateFlag = true;
					   }
					   else
					   {
						   throw new SQLException("Updating Purchase Order remarks failed, no rows affected.");
					   }
					   this.mLogger.query(this.printQuery, query);		
					}
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw new Exception("Item already added to out going order");
			} finally {
				if (connection != null) {
					DbBean.closeConnection(connection);
				}
			}
			return updateFlag;
		}
	  
	 
	  public  List<MultiPoEstDetRemarks>  GetmultiPoEstDetRemarksbyitems(String plant, String POMULTIESTNO, String POMULTIESTLNNO, String item)throws Exception {
			Connection connection = null;
			PreparedStatement ps = null;
			boolean status = false;
		    String query = "";
		    List<MultiPoEstDetRemarks> multiPoEstDetRemarkslist = new ArrayList<MultiPoEstDetRemarks>();
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT * FROM ["+ plant +"_PO_MULTI_ESTDET_REMARKS] WHERE PLANT='"+plant+"' AND POMULTIESTNO='"+POMULTIESTNO+"' AND POMULTIESTLNNO='"+POMULTIESTLNNO+"' AND ITEM='"+item+"'";

				if(connection != null){
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   ResultSet rst = ps.executeQuery();
					   while (rst.next()) {
						   MultiPoEstDetRemarks multiPoEstDetRemarks=new MultiPoEstDetRemarks();
		                   ResultSetToObjectMap.loadResultSetIntoObject(rst, multiPoEstDetRemarks);
		                   multiPoEstDetRemarkslist.add(multiPoEstDetRemarks);
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
			return multiPoEstDetRemarkslist;
		}
	  
	  
	  public boolean IsExistPoDetRemarks(String plant, String POMULTIESTNO, String polno, String item)throws Exception {
			Connection connection = null;
			PreparedStatement ps = null;
			boolean status = false;
		    String query = "";
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT * FROM ["+ plant +"_PO_MULTI_ESTDET_REMARKS] WHERE PLANT='"+plant+"' AND POMULTIESTNO='"+POMULTIESTNO+"' AND POMULTIESTLNNO='"+polno+"' AND ITEM='"+item+"'";

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
	  
	  
	  public  List<MultiPoEstDetRemarks>  GetPoDetRemarksbyitems(String plant, String POMULTIESTNO, String polno, String item)throws Exception {
			Connection connection = null;
			PreparedStatement ps = null;
			boolean status = false;
		    String query = "";
		    List<MultiPoEstDetRemarks> multiPoEstDetRemarkslist = new ArrayList<MultiPoEstDetRemarks>();
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT * FROM ["+ plant +"_PO_MULTI_ESTDET_REMARKS] WHERE PLANT='"+plant+"' AND POMULTIESTNO='"+POMULTIESTNO+"' AND POMULTIESTLNNO='"+polno+"' AND ITEM='"+item+"'";

				if(connection != null){
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   ResultSet rst = ps.executeQuery();
					   while (rst.next()) {
						   MultiPoEstDetRemarks multiPoEstDetRemarks=new MultiPoEstDetRemarks();
		                   ResultSetToObjectMap.loadResultSetIntoObject(rst, multiPoEstDetRemarks);
		                   multiPoEstDetRemarkslist.add(multiPoEstDetRemarks);
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
			return multiPoEstDetRemarkslist;
		}
	  
	  
	  public boolean DeletePoDetRemarks(String plant, int id)
		        throws Exception {
				boolean deletestatus = false;
				PreparedStatement ps = null;
				Connection con = null;
				try {
				        con = DbBean.getConnection();
				        
				        
				        String sQry = "DELETE FROM " + "[" + plant +"_PO_MULTI_ESTDET_REMARKS]"
				                        + " WHERE ID_REMARKS ='"+id+"'";
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
	
	  
	 public boolean insertPoMultiRemarks(Hashtable ht) throws Exception {
			boolean insertFlag = false;
			java.sql.Connection conn = null;
			try {
				conn = DbBean.getConnection();
				String FIELDS = "", VALUES = "";
				Enumeration enum1 = ht.keys();
				for (int i = 0; i < ht.size(); i++) {
					String key = _StrUtils.fString((String) enum1.nextElement());
					String value = _StrUtils.fString((String) ht.get(key));
					FIELDS += key + ",";
					VALUES += "'" + value + "',";
				}
				String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_PO_MULTI_ESTDET_REMARKS]" + "("
						+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
						+ VALUES.substring(0, VALUES.length() - 1) + ")";

				this.mLogger.query(this.printQuery, query);

				insertFlag = insertData(conn, query);
				TABLE_NAME = "PO_MULTI_ESTDET";
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (conn != null) {
					DbBean.closeConnection(conn);
				}
			}
			return insertFlag;
		}
	 
	public boolean update(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;

		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "[" + htCondition.get("PLANT") + "_"+ "PO_MULTI_ESTDET" + "]");
			sql.append(query);
			sql.append(" WHERE ");

			String conditon = formCondition(htCondition);

			sql.append(conditon);

			if (extCond.length() != 0) {
				sql.append(extCond);
			}
			this.mLogger.query(this.printQuery, sql.toString());

			flag = updateData(con, sql.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return flag;

	}

	
	public MultiPoEstDet getPoDetByMultiEstPonoPllno(String plant, String POMULTIESTNO, int poline) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    MultiPoEstDet multiPoEstDet=new MultiPoEstDet();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,POMULTIESTNO,POMULTIESTLNNO,LNSTAT,ITEM,ItemDesc,TRANDATE,UNITCOST,QTYOR,QTYRC,UNITMO,USERFLD1,USERFLD2,USERFLD3,USERFLD4,CURRENCYUSEQT,PRODGST,PRODUCTDELIVERYDATE,DISCOUNT,ISNULL(DISCOUNT_TYPE,'') AS DISCOUNT_TYPE,ISNULL(ACCOUNT_NAME,'') AS ACCOUNT_NAME,ISNULL(EXPIREDATE,'') EXPIREDATE,ISNULL(TAX_TYPE,'') AS TAX_TYPE,ISNULL(COMMENT1,'') AS COMMENT1,ISNULL(UNITCOST_AOD,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST_AOD,CRAT,CRBY,UPAT,UPBY,CustCode,CustName,CURRENCYID,TAXTREATMENT\r\n" + 
					" FROM ["+ plant +"_PO_MULTI_ESTDET] WHERE POMULTIESTNO='"+POMULTIESTNO+"' AND POMULTIESTLNNO='"+poline+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, multiPoEstDet);
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
		return multiPoEstDet;
	}
	
	
	  public boolean updateMultiPoEstDetRemarksBYID(List<MultiPoEstDetRemarks> MultipoEstDetRemarkslist) throws Exception {
			boolean updateFlag = false;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
			try {
				connection = DbBean.getConnection();
				
				for(MultiPoEstDetRemarks multiPoEstDetRemarks : MultipoEstDetRemarkslist) {
					query = "UPDATE ["+multiPoEstDetRemarks.getPLANT()+"_PO_MULTI_ESTDET_REMARKS] SET " + 
							"           [PLANT] = ?" + 
							"           ,[POMULTIESTNO] = ?" +  
							"           ,[POMULTIESTLNNO] = ?" +  
							"           ,[ITEM] = ?" +  
							"           ,[REMARKS] = ?" +   
							"           ,[UPAT] = ?" +  
							"           ,[UPBY] = ?" +  
							"     WHERE ID_REMARKS = ? ";
					if(connection != null){
					   ps = connection.prepareStatement(query);
					   ps.setString(1, multiPoEstDetRemarks.getPLANT());
			 			ps.setString(2, multiPoEstDetRemarks.getPOMULTIESTNO());
			 			ps.setInt(3, multiPoEstDetRemarks.getPOMULTIESTLNNO());
			 			ps.setString(4, multiPoEstDetRemarks.getITEM());
			 			ps.setString(5, multiPoEstDetRemarks.getREMARKS());
			 			ps.setString(6, multiPoEstDetRemarks.getUPAT());
			 			ps.setString(7, multiPoEstDetRemarks.getUPBY());
			 			ps.setInt(8, multiPoEstDetRemarks.getID_REMARKS());
			 			
					   int count=ps.executeUpdate();
					   if(count>0)
					   {
						   updateFlag = true;
					   }
					   else
					   {
						   throw new SQLException("Updating Purchase Order remarks failed, no rows affected.");
					   }
					   this.mLogger.query(this.printQuery, query);		
					}
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw new Exception("Item already added to out going order");
			} finally {
				if (connection != null) {
					DbBean.closeConnection(connection);
				}
			}
			return updateFlag;
		}
	
	 public List<MultiPoEstDetRemarks> getPoDetRemarksByPono(String plant, String POMULTIESTNO) throws Exception {
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
		    List<MultiPoEstDetRemarks> multiPoEstDetRemarkslist = new ArrayList<MultiPoEstDetRemarks>();
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT * FROM ["+ plant +"_PO_MULTI_ESTDET_REMARKS] WHERE POMULTIESTNO='"+POMULTIESTNO+"'";

				if(connection != null){
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   ResultSet rst = ps.executeQuery();
					   while (rst.next()) {
						   MultiPoEstDetRemarks multiPoEstDetRemarks=new MultiPoEstDetRemarks();
		                   ResultSetToObjectMap.loadResultSetIntoObject(rst, multiPoEstDetRemarks);
		                   multiPoEstDetRemarkslist.add(multiPoEstDetRemarks);
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
			return multiPoEstDetRemarkslist;
		}
	

    public boolean isExisit(Hashtable ht, String extCond) throws Exception {
            boolean flag = false;
            java.sql.Connection con = null;
            try {
                    con = com.track.gates.DbBean.getConnection();

                    StringBuffer sql = new StringBuffer(" SELECT ");
                    sql.append("COUNT(*) ");
                    sql.append(" ");
                    sql.append(" FROM "  + "[" + ht.get("PLANT") + "_"+ "PO_MULTI_ESTDET" + "]");
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
	public ArrayList getInboundOrderDetailsByWMS(String plant, String orderno)
			throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;

			String sQry = "select POMULTIESTNO,POMULTIESTLNNO,item,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,isnull(userfld2,'') as ref,isnull(userfld3,'') as custname from "
					+ "["
					+ plant
					+ "_"
					+ "multiPoEstDet"
					+ "]"
					+ " where plant='"
					+ plant
					+ "' and POMULTIESTNO like '"
					+ orderno
					+ "%' and lnstat <> 'C'";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);
			TABLE_NAME = "PO_MULTI_ESTDET";
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

	public MultiPoEstDet getPoDetByPonoPllno(String plant, String POMULTIESTNO, int poline) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    MultiPoEstDet multiPoEstDet=new MultiPoEstDet();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,POMULTIESTNO,POMULTIESTLNNO,LNSTAT,ITEM,ItemDesc,TRANDATE,UNITCOST,QTYOR,QTYRC,UNITMO,USERFLD1,USERFLD2,USERFLD3,USERFLD4,CURRENCYUSEQT,PRODGST,PRODUCTDELIVERYDATE,DISCOUNT,ISNULL(DISCOUNT_TYPE,'') AS DISCOUNT_TYPE,ISNULL(ACCOUNT_NAME,'') AS ACCOUNT_NAME,ISNULL(EXPIREDATE,'') EXPIREDATE,ISNULL(TAX_TYPE,'') AS TAX_TYPE,ISNULL(COMMENT1,'') AS COMMENT1,ISNULL(UNITCOST_AOD,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST_AOD,CRAT,CRBY,UPAT,UPBY,CustCode,CustName,CURRENCYID,TAXTREATMENT" + 
					" FROM ["+ plant +"_PO_MULTI_ESTDET] WHERE POMULTIESTNO='"+POMULTIESTNO+"' AND POMULTIESTLNNO='"+poline+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, multiPoEstDet);
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
		return multiPoEstDet;
	}
    
    public boolean addMultiPoEstDet(MultiPoEstDet multiPoEstDet) throws Exception {
 		boolean insertFlag = false;
 		boolean flag = false;
 		int HdrId = 0;
 		Connection connection = null;
 		PreparedStatement ps = null;
 	    String query = "";
 		try {	    
 			connection = DbBean.getConnection();
 			query = "INSERT INTO ["+multiPoEstDet.getPLANT()+"_PO_MULTI_ESTDET]" + 
 					"           ([PLANT]" + 
 					"           ,[POMULTIESTNO]" + 
 					"           ,[POMULTIESTLNNO]" + 
 					"           ,[LNSTAT]" + 
 					"           ,[ITEM]" + 
 					"           ,[ItemDesc]" + 
 					"           ,[TRANDATE]" + 
 					"           ,[UNITCOST]" + 
 					"           ,[QTYOR]" + 
 					"           ,[QTYRC]" + 
 					"           ,[UNITMO]" + 
 					"           ,[USERFLD1]" + 
 					"           ,[USERFLD2]" + 
 					"           ,[USERFLD3]" + 
 					"           ,[USERFLD4]" + 
 					"           ,[CURRENCYUSEQT]" + 
 					"           ,[PRODGST]" + 
 					"           ,[PRODUCTDELIVERYDATE]" + 
 					"           ,[COMMENT1]" + 
 					"           ,[CRAT]" +
 					"           ,[DISCOUNT]" + 
 					"           ,[DISCOUNT_TYPE]" + 
 					"           ,[ACCOUNT_NAME]" + 
 					"           ,[TAX_TYPE]" + 
 					"           ,[UNITCOST_AOD]" + 
 					"           ,[CRBY]"+
 					"           ,[CustCode]"+
 					"           ,[CustName]"+
 					"           ,[CURRENCYID]"+
 					"           ,[TAXTREATMENT]"+
 					"			,[EXPIREDATE]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

 			if(connection != null){
	 			ps = connection.prepareStatement(query);
	 			ps.setString(1, multiPoEstDet.getPLANT());
	 			ps.setString(2, multiPoEstDet.getPOMULTIESTNO());
	 			ps.setInt(3, multiPoEstDet.getPOMULTIESTLNNO());
	 			ps.setString(4, multiPoEstDet.getLNSTAT());
	 			ps.setString(5, multiPoEstDet.getITEM());
	 			ps.setString(6, multiPoEstDet.getItemDesc());
	 			ps.setString(7, multiPoEstDet.getTRANDATE());
	 			ps.setDouble(8, multiPoEstDet.getUNITCOST());
	 			ps.setBigDecimal(9, multiPoEstDet.getQTYOR());
	 			ps.setBigDecimal(10, multiPoEstDet.getQTYRC());
	 			ps.setString(11, multiPoEstDet.getUNITMO());
	 			ps.setString(12, multiPoEstDet.getUSERFLD1());
	 			ps.setString(13, multiPoEstDet.getUSERFLD2());
	 			ps.setString(14, multiPoEstDet.getUSERFLD3());
	 			ps.setString(15, multiPoEstDet.getUSERFLD4());
	 			ps.setDouble(16, multiPoEstDet.getCURRENCYUSEQT());
	 			ps.setDouble(17, multiPoEstDet.getPRODGST());
	 			ps.setString(18, multiPoEstDet.getPRODUCTDELIVERYDATE());
	 			ps.setString(19, multiPoEstDet.getCOMMENT1());
	 			ps.setString(20, multiPoEstDet.getCRAT());
	 			ps.setDouble(21, multiPoEstDet.getDISCOUNT());
	 			ps.setString(22, multiPoEstDet.getDISCOUNT_TYPE());
	 			ps.setString(23, multiPoEstDet.getACCOUNT_NAME());
	 			ps.setString(24, multiPoEstDet.getTAX_TYPE());
	 			ps.setDouble(25, multiPoEstDet.getUNITCOST_AOD());
	 			ps.setString(26, multiPoEstDet.getCRBY());
	 			ps.setString(27, multiPoEstDet.getCustCode());
	 			ps.setString(28, multiPoEstDet.getCustName());
	 			ps.setString(29, multiPoEstDet.getCURRENCYID());
	 			ps.setString(30, multiPoEstDet.getTAXTREATMENT());
	 			ps.setString(31, multiPoEstDet.getEXPIREDATE());


 			   int count=ps.executeUpdate();
 			   if(count>0)
 			   {
 				   insertFlag = true;
 			   }
 			   else
 			   {
 				   throw new SQLException("Creating Purchase Order detail failed, no rows affected.");
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
    
	public boolean updatePoDet(List<MultiPoEstDet> multiPoEstDetList) throws Exception {
		boolean updateFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			
			for(MultiPoEstDet multiPoEstDet : multiPoEstDetList) {
				query = "UPDATE ["+multiPoEstDet.getPLANT()+"_PO_MULTI_ESTDET] SET " + 
						"           [PLANT] = ?" + 
						"           ,[POMULTIESTNO] = ?" +  
						"           ,[POMULTIESTLNNO] = ?" +  
						"           ,[LNSTAT] = ?" +  
						"           ,[ITEM] = ?" +  
						"           ,[ItemDesc] = ?" +  
						"           ,[TRANDATE] = ?" +  
						"           ,[UNITCOST] = ?" +  
						"           ,[QTYOR] = ?" +  
						"           ,[QTYRC] = ?" +  
						"           ,[UNITMO] = ?" +  
						"           ,[USERFLD1] = ?" +  
						"           ,[USERFLD2] = ?" +  
						"           ,[USERFLD3] = ?" +  
						"           ,[USERFLD4] = ?" +  
						"           ,[CURRENCYUSEQT] = ?" +  
						"           ,[PRODGST] = ?" +  
						"           ,[PRODUCTDELIVERYDATE] = ?" +  
						"           ,[COMMENT1] = ?" +  
						"           ,[UPAT] = ?" +  
						"           ,[UPBY] = ?" +
						"           ,[DISCOUNT] = ?" +
						"           ,[DISCOUNT_TYPE] = ?" +
						"           ,[ACCOUNT_NAME] = ?" +
						"           ,[TAX_TYPE] = ?" +
						"           ,[CustCode] = ?" +
						"           ,[CustName] = ?" +
						"           ,[CURRENCYID] = ?" +
						"           ,[TAXTREATMENT] = ?" +

						"     ,[EXPIREDATE] = ? WHERE POMULTIESTNO = ? AND POMULTIESTLNNO = ? ";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, multiPoEstDet.getPLANT());
		 			ps.setString(2, multiPoEstDet.getPOMULTIESTNO());
		 			ps.setInt(3, multiPoEstDet.getPOMULTIESTLNNO());
		 			ps.setString(4, multiPoEstDet.getLNSTAT());
		 			ps.setString(5, multiPoEstDet.getITEM());
		 			ps.setString(6, multiPoEstDet.getItemDesc());
		 			ps.setString(7, multiPoEstDet.getTRANDATE());
		 			ps.setDouble(8, multiPoEstDet.getUNITCOST());
		 			ps.setBigDecimal(9, multiPoEstDet.getQTYOR());
		 			ps.setBigDecimal(10, multiPoEstDet.getQTYRC());
		 			ps.setString(11, multiPoEstDet.getUNITMO());
		 			ps.setString(12, multiPoEstDet.getUSERFLD1());
		 			ps.setString(13, multiPoEstDet.getUSERFLD2());
		 			ps.setString(14, multiPoEstDet.getUSERFLD3());
		 			ps.setString(15, multiPoEstDet.getUSERFLD4());
		 			ps.setDouble(16, multiPoEstDet.getCURRENCYUSEQT());
		 			ps.setDouble(17, multiPoEstDet.getPRODGST());
		 			ps.setString(18, multiPoEstDet.getPRODUCTDELIVERYDATE());
		 			ps.setString(19, multiPoEstDet.getCOMMENT1());
		 			ps.setString(20, multiPoEstDet.getUPAT());
		 			ps.setString(21, multiPoEstDet.getUPBY());
		 			ps.setDouble(22, multiPoEstDet.getDISCOUNT());
		 			ps.setString(23, multiPoEstDet.getDISCOUNT_TYPE());
		 			ps.setString(24, multiPoEstDet.getACCOUNT_NAME());
		 			ps.setString(25, multiPoEstDet.getTAX_TYPE());
		 			ps.setString(26, multiPoEstDet.getCustCode());
		 			ps.setString(27, multiPoEstDet.getCustName());
		 			ps.setString(28, multiPoEstDet.getCURRENCYID());
		 			ps.setString(29, multiPoEstDet.getTAXTREATMENT());
		 			ps.setString(30, multiPoEstDet.getEXPIREDATE());
		 			ps.setString(31, multiPoEstDet.getPOMULTIESTNO());
		 			ps.setInt(32, multiPoEstDet.getPOMULTIESTLNNO());
		 			
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   updateFlag = true;
				   }
				   else
				   {
					   throw new SQLException("Updating Purchase Order detail failed, no rows affected.");
				   }
				   this.mLogger.query(this.printQuery, query);		
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Item already added to out going order");
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return updateFlag;
	}
	
	public boolean isExisitPoEstMultiRemarks(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_PO_MULTI_ESTDET_REMARKS]");
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
	
	public boolean updatePoDetpo(MultiPoEstDet multiPoEstDet) throws Exception {
		boolean updateFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			

				query = "UPDATE ["+multiPoEstDet.getPLANT()+"_PO_MULTI_ESTDET] SET " + 
						"           [PLANT] = ?" + 
						"           ,[POMULTIESTNO] = ?" +  
						"           ,[POMULTIESTLNNO] = ?" +  
						"           ,[LNSTAT] = ?" +  
						"           ,[ITEM] = ?" +  
						"           ,[ItemDesc] = ?" +  
						"           ,[TRANDATE] = ?" +  
						"           ,[UNITCOST] = ?" +  
						"           ,[QTYOR] = ?" +  
						"           ,[QTYRC] = ?" +  
						"           ,[UNITMO] = ?" +  
						"           ,[USERFLD1] = ?" +  
						"           ,[USERFLD2] = ?" +  
						"           ,[USERFLD3] = ?" +  
						"           ,[USERFLD4] = ?" +  
						"           ,[CURRENCYUSEQT] = ?" +  
						"           ,[PRODGST] = ?" +  
						"           ,[PRODUCTDELIVERYDATE] = ?" +  
						"           ,[COMMENT1] = ?" +  
						"           ,[UPAT] = ?" +  
						"           ,[UPBY] = ?" +
						"           ,[DISCOUNT] = ?" +
						"           ,[DISCOUNT_TYPE] = ?" +
						"           ,[ACCOUNT_NAME] = ?" +
						"           ,[TAX_TYPE] = ?" +
						"           ,[CustCode] = ?" +
						"           ,[CustName] = ?" +
						"           ,[CURRENCYID] = ?" +
						"           ,[TAXTREATMENT] = ?" +
						"   		,[EXPIREDATE] = ?  WHERE POMULTIESTNO = ? AND POMULTIESTLNNO = ? ";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, multiPoEstDet.getPLANT());
		 			ps.setString(2, multiPoEstDet.getPOMULTIESTNO());
		 			ps.setInt(3, multiPoEstDet.getPOMULTIESTLNNO());
		 			ps.setString(4, multiPoEstDet.getLNSTAT());
		 			ps.setString(5, multiPoEstDet.getITEM());
		 			ps.setString(6, multiPoEstDet.getItemDesc());
		 			ps.setString(7, multiPoEstDet.getTRANDATE());
		 			ps.setDouble(8, multiPoEstDet.getUNITCOST());
		 			ps.setBigDecimal(9, multiPoEstDet.getQTYOR());
		 			ps.setBigDecimal(10, multiPoEstDet.getQTYRC());
		 			ps.setString(11, multiPoEstDet.getUNITMO());
		 			ps.setString(12, multiPoEstDet.getUSERFLD1());
		 			ps.setString(13, multiPoEstDet.getUSERFLD2());
		 			ps.setString(14, multiPoEstDet.getUSERFLD3());
		 			ps.setString(15, multiPoEstDet.getUSERFLD4());
		 			ps.setDouble(16, multiPoEstDet.getCURRENCYUSEQT());
		 			ps.setDouble(17, multiPoEstDet.getPRODGST());
		 			ps.setString(18, multiPoEstDet.getPRODUCTDELIVERYDATE());
		 			ps.setString(19, multiPoEstDet.getCOMMENT1());
		 			ps.setString(20, multiPoEstDet.getUPAT());
		 			ps.setString(21, multiPoEstDet.getUPBY());
		 			ps.setDouble(22, multiPoEstDet.getDISCOUNT());
		 			ps.setString(23, multiPoEstDet.getDISCOUNT_TYPE());
		 			ps.setString(24, multiPoEstDet.getACCOUNT_NAME());
		 			ps.setString(25, multiPoEstDet.getTAX_TYPE());
		 			ps.setString(26, multiPoEstDet.getCustCode());
		 			ps.setString(27, multiPoEstDet.getCustName());
		 			ps.setString(28, multiPoEstDet.getCURRENCYID());
		 			ps.setString(29, multiPoEstDet.getTAXTREATMENT());
		 			ps.setString(30, multiPoEstDet.getEXPIREDATE());
		 			ps.setString(31, multiPoEstDet.getPOMULTIESTNO());
		 			ps.setInt(32, multiPoEstDet.getPOMULTIESTLNNO());
		 			
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   updateFlag = true;
				   }
				   else
				   {
					   throw new SQLException("Updating Purchase Order detail failed, no rows affected.");
				   }
				   this.mLogger.query(this.printQuery, query);		
				}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Item already added to out going order");
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return updateFlag;
	}
	
	public boolean updatePoDetpoEdit(MultiPoEstDet multiPoEstDet,int POMULTIESTLNNO) throws Exception {
		boolean updateFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			

				query = "UPDATE ["+multiPoEstDet.getPLANT()+"_PO_MULTI_ESTDET] SET " + 
						"           [PLANT] = ?" + 
						"           ,[POMULTIESTNO] = ?" +  
						"           ,[POMULTIESTLNNO] = ?" +  
						"           ,[LNSTAT] = ?" +  
						"           ,[ITEM] = ?" +  
						"           ,[ItemDesc] = ?" +  
						"           ,[TRANDATE] = ?" +  
						"           ,[UNITCOST] = ?" +  
						"           ,[QTYOR] = ?" +  
						"           ,[QTYRC] = ?" +  
						"           ,[UNITMO] = ?" +  
						"           ,[USERFLD1] = ?" +  
						"           ,[USERFLD2] = ?" +  
						"           ,[USERFLD3] = ?" +  
						"           ,[USERFLD4] = ?" +  
						"           ,[CURRENCYUSEQT] = ?" +  
						"           ,[PRODGST] = ?" +  
						"           ,[PRODUCTDELIVERYDATE] = ?" +  
						"           ,[COMMENT1] = ?" +  
						"           ,[UPAT] = ?" +  
						"           ,[UPBY] = ?" +
						"           ,[DISCOUNT] = ?" +
						"           ,[DISCOUNT_TYPE] = ?" +
						"           ,[ACCOUNT_NAME] = ?" +
						"           ,[UNITCOST_AOD] = ?" +
						"           ,[TAX_TYPE] = ?" +		
						"           ,[CustCode] = ?" +
						"           ,[CustName] = ?" +
						"           ,[CURRENCYID] = ?" +
						"           ,[TAXTREATMENT] = ?" +
						"    ,[EXPIREDATE] = ? WHERE POMULTIESTNO = ? AND POMULTIESTLNNO = ? ";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, multiPoEstDet.getPLANT());
		 			ps.setString(2, multiPoEstDet.getPOMULTIESTNO());
		 			ps.setInt(3, multiPoEstDet.getPOMULTIESTLNNO());
		 			ps.setString(4, multiPoEstDet.getLNSTAT());
		 			ps.setString(5, multiPoEstDet.getITEM());
		 			ps.setString(6, multiPoEstDet.getItemDesc());
		 			ps.setString(7, multiPoEstDet.getTRANDATE());
		 			ps.setDouble(8, multiPoEstDet.getUNITCOST());
		 			ps.setBigDecimal(9, multiPoEstDet.getQTYOR());
		 			ps.setBigDecimal(10, multiPoEstDet.getQTYRC());
		 			ps.setString(11, multiPoEstDet.getUNITMO());
		 			ps.setString(12, multiPoEstDet.getUSERFLD1());
		 			ps.setString(13, multiPoEstDet.getUSERFLD2());
		 			ps.setString(14, multiPoEstDet.getUSERFLD3());
		 			ps.setString(15, multiPoEstDet.getUSERFLD4());
		 			ps.setDouble(16, multiPoEstDet.getCURRENCYUSEQT());
		 			ps.setDouble(17, multiPoEstDet.getPRODGST());
		 			ps.setString(18, multiPoEstDet.getPRODUCTDELIVERYDATE());
		 			ps.setString(19, multiPoEstDet.getCOMMENT1());
		 			ps.setString(20, multiPoEstDet.getUPAT());
		 			ps.setString(21, multiPoEstDet.getUPBY());
		 			ps.setDouble(22, multiPoEstDet.getDISCOUNT());
		 			ps.setString(23, multiPoEstDet.getDISCOUNT_TYPE());
		 			ps.setString(24, multiPoEstDet.getACCOUNT_NAME());
		 			ps.setDouble(25, multiPoEstDet.getUNITCOST_AOD());
		 			ps.setString(26, multiPoEstDet.getTAX_TYPE());
		 			ps.setString(27, multiPoEstDet.getCustCode());
		 			ps.setString(28, multiPoEstDet.getCustName());
		 			ps.setString(29, multiPoEstDet.getCURRENCYID());
		 			ps.setString(30, multiPoEstDet.getTAXTREATMENT());
		 			ps.setString(31, multiPoEstDet.getEXPIREDATE());
		 			ps.setString(32, multiPoEstDet.getPOMULTIESTNO());
		 			ps.setInt(33,POMULTIESTLNNO);
		 			
		 			
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   updateFlag = true;
				   }
				   else
				   {
					   throw new SQLException("Updating Purchase Order detail failed, no rows affected.");
				   }
				   this.mLogger.query(this.printQuery, query);		
				}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Item already added to out going order");
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return updateFlag;
	}
	
	
	
	
	public MultiPoEstDet getPoDetByPonoItem(String plant, String POMULTIESTNO, String item) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    MultiPoEstDet multiPoEstDet=new MultiPoEstDet();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT ID,PLANT,POMULTIESTNO,POMULTIESTLNNO,LNSTAT,ITEM,ItemDesc,TRANDATE,ISNULL(UNITCOST,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST,QTYOR,QTYRC,UNITMO,USERFLD1,USERFLD2,USERFLD3,USERFLD4,CURRENCYUSEQT,PRODGST,PRODUCTDELIVERYDATE, CASE WHEN ISNULL(DISCOUNT_TYPE,'0') = '%' THEN DISCOUNT ELSE (DISCOUNT * ISNULL(CURRENCYUSEQT,1))END AS DISCOUNT,ISNULL(DISCOUNT_TYPE,'') AS DISCOUNT_TYPE,ISNULL(ACCOUNT_NAME,'') AS ACCOUNT_NAME,ISNULL(EXPIREDATE,'') EXPIREDATE,ISNULL(TAX_TYPE,'') AS TAX_TYPE,ISNULL(UNITCOST_AOD,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST_AOD,ISNULL(COMMENT1,'') AS COMMENT1,CRAT,CRBY,UPAT,UPBY,CustCode,CustName,CURRENCYID,TAXTREATMENT\r\n" + 
					" FROM ["+ plant +"_PO_MULTI_ESTDET] WHERE POMULTIESTNO='"+POMULTIESTNO+"' AND ITEM='"+item+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, multiPoEstDet);
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
		return multiPoEstDet;
	}
	
	public List<MultiPoEstDet> getPoDetByPono(String plant, String POMULTIESTNO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<MultiPoEstDet> multiPoEstDetlist = new ArrayList<MultiPoEstDet>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,POMULTIESTNO,POMULTIESTLNNO,LNSTAT,ITEM,ItemDesc,TRANDATE,ISNULL(UNITCOST,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST,QTYOR,QTYRC,UNITMO,USERFLD1,USERFLD2,USERFLD3,USERFLD4,CURRENCYUSEQT,ISNULL(PRODGST,'0') PRODGST,PRODUCTDELIVERYDATE,CASE WHEN ISNULL(DISCOUNT_TYPE,'0') = '%' THEN ISNULL(DISCOUNT,'0') ELSE (ISNULL(DISCOUNT,'0') * ISNULL(CURRENCYUSEQT,1))END AS DISCOUNT,ISNULL(DISCOUNT_TYPE,'') AS DISCOUNT_TYPE,ISNULL(ACCOUNT_NAME,'') AS ACCOUNT_NAME,ISNULL(EXPIREDATE,'') EXPIREDATE,ISNULL(TAX_TYPE,'') AS TAX_TYPE,ISNULL(COMMENT1,'') AS COMMENT1,ISNULL(UNITCOST_AOD,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST_AOD,CRAT,CRBY,UPAT,UPBY,CustCode,CustName,CURRENCYID,TAXTREATMENT\r\n" + 
					" FROM ["+ plant +"_PO_MULTI_ESTDET] WHERE POMULTIESTNO='"+POMULTIESTNO+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   MultiPoEstDet multiPoEstDet=new MultiPoEstDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, multiPoEstDet);
	                   multiPoEstDetlist.add(multiPoEstDet);
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
		return multiPoEstDetlist;
	}
	
	public List<MultiPoEstDet> getPoDetByPOMULTIESTNO(String plant, String POMULTIESTNO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<MultiPoEstDet> multiPoEstDetlist = new ArrayList<MultiPoEstDet>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,POMULTIESTNO,POMULTIESTLNNO,LNSTAT,ITEM,ItemDesc,TRANDATE,ISNULL(UNITCOST,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST,QTYOR,QTYRC,UNITMO,USERFLD1,USERFLD2,USERFLD3,USERFLD4,CURRENCYUSEQT,ISNULL(PRODGST,'0') PRODGST,PRODUCTDELIVERYDATE,CASE WHEN ISNULL(DISCOUNT_TYPE,'0') = '%' THEN ISNULL(DISCOUNT,'0') ELSE (ISNULL(DISCOUNT,'0') * ISNULL(CURRENCYUSEQT,1))END AS DISCOUNT,ISNULL(DISCOUNT_TYPE,'') AS DISCOUNT_TYPE,ISNULL(ACCOUNT_NAME,'') AS ACCOUNT_NAME,ISNULL(EXPIREDATE,'') EXPIREDATE,ISNULL(TAX_TYPE,'') AS TAX_TYPE,ISNULL(COMMENT1,'') AS COMMENT1,ISNULL(UNITCOST_AOD,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST_AOD,CRAT,CRBY,UPAT,UPBY,CustCode,CustName,CURRENCYID,TAXTREATMENT\r\n" + 
					" FROM ["+ plant +"_PO_MULTI_ESTDET] WHERE POMULTIESTNO='"+POMULTIESTNO+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   MultiPoEstDet multiPoEstDet=new MultiPoEstDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, multiPoEstDet);
	                   multiPoEstDetlist.add(multiPoEstDet);
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
		return multiPoEstDetlist;
	}
	
	public boolean DeletePoDet(String plant, String POMULTIESTNO, int poline)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        String sQry = "DELETE FROM " + "[" + plant +"_PO_MULTI_ESTDET] WHERE POMULTIESTNO='"+POMULTIESTNO+"' AND POMULTIESTLNNO='"+poline+"'";
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
	
	
	  
	
}
