package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.db.util.EmailMsgUtil;
import com.track.gates.DbBean;
import com.track.tranaction.SendEmail;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONObject;

@WebServlet("/CommonEmailServlet")
@SuppressWarnings({"rawtypes"})
public class CommonEmailServlet extends HttpServlet implements IMLogger{
	
	private static final long serialVersionUID = 1L;
//	private boolean printLog = MLoggerConstant.CommonEmailServlet_PRINTPLANTMASTERLOG;
//	private boolean printInfo = MLoggerConstant.CommonEmailServlet_PRINTPLANTMASTERINFO;
	String action = "";
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		action = StrUtils.fString(req.getParameter("action")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
//		String user = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		List<String> attachmentLocations = new ArrayList<>();
		String attachmentLocation = null;
		
		
		if(action.equalsIgnoreCase("sendEmail")) {
			mLogger.info("SendEmail");
			/*
			 * String sendTo = StrUtils.fString(req.getParameter("send_to")).trim(); String
			 * sendCC = StrUtils.fString(req.getParameter("send_cc")).trim(); String
			 * sendSubject = StrUtils.fString(req.getParameter("send_subject")).trim();
			 * String sendBody = StrUtils.fString(req.getParameter("send_body")).trim();
			 * String sendAttachment =
			 * StrUtils.fString(req.getParameter("send_attachment")).trim();
			 * 
			 * JSONObject mailresp=new JSONObject(); mailresp.put("STATUS", "200");
			 * resp.setContentType("application/json"); resp.setCharacterEncoding("UTF-8");
			 * resp.getWriter().write(mailresp.toString()); resp.getWriter().flush();
			 * resp.getWriter().close();
			 */
			
			try{
				////////////////
				String sendTo = null;
				String sendFrom = null;
				String sendCC=null;
				String sendSubject = null;
				String sendBody = null;
				//String strbody="<html> <head> <title>Page Title</title> </head> <body> <table width='100%' border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td align='center' valign='top' bgcolor='#FFFFFF'> <table width='553' border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td colspan='3' align='left' valign='top' bgcolor='#6e8ba8'> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='7' style='font-size:0px;line-height:0px'></td> </tr> </tbody> </table> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td width='24'></td> <td align='left' style='font-family:'Helvetica Neue',Helvetica,Verdana,sans-serif;font-size:18px;color:#ffffff'>Retail Company  </td> </tr> </tbody> </table> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='7' style='font-size:0px;line-height:0px'></td> </tr> </tbody> </table> </td> </tr> <tr> <td width='2' bgcolor='#c0d0e4'></td> <td> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td width='549' bgcolor='#e6ecf4'> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='15' style='font-size:0px;line-height:0px'></td> </tr> </tbody> </table> <table width='549' border='0' cellpadding='0' cellspacing='0'> <tbody> <tr> <td width='24'></td> <td align='left' valign='top'> <span style='font-family:'Helvetica Neue',Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold'>Profit and Loss   </span> <br> <span style='font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px'>For the period ending 11 August, 2020</span> </td> </tr> </tbody> </table> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='15' style='font-size:0px;line-height:0px'></td> </tr> </tbody> </table> </td> </tr> </tbody> </table> <table width='549' border='0' cellpadding='0' cellspacing='0'> <tbody> <tr> <td align='left' valign='top'> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='20' style='font-size:0px;line-height:0px'></td> </tr> </tbody> </table> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='15' style='font-size:0px;line-height:0px'></td> </tr> </tbody> </table> <div style='font-size:14px;color:#000;font-family:'Helvetica Neue',Helvetica,Verdana,sans-serif;margin-left:22px;margin-right:22px;line-height:16px'> Hello<br><br>Attached is the Profit and Loss report for Retail Company. <br><br>Regards<br>suresh kumar </div> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='15' style='font-size:0px;line-height:0px'></td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </td> <td width='2' bgcolor='#c0d0e4'></td> </tr> <tr> <td colspan='3' align='right' bgcolor='#1f3246' style='font-family:'Helvetica Neue',Helvetica,Verdana,sans-serif;color:#ffffff;font-size:11px'> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='8'></td> </tr> </tbody> </table> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='8'></td> </tr> </tbody> </table> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='8'></td> </tr> </tbody> </table> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='8'></td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </body> </html>";
				boolean isMultipart = ServletFileUpload.isMultipartContent(req);
				if(isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);				
				List items = upload.parseRequest(req);
				Iterator iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("send_to")) {
							sendTo = StrUtils.fString(fileItem.getString()).trim();
						}
						else if (fileItem.getFieldName().equalsIgnoreCase("send_cc")) {
							sendCC = StrUtils.fString(fileItem.getString()).trim();
						}
						else if (fileItem.getFieldName().equalsIgnoreCase("send_subject")) {
							sendSubject = StrUtils.fString(fileItem.getString()).trim();
						}
						else if (fileItem.getFieldName().equalsIgnoreCase("send_body")) {
							sendBody = StrUtils.fString(fileItem.getString()).trim();
						}
						else if (fileItem.getFieldName().equalsIgnoreCase("PONO")) {
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER);
							sendFrom = (String)emailMsg.get(IDBConstants.EMAIL_FROM);
							String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
							if ("both".equals(sendAttachment) || "receiving_list".equals(sendAttachment)) {
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Receiving_List_" + StrUtils.fString(fileItem.getString()).trim() + ".pdf";
								if (attachmentLocation != null) {
									attachmentLocations.add(attachmentLocation);
								}
							}
							if ("both".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + StrUtils.fString(fileItem.getString()).trim() + ".pdf";
								if (attachmentLocation != null) {
									attachmentLocations.add(attachmentLocation);
								}
							}
						}else if (fileItem.getFieldName().equalsIgnoreCase("DONO")) {
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.SALES_ORDER);
							sendFrom = (String)emailMsg.get(IDBConstants.EMAIL_FROM);
							String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
							if ("both".equals(sendAttachment) || "picking_list".equals(sendAttachment)) {
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Picking_List_" + StrUtils.fString(fileItem.getString()).trim() + ".pdf";
								if (attachmentLocation != null) {
									attachmentLocations.add(attachmentLocation);
								}
							}
							if ("both".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + StrUtils.fString(fileItem.getString()).trim() + ".pdf";
								if (attachmentLocation != null) {
									attachmentLocations.add(attachmentLocation);
								}
							}
						}else if (fileItem.getFieldName().equalsIgnoreCase("ESTNO")) {
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.ESTIMATE_ORDER);
							sendFrom = (String)emailMsg.get(IDBConstants.EMAIL_FROM);
							if ("estimate".equals(emailMsg.get(IDBConstants.SEND_ATTACHMENT))) {
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Estimate_" + StrUtils.fString(fileItem.getString()).trim() + ".pdf";
								if (attachmentLocation != null) {
									attachmentLocations.add(attachmentLocation);
								}
							}
						}else if (fileItem.getFieldName().equalsIgnoreCase("BILLNO")) {
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.BILL);
							sendFrom = (String)emailMsg.get(IDBConstants.EMAIL_FROM);
							if ("bill".equals(emailMsg.get(IDBConstants.SEND_ATTACHMENT))) {
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Bill_" + StrUtils.fString(fileItem.getString()).trim() + ".pdf";
								if (attachmentLocation != null) {
									attachmentLocations.add(attachmentLocation);
								}
							}
						}else if (fileItem.getFieldName().equalsIgnoreCase("INVOICENO")) {
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.INVOICE);
							sendFrom = (String)emailMsg.get(IDBConstants.EMAIL_FROM);
							if ("invoice".equals(emailMsg.get(IDBConstants.SEND_ATTACHMENT))) {
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + StrUtils.fString(fileItem.getString()).trim() + ".pdf";
								if (attachmentLocation != null) {
									attachmentLocations.add(attachmentLocation);
								}
							}
						}else if (fileItem.getFieldName().equalsIgnoreCase("TONO")) {
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.CONSIGNMENT_ORDER);
							sendFrom = (String)emailMsg.get(IDBConstants.EMAIL_FROM);
							String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
							if ("both".equals(sendAttachment) || "consignment".equals(sendAttachment)) {
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Consignment_" + StrUtils.fString(fileItem.getString()).trim() + ".pdf";
								if (attachmentLocation != null) {
									attachmentLocations.add(attachmentLocation);
								}
							}
							if ("both".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + StrUtils.fString(fileItem.getString()).trim() + ".pdf";
								if (attachmentLocation != null) {
									attachmentLocations.add(attachmentLocation);
								}
							}
						}else if (fileItem.getFieldName().equalsIgnoreCase("GRNO_PONO")) {
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER_AR);
							sendFrom = (String)emailMsg.get(IDBConstants.EMAIL_FROM);
							String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
							if ("both".equals(sendAttachment) || "bothwithgrno".equals(sendAttachment) || "po".equals(sendAttachment) || "powithgrno".equals(sendAttachment)) {
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/PO_" + StrUtils.fString(fileItem.getString()).trim() + ".pdf";
								if (attachmentLocation != null) {
									attachmentLocations.add(attachmentLocation);
								}
							}
							//	Print with GRNO option is not available for invoice
							if ("both".equals(sendAttachment) || "bothwithgrno".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + StrUtils.fString(fileItem.getString()).trim() + ".pdf";
								if (attachmentLocation != null) {
									attachmentLocations.add(attachmentLocation);
								}
							}						
						}else if (fileItem.getFieldName().equalsIgnoreCase("GINO_DONO")) {
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.SALES_ORDER_API);
							sendFrom = (String)emailMsg.get(IDBConstants.EMAIL_FROM);
							String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
							if ("both".equals(sendAttachment) || "do".equals(sendAttachment) || sendAttachment.contains("withgino")) {
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/DO_" + StrUtils.fString(fileItem.getString()).trim() + ".pdf";
								if (attachmentLocation != null) {
									attachmentLocations.add(attachmentLocation);
								}
							}
							//	Print with GINO option is not available for invoice
							if ("both".equals(sendAttachment) || "bothwithgino".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + StrUtils.fString(fileItem.getString()).trim() + ".pdf";
								if (attachmentLocation != null) {
									attachmentLocations.add(attachmentLocation);
								}
							}						
						}

					}
					
					if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
						mLogger.info("FileProcessing");
						String fileLocation = "C:/ATTACHMENTS/Mail";
						//String filetempLocation = "C:/ATTACHMENTS/MailTemp";
						String fileName = StrUtils.fString(fileItem.getName()).trim();
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						
						File path = new File(fileLocation);
						if (!path.exists()) {
							path.mkdirs();
						}
						
						//fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						File uploadedFile = new File(path + "/" +fileName);
						if (uploadedFile.exists()) {
							uploadedFile.delete();
						}
						
						fileItem.write(uploadedFile);
						attachmentLocation=uploadedFile.getAbsolutePath();
						attachmentLocations.add(attachmentLocation);

				}
			}

				}
				SendEmail sendMail=new SendEmail();
				//mLogger.info("Locations:"+attachmentLocations);
