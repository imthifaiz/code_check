package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.JournalDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoEstDetDAO;
import com.track.dao.PoEstHdrDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.VendMstDAO;
import com.track.dao.multiPoEstDetDAO;
import com.track.db.util.CoaUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.VendUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;

import net.sf.json.JSONObject;

import com.track.dao.CoaDAO;
import com.track.dao.CustomerBeanDAO;

public class WmsUploadSupplerSheet implements WmsTran, IMLogger {
	
	DateUtils dateUtils = null;
	VendUtil vendUtil = null;
        CustUtil custUtil =null;
        
	ItemSesBeanDAO itemsesdao = null;
      
	public WmsUploadSupplerSheet() {
		vendUtil = new VendUtil();
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
		MLogger.log(0, "2.insertSuppliersheet -  Stage : 1");
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
            PoHdrDAO PoHdrDAO = new  PoHdrDAO();
        	PoDetDAO PoDetDAO = new  PoDetDAO();
        	RecvDetDAO RecvDetDAO = new RecvDetDAO();
        	CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
            try {
                    this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
               
            
            boolean result = false;
                Hashtable htcs = new Hashtable();
                Hashtable htcdn = new Hashtable();
                htcs.clear();
            
               
                htcs.put("PLANT", (String)map.get("PLANT"));
                htcs.put(IDBConstants.VENDOR_CODE, (String) map.get(IDBConstants.VENDOR_CODE));
                htcs.put(IDBConstants.VENDOR_NAME, (String) map.get(IDBConstants.VENDOR_NAME));
                htcs.put(IDBConstants.companyregnumber, (String) map.get(IDBConstants.companyregnumber));
                htcs.put("CURRENCY_ID" , (String) map.get(IDBConstants.CURRENCYID)); //Author Name: Resvi , Date: 12/07/2021 
                htcs.put(IDBConstants.RCBNO, (String) map.get(IDBConstants.RCBNO));
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
                htcs.put(IConstants.payment_terms, map.get(IConstants.payment_terms));
                htcs.put(IConstants.PAYTERMS, map.get(IConstants.PAYTERMS));
                htcs.put(IConstants.PAYINDAYS, map.get(IConstants.PAYINDAYS));
                htcs.put(IDBConstants.SUPPLIERTYPEID, (String) map.get(IDBConstants.SUPPLIERTYPEID));
                htcs.put(IConstants.TRANSPORTID, (String) map.get(IConstants.TRANSPORTID));
                   
                htcs.put(IConstants.CUSTOMEREMAIL, map.get(IConstants.CUSTOMEREMAIL));
                htcs.put(IConstants.WORKPHONE, map.get(IConstants.WORKPHONE));
                htcs.put(IConstants.FACEBOOK, map.get(IConstants.FACEBOOK));
                htcs.put(IConstants.LINKEDIN, map.get(IConstants.LINKEDIN));
                htcs.put(IConstants.TWITTER, map.get(IConstants.TWITTER));
                htcs.put(IConstants.SKYPE, map.get(IConstants.SKYPE));
                htcs.put(IConstants.WEBSITE, map.get(IConstants.WEBSITE));
                htcs.put("ISPEPPOL", map.get("ISPEPPOL"));
                htcs.put("PEPPOL_ID", map.get("PEPPOL_ID"));
//                htcs.put(IConstants.OPENINGBALANCE, map.get(IConstants.OPENINGBALANCE));
                htcs.put(IConstants.TAXTREATMENT, map.get(IConstants.TAXTREATMENT));
                htcs.put(IDBConstants.CREATED_BY, map.get(IConstants.CREATED_BY));
                htcs.put(IDBConstants.CREATED_AT, map.get(IConstants.CREATED_AT));
                htcs.put(IDBConstants.IBAN, (String) map.get(IDBConstants.IBAN));
                htcs.put(IDBConstants.BANKNAME, (String) map.get(IDBConstants.BANKNAME));
                htcs.put(IDBConstants.BANKROUTINGCODE, (String) map.get(IDBConstants.BANKROUTINGCODE));
                htcdn.put(IDBConstants.PLANT, map.get("PLANT"));
                htcdn.put(IDBConstants.VENDOR_CODE, (String) map.get(IDBConstants.VENDOR_CODE));
                String OLD_VENDOR_NAME = (String) map.get(IDBConstants.VENDOR_NAME);
                String VENDOR_NAME = (String) map.get(IDBConstants.VENDOR_NAME);
                JSONObject vendorJson=new VendMstDAO().getVendorName((String)map.get("PLANT"), (String) map.get(IDBConstants.VENDOR_CODE));
				if(!vendorJson.isEmpty()) {
					OLD_VENDOR_NAME = vendorJson.getString("VNAME");
				}
                result=vendUtil.isExistsVendorMst(htcdn);
                if(result==true)
                {
                	Hashtable ht = new Hashtable();
        			ht.put(IDBConstants.PLANT, map.get("PLANT"));
                	if(!customerBeanDAO.isExistsSupplier(ht,
        					" VENDNO <> '"+ (String) map.get(IDBConstants.VENDOR_CODE) + "'  AND VNAME = '" + (String) map.get(IDBConstants.VENDOR_NAME) + "'"))
        			{
                	flag = custUtil.updateVendor(htcs,htcdn);
                	
                	Hashtable htpohdr = new Hashtable();
        			htpohdr.put("CustCode", (String) map.get(IDBConstants.VENDOR_CODE));
        			htpohdr.put(IConstants.PLANT, map.get("PLANT"));
        			
        			Hashtable htJournalDet = new Hashtable();
                    htJournalDet.put("ACCOUNT_NAME", (String) map.get(IDBConstants.VENDOR_CODE)+"-"+OLD_VENDOR_NAME);
                    htJournalDet.put(IConstants.PLANT, (String)map.get("PLANT"));
        			
                    String updateJournalDet ="set ACCOUNT_NAME='"+(String) map.get(IDBConstants.VENDOR_CODE)+"-"+VENDOR_NAME+"'"; 
        			
        			StringBuffer updatepohdr = new StringBuffer("set ");
        			updatepohdr.append(IConstants.IN_CUST_CODE + " = '" + (String) map.get(IDBConstants.VENDOR_CODE) + "'");
        			updatepohdr.append("," +IConstants.IN_CUST_NAME + " = '" + (String) map.get(IDBConstants.VENDOR_NAME) + "'");
        			updatepohdr.append("," + IConstants.IN_PERSON_IN_CHARGE + " = '"+ (String) map.get(IDBConstants.NAME) + "'");
        			updatepohdr.append(",Address = '"+ (String) map.get(IDBConstants.ADDRESS1) + "'");
        			updatepohdr.append(",Address2 = '"+ (String) map.get(IDBConstants.ADDRESS2) + "'");
        			updatepohdr.append(",Address3 = '"+ (String) map.get(IDBConstants.ADDRESS3) + "'");
                    
        			Hashtable htpodet = new Hashtable();
        			htpodet.put(IConstants.PLANT, map.get("PLANT")); 
        			
        			String podetquery = "select Count(*) from "+map.get("PLANT")+"_PODET where PONO in(select PONO from "+map.get("PLANT")+"_POHDR where plant='"+map.get("PLANT")+"' and  CustCode='"+(String) map.get(IDBConstants.VENDOR_CODE)+"' )";
        			String podetupdate = "set userfld3='"+(String) map.get(IDBConstants.VENDOR_NAME)+"' ";

        			String recvdetquery = "select Count(*) from "+map.get("PLANT")+"_RECVDET where PONO in(select PONO from "+map.get("PLANT")+"_POHDR where  plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.VENDOR_CODE)+"' )";
        			String recvdetupdate = "set cname='"+(String) map.get(IDBConstants.VENDOR_NAME)+"' ";
        			
        			String poestdetquery = "select Count(*) from "+(String)map.get("PLANT")+"_POESTDET where POESTNO in(select POESTNO from "+(String)map.get("PLANT")+"_POESTHDR where plant='"+(String)map.get("PLANT")+"' and  CustCode='"+(String) map.get(IDBConstants.VENDOR_CODE)+"' )";
        			String poestdetupdate = "set userfld3='"+(String) map.get(IDBConstants.VENDOR_NAME)+"' ";
        			
        			String pomultiestdetupdate = "set CustName='"+(String) map.get(IDBConstants.VENDOR_NAME)+"' ";
        			
        			 boolean updateflag = false;
        			 
        			if(flag){
        			if(!VENDOR_NAME.equalsIgnoreCase(OLD_VENDOR_NAME))
       				{
        				updateflag = PoHdrDAO.isExisit(htpohdr);
       				 	if(updateflag){
       				 	updateflag = PoHdrDAO.updatePO(updatepohdr.toString(), htpohdr, " ");
       				 }
       				 if(updateflag){
       					updateflag = PoDetDAO.isExisit(podetquery);
       					 if(updateflag)
       					 {
       						updateflag = PoDetDAO.update(podetupdate, htpodet, " and PONO in(select PONO from "+map.get("PLANT")+"_POHDR where plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.VENDOR_CODE)+"' )");
       					 }
       				 }
       				 if(updateflag){
       					updateflag = RecvDetDAO.isExisit(recvdetquery);
       					 if(updateflag)
       					 {
       						updateflag = RecvDetDAO.update(recvdetupdate, htpodet, " and PONO in(select PONO from "+map.get("PLANT")+"_POHDR where plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.VENDOR_CODE)+"' )",(String) map.get("PLANT"));
       					 }
       				 }
       				updateflag =new PoEstHdrDAO().isExisit(htpohdr);//POEST
   				 if(updateflag){
   					updateflag =new PoEstHdrDAO().updatePO(updatepohdr.toString(), htpohdr, " ");
   				 }
   				 if(updateflag){
   					updateflag =new PoEstDetDAO().isExisit(poestdetquery);
   					 if(updateflag)
   					 {
   						updateflag =new PoEstDetDAO().update(poestdetupdate, htpodet, " and POESTNO in(select POESTNO from "+(String)map.get("PLANT")+"_POESTHDR where plant='"+(String)map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.VENDOR_CODE)+"' )");
   					 }
   				 }
   				updateflag =new multiPoEstDetDAO().isExisit(htpohdr);//POMULTIESTDET
   				 if(updateflag)
   				 {
   					updateflag =new multiPoEstDetDAO().update(pomultiestdetupdate, htpodet, " ");
   				 }
   				//FINCHARTOFACCOUNTS & FINJOURNALDET UPDATE
   				updateflag = new CoaDAO().isExisitcoaAccount(htJournalDet);
   				 if(updateflag){
   					updateflag = new CoaDAO().updatecoaAccount(updateJournalDet.toString(), htJournalDet, " ");
   					 }
   				updateflag = new JournalDAO().isExisitJournalDet(htJournalDet);
   				 if(updateflag){
   					updateflag = new JournalDAO().updateJournalDet(updateJournalDet.toString(), htJournalDet, " ");
   					 }
   				 }
       			}
        		} else {
        			
        			    throw new Exception("Supplier Name Exists already. Try again with diffrent Supplier Name");	
        				
        			}	
                }else{
                	if(!custUtil.isExistVendorName((String) map.get(IDBConstants.VENDOR_NAME), (String) map.get("PLANT")))
                	{
                		flag = custUtil.insertVendor(htcs);
                		
                		if(flag) {
                			CoaDAO coaDAO = new CoaDAO();
                			CoaUtil coaUtil = new CoaUtil();
            				DateUtils dateutils = new DateUtils();
            				/*Hashtable<String, String> accountHt = new Hashtable<>();
            				accountHt.put("PLANT", (String) map.get("PLANT"));
            				accountHt.put("ACCOUNTTYPE", "6");
            				accountHt.put("ACCOUNTDETAILTYPE", "18");
            				accountHt.put("ACCOUNT_NAME", (String) map.get(IDBConstants.VENDOR_CODE)+"-"+(String) map.get(IDBConstants.VENDOR_NAME));
            				accountHt.put("DESCRIPTION", "");
            				accountHt.put("ISSUBACCOUNT", "0");
            				accountHt.put("SUBACCOUNTNAME", "");
            				accountHt.put("OPENINGBALANCE", "");
            				accountHt.put("OPENINGBALANCEDATE", "");
            				accountHt.put("CRAT", dateutils.getDateTime());
            				accountHt.put("CRBY",(String) map.get(IConstants.CREATED_BY));
            				accountHt.put("UPAT", dateutils.getDateTime());
            				
            				String gcode = coaDAO.GetGCodeById("6", (String) map.get("PLANT"));
            				String dcode = coaDAO.GetDCodeById("18", (String) map.get("PLANT"));
            				List<Map<String, String>> listQry = coaDAO.getMaxAccoutCode((String) map.get("PLANT"), "6", "18");
            				String maxid = "";
            				String atcode = "";
            				if(listQry.size() > 0) {
            					for (int i = 0; i < listQry.size(); i++) {
            						Map<String, String> m = listQry.get(i);
            						maxid = m.get("CODE");
            					}
            				
            					int count = Integer.valueOf(maxid);
            					atcode = String.valueOf(count+1);
            					if(atcode.length() == 1) {
            						atcode = "0"+atcode;
            					}
            				}else {
            					atcode = "01";
            				}
            				String accountCode = gcode+"-"+dcode+atcode;
            				accountHt.put("ACCOUNT_CODE", accountCode);
            				accountHt.put("CODE", atcode);
            				
            				coaUtil.addAccount(accountHt, (String) map.get("PLANT"));
                		*/}
                	}
                	else {
            			
        			    throw new Exception("Supplier Name Exists already. Try again with diffrent Supplier Name");	
        				
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
                    htmov.put(IDBConstants.DIRTYPE, TransactionConstants.CNT_SUP_UPLOAD);
                    htmov.put(IDBConstants.VENDOR_CODE, map.get(IDBConstants.VENDOR_CODE));
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