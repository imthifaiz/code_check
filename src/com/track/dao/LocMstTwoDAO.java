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

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.object.DoDet;
import com.track.db.object.DoHdr;
import com.track.db.object.LOC_TYPE_MST2;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

public class LocMstTwoDAO extends BaseDAO {
	public static final String TABLE_NAME = "LOC_TYPE_MST2";

	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.LOCMSTDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.LOCMSTDAO_PRINTPLANTMASTERLOG;

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

	public LocMstTwoDAO() {
		StrUtils _StrUtils = new StrUtils();
	}

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from  ["
					+ (String) ht.get("PLANT") + "_" + TABLE_NAME + "]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
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

	public String getLocDesc(String aPlant, String loc) throws Exception {
		String itemDesc = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("loc", loc);
		String query = " locDesc ";
		Map m = selectRow(query, ht);
		itemDesc = StrUtils.fString((String) m.get("locDesc"));
		return itemDesc;
	}

	public ArrayList selectLocMst(String query, Hashtable ht) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from  ["
				+ (String) ht.get("PLANT") + "_" + TABLE_NAME + "]");
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

	public ArrayList selectLocMst(String query, Hashtable ht, String extCondi)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from   ["
				+ (String) ht.get("PLANT") + "_" + TABLE_NAME + "]");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			if (extCondi.length() > 0)
				sql.append(" and " + extCondi);
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

	public boolean isExistsLoc(Hashtable ht) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();

			// query
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" 1 ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME
					+ "]");
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
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME
					+ "]");
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

	public boolean isExisit(String sql) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
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
//imthi insert
	public boolean insertLocMst(LOC_TYPE_MST2 loc_TYPE_MST2) throws Exception {
			boolean insertFlag = false;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
			try {
				connection = DbBean.getConnection();
			
					query = "INSERT INTO ["+loc_TYPE_MST2.getPLANT()+"_"+TABLE_NAME+"]" + 
							"           ([PLANT]" + 
							"           ,[LOC_TYPE_ID2]" + 
							"           ,[LOC_TYPE_DESC2]" + 
							"           ,[CRAT]" + 
							"           ,[CRBY]" + 
							"           ,[UPAT]" + 
							"           ,[UPBY]" + 
							"           ,[IsActive])" + 
							"     VALUES(?,?,?,?,?,?,?,?)";
					if(connection != null){
					   ps = connection.prepareStatement(query);
					   ps.setString(1, loc_TYPE_MST2.getPLANT());
					   ps.setString(2, loc_TYPE_MST2.getLOC_TYPE_ID2());
					   ps.setString(3, loc_TYPE_MST2.getLOC_TYPE_DESC2());
					   ps.setString(4, loc_TYPE_MST2.getCRAT());
					   ps.setString(5, loc_TYPE_MST2.getCRBY());
					   ps.setString(6, loc_TYPE_MST2.getUPAT());
					   ps.setString(7, loc_TYPE_MST2.getUPBY());
					   ps.setString(8, loc_TYPE_MST2.getIsActive());
					
					  
					   int count=ps.executeUpdate();
					   if(count>0)
					   {
						   insertFlag = true;
					   }
					   else
					   {
						   throw new SQLException("Creating Location Type Two failed, no rows affected.");
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
			return insertFlag;
		}
	//imti insert end
	
	//imti byid
	public List<LOC_TYPE_MST2> getLOC_TYPE_MST2ById(String plant,String id) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		List<LOC_TYPE_MST2> loc_TYPE_MST2 = new ArrayList<LOC_TYPE_MST2>();
		try {
			connection = DbBean.getConnection();
			query = "SELECT " + 
					"           [PLANT]" + 
					"           ,[LOC_TYPE_ID2]" + 
					"           ,[LOC_TYPE_DESC2]" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]" + 
					"           ,[UPAT]" + 
					"           ,[UPBY]" + 
					"           ,[IsActive]" + 
					"           ,ISNULL([TAX_TYPE],'') TAX_TYPE FROM ["+ plant +"_"+TABLE_NAME+"] WHERE "+ 
					" id=? AND plant=?";
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, id);
				   ps.setString(2, plant);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   LOC_TYPE_MST2 locTYPEMST2 = new LOC_TYPE_MST2();
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, locTYPEMST2);
	                    loc_TYPE_MST2.add(locTYPEMST2);
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
		return loc_TYPE_MST2;
	}
	//imti byid end
	
	//imti withoutid error
	public List<LOC_TYPE_MST2> getLOC_TYPE_MST2(String plant,String Location_Type_ID)  throws Exception {
		 boolean flag = false;
		   int journalHdrId = 0;
		   Connection connection = null;
		   PreparedStatement ps = null;
		   String query = "", fields = "", dtCondStr = "";
		   List<LOC_TYPE_MST2> doHeaders = new ArrayList<LOC_TYPE_MST2>();
		   List<String> args = null;
		   try {
			   args = new ArrayList<String>();
			   connection = DbBean.getConnection();
			   query = "SELECT [PLANT]" + 
			   		"      ,[LOC_TYPE_ID2]" + 
			   		"      ,[LOC_TYPE_DESC2]" + 
			   		"      ,[CRAT]" + 
			   		"      ,[CRBY]" + 
			   		"      ,[UPAT]" + 
			   		"      ,[UPBY]" + 
			   		"      ,[IsActive]" + 
			   		"     FROM ["+ plant +"_LOC_TYPE_MST2] WHERE ";
		
				
			   query +=" PLANT = '"+plant+"' AND ISNULL(LOC_TYPE_ID2,'') LIKE '%"+Location_Type_ID+"%' ";
				
		
				query += " ORDER BY LOC_TYPE_ID2 DESC ";
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
						LOC_TYPE_MST2 lOC_TYPE_MST2 = new LOC_TYPE_MST2();
						ResultSetToObjectMap.loadResultSetIntoObject(rst, lOC_TYPE_MST2);
						doHeaders.add(lOC_TYPE_MST2);
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
	//imti withoutid error end
	
	//imti update 
	public boolean updateLOC_TYPE_MST2(LOC_TYPE_MST2 lOC_TYPE_MST2) throws Exception {
		boolean updateFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			
//			for(LOC_TYPE_MST2 loc_TYPE_MST2 : lOC_TYPE_MST2) {
				query = "UPDATE ["+lOC_TYPE_MST2.getPLANT()+"_"+TABLE_NAME+"] SET " + 
						"           [PLANT] = ?" + 
						"           ,[LOC_TYPE_ID2]= ?" + 
						"           ,[LOC_TYPE_DESC2]= ?" + 
						"           ,[CRAT]= ?" + 
						"           ,[CRBY]= ?" + 
						"           ,[UPAT]= ?" + 
						"           ,[UPBY]= ?" + 
						"           ,[IsActive]= ?" + 
						"     WHERE LOC_TYPE_ID2 = ? ";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, lOC_TYPE_MST2.getPLANT());
				   ps.setString(2, lOC_TYPE_MST2.getLOC_TYPE_ID2());
				   ps.setString(3, lOC_TYPE_MST2.getLOC_TYPE_DESC2());
				   ps.setString(4, lOC_TYPE_MST2.getCRAT());
				   ps.setString(5, lOC_TYPE_MST2.getCRBY());
				   ps.setString(6, lOC_TYPE_MST2.getUPAT());
				   ps.setString(7, lOC_TYPE_MST2.getUPBY());
				   ps.setString(8, lOC_TYPE_MST2.getIsActive());
				  
				   ps.setString(9, lOC_TYPE_MST2.getLOC_TYPE_ID2());
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   updateFlag = true;
				   }
				   else
				   {
					   throw new SQLException("Updating Locationtypetwo Order failed, no rows affected.");
				   }
				   this.mLogger.query(this.printQuery, query);		
				}
