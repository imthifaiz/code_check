package com.track.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.db.util.ItemMstUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ShipHisDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.SHIPHISDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.SHIPHISDAO_PRINTPLANTMASTERLOG;

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

	StrUtils _StrUtils = null;

	public ShipHisDAO() {
		_StrUtils = new StrUtils();
	}

	public static String plant = "";
	public static String TABLE_NAME = "SHIPHIS";

	public ShipHisDAO(String plant) {
//		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "SHIPHIS" + "]";
	}

	public boolean isExisit(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql
					.append(" FROM " + "[" + ht.get("PLANT") + "_" + "SHIPHIS"
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

	public boolean isExisit(String query) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			this.mLogger.query(this.printQuery, query);

			flag = isExists(con, query);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return flag;

	}

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;

		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "SHIPHIS" + "] S ");
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

	public ArrayList selectShipHis(String query, Hashtable ht) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "SHIPHIS" + "]");
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
	
	public ArrayList selectShipHisbyjoin (String query1,String query2, Hashtable ht, String cgroup) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query1 + " from " + query2);
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

				sql.append(" WHERE ");

				conditon = formConditioninvoice(ht);

				sql.append(conditon);
				
				sql.append(cgroup);

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

	public ArrayList selectShipHis(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "SHIPHIS" + "] S");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

				sql.append(" WHERE ");

				conditon = formCondition(ht);

				sql.append(conditon);
				if (extCond.length() > 0)
					sql.append(" and " + extCond);
			}else if (extCond.length() > 0)
				sql.append(" where " + extCond);
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
	
	public ArrayList selectPDAShipHis(String query, Hashtable ht, String extCond)
	throws Exception {
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

	public ArrayList selectCustomerDetails(String query, Hashtable ht,
			String extCond) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DOHDR" + "]  AS dh, ["
				+ ht.get("PLANT") + "_" + "CUSTMST" + "]  AS cus "
				);
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

				sql.append(" WHERE ");

				conditon = " dh.DONO = '" + ht.get("DONO") + "' AND dh.CustCode = cus.CUSTNO ";

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

	public boolean insertShipHis(Hashtable ht) throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
//		String TABLE = "SHIPHIS";
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_SHIPHIS]"
					+ "(" + FIELDS.substring(0, FIELDS.length() - 1)
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
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

		return flag;

	}

	public boolean isExisit(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		String TABLE = "SHIPHIS";
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

	public ArrayList getShipHisDetail(String plant, String orderno)
			throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

//			boolean flag = false;

			StringBuffer sQry = new StringBuffer(
					" SELECT DOLNO,ITEM,ITEMDESC,SUM(PICKQTY) as PICKQTY,BATCH,ID,LOC FROM "
							+ "[" + plant + "_" + "shiphis" + "]" + "");
			sQry
					.append(" where plant='"
							+ plant
							+ "' and dono = '"
							+ orderno
							+ "' and isnull(status,'')  <> 'C' group by DOLNO,item,itemdesc,batch,id,loc ");

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

//			boolean flag = false;

			StringBuffer sQry = new StringBuffer(
					" SELECT DOLNO,ITEM,ITEMDESC,(SELECT STKUOM FROM "
							+ "["
							+ plant
							+ "_itemmst] WHERE ITEM=a.ITEM"
							+ ") AS UOM,(SELECT UNITPRICE FROM "
							+ "["
							+ plant
							+ "_DODET] WHERE DONO='"
							+ orderno
							+ "' and dolnno=a.dolno and ITEM=a.ITEM"
							+ ") AS UPRICE,(SELECT UNITPRICE FROM "
							+ "["
							+ plant
							+ "_DODET] WHERE DONO='"
							+ orderno
							+ "' and dolnno=a.dolno and ITEM=a.ITEM"
							+ ")*SUM(PICKQTY) AS PRICE,MAX(ORDQTY) AS ORDQTY,SUM(PICKQTY) as PICKQTY FROM "
							+ "[" + plant + "_" + "shiphis" + "] a " + "");
			sQry.append(" where plant='" + plant + "' and dono = '" + orderno
					+ "'  group by DOLNO,item,itemdesc");

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
	
	public ArrayList getDoDetailToPrintNew(String plant, String orderno,String afrmDate,String atoDate)
	throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
	
//		boolean flag = false;
		String dtCondStr="",sCondition="";
		
		dtCondStr =    "  and ISNULL(b.CollectionDate,'')<>'' AND CAST((SUBSTRING(b.CollectionDate, 7, 4) + '-' + SUBSTRING(b.CollectionDate, 4, 2) + '-' + SUBSTRING(b.CollectionDate, 1, 2)) AS date)";
	
           if (afrmDate.length() > 0) {
           	sCondition = sCondition +dtCondStr+ " >= '" 
						+ afrmDate
						+ "'  ";
          if (atoDate.length() > 0) {
					sCondition = sCondition + dtCondStr + " <= '" 
					+ atoDate
					+ "'  ";
					}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition +dtCondStr+ "  <= '" 
					+ atoDate
					+ "'  ";
				}
			} 
		
		String ConvertUnitCostToOrderCurrency = " ISNULL((CAST(ISNULL(UNITPRICE,0) *(SELECT CURRENCYUSEQT  FROM [" +plant+"_DODET] b "
		+ " where DONO = '"
		+ orderno
		+ "' AND PLANT = '"
		+ plant
		+ "' and a.DOLNNO = b.DOLNNO) AS DECIMAL(20,4)) ),0) ";
		
	
		StringBuffer sQry = new StringBuffer(
				" SELECT a.DOLNNO as DOLNO,ITEM,ITEMDESC,(SELECT STKUOM FROM "
						+ "["
						+ plant
						+ "_itemmst] WHERE ITEM=a.ITEM"
						+ ") AS UOM," 
						+ConvertUnitCostToOrderCurrency
						+ " AS UPRICE," 
						+ "ISNULL("+ConvertUnitCostToOrderCurrency		
						+ " * SUM(QTYPICK),0) AS PRICE,MAX(QTYOR) AS ORDQTY,MAX(QTYPICK) as PICKQTY," 
						+" MAX(QTYIS) as ISSUEQTY,PICKSTATUS as STATUS,"
						+"(SELECT CollectionDate FROM  " + plant+ "_DOHDR WHERE DONO=a.DONO) AS Trandate,"
						+ " (SELECT CURRENCYID from " + plant+ "_DOHDR WHERE DONO ='"+ orderno+ "' ) as CurrencyID"
						+ " FROM "
						+ "[" + plant + "_" + "dodet" + "] a,["+ plant +"_dohdr] b " + "");
		sQry.append(" where a.plant='" + plant + "' and a.dono = '" + orderno + "' and a.dono=b.dono "+sCondition
				+ " group by a.DONO,DOLNNO,item,itemdesc,UNITPRICE,PICKSTATUS");
	
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
	
	public ArrayList getVoidDetailToPrint(String plant, String orderno,String afrmDate,String atoDate)
			throws Exception {
		
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			
//		boolean flag = false;
			String dtCondStr="",sCondition="";
			
			dtCondStr =    "  and ISNULL(b.CollectionDate,'')<>'' AND CAST((SUBSTRING(b.CollectionDate, 7, 4) + '-' + SUBSTRING(b.CollectionDate, 4, 2) + '-' + SUBSTRING(b.CollectionDate, 1, 2)) AS date)";
			
			if (afrmDate.length() > 0) {
				sCondition = sCondition +dtCondStr+ " >= '" 
						+ afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + dtCondStr + " <= '" 
							+ atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition +dtCondStr+ "  <= '" 
							+ atoDate
							+ "'  ";
				}
			} 
			
			String ConvertUnitCostToOrderCurrency = " ISNULL((CAST(ISNULL(UNITPRICE,0) *(SELECT CURRENCYUSEQT  FROM [" +plant+"_DODET] b "
					+ " where DONO = '"
					+ orderno
					+ "' AND PLANT = '"
					+ plant
					+ "' and a.DOLNNO = b.DOLNNO) AS DECIMAL(20,4)) ),0) ";
			
			
			StringBuffer sQry = new StringBuffer(
					" SELECT a.DOLNNO as DOLNO,ITEM,ITEMDESC,ISNULL(a.DISCOUNT,'') DISCOUNT,a.DISCOUNT_TYPE,(SELECT STKUOM FROM "
							+ "["
							+ plant
							+ "_itemmst] WHERE ITEM=a.ITEM"
							+ ") AS UOM," 
							+ConvertUnitCostToOrderCurrency
							+ " AS UPRICE," 
							+ "ISNULL("+ConvertUnitCostToOrderCurrency		
							+ " * SUM(QTYPICK),0) AS PRICE,MAX(QTYOR) AS ORDQTY," 
							+"(SELECT CollectionDate FROM  " + plant+ "_DOHDR WHERE DONO=a.DONO) AS Trandate,"
							+ " (SELECT CURRENCYID from " + plant+ "_DOHDR WHERE DONO ='"+ orderno+ "' ) as CurrencyID"
							+ " FROM "
							+ "[" + plant + "_" + "dodet" + "] a,["+ plant +"_dohdr] b " + "");
			sQry.append(" where a.plant='" + plant + "' and a.dono = '" + orderno + "' and a.dono=b.dono "+sCondition
					+ " group by a.DONO,DOLNNO,item,itemdesc,UNITPRICE,a.DISCOUNT,a.DISCOUNT_TYPE");
			
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

	public ArrayList getTotalStockSaleLstWithDates(Hashtable ht, String fromdt,
			String todt,String productDesc,String ispos) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
	    String extCond = "";
	    String  fdate = "", tdate = "";
	    String custtypecond="";
	    
	    
		if(fromdt==null) fromdt=""; else fromdt = fromdt.trim();
		if (fromdt.length()>5)
		fdate    = fromdt.substring(6)+fromdt.substring(3,5)+fromdt.substring(0,2);
	
		if(todt==null) todt=""; else todt = todt.trim();
		if (todt.length()>5)
		tdate    = todt.substring(6)+todt.substring(3,5)+todt.substring(0,2);
		
		if (ht.size() > 0) {
			if (ht.get("CUSTTYPE") != null) {
				String custtype = ht.get("CUSTTYPE").toString();
				custtypecond = " AND CNAME in (select CNAME from "+ht.get("PLANT")+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
				ht.remove("CUSTTYPE");
			}
		}
		      if (productDesc.length() > 0 ) {
                    if (productDesc.indexOf("%") != -1) {
                            productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
                    }
                       extCond = " and replace(ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
                }
                else
                {
                	 extCond = " AND DONO <> ''";
                }
		                       
                String condition = "";
            	if (ht.size() > 0) {
   				  condition = formCondition(ht);
    			}
            
            	String bcondition = "";
    		   	if (fdate.length() > 0) {
					bcondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)  >= '" + fdate
							+ "'  ";
					if (tdate.length() > 0) {
						bcondition = bcondition + " AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)   <= '" + tdate
								+ "'  ";
					}
				} else {
					if (tdate.length() > 0) {
						bcondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2) <= '" + tdate
								+ "'  ";
					}
				}
    		   	
    		   	String posext="";
    		   	if(ispos.equalsIgnoreCase("3"))
    		   		posext =  " WHERE SS.ORDERTYPE = 'POS' " ;
    		   	else if(ispos.equalsIgnoreCase("2"))
    		   		posext =  " WHERE SS.ORDERTYPE != 'POS' " ;
    		       		
                StringBuffer sql = new StringBuffer(" SELECT DISTINCT ITEM,(select isnull(ITEMDESC,'') from [" + ht.get("PLANT") +"_itemmst] where item=a.item)ITEMDESC,CNAME,'' AS CUSTCODE,SUM(PICKQTY)PICKQTY ");  
                sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "SHIPHIS" + "] A");
                //sql.append("  WHERE  DONO<>'MISC-ISSUE' AND STATUS='C' AND DONO like 'S%' and DONO not like 'SM%' AND " + condition + bcondition +  extCond +custtypecond);
                sql.append("  WHERE  DONO<>'MISC-ISSUE' AND STATUS='C' AND DONO in (SELECT SS.DONO FROM ["+ht.get("PLANT")+"_DOHDR] SS"+posext+") AND " + condition + bcondition +  extCond +custtypecond);
               	sql.append(" GROUP BY ITEM,CNAME ");
                
		
		try {
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

	public ArrayList getTotalSupplierStockSalestWithDates(Hashtable ht, String fromdt,
			String todt,String productDesc,String ispos) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		String extCond = "";
		String  fdate = "", tdate = "";
		String custtypecond="";
		
		
		if(fromdt==null) fromdt=""; else fromdt = fromdt.trim();
		if (fromdt.length()>5)
			fdate    = fromdt.substring(6)+fromdt.substring(3,5)+fromdt.substring(0,2);
		
		if(todt==null) todt=""; else todt = todt.trim();
		if (todt.length()>5)
			tdate    = todt.substring(6)+todt.substring(3,5)+todt.substring(0,2);
		
		if (ht.size() > 0) {
			if (ht.get("CUSTTYPE") != null) {
				String custtype = ht.get("CUSTTYPE").toString();
				custtypecond = " AND CNAME in (select CNAME from "+ht.get("R.PLANT")+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
				ht.remove("CUSTTYPE");
			}
		}
		if (productDesc.length() > 0 ) {
			if (productDesc.indexOf("%") != -1) {
				productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
			}
			extCond = " and replace(ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
		}
		else
		{
			extCond = " AND DONO <> ''";
		}
		
		String condition = "";
		if (ht.size() > 0) {
			condition = formCondition(ht);
		}
		
		String bcondition = "";
		if (fdate.length() > 0) {
			bcondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)  >= '" + fdate
					+ "'  ";
			if (tdate.length() > 0) {
				bcondition = bcondition + " AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)   <= '" + tdate
						+ "'  ";
			}
		} else {
			if (tdate.length() > 0) {
				bcondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2) <= '" + tdate
						+ "'  ";
			}
		}
		
		String posext="";
		if(ispos.equalsIgnoreCase("3"))
			posext =  " WHERE SS.ORDERTYPE = 'POS' " ;
		else if(ispos.equalsIgnoreCase("2"))
			posext =  " WHERE SS.ORDERTYPE != 'POS' " ;
		
		//not added custcode
