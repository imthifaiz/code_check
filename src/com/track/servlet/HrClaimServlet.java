package com.track.servlet;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.CoaDAO;
import com.track.dao.EmpAttachDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.HrClaimDAO;
import com.track.dao.HrDeductionDetDAO;
import com.track.dao.HrDeductionHdrDAO;
import com.track.dao.HrEmpSalaryDetDAO;
import com.track.dao.HrLeaveApplyDetDAO;
import com.track.dao.HrLeaveTypeDAO;
import com.track.dao.HrPayrollAdditionMstDAO;
import com.track.dao.HrPayrollDETDAO;
import com.track.dao.HrPayrollPaymentDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.db.object.EmployeeLeaveDET;
import com.track.db.object.HrClaim;
import com.track.db.object.HrClaimAttachment;
import com.track.db.object.HrClaimPojo;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrLeaveApplyDet;
import com.track.db.object.HrLeaveApplyHdr;
import com.track.db.object.HrLeaveType;
import com.track.db.object.HrPayrollDET;
import com.track.db.object.HrPayrollHDR;
import com.track.db.object.HrPayrollPaymentDet;
import com.track.db.object.HrPayrollPaymentHdr;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.MonthYearPojo;
import com.track.db.util.CoaUtil;
import com.track.db.util.PlantMstUtil;
import com.track.gates.DbBean;
import com.track.gates.userBean;
import com.track.service.HrEmpTypeService;
import com.track.service.HrPayrollDETService;
import com.track.service.HrPayrollHDRService;
import com.track.service.JournalService;
import com.track.serviceImplementation.HrEmpTypeServiceImpl;
import com.track.serviceImplementation.HrPayrollDETServiceImpl;
import com.track.serviceImplementation.HrPayrollHDRServiceImpl;
import com.track.serviceImplementation.JournalEntry;
import com.track.tranaction.SendEmail;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import edu.emory.mathcs.backport.java.util.Arrays;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/HrClaimServlet")
public class HrClaimServlet extends HttpServlet implements IMLogger {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("Submit")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		HrClaimDAO hrClaimDAO = new HrClaimDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
		HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
		SendEmail sendMail=new SendEmail();
		CoaDAO coaDAO = new CoaDAO();
		CoaUtil coaUtil = new CoaUtil();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();

		if (action.equalsIgnoreCase("SAVE")) {
			String empid = request.getParameter("empid");
			String repid = request.getParameter("repid");
			String[] claimdate = request.getParameterValues("claimdate");
			String[] claimid = request.getParameterValues("claimid");
			String[] description = request.getParameterValues("description");
			String[] fromplace = request.getParameterValues("fromplace");
			String[] toplace = request.getParameterValues("toplace");
			String[] distance = request.getParameterValues("distance");
			String[] amount = request.getParameterValues("amount");
			String[] clkey = request.getParameterValues("clkey");

			UserTransaction ut = null;
			String result = "";
			try {
				/* Get Transaction object */
				ut = DbBean.getUserTranaction();
				/* Begin Transaction */
				ut.begin();
				if (amount != null) {
					for (int i = 0; i < amount.length; i++) {

						HrClaim hrClaim = new HrClaim();

						hrClaim.setPLANT(plant);
						hrClaim.setEMPNOID(Integer.valueOf(empid));
						hrClaim.setCLAIMID(Integer.valueOf(claimid[i]));
						hrClaim.setCLKEY(clkey[i]);
						hrClaim.setDESCRIPTION(description[i]);
						hrClaim.setCLAIMDATE(claimdate[i]);
						hrClaim.setFROM_PLACE(fromplace[i]);
						hrClaim.setTO_PLACE(toplace[i]);
						hrClaim.setDISTANCE(Double.valueOf(distance[i]));
						hrClaim.setAMOUNT(Double.valueOf(amount[i]));
						hrClaim.setSTATUS("Pending");
						hrClaim.setREPORT_INCHARGE_ID(Integer.valueOf(repid));
						hrClaim.setCRBY(username);

						int cid = hrClaimDAO.addHrClaim(hrClaim);

						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);
						htMovHis.put("DIRTYPE", TransactionConstants.APPLY_CLAIM);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, cid);
						htMovHis.put(IDBConstants.CREATED_BY, username);
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS", claimid[i] + "," + description[i] + "," + amount[i]);
						movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS

					}
				}

