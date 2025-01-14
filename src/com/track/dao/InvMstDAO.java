package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;
/*- ************Modification History*********************************
Jan 2, 2015,Bruhan, Description:New Method:getAssignInvLocByPDA,getAssignLocByPDA
*/


@SuppressWarnings({"rawtypes", "unchecked"})
public class InvMstDAO extends BaseDAO {
	public static String TABLE_NAME = "INVMST";
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

	public InvMstDAO() {

	}

	public InvMstDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "INVMST" + "]";

	}

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;

		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "INVMST" + "]");
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

	public Map selectRowINV(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "INVMST" + "]");
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

	public Map selectRow(String query, Hashtable ht, String extCond)
			throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("plant") + "_" + "INVMST" + "] WITH (READUNCOMMITTED)");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
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
	
	public Map selectRowExpiryDate(String query, Hashtable ht, String extCond)
			throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("plant") + "_" + "INVMST" + "]");
			sql.append(" WITH (READUNCOMMITTED) WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
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
	
	

	public String getItemLoc(String aPlant, String aItem, String extCond)
			throws Exception {
		String itemLoc = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("item", aItem);
		String query = " loc ";
		Map m = selectRow(query, ht, extCond);
		itemLoc = (String) m.get("loc");
		return itemLoc;
	}
	
	public String getItemimage(String aPlant, String aItem, String extCond)
			throws Exception {
		String itemLoc = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("item", aItem);
		String query = " CATLOGPATH ";
		Map m = selectRow(query, ht, extCond);
		itemLoc = (String) m.get("CATLOGPATH");
		return itemLoc;
	}

	public String getBatchQty(String aPlant, String aItem, String aBatch,
			String aLoc, String extCond) throws Exception {
		String qty = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("item", aItem);
		ht.put("userfld4", aBatch);
		ht.put("loc", aLoc);

		String query = " qty ";
		Map m = selectRow(query, ht, extCond);
		qty = (String) m.get("qty");
		if (qty == "" || qty == null)
			qty = "0";
		return qty;
	}

	/*public String getCustCode(String aPlant, String aItem) throws Exception {
		String itemLoc = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("item", aItem);
		String query = " ISNULL( " + IDBConstants.INVMST_CUST_CODE + ",'') as "
				+ IDBConstants.INVMST_CUST_CODE + " ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht);

		itemLoc = (String) m.get(IDBConstants.INVMST_CUST_CODE);
		return itemLoc;
	}*/

	public ArrayList selectInvMst(String query, Hashtable ht) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "INVMST" + "]");
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

	public ArrayList selectInvMstByCrat(String query, Hashtable ht)
			throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "INVMST" + "]");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			sql.append(" order by substring(convert(VARCHAR,CRAT,112),5,4) + '-' +       substring(convert(VARCHAR,CRAT,112),3,2) + '-' +       left(convert(VARCHAR,CRAT,112),2) asc");
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

	public ArrayList selectInvMst(String query, Hashtable ht, String extCondi)
			throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ (ht.get("plant") == null ? ht.get(IDBConstants.PLANT) : ht.get("plant")) + "_" + "INVMST" + "] A WITH (READUNCOMMITTED)");
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
			
			//sql.append(" order by substring(convert(VARCHAR,CRAT,112),5,4) + '-' +       substring(convert(VARCHAR,CRAT,112),3,2) + '-' +       left(convert(VARCHAR,CRAT,112),2) asc");
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
 /*---Added by Bruhan on July 9 2014, Description:PDA Inventory Query by location with location type
	 ********** Modification History *************************************
	 * 
	 * 
	 * 
	 */
	 public ArrayList selectInvMst(String query, Hashtable ht, String extCondi,String extCond2)
			throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("plant") + "_" + "INVMST" + "] WHERE PLANT <> ''");
		String conditon = "";
		
		try {
			con = com.track.gates.DbBean.getConnection();
			if (ht.size() > 0) {
				sql.append(" AND ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			if (extCond2.length() > 0)
				sql.append(" and " + extCond2);
			
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

	public ArrayList selectInvMstWithAlternateItem(String query, Hashtable ht,
			String extCondi) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("plant") + "_" + "INVMST" + "] invmst, ["
				+ ht.get("plant") + "_" + "ALTERNATE_ITEM_MAPPING] altitem ");

	
		String conditon = " WHERE invmst.plant='" + ht.get("plant") + "' "
				+ " AND altitem.ITEM = invmst.ITEM  " + extCondi;

		try {
			sql = sql.append(conditon);
			con = com.track.gates.DbBean.getConnection();

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

	public ArrayList selectInvMstDetails(String query, Hashtable ht,
			String extCondi) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
				+ TABLE_NAME);
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

	public ArrayList selectForReport(String query, Hashtable ht, String extCond)
			throws Exception {
//		boolean flag = false;
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(query);
			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" AND ");
				conditon = formCondition(ht);
				sql.append(" " + conditon);
			}

			if (extCond.length() > 0) {
				sql.append("  ");

				sql.append(" " + extCond);
			}
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

	public ArrayList selectReport(String query, Hashtable ht, String extCond)
			throws Exception {
//		boolean flag = false;
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(query);
			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(" " + conditon);
			}

			if (extCond.length() > 0) {
				sql.append("  ");

				sql.append(" " + extCond);
			}
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
	
	public boolean isExisit(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "INVMST" + "]");
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

	public boolean isExisitBomQty(Hashtable ht, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "INVMST" + "]");
			sql.append(" WHERE  " + formCondition(ht));
			if (extCond.length() > 0)
				sql.append(" and Qty >= 0 and " + extCond);

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

	public boolean isExisitKittingQty(Hashtable ht, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "INVMST" + "]");
			sql.append(" WHERE  QTY > 0 AND  " + formCondition(ht));
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

	public boolean insertInvMst(Hashtable ht) throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration
						.nextElement());
				//String value = StrUtils.fString((String) ht.get(key));
				String value = StrUtils.fString(String.valueOf(ht.get(key)));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "INVMST" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
