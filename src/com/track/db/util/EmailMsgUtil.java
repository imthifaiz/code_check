package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.mail.internet.AddressException;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.EmailMsgDAO;
import com.track.tranaction.SendEmail;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class EmailMsgUtil {
    EmailMsgDAO emailDao =null;
    StrUtils strUtils = null;
    private boolean printLog = MLoggerConstant.EmailMsgUtil_PRINTPLANTMASTERLOG;
    private MLogger mLogger = new MLogger();


    public MLogger getmLogger() {
            return mLogger;
    }

    public void setmLogger(MLogger mLogger) {
            this.mLogger = mLogger;
    }
    public EmailMsgUtil() {
        strUtils = new StrUtils();
        emailDao =  new EmailMsgDAO();
    }
  //---Modified by Bruhan on March 21 2014, Description: Include Work Order in Module Type  --ManufacturingModuelsChange

    public boolean updateEmailMessageFormat(Hashtable<String, String> ht) throws Exception {
            boolean flag = false;
           
            Hashtable<String, String> htCond = new Hashtable<>();
            htCond.put(IDBConstants.PLANT, (String)ht.get(IDBConstants.PLANT));
            htCond.put(IDBConstants.MODULE_TYPE, (String)ht.get(IDBConstants.MODULE_TYPE));
            try {

				StringBuffer updateQyery = new StringBuffer("set ");

				updateQyery.append(IDBConstants.SUBJECT + " = '" + (String) ht.get(IDBConstants.SUBJECT) + "'");
				updateQyery.append("," + IDBConstants.BODY1 + " = '" + (String) ht.get(IDBConstants.BODY1) + "'");
				updateQyery.append("," + IDBConstants.ORDER_NO + " = '" + (String) ht.get(IDBConstants.ORDER_NO) + "'");
				updateQyery.append("," + IDBConstants.BODY2 + " = '" + (String) ht.get(IDBConstants.BODY2) + "'");
				updateQyery.append("," + IDBConstants.WEB_LINK + " = '" + (String) ht.get(IDBConstants.WEB_LINK) + "'");
				updateQyery.append(
						"," + IDBConstants.IS_CC_CHECKED + " = '" + (String) ht.get(IDBConstants.IS_CC_CHECKED) + "'");
				updateQyery.append(
						"," + IDBConstants.CC_SUBJECT + " = '" + (String) ht.get(IDBConstants.CC_SUBJECT) + "'");
				updateQyery.append("," + IDBConstants.CC_BODY1 + " = '" + (String) ht.get(IDBConstants.CC_BODY1) + "'");
				updateQyery.append("," + IDBConstants.CC_BODY2 + " = '" + (String) ht.get(IDBConstants.CC_BODY2) + "'");
				updateQyery.append(
						"," + IDBConstants.CC_WEB_LINK + " = '" + (String) ht.get(IDBConstants.CC_WEB_LINK) + "'");
				updateQyery.append(
						"," + IDBConstants.CC_EMAILTO + " = '" + (String) ht.get(IDBConstants.CC_EMAILTO) + "'");
				updateQyery.append(
						"," + IDBConstants.EMAIL_FROM + " = '" + (String) ht.get(IDBConstants.EMAIL_FROM) + "'");
				updateQyery.append("," + IDBConstants.UPDATED_AT + " = '" + DateUtils.getDateTime() + "'");
				updateQyery.append(
						"," + IDBConstants.UPDATED_BY + " = '" + (String) ht.get(IDBConstants.LOGIN_USER) + "'");
				// Start added by Bruhan for Email Notification on 11 July 2012.
				if (ht.get(IDBConstants.MODULE_TYPE).equals(IConstants.PURCHASE_ORDER)
						|| ht.get(IDBConstants.MODULE_TYPE).equals(IConstants.SALES_ORDER)
						|| ht.get(IDBConstants.MODULE_TYPE).equals(IConstants.CONSIGNMENT_ORDER)
						|| ht.get(IDBConstants.MODULE_TYPE).equals(IConstants.EXPIREDATE)
						|| ht.get(IDBConstants.MODULE_TYPE).equals(IConstants.MINMAX)
						|| ht.get(IDBConstants.MODULE_TYPE).equals(IConstants.ESTIMATE_ORDER)) {
					updateQyery.append(
							"," + IDBConstants.EMAIL_TO + " = '" + (String) ht.get(IDBConstants.EMAIL_TO) + "'");
					
				}
				
				if (ht.get(IDBConstants.MODULE_TYPE).equals(IConstants.EXPIREDATE)) {
					updateQyery.append("," + "EXPIRYIN" + " = '" + (String) ht.get("EXPIRYIN") + "'");
					updateQyery.append("," + "TIME" + " = '" + (String) ht.get("TIME") + "'");
				}
				if (ht.get(IDBConstants.MODULE_TYPE).equals(IConstants.MINMAX)) {
					updateQyery.append("," + "TIME" + " = '" + (String) ht.get("TIME") + "'");
				}
				updateQyery.append(
						"," + IDBConstants.ISAUTOEMAIL + " = '" + (String) ht.get(IDBConstants.ISAUTOEMAIL) + "'");
				updateQyery.append(", " + IDBConstants.SEND_ATTACHMENT + " = '")
						.append((String) ht.get(IDBConstants.SEND_ATTACHMENT) + "'");
				updateQyery.append(", " + IDBConstants.UPONCREATION + " = '")
				.append((String) ht.get(IDBConstants.UPONCREATION) + "'");
				updateQyery.append(", " + IDBConstants.CONVERTFROMEST + " = '")
				.append((String) ht.get(IDBConstants.CONVERTFROMEST) + "'");
				updateQyery.append(", " + IDBConstants.UPONAPPROVE + " = '")
				.append((String) ht.get(IDBConstants.UPONAPPROVE) + "'");
				// End added by Bruhan for Email Notification on 11 July 2012.
				emailDao.setmLogger(mLogger);
				flag = emailDao.updateEmailMessage(updateQyery.toString(), htCond, " ");
              
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            }
            return flag;
    }
    
    public Map<String, String> getEmailMsgDetails(String aplant,String moduleType) throws Exception {
           return emailDao.getEmailMsgDetails(aplant,moduleType);
    }
    
    
    /**
	 * Author Bruhan. 11 july 2012
	 * method : sendEmail(String plant,String ModuleType,String OrderType) description : Retrieves all the
	 * details for sending the mail and calls sendmail() to send the mail.)
	 * 
	 * @param : String plant,String ModuleType,String OrderType
	 * @return : void
	 * @throws Exception
	 */
    public void sendEmail(String plant,String orderNo,String OrderType){

		try {
			Map m = new EmailMsgUtil().getEmailMsgDetails(plant, OrderType);

			if (!m.isEmpty()) {
				String from = (String) m.get("EMAIL_FROM");
				String to = (String) m.get("EMAIL_TO");
				String subject = (String) m.get("SUBJECT");
				String body1 = (String) m.get("BODY1");
				String body2 = (String) m.get("BODY2");
				String webLink = (String) m.get("WEB_LINK");
				String htmlmsg = "<html><head><title>"
					+ "</title></head><body><BR><p>" + body1 + "<b>"
					+ orderNo + "</b> <br>" + "<p>" + body2
					+ "<br><br>" + "<a href=" + webLink + ">"
					+ webLink + "</a>" + " </body></html>";
				
				SendEmail sendmail = new SendEmail();
				sendmail.sendTOMail(from, to, "", "", subject, htmlmsg, "");
				String isccChecked = (String) m.get("IS_CC_CHECKED");
				if (isccChecked.equalsIgnoreCase("Y")) {
					subject = (String) m.get("CC_SUBJECT");
					String ccbody1 = (String) m.get("CC_BODY1");

					String ccbody2 = (String) m.get("CC_BODY2");
					String ccwebLink = (String) m.get("CC_WEB_LINK");
					String mailtoCopy = (String) m.get("CC_EMAILTO");
					htmlmsg = "<html><head><title>"
							+ "</title></head><body><BR><p>" + ccbody1 + "<b>"
							+ orderNo + "</b> <br>" + "<p>" + ccbody2
							+ "<br><br>" + "<a href=" + ccwebLink + ">"
							+ ccwebLink + "</a>" + " </body></html>";

					sendmail.sendTOMail(from, mailtoCopy, "", "", subject,
							htmlmsg, "");
				}
			}

		} catch (AddressException ex) {
			System.out.println("Error Message" + ex.getMessage());

		} catch (Exception e) {
			System.out.println("Error Message" + e.getMessage());
		}

	}
    
    public void sendEmailTo(String plant,String orderNo,String OrderType,String to,String CName,java.util.List<String> fileNames,String PLNTDESC,String dono,String gino,String ptype){
    	
    	try {
    		Map m = new EmailMsgUtil().getEmailMsgDetails(plant, OrderType);
    		
    		if (!m.isEmpty()) {
    			String from = (String) m.get("EMAIL_FROM");
    			String subject = (String) m.get("SUBJECT");
    			if (ptype.equalsIgnoreCase("Update"))
    				subject =ptype+" "+subject;
    			subject =subject.replace("{COMPANY_NAME}", PLNTDESC);
    			subject =subject.replace("{ORDER_NO}", dono);
    			subject =subject.replace("{INVOICE_NO}", orderNo);
    			subject =subject.replace("{DO}", gino);
    			String body1 = (String) m.get("BODY1");
    			body1 =body1.replace("{SUPPLIER_NAME}", CName);
    			body1 =body1.replace("{CUSTOMER_NAME}", CName);
    			body1 =body1.replace("{ORDER_NO}", dono);
    			body1 =body1.replace("{INVOICE_NO}", orderNo);
    			body1 =body1.replace("{DO}", gino);
    			String body2 = (String) m.get("BODY2");
    			String webLink = (String) m.get("WEB_LINK");
    			String htmlmsg = "<html><head><title>"
    					+ "</title></head><body><BR><p>"
    					//+ OrderType + " : " + orderNo + "<br/><p>"
    					+ body1 + "<p>" + body2
    					+ "<br><br>" + "<a href=" + webLink + ">"
    					+ webLink + "</a>" + " </body></html>";
    			
    			SendEmail sendmail = new SendEmail();
    			sendmail.sendTOMailPdf(from, to, "", "", subject, htmlmsg, fileNames);
    			String isccChecked = (String) m.get("IS_CC_CHECKED");
    			if (isccChecked.equalsIgnoreCase("Y")) {
    				subject = (String) m.get("CC_SUBJECT");
    				subject =subject.replace("{COMPANY_NAME}", PLNTDESC);
        			subject =subject.replace("{ORDER_NO}", dono);
        			subject =subject.replace("{INVOICE_NO}", orderNo);
        			subject =subject.replace("{DO}", gino);
    				String ccbody1 = (String) m.get("CC_BODY1");
    				ccbody1 =ccbody1.replace("{SUPPLIER_NAME}", CName);
    				ccbody1 =ccbody1.replace("{CUSTOMER_NAME}", CName);
        			ccbody1 =ccbody1.replace("{ORDER_NO}", dono);
        			ccbody1 =ccbody1.replace("{INVOICE_NO}", orderNo);
        			ccbody1 =ccbody1.replace("{DO}", gino);
    				String ccbody2 = (String) m.get("CC_BODY2");
    				String ccwebLink = (String) m.get("CC_WEB_LINK");
    				String mailtoCopy = (String) m.get("CC_EMAILTO");
    				htmlmsg = "<html><head><title>"
    						+ "</title></head><body><BR><p>" + ccbody1 
    						+ "<p>" + ccbody2
    						+ "<br><br>" + "<a href=" + ccwebLink + ">"
    						+ ccwebLink + "</a>" + " </body></html>";
    				
    				sendmail.sendTOMailPdf(from, mailtoCopy, "", "", subject,
    						htmlmsg, fileNames);
    			}
    		}
    		
    	} catch (AddressException ex) {
    		System.out.println("Error Message" + ex.getMessage());
    		
    	} catch (Exception e) {
    		System.out.println("Error Message" + e.getMessage());
    	}
    	
    }
    
    public void sendPayslipEmail(String plant,String emplyeename,String OrderType,String to,String reporing_employee,java.util.List<String> fileNames,String month,String year){
    	
    	try {
    		Map m = new EmailMsgUtil().getEmailMsgDetails(plant, OrderType);
    		
    		if (!m.isEmpty()) {
    			String from = (String) m.get("EMAIL_FROM");
    			String subject = (String) m.get("SUBJECT");
    			subject =subject.replace("{MONTH}", month);
    			subject =subject.replace("{YEAR}", year);
    			String body1 = (String) m.get("BODY1");
    			body1 =body1.replace("{MONTH}", month);
    			body1 =body1.replace("{YEAR}", year);
    			body1 =body1.replace("{EMPLOYEE_NAME}", emplyeename);
    			body1 =body1.replace("{EMPLOYEE_REPORTING}", reporing_employee);
    			String body2 = (String) m.get("BODY2");
    			String webLink = (String) m.get("WEB_LINK");
    			String htmlmsg = "<html><head><title>"
    					+ "</title></head><body><BR><p>"
    					//+ OrderType + " : " + orderNo + "<br/><p>"
    					+ body1 + "<p>" + body2
    					+ "<br><br>" + "<a href=" + webLink + ">"
    					+ webLink + "</a>" + " </body></html>";
    			
    			SendEmail sendmail = new SendEmail();
    			sendmail.sendTOMailPdf(from, to, "", "", subject, htmlmsg, fileNames);
    			String isccChecked = (String) m.get("IS_CC_CHECKED");
    			if (isccChecked.equalsIgnoreCase("Y")) {
    				subject = (String) m.get("CC_SUBJECT");
    				subject =subject.replace("{MONTH}", month);
        			subject =subject.replace("{YEAR}", year);
    				String ccbody1 = (String) m.get("CC_BODY1");
    				ccbody1 =ccbody1.replace("{MONTH}", month);
    				ccbody1 =ccbody1.replace("{YEAR}", year);
    				ccbody1 =ccbody1.replace("{EMPLOYEE_NAME}", emplyeename);
    				ccbody1 =ccbody1.replace("{EMPLOYEE_REPORTING}", reporing_employee);
    				String ccbody2 = (String) m.get("CC_BODY2");
    				String ccwebLink = (String) m.get("CC_WEB_LINK");
    				String mailtoCopy = (String) m.get("CC_EMAILTO");
    				htmlmsg = "<html><head><title>"
    						+ "</title></head><body><BR><p>" + ccbody1 
    						+ "<p>" + ccbody2
    						+ "<br><br>" + "<a href=" + ccwebLink + ">"
    						+ ccwebLink + "</a>" + " </body></html>";
    				
    				sendmail.sendTOMailPdf(from, mailtoCopy, "", "", subject,
    						htmlmsg, fileNames);
    			}
    		}
    		
    	} catch (AddressException ex) {
    		System.out.println("Error Message" + ex.getMessage());
    		
    	} catch (Exception e) {
    		System.out.println("Error Message" + e.getMessage());
    	}
    	
    }
    
    public ArrayList getConfigApprovalDetails(String aplant,String Type,String UserType,String UserId) throws Exception {        
        return emailDao.getConfigApprovalDetails(aplant,Type,UserType,UserId);
 }
    
    public boolean updateConfigApproval(Hashtable ht) throws Exception {
        boolean flag = false;
      //Added by Azees for ConfigApproval Notification on 15 Oct 2019.
        Hashtable htCond = new Hashtable();
        htCond.put(IDBConstants.PLANT, (String)ht.get(IDBConstants.PLANT));
        htCond.put(IDBConstants.ORDERTYPE, (String)ht.get(IDBConstants.ORDERTYPE));
        htCond.put(IDBConstants.USERTYPE, (String)ht.get(IDBConstants.USERTYPE));
        htCond.put(IDBConstants.USERID, (String)ht.get(IDBConstants.USERID));
        try {

        	 ArrayList arrList = new ArrayList();
             arrList= emailDao.getConfigApprovalDetails((String)ht.get(IDBConstants.PLANT),(String)ht.get(IDBConstants.ORDERTYPE),(String)ht.get(IDBConstants.USERTYPE),(String) ht.get(IDBConstants.USERID));
            if(!arrList.isEmpty()){
                StringBuffer updateQyery = new StringBuffer("set ");

                /*updateQyery.append(IDBConstants.USERID + " = '"
                                + (String) ht.get(IDBConstants.USERID) + "'");*/
                updateQyery.append(IDBConstants.EMAIL + " = '"
                                + (String) ht.get(IDBConstants.EMAIL) + "'");
                updateQyery.append("," + IDBConstants.ISACTIVE + " = '"
                                + (String) ht.get(IDBConstants.ISACTIVE) + "'");                
                updateQyery.append("," + IDBConstants.UPDATED_AT + " = '"
                                + DateUtils.getDateTime() + "'");
                updateQyery.append("," + IDBConstants.UPDATED_BY + " = '"
                                + (String) ht.get(IDBConstants.LOGIN_USER) + "'");              
				
                emailDao.setmLogger(mLogger);
                flag = emailDao.updateConfigApproval(updateQyery.toString(), htCond,  " ");
            }
            else
            {            	
                htCond.put(IDBConstants.EMAIL, (String)ht.get(IDBConstants.EMAIL));
                htCond.put(IDBConstants.ISACTIVE, (String)ht.get(IDBConstants.ISACTIVE));
                htCond.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
                htCond.put(IDBConstants.CREATED_BY, (String) ht.get(IDBConstants.LOGIN_USER));                   
            	flag = emailDao.insConfigApproval(htCond);
            }
          
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
                throw e;
        }
        return flag;
}
    public ArrayList getConfigApprovalEmailDetails(String aplant,String Type,String EmailType) throws Exception {        
        ArrayList arrList = new ArrayList();
        arrList=emailDao.getConfigApprovalEmailDetails(aplant,Type,EmailType);
         return arrList;
 }
    public boolean updateConfigApprovalEmail(Hashtable ht) throws Exception {
        boolean flag = false;
      //Added by Azees for ConfigApproval Notification on 29 Oct 2019.
        Hashtable htCond = new Hashtable();
        htCond.put(IDBConstants.PLANT, (String)ht.get(IDBConstants.PLANT));
        htCond.put(IDBConstants.ORDERTYPE, (String)ht.get(IDBConstants.ORDERTYPE));
        htCond.put(IDBConstants.EMAILTYPE, (String)ht.get(IDBConstants.EMAILTYPE));
        try {

        	 ArrayList arrList = new ArrayList();
             arrList= emailDao.getConfigApprovalEmailDetails((String)ht.get(IDBConstants.PLANT),(String)ht.get(IDBConstants.ORDERTYPE),(String)ht.get(IDBConstants.EMAILTYPE));
            if(!arrList.isEmpty()){
                StringBuffer updateQyery = new StringBuffer("set ");

                
                updateQyery.append(IDBConstants.EMAIL_TO + " = '"
                                + (String) ht.get(IDBConstants.EMAIL_TO) + "'");                
                updateQyery.append("," + IDBConstants.ATTACHMENT + " = '"
                        + (String) ht.get(IDBConstants.ATTACHMENT) + "'");
                updateQyery.append("," + IDBConstants.ATTACHMENT_TO + " = '"
                        + (String) ht.get(IDBConstants.ATTACHMENT_TO) + "'");
                updateQyery.append("," + IDBConstants.ISAUTOEMAIL + " = '"
                        + (String) ht.get(IDBConstants.ISAUTOEMAIL) + "'");
                updateQyery.append("," + IDBConstants.UPDATED_AT + " = '"
                                + DateUtils.getDateTime() + "'");
                updateQyery.append("," + IDBConstants.UPDATED_BY + " = '"
                                + (String) ht.get(IDBConstants.LOGIN_USER) + "'");              
				
                emailDao.setmLogger(mLogger);
                flag = emailDao.updateConfigApprovalEmail(updateQyery.toString(), htCond,  " ");
            }
            else
            {
            	
                htCond.put(IDBConstants.EMAIL_TO, (String)ht.get(IDBConstants.EMAIL_TO));
                htCond.put(IDBConstants.ATTACHMENT, (String)ht.get(IDBConstants.ATTACHMENT));
                htCond.put(IDBConstants.ATTACHMENT_TO, (String)ht.get(IDBConstants.ATTACHMENT_TO));
                htCond.put(IDBConstants.ISAUTOEMAIL, (String)ht.get(IDBConstants.ISAUTOEMAIL));
                htCond.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
                htCond.put(IDBConstants.CREATED_BY, (String) ht.get(IDBConstants.LOGIN_USER));                   
            	flag = emailDao.insConfigApprovalEmail(htCond);
            }
          
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
                throw e;
        }
        return flag;
}

}
