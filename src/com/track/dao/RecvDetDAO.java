package com.track.dao;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import java.util.Map;

public class RecvDetDAO extends BaseDAO {

	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.RECVDETDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.RECVDETDAO_PRINTPLANTMASTERLOG;

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

	public RecvDetDAO() {
		_StrUtils = new StrUtils();
	}

	public static String TABLE_NAME = "RECVDET";
	public String plant = "";

	public RecvDetDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "RecvDet" + "]";
	}

	public boolean isExisit(Hashtable ht, String aPlant) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "[" + aPlant + "_" + "RECVDET" + "]");
			sql.append(" WHERE  " + formCondition(ht));
			this.mLogger.query(this.printQuery, sql.toString());
			flag = isExists(con, sql.toString());
			TABLE_NAME = "RECVDET";
		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			MLogger.log(-1, "");
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}

	public boolean insertRecvDet(Hashtable ht) throws Exception {

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
					+ TABLE_NAME + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			

			this.mLogger.query(this.printQuery, query);
		
			insertFlag = insertData(conn, query);
			TABLE_NAME = "RECVDET";
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

	public boolean update(String query, Hashtable htCondition, String extCond,
			String aPlant) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "[" + aPlant + "_"
					+ "RECVDET" + "]");
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

	public ArrayList getRevInboundOrderDetailsByWMS(String plant, String orderno)
			throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			String sQry = "select distinct pono,cname as custname from " + "["
					+ plant + "_" + "recvdet" + "]" + " where plant='" + plant
					+ "' and pono like '" + orderno
					+ "%'  group by pono,cname,crat";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);
			TABLE_NAME = "RECVDET";
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

	public ArrayList getRevInboundItemListByWMS(String plant, String orderno,
			String itemno) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			String sQry = "select pono,item,isnull(loc,'') loc,isnull(ordqty,0) as qtyor,isnull(recqty,0) as qtyrc,isnull(Remark,'') as remark,isnull(cname,'') as custname from "
					+ "["
					+ plant
					+ "_"
					+ "recvdet"
					+ "]"
					+ "  where plant='"
					+ plant
					+ "' and pono = '"
					+ orderno
					+ "' and recqty <> reverseqty and item like '"
					+ itemno
					+ "%'";
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

	public ArrayList getRevLocByWMS(String plant, String orderno,
			String itemno, String loc) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select  pono,item,isnull(loc,'') loc,(select locdesc from "+plant+"_locmst where loc=a.loc) as locdesc from " + "["
					+ plant + "_" + "RECVDET] a where plant='" + plant
					+ "' and pono = '" + orderno + "' and item = '" + itemno
					+ "' and item = '" + itemno + "'and loc like '" + loc
					+ "%' group by pono,item,loc";
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

	public ArrayList getRevBatchListByWMS(String plant, String orderno,
			String itemno, String loc, String batch) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			StringBuffer sQuery = new StringBuffer(
					"select a.pono,a.cname as custname,a.item,a.itemdesc,a.loc,c.qtyor as qtyor , c.qtyrc as qtyrc,isnull(b.reverseqty,0) as qtyrev,a.remark,isnull(a.custname,'') as custname,a.batch as Batch from ");
			sQuery
					.append("(select top 1 pono,isnull(cname,'') cname,item,isnull(itemdesc,'') itemdesc,isnull(loc,'') loc,");
			sQuery
					.append("isnull(ordqty,0) as qtyor,isnull(recqty,0) as qtyrc,");
			sQuery
					.append("isnull(Remark,'') as remark,isnull(cname,'') as custname,isnull(Batch,'')as Batch from "
							+ "[" + plant + "_" + "RECVDET]");
			sQuery.append(" where plant='" + plant + "' and pono = '" + orderno
					+ "' and recqty <> reverseqty and item = '" + itemno
					+ "' and loc = '" + loc + "' and batch like '" + batch
					+ "%') a, ");
			sQuery
					.append(" (select pono,item,isnull(loc,'') loc,isnull(Batch,'')as Batch,isnull(sum(reverseqty),0) reverseqty from "
							+ "[" + plant + "_" + "RECVDET]");
			sQuery.append(" where plant='" + plant + "' and pono = '" + orderno
					+ "' and recqty <> reverseqty and item = '" + itemno
					+ "' and loc = '" + loc + "' and batch like '" + batch
					+ "%' group by pono,item,loc,batch) b, ");
			sQuery.append(" (select pono,item,qtyor,qtyrc from " + "[" + plant
					+ "_" + "PODET] where pono = '" + orderno
					+ "' and item = '" + itemno + "') c");
			sQuery
					.append(" where a.pono=b.pono and a.item=b.item and a.loc=b.loc and a.batch=b.batch and a.pono=c.pono  and a.item=c.item and b.item=c.item and b.pono=c.pono"); // b.reverseqty
			this.mLogger.query(this.printQuery, sQuery.toString());
			al = selectData(con, sQuery.toString());

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
        
  
	
	public ArrayList getTotalStockIn(Hashtable ht, String condtn,String fromdt,String todt) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		ArrayList totalStock=null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("SUM(isnull(RECQTY,0)) RECQTY ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "RECVDET" + "]");
			sql.append(" WHERE  " + formCondition(ht));
			
			
			if(fromdt.length()>0)
			{
				sql.append(" AND SUBSTRING(CRAT,0,9) > '"+DateUtils.cDateyyyymmdd(fromdt)+"'");
			}
			if(todt.length()>0)
			{
				sql.append(" AND SUBSTRING(CRAT,0,9) < '"+DateUtils.cDateyyyymmdd(todt)+"'");
			}
			if(condtn.length()>0)
			{
				sql.append(""+condtn);
			}
			this.mLogger.query(this.printQuery, sql.toString());
			totalStock = selectData(con, sql.toString());
			
		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			MLogger.log(-1, "");
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return totalStock;

	}
	
	
	public ArrayList getAverageCost(String plant, String afrmDate,String atoDate,String item,String currencyid
			
			) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		ArrayList averagecost=null;
		
		String sCondition="";
		if (afrmDate.length() > 0) {
			sCondition = sCondition + " and CRAT  >= '" + afrmDate
					+ "'  ";
			if (atoDate.length() > 0) {
				sCondition = sCondition + " and CRAT   <= '" + atoDate
						+ "'  ";
			}
		} else {
			if (atoDate.length() > 0) {
				sCondition = sCondition + " and CRAT <= '" + atoDate
						+ "'  ";
			}
		}
		
		
		try {
			/*con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT A.ITEM,A.ORDQTY,A.UNITCOST,");
			sql.append("A.TOTAL,A.AVERAGECOST");
			sql.append(" FROM");
			sql.append("(");
			sql.append(" SELECT ITEM,SUM(ORDQTY) AS ORDQTY,");
			sql.append("SUM(ISNULL(UNITCOST,0)) AS UNITCOST,");
			sql.append("SUM(ISNULL(UNITCOST,0))*SUM(ORDQTY) TOTAL,");
			sql.append("(SUM(ISNULL(UNITCOST,0))*SUM(ORDQTY)/SUM(ORDQTY))AS ACWITOUTEXCHANGE,");
			sql.append("ISNULL((SUM(UNITCOST)*SUM(ORDQTY)/SUM(ORDQTY))/(SELECT CURRENCYUSEQT FROM " + "[" + plant + "_" + "CURRENCYMST" + "] WHERE CURRENCYID=A.CURRENCYID),0) AS AVERAGECOST");
			//sql.append("CURRENCYID");
			sql.append(" FROM " + "[" + plant + "_" + "RECVDET" + "]");
			sql.append(" WHERE  ITEM='"+item+"'");
			sql.append(" AND PONO LIKE 'I%' AND ORDQTY >0 " + sCondition );
			sql.append("  GROUP BY ITEM)A ORDER BY A.ITEM ");*/
			
			
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT A.ITEM,A.ORDQTY,A.UNITCOST,");
			sql.append("A.TOTAL,ISNULL((A.AVERAGECOST*B.CURRENCYUSEQT),0) AS AVERAGECOST ");
			//sql.append("A.TOTAL,A.AVERAGECOST AS AVERAGECOST  ");
			sql.append(" FROM");
			sql.append("(");
			sql.append(" SELECT ITEM,SUM(ORDQTY) AS ORDQTY,");
			sql.append(" SUM(ISNULL(UNITCOST,0)) AS UNITCOST,");
			sql.append(" SUM(ISNULL(UNITCOST,0))*SUM(ORDQTY) TOTAL,");
			sql.append(" (SUM(ISNULL(UNITCOST,0))*SUM(ORDQTY)/SUM(ORDQTY))AS ACWITOUTEXCHANGE,");
			sql.append(" ISNULL((SUM(UNITCOST)*SUM(ORDQTY)/SUM(ORDQTY))/");
			//(SELECT CURRENCYUSEQT FROM " + "[" + plant + "_" + "CURRENCYMST" + "] WHERE CURRENCYID IN(SELECT CURRENCYID ");
			//sql.append(" FROM " + "[" + plant + "_" + "RECVDET" + "] WHERE ");
			//sql.append(" ITEM='"+item+"' AND LOC ='"+loc+"' AND BATCH='"+batch+"'AND PONO LIKE 'I%')),0) AS AVERAGECOST ");
			sql.append("(SELECT CURRENCYUSEQT " );
			sql.append(" FROM " + "[" + plant + "_" + "CURRENCYMST" + "]");
			sql.append(" WHERE CURRENCYID ='USD'),0) AS AVERAGECOST ");
			sql.append(" FROM " + "[" + plant + "_" + "RECVDET" + "]");
			sql.append(" WHERE  ITEM='"+item+"'");
			//sql.append(" AND CURRENCYID IS NOT NULL AND CURRENCYID <>'' ");
			sql.append(" AND UNITCOST IS NOT NULL  AND UNITCOST >0  AND UNITCOST <> '' " );
			sql.append(" AND PONO LIKE 'I%' AND ORDQTY >0 " + sCondition );
			sql.append(" GROUP BY ITEM");
			sql.append(" )A,  ");
			sql.append(" ( SELECT CURRENCYUSEQT ");
			sql.append(" FROM " + "[" + plant + "_" + "CURRENCYMST" + "]");
			sql.append(" WHERE  CURRENCYID='"+currencyid+"') B ");
			sql.append(" ORDER BY A.ITEM ");
			this.mLogger.query(this.printQuery, sql.toString());
			averagecost = selectData(con, sql.toString());
			
		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			MLogger.log(-1, "");
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return averagecost;

	}

    public String getAverageCostForItem(String plant, String afrmDate,String atoDate,String item,String currencyid ) throws Exception {
            boolean flag = false;
            String averageCost="";
            java.sql.Connection con = null;
            ArrayList averagecost=null;
            
            String sCondition="";
            if (afrmDate.length() > 0) {
                    sCondition = sCondition + " and CRAT  >= '" + afrmDate
                                    + "'  ";
                    if (atoDate.length() > 0) {
                            sCondition = sCondition + " and CRAT   <= '" + atoDate
                                            + "'  ";
                    }
            } else {
                    if (atoDate.length() > 0) {
                            sCondition = sCondition + " and CRAT <= '" + atoDate
                                            + "'  ";
                    }
            }
            
            
            try {
                   
                    String ConvertUnitCostToOrderCurrency = " (CAST(ISNULL(UNITCOST,0) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                                                            "  WHERE  CURRENCYID='"+DbBean.LOCAL_CURRENCY+"')/(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                                                            " WHERE  CURRENCYID=ISNULL((SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO =R.PONO),'"+DbBean.LOCAL_CURRENCY+"')) AS DECIMAL(20,4)) ) ";
                    
                    con = com.track.gates.DbBean.getConnection();
                    StringBuffer sql = new StringBuffer(" Select SUM(A.RECQTY) AS TOTRECV,SUM(A.UNITCOST) AS TOTALCOST ,CAST(SUM(A.UNITCOST)/SUM(A.RECQTY) AS DECIMAL(20,4)) AS AVERGAGE_COST from ");
                    sql.append(" (select RECQTY,CAST("+ConvertUnitCostToOrderCurrency+" * CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  ");
                    sql.append(" WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO =R.PONO)) AS DECIMAL(20,4)) ");
                    sql.append("  /CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,4)) * RECQTY AS DECIMAL(20,4)) AS UNITCOST ");
                    sql.append("   from "+plant+"_RECVDET R where item ='"+item+"' AND UNITCOST IS NOT NULL  AND UNITCOST >0  AND UNITCOST <> ''  AND TRAN_TYPE ='IB' AND ORDQTY >0 " + sCondition +"  ) A ");
                  
            
                    this.mLogger.query(this.printQuery, sql.toString());
                    
                    Map m = this.getRowOfData(con, sql.toString());
                    averageCost = StrUtils.fString((String) m.get("AVERGAGE_COST"));
                    
            } catch (Exception e) {
                    MLogger.log(0, "" + e.getMessage());
                    MLogger.log(-1, "");
                    throw e;
            } finally {
                    if (con != null) {
                            DbBean.closeConnection(con);
                    }
            }
            return averageCost;

    }


