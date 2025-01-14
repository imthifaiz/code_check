
package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.CoaDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EstHdrDAO;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.JournalDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TransportModeDAO;
import com.track.dao.VendMstDAO;
import com.track.db.util.CoaUtil;
import com.track.db.util.CustUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;

import net.sf.json.JSONObject;

public class WmsUploadCustomerSheet implements WmsTran, IMLogger {
	
	DateUtils dateUtils = null;
	
        CustUtil custUtil =null;
        
	ItemSesBeanDAO itemsesdao = null;
      
	public WmsUploadCustomerSheet() {
		
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
		MLogger.log(0, "2.insertCustomersheet -  Stage : 1");
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
            DoHdrDAO DoHdrDAO = new  DoHdrDAO();
            DoDetDAO DoDetDAO = new  DoDetDAO();
            ShipHisDAO ShipHisDAO = new ShipHisDAO();
            EstHdrDAO EstHdrDAO = new EstHdrDAO();
            //WorkOrderHdrDAO WorkOrderHdrDAO = new WorkOrderHdrDAO();
            //WorkOrderDetDAO WorkOrderDetDAO = new WorkOrderDetDAO();
            CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();

            try {
                    this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
               
            
            boolean result = false;
                Hashtable htcs = new Hashtable();
                Hashtable htcdn = new Hashtable();
                htcs.clear();
            
               
                htcs.put("PLANT", (String)map.get("PLANT"));
                htcs.put(IDBConstants.CUSTOMER_CODE, (String) map.get(IDBConstants.CUSTOMER_CODE));
                htcs.put(IDBConstants.CUSTOMER_NAME, (String) map.get(IDBConstants.CUSTOMER_NAME));
                htcs.put(IConstants.ISSHOWBYPRODUCT, (String) map.get(IConstants.ISSHOWBYPRODUCT));
                htcs.put(IConstants.ISSHOWBYCATEGORY, (String) map.get(IConstants.ISSHOWBYCATEGORY));
                htcs.put(IDBConstants.companyregnumber, (String) map.get(IDBConstants.companyregnumber));
                htcs.put("CURRENCY_ID" , (String) map.get(IDBConstants.CURRENCYID)); //Author Name: Resvi , Date: 12/07/2021 
                htcs.put(IDBConstants.RCBNO, (String) map.get(IDBConstants.RCBNO));
                htcs.put(IDBConstants.CUSTOMERTYPEID, (String) map.get(IDBConstants.CUSTOMERTYPEID));
                htcs.put(IConstants.TRANSPORTID, (String) map.get(IConstants.TRANSPORTID));
                htcs.put(IDBConstants.NAME, (String) map.get(IDBConstants.NAME));
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

                String sameascontactaddress = (String)map.get(IConstants.SAME_AS_CONTACT_ADDRESS);
            	if ( sameascontactaddress .equals("1") ){
            	  htcs.put(IConstants.SHIP_CONTACTNAME, map.get(IDBConstants.NAME));
                  htcs.put(IConstants.SHIP_DESGINATION, map.get(IDBConstants.DESGINATION));
                  htcs.put(IConstants.SHIP_WORKPHONE, map.get(IDBConstants.WORKPHONE));
                  htcs.put(IConstants.SHIP_HPNO, map.get(IDBConstants.HPNO));
                  htcs.put(IConstants.SHIP_EMAIL, map.get(IDBConstants.EMAIL));
                  htcs.put(IConstants.SHIP_COUNTRY_CODE, map.get(IDBConstants.COUNTRY));
                  htcs.put(IConstants.SHIP_ADDR1, map.get(IDBConstants.ADDRESS1));
                  htcs.put(IConstants.SHIP_ADDR2, map.get(IDBConstants.ADDRESS2));
                  htcs.put(IConstants.SHIP_ADDR3, map.get(IDBConstants.ADDRESS3));
                  htcs.put(IConstants.SHIP_ADDR4, map.get(IDBConstants.ADDRESS4));
                  htcs.put(IConstants.SHIP_STATE, map.get(IDBConstants.STATE));
                  htcs.put(IConstants.SHIP_ZIP, map.get(IDBConstants.ZIP));
            	}else {
                     htcs.put(IConstants.SHIP_CONTACTNAME, map.get(IConstants.SHIP_CONTACTNAME));
                     htcs.put(IConstants.SHIP_DESGINATION, map.get(IConstants.SHIP_DESGINATION));
                     htcs.put(IConstants.SHIP_WORKPHONE, map.get(IConstants.SHIP_WORKPHONE));
                     htcs.put(IConstants.SHIP_HPNO, map.get(IConstants.SHIP_HPNO));
                     htcs.put(IConstants.SHIP_EMAIL, map.get(IConstants.SHIP_EMAIL));
                     htcs.put(IConstants.SHIP_COUNTRY_CODE, map.get(IConstants.SHIP_COUNTRY_CODE));
                     htcs.put(IConstants.SHIP_ADDR1, map.get(IConstants.SHIP_ADDR1));
                     htcs.put(IConstants.SHIP_ADDR2, map.get(IConstants.SHIP_ADDR2));
                     htcs.put(IConstants.SHIP_ADDR3, map.get(IConstants.SHIP_ADDR3));
                     htcs.put(IConstants.SHIP_ADDR4, map.get(IConstants.SHIP_ADDR4));
                     htcs.put(IConstants.SHIP_STATE, map.get(IConstants.SHIP_STATE));
                     htcs.put(IConstants.SHIP_ZIP, map.get(IConstants.SHIP_ZIP));
            	}
//            	htcs.put(IConstants.SAME_AS_CONTACT_ADDRESS, map.get(IConstants.SAME_AS_CONTACT_ADDRESS));
            	
                htcs.put(IConstants.REMARKS, map.get(IDBConstants.REMARKS));
                htcs.put(IConstants.ISACTIVE, map.get(IDBConstants.ISACTIVE));
                htcs.put(IConstants.payment_terms, map.get(IConstants.payment_terms));
                htcs.put(IConstants.PAYTERMS, map.get(IConstants.PAYTERMS));
                htcs.put(IConstants.PAYINDAYS, map.get(IConstants.PAYINDAYS));
				htcs.put(IConstants.CREDITLIMIT, map.get(IConstants.CREDITLIMIT));
                htcs.put(IConstants.CREDIT_LIMIT_BY, map.get(IConstants.CREDIT_LIMIT_BY));
                
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
                htcdn.put(IDBConstants.CUSTOMER_CODE, (String) map.get(IDBConstants.CUSTOMER_CODE));                
                //htcs.put(IDBConstants.CUSTOMERSTATUSID, (String) map.get(IDBConstants.CUSTOMERSTATUSID));
                String OLD_CUSTOMER_NAME = (String) map.get(IDBConstants.CUSTOMER_NAME);
                String CUSTOMER_NAME = (String) map.get(IDBConstants.CUSTOMER_NAME);
                JSONObject cutomerJson=new CustMstDAO().getCustomerName((String)map.get("PLANT"), (String) map.get(IDBConstants.CUSTOMER_CODE));
				if(!cutomerJson.isEmpty()) {
					OLD_CUSTOMER_NAME = cutomerJson.getString("CNAME");
				}
                result=custUtil.isExistCustomer((String) map.get(IDBConstants.CUSTOMER_CODE),(String) map.get("PLANT"));
                if(result==true)
                {
                	Hashtable ht = new Hashtable();
        			ht.put(IDBConstants.PLANT, map.get("PLANT"));
                	if(!customerBeanDAO.isExisitCustomer(ht,
        					" CUSTNO <> '"+ (String) map.get(IDBConstants.CUSTOMER_CODE) + "'  AND CNAME = '" + (String) map.get(IDBConstants.CUSTOMER_NAME) + "'"))
        			{
                	flag = custUtil.updateCustomer(htcs,htcdn);
                	
                	Hashtable htdohdr = new Hashtable();
                    htdohdr.put("CustCode", (String) map.get(IDBConstants.CUSTOMER_CODE));
                    htdohdr.put(IConstants.PLANT, map.get("PLANT"));
                    
                    Hashtable htJournalDet = new Hashtable();
                    htJournalDet.put("ACCOUNT_NAME", (String) map.get(IDBConstants.CUSTOMER_CODE)+"-"+OLD_CUSTOMER_NAME);
                    htJournalDet.put(IConstants.PLANT, (String)map.get("PLANT"));
        			
                    String updateJournalDet ="set ACCOUNT_NAME='"+(String) map.get(IDBConstants.CUSTOMER_CODE)+"-"+CUSTOMER_NAME+"'"; 
        			
        			StringBuffer updatedohdr = new StringBuffer("set ");
        			updatedohdr.append(IConstants.IN_CUST_CODE + " = '" + (String) map.get(IDBConstants.CUSTOMER_CODE) + "'");
        			updatedohdr.append("," +IConstants.IN_CUST_NAME + " = '" + (String) map.get(IDBConstants.CUSTOMER_NAME) + "'");
        			updatedohdr.append("," + IConstants.IN_PERSON_IN_CHARGE + " = '"+ (String) map.get(IDBConstants.NAME) + "'");
        			updatedohdr.append(",Address = '"+ (String) map.get(IDBConstants.ADDRESS1) + "'");
        			updatedohdr.append(",Address2 = '"+ (String) map.get(IDBConstants.ADDRESS2) + "'");
        			updatedohdr.append(",Address3 = '"+ (String) map.get(IDBConstants.ADDRESS3) + "'");
                  
        			Hashtable htdodet = new Hashtable();
        			htdodet.put(IConstants.PLANT, map.get("PLANT")); 
        			
        			String dodetquery = "select Count(*) from "+map.get("PLANT")+"_DODET where DONO in(select DONO from "+map.get("PLANT")+"_DOHDR where plant='"+map.get("PLANT")+"' and  CustCode='"+(String) map.get(IDBConstants.CUSTOMER_CODE)+"' )";
        			String dodetupdate = "set userfld3='"+(String) map.get(IDBConstants.CUSTOMER_NAME)+"' ";

        			String shiphisquery = "select Count(*) from "+map.get("PLANT")+"_SHIPHIS where DONO in(select DONO from "+map.get("PLANT")+"_DOHDR where  plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.CUSTOMER_CODE)+"' )";
        			String shiphisupdate = "set cname='"+(String) map.get(IDBConstants.CUSTOMER_NAME)+"' ";
        			
        			String wodetquery = "select Count(*) from "+map.get("PLANT")+"_WODET where WONO in(select WONO from "+map.get("PLANT")+"_WOHDR where plant='"+map.get("PLANT")+"' and  CustCode='"+(String) map.get(IDBConstants.CUSTOMER_CODE)+"' )";
        			String wodetupdate = "set userfld3='"+(String) map.get(IDBConstants.CUSTOMER_NAME)+"' ";
        			if(!CUSTOMER_NAME.equalsIgnoreCase(OLD_CUSTOMER_NAME))
       				{
        			Boolean updateflag = false;
        			updateflag = DoHdrDAO.isExisit(htdohdr,"");
   				 	if(updateflag){
   				 		updateflag = DoHdrDAO.update(updatedohdr.toString(), htdohdr, " ");
   				 	}
   				 	if(updateflag){
   				 		updateflag = DoDetDAO.isExisit(dodetquery);
   				 		if(updateflag)
   				 		{
   				 			updateflag = DoDetDAO.update(dodetupdate, htdodet, " and DONO in(select DONO from "+map.get("PLANT")+"_DOHDR where plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.CUSTOMER_CODE)+"' )");
   				 		}
   				 	}
   				 	if(updateflag){
   				 		updateflag = ShipHisDAO.isExisit(shiphisquery);
   				 		if(updateflag)
   				 		{
   				 			updateflag = ShipHisDAO.update(shiphisupdate, htdodet, " and DONO in(select DONO from "+map.get("PLANT")+"_DOHDR where plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.CUSTOMER_CODE)+"' )");
   				 		}
   				 	}
   				 updateflag = EstHdrDAO.isExisit(htdohdr);
   				 	if(updateflag){
   				 		updateflag = EstHdrDAO.update(updatedohdr.toString(), htdohdr, " ");
   					 }
   				 updateflag = new CoaDAO().isExisitcoaAccount(htJournalDet);
   				 if(updateflag){
   					updateflag = new CoaDAO().updatecoaAccount(updateJournalDet.toString(), htJournalDet, " ");
   					 }
   				updateflag = new JournalDAO().isExisitJournalDet(htJournalDet);
   				 if(updateflag){
   					updateflag = new JournalDAO().updateJournalDet(updateJournalDet.toString(), htJournalDet, " ");
   					 }	
       				}
   				 /*updateflag = WorkOrderHdrDAO.isExisit(htdohdr,"");
   				    if(updateflag){
   				 	
   				 		updateflag =  WorkOrderHdrDAO.update(updatedohdr.toString(), htdohdr, " ");
   				 	}
   				 	if(updateflag){
   				 		updateflag = WorkOrderDetDAO.isExisit(wodetquery);
   				 		if(updateflag)
   				 		{
   				 			updateflag = WorkOrderDetDAO.update(wodetupdate, htdodet, " and WONO in(select WONO from "+map.get("PLANT")+"_WOHDR where plant='"+map.get("PLANT")+"' and CustCode='"+(String) map.get(IDBConstants.CUSTOMER_CODE)+"' )");
   				 		}
   				 	}*/
        			} else {
            			
        			    throw new Exception("Customer Name Exists already. Try again with diffrent Customer Name");	
        				
        			}
                
                }else{
                	if(!custUtil.isExistCustomerName((String) map.get(IDBConstants.CUSTOMER_NAME), (String) map.get("PLANT")))
                	{
                		flag = custUtil.insertCustomer(htcs);
                		
                		if(flag) {
                			/*CoaUtil coaUtil = new CoaUtil();
            				CoaDAO coaDAO = new CoaDAO();
            				DateUtils dateutils = new DateUtils();
            				Hashtable<String, String> accountHt = new Hashtable<>();
            				accountHt.put("PLANT", (String) map.get("PLANT"));
            				accountHt.put("ACCOUNTTYPE", "3");
            				accountHt.put("ACCOUNTDETAILTYPE", "7");
            				accountHt.put("ACCOUNT_NAME", (String) map.get(IDBConstants.CUSTOMER_CODE)+"-"+(String) map.get(IDBConstants.CUSTOMER_NAME));
            				accountHt.put("DESCRIPTION", "");
            				accountHt.put("ISSUBACCOUNT", "0");
            				accountHt.put("SUBACCOUNTNAME", "");
            				accountHt.put("OPENINGBALANCE", "");
            				accountHt.put("OPENINGBALANCEDATE", "");
            				accountHt.put("CRAT", dateutils.getDateTime());
            				accountHt.put("CRBY", (String)map.get(IConstants.CREATED_BY));
            				accountHt.put("UPAT", dateutils.getDateTime());
            				
            				String gcode = coaDAO.GetGCodeById("3", (String) map.get("PLANT"));
            				String dcode = coaDAO.GetDCodeById("7", (String) map.get("PLANT"));
            				List<Map<String, String>> listQry = coaDAO.getMaxAccoutCode((String) map.get("PLANT"), "3", "7");
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
            			
        			    throw new Exception("Customer Name Exists already. Try again with diffrent Customer Name");	
        				
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
                    htmov.put(IDBConstants.DIRTYPE, TransactionConstants.CNT_CUST_UPLOAD);
                    htmov.put(IDBConstants.CUSTOMER_CODE, map.get(IDBConstants.CUSTOMER_CODE));
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
