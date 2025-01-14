package com.track.servlet;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CoaDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EmployeeLeaveDetDAO;
import com.track.dao.HrClaimDAO;
import com.track.dao.HrDeductionDetDAO;
import com.track.dao.HrDeductionHdrDAO;
import com.track.dao.HrEmpSalaryDAO;
import com.track.dao.HrEmpSalaryDetDAO;
import com.track.dao.HrLeaveApplyDetDAO;
import com.track.dao.HrLeaveApplyHdrDAO;
import com.track.dao.HrLeaveTypeDAO;
import com.track.dao.HrPayrollAdditionMstDAO;
import com.track.dao.HrPayrollDETDAO;
import com.track.dao.HrPayrollHDRDAO;
import com.track.dao.HrPayrollPaymentDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.db.object.EmployeeLeaveDET;
import com.track.db.object.HrDeductionDet;
import com.track.db.object.HrDeductionHdr;
import com.track.db.object.HrEmpSalaryDET;
import com.track.db.object.HrEmpSalaryMst;
import com.track.db.object.HrLeaveApplyDet;
import com.track.db.object.HrLeaveApplyHdr;
import com.track.db.object.HrLeaveType;
import com.track.db.object.HrPayrollAdditionMst;
import com.track.db.object.HrPayrollDET;
import com.track.db.object.HrPayrollHDR;
import com.track.db.object.HrPayrollPaymentHdr;
import com.track.db.object.HrpayrollDeductionPojo;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.MonthYearPojo;
import com.track.db.object.PayrollDeductionPojo;
import com.track.db.object.PayrollPojo;
import com.track.db.util.CoaUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.gates.userBean;
import com.track.service.HrPayrollDETService;
import com.track.service.HrPayrollHDRService;
import com.track.service.JournalService;
import com.track.serviceImplementation.HrPayrollDETServiceImpl;
import com.track.serviceImplementation.HrPayrollHDRServiceImpl;
import com.track.serviceImplementation.JournalEntry;
import com.track.tranaction.SendEmail;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/PayrollServlet")
public class PayrollServlet extends HttpServlet implements IMLogger  {

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
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		PlantMstDAO  plantMstDAO = new PlantMstDAO();
		HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
		HrDeductionHdrDAO hrDeductionHdrDAO = new HrDeductionHdrDAO();
		HrDeductionDetDAO hrDeductionDetDAO = new HrDeductionDetDAO();
		HrPayrollHDRService hrPayrollHDRService = new HrPayrollHDRServiceImpl();
		HrPayrollDETService HrPayrollDETService = new HrPayrollDETServiceImpl();
		HrPayrollDETDAO hrPayrollDETDAO = new HrPayrollDETDAO();
		JSONObject jsonObjectResult = new JSONObject();
		
		
		if(action.equalsIgnoreCase("paysearch"))
		{
			
			String filterid=StrUtils.fString(request.getParameter("selfilter"));
			String employeeid=StrUtils.fString(request.getParameter("empid"));
			String fmonth=StrUtils.fString(request.getParameter("from_month"));
			String fyear=StrUtils.fString(request.getParameter("from_year"));
			String tmonth=StrUtils.fString(request.getParameter("to_month"));
			String tyear=StrUtils.fString(request.getParameter("to_year"));

			try {
				response.sendRedirect("jsp/DownloadPayslip.jsp?selfilter="+ filterid+"&empid="+ employeeid+"&from_year="+ fyear+"&to_month="+ tmonth+"&to_year="+ tyear+"&from_month="+ fmonth+"&action=ADD");
			} catch (Exception e) {
				response.sendRedirect("jsp/DownloadPayslip.jsp");
				
			}
		}
		
		if(action.equalsIgnoreCase("Save"))
		{
			
			String empid = StrUtils.fString(request.getParameter("pay_empid"));
			String payroll = StrUtils.fString(request.getParameter("pay_no"));
			String fromdate = StrUtils.fString(request.getParameter("pay_fromdate"));
			String todate = StrUtils.fString(request.getParameter("pay_todate"));
			String month= StrUtils.fString(request.getParameter("pay_month"));
			String year= StrUtils.fString(request.getParameter("pay_year"));
			String paydays= StrUtils.fString(request.getParameter("pay_attendance"));
			String netamount = StrUtils.fString(request.getParameter("pay_netamount"));
			String paydate= StrUtils.fString(request.getParameter("pay_date"));
			/*
			 * String paymode= StrUtils.fString(request.getParameter("payment_mode"));
			 * String paidthrough=
			 * StrUtils.fString(request.getParameter("paid_through_account_name")); String
			 * bankname= StrUtils.fString(request.getParameter("bankname")); String
			 * chequeno= StrUtils.fString(request.getParameter("chequeno")); String
			 * chequedate= StrUtils.fString(request.getParameter("chequedate")); String
			 * chequeamount= StrUtils.fString(request.getParameter("chequeamount"));
			 */
			String reference= StrUtils.fString(request.getParameter("refrence"));
			String notes= StrUtils.fString(request.getParameter("note"));
			String ischeque= StrUtils.fString(request.getParameter("pay_ischeque"));
			
			String[] payadditionname = request.getParameterValues("pay_addname");
			String[] payadditionvalue = request.getParameterValues("pay_addamount");
			String[] paydeductionname = request.getParameterValues("pay_deductname");
			String[] paydeducttionvalue = request.getParameterValues("pay_deductamount");
			String[] paydeductid = request.getParameterValues("pay_deductid");
			CoaDAO coaDAO = new CoaDAO();
			EmployeeDAO employeeDAO = new EmployeeDAO();
	        UserTransaction ut = null;
			try {
				
				ut = DbBean.getUserTranaction();				
				ut.begin();
				double tamount =0;
				double damount =0;
				List<JournalDetail> journalDetails=new ArrayList<>();
				List<JournalDetail> journalDetailDetection=new ArrayList<>();
				
				HrPayrollHDR hrPayrollHDR = new HrPayrollHDR();
				
				hrPayrollHDR.setPLANT(plant);
				hrPayrollHDR.setEMPID(Integer.valueOf(empid));
				hrPayrollHDR.setPAYROLL(payroll);
				hrPayrollHDR.setFROMDATE(fromdate);
				hrPayrollHDR.setTODATE(todate);
				hrPayrollHDR.setMONTH(month);
				hrPayrollHDR.setYEAR(year);
				hrPayrollHDR.setPAYDAYS(Double.valueOf(paydays));
				hrPayrollHDR.setTOTAL_AMOUNT(Double.valueOf(netamount));
				hrPayrollHDR.setPAYMENT_DATE(paydate);
				/*
				hrPayrollHDR.setPAYMENT_MODE(paymode);
				hrPayrollHDR.setPAID_THROUGH(paidthrough);
				if(ischeque.equalsIgnoreCase("1")) {
				hrPayrollHDR.setBANK_BRANCH(bankname);
				hrPayrollHDR.setCHECQUE_NO(chequeno);
				hrPayrollHDR.setCHEQUE_DATE(chequedate);
				hrPayrollHDR.setCHEQUE_AMOUNT(Double.valueOf(chequeamount));
				}else {
					hrPayrollHDR.setBANK_BRANCH("");
					hrPayrollHDR.setCHECQUE_NO("");
					hrPayrollHDR.setCHEQUE_DATE("");
					hrPayrollHDR.setCHEQUE_AMOUNT(Double.valueOf("0"));
				}
				*/
				hrPayrollHDR.setPAYMENT_MODE("");
				hrPayrollHDR.setPAID_THROUGH("");
				hrPayrollHDR.setBANK_BRANCH("");
				hrPayrollHDR.setCHECQUE_NO("");
				hrPayrollHDR.setCHEQUE_DATE("");
				hrPayrollHDR.setCHEQUE_AMOUNT(Double.valueOf("0"));
				hrPayrollHDR.setREFERENCE(reference);
				hrPayrollHDR.setNOTE(notes);
				hrPayrollHDR.setSTATUS("OPEN");
				hrPayrollHDR.setCRBY(username);
				
				int hdrid = hrPayrollHDRService.addpayrollhdr(hrPayrollHDR);
				
				if(hdrid > 0) {
					List<HrEmpSalaryDET> HrEmpSalaryDETList = hrEmpSalaryDetDAO.EmpSalarydetlistbyempid(plant, Integer.valueOf(empid));
					int k=0;
					for(HrEmpSalaryDET hrEmpSalaryDET:HrEmpSalaryDETList) {
						
						String saltype = hrEmpSalaryDET.getSALARYTYPE();
						saltype = saltype.replaceAll(" ", "");
						String etval = saltype+"_"+empid+"_B";
						String saltypeamount = StrUtils.fString(request.getParameter(etval));
						
						HrPayrollDET hrPayrollDET = new HrPayrollDET();
						hrPayrollDET.setPLANT(plant);
						hrPayrollDET.setHDRID(hdrid);
						hrPayrollDET.setLNNO(k);
						hrPayrollDET.setSALARYTYPE(hrEmpSalaryDET.getSALARYTYPE());
						hrPayrollDET.setAMOUNT(Double.valueOf(saltypeamount));
						hrPayrollDET.setAMOUNT_TYPE("ADDITION");
						hrPayrollDET.setCRBY(username);
						HrPayrollDETService.addpayrolldet(hrPayrollDET);
						
						tamount = tamount + Double.valueOf(saltypeamount);
						
						JournalDetail journalDetail=new JournalDetail();
						journalDetail.setPLANT(plant);
						JSONObject coaJson2=coaDAO.getCOAByName(plant, hrEmpSalaryDET.getSALARYTYPE());
						journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
						journalDetail.setACCOUNT_NAME(hrEmpSalaryDET.getSALARYTYPE());
						journalDetail.setDEBITS(Double.valueOf(saltypeamount));
						journalDetails.add(journalDetail);
						
						k++;	
					}
					
					if(payadditionvalue != null) {
						for (int i = 0; i < payadditionvalue.length; i++) {
							if(!payadditionname[i].equalsIgnoreCase("")) {
								HrPayrollDET hrPayrollDET = new HrPayrollDET();
								hrPayrollDET.setPLANT(plant);
								hrPayrollDET.setHDRID(hdrid);
								hrPayrollDET.setLNNO(i);
								hrPayrollDET.setSALARYTYPE(payadditionname[i]);
								hrPayrollDET.setAMOUNT(Double.valueOf(payadditionvalue[i]));
								hrPayrollDET.setAMOUNT_TYPE("ADDITION");
								hrPayrollDET.setCRBY(username);
								HrPayrollDETService.addpayrolldet(hrPayrollDET);
								
								tamount = tamount + Double.valueOf(payadditionvalue[i]);
								
								JournalDetail journalDetail=new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson2=coaDAO.getCOAByName(plant, payadditionname[i]);
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail.setACCOUNT_NAME(payadditionname[i]);
								journalDetail.setDEBITS(Double.valueOf(payadditionvalue[i]));
								journalDetails.add(journalDetail);
								
								Hashtable htMovHis = new Hashtable();
								htMovHis.clear();
								htMovHis.put(IDBConstants.PLANT, plant);					
								htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYROLL_ADDITION);	
								htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));													
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
								htMovHis.put(IDBConstants.CREATED_BY, username);		
								htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
								htMovHis.put("REMARKS",payadditionname[i]+","+payadditionvalue[i]);
								movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
							}
						}
					}
					
