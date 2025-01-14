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

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.object.MultiPoEstHdr; //Resvi
import com.track.db.object.PoHdr;
import com.track.db.object.MultiPoEstDet; //Resvi
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MultiPoEstHdrDAO extends BaseDAO {

	public static String plant = "";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.POMULTIHDRDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.POMULTIHDRDAO_PRINTPLANTMASTERLOG;

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

	public MultiPoEstHdrDAO() {
		_StrUtils = new StrUtils();
	}

	public MultiPoEstHdrDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_PO_MULTI_ESTHDR" + "]";
	}

	public static String TABLE_NAME = "PO_MULTI_ESTHDR";

	public Boolean removeOrder(String plant, String POMULTIESTNO) throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant + "_" + "PO_MULTI_ESTHDR] WHERE POMULTIESTNO='"
					+ POMULTIESTNO + "' AND PLANT='" + plant + "'";
			this.mLogger.query(this.printQuery, sql.toString());
			this.DeleteRow(connection, sql);
			return Boolean.valueOf(true);
		} catch (Exception e) {
			this.mLogger.log(0, "[ERROR] : " + e);
			return Boolean.valueOf(false);
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
	}

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "PO_MULTI_ESTHDR" + "]");
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
					+ "[" + ht.get("PLANT") + "_" + "PO_MULTI_ESTHDR" + "]");
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
        
    public String getOrderTypeForPO(String aPlant, String aPOMULTIESTNO) throws Exception {
            String OrderType = "";

            Hashtable ht = new Hashtable();
            ht.put("PLANT", aPlant);
            ht.put("POMULTIESTNO", aPOMULTIESTNO);
            String query = " case  isnull(ordertype,'')  when '' then 'INBOUND ORDER' else upper(ordertype) end AS ORDERTYPE  ";
            Map m = selectRow(query, ht);
            OrderType = (String) m.get("ORDERTYPE");
            return OrderType;
    }
    
    
    
    public List getPreviousOrderDetails(Hashtable ht, String rows) throws Exception {
    	java.sql.Connection con = null;
		List<Map<String, String>> ordersList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="", condition = "", unitCostCond = "";
		List<String> args = null;
		try {
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			
			/*Storing all the query param argument in list sequentially*/
			condition += " WHERE A.PLANT = ? ";
			args.add((String)ht.get("PLANT"));
			if(!((String)ht.get("ITEM")).equalsIgnoreCase("")) {
				condition += " AND B.ITEM = ? ";
				args.add((String)ht.get("ITEM"));
			}
			if(!((String)ht.get("VENDNO")).equalsIgnoreCase("")) {
				condition += " AND B.CustCode = ? ";
				args.add((String)ht.get("VENDNO"));
			}
			
			unitCostCond = "CASE WHEN UNITMO='" + ht.get("UOM") + "' THEN UNITCOST ELSE ISNULL((select ISNULL(QPUOM,1) from [" + ht.get("PLANT") + "_UOM] where UOM='" + ht.get("UOM") + "'),1) * (UNITCOST / (ISNULL((select ISNULL(QPUOM,1) from ["+ ht.get("PLANT") +"_UOM] where UOM=UNITMO),1))) END  AS UNITCOST";
			
			query = "SELECT TOP "+ rows +" a.POMULTIESTNO,B.CustCode,c.VNAME,ITEM,CollectionDate,"+unitCostCond+" FROM [" + ht.get("PLANT") + "_PO_MULTI_ESTHDR] a JOIN [" + ht.get("PLANT") + "_PO_MULTI_ESTDET] B ON a.POMULTIESTNO = b.POMULTIESTNO"
					+" JOIN [" + ht.get("PLANT") + "_VENDMST] c on B.CustCode = c.VENDNO "
					+ condition
					+" ORDER BY CAST((SUBSTRING(CollectionDate,7,4) + '-' + SUBSTRING(CollectionDate,4,2) + '-' + SUBSTRING(CollectionDate,1,2))AS DATE) DESC";

			PreparedStatement ps = con.prepareStatement(query);
			this.mLogger.query(this.printQuery, query);			
			ordersList = selectData(ps, args);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}		
		return ordersList;
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
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "PO_MULTI_ESTHDR" + "]");
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
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "PO_MULTI_ESTHDR" + "]");
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

	public ArrayList selectMultiPoEstHdr(String query, Hashtable ht) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "PO_MULTI_ESTHDR" + "]");
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

	public ArrayList selectPoHdr(String query, Hashtable ht, String extCond)
			throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "PO_MULTI_ESTHDR" + "]");

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

	
        
    public ArrayList selectPoHdrNewOrdersByItem(String query, Hashtable ht,
                    String extCond) throws Exception {
//            boolean flag = false;
            ArrayList alData = new ArrayList();
            java.sql.Connection con = null;

            StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
                            + ht.get("PLANT") + "_" + "PO_MULTI_ESTHDR" + "] a, [" + ht.get("PLANT")
                            + "_" + "PO_MULTI_ESTDET" + "] b WHERE a.POMULTIESTNO=b.POMULTIESTNO AND a.PLANT='"+ ht.get("PLANT") + "' AND b.ITEM like'"+ ht.get("ITEM") + "%' ");

