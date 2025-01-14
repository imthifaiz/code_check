package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.track.dao.InvMstDAO;
import com.track.dao.LocMstDAO;
import com.track.util.StrUtils;
import com.track.constants.MLoggerConstant;
import com.track.dao.AgeingDAO;
import com.track.dao.BillDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.OrderPaymentDAO;
import com.track.dao.PlantMstDAO;
import com.track.util.MLogger;

public class AgeingUtil {
	private boolean printLog = MLoggerConstant.AgeingUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	private BillDAO billDao = new BillDAO();
	
	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public boolean  InsertTempOrderPaymentForAgeingReport(String plant,String afrmDate,String atoDate,
			String order,String custname,String ordno, String name)
			throws Exception {
	OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
	boolean insertflag  = false;
	boolean isExistsPaymentDetails = false;
	boolean isExiststemp = false;
	String sql = "";
	CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	ArrayList arrCust = new ArrayList();
		
		try {
			Hashtable htCond = new Hashtable();
			String searchCond ="",dtCondStr="",extraCon="",sCustCode="", billdtCondStr="", billsearchCond ="",
					invoicedtCondStr="", invoicesearchCond ="";				   
			
			dtCondStr = "  AND CAST((SUBSTRING(PAYMENT_DT,7,4) + '-' + SUBSTRING(PAYMENT_DT,4,2) + '-' + SUBSTRING(PAYMENT_DT,1,2)) AS date) ";
			billdtCondStr = " AND CAST((SUBSTRING(A.CRAT,1,4) + '-' + SUBSTRING(A.CRAT,5,2) + '-' + SUBSTRING(A.CRAT,7,2)) AS date) "; 
			
			if(custname.length()>0){
            	if(order.equalsIgnoreCase("INVOICE")){
            		//sCustCode = customerBeanDAO.getCustomerCode(plant, custname);
            		invoicesearchCond = invoicesearchCond + " AND (   CNAME = '"+custname+"') ";
            	}
            	else if(order.equalsIgnoreCase("BILL")){
            		sCustCode = customerBeanDAO.getVendorCode(plant, custname);
            		billsearchCond = billsearchCond + " AND (   VENDNO = '"+custname+"') ";	            		
            	}            	
            	searchCond =searchCond + " AND (   CustCode = '"+sCustCode+"') ";            	
            }
			
            if(ordno.length()>0){
            	if(order.equalsIgnoreCase("INVOICE")){
            		invoicesearchCond =invoicesearchCond + " AND ( DONO = '"+ordno+"') ";
            	}else {
            		searchCond =searchCond + " AND ( PONO = '"+ordno+"') ";
            	}
			}
            
            if(name.length()>0){
            	if(order.equalsIgnoreCase("INVOICE")){
            		invoicesearchCond =invoicesearchCond + " AND ( INVOICE = '"+name+"') ";
            	}else {
            		searchCond =searchCond + " AND ( BILL = '"+name+"') ";
            	}					
			}
	            
            if (afrmDate.length() > 0) {
            	searchCond = searchCond /*+ dtCondStr + "  >= '" 
						+ afrmDate
						+ "'  "*/;
            	
            	billsearchCond = billsearchCond /*+ billdtCondStr + "  >= '" 
						+ afrmDate
						+ "'  "*/;
            	
            	invoicesearchCond = invoicesearchCond /*+ dtCondStr + "  >= '" 
						+ afrmDate
						+ "'  "*/;
				 
				if (atoDate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";					
					billsearchCond = billsearchCond +billdtCondStr+ " <= '" 
							+ atoDate
							+ "'  ";					
					invoicesearchCond = invoicesearchCond +dtCondStr+ " <= '" 
							+ atoDate
							+ "'  ";					
				}
			} else {
				if (atoDate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";					
					billsearchCond = billsearchCond +billdtCondStr+ " <= '" 
							+ atoDate
							+ "'  ";					
					invoicesearchCond = invoicesearchCond +dtCondStr+ " <= '" 
							+ atoDate
							+ "'  ";					
				}
			} 
            Hashtable htValues = new Hashtable();
        	htValues.put("A.PLANT", plant);    
        	if(order.equalsIgnoreCase("INVOICE")){
        		isExistsPaymentDetails  = new InvoiceDAO().isExisit(htValues,billsearchCond);
        	}else {
        		isExistsPaymentDetails  = new BillDAO().isExisit(htValues,billsearchCond);
        	}	        
	        if(isExistsPaymentDetails)
	        {
	        	htValues.remove("A.PLANT");
	        	htValues.put("PLANT", plant);    
	        	isExiststemp = new AgeingDAO().isExisittemp(htValues, "");
	        	if(isExiststemp){
	        		boolean deleteflag = new AgeingDAO().deletetemporderpayment(htValues);
	        	}
	        	htValues.put("ORDERNAME", order);
	        	if(order.equalsIgnoreCase("INVOICE")){
	        		insertflag = new AgeingDAO().insertinvoicetemporderpayment(htValues, invoicesearchCond);     		
            	}else {
            		insertflag = new AgeingDAO().inserttemporderpayment(htValues, searchCond);
            	}
	        }
	        else
	        {
	        	insertflag = true;
	        }
		} catch (Exception e) {
			this.mLogger.Exception(e.getMessage());
			throw e;
		}
		return insertflag;
	}
	
	public boolean  InsertTempOrderPayment(String plant,String afrmDate,String atoDate, String order,
			String custname,String ordno, String name)
			throws Exception {
	 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
	 boolean insertflag  = false;
	 boolean isExistsPaymentDetails = false;
	 boolean isExiststemp = false;
		String sql = "";
		CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
		ArrayList arrCust = new ArrayList();
		
		try {
			Hashtable htCond = new Hashtable();
			String searchCond ="",dtCondStr="",extraCon="",sCustCode="", billdtCondStr="", billsearchCond ="",
					invoicedtCondStr="", invoicesearchCond ="";
			   
	            dtCondStr = "  AND CAST((SUBSTRING(PAYMENT_DT,7,4) + '-' + SUBSTRING(PAYMENT_DT,4,2) + '-' + SUBSTRING(PAYMENT_DT,1,2)) AS date) ";
	            billdtCondStr = " AND CAST((SUBSTRING(A.CRAT,1,4) + '-' + SUBSTRING(A.CRAT,5,2) + '-' + SUBSTRING(A.CRAT,7,2)) AS date) ";
	            
	            if(custname.length()>0){
	            	if(order.equalsIgnoreCase("INVOICE")){
	            		sCustCode = customerBeanDAO.getCustomerCode(plant, custname);
	            		invoicesearchCond = invoicesearchCond + " AND (   CNAME = '"+custname+"') ";
	            	}
	            	else if(order.equalsIgnoreCase("BILL")){
	            		sCustCode = customerBeanDAO.getVendorCode(plant, custname);
	            		billsearchCond = billsearchCond + " AND (   VENDNO = '"+sCustCode+"') ";
	            	}
	            	searchCond =searchCond + " AND ( a.VENDORNAME = '"+custname+"') ";
	            }
	            
	            if(ordno.length()>0){
	            	if(order.equalsIgnoreCase("INVOICE")){
	            		invoicesearchCond =invoicesearchCond + " AND ( DONO = '"+ordno+"') ";
	            	}else {
	            		searchCond =searchCond + " AND ( PONO = '"+ordno+"') ";
	            	}
					
				}
	            
	            if(name.length()>0){
	            	if(order.equalsIgnoreCase("INVOICE")){
	            		invoicesearchCond =invoicesearchCond + " AND ( INVOICE = '"+name+"') ";
	            	}else {
	            		searchCond =searchCond + " AND ( BILL = '"+name+"') ";
	            	}					
				}
	            
	            if (afrmDate.length() > 0) {
	            	searchCond = searchCond + dtCondStr + "  >= '" 
							+ afrmDate
							+ "'  ";
	            	billsearchCond = billsearchCond + billdtCondStr + "  >= '" 
							+ afrmDate
							+ "'  ";
	            	invoicesearchCond = invoicesearchCond + dtCondStr + "  >= '" 
							+ afrmDate
							+ "'  ";
					 
					if (atoDate.length() > 0) {
						searchCond = searchCond +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						billsearchCond = billsearchCond +billdtCondStr+ " <= '" 
								+ atoDate
								+ "'  ";					
						invoicesearchCond = invoicesearchCond +dtCondStr+ " <= '" 
								+ atoDate
								+ "'  ";						
					}
				} else {
					if (atoDate.length() > 0) {
						searchCond = searchCond +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						billsearchCond = billsearchCond +billdtCondStr+ " <= '" 
								+ atoDate
								+ "'  ";					
						invoicesearchCond = invoicesearchCond +dtCondStr+ " <= '" 
								+ atoDate
								+ "'  ";
					}
				} 
	            Hashtable htValues = new Hashtable();
	            htValues.put("A.PLANT", plant);    
	        	if(order.equalsIgnoreCase("INVOICE")){
	        		isExistsPaymentDetails  = new InvoiceDAO().isExisit(htValues,billsearchCond);
	        	}else {
	        		isExistsPaymentDetails  = new BillDAO().isExisit(htValues,billsearchCond);
	        	}
	        if(isExistsPaymentDetails)
	        {
	        	htValues.remove("A.PLANT");
	        	htValues.put("PLANT", plant);    
	        	isExiststemp = new AgeingDAO().isExisittemp(htValues, "");
	        	if(isExiststemp){
	        		boolean deleteflag = new AgeingDAO().deletetemporderpayment(htValues);
	        	}
	        	htValues.put("ORDERNAME", order);
	        	if(order.equalsIgnoreCase("INVOICE")){
	        		insertflag = new AgeingDAO().insertinvoicetemporderpayment(htValues, invoicesearchCond);     		
            	}else {
            		insertflag = new AgeingDAO().inserttemporderpayment(htValues, searchCond);
            	}	        	
	        }
	        else
	        {
	        	insertflag = true;
	        }
	                        
			
		} catch (Exception e) {
			this.mLogger.Exception(e.getMessage());
			throw e;
		}

		return insertflag;
	}
	
