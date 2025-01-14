package com.track.db.util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.ExpensesDAO;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ExpensesUtil {
	
	private boolean printLog = MLoggerConstant.ExpensesUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	private ExpensesDAO expenseDao = new ExpensesDAO();
	
	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public int addexpenseHdr(Hashtable ht, String plant) {
		int expenseHdrId = 0;
		try {
			expenseHdrId = expenseDao.addexpenseHdr(ht, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return expenseHdrId;		
	}
	
	public boolean addMultipleExpenseDet(List<Hashtable<String, String>> expenseDetInfoList, String plant){
		boolean flag = false;		
		try {
			flag = expenseDao.addMultipleExpenseDet(expenseDetInfoList, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean addExpenseAttachments(List<Hashtable<String, String>> expenseAttachmentList, String plant){
		boolean flag = false;		
		try {
			flag = expenseDao.addExpenseAttachments(expenseAttachmentList, plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public ArrayList getExpensesSummary(Hashtable ht, String afrmDate,
			String atoDate, String plant, String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			
			expenseDao.setmLogger(mLogger);

	        		dtCondStr ="and ISNULL(A.EXPENSES_DATE,'')<>'' AND CAST((SUBSTRING(A.EXPENSES_DATE, 7, 4) + '-' + SUBSTRING(a.EXPENSES_DATE, 4, 2) + '-' + SUBSTRING(a.EXPENSES_DATE, 1, 2)) AS date)";
	        		extraCon= "order by ID desc, CAST((SUBSTRING(A.EXPENSES_DATE, 7, 4) + SUBSTRING(A.EXPENSES_DATE, 4, 2) + SUBSTRING(A.EXPENSES_DATE, 1, 2)) AS date) desc";    			        
			       
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
                 	sCondition = sCondition + " AND A.VENDNO in (select VENDNO from "+plant+"_VENDMST where VNAME like '"+custname+"%')";
                 	
                  } 
			          
			           sql = new StringBuffer("select A.ID,EXPENSES_DATE,A.CUSTNO,A.VENDNO,'' as EXPENSES_ACCOUNT,PAID_THROUGH,");
			           sql.append(" ISNULL((select ISNULL(VNAME,'') FROM " + plant +"_VENDMST V WHERE VENDNO=A.VENDNO),'') as VNAME,REFERENCE,");
			           sql.append(" ISNULL((SELECT TOP 1 B.CURRENCYTOBASE FROM " + plant +"_FINEXPENSESDET B WHERE B.EXPENSESHDRID=A.ID),1) CURRENCYTOBASE,");
			           sql.append(" STATUS,ISNULL(CURRENCYID,'') CURRENCYID,cast(SUB_TOTAL as DECIMAL(30," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as SUB_TOTAL_AMOUNT,cast(TOTAL_AMOUNT as DECIMAL(30," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,ISNULL((select ISNULL(CNAME,'') FROM " + plant +"_CUSTMST V WHERE V.CUSTNO=A.CUSTNO),'') as CNAME");
			           sql.append(" from " + plant +"_FINEXPENSESHDR A WHERE A.PLANT='"+ plant+"' " + sCondition);			           
                     
			        
   				if (ht.get("ACCOUNT_NAME") != null) {
  					  sql.append(" AND A.ID IN ISNULL((SELECT B.EXPENSESHDRID FROM " + plant +"_FINEXPENSESDET B WHERE B.EXPENSES_ACCOUNT = '" + ht.get("ACCOUNT_NAME") + "'),0)");
  				    }
   				if (ht.get("REFERENCE") != null) {
					  sql.append(" AND A.REFERENCE LIKE '%" + ht.get("REFERENCE") + "%'");
				    }
   				if (ht.get("STATUS") != null) {
					  sql.append(" AND A.STATUS = '" + ht.get("STATUS") + "'");
				    }
  				   
  				  arrList = expenseDao.selectForReport(sql.toString(), htData, extraCon);
       
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getExpensesSummary:", e);
		}
		return arrList;
	}

	public ArrayList getPosExpensesSummary(Hashtable ht, String afrmDate,
			String atoDate, String plant, String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			
			expenseDao.setmLogger(mLogger);

	        		dtCondStr ="and ISNULL(A.EXPENSES_DATE,'')<>'' AND CAST((SUBSTRING(A.EXPENSES_DATE, 7, 4) + '-' + SUBSTRING(a.EXPENSES_DATE, 4, 2) + '-' + SUBSTRING(a.EXPENSES_DATE, 1, 2)) AS date)";
	        		extraCon= "order by ID desc, CAST((SUBSTRING(A.EXPENSES_DATE, 7, 4) + SUBSTRING(A.EXPENSES_DATE, 4, 2) + SUBSTRING(A.EXPENSES_DATE, 1, 2)) AS date) desc";    			        
			       
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
                 	sCondition = sCondition + " AND A.VENDNO in (select VENDNO from "+plant+"_VENDMST where VNAME like '"+custname+"%')";
                 	
                  } 
			          
			           sql = new StringBuffer("select A.ID,EXPENSES_DATE,A.CUSTNO,A.VENDNO,'' as EXPENSES_ACCOUNT,PAID_THROUGH,ISNULL(A.OUTLET,'') OUTLET,ISNULL(A.TERMINAL,'') TERMINAL,");
			           sql.append(" ISNULL((select ISNULL(VNAME,'') FROM " + plant +"_VENDMST V WHERE VENDNO=A.VENDNO),'') as VNAME,REFERENCE,");
			           sql.append(" ISNULL((SELECT TOP 1 B.CURRENCYTOBASE FROM " + plant +"_FINEXPENSESDET B WHERE B.EXPENSESHDRID=A.ID),1) CURRENCYTOBASE,");
			           sql.append(" STATUS,ISNULL(CURRENCYID,'') CURRENCYID,cast(SUB_TOTAL as DECIMAL(30," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as SUB_TOTAL_AMOUNT,cast(TOTAL_AMOUNT as DECIMAL(30," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,ISNULL((select ISNULL(CNAME,'') FROM " + plant +"_CUSTMST V WHERE V.CUSTNO=A.CUSTNO),'') as CNAME");
			           sql.append(" from " + plant +"_FINEXPENSESHDR A WHERE A.PLANT='"+ plant+"' AND ISNULL(ISPOS,0) = 1 " + sCondition);			           
                     
			        
   				if (ht.get("ACCOUNT_NAME") != null) {
  					  sql.append(" AND A.ID IN ISNULL((SELECT B.EXPENSESHDRID FROM " + plant +"_FINEXPENSESDET B WHERE B.EXPENSES_ACCOUNT = '" + ht.get("ACCOUNT_NAME") + "'),0)");
  				    }
   				if (ht.get("REFERENCE") != null) {
					  sql.append(" AND A.REFERENCE LIKE '%" + ht.get("REFERENCE") + "%'");
				    }
   				if (ht.get("STATUS") != null) {
					  sql.append(" AND A.STATUS = '" + ht.get("STATUS") + "'");
				    }
   				if (ht.get("OUTLET") != null) {
					  sql.append(" AND A.OUTLET = '" + ht.get("OUTLET") + "'");
				    }
   				if (ht.get("TERMINAL") != null) {
					  sql.append(" AND A.TERMINAL = '" + ht.get("TERMINAL") + "'");
				    }
  				   
  				  arrList = expenseDao.selectForReport(sql.toString(), htData, extraCon);
       
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getExpensesSummary:", e);
		}
		return arrList;
	}
	
	public ArrayList getApExpensesSummary(Hashtable ht, String afrmDate,
			String atoDate, String plant, String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			
			expenseDao.setmLogger(mLogger);

	        		dtCondStr ="and ISNULL(A.EXPENSES_DATE,'')<>'' AND CAST((SUBSTRING(A.EXPENSES_DATE, 7, 4) + '-' + SUBSTRING(a.EXPENSES_DATE, 4, 2) + '-' + SUBSTRING(a.EXPENSES_DATE, 1, 2)) AS date)";
	        		extraCon= "order by ID desc, CAST((SUBSTRING(A.EXPENSES_DATE, 7, 4) + SUBSTRING(A.EXPENSES_DATE, 4, 2) + SUBSTRING(A.EXPENSES_DATE, 1, 2)) AS date) desc";    			        
			       
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
                 	sCondition = sCondition + " AND A.VENDNO in (select VENDNO from "+plant+"_VENDMST where VNAME like '"+custname+"%')";
                 	
                  } 
			          
			           sql = new StringBuffer("select A.ID,EXPENSES_DATE,A.CUSTNO,A.VENDNO,'' as EXPENSES_ACCOUNT,PAID_THROUGH,ISNULL(EXBILL,'') AS EXBILL,ISNULL(A.ISPAID,'0') ISPAID,ISNULL(A.OUTLET,'') OUTLET,ISNULL(A.TERMINAL,'') TERMINAL,");
			           sql.append(" ISNULL((select ISNULL(VNAME,'') FROM " + plant +"_VENDMST V WHERE VENDNO=A.VENDNO),'') as VNAME,REFERENCE,");
			           sql.append(" ISNULL((SELECT TOP 1 B.CURRENCYTOBASE FROM " + plant +"_FINEXPENSESDET B WHERE B.EXPENSESHDRID=A.ID),1) CURRENCYTOBASE,");
			           sql.append(" STATUS,ISNULL(CURRENCYID,'') CURRENCYID,cast(SUB_TOTAL as DECIMAL(30," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as SUB_TOTAL_AMOUNT,cast(TOTAL_AMOUNT as DECIMAL(30," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,ISNULL((select ISNULL(CNAME,'') FROM " + plant +"_CUSTMST V WHERE V.CUSTNO=A.CUSTNO),'') as CNAME");
			           sql.append(" from " + plant +"_FINEXPENSESHDR A WHERE A.PLANT='"+ plant+"' AND ISNULL(ISPOS,0) = 0 " + sCondition);			           
                     
			        
   				if (ht.get("ACCOUNT_NAME") != null) {
  					  sql.append(" AND A.ID IN ISNULL((SELECT B.EXPENSESHDRID FROM " + plant +"_FINEXPENSESDET B WHERE B.EXPENSES_ACCOUNT = '" + ht.get("ACCOUNT_NAME") + "'),0)");
  				    }
   				if (ht.get("REFERENCE") != null) {
					  sql.append(" AND A.REFERENCE LIKE '%" + ht.get("REFERENCE") + "%'");
				    }
   				if (ht.get("STATUS") != null) {
					  sql.append(" AND A.STATUS = '" + ht.get("STATUS") + "'");
				    }
  				   
  				  arrList = expenseDao.selectForReport(sql.toString(), htData, extraCon);
       
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getExpensesSummary:", e);
		}
		return arrList;
	}
	public ArrayList getPOSExpenses(Hashtable ht, String afrmDate,
			String atoDate, String plant, String outletcode,String terminalcode,String cust_name) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		Hashtable htData = new Hashtable();
		try {
			
			expenseDao.setmLogger(mLogger);
			
			dtCondStr ="and ISNULL(A.EXPENSES_DATE,'')<>'' AND CAST((SUBSTRING(A.EXPENSES_DATE, 7, 4) + '-' + SUBSTRING(a.EXPENSES_DATE, 4, 2) + '-' + SUBSTRING(a.EXPENSES_DATE, 1, 2)) AS date)";
			extraCon= "order by ID desc, CAST((SUBSTRING(A.EXPENSES_DATE, 7, 4) + SUBSTRING(A.EXPENSES_DATE, 4, 2) + SUBSTRING(A.EXPENSES_DATE, 1, 2)) AS date) desc";    			        
			
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
			
			sql = new StringBuffer("select A.ID,EXPENSES_DATE,A.CUSTNO,A.VENDNO,'' as EXPENSES_ACCOUNT,PAID_THROUGH,");
			sql.append(" ISNULL((select ISNULL(VNAME,'') FROM " + plant +"_VENDMST V WHERE VENDNO=A.VENDNO),'') as VNAME,REFERENCE,");
			sql.append(" B.DESCRIPTION,B.AMOUNT,B.EXPENSES_ACCOUNT,");
			sql.append(" ISNULL((SELECT TOP 1 B.CURRENCYTOBASE FROM " + plant +"_FINEXPENSESDET B WHERE B.EXPENSESHDRID=A.ID),1) CURRENCYTOBASE,");
			sql.append(" STATUS,ISNULL(CURRENCYID,'') CURRENCYID,cast(SUB_TOTAL as DECIMAL(30," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as SUB_TOTAL_AMOUNT,cast(TOTAL_AMOUNT as DECIMAL(30," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,ISNULL((select ISNULL(CNAME,'') FROM " + plant +"_CUSTMST V WHERE V.CUSTNO=A.CUSTNO),'') as CNAME");
			sql.append(" from " + plant +"_FINEXPENSESHDR A JOIN " + plant +"_FINEXPENSESDET B ON A.ID=B.EXPENSESHDRID WHERE A.ISPOS=1 AND A.PLANT='"+ plant+"'" + sCondition);			           
			
			
			if (outletcode.length() > 0) {
				sql.append(" AND A.OUTLET LIKE '%" + outletcode + "%'");
			}
			if (terminalcode.length() > 0) {
				sql.append(" AND A.TERMINAL LIKE '%" + terminalcode + "%'");
			}
			
			arrList = expenseDao.selectForReport(sql.toString(), htData, extraCon);
			
			
		}catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getExpensesPOS:", e);
		}
		return arrList;
	}
	
	public String GetHDRMAXId(String plant) {
		String expenseId = "";
		try {			
			expenseId = expenseDao.GetHDRMAXId(plant);

		} catch (Exception e) {
		}
		return expenseId;
	}
	public ArrayList getEditIExpensesDetails(Hashtable ht, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",extraCon="";//dtCondStr="",
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			ExpensesDAO movHisDAO = new ExpensesDAO();
			movHisDAO.setmLogger(mLogger);
	          
	           sql = new StringBuffer("select A.ID,EXPENSES_DATE,A.ISPAID,ISNULL(A.BILL,'') BILL,ISNULL(A.EXBILL,'') EXBILL,ISNULL(A.OUTLET,'') OUTLET,ISNULL(A.TERMINAL,'') TERMINAL,ISNULL(A.EXPENSETAX,0) AS EXPENSETAX,ISNULL(A.EXPENSETAXAMOUNT,0) AS EXPENSETAXAMOUNT,ISNULL(B.ISEXPENSEGST,0) ISEXPENSEGST,A.SHIPMENT_CODE,A.PONO as ORDERNO,A.CUSTNO as CUST_CODE,A.VENDNO,PAID_THROUGH,REFERENCE,A.CURRENCYID,A.ISBILLABLE,ISNULL(A.TAXAMOUNT,0) TAXAMOUNT,");
	           sql.append(" ISNULL((SELECT CNAME FROM " + plant +"_CUSTMST E where E.CUSTNO=A.CUSTNO),'') as CUSTOMER,ISNULL((SELECT VNAME FROM " + plant +"_VENDMST E where E.VENDNO=A.VENDNO),'') as VNAME,");
	           sql.append(" STATUS,A.SUB_TOTAL,cast(TOTAL_AMOUNT as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,ISNULL(TAXTREATMENT, 0) sTAXTREATMENT,ISNULL(REVERSECHARGE, 0) REVERSECHARGE,ISNULL(GOODSIMPORT, 0) GOODSIMPORT,");
	           sql.append(" B.ID as DETID,EXPENSES_ACCOUNT,DESCRIPTION,TAX_TYPE,AMOUNT,ISNULL((SELECT TAXTREATMENT FROM " + plant +"_CUSTMST E where E.CUSTNO=A.CUSTNO),'') as TAXTREATMENT,");
	           sql.append(" CURRENCYTOBASE,ISNULL((SELECT ISNULL(DISPLAY,'') DISPLAY FROM ["+ht.get("PLANT")+"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID), '') DISPLAY,ISNULL(A.TAXID,'0') TAXID,ISNULL(A.STANDARDTAX,'0') STANDARDTAX,ISNULL(A.PROJECTID,'0') PROJECTID,");
	           sql.append(" ISNULL((SELECT COUNT(*) FROM " + plant +"_FINEXPENSESATTACHMENTS N where A.ID=N.EXPENSESHDRID),0) as ATTACHNOTE_COUNT ");
	           sql.append(" from " + plant +"_FINEXPENSESHDR A JOIN " + plant +"_FINEXPENSESDET B ON A.ID=EXPENSESHDRID WHERE A.PLANT='"+ plant+"'" + sCondition);			           
			           
                   
			            
 				if (ht.get("ID") != null) {
					  sql.append(" AND A.ID = '" + ht.get("ID") + "'");
				    }   
				   
				  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
     
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getExpensesEdit:", e);
		}
		return arrList;
	}
	public int updateExpensesHdr(Hashtable ht) throws Exception {
		  int expenseHdrId = 0;

			Hashtable htCond = new Hashtable();
			htCond.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
			htCond.put("ID", (String) ht
					.get("ID"));
			try {
				
				StringBuffer updateQyery = new StringBuffer("set ");

				updateQyery.append("CUSTNO" + " = '"
						+ (String) ht.get("CUSTNO") + "'");
				updateQyery.append("," + "VENDNO" + " = '"
						+ (String) ht.get("VENDNO") + "'");		
				updateQyery.append("," + "PONO" + " = '"
						+ (String) ht.get("PONO") + "'");				
				updateQyery.append("," + "EXPENSES_DATE" + " = '"
						+ (String) ht.get("EXPENSES_DATE") + "'");
				updateQyery.append("," + "SHIPMENT_CODE" + " = '"
						+ (String) ht.get("SHIPMENT_CODE") + "'");				
				updateQyery.append("," + "PAID_THROUGH" + " = '"
						+ (String) ht.get("PAID_THROUGH") + "'");		
				updateQyery.append("," + "REFERENCE" + " = '"
						+ (String) ht.get("REFERENCE") + "'");				
				updateQyery.append("," + "CURRENCYID" + " = '"
						+ (String) ht.get("CURRENCYID") + "'");				
				updateQyery.append("," + "SUB_TOTAL" + " = '"
						+ (String) ht.get("SUB_TOTAL") + "'");
				updateQyery.append("," + "TOTAL_AMOUNT" + " = '"
						+ (String) ht.get("TOTAL_AMOUNT") + "'");
				updateQyery.append("," + "UPAT" + " = '"
						+ (String) ht.get("UPAT") + "'");
				updateQyery.append("," + "UPBY" + " = '"
						+ (String) ht.get("UPBY") + "'");
				updateQyery.append("," + "STATUS" + " = '"
						+ (String) ht.get("STATUS") + "'");
				updateQyery.append("," + "TAXTREATMENT" + " = '"
						+ (String) ht.get("TAXTREATMENT") + "'");
				updateQyery.append("," + "REVERSECHARGE" + " = '"
						+ (String) ht.get("REVERSECHARGE") + "'");
				updateQyery.append("," + "GOODSIMPORT" + " = '"
						+ (String) ht.get("GOODSIMPORT") + "'");
				updateQyery.append("," + "TAXAMOUNT" + " = '"
						+ (String) ht.get("TAXAMOUNT") + "'");
				updateQyery.append("," + "ISPAID" + " = '"
						+ (String) ht.get("ISPAID") + "'");
				updateQyery.append("," + "TAXID" + " = '"
						+ (String) ht.get("TAXID") + "'");
				updateQyery.append("," + "STANDARDTAX" + " = '"
						+ (String) ht.get("STANDARDTAX") + "'");
				updateQyery.append("," + "PROJECTID" + " = '"
						+ (String) ht.get("PROJECTID") + "'");
				updateQyery.append("," + "EXPENSETAX" + " = '"
						+ (String) ht.get("EXPENSETAX") + "'");
				updateQyery.append("," + "EXPENSETAXAMOUNT" + " = '"
						+ (String) ht.get("EXPENSETAXAMOUNT") + "'");
				updateQyery.append("," + "EXBILL" + " = '"
						+ (String) ht.get("EXBILL") + "'");
				
				ExpensesDAO  dao = new ExpensesDAO();
				dao.setmLogger(mLogger);
				expenseHdrId = dao.update(updateQyery.toString(), htCond,"");
			
	  } catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Edit Invoice Cannot be modified");
		}
		return expenseHdrId;
	  }
	
	public List getExpensesforDetails(Hashtable ht, String plant) {
		List arrList = new ArrayList();
		String sCondition = "",extraCon="";//dtCondStr="",
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			ExpensesDAO movHisDAO = new ExpensesDAO();
			movHisDAO.setmLogger(mLogger);
	          
	           sql = new StringBuffer("select A.ID,EXPENSES_DATE,A.SHIPMENT_CODE,ISNULL(A.PONO,'') as ORDERNO,A.CUSTNO as CUST_CODE,A.VENDNO,PAID_THROUGH,REFERENCE,A.CURRENCYID,A.ISBILLABLE,ISNULL(GSTPERCENTAGE,0) GSTPERCENTAGE,ISNULL(A.TAX_STATUS,0) TAX_STATUS,");
	           sql.append(" ISNULL((SELECT CNAME FROM " + plant +"_CUSTMST E where E.CUSTNO=A.CUSTNO),'') as CUSTOMER,ISNULL((SELECT VNAME FROM " + plant +"_VENDMST E where E.VENDNO=A.VENDNO),'') as VNAME,");
	           sql.append(" STATUS,A.SUB_TOTAL,cast(TOTAL_AMOUNT as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,");
	           sql.append(" B.ID as DETID,EXPENSES_ACCOUNT,DESCRIPTION,TAX_TYPE,AMOUNT,");
	           sql.append(" ISNULL((SELECT COUNT(*) FROM " + plant +"_FINEXPENSESATTACHMENTS N where A.ID=N.EXPENSESHDRID),0) as ATTACHNOTE_COUNT ");
	           sql.append(" from " + plant +"_FINEXPENSESHDR A JOIN " + plant +"_FINEXPENSESDET B ON A.ID=EXPENSESHDRID LEFT JOIN " + plant +"_COMPANY_CONFIG C ON B.TAX_TYPE = C.GSTTYPE WHERE A.PLANT='"+ plant+"'" + sCondition);			           
			           
                   
			            
 				if (ht.get("ID") != null) {
					  sql.append(" AND A.ID = '" + ht.get("ID") + "'");
				    }   
				   
				  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
     
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getExpensesEdit:", e);
		}
		return arrList;
	}
	
	public ArrayList getExpenseDetails(Hashtable ht, String afrmDate,
			String atoDate, String plant, String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			
			expenseDao.setmLogger(mLogger);

			dtCondStr ="and ISNULL(A.EXPENSES_DATE,'')<>'' AND CAST((SUBSTRING(A.EXPENSES_DATE, 7, 4) + '-' + SUBSTRING(a.EXPENSES_DATE, 4, 2) + '-' + SUBSTRING(a.EXPENSES_DATE, 1, 2)) AS date)";
			extraCon= "order by ID desc, CAST((SUBSTRING(A.EXPENSES_DATE, 7, 4) + SUBSTRING(A.EXPENSES_DATE, 4, 2) + SUBSTRING(A.EXPENSES_DATE, 1, 2)) AS date) desc";    			        
			       
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
	         	sCondition = sCondition + " AND A.VENDNO in (select VENDNO from "+plant+"_VENDMST where VNAME like '"+custname+"%')";
         	} 
			          
		   sql = new StringBuffer("select A.ID,EXPENSES_DATE,A.CUSTNO,A.VENDNO,ISNULL(B.EXPENSES_ACCOUNT,'') as EXPENSES_ACCOUNT,PAID_THROUGH,");
		   sql.append(" ISNULL((select ISNULL(VNAME,'') FROM [" + plant +"_VENDMST] V WHERE VENDNO=A.VENDNO),'') as VNAME,REFERENCE,");
		   sql.append(" ISNULL((SELECT CURRENCYUSEQT FROM ["+ plant +"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID),0) AS CURRENCYUSEQT,");
		   sql.append(" STATUS,ISNULL(CURRENCYID,'') CURRENCYID,cast(B.AMOUNT as DECIMAL(30," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as SUB_TOTAL_AMOUNT,ISNULL(B.TAX_TYPE,'') as TAX_TYPE,ISNULL((select ISNULL(CNAME,'') FROM " + plant +"_CUSTMST V WHERE V.CUSTNO=A.CUSTNO),'') as CNAME");
		   sql.append(" from " + plant +"_FINEXPENSESHDR A JOIN " + plant +"_FINEXPENSESDET B ON B.EXPENSESHDRID=A.ID WHERE A.PLANT='"+ plant+"'" + sCondition);			           
                     
			        
			if (ht.get("ACCOUNT_NAME") != null) {
				  sql.append(" AND A.ID IN (SELECT B.EXPENSESHDRID FROM " + plant +"_FINEXPENSESDET B WHERE B.EXPENSES_ACCOUNT = '" + ht.get("ACCOUNT_NAME") + "')");
			    }
			if (ht.get("REFERENCE") != null) {
				  sql.append(" AND A.REFERENCE LIKE '%" + ht.get("REFERENCE") + "%'");
			    }
			if (ht.get("STATUS") != null) {
				  sql.append(" AND A.STATUS = '" + ht.get("STATUS") + "'");
			    }
  				   
			arrList = expenseDao.selectForReport(sql.toString(), htData, extraCon);
       
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,"Exception :repportUtil :: getExpensesSummary:", e);
		 }
		return arrList;
	}
	
	public List getConvExpensesforDetails(Hashtable ht, String plant) {
		List arrList = new ArrayList();
		String sCondition = "",extraCon="";//dtCondStr="",
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			ExpensesDAO movHisDAO = new ExpensesDAO();
			movHisDAO.setmLogger(mLogger);
	          
	           sql = new StringBuffer("select A.ID,EXPENSES_DATE,A.SHIPMENT_CODE,ISNULL(A.TAXAMOUNT,0) AS TAXAMOUNT,ISNULL(A.EXBILL,'') AS EXBILL,ISNULL(A.ISPOS,0) AS ISPOS,ISNULL(A.EXPENSETAX,0) AS EXPENSETAX,ISNULL(A.EXPENSETAXAMOUNT,0) AS EXPENSETAXAMOUNT,ISNULL(A.PAYMENTID,0) as PAYMENTID,ISNULL(A.DEBITNOTEAMOUNT,0) as DEBITNOTEAMOUNT,ISNULL(A.DEBITNOTEREMARK,'') as DEBITNOTEREMARK,ISNULL(A.PONO,'') as ORDERNO,A.CUSTNO as CUST_CODE,A.VENDNO,PAID_THROUGH,REFERENCE,A.CURRENCYID,A.ISBILLABLE,ISNULL(GSTPERCENTAGE,0) GSTPERCENTAGE,ISNULL(B.ISEXPENSEGST,0) ISEXPENSEGST,ISNULL(A.TAX_STATUS,0) TAX_STATUS,");
	           sql.append(" ISNULL((SELECT CNAME FROM " + plant +"_CUSTMST E where E.CUSTNO=A.CUSTNO),'') as CUSTOMER,ISNULL((SELECT VNAME FROM " + plant +"_VENDMST E where E.VENDNO=A.VENDNO),'') as VNAME,");
	           sql.append(" STATUS,(A.SUB_TOTAL*ISNULL(CURRENCYTOBASE,1)) SUB_TOTAL,cast((TOTAL_AMOUNT*ISNULL(CURRENCYTOBASE,1)) as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,ISNULL(CURRENCYTOBASE,1) as CURRENCYTOBASE,");
	           sql.append(" B.ID as DETID,EXPENSES_ACCOUNT,DESCRIPTION,TAX_TYPE,(AMOUNT*ISNULL(CURRENCYTOBASE,1)) AMOUNT,ISNULL(A.TAXID,'0') TAXID,ISNULL(A.STANDARDTAX,'0') STANDARDTAX,ISNULL(A.PROJECTID,0) PROJECTID,");
	           sql.append(" ISNULL((SELECT COUNT(*) FROM " + plant +"_FINEXPENSESATTACHMENTS N where A.ID=N.EXPENSESHDRID),0) as ATTACHNOTE_COUNT ");
	           sql.append(" from " + plant +"_FINEXPENSESHDR A JOIN " + plant +"_FINEXPENSESDET B ON A.ID=EXPENSESHDRID LEFT JOIN " + plant +"_COMPANY_CONFIG C ON B.TAX_TYPE = C.GSTTYPE WHERE A.PLANT='"+ plant+"'" + sCondition);			           
			           
                   
			            
 				if (ht.get("ID") != null) {
					  sql.append(" AND A.ID = '" + ht.get("ID") + "'");
				    }   
				   
				  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
     
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getExpensesEdit:", e);
		}
		return arrList;
	}
	
	public ArrayList getExpensesByCategory(Hashtable ht, String afrmDate,
			String atoDate, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			
			expenseDao.setmLogger(mLogger);

			dtCondStr ="and ISNULL(A.EXPENSES_DATE,'')<>'' AND CAST((SUBSTRING(A.EXPENSES_DATE, 7, 4) + '-' + SUBSTRING(a.EXPENSES_DATE, 4, 2) + '-' + SUBSTRING(a.EXPENSES_DATE, 1, 2)) AS date)";
			extraCon= "ORDER BY EXPENSES_ACCOUNT";    			        
			       
			if (afrmDate.length() > 0) {
				sCondition = sCondition + dtCondStr + "  >= '" + afrmDate+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition +dtCondStr+ " <= '" + atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition +dtCondStr+ " <= '" + atoDate + "'  ";
				}
			}   
			          
		   sql = new StringBuffer("SELECT SUM(amountwithtax) AS amountwithtax, SUM(amount) AS amount, EXPENSES_ACCOUNT from (");
		   sql.append(" select CASE WHEN (datalength(ISNULL(TAX_TYPE,''))=0) THEN ");
		   sql.append(" cast(amount * ISNULL((select CURRENCYUSEQT from [" + plant +"_CURRENCYMST] where CURRENCYID=a.CURRENCYID),0)  as DECIMAL(20,5)) ");
		   sql.append(" ELSE ");
		   sql.append(" CASE WHEN (CHARINDEX('[', TAX_TYPE) = 0) THEN ");
		   sql.append(" cast(amount * ISNULL((select CURRENCYUSEQT from [" + plant +"_CURRENCYMST] where CURRENCYID=a.CURRENCYID),0)  as DECIMAL(20,5)) ");
		   sql.append(" ELSE ");
		   sql.append(" cast(amount * ISNULL((select CURRENCYUSEQT from [" + plant +"_CURRENCYMST] where CURRENCYID=a.CURRENCYID),0)  * (1+(cast(SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE)+1,3)as DECIMAL(20,5)))/100) as DECIMAL(20,5)) ");
		   sql.append(" END ");
		   sql.append("END   as amountwithtax, ");
		   sql.append(" amount * ISNULL((select CURRENCYUSEQT from [" + plant +"_CURRENCYMST] where CURRENCYID=a.CURRENCYID),0) as amount, EXPENSES_ACCOUNT ");
		   sql.append(" from [" + plant +"_FINEXPENSESHDR] A JOIN [" + plant +"_FINEXPENSESDET] B ON B.EXPENSESHDRID=A.ID WHERE  A.PLANT='"+ plant+"'" + sCondition);
			        
		   if (ht.get("ACCOUNT_NAME") != null) {
			   sql.append(" AND A.ID IN (SELECT ISNULL(B.EXPENSESHDRID,0) FROM " + plant +"_FINEXPENSESDET B WHERE B.EXPENSES_ACCOUNT = '" + ht.get("ACCOUNT_NAME") + "')");
		   }
		   sql.append(") T  group by EXPENSES_ACCOUNT");
		   arrList = expenseDao.selectForReport(sql.toString(), htData, extraCon);
		}catch (Exception e) {
			this.mLogger.exception(this.printLog,"Exception :repportUtil :: getExpensesByCategory:", e);
		}
		return arrList;
	}
	
	public ArrayList getExpensesByCustomer(Hashtable ht, String afrmDate,
			String atoDate, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			
			expenseDao.setmLogger(mLogger);

			dtCondStr ="and ISNULL(A.EXPENSES_DATE,'')<>'' AND a.CUSTNO <> '' AND CAST((SUBSTRING(A.EXPENSES_DATE, 7, 4) + '-' + SUBSTRING(a.EXPENSES_DATE, 4, 2) + '-' + SUBSTRING(a.EXPENSES_DATE, 1, 2)) AS date)";
			extraCon= "ORDER BY CNAME";    			        
			       
			if (afrmDate.length() > 0) {
				sCondition = sCondition + dtCondStr + "  >= '" + afrmDate+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition +dtCondStr+ " <= '" + atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition +dtCondStr+ " <= '" + atoDate + "'  ";
				}
			}   
			          
		   sql = new StringBuffer("SELECT SUM(amountwithtax) AS amountwithtax, SUM(amount) AS amount, CNAME, count(cname) as expensecount  from (");
		   sql.append(" SELECT CASE WHEN (datalength(ISNULL(TAX_TYPE,''))=0) THEN ");
		   sql.append(" CAST(amount * ISNULL((select CURRENCYUSEQT from [" + plant +"_CURRENCYMST] WHERE CURRENCYID=a.CURRENCYID),0)  AS DECIMAL(20,5)) ");
		   sql.append(" ELSE ");
		   sql.append(" CASE WHEN (CHARINDEX('[', TAX_TYPE) = 0) THEN ");
		   sql.append(" cast(amount * ISNULL((select CURRENCYUSEQT from [" + plant +"_CURRENCYMST] where CURRENCYID=a.CURRENCYID),0)  as DECIMAL(20,5)) ");
		   sql.append(" ELSE ");
		   sql.append(" cast(amount * ISNULL((select CURRENCYUSEQT from [" + plant +"_CURRENCYMST] where CURRENCYID=a.CURRENCYID),0)  * (1+(cast(SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE)+1,3)as DECIMAL(20,5)))/100) as DECIMAL(20,5)) ");
		   sql.append(" END ");
		   sql.append("END   as amountwithtax, ");
		   //sql.append(" CAST(amount * ISNULL((select CURRENCYUSEQT from [" + plant +"_CURRENCYMST] WHERE CURRENCYID=a.CURRENCYID),0)  * (1+(cast(SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE)+1,3)as DECIMAL(20,5)))/100) as DECIMAL(20,5)) END AS amountwithtax, ");
		   sql.append(" amount * ISNULL((select CURRENCYUSEQT from [" + plant +"_CURRENCYMST] WHERE CURRENCYID=a.CURRENCYID),0) AS amount, ");
		   sql.append(" CASE WHEN  (datalength(ISNULL(A.CUSTNO,''))=0) THEN 'Others' ELSE (SELECT CNAME FROM [" + plant +"_CUSTMST] where CUSTNO = a.CUSTNO ) END as CNAME ");
		   sql.append(" FROM [" + plant +"_FINEXPENSESHDR] A JOIN [" + plant +"_FINEXPENSESDET] B ON B.EXPENSESHDRID=A.ID WHERE  A.PLANT='"+ plant+"'" + sCondition);
		   if (ht.get("CUSTNO") != null) {
			   sql.append(" AND A.CUSTNO = '"+ht.get("CUSTNO")+"' ");
		   }
		   sql.append(") T  GROUP BY CNAME");
		   arrList = expenseDao.selectForReport(sql.toString(), htData, extraCon);
		}catch (Exception e) {
			this.mLogger.exception(this.printLog,"Exception :repportUtil :: getExpensesByCustomer:", e);
		}
		return arrList;
	}
	
	public ArrayList getExpensesBySupplier(Hashtable ht, String afrmDate,
			String atoDate, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			
			expenseDao.setmLogger(mLogger);

			dtCondStr ="and ISNULL(A.EXPENSES_DATE,'')<>'' AND a.VENDNO <> '' AND CAST((SUBSTRING(A.EXPENSES_DATE, 7, 4) + '-' + SUBSTRING(a.EXPENSES_DATE, 4, 2) + '-' + SUBSTRING(a.EXPENSES_DATE, 1, 2)) AS date)";
			extraCon= "ORDER BY VNAME";    			        
			       
			if (afrmDate.length() > 0) {
				sCondition = sCondition + dtCondStr + "  >= '" + afrmDate+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition +dtCondStr+ " <= '" + atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition +dtCondStr+ " <= '" + atoDate + "'  ";
				}
			}   
			          
		   sql = new StringBuffer("SELECT SUM(amountwithtax) AS amountwithtax, SUM(amount) AS amount, VNAME, count(VNAME) as expensecount  from (");
		   sql.append(" SELECT CASE WHEN (datalength(ISNULL(TAX_TYPE,''))=0) THEN ");
		   sql.append(" CAST(amount * ISNULL((select CURRENCYUSEQT from [" + plant +"_CURRENCYMST] WHERE CURRENCYID=a.CURRENCYID),0)  AS DECIMAL(20,5)) ");
		   sql.append(" ELSE ");
		   sql.append(" CASE WHEN (CHARINDEX('[', TAX_TYPE) = 0) THEN ");
		   sql.append(" cast(amount * ISNULL((select CURRENCYUSEQT from [" + plant +"_CURRENCYMST] where CURRENCYID=a.CURRENCYID),0)  as DECIMAL(20,5)) ");
		   sql.append(" ELSE ");
		   sql.append(" cast(amount * ISNULL((select CURRENCYUSEQT from [" + plant +"_CURRENCYMST] where CURRENCYID=a.CURRENCYID),0)  * (1+(cast(SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE)+1,3)as DECIMAL(20,5)))/100) as DECIMAL(20,5)) ");
		   sql.append(" END ");
		   sql.append("END   as amountwithtax, ");
		  // sql.append(" CAST(amount * ISNULL((select CURRENCYUSEQT from [" + plant +"_CURRENCYMST] WHERE CURRENCYID=a.CURRENCYID),0)  * (1+(cast(SUBSTRING(TAX_TYPE, CHARINDEX('[', TAX_TYPE)+1,3)as DECIMAL(20,5)))/100) as DECIMAL(20,5)) END AS amountwithtax, ");
		   sql.append(" amount * ISNULL((select CURRENCYUSEQT from [" + plant +"_CURRENCYMST] WHERE CURRENCYID=a.CURRENCYID),0) AS amount, ");
		   sql.append(" CASE WHEN  (datalength(ISNULL(A.VENDNO,''))=0) THEN 'Others' ELSE (SELECT VNAME FROM [" + plant +"_VENDMST] where VENDNO = a.VENDNO ) END as VNAME ");
		   sql.append(" FROM [" + plant +"_FINEXPENSESHDR] A JOIN [" + plant +"_FINEXPENSESDET] B ON B.EXPENSESHDRID=A.ID WHERE  A.PLANT='"+ plant+"'" + sCondition);
		   if (ht.get("VENDNO") != null) {
			   sql.append(" AND A.VENDNO = '"+ht.get("VENDNO")+"' ");
		   }
		   sql.append(") T  GROUP BY VNAME");
		   arrList = expenseDao.selectForReport(sql.toString(), htData, extraCon);
		}catch (Exception e) {
			this.mLogger.exception(this.printLog,"Exception :repportUtil :: getExpensesByCustomer:", e);
		}
		return arrList;
	}
	
	public int updateExpensesHdrstatus(Hashtable ht) throws Exception {
		  int expenseHdrId = 0;

			Hashtable htCond = new Hashtable();
			htCond.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
			htCond.put("ID", (String) ht.get("ID"));
			try {
				
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append("UPBY" + " = '"
						+ (String) ht.get("UPBY") + "'");
				updateQyery.append("," + "STATUS" + " = '"
						+ (String) ht.get("STATUS") + "'");
				
				ExpensesDAO  dao = new ExpensesDAO();
				dao.setmLogger(mLogger);
				expenseHdrId = dao.update(updateQyery.toString(), htCond,"");
			
	  } catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Edit Invoice Cannot be modified");
		}
		return expenseHdrId;
	  }
	
	public boolean deleteexpense(String plant,String TranId)
	        throws Exception {
			boolean deleteprdForTranId = false;
			PreparedStatement ps = null;
			PreparedStatement pshdr = null;
			PreparedStatement psatt = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        String sQry = "DELETE FROM " + "[" + plant + "_FINEXPENSESHDR" + "]"
			                        + " WHERE ID ='"+TranId+"'";
			        this.mLogger.query(this.printLog, sQry);
			        ps = con.prepareStatement(sQry);
			        int iCnt = ps.executeUpdate();
			        if (iCnt > 0)
			                deleteprdForTranId = true;
			        if(deleteprdForTranId) {
			        sQry = "DELETE FROM " + "[" + plant + "_FINEXPENSESDET" + "]"
	                        + " WHERE EXPENSESHDRID ='"+TranId+"'";
			        this.mLogger.query(this.printLog, sQry);
			        pshdr = con.prepareStatement(sQry);
//			        int iCnthdr = 
			        		pshdr.executeUpdate();
			       
			     
			         sQry = "DELETE FROM " + "[" + plant + "_FINEXPENSESATTACHMENTS" + "]"
		                    + " WHERE EXPENSESHDRID ='"+TranId+"'";
				    this.mLogger.query(this.printLog, sQry);
				    psatt = con.prepareStatement(sQry);
//				    int iCntatt = 
				    		psatt.executeUpdate();
				    
			        }
			        
			} catch (Exception e) {
			        this.mLogger.exception(this.printLog, "", e);
			} finally {
			        DbBean.closeConnection(con, ps);
			}
			
			return deleteprdForTranId;
 	}
	
	public ArrayList getTotalExpenseSummaryFromJournlForDashboard(String plant, String fromDate, String toDate,String Account ,String numberOfDecimal) throws Exception {
		ArrayList al = null;
		Hashtable htData = new Hashtable();
		String extraCon = "";
		try {
			/*String aQuery = "select hdr.JOURNAL_DATE,jd.ACCOUNT_NAME,CAST(((ISNULL(jd.CREDITS, 0))-(ISNULL(jd.DEBITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_AMOUNT," + 
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
					"where ca.ACCOUNTTYPE in (9) AND CONVERT(DATETIME, hdr.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "'";*/
			
			
			String aQuery = "select hdr.JOURNAL_DATE,jd.ACCOUNT_NAME,CAST(((ISNULL(jd.DEBITS, 0))) AS DECIMAL(25,"+numberOfDecimal+"))AS TOTAL_AMOUNT,\r\n" + 
					"CASE    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'INVOICE' THEN ISNULL((SELECT IVC.CNAME FROM "+plant+"_FININVOICEHDR AS IV LEFT JOIN "+plant+"_CUSTMST AS IVC ON IVC.CUSTNO = IV.CUSTNO WHERE IV.INVOICE = hdr.TRANSACTION_ID),'-')    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'SALESPAYMENT' THEN ISNULL((SELECT RVC.CNAME FROM "+plant+"_FINRECEIVEHDR AS RV LEFT JOIN "+plant+"_CUSTMST AS RVC ON RVC.CUSTNO = RV.CUSTNO WHERE RV.ID = hdr.TRANSACTION_ID),'-')	\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'PURCHASEPAYMENT' THEN ISNULL((SELECT PVC.VNAME FROM "+plant+"_FINPAYMENTHDR AS PV LEFT JOIN "+plant+"_VENDMST AS PVC ON PV.VENDNO = PVC.VENDNO WHERE PV.ID = hdr.TRANSACTION_ID),'-')    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'BILL' THEN ISNULL((SELECT VM.VNAME FROM "+plant+"_FINBILLHDR AS BI LEFT JOIN "+plant+"_VENDMST AS VM ON BI.VENDNO = VM.VENDNO WHERE BI.BILL = hdr.TRANSACTION_ID),'-')    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' THEN ISNULL((SELECT VM.VNAME FROM "+plant+"_FINVENDORCREDITNOTEHDR AS SD LEFT JOIN "+plant+"_VENDMST AS VM ON SD.VENDNO = VM.VENDNO WHERE SD.ID = hdr.TRANSACTION_ID),'-')	\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' THEN ISNULL((SELECT CM.CNAME  FROM "+plant+"_FINCUSTOMERCREDITNOTEHDR AS CN LEFT JOIN "+plant+"_CUSTMST AS CM ON CM.CUSTNO = CN.CUSTNO WHERE CN.CREDITNOTE = hdr.TRANSACTION_ID),'-')\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'EXPENSE' THEN '-'\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'EXPENSE PAID' THEN '-'\r\n" + 
					"ELSE '-'END AS NAME,\r\n" + 
					"CASE    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'INVOICE' THEN ISNULL((SELECT INVOICE FROM "+plant+"_FININVOICEHDR  WHERE INVOICE = hdr.TRANSACTION_ID),'-')    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'SALESPAYMENT' THEN ISNULL((SELECT REFERENCE FROM "+plant+"_FINRECEIVEHDR WHERE ID = hdr.TRANSACTION_ID),'-')	\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'PURCHASEPAYMENT' THEN ISNULL((SELECT REFERENCE FROM "+plant+"_FINPAYMENTHDR WHERE ID = hdr.TRANSACTION_ID),'-')\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'BILL' THEN ISNULL((SELECT BI.BILL FROM "+plant+"_FINBILLHDR AS BI WHERE BI.BILL = hdr.TRANSACTION_ID),'-')\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' THEN ISNULL((SELECT CN.CREDITNOTE FROM "+plant+"_FINCUSTOMERCREDITNOTEHDR AS CN WHERE CN.CREDITNOTE = hdr.TRANSACTION_ID),'-')	      \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' THEN ISNULL((SELECT SD.CREDITNOTE FROM "+plant+"_FINVENDORCREDITNOTEHDR AS SD WHERE SD.ID = hdr.TRANSACTION_ID),'-')\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'EXPENSE' THEN ISNULL((SELECT ISNULL(EX.REFERENCE,'') FROM "+plant+"_FINEXPENSESHDR AS EX WHERE EX.ID = hdr.TRANSACTION_ID),'-')       \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'EXPENSE PAID' THEN ISNULL((SELECT ISNULL(EX.REFERENCE,'') FROM "+plant+"_FINEXPENSESHDR AS EX WHERE EX.ID = hdr.TRANSACTION_ID),'-')       \r\n" +
					"ELSE '-'END AS REFERENCE \r\n" + 
					"from ["+plant+"_FINJOURNALDET] jd inner join ["+plant+"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+plant+"_FINACCOUNTDETAILTYPE] ad on ad.ID=ca.ACCOUNTDETAILTYPE \r\n" + 
					"inner join ["+plant+"_FINJOURNALHDR] hdr on hdr.ID=jd.JOURNALHDRID where ca.ACCOUNTTYPE in (11) \r\n" + 
					"AND (hdr.TRANSACTION_TYPE = 'EXPENSE' OR hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' OR hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' OR hdr.TRANSACTION_TYPE = 'BILL' OR \r\n" + 
					"hdr.TRANSACTION_TYPE = 'PURCHASEPAYMENT' OR hdr.TRANSACTION_TYPE = 'SALESPAYMENT' OR hdr.TRANSACTION_TYPE = 'INVOICE' ) "+
					"AND CONVERT(DATETIME, hdr.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "'";
			
			
			if (Account != null && !Account.equalsIgnoreCase("")) {
				aQuery += " AND jd.ACCOUNT_NAME = '" + Account + "'";
			}
 			
			aQuery += " order by hdr.ID desc";
			
			al = expenseDao.selectForReport(aQuery.toString(), htData, extraCon);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getPaymentIssuedSummaryFromJournlForDashboard(String plant, String fromDate, String toDate,String Account ,String numberOfDecimal) throws Exception {
		ArrayList al = null;
		Hashtable htData = new Hashtable();
		String extraCon = "";
		try {
			
			String aQuery = "select hdr.JOURNAL_DATE,hdr.TRANSACTION_ID,jd.ACCOUNT_NAME,CAST(((ISNULL(jd.CREDITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_AMOUNT, \r\n" + 
					"CASE    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'INVOICE' THEN ISNULL((SELECT IVC.CNAME FROM "+plant+"_FININVOICEHDR AS IV LEFT JOIN "+plant+"_CUSTMST AS IVC ON IVC.CUSTNO = IV.CUSTNO WHERE IV.INVOICE = hdr.TRANSACTION_ID),'-')    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'SALESPAYMENT' THEN ISNULL((SELECT RVC.CNAME FROM "+plant+"_FINRECEIVEHDR AS RV LEFT JOIN "+plant+"_CUSTMST AS RVC ON RVC.CUSTNO = RV.CUSTNO WHERE RV.ID = hdr.TRANSACTION_ID),'-')	\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'PURCHASEPAYMENT' THEN ISNULL((SELECT PVC.VNAME FROM "+plant+"_FINPAYMENTHDR AS PV LEFT JOIN "+plant+"_VENDMST AS PVC ON PV.VENDNO = PVC.VENDNO WHERE PV.ID = hdr.TRANSACTION_ID),'-')    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'BILL' THEN ISNULL((SELECT VM.VNAME FROM "+plant+"_FINBILLHDR AS BI LEFT JOIN "+plant+"_VENDMST AS VM ON BI.VENDNO = VM.VENDNO WHERE BI.BILL = hdr.TRANSACTION_ID),'-')    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' THEN ISNULL((SELECT VM.VNAME FROM "+plant+"_FINVENDORCREDITNOTEHDR AS SD LEFT JOIN "+plant+"_VENDMST AS VM ON SD.VENDNO = VM.VENDNO WHERE SD.ID = hdr.TRANSACTION_ID),'-')	\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' THEN ISNULL((SELECT CM.CNAME  FROM "+plant+"_FINCUSTOMERCREDITNOTEHDR AS CN LEFT JOIN "+plant+"_CUSTMST AS CM ON CM.CUSTNO = CN.CUSTNO WHERE CN.CREDITNOTE = hdr.TRANSACTION_ID),'-')\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'EXPENSE' THEN '-'\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'EXPENSE PAID' THEN '-'\r\n" + 
					"ELSE '-'END AS NAME, \r\n" + 
					"CASE    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'INVOICE' THEN '-'    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'SALESPAYMENT' THEN ISNULL((SELECT DEPOSIT_TO FROM "+plant+"_FINRECEIVEHDR WHERE ID = hdr.TRANSACTION_ID),'-')	\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'PURCHASEPAYMENT' THEN ISNULL((SELECT PAID_THROUGH FROM "+plant+"_FINPAYMENTHDR WHERE ID = hdr.TRANSACTION_ID),'-')\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'BILL' THEN '-' \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' THEN '-'    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' THEN '-' \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'EXPENSE' THEN '-'  \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'EXPENSE PAID' THEN '-'  \r\n" + 
					"ELSE '-'END AS PAIDTO \r\n" +
					"from ["+plant+"_FINJOURNALDET] jd inner join ["+plant+"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+plant+"_FINACCOUNTDETAILTYPE] ad on ad.ID=ca.ACCOUNTDETAILTYPE \r\n" + 
					"inner join ["+plant+"_FINJOURNALHDR] hdr on hdr.ID=jd.JOURNALHDRID where ca.ACCOUNTDETAILTYPE in (8,9) \r\n" + 
					"AND (hdr.TRANSACTION_TYPE = 'EXPENSE' OR hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' OR hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' OR hdr.TRANSACTION_TYPE = 'BILL' OR \r\n" + 
					"hdr.TRANSACTION_TYPE = 'PURCHASEPAYMENT' OR hdr.TRANSACTION_TYPE = 'SALESPAYMENT' OR hdr.TRANSACTION_TYPE = 'INVOICE' OR hdr.TRANSACTION_TYPE = 'EXPENSE PAID' ) "+
					"AND CONVERT(DATETIME, hdr.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "'";
			
			
			if (Account != null && !Account.equalsIgnoreCase("")) {
				aQuery += " AND jd.ACCOUNT_NAME = '" + Account + "'";
			}
 			
			aQuery += " order by hdr.ID desc";
			
			al = expenseDao.selectForReport(aQuery.toString(), htData, extraCon);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getPaymentReceiptSummaryFromJournlForDashboard(String plant, String fromDate, String toDate,String Account ,String numberOfDecimal) throws Exception {
		ArrayList al = null;
		Hashtable htData = new Hashtable();
		String extraCon = "";
		try {
			
			String aQuery = "select hdr.JOURNAL_DATE,hdr.TRANSACTION_ID,jd.ACCOUNT_NAME,CAST(((ISNULL(jd.DEBITS, 0))) AS DECIMAL(18,"+numberOfDecimal+"))AS TOTAL_AMOUNT, \r\n" + 
					"CASE    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'INVOICE' THEN ISNULL((SELECT IVC.CNAME FROM "+plant+"_FININVOICEHDR AS IV LEFT JOIN "+plant+"_CUSTMST AS IVC ON IVC.CUSTNO = IV.CUSTNO WHERE IV.INVOICE = hdr.TRANSACTION_ID),'-')    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'SALESPAYMENT' THEN ISNULL((SELECT RVC.CNAME FROM "+plant+"_FINRECEIVEHDR AS RV LEFT JOIN "+plant+"_CUSTMST AS RVC ON RVC.CUSTNO = RV.CUSTNO WHERE RV.ID = hdr.TRANSACTION_ID),'-')	\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'PURCHASEPAYMENT' THEN ISNULL((SELECT PVC.VNAME FROM "+plant+"_FINPAYMENTHDR AS PV LEFT JOIN "+plant+"_VENDMST AS PVC ON PV.VENDNO = PVC.VENDNO WHERE PV.ID = hdr.TRANSACTION_ID),'-')    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'BILL' THEN ISNULL((SELECT VM.VNAME FROM "+plant+"_FINBILLHDR AS BI LEFT JOIN "+plant+"_VENDMST AS VM ON BI.VENDNO = VM.VENDNO WHERE BI.BILL = hdr.TRANSACTION_ID),'-')    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' THEN ISNULL((SELECT VM.VNAME FROM "+plant+"_FINVENDORCREDITNOTEHDR AS SD LEFT JOIN "+plant+"_VENDMST AS VM ON SD.VENDNO = VM.VENDNO WHERE SD.ID = hdr.TRANSACTION_ID),'-')	\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' THEN ISNULL((SELECT CM.CNAME  FROM "+plant+"_FINCUSTOMERCREDITNOTEHDR AS CN LEFT JOIN "+plant+"_CUSTMST AS CM ON CM.CUSTNO = CN.CUSTNO WHERE CN.CREDITNOTE = hdr.TRANSACTION_ID),'-')\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'EXPENSE' THEN '-'\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'EXPENSE PAID' THEN '-'\r\n" + 
					"ELSE '-'END AS NAME, \r\n" + 
					"CASE    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'INVOICE' THEN '-'    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'SALESPAYMENT' THEN ISNULL((SELECT DEPOSIT_TO FROM "+plant+"_FINRECEIVEHDR WHERE ID = hdr.TRANSACTION_ID),'-')	\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'PURCHASEPAYMENT' THEN ISNULL((SELECT PAID_THROUGH FROM "+plant+"_FINPAYMENTHDR WHERE ID = hdr.TRANSACTION_ID),'-')\r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'BILL' THEN '-' \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' THEN '-'    \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' THEN '-' \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'EXPENSE' THEN '-'  \r\n" + 
					"WHEN hdr.TRANSACTION_TYPE = 'EXPENSE PAID' THEN '-'  \r\n" + 
					"ELSE '-'END AS DEPOSITTO \r\n" + 
					"from ["+plant+"_FINJOURNALDET] jd inner join ["+plant+"_FINCHARTOFACCOUNTS] ca on ca.ID=jd.ACCOUNT_ID inner join ["+plant+"_FINACCOUNTDETAILTYPE] ad on ad.ID=ca.ACCOUNTDETAILTYPE \r\n" + 
					"inner join ["+plant+"_FINJOURNALHDR] hdr on hdr.ID=jd.JOURNALHDRID where ca.ACCOUNTDETAILTYPE in (8,9) \r\n" + 
					"AND (hdr.TRANSACTION_TYPE = 'EXPENSE' OR hdr.TRANSACTION_TYPE = 'SUPPLIERCREDITNOTES' OR hdr.TRANSACTION_TYPE = 'CUSTOMERCREDITNOTES' OR hdr.TRANSACTION_TYPE = 'BILL' OR \r\n" + 
					"hdr.TRANSACTION_TYPE = 'PURCHASEPAYMENT' OR hdr.TRANSACTION_TYPE = 'SALESPAYMENT' OR hdr.TRANSACTION_TYPE = 'INVOICE' OR hdr.TRANSACTION_TYPE = 'EXPENSE PAID' ) "+
					"AND CONVERT(DATETIME, hdr.JOURNAL_DATE, 103) BETWEEN '" + fromDate + "' AND '" + toDate + "'";
			
			
			if (Account != null && !Account.equalsIgnoreCase("")) {
				aQuery += " AND jd.ACCOUNT_NAME = '" + Account + "'";
			}
 			
			aQuery += " order by hdr.ID desc";
			
			al = expenseDao.selectForReport(aQuery.toString(), htData, extraCon);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public List getExpensesByVendno(Hashtable ht, String plant) {
		List arrList = new ArrayList();
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			expenseDao.setmLogger(mLogger);  
			sql = new StringBuffer("select A.ID,EXPENSES_DATE,A.CUSTNO,A.VENDNO,'' as EXPENSES_ACCOUNT,PAID_THROUGH,ISNULL(PONO,'') PONO,ISNULL(EXBILL,'') EXBILL,ISNULL(SHIPMENT_CODE,'') SHIPMENT_CODE,");
	           sql.append(" ISNULL((select ISNULL(VNAME,'') FROM " + plant +"_VENDMST V WHERE VENDNO=A.VENDNO),'') as VNAME,REFERENCE,");
	           sql.append(" ISNULL((SELECT TOP 1 B.CURRENCYTOBASE FROM " + plant +"_FINEXPENSESDET B WHERE B.EXPENSESHDRID=A.ID),1) CURRENCYTOBASE,");
	           sql.append(" STATUS,ISNULL(CURRENCYID,'') CURRENCYID,cast(SUB_TOTAL as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as SUB_TOTAL_AMOUNT,cast(TOTAL_AMOUNT as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,ISNULL((select ISNULL(CNAME,'') FROM " + plant +"_CUSTMST V WHERE V.CUSTNO=A.CUSTNO),'') as CNAME");
	           sql.append(" from " + plant +"_FINEXPENSESHDR A WHERE A.PLANT='"+ plant+"' AND ISNULL(A.PAYMENT_STATUS,'') != 'Paid' AND A.ispaid != '1'");
		   if (ht.get("VENDNO") != null) {
			   sql.append(" AND A.VENDNO = '"+ht.get("VENDNO")+"' ");
		   }
		   arrList = expenseDao.selectForReport(sql.toString(), htData, "");
		}catch (Exception e) {
			this.mLogger.exception(this.printLog,"Exception :repportUtil :: getExpensesByCustomer:", e);
		}
		return arrList;
	}

	
	public boolean updateexpHdr(String query, Hashtable htCondition, String extCond)
		     throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
		     con = com.track.gates.DbBean.getConnection();
		     StringBuffer sql = new StringBuffer(" UPDATE " + "["
		                     + htCondition.get("PLANT") + "_FINEXPENSESHDR]");
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
		     if (con != null) {
		             DbBean.closeConnection(con);
		     }
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
	
	public boolean isExisitBillSupplier(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "["+ht.get("PLANT")+"_FINEXPENSESHDR]");
			sql.append(" WHERE  " + formCondition(ht));

			this.mLogger.query(this.printLog, sql.toString());

			flag = expenseDao.isExists(con, sql.toString());

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
	
}