//		StringBuffer sql = new StringBuffer(" SELECT DISTINCT A.ITEM,isnull(I.ITEMDESC,'') ITEMDESC,R.CNAME,SUM(PICKQTY)PICKQTY ");  
		//added custcode
		StringBuffer sql = new StringBuffer(" SELECT DISTINCT A.ITEM,isnull(I.ITEMDESC,'') ITEMDESC,R.CNAME,ISNULL((SELECT C.VENDNO FROM ["+ht.get("R.PLANT")+"_VENDMST] C where C.VNAME=R.CNAME),'') as CUSTCODE,SUM(PICKQTY)PICKQTY ");
		sql.append(" FROM " + "[" + ht.get("R.PLANT") + "_" + "SHIPHIS" + "] A JOIN [" + ht.get("R.PLANT") + "_itemmst] I ON A.item=I.item join [" + ht.get("R.PLANT") + "_RECVDET] R ON A.ITEM=R.ITEM");
		sql.append("  WHERE  DONO<>'MISC-ISSUE' AND A.STATUS='C' AND DONO in (SELECT SS.DONO FROM ["+ht.get("R.PLANT")+"_DOHDR] SS"+posext+") AND R.CNAME!='' AND R.CNAME IS NOT NULL AND " + condition + bcondition +  extCond +custtypecond);
		sql.append(" GROUP BY A.ITEM,I.ITEMDESC,R.CNAME ");
		
		
		try {
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
	
	public ArrayList getStockSale(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "SHIPHIS" + "]");
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
			sql.append(" group by item ");
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
/*
	public int getCountofExpStock(String aPlant, String aItem, String custcode,
			String fromdt, String todt) throws Exception {

		int expdtcnt = 0;

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = DbBean.getConnection();
			String aQuery = "select COUNT(expirydat) " + " from " + "["
					+ aPlant + "_SHIPHIS]";
			aQuery = aQuery + "where  item='" + aItem + "'";
			aQuery = aQuery + "and  userfld2='" + custcode + "'";
			aQuery = aQuery
					+ " and REPLACE(isnull(EXPIRYDAT,''),'-','')<convert(varchar(8),GETDATE(),112) and expirydat<>'' ";

			if (fromdt.length() > 0)
				aQuery = aQuery + " and EXPIRYDAT > '"
						+ DateUtils.getDateinyyyy_mm_dd(fromdt) + "'";
			if (todt.length() > 0)
				aQuery = aQuery + " and EXPIRYDAT < '"
						+ DateUtils.getDateinyyyy_mm_dd(todt) + "'";

			ps = con.prepareStatement(aQuery);
			rs = ps.executeQuery();
			while (rs.next()) {

				expdtcnt = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return expdtcnt;
	}
	
	
	public int getCountofExpStockWithDateFormats(String aPlant, String aItem, String custcode,
			String fromdt, String todt) throws Exception {

		int expdtcnt = 0;

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = DbBean.getConnection();
			String aQuery = "select COUNT(expirydat) " + " from " + "["
					+ aPlant + "_SHIPHIS]";
			aQuery = aQuery + "where  item='" + aItem + "'";
			aQuery = aQuery + "and  userfld2='" + custcode + "'";
			
			if (fromdt.length() > 0)
				aQuery = aQuery + " and LTRIM(RTRIM(A.EXPIREDATE)) <> '' AND A.EXPIREDATE is not null AND convert(datetime,A.EXPIREDATE,103) >= convert(datetime,'" + fromdt.toUpperCase() + "',103) ";
				
			if (todt.length() > 0)
				aQuery = aQuery + " and LTRIM(RTRIM(A.EXPIREDATE)) <> '' AND A.EXPIREDATE is not null AND convert(datetime,A.EXPIREDATE,103) <= convert(datetime,'" + fromdt.toUpperCase() + "',103) ";
			

			ps = con.prepareStatement(aQuery);
			rs = ps.executeQuery();
			while (rs.next()) {

				expdtcnt = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return expdtcnt;
	}
	
	public int getCountofExpStockWithDateFormatsAndCustomer(String aPlant, String aItem, String custcode,String custName,
			String fromdt, String todt) throws Exception {

		int expdtcnt = 0;

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = DbBean.getConnection();
			String aQuery = "select COUNT(expirydat) " + " from " + "["
					+ aPlant + "_SHIPHIS]";
			aQuery = aQuery + "where  item='" + aItem + "'";
			aQuery = aQuery + "and  userfld2='" + custcode + "'";
			aQuery = aQuery + "and  CNAME='" + custName + "'";
			
			if (fromdt.length() > 0)
				aQuery = aQuery + " and LTRIM(RTRIM(EXPIRYDAT)) <> '' AND EXPIRYDAT is not null AND convert(datetime,EXPIRYDAT,103) >= convert(datetime,'" + fromdt.toUpperCase() + "',103) ";
				
			if (todt.length() > 0)
				aQuery = aQuery + " and LTRIM(RTRIM(EXPIRYDAT)) <> '' AND EXPIRYDAT is not null AND convert(datetime,EXPIRYDAT,103) <= convert(datetime,'" + fromdt.toUpperCase() + "',103) ";
				

			ps = con.prepareStatement(aQuery);
			rs = ps.executeQuery();
			while (rs.next()) {

				expdtcnt = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return expdtcnt;
	}
	    */
	public ArrayList selectCustomerReterunOrderNo(String query, Hashtable ht, String extCond)
		throws Exception {
//	boolean flag = false;
	ArrayList alData = new ArrayList();
	
	java.sql.Connection con = null;

	StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
			+ ht.get("PLANT") + "_" + "SHIPHIS" + "]");
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
	
	
	public String getPickQty(String aPlant, String aDono,String aDolno,String aItem,String aLoc,String aBatch) throws Exception {
		String pickqty = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("dono", aDono);
		ht.put("dolno", aDolno);
		ht.put("loc1", aLoc);
		ht.put("batch", aBatch);

		String query = " isnull(sum(pickqty),0) as qtyPick ";

		Map m = selectRow(query, ht, "  Reverseqty is  null group by dono,dolno,item,loc1,batch" );
		
		if (m.size() > 0) {
			pickqty = (String) m.get("qtyPick");
		} else {
			pickqty =  "0";
		}
		if (pickqty.equalsIgnoreCase(null) || pickqty.length() == 0) {
			pickqty =  "0";
		}
		return pickqty;
	}
	
	public String getIssueQty(String aPlant, String aDono,String aDolno,String aItem,String aLoc,String aBatch) throws Exception {
				
		String pickqty = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("dono", aDono);
		ht.put("dolno", aDolno);
		ht.put("loc1", aLoc);
		ht.put("batch", aBatch);


		String query = " isnull(sum(pickqty),0) as qtyIssue ";

		Map m = selectRow(query, ht, " STATUS ='C' group by dono,dolno,item,loc1,batch ");
		if (m.size() > 0) {
			pickqty = (String) m.get("qtyIssue");
		} else {
			pickqty = "0";
		}
		if (pickqty.equalsIgnoreCase(null) || pickqty.length() == 0) {
			pickqty =  "0";
		}
		return pickqty;
	}
	
	public String getReverseQty(String aPlant, String aDono,String aDolno,String aItem,String aLoc,String aBatch) throws Exception {
		
		String reveseqty = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("dono", aDono);
		ht.put("dolno", aDolno);
		ht.put("loc1", aLoc);
		ht.put("batch", aBatch);


		String query = " isnull(sum(pickqty),0) as reverseIssue ";

		Map m = selectRow(query, ht, " STATUS ='O' and Reverseqty is not null group by dono,dolno,item,loc1,batch ");
		if (m.size() > 0) {
			reveseqty = (String) m.get("reverseIssue");
		} else {
			reveseqty = "0";
		}
		if (reveseqty.equalsIgnoreCase(null) || reveseqty.length() == 0) {
			reveseqty =  "0";
		}
		return reveseqty;
	}
	public String getPDAReverseQty(String aPlant, String aDono,String aDolno,String aItem) throws Exception {
		
		String reveseqty = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("dono", aDono);
		ht.put("dolno", aDolno);
	


		String query = " isnull(sum(pickqty),0) as reverseIssue ";

		Map m = selectRow(query, ht, " STATUS ='O' and Reverseqty is not null group by dono,dolno,item,loc1,batch ");
		if (m.size() > 0) {
			reveseqty = (String) m.get("reverseIssue");
		} else {
			reveseqty = "0";
		}
		if (reveseqty.equalsIgnoreCase(null) || reveseqty.length() == 0) {
			reveseqty =  "0";
		}
		return reveseqty;
	}
	
	
	
	public Map selectRow(String query, Hashtable ht, String extCondi)
	throws Exception {
	Map map = new HashMap();
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
				+ "[" + (String) ht.get("PLANT") + "_SHIPHIS]");
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

public boolean deleteSHIPHIS(java.util.Hashtable ht) throws Exception {
	boolean delete = false;
	java.sql.Connection con = null;
	try {
		con = DbBean.getConnection();

		StringBuffer sql = new StringBuffer(" DELETE ");
		sql.append(" ");
		sql.append(" FROM " + "[" + ht.get("PLANT") + "_SHIPHIS]");
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

public ArrayList getIssuedOutboundOrderDetails(String plant, String orderno,String aissueDate,String atoDate)
throws Exception {
java.sql.Connection con = null;
ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
//		boolean flag = false;
		String sCondition = "";
		
		
			sCondition = sCondition + " AND  substring(crat,1,8) = '"
					+ aissueDate + "' ";
		
		
		
		String sQry = "select dono,dolno,item,itemdesc,batch,loc1 as loc,unitprice,ordqty,pickqty,(select STKUOM from ["+plant+"_ITEMMST] where ITEM = S.item ) uom from " + "["
			+ plant + "_" + "shiphis" + "]S" + " where plant='" + plant
			+ "' and dono like '" + orderno 
			+ "%' " + sCondition + " order by CAST(dolno as int)";
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

public Boolean removeOrder(String plant, String dono) throws SQLException {
	Connection connection = null;
	try {
		connection = DbBean.getConnection();
		String sql = "DELETE FROM [" + plant + "_" + "SHIPHIS] WHERE DONO='"
				+ dono + "' AND PLANT='" + plant + "'";
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

public ArrayList getDoDetailToPrintshiphis(String plant, String orderno,String afrmDate,String atoDate)
		throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
		
//			boolean flag = false;
			String dtCondStr="",sCondition="";
			
			dtCondStr =    "  AND CAST((SUBSTRING(a.ISSUEDATE, 7, 4) + '-' + SUBSTRING(a.ISSUEDATE, 4, 2) + '-' + SUBSTRING(a.ISSUEDATE, 1, 2)) AS date)";
		
	           if (afrmDate.length() > 0) {
	           	sCondition = sCondition +dtCondStr+ " >= '" 
							+ afrmDate
							+ "'  ";
	          if (atoDate.length() > 0) {
						sCondition = sCondition + dtCondStr + " <= '" 
						+ atoDate
						+ "'  ";
						}
				} else {
					if (atoDate.length() > 0) {
						sCondition = sCondition +dtCondStr+ "  <= '" 
						+ atoDate
						+ "'  ";
					}
				} 
			
			String ConvertUnitCostToOrderCurrency = " ISNULL((CAST(ISNULL(UNITPRICE,0) *(SELECT CURRENCYUSEQT  FROM [" +plant+"_DODET] b "
			+ " where DONO = '"
			+ orderno
			+ "' AND PLANT = '"
			+ plant
			+ "' and a.DOLNO = b.DOLNNO) AS DECIMAL(20,4)) ),0) ";
			
		
			StringBuffer sQry = new StringBuffer(
					" SELECT a.DOLNO as DOLNO,ITEM,ITEMDESC,(SELECT STKUOM FROM "
							+ "["
							+ plant
							+ "_itemmst] WHERE ITEM=a.ITEM"
							+ ") AS UOM," 
							+ConvertUnitCostToOrderCurrency
							+ " AS UPRICE," 
							+ "ISNULL("+ConvertUnitCostToOrderCurrency		
							+ " * SUM(PICKQTY),0) AS PRICE,MAX(ORDQTY) AS ORDQTY,ISNULL(SUM(PICKQTY),0) as PICKQTY," 
							+" case a.status when 'C' then isnull(sum(a.pickqty),0) else '0' end  as ISSUEQTY,STATUS,"
							+"ISNULL(ISSUEDATE,'') as Trandate,"
							+ " (SELECT CURRENCYID from " + plant+ "_DOHDR WHERE DONO ='"+ orderno+ "' ) as CurrencyID"
							+ " FROM "
							+ "[" + plant + "_" + "shiphis" + "] a " + "");
			sQry.append(" where plant='" + plant + "' and dono = '" + orderno +"'  AND ISNULL(ISSUEDATE,'') <>'' AND DONO<>'MISC-ISSUE'" + sCondition
					+ "  group by DOLNO,item,itemdesc,UNITPRICE,STATUS,ISSUEDATE");
		
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
public ArrayList getIssuedDetailsforreverse(String plant, Hashtable ht)
		throws Exception {
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
//		boolean flag = false;
		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append(" DONO,DOLNO,ITEM,BATCH,LOC,PICKQTY,CRAT,ISNULL(ID,0) AS ID");
		sql.append(" ");
		sql.append(" FROM " + "[" + plant + "_" + "SHIPHIS" + "]");
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
public ArrayList getproductcustomersaleswithzeroqty(String plant,String item,String itemDesc,String custname,String loc,
		String sort, String fromdt,String todt,String ispos,String radiosearch) throws Exception {
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;
    String extCond = "";
    String extCond1 = "";
    String  fdate = "", tdate = "";
//    String custtypecond="";
    
    
	if(fromdt==null) fromdt=""; else fromdt = fromdt.trim();
	if (fromdt.length()>5)
	fdate    = fromdt.substring(6)+fromdt.substring(3,5)+fromdt.substring(0,2);

	if(todt==null) todt=""; else todt = todt.trim();
	if (todt.length()>5)
	tdate    = todt.substring(6)+todt.substring(3,5)+todt.substring(0,2);
	
	if (item.length() > 0 ) {
		if(sort.equalsIgnoreCase("PRODUCT"))
        extCond = " where A.ITEM='"+item+"' ";
		else
        extCond = " AND ITEM='"+item+"' ";
		extCond1 = " AND S.ITEM='"+item+"' ";
    }
	
	if (itemDesc.length() > 0 ) {
        if (itemDesc.indexOf("%") != -1) {
        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
        }
        extCond = " where replace(ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
    }
	                       
     String cond = "";
     if (custname.length()>0){
       	custname = StrUtils.InsertQuotes(custname);
       	cond =  " WHERE CNAME LIKE '%"+custname+"%' " ;
     }
        	String bcondition = "";
        	String bcondition1 = "";
		   	if (fdate.length() > 0) {
				bcondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)  >= '" + fdate
						+ "'  ";
						bcondition1 =" AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2)  >= '" + fdate
						+ "'  ";
				if (tdate.length() > 0) {
					bcondition = bcondition + " AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)   <= '" + tdate
							+ "'  ";
					bcondition1 = bcondition1 + " AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2)   <= '" + tdate
							+ "'  ";
				}
			} else {
				if (tdate.length() > 0) {
					bcondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2) <= '" + tdate
							+ "'  ";
					bcondition1 =" AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2) <= '" + tdate
							+ "'  ";
				}
			}
		   	
		   	//POSSEARCH
		   	String posext="";
		   	if(ispos.equalsIgnoreCase("3"))
		   		posext =  " WHERE SS.ORDERTYPE = 'POS' " ;
		   	else if(ispos.equalsIgnoreCase("2"))
		   		posext =  " WHERE SS.ORDERTYPE != 'POS' " ;
		   	
		   	StringBuffer sql = new StringBuffer();
		   	if(sort.equalsIgnoreCase("PRODUCT")){
		   	/*	sql.append(" select ITEM as ID ,ITEMDESC as NAME,UNITPRICE,COST, ");  
		   		//sql.append(" (SELECT ISNULL(SUM(PICKQTY),0) FROM [" + plant +"_SHIPHIS] where (DONO like 'S%' OR DONO like 'TI%') AND PLANT='"+ plant +"' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND DONO <> 'MISC-ISSUE' AND ITEM=A.ITEM "+bcondition+")AS TOTQTY,");
		   		sql.append(" (SELECT ISNULL(SUM(PICKQTY),0) FROM [" + plant +"_SHIPHIS] where (DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS "+posext+")) AND PLANT='"+ plant +"' AND LOC LIKE '%"+loc+"%'  AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND DONO <> 'MISC-ISSUE' AND ITEM=A.ITEM "+bcondition+")AS TOTQTY,");
		   		sql.append(" (SELECT isnull(SUM(PICKQTY*UNITPRICE),0)  FROM [" + plant +"_SHIPHIS] where (DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS "+posext+")) AND PLANT='"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND DONO <> 'MISC-ISSUE' AND ITEM=A.ITEM "+bcondition+")AS TOTPRICE, ");
		   		sql.append(" (SELECT isnull(SUM(PICKQTY),0)*A.COST  FROM [" + plant +"_SHIPHIS] where (DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS "+posext+")) AND PLANT='"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND DONO <> 'MISC-ISSUE' AND ITEM=A.ITEM "+bcondition+")AS TOTCOST, ");
		   		sql.append(" isnull((SELECT ISNULL(SUM(QTY),0)  FROM [" + plant +"_Invmst] where PLANT='"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM=A.ITEM),0)AS QTY ");
		   		sql.append(" FROM ["+ plant +"_ITEMMST]A "+extCond);*/
		   		
		   		if(ispos.equalsIgnoreCase("3"))
			   		posext =  " AND A.ORDERTYPE = 'POS' " ;
			   	else if(ispos.equalsIgnoreCase("2"))
			   		posext =  " AND A.ORDERTYPE != 'POS' " ;
		   		
		   				sql.append("WITH ProcessedOrders AS ( ");
		   				sql.append("SELECT A.DONO, B.ITEM, SUM(B.QTYOR) AS QTYOR, SUM(B.UNITCOST * B.QTYOR) AS TOTAL_UNITCOST, "); 
		   				sql.append("SUM(B.UNITPRICE * B.QTYOR) AS TOTAL_UNITPRICE,SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE, '%') = '%' ");
		   				sql.append("THEN (B.UNITPRICE * B.QTYOR) * (B.DISCOUNT / 100) ELSE B.DISCOUNT END) AS TOTAL_DISCOUNT, ");
		   				sql.append("A.ITEM_RATES, A.OUTBOUND_GST ");
		   				sql.append("FROM ["+ plant +"_DOHDR] A JOIN ["+ plant +"_DODET] B ON A.DONO = B.DONO JOIN ["+ plant +"_SHIPHIS] S ON B.DONO = S.DONO AND B.ITEM=S.ITEM AND B.DOLNNO=S.DOLNO ");
		   				sql.append("WHERE S.STATUS='C' "+posext+" AND A.PLANT = '"+ plant +"' AND S.LOC LIKE '%"+loc+"%' "+bcondition1+" ");
		   				sql.append("GROUP BY A.DONO, B.ITEM, A.ITEM_RATES, A.OUTBOUND_GST), ");
		   				if(radiosearch.equalsIgnoreCase("3")) {
		   					
		   				sql.append("BProcessedOrders AS ( ");
		   				sql.append("SELECT A.DONO, B.ITEM, SUM(B.QTYOR) AS QTYOR, SUM(B.UNITCOST * B.QTYOR) AS TOTAL_UNITCOST, "); 
		   				sql.append("SUM(B.UNITPRICE * B.QTYOR) AS TOTAL_UNITPRICE,SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE, '%') = '%' ");
		   				sql.append("THEN (B.UNITPRICE * B.QTYOR) * (B.DISCOUNT / 100) ELSE B.DISCOUNT END) AS TOTAL_DISCOUNT, ");
		   				sql.append("A.ITEM_RATES, A.OUTBOUND_GST ");
		   				sql.append("FROM ["+ plant +"_DOHDR] A JOIN ["+ plant +"_DODET] B ON A.DONO = B.DONO JOIN ["+ plant +"_SHIPHIS] S ON B.DONO = S.DONO AND B.ITEM=S.ITEM AND B.DOLNNO=S.DOLNO ");
		   				sql.append("WHERE S.STATUS='C' "+posext+" AND A.PLANT = '"+ plant +"' AND S.LOC LIKE '%"+loc+"%' "+bcondition1.replace(">=","<=")+" ");
		   				sql.append("GROUP BY A.DONO, B.ITEM, A.ITEM_RATES, A.OUTBOUND_GST), ");
		   				}
		   				sql.append("ExchangeOrders AS (SELECT PE.ITEM, SUM(ISNULL(I.COST, 0) * PE.QTY) AS EXCOST,SUM(ISNULL(PE.UNITPRICE, 0) * PE.QTY) AS EXPRICE ");
		   				sql.append("FROM "+ plant +"_POSEXCHANGEDET PE JOIN "+ plant +"_ITEMMST I ON I.ITEM = PE.ITEM ");
		   				sql.append("JOIN ["+ plant +"_DOHDR] A ON PE.EXDONO = A.DONO ");
		   				sql.append("WHERE A.ORDER_STATUS = 'PROCESSED' "+posext+" AND A.PLANT = '"+ plant +"' "+bcondition1+" ");
		   				sql.append("GROUP BY PE.ITEM) ");
		   				sql.append("SELECT A.ITEM AS ID,A.ITEMDESC AS NAME,A.UNITPRICE,isnull(A.COST,0) as COST,ISNULL(SUM(PO.QTYOR), 0) AS TOTQTY, ");
		   				if(radiosearch.equalsIgnoreCase("3")) {
		   					sql.append("ISNULL(SUM(BPO.QTYOR), 0) AS BTOTQTY, ");
		   				}else {
		   					sql.append("ISNULL('0', 0) AS BTOTQTY,  ");
		   				}
		   				sql.append("ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'../jsp/dist/img/NO_IMG.png') AS CATALOG, ");
		   				sql.append("ROUND(ISNULL(SUM(PO.TOTAL_UNITCOST), 0),5) - ISNULL(E.EXCOST, 0) AS TOTCOST, ");
		   				sql.append("ISNULL(SUM(PO.TOTAL_UNITPRICE - PO.TOTAL_DISCOUNT) - ISNULL(E.EXPRICE, 0), 0) AS TOTAMT, ");
		   				sql.append("ISNULL(CASE WHEN ISNULL(PO.ITEM_RATES, 1) = 1 THEN SUM(PO.TOTAL_UNITPRICE) - ((SUM(PO.TOTAL_UNITPRICE) / (100 + PO.OUTBOUND_GST)) * 100) ELSE (SUM(PO.TOTAL_UNITPRICE) / 100) * PO.OUTBOUND_GST END , 0) AS TOTTAX, ");
		   				sql.append("ISNULL(SUM(PO.TOTAL_UNITPRICE - PO.TOTAL_DISCOUNT) ");
		   				sql.append("+ CASE WHEN ISNULL(PO.ITEM_RATES, 1) = 1 ");		   				
		   				sql.append("THEN SUM(PO.TOTAL_UNITPRICE) - ((SUM(PO.TOTAL_UNITPRICE) / (100 + PO.OUTBOUND_GST)) * 100) ");
		   				sql.append("ELSE (SUM(PO.TOTAL_UNITPRICE) / 100) * PO.OUTBOUND_GST END , 0) AS TOTPRICE, "); 
		   				//sql.append("- ISNULL(E.EXPRICE, 0), 0) AS TOTPRICE, ");
		   				sql.append("ISNULL((SELECT SUM(QTY) FROM ["+ plant +"_Invmst] WHERE PLANT = '"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM = A.ITEM), 0) AS QTY ");
//		   				sql.append(",ISNULL((SELECT TOP 1 IV.UPAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM), '') AS UPAT ");
		   				sql.append(", CASE WHEN ISNULL((SELECT TOP 1 IV.UPAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM), '') = '' THEN ISNULL((SELECT TOP 1 IV.CRAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM), '') ELSE (SELECT TOP 1 IV.UPAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM) END AS UPAT ");
		   				sql.append(", ISNULL((select SUM(C.QTYOR) AS QTYOR FROM ["+plant+"_POHDR] P JOIN ["+plant+"_PODET] C ON P.PONO = C.PONO JOIN ["+plant+"_RECVDET] R ON P.PONO = C.PONO AND C.ITEM=R.ITEM AND C.POLNNO=R.LNNO  where R.ITEM=A.item "+bcondition1.replace("A.DELDATE", "R.RECVDATE")+"),0) TOTQTYOR ");
		   				String reccond = bcondition1.replace("A.DELDATE", "R.RECVDATE");
		   				reccond = reccond.replace(">=","<=");
		   				if(radiosearch.equalsIgnoreCase("3")) {
		   				sql.append(", ISNULL((select SUM(C.QTYOR) AS QTYOR FROM ["+plant+"_POHDR] P JOIN ["+plant+"_PODET] C ON P.PONO = C.PONO JOIN ["+plant+"_RECVDET] R ON P.PONO = C.PONO AND C.ITEM=R.ITEM AND C.POLNNO=R.LNNO  where R.ITEM=A.item "+reccond+") ,0) BTOTQTYOR ");
		   				}else {
		   					sql.append(", ISNULL('0', 0) AS BTOTQTYOR  ");
		   					
		   				}
		   				sql.append("FROM ["+ plant +"_ITEMMST] A LEFT JOIN ProcessedOrders PO ON A.ITEM = PO.ITEM ");
		   				if(radiosearch.equalsIgnoreCase("3")) {
		   					sql.append("LEFT JOIN BProcessedOrders BPO ON A.ITEM = BPO.ITEM ");
		   				}
		   				sql.append("LEFT JOIN ExchangeOrders E ON A.ITEM = E.ITEM "+extCond);
		   				sql.append("GROUP BY A.ITEM,A.ITEMDESC,A.CATLOGPATH,A.UNITPRICE,A.COST,PO.ITEM_RATES,PO.OUTBOUND_GST,E.EXCOST,E.EXPRICE; ");

		   	}
		   	else{
		   		sql.append(" select CUSTNO as ID ,CNAME as NAME, ");  
		   		//sql.append(" (SELECT ISNULL(SUM(PICKQTY),0) FROM [" + plant +"_SHIPHIS] where (DONO like 'S%' OR DONO like 'TI%') AND PLANT='"+ plant +"' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND CNAME=A.CNAME "+bcondition+")AS TOTQTY,");
		   		sql.append(" (SELECT ISNULL(SUM(PICKQTY),0) FROM [" + plant +"_SHIPHIS] where (DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS "+posext+")) AND PLANT='"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND CNAME=A.CNAME "+extCond+bcondition+")AS TOTQTY,");
		   		sql.append(" (SELECT isnull(SUM(PICKQTY*UNITPRICE),0)  FROM [" + plant +"_SHIPHIS] where (DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS "+posext+")) AND PLANT='"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND CNAME=A.CNAME "+extCond+bcondition+")AS TOTPRICE, ");
		   		if(loc.equalsIgnoreCase(""))		   		
		   		sql.append(" ISNULL((SELECT ISNULL(SUM(QTY),0) FROM [" + plant +"_SHIPHIS] S JOIN [" + plant +"_INVMST] I ON I.ITEM=S.ITEM where (DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS "+posext+")) AND S.PLANT='"+ plant +"' AND S.ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and S.STATUS='C' AND CNAME=A.CNAME "+extCond1+bcondition+"),0)AS QTY");
		   		else
		   		sql.append(" ISNULL((SELECT ISNULL(SUM(QTY),0) FROM [" + plant +"_SHIPHIS] S JOIN [" + plant +"_INVMST] I ON I.ITEM=S.ITEM where (DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS "+posext+")) AND S.PLANT='"+ plant +"' AND I.LOC LIKE '%"+loc+"%' AND S.LOC LIKE '%"+loc+"%' AND S.ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and S.STATUS='C' AND CNAME=A.CNAME "+extCond1+bcondition+"),0)AS QTY");
		   		sql.append(" FROM ["+ plant +"_CUSTMST]A "+cond);
		   	}
            
	
	try {
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

public ArrayList getproductReorderqty(String plant,String item,String itemDesc,String custname,String loc,
		String sort, String fromdt,String todt,String ispos) throws Exception {
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;
	String extCond = "";
	String extCond1 = "";
	String  fdate = "", tdate = "";
//    String custtypecond="";
	
	
	if(fromdt==null) fromdt=""; else fromdt = fromdt.trim();
	if (fromdt.length()>5)
		fdate    = fromdt.substring(6)+fromdt.substring(3,5)+fromdt.substring(0,2);
	
	if(todt==null) todt=""; else todt = todt.trim();
	if (todt.length()>5)
		tdate    = todt.substring(6)+todt.substring(3,5)+todt.substring(0,2);
	
	if (item.length() > 0 ) {
		if(sort.equalsIgnoreCase("PRODUCT"))
			extCond = " where A.ITEM='"+item+"' ";
		else
			extCond = " AND ITEM='"+item+"' ";
		extCond1 = " AND S.ITEM='"+item+"' ";
	}
	
	if (itemDesc.length() > 0 ) {
		if (itemDesc.indexOf("%") != -1) {
			itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		}
		extCond = " where replace(ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	}
	
	String cond = "";
	if (custname.length()>0){
		custname = StrUtils.InsertQuotes(custname);
		cond =  " WHERE CNAME LIKE '%"+custname+"%' " ;
	}
	String bcondition = "";
	String bcondition1 = "";
	if (fdate.length() > 0) {
		bcondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)  >= '" + fdate
				+ "'  ";
		bcondition1 =" AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2)  >= '" + fdate
				+ "'  ";
		if (tdate.length() > 0) {
			bcondition = bcondition + " AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)   <= '" + tdate
					+ "'  ";
			bcondition1 = bcondition1 + " AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2)   <= '" + tdate
					+ "'  ";
		}
	} else {
		if (tdate.length() > 0) {
			bcondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2) <= '" + tdate
					+ "'  ";
			bcondition1 =" AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2) <= '" + tdate
					+ "'  ";
		}
	}
	
	//POSSEARCH
	String posext="";
	if(ispos.equalsIgnoreCase("3")) {
//		   		posext =  " WHERE SS.ORDERTYPE = 'POS' " ;
	}
	else if(ispos.equalsIgnoreCase("2"))
		posext =  " WHERE SS.ORDERTYPE != 'POS' " ;
	
	StringBuffer sql = new StringBuffer();
	if(sort.equalsIgnoreCase("PRODUCT")){
		/*	sql.append(" select ITEM as ID ,ITEMDESC as NAME,UNITPRICE,COST, ");  
		   		//sql.append(" (SELECT ISNULL(SUM(PICKQTY),0) FROM [" + plant +"_SHIPHIS] where (DONO like 'S%' OR DONO like 'TI%') AND PLANT='"+ plant +"' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND DONO <> 'MISC-ISSUE' AND ITEM=A.ITEM "+bcondition+")AS TOTQTY,");
		   		sql.append(" (SELECT ISNULL(SUM(PICKQTY),0) FROM [" + plant +"_SHIPHIS] where (DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS "+posext+")) AND PLANT='"+ plant +"' AND LOC LIKE '%"+loc+"%'  AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND DONO <> 'MISC-ISSUE' AND ITEM=A.ITEM "+bcondition+")AS TOTQTY,");
		   		sql.append(" (SELECT isnull(SUM(PICKQTY*UNITPRICE),0)  FROM [" + plant +"_SHIPHIS] where (DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS "+posext+")) AND PLANT='"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND DONO <> 'MISC-ISSUE' AND ITEM=A.ITEM "+bcondition+")AS TOTPRICE, ");
		   		sql.append(" (SELECT isnull(SUM(PICKQTY),0)*A.COST  FROM [" + plant +"_SHIPHIS] where (DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS "+posext+")) AND PLANT='"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND DONO <> 'MISC-ISSUE' AND ITEM=A.ITEM "+bcondition+")AS TOTCOST, ");
		   		sql.append(" isnull((SELECT ISNULL(SUM(QTY),0)  FROM [" + plant +"_Invmst] where PLANT='"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM=A.ITEM),0)AS QTY ");
		   		sql.append(" FROM ["+ plant +"_ITEMMST]A "+extCond);*/
		
		if(ispos.equalsIgnoreCase("3")) {
//			   		posext =  " AND A.ORDERTYPE = 'POS' " ;
		}
		else if(ispos.equalsIgnoreCase("2"))
			posext =  " AND A.ORDERTYPE != 'POS' " ;
		
		if(ispos.equalsIgnoreCase("3")) {
			sql.append("WITH ProcessedOrders AS ( ");
			sql.append("SELECT A.DONO, B.ITEM, SUM(B.QTYOR) AS QTYOR, SUM(B.UNITCOST * B.QTYOR) AS TOTAL_UNITCOST, "); 
			sql.append("SUM(B.UNITPRICE * B.QTYOR) AS TOTAL_UNITPRICE,SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE, '%') = '%' ");
			sql.append("THEN (B.UNITPRICE * B.QTYOR) * (B.DISCOUNT / 100) ELSE B.DISCOUNT END) AS TOTAL_DISCOUNT, ");
			sql.append("A.ITEM_RATES, A.OUTBOUND_GST ");
			sql.append("FROM ["+ plant +"_FINPOSHDR] A JOIN ["+ plant +"_FINPOSDET] B ON A.DONO = B.DONO JOIN ["+ plant +"_SHIPHIS] S ON B.DONO = S.DONO AND B.ITEM=S.ITEM AND B.DOLNNO=S.DOLNO ");
			sql.append("WHERE S.STATUS='C' "+posext+" AND A.PLANT = '"+ plant +"' AND S.LOC LIKE '%"+loc+"%' "+bcondition1+" ");
			sql.append("GROUP BY A.DONO, B.ITEM, A.ITEM_RATES, A.OUTBOUND_GST), ");
			sql.append("ExchangeOrders AS (SELECT PE.ITEM, SUM(ISNULL(I.COST, 0) * PE.QTY) AS EXCOST,SUM(ISNULL(PE.UNITPRICE, 0) * PE.QTY) AS EXPRICE ");
			sql.append("FROM "+ plant +"_POSEXCHANGEDET PE JOIN "+ plant +"_ITEMMST I ON I.ITEM = PE.ITEM ");
			sql.append("JOIN ["+ plant +"_FINPOSHDR] A ON PE.EXDONO = A.DONO ");
			sql.append("WHERE A.ORDER_STATUS = 'PROCESSED' "+posext+" AND A.PLANT = '"+ plant +"' "+bcondition1+" ");
			sql.append("GROUP BY PE.ITEM) ");
			sql.append("SELECT A.ITEM AS ID,A.ITEMDESC AS NAME,A.UNITPRICE,isnull(A.COST,0) as COST,ISNULL(SUM(PO.QTYOR), 0) AS TOTQTY, ");
			sql.append("ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'../jsp/dist/img/NO_IMG.png') AS CATALOG, ");
			sql.append("ROUND(ISNULL(SUM(PO.TOTAL_UNITCOST), 0),5) - ISNULL(E.EXCOST, 0) AS TOTCOST, ");
			sql.append("ISNULL(SUM(PO.TOTAL_UNITPRICE - PO.TOTAL_DISCOUNT) ");
			sql.append("+ CASE WHEN ISNULL(PO.ITEM_RATES, 1) = 1 "); 
			sql.append("THEN SUM(PO.TOTAL_UNITPRICE) - ((SUM(PO.TOTAL_UNITPRICE) / (100 + PO.OUTBOUND_GST)) * 100) ");
			sql.append("ELSE (SUM(PO.TOTAL_UNITPRICE) / 100) * PO.OUTBOUND_GST END "); 
			sql.append("- ISNULL(E.EXPRICE, 0), 0) AS TOTPRICE, ");
			sql.append("ISNULL((SELECT SUM(QTY) FROM ["+ plant +"_Invmst] WHERE PLANT = '"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM = A.ITEM), 0) AS QTY ");
//	   				sql.append(",ISNULL((SELECT TOP 1 IV.UPAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM), '') AS UPAT ");
			sql.append(", CASE WHEN ISNULL((SELECT TOP 1 IV.UPAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM), '') = '' THEN ISNULL((SELECT TOP 1 IV.CRAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM), '') ELSE (SELECT TOP 1 IV.UPAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM) END AS UPAT ");
			sql.append("FROM ["+ plant +"_ITEMMST] A LEFT JOIN ProcessedOrders PO ON A.ITEM = PO.ITEM LEFT JOIN ExchangeOrders E ON A.ITEM = E.ITEM "+extCond);
			sql.append("GROUP BY A.ITEM,A.ITEMDESC,A.CATLOGPATH,A.UNITPRICE,A.COST,PO.ITEM_RATES,PO.OUTBOUND_GST,E.EXCOST,E.EXPRICE; ");
		}else if(ispos.equalsIgnoreCase("1")) {
			sql.append("WITH ProcessedOrders AS ( ");
			sql.append("SELECT A.DONO, B.ITEM, SUM(B.QTYOR) AS QTYOR, SUM(B.UNITCOST * B.QTYOR) AS TOTAL_UNITCOST, "); 
			sql.append("SUM(B.UNITPRICE * B.QTYOR) AS TOTAL_UNITPRICE,SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE, '%') = '%' ");
			sql.append("THEN (B.UNITPRICE * B.QTYOR) * (B.DISCOUNT / 100) ELSE B.DISCOUNT END) AS TOTAL_DISCOUNT, ");
			sql.append("A.ITEM_RATES, A.OUTBOUND_GST ");
			sql.append("FROM ["+ plant +"_DOHDR] A JOIN ["+ plant +"_DODET] B ON A.DONO = B.DONO JOIN ["+ plant +"_SHIPHIS] S ON B.DONO = S.DONO AND B.ITEM=S.ITEM AND B.DOLNNO=S.DOLNO ");
			sql.append("WHERE S.STATUS='C' "+posext+" AND A.PLANT = '"+ plant +"' AND S.LOC LIKE '%"+loc+"%' "+bcondition1+" ");
			sql.append("GROUP BY A.DONO, B.ITEM, A.ITEM_RATES, A.OUTBOUND_GST), ");
			sql.append("ExchangeOrders AS (SELECT PE.ITEM, SUM(ISNULL(I.COST, 0) * PE.QTY) AS EXCOST,SUM(ISNULL(PE.UNITPRICE, 0) * PE.QTY) AS EXPRICE ");
			sql.append("FROM "+ plant +"_POSEXCHANGEDET PE JOIN "+ plant +"_ITEMMST I ON I.ITEM = PE.ITEM ");
			sql.append("JOIN ["+ plant +"_DOHDR] A ON PE.EXDONO = A.DONO ");
			sql.append("WHERE A.ORDER_STATUS = 'PROCESSED' "+posext+" AND A.PLANT = '"+ plant +"' "+bcondition1+" ");
			sql.append("GROUP BY PE.ITEM), ");
			
//			//added pos for both sales
//			String poscond= bcondition1.replace("A.DELDATE", "F.DELDATE");
//			sql.append("POSOrders AS (SELECT F.DONO, D.ITEM, SUM(D.QTYOR) AS QTYOR, SUM(D.UNITCOST * D.QTYOR) AS TOTAL_UNITCOST, SUM(D.UNITPRICE * D.QTYOR) AS TOTAL_UNITPRICE,SUM(CASE WHEN ISNULL(D.DISCOUNT_TYPE, '%') = '%' THEN (D.UNITPRICE * D.QTYOR) * (D.DISCOUNT / 100) ELSE D.DISCOUNT END) AS TOTAL_DISCOUNT, F.ITEM_RATES, F.OUTBOUND_GST  ");
//			sql.append("FROM ["+plant+"_FINPOSHDR] F JOIN ["+plant+"_FINPOSDET] D ON F.DONO = D.DONO ");
//			sql.append("WHERE F.PLANT = '"+ plant +"' "+poscond+" ");
//			sql.append(" GROUP BY F.DONO, D.ITEM, F.ITEM_RATES, F.OUTBOUND_GST) ");
//			//end
			
			//added Salespos Qty
			sql.append("SalesPOSOrders AS ( SELECT A.DONO, B.ITEM, SUM(B.QTYOR) AS QTYOR ");
			sql.append("FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO = B.DONO JOIN ["+plant+"_SHIPHIS] S ON B.DONO = S.DONO AND B.ITEM=S.ITEM AND B.DOLNNO=S.DOLNO ");
			sql.append("WHERE S.STATUS='C' "+posext+" AND A.PLANT = '"+ plant +"' AND A.ORDERTYPE = 'POS'  AND S.LOC LIKE '%"+loc+"%' "+bcondition1+" ");
			sql.append("GROUP BY A.DONO, B.ITEM), ");
			//end
			
			//added SalesReturn Qty
			String SReturncond= bcondition1.replace("A.DELDATE", "F.RETURN_DATE ");
			sql.append("SoReturnorder AS (SELECT F.DONO, F.ITEM, SUM(F.RETURN_QTY) AS QTYOR ");
			sql.append("FROM ["+plant+"_FINSORETURN] F ");
			sql.append("WHERE F.PLANT = '"+ plant +"' "+SReturncond+" ");
			sql.append("GROUP BY F.DONO, F.ITEM), ");
			//end
			
			//added Purchase Qty
			String purcondcond= bcondition1.replace("A.DELDATE", "A.CollectionDate ");
			sql.append("PurchaseOrders AS ( SELECT A.PONO, B.ITEM, SUM(B.QTYOR) AS QTYOR ");
			sql.append("FROM ["+plant+"_POHDR] A JOIN ["+plant+"_PODET] B ON A.PONO = B.PONO JOIN ["+plant+"_RECVDET] S ON B.PONO = S.PONO AND B.ITEM=S.ITEM AND B.POLNNO=S.LNNO ");
			sql.append("WHERE A.STATUS='C' "+posext+" AND A.PLANT = '"+ plant +"' AND S.LOC LIKE '%"+loc+"%' "+purcondcond+" ");
			sql.append("GROUP BY A.PONO, B.ITEM), ");
			//end
			
			
			//added PoReturn Qty
			sql.append("PoReturnorder AS (SELECT F.PONO, F.ITEM, SUM(F.RETURN_QTY) AS QTYOR ");
			sql.append("FROM ["+plant+"_FINPORETURN] F ");
			sql.append("WHERE F.PLANT = '"+ plant +"' "+SReturncond+" ");
			sql.append("GROUP BY F.PONO, F.ITEM) ");
			//end
			
			sql.append("SELECT A.ITEM AS ID,A.ITEMDESC AS NAME,A.UNITPRICE,isnull(A.COST,0) as COST,ISNULL(SUM(PO.QTYOR), 0) AS TOTQTY, ");
			sql.append("ISNULL(SUM(SPOS.QTYOR), 0) AS POSQTY,ISNULL(SUM(PU.QTYOR), 0) AS PUQTY,ISNULL(SUM(RPU.QTYOR), 0) AS RPUQTY,ISNULL(SUM(SPU.QTYOR), 0) AS RSOQTY, ");
			sql.append("ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'../jsp/dist/img/NO_IMG.png') AS CATALOG, ");
			sql.append("ROUND(ISNULL(SUM(PO.TOTAL_UNITCOST), 0),5) - ISNULL(E.EXCOST, 0) AS TOTCOST, ");
			sql.append("ISNULL(SUM(PO.TOTAL_UNITPRICE - PO.TOTAL_DISCOUNT) ");
			sql.append("+ CASE WHEN ISNULL(PO.ITEM_RATES, 1) = 1 "); 
			sql.append("THEN SUM(PO.TOTAL_UNITPRICE) - ((SUM(PO.TOTAL_UNITPRICE) / (100 + PO.OUTBOUND_GST)) * 100) ");
			sql.append("ELSE (SUM(PO.TOTAL_UNITPRICE) / 100) * PO.OUTBOUND_GST END "); 
			sql.append("- ISNULL(E.EXPRICE, 0), 0) AS TOTPRICE, ");
			sql.append("ISNULL((SELECT SUM(QTY) FROM ["+ plant +"_Invmst] WHERE PLANT = '"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM = A.ITEM), 0) AS QTY ");
//		   			sql.append(",ISNULL((SELECT TOP 1 IV.UPAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM), '') AS UPAT ");
			sql.append(", CASE WHEN ISNULL((SELECT TOP 1 IV.UPAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM), '') = '' THEN ISNULL((SELECT TOP 1 IV.CRAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM), '') ELSE (SELECT TOP 1 IV.UPAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM) END AS UPAT ");
			sql.append("FROM ["+ plant +"_ITEMMST] A LEFT JOIN ProcessedOrders PO ON A.ITEM = PO.ITEM LEFT JOIN ExchangeOrders E ON A.ITEM = E.ITEM ");
//			sql.append("LEFT JOIN POSOrders POS ON A.ITEM = POS.ITEM  "+extCond);
			sql.append("LEFT JOIN SalesPOSOrders SPOS ON A.ITEM = SPOS.ITEM ");
			sql.append("LEFT JOIN PurchaseOrders PU ON A.ITEM = PU.ITEM ");
			sql.append("LEFT JOIN PoReturnorder RPU ON A.ITEM = RPU.ITEM ");
			sql.append("LEFT JOIN SoReturnorder SPU ON A.ITEM = SPU.ITEM "+extCond);
			sql.append("GROUP BY A.ITEM,A.ITEMDESC,A.CATLOGPATH,A.UNITPRICE,A.COST,PO.ITEM_RATES,PO.OUTBOUND_GST,E.EXCOST,E.EXPRICE; ");
		}else {
			sql.append("WITH ProcessedOrders AS ( ");
			sql.append("SELECT A.DONO, B.ITEM, SUM(B.QTYOR) AS QTYOR, SUM(B.UNITCOST * B.QTYOR) AS TOTAL_UNITCOST, "); 
			sql.append("SUM(B.UNITPRICE * B.QTYOR) AS TOTAL_UNITPRICE,SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE, '%') = '%' ");
			sql.append("THEN (B.UNITPRICE * B.QTYOR) * (B.DISCOUNT / 100) ELSE B.DISCOUNT END) AS TOTAL_DISCOUNT, ");
			sql.append("A.ITEM_RATES, A.OUTBOUND_GST ");
			sql.append("FROM ["+ plant +"_DOHDR] A JOIN ["+ plant +"_DODET] B ON A.DONO = B.DONO JOIN ["+ plant +"_SHIPHIS] S ON B.DONO = S.DONO AND B.ITEM=S.ITEM AND B.DOLNNO=S.DOLNO ");
			sql.append("WHERE S.STATUS='C' "+posext+" AND A.PLANT = '"+ plant +"' AND S.LOC LIKE '%"+loc+"%' "+bcondition1+" ");
			sql.append("GROUP BY A.DONO, B.ITEM, A.ITEM_RATES, A.OUTBOUND_GST), ");
			sql.append("ExchangeOrders AS (SELECT PE.ITEM, SUM(ISNULL(I.COST, 0) * PE.QTY) AS EXCOST,SUM(ISNULL(PE.UNITPRICE, 0) * PE.QTY) AS EXPRICE ");
			sql.append("FROM "+ plant +"_POSEXCHANGEDET PE JOIN "+ plant +"_ITEMMST I ON I.ITEM = PE.ITEM ");
			sql.append("JOIN ["+ plant +"_DOHDR] A ON PE.EXDONO = A.DONO ");
			sql.append("WHERE A.ORDER_STATUS = 'PROCESSED' "+posext+" AND A.PLANT = '"+ plant +"' "+bcondition1+" ");
			sql.append("GROUP BY PE.ITEM) ");
			sql.append("SELECT A.ITEM AS ID,A.ITEMDESC AS NAME,A.UNITPRICE,isnull(A.COST,0) as COST,ISNULL(SUM(PO.QTYOR), 0) AS TOTQTY, ");
			sql.append("ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'../jsp/dist/img/NO_IMG.png') AS CATALOG, ");
			sql.append("ROUND(ISNULL(SUM(PO.TOTAL_UNITCOST), 0),5) - ISNULL(E.EXCOST, 0) AS TOTCOST, ");
			sql.append("ISNULL(SUM(PO.TOTAL_UNITPRICE - PO.TOTAL_DISCOUNT) ");
			sql.append("+ CASE WHEN ISNULL(PO.ITEM_RATES, 1) = 1 "); 
			sql.append("THEN SUM(PO.TOTAL_UNITPRICE) - ((SUM(PO.TOTAL_UNITPRICE) / (100 + PO.OUTBOUND_GST)) * 100) ");
			sql.append("ELSE (SUM(PO.TOTAL_UNITPRICE) / 100) * PO.OUTBOUND_GST END "); 
			sql.append("- ISNULL(E.EXPRICE, 0), 0) AS TOTPRICE, ");
			sql.append("ISNULL((SELECT SUM(QTY) FROM ["+ plant +"_Invmst] WHERE PLANT = '"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM = A.ITEM), 0) AS QTY ");
//		   				sql.append(",ISNULL((SELECT TOP 1 IV.UPAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM), '') AS UPAT ");
			sql.append(", CASE WHEN ISNULL((SELECT TOP 1 IV.UPAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM), '') = '' THEN ISNULL((SELECT TOP 1 IV.CRAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM), '') ELSE (SELECT TOP 1 IV.UPAT FROM ["+plant+"_Invmst] IV WHERE PLANT = '"+plant+"' AND LOC LIKE '%%' AND ITEM = A.ITEM) END AS UPAT ");
			sql.append("FROM ["+ plant +"_ITEMMST] A LEFT JOIN ProcessedOrders PO ON A.ITEM = PO.ITEM LEFT JOIN ExchangeOrders E ON A.ITEM = E.ITEM "+extCond);
			sql.append("GROUP BY A.ITEM,A.ITEMDESC,A.CATLOGPATH,A.UNITPRICE,A.COST,PO.ITEM_RATES,PO.OUTBOUND_GST,E.EXCOST,E.EXPRICE; ");
		}
		
	}
	else{
		if(ispos.equalsIgnoreCase("3")) {
			sql.append(" select CUSTNO as ID ,CNAME as NAME, ");  
			sql.append(" (SELECT ISNULL(SUM(PICKQTY),0) FROM [" + plant +"_SHIPHIS] where (DONO in (SELECT SS.DONO FROM ["+plant+"_FINPOSHDR] SS "+posext+")) AND PLANT='"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND CNAME=A.CNAME "+extCond+bcondition+")AS TOTQTY,");
			sql.append(" (SELECT isnull(SUM(PICKQTY*UNITPRICE),0)  FROM [" + plant +"_SHIPHIS] where (DONO in (SELECT SS.DONO FROM ["+plant+"_FINPOSHDR] SS "+posext+")) AND PLANT='"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND CNAME=A.CNAME "+extCond+bcondition+")AS TOTPRICE, ");
			if(loc.equalsIgnoreCase(""))		   		
				sql.append(" ISNULL((SELECT ISNULL(SUM(QTY),0) FROM [" + plant +"_SHIPHIS] S JOIN [" + plant +"_INVMST] I ON I.ITEM=S.ITEM where (DONO in (SELECT SS.DONO FROM ["+plant+"_FINPOSHDR] SS "+posext+")) AND S.PLANT='"+ plant +"' AND S.ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and S.STATUS='C' AND CNAME=A.CNAME "+extCond1+bcondition+"),0)AS QTY");
			else
				sql.append(" ISNULL((SELECT ISNULL(SUM(QTY),0) FROM [" + plant +"_SHIPHIS] S JOIN [" + plant +"_INVMST] I ON I.ITEM=S.ITEM where (DONO in (SELECT SS.DONO FROM ["+plant+"_FINPOSHDR] SS "+posext+")) AND S.PLANT='"+ plant +"' AND I.LOC LIKE '%"+loc+"%' AND S.LOC LIKE '%"+loc+"%' AND S.ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and S.STATUS='C' AND CNAME=A.CNAME "+extCond1+bcondition+"),0)AS QTY");
			sql.append(" FROM ["+ plant +"_CUSTMST]A "+cond);
		}else {
			sql.append(" select CUSTNO as ID ,CNAME as NAME, ");  
			//sql.append(" (SELECT ISNULL(SUM(PICKQTY),0) FROM [" + plant +"_SHIPHIS] where (DONO like 'S%' OR DONO like 'TI%') AND PLANT='"+ plant +"' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND CNAME=A.CNAME "+bcondition+")AS TOTQTY,");
			sql.append(" (SELECT ISNULL(SUM(PICKQTY),0) FROM [" + plant +"_SHIPHIS] where (DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS "+posext+")) AND PLANT='"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND CNAME=A.CNAME "+extCond+bcondition+")AS TOTQTY,");
			sql.append(" (SELECT isnull(SUM(PICKQTY*UNITPRICE),0)  FROM [" + plant +"_SHIPHIS] where (DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS "+posext+")) AND PLANT='"+ plant +"' AND LOC LIKE '%"+loc+"%' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and STATUS='C' AND CNAME=A.CNAME "+extCond+bcondition+")AS TOTPRICE, ");
			if(loc.equalsIgnoreCase(""))		   		
				sql.append(" ISNULL((SELECT ISNULL(SUM(QTY),0) FROM [" + plant +"_SHIPHIS] S JOIN [" + plant +"_INVMST] I ON I.ITEM=S.ITEM where (DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS "+posext+")) AND S.PLANT='"+ plant +"' AND S.ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and S.STATUS='C' AND CNAME=A.CNAME "+extCond1+bcondition+"),0)AS QTY");
			else
				sql.append(" ISNULL((SELECT ISNULL(SUM(QTY),0) FROM [" + plant +"_SHIPHIS] S JOIN [" + plant +"_INVMST] I ON I.ITEM=S.ITEM where (DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS "+posext+")) AND S.PLANT='"+ plant +"' AND I.LOC LIKE '%"+loc+"%' AND S.LOC LIKE '%"+loc+"%' AND S.ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) and S.STATUS='C' AND CNAME=A.CNAME "+extCond1+bcondition+"),0)AS QTY");
			sql.append(" FROM ["+ plant +"_CUSTMST]A "+cond);
		}
	}
	
	
	try {
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

//getAllCompProductReorderqty
public ArrayList getAllCompProductReorderqty(String plant,String item,String itemDesc,String custname,String loc,
		String sort, String fromdt,String todt,String ispos,String isCompbased) throws Exception {
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;
	String extCond = "";
	String extCond1 = "";
	String  fdate = "", tdate = "";
	String parentplant = plant;
	
	
	if(fromdt==null) fromdt=""; else fromdt = fromdt.trim();
	if (fromdt.length()>5)
		fdate    = fromdt.substring(6)+fromdt.substring(3,5)+fromdt.substring(0,2);
	
	if(todt==null) todt=""; else todt = todt.trim();
	if (todt.length()>5)
		tdate    = todt.substring(6)+todt.substring(3,5)+todt.substring(0,2);
	
	if (item.length() > 0 ) {
		if(sort.equalsIgnoreCase("PRODUCT"))
			extCond = " where A.ITEM='"+item+"' ";
		else
			extCond = " AND ITEM='"+item+"' ";
		extCond1 = " AND S.ITEM='"+item+"' ";
	}
	
	if (itemDesc.length() > 0 ) {
		if (itemDesc.indexOf("%") != -1) {
			itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		}
		extCond = " where replace(ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	}
	
	String cond = "",condt="";
	if (custname.length()>0){
		custname = StrUtils.InsertQuotes(custname);
		cond =  " WHERE CNAME LIKE '%"+custname+"%' " ;
	}
	if (item.length()>0){
		condt = "and A.ITEM='"+item+"' ";
	}
	if (custname.length()>0){
//		condt = condt  + " and A.VENDNO = '"+custname+"'";
		condt = condt  + " and PU.CustCode = '"+custname+"'";
	}
	String bcondition = "";
	String bcondition1 = "";
	if (fdate.length() > 0) {
		bcondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)  >= '" + fdate
				+ "'  ";
		bcondition1 =" AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2)  >= '" + fdate
				+ "'  ";
		if (tdate.length() > 0) {
			bcondition = bcondition + " AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)   <= '" + tdate
					+ "'  ";
			bcondition1 = bcondition1 + " AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2)   <= '" + tdate
					+ "'  ";
		}
	} else {
		if (tdate.length() > 0) {
			bcondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2) <= '" + tdate
					+ "'  ";
			bcondition1 =" AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2) <= '" + tdate
					+ "'  ";
		}
	}
	
	//POSSEARCH
	String posext="";
	StringBuffer sql = new StringBuffer();
	
	ArrayList addcomp = new ArrayList();
	Hashtable htData = new Hashtable();	
	String childplant ="";
	String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
  	ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
  	addcomp.add(plant);
      	if (arrList.size() > 0) {
      		for (int i=0; i < arrList.size(); i++ ) {
      			Map m = (Map) arrList.get(i);
      			childplant = (String) m.get("CHILD_PLANT");
      			addcomp.add(childplant);
      		  }
      	  }
      	
      	
      	
      	if(isCompbased.equalsIgnoreCase("0")) {
      		addcomp.clear();
      		addcomp.add(plant);
      	}
      	
      	
      	StringBuilder queryExchangeBuilder = new StringBuilder();
      	StringBuilder querySalesPosBuilder = new StringBuilder();
		StringBuilder querySalesBuilder = new StringBuilder();
		StringBuilder querySalesReturnBuilder = new StringBuilder();
		StringBuilder queryPurchaseBuilder = new StringBuilder();
		StringBuilder queryPurchaseReturnBuilder = new StringBuilder();
		StringBuilder queryInventoryBuilder = new StringBuilder();
	
	if(sort.equalsIgnoreCase("PRODUCT")){
		
		if(ispos.equalsIgnoreCase("1")) {
			
			
			querySalesBuilder.append("WITH ProcessedOrders AS (");
	      	for (int i = 0; i < addcomp.size(); i++) { plant = (String) addcomp.get(i);
	      	    if (i > 0) {querySalesBuilder.append(" UNION ");}
	      	    
	      	  querySalesBuilder.append("SELECT B.PLANT, B.ITEM, SUM(B.QTYOR) AS QTYOR, ")
                .append("SUM(B.UNITCOST * B.QTYOR) AS TOTAL_UNITCOST, ")
                .append("SUM(B.UNITPRICE * B.QTYOR) AS TOTAL_UNITPRICE, ")
                .append("SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE, '%') = '%' ")
                .append("THEN (B.UNITPRICE * B.QTYOR) * (B.DISCOUNT / 100) ELSE B.DISCOUNT END) AS TOTAL_DISCOUNT, ")
                .append("A.ITEM_RATES, A.OUTBOUND_GST ")
                .append("FROM [").append(plant).append("_DOHDR] A ")
                .append("JOIN [").append(plant).append("_DODET] B ON A.DONO = B.DONO ")
                .append("JOIN [").append(plant).append("_SHIPHIS] S ON B.DONO = S.DONO AND B.ITEM = S.ITEM AND B.DOLNNO = S.DOLNO ")
                .append("WHERE S.STATUS='C' AND A.PLANT = '").append(plant).append("' AND S.LOC LIKE '%"+loc+"%' "+bcondition1+" ")
                .append("GROUP BY B.PLANT, B.ITEM, A.ITEM_RATES, A.OUTBOUND_GST ");
	      	}
	      	querySalesBuilder.append("), ");
			String salesQuery = querySalesBuilder.toString();
			
			queryExchangeBuilder.append("ExchangeOrders AS (");
			for (int i = 0; i < addcomp.size(); i++) {plant = (String) addcomp.get(i);
				if (i > 0) {queryExchangeBuilder.append(" UNION ");}
				
				queryExchangeBuilder.append("SELECT PE.ITEM, SUM(ISNULL(I.COST, 0) * PE.QTY) AS EXCOST,SUM(ISNULL(PE.UNITPRICE, 0) * PE.QTY) AS EXPRICE  ")
				.append("FROM [").append(plant).append("_POSEXCHANGEDET] PE ")
				.append("JOIN [").append(plant).append("_ITEMMST] I ON I.ITEM = PE.ITEM ")
				.append("JOIN [").append(plant).append("_DOHDR] A ON PE.EXDONO = A.DONO ")
				.append("WHERE A.ORDER_STATUS = 'PROCESSED'  AND A.PLANT = '").append(plant).append("'")
				.append(" "+bcondition1+" ")
				.append(" GROUP BY PE.ITEM ");
			}
			queryExchangeBuilder.append("), ");
			String ExchangeQuery = queryExchangeBuilder.toString();
			
			querySalesPosBuilder.append("SalesPOSOrders AS (");
			for (int i = 0; i < addcomp.size(); i++) {
				plant = (String) addcomp.get(i);
				if (i > 0) {querySalesPosBuilder.append(" UNION ");}
				
				querySalesPosBuilder.append("SELECT A.PLANT, B.ITEM, SUM(B.QTYOR) AS QTYOR FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO = B.DONO JOIN ["+plant+"_SHIPHIS] S ON B.DONO = S.DONO AND B.ITEM=S.ITEM AND ")
				.append("B.DOLNNO=S.DOLNO WHERE S.STATUS='C'  AND A.PLANT = '"+plant+"' AND A.ORDERTYPE = 'POS'  AND S.LOC LIKE '%%' ")
				.append(" "+bcondition1+" ")
				.append("GROUP BY A.PLANT, B.ITEM ");
			}
			
			querySalesPosBuilder.append("), ");
			String SalesPosQuery = querySalesPosBuilder.toString();
			
			querySalesReturnBuilder.append("SoReturnorder AS (");
			String SReturncond= bcondition1.replace("A.DELDATE", "F.RETURN_DATE ");
			for (int i = 0; i < addcomp.size(); i++) {
				plant = (String) addcomp.get(i);
				if (i > 0) {
					querySalesReturnBuilder.append(" UNION ");
				}
				
				querySalesReturnBuilder.append("SELECT F.PLANT, F.ITEM, SUM(F.RETURN_QTY) AS QTYOR FROM ["+plant+"_FINSORETURN] F WHERE F.PLANT = '"+plant+"' ")
				.append(" "+SReturncond+" ")
				.append("GROUP BY F.PLANT, F.ITEM ");
			}
			
			querySalesReturnBuilder.append("), ");
			String SalesReturnQuery = querySalesReturnBuilder.toString();
			
			queryPurchaseBuilder.append("PurchaseOrders AS (");
			String purcondcond= bcondition1.replace("A.DELDATE", "A.CollectionDate ");
			for (int i = 0; i < addcomp.size(); i++) {plant = (String) addcomp.get(i);
				if (i > 0) {queryPurchaseBuilder.append(" UNION ");}
				
//				queryPurchaseBuilder.append("SELECT A.PLANT, B.ITEM, SUM(B.QTYOR) AS QTYOR FROM ["+plant+"_POHDR] A JOIN ["+plant+"_PODET] B ON A.PONO = B.PONO JOIN ["+plant+"_RECVDET] S ON B.PONO = S.PONO AND B.ITEM=S.ITEM ")
//				.append("AND B.POLNNO=S.LNNO WHERE A.STATUS='C'  AND A.PLANT = '"+plant+"' AND S.LOC LIKE '%"+loc+"%'  "+purcondcond+" ")
//				.append("GROUP BY A.PLANT, B.ITEM ");
				queryPurchaseBuilder.append("SELECT A.PLANT, B.ITEM,A.CustCode, SUM(B.QTYOR) AS QTYOR FROM ["+plant+"_POHDR] A JOIN ["+plant+"_PODET] B ON A.PONO = B.PONO JOIN ["+plant+"_RECVDET] S ON B.PONO = S.PONO AND B.ITEM=S.ITEM ")
				.append("AND B.POLNNO=S.LNNO WHERE A.STATUS='C'  AND A.PLANT = '"+plant+"' AND S.LOC LIKE '%"+loc+"%'  "+purcondcond+" ")
				.append("GROUP BY A.PLANT, B.ITEM,A.CustCode");
			}
			
			queryPurchaseBuilder.append("), ");
			String PurchaseQuery = queryPurchaseBuilder.toString();
			
			queryPurchaseReturnBuilder.append("PoReturnorder AS (");
			for (int i = 0; i < addcomp.size(); i++) {plant = (String) addcomp.get(i);
				if (i > 0) {queryPurchaseReturnBuilder.append(" UNION ");}
				
				queryPurchaseReturnBuilder.append("SELECT F.PLANT, F.ITEM, SUM(F.RETURN_QTY) AS QTYOR FROM ["+plant+"_FINPORETURN] F WHERE F.PLANT = '"+plant+"' ")
				.append(" "+SReturncond+" ")
				.append("GROUP BY F.PLANT, F.ITEM ");
			}
			
			queryPurchaseReturnBuilder.append("), ");
			String PurchaseReturnQuery = queryPurchaseReturnBuilder.toString();
			
			queryInventoryBuilder.append("inventory AS (");
			for (int i = 0; i < addcomp.size(); i++) {plant = (String) addcomp.get(i);
			if (i > 0) {queryInventoryBuilder.append(" UNION ");}
			
			queryInventoryBuilder.append("SELECT F.PLANT, F.ITEM, SUM(F.QTY) AS QTYOR FROM ["+plant+"_Invmst] F WHERE F.PLANT = '"+plant+"' ")
			.append("GROUP BY F.PLANT, F.ITEM ");
			}
			
			queryInventoryBuilder.append(") ");
			String InventoryQuery = queryInventoryBuilder.toString();
			
			String finalquery = salesQuery+ExchangeQuery+SalesPosQuery+SalesReturnQuery+PurchaseQuery+PurchaseReturnQuery+InventoryQuery;
			finalquery = finalquery + " SELECT A.ITEM AS ID,A.ITEMDESC AS NAME,isnull(A.UNITPRICE,0) as UNITPRICE,isnull(A.COST,0) as COST, "
					+ "ISNULL((SELECT SUM(QTYOR) FROM ProcessedOrders PO where PO.ITEM = A.ITEM), 0) AS TOTQTY, "
					+ "ISNULL((SELECT SUM(QTYOR) FROM SalesPOSOrders SPOS where SPOS.ITEM = A.ITEM), 0) AS POSQTY, "
					+ "ISNULL((SELECT SUM(QTYOR) FROM SoReturnorder SPU where SPU.ITEM = A.ITEM), 0) AS RSOQTY, "
					+ "ISNULL((SELECT SUM(QTYOR) FROM PurchaseOrders PU where PU.ITEM = A.ITEM), 0) AS PUQTY, "
					+ "ISNULL((SELECT SUM(QTYOR) FROM PoReturnorder RPU where RPU.ITEM = A.ITEM), 0) AS RPUQTY, "
					+ "ISNULL((SELECT SUM(QTYOR) FROM inventory INV where INV.ITEM = A.ITEM), 0) AS QTY,0 TOTCOST,0 TOTPRICE "
					+ ",ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'../jsp/dist/img/NO_IMG.png') AS CATALOG "
//					+ ",ROUND(ISNULL(SUM(PO.TOTAL_UNITCOST), 0),5) - ISNULL(E.EXCOST, 0) AS TOTCOST, ISNULL(SUM(PO.TOTAL_UNITPRICE - PO.TOTAL_DISCOUNT) + CASE WHEN ISNULL(PO.ITEM_RATES, 1) = 1  "
//					+ "THEN SUM(PO.TOTAL_UNITPRICE) - ((SUM(PO.TOTAL_UNITPRICE) / (100 + PO.OUTBOUND_GST)) * 100) ELSE (SUM(PO.TOTAL_UNITPRICE) / 100) * PO.OUTBOUND_GST END - ISNULL(E.EXPRICE, 0), 0) AS TOTPRICE "
//					+ " ISNULL(SUM(PO.QTYOR),0) AS QTYOR, "
//					+ "	ISNULL(SUM(SPOS.QTYOR),0) AS SQTYOR, "
//					+ "	ISNULL(SUM(SPU.QTYOR),0) AS SRQTYOR, "
//					+ "	ISNULL(SUM(PU.QTYOR/2/2),0) AS POQTYOR, "
//					+ "	ISNULL(SUM(RPU.QTYOR),0) AS PROQTYOR, "
//					+ " ISNULL(SUM(PO.TOTAL_UNITCOST),0) AS TOTAL_UNITCOST, "
//					+ " ISNULL(SUM(PO.TOTAL_UNITPRICE),0) AS TOTAL_UNITPRICE, "
//					+ " ISNULL(SUM(PO.TOTAL_DISCOUNT),0) AS TOTAL_DISCOUNT, "
//					+ " MAX(PO.ITEM_RATES) AS ITEM_RATES, "
//					+ " MAX(PO.OUTBOUND_GST) AS OUTBOUND_GST "
					+ "FROM ["+parentplant+"_ITEMMST] A "
					+ "LEFT JOIN ProcessedOrders PO ON A.ITEM = PO.ITEM "
					+ "LEFT JOIN ExchangeOrders E ON A.ITEM = E.ITEM "
					+ "LEFT JOIN SalesPOSOrders SPOS ON A.ITEM = SPOS.ITEM "
					+ "LEFT JOIN SoReturnorder SPU ON A.ITEM = SPU.ITEM "
					+ "LEFT JOIN PurchaseOrders PU ON A.ITEM = PU.ITEM "
					+ "LEFT JOIN PoReturnorder RPU ON A.ITEM = RPU.ITEM "
					+ "where A.IsActive='Y' "+condt+" "
					+ "GROUP BY A.ITEM,A.ITEMDESC,A.CATLOGPATH,A.UNITPRICE,A.COST" ;
//					+ "GROUP BY A.ITEM,A.ITEMDESC,A.CATLOGPATH,A.UNITPRICE,A.COST,PO.ITEM_RATES,PO.OUTBOUND_GST,E.EXCOST,E.EXPRICE" ;
//					+ "GROUP BY A.ITEM;" ;
			
			sql.append(" "+finalquery+" ");
			//end
		}
		
	}
	
	
	try {
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

//BY IMTHI 22-02-2023 for Sales Performance Summary
public ArrayList getSalesPerformanceSummary(String plant,String item,String PRD_DEPT_ID,String PRD_CLS_ID,String PRD_TYPE_ID,String PRD_BRAND_ID,String custname,
		String sort, String fromdt,String todt,String[] chkdLoc,String ispos) throws Exception {
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;
	String  fdate = "", tdate = "";
	String extCond = "", extCond1 = "",extCond2 = "",extCond3 = "",extCond4 = "";
	
	String posext="";
	if(ispos.equalsIgnoreCase("3"))
   		posext =  " AND S.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE ='POS' AND  DONO=S.DONO)";
   	else if(ispos.equalsIgnoreCase("2"))
   		posext =  " AND S.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE !='POS' AND  DONO=S.DONO)";
	
	
	if(fromdt==null) fromdt=""; else fromdt = fromdt.trim();
	if (fromdt.length()>5)
		fdate    = fromdt.substring(6)+fromdt.substring(3,5)+fromdt.substring(0,2);
	
	if(todt==null) todt=""; else todt = todt.trim();
	if (todt.length()>5)
		tdate    = todt.substring(6)+todt.substring(3,5)+todt.substring(0,2);
	
	
	if (PRD_DEPT_ID.length() > 0 ) {
		if (PRD_DEPT_ID.indexOf("%") != -1) {
			PRD_DEPT_ID = PRD_DEPT_ID.substring(0, PRD_DEPT_ID.indexOf("%") - 1);
		}
		extCond = " and replace(PRD_DEPT_ID,' ','') like '%"+ StrUtils.InsertQuotes(PRD_DEPT_ID.replaceAll(" ","")) + "%' ";
	}
	
	if (PRD_CLS_ID.length() > 0 ) {
		if (PRD_CLS_ID.indexOf("%") != -1) {
			PRD_CLS_ID = PRD_CLS_ID.substring(0, PRD_CLS_ID.indexOf("%") - 1);
		}
		extCond1 = " and replace(PRD_CLS_ID,' ','') like '%"+ StrUtils.InsertQuotes(PRD_CLS_ID.replaceAll(" ","")) + "%' ";
	}
	
	if (PRD_TYPE_ID.length() > 0 ) {
		if (PRD_TYPE_ID.indexOf("%") != -1) {
			PRD_TYPE_ID = PRD_TYPE_ID.substring(0, PRD_TYPE_ID.indexOf("%") - 1);
		}
		extCond2 = " and replace(ITEMTYPE,' ','') like '%"+ StrUtils.InsertQuotes(PRD_TYPE_ID.replaceAll(" ","")) + "%' ";
	}
	
	if (PRD_BRAND_ID.length() > 0 ) {
		if (PRD_BRAND_ID.indexOf("%") != -1) {
			PRD_BRAND_ID = PRD_BRAND_ID.substring(0, PRD_BRAND_ID.indexOf("%") - 1);
		}
		extCond3 = " and replace(PRD_BRAND_ID,' ','') like '%"+ StrUtils.InsertQuotes(PRD_BRAND_ID.replaceAll(" ","")) + "%' ";
	}
	
	if (item.length() > 0 ) {
		extCond4 = " and ITEM='"+item+"'";
	}

	String cond = "";
	if (custname.length()>0){
		custname = StrUtils.InsertQuotes(custname);
		cond =  " CNAME LIKE '%"+custname+"%' " ;
	}
	
	String bcondition = "";
	if (fdate.length() > 0) {
		bcondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)  >= '" + fdate+ "'  ";
		if (tdate.length() > 0) {
			bcondition = bcondition + " AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)   <= '" + tdate+ "'  ";
		}
	} else {
		if (tdate.length() > 0) {
			bcondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2) <= '" + tdate+ "'  ";
		}
	}
	
	String Scondition = "";
	if (custname.length()==0 & chkdLoc==null){
		if (fdate.length() > 0) {
			Scondition ="  SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)  >= '" + fdate+ "'  ";
			if (tdate.length() > 0) {
				Scondition = Scondition + " AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)   <= '" + tdate+ "'  ";
			}
		} else {
			if (tdate.length() > 0) {
				Scondition ="  SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2) <= '" + tdate+ "'  ";
			}
		}
	}else {
		if (fdate.length() > 0) {
			Scondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)  >= '" + fdate+ "'  ";
			if (tdate.length() > 0) {
				Scondition = Scondition + " AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)   <= '" + tdate+ "'  ";
			}
		} else {
			if (tdate.length() > 0) {
				Scondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2) <= '" + tdate+ "'  ";
			}
		}
	}
	
	
	ArrayList check = new ArrayList();
	StringBuffer sql = new StringBuffer();
	String LOC = "";
	if (chkdLoc!=null){
		for (int i = 0; i < chkdLoc.length; i++) {
			check.add("'"+chkdLoc[i]+"'");
			String location = check.toString().replaceAll("\\[|\\]", "");
			LOC =  " LOC in ("+""+location+""+") " ;
			if (custname.length()>0){
				LOC =  " AND LOC in ("+""+location+""+") " ;
			}
		}
	}
	
	String isand ="";
	if (custname.length()==0 & chkdLoc!=null)
		isand =" AND ";
	else if (custname.length()>0 & chkdLoc!=null)
		isand =" AND ";
		
		if(sort.equalsIgnoreCase("SELLING")){
			sql.append(" select ITEM as ITEM ,ITEMDESC as NAME,A.PRD_DEPT_ID as DEPT,A.PRD_CLS_ID as CLASS,A.PRD_BRAND_ID as BRAND, ");  
			sql.append(" (SELECT ISNULL(SUM(PICKQTY),0) FROM [" + plant +"_SHIPHIS] S where (INVOICENO != '') AND PLANT='"+ plant +"' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) AND ITEM=A.ITEM "+isand+cond+LOC+posext+bcondition+")AS TOTQTY,");
			sql.append(" (SELECT ISNULL(SUM(PICKQTY*J.COST),0) FROM [" + plant +"_SHIPHIS] S join [" + plant +"_ITEMMST] J on S.ITEM = J.ITEM where (S.INVOICENO != '') AND S.PLANT='"+ plant +"' AND S.ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) AND S.ITEM=A.ITEM "+isand+cond+LOC+posext+bcondition+")AS TOTCOST,");
			sql.append(" (SELECT isnull(SUM(PICKQTY*UNITPRICE),0)  FROM [" + plant +"_SHIPHIS] S where (INVOICENO != '') AND PLANT='"+ plant +"' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) AND ITEM=A.ITEM "+isand+cond+LOC+posext+bcondition+")AS TOTPRICE ");
			sql.append(" FROM ["+ plant +"_ITEMMST]A where A.ITEM in (select ITEM from ["+plant+"_SHIPHIS] S WHERE"+cond+LOC+Scondition+" )"+extCond+extCond1+extCond2+extCond3+extCond4+" order by TOTPRICE desc");
		}else {
			sql.append(" select ITEM as ITEM ,ITEMDESC as NAME,A.PRD_DEPT_ID as DEPT,A.PRD_CLS_ID as CLASS,A.PRD_BRAND_ID as BRAND, ");  
			sql.append(" (SELECT ISNULL(SUM(PICKQTY),0) FROM [" + plant +"_SHIPHIS] S where (INVOICENO != '') AND PLANT='"+ plant +"' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) AND ITEM=A.ITEM "+isand+cond+LOC+posext+bcondition+")AS TOTQTY,");
			sql.append(" (SELECT ISNULL(SUM(PICKQTY*J.COST),0) FROM [" + plant +"_SHIPHIS] S join [" + plant +"_ITEMMST] J on S.ITEM = J.ITEM where (S.INVOICENO != '') AND S.PLANT='"+ plant +"' AND S.ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) AND S.ITEM=A.ITEM "+isand+cond+LOC+posext+bcondition+")AS TOTCOST,");
			sql.append(" (SELECT isnull(SUM(PICKQTY*UNITPRICE),0)  FROM [" + plant +"_SHIPHIS] S where (INVOICENO != '') AND PLANT='"+ plant +"' AND ITEM NOT IN (SELECT ITEM FROM ["+ plant +"_ITEMMST] WHERE NONSTKFLAG='Y' ) AND ITEM=A.ITEM "+isand+cond+LOC+posext+bcondition+")AS TOTPRICE ");
			sql.append(" FROM ["+ plant +"_ITEMMST]A where A.ITEM in (select ITEM from ["+plant+"_SHIPHIS] S WHERE"+cond+LOC+Scondition+" )"+extCond+extCond1+extCond2+extCond3+extCond4+" order by TOTPRICE asc");
		}
	try {
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

/*   calculation to get prodgst for report>Outbound Order Summary by price(sales report by outbound order)
 *   sum of dodet pickqty wher status in (N,C)
 *   sum of unitprice group by dono,item,unitprice,prodgst
 *   sum of prodgst group by dono,item,unitprice,prodgst
 */
public ArrayList getShippingProductGst(String plant,String orderno) throws Exception {
    java.sql.Connection con = null;
    ArrayList alData = new ArrayList();
    try {
    con = DbBean.getConnection();
  
    StringBuffer SqlQuery = new StringBuffer(" select dono,item,isnull(prodgst,0)prodgst,  ") ;
    SqlQuery.append("(sum(qtypick)*isnull(unitprice,0)*isnull(prodgst,0)/100) subtotal ");
    SqlQuery.append(" from ["+plant+"_DODET] where dono='"+orderno+"' and pickStatus in('O','C')  ");
    SqlQuery.append(" group by dono,item,unitprice,prodgst ");
    System.out.println(SqlQuery.toString());
//        Map m = this.getRowOfData(con, SqlQuery.toString());

        alData = selectData(con, SqlQuery.toString());

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

public ArrayList getShippingProductGST(String plant,String orderno) throws Exception {
    java.sql.Connection con = null;
    ArrayList alData = new ArrayList();
    try {
    con = DbBean.getConnection();
  
    StringBuffer SqlQuery = new StringBuffer(" select tono,item,isnull(prodgst,0)prodgst,  ") ;
    SqlQuery.append("(sum(qtypick)*isnull(unitprice,0)*isnull(prodgst,0)/100) subtotal ");
    SqlQuery.append(" from ["+plant+"_TODET] where tono='"+orderno+"' and pickStatus in('O','C')  ");
    SqlQuery.append(" group by tono,item,unitprice,prodgst ");
    System.out.println(SqlQuery.toString());
//        Map m = this.getRowOfData(con, SqlQuery.toString());

        alData = selectData(con, SqlQuery.toString());

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

public boolean insertDOHDR(Hashtable ht) throws Exception {
	boolean insertFlag = false;
	java.sql.Connection conn = null;
//	String TABLE = "DOHDR";
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
		String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_DOHDR]"
				+ "(" + FIELDS.substring(0, FIELDS.length() - 1)
				+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1)
				+ ")";
		this.mLogger.query(this.printQuery, query);

		insertFlag = insertData(conn, query);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw new Exception("Item already added to DOHDR");

	} finally {
		if (conn != null) {
			DbBean.closeConnection(conn);
		}
	}
	return insertFlag;
}

public boolean insertDODET(Hashtable ht) throws Exception {
	boolean insertFlag = false;
	java.sql.Connection conn = null;
//	String TABLE = "DODET";
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
		String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_DODET]"
				+ "(" + FIELDS.substring(0, FIELDS.length() - 1)
				+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1)
				+ ")";
		this.mLogger.query(this.printQuery, query);

		insertFlag = insertData(conn, query);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw new Exception("Item already added to DODET");

	} finally {
		if (conn != null) {
			DbBean.closeConnection(conn);
		}
	}
	return insertFlag;
}
public boolean insertTOHDR(Hashtable ht) throws Exception {
	boolean insertFlag = false;
	java.sql.Connection conn = null;
//	String TABLE = "TOHDR";
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
		String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_TOHDR]"
				+ "(" + FIELDS.substring(0, FIELDS.length() - 1)
				+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1)
				+ ")";
		this.mLogger.query(this.printQuery, query);

		insertFlag = insertData(conn, query);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw new Exception("Item already added to DOHDR");

	} finally {
		if (conn != null) {
			DbBean.closeConnection(conn);
		}
	}
	return insertFlag;
}