//				mLogger.info("sendCC : " + sendCC);
				String mailResp=sendMail.sendTOMailPdf(sendFrom == null ? sendTo : sendFrom,sendTo,sendCC,"", sendSubject, sendBody, attachmentLocations);

				  if(mailResp!=null && mailResp.equals("Sent")) {
					  mLogger.info("Mail Status :"+mailResp);
					    JSONObject result=new JSONObject();
						result.put("STATUS", "200");
						resp.setContentType("application/json");
						resp.setCharacterEncoding("UTF-8");
						resp.getWriter().write(result.toString());
						resp.getWriter().flush();
						resp.getWriter().close();
				  }
				 
			}
			catch(Exception e)
			{
				e.printStackTrace();
				mLogger.exception(true, "ERROR IN Common Email Servlet PAGE ", e);
			    JSONObject result=new JSONObject();
				result.put("STATUS", "98");
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(e.getMessage());
				resp.getWriter().flush();
				resp.getWriter().close();
			}finally {
				// delete temp uploaded file
				File tempPath = new File(attachmentLocation);
				if (tempPath.exists()) {
				//	File tempUploadedfile = new File(tempPath + "/"+ fileName);
					//if (tempUploadedfile.exists()) {
					tempPath.delete();
					//}
				}
			}
		}
	}
	
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		action = StrUtils.fString(req.getParameter("action")).trim();
		
	}
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE,userCode);
		return loggerDetailsHasMap;
	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

}
