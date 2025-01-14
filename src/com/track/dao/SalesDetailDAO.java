package com.track.dao;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class SalesDetailDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.SALESDETDAO_PRINTPLANTMASTERQUERY;
	public MLogger getmLogger() {
		return mLogger;
	}
	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
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
	private boolean printLog = MLoggerConstant.SALESDETDAO_PRINTPLANTMASTERLOG;
	public static final String TABLE_NAME = "SALES_DETAIL";
	public boolean insertIntoSalesDet(Hashtable ht) throws Exception {
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
					+ TABLE_NAME + "]" + "("
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
	//Retrieve Sales Details
	public ArrayList getSalesDet(String plant) throws Exception {

		MLogger.log(1, this.getClass() + " getReasonCode()");
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;

		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer("  SELECT  ");
			sql.append("tranid,item,");
			sql.append("itemdesc,");
			sql.append("qty,unitprice,purchasedate,purchasetime");
			sql.append(" ");
			sql.append(" FROM " + "[" + plant + "_"
					+ "sales_detail] ");
			sql.append(" ORDER BY tranid,item");
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
	
	public ArrayList getSalesDetails(String plant,String startdt,String enddt,String item) throws Exception {

		MLogger.log(1, this.getClass() + " getReasonCode()");
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		String scondtn ="";
		try {
			con = DbBean.getConnection();
			if(startdt.length()>0)
			scondtn="and purchasedate>='"+startdt+"'";
			if(enddt.length()>0)
				scondtn=scondtn+"and purchasedate<='"+enddt+"'";
			if(item.length()>0)
				scondtn=scondtn+"and item='"+item+"'";
			StringBuffer sql = new StringBuffer("  SELECT  ");
			sql.append("tranid,item,");
			sql.append("itemdesc,");
			sql.append("qty,unitprice,purchasedate,purchasetime");
			sql.append(" ");
			sql.append(" FROM " + "[" + plant + "_"
					+ "sales_detail] where plant='"+plant+"'"+scondtn);
			
			
			sql.append(" ORDER BY tranid,item");
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
	//Distinct sale transactions
	public ArrayList getDistinctSales(String plant,String startdt,String enddt,String item,String extracondtn) throws Exception {

		MLogger.log(1, this.getClass() + " getReasonCode()");
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		String scondtn ="";
		try {
			
			con = DbBean.getConnection();
			if(startdt.length()>0)
				scondtn="and purchasedate>='"+startdt+"'";
				if(enddt.length()>0)
					scondtn=scondtn+"and purchasedate<='"+enddt+"'";
				if(item.length()>0)
					scondtn=scondtn+"and item='"+item+"'";
			StringBuffer sql = new StringBuffer("  SELECT  ");
			sql.append("tranid,sum(unitprice) as price");
		    sql.append(" FROM " + "[" + plant + "_"
					+ "sales_detail] where plant='"+plant+"'"+scondtn+extracondtn);
		
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
	
	public ArrayList getDistinctSales(String plant,String startdt,String enddt,String item,String loc,String extracondtn) throws Exception {

		MLogger.log(1, this.getClass() + " getReasonCode()");
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		String scondtn ="";
		try {
			
			con = DbBean.getConnection();
				if(startdt.length()>0)
					scondtn="and purchasedate>='"+startdt+"'";
				if(enddt.length()>0)
					scondtn=scondtn+"and purchasedate<='"+enddt+"'";
				if(item.length()>0)
					scondtn=scondtn+"and item='"+item+"'";
				if(loc.length()>0)
					scondtn=scondtn+"and loc='"+loc+"'";
			StringBuffer sql = new StringBuffer("  SELECT  ");
			sql.append("tranid,sum(unitprice) as price");
		    sql.append(" FROM " + "[" + plant + "_"
					+ "sales_detail] where plant='"+plant+"'"+scondtn+extracondtn);
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
	public ArrayList getDistinctSales(String plant,String startdt,String enddt,String item,String loc,String recptno,String extracondtn) throws Exception {

		MLogger.log(1, this.getClass() + " getDistinctSales()");
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		String scondtn ="";
		try {
			
			con = DbBean.getConnection();
				if(startdt.length()>0)
					{startdt    = startdt.substring(6)+"-"+ startdt.substring(3,5)+"-"+startdt.substring(0,2);
					scondtn="and purchasedate>='"+startdt+"'";}
				if(enddt.length()>0)
					{startdt    = startdt.substring(6)+"-"+ startdt.substring(3,5)+"-"+startdt.substring(0,2);
					scondtn=scondtn+"and purchasedate<='"+enddt+"'";}
				if(item.length()>0)
					scondtn=scondtn+"and item='"+item+"'";
				if(loc.length()>0)
					scondtn=scondtn+"and loc='"+loc+"'";
				if(recptno.length()>0)
					scondtn=scondtn+"and Tranid='"+recptno+"'";
				StringBuffer sql = new StringBuffer("  SELECT  ");
				sql.append("tranid,sum(unitprice) as price");
		
				sql.append(" FROM " + "[" + plant + "_"
					+ "sales_detail] where plant='"+plant+"'"+scondtn+extracondtn);
			
			
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
	 //Get Sales by tran type
	 public ArrayList getDistinctSales(String plant,String startdt,String enddt,String loc,String recptno,String userid,String trantype,String paymentmode,String extracondtn) throws Exception {

	         MLogger.log(1, this.getClass() + " getDistinctSales()");
	         ArrayList al = new ArrayList();
	         java.sql.Connection con = null;
	         String scondtn ="";
	         try {
	                 
	                 con = DbBean.getConnection();
	                         if(startdt.length()>0)
	                                 {startdt    = startdt.substring(6)+"-"+ startdt.substring(3,5)+"-"+startdt.substring(0,2);
	                                 scondtn="and purchasedate>='"+startdt+"'";}
	                         if(enddt.length()>0)
	                                 {enddt    = enddt.substring(6)+"-"+ enddt.substring(3,5)+"-"+enddt.substring(0,2);
	                                 scondtn=scondtn+"and purchasedate<='"+enddt+"'";}
	                      
	                         if(loc.length()>0)
	                                 scondtn=scondtn+"and loc='"+loc+"'";
	                         if(recptno.length()>0)
	                                 scondtn=scondtn+"and Tranid='"+recptno+"'";
	                         if(trantype.length()>0)
	                                 scondtn=scondtn+"and Trantype='"+trantype+"'";
	                         if(paymentmode.length()>0)
                                 scondtn=scondtn+"and b.PAYMENTMODE='"+paymentmode+"'";
	                         if(userid.length()>0)
                                 scondtn=scondtn+" and CRBY ='"+userid+"'";
	                         StringBuffer sql = new StringBuffer("  SELECT  ");
	                         //sql.append("tranid,sum(unitprice) as price,");
	                         //sql.append("tranid,sum(unitprice) as price,isnull((select POSTRANID from ["+plant+"_poshdr] where RECEIPTNO=tranid),'') as postranid");
	                         sql.append("tranid,b.PAYMENTMODE as paymentMode,(case when TranType='POS_REFUND'  Then -amount else amount end ) as amount,isnull(CRBY,'') CRBY,isnull((select top 1 POSTRANID from ["+plant+"_poshdr] where RECEIPTNO=tranid),'') as postranid,purchasedate, CASE WHEN ISNULL(REMARKS,'')<>'' AND ISNULL(RSNCODE,'')<>'' THEN REMARKS+','+RSNCODE WHEN ISNULL(REMARKS,'')<>'' AND ISNULL(RSNCODE,'')='' THEN  REMARKS WHEN ISNULL(REMARKS,'')='' AND ISNULL(RSNCODE,'')<>'' THEN RSNCODE  ELSE '' END as remarks");
	                         sql.append(" FROM " + "[" + plant + "_"
	                                 + "sales_detail] a ,["+ plant + "_POS_PAYMENT_DETAILS] b where a.TRANID = b.RECEIPTNO and a.PLANT = b.PLANT and b.plant='"+plant+"'"+scondtn+extracondtn);
	                 
	                 
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
	 
	 public ArrayList getDistinctSalesForMgmt(String plant,String startdt,String enddt,String item,String loc,String recptno,String userid,String trantype,String prdclsid,String prdtypeid,String prdbrandid,String extracondtn) throws Exception {

         MLogger.log(1, this.getClass() + " getDistinctSales()");
         ArrayList al = new ArrayList();
         java.sql.Connection con = null;
         String scondtn ="";
         try {
                 
                 con = DbBean.getConnection();
                         if(startdt.length()>0)
                                 {startdt    = startdt.substring(6)+"-"+ startdt.substring(3,5)+"-"+startdt.substring(0,2);
                                 scondtn="and purchasedate>='"+startdt+"'";}
                         if(enddt.length()>0)
                                 {enddt    = enddt.substring(6)+"-"+ enddt.substring(3,5)+"-"+enddt.substring(0,2);
                                 scondtn=scondtn+"and purchasedate<='"+enddt+"'";}
                         if(item.length()>0)
                                 scondtn=scondtn+"and item='"+item+"'";
                         if(loc.length()>0)
                                 scondtn=scondtn+"and loc='"+loc+"'";
                         if(recptno.length()>0)
                                 scondtn=scondtn+"and Tranid='"+recptno+"'";
                         if(trantype.length()>0)
                                 scondtn=scondtn+"and Trantype='"+trantype+"'";
                         if(userid.length()>0)
                             scondtn=scondtn+" and CRBY ='"+userid+"'";
 					if(prdclsid != null && prdclsid!="") 
                         {
                        	 scondtn = scondtn + " AND ITEM IN(SELECT ITEM FROM  ["+ plant+ "_"+ "ITEMMST"+ "] WHERE PRD_CLS_ID='"+prdclsid+"')" ;
                         }
                         if(prdtypeid != null && prdtypeid!="") 
                         {
                        	 scondtn = scondtn + " AND ITEM IN(SELECT ITEM FROM  ["+ plant+ "_"+ "ITEMMST"+ "] WHERE ITEMTYPE='"+prdtypeid+"')" ;
                         }
                         if(prdbrandid != null && prdbrandid!="") 
                         {
                        	 scondtn = scondtn + " AND ITEM IN(SELECT ITEM FROM  ["+ plant+ "_"+ "ITEMMST"+ "] WHERE PRD_BRAND_ID='"+prdbrandid+"')" ;
                         }
                         StringBuffer sql = new StringBuffer("  SELECT  ");
                         //sql.append("tranid,sum(unitprice) as price,");
                         //sql.append("tranid,sum(unitprice) as price,isnull((select POSTRANID from ["+plant+"_poshdr] where RECEIPTNO=tranid),'') as postranid");
                         sql.append("tranid, item,itemdesc,batch,qty,isnull(CRBY,'') CRBY,(case when TranType='POS_REFUND'  Then sum(-unitprice) else sum(unitprice) end ) as price,isnull((select top 1  POSTRANID from ["+plant+"_poshdr] where RECEIPTNO=tranid),'') as postranid,purchasedate, CASE WHEN ISNULL(REMARKS,'')<>'' AND ISNULL(RSNCODE,'')<>'' THEN REMARKS+','+RSNCODE WHEN ISNULL(REMARKS,'')<>'' AND ISNULL(RSNCODE,'')='' THEN  REMARKS WHEN ISNULL(REMARKS,'')='' AND ISNULL(RSNCODE,'')<>'' THEN RSNCODE  ELSE '' END as remarks");
                         sql.append(" FROM " + "[" + plant + "_"
                                 + "sales_detail] where plant='"+plant+"'"+scondtn+extracondtn);
                 
                 
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
	//Retrieve sales by tranid
	public ArrayList getSalesByTranid(String plant,String tranid,String condtn) throws Exception {

		MLogger.log(1, this.getClass() + " getReasonCode()");
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		String scondtn ="";
		try {
			
			con = DbBean.getConnection();
			
			StringBuffer sql = new StringBuffer("  SELECT  ");
			sql.append("tranid,item,");
			sql.append("itemdesc,");
			sql.append("qty,unitprice,purchasedate,purchasetime,isnull(paymentmode,'') as paymentmode,trantype ");
			sql.append(" ");
			sql.append(" FROM " + "[" + plant + "_"
					+ "sales_detail] where plant='"+plant+"'"+"and tranid='"+tranid+"'"+scondtn);
			
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
	//Retrieve sales by tranid
	public ArrayList getSalesforPDFTranid(String plant,String tranid,String condtn) throws Exception {

		MLogger.log(1, this.getClass() + " getReasonCode()");
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		String scondtn ="";
		try {
			
			con = DbBean.getConnection();
			
			StringBuffer sql = new StringBuffer("  SELECT  ");
			sql.append("ITEM,ITEMDESC,QTY,UNITPRICE, QTY * UNITPRICE AS PRICE");
			
			sql.append(" ");
			sql.append(" FROM " + "[" + plant + "_"
					+ "sales_detail] where plant='"+plant+"'"+"and tranid='"+tranid+"'"+scondtn);
			
			//			sql.append(" ORDER BY tranid,item");
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
	//added by Bruhan to get batch in pos refund from saled details on 16 dec 2013
	public ArrayList getBatchfromsalesdetailsposrefund (String plant, String item,
			String loc, String batch) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
//			if(batch.equalsIgnoreCase("NOBATCH"))
//				batch = "";
			String sQry = "select distinct batch,sum(qty) as qty from "
					+ "["
					+ plant
					+ "_"
					+ "SALES_DETAIL] where plant='"
					+ plant
					+ "' and item='"
					+ item
					+ "' and loc ='"
					+ loc
					
					+ "' and  batch like '"
					+ batch+"%'"
					+ " and qty>0  AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP%'"
					+ " group by BATCH ORDER BY batch ";
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
	
	//end code by Bruhan to get batch in pos refund from saled details on 16 dec 2013
	
		public boolean isExisit(Hashtable ht, String extCond) throws Exception {
        boolean flag = false;
        java.sql.Connection con = null;
        try {
                con = com.track.gates.DbBean.getConnection();
                StringBuffer sql = new StringBuffer(" SELECT ");
                sql.append("COUNT(*) ");
                sql.append(" ");
                sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME + "]");
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
	public boolean update(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + TABLE_NAME + "]");
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
	public boolean deleteSales(java.util.Hashtable ht) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME + "]");
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
}
