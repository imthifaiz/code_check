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
import com.track.constants.MLoggerConstant;
import com.track.db.object.CostofgoodsLanded;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;
/*- ************Modification History*********************************
 Dec 5 2014 Bruhan, Description:New Method:getItemLoc
 Bruhan, change code:002  modifed query to get nonstock flag instead of getting this from separate method
 Bruhan, code:003 modified query to get item loc for item master
*/
public class ItemMstDAO extends BaseDAO {
	public static final String TABLE_NAME = "ItemMst";

	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.ITEMMSTDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.ITEMMSTDAO_PRINTPLANTMASTERLOG;

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

	public ItemMstDAO() {
		StrUtils _StrUtils = new StrUtils();
	}
	public ArrayList getNonStkList(String plant, String cond) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct NONSTKCODE,NONSTOCKDESC    from " + "[" + plant
			+ "_" + "NONSTOCKMST] where isnull(NONSTKCODE,'')<>'' " + cond;
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
	public String getNonStockFlag(String aPlant, String aItem) throws Exception {
		String itemNonStkFlg = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		//ht.put("item", aItem);

		String query = " isnull(NONSTKFLAG,'') as NONSTKFLAG ";

		Map m = selectRow(query, ht, "  ITEM IN( SELECT ITEM FROM ["+aPlant+"_ALTERNATE_ITEM_MAPPING] WHERE plant='"+aPlant+"' AND ALTERNATE_ITEM_NAME = '"+aItem+"')");
		if (m.size() > 0) {
			itemNonStkFlg = (String) m.get("NONSTKFLAG");
		} else {
			itemNonStkFlg = "";
		}
		if (itemNonStkFlg.equalsIgnoreCase(null) || itemNonStkFlg.length() == 0) {
			itemNonStkFlg = "";
		}
		return itemNonStkFlg;
	}
        

        public Map getProductNonStockDetails(String aPlant, String aItem) throws Exception {
              
                Hashtable ht = new Hashtable();
                ht.put("PLANT", aPlant);
                ht.put("item", aItem);

                String query = " ITEMDESC,STKUOM,ISNULL(NONSTKFLAG,'') as NONSTKFLAG,ISNULL((SELECT NONSTOCKDESC  from  "+aPlant+"_NONSTOCKMST  where NONSTKCODE="+aPlant+"_ITEMMST.NONSTKTYPEID ),'') as STOCKTYPEDESC ";

                Map m = selectRow(query, ht, "");
              
                return m;
        }


	public Map selectRow(String query, Hashtable ht, String extCondi)
			throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + (String) ht.get("PLANT") + "_" + TABLE_NAME + "]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			if (extCondi.length() > 0)
				sql.append(" and " + extCondi);
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

	public String getItemDesc(String aPlant, String aItem) throws Exception {
		String itemDesc = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("item", aItem);

		String query = " isnull(itemDesc,'') as itemDesc ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			itemDesc = (String) m.get("itemDesc");
		} else {
			itemDesc = "";
		}
		if (itemDesc.equalsIgnoreCase(null) || itemDesc.length() == 0) {
			itemDesc = "";
		}
		return itemDesc;
	}
	