	public ArrayList getcustomerorsuppliername(String plant,String afrmDate,String atoDate, String order,String custname)
			throws Exception {
	 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		String sql = "";
		ArrayList al = null;
		try {
			Hashtable htCond = new Hashtable();
			String searchCond ="",dtCondStr="",extraCon="",dtCondpmt="",searchCondord="",searchCondpmt="";
			if(order.equalsIgnoreCase("INVOICE"))
	        {
				dtCondStr = "  AND CAST((SUBSTRING(INVOICE_DATE,7,4) + '-' + SUBSTRING(INVOICE_DATE,4,2) + '-' + SUBSTRING(INVOICE_DATE,1,2)) AS date) ";
	        }else {
	        	dtCondStr = "  AND CAST((SUBSTRING(BILL_DATE,7,4) + '-' + SUBSTRING(BILL_DATE,4,2) + '-' + SUBSTRING(BILL_DATE,1,2)) AS date) ";
	        }
	            
	           
	            if(custname.length()>0){
	            	if(order.equalsIgnoreCase("INVOICE"))
	    	        {
	            		searchCond =searchCond + " AND (   B.CNAME ='"+custname+"' or B.CUSTNO = '"+custname+"') ";
	    	        }else {
	    	        	searchCond =searchCond + " AND (   B.VNAME ='"+custname+"' OR B.VENDNO = '"+custname+"') ";
	    	        }
	            }
	            
	            if (afrmDate.length() > 0) {
					 searchCondord = searchCond + dtCondStr + "  >= '" 
							+ afrmDate
							+ "'  ";
					 
					if (atoDate.length() > 0) {
						searchCondord = searchCondord +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						
					}
				} else {
					if (atoDate.length() > 0) {
						searchCondord = searchCond +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						
					}else {
						searchCondord = searchCond;
					}
				}    
	                        
				
				 
	        if(order.equalsIgnoreCase("INVOICE"))
	        {	
	        	sql = ""+"SELECT distinct CNAME AS custname,B.CUSTNO AS custcode,ISNULL(PAY_IN_DAYS,'')pmtdays from "
						+"["+plant+"_FININVOICEHDR]A,["+plant+"_CUSTMST]B WHERE A.PLANT='"+plant+"' AND A.CUSTNO=B.CUSTNO"+searchCondord;
						
		        }else{
		         sql = ""+"SELECT distinct VNAME as custname,B.VENDNO as custcode,ISNULL(PAY_IN_DAYS,'')pmtdays from "
							+"["+plant+"_FINBILLHDR]A,["+plant+"_VENDMST]B WHERE A.PLANT='"+plant+"' AND A.VENDNO=B.VENDNO"+searchCondord;
		        }
			orderPayDao.setmLogger(mLogger);
			al = orderPayDao.selectForReport(sql, htCond, "");
		} catch (Exception e) {
			this.mLogger.Exception(e.getMessage());
			throw e;
		}

		return al;
	}
	
	
	public ArrayList getcustomerorsuppliernamegroupby(String plant,String afrmDate,String atoDate, String order,String custname)
			throws Exception {
	 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		String sql = "";
		ArrayList al = null;
		try {
			Hashtable htCond = new Hashtable();
			String searchCond ="",dtCondStr="",extraCon="",dtCondpmt="",searchCondord="",searchCondpmt="";
			if(order.equalsIgnoreCase("INVOICE"))
	        {
				dtCondStr = "  AND CAST((SUBSTRING(INVOICE_DATE,7,4) + '-' + SUBSTRING(INVOICE_DATE,4,2) + '-' + SUBSTRING(INVOICE_DATE,1,2)) AS date) ";
	        }else {
	        	dtCondStr = "  AND CAST((SUBSTRING(BILL_DATE,7,4) + '-' + SUBSTRING(BILL_DATE,4,2) + '-' + SUBSTRING(BILL_DATE,1,2)) AS date) ";
	        }
	            
	           
	            if(custname.length()>0){
	            	if(order.equalsIgnoreCase("INVOICE"))
	    	        {
	            		searchCond =searchCond + " AND (   B.CNAME ='"+custname+"' or B.CUSTNO = '"+custname+"') ";
	    	        }else {
	    	        	searchCond =searchCond + " AND (   B.VNAME ='"+custname+"' OR B.VENDNO = '"+custname+"') ";
	    	        }
	            }
	            
	            if (afrmDate.length() > 0) {
					 searchCondord = searchCond + dtCondStr + "  >= '" 
							+ afrmDate
							+ "'  ";
					 
					if (atoDate.length() > 0) {
						searchCondord = searchCondord +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						
					}
				} else {
					if (atoDate.length() > 0) {
						searchCondord = searchCond +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						
					}else {
						searchCondord = searchCond;
					}
				}    
	                        
				
				 
	        if(order.equalsIgnoreCase("INVOICE"))
	        {	
	        	sql = ""+"SELECT CNAME AS custname,A.CURRENCYID AS currency,B.CUSTNO AS custcode,ISNULL(PAY_IN_DAYS,'')pmtdays from "
						+"["+plant+"_FININVOICEHDR]A,["+plant+"_CUSTMST]B WHERE A.PLANT='"+plant+"' AND A.BILL_STATUS!='Draft' AND A.CUSTNO=B.CUSTNO"+searchCondord+" GROUP BY A.CUSTNO,A.CURRENCYID,b.CNAME,B.CUSTNO,B.PAY_IN_DAYS";
						
		        }else{
		         sql = ""+"SELECT VNAME as custname,A.CURRENCYID AS currency,B.VENDNO as custcode,ISNULL(PAY_IN_DAYS,'')pmtdays from "
							+"["+plant+"_FINBILLHDR]A,["+plant+"_VENDMST]B WHERE A.PLANT='"+plant+"' AND A.BILL_STATUS!='Draft' AND A.VENDNO=B.VENDNO"+searchCondord+" GROUP BY A.VENDNO,A.CURRENCYID,b.VNAME,B.VENDNO,B.PAY_IN_DAYS";
		        }
			orderPayDao.setmLogger(mLogger);
			al = orderPayDao.selectForReport(sql, htCond, "");
		} catch (Exception e) {
			this.mLogger.Exception(e.getMessage());
			throw e;
		}

		return al;
	}
	
	public ArrayList getcurrencygroupby(String plant,String afrmDate,String atoDate, String order,String custname)
			throws Exception {
	 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		String sql = "";
		ArrayList al = null;
		try {
			Hashtable htCond = new Hashtable();
			String searchCond ="",dtCondStr="",extraCon="",dtCondpmt="",searchCondord="",searchCondpmt="";
			if(order.equalsIgnoreCase("INVOICE"))
	        {
				dtCondStr = "  AND CAST((SUBSTRING(INVOICE_DATE,7,4) + '-' + SUBSTRING(INVOICE_DATE,4,2) + '-' + SUBSTRING(INVOICE_DATE,1,2)) AS date) ";
	        }else {
	        	dtCondStr = "  AND CAST((SUBSTRING(BILL_DATE,7,4) + '-' + SUBSTRING(BILL_DATE,4,2) + '-' + SUBSTRING(BILL_DATE,1,2)) AS date) ";
	        }
	            
	           

	            
	            if (afrmDate.length() > 0) {
					 searchCondord = searchCond + dtCondStr + "  >= '" 
							+ afrmDate
							+ "'  ";
					 
					if (atoDate.length() > 0) {
						searchCondord = searchCondord +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						
					}
				} else {
					if (atoDate.length() > 0) {
						searchCondord = searchCond +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						
					}else {
						searchCondord = searchCond;
					}
				}    
	                        
				
				 
	        if(order.equalsIgnoreCase("INVOICE"))
	        {	
	        	sql = ""+"SELECT A.CURRENCYID AS currency from "
						+"["+plant+"_FININVOICEHDR]A WHERE A.PLANT='"+plant+"' "+searchCondord+" GROUP BY A.CURRENCYID";
						
		        }else{
		         sql = ""+"SELECT A.CURRENCYID AS currency from "
							+"["+plant+"_FINBILLHDR]A WHERE A.PLANT='"+plant+"' "+searchCondord+" GROUP BY A.CURRENCYID";
		        }
			orderPayDao.setmLogger(mLogger);
			al = orderPayDao.selectForReport(sql, htCond, "");
		} catch (Exception e) {
			this.mLogger.Exception(e.getMessage());
			throw e;
		}

		return al;
	}
	
