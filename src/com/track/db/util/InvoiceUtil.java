package com.track.db.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.DoDetDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.PlantMstDAO;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;
@SuppressWarnings({"rawtypes", "unchecked"})
public class InvoiceUtil {
	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.InvoiceUtil_PRINTPLANTMASTERLOG;
	private InvoiceDAO invoiceDao = new InvoiceDAO();
	InvoiceDAO _InvoiceDAO = null;
	
	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
		
	}
	
	public int addInvoiceHdr(Hashtable ht, String plant) {
		int invoiceHdrId = 0;
		try {
			invoiceHdrId = invoiceDao.addInvoiceHdr(ht, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return invoiceHdrId;		
	}
	
	public boolean addMultipleInvoiceDet(List<Hashtable<String, String>> invoiceDetInfoList, String plant){
		boolean flag = false;		
		try {
			flag = invoiceDao.addMultipleInvoiceDet(invoiceDetInfoList, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean addInvoiceAttachments(List<Hashtable<String, String>> invoiceAttachmentList, String plant){
		boolean flag = false;		
		try {
			flag = invoiceDao.addInvoiceAttachments(invoiceAttachmentList, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	  public ArrayList getInvoiceSummaryView(Hashtable ht, String afrmDate,
				String atoDate, String plant, String custname) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",dtCondStr="",extraCon="";
			StringBuffer sql;
			 Hashtable htData = new Hashtable();
			try {
				InvoiceDAO movHisDAO = new InvoiceDAO();
				movHisDAO.setmLogger(mLogger);

				dtCondStr ="and ISNULL(A.INVOICE_DATE,'')<>'' AND CAST((SUBSTRING(A.INVOICE_DATE, 7, 4) + '-' + SUBSTRING(a.INVOICE_DATE, 4, 2) + '-' + SUBSTRING(a.INVOICE_DATE, 1, 2)) AS date)";
	    		extraCon= "order by A.ID desc,CAST((SUBSTRING(A.INVOICE_DATE, 7, 4) + SUBSTRING(A.INVOICE_DATE, 4, 2) + SUBSTRING(A.INVOICE_DATE, 1, 2)) AS date) desc ";    			        
		       
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
	       	sCondition = sCondition + " AND CNAME LIKE '%"+custname+"%' " ;
	        } 
		            sql = new StringBuffer("select A.ID,INVOICE_DATE,INVOICE,ISNULL(A.GINO,'') AS GINO,ISNULL(A.ORDERTYPE,'') AS ORDERTYPE,A.DONO,ISNULL((select DELDATE from "+ plant+"_DOHDR p where p.DONO=A.DONO ),ISNULL(A.DELIVERY_DATE,'')) as DELDATE,A.CUSTNO,ISNULL(A.ISEXPENSE,0) as ISEXPENSE,");
		            sql.append(" ISNULL(CURRENCYID,'') as CURRENCYID,ISNULL((SELECT top 1 B.CURRENCYUSEQT FROM " + plant +"_FININVOICEDET B WHERE B.INVOICEHDRID=A.ID),1) CURRENCYUSEQT,");
		            sql.append(" ISNULL((select SUM(BALANCE) from " + plant +"_FINRECEIVEDET r where r.TYPE='ADVANCE' and r.DONO = A.DONO ),'') as creditbalance,");
		           sql.append(" ISNULL(CNAME,'') as CNAME,ISNULL((select CURRENCYID from " + plant +"_DOHDR p where p.DONO=A.DONO ),ISNULL(A.CURRENCYID,'')) as CURRENCYID,");
		           sql.append(" BILL_STATUS,DUE_DATE,cast(TOTAL_AMOUNT as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,cast((TOTAL_AMOUNT-(ISNULL((select SUM(AMOUNT) from " + plant +"_FINRECEIVEDET p where p.INVOICEHDRID=A.ID and p.TYPE='REGULAR'),0)))as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as BALANCE_DUE");
		           sql.append(" from " + plant +"_FININVOICEHDR A JOIN " + plant +"_CUSTMST V ON V.CUSTNO=A.CUSTNO WHERE A.PLANT='"+ plant+"'" + sCondition);	
				           
	               if (ht.get("ITEM") != null) {
       				   sql.append(" AND A.ID IN (SELECT INVOICEHDRID from [" + plant +"_FININVOICEDET] WHERE ITEM ='" + ht.get("ITEM") + "')");
       					}        
	 				if (ht.get("DONO") != null) {
						  sql.append(" AND A.DONO = '" + ht.get("DONO") + "'");
					    }
	 				if (ht.get("INVOICE") != null) {
						  sql.append(" AND INVOICE = '" + ht.get("INVOICE") + "'");
					    }
	 				if (ht.get("BILL_STATUS") != null) {
						  sql.append(" AND BILL_STATUS = '" + ht.get("BILL_STATUS") + "'");
					    }
	 				if (ht.get("PLNO") != null) {
	  					  sql.append(" AND (A.GINO!='' OR A.GINO is not null OR A.GINO in (select ISNULL(P.GINO,'') from "+plant+"_DNPLHDR P WHERE P.PACKINGLIST = '" + ht.get("PLNO") + "')) AND A.INVOICE in (select ISNULL(P.INVOICE,'') from "+plant+"_DNPLHDR P WHERE P.PACKINGLIST = '" + ht.get("PLNO") + "')");
	  					
	  				    }
	 				if (ht.get("EMPNO") != null) {
						  sql.append(" AND EMPNO = '" + ht.get("EMPNO") + "'");
					    }
	 				if (ht.get("ORDERTYPE") != null) {
	 					sql.append(" AND ORDERTYPE = '" + ht.get("ORDERTYPE") + "'");
	 				}
					  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
	     
		
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getInvoiceSummary:", e);
			}
			return arrList;
		}
	  
	  public ArrayList getPeppolSummaryView(Hashtable ht, String afrmDate,
				String atoDate, String plant, String custname) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",dtCondStr="",extraCon="";
			StringBuffer sql;
			 Hashtable htData = new Hashtable();
			try {
				InvoiceDAO movHisDAO = new InvoiceDAO();
				movHisDAO.setmLogger(mLogger);

				dtCondStr ="and ISNULL(A.INVOICE_DATE,'')<>'' AND CAST((SUBSTRING(A.INVOICE_DATE, 7, 4) + '-' + SUBSTRING(a.INVOICE_DATE, 4, 2) + '-' + SUBSTRING(a.INVOICE_DATE, 1, 2)) AS date)";
	    		extraCon= "order by A.ID desc,CAST((SUBSTRING(A.INVOICE_DATE, 7, 4) + SUBSTRING(A.INVOICE_DATE, 4, 2) + SUBSTRING(A.INVOICE_DATE, 1, 2)) AS date) desc ";    			        
		       
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
	       	sCondition = sCondition + " AND CNAME LIKE '%"+custname+"%' " ;
	        } 
		            sql = new StringBuffer("select A.ID,INVOICE_DATE,INVOICE,ISNULL(A.GINO,'') AS GINO,A.DONO,A.CUSTNO,ISNULL(A.ISEXPENSE,0) as ISEXPENSE,ISNULL(A.PEPPOL_STATUS,0) as PEPPOL_STATUS,");
		            sql.append(" ISNULL(CURRENCYID,'') as CURRENCYID,ISNULL((SELECT top 1 B.CURRENCYUSEQT FROM " + plant +"_FININVOICEDET B WHERE B.INVOICEHDRID=A.ID),1) CURRENCYUSEQT,ISNULL(A.PEPPOL_DOC_ID,'') as PEPPOL_DOC_ID,");
		            sql.append(" ISNULL((select SUM(BALANCE) from " + plant +"_FINRECEIVEDET r where r.TYPE='ADVANCE' and r.DONO = A.DONO ),'') as creditbalance,");
		           sql.append(" ISNULL(CNAME,'') as CNAME,ISNULL((select CURRENCYID from " + plant +"_DOHDR p where p.DONO=A.DONO ),ISNULL(A.CURRENCYID,'')) as CURRENCYID,");
		           sql.append(" BILL_STATUS,DUE_DATE,cast(TOTAL_AMOUNT as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,cast((TOTAL_AMOUNT-(ISNULL((select SUM(AMOUNT) from " + plant +"_FINRECEIVEDET p where p.INVOICEHDRID=A.ID and p.TYPE='REGULAR'),0)))as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as BALANCE_DUE");
		           sql.append(" from " + plant +"_FININVOICEHDR A JOIN " + plant +"_CUSTMST V ON V.CUSTNO=A.CUSTNO WHERE A.PLANT='"+ plant+"' " + sCondition);	
				           
	               if (ht.get("ITEM") != null) {
     				   sql.append(" AND A.ID IN (SELECT INVOICEHDRID from [" + plant +"_FININVOICEDET] WHERE ITEM ='" + ht.get("ITEM") + "')");
     					}        
	 				if (ht.get("DONO") != null) {
						  sql.append(" AND A.DONO = '" + ht.get("DONO") + "'");
					    }
	 				if (ht.get("INVOICE") != null) {
						  sql.append(" AND INVOICE = '" + ht.get("INVOICE") + "'");
					    }
	 				if (ht.get("BILL_STATUS") != null) {
						  sql.append(" AND BILL_STATUS = '" + ht.get("BILL_STATUS") + "'");
					    }
	 				if (ht.get("PLNO") != null) {
	  					  sql.append(" AND (A.GINO!='' OR A.GINO is not null OR A.GINO in (select ISNULL(P.GINO,'') from "+plant+"_DNPLHDR P WHERE P.PACKINGLIST = '" + ht.get("PLNO") + "')) AND A.INVOICE in (select ISNULL(P.INVOICE,'') from "+plant+"_DNPLHDR P WHERE P.PACKINGLIST = '" + ht.get("PLNO") + "')");
	  					
	  				    }
	 				if (ht.get("EMPNO") != null) {
						  sql.append(" AND EMPNO = '" + ht.get("EMPNO") + "'");
					    }
					  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
	     
		
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getInvoiceSummary:", e);
			}
			return arrList;
		}
	  
	  public ArrayList getEditInvoiceDetails(Hashtable ht, String plant) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",extraCon="";//dtCondStr="",
			StringBuffer sql;
			 Hashtable htData = new Hashtable();
			try {
				InvoiceDAO movHisDAO = new InvoiceDAO();
				movHisDAO.setmLogger(mLogger);
				
		           sql = new StringBuffer("select INVOICE_DATE,INVOICE,ISNULL(UNITCOST,'') as UCOST,ISNULL(ADDONAMOUNT,'') as ADDONAMOUNT,ISNULL(ADDONTYPE,'') as ADDONTYPE,A.DONO as ORDERNO,ISNULL(A.ORDER_DISCOUNT,'0') as ORDER_DISCOUNT,A.CUSTNO as CUST_CODE,ISNULL(CNAME,'') as CUSTOMER,ISNULL(DATEOFBIRTH,'') as DOB,ISNULL(NATIONALITY,'') as NATIONALITY,ISNULL(B.Note,'') as Notesexp,DUE_DATE,A.PAYMENT_TERMS,A.NOTE,A.TERMSCONDITIONS,");
		           sql.append(" ISNULL(B.ACCOUNT_NAME,'') AS ACCOUNT_NAME,ISNULL(B.LOC,'') AS LOC,ISNULL(B.UOM,'') AS UOM,ISNULL(B.BATCH,'') AS BATCH,A.BILL_STATUS,");
		           sql.append(" EMPNO,ISNULL((SELECT FNAME FROM " + plant +"_EMP_MST E where E.EMPNO=A.EMPNO),'') as EMP_NAME,ITEM_RATES,A.DISCOUNT,A.DISCOUNT_TYPE,A.DISCOUNT_ACCOUNT,");
		           sql.append(" BILL_STATUS,A.SHIPPINGCOST,A.ADJUSTMENT,A.SUB_TOTAL,cast(TOTAL_AMOUNT as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,");
		           sql.append(" ISNULL(CURRENCYID, '') CURRENCYID,ISNULL(B.CURRENCYUSEQT,0) CURRENCYUSEQT,ISNULL(A.OUTBOUD_GST,0) OUTBOUD_GST,ISNULL(A.SHIPPINGID,'') SHIPPINGID,ISNULL(A.SHIPPINGCUSTOMER,'') SHIPPINGCUSTOMER,");
		           sql.append(" ISNULL(A.ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,ISNULL(A.TAXID,'0') TAXID,ISNULL(A.ISDISCOUNTTAX,'0') ISDISCOUNTTAX,ISNULL(A.ISORDERDISCOUNTTAX,'0') ISORDERDISCOUNTTAX,ISNULL(A.ISSHIPPINGTAX,'0') ISSHIPPINGTAX,ISNULL(A.PROJECTID,'0') PROJECTID,ISNULL(A.TRANSPORTID,'0') TRANSPORTID,ISNULL(A.ORDERTYPE,'0') ORDERTYPE, ");
		           sql.append(" ISNULL((SELECT ISNULL(DISPLAY,'') DISPLAY FROM ["+ht.get("PLANT")+"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID), '') DISPLAY,");
		           sql.append(" B.ID as DETID,B.LNNO,ITEM,QTY,UNITPRICE,B.DISCOUNT as ITEM_DISCOUNT,B.DISCOUNT_TYPE as ITEM_DISCOUNT_TYPE,TAX_TYPE,AMOUNT,ISNULL(B.IS_COGS_SET,'N') IS_COGS_SET,");
		           sql.append(" ISNULL(SALES_LOCATION,'') SALES_LOCATION,ISNULL((SELECT PREFIX FROM FINSALESLOCATION C WHERE A.SALES_LOCATION = C.STATE),'') as STATE_PREFIX,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT,");
		           sql.append(" ISNULL((SELECT UNITPRICE FROM "+ plant +"_ITEMMST C WHERE B.ITEM = C.ITEM),'') as BASECOST,");
		           sql.append(" ISNULL((SELECT COST FROM "+ plant +"_ITEMMST C WHERE B.ITEM = C.ITEM),'') as UNITCOST,");
		           sql.append(" ISNULL((SELECT INCPRICE FROM "+ plant +"_ITEMMST C WHERE B.ITEM = C.ITEM),'') as INCPRICE,");
		           sql.append(" ISNULL((SELECT CPPI FROM "+ plant +"_ITEMMST C WHERE B.ITEM = C.ITEM),'') as CPPI,");
		           sql.append(" ISNULL((SELECT ITEMDESC FROM "+ plant +"_ITEMMST C WHERE B.ITEM = C.ITEM),'') as ITEMDESC,");
		           sql.append(" ISNULL((SELECT CATLOGPATH FROM "+ plant +"_ITEMMST C WHERE B.ITEM = C.ITEM),'') as CATLOGPATH, ISNULL((SELECT COUNT(*) FROM " + plant +"_FININVOICEATTACHMENTS N where A.ID=N.INVOICEHDRID),0) as ATTACHNOTE_COUNT ");
		           sql.append(" from " + plant +"_FININVOICEHDR A JOIN " + plant +"_FININVOICEDET B ON A.ID=INVOICEHDRID JOIN " + plant +"_CUSTMST V ON V.CUSTNO=A.CUSTNO WHERE A.PLANT='"+ plant+"'" + sCondition);			           
				           
				            
	 				if (ht.get("ID") != null) {
						  sql.append(" AND A.ID = '" + ht.get("ID") + "'");
					    }   
					   
					  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
	     
		
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getInvoiceEdit:", e);
			}
			return arrList;
		}
	  public int updateInvoiceHdr(Hashtable ht) throws Exception {
		  int invoiceHdrId = 0;

			Hashtable htCond = new Hashtable();
			htCond.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
			htCond.put("ID", (String) ht
					.get("ID"));
			try {
				
				StringBuffer updateQyery = new StringBuffer("set ");

				updateQyery.append("CUSTNO" + " = '"
						+ (String) ht.get("CUSTNO") + "'");
				updateQyery.append("," + "INVOICE" + " = '"
						+ (String) ht.get("INVOICE") + "'");		
				updateQyery.append("," + "DONO" + " = '"
						+ (String) ht.get("DONO") + "'");				
				updateQyery.append("," + "INVOICE_DATE" + " = '"
						+ (String) ht.get("INVOICE_DATE") + "'");
				updateQyery.append("," + "DUE_DATE" + " = '"
						+ (String) ht.get("DUE_DATE") + "'");				
				updateQyery.append("," + "EMPNO" + " = '"
						+ (String) ht.get("EMPNO") + "'");		
				updateQyery.append("," + "ORDERTYPE" + " = '"
						+ (String) ht.get("ORDERTYPE") + "'");		
				updateQyery.append("," + "TRANSPORTID" + " = '"
						+ (String) ht.get("TRANSPORTID") + "'");		
				updateQyery.append("," + "JobNum" + " = '"
						+ (String) ht.get("JobNum") + "'");		
				updateQyery.append("," + "ITEM_RATES" + " = '"
						+ (String) ht.get("ITEM_RATES") + "'");				
				updateQyery.append("," + "DISCOUNT" + " = '"
						+ (String) ht.get("DISCOUNT") + "'");
				updateQyery.append("," + "DISCOUNT_TYPE" + " = '"
						+ (String) ht.get("DISCOUNT_TYPE") + "'");
				updateQyery.append("," + "DISCOUNT_ACCOUNT" + " = '"
						+ (String) ht.get("DISCOUNT_ACCOUNT") + "'");
				updateQyery.append("," + "SHIPPINGCOST" + " = '"
						+ (String) ht.get("SHIPPINGCOST") + "'");		
				updateQyery.append("," + "ADJUSTMENT" + " = '"
						+ (String) ht.get("ADJUSTMENT") + "'");				
				updateQyery.append("," + "SUB_TOTAL" + " = '"
						+ (String) ht.get("SUB_TOTAL") + "'");
				updateQyery.append("," + "TOTAL_AMOUNT" + " = '"
						+ (String) ht.get("TOTAL_AMOUNT") + "'");				
				updateQyery.append("," + "BILL_STATUS" + " = '"
						+ (String) ht.get("BILL_STATUS") + "'");		
				updateQyery.append("," + "NOTE" + " = '"
						+ (String) ht.get("NOTE") + "'");				
				updateQyery.append("," + "TERMSCONDITIONS" + " = '"
						+ (String) ht.get("TERMSCONDITIONS") + "'");
				updateQyery.append("," + "UPAT" + " = '"
						+ (String) ht.get("UPAT") + "'");
				updateQyery.append("," + "UPBY" + " = '"
						+ (String) ht.get("UPBY") + "'");
				updateQyery.append("," + "PAYMENT_TERMS" + " = '"
						+ (String) ht.get("PAYMENT_TERMS") + "'");
				updateQyery.append("," + "SALES_LOCATION" + " = '"
						+ (String) ht.get("SALES_LOCATION") + "'");
				updateQyery.append("," + "TAXTREATMENT" + " = '"
						+ (String) ht.get("TAXTREATMENT") + "'");
				updateQyery.append("," + "TAXAMOUNT" + " = '"
						+ (String) ht.get("TAXAMOUNT") + "'");
				updateQyery.append("," + "DEDUCT_INV" + " = '"
						+ (String) ht.get("DEDUCT_INV") + "'");
				updateQyery.append("," + "CURRENCYUSEQT" + " = '"
						+ (String) ht.get("CURRENCYUSEQT") + "'");
				updateQyery.append("," + "ORDERDISCOUNTTYPE" + " = '"
						+ (String) ht.get("ORDERDISCOUNTTYPE") + "'");
				updateQyery.append("," + "TAXID" + " = '"
						+ (String) ht.get("TAXID") + "'");
				updateQyery.append("," + "ISDISCOUNTTAX" + " = '"
						+ (String) ht.get("ISDISCOUNTTAX") + "'");
				updateQyery.append("," + "ISORDERDISCOUNTTAX" + " = '"
						+ (String) ht.get("ISORDERDISCOUNTTAX") + "'");
				updateQyery.append("," + "ISSHIPPINGTAX" + " = '"
						+ (String) ht.get("ISSHIPPINGTAX") + "'");
				updateQyery.append("," + "PROJECTID" + " = '"
						+ (String) ht.get("PROJECTID") + "'");
				updateQyery.append("," + "SHIPPINGID" + " = '"
						+ (String) ht.get("SHIPPINGID") + "'");
				updateQyery.append("," + "SHIPPINGCUSTOMER" + " = '"
						+ (String) ht.get("SHIPPINGCUSTOMER") + "'");
				updateQyery.append("," + "OUTBOUD_GST" + " = '"
						+ (String) ht.get("OUTBOUD_GST") + "'");

				updateQyery.append("," + "SHIPCONTACTNAME" + " = '"
						+ (String) ht.get("SHIPCONTACTNAME") + "'");
				updateQyery.append("," + "SHIPDESGINATION" + " = '"
						+ (String) ht.get("SHIPDESGINATION") + "'");
				updateQyery.append("," + "SHIPWORKPHONE" + " = '"
						+ (String) ht.get("SHIPWORKPHONE") + "'");
				updateQyery.append("," + "SHIPHPNO" + " = '"
						+ (String) ht.get("SHIPHPNO") + "'");
				updateQyery.append("," + "SHIPEMAIL" + " = '"
						+ (String) ht.get("SHIPEMAIL") + "'");
				updateQyery.append("," + "SHIPCOUNTRY" + " = '"
						+ (String) ht.get("SHIPCOUNTRY") + "'");
				updateQyery.append("," + "SHIPADDR1" + " = '"
						+ (String) ht.get("SHIPADDR1") + "'");
				updateQyery.append("," + "SHIPADDR2" + " = '"
						+ (String) ht.get("SHIPADDR2") + "'");
				updateQyery.append("," + "SHIPADDR3" + " = '"
						+ (String) ht.get("SHIPADDR3") + "'");
				updateQyery.append("," + "SHIPADDR4" + " = '"
						+ (String) ht.get("SHIPADDR4") + "'");
				updateQyery.append("," + "SHIPSTATE" + " = '"
						+ (String) ht.get("SHIPSTATE") + "'");
				updateQyery.append("," + "SHIPZIP" + " = '"
						+ (String) ht.get("SHIPZIP") + "'");
				
				InvoiceDAO  dao = new InvoiceDAO();
				dao.setmLogger(mLogger);
				invoiceHdrId = dao.update(updateQyery.toString(), htCond,"");
			
	  } catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Edit Invoice Cannot be modified");
		}
		return invoiceHdrId;
	  }
	  
	  public List getInvoiceHdrById(Hashtable ht) throws Exception {
			List invoiceHdrList = new ArrayList();
			try {
				invoiceDao.setmLogger(mLogger);
				invoiceHdrList = invoiceDao.getInvoiceHdrById(ht);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return invoiceHdrList;
	 }
	  
	  public List getInvoiceDetByHdrId(Hashtable ht) throws Exception {
			List invoiceDetList = new ArrayList();
			try {
				invoiceDao.setmLogger(mLogger);
				invoiceDetList = invoiceDao.getInvoiceDetByHdrId(ht);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return invoiceDetList;
	 }
	  
	  public ArrayList getInvoiceDetailSummaryView(Hashtable ht, String afrmDate,
				String atoDate, String plant, String custname) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",dtCondStr="",extraCon="";
			StringBuffer sql;
			 Hashtable htData = new Hashtable();
			try {
				InvoiceDAO movHisDAO = new InvoiceDAO();
				movHisDAO.setmLogger(mLogger);

				dtCondStr ="and ISNULL(A.INVOICE_DATE,'')<>'' AND CAST((SUBSTRING(A.INVOICE_DATE, 7, 4) + '-' + SUBSTRING(a.INVOICE_DATE, 4, 2) + '-' + SUBSTRING(a.INVOICE_DATE, 1, 2)) AS date)";
	    		extraCon= "order by A.ID desc,CAST((SUBSTRING(A.INVOICE_DATE, 7, 4) + SUBSTRING(A.INVOICE_DATE, 4, 2) + SUBSTRING(A.INVOICE_DATE, 1, 2)) AS date) desc ";    			        
		       
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
	       	sCondition = sCondition + " AND CNAME LIKE '%"+custname+"%' " ;
	        } 
		            sql = new StringBuffer("SELECT ID,INVOICE_DATE, INVOICE, DONO, CUSTNO, ISEXPENSE, CNAME, ");
		            sql.append("cast(TOTAL_AMOUNT * ISNULL((SELECT CURRENCYUSEQT FROM [" + plant +"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID),1) as DECIMAL(20,5)) as BASE_TOTAL_AMOUNT,");
		            sql.append("cast((cast(TOTAL_AMOUNT * ISNULL((SELECT CURRENCYUSEQT FROM [" + plant +"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID),1) as DECIMAL(20,5))-(ISNULL((select SUM(AMOUNT) from [" + plant +"_FINRECEIVEDET] p where p.INVOICEHDRID=A.ID and p.TYPE='REGULAR'),0)))as DECIMAL(20,5)) as BASE_BALANCE_DUE, ");
		            sql.append("CURRENCYID, BILL_STATUS, DUE_DATE, TOTAL_AMOUNT, BALANCE_DUE FROM (");
		            sql.append(" select A.ID,INVOICE_DATE,INVOICE,A.DONO,A.CUSTNO,ISNULL(A.ISEXPENSE,0) as ISEXPENSE,");
		           sql.append(" ISNULL(CNAME,'') as CNAME,ISNULL((select CURRENCYID from " + plant +"_DOHDR p where p.DONO=A.DONO ),'') as CURRENCYID,");
		           sql.append(" BILL_STATUS,DUE_DATE,cast(TOTAL_AMOUNT as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,cast((TOTAL_AMOUNT-(ISNULL((select SUM(AMOUNT) from " + plant +"_FINRECEIVEDET p where p.INVOICEHDRID=A.ID and p.TYPE='REGULAR'),0)))as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as BALANCE_DUE");
		           sql.append(" from " + plant +"_FININVOICEHDR A JOIN " + plant +"_CUSTMST V ON V.CUSTNO=A.CUSTNO WHERE A.PLANT='"+ plant+"'" + sCondition);
		           
				           
	               if (ht.get("ITEM") != null) {
     				   sql.append(" AND A.ID IN (SELECT INVOICEHDRID from [" + plant +"_FININVOICEDET] WHERE ITEM ='" + ht.get("ITEM") + "')");
     					}        
	 				if (ht.get("DONO") != null) {
						  sql.append(" AND A.DONO = '" + ht.get("DONO") + "'");
					    }
	 				if (ht.get("INVOICE") != null) {
						  sql.append(" AND INVOICE = '" + ht.get("INVOICE") + "'");
					    }
	 				if (ht.get("BILL_STATUS") != null) {
						  sql.append(" AND BILL_STATUS = '" + ht.get("BILL_STATUS") + "'");
					    }
	 				sql.append(") A ");  
					  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
	     
		
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getInvoiceSummary:", e);
			}
			return arrList;
		}
	  
	  public ArrayList getCustomerBalances(Hashtable ht, String afrmDate,
				String atoDate, String plant, String custname) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",dtCondStr="", sCondition2 = "", sCondition3 = "",dtCondStr2="",extraCon="";
			StringBuffer sql;
			 Hashtable htData = new Hashtable();
			try {
				InvoiceDAO movHisDAO = new InvoiceDAO();
				movHisDAO.setmLogger(mLogger);

				dtCondStr =" AND ISNULL(A.INVOICE_DATE,'')<>'' AND CAST((SUBSTRING(A.INVOICE_DATE, 7, 4) + '-' + SUBSTRING(a.INVOICE_DATE, 4, 2) + '-' + SUBSTRING(a.INVOICE_DATE, 1, 2)) AS date)";
				dtCondStr2 =" AND ISNULL(A.RECEIVE_DATE,'')<>'' AND CAST((SUBSTRING(A.RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(a.RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(a.RECEIVE_DATE, 1, 2)) AS date)";
	    		extraCon= " ORDER BY A.CUSTNO";    			        
		       
				   if (afrmDate.length() > 0) {
	              	sCondition = sCondition + dtCondStr + "  >= '" + afrmDate + "' ";
	              	sCondition2 = sCondition2 + dtCondStr2 + "  >= '" + afrmDate + "' ";
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
				       	sCondition3 = " WHERE A.CUSTNO = '"+custname+"' " ;
			        } 
		            sql = new StringBuffer("SELECT (SELECT CNAME FROM [" + plant + "_CUSTMST] WHERE CUSTNO = A.CUSTNO) AS CNAME, ISNULL(A.BALANCE_DUE,0) AS BALANCE_DUE, ISNULL(B.UNUSED_AMOUNT,0) AS UNUSED_AMOUNT, cast((A.BALANCE_DUE - ISNULL(B.UNUSED_AMOUNT,0)) as DECIMAL(20,5)) as BALANCE  FROM (");
		            sql.append(" SELECT A.CUSTNO, SUM(A.BALANCE_DUE) AS BALANCE_DUE FROM (");
		            sql.append(" SELECT A.CUSTNO,cast((TOTAL_AMOUNT-(ISNULL((select SUM(AMOUNT) from [" + plant +"_FINRECEIVEDET] P where P.INVOICEHDRID=A.ID and P.TYPE='REGULAR'),0)))as DECIMAL(20,5)) AS BALANCE_DUE ");
		            sql.append(" FROM [" + plant +"_FININVOICEHDR] A WHERE A.PLANT='" + plant +"'  AND A.BILL_STATUS <> 'Draft' "+sCondition);
		            sql.append(" ) A GROUP BY A.CUSTNO ) A LEFT JOIN");
		            sql.append(" (");
		            sql.append(" SELECT A.CUSTNO, SUM(A.UNUSED_AMOUNT) AS UNUSED_AMOUNT FROM (");
		            sql.append(" SELECT A.CUSTNO,CAST((ISNULL(A.AMOUNTRECEIVED,0) - ISNULL(A.AMOUNTUFP,0)) AS DECIMAL(20,5)) AS UNUSED_AMOUNT FROM [" + plant +"_FINRECEIVEHDR] A  WHERE A.PLANT='" + plant +"' " +sCondition2);
		            sql.append(" ) A GROUP BY A.CUSTNO) B ON A.CUSTNO = B.CUSTNO "+sCondition3);
		            
		            arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,"Exception :repportUtil :: getInvoiceSummary:", e);
			}
			return arrList;
		}
	  public ArrayList getGINOToInvoiceSummaryView(Hashtable ht, String afrmDate,
				String atoDate, String plant, String custname) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",dtCondStr="",extraCon="";
			StringBuffer sql;
			 Hashtable htData = new Hashtable();
			try {
				invoiceDao.setmLogger(mLogger);

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
			           
				           if (custname.length()>0){
				        	   custname = StrUtils.InsertQuotes(custname);
//	                 	sCondition = sCondition + " AND CNAME LIKE '%"+custname+"%' " ;
                        sCondition= sCondition+" AND CUSTNO in (select CUSTNO from "+plant+"_CUSTMST where CNAME like '"+custname+"%')";
	                  } 
				          
				         /*  sql = new StringBuffer("select A.ID as ID,A.DONO,GINO,A.CUSTNO,ISNULL(CNAME,'') as CNAME,(SUBSTRING(A.CRAT, 7, 2) + '/' + SUBSTRING(A.CRAT, 5, 2) + '/' + SUBSTRING(A.CRAT, 1, 4)) as CRAT,");				           
				           sql.append(" A.STATUS,cast(((((AMOUNT+P.SHIPPINGCOST)*P.OUTBOUND_GST)/100)+(AMOUNT+P.SHIPPINGCOST)) as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,ISNULL(QTY,0) as QTY,");
				           sql.append(" ISNULL( (SELECT SUM(D.QTY) FROM [" + plant +"_FINGINOTOINVOICE] B LEFT JOIN [" + plant +"_FININVOICEHDR] C ");
		        		   sql.append("ON B.CUSTNO = C.CUSTNO AND B.DONO =  C.DONO AND B.GINO = C.GINO ");
		        		   sql.append("LEFT JOIN [" + plant +"_FININVOICEDET] D ON C.ID = D.INVOICEHDRID ");
		        		   sql.append("WHERE B.CUSTNO = A.CUSTNO AND B.DONO = A.DONO AND B.GINO = A.GINO),0) AS INVOICEDQTY,");
		        		   sql.append("ISNULL(( SELECT SUM(C.RETURN_QTY) FROM [" + plant +"_FINGINOTOINVOICE] B LEFT JOIN [" + plant +"_FINSORETURN] C ");
      				   sql.append("ON B.CUSTNO = C.CUSTNO AND B.DONO = C.DONO AND B.GINO = C.GINO");
      				   sql.append(" WHERE B.CUSTNO = A.CUSTNO AND B.DONO = A.DONO AND B.GINO = A.GINO),0) AS RETURNEDQTY");
				           sql.append(" from " + plant +"_FINGINOTOINVOICE A JOIN " + plant +"_CUSTMST V ON V.CUSTNO=A.CUSTNO JOIN " + plant +"_DOHDR P ON P.DONO=A.DONO WHERE A.PLANT='"+ plant+"'" + sCondition);*/			           
	                     
				           
				       sql = new StringBuffer("select A.ID as ID,A.DONO,GINO,A.CUSTNO,ISNULL((select ISNULL(CNAME,'')from " + plant +"_CUSTMST V where V.CUSTNO=A.CUSTNO),'') as CNAME,(SUBSTRING(A.CRAT, 7, 2) + '/' + SUBSTRING(A.CRAT, 5, 2) + '/' + SUBSTRING(A.CRAT, 1, 4)) as CRAT,");				           
				       sql.append(" A.STATUS,cast(((((AMOUNT+(ISNULL((select ISNULL (P.SHIPPINGCOST,0) from " + plant +"_DOHDR P where P.DONO=A.DONO),0)))*(ISNULL((select ISNULL (P.OUTBOUND_GST,0) from " + plant +"_DOHDR P where P.DONO=A.DONO),0)))/100)+(AMOUNT+(ISNULL((select ISNULL (P.SHIPPINGCOST,0) from " + plant +"_DOHDR P where P.DONO=A.DONO),0)))) as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,ISNULL(QTY,0) as QTY,");
				       sql.append(" ISNULL( (SELECT SUM(D.QTY) FROM [" + plant +"_FINGINOTOINVOICE] B LEFT JOIN [" + plant +"_FININVOICEHDR] C ");
		        	   sql.append("ON B.CUSTNO = C.CUSTNO AND B.DONO =  C.DONO AND B.GINO = C.GINO ");
		        	   sql.append("LEFT JOIN [" + plant +"_FININVOICEDET] D ON C.ID = D.INVOICEHDRID ");
		        	   sql.append("WHERE B.CUSTNO = A.CUSTNO AND B.DONO = A.DONO AND B.GINO = A.GINO),0) AS INVOICEDQTY,");
		        	   sql.append("ISNULL(( SELECT SUM(C.RETURN_QTY) FROM [" + plant +"_FINGINOTOINVOICE] B LEFT JOIN [" + plant +"_FINSORETURN] C ");
      				   sql.append("ON B.CUSTNO = C.CUSTNO AND B.DONO = C.DONO AND B.GINO = C.GINO");
      				   sql.append(" WHERE B.CUSTNO = A.CUSTNO AND B.DONO = A.DONO AND B.GINO = A.GINO),0) AS RETURNEDQTY");
				       sql.append(" from " + plant +"_FINGINOTOINVOICE A  WHERE A.PLANT='"+ plant+"'" + sCondition);			           

				           
				           if (ht.get("DONO") != null) {
			  					  sql.append(" AND A.DONO = '" + ht.get("DONO") + "'");
			  				    }
						   if (ht.get("GINO") != null) {
			  					  sql.append(" AND GINO = '" + ht.get("GINO") + "'");
			  				    }
						   if (ht.get("STATUS") != null) {
			  					  sql.append(" AND A.STATUS = '" + ht.get("STATUS") + "'");
			  				    }
						   if (ht.get("PLNO") != null) {
			  					  sql.append(" AND GINO IN (select ISNULL(P.GINO,'') from "+plant+"_DNPLHDR P WHERE P.PACKINGLIST = '" + ht.get("PLNO") + "')");
			  				    }
				           
	  				  arrList = invoiceDao.selectForReport(sql.toString(), htData, extraCon);
	       
		
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getGRNOToBillSummary:", e);
			}
			return arrList;
		}
	  
	  public ArrayList getTotalSalesForDashBoard(String plant, String fromDate, String toDate) throws Exception {
			ArrayList al = null;
			Hashtable htData = new Hashtable();
			String extraCon="";
			try {
				String aQuery = "SELECT ISNULL(SUM(ISNULL(QTY, 0) * (ISNULL(UNITPRICE, 0))),0) AS TOTAL_SALES FROM " 
						+ "[" + plant + "_FININVOICEHDR] A JOIN [" + plant + "_FININVOICEDET] B ON A.ID = B.INVOICEHDRID "
						+ "WHERE CONVERT(DATETIME, INVOICE_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "'";
				al = invoiceDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
		}
	  
	  public ArrayList getTotalSalesForDashBoardFromJournal(String plant, String fromDate, String toDate) throws Exception {
			ArrayList al = null;
			Hashtable htData = new Hashtable();
			String extraCon="";
			try {
				
				String aQuery = "SELECT (SUM(ISNULL(CREDITS, 0))-SUM(ISNULL(DEBITS, 0)))AS TOTAL_SALES FROM " + plant + "_FINJOURNALDET A JOIN ["+plant+"_FINJOURNALHDR] B ON A.JOURNALHDRID = B.ID "
						+ "WHERE A.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='8')"
						+ "AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "'";
				al = invoiceDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
		}
	  
	  public ArrayList getTotalSalesSummaryByInvoice(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
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
					aQuery += " SELECT CONVERT(nvarchar, n, 107) INVOICE_DATE,ISNULL(TOTAL_PRICE,0) as TOTAL_PRICE from cte a left join (" + 
							" SELECT CONVERT(DATE, A.INVOICE_DATE, 103) AS CDATE,SUM(CAST((UNITPRICE * QTY) AS DECIMAL(18,"+numberOfDecimal+")))" + 
							" AS TOTAL_PRICE FROM   ["+plant+"_FININVOICEHDR] A JOIN ["+plant+"_FININVOICEDET] B ON A.ID = B.INVOICEHDRID " + 
							" WHERE CONVERT(DATETIME, INVOICE_DATE, 103) between '"+fromDate+"' and '"+toDate+"'" + 
							" GROUP BY CONVERT(DATE, A.INVOICE_DATE, 103) )" + 
							" b on a.n = b.CDATE order by n";
				}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
					aQuery += " SELECT CONVERT(nvarchar, n, 107) INVOICE_DATE,ISNULL(TOTAL_PRICE,0) as TOTAL_PRICE from cte a left join (" + 
							" SELECT datename(month, (CONVERT(DATE, A.INVOICE_DATE, 103))) AS CDATE,SUM(CAST((UNITPRICE * QTY) AS DECIMAL(18,"+numberOfDecimal+")))" + 
							" AS TOTAL_PRICE FROM   ["+plant+"_FININVOICEHDR] A JOIN ["+plant+"_FININVOICEDET] B ON A.ID = B.INVOICEHDRID " + 
							" WHERE CONVERT(DATETIME, INVOICE_DATE, 103) between '"+fromDate+"' and '"+toDate+"'" + 
							" GROUP BY datename(month, (CONVERT(DATE, A.INVOICE_DATE, 103))) )" + 
							" b on a.m = b.CDATE order by n";
				}
				al = invoiceDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
	}
	  
	  public ArrayList getTotalSalesSummaryByInvoiceFromJournal(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
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
				} else if(period.equalsIgnoreCase("Last 15 days")) {
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
					aQuery ="SELECT CONVERT(nvarchar, (CONVERT(DATE, B.JOURNAL_DATE, 103)), 107) AS INVOICE_DATE,CAST((SUM(ISNULL(C.CREDITS, 0))-SUM(ISNULL(C.DEBITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_PRICE FROM "+plant+"_FINJOURNALDET C " + 
							"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID WHERE C.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='8') " +
						    "AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY B.JOURNAL_DATE";
				}else if(period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month") || period.equalsIgnoreCase("Last 7 days") || period.equalsIgnoreCase("This week") || period.equalsIgnoreCase("Last 15 days")) {
					aQuery += " SELECT CONVERT(nvarchar, n, 107) INVOICE_DATE,ISNULL(TOTAL_PRICE,0) as TOTAL_PRICE from cte a left join ("+
							"SELECT CONVERT(DATE, B.JOURNAL_DATE, 103) AS CDATE,CAST((SUM(ISNULL(C.CREDITS, 0))-SUM(ISNULL(C.DEBITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_PRICE FROM " + plant + "_FINJOURNALDET C "+
							"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID " + 
							"WHERE C.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='8')" + 
							"AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY B.JOURNAL_DATE )" + 
							" j on a.n = j.CDATE order by a.n";
				}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
					aQuery += " SELECT CONVERT(nvarchar, n, 107) INVOICE_DATE,ISNULL(TOTAL_PRICE,0) as TOTAL_PRICE from cte a left join (" + 
							"SELECT datename(month, (CONVERT(DATE, B.JOURNAL_DATE, 103))) AS CDATE,CAST((SUM(ISNULL(C.CREDITS, 0))-SUM(ISNULL(C.DEBITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_PRICE FROM " + plant + "_FINJOURNALDET C "+
							"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID " + 
							"WHERE C.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='8')" + 
							"AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY datename(month, (CONVERT(DATE, B.JOURNAL_DATE, 103))) )" + 
							" j on a.m = j.CDATE order by a.n";
				}
				al = invoiceDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
	}
	  
	public ArrayList getTotalIncomeForDashboard(String plant, String fromDate, String toDate, String numberOfDecimal) throws Exception {
		ArrayList al = null;
		Hashtable htData = new Hashtable();
		String extraCon = "";
		try {
			String aQuery = "SELECT CAST(SUM(AMOUNTPAID) as DECIMAL(18,"+numberOfDecimal+")) TOTAL_AMOUNT,ACCOUNTDETAILTYPE FROM ( " 
					+ " SELECT AMOUNTPAID,PAYMENT_DATE, "
					+ " (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
					+ " ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = A.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE "
					+ " FROM ["+plant+"_FINPAYMENTHDR] A "
					+ " WHERE CONVERT(DATETIME, PAYMENT_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
					+ " AND ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=9) "
					+ " UNION ALL "
					+ " SELECT AMOUNTRECEIVED,RECEIVE_DATE, "
					+ " (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D  "
					+ " ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = A.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE "
					+ " FROM ["+plant+"_FINRECEIVEHDR] A "
					+ " WHERE  CONVERT(DATETIME, RECEIVE_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
					+ " AND ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=9) "
					+ " ) A GROUP BY ACCOUNTDETAILTYPE";
			al = invoiceDao.selectForReport(aQuery.toString(), htData, extraCon);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getTotalIncomeFromJournlForDashboard(String plant, String fromDate, String toDate, String numberOfDecimal) throws Exception {
		ArrayList al = null;
		Hashtable htData = new Hashtable();
		String extraCon = "";
		try {
			/*String aQuery = "SELECT ACCOUNTDETAILTYPE,CAST((SUM(ISNULL(C.CREDITS, 0))-SUM(ISNULL(C.DEBITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_AMOUNT FROM " + plant + "_FINJOURNALDET C "+ 
					"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID " + 
					"WHERE C.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='9')" +
					"AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY B.ACCOUNTDETAILTYPE ";*/
			String aQuery = "select ca.ACCOUNT_NAME,CAST((SUM(ISNULL(jd.CREDITS, 0))-SUM(ISNULL(jd.DEBITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_AMOUNT from ["+plant+"_FINJOURNALDET] jd inner join ["+plant+"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+plant+"_FINACCOUNTDETAILTYPE] ad on ad.ID=ca.ACCOUNTDETAILTYPE inner join ["+plant+"_FINJOURNALHDR] hdr on hdr.ID=jd.JOURNALHDRID where ca.ACCOUNTTYPE in (9) AND CONVERT(DATETIME, hdr.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY ca.ACCOUNT_NAME ";
			
			al = invoiceDao.selectForReport(aQuery.toString(), htData, extraCon);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getTotalIncomeSummaryFromJournlForDashboard(String plant, String fromDate, String toDate,String Account ,String numberOfDecimal) throws Exception {
		ArrayList al = null;
		Hashtable htData = new Hashtable();
		String extraCon = "";
		try {
			String aQuery = "select hdr.JOURNAL_DATE,jd.ACCOUNT_NAME,CAST(((ISNULL(jd.CREDITS, 0))-(ISNULL(jd.DEBITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_AMOUNT," + 
					"CASE" + 
					"    WHEN hdr.TRANSACTION_TYPE = 'INVOICE' THEN ISNULL((SELECT IVC.CNAME FROM "+plant+"_FININVOICEHDR AS IV LEFT JOIN "+plant+"_CUSTMST AS IVC ON IVC.CUSTNO = IV.CUSTNO WHERE IV.INVOICE = hdr.TRANSACTION_ID),'-')" + 
					"    WHEN hdr.TRANSACTION_TYPE = 'SALESPAYMENT' THEN ISNULL((SELECT RVC.CNAME FROM "+plant+"_FINRECEIVEHDR AS RV LEFT JOIN "+plant+"_CUSTMST AS RVC ON RVC.CUSTNO = RV.CUSTNO WHERE RV.ID = hdr.TRANSACTION_ID),'-')" + 
					"	WHEN hdr.TRANSACTION_TYPE = 'PURCHASEPAYMENT' THEN ISNULL((SELECT PVC.VNAME FROM "+plant+"_FINPAYMENTHDR AS PV LEFT JOIN "+plant+"_VENDMST AS PVC ON PV.VENDNO = PVC.VENDNO WHERE PV.ID = hdr.TRANSACTION_ID),'-')" + 
					"    ELSE '-'" + 
					"END AS NAME," + 
					"CASE" + 
					"    WHEN hdr.TRANSACTION_TYPE = 'INVOICE' THEN ISNULL((SELECT INVOICE FROM "+plant+"_FININVOICEHDR  WHERE INVOICE = hdr.TRANSACTION_ID),'-')" + 
					"    WHEN hdr.TRANSACTION_TYPE = 'SALESPAYMENT' THEN ISNULL((SELECT REFERENCE FROM "+plant+"_FINRECEIVEHDR WHERE ID = hdr.TRANSACTION_ID),'-')" + 
					"	WHEN hdr.TRANSACTION_TYPE = 'PURCHASEPAYMENT' THEN ISNULL((SELECT REFERENCE FROM "+plant+"_FINPAYMENTHDR WHERE ID = hdr.TRANSACTION_ID),'-')" + 
					"    ELSE '-'" + 
					"END AS REFERENCE " + 
					"from ["+plant+"_FINJOURNALDET] jd inner join ["+plant+"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join " + 
					"["+plant+"_FINACCOUNTDETAILTYPE] ad on ad.ID=ca.ACCOUNTDETAILTYPE inner join ["+plant+"_FINJOURNALHDR] hdr on hdr.ID=jd.JOURNALHDRID " + 
					"where ca.ACCOUNTTYPE in (9) AND CONVERT(DATETIME, hdr.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "'";
			
			if (Account != null && !Account.equalsIgnoreCase("")) {
				aQuery += " AND jd.ACCOUNT_NAME = '" + Account + "'";
			}
 			
			aQuery += " order by hdr.ID desc";
			
			al = invoiceDao.selectForReport(aQuery.toString(), htData, extraCon);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	public ArrayList getTotalIncomeSummaryByInvoice(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
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
				aQuery += " SELECT CONVERT(nvarchar, n, 107) INVOICE_DATE,ISNULL(TOTAL_AMOUNT,0) as TOTAL_PRICE from cte a left join (" + 
							"SELECT CONVERT(DATE, A.PAYMENT_DATE, 103) AS CDATE ,SUM(AMOUNTPAID) TOTAL_AMOUNT FROM ( " 
						+ " SELECT AMOUNTPAID,PAYMENT_DATE, "
						+ " (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
						+ " ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = A.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE "
						+ " FROM ["+plant+"_FINPAYMENTHDR] A "
						+ " WHERE CONVERT(DATETIME, PAYMENT_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
						+ " AND ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=9) "
						+ " UNION ALL "
						+ " SELECT AMOUNTRECEIVED,RECEIVE_DATE, "
						+ " (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D  "
						+ " ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = A.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE "
						+ " FROM ["+plant+"_FINRECEIVEHDR] A "
						+ " WHERE  CONVERT(DATETIME, RECEIVE_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
						+ " AND ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=9) "
						+ " ) A GROUP BY CONVERT(DATE, A.PAYMENT_DATE, 103) " +
						" ) b on a.n = b.CDATE order by n";
			}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
				aQuery += " SELECT CONVERT(nvarchar, n, 107) INVOICE_DATE,ISNULL(TOTAL_AMOUNT,0) as TOTAL_PRICE from cte a left join (" + 
						"SELECT datename(month, (CONVERT(DATE, A.PAYMENT_DATE, 103))) AS CDATE ,SUM(AMOUNTPAID) TOTAL_AMOUNT FROM ( " 
						+ " SELECT AMOUNTPAID,PAYMENT_DATE, "
						+ " (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D "
						+ " ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = A.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE "
						+ " FROM ["+plant+"_FINPAYMENTHDR] A "
						+ " WHERE CONVERT(DATETIME, PAYMENT_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
						+ " AND ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=9) "
						+ " UNION ALL "
						+ " SELECT AMOUNTRECEIVED,RECEIVE_DATE, "
						+ " (SELECT C.ACCOUNTDETAILTYPE FROM ["+plant+"_FINACCOUNTDETAILTYPE] C JOIN ["+plant+"_FINCHARTOFACCOUNTS] D  "
						+ " ON C.ID = D.ACCOUNTDETAILTYPE WHERE D.ACCOUNT_NAME = A.ACCOUNT_NAME) AS ACCOUNTDETAILTYPE "
						+ " FROM ["+plant+"_FINRECEIVEHDR] A "
						+ " WHERE  CONVERT(DATETIME, RECEIVE_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
						+ " AND ACCOUNT_NAME IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=9) "
						+ " ) A GROUP BY datename(month, (CONVERT(DATE, A.PAYMENT_DATE, 103))) " + 
						" ) b on a.m = b.CDATE order by n";
			}
			al = invoiceDao.selectForReport(aQuery.toString(), htData, extraCon);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getTotalIncomeSummaryByInvoiceFromJournal(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
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
				aQuery += " SELECT CONVERT(nvarchar, n, 107) INVOICE_DATE,ISNULL(TOTAL_PRICE,0) as TOTAL_PRICE from cte a left join ("+
						"SELECT CONVERT(DATE, B.JOURNAL_DATE, 103) AS CDATE,CAST((SUM(ISNULL(C.CREDITS, 0))-SUM(ISNULL(C.DEBITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_PRICE FROM " + plant + "_FINJOURNALDET C "+
						"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID " + 
						"WHERE C.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='9')" + 
						"AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY B.JOURNAL_DATE )" + 
						" j on a.n = j.CDATE order by a.n";
			}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
				aQuery += " SELECT CONVERT(nvarchar, n, 107) INVOICE_DATE,ISNULL(TOTAL_PRICE,0) as TOTAL_PRICE from cte a left join (" + 
						"SELECT datename(month, (CONVERT(DATE, B.JOURNAL_DATE, 103))) AS CDATE,CAST((SUM(ISNULL(C.CREDITS, 0))-SUM(ISNULL(C.DEBITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_PRICE FROM " + plant + "_FINJOURNALDET C "+
						"JOIN ["+plant+"_FINJOURNALHDR] B ON C.JOURNALHDRID = B.ID " + 
						"WHERE C.ACCOUNT_ID IN (SELECT ID  FROM " + plant + "_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='9')" + 
						"AND CONVERT(DATETIME, B.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY datename(month, (CONVERT(DATE, B.JOURNAL_DATE, 103))) )" + 
						" j on a.m = j.CDATE order by a.n";
			}
			al = invoiceDao.selectForReport(aQuery.toString(), htData, extraCon);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	
	public ArrayList getInvoiceSummaryDashboardView(String afrmDate, String atoDate, String plant, String custname,String status,String numberOfDecimal) {
		ArrayList arrList = new ArrayList();
		String extraCon="";//dtCondStr="",sCondition = "",
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			InvoiceDAO movHisDAO = new InvoiceDAO();
			movHisDAO.setmLogger(mLogger);
		
			/*sql = new StringBuffer("SELECT JD.CREDITS as CREDITS,A.ID,INVOICE_DATE,INVOICE,ISNULL(A.GINO,'') AS GINO,A.DONO,A.CUSTNO,ISNULL(A.ISEXPENSE,0) as ISEXPENSE, " + 
					"ISNULL(A.CURRENCYID,'') as CURRENCYID,ISNULL((SELECT top 1 B.CURRENCYUSEQT FROM " + plant +"_FININVOICEDET B WHERE B.INVOICEHDRID=A.ID),1) CURRENCYUSEQT, " + 
					"ISNULL((select SUM(BALANCE) from " + plant +"_FINRECEIVEDET r where r.TYPE='ADVANCE' and r.DONO = A.DONO ),'') as creditbalance, " + 
					"ISNULL(CNAME,'') as CNAME,ISNULL((select CURRENCYID from " + plant +"_DOHDR p where p.DONO=A.DONO )," + 
					"ISNULL(A.CURRENCYID,'')) as CURRENCYID, BILL_STATUS,DUE_DATE,cast(A.TOTAL_AMOUNT as DECIMAL(20,5)) as TOTAL_AMOUNT," + 
					"cast((A.TOTAL_AMOUNT-(ISNULL((select SUM(AMOUNT) from " + plant +"_FINRECEIVEDET p where p.INVOICEHDRID=A.ID and p.TYPE='REGULAR'),0)))as DECIMAL(20,5)) as BALANCE_DUE " + 
					"FROM  " + plant +"_FINJOURNALDET JD JOIN [" + plant +"_FINJOURNALHDR] JH ON JD.JOURNALHDRID = JH.ID JOIN " + plant +"_FININVOICEHDR A ON A.INVOICE=JH.TRANSACTION_ID JOIN " + plant +"_CUSTMST V ON V.CUSTNO=A.CUSTNO " + 
					"WHERE JD.ACCOUNT_ID IN (SELECT ID  FROM " + plant +"_FINCHARTOFACCOUNTS WHERE ACCOUNTTYPE='8') AND CONVERT(DATETIME, JH.JOURNAL_DATE, 103) BETWEEN '" + afrmDate + "' AND '" + atoDate + "'");
            
 			if (custname.length()>0) {
				sql.append(" AND V.CNAME = '" + custname + "'");
			}
 			if (status != null && !status.equalsIgnoreCase("")) {
 				sql.append(" AND A.BILL_STATUS = '" + status + "'");
			}
 			
 			sql.append(" order by A.ID desc");*/
			
			sql = new StringBuffer("select hdr.JOURNAL_DATE,jd.ID,CAST(((ISNULL(jd.CREDITS, 0))-(ISNULL(jd.DEBITS, 0))) AS DECIMAL(25,"+numberOfDecimal+"))AS TOTAL_AMOUNT," + 
					"CASE    " + 
					"WHEN hdr.TRANSACTION_TYPE = 'INVOICE' THEN ISNULL((SELECT CM.CNAME FROM " + plant +"_FININVOICEHDR AS IV LEFT JOIN " + plant +"_CUSTMST AS CM ON IV.CUSTNO = CM.CUSTNO WHERE IV.INVOICE = hdr.TRANSACTION_ID),'-')    " + 
					"WHEN hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' THEN ISNULL((SELECT CM.CNAME  FROM " + plant +"_FINCUSTOMERCREDITNOTEHDR AS CN LEFT JOIN " + plant +"_CUSTMST AS CM ON CM.CUSTNO = CN.CUSTNO WHERE CN.CREDITNOTE = hdr.TRANSACTION_ID),'-')	" + 
					"ELSE '-'END AS NAME," + 
					"CASE    " + 
					"WHEN hdr.TRANSACTION_TYPE = 'INVOICE' THEN ISNULL((SELECT IV.INVOICE FROM " + plant +"_FININVOICEHDR AS IV WHERE IV.INVOICE = hdr.TRANSACTION_ID),'-')    " + 
					"WHEN hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' THEN ISNULL((SELECT CN.CREDITNOTE FROM " + plant +"_FINCUSTOMERCREDITNOTEHDR AS CN WHERE CN.CREDITNOTE = hdr.TRANSACTION_ID),'-')	  " + 
					"ELSE '-'END AS REFERENCE," + 
					"CASE    " + 
					"WHEN hdr.TRANSACTION_TYPE = 'INVOICE' THEN ISNULL((SELECT IV.BILL_STATUS FROM " + plant +"_FININVOICEHDR AS IV WHERE IV.INVOICE = hdr.TRANSACTION_ID),'-')    " + 
					"WHEN hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' THEN ISNULL((SELECT CN.CREDIT_STATUS FROM " + plant +"_FINCUSTOMERCREDITNOTEHDR AS CN WHERE CN.CREDITNOTE = hdr.TRANSACTION_ID),'-')	  " + 
					"ELSE '-'END AS STATUS, " + 
					"CASE    " + 
					"WHEN hdr.TRANSACTION_TYPE = 'INVOICE' THEN ISNULL((SELECT IV.INVOICE FROM " + plant +"_FININVOICEHDR AS IV WHERE IV.INVOICE = hdr.TRANSACTION_ID),'-')    " + 
					"WHEN hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' THEN ISNULL((SELECT CN.INVOICE FROM " + plant +"_FINCUSTOMERCREDITNOTEHDR AS CN WHERE CN.CREDITNOTE = hdr.TRANSACTION_ID),'-')	  " + 
					"ELSE '-'END AS CONDITIONS " + 
					"from [" + plant +"_FINJOURNALDET] jd inner join [" + plant +"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID " + 
					"inner join [" + plant +"_FINACCOUNTDETAILTYPE] ad on ad.ID=ca.ACCOUNTDETAILTYPE " + 
					"inner join [" + plant +"_FINJOURNALHDR] hdr on hdr.ID=jd.JOURNALHDRID where ca.ACCOUNTTYPE='8' " + 
					"AND (hdr.TRANSACTION_TYPE = 'INVOICE' OR hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES') " + 
					"AND CONVERT(DATETIME, hdr.JOURNAL_DATE, 103) BETWEEN '" + afrmDate + "' AND '" + atoDate + "' order by hdr.ID desc");
 			
			arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
     
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getInvoiceSummary:", e);
		}
		return arrList;
	}
	
	 public boolean saveInvoiceMultiRemarks(Hashtable ht) throws Exception {
			boolean flag = false;
			try {
				invoiceDao.setmLogger(mLogger);
				flag = invoiceDao.insertInvoiceMultiRemarks(ht);

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}

			return flag;
		}
	 
	 public ArrayList getpeppolinvoicesummary(String plant) throws Exception {
			ArrayList al = null;
			Hashtable htData = new Hashtable();
			String extraCon = "";
			try {
				String aQuery = "SELECT ID,INVOICE,INVOICE_DATE,CUSTNO,SUB_TOTAL,TAXAMOUNT,TOTAL_AMOUNT,PEPPOL_DOC_ID,ISNULL(PEPPOL_STATUS,0) PEPPOL_STATUS FROM "+plant+"_FININVOICEHDR WHERE ORIGIN='PEPPOL' ORDER BY ID DESC";
				al = invoiceDao.selectForReport(aQuery.toString(), htData, extraCon);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return al;
		}
	 
	 public boolean updatepeppolstatus(String query, Hashtable htCondition, String extCond)
				throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" UPDATE " + "["
						+ htCondition.get("PLANT") + "_" + "FININVOICEHDR" + "]");
				sql.append(" ");
				sql.append(query);
				sql.append(" WHERE ");
				String conditon = formCondition(htCondition);
				sql.append(conditon);

				if (extCond.length() != 0) {
					sql.append(extCond);
				}

				flag = updateData(con, sql.toString());

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				DbBean.closeConnection(con);
			}
			return flag;
		}
	 
	 public String formCondition(Hashtable<String, String> ht) throws Exception {
			String sCondition = "";
			try {
				Enumeration<String> enumeration = ht.keys();
				for (int i = 0; i < ht.size(); i++) {
					String key = StrUtils.fString((String) enumeration
							.nextElement());
					String value = StrUtils.fString((String) ht.get(key));
					sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
							+ "' AND ";
				}
				sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
						sCondition.length() - 4) : "";
			} catch (Exception e) {
				throw e;
			}
			return sCondition;
		}
	 
	 public boolean updateData(Connection conn, String sql) throws Exception {		
			boolean flag = false;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			int updateCount = 0;
			try {
				stmt = conn.prepareStatement(sql);
				updateCount = stmt.executeUpdate();
				if (updateCount > 0) {
					flag = true;

				}
				else
					throw new Exception("Unable to update!");
				
			} catch (Exception e) {
				MLogger.log(0, "Exception" + e.getMessage());
				throw e;
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (stmt != null) {
						stmt.close();
					}
				} catch (Exception e) {
					throw e;
				}
			}
			return flag;
		}
	 

	  public ArrayList getReturnSummaryView(Hashtable ht, String afrmDate,
				String atoDate, String plant, String custname) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",dtCondStr="",extraCon="";
			StringBuffer sql;
			 Hashtable htData = new Hashtable();
			try {
				InvoiceDAO movHisDAO = new InvoiceDAO();
				movHisDAO.setmLogger(mLogger);

				dtCondStr ="and ISNULL(A.RETURN_DATE,'')<>'' AND CAST((SUBSTRING(A.RETURN_DATE, 7, 4) + '-' + SUBSTRING(a.RETURN_DATE, 4, 2) + '-' + SUBSTRING(a.RETURN_DATE, 1, 2)) AS date)";
	    		extraCon= "order by A.ID desc,CAST((SUBSTRING(A.RETURN_DATE, 7, 4) + SUBSTRING(A.RETURN_DATE, 4, 2) + SUBSTRING(A.RETURN_DATE, 1, 2)) AS date) desc ";    			        
		       
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
	       	sCondition = sCondition + " AND V.VNAME LIKE '%"+custname+"%' " ;
	        } 
		            sql = new StringBuffer("select A.ID,RETURN_DATE,IDRNO,ISNULL(A.ORDERTYPE,'') AS ORDERTYPE,A.VENDNO,A.VNAME,");
		            sql.append(" ISNULL(CURRENCYID,'') as CURRENCYID,ISNULL((SELECT top 1 B.CURRENCYUSEQT FROM " + plant +"_PRODUCTRETURNDET B WHERE B.PRODUCTHDRID=A.ID),1) CURRENCYUSEQT,");
