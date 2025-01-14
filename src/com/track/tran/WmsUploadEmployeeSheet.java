package com.track.tran;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.CoaDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.JournalDAO;
import com.track.dao.MovHisDAO;
import com.track.db.util.CoaUtil;
import com.track.db.util.EmployeeUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;

public class WmsUploadEmployeeSheet implements WmsTran, IMLogger{
	
	DateUtils dateUtils = null;
	 EmployeeUtil empUtil =null;
    ItemSesBeanDAO itemsesdao = null;

	public WmsUploadEmployeeSheet()  {
		
		dateUtils = new DateUtils();
		empUtil=new EmployeeUtil();
	}

	private MLogger mLogger = new MLogger();
	
	public MLogger getmLogger() {
		return mLogger;
	}
	
	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;
	
	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		MLogger.log(0, "2.insertEmployeesheet -  Stage : 1");
		flag = processCountSheet(m);
		MLogger.log(0, "processCountSheet() : Transaction : " + flag);
		if (flag == true)
			flag = processMovHis(m);

		return flag;
	}
	
	@SuppressWarnings("unchecked")
   public boolean processCountSheet(Map map) throws Exception {
           MLogger.log(1, this.getClass() + " processCountSheet()");
           boolean flag = false;

           try {
                   this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
              
           
           boolean result = false;
               Hashtable htcs = new Hashtable();
               Hashtable htcdn = new Hashtable();
               htcs.clear();
               htcs.put("PLANT", (String)map.get("PLANT"));
               htcs.put(IDBConstants.EMPNO ,  (String) map.get(IDBConstants.EMPNO));
               htcs.put(IDBConstants.FNAME, (String) map.get(IDBConstants.FNAME));
               htcs.put(IDBConstants.EMPUSERID, (String) map.get(IDBConstants.EMPUSERID));   
               htcs.put(IDBConstants.PASSWORD_EMP, (String) map.get(IDBConstants.PASSWORD_EMP));   
               htcs.put(IDBConstants.EMPLOYEETYPEID, (String) map.get(IDBConstants.EMPLOYEETYPEID));  
               htcs.put(IDBConstants.GENDER, (String) map.get(IDBConstants.GENDER));
               htcs.put(IDBConstants.DOB, (String) map.get(IDBConstants.DOB));
               htcs.put(IDBConstants.TELNO, (String) map.get(IDBConstants.TELNO));
               htcs.put(IDBConstants.EMAIL, (String) map.get(IDBConstants.EMAIL));
               htcs.put(IDBConstants.PASSPORTNUMBER, (String) map.get(IDBConstants.PASSPORTNUMBER));
               htcs.put(IDBConstants.REPORTING_INCHARGE, (String) map.get(IDBConstants.REPORTING_INCHARGE));
               htcs.put(IDBConstants.OUTLETS_CODE, (String) map.get(IDBConstants.OUTLETS_CODE));
               htcs.put(IDBConstants.COUNTRYOFISSUE, (String) map.get(IDBConstants.COUNTRYOFISSUE));
               htcs.put(IDBConstants.PASSPORTEXPIRYDATE, (String) map.get(IDBConstants.PASSPORTEXPIRYDATE));
               htcs.put(IDBConstants.COUNTRY, (String) map.get(IDBConstants.COUNTRY));
               htcs.put(IDBConstants.UNITNO, (String) map.get(IDBConstants.ADDRESS1));
               htcs.put(IConstants.BUILDING, (String) map.get(IDBConstants.ADDRESS2));
               htcs.put(IDBConstants.STREET, (String) map.get(IDBConstants.ADDRESS3));
               htcs.put(IDBConstants.CITY, (String) map.get(IDBConstants.ADDRESS4));
               htcs.put(IDBConstants.STATE, (String) map.get(IDBConstants.STATE));               
               htcs.put(IConstants.ZIP, map.get(IDBConstants.ZIP));
               htcs.put(IDBConstants.FACEBOOK, (String) map.get(IDBConstants.FACEBOOK));
               htcs.put(IDBConstants.TWITTER, (String) map.get(IDBConstants.TWITTER));
               htcs.put(IDBConstants.LINKEDIN, (String) map.get(IDBConstants.LINKEDIN));
               htcs.put(IDBConstants.SKYPE, (String) map.get(IDBConstants.SKYPE));
               htcs.put(IDBConstants.EMIRATESID, (String) map.get(IDBConstants.EMIRATESID));
               htcs.put(IDBConstants.EMIRATESIDEXPIRY, (String) map.get(IDBConstants.EMIRATESIDEXPIRY));
               htcs.put(IDBConstants.VISANUMBER, (String) map.get(IDBConstants.VISANUMBER));
               htcs.put(IDBConstants.VISAEXPIRYDATE, (String) map.get(IDBConstants.VISAEXPIRYDATE));
               htcs.put(IDBConstants.DEPTARTMENT, (String) map.get(IDBConstants.DEPTARTMENT));
               htcs.put(IDBConstants.DESGINATION, (String) map.get(IDBConstants.DESGINATION));
               htcs.put("ISCASHIER", (String) map.get("ISCASHIER"));
               htcs.put("ISSALESMAN", (String) map.get("ISSALESMAN"));
               htcs.put(IDBConstants.DATEOFJOINING, (String) map.get(IDBConstants.DATEOFJOINING));
               htcs.put(IDBConstants.DATEOFLEAVING, (String) map.get(IDBConstants.DATEOFLEAVING));
               htcs.put(IDBConstants.LABOURCARDNUMBER, (String) map.get(IDBConstants.LABOURCARDNUMBER));
               htcs.put(IDBConstants.WORKPERMITNUMBER, (String) map.get(IDBConstants.WORKPERMITNUMBER));
               htcs.put(IDBConstants.CONTRACTSTARTDATE, (String) map.get(IDBConstants.CONTRACTSTARTDATE));
               htcs.put(IDBConstants.CONTRACTENDDATE, (String) map.get(IDBConstants.CONTRACTENDDATE));
               htcs.put(IDBConstants.IBAN, (String) map.get(IDBConstants.IBAN));
               htcs.put(IDBConstants.BANKNAME, (String) map.get(IDBConstants.BANKNAME));
               htcs.put(IDBConstants.BANKROUTINGCODE, (String) map.get(IDBConstants.BANKROUTINGCODE));
               /*htcs.put(IDBConstants.BASICSALARY, (String) map.get(IDBConstants.BASICSALARY));                                     
               htcs.put(IDBConstants.HOUSERENTALLOWANCE, (String) map.get(IDBConstants.HOUSERENTALLOWANCE));
               htcs.put(IDBConstants.TRANSPORTALLOWANCE, (String) map.get(IDBConstants.TRANSPORTALLOWANCE));
               htcs.put(IDBConstants.COMMUNICATIONALLOWANCE, (String) map.get(IDBConstants.COMMUNICATIONALLOWANCE));                                     
               htcs.put(IDBConstants.OTHERALLOWANCE, (String) map.get(IDBConstants.OTHERALLOWANCE));
               htcs.put(IDBConstants.BONUS, (String) map.get(IDBConstants.BONUS));
               htcs.put(IDBConstants.COMMISSION, (String) map.get(IDBConstants.COMMISSION));*/
               htcs.put(IDBConstants.GRATUITY, (String) map.get(IDBConstants.GRATUITY));                                     
               htcs.put(IDBConstants.AIRTICKET, (String) map.get(IDBConstants.AIRTICKET));
               htcs.put(IDBConstants.LEAVESALARY, (String) map.get(IDBConstants.LEAVESALARY));
               htcs.put(IConstants.REMARKS, map.get(IDBConstants.REMARKS));
               htcs.put(IConstants.ISACTIVE, map.get(IDBConstants.ISACTIVE));
               htcdn.put(IDBConstants.PLANT, map.get("PLANT"));
               htcdn.put(IDBConstants.EMPNO , (String) map.get(IDBConstants.EMPNO));
               Hashtable ht = new Hashtable();
               ht.put(IDBConstants.PLANT,(String) map.get(IDBConstants.PLANT));
               ht.put(IDBConstants.EMPNO,(String) map.get(IDBConstants.EMPNO));
               String EMPLOYEE_NAME = (String) map.get(IDBConstants.FNAME);
               String OLD_EMPLOYEE_NAME=new EmployeeDAO().getEmpname((String)map.get("PLANT"), (String) map.get(IDBConstants.EMPNO),"");
				
               result=empUtil.isExistsEmployee(ht);
               if(result==true)
               {
               flag = empUtil.updateEmployeeMst(htcs,htcdn);
               if(!EMPLOYEE_NAME.equalsIgnoreCase(OLD_EMPLOYEE_NAME))
  				{
            	   Hashtable htJournalDet = new Hashtable();
                   htJournalDet.put("ACCOUNT_NAME", (String) map.get(IDBConstants.EMPNO)+"-"+OLD_EMPLOYEE_NAME);
                   htJournalDet.put(IConstants.PLANT, (String)map.get("PLANT"));
       			
                   String updateJournalDet ="set ACCOUNT_NAME='"+(String) map.get(IDBConstants.EMPNO)+"-"+EMPLOYEE_NAME+"'"; 
                   
                 //FINCHARTOFACCOUNTS & FINJOURNALDET UPDATE
   				boolean updateflag = new CoaDAO().isExisitcoaAccount(htJournalDet);
   				 if(updateflag){
   					updateflag = new CoaDAO().updatecoaAccount(updateJournalDet.toString(), htJournalDet, " ");
   					 }
   				updateflag = new JournalDAO().isExisitJournalDet(htJournalDet);
   				 if(updateflag){
   					updateflag = new JournalDAO().updateJournalDet(updateJournalDet.toString(), htJournalDet, " ");
   					 }
  				}
               }else{
	               flag = empUtil.insertEmployeeMst(htcs);
	               if(flag) {
	            	   CoaUtil coaUtil = new CoaUtil();
	              	   CoaDAO coaDAO = new CoaDAO();
	              	   DateUtils dateutils = new DateUtils();
	              	   String accname = (String) map.get(IDBConstants.EMPNO)+"-"+(String) map.get(IDBConstants.FNAME);
	   								
		   				Hashtable<String, String> accountHt = new Hashtable<>();
		   				accountHt.put("PLANT", (String)map.get("PLANT"));
		   				accountHt.put("ACCOUNTTYPE", "6");
		   				accountHt.put("ACCOUNTDETAILTYPE", "20");
		   				accountHt.put("ACCOUNT_NAME", accname);
		   				accountHt.put("DESCRIPTION", "");
		   				accountHt.put("ISSUBACCOUNT", "1");
		   				accountHt.put("SUBACCOUNTNAME", "38");
		   				accountHt.put("OPENINGBALANCE", "");
		   				accountHt.put("OPENINGBALANCEDATE", "");
		   				accountHt.put("CRAT", dateutils.getDateTime());
		   				accountHt.put("CRBY", (String)map.get("LOGIN_USER"));
		   				accountHt.put("UPAT", dateutils.getDateTime());
		   				
		   				String Acode = coaDAO.GetAccountCodeByID("38", (String)map.get("PLANT"));
		   				List<Map<String, String>> subaccount = coaDAO.getMaxSubCode((String)map.get("PLANT"), "38");
		   				String scode ="";
		   				String atcode ="";
		   				if(subaccount.size() > 0) {
		   					for (int i = 0; i < subaccount.size(); i++) {
		   						Map<String, String> m = subaccount.get(i);
		   						scode = m.get("CODE");
		   					}
		   					if(scode == null){
		   						atcode = "01";
		   					}else{
		   						int count = Integer.valueOf(scode);
		   						atcode = String.valueOf(count+1);
		   						if(atcode.length() == 1) {
		   							atcode = "0"+atcode;
		   						}
		   					}
		   				}else {
		   					atcode = "01";
		   				}
		   				String accountcode = Acode +"-"+atcode;
		   				accountHt.put("ACCOUNT_CODE", accountcode);
		   				accountHt.put("SUB_CODE", atcode);
		   				
		   				coaUtil.addAccount(accountHt, (String)map.get("PLANT"));
	               }
               }


           } catch (Exception e) {
                   MLogger.log(-1, "Exception :: " + e.getMessage());
                   throw e;
           }
           MLogger.log(-1, this.getClass() + " processCountSheet()");
           return flag;
   }

   @SuppressWarnings("unchecked")
   public boolean processMovHis(Map map) throws Exception {
           boolean flag = false;
           MovHisDAO movHisDao = new MovHisDAO();
           this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
           movHisDao.setmLogger(mLogger);
           try {
        	   
        	  
        	   
                   Hashtable htmov = new Hashtable();
                   htmov.clear();
                   htmov.put(IDBConstants.PLANT, map.get("PLANT"));
                   htmov.put(IDBConstants.DIRTYPE, TransactionConstants.CNT_EMPLOYEE_UPLOAD );
                   htmov.put(IDBConstants.CUSTOMER_CODE, map.get(IDBConstants.EMPNO));
                   htmov.put(IDBConstants.MOVTID, "");
                   htmov.put(IDBConstants.RECID, "");
                   htmov.put(IDBConstants.PONO, "");
                   htmov.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
                   htmov.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
                   htmov.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
                   flag = movHisDao.insertIntoMovHis(htmov);

           } catch (Exception e) {
                   throw e;
           }
           return flag;
   }


}