//            String conditon = "";

            try {
                    con = com.track.gates.DbBean.getConnection();
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

	public ArrayList selectPoHdrReciving(String query, Hashtable ht,
			String extCond) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "PO_MULTI_ESTHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "VENDMST" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.POMULTIESTNO like'"
						+ ht.get("POMULTIESTNO") + "%' and a.custname=b.vname";

				sql.append(conditon);

			}
			extCond = "";
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

	public ArrayList selectSupplierHdrReciving(String query, Hashtable ht,
			String extCond) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "PO_MULTI_ESTHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "VENDMST" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.POMULTIESTNO ='"
						+ ht.get("POMULTIESTNO") + "' and a.CustCode=b.vendno";

				sql.append(conditon);

			}
			extCond = "";
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

	 //not by Draft - azees
	public ArrayList selectSupplierHdrRecivingWithext(String query, Hashtable ht,
			String extCond) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "PO_MULTI_ESTHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "VENDMST" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.POMULTIESTNO ='"
						+ ht.get("POMULTIESTNO") + "' and a.CustCode=b.vendno";

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

	
	public boolean insertPoHdr(Hashtable ht) throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "PO_MULTI_ESTHDR" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("incoming order created already");
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}

	
	public boolean updatePO(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + "PO_MULTI_ESTHDR" + "]");
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

	public String getCurrencyID(String aPlant, String aPOMULTIESTNO) throws Exception {
		String currencyid = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("POMULTIESTNO", aPOMULTIESTNO);

		String query = " isnull(Currencyid,'') as currencyid ";
		
		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			currencyid = (String) m.get("currencyid");
		} else {
			currencyid = "";
		}
		if (currencyid.equalsIgnoreCase(null) || currencyid.length() == 0) {
			currencyid = "";
		}
		return currencyid;
	}
        
	
	public MultiPoEstHdr getPoHdrByPono(String plant, String POMULTIESTNO)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    MultiPoEstHdr multiPoEstHdr=new MultiPoEstHdr();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,POMULTIESTNO,STATUS,ISNULL(SAMEASEXPDATE,'') SAMEASEXPDATE,ISNULL(ISPRODUCTASSIGNEDSUPPLIER,'') ISPRODUCTASSIGNEDSUPPLIER,ISNULL(PROJECTID,0) PROJECTID,ISNULL(TRANSPORTID,0) TRANSPORTID,ISNULL(EMPNO,0) EMPNO, ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS,ORDERTYPE,DELDATE,CRAT,CRBY,UPAT,UPBY,JobNum,CollectionDate,CollectionTime,Remark1,Remark2,SHIPPINGID,SHIPPINGCUSTOMER,INBOUND_GST,STATUS_ID,REMARK3,INCOTERMS,PAYMENTTYPE,DELIVERYDATEFORMAT,ISNULL(PURCHASE_LOCATION,'') AS PURCHASE_LOCATION,ORDER_STATUS,REVERSECHARGE,GOODSIMPORT,LOCALEXPENSES,ISNULL(TAXID,'0') AS TAXID,ISNULL(ISTAXINCLUSIVE,'0') AS ISTAXINCLUSIVE FROM ["+ plant +"_PO_MULTI_ESTHDR] WHERE POMULTIESTNO='"+POMULTIESTNO+"'";
			System.out.println(query);
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, multiPoEstHdr);
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
		return multiPoEstHdr;
	}
	

	public MultiPoEstHdr getPoHdrByDraft(String plant, String POMULTIESTNO,String orderstatus)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "",cond="";
	    MultiPoEstHdr multiPoEstHdr=new MultiPoEstHdr();
	    if(POMULTIESTNO.equalsIgnoreCase("")) {
	    	cond = "WHERE ORDER_STATUS='"+orderstatus+"'" ;
	    }else {
	    	cond = "WHERE POMULTIESTNO='"+POMULTIESTNO+"' and ORDER_STATUS='"+orderstatus+"' " ;
	    }
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT TOP 1 PLANT,ID,POMULTIESTNO,STATUS,ISNULL(SAMEASEXPDATE,'') SAMEASEXPDATE,ISNULL(ISPRODUCTASSIGNEDSUPPLIER,'') ISPRODUCTASSIGNEDSUPPLIER,ISNULL(PROJECTID,0) PROJECTID,ISNULL(TRANSPORTID,0) TRANSPORTID,ISNULL(EMPNO,0) EMPNO, ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS,ORDERTYPE,DELDATE,CRAT,CRBY,UPAT,UPBY,JobNum,CollectionDate,CollectionTime,Remark1,Remark2,SHIPPINGID,SHIPPINGCUSTOMER,INBOUND_GST,STATUS_ID,REMARK3,INCOTERMS,PAYMENTTYPE,DELIVERYDATEFORMAT,ISNULL(PURCHASE_LOCATION,'') AS PURCHASE_LOCATION,ORDER_STATUS,REVERSECHARGE,GOODSIMPORT,LOCALEXPENSES,ISNULL(TAXID,'0') AS TAXID,ISNULL(ISTAXINCLUSIVE,'0') AS ISTAXINCLUSIVE FROM ["+ plant +"_PO_MULTI_ESTHDR] "+cond+" ";
			System.out.println(query);
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, multiPoEstHdr);
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
		return multiPoEstHdr;
	}
	
	


	public String getOrderType(String aPlant, String aPOMULTIESTNO) throws Exception {
		String orderType = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("POMULTIESTNO", aPOMULTIESTNO);

		String query = " isnull(ORDERTYPE,'') as ORDERTYPE ";
		
		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			orderType = (String) m.get("ORDERTYPE");
		} else {
			orderType = "";
		}
		if (orderType.equalsIgnoreCase(null) || orderType.length() == 0) {
			orderType = "";
		}
		return orderType;
	}
	
	 
	 
	
		
 

		
		
		 public ArrayList getSupplierList(String plant,String custname) throws Exception {

		        java.sql.Connection con = null;
		        ArrayList al = new ArrayList();
		        try {
		                con = com.track.gates.DbBean.getConnection();
		                UserLocUtil userLocUtil = new UserLocUtil();
		                userLocUtil.setmLogger(mLogger);
		                         
		               
		                String sQry = "select distinct isnull(custcode,'') custcode ,isnull(custname,'') custname  from "
		                                + "["
		                                + plant
		                                + "_"
		                                + "multiPoEstHdr] where plant='"
		                                + plant
		                                + "' and custcode like '"+custname+"%' or custname like  '"+custname+"%' " 
		                                + " ORDER BY custcode ";
		                this.mLogger.query(this.printQuery, sQry);
		                System.out.println(sQry);

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

        

     
    public String getSuppliercode(String aPlant, String aPOMULTIESTNO) throws Exception {
		String custcode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("POMULTIESTNO", aPOMULTIESTNO);

		String query = " isNull(CustCode,'') as custcode ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht);

		custcode = (String) m.get("custcode");
		return custcode;
	}	
		    
    
        
    public String getDiscountSupplierCode(String plant, String POMULTIESTNO) throws Exception {
	      java.sql.Connection con = null;
	      String suppliercode = "";
	     String SqlQuery="";
	      try {
	        con = DbBean.getConnection();
	        SqlQuery = " select isnull(custcode,'') vendno FROM ["+plant+"_PO_MULTI_ESTHDR]  WHERE POMULTIESTNO='"+POMULTIESTNO+"'";
	        System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery);
	       suppliercode = (String) m.get("vendno");
	       
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return suppliercode;
	  }
    
    
    
    
    
    
    public boolean addMultiPoEstHdr(MultiPoEstHdr multiPoEstHdr) throws Exception {
 		boolean insertFlag = false;
// 		boolean flag = false;
// 		int HdrId = 0;
 		Connection connection = null;
 		PreparedStatement ps = null;
 	    String query = "";
 		try {	    
 			connection = DbBean.getConnection();
 			query = "INSERT INTO ["+multiPoEstHdr.getPLANT()+"_PO_MULTI_ESTHDR]" + 
 					"           ([PLANT]" + 
 					"           ,[POMULTIESTNO]" +
 					"           ,[STATUS]" +
 					"           ,[ORDERTYPE]" +
 					"           ,[DELDATE]" +
 					"           ,[CRAT]" +
 					"           ,[CRBY]" +
 					"           ,[UPAT]" +
 					"           ,[UPBY]" +
 					"           ,[JobNum]" +
 					"           ,[CollectionDate]" +
 					"           ,[CollectionTime]" +
 					"           ,[Remark1]" +
 					"           ,[Remark2]" +
 					"           ,[SHIPPINGID]" +
 					"           ,[SHIPPINGCUSTOMER]" +
 					"           ,[INBOUND_GST]" +
 					"           ,[STATUS_ID]" +
 					"           ,[REMARK3]" +
 					"           ,[INCOTERMS]" +
 					"           ,[PAYMENTTYPE]" +
 					"           ,[DELIVERYDATEFORMAT]" +
 					"           ,[PURCHASE_LOCATION]" +
 					"           ,[REVERSECHARGE]" +
 					"           ,[ORDER_STATUS]" +
 					"           ,[LOCALEXPENSES]" +
 					"           ,[TAXID]" +
 					"           ,[ISTAXINCLUSIVE]" +
 					"           ,[PROJECTID]" +
 					"           ,[TRANSPORTID]" +
 					"           ,[PAYMENT_TERMS]" +
 					"           ,[EMPNO]" +
 					"           ,[GOODSIMPORT]"+
 					"           ,[SAMEASEXPDATE]"+
 					"           ,[ISPRODUCTASSIGNEDSUPPLIER]"+
 		          	"           ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 			if(connection != null){
	 			ps = connection.prepareStatement(query);
	 			ps.setString(1, multiPoEstHdr.getPLANT());
	 			ps.setString(2, multiPoEstHdr.getPOMULTIESTNO());
	 			ps.setString(3, multiPoEstHdr.getSTATUS());
	 			ps.setString(4, multiPoEstHdr.getORDERTYPE());
	 			ps.setString(5, multiPoEstHdr.getDELDATE());
	 			ps.setString(6, multiPoEstHdr.getCRAT());
	 			ps.setString(7, multiPoEstHdr.getCRBY());
	 			ps.setString(8, multiPoEstHdr.getUPAT());
	 			ps.setString(9, multiPoEstHdr.getUPBY());
	 			ps.setString(10, multiPoEstHdr.getJobNum());
	 			ps.setString(11, multiPoEstHdr.getCollectionDate());
	 			ps.setString(12, multiPoEstHdr.getCollectionTime());
	 			ps.setString(13, multiPoEstHdr.getRemark1());
	 			ps.setString(14, multiPoEstHdr.getRemark2());
	 			ps.setString(15, multiPoEstHdr.getSHIPPINGID());
	 			ps.setString(16, multiPoEstHdr.getSHIPPINGCUSTOMER());
	 			ps.setDouble(17, multiPoEstHdr.getINBOUND_GST());
	 			ps.setString(18, multiPoEstHdr.getSTATUS_ID());
	 			ps.setString(19, multiPoEstHdr.getREMARK3());
	 			ps.setString(20, multiPoEstHdr.getINCOTERMS());
	 			ps.setString(21, multiPoEstHdr.getPAYMENTTYPE());
	 			ps.setDouble(22, multiPoEstHdr.getDELIVERYDATEFORMAT());
	 			ps.setString(23, multiPoEstHdr.getPURCHASE_LOCATION());
	 			ps.setDouble(24, multiPoEstHdr.getREVERSECHARGE());
	 			ps.setString(25, multiPoEstHdr.getORDER_STATUS());
	 			ps.setDouble(26, multiPoEstHdr.getLOCALEXPENSES());
	 			ps.setInt(27, multiPoEstHdr.getTAXID());
	 			ps.setDouble(28, multiPoEstHdr.getISTAXINCLUSIVE());
	 			ps.setDouble(29, multiPoEstHdr.getPROJECTID());
	 			ps.setDouble(30, multiPoEstHdr.getTRANSPORTID());
	 			ps.setString(31, multiPoEstHdr.getPAYMENT_TERMS());
	 			ps.setString(32, multiPoEstHdr.getEMPNO());
	 			ps.setDouble(33, multiPoEstHdr.getGOODSIMPORT());
	 			ps.setDouble(34, multiPoEstHdr.getSAMEASEXPDATE());
	 			ps.setDouble(35, multiPoEstHdr.getISPRODUCTASSIGNEDSUPPLIER());


 			   int count=ps.executeUpdate();
 			   if(count>0)
 			   {
 				   insertFlag = true;
 			   }
 			   else
 			   {
 				   throw new SQLException("Creating Purchase Order failed, no rows affected.");
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
    
    
    public boolean updatePoHdr(MultiPoEstHdr multiPoEstHdr) throws Exception {
		boolean updateFlag = false;
//		boolean flag = false;
//		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "UPDATE ["+multiPoEstHdr.getPLANT()+"_PO_MULTI_ESTHDR] SET" + 
					"           [PLANT] = ?" + 
					"           ,[POMULTIESTNO] = ?" +
					"           ,[STATUS] = ?" +
					"           ,[ORDERTYPE] = ?" +
					"           ,[DELDATE] = ?" +
					"           ,[UPAT] = ?" +
					"           ,[UPBY] = ?" +
					"           ,[JobNum] = ?" +
					"           ,[CollectionDate] = ?" +
					"           ,[CollectionTime] = ?" +
					"           ,[Remark1] = ?" +
					"           ,[Remark2] = ?" +
					"           ,[SHIPPINGID] = ?" +
					"           ,[SHIPPINGCUSTOMER] = ?" +
					"           ,[INBOUND_GST] = ?" +
					"           ,[STATUS_ID] = ?" +
					"           ,[REMARK3] = ?" +
					"           ,[INCOTERMS] = ?" +
					"           ,[PAYMENTTYPE] = ?" +
					"           ,[DELIVERYDATEFORMAT] = ?" +
					"           ,[PURCHASE_LOCATION] = ?" +
					"           ,[REVERSECHARGE] = ?" +
					"           ,[ORDER_STATUS] = ?" +
					"           ,[LOCALEXPENSES] = ?" +
					"           ,[TAXID] = ?" +
					"           ,[ISTAXINCLUSIVE] = ?" +
					"           ,[PROJECTID] = ?" +
					"           ,[TRANSPORTID] = ?" +
					"           ,[PAYMENT_TERMS] = ?" +
					"           ,[EMPNO] = ?" +
					"           ,[GOODSIMPORT] = ?"+
					"           ,[SAMEASEXPDATE] = ?"+
					"           ,[ISPRODUCTASSIGNEDSUPPLIER] = ?"+
		         	"            WHERE [POMULTIESTNO] = ?";
			if(connection != null){
			   ps = connection.prepareStatement(query);
			   ps.setString(1, multiPoEstHdr.getPLANT());
	 			ps.setString(2, multiPoEstHdr.getPOMULTIESTNO());
	 			ps.setString(3, multiPoEstHdr.getSTATUS());
	 			ps.setString(4, multiPoEstHdr.getORDERTYPE());
	 			ps.setString(5, multiPoEstHdr.getDELDATE());
	 			ps.setString(6, multiPoEstHdr.getUPAT());
	 			ps.setString(7, multiPoEstHdr.getUPBY());
	 			ps.setString(8, multiPoEstHdr.getJobNum());
	 			ps.setString(9, multiPoEstHdr.getCollectionDate());
	 			ps.setString(10, multiPoEstHdr.getCollectionTime());
	 			ps.setString(11, multiPoEstHdr.getRemark1());
	 			ps.setString(12, multiPoEstHdr.getRemark2());
	 			ps.setString(13, multiPoEstHdr.getSHIPPINGID());
	 			ps.setString(14, multiPoEstHdr.getSHIPPINGCUSTOMER());
	 			ps.setDouble(15, multiPoEstHdr.getINBOUND_GST());
	 			ps.setString(16, multiPoEstHdr.getSTATUS_ID());
	 			ps.setString(17, multiPoEstHdr.getREMARK3());
	 			ps.setString(18, multiPoEstHdr.getINCOTERMS());
	 			ps.setString(19, multiPoEstHdr.getPAYMENTTYPE());
	 			ps.setDouble(20, multiPoEstHdr.getDELIVERYDATEFORMAT());
	 			ps.setString(21, multiPoEstHdr.getPURCHASE_LOCATION());
	 			ps.setDouble(22, multiPoEstHdr.getREVERSECHARGE());
	 			ps.setString(23, multiPoEstHdr.getORDER_STATUS());
	 			ps.setDouble(24, multiPoEstHdr.getLOCALEXPENSES());
	 			ps.setInt(25, multiPoEstHdr.getTAXID());
	 			ps.setDouble(26, multiPoEstHdr.getISTAXINCLUSIVE());
	 			ps.setInt(27, multiPoEstHdr.getPROJECTID());
	 			ps.setInt(28, multiPoEstHdr.getTRANSPORTID());
	 			ps.setString(29, multiPoEstHdr.getPAYMENT_TERMS());
	 			ps.setString(30, multiPoEstHdr.getEMPNO());
	 			ps.setDouble(31, multiPoEstHdr.getGOODSIMPORT());
	 			ps.setDouble(32, multiPoEstHdr.getSAMEASEXPDATE());
	 			ps.setDouble(33, multiPoEstHdr.getISPRODUCTASSIGNEDSUPPLIER());
	 			
	 			ps.setString(34, multiPoEstHdr.getPOMULTIESTNO());
	 			
			   int count=ps.executeUpdate();
			   if(count>0)
			   {
				   updateFlag = true;
			   }
			   else
			   {
				   throw new SQLException("Updateing Multi Purchase Estimate Order failed, no rows affected.");
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
		return updateFlag;
	}
    
	public MultiPoEstHdr getPoHdrByPOMULTIESTNO(String plant, String POMULTIESTNO)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    MultiPoEstHdr multiPoEstHdr =new MultiPoEstHdr();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,POMULTIESTNO,STATUS,ISNULL(PROJECTID,0) PROJECTID,ISNULL(TRANSPORTID,0) TRANSPORTID,ISNULL(EMPNO,0) EMPNO, ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS,ORDERTYPE,DELDATE,CRAT,CRBY,UPAT,UPBY,JobNum,CollectionDate,CollectionTime,Remark1,Remark2,SHIPPINGID,SHIPPINGCUSTOMER,INBOUND_GST,STATUS_ID,REMARK3,INCOTERMS,PAYMENTTYPE,DELIVERYDATEFORMAT,ISNULL(PURCHASE_LOCATION,'') AS PURCHASE_LOCATION,ORDER_STATUS,REVERSECHARGE,GOODSIMPORT,LOCALEXPENSES,ISNULL(TAXID,'0') AS TAXID FROM ["+ plant +"_PO_MULTI_ESTHDR] WHERE POMULTIESTNO='"+POMULTIESTNO+"'";
			System.out.println(query);
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, multiPoEstHdr);
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
		return multiPoEstHdr;
	}
	
	public List<MultiPoEstHdr> getPoHdrSummary(Hashtable ht, String afrmDate, String atoDate) throws Exception {
//		   boolean flag = false;
//		   int journalHdrId = 0;
		   Connection connection = null;
		   PreparedStatement ps = null;
		   String query = "", fields = "", dtCondStr = "";
		   List<MultiPoEstHdr> MultiPoEstHeader = new ArrayList<MultiPoEstHdr>();
		   List<String> args = null;
		   try {
			   args = new ArrayList<String>();
		   connection = DbBean.getConnection();
			   query = "SELECT PLANT,ID,POMULTIESTNO,STATUS,ISNULL(SAMEASEXPDATE,'') SAMEASEXPDATE,ISNULL(ISPRODUCTASSIGNEDSUPPLIER,'') ISPRODUCTASSIGNEDSUPPLIER,ISNULL(PROJECTID,0) PROJECTID,ISNULL(TRANSPORTID,0) TRANSPORTID,ISNULL(EMPNO,0) EMPNO,ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS,ORDERTYPE,DELDATE,CRAT,CRBY,UPAT,UPBY,JobNum,CollectionDate,CollectionTime,Remark1,Remark2,SHIPPINGID,SHIPPINGCUSTOMER,INBOUND_GST,STATUS_ID,REMARK3,INCOTERMS,PAYMENTTYPE,DELIVERYDATEFORMAT,ISNULL(PURCHASE_LOCATION,'') AS PURCHASE_LOCATION,ORDER_STATUS,REVERSECHARGE,GOODSIMPORT,ISTAXINCLUSIVE,LOCALEXPENSES,ISNULL(TAXID,'0') AS TAXID FROM ["+ ht.get("PLANT") +"_PO_MULTI_ESTHDR] WHERE ";
			   Enumeration enum1 = ht.keys();
				for (int i = 0; i < ht.size(); i++) {
					String key = StrUtils.fString((String) enum1.nextElement());
					String value = StrUtils.fString((String) ht.get(key));
					fields += key + "=? AND ";
					args.add(value);
				}			
				query += fields.substring(0, fields.length() - 5);
				
				dtCondStr =" AND ISNULL(CollectionDate,'')<>'' AND CAST((SUBSTRING(CollectionDate, 7, 4) + '-' + SUBSTRING(CollectionDate, 4, 2) + '-' + SUBSTRING(CollectionDate, 1, 2)) AS date)";
				
				if (afrmDate.length() > 0) {
					query +=  dtCondStr + "  >= '" + afrmDate + "' ";
	  				if (atoDate.length() > 0) {
	  					query += dtCondStr+ " <= '" + atoDate + "' ";
	  				}
				} else {
	  				if (atoDate.length() > 0) {
	  					query += dtCondStr+ " <= '" + atoDate + "' ";
	  				}
				}
				
				query += " ORDER BY  CAST((SUBSTRING(CollectionDate,7,4) + '-' + SUBSTRING(CollectionDate,4,2) + '-' + SUBSTRING(CollectionDate,1,2))AS DATE) DESC,POMULTIESTNO DESC";
				this.mLogger.query(this.printQuery, query);
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
						MultiPoEstHdr multiPoEstHdr = new MultiPoEstHdr();
						ResultSetToObjectMap.loadResultSetIntoObject(rst, multiPoEstHdr);
						MultiPoEstHeader.add(multiPoEstHdr);
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
		   return MultiPoEstHeader;
		}
	
	public int Ordercount(String plant, String afrmDate, String atoDate)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int Ordercount = 0;
		String sCondition = "";
		String dtCondStr =    " AND (SUBSTRING(CollectionDate, 7, 4) + '-' + SUBSTRING(CollectionDate, 4, 2) + '-' + SUBSTRING(CollectionDate, 1, 2))";
		if (afrmDate.length() > 0) {
          	sCondition = " " + dtCondStr +"  >= '" + afrmDate
          	+ "'  ";
          	if (atoDate.length() > 0) {
          		sCondition = sCondition + " " + dtCondStr +" <= '" + atoDate
          		+ "'  ";
          	}
          }
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
					+ "PO_MULTI_ESTHDR" + "]" + " WHERE " + IConstants.PLANT
					+ " = '" + plant.toUpperCase() + "'"+ sCondition;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Ordercount = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return Ordercount;
	}
    
}