	public ArrayList getAgeingDetailsByFrwdBal(String plant,String afrmDate,String atoDate, String order,String custName,String orderno,String currencyid)
			throws Exception {
	 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		String sql = "";
		ArrayList al = null;
		try {
			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			Hashtable htCond = new Hashtable();
			String searchCond ="",searchCond1="",dtCondStr="",extraCon="",dtCondpmt="",
					searchCondord="",searchCondpmt="",sCustCode="";
			              
	            if(custName.length()>0){
	            	sCustCode = customerBeanDAO.getCustomerCode(plant, custName);
	            	searchCond = " AND CUSTNO =N'"+sCustCode+"'";
	            }
	            if(custName.length()>0){
	            	
	            	//searchCond1 =searchCond1 + " AND (   B.CUSTNAME =N'"+custName+"' or B.CustCode = N'"+custName+"' ) and a.currencyid='"+currencyid+"'";
	            	searchCond1 =searchCond1 + " AND (   B.CUSTNAME =N'"+custName+"' or B.CustCode = N'"+custName+"' ) ";
	            }
	            
	            dtCondStr = "  AND CAST((SUBSTRING(CollectionDate,7,4) + '-' + SUBSTRING(CollectionDate,4,2) + '-' + SUBSTRING(CollectionDate,1,2)) AS date) ";
	            dtCondpmt = "  AND CAST((SUBSTRING(payment_dt, 7, 4) + '-' + SUBSTRING(payment_dt, 4, 2) + '-' + SUBSTRING(payment_dt, 1, 2)) AS date)";
	            
	           // extraCon=" order by CAST((SUBSTRING(CollectionDate, 7, 4) + SUBSTRING(CollectionDate, 4, 2) + SUBSTRING(CollectionDate, 1, 2)) AS date) ";
	            if (afrmDate.length() > 0) {
					 searchCondord = searchCond /*+ dtCondStr + "  >= '" 
							+ afrmDate
							+ "'  "*/;
					 /*searchCondpmt = searchCondpmt + dtCondpmt + "  >= '" 
								+ afrmDate
								+ "'  ";*/
					if (atoDate.length() > 0) {
						searchCondord = searchCondord +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						searchCondpmt = searchCondpmt +dtCondpmt+ " <= '" 
								+ atoDate
								+ "'  ";
					}
				} else {
					if (atoDate.length() > 0) {
						searchCondord = searchCond +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						searchCondpmt = searchCondpmt +dtCondpmt+ " <= '" 
								+ atoDate
								+ "'  ";
					}
				}    
	                        
				/*if(orderno.length()>0) 
				{
					if(order.equalsIgnoreCase("INVOICE")){
						searchCondord = searchCondord+" AND DONO='"+orderno+"' ";
						searchCondpmt = searchCondpmt+" AND ORDNO='"+orderno+"' ";
					}
					else if(order.equalsIgnoreCase("BILL")){
						searchCondord = searchCondord+" AND PONO='"+orderno+"' ";
						searchCondpmt = searchCondpmt+" AND ORDNO='"+orderno+"' ";
					}
				}*/
				 
	           String extraCond =" where orderAmt>0 ";
				 
	        if(order.equalsIgnoreCase("INVOICE"))
	        {	
	        	
	        	sql = "SELECT orderNo,ordDate,Tax,orderAmt,amtReceived,"+
	        			"((CAST(orderAmt AS DECIMAL(18, 2))) - "+
	        			"((CAST(amtReceived AS DECIMAL(18, 2))))) AS DueToPay FROM("+ 
	        			"SELECT DONO AS orderNo,I.INVOICE_DATE as ordDate, "+
	        			"ISNULL(I.TOTAL_AMOUNT,0) as orderAmt,"+
			        	"ISNULL((SELECT SUM(AMOUNT_PAID) FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] WHERE"+  
			        	" PLANT =I.PLANT and ORDERTYPE='INVOICE' and ISNULL(PAYMENT_MODE,'')<>'' "+
			        	" AND  ORDERNAME =I.INVOICE),0) as amtReceived ," +
			        	" CASE WHEN Outboud_Gst>0 then 1+(Outboud_Gst/100) else 0 end AS TAX"+ 
			        	" FROM ["+plant+"_FININVOICEHDR] I WHERE PLANT='"+plant+"' "+searchCond+") A";
		        }else{
		        	sql = ""+"SELECT orderNo,ordDate,Tax,orderAmt"
							+",amtReceived,((CAST(orderAmt AS DECIMAL(18, 2))) - ((CAST(amtReceived AS DECIMAL(18, 2))))) AS DueToPay FROM( "
							+"SELECT PONO AS orderNo,COLLECTIONDATE as ordDate, "
							+" ISNULL((SELECT SUM(ORD_AMT) FROM (SELECT CASE WHEN Inbound_Gst>0 THEN ROUND((1+(Inbound_Gst/100))*ROUND(QTYOR * UNITCOST*CURRENCYUSEQT,2),2) ELSE ROUND(QTYOR * UNITCOST*CURRENCYUSEQT,2) END [ORD_AMT] from ["+plant+"_PODET] where PLANT =["+plant+"_POHDR].PLANT and  Pono =["+plant+"_POHDR].PONO)T),0)   as orderAmt "
							+",ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] where  PLANT =["+plant+"_POHDR].PLANT and ORDERTYPE='BILL' and ISNULL(PAYMENT_MODE,'')<>'' and ORDNO =["+plant+"_POHDR].PONO "+searchCondpmt+" ),0) as amtReceived "
							+ ",CASE WHEN Inbound_Gst>0 then 1+(Inbound_Gst/100) else 0 end AS TAX FROM ["+plant+"_POHDR] WHERE PLANT='"+plant+"'" + searchCondord +") A"
							+" UNION"
							+" SELECT orderNo,ordDate,Tax,orderAmt,"
							+"amtReceived,((CAST(orderAmt AS DECIMAL(18, 2))) - ((CAST(amtReceived AS DECIMAL(18, 2))))) AS DueToPay  FROM( "
							+"SELECT ORDNO AS orderNo, COLLECTIONDATE as ordDate,"
							+"ISNULL((SELECT SUM(ORD_AMT) FROM (SELECT CASE WHEN Inbound_Gst>0 THEN ROUND((1+(Inbound_Gst/100))*ROUND(QTYOR * UNITCOST*CURRENCYUSEQT,2),2) ELSE ROUND(QTYOR * UNITCOST*CURRENCYUSEQT,2) END [ORD_AMT] from ["+plant+"_PODET] where PLANT =A.PLANT and pono =A.ORDNO)T),0)   as orderAmt,"
							+"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] where  PLANT =A.PLANT and "
							+"ORDERTYPE='BILL' and ISNULL(PAYMENT_MODE,'')<>'' AND ORDNO = B.PONO   ),0)as amtReceived  ,"
							+"CASE WHEN Inbound_Gst>0 then 1+(Inbound_Gst/100) else 0 end  AS TAX  FROM "
							+"["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]A,["+plant+"_POHDR]B WHERE A.PLANT='"+plant+"' and "
							+"a.ORDNO = b.PONO " + searchCond1 + searchCondpmt +")A";
		        	
		        }
			orderPayDao.setmLogger(mLogger);
			al = orderPayDao.selectForReport(sql, htCond, extraCond);
		} catch (Exception e) {
			this.mLogger.Exception(e.getMessage());
			throw e;
		}

