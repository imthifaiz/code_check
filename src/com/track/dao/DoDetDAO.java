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
import com.track.db.object.DoDetRemarks;
import com.track.db.object.DoHdr;
import com.track.db.object.HrDeductionHdr;
import com.track.db.object.HrEmpType;
import com.track.db.util.PlantMstUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils; 

public class DoDetDAO extends BaseDAO {

	StrUtils _StrUtils = null;
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.DODETDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.DODETDAO_PRINTPLANTMASTERLOG;
	
	public static String TABLE_DET = "DODET";

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

	public DoDetDAO() {
		_StrUtils = new StrUtils();
	}

	public static String plant = "";
	public static String TABLE_NAME = "DODET";

	public DoDetDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "DODET" + "]";
	}

	public ArrayList getDoHdrInfo(String SupplierID, String receiptDate,
			String plant) throws Exception {
		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			boolean flag = false;
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("PONO,PACKSLIP,COMMENT1 ");
			sql.append(" ");
			sql.append(" FROM POHDR");
			sql.append(" WHERE vendno = '" + SupplierID + "' ");
			sql.append(" AND orddate = '" + receiptDate + "' ");
			sql.append(" AND  PLANT = '" + plant + "' ");
			sql.append(" AND  LNSTAT <> 'C'");
			this.mLogger.query(this.printQuery, sql.toString());
			al = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return al;
	}

	

	public boolean isExisit(String query) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			this.mLogger.query(this.printQuery, query);
			flag = isExists(con, query);
		} catch (Exception e) {
			this.mLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "DODET" + "]");
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
	public ArrayList selectdnplDoDet(String query, Hashtable ht, String extCond) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;


		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ (ht.get("PLANT") == null ? ht.get("A.PLANT") : ht.get("PLANT")) + "_" + "SHIPHIS" + "] A INNER JOIN [" + 
				(ht.get("PLANT") == null ? ht.get("A.PLANT") : ht.get("PLANT")) + "_" + "DODET" + "] B ON A.DONO = B.DONO AND A.DOLNO = B.DOLNNO ");
		
				
		String conditon = "";
		try {
			con = com.track.gates.DbBean.getConnection();
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = "a.plant='" + (ht.get("PLANT") == null ? ht.get("A.PLANT") : ht.get("PLANT")) + "' and a.dono ='"
						+ ht.get("A.DONO") + "' and a.INVOICENO = '" + ht.get("INVOICENO") + "'";
				conditon = formCondition(ht);
				sql.append(conditon);
				if (extCond.length() > 0)
					sql.append(" and " + extCond);
			}else {
				if (extCond.length() > 0)
					sql.append(" WHERE " + extCond);
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

	
	public ArrayList selectdnplDoDet(String query, Hashtable ht) throws Exception {
		return selectdnplDoDet(query, ht, "");
	}
	
	
	

	public ArrayList selectDoDet(String query, Hashtable ht) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;


		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DODET" + "]");


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

	public ArrayList selectDoDet(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DODET" + "] a ");
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
	public ArrayList selectDoDetForPage(String query, Hashtable ht, String extCond)
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

	public ArrayList selectDoDetForIssue(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "SHIPHIS" + "] c ");
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

	public ArrayList selectReverseDoDet(String query, Hashtable ht,
			String extCond, String plant) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ plant + "_" + "DODET" + "] a,[" + plant + "_" + "SHIPHIS"
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

	public boolean insertDoDet(Hashtable ht) throws Exception {
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
					+ "DODET" + "]" + "("
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
        
    public boolean isExisit(Hashtable ht) throws Exception {
            this.mLogger.log(1, this.getClass() + " isExisit()");
            boolean flag = false;
            java.sql.Connection con = null;
            try {
                    con = com.track.gates.DbBean.getConnection();
                    StringBuffer sql = new StringBuffer(" SELECT ");
                    sql.append("COUNT(*) ");
                    sql.append(" ");
                    sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "DODET" + "]");
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

	public boolean insertShipHis(Hashtable ht) throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		String TABLE = "SHIPHIS";
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_" + TABLE
					+ "]" + "(" + FIELDS.substring(0, FIELDS.length() - 1)
					+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1)
					+ ")";
			this.mLogger.query(this.printQuery, query);
			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Item already added to shiphis");

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
		String TABLE = "DODET";
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

	public boolean updateShipHis(String query, Hashtable htCondition,
			String extCond) throws Exception {
		boolean flag = false;
		String TABLE = "SHIPHIS";
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
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return flag;

	}

	public boolean isExisit(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		String TABLE = "DODET";
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE + "]");
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

	public ArrayList getOutBoundOrderDetailsByWMS(String plant, String orderno)
			throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			StringBuffer sQry = new StringBuffer(
					"select  distinct a.dono,isnull(a.userfld3,'') as custname,");
			sQry
					.append(" isnull(name,'') contactname,isnull(b.telno,'') as telno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3 ");
			sQry.append(" from  " + "[" + plant + "_" + "dodet" + "]" + " a,"
					+ "[" + plant + "_" + "custmst" + "]" + " b");
			sQry
					.append(" where a.plant='"
							+ plant
							+ "' and a.dono like '"
							+ orderno
							+ "%' and a.pickstatus <> 'C' and a.LNSTAT <> 'C' and a.userfld3=b.cname");
			this.mLogger.query(this.printQuery, sQry.toString());
			al = selectData(con, sQry.toString());
			TABLE_NAME = "DODET";
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
	

	public ArrayList getOutBoundPickingItemDetailsByWMS(String plant,
			String orderno, String item) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			StringBuffer sQry = new StringBuffer(
					"select  distinct item,itemdesc,unitmo,isnull(qtyor,0) as qtyor,isnull(qtypick,0) as qtypick,isnull(userfld2,'') as ref");
			sQry.append(" from  " + "[" + plant + "_" + "dodet" + "]" + "");
			sQry.append(" where plant='" + plant + "' and dono = '" + orderno
					+ "' and item like '" + item + "%'and LNSTAT <> 'C'");

			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "DODET";
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
	

	public ArrayList getOutBoundReverseCheckItemListByWMS(String plant,
			String orderno, String orderlnno, String item) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			StringBuffer sQry = new StringBuffer(
					"select  distinct isnull(qtypick,0) as qtypick");
			sQry.append(" from  " + "[" + plant + "_" + "dodet" + "]" + "");
			sQry.append(" where plant='" + plant + "' and dono = '" + orderno
					+ "' and item like '" + item + "' and dolnno='" + orderlnno
					+ "'" + " and pickstatus <> 'C' and LNSTAT <> 'C'");

			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "DODET";
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

	public ArrayList getOutBoundOrderIssueDetailsByWMS(String plant,
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
			TABLE_NAME = "DODET";
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

	public boolean insertPickDet(Hashtable ht) throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			String TABLE = "SHIPHIS";
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
			TABLE_NAME = "SHIPHIS";
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

	public ArrayList getIssueShipHisDetailByWMS(String plant, String orderno,
			String lineno, String item) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			StringBuffer sQry = new StringBuffer(
					"select  loc,batch,sum(pickqty)pickqty ");
			sQry.append(" from  " + "[" + plant + "_" + "shiphis" + "]" + "");
			sQry
					.append(" where plant='"
							+ plant
							+ "' and dono = '"
							+ orderno
							+ "' and dolno = '"
							+ lineno
							+ "'and item = '"
							+ item
							+ "' AND isnull(status,'')<>'C'  group by dono,dolno,item,loc,batch ");
					
			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "SHIPHIS";
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

	public ArrayList getOBReverseDetailsByWMS(String plant, String orderno,
			String lineno, String itemno) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {

			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select loc,batch from  " + "[" + plant + "_"
					+ "shiphis" + "]" + " where plant='" + plant
					+ "'and dono = '" + orderno + "'  and dolno = '" + lineno
					+ "' and item = '" + itemno + "' and loc ='SHIPPINGAREA'";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);
			TABLE_NAME = "SHIPHIS";

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

	public ArrayList selectDoDetOShipping(String query, Hashtable ht,
			String extCond) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("plant") + "_" + "DODET" + "]");
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

	public boolean delete(java.util.Hashtable ht) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_DODET]");
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

	public Boolean removeOrderDetails(String plant2, String dono)
			throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant2 + "_" + "DODET] WHERE DONO='"
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
	
	public int getCountDoNo(Hashtable ht, String extCon) throws Exception {
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
					+ ht.get("PLANT") + "_DOHDR]" + " WHERE "
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
	
	public int getMaxDoLnNo(Hashtable ht) throws Exception {
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
			String sQry = "SELECT MAX(DOLNNO) FROM " + "["
					+ ht.get("PLANT") + "_DODET]" + " WHERE "
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
	
	public String getCheckInSummaryItem(String plant, String aClass) {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();
	
		
			String query = " SELECT DISTINCT ITEM  FROM [" + plant + "_"
			+ "DODET] WHERE PLANT='" + plant
			+ "' AND ITEM = '" + aClass + "' AND LNSTAT='N'  ORDER BY ITEM"; 

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
	
	public List queryClassMst(String plant, String aClass) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List listQty = new ArrayList();
		Connection con = null;
		
		try {

			con = DbBean.getConnection();
			String sCondition = " WHERE DONO IN (SELECT DONO FROM" + "["+ plant + "_" + "DOHDR " + "] WHERE ORDERTYPE ='Mobile Registration'  AND STATUS <> 'O' ) AND ITEM LIKE '"
				+ aClass.toUpperCase() 
				//+ "%' AND LNSTAT = 'N' ";
				+ "%' ";
			String sQry = "SELECT DISTINCT ITEM,ITEMDESC FROM "
					+ "["
					+ plant
					+ "_"
					+ "DODET "
					+ "]  "
					+ sCondition
				    + " ORDER BY ITEM ";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Vector lineVec = new Vector();
				lineVec.add(0, StrUtils.fString((String) rs.getString("ITEM")));
				lineVec.add(1, StrUtils.fString((String) rs.getString("ITEMDESC")));
				listQty.add(lineVec);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQty;
	}
	
	public List queryAttendan(String plant, String aAttendan) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List listQty = new ArrayList();
		Connection con = null;
		
		try {

			con = DbBean.getConnection();
			String sCondition1 = " WHERE ORDERTYPE ='Mobile Registration' AND CUSTNAME LIKE '"
				+ aAttendan.toUpperCase() 
				+ "%' OR  CUSTCODE LIKE '"
				+ aAttendan.toUpperCase() 
				+ "%' ";
			
			String sCondition2 = " WHERE  ORDERTYPE ='Mobile Registration' AND CUSTNAME LIKE '"
				+ aAttendan.toUpperCase() 
				+ "%' AND  CUSTCODE LIKE '"
				+ aAttendan.toUpperCase() 
				+ "%' ";
			String sQry="";
			if(!aAttendan.equals(""))
			{
				 sQry = "SELECT DISTINCT  CUSTCODE,CUSTNAME FROM "
					+ "["
					+ plant
					+ "_"
					+ "DOHDR "
					+ "]  "
					+ sCondition1
				    + " ORDER BY CUSTCODE ";
			}
			else
			{   
				sQry = "SELECT DISTINCT  CUSTCODE,CUSTNAME FROM "
					+ "["
					+ plant
					+ "_"
					+ "DOHDR "
					+ "]  "
					+ sCondition2
				    + " ORDER BY CUSTCODE ";
			}
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Vector lineVec = new Vector();
				lineVec.add(0, StrUtils.fString((String) rs.getString("CUSTCODE")));
				lineVec.add(1, StrUtils.fString((String) rs.getString("CUSTNAME")));
				listQty.add(lineVec);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQty;
	}
	
	
	public ArrayList selectClassDetails(String Plant, String ClassId, String Status)
	throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		
		String strStatus="";
		String trantype="";
		if (Status.equalsIgnoreCase("Registered"))
		{
			strStatus="N";
			trantype=" IS NULL";
		}
		else if (Status.equalsIgnoreCase("Attended"))
		{
			strStatus="C";
			trantype="= '"+"Attendance"+"'";
		}
		else if (Status.equalsIgnoreCase("ClockIn"))
		{
			strStatus="O";
			trantype="= '"+"Clockin"+"'";
		}
		else if (Status.equalsIgnoreCase("ClockOut"))
		{
			strStatus="C";
			trantype="= '"+"Clockin"+"'";
		}
		else 
		{
			strStatus="";
		}
	
		
		String sCondition="";
		if(!strStatus.equals(""))
		{
			
			sCondition = " WHERE B.ITEM = '" + ClassId + "'  AND B.LNSTAT = '" + strStatus + "'"+" AND B.TRANTYPE "+trantype+" AND A.DONO=B.DONO AND A.ORDERTYPE='Mobile Registration' ";
		}
		else
		{
			
			 sCondition = " WHERE B.ITEM = '" + ClassId + "'  AND A.DONO=B.DONO AND A.ORDERTYPE='Mobile Registration'" ;
		}
		String sql = "SELECT A.DONO, A.CUSTCODE,A.CUSTNAME,B.LNSTAT,B.TRANTYPE from "
			+ "["
			+ Plant
			+ "_"
			+ "DOHDR "
			+ "] A, "
			+ "["
			+ Plant
			+ "_"
			+ "DODET "
			+ "] B "
			+ sCondition
		    + " ORDER BY A.CUSTNAME,LNSTAT ";
				
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

 System.out.println("dodet query"+sql.toString());          
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
	
	
	public ArrayList selectAttendanDetails(String Plant, String Attendan)
	throws Exception {
		
		ArrayList alData = new ArrayList();
		
	
		
		java.sql.Connection con = null;
		
		String strStatus="";
		
		
		
		String sCondition1="";
		String sCondition2="";
		
		
		//Edited in the query trantype
		//By CustomerName
		sCondition1 = " WHERE A.CUSTNAME = '" + Attendan + "'  AND A.DONO=B.DONO AND A.ORDERTYPE='Mobile Registration' ";
				
		String sql1 = "SELECT A.DONO, A.CUSTCODE,A.CUSTNAME,B.LNSTAT,B.TRANTYPE from "
			+ "["
			+ Plant
			+ "_"
			+ "DOHDR "
			+ "] A, "
			+ "["
			+ Plant
			+ "_"
			+ "DODET "
			+ "] B "
			+ sCondition1
		    + " ORDER BY A.CUSTNAME,LNSTAT ";
		
		//By CustomerId
		sCondition2 = " WHERE A.CUSTCODE = '" + Attendan + "'  AND A.DONO=B.DONO AND A.ORDERTYPE='Mobile Registration' ";
		
		String sql2 = "SELECT A.DONO, A.CUSTCODE,A.CUSTNAME,B.LNSTAT,B.TRANTYPE from "
			+ "["
			+ Plant
			+ "_"
			+ "DOHDR "
			+ "] A, "
			+ "["
			+ Plant
			+ "_"
			+ "DODET "
			+ "] B "
			+ sCondition2
		    + " ORDER BY A.CUSTNAME,LNSTAT ";
				
		
		try {
			con = com.track.gates.DbBean.getConnection();

			
           
			alData = selectData(con, sql1.toString());
			if(alData.size()<=0)
			{
				alData.clear();
				alData = selectData(con, sql2.toString());
			}
			
			
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
	
	
	 public ArrayList getDODetDetails(String Plant,
				String DONO,String item) {
		 
			ArrayList al = null;
			Connection con = null;
			PlantMstUtil _PlantMstUtil = new PlantMstUtil();
			try {
				con = DbBean.getConnection();
				String query = "select dono,dolnno, lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,"
						+ " isnull(qtyPick,0) as qtyPick , (select SUM(isnull(qtyor,0)- isnull(qtyPick,0))  from "
						+ " " +Plant+"_DODET where DONO=a.dono and ITEM =a.item)  totBal from "
						+ " " +Plant+"_DODET a where dono = '"+DONO+"' and plant <> '' and PickStatus <>'C' " 
					    + " and ITEM = '"+item+"' order by dolnno";

				this.setmLogger(mLogger);
				this.mLogger.query(this.printQuery, query.toString());
				al = selectData(con, query.toString());
                System.out.println("query......"+query);
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

/*
//Start code by Bruhan for ouboundorder pick issue(random) on 8 Apr 2013 
	 public boolean isExisittemp(Hashtable ht) throws Exception {
         this.mLogger.log(1, this.getClass() + " isExisit()");
         boolean flag = false;
         java.sql.Connection con = null;
         try {
                 con = com.track.gates.DbBean.getConnection();
                 StringBuffer sql = new StringBuffer(" SELECT ");
                 sql.append("COUNT(*) ");
                 sql.append(" ");
                 sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "OB_RANDOM_SCAN_TEMP" + "]");
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
	 
	 public boolean updatetemptable(String query, Hashtable htCondition,
				String extCond) throws Exception {
			boolean flag = false;
			String TABLE = "OB_RANDOM_SCAN_TEMP";
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
				throw e;
			} finally {
				DbBean.closeConnection(con);
			}
			return flag;

		}
	 
	 public boolean inserttemp(Hashtable ht) throws Exception {
			boolean insertFlag = false;
			java.sql.Connection conn = null;
			try {
				String TABLE = "OB_RANDOM_SCAN_TEMP";
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
				TABLE_NAME = "OB_RANDOM_SCAN_TEMP";
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
	 
	 public ArrayList selecttemp(String query,Hashtable ht,String extCond)
		throws Exception {
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;

	StringBuffer sql = new StringBuffer(" SELECT"+query+"from " + "["
			+ ht.get("PLANT") + "_" + "OB_RANDOM_SCAN_TEMP" + "]");
	String conditon = "";

	try {
		con = com.track.gates.DbBean.getConnection();
		if (ht.size() > 0) {
			sql.append(" WHERE ");
			conditon = formCondition(ht);
			sql.append(conditon);
			}
		if (extCond.length() != 0) {
			sql.append(extCond);
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
	 
 public boolean deletetemp(Hashtable ht) throws Exception {
			boolean delete = false;
			java.sql.Connection con = null;
			try {
				con = DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" DELETE ");
				sql.append(" ");
				sql.append(" FROM " + "[" + ht.get("PLANT") + "_OB_RANDOM_SCAN_TEMP]");
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

	 
//End code by Bruhan for ouboundorder pick issue(random) on 8 Apr 2013
*/

	 
	 public ArrayList getDODetDetailsRandom(String Plant,
				String DONO,String item) {
		 
			ArrayList al = null;
			Connection con = null;
			PlantMstUtil _PlantMstUtil = new PlantMstUtil();
			try {
				con = DbBean.getConnection();
		
				String query = "select dono,dolnno, lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,"
					+ " isnull(qtyPick,0) as qtyPick , (isnull(qtyor,0)- isnull(qtyPick,0)) totBal "
					+ " from "
					+ " " +Plant+"_DODET a where dono = '"+DONO+"' and plant <> '' and PickStatus <>'C' " 
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
	 
	 public ArrayList getDODetDetailsIssueRandom(String Plant,
				String DONO,String item) {
		 
			ArrayList al = null;
			Connection con = null;
			PlantMstUtil _PlantMstUtil = new PlantMstUtil();
			try {
				con = DbBean.getConnection();
		
				String query = "select dono,dolnno, lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,"
					+ " isnull(qtyPick,0) as qtyPick , (isnull(qtyor,0)- isnull(qtyPick,0)) totBal "
					+ " from "
					+ " " +Plant+"_DODET a where dono = '"+DONO+"' and plant <> '' and lnstat <>'C' " 
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
	 
	 
	 
	public ArrayList selectPickItemDetails(String Plant, String Dono,String Item)
		throws Exception {
			
			ArrayList alData = new ArrayList();
			java.sql.Connection con = null;
			String sCondition1="";
			sCondition1 = " WHERE DONO = '" + Dono + "'  AND ITEM='" + Item + "' AND PICKSTATUS <> 'C'";
			String sql = "SELECT DISTINCT(ITEM) ITEM,ISNULL(ITEMDESC,'') ITEMDESC,ISNULL(UNITMO,'') UOM from "
				+ "["
				+ Plant
				+ "_"
				+ "DODET "
				+ "]  "
				+ sCondition1;
			   // + " ORDER BY ITEM ";
			
			try {
				con = com.track.gates.DbBean.getConnection();
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_DODET_REMARKS]" + "("
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
	
	public ArrayList selectDoMultiRemarks(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DODET_REMARKS" + "] a ");
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
	 public boolean deleteDoMultiRemarks(Hashtable ht) throws Exception {
         boolean delete = false;
         java.sql.Connection con = null;
         try {
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer(" DELETE ");
                 sql.append(" ");
                 sql.append(" FROM " + "[" + ht.get("PLANT") + "_DODET_REMARKS]");
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
	 

	 public boolean updateDoMultiRemarks(String query, Hashtable htCondition, String extCond,
				String aPlant) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" UPDATE " + "[" + aPlant + "_"
						+ "DODET_REMARKS" + "]");
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
	
	 
	  public String getMultiRemarks(String aPlant, String doNO,String dolnno,String aItem) throws Exception {
	      java.sql.Connection con = null;
	      String rermarks = "";
	      try {
	              
	              con = DbBean.getConnection();
	     

	      StringBuffer SqlQuery = new StringBuffer(" select isnull(remarks,'') remarks  ");
	      SqlQuery.append(" from "+aPlant+"_dodet_remarks where item='"+aItem+"' and dono ='"+doNO+"' and dolnno ='"+dolnno+"' ");

	      
	      System.out.println(SqlQuery.toString());
	          Map m = this.getRowOfData(con, SqlQuery.toString());

	          rermarks = (String) m.get("remarks");

	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return rermarks;
	  }

	  public List<DoDetRemarks> getDoDetRemarksByDono(String plant, String dono) throws Exception {
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
		    List<DoDetRemarks> doDetRemarksist = new ArrayList<DoDetRemarks>();
			try {	    
				connection = DbBean.getConnection();
				query = "SELECT * FROM ["+ plant +"_DODET_REMARKS] WHERE DONO='"+dono+"'";

				if(connection != null){
					   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					   ResultSet rst = ps.executeQuery();
					   while (rst.next()) {
						   DoDetRemarks doDetRemarks=new DoDetRemarks();
		                   ResultSetToObjectMap.loadResultSetIntoObject(rst, doDetRemarks);
		                   doDetRemarksist.add(doDetRemarks);
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
			return doDetRemarksist;
		}
		
 public boolean isExisitDoMultiRemarks(Hashtable ht) throws Exception {
          this.mLogger.log(1, this.getClass() + " isExisit()");
          boolean flag = false;
          java.sql.Connection con = null;
          try {
                  con = com.track.gates.DbBean.getConnection();
                  StringBuffer sql = new StringBuffer(" SELECT ");
                  sql.append("COUNT(*) ");
                  sql.append(" ");
                  sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "DODET_REMARKS" + "]");
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
public boolean insertDoDetTemp(Hashtable ht) throws Exception {
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
					+ "DODET_TEMP" + "]" + "("
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
 public ArrayList selectDoDetTemp(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DODET_TEMP" + "] a ");
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
 
 public String getTempDono(String key, String plant)
			throws Exception {
		
		String selectStr = "";
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			Statement stmt = con.createStatement();
			String q;
			String scondn=" where item='"+key+"' and itemdesc='NEWDO'";
				q = "select isnull(dono,'') dono,isnull(item,'') item from " + "[" + plant
						+ "_dodet_temp" + "]";
				if(key!=null)
					q=q+scondn;
			System.out.println("query"+q);
			this.mLogger.query(printQuery, q);
			ResultSet rs = stmt.executeQuery(q);
		
			if (rs != null) {
				String val;
				while (rs.next()) {
					val = rs.getString("dono").trim()+"^"+rs.getString("item").trim();
					selectStr=val;
				}
			}
		} // End of try
		catch (Exception e) {
			DbBean.writeError("DODetDAO", "getTempDono()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return selectStr;
 }

 

public boolean isExisitTempDono(Hashtable ht, String extCond) throws Exception {
	boolean flag = false;
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();

		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append("COUNT(*) ");
		sql.append(" ");
		sql.append(" FROM " + "["+ht.get("PLANT")+"_DODET_TEMP]");
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

public boolean deleteTempDono(java.util.Hashtable ht,String extCond) throws Exception {
	boolean delete = false;
	java.sql.Connection con = null;
	try {
		con = DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" DELETE ");
		sql.append(" ");
		sql.append(" FROM " + "[" + ht.get("PLANT") + "_DODET_TEMP]");
		sql.append(" WHERE " + formCondition(ht));
		if (extCond.length() > 0)
			sql.append(" and " + extCond);
		this.mLogger.query(this.printQuery, sql.toString());
		System.out.println("Delete: "+ sql.toString());
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

public String getDono(String key, String plant)
		throws Exception {
	
	String selectStr = "";
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();
		Statement stmt = con.createStatement();
		String q;
		String scondn=" where dono='"+key+"' ";
			q = "select distinct isnull(dono,'') dono from " + "[" + plant
					+ "_dodet" + "]";
			if(key!=null)
				q=q+scondn;
		System.out.println("query"+q);
		this.mLogger.query(printQuery, q);
		ResultSet rs = stmt.executeQuery(q);
	
		if (rs != null) {
			String val;
			while (rs.next()) {
				val = rs.getString("dono").trim();
				selectStr=val;
			}
		}
	} // End of try
	catch (Exception e) {
		DbBean.writeError("DODetDAO", "getDono()", e);
	} finally {
		DbBean.closeConnection(con);
	}
	return selectStr;
}

public List selectRemarks(String query, Hashtable ht) throws Exception {
	List remarksList = new ArrayList();

	java.sql.Connection con = null;
	try {

		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
				+ "[" + ht.get("PLANT") + "_" + "DODET_REMARKS" + "]");
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
    public ArrayList getDODetDetailsRandomMultiUOM(String Plant,
			String DONO,String item,String uom) {
	 
		ArrayList al = null;
		Connection con = null;
		PlantMstUtil _PlantMstUtil = new PlantMstUtil();
		try {
			con = DbBean.getConnection();
	
			String query = "select dono,dolnno, lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,"
				+ " isnull(qtyPick,0) as qtyPick , (isnull(qtyor,0)- isnull(qtyPick,0)) totBal "
				+ " from "
				+ " " +Plant+"_DODET a where dono = '"+DONO+"' and plant <> '' and PickStatus <>'C' " 
			    + " and ITEM = '"+item+"'and UNITMO = '"+uom+"' order by dolnno";

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
    public ArrayList getDoUOMByOrder(String Plant,String user,String DONO,String item) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
				con = DbBean.getConnection();			
			String query = "select UNITMO as uom,ISNULL(QPUOM,1) as uomqty"				
				+ " from "
				+ " " +Plant+"_DODET a join " +Plant+"_UOM u on u.UOM=a.UNITMO where dono = '"+DONO+"' and a.plant <> '' and LNSTAT <>'C' " 
			    + " and ITEM = '"+item+"'  order by dolnno";

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
	public String getComittedStockByProduct(String aItem, String aPlant)
			throws Exception {
		java.sql.Connection con = null;
		String comittedStock = "";
		try {
			con = com.track.gates.DbBean.getConnection();
			String query = "SELECT ISNULL(((ISNULL(SUM(QTYOR),0)-ISNULL(SUM(QtyPick),0))* ISNULL((select u.QPUOM from ["+aPlant+"_UOM] u where u.UOM=B.INVENTORYUOM),1)),0) AS COMITTEDSTOCK FROM ["+aPlant+"_DODET] A JOIN ["+aPlant+"_ITEMMST] B ON A.ITEM=B.ITEM WHERE A.ITEM=? AND LNSTAT <> 'C' AND A.ITEM IN (SELECT ITEM FROM "+aPlant+"_ITEMMST WHERE NONSTKFLAG <> 'Y') AND A.PLANT=? GROUP BY B.INVENTORYUOM";
			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,aItem);  
			stmt.setString(2,aPlant);  
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){  
				comittedStock = rs.getString("COMITTEDSTOCK");
			}
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return comittedStock;
	}
	
	public String getQtyToBeShpdByProduct(String aItem, String aPlant)
			throws Exception {
		java.sql.Connection con = null;
		String qtyToBeShpd = "";
		try {
			con = com.track.gates.DbBean.getConnection();
			String query = "SELECT (ISNULL(SUM(QtyPick),0)-ISNULL(SUM(QTYIS),0))* ISNULL((select u.QPUOM from ["+aPlant+"_UOM] u where u.UOM=B.INVENTORYUOM),1) AS QTYTOBESHIPPED FROM ["+aPlant+"_DODET] A JOIN ["+aPlant+"_ITEMMST] B ON A.ITEM=B.ITEM WHERE A.ITEM=? AND A.ITEM IN (SELECT ITEM FROM ["+aPlant+"_ITEMMST] WHERE NONSTKFLAG <> 'Y') AND LNSTAT <> 'C' AND A.PLANT=? GROUP BY A.ITEM, B.INVENTORYUOM";
			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,aItem);  
			stmt.setString(2,aPlant);  
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){  
				qtyToBeShpd = rs.getString("QTYTOBESHIPPED");
			}
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return qtyToBeShpd;
	}
	
	public ArrayList getTotalIssueByProduct(String item, String plant, String fromDate, String toDate)
			throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = "select SUM(ISNULL(PICKQTY, 0) * (ISNULL(UNITPRICE, 0))) as TOTAL_ISSUE from " + "[" + plant
					+ "_" + "SHIPHIS] where ITEM='"+item+"' AND CONVERT(DATETIME, ISSUEDATE, 103) between '" + fromDate + "' and '" + toDate
					+ "'";
			al = selectData(con, aQuery.toString());
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
	public ArrayList getTotalIssueQtyByProduct(String item, String plant, String fromDate, String toDate)
			throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = "select SUM(ISNULL(PICKQTY, 0)) as TOTAL_ISSUE from " + "[" + plant
					+ "_" + "SHIPHIS] where ITEM='"+item+"' AND CONVERT(DATETIME, ISSUEDATE, 103) between '" + fromDate + "' and '" + toDate
					+ "'";
			al = selectData(con, aQuery.toString());
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
	/*End code by Abhilash on 02-11-2019*/
	public String getTotalReceiptCostByOrder(String dono, String plant)
			throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		String totalCost = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT (SHIPPINGCOST + (SHIPPINGCOST * (a.OUTBOUND_GST/100)) + (SUM(UNITPRICE * QTYOR)-(((SUM(UNITPRICE * QTYOR)/100)*a.ORDERDISCOUNT))) + ((SUM(UNITPRICE * QTYOR)-(((SUM(UNITPRICE * QTYOR)/100)*a.ORDERDISCOUNT))) * (a.OUTBOUND_GST/100))) as TOTALCOST FROM "
					+ "[" + plant+"_DOHDR] a join [" + plant+"_DODET] b on a.DONO = b.DONO "
							+ "where a.dono=? and a.plant=? group by a.OUTBOUND_GST,a.SHIPPINGCOST,a.ORDERDISCOUNT";
			
			this.mLogger.query(this.printQuery, query);

			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,dono);  
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
	public ArrayList getIssueShipHisDetailWMS(String plant, String orderno,
			String lineno, String item) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			StringBuffer sQry = new StringBuffer(
					"select  ISSUEDATE,loc,batch,sum(pickqty)pickqty ");
			sQry.append(" from  " + "[" + plant + "_" + "shiphis" + "]" + "");
			sQry
					.append(" where plant='"
							+ plant
							+ "' and dono = '"
							+ orderno
							+ "' and dolno = '"
							+ lineno
							+ "'and item = '"
							+ item
							+ "' AND isnull(status,'')<>'C'  group by dono,dolno,item,loc,batch,ISSUEDATE ");
					
			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "SHIPHIS";
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
	public boolean insertGINOtoInvoice(Hashtable ht) throws Exception {

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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_FINGINOTOINVOICE"
					+ "]" + "("
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
	public boolean updateGINOtoInvoice(String query, Hashtable htCondition, String extCond,
			String aPlant) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "[" + aPlant + "_"
					+ "FINGINOTOINVOICE" + "]");
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
	public ArrayList selectgitoinvoice(String query, Hashtable ht, String extCond)
			throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		
		String plant = (String) ht.get("PLANT");

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ plant + "_" + "FINGINOTOINVOICE" + "] A ");
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
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;
	}
	
	public ArrayList selectginofromshiphis(String query, Hashtable ht, String extCond)
			throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		
		String plant = (String) ht.get("PLANT");

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ plant + "_" + "SHIPHIS" + "] A ");
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
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;
	}
	
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
						"           ,[DISCOUNT_TYPE]" +
						"           ,[UNITCOST]" +
						"           ,[ADDONAMOUNT]" +
						"           ,[ADDONTYPE]" +
						"           ,[UKEY])" +
						"     VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
				   ps.setDouble(28, doDet.getUNITCOST());
				   ps.setDouble(29, doDet.getADDONAMOUNT());
				   ps.setString(30, doDet.getADDONTYPE());
				   ps.setString(31, doDet.getUKEY());
				  
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
	
	public boolean addDoDet(DoDet doDet) throws Exception {
		boolean insertFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			
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
						"           ,[DISCOUNT_TYPE]" +
						"           ,[UNITCOST]" +
						"           ,[ADDONAMOUNT]" +
						"           ,[ADDONTYPE]" +
						"           ,[UKEY])" +
						"     VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
				   ps.setDouble(28, doDet.getUNITCOST());
				   ps.setDouble(29, doDet.getADDONAMOUNT());
				   ps.setString(30, doDet.getADDONTYPE());
				   ps.setString(31, doDet.getUKEY());
				  
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
	
	public String getTotalAmount(String dono, String plant) throws Exception {
		java.sql.Connection con = null;
		String totalAmount = "0.0";
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ISNULL((SUM(B.QTYOR*B.UNITPRICE) + (SUM(B.QTYOR*B.UNITPRICE) * (A.OUTBOUND_GST/100)) + A.SHIPPINGCOST),0) AS AMOUNT FROM "
					+ "[" + plant + "_" + "DOHDR" + "] A JOIN "
					+ "[" + plant + "_" + "DODET" + "] B ON A.DONO = B.DONO ");
			sql.append(" WHERE A.DONO = ? AND A.PLANT = ? GROUP BY A.OUTBOUND_GST, A.SHIPPINGCOST");
			this.mLogger.query(this.printQuery, sql.toString());
			
			PreparedStatement stmt = con.prepareStatement(sql.toString());  
			stmt.setString(1,dono);  
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
	
	public List<DoDet> getDoDetById(String plant,String dono)throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		List<DoDet> doDetList = new ArrayList<DoDet>();
		try {
			connection = DbBean.getConnection();
			query = "SELECT " + 
					"           [PLANT]" + 
					"           ,[DONO]" + 
					"           ,[DOLNNO]" + 
					"           ,[PickStatus]" + 
					"           ,[LNSTAT]" + 
					"           ,[ITEM]" + 
					"           ,[ItemDesc]" + 
					"           ,[TRANDATE]" + 
					"           ,[TRANTYPE]" + 
					"           ,[UNITPRICE]" + 
					"           ,[ASNMT]" + 
					"           ,[QTYOR]" + 
					"           ,[QTYIS]" + 
					"           ,ISNULL([QtyPick],0.0) QtyPick" + 
					"           ,[UNITMO]" + 
					"           ,ISNULL([DELDATE], '') DELDATE" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]" + 
					"           ,[UPAT]" + 
					"           ,[UPBY]" + 
					"           ,[USERFLD1]" + 
					"           ,[USERFLD2]" + 
					"           ,[USERFLD3]" + 
					"           ,[CURRENCYUSEQT]" + 
					"           ,[ESTNO]" + 
					"           ,ISNULL([ESTLNNO],0) ESTLNNO" + 
					"           ,ISNULL([PRODGST],0) PRODGST" +
					"           ,[PRODUCTDELIVERYDATE]" + 
					"           ,ISNULL([ACCOUNT_NAME],'') ACCOUNT_NAME" + 
					"           ,ISNULL([DISCOUNT],0) DISCOUNT" +
					"           ,ISNULL([DISCOUNT_TYPE],'') DISCOUNT_TYPE" +
					"           ,ISNULL([ID],'') ID" +
					"           ,ISNULL([UNITCOST],0) UNITCOST" +
					"           ,ISNULL([ADDONAMOUNT],0) ADDONAMOUNT" +
					"           ,ISNULL([ADDONTYPE],'%') ADDONTYPE" +
					"           ,ISNULL([UKEY],'') UKEY" +
					"           ,ISNULL([TAX_TYPE],'') TAX_TYPE FROM ["+ plant +"_"+TABLE_DET+"] WHERE "+ 
					" DONO=? AND PLANT=? ORDER BY DOLNNO ASC";
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, dono);
				   ps.setString(2, plant);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   	DoDet doDet = new DoDet();
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, doDet);
	                    doDetList.add(doDet);
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
		return doDetList;
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
						"     WHERE DONO = ? AND DOLNNO = ? ";
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
				   
				   ps.setString(24, doDet.getDONO());
				   ps.setLong(25, doDet.getDOLNNO());
				  
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
	
	public DoDet getDoDetByDonoItm(String plant,String dono,String item)throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    DoDet doDet = new DoDet();
		try {
			connection = DbBean.getConnection();
			query = "SELECT " + 
					"           [ID]" + 
					"           ,[PLANT]" + 
					"           ,[DONO]" + 
					"           ,[DOLNNO]" + 
					"           ,[PickStatus]" + 
					"           ,[LNSTAT]" + 
					"           ,[ITEM]" + 
					"           ,[ItemDesc]" + 
					"           ,[TRANDATE]" + 
					"           ,[TRANTYPE]" + 
					"           ,[UNITPRICE]" + 
					"           ,[ASNMT]" + 
					"           ,[QTYOR]" + 
					"           ,[QTYIS]" + 
					"           ,ISNULL([QtyPick],0.0) QtyPick" + 
					"           ,[UNITMO]" + 
					"           ,ISNULL([DELDATE], '') DELDATE" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]" + 
					"           ,[UPAT]" + 
					"           ,[UPBY]" + 
					"           ,[USERFLD1]" + 
					"           ,[USERFLD2]" + 
					"           ,[USERFLD3]" + 
					"           ,[CURRENCYUSEQT]" + 
					"           ,[ESTNO]" + 
					"           ,ISNULL([ESTLNNO],0) ESTLNNO" + 
					"           ,ISNULL([PRODGST],0) PRODGST" +
					"           ,[PRODUCTDELIVERYDATE]" + 
					"           ,ISNULL([ACCOUNT_NAME],'') ACCOUNT_NAME" + 
					"           ,ISNULL([DISCOUNT],0) DISCOUNT" +
					"           ,ISNULL([DISCOUNT_TYPE],'') DISCOUNT_TYPE" +
					"           ,ISNULL([UNITCOST],0) UNITCOST" +
					"           ,ISNULL([ADDONAMOUNT],0) ADDONAMOUNT" +
					"           ,ISNULL([ADDONTYPE],'%') ADDONTYPE" +
					"      		,ISNULL([UKEY], '') UKEY" +
					"           ,ISNULL([TAX_TYPE],'') TAX_TYPE FROM ["+ plant +"_"+TABLE_DET+"] WHERE "+ 
					" DONO=? AND PLANT=? AND ITEM=?";
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, dono);
				   ps.setString(2, plant);
				   ps.setString(3, item);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, doDet);
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
		return doDet;
	}
	
	public boolean isgetDoDetById(String plant, String dono, int lnno, String item)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
	    HrEmpType hrEmpType=new HrEmpType();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_DET+"]  WHERE DONO ='"+dono+"' AND DOLNNO ='"+lnno+"' AND ITEM ='"+item+"'";

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
	
	public DoDet getDoDetById(String plant, String dono, int lnno, String item) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    DoDet doDet=new DoDet();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,DONO,DOLNNO,PickStatus,LNSTAT,ITEM,ItemDesc,TRANDATE,TRANTYPE,"
					+ "UNITPRICE,ASNMT,QTYOR,QTYIS,QtyPick,UNITMO,DELDATE,CRAT,CRBY,UPAT,UPBY,"
					+ "USERFLD1,USERFLD2,USERFLD3,CURRENCYUSEQT,ESTNO,ESTLNNO,"
					+ "ISNULL(PRODGST,'') AS PRODGST,PRODUCTDELIVERYDATE,ACCOUNT_NAME,TAX_TYPE,"
					+ "ISNULL(DISCOUNT,0) AS DISCOUNT,DISCOUNT_TYPE,UNITCOST,ISNULL([ADDONTYPE],'%') ADDONTYPE,ISNULL([ADDONAMOUNT],0) ADDONAMOUNT,ID,ISNULL(UKEY,'') AS UKEY "
					+ "FROM ["+ plant +"_"+TABLE_DET+"]  WHERE DONO ='"+dono+"' AND "
							+ "DOLNNO ='"+lnno+"' AND ITEM ='"+item+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, doDet);
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
		return doDet;
	}
}
