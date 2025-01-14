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
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class LoanDetDAO extends BaseDAO {

	public static String TABLE_NAME = "LOANDET";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.LOANDETDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.LOANDETDAO_PRINTPLANTMASTERLOG;

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

	public LoanDetDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "LOANDET" + "]";
	}

	public LoanDetDAO() {
		_StrUtils = new StrUtils();
	}

	

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;

		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ TABLE_NAME);
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			this.mLogger.query(this.printQuery, sql.toString());

			map = getRowOfData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			this.mLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return map;
	}

	public ArrayList selectLoanDet(String query, Hashtable ht) throws Exception {
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
			this.mLogger.query(this.printQuery, sql.toString());
			alData = selectData(con, sql.toString());
			TABLE_NAME = "LOANDET";
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

	public ArrayList selectLoanDet(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_LOANDET]");
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
			TABLE_NAME = "LOANDET";
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

	public ArrayList selectLoanDet(String query, Hashtable ht, String extCond,
			String plant) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ plant + "_" + "LOANDET" + "] A");
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
			TABLE_NAME = "LOANDET";
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

	public boolean insertLoanDet(Hashtable ht) throws Exception {
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_LOANDET]"
					+ "(" + FIELDS.substring(0, FIELDS.length() - 1)
					+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1)
					+ ")";

			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);
			TABLE_NAME = "LOANDET";
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
		java.sql.Connection con = com.track.gates.DbBean.getConnection();
		try {
			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_LOANDET]");
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
	
	

	public boolean delete(java.util.Hashtable ht) throws Exception {

		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_LOANDET]");
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

	public boolean isExisit(Hashtable ht, String extCond) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_LOANDET]");
			sql.append(" WHERE  " + formCondition(ht));

			if (extCond.length() > 0)
				sql.append(" and " + extCond);

			this.mLogger.query(this.printQuery, sql.toString());

			flag = isExists(con, sql.toString());
			TABLE_NAME = "LOANDET";
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
	
	 public boolean isExisitLoanMultiRemarks(Hashtable ht) throws Exception {
         this.mLogger.log(1, this.getClass() + " isExisit()");
         boolean flag = false;
         java.sql.Connection con = null;
         try {
                 con = com.track.gates.DbBean.getConnection();
                 StringBuffer sql = new StringBuffer(" SELECT ");
                 sql.append("COUNT(*) ");
                 sql.append(" ");
                 sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "LOANDET_REMARKS" + "]");
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
	 
	 public boolean deleteLoanMultiRemarks(Hashtable ht) throws Exception {
         boolean delete = false;
         java.sql.Connection con = null;
         try {
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer(" DELETE ");
                 sql.append(" ");
                 sql.append(" FROM " + "[" + ht.get("PLANT") + "_LOANDET_REMARKS]");
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

	public String getNextLoanOrderLineNo(String aOrdno, String aPlant)
			throws Exception {
		String q = "";
		String result = "";

		Connection con = com.track.gates.DbBean.getConnection();
		try {
			Statement stmt = con.createStatement();
			q = "select max(cast(ordlnno as int))+1 from " + "[" + aPlant + "_"
					+ "LOANDET" + "]" + " where ORDNO = '" + aOrdno + "'";
			this.mLogger.query(this.printQuery, q.toString());
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				result = rs.getString(1);
				if (result == null) {
					result = "1";
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			DbBean.writeError("LOANDET", "listDODET()", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return result;
	}

	public ArrayList selectForReport(String query, Hashtable ht)
			throws Exception {

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

	public ArrayList getLoanOrderPickDetails(String plant, String orderno)
			throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sQry = new StringBuffer(
			"select a.ordno,a.ordlnno,a.item,isnull(a.qtyor,0) as qtyor,isnull(a.qtyrc,0) as qtyrc,isnull(a.qtyis,0) as qtyis,isnull(a.userfld2,'') as ref,isnull(a.userfld3,'') as custname,");
			sQry.append(" isnull(name,'') contactname,isnull(b.telno,'') as telno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3 ");
			sQry.append(" from  " + "[" + plant + "_" + "loandet" + "]" + " a,"
					+ "[" + plant + "_" + "loan_assignee_mst" + "]" + " b");
			sQry.append(" where a.plant='" + plant + "' and a.ordno like '"
					+ orderno + "%'  and a.userfld3=b.cname"); 

			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "LOANDET";
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

	public ArrayList getLoanOrderReceivingDetails(String plant, String orderno)
			throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sQry = new StringBuffer(
			"select a.ordno,a.ordlnno,a.item,isnull(a.qtyor,0) as qtyor,isnull(a.qtyrc,0) as qtyrc,isnull(a.qtyis,0) as qtyis,isnull(a.userfld2,'') as ref,isnull(a.userfld3,'') as custname,");
			sQry.append(" isnull(name,'') contactname,isnull(b.telno,'') as telno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3 ");
			sQry.append(" from  " + "[" + plant + "_" + "loandet" + "]" + " a,"
					+ "[" + plant + "_" + "loan_assignee_mst" + "]" + " b");
			sQry
					.append(" where a.plant='"
							+ plant
							+ "' and a.ordno like '"
							+ orderno
							+ "%' and isnull(a.Recvstatus,'') <> 'C' and a.userfld3=b.cname");

			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "LOANDET";
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

	public ArrayList getLoanOrderItemDetails(String plant, String orderno,
			String type) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			String strStatus = "";
			if (type.equalsIgnoreCase("R")) {
				strStatus = "isnull(Recvstatus,'')";
			} else {
				strStatus = "isnull(pickStatus,'')";
			}

			StringBuffer sQry = new StringBuffer(
					"select a.ordno,ordlnno,"
							+ strStatus
							+ " as lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyrc,0) as qtyrc,a.loc as frLoc,a.loc1 as toLoc,a.CustName as custname,isnull(b.UNITMO,0) as UOM,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=UNITMO),1) UOMQTY ");
			sQry.append(" ");
			sQry.append(" from  " + "[" + plant + "_" + "loanhdr" + "]" + " a,"
					+ "[" + plant + "_" + "loandet" + "]" + " b");
			sQry.append(" where  a.plant=b.plant and a.ordno=b.ordno and a.plant='"
							+ plant + "' and a.ordno like '" + orderno + "%'  ");
			if (type.equalsIgnoreCase("P")) {
				sQry
						.append(" and isnull(b.pickstatus,'') <> 'C' order by cast(ordlnno as int) ");
			} else if (type.equalsIgnoreCase("R")) {
				sQry
						.append(" and isnull(b.Recvstatus,'') <> 'C' order by cast(ordlnno as int) ");
			}

			this.mLogger.query(this.printQuery, sQry.toString());

			al = selectData(con, sQry.toString());
			TABLE_NAME = "LOANDET";
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

	public ArrayList getLoanOrderBatchListToRecv(String plant, String ordno,
			String ordlnno, String item, String loc, String batch)
			throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			
			
			StringBuffer sQry = new StringBuffer(
					"select distinct  batch,sum(pickqty) as pickQty,isnull((select sum(recqty) as recqty from  "
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
							+ "loan_pick"
							+ "] as a"
							+ " where plant='" + plant + "' and  ");
			sQry.append(" ");
			sQry.append("  ordno ='" + ordno + "' and ordlnno='" + ordlnno
					+ "' and  item='" + item + "'  and  batch  ");
			//sQry.append(" like '%"+batch+"%'  and PickQty > 0 group by batch ORDER BY batch");
			sQry.append(" like '%'  and PickQty > 0 group by batch ORDER BY batch");
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
public ArrayList getNewLoanOrderBatchListToRecv(String plant, String ordno,
			String ordlnno, String item, String loc, String batch)
			throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			
			
			StringBuffer sQry = new StringBuffer(
					"select distinct  batch,sum(pickqty) as pickQty,isnull((select sum(recqty) as recqty from  "
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
							+ "loan_pick"
							+ "] as a"
							+ " where plant='" + plant + "' and  ");
			sQry.append(" ");
			sQry.append("  ordno ='" + ordno + "' and ordlnno='" + ordlnno
					+ "' and  item='" + item + "'  and  batch  ");
			sQry.append(" like '%"+batch+"%'" );
			sQry.append("and PickQty > 0 group by batch ORDER BY batch");
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

	public Boolean removeOrderDetails(String plant2, String dono)
			throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant2 + "_"
					+ "LOANDET] WHERE ORDNO='" + dono + "' AND PLANT='"
					+ plant2 + "'";
			this.mLogger.query(this.printQuery, sql.toString());
			this.DeleteRow(connection, sql);
			return Boolean.valueOf(true);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			return Boolean.valueOf(false);
		} finally {
			DbBean.closeConnection(connection);
		}
	}
        
    
    public ArrayList getLoanDetailToPrint(String plant, String orderno)
            throws Exception {

    java.sql.Connection con = null;
    ArrayList al = new ArrayList();
    try {
            con = com.track.gates.DbBean.getConnection();

            boolean flag = false;

            StringBuffer sQry = new StringBuffer(
                           " SELECT ORDLNNO,ITEM,ITEMDESC,UNITMO,QTYOR,ORDNO,ISNULL(QTYIS,0)AS QTYIS,ISNULL(PICKSTATUS,0)AS PICKSTATUS,ISNULL((SELECT EMPNO FROM ["+plant+"_LOANHDR] WHERE ordno=a.ordno),'')AS EMPLOYEE,ISNULL((SELECT ORDERTYPE FROM ["+plant+"_LOANHDR] WHERE ordno=a.ordno),'')AS ORDERTYPE,ISNULL(QTYRC,0)AS QTYRC,ISNULL((SELECT JobNum FROM ["+plant+"_LOANHDR] WHERE ordno=a.ordno),'')AS JUBNUM FROM "
                                            + "[" + plant + "_" + "LOANDET" + "] a " + "");
            sQry.append(" where plant='"
                                            + plant
                                            + "' and ordno = '"
                                            + orderno  
                                            + "'" + " and PICKSTATUS != 'N' ");

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

    public ArrayList getLoanDetailWithPrice(String ITEM,String CUST_NAME,String JOBNO,String plant, String orderno,String afrmDate,
			String atoDate)
            throws Exception {

    java.sql.Connection con = null;
    ArrayList al = new ArrayList();
    String sCondition = "";
    try {
            con = com.track.gates.DbBean.getConnection();
          
            if (ITEM.length() > 0 ) {
		        if (ITEM.indexOf("%") != -1) {
		        	ITEM = ITEM.substring(0, ITEM.indexOf("%") - 1);
		        }
	            sCondition = " and replace(ITEM,' ','') like '%"+ new StrUtils().InsertQuotes(ITEM.replaceAll(" ","")) + "%' ";
	        }
            
            if (CUST_NAME.length() > 0 ) {
		        if (CUST_NAME.indexOf("%") != -1) {
		        	CUST_NAME = CUST_NAME.substring(0, CUST_NAME.indexOf("%") - 1);
		        }
	            sCondition = " and replace(b.custName,' ','') like '%"+ new StrUtils().InsertQuotes(CUST_NAME.replaceAll(" ","")) + "%' ";
	        }
            
            if (JOBNO.length() > 0 ) {
		        if (JOBNO.indexOf("%") != -1) {
		        	JOBNO = JOBNO.substring(0, JOBNO.indexOf("%") - 1);
		        }
	            sCondition = " and replace(b.JobNum,' ','') like '%"+ new StrUtils().InsertQuotes(JOBNO.replaceAll(" ","")) + "%' ";
	        }
            
            if (afrmDate.length() > 0) {
				sCondition = sCondition + " AND  A.TRANDATE  >= '" + afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND A.TRANDATE <= '" + atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  A.TRANDATE  <= '" + atoDate
							+ "'  ";
				}
			}

            boolean flag = false;
          
            StringBuffer sQry = new StringBuffer(
            		" SELECT ORDLNNO,ITEM,ITEMDESC,UNITMO,QTYOR,ISNULL(b.ORDNO,'')AS ORDNO,ISNULL(QTYIS,0)AS QTYIS,ISNULL(a.RENTALPRICE,0)AS RENTALPRICE,ISNULL(b.EMPNO,0)AS EMPLOYEE,isnull(b.custName,'') customername,isnull(b.CollectionDate,'') ORDDATE,isnull(b.JobNum,'') refno,isnull(a.PRODGST,'') PRODGST,ISNULL((((a.RENTALPRICE*a.PRODGST)/100)*a.qtyor),0) as taxval,isnull(a.RENTALPRICE*a.qtyor+(a.RENTALPRICE*a.PRODGST*a.qtyor)/100,0)as totalwithtax,isnull(a.RENTALPRICE*a.qtyor,'') orderprice,isnull(a.QTYRC,0) QTYRC");
			sQry.append(" ");
			sQry.append(" from  " + "[" + plant + "_" + "loanhdr" + "]" + " b,"
					+ "[" + plant + "_" + "loandet" + "]" + " a");
			sQry.append(" where  a.plant=b.plant and a.PICKSTATUS != 'N' and a.ordno=b.ordno and a.plant='"
					+ plant + "' and a.ordno like '" + orderno + "%'  " + sCondition);
           
                                         
         
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

    
    public String getBatchQtyWithRecv(String plant, String Orderno,String itemno,String lnnum)
	throws Exception {
    	String q = "";
    	String result = "";

    	Connection con = com.track.gates.DbBean.getConnection();
    	try {
    		Statement stmt = con.createStatement();
    	
    		StringBuffer sQry = new StringBuffer("SELECT (B.PICKQTY-A.RECQTY) AS RECQTY FROM ");
    		sQry.append("(SELECT SUM(RECQTY)AS RECQTY,PONO,LNNO,ITEM,BATCH FROM ["+ plant +"_RECVDET] WHERE  ");
    		sQry.append(" PONO='" + Orderno + "' AND LNNO='" +lnnum +"' AND ITEM = '" + itemno + "'  AND BATCH='NOBATCH'   ");
    		sQry.append(" GROUP BY  PLANT,PONO,LNNO,ITEM,BATCH) A, ");
    		sQry.append("(SELECT SUM(PICKQTY) AS PICKQTY,ORDNO,ORDLNNO,ITEM,BATCH  FROM ["+ plant +"_LOAN_PICK]  WHERE   ");
    		sQry.append(" ORDNO='" + Orderno + "' AND ORDLNNO='" +lnnum +"' AND ITEM = '" + itemno + "'  AND BATCH='NOBATCH'  GROUP BY  ORDNO,ORDLNNO,ITEM,BATCH) B ");
    		sQry.append("   WHERE A.PONO=B.ORDNO AND A.LNNO =B.ORDLNNO AND A.ITEM=B.ITEM AND A.BATCH=B.BATCH ");
    		

    		this.mLogger.query(this.printQuery, sQry.toString());
    		ResultSet rs = stmt.executeQuery(sQry.toString());
    		while (rs.next()) {
    			result = rs.getString(1);
    			if (result == null) {
    				result = "";
    			}
    		}
    	} catch (Exception e) {
    		this.mLogger.exception(this.printLog, "", e);
    		DbBean.writeError("LOANDET", "listDODET()", e);
    	} finally {
    		DbBean.closeConnection(con);
    	}
    	return result;
    }
    
    public String getBatchQtyWithOutRecv(String plant, String Orderno,String itemno,String lnnum)
	throws Exception {
    	String q = "";
    	String result = "";

    	Connection con = com.track.gates.DbBean.getConnection();
    	try {
    		Statement stmt = con.createStatement();
    		q = " SELECT ISNULL(SUM(PICKQTY),0)AS PICKQTY  FROM [" + plant + "_"
			+ "LOAN_PICK] WHERE PLANT='" + plant
			+ "' AND ORDNO='" + Orderno + "' AND ORDLNNO='" +lnnum +"' AND ITEM = '" + itemno + "'  AND BATCH='NOBATCH' GROUP BY  PLANT,ORDNO,ORDLNNO,ITEM";
    		
    		
    		this.mLogger.query(this.printQuery, q.toString());
    		ResultSet rs = stmt.executeQuery(q.toString());
    		while (rs.next()) {
    			result = rs.getString(1);
    			if (result == null) {
    				result = "0";
    			}
    		}
    	} catch (Exception e) {
    		this.mLogger.exception(this.printLog, "", e);
    		DbBean.writeError("LOANDET", "listDODET()", e);
    	} finally {
    		DbBean.closeConnection(con);
    	}
    	return result;
    }
    
    public String getBatchQtyPicking(String plant, String itemno,String loc)
	throws Exception {
    	String q = "";
    	String result = "";

    	Connection con = com.track.gates.DbBean.getConnection();
    	try {
    		Statement stmt = con.createStatement();
    		q = " SELECT ISNULL(SUM(QTY),0)AS QTY  FROM [" + plant + "_"
			+ "INVMST] WHERE PLANT='" + plant 
			+ "' AND ITEM = '" + itemno + "'  AND LOC='" + loc + "' AND USERFLD4='NOBATCH' GROUP BY PLANT,ITEM,LOC";
    		
    		
    		this.mLogger.query(this.printQuery, q.toString());
    		ResultSet rs = stmt.executeQuery(q.toString());
    		while (rs.next()) {
    			result = rs.getString(1);
    			if (result == null) {
    				result = "0";
    			}
    		}
    	} catch (Exception e) {
    		this.mLogger.exception(this.printLog, "", e);
    		DbBean.writeError("LOANDET", "listDODET()", e);
    	} finally {
    		DbBean.closeConnection(con);
    	}
    	return result;
    }
    
    
    public boolean isExisitLOPick(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "LOAN_PICK"+ "]");
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
  
    public boolean updateLOPickTable(String query, Hashtable htCondition, String extCond)
            throws Exception {
    boolean flag = false;
    java.sql.Connection con = null;
    try {
            con = com.track.gates.DbBean.getConnection();
            StringBuffer sql = new StringBuffer(" UPDATE " + "["
                            + htCondition.get("PLANT") + "_" + "LOAN_PICK" + "]");
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
					+ ht.get("PLANT") + "_LOANHDR]" + " WHERE "
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
 
 public boolean insertLoanMultiRemarks(java.util.Hashtable ht) throws Exception {
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_LOANDET_REMARKS]" + "("
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
	
	public ArrayList selectLoanMultiRemarks(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "LOANDET_REMARKS" + "] a ");
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
	public String getBatchQtyPickingMutiUOM(String plant, String itemno,String loc,String uomqty)
			throws Exception {
		    	String q = "";
		    	String result = "";

		    	Connection con = com.track.gates.DbBean.getConnection();
		    	try {
		    		Statement stmt = con.createStatement();
		    		q = " SELECT convert(int,(ISNULL(SUM(QTY),0)/"+ uomqty +")) AS QTY  FROM [" + plant + "_"
					+ "INVMST] WHERE PLANT='" + plant 
					+ "' AND ITEM = '" + itemno + "'  AND LOC='" + loc + "' AND USERFLD4='NOBATCH' GROUP BY PLANT,ITEM,LOC";
		    		
		    		
		    		this.mLogger.query(this.printQuery, q.toString());
		    		ResultSet rs = stmt.executeQuery(q.toString());
		    		while (rs.next()) {
		    			result = rs.getString(1);
		    			if (result == null) {
		    				result = "0";
		    			}
		    		}
		    	} catch (Exception e) {
		    		this.mLogger.exception(this.printLog, "", e);
		    		DbBean.writeError("LOANDET", "listDODET()", e);
		    	} finally {
		    		DbBean.closeConnection(con);
		    	}
		    	return result;
		    }
   
}
