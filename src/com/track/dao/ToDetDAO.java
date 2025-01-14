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

import com.track.constants.MLoggerConstant;
import com.track.db.object.DoDet;
import com.track.db.object.HrEmpType;
import com.track.db.object.ToDet;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

public class ToDetDAO extends BaseDAO {

	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.TODETDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.TODETDAO_PRINTPLANTMASTERLOG;
	public static String TABLE_DET = "TODET";

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

	public ToDetDAO() {
		_StrUtils = new StrUtils();
	}

	public static String TABLE_NAME = "TODET";
	public static String plant = "";

	public ToDetDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "TODET" + "]";
	}

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "TODET" + "]");
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
					+ "[" + ht.get("PLANT") + "_" + "TODET" + "]");
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
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "TODET" + "]");

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
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "TODET" + "]");
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

	public ArrayList selectToDet(String query, Hashtable ht) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "TODET" + "]");
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
 	/***Bruhan,Nov 11 2014,Description: To check transfer detail exists in TO_PICK table
	 *     **********************  Modification History  ******************************
	 */ 
	public boolean isExisitTOPick(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql
					.append(" FROM " + "[" + ht.get("PLANT") + "_" + "TO_PICK"
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
	 /***Bruhan,Nov 11 2014,Description: To check transfer detail exists in RECVDET table
		 ***********************  Modification History  ******************************

	 */ 
	public boolean isExisitTOReceive(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql
					.append(" FROM " + "[" + ht.get("PLANT") + "_" + "RECVDET"
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

	public ArrayList selectToDet(String query, Hashtable ht, String extCond)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "TODET" + "] a ");
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
                                
                                System.out.println("sql:::"+sql.toString());
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

	public boolean insertToDet(Hashtable ht) throws Exception {
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
					+ "TODET" + "]" + "("
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
		Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
	
			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + "TODET" + "]");
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
        
    
    public boolean updateToPickTable(String query, Hashtable htCondition, String extCond)
                    throws Exception {
            boolean flag = false;
            java.sql.Connection con = null;
            try {
                    con = com.track.gates.DbBean.getConnection();
                    StringBuffer sql = new StringBuffer(" UPDATE " + "["
                                    + htCondition.get("PLANT") + "_" + "TO_PICK" + "]");
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
                    if (con != null) {
                            DbBean.closeConnection(con);
                    }
            }
            return flag;
    }
 /***Bruhan,Nov 11 2014,Description: To update Recv table
   	 *     **********************  Modification History  ******************************
   	 */ 
    public boolean updateToRecvTable(String query, Hashtable htCondition, String extCond)
            throws Exception {
    boolean flag = false;
    java.sql.Connection con = null;
    try {
            con = com.track.gates.DbBean.getConnection();
            StringBuffer sql = new StringBuffer(" UPDATE " + "["
                            + htCondition.get("PLANT") + "_" + "RECVDET" + "]");
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
            if (con != null) {
                    DbBean.closeConnection(con);
            }
    }
    return flag;
}
    
    
    

	public ArrayList getTransferReceivingDetailsByWMS(String plant,
			String orderno) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			StringBuffer sQry = new StringBuffer(
					"select a.tono,a.tolnno,a.item,isnull(a.qtyor,0) as qtyor,isnull(a.qtypick,0) as qtypick,isnull(a.qtyrc,0) as qtyrc,isnull(a.userfld2,'') as ref,isnull(a.userfld3,'') as custname,");
			sQry
					.append(" isnull(name,'') contactname,isnull(b.telno,'') as telno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3 ");
			sQry.append(" from  " + "[" + plant + "_" + "todet" + "]" + " a,"
					+ "[" + plant + "_" + "to_assignee_master" + "]" + " b");
			sQry.append(" where a.plant='" + plant + "' and a.tono like '"
					+ orderno
					+ "%' and a.LNSTAT <> 'C'  and a.userfld3=b.assignename");

			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "TODET";
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

	public ArrayList getTransferItemListByWMS(String plant, String orderno,
			String itemno) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {

			con = com.track.gates.DbBean.getConnection();
			//below query is changed by Bruhan to get itemdesc,unitmo for transfer order random
			
			boolean flag = false;
			String sQry = "select distinct tono,tolnno,item,itemdesc,unitmo,isnull(qtyor,0)as qtyor,isnull(qtyrc,0) as qtyrc,isnull(qtypick,0) as qtypick,isnull(userfld2,'') as ref,isnull(userfld3,'') as custname from "
					+ "["
					+ plant
					+ "_"
					+ "todet"
					+ "]"
					+ " where plant='"
					+ plant
					+ "'and tono = '"
					+ orderno
					+ "'  and lnstat <> 'C' and item = '" + itemno + "'";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);
			TABLE_NAME = "TODET";

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
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_TODET]");
			sql.append(" WHERE " + formCondition(ht));

			this.mLogger.query(this.printQuery, sql.toString());
			delete = updateConData(con, sql.toString());
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

	public Boolean removeOrderDetails(String plant2, String tono)
			throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant2 + "_" + "TODET] WHERE TONO='"
					+ tono + "' AND PLANT='" + plant2 + "'";
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
	public ArrayList getToOrderBatchListToRecv(String plant, String ordno,
			String ordlnno, String item) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sQry = new StringBuffer(
					"select distinct  batch,loc,sum(pickqty) as pickQty,isnull((select sum(recqty) from  "
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
			sQry.append(" like '%' and PickQty > 0 group by batch,loc ");

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
     
	public ArrayList getToOrderBatchListToRecv(String plant, String ordno,
			String ordlnno, String item,String batch) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		String batchsql="";
		try {
			con = com.track.gates.DbBean.getConnection();
			if(!batch.equalsIgnoreCase("NOBATCH"))
			{
				batchsql = " and  batch like '"+batch+"%'";
			}
			StringBuffer sQry = new StringBuffer(
					"select distinct  batch,loc,sum(pickqty) as pickQty,isnull((select sum(recqty) from  "
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
					+ "' and  item='" + item +"'"+ batchsql);
			sQry.append(" and PickQty > 0 group by batch,loc ");

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
    public ArrayList getToOrderQtyToRecvForBatch(String plant, String ordno,
                    String ordlnno, String item,String batch,String loc) throws Exception {

            java.sql.Connection con = null;
            ArrayList al = new ArrayList();
            try {
                    con = com.track.gates.DbBean.getConnection();

                    StringBuffer sQry = new StringBuffer(
                                    "select distinct  batch,loc,sum(pickqty) as pickQty,isnull((select sum(recqty) from  "
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
                    sQry.append(" = '"+batch+"' and loc ='"+loc+"' and PickQty > 0 group by batch,loc ");

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

	public ArrayList getToOrderBatchDetails(String plant, String ordno,
			String ordlnno, String item, String batch) throws Exception {

		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();

			StringBuffer sQry = new StringBuffer(
					"select distinct  batch,loc,sum(pickqty) as pickQty,isnull((select sum(recqty) from  "
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
					+ "' and  item='" + item + "'  and  batch ='" + batch
					+ "' and PickQty > 0 group by batch,loc ");

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
        
        
    public ArrayList getDoDetailToPrint(String plant, String orderno)
                throws Exception {

        java.sql.Connection con = null;
        ArrayList al = new ArrayList();
        try {
                con = com.track.gates.DbBean.getConnection();

                boolean flag = false;

                StringBuffer sQry = new StringBuffer(
                                " SELECT TOLNNO,ITEM,ITEMDESC,UNITMO,QTYOR,ISNULL(qtypick,0) as qtypick FROM "
                                                + "[" + plant + "_" + "Todet" + "] a " + "");
                sQry
                                .append(" where plant='"
                                                + plant
                                                + "' and tono = '"
                                                + orderno
                                                + "'");

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
    
    public ArrayList getDoDetailToPrintTo(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String custname,String custtype) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",extraCon="",dtCondStr="",dtcondpick="";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			            if (custname.length()>0){
                         	custname = new StrUtils().InsertQuotes(custname);
                         	sCondition =  sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
                         }
			 
				 	dtCondStr  = "  and ISNULL(b.RECVDATE,'')<>'' AND CAST((SUBSTRING(b.RECVDATE, 7, 4) + '-' + SUBSTRING(b.RECVDATE, 4, 2) + '-' + SUBSTRING(b.RECVDATE, 1, 2)) AS date)";
				 	dtcondpick = "  and ISNULL(RECVDATE,'')<>'' AND CAST((SUBSTRING(RECVDATE, 7, 4) + '-' + SUBSTRING(RECVDATE, 4, 2) + '-' + SUBSTRING(RECVDATE, 1, 2)) AS date)";
			 
                        if (afrmDate.length() > 0) {
                        	sCondition = sCondition +dtCondStr+ " >= '" 
            						+ afrmDate
            						+ "'  ";
                        	dtcondpick = dtcondpick+ " >= '"+ afrmDate+ "'  ";
            				if (atoDate.length() > 0) {
            					sCondition = sCondition + dtCondStr + " <= '" 
            					+ atoDate
            					+ "'  ";
            					dtcondpick = dtcondpick +" AND CAST((SUBSTRING(RECVDATE, 7, 4) + '-' + SUBSTRING(RECVDATE, 4, 2) + '-' + SUBSTRING(RECVDATE, 1, 2)) AS date) <= '"+ atoDate+ "'  "; 	
            				}
            			} else {
            				if (atoDate.length() > 0) {
            					sCondition = sCondition +dtCondStr+ "  <= '" 
            					+ atoDate
            					+ "'  ";
            					dtcondpick = dtcondpick+"  <= '"+ atoDate+ "'  ";
            				}
            			} 
                        
                        if (custtype != null && custtype!="") {
                			sCondition = sCondition + " AND custname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
                		}
                        
                        
                        
                        	extraCon = "  group by  b.cname,b.pono,a.empno order by b.cname,b.pono ";
                          
                        	StringBuffer sql1 = new StringBuffer(" select b.PONO as dono,b.cname as custname,");
                        	sql1.append("(SELECT CUSTCODE  FROM ["+plant+"_TOHDR] WHERE TONO=b.PONO) custcode,");
            				sql1.append("(SELECT ORDERTYPE  FROM ["+plant+"_TOHDR] WHERE TONO=b.PONO) ordertype,");
            				sql1.append("(SELECT JOBNUM  FROM ["+plant+"_TOHDR] WHERE TONO=b.PONO) jobnum,");
            				sql1.append("(SELECT STATUS  FROM ["+plant+"_TOHDR] WHERE TONO=b.PONO) status,");
            				sql1.append("isnull((select isnull(fname,'') from ["+plant+"_EMP_MST] where EMPNO=a.EMPNO),'')empname,");
            				sql1.append("(SELECT SUM(QTYOR) FROM ["+plant+"_TODET] WHERE  TONO=b.PONO GROUP BY TONO) qtyor,");
            				sql1.append("(SELECT SUM(RECQTY) FROM ["+plant+"_RECVDET] WHERE PONO like 'C%' and PONO=b.PONO"+dtcondpick+" ) qtypick,");
            				sql1.append("isnull(sum(b.RECQTY),0) qty ");
            				sql1.append(" from " + "[" + plant + "_" + "tohdr" + "] a,");
            				sql1.append( "[" + plant + "_" + "RECVDET" + "] b");
            				sql1.append(" where a.tono=b.pono and b.PONO like 'C%' and b.RECEIVESTATUS='C'" + sCondition);
            				arrList = movHisDAO.selectForReport(sql1.toString(), ht,extraCon);
            				
                        
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getCustomerDOInvoiceSummary:", e);
		}
		return arrList;
	}

    
    public int getCountToNo(Hashtable ht, String extCon) throws Exception {
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
					+ ht.get("PLANT") + "_TOHDR]" + " WHERE "
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
    
    public int getMaxToLnNo(Hashtable ht) throws Exception {
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
			String sQry = "SELECT MAX(TOLNNO) FROM " + "["
					+ ht.get("PLANT") + "_TODET]" + " WHERE "
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
    
  //Start code by Bruhan for transfer pick issue(random) on 8 May 2013
    
public ArrayList listTODETDetailswithscanqty(String plant,String aTONO)
    throws Exception {

		Connection con = null;
		ArrayList al = new ArrayList();
		
		try {
			con = DbBean.getConnection();

			StringBuffer sQry = new StringBuffer(
					 "select a.tono,a.item,ISNULL(a.itemdesc,'')itemdesc,sum(ISNULL(a.qtyor,0)) as qtyor,sum(ISNULL(a.qtypick,0)) as qtypick,"
          	 			+"isnull(b.fromwarehouse,'') as fromloc,isnull(b.towarehouse,'') as toloc,"
          	 			+"isnull((select SUM(qty)  from "+plant+"_RANDOM_SCAN_TEMP  where "+plant+"_RANDOM_SCAN_TEMP.ORDERNO = a.TONO" 
          	 			+" and "+plant+"_RANDOM_SCAN_TEMP.ITEM =a.ITEM group by item),0)as scannedqty"
          	 			+"  from "+plant+"_TODET a,"+plant+"_TOHDR b"	
						+ " where a.tono=b.tono and a.PLANT=b.plant and a.plant ='"
						+ plant
						+ "' and a.tono='"
						+ aTONO);
							
			sQry.append(" ");
			sQry.append("' and a.plant <> '' and ISNULL(a.pickstatus,'') <>'C' group by  a.TONO,a.ITEM,a.ItemDesc,b.fromwarehouse,b.TOWAREHOUSE order by a.item,a.tono");

					

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

public ArrayList getTODetDetails(String Plant,String TONO,String item) {
	
	ArrayList al = null;
	Connection con = null;
	
	try {
		con = DbBean.getConnection();
		String query = "select tono,tolnno, lnstat,item,isnull(qtyor,0) as qtyor,"
				+ " isnull(qtyPick,0) as qtyPick , (select SUM(isnull(qtyor,0)- isnull(qtyPick,0))  from "
				+ " " +Plant+"_TODET where TONO=a.tono and ITEM =a.item)  totBal from "
				+ " " +Plant+"_TODET a where tono = '"+TONO+"' and plant <> '' and PickStatus <>'C' " 
			    + " and ITEM = '"+item+"' order by tolnno";

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
    
//End code by Bruhan for transfer pick issue(random) on 8 May 2013 
    

		public ArrayList selectPickItemDetails(String Plant, String tono,String Item)
		throws Exception {
		
			ArrayList alData = new ArrayList();
			java.sql.Connection con = null;
			String sCondition1="";
			sCondition1 = " WHERE TONO = '" + tono + "'  AND ITEM='" + Item + "' AND PICKSTATUS <> 'C'";
			String sql = "SELECT DISTINCT(ITEM) ITEM,ISNULL(ITEMDESC,'') ITEMDESC,ISNULL(UNITMO,'') UOM from "
				+ "["
				+ Plant
				+ "_"
				+ "TODET "
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
		
		public ArrayList selectReceiveItemDetails(String Plant, String tono,String Item)
		throws Exception {
		
			ArrayList alData = new ArrayList();
			java.sql.Connection con = null;
			String sCondition1="";
			sCondition1 = " WHERE TONO = '" + tono + "'  AND ITEM='" + Item + "' AND lnstat <> 'C'";
			String sql = "SELECT DISTINCT(ITEM) ITEM,ISNULL(ITEMDESC,'') ITEMDESC,ISNULL(UNITMO,'') UOM from "
				+ "["
				+ Plant
				+ "_"
				+ "TODET "
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

		public ArrayList getTODetDetailsRandom(String Plant,
					String TONO,String item) {
			 
				ArrayList al = null;
				Connection con = null;
				//PlantMstUtil _PlantMstUtil = new PlantMstUtil();
				try {
					con = DbBean.getConnection();
			
					String query = "select tono,tolnno,pickstatus lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,"
						+ " isnull(qtyPick,0) as qtyPick , (isnull(qtyor,0)- isnull(qtyPick,0)) totBal,(isnull(qtyPick,0)- isnull(qtyrc,0))totRecBal "
						+ " from "
						+ " " +Plant+"_TODET a where tono = '"+TONO+"' and plant <> '' and PickStatus <>'C' " 
					    + " and ITEM = '"+item+"' order by tolnno";

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
		
		public ArrayList getTODetDetailsRecvRandom(String Plant,
				String TONO,String item) {
		 
			ArrayList al = null;
			Connection con = null;
			//PlantMstUtil _PlantMstUtil = new PlantMstUtil();
			try {
				con = DbBean.getConnection();
		
				String query = "select tono,tolnno,pickstatus lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,"
					+ " isnull(qtyPick,0) as qtyPick , (isnull(qtyor,0)- isnull(qtyPick,0)) totBal,(isnull(qtyPick,0)- isnull(qtyrc,0))totRecBal "
					+ " from "
					+ " " +Plant+"_TODET a where tono = '"+TONO+"' and plant <> '' and lnstat <>'C' AND isnull(qtypick,0) > 0  and isnull(qtyrc,0) < isnull(qtypick,0)" 
				    + " and ITEM = '"+item+"' order by item,tolnno";

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
		public ArrayList getToOrderBatchListToRecvRandom(String plant, String ordno,
				 String item,String batch) throws Exception {

			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sQry = new StringBuffer(
						"select distinct  batch,loc,sum(pickqty) as pickQty,isnull((select sum(recqty) from  "
								+ "["
								+ plant
								+ "_"
								+ "Recvdet"
								+ "]"
								+ " where plant ='"
								+ plant
								+ "' and pono ='"
								+ ordno
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
				sQry.append("  tono ='" + ordno + "'  and  item='" + item + "'  and  ");
				sQry.append("  PickQty > 0 group by batch,loc ");

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
		
		public ArrayList getToOrderBatchListByBatchToRecvRandom(String plant, String ordno,
				 String item,String batch) throws Exception {

			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sQry = new StringBuffer(
						"select distinct  batch,loc,sum(pickqty) as pickQty,isnull((select sum(recqty) from  "
								+ "["
								+ plant
								+ "_"
								+ "Recvdet"
								+ "]"
								+ " where plant ='"
								+ plant
								+ "' and pono ='"
								+ ordno
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
				sQry.append("  tono ='" + ordno + "'  and  item='" + item + "'  and  batch  ");
				sQry.append(" like '%" + batch + "%' and PickQty > 0 group by batch,loc ");

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
         	
		
          	
		
		
		public ArrayList getToOrderBatchListToRecvQtyRandom(String plant, String ordno,
				 String item,String loc,String batch) throws Exception {

			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sQry = new StringBuffer(
						"select distinct  batch,loc,sum(pickqty) as pickQty,isnull((select sum(recqty) from  "
								+ "["
								+ plant
								+ "_"
								+ "Recvdet"
								+ "]"
								+ " where plant ='"
								+ plant
								+ "' and pono ='"
								+ ordno
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
				sQry.append("  tono ='" + ordno + "'  and  item='" + item + "'  and  batch  ");
				sQry.append(" like '%" + batch + "%' and loc='" + loc +"' and PickQty > 0 group by batch,loc ");

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
		
//start code added by Bruhan for transfer order reversal on 20 August 2013		
public ArrayList selectReverseToDet(String query, Hashtable ht, String extCond)
		throws Exception {
	boolean flag = false;
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;
	
	StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
			+ ht.get("a.PLANT") + "_" + "TODET" + "]a,[" +  ht.get("a.PLANT") + "_" + "TO_PICK "
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
			sql.append(" " + extCond);
                            
                            System.out.println("sql:::"+sql.toString());
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


public boolean deleteTOPick(java.util.Hashtable ht) throws Exception {
	boolean delete = false;
	java.sql.Connection con = null;
	try {
		con = DbBean.getConnection();

		StringBuffer sql = new StringBuffer(" DELETE ");
		sql.append(" ");
		sql.append(" FROM " + "[" + ht.get("PLANT") + "_TO_PICK]");
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
//End code added by Bruhan for transfer order reversal on 20 August 2013         	
  
//start code added by Bruhan for transfer order pick&issue by product on 23 sep 2013         	

public ArrayList selectToDetailsbyProd(String query, Hashtable ht, String extCond)
throws Exception {
boolean flag = false;
ArrayList alData = new ArrayList();
java.sql.Connection con = null;

StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
	+ ht.get("a.PLANT") + "_" + "TODET" + "] a,"+ "["
	+ ht.get("a.PLANT") + "_" + "TOHDR" + "] b");
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
                    
                    System.out.println("sql:::"+sql.toString());
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

public ArrayList getpickedDetailsforreverse(String plant, Hashtable ht)
		throws Exception {
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		boolean flag = false;
		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append(" TONO,TOLNO,ITEM,BATCH,PICKQTY,CRAT,ID");
		sql.append(" ");
		sql.append(" FROM " + "[" + plant + "_" + "TO_PICK" + "]");
		sql.append(" WHERE  " + formCondition(ht));
		sql.append(" order by ISSUEDATE asc");
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

public ArrayList selectginotoinvoice(String query, Hashtable ht, String extCond)
		throws Exception {
//	boolean flag = false;
	ArrayList alData = new ArrayList();
	
	String plant = (String) ht.get("PLANT");

	java.sql.Connection con = null;

	StringBuffer sql = new StringBuffer(" SELECT DISTINCT " + query + " from " + "["
			+ plant + "_" + "TO_PICK" + "] A ");
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
//imtinavas
public boolean insertToDet(List<ToDet> toDetList) throws Exception {
	boolean insertFlag = false;
	Connection connection = null;
	PreparedStatement ps = null;
    String query = "";
	try {
		connection = DbBean.getConnection();
		
		for(ToDet toDet : toDetList) {
			query = "INSERT INTO ["+toDet.getPLANT()+"_"+TABLE_DET+"]" + 
					"           ([PLANT]" + 
					"           ,[TONO]" + 
					"           ,[TOLNNO]" + 
					"           ,[PickStatus]" + 
					"           ,[LNSTAT]" + 
					"           ,[ITEM]" + 
					"           ,[ItemDesc]" + 
					"           ,[TRANDATE]" + 
					"           ,[ASNMT]" + 
					"           ,[QTYOR]" + 
					"           ,[QTYRC]" + 
					"           ,[QtyPick]" +
					"           ,[QTYAC]" + 
					"           ,[QTYRJ]" + 
					"           ,[LOC]" + 
					"           ,[WHID]" + 
					"           ,[POLTYPE]" + 
					"           ,[UNITMO]" + 
					"           ,[DELDATE]" + 
					"           ,[COMMENT1]" + 
					"           ,[COMMENT2]" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]" + 
					"           ,[UPAT]" + 
					"           ,[UPBY]" + 
					"           ,[RECSTAT]" + 
					"           ,[USERFLD1]" + 
					"           ,[USERFLD2]" + 
					"           ,[USERFLD3]" + 
					"           ,[USERFLD4]" + 
					"           ,[USERFLD5]" + 
					"           ,[USERFLD6]" + 
					"           ,[USERFLG1]" + 
					"           ,[USERFLG2]" + 
					"           ,[USERFLG3]" + 
					"           ,[USERFLG4]" + 
					"           ,[USERFLG5]" + 
					"           ,[USERFLG6]" + 
					"           ,[USERTIME1]" + 
					"           ,[USERTIME2]" + 
					"           ,[USERTIME3]" + 
					"           ,[USERDBL1]" + 
					"           ,[USERDBL2]" + 
					"           ,[USERDBL3]" + 
					"           ,[USERDBL4]" + 
					"           ,[USERDBL5]" + 
					"           ,[USERDBL6]" + 
					"           ,[UNITPRICE]" + 
					"           ,[CURRENCYUSEQT]" + 
					"           ,[PRODGST]" +
					"           ,[PRODUCTDELIVERYDATE]" + 
					"           ,[ACCOUNT_NAME]" +
					"           ,[TAX_TYPE]" + 
					"           ,[DISCOUNT]" +
					"           ,[DISCOUNT_TYPE])" +
					"     VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if(connection != null){
			   ps = connection.prepareStatement(query);
			   ps.setString(1, toDet.getPLANT());
			   ps.setString(2, toDet.getTONO());
			   ps.setLong(3, toDet.getTOLNNO());
			   ps.setString(4, toDet.getPickStatus());
			   ps.setString(5, toDet.getLNSTAT());
			   ps.setString(6, toDet.getITEM());
			   ps.setString(7, toDet.getItemDesc());
			   ps.setString(8, toDet.getTRANDATE());
			   ps.setString(9, toDet.getASNMT());
			   ps.setBigDecimal(10, toDet.getQTYOR());
			   ps.setBigDecimal(11, toDet.getQTYRC());
			   ps.setBigDecimal(12, toDet.getQtyPick());
			   ps.setBigDecimal(13, toDet.getQTYAC());
			   ps.setString(14, toDet.getQTYRJ());
			   ps.setString(15, toDet.getLOC());
			   ps.setString(16, toDet.getWHID());
			   ps.setString(17, toDet.getPOLTYPE());
			   ps.setString(18, toDet.getUNITMO());
			   ps.setString(19, toDet.getDELDATE());
			   ps.setString(20, toDet.getCOMMENT1());
			   ps.setString(21, toDet.getCOMMENT2());
			   ps.setString(22, toDet.getCRAT());
			   ps.setString(23, toDet.getCRBY());
			   ps.setString(24, toDet.getUPAT());
			   ps.setString(25, toDet.getUPBY());
			   ps.setString(26, toDet.getRECSTAT());
			   ps.setString(27, toDet.getUSERFLD1());
			   ps.setString(28, toDet.getUSERFLD2());
			   ps.setString(29, toDet.getUSERFLD3());
			   ps.setString(30, toDet.getUSERFLD4());
			   ps.setString(31, toDet.getUSERFLD5());
			   ps.setString(32, toDet.getUSERFLD6());
			   ps.setString(33, toDet.getUSERFLG1());
			   ps.setString(34, toDet.getUSERFLG2());
			   ps.setString(35, toDet.getUSERFLG3());
			   ps.setString(36, toDet.getUSERFLG4());
			   ps.setString(37, toDet.getUSERFLG5());
			   ps.setString(38, toDet.getUSERFLG6());
			   ps.setString(39, toDet.getUSERTIME1());
			   ps.setString(40, toDet.getUSERTIME2());
			   ps.setString(41, toDet.getUSERTIME3());
			   ps.setString(42, toDet.getUSERDBL1());
			   ps.setString(43, toDet.getUSERDBL2());
			   ps.setString(44, toDet.getUSERDBL3());
			   ps.setString(45, toDet.getUSERDBL4());
			   ps.setString(46, toDet.getUSERDBL4());
			   ps.setString(47, toDet.getUSERDBL6());
			   ps.setDouble(48, toDet.getUNITPRICE());
			   ps.setDouble(49, toDet.getCURRENCYUSEQT());
			   ps.setDouble(50, toDet.getPRODGST());
			   ps.setString(51, toDet.getPRODUCTDELIVERYDATE());
			   ps.setString(52, toDet.getACCOUNT_NAME());
			   ps.setString(53, toDet.getTAX_TYPE());
			   ps.setDouble(54, toDet.getDISCOUNT());
			   ps.setString(55, toDet.getDISCOUNT_TYPE());
			  
			   int count=ps.executeUpdate();
			   if(count>0)
			   {
				   insertFlag = true;
			   }
			   else
			   {
				   throw new SQLException("Creating Consigment Order failed, no rows affected.");
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

public List<ToDet> getToDetById(String plant,String tono)throws Exception {
	boolean flag = false;
	Connection connection = null;
	PreparedStatement ps = null;
    String query = "";
	List<ToDet> toDetList = new ArrayList<ToDet>();
	try {
		connection = DbBean.getConnection();
		query = "SELECT " + 	
				"           [PLANT]" + 
				"           ,[TONO]" + 
				"           ,[TOLNNO]" + 
				"           ,[PickStatus]" + 
				"           ,[LNSTAT]" + 
				"           ,[ITEM]" + 
				"           ,[ItemDesc]" + 
				"           ,[TRANDATE]" + 
				"           ,[ASNMT]" + 
				"           ,[QTYOR]" + 
				"           ,[QTYRC]" + 
				"           ,ISNULL([QtyPick],0.0) QtyPick" + 
				"           ,ISNULL([QTYAC],0.0) QTYAC" + 
				"           ,[QTYRJ]" + 
				"           ,[LOC]" + 
				"           ,[WHID]" + 
				"           ,[POLTYPE]" + 
				"           ,[UNITMO]" + 
				"           ,ISNULL([DELDATE], '') DELDATE" + 
				"           ,[COMMENT1]" + 
				"           ,[COMMENT2]" + 
				"           ,[CRAT]" + 
				"           ,[CRBY]" + 
				"           ,[UPAT]" + 
				"           ,[UPBY]" + 
				"           ,[RECSTAT]" + 
				"           ,[USERFLD1]" + 
				"           ,[USERFLD2]" + 
				"           ,[USERFLD3]" + 
				"           ,[USERFLD4]" + 
				"           ,[USERFLD5]" + 
				"           ,[USERFLD6]" + 
				"           ,[USERFLG1]" + 
				"           ,[USERFLG2]" + 
				"           ,[USERFLG3]" + 
				"           ,[USERFLG4]" + 
				"           ,[USERFLG5]" + 
				"           ,[USERFLG6]" + 
				"           ,[USERTIME1]" + 
				"           ,[USERTIME2]" + 
				"           ,[USERTIME3]" + 
				"           ,[USERDBL1]" + 
				"           ,[USERDBL2]" + 
				"           ,[USERDBL3]" + 
				"           ,[USERDBL4]" + 
				"           ,[USERDBL5]" + 
				"           ,[USERDBL6]" + 
//				"           ,[UNITPRICE]" + 
				"			,ISNULL([UNITPRICE],0.0) UNITPRICE" + 	
//				"           ,[CURRENCYUSEQT]" + 
				"			,ISNULL([CURRENCYUSEQT],0.0) CURRENCYUSEQT" + 
				"			,ISNULL([PRODGST],0.0) PRODGST" + 
//				"           ,[PRODGST]" +
				"           ,[PRODUCTDELIVERYDATE]" + 
				"           ,ISNULL([ACCOUNT_NAME],'') ACCOUNT_NAME" + 
				"           ,ISNULL([DISCOUNT],0) DISCOUNT" +
				"           ,ISNULL([DISCOUNT_TYPE],'') DISCOUNT_TYPE" +
				"           ,ISNULL([TAX_TYPE],'') TAX_TYPE FROM ["+ plant +"_"+TABLE_DET+"] WHERE "+ 
				" TONO=? AND PLANT=?";
		if(connection != null){
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			   ps.setString(1, tono);
			   ps.setString(2, plant);
			   ResultSet rst = ps.executeQuery();
			   while (rst.next()) {
				   	ToDet toDet = new ToDet();
                    ResultSetToObjectMap.loadResultSetIntoObject(rst, toDet);
                    toDetList.add(toDet);
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
	return toDetList;
}

public boolean updateToDet(List<ToDet> toDetList) throws Exception {
	boolean updateFlag = false;
	Connection connection = null;
	PreparedStatement ps = null;
    String query = "";
	try {
		connection = DbBean.getConnection();
		
		for(ToDet toDet : toDetList) {
			query = "UPDATE ["+toDet.getPLANT()+"_"+TABLE_DET+"] SET " + 
					"           ([PLANT] = ?" + 
					"           ,[TONO] = ?" + 
					"           ,[TOLNNO] = ?" + 
					"           ,[PickStatus] = ?" + 
					"           ,[LNSTAT] = ?" + 
					"           ,[ITEM] = ?" + 
					"           ,[ItemDesc] = ?" + 
					"           ,[TRANDATE] = ?" + 
					"           ,[ASNMT] = ?" + 
					"           ,[QTYOR] = ?" + 
					"           ,[QTYRC] = ?" + 
					"           ,[QtyPick] = ?" +
					"           ,[QTYAC] = ?" + 
					"           ,[QTYRJ] = ?" + 
					"           ,[LOC] = ?" + 
					"           ,[WHID] = ?" + 
					"           ,[POLTYPE] = ?" + 
					"           ,[UNITMO] = ?" + 
					"           ,[DELDATE] = ?" + 
					"           ,[COMMENT1] = ?" + 
					"           ,[COMMENT2] = ?" + 
					"           ,[CRAT] = ?" + 
					"           ,[CRBY] = ?" + 
					"           ,[UPAT] = ?" + 
					"           ,[UPBY] = ?" + 
					"           ,[RECSTAT] = ?" + 
					"           ,[USERFLD1] = ?" + 
					"           ,[USERFLD2] = ?" + 
					"           ,[USERFLD3] = ?" + 
					"           ,[USERFLD4] = ?" + 
					"           ,[USERFLD5] = ?" + 
					"           ,[USERFLD6] = ?" + 
					"           ,[USERFLG1] = ?" + 
					"           ,[USERFLG2] = ?" + 
					"           ,[USERFLG3] = ?" + 
					"           ,[USERFLG4] = ?" + 
					"           ,[USERFLG5] = ?" + 
					"           ,[USERFLG6] = ?" + 
					"           ,[USERTIME1] = ?" + 
					"           ,[USERTIME2] = ?" + 
					"           ,[USERTIME3] = ?" + 
					"           ,[USERDBL1] = ?" + 
					"           ,[USERDBL2] = ?" + 
					"           ,[USERDBL3] = ?" + 
					"           ,[USERDBL4] = ?" + 
					"           ,[USERDBL5] = ?" + 
					"           ,[USERDBL6] = ?" + 
					"           ,[UNITPRICE] = ?" + 
					"           ,[CURRENCYUSEQT] = ?" + 
					"           ,[PRODGST] = ?" +
					"           ,[PRODUCTDELIVERYDATE] = ?" + 
					"           ,[ACCOUNT_NAME] = ?" +
					"           ,[TAX_TYPE] = ?" + 
					"           ,[DISCOUNT] = ?" +
					"           ,[DISCOUNT_TYPE] = ?" +
					"     WHERE TONO = ? AND TOLNNO = ? ";
			if(connection != null){
			   ps = connection.prepareStatement(query);
			   ps.setString(1, toDet.getPLANT());
			   ps.setString(2, toDet.getTONO());
			   ps.setLong(3, toDet.getTOLNNO());
			   ps.setString(4, toDet.getPickStatus());
			   ps.setString(5, toDet.getLNSTAT());
			   ps.setString(6, toDet.getITEM());
			   ps.setString(7, toDet.getItemDesc());
			   ps.setString(8, toDet.getTRANDATE());
			   ps.setString(9, toDet.getASNMT());
			   ps.setBigDecimal(10, toDet.getQTYOR());
			   ps.setBigDecimal(11, toDet.getQTYRC());
			   ps.setBigDecimal(12, toDet.getQtyPick());
			   ps.setBigDecimal(13, toDet.getQTYAC());
			   ps.setString(14, toDet.getQTYRJ());
			   ps.setString(15, toDet.getLOC());
			   ps.setString(16, toDet.getWHID());
			   ps.setString(17, toDet.getPOLTYPE());
			   ps.setString(18, toDet.getUNITMO());
			   ps.setString(19, toDet.getDELDATE());
			   ps.setString(20, toDet.getCOMMENT1());
			   ps.setString(21, toDet.getCOMMENT2());
			   ps.setString(22, toDet.getCRAT());
			   ps.setString(23, toDet.getCRBY());
			   ps.setString(24, toDet.getUPAT());
			   ps.setString(25, toDet.getUPBY());
			   ps.setString(26, toDet.getRECSTAT());
			   ps.setString(27, toDet.getUSERFLD1());
			   ps.setString(28, toDet.getUSERFLD2());
			   ps.setString(29, toDet.getUSERFLD3());
			   ps.setString(30, toDet.getUSERFLD4());
			   ps.setString(31, toDet.getUSERFLD5());
			   ps.setString(32, toDet.getUSERFLD6());
			   ps.setString(33, toDet.getUSERFLG1());
			   ps.setString(34, toDet.getUSERFLG2());
			   ps.setString(35, toDet.getUSERFLG3());
			   ps.setString(36, toDet.getUSERFLG4());
			   ps.setString(37, toDet.getUSERFLG5());
			   ps.setString(38, toDet.getUSERFLG6());
			   ps.setString(39, toDet.getUSERTIME1());
			   ps.setString(40, toDet.getUSERTIME2());
			   ps.setString(41, toDet.getUSERTIME3());
			   ps.setString(42, toDet.getUSERDBL1());
			   ps.setString(43, toDet.getUSERDBL2());
			   ps.setString(44, toDet.getUSERDBL3());
			   ps.setString(45, toDet.getUSERDBL4());
			   ps.setString(46, toDet.getUSERDBL4());
			   ps.setString(47, toDet.getUSERDBL6());
			   ps.setDouble(48, toDet.getUNITPRICE());
			   ps.setDouble(49, toDet.getCURRENCYUSEQT());
			   ps.setDouble(50, toDet.getPRODGST());
			   ps.setString(51, toDet.getPRODUCTDELIVERYDATE());
			   ps.setString(52, toDet.getACCOUNT_NAME());
			   ps.setString(53, toDet.getTAX_TYPE());
			   ps.setDouble(54, toDet.getDISCOUNT());
			   ps.setString(55, toDet.getDISCOUNT_TYPE());
			   
			   ps.setString(56, toDet.getTONO());
			   ps.setLong(57, toDet.getTOLNNO());
			  
			   int count=ps.executeUpdate();
			   if(count>0)
			   {
				   updateFlag = true;
			   }
			   else
			   {
				   throw new SQLException("Updating Consignment Order failed, no rows affected.");
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
public boolean isgetToDetById(String plant, String tono, int lnno, String item)throws Exception {
	Connection connection = null;
	PreparedStatement ps = null;
	boolean status = false;
    String query = "";
    HrEmpType hrEmpType=new HrEmpType();
	try {	    
		connection = DbBean.getConnection();
		query = "SELECT * FROM ["+ plant +"_"+TABLE_DET+"]  WHERE TONO ='"+tono+"' AND TOLNNO ='"+lnno+"' AND ITEM ='"+item+"'";

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

public ToDet getToDetById(String plant, String tono, int lnno, String item) throws Exception {
	Connection connection = null;
	PreparedStatement ps = null;
    String query = "";
    ToDet toDet=new ToDet();
	try {	    
		connection = DbBean.getConnection();
		query = "SELECT * FROM ["+ plant +"_"+TABLE_DET+"]  WHERE TONO ='"+tono+"' AND TOLNNO ='"+lnno+"' AND ITEM ='"+item+"'";

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


public boolean isExisitToMultiRemarks(Hashtable ht) throws Exception {
  this.mLogger.log(1, this.getClass() + " isExisit()");
  boolean flag = false;
  java.sql.Connection con = null;
  try {
          con = com.track.gates.DbBean.getConnection();
          StringBuffer sql = new StringBuffer(" SELECT ");
          sql.append("COUNT(*) ");
          sql.append(" ");
          sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "TODET_REMARKS" + "]");
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
public boolean insertToMultiRemarks(Hashtable ht) throws Exception {
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
		String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_TODET_REMARKS]" + "("
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
public boolean deleteToMultiRemarks(Hashtable ht) throws Exception {
    boolean delete = false;
    java.sql.Connection con = null;
    try {
            con = DbBean.getConnection();
            StringBuffer sql = new StringBuffer(" DELETE ");
            sql.append(" ");
            sql.append(" FROM " + "[" + ht.get("PLANT") + "_TODET_REMARKS]");
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

public List selectRemarks(String query, Hashtable ht) throws Exception {
	List remarksList = new ArrayList();

	java.sql.Connection con = null;
	try {

		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
				+ "[" + ht.get("PLANT") + "_" + "TODET_REMARKS" + "]");
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
}
