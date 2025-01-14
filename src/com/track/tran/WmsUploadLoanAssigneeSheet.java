package com.track.tran;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.LoanDetDAO;
import com.track.dao.LoanHdrDAO;
import com.track.dao.MovHisDAO;
import com.track.db.util.CustUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.dao.RecvDetDAO;


public class WmsUploadLoanAssigneeSheet implements WmsTran, IMLogger{
	DateUtils dateUtils = null;
	CustUtil custUtil =null;
    ItemSesBeanDAO itemsesdao = null;
    
public WmsUploadLoanAssigneeSheet() {
		
		dateUtils = new DateUtils();
		custUtil=new CustUtil();
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
		MLogger.log(0, "2.insertLoanAssigneesheet -  Stage : 1");
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
            LoanHdrDAO LoanHdrDAO = new LoanHdrDAO();
        	LoanDetDAO LoanDetDAO = new LoanDetDAO();
        	RecvDetDAO RecvDetDAO = new RecvDetDAO();

            try {
                    this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
               
            
            boolean result = false;
                Hashtable htcs = new Hashtable();
                Hashtable htcdn = new Hashtable();
                htcs.clear();
            
               
                htcs.put("PLANT", (String)map.get("PLANT"));
                htcs.put(IDBConstants.LASSIGNNO, (String) map.get(IDBConstants.LASSIGNNO));
                htcs.put(IDBConstants.CUSTOMER_NAME, (String) map.get(IDBConstants.CUSTOMER_NAME));
                htcs.put(IDBConstants.NAME, (String) map.get(IDBConstants.NAME));
                htcs.put("COMMENT1", "");
                htcs.put(IDBConstants.DESGINATION, (String) map.get(IDBConstants.DESGINATION));
                htcs.put(IDBConstants.TELNO, (String) map.get(IDBConstants.TELNO));
                htcs.put(IDBConstants.HPNO, (String) map.get(IDBConstants.HPNO));
                htcs.put(IDBConstants.FAX, (String) map.get(IDBConstants.FAX));
                htcs.put(IDBConstants.EMAIL, (String) map.get(IDBConstants.EMAIL));
                htcs.put(IDBConstants.ADDRESS1, (String) map.get(IDBConstants.ADDRESS1));
                htcs.put(IConstants.ADDRESS2, (String) map.get(IDBConstants.ADDRESS2));
                htcs.put(IDBConstants.ADDRESS3, (String) map.get(IDBConstants.ADDRESS3));
                htcs.put(IDBConstants.ADDRESS4, (String) map.get(IDBConstants.ADDRESS4));
                htcs.put(IDBConstants.STATE, (String) map.get(IDBConstants.STATE));
                htcs.put(IDBConstants.COUNTRY, (String) map.get(IDBConstants.COUNTRY));
                htcs.put(IConstants.ZIP, map.get(IDBConstants.ZIP));
                htcs.put(IConstants.REMARKS, map.get(IDBConstants.REMARKS));
                htcs.put(IConstants.ISACTIVE, map.get(IDBConstants.ISACTIVE));
    
                htcdn.put(IDBConstants.PLANT, map.get("PLANT"));
                htcdn.put(IDBConstants.LASSIGNNO, (String) map.get(IDBConstants.LASSIGNNO));
                result=custUtil.isExistLoanAssignee((String) map.get(IDBConstants.LASSIGNNO),(String) map.get("PLANT"));
                if(result==true)
                {
                	flag = custUtil.updateLoanAssignee(htcs,htcdn);
                
                	Hashtable htloanhdr = new Hashtable();
        			htloanhdr.put("CustCode", (String) map.get(IDBConstants.LASSIGNNO));
        			htloanhdr.put(IConstants.PLANT, map.get("PLANT"));
        			
        			StringBuffer updateloanhdr = new StringBuffer("set ");
        			updateloanhdr.append(IConstants.IN_CUST_CODE + " = '" + (String) map.get(IDBConstants.LASSIGNNO) + "'");
        			updateloanhdr.append("," +IConstants.IN_CUST_NAME + " = '" + (String) map.get(IDBConstants.CUSTOMER_NAME) + "'");
        			updateloanhdr.append("," + IConstants.IN_PERSON_IN_CHARGE + " = '"+ (String) map.get(IDBConstants.NAME) + "'");
        			updateloanhdr.append(",Address = '"+ (String) map.get(IDBConstants.ADDRESS1) + "'");
        			updateloanhdr.append(",Address2 = '"+ (String) map.get(IDBConstants.ADDRESS2) + "'");
        			updateloanhdr.append(",Address3 = '"+ (String) map.get(IDBConstants.ADDRESS3) + "'");
                    
        			Hashtable htloandet = new Hashtable();
        			htloandet.put(IConstants.PLANT, map.get("PLANT")); 
        			
        			String loandetquery = "select Count(*) from "+map.get("PLANT")+"_LOANDET where ORDNO in(select ORDNO from "+map.get("PLANT")+"_LOANHDR where plant='"+map.get("PLANT")+"' and  CustCode='"+(String) map.get(IDBConstants.LASSIGNNO)+"' )";
        			String loandetupdate = "set userfld3='"+(String) map.get(IDBConstants.CUSTOMER_NAME)+"' ";

        			String loanpickquery = "select Count(*) from "+map.get("PLANT")+"_LOAN_PICK where ORDNO in(select ORDNO from "+map.get("PLANT")+"_LOANHDR where  plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.LASSIGNNO)+"' )";
        			String loanpickupdate = "set cname='"+(String) map.get(IDBConstants.CUSTOMER_NAME)+"' ";
        			
        			String recvdetquery = "select Count(*) from "+map.get("PLANT")+"_RECVDET where PONO in(select ORDNO from "+map.get("PLANT")+"_LOANHDR where  plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.LASSIGNNO)+"' )";
        			String recvdetupdate = "set cname='"+(String) map.get(IDBConstants.CUSTOMER_NAME)+"' ";

        			Boolean updateflag = false;
        			updateflag = LoanHdrDAO.isExisit(htloanhdr,"");
   				 	if(updateflag){
   				 		updateflag = LoanHdrDAO.update(updateloanhdr.toString(), htloanhdr, " ");
   				 	}
   				 	if(updateflag){
   				 		updateflag = LoanDetDAO.isExisit(loandetquery);
   				 		if(updateflag)
   				 		{
   				 			updateflag = LoanDetDAO.update(loandetupdate, htloandet, " and ORDNO in(select ORDNO from "+map.get("PLANT")+"_LOANHDR where plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.LASSIGNNO)+"' )");
   				 		}
   				 	}
   				 	if(updateflag){
   				 		updateflag = LoanDetDAO.isExisit(loanpickquery);
   				 		if(updateflag)
   				 		{
   				 			updateflag = LoanDetDAO.updateLOPickTable(loanpickupdate, htloandet, " and ORDNO in(select ORDNO from "+map.get("PLANT")+"_LOANHDR where plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.LASSIGNNO)+"' )");
   				 		}
   				 	}
   				 if(updateflag){
   					 updateflag = RecvDetDAO.isExisit(recvdetquery);
					 if(updateflag)
					 {
						 updateflag = RecvDetDAO.update(recvdetupdate, htloandet, " and PONO in(select ORDNO from "+map.get("PLANT")+"_LOANHDR where plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.LASSIGNNO)+"' )", map.get("PLANT").toString());
					 }
				 }
   				 

                }else{
                flag = custUtil.insertLoanAssignee(htcs);
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
                    htmov.put(IDBConstants.DIRTYPE, TransactionConstants.CNT_LOAN_ASSINGEE_UPLOAD);
                    htmov.put(IDBConstants.CUSTOMER_CODE, map.get(IDBConstants.LASSIGNNO));
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
