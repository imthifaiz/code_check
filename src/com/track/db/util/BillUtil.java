package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.dao.AgeingDAO;
import com.track.dao.BillDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.OrderPaymentDAO;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;
@SuppressWarnings({"rawtypes", "unchecked"})
public class BillUtil {
	private boolean printLog = MLoggerConstant.BillUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	private BillDAO billDao = new BillDAO();
	
	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public int addBillHdr(Hashtable ht, String plant) {
		int billHdrId = 0;
		try {
			billHdrId = billDao.addBillHdr(ht, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return billHdrId;		
	}
	
	public boolean addMultipleBillDet(List<Hashtable<String, String>> billDetInfoList, String plant){
		boolean flag = false;		
		try {
			flag = billDao.addMultipleBillDet(billDetInfoList, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean addBillAttachments(List<Hashtable<String, String>> billAttachmentList, String plant){
		boolean flag = false;		
		try {
			flag = billDao.addBillAttachments(billAttachmentList, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	 public ArrayList getBillSummaryView(Hashtable ht, String afrmDate,
				String atoDate, String plant, String custname) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",dtCondStr="",extraCon="";
			StringBuffer sql;
			 Hashtable htData = new Hashtable();
			try {
				billDao.setmLogger(mLogger);

		        		dtCondStr ="and ISNULL(A.BILL_DATE,'')<>'' AND CAST((SUBSTRING(A.BILL_DATE, 7, 4) + '-' + SUBSTRING(a.BILL_DATE, 4, 2) + '-' + SUBSTRING(a.BILL_DATE, 1, 2)) AS date)";
		        		extraCon= "order by A.ID desc,CAST((SUBSTRING(A.BILL_DATE, 7, 4) + SUBSTRING(A.BILL_DATE, 4, 2) + SUBSTRING(A.BILL_DATE, 1, 2)) AS date) desc";    			        
				       
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
	                 	custname = StrUtils.InsertQuotes(custname);
	                 	sCondition = sCondition + " AND VNAME LIKE '%"+custname+"%' " ;
	                  } 
				          
				           sql = new StringBuffer("select A.ID as ID,BILL_DATE,BILL,A.PONO,ISNULL(GRNO,'') as GRNO,ISNULL(A.ORDERTYPE,'') AS ORDERTYPE,A.VENDNO,A.REFERENCE_NUMBER,");
				           //sql.append(" ISNULL(VNAME,'') as VNAME,ISNULL((select CURRENCYID from " + plant +"_POHDR p where p.PONO=A.PONO ),'') as CURRENCYID,");
				           sql.append(" ISNULL((SELECT top 1 B.CURRENCYUSEQT FROM " + plant +"_FINBILLDET B WHERE B.BILLHDRID=A.ID),1) CURRENCYUSEQT,");
				           sql.append(" ISNULL(VNAME,'') as VNAME,ISNULL(CURRENCYID,'') as CURRENCYID,");
				           sql.append(" BILL_STATUS,DUE_DATE,cast(TOTAL_AMOUNT as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,cast((TOTAL_AMOUNT-(select ISNULL(SUM(AMOUNT),0) FROM " + plant+"_FINPAYMENTDET WHERE PONO= A.PONO AND TYPE= 'REGULAR' AND PLANT= '"+plant+"' AND BILLHDRID = A.ID)) as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as BALANCE_DUE,");				           
				           sql.append("cast((select ISNULL(SUM(BALANCE),0) from " + plant +"_FINPAYMENTDET where pono=A.PONO AND TYPE='ADVANCE' AND PONO <> '') as DECIMAL(20,5)) as AVAILABLE_CREDIT");
				           sql.append(" from " + plant +"_FINBILLHDR A JOIN " + plant +"_VENDMST V ON V.VENDNO=A.VENDNO WHERE A.PLANT='"+ plant+"'" + sCondition);			           
	                     
				            if (ht.get("JOBNUM") != null) {
		       				   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  PONO=A.PONO)");
		       			    }
							if (ht.get("ITEM") != null) {
			       				   sql.append(" AND A.ID IN (SELECT BILLHDRID from [" + plant +"_FINBILLDET] WHERE ITEM ='" + ht.get("ITEM") + "')");
			       			}
				        
	   				if (ht.get("PONO") != null) {
	  					  sql.append(" AND A.PONO = '" + ht.get("PONO") + "'");
	  				    }
	   				if (ht.get("STATUS") != null) {
	  					  sql.append(" AND A.BILL_STATUS = '" + ht.get("STATUS") + "'");
	  				    }
	   				if (ht.get("REFERENCE") != null) {
	  					  sql.append(" AND A.REFERENCE_NUMBER = '" + ht.get("REFERENCE") + "'");
	  				    }
	   				if (ht.get("GRNO") != null) {
	  					  sql.append(" AND A.GRNO = '" + ht.get("GRNO") + "'");
	  				    }
	   				if (ht.get("BILL") != null) {
	  					  sql.append(" AND A.BILL = '" + ht.get("BILL") + "'");
	  				    }
	   				if (ht.get("ORDERTYPE") != null) {
	 					sql.append(" AND ORDERTYPE = '" + ht.get("ORDERTYPE") + "'");
	 				}
	  				    
	   				/*if (ht.get("ORDERTYPE") != null) {
	  					   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  PONO=A.PONO)");
	   				}*/
	  				   
	  				   
	  				  arrList = billDao.selectForReport(sql.toString(), htData, extraCon);
	       
		
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
			}
			return arrList;
		}
	 
	 public List getBillHdrById(Hashtable ht) throws Exception {
			List billHdrList = new ArrayList();
			try {
				billDao.setmLogger(mLogger);
				billHdrList = billDao.getBillHdrById(ht);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return billHdrList;
	 }
	 
	 public List getBillDetByHdrId(Hashtable ht) throws Exception {
			List billDetList = new ArrayList();
			try {
				billDao.setmLogger(mLogger);
				billDetList = billDao.getBillDetByHdrId(ht);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return billDetList;
	 }

	 public ArrayList getBillPaymentSummaryView(Hashtable ht, String afrmDate,
				String atoDate, String plant, String vendname) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",dtCondStr="",extraCon="";
			StringBuffer sql;
			 Hashtable htData = new Hashtable();
			try {
				billDao.setmLogger(mLogger);

		        		dtCondStr ="and ISNULL(A.PAYMENT_DATE,'')<>'' AND CAST((SUBSTRING(A.PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(a.PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(a.PAYMENT_DATE, 1, 2)) AS date)";
		        		extraCon= "order by A.ID desc,CAST((SUBSTRING(A.PAYMENT_DATE, 7, 4) + SUBSTRING(A.PAYMENT_DATE, 4, 2) + SUBSTRING(A.PAYMENT_DATE, 1, 2)) AS date) desc ";    			        
				       
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
			           
				           if (vendname.length()>0){
				        	   vendname = StrUtils.InsertQuotes(vendname);
	                 	sCondition = sCondition + " AND VNAME LIKE '%"+vendname+"%' " ;
	                  } 
				          
				           sql = new StringBuffer("select A.ID as ID,PAYMENT_DATE,PAYMENT_MODE,A.PAID_THROUGH,A.VENDNO,ISNULL(A.ACCOUNT_NAME,'') as ACCOUNT_NAME,ISNULL(VNAME,'') as VNAME,ISNULL((select SUM(UM.BALANCE) from " + plant +"_FINPAYMENTDET UM where UM.PAYHDRID=A.ID and UM.LNNO =0 ),0) as UNUSEDAMOUNT,");
				           sql.append(" ISNULL(CURRENCYID,'') CURRENCYID,ISNULL((SELECT TOP 1 B.CURRENCYUSEQT FROM " + plant +"_FINPAYMENTDET B WHERE B.PAYHDRID=A.ID),1) CURRENCYUSEQT,");
				           sql.append(" REFERENCE,cast(AMOUNTPAID as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,cast((AMOUNTPAID-(ISNULL((select SUM(p.AMOUNT) from " + plant +"_FINPAYMENTDET p where p.PAYHDRID=A.ID and p.LNNO !=0 ),0))) as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as BALANCE_DUE");
				           sql.append(" from " + plant +"_FINPAYMENTHDR A LEFT JOIN " + plant +"_VENDMST V ON V.VENDNO=A.VENDNO WHERE A.PLANT='"+ plant+"'" + sCondition);
				           
				           if (ht.get("REFERENCE") != null) {
			  					  sql.append(" AND A.REFERENCE = '" + ht.get("REFERENCE") + "'");
			  				    }
	  				   
	  				  arrList = billDao.selectForReport(sql.toString(), htData, extraCon);
	       
		
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getBillPaymentSummary:", e);
			}
			return arrList;
		}
	 public ArrayList getGRNOToBillSummaryView(Hashtable ht, String afrmDate,
				String atoDate, String plant, String vendname) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",dtCondStr="",extraCon="";
			StringBuffer sql;
			 Hashtable htData = new Hashtable();
			try {
				billDao.setmLogger(mLogger);

		        		dtCondStr ="and ISNULL(A.CRAT,'')<>'' AND CAST((SUBSTRING(A.CRAT, 1, 4) + '-' + SUBSTRING(a.CRAT, 5, 2) + '-' + SUBSTRING(a.CRAT, 7, 2)) AS date)";
		        		extraCon= "order by A.ID DESC,CAST((SUBSTRING(A.CRAT, 1, 4) + '-' + SUBSTRING(a.CRAT, 5, 2) + '-' + SUBSTRING(a.CRAT, 7, 2)) AS date) DESC ";
				       
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
			           
				           if (vendname.length()>0){
				        	   vendname = StrUtils.InsertQuotes(vendname);
//	                 	sCondition = sCondition + " AND VNAME LIKE '%"+vendname+"%' " ;
	                 	sCondition= sCondition+" AND VENDNO in (select VENDNO from "+plant+"_VENDMST where VNAME like '"+vendname+"%')";
	                  } 
				          
				          /* sql = new StringBuffer("select A.ID as ID,A.PONO,GRNO,A.VENDNO,ISNULL(VNAME,'') as VNAME,(SUBSTRING(A.CRAT, 7, 2) + '/' + SUBSTRING(A.CRAT, 5, 2) + '/' + SUBSTRING(A.CRAT, 1, 4)) as CRAT,");				           
				           sql.append(" A.STATUS,cast(((((AMOUNT+P.SHIPPINGCOST)*P.INBOUND_GST)/100)+(AMOUNT+P.SHIPPINGCOST)) as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,ISNULL(QTY,0) as QTY,");
				           sql.append(" ISNULL( (SELECT SUM(D.QTY) FROM [" + plant +"_FINGRNOTOBILL] B LEFT JOIN [" + plant +"_FINBILLHDR] C ");
		        		   sql.append("ON B.VENDNO = C.VENDNO AND B.PONO =  C.PONO AND B.GRNO = C.GRNO ");
		        		   sql.append("LEFT JOIN [" + plant +"_FINBILLDET] D ON C.ID = D.BILLHDRID ");
		        		   sql.append("WHERE B.VENDNO = A.VENDNO AND B.PONO = A.PONO AND B.GRNO = A.GRNO),0) AS BILLEDQTY,");
		        		   sql.append("ISNULL(( SELECT SUM(C.RETURN_QTY) FROM [" + plant +"_FINGRNOTOBILL] B LEFT JOIN [" + plant +"_FINPORETURN] C ");
        				   sql.append("ON B.VENDNO = C.VENDNO AND B.PONO = C.PONO AND B.GRNO = C.GRNO");
        				   sql.append(" WHERE B.VENDNO = A.VENDNO AND B.PONO = A.PONO AND B.GRNO = A.GRNO),0) AS RETURNEDQTY");
				           sql.append(" from " + plant +"_FINGRNOTOBILL A JOIN " + plant +"_VENDMST V ON V.VENDNO=A.VENDNO JOIN " + plant +"_POHDR P ON P.PONO=A.PONO WHERE A.PLANT='"+ plant+"'" + sCondition);*/			           
	                     
				           sql = new StringBuffer("select A.ID as ID,A.PONO,GRNO,A.VENDNO,ISNULL((select ISNULL(VNAME,'')from " + plant +"_VENDMST V where V.VENDNO=A.VENDNO),'') as VNAME,(SUBSTRING(A.CRAT, 7, 2) + '/' + SUBSTRING(A.CRAT, 5, 2) + '/' + SUBSTRING(A.CRAT, 1, 4)) as CRAT,");				           
				           sql.append(" A.STATUS,cast(((((AMOUNT+(ISNULL((select TOP 1 ISNULL(P.SHIPPINGCOST,0) from " + plant +"_POHDR P where P.PONO=A.PONO),0))) *(ISNULL((select TOP 1 ISNULL(P.INBOUND_GST,0) from " + plant +"_POHDR P where P.PONO=A.PONO),0)))/100)+(AMOUNT+(ISNULL((select TOP 1 ISNULL(P.SHIPPINGCOST,0) from " + plant +"_POHDR P where P.PONO=A.PONO),0)))) as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,ISNULL(QTY,0) as QTY,");
				           sql.append(" ISNULL( (SELECT SUM(D.QTY) FROM [" + plant +"_FINGRNOTOBILL] B LEFT JOIN [" + plant +"_FINBILLHDR] C ");
		        		   sql.append("ON B.VENDNO = C.VENDNO AND B.PONO =  C.PONO AND B.GRNO = C.GRNO ");
		        		   sql.append("LEFT JOIN [" + plant +"_FINBILLDET] D ON C.ID = D.BILLHDRID ");
		        		   sql.append("WHERE B.VENDNO = A.VENDNO AND B.PONO = A.PONO AND B.GRNO = A.GRNO),0) AS BILLEDQTY,");
		        		   sql.append("ISNULL(( SELECT SUM(C.RETURN_QTY) FROM [" + plant +"_FINGRNOTOBILL] B LEFT JOIN [" + plant +"_FINPORETURN] C ");
        				   sql.append("ON B.VENDNO = C.VENDNO AND B.PONO = C.PONO AND B.GRNO = C.GRNO");
        				   sql.append(" WHERE B.VENDNO = A.VENDNO AND B.PONO = A.PONO AND B.GRNO = A.GRNO),0) AS RETURNEDQTY");
				           sql.append(" from " + plant +"_FINGRNOTOBILL A  WHERE A.PLANT='"+ plant+"'" + sCondition);
				           
				           if (ht.get("PONO") != null) {
			  					  sql.append(" AND A.PONO = '" + ht.get("PONO") + "'");
			  				    }
						   if (ht.get("GRNO") != null) {
			  					  sql.append(" AND GRNO = '" + ht.get("GRNO") + "'");
			  				    }
						   if (ht.get("STATUS") != null) {
			  					  sql.append(" AND A.STATUS = '" + ht.get("STATUS") + "'");
			  				    }
				           
	  				  arrList = billDao.selectForReport(sql.toString(), htData, extraCon);
	       
		
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getGRNOToBillSummary:", e);
			}
			return arrList;
		}
	 
	 public boolean  InsertTempOrderPayment(String plant,String afrmDate,String atoDate, String order,String custname)
				throws Exception {
//		OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		boolean insertflag  = false;
		boolean isExistsPaymentDetails = false;
		boolean isExiststemp = false;
//		String sql = "";
		CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
//		ArrayList arrCust = new ArrayList();
			
			try {
//				Hashtable htCond = new Hashtable();
				String searchCond ="",dtCondStr="",sCustCode="", billdtCondStr="", billsearchCond ="", invoicesearchCond ="";//,extraCon="",invoicedtCondStr="";				   
				
				dtCondStr = "  AND CAST((SUBSTRING(PAYMENT_DT,7,4) + '-' + SUBSTRING(PAYMENT_DT,4,2) + '-' + SUBSTRING(PAYMENT_DT,1,2)) AS date) ";
				billdtCondStr = " AND CAST((SUBSTRING(A.CRAT,1,4) + '-' + SUBSTRING(A.CRAT,5,2) + '-' + SUBSTRING(A.CRAT,7,2)) AS date) "; 
				
				if(custname.length()>0){
	            	if(order.equalsIgnoreCase("INVOICE")){
	            		sCustCode = customerBeanDAO.getCustomerCode(plant, custname);
	            		invoicesearchCond = invoicesearchCond + " AND (   CNAME = '"+custname+"') ";
	            	}
	            	else if(order.equalsIgnoreCase("BILL")){
	            		sCustCode = customerBeanDAO.getVendorCode(plant, custname);
	            		billsearchCond = billsearchCond + " AND (   VENDNO = '"+custname+"') ";	            		
	            	}
	            	
	            	searchCond =searchCond + " AND (   CustCode = '"+sCustCode+"') ";
	            	
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
//		        		boolean deleteflag = 
		        		new AgeingDAO().deletetemporderpayment(htValues);
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
				String searchCond ="",dtCondStr="",searchCondord="";//,extraCon="",dtCondpmt="",searchCondpmt="";
				   
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
		        {	
		        	sql = ""+"SELECT distinct custname,custcode,ISNULL(PAY_IN_DAYS,'')pmtdays from "
							+"["+plant+"_DOHDR]A,["+plant+"_CUSTMST]B WHERE A.PLANT='"+plant+"' AND A.CustCode=B.CUSTNO"+searchCondord;
							
			        }else{
			         sql = ""+"SELECT distinct custname,custcode,ISNULL(PAY_IN_DAYS,'')pmtdays from "
								+"["+plant+"_POHDR]A,["+plant+"_VENDMST]B WHERE A.PLANT='"+plant+"' AND A.CustCode=B.VENDNO"+searchCondord;
			        }
				orderPayDao.setmLogger(mLogger);
				al = orderPayDao.selectForReport(sql, htCond, "");
			} catch (Exception e) {
				this.mLogger.Exception(e.getMessage());
				throw e;
			}

			return al;
		}
	 
	 public ArrayList getPaymentStatementDetails(String plant,String afrmDate,String atoDate, String order,String custcode,String currencyid)
				throws Exception {
		 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
			String sql = "";
			ArrayList al = null;
			try {
				Hashtable htCond = new Hashtable();
				String searchCond ="",dtCondStr="",extraCon="";
				               
		            if(custcode.length()>0){
		            	searchCond =searchCond + " AND (   CustCode = '"+custcode+"') ";
		            }
		            if(currencyid.length()>0){
		            	searchCond =searchCond + " AND (   Currencyid = '"+currencyid+"') ";
		            }

		            dtCondStr = "  AND CAST((SUBSTRING(PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PAYMENT_DT, 4, 2) + '-' + SUBSTRING(PAYMENT_DT, 1, 2)) AS date)";
		       	   if (afrmDate.length() > 0) {
						 searchCond = searchCond + dtCondStr + "  >= '" 
								+ afrmDate
								+ "'  ";
					
						if (atoDate.length() > 0) {
							searchCond = searchCond +dtCondStr+ " <= '" 
							+ atoDate
							+ "'  ";
							
						}
					 } else {
						if (atoDate.length() > 0) {
							searchCond = searchCond +dtCondStr+ " <= '" 
							+ atoDate
							+ "'  ";
							
						}
					 }   

		       	  extraCon = "ORDER BY rowid ";
		           
		        if(order.equalsIgnoreCase("INVOICE"))
		        {	
		        	sql = "" +"SELECT case when ISNULL(PAYMENT_REFNO,'')='' then ORDNO else ORDNO+'Pmtrefno'+PAYMENT_REFNO end[ORDNO],"
		        			 +"ORDERNAME,PAYMENT_DT as TRANDATE,CustCode,"  
		        			 +"case when ORDERTYPE='INVOICE' and ISNULL(PAYMENT_MODE,'')='' then AMOUNT_PAID else -AMOUNT_PAID " 
		        			 +"end[AMOUNT], "
		        			 +"BALANCE =  COALESCE("
		        			 +"("
		        			 +"SELECT SUM(ROUND(AMOUNT_PAID,2))"
		        			 +"FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]  AS I "
		        			 +"WHERE I.ROWID <= PMT.ROWID AND CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + "
		        			 +"SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <= " 
		        			 +"CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' +" 
		        			 +"SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) "+searchCond+" and PMT.CustCode=I.CustCode and ORDERTYPE='INVOICE' " 
		        			 +"and ISNULL(PAYMENT_MODE,'')=''), 0" 
		        			 +" )"
		        			 +"-"
		        			 +" COALESCE("
		        			 +"("
		        			 +"SELECT SUM(ROUND(AMOUNT_PAID,2))"
		        			 +"FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]  AS I "
		        			 +"WHERE I.ROWID <= PMT.ROWID AND CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + "
		        			 +"SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <= " 
		        			 +"CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' + "
		        			 +"SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) "+searchCond+" and PMT.CustCode=I.CustCode and ORDERTYPE='INVOICE' " 
		        			 +" and "
		        			 +"ISNULL(PAYMENT_MODE,'')<>''), 0"
		        			 +")"
		        			 +"FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]  AS PMT "
		        			 +"where PLANT = '"+plant+"' AND "
		        			 +"ORDERTYPE='INVOICE'" +searchCond+extraCon;
		        }else{
		        	sql = "" +"SELECT case when ISNULL(PAYMENT_REFNO,'')='' then ORDNO else ORDNO+'Pmtrefno'+PAYMENT_REFNO end[ORDNO],"
		       			 +"ORDERNAME,PAYMENT_DT as TRANDATE,CustCode,"  
		       			 +"case when ORDERTYPE='BILL' and ISNULL(PAYMENT_MODE,'')='' then AMOUNT_PAID else -AMOUNT_PAID " 
		       			 +"end[AMOUNT], "
		       			 +"BALANCE =  COALESCE("
		       			 +"("
		       			 +"SELECT SUM(ROUND(AMOUNT_PAID,2))"
		       			 +"FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]  AS I "
		       			 +"WHERE I.ROWID <= PMT.ROWID AND CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + "
		       			 +"SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <= " 
		       			 +"CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' +" 
		       			 +"SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) "+searchCond+" and PMT.CustCode=I.CustCode and ORDERTYPE='BILL' " 
		       			 +"and ISNULL(PAYMENT_MODE,'')=''), 0" 
		       			 +" )"
		       			 +"-"
		       			 +" COALESCE("
		       			 +"("
		       			 +"SELECT SUM(ROUND(AMOUNT_PAID,2))"
		       			 +"FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]  AS I "
		       			 +"WHERE I.ROWID <= PMT.ROWID AND CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + "
		       			 +"SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <= " 
		       			 +"CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' + "
		       			 +"SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) "+searchCond+" and PMT.CustCode=I.CustCode and ORDERTYPE='BILL' " 
		       			 +" and  "
		       			 +"ISNULL(PAYMENT_MODE,'')<>''), 0"
		       			 +")"
		       			 +"FROM ["+plant+"_FIN_TEMP_ORDER_PAYMENT_DET]  AS PMT "
		       			 +"where PLANT = '"+plant+"' AND "
		       			 +"ORDERTYPE='BILL'" +searchCond+extraCon;
		        }
				orderPayDao.setmLogger(mLogger);
				al = orderPayDao.selectForReport(sql, htCond, "");
			} catch (Exception e) {
				this.mLogger.Exception(e.getMessage());
				throw e;
			}

			return al;
		}
	 
	 public ArrayList getBillNoForAutoSuggestion(Hashtable ht, String plant) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",vendNo="",extraCon="";
			StringBuffer sql;
			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			Hashtable htData = new Hashtable();
			try {
				billDao.setmLogger(mLogger);
				          
	           sql = new StringBuffer("SELECT ID, VENDNO, BILL, PONO, BILL_DATE, DUE_DATE, PAYMENT_TERMS,ISNULL(BILL_TYPE,'') AS BILL_TYPE, "
	           		+ "ITEM_RATES, DISCOUNT, DISCOUNT_TYPE, DISCOUNT_ACCOUNT,SHIPPINGCOST, ADJUSTMENT, "
	           		+ "SUB_TOTAL, TOTAL_AMOUNT, BILL_STATUS, NOTE, GRNO, ADJUSTMENT_LABEL, SHIPMENT_CODE, "
	           		+ "REFERENCE_NUMBER FROM ["+plant+"_FINBILLHDR] WHERE PLANT='"+ plant+"'AND BILL_STATUS!='CANCELLED'" + sCondition);		           
             
	           if (ht.get("PONO") != null && ht.get("PONO") != "") {
				  sql.append(" AND PONO = '" + ht.get("PONO") + "'");
			    }
	           
	           if (ht.get("GRNO") != null && ht.get("GRNO") != "") {
					  sql.append(" AND GRNO = '" + ht.get("GRNO") + "'");
				    }
			   if (ht.get("VENDNO") != null && ht.get("VENDNO") != "") {
				   vendNo = customerBeanDAO.getVendorCode(plant, (String)ht.get("VENDNO"));
				  sql.append(" AND VENDNO = '" + vendNo + "'");
			    }
			   if (ht.get("BILL") != null && ht.get("BILL") != "") {
					  sql.append(" AND BILL LIKE '%"+ht.get("BILL")+"%'");
			    }
			   
			   extraCon = "ORDER BY ID DESC";
			   
			   arrList = billDao.selectForReport(sql.toString(), htData, extraCon);
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
			}
			return arrList;
		}
	 
	 public ArrayList getBillNoForAutoSuggestionForPoReturn(Hashtable ht, String plant) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",vendNo="",extraCon="";
			StringBuffer sql;
			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			Hashtable htData = new Hashtable();
			try {
				billDao.setmLogger(mLogger);
				          
	           sql = new StringBuffer("SELECT ID, VENDNO, BILL, PONO, BILL_DATE, DUE_DATE, PAYMENT_TERMS,ISNULL(BILL_TYPE,'') AS BILL_TYPE, "
	           		+ "ITEM_RATES, DISCOUNT, DISCOUNT_TYPE, DISCOUNT_ACCOUNT,SHIPPINGCOST, ADJUSTMENT, "
	           		+ "SUB_TOTAL, TOTAL_AMOUNT, BILL_STATUS, NOTE, GRNO, ADJUSTMENT_LABEL, SHIPMENT_CODE, "
	           		+ "REFERENCE_NUMBER FROM ["+plant+"_FINBILLHDR] WHERE PLANT='"+ plant+"'AND BILL_STATUS!='CANCELLED' AND BILL_TYPE!='NON INVENTORY'" + sCondition);		           
          
	           if (ht.get("PONO") != null && ht.get("PONO") != "") {
				  sql.append(" AND PONO = '" + ht.get("PONO") + "'");
			    }
	           
	           if (ht.get("GRNO") != null && ht.get("GRNO") != "") {
					  sql.append(" AND GRNO = '" + ht.get("GRNO") + "'");
				    }
			   if (ht.get("VENDNO") != null && ht.get("VENDNO") != "") {
				   vendNo = customerBeanDAO.getVendorCode(plant, (String)ht.get("VENDNO"));
				  sql.append(" AND VENDNO = '" + vendNo + "'");
			    }
			   if (ht.get("BILL") != null && ht.get("BILL") != "") {
					  sql.append(" AND BILL LIKE '%"+ht.get("BILL")+"%'");
			    }
			   
			   extraCon = "ORDER BY ID DESC";
			   
			   arrList = billDao.selectForReport(sql.toString(), htData, extraCon);
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
			}
			return arrList;
		}
	 
	 public ArrayList getBillDetailsSView(Hashtable ht, String afrmDate, String atoDate, String plant, String custname) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",dtCondStr="",extraCon="";
			StringBuffer sql;
			 Hashtable htData = new Hashtable();
			try {
				billDao.setmLogger(mLogger);

		        		dtCondStr ="and ISNULL(A.BILL_DATE,'')<>'' AND CAST((SUBSTRING(A.BILL_DATE, 7, 4) + '-' + SUBSTRING(a.BILL_DATE, 4, 2) + '-' + SUBSTRING(a.BILL_DATE, 1, 2)) AS date)";
		        		extraCon= "order by A.ID desc,CAST((SUBSTRING(A.BILL_DATE, 7, 4) + SUBSTRING(A.BILL_DATE, 4, 2) + SUBSTRING(A.BILL_DATE, 1, 2)) AS date) desc";    			        
				       
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
	                 	custname = StrUtils.InsertQuotes(custname);
	                 	sCondition = sCondition + " AND VNAME LIKE '%"+custname+"%' " ;

	                  } 
				          
				           sql = new StringBuffer("SELECT ID,BILL_DATE,BILL,PONO,VENDNO,VNAME,BILL_STATUS,DUE_DATE,CURRENCYID,");
				           sql.append(" cast(TOTAL_AMOUNT * ISNULL((SELECT CURRENCYUSEQT FROM [" + plant +"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID),1) as DECIMAL(20,5)) as BASE_TOTAL_AMOUNT,");
				           sql.append(" cast((cast(TOTAL_AMOUNT * ISNULL((SELECT CURRENCYUSEQT FROM [" + plant +"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID),1) as DECIMAL(20,5))");
				           sql.append(" -(ISNULL((select ISNULL(SUM(AMOUNT),0) FROM " + plant +"_FINPAYMENTDET WHERE PONO= A.PONO AND TYPE= 'REGULAR' AND PLANT= 'TEST' AND BILLHDRID = A.ID),0)))as DECIMAL(20,5)) as BASE_BALANCE_DUE,");
				           sql.append(" TOTAL_AMOUNT,BALANCE_DUE,AVAILABLE_CREDIT FROM (");
				           sql.append(" select A.ID as ID,BILL_DATE,BILL,A.PONO,A.VENDNO,");
				           sql.append(" ISNULL(VNAME,'') as VNAME,ISNULL((select CURRENCYID from " + plant +"_POHDR p where p.PONO=A.PONO ),'') as CURRENCYID,");
				           sql.append(" BILL_STATUS,DUE_DATE,cast(TOTAL_AMOUNT as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,cast((TOTAL_AMOUNT-(select ISNULL(SUM(AMOUNT),0) FROM " + plant+"_FINPAYMENTDET WHERE PONO= A.PONO AND TYPE= 'REGULAR' AND PLANT= '"+plant+"' AND BILLHDRID = A.ID)) as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as BALANCE_DUE,");				           
				           sql.append("cast((select ISNULL(SUM(BALANCE),0) from " + plant +"_FINPAYMENTDET where pono=A.PONO AND TYPE='ADVANCE' AND PONO <> '') as DECIMAL(20,5)) as AVAILABLE_CREDIT");
				           sql.append(" from " + plant +"_FINBILLHDR A JOIN " + plant +"_VENDMST V ON V.VENDNO=A.VENDNO WHERE A.PLANT='"+ plant+"'" + sCondition);			           
	                     
				            if (ht.get("JOBNUM") != null) {
		       				   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  PONO=A.PONO)");
		       			    }
							if (ht.get("ITEM") != null) {
			       				   sql.append(" AND A.ID IN (SELECT BILLHDRID from [" + plant +"_FINBILLDET] WHERE ITEM ='" + ht.get("ITEM") + "')");
			       			}
				        
	   				if (ht.get("PONO") != null) {
	  					  sql.append(" AND A.PONO = '" + ht.get("PONO") + "'");
	  				    }
	   				if (ht.get("STATUS") != null) {
	  					  sql.append(" AND A.BILL_STATUS = '" + ht.get("STATUS") + "'");
	  				    }
	   				if (ht.get("REFERENCE") != null) {
	  					  sql.append(" AND A.REFERENCE_NUMBER = '" + ht.get("REFERENCE") + "'");
	  				    }
	   				if (ht.get("GRNO") != null) {
	  					  sql.append(" AND A.GRNO = '" + ht.get("GRNO") + "'");
	  				    }
	   				if (ht.get("BILL") != null) {
	  					  sql.append(" AND A.BILL = '" + ht.get("BILL") + "'");
	  				    }
	  				   
	   				sql.append(") A");
	  				  arrList = billDao.selectForReport(sql.toString(), htData, extraCon);
	       
		
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
			}
			return arrList;
		}
	 
	 public ArrayList getPaymentsMadeSummaryView(Hashtable ht, String afrmDate,
				String atoDate, String plant, String vendname) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",dtCondStr="",extraCon="";
			StringBuffer sql;
			 Hashtable htData = new Hashtable();
			try {
				billDao.setmLogger(mLogger);

		        		dtCondStr ="and ISNULL(A.PAYMENT_DATE,'')<>'' AND CAST((SUBSTRING(A.PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(a.PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(a.PAYMENT_DATE, 1, 2)) AS date)";
		        		extraCon= "order by A.ID desc,CAST((SUBSTRING(A.PAYMENT_DATE, 7, 4) + SUBSTRING(A.PAYMENT_DATE, 4, 2) + SUBSTRING(A.PAYMENT_DATE, 1, 2)) AS date) desc ";    			        
				       
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
			           
				           if (vendname.length()>0){
				        	   vendname = StrUtils.InsertQuotes(vendname);
	                 	sCondition = sCondition + " AND VNAME LIKE '%"+vendname+"%' " ;
	                  } 
				          
				           sql = new StringBuffer("select A.ID as ID,PAYMENT_DATE,PAYMENT_MODE,A.PAID_THROUGH,A.VENDNO,ISNULL(VNAME,'') as VNAME,");				           
				           sql.append(" REFERENCE,cast(AMOUNTPAID as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,cast((AMOUNTPAID-(ISNULL((select SUM(p.AMOUNT) from " + plant +"_FINPAYMENTDET p where p.PAYHDRID=A.ID ),0))) as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as BALANCE_DUE");
				           sql.append(", ISNULL((STUFF((SELECT ',' + CONVERT(VARCHAR,ISNULL((SELECT BILL FROM " + plant +"_FINBILLHDR WHERE ID = BILLHDRID),''))");
				           sql.append(" FROM [" + plant +"_FINPAYMENTDET] WHERE (PAYHDRID = A.ID) AND BILLHDRID <> 0 ");
				           sql.append(" FOR XML PATH ('')), 1, 1, '')),'') AS BILL, NOTE");
				           sql.append(" from " + plant +"_FINPAYMENTHDR A JOIN " + plant +"_VENDMST V ON V.VENDNO=A.VENDNO WHERE A.PLANT='"+ plant+"'" + sCondition);
				           
				           if (ht.get("REFERENCE") != null) {
				        	   sql.append(" AND A.REFERENCE = '" + ht.get("REFERENCE") + "'");
				           }
	  				  arrList = billDao.selectForReport(sql.toString(), htData, extraCon);		
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,"Exception :repportUtil :: getBillPaymentSummary:", e);
			}
			return arrList;
		}
	 
	 public ArrayList getVendorBalances(Hashtable ht, String afrmDate, String atoDate, String plant, String custname) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",sCondition2 = "",sCondition3 = "",dtCondStr="",dtCondStr2="",extraCon="";
			StringBuffer sql;
			 Hashtable htData = new Hashtable();
			try {
				billDao.setmLogger(mLogger);

		        		dtCondStr =" AND ISNULL(A.BILL_DATE,'')<>'' AND CAST((SUBSTRING(A.BILL_DATE, 7, 4) + '-' + SUBSTRING(a.BILL_DATE, 4, 2) + '-' + SUBSTRING(a.BILL_DATE, 1, 2)) AS date)";
		        		
		        		dtCondStr2 =" AND ISNULL(A.PAYMENT_DATE,'')<>'' AND CAST((SUBSTRING(A.PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(a.PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(a.PAYMENT_DATE, 1, 2)) AS date)";
		        		
		        		extraCon= " ORDER BY A.VENDNO";    			        
				       
						   if (afrmDate.length() > 0) {
			              	sCondition = sCondition + dtCondStr + " >= '" + afrmDate + "' ";
			              	sCondition2 = sCondition2 + dtCondStr2 + " >= '" + afrmDate + "' ";
			  				if (atoDate.length() > 0) {
			  					sCondition = sCondition +dtCondStr+ " <= '" + atoDate + "' ";
			  					sCondition2 = sCondition2 +dtCondStr2+ " <= '" + atoDate + "' ";
			  				}
			  			  } else {
			  				if (atoDate.length() > 0) {
			  					sCondition = sCondition +dtCondStr+ " <= '" + atoDate + "' ";
			  					sCondition2 = sCondition2 +dtCondStr2+ " <= '" + atoDate + "' ";
			  				}
			  		     	}   
			           
				           if (custname.length()>0){
				        	   custname = StrUtils.InsertQuotes(custname);
				        	   sCondition3 = " WHERE A.VENDNO = '"+custname+"'";
				           } 
				          
				           sql = new StringBuffer("SELECT (SELECT VNAME FROM [" + plant +"_VENDMST] WHERE VENDNO = A.VENDNO) VNAME,A.BALANCE_DUE,B.EXCESS_PAYMENT, CAST((A.BALANCE_DUE - B.EXCESS_PAYMENT) AS DECIMAL(20,5)) AS BALANCE FROM ");
				           sql.append(" (SELECT VENDNO,SUM(BALANCE_DUE) AS BALANCE_DUE FROM (");
				           sql.append(" SELECT A.VENDNO, cast(((TOTAL_AMOUNT)-(select ISNULL(SUM(AMOUNT),0) FROM [" + plant +"_FINPAYMENTDET] WHERE PONO= A.PONO AND TYPE= 'REGULAR' ");
				           sql.append(" AND PLANT= '" + plant +"' AND BILLHDRID = A.ID)) as DECIMAL(20,5)) as BALANCE_DUE ");
				           sql.append(" FROM [" + plant +"_FINBILLHDR] A  WHERE A.PLANT='" + plant +"' AND A.BILL_STATUS <> 'Draft' " + sCondition);
				           sql.append(" ) A GROUP BY A.VENDNO) A JOIN ");
				           sql.append(" ( SELECT A.VENDNO, SUM(BALANCE_DUE) AS EXCESS_PAYMENT FROM ( ");
				           sql.append(" SELECT A.VENDNO, cast((AMOUNTPAID-(ISNULL((SELECT SUM(p.AMOUNT) FROM [" + plant +"_FINPAYMENTDET] P where P.PAYHDRID=A.ID AND P.TYPE <> 'ADVANCE'),0))) as DECIMAL(20,5)) as BALANCE_DUE");
				           sql.append(" FROM [" + plant +"_FINPAYMENTHDR] A WHERE A.PLANT='" + plant +"'"+sCondition2);
				           sql.append(" )A GROUP BY A.VENDNO) B ON A.VENDNO = B.VENDNO " + sCondition3);
	  				  arrList = billDao.selectForReport(sql.toString(), htData, extraCon);
	       	
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
			}
			return arrList;
		}
	 
	 public ArrayList getTotalPurchaseForDashBoard(String plant, String fromDate, String toDate) throws Exception {
		ArrayList al = null;
		Hashtable htData = new Hashtable();
		String extraCon="";
		try {
			String aQuery = "SELECT ISNULL(SUM(ISNULL(QTY, 0) * (ISNULL(COST, 0))),0) AS TOTAL_PURCHASE FROM " 
					+ "[" + plant + "_" + "FINBILLHDR] A JOIN [" + plant + "_FINBILLDET] B ON A.ID = B.BILLHDRID "
					+ "WHERE CONVERT(DATETIME, BILL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "'";
			al = billDao.selectForReport(aQuery.toString(), htData, extraCon);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	 
	 public ArrayList getTotalPurchaseForDashBoardFromJournal(String plant, String fromDate, String toDate) throws Exception {
			ArrayList al = null;
			Hashtable htData = new Hashtable();
			String extraCon="";
			try {
				String aQuery = "SELECT (SUM(ISNULL(DEBITS, 0))-SUM(ISNULL(CREDITS, 0)))AS TOTAL_PURCHASE FROM " + plant + "_FINJOURNALDET A JOIN ["+plant+"_FINJOURNALHDR] B ON A.JOURNALHDRID = B.ID "
						+ "WHERE A.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='10')"
						+ "AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "'";
				System.out.println("-----------------tiles--------------------------");
				System.out.println(aQuery);
				System.out.println("-------------------------------------------");
				al = billDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
		}
	 
	 public ArrayList getTotalPurchaseSummaryByBill(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
			ArrayList al = null;
			String aQuery = "";
			Hashtable htData = new Hashtable();
			String extraCon="";
			try {
				if(period.equalsIgnoreCase("Last 30 days")) {
					aQuery ="with cte as" + 
							"  (" + 
							"  select CONVERT(DATE,getdate()) as   n" + 
							"  union all" + 
							"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)> DATEADD(month, -1, getdate())" + 
							"  )";
				} else if(period.equalsIgnoreCase("This Month")) {
					aQuery ="with cte as" + 
					" (" + 
					" select EOMONTH(GETDATE()) as   n" + 
					" union all" + 
					" select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0)" + 
					" )";
				} else if(period.equalsIgnoreCase("This quarter")) {
					aQuery ="with cte as" + 
							" (" + 
							" SELECT " + 
							" CASE "+
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " + 
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) END AS n, " +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN datename(month,dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN datename(month,dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN datename(month,dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" ELSE datename(month,dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) END AS m " +
							" union all " +
							" SELECT  dateadd(MONTH,-1,n)," +
							" datename(month,dateadd(MONTH,-1,n)) as m FROM cte " +
							" WHERE dateadd(dd,-1,n)>" +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,3,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,6,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,9,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) END " +
							"  )";
				} else if(period.equalsIgnoreCase("This year")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)) as m" + 
							"  union all" + 
							"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, 0, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
							"  )";
				}  else if(period.equalsIgnoreCase("Last month")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select EOMONTH(GETDATE(),-1) as   n" + 
							"  union all" + 
							"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= dateadd(dd,1,EOMONTH(GETDATE(),-2)) " + 
							"  )";
				}  else if(period.equalsIgnoreCase("Last quarter")) {
					aQuery ="with cte as" + 
							" (" + 
							" SELECT " + 
							" CASE "+
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) " + 
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 2, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 5, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 8, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 8, GETDATE()), 0)) END AS n, " +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN datename(month,dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1))) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN datename(month,dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN datename(month,dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" ELSE datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1))) END AS m " +
							" union all " +
							" SELECT  dateadd(MONTH,-1,n)," +
							" datename(month,dateadd(MONTH,-1,n)) as m FROM cte " +
							" WHERE dateadd(dd,-1,n)>" +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,-2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,3,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,6,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,-2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) END " +
							"  )";
				}  else if(period.equalsIgnoreCase("Last year")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) as m" + 
							"  union all" + 
							"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, -1, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
							"  )";
				}
				
				if(period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month")) {
					aQuery += " SELECT CONVERT(nvarchar, n, 107) BILL_DATE,ISNULL(TOTAL_COST,0) as TOTAL_COST from cte a left join (" + 
							" SELECT CONVERT(DATE, A.BILL_DATE, 103) AS CDATE,SUM(CAST((COST * QTY) AS DECIMAL(18,"+numberOfDecimal+")))" + 
							" AS TOTAL_COST FROM   ["+plant+"_FINBILLHDR] A JOIN ["+plant+"_FINBILLDET] B ON A.ID = B.BILLHDRID " + 
							" WHERE CONVERT(DATETIME, BILL_DATE, 103) between '"+fromDate+"' and '"+toDate+"'" + 
							" GROUP BY CONVERT(DATE, A.BILL_DATE, 103) )" + 
							" b on a.n = b.CDATE order by n";
				}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
					aQuery += " SELECT CONVERT(nvarchar, n, 107) BILL_DATE,ISNULL(TOTAL_COST,0) as TOTAL_COST from cte a left join (" + 
							" SELECT datename(month, (CONVERT(DATE, A.BILL_DATE, 103))) AS CDATE,SUM(CAST((COST * QTY) AS DECIMAL(18,"+numberOfDecimal+")))" + 
							" AS TOTAL_COST FROM   ["+plant+"_FINBILLHDR] A JOIN ["+plant+"_FINBILLDET]B ON A.ID = B.BILLHDRID " + 
							" WHERE CONVERT(DATETIME, BILL_DATE, 103) between '"+fromDate+"' and '"+toDate+"'" + 
							" GROUP BY datename(month, (CONVERT(DATE, A.BILL_DATE, 103))) )" + 
							" b on a.m = b.CDATE order by n";
				}
				
				
				
				al = billDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
	}
	 
	 public ArrayList getTotalPurchaseSummaryByBillFromJournal(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
			ArrayList al = null;
			String aQuery = "";
			Hashtable htData = new Hashtable();
			String extraCon="";
			try {

				if(period.equalsIgnoreCase("Last 30 days")) {
					aQuery ="with cte as" + 
							"  (" + 
							"  select CONVERT(DATE,getdate()) as   n" + 
							"  union all" + 
							"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)> DATEADD(month, -1, getdate())" + 
							"  )";
				}else if(period.equalsIgnoreCase("Last 15 days")) {
					aQuery ="with cte as" + 
							"  (" + 
							"  select CONVERT(DATE,getdate()) as   n" + 
							"  union all" + 
							"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)> DATEADD(dd, -15, getdate())" + 
							"  )";
				}else if(period.equalsIgnoreCase("Last 7 days")) {
					aQuery ="with cte as" + 
							"  (" + 
							"  select CONVERT(DATE,getdate()) as   n" + 
							"  union all" + 
							"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)> DATEADD(dd, -7, getdate())" + 
							"  )";
				} else if(period.equalsIgnoreCase("This week")) {
					aQuery ="with cte as ( SELECT CONVERT(DATE,  DATEADD(day, 7-(DATEPART(dw, getdate())), getdate())) AS n union all select  dateadd(DAY,-1,n) from cte " + 
							"where dateadd(dd,-1,n)>= DATEADD(dd, 1-(DATEPART(dw, getdate())), getdate())  ) ";
				} else if(period.equalsIgnoreCase("This Month")) {
					aQuery ="with cte as" + 
					" (" + 
					" select EOMONTH(GETDATE()) as   n" + 
					" union all" + 
					" select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0)" + 
					" )";
				} else if(period.equalsIgnoreCase("This quarter")) {
					aQuery ="with cte as" + 
							" (" + 
							" SELECT " + 
							" CASE "+
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " + 
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) END AS n, " +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN datename(month,dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN datename(month,dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN datename(month,dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" ELSE datename(month,dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) END AS m " +
							" union all " +
							" SELECT  dateadd(MONTH,-1,n)," +
							" datename(month,dateadd(MONTH,-1,n)) as m FROM cte " +
							" WHERE dateadd(dd,-1,n)>" +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,3,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,6,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,9,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) END " +
							"  )";
				} else if(period.equalsIgnoreCase("This year")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)) as m" + 
							"  union all" + 
							"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, 0, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
							"  )";
				}  else if(period.equalsIgnoreCase("Last month")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select EOMONTH(GETDATE(),-1) as   n" + 
							"  union all" + 
							"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= dateadd(dd,1,EOMONTH(GETDATE(),-2)) " + 
							"  )";
				}  else if(period.equalsIgnoreCase("Last quarter")) {
					aQuery ="with cte as" + 
							" (" + 
							" SELECT " + 
							" CASE "+
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) " + 
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 2, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 5, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 8, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 8, GETDATE()), 0)) END AS n, " +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN datename(month,dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1))) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN datename(month,dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN datename(month,dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" ELSE datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1))) END AS m " +
							" union all " +
							" SELECT  dateadd(MONTH,-1,n)," +
							" datename(month,dateadd(MONTH,-1,n)) as m FROM cte " +
							" WHERE dateadd(dd,-1,n)>" +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,-2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,3,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,6,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,-2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) END " +
							"  )";
				}  else if(period.equalsIgnoreCase("Last year")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) as m" + 
							"  union all" + 
							"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, -1, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
							"  )";
				}
				
				if(period.equalsIgnoreCase("Today") || period.equalsIgnoreCase("yesterday")) {
					aQuery ="SELECT CONVERT(nvarchar, (CONVERT(DATE, B.JOURNAL_DATE, 103)), 107) AS BILL_DATE,CAST(((SUM(ISNULL(C.DEBITS, 0)))-(SUM(ISNULL(C.CREDITS, 0)))) AS DECIMAL(18,"+numberOfDecimal+")) AS TOTAL_COST FROM "+plant+"_FINJOURNALDET C " + 
							"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID WHERE C.ACCOUNT_ID IN (SELECT ID  FROM "+plant+"_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='3' " + 
							"AND ACCOUNTDETAILTYPE = '6') AND (B.TRANSACTION_TYPE = 'BILL' OR B.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES') AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) " + 
							"BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY B.JOURNAL_DATE";
				}else if(period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month") || period.equalsIgnoreCase("Last 7 days") || period.equalsIgnoreCase("This week") || period.equalsIgnoreCase("Last 15 days")) {
					aQuery += " SELECT CONVERT(nvarchar, n, 107) BILL_DATE,ISNULL(TOTAL_COST,0) as TOTAL_COST from cte a left join ("+
							"SELECT CONVERT(DATE, B.JOURNAL_DATE, 103) AS CDATE,CAST(((SUM(ISNULL(C.DEBITS, 0)))-(SUM(ISNULL(C.CREDITS, 0)))) AS DECIMAL(18,"+numberOfDecimal+")) AS TOTAL_COST FROM " + plant + "_FINJOURNALDET C "+
							"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID " + 
							"WHERE C.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='3' AND ACCOUNTDETAILTYPE = '6')" + 
							" AND (B.TRANSACTION_TYPE = 'BILL' OR B.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES') AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY B.JOURNAL_DATE )" + 
							" j on a.n = j.CDATE order by a.n";
				}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
					aQuery += " SELECT CONVERT(nvarchar, n, 107) BILL_DATE,ISNULL(TOTAL_COST,0) as TOTAL_COST from cte a left join (" + 
							"SELECT datename(month, (CONVERT(DATE, B.JOURNAL_DATE, 103))) AS CDATE,CAST(((SUM(ISNULL(C.DEBITS, 0)))-(SUM(ISNULL(C.CREDITS, 0)))) AS DECIMAL(18,"+numberOfDecimal+")) AS TOTAL_COST FROM " + plant + "_FINJOURNALDET C "+
							"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID " + 
							"WHERE C.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='3' AND ACCOUNTDETAILTYPE = '6')" + 
							"AND (B.TRANSACTION_TYPE = 'BILL' OR B.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES') AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY datename(month, (CONVERT(DATE, B.JOURNAL_DATE, 103))) )" + 
							" j on a.m = j.CDATE order by a.n";
				}
				
				System.out.println("------------------chart--------"+period+"-----------------");
				System.out.println(aQuery);
				System.out.println("-------------------------------------------");
				al = billDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
	}
	 
	/* public ArrayList getTotalPurchaseSummaryByBillFromJournal(String plant, String fromDate, String toDate) throws Exception {
			ArrayList al = null;
			Hashtable htData = new Hashtable();
			String extraCon="";
			try {
				String aQuery = "SELECT B.JOURNAL_DATE AS BILL_DATE,(SUM(ISNULL(DEBITS, 0))-SUM(ISNULL(CREDITS, 0)))AS TOTAL_COST FROM " + plant + "_FINJOURNALDET A JOIN ["+plant+"_FINJOURNALHDR] B ON A.JOURNALHDRID = B.ID "
						+ "WHERE A.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='10')"
						+ "AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY B.JOURNAL_DATE";
				al = billDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
	}*/
	 
	 public ArrayList getTotalExpenseForDashboard(String plant, String fromDate, String toDate, String numberOfDecimal) throws Exception {
			ArrayList al = null;
			Hashtable htData = new Hashtable();
			String extraCon = "";
			try {
				String aQuery = "SELECT CAST(SUM(AMOUNTPAID) as DECIMAL(18,"+numberOfDecimal+")) as TOTAL_AMOUNT,ACCOUNTDETAILTYPE FROM ( " 
						+ " SELECT AMOUNTPAID,PAYMENT_DATE, "
						+" (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
						+" ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = A.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE "
						+" FROM ["+plant+"_FINPAYMENTHDR] A "
						+" WHERE CONVERT(DATETIME, PAYMENT_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
						+" AND ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=11) "
						+" UNION ALL "
						+" SELECT AMOUNTRECEIVED,RECEIVE_DATE, "
						+" (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
						+" ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = A.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE "
						+" from ["+plant+"_FINRECEIVEHDR] A "
						+" WHERE  CONVERT(DATETIME, RECEIVE_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
						+" AND ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=11) "
						+" UNION ALL "
						+" SELECT ((CASE WHEN (LEN(SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE), 1)) > 0 ) THEN "
						+" AMOUNT + CAST(AMOUNT * SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE)+1, 1) AS float)/100  ELSE 0.0 END)) TOTAL_AMOUNT, "
						+" A.EXPENSES_DATE, "
						+" (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
						+" ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = B.EXPENSES_ACCOUNT) AS ACCOUNTDETAILTYPE FROM "
						+" ["+plant+"_FINEXPENSESHDR] A JOIN ["+plant+"_FINEXPENSESDET] B ON A.ID = B.EXPENSESHDRID "
						+" WHERE CONVERT(DATETIME, EXPENSES_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
						+" AND B.EXPENSES_ACCOUNT IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=11) "
						+" UNION ALL "
						+" SELECT ((CASE WHEN (LEN(SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE), 1)) > 0 ) THEN "
						+" AMOUNT + CAST(AMOUNT * SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE)+1, 1) AS float)/100  ELSE 0.0 END)) TOTAL_AMOUNT, "
						+" A.CREDIT_DATE, "
						+" (SELECT DISTINCT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
						+" ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = B.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE FROM "
						+" ["+plant+"_FINVENDORCREDITNOTEHDR] A JOIN ["+plant+"_FINVENDORCREDITNOTEDET] B ON A.ID = B.HDRID "
						+" WHERE CONVERT(DATETIME, CREDIT_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
						+" AND B.ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=11) "
						+" ) A GROUP BY ACCOUNTDETAILTYPE";
				al = billDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
	}
	 
	 public ArrayList getTotalExpenseForDashboardFromJournal(String plant, String fromDate, String toDate, String numberOfDecimal) throws Exception {
			ArrayList al = null;
			Hashtable htData = new Hashtable();
			String extraCon = "";
			try {
				String aQuery = "select ca.ACCOUNT_NAME,CAST((SUM(ISNULL(jd.DEBITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_AMOUNT from ["+plant+"_FINJOURNALDET] jd inner join ["+plant+"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+plant+"_FINACCOUNTDETAILTYPE] ad on ad.ID=ca.ACCOUNTDETAILTYPE inner join ["+plant+"_FINJOURNALHDR] hdr on hdr.ID=jd.JOURNALHDRID where ca.ACCOUNTTYPE in (11) AND hdr.TRANSACTION_TYPE != 'BILL_REVERSAL'  AND CONVERT(DATETIME, hdr.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' AND (hdr.TRANSACTION_TYPE = 'EXPENSE' OR hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' OR hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' OR hdr.TRANSACTION_TYPE = 'BILL' OR hdr.TRANSACTION_TYPE = 'PURCHASEPAYMENT' OR hdr.TRANSACTION_TYPE = 'SALESPAYMENT' OR hdr.TRANSACTION_TYPE = 'INVOICE' ) GROUP BY ca.ACCOUNT_NAME ";
				al = billDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
	}
	 
	 public ArrayList getTotalPaymentMadeForDashboardFromJournal(String plant, String fromDate, String toDate, String numberOfDecimal) throws Exception {
			ArrayList al = null;
			Hashtable htData = new Hashtable();
			String extraCon = "";
			try {
				String aQuery = "select ca.ACCOUNT_NAME,CAST((SUM(ISNULL(jd.CREDITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_AMOUNT from ["+plant+"_FINJOURNALDET] jd inner join ["+plant+"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+plant+"_FINACCOUNTDETAILTYPE] ad on ad.ID=ca.ACCOUNTDETAILTYPE inner join ["+plant+"_FINJOURNALHDR] hdr on hdr.ID=jd.JOURNALHDRID where ca.ACCOUNTDETAILTYPE in (8,9) AND (hdr.TRANSACTION_TYPE = 'EXPENSE' OR hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' OR hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' OR hdr.TRANSACTION_TYPE = 'BILL' OR hdr.TRANSACTION_TYPE = 'PURCHASEPAYMENT' OR hdr.TRANSACTION_TYPE = 'SALESPAYMENT' OR hdr.TRANSACTION_TYPE = 'INVOICE' OR hdr.TRANSACTION_TYPE = 'EXPENSE PAID' ) AND CONVERT(DATETIME, hdr.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY ca.ACCOUNT_NAME ";
				al = billDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
	}
	 
	 public ArrayList getTotalPaymentReceiptForDashboardFromJournal(String plant, String fromDate, String toDate, String numberOfDecimal) throws Exception {
			ArrayList al = null;
			Hashtable htData = new Hashtable();
			String extraCon = "";
			try {
				String aQuery = "select ca.ACCOUNT_NAME,CAST((SUM(ISNULL(jd.DEBITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_AMOUNT from ["+plant+"_FINJOURNALDET] jd inner join ["+plant+"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+plant+"_FINACCOUNTDETAILTYPE] ad on ad.ID=ca.ACCOUNTDETAILTYPE inner join ["+plant+"_FINJOURNALHDR] hdr on hdr.ID=jd.JOURNALHDRID where ca.ACCOUNTDETAILTYPE in (8,9) AND (hdr.TRANSACTION_TYPE = 'EXPENSE' OR hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' OR hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' OR hdr.TRANSACTION_TYPE = 'BILL' OR hdr.TRANSACTION_TYPE = 'PURCHASEPAYMENT' OR hdr.TRANSACTION_TYPE = 'SALESPAYMENT' OR hdr.TRANSACTION_TYPE = 'INVOICE' OR hdr.TRANSACTION_TYPE = 'EXPENSE PAID' ) AND CONVERT(DATETIME, hdr.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY ca.ACCOUNT_NAME ";
				al = billDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
	}
	 
	 public ArrayList getTotalExpenseSummaryByBill(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
			ArrayList al = null;
			String aQuery = "";
			Hashtable htData = new Hashtable();
			String extraCon="";
			try {
				if(period.equalsIgnoreCase("Last 30 days")) {
					aQuery ="with cte as" + 
							"  (" + 
							"  select CONVERT(DATE,getdate()) as   n" + 
							"  union all" + 
							"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)> DATEADD(month, -1, getdate())" + 
							"  )";
				} else if(period.equalsIgnoreCase("This Month")) {
					aQuery ="with cte as" + 
					" (" + 
					" select EOMONTH(GETDATE()) as   n" + 
					" union all" + 
					" select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0)" + 
					" )";
				} else if(period.equalsIgnoreCase("This quarter")) {
					aQuery ="with cte as" + 
							" (" + 
							" SELECT " + 
							" CASE "+
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " + 
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) END AS n, " +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN datename(month,dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN datename(month,dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN datename(month,dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" ELSE datename(month,dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) END AS m " +
							" union all " +
							" SELECT  dateadd(MONTH,-1,n)," +
							" datename(month,dateadd(MONTH,-1,n)) as m FROM cte " +
							" WHERE dateadd(dd,-1,n)>" +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,3,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,6,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,9,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) END " +
							"  )";
				} else if(period.equalsIgnoreCase("This year")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)) as m" + 
							"  union all" + 
							"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, 0, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
							"  )";
				}  else if(period.equalsIgnoreCase("Last month")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select EOMONTH(GETDATE(),-1) as   n" + 
							"  union all" + 
							"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= dateadd(dd,1,EOMONTH(GETDATE(),-2)) " + 
							"  )";
				}  else if(period.equalsIgnoreCase("Last quarter")) {
					aQuery ="with cte as" + 
							" (" + 
							" SELECT " + 
							" CASE "+
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) " + 
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 2, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 5, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 8, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 8, GETDATE()), 0)) END AS n, " +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN datename(month,dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1))) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN datename(month,dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN datename(month,dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" ELSE datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1))) END AS m " +
							" union all " +
							" SELECT  dateadd(MONTH,-1,n)," +
							" datename(month,dateadd(MONTH,-1,n)) as m FROM cte " +
							" WHERE dateadd(dd,-1,n)>" +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,-2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,3,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,6,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,-2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) END " +
							"  )";
				}  else if(period.equalsIgnoreCase("Last year")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) as m" + 
							"  union all" + 
							"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, -1, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
							"  )";
				}
				
				if(period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month")) {
					aQuery += " SELECT CONVERT(nvarchar, n, 107) BILL_DATE,ISNULL(TOTAL_AMOUNT,0) as TOTAL_COST from cte a left join (" + 
							"SELECT CONVERT(DATE, A.PAYMENT_DATE, 103) AS CDATE,CAST(SUM(AMOUNTPAID) as DECIMAL(18,"+numberOfDecimal+")) as TOTAL_AMOUNT FROM ( " 
							+ " SELECT AMOUNTPAID,PAYMENT_DATE, "
							+" (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
							+" ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = A.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE "
							+" FROM ["+plant+"_FINPAYMENTHDR] A "
							+" WHERE CONVERT(DATETIME, PAYMENT_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
							+" AND ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=11) "
							+" UNION ALL "
							+" SELECT AMOUNTRECEIVED,RECEIVE_DATE, "
							+" (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
							+" ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = A.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE "
							+" from ["+plant+"_FINRECEIVEHDR] A "
							+" WHERE  CONVERT(DATETIME, RECEIVE_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
							+" AND ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=11) "
							+" UNION ALL "
							+" SELECT ((CASE WHEN (LEN(SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE), 1)) > 0 ) THEN "
							+" AMOUNT + CAST(AMOUNT * SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE)+1, 1) AS float)/100  ELSE 0.0 END)) TOTAL_AMOUNT, "
							+" A.EXPENSES_DATE, "
							+" (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
							+" ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = B.EXPENSES_ACCOUNT) AS ACCOUNTDETAILTYPE FROM "
							+" ["+plant+"_FINEXPENSESHDR] A JOIN ["+plant+"_FINEXPENSESDET] B ON A.ID = B.EXPENSESHDRID "
							+" WHERE CONVERT(DATETIME, EXPENSES_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
							+" AND B.EXPENSES_ACCOUNT IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=11) "
							+" UNION ALL "
							+" SELECT ((CASE WHEN (LEN(SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE), 1)) > 0 ) THEN "
							+" AMOUNT + CAST(AMOUNT * SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE)+1, 1) AS float)/100  ELSE 0.0 END)) TOTAL_AMOUNT, "
							+" A.CREDIT_DATE, "
							+" (SELECT DISTINCT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
							+" ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = B.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE FROM "
							+" ["+plant+"_FINVENDORCREDITNOTEHDR] A JOIN ["+plant+"_FINVENDORCREDITNOTEDET] B ON A.ID = B.HDRID "
							+" WHERE CONVERT(DATETIME, CREDIT_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
							+" AND B.ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=11) "
							+" ) A GROUP BY CONVERT(DATE, A.PAYMENT_DATE, 103)"+
							 " ) B ON A.N = B.CDATE ORDER BY N";
				}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
					aQuery += " SELECT CONVERT(nvarchar, n, 107) BILL_DATE,ISNULL(TOTAL_AMOUNT,0) as TOTAL_COST from cte a left join (" + 
							"SELECT datename(month, (CONVERT(DATE, A.PAYMENT_DATE, 103))) AS CDATE,CAST(SUM(AMOUNTPAID) as DECIMAL(18,"+numberOfDecimal+")) as TOTAL_AMOUNT FROM ( " 
							+ " SELECT AMOUNTPAID,PAYMENT_DATE, "
							+" (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
							+" ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = A.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE "
							+" FROM ["+plant+"_FINPAYMENTHDR] A "
							+" WHERE CONVERT(DATETIME, PAYMENT_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
							+" AND ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=11) "
							+" UNION ALL "
							+" SELECT AMOUNTRECEIVED,RECEIVE_DATE, "
							+" (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
							+" ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = A.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE "
							+" from ["+plant+"_FINRECEIVEHDR] A "
							+" WHERE  CONVERT(DATETIME, RECEIVE_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
							+" AND ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=11) "
							+" UNION ALL "
							+" SELECT ((CASE WHEN (LEN(SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE), 1)) > 0 ) THEN "
							+" AMOUNT + CAST(AMOUNT * SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE)+1, 1) AS float)/100  ELSE 0.0 END)) TOTAL_AMOUNT, "
							+" A.EXPENSES_DATE, "
							+" (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
							+" ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = B.EXPENSES_ACCOUNT) AS ACCOUNTDETAILTYPE FROM "
							+" ["+plant+"_FINEXPENSESHDR] A JOIN ["+plant+"_FINEXPENSESDET] B ON A.ID = B.EXPENSESHDRID "
							+" WHERE CONVERT(DATETIME, EXPENSES_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
							+" AND B.EXPENSES_ACCOUNT IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=11) "
							+" UNION ALL "
							+" SELECT ((CASE WHEN (LEN(SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE), 1)) > 0 ) THEN "
							+" AMOUNT + CAST(AMOUNT * SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE)+1, 1) AS float)/100  ELSE 0.0 END)) TOTAL_AMOUNT, "
							+" A.CREDIT_DATE, "
							+" (SELECT DISTINCT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
							+" ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = B.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE FROM "
							+" ["+plant+"_FINVENDORCREDITNOTEHDR] A JOIN ["+plant+"_FINVENDORCREDITNOTEDET] B ON A.ID = B.HDRID "
							+" WHERE CONVERT(DATETIME, CREDIT_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
							+" AND B.ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=11) "
							+" ) A GROUP BY datename(month, (CONVERT(DATE, A.PAYMENT_DATE, 103)))"+
							 " ) B ON A.M = B.CDATE ORDER BY N";
				}
				al = billDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
	}
	 
	 
	 public ArrayList getTotalExpenseSummaryByBillFromJourny(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
			ArrayList al = null;
			String aQuery = "";
			Hashtable htData = new Hashtable();
			String extraCon="";
			try {
				if(period.equalsIgnoreCase("Last 30 days")) {
					aQuery ="with cte as" + 
							"  (" + 
							"  select CONVERT(DATE,getdate()) as   n" + 
							"  union all" + 
							"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)> DATEADD(month, -1, getdate())" + 
							"  )";
				} else if(period.equalsIgnoreCase("This Month")) {
					aQuery ="with cte as" + 
					" (" + 
					" select EOMONTH(GETDATE()) as   n" + 
					" union all" + 
					" select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0)" + 
					" )";
				} else if(period.equalsIgnoreCase("This quarter")) {
					aQuery ="with cte as" + 
							" (" + 
							" SELECT " + 
							" CASE "+
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " + 
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) END AS n, " +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN datename(month,dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN datename(month,dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN datename(month,dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" ELSE datename(month,dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) END AS m " +
							" union all " +
							" SELECT  dateadd(MONTH,-1,n)," +
							" datename(month,dateadd(MONTH,-1,n)) as m FROM cte " +
							" WHERE dateadd(dd,-1,n)>" +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,3,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,6,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,9,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) END " +
							"  )";
				} else if(period.equalsIgnoreCase("This year")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)) as m" + 
							"  union all" + 
							"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, 0, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
							"  )";
				}  else if(period.equalsIgnoreCase("Last month")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select EOMONTH(GETDATE(),-1) as   n" + 
							"  union all" + 
							"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= dateadd(dd,1,EOMONTH(GETDATE(),-2)) " + 
							"  )";
				}  else if(period.equalsIgnoreCase("Last quarter")) {
					aQuery ="with cte as" + 
							" (" + 
							" SELECT " + 
							" CASE "+
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) " + 
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 2, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 5, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 8, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 8, GETDATE()), 0)) END AS n, " +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN datename(month,dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1))) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN datename(month,dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN datename(month,dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" ELSE datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1))) END AS m " +
							" union all " +
							" SELECT  dateadd(MONTH,-1,n)," +
							" datename(month,dateadd(MONTH,-1,n)) as m FROM cte " +
							" WHERE dateadd(dd,-1,n)>" +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,-2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,3,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,6,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,-2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) END " +
							"  )";
				}  else if(period.equalsIgnoreCase("Last year")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) as m" + 
							"  union all" + 
							"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, -1, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
							"  )";
				}
				
				if(period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month")) {
					aQuery += " SELECT CONVERT(nvarchar, n, 107) BILL_DATE,ISNULL(TOTAL_COST,0) as TOTAL_COST from cte a left join ("+
							"SELECT CONVERT(DATE, B.JOURNAL_DATE, 103) AS CDATE,CAST((SUM(ISNULL(C.DEBITS, 0))) AS DECIMAL(25,"+numberOfDecimal+"))AS TOTAL_COST FROM " + plant + "_FINJOURNALDET C "+
							"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID " + 
							"WHERE C.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='11')" + 
							"AND B.TRANSACTION_TYPE != 'BILL_REVERSAL' AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY B.JOURNAL_DATE )" + 
							" j on a.n = j.CDATE order by a.n";
				}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
					aQuery += " SELECT CONVERT(nvarchar, n, 107) BILL_DATE,ISNULL(TOTAL_COST,0) as TOTAL_COST from cte a left join (" + 
							"SELECT datename(month, (CONVERT(DATE, B.JOURNAL_DATE, 103))) AS CDATE,CAST((SUM(ISNULL(C.DEBITS, 0))) AS DECIMAL(25,"+numberOfDecimal+"))AS TOTAL_COST FROM " + plant + "_FINJOURNALDET C "+
							"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID " + 
							"WHERE C.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='11')" + 
							"AND B.TRANSACTION_TYPE != 'BILL_REVERSAL' AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY datename(month, (CONVERT(DATE, B.JOURNAL_DATE, 103))) )" + 
							" j on a.m = j.CDATE order by a.n";
				}
				al = billDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
	}
	 
	 public ArrayList getTotalPaymentMadeSummaryFromJourny(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
			ArrayList al = null;
			String aQuery = "";
			Hashtable htData = new Hashtable();
			String extraCon="";
			try {
				if(period.equalsIgnoreCase("Last 30 days")) {
					aQuery ="with cte as" + 
							"  (" + 
							"  select CONVERT(DATE,getdate()) as   n" + 
							"  union all" + 
							"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)> DATEADD(month, -1, getdate())" + 
							"  )";
				} else if(period.equalsIgnoreCase("This Month")) {
					aQuery ="with cte as" + 
					" (" + 
					" select EOMONTH(GETDATE()) as   n" + 
					" union all" + 
					" select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0)" + 
					" )";
				} else if(period.equalsIgnoreCase("This quarter")) {
					aQuery ="with cte as" + 
							" (" + 
							" SELECT " + 
							" CASE "+
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " + 
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) END AS n, " +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN datename(month,dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN datename(month,dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN datename(month,dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" ELSE datename(month,dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) END AS m " +
							" union all " +
							" SELECT  dateadd(MONTH,-1,n)," +
							" datename(month,dateadd(MONTH,-1,n)) as m FROM cte " +
							" WHERE dateadd(dd,-1,n)>" +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,3,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,6,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,9,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) END " +
							"  )";
				} else if(period.equalsIgnoreCase("This year")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)) as m" + 
							"  union all" + 
							"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, 0, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
							"  )";
				}  else if(period.equalsIgnoreCase("Last month")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select EOMONTH(GETDATE(),-1) as   n" + 
							"  union all" + 
							"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= dateadd(dd,1,EOMONTH(GETDATE(),-2)) " + 
							"  )";
				}  else if(period.equalsIgnoreCase("Last quarter")) {
					aQuery ="with cte as" + 
							" (" + 
							" SELECT " + 
							" CASE "+
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) " + 
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 2, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 5, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 8, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 8, GETDATE()), 0)) END AS n, " +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN datename(month,dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1))) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN datename(month,dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN datename(month,dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" ELSE datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1))) END AS m " +
							" union all " +
							" SELECT  dateadd(MONTH,-1,n)," +
							" datename(month,dateadd(MONTH,-1,n)) as m FROM cte " +
							" WHERE dateadd(dd,-1,n)>" +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,-2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,3,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,6,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,-2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) END " +
							"  )";
				}  else if(period.equalsIgnoreCase("Last year")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) as m" + 
							"  union all" + 
							"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, -1, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
							"  )";
				}
				
				if(period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month")) {
					aQuery += " SELECT CONVERT(nvarchar, n, 107) BILL_DATE,ISNULL(TOTAL_COST,0) as TOTAL_COST from cte a left join ("+
							"SELECT CONVERT(DATE, B.JOURNAL_DATE, 103) AS CDATE,CAST((SUM(ISNULL(C.CREDITS, 0))) AS DECIMAL(25,"+numberOfDecimal+"))AS TOTAL_COST FROM " + plant + "_FINJOURNALDET C "+
							"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID " + 
							"WHERE C.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE  ACCOUNTDETAILTYPE IN (8,9))" + 
							"AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY B.JOURNAL_DATE )" + 
							" j on a.n = j.CDATE order by a.n";
				}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
					aQuery += " SELECT CONVERT(nvarchar, n, 107) BILL_DATE,ISNULL(TOTAL_COST,0) as TOTAL_COST from cte a left join (" + 
							"SELECT datename(month, (CONVERT(DATE, B.JOURNAL_DATE, 103))) AS CDATE,CAST((SUM(ISNULL(C.CREDITS, 0))) AS DECIMAL(25,"+numberOfDecimal+"))AS TOTAL_COST FROM " + plant + "_FINJOURNALDET C "+
							"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID " + 
							"WHERE C.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE  ACCOUNTDETAILTYPE IN (8,9))" + 
							"AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY datename(month, (CONVERT(DATE, B.JOURNAL_DATE, 103))) )" + 
							" j on a.m = j.CDATE order by a.n";
				}
				al = billDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
	}
	 
	 public ArrayList getTotalPaymentReceiptSummaryFromJourny(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
			ArrayList al = null;
			String aQuery = "";
			Hashtable htData = new Hashtable();
			String extraCon="";
			try {
				if(period.equalsIgnoreCase("Last 30 days")) {
					aQuery ="with cte as" + 
							"  (" + 
							"  select CONVERT(DATE,getdate()) as   n" + 
							"  union all" + 
							"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)> DATEADD(month, -1, getdate())" + 
							"  )";
				} else if(period.equalsIgnoreCase("This Month")) {
					aQuery ="with cte as" + 
					" (" + 
					" select EOMONTH(GETDATE()) as   n" + 
					" union all" + 
					" select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0)" + 
					" )";
				} else if(period.equalsIgnoreCase("This quarter")) {
					aQuery ="with cte as" + 
							" (" + 
							" SELECT " + 
							" CASE "+
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " + 
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) END AS n, " +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN datename(month,dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN datename(month,dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN datename(month,dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" ELSE datename(month,dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) END AS m " +
							" union all " +
							" SELECT  dateadd(MONTH,-1,n)," +
							" datename(month,dateadd(MONTH,-1,n)) as m FROM cte " +
							" WHERE dateadd(dd,-1,n)>" +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,3,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,6,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,9,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) END " +
							"  )";
				} else if(period.equalsIgnoreCase("This year")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)) as m" + 
							"  union all" + 
							"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, 0, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
							"  )";
				}  else if(period.equalsIgnoreCase("Last month")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select EOMONTH(GETDATE(),-1) as   n" + 
							"  union all" + 
							"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= dateadd(dd,1,EOMONTH(GETDATE(),-2)) " + 
							"  )";
				}  else if(period.equalsIgnoreCase("Last quarter")) {
					aQuery ="with cte as" + 
							" (" + 
							" SELECT " + 
							" CASE "+
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) " + 
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 2, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 5, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 8, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,11,DATEADD(yy, DATEDIFF(yy, 8, GETDATE()), 0)) END AS n, " +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN datename(month,dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1))) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN datename(month,dateadd(MONTH,5,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN datename(month,dateadd(MONTH,8,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))) " +
							" ELSE datename(month,dateadd(MONTH,2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1))) END AS m " +
							" union all " +
							" SELECT  dateadd(MONTH,-1,n)," +
							" datename(month,dateadd(MONTH,-1,n)) as m FROM cte " +
							" WHERE dateadd(dd,-1,n)>" +
							" CASE " +
							" WHEN (DATEPART(q, GETDATE()) = 1) THEN dateadd(MONTH,-2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) " +
							" WHEN (DATEPART(q, GETDATE()) = 2) THEN dateadd(MONTH,0,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 3) THEN dateadd(MONTH,3,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" WHEN (DATEPART(q, GETDATE()) = 4) THEN dateadd(MONTH,6,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0)) " +
							" ELSE dateadd(MONTH,-2,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) END " +
							"  )";
				}  else if(period.equalsIgnoreCase("Last year")) {
					aQuery =" with cte as" + 
							"  (" + 
							"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) as m" + 
							"  union all" + 
							"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, -1, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
							"  )";
				}
				
				if(period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month")) {
					aQuery += " SELECT CONVERT(nvarchar, n, 107) BILL_DATE,ISNULL(TOTAL_COST,0) as TOTAL_COST from cte a left join ("+
							"SELECT CONVERT(DATE, B.JOURNAL_DATE, 103) AS CDATE,CAST((SUM(ISNULL(C.DEBITS, 0))) AS DECIMAL(25,"+numberOfDecimal+"))AS TOTAL_COST FROM " + plant + "_FINJOURNALDET C "+
							"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID " + 
							"WHERE C.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE  ACCOUNTDETAILTYPE IN (8,9))" + 
							"AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY B.JOURNAL_DATE )" + 
							" j on a.n = j.CDATE order by a.n";
				}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
					aQuery += " SELECT CONVERT(nvarchar, n, 107) BILL_DATE,ISNULL(TOTAL_COST,0) as TOTAL_COST from cte a left join (" + 
							"SELECT datename(month, (CONVERT(DATE, B.JOURNAL_DATE, 103))) AS CDATE,CAST((SUM(ISNULL(C.DEBITS, 0))) AS DECIMAL(25,"+numberOfDecimal+"))AS TOTAL_COST FROM " + plant + "_FINJOURNALDET C "+
							"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID " + 
							"WHERE C.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE  ACCOUNTDETAILTYPE IN (8,9))" + 
							"AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY datename(month, (CONVERT(DATE, B.JOURNAL_DATE, 103))) )" + 
							" j on a.m = j.CDATE order by a.n";
				}
				al = billDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
	}
	 
	 
	 public ArrayList getpurchaseDashboard(String plant, String afrmDate,String atoDate, String status, String custname,String numberOfDecimal) {
			ArrayList arrList = new ArrayList();
			String extraCon="";//,sCondition = "",dtCondStr=""
			StringBuffer sql;
			Hashtable htData = new Hashtable();
			try {
				billDao.setmLogger(mLogger);
		           
				/*sql = new StringBuffer("select A.ID as ID,BILL_DATE,BILL,A.PONO,ISNULL(GRNO,'') as GRNO,A.VENDNO,A.REFERENCE_NUMBER,JD.DEBITS, " + 
						"ISNULL((SELECT top 1 B.CURRENCYUSEQT FROM " + plant +"_FINBILLDET B WHERE B.BILLHDRID=A.ID),1) CURRENCYUSEQT, " + 
						"ISNULL(VNAME,'') as VNAME,ISNULL(A.CURRENCYID,'') as CURRENCYID, BILL_STATUS,DUE_DATE," + 
						"cast(A.TOTAL_AMOUNT as DECIMAL(20,5)) as TOTAL_AMOUNT,cast((A.TOTAL_AMOUNT-(select ISNULL(SUM(AMOUNT),0)" + 
						"FROM " + plant +"_FINPAYMENTDET WHERE PONO= A.PONO AND TYPE= 'REGULAR' AND PLANT= 'TEST' AND BILLHDRID = A.ID)) as DECIMAL(20,5)) as BALANCE_DUE," + 
						"cast((select ISNULL(SUM(BALANCE),0) from " + plant +"_FINPAYMENTDET where pono=A.PONO AND TYPE='ADVANCE' AND PONO <> '') as DECIMAL(20,5)) as AVAILABLE_CREDIT from " + 
						 plant +"_FINJOURNALDET JD JOIN [" + plant +"_FINJOURNALHDR] JH ON JD.JOURNALHDRID = JH.ID " + 
						"JOIN [" + plant +"_FINBILLHDR] A ON A.BILL = JH.TRANSACTION_ID JOIN " + plant +"_VENDMST V ON V.VENDNO=A.VENDNO WHERE A.PLANT='TEST' AND " + 
						"JD.ACCOUNT_ID IN (SELECT ID  FROM " + plant +"_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='3' AND ACCOUNTDETAILTYPE = '6') AND " + 
						"JH.TRANSACTION_TYPE = 'BILL' AND CONVERT(DATETIME, JH.JOURNAL_DATE, 103) " + 
						"BETWEEN '" + afrmDate + "' AND '" + atoDate + "'");      
				
				if (custname.length()>0){
                 	custname = StrUtils.InsertQuotes(custname);
                 	sql.append("  AND V.VNAME LIKE '%"+custname+"%' ");
                } 

	   		    if (status != null && !status.equalsIgnoreCase("")) {
	   		    	sql.append(" AND A.BILL_STATUS = '" + status + "'");	
	  			}
	   			
	   		    sql.append(" order by A.ID desc");*/
				
				sql = new StringBuffer("select hdr.JOURNAL_DATE,jd.ID,CAST(((ISNULL(jd.DEBITS, 0))-(ISNULL(jd.CREDITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_AMOUNT, " + 
						"CASE    " + 
						"WHEN hdr.TRANSACTION_TYPE = 'BILL' THEN ISNULL((SELECT VM.VNAME FROM " + plant +"_FINBILLHDR AS BI LEFT JOIN " + plant +"_VENDMST AS VM ON BI.VENDNO = VM.VENDNO WHERE BI.BILL = hdr.TRANSACTION_ID),'-')    " + 
						"WHEN hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' THEN ISNULL((SELECT VM.VNAME FROM " + plant +"_FINVENDORCREDITNOTEHDR AS SD LEFT JOIN " + plant +"_VENDMST AS VM ON SD.VENDNO = VM.VENDNO WHERE SD.ID = hdr.TRANSACTION_ID),'-')	" + 
						"ELSE '-' END AS NAME, " + 
						"CASE    " + 
						"WHEN hdr.TRANSACTION_TYPE = 'BILL' THEN ISNULL((SELECT BI.BILL FROM " + plant +"_FINBILLHDR AS BI WHERE BI.BILL = hdr.TRANSACTION_ID),'-')    " + 
						"WHEN hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' THEN ISNULL((SELECT SD.CREDITNOTE FROM " + plant +"_FINVENDORCREDITNOTEHDR AS SD WHERE SD.ID = hdr.TRANSACTION_ID),'-')	" + 
						"ELSE '-' END AS REFERENCE, " + 
						"CASE    " + 
						"WHEN hdr.TRANSACTION_TYPE = 'BILL' THEN ISNULL((SELECT BI.BILL_STATUS FROM " + plant +"_FINBILLHDR AS BI WHERE BI.BILL = hdr.TRANSACTION_ID),'-')    " + 
						"WHEN hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' THEN ISNULL((SELECT SD.CREDIT_STATUS FROM " + plant +"_FINVENDORCREDITNOTEHDR AS SD WHERE SD.ID = hdr.TRANSACTION_ID),'-')	  " + 
						"ELSE '-' END AS STATUS,CASE    " + 
						"WHEN hdr.TRANSACTION_TYPE = 'BILL' THEN ISNULL((SELECT BI.BILL FROM " + plant +"_FINBILLHDR AS BI WHERE BI.BILL = hdr.TRANSACTION_ID),'-')    " + 
						"WHEN hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' THEN ISNULL((SELECT SD.BILL FROM " + plant +"_FINVENDORCREDITNOTEHDR AS SD WHERE SD.ID = hdr.TRANSACTION_ID),'-')	  \r\n" + 
						"ELSE '-' END AS CONDITIONS " + 
						"from [" + plant +"_FINJOURNALDET] jd inner join [" + plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID " + 
						"inner join [" + plant +"_FINACCOUNTDETAILTYPE] ad on ad.ID=ca.ACCOUNTDETAILTYPE " + 
						"inner join [" + plant +"_FINJOURNALHDR] hdr on hdr.ID=jd.JOURNALHDRID where ca.ACCOUNTTYPE='3' AND ca.ACCOUNTDETAILTYPE = '6' " + 
						"AND (hdr.TRANSACTION_TYPE = 'BILL' OR hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES') " + 
						"AND CONVERT(DATETIME, hdr.JOURNAL_DATE, 103) BETWEEN '" + afrmDate + "' AND '" + atoDate + "' order by hdr.ID desc");
	  				   
	  			arrList = billDao.selectForReport(sql.toString(), htData, extraCon);
	       
		
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
			}
			return arrList;
		}
	 
	 	public String getCurrencyID(String plant, String bill) throws Exception {
			String currencyid = "";
			ArrayList arrList = new ArrayList();
			try {
				Hashtable ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("BILL", bill);
				StringBuffer sql = new StringBuffer("select ISNULL(CURRENCYID,'') as CURRENCYID from " + plant +"_FINBILLHDR  WHERE PLANT='"+ plant+"'");			           
				arrList = billDao.selectForReport(sql.toString(), ht, "");
				Map m=(Map)arrList.get(0);
				currencyid = (String) m.get("CURRENCYID");
			}catch (Exception e) {
				this.mLogger.exception(this.printLog,"unable to get currency id", e);
			}

			return currencyid;
		}
	 	
	 	public String getbygrno(String plant, String grno) throws Exception {
			String billtype = "";
			ArrayList arrList = new ArrayList();
			try {
				Hashtable ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("GRNO", grno);
				StringBuffer sql = new StringBuffer("select ISNULL(BILL_TYPE,'') as BILL_TYPE from " + plant +"_FINBILLHDR  WHERE PLANT='"+ plant+"'");			           
				arrList = billDao.selectForReport(sql.toString(), ht, "");
				if(arrList.size() > 0) {
					Map m=(Map)arrList.get(0);
					billtype = (String) m.get("BILL_TYPE");
				}else {
					billtype = "PURCHASE";
				}
			}catch (Exception e) {
				this.mLogger.exception(this.printLog,"unable to get bill type", e);
			}

			return billtype;
		}
	 	
	 	public String getbybillno(String plant, String billno) throws Exception {
			String billtype = "";
			ArrayList arrList = new ArrayList();
			try {
				Hashtable ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("BILL", billno);
				StringBuffer sql = new StringBuffer("select ISNULL(BILL_TYPE,'') as BILL_TYPE from " + plant +"_FINBILLHDR  WHERE PLANT='"+ plant+"'");			           
				arrList = billDao.selectForReport(sql.toString(), ht, "");
				if(arrList.size() > 0) {
					Map m=(Map)arrList.get(0);
					billtype = (String) m.get("BILL_TYPE");
				}else {
					billtype = "PURCHASE";
				}
			}catch (Exception e) {
				this.mLogger.exception(this.printLog,"unable to get bill type", e);
			}

			return billtype;
		}
}