		return al;
	}
	
	public ArrayList getAgeingReportDetails(String prevFromDate,String balanceFrwdCond,String ExtraCond, String Paymenttype, String PmtDateRange, String OrdDateRange, String plant)
			throws Exception {
	 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		String sql = "";
		ArrayList al = null;
		try {
				Hashtable htCond = new Hashtable();
	        	sql = "SELECT '' as ROWID,'Balance Forward' as BILL,'"+prevFromDate+"' as TRANDATE,'' as Custcode,'' as AMOUNT," + 
	        			"BALANCE =  COALESCE(" + 
	        			"(" + 
	        			"  SELECT SUM(ROUND(AMOUNT_PAID,2))" + 
	        			"    FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]  AS I" + 
	        			"    WHERE "+balanceFrwdCond+ 
	        			"    and ISNULL(PAYMENT_MODE,'')=''  and "+ExtraCond+"), 0" + 
	        			")" + 
	        			"-" + 
	        			"COALESCE(" + 
	        			"(" + 
	        			"  SELECT SUM(ROUND(AMOUNT_PAID,2))" + 
	        			"    FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]  AS I" + 
	        			"    WHERE "+balanceFrwdCond+ 
	        			"    and ISNULL(PAYMENT_MODE,'')<>''  and "+ExtraCond+"  "+Paymenttype+" ), 0" + 
	        			")" + 
	        			" UNION " + 
	        			"SELECT ROWID,case when ISNULL(PAYMENT_REFNO,'')='' then ORDERNAME else ORDERNAME+'-'+PAYMENT_REFNO end[BILL]," + 
	        			"PAYMENT_DT as TRANDATE,CustCode,  " + 
	        			"case when "+ExtraCond+" and ISNULL(PAYMENT_MODE,'')='' then AMOUNT_PAID else -AMOUNT_PAID " + 
	        			"end[AMOUNT], " + 
	        			"BALANCE =  COALESCE(" + 
	        			"(" + 
	        			"  SELECT SUM(ROUND(AMOUNT_PAID,2))" + 
	        			"    FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]  AS I" + 
	        			"    WHERE I.ROWID <= PMT.ROWID AND " + 
	        			"    CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + " + 
	        			"	SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <=  " + 
	        			"     CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' + " + 
	        			"    SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date)  "+PmtDateRange+" and PMT.CustCode=I.CustCode  " + 
	        			"    and ISNULL(PAYMENT_MODE,'')=''  and  "+ExtraCond+"), 0" + 
	        			")" + 
	        			"-" + 
	        			"COALESCE(" + 
	        			"(" + 
	        			"  SELECT SUM(ROUND(AMOUNT_PAID,2))" + 
	        			"    FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]  AS I" + 
	        			"    WHERE I.ROWID <= PMT.ROWID AND" + 
	        			"      CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + " + 
	        			"	SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <=  " + 
	        			"     CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' + " + 
	        			"    SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) "+PmtDateRange+" and PMT.CustCode=I.CustCode  " + 
	        			"    and ISNULL(PAYMENT_MODE,'')<>''  and "+ExtraCond+"  "+Paymenttype+"), 0" + 
	        			")" + 
	        			"FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]  AS PMT" + 
	        			" WHERE PLANT = '"+plant+"' "+OrdDateRange+"";		        
			al = orderPayDao.selectForReport(sql, htCond, "");
		} catch (Exception e) {
			this.mLogger.Exception(e.getMessage());
			throw e;
		}
		return al;
	}
	
	public ArrayList getOutstandingAgeingReportDetails(String ExtraCond, String sCustCode, String PmtDateRange, String plant)
			throws Exception {
	 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		String sql = "";
		ArrayList al = null;
		try {
				Hashtable htCond = new Hashtable();
	        	sql = "SELECT * FROM (SELECT case when ISNULL(PAYMENT_REFNO,'')='' then ORDERNAME else ORDERNAME+'-'+PAYMENT_REFNO end[BILL]," + 
	        			"PAYMENT_DT as TRANDATE,CustCode,   ( Round(AMOUNT_PAID,2) - isnull((select sum(AMOUNT_PAID) " + 
	        			"from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] B where B.ORDNO=PMT.ORDNO " + 
	        			"and ISNULL(PAYMENT_MODE,'')<>''),0)) AS AMOUNT ," + 
	        			"(SELECT count(*) from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] f  where ISNULL(PAYMENT_MODE,'')='' and AMOUNT_PAID<0 and f.ORDNO=pmt.ORDNO) as minuCount," + 
	        			"(SELECT SUM(AMOUNT) from (SELECT ( Round(AMOUNT_PAID,2) - isnull((select sum(AMOUNT_PAID) " + 
	        			"from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] B where B.ORDNO=PMT1.ORDNO " + 
	        			"and ISNULL(PAYMENT_MODE,'')<>''),0)) AS AMOUNT " + 
	        			"from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] PMT1 where PMT1.ROWID <= (PMT.ROWID) and "+ExtraCond+" and CustCode= '"+sCustCode+"'  and CAST((SUBSTRING(PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PAYMENT_DT, 4, 2) + '-' + " + 
	        			"	SUBSTRING(PAYMENT_DT, 1, 2)) AS date)   <=  " + 
	        			"     CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' + " + 
	        			"    SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) "+PmtDateRange+" and ISNULL(PAYMENT_MODE,'')='') T ) BALANCE" + 
	        			" from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] PMT where ISNULL(PAYMENT_MODE,'')='' and "+ExtraCond+" and CustCode= '"+sCustCode+"'  and CAST((SUBSTRING(PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PAYMENT_DT, 4, 2) + '-' + " + 
	        			"	SUBSTRING(PAYMENT_DT, 1, 2)) AS date)   <=  " + 
	        			"     CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' + " + 
	        			"    SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) "+PmtDateRange+") D WHERE (D.minuCount>0 ) OR (D.AMOUNT>0 and D.BALANCE>0)";		        
			al = orderPayDao.selectForReport(sql, htCond, "");
		} catch (Exception e) {
			this.mLogger.Exception(e.getMessage());
			throw e;
		}
		return al;
	}
	
	public ArrayList getAgeingDetails(String plant,String afrmDate,String atoDate, String order,String custName,String orderno,String currencyid)
			throws Exception {
	 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		String sql = "";
		ArrayList al = null;
		try {
			Hashtable htCond = new Hashtable();
			String searchCond ="",searchCond1="",dtCondStr="",extraCon="",dtCondpmt="",searchCondord="",searchCondpmt="";
			              
	            if(custName.length()>0){
	            	searchCond =searchCond + " AND (   CUSTNAME ='"+custName+"' or CustCode = '"+custName+"') and currencyid='"+currencyid+"'";
	            }
	            if(custName.length()>0){
	            	
	            	/*searchCond1 =searchCond1 + " AND (   B.CUSTNAME ='"+custName+"' or B.CustCode = '"+custName+"' ) and a.currencyid='"+currencyid+"'";*/
	            	searchCond1 =searchCond1 + " AND (   B.CUSTNAME ='"+custName+"' or B.CustCode = '"+custName+"' ) ";
	            }
	            
	            dtCondStr = "  AND CAST((SUBSTRING(CollectionDate,7,4) + '-' + SUBSTRING(CollectionDate,4,2) + '-' + SUBSTRING(CollectionDate,1,2)) AS date) ";
	            dtCondpmt = "  AND CAST((SUBSTRING(payment_dt, 7, 4) + '-' + SUBSTRING(payment_dt, 4, 2) + '-' + SUBSTRING(payment_dt, 1, 2)) AS date)";
	            
	           // extraCon=" order by CAST((SUBSTRING(CollectionDate, 7, 4) + SUBSTRING(CollectionDate, 4, 2) + SUBSTRING(CollectionDate, 1, 2)) AS date) ";
	            if (afrmDate.length() > 0) {
					 searchCondord = searchCond + dtCondStr + "  >= '" 
							+ afrmDate
							+ "'  ";
					 searchCondpmt = searchCondpmt + dtCondpmt + "  >= '" 
								+ afrmDate
								+ "'  ";
					if (atoDate.length() > 0) {
						searchCondord = searchCondord +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						searchCondpmt = searchCondpmt +dtCondpmt+ " <= '" 
								+ atoDate
								+ "'  ";
					}
				} else {
					if (atoDate.length() > 0) {
						searchCondord = searchCond +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						searchCondpmt = searchCondpmt +dtCondpmt+ " <= '" 
								+ atoDate
								+ "'  ";
					}
				}    
	                        
				/*if(orderno.length()>0) 
				{
					if(order.equalsIgnoreCase("INVOICE")){
						searchCondord = searchCondord+" AND DONO='"+orderno+"' ";
						searchCondpmt = searchCondpmt+" AND ORDNO='"+orderno+"' ";
					}
					else if(order.equalsIgnoreCase("BILL")){
						searchCondord = searchCondord+" AND PONO='"+orderno+"' ";
						searchCondpmt = searchCondpmt+" AND ORDNO='"+orderno+"' ";
					}
				}*/
				 
	           String extraCond =" where orderAmt>0 ";
				 
	        if(order.equalsIgnoreCase("INVOICE"))
	        {	
	        	sql = ""+"SELECT orderNo,ordDate,Tax,orderAmt"
						+",amtReceived,((CAST(orderAmt AS DECIMAL(18, 2))) - ((CAST(amtReceived AS DECIMAL(18, 2))))) AS DueToPay FROM( "
						+"SELECT DONO AS orderNo,COLLECTIONDATE as ordDate, "
						+"  ISNULL((SELECT SUM(Tax+ORD_AMT) FROM (SELECT CASE WHEN Outbound_Gst>0 THEN ROUND(((Outbound_Gst/100))*ROUND(QTYOR * UNITPRICE*CURRENCYUSEQT,2),2) ELSE 0 END [TAX], ROUND(QTYOR*UNITPRICE*CURRENCYUSEQT,2) [ORD_AMT] from ["+plant+"_DODET] where PLANT =["+plant+"_DOHDR].PLANT and dono =["+plant+"_DOHDR].dono)T),0)   as orderAmt "
						+" ,ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] where  PLANT =["+plant+"_DOHDR].PLANT and ORDERTYPE='INVOICE' and ISNULL(PAYMENT_MODE,'')<>'' and  ORDNO =["+plant+"_DOHDR].dono "+searchCondpmt+"  ),0) as amtReceived "
						+ ", CASE WHEN Outbound_Gst>0 then 1+(Outbound_Gst/100) else 0 end AS TAX FROM ["+plant+"_DOHDR] WHERE PLANT='"+plant+"'" + searchCondord +") A" 
						+" UNION"
						+" SELECT orderNo,ordDate,Tax,orderAmt,"
						+"amtReceived,((CAST(orderAmt AS DECIMAL(18, 2))) - ((CAST(amtReceived AS DECIMAL(18, 2))))) AS DueToPay  FROM( "
						+"SELECT ORDNO AS orderNo, COLLECTIONDATE as ordDate,"
						+"ISNULL((SELECT SUM(Tax+ORD_AMT) FROM (SELECT CASE WHEN Outbound_Gst>0 THEN ROUND(((Outbound_Gst/100))*ROUND(QTYOR * UNITPRICE*CURRENCYUSEQT,2),2) ELSE 0 END [TAX], ROUND(QTYOR*UNITPRICE*CURRENCYUSEQT,2) [ORD_AMT] from ["+plant+"_DODET] where PLANT =A.PLANT and dono =A.ORDNO)T),0)   as orderAmt,"
						+"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] where  PLANT =A.PLANT and "
						+" ORDERTYPE='INVOICE' and ISNULL(PAYMENT_MODE,'')<>'' AND ORDNO = B.DONO   ),0)as amtReceived  ,"
						+"CASE WHEN Outbound_Gst>0 then 1+(Outbound_Gst/100) else 0 end  AS TAX  FROM "
						+"["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]A,["+plant+"_DOHDR]B WHERE A.PLANT='"+plant+"' and "
						+"a.ORDNO = b.DONO " +searchCond1 + searchCondpmt +")A";
		        }else{
		        	sql = ""+"SELECT orderNo,ordDate,Tax,orderAmt"
							+",amtReceived,((CAST(orderAmt AS DECIMAL(18, 2))) - ((CAST(amtReceived AS DECIMAL(18, 2))))) AS DueToPay FROM( "
							+"SELECT PONO AS orderNo,COLLECTIONDATE as ordDate, "
							+"  ISNULL((SELECT SUM(ORD_AMT) FROM (SELECT CASE WHEN Inbound_Gst>0 THEN ROUND((1+(Inbound_Gst/100))*ROUND(QTYOR * UNITCOST*CURRENCYUSEQT,2),2) ELSE ROUND(QTYOR * UNITCOST*CURRENCYUSEQT,2) END [ORD_AMT] from ["+plant+"_PODET] where PLANT =["+plant+"_POHDR].PLANT and  Pono =["+plant+"_POHDR].PONO)T),0)   as orderAmt "
							+" ,ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] where  PLANT =["+plant+"_POHDR].PLANT and ORDERTYPE='BILL' and ISNULL(PAYMENT_MODE,'')<>'' and ORDNO =["+plant+"_POHDR].PONO "+searchCondpmt+" ),0) as amtReceived "
							+ ", CASE WHEN Inbound_Gst>0 then 1+(Inbound_Gst/100) else 0 end AS TAX FROM ["+plant+"_POHDR] WHERE PLANT='"+plant+"'" + searchCondord +") A"
							+" UNION"
							+" SELECT orderNo,ordDate,Tax,orderAmt,"
							+"amtReceived,((CAST(orderAmt AS DECIMAL(18, 2))) - ((CAST(amtReceived AS DECIMAL(18, 2))))) AS DueToPay  FROM( "
							+"SELECT ORDNO AS orderNo, COLLECTIONDATE as ordDate,"
							+"ISNULL((SELECT SUM(ORD_AMT) FROM (SELECT CASE WHEN Inbound_Gst>0 THEN ROUND((1+(Inbound_Gst/100))*ROUND(QTYOR * UNITCOST*CURRENCYUSEQT,2),2) ELSE ROUND(QTYOR * UNITCOST*CURRENCYUSEQT,2) END [ORD_AMT] from ["+plant+"_PODET] where PLANT =A.PLANT and pono =A.ORDNO)T),0)   as orderAmt,"
							+"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] where  PLANT =A.PLANT and "
							+"ORDERTYPE='BILL' and ISNULL(PAYMENT_MODE,'')<>'' AND ORDNO = B.PONO   ),0)as amtReceived  ,"
							+"CASE WHEN Inbound_Gst>0 then 1+(Inbound_Gst/100) else 0 end  AS TAX  FROM "
							+"["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]A,["+plant+"_POHDR]B WHERE A.PLANT='"+plant+"' and "
							+"a.ORDNO = b.PONO " + searchCond1 + searchCondpmt +")A";
		        	
		        }
			orderPayDao.setmLogger(mLogger);
			al = orderPayDao.selectForReport(sql, htCond, extraCond);
		} catch (Exception e) {
			this.mLogger.Exception(e.getMessage());
			throw e;
		}

		return al;
	}
	
	public ArrayList getAgeingDetailsCount(String plant,String currencycond,String sCustCode,String orderno,String extraCon)
			throws Exception {
	 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		String sql = "";
		ArrayList al = null;
		try {
			Hashtable htCond = new Hashtable();
			String searchCond ="",searchCond1="",dtCondStr="",dtCondpmt="",searchCondord="",searchCondpmt="";
			      sql="SELECT * FROM (SELECT case when ISNULL(PAYMENT_REFNO,'')='' then ORDNO else ORDNO+'-'+PAYMENT_REFNO end[ORDNO]," +
			      		"PAYMENT_DT as TRANDATE,CustCode,   ( AMOUNT_PAID - isnull((select sum(AMOUNT_PAID) from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] B where B.ORDNO=PMT.ORDNO" +
			      		" and ISNULL(PAYMENT_MODE,'')<>''),0)) AS AMOUNT ," +
			      		" (SELECT count(*) from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] f  where ISNULL(PAYMENT_MODE,'')='' and AMOUNT_PAID<0 and f.ORDNO=pmt.ORDNO) as minuCount, " +
			      		"(SELECT SUM(AMOUNT) from (SELECT ( AMOUNT_PAID - isnull((select sum(AMOUNT_PAID) from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] B" +
			      		" where B.ORDNO=PMT1.ORDNO and ISNULL(PAYMENT_MODE,'')<>''),0)) AS AMOUNT from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] PMT1 where PMT1.ROWID <= (PMT.ROWID)" +
			      				" and "+extraCon+" and CustCode= '"+sCustCode+"'  and" +
			      		"  CAST((SUBSTRING(PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PAYMENT_DT, 4, 2) + '-' + 	SUBSTRING(PAYMENT_DT, 1, 2)) AS " +
			      		"date)   <=       CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' +     SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) "+ currencycond+ " " +		      		
			      		"   and ISNULL(PAYMENT_MODE,'')='') T ) BALANCE from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] PMT where" +
			      		" ISNULL(PAYMENT_MODE,'')='' and "+extraCon+" and CustCode= '"+sCustCode+"'  and CAST((SUBSTRING(PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PAYMENT_DT, 4, 2) + '-' + 	SUBSTRING(PAYMENT_DT, 1, 2)) AS date)" +
			      		"   <=       CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' +     SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) "+ currencycond+ " ) D WHERE (D.minuCount>0 ) OR (D.AMOUNT>0 and D.BALANCE>0)" ;
			orderPayDao.setmLogger(mLogger);
			al = orderPayDao.selectForReport(sql,htCond,"");
		} catch (Exception e) {
			this.mLogger.Exception(e.getMessage());
			throw e;
		}
		return al;
	}
	
	public ArrayList getcustomerorsuppliernameConsolidated(String plant,String afrmDate,String atoDate, String order,String custname)
			throws Exception {
	 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		String sql = "";
		ArrayList al = null;
		try {
			Hashtable htCond = new Hashtable();
			String searchCond ="",dtCondStr="",extraCon="",dtCondpmt="",searchCondord="",searchCondpmt="";
			   
	            dtCondStr = "  AND CAST((SUBSTRING(CollectionDate,7,4) + '-' + SUBSTRING(CollectionDate,4,2) + '-' + SUBSTRING(CollectionDate,1,2)) AS date) ";
	           
	            if(custname.length()>0){
	            	searchCond =searchCond + " AND (   CUSTNAME ='"+custname+"' or CustCode = '"+custname+"') ";
	            }
	            
	            if (afrmDate.length() > 0) {
					 searchCondord = searchCond + dtCondStr + "  >= '" 
							+ afrmDate
							+ "'  ";
					 
					if (atoDate.length() > 0) {
						searchCondord = searchCondord +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						
					}
				} else {
					if (atoDate.length() > 0) {
						searchCondord = searchCond +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						
					}
				}    
	                        
				
				 
	        if(order.equalsIgnoreCase("INVOICE"))
	        {	/*
	        	sql = ""+"SELECT distinct custname,custcode,currencyid,ISNULL(PAY_IN_DAYS,'')pmtdays from "
						+"["+plant+"_DOHDR]A,["+plant+"_CUSTMST]B WHERE A.PLANT='"+plant+"' AND A.CustCode=B.CUSTNO"+searchCondord;*/

	        	sql = ""+"SELECT distinct custname,custcode,currencyid,ISNULL(PAY_IN_DAYS,'')pmtdays from "
						+"["+plant+"_DOHDR]A,["+plant+"_CUSTMST]B WHERE A.PLANT='"+plant+"' AND A.CustCode=B.CUSTNO"+searchCondord+" ORDER BY currencyid";
						
		        }else{
		         sql = ""+"SELECT distinct custname,custcode,currencyid,ISNULL(PAY_IN_DAYS,'')pmtdays from "
							+"["+plant+"_POHDR]A,["+plant+"_VENDMST]B WHERE A.PLANT='"+plant+"' AND A.CustCode=B.VENDNO"+searchCondord+" ORDER BY currencyid";
		        }
			orderPayDao.setmLogger(mLogger);
			al = orderPayDao.selectForReport(sql, htCond, "");
		} catch (Exception e) {
			this.mLogger.Exception(e.getMessage());
			throw e;
		}

		return al;
	}
	
	public ArrayList getAgeingDetailsConsolidate(String plant,String afrmDate,String atoDate, String order,String custName,String orderno,String currencyid)
			throws Exception {
	 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		String sql = "";
		ArrayList al = null;
		try {
			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			Hashtable htCond = new Hashtable();
			String searchCond ="",searchCond1="",dtCondStr="",extraCon="",dtCondpmt="",searchCondord="",searchCondpmt="",sCustCode="";
			              
	            if(custName.length()>0){
	            	sCustCode = customerBeanDAO.getCustomerCode(plant, custName);
	            	searchCond = " AND CUSTNO =N'"+sCustCode+"'";
	            }
	            if(custName.length()>0){
	            	
	            	//searchCond1 =searchCond1 + " AND (   B.CUSTNAME ='"+custName+"' or B.CustCode = '"+custName+"' ) and a.currencyid='"+currencyid+"'";
	            	
	            	searchCond1 =searchCond1 + "  AND (   B.CUSTNAME ='"+custName+"' or B.CustCode = '"+custName+"' )";
	            }
	            
	            dtCondStr = "  AND CAST((SUBSTRING(INVOICE_DATE,7,4) + '-' + SUBSTRING(INVOICE_DATE,4,2) + '-' + SUBSTRING(INVOICE_DATE,1,2)) AS date) ";
	            dtCondpmt = "  AND CAST((SUBSTRING(payment_dt, 7, 4) + '-' + SUBSTRING(payment_dt, 4, 2) + '-' + SUBSTRING(payment_dt, 1, 2)) AS date)";
	            
	           // extraCon=" order by CAST((SUBSTRING(CollectionDate, 7, 4) + SUBSTRING(CollectionDate, 4, 2) + SUBSTRING(CollectionDate, 1, 2)) AS date) ";
	            if (afrmDate.length() > 0) {
					 searchCondord = searchCond + dtCondStr + "  >= '" 
							+ afrmDate
							+ "'  ";
					 searchCondpmt = searchCondpmt + dtCondpmt + "  >= '" 
								+ afrmDate
								+ "'  ";
					if (atoDate.length() > 0) {
						searchCondord = searchCondord +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						searchCondpmt = searchCondpmt +dtCondpmt+ " <= '" 
								+ atoDate
								+ "'  ";
					}
				} else {
					if (atoDate.length() > 0) {
						searchCondord = searchCond +dtCondStr+ " <= '" 
						+ atoDate
						+ "'  ";
						searchCondpmt = searchCondpmt +dtCondpmt+ " <= '" 
								+ atoDate
								+ "'  ";
					}
				}    
	                        
				if(orderno.length()>0) 
				{
					if(order.equalsIgnoreCase("INVOICE")){
						searchCondord = searchCondord+" AND DONO='"+orderno+"' ";
						searchCondpmt = searchCondpmt+" AND ORDNO='"+orderno+"' ";
					}
					else if(order.equalsIgnoreCase("BILL")){
						searchCondord = searchCondord+" AND PONO='"+orderno+"' ";
						searchCondpmt = searchCondpmt+" AND ORDNO='"+orderno+"' ";
					}
				}
				 
	           String extraCond =" where orderAmt>0 ";
				 
	        if(order.equalsIgnoreCase("INVOICE"))
	        {	
	        	sql = "SELECT orderNo,ordDate,Tax,orderAmt,amtReceived," + 
	        			"((CAST(orderAmt AS DECIMAL(18, 2))) - " + 
	        			"((CAST(amtReceived AS DECIMAL(18, 2))))) AS DueToPay FROM( " + 
	        			"SELECT DONO AS orderNo,INVOICE_DATE as ordDate," +
	        			"ISNULL(I.TOTAL_AMOUNT,0)   as orderAmt  ," + 
	        			"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] " + 
	        			"where  PLANT =I.PLANT and ORDERTYPE='INVOICE' and ISNULL(PAYMENT_MODE,'')<>'' and  ORDNO =I.dono AND ORDERNAME = I.INVOICE " + 
	        			"AND CAST((SUBSTRING(payment_dt, 7, 4) + '-' + SUBSTRING(payment_dt, 4, 2) + '-' + SUBSTRING(payment_dt, 1, 2)) AS date) " + 
	        			">= '2021-01-01'    ),0) as amtReceived ," + 
	        			"I.TAXAMOUNT AS TAX FROM ["+plant+"_FININVOICEHDR] I WHERE PLANT='"+plant+"'" + searchCondord +") A";
		        }else{
		        	sql = ""+"SELECT orderNo,ordDate,Tax,orderAmt"
							+",amtReceived,((CAST(orderAmt AS DECIMAL(18, 2))) - ((CAST(amtReceived AS DECIMAL(18, 2))))) AS DueToPay FROM( "
							+"SELECT PONO AS orderNo,COLLECTIONDATE as ordDate, "
							+"  ISNULL((SELECT SUM(ORD_AMT) FROM (SELECT CASE WHEN Inbound_Gst>0 THEN ROUND((1+(Inbound_Gst/100))*ROUND(QTYOR * UNITCOST*CURRENCYUSEQT,2),2) ELSE ROUND(QTYOR * UNITCOST*CURRENCYUSEQT,2) END [ORD_AMT] from ["+plant+"_PODET] where PLANT =["+plant+"_POHDR].PLANT and  Pono =["+plant+"_POHDR].PONO)T),0)   as orderAmt "
							+" ,ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] where  PLANT =["+plant+"_POHDR].PLANT and ORDERTYPE='BILL' and ISNULL(PAYMENT_MODE,'')<>'' and ORDNO =["+plant+"_POHDR].PONO "+searchCondpmt+" ),0) as amtReceived "
							+ ", CASE WHEN Inbound_Gst>0 then 1+(Inbound_Gst/100) else 0 end AS TAX FROM ["+plant+"_POHDR] WHERE PLANT='"+plant+"'" + searchCondord +") A"
							+" UNION"
							+" SELECT orderNo,ordDate,Tax,orderAmt,"
							+"amtReceived,((CAST(orderAmt AS DECIMAL(18, 2))) - ((CAST(amtReceived AS DECIMAL(18, 2))))) AS DueToPay  FROM( "
							+"SELECT ORDNO AS orderNo, COLLECTIONDATE as ordDate,"
							+"ISNULL((SELECT SUM(ORD_AMT) FROM (SELECT CASE WHEN Inbound_Gst>0 THEN ROUND((1+(Inbound_Gst/100))*ROUND(QTYOR * UNITCOST*CURRENCYUSEQT,2),2) ELSE ROUND(QTYOR * UNITCOST*CURRENCYUSEQT,2) END [ORD_AMT] from ["+plant+"_PODET] where PLANT =A.PLANT and pono =A.ORDNO)T),0)   as orderAmt,"
							+"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET] where  PLANT =A.PLANT and "
							+"ORDERTYPE='BILL' and ISNULL(PAYMENT_MODE,'')<>'' AND ORDNO = B.PONO   ),0)as amtReceived  ,"
							+"CASE WHEN Inbound_Gst>0 then 1+(Inbound_Gst/100) else 0 end  AS TAX  FROM "
							+"["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]A,["+plant+"_POHDR]B WHERE A.PLANT='"+plant+"' and "
							+"a.ORDNO = b.PONO " + searchCond1 + searchCondpmt +")A";
		        	
		        }
			orderPayDao.setmLogger(mLogger);
			al = orderPayDao.selectForReport(sql, htCond, extraCond);
		} catch (Exception e) {
			this.mLogger.Exception(e.getMessage());
			throw e;
		} 

		return al;
	}
	
	public ArrayList getConsolidateAgeingReportDetails(String ExtraCond, String Paymenttype, String PmtDateRange, String OrdDateRange, String OrderStatus, String plant)
			throws Exception {
	 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		String sql = "";
		ArrayList al = null;
		try {
				Hashtable htCond = new Hashtable();
	        	sql = "SELECT case when ISNULL(PAYMENT_REFNO,'')='' then ORDERNAME else ORDERNAME+'-'+PAYMENT_REFNO end[BILL]," + 
	        			"PAYMENT_DT as TRANDATE,CustCode as CustName,CASE WHEN ("+OrderStatus+"=1)  THEN (SELECT distinct CUSTNO FROM ["+plant+"_CUSTMST] where CNAME=PMT.CustCode)  ELSE (SELECT distinct VENDNO FROM ["+plant+"_VENDMST] where VNAME=PMT.CustCode)  END  AS CustCode," + 
	        			"case when "+ExtraCond+" and ISNULL(PAYMENT_MODE,'')='' then AMOUNT_PAID else -AMOUNT_PAID " + 
	        			"end[AMOUNT], " + 
	        			"BALANCE =  COALESCE(" + 
	        			"(" + 
	        			"  SELECT SUM(ROUND(AMOUNT_PAID,2))" + 
	        			"    FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]  AS I" + 
	        			"    WHERE I.ROWID <= PMT.ROWID AND " + 
	        			"    CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + " + 
	        			"	SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <=  " + 
	        			"     CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' + " + 
	        			"    SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date)  "+PmtDateRange+" and PMT.CustCode=I.CustCode  " + 
	        			"    and ISNULL(PAYMENT_MODE,'')=''  and  "+ExtraCond+"), 0" + 
	        			")" + 
	        			"-" + 
	        			"COALESCE(" + 
	        			"(" + 
	        			"  SELECT SUM(ROUND(AMOUNT_PAID,2))" + 
	        			"    FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]  AS I" + 
	        			"    WHERE I.ROWID <= PMT.ROWID AND" + 
	        			"      CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + " + 
	        			"	SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <=  " + 
	        			"     CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' + " + 
	        			"    SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) "+PmtDateRange+" and PMT.CustCode=I.CustCode  " + 
	        			"    and ISNULL(PAYMENT_MODE,'')<>''  and "+ExtraCond+"  "+Paymenttype+"), 0" + 
	        			")" + 
	        			" FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]  AS PMT " + 
	        			" where PLANT = '"+plant+"' "+OrdDateRange+" " + 
	        			" ORDER BY ROWID";		        
			al = orderPayDao.selectForReport(sql, htCond, "");
		} catch (Exception e) {
			this.mLogger.Exception(e.getMessage());
			throw e;
		}
		return al;
	}
	
	public ArrayList getInvoiceNoForAutoSuggestion(Hashtable ht, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",custNo="",extraCon="";
		StringBuffer sql;
		CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
		Hashtable htData = new Hashtable();
		try {
			billDao.setmLogger(mLogger);
			          
           sql = new StringBuffer("SELECT ID, CUSTNO, INVOICE, DONO, INVOICE_DATE, DUE_DATE, PAYMENT_TERMS, EMPNO,"
           		+ "ITEM_RATES, DISCOUNT, DISCOUNT_TYPE, DISCOUNT_ACCOUNT,SHIPPINGCOST, ADJUSTMENT, "
           		+ "SUB_TOTAL, TOTAL_AMOUNT, BILL_STATUS, NOTE, TERMSCONDITIONS "
           		+ " FROM ["+plant+"_FININVOICEHDR] WHERE PLANT='"+ plant+"'" + sCondition);		           
         
           if (ht.get("DONO") != null && ht.get("DONO") != "") {
			  sql.append(" AND DONO = '" + ht.get("DONO") + "'");
		    }
		   if (ht.get("CUSTNO") != null && ht.get("CUSTNO") != "") {
			   custNo = customerBeanDAO.getCustomerCode(plant, (String)ht.get("CUSTNO"));
			  sql.append(" AND CUSTNO = '" + custNo + "'");
		    }
		   if (ht.get("INVOICE") != null && ht.get("INVOICE") != "") {
				  sql.append(" AND INVOICE LIKE '%"+ht.get("INVOICE")+"%'");
		    }
		   
		   extraCon = "ORDER BY ID DESC";
		   
		   arrList = billDao.selectForReport(sql.toString(), htData, extraCon);
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
		}
		return arrList;
	}
	
	public ArrayList getPlantMstDetails(String plant) {
		ArrayList al = null;
		PlantMstDAO plantmst = new PlantMstDAO();
		plantmst.setmLogger(mLogger);
		try {
			al = plantmst.getPlantMstDetails(plant);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return al;
	}
	
	public ArrayList getInvListAgingSummaryByCost(String aPlant, String aItem, 
			String productDesc, Hashtable ht,String loc,String loctypeid,String loctypeid2,String loctypeid3,String afrmDate, String atoDate) throws Exception {
		ArrayList arrList = new ArrayList();
		String sCondition=""; 
        String aLocCond ="",dtCondStr="",searchCondord="";
			            
        dtCondStr = "  AND CAST((SUBSTRING(b.recvdate,7,4) + '-' + SUBSTRING(b.recvdate,4,2) + '-' + SUBSTRING(b.recvdate,1,2)) AS date) ";
		    
        if (afrmDate.length() > 0) {
			 searchCondord += dtCondStr+ " >= '" 
						+ afrmDate
						+ "'  ";
		}
        
        if (atoDate.length() > 0) {
			searchCondord += dtCondStr+ " <= '" 
			+ atoDate
			+ "'  ";
			
		}
	    if (productDesc.length() > 0 ) {
	        if (productDesc.indexOf("%") != -1) {
	                productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
	        }
         
	        sCondition = " and replace(b.ITEMDESC,' ','') like N'%"+ new StrUtils().InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
	    }
	    if (loctypeid != null && loctypeid!="") {
			sCondition = sCondition + " AND a.LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
		}
	    if (loctypeid2 != null && loctypeid2!="") {
			sCondition = sCondition + " AND a.LOC IN ( "+new LocMstDAO().getLocForLocType2(aPlant,loctypeid2)+" )";
		}
	    if (loctypeid3 != null && loctypeid3!="") {
			sCondition = sCondition + " AND a.LOC IN ( "+new LocMstDAO().getLocForLocType3(aPlant,loctypeid3)+" )";
		}

        if(loc.length()>0){
        loc = "  AND A.LOC like'%"+loc+"%'";
            aLocCond = ", a.loc ";
        }
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			sCondition = sCondition
					+ " group by a.item,a.loc,c.ITEMDESC,c.COST,c.UnitPrice,c.stkuom,a.userfld4,b.recvdate,QTY,b.RECQTY"+aLocCond +" Order by ITEM,LOC";

			String aQuery = "select isnull(a.ITEM,'') ITEM,UPPER(a.LOC) LOC,isnull(c.ITEMDESC,'') ITEMDESC,isnull(c.COST,'') COST,isnull(c.UnitPrice,'') UnitPrice,c.stkuom as STKUOM,isnull(a.userfld4,'') BATCH,QTY, SUM(b.RECQTY) RECQTY,isnull(b.recvdate,'') RECVDATE  from "
					+ "["
					+ aPlant
					+ "_"
					+ "invmst"
					+ "]"
					+ " A,"
					+ "["
					+ aPlant
					+ "_"
					+ "recvdet"
					+ "]"
					+ " as b,"
					+ "["
					+ aPlant
					+ "_"
					+ "itemmst"
					+ "]"
					+ " as c"
					+" where a.item=b.item and b.item=c.item and a.PLANT = b.PLANT and a.PLANT = c.PLANT and a.PLANT = '"+ aPlant+"' and a.item in (SELECT ITEM FROM "
					+ aPlant
					+ "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"
					//+ aItem + "%') AND b.NONSTKFLAG<>'Y' AND  QTY>0 and c.tran_type in('MR','IB') "+loc+searchCondord;
					+ aItem + "%') AND c.NONSTKFLAG<>'Y' AND  QTY>0 and b.tran_type in('MR','IB','BILL') "+loc+searchCondord;
			arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	public ArrayList getInvListAgingSummaryByCostWithStktake(String aPlant, String aItem, 
			String productDesc, Hashtable ht,String loc,String loctypeid,String loctypeid2,String loctypeid3,String afrmDate, String atoDate) throws Exception {
		ArrayList arrList = new ArrayList();
		String sCondition=""; 
		String aLocCond ="",dtCondStr="",searchCondord="";
		
		dtCondStr = "  AND CAST((SUBSTRING(b.recvdate,7,4) + '-' + SUBSTRING(b.recvdate,4,2) + '-' + SUBSTRING(b.recvdate,1,2)) AS date) ";
		
		if (afrmDate.length() > 0) {
			searchCondord += dtCondStr+ " >= '" 
					+ afrmDate
					+ "'  ";
		}
		
		if (atoDate.length() > 0) {
			searchCondord += dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
			
		}
		if (productDesc.length() > 0 ) {
			if (productDesc.indexOf("%") != -1) {
				productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
			}
			
			sCondition = " and replace(CombinedResults.ITEMDESC,' ','') like N'%"+ new StrUtils().InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
		}
		if (loctypeid != null && loctypeid!="") {
			sCondition = sCondition + " AND CombinedResults.LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
		}
		if (loctypeid2 != null && loctypeid2!="") {
			sCondition = sCondition + " AND CombinedResults.LOC IN ( "+new LocMstDAO().getLocForLocType2(aPlant,loctypeid2)+" )";
		}
		if (loctypeid3 != null && loctypeid3!="") {
			sCondition = sCondition + " AND CombinedResults.LOC IN ( "+new LocMstDAO().getLocForLocType3(aPlant,loctypeid3)+" )";
		}
		
		if(loc.length()>0){
			loc = "  AND A.LOC like'%"+loc+"%'";
			aLocCond = ", a.loc ";
		}
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);

			sCondition = sCondition
					+ " GROUP BY ITEM, LOC, ITEMDESC, COST, UnitPrice, STKUOM, BATCH, RECVDATE,QTY ORDER BY ITEM, LOC";
			
			String aQuery = "SELECT     ITEM,    LOC,    ITEMDESC,    COST,    UnitPrice,    STKUOM,    BATCH,    QTY AS QTY,    SUM(RECQTY) AS RECQTY,    RECVDATE "
					+ "FROM ( SELECT ISNULL(a.PLANT, '') AS PLANT,ISNULL(a.ITEM, '') AS ITEM,        UPPER(a.LOC) AS LOC,        ISNULL(c.ITEMDESC, '') AS ITEMDESC,ISNULL(c.COST, '') AS COST, "
					+ "ISNULL(c.UnitPrice, '') AS UnitPrice,        c.stkuom AS STKUOM,        ISNULL(a.userfld4, '') AS BATCH,        QTY,        SUM(b.RECQTY) AS RECQTY, "
					+ "CAST((SUBSTRING(b.recvdate, 7, 4) + '-' + SUBSTRING(b.recvdate, 4, 2) + '-' + SUBSTRING(b.recvdate, 1, 2)) AS date ) AS RECVDATE "
					    + "FROM ["+aPlant+"_invmst] A JOIN ["+aPlant+"_recvdet] B ON a.item = b.item AND a.PLANT = b.PLANT "
					    + "JOIN ["+aPlant+"_itemmst] C ON b.item = c.item AND a.PLANT = c.PLANT "
					    + "WHERE a.PLANT = '"+aPlant+"' AND a.item IN ( SELECT ITEM "
					    + "FROM "+aPlant+"_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME LIKE '"+ aItem +"%') "
					        + "AND c.NONSTKFLAG <> 'Y' AND QTY > 0 AND b.tran_type IN ('MR', 'IB', 'BILL') "
					        +loc+searchCondord
					        + "GROUP BY a.PLANT,a.item, a.loc, c.ITEMDESC, c.COST, c.UnitPrice, c.stkuom, a.userfld4, b.recvdate, A.QTY "

					        + "UNION ALL "

						+ "SELECT ISNULL(a.PLANT, '') AS PLANT,ISNULL(a.ITEM, '') AS ITEM, UPPER(a.LOC) AS LOC,ISNULL(c.ITEMDESC, '') AS ITEMDESC,"
						+ "ISNULL(c.COST, '') AS COST,ISNULL(c.UnitPrice, '') AS UnitPrice,c.stkuom AS STKUOM,"
					    + "ISNULL(a.userfld4, '') AS BATCH,A.QTY,SUM(b.QTY) AS RECQTY,CAST((b.PURCHASEDATE) AS date) AS RECVDATE "
					    + "FROM ["+aPlant+"_invmst] A JOIN ["+aPlant+"_SALES_DETAIL] B ON a.item = b.item AND a.PLANT = b.PLANT "
					    + "JOIN ["+aPlant+"_itemmst] C ON b.item = c.item AND a.PLANT = c.PLANT "
					    + "WHERE a.PLANT = '"+aPlant+"' AND a.item IN (SELECT ITEM FROM "+aPlant+"_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME LIKE '"+ aItem +"%') "
					        + "AND c.NONSTKFLAG <> 'Y' AND A.QTY > 0 AND b.TRANTYPE IN ('MOVEWITHBATCH') "
					        + loc
					        + "AND CAST(b.PURCHASEDATE AS date) >= '"+afrmDate+"' "
					    + "GROUP BY a.PLANT,a.item, a.loc, c.ITEMDESC, c.COST, c.UnitPrice, c.stkuom, a.userfld4, b.PURCHASEDATE, A.QTY "
					+ ") AS CombinedResults where CombinedResults.ITEM!='' ";

			
			
			arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
	
	public Map getAgingSummaryForReport(Hashtable ht) throws Exception{
		Map agingDetail = new Hashtable();
		AgeingDAO ageingDao = new AgeingDAO();
		agingDetail = ageingDao.getAgingSummaryForReport(ht);
		return agingDetail;
	}
	
	public Map getAgingSummaryForReportBYCURRENCY(Hashtable ht,String currency) throws Exception{
		Map agingDetail = new Hashtable();
		AgeingDAO ageingDao = new AgeingDAO();
		agingDetail = ageingDao.getAgingSummaryForReportBycurrecy(ht,currency);
		return agingDetail;
	}
	
	public ArrayList getAgingDetailForReport(Hashtable ht) throws Exception{
		ArrayList al = new ArrayList();
		AgeingDAO ageingDao = new AgeingDAO();
		al = ageingDao.getAgingDetailForReport(ht);
		return al;
	}
	
	public ArrayList getAgingDetailForReportbycurrency(Hashtable ht,String currency) throws Exception{
		ArrayList al = new ArrayList();
		AgeingDAO ageingDao = new AgeingDAO();
		al = ageingDao.getAgingDetailForReportByCurrency(ht,currency);
		return al;
	}
	
	public Map getSupplierAgingSummaryForReport(Hashtable ht) throws Exception{
		Map agingDetail = new Hashtable();
		AgeingDAO ageingDao = new AgeingDAO();
		agingDetail = ageingDao.getSupplierAgingSummaryForReport(ht);
		return agingDetail;
	}
	
	public Map getSupplierAgingSummaryForReportByCurrency(Hashtable ht,String currency) throws Exception{
		Map agingDetail = new Hashtable();
		AgeingDAO ageingDao = new AgeingDAO();
		agingDetail = ageingDao.getSupplierAgingSummaryForReportByCurrency(ht,currency);
		return agingDetail;
	}
	
	public ArrayList getSupplierAgingDetailForReport(Hashtable ht) throws Exception{
		ArrayList al = new ArrayList();
		AgeingDAO ageingDao = new AgeingDAO();
		al = ageingDao.getSupplierAgingDetailForReport(ht);
		return al;
	}
	
	public ArrayList getSupplierAgingDetailForReportBycurrency(Hashtable ht,String currency) throws Exception{
		ArrayList al = new ArrayList();
		AgeingDAO ageingDao = new AgeingDAO();
		al = ageingDao.getSupplierAgingDetailForReportBycurrency(ht,currency);
		return al;
	}
	
	public ArrayList getAgingSummaryForDashboard(Hashtable ht) throws Exception{
		ArrayList al = new ArrayList();
		AgeingDAO ageingDao = new AgeingDAO();
		al = ageingDao.getAgingSummaryForDashboard(ht);
		return al;
	}
	
	public ArrayList getSupplierAgingSummaryForDashboard(Hashtable ht) throws Exception{
		ArrayList al = new ArrayList();
		AgeingDAO ageingDao = new AgeingDAO();
		al = ageingDao.getSupplierAgingSummaryForDashboard(ht);
		return al;
	}
	
	public ArrayList getAccountPayableForDashboard(Hashtable ht) throws Exception{
		ArrayList al = new ArrayList();
		AgeingDAO ageingDao = new AgeingDAO();
		al = ageingDao.getAccountPayableForDashboard(ht);
		return al;
	}
	
	public ArrayList getAccountReceivableForDashboard(Hashtable ht) throws Exception{
		ArrayList al = new ArrayList();
		AgeingDAO ageingDao = new AgeingDAO();
		al = ageingDao.getAccountReceivableForDashboard(ht);
		return al;
	}
}
