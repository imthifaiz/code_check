package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.db.util.CustUtil;
import com.track.db.util.CustUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;

public class WmsUploadTransferAssigneeSheet  implements WmsTran, IMLogger {
	
	 DateUtils dateUtils = null;
	 CustUtil custUtil =null;
     ItemSesBeanDAO itemsesdao = null;

	public WmsUploadTransferAssigneeSheet() {
		
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
		MLogger.log(0, "2.insertTransferAssingeesheet -  Stage : 1");
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
            ToHdrDAO ToHdrDAO = new ToHdrDAO();
            ToDetDAO ToDetDAO = new ToDetDAO();
            RecvDetDAO RecvDetDAO = new RecvDetDAO();
            try {
                    this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
               
            
            boolean result = false;
                Hashtable htcs = new Hashtable();
                Hashtable htcdn = new Hashtable();
                htcs.clear();
            
               
                htcs.put("PLANT", (String)map.get("PLANT"));
                htcs.put(IDBConstants.ASSIGNENO, (String) map.get(IDBConstants.ASSIGNENO));
                htcs.put(IDBConstants.ASSIGNENAME, (String) map.get(IDBConstants.ASSIGNENAME));
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
                htcdn.put(IDBConstants.ASSIGNENO, (String) map.get(IDBConstants.ASSIGNENO));
                result=custUtil.isExistToAssignee((String) map.get(IDBConstants.ASSIGNENO),(String) map.get("PLANT"));
                if(result==true)
                {
                	flag = custUtil.updateToAssignee(htcs,htcdn);
                	
                	Hashtable httohdr = new Hashtable();
                  	httohdr.put("CustCode", (String) map.get(IDBConstants.ASSIGNENO));
                  	httohdr.put(IConstants.PLANT, map.get("PLANT"));
        			
        			StringBuffer updatetohdr = new StringBuffer("set ");
        			updatetohdr.append(IConstants.IN_CUST_CODE + " = '" + (String) map.get(IDBConstants.ASSIGNENO) + "'");
        			updatetohdr.append("," +IConstants.IN_CUST_NAME + " = '" + (String) map.get(IDBConstants.ASSIGNENAME) + "'");
        			updatetohdr.append("," + IConstants.IN_PERSON_IN_CHARGE + " = '"+ (String) map.get(IDBConstants.NAME) + "'");
        			updatetohdr.append(",Address = '"+ (String) map.get(IDBConstants.ADDRESS1) + "'");
        			updatetohdr.append(",Address2 = '"+ (String) map.get(IDBConstants.ADDRESS2) + "'");
        			updatetohdr.append(",Address3 = '"+ (String) map.get(IDBConstants.ADDRESS3) + "'");
                  
        			Hashtable httodet = new Hashtable();
        			httodet.put(IConstants.PLANT, map.get("PLANT")); 
        			
        			String todetquery = "select Count(*) from "+map.get("PLANT")+"_TODET where TONO in(select TONO from "+map.get("PLANT")+"_TOHDR where plant='"+map.get("PLANT")+"' and  CustCode='"+(String) map.get(IDBConstants.ASSIGNENO)+"' )";
        			String todetupdate = "set userfld3='"+(String) map.get(IDBConstants.ASSIGNENAME)+"' ";

        			String topickquery = "select Count(*) from "+map.get("PLANT")+"_TO_PICK where TONO in(select TONO from "+map.get("PLANT")+"_TOHDR where  plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.ASSIGNENO)+"' )";
        			String topickupdate = "set cname='"+(String) map.get(IDBConstants.ASSIGNENAME)+"' ";
        			
        			String recvdetquery = "select Count(*) from "+map.get("PLANT")+"_RECVDET where PONO in(select TONO from "+map.get("PLANT")+"_TOHDR where  plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.ASSIGNENO)+"' )";
        			String recvdetupdate = "set cname='"+(String) map.get(IDBConstants.ASSIGNENAME)+"' ";

        			Boolean updateflag = false;
        			updateflag = ToHdrDAO.isExisit(httohdr);
   				 	if(updateflag){
   				 		updateflag = ToHdrDAO.update(updatetohdr.toString(), httohdr, " ");
   				 	}
   				 	if(updateflag){
   				 		updateflag = ToDetDAO.isExisit(todetquery);
   				 		if(updateflag)
   				 		{
   				 			updateflag = ToDetDAO.update(todetupdate, httodet, " and TONO in(select TONO from "+map.get("PLANT")+"_TOHDR where plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.ASSIGNENO)+"' )");
   				 		}
   				 	}
   				 	if(updateflag){
   				 		updateflag = ToDetDAO.isExisit(topickquery);
   				 		if(updateflag)
   				 		{
   				 			updateflag = ToDetDAO.updateToPickTable(topickupdate, httodet, " and TONO in(select TONO from "+map.get("PLANT")+"_TOHDR where plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.ASSIGNENO)+"' )");
   				 		}
   				 	}
   				   if(updateflag){
   					   updateflag = RecvDetDAO.isExisit(recvdetquery);
					  if(updateflag)
					  {
						  updateflag = RecvDetDAO.update(recvdetupdate, httodet, " and PONO in(select TONO from "+map.get("PLANT")+"_TOHDR where plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.ASSIGNENO)+"' )", map.get("PLANT").toString());
					  }
				  }
   				 
                }else{
                flag = custUtil.insertToAssignee(htcs);
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
                    htmov.put(IDBConstants.DIRTYPE, TransactionConstants.CNT_TO_ASSIGNEE_UPLOAD);
                    htmov.put(IDBConstants.CUSTOMER_CODE, map.get(IDBConstants.ASSIGNENO));
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