					if(paydeducttionvalue != null) {
						for (int i = 0; i < paydeducttionvalue.length; i++) {
							
							HrPayrollDET hrPayrollDET = new HrPayrollDET();
							hrPayrollDET.setPLANT(plant);
							hrPayrollDET.setHDRID(hdrid);
							hrPayrollDET.setLNNO(i);
							hrPayrollDET.setSALARYTYPE(paydeductionname[i]);
							hrPayrollDET.setAMOUNT(Double.valueOf(paydeducttionvalue[i]));
							hrPayrollDET.setAMOUNT_TYPE("DEDUCTION");
							hrPayrollDET.setCRBY(username);
							HrPayrollDETService.addpayrolldet(hrPayrollDET);
							
							HrDeductionDet hrDeductionDet = hrDeductionDetDAO.getdeductiondetById(plant, Integer.valueOf(paydeductid[i]));
							hrDeductionDet.setSTATUS("Deducted");
							hrDeductionDetDAO.updatedeductiondet(hrDeductionDet, username);
							
							damount = damount+Double.valueOf(paydeducttionvalue[i]);
							
							String empcode = employeeDAO.getEmpcode(plant, empid, "");
							String empname = employeeDAO.getEmpnamebyid(plant, empid, "");
							String accname = paydeductionname[i]+"-"+empcode+"-"+empname;
							
							JournalDetail journalDetail=new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson2=coaDAO.getCOAByNameandsubid(plant, accname,"18");
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
							journalDetail.setACCOUNT_NAME(accname);
							journalDetail.setCREDITS(Double.valueOf(paydeducttionvalue[i]));
							journalDetailDetection.add(journalDetail);
							
							Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, plant);					
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYROLL_DEDUCTION);	
							htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));													
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
							htMovHis.put(IDBConstants.CREATED_BY, username);		
							htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
							htMovHis.put("REMARKS",paydeductionname[i]+","+paydeducttionvalue[i]);
							movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
							
						}
					}
					
					
					String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					String empcode = employeeDAO.getEmpcode(plant, empid, "");
					String empname = employeeDAO.getEmpnamebyid(plant, empid, "");
					String payclearing = empcode+"-"+empname;
					
					JournalHeader journalHead=new JournalHeader();
					journalHead.setPLANT(plant);
					journalHead.setJOURNAL_DATE(paydate);
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(curency);
					journalHead.setTRANSACTION_TYPE("PAYROLL");
					journalHead.setTRANSACTION_ID(payroll);
					journalHead.setSUB_TOTAL(tamount);
					journalHead.setTOTAL_AMOUNT(tamount);
					journalHead.setCRAT(dateutils.getDateTime());
					journalHead.setCRBY(username);
					
					JournalDetail journalDetail_1=new JournalDetail();
					journalDetail_1.setPLANT(plant);
					JSONObject coaJson1=coaDAO.getCOAByName(plant, payclearing);
					journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
					journalDetail_1.setACCOUNT_NAME(payclearing);
					journalDetail_1.setCREDITS(tamount);
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
							Hashtable htMovHis = new Hashtable();
							htMovHis.put(IDBConstants.PLANT, plant);
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
							htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
							htMovHis.put(IDBConstants.ITEM, "");
							htMovHis.put(IDBConstants.QTY, "0.0");
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
							htMovHis.put(IDBConstants.CREATED_BY, username);		
							htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMovHis.put("REMARKS","");
							movHisDao.insertIntoMovHis(htMovHis);
						}
					}
					
					if(damount > 0) {
						JournalHeader journalHeadDetails=new JournalHeader();
						journalHeadDetails.setPLANT(plant);
						journalHeadDetails.setJOURNAL_DATE(paydate);
						journalHeadDetails.setJOURNAL_STATUS("PUBLISHED");
						journalHeadDetails.setJOURNAL_TYPE("Cash");
						journalHeadDetails.setCURRENCYID(curency);
						journalHeadDetails.setTRANSACTION_TYPE("PAYROLLDEDUCTION");
						journalHeadDetails.setTRANSACTION_ID(payroll);
						journalHeadDetails.setSUB_TOTAL(damount);
						journalHeadDetails.setTOTAL_AMOUNT(Double.valueOf(damount));
						journalHeadDetails.setCRAT(dateutils.getDateTime());
						journalHeadDetails.setCRBY(username);
						
						JournalDetail journalDetail_2=new JournalDetail();
						journalDetail_2.setPLANT(plant);
						JSONObject coaJson3=coaDAO.getCOAByName(plant, payclearing);
						journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
						journalDetail_2.setACCOUNT_NAME(payclearing);
						journalDetail_2.setDEBITS(damount);
						journalDetailDetection.add(journalDetail_2);
						
						Journal journalDet=new Journal();
						journalDet.setJournalHeader(journalHeadDetails);
						journalDet.setJournalDetails(journalDetailDetection);
						Journal journalFromdet=journalService.getJournalByTransactionId(plant, journalHeadDetails.getTRANSACTION_ID(),journalHeadDetails.getTRANSACTION_TYPE());
						if(journalFromdet.getJournalHeader()!=null){
							if(journalFromdet.getJournalHeader().getID()>0){
								journalHeadDetails.setID(journalFromdet.getJournalHeader().getID());
								journalService.updateJournal(journalDet, username);
								Hashtable jhtMovHis = new Hashtable();
								jhtMovHis.put(IDBConstants.PLANT, plant);
								jhtMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
								jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
								jhtMovHis.put(IDBConstants.ITEM, "");
								jhtMovHis.put(IDBConstants.QTY, "0.0");
								jhtMovHis.put("RECID", "");
								jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journalDet.getJournalHeader().getTRANSACTION_TYPE()+" "+journalDet.getJournalHeader().getTRANSACTION_ID());
								jhtMovHis.put(IDBConstants.CREATED_BY, username);		
								jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								jhtMovHis.put("REMARKS","");
								movHisDao.insertIntoMovHis(jhtMovHis);
							}else{
								journalService.addJournal(journalDet, username);
								Hashtable htMovHis = new Hashtable();
								htMovHis.put(IDBConstants.PLANT, plant);
								htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
								htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
								htMovHis.put(IDBConstants.ITEM, "");
								htMovHis.put(IDBConstants.QTY, "0.0");
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journalDet.getJournalHeader().getTRANSACTION_TYPE()+" "+journalDet.getJournalHeader().getTRANSACTION_ID());
								htMovHis.put(IDBConstants.CREATED_BY, username);		
								htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMovHis.put("REMARKS","");
								movHisDao.insertIntoMovHis(htMovHis);
							}
						}
					}
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYROLL);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));													
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",payroll+","+empid+","+month+","+year+","+paydays+","+netamount);
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
					new TblControlUtil().updateTblControlIESeqNo(plant, "PAYROLL", "PR", payroll);
					
					DbBean.CommitTran(ut);
					String result = "Payroll generated successfully.";
					response.sendRedirect("../payroll/summary?result="+ result);
					
				}else {
					DbBean.RollbackTran(ut);
					String result = "Couldn't generated Payroll";
					response.sendRedirect("../payroll/new?result="+ result);
				}
				
			} catch (Exception e) {
				System.out.println(e);
				System.out.println(e.getMessage());
				DbBean.RollbackTran(ut);
				String result = "Couldn't generated Payroll";
				response.sendRedirect("../payroll/new?result="+ result);
				
			}
		}
		
		
		if(action.equalsIgnoreCase("Update"))
		{
			String id = StrUtils.fString(request.getParameter("pay_id"));
			String empid = StrUtils.fString(request.getParameter("pay_empid"));
			String netamount = StrUtils.fString(request.getParameter("pay_netamount"));
			String paydate= StrUtils.fString(request.getParameter("pay_date"));
			
			/*
			 * String paymode= StrUtils.fString(request.getParameter("payment_mode"));
			 * String paidthrough=
			 * StrUtils.fString(request.getParameter("paid_through_account_name")); String
			 * bankname= StrUtils.fString(request.getParameter("bankname")); String
			 * chequeno= StrUtils.fString(request.getParameter("chequeno")); String
			 * chequedate= StrUtils.fString(request.getParameter("chequedate")); String
			 * chequeamount= StrUtils.fString(request.getParameter("chequeamount"));
			 */
			
			String reference= StrUtils.fString(request.getParameter("refrence"));
			String notes= StrUtils.fString(request.getParameter("note"));
			String ischeque= StrUtils.fString(request.getParameter("pay_ischeque"));
			
			String[] payadditionname = request.getParameterValues("pay_addname");
			String[] payadditionvalue = request.getParameterValues("pay_addamount");
			CoaDAO coaDAO = new CoaDAO();
			EmployeeDAO employeeDAO = new EmployeeDAO();

			
	        UserTransaction ut = null;
			try {
				
				ut = DbBean.getUserTranaction();				
				ut.begin();
				
				double tamount =0;
				double damount =0;
				List<JournalDetail> journalDetails=new ArrayList<>();
				List<JournalDetail> journalDetailDetection=new ArrayList<>();
				
				
				HrPayrollHDR hrPayrollHDR = hrPayrollHDRService.getpayrollhdrById(plant, Integer.valueOf(id));
				
				hrPayrollHDR.setPLANT(plant);
				hrPayrollHDR.setTOTAL_AMOUNT(Double.valueOf(netamount));
				hrPayrollHDR.setPAYMENT_DATE(paydate);
				
				/*
				 * hrPayrollHDR.setPAYMENT_MODE(paymode);
				 * hrPayrollHDR.setPAID_THROUGH(paidthrough); if(ischeque.equalsIgnoreCase("1"))
				 * { hrPayrollHDR.setBANK_BRANCH(bankname);
				 * hrPayrollHDR.setCHECQUE_NO(chequeno);
				 * hrPayrollHDR.setCHEQUE_DATE(chequedate);
				 * hrPayrollHDR.setCHEQUE_AMOUNT(Double.valueOf(chequeamount)); }else {
				 * hrPayrollHDR.setBANK_BRANCH(""); hrPayrollHDR.setCHECQUE_NO("");
				 * hrPayrollHDR.setCHEQUE_DATE("");
				 * hrPayrollHDR.setCHEQUE_AMOUNT(Double.valueOf("0")); }
				 */
				
				hrPayrollHDR.setREFERENCE(reference);
				hrPayrollHDR.setNOTE(notes);
				hrPayrollHDR.setUPBY(username);
				
				hrPayrollHDRService.updatepayrollhdr(hrPayrollHDR, username);
				
				/* if(hdrid > 0) { */
					List<HrPayrollDET> additionlist = hrPayrollDETDAO.getsalaryaddition(plant, Integer.valueOf(id), hrPayrollHDR.getEMPID());
					for(HrPayrollDET addallowance:additionlist){
						HrPayrollDETService.Deletepayrolldet(plant, addallowance.getID());
					}
					if(payadditionvalue != null) {
						for (int i = 0; i < payadditionvalue.length; i++) {
							if(!payadditionname[i].equalsIgnoreCase("")) {
								HrPayrollDET hrPayrollDET = new HrPayrollDET();
								hrPayrollDET.setPLANT(plant);
								hrPayrollDET.setHDRID(Integer.valueOf(id));
								hrPayrollDET.setLNNO(i);
								hrPayrollDET.setSALARYTYPE(payadditionname[i]);
								hrPayrollDET.setAMOUNT(Double.valueOf(payadditionvalue[i]));
								hrPayrollDET.setAMOUNT_TYPE("ADDITION");
								hrPayrollDET.setCRBY(username);
								HrPayrollDETService.addpayrolldet(hrPayrollDET);
								
								tamount = tamount + Double.valueOf(payadditionvalue[i]);
								
								JournalDetail journalDetail=new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson2=coaDAO.getCOAByName(plant, payadditionname[i]);
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail.setACCOUNT_NAME(payadditionname[i]);
								journalDetail.setDEBITS(Double.valueOf(payadditionvalue[i]));
								journalDetails.add(journalDetail);
								
								Hashtable htMovHis = new Hashtable();
								htMovHis.clear();
								htMovHis.put(IDBConstants.PLANT, plant);					
								htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYROLL_ADDITION);	
								htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));													
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM, id);
								htMovHis.put(IDBConstants.CREATED_BY, username);		
								htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
								htMovHis.put("REMARKS",payadditionname[i]+","+payadditionvalue[i]);
								movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
							}
						}
					}
					
					List<HrPayrollDET> allowancelist = hrPayrollDETDAO.getsalaryallowance(plant, Integer.valueOf(id), Integer.valueOf(empid));
					for(HrPayrollDET allowance:allowancelist){
						tamount = tamount + allowance.getAMOUNT();
						
						JournalDetail journalDetail=new JournalDetail();
						journalDetail.setPLANT(plant);
						JSONObject coaJson2=coaDAO.getCOAByName(plant, allowance.getSALARYTYPE());
						journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
						journalDetail.setACCOUNT_NAME(allowance.getSALARYTYPE());
						journalDetail.setDEBITS(allowance.getAMOUNT());
						journalDetails.add(journalDetail);
					}
					
					
					List<HrPayrollDET> deductionlist = hrPayrollDETDAO.getpayrolldetByhdrid(plant, Integer.valueOf(id));
					for(HrPayrollDET deduction:deductionlist){
						if(deduction.getAMOUNT_TYPE().equalsIgnoreCase("DEDUCTION")) {
							damount = damount+deduction.getAMOUNT();
							
							String empcode = employeeDAO.getEmpcode(plant, empid, "");
							String empname = employeeDAO.getEmpnamebyid(plant, empid, "");
							String accname = deduction.getSALARYTYPE()+"-"+empcode+"-"+empname;
							
							JournalDetail journalDetail=new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson2=coaDAO.getCOAByNameandsubid(plant, accname,"18");
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
							journalDetail.setACCOUNT_NAME(accname);
							journalDetail.setCREDITS(deduction.getAMOUNT());
							journalDetailDetection.add(journalDetail);
						}
					}
					
					
					String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					String empcode = employeeDAO.getEmpcode(plant, empid, "");
					String empname = employeeDAO.getEmpnamebyid(plant, empid, "");
					String payclearing = empcode+"-"+empname;
					
					JournalHeader journalHead=new JournalHeader();
					journalHead.setPLANT(plant);
					journalHead.setJOURNAL_DATE(paydate);
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(curency);
					journalHead.setTRANSACTION_TYPE("PAYROLL");
					journalHead.setTRANSACTION_ID(hrPayrollHDR.getPAYROLL());
					journalHead.setSUB_TOTAL(tamount);
					journalHead.setTOTAL_AMOUNT(tamount);
					journalHead.setCRAT(dateutils.getDateTime());
					journalHead.setCRBY(username);
					
					JournalDetail journalDetail_1=new JournalDetail();
					journalDetail_1.setPLANT(plant);
					JSONObject coaJson1=coaDAO.getCOAByName(plant, payclearing);
					journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
					journalDetail_1.setACCOUNT_NAME(payclearing);
					journalDetail_1.setCREDITS(tamount);
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
							Hashtable htMovHis = new Hashtable();
							htMovHis.put(IDBConstants.PLANT, plant);
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
							htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
							htMovHis.put(IDBConstants.ITEM, "");
							htMovHis.put(IDBConstants.QTY, "0.0");
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
							htMovHis.put(IDBConstants.CREATED_BY, username);		
							htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMovHis.put("REMARKS","");
							movHisDao.insertIntoMovHis(htMovHis);
						}
					}
					
					JournalHeader journalHeadDetails=new JournalHeader();
					journalHeadDetails.setPLANT(plant);
					journalHeadDetails.setJOURNAL_DATE(paydate);
					journalHeadDetails.setJOURNAL_STATUS("PUBLISHED");
					journalHeadDetails.setJOURNAL_TYPE("Cash");
					journalHeadDetails.setCURRENCYID(curency);
					journalHeadDetails.setTRANSACTION_TYPE("PAYROLLDEDUCTION");
					journalHeadDetails.setTRANSACTION_ID(hrPayrollHDR.getPAYROLL());
					journalHeadDetails.setSUB_TOTAL(damount);
					journalHeadDetails.setTOTAL_AMOUNT(Double.valueOf(damount));
					journalHeadDetails.setCRAT(dateutils.getDateTime());
					journalHeadDetails.setCRBY(username);
					
					JournalDetail journalDetail_2=new JournalDetail();
					journalDetail_2.setPLANT(plant);
					JSONObject coaJson3=coaDAO.getCOAByName(plant, payclearing);
					journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
					journalDetail_2.setACCOUNT_NAME(payclearing);
					journalDetail_2.setDEBITS(damount);
					journalDetailDetection.add(journalDetail_2);
					
					Journal journalDet=new Journal();
					journalDet.setJournalHeader(journalHeadDetails);
					journalDet.setJournalDetails(journalDetailDetection);
					Journal journalFromdet=journalService.getJournalByTransactionId(plant, journalHeadDetails.getTRANSACTION_ID(),journalHeadDetails.getTRANSACTION_TYPE());
					if(journalFromdet.getJournalHeader()!=null){
						if(journalFromdet.getJournalHeader().getID()>0){
							journalHeadDetails.setID(journalFromdet.getJournalHeader().getID());
							journalService.updateJournal(journalDet, username);
							Hashtable jhtMovHis = new Hashtable();
							jhtMovHis.put(IDBConstants.PLANT, plant);
							jhtMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
							jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
							jhtMovHis.put(IDBConstants.ITEM, "");
							jhtMovHis.put(IDBConstants.QTY, "0.0");
							jhtMovHis.put("RECID", "");
							jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journalDet.getJournalHeader().getTRANSACTION_TYPE()+" "+journalDet.getJournalHeader().getTRANSACTION_ID());
							jhtMovHis.put(IDBConstants.CREATED_BY, username);		
							jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							jhtMovHis.put("REMARKS","");
							movHisDao.insertIntoMovHis(jhtMovHis);
						}else{
							journalService.addJournal(journalDet, username);
							Hashtable htMovHis = new Hashtable();
							htMovHis.put(IDBConstants.PLANT, plant);
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
							htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
							htMovHis.put(IDBConstants.ITEM, "");
							htMovHis.put(IDBConstants.QTY, "0.0");
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journalDet.getJournalHeader().getTRANSACTION_TYPE()+" "+journalDet.getJournalHeader().getTRANSACTION_ID());
							htMovHis.put(IDBConstants.CREATED_BY, username);		
							htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMovHis.put("REMARKS","");
							movHisDao.insertIntoMovHis(htMovHis);
						}
					}
					
					
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.UPDATE_PAYROLL);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));													
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, id);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",hrPayrollHDR.getPAYROLL()+","+hrPayrollHDR.getEMPID()+","+hrPayrollHDR.getMONTH()+","+hrPayrollHDR.getYEAR()+","+hrPayrollHDR.getPAYDAYS()+","+netamount);
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
					
					DbBean.CommitTran(ut);
					String result = "Payroll Update successfully.";
					response.sendRedirect("../payroll/summary?result="+ result);
					
				/*}else {
					DbBean.RollbackTran(ut);
					String result = "Couldn't Update Payroll";
					response.sendRedirect("jsp/EditPayroll.jsp?id="+id+"&result="+ result);
				}*/
				
			} catch (Exception e) {
				System.out.println(e);
				System.out.println(e.getMessage());
				DbBean.RollbackTran(ut);
				String result = "Couldn't Update Payroll";
				response.sendRedirect("../payroll/edit?id="+id+"&result="+ result);
				
			}
		}
		
		
		
		if(action.equalsIgnoreCase("GENERATE_PAYROLL"))
		{

			String FROMDATE= StrUtils.fString(request.getParameter("pay_fromdate"));
			String TODATE= StrUtils.fString(request.getParameter("pay_todate"));
			String MONTH= StrUtils.fString(request.getParameter("pay_month"));
			String YEAR= StrUtils.fString(request.getParameter("pay_year"));
			EmployeeDAO employeeDAO = new EmployeeDAO();
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();

	        UserTransaction ut = null;
			try {
				
				ut = DbBean.getUserTranaction();				
				ut.begin();
				ArrayList employeelist = employeeDAO.getAllEmployeeDetails(plant);
				 if (employeelist.size() > 0) {
		               for(int i =0; i<employeelist.size(); i++) {
		            	   	Map employee = (Map)employeelist.get(i);
		                   	String EMPID= (String)employee.get("ID");
		                   
		               		boolean payrollcheck = hrPayrollHDRService.Ispayrollhdr(plant, Integer.valueOf(EMPID), MONTH, YEAR);
						
		               		if(payrollcheck) {
		               			continue;
		               		}else {
						
								String empworkmandays = plantMstDAO.getworkmandsys(plant);
								String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
								
								float totaldays = 0;
								float actualtotaldays = 0;
								double totaldaystopay = 0;
								double netamount = 0;
								
								if(empworkmandays.equalsIgnoreCase("30DAYS")) {
									totaldays = 30;
									actualtotaldays = gettotalnoofdays(FROMDATE, TODATE);
								}else {
									totaldays = gettotalnoofdays(FROMDATE, TODATE);
									actualtotaldays = totaldays;
								}
							
								double lopdays = getlosofpaydays(plant, FROMDATE, TODATE, Integer.valueOf(EMPID));
								
								List<HrEmpSalaryDET> HrEmpSalaryDETList = hrEmpSalaryDetDAO.EmpSalarydetlistbyempid(plant, Integer.valueOf(EMPID));
								
								if(lopdays >= totaldays || lopdays >= actualtotaldays) {
									double zeroval = Double.valueOf(StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal));
									for(HrEmpSalaryDET hrEmpSalaryDET:HrEmpSalaryDETList) {
										hrEmpSalaryDET.setSALARY(zeroval);
									}
									
								}else {
									totaldaystopay = totaldays - lopdays;
									for(HrEmpSalaryDET hrEmpSalaryDET:HrEmpSalaryDETList) {
										double salaryamount = (hrEmpSalaryDET.getSALARY()/totaldays)*totaldaystopay;
										netamount = netamount+salaryamount;
										hrEmpSalaryDET.setSALARY(Double.valueOf(StrUtils.addZeroes(salaryamount, numberOfDecimal)));
									}
								}
								
								List<HrDeductionHdr> payrolladdition = hrDeductionHdrDAO.getdeductionhdrempmonthyear(plant, EMPID, MONTH, YEAR);
								
								List<HrDeductionDet> payrolldeduction = hrDeductionDetDAO.getdeductiondetbyempidmonthyear(plant, EMPID, MONTH, YEAR);
								
								List<HrpayrollDeductionPojo> hrpayrollDeductionPojolist = new ArrayList<HrpayrollDeductionPojo>();
								
								for(HrDeductionDet hrDeductionDet:payrolldeduction) {
									netamount = netamount-hrDeductionDet.getDUE_AMOUNT();
									
									HrpayrollDeductionPojo hrpayrollDeductionPojo = new HrpayrollDeductionPojo();
									
									hrpayrollDeductionPojo.setID(hrDeductionDet.getID());
									hrpayrollDeductionPojo.setHDRID(hrDeductionDet.getHDRID());
									hrpayrollDeductionPojo.setDUE_AMOUNT(hrDeductionDet.getDUE_AMOUNT());
									HrDeductionHdr hrDeductionHdr = hrDeductionHdrDAO.getdeductionhdrById(plant, hrDeductionDet.getHDRID());	
									hrpayrollDeductionPojo.setDEDUCTION_NAME(hrDeductionHdr.getDEDUCTION_NAME());
									hrpayrollDeductionPojolist.add(hrpayrollDeductionPojo);
									
								}
								
			                 
			                String payrollno = _TblControlDAO.getNextOrder(plant,username,"PAYROLL");
			                   
			                HrPayrollHDR hrPayrollHDR = new HrPayrollHDR();
			   				
			   				hrPayrollHDR.setPLANT(plant);
			   				hrPayrollHDR.setEMPID(Integer.valueOf(EMPID));
			   				hrPayrollHDR.setPAYROLL(payrollno);
			   				hrPayrollHDR.setFROMDATE(FROMDATE);
			   				hrPayrollHDR.setTODATE(TODATE);
			   				hrPayrollHDR.setMONTH(MONTH);
			   				hrPayrollHDR.setYEAR(YEAR);
			   				hrPayrollHDR.setPAYDAYS(Double.valueOf(totaldaystopay));
			   				hrPayrollHDR.setTOTAL_AMOUNT(Double.valueOf(netamount));
			   				hrPayrollHDR.setPAYMENT_DATE(dateutils.getDate());
			   				/*hrPayrollHDR.setPAYMENT_MODE(paymode);
			   				hrPayrollHDR.setPAID_THROUGH(paidthrough);
			   				if(ischeque.equalsIgnoreCase("1")) {
			   				hrPayrollHDR.setBANK_BRANCH(bankname);
			   				hrPayrollHDR.setCHECQUE_NO(chequeno);
			   				hrPayrollHDR.setCHEQUE_DATE(chequedate);
			   				hrPayrollHDR.setCHEQUE_AMOUNT(Double.valueOf(chequeamount));
			   				}else {
			   					hrPayrollHDR.setBANK_BRANCH("");
			   					hrPayrollHDR.setCHECQUE_NO("");
			   					hrPayrollHDR.setCHEQUE_DATE("");
			   					hrPayrollHDR.setCHEQUE_AMOUNT(Double.valueOf("0"));
			   				}
			   				hrPayrollHDR.setREFERENCE(reference);
			   				hrPayrollHDR.setNOTE(notes);*/
			   				hrPayrollHDR.setCRBY(username);
			   				
			   				int hdrid = hrPayrollHDRService.addpayrollhdr(hrPayrollHDR);
			   				
			   				if(hdrid > 0) {
			   					int k=0;
			   					for(HrEmpSalaryDET hrEmpSalaryDET:HrEmpSalaryDETList) {
			   						
			   						
			   						HrPayrollDET hrPayrollDET = new HrPayrollDET();
			   						hrPayrollDET.setPLANT(plant);
			   						hrPayrollDET.setHDRID(hdrid);
			   						hrPayrollDET.setLNNO(k);
			   						hrPayrollDET.setSALARYTYPE(hrEmpSalaryDET.getSALARYTYPE());
			   						hrPayrollDET.setAMOUNT(hrEmpSalaryDET.getSALARY());
			   						hrPayrollDET.setAMOUNT_TYPE("ADDITION");
			   						hrPayrollDET.setCRBY(username);
			   						HrPayrollDETService.addpayrolldet(hrPayrollDET);
			   						k++;	
			   					}
			   					
			   					/*if(payadditionvalue != null) {
			   						for (int i = 0; i < payrolladdition.length; i++) {
			   							if(!payadditionname[i].equalsIgnoreCase("")) {
			   								HrPayrollDET hrPayrollDET = new HrPayrollDET();
			   								hrPayrollDET.setPLANT(plant);
			   								hrPayrollDET.setHDRID(hdrid);
			   								hrPayrollDET.setLNNO(i);
			   								hrPayrollDET.setSALARYTYPE(payadditionname[i]);
			   								hrPayrollDET.setAMOUNT(Double.valueOf(payadditionvalue[i]));
			   								hrPayrollDET.setAMOUNT_TYPE("ADDITION");
			   								hrPayrollDET.setCRBY(username);
			   								HrPayrollDETService.addpayrolldet(hrPayrollDET);
			   								
			   								Hashtable htMovHis = new Hashtable();
			   								htMovHis.clear();
			   								htMovHis.put(IDBConstants.PLANT, plant);					
			   								htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYROLL_ADDITION);	
			   								htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));													
			   								htMovHis.put("RECID", "");
			   								htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
			   								htMovHis.put(IDBConstants.CREATED_BY, username);		
			   								htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			   								htMovHis.put("REMARKS",payadditionname[i]+","+payadditionvalue[i]);
			   								movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
			   							}
			   						}
			   					}*/
			   					
			   						for (HrpayrollDeductionPojo deduction:hrpayrollDeductionPojolist) {
			   							
			   							HrPayrollDET hrPayrollDET = new HrPayrollDET();
			   							hrPayrollDET.setPLANT(plant);
			   							hrPayrollDET.setHDRID(hdrid);
			   							hrPayrollDET.setLNNO(i);
			   							hrPayrollDET.setSALARYTYPE(deduction.getDEDUCTION_NAME());
			   							hrPayrollDET.setAMOUNT(deduction.getDUE_AMOUNT());
			   							hrPayrollDET.setAMOUNT_TYPE("DEDUCTION");
			   							hrPayrollDET.setCRBY(username);
			   							HrPayrollDETService.addpayrolldet(hrPayrollDET);
			   							
			   							HrDeductionDet hrDeductionDet = hrDeductionDetDAO.getdeductiondetById(plant, Integer.valueOf(deduction.getID()));
			   							hrDeductionDet.setSTATUS("Deducted");
			   							hrDeductionDetDAO.updatedeductiondet(hrDeductionDet, username);
			   							
			   							Hashtable htMovHis = new Hashtable();
			   							htMovHis.clear();
			   							htMovHis.put(IDBConstants.PLANT, plant);					
			   							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYROLL_DEDUCTION);	
			   							htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));													
			   							htMovHis.put("RECID", "");
			   							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
			   							htMovHis.put(IDBConstants.CREATED_BY, username);		
			   							htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			   							htMovHis.put("REMARKS",deduction.getDEDUCTION_NAME()+","+deduction.getDUE_AMOUNT());
			   							movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
			   							
			   						}
			   		
			   					
			   					Hashtable htMovHis = new Hashtable();
			   					htMovHis.clear();
			   					htMovHis.put(IDBConstants.PLANT, plant);					
			   					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYROLL);	
			   					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));													
			   					htMovHis.put("RECID", "");
			   					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
			   					htMovHis.put(IDBConstants.CREATED_BY, username);		
			   					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			   					htMovHis.put("REMARKS",payrollno+","+EMPID+","+MONTH+","+YEAR+","+totaldaystopay+","+netamount);
			   					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
			               }
			   				
		               		}
					 }
				 }
				
					
				DbBean.CommitTran(ut);
				String result = "Payroll generated successfully.";
				response.sendRedirect("jsp/.jsp?result="+ result);
			} catch (Exception e) {
				System.out.println(e);
				System.out.println(e.getMessage());
				DbBean.RollbackTran(ut);
				String result = "Couldn't generated Payroll";
				response.sendRedirect("jsp/.jsp?result="+ result);
				
			}
		}

		
		if (action.equals("PAYROLL")) {
	        jsonObjectResult = this.generateparrollno(plant, username);
	        response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	    }
		

		if(action.equalsIgnoreCase("CALCULATE_EMPLOYEE_SALARY"))
		{
			String EMPID= StrUtils.fString(request.getParameter("EMPID"));
			String FROMDATE= StrUtils.fString(request.getParameter("FROMDATE"));
			String TODATE= StrUtils.fString(request.getParameter("TODATE"));
			String MONTH= StrUtils.fString(request.getParameter("MONTH"));
			String YEAR= StrUtils.fString(request.getParameter("YEAR"));
			
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        UserTransaction ut = null;
			try {
				
				ut = DbBean.getUserTranaction();				
				ut.begin();
				
				boolean payrollcheck = hrPayrollHDRService.Ispayrollhdr(plant, Integer.valueOf(EMPID), MONTH, YEAR);
				
				if(payrollcheck) {
					resultJson.put("STATUS", "REPEAT");
					DbBean.CommitTran(ut);
				}else {
				
					String empworkmandays = plantMstDAO.getworkmandsys(plant);
					String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
					
					float totaldays = 0;
					float actualtotaldays = 0;
					double totaldaystopay = 0;
					double netamount = 0;
					
					if(empworkmandays.equalsIgnoreCase("30DAYS")) {
						totaldays = 30;
						actualtotaldays = gettotalnoofdays(FROMDATE, TODATE);
					}else {
						totaldays = gettotalnoofdays(FROMDATE, TODATE);
						actualtotaldays = totaldays;
					}
				
					double lopdays = getlosofpaydays(plant, FROMDATE, TODATE, Integer.valueOf(EMPID));
					
					List<HrEmpSalaryDET> HrEmpSalaryDETList = hrEmpSalaryDetDAO.EmpSalarydetlistbyempid(plant, Integer.valueOf(EMPID));
					
					if(lopdays >= totaldays || lopdays >= actualtotaldays) {
						double zeroval = Double.valueOf(StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal));
						for(HrEmpSalaryDET hrEmpSalaryDET:HrEmpSalaryDETList) {
							hrEmpSalaryDET.setSALARY(zeroval);
						}
						
					}else {
						totaldaystopay = totaldays - lopdays;
						for(HrEmpSalaryDET hrEmpSalaryDET:HrEmpSalaryDETList) {
							double salaryamount = (hrEmpSalaryDET.getSALARY()/totaldays)*totaldaystopay;
							netamount = netamount+salaryamount;
							hrEmpSalaryDET.setSALARY(Double.valueOf(StrUtils.addZeroes(salaryamount, numberOfDecimal)));
						}
					}
					
					List<HrDeductionHdr> payrolladdition = hrDeductionHdrDAO.getdeductionhdrempmonthyear(plant, EMPID, MONTH, YEAR);
					
					List<HrDeductionDet> payrolldeduction = hrDeductionDetDAO.getdeductiondetbyempidmonthyear(plant, EMPID, MONTH, YEAR);
					
					List<HrpayrollDeductionPojo> hrpayrollDeductionPojolist = new ArrayList<HrpayrollDeductionPojo>();
					
					for(HrDeductionDet hrDeductionDet:payrolldeduction) {
						netamount = netamount-hrDeductionDet.getDUE_AMOUNT();
						
						HrpayrollDeductionPojo hrpayrollDeductionPojo = new HrpayrollDeductionPojo();
						
						hrpayrollDeductionPojo.setID(hrDeductionDet.getID());
						hrpayrollDeductionPojo.setHDRID(hrDeductionDet.getHDRID());
						hrpayrollDeductionPojo.setDUE_AMOUNT(hrDeductionDet.getDUE_AMOUNT());
						HrDeductionHdr hrDeductionHdr = hrDeductionHdrDAO.getdeductionhdrById(plant, hrDeductionDet.getHDRID());	
						hrpayrollDeductionPojo.setDEDUCTION_NAME(hrDeductionHdr.getDEDUCTION_NAME());
						hrpayrollDeductionPojolist.add(hrpayrollDeductionPojo);
						
					}
					
				
					
					resultJson.put("STATUS", "OK");
					resultJson.put("SALARYALLOWANCE", HrEmpSalaryDETList);
					resultJson.put("ATTDAYS", totaldaystopay);
					resultJson.put("PAYADD", payrolladdition); 
					resultJson.put("PAYSUB", hrpayrollDeductionPojolist);
					resultJson.put("NETAMOUNT", netamount); 
					DbBean.CommitTran(ut);
				}
				
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				resultJson.put("STATUS", "NOT OK");
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			response.getWriter().write(resultJson.toString());
		}
		
		if(action.equalsIgnoreCase("SaveDetection"))
		{
			
			CoaDAO coaDAO = new CoaDAO();
			CoaUtil coaUtil = new CoaUtil();
			EmployeeDAO employeeDAO = new EmployeeDAO();
	        UserTransaction ut = null;
			try {
				
				ut = DbBean.getUserTranaction();				
				ut.begin();
				
				List<JournalDetail> journalDetails=new ArrayList<>();
				
				String Deduction_month = StrUtils.fString(request.getParameter("Deduction_month"));
				String Deduction_year = StrUtils.fString(request.getParameter("Deduction_year"));
				String Deduction_name = StrUtils.fString(request.getParameter("Deduction_name"));
				String Deduction_empid = StrUtils.fString(request.getParameter("Deduction_empid"));
				String Deduction_emptype = StrUtils.fString(request.getParameter("Deduction_emptype"));
				String Deduction_date = StrUtils.fString(request.getParameter("Deduction_date"));
				String Deduction_amount = StrUtils.fString(request.getParameter("Deduction_amount"));
				String Deduction_dueamount = StrUtils.fString(request.getParameter("Deduction_dueamount"));
				String Deduction_isgratuity = StrUtils.fString(request.getParameter("Deduction_isgratuity"));
				
				
				HrDeductionHdr hrDeductionHdr = new HrDeductionHdr();
				
				hrDeductionHdr.setPLANT(plant);
				hrDeductionHdr.setEMPID(Integer.valueOf(Deduction_empid));
				hrDeductionHdr.setDEDUCTION_NAME(Deduction_name);
				hrDeductionHdr.setDEDUCTION_AMOUNT(Double.valueOf(Deduction_amount));
				hrDeductionHdr.setDEDUCTION_DUE(Double.valueOf(Deduction_dueamount));
				hrDeductionHdr.setDEDUCTION_DATE(Deduction_date);
				hrDeductionHdr.setMONTH(Deduction_month);
				hrDeductionHdr.setYEAR(Deduction_year);
				hrDeductionHdr.setISGRATUITY(Short.valueOf(Deduction_isgratuity));
				hrDeductionHdr.setCRBY(username);
				
				int hid = hrDeductionHdrDAO.adddeductionhdr(hrDeductionHdr);
				
				if(Short.valueOf(Deduction_isgratuity) == 0) {
					
					double amount = Double.valueOf(Deduction_amount); 
					double dueamount = Double.valueOf(Deduction_dueamount); 
					double amounttimes = amount / dueamount;
					int amttimes  = (int)amounttimes;
					double amountbal = amount % dueamount;
					
					int dmonth = Integer.valueOf(Deduction_month);
					int dyear = Integer.valueOf(Deduction_year);
					
					for (int i = 0; i < amttimes; i++) {
						HrDeductionDet hrDeductionDet = new HrDeductionDet();
						hrDeductionDet.setPLANT(plant);
						hrDeductionDet.setEMPID(Integer.valueOf(Deduction_empid));
						hrDeductionDet.setHDRID(hid);
						hrDeductionDet.setDUE_AMOUNT(Double.valueOf(Deduction_dueamount));
						hrDeductionDet.setLNNO((i));
						hrDeductionDet.setCRBY(username);
						hrDeductionDet.setSTATUS("Pending");
						dmonth = dmonth + 1;
						if((dmonth > 12)) {
							dmonth = 1;
							dyear = dyear+1;
							hrDeductionDet.setDUE_MONTH(String.valueOf(dmonth));
							hrDeductionDet.setDUE_YEAR(String.valueOf(dyear));
						}else {
							hrDeductionDet.setDUE_MONTH(String.valueOf(dmonth));
							hrDeductionDet.setDUE_YEAR(String.valueOf(dyear));
						}
						
						hrDeductionDetDAO.adddeductiondet(hrDeductionDet);
					}
					
					if(amountbal > 0) {
						HrDeductionDet hrDeductionDet = new HrDeductionDet();
						hrDeductionDet.setPLANT(plant);
						hrDeductionDet.setEMPID(Integer.valueOf(Deduction_empid));
						hrDeductionDet.setHDRID(hid);
						hrDeductionDet.setDUE_AMOUNT(amountbal);
						hrDeductionDet.setLNNO((amttimes));
						hrDeductionDet.setCRBY(username);
						hrDeductionDet.setSTATUS("Pending");
						dmonth = dmonth + 1;
						if((dmonth > 12)) {
							dmonth = 1;
							dyear = dyear+1;
							hrDeductionDet.setDUE_MONTH(String.valueOf(dmonth));
							hrDeductionDet.setDUE_YEAR(String.valueOf(dyear));
						}else {
							hrDeductionDet.setDUE_MONTH(String.valueOf(dmonth));
							hrDeductionDet.setDUE_YEAR(String.valueOf(dyear));
						}
						
						hrDeductionDetDAO.adddeductiondet(hrDeductionDet);
					}
					
				}
				
				String empcode = employeeDAO.getEmpcode(plant, Deduction_empid, "");
				String empname = employeeDAO.getEmpnamebyid(plant, Deduction_empid, "");
				String accname = Deduction_name+"-"+empcode+"-"+empname;
				String payclearing = empcode+"-"+empname;
				if(Short.valueOf(Deduction_isgratuity) == 0) {
					boolean isexist = coaDAO.isExistsAccount(accname, plant,"18");
					if(!isexist) {	
						Hashtable<String, String> accountHt = new Hashtable<>();
						accountHt.put("PLANT", plant);
						accountHt.put("ACCOUNTTYPE", "3");
						accountHt.put("ACCOUNTDETAILTYPE", "10");
						accountHt.put("ACCOUNT_NAME", accname);
						accountHt.put("DESCRIPTION", "");
						accountHt.put("ISSUBACCOUNT", "1");
						accountHt.put("SUBACCOUNTNAME", "18");
						accountHt.put("OPENINGBALANCE", "");
						accountHt.put("OPENINGBALANCEDATE", "");
						accountHt.put("CRAT", dateutils.getDateTime());
						accountHt.put("CRBY", username);
						accountHt.put("UPAT", dateutils.getDateTime());
						String gcode = coaDAO.GetGCodeById("3", plant);
						String dcode = coaDAO.GetDCodeById("10", plant);
						List<Map<String, String>> listQry = coaDAO.getMaxAccoutCode(plant, "3", "10");
						String maxid = "";
						String atcode = "";
						if(listQry.size() > 0) {
							for (int J = 0; J < listQry.size(); J++) {
								Map<String, String> m = listQry.get(J);
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
						coaUtil.addAccount(accountHt, plant);
					}
					

					String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					
					
					JournalHeader journalHead=new JournalHeader();
					journalHead.setPLANT(plant);
					journalHead.setJOURNAL_DATE(Deduction_date);
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(curency);
					journalHead.setTRANSACTION_TYPE("LOANORADVANCE");
					journalHead.setTRANSACTION_ID(String.valueOf(hid));
					journalHead.setSUB_TOTAL(Double.valueOf(Deduction_amount));
					journalHead.setTOTAL_AMOUNT(Double.valueOf(Deduction_amount));
					journalHead.setCRAT(dateutils.getDateTime());
					journalHead.setCRBY(username);
					

					JournalDetail journalDetail_2=new JournalDetail();
					journalDetail_2.setPLANT(plant);
					JSONObject coaJson3=coaDAO.getCOAByNameandsubid(plant, accname,"18");
					journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
					journalDetail_2.setACCOUNT_NAME(accname);
					journalDetail_2.setDEBITS(Double.valueOf(Deduction_amount));
					journalDetails.add(journalDetail_2);
					
					JournalDetail journalDetail_1=new JournalDetail();
					journalDetail_1.setPLANT(plant);
					JSONObject coaJson1=coaDAO.getCOAByName(plant, payclearing);
					journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
					journalDetail_1.setACCOUNT_NAME(payclearing);
					journalDetail_1.setCREDITS(Double.valueOf(Deduction_amount));
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
							Hashtable htMovHis = new Hashtable();
							htMovHis.put(IDBConstants.PLANT, plant);
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
							htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
							htMovHis.put(IDBConstants.ITEM, "");
							htMovHis.put(IDBConstants.QTY, "0.0");
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
							htMovHis.put(IDBConstants.CREATED_BY, username);		
							htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMovHis.put("REMARKS","");
							movHisDao.insertIntoMovHis(htMovHis);
						}
					}
					
				}else {
					boolean isexist = coaDAO.isExistsAccount(accname, plant,"19");
					if(!isexist) {
						Hashtable<String, String> accountHt = new Hashtable<>();
						accountHt.put("PLANT", plant);
						accountHt.put("ACCOUNTTYPE", "3");
						accountHt.put("ACCOUNTDETAILTYPE", "10");
						accountHt.put("ACCOUNT_NAME", accname);
						accountHt.put("DESCRIPTION", "");
						accountHt.put("ISSUBACCOUNT", "1");
						accountHt.put("SUBACCOUNTNAME", "19");
						accountHt.put("OPENINGBALANCE", "");
						accountHt.put("OPENINGBALANCEDATE", "");
						accountHt.put("CRAT", dateutils.getDateTime());
						accountHt.put("CRBY", username);
						accountHt.put("UPAT", dateutils.getDateTime());
						String gcode = coaDAO.GetGCodeById("3", plant);
						String dcode = coaDAO.GetDCodeById("10", plant);
						List<Map<String, String>> listQry = coaDAO.getMaxAccoutCode(plant, "3", "10");
						String maxid = "";
						String atcode = "";
						if(listQry.size() > 0) {
							for (int J = 0; J < listQry.size(); J++) {
								Map<String, String> m = listQry.get(J);
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
						coaUtil.addAccount(accountHt, plant);
					}
					

					String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					
					JournalHeader journalHead=new JournalHeader();
					journalHead.setPLANT(plant);
					journalHead.setJOURNAL_DATE(Deduction_date);
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(curency);
					journalHead.setTRANSACTION_TYPE("LOANORADVANCE");
					journalHead.setTRANSACTION_ID(String.valueOf(hid));
					journalHead.setSUB_TOTAL(Double.valueOf(Deduction_amount));
					journalHead.setTOTAL_AMOUNT(Double.valueOf(Deduction_amount));
					journalHead.setCRAT(dateutils.getDateTime());
					journalHead.setCRBY(username);
					

					JournalDetail journalDetail_2=new JournalDetail();
					journalDetail_2.setPLANT(plant);
					JSONObject coaJson3=coaDAO.getCOAByNameandsubid(plant, accname,"19");
					journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
					journalDetail_2.setACCOUNT_NAME(accname);
					journalDetail_2.setDEBITS(Double.valueOf(Deduction_amount));
					journalDetails.add(journalDetail_2);
					
					JournalDetail journalDetail_1=new JournalDetail();
					journalDetail_1.setPLANT(plant);
					JSONObject coaJson1=coaDAO.getCOAByName(plant, payclearing);
					journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
					journalDetail_1.setACCOUNT_NAME(payclearing);
					journalDetail_1.setCREDITS(Double.valueOf(Deduction_amount));
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
							Hashtable htMovHis = new Hashtable();
							htMovHis.put(IDBConstants.PLANT, plant);
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
							htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
							htMovHis.put(IDBConstants.ITEM, "");
							htMovHis.put(IDBConstants.QTY, "0.0");
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
							htMovHis.put(IDBConstants.CREATED_BY, username);		
							htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMovHis.put("REMARKS","");
							movHisDao.insertIntoMovHis(htMovHis);
						}
					}
					
				}
				
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYROLL_DEDUCTION_MASTER);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));													
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",Deduction_empid+","+Deduction_name+","+Deduction_amount+","+Deduction_dueamount);
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
				
				DbBean.CommitTran(ut);
				String result = "Payroll Deduction added successfully.";
				response.sendRedirect("../payroll/deduction?result="+ result);
				
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				String result = "Couldn't add Payroll Deduction";
				response.sendRedirect("../payroll/creatededuction?result="+ result);
				
			}
		}
		
		if(action.equalsIgnoreCase("EditDetection"))
		{
			
			String Deduction_month = StrUtils.fString(request.getParameter("Deduction_month"));
			String Deduction_year = StrUtils.fString(request.getParameter("Deduction_year"));
			String Deduction_name = StrUtils.fString(request.getParameter("Deduction_name"));
			String Deduction_empid = StrUtils.fString(request.getParameter("Deduction_empid"));
			String Deduction_emptype = StrUtils.fString(request.getParameter("Deduction_emptype"));
			String Deduction_date = StrUtils.fString(request.getParameter("Deduction_date"));
			String Deduction_amount = StrUtils.fString(request.getParameter("Deduction_amount"));
			String Deduction_dueamount = StrUtils.fString(request.getParameter("Deduction_dueamount"));
			String Deduction_isgratuity = StrUtils.fString(request.getParameter("Deduction_isgratuity"));
			String hdrid = StrUtils.fString(request.getParameter("hdrid"));
			CoaDAO coaDAO = new CoaDAO();
			CoaUtil coaUtil = new CoaUtil();
			EmployeeDAO employeeDAO = new EmployeeDAO();
			
	        UserTransaction ut = null;
			try {
				
				ut = DbBean.getUserTranaction();				
				ut.begin();
				
				List<JournalDetail> journalDetails=new ArrayList<>();
				HrDeductionHdr hrDeductionHdr = hrDeductionHdrDAO.getdeductionhdrById(plant, Integer.valueOf(hdrid));
				
				hrDeductionHdr.setPLANT(plant);
				hrDeductionHdr.setEMPID(Integer.valueOf(Deduction_empid));
				hrDeductionHdr.setDEDUCTION_NAME(Deduction_name);
				hrDeductionHdr.setDEDUCTION_AMOUNT(Double.valueOf(Deduction_amount));
				hrDeductionHdr.setDEDUCTION_DUE(Double.valueOf(Deduction_dueamount));
				hrDeductionHdr.setDEDUCTION_DATE(Deduction_date);
				hrDeductionHdr.setMONTH(Deduction_month);
				hrDeductionHdr.setYEAR(Deduction_year);
				hrDeductionHdr.setISGRATUITY(Short.valueOf(Deduction_isgratuity));
				hrDeductionHdr.setCRBY(username);
				
				hrDeductionHdrDAO.updatedeductionhdr(hrDeductionHdr, username);
				
				List<HrDeductionDet> HrDeductionDetList = hrDeductionDetDAO.getAlldeductiondetbyhdrid(plant, Integer.valueOf(hdrid));
				for (HrDeductionDet hrDeductionDet : HrDeductionDetList) {
					hrDeductionDetDAO.Deletedeductiondet(plant, hrDeductionDet.getID());
				}
				
				
				if(Short.valueOf(Deduction_isgratuity) == 0) {
					
					double amount = Double.valueOf(Deduction_amount); 
					double dueamount = Double.valueOf(Deduction_dueamount); 
					double amounttimes = amount / dueamount;
					int amttimes  = (int)amounttimes;
					double amountbal = amount % dueamount;
					
					int dmonth = Integer.valueOf(Deduction_month);
					int dyear = Integer.valueOf(Deduction_year);
					
					for (int i = 0; i < amttimes; i++) {
						HrDeductionDet hrDeductionDet = new HrDeductionDet();
						hrDeductionDet.setPLANT(plant);
						hrDeductionDet.setEMPID(Integer.valueOf(Deduction_empid));
						hrDeductionDet.setHDRID(Integer.valueOf(hdrid));
						hrDeductionDet.setDUE_AMOUNT(Double.valueOf(Deduction_dueamount));
						hrDeductionDet.setLNNO((i));
						hrDeductionDet.setCRBY(username);
						hrDeductionDet.setSTATUS("Pending");
						dmonth = dmonth + 1;
						if((dmonth > 12)) {
							dmonth = 1;
							dyear = dyear+1;
							hrDeductionDet.setDUE_MONTH(String.valueOf(dmonth));
							hrDeductionDet.setDUE_YEAR(String.valueOf(dyear));
						}else {
							hrDeductionDet.setDUE_MONTH(String.valueOf(dmonth));
							hrDeductionDet.setDUE_YEAR(String.valueOf(dyear));
						}
						
						hrDeductionDetDAO.adddeductiondet(hrDeductionDet);
					}
					
					if(amountbal > 0) {
						HrDeductionDet hrDeductionDet = new HrDeductionDet();
						hrDeductionDet.setPLANT(plant);
						hrDeductionDet.setEMPID(Integer.valueOf(Deduction_empid));
						hrDeductionDet.setHDRID(Integer.valueOf(hdrid));
						hrDeductionDet.setDUE_AMOUNT(amountbal);
						hrDeductionDet.setLNNO((amttimes));
						hrDeductionDet.setCRBY(username);
						hrDeductionDet.setSTATUS("Pending");
						dmonth = dmonth + 1;
						if((dmonth > 12)) {
							dmonth = 1;
							dyear = dyear+1;
							hrDeductionDet.setDUE_MONTH(String.valueOf(dmonth));
							hrDeductionDet.setDUE_YEAR(String.valueOf(dyear));
						}else {
							hrDeductionDet.setDUE_MONTH(String.valueOf(dmonth));
							hrDeductionDet.setDUE_YEAR(String.valueOf(dyear));
						}
						
						hrDeductionDetDAO.adddeductiondet(hrDeductionDet);
					}
					
				}
				
				String empcode = employeeDAO.getEmpcode(plant, Deduction_empid, "");
				String empname = employeeDAO.getEmpnamebyid(plant, Deduction_empid, "");
				String accname = Deduction_name+"-"+empcode+"-"+empname;
				String payclearing = empcode+"-"+empname;
				if(Short.valueOf(Deduction_isgratuity) == 0) {
					boolean isexist = coaDAO.isExistsAccount(accname, plant,"18");
					if(!isexist) {
						Hashtable<String, String> accountHt = new Hashtable<>();
						accountHt.put("PLANT", plant);
						accountHt.put("ACCOUNTTYPE", "3");
						accountHt.put("ACCOUNTDETAILTYPE", "10");
						accountHt.put("ACCOUNT_NAME", accname);
						accountHt.put("DESCRIPTION", "");
						accountHt.put("ISSUBACCOUNT", "1");
						accountHt.put("SUBACCOUNTNAME", "18");
						accountHt.put("OPENINGBALANCE", "");
						accountHt.put("OPENINGBALANCEDATE", "");
						accountHt.put("CRAT", dateutils.getDateTime());
						accountHt.put("CRBY", username);
						accountHt.put("UPAT", dateutils.getDateTime());
						String gcode = coaDAO.GetGCodeById("3", plant);
						String dcode = coaDAO.GetDCodeById("10", plant);
						List<Map<String, String>> listQry = coaDAO.getMaxAccoutCode(plant, "3", "10");
						String maxid = "";
						String atcode = "";
						if(listQry.size() > 0) {
							for (int J = 0; J < listQry.size(); J++) {
								Map<String, String> m = listQry.get(J);
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
						coaUtil.addAccount(accountHt, plant);
					}
					
					String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					
					JournalHeader journalHead=new JournalHeader();
					journalHead.setPLANT(plant);
					journalHead.setJOURNAL_DATE(Deduction_date);
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(curency);
					journalHead.setTRANSACTION_TYPE("LOANORADVANCE");
					journalHead.setTRANSACTION_ID(String.valueOf(hdrid));
					journalHead.setSUB_TOTAL(Double.valueOf(Deduction_amount));
					journalHead.setTOTAL_AMOUNT(Double.valueOf(Deduction_amount));
					journalHead.setCRAT(dateutils.getDateTime());
					journalHead.setCRBY(username);
					

					JournalDetail journalDetail_2=new JournalDetail();
					journalDetail_2.setPLANT(plant);
					JSONObject coaJson3=coaDAO.getCOAByNameandsubid(plant, accname,"18");
					journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
					journalDetail_2.setACCOUNT_NAME(accname);
					journalDetail_2.setDEBITS(Double.valueOf(Deduction_amount));
					journalDetails.add(journalDetail_2);
					
					JournalDetail journalDetail_1=new JournalDetail();
					journalDetail_1.setPLANT(plant);
					JSONObject coaJson1=coaDAO.getCOAByName(plant, payclearing);
					journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
					journalDetail_1.setACCOUNT_NAME(payclearing);
					journalDetail_1.setCREDITS(Double.valueOf(Deduction_amount));
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
							Hashtable htMovHis = new Hashtable();
							htMovHis.put(IDBConstants.PLANT, plant);
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
							htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
							htMovHis.put(IDBConstants.ITEM, "");
							htMovHis.put(IDBConstants.QTY, "0.0");
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
							htMovHis.put(IDBConstants.CREATED_BY, username);		
							htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMovHis.put("REMARKS","");
							movHisDao.insertIntoMovHis(htMovHis);
						}
					}
				}else {
					boolean isexist = coaDAO.isExistsAccount(accname, plant,"19");
					if(!isexist) {
						Hashtable<String, String> accountHt = new Hashtable<>();
						accountHt.put("PLANT", plant);
						accountHt.put("ACCOUNTTYPE", "3");
						accountHt.put("ACCOUNTDETAILTYPE", "10");
						accountHt.put("ACCOUNT_NAME", accname);
						accountHt.put("DESCRIPTION", "");
						accountHt.put("ISSUBACCOUNT", "1");
						accountHt.put("SUBACCOUNTNAME", "19");
						accountHt.put("OPENINGBALANCE", "");
						accountHt.put("OPENINGBALANCEDATE", "");
						accountHt.put("CRAT", dateutils.getDateTime());
						accountHt.put("CRBY", username);
						accountHt.put("UPAT", dateutils.getDateTime());
						String gcode = coaDAO.GetGCodeById("3", plant);
						String dcode = coaDAO.GetDCodeById("10", plant);
						List<Map<String, String>> listQry = coaDAO.getMaxAccoutCode(plant, "3", "10");
						String maxid = "";
						String atcode = "";
						if(listQry.size() > 0) {
							for (int J = 0; J < listQry.size(); J++) {
								Map<String, String> m = listQry.get(J);
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
						coaUtil.addAccount(accountHt, plant);
					}
					
					String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					
					JournalHeader journalHead=new JournalHeader();
					journalHead.setPLANT(plant);
					journalHead.setJOURNAL_DATE(Deduction_date);
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(curency);
					journalHead.setTRANSACTION_TYPE("LOANORADVANCE");
					journalHead.setTRANSACTION_ID(String.valueOf(hdrid));
					journalHead.setSUB_TOTAL(Double.valueOf(Deduction_amount));
					journalHead.setTOTAL_AMOUNT(Double.valueOf(Deduction_amount));
					journalHead.setCRAT(dateutils.getDateTime());
					journalHead.setCRBY(username);
					

					JournalDetail journalDetail_2=new JournalDetail();
					journalDetail_2.setPLANT(plant);
					JSONObject coaJson3=coaDAO.getCOAByNameandsubid(plant, accname,"19");
					journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
					journalDetail_2.setACCOUNT_NAME(accname);
					journalDetail_2.setDEBITS(Double.valueOf(Deduction_amount));
					journalDetails.add(journalDetail_2);
					
					JournalDetail journalDetail_1=new JournalDetail();
					journalDetail_1.setPLANT(plant);
					JSONObject coaJson1=coaDAO.getCOAByName(plant, payclearing);
					journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
					journalDetail_1.setACCOUNT_NAME(payclearing);
					journalDetail_1.setCREDITS(Double.valueOf(Deduction_amount));
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
							Hashtable htMovHis = new Hashtable();
							htMovHis.put(IDBConstants.PLANT, plant);
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
							htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
							htMovHis.put(IDBConstants.ITEM, "");
							htMovHis.put(IDBConstants.QTY, "0.0");
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
							htMovHis.put(IDBConstants.CREATED_BY, username);		
							htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMovHis.put("REMARKS","");
							movHisDao.insertIntoMovHis(htMovHis);
						}
					}
				}
				
				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.UPDATE_PAYROLL_DEDUCTION_MASTER);	
				htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));													
				htMovHis.put("RECID", "");
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				htMovHis.put("REMARKS",Deduction_empid+","+Deduction_name+","+Deduction_amount+","+Deduction_dueamount);
				movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
				
				
				DbBean.CommitTran(ut);
				String result = "Payroll Deduction edited successfully.";
				response.sendRedirect("../payroll/deduction?result="+ result);
				
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				String result = "Couldn't edit Payroll Deduction";
				response.sendRedirect("../payroll/editdeduction?ID="+hdrid+"&result="+ result);
				
			}
		}
		
		if(action.equalsIgnoreCase("DEDUCTION_DESCRIPTION"))
		{
			String HDRID= StrUtils.fString(request.getParameter("HDRID"));
			
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        UserTransaction ut = null;
			try {
				
				ut = DbBean.getUserTranaction();				
				ut.begin();
				
				HrDeductionHdr hrDeductionHdr = hrDeductionHdrDAO.getdeductionhdrById(plant, Integer.valueOf(HDRID));
				List<HrDeductionDet> hrDeductionDet = hrDeductionDetDAO.getAlldeductiondetbyhdrid(plant, Integer.valueOf(HDRID));
				resultJson.put("STATUS", "OK");
				resultJson.put("DHDR", hrDeductionHdr);
				resultJson.put("DDET", hrDeductionDet);
				DbBean.CommitTran(ut);
				
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				resultJson.put("STATUS", "NOT OK");
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			response.getWriter().write(resultJson.toString());
		}
		
		if(action.equalsIgnoreCase("PAYROLL_NUMBER_CHECK"))
		{
			String PAYROLL= StrUtils.fString(request.getParameter("PAYROLL"));
			
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        UserTransaction ut = null;
			try {
				HrPayrollHDRDAO hrPayrollHDRDAO = new HrPayrollHDRDAO();
				boolean ispayroll = hrPayrollHDRDAO.Ispayrollnumber(plant, PAYROLL);
				if(ispayroll) {
					resultJson.put("STATUS", "NOT OK");
				}else {
					resultJson.put("STATUS", "OK");
				}
				
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				resultJson.put("STATUS", "NOT OK");
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			response.getWriter().write(resultJson.toString());
		}
		
		if(action.equalsIgnoreCase("sendEmail")) {
			String attachmentLocation = null;
			try{
				////////////////
				String sendTo = null;
				String sendCC=null;
				String sendSubject = null;
				String sendBody = null;
				String filenamesend = null;
				String arrayofdata = null;
				
				//String strbody="<html> <head> <title>Page Title</title> </head> <body> <table width='100%' border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td align='center' valign='top' bgcolor='#FFFFFF'> <table width='553' border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td colspan='3' align='left' valign='top' bgcolor='#6e8ba8'> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='7' style='font-size:0px;line-height:0px'></td> </tr> </tbody> </table> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td width='24'></td> <td align='left' style='font-family:'Helvetica Neue',Helvetica,Verdana,sans-serif;font-size:18px;color:#ffffff'>Retail Company  </td> </tr> </tbody> </table> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='7' style='font-size:0px;line-height:0px'></td> </tr> </tbody> </table> </td> </tr> <tr> <td width='2' bgcolor='#c0d0e4'></td> <td> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td width='549' bgcolor='#e6ecf4'> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='15' style='font-size:0px;line-height:0px'></td> </tr> </tbody> </table> <table width='549' border='0' cellpadding='0' cellspacing='0'> <tbody> <tr> <td width='24'></td> <td align='left' valign='top'> <span style='font-family:'Helvetica Neue',Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold'>Profit and Loss   </span> <br> <span style='font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px'>For the period ending 11 August, 2020</span> </td> </tr> </tbody> </table> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='15' style='font-size:0px;line-height:0px'></td> </tr> </tbody> </table> </td> </tr> </tbody> </table> <table width='549' border='0' cellpadding='0' cellspacing='0'> <tbody> <tr> <td align='left' valign='top'> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='20' style='font-size:0px;line-height:0px'></td> </tr> </tbody> </table> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='15' style='font-size:0px;line-height:0px'></td> </tr> </tbody> </table> <div style='font-size:14px;color:#000;font-family:'Helvetica Neue',Helvetica,Verdana,sans-serif;margin-left:22px;margin-right:22px;line-height:16px'> Hello<br><br>Attached is the Profit and Loss report for Retail Company. <br><br>Regards<br>suresh kumar </div> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='15' style='font-size:0px;line-height:0px'></td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </td> <td width='2' bgcolor='#c0d0e4'></td> </tr> <tr> <td colspan='3' align='right' bgcolor='#1f3246' style='font-family:'Helvetica Neue',Helvetica,Verdana,sans-serif;color:#ffffff;font-size:11px'> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='8'></td> </tr> </tbody> </table> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='8'></td> </tr> </tbody> </table> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='8'></td> </tr> </tbody> </table> <table border='0' cellspacing='0' cellpadding='0'> <tbody> <tr> <td height='8'></td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </body> </html>";
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);				
					@SuppressWarnings("rawtypes")
					List items = upload.parseRequest(request);
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
							else if (fileItem.getFieldName().equalsIgnoreCase("tabledata")) {
								arrayofdata = StrUtils.fString(fileItem.getString()).trim();
							}
							else if (fileItem.getFieldName().equalsIgnoreCase("filename")) {
								filenamesend = StrUtils.fString(fileItem.getString()).trim();
							}
							
						}
						
						if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							String fileLocation = "C:/ATTACHMENTS/Mail";
							//String filetempLocation = "C:/ATTACHMENTS/MailTemp";
							String fileName = StrUtils.fString(fileItem.getName()).trim();
							fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							
							File path = new File(fileLocation);
							if (!path.exists()) {
								path.mkdirs();
							}
							
							//fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							//File uploadedFile = new File(path + "/" +fileName+".xlsx");
							File uploadedFile = new File(path + "/Payroll Report.xlsx");
							if (uploadedFile.exists()) {
								uploadedFile.delete();
							}
							
							fileItem.write(uploadedFile);
							attachmentLocation=uploadedFile.getAbsolutePath();
						}
					}
				}
				
				userBean ubean = new userBean();
				String companyname = ubean.getCompanyName(plant).toUpperCase(); 
				
				String body="";
				body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
				body +="<div style='position: relative;padding: 0px 15px 0 15px;'>";
				body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;vertical-align: middle;'>";
				body +="<span style='margin-left: 15px;font-size: 15px;font-weight: bold;vertical-align: middle;'>"+companyname+"</span>";
				body +="</div>";
				body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
				body +="<span>Payroll Report</span>";
				body +="</div>";
				body +="<div class='box-header with-border' style='margin-top: 26px;'>";
				body +="<p>Hello,</p>";
				body +="<p>Please find the attached payroll report.</p>";
				body +="</div>";
				body +="<div style='margin-top:30px'>";
				body +="<p>Regards <br>HR Team</p>";
				body +="</div>";
				body +="</div>";
				String subject ="RE:Payroll Report";
				
				
				
				
				SendEmail sendMail=new SendEmail();
				String mailResp=sendMail.sendTOMailPdf(sendTo,sendTo,sendCC,"", subject, sendBody, attachmentLocation);
				
				if(mailResp!=null && mailResp.equals("Sent")) {
					    JSONObject result=new JSONObject();
						result.put("STATUS", "200");
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(result.toString());
						response.getWriter().flush();
						response.getWriter().close();
				 }
				 
			}
			catch(Exception e)
			{
				e.printStackTrace();
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
		
		
		if(action.equalsIgnoreCase("GET_TOTAL_NET_SALARY")) {
			JSONArray jsonResultArray = new JSONArray();
			jsonObjectResult = this.getTotalNetSalary(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		
		if(action.equalsIgnoreCase("GET_TOTAL_GROSS_SALARY")) {
			JSONArray jsonResultArray = new JSONArray();
			jsonObjectResult = this.getTotalGrossSalary(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		
		if(action.equalsIgnoreCase("GET_TOTAL_DEDUCTION")) {
			JSONArray jsonResultArray = new JSONArray();
			jsonObjectResult = this.getTotalDeduction(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		
		if(action.equalsIgnoreCase("GET_TOTAL_NET_GROSS_SALARY")) {
			JSONArray jsonResultArray = new JSONArray();
			jsonObjectResult = this.getTotalNetGrossSalary(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = StrUtils.fString(req.getParameter("CMD")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
		HrPayrollHDRService hrPayrollHDRService = new HrPayrollHDRServiceImpl();
		HrPayrollDETService HrPayrollDETService = new HrPayrollDETServiceImpl();
		PlantMstDAO  plantMstDAO = new PlantMstDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		HrDeductionHdrDAO hrDeductionHdrDAO = new HrDeductionHdrDAO();
		HrDeductionDetDAO hrDeductionDetDAO = new HrDeductionDetDAO();
		HrPayrollDETDAO hrPayrollDETDAO = new HrPayrollDETDAO();
		
		if(action.equalsIgnoreCase("GET_SALARY_ALLOWANCES"))
		{
			String EMPID= StrUtils.fString(req.getParameter("EMPID"));
			String FROMDATE= StrUtils.fString(req.getParameter("FROMDATE"));
			String TODATE= StrUtils.fString(req.getParameter("TODATE"));
			String MONTH= StrUtils.fString(req.getParameter("MONTH"));
			String YEAR= StrUtils.fString(req.getParameter("YEAR"));
			
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        
			try{
				List<HrEmpSalaryDET> HrEmpSalaryDETList = hrEmpSalaryDetDAO.EmpSalarydetlistbyempid(plant, Integer.valueOf(EMPID));
				resultJson.put("SALARYALLOWANCE", HrEmpSalaryDETList);   
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			resp.getWriter().write(resultJson.toString());
			
		}
		
		if(action.equalsIgnoreCase("GET_PAYROLL_SUMMARY"))
		{
			String balace = "0";
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        EmployeeDAO employeeDAO = new EmployeeDAO();
	       // HrDeductionHdrDAO hrDeductionHdrDAO = new HrDeductionHdrDAO()
			
			try {
				List<HrPayrollHDR> PayrollHDRlist = hrPayrollHDRService.getAllpayrollhdr(plant);
				List<PayrollPojo> PayrollPojoList = new ArrayList<PayrollPojo>();
				String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
				
				String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
				for (HrPayrollHDR hrPayrollHDR:PayrollHDRlist) {
					
					String empname = employeeDAO.getEmpnamebyid(plant, String.valueOf(hrPayrollHDR.getEMPID()), "");
					String empcode =employeeDAO.getEmpcode(plant, String.valueOf(hrPayrollHDR.getEMPID()), "");
					String monthyear = months[Integer.valueOf(hrPayrollHDR.getMONTH())-1]+"-"+hrPayrollHDR.getYEAR();
					
					PayrollPojo PayrollPojo = new PayrollPojo();
					PayrollPojo.setID(hrPayrollHDR.getID());
					PayrollPojo.setEMPCODE(empcode);
					PayrollPojo.setEMPNAME(empname);
					PayrollPojo.setMONTH(monthyear);
					PayrollPojo.setPAYROLL(hrPayrollHDR.getPAYROLL());
					PayrollPojo.setATDAYS(String.valueOf(hrPayrollHDR.getPAYDAYS()));
					PayrollPojo.setSALARY(StrUtils.addZeroes(hrPayrollHDR.getTOTAL_AMOUNT(), numberOfDecimal));
					PayrollPojo.setSTATUS(hrPayrollHDR.getSTATUS());
					PayrollPojoList.add(PayrollPojo);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJson.put("PAYROLL", PayrollPojoList);   
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			
			
			resp.getWriter().write(resultJson.toString());
			
		}
		
		if(action.equalsIgnoreCase("GET_PAYROLL_PROCESSING"))
		{
			String balace = "0";
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        EmployeeDAO employeeDAO = new EmployeeDAO();
	       // HrDeductionHdrDAO hrDeductionHdrDAO = new HrDeductionHdrDAO()
			
			try {
				List<HrPayrollHDR> PayrollHDRlist = hrPayrollHDRService.payrollhdrbystatus(plant,"OPEN");
				List<PayrollPojo> PayrollPojoList = new ArrayList<PayrollPojo>();
				String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
				
				String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
				for (HrPayrollHDR hrPayrollHDR:PayrollHDRlist) {
					
					String empname = employeeDAO.getEmpnamebyid(plant, String.valueOf(hrPayrollHDR.getEMPID()), "");
					String empcode =employeeDAO.getEmpcode(plant, String.valueOf(hrPayrollHDR.getEMPID()), "");
					String monthyear = months[Integer.valueOf(hrPayrollHDR.getMONTH())-1]+"-"+hrPayrollHDR.getYEAR();
					
					PayrollPojo PayrollPojo = new PayrollPojo();
					PayrollPojo.setID(hrPayrollHDR.getID());
					PayrollPojo.setEMPCODE(empcode);
					PayrollPojo.setEMPNAME(empname);
					PayrollPojo.setMONTH(monthyear);
					PayrollPojo.setPAYROLL(hrPayrollHDR.getPAYROLL());
					PayrollPojo.setATDAYS(String.valueOf(hrPayrollHDR.getPAYDAYS()));
					PayrollPojo.setSALARY(StrUtils.addZeroes(hrPayrollHDR.getTOTAL_AMOUNT(), numberOfDecimal));
					PayrollPojoList.add(PayrollPojo);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJson.put("PAYROLL", PayrollPojoList);   
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			
			
			resp.getWriter().write(resultJson.toString());
			
		}
		
		if(action.equalsIgnoreCase("GET_PAYROLL_DEDUCTION_SUMMARY"))
		{
			String balace = "0";
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        EmployeeDAO employeeDAO = new EmployeeDAO();
	       // HrDeductionHdrDAO hrDeductionHdrDAO = new HrDeductionHdrDAO()
			
			try {
				List<HrDeductionHdr> hrDeductionHdrlist = hrDeductionHdrDAO.getAlldeductionhdr(plant);
				List<PayrollDeductionPojo> PayrollDeductionPojolist = new ArrayList<PayrollDeductionPojo>();
				String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
				
				String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
				for (HrDeductionHdr hrDeductionHdr:hrDeductionHdrlist) {
					
					String empname = employeeDAO.getEmpnamebyid(plant, String.valueOf(hrDeductionHdr.getEMPID()), "");
					String empcode =employeeDAO.getEmpcode(plant, String.valueOf(hrDeductionHdr.getEMPID()), "");
					String monthyear = months[Integer.valueOf(hrDeductionHdr.getMONTH())-1]+"-"+hrDeductionHdr.getYEAR();
					
					PayrollDeductionPojo payrollDeductionPojo = new PayrollDeductionPojo();
					payrollDeductionPojo.setID(hrDeductionHdr.getID());
					payrollDeductionPojo.setEMPCODE(empcode);
					payrollDeductionPojo.setEMPNAME(empname);
					payrollDeductionPojo.setMONTH(monthyear);
					payrollDeductionPojo.setDAMOUNT(StrUtils.addZeroes(hrDeductionHdr.getDEDUCTION_AMOUNT(), numberOfDecimal));
					payrollDeductionPojo.setDUEAMOUNT(StrUtils.addZeroes(hrDeductionHdr.getDEDUCTION_DUE(), numberOfDecimal));
					payrollDeductionPojo.setDNAME(hrDeductionHdr.getDEDUCTION_NAME());
					payrollDeductionPojo.setISGRATUITY(hrDeductionHdr.getISGRATUITY());
					if(hrDeductionDetDAO.Checkpocess(plant, hrDeductionHdr.getID())) {
						payrollDeductionPojo.setISPROCESSED(Short.valueOf("1"));
					}else {
						payrollDeductionPojo.setISPROCESSED(Short.valueOf("0"));
					}
					PayrollDeductionPojolist.add(payrollDeductionPojo);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJson.put("PAYROLLDEDUCTION", PayrollDeductionPojolist);   
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			
			
			resp.getWriter().write(resultJson.toString());
			
		}
		
		if(action.equalsIgnoreCase("GET_PAYROLL_REPORT"))
		{
			String balace = "0";
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        EmployeeDAO employeeDAO = new EmployeeDAO();
	        HrEmpSalaryDAO hrEmpSalaryDAO = new HrEmpSalaryDAO();
	        HrClaimDAO hrClaimDAO = new HrClaimDAO();
	        HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
	        
			try {
				
				/*ArrayList employeelist = employeeDAO.getAllEmployeeDetails(plant);
				 if (employeelist.size() > 0) {
		               for(int i =0; i<employeelist.size(); i++) {
		                   Map employee = (Map)employeelist.get(i);
		               }
				 }*/
				String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
				String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
				JSONArray payrollarray = new JSONArray();
				List<HrPayrollHDR> hrpayrollhdrlist = hrPayrollHDRService.getAllpayrollhdr(plant);
				for (HrPayrollHDR hrpayroll:hrpayrollhdrlist) {
					 JSONObject payroll = new JSONObject();
					 
					 ArrayList arrEmp = employeeDAO.getEmployeeListbyid(String.valueOf(hrpayroll.getEMPID()),plant);
					 Map employee=(Map)arrEmp.get(0);
					 String attdays = String.valueOf(hrpayroll.getPAYDAYS());
					 String paymonth = months[Integer.valueOf(hrpayroll.getMONTH())-1]+"-"+hrpayroll.getYEAR();
							 
					 payroll.put("Emp Code",employee.get("EMPNO"));
					 payroll.put("Name", employee.get("FNAME"));
					 payroll.put("Designation", employee.get("DESGINATION"));
					 payroll.put("Date Of Joining", employee.get("DATEOFJOINING"));
					 payroll.put("Month", paymonth);
					 payroll.put("Attendance", attdays);
					 
					 double payrolladdition = 0;
					 double payrolldeduction = 0;
					 double gratuityamount = 0;
					 double totalsalary = 0;
					 double netsalary = 0;
					 
					 List<HrEmpSalaryMst> empsalarymstlist = hrEmpSalaryDAO.getAllSalary(plant);
					 List<HrPayrollDET> hrPayrollDETlist = HrPayrollDETService.getpayrolldetByhdrid(plant, hrpayroll.getID());
					 for (HrEmpSalaryMst empsalarymst:empsalarymstlist) {
						 boolean ithasnoallowance = true;
						 for (HrPayrollDET hrPayrollDET : hrPayrollDETlist) {
							if(empsalarymst.getSALARYTYPE().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())) {
								payroll.put(empsalarymst.getSALARYTYPE(),StrUtils.addZeroes(hrPayrollDET.getAMOUNT(), numberOfDecimal));
								totalsalary = totalsalary + hrPayrollDET.getAMOUNT();
								ithasnoallowance = false;
							}
						 }
						  if(ithasnoallowance) {
							  payroll.put(empsalarymst.getSALARYTYPE(),StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal));
						  }
					 }
					 
					 payroll.put("Total Salary", StrUtils.addZeroes(totalsalary, numberOfDecimal));
					 
					 List<HrPayrollAdditionMst> payaddmstadd = hrPayrollAdditionMstDAO.payrolladditionmstadd(plant, "");
					 for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){
						 for(HrPayrollAdditionMst hrPayrollAdditionMst:payaddmstadd){
							 if(hrPayrollAdditionMst.getADDITION_NAME().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())){
								 payrolladdition = payrolladdition + hrPayrollDET.getAMOUNT();
							 }
						 }
					 }
					 List<HrDeductionHdr> hrDeductionHdrlist =  hrDeductionHdrDAO.getdeductionhdrempmonthyear(plant,String.valueOf(hrpayroll.getEMPID()), hrpayroll.getMONTH(), hrpayroll.getYEAR());
					 for (HrDeductionHdr hrDeductionHdr : hrDeductionHdrlist) {
						 payrolladdition = payrolladdition + hrDeductionHdr.getDEDUCTION_AMOUNT();
						 if(hrDeductionHdr.getISGRATUITY() == 1) {
							 gratuityamount = gratuityamount + hrDeductionHdr.getDEDUCTION_AMOUNT(); 
						 }
					 }
					 
					 List<HrPayrollAdditionMst> payaddmstdeduct = hrPayrollAdditionMstDAO.payrolladditionmstdeduct(plant, "");
					 for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){
						 for(HrPayrollAdditionMst hrPayrollAdditionMst:payaddmstdeduct){
							 if(hrPayrollAdditionMst.getADDITION_NAME().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())){
								 payrolldeduction = payrolldeduction + hrPayrollDET.getAMOUNT();
							 }
						 }
					  }
					 
					 payroll.put("Addition", StrUtils.addZeroes(payrolladdition, numberOfDecimal));
					 payroll.put("Deduction", StrUtils.addZeroes(payrolldeduction, numberOfDecimal));
					 netsalary = (totalsalary + payrolladdition) - payrolldeduction;
					 payroll.put("Net Salary", StrUtils.addZeroes(netsalary, numberOfDecimal));
					 payroll.put("Gratuity", StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal));
					 payroll.put("Loan Against Gratuity", StrUtils.addZeroes(gratuityamount, numberOfDecimal));
					 
					 double claimamount = hrClaimDAO.getclaimamountbtdates(plant, hrpayroll.getEMPID(), hrpayroll.getFROMDATE(), hrpayroll.getTODATE());
					 
					 payroll.put("Claim Amount", StrUtils.addZeroes(claimamount, numberOfDecimal));
					 
					 payrollarray.add(payroll);
				}
				
				JSONObject resultJsonInt = new JSONObject();
				resultJson.put("PAYROLL", payrollarray);   
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			
			
			resp.getWriter().write(resultJson.toString());
			
		}
		
		
		if(action.equalsIgnoreCase("GET_PAYROLL_REPORT_FILTER"))
		{
			String balace = "0";
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        EmployeeDAO employeeDAO = new EmployeeDAO();
	        HrEmpSalaryDAO hrEmpSalaryDAO = new HrEmpSalaryDAO();
	        HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
	        String fm= StrUtils.fString(req.getParameter("FM"));
			String fy= StrUtils.fString(req.getParameter("FY"));
			String tm= StrUtils.fString(req.getParameter("TM"));
			String ty= StrUtils.fString(req.getParameter("TY"));
			String filter= StrUtils.fString(req.getParameter("FILTER"));
			try {
				//List<HrPayrollHDR> hrpayrollhdrlist = hrPayrollHDRService.getAllpayrollhdr(plant);
				
				List<HrPayrollHDR> hrpayrollhdrlist = new ArrayList<HrPayrollHDR>();
				List<MonthYearPojo> monthyyearlist = new ArrayList<MonthYearPojo>();
				if(filter.equalsIgnoreCase("2") || filter.equalsIgnoreCase("3")) {
					monthyyearlist = getmonthyear(fm, fy, fm, fy);
				}else {
					monthyyearlist = getmonthyear(fm, fy, tm, ty);
				}
				for (MonthYearPojo monthYearPojo : monthyyearlist) {
					List<HrPayrollHDR> hrpayrollhdrmylist = hrPayrollHDRService.payrollhdrbymonthyear(plant, monthYearPojo.getMONTH(), monthYearPojo.getYEAR());
					hrpayrollhdrlist.addAll(hrpayrollhdrmylist);
				}
				String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
				String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
				JSONArray payrollarray = new JSONArray();
				
				for (HrPayrollHDR hrpayroll:hrpayrollhdrlist) {
					 JSONObject payroll = new JSONObject();
					 
					 ArrayList arrEmp = employeeDAO.getEmployeeListbyid(String.valueOf(hrpayroll.getEMPID()),plant);
					 Map employee=(Map)arrEmp.get(0);
					 String attdays = String.valueOf(hrpayroll.getPAYDAYS());
					 String paymonth = months[Integer.valueOf(hrpayroll.getMONTH())-1]+"-"+hrpayroll.getYEAR();
							 
					 payroll.put("Emp Code",employee.get("EMPNO"));
					 payroll.put("Name", employee.get("FNAME"));
					 payroll.put("Designation", employee.get("DESGINATION"));
					 payroll.put("Date Of Joining", employee.get("DATEOFJOINING"));
					 payroll.put("Month", paymonth);
					 payroll.put("Attendance", attdays);
					 
					 double payrolladdition = 0;
					 double payrolldeduction = 0;
					 double gratuityamount = 0;
					 double totalsalary = 0;
					 double netsalary = 0;
					 
					 List<HrEmpSalaryMst> empsalarymstlist = hrEmpSalaryDAO.getAllSalary(plant);
					 List<HrPayrollDET> hrPayrollDETlist = HrPayrollDETService.getpayrolldetByhdrid(plant, hrpayroll.getID());
					 for (HrEmpSalaryMst empsalarymst:empsalarymstlist) {
						 boolean ithasnoallowance = true;
						 for (HrPayrollDET hrPayrollDET : hrPayrollDETlist) {
							if(empsalarymst.getSALARYTYPE().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())) {
								payroll.put(empsalarymst.getSALARYTYPE(),StrUtils.addZeroes(hrPayrollDET.getAMOUNT(), numberOfDecimal));
								totalsalary = totalsalary + hrPayrollDET.getAMOUNT();
								ithasnoallowance = false;
							}
						 }
						  if(ithasnoallowance) {
							  payroll.put(empsalarymst.getSALARYTYPE(),StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal));
						  }
					 }
					 
					 payroll.put("Total Salary", StrUtils.addZeroes(totalsalary, numberOfDecimal));
					 
					 List<HrPayrollAdditionMst> payaddmstadd = hrPayrollAdditionMstDAO.payrolladditionmstadd(plant, "");
					 for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){
						 for(HrPayrollAdditionMst hrPayrollAdditionMst:payaddmstadd){
							 if(hrPayrollAdditionMst.getADDITION_NAME().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())){
								 payrolladdition = payrolladdition + hrPayrollDET.getAMOUNT();
							 }
						 }
					 }
					 List<HrDeductionHdr> hrDeductionHdrlist =  hrDeductionHdrDAO.getdeductionhdrempmonthyear(plant,String.valueOf(hrpayroll.getEMPID()), hrpayroll.getMONTH(), hrpayroll.getYEAR());
					 for (HrDeductionHdr hrDeductionHdr : hrDeductionHdrlist) {
						 payrolladdition = payrolladdition + hrDeductionHdr.getDEDUCTION_AMOUNT();
						 if(hrDeductionHdr.getISGRATUITY() == 1) {
							 gratuityamount = gratuityamount + hrDeductionHdr.getDEDUCTION_AMOUNT(); 
						 }
					 }
					 
					 List<HrPayrollAdditionMst> payaddmstdeduct = hrPayrollAdditionMstDAO.payrolladditionmstdeduct(plant, "");
					 for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){
						 for(HrPayrollAdditionMst hrPayrollAdditionMst:payaddmstdeduct){
							 if(hrPayrollAdditionMst.getADDITION_NAME().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())){
								 payrolldeduction = payrolldeduction + hrPayrollDET.getAMOUNT();
							 }
						 }
					  }
					 
					 payroll.put("Addition", StrUtils.addZeroes(payrolladdition, numberOfDecimal));
					 payroll.put("Deduction", StrUtils.addZeroes(payrolldeduction, numberOfDecimal));
					 netsalary = (totalsalary + payrolladdition) - payrolldeduction;
					 payroll.put("Net Salary", StrUtils.addZeroes(netsalary, numberOfDecimal));
					 payroll.put("Gratuity", StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal));
					 payroll.put("Loan Against Gratuity", StrUtils.addZeroes(gratuityamount, numberOfDecimal));
					 
					 payrollarray.add(payroll);
				}
				
				JSONObject resultJsonInt = new JSONObject();
				resultJson.put("PAYROLL", payrollarray);   
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			
			
			resp.getWriter().write(resultJson.toString());
			
		}
		
		
		if(action.equalsIgnoreCase("GET_PAYSLIP"))
		{
			 String hdrid= StrUtils.fString(req.getParameter("HDRID"));
			 EmployeeDAO employeeDAO = new EmployeeDAO();
			 ByteArrayOutputStream out = new ByteArrayOutputStream();            
			    try {
			    	
			    	Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
			        PdfWriter writer = PdfWriter.getInstance(doc, out);
			        doc.open();
			    	addPDFbody(doc,hdrid,plant);
			        doc.close();
			        writer.close();
				    byte[] bytes = out.toByteArray();
				    resp.addHeader("Content-disposition","attachment;filename=payslip.pdf");
				    resp.setContentLength(bytes.length);
				    resp.getOutputStream().write(bytes);
				    resp.setContentType("application/pdf");
			    }catch (Exception e) {
					System.out.println(e);
				}
		}
		
		if(action.equalsIgnoreCase("GET_PAYSLIP_OPEN"))
		{
			 String hdrid= StrUtils.fString(req.getParameter("HDRID"));
			 EmployeeDAO employeeDAO = new EmployeeDAO();
			 ByteArrayOutputStream out = new ByteArrayOutputStream();            
			    try {
			    	
			    	Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
			        PdfWriter writer = PdfWriter.getInstance(doc, out);
			        doc.open();
			    	addPDFbody(doc,hdrid,plant);
			        doc.close();
			        writer.close();
				    byte[] bytes = out.toByteArray();
				    resp.addHeader("Content-disposition","inline;filename=payslip.pdf");
				    resp.setContentLength(bytes.length);
				    resp.getOutputStream().write(bytes);
				    resp.setContentType("application/pdf");
			    }catch (Exception e) {
					System.out.println(e);
				}
		}
		
		if(action.equalsIgnoreCase("GET_PAYSLIP_ALL"))
		{
			String filter=StrUtils.fString(req.getParameter("selfilter"));
			String empid=StrUtils.fString(req.getParameter("empid"));
			String fm=StrUtils.fString(req.getParameter("from_month"));
			String fy=StrUtils.fString(req.getParameter("from_year"));
			String tm=StrUtils.fString(req.getParameter("to_month"));
			String ty=StrUtils.fString(req.getParameter("to_year"));

			 ByteArrayOutputStream out = new ByteArrayOutputStream();            
			    try {
			    	
			    	Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
			        PdfWriter writer = PdfWriter.getInstance(doc, out);
			        doc.open();
			        
			        List<HrPayrollHDR> hrpayrollhdrlist = new ArrayList<HrPayrollHDR>();
					List<MonthYearPojo> monthyyearlist = new ArrayList<MonthYearPojo>();
					if(filter.equalsIgnoreCase("2") || filter.equalsIgnoreCase("3")) {
						monthyyearlist = getmonthyear(fm, fy, fm, fy);
					}else {
						monthyyearlist = getmonthyear(fm, fy, tm, ty);
					}
					for (MonthYearPojo monthYearPojo : monthyyearlist) {
						List<HrPayrollHDR> hrpayrollhdrmylist = hrPayrollHDRService.payrollhdrbymonthyear(plant, monthYearPojo.getMONTH(), monthYearPojo.getYEAR());
						hrpayrollhdrlist.addAll(hrpayrollhdrmylist);
					}
					
					hrpayrollhdrlist = hrpayrollhdrlist.stream().filter((p)->p.getSTATUS().equalsIgnoreCase("PAID")).collect(Collectors.toList());
					
					String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
					String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
					JSONArray payrollarray = new JSONArray();
					
					List<HrPayrollHDR> hrpayrollhdrlistpayslip = new ArrayList<HrPayrollHDR>();
					
					for (HrPayrollHDR hrpayrollhdr : hrpayrollhdrlist) {
							if(empid.equalsIgnoreCase("0")) {
								hrpayrollhdrlistpayslip.add(hrpayrollhdr);
							}else if(empid.equalsIgnoreCase(String.valueOf(hrpayrollhdr.getEMPID()))) {
								hrpayrollhdrlistpayslip.add(hrpayrollhdr);
							}
					}
					
					
					int i=0;
					for (HrPayrollHDR hrpayrollhdr : hrpayrollhdrlistpayslip) {
						if(i == 0) {
							addPDFbody(doc,String.valueOf(hrpayrollhdr.getID()),plant);
						}else {
							doc.newPage();
							addPDFbody(doc,String.valueOf(hrpayrollhdr.getID()),plant);
						}
						i++;
					}
			    	
			        doc.close();
			        writer.close();
				    byte[] bytes = out.toByteArray();
				    resp.addHeader("Content-disposition","attachment;filename=payslip.pdf");
				    resp.setContentLength(bytes.length);
				    resp.getOutputStream().write(bytes);
				    resp.setContentType("application/pdf");
			    }catch (Exception e) {
					System.out.println(e);
				}
		}
		
		
		if(action.equalsIgnoreCase("GET_PAYSLIP_ALL_PRINT"))
		{
			String filter=StrUtils.fString(req.getParameter("selfilter"));
			String empid=StrUtils.fString(req.getParameter("empid"));
			String fm=StrUtils.fString(req.getParameter("from_month"));
			String fy=StrUtils.fString(req.getParameter("from_year"));
			String tm=StrUtils.fString(req.getParameter("to_month"));
			String ty=StrUtils.fString(req.getParameter("to_year"));

			 ByteArrayOutputStream out = new ByteArrayOutputStream();            
			    try {
			    	
			    	Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
			        PdfWriter writer = PdfWriter.getInstance(doc, out);
			        doc.open();
			        
			        List<HrPayrollHDR> hrpayrollhdrlist = new ArrayList<HrPayrollHDR>();
					List<MonthYearPojo> monthyyearlist = new ArrayList<MonthYearPojo>();
					if(filter.equalsIgnoreCase("2") || filter.equalsIgnoreCase("3")) {
						monthyyearlist = getmonthyear(fm, fy, fm, fy);
					}else {
						monthyyearlist = getmonthyear(fm, fy, tm, ty);
					}
					for (MonthYearPojo monthYearPojo : monthyyearlist) {
						List<HrPayrollHDR> hrpayrollhdrmylist = hrPayrollHDRService.payrollhdrbymonthyear(plant, monthYearPojo.getMONTH(), monthYearPojo.getYEAR());
						hrpayrollhdrlist.addAll(hrpayrollhdrmylist);
					}
					hrpayrollhdrlist = hrpayrollhdrlist.stream().filter((p)->p.getSTATUS().equalsIgnoreCase("PAID")).collect(Collectors.toList());
					
					String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
					String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
					JSONArray payrollarray = new JSONArray();
					
					List<HrPayrollHDR> hrpayrollhdrlistpayslip = new ArrayList<HrPayrollHDR>();
					
					for (HrPayrollHDR hrpayrollhdr : hrpayrollhdrlist) {
							if(empid.equalsIgnoreCase("0")) {
								hrpayrollhdrlistpayslip.add(hrpayrollhdr);
							}else if(empid.equalsIgnoreCase(String.valueOf(hrpayrollhdr.getEMPID()))) {
								hrpayrollhdrlistpayslip.add(hrpayrollhdr);
							}
					}
					
					
					int i=0;
					for (HrPayrollHDR hrpayrollhdr : hrpayrollhdrlistpayslip) {
						if(i == 0) {
							addPDFbody(doc,String.valueOf(hrpayrollhdr.getID()),plant);
						}else {
							doc.newPage();
							addPDFbody(doc,String.valueOf(hrpayrollhdr.getID()),plant);
						}
						i++;
					}
			    	
			        doc.close();
			        writer.close();
				    byte[] bytes = out.toByteArray();
				    resp.addHeader("Content-disposition","inline;filename=payslip.pdf");
				    resp.setContentLength(bytes.length);
				    resp.getOutputStream().write(bytes);
				    resp.setContentType("application/pdf");
			    }catch (Exception e) {
					System.out.println(e);
				}
		}
		
		
		if(action.equalsIgnoreCase("SEND_PAYSLIP_EMAIL"))
		{
			String filter=StrUtils.fString(req.getParameter("selfilter"));
			String empid=StrUtils.fString(req.getParameter("empid"));
			String fm=StrUtils.fString(req.getParameter("from_month"));
			String fy=StrUtils.fString(req.getParameter("from_year"));
			String tm=StrUtils.fString(req.getParameter("to_month"));
			String ty=StrUtils.fString(req.getParameter("to_year"));
			EmployeeDAO employeeDAO = new EmployeeDAO();
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			JSONObject resultJson = new JSONObject();
			    try {
			    	
			    	userBean ubean = new userBean();
			        
			        List<HrPayrollHDR> hrpayrollhdrlist = new ArrayList<HrPayrollHDR>();
					List<MonthYearPojo> monthyyearlist = new ArrayList<MonthYearPojo>();
					if(filter.equalsIgnoreCase("2") || filter.equalsIgnoreCase("3")) {
						monthyyearlist = getmonthyear(fm, fy, fm, fy);
					}else {
						monthyyearlist = getmonthyear(fm, fy, tm, ty);
					}
					for (MonthYearPojo monthYearPojo : monthyyearlist) {
						List<HrPayrollHDR> hrpayrollhdrmylist = hrPayrollHDRService.payrollhdrbymonthyear(plant, monthYearPojo.getMONTH(), monthYearPojo.getYEAR());
						hrpayrollhdrlist.addAll(hrpayrollhdrmylist);
					}
					hrpayrollhdrlist = hrpayrollhdrlist.stream().filter((p)->p.getSTATUS().equalsIgnoreCase("PAID")).collect(Collectors.toList());
					String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
					String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
					JSONArray payrollarray = new JSONArray();
					
					List<HrPayrollHDR> hrpayrollhdrlistpayslip = new ArrayList<HrPayrollHDR>();
					
					for (HrPayrollHDR hrpayrollhdr : hrpayrollhdrlist) {
							if(empid.equalsIgnoreCase("0")) {
								hrpayrollhdrlistpayslip.add(hrpayrollhdr);
							}else if(empid.equalsIgnoreCase(String.valueOf(hrpayrollhdr.getEMPID()))) {
								hrpayrollhdrlistpayslip.add(hrpayrollhdr);
							}
					}
					
					for (HrPayrollHDR hrpayrollhdr : hrpayrollhdrlistpayslip) {
						
						ArrayList empmstlistapp = employeeDAO.getEmployeeListbyid(String.valueOf(hrpayrollhdr.getEMPID()),plant);
						Map empmstapp=(Map)empmstlistapp.get(0);
				        String to = (String)empmstapp.get("EMAIL");

				        if(to.equalsIgnoreCase("") || to == null) {
				        	continue;
				        }

						Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
				        PdfWriter writer = PdfWriter.getInstance(doc, out);
				        doc.open();
				        addPDFbody(doc,String.valueOf(hrpayrollhdr.getID()),plant);
				        doc.close();
				        writer.close();
					    byte[] bytes = out.toByteArray();
					    
					    DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
			            MimeBodyPart pdfBodyPart = new MimeBodyPart();
			            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			            pdfBodyPart.setFileName("Payslip.pdf");
			            
						String monthyear = months[Integer.valueOf(hrpayrollhdr.getMONTH())-1]+","+hrpayrollhdr.getYEAR();
						
						String companyname = ubean.getCompanyName(plant).toUpperCase();             
			            String body="";
						body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
						body +="<div style='position: relative;padding: 0px 15px 0 15px;'>";
						body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;vertical-align: middle;'>";
						body +="<span style='margin-left: 15px;font-size: 15px;font-weight: bold;vertical-align: middle;'>"+companyname+"</span>";
						body +="</div>";
						body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
						body +="<span>Payslip for "+monthyear+"</span>";
						body +="</div>";
						body +="<div class='box-header with-border' style='margin-top: 26px;'>";
						body +="<p>Hello,</p>";
						body +="<p>Please find the attached payslip.</p>";
						body +="</div>";
						body +="<div style='margin-top:30px'>";
						body +="<p>Regards <br>HR Team</p>";
						body +="</div>";
						body +="</div>";
						body +=" <img src=\"cid:myimg\" /></body></html>";
						String subject ="RE:Payslip for "+monthyear;

						
						MimeBodyPart textPart = new MimeBodyPart();       
				        textPart.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");       
				        textPart.setContent(body, "text/html; charset=utf-8"); 
				         
				        MimeMultipart multipart = new MimeMultipart("mixed");     
				        multipart.addBodyPart(textPart);
				        multipart.addBodyPart(pdfBodyPart);
				        
				        
				        
					    SendEmail sendMail=new SendEmail();
						String mailResp=sendMail.sendTOMailPdfpayroll("info@u-clo.com",to,"","", subject, body, multipart);
					    
					}
					
					resultJson.put("STATUS", "OK");
					
			    }catch (Exception e) {
			    	
			    	resultJson.put("STATUS", "NOT OK");
			    	
					System.out.println(e);
				}
			    
			    resp.getWriter().write(resultJson.toString());
		}
		
		if(action.equalsIgnoreCase("TEST"))
		{
			 String hdrid= "12";
			 EmployeeDAO employeeDAO = new EmployeeDAO();
			 ByteArrayOutputStream out = new ByteArrayOutputStream();            
			    try {
			    	
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
		            pdfBodyPart.setFileName("Payslip.pdf");
		                         
		            String body="";
					body +="<div style='background: whitesmoke;width: 75%;padding: 1%;font-family:Calibri;'>";
					body +="<div>";
					body +="<img src='https://ordermgt.u-clo.com/track/GetCustomerLogoByPlantServlet?PLANT="+plant+"' style='width: 90px;'>";
					body +="</div>";
					body +="<div Style='background: #008d4c;height: 25px;width: 100%;border-radius: 5px;color: white;padding-top: 5px;padding-left: 5px;'>";
					body +="<span>Payslip for september - 2020</span>";
					body +="</div>";
					body +="<div class='box-header with-border' style='margin-top: 26px;'>";
					body +="<p>Hello,</p>";
					body +="<p>Please find the attached file.</p>";
					body +="</div>";
					body +="<div style='margin-top:30px'>";
					body +="<p>Regards <br>HR Team</p>";
					body +="</div>";
					body +="</div>";
					body +=" <img src=\"cid:myimg\" /></body></html>";
					String subject ="RE:Payslip for september - 2020";

					
					MimeBodyPart textPart = new MimeBodyPart();       
			        textPart.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");       
			        textPart.setContent(body, "text/html; charset=utf-8"); 
			         
			        MimeMultipart multipart = new MimeMultipart("mixed");     
			        multipart.addBodyPart(textPart);
			        multipart.addBodyPart(pdfBodyPart);
			        
				    SendEmail sendMail=new SendEmail();
					String mailResp=sendMail.sendTOMailPdfpayroll("info@u-clo.com","kumarankirshi@gmail.com","","", subject, body, multipart);
				    
			    }catch (Exception e) {
					System.out.println(e);
				}
		}
		
		if(action.equalsIgnoreCase("DELETE_DEDUCTION"))
		{
			String hdrid = StrUtils.fString(req.getParameter("hdrid"));
			
			
	        UserTransaction ut = null;
			try {
				
				ut = DbBean.getUserTranaction();				
				ut.begin();
				HrDeductionHdr HrDeductionDet = hrDeductionHdrDAO.getdeductionhdrById(plant, Integer.valueOf(hdrid));
				hrDeductionHdrDAO.Deletedeductionhdr(plant, Integer.valueOf(hdrid));
				
				List<HrDeductionDet> HrDeductionDetList = hrDeductionDetDAO.getAlldeductiondetbyhdrid(plant, Integer.valueOf(hdrid));
				for (HrDeductionDet hrDeductionDet : HrDeductionDetList) {
					hrDeductionDetDAO.Deletedeductiondet(plant, hrDeductionDet.getID());
				}
				
				JournalService journalService=new JournalEntry();
				Journal journalFrom=journalService.getJournalByTransactionId(plant, hdrid,"LOANORADVANCE");
				if(journalFrom.getJournalHeader()!=null)
				{
					if(journalFrom.getJournalHeader().getID()>0)
					{
						journalService.DeleteJournal(plant, journalFrom.getJournalHeader().getID());
						Hashtable jhtMovHis = new Hashtable();
						jhtMovHis.put(IDBConstants.PLANT, plant);
						jhtMovHis.put("DIRTYPE", TransactionConstants.DELETE_JOURNAL);	
						jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
						jhtMovHis.put(IDBConstants.ITEM, "");
						jhtMovHis.put(IDBConstants.QTY, "0.0");
						jhtMovHis.put("RECID", "");
						jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journalFrom.getJournalHeader().getTRANSACTION_TYPE()+" "+journalFrom.getJournalHeader().getTRANSACTION_ID());
						jhtMovHis.put(IDBConstants.CREATED_BY, username);		
						jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						jhtMovHis.put("REMARKS","");
						movHisDao.insertIntoMovHis(jhtMovHis);
					}
				}
				
				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.DELETE_PAYROLL_DEDUCTION_MASTER);	
				htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));													
				htMovHis.put("RECID", "");
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				htMovHis.put("REMARKS",HrDeductionDet.getEMPID()+","+HrDeductionDet.getDEDUCTION_NAME()+","+HrDeductionDet.getDEDUCTION_AMOUNT()+","+HrDeductionDet.getDEDUCTION_DUE());
				movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
				
				DbBean.CommitTran(ut);
				String result = "Payroll Deduction deleted successfully.";
				resp.sendRedirect("../payroll/deduction?result="+ result);
				
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				String result = "Couldn't delete Payroll Deduction";
				resp.sendRedirect("../payroll/editdeduction?ID="+hdrid+"&result="+ result);
				
			}
		
		}
		
	}
	
	private JSONObject generateparrollno(String plant, String userId) {
		JSONObject resultJson = new JSONObject();
		try {
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			
			String payrollno = _TblControlDAO.getNextOrder(plant,userId,"PAYROLL");

			if (payrollno.length() > 0) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("payroll", payrollno);
				resultJson.put("result", resultObjectJson);
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	
	
	
	public float gettotalnoofdays(String dateBeforeString,String dateAfterString) {
		SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
		float daysBetween=0;
		try {
			Date dateBefore = myFormat.parse(dateBeforeString);
			Date dateAfter = myFormat.parse(dateAfterString);
			long difference = dateAfter.getTime() - dateBefore.getTime();
			daysBetween = (difference / (1000*60*60*24));
			daysBetween = daysBetween + 1;
			return daysBetween;
		} catch (Exception e) {
			e.printStackTrace();
			return daysBetween;
		}
	}

	public double getlosofpaydays(String plant,String fromdate, String todate,int empid) throws Exception {
		HrLeaveApplyDetDAO hrLeaveApplyDetDAO = new HrLeaveApplyDetDAO();
		HrLeaveApplyHdrDAO hrLeaveApplyHdrDAO = new HrLeaveApplyHdrDAO();
		EmployeeLeaveDetDAO employeeLeaveDetDAO = new EmployeeLeaveDetDAO();
		HrLeaveTypeDAO hrLeaveTypeDAO = new HrLeaveTypeDAO();
		double lopdays = 0;
		try {
			List<HrLeaveApplyDet> leavedetlist = hrLeaveApplyDetDAO.getHrLeaveApplyDetbyEmpidAndDateApproved(plant, empid, fromdate, todate);
			for(HrLeaveApplyDet hrLeaveApplyDet:leavedetlist) {
				HrLeaveApplyHdr hrLeaveApplyHdr = hrLeaveApplyHdrDAO.getHrLeaveApplyHdrById(plant, hrLeaveApplyDet.getLEAVEHDRID());
				EmployeeLeaveDET employeeLeaveDET = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, hrLeaveApplyHdr.getLEAVETYPEID());
				HrLeaveType hrLeaveType = hrLeaveTypeDAO.getLeavetypeById(plant, employeeLeaveDET.getLEAVETYPEID());
				if(hrLeaveType.getISNOPAYLEAVE() == 1) {
					if(hrLeaveApplyDet.getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")) {
						lopdays = lopdays +1;
					}
					
					if(hrLeaveApplyDet.getPREPOSTLUNCHTYPE().equalsIgnoreCase("halfday")) {
						lopdays = lopdays +0.5;
					}
				}
			}
			
			return lopdays;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return lopdays;
		}
	}
	
	public List<MonthYearPojo> getmonthyear(String fm,String fy,String tm,String ty) throws ParseException{
		DateUtils dateUtils = new DateUtils();
		String fromdate = "01/"+fm+"/"+fy;
		String todate = "01/"+tm+"/"+ty;
		int tom = Integer.valueOf(tm);
		int toy = Integer.valueOf(ty);
		int fom = 0;
		int foy = 0;
		String tdate ="0";
		
		int i = 0;
		List<MonthYearPojo> monthyearlist = new ArrayList<MonthYearPojo>();
		do {
			
			if(i != 0) {
				
				i++;
				fromdate = dateUtils.addMonth(fromdate, 1);
				String[] splitdate = fromdate.split("/");
				String cyear = splitdate[2];
				String cmonth = splitdate[1];
				MonthYearPojo monthYearPojo = new MonthYearPojo();
				fom = Integer.valueOf(cmonth);
				foy = Integer.valueOf(cyear);
				tdate ="01/"+fom+"/"+foy;
				monthYearPojo.setMONTH(String.valueOf(fom));
				monthYearPojo.setYEAR(cyear);
				monthyearlist.add(monthYearPojo);
			}else {
				i++;
				String[] splitdate = fromdate.split("/");
				String cyear = splitdate[2];
				String cmonth = splitdate[1];
				MonthYearPojo monthYearPojo = new MonthYearPojo();
				fom = Integer.valueOf(cmonth);
				foy = Integer.valueOf(cyear);
				tdate ="01/"+fom+"/"+foy;
				monthYearPojo.setMONTH(String.valueOf(fom));
				monthYearPojo.setYEAR(cyear);
				monthyearlist.add(monthYearPojo);
			}
			
		}while(!tdate.equalsIgnoreCase(todate));
		
		return monthyearlist;
	}
	
	private void addPDFbody(Document doc, String hdrid,String plant) throws DocumentException, IOException {
		
		HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
		HrPayrollHDRService hrPayrollHDRService = new HrPayrollHDRServiceImpl();
		HrPayrollDETService HrPayrollDETService = new HrPayrollDETServiceImpl();
		PlantMstDAO  plantMstDAO = new PlantMstDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
		HrDeductionHdrDAO hrDeductionHdrDAO = new HrDeductionHdrDAO();
		HrDeductionDetDAO hrDeductionDetDAO = new HrDeductionDetDAO();
		HrPayrollDETDAO hrPayrollDETDAO = new HrPayrollDETDAO();
		HrPayrollPaymentDAO hrPayrollPaymentDAO = new HrPayrollPaymentDAO();
		
		try {
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	
	    	userBean ubean = new userBean();
	    	String companyname = ubean.getCompanyName(plant).toUpperCase();
	    	
	    	HrPayrollHDR hrpayrollhdr= hrPayrollHDRService.getpayrollhdrById(plant, Integer.valueOf(hdrid));
			ArrayList arrEmp = employeeDAO.getEmployeeListbyid(String.valueOf(hrpayrollhdr.getEMPID()),plant);
			Map employee=(Map)arrEmp.get(0);
			
			String empcode = (String) employee.get("EMPNO");
			String empname = (String) employee.get("FNAME");
			String department = (String) employee.get("DEPT");
			String designation = (String) employee.get("DESGINATION");
			String passportno = (String) employee.get("PASSPORTNUMBER");
			String doj = (String) employee.get("DATEOFJOINING");
			String lcardno = (String) employee.get("LABOURCARDNUMBER");
			String bank = (String) employee.get("BANKNAME");
			String payperiod = hrpayrollhdr.getFROMDATE() +" to "+ hrpayrollhdr.getTODATE();
			String dateofpay = hrpayrollhdr.getPAYMENT_DATE();
			
			List<HrPayrollDET> allowancelist = hrPayrollDETDAO.getsalaryallowance(plant, Integer.valueOf(hdrid), hrpayrollhdr.getEMPID());
			List<HrPayrollDET> additionlist = hrPayrollDETDAO.getsalaryaddition(plant, Integer.valueOf(hdrid), hrpayrollhdr.getEMPID());
			List<HrPayrollDET> deductionlist = hrPayrollDETDAO.getsalarydeduction(plant, Integer.valueOf(hdrid), hrpayrollhdr.getEMPID(),hrpayrollhdr.getMONTH(),hrpayrollhdr.getYEAR());

			int payhdrid = hrPayrollPaymentDAO.getHrPayrollPaymendetbypayId(plant, hrpayrollhdr.getID());
			String paymode = "-";
			
			if(payhdrid != 0){
				HrPayrollPaymentHdr pphdr = hrPayrollPaymentDAO.getHrPayrollPaymentHdrId(plant, payhdrid);
				paymode = pphdr.getPAYMENT_MODE();
			}
			
			
			double allowamt = 0;
			for(HrPayrollDET salaryallow:allowancelist){
				allowamt = allowamt + salaryallow.getAMOUNT();
			}
			
			double netamount = allowamt;
			for(HrPayrollDET addlist:additionlist) {
				netamount = netamount + addlist.getAMOUNT();
			}
			
			for(HrPayrollDET deductlist:deductionlist) {
				netamount = netamount - deductlist.getAMOUNT();
			}
			String sealname ="";
			String signname ="";
			PlantMstUtil plantmstutil = new PlantMstUtil();
			List viewlistQry = plantmstutil.getPlantMstDetails(plant);
            for (int i = 0; i < viewlistQry.size(); i++) {
                Map map = (Map) viewlistQry.get(i);
                sealname=StrUtils.fString((String)map.get("SEALNAME"));
                signname=StrUtils.fString((String)map.get("SIGNATURENAME"));         
            }
	        
	
	        
	        Paragraph preface = new Paragraph(new Chunk("PAYSLIP", new Font(Font.HELVETICA, 13, Font.BOLD, Color.BLACK))); 
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
			
			Paragraph row9a = new Paragraph();
			row9a.add(new Chunk("Pay Period", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row9a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c9a = new PdfPCell();
			c9a.addElement(row9a);
			c9a.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row9b = new Paragraph();
			row9b.add(new Chunk(payperiod, new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row9b.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c9c = new PdfPCell();
			c9c.addElement(row9b);
			c9c.setBorder(Rectangle.UNDEFINED);
			
			PdfPCell c9b = new PdfPCell();
			c9b.addElement(row1c);
			c9b.setBorder(Rectangle.UNDEFINED);
			
			tablebody1.addCell(c9a);
			tablebody1.addCell(c9b);
			tablebody1.addCell(c9c);
			
			Paragraph row10a = new Paragraph();
			row10a.add(new Chunk("Date Of Payment", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			row10a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c10a = new PdfPCell();
			c10a.addElement(row10a);
			c10a.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row10b = new Paragraph();
			row10b.add(new Chunk(dateofpay, new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
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
			row10bpt.add(new Chunk(paymode, new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
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
			
			
			PdfPTable tablebody2 = new PdfPTable(2);
			tablebody2.setWidthPercentage(100);
			tablebody2.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablebody2.setSpacingBefore(20f);
			
			Paragraph row13a = new Paragraph();
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
			tablebody2.addCell(c14b);
	        
			for (HrPayrollDET hrPayrollDET : allowancelist) {
			
				Paragraph row11a = new Paragraph();
				row11a.add(new Chunk(hrPayrollDET.getSALARYTYPE(), new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
				row11a.setAlignment(Element.ALIGN_LEFT);
				
				PdfPCell c11a = new PdfPCell();
				c11a.addElement(row11a);
				c11a.setBorderWidth(1);
				c11a.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c11a.setBorder(Rectangle.BOTTOM);
				c11a.setBorderColor(Color.LIGHT_GRAY);
				c11a.setMinimumHeight(25f);
	
				Paragraph row12b = new Paragraph();
				row12b.add(new Chunk(StrUtils.addZeroes(hrPayrollDET.getAMOUNT(), numberOfDecimal), new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
				row12b.setAlignment(Element.ALIGN_RIGHT);
				
				PdfPCell c12b = new PdfPCell();
				c12b.addElement(row12b);
				c12b.setBorderWidth(1);
				c12b.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c12b.setBorder(Rectangle.BOTTOM);
				c12b.setBorderColor(Color.LIGHT_GRAY);
				c12b.setMinimumHeight(25f);
	
				tablebody2.addCell(c11a);
				tablebody2.addCell(c12b);
		        
			}
			
			Paragraph row11a = new Paragraph();
			row11a.add(new Chunk("Gross Salary", new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK)));
			row11a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c11a = new PdfPCell();
			c11a.addElement(row11a);
			c11a.setBorderWidth(1);
			c11a.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c11a.setBorder(Rectangle.BOTTOM);
			c11a.setBorderColor(Color.LIGHT_GRAY);
			c11a.setMinimumHeight(25f);
	
			Paragraph row12b = new Paragraph();
			row12b.add(new Chunk(StrUtils.addZeroes(allowamt, numberOfDecimal), new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK)));
			row12b.setAlignment(Element.ALIGN_RIGHT);
			
			PdfPCell c12b = new PdfPCell();
			c12b.addElement(row12b);
			c12b.setBorderWidth(1);
			c12b.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c12b.setBorder(Rectangle.BOTTOM);
			c12b.setBorderColor(Color.LIGHT_GRAY);
			c12b.setMinimumHeight(25f);
	
			tablebody2.addCell(c11a);
			tablebody2.addCell(c12b);
	        
			doc.add(tablebody2);
	
			if(additionlist.size() > 0) {
			
				Paragraph p1 = new Paragraph();
				p1.add(new Chunk("Additions", new Font(Font.HELVETICA, 13, Font.BOLD, Color.BLACK)));
				p1.setAlignment(Element.ALIGN_LEFT);
				p1.setSpacingBefore(10f);
	
				doc.add(p1);
				
				PdfPTable tablebody3 = new PdfPTable(2);
				
				//int[] body3data = { 45, 10, 120 };
				//tablebody3.setWidths(body3data);
				tablebody3.setWidthPercentage(100);
				tablebody3.setHorizontalAlignment(Element.ALIGN_LEFT);
				//tablebody3.setSpacingBefore(10f);
				
				
				for (HrPayrollDET hrPayrollDET : additionlist) {
					Paragraph row15a = new Paragraph();
					row15a.add(new Chunk(hrPayrollDET.getSALARYTYPE(), new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
					row15a.setAlignment(Element.ALIGN_LEFT);
					
					PdfPCell c15a = new PdfPCell();
					c15a.addElement(row15a);
					c15a.setBorder(Rectangle.UNDEFINED);
					
					Paragraph row15b = new Paragraph();
					row15b.add(new Chunk(StrUtils.addZeroes(hrPayrollDET.getAMOUNT(), numberOfDecimal), new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
					row15b.setAlignment(Element.ALIGN_RIGHT);
					
					PdfPCell c15c = new PdfPCell();
					c15c.addElement(row15b);
					c15c.setBorder(Rectangle.UNDEFINED);
					
					/*PdfPCell c15b = new PdfPCell();
					c15b.addElement(row1c);
					c15b.setBorder(Rectangle.UNDEFINED);*/
				
				
					tablebody3.addCell(c15a);
					//tablebody3.addCell(c15b);
					tablebody3.addCell(c15c);
				}
				
				doc.add(tablebody3);
			}
			
			if(deductionlist.size() > 0) {
				Paragraph p2 = new Paragraph();
				p2.add(new Chunk("Deductions", new Font(Font.HELVETICA, 13, Font.BOLD, Color.BLACK)));
				p2.setAlignment(Element.ALIGN_LEFT);
				p2.setSpacingBefore(10f);
				
				doc.add(p2);
				
				PdfPTable tablebody4 = new PdfPTable(2);
				
				//int[] body4data = { 45, 10, 120 };
				//tablebody4.setWidths(body4data);
				tablebody4.setWidthPercentage(100);
				tablebody4.setHorizontalAlignment(Element.ALIGN_LEFT);
				//tablebody4.setSpacingBefore(10f);
				
				for (HrPayrollDET hrPayrollDET :deductionlist) {
				
					Paragraph row16a = new Paragraph();
					row16a.add(new Chunk(hrPayrollDET.getSALARYTYPE(), new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
					row16a.setAlignment(Element.ALIGN_LEFT);
					
					PdfPCell c16a = new PdfPCell();
					c16a.addElement(row16a);
					c16a.setBorder(Rectangle.UNDEFINED);
					
					Paragraph row16b = new Paragraph();
					row16b.add(new Chunk(StrUtils.addZeroes(hrPayrollDET.getAMOUNT(), numberOfDecimal), new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
					row16b.setAlignment(Element.ALIGN_RIGHT);
					
					PdfPCell c16c = new PdfPCell();
					c16c.addElement(row16b);
					c16c.setBorder(Rectangle.UNDEFINED);
					
				/*
				 * PdfPCell c16b = new PdfPCell(); c16b.addElement(row1c);
				 */
					//c16b.setBorder(Rectangle.UNDEFINED);
					
					tablebody4.addCell(c16a);
					//tablebody4.addCell(c16b);
					tablebody4.addCell(c16c);
				}
				
				doc.add(tablebody4);
			}
	
			PdfPTable tablebody5 = new PdfPTable(2);
			
			//int[] body5data = { 45, 10, 120 };
			//tablebody5.setWidths(body5data);
			tablebody5.setWidthPercentage(100);
			tablebody5.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablebody5.setSpacingBefore(10f);
			
			Paragraph row17a = new Paragraph();
			row17a.add(new Chunk("Net Salary", new Font(Font.HELVETICA, 13, Font.BOLD, Color.BLACK)));
			row17a.setAlignment(Element.ALIGN_LEFT);
			
			PdfPCell c17a = new PdfPCell();
			c17a.addElement(row17a);
			c17a.setBorder(Rectangle.UNDEFINED);
			
			Paragraph row17b = new Paragraph();
			row17b.add(new Chunk(StrUtils.addZeroes(netamount, numberOfDecimal), new Font(Font.HELVETICA, 13, Font.BOLD, Color.BLACK)));
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
			
			PdfPTable tablefooter = new PdfPTable(3);
			
			int[] footerdata = { 30, 100, 30 }; // 23, 20, 25, 32 };
			tablefooter.setWidths(footerdata);
			tablefooter.setWidthPercentage(100);
			tablefooter.setHorizontalAlignment(Element.ALIGN_CENTER);
			tablefooter.setSpacingBefore(10f);
	        
	        String sealurl = MLoggerConstant.PROPS_FOLDER + "/track/Seals/"+sealname;
	        Image seal = Image.getInstance(sealurl);
	        seal.scaleAbsolute(60, 60);
	        
	        String signurl = MLoggerConstant.PROPS_FOLDER + "/track/Signatures/"+signname;
	        Image sign = Image.getInstance(signurl);
	        sign.scaleAbsolute(60, 60);
	
			Paragraph fmiddle = new Paragraph();
			fmiddle.add(new Chunk("", new Font(Font.HELVETICA, 13, Font.BOLD, Color.BLACK)));
			fmiddle.setAlignment(Element.ALIGN_CENTER);
	
			PdfPCell fc1 = new PdfPCell();
			fc1.addElement(seal);
			fc1.setHorizontalAlignment(Element.ALIGN_CENTER);
			fc1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			fc1.setBorder(Rectangle.UNDEFINED);
			//fc1.setBackgroundColor(Color.gray);
			
			PdfPCell fc2 = new PdfPCell();
			//fc2.setPaddingTop(5);
			//fc2.setPaddingRight(60);
			fc2.addElement(fmiddle);
			fc2.setBorder(Rectangle.UNDEFINED);
			
			
			
			PdfPTable tablefin = new PdfPTable(1);
			
			int[] footerin = {30}; // 23, 20, 25, 32 };
			tablefin.setWidths(footerin);
			tablefin.setWidthPercentage(100);
			tablefin.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			PdfPCell fin1 = new PdfPCell();
			fin1.addElement(sign);
			fin1.setHorizontalAlignment(Element.ALIGN_CENTER);
			fin1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			fin1.setBorder(Rectangle.UNDEFINED);
			fin1.setMinimumHeight(60f);
			//fin1.setBackgroundColor(Color.green);
			
			Paragraph pfin1 = new Paragraph();
			pfin1.add(new Chunk("Signature", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)));
			pfin1.setAlignment(Element.ALIGN_CENTER);
			
			PdfPCell fin2 = new PdfPCell();
			fin2.addElement(pfin1);
			fin2.setHorizontalAlignment(Element.ALIGN_CENTER);
			fin2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			fin2.setBorder(Rectangle.UNDEFINED);
			//fin2.setBackgroundColor(Color.blue);
			
			tablefin.addCell(fin1);
			tablefin.addCell(fin2);
			
	
			PdfPCell fc3 = new PdfPCell();
			fc3.addElement(tablefin);
			//fc3.setHorizontalAlignment(Element.ALIGN_CENTER);
			//fc3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			fc3.setBorder(Rectangle.UNDEFINED);
			
			
			tablefooter.addCell(fc1);
			tablefooter.addCell(fc2);
			tablefooter.addCell(fc3);
			
			doc.add(tablefooter);
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	

	private JSONObject getTotalNetSalary(HttpServletRequest request) {
		
		HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
		HrPayrollHDRService hrPayrollHDRService = new HrPayrollHDRServiceImpl();
		HrPayrollDETService HrPayrollDETService = new HrPayrollDETServiceImpl();
		PlantMstDAO  plantMstDAO = new PlantMstDAO();
		DateUtils dateutils = new DateUtils();
		HrDeductionHdrDAO hrDeductionHdrDAO = new HrDeductionHdrDAO();
		HrDeductionDetDAO hrDeductionDetDAO = new HrDeductionDetDAO();
		HrPayrollDETDAO hrPayrollDETDAO = new HrPayrollDETDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
	    HrEmpSalaryDAO hrEmpSalaryDAO = new HrEmpSalaryDAO();
	    HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		 double payrolladdition = 0;
		 double payrolldeduction = 0;
		 double gratuityamount = 0;
		 double totalsalary = 0;
		 double netsalary = 0;
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			
			List<HrPayrollHDR> hrpayrollhdrlist = new ArrayList<HrPayrollHDR>();
			List<MonthYearPojo> monthyyearlist = new ArrayList<MonthYearPojo>();
			String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
			
			String[] fromDatesplit = fromDate.split("/");
			String[] toDatesplit = toDate.split("/");
			
			String fm = fromDatesplit[1];
			String fy = fromDatesplit[2];
			String tm = toDatesplit[1];
			String ty = toDatesplit[2];
			
			monthyyearlist = getmonthyeartile(fm, fy, tm, ty);

			for (MonthYearPojo monthYearPojo : monthyyearlist) {
				List<HrPayrollHDR> hrpayrollhdrmylist = hrPayrollHDRService.payrollhdrbymonthyear(plant, monthYearPojo.getMONTH(), monthYearPojo.getYEAR());
				hrpayrollhdrlist.addAll(hrpayrollhdrmylist);
			}
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			
			JSONArray payrollarray = new JSONArray();
			
			for (HrPayrollHDR hrpayroll:hrpayrollhdrlist) {

				 
				 ArrayList arrEmp = employeeDAO.getEmployeeListbyid(String.valueOf(hrpayroll.getEMPID()),plant);
				 Map employee=(Map)arrEmp.get(0);
				 String attdays = String.valueOf(hrpayroll.getPAYDAYS());
				 String paymonth = months[Integer.valueOf(hrpayroll.getMONTH())-1]+"-"+hrpayroll.getYEAR();
				 
				
				 
				 List<HrEmpSalaryMst> empsalarymstlist = hrEmpSalaryDAO.getAllSalary(plant);
				 List<HrPayrollDET> hrPayrollDETlist = HrPayrollDETService.getpayrolldetByhdrid(plant, hrpayroll.getID());
				 for (HrEmpSalaryMst empsalarymst:empsalarymstlist) {
					 boolean ithasnoallowance = true;
					 for (HrPayrollDET hrPayrollDET : hrPayrollDETlist) {
						if(empsalarymst.getSALARYTYPE().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())) {
							totalsalary = totalsalary + hrPayrollDET.getAMOUNT();
							ithasnoallowance = false;
						}
					 }
					  
				 }
				 
				 
				 List<HrPayrollAdditionMst> payaddmstadd = hrPayrollAdditionMstDAO.payrolladditionmstadd(plant, "");
				 for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){
					 for(HrPayrollAdditionMst hrPayrollAdditionMst:payaddmstadd){
						 if(hrPayrollAdditionMst.getADDITION_NAME().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())){
							 payrolladdition = payrolladdition + hrPayrollDET.getAMOUNT();
						 }
					 }
				 }
				 List<HrDeductionHdr> hrDeductionHdrlist =  hrDeductionHdrDAO.getdeductionhdrempmonthyear(plant,String.valueOf(hrpayroll.getEMPID()), hrpayroll.getMONTH(), hrpayroll.getYEAR());
				 for (HrDeductionHdr hrDeductionHdr : hrDeductionHdrlist) {
					 payrolladdition = payrolladdition + hrDeductionHdr.getDEDUCTION_AMOUNT();
					 if(hrDeductionHdr.getISGRATUITY() == 1) {
						 gratuityamount = gratuityamount + hrDeductionHdr.getDEDUCTION_AMOUNT(); 
					 }
				 }
				 
				 List<HrPayrollAdditionMst> payaddmstdeduct = hrPayrollAdditionMstDAO.payrolladditionmstdeduct(plant, "");
				 for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){
					 for(HrPayrollAdditionMst hrPayrollAdditionMst:payaddmstdeduct){
						 if(hrPayrollAdditionMst.getADDITION_NAME().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())){
							 payrolldeduction = payrolldeduction + hrPayrollDET.getAMOUNT();
						 }
					 }
				  }
				 
				 netsalary = (totalsalary + payrolladdition) - payrolldeduction;
			
				 
			}


			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("NET_SALARY", netsalary);
			resultJson.put("errors", jsonArrayErr);
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("NET_SALARY", "");
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalGrossSalary(HttpServletRequest request) {
		
		HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
		HrPayrollHDRService hrPayrollHDRService = new HrPayrollHDRServiceImpl();
		HrPayrollDETService HrPayrollDETService = new HrPayrollDETServiceImpl();
		PlantMstDAO  plantMstDAO = new PlantMstDAO();
		DateUtils dateutils = new DateUtils();
		HrDeductionHdrDAO hrDeductionHdrDAO = new HrDeductionHdrDAO();
		HrDeductionDetDAO hrDeductionDetDAO = new HrDeductionDetDAO();
		HrPayrollDETDAO hrPayrollDETDAO = new HrPayrollDETDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
	    HrEmpSalaryDAO hrEmpSalaryDAO = new HrEmpSalaryDAO();
	    HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		 double payrolladdition = 0;
		 double payrolldeduction = 0;
		 double gratuityamount = 0;
		 double totalsalary = 0;
		 double netsalary = 0;
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			
			List<HrPayrollHDR> hrpayrollhdrlist = new ArrayList<HrPayrollHDR>();
			List<MonthYearPojo> monthyyearlist = new ArrayList<MonthYearPojo>();
			String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
			
			String[] fromDatesplit = fromDate.split("/");
			String[] toDatesplit = toDate.split("/");
			
			String fm = fromDatesplit[1];
			String fy = fromDatesplit[2];
			String tm = toDatesplit[1];
			String ty = toDatesplit[2];
			
			monthyyearlist = getmonthyeartile(fm, fy, tm, ty);

			for (MonthYearPojo monthYearPojo : monthyyearlist) {
				List<HrPayrollHDR> hrpayrollhdrmylist = hrPayrollHDRService.payrollhdrbymonthyear(plant, monthYearPojo.getMONTH(), monthYearPojo.getYEAR());
				hrpayrollhdrlist.addAll(hrpayrollhdrmylist);
			}
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			
			JSONArray payrollarray = new JSONArray();
			
			for (HrPayrollHDR hrpayroll:hrpayrollhdrlist) {
				 
				 ArrayList arrEmp = employeeDAO.getEmployeeListbyid(String.valueOf(hrpayroll.getEMPID()),plant);
				 Map employee=(Map)arrEmp.get(0);
				 String attdays = String.valueOf(hrpayroll.getPAYDAYS());
				 String paymonth = months[Integer.valueOf(hrpayroll.getMONTH())-1]+"-"+hrpayroll.getYEAR();

				
				 
				 List<HrEmpSalaryMst> empsalarymstlist = hrEmpSalaryDAO.getAllSalary(plant);
				 List<HrPayrollDET> hrPayrollDETlist = HrPayrollDETService.getpayrolldetByhdrid(plant, hrpayroll.getID());
				 for (HrEmpSalaryMst empsalarymst:empsalarymstlist) {
					 boolean ithasnoallowance = true;
					 for (HrPayrollDET hrPayrollDET : hrPayrollDETlist) {
						if(empsalarymst.getSALARYTYPE().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())) {
							totalsalary = totalsalary + hrPayrollDET.getAMOUNT();
							ithasnoallowance = false;
						}
					 }
				 }
				 
				 List<HrPayrollAdditionMst> payaddmstadd = hrPayrollAdditionMstDAO.payrolladditionmstadd(plant, "");
				 for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){
					 for(HrPayrollAdditionMst hrPayrollAdditionMst:payaddmstadd){
						 if(hrPayrollAdditionMst.getADDITION_NAME().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())){
							 payrolladdition = payrolladdition + hrPayrollDET.getAMOUNT();
						 }
					 }
				 }
				 List<HrDeductionHdr> hrDeductionHdrlist =  hrDeductionHdrDAO.getdeductionhdrempmonthyear(plant,String.valueOf(hrpayroll.getEMPID()), hrpayroll.getMONTH(), hrpayroll.getYEAR());
				 for (HrDeductionHdr hrDeductionHdr : hrDeductionHdrlist) {
					 payrolladdition = payrolladdition + hrDeductionHdr.getDEDUCTION_AMOUNT();
					 if(hrDeductionHdr.getISGRATUITY() == 1) {
						 gratuityamount = gratuityamount + hrDeductionHdr.getDEDUCTION_AMOUNT(); 
					 }
				 }
				 
				 List<HrPayrollAdditionMst> payaddmstdeduct = hrPayrollAdditionMstDAO.payrolladditionmstdeduct(plant, "");
				 for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){
					 for(HrPayrollAdditionMst hrPayrollAdditionMst:payaddmstdeduct){
						 if(hrPayrollAdditionMst.getADDITION_NAME().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())){
							 payrolldeduction = payrolldeduction + hrPayrollDET.getAMOUNT();
						 }
					 }
				  }
				 

				 netsalary = netsalary + (totalsalary + payrolladdition);
				 
				
			}


			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("GROSS_SALARY", totalsalary);
			resultJson.put("errors", jsonArrayErr);
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("GROSS_SALARY", "");
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	public List<MonthYearPojo> getmonthyeartile(String fm,String fy,String tm,String ty) throws ParseException{
		DateUtils dateUtils = new DateUtils();
		String fromdate = "01/"+fm+"/"+fy;
		String todate = "01/"+tm+"/"+ty;
		int tom = Integer.valueOf(tm);
		int toy = Integer.valueOf(ty);
		int fom = 0;
		int foy = 0;
		String tdate ="0";
		
		int i = 0;
		List<MonthYearPojo> monthyearlist = new ArrayList<MonthYearPojo>();
		do {
			
			if(i != 0) {
				
				i++;
				fromdate = dateUtils.addMonth(fromdate, 1);
				String[] splitdate = fromdate.split("/");
				String cyear = splitdate[2];
				String cmonth = splitdate[1];
				MonthYearPojo monthYearPojo = new MonthYearPojo();
				fom = Integer.valueOf(cmonth);
				foy = Integer.valueOf(cyear);
				if(cmonth.length() == 1) {
					cmonth = "0"+cmonth;
				}
				tdate ="01/"+cmonth+"/"+foy;
				monthYearPojo.setMONTH(String.valueOf(fom));
				monthYearPojo.setYEAR(cyear);
				monthyearlist.add(monthYearPojo);
			}else {
				i++;
				String[] splitdate = fromdate.split("/");
				String cyear = splitdate[2];
				String cmonth = splitdate[1];
				MonthYearPojo monthYearPojo = new MonthYearPojo();
				fom = Integer.valueOf(cmonth);
				foy = Integer.valueOf(cyear);
				if(cmonth.length() == 1) {
					cmonth = "0"+cmonth;
				}
				tdate ="01/"+cmonth+"/"+foy;
				monthYearPojo.setMONTH(String.valueOf(fom));
				monthYearPojo.setYEAR(cyear);
				monthyearlist.add(monthYearPojo);
			}
			
		}while(!tdate.equalsIgnoreCase(todate));
		
		return monthyearlist;
	}
	
	
	private JSONObject getTotalNetGrossSalary(HttpServletRequest request) {
		
		HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
		HrPayrollHDRService hrPayrollHDRService = new HrPayrollHDRServiceImpl();
		HrPayrollDETService HrPayrollDETService = new HrPayrollDETServiceImpl();
		PlantMstDAO  plantMstDAO = new PlantMstDAO();
		DateUtils dateutils = new DateUtils();
		HrDeductionHdrDAO hrDeductionHdrDAO = new HrDeductionHdrDAO();
		HrDeductionDetDAO hrDeductionDetDAO = new HrDeductionDetDAO();
		HrPayrollDETDAO hrPayrollDETDAO = new HrPayrollDETDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
	    HrEmpSalaryDAO hrEmpSalaryDAO = new HrEmpSalaryDAO();
	    HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		 
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			
			List<HrPayrollHDR> hrpayrollhdrlist = new ArrayList<HrPayrollHDR>();
			List<MonthYearPojo> monthyyearlist = new ArrayList<MonthYearPojo>();
			String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
			
			String[] fromDatesplit = fromDate.split("/");
			String[] toDatesplit = toDate.split("/");
			
			String fm = fromDatesplit[1];
			String fy = fromDatesplit[2];
			String tm = toDatesplit[1];
			String ty = toDatesplit[2];
			
			monthyyearlist = getmonthyeartile(fm, fy, tm, ty);
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			
			JSONArray payrollarray = new JSONArray();
			
			String lable = "[ 0,";
			String netsal = "[ 0,";
			String grosssal = "[ 0,";
			String deduct = "[ 0,";
			int mmm = 0;
			int mm = 0;
			for (MonthYearPojo monthYearPojo : monthyyearlist) {
				List<HrPayrollHDR> hrpayrollhdrmylist = hrPayrollHDRService.payrollhdrbymonthyear(plant, monthYearPojo.getMONTH(), monthYearPojo.getYEAR());
				if(hrpayrollhdrmylist.isEmpty()) {
					HrPayrollHDR hrPayrollHDR = new HrPayrollHDR();
					hrPayrollHDR.setID(0);
					//hrpayrollhdrlist.add(hrPayrollHDR);
					
					if(mmm == 0) {
					 	netsal += 0;
						grosssal += 0;
						deduct += 0;
					}else {
						netsal += ","+0;
						grosssal += ","+0;
						deduct += ","+0;
					}
				mmm = mmm+1;
				}else {
					//hrpayrollhdrlist.addAll(hrpayrollhdrmylist);
					
					double nettsal = 0;
					double grosstsal = 0;
					double detsal = 0;
					
					for (HrPayrollHDR hrpayroll:hrpayrollhdrmylist) {
						
						 double payrolladdition = 0;
						 double payrolldeduction = 0;
						 double gratuityamount = 0;
						 double totalsalary = 0;
						 double netsalary = 0;

						if(hrpayroll.getID() != 0) { 
							 ArrayList arrEmp = employeeDAO.getEmployeeListbyid(String.valueOf(hrpayroll.getEMPID()),plant);
							 Map employee=(Map)arrEmp.get(0);
							 String attdays = String.valueOf(hrpayroll.getPAYDAYS());
							 String paymonth = months[Integer.valueOf(hrpayroll.getMONTH())-1]+"-"+hrpayroll.getYEAR();
							 
							
							 
							 List<HrEmpSalaryMst> empsalarymstlist = hrEmpSalaryDAO.getAllSalary(plant);
							 List<HrPayrollDET> hrPayrollDETlist = HrPayrollDETService.getpayrolldetByhdrid(plant, hrpayroll.getID());
							 for (HrEmpSalaryMst empsalarymst:empsalarymstlist) {
								 boolean ithasnoallowance = true;
								 for (HrPayrollDET hrPayrollDET : hrPayrollDETlist) {
									if(empsalarymst.getSALARYTYPE().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())) {
										totalsalary = totalsalary + hrPayrollDET.getAMOUNT();
										ithasnoallowance = false;
									}
								 }
								  
							 }
							 
							 
							 List<HrPayrollAdditionMst> payaddmstadd = hrPayrollAdditionMstDAO.payrolladditionmstadd(plant, "");
							 for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){
								 for(HrPayrollAdditionMst hrPayrollAdditionMst:payaddmstadd){
									 if(hrPayrollAdditionMst.getADDITION_NAME().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())){
										 payrolladdition = payrolladdition + hrPayrollDET.getAMOUNT();
									 }
								 }
							 }
							 List<HrDeductionHdr> hrDeductionHdrlist =  hrDeductionHdrDAO.getdeductionhdrempmonthyear(plant,String.valueOf(hrpayroll.getEMPID()), hrpayroll.getMONTH(), hrpayroll.getYEAR());
							 for (HrDeductionHdr hrDeductionHdr : hrDeductionHdrlist) {
								 payrolladdition = payrolladdition + hrDeductionHdr.getDEDUCTION_AMOUNT();
								 if(hrDeductionHdr.getISGRATUITY() == 1) {
									 gratuityamount = gratuityamount + hrDeductionHdr.getDEDUCTION_AMOUNT(); 
								 }
							 }
							 
							 List<HrPayrollAdditionMst> payaddmstdeduct = hrPayrollAdditionMstDAO.payrolladditionmstdeduct(plant, "");
							 for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){
								 for(HrPayrollAdditionMst hrPayrollAdditionMst:payaddmstdeduct){
									 if(hrPayrollAdditionMst.getADDITION_NAME().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())){
										 payrolldeduction = payrolldeduction + hrPayrollDET.getAMOUNT();
									 }
								 }
							  }
						}
						 netsalary = (totalsalary + payrolladdition) - payrolldeduction;
						 
						 nettsal = nettsal + netsalary;
						 grosstsal = grosstsal + totalsalary;
						 detsal = detsal + payrolldeduction;
					}
					
					
					
					if(mmm == 0) {
					 	netsal += nettsal;
						grosssal += grosstsal;
						deduct += detsal;
					}else {
						netsal += ","+nettsal;
						grosssal += ","+grosstsal;
						deduct += ","+detsal;
					}
				mmm = mmm+1;

					
				}
				
				if(mm == 0) {
					lable += '"'+months[Integer.valueOf(monthYearPojo.getMONTH())-1]+"-"+monthYearPojo.getYEAR()+'"';
				}else {
					lable += ',';
					lable += '"'+months[Integer.valueOf(monthYearPojo.getMONTH())-1]+"-"+monthYearPojo.getYEAR()+'"';
				}
				mm = mm+1;
			}
			 lable += "]";
			 netsal += "]";
				grosssal += "]";
				deduct += "]";
		
			
			
			
			
			String bcurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();

			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("LABLE", lable);
			resultJson.put("NETSAL", netsal);
			resultJson.put("GROSSSAL", grosssal);
			resultJson.put("DEDUCTION", deduct);
			resultJson.put("BCURRENCY", bcurrency);
			resultJson.put("errors", jsonArrayErr);
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("NET_SALARY", "");
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalDeduction(HttpServletRequest request) {
		
		HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
		HrPayrollHDRService hrPayrollHDRService = new HrPayrollHDRServiceImpl();
		HrPayrollDETService HrPayrollDETService = new HrPayrollDETServiceImpl();
		PlantMstDAO  plantMstDAO = new PlantMstDAO();
		DateUtils dateutils = new DateUtils();
		HrDeductionHdrDAO hrDeductionHdrDAO = new HrDeductionHdrDAO();
		HrDeductionDetDAO hrDeductionDetDAO = new HrDeductionDetDAO();
		HrPayrollDETDAO hrPayrollDETDAO = new HrPayrollDETDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
	    HrEmpSalaryDAO hrEmpSalaryDAO = new HrEmpSalaryDAO();
	    HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		 double payrolladdition = 0;
		 double payrolldeduction = 0;
		 double gratuityamount = 0;
		 double totalsalary = 0;
		 double netsalary = 0;
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			
			List<HrPayrollHDR> hrpayrollhdrlist = new ArrayList<HrPayrollHDR>();
			List<MonthYearPojo> monthyyearlist = new ArrayList<MonthYearPojo>();
			String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
			
			String[] fromDatesplit = fromDate.split("/");
			String[] toDatesplit = toDate.split("/");
			
			String fm = fromDatesplit[1];
			String fy = fromDatesplit[2];
			String tm = toDatesplit[1];
			String ty = toDatesplit[2];
			
			monthyyearlist = getmonthyeartile(fm, fy, tm, ty);

			for (MonthYearPojo monthYearPojo : monthyyearlist) {
				List<HrPayrollHDR> hrpayrollhdrmylist = hrPayrollHDRService.payrollhdrbymonthyear(plant, monthYearPojo.getMONTH(), monthYearPojo.getYEAR());
				hrpayrollhdrlist.addAll(hrpayrollhdrmylist);
			}
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			
			
			for (HrPayrollHDR hrpayroll:hrpayrollhdrlist) {
				 
				 ArrayList arrEmp = employeeDAO.getEmployeeListbyid(String.valueOf(hrpayroll.getEMPID()),plant);
				 Map employee=(Map)arrEmp.get(0);
				 String attdays = String.valueOf(hrpayroll.getPAYDAYS());
				 String paymonth = months[Integer.valueOf(hrpayroll.getMONTH())-1]+"-"+hrpayroll.getYEAR();

				
				 
				 List<HrEmpSalaryMst> empsalarymstlist = hrEmpSalaryDAO.getAllSalary(plant);
				 List<HrPayrollDET> hrPayrollDETlist = HrPayrollDETService.getpayrolldetByhdrid(plant, hrpayroll.getID());
				 for (HrEmpSalaryMst empsalarymst:empsalarymstlist) {
					 boolean ithasnoallowance = true;
					 for (HrPayrollDET hrPayrollDET : hrPayrollDETlist) {
						if(empsalarymst.getSALARYTYPE().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())) {
							totalsalary = totalsalary + hrPayrollDET.getAMOUNT();
							ithasnoallowance = false;
						}
					 }
				 }
				 
				
				 
				 List<HrPayrollAdditionMst> payaddmstdeduct = hrPayrollAdditionMstDAO.payrolladditionmstdeduct(plant, "");
				 for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){
					 for(HrPayrollAdditionMst hrPayrollAdditionMst:payaddmstdeduct){
						 if(hrPayrollAdditionMst.getADDITION_NAME().equalsIgnoreCase(hrPayrollDET.getSALARYTYPE())){
							 payrolldeduction = payrolldeduction + hrPayrollDET.getAMOUNT();
						 }
					 }
				  }
				 

				 
				
			}


			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("DEDUCTION", payrolldeduction);
			resultJson.put("errors", jsonArrayErr);
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("GROSS_SALARY", "");
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
}