public boolean insertTODET(Hashtable ht) throws Exception {
	boolean insertFlag = false;
	java.sql.Connection conn = null;
//	String TABLE = "TODET";
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
		String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_TODET]"
				+ "(" + FIELDS.substring(0, FIELDS.length() - 1)
				+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1)
				+ ")";
		this.mLogger.query(this.printQuery, query);

		insertFlag = insertData(conn, query);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw new Exception("Item already added to DODET");

	} finally {
		if (conn != null) {
			DbBean.closeConnection(conn);
		}
	}
	return insertFlag;
}
public ArrayList getShipHisDetailByTranId(String plant, String tranId)
		throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();

//		boolean flag = false;

		String sql = "SELECT REFNO,EMPNO FROM ["+plant+"_SHIPHIS] WHERE DONO='"+tranId+"'";

		this.mLogger.query(this.printQuery, sql);
		al = selectData(con, sql);

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

public boolean isExisited(String plant,String dono) throws Exception {
	boolean flag = false;
	java.sql.Connection con = null;
	String TABLE = "DOHDR";
	try {
		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append("COUNT(*) ");
		sql.append(" ");
		sql.append(" FROM " + "[" + plant + "_" + TABLE + "]");
		sql.append(" WHERE  DONO='" +dono+"'" );
/*
		if (extCond.length() > 0)
			sql.append(" and " + extCond);*/

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

public ArrayList getShipHisNewDetail(String plant, String orderno)
		throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();

//		boolean flag = false;

		StringBuffer sQry = new StringBuffer(
				" SELECT DOLNO,ITEM,ITEMDESC,SUM(PICKQTY) as PICKQTY,BATCH,ID,LOC,SID FROM "
						+ "[" + plant + "_" + "shiphis" + "]" + "");
		sQry
				.append(" where plant='"
						+ plant
						+ "' and dono = '"
						+ orderno
						+ "' and isnull(status,'')  <> 'C' group by DOLNO,item,itemdesc,batch,id,loc,SID ");

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

public ArrayList getsalesforecasting(String plant, String datawise, String FDate1, String TDate1, String FDate2, String TDate2, String FDate3, String TDate3, String FDate4, String TDate4)
		throws Exception {
	
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		
		
		StringBuffer sQry = new StringBuffer("");
		if(datawise.equalsIgnoreCase("PRODUCT")) {
		sQry
		.append("select IM.ITEM,"
				+ "ISNULL((select SUM(S.PICKQTY) from " + plant + "_shiphis S WHERE IM.ITEM=S.ITEM AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) >= '"+FDate4+"'   AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2)<= '"+TDate4+"'),0) as PYEAR1,"
				+ "ISNULL((select SUM(S.PICKQTY) from " + plant + "_shiphis S WHERE IM.ITEM=S.ITEM AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) >= '"+FDate3+"'   AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2)<= '"+TDate3+"'),0) as PYEAR2,"
				+ "ISNULL((select SUM(S.PICKQTY) from " + plant + "_shiphis S WHERE IM.ITEM=S.ITEM AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) >= '"+FDate2+"'   AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2)<= '"+TDate2+"'),0) as PYEAR3,"
				+ "ISNULL((select SUM(S.PICKQTY) from " + plant + "_shiphis S WHERE IM.ITEM=S.ITEM AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) >= '"+FDate1+"'   AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2)<= '"+TDate1+"'),0) as CYEAR"
				+ " from " + plant + "_ITEMMST IM");
		} else {
			sQry
			.append("select IM.PRD_CLS_ID as ITEM,"
					+ "ISNULL((select SUM(S.PICKQTY) from " + plant + "_shiphis S JOIN " + plant + "_itemmst I on I.ITEM=S.ITEM WHERE IM.PRD_CLS_ID=I.PRD_CLS_ID AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) >= '"+FDate4+"'   AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2)<= '"+TDate4+"'),0) as PYEAR1,"
					+ "ISNULL((select SUM(S.PICKQTY) from " + plant + "_shiphis S JOIN " + plant + "_itemmst I on I.ITEM=S.ITEM WHERE IM.PRD_CLS_ID=I.PRD_CLS_ID AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) >= '"+FDate3+"'   AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2)<= '"+TDate3+"'),0) as PYEAR2,"
					+ "ISNULL((select SUM(S.PICKQTY) from " + plant + "_shiphis S JOIN " + plant + "_itemmst I on I.ITEM=S.ITEM WHERE IM.PRD_CLS_ID=I.PRD_CLS_ID AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) >= '"+FDate2+"'   AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2)<= '"+TDate2+"'),0) as PYEAR3,"
					+ "ISNULL((select SUM(S.PICKQTY) from " + plant + "_shiphis S JOIN " + plant + "_itemmst I on I.ITEM=S.ITEM WHERE IM.PRD_CLS_ID=I.PRD_CLS_ID AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) >= '"+FDate1+"'   AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2)<= '"+TDate1+"'),0) as CYEAR"
					+ " from " + plant + "_PRD_CLASS_MST IM");
			
		}
		sQry
		.append(" where plant='"
				+ plant
				+ "' ");
		
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

}