System.out.println( query);
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
			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + "INVMST" + "]");
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
//System.out.print("inv update"+sql.toString());
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

	public boolean updateInvMstIssue(String query, Hashtable htCondition,
			String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + "INVMST" + "]");
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE QTY <=0 AND ");
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
			if (con != null) {
				DbBean.closeConnection(con);
			}
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
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_INVMST]");
			sql.append(" WHERE " + formCondition(ht));
		
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

	public String getExpireDate(String aPlant, String aItem, String batch)
			throws Exception {
		String itemLoc = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("item", aItem);
		ht.put("userfld4", batch);

		String query = " distinct isnull(userfld3,'') as userfld3";
		Map m = selectRowINV(query, ht);
		itemLoc = (String) m.get("userfld3");
		return itemLoc;
	}

	public String getInvExpireDate(String aPlant, String aItem, String loc,
			String batch) throws Exception {
		String itemLoc = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("item", aItem);
		ht.put("userfld4", batch);
		ht.put("loc", loc);

		String query = " distinct isnull(expiredate,'') as expiredate";
		Map m = selectRowINV(query, ht);
		itemLoc = (String) m.get("expiredate");
		return itemLoc;
	}

	public ArrayList getInvLocByWMS(String plant, String itemno, String loc)
			throws Exception {

		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
//			boolean flag = false;
			String sQry = "select loc,userfld4 as batch from " + "[" + plant
					+ "_" + "invmst" + "]" + " where plant='" + plant
					+ "'  and Qty > 0  and item = '" + itemno
					+ "' and loc like '" + loc + "%'";
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

	public ArrayList getPutAwayItemByWMS(String plant, String itemno)
			throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
//			boolean flag = false;
			String sQry = "select item,loc as FromLoc,Qty from " + "[" + plant
					+ "_" + "invmst" + "]" + " where plant='" + plant
					+ "'  and Qty > 0 and LOC='HOLDINGAREA' and item like '"
					+ itemno + "%'";
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

	public ArrayList getInvItemByWMS(String plant, String itemno,
			String itemDesc,String aLoc) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
			if (itemDesc.length() > 0) {
				itemDesc = " AND altitem.ITEM in (SELECT ITEM FROM " + plant
						+ "_ITEMMST WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(itemDesc.replaceAll(" ",""))+"%')" ;
				
			}
                        if (aLoc.length() > 0) {
                            aLoc = "   and loc = '"+ aLoc+"'";
                        }
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String sQry = "select distinct(invmst.item) item ,(select itemdesc from "+plant+"_ITEMMST where ITEM =invmst.ITEM) as itemDesc,(select stkuom from "+plant+"_ITEMMST where ITEM =invmst.ITEM) as uom from " + "["
					+ plant + "_" + "invmst" + "] invmst, [" + plant + "_"
					+ "ALTERNATE_ITEM_MAPPING] altitem  "
					+ " where invmst.plant='" + plant + "'  "+ aLoc
					+ " and Qty > 0  and  altitem.ALTERNATE_ITEM_NAME like '"
					+ itemno + "%' and altitem.ITEM = invmst.ITEM " + itemDesc
					+ " ORDER BY invmst.item ";
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
	
	
	public ArrayList getCustList(String plant, String custId) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			
			con = com.track.gates.DbBean.getConnection();
//			boolean flag = false;
			String sQry = "select CUSTNO, CNAME, IsActive,ISNULL(TAXTREATMENT,'') TAXTREATMENT from " + "["
					+ plant + "_" + "CUSTMST" + "]"
					+ " where plant='" + plant
					+ "' and CUSTNO LIKE '"
					+ custId + "%' OR CNAME LIKE '" + custId + "%'"
					+ " ORDER BY CUSTNO ";
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

	public ArrayList getPutAwayBatchByWMS(String plant, String itemno,
			String batch) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
//			boolean flag = false;
			String sQry = "select item,loc as FromLoc,Qty,Qty as CheckQty,userfld4 as Batch from "
					+ "["
					+ plant
					+ "_"
					+ "invmst"
					+ "]"
					+ " where plant='"
					+ plant
					+ "'  and Qty > 0 and LOC='HOLDINGAREA' and item='"
					+ itemno + "' and userfld4 like '" + batch + "%'";
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

	public ArrayList getLocationTransferLocByWMS(String plant, String fromloc)
			throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			String sQry = "select distinct loc as FromLoc from " + "[" + plant
					+ "_" + "invmst] where plant='" + plant
					+ "'  and Qty > 0   and  loc like '" + fromloc + "%' ";
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

	public ArrayList getStockTransferLocByWMS(String plant, String fromloc,
			String user) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			UserLocUtil userLocUtil = new UserLocUtil();
			userLocUtil.setmLogger(mLogger);
			String condAssignedLocforUser = " ";
			condAssignedLocforUser = userLocUtil.getUserLocAssigned(user,
					plant, "LOC");

			
			
			String sQry = "select distinct loc as FromLoc,locdesc from "
					+ "["
					+ plant
					+ "_"
					+ "LOCMST] WHERE LOC in (select distinct loc from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] A where plant='"
					+ plant
					+ "'  and Qty > 0   and  loc like '%"
					+ fromloc
					+ "%' and loc not like 'SHIPPINGAREA%' and loc not like 'TEMP_TO%' "
					+ condAssignedLocforUser + " ) "
					+ " ORDER BY loc ";
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

	public ArrayList getStockTransferLocByPDA(String plant, String fromloc,
			String user) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			UserLocUtil userLocUtil = new UserLocUtil();
			userLocUtil.setmLogger(mLogger);
			String condAssignedLocforUser = " ";
			condAssignedLocforUser = userLocUtil.getUserLocAssigned(user,
					plant, "LOC");

			String sQry = "select distinct loc as FromLoc,isnull(locdesc,'') as LocDesc from "
					+ "["
					+ plant
					+ "_"
					+ "locmst] where plant='"
					+ plant
					+ "' and  loc like '"
					+ fromloc
					+ "%' and loc not like 'SHIPPINGAREA%' and loc not like 'TEMP_TO%' and loc not like 'HOLDINGAREA%' and isActive='Y'"
					+ condAssignedLocforUser
					+ " ORDER BY loc ";
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

	public ArrayList getLocationTransferItemByWMS(String plant, String itemno,
			String loc) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
//			String strHolding = "HOLDINGAREA";
			String sQry = "select item,loc as FromLoc,Qty,isnull(userfld4,'NOBATCH') as Batch from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] where plant='"
					+ plant
					+ "'  and Qty > 0  and item like '"
					+ itemno
					+ "%' and   loc='" + loc + "' order by crat,item,userfld4";
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
	
	

	public ArrayList getLocationTransferItemWithOutLoc(String plant,
			String itemno, String itemDesc, String loc) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
			if (itemDesc.length() > 0) {
				itemDesc = "  and altitem.ALTERNATE_ITEM_NAME IN ( SELECT ITEM FROM "
						+ plant
						+ "_ITEMMST WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(itemDesc.replaceAll(" ",""))+"%')" ;	
			}
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
//			String strHolding = "HOLDINGAREA";
			String sQry = "select distinct invmst.item as item ,(select itemdesc +'||'+ stkuom from "+plant+"_ITEMMST where item = invmst.item) as ItemDesc from " + "["
					+ plant + "_" + "invmst] invmst, " + "[" + plant + "_"
					+ "ALTERNATE_ITEM_MAPPING] altitem where invmst.plant='"
					+ plant + "' and altitem.ITEM = invmst.ITEM  and loc = '"
					+ loc
					+ "'   and Qty > 0  and altitem.ALTERNATE_ITEM_NAME like '"
					+ itemno + "%' " + itemDesc
					+ " ORDER BY invmst.item ";
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

	public ArrayList getLocationTransferBatch(String plant, String itemno,
			String loc, String batch) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
//			boolean flag = false;
			if(batch.equalsIgnoreCase("NOBATCH"))
			{
				batch = "";
			}
			String sQry = "select isnull(userfld4,'') as batch,isnull(Qty,0) as qty,ISNULL(expiredate,'') as expiredate from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] where plant='"
					+ plant
					+ "'  and loc = '"
					+ loc
					+ "'   and Qty > 0  and item = '"
					+ itemno + "' and userfld4 like '" + batch + "%' ORDER BY userfld4 ";
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

	public ArrayList getMiscIssueLocByWMS(String plant, String itemno,
			String loc, String userID) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			String extraLocCondForUer = uslocUtil.getUserLocAssigned(userID,
					plant, "LOC");
//			boolean flag = false;
			String sQry = "select  distinct isnull(loc,'') loc,USERFLD4 as batch,qty from "
					+ "["
					+ plant
					+ "_"
					+ "INVMST] where plant='"
					+ plant
					+ "' and item = '"
					+ itemno
					+ "' and loc like '"
					+ loc
					+ "%' and loc <>'TRANSITAREA' and qty > 0 and loc not like 'SHIPPINGAREA%' and loc not like 'TEMP_TO%' "
					+ extraLocCondForUer;
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

	public ArrayList getOutBoundPickingLocByWMS(String plant, String item,
			String loc, String userId) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			UserLocUtil userLocUtil = new UserLocUtil();
			userLocUtil.setmLogger(mLogger);
			String condAssignedLocforUser = " ";
			condAssignedLocforUser = userLocUtil.getUserLocAssigned(userId,
					plant, "LOC");
//			boolean flag = false;
			String sQry = "select distinct LOC,isnull((select locdesc from "
					+ plant
					+ "_locmst where loc =a.loc),'') as LOCDESC from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] a where plant='"
					+ plant
					+ "' and item='"
					+ item
					+ "' and loc like '"
					+ loc
					+ "%' and qty>0 and loc not like 'SHIPPINGAREA%' and loc not like 'TEMP_TO%'"
					+ condAssignedLocforUser
					+ " ORDER BY loc ";
			mLogger.query(this.printQuery, sQry);
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

	public ArrayList getOutBoundIssueLocByWMS(String plant, String item,
			String loc) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
//			boolean flag = false;
			String sQry = "select distinct(loc) from " + "[" + plant + "_"
					+ "invmst] where plant='" + plant + "' and item='" + item
					+ "' and loc like '" + loc
					+ "%' and qty>0 and loc = 'SHIPPINGAREA'";
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

	public ArrayList getTotalQuantityForOutBoundPickingBatchByWMS(String plant, String item,
			String loc, String batch) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String batchCond="";
		if(batch.length()>0){
			batchCond =" and  userfld4 = '"+ batch+"' ";
		}else{
			batchCond =" and  userfld4 like '"+ batch+"%' ";
		}
			String sQry = "with tempdb as (select distinct userfld4 as batch,SUM(qty) AS STKQTY from "
				+ "["
				+ plant
				+ "_"
				+ "invmst] where plant='"
				+ plant
				+ "' and item='"
				+ item
				+ "' and loc ='"
				+ loc + "' " + batchCond
				+ " and qty>0  AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%' "
				+ " GROUP BY userfld4 )"
				+ "select batch,STKQTY as qty from tempdb b";
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
	public ArrayList getOutBoundPickingBatchByWMS(String plant, String item,
			String loc, String batch) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String batchCond="";
		if(batch.length()>0){
			batchCond =" and  userfld4 = '"+ batch+"' ";
		}else{
			batchCond =" and  userfld4 like '"+ batch+"%' ";
		}
			String sQry = "select userfld4 as batch,sum(qty) as qty,'' as uom,(select STKQTY from "+ plant+"_ITEMMST where ITEM = '"+ item+"') as MINSTKQTY,'' as crat,'' expirydate, -1 as id  from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] where plant='"
					+ plant
					+ "' and item='"
					+ item
					+ "' and loc ='"
					+ loc + "' " + batchCond
					+ " and qty>0  AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%' and userfld4='NOBATCH' "
					+ " GROUP BY userfld4"
					+ " UNION ALL "					
					+ "select distinct userfld4 as batch,qty,isnull(userfld5,'') as uom,(select STKQTY from "+ plant+"_ITEMMST where ITEM = '"+ item+"') as MINSTKQTY,SUBSTRING(crat,1,2)+'/'+ ISNULL(SUBSTRING(crat,3,2)+'/'+ SUBSTRING(crat,5,4),'') as crat,isnull(expiredate,'') expirydate, id  from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] where plant='"
					+ plant
					+ "' and item='"
					+ item
					+ "' and loc ='"
					+ loc + "' " + batchCond
					+ " and qty>0  AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%' and userfld4!='NOBATCH' "
					+ " ORDER BY crat ";
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
	
	public ArrayList getOutBoundPickingBatchBYPDA(String plant, String item,
			String loc, String batch,String invid) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String batchCond="";
		if(batch.length()>0){
			batchCond =" and  userfld4 = '"+ batch+"' ";
		}else{
			batchCond =" and  userfld4 like '"+ batch+"%' ";
		}
		if(invid.length()>0){
			batchCond =" and  id = '"+ invid+"' ";
		}
			String sQry = "select distinct userfld4 as batch,qty,isnull(userfld5,'') as uom,SUBSTRING(crat,1,2)+'/'+ ISNULL(SUBSTRING(crat,3,2)+'/'+ SUBSTRING(crat,5,4),'') as crat,isnull(expiredate,'') expirydate, id  from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] where plant='"
					+ plant
					+ "' and item='"
					+ item
					+ "' and loc ='"
					+ loc + "' " + batchCond
					+ " and qty>0  AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%' "
					+ " ORDER BY crat ";
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
	
	public ArrayList getBatchByWMS(String plant, String batch) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
//			if(batch.equalsIgnoreCase("NOBATCH"))
//				batch = "";
			String sQry = "select distinct userfld4 as batch,qty,isnull(userfld5,'') as uom from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] where plant='"
					+ plant
					+ "' and  userfld4 like '"
					+ batch+"%'"
					+ " and qty>0 "
					+ " ORDER BY userfld4 ";
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
	//end

	public ArrayList getAvaliableQtyForBatch(String plant, String item,
			String loc, String batch) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String sQry = "select distinct userfld4 as batch,qty from " + "["
					+ plant + "_" + "invmst] where plant='" + plant
					+ "' and item='" + item + "' and loc ='" + loc
					+ "' and  userfld4 like '" + batch + "' and qty>0";
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

	public ArrayList getOutBoundIssueBatchByWMS(String plant, String item,
			String loc, String batch) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
//			boolean flag = false;
			String sQry = "select distinct userfld4 as batch,qty from " + "["
					+ plant + "_" + "invmst] where plant='" + plant
					+ "' and item='" + item + "' and loc ='" + loc
					+ "' and  userfld4 like '" + batch
					+ "%' and qty>0 and loc = 'SHIPPINGAREA'";
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

	public ArrayList getToReceiveBatchByWMS(String plant, String item,
			String loc, String batch) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String sQry = "select distinct(userfld4) as batch,qty from " + "["
					+ plant + "_" + "invmst] where plant='" + plant
					+ "' and item='" + item + "' and loc='" + loc
					+ "' and userfld4 like '" + batch + "%' and qty>0 ";
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

	public String getChildKittingItem(String plant, String item) {
//		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();

			String query = " SELECT ITEM FROM [" + plant + "_"
					+ "INVMST] WHERE PLANT='" + plant + "' AND ITEM='" + item
					+ "' ";

			this.mLogger.query(this.printQuery, query);
			Map m = getRowOfData(conn, query);

			return (String) m.get("ITEM");
		} catch (Exception e) {
		
			return null;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}

	}

	public String getChildKittingInvBatch(String aPlant, String aItem,
			String aLoc, String aBatch) throws Exception {
		String batch = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("item", aItem);
		ht.put("loc", aLoc);
		ht.put("userfld4", aBatch);

		String query = " isnull(userfld4,'') as Batch ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			batch = (String) m.get("Batch");
		} else {
			batch = "";
		}
		if (batch.equalsIgnoreCase(null) || batch.length() == 0) {
			batch = "";
		}
		return batch;
	}

	public String getChildKittingInvQty(String aPlant, String aItem,
			String aLoc, String aBatch) throws Exception {
		String qty = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("item", aItem);
		ht.put("loc", aLoc);
		ht.put("userfld4", aBatch);

		String query = " isnull(qty,0) qty ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			qty = (String) m.get("qty");
		} else {
			qty = "";
		}
		if (qty.equalsIgnoreCase(null) || qty.length() == 0) {
			qty = "";
		}
		return qty;
	}

	public ArrayList getKittingChildProducts(String plant, String user,
			String parentloc) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			String sQry = "select distinct a.item, b.itemdesc  from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] a, "
					+ "["
					+ plant
					+ "_"
					+ "itemmst] b where a.plant='"
					+ plant
					+ "'  and a.loc='"
					+ parentloc
					+ "'  and a.qty>0 and a.item=b.item and isnull(b.userfld1,'') not in('k')  ORDER BY a.item ";

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

	public ArrayList getKittingChildBatch(String plant, String user,
			String parentloc, String childproduct) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			String sQry = "select isnull(userfld4,'') Batch,qty from " + "["
					+ plant + "_" + "invmst]  where plant='" + plant
					+ "'  and item='" + childproduct + "'  and loc='"
					+ parentloc + "'  and qty>0 ORDER BY userfld4 ";

			// this.mLogger.query(this.printQuery, sQry);

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

	public ArrayList getKittingCheckBatch(String plant, String user,
			String parentbatch) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			String sQry = "select isnull(userfld4,'') as Batch,isnull(Loc,'') Loc,isnull(Item,'') Item from "
					+ "["
					+ plant
					+ "_"
					+ "invmst]  where plant='"
					+ plant
					+ "'  and userfld4='" + parentbatch + "'and qty > 0";

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

	public String getInvExpiryDate(String aPlant, String aItem, String aLoc,
			String aBatch) throws Exception {
		String expiredate = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("item", aItem);
		ht.put("loc", aLoc);
		ht.put("userfld4", aBatch);
		//ht.put("id", Invid);

		String query = " isnull(expiredate,'') as ExpireDate ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			expiredate = (String) m.get("ExpireDate");
		} else {
			expiredate = "";
		}
		if (expiredate.equalsIgnoreCase(null) || expiredate.length() == 0) {
			expiredate = "";
		}
		return expiredate;
	}

	public String getInvExpiryDatekitting(String aPlant, String aItem,
			String aBatch) throws Exception {
		String expiredate = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("item", aItem);
		ht.put("userfld4", aBatch);

		String query = " isnull(expiredate,'') as ExpireDate ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			expiredate = (String) m.get("ExpireDate");
		} else {
			expiredate = "";
		}
		if (expiredate.equalsIgnoreCase(null) || expiredate.length() == 0) {
			expiredate = "";
		}
		return expiredate;
	}

	public ArrayList selectForReportWithExpDate(String query, Hashtable ht,
			String extCond) throws Exception {
//		boolean flag = false;
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(query);
			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" AND ");
				conditon = formConditionWithExoDate(ht);
				sql.append(" " + conditon);
			}

			if (extCond.length() > 0) {
				sql.append("  ");

				sql.append(" " + extCond);
			}
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
	
	//---Added by Bruhan on March 5 2014, Description: Get QC Reverse location from Invmst --ManufacturingModuelsChange
			public ArrayList getQCReverseLocByWMS(String plant, String fromloc,
					String user) throws Exception {

				java.sql.Connection con = null;
				ArrayList al = new ArrayList();
				try {
					con = com.track.gates.DbBean.getConnection();
					UserLocUtil userLocUtil = new UserLocUtil();
					userLocUtil.setmLogger(mLogger);
					String sQry = "select distinct loc as FromLoc from "
							+ "["
							+ plant
							+ "_"
							+ "invmst]"
							+ "where plant='"
							+ plant
							+ "'  and Qty > 0   and  loc like '%"
							+ fromloc
							+ "%' and loc not like 'SHIPPINGAREA%' and loc not like 'TEMP_TO%' and loc  like 'QC_%' and loc not like 'WIP_%'"
							+ " ORDER BY loc ";
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
			
			//---Added by Bruhan on March 5 2014, Description: Get WIP Reverse location from Invmst --ManufacturingModuelsChange
			public ArrayList getWIPReverseLocByWMS(String plant, String fromloc,
					String user) throws Exception {

				java.sql.Connection con = null;
				ArrayList al = new ArrayList();
				try {
					con = com.track.gates.DbBean.getConnection();
					UserLocUtil userLocUtil = new UserLocUtil();
					userLocUtil.setmLogger(mLogger);
					String sQry = "select distinct loc as FromLoc from "
							+ "["
							+ plant
							+ "_"
							+ "invmst]"
							+ "where plant='"
							+ plant
							+ "'  and Qty > 0   and  loc like '%"
							+ fromloc
							+ "%' and loc not like 'SHIPPINGAREA%' and loc not like 'TEMP_TO%' and loc not like 'QC_%' and loc like 'WIP_%'"
							+ " ORDER BY loc ";
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
			
	
			public ArrayList getAssignLocByPDA(String plant, String fromloc,
					String user) throws Exception {

				java.sql.Connection con = null;
				ArrayList al = new ArrayList();
				try {
					con = com.track.gates.DbBean.getConnection();
					UserLocUtil userLocUtil = new UserLocUtil();
					userLocUtil.setmLogger(mLogger);
					String condAssignedLocforUser = " ";
					condAssignedLocforUser = userLocUtil.getUserLocAssigned(user,
							plant, "LOC");

					String sQry = "select distinct loc as FromLoc,isnull(locdesc,'') as LocDesc from "
							+ "["
							+ plant
							+ "_"
							+ "locmst] where plant='"
							+ plant
							+ "' and  loc like '"
							+ fromloc
							+ "%' and loc not like 'SHIPPINGAREA%' and loc not like 'TEMP_TO%' and loc not like 'HOLDINGAREA%' and loc not like 'HOLDING_AREA%' and isActive='Y'"
							+ condAssignedLocforUser
							+ " ORDER BY loc ";
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
			
			

			public ArrayList getAssignInvLocByPDA(String plant, String loc,String item,
					String user) throws Exception {

				java.sql.Connection con = null;
				ArrayList al = new ArrayList();
				try {
					con = com.track.gates.DbBean.getConnection();
					UserLocUtil userLocUtil = new UserLocUtil();
					userLocUtil.setmLogger(mLogger);
					String condAssignedLocforUser = " ";
					condAssignedLocforUser = userLocUtil.getUserLocAssigned(user,
							plant, "LOC");

										
					String sQry = "select distinct loc as Loc,LocDesc from "
							+ "["
							+ plant
							+ "_"
							+ "LOCMST] WHERE LOC in (select distinct loc from "
							+ "["
							+ plant
							+ "_"
							+ "invmst] A where plant='"
							+ plant
							+ "' and item='"
							+ item
							+ "'  and Qty > 0   and  loc like '%"
							+ loc
							+ "%' and loc not like 'SHIPPINGAREA%' and loc not like 'TEMP_TO%' and loc <> 'HOLDING_AREA' "
							+ condAssignedLocforUser + " ) "
							+ " ORDER BY loc ";
					
					
					
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

			public ArrayList getWMSKittingCheckBatch(String plant, String item,String loc,String batch) throws Exception {

				java.sql.Connection con = null;
				ArrayList al = new ArrayList();
				try {
					con = com.track.gates.DbBean.getConnection();

					String sQry = "select isnull(userfld4,'') as Batch,isnull(Loc,'') Loc,isnull(Item,'') Item from "
							+ "["
							+ plant
							+ "_"
							+ "invmst]  where plant='"
							+ plant
							+ "' and item='" +item  + "' and userfld4='" +batch + "'  and loc='" + loc + "'and qty > 0";

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
			
public String getItemTotalInvQty(String aPlant, String aItem,String aLoc) throws Exception {
	String qty = "";
		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("item", aItem);
		ht.put("loc", aLoc);
		
		String query = " isnull(sum(QTY),0) qty ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			qty = (String) m.get("qty");
				} else {
					qty = "";
				}
				if (qty.equalsIgnoreCase(null) || qty.length() == 0) {
					qty = "";
				}
				return qty;
	}
public ArrayList getinvbatchqty(String plant, String item,String loc) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();

		String sQry = "select isnull(userfld4,'') as batch,isnull(sum(QTY),0) qty from "
				+ "["
				+ plant
				+ "_"
				+ "invmst]  where plant='"
				+ plant
				+ "' and item='" +item + "'  and loc='" + loc + "' and qty > 0 group by userfld4";

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
	public ArrayList selectForReport(String query) throws Exception {
//		boolean flag = false;
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
	
	public ArrayList ValidateOBLocation(String plant, String dono,String item,String loc) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			String sQry = "select item from" + "["+plant+ "_"+ "invmst] where item='" +item + "' and loc='" + loc + "'"
					+ " and item in (select item from"  + "["+plant+ "_"+ "dodet] where " 
					+ " dono='" +dono + "' and pickstatus <>'C')"
					+ " and loc in (select loc from " + "["+plant+ "_"+ "locmst] where loc='" + loc + "' and isactive='Y')";
				
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
	
	public ArrayList ValidateGoodsIssueLocation(String plant, String item,String loc) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			String sQry = "select item from" + "["+plant+ "_"+ "invmst] where item='" +item + "' and loc='" + loc + "'";
					
				
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
	
	public ArrayList getOutBoundPickingBatchIdByWMS(String plant, String item,
			String loc, String batch, String batchId) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String batchCond="";
			String batchIdCond="";
			
			if(batchId != "" )
			{
				
				 	if(!batchId.equals("-1"))
					{
						batchIdCond =" and  Id = '"+ batchId+"' ";
					}		
			}
			
		if(batch.length()>0){
			batchCond =" and  userfld4 = '"+ batch+"' ";
		}else{
			batchCond =" and  userfld4 like '"+ batch+"%' ";
		}
			String sQry = "select distinct userfld4 as batch,qty,isnull(userfld5,'') as uom,SUBSTRING(crat,1,2)+'/'+ ISNULL(SUBSTRING(crat,3,2)+'/'+ SUBSTRING(crat,5,4),'') as crat,isnull(expiredate,'') expirydate, id  from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] where plant='"
					+ plant
					+ "' and item='"
					+ item
					+ "' and loc ='"
					+ loc + "' " + batchCond + batchIdCond
					+ " and qty>0  AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%' "
					+ " ORDER BY crat ";
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
	
	public ArrayList selectInvMstDes(String query, Hashtable ht, String extCondi)
			throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ (ht.get("plant") == null ? ht.get(IDBConstants.PLANT) : ht.get("plant")) + "_" + "INVMST" + "] A WITH (READUNCOMMITTED) ");
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
			
			sql.append(" order by ID desc");
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
	public ArrayList getTotalQuantityForOutBoundPickingBatchByWMSMuliUOM(String plant, String item,
			String loc, String batch, String uom) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String batchCond="";
		if(batch.length()>0){
			batchCond =" and  userfld4 = '"+ batch+"' ";
		}else{
			batchCond =" and  userfld4 like '"+ batch+"%' ";
		}
			String sQry = "with tempdb as (select distinct userfld4 as batch,SUM(qty) AS STKQTY,ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1) as UOMQTY from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] where plant='"
					+ plant
					+ "' and item='"
					+ item
					+ "' and loc ='"
					+ loc + "' " + batchCond
					+ " and qty>0  AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%' "
					+ " GROUP BY userfld4 )"
					+ "select batch,convert(int,(STKQTY/UOMQTY)) qty from tempdb b";
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
	public ArrayList getOutBoundPickingBatchByWMSMultiUOM(String plant, String item,
			String loc, String batch, String uom) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String batchCond="";
		if(batch.length()>0){
			batchCond =" and  userfld4 = '"+ batch+"' ";
		}else{
			batchCond =" and  userfld4 like '"+ batch+"%' ";
		}
			String sQry = "with tempdb as ( select userfld4 as batch,sum(qty) as STKQTY,'' as uom,'' as crat,'' expirydate, -1 as id,ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1) as UOMQTY  from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] where plant='"
					+ plant
					+ "' and item='"
					+ item
					+ "' and loc ='"
					+ loc + "' " + batchCond
					+ " and qty>0  AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%' and userfld4='NOBATCH' "
					+ " GROUP BY userfld4"
					+ " UNION ALL "					
					+ "select distinct userfld4 as batch,qty as STKQTY,isnull(userfld5,'') as uom,SUBSTRING(crat,1,2)+'/'+ ISNULL(SUBSTRING(crat,3,2)+'/'+ SUBSTRING(crat,5,4),'') as crat,isnull(expiredate,'') expirydate, id,ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1) as UOMQTY  from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] where plant='"
					+ plant
					+ "' and item='"
					+ item
					+ "' and loc ='"
					+ loc + "' " + batchCond
					+ " and qty>0  AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%' and userfld4!='NOBATCH' )"
					+ "select batch,convert(int,(STKQTY/UOMQTY)) qty,uom,crat,expirydate,id,case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end pcsqty,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ plant +"_UOM u where u.UOM='"+uom+"'),'') as pcsuom from tempdb b ORDER BY crat ";
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
	public ArrayList getOutBoundPickingBatchByWMSMultiUOMWithNegQty(String plant, String item,
			String loc, String batch, String uom) throws Exception {
		
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			
//			boolean flag = false;
			String batchCond="";
			if(batch.length()>0){
				batchCond =" and  userfld4 = '"+ batch+"' ";
			}else{
				batchCond =" and  userfld4 like '"+ batch+"%' ";
			}
			String sQry = "with tempdb as ( select userfld4 as batch,sum(qty) as STKQTY,'' as uom,'' as crat,'' expirydate, -1 as id,ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1) as UOMQTY  from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] where plant='"
					+ plant
					+ "' and item='"
					+ item
					+ "' and loc ='"
					+ loc + "' " + batchCond
					+ " AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%' and userfld4='NOBATCH' "
					+ " GROUP BY userfld4"
					+ " UNION ALL "					
					+ "select distinct userfld4 as batch,qty as STKQTY,isnull(userfld5,'') as uom,SUBSTRING(crat,1,2)+'/'+ ISNULL(SUBSTRING(crat,3,2)+'/'+ SUBSTRING(crat,5,4),'') as crat,isnull(expiredate,'') expirydate, id,ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1) as UOMQTY  from "
					+ "["
					+ plant
					+ "_"
					+ "invmst] where plant='"
					+ plant
					+ "' and item='"
					+ item
					+ "' and loc ='"
					+ loc + "' " + batchCond
					+ " AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%' and userfld4!='NOBATCH' )"
					+ "select batch,convert(int,(STKQTY/UOMQTY)) qty,uom,crat,expirydate,id,case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end pcsqty,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ plant +"_UOM u where u.UOM='"+uom+"'),'') as pcsuom from tempdb b ORDER BY crat ";
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
	public ArrayList getAvaliableQtyForBatchMutiUOM(String plant, String item,
			String loc, String batch, String uom) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String sQry = "select distinct userfld4 as batch,convert(int,(qty/(ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1)))) qty from " + "["
					+ plant + "_" + "invmst] where plant='" + plant
					+ "' and item='" + item + "' and loc ='" + loc
					+ "' and  userfld4 like '" + batch + "' and qty>0";
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
	public ArrayList getAvaliableQtyForBatchMutiUOMPDA(String plant, String item,
			String loc, String batch, String uom) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String sQry = "with tempdb as ( select isnull(item,'') item,isnull(userfld4,'') userfld4,isnull(loc,'') loc,isnull(qty,0) STKQTY," 
	        		 +" SUBSTRING(crat,1,2)+'/'+ ISNULL(SUBSTRING(crat,3,2)+'/'+ SUBSTRING(crat,5,4),'') as CRAT,ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1) as UOMQTY," 
	         		+"(SELECT ISNULL(NONSTKFLAG,'') from "+ plant + "_ITEMMST WHERE ITEM=A.ITEM) ISNONSTOCK,ID,ISNULL(EXPIREDATE,'') EXPIRYDATE "
			+ " from ["+ plant + "_" + "invmst] A where plant='" + plant
			+ "' and item='" + item + "' and loc ='" + loc
			+ "' and  userfld4 LIKE '%' and qty>0 )"
			+ "select item,userfld4,loc,convert(int,(STKQTY/UOMQTY)) qty,CRAT,ISNONSTOCK,id,EXPIRYDATE,case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end pcsqty from tempdb b ORDER BY userfld4,CRAT ";
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
	
	public ArrayList getAvaliableQtyForSingleBatchMutiUOMPDA(String plant, String item,
			String loc, String batch, String uom) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String sQry = "with tempdb as ( select isnull(item,'') item,isnull(userfld4,'') userfld4,isnull(loc,'') loc,isnull(qty,0) STKQTY," 
	        		 +" SUBSTRING(crat,1,2)+'/'+ ISNULL(SUBSTRING(crat,3,2)+'/'+ SUBSTRING(crat,5,4),'') as CRAT,ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1) as UOMQTY," 
	         		+"(SELECT ISNULL(NONSTKFLAG,'') from "+ plant + "_ITEMMST WHERE ITEM=A.ITEM) ISNONSTOCK,ID,ISNULL(EXPIREDATE,'') EXPIRYDATE "
			+ " from ["+ plant + "_" + "invmst] A where plant='" + plant
			+ "' and item='" + item + "' and loc ='" + loc
			+ "' and  userfld4 LIKE '%"+batch+"%' and qty>0 )"
			+ "select item,userfld4,loc,convert(int,(STKQTY/UOMQTY)) qty,CRAT,ISNONSTOCK,id,EXPIRYDATE,case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end pcsqty from tempdb b ORDER BY userfld4,CRAT ";
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
	
	public ArrayList getAvaliableQtyForBatchMutiUOMPDASpinner(String plant, String item,
			String loc, String batch, String uom) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String sQry = "with tempdb as ( select isnull(item,'') item,isnull(userfld4,'') userfld4,isnull(loc,'') loc,isnull(qty,0) STKQTY," 
	        		 +" SUBSTRING(crat,1,2)+'/'+ ISNULL(SUBSTRING(crat,3,2)+'/'+ SUBSTRING(crat,5,4),'') as CRAT,ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1) as UOMQTY," 
	         		+"(SELECT ISNULL(NONSTKFLAG,'') from "+ plant + "_ITEMMST WHERE ITEM=A.ITEM) ISNONSTOCK,ID,ISNULL(EXPIREDATE,'') EXPIRYDATE "
			+ " from ["+ plant + "_" + "invmst] A where plant='" + plant
			+ "' and item='" + item + "' and loc ='" + loc
			+ "' and  userfld4 LIKE '"+batch+"%' and qty>0 )"
			+ "select item,userfld4,UOMQTY,loc,convert(int,(STKQTY/UOMQTY)) qty,CRAT,ISNONSTOCK,id,EXPIRYDATE,case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end pcsqty from tempdb b ORDER BY userfld4,CRAT ";
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
	
	public ArrayList selectInvMstByCrat(String query, Hashtable ht, String extCondi)
			throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ (ht.get("plant") == null ? ht.get(IDBConstants.PLANT) : ht.get("plant")) + "_" + "INVMST" + "] A ");
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
			
			sql.append(" order by substring(convert(VARCHAR,CRAT,112),5,4) + '-' +       substring(convert(VARCHAR,CRAT,112),3,2) + '-' +       left(convert(VARCHAR,CRAT,112),2) asc");
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
	
	/*Start code by Abhilash on 02-11-2019*/
	public String getStockOnHandByProduct(String aItem, String aPlant)
			throws Exception {
		java.sql.Connection con = null;
		String stockOnHand = "";
		try {
			con = com.track.gates.DbBean.getConnection();
			String query = "SELECT ISNULL(SUM(QTY),0) AS STOCKONHAND FROM ["+aPlant+"_INVMST] WHERE ITEM=? AND ITEM IN (SELECT ITEM FROM "+aPlant+"_ITEMMST WHERE NONSTKFLAG <> 'Y') AND PLANT=?";
			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,aItem);  
			stmt.setString(2,aPlant);  
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){  
				stockOnHand = rs.getString("STOCKONHAND");
			}
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return stockOnHand;
	}
	/*End code by Abhilash on 02-11-2019*/
	
	/*start code by vicky on 10-11-2019*/
	public ArrayList getAvaliableQtyForInvProductUOMPDA(String plant, String item,
			 String uom) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String sQry = "with tempdb as ( select isnull(item,'') item,isnull(userfld4,'') userfld4,isnull(loc,'') loc,isnull(qty,0) STKQTY," 
	        		 +" SUBSTRING(crat,1,2)+'/'+ ISNULL(SUBSTRING(crat,3,2)+'/'+ SUBSTRING(crat,5,4),'') as CRAT,ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1) as UOMQTY," 
	         		+"(SELECT ISNULL(NONSTKFLAG,'') from "+ plant + "_ITEMMST WHERE ITEM=A.ITEM) ISNONSTOCK,ID,ISNULL(EXPIREDATE,'') EXPIRYDATE "
			+ " from ["+ plant + "_" + "invmst] A where plant='" + plant
			+ "' and item='" + item 
			+ "' and  userfld4 LIKE '%' and qty>0 )"
			+ "select item,userfld4,loc,convert(int,(STKQTY/UOMQTY)) qty,CRAT,ISNONSTOCK,id,EXPIRYDATE,case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end pcsqty from tempdb b ORDER BY item,loc ";
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
	public ArrayList getAvaliableQtyForInvLocationUOMPDA(String plant, String loc,
			 String uom) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;
			String sQry = "with tempdb as ( select isnull(item,'') item,isnull(userfld4,'') userfld4,isnull(loc,'') loc,isnull(qty,0) STKQTY," 
	        		 +" SUBSTRING(crat,1,2)+'/'+ ISNULL(SUBSTRING(crat,3,2)+'/'+ SUBSTRING(crat,5,4),'') as CRAT,ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1) as UOMQTY," 
	         		+"(SELECT ISNULL(NONSTKFLAG,'') from "+ plant + "_ITEMMST WHERE ITEM=A.ITEM) ISNONSTOCK,ID,ISNULL(EXPIREDATE,'') EXPIRYDATE "
			+ " from ["+ plant + "_" + "invmst] A where plant='" + plant
			+ "' and loc='" + loc 
			+ "' and  userfld4 LIKE '%' and qty>0 )"
			+ "select item,userfld4,loc,convert(int,(STKQTY/UOMQTY)) qty,CRAT,ISNONSTOCK,id,EXPIRYDATE,case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end pcsqty from tempdb b ORDER BY item,loc ";
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
	/*End code by vicky on 10-11-2019*/
	
	public Map getAvailableQtyByProduct(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		String query="";//, item = "", plant = "";
		Map map = new HashMap();
		try {	    
			con = com.track.gates.DbBean.getConnection();
//			item = (String) ht.get("item");
			plant = (String) ht.get("plant");			
			query = "SELECT (QTY - ORDQTY - ESTQTY) AVLBQTY FROM (" + 
					"SELECT (SELECT ISNULL(SUM(qty),0) FROM ["+ht.get("plant")+"_INVMST] WHERE " + 
					"ITEM='"+ht.get("item")+"'  AND LOC NOT LIKE 'SHIPPINGAREA%' and LOC NOT LIKE 'TEMP_TO%') QTY," + 
					"ISNULL((SELECT SUM(QTYOR - ISNULL(QtyPick,0)) from ["+ht.get("plant")+"_DODET] D JOIN [" + plant + "_DOHDR] H ON D.DONO=H.DONO WHERE ITEM='"+ht.get("item")+"' "
					+ "and isnull(QTYOR,0) <>isnull(QTYIS,0) and H.ORDER_STATUS!='Draft' AND LNSTAT <> 'C'),0) ORDQTY,"+
					"ISNULL((SELECT SUM(QTYOR-QTYIS) from ["+ht.get("plant")+"_ESTDET]  D JOIN [" + plant + "_ESTHDR] H ON D.ESTNO=H.ESTNO "
							+ "WHERE ITEM='"+ht.get("item")+"' and H.ORDER_STATUS!='Draft'),0) ESTQTY ) A";
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
	
	public Map getPOAvailableQtyByProduct(Hashtable ht, String incommingQty) throws Exception {
		java.sql.Connection con = null;
		String query="";//, item = "", plant = "";
		Map map = new HashMap();
		try {	    
			con = com.track.gates.DbBean.getConnection();
//			item = (String) ht.get("item");
			plant = (String) ht.get("plant");			
			query = "SELECT (( "+incommingQty+"+QTY) - ORDQTY - ESTQTY) AVLBQTY FROM (" + 
					"SELECT (SELECT ISNULL(SUM(qty),0) FROM ["+ht.get("plant")+"_INVMST] WHERE " + 
					"ITEM='"+ht.get("item")+"'  AND LOC NOT LIKE 'SHIPPINGAREA%' and LOC NOT LIKE 'TEMP_TO%') QTY," + 
					"ISNULL((SELECT SUM(QTYOR - ISNULL(QtyPick,0)) from ["+ht.get("plant")+"_DODET] D JOIN [" + plant + "_DOHDR] H ON D.DONO=H.DONO WHERE ITEM='"+ht.get("item")+"' "
					+ "and isnull(QTYOR,0) <>isnull(QTYIS,0) and H.ORDER_STATUS!='Draft' AND LNSTAT <> 'C'),0) ORDQTY,"+
					"ISNULL((SELECT SUM(QTYOR-QTYIS) from ["+ht.get("plant")+"_ESTDET] D JOIN [" + plant + "_ESTHDR] H ON D.ESTNO=H.ESTNO "
					+ "WHERE ITEM='"+ht.get("item")+"' and H.ORDER_STATUS!='Draft'),0) ESTQTY ) A";
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
	
	/* Method to get the Inventory, Billed, Unbilled Qty based on item */
	
	public Map getInvDataByProduct(String item, String plant) throws Exception {
		java.sql.Connection con = null;
		String query="";
		Map map = new HashMap();
		try {
			con = com.track.gates.DbBean.getConnection();		
			query = "SELECT " +  
				" ISNULL((SELECT SUM(QTY) AS INV_QTY FROM " + 
				" ["+plant+"_INVMST] A JOIN ["+plant+"_ITEMMST] B ON A.ITEM=B.ITEM " +
				" WHERE A.item = '"+item+"'),'0') AS INV_QTY," +  
				" ISNULL((SELECT SUM(BILLQTY) FROM ( SELECT (CASE WHEN TRAN_TYPE='IB' THEN" + 
				" ISNULL((SELECT (SUM(E.QTY)) FROM ["+plant+"_FINBILLHDR] d  JOIN ["+plant+"_FINBILLDET] e ON d.ID = e.BILLHDRID AND c.ITEM=e.ITEM " + 
				" WHERE c.PONO = d.PONO AND c.GRNO = d.GRNO AND C.BILL_STATUS='BILLED'),0) ELSE " + 
				" (CASE WHEN ISNULL(TRAN_TYPE,'') IN ('GOODSRECEIPTWITHBATCH','MR','INVENTORYUPLOAD','') THEN SUM(C.RECQTY) ELSE 0 END)  END) AS BILLQTY " + 
				" FROM ["+plant+"_RECVDET] C WHERE ITEM = '"+item+"' " + 
				" GROUP BY PONO,GRNO,TRAN_TYPE,item,BILL_STATUS ) A),'0') AS BILL_QTY," + 
				" (SELECT ISNULL(SUM(ISNULL(B.QTY,0)),0) AS INVOICE_QTY FROM "
				+ "["+plant+"_FININVOICEHDR] A JOIN ["+plant+"_FININVOICEDET] B ON A.ID =B.INVOICEHDRID " + 
				" WHERE A.BILL_STATUS <> 'Draft' AND ISNULL(B.IS_COGS_SET,'')='Y' " + 
				" AND B.ITEM='"+item+"') AS INVOICE_QTY,"+
				" (SELECT ISNULL((SUM(C.RECQTY)*ISNULL((SELECT ISNULL(QPUOM,1) from ["+plant+"_UOM] where UOM=''),1)),0) AS UNBILL" + 
				" FROM ["+plant+"_RECVDET] c LEFT JOIN ["+plant+"_FINBILLHDR] d ON c.PONO = d.PONO AND c.GRNO = d.GRNO " + 
				" LEFT JOIN ["+plant+"_FINBILLDET] e ON d.ID = e.BILLHDRID AND c.ITEM=e.ITEM " + 
				" WHERE c.item = '"+item+"' AND c.TRAN_TYPE IN ('IB') " + 
				" AND ISNULL(C.BILL_STATUS,'')<>'BILLED') AS UNBILL_QTY";
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
	
	public boolean isExisitbatch(String plant, String loc, String batch) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM ["+ plant+"_INVMST] WHERE  LOC = '"+ loc+"' AND USERFLD4 = '"+ batch+"'");
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
	
	public int getqty(String plant, String item,
			String loc, String batch, String uom) throws Exception {

		java.sql.Connection con = null;
		int qty = 0;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			
//			boolean flag = false;
			String sQry = "select distinct userfld4 as batch,convert(int,(qty/(ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1)))) qty from " + "["
					+ plant + "_" + "invmst] where plant='" + plant
					+ "' and item='" + item + "' and loc ='" + loc
					+ "' and  userfld4='" + batch + "' and qty>0";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);
			
			if(al.size() > 0) {
				Map aldata=(Map)al.get(0);
				qty = Integer.valueOf((String) aldata.get("qty"));
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return qty;
	}
	public ArrayList getAvaliableQtyForInvLocationUOMPDAwithItem(String plant, String loc,
			 String uom,String item) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
             
			boolean flag = false;
			String sQry = "with tempdb as ( select isnull(item,'') item,isnull(userfld4,'') userfld4,isnull(loc,'') loc,isnull(qty,0) STKQTY," 
	        		 +" SUBSTRING(crat,1,2)+'/'+ ISNULL(SUBSTRING(crat,3,2)+'/'+ SUBSTRING(crat,5,4),'') as CRAT,ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1) as UOMQTY," 
	         		+"(SELECT ISNULL(NONSTKFLAG,'') from "+ plant + "_ITEMMST WHERE ITEM=A.ITEM) ISNONSTOCK,ID,ISNULL(EXPIREDATE,'') EXPIRYDATE "
			+ " from ["+ plant + "_" + "invmst] A where plant='" + plant
			+ "' and loc='" + loc
			+ "' and item LIKE '%"+item+"%" 
			+ "' and  userfld4 LIKE '%' and qty>0 )"
			+ "select item,userfld4,loc,convert(int,(STKQTY/UOMQTY)) qty,CRAT,ISNONSTOCK,id,EXPIRYDATE,case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end pcsqty from tempdb b ORDER BY item,loc ";
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
	public ArrayList getAvaliableQtyForInvProductUOMPDAwithLoc(String plant, String item,
			 String uom,String loc) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "with tempdb as ( select isnull(item,'') item,isnull(userfld4,'') userfld4,isnull(loc,'') loc,isnull(qty,0) STKQTY," 
	        		 +" SUBSTRING(crat,1,2)+'/'+ ISNULL(SUBSTRING(crat,3,2)+'/'+ SUBSTRING(crat,5,4),'') as CRAT,ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1) as UOMQTY," 
	         		+"(SELECT ISNULL(NONSTKFLAG,'') from "+ plant + "_ITEMMST WHERE ITEM=A.ITEM) ISNONSTOCK,ID,ISNULL(EXPIREDATE,'') EXPIRYDATE "
			+ " from ["+ plant + "_" + "invmst] A where plant='" + plant
			+ "' and item='" + item 
			+ "' and loc LIKE '%"+loc+"%" 
			+ "' and  userfld4 LIKE '%' and qty>0 )"
			+ "select item,userfld4,loc,convert(int,(STKQTY/UOMQTY)) qty,CRAT,ISNONSTOCK,id,EXPIRYDATE,case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end pcsqty from tempdb b ORDER BY item,loc ";
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
	public ArrayList getAvaliableQtyForInvPrd_LocUOMPDA(String plant, String loc,
			 String uom,String item) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
            
			boolean flag = false;
			String sQry = "with tempdb as ( select isnull(item,'') item,isnull(userfld4,'') userfld4,isnull(loc,'') loc,isnull(qty,0) STKQTY," 
	        		 +" SUBSTRING(crat,1,2)+'/'+ ISNULL(SUBSTRING(crat,3,2)+'/'+ SUBSTRING(crat,5,4),'') as CRAT,ISNULL((select u.QPUOM from "+ plant+"_UOM u where u.UOM='"+uom+"'),1) as UOMQTY," 
	         		+"(SELECT ISNULL(NONSTKFLAG,'') from "+ plant + "_ITEMMST WHERE ITEM=A.ITEM) ISNONSTOCK,ID,ISNULL(EXPIREDATE,'') EXPIRYDATE "
			+ " from ["+ plant + "_" + "invmst] A where plant='" + plant
			+ "' and loc LIKE '%"+loc+"%"
			+ "' and item LIKE '%"+item+"%" 
			+ "' and  userfld4 LIKE '%' and qty>0 )"
			+ "select item,userfld4,loc,convert(int,(STKQTY/UOMQTY)) qty,CRAT,ISNONSTOCK,id,EXPIRYDATE,case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end pcsqty from tempdb b ORDER BY item,loc ";
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
	//created by vicky Desc:Used for getting All To locations in PDA stock move
	public ArrayList getStockTransferLocForPDA(String plant, String fromloc,
			String user) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			UserLocUtil userLocUtil = new UserLocUtil();
			userLocUtil.setmLogger(mLogger);
			String condAssignedLocforUser = " ";
			condAssignedLocforUser = userLocUtil.getUserLocAssigned(user,
					plant, "LOC");

			String sQry = "select distinct loc as FromLoc,isnull(locdesc,'') as LocDesc from "
					+ "["
					+ plant
					+ "_"
					+ "locmst] where plant='"
					+ plant
					+ "' and  loc like '"
					+ fromloc
					+ "%' and loc not like 'SHIPPINGAREA%' and loc not like 'TEMP_TO%' and loc not like 'HOLDINGAREA%' and isActive='Y'"
					+ " ORDER BY loc ";
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
	//Created by Vicky Used for Sales Check PDA
	public ArrayList getDetailsForBatchPDACheck(String plant,String dono,String item,
			 String batch, String uom) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			String sQry ="with d as(SELECT (ROW_NUMBER() OVER ( ORDER BY batch)) AS ID,dono,cname,item,itemdesc,batch,status,sum(PICKQTY)as pickqty FROM ["+ plant+"_SHIPHIS] where ITEM LIKE '%"+item+"%' and dono='"+dono+"' and STATUS='C' group by  dono,cname,item,itemdesc,batch,status)Select*,isnull((select sum(a.QTYOR)from "+ plant+"_dodet a where a.DONO=d.DONO and a.ITEM=d.ITEM),0)ordqty from d ORDER BY BATCH";
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
	//Created by Vicky Used for Sales Check PDA Batch Validation
	public ArrayList getValidateForBatchPDASalesCheck(String plant,String dono,String item,
			 String batch, String uom) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			String sQry ="with d as(SELECT (ROW_NUMBER() OVER ( ORDER BY batch)) AS ID,dono,cname,item,itemdesc,batch,status,sum(PICKQTY)as pickqty FROM ["+ plant+"_SHIPHIS] where ITEM LIKE '%"+item+"%' and dono='"+dono+"' and Batch='"+batch+"' and STATUS='C' group by  dono,cname,item,itemdesc,batch,status)Select*,isnull((select sum(a.QTYOR)from "+ plant+"_dodet a where a.DONO=d.DONO and a.ITEM=d.ITEM),0)ordqty from d ORDER BY BATCH";
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
	
	public String getItemTotalQty(String aPlant, String aItem,String extn) throws Exception {
		String qty = "";
			Hashtable ht = new Hashtable();
			ht.put("plant", aPlant);
			ht.put("item", aItem);
			
			String query = " isnull(sum(QTY),0) qty ";

			Map m = selectRow(query, ht, extn);
			if (m.size() > 0) {
				qty = (String) m.get("qty");
					} else {
						qty = "";
					}
					if (qty.equalsIgnoreCase(null) || qty.length() == 0) {
						qty = "";
					}
					return qty;
		}
}
