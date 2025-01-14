package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.BillPaymentDAO;
import com.track.dao.CoaDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EmployeeLeaveDetDAO;
import com.track.dao.HrLeaveApplyDetDAO;
import com.track.dao.HrLeaveApplyHdrDAO;
import com.track.dao.HrLeaveTypeDAO;
import com.track.dao.MovHisDAO;
import com.track.db.object.BlockingDates;
import com.track.db.object.EmployeeLeaveDET;
import com.track.db.object.HrClaimAttachment;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrHolidayMst;
import com.track.db.object.HrLeaveApplyAttachment;
import com.track.db.object.HrLeaveApplyDet;
import com.track.db.object.HrLeaveApplyHdr;
import com.track.db.object.HrLeaveType;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.LeaveTypePojo;
import com.track.db.util.BillPaymentUtil;
import com.track.gates.DbBean;
import com.track.service.HrHolidayMstService;
import com.track.service.HrLeaveApplyDetService;
import com.track.service.HrLeaveApplyHdrService;
import com.track.service.JournalService;
import com.track.serviceImplementation.HrHolidayMstServiceImpl;
import com.track.serviceImplementation.HrLeaveApplyDetServiceImpl;
import com.track.serviceImplementation.HrLeaveApplyHdrServiceImpl;
import com.track.serviceImplementation.JournalEntry;
import com.track.tranaction.SendEmail;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/HrLeaveApplyServlet")
public class HrLeaveApplyServlet extends HttpServlet implements IMLogger  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		// TODO Auto-generated method stub
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("Submit")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		HrLeaveApplyHdrService hrLeaveApplyHdrService = new HrLeaveApplyHdrServiceImpl();
		HrLeaveApplyDetService hrLeaveApplyDetService = new HrLeaveApplyDetServiceImpl();
		HrHolidayMstService hrHolidayMstService = new HrHolidayMstServiceImpl();
		EmployeeLeaveDetDAO employeeLeaveDetDAO = new EmployeeLeaveDetDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
		HrLeaveTypeDAO  hrLeaveTypeDAO = new HrLeaveTypeDAO();
		SendEmail sendMail=new SendEmail();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		if(action.equalsIgnoreCase("SAVE")) {
			String empid = "", repid="", lvtid = "", nodays = "", leaveduration = "", sdate = "", sdaystatus = "",edate="",edaystatus="";
			HrLeaveApplyHdr hrLeaveApplyHdr = new HrLeaveApplyHdr();
			HrLeaveApplyHdrDAO hrLeaveApplyHdrDAO = new HrLeaveApplyHdrDAO();
			List<HrLeaveApplyAttachment> hrLeaveApplyAttachmentList = new ArrayList<HrLeaveApplyAttachment>();
			List<HrLeaveApplyDet> hrLeaveApplyDetlist = new ArrayList<HrLeaveApplyDet>();
			hrLeaveApplyHdr.setPLANT(plant);
			UserTransaction ut = null;
			String result="";
			FileItem fileUploadItem = null;
			try{
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					Iterator fileIterator = items.iterator();
					
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();						
						
						if (fileItem.isFormField()) {

							if (fileItem.getFieldName().equalsIgnoreCase("empid")) {
								empid = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("repid")) {
								repid = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("lvtid")) {
								lvtid = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("nodays")) {
								nodays = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("leaveduration")) {
								leaveduration = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("sdate")) {
								sdate = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("sdaystatus")) {
								sdaystatus = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("edate")) {
								edate = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("edaystatus")) {
								edaystatus = StrUtils.fString(fileItem.getString()).trim();
							}

						}
						
						if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							String fileLocation = "C:/ATTACHMENTS/LEAVE" + "/"+ plant + "/"+ empid;
							String filetempLocation = "C:/ATTACHMENTS/LEAVE" + "/temp" + "/"+ plant + "/"+ empid;
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
							
							// delete temp uploaded file
							File tempPath = new File(filetempLocation);
							if (tempPath.exists()) {
								File tempUploadedfile = new File(tempPath + "/"+ fileName);
								if (tempUploadedfile.exists()) {
									tempUploadedfile.delete();
								}
							}
							
							HrLeaveApplyAttachment hrLeaveApplyAttachment = new HrLeaveApplyAttachment();
							hrLeaveApplyAttachment.setPLANT(plant);
							hrLeaveApplyAttachment.setFileType(fileItem.getContentType());
							hrLeaveApplyAttachment.setFileName(fileName);
							hrLeaveApplyAttachment.setFileSize((int) fileItem.getSize());
							hrLeaveApplyAttachment.setFilePath(fileLocation);
							hrLeaveApplyAttachment.setCRAT(dateutils.getDateTime());
							hrLeaveApplyAttachment.setCRBY(username);
							hrLeaveApplyAttachment.setUPAT(dateutils.getDateTime());
							hrLeaveApplyAttachmentList.add(hrLeaveApplyAttachment);
							
						}
					}
					
					/*Get Transaction object*/
					ut = DbBean.getUserTranaction();				
					/*Begin Transaction*/
					ut.begin();
					
					hrLeaveApplyHdr.setEMPNOID(Integer.valueOf(empid));
					hrLeaveApplyHdr.setLEAVETYPEID(Integer.valueOf(lvtid));
					hrLeaveApplyHdr.setREPORT_INCHARGE_ID(Integer.valueOf(repid));
					hrLeaveApplyHdr.setNUMBEROFDAYS(Double.valueOf(nodays));
					if(leaveduration.equalsIgnoreCase("1")) {
						hrLeaveApplyHdr.setFROM_DATE(sdate);
						hrLeaveApplyHdr.setTO_DATE(sdate);
						edate = sdate;
					}else {
						hrLeaveApplyHdr.setFROM_DATE(sdate);
						hrLeaveApplyHdr.setTO_DATE(edate);
					}
					hrLeaveApplyHdr.setSTATUS("Pending");
					hrLeaveApplyHdr.setCRBY(username);
					hrLeaveApplyHdr.setCRAT(dateutils.getDateTime());
					
					int hdrid = hrLeaveApplyHdrService.addHrLeaveApplyHdr(hrLeaveApplyHdr);
					
					if(hdrid > 0) {
						List<BlockingDates> BlockingDateslist = new ArrayList<BlockingDates>();	
						
						List<HrHolidayMst> holidayList = hrHolidayMstService.getAllHoliday(plant);
						List<HrLeaveApplyDet> hrLeaveApplyDetList = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpidfullday(plant,Integer.valueOf(empid));
						List<BlockingDates> Blockingleavedays = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpidTwohalfdays(plant, Integer.valueOf(empid));
						int arrsize = holidayList.size() + hrLeaveApplyDetList.size()+Blockingleavedays.size();
						ArrayList<String> datelist = new ArrayList<String>(arrsize);
						for (HrHolidayMst HrHolidayMst : holidayList) {
							datelist.add(HrHolidayMst.getHOLIDAY_DATE());
						}	
						for (HrLeaveApplyDet HrLeaveApplyDet : hrLeaveApplyDetList) {
							datelist.add(HrLeaveApplyDet.getLEAVE_DATE());
						}
						
						for (BlockingDates Blockingleave : Blockingleavedays) {
							datelist.add(Blockingleave.getBLOCKING_DATE());
						}
						
						BlockingDateslist = getdatebetween(sdate, edate);
						
						int datesize = BlockingDateslist.size();
						int di = 0;
						for (BlockingDates blockingDates : BlockingDateslist) {
							if(!datelist.contains(blockingDates.getBLOCKING_DATE())) {
								List<HrLeaveApplyDet> hrLeaveApplyDetListdate = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpiddate(plant, Integer.valueOf(empid), blockingDates.getBLOCKING_DATE());
								if(hrLeaveApplyDetListdate.size() > 0) {
									if(hrLeaveApplyDetListdate.size() == 1) {
										HrLeaveApplyDet hrLeaveApplyDethalfday = hrLeaveApplyDetListdate.get(0);
										if(hrLeaveApplyDethalfday.getPREPOSTLUNCH().equalsIgnoreCase("Morning")) {
											HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
											hrLeaveApplyDet.setPLANT(plant);
											hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
											hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
											hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
										    hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Afternoon");
											hrLeaveApplyDet.setSTATUS("Pending");
											hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
											hrLeaveApplyDet.setCRBY(username);
											hrLeaveApplyDetlist.add(hrLeaveApplyDet);
											hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
										}
										
										if(hrLeaveApplyDethalfday.getPREPOSTLUNCH().equalsIgnoreCase("Afternoon")) {
											HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
											hrLeaveApplyDet.setPLANT(plant);
											hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
											hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
											hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
										    hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Morning");
											hrLeaveApplyDet.setSTATUS("Pending");
											hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
											hrLeaveApplyDet.setCRBY(username);
											hrLeaveApplyDetlist.add(hrLeaveApplyDet);
											hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
										}
										
									}
								}else {
									
									if(di == 0) {
										HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
										hrLeaveApplyDet.setPLANT(plant);
										hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
										hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
										hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
										if(sdaystatus.equalsIgnoreCase("Fullday")) {
											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Fullday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Fullday");
										}
										if(sdaystatus.equalsIgnoreCase("Morning")) {
											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Morning");
										}
										if(sdaystatus.equalsIgnoreCase("Afternoon")) {
											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Afternoon");
										}
										hrLeaveApplyDet.setSTATUS("Pending");
										hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
										hrLeaveApplyDet.setCRBY(username);
										hrLeaveApplyDetlist.add(hrLeaveApplyDet);
										hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
									}else if(di == (datesize - 1)) {
										HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
										hrLeaveApplyDet.setPLANT(plant);
										hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
										hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
										hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
										if(edaystatus.equalsIgnoreCase("Fullday")) {
											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Fullday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Fullday");
										}
										if(edaystatus.equalsIgnoreCase("Morning")) {
											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Morning");
										}
										if(edaystatus.equalsIgnoreCase("Afternoon")) {
											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Afternoon");
										}
										hrLeaveApplyDet.setSTATUS("Pending");
										hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
										hrLeaveApplyDet.setCRBY(username);
										hrLeaveApplyDetlist.add(hrLeaveApplyDet);
										hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
									}else {
										HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
										hrLeaveApplyDet.setPLANT(plant);
										hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
										hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
										hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
										hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Fullday");
										hrLeaveApplyDet.setPREPOSTLUNCH("Fullday");
										hrLeaveApplyDet.setSTATUS("Pending");
										hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
										hrLeaveApplyDet.setCRBY(username);
										hrLeaveApplyDetlist.add(hrLeaveApplyDet);
										hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
									}
								}
							}
							
							di++;
						}
						
						
						for(HrLeaveApplyAttachment leaveApplyAttachment:hrLeaveApplyAttachmentList) {
							leaveApplyAttachment.setLEAVEAPPLYHDRID(hdrid);
							hrLeaveApplyHdrDAO.addHrLeaveApplyAttachment(leaveApplyAttachment);
						}
						
						EmployeeLeaveDET employeelavedet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, Integer.valueOf(lvtid));
						HrLeaveType Hrleavetype = hrLeaveTypeDAO.getLeavetypeById(plant, employeelavedet.getLEAVETYPEID());
						if(Hrleavetype.getISNOPAYLEAVE() == 0) {
							double leavebal = employeelavedet.getLEAVEBALANCE() - Double.valueOf(nodays);
							employeelavedet.setLEAVEBALANCE(leavebal);
							employeeLeaveDetDAO.updateEmployeeLeavedet(employeelavedet, username);
						}
						
						ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(hrLeaveApplyHdr.getEMPNOID()),plant);
						Map empmst=(Map)empmstlist.get(0);

						ArrayList empmstlistapp = employeeDAO.getEmployeeListbyid((String)empmst.get("REPORTING_INCHARGE"),plant);
						Map empmstapp=(Map)empmstlistapp.get(0);
						
						EmployeeLeaveDET emplvtdet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, hrLeaveApplyHdr.getLEAVETYPEID());
						HrLeaveType hrLeaveType = hrLeaveTypeDAO.getLeavetypeById(plant, emplvtdet.getLEAVETYPEID());
						String body="<html><body>";
						body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
						body +="<div>";
						body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;'>";
						body +="</div>";
						body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
						body +="<span>Leave Apply</span>";
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
						body +="<td  style='width: 140px;padding: 2%;'>Leave Type</td>";
						body +="<td style='width: 20px;'>:</td>";
						body +="<td>"+hrLeaveType.getLEAVETYPE()+"</td>";
						body +="</tr>";
						body +="<tr>";
						body +="<td  style='width: 140px;padding: 2%;'>Number of Days</td>";
						body +="<td style='width: 20px;'>:</td>";
						body +="<td>"+hrLeaveApplyHdr.getNUMBEROFDAYS()+"</td>";
						body +="</tr>";
						body +="<tr>";
						body +="<td  style='width: 140px;padding: 2%;'>Status</td>";
						body +="<td style='width: 20px;'>:</td>";
						body +="<td>Pending</td>";
						body +="</tr>";
						body +="<tr>";
						body +="<td  style='width: 140px;padding: 2%;'>Notes</td>";
						body +="<td style='width: 20px;'>:</td>";
						if(hrLeaveApplyHdr.getNOTES() == null || hrLeaveApplyHdr.getNOTES().equalsIgnoreCase("null")){
							body +="<td>-</td>";
						}else {
							body +="<td>"+hrLeaveApplyHdr.getNOTES()+"</td>";
						}
						body +="</tr>";
						body +="</tbody>";
						body +="</table>";
						body +="</div>";
						body +="</div>";
						body +="<div class='row'>";
						body +="<table style='border-collapse: collapse;width:75%'>";
						body +="<thead>";
						body +="<tr>";
						body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Date</th>";
						body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Fullday/Halfday</th>";
						body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Morning/Afternoon</th>";
						body +="</tr>";
						body +="</thead>";
						body +="<tbody>";
						for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist){
							body +="<tr>";
							body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getLEAVE_DATE()+"</td>";
							body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getPREPOSTLUNCHTYPE()+"</td>";
							if(hrLeaveApplyDet.getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")){
								body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>-</td>";
							}else {
								body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getPREPOSTLUNCH()+"</td>";
							}
							body +="</tr>";
						}
						body +="</tbody>";
						body +="</table>";
						body +="</div>";
						body +="</div>";
						body +="<div style='margin-top:30px'>";
						body +="<a href='https://ordermgt.u-clo.com/track/jsp/EmployeeLogin.jsp'>click here to login</a>";
						body +="</div>";
						body +="<div style='margin-top:30px'>";
						body +="<p>Regards <br>"+empmst.get("FNAME")+"</p>";
						/* body +="<p style='margin-top:-14px'>"+empmstapp.get("FNAME")+"</p>"; */
						body +="</div>";
						body +="</div>";
						body +=" </body></html>";
						String subject ="";
						if(hrLeaveApplyHdr.getNUMBEROFDAYS() > 1) {
							subject = "RE:Leave request for your approval "+empmst.get("FNAME")+" from "+hrLeaveApplyHdr.getFROM_DATE()+" to "+hrLeaveApplyHdr.getTO_DATE();
						}else {
							if(hrLeaveApplyDetlist.get(0).getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")) {
								subject = "RE:Leave request for your approval "+empmst.get("FNAME")+"-"+hrLeaveApplyHdr.getFROM_DATE();
							}else {
								subject = "RE:Leave request for your approval "+empmst.get("FNAME")+"-"+hrLeaveApplyHdr.getFROM_DATE()+" "+hrLeaveApplyDetlist.get(0).getPREPOSTLUNCH();
							}
						}
						
						try {
							String to = (String)empmstapp.get("EMAIL");
							sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");
						}catch (Exception e) {
							System.out.println(e);
						}
			
						
						Hashtable htMovHis = new Hashtable();
	 					htMovHis.clear();
	 					htMovHis.put(IDBConstants.PLANT, plant);					
	 					htMovHis.put("DIRTYPE", TransactionConstants.APPLY_LEAVE);	
	 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
	 					htMovHis.put("RECID", hdrid);
	 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
	 					htMovHis.put(IDBConstants.CREATED_BY, username);		
	 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
	 					htMovHis.put("REMARKS",hrLeaveApplyHdr.getEMPNOID()+","+hrLeaveApplyHdr.getLEAVETYPEID()+","+hrLeaveApplyHdr.getFROM_DATE()+","+hrLeaveApplyHdr.getTO_DATE()+","+hrLeaveApplyHdr.getNUMBEROFDAYS());
	 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						
						
						DbBean.CommitTran(ut);
						result = "Leave applied successfully";
						response.sendRedirect("jsp/ApplyLeave.jsp?result="+ result);	
					}else {
						DbBean.RollbackTran(ut);
						result = "Unable to apply leave";
						response.sendRedirect("jsp/ApplyLeave.jsp?result="+result);
					}
					
									
				}
			}catch (Exception e) {
				 DbBean.RollbackTran(ut);
				 e.printStackTrace();
				 result = "Unable to apply leave";
				 response.sendRedirect("jsp/ApplyLeave.jsp?result="+result);
			}
		
			
		}
		
		if(action.equalsIgnoreCase("EMP_SAVE")) {
			String empid = "", repid="", lvtid = "", nodays = "", leaveduration = "", sdate = "", sdaystatus = "",edate="",edaystatus="";
			HrLeaveApplyHdr hrLeaveApplyHdr = new HrLeaveApplyHdr();
			HrLeaveApplyHdrDAO hrLeaveApplyHdrDAO = new HrLeaveApplyHdrDAO();
			List<HrLeaveApplyAttachment> hrLeaveApplyAttachmentList = new ArrayList<HrLeaveApplyAttachment>();
			List<HrLeaveApplyDet> hrLeaveApplyDetlist = new ArrayList<HrLeaveApplyDet>();
			hrLeaveApplyHdr.setPLANT(plant);
			UserTransaction ut = null;
			String result="";
			FileItem fileUploadItem = null;
			try{
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					Iterator fileIterator = items.iterator();
					
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();						
						
						if (fileItem.isFormField()) {

							if (fileItem.getFieldName().equalsIgnoreCase("empid")) {
								empid = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("repid")) {
								repid = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("lvtid")) {
								lvtid = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("nodays")) {
								nodays = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("leaveduration")) {
								leaveduration = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("sdate")) {
								sdate = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("sdaystatus")) {
								sdaystatus = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("edate")) {
								edate = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("edaystatus")) {
								edaystatus = StrUtils.fString(fileItem.getString()).trim();
							}

						}
						
						if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							String fileLocation = "C:/ATTACHMENTS/LEAVE" + "/"+ plant + "/"+ empid;
							String filetempLocation = "C:/ATTACHMENTS/LEAVE" + "/temp" + "/"+ plant + "/"+ empid;
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
							
							// delete temp uploaded file
							File tempPath = new File(filetempLocation);
							if (tempPath.exists()) {
								File tempUploadedfile = new File(tempPath + "/"+ fileName);
								if (tempUploadedfile.exists()) {
									tempUploadedfile.delete();
								}
							}
							
							HrLeaveApplyAttachment hrLeaveApplyAttachment = new HrLeaveApplyAttachment();
							hrLeaveApplyAttachment.setPLANT(plant);
							hrLeaveApplyAttachment.setFileType(fileItem.getContentType());
							hrLeaveApplyAttachment.setFileName(fileName);
							hrLeaveApplyAttachment.setFileSize((int) fileItem.getSize());
							hrLeaveApplyAttachment.setFilePath(fileLocation);
							hrLeaveApplyAttachment.setCRAT(dateutils.getDateTime());
							hrLeaveApplyAttachment.setCRBY(username);
							hrLeaveApplyAttachment.setUPAT(dateutils.getDateTime());
							hrLeaveApplyAttachmentList.add(hrLeaveApplyAttachment);
							
						}
					}
					
					/*Get Transaction object*/
					ut = DbBean.getUserTranaction();				
					/*Begin Transaction*/
					ut.begin();
					
					hrLeaveApplyHdr.setEMPNOID(Integer.valueOf(empid));
					hrLeaveApplyHdr.setLEAVETYPEID(Integer.valueOf(lvtid));
					hrLeaveApplyHdr.setREPORT_INCHARGE_ID(Integer.valueOf(repid));
					hrLeaveApplyHdr.setNUMBEROFDAYS(Double.valueOf(nodays));
					if(leaveduration.equalsIgnoreCase("1")) {
						hrLeaveApplyHdr.setFROM_DATE(sdate);
						hrLeaveApplyHdr.setTO_DATE(sdate);
						edate = sdate;
					}else {
						hrLeaveApplyHdr.setFROM_DATE(sdate);
						hrLeaveApplyHdr.setTO_DATE(edate);
					}
					hrLeaveApplyHdr.setSTATUS("Pending");
					hrLeaveApplyHdr.setCRBY(username);
					hrLeaveApplyHdr.setCRAT(dateutils.getDateTime());
					
					int hdrid = hrLeaveApplyHdrService.addHrLeaveApplyHdr(hrLeaveApplyHdr);
					
					if(hdrid > 0) {
						List<BlockingDates> BlockingDateslist = new ArrayList<BlockingDates>();	
						
						List<HrHolidayMst> holidayList = hrHolidayMstService.getAllHoliday(plant);
						List<HrLeaveApplyDet> hrLeaveApplyDetList = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpidfullday(plant,Integer.valueOf(empid));
						List<BlockingDates> Blockingleavedays = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpidTwohalfdays(plant, Integer.valueOf(empid));
						int arrsize = holidayList.size() + hrLeaveApplyDetList.size()+Blockingleavedays.size();
						ArrayList<String> datelist = new ArrayList<String>(arrsize);
						for (HrHolidayMst HrHolidayMst : holidayList) {
							datelist.add(HrHolidayMst.getHOLIDAY_DATE());
						}	
						for (HrLeaveApplyDet HrLeaveApplyDet : hrLeaveApplyDetList) {
							datelist.add(HrLeaveApplyDet.getLEAVE_DATE());
						}
						
						for (BlockingDates Blockingleave : Blockingleavedays) {
							datelist.add(Blockingleave.getBLOCKING_DATE());
						}
						
						BlockingDateslist = getdatebetween(sdate, edate);
						
						int datesize = BlockingDateslist.size();
						int di = 0;
						for (BlockingDates blockingDates : BlockingDateslist) {
							if(!datelist.contains(blockingDates.getBLOCKING_DATE())) {
								List<HrLeaveApplyDet> hrLeaveApplyDetListdate = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpiddate(plant, Integer.valueOf(empid), blockingDates.getBLOCKING_DATE());
								if(hrLeaveApplyDetListdate.size() > 0) {
									if(hrLeaveApplyDetListdate.size() == 1) {
										HrLeaveApplyDet hrLeaveApplyDethalfday = hrLeaveApplyDetListdate.get(0);
										if(hrLeaveApplyDethalfday.getPREPOSTLUNCH().equalsIgnoreCase("Morning")) {
											HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
											hrLeaveApplyDet.setPLANT(plant);
											hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
											hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
											hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
										    hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Afternoon");
											hrLeaveApplyDet.setSTATUS("Pending");
											hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
											hrLeaveApplyDet.setCRBY(username);
											hrLeaveApplyDetlist.add(hrLeaveApplyDet);
											hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
										}
										
										if(hrLeaveApplyDethalfday.getPREPOSTLUNCH().equalsIgnoreCase("Afternoon")) {
											HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
											hrLeaveApplyDet.setPLANT(plant);
											hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
											hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
											hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
										    hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Morning");
											hrLeaveApplyDet.setSTATUS("Pending");
											hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
											hrLeaveApplyDet.setCRBY(username);
											hrLeaveApplyDetlist.add(hrLeaveApplyDet);
											hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
										}
										
									}
								}else {
									
									if(di == 0) {
										HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
										hrLeaveApplyDet.setPLANT(plant);
										hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
										hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
										hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
										if(sdaystatus.equalsIgnoreCase("Fullday")) {
											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Fullday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Fullday");
										}
										if(sdaystatus.equalsIgnoreCase("Morning")) {
											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Morning");
										}
										if(sdaystatus.equalsIgnoreCase("Afternoon")) {
											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Afternoon");
										}
										hrLeaveApplyDet.setSTATUS("Pending");
										hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
										hrLeaveApplyDet.setCRBY(username);
										hrLeaveApplyDetlist.add(hrLeaveApplyDet);
										hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
									}else if(di == (datesize - 1)) {
										HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
										hrLeaveApplyDet.setPLANT(plant);
										hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
										hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
										hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
										if(edaystatus.equalsIgnoreCase("Fullday")) {
											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Fullday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Fullday");
										}
										if(edaystatus.equalsIgnoreCase("Morning")) {
											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Morning");
										}
										if(edaystatus.equalsIgnoreCase("Afternoon")) {
											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
											hrLeaveApplyDet.setPREPOSTLUNCH("Afternoon");
										}
										hrLeaveApplyDet.setSTATUS("Pending");
										hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
										hrLeaveApplyDet.setCRBY(username);
										hrLeaveApplyDetlist.add(hrLeaveApplyDet);
										hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
									}else {
										HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
										hrLeaveApplyDet.setPLANT(plant);
										hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
										hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
										hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
										hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Fullday");
										hrLeaveApplyDet.setPREPOSTLUNCH("Fullday");
										hrLeaveApplyDet.setSTATUS("Pending");
										hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
										hrLeaveApplyDet.setCRBY(username);
										hrLeaveApplyDetlist.add(hrLeaveApplyDet);
										hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
									}
								}
							}
							
							di++;
						}
						
						
						for(HrLeaveApplyAttachment leaveApplyAttachment:hrLeaveApplyAttachmentList) {
							leaveApplyAttachment.setLEAVEAPPLYHDRID(hdrid);
							hrLeaveApplyHdrDAO.addHrLeaveApplyAttachment(leaveApplyAttachment);
						}
						
						
						
						EmployeeLeaveDET employeelavedet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, Integer.valueOf(lvtid));
						HrLeaveType Hrleavetype = hrLeaveTypeDAO.getLeavetypeById(plant, employeelavedet.getLEAVETYPEID());
						if(Hrleavetype.getISNOPAYLEAVE() == 0) {
							double leavebal = employeelavedet.getLEAVEBALANCE() - Double.valueOf(nodays);
							employeelavedet.setLEAVEBALANCE(leavebal);
							employeeLeaveDetDAO.updateEmployeeLeavedet(employeelavedet, username);
						}
						
						ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(hrLeaveApplyHdr.getEMPNOID()),plant);
						Map empmst=(Map)empmstlist.get(0);

						ArrayList empmstlistapp = employeeDAO.getEmployeeListbyid((String)empmst.get("REPORTING_INCHARGE"),plant);
						Map empmstapp=(Map)empmstlistapp.get(0);
						
						EmployeeLeaveDET emplvtdet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, hrLeaveApplyHdr.getLEAVETYPEID());
						HrLeaveType hrLeaveType = hrLeaveTypeDAO.getLeavetypeById(plant, emplvtdet.getLEAVETYPEID());
						String body="";
						body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
						body +="<div>";
						body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;'>";
						body +="</div>";
						body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
						body +="<span>Leave Apply</span>";
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
						body +="<td  style='width: 140px;padding: 2%;'>Leave Type</td>";
						body +="<td style='width: 20px;'>:</td>";
						body +="<td>"+hrLeaveType.getLEAVETYPE()+"</td>";
						body +="</tr>";
						body +="<tr>";
						body +="<td  style='width: 140px;padding: 2%;'>Number of Days</td>";
						body +="<td style='width: 20px;'>:</td>";
						body +="<td>"+hrLeaveApplyHdr.getNUMBEROFDAYS()+"</td>";
						body +="</tr>";
						body +="<tr>";
						body +="<td  style='width: 140px;padding: 2%;'>Status</td>";
						body +="<td style='width: 20px;'>:</td>";
						body +="<td>Pending</td>";
						body +="</tr>";
						body +="<tr>";
						body +="<td  style='width: 140px;padding: 2%;'>Notes</td>";
						body +="<td style='width: 20px;'>:</td>";
						if(hrLeaveApplyHdr.getNOTES() == null || hrLeaveApplyHdr.getNOTES().equalsIgnoreCase("null")){
							body +="<td>-</td>";
						}else {
							body +="<td>"+hrLeaveApplyHdr.getNOTES()+"</td>";
						}
						body +="</tr>";
						body +="</tbody>";
						body +="</table>";
						body +="</div>";
						body +="</div>";
						body +="<div class='row'>";
						body +="<table style='border-collapse: collapse;width:75%'>";
						body +="<thead>";
						body +="<tr>";
						body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Date</th>";
						body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Fullday/Halfday</th>";
						body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Morning/Afternoon</th>";
						body +="</tr>";
						body +="</thead>";
						body +="<tbody>";
						for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist){
							body +="<tr>";
							body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getLEAVE_DATE()+"</td>";
							body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getPREPOSTLUNCHTYPE()+"</td>";
							if(hrLeaveApplyDet.getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")){
								body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>-</td>";
							}else {
								body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getPREPOSTLUNCH()+"</td>";
							}
							body +="</tr>";
						}
						body +="</tbody>";
						body +="</table>";
						body +="</div>";
						body +="</div>";
						body +="<div style='margin-top:30px'>";
						body +="<a href='https://ordermgt.u-clo.com/track/jsp/EmployeeLogin.jsp'>click here to login</a>";
						body +="</div>";
						body +="<div style='margin-top:30px'>";
						body +="<p>Regards <br>"+empmst.get("FNAME")+"</p>";
						/* body +="<p style='margin-top:-14px'>"+empmstapp.get("FNAME")+"</p>"; */
						body +="</div>";
						body +="</div>";
						String subject ="";
						if(hrLeaveApplyHdr.getNUMBEROFDAYS() > 1) {
							subject = "RE:Leave request for your approval "+empmst.get("FNAME")+" from "+hrLeaveApplyHdr.getFROM_DATE()+" to "+hrLeaveApplyHdr.getTO_DATE();
						}else {
							if(hrLeaveApplyDetlist.get(0).getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")) {
								subject = "RE:Leave request for your approval "+empmst.get("FNAME")+"-"+hrLeaveApplyHdr.getFROM_DATE();
							}else {
								subject = "RE:Leave request for your approval "+empmst.get("FNAME")+" - "+hrLeaveApplyHdr.getFROM_DATE()+" "+hrLeaveApplyDetlist.get(0).getPREPOSTLUNCH();
							}
						}
						try {
						String to = (String)empmstapp.get("EMAIL");
					    sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");
						}catch (Exception e) {
							System.out.println(e);
						}
						
						Hashtable htMovHis = new Hashtable();
	 					htMovHis.clear();
	 					htMovHis.put(IDBConstants.PLANT, plant);					
	 					htMovHis.put("DIRTYPE", TransactionConstants.APPLY_LEAVE);	
	 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
	 					htMovHis.put("RECID", hdrid);
	 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
	 					htMovHis.put(IDBConstants.CREATED_BY, username);		
	 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
	 					htMovHis.put("REMARKS",hrLeaveApplyHdr.getEMPNOID()+","+hrLeaveApplyHdr.getLEAVETYPEID()+","+hrLeaveApplyHdr.getFROM_DATE()+","+hrLeaveApplyHdr.getTO_DATE()+","+hrLeaveApplyHdr.getNUMBEROFDAYS());
	 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						
						
						DbBean.CommitTran(ut);
						result = "Leave applied successfully";
						response.sendRedirect("jsp/EmpLeaveApply.jsp?rsuccess="+ result);	
					}else {
						DbBean.RollbackTran(ut);
						result = "Unable to apply leave";
						response.sendRedirect("jsp/EmpLeaveApply.jsp?resultnew="+ result);
					}
					
									
				}
			}catch (Exception e) {
				 DbBean.RollbackTran(ut);
				 e.printStackTrace();
				 result = "Unable to apply leave";
				 response.sendRedirect("jsp/EmpLeaveApply.jsp?resultnew="+ result);
			}
		
			
		}
		
		if(action.equalsIgnoreCase("APPROVE_LEAVE_LIST")) {
			String[] appcheck  = request.getParameterValues("appcheck");
			String[] reasoncheck  = request.getParameterValues("reasoncheck");
			HrLeaveApplyDetDAO hrLeaveApplyDetDAO = new HrLeaveApplyDetDAO();
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
					HrLeaveApplyHdr hrLeaveApplyHdr = hrLeaveApplyHdrService.getHrLeaveApplyHdrById(plant, Integer.valueOf(hdrid));
					List<HrLeaveApplyDet> hrLeaveApplyDetlist = hrLeaveApplyDetDAO.getHrLeaveApplyDetbyhdrid(plant, Integer.valueOf(hdrid));
					for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist) {
						hrLeaveApplyDet.setSTATUS("Approved");
						hrLeaveApplyDetDAO.updateHrLeaveApplyDet(hrLeaveApplyDet, username);
					}
					hrLeaveApplyHdr.setSTATUS("Approved");
					hrLeaveApplyHdr.setREASON(reason);
					hrLeaveApplyHdrService.updateHrLeaveApplyHdr(hrLeaveApplyHdr, username);
					
					ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(hrLeaveApplyHdr.getEMPNOID()),plant);
					Map empmst=(Map)empmstlist.get(0);

					ArrayList empmstlistapp = employeeDAO.getEmployeeListbyid((String)empmst.get("REPORTING_INCHARGE"),plant);
					Map empmstapp=(Map)empmstlistapp.get(0);
					
					EmployeeLeaveDET emplvtdet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, hrLeaveApplyHdr.getLEAVETYPEID());
					HrLeaveType hrLeaveType = hrLeaveTypeDAO.getLeavetypeById(plant, emplvtdet.getLEAVETYPEID());
					String body="";
					body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
					body +="<div>";
					body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;'>";
					body +="</div>";
					body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
					body +="<span>Leave Approved</span>";
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
					body +="<td  style='width: 140px;padding: 2%;'>Leave Type</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+hrLeaveType.getLEAVETYPE()+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Number of Days</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+hrLeaveApplyHdr.getNUMBEROFDAYS()+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Status</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>Approved</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Reason</td>";
					body +="<td style='width: 20px;'>:</td>";
					if(hrLeaveApplyHdr.getREASON() == null || hrLeaveApplyHdr.getREASON().equalsIgnoreCase("null")){
						body +="<td>-</td>";
					}else {
						body +="<td>"+hrLeaveApplyHdr.getREASON()+"</td>";
					}
					body +="</tr>";
					body +="</tbody>";
					body +="</table>";
					body +="</div>";
					body +="</div>";
					body +="<div class='row'>";
					body +="<table style='border-collapse: collapse;width:75%'>";
					body +="<thead>";
					body +="<tr>";
					body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Date</th>";
					body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Fullday/Halfday</th>";
					body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Morning/Afternoon</th>";
					body +="</tr>";
					body +="</thead>";
					body +="<tbody>";
					for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist){
						body +="<tr>";
						body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getLEAVE_DATE()+"</td>";
						body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getPREPOSTLUNCHTYPE()+"</td>";
						if(hrLeaveApplyDet.getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")){
							body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>-</td>";
						}else {
							body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getPREPOSTLUNCH()+"</td>";
						}
						body +="</tr>";
					}
					body +="</tbody>";
					body +="</table>";
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
					if(hrLeaveApplyHdr.getNUMBEROFDAYS() > 1) {
						subject = "RE:Leave Approved "+empmst.get("FNAME")+" from "+hrLeaveApplyHdr.getFROM_DATE()+" to "+hrLeaveApplyHdr.getTO_DATE();
					}else {
						if(hrLeaveApplyDetlist.get(0).getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")) {
							subject = "RE:Leave Approved "+empmst.get("FNAME")+"-"+hrLeaveApplyHdr.getFROM_DATE();
						}else {
							subject = "RE:Leave Approved "+empmst.get("FNAME")+" - "+hrLeaveApplyHdr.getFROM_DATE()+" "+hrLeaveApplyDetlist.get(0).getPREPOSTLUNCH();
						}
					}
					
					try {
					String to = (String)empmst.get("EMAIL");
				    sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");
					}catch (Exception e) {
						System.out.println(e);
					}
					
					Hashtable htMovHis = new Hashtable();
 					htMovHis.clear();
 					htMovHis.put(IDBConstants.PLANT, plant);					
 					htMovHis.put("DIRTYPE", TransactionConstants.APPLY_LEAVE);	
 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
 					htMovHis.put("RECID", hdrid);
 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
 					htMovHis.put(IDBConstants.CREATED_BY, username);		
 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
 					htMovHis.put("REMARKS","Approved,"+hrLeaveApplyHdr.getEMPNOID()+","+hrLeaveApplyHdr.getLEAVETYPEID()+","+hrLeaveApplyHdr.getFROM_DATE()+","+hrLeaveApplyHdr.getTO_DATE()+","+hrLeaveApplyHdr.getNUMBEROFDAYS());
 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
				}
					
				DbBean.CommitTran(ut);
				result = "Leave Approved Successfully";
				response.sendRedirect("jsp/EmpDashboard.jsp?rsuccess="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Leave Approve failed";
				response.sendRedirect("jsp/EmpDashboard.jsp?resultnew="+ result);
			}

		}
		
		if(action.equalsIgnoreCase("REJECT_LEAVE_LIST")) {
			String[] appcheck  = request.getParameterValues("appcheck");
			String[] reasoncheck  = request.getParameterValues("reasoncheck");
			HrLeaveApplyDetDAO hrLeaveApplyDetDAO = new HrLeaveApplyDetDAO();
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
					HrLeaveApplyHdr hrLeaveApplyHdr = hrLeaveApplyHdrService.getHrLeaveApplyHdrById(plant, Integer.valueOf(hdrid));
					List<HrLeaveApplyDet> hrLeaveApplyDetlist = hrLeaveApplyDetDAO.getHrLeaveApplyDetbyhdrid(plant, Integer.valueOf(hdrid));
					for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist) {
						hrLeaveApplyDet.setSTATUS("Rejected");
						hrLeaveApplyDetDAO.updateHrLeaveApplyDet(hrLeaveApplyDet, username);
					}
					
					EmployeeLeaveDET employeeLeaveDet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, hrLeaveApplyHdr.getLEAVETYPEID());
					HrLeaveType Hrleavetype = hrLeaveTypeDAO.getLeavetypeById(plant, employeeLeaveDet.getLEAVETYPEID());
					if(Hrleavetype.getISNOPAYLEAVE() == 0) {
						double leavebalance = employeeLeaveDet.getLEAVEBALANCE() + hrLeaveApplyHdr.getNUMBEROFDAYS();
						employeeLeaveDet.setLEAVEBALANCE(leavebalance);
						employeeLeaveDetDAO.updateEmployeeLeavedet(employeeLeaveDet, username);
					}
					
					hrLeaveApplyHdr.setSTATUS("Rejected");
					hrLeaveApplyHdr.setREASON(reason);
					hrLeaveApplyHdrService.updateHrLeaveApplyHdr(hrLeaveApplyHdr, username);
					

					ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(hrLeaveApplyHdr.getEMPNOID()),plant);
					Map empmst=(Map)empmstlist.get(0);

					ArrayList empmstlistapp = employeeDAO.getEmployeeListbyid((String)empmst.get("REPORTING_INCHARGE"),plant);
					Map empmstapp=(Map)empmstlistapp.get(0);
					
					EmployeeLeaveDET emplvtdet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, hrLeaveApplyHdr.getLEAVETYPEID());
					HrLeaveType hrLeaveType = hrLeaveTypeDAO.getLeavetypeById(plant, emplvtdet.getLEAVETYPEID());
					String body="";
					body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
					body +="<div>";
					body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;'>";
					body +="</div>";
					body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
					body +="<span>Leave Rejected</span>";
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
					body +="<td  style='width: 140px;padding: 2%;'>Leave Type</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+hrLeaveType.getLEAVETYPE()+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Number of Days</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>"+hrLeaveApplyHdr.getNUMBEROFDAYS()+"</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Status</td>";
					body +="<td style='width: 20px;'>:</td>";
					body +="<td>Rejected</td>";
					body +="</tr>";
					body +="<tr>";
					body +="<td  style='width: 140px;padding: 2%;'>Reason</td>";
					body +="<td style='width: 20px;'>:</td>";
					if(hrLeaveApplyHdr.getREASON() == null || hrLeaveApplyHdr.getREASON().equalsIgnoreCase("null")){
						body +="<td>-</td>";
					}else {
						body +="<td>"+hrLeaveApplyHdr.getREASON()+"</td>";
					}
					body +="</tr>";
					body +="</tbody>";
					body +="</table>";
					body +="</div>";
					body +="</div>";
					body +="<div class='row'>";
					body +="<table style='border-collapse: collapse;width:75%'>";
					body +="<thead>";
					body +="<tr>";
					body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Date</th>";
					body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Fullday/Halfday</th>";
					body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Morning/Afternoon</th>";
					body +="</tr>";
					body +="</thead>";
					body +="<tbody>";
					for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist){
						body +="<tr>";
						body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getLEAVE_DATE()+"</td>";
						body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getPREPOSTLUNCHTYPE()+"</td>";
						if(hrLeaveApplyDet.getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")){
							body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>-</td>";
						}else {
							body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getPREPOSTLUNCH()+"</td>";
						}
						body +="</tr>";
					}
					body +="</tbody>";
					body +="</table>";
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
					if(hrLeaveApplyHdr.getNUMBEROFDAYS() > 1) {
						subject = "RE:Leave Rejected "+empmst.get("FNAME")+" from "+hrLeaveApplyHdr.getFROM_DATE()+" to "+hrLeaveApplyHdr.getTO_DATE();
					}else {
						if(hrLeaveApplyDetlist.get(0).getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")) {
							subject = "RE:Leave Rejected "+empmst.get("FNAME")+"-"+hrLeaveApplyHdr.getFROM_DATE();
						}else {
							subject = "RE:Leave Rejected "+empmst.get("FNAME")+" - "+hrLeaveApplyHdr.getFROM_DATE()+" "+hrLeaveApplyDetlist.get(0).getPREPOSTLUNCH();
						}
					}
					
					try {
					String to = (String)empmst.get("EMAIL");
				    sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");
					}catch (Exception e) {
						System.out.println(e);
					}
					
					Hashtable htMovHis = new Hashtable();
 					htMovHis.clear();
 					htMovHis.put(IDBConstants.PLANT, plant);					
 					htMovHis.put("DIRTYPE", TransactionConstants.APPLY_LEAVE);	
 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
 					htMovHis.put("RECID", hdrid);
 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
 					htMovHis.put(IDBConstants.CREATED_BY, username);		
 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
 					htMovHis.put("REMARKS","Rejected,"+hrLeaveApplyHdr.getEMPNOID()+","+hrLeaveApplyHdr.getLEAVETYPEID()+","+hrLeaveApplyHdr.getFROM_DATE()+","+hrLeaveApplyHdr.getTO_DATE()+","+hrLeaveApplyHdr.getNUMBEROFDAYS());
 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
				}
					
				DbBean.CommitTran(ut);
				result = "Leave Rejected Successfully";
				response.sendRedirect("jsp/EmpDashboard.jsp?rsuccess="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Leave Reject failed";
				response.sendRedirect("jsp/EmpDashboard.jsp?resultnew="+ result);
			}
		}
		
		
		if(action.equalsIgnoreCase("downloadAttachmentById")) {
			
			System.out.println("Attachments by ID");
			int ID=Integer.parseInt(request.getParameter("attachid"));
			HrLeaveApplyHdrDAO hrLeaveApplyHdrDAO =new HrLeaveApplyHdrDAO();
			FileHandling fileHandling=new FileHandling(); 
			try {
				
				HrLeaveApplyAttachment hrLeaveApplyAttachment = hrLeaveApplyHdrDAO.getHrLeaveApplyAttachmentById(plant, ID);
				String filePath=hrLeaveApplyAttachment.getFilePath();
				String fileType=hrLeaveApplyAttachment.getFileType();
				String fileName=hrLeaveApplyAttachment.getFileName();
				fileHandling.fileDownload(filePath, fileName, fileType, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
		
		if(action.equalsIgnoreCase("APPROVE_LEAVE")) {
			
			String hdrid = StrUtils.fString(request.getParameter("HDRID"));
			String reason = StrUtils.fString(request.getParameter("REASON"));
			HrLeaveApplyDetDAO hrLeaveApplyDetDAO = new HrLeaveApplyDetDAO();

			JSONObject resultJson = new JSONObject();
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				HrLeaveApplyHdr hrLeaveApplyHdr = hrLeaveApplyHdrService.getHrLeaveApplyHdrById(plant, Integer.valueOf(hdrid));
				List<HrLeaveApplyDet> hrLeaveApplyDetlist = hrLeaveApplyDetDAO.getHrLeaveApplyDetbyhdrid(plant, Integer.valueOf(hdrid));
				for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist) {
					hrLeaveApplyDet.setSTATUS("Approved");
					hrLeaveApplyDetDAO.updateHrLeaveApplyDet(hrLeaveApplyDet, username);
				}
				hrLeaveApplyHdr.setSTATUS("Approved");
				hrLeaveApplyHdr.setREASON(reason);
				hrLeaveApplyHdrService.updateHrLeaveApplyHdr(hrLeaveApplyHdr, username);
				
				ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(hrLeaveApplyHdr.getEMPNOID()),plant);
				Map empmst=(Map)empmstlist.get(0);

				ArrayList empmstlistapp = employeeDAO.getEmployeeListbyid((String)empmst.get("REPORTING_INCHARGE"),plant);
				Map empmstapp=(Map)empmstlistapp.get(0);
				
				EmployeeLeaveDET emplvtdet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, hrLeaveApplyHdr.getLEAVETYPEID());
				HrLeaveType hrLeaveType = hrLeaveTypeDAO.getLeavetypeById(plant, emplvtdet.getLEAVETYPEID());
				String body="";
				body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
				body +="<div>";
				body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;'>";
				body +="</div>";
				body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
				body +="<span>Leave Approved</span>";
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
				body +="<td  style='width: 140px;padding: 2%;'>Leave Type</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+hrLeaveType.getLEAVETYPE()+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Number of Days</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+hrLeaveApplyHdr.getNUMBEROFDAYS()+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Status</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>Approved</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Reason</td>";
				body +="<td style='width: 20px;'>:</td>";
				if(hrLeaveApplyHdr.getREASON() == null || hrLeaveApplyHdr.getREASON().equalsIgnoreCase("null")){
					body +="<td>-</td>";
				}else {
					body +="<td>"+hrLeaveApplyHdr.getREASON()+"</td>";
				}
				body +="</tr>";
				body +="</tbody>";
				body +="</table>";
				body +="</div>";
				body +="</div>";
				body +="<div class='row'>";
				body +="<table style='border-collapse: collapse;width:75%'>";
				body +="<thead>";
				body +="<tr>";
				body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Date</th>";
				body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Fullday/Halfday</th>";
				body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Morning/Afternoon</th>";
				body +="</tr>";
				body +="</thead>";
				body +="<tbody>";
				for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist){
					body +="<tr>";
					body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getLEAVE_DATE()+"</td>";
					body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getPREPOSTLUNCHTYPE()+"</td>";
					if(hrLeaveApplyDet.getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")){
						body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>-</td>";
					}else {
						body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getPREPOSTLUNCH()+"</td>";
					}
					body +="</tr>";
				}
				body +="</tbody>";
				body +="</table>";
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
				if(hrLeaveApplyHdr.getNUMBEROFDAYS() > 1) {
					subject = "RE:Leave Approved "+empmst.get("FNAME")+" from "+hrLeaveApplyHdr.getFROM_DATE()+" to "+hrLeaveApplyHdr.getTO_DATE();
				}else {
					if(hrLeaveApplyDetlist.get(0).getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")) {
						subject = "RE:Leave Approved "+empmst.get("FNAME")+"-"+hrLeaveApplyHdr.getFROM_DATE();
					}else {
						subject = "RE:Leave Approved "+empmst.get("FNAME")+" - "+hrLeaveApplyHdr.getFROM_DATE()+" "+hrLeaveApplyDetlist.get(0).getPREPOSTLUNCH();
					}
				}
				
				try {
				String to = (String)empmst.get("EMAIL");
			    sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");
				}catch (Exception e) {
					System.out.println(e);
				}
				
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.APPLY_LEAVE);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
					htMovHis.put("RECID", hdrid);
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS","Approved,"+hrLeaveApplyHdr.getEMPNOID()+","+hrLeaveApplyHdr.getLEAVETYPEID()+","+hrLeaveApplyHdr.getFROM_DATE()+","+hrLeaveApplyHdr.getTO_DATE()+","+hrLeaveApplyHdr.getNUMBEROFDAYS());
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
		
		if(action.equalsIgnoreCase("REJECT_LEAVE")) {
			String hdrid = StrUtils.fString(request.getParameter("HDRID"));
			String reason = StrUtils.fString(request.getParameter("REASON"));
			HrLeaveApplyDetDAO hrLeaveApplyDetDAO = new HrLeaveApplyDetDAO();
			JSONObject resultJson = new JSONObject();
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();

				HrLeaveApplyHdr hrLeaveApplyHdr = hrLeaveApplyHdrService.getHrLeaveApplyHdrById(plant, Integer.valueOf(hdrid));
				List<HrLeaveApplyDet> hrLeaveApplyDetlist = hrLeaveApplyDetDAO.getHrLeaveApplyDetbyhdrid(plant, Integer.valueOf(hdrid));
				for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist) {
					hrLeaveApplyDet.setSTATUS("Rejected");
					hrLeaveApplyDetDAO.updateHrLeaveApplyDet(hrLeaveApplyDet, username);
				}
					
				EmployeeLeaveDET employeeLeaveDet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, hrLeaveApplyHdr.getLEAVETYPEID());
				HrLeaveType Hrleavetype = hrLeaveTypeDAO.getLeavetypeById(plant, employeeLeaveDet.getLEAVETYPEID());
				if(Hrleavetype.getISNOPAYLEAVE() == 0) {
					double leavebalance = employeeLeaveDet.getLEAVEBALANCE() + hrLeaveApplyHdr.getNUMBEROFDAYS();
					employeeLeaveDet.setLEAVEBALANCE(leavebalance);
					employeeLeaveDetDAO.updateEmployeeLeavedet(employeeLeaveDet, username);
				}
					
				hrLeaveApplyHdr.setSTATUS("Rejected");
				hrLeaveApplyHdr.setREASON(reason);
				hrLeaveApplyHdrService.updateHrLeaveApplyHdr(hrLeaveApplyHdr, username);
				
				
				
				ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(hrLeaveApplyHdr.getEMPNOID()),plant);
				Map empmst=(Map)empmstlist.get(0);

				ArrayList empmstlistapp = employeeDAO.getEmployeeListbyid((String)empmst.get("REPORTING_INCHARGE"),plant);
				Map empmstapp=(Map)empmstlistapp.get(0);
				
				EmployeeLeaveDET emplvtdet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, hrLeaveApplyHdr.getLEAVETYPEID());
				HrLeaveType hrLeaveType = hrLeaveTypeDAO.getLeavetypeById(plant, emplvtdet.getLEAVETYPEID());
				String body="";
				body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
				body +="<div>";
				body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;'>";
				body +="</div>";
				body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
				body +="<span>Leave Rejected</span>";
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
				body +="<td  style='width: 140px;padding: 2%;'>Leave Type</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+hrLeaveType.getLEAVETYPE()+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Number of Days</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+hrLeaveApplyHdr.getNUMBEROFDAYS()+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Status</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>Rejected</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Reason</td>";
				body +="<td style='width: 20px;'>:</td>";
				if(hrLeaveApplyHdr.getREASON() == null || hrLeaveApplyHdr.getREASON().equalsIgnoreCase("null")){
					body +="<td>-</td>";
				}else {
					body +="<td>"+hrLeaveApplyHdr.getREASON()+"</td>";
				}
				body +="</tr>";
				body +="</tbody>";
				body +="</table>";
				body +="</div>";
				body +="</div>";
				body +="<div class='row'>";
				body +="<table style='border-collapse: collapse;width:75%'>";
				body +="<thead>";
				body +="<tr>";
				body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Date</th>";
				body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Fullday/Halfday</th>";
				body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Morning/Afternoon</th>";
				body +="</tr>";
				body +="</thead>";
				body +="<tbody>";
				for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist){
					body +="<tr>";
					body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getLEAVE_DATE()+"</td>";
					body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getPREPOSTLUNCHTYPE()+"</td>";
					if(hrLeaveApplyDet.getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")){
						body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>-</td>";
					}else {
						body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getPREPOSTLUNCH()+"</td>";
					}
					body +="</tr>";
				}
				body +="</tbody>";
				body +="</table>";
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
				if(hrLeaveApplyHdr.getNUMBEROFDAYS() > 1) {
					subject = "RE:Leave Rejected "+empmst.get("FNAME")+" from "+hrLeaveApplyHdr.getFROM_DATE()+" to "+hrLeaveApplyHdr.getTO_DATE();
				}else {
					if(hrLeaveApplyDetlist.get(0).getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")) {
						subject = "RE:Leave Rejected "+empmst.get("FNAME")+"-"+hrLeaveApplyHdr.getFROM_DATE();
					}else {
						subject = "RE:Leave Rejected "+empmst.get("FNAME")+"-"+hrLeaveApplyHdr.getFROM_DATE()+" "+hrLeaveApplyDetlist.get(0).getPREPOSTLUNCH();
					}
				}
				
				try {
				String to = (String)empmst.get("EMAIL");
			    sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");
				}catch (Exception e) {
					System.out.println(e);
				}
				
				
				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.APPLY_LEAVE);	
				htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
				htMovHis.put("RECID", hdrid);
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				htMovHis.put("REMARKS","Rejected,"+hrLeaveApplyHdr.getEMPNOID()+","+hrLeaveApplyHdr.getLEAVETYPEID()+","+hrLeaveApplyHdr.getFROM_DATE()+","+hrLeaveApplyHdr.getTO_DATE()+","+hrLeaveApplyHdr.getNUMBEROFDAYS());
				movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
				DbBean.CommitTran(ut);
				resultJson.put("STATUS", "OK");
				/*
				 * result = "Leave Rejected Successfully";
				 * response.sendRedirect("jsp/EmpDashboard.jsp?rsuccess="+ result);
				 */
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				resultJson.put("STATUS", "NOT OK");
				/*
				 * result = "Leave Reject failed";
				 * response.sendRedirect("jsp/EmpDashboard.jsp?resultnew="+ result);
				 */
			}
			
			response.getWriter().write(resultJson.toString());	
		}
		
		if(action.equalsIgnoreCase("CANCEL_LEAVE")) {
			String hdrid = StrUtils.fString(request.getParameter("HDRID"));
			String reason = StrUtils.fString(request.getParameter("REASON"));
			HrLeaveApplyDetDAO hrLeaveApplyDetDAO = new HrLeaveApplyDetDAO();
			JSONObject resultJson = new JSONObject();
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();

				HrLeaveApplyHdr hrLeaveApplyHdr = hrLeaveApplyHdrService.getHrLeaveApplyHdrById(plant, Integer.valueOf(hdrid));
				List<HrLeaveApplyDet> hrLeaveApplyDetlist = hrLeaveApplyDetDAO.getHrLeaveApplyDetbyhdrid(plant, Integer.valueOf(hdrid));
				for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist) {
					hrLeaveApplyDet.setSTATUS("Cancelled");
					hrLeaveApplyDetDAO.updateHrLeaveApplyDet(hrLeaveApplyDet, username);
				}
					
				EmployeeLeaveDET employeeLeaveDet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, hrLeaveApplyHdr.getLEAVETYPEID());
				HrLeaveType Hrleavetype = hrLeaveTypeDAO.getLeavetypeById(plant, employeeLeaveDet.getLEAVETYPEID());
				if(Hrleavetype.getISNOPAYLEAVE() == 0) {
					double leavebalance = employeeLeaveDet.getLEAVEBALANCE() + hrLeaveApplyHdr.getNUMBEROFDAYS();
					employeeLeaveDet.setLEAVEBALANCE(leavebalance);
					employeeLeaveDetDAO.updateEmployeeLeavedet(employeeLeaveDet, username);
				}
					
				hrLeaveApplyHdr.setSTATUS("Cancelled");
				hrLeaveApplyHdr.setREASON(reason);
				hrLeaveApplyHdrService.updateHrLeaveApplyHdr(hrLeaveApplyHdr, username);
				
				
				ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(hrLeaveApplyHdr.getEMPNOID()),plant);
				Map empmst=(Map)empmstlist.get(0);

				ArrayList empmstlistapp = employeeDAO.getEmployeeListbyid((String)empmst.get("REPORTING_INCHARGE"),plant);
				Map empmstapp=(Map)empmstlistapp.get(0);
				
				EmployeeLeaveDET emplvtdet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, hrLeaveApplyHdr.getLEAVETYPEID());
				HrLeaveType hrLeaveType = hrLeaveTypeDAO.getLeavetypeById(plant, emplvtdet.getLEAVETYPEID());
				String body="";
				body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
				body +="<div>";
				body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;'>";
				body +="</div>";
				body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
				body +="<span>Leave Cancelled</span>";
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
				body +="<td  style='width: 140px;padding: 2%;'>Leave Type</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+hrLeaveType.getLEAVETYPE()+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Number of Days</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>"+hrLeaveApplyHdr.getNUMBEROFDAYS()+"</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Status</td>";
				body +="<td style='width: 20px;'>:</td>";
				body +="<td>Cancelled</td>";
				body +="</tr>";
				body +="<tr>";
				body +="<td  style='width: 140px;padding: 2%;'>Reason</td>";
				body +="<td style='width: 20px;'>:</td>";
				if(hrLeaveApplyHdr.getREASON() == null || hrLeaveApplyHdr.getREASON().equalsIgnoreCase("null")){
					body +="<td>-</td>";
				}else {
					body +="<td>"+hrLeaveApplyHdr.getREASON()+"</td>";
				}
				body +="</tr>";
				body +="</tbody>";
				body +="</table>";
				body +="</div>";
				body +="</div>";
				body +="<div class='row'>";
				body +="<table style='border-collapse: collapse;width:75%'>";
				body +="<thead>";
				body +="<tr>";
				body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Date</th>";
				body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Fullday/Halfday</th>";
				body +="<th style='border: 1px solid white;padding: 5px;text-align: left;background-color: steelblue;color: white;font-weight: initial;'>Morning/Afternoon</th>";
				body +="</tr>";
				body +="</thead>";
				body +="<tbody>";
				for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist){
					body +="<tr>";
					body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getLEAVE_DATE()+"</td>";
					body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getPREPOSTLUNCHTYPE()+"</td>";
					if(hrLeaveApplyDet.getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")){
						body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>-</td>";
					}else {
						body +="<td style='border: 1px solid white;padding: 8px;background: #eaeaea8c;color: currentcolor'>"+hrLeaveApplyDet.getPREPOSTLUNCH()+"</td>";
					}
					body +="</tr>";
				}
				body +="</tbody>";
				body +="</table>";
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
				if(hrLeaveApplyHdr.getNUMBEROFDAYS() > 1) {
					subject = "RE:Leave Cancelled "+empmst.get("FNAME")+" from "+hrLeaveApplyHdr.getFROM_DATE()+" to "+hrLeaveApplyHdr.getTO_DATE();
				}else {
					if(hrLeaveApplyDetlist.get(0).getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")) {
						subject = "RE:Leave Cancelled "+empmst.get("FNAME")+"-"+hrLeaveApplyHdr.getFROM_DATE();
					}else {
						subject = "RE:Leave Cancelled "+empmst.get("FNAME")+"-"+hrLeaveApplyHdr.getFROM_DATE()+" "+hrLeaveApplyDetlist.get(0).getPREPOSTLUNCH();
					}
				}
				
				try {
				String to = (String)empmst.get("EMAIL");
			    sendMail.sendTOMail("info@u-clo.com", to, "", "", subject, body, "");
				}catch (Exception e) {
					System.out.println(e);
				}
				
				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.APPLY_LEAVE);	
				htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
				htMovHis.put("RECID", hdrid);
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				htMovHis.put("REMARKS","Cancelled,"+hrLeaveApplyHdr.getEMPNOID()+","+hrLeaveApplyHdr.getLEAVETYPEID()+","+hrLeaveApplyHdr.getFROM_DATE()+","+hrLeaveApplyHdr.getTO_DATE()+","+hrLeaveApplyHdr.getNUMBEROFDAYS());
				movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
				DbBean.CommitTran(ut);
				resultJson.put("STATUS", "OK");
				/*
				 * result = "Leave cancelled Successfully";
				 * response.sendRedirect("jsp/EmpDashboard.jsp?rsuccess="+ result);
				 */
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				resultJson.put("STATUS", "NOT OK");
				/*
				 * result = "Leave cancel failed";
				 * response.sendRedirect("jsp/EmpDashboard.jsp?resultnew="+ result);
				 */
			}
			
			response.getWriter().write(resultJson.toString());	
		
		}
		
		if(action.equalsIgnoreCase("downloadAttachmentById"))
		{
			System.out.println("Attachments by ID");
			int ID=Integer.parseInt(request.getParameter("attachid"));
			FileHandling fileHandling=new FileHandling(); 
			HrLeaveApplyHdrDAO hrLeaveApplyHdrDAO = new HrLeaveApplyHdrDAO();
			try {
				HrLeaveApplyAttachment  hrLeaveApplyAttachment = hrLeaveApplyHdrDAO.getHrLeaveApplyAttachmentById(plant, ID);
				String filePath=hrLeaveApplyAttachment.getFilePath();
				String fileType=hrLeaveApplyAttachment.getFileType();
				String fileName=hrLeaveApplyAttachment.getFileName();
				fileHandling.fileDownload(filePath, fileName, fileType, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("CMD")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		HrHolidayMstService hrHolidayMstService = new HrHolidayMstServiceImpl();
		HrLeaveApplyDetService hrLeaveApplyDetService = new HrLeaveApplyDetServiceImpl();
		HrLeaveApplyHdrService hrLeaveApplyHdrService = new HrLeaveApplyHdrServiceImpl();
		EmployeeLeaveDetDAO employeeLeaveDetDAO = new EmployeeLeaveDetDAO();
		HrLeaveTypeDAO  hrLeaveTypeDAO = new HrLeaveTypeDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		
		if(action.equalsIgnoreCase("GET_HOLIDAY_LEAVE_DATE"))
		{
			String balace = "0";
			String empnoid = StrUtils.fString(request.getParameter("EMPID"));
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			
			try {
				List<BlockingDates> BlockingDateslist = new ArrayList<BlockingDates>();	
				List<HrHolidayMst> holidayList = hrHolidayMstService.getAllHoliday(plant);
				for (HrHolidayMst HrHolidayMst : holidayList) {
					BlockingDates BlockingDates = new BlockingDates();
					BlockingDates.setBLOCKING_DATE(HrHolidayMst.getHOLIDAY_DATE());
					BlockingDateslist.add(BlockingDates);
				}
				List<HrLeaveApplyDet> hrLeaveApplyDetList = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpidfullday(plant,Integer.valueOf(empnoid));
				for (HrLeaveApplyDet HrLeaveApplyDet : hrLeaveApplyDetList) {
					BlockingDates BlockingDates = new BlockingDates();
					BlockingDates.setBLOCKING_DATE(HrLeaveApplyDet.getLEAVE_DATE());
					BlockingDateslist.add(BlockingDates);
				}
				
				List<BlockingDates> Blockingleavedays = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpidTwohalfdays(plant, Integer.valueOf(empnoid));
				for (BlockingDates Blockingleave : Blockingleavedays) {
					BlockingDates BlockingDates = new BlockingDates();
					BlockingDates.setBLOCKING_DATE(Blockingleave.getBLOCKING_DATE());
					BlockingDateslist.add(BlockingDates);
				}

				resultJson.put("BLOCKLIST", BlockingDateslist);   
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			response.getWriter().write(resultJson.toString());
			
		}
		
		if(action.equalsIgnoreCase("GET_NO_OF_DAYS"))
		{
			String empnoid = StrUtils.fString(request.getParameter("EMPID"));
			String strdate = StrUtils.fString(request.getParameter("STRDATE"));
			String enddate = StrUtils.fString(request.getParameter("ENDDATE"));
			String sstatus = StrUtils.fString(request.getParameter("SSTATUS"));
			String estatus = StrUtils.fString(request.getParameter("ESTATUS"));
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			try {
				double ndays = 0.0;
				List<BlockingDates> BlockingDateslist = new ArrayList<BlockingDates>();	
				
				List<HrHolidayMst> holidayList = hrHolidayMstService.getAllHoliday(plant);
				List<HrLeaveApplyDet> hrLeaveApplyDetList = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpidfullday(plant,Integer.valueOf(empnoid));
				List<BlockingDates> Blockingleavedays = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpidTwohalfdays(plant, Integer.valueOf(empnoid));
				int arrsize = holidayList.size() + hrLeaveApplyDetList.size()+Blockingleavedays.size();
				ArrayList<String> datelist = new ArrayList<String>(arrsize);
				for (HrHolidayMst HrHolidayMst : holidayList) {
					datelist.add(HrHolidayMst.getHOLIDAY_DATE());
				}	
				for (HrLeaveApplyDet HrLeaveApplyDet : hrLeaveApplyDetList) {
					datelist.add(HrLeaveApplyDet.getLEAVE_DATE());
				}
				
				for (BlockingDates Blockingleave : Blockingleavedays) {
					datelist.add(Blockingleave.getBLOCKING_DATE());
				}
				
				BlockingDateslist = getdatebetween(strdate, enddate);
				int datesize = BlockingDateslist.size();
				int di = 0;
				for (BlockingDates blockingDates : BlockingDateslist) {
					if(datelist.contains(blockingDates.getBLOCKING_DATE())) {
						ndays = ndays + 0.0;
					}else {
						List<HrLeaveApplyDet> hrLeaveApplyDetListdate = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpiddate(plant, Integer.valueOf(empnoid), blockingDates.getBLOCKING_DATE());
						if(hrLeaveApplyDetListdate.size() > 0) {
							if(hrLeaveApplyDetListdate.size() == 1) {
								ndays = ndays + 0.5;
							}
						}else {
							if(di == 0) {
								if(sstatus.equalsIgnoreCase("Fullday")) {
									ndays = ndays + 1.0;
								}
								
								if(sstatus.equalsIgnoreCase("Morning")) {
									ndays = ndays + 0.5;
								}
								
								if(sstatus.equalsIgnoreCase("Afternoon")) {
									ndays = ndays + 0.5;
								}
								
							}else if(di == (datesize -1)) {
								if(estatus.equalsIgnoreCase("Fullday")) {
									ndays = ndays + 1.0;
								}
								
								if(estatus.equalsIgnoreCase("Morning")) {
									ndays = ndays + 0.5;
								}
								
								if(estatus.equalsIgnoreCase("Afternoon")) {
									ndays = ndays + 0.5;
								}
							}else {
								ndays = ndays + 1.0;
							}
							
						}
					}
					
					di++;
				}
				resultJson.put("NDAYS", ndays);   
			} catch (Exception e) {
				resultJson.put("NDAYS", "0.0");   
			}
			response.getWriter().write(resultJson.toString());
			
		}
		
		if(action.equalsIgnoreCase("GET_FULL_OR_HALFDAY_STATUS"))
		{
			String empnoid = StrUtils.fString(request.getParameter("EMPID"));
			String leavedate = StrUtils.fString(request.getParameter("LEAVEDATE"));
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			try {
				String fday = "Fullday";
				String daysession = "Fullday";
				List<HrLeaveApplyDet> hrLeaveApplyDetList = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpiddate(plant,Integer.valueOf(empnoid),leavedate);
				if(hrLeaveApplyDetList.size() > 0) {
					if(hrLeaveApplyDetList.size() == 1) {
						HrLeaveApplyDet hrLeaveApplyDet = hrLeaveApplyDetList.get(0);
						if(hrLeaveApplyDet.getPREPOSTLUNCH().equalsIgnoreCase("Morning")) {
							fday = "Halfday";
							daysession = "Afternoon";
						}else {
							fday = "Halfday";
							daysession = "Morning";
						}
					}
				}
				resultJson.put("STATUS", "OK");
				resultJson.put("DAY", fday); 
				resultJson.put("DAYSESSION", daysession); 
			} catch (Exception e) {
				resultJson.put("STATUS", "NOT OK");
			}
			response.getWriter().write(resultJson.toString());
			
		}
		
		if(action.equalsIgnoreCase("APPROVE_LEAVE")) {
			
			String hdrid = StrUtils.fString(request.getParameter("HDRID"));
			HrLeaveApplyDetDAO hrLeaveApplyDetDAO = new HrLeaveApplyDetDAO();
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				HrLeaveApplyHdr hrLeaveApplyHdr = hrLeaveApplyHdrService.getHrLeaveApplyHdrById(plant, Integer.valueOf(hdrid));
				List<HrLeaveApplyDet> hrLeaveApplyDetlist = hrLeaveApplyDetDAO.getHrLeaveApplyDetbyhdrid(plant, Integer.valueOf(hdrid));
				for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist) {
					hrLeaveApplyDet.setSTATUS("Approved");
					hrLeaveApplyDetDAO.updateHrLeaveApplyDet(hrLeaveApplyDet, username);
				}
				hrLeaveApplyHdr.setSTATUS("Approved");
				hrLeaveApplyHdrService.updateHrLeaveApplyHdr(hrLeaveApplyHdr, username);
				
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.APPLY_LEAVE);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
					htMovHis.put("RECID", hdrid);
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS","Approved,"+hrLeaveApplyHdr.getEMPNOID()+","+hrLeaveApplyHdr.getLEAVETYPEID()+","+hrLeaveApplyHdr.getFROM_DATE()+","+hrLeaveApplyHdr.getTO_DATE()+","+hrLeaveApplyHdr.getNUMBEROFDAYS());
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS

				DbBean.CommitTran(ut);
				result = "Leave Approved Successfully";
				response.sendRedirect("jsp/EmpDashboard.jsp?rsuccess="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Leave Approve failed";
				response.sendRedirect("jsp/EmpDashboard.jsp?resultnew="+ result);
			}
		}
		
		if(action.equalsIgnoreCase("REJECT_LEAVE")) {
			String hdrid = StrUtils.fString(request.getParameter("HDRID"));
			HrLeaveApplyDetDAO hrLeaveApplyDetDAO = new HrLeaveApplyDetDAO();
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();

				HrLeaveApplyHdr hrLeaveApplyHdr = hrLeaveApplyHdrService.getHrLeaveApplyHdrById(plant, Integer.valueOf(hdrid));
				List<HrLeaveApplyDet> hrLeaveApplyDetlist = hrLeaveApplyDetDAO.getHrLeaveApplyDetbyhdrid(plant, Integer.valueOf(hdrid));
				for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist) {
					hrLeaveApplyDet.setSTATUS("Rejected");
					hrLeaveApplyDetDAO.updateHrLeaveApplyDet(hrLeaveApplyDet, username);
				}
					
				EmployeeLeaveDET employeeLeaveDet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, hrLeaveApplyHdr.getLEAVETYPEID());
				HrLeaveType Hrleavetype = hrLeaveTypeDAO.getLeavetypeById(plant, employeeLeaveDet.getLEAVETYPEID());
				if(Hrleavetype.getISNOPAYLEAVE() == 0) {
					double leavebalance = employeeLeaveDet.getLEAVEBALANCE() + hrLeaveApplyHdr.getNUMBEROFDAYS();
					employeeLeaveDet.setLEAVEBALANCE(leavebalance);
					employeeLeaveDetDAO.updateEmployeeLeavedet(employeeLeaveDet, username);
				}
					
				hrLeaveApplyHdr.setSTATUS("Rejected");
				hrLeaveApplyHdrService.updateHrLeaveApplyHdr(hrLeaveApplyHdr, username);
				
				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.APPLY_LEAVE);	
				htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
				htMovHis.put("RECID", hdrid);
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				htMovHis.put("REMARKS","Rejected,"+hrLeaveApplyHdr.getEMPNOID()+","+hrLeaveApplyHdr.getLEAVETYPEID()+","+hrLeaveApplyHdr.getFROM_DATE()+","+hrLeaveApplyHdr.getTO_DATE()+","+hrLeaveApplyHdr.getNUMBEROFDAYS());
				movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
				DbBean.CommitTran(ut);
				result = "Leave Rejected Successfully";
				response.sendRedirect("jsp/EmpDashboard.jsp?rsuccess="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Leave Reject failed";
				response.sendRedirect("jsp/EmpDashboard.jsp?resultnew="+ result);
			}
		}
		
		if(action.equalsIgnoreCase("CANCEL_LEAVE")) {
			String hdrid = StrUtils.fString(request.getParameter("HDRID"));
			HrLeaveApplyDetDAO hrLeaveApplyDetDAO = new HrLeaveApplyDetDAO();
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();

				HrLeaveApplyHdr hrLeaveApplyHdr = hrLeaveApplyHdrService.getHrLeaveApplyHdrById(plant, Integer.valueOf(hdrid));
				List<HrLeaveApplyDet> hrLeaveApplyDetlist = hrLeaveApplyDetDAO.getHrLeaveApplyDetbyhdrid(plant, Integer.valueOf(hdrid));
				for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist) {
					hrLeaveApplyDet.setSTATUS("Cancelled");
					hrLeaveApplyDetDAO.updateHrLeaveApplyDet(hrLeaveApplyDet, username);
				}
					
				EmployeeLeaveDET employeeLeaveDet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, hrLeaveApplyHdr.getLEAVETYPEID());
				HrLeaveType Hrleavetype = hrLeaveTypeDAO.getLeavetypeById(plant, employeeLeaveDet.getLEAVETYPEID());
				if(Hrleavetype.getISNOPAYLEAVE() == 0) {
					double leavebalance = employeeLeaveDet.getLEAVEBALANCE() + hrLeaveApplyHdr.getNUMBEROFDAYS();
					employeeLeaveDet.setLEAVEBALANCE(leavebalance);
					employeeLeaveDetDAO.updateEmployeeLeavedet(employeeLeaveDet, username);
				}
					
				hrLeaveApplyHdr.setSTATUS("Cancelled");
				hrLeaveApplyHdrService.updateHrLeaveApplyHdr(hrLeaveApplyHdr, username);
				
				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.APPLY_LEAVE);	
				htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
				htMovHis.put("RECID", hdrid);
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				htMovHis.put("REMARKS","Cancelled,"+hrLeaveApplyHdr.getEMPNOID()+","+hrLeaveApplyHdr.getLEAVETYPEID()+","+hrLeaveApplyHdr.getFROM_DATE()+","+hrLeaveApplyHdr.getTO_DATE()+","+hrLeaveApplyHdr.getNUMBEROFDAYS());
				movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
				DbBean.CommitTran(ut);
				result = "Leave cancelled Successfully";
				response.sendRedirect("jsp/EmpDashboard.jsp?rsuccess="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Leave cancel failed";
				response.sendRedirect("jsp/EmpDashboard.jsp?resultnew="+ result);
			}
		
		}
		
	}
		
	public List<BlockingDates> getdatebetween(String str_date,String end_date) throws ParseException{
		List<Date> dates = new ArrayList<Date>();
		List<BlockingDates> BetweenDateslist = new ArrayList<BlockingDates>();	
		DateFormat formatter ; 
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date  startDate = (Date)formatter.parse(str_date); 
		Date  endDate = (Date)formatter.parse(end_date);
		long interval = 24*1000 * 60 * 60; // 1 hour in millis
		long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
		long curTime = startDate.getTime();
		while (curTime <= endTime) {
		    dates.add(new Date(curTime));
		    curTime += interval;
		}
		for(int i=0;i<dates.size();i++){
		    Date lDate =(Date)dates.get(i);
		    String ds = formatter.format(lDate);    
		    BlockingDates blockingDates = new BlockingDates();
		    blockingDates.setBLOCKING_DATE(ds);
		    BetweenDateslist.add(blockingDates);
		}
		return BetweenDateslist;
	}


}
