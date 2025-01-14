package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.dao.SupplierCreditDAO;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class SupplierCreditUtil {
	private boolean printLog = MLoggerConstant.SupplierCreditUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	private SupplierCreditDAO supplierCreditDAO = new SupplierCreditDAO();
	
	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public int addSupplierCreditHdr(Hashtable ht, String plant) {
		int HdrId = 0;
		try {
			HdrId = supplierCreditDAO.addSupplierCreditHdr(ht, plant);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return HdrId;		
	}
	
	public boolean addMultipleCreditDet(List<Hashtable<String, String>> htDetInfoList, String plant){
		boolean flag = false;		
		try {
			flag = supplierCreditDAO.addMultipleCreditDet(htDetInfoList, plant);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean addAttachments(List<Hashtable<String, String>> htAttachmentList, String plant){
		boolean flag = false;		
		try {
			flag = supplierCreditDAO.addAttachments(htAttachmentList, plant);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	public ArrayList getSupplierCreditSummaryView(Hashtable ht, String afrmDate,
			String atoDate, String plant, String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			supplierCreditDAO.setmLogger(mLogger);

	        		dtCondStr ="and ISNULL(A.CREDIT_DATE,'')<>'' AND CAST((SUBSTRING(A.CREDIT_DATE, 7, 4) + '-' + SUBSTRING(a.CREDIT_DATE, 4, 2) + '-' + SUBSTRING(a.CREDIT_DATE, 1, 2)) AS date)";
	        		extraCon= "order by A.ID desc, CAST((SUBSTRING(A.CREDIT_DATE, 7, 4) + SUBSTRING(A.CREDIT_DATE, 4, 2) + SUBSTRING(A.CREDIT_DATE, 1, 2)) AS date) desc";    			        
			       
					   if (afrmDate.length() > 0) {
		              	sCondition = sCondition + dtCondStr + "  >= '" 
		  						+ afrmDate
		  						+ "'  ";
		  				if (atoDate.length() > 0) {
		  					sCondition = sCondition +dtCondStr+ " <= '" 
		  					+ atoDate
		  					+ "'  ";
		  				}
		  			  } else {
		  				if (atoDate.length() > 0) {
		  					sCondition = sCondition +dtCondStr+ " <= '" 
		  					+ atoDate
		  					+ "'  ";
		  				}
		  		     	}   
		           
			           if (custname.length()>0){
                 	custname = new StrUtils().InsertQuotes(custname);
                 	sCondition = sCondition + " AND VNAME LIKE '%"+custname+"%' " ;
                  } 
			          
			           sql = new StringBuffer("select A.ID as ID,CREDIT_DATE,CREDITNOTE,ISNULL(A.PONO,'') PONO,A.VENDNO,");
			           //sql.append(" ISNULL(VNAME,'') as VNAME,ISNULL((select CURRENCYID from " + plant +"_POHDR p where p.PONO=A.PONO ),'') as CURRENCYID,NOTE,TERMSCONDITIONS,");
			           sql.append(" ISNULL(VNAME,'') as VNAME,ISNULL(CURRENCYID,'') as CURRENCYID,NOTE,TERMSCONDITIONS,");
			           sql.append(" ISNULL((SELECT TOP 1 B.CURRENCYUSEQT FROM " + plant +"_FINVENDORCREDITNOTEDET B WHERE B.HDRID=A.ID),1) CURRENCYUSEQT,");
			           sql.append(" CREDIT_STATUS,cast(TOTAL_AMOUNT as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,cast(((ISNULL((select BALANCE from " + plant +"_FINPAYMENTDET p JOIN " + plant +"_FINPAYMENTHDR V ON p.PAYHDRID=V.ID AND P.TYPE='ADVANCE' where V.REFERENCE=A.CREDITNOTE ),0))) as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as BALANCE_DUE,");				           
			           sql.append(" ISNULL(A.PAYMENT_TERMS,'') PAYMENT_TERMS,ITEM_RATE,SUB_TOTAL,ISNULL(SHIPPINGCOST,0) SHIPPINGCOST,ADJUSTMENT,ISNULL(DISCOUNT,0) DISCOUNT,ISNULL(DISCOUNT_TYPE,'') as DISCOUNT_TYPE,ISNULL(DISCOUNT_ACCOUNT,'') DISCOUNT_ACCOUNT");
			           sql.append(" from " + plant +"_FINVENDORCREDITNOTEHDR A JOIN " + plant +"_VENDMST V ON V.VENDNO=A.VENDNO WHERE A.PLANT='"+ plant+"'" + sCondition);			           
                     
			            
   				if (ht.get("PONO") != null) {
  					  sql.append(" AND A.PONO = '" + ht.get("PONO") + "'");
  				    }
   				if (ht.get("CREDITNOTE") != null) {
   					sql.append(" AND A.CREDITNOTE = '" + ht.get("CREDITNOTE") + "'");
   				}
   				
   				if (ht.get("REFERENCE") != null) {
   					sql.append(" AND A.PONO = '" + ht.get("REFERENCE") + "'");
   				}
   				
   				if (ht.get("STATUS") != null) {
   					sql.append(" AND A.CREDIT_STATUS = '" + ht.get("STATUS") + "'");
   				}
  				   
  				   
  				  arrList = supplierCreditDAO.selectForReport(sql.toString(), htData, extraCon);
       
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getsupplierCreditSummary:", e);
		}
		return arrList;
	}
	
	public List getHdrById(Hashtable ht) throws Exception {
		List HdrList = new ArrayList();
		try {
			supplierCreditDAO.setmLogger(mLogger);
			HdrList = supplierCreditDAO.getHdrById(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return HdrList;
 }

	public List getDetByHdrId(Hashtable ht) throws Exception {
		List CNDetList = new ArrayList();
		try {
			supplierCreditDAO.setmLogger(mLogger);
			CNDetList = supplierCreditDAO.getDetByHdrId(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return CNDetList;
	}
	
	public ArrayList getVendorCreditDetails(Hashtable ht, String afrmDate,
			String atoDate, String plant, String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			supplierCreditDAO.setmLogger(mLogger);

	        		dtCondStr ="and ISNULL(A.CREDIT_DATE,'')<>'' AND CAST((SUBSTRING(A.CREDIT_DATE, 7, 4) + '-' + SUBSTRING(a.CREDIT_DATE, 4, 2) + '-' + SUBSTRING(a.CREDIT_DATE, 1, 2)) AS date)";
	        		extraCon= "order by A.ID desc, CAST((SUBSTRING(A.CREDIT_DATE, 7, 4) + SUBSTRING(A.CREDIT_DATE, 4, 2) + SUBSTRING(A.CREDIT_DATE, 1, 2)) AS date) desc";    			        
			       
					   if (afrmDate.length() > 0) {
		              	sCondition = sCondition + dtCondStr + "  >= '" 
		  						+ afrmDate
		  						+ "'  ";
		  				if (atoDate.length() > 0) {
		  					sCondition = sCondition +dtCondStr+ " <= '" 
		  					+ atoDate
		  					+ "'  ";
		  				}
		  			  } else {
		  				if (atoDate.length() > 0) {
		  					sCondition = sCondition +dtCondStr+ " <= '" 
		  					+ atoDate
		  					+ "'  ";
		  				}
		  		     	}   
		           
			           if (custname.length()>0){
                 	custname = new StrUtils().InsertQuotes(custname);
                 	sCondition = sCondition + " AND VNAME LIKE '%"+custname+"%' " ;
                  } 
			           sql = new StringBuffer("SELECT ID,CREDIT_DATE,CREDITNOTE,PONO,VENDNO,VNAME,CURRENCYID,NOTE,TERMSCONDITIONS,CREDIT_STATUS,TOTAL_AMOUNT,");
			           sql.append(" BALANCE_DUE,PAYMENT_TERMS,ITEM_RATE,SUB_TOTAL,SHIPPINGCOST,ADJUSTMENT,DISCOUNT,DISCOUNT_TYPE,DISCOUNT_ACCOUNT,");
			           sql.append(" cast(TOTAL_AMOUNT * ISNULL((SELECT CURRENCYUSEQT FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID),1) as DECIMAL(20,5)) as BASE_TOTAL_AMOUNT,");
			           sql.append(" cast(((ISNULL((select BALANCE from ["+plant+"_FINPAYMENTDET] p JOIN ["+plant+"_FINPAYMENTHDR] V ON p.PAYHDRID=V.ID ");
			           sql.append(" AND P.TYPE='ADVANCE' where V.REFERENCE=A.CREDITNOTE ),0))* ISNULL((SELECT CURRENCYUSEQT FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID),1)) as DECIMAL(20,5)) as BASE_BALANCE_DUE FROM (");
			           sql.append(" select A.ID as ID,CREDIT_DATE,CREDITNOTE,ISNULL(A.PONO,'') PONO,A.VENDNO, ISNULL(VNAME,'') as VNAME,");
			           sql.append(" ISNULL((select CURRENCYID from ["+plant+"_POHDR] p where p.PONO=A.PONO ),'') as CURRENCYID,NOTE,TERMSCONDITIONS, CREDIT_STATUS,");
			           sql.append(" cast(TOTAL_AMOUNT as DECIMAL(20,5)) as TOTAL_AMOUNT,");
			           sql.append(" cast(((ISNULL((select BALANCE from ["+plant+"_FINPAYMENTDET] p JOIN ["+plant+"_FINPAYMENTHDR] V ON p.PAYHDRID=V.ID ");
			           sql.append(" AND P.TYPE='ADVANCE' where V.REFERENCE=A.CREDITNOTE ),0))) as DECIMAL(20,5)) as BALANCE_DUE,");
			           sql.append(" ISNULL(A.PAYMENT_TERMS,'') PAYMENT_TERMS,ITEM_RATE,SUB_TOTAL,ISNULL(SHIPPINGCOST,0) SHIPPINGCOST,ADJUSTMENT,ISNULL(DISCOUNT,0) DISCOUNT,");
			           sql.append(" ISNULL(DISCOUNT_TYPE,'') as DISCOUNT_TYPE,ISNULL(DISCOUNT_ACCOUNT,'') DISCOUNT_ACCOUNT from ");
			           sql.append(" ["+plant+"_FINVENDORCREDITNOTEHDR] A JOIN ["+plant+"_VENDMST] V ON V.VENDNO=A.VENDNO WHERE A.PLANT='"+ plant +"'" + sCondition);
       
   				if (ht.get("PONO") != null) {
   					sql.append(" AND A.PONO = '" + ht.get("PONO") + "'");
  				}
   				if (ht.get("CREDITNOTE") != null) {
   					sql.append(" AND A.CREDITNOTE = '" + ht.get("CREDITNOTE") + "'");
   				}
   				
   				if (ht.get("REFERENCE") != null) {
   					sql.append(" AND A.PONO = '" + ht.get("REFERENCE") + "'");
   				}
   				
   				if (ht.get("STATUS") != null) {
   					sql.append(" AND A.CREDIT_STATUS = '" + ht.get("STATUS") + "'");
   				}
   				sql.append(" ) A ");
  				   
  				arrList = supplierCreditDAO.selectForReport(sql.toString(), htData, extraCon);

		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getsupplierCreditSummary:", e);
		}
		return arrList;
	}


}
