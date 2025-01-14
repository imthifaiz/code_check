package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class CatalogDAO extends BaseDAO {
	public static String TABLE_NAME = "CATALOGMST";
	public String plant = "";

	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.INVMSTDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.INVMSTDAO_PRINTPLANTMASTERLOG;

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
	 public boolean saveMst(Hashtable ht) throws Exception {

			boolean insertFlag = false;
			java.sql.Connection conn = null;
			try {
				conn = DbBean.getConnection();
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = ht.keys();
				for (int i = 0; i < ht.size(); i++) {
					String key = StrUtils.fString((String) enumeration
							.nextElement());
					String value = StrUtils.fString((String) ht.get(key));
					FIELDS += key + ",";
					VALUES += "'" + value + "',";
				}
				
				String query = "INSERT INTO " + "["+ht.get("PLANT")+"_"+TABLE_NAME+"]" + "("
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
	 public ArrayList selectMst(String query, Hashtable ht, String extCondi)
		throws Exception {

	boolean flag = false;
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;

	StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
			+"["+ht.get("PLANT")+"_"+ TABLE_NAME+"]");
	String conditon = "";

	try {
		con = com.track.gates.DbBean.getConnection();

		if (ht.size() > 0) {
			sql.append(" WHERE ");
			conditon = formCondition(ht);
			sql.append(conditon);
		}
		if (extCondi.length() > 0)
			sql.append("  " + extCondi);
		
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
	 public ArrayList prevNextList(String query, Hashtable ht, String extCondi)
		throws Exception {

	boolean flag = false;
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;

	StringBuffer sql = new StringBuffer(" SELECT " + "distinct CATLOGID, PRODUCTID,PRICE,isnull(CATLOGPATH,'') CATLOGPATH ,DESCRIPTION1,DESCRIPTION2,DESCRIPTION3,ISNULL(DETAILLABEL,'') DETAILLABEL,ISACTIVE" + " from "
			+"["+ht.get("PLANT")+"_"+ TABLE_NAME+"]");
	String conditon = "",prdclass="",prdtype="";
	try {
		con = com.track.gates.DbBean.getConnection();
		if (ht.size() > 0) {
			sql.append(" WHERE ");
			conditon = formCondition(ht);
			sql.append(conditon);
		}
		if (extCondi.length() > 0)
			sql.append("  " + extCondi);
		//Get product class and product type
		Hashtable htprd = new Hashtable();
		htprd.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
		htprd.put(IDBConstants.ITEM, ht.get(IDBConstants.PRODUCTID));
		List prdlist = getProductclstype("distinct ITEM,ITEMDESC,UNITPRICE,COST,isnull(PRD_CLS_ID,'') PRD_CLS_ID,isnull(ITEMTYPE,'NOTYPE') ITEMTYPE ", htprd);
		for (int i = 0; i < prdlist.size(); i++) { 
			Map linearr = (Map)prdlist.get(i);
			prdclass = (String)linearr.get("PRD_CLS_ID");
			prdtype = (String)linearr.get("ITEMTYPE");
		}
System.out.println("cls+type+"+prdclass+prdtype);		

		sql.append(" UNION ALL ");
		sql.append( " SELECT " + "distinct CATLOGID, PRODUCTID,PRICE" );
		sql.append(",isnull(CATLOGPATH,'') CATLOGPATH ,DESCRIPTION1,DESCRIPTION2,DESCRIPTION3,ISNULL(DETAILLABEL,'') DETAILLABEL,A.ISACTIVE ISACTIVE from");
		sql.append("["+ht.get("PLANT")+"_"+ TABLE_NAME+"] a,");
		sql.append("["+ht.get("PLANT")+"_"+ "ITEMMST"+"] b ");
		sql.append(" WHERE A.ISACTIVE='Y' AND A.PRODUCTID=B.ITEM AND A.PRODUCTID<>'"+ht.get(IDBConstants.PRODUCTID)+"'");
		if(!prdclass.equalsIgnoreCase("NOCLASSIFICIATION"))
			sql.append("AND B.PRD_CLS_ID='"+prdclass+"'");
		if(!prdtype.equalsIgnoreCase("NOTYPE"))
			sql.append("AND B.ITEMTYPE='"+prdtype+"'");

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
	 public ArrayList getProductclstype(String query,Hashtable ht) throws Exception
	 {
		 List itemlist =null;
		 ItemMstDAO itemdao = new ItemMstDAO();
		 try {
			
			 itemlist=itemdao.selectItemMst(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
			
		 return (ArrayList) itemlist;
	 }
	 
	 public boolean updateMst(Hashtable htUpdate, Hashtable htCondition)
		throws Exception {
			boolean update = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sUpdate = " ", sCondition = " ";
			
				// generate the condition string
				Enumeration enumUpdate = htUpdate.keys();
				for (int i = 0; i < htUpdate.size(); i++) {
					String key = StrUtils
							.fString((String) enumUpdate.nextElement());
					String value = StrUtils.fString((String) htUpdate.get(key));
					sUpdate += key.toUpperCase() + " = '" + value + "',";
				}
			
				// generate the update string
				Enumeration enumCondition = htCondition.keys();
				for (int i = 0; i < htCondition.size(); i++) {
					String key = StrUtils.fString((String) enumCondition
							.nextElement());
					String value = StrUtils.fString((String) htCondition.get(key));
			
					sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
							+ "' AND ";
				}
				sUpdate = (sUpdate.length() > 0) ? " SET "
						+ sUpdate.substring(0, sUpdate.length() - 1) : "";
				sCondition = (sCondition.length() > 0) ? " WHERE  "
						+ sCondition.substring(0, sCondition.length() - 4) : "";
				String stmt = "UPDATE " + "[" + htCondition.get("PLANT") + "_"
						+ TABLE_NAME + "] " + sUpdate + sCondition;
				this.mLogger.query(this.printQuery, stmt);
				ps = con.prepareStatement(stmt);
				int iCnt = ps.executeUpdate();
				if (iCnt > 0)
					update = true;
			
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			
			return update;
		}
	 /**
		 * Author Bruhan. 12 july 2012 method :
		 * delMst(Hashtable htcondtn) description : Deletes the Calatog from the catalog master)
		 * 
		 * @param : Hashtable htcondtn
		 * @return : Boolean 
		 * @throws Exception
		 */
	public boolean delMst(Hashtable<String, String> ht) throws Exception {

		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + ht.get(IDBConstants.PLANT) + "_"
					+ TABLE_NAME + " ] WHERE " + formCondition(ht);
			this.mLogger.query(this.printQuery, sql.toString());
			this.DeleteRow(connection, sql);
			return Boolean.valueOf(true);
		} catch (Exception e) {
			MLogger.log(0, "[ERROR] : " + e);
			return Boolean.valueOf(false);
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
	}
	 public boolean isExisit(Hashtable ht, String extCond) throws Exception {

			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" ");
				sql.append(" FROM " +  "["+ht.get(IDBConstants.PLANT)+"_"+TABLE_NAME+"]");
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
	 public int getCount(Hashtable ht, String extCond) throws Exception {

			boolean flag = false;
			java.sql.Connection con = null;int count=0;
			try {
				con = com.track.gates.DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" ");
				sql.append(" FROM " + "["+ht.get(IDBConstants.PLANT)+"_"+TABLE_NAME+"]");
				sql.append(" WHERE  " + formCondition(ht));
				if (extCond.length() > 0)
					sql.append(" and " + extCond);

				this.mLogger.query(this.printQuery, sql.toString());

				count = countRows(con, sql.toString());
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}

			return count;

		}
	 
	 
	  public List getCatalogList(String plant,String aItem, String aItemDesc, String cond,int start,int end,String prdclsid,String prdtypeid,String prdbrandid) {
     
      List listQty = new ArrayList();
      Connection con = null;
      try {
              con = DbBean.getConnection();
              String  sCondition ="";
              String  sQryCondition = " WHERE ID BETWEEN "+start+" and  "+end+" ";
           
               sCondition = sCondition +  " WHERE REPLACE(DESCRIPTION1,' ','') LIKE '%"+new StrUtils().InsertQuotes(aItemDesc.replaceAll(" ",""))+"%' AND PRODUCTID LIKE  '"
                              + aItem.toUpperCase() + "%'";
  
               if(prdclsid != null && prdclsid!="") 
               {
               	sCondition = sCondition + " AND PRODUCTID IN(SELECT ITEM FROM  ["+ plant+ "_"+ "ITEMMST"+ "] WHERE PRD_CLS_ID='"+prdclsid+"')" ;
               }
               if(prdtypeid != null && prdtypeid!="") 
               {
               	sCondition = sCondition + " AND PRODUCTID IN(SELECT ITEM FROM  ["+ plant+ "_"+ "ITEMMST"+ "] WHERE ITEMTYPE='"+prdtypeid+"')" ;
               }
               if(prdbrandid != null && prdbrandid!="") 
               {
               	sCondition = sCondition + " AND PRODUCTID IN(SELECT ITEM FROM  ["+ plant+ "_"+ "ITEMMST"+ "] WHERE PRD_BRAND_ID='"+prdbrandid+"')" ;
               }
         
              String sQry = "SELECT * FROM " + 
              "(SELECT (ROW_NUMBER() OVER ( ORDER BY CATLOGID)) AS ID,CATLOGID, PRODUCTID,PRICE,isnull(CATLOGPATH,'') CATLOGPATH , "
              + "  DESCRIPTION1,DESCRIPTION2,DESCRIPTION3,ISACTIVE  FROM "
               + "[" + plant + "_" + "CATALOGMST " + "] "+sCondition+" )A" +sQryCondition + cond;
              this.mLogger.query(this.printQuery, sQry);
              listQty = selectData(con, sQry.toString());
      } catch (Exception e) {
              this.mLogger.exception(this.printLog, "", e);
      } finally {
              DbBean.closeConnection(con);
      }
      return listQty;
}
	  
		
		public ArrayList getAdditionalImg(String plant, String itemno
				) throws Exception {
			
			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();
				boolean flag = false;
				String sQry = "select ISNULL(CATLOGPATH,'') CATLOGPATH,ISNULL(USERTIME1,'2') USERTIME1 from" + "["
						+ plant + "_" + "CATALOGMST" + "]"
						+ " where PLANT='" + plant + "'   and PRODUCTID ='" + itemno + "' order by USERTIME1" ;
				
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
	  
	  public boolean saveUserImage(Hashtable ht,Hashtable htUpdate,Hashtable htCondition) throws Exception {
			boolean insertFlag = false;
			java.sql.Connection conn = null;
			try {
				conn = DbBean.getConnection();
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = ht.keys();
				for (int i = 0; i < ht.size(); i++) {
					String key = StrUtils.fString((String) enumeration
							.nextElement());
					String value = StrUtils.fString((String) ht.get(key));
					FIELDS += key + ",";
					VALUES += "'" + value + "',";
				}
				String sUpdate = " ", sCondition = " ";
				
				// generate the condition string
				Enumeration enumUpdate = htUpdate.keys();
				for (int i = 0; i < htUpdate.size(); i++) {
					String key = StrUtils
							.fString((String) enumUpdate.nextElement());
					String value = StrUtils.fString((String) htUpdate.get(key));
					sUpdate += key.toUpperCase() + " = '" + value + "',";
				}
			
				// generate the update string
				Enumeration enumCondition = htCondition.keys();
				for (int i = 0; i < htCondition.size(); i++) {
					String key = StrUtils.fString((String) enumCondition
							.nextElement());
					String value = StrUtils.fString((String) htCondition.get(key));
			 if(i>0){
				 sCondition += " and ";
			 }
					sCondition += key.toUpperCase() + " = '" + value.toUpperCase()+ "'";
				}
				sUpdate = (sUpdate.length() > 0) ? " SET "
						+ sUpdate.substring(0, sUpdate.length() - 1) : "";
				sCondition = (sCondition.length() > 0) ? " WHERE  "
						+ sCondition.substring(0, sCondition.length()) : "";
				String stmt = "UPDATE user_info " + sUpdate + sCondition;
				this.mLogger.query(this.printQuery, stmt);
				insertFlag = insertData(conn, stmt);

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
	  


}