//			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Location already added ");
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return updateFlag;
	}
	//imti update end
	
	//imti delete
	public boolean deleteLOC_TYPE_MST2(String plant,String id)  throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + plant + "_LOC_TYPE_MST2]");
			sql.append(" WHERE LOC_TYPE_ID2 = '" +id+"'");
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
		
		/*
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = lOC_TYPE_MST2.keys();
			for (int i = 0; i < lOC_TYPE_MST2.size(); i++) {
				String key = _StrUtils.fString((String) enum1.nextElement());
				String value = _StrUtils.fString((String) lOC_TYPE_MST2.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "["+lOC_TYPE_MST2.get(IDBConstants.PLANT)+"_"+TABLE_NAME +"]"+ "("
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
	} */

	public boolean update(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "["+htCondition.get(IDBConstants.PLANT)+"_"+TABLE_NAME+"]");
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE ");
			String conditon = formCondition(htCondition);
			sql.append(conditon);

			if (extCond.length() != 0) {
				sql.append(extCond);
			}

			flag = updateData(con, sql.toString());
			this.mLogger.query(this.printQuery, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return flag;
	}


	public ArrayList getLocByWMS(String plant,String user) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
                        UserLocUtil userLocUtil = new UserLocUtil();
		        userLocUtil.setmLogger(mLogger);
		        String condAssignedLocforUser = " "; 
			condAssignedLocforUser =  userLocUtil.getUserLocAssigned(user,plant,"LOC");
                       
			String sQry = "select distinct(loc) as loc,locdesc from "
					+ "["
					+ plant
					+ "_"
					+ "locmst] where plant='"
					+ plant
					+ "' AND ISACTIVE ='Y' and loc not like 'SHIPPINGAREA%' and loc not like 'TEMP_TO%' " +condAssignedLocforUser
					+ " ORDER BY loc, locdesc ";
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

	public ArrayList getStockMoveLocByWMS(String plant) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sHolding = "HOLDINGAREA";
			String sQry = "select distinct(loc),locdesc from "
					+ "["
					+ plant
					+ "_"
					+ "locmst] where plant='"
					+ plant
					+ "' AND ISACTIVE ='Y'  and loc not like 'SHIPPINGAREA%' and loc not like 'TEMP_TO%'";
			this.mLogger.query(this.printQuery, sQry.toString());

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
        
    public ArrayList getLocByWMS(String plant,String user,String loc) throws Exception {

              java.sql.Connection con = null;
              ArrayList al = new ArrayList();
              try {
                      con = com.track.gates.DbBean.getConnection();
                      UserLocUtil userLocUtil = new UserLocUtil();
                      userLocUtil.setmLogger(mLogger);
                      String condAssignedLocforUser = " "; 
                      condAssignedLocforUser =  userLocUtil.getUserLocAssigned(user,plant,"LOC");
                     
                      String sQry = "select distinct(loc) as loc,locdesc from "
                                      + "["
                                      + plant
                                      + "_"
                                      + "locmst] where plant='"
                                      + plant
                                      + "' AND ISACTIVE ='Y' and loc like '%"+loc+"%' OR locdesc like '"+loc+"%' and loc not like 'SHIPPINGAREA%'  and loc not like 'TEMP_TO%' " +condAssignedLocforUser
                                      + " ORDER BY loc ";
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
    
    public String getLocForLocType(String aPlant, String alocType) throws Exception {
		String strLoclist = "";
		ArrayList al =new ArrayList();

		  java.sql.Connection con = null;
        
          try {
                  con = com.track.gates.DbBean.getConnection();
               
               
                 
                  String sQry = "  select STUFF((    SELECT ',' + ''''+LOC+'''' AS [text()],',' + + '''SHIPPINGAREA_'+LOC+'''' AS [text()],',' + + '''TEMP_TO_'+ LOC+'''' AS [text()] from "
                                  + "["
                                  + aPlant
                                  + "_"
                                  + "locmst] where plant='"
                                  + aPlant
                                  + "' AND  LOC_TYPE_ID like '%"+alocType+"%'   FOR XML PATH('') ), 1, 1, '' )  AS LOC ";
                  this.mLogger.query(this.printQuery, sQry);

                  al = selectData(con, sQry);
                  if(al.size()>0){
                	  for (int i = 0; i < al.size(); i++) {
      					Map map = (Map) al.get(i);
      					strLoclist=(String) map.get("LOC");
                	  }
                  }

          } catch (Exception e) {
                  this.mLogger.exception(this.printLog, "", e);
                  throw e;
          } finally {
                  if (con != null) {
                          DbBean.closeConnection(con);
                  }
          }
          return strLoclist;
}
    
    public String getLocForLocType2(String aPlant, String alocType) throws Exception {
		String strLoclist = "";
		ArrayList al =new ArrayList();

		  java.sql.Connection con = null;
        
          try {
                  con = com.track.gates.DbBean.getConnection();
               
               
                 
                  String sQry = "  select STUFF((    SELECT ',' + ''''+LOC+'''' AS [text()],',' + + '''SHIPPINGAREA_'+LOC+'''' AS [text()],',' + + '''TEMP_TO_'+ LOC+'''' AS [text()] from "
                                  + "["
                                  + aPlant
                                  + "_"
                                  + "locmst] where plant='"
                                  + aPlant
                                  + "' AND  LOC_TYPE_ID2 like '%"+alocType+"%'   FOR XML PATH('') ), 1, 1, '' )  AS LOC ";
                  this.mLogger.query(this.printQuery, sQry);

                  al = selectData(con, sQry);
                  if(al.size()>0){
                	  for (int i = 0; i < al.size(); i++) {
      					Map map = (Map) al.get(i);
      					strLoclist=(String) map.get("LOC");
                	  }
                  }

          } catch (Exception e) {
                  this.mLogger.exception(this.printLog, "", e);
                  throw e;
          } finally {
                  if (con != null) {
                          DbBean.closeConnection(con);
                  }
          }
          return strLoclist;
}

    public boolean isExisit(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "["+ht.get("PLANT")+"_LOC_TYPE_MST2]");
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

}