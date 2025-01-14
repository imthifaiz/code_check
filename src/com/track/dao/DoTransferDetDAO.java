package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.db.object.DoDet;
import com.track.db.util.PlantMstUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class DoTransferDetDAO extends BaseDAO {

	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.DOTRANSFERDETDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.DOTRANSFERDETDAO_PRINTPLANTMASTERLOG;
	
	public static String TABLE_DET = "DOTransferDET";

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

	public DoTransferDetDAO() {
		_StrUtils = new StrUtils();
	}

	public static String TABLE_NAME = "DOTRANSFERDET";
	public static String plant = "";

	public DoTransferDetDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "DOTRANSFERDET" + "]";
	}

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "DOTRANSFERDET" + "]");
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
					+ "[" + ht.get("PLANT") + "_" + "DOTRANSFERDET" + "]");
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
	
	public Map selectDoTransferQty(String query, Hashtable ht, String extCond)
	throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
			+ "[" + ht.get("PLANT") + "_" + "DOTRANSFER_PICK" + "]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);

			if (extCond.length() > 0)
			{ sql.append(" and " + extCond + "group by dono,item");}
			else{ sql.append(" group by dono,item");  }
				

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
	
	
	public ArrayList selectDoTransferDet(String query, Hashtable ht, String extCond)
	throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
		+ ht.get("PLANT") + "_" + "DOTRANSFERDET" + "]");
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

	public boolean isExisit(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "DOTRANSFERDET" + "]");

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
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "DOTRANSFERDET" + "]");
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
	
	public boolean isExisitDoTransferPick(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "DOTRANSFER_PICK" + "]");
			sql.append(" WHERE  " + formCondition(ht));
			if (extCond.length() > 0)
				sql.append("  " + extCond);

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
        
    public boolean isValidPrdToTransferPick(Hashtable ht,String plant, String extCond) throws Exception {
            boolean flag = false;
            java.sql.Connection con = null;
            try {
                    con = com.track.gates.DbBean.getConnection();
                    StringBuffer sql = new StringBuffer("Select  COUNT(*) from " + "[" + plant + "_" + "DOTRANSFER_PICK" + "] a  ,["+plant+"_INVMST] b where  a.ITEM=b.ITEM and a.TOLOC =b.LOC and a.BATCH =b.userfld4 and ");
                    sql.append("  ");
                    sql.append(" ");
                    
                    sql.append("   " + formCondition(ht));
                    if (extCond.length() > 0)
                            sql.append("  " + extCond);

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

	public ArrayList selectDOTRANSFERDET(String query, Hashtable ht) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOTRANSFERDET" + "]");
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
	
	public ArrayList getDoTransferDetailsByWMS(String plant,
			String orderno) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;

			StringBuffer sQry = new StringBuffer(
					"select a.dono,a.dolnno,a.item,isnull(a.qtyor,0) as qtyor,isnull(a.qtypick,0) as qtypick,isnull(a.qtyis,0) as qtyis,isnull(a.userfld2,'') as ref,isnull(a.userfld3,'') as custname,");
			sQry
					.append(" isnull(name,'') contactname,isnull(b.telno,'') as telno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3 ");
			sQry.append(" from  " + "[" + plant + "_" + "dodet" + "]" + " a,"
					+ "[" + plant + "_" + "custmst" + "]" + " b");
			sQry
					.append(" where a.plant='" + plant + "' and a.dono like '"
							+ orderno
							+ "%' and a.LNSTAT <> 'C' and a.userfld3=b.cname");

			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "DOTRANSFERDET";
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
	public Boolean removeOrder(String plant2, String tono) throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant2 + "_" + "DOTRANSFERDET] WHERE DONO='"
					+ tono + "' AND PLANT='" + plant2 + "'";
			this.mLogger.query(this.printQuery, sql.toString());
			this.DeleteRow(connection, sql);
			return Boolean.valueOf(true);
		} catch (Exception e) {
			MLogger.log(0, "[ERROR] : " + e);
			return Boolean.valueOf(false);
		} finally {
			DbBean.closeConnection(connection);
		}
	}

	public ArrayList selectDOTRANSFERDET(String query, Hashtable ht, String extCond)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOTRANSFERDET" + "] ");
		String conditon = "";
		

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");
				sql.append(" DONO = '" + ht.get("DONO") + "' AND PLANT = '"
						+ ht.get("PLANT") + "' ");
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

	public boolean insertDOTRANSFERDET(Hashtable ht) throws Exception {
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
					+ "DOTRANSFERDET" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Transfer order created already");
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
					+ htCondition.get("PLANT") + "_" + "DOTRANSFERDET" + "]");
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
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;
	}

	
	
	
	public ArrayList getDoTransferReceivingDetailsByWMS(String plant,
			String orderno) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;

			StringBuffer sQry = new StringBuffer(
					"select a.dono,a.dolnno,a.item,isnull(a.qtyor,0) as qtyor,isnull(a.qtypick,0) as qtypick,isnull(a.qtyis,0) as qtyis,isnull(a.userfld2,'') as ref,isnull(a.userfld3,'') as custname,");
			sQry
					.append(" isnull(name,'') contactname,isnull(b.telno,'') as telno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3 ");
			sQry.append(" from  " + "[" + plant + "_" + "dotransferdet" + "]" + " a,"
					+ "[" + plant + "_" + "custmst" + "]" + " b");
			sQry
					.append(" where a.plant='" + plant + "' and a.dono like '"
							+ orderno
							+ "%' and a.LNSTAT <> 'C' and a.userfld3=b.cname");

			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "DOTRANSFERDET";
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

	public boolean insertDoPickDet(Hashtable ht) throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			String TABLE = "TO_PICK";
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _StrUtils.fString((String) enum1.nextElement());
				String value = _StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_" + TABLE
					+ "]" + "(" + FIELDS.substring(0, FIELDS.length() - 1)
					+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1)
					+ ")";

			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);
			TABLE_NAME = "TO_PICK ";
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

	public ArrayList getDoTransferItemListByWMS(String plant, String orderno,
			String itemno) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {

			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select tono,tolnno,item,isnull(qtyor,0)as qtyor,isnull(qtyrc,0) as qtyrc,isnull(userfld2,'') as ref,isnull(userfld3,'') as custname from "
					+ "["
					+ plant
					+ "_"
					+ "DOTRANSFERDET"
					+ "]"
					+ " where plant='"
					+ plant
					+ "'and tono = '"
					+ orderno
					+ "'  and lnstat <> 'C' and item like '" + itemno + "%'";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);
			TABLE_NAME = "DOTRANSFERDET";

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

	public boolean delete(java.util.Hashtable ht) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_DOTRANSFERDET]");
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

	public Boolean DoremoveOrderDetails(String plant2, String dono)
			throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant2 + "_" + "DOTRANSFERDET] WHERE DONO='"
					+ dono + "' AND PLANT='" + plant2 + "'";
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

	// By Samatha to show the list of batch to receive for To Order
	public ArrayList getDoToOrderBatchListToRecv(String plant, String ordno,
			String ordlnno, String item) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sQry = new StringBuffer(
					"select distinct  batch,sum(pickqty) as pickQty,isnull((select sum(recqty) from  "
							+ "["
							+ plant
							+ "_"
							+ "Recvdet"
							+ "]"
							+ " where plant ='"
							+ plant
							+ "' and pono ='"
							+ ordno
							+ "' and lnno='"
							+ ordlnno
							+ "' and item ='"
							+ item
							+ "' and batch =a.batch),0) as recQty from "
							+ "["
							+ plant
							+ "_"
							+ "to_pick"
							+ "] as a"
							+ " where plant='" + plant + "' and  ");
			sQry.append(" ");
			sQry.append("  tono ='" + ordno + "' and tolno='" + ordlnno
					+ "' and  item='" + item + "'  and  batch  ");
			sQry.append(" like '%' and PickQty > 0 group by batch ");

			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());

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
	
	
	public boolean insertDoTransferPickDet(Hashtable ht) throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			String TABLE = "DOTRANSFER_PICK";
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _StrUtils.fString((String) enum1.nextElement());
				String value = _StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_" + TABLE
					+ "]" + "(" + FIELDS.substring(0, FIELDS.length() - 1)
					+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1)
					+ ")";

			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);
			TABLE_NAME = "DOTRANSFER_PICK";
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
	
	public boolean updateDoTransferPickDet(String query, Hashtable htCondition, String extCond)
	throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "["
			+ htCondition.get("PLANT") + "_" + "DOTRANSFER_PICK" + "]");
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
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;
	}
	
	public boolean updateDoTransferPickQtyZero(String query, Hashtable htCondition, String extCond)
	throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "["
			+ htCondition.get("PLANT") + "_" + "DOTRANSFER_PICK" + "]");
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE PICKQTY<=0 AND ");
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
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;
	}
	
		
	public String getDoTransferPickQty(String aPlant, String aOrderno ,String aItem, String aDolnno) throws Exception {
		String pickqty = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("DONO", aOrderno);
		ht.put("ITEM", aItem);
		ht.put("DOLNO", aDolnno);
		
		String query = " isnull(sum(pickqty),0) as pickqty ";
		
		Map m = selectDoTransferQty(query, ht, "");
		if (m.size() > 0) {
			pickqty = (String) m.get("pickqty");
		} else {
			pickqty = "";
		}
		if (pickqty.equalsIgnoreCase(null) || pickqty.length() == 0) {
			pickqty = "0";
		}
		return pickqty;
	}
		
	public boolean deleteDoDet(java.util.Hashtable ht) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_DOTRANSFERDET]");
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
	
	
	//start code by Bruhan for random outbound transfer on 19 june 2013
	

	
	public String getDoTransferPickQty_Random(String aPlant, String aOrderno ,String aItem) throws Exception {
		String pickqty = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("DONO", aOrderno);
		ht.put("ITEM", aItem);
		
		
		String query = " isnull(sum(pickqty),0) as pickqty ";
		
		Map m = selectDoTransferQty(query, ht, "");
		if (m.size() > 0) {
			pickqty = (String) m.get("pickqty");
		} else {
			pickqty = "";
		}
		if (pickqty.equalsIgnoreCase(null) || pickqty.length() == 0) {
			pickqty = "0";
		}
		return pickqty;
	}
	
	
	public ArrayList getDOTransferDetDetailsRandom(String Plant,
			String DONO,String item) {
	 
		ArrayList al = null;
		Connection con = null;
		PlantMstUtil _PlantMstUtil = new PlantMstUtil();
		try {
			con = DbBean.getConnection();
	
			String query = "select dono,dolnno, lnstat,item,isnull(qtyor,0) as qtyor,"
				+ " isnull(qtyPick,0) as qtyPick , (isnull(qtyor,0)- isnull(qtyPick,0)) totBal "
				+ " from "
				+ " " +Plant+"_DOTRANSFERDET a where dono = '"+DONO+"' and plant <> '' and PickStatus <>'C' " 
			    + " and ITEM = '"+item+"' order by dolnno";

			this.setmLogger(mLogger);
			this.mLogger.query(this.printQuery, query.toString());
			al = selectData(con, query.toString());
      		return al;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			return null;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

	}
	
	//End code by Bruhan for random outbound transfer on 19 june 2013	
	
	public boolean insertDoDet(List<DoDet> doDetList) throws Exception {
		boolean insertFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			
			for(DoDet doDet : doDetList) {
				query = "INSERT INTO ["+doDet.getPLANT()+"_"+TABLE_DET+"]" + 
						"           ([PLANT]" + 
						"           ,[DONO]" + 
						"           ,[DOLNNO]" + 
						"           ,[PickStatus]" + 
						"           ,[LNSTAT]" + 
						"           ,[ITEM]" + 
						"           ,[ItemDesc]" + 
						"           ,[TRANDATE]" + 
						"           ,[UNITPRICE]" + 
						"           ,[QTYOR]" + 
						"           ,[QTYIS]" + 
						"           ,[QtyPick]" + 
						"           ,[UNITMO]" +  
						"           ,[CRAT]" + 
						"           ,[CRBY]" + 
						"           ,[USERFLD1]" + 
						"           ,[USERFLD2]" + 
						"           ,[USERFLD3]" + 
						"           ,[CURRENCYUSEQT]" + 
						"           ,[ESTNO]" + 
						"           ,[ESTLNNO]" + 
						"           ,[PRODGST]" + 
						"           ,[PRODUCTDELIVERYDATE]" +
						"           ,[TAX_TYPE]" +
						"           ,[ACCOUNT_NAME]" +
						"           ,[DISCOUNT]" +
						"           ,[DISCOUNT_TYPE])" +
						"     VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, doDet.getPLANT());
				   ps.setString(2, doDet.getDONO());
				   ps.setLong(3, doDet.getDOLNNO());
				   ps.setString(4, doDet.getPickStatus());
				   ps.setString(5, doDet.getLNSTAT());
				   ps.setString(6, doDet.getITEM());
				   ps.setString(7, doDet.getItemDesc());
				   ps.setString(8, doDet.getTRANDATE());
				   ps.setDouble(9, doDet.getUNITPRICE());
				   ps.setBigDecimal(10, doDet.getQTYOR());
				   ps.setBigDecimal(11, doDet.getQTYIS());
				   ps.setBigDecimal(12, doDet.getQtyPick());
				   ps.setString(13, doDet.getUNITMO());
				   ps.setString(14, doDet.getCRAT());
				   ps.setString(15, doDet.getCRBY());
				   ps.setString(16, doDet.getUSERFLD1());
				   ps.setString(17, doDet.getUSERFLD2());
				   ps.setString(18, doDet.getUSERFLD3());
				   ps.setDouble(19, doDet.getCURRENCYUSEQT());
				   ps.setString(20, doDet.getESTNO());
				   ps.setInt(21, doDet.getESTLNNO());
				   ps.setDouble(22, doDet.getPRODGST());
				   ps.setString(23, doDet.getPRODUCTDELIVERYDATE());
				   ps.setString(24, doDet.getTAX_TYPE());
				   ps.setString(25, doDet.getACCOUNT_NAME());
				   ps.setDouble(26, doDet.getDISCOUNT());
				   ps.setString(27, doDet.getDISCOUNT_TYPE());
				   
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   insertFlag = true;
				   }
				   else
				   {
					   throw new SQLException("Creating Sales Order failed, no rows affected.");
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
	
	public boolean updateDoDet(List<DoDet> doDetList) throws Exception {
		boolean updateFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			
			for(DoDet doDet : doDetList) {
				query = "UPDATE ["+doDet.getPLANT()+"_"+TABLE_DET+"] SET " + 
						"           [PLANT] = ?" + 
						"           ,[DONO] = ?" + 
						"           ,[DOLNNO] = ?" + 
						"           ,[PickStatus] = ?" + 
						"           ,[LNSTAT] = ?" + 
						"           ,[ITEM] = ?" + 
						"           ,[ItemDesc] = ?" + 
						"           ,[TRANDATE] = ?" + 
						"           ,[UNITPRICE] = ?" + 
						"           ,[QTYOR] = ?" + 
						"           ,[UNITMO] = ?" +  
						"           ,[UPAT] = ?" + 
						"           ,[UPBY] = ?" + 
						"           ,[USERFLD1] = ?" + 
						"           ,[USERFLD2] = ?" + 
						"           ,[USERFLD3] = ?" + 
						"           ,[CURRENCYUSEQT] = ?" + 
						"           ,[PRODGST] = ?" + 
						"           ,[PRODUCTDELIVERYDATE] = ?" +
						"           ,[TAX_TYPE] = ?" +
						"           ,[ACCOUNT_NAME] = ?" +
						"           ,[DISCOUNT] = ?" +
						"           ,[DISCOUNT_TYPE] = ?" +
						"     WHERE DONO = ? AND DOLNNO = ?";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, doDet.getPLANT());
				   ps.setString(2, doDet.getDONO());
				   ps.setLong(3, doDet.getDOLNNO());
				   ps.setString(4, doDet.getPickStatus());
				   ps.setString(5, doDet.getLNSTAT());
				   ps.setString(6, doDet.getITEM());
				   ps.setString(7, doDet.getItemDesc());
				   ps.setString(8, doDet.getTRANDATE());
				   ps.setDouble(9, doDet.getUNITPRICE());
				   ps.setBigDecimal(10, doDet.getQTYOR());
				   ps.setString(11, doDet.getUNITMO());
				   ps.setString(12, doDet.getUPAT());
				   ps.setString(13, doDet.getUPBY());
				   ps.setString(14, doDet.getUSERFLD1());
				   ps.setString(15, doDet.getUSERFLD2());
				   ps.setString(16, doDet.getUSERFLD3());
				   ps.setDouble(17, doDet.getCURRENCYUSEQT());
				   ps.setDouble(18, doDet.getPRODGST());
				   ps.setString(19, doDet.getPRODUCTDELIVERYDATE());
				   ps.setString(20, doDet.getTAX_TYPE());
				   ps.setString(21, doDet.getACCOUNT_NAME());
				   ps.setDouble(22, doDet.getDISCOUNT());
				   ps.setString(23, doDet.getDISCOUNT_TYPE());
				   
				   ps.setString(23, doDet.getDONO());
				   ps.setLong(24, doDet.getDOLNNO());
				   
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   updateFlag = true;
				   }
				   else
				   {
					   throw new SQLException("Updating Sales Order failed, no rows affected.");
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
	
}
