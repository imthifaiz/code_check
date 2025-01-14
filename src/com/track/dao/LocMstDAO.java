package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class LocMstDAO extends BaseDAO {
	public static final String TABLE_NAME = "LocMst";

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

	public LocMstDAO() {
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

	public boolean insertLocMst(Hashtable ht) throws Exception {
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
			String query = "INSERT INTO " + "["+ht.get(IDBConstants.PLANT)+"_"+TABLE_NAME +"]"+ "("
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
                     
                      String sQry = "select distinct(loc) as loc,locdesc,LOC_TYPE_ID,LOC_TYPE_ID2 from "
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
    
    public ArrayList getLocByWMSImport(String plant,String user,String loc) throws Exception {

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
                                + "' AND ISACTIVE ='Y' AND loc ='"+loc+"' " +condAssignedLocforUser
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
    public String getLocForLocType3(String aPlant, String alocType) throws Exception {
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
                                  + "' AND  LOC_TYPE_ID3 like '%"+alocType+"%'   FOR XML PATH('') ), 1, 1, '' )  AS LOC ";
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
	public ArrayList selectLocForInvPDA(String query, Hashtable ht, String extCond)
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
	//Created by vicky Desc:Used for fetching All locations details for PDA			
	public ArrayList getLocByWMSPDA(String plant,String user) throws Exception {
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
					+ "' AND ISACTIVE ='Y' and loc not like 'SHIPPINGAREA%' and loc not like 'TEMP_TO%' "
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
	
	public boolean isExistPRDLoc(String prd_loc_id,String prd_loc_desc, String plant) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + TABLE_NAME + "]" + " WHERE "
					+ IConstants.LOC + " = '"
					+ prd_loc_desc.toUpperCase() + "'";
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