//		            sql.append(" ISNULL((select SUM(BALANCE) from " + plant +"_FINRECEIVEDET r where r.TYPE='ADVANCE' and r.DONO = A.DONO ),'') as creditbalance,");
		           sql.append(" ISNULL(V.VNAME,'') as VNAME,ISNULL(A.CURRENCYID,'') as CURRENCYID,");
		           sql.append(" RETURN_STATUS,DUE_DATE,cast(TOTAL_AMOUNT as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT");
		           sql.append(" from " + plant +"_PRODUCTRETURNHDR A JOIN " + plant +"_VENDMST V ON V.VENDNO=A.VENDNO WHERE A.PLANT='"+ plant+"'" + sCondition);	
				           
	               if (ht.get("ITEM") != null) {
      				   sql.append(" AND A.ID IN (SELECT PRODUCTHDRID from [" + plant +"_PRODUCTRETURNDET] WHERE ITEM ='" + ht.get("ITEM") + "')");
      					}        
	 				if (ht.get("IDRNO") != null) {
						  sql.append(" AND IDRNO = '" + ht.get("IDRNO") + "'");
					    }
	 				if (ht.get("RETURN_STATUS") != null) {
						  sql.append(" AND RETURN_STATUS = '" + ht.get("RETURN_STATUS") + "'");
					    }
	 				if (ht.get("EMPNO") != null) {
						  sql.append(" AND EMPNO = '" + ht.get("EMPNO") + "'");
					    }
	 				if (ht.get("ORDERTYPE") != null) {
	 					sql.append(" AND ORDERTYPE = '" + ht.get("ORDERTYPE") + "'");
	 				}
					  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
	     
		
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getInvoiceSummary:", e);
			}
			return arrList;
		}
	  
	  public ArrayList getReceiveSummaryView(Hashtable ht, String afrmDate,
				String atoDate, String plant, String custname) {
			ArrayList arrList = new ArrayList();
			String sCondition = "",dtCondStr="",extraCon="";
			StringBuffer sql;
			 Hashtable htData = new Hashtable();
			try {
				InvoiceDAO movHisDAO = new InvoiceDAO();
				movHisDAO.setmLogger(mLogger);

				dtCondStr ="and ISNULL(A.RECEIVE_DATE,'')<>'' AND CAST((SUBSTRING(A.RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(a.RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(a.RECEIVE_DATE, 1, 2)) AS date)";
	    		extraCon= "order by A.ID desc,CAST((SUBSTRING(A.RECEIVE_DATE, 7, 4) + SUBSTRING(A.RECEIVE_DATE, 4, 2) + SUBSTRING(A.RECEIVE_DATE, 1, 2)) AS date) desc ";    			        
		       
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
	       	sCondition = sCondition + " AND V.CNAME LIKE '%"+custname+"%' " ;
	        } 
		            sql = new StringBuffer("select A.ID,RECEIVE_DATE,ICRNO,A.CustCode,A.CustName,");
		            sql.append(" ISNULL(CURRENCYID,'') as CURRENCYID,ISNULL((SELECT top 1 B.CURRENCYUSEQT FROM " + plant +"_PRODUCTRECEIVEDET B WHERE B.RECEIVEHDRID=A.ID),1) CURRENCYUSEQT,");
//		            sql.append(" ISNULL((select SUM(BALANCE) from " + plant +"_FINRECEIVEDET r where r.TYPE='ADVANCE' and r.DONO = A.DONO ),'') as creditbalance,");
		           sql.append(" ISNULL(V.CNAME,'') as CNAME,ISNULL(A.CURRENCYID,'') as CURRENCYID,");
		           sql.append(" RECEIVE_STATUS,cast(TOTAL_AMOUNT as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT");
		           sql.append(" from " + plant +"_PRODUCTRECEIVEHDR A JOIN " + plant +"_CUSTMST V ON V.CUSTNO=A.CustCode WHERE A.PLANT='"+ plant+"'" + sCondition);	
				           
	               if (ht.get("ITEM") != null) {
    				   sql.append(" AND A.ID IN (SELECT RECEIVEHDRID from [" + plant +"_PRODUCTRECEIVEDET] WHERE ITEM ='" + ht.get("ITEM") + "')");
    					}        
	 				if (ht.get("ICRNO") != null) {
						  sql.append(" AND ICRNO = '" + ht.get("ICRNO") + "'");
					    }
	 				if (ht.get("RECEIVE_STATUS") != null) {
						  sql.append(" AND RECEIVE_STATUS = '" + ht.get("RECEIVE_STATUS") + "'");
					    }
	 				if (ht.get("EMPNO") != null) {
						  sql.append(" AND EMPNO = '" + ht.get("EMPNO") + "'");
					    }
					  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
	     
		
			 }catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getInvoiceSummary:", e);
			}
			return arrList;
		}
	
}