	public String getcatlogpath(String aPlant, String aItem) throws Exception {
		String cpath = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("item", aItem);

		String query = " isnull(CATLOGPATH,'') as CATLOGPATH ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			cpath = (String) m.get("CATLOGPATH");
		} else {
			cpath = "";
		}
		if (cpath.equalsIgnoreCase(null) || cpath.length() == 0) {
			cpath = "";
		}
		return cpath;
	}
	
	
	public String getItemCost(String aPlant, String aItem) throws Exception {
		String cost = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("item", aItem);

		String query = " isnull(COST,'0') as cost ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			cost = (String) m.get("cost");
		} else {
			cost = "";
		}
		if (cost.equalsIgnoreCase(null) || cost.length() == 0) {
			cost = "";
		}
		return cost;
	}
	
	public String getItemPrice(String aPlant, String aItem) throws Exception {
		String price = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("item", aItem);

		String query = " isnull(UnitPrice,'0') as price ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			price = (String) m.get("price");
		} else {
			price = "";
		}
		if (price.equalsIgnoreCase(null) || price.length() == 0) {
			price = "";
		}
		return price;
	}
	
	public String getItemDept(String aPlant, String aItem) throws Exception {
		String dept = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("item", aItem);

		String query = " isnull(PRD_DEPT_ID,'') as dept ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			dept = (String) m.get("dept");
		} else {
			dept = "";
		}
		return dept;
	}

	public Map getItemDetailDescription(String aPlant, String aItem)
			throws Exception {
		String itemDesc = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("item", aItem);
		//change code:002
		//change code:003
		String query = " isnull(itemDesc,'') as itemDesc,isnull(Remark1,'') as DetailItemDesc,isnull(Remark2,'') as Supplier,StkUom as uom,isnull(NONSTKFLAG,'') as NONSTKFLAG,isnull(ITEM_LOC,'') as ITEM_LOC ";

		Map m = selectRow(query, ht, "");

		return m;
	}

	public ArrayList selectItemMst(String query, Hashtable ht) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from  "
				+ "[" + ht.get("PLANT") + "_" + TABLE_NAME + "]");
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

	public ArrayList getMpriceDetails(String selectList, Hashtable ht,
			String extCond) throws Exception {
		boolean flag = false;

		Connection con = null;
		ArrayList alResult = new ArrayList();
		StringBuffer sql = new StringBuffer(" SELECT " + selectList + " from "
				+ "[" + ht.get("PLANT") + "_" + TABLE_NAME + "]");

		try {
			con = DbBean.getConnection();

			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			if (extCond.length() > 0) {
				sql.append(" ");
				sql.append(extCond);
			}
			this.mLogger.query(this.printQuery, sql.toString());
			alResult = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

		return alResult;

	}
	
	public ArrayList selectItemMst(String query, Hashtable ht, String extCondi)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + TABLE_NAME + "]");
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

	public boolean insertItemMst(Hashtable ht) throws Exception {

		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _StrUtils.fString((String) enumeration
						.nextElement());
				String value = _StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + TABLE_NAME + "("
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
			StringBuffer sql = new StringBuffer(" UPDATE " + TABLE_NAME);
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

	public ArrayList getMiscItemByWMS(String plant, String itemno,
			String ItemDesc) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			if (ItemDesc.length() > 0) {
				ItemDesc = " AND itemmst.itemdesc like '%" + ItemDesc + "%'";
			}
			boolean flag = false;
			// query

			String sQry = "select distinct itemmst.item item,itemmst.itemdesc itemdesc,itemmst.stkuom as uom from ["
					+ plant
					+ "_"
					+ "itemmst"
					+ "] itemmst,["
					+ plant
					+ "_"
					+ "ALTERNATE_ITEM_MAPPING] altitem  where itemmst.plant='"
					+ plant
					+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
					+ itemno
					+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y' and isnull(userfld1,'N')='N'"
					+ ItemDesc + " ORDER BY itemmst.item ,itemmst.itemdesc ";
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

	public Map selectRow(String query, Hashtable ht) throws Exception {
		boolean flag = false;
		Map resultMap = null;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + TABLE_NAME + "]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			this.mLogger.query(this.printQuery, sql.toString());
			resultMap = getRowOfData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return resultMap;

	}

	public String getItemUOM(String company, String item) throws Exception {
		String itemuom = "";
		try {
			String query = "  STKUOM  ";
			Hashtable ht = new Hashtable();
			ht.put("PLANT", company);
			ht.put("ITEM", item);
			Map m = selectRow(query, ht);

			itemuom = (String) m.get("STKUOM");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return itemuom;
	}

	public boolean insertAlternateItem(String plant, String item,
			String alternateItemName) {

		boolean insertFlag = false;
		item = StrUtils.fString((String) item);
		alternateItemName = StrUtils.fString((String) alternateItemName);
		Connection conn = null;
		try {
			conn = DbBean.getConnection();

			String query = "INSERT INTO ["
					+ plant
					+ "_"
					+ "ALTERNATE_ITEM_MAPPING] ( PLANT, ITEM, ALTERNATE_ITEM_NAME ) "
					+ " VALUES('" + plant + "', '" + item + "', '"
					+ alternateItemName + "' )";

			this.mLogger.query(this.printQuery, query);
			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			return insertFlag;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}

	public List<String> getAllAssignedAlternateItems(String plant, String item) {
		List<String> resultSet = new ArrayList<String>();
		Connection conn = null;
		try {
			conn = DbBean.getConnection();

			String query = " SELECT ALTERNATE_ITEM_NAME FROM [" + plant + "_"
					+ "ALTERNATE_ITEM_MAPPING] WHERE PLANT='" + plant
					+ "' AND ITEM='" + item + "' ORDER BY CASE WHEN ALTERNATE_ITEM_NAME ='"+item+"' THEN 1 ELSE 2 END, ALTERNATE_ITEM_NAME";

			this.mLogger.query(this.printQuery, query);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				resultSet.add(rs.getString(1));
			}
			return resultSet;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			return resultSet;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
	}

	public boolean removeAlternateItems(String plant, String item, String cond) {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();

			String query = " DELETE FROM [" + plant + "_"
					+ "ALTERNATE_ITEM_MAPPING] WHERE PLANT='" + plant
					+ "' AND ITEM='" + item + "'   " + cond;

			this.mLogger.query(this.printQuery, query);
			insertFlag = DeleteRow(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			return insertFlag;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}

	public String getItemIdFromAlternate(String plant, String alernateItem) {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();
			
			String query = " SELECT ITEM FROM [" + plant + "_"
			+ "ALTERNATE_ITEM_MAPPING] WHERE PLANT='" + plant
			+ "' AND ALTERNATE_ITEM_NAME='" + alernateItem + "'  ORDER BY ITEM"; 

			
			this.mLogger.query(this.printQuery, query);
			Map m = getRowOfData(conn, query);

			return (String) m.get("ITEM");
		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			return null;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}

	}
	
	public boolean getischildcal(String plant,String item) throws Exception {
	    java.sql.Connection con = null;
	    String spy = "";
	    boolean isuserloc = false;
	    try {
	            
	    con = DbBean.getConnection();
	    StringBuffer SqlQuery = new StringBuffer(" select isnull(ISCHILDCAL,'0') ISCHILDCAL from [" + plant + "_"
	    		+ "ITEMMST] where PLANT='"+plant+"' AND ITEM='" + item + "'") ;
	     
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        spy = (String) m.get("ISCHILDCAL");
	        
	        if(spy.equalsIgnoreCase("1")) {
	        	isuserloc = true;
	        }

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return isuserloc;
	}
	
	public String getItemIdFromAlternatePDAMiscReceive(String plant, String alernateItem) {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();

			
			String query = " SELECT ITEM FROM [" + plant + "_"
			+ "ALTERNATE_ITEM_MAPPING] WHERE PLANT='" + plant
			+ "' AND ALTERNATE_ITEM_NAME='" + alernateItem + "' AND ITEM IN" +
					" (SELECT ITEM FROM [" + plant + "_"
			+ "ITEMMST] WHERE PLANT='" + plant
			+ "' AND USERFLD1='N') ORDER BY ITEM"; 

			System.out.println(query);
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
	
	
	
	public String getItemIdFromAlternateReceive(String plant, String alernateItem) {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();

			/* Modified By:Bruhan Date:20110621
			 * Change Made:Remove WHERE ISNULL(USERFLD1,'N')='N') from query 
			 * Reason: In stock move parent item can't move because
			 *         the condition WHERE ISNULL(USERFLD1,'N')='N'),for parent item
			 *         USERFLD1='K',So we  remove the condition.
				
			 Query Before Change:         
			        String query = " SELECT ITEM FROM [" + plant + "_"
					+ "ALTERNATE_ITEM_MAPPING] WHERE PLANT='" + plant
					+ "' AND ALTERNATE_ITEM_NAME='" + alernateItem
					+ "' AND ITEM IN(SELECT ITEM FROM  [" + plant + "_"
					+ "ITEMMST WHERE ISNULL(USERFLD1,'N')='N')]  ORDER BY ITEM ";
			
			*/
			String query = " SELECT ITEM FROM [" + plant + "_"
			+ "ALTERNATE_ITEM_MAPPING] WHERE PLANT='" + plant
			+ "' AND ALTERNATE_ITEM_NAME='" + alernateItem + "'  ORDER BY ITEM"; 

			System.out.println(query);
			this.mLogger.query(this.printQuery, query);
			Map m = getRowOfData(conn, query);

			return (String) m.get("ITEM");
		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			return null;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}

	}


	public String getInvItemIdFromAlternate(String plant, String alernateItem) {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();

			String query = " SELECT ITEM FROM [" + plant + "_"
					+ "ALTERNATE_ITEM_MAPPING] WHERE PLANT='" + plant
					+ "' AND ALTERNATE_ITEM_NAME='" + alernateItem
					+ "' AND ITEM IN(SELECT ITEM FROM  [" + plant + "_"
					+ "ITEMMST] )";

			System.out.println(query);
			this.mLogger.query(this.printQuery, query);
			Map m = getRowOfData(conn, query);

			return (String) m.get("ITEM");
		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			return null;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}

	}

	public String getKittingItem(String plant, String item) {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();

			String query = " SELECT ITEM FROM [" + plant + "_"
					+ "ITEMMST] WHERE PLANT='" + plant + "' AND ITEM='" + item
					//+ "' AND ISNULL(USERFLD1,'N')='K'";
					+ "' AND ISNULL(USERFLD1,'N')='N'";

			this.mLogger.query(this.printQuery, query);
			Map m = getRowOfData(conn, query);

			return (String) m.get("ITEM");
		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			return null;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}

	}

	public ArrayList getUomList(String plant, String cond) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct uom as stkuom  from " + "[" + plant
					+ "_" + "UOM] where isnull(uom,'')<>'' " + cond;
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

	public ArrayList getKittingProducts(String plant, String user)
			throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			String sQry = "select distinct item,itemdesc from "
					+ "["
					+ plant
					+ "_"
					+ "itemmst] where plant='"
					+ plant
					+ "' AND ISACTIVE='Y' Order by Item";
					//+ "' AND ISACTIVE='Y' AND  ISNULL(USERFLD1,'N') ='K' Order by Item";

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

	public String getKittingParentItem(String aPlant, String aItem)
			throws Exception {
		String itemdesc = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("item", aItem);

		String query = " isnull(itemdesc,'') as ItemDesc ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			itemdesc = (String) m.get("ItemDesc");
		} else {
			itemdesc = "";
		}
		if (itemdesc.equalsIgnoreCase(null) || itemdesc.length() == 0) {
			itemdesc = "";
		}
		return itemdesc;
	}

	@SuppressWarnings("unchecked")
	public ArrayList getItemDetails(String item, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrCust = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT ITEM, ITEMDESC, REMARK1, STKUOM FROM "
					+ "["
					+ plant
					+ "_"
					+ "ITEMMST"
					+ "]"
					+ " WHERE ITEM = '" + item + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrCust.add(0, StrUtils.fString((String) rs.getString(1))); // ITEM
				arrCust.add(1, StrUtils.fString((String) rs.getString(2))); // ITEMDESC
				arrCust.add(2, StrUtils.fString((String) rs.getString(3))); // REMARK1
				arrCust.add(3, StrUtils.fString((String) rs.getString(4))); // STKUOM
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrCust;
	}
public String getItemFromDesc(String plant, String desc) throws Exception {
		String item = "";
		try {
			String query = "  ITEM  ";
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("ITEMDESC", desc);
			Map m = selectRow(query, ht);

			item = (String) m.get("ITEM");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return item;
	}
	public String getItemLoc(String company, String item) throws Exception {
	String itemloc = "";
	try {
		String query = "  ISNULL(ITEM_LOC,'') ITEM_LOC  ";
		Hashtable ht = new Hashtable();
		ht.put("PLANT", company);
		ht.put("ITEM", item);
		Map m = selectRow(query, ht);

		itemloc = (String) m.get("ITEM_LOC");

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	}
	return itemloc;
}
	public String getItemNetWeight(String company, String item) throws Exception {
		String NetWeight = "";
		try {
			String query = "  NETWEIGHT  ";
			Hashtable ht = new Hashtable();
			ht.put("PLANT", company);
			ht.put("ITEM", item);
			Map m = selectRow(query, ht);
			NetWeight = (String) m.get("NETWEIGHT");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return NetWeight;
	}
	public ArrayList getCustomerDiscountOB(String plant, String itemno
			) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			String sQry = "select ISNULL(obdiscount,0) obdiscount,isnull(customer_type_id,'') customertype,isnull(customer_type_desc,'') cname  from" + "["
					+ plant + "_" + "MULTI_PRICE_MAPPING" + "]"
					+ " where plant='" + plant + "'   and item ='" + itemno + "'" ;
					
					//+ " ORDER BY invmst.item ";
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
	public boolean removeCustomerDiscountOB(String plant, String item, String cond) {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();

			String query = " DELETE FROM [" + plant + "_"
					+ "MULTI_PRICE_MAPPING] WHERE PLANT='" + plant
					+ "' AND ITEM='" + item + "'   " + cond;

			this.mLogger.query(this.printQuery, query);
			insertFlag = DeleteRow(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			return insertFlag;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}
	
	public boolean isExisitCustomerDiscount(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_"  + "MULTI_PRICE_MAPPING" + "]");
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
	
	public boolean removeSupplierDiscountIB(String plant, String item, String cond) {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();

			String query = " DELETE FROM [" + plant + "_"
					+ "MULTI_COST_MAPPING] WHERE PLANT='" + plant
					+ "' AND ITEM='" + item + "'   " + cond;

			this.mLogger.query(this.printQuery, query);
			insertFlag = DeleteRow(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			return insertFlag;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}
	
	public ArrayList getSupplierDiscountIB(String plant, String itemno
			) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			String sQry = "select ISNULL(ibdiscount,0) ibdiscount,isnull(vendno,'') vendno,isnull(vname,'') vname  from" + "["
					+ plant + "_" + "MULTI_COST_MAPPING" + "]"
					+ " where plant='" + plant + "'   and item ='" + itemno + "'" ;
					
					//+ " ORDER BY invmst.item ";
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
	
	public ArrayList getAdditionalPrd(String plant, String itemno
			) throws Exception {
		
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			String sQry = "select ISNULL(ADDITIONALITEM,'') ADDITIONALITEM from" + "["
					+ plant + "_" + "ADDITIONALITEM" + "]"
					+ " where plant='" + plant + "'   and item ='" + itemno + "'" ;
			
			//+ " ORDER BY invmst.item ";
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
	
	public ArrayList getAdditionalDesc(String plant, String itemno
			) throws Exception {
		
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			String sQry = "select ISNULL(ITEMDETAILDESC,'') ITEMDETAILDESC from" + "["
					+ plant + "_" + "ITEMDET_DESC" + "]"
					+ " where plant='" + plant + "'   and item ='" + itemno + "'" ;
			
			//+ " ORDER BY invmst.item ";
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
	
	public boolean removeAdditionalPrd(String plant, String item, String cond) {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();
			
			String query = " DELETE FROM [" + plant + "_"
					+ "ADDITIONALITEM] WHERE PLANT='" + plant
					+ "' AND ITEM='" + item + "'   " + cond;
			
			this.mLogger.query(this.printQuery, query);
			insertFlag = DeleteRow(conn, query);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			return insertFlag;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}
	
	public boolean removeAdditionalDesc(String plant, String item, String cond) {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();

			String query = " DELETE FROM [" + plant + "_"
					+ "ITEMDET_DESC] WHERE PLANT='" + plant
					+ "' AND ITEM='" + item + "'   " + cond;

			this.mLogger.query(this.printQuery, query);
			insertFlag = DeleteRow(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			return insertFlag;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}
	
	public boolean isExisitSupplierDiscount(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_"  + "MULTI_COST_MAPPING" + "]");
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
	
	public String getItemMinPrice(String aPlant, String aItem) throws Exception {
		String minprice = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("item", aItem);

		String query = " isnull(MINSPRICE,'') as minprice ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			minprice = (String) m.get("minprice");
		} else {
			minprice = "";
		}
		if (minprice.equalsIgnoreCase(null) || minprice.length() == 0) {
			minprice = "";
		}
		return minprice;
	}
	public String getItemDetailDesc(String aPlant, String aItem) throws Exception {
		String itemDetailDesc = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("item", aItem);

		String query = " isnull(Remark1,'') as itemDetailDesc ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			itemDetailDesc = (String) m.get("itemDetailDesc");
		} else {
			itemDetailDesc = "";
		}
		if (itemDetailDesc.equalsIgnoreCase(null) || itemDetailDesc.length() == 0) {
			itemDetailDesc = "";
		}
		return itemDetailDesc;
	}
public ArrayList getLabelprintDetails(String plant,String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select ID,ITEM,isnull(ITEMDESC,'')ITEMDESC,ISNULL(BATCH,'')BATCH,ISNULL(LOC,'')LOC,ISNULL(REMARKS,'')REMARKS,ISNULL(EXPIRYDATE,'')EXPIRYDATE from"
					+ "["
					+ plant
					+ "_"
					+ "LABEL_TEMP] where PLANT<>'' AND PLANT='"
					+ plant
					+"'   " + extraCon
					+ " order by CRAT ";
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
public boolean insertLabelDetails(Hashtable ht) throws Exception {

	boolean insertFlag = false;
	java.sql.Connection conn = null;
	try {
		conn = DbBean.getConnection();
		String FIELDS = "", VALUES = "";
		Enumeration enumeration = ht.keys();
		for (int i = 0; i < ht.size(); i++) {
			String key = _StrUtils.fString((String) enumeration
					.nextElement());
			String value = _StrUtils.fString((String) ht.get(key));
			FIELDS += key + ",";
			VALUES += "'" + value + "',";
		}
		String query = "INSERT INTO " +  "[" + ht.get("PLANT") + "_"
				+ "LABEL_TEMP" + "]" + "("
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
public boolean removeLabelDetailsTemp(String plant,String cond) {
	boolean deleteFlag = false;
	Connection conn = null;
	try {
		conn = DbBean.getConnection();

		String query = " DELETE FROM [" + plant + "_"
				+ "LABEL_TEMP] WHERE PLANT='" + plant
				+ "'" + cond;

		this.mLogger.query(this.printQuery, query);
		deleteFlag = DeleteRow(conn, query);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		return deleteFlag;
	} finally {
		if (conn != null) {
			DbBean.closeConnection(conn);
		}
	}
	return deleteFlag;
}
public ArrayList getSupplierDiscountDetails(String plant,String item,String desc,String brand,String pclass,String type,String extraCon) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		
		if (item.length() > 0) {
			extraCon = extraCon + " AND ITEM  = '" + item	+ "'  ";
		}
		
		if (desc.length() > 0) {
			extraCon = extraCon + " AND ITEM IN (select ITEM from  ["+plant+"_ITEMMST] where ITEMDESC = '" + desc + "' and ITEM=SD.ITEM)";
		}
		
		if (brand.length() > 0) {
			extraCon = extraCon + " AND ITEM IN (select ITEM from  ["+plant+"_ITEMMST] where PRD_BRAND_ID = '" +brand + "' and ITEM=SD.ITEM)";
		}
		
		if (pclass.length() > 0) {
			extraCon = extraCon + " AND ITEM IN (select ITEM from  ["+plant+"_ITEMMST] where PRD_CLS_ID = '" +pclass + "' and ITEM=SD.ITEM)";
		}

		if (type.length() > 0) {
			extraCon = extraCon + " AND ITEM IN (select ITEM from  ["+plant+"_ITEMMST] where ITEMTYPE = '" +type + "' and ITEM=SD.ITEM)";
		}
	
		boolean flag = false;
		String sQry = "select ITEM,VENDNO,IBDISCOUNT FROM"
				+ "["
				+ plant
				+ "_"
				+ "MULTI_COST_MAPPING]SD where PLANT <>'' "
				+" " + extraCon
				+ " order by ITEM ";
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
public ArrayList getCustomerDiscountDetails(String plant,String item,String desc,String brand,String pclass,String type,String extraCon) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		
		if (item.length() > 0) {
			extraCon = extraCon + " AND ITEM  = '" + item	+ "'  ";
		}
		
		if (desc.length() > 0) {
			extraCon = extraCon + " AND ITEM IN (select ITEM from  ["+plant+"_ITEMMST] where ITEMDESC = '" + desc + "' and ITEM=CD.ITEM)";
		}
		
		if (brand.length() > 0) {
			extraCon = extraCon + " AND ITEM IN (select ITEM from  ["+plant+"_ITEMMST] where PRD_BRAND_ID = '" +brand + "' and ITEM=CD.ITEM)";
		}
		
		if (pclass.length() > 0) {
			extraCon = extraCon + " AND ITEM IN (select ITEM from  ["+plant+"_ITEMMST] where PRD_CLS_ID = '" +pclass + "' and ITEM=CD.ITEM)";
		}

		if (type.length() > 0) {
			extraCon = extraCon + " AND ITEM IN (select ITEM from  ["+plant+"_ITEMMST] where ITEMTYPE = '" +type + "' and ITEM=CD.ITEM)";
		}
	
		boolean flag = false;
		String sQry = "select ITEM,CUSTOMER_TYPE_ID,OBDISCOUNT FROM"
				+ "["
				+ plant
				+ "_"
				+ "MULTI_PRICE_MAPPING]CD where PLANT <>'' "
				+" " + extraCon
				+ " order by ITEM ";
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

public String getProductGst(String plant,String item) throws Exception {
    java.sql.Connection con = null;
    String prodgst = "";
    try {
            
    con = DbBean.getConnection();
    StringBuffer SqlQuery = new StringBuffer(" select isnull(PRODGST,0) prodgst from ["+plant+"_ITEMMST] where item='"+item+"'") ;
     
    System.out.println(SqlQuery.toString());
        Map m = this.getRowOfData(con, SqlQuery.toString());

       prodgst = (String) m.get("prodgst");

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            throw e;
    } finally {
            if (con != null) {
                    DbBean.closeConnection(con);
            }
    }
    return prodgst;
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
	
	
	public boolean isExistsAlternateBrandItem(Hashtable ht) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" 1 ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "ALTERNATE_BRAND_ITEM_MAPPING"
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
	
	public boolean insertAlternateBrandItem(Hashtable ht) throws Exception {
		boolean insertedInv = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "ALTERNATE_BRAND_ITEM_MAPPING" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			insertedInv = insertData(con, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);

			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return insertedInv;
	}
	
	public boolean deleteAlternateBrandItem(Hashtable ht) throws Exception {
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		String tablename="ALTERNATE_BRAND_ITEM_MAPPING";
		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String sql = "DELETE " + "[" + ht.get("PLANT") + "_" + tablename
					+ "]" + " WHERE " + sCondition;
			ps = con.prepareStatement(sql);
			this.mLogger.query(this.printQuery, sql);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				deleteItemMst = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return deleteItemMst;
	}
	public ArrayList getAltItems(String plant, String itemno,
			String ItemDesc) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			// query

			String sQry = "select DISTINCT AIM.ITEM,IM.ITEMDESC,IM.PRD_BRAND_ID,IM.VINNO,IM.MODEL FROM ["+plant+"_ALTERNATE_BRAND_ITEM_MAPPING] AIM JOIN ["+plant+"_ITEMMST] IM ON IM.ITEM=AIM.ITEM";
			if(!itemno.isEmpty())
				sQry=sQry+" Where AIM.ITEM='"+itemno+"'";
			if(!ItemDesc.isEmpty() && itemno.isEmpty())
				sQry=sQry+" Where IM.ITEMDESC='"+ItemDesc+"'";
			if(!ItemDesc.isEmpty() && !itemno.isEmpty())
				sQry=sQry+" AND IM.ITEMDESC='"+ItemDesc+"'";
			
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
	public String getLocalCurrency(String aPlant, String CURRENCY,String item,String uom) throws Exception {
	    java.sql.Connection con = null;
	    String UnitCostForSelCurrency = "";
	    try {
	    	
	    	con = DbBean.getConnection();
	        StringBuffer SqlQuery = new StringBuffer("select  cast (((case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ aPlant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ aPlant+"_UOM where UOM=SALESUOM),1))) end) * (SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID= '"+CURRENCY+"')) ");
	        SqlQuery.append(" aS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) AS UNITCOST from "+aPlant+"_itemmst where item='"+item+"'");


	   // 
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        UnitCostForSelCurrency = (String) m.get("UNITCOST");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return UnitCostForSelCurrency;
	}
	public String getLocalCurrencyCost(String aPlant, String CURRENCY,String item,String uom) throws Exception {
	    java.sql.Connection con = null;
	    String UnitCostForSelCurrency = "";
	    try {
	            
	    	con = DbBean.getConnection();
	        StringBuffer SqlQuery = new StringBuffer("select  cast (((case when PURCHASEUOM='"+uom+"' then COST else ISNULL((select ISNULL(QPUOM,1) from "+ aPlant+"_UOM where UOM='"+uom+"'),1) * (COST / (ISNULL((select ISNULL(QPUOM,1) from "+ aPlant+"_UOM where UOM=PURCHASEUOM),1))) end) * (SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID= '"+CURRENCY+"')) ");
	        SqlQuery.append(" aS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) AS UNITCOST from "+aPlant+"_itemmst where item='"+item+"'");


	   // 
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        UnitCostForSelCurrency = (String) m.get("UNITCOST");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return UnitCostForSelCurrency;
	}
	public String getLocalCurrencyWRD(String aPlant, String CURRENCY,String item,String uom) throws Exception {
	    java.sql.Connection con = null;
	    String UnitCostForSelCurrency = "";
	    try {
	    	
	    	con = DbBean.getConnection();
	        StringBuffer SqlQuery = new StringBuffer("select  ((case when PURCHASEUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ aPlant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ aPlant+"_UOM where UOM=PURCHASEUOM),1))) end) * (SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID= '"+CURRENCY+"')) ");
	        SqlQuery.append("  AS UNITCOST from "+aPlant+"_itemmst where item='"+item+"'");


	   // 
	    System.out.println(SqlQuery.toString());
	        Map m = this.getRowOfData(con, SqlQuery.toString());

	        UnitCostForSelCurrency = (String) m.get("UNITCOST");

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return UnitCostForSelCurrency;
	}
	public String getItemPurchaseUOM(String company, String item) throws Exception {
		String itemuom = "";
		try {
			String query = "  PURCHASEUOM  ";
			Hashtable ht = new Hashtable();
			ht.put("PLANT", company);
			ht.put("ITEM", item);
			Map m = selectRow(query, ht);

			itemuom = (String) m.get("PURCHASEUOM");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return itemuom;
	}
	public String getItemSalesUOM(String company, String item) throws Exception {
		String itemuom = "";
		try {
			String query = "  SALESUOM  ";
			Hashtable ht = new Hashtable();
			ht.put("PLANT", company);
			ht.put("ITEM", item);
			Map m = selectRow(query, ht);

			itemuom = (String) m.get("SALESUOM");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return itemuom;
	}
	public String getInvUOM(String company, String item) throws Exception {
		String itemuom = "";
		try {
			String query = "  INVENTORYUOM  ";
			Hashtable ht = new Hashtable();
			ht.put("PLANT", company);
			ht.put("ITEM", item);
			Map m = selectRow(query, ht);

			itemuom = (String) m.get("INVENTORYUOM");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return itemuom;
	}
	public String getItemMutiUOM(String company, String item,String UOM) throws Exception {
		String itemuom = "";
		try {
			
			String query = UOM;
			Hashtable ht = new Hashtable();
			ht.put("PLANT", company);
			ht.put("ITEM", item);
			Map m = selectRow(query, ht);

			itemuom = (String) m.get(UOM);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return itemuom;
	}
	public CostofgoodsLanded getItemMSTDetails(List item, String plant) {
		CostofgoodsLanded dbObj=null;
		double netweight=0.0;
		StringBuffer itemId=new StringBuffer();
		for(int i=0;i<item.size();i++) {
			itemId.append("'").append(item.get(i)).append("'").append(",");;
		}
		itemId.deleteCharAt(itemId.length() - 1);
		String query = "SELECT ITEM,NETWEIGHT FROM [" + plant + "_ITEMMST] WHERE ITEM IN (" + itemId.toString() + ")";
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			while (rset.next()) {
				netweight+=Double.parseDouble(rset.getString("NETWEIGHT"));
			}
			dbObj=new CostofgoodsLanded();
			dbObj.setWeight(netweight);
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return dbObj;
	}
	public CostofgoodsLanded getItemMSTDetails(String item, String plant) {
		CostofgoodsLanded dbObj=null;
		StringBuffer itemId=new StringBuffer();
		String query = "SELECT ITEM,ISNULL(NETWEIGHT,'0') AS NETWEIGHT FROM [" + plant + "_ITEMMST] WHERE ITEM = '" + item + "' ";
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			while (rset.next()) {
				dbObj=new CostofgoodsLanded();
				dbObj.setItem(rset.getString("ITEM"));
				dbObj.setNet_weight(Double.parseDouble(rset.getString("NETWEIGHT")));
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return dbObj;
	}
	public Double getINVMSTDetails(String item, String plant) {
		Double qty=0.0;
		String query = "SELECT SUM(QTY) AS QTY FROM [" + plant + "_INVMST] WHERE ITEM = '" + item + "' ";
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			while (rset.next()) {
			  qty=(Double.parseDouble(rset.getString("QTY")));
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return qty;
	}
	
	public int Itemcount(String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int Itemcount = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ TABLE_NAME + "]" + " WHERE " + IConstants.PLANT
					+ " = '" + plant.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Itemcount = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return Itemcount;
	}
	
	public String getItemIsComp(String aPlant, String aItem) throws Exception {
		String itemDesc = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("item", aItem);

		String query = " isnull(ISCOMPRO,'0') as ISCOMPRO ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			itemDesc = (String) m.get("ISCOMPRO");
		} else {
			itemDesc = "";
		}
		if (itemDesc.equalsIgnoreCase(null) || itemDesc.length() == 0) {
			itemDesc = "";
		}
		return itemDesc;
	}
	
	
	public ArrayList getallitem(String plant,String item,String dept,String brand,String cat,String sybcat,String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			
			if (item.length() > 0) {
				extraCon = extraCon + " AND ITEM  = '" + item	+ "'  ";
			}
			
			if (dept.length() > 0) {
				extraCon = extraCon + " AND PRD_DEPT_ID  = '" + item	+ "'  ";
			}
			
			if (brand.length() > 0) {
				extraCon = extraCon + " AND PRD_BRAND_ID  = '" + brand	+ "'  ";
			}
			
			if (cat.length() > 0) {
				extraCon = extraCon + " AND PRD_CLS_ID  = '" + cat	+ "'  ";
			}

			if (sybcat.length() > 0) {
				extraCon = extraCon + " AND ITEMTYPE  = '" + sybcat	+ "'  ";
			}
		
			boolean flag = false;
			String sQry = "select * FROM"
					+ "["
					+ plant
					+ "_"
					+ "ItemMst]SD where PLANT <>'' "
					+" " + extraCon
					+ " order by ITEM ";
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
	
	
	public ArrayList getItemSupplier(String plant, String item) throws Exception {
		
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			String sQry = "select ISNULL(ITEM,'')ITEM,ISNULL(VENDNO,'') VENDNO  from" + "["
					+ plant + "_" + "ITEM_SUPPLIER" + "]"
					+ " where plant='" + plant + "'   and item ='" + item + "'" ;
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
	
	
	public boolean removeItemsupplier(String plant, String item) {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();

			String query = " DELETE FROM [" + plant + "_"
					+ "ITEM_SUPPLIER] WHERE PLANT='" + plant
					+ "' AND ITEM='" + item + "'   ";
			this.mLogger.query(this.printQuery, query);
			insertFlag = DeleteRow(conn, query);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			return insertFlag;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}
	
	public int addProductReturnHdr(Hashtable ht, String plant)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		int count = 0;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
	    String query = "";
		try {
			 /*Instantiate the list*/
		    args = new ArrayList<String>();		    
			connection = DbBean.getConnection();			
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration.nextElement());
				System.out.println(key);
				String value = StrUtils.fString((String) ht.get(key));
				
				System.out.println("---------  "+value);
				args.add(value);
				FIELDS += key + ",";
				VALUES += "'"+value+"',";
				//VALUES += value+",";
			}
			query = "INSERT INTO ["+ plant +"_PRODUCTRETURNHDR] ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			//invoiceHdrId = execute_NonSelectQueryGetLastInsert(ps, args);
			count = ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			   if (rs.next()){
				   invoiceHdrId = rs.getInt(1);
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
		return invoiceHdrId;
	}
	
	public boolean addProductReturnDet(List<Hashtable<String, String>> invoiceDetInfoList, String plant) 
			throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {		
			/*Instantiate the list*/
			args = new ArrayList<String>();
			
			connection = DbBean.getConnection();
			
			for (Hashtable<String, String> invoiceDetInfo : invoiceDetInfoList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = invoiceDetInfo.keys();
				for (int i = 0; i < invoiceDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) invoiceDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ plant +"_PRODUCTRETURNDET]  ("
				+ FIELDS.substring(0, FIELDS.length() - 1)
				+ ") VALUES ("
				+ VALUES.substring(0, VALUES.length() - 1) + ")";
				
				if(connection != null){
					/*Create  PreparedStatement object*/
					ps = connection.prepareStatement(query);
					this.mLogger.query(this.printQuery, query);
					flag = execute_NonSelectQuery(ps, args);
					if(flag){
						args.clear();
					}
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
		return flag;		
	}
	
	 public boolean isExisit(Hashtable ht) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" ");
				sql.append(" FROM " + "["+ht.get("PLANT")+"_PRODUCTRETURNHDR]");
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
	 
	 public List getConvProductHdrById(Hashtable ht) throws Exception {
			java.sql.Connection con = null;
			List<Map<String, String>> invoiceHdrList = new ArrayList<>();
			Map<String, String> map = null;
			String query="";
			List<String> args = null;
			try {			
				/*Instantiate the list*/
			    args = new ArrayList<String>();
			    
				con = com.track.gates.DbBean.getConnection();
				query="SELECT ID, VENDNO,VNAME,IDRNO,RETURN_DATE,DUE_DATE,PAYMENT_TERMS,EMPNO,ITEM_RATES,ISNULL(ORDER_DISCOUNT,0) as ORDER_DISCOUNT,"
						+ "(case when DISCOUNT_TYPE='%' then ISNULL(DISCOUNT, 0) else (ISNULL(DISCOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_PRODUCTRETURNDET] WHERE PRODUCTHDRID = A.ID ),1)) end) DISCOUNT,"
						+ "DISCOUNT_TYPE,DISCOUNT_ACCOUNT,"
						+ "(ISNULL(SHIPPINGCOST, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_PRODUCTRETURNDET] WHERE PRODUCTHDRID = A.ID ),1)) SHIPPINGCOST, "
						+ "(ISNULL(ADJUSTMENT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_PRODUCTRETURNDET] WHERE PRODUCTHDRID = A.ID ),1))  ADJUSTMENT,"
						+ "(ISNULL(SUB_TOTAL, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_PRODUCTRETURNDET] WHERE PRODUCTHDRID = A.ID ),1)) SUB_TOTAL, "
						+ "ISNULL(TAX_STATUS,'') TAX_STATUS,ISNULL(A.EMPNO,0) EMPNO,ISNULL((SELECT FNAME FROM " +ht.get("PLANT")  +"_EMP_MST E where E.EMPNO=A.EMPNO),'') as EMP_NAME,ISNULL(A.ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,ISNULL(A.TAXID,'0') TAXID,ISNULL(A.ISDISCOUNTTAX,'0') ISDISCOUNTTAX,ISNULL(A.ISORDERDISCOUNTTAX,'0') ISORDERDISCOUNTTAX,ISNULL(A.ISSHIPPINGTAX,'0') ISSHIPPINGTAX,ISNULL(A.TRANSPORTID,0) TRANSPORTID,"
						+ "(ISNULL(TOTAL_AMOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_PRODUCTRETURNDET] WHERE PRODUCTHDRID = A.ID ),1)) TOTAL_AMOUNT,"
						+ "RETURN_STATUS,NOTE,TERMSCONDITIONS,CRAT,CRBY,UPAT,UPBY,"
						+ "(ISNULL(TAXAMOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_PRODUCTRETURNDET] WHERE PRODUCTHDRID = A.ID ),1)) as TAXAMOUNT,"
						+ " ISNULL(CURRENCYID, '') CURRENCYID,ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_PRODUCTRETURNDET] WHERE PRODUCTHDRID = A.ID ),1) CURRENCYUSEQT, "
						+ " ISNULL((SELECT ISNULL(DISPLAY,'') DISPLAY FROM ["+ht.get("PLANT")+"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID), '') DISPLAY,"
						+ " ISNULL(SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(SHIPHPNO,'') SHIPHPNO,ISNULL(SHIPEMAIL,'') SHIPEMAIL,ISNULL(SHIPCOUNTRY,'') SHIPCOUNTRY,ISNULL(SHIPADDR1,'') SHIPADDR1,ISNULL(SHIPADDR2,'') SHIPADDR2,ISNULL(SHIPADDR3,'') SHIPADDR3,ISNULL(SHIPADDR4,'') SHIPADDR4,ISNULL(SHIPSTATE,'') SHIPSTATE,ISNULL(SHIPZIP,'') SHIPZIP,"
						+ "ISNULL(SHIPPINGID,'') as SHIPPINGID,ISNULL(SHIPPINGCUSTOMER,'') as SHIPPINGCUSTOMER,ISNULL(ORDERTYPE,'') ORDERTYPE FROM "
				+ "[" + ht.get("PLANT") + "_PRODUCTRETURNHDR] A WHERE ID = ? AND PLANT =? ";
				PreparedStatement ps = con.prepareStatement(query);  
				
				/*Storing all the query param argument in list squentially*/
				args.add((String) ht.get("ID"));
				args.add((String) ht.get("PLANT"));
				
				this.mLogger.query(this.printQuery, query);
				
				invoiceHdrList = selectData(ps, args);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return invoiceHdrList;
		}
	 
	 public List getConvProductDetByHdrId(Hashtable ht) throws Exception {
			java.sql.Connection con = null;
			List<Map<String, String>> InvoiceDetList = new ArrayList<>();
			Map<String, String> map = null;
			String query="";
			List<String> args = null;
			try {			
				/*Instantiate the list*/
			    args = new ArrayList<String>();
			    
				con = com.track.gates.DbBean.getConnection();
				query="SELECT ID, LNNO,PRODUCTHDRID,ITEM,ACCOUNT_NAME,QTY,"
						+ "(UNITPRICE*ISNULL(CURRENCYUSEQT,1)) UNITPRICE,ISNULL(CURRENCYUSEQT,1) CURRENCYUSEQT,"
						+ "(case when DISCOUNT_TYPE='%' then DISCOUNT else (DISCOUNT*ISNULL(CURRENCYUSEQT,1)) end) DISCOUNT,"
						+ "ISNULL(DISCOUNT_TYPE,0) DISCOUNT_TYPE,TAX_TYPE,ISNULL(GSTPERCENTAGE,0) GSTPERCENTAGE,"
						+ "(AMOUNT*ISNULL(CURRENCYUSEQT,1)) AMOUNT,"
						+ "ISNULL(UOM,'') UOM,ISNULL(LOC,'') LOC,ISNULL(BATCH, '') BATCH,"
						+ "ISNULL((SELECT ITEMDESC FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS ITEMDESC,"
						+ "ISNULL((SELECT HSCODE FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS HSCODE,"
						+ "ISNULL((SELECT COO FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS COO,"
						+ "ISNULL((SELECT CASE WHEN PRD_BRAND_ID = 'NOBRAND' THEN '' ELSE PRD_BRAND_ID END AS BRAND FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS BRAND,"
						+ "ISNULL((SELECT REMARK1 FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS DETAILDESC,"
						+ "CRAT,CRBY,UPAT,UPBY  FROM "
				+ "[" + ht.get("PLANT") + "_PRODUCTRETURNDET] A LEFT JOIN "
				+ "[" + ht.get("PLANT") + "_COMPANY_CONFIG] B ON A.TAX_TYPE = B.GSTTYPE"
						+ " WHERE PRODUCTHDRID = ? AND A.PLANT =? ORDER BY A.LNNO";
				PreparedStatement ps = con.prepareStatement(query);  
				
				/*Storing all the query param argument in list squentially*/
				args.add((String) ht.get("PRODUCTHDRID"));
				args.add((String) ht.get("PLANT"));
				
				this.mLogger.query(this.printQuery, query);
				
				InvoiceDetList = selectData(ps, args);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return InvoiceDetList;
		}
	 
	 public ArrayList selectProductReceiveHdr(String query, Hashtable ht, String extCond)
			 throws Exception {
//			boolean flag = false;
		 ArrayList alData = new ArrayList();
		 java.sql.Connection con = null;
		 
		 StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				 + ht.get("PLANT") + "_" + "PRODUCTRECEIVEHDR" + "]");
		 
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
			 throw e;
		 } finally {
			 if (con != null) {
				 DbBean.closeConnection(con);
			 }
		 }
		 return alData;
	 }
	 
		public ArrayList selectProductReturnHdr(String query, Hashtable ht, String extCond)
				throws Exception {
//			boolean flag = false;
			ArrayList alData = new ArrayList();
			java.sql.Connection con = null;

			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ ht.get("PLANT") + "_" + "PRODUCTRETURNHDR" + "]");

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
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return alData;
		}
		
		public int addProductReceiveHdr(Hashtable ht, String plant)throws Exception {
			boolean flag = false;
			int invoiceHdrId = 0;
			int count = 0;
			Connection connection = null;
			PreparedStatement ps = null;
			List<String> args = null;
		    String query = "";
			try {
				 /*Instantiate the list*/
			    args = new ArrayList<String>();		    
				connection = DbBean.getConnection();			
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = ht.keys();
				for (int i = 0; i < ht.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					System.out.println(key);
					String value = StrUtils.fString((String) ht.get(key));
					
					System.out.println("---------  "+value);
					args.add(value);
					FIELDS += key + ",";
					VALUES += "'"+value+"',";
					//VALUES += value+",";
				}
				query = "INSERT INTO ["+ plant +"_PRODUCTRECEIVEHDR] ("
						+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
						+ VALUES.substring(0, VALUES.length() - 1) + ")";

				if(connection != null){
					  /*Create  PreparedStatement object*/
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   
				this.mLogger.query(this.printQuery, query);
				//invoiceHdrId = execute_NonSelectQueryGetLastInsert(ps, args);
				count = ps.executeUpdate();
				ResultSet rs = ps.getGeneratedKeys();
				   if (rs.next()){
					   invoiceHdrId = rs.getInt(1);
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
			return invoiceHdrId;
		}
		
		public boolean addProductReceiveDet(List<Hashtable<String, String>> invoiceDetInfoList, String plant) 
				throws Exception {
			boolean flag = false;
			Connection connection = null;
			PreparedStatement ps = null;
			List<String> args = null;
			String query = "";
			try {		
				/*Instantiate the list*/
				args = new ArrayList<String>();
				
				connection = DbBean.getConnection();
				
				for (Hashtable<String, String> invoiceDetInfo : invoiceDetInfoList) {
					String FIELDS = "", VALUES = "";
					Enumeration enumeration = invoiceDetInfo.keys();
					for (int i = 0; i < invoiceDetInfo.size(); i++) {
						String key = StrUtils.fString((String) enumeration.nextElement());
						String value = StrUtils.fString((String) invoiceDetInfo.get(key));
						args.add(value);
						FIELDS += key + ",";
						VALUES += "?,";
					}		
					query = "INSERT INTO ["+ plant +"_PRODUCTRECEIVEDET]  ("
					+ FIELDS.substring(0, FIELDS.length() - 1)
					+ ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
					
					if(connection != null){
						/*Create  PreparedStatement object*/
						ps = connection.prepareStatement(query);
						this.mLogger.query(this.printQuery, query);
						flag = execute_NonSelectQuery(ps, args);
						if(flag){
							args.clear();
						}
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
			return flag;		
		}
		
		public List getConvProductReceiveHdrById(Hashtable ht) throws Exception {
			java.sql.Connection con = null;
			List<Map<String, String>> invoiceHdrList = new ArrayList<>();
			Map<String, String> map = null;
			String query="";
			List<String> args = null;
			try {			
				/*Instantiate the list*/
			    args = new ArrayList<String>();
			    
				con = com.track.gates.DbBean.getConnection();
				query="SELECT PLANT,ID, CustCode,CustName,ICRNO,RECEIVE_DATE,PAYMENT_TERMS,EMPNO,ITEM_RATES,ISNULL(ORDER_DISCOUNT,0) as ORDER_DISCOUNT,"
						+ "(case when DISCOUNT_TYPE='%' then ISNULL(DISCOUNT, 0) else (ISNULL(DISCOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_PRODUCTRECEIVEDET] WHERE RECEIVEHDRID = A.ID ),1)) end) DISCOUNT,"
						+ "DISCOUNT_TYPE,DISCOUNT_ACCOUNT,"
						+ "(ISNULL(SHIPPINGCOST, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_PRODUCTRECEIVEDET] WHERE RECEIVEHDRID = A.ID ),1)) SHIPPINGCOST, "
						+ "(ISNULL(ADJUSTMENT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_PRODUCTRECEIVEDET] WHERE RECEIVEHDRID = A.ID ),1))  ADJUSTMENT,"
						+ "(ISNULL(SUB_TOTAL, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_PRODUCTRECEIVEDET] WHERE RECEIVEHDRID = A.ID ),1)) SUB_TOTAL, "
						+ "ISNULL(TAX_STATUS,'') TAX_STATUS,ISNULL(JobNum,'') JobNum,ISNULL(TAXTREATMENT,'') TAXTREATMENT,ISNULL(A.EMPNO,0) EMPNO,ISNULL((SELECT FNAME FROM " +ht.get("PLANT")  +"_EMP_MST E where E.EMPNO=A.EMPNO),'') as EMP_NAME,ISNULL(A.ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,ISNULL(A.TAXID,'0') TAXID,ISNULL(A.ISDISCOUNTTAX,'0') ISDISCOUNTTAX,ISNULL(A.ISORDERDISCOUNTTAX,'0') ISORDERDISCOUNTTAX,ISNULL(A.ISSHIPPINGTAX,'0') ISSHIPPINGTAX,ISNULL(A.TRANSPORTID,0) TRANSPORTID,"
						+ "(ISNULL(TOTAL_AMOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_PRODUCTRECEIVEDET] WHERE RECEIVEHDRID = A.ID ),1)) TOTAL_AMOUNT,"
						+ "RECEIVE_STATUS,NOTE,CRAT,CRBY,UPAT,UPBY,"
						+ "(ISNULL(TAXAMOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_PRODUCTRECEIVEDET] WHERE RECEIVEHDRID = A.ID ),1)) as TAXAMOUNT,"
						+ " ISNULL(CURRENCYID, '') CURRENCYID,ISNULL(INBOUND_GST, 0) INBOUND_GST,ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_PRODUCTRECEIVEDET] WHERE RECEIVEHDRID = A.ID ),1) CURRENCYUSEQT, "
						+ " ISNULL((SELECT ISNULL(DISPLAY,'') DISPLAY FROM ["+ht.get("PLANT")+"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID), '') DISPLAY,"
						+ " ISNULL(SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(SHIPHPNO,'') SHIPHPNO,ISNULL(SHIPEMAIL,'') SHIPEMAIL,ISNULL(SHIPCOUNTRY,'') SHIPCOUNTRY,ISNULL(SHIPADDR1,'') SHIPADDR1,ISNULL(SHIPADDR2,'') SHIPADDR2,ISNULL(SHIPADDR3,'') SHIPADDR3,ISNULL(SHIPADDR4,'') SHIPADDR4,ISNULL(SHIPSTATE,'') SHIPSTATE,ISNULL(SHIPZIP,'') SHIPZIP,"
						+ "ISNULL(SHIPPINGID,'') as SHIPPINGID,ISNULL(SHIPPINGCUSTOMER,'') as SHIPPINGCUSTOMER,ISNULL(CREDITNOTESSTATUS,'') as CREDITNOTESSTATUS,ISNULL(TOTAL_PAYING,'') as TOTAL_PAYING FROM "
				+ "[" + ht.get("PLANT") + "_PRODUCTRECEIVEHDR] A WHERE ID = ? AND PLANT =? ";
				PreparedStatement ps = con.prepareStatement(query);  
				
				/*Storing all the query param argument in list squentially*/
				args.add((String) ht.get("ID"));
				args.add((String) ht.get("PLANT"));
				
				this.mLogger.query(this.printQuery, query);
				
				invoiceHdrList = selectData(ps, args);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return invoiceHdrList;
		}
	 
	 public List getConvProductReceiveDetByHdrId(Hashtable ht) throws Exception {
			java.sql.Connection con = null;
			List<Map<String, String>> InvoiceDetList = new ArrayList<>();
			Map<String, String> map = null;
			String query="";
			List<String> args = null;
			try {			
				/*Instantiate the list*/
			    args = new ArrayList<String>();
			    
				con = com.track.gates.DbBean.getConnection();
				query="SELECT A.PLANT,ID, LNNO,LNSTAT,ICRNO,RECEIVEHDRID,ITEM,ACCOUNT_NAME,QTY,QTYRC,"
						+ "(UNITPRICE*ISNULL(CURRENCYUSEQT,1)) UNITPRICE,ISNULL(CURRENCYUSEQT,1) CURRENCYUSEQT,"
						+ "(case when DISCOUNT_TYPE='%' then DISCOUNT else (DISCOUNT*ISNULL(CURRENCYUSEQT,1)) end) DISCOUNT,"
						+ "ISNULL(DISCOUNT_TYPE,0) DISCOUNT_TYPE,TAX_TYPE,ISNULL(GSTPERCENTAGE,0) GSTPERCENTAGE,ISNULL(UNITCOST,0) UNITCOST,"
						+ "(AMOUNT*ISNULL(CURRENCYUSEQT,1)) AMOUNT,"
						+ "ISNULL(UOM,'') UOM,"
						+ "ISNULL((SELECT ITEMDESC FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS ITEMDESC,"
						+ "ISNULL((SELECT HSCODE FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS HSCODE,"
						+ "ISNULL((SELECT COO FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS COO,"
						+ "ISNULL((SELECT CASE WHEN PRD_BRAND_ID = 'NOBRAND' THEN '' ELSE PRD_BRAND_ID END AS BRAND FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS BRAND,"
						+ "ISNULL((SELECT REMARK1 FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS DETAILDESC,"
						+ "CRAT,CRBY,UPAT,UPBY  FROM "
				+ "[" + ht.get("PLANT") + "_PRODUCTRECEIVEDET] A LEFT JOIN "
				+ "[" + ht.get("PLANT") + "_COMPANY_CONFIG] B ON A.TAX_TYPE = B.GSTTYPE"
						+ " WHERE RECEIVEHDRID = ? AND A.PLANT =? ORDER BY A.LNNO";
				PreparedStatement ps = con.prepareStatement(query);  
				
				/*Storing all the query param argument in list squentially*/
				args.add((String) ht.get("RECEIVEHDRID"));
				args.add((String) ht.get("PLANT"));
				
				this.mLogger.query(this.printQuery, query);
				
				InvoiceDetList = selectData(ps, args);
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return InvoiceDetList;
		}
	 
	 public List getConvProductReceiveDetByHdrIdbyLnno(Hashtable ht) throws Exception {
		 java.sql.Connection con = null;
		 List<Map<String, String>> InvoiceDetList = new ArrayList<>();
		 Map<String, String> map = null;
		 String query="";
		 List<String> args = null;
		 try {			
			 /*Instantiate the list*/
			 args = new ArrayList<String>();
			 
			 con = com.track.gates.DbBean.getConnection();
			 query="SELECT A.PLANT,ID, LNNO,LNSTAT,ICRNO,RECEIVEHDRID,ITEM,ACCOUNT_NAME,QTY,QTYRC,"
					 + "(UNITPRICE*ISNULL(CURRENCYUSEQT,1)) UNITPRICE,ISNULL(CURRENCYUSEQT,1) CURRENCYUSEQT,"
					 + "(case when DISCOUNT_TYPE='%' then DISCOUNT else (DISCOUNT*ISNULL(CURRENCYUSEQT,1)) end) DISCOUNT,"
					 + "ISNULL(DISCOUNT_TYPE,0) DISCOUNT_TYPE,TAX_TYPE,ISNULL(GSTPERCENTAGE,0) GSTPERCENTAGE,ISNULL(UNITCOST,0) UNITCOST,"
					 + "(AMOUNT*ISNULL(CURRENCYUSEQT,1)) AMOUNT,"
					 + "ISNULL(UOM,'') UOM,"
					 + "ISNULL((SELECT ITEMDESC FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS ITEMDESC,"
					 + "ISNULL((SELECT HSCODE FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS HSCODE,"
					 + "ISNULL((SELECT COO FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS COO,"
					 + "ISNULL((SELECT CASE WHEN PRD_BRAND_ID = 'NOBRAND' THEN '' ELSE PRD_BRAND_ID END AS BRAND FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS BRAND,"
					 + "ISNULL((SELECT REMARK1 FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS DETAILDESC,"
					 + "CRAT,CRBY,UPAT,UPBY  FROM "
					 + "[" + ht.get("PLANT") + "_PRODUCTRECEIVEDET] A LEFT JOIN "
					 + "[" + ht.get("PLANT") + "_COMPANY_CONFIG] B ON A.TAX_TYPE = B.GSTTYPE"
					 + " WHERE RECEIVEHDRID = ? AND A.PLANT =? AND A.LNNO =? ORDER BY A.LNNO";
			 PreparedStatement ps = con.prepareStatement(query);  
			 
			 /*Storing all the query param argument in list squentially*/
			 args.add((String) ht.get("RECEIVEHDRID"));
			 args.add((String) ht.get("PLANT"));
			 args.add((String) ht.get("LNNO"));
			 
			 this.mLogger.query(this.printQuery, query);
			 
			 InvoiceDetList = selectData(ps, args);
			 
		 } catch (Exception e) {
			 this.mLogger.exception(this.printLog, "", e);
			 throw e;
		 } finally {
			 if (con != null) {
				 DbBean.closeConnection(con);
			 }
		 }
		 return InvoiceDetList;
	 }
	 
	 public ArrayList selectReceivingProduct(String query, Hashtable ht)
				throws Exception {
//			boolean flag = false;
			ArrayList<Map<String, String>> alData = new ArrayList<>();
			java.sql.Connection con = null;
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ ht.get("PLANT") + "_" + "PRODUCTRECEIVEHDR" + "] A,");
			sql.append("[" + ht.get("PLANT") + "_" + "CUSTMST" + "] B");
			String conditon = "";

			try {
				con = com.track.gates.DbBean.getConnection();

				if (ht.size() > 0) {
					sql.append(" WHERE ");

					conditon = "a.plant='" + ht.get("PLANT") + "' and a.ICRNO ='"
							+ ht.get("ICRNO") + "' and a.CustCode=b.custno";

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
	

}

