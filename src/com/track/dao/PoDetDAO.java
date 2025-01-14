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
import com.track.db.object.PoDet;
import com.track.db.object.PoDetRemarks;
import com.track.db.object.PoHdr;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PoDetDAO extends BaseDAO {

	public static String TABLE_NAME = "PODET";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.PODETDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.PODETDAO_PRINTPLANTMASTERLOG;

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

	public PoDetDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "PODET" + "]";
	}

	public PoDetDAO() {

	}

	public boolean isExisit(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_PODET]");
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
			TABLE_NAME = "PODET";
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
                    sql.append(" FROM " + "[" + ht.get("PLANT") + "_PODET]");
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
			TABLE_NAME="PODET";
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+"["+ht.get(IDBConstants.PLANT)+"_PODET]");
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
		TABLE_NAME = "PODET";
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
			TABLE_NAME = "PODET";
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
	
	public ArrayList selectPoEstDet(String query, Hashtable ht) throws Exception {
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

	public ArrayList selectPoDet(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_PODET]");
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
			TABLE_NAME = "PODET";
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
			TABLE_NAME = "PODET";
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
				+ plant + "_" + "PODET" + "] a ");
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
			TABLE_NAME = "PODET";
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
				+ plant + "_" + "PODET" + "]a,[" + plant + "_" + "RECVDET"
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
			TABLE_NAME = "PODET";
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_PODET ]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);
			TABLE_NAME = "PODET";
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
			StringBuffer sql = new StringBuffer(" UPDATE " + "[" + htCondition.get("PLANT") + "_"+ "PODET" + "]");
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
                    sql.append(" FROM "  + "[" + ht.get("PLANT") + "_"+ "PODET" + "]");
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

			String sQry = "select pono,polnno,item,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,isnull(userfld2,'') as ref,isnull(userfld3,'') as custname from "
					+ "["
					+ plant
					+ "_"
					+ "podet"
					+ "]"
					+ " where plant='"
					+ plant
					+ "' and pono like '"
					+ orderno
					+ "%' and lnstat <> 'C'";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);
			TABLE_NAME = "PODET";
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

	public ArrayList getInboundItemListByWMS(String plant, String orderno,
			String itemno) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;

			String sQry = "select pono,polnno,item,isnull(qtyor,0)as qtyor,isnull(qtyrc,0) as qtyrc,isnull(userfld2,'') as ref,isnull(userfld3,'') as custname from "
					+ "["
					+ plant
					+ "_"
					+ "podet"
					+ "]"
					+ " where plant='"
					+ plant
					+ "'and pono = '"
					+ orderno
					+ "'  and lnstat <> 'C' and item like '" + itemno + "%'";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);
			TABLE_NAME = "PODET";

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
	public ArrayList getInboundItemListByWMS(String plant, String orderno,
			String itemno,String lnno) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;

			String sQry = "select pono,polnno,item,isnull(qtyor,0)as qtyor,isnull(qtyrc,0) as qtyrc,isnull(userfld2,'') as ref,isnull(userfld3,'') as custname,isnull(UNITMO,'') as uom,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM a where UOM=UNITMO),1) UOMQTY,(UNITCOST*CURRENCYUSEQT) as UNITCOST from "
					+ "["
					+ plant
					+ "_"
					+ "podet"
					+ "]"
					+ " where plant='"
					+ plant
					+ "'and pono = '"
					+ orderno
					+ "'"+ "and polnno = '"
					+ lnno
					+ "'"
					+"and lnstat <> 'C' and item like '" + itemno + "%'";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);
			TABLE_NAME = "PODET";

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
	public ArrayList getInboundReverseCheckItemListByWMS(String plant,
			String orderno, String orderlnno, String itemno) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;

			String sQry = "select isnull(qtyrc,0) as qtyrc from " + "[" + plant
					+ "_" + "podet" + "]" + " where plant='" + plant
					+ "'and pono = '" + orderno + "'  and  item = '" + itemno
					+ "' and polnno='" + orderlnno + "'";
			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry);
			TABLE_NAME = "PODET";

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

	public ArrayList getInboundReverseItemListByWMS(String plant,
			String orderno, String itemno) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {

			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select pono,polnno,item,isnull(qtyor,0)as qtyor,isnull(qtyrc,0) as qtyrc,isnull(userfld2,'') as ref,isnull(userfld3,'') as custname from "
					+ "["
					+ plant
					+ "_"
					+ "podet"
					+ "]"
					+ " where plant='"
					+ plant
					+ "'and pono = '"
					+ orderno
					+ "'   and item like '"
					+ itemno + "%'";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);
			TABLE_NAME = "PODET";

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

	public Boolean removeOrderDetails(String plant, String pono)
			throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant + "_" + "PODET] WHERE PONO='"
					+ pono + "' AND PLANT='" + plant + "'";
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
					+ ht.get("PLANT") + "_POHDR]" + " WHERE "
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
			String sQry = "SELECT MAX(POLNNO) FROM " + "["
					+ ht.get("PLANT") + "_PODET]" + " WHERE "
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

	public ArrayList getPODetDetailsRandom(String Plant,
				String PONO,String item) {
		 
			ArrayList al = null;
			Connection con = null;
			PlantMstUtil _PlantMstUtil = new PlantMstUtil();
			try {
				con = DbBean.getConnection();
				
				String query = "select pono,polnno, item,isnull(qtyor,0) as Qtyor,isnull(QtyRc,0) as QtyRc,"
					+ " (isnull(QtyOr,0)- isnull(QtyRc,0)) totBal,isnull(UNITCOST,0) as UNITCOST "
					+ " from "
					+ " " +Plant+"_PODET a where pono = '"+PONO+"' and plant <> '' and LNSTAT <>'C' " 
				    + " and ITEM = '"+item+"' order by polnno";

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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_PODET_REMARKS]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);
			TABLE_NAME = "PODET";
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
				+ ht.get("PLANT") + "_" + "PODET_REMARKS" + "] a ");
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
                 sql.append(" FROM " + "[" + ht.get("PLANT") + "_PODET_REMARKS]");
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
	 public boolean updatePoMultiRemarks(String query, Hashtable htCondition, String extCond,
				String aPlant) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" UPDATE " + "[" + aPlant + "_"
						+ "PODET_REMARKS" + "]");
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
				sql.append(" FROM " + "[" + ht.get("PLANT") + "_PODET_REMARKS]");
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
				TABLE_NAME="PODET_REMARKS";
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
	public ArrayList getPODetDetailsRandomMutiUOM(String Plant,
			String PONO,String item,String uom) {
	 
		ArrayList al = null;
		Connection con = null;
		PlantMstUtil _PlantMstUtil = new PlantMstUtil();
		try {
			con = DbBean.getConnection();
			
			String query = "select pono,polnno, item,isnull(qtyor,0) as Qtyor,isnull(QtyRc,0) as QtyRc,"
				+ " (isnull(QtyOr,0)- isnull(QtyRc,0)) totBal,isnull(UNITCOST,0) as UNITCOST "
				+ " from "
				+ " " +Plant+"_PODET a where pono = '"+PONO+"' and plant <> '' and LNSTAT <>'C' " 
			    + " and ITEM = '"+item+"' and UNITMO = '"+uom+"' order by polnno";

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
	public ArrayList getUOMByOrder(String Plant,String user,String PONO,String item) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
				con = DbBean.getConnection();			
			String query = "select UNITMO as uom,ISNULL(QPUOM,1) as uomqty"				
				+ " from "
				+ " " +Plant+"_PODET a join " +Plant+"_UOM u on u.UOM=a.UNITMO where pono = '"+PONO+"' and a.plant <> '' and LNSTAT <>'C' " 
			    + " and ITEM = '"+item+"'  order by polnno";

			this.setmLogger(mLogger);
			this.mLogger.query(this.printQuery, query.toString());
			al = selectData(con, query.toString());   		

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
	
	/*Start code by Abhilash on 02-11-2019*/
	public String getQtyToBeRecvByProduct(String aItem, String aPlant)
			throws Exception {
		java.sql.Connection con = null;
		String qtyToBeRecv = "";
		try {
			con = com.track.gates.DbBean.getConnection();
			String query = "select (ISNULL(SUM(QTYOR),0)-ISNULL(SUM(QTYRC),0))* ISNULL((select u.QPUOM from ["+aPlant+"_UOM] u where u.UOM=B.INVENTORYUOM),1) AS QTYTOBERECV from ["+aPlant+"_PODET] A JOIN ["+aPlant+"_ITEMMST] B ON A.ITEM=B.ITEM where A.ITEM=? AND A.ITEM IN (SELECT ITEM FROM ["+aPlant+"_ITEMMST] WHERE NONSTKFLAG <> 'Y') and LNSTAT <>'C' AND A.PLANT=? GROUP BY A.ITEM,B.INVENTORYUOM";
			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,aItem);  
			stmt.setString(2,aPlant);  
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){  
				qtyToBeRecv = rs.getString("QTYTOBERECV");
			}
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return qtyToBeRecv;
	}
	
	public ArrayList getTotalReceiptByProduct(String item, String plant, String fromDate, String toDate)
			throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
				con = DbBean.getConnection();
			String aQuery = "select SUM(ISNULL(RECQTY, 0) * (ISNULL(UNITCOST, 0))) as TOTAL_RECEIPT from " + "[" + plant
					+ "_" + "RECVDET] A JOIN  " + "[" + plant+"_ITEMMST] B ON A.ITEM = B.ITEM  where B.ITEM='"+item+"' AND B.NONSTKFLAG <> 'Y' AND CONVERT(DATETIME, RECVDATE, 103) between '" + fromDate + "' and '" + toDate
					+ "'";

			al = selectData(con, aQuery.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}
	public ArrayList getTotalQtyReceiptByProduct(String item, String plant, String fromDate, String toDate)
			throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = "select SUM(ISNULL(RECQTY, 0)) as TOTAL_RECEIPT from " + "[" + plant
					+ "_" + "RECVDET] A JOIN  " + "[" + plant+"_ITEMMST] B ON A.ITEM = B.ITEM  where B.ITEM='"+item+"' AND B.NONSTKFLAG <> 'Y' AND CONVERT(DATETIME, RECVDATE, 103) between '" + fromDate + "' and '" + toDate
					+ "'";
			
			al = selectData(con, aQuery.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		
		return al;
	}

	/*End code by Abhilash on 02-11-2019*/
	
	public String getTotalReceiptCostByOrder(String pono, String plant)
			throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		String totalCost = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT (a.SHIPPINGCOST+ (a.SHIPPINGCOST* (a.INBOUND_GST/100)) + (SUM(UNITCOST * QTYOR)-(((SUM(UNITCOST * QTYOR)/100)*a.ORDERDISCOUNT))) + ((SUM(UNITCOST * QTYOR)-(((SUM(UNITCOST * QTYOR)/100)*a.ORDERDISCOUNT))) * (a.INBOUND_GST/100))) as TOTALCOST FROM "
					+ "[" + plant+"_POHDR] a join [" + plant+"_PODET] b on a.PONO = b.PONO "
							+ "where a.pono=? and a.plant=? group by a.INBOUND_GST,a.ORDERDISCOUNT,a.SHIPPINGCOST";
			
			this.mLogger.query(this.printQuery, query.toString());

			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,pono);  
			stmt.setString(2,plant);  
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				totalCost = rs.getString("TOTALCOST");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return totalCost;
	}
	
	
    
    public boolean addPoDet(PoDet PoDet) throws Exception {
 		boolean insertFlag = false;
 		boolean flag = false;
 		int HdrId = 0;
 		Connection connection = null;
 		PreparedStatement ps = null;
 	    String query = "";
 		try {	    
 			connection = DbBean.getConnection();
 			query = "INSERT INTO ["+PoDet.getPLANT()+"_PODET]" + 
 					"           ([PLANT]" + 
 					"           ,[PONO]" + 
 					"           ,[POLNNO]" + 
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
 					"           ,[CRBY],[POESTLNNO],[POESTNO],[UKEY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

 			if(connection != null){
	 			ps = connection.prepareStatement(query);
	 			ps.setString(1, PoDet.getPLANT());
	 			ps.setString(2, PoDet.getPONO());
	 			ps.setInt(3, PoDet.getPOLNNO());
	 			ps.setString(4, PoDet.getLNSTAT());
	 			ps.setString(5, PoDet.getITEM());
	 			ps.setString(6, PoDet.getItemDesc());
	 			ps.setString(7, PoDet.getTRANDATE());
	 			ps.setDouble(8, PoDet.getUNITCOST());
	 			ps.setBigDecimal(9, PoDet.getQTYOR());
	 			ps.setBigDecimal(10, PoDet.getQTYRC());
	 			ps.setString(11, PoDet.getUNITMO());
	 			ps.setString(12, PoDet.getUSERFLD1());
	 			ps.setString(13, PoDet.getUSERFLD2());
	 			ps.setString(14, PoDet.getUSERFLD3());
	 			ps.setString(15, PoDet.getUSERFLD4());
	 			ps.setDouble(16, PoDet.getCURRENCYUSEQT());
	 			ps.setDouble(17, PoDet.getPRODGST());
	 			ps.setString(18, PoDet.getPRODUCTDELIVERYDATE());
	 			ps.setString(19, PoDet.getCOMMENT1());
	 			ps.setString(20, PoDet.getCRAT());
	 			ps.setDouble(21, PoDet.getDISCOUNT());
	 			ps.setString(22, PoDet.getDISCOUNT_TYPE());
	 			ps.setString(23, PoDet.getACCOUNT_NAME());
	 			ps.setString(24, PoDet.getTAX_TYPE());
	 			ps.setDouble(25, PoDet.getUNITCOST_AOD());
	 			ps.setString(26, PoDet.getCRBY());
	 			ps.setInt(27, PoDet.getPOESTLNNO());
	 			ps.setString(28, PoDet.getPOESTNO());
	 			ps.setString(29, PoDet.getUKEY());


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
    
	public boolean updatePoDet(List<PoDet> PoDetList) throws Exception {
		boolean updateFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			
			for(PoDet PoDet : PoDetList) {
				query = "UPDATE ["+PoDet.getPLANT()+"_PODET] SET " + 
						"           [PLANT] = ?" + 
						"           ,[PONO] = ?" +  
						"           ,[POLNNO] = ?" +  
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
						"     WHERE PONO = ? AND POLNNO = ? ";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, PoDet.getPLANT());
		 			ps.setString(2, PoDet.getPONO());
		 			ps.setInt(3, PoDet.getPOLNNO());
		 			ps.setString(4, PoDet.getLNSTAT());
		 			ps.setString(5, PoDet.getITEM());
		 			ps.setString(6, PoDet.getItemDesc());
		 			ps.setString(7, PoDet.getTRANDATE());
		 			ps.setDouble(8, PoDet.getUNITCOST());
		 			ps.setBigDecimal(9, PoDet.getQTYOR());
		 			ps.setBigDecimal(10, PoDet.getQTYRC());
		 			ps.setString(11, PoDet.getUNITMO());
		 			ps.setString(12, PoDet.getUSERFLD1());
		 			ps.setString(13, PoDet.getUSERFLD2());
		 			ps.setString(14, PoDet.getUSERFLD3());
		 			ps.setString(15, PoDet.getUSERFLD4());
		 			ps.setDouble(16, PoDet.getCURRENCYUSEQT());
		 			ps.setDouble(17, PoDet.getPRODGST());
		 			ps.setString(18, PoDet.getPRODUCTDELIVERYDATE());
		 			ps.setString(19, PoDet.getCOMMENT1());
		 			ps.setString(20, PoDet.getUPAT());
		 			ps.setString(21, PoDet.getUPBY());
		 			ps.setDouble(22, PoDet.getDISCOUNT());
		 			ps.setString(23, PoDet.getDISCOUNT_TYPE());
		 			ps.setString(24, PoDet.getACCOUNT_NAME());
		 			ps.setString(25, PoDet.getTAX_TYPE());
		 			ps.setString(26, PoDet.getPONO());
		 			ps.setInt(27, PoDet.getPOLNNO());
		 			
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
	
	public boolean updatePoDetpo(PoDet PoDet) throws Exception {
		boolean updateFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			

				query = "UPDATE ["+PoDet.getPLANT()+"_PODET] SET " + 
						"           [PLANT] = ?" + 
						"           ,[PONO] = ?" +  
						"           ,[POLNNO] = ?" +  
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
						"     WHERE PONO = ? AND POLNNO = ? ";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, PoDet.getPLANT());
		 			ps.setString(2, PoDet.getPONO());
		 			ps.setInt(3, PoDet.getPOLNNO());
		 			ps.setString(4, PoDet.getLNSTAT());
		 			ps.setString(5, PoDet.getITEM());
		 			ps.setString(6, PoDet.getItemDesc());
		 			ps.setString(7, PoDet.getTRANDATE());
		 			ps.setDouble(8, PoDet.getUNITCOST());
		 			ps.setBigDecimal(9, PoDet.getQTYOR());
		 			ps.setBigDecimal(10, PoDet.getQTYRC());
		 			ps.setString(11, PoDet.getUNITMO());
		 			ps.setString(12, PoDet.getUSERFLD1());
		 			ps.setString(13, PoDet.getUSERFLD2());
		 			ps.setString(14, PoDet.getUSERFLD3());
		 			ps.setString(15, PoDet.getUSERFLD4());
		 			ps.setDouble(16, PoDet.getCURRENCYUSEQT());
		 			ps.setDouble(17, PoDet.getPRODGST());
		 			ps.setString(18, PoDet.getPRODUCTDELIVERYDATE());
		 			ps.setString(19, PoDet.getCOMMENT1());
		 			ps.setString(20, PoDet.getUPAT());
		 			ps.setString(21, PoDet.getUPBY());
		 			ps.setDouble(22, PoDet.getDISCOUNT());
		 			ps.setString(23, PoDet.getDISCOUNT_TYPE());
		 			ps.setString(24, PoDet.getACCOUNT_NAME());
		 			ps.setString(25, PoDet.getTAX_TYPE());
		 			ps.setString(26, PoDet.getPONO());
		 			ps.setInt(27, PoDet.getPOLNNO());
		 			
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
	
	public boolean updatePoDetpoEdit(PoDet PoDet,int polnno) throws Exception {
		boolean updateFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			

				query = "UPDATE ["+PoDet.getPLANT()+"_PODET] SET " + 
						"           [PLANT] = ?" + 
						"           ,[PONO] = ?" +  
						"           ,[POLNNO] = ?" +  
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
						"           ,[UKEY] = ?" +	
						"     WHERE PONO = ? AND POLNNO = ? ";
				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, PoDet.getPLANT());
		 			ps.setString(2, PoDet.getPONO());
		 			ps.setInt(3, PoDet.getPOLNNO());
		 			ps.setString(4, PoDet.getLNSTAT());
		 			ps.setString(5, PoDet.getITEM());
		 			ps.setString(6, PoDet.getItemDesc());
		 			ps.setString(7, PoDet.getTRANDATE());
		 			ps.setDouble(8, PoDet.getUNITCOST());
		 			ps.setBigDecimal(9, PoDet.getQTYOR());
		 			ps.setBigDecimal(10, PoDet.getQTYRC());
		 			ps.setString(11, PoDet.getUNITMO());
		 			ps.setString(12, PoDet.getUSERFLD1());
		 			ps.setString(13, PoDet.getUSERFLD2());
		 			ps.setString(14, PoDet.getUSERFLD3());
		 			ps.setString(15, PoDet.getUSERFLD4());
		 			ps.setDouble(16, PoDet.getCURRENCYUSEQT());
		 			ps.setDouble(17, PoDet.getPRODGST());
		 			ps.setString(18, PoDet.getPRODUCTDELIVERYDATE());
		 			ps.setString(19, PoDet.getCOMMENT1());
		 			ps.setString(20, PoDet.getUPAT());
		 			ps.setString(21, PoDet.getUPBY());
		 			ps.setDouble(22, PoDet.getDISCOUNT());
		 			ps.setString(23, PoDet.getDISCOUNT_TYPE());
		 			ps.setString(24, PoDet.getACCOUNT_NAME());
		 			ps.setDouble(25, PoDet.getUNITCOST_AOD());
		 			ps.setString(26, PoDet.getTAX_TYPE());
		 			ps.setString(27, PoDet.getUKEY());
		 			ps.setString(28, PoDet.getPONO());
		 			ps.setInt(29, polnno);
		 			
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
	
	public String getTotalAmount(String pono, String plant) throws Exception {
		java.sql.Connection con = null;
		String totalAmount = "0.0";
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ISNULL((SUM(B.QTYOR*B.UNITCOST) + (SUM(B.QTYOR*B.UNITCOST) * (A.INBOUND_GST/100)) + A.SHIPPINGCOST),0) AS AMOUNT FROM "
					+ "[" + plant + "_" + "POHDR" + "] A JOIN "
					+ "[" + plant + "_" + "PODET" + "] B ON A.PONO = B.PONO ");
			sql.append(" WHERE A.PONO = ? AND A.PLANT = ? GROUP BY A.INBOUND_GST, A.SHIPPINGCOST");
			this.mLogger.query(this.printQuery, sql.toString());
			
			PreparedStatement stmt = con.prepareStatement(sql.toString());  
			stmt.setString(1,pono);  
			stmt.setString(2,plant);
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs != null) {
				String val;
				while (rs.next()) {
					totalAmount = rs.getString("AMOUNT").trim();
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
		return totalAmount;
	}
	
	
	public PoDet getPoDetByPonoPllno(String plant, String pono, int poline) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    PoDet poDet=new PoDet();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,PONO,POLNNO,LNSTAT,ISNULL(UKEY,'') AS UKEY,ISNULL(POESTNO,'') AS POESTNO,ISNULL(POESTLNNO,0) AS POESTLNNO,ITEM,ItemDesc,TRANDATE,UNITCOST,QTYOR,QTYRC,UNITMO,USERFLD1,USERFLD2,USERFLD3,USERFLD4,CURRENCYUSEQT,PRODGST,PRODUCTDELIVERYDATE,DISCOUNT,ISNULL(DISCOUNT_TYPE,'') AS DISCOUNT_TYPE,ISNULL(ACCOUNT_NAME,'') AS ACCOUNT_NAME,ISNULL(TAX_TYPE,'') AS TAX_TYPE,ISNULL(COMMENT1,'') AS COMMENT1,ISNULL(UNITCOST_AOD,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST_AOD,CRAT,CRBY,UPAT,UPBY" + 
					" FROM ["+ plant +"_PODET] WHERE PONO='"+pono+"' AND POLNNO='"+poline+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, poDet);
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
		return poDet;
	}
	
	public PoDet getPoDetByPonoItem(String plant, String pono, String item) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    PoDet poDet=new PoDet();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT ID,PLANT,PONO,POLNNO,LNSTAT,ITEM,ItemDesc,ISNULL(UKEY,'') AS UKEY,TRANDATE,ISNULL(UNITCOST,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST,QTYOR,QTYRC,UNITMO,USERFLD1,USERFLD2,USERFLD3,USERFLD4,CURRENCYUSEQT,PRODGST,PRODUCTDELIVERYDATE, CASE WHEN ISNULL(DISCOUNT_TYPE,'0') = '%' THEN DISCOUNT ELSE (DISCOUNT * ISNULL(CURRENCYUSEQT,1))END AS DISCOUNT,ISNULL(DISCOUNT_TYPE,'') AS DISCOUNT_TYPE,ISNULL(ACCOUNT_NAME,'') AS ACCOUNT_NAME,ISNULL(TAX_TYPE,'') AS TAX_TYPE,ISNULL(UNITCOST_AOD,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST_AOD,ISNULL(COMMENT1,'') AS COMMENT1,CRAT,CRBY,UPAT,UPBY,ISNULL(POESTNO,'') AS POESTNO,ISNULL(POESTLNNO,0) AS POESTLNNO" + 
					" FROM ["+ plant +"_PODET] WHERE PONO='"+pono+"' AND ITEM='"+item+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, poDet);
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
		return poDet;
	}
	
	public List<PoDet> getPoDetByPono(String plant, String pono) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<PoDet> podetlist = new ArrayList<PoDet>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,PONO,POLNNO,LNSTAT,ITEM,ITEMDESC,ISNULL(UKEY,'') AS UKEY,ItemDesc,TRANDATE,ISNULL(UNITCOST,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST,QTYOR,QTYRC,UNITMO,USERFLD1,USERFLD2,USERFLD3,USERFLD4,CURRENCYUSEQT,ISNULL(PRODGST,'0') PRODGST,PRODUCTDELIVERYDATE,CASE WHEN ISNULL(DISCOUNT_TYPE,'0') = '%' THEN ISNULL(DISCOUNT,'0') ELSE (ISNULL(DISCOUNT,'0') * ISNULL(CURRENCYUSEQT,1))END AS DISCOUNT,ISNULL(DISCOUNT_TYPE,'') AS DISCOUNT_TYPE,ISNULL(ACCOUNT_NAME,'') AS ACCOUNT_NAME,ISNULL(TAX_TYPE,'') AS TAX_TYPE,ISNULL(COMMENT1,'') AS COMMENT1,ISNULL(UNITCOST_AOD,0)*ISNULL(CURRENCYUSEQT,1) UNITCOST_AOD,CRAT,CRBY,UPAT,UPBY,ISNULL(POESTNO,'') POESTNO,ISNULL(POESTLNNO,0) POESTLNNO" + 
					" FROM ["+ plant +"_PODET] WHERE PONO='"+pono+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   PoDet poDet=new PoDet();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, poDet);
	                   podetlist.add(poDet);
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
	
	public boolean DeletePoDet(String plant, String pono, int poline)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        String sQry = "DELETE FROM " + "[" + plant +"_PODET] WHERE PONO='"+pono+"' AND POLNNO='"+poline+"'";
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
	
	  public boolean addPoDetRemarks(PoDetRemarks poDetRemarks) throws Exception {
	 		boolean insertFlag = false;
	 		boolean flag = false;
	 		int HdrId = 0;
	 		Connection connection = null;
	 		PreparedStatement ps = null;
	 	    String query = "";
	 		try {	    
	 			connection = DbBean.getConnection();
	 			query = "INSERT INTO ["+poDetRemarks.getPLANT()+"_PODET_REMARKS]" + 
	 					"           ([PLANT]" + 
	 					"           ,[PONO]" + 
	 					"           ,[POLNNO]" + 
	 					"           ,[ITEM]" + 
	 					"           ,[REMARKS]" + 
	 					"           ,[CRAT]" +
	 					"           ,[CRBY],[UKEY]) VALUES (?,?,?,?,?,?,?,?)";

	 			if(connection != null){
		 			ps = connection.prepareStatement(query);
		 			ps.setString(1, poDetRemarks.getPLANT());
		 			ps.setString(2, poDetRemarks.getPONO());
		 			ps.setInt(3, poDetRemarks.getPOLNNO());
		 			ps.setString(4, poDetRemarks.getITEM());
		 			ps.setString(5, poDetRemarks.getREMARKS());
		 			ps.setString(6, poDetRemarks.getCRAT());
		 			ps.setString(7, poDetRemarks.getCRBY());
		 			ps.setString(8, poDetRemarks.getUKEY());
		 			
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
	  
	  public boolean updatePoDetRemarks(List<PoDetRemarks> poDetRemarkslist) throws Exception {
			boolean updateFlag = false;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
			try {
				connection = DbBean.getConnection();
				
				for(PoDetRemarks poDetRemarks : poDetRemarkslist) {
					query = "UPDATE ["+poDetRemarks.getPLANT()+"_PODET_REMARKS] SET " + 
							"           [PLANT] = ?" + 
							"           ,[PONO] = ?" +  
							"           ,[POLNNO] = ?" +  
							"           ,[ITEM] = ?" +  
							"           ,[REMARKS] = ?" +   
							"           ,[UPAT] = ?" +  
							"           ,[UPBY] = ?" +  
							"           ,[UKEY] = ?" +  
							"     WHERE PONO = ? AND POLNNO = ? ";
					if(connection != null){
					   ps = connection.prepareStatement(query);
					   ps.setString(1, poDetRemarks.getPLANT());
			 			ps.setString(2, poDetRemarks.getPONO());
			 			ps.setInt(3, poDetRemarks.getPOLNNO());
			 			ps.setString(4, poDetRemarks.getITEM());
			 			ps.setString(5, poDetRemarks.getREMARKS());
			 			ps.setString(6, poDetRemarks.getUPAT());
			 			ps.setString(7, poDetRemarks.getUPBY());
			 			ps.setString(8, poDetRemarks.getUKEY());
			 			ps.setString(9, poDetRemarks.getPONO());
			 			ps.setInt(10, poDetRemarks.getPOLNNO());
			 			
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
	  
	  public boolean updatePoDetRemarksBYID(List<PoDetRemarks> poDetRemarkslist) throws Exception {
			boolean updateFlag = false;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
			try {
				connection = DbBean.getConnection();
				
				for(PoDetRemarks poDetRemarks : poDetRemarkslist) {
					query = "UPDATE ["+poDetRemarks.getPLANT()+"_PODET_REMARKS] SET " + 
							"           [PLANT] = ?" + 
							"           ,[PONO] = ?" +  
							"           ,[POLNNO] = ?" +  
							"           ,[ITEM] = ?" +  
							"           ,[REMARKS] = ?" +   
							"           ,[UPAT] = ?" +  
							"           ,[UPBY] = ?" +  
							"           ,[UKEY] = ?" +  
							"     WHERE ID_REMARKS = ? ";
					if(connection != null){
					   ps = connection.prepareStatement(query);
					   ps.setString(1, poDetRemarks.getPLANT());
			 			ps.setString(2, poDetRemarks.getPONO());
			 			ps.setInt(3, poDetRemarks.getPOLNNO());
			 			ps.setString(4, poDetRemarks.getITEM());
			 			ps.setString(5, poDetRemarks.getREMARKS());
			 			ps.setString(6, poDetRemarks.getUPAT());
			 			ps.setString(7, poDetRemarks.getUPBY());
			 			ps.setString(8, poDetRemarks.getUKEY());
			 			ps.setInt(9, poDetRemarks.getID_REMARKS());
			 			
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
	  
	  public List<PoDetRemarks> getPoDetRemarksByPono(String plant, String pono) throws Exception {
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
		    List<PoDetRemarks> PoDetRemarksist = new ArrayList<PoDetRemarks>();
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT * FROM ["+ plant +"_PODET_REMARKS] WHERE PONO='"+pono+"'";

				if(connection != null){
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   ResultSet rst = ps.executeQuery();
					   while (rst.next()) {
						   PoDetRemarks poDetRemarks=new PoDetRemarks();
		                   ResultSetToObjectMap.loadResultSetIntoObject(rst, poDetRemarks);
		                   PoDetRemarksist.add(poDetRemarks);
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
	  
	  public boolean IsExistPoDetRemarks(String plant, String pono, String ponoln, String item)throws Exception {
			Connection connection = null;
			PreparedStatement ps = null;
			boolean status = false;
		    String query = "";
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT * FROM ["+ plant +"_PODET_REMARKS] WHERE PLANT='"+plant+"' AND PONO='"+pono+"' AND POLNNO='"+ponoln+"' AND ITEM='"+item+"'";

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
	  
	  
	  
	  public  List<PoDetRemarks>  GetPoDetRemarksbyitems(String plant, String pono, String ponoln, String item)throws Exception {
			Connection connection = null;
			PreparedStatement ps = null;
			boolean status = false;
		    String query = "";
		    List<PoDetRemarks> PoDetRemarkslist = new ArrayList<PoDetRemarks>();
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT * FROM ["+ plant +"_PODET_REMARKS] WHERE PLANT='"+plant+"' AND PONO='"+pono+"' AND POLNNO='"+ponoln+"' AND ITEM='"+item+"'";

				if(connection != null){
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   ResultSet rst = ps.executeQuery();
					   while (rst.next()) {
						   PoDetRemarks poDetRemarks=new PoDetRemarks();
		                   ResultSetToObjectMap.loadResultSetIntoObject(rst, poDetRemarks);
		                   PoDetRemarkslist.add(poDetRemarks);
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
			return PoDetRemarkslist;
		}
	  
	  public boolean DeletePoDetRemarks(String plant, int id)
		        throws Exception {
				boolean deletestatus = false;
				PreparedStatement ps = null;
				Connection con = null;
				try {
				        con = DbBean.getConnection();
				        
				        
				        String sQry = "DELETE FROM " + "[" + plant +"_PODET_REMARKS]"
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
	  
	  
		public ArrayList selectProductreceiveDet(String query, Hashtable ht, String extCond,
				String plant) throws Exception {
			ArrayList alData = new ArrayList();
			java.sql.Connection con = null;
			
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
					+ plant + "_" + "PRODUCTRECEIVEDET" + "] a join ["+plant+"_PRODUCTRECEIVEHDR] b on a.RECEIVEHDRID=b.ID");
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
				TABLE_NAME = "PRODUCTRECEIVEDET";
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
		
		public ArrayList selectProductreceiveDet(String query, Hashtable ht) throws Exception {
			ArrayList alData = new ArrayList();
			java.sql.Connection con = null;
			TABLE_NAME = "PRODUCTRECEIVEDET";
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+"["+ht.get(IDBConstants.PLANT)+"_"+ TABLE_NAME+"] D");
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
				TABLE_NAME = "PRODUCTRECEIVEDET";
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
		
		public Map selectProductReceiceRow(String query, Hashtable ht) throws Exception {
			Map map = new HashMap();

			java.sql.Connection con = null;
			try {
				TABLE_NAME="PRODUCTRECEIVEDET";
				con = com.track.gates.DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
						+"["+ht.get(IDBConstants.PLANT)+"_PRODUCTRECEIVEDET]");
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
		
		public boolean updateProductReceive(String query, Hashtable htCondition, String extCond)
				throws Exception {
			boolean flag = false;

			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" UPDATE " + "[" + htCondition.get("PLANT") + "_"+ "PRODUCTRECEIVEDET" + "]");
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
		
		public boolean isExisitProductReceive(Hashtable ht, String extCond) throws Exception {
            boolean flag = false;
            java.sql.Connection con = null;
            try {
                    con = com.track.gates.DbBean.getConnection();

                    StringBuffer sql = new StringBuffer(" SELECT ");
                    sql.append("COUNT(*) ");
                    sql.append(" ");
                    sql.append(" FROM "  + "[" + ht.get("PLANT") + "_"+ "PRODUCTRECEIVEDET" + "]");
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
	       
	  
	  
	  
	
}
