/* created by NAVAS */
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
import java.util.Vector;
import com.track.constants.MLoggerConstant;
import com.track.db.object.DoDet;
import com.track.db.object.estDet;
import com.track.db.util.PlantMstUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils; 

public class EstDetDAO extends BaseDAO {
	StrUtils _StrUtils = null;
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.ESTDetDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.ESTDetDAO_PRINTPLANTMASTERLOG;
	/* created by NAVAS */
	public static String TABLE_DET = "ESTDET";
	
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

	public EstDetDAO() {
		_StrUtils = new StrUtils();
		
	}

	public static String plant = "";
	public static String TABLE_NAME = "ESTDET";
	
	public boolean insertESTDet(Hashtable ht) throws Exception {
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "ESTDET" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Item already added to out going order");

		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}
	
	public ArrayList selectESTDet(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "ESTDET" + "] a ");
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
	
	public boolean update(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		String TABLE = "ESTDET";
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
	
	public boolean delete(java.util.Hashtable ht) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_ESTDET]");
			sql.append(" WHERE " + formCondition(ht));
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
	
	//---Added by Bruhan on April 14 2014, Description: To check data exists or not
	public boolean isExisit(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "["+ht.get("PLANT")+"_ESTDET]");
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
	
	public Boolean removeOrderDetails(String plant2, String estno)
			throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant2 + "_" + "ESTDET] WHERE ESTNO='"
					+ estno + "' AND PLANT='" + plant2 + "'";
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
public boolean insertEstimateMultiRemarks(Hashtable ht) throws Exception {
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_ESTIMATE_REMARKS]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);
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
	
	 public boolean deleteEstimateMultiRemarks(Hashtable ht) throws Exception {
         boolean delete = false;
         java.sql.Connection con = null;
         try {
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer(" DELETE ");
                 sql.append(" ");
                 sql.append(" FROM " + "[" + ht.get("PLANT") + "_ESTIMATE_REMARKS]");
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
	 
	 public ArrayList selectEstimateMultiRemarks(String query, Hashtable ht, String extCond)
				throws Exception {
			ArrayList alData = new ArrayList();
			java.sql.Connection con = null;

			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ ht.get("PLANT") + "_" + "ESTIMATE_REMARKS" + "] a ");
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
	
		public List selectRemarks(String query, Hashtable ht) throws Exception {
		List remarksList = new ArrayList();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "ESTIMATE_REMARKS" + "]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			this.mLogger.query(this.printQuery, sql.toString());

			remarksList = selectData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return remarksList;
	}
		
	public Map getEstQtyByProduct(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		String query="", item = "", plant = "";
		Map map = new HashMap();
		try {	    
			con = com.track.gates.DbBean.getConnection();
			item = (String) ht.get("item");
			plant = (String) ht.get("plant");			
			query = "SELECT ISNULL(SUM(QTYOR-QTYIS),0) AS ESTQTY FROM ["+plant+"_ESTHDR] a JOIN ["+plant+"_ESTDET] b "
					+ "ON a.ESTNO = b.ESTNO WHERE ITEM='"+item+"'and a.ORDER_STATUS!='Draft' AND a.PLANT='"+plant+"'";
			this.mLogger.query(this.printQuery, query);
			map = getRowOfData(con, query);
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

	/* created by navas */
	public boolean deleteDoMultiRemarks(Hashtable ht) throws Exception {
        boolean delete = false;
        java.sql.Connection con = null;
        try {
                con = DbBean.getConnection();
                StringBuffer sql = new StringBuffer(" DELETE ");
                sql.append(" ");
                sql.append(" FROM " + "[" + ht.get("PLANT") + "_ESTIMATE_REMARKS]");
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
	
	public boolean insertestDet(List<estDet> estdetList) throws Exception {
		boolean insertFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			
			for(estDet estdet : estdetList) {
				query = "INSERT INTO ["+estdet.getPLANT()+"_"+TABLE_DET+"]" + 
						"           ([PLANT]" + 
						"           ,[ESTNO]" + 
						"           ,[ESTLNNO]" + 
						"           ,[ITEM]" + 
						"           ,[ItemDesc]" + 
						"           ,[STATUS]" + 
						"           ,[TRANDATE]" + 
						"           ,[UNITPRICE]" + 
						"           ,[QTYOR]" + 
						"           ,[UNITMO]" +
						"           ,[DELDATE]" + 
						"           ,[COMMENT1]" +  
						"           ,[COMMENT2]" +  
						"           ,[CRAT]" + 
						"           ,[CRBY]" + 
						"           ,[UPAT]" + 
						"           ,[UPBY]" +
						"           ,[CURRENCYUSEQT]" + 
						"           ,[QTYIS]" + 
						"           ,[PRODGST]" + 
						"           ,[PRODUCTDELIVERYDATE]" + 
						"           ,[ACCOUNT_NAME]" +
						"           ,[TAX_TYPE]" + 
						"           ,[DISCOUNT]" +
						"           ,[DISCOUNT_TYPE])" +
						"     VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, estdet.getPLANT());
				   ps.setString(2, estdet.getESTNO());
				   ps.setLong(3, estdet.getESTLNNO());
				   ps.setString(4, estdet.getITEM());
				   ps.setString(5, estdet.getItemDesc());
				   ps.setString(6, estdet.getSTATUS());
				   ps.setString(7, estdet.getTRANDATE());
				   ps.setDouble(8, estdet.getUNITPRICE());
				   ps.setBigDecimal(9, estdet.getQTYOR());
				   ps.setString(10, estdet.getUNITMO());
				   ps.setString(11, estdet.getDELDATE());
				   ps.setString(12, estdet.getCOMMENT1());
				   ps.setString(13, estdet.getCOMMENT2());
				   ps.setString(14, estdet.getCRAT());
				   ps.setString(15, estdet.getCRBY());
				   ps.setString(16, estdet.getUPAT());
				   ps.setString(17, estdet.getUPBY());
				   ps.setDouble(18, estdet.getCURRENCYUSEQT());
				   ps.setBigDecimal(19, estdet.getQTYIS());
				   ps.setDouble(20, estdet.getPRODGST());
				   ps.setString(21, estdet.getPRODUCTDELIVERYDATE());
				   ps.setString(22, estdet.getACCOUNT_NAME());
				   ps.setString(23, estdet.getTAX_TYPE());
				   ps.setDouble(24, estdet.getDISCOUNT());
				   ps.setString(25, estdet.getDISCOUNT_TYPE());
				   
				   
				  
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   insertFlag = true;
				   }
				   else
				   {
					   throw new SQLException("Creating Sales Estimate failed, no rows affected.");
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
		return insertFlag;
	}
	
	/* EDITED BY NAVAS */
	public boolean updateestDet(List<estDet> estdetList) throws Exception {
		boolean updateFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			
			for(estDet estdet : estdetList) {
				query = "UPDATE ["+estdet.getPLANT()+"_"+TABLE_DET+"] SET " + 
						"           [PLANT] = ?" + 
						"           ,[ESTNO] = ?" + 
						"           ,[ESTLNNO] = ?" + 
						"           ,[ITEM] = ?" + 
						"           ,[ItemDesc] = ?" + 
						"           ,[STATUS] = ?" + 
						"           ,[TRANDATE] = ?" + 
						"           ,[UNITPRICE] = ?" + 
						"           ,[QTYOR] = ?" + 
						"           ,[UNITMO] = ?" +
						"           ,[DELDATE] = ?" + 
						"           ,[COMMENT1] = ?" +  
						"           ,[COMMENT2] = ?" +  
						"           ,[CRAT] = ?" + 
						"           ,[CRBY] = ?" + 
						"           ,[UPAT] = ?" + 
						"           ,[UPBY] = ?" +
						"           ,[CURRENCYUSEQT] = ?" + 
						"           ,[QTYIS] = ?" + 
						"           ,[PRODGST] = ?" + 
						"           ,[PRODUCTDELIVERYDATE] = ?" + 
						"           ,[ACCOUNT_NAME] = ?" +
						"           ,[TAX_TYPE] = ?" + 
						"           ,[DISCOUNT] = ?" +
						"           ,[DISCOUNT_TYPE] = ?" +
						"     WHERE ESTNO = ? AND ESTLNNO = ? ";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, estdet.getPLANT());
				   ps.setString(2, estdet.getESTNO());
				   ps.setLong(3, estdet.getESTLNNO());
				   ps.setString(4, estdet.getITEM());
				   ps.setString(5, estdet.getItemDesc());
				   ps.setString(6, estdet.getSTATUS());
				   ps.setString(7, estdet.getTRANDATE());
				   ps.setDouble(8, estdet.getUNITPRICE());
				   ps.setBigDecimal(9, estdet.getQTYOR());
				   ps.setString(10, estdet.getUNITMO());
				   ps.setString(11, estdet.getDELDATE());
				   ps.setString(12, estdet.getCOMMENT1());
				   ps.setString(13, estdet.getCOMMENT2());
				   ps.setString(14, estdet.getCRAT());
				   ps.setString(15, estdet.getCRBY());
				   ps.setString(16, estdet.getUPAT());
				   ps.setString(17, estdet.getUPBY());
				   ps.setDouble(18, estdet.getCURRENCYUSEQT());
				   ps.setBigDecimal(19, estdet.getQTYIS());
				   ps.setDouble(20, estdet.getPRODGST());
				   ps.setString(21, estdet.getPRODUCTDELIVERYDATE());
				   ps.setString(22, estdet.getACCOUNT_NAME());
				   ps.setString(23, estdet.getTAX_TYPE());
				   ps.setDouble(24, estdet.getDISCOUNT());
				   ps.setString(25, estdet.getDISCOUNT_TYPE());
				   
				   
				  
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   updateFlag = true;
				   }
				   else
				   {
					   throw new SQLException("Updating Sales Estimate failed, no rows affected.");
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

	/* created by resvi */
	public boolean isExisitDoMultiRemarks(Hashtable ht) throws Exception {
        this.mLogger.log(1, this.getClass() + " isExisit()");
        boolean flag = false;
        java.sql.Connection con = null;
        try {
                con = com.track.gates.DbBean.getConnection();
                StringBuffer sql = new StringBuffer(" SELECT ");
                sql.append("COUNT(*) ");
                sql.append(" ");
                sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "ESTIMATE_REMARKS" + "]");
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

	/* CREATED BY NAVAS */
	public boolean insertDoMultiRemarks(Hashtable ht) throws Exception {
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_ESTIMATE_REMARKS]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);
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
	
	/* CREATED BY NAVAS */
	public List<estDet> getestDetById(String plant,String estno)throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		List<estDet> estdetList = new ArrayList<estDet>();
		try {
			connection = DbBean.getConnection();
			query = "SELECT " + 
					"           [PLANT]" + 
					"           ,[ESTNO]" + 
					"           ,ISNULL([ESTLNNO],0) ESTLNNO" + 
					"           ,[ITEM]" + 
					"           ,[ItemDesc]" + 
					"           ,[STATUS]" + 
					"           ,[TRANDATE]" + 
					"           ,[UNITPRICE]" + 
					"           ,[QTYOR]" + 
					"           ,[UNITMO]" +
					"           ,ISNULL([DELDATE], '') DELDATE" + 
					"           ,[COMMENT1]" +  
					"           ,[COMMENT2]" +  
					"           ,[CRAT]" + 
					"           ,[CRBY]" + 
					"           ,[UPAT]" + 
					"           ,[UPBY]" +
					"           ,[CURRENCYUSEQT]" + 
					"           ,[QTYIS]" + 
					"           ,ISNULL([PRODGST],0) PRODGST" +
					"           ,[PRODUCTDELIVERYDATE]" + 
					"           ,ISNULL([ACCOUNT_NAME],'') ACCOUNT_NAME" + 
					"           ,ISNULL([DISCOUNT],0) DISCOUNT" +
					"           ,ISNULL([DISCOUNT_TYPE],'') DISCOUNT_TYPE" +
					"           ,ISNULL([TAX_TYPE],'') TAX_TYPE FROM ["+ plant +"_"+TABLE_DET+"] WHERE "+ 
					" ESTNO=? AND PLANT=?";
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, estno);
				   ps.setString(2, plant);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   	estDet estdet = new estDet();
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, estdet);
	                    estdetList.add(estdet);
				   }   
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return estdetList;
	}

	public estDet isgetEstDetById(String plant, String estno, int lnno, String item) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    estDet toDet=new estDet();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_DET+"]  WHERE ESTNO ='"+estno+"' AND ESTLNNO ='"+lnno+"' AND ITEM ='"+item+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, toDet);
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
		return toDet;
	}
		
	
}

