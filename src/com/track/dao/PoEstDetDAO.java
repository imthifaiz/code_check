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
import com.track.db.object.PoEstDet;
import com.track.db.object.PoEstHdr;
import com.track.db.object.estDet;
import com.track.db.object.PoEstDetRemarks;

import com.track.db.util.PlantMstUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PoEstDetDAO extends BaseDAO {

	public static String TABLE_NAME = "POESTDET";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.POESTDETDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.POESTDETDAO_PRINTPLANTMASTERLOG;

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

	public PoEstDetDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "POESTDET" + "]";
	}

	public PoEstDetDAO() {

	}

	public boolean isExisit(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_POESTDET]");
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
			TABLE_NAME = "POESTDET";
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
                    sql.append(" FROM " + "[" + ht.get("PLANT") + "_POESTDET]");
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
			TABLE_NAME="POESTDET";
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+"["+ht.get(IDBConstants.PLANT)+"_POESTDET]");
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
		TABLE_NAME = "POESTDET";
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
			TABLE_NAME = "POESTDET";
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
			TABLE_NAME = "POESTDET";
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
	
	public ArrayList selectReversePoDet(String query, Hashtable ht,
			String extCond, String plant) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ plant + "_" + "POESTDET" + "]a,[" + plant + "_" + "RECVDET"
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
			TABLE_NAME = "POESTDET";
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_POESTDET ]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);
			TABLE_NAME = "POESTDET";
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
			StringBuffer sql = new StringBuffer(" UPDATE " + "[" + htCondition.get("PLANT") + "_"+ "POESTDET" + "]");
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

	

    public boolean isExisit(Hashtable ht, String extCond) throws Exception {
            boolean flag = false;
            java.sql.Connection con = null;
            try {
                    con = com.track.gates.DbBean.getConnection();

                    StringBuffer sql = new StringBuffer(" SELECT ");
                    sql.append("COUNT(*) ");
                    sql.append(" ");
                    sql.append(" FROM "  + "[" + ht.get("PLANT") + "_"+ "POESTDET" + "]");
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
	
	
	
	
	public Boolean removeOrderDetails(String plant, String POESTNO)
			throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant + "_" + "POESTDET] WHERE POESTNO='"
					+ POESTNO + "' AND PLANT='" + plant + "'";
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
	
	/**
	 * method : getCountItemmst(Hashtable ht) description : get the count of
	 * records in Itemmst for the given condition
	 * 
	 * @param : Hashtable ht
	 * @return : int - count
	 * @throws Exception
	 */
	public int getCountPoNo(Hashtable ht, String extCon) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iCntItemmst = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key + " = '" + value + "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String sQry = "SELECT COUNT(*) FROM " + "["
					+ ht.get("PLANT") + "_POESTHDR]" + " WHERE "
					+ sCondition;
			
			if(extCon.length() > 0){
				sQry = sQry + " AND " + extCon;
			}
			
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCntItemmst = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCntItemmst;
	}
	
	public int getMaxPoLnNo(Hashtable ht) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iCntItemmst = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key + " = '" + value + "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String sQry = "SELECT MAX(POESTLNNO) FROM " + "["
					+ ht.get("PLANT") + "_POESTDET]" + " WHERE "
					+ sCondition;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCntItemmst = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCntItemmst;
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_POESTDET_REMARKS]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);
			TABLE_NAME = "POESTDET";
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
	
	public ArrayList selectPoMultiRemarks(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "POESTDET_REMARKS" + "] a ");
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
	 public boolean deletePoMultiRemarks(Hashtable ht) throws Exception {
         boolean delete = false;
         java.sql.Connection con = null;
         try {
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer(" DELETE ");
                 sql.append(" ");
                 sql.append(" FROM " + "[" + ht.get("PLANT") + "_POESTDET_REMARKS]");
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
	 
	 
		public boolean updatepo(String query, Hashtable htCondition, String extCond)
				throws Exception {
			boolean flag = false;
			String TABLE = "POESTDET";
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
		
	 
	 public boolean updatepoestDet(List<PoEstDet> poestdetList) throws Exception {
			boolean updateFlag = false;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
			try {
				connection = DbBean.getConnection();
				
				for(PoEstDet poEstDet : poestdetList) {
					query = "UPDATE ["+poEstDet.getPLANT()+"_"+TABLE_NAME+"] SET " + 
							"           [PLANT] = ?" + 
							"           ,[POESTNO] = ?" +  
							"           ,[POESTLNNO] = ?" +  
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
							"     WHERE POESTNO = ? AND POESTLNNO = ? ";
					if(connection != null){
						   ps = connection.prepareStatement(query);
						   ps.setString(1, poEstDet.getPLANT());
								ps.setString(2, poEstDet.getPOESTNO());
								ps.setInt(3, poEstDet.getPOESTLNNO());
								ps.setString(4, poEstDet.getLNSTAT());
								ps.setString(5, poEstDet.getITEM());
								ps.setString(6, poEstDet.getItemDesc());
								ps.setString(7, poEstDet.getTRANDATE());
								ps.setDouble(8, poEstDet.getUNITCOST());
								ps.setBigDecimal(9, poEstDet.getQTYOR());
								ps.setBigDecimal(10, poEstDet.getQTYRC());
								ps.setString(11, poEstDet.getUNITMO());
								ps.setString(12, poEstDet.getUSERFLD1());
								ps.setString(13, poEstDet.getUSERFLD2());
								ps.setString(14, poEstDet.getUSERFLD3());
								ps.setString(15, poEstDet.getUSERFLD4());
								ps.setDouble(16, poEstDet.getCURRENCYUSEQT());
								ps.setDouble(17, poEstDet.getPRODGST());
								ps.setString(18, poEstDet.getPRODUCTDELIVERYDATE());
								ps.setString(19, poEstDet.getCOMMENT1());
								ps.setString(20, poEstDet.getUPAT());
								ps.setString(21, poEstDet.getUPBY());
								ps.setDouble(22, poEstDet.getDISCOUNT());
								ps.setString(23, poEstDet.getDISCOUNT_TYPE());
								ps.setString(24, poEstDet.getACCOUNT_NAME());
								ps.setString(25, poEstDet.getTAX_TYPE());
								ps.setString(26, poEstDet.getPOESTNO());
								ps.setInt(27, poEstDet.getPOESTLNNO());

					   
					   
					  
					   int count=ps.executeUpdate();
					   if(count>0)
					   {
						   updateFlag = true;
					   }
					   else
					   {
						   throw new SQLException("Updating Purchase Estimate failed, no rows affected.");
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
	 public boolean updatePoMultiRemarks(String query, Hashtable htCondition, String extCond,
				String aPlant) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" UPDATE " + "[" + aPlant + "_"
						+ "POESTDET_REMARKS" + "]");
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
	
	 public boolean isExisitPoMultiRemarks(Hashtable ht) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" ");
				sql.append(" FROM " + "[" + ht.get("PLANT") + "_POESTDET_REMARKS]");
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
	 
		public List selectRemarks(String query, Hashtable ht) throws Exception {
			List remarksList = new ArrayList();

			java.sql.Connection con = null;
			try {
				TABLE_NAME="POESTDET_REMARKS";
				con = com.track.gates.DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
						+"["+ht.get(IDBConstants.PLANT)+"_"+ TABLE_NAME+"]");
				sql.append(" WHERE ");
				String conditon = formCondition(ht);
				sql.append(conditon);
				this.mLogger.query(this.printQuery, sql.toString());
				remarksList = selectData(con, sql.toString());
				TABLE_NAME="";
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
		
	public ArrayList selectForReport(String query) throws Exception {
		boolean flag = false;
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(query);
			this.mLogger.query(this.printQuery, sql.toString());
			al = selectData(con, sql.toString());
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
	
    public boolean addPoDet(PoEstDet poEstDet) throws Exception {
 		boolean insertFlag = false;
 		boolean flag = false;
 		int HdrId = 0;
 		Connection connection = null;
 		PreparedStatement ps = null;
 	    String query = "";
 		try {	    
 			connection = DbBean.getConnection();
 			query = "INSERT INTO ["+poEstDet.getPLANT()+"_POESTDET]" + 
 					"           ([PLANT]" + 
 					"           ,[POESTNO]" + 
 					"           ,[POESTLNNO]" + 
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
 					"           ,[CRBY],[POMULTIESTLNNO],[POMULTIESTNO]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

 			if(connection != null){
	 			ps = connection.prepareStatement(query);
	 			ps.setString(1, poEstDet.getPLANT());
	 			ps.setString(2, poEstDet.getPOESTNO());
	 			ps.setInt(3, poEstDet.getPOESTLNNO());
	 			ps.setString(4, poEstDet.getLNSTAT());
	 			ps.setString(5, poEstDet.getITEM());
	 			ps.setString(6, poEstDet.getItemDesc());
	 			ps.setString(7, poEstDet.getTRANDATE());
	 			ps.setDouble(8, poEstDet.getUNITCOST());
	 			ps.setBigDecimal(9, poEstDet.getQTYOR());
	 			ps.setBigDecimal(10, poEstDet.getQTYRC());
	 			ps.setString(11, poEstDet.getUNITMO());
	 			ps.setString(12, poEstDet.getUSERFLD1());
	 			ps.setString(13, poEstDet.getUSERFLD2());
	 			ps.setString(14, poEstDet.getUSERFLD3());
	 			ps.setString(15, poEstDet.getUSERFLD4());
	 			ps.setDouble(16, poEstDet.getCURRENCYUSEQT());
	 			ps.setDouble(17, poEstDet.getPRODGST());
	 			ps.setString(18, poEstDet.getPRODUCTDELIVERYDATE());
	 			ps.setString(19, poEstDet.getCOMMENT1());
	 			ps.setString(20, poEstDet.getCRAT());
	 			ps.setDouble(21, poEstDet.getDISCOUNT());
	 			ps.setString(22, poEstDet.getDISCOUNT_TYPE());
	 			ps.setString(23, poEstDet.getACCOUNT_NAME());
	 			ps.setString(24, poEstDet.getTAX_TYPE());
	 			ps.setDouble(25, poEstDet.getUNITCOST_AOD());
	 			ps.setString(26, poEstDet.getCRBY());
	 			ps.setInt(27, poEstDet.getPOMULTIESTLNNO());
	 			ps.setString(28, poEstDet.getPOMULTIESTNO());


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
    
	public boolean updatePoDet(List<PoEstDet> PoDetList) throws Exception {
		boolean updateFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			
			for(PoEstDet poEstDet : PoDetList) {
				query = "UPDATE ["+poEstDet.getPLANT()+"_POESTDET] SET " + 
						"           [PLANT] = ?" + 
						"           ,[POESTNO] = ?" +  
						"           ,[POESTLNNO] = ?" +  
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
						"     WHERE POESTNO = ? AND POESTLNNO = ? ";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, poEstDet.getPLANT());
		 			ps.setString(2, poEstDet.getPOESTNO());
		 			ps.setInt(3, poEstDet.getPOESTLNNO());
		 			ps.setString(4, poEstDet.getLNSTAT());
		 			ps.setString(5, poEstDet.getITEM());
		 			ps.setString(6, poEstDet.getItemDesc());
		 			ps.setString(7, poEstDet.getTRANDATE());
		 			ps.setDouble(8, poEstDet.getUNITCOST());
		 			ps.setBigDecimal(9, poEstDet.getQTYOR());
		 			ps.setBigDecimal(10, poEstDet.getQTYRC());
		 			ps.setString(11, poEstDet.getUNITMO());
		 			ps.setString(12, poEstDet.getUSERFLD1());
		 			ps.setString(13, poEstDet.getUSERFLD2());
		 			ps.setString(14, poEstDet.getUSERFLD3());
		 			ps.setString(15, poEstDet.getUSERFLD4());
		 			ps.setDouble(16, poEstDet.getCURRENCYUSEQT());
		 			ps.setDouble(17, poEstDet.getPRODGST());
		 			ps.setString(18, poEstDet.getPRODUCTDELIVERYDATE());
		 			ps.setString(19, poEstDet.getCOMMENT1());
		 			ps.setString(20, poEstDet.getUPAT());
		 			ps.setString(21, poEstDet.getUPBY());
		 			ps.setDouble(22, poEstDet.getDISCOUNT());
		 			ps.setString(23, poEstDet.getDISCOUNT_TYPE());
		 			ps.setString(24, poEstDet.getACCOUNT_NAME());
		 			ps.setString(25, poEstDet.getTAX_TYPE());
		 			ps.setString(26, poEstDet.getPOESTNO());
		 			ps.setInt(27, poEstDet.getPOESTLNNO());
		 			
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
	
	public boolean updatePoDetpo(PoEstDet PoEstDet) throws Exception {
		boolean updateFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			

				query = "UPDATE ["+PoEstDet.getPLANT()+"_POESTDET] SET " + 
						"           [PLANT] = ?" + 
						"           ,[POESTNO] = ?" +  
						"           ,[POESTLNNO] = ?" +  
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
						"     WHERE POESTNO = ? AND POESTLNNO = ? ";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, PoEstDet.getPLANT());
		 			ps.setString(2, PoEstDet.getPOESTNO());
		 			ps.setInt(3, PoEstDet.getPOESTLNNO());
		 			ps.setString(4, PoEstDet.getLNSTAT());
		 			ps.setString(5, PoEstDet.getITEM());
		 			ps.setString(6, PoEstDet.getItemDesc());
		 			ps.setString(7, PoEstDet.getTRANDATE());
		 			ps.setDouble(8, PoEstDet.getUNITCOST());
		 			ps.setBigDecimal(9, PoEstDet.getQTYOR());
		 			ps.setBigDecimal(10, PoEstDet.getQTYRC());
		 			ps.setString(11, PoEstDet.getUNITMO());
		 			ps.setString(12, PoEstDet.getUSERFLD1());
		 			ps.setString(13, PoEstDet.getUSERFLD2());
		 			ps.setString(14, PoEstDet.getUSERFLD3());
		 			ps.setString(15, PoEstDet.getUSERFLD4());
		 			ps.setDouble(16, PoEstDet.getCURRENCYUSEQT());
		 			ps.setDouble(17, PoEstDet.getPRODGST());
		 			ps.setString(18, PoEstDet.getPRODUCTDELIVERYDATE());
		 			ps.setString(19, PoEstDet.getCOMMENT1());
		 			ps.setString(20, PoEstDet.getUPAT());
		 			ps.setString(21, PoEstDet.getUPBY());
		 			ps.setDouble(22, PoEstDet.getDISCOUNT());
		 			ps.setString(23, PoEstDet.getDISCOUNT_TYPE());
		 			ps.setString(24, PoEstDet.getACCOUNT_NAME());
		 			ps.setString(25, PoEstDet.getTAX_TYPE());
		 			ps.setString(26, PoEstDet.getPOESTNO());
		 			ps.setInt(27, PoEstDet.getPOESTLNNO());
		 			
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
	
	public boolean updatePoDetpoEdit(PoEstDet PoEstDet,int POESTLNNO) throws Exception {
		boolean updateFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			

				query = "UPDATE ["+PoEstDet.getPLANT()+"_POESTDET] SET " + 
						"           [PLANT] = ?" + 
						"           ,[POESTNO] = ?" +  
						"           ,[POESTLNNO] = ?" +  
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
						"     WHERE POESTNO = ? AND POESTLNNO = ? ";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, PoEstDet.getPLANT());
		 			ps.setString(2, PoEstDet.getPOESTNO());
		 			ps.setInt(3, PoEstDet.getPOESTLNNO());
		 			ps.setString(4, PoEstDet.getLNSTAT());
		 			ps.setString(5, PoEstDet.getITEM());
		 			ps.setString(6, PoEstDet.getItemDesc());
		 			ps.setString(7, PoEstDet.getTRANDATE());
		 			ps.setDouble(8, PoEstDet.getUNITCOST());
		 			ps.setBigDecimal(9, PoEstDet.getQTYOR());
		 			ps.setBigDecimal(10, PoEstDet.getQTYRC());
		 			ps.setString(11, PoEstDet.getUNITMO());
		 			ps.setString(12, PoEstDet.getUSERFLD1());
		 			ps.setString(13, PoEstDet.getUSERFLD2());
		 			ps.setString(14, PoEstDet.getUSERFLD3());
		 			ps.setString(15, PoEstDet.getUSERFLD4());
		 			ps.setDouble(16, PoEstDet.getCURRENCYUSEQT());
		 			ps.setDouble(17, PoEstDet.getPRODGST());
		 			ps.setString(18, PoEstDet.getPRODUCTDELIVERYDATE());
		 			ps.setString(19, PoEstDet.getCOMMENT1());
		 			ps.setString(20, PoEstDet.getUPAT());
		 			ps.setString(21, PoEstDet.getUPBY());
		 			ps.setDouble(22, PoEstDet.getDISCOUNT());
		 			ps.setString(23, PoEstDet.getDISCOUNT_TYPE());
		 			ps.setString(24, PoEstDet.getACCOUNT_NAME());
		 			ps.setDouble(25, PoEstDet.getUNITCOST_AOD());
		 			ps.setString(26, PoEstDet.getTAX_TYPE());
		 			ps.setString(27, PoEstDet.getPOESTNO());
		 			ps.setInt(28, POESTLNNO);
		 			
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
	
	
	
	
	public PoEstDet getPoDetByPonoPllno(String plant, String POESTNO, int poline) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    PoEstDet poEstDet=new PoEstDet();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,POESTNO,POESTLNNO,LNSTAT,ITEM,ItemDesc,TRANDATE,UNITCOST,QTYOR,QTYRC,UNITMO,USERFLD1,USERFLD2,USERFLD3,USERFLD4,CURRENCYUSEQT,PRODGST,ISNULL(POMULTIESTLNNO,'0') POMULTIESTLNNO,POMULTIESTNO,PRODUCTDELIVERYDATE,DISCOUNT,ISNULL(DISCOUNT_TYPE,'') AS DISCOUNT_TYPE,ISNULL(ACCOUNT_NAME,'') AS ACCOUNT_NAME,ISNULL(TAX_TYPE,'') AS TAX_TYPE,ISNULL(COMMENT1,'') AS COMMENT1,ISNULL(UNITCOST_AOD,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST_AOD,CRAT,CRBY,UPAT,UPBY\r\n" + 
					" FROM ["+ plant +"_POESTDET] WHERE POESTNO='"+POESTNO+"' AND POESTLNNO='"+poline+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, poEstDet);
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
		return poEstDet;
	}
	
	
	
	public List<PoEstDet> getPoDetByPono(String plant, String POESTNO) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<PoEstDet> podetlist = new ArrayList<PoEstDet>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,POESTNO,POESTLNNO,LNSTAT,ITEM,ItemDesc,TRANDATE,ISNULL(UNITCOST,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST,QTYOR,QTYRC,UNITMO,USERFLD1,USERFLD2,USERFLD3,USERFLD4,CURRENCYUSEQT,ISNULL(PRODGST,'0') PRODGST,ISNULL(POMULTIESTLNNO,'0') POMULTIESTLNNO,POMULTIESTNO,PRODUCTDELIVERYDATE,CASE WHEN ISNULL(DISCOUNT_TYPE,'0') = '%' THEN ISNULL(DISCOUNT,'0') ELSE (ISNULL(DISCOUNT,'0') * ISNULL(CURRENCYUSEQT,1))END AS DISCOUNT,ISNULL(DISCOUNT_TYPE,'') AS DISCOUNT_TYPE,ISNULL(ACCOUNT_NAME,'') AS ACCOUNT_NAME,ISNULL(TAX_TYPE,'') AS TAX_TYPE,ISNULL(COMMENT1,'') AS COMMENT1,ISNULL(UNITCOST_AOD,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST_AOD,CRAT,CRBY,UPAT,UPBY\r\n" + 
					" FROM ["+ plant +"_POESTDET] WHERE POESTNO='"+POESTNO+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   PoEstDet poEstDet=new PoEstDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, poEstDet);
	                   podetlist.add(poEstDet);
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
		return podetlist;
	}
	
	public boolean DeletePoDet(String plant, String POESTNO, int poline)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        String sQry = "DELETE FROM " + "[" + plant +"_POESTDET] WHERE POESTNO='"+POESTNO+"' AND POESTLNNO='"+poline+"'";
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
	
	  public boolean addPoDetRemarks(PoEstDetRemarks poEstDetRemarks) throws Exception {
	 		boolean insertFlag = false;
	 		boolean flag = false;
	 		int HdrId = 0;
	 		Connection connection = null;
	 		PreparedStatement ps = null;
	 	    String query = "";
	 		try {	    
	 			connection = DbBean.getConnection();
	 			query = "INSERT INTO ["+poEstDetRemarks.getPLANT()+"_POESTDET_REMARKS]" + 
	 					"           ([PLANT]" + 
	 					"           ,[POESTNO]" + 
	 					"           ,[POESTLNNO]" + 
	 					"           ,[ITEM]" + 
	 					"           ,[REMARKS]" + 
	 					"           ,[CRAT]" +
	 					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?)";

	 			if(connection != null){
		 			ps = connection.prepareStatement(query);
		 			ps.setString(1, poEstDetRemarks.getPLANT());
		 			ps.setString(2, poEstDetRemarks.getPOESTNO());
		 			ps.setInt(3, poEstDetRemarks.getPOESTLNNO());
		 			ps.setString(4, poEstDetRemarks.getITEM());
		 			ps.setString(5, poEstDetRemarks.getREMARKS());
		 			ps.setString(6, poEstDetRemarks.getCRAT());
		 			ps.setString(7, poEstDetRemarks.getCRBY());
		 			
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
	  
	  public boolean updatePoDetRemarks(List<PoEstDetRemarks> poEstDetRemarkslist) throws Exception {
			boolean updateFlag = false;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
			try {
				connection = DbBean.getConnection();
				
				for(PoEstDetRemarks poEstDetRemarks : poEstDetRemarkslist) {
					query = "UPDATE ["+poEstDetRemarks.getPLANT()+"_POESTDET_REMARKS] SET " + 
							"           [PLANT] = ?" + 
							"           ,[POESTNO] = ?" +  
							"           ,[POESTLNNO] = ?" +  
							"           ,[ITEM] = ?" +  
							"           ,[REMARKS] = ?" +   
							"           ,[UPAT] = ?" +  
							"           ,[UPBY] = ?" +  
							"     WHERE POESTNO = ? AND POESTLNNO = ? ";
					if(connection != null){
					   ps = connection.prepareStatement(query);
					   ps.setString(1, poEstDetRemarks.getPLANT());
			 			ps.setString(2, poEstDetRemarks.getPOESTNO());
			 			ps.setInt(3, poEstDetRemarks.getPOESTLNNO());
			 			ps.setString(4, poEstDetRemarks.getITEM());
			 			ps.setString(5, poEstDetRemarks.getREMARKS());
			 			ps.setString(6, poEstDetRemarks.getUPAT());
			 			ps.setString(7, poEstDetRemarks.getUPBY());
			 			ps.setString(8, poEstDetRemarks.getPOESTNO());
			 			ps.setInt(9, poEstDetRemarks.getPOESTLNNO());
			 			
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
	  
	  public boolean updatePoDetRemarksBYID(List<PoEstDetRemarks> poDetRemarkslist) throws Exception {
			boolean updateFlag = false;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
			try {
				connection = DbBean.getConnection();
				
				for(PoEstDetRemarks poEstDetRemarks : poDetRemarkslist) {
					query = "UPDATE ["+poEstDetRemarks.getPLANT()+"_POESTDET_REMARKS] SET " + 
							"           [PLANT] = ?" + 
							"           ,[POESTNO] = ?" +  
							"           ,[POESTLNNO] = ?" +  
							"           ,[ITEM] = ?" +  
							"           ,[REMARKS] = ?" +   
							"           ,[UPAT] = ?" +  
							"           ,[UPBY] = ?" +  
							"     WHERE ID_REMARKS = ? ";
					if(connection != null){
					   ps = connection.prepareStatement(query);
					   ps.setString(1, poEstDetRemarks.getPLANT());
			 			ps.setString(2, poEstDetRemarks.getPOESTNO());
			 			ps.setInt(3, poEstDetRemarks.getPOESTLNNO());
			 			ps.setString(4, poEstDetRemarks.getITEM());
			 			ps.setString(5, poEstDetRemarks.getREMARKS());
			 			ps.setString(6, poEstDetRemarks.getUPAT());
			 			ps.setString(7, poEstDetRemarks.getUPBY());
			 			ps.setInt(8, poEstDetRemarks.getID_REMARKS());
			 			
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
	  
	  public List<PoEstDetRemarks> getPoDetRemarksByPono(String plant, String POESTNO) throws Exception {
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
		    List<PoEstDetRemarks> PoDetRemarksist = new ArrayList<PoEstDetRemarks>();
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT * FROM ["+ plant +"_POESTDET_REMARKS] WHERE POESTNO='"+POESTNO+"'";

				if(connection != null){
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   ResultSet rst = ps.executeQuery();
					   while (rst.next()) {
						   PoEstDetRemarks poEstDetRemarks=new PoEstDetRemarks();
		                   ResultSetToObjectMap.loadResultSetIntoObject(rst, poEstDetRemarks);
		                   PoDetRemarksist.add(poEstDetRemarks);
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
			return PoDetRemarksist;
		}
	  
	  public boolean IsExistPoDetRemarks(String plant, String POESTNO, String ponoln, String item)throws Exception {
			Connection connection = null;
			PreparedStatement ps = null;
			boolean status = false;
		    String query = "";
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT * FROM ["+ plant +"_POESTDET_REMARKS] WHERE PLANT='"+plant+"' AND POESTNO='"+POESTNO+"' AND POESTLNNO='"+ponoln+"' AND ITEM='"+item+"'";

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
	  
	  
	  
	  public  List<PoEstDetRemarks>  GetPoDetRemarksbyitems(String plant, String POESTNO, String ponoln, String item)throws Exception {
			Connection connection = null;
			PreparedStatement ps = null;
			boolean status = false;
		    String query = "";
		    List<PoEstDetRemarks> PoEstDetRemarkslist = new ArrayList<PoEstDetRemarks>();
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT * FROM ["+ plant +"_POESTDET_REMARKS] WHERE PLANT='"+plant+"' AND POESTNO='"+POESTNO+"' AND POESTLNNO='"+ponoln+"' AND ITEM='"+item+"'";

				if(connection != null){
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   ResultSet rst = ps.executeQuery();
					   while (rst.next()) {
						   PoEstDetRemarks poEstDetRemarks=new PoEstDetRemarks();
		                   ResultSetToObjectMap.loadResultSetIntoObject(rst, poEstDetRemarks);
		                   PoEstDetRemarkslist.add(poEstDetRemarks);
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
			return PoEstDetRemarkslist;
		}
	  
	  public boolean DeletePoDetRemarks(String plant, int id)
		        throws Exception {
				boolean deletestatus = false;
				PreparedStatement ps = null;
				Connection con = null;
				try {
				        con = DbBean.getConnection();
				        
				        
				        String sQry = "DELETE FROM " + "[" + plant +"_POESTDET_REMARKS]"
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
	
}