				DbBean.CommitTran(ut);
				result = "Claim applied added successfully.";
				response.sendRedirect("jsp/EmpClaimApply.jsp?rsuccess=" + result);
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't apply claim.";
				response.sendRedirect("jsp/EmpClaimApply.jsp?resultnew=" + result);
			}

		}

		if(action.equalsIgnoreCase("APPROVE_CLAIM")) {
			
			String hdrid = StrUtils.fString(request.getParameter("HDRID"));
			
			JSONObject resultJson = new JSONObject();
			
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				HrClaim hrClaim = hrClaimDAO.getHrClaimById(plant, Integer.valueOf(hdrid));
				hrClaim.setSTATUS("Approved");
				hrClaim.setREASON("");
				hrClaimDAO.updateHrClaim(hrClaim, username);	
				ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(hrClaim.getEMPNOID()),plant);
				Map empmst=(Map)empmstlist.get(0);

				ArrayList empmstlistapp = employeeDAO.getEmployeeListbyid((String)empmst.get("REPORTING_INCHARGE"),plant);
				Map empmstapp=(Map)empmstlistapp.get(0);
				
				String cname = hrPayrollAdditionMstDAO.getclsimnamebyid(hrClaim.getPLANT(), String.valueOf(hrClaim.getCLAIMID()), "");
				
				
				String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
				String payclearing = empmst.get("EMPNO")+"-"+empmst.get("FNAME");
				
				List<JournalDetail> journalDetails=new ArrayList<>();
				
				JournalHeader journalHead=new JournalHeader();
				journalHead.setPLANT(plant);
				journalHead.setJOURNAL_DATE(hrClaim.getCRAT());
				journalHead.setJOURNAL_STATUS("PUBLISHED");
				journalHead.setJOURNAL_TYPE("Cash");
				journalHead.setCURRENCYID(curency);
				journalHead.setTRANSACTION_TYPE("CLAIM");
				journalHead.setTRANSACTION_ID(hdrid);
				journalHead.setSUB_TOTAL(hrClaim.getAMOUNT());
				journalHead.setTOTAL_AMOUNT(hrClaim.getAMOUNT());
				journalHead.setCRAT(dateutils.getDateTime());
				journalHead.setCRBY(username);
				
				JournalDetail journalDetail=new JournalDetail();
				journalDetail.setPLANT(plant);
				JSONObject coaJson2=coaDAO.getCOAByName(plant, cname);
				journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
				journalDetail.setACCOUNT_NAME(cname);
				journalDetail.setDEBITS(hrClaim.getAMOUNT());
				journalDetails.add(journalDetail);
				
				JournalDetail journalDetail_1=new JournalDetail();
				journalDetail_1.setPLANT(plant);
				JSONObject coaJson1=coaDAO.getCOAByName(plant, payclearing);
				journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
				journalDetail_1.setACCOUNT_NAME(payclearing);
				journalDetail_1.setCREDITS(hrClaim.getAMOUNT());
				journalDetails.add(journalDetail_1);
				
				
				Journal journal=new Journal();
				journal.setJournalHeader(journalHead);
				journal.setJournalDetails(journalDetails);
				JournalService journalService=new JournalEntry();
				Journal journalFrom=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
				if(journalFrom.getJournalHeader()!=null){
					if(journalFrom.getJournalHeader().getID()>0){
						journalHead.setID(journalFrom.getJournalHeader().getID());
						journalService.updateJournal(journal, username);
						Hashtable jhtMovHis = new Hashtable();
						jhtMovHis.put(IDBConstants.PLANT, plant);
						jhtMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
						jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
						jhtMovHis.put(IDBConstants.ITEM, "");
						jhtMovHis.put(IDBConstants.QTY, "0.0");
						jhtMovHis.put("RECID", "");
						jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
						jhtMovHis.put(IDBConstants.CREATED_BY, username);		
						jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						jhtMovHis.put("REMARKS","");
						movHisDao.insertIntoMovHis(jhtMovHis);
					}else{
						journalService.addJournal(journal, username);
						Hashtable jhtMovHis = new Hashtable();
						jhtMovHis.put(IDBConstants.PLANT, plant);
						jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
						jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
						jhtMovHis.put(IDBConstants.ITEM, "");
						jhtMovHis.put(IDBConstants.QTY, "0.0");
						jhtMovHis.put("RECID", "");
						jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
						jhtMovHis.put(IDBConstants.CREATED_BY, username);		
						jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						jhtMovHis.put("REMARKS","");
						movHisDao.insertIntoMovHis(jhtMovHis);
					}
				}

				String body="";
				body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
				body +="<div>";
				body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;'>";
				body +="</div>";
				body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
				body +="<span>Claim Approved</span>";
				body +="</div>";
				body +="<div class='box-header with-border' style='margin-top: 26px;'>";
				body +="<p>Hello,</p>";
				body +="</div>";
				body +="<div>";
				body +="<div class='row'>";
				body +="<div class='col-md-6'>";
				body +="<table style='border-collapse: collapse;'>";
				body +="<tbody>";
				body +="<tr>";
				body +="<td style='width: 140px;padding: 2%;'>Employee Code</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+empmst.get("EMPNO")+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Employee Name</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+empmst.get("FNAME")+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Date</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+hrClaim.getCLAIMDATE()+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Claim</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+cname+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Description</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+hrClaim.getDESCRIPTION()+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Amount</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+hrClaim.getAMOUNT()+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Status</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>Approved</td>";
				body +="</tr>";
				body +="</tbody>";
				body +="</table>";
				body +="</div>";
				body +="</div>";
		
				body +="</div>";
				body +="<div style='margin-top:30px'>";
				body +="<a href='https://ordermgt.u-clo.com/track/jsp/EmployeeLogin.jsp'>click here to login</a>";
				body +="</div>";
				body +="<div style='margin-top:30px'>";
				body +="<p>Regards <br>"+empmstapp.get("FNAME")+"</p>";
				/* body +="<p style='margin-top:-14px'>"+empmstapp.get("FNAME")+"</p>"; */
				body +="</div>";
				body +="</div>";
				String subject ="";
				subject = "RE:Claim Approved "+empmst.get("FNAME")+" for "+cname+" on "+hrClaim.getCLAIMDATE();
				
				/*String to = (String)empmst.get("EMAIL");
			    sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");*/
				try {
					String to = (String)empmst.get("EMAIL");
				    sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");
					}catch (Exception e) {
						System.out.println(e);
					}
				
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.APPROVED_CLAIM);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
					htMovHis.put("RECID", hdrid);
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS","Approved,"+hrClaim.getEMPNOID()+","+cname+","+hrClaim.getAMOUNT()+","+hrClaim.getCLAIMDATE());
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS

				DbBean.CommitTran(ut);
				resultJson.put("STATUS", "OK");   
				/*
				 * result = "Leave Approved Successfully";
				 * response.sendRedirect("jsp/EmpDashboard.jsp?rsuccess="+ result);
				 */
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				resultJson.put("STATUS", "NOT OK");   
				/*
				 * result = "Leave Approve failed";
				 * response.sendRedirect("jsp/EmpDashboard.jsp?resultnew="+ result);
				 */
			}
			
			response.getWriter().write(resultJson.toString());	
		}
		
		if(action.equalsIgnoreCase("APPROVE_CLAIM_LIST")) {
			String[] appcheck  = request.getParameterValues("appclaimcheck");
			String[] reasoncheck  = request.getParameterValues("clreasoncheck");

			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				for (int i = 0; i < appcheck.length; i++) {
					String	hdrid = appcheck[i];
					String  reason = reasoncheck[i];
					
					HrClaim hrClaim = hrClaimDAO.getHrClaimById(plant, Integer.valueOf(hdrid));
					hrClaim.setSTATUS("Approved");
					hrClaim.setREASON(reason);
					hrClaimDAO.updateHrClaim(hrClaim, username);	
					ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(hrClaim.getEMPNOID()),plant);
					Map empmst=(Map)empmstlist.get(0);

					ArrayList empmstlistapp = employeeDAO.getEmployeeListbyid((String)empmst.get("REPORTING_INCHARGE"),plant);
					Map empmstapp=(Map)empmstlistapp.get(0);
					
					 String cname = hrPayrollAdditionMstDAO.getclsimnamebyid(hrClaim.getPLANT(), String.valueOf(hrClaim.getCLAIMID()), "");
					 
					 String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
						String payclearing = empmst.get("EMPNO")+"-"+empmst.get("FNAME");
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(hrClaim.getCRAT());
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("CLAIM");
						journalHead.setTRANSACTION_ID(hdrid);
						journalHead.setSUB_TOTAL(hrClaim.getAMOUNT());
						journalHead.setTOTAL_AMOUNT(hrClaim.getAMOUNT());
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);
						
						JournalDetail journalDetail=new JournalDetail();
						journalDetail.setPLANT(plant);
						JSONObject coaJson2=coaDAO.getCOAByName(plant, cname);
						journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
						journalDetail.setACCOUNT_NAME(cname);
						journalDetail.setDEBITS(hrClaim.getAMOUNT());
						journalDetails.add(journalDetail);
						
						JournalDetail journalDetail_1=new JournalDetail();
						journalDetail_1.setPLANT(plant);
						JSONObject coaJson1=coaDAO.getCOAByName(plant, payclearing);
						journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
						journalDetail_1.setACCOUNT_NAME(payclearing);
						journalDetail_1.setCREDITS(hrClaim.getAMOUNT());
						journalDetails.add(journalDetail_1);
						
						
						Journal journal=new Journal();
						journal.setJournalHeader(journalHead);
						journal.setJournalDetails(journalDetails);
						JournalService journalService=new JournalEntry();
						Journal journalFrom=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
						if(journalFrom.getJournalHeader()!=null){
							if(journalFrom.getJournalHeader().getID()>0){
								journalHead.setID(journalFrom.getJournalHeader().getID());
								journalService.updateJournal(journal, username);
								Hashtable jhtMovHis = new Hashtable();
								jhtMovHis.put(IDBConstants.PLANT, plant);
								jhtMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
								jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
								jhtMovHis.put(IDBConstants.ITEM, "");
								jhtMovHis.put(IDBConstants.QTY, "0.0");
								jhtMovHis.put("RECID", "");
								jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
								jhtMovHis.put(IDBConstants.CREATED_BY, username);		
								jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								jhtMovHis.put("REMARKS","");
								movHisDao.insertIntoMovHis(jhtMovHis);
							}else{
								journalService.addJournal(journal, username);
								Hashtable jhtMovHis = new Hashtable();
								jhtMovHis.put(IDBConstants.PLANT, plant);
								jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
								jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
								jhtMovHis.put(IDBConstants.ITEM, "");
								jhtMovHis.put(IDBConstants.QTY, "0.0");
								jhtMovHis.put("RECID", "");
								jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
								jhtMovHis.put(IDBConstants.CREATED_BY, username);		
								jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								jhtMovHis.put("REMARKS","");
								movHisDao.insertIntoMovHis(jhtMovHis);
							}
						}

					String body="";
					body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
					body +="<div>";
					body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;'>";
					body +="</div>";
					body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
					body +="<span>Claim Approved</span>";
					body +="</div>";
					body +="<div class='box-header with-border' style='margin-top: 26px;'>";
					body +="<p>Hello,</p>";
					body +="</div>";
					body +="<div>";
					body +="<div class='row'>";
					body +="<div class='col-md-6'>";
					body +="<table style='border-collapse: collapse;'>";
					body +="<tbody>";
					body +="<tr>";
					body +="<td style='width: 140px;padding: 2%;'>Employee Code</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+empmst.get("EMPNO")+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Employee Name</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+empmst.get("FNAME")+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Date</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+hrClaim.getCLAIMDATE()+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Claim</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+cname+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Description</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+hrClaim.getDESCRIPTION()+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Amount</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+hrClaim.getAMOUNT()+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Status</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>Approved</td>";
					body +="</tr>";
					body +="</tbody>";
					body +="</table>";
					body +="</div>";
					body +="</div>";
			
					body +="</div>";
					body +="<div style='margin-top:30px'>";
					body +="<a href='https://ordermgt.u-clo.com/track/jsp/EmployeeLogin.jsp'>click here to login</a>";
					body +="</div>";
					body +="<div style='margin-top:30px'>";
					body +="<p>Regards <br>"+empmstapp.get("FNAME")+"</p>";
					/* body +="<p style='margin-top:-14px'>"+empmstapp.get("FNAME")+"</p>"; */
					body +="</div>";
					body +="</div>";
					String subject ="";
					subject = "RE:Claim Approved "+empmst.get("FNAME")+" for "+cname+" on "+hrClaim.getCLAIMDATE();
					
					/*String to = (String)empmst.get("EMAIL");
				    sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");*/
					try {
						String to = (String)empmst.get("EMAIL");
					    sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");
						}catch (Exception e) {
							System.out.println(e);
						}
					
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);					
						htMovHis.put("DIRTYPE", TransactionConstants.APPROVED_CLAIM);	
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
						htMovHis.put("RECID", hdrid);
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS","Approved,"+hrClaim.getEMPNOID()+","+cname+","+hrClaim.getAMOUNT()+","+hrClaim.getCLAIMDATE());
						movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
				}
					
				DbBean.CommitTran(ut);
				result = "Claim Approved Successfully";
				response.sendRedirect("jsp/EmpDashboard.jsp?rsuccess="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Claim Approve failed";
				response.sendRedirect("jsp/EmpDashboard.jsp?resultnew="+ result);
			}

		}
		
		if(action.equalsIgnoreCase("REJECT_CLAIM")) {
			
			String hdrid = StrUtils.fString(request.getParameter("HDRID"));
			String reason = StrUtils.fString(request.getParameter("REASON"));
			
			JSONObject resultJson = new JSONObject();
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				HrClaim hrClaim = hrClaimDAO.getHrClaimById(plant, Integer.valueOf(hdrid));
				hrClaim.setSTATUS("Rejected");
				hrClaim.setREASON(reason);
				hrClaimDAO.updateHrClaim(hrClaim, username);	
				ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(hrClaim.getEMPNOID()),plant);
				Map empmst=(Map)empmstlist.get(0);

				ArrayList empmstlistapp = employeeDAO.getEmployeeListbyid((String)empmst.get("REPORTING_INCHARGE"),plant);
				Map empmstapp=(Map)empmstlistapp.get(0);
				
				 String cname = hrPayrollAdditionMstDAO.getclsimnamebyid(hrClaim.getPLANT(), String.valueOf(hrClaim.getCLAIMID()), "");

				String body="";
				body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
				body +="<div>";
				body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;'>";
				body +="</div>";
				body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
				body +="<span>Claim Rejected</span>";
				body +="</div>";
				body +="<div class='box-header with-border' style='margin-top: 26px;'>";
				body +="<p>Hello,</p>";
				body +="</div>";
				body +="<div>";
				body +="<div class='row'>";
				body +="<div class='col-md-6'>";
				body +="<table style='border-collapse: collapse;'>";
				body +="<tbody>";
				body +="<tr>";
				body +="<td style='width: 140px;padding: 2%;'>Employee Code</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+empmst.get("EMPNO")+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Employee Name</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+empmst.get("FNAME")+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Date</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+hrClaim.getCLAIMDATE()+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Claim</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+cname+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Description</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+hrClaim.getDESCRIPTION()+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Amount</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+hrClaim.getAMOUNT()+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Status</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>Rejected</td>";
				body +="</tr>";
				body +="</tbody>";
				body +="</table>";
				body +="</div>";
				body +="</div>";
		
				body +="</div>";
				body +="<div style='margin-top:30px'>";
				body +="<a href='https://ordermgt.u-clo.com/track/jsp/EmployeeLogin.jsp'>click here to login</a>";
				body +="</div>";
				body +="<div style='margin-top:30px'>";
				body +="<p>Regards <br>"+empmstapp.get("FNAME")+"</p>";
				/* body +="<p style='margin-top:-14px'>"+empmstapp.get("FNAME")+"</p>"; */
				body +="</div>";
				body +="</div>";
				String subject ="";
				subject = "RE:Claim Rejected "+empmst.get("FNAME")+" for "+cname+" on "+hrClaim.getCLAIMDATE();
				
				/*String to = (String)empmst.get("EMAIL");
			    sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");*/
				try {
					String to = (String)empmst.get("EMAIL");
				    sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");
					}catch (Exception e) {
						System.out.println(e);
					}
				
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.REJECTED_CLAIM);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
					htMovHis.put("RECID", hdrid);
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS","Rejected,"+hrClaim.getEMPNOID()+","+cname+","+hrClaim.getAMOUNT()+","+hrClaim.getCLAIMDATE());
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS

				DbBean.CommitTran(ut);
				resultJson.put("STATUS", "OK");   
				/*
				 * result = "Leave Approved Successfully";
				 * response.sendRedirect("jsp/EmpDashboard.jsp?rsuccess="+ result);
				 */
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				resultJson.put("STATUS", "NOT OK");   
				/*
				 * result = "Leave Approve failed";
				 * response.sendRedirect("jsp/EmpDashboard.jsp?resultnew="+ result);
				 */
			}
			
			response.getWriter().write(resultJson.toString());	
		}
		
		if(action.equalsIgnoreCase("REJECT_CLAIM_LIST")) {
			String[] appcheck  = request.getParameterValues("appclaimcheck");
			String[] reasoncheck  = request.getParameterValues("clreasoncheck");
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				for (int i = 0; i < appcheck.length; i++) {
					String	hdrid = appcheck[i];
					String  reason = reasoncheck[i];
					
					HrClaim hrClaim = hrClaimDAO.getHrClaimById(plant, Integer.valueOf(hdrid));
					hrClaim.setSTATUS("Rejected");
					hrClaim.setREASON(reason);
					hrClaimDAO.updateHrClaim(hrClaim, username);	
					ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(hrClaim.getEMPNOID()),plant);
					Map empmst=(Map)empmstlist.get(0);

					ArrayList empmstlistapp = employeeDAO.getEmployeeListbyid((String)empmst.get("REPORTING_INCHARGE"),plant);
					Map empmstapp=(Map)empmstlistapp.get(0);
					
					 String cname = hrPayrollAdditionMstDAO.getclsimnamebyid(hrClaim.getPLANT(), String.valueOf(hrClaim.getCLAIMID()), "");

					String body="";
					body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
					body +="<div>";
					body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;'>";
					body +="</div>";
					body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
					body +="<span>Claim Rejected</span>";
					body +="</div>";
					body +="<div class='box-header with-border' style='margin-top: 26px;'>";
					body +="<p>Hello,</p>";
					body +="</div>";
					body +="<div>";
					body +="<div class='row'>";
					body +="<div class='col-md-6'>";
					body +="<table style='border-collapse: collapse;'>";
					body +="<tbody>";
					body +="<tr>";
					body +="<td style='width: 140px;padding: 2%;'>Employee Code</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+empmst.get("EMPNO")+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Employee Name</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+empmst.get("FNAME")+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Date</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+hrClaim.getCLAIMDATE()+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Claim</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+cname+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Description</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+hrClaim.getDESCRIPTION()+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Amount</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+hrClaim.getAMOUNT()+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Status</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>Rejected</td>";
					body +="</tr>";
					body +="</tbody>";
					body +="</table>";
					body +="</div>";
					body +="</div>";
			
					body +="</div>";
					body +="<div style='margin-top:30px'>";
					body +="<a href='https://ordermgt.u-clo.com/track/jsp/EmployeeLogin.jsp'>click here to login</a>";
					body +="</div>";
					body +="<div style='margin-top:30px'>";
					body +="<p>Regards <br>"+empmstapp.get("FNAME")+"</p>";
					/* body +="<p style='margin-top:-14px'>"+empmstapp.get("FNAME")+"</p>"; */
					body +="</div>";
					body +="</div>";
					String subject ="";
					subject = "RE:Claim Rejected "+empmst.get("FNAME")+" for "+cname+" on "+hrClaim.getCLAIMDATE();
					
					/*String to = (String)empmst.get("EMAIL");
				    sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");*/
					try {
						String to = (String)empmst.get("EMAIL");
					    sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");
						}catch (Exception e) {
							System.out.println(e);
						}
					
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);					
						htMovHis.put("DIRTYPE", TransactionConstants.REJECTED_CLAIM);	
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
						htMovHis.put("RECID", hdrid);
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS","Rejected,"+hrClaim.getEMPNOID()+","+cname+","+hrClaim.getAMOUNT()+","+hrClaim.getCLAIMDATE());
						movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
				}
					
				DbBean.CommitTran(ut);
				result = "Claim Rejected Successfully";
				response.sendRedirect("jsp/EmpDashboard.jsp?rsuccess="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Claim Reject failed";
				response.sendRedirect("jsp/EmpDashboard.jsp?resultnew="+ result);
			}
		}
		
		if(action.equalsIgnoreCase("add_attachments")) {
			
			  JSONObject objectResult = new JSONObject(); 
			  objectResult = claimAttachments(request, response);
			 

		}
		
		if(action.equalsIgnoreCase("downloadAttachmentById"))
		{
			System.out.println("Attachments by ID");
			int ID=Integer.parseInt(request.getParameter("attachid"));
			FileHandling fileHandling=new FileHandling(); 

			try {
				HrClaimAttachment hrClaimAttachment = hrClaimDAO.getHrClaimAttachmentById(plant, ID);
				String filePath=hrClaimAttachment.getFilePath();
				String fileType=hrClaimAttachment.getFileType();
				String fileName=hrClaimAttachment.getFileName();
				fileHandling.fileDownload(filePath, fileName, fileType, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = StrUtils.fString(req.getParameter("CMD")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		HrClaimDAO hrClaimDAO = new HrClaimDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
		HrPayrollPaymentDAO hrPayrollPaymentDAO = new HrPayrollPaymentDAO();

		if (action.equalsIgnoreCase("GET_CLAIM_SUMMARY")) {
			String balace = "0";
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();

			try {
				List<HrClaimPojo> hrClaimPojo = hrClaimDAO.getAllHrClaimPojo(plant);
				JSONObject resultJsonInt = new JSONObject();
				resultJson.put("CLAIMSUMMARY", hrClaimPojo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}

			resp.getWriter().write(resultJson.toString());

		}
		
		if (action.equalsIgnoreCase("GET_CLAIM_PROCESS")) {
			String balace = "0";
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();

			try {
				List<HrClaimPojo> hrClaimPojo = hrClaimDAO.getHrClaimPojoProcess(plant);
				JSONObject resultJsonInt = new JSONObject();
				resultJson.put("CLAIMSUMMARY", hrClaimPojo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}

			resp.getWriter().write(resultJson.toString());

		}
		
		if (action.equalsIgnoreCase("GET_CLAIM_PROCESS_ACCOUNT")) {
			String balace = "0";
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			String Account = StrUtils.fString(req.getParameter("ACCOUNT"));

			try {
				List<HrClaimPojo> hrClaimPojo = hrClaimDAO.getHrClaimPojoProcess(plant);
				String[] acct = Account.split("-");
				String empcode = acct[0];
				/* int empid = Integer.valueOf(employeeDAO.getEmpid(plant, empcode, "")); */
				List<HrClaimPojo> hrClaimPojofilter = hrClaimPojo.stream().filter((c)->c.getEMPCODE().equalsIgnoreCase(empcode)).collect(Collectors.toList());
				JSONObject resultJsonInt = new JSONObject();
				resultJson.put("CLAIMSUMMARY", hrClaimPojofilter);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}

			resp.getWriter().write(resultJson.toString());

		}

		if(action.equalsIgnoreCase("GET_CLAIM"))
		{
			 String hdrid= StrUtils.fString(req.getParameter("HDRID"));

			 ByteArrayOutputStream out = new ByteArrayOutputStream();            
			    try {
			    	
			    	Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
			        PdfWriter writer = PdfWriter.getInstance(doc, out);
			        doc.open();
			    	addPDFbody(doc,hdrid,plant);
			        doc.close();
			        writer.close();
				    byte[] bytes = out.toByteArray();
				    resp.addHeader("Content-disposition","attachment;filename=claim.pdf");
				    resp.setContentLength(bytes.length);
				    resp.getOutputStream().write(bytes);
				    resp.setContentType("application/pdf");
			    }catch (Exception e) {
					System.out.println(e);
				}
		}
		
		if(action.equalsIgnoreCase("GET_CLAIM_PRINT"))
		{
			 String hdrid= StrUtils.fString(req.getParameter("HDRID"));

			 ByteArrayOutputStream out = new ByteArrayOutputStream();            
			    try {
			    	
			    	Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
			        PdfWriter writer = PdfWriter.getInstance(doc, out);
			        doc.open();
			    	addPDFbody(doc,hdrid,plant);
			    	doc.close();
			        writer.close();
				    byte[] bytes = out.toByteArray();
				    resp.addHeader("Content-disposition","inline;filename=claim.pdf");
				    resp.setContentLength(bytes.length);
				    resp.getOutputStream().write(bytes);
				    resp.setContentType("application/pdf");
			    }catch (Exception e) {
					System.out.println(e);
				}
		}
		
		if(action.equalsIgnoreCase("SEND_CLAIM_EMAIL"))
		{
			String hdrid= StrUtils.fString(req.getParameter("HDRID"));
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			JSONObject resultJson = new JSONObject();
			    try {
			    	
			    		userBean ubean = new userBean();
			    		
			    		HrPayrollPaymentHdr payhdr = hrPayrollPaymentDAO.getHrPayrollPaymentHdrId(plant, Integer.valueOf(hdrid));
			    		
			    		
				    	
				    	String[] acct = payhdr.getACCOUNT_NAME().split("-");
				    	String empcode = acct[0];
				    	String empid = employeeDAO.getEmpid(plant, empcode, "");
				    	
				    	ArrayList empmstlistapp = employeeDAO.getEmployeeListbyid(empid,plant);
						Map empmstapp=(Map)empmstlistapp.get(0);
				        String to = (String)empmstapp.get("EMAIL");
				        
				        if(to.equalsIgnoreCase("") || to == null) {
				        	resultJson.put("STATUS", "NOT OK");
				        }

						Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
				        PdfWriter writer = PdfWriter.getInstance(doc, out);
				        doc.open();
				        addPDFbody(doc,hdrid,plant);
				        doc.close();
				        writer.close();
					    byte[] bytes = out.toByteArray();
					    
					    DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
			            MimeBodyPart pdfBodyPart = new MimeBodyPart();
			            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			            pdfBodyPart.setFileName("Claim.pdf");
						
						String companyname = ubean.getCompanyName(plant).toUpperCase();             
			            String body="";
						body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
						body +="<div style='position: relative;padding: 0px 15px 0 15px;'>";
						body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;vertical-align: middle;'>";
						body +="<span style='margin-left: 15px;font-size: 15px;font-weight: bold;vertical-align: middle;'>"+companyname+"</span>";
						body +="</div>";
						body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
						body +="<span>Claim</span>";
						body +="</div>";
						body +="<div class='box-header with-border' style='margin-top: 26px;'>";
						body +="<p>Hello,</p>";
						body +="<p>Please find the attached claim.</p>";
						body +="</div>";
						body +="<div style='margin-top:30px'>";
						body +="<p>Regards <br>HR Team</p>";
						body +="</div>";
						body +="</div>";
						body +=" <img src=\"cid:myimg\" /></body></html>";
						String subject ="RE:Claim";

						
						MimeBodyPart textPart = new MimeBodyPart();       
				        textPart.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");       
				        textPart.setContent(body, "text/html; charset=utf-8"); 
				         
				        MimeMultipart multipart = new MimeMultipart("mixed");     
				        multipart.addBodyPart(textPart);
				        multipart.addBodyPart(pdfBodyPart);
				        
				        
				        try {
					    SendEmail sendMail=new SendEmail();
						String mailResp=sendMail.sendTOMailPdfpayroll("info@u-clo.com",to,"","", subject, body, multipart);
				        } catch (Exception e) {
				        	System.out.println(e);
						}
					    
				
					resultJson.put("STATUS", "OK");
					
			    }catch (Exception e) {
			    	
			    	resultJson.put("STATUS", "NOT OK");
			    	
					System.out.println(e);
				}
			    
			    resp.getWriter().write(resultJson.toString());
		}

	}
	
	private void addPDFbody(Document doc, String hdrid,String plant) throws DocumentException, IOException {
		

		PlantMstDAO plantMstDAO = new PlantMstDAO();
		HrPayrollPaymentDAO hrPayrollPaymentDAO = new HrPayrollPaymentDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
		HrClaimDAO hrClaimDAO = new HrClaimDAO();
	
		try {
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	
	    	userBean ubean = new userBean();
	    	String companyname = ubean.getCompanyName(plant).toUpperCase();
	    	
	    	HrPayrollPaymentHdr payhdr = hrPayrollPaymentDAO.getHrPayrollPaymentHdrId(plant, Integer.valueOf(hdrid));
	    	
	    	String[] acct = payhdr.getACCOUNT_NAME().split("-");
	    	String empcode = acct[0];
	    	String empid = employeeDAO.getEmpid(plant, empcode, "");
	    	ArrayList arrEmp = employeeDAO.getEmployeeListbyid(empid,plant);
	    	Map employee=(Map)arrEmp.get(0);

	    	String empname = (String) employee.get("FNAME");
	    	String department = (String) employee.get("DEPT");
	    	String designation = (String) employee.get("DESGINATION");
	    	String passportno = (String) employee.get("PASSPORTNUMBER");
	    	String doj = (String) employee.get("DATEOFJOINING");
	    	String lcardno = (String) employee.get("LABOURCARDNUMBER");
	    	String bank = (String) employee.get("BANKNAME");

	    	 List<HrClaimPojo> HrClaimPojolist=new ArrayList<HrClaimPojo>();
	    	 List<HrPayrollPaymentDet> paydet = hrPayrollPaymentDAO.getdetbyhdrid(plant, payhdr.getID());
	         for (HrPayrollPaymentDet hrPayrollPaymentDet : paydet) {
	        	 HrClaimPojo claim = hrClaimDAO.getAllHrClaimPojobyid(plant, hrPayrollPaymentDet.getPAYID());
	        	 HrClaimPojolist.add(claim);
	    	 }
			

	        Paragraph preface = new Paragraph(new Chunk("CLAIM", new Font(Font.HELVETICA, 13, Font.BOLD, Color.BLACK))); 
	        preface.setAlignment(Element.ALIGN_CENTER);
	        doc.add(preface);
	
	        
	        PdfPTable tableheader = new PdfPTable(2);
			
			int[] headerdata = { 30, 125 }; // 23, 20, 25, 32 };
			tableheader.setWidths(headerdata);
			tableheader.setWidthPercentage(100);
			tableheader.setHorizontalAlignment(Element.ALIGN_CENTER);
	        
	        String imageUrl = MLoggerConstant.PROPS_FOLDER + "/track/Logos/"+plant.toLowerCase()+"Logo.GIF";
	        Image image1 = Image.getInstance(imageUrl);
	        //image1.setAbsolutePosition(100f, 550f);
	        image1.scaleAbsolute(60, 60);
	
			Paragraph title = new Paragraph();
			title.add(new Chunk(companyname, new Font(Font.HELVETICA, 13, Font.BOLD, Color.BLACK)));
			title.setAlignment(Element.ALIGN_CENTER);
	
			PdfPCell cell1 = new PdfPCell();
			cell1.addElement(image1);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell1.setBorder(Rectangle.UNDEFINED);
			
			PdfPCell cell2 = new PdfPCell();
			cell2.setPaddingTop(5);
			cell2.setPaddingRight(60);
			cell2.addElement(title);
			cell2.setBorder(Rectangle.UNDEFINED);
			
			tableheader.addCell(cell1);
			tableheader.addCell(cell2);	
			
			doc.add(tableheader);
			
			PdfPTable tablebody1 = new PdfPTable(3);
			
			int[] body1data = { 45, 10, 120 };
			tablebody1.setWidths(body1data);
			tablebody1.setWidthPercentage(100);
			tablebody1.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			
			Paragraph row1a = new Paragraph();
			row1a.add(new Chunk("Employee Code", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row1a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c1a = new PdfPCell();
			c1a.addElement(row1a);
			c1a.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row1b = new Paragraph();
			row1b.add(new Chunk(empcode, new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row1b.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c1c = new PdfPCell();
			c1c.addElement(row1b);
			c1c.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row1c = new Paragraph();
			row1c.add(new Chunk(":", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row1c.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c1b = new PdfPCell();
			c1b.addElement(row1c);
			c1b.setBorder(Rectangle.UNDEFINED);
			
			tablebody1.addCell(c1a);
			tablebody1.addCell(c1b);
			tablebody1.addCell(c1c);
			
			Paragraph row2a = new Paragraph();
			row2a.add(new Chunk("Name", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row2a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c2a = new PdfPCell();
			c2a.addElement(row2a);
			c2a.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row2b = new Paragraph();
			row2b.add(new Chunk(empname, new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row2b.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c2c = new PdfPCell();
			c2c.addElement(row2b);
			c2c.setBorder(Rectangle.UNDEFINED);
			
			PdfPCell c2b = new PdfPCell();
			c2b.addElement(row1c);
			c2b.setBorder(Rectangle.UNDEFINED);
			
			tablebody1.addCell(c2a);
			tablebody1.addCell(c2b);
			tablebody1.addCell(c2c);
			
			
			Paragraph row3a = new Paragraph();
			row3a.add(new Chunk("Department", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row3a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c3a = new PdfPCell();
			c3a.addElement(row3a);
			c3a.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row3b = new Paragraph();
			row3b.add(new Chunk(department, new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row3b.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c3c = new PdfPCell();
			c3c.addElement(row3b);
			c3c.setBorder(Rectangle.UNDEFINED);
			
			PdfPCell c3b = new PdfPCell();
			c3b.addElement(row1c);
			c3b.setBorder(Rectangle.UNDEFINED);
			
			tablebody1.addCell(c3a);
			tablebody1.addCell(c3b);
			tablebody1.addCell(c3c);
			
			Paragraph row4a = new Paragraph();
			row4a.add(new Chunk("Designation", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row4a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c4a = new PdfPCell();
			c4a.addElement(row4a);
			c4a.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row4b = new Paragraph();
			row4b.add(new Chunk(designation, new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row4b.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c4c = new PdfPCell();
			c4c.addElement(row4b);
			c4c.setBorder(Rectangle.UNDEFINED);
			
			PdfPCell c4b = new PdfPCell();
			c4b.addElement(row1c);
			c4b.setBorder(Rectangle.UNDEFINED);
			
			tablebody1.addCell(c4a);
			tablebody1.addCell(c4b);
			tablebody1.addCell(c4c);
			
			Paragraph row5a = new Paragraph();
			row5a.add(new Chunk("Passport Number", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row5a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c5a = new PdfPCell();
			c5a.addElement(row5a);
			c5a.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row5b = new Paragraph();
			row5b.add(new Chunk(passportno, new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row5b.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c5c = new PdfPCell();
			c5c.addElement(row5b);
			c5c.setBorder(Rectangle.UNDEFINED);
			
			PdfPCell c5b = new PdfPCell();
			c5b.addElement(row1c);
			c5b.setBorder(Rectangle.UNDEFINED);
			
			tablebody1.addCell(c5a);
			tablebody1.addCell(c5b);
			tablebody1.addCell(c5c);
			
			Paragraph row6a = new Paragraph();
			row6a.add(new Chunk("Date Of Joining", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row6a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c6a = new PdfPCell();
			c6a.addElement(row6a);
			c6a.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row6b = new Paragraph();
			row6b.add(new Chunk(doj, new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row6b.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c6c = new PdfPCell();
			c6c.addElement(row6b);
			c6c.setBorder(Rectangle.UNDEFINED);
			
			PdfPCell c6b = new PdfPCell();
			c6b.addElement(row1c);
			c6b.setBorder(Rectangle.UNDEFINED);
			
			tablebody1.addCell(c6a);
			tablebody1.addCell(c6b);
			tablebody1.addCell(c6c);
			
			Paragraph row7a = new Paragraph();
			row7a.add(new Chunk("Labour Card Number", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row7a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c7a = new PdfPCell();
			c7a.addElement(row7a);
			c7a.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row7b = new Paragraph();
			row7b.add(new Chunk(lcardno, new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row7b.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c7c = new PdfPCell();
			c7c.addElement(row7b);
			c7c.setBorder(Rectangle.UNDEFINED);
			
			PdfPCell c7b = new PdfPCell();
			c7b.addElement(row1c);
			c7b.setBorder(Rectangle.UNDEFINED);
			
			tablebody1.addCell(c7a);
			tablebody1.addCell(c7b);
			tablebody1.addCell(c7c);
			
			Paragraph row8a = new Paragraph();
			row8a.add(new Chunk("Bank Details", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row8a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c8a = new PdfPCell();
			c8a.addElement(row8a);
			c8a.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row8b = new Paragraph();
			row8b.add(new Chunk(bank, new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row8b.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c8c = new PdfPCell();
			c8c.addElement(row8b);
			c8c.setBorder(Rectangle.UNDEFINED);
			
			PdfPCell c8b = new PdfPCell();
			c8b.addElement(row1c);
			c8b.setBorder(Rectangle.UNDEFINED);
			
			tablebody1.addCell(c8a);
			tablebody1.addCell(c8b);
			tablebody1.addCell(c8c);
			

			Paragraph row10a = new Paragraph();
			row10a.add(new Chunk("Date Of Payment", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row10a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c10a = new PdfPCell();
			c10a.addElement(row10a);
			c10a.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row10b = new Paragraph();
			row10b.add(new Chunk(payhdr.getPAYMENT_DATE(), new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row10b.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c10c = new PdfPCell();
			c10c.addElement(row10b);
			c10c.setBorder(Rectangle.UNDEFINED);
			
			PdfPCell c10b = new PdfPCell();
			c10b.addElement(row1c);
			c10b.setBorder(Rectangle.UNDEFINED);
			
			tablebody1.addCell(c10a);
			tablebody1.addCell(c10b);
			tablebody1.addCell(c10c);
			
			
			Paragraph row10apt = new Paragraph();
			row10apt.add(new Chunk("Mode Of Payment", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row10apt.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c10apt = new PdfPCell();
			c10apt.addElement(row10apt);
			c10apt.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row10bpt = new Paragraph();
			row10bpt.add(new Chunk(payhdr.getPAYMENT_MODE(), new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row10bpt.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c10cpt = new PdfPCell();
			c10cpt.addElement(row10bpt);
			c10cpt.setBorder(Rectangle.UNDEFINED);
			
			PdfPCell c10bpt = new PdfPCell();
			c10bpt.addElement(row1c);
			c10bpt.setBorder(Rectangle.UNDEFINED);
			
			tablebody1.addCell(c10apt);
			tablebody1.addCell(c10bpt);
			tablebody1.addCell(c10cpt);
	        
			doc.add(tablebody1);
			
			
			PdfPTable tablebody2 = new PdfPTable(7);
			tablebody2.setWidthPercentage(100);
			tablebody2.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablebody2.setSpacingBefore(20f);
			
			/*Paragraph row13a = new Paragraph();
			row13a.add(new Chunk("Earnings", new Font(Font.HELVETICA, 13, Font.BOLD, Color.BLACK)));
			row13a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c13a = new PdfPCell();
			c13a.addElement(row13a);
			c13a.setBorderWidth(1);
			c13a.setBorderColor(Color.LIGHT_GRAY);
			c13a.setBackgroundColor(Color.LIGHT_GRAY);
			c13a.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c13a.setBorder(Rectangle.TOP);
			c13a.setMinimumHeight(25f);
	
			Paragraph row14b = new Paragraph();
			row14b.add(new Chunk("Amount", new Font(Font.HELVETICA, 13, Font.BOLD, Color.BLACK)));
			row14b.setAlignment(Element.ALIGN_RIGHT);
			
			
			PdfPCell c14b = new PdfPCell();
			c14b.addElement(row14b);
			c14b.setBorderColor(Color.LIGHT_GRAY);
			c14b.setBackgroundColor(Color.LIGHT_GRAY);
			c14b.setBorderWidth(1);
			c14b.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c14b.setBorder(Rectangle.TOP);
			c14b.setMinimumHeight(25f);
	
			tablebody2.addCell(c13a);
			tablebody2.addCell(c14b);*/
			String[] theading = {"Date","Claim","Description","From Place","To Place","Disctance","Amount"};
			int lt = theading.length - 1;
			for (int i = 0; i < theading.length; i++) {
				if(lt == i) {
					Paragraph row13a = new Paragraph();
					row13a.add(new Chunk(theading[i], new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
					row13a.setAlignment(Element.ALIGN_RIGHT);
					
					PdfPCell c13a = new PdfPCell();
					c13a.addElement(row13a);
					c13a.setBorderWidth(1);
					c13a.setBorderColor(Color.LIGHT_GRAY);
					c13a.setBackgroundColor(Color.LIGHT_GRAY);
					c13a.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c13a.setBorder(Rectangle.TOP);
					c13a.setMinimumHeight(25f);
			
					tablebody2.addCell(c13a);
				}else {
					Paragraph row13a = new Paragraph();
					row13a.add(new Chunk(theading[i], new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
					row13a.setAlignment(Element.ALIGN_LEFT);
					
					PdfPCell c13a = new PdfPCell();
					c13a.addElement(row13a);
					c13a.setBorderWidth(1);
					c13a.setBorderColor(Color.LIGHT_GRAY);
					c13a.setBackgroundColor(Color.LIGHT_GRAY);
					c13a.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c13a.setBorder(Rectangle.TOP);
					c13a.setMinimumHeight(25f);
			
					tablebody2.addCell(c13a);
				}
			}
			
	        
			for(HrClaimPojo claimpojo:HrClaimPojolist) {
			
				Paragraph row11a = new Paragraph();
				row11a.add(new Chunk(claimpojo.getCLAIMDATE(), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
				row11a.setAlignment(Element.ALIGN_LEFT);
				
				PdfPCell c11a = new PdfPCell();
				c11a.addElement(row11a);
				c11a.setBorderWidth(1);
				c11a.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c11a.setBorder(Rectangle.BOTTOM);
				c11a.setBorderColor(Color.LIGHT_GRAY);
				c11a.setMinimumHeight(25f);
	
				tablebody2.addCell(c11a);
				
				Paragraph row12b = new Paragraph();
				row12b.add(new Chunk(claimpojo.getCLAIMNAME(), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
				row12b.setAlignment(Element.ALIGN_LEFT);
				
				PdfPCell c12b = new PdfPCell();
				c12b.addElement(row12b);
				c12b.setBorderWidth(1);
				c12b.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c12b.setBorder(Rectangle.BOTTOM);
				c12b.setBorderColor(Color.LIGHT_GRAY);
				c12b.setMinimumHeight(25f);
	
				tablebody2.addCell(c12b);
				
				Paragraph row12b01 = new Paragraph();
				row12b01.add(new Chunk(claimpojo.getDESCRIPTION(), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
				row12b01.setAlignment(Element.ALIGN_LEFT);
				
				PdfPCell c12b01 = new PdfPCell();
				c12b01.addElement(row12b01);
				c12b01.setBorderWidth(1);
				c12b01.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c12b01.setBorder(Rectangle.BOTTOM);
				c12b01.setBorderColor(Color.LIGHT_GRAY);
				c12b01.setMinimumHeight(25f);
	
				tablebody2.addCell(c12b01);
				
				Paragraph row12b02 = new Paragraph();
				row12b02.add(new Chunk(claimpojo.getFROM_PLACE(), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
				row12b02.setAlignment(Element.ALIGN_LEFT);
				
				PdfPCell c12b02 = new PdfPCell();
				c12b02.addElement(row12b02);
				c12b02.setBorderWidth(1);
				c12b02.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c12b02.setBorder(Rectangle.BOTTOM);
				c12b02.setBorderColor(Color.LIGHT_GRAY);
				c12b02.setMinimumHeight(25f);
	
				tablebody2.addCell(c12b02);
				
				Paragraph row12b03 = new Paragraph();
				row12b03.add(new Chunk(claimpojo.getTO_PLACE(), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
				row12b03.setAlignment(Element.ALIGN_LEFT);
				
				PdfPCell c12b03 = new PdfPCell();
				c12b03.addElement(row12b03);
				c12b03.setBorderWidth(1);
				c12b03.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c12b03.setBorder(Rectangle.BOTTOM);
				c12b03.setBorderColor(Color.LIGHT_GRAY);
				c12b03.setMinimumHeight(25f);
	
				tablebody2.addCell(c12b03);
				
				Paragraph row12b04 = new Paragraph();
				row12b04.add(new Chunk(String.valueOf(claimpojo.getDISTANCE()), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
				row12b04.setAlignment(Element.ALIGN_LEFT);
				
				PdfPCell c12b04 = new PdfPCell();
				c12b04.addElement(row12b04);
				c12b04.setBorderWidth(1);
				c12b04.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c12b04.setBorder(Rectangle.BOTTOM);
				c12b04.setBorderColor(Color.LIGHT_GRAY);
				c12b04.setMinimumHeight(25f);
	
				tablebody2.addCell(c12b04);
				
				Paragraph row12b05 = new Paragraph();
				row12b05.add(new Chunk(StrUtils.addZeroes(claimpojo.getAMOUNT(), numberOfDecimal), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
				row12b05.setAlignment(Element.ALIGN_RIGHT);
				
				PdfPCell c12b05 = new PdfPCell();
				c12b05.addElement(row12b05);
				c12b05.setBorderWidth(1);
				c12b05.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c12b05.setBorder(Rectangle.BOTTOM);
				c12b05.setBorderColor(Color.LIGHT_GRAY);
				c12b05.setMinimumHeight(25f);
	
				tablebody2.addCell(c12b05);
		        
			}
			
			doc.add(tablebody2);

			PdfPTable tablebody5 = new PdfPTable(2);
			
			//int[] body5data = { 45, 10, 120 };
			//tablebody5.setWidths(body5data);
			tablebody5.setWidthPercentage(100);
			tablebody5.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablebody5.setSpacingBefore(10f);
			
			Paragraph row17a = new Paragraph();
			row17a.add(new Chunk("Total Amount", new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK)));
			row17a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c17a = new PdfPCell();
			c17a.addElement(row17a);
			c17a.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row17b = new Paragraph();
			row17b.add(new Chunk(StrUtils.addZeroes(payhdr.getAMOUNTPAID(), numberOfDecimal), new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK)));
			row17b.setAlignment(Element.ALIGN_RIGHT);
			
			PdfPCell c17c = new PdfPCell();
			c17c.addElement(row17b);
			c17c.setBorder(Rectangle.UNDEFINED);
			
			//PdfPCell c17b = new PdfPCell();
			//c17b.addElement(row1c);
			//c17b.setBorder(Rectangle.UNDEFINED);
			
			tablebody5.addCell(c17a);
			//tablebody5.addCell(c17b);
			tablebody5.addCell(c17c);
			
			doc.add(tablebody5);
			
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public JSONObject claimAttachments(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String empid = request.getSession().getAttribute("EMP_USER_ID").toString();
		StrUtils strUtils = new StrUtils();
		UserTransaction ut = null;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			try {
				ut = com.track.gates.DbBean.getUserTranaction();
				String result = "", strpath = "", catlogpath = "";
				DateUtils dateutils = new DateUtils();
				HrClaimDAO hrClaimDAO = new HrClaimDAO(); 
				EmployeeDAO employeeDAO = new EmployeeDAO();
				boolean imageSizeflg = false;
				List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				ut.begin();
				String sCustCode = employeeDAO.getEmpcode(plant, empid, "");
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (!item.isFormField() && (item.getName().length() > 0)) {

							String fileLocationATT = "C:/ATTACHMENTS/EMPLOYEECLAIM" + "/"+ sCustCode;
							String filetempLocationATT = "C:/ATTACHMENTS/EMPLOYEECLAIM" + "/temp" + "/"+ sCustCode;
							String fileName = StrUtils.fString(item.getName()).trim();
							fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							
							File path = new File(fileLocationATT);
							if (!path.exists()) {
								path.mkdirs();
							}
							
							//fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							File uploadedFile = new File(path + "/" +fileName);
							if (uploadedFile.exists()) {
								uploadedFile.delete();
							}
							
							item.write(uploadedFile);
							
							// delete temp uploaded file
							File tempPath = new File(filetempLocationATT);
							if (tempPath.exists()) {
								File tempUploadedfile = new File(tempPath + "/"+ fileName);
								if (tempUploadedfile.exists()) {
									tempUploadedfile.delete();
								}
							}
							
							
							HrClaimAttachment hrClaimAttachment = new HrClaimAttachment();
							hrClaimAttachment.setPLANT(plant);
							hrClaimAttachment.setCLAIMKEY(item.getFieldName());
							hrClaimAttachment.setFileType(item.getContentType());
							hrClaimAttachment.setFileName(fileName);
							hrClaimAttachment.setFileSize((int)item.getSize());
							hrClaimAttachment.setFilePath(fileLocationATT);
							hrClaimAttachment.setCRAT(dateutils.getDateTime());
							hrClaimAttachment.setCRBY(username);
							hrClaimAttachment.setUPAT(dateutils.getDateTime());
							hrClaimDAO.addHrClaimAttachment(hrClaimAttachment);
						}
					}

					DbBean.CommitTran(ut);

					result = "<font color=\"green\"> Claim Attachments successfully</font>";
					resultObj.put("employee", sCustCode);
					resultObj.put("message", result);
				

			} catch (Exception e) {
				DbBean.RollbackTran(ut);
			}
			resultJSON.put("result", resultObj);
		}
		return resultJSON;
	}
	
	
	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		// TODO Auto-generated method stub

	}

}