//start code added by Bruhan for inbound order reversal on 26 August 2013 
public boolean deleteRECVDET(java.util.Hashtable ht) throws Exception {
    	boolean delete = false;
    	java.sql.Connection con = null;
    	try {
    		con = DbBean.getConnection();

    		StringBuffer sql = new StringBuffer(" DELETE ");
    		sql.append(" ");
    		sql.append(" FROM " + "[" + ht.get("PLANT") + "_RECVDET]");
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
//End code added by Bruhan for inbound order reversal on 26 August 2013         	
        

//start code by Bruhan for received orders history on 25 sep 2013
public ArrayList getReceivedInboundOrderDetails(String plant, String orderno,String arecDate,String atoDate)
throws Exception {
java.sql.Connection con = null;
ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		boolean flag = false;
		String sCondition = "";
		
		
			sCondition = sCondition + " AND  substring(crat,1,8) = '"
					+ arecDate + "'  ";
		
		
		
		String sQry = "select pono,lnno,item,itemdesc,batch,loc,unitcost,ordqty,recqty,(select STKUOM from ["+plant+"_ITEMMST] where ITEM = R.item ) uom from " + "["
			+ plant + "_" + "recvdet" + "]R" + " where plant='" + plant
			+ "' and pono like '" + orderno 
			+ "%' " + sCondition + " order by CAST(LNNO as int)";
		this.mLogger.query(this.printQuery, sQry);

		al = selectData(con, sQry);
		//TABLE_NAME = "RECVDET";
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
//end code by Bruhan for received orders history on 25 sep 2013

//---Added by Bruhan on March 28 2014, Description: To Delete RecvDet for InboundOrderMultipleReverse
public boolean deleteRecvDet(java.util.Hashtable ht) throws Exception {
	boolean delete = false;
	java.sql.Connection con = null;
	try {
		con = DbBean.getConnection();

		StringBuffer sql = new StringBuffer(" DELETE ");
		sql.append(" ");
		sql.append(" FROM " + "[" + ht.get("PLANT") + "_RECVDET]");
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
//---End Added by Bruhan on March 28 2014, Description: To Delete RecvDet for InboundOrderMultipleReverse
public ArrayList getPoDetailToPrintNew(String plant, String orderno,String extcond)
		throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			String ConvertUnitCostToOrderCurrency = " ISNULL((CAST(ISNULL(UNITCOST,0) *(SELECT CURRENCYUSEQT  FROM [" +plant+"_PODET] b "
			+ " where PONO = '"
			+ orderno
			+ "' AND PLANT = '"
			+ plant
			+ "' and a.LNNO = b.POLNNO) AS DECIMAL(20,4)) ),0) ";
			StringBuffer sQry = new StringBuffer(
					" SELECT a.LNNO as POLNO,ITEM,ITEMDESC,(SELECT ISNULL(STKUOM,'') STKUOM FROM "
							+ "["
							+ plant
							+ "_itemmst] WHERE ITEM=a.ITEM"
							+ ") AS UOM," 
							+ConvertUnitCostToOrderCurrency
							+ " AS UCOST," 
							+ "ISNULL("+ConvertUnitCostToOrderCurrency		
							+ " * SUM(RECQTY),0) AS COST,MAX(ORDQTY) AS ORDQTY,ISNULL(SUM(RECQTY),0) AS RECQTY,ISNULL(RECVDATE,'') AS RECVDATE," 
							+ "(SELECT STATUS from " + plant+ "_POHDR WHERE PONO ='"+ orderno+ "' ) as STATUS,"
							+ " (SELECT CURRENCYID from " + plant+ "_POHDR WHERE PONO ='"+ orderno+ "' ) as CurrencyID"
							+ " FROM "
							+ "[" + plant + "_" + "recvdet" + "] a " + "");
			sQry.append(" where plant='" + plant + "' and pono = '" + orderno + "' and tran_type='IB' " + extcond + " group by LNNO,ITEM,ITEMDESC,UNITCOST,RECVDATE order by LNNO,item,itemdesc");
		
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

public ArrayList getRcvdDetailsforreverse(String plant, Hashtable ht)
		throws Exception {
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		boolean flag = false;
		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append(" PONO,LNNO,ITEM,BATCH,LOC,RECQTY,CRAT,ISNULL(ID,0) AS ID");
		sql.append(" ");
		sql.append(" FROM " + "[" + plant + "_" + "RECVDET" + "]");
		sql.append(" WHERE  " + formCondition(ht));
		sql.append("and ID in (select ID from " + "[" + plant + "_" + "INVMST" + "] where QTY>0)");
		sql.append(" order by RECVDATE asc");
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
public ArrayList getRcvdDetinvforreverse(String plant, String LOC, String ITEM, String BATCH,Hashtable ht)
		throws Exception {
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		boolean flag = false;
		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append(" PONO,LNNO,ITEM,BATCH,LOC,RECQTY,CRAT,");
		sql.append(" ISNULL((select top 1 ID from [" + plant + "_INVMST] I where USERFLD4 = '" + BATCH + "' AND I.LOC = '" + LOC + "' AND I.ITEM = '" + ITEM + "' AND I.PLANT = '"+ plant +"' AND I.QTY>0 order by I.ID asc),0) AS ID  ");
		sql.append(" FROM " + "[" + plant + "_" + "RECVDET" + "]");
		sql.append(" WHERE  " + formCondition(ht));
		sql.append(" order by RECVDATE asc");
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

public boolean isExisit(String query) throws Exception {
	boolean flag = false;

	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();
		this.mLogger.query(this.printQuery, query);

		flag = isExists(con, query);
		
	} catch (Exception e) {
		throw e;
	} finally {
		DbBean.closeConnection(con);
	}
	return flag;

}
public ArrayList getRecvProductGst(String plant,String orderno) throws Exception {
    java.sql.Connection con = null;
    ArrayList alData = new ArrayList();
    try {
    con = DbBean.getConnection();
  
    StringBuffer SqlQuery = new StringBuffer(" select a.pono,item,isnull(prodgst,0)prodgst,  ") ;
    SqlQuery.append("(sum(qtyrc)*(isnull(unitcost*CURRENCYUSEQT,0)+ (isnull(unitcost,0) * (((isnull(b.SHIPPINGCOST,0)+isnull(b.LOCALEXPENSES,0))*100)/ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=a.pono),0))/100)/a.CURRENCYUSEQT)*isnull(prodgst,0)/100) subtotal ");
    SqlQuery.append(" from ["+plant+"_PODET] a, ["+plant+"_pohdr] b where a.PONO=b.PONO AND a.pono='"+orderno+"' and lnstat in('O','C')  ");
    SqlQuery.append(" group by a.pono,item,unitcost,prodgst,CURRENCYUSEQT,b.SHIPPINGCOST,b.LOCALEXPENSES ");
    System.out.println(SqlQuery.toString());
        Map m = this.getRowOfData(con, SqlQuery.toString());

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
public ArrayList getRecDetDetailsByTranID(String tranId,String plant)
		throws Exception {
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		boolean flag = false;
		String sql = "SELECT REFNO,EMPNO FROM ["+plant+"_RECVDET] WHERE PONO='"+tranId+"'";

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
public boolean insertGRNtoBill(Hashtable ht) throws Exception {

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
		String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_FINGRNOTOBILL"
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

public boolean insertShippingHdr(Hashtable ht) throws Exception {
	
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
		String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_SHIPPINGHDR"
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

public boolean updateShippingHDR(String query, Hashtable htCondition, String extCond)
		throws Exception {
	boolean flag = false;
	String TABLE = "SHIPPINGHDR";
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
public boolean updateGRNtoBill(String query, Hashtable htCondition, String extCond,
		String aPlant) throws Exception {
	boolean flag = false;
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" UPDATE " + "[" + aPlant + "_"
				+ "FINGRNOTOBILL" + "]");
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

public ArrayList selectForReport(String query, Hashtable ht, String extCond)
		throws Exception {
//	boolean flag = false;
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

public ArrayList getRcvdDetailsforreverse_nonstock(String plant, Hashtable ht)
		throws Exception {
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		boolean flag = false;
		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append(" PONO,LNNO,ITEM,BATCH,LOC,RECQTY,CRAT,ISNULL(ID,0) AS ID");
		sql.append(" ");
		sql.append(" FROM " + "[" + plant + "_" + "RECVDET" + "]");
		sql.append(" WHERE  " + formCondition(ht));		
		sql.append(" order by RECVDATE asc");
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

}