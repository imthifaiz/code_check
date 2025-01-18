package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.AgeingUtil;
import com.track.db.util.BillUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.PlantMstUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AgeingServlet  extends HttpServlet implements IMLogger  {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.AgeingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.AgeingServlet_PRINTPLANTMASTERINFO;
	String action = "";
	
	AgeingUtil ageingUtil = new AgeingUtil();
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		action = StrUtils.fString(request.getParameter("Submit")).trim();
		JSONObject jsonObjectResult = new JSONObject();
		try{
			if (action.equalsIgnoreCase("printAgeingReport")) {			
				PrintAgeingReport(request, response);			
			}else if (action.equalsIgnoreCase("printAgeingReportOutstanding")) {
				PrintOutstandingAgeingReport(request, response);
			}else if (action.equalsIgnoreCase("printConsolidatedAgeingReport")) {
				PrintConsolidatedAgeingReport(request, response);
			}else if (action.equalsIgnoreCase("view_inv_summary_aging_days")) {
				getInventorysmryAgingDetailsByDays(request, response);
			}else if (action.equalsIgnoreCase("PrintCustomerAgeingReport")) {
				PrintCustomerAgeingReport(request, response);
			}else if (action.equalsIgnoreCase("GET_CUSTOMER_AGING_SUMMARY_VIEW")) {
				getCustomerAgeingSummary(request, response);
			}else if (action.equalsIgnoreCase("PrintCustomerConsolidatedAgeingReport")) {
				PrintCustomerConsolidatedAgeingReport(request, response);
			}else if (action.equalsIgnoreCase("PrintSupplierAgeingReport")) {
				PrintSupplierAgeingReport(request, response);
			}else if (action.equalsIgnoreCase("GET_SUPPLIER_AGING_SUMMARY_VIEW")) {
				getSupplierAgeingSummary(request, response);
			}else if (action.equalsIgnoreCase("PrintSupplierConsolidatedAgeingReport")) {
				PrintSupplierConsolidatedAgeingReport(request, response);
			}
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void PrintAgeingReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray ageingDetailsArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
		try {
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
		    CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			Map m = null;
			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", 
				   CSTATE = "", CHPNO = "", CEMAIL = "",CRCBNO="",LOGIN_USER="",invoiceNo="";
			
			String Orderno ="",ordRefNo="",custName="",custcode="",Duetopay="",OrdDate="",OrderAmt="",chkddata="",PAYMENT_AMT="";
			String ORDER ="",ordNo="",PAYMENT_DATE="",PAYMENT_MODE="",PAYMENT_REF="",id="";
			String sCustCode ="",sCustName="",sAddr1="",sAddr2="",sAddr3="",sAddr4="",sCountry="",sZip="",sContactName="",
			STATEMENT_DATE="",sTelNo="",sHpNo="",sFax="",sEmail="",pmtdays="",dueDate="",FROM_DATE="",TO_DATE="",fdate="",tdate="",currencyid="";
			ArrayList arrCust = new ArrayList();
			ArrayList arrCustomers = new ArrayList();
			
			String PLANT = (String) session.getAttribute("PLANT");
			LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			ORDER=StrUtils.fString(request.getParameter("Order"));
	        ordRefNo = StrUtils.fString(request.getParameter("ordRefNo"));
	        custName= StrUtils.fString(request.getParameter("custName"));
	        STATEMENT_DATE     = StrUtils.fString(request.getParameter("STATEMENT_DATE"));
	        FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
	        TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
	        Orderno = StrUtils.fString(request.getParameter("ORDERNO"));
	        invoiceNo   = StrUtils.fString(request.getParameter("NAME"));
	        
	        if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	        String curDate =DateUtils.getDate();
            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
            
            if(STATEMENT_DATE.length()<0||STATEMENT_DATE==null||STATEMENT_DATE.equalsIgnoreCase(""))STATEMENT_DATE=curDate;
	           
	        if (FROM_DATE.length()>5)

	        	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	       	if (TO_DATE.length()>5)
	        	tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
 
	        String searchCond="",extracondpmt="",dtCondStr="",
	        		extraCon="",dtCondpmt="",paymenttype="",currencycond="",subSearchCond="";
		   
            if(ORDER.equalsIgnoreCase("BILL"))
            {
            	extraCon = " ORDERTYPE='BILL' ";
            }
            else if(ORDER.equalsIgnoreCase("INVOICE"))
            {
            	extraCon = " ORDERTYPE='INVOICE' ";
            }
            /*if(ORDER.equalsIgnoreCase("outbound"))
            {
            	paymenttype = " and ISNULL(PAYMENT_TYPE,'')='CREDIT' ";
            }
            else if(ORDER.equalsIgnoreCase("inbound"))
            {
            	paymenttype = " and ISNULL(PAYMENT_TYPE,'')='DEBIT' ";
            }*/
            
            paymenttype = " and ISNULL(PAYMENT_MODE,'')<>'' ";
          
            dtCondStr = "  AND CAST((SUBSTRING(PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PAYMENT_DT, 4, 2) + '-' + SUBSTRING(PAYMENT_DT, 1, 2)) AS date)";
        	 if (fdate.length() > 0) {
				 searchCond = searchCond + dtCondStr + "  >= '" 
						+ fdate
						+ "'  ";
			
				if (tdate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ tdate
					+ "'  ";
					
				}
			} else {
				if (tdate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ tdate
					+ "'  ";
					
				}
			} 
			if (tdate.length() > 0) {
				subSearchCond = subSearchCond +dtCondStr+ " <= '" 
				+ tdate
				+ "'  ";
				
			}
			
        	Calendar cal = new GregorianCalendar();
   			SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
   			SimpleDateFormat sdfrmt2 = new SimpleDateFormat("yyyy-MM-dd");
       		Date date;
       		date = sdfrmt.parse(FROM_DATE);
  			cal.setTime(date);
  			cal.add(cal.DATE, -1);
  			sdfrmt.setCalendar(cal);
  			String prevFromDate = sdfrmt.format(cal.getTime());

			String SysDate = DateUtils.getDate();
			String jasperPath = "";

			java.util.Date cDt = new java.util.Date(System.currentTimeMillis());
			ArrayList listQry = ageingUtil.getPlantMstDetails(PLANT);
			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);
				CNAME = (String) map.get("PLNTDESC");
				CADD1 = (String) map.get("ADD1");
				CADD2 = (String) map.get("ADD2");
				CADD3 = (String) map.get("ADD3");
				CADD4 = (String) map.get("ADD4");
				CSTATE = (String) map.get("STATE");
				CCOUNTRY = (String) map.get("COUNTY");
				CZIP = (String) map.get("ZIP");
				CTEL = (String) map.get("TELNO");
				CFAX = (String) map.get("FAX");
				CONTACTNAME = (String) map.get("NAME");
				CHPNO = (String) map.get("HPNO");
				CEMAIL = (String) map.get("EMAIL");
				CRCBNO = (String) map.get("RCBNO");
			}
			
		boolean flag =  ageingUtil.InsertTempOrderPaymentForAgeingReport(PLANT, fdate, tdate, ORDER, custName, Orderno, invoiceNo);	
		arrCustomers =  ageingUtil.getcustomerorsuppliername(PLANT, fdate, tdate, ORDER, custName);
		
		String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
		File checkImageFile = new File(imagePath);
		if (!checkImageFile.exists()) {
			imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		}
		
		for (int i = 0; i < arrCustomers.size(); i++)
		{
			Map lineArrcust = (Map) arrCustomers.get(i);                      
			custcode = (String)lineArrcust.get("custcode");
			custName = (String)lineArrcust.get("custname");
			/*currencyid = (String)lineArrcust.get("currencyid");*/
			JSONArray ageingDetArray = new JSONArray();
            extracondpmt="";
			if(custcode.length()>0){
				extracondpmt = " AND "+extraCon + searchCond + " AND (   CustCode = '"+custName+"') "; 	
			}
			/*if(currencyid.length()>0){
				extracondpmt =    extracondpmt+ " ";	
			}*/
            currencycond = subSearchCond + " AND (   CustCode = '"+custName+"') ";

 			String balanceFrwdCond = "";
 			if (fdate.length() > 0) {
 				balanceFrwdCond = " CAST((SUBSTRING(PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PAYMENT_DT, 4, 2) + '-' + SUBSTRING(PAYMENT_DT, 1, 2)) AS date) < = '"+
 						sdfrmt2.format(cal.getTime())+"' " + " AND (   CustCode = '"+custName+"') ";
 			}
            String minseq = "";
     		String sBatchSeq = "",statementNo="";
     		boolean insertFlag = false;
     		TblControlDAO _TblControlDAO = new TblControlDAO();
     		_TblControlDAO.setmLogger(mLogger);
     		Hashtable ht = new Hashtable();

     		String query = " isnull(NXTSEQ,'') as NXTSEQ";
     		ht.put(IDBConstants.PLANT, PLANT);
     		ht.put(IDBConstants.TBL_FUNCTION, "PAYMENT");
     		
     			boolean exitFlag = false;
     			boolean resultflag = false;
     			exitFlag = _TblControlDAO.isExisit(ht, "", PLANT);

     			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
     			if (exitFlag == false) {

     				Map htInsert = null;
     				Hashtable htTblCntInsert = new Hashtable();
     				htTblCntInsert.put(IDBConstants.PLANT, PLANT);
     				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
     				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PMT");
     				htTblCntInsert.put("MINSEQ", "0000");
     				htTblCntInsert.put("MAXSEQ", "9999");
     				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
     				htTblCntInsert.put(IDBConstants.CREATED_BY, LOGIN_USER);
     				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
     				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, PLANT);

     				statementNo = "1";
     			} else {
     				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

     				Map m1 = _TblControlDAO.selectRow(query, ht, "");
     				sBatchSeq = (String) m1.get("NXTSEQ");
     				System.out.println("length" + sBatchSeq.length());

     				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

     				String updatedSeq = Integer.toString(inxtSeq);
     				
     				Map htUpdate = null;

     				Hashtable htTblCntUpdate = new Hashtable();
     				htTblCntUpdate.put(IDBConstants.PLANT, PLANT);
     				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
     				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PMT");
     				StringBuffer updateQyery = new StringBuffer("set ");
     				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

     				boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", PLANT);
     				statementNo = updatedSeq;
     			}
             
			if(ORDER.equalsIgnoreCase("INVOICE")){
					 arrCust = cUtil.getCustomerDetails(custcode, PLANT);
					 if(arrCust.size() > 0){
					 	 sCustCode = (String) arrCust.get(0);
						 sCustName = (String) arrCust.get(1);
						 sAddr1 = (String) arrCust.get(2);
						 sAddr2 = (String) arrCust.get(3);
						 sAddr3 = (String) arrCust.get(4);
						 sCountry = (String) arrCust.get(5);
						 sZip = (String) arrCust.get(6);
						 sContactName = (String) arrCust.get(9);
						 sTelNo = (String) arrCust.get(11);
						 sHpNo = (String) arrCust.get(12);
						 sFax = (String) arrCust.get(13);
						 sEmail = (String) arrCust.get(14);
						 sAddr4 = (String) arrCust.get(16);
						 pmtdays = (String) arrCust.get(19);
						}
			 }else if(ORDER.equalsIgnoreCase("BILL")){
				 arrCust = customerBeanDAO.getVendorDetails(custcode, PLANT);
				 if(arrCust.size() > 0){
				     sCustCode = (String) arrCust.get(0);
					 sCustName = (String) arrCust.get(1);
					 sAddr1 = (String) arrCust.get(2);
					 sAddr2 = (String) arrCust.get(3);
					 sAddr3 = (String) arrCust.get(4);
					 sCountry = (String) arrCust.get(5);
					 sZip = (String) arrCust.get(6);
					 sContactName = (String) arrCust.get(8);
					 sTelNo = (String) arrCust.get(10);
					 sHpNo = (String) arrCust.get(11);
					 sEmail = (String) arrCust.get(12);
					 sFax= (String) arrCust.get(13);
					 sAddr4 = (String) arrCust.get(15);
					 pmtdays = (String) arrCust.get(18);
					}					
			 }
			 
			 if(arrCust.size() > 0){				 
				 ArrayList movQryList  = new ArrayList();
				 ArrayList reportDetailList  = new ArrayList();
				 double currentdue=0;double days30=0;double days60=0;double days90=0;double days120=0;double amtdue=0;
				 curDate = STATEMENT_DATE;
				 curDate = curDate.substring(0,2) + curDate.substring(3,5) + curDate.substring(6);
				 int pdays = 0;
				 if(pmtdays.length()>0)
				 {
	  				 pdays = Integer.parseInt(pmtdays);
	  			 }
				 movQryList = ageingUtil.getAgeingDetailsByFrwdBal(PLANT, fdate, tdate, ORDER, custName,"",currencyid);
				 reportDetailList = ageingUtil.getAgeingReportDetails(prevFromDate, balanceFrwdCond, extraCon, paymenttype, currencycond, extracondpmt, PLANT);
				 
				 for (int iCnt =0; iCnt<movQryList.size(); iCnt++){					 
					 Map lineArr = (Map) movQryList.get(iCnt);
		               int iIndex = iCnt + 1;		              
		               String ordDate = (String)lineArr.get("ordDate");
		               double balPay = Double.parseDouble((String)lineArr.get("DueToPay"));		               
		               ordDate = ordDate.substring(0,2) + ordDate.substring(3,5) + ordDate.substring(6);		                
		               Calendar cal1 = new GregorianCalendar();
		               Calendar cal2 = new GregorianCalendar();
		               SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		               Date d1 = sdf.parse(ordDate);
		               cal1.setTime(d1);
		               Date d2 = sdf.parse(curDate);
		               cal2.setTime(d2);		               
		               Long diff = d2.getTime()-d1.getTime();		               
		               int days =  (int) (diff/(1000 * 60 * 60 * 24));	
		               if(days <= pdays)
		               {
		            	   currentdue = currentdue + balPay;
		               }
		               else{		            	   
		            	   days =  days-pdays;		               
		            	   if(days>0 && days<=30)
		            	   {
		            		   days30 = days30 + balPay;
		            	   }
		            	   if(days>30 && days<=60)
		            	   {
		            		   days60 = days60 + balPay;
		            	   }
		            	   if(days>60 && days<=90)
		            	   {
		            		   days90 = days90 + balPay;
		            	   }
		            	   if(days>90)
		            	   {
		            		   days120 = days120 + balPay;
		            	   }
		               }  		               
				 }				 
				 amtdue =  currentdue+days30+days60+days90+days120;					 
				 String currentdueamt = String.valueOf(currentdue);
				 currentdueamt = StrUtils.formattwodecNum(currentdueamt); 
				 
				 String days30due = String.valueOf(days30);
				 days30due = StrUtils.formattwodecNum(days30due); 
				
				 String days60due = String.valueOf(days60);
				 days60due = StrUtils.formattwodecNum(days60due); 
				
				 String days90due = String.valueOf(days90);
				 days90due = StrUtils.formattwodecNum(days90due); 
				
				 String days120due = String.valueOf(days120);
				 days120due = StrUtils.formattwodecNum(days120due); 
				
				 String totaldue = String.valueOf(amtdue);
				 totaldue = StrUtils.formattwodecNum(totaldue); 
				 
				 for (int iCnt =0; iCnt<reportDetailList.size(); iCnt++){
					 Map lineArr = (Map) reportDetailList.get(iCnt);
					 JSONObject reportDetails = new JSONObject();
					 
					 String trandate = (String)lineArr.get("TRANDATE");
					 String bill = (String)lineArr.get("BILL");
					 String amount = (String)lineArr.get("AMOUNT");
					 String balance = (String)lineArr.get("BALANCE");
					 
					 reportDetails.put("trandate", trandate);
					 reportDetails.put("bill", bill);
					 reportDetails.put("amount", amount);
					 reportDetails.put("balance", balance);
					 
					 ageingDetArray.add(reportDetails);
				 }
				 
			                
	            Map parameters = new HashMap();
	            JSONObject reportContent = new JSONObject();
	            
	            
	            /* Company Details */
	            reportContent.put("fromAddress_CompanyName", CNAME);
	            reportContent.put("fromAddress_CompanyName", CNAME);
	            reportContent.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
	            reportContent.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
	            reportContent.put("fromAddress_Country", CSTATE + " " +CCOUNTRY);
	            reportContent.put("fromAddress_ZIPCode", CZIP);
	            reportContent.put("fromAddress_RCBNO", CRCBNO);
	            reportContent.put("fromAddress_TpNo", CTEL);
	            reportContent.put("fromAddress_FaxNo", CFAX);
	            reportContent.put("fromAddress_ContactPersonName", CONTACTNAME);
	            reportContent.put("fromAddress_ContactPersonMobile", CHPNO);
	            reportContent.put("fromAddress_ContactPersonEmail", CEMAIL);
	            
	            /* Customer Details */
	            reportContent.put("To_ContactName", sContactName);
	            reportContent.put("To_CompanyName", sCustName);
	            reportContent.put("To_BlockAddress", sAddr1+"  " + sAddr2);
	            reportContent.put("To_RoadAddress", sAddr3 +"  " + sAddr4);
	            reportContent.put("To_Country", sCountry);
	            reportContent.put("To_ZIPCode", sZip);
	            
	            reportContent.put("statementNo", statementNo);	            
	            reportContent.put("CurDate", STATEMENT_DATE);
	            
	            /* Label Details */
	            reportContent.put("lblHeader", "To");
	            reportContent.put("lblstatement", "Statement Of Account");
	            reportContent.put("lblDate", "Date");
	            reportContent.put("lblstatementno", "Statement No");	            
	            reportContent.put("lblamtdue", "Amount Due");	            
	            reportContent.put("lblOrderNo", "Order No");
	            reportContent.put("lblAmount", "Amount");
	            reportContent.put("lblBalance", "Balance");
	            reportContent.put("lblcurrentdue", "Current Due");
	            reportContent.put("lbl30daysdue", "1-30 Days Past Due");
	            reportContent.put("lbl60daysdue", "31-60 Days Past Due");
	            reportContent.put("lbl90daysdue", "61-90 Days Past Due");
	            reportContent.put("lbl90plusdaysdue", "90+ Days Past Due");
	            
	            /* Aging by Days*/
	            reportContent.put("currentdue", currentdueamt);
	            reportContent.put("v30daysdue", days30due);
	            reportContent.put("v60daysdue", days60due);
	            reportContent.put("v90daysdue", days90due);
	            reportContent.put("v90plusdaysdue", days120due);
				reportContent.put("amountdue", totaldue);
				
				jsonArray.add(reportContent);
				ageingDetailsArray.add(ageingDetArray);
			 }
			 resultJson.put("reportContent", jsonArray);
			 resultJson.put("reportContentDetails", ageingDetailsArray);		 
		}
		
		JSONObject resultJsonInt = new JSONObject();
        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
        resultJsonInt.put("ERROR_CODE", "100");
        jsonArrayErr.add(resultJsonInt);
        resultJson.put("errors", jsonArrayErr);
        
		} catch (Exception e) {
			jsonArray.add("");
    		resultJson.put("items", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		} 
        response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	public void PrintOutstandingAgeingReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray ageingDetailsArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
		try {
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
		    CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			Map m = null;
			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", 
					CSTATE = "", CHPNO = "", CEMAIL = "",CRCBNO="",LOGIN_USER="",name="";
			
			String Orderno ="",ordRefNo="",custName="",custcode="",Duetopay="",OrdDate="",OrderAmt="",chkddata="",PAYMENT_AMT="";
			String ORDER ="",ordNo="",PAYMENT_DATE="",PAYMENT_MODE="",PAYMENT_REF="",id="";
			String sCustCode ="",sCustName="",sAddr1="",sAddr2="",sAddr3="",sAddr4="",sCountry="",sZip="",sContactName="",
			STATEMENT_DATE="",sTelNo="",sHpNo="",sFax="",sEmail="",pmtdays="",dueDate="",FROM_DATE="",TO_DATE="",fdate="",tdate="",currencyid="";
					ArrayList arrCust = new ArrayList();
			List jasperPrintList = new ArrayList();
			ArrayList arrCustomers = new ArrayList();
			
			String PLANT = (String) session.getAttribute("PLANT");
			LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			ORDER=StrUtils.fString(request.getParameter("Order"));
	        ordRefNo = StrUtils.fString(request.getParameter("ordRefNo"));
	        custName= StrUtils.fString(request.getParameter("custName"));
	        STATEMENT_DATE     = StrUtils.fString(request.getParameter("STATEMENT_DATE"));
	        FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
	        TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
	        Orderno = StrUtils.fString(request.getParameter("ORDERNO"));
	        name = StrUtils.fString(request.getParameter("NAME"));
	        
	        if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	        String curDate =DateUtils.getDate();
            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
            
            if(STATEMENT_DATE.length()<0||STATEMENT_DATE==null||STATEMENT_DATE.equalsIgnoreCase(""))STATEMENT_DATE=curDate;
            
	        if (FROM_DATE.length()>5)

	        	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	       	if (TO_DATE.length()>5)
	        	tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
 
	        String searchCond="",extracondpmt="",dtCondStr="",
	        		extraCon="",dtCondpmt="",paymenttype="",currencycond="";
		   
	        if(ORDER.equalsIgnoreCase("BILL"))
            {
            	extraCon = " ORDERTYPE='BILL' ";
            }
            else if(ORDER.equalsIgnoreCase("INVOICE"))
            {
            	extraCon = " ORDERTYPE='INVOICE' ";
            }
            /*if(ORDER.equalsIgnoreCase("outbound"))
            {
            	paymenttype = " and ISNULL(PAYMENT_TYPE,'')='CREDIT' ";
            }
            else if(ORDER.equalsIgnoreCase("inbound"))
            {
            	paymenttype = " and ISNULL(PAYMENT_TYPE,'')='DEBIT' ";
            }*/
	        paymenttype = " and ISNULL(PAYMENT_MODE,'')<>'' ";
          
            dtCondStr = "  AND CAST((SUBSTRING(PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PAYMENT_DT, 4, 2) + '-' + SUBSTRING(PAYMENT_DT, 1, 2)) AS date)";
        	 if (fdate.length() > 0) {
				 searchCond = searchCond + dtCondStr + "  >= '" 
						+ fdate
						+ "'  ";
			
				if (tdate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ tdate
					+ "'  ";
					
				}
			} else {
				if (tdate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ tdate
					+ "'  ";
					
				}
			}   

			String SysDate = DateUtils.getDate();
			String jasperPath = "";
			
				jasperPath = DbBean.JASPER_INPUT + "/" + "rptOutstandingAgeingDetails";
			
			String imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo.gif";
			String imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo1.gif";
			File checkImageFile = new File(imagePath);
	        if (!checkImageFile.exists()) {
	           imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+"NoLogo.jpg"; 
	        }
	        File checkImageFile1 = new File(imagePath1);
	        if (!checkImageFile1.exists()) {
	           imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+"NoLogo.jpg"; 
	        }

			java.util.Date cDt = new java.util.Date(System.currentTimeMillis());
			ArrayList listQry = ageingUtil.getPlantMstDetails(PLANT);
			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);

				CNAME = (String) map.get("PLNTDESC");
				CADD1 = (String) map.get("ADD1");
				CADD2 = (String) map.get("ADD2");
				CADD3 = (String) map.get("ADD3");
				CADD4 = (String) map.get("ADD4");
				CSTATE = (String) map.get("STATE");
				CCOUNTRY = (String) map.get("COUNTY");
				CZIP = (String) map.get("ZIP");
				CTEL = (String) map.get("TELNO");
				CFAX = (String) map.get("FAX");
				CONTACTNAME = (String) map.get("NAME");
				CHPNO = (String) map.get("HPNO");
				CEMAIL = (String) map.get("EMAIL");
				CRCBNO = (String) map.get("RCBNO");

			}
			
		boolean flag =  ageingUtil.InsertTempOrderPayment(PLANT, fdate, tdate, ORDER, custName,Orderno,name);	
		arrCustomers =  ageingUtil.getcustomerorsuppliername(PLANT, fdate, tdate, ORDER, custName);
		for (int i = 0; i < arrCustomers.size(); i++)
		{
			Map lineArrcust = (Map) arrCustomers.get(i);
			JSONArray ageingDetArray = new JSONArray();
                      
			custcode = (String)lineArrcust.get("custcode");
			custName = (String)lineArrcust.get("custname");
			/*currencyid = (String)lineArrcust.get("currencyid");*/
			
             extracondpmt="";
             if(custcode.length()>0){
            	 extracondpmt = " AND "+extraCon + searchCond + " AND (   CustCode = '"+custName+"') ";
              	
             }
             /*if(currencyid.length()>0){
            	 extracondpmt =    extracondpmt+ " AND (   currencyid = '"+currencyid+"') ";
              	
             }*/
             currencycond = searchCond + " AND (   CustCode = '"+custName+"') ";
            String minseq = "";
     		String sBatchSeq = "",statementNo="";
     		boolean insertFlag = false; 
     		TblControlDAO _TblControlDAO = new TblControlDAO();
     		_TblControlDAO.setmLogger(mLogger);
     		Hashtable ht = new Hashtable();

     		String query = " isnull(NXTSEQ,'') as NXTSEQ";
     		ht.put(IDBConstants.PLANT, PLANT);
     		ht.put(IDBConstants.TBL_FUNCTION, "PAYMENT");
     		
     			boolean exitFlag = false;
     			boolean resultflag = false;
     			exitFlag = _TblControlDAO.isExisit(ht, "", PLANT);

     			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
     			if (exitFlag == false) {

     				Map htInsert = null;
     				Hashtable htTblCntInsert = new Hashtable();
     				htTblCntInsert.put(IDBConstants.PLANT, PLANT);
     				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
     				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PMT");
     				htTblCntInsert.put("MINSEQ", "0000");
     				htTblCntInsert.put("MAXSEQ", "9999");
     				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
     				htTblCntInsert.put(IDBConstants.CREATED_BY, LOGIN_USER);
     				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
     				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, PLANT);

     				statementNo = "1";
     			} else {
     				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

     				Map m1 = _TblControlDAO.selectRow(query, ht, "");
     				sBatchSeq = (String) m1.get("NXTSEQ");
     				System.out.println("length" + sBatchSeq.length());

     				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

     				String updatedSeq = Integer.toString(inxtSeq);
     				
     				Map htUpdate = null;

     				Hashtable htTblCntUpdate = new Hashtable();
     				htTblCntUpdate.put(IDBConstants.PLANT, PLANT);
     				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
     				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PMT");
     				StringBuffer updateQyery = new StringBuffer("set ");
     				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

     				boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", PLANT);
     				statementNo = updatedSeq;
     			}
             
			if(ORDER.equalsIgnoreCase("INVOICE")){
					 arrCust = cUtil.getCustomerDetails(custcode, PLANT);
					 if(arrCust.size() > 0){
					 	 sCustCode = (String) arrCust.get(0);
						 sCustName = (String) arrCust.get(1);
						 sAddr1 = (String) arrCust.get(2);
						 sAddr2 = (String) arrCust.get(3);
						 sAddr3 = (String) arrCust.get(4);
						 sCountry = (String) arrCust.get(5);
						 sZip = (String) arrCust.get(6);
						 sContactName = (String) arrCust.get(9);
						 sTelNo = (String) arrCust.get(11);
						 sHpNo = (String) arrCust.get(12);
						 sFax = (String) arrCust.get(13);
						 sEmail = (String) arrCust.get(14);
						 sAddr4 = (String) arrCust.get(16);
						 pmtdays = (String) arrCust.get(19);
						}
				 }
			 else if(ORDER.equalsIgnoreCase("BILL"))
				 {
					 arrCust = customerBeanDAO.getVendorDetails(custcode, PLANT);
					 if(arrCust.size() > 0){
					     sCustCode = (String) arrCust.get(0);
						 sCustName = (String) arrCust.get(1);
						 sAddr1 = (String) arrCust.get(2);
						 sAddr2 = (String) arrCust.get(3);
						 sAddr3 = (String) arrCust.get(4);
						 sCountry = (String) arrCust.get(5);
						 sZip = (String) arrCust.get(6);
						 sContactName = (String) arrCust.get(8);
						 sTelNo = (String) arrCust.get(10);
						 sHpNo = (String) arrCust.get(11);
						 sEmail = (String) arrCust.get(12);
						 sFax= (String) arrCust.get(13);
						 sAddr4 = (String) arrCust.get(15);
						 pmtdays = (String) arrCust.get(18);
						}
					
				 }
			 
			 if(arrCust.size() > 0){
				 
				 ArrayList movQryList  = new ArrayList();
				 ArrayList reportDetailList  = new ArrayList();
				 double currentdue=0;double days30=0;double days60=0;double days90=0;double days120=0;double amtdue=0;
				 curDate = STATEMENT_DATE;
				 curDate = curDate.substring(0,2) + curDate.substring(3,5) + curDate.substring(6);
				 int pdays = 0;
				 if(pmtdays.length()>0)
				 {
	  				 pdays = Integer.parseInt(pmtdays);
	  			 }
				 movQryList = ageingUtil.getAgeingDetails(PLANT, fdate, tdate, ORDER, custName,"",currencyid);
				 reportDetailList = ageingUtil.getOutstandingAgeingReportDetails(extraCon, custName, currencycond, PLANT);
				 
				 for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					 
					 Map lineArr = (Map) movQryList.get(iCnt);
		               int iIndex = iCnt + 1;
		              
		               String ordDate = (String)lineArr.get("ordDate");
		               double balPay = Double.parseDouble((String)lineArr.get("DueToPay"));
		               
		               ordDate = ordDate.substring(0,2) + ordDate.substring(3,5) + ordDate.substring(6);
		                
		               Calendar cal1 = new GregorianCalendar();
		               Calendar cal2 = new GregorianCalendar();

		               SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

		               Date d1 = sdf.parse(ordDate);
		               cal1.setTime(d1);
		               Date d2 = sdf.parse(curDate);
		               cal2.setTime(d2);
		               
		               Long diff = d2.getTime()-d1.getTime();
		               
		               int days =  (int) (diff/(1000 * 60 * 60 * 24));
		               
		               
		               if(days <= pdays)
		               {
		            	   currentdue = currentdue + balPay;
		               }
		               else{
		            	   
		            	   days =  days-pdays;
		               
		            	   if(days>0 && days<=30)
		            	   {
		            		   days30 = days30 + balPay;
		            	   }
		            	   if(days>30 && days<=60)
		            	   {
		            		   days60 = days60 + balPay;
		            	   }
		            	   if(days>60 && days<=90)
		            	   {
		            		   days90 = days90 + balPay;
		            	   }
		            	   if(days>90)
		            	   {
		            		   days120 = days120 + balPay;
		            	   }
		               }  
		               
				 }
				 
				 amtdue =  currentdue+days30+days60+days90+days120;
					 
				 String currentdueamt = String.valueOf(currentdue);
				 currentdueamt = StrUtils.formattwodecNum(currentdueamt); 
				 
				 String days30due = String.valueOf(days30);
				 if(Double.parseDouble(days30due) == -0.00) {
					 days30due = "0.00";
					}
				 days30due = StrUtils.formattwodecNum(days30due); 
				 
				
				
				 String days60due = String.valueOf(days60);
				 if(Double.parseDouble(days60due) == -0.00) {
					 days60due = "0.00";
					}
				 days60due = StrUtils.formattwodecNum(days60due);	
				
				 String days90due = String.valueOf(days90);
				 if(Double.parseDouble(days90due) == -0.00) {
					 days90due = "0.00";
					}
				 days90due = StrUtils.formattwodecNum(days90due); 
				
				 String days120due = String.valueOf(days120);
				 
				 if(Double.parseDouble(days120due) == -0.00) {
					 days120due = "0.00";
					}
				 days120due = StrUtils.formattwodecNum(days120due); 
				
				
				
				 String totaldue = String.valueOf(amtdue);
				 totaldue = StrUtils.formattwodecNum(totaldue); 
				 
				 for (int iCnt =0; iCnt<reportDetailList.size(); iCnt++){
					 Map lineArr = (Map) reportDetailList.get(iCnt);
					 JSONObject reportDetails = new JSONObject();
					 
					 String trandate = (String)lineArr.get("TRANDATE");
					 String bill = (String)lineArr.get("BILL");
					 String amount = (String)lineArr.get("AMOUNT");
					 String balance = (String)lineArr.get("BALANCE");
					 
					 reportDetails.put("trandate", trandate);
					 reportDetails.put("bill", bill);
					 reportDetails.put("amount", amount);
					 reportDetails.put("balance", balance);
					 
					 ageingDetArray.add(reportDetails);
				 }
				 JSONObject reportContent = new JSONObject();
				 /* Company Details */
		            reportContent.put("fromAddress_CompanyName", CNAME);
		            reportContent.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
		            reportContent.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
		            reportContent.put("fromAddress_Country", CSTATE + " " +CCOUNTRY);
		            reportContent.put("fromAddress_ZIPCode", CZIP);
		            reportContent.put("fromAddress_RCBNO", CRCBNO);
		            reportContent.put("fromAddress_TpNo", CTEL);
		            reportContent.put("fromAddress_FaxNo", CFAX);
		            reportContent.put("fromAddress_ContactPersonName", CONTACTNAME);
		            reportContent.put("fromAddress_ContactPersonMobile", CHPNO);
		            reportContent.put("fromAddress_ContactPersonEmail", CEMAIL);
		            
		            /* Customer Details */
		            reportContent.put("To_ContactName", sContactName);
		            reportContent.put("To_CompanyName", sCustName);
		            reportContent.put("To_BlockAddress", sAddr1+"  " + sAddr2);
		            reportContent.put("To_RoadAddress", sAddr3 +"  " + sAddr4);
		            reportContent.put("To_Country", sCountry);
		            reportContent.put("To_ZIPCode", sZip);
		            
		            reportContent.put("statementNo", statementNo);	            
		            reportContent.put("CurDate", STATEMENT_DATE);
		            
		            /* Label Details */
		            reportContent.put("lblHeader", "To");
		            reportContent.put("lblstatement", "Statement Of Account");
		            reportContent.put("lblDate", "Date");
		            reportContent.put("lblstatementno", "Statement No");	            
		            reportContent.put("lblamtdue", "Amount Due");	            
		            reportContent.put("lblOrderNo", "Order No");
		            reportContent.put("lblAmount", "Amount");
		            reportContent.put("lblBalance", "Balance");
		            reportContent.put("lblcurrentdue", "Current Due");
		            reportContent.put("lbl30daysdue", "1-30 Days Past Due");
		            reportContent.put("lbl60daysdue", "31-60 Days Past Due");
		            reportContent.put("lbl90daysdue", "61-90 Days Past Due");
		            reportContent.put("lbl90plusdaysdue", "90+ Days Past Due");
		            
		            /* Aging by Days*/
		            reportContent.put("currentdue", currentdueamt);
		            reportContent.put("v30daysdue", days30due);
		            reportContent.put("v60daysdue", days60due);
		            reportContent.put("v90daysdue", days90due);
		            reportContent.put("v90plusdaysdue", days120due);
					reportContent.put("amountdue", totaldue);
					
		       ArrayList account = ageingUtil.getAgeingDetailsCount(PLANT,currencycond,custName,Orderno,extraCon);
		       int account_cnt = account.size();
				if(arrCustomers.size()==1){
					jsonArray.add(reportContent);
					ageingDetailsArray.add(ageingDetArray);
				}else{
				if(account_cnt>0){
					jsonArray.add(reportContent);
					ageingDetailsArray.add(ageingDetArray);
				}
			 }
			 }
			 resultJson.put("reportContent", jsonArray);
			 resultJson.put("reportContentDetails", ageingDetailsArray);
		}
		JSONObject resultJsonInt = new JSONObject();
        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
        resultJsonInt.put("ERROR_CODE", "100");
        jsonArrayErr.add(resultJsonInt);
        resultJson.put("errors", jsonArrayErr);
		} catch (IOException e) {
			jsonArray.add("");
    		resultJson.put("items", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}	
	
	public void PrintConsolidatedAgeingReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray ageingDetailsArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
		try {

			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
		    CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			Map m = null;
			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", 
					CSTATE = "", CHPNO = "", CEMAIL = "",CRCBNO="",LOGIN_USER="",OrderStatus="",invoiceNo="";
			
			String Orderno ="",ordRefNo="",custName="",custcode="",Duetopay="",OrdDate="",OrderAmt="",chkddata="",PAYMENT_AMT="";
			String ORDER ="",ordNo="",PAYMENT_DATE="",PAYMENT_MODE="",PAYMENT_REF="",id="";
			String sCustCode ="",sCustName="",custNameFilter="",sAddr1="",sAddr2="",sAddr3="",sAddr4="",sCountry="",sZip="",sContactName="",
			STATEMENT_DATE="",sTelNo="",sHpNo="",sFax="",sEmail="",pmtdays="",dueDate="",FROM_DATE="",TO_DATE="",fdate="",tdate="",currencyid="";
					ArrayList arrCust = new ArrayList();
			List jasperPrintList = new ArrayList();
			ArrayList arrCustomers = new ArrayList();
			
			String PLANT = (String) session.getAttribute("PLANT");
			LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			ORDER=StrUtils.fString(request.getParameter("Order"));
	        ordRefNo = StrUtils.fString(request.getParameter("ordRefNo"));
	        custNameFilter= StrUtils.fString(request.getParameter("custName"));
	        custName= StrUtils.fString(request.getParameter("custName"));
	        STATEMENT_DATE     = StrUtils.fString(request.getParameter("STATEMENT_DATE"));
	        FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
	        TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
	        Orderno = StrUtils.fString(request.getParameter("ORDERNO"));
	        invoiceNo   = StrUtils.fString(request.getParameter("NAME"));
	        
	        if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	        String curDate =DateUtils.getDate();
            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
            
            if(STATEMENT_DATE.length()<0||STATEMENT_DATE==null||STATEMENT_DATE.equalsIgnoreCase(""))STATEMENT_DATE=curDate;
            
	        if (FROM_DATE.length()>5)

	        	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	       	if (TO_DATE.length()>5)
	        	tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
 
	        String searchCond="",extracondpmt="",dtCondStr="",
	        		extraCon="",dtCondpmt="",paymenttype="",currencycond="";
		   
	        if(ORDER.equalsIgnoreCase("BILL"))
            {
            	extraCon = " ORDERTYPE='BILL' ";
            }
            else if(ORDER.equalsIgnoreCase("INVOICE"))
            {
            	extraCon = " ORDERTYPE='INVOICE' ";
            }
	        
	        if(ORDER.equalsIgnoreCase("BILL"))
            {
	        	paymenttype = " and ISNULL(PAYMENT_MODE,'')<>'' and ORDERTYPE='BILL' ";
            }
            else if(ORDER.equalsIgnoreCase("INVOICE"))
            {
            	paymenttype = " and ISNULL(PAYMENT_MODE,'')<>'' and ORDERTYPE='INVOICE' ";
            }
          
            dtCondStr = "  AND CAST((SUBSTRING(PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PAYMENT_DT, 4, 2) + '-' + SUBSTRING(PAYMENT_DT, 1, 2)) AS date)";
        	 if (fdate.length() > 0) {
				 searchCond = searchCond + dtCondStr + "  >= '" 
						+ fdate
						+ "'  ";
			
				if (tdate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ tdate
					+ "'  ";
					
				}
			} else {
				if (tdate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ tdate
					+ "'  ";
					
				}
			}   

			String SysDate = DateUtils.getDate();
			String jasperPath = "";
			
				jasperPath = DbBean.JASPER_INPUT + "/" + "rptAgeingDetailsConsolidated";
			
			String imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo.gif";
			String imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo1.gif";
			File checkImageFile = new File(imagePath);
	        if (!checkImageFile.exists()) {
	           imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+"NoLogo.jpg"; 
	        }
	        File checkImageFile1 = new File(imagePath1);
	        if (!checkImageFile1.exists()) {
	           imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+"NoLogo.jpg"; 
	        }

			java.util.Date cDt = new java.util.Date(System.currentTimeMillis());
			ArrayList listQry = ageingUtil.getPlantMstDetails(PLANT);
			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);
				CNAME = (String) map.get("PLNTDESC");
				CADD1 = (String) map.get("ADD1");
				CADD2 = (String) map.get("ADD2");
				CADD3 = (String) map.get("ADD3");
				CADD4 = (String) map.get("ADD4");
				CCOUNTRY = (String) map.get("COUNTY");
				CZIP = (String) map.get("ZIP");
				CTEL = (String) map.get("TELNO");
				CFAX = (String) map.get("FAX");
				CONTACTNAME = (String) map.get("NAME");
				CHPNO = (String) map.get("HPNO");
				CEMAIL = (String) map.get("EMAIL");
				CRCBNO = (String) map.get("RCBNO");

			}
			
		boolean flag =  ageingUtil.InsertTempOrderPayment(PLANT, fdate, tdate, ORDER, custName, Orderno, invoiceNo);
	
		arrCustomers =  ageingUtil.getcustomerorsuppliernameConsolidated(PLANT, fdate, tdate, ORDER, custName);
		String oCurrency="",nCurrency="";
		
		for (int i = 0; i < arrCustomers.size(); i++)
		{
			Map lineArrcust = (Map) arrCustomers.get(i);
			
			nCurrency	=(String)lineArrcust.get("currencyid");
			if(oCurrency.equalsIgnoreCase(nCurrency)){
				
				continue;
			}
		
                      
			custcode = (String)lineArrcust.get("custcode");
			custName = (String)lineArrcust.get("custname");
			/*currencyid = (String)lineArrcust.get("currencyid");*/
			JSONArray ageingDetArray = new JSONArray();
			
             extracondpmt="";
           //  if(custcode.length()>0){
            	/* extracondpmt = " AND "+extraCon + searchCond + " AND (   CustCode = '"+custcode+"') ";*/
            	 extracondpmt = " AND "+extraCon + searchCond ;
              	
         //    }
             if(currencyid.length()>0){
            	 extracondpmt =    extracondpmt+ " AND (   currencyid = '"+currencyid+"') ";
              	
             }
           /*  currencycond = searchCond + " AND (   CustCode = '"+custcode+"') AND (   currencyid = '"+currencyid+"') ";*/
             currencycond = searchCond + "  ";
            String minseq = "";
     		String sBatchSeq = "",statementNo="";
     		boolean insertFlag = false;
     		TblControlDAO _TblControlDAO = new TblControlDAO();
     		_TblControlDAO.setmLogger(mLogger);
     		Hashtable ht = new Hashtable();

     		String query = " isnull(NXTSEQ,'') as NXTSEQ";
     		ht.put(IDBConstants.PLANT, PLANT);
     		ht.put(IDBConstants.TBL_FUNCTION, "PAYMENT");
     		
     			boolean exitFlag = false;
     			boolean resultflag = false;
     			exitFlag = _TblControlDAO.isExisit(ht, "", PLANT);

     			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
     			if (exitFlag == false) {

     				Map htInsert = null;
     				Hashtable htTblCntInsert = new Hashtable();
     				htTblCntInsert.put(IDBConstants.PLANT, PLANT);
     				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
     				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PMT");
     				htTblCntInsert.put("MINSEQ", "0000");
     				htTblCntInsert.put("MAXSEQ", "9999");
     				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
     				htTblCntInsert.put(IDBConstants.CREATED_BY, LOGIN_USER);
     				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
     				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, PLANT);

     				statementNo = "1";
     			} else {
     				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

     				Map m1 = _TblControlDAO.selectRow(query, ht, "");
     				sBatchSeq = (String) m1.get("NXTSEQ");
     				System.out.println("length" + sBatchSeq.length());

     				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

     				String updatedSeq = Integer.toString(inxtSeq);
     				
     				Map htUpdate = null;

     				Hashtable htTblCntUpdate = new Hashtable();
     				htTblCntUpdate.put(IDBConstants.PLANT, PLANT);
     				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
     				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PMT");
     				StringBuffer updateQyery = new StringBuffer("set ");
     				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

     				boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", PLANT);
     				statementNo = updatedSeq;
     			}
             
             
             
			if(ORDER.equalsIgnoreCase("INVOICE")){
					 arrCust = cUtil.getCustomerDetails(custcode, PLANT);
					 if(arrCust.size() > 0){
					 	 sCustCode = (String) arrCust.get(0);
						 sCustName = (String) arrCust.get(1);
						 sAddr1 = (String) arrCust.get(2);
						 sAddr2 = (String) arrCust.get(3);
						 sAddr3 = (String) arrCust.get(4);
						 sCountry = (String) arrCust.get(5);
						 sZip = (String) arrCust.get(6);
						 sContactName = (String) arrCust.get(9);
						 sTelNo = (String) arrCust.get(11);
						 sHpNo = (String) arrCust.get(12);
						 sFax = (String) arrCust.get(13);
						 sEmail = (String) arrCust.get(14);
						 sAddr4 = (String) arrCust.get(16);
						 pmtdays = (String) arrCust.get(19);
						}
					 OrderStatus = "1";
				 }
			 else if(ORDER.equalsIgnoreCase("BILL"))
				 {
					 arrCust = customerBeanDAO.getVendorDetails(custcode, PLANT);
					 if(arrCust.size() > 0){
					     sCustCode = (String) arrCust.get(0);
						 sCustName = (String) arrCust.get(1);
						 sAddr1 = (String) arrCust.get(2);
						 sAddr2 = (String) arrCust.get(3);
						 sAddr3 = (String) arrCust.get(4);
						 sCountry = (String) arrCust.get(5);
						 sZip = (String) arrCust.get(6);
						 sContactName = (String) arrCust.get(8);
						 sTelNo = (String) arrCust.get(10);
						 sHpNo = (String) arrCust.get(11);
						 sEmail = (String) arrCust.get(12);
						 sFax= (String) arrCust.get(13);
						 sAddr4 = (String) arrCust.get(15);
						 pmtdays = (String) arrCust.get(18);
						}
					 OrderStatus = "0";
				 }
			 
			 if(arrCust.size() > 0){
				 
				 ArrayList movQryList  = new ArrayList();
				 ArrayList reportDetailList  = new ArrayList();
				 double currentdue=0;double days30=0;double days60=0;double days90=0;double days120=0;double amtdue=0;
				 curDate = STATEMENT_DATE;
				 curDate = curDate.substring(0,2) + curDate.substring(3,5) + curDate.substring(6);
				 int pdays = 0;
				 if(pmtdays.length()>0)
				 {
	  				 pdays = Integer.parseInt(pmtdays);
	  			 }
				 movQryList = ageingUtil.getAgeingDetailsConsolidate(PLANT, fdate, tdate, ORDER, custNameFilter,"",currencyid);
				 reportDetailList = ageingUtil.getConsolidateAgeingReportDetails(extraCon, paymenttype, currencycond, extracondpmt, OrderStatus, PLANT);
				 for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					 
					 Map lineArr = (Map) movQryList.get(iCnt);
		               int iIndex = iCnt + 1;
		              
		               String ordDate = (String)lineArr.get("ordDate");
		               double balPay = Double.parseDouble((String)lineArr.get("DueToPay"));
		               
		               ordDate = ordDate.substring(0,2) + ordDate.substring(3,5) + ordDate.substring(6);
		                
		               Calendar cal1 = new GregorianCalendar();
		               Calendar cal2 = new GregorianCalendar();

		               SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

		               Date d1 = sdf.parse(ordDate);
		               cal1.setTime(d1);
		               Date d2 = sdf.parse(curDate);
		               cal2.setTime(d2);
		               
		               Long diff = d2.getTime()-d1.getTime();
		               
		               int days =  (int) (diff/(1000 * 60 * 60 * 24));
		               
		               
		               if(days <= pdays)
		               {
		            	   currentdue = currentdue + balPay;
		               }
		               else{
		            	   
		            	   days =  days-pdays;
		               
		            	   if(days>0 && days<=30)
		            	   {
		            		   days30 = days30 + balPay;
		            	   }
		            	   if(days>30 && days<=60)
		            	   {
		            		   days60 = days60 + balPay;
		            	   }
		            	   if(days>60 && days<=90)
		            	   {
		            		   days90 = days90 + balPay;
		            	   }
		            	   if(days>90)
		            	   {
		            		   days120 = days120 + balPay;
		            	   }
		               }  
		               
				 }
				 
				 amtdue =  currentdue+days30+days60+days90+days120;
					 
				 String currentdueamt = String.valueOf(currentdue);
				 currentdueamt = StrUtils.formattwodecNum(currentdueamt); 
				 
				 String days30due = String.valueOf(days30);
				 days30due = StrUtils.formattwodecNum(days30due); 
				
				 String days60due = String.valueOf(days60);
				 days60due = StrUtils.formattwodecNum(days60due); 
				
				 String days90due = String.valueOf(days90);
				 days90due = StrUtils.formattwodecNum(days90due); 
				
				 String days120due = String.valueOf(days120);
				 days120due = StrUtils.formattwodecNum(days120due); 
				
				 String totaldue = String.valueOf(amtdue);
				 totaldue = StrUtils.formattwodecNum(totaldue); 
				 
				 for (int iCnt =0; iCnt<reportDetailList.size(); iCnt++){
					 Map lineArr = (Map) reportDetailList.get(iCnt);
					 JSONObject reportDetails = new JSONObject();
					 
					 String trandate = (String)lineArr.get("TRANDATE");
					 String CustCode = (String)lineArr.get("CustCode");
					 String custname = (String)lineArr.get("CustName");
					 String bill = (String)lineArr.get("BILL");
					 String amount = (String)lineArr.get("AMOUNT");
					 String balance = (String)lineArr.get("BALANCE");
					 
					 reportDetails.put("trandate", trandate);
					 reportDetails.put("custcode", CustCode);
					 reportDetails.put("custname", custname);
					 reportDetails.put("bill", bill);
					 reportDetails.put("amount", amount);
					 reportDetails.put("balance", balance);
					 
					 ageingDetArray.add(reportDetails);
				 }
	            JSONObject reportContent = new JSONObject();
	            
	            
	            /* Company Details */
	            reportContent.put("fromAddress_CompanyName", CNAME);
	            reportContent.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
	            reportContent.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
	            reportContent.put("fromAddress_Country", CSTATE + " " +CCOUNTRY);
	            reportContent.put("fromAddress_ZIPCode", CZIP);
	            reportContent.put("fromAddress_RCBNO", CRCBNO);
	            reportContent.put("fromAddress_TpNo", CTEL);
	            reportContent.put("fromAddress_FaxNo", CFAX);
	            reportContent.put("fromAddress_ContactPersonName", CONTACTNAME);
	            reportContent.put("fromAddress_ContactPersonMobile", CHPNO);
	            reportContent.put("fromAddress_ContactPersonEmail", CEMAIL);
	            
	            /* Customer Details */
	            reportContent.put("To_ContactName", sContactName);
	            reportContent.put("To_CompanyName", sCustName);
	            reportContent.put("To_BlockAddress", sAddr1+"  " + sAddr2);
	            reportContent.put("To_RoadAddress", sAddr3 +"  " + sAddr4);
	            reportContent.put("To_Country", sCountry);
	            reportContent.put("To_ZIPCode", sZip);
	            
	            reportContent.put("statementNo", statementNo);	            
	            reportContent.put("CurDate", STATEMENT_DATE);
	            
	            /* Label Details */
	            reportContent.put("lblHeader", "To");
	            reportContent.put("lblstatement", "Statement Of Account");
	            reportContent.put("lblDate", "Date");
	            reportContent.put("lblstatementno", "Statement No");	            
	            reportContent.put("lblamtdue", "Amount Due");	            
	            reportContent.put("lblOrderNo", "Order No");
	            reportContent.put("lblAmount", "Amount");
	            reportContent.put("lblBalance", "Balance");
	            reportContent.put("lblcurrentdue", "Current Due");
	            reportContent.put("lbl30daysdue", "1-30 Days Past Due");
	            reportContent.put("lbl60daysdue", "31-60 Days Past Due");
	            reportContent.put("lbl90daysdue", "61-90 Days Past Due");
	            reportContent.put("lbl90plusdaysdue", "90+ Days Past Due");
	            if(ORDER.equalsIgnoreCase("outbound")){
	            	reportContent.put("lblCustCode", "Customer Id");
	            	reportContent.put("lblCustName", "Customer Name");
				}else{
					reportContent.put("lblCustCode", "Supplier Id");
					reportContent.put("lblCustName", "Supplier Name");
				}
	            
	            /* Aging by Days*/
	            reportContent.put("currentdue", currentdueamt);
	            reportContent.put("v30daysdue", days30due);
	            reportContent.put("v60daysdue", days60due);
	            reportContent.put("v90daysdue", days90due);
	            reportContent.put("v90plusdaysdue", days120due);
				reportContent.put("amountdue", totaldue);
				
				oCurrency=nCurrency;
				jsonArray.add(reportContent);
				ageingDetailsArray.add(ageingDetArray);
			 }
			 resultJson.put("reportContent", jsonArray);
			 resultJson.put("reportContentDetails", ageingDetailsArray);		
		}
		
		JSONObject resultJsonInt = new JSONObject();
        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
        resultJsonInt.put("ERROR_CODE", "100");
        jsonArrayErr.add(resultJsonInt);
        resultJson.put("errors", jsonArrayErr);
        
		} catch (Exception e) {
			jsonArray.add("");
    		resultJson.put("items", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		} 
        response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	private void getInventorysmryAgingDetailsByDays(HttpServletRequest request, 
			HttpServletResponse response) throws IOException, Exception {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        DateUtils _dateUtils =  new DateUtils();
       
        StrUtils strUtils = new StrUtils();
        String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", 
				CSTATE = "", CHPNO = "", CEMAIL = "",CRCBNO="",LOGIN_USER="",OrderStatus="",invoiceNo="";
      //  String PLANT="",ITEM="",ITEM_DESC="",PRD_CLS_ID="",PRD_TYPE_ID="",COND="",LOC="",BATCH=""   ;
        try {
        
           String PLANT = strUtils.fString(request.getParameter("PLANT"));
           String LOC = strUtils.fString(request.getParameter("LOC"));
           String ITEM = strUtils.fString(request.getParameter("ITEM"));
           String BATCH = strUtils.fString(request.getParameter("BATCH"));
           String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
           String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
           String PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
           String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
           String LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
           String LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
           String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
           String radiobtnval = strUtils.fString(request.getParameter("radiobtnval"));
           String FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
           String TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
           String STATEMENT_DATE     = StrUtils.fString(request.getParameter("STATEMENT_DATE"));
           //String frmDate = curDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
           String fdate = "", tdate = "";
           
           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	        String curDate =DateUtils.getDate();
//           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE="";
           
           if(STATEMENT_DATE.length()<0||STATEMENT_DATE==null||STATEMENT_DATE.equalsIgnoreCase(""))STATEMENT_DATE=curDate;
             
	        if (FROM_DATE.length()>5)
	        	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	       	if (TO_DATE.length()>5)
	        	tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
	       	
	       	Hashtable ht = new Hashtable();

            if(strUtils.fString(PLANT).length() > 0)               ht.put("CombinedResults.PLANT",PLANT);
            //if(strUtils.fString(LOC).length() > 0)                 ht.put("a.LOC",LOC);
            //if(strUtils.fString(BATCH).length() > 0)               ht.put("a.USERFLD4",BATCH);
            if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("c.PRD_CLS_ID",PRD_CLS_ID);
            if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("c.ITEMTYPE",PRD_TYPE_ID) ;  
            if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("c.PRD_BRAND_ID",PRD_BRAND_ID) ; 
            //if(strUtils.fString(LOC_TYPE_ID).length() > 0)         ht.put("isnull(C.LOC_TYPE_ID,'')",LOC_TYPE_ID);
            ArrayList invQryList= ageingUtil.getInvListAgingSummaryByCostWithStktake( PLANT,ITEM,PRD_DESCRIP,ht,LOC,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,fdate,tdate);
            //ArrayList invQryList= ageingUtil.getInvListAgingSummaryByCost( PLANT,ITEM,PRD_DESCRIP,ht,LOC,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,fdate,tdate);
            if (invQryList.size() > 0) {
            int iIndex = 0;
            int irow = 0;
            double sumprdQty = 0;
            double recQty = 0;
            double lastSumPrdQty = 0;
            String lastProduct="",lastDescription="",lastLocation="",lastBatch="",lastuom="";
            String item="",loc="",batch="",uom="",cost="",listprice="";
            double days30=0;double days60=0;double days90=0;double days120=0;
            double prevdays30=0;double prevdays60=0;double prevdays90=0;double prevdays120=0;
            curDate = curDate.substring(0,2) + curDate.substring(3,5) + curDate.substring(6);
            
            String minseq = "";
    		String sBatchSeq = "",statementNo="";
    		boolean insertFlag = false;
    	   TblControlDAO _TblControlDAO = new TblControlDAO();
    		_TblControlDAO.setmLogger(mLogger);
    		Hashtable htab = new Hashtable();

    		String query = " isnull(NXTSEQ,'') as NXTSEQ";
    		htab.put(IDBConstants.PLANT, PLANT);
    		htab.put(IDBConstants.TBL_FUNCTION, "PAYMENT");
    		
    			boolean exitFlag = false;
    			boolean resultflag = false;
    			exitFlag = _TblControlDAO.isExisit(htab, "", PLANT);

    			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
    			if (exitFlag == false) {

    				Map htInsert = null;
    				Hashtable htTblCntInsert = new Hashtable();
    				htTblCntInsert.put(IDBConstants.PLANT, PLANT);
    				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
    				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PMT");
    				htTblCntInsert.put("MINSEQ", "0000");
    				htTblCntInsert.put("MAXSEQ", "9999");
    				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
    				htTblCntInsert.put(IDBConstants.CREATED_BY, LOGIN_USER);
    				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
    				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, PLANT);

    				statementNo = "1";
    			} else {
    				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

    				Map m1 = _TblControlDAO.selectRow(query, htab, "");
    				sBatchSeq = (String) m1.get("NXTSEQ");
    				System.out.println("length" + sBatchSeq.length());

    				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

    				String updatedSeq = Integer.toString(inxtSeq);
    				
    				Map htUpdate = null;

    				Hashtable htTblCntUpdate = new Hashtable();
    				htTblCntUpdate.put(IDBConstants.PLANT, PLANT);
    				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
    				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PMT");
    				StringBuffer updateQyery = new StringBuffer("set ");
    				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

    				boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", PLANT);
    				statementNo = updatedSeq;
			}
    			
    			ArrayList listQry = ageingUtil.getPlantMstDetails(PLANT);
       			for (int i = 0; i < listQry.size(); i++) {
       				Map map = (Map) listQry.get(i);
       				CNAME = (String) map.get("PLNTDESC");
       				CADD1 = (String) map.get("ADD1");
       				CADD2 = (String) map.get("ADD2");
       				CADD3 = (String) map.get("ADD3");
       				CADD4 = (String) map.get("ADD4");
       				CCOUNTRY = (String) map.get("COUNTY");
       				CZIP = (String) map.get("ZIP");
       				CTEL = (String) map.get("TELNO");
       				CFAX = (String) map.get("FAX");
       				CONTACTNAME = (String) map.get("NAME");
       				CHPNO = (String) map.get("HPNO");
       				CEMAIL = (String) map.get("EMAIL");
       				CRCBNO = (String) map.get("RCBNO");

       			}
       			JSONObject reportContent = new JSONObject();
       			/* Company Details */
	            reportContent.put("fromAddress_CompanyName", CNAME);
	            reportContent.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
	            reportContent.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
	            reportContent.put("fromAddress_Country", CSTATE + " " +CCOUNTRY);
	            reportContent.put("fromAddress_ZIPCode", CZIP);
	            reportContent.put("fromAddress_RCBNO", CRCBNO);
	            reportContent.put("fromAddress_TpNo", CTEL);
	            reportContent.put("fromAddress_FaxNo", CFAX);
	            reportContent.put("fromAddress_ContactPersonName", CONTACTNAME);
	            reportContent.put("fromAddress_ContactPersonMobile", CHPNO);
	            reportContent.put("fromAddress_ContactPersonEmail", CEMAIL);
	            
	            reportContent.put("statementNo", statementNo);	            
	            reportContent.put("CurDate", STATEMENT_DATE);
	            
	            /* Label Details */
	            reportContent.put("lblDate", "Date");
	            reportContent.put("lblstatementno", "Statement No");
	            reportContent.put("lblstatement", "Statement Of Account");
    			
            for (int iCnt =0; iCnt<invQryList.size(); iCnt++){             
                             
				String result="";
				JSONObject reportDetails = new JSONObject();
				
				Map lineArr = (Map) invQryList.get(iCnt);
				sumprdQty = sumprdQty + Double.parseDouble((String)lineArr.get("QTY"));
				
				JSONObject resultJsonInt = new JSONObject();
				            
				String recdate = strUtils.fString((String)lineArr.get("RECVDATE"));
				item = (String)lineArr.get("ITEM");
				uom=new ItemMstDAO().getInvUOM(PLANT,item);
				loc= (String)lineArr.get("LOC");
				batch = (String)lineArr.get("BATCH");
				cost =  (String)lineArr.get("COST");
				listprice = (String)lineArr.get("UnitPrice");
				sumprdQty = Double.parseDouble((String)lineArr.get("QTY"));
				recQty = Double.parseDouble((String)lineArr.get("RECQTY"));
				recdate = recdate.substring(8,10) + recdate.substring(5,7) + recdate.substring(0,4); //new
//				recdate = recdate.substring(0,2) + recdate.substring(3,5) + recdate.substring(6); //old
				Calendar cal1 = new GregorianCalendar();
				Calendar cal2 = new GregorianCalendar();
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				Date d1 = sdf.parse(recdate);
				cal1.setTime(d1);
				Date d2 = sdf.parse(curDate);
				cal2.setTime(d2);         		               
				Long diff = d2.getTime()-d1.getTime();         		               
				int days =  (int) (diff/(1000 * 60 * 60 * 24));
				/*if(radiobtnval.equalsIgnoreCase("M")){*/
					if(days>0 && days<=30)
					{
						days30 = days30 + recQty;
					}
				   if(days>30 && days<=60)
				   {
					   days60 = days60 + recQty;
				   }
            	   if(days>60 && days<=90)
            	   {
            		   days90 = days90 + recQty;
            	   }
            	   if(days>90)
            	   {
            		   days120 = days120 + recQty;
            	   }
                /*}else{
            	  if(days>0 && days<=365)
            	   {
            		   days30 = days30 + recQty;
            	   }
            	   if(days>365 && days<=730)
            	   {
            		   days60 = days60 + recQty;
            	   }
            	   if(days>730 && days<=1095)
            	   {
            		   days90 = days90 + recQty;
            	   }
            	   if(days>1095)
            	   {
            		   days120 = days120 + recQty;
            	   }
	               }*/
            	   
       			
                   if(!(lastProduct.equalsIgnoreCase(item)&&lastLocation.equalsIgnoreCase(loc)&&lastBatch.equalsIgnoreCase(batch)&&lastSumPrdQty == sumprdQty)){
                	   String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                        	   
            	   if(!lastProduct.equalsIgnoreCase("")&&!lastLocation.equalsIgnoreCase("")&&!lastLocation.equalsIgnoreCase("")){
            		   irow=irow+1;
            		   if(prevdays30>lastSumPrdQty){
            			   prevdays30 = lastSumPrdQty;
            		   }
            		   if((prevdays30+prevdays60)>lastSumPrdQty){
            			   prevdays60 = lastSumPrdQty-prevdays30;
            		   }
            		   if((prevdays30+prevdays60+prevdays90)>lastSumPrdQty){
            			   prevdays90 = lastSumPrdQty-prevdays30-prevdays60;
            		   }
            		   if((prevdays30+prevdays60+prevdays90+prevdays120)>lastSumPrdQty){
            			   prevdays120 = lastSumPrdQty-prevdays30-prevdays60-prevdays90;
            		   }
            		   
            		   reportDetails.put("product", strUtils.fString(StrUtils.forHTMLTag(lastProduct)));
            		   reportDetails.put("description", strUtils.fString(StrUtils.forHTMLTag(lastDescription)));
            		   reportDetails.put("COST", strUtils.fString(StrUtils.forHTMLTag(cost)));
            		   reportDetails.put("UnitPrice", strUtils.fString(StrUtils.forHTMLTag(listprice)));
            		   reportDetails.put("uom", strUtils.fString(StrUtils.forHTMLTag(lastuom)));
            		   reportDetails.put("location", strUtils.fString(StrUtils.forHTMLTag(lastLocation)));
            		   reportDetails.put("batch", strUtils.fString(StrUtils.forHTMLTag(lastBatch)));
//            		   reportDetails.put("qty", strUtils.fString(StrUtils.forHTMLTag(Double.toString(lastSumPrdQty))));
            		   reportDetails.put("qty", Numbers.toMillionFormat(lastSumPrdQty,3));
            		   reportDetails.put("prevdays30", Numbers.toMillionFormat(prevdays30,3));
            		   reportDetails.put("prevdays60", Numbers.toMillionFormat(prevdays60,3));
            		   reportDetails.put("prevdays90", Numbers.toMillionFormat(prevdays90,3));
            		   reportDetails.put("prevdays120", Numbers.toMillionFormat(prevdays120,3));

            		   days30=0;days60=0;days90=0;days120=0;
            		   //prevdays30=0;prevdays60=0;prevdays90=0;prevdays120=0;
            		  /* if(radiobtnval.equalsIgnoreCase("M"))
 		               {*/
            		   if(days>0 && days<=30)
	            	   {
	            		   days30 = days30 + recQty;
	            	   }
	            	   if(days>30 && days<=60)
	            	   {
	            		   days60 = days60 + recQty;
	            	   }
	            	   if(days>60 && days<=90)
	            	   {
	            		   days90 = days90 + recQty;
	            	   }
	            	   if(days>90)
	            	   {
	            		   days120 = days120 + recQty;
	            	   }
	            	   /*}
            		   else{
            			   if(days>0 && days<=365)
   		            	   {
   		            		   days30 = days30 + recQty;
   		            	   }
   		            	   if(days>365 && days<=730)
   		            	   {
   		            		   days60 = days60 + recQty;
   		            	   }
   		            	   if(days>730 && days<=1095)
   		            	   {
   		            		   days90 = days90 + recQty;
   		            	   }
   		            	   if(days>1095)
   		            	   {
   		            		   days120 = days120 + recQty;
   		            	   }
            		   }*/
	            	   jsonArray.add(reportDetails);	            	   
            	   }            	   
               }
               if(iIndex+1 == invQryList.size()){
        		   irow=irow+1;
            	  String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
            	   if(days30>sumprdQty){
     			   days30 = sumprdQty;
         		   }
            	   if((days30+days60)>sumprdQty){
         			   days60 = sumprdQty-days30;
         		   }
            	   if((days30+days60+days90)>sumprdQty){
         			   days90 = sumprdQty-days30-days60;
         		   }
            	   if((days30+days60+days90+days120)>sumprdQty){
         			   days120 = sumprdQty-days30-days60-days90;
         		   }
            	   
            	   reportDetails.put("product", strUtils.fString(StrUtils.forHTMLTag(item)));
        		   reportDetails.put("description", strUtils.fString(StrUtils.forHTMLTag((String)lineArr.get("ITEMDESC"))));
        		   reportDetails.put("COST", strUtils.fString(StrUtils.forHTMLTag(cost)));
        		   reportDetails.put("UnitPrice", strUtils.fString(StrUtils.forHTMLTag(listprice)));
        		   reportDetails.put("uom", strUtils.fString(StrUtils.forHTMLTag(uom)));
        		   reportDetails.put("location", strUtils.fString(StrUtils.forHTMLTag(loc)));
        		   reportDetails.put("batch", strUtils.fString(StrUtils.forHTMLTag(batch)));
        		   reportDetails.put("qty", Numbers.toMillionFormat(sumprdQty,3));
        		   reportDetails.put("prevdays30", Numbers.toMillionFormat(days30,3));
        		   reportDetails.put("prevdays60", Numbers.toMillionFormat(days60,3));
        		   reportDetails.put("prevdays90", Numbers.toMillionFormat(days90,3));
        		   reportDetails.put("prevdays120", Numbers.toMillionFormat(days120,3));
        		   jsonArray.add(reportDetails);
               }
                         
                 iIndex=iIndex+1;
                 lastProduct = item;
                 lastLocation= loc;
                 lastuom= uom;
                 lastBatch = batch;
                 lastSumPrdQty = sumprdQty;
                 lastDescription = (String)lineArr.get("ITEMDESC");
                 prevdays30 = days30;
                 prevdays60 = days60;
                 prevdays90 = days90;
                 prevdays120 = days120;
             
                

                }
                resultJson.put("INVDETAILS", jsonArray);
                resultJson.put("reportContent", reportContent);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
            } else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
                resultJson.put("INVDETAILS", jsonArray);
                resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	/* New Method*/
	public void PrintCustomerAgeingReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		HttpSession session = request.getSession();
		String LOGIN_USER="",ORDER="",ordRefNo="",custName="",STATEMENT_DATE="",
				FROM_DATE="",TO_DATE="",Orderno="",invoiceNo="",fdate="",tdate="",numberOfDecimal="2";
		
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray ageingDetailsArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
		try {
		String PLANT = (String) session.getAttribute("PLANT");
		LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		ORDER=StrUtils.fString(request.getParameter("Order"));
        ordRefNo = StrUtils.fString(request.getParameter("ordRefNo"));
        custName = StrUtils.fString(request.getParameter("custName"));
        STATEMENT_DATE = StrUtils.fString(request.getParameter("STATEMENT_DATE"));
        FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
        TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
        Orderno = StrUtils.fString(request.getParameter("ORDERNO"));
        invoiceNo = StrUtils.fString(request.getParameter("NAME"));
        
        String curDate =DateUtils.getDate();
        //if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;        
        if(STATEMENT_DATE.length()<0||STATEMENT_DATE==null||STATEMENT_DATE.equalsIgnoreCase(""))STATEMENT_DATE=curDate;
        if (FROM_DATE.length()>5)
        	fdate = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
       	if (TO_DATE.length()>5)
        	tdate = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
       	ArrayList arrCustomers = new ArrayList();
       	/*ArrayList listQry = ageingUtil.getPlantMstDetails(PLANT);
		for (int i = 0; i < listQry.size(); i++) {
			Map map = (Map) listQry.get(i);
			CNAME = (String) map.get("PLNTDESC");
			CADD1 = (String) map.get("ADD1");
			CADD2 = (String) map.get("ADD2");
			CADD3 = (String) map.get("ADD3");
			CADD4 = (String) map.get("ADD4");
			CSTATE = (String) map.get("STATE");
			CCOUNTRY = (String) map.get("COUNTY");
			CZIP = (String) map.get("ZIP");
			CTEL = (String) map.get("TELNO");
			CFAX = (String) map.get("FAX");
			CONTACTNAME = (String) map.get("NAME");
			CHPNO = (String) map.get("HPNO");
			CEMAIL = (String) map.get("EMAIL");
			CRCBNO = (String) map.get("RCBNO");
		}*/
		
		Hashtable htCondition = new Hashtable();
		htCondition.put("PLANT", PLANT);
		String plntQuery = "PLANT,PLNTDESC, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE, CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE,CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,NAME,DESGINATION,TELNO,HPNO,EMAIL,ISNULL(ADD1,'') as ADD1,ISNULL(ADD2,'') as ADD2,ISNULL(ADD3,'') as ADD3,ISNULL(ADD4,'') as ADD4,COUNTY,ZIP,isnull(USERFLD1,'') AS REMARKS,USERFLD2 AS FAX,RCBNO,ISNULL(WEBSITE,'')WEBSITE,ISNULL(companyregnumber,'')companyregnumber,GSTREGNO,SALES_CHARGE_BY,SALES_PERCENT,SALES_FR_DOLLARS,SALES_FR_CENTS,ENQUIRY_FR_DOLLARS,ENQUIRY_FR_CENTS,NUMBER_OF_CATALOGS,BASE_CURRENCY,ISNULL(NUMBER_OF_SIGNATURES,'') NUMBER_OF_SIGNATURES,ISNULL(TAXBY,'') TAXBY,ISNULL(TAXBYLABEL,'') TAXBYLABEL, ISNULL(TAXBYLABELORDERMANAGEMENT,'') TAXBYLABELORDERMANAGEMENT,ISNULL(STATE,'') STATE,ENABLE_INVENTORY, ENABLE_ACCOUNTING,ISNULL(NUMBEROFDECIMAL,'2') NUMBEROFDECIMAL,ISNULL(ISTAXREGISTRED,'0') ISTAXREGISTRED,REPROTSBASIS,CONVERT(VARCHAR(20), CONVERT(DATETIME, FISCAL_YEAR), 103) AS FISCALYEAR, CONVERT(VARCHAR(20), CONVERT(DATETIME, PAYROLL_YEAR), 103) AS PAYROLLYEAR,ISNULL(ENABLE_PAYROLL,0) AS ENABLE_PAYROLL,REGION,ISNULL((SELECT COUNTRY_CODE FROM COUNTRYMASTER WHERE COUNTRYNAME=COUNTY),'') COUNTRY_CODE,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ID,ISNULL(EMPLOYEEWORKINGMANDAYSBY,'') EMPLOYEEWORKINGMANDAYSBY,ISNULL(SEALNAME,'') SEALNAME,ISNULL(SIGNATURENAME,'') SIGNATURENAME,ISNULL(NUMBER_OF_SUPPLIER,'') NUMBER_OF_SUPPLIER,ISNULL(NUMBER_OF_CUSTOMER,'') NUMBER_OF_CUSTOMER,ISNULL(NUMBER_OF_EMPLOYEE,'') NUMBER_OF_EMPLOYEE,ISNULL(NUMBER_OF_INVENTORY,'') NUMBER_OF_INVENTORY ,ISNULL(NUMBER_OF_LOCATION,'') NUMBER_OF_LOCATION ,ISNULL(NUMBER_OF_ORDER,'') NUMBER_OF_ORDER";
		Map plantDetail = new PlantMstDAO().selectRow(plntQuery, htCondition);
		
		//arrCustomers =  ageingUtil.getcustomerorsuppliername(PLANT, fdate, tdate, "INVOICE", custName);
		arrCustomers =  ageingUtil.getcustomerorsuppliernamegroupby(PLANT, fdate, tdate, "INVOICE", custName);
		
		String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
		File checkImageFile = new File(imagePath);
		if (!checkImageFile.exists()) {
			imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		}
		
		for(int i=0; i<arrCustomers.size();i++) {
			Map customer = (Map)arrCustomers.get(i);
			boolean insertFlag = false;
			String statementNo = "", sBatchSeq = "";
			double notDue =0, days30=0, days60=0, days90=0, days90Plus=0, totaldue=0;
			ArrayList reportDetailList  = new ArrayList();
			JSONArray ageingDetArray = new JSONArray();
			
			htCondition = new Hashtable();
			htCondition.put("PLANT", PLANT);
			htCondition.put("CUSTNO", (String)customer.get("custcode"));
			Map custDetail = new CustomerBeanDAO().getCustomerDetail(htCondition, PLANT);
			
			htCondition = new Hashtable();
     		String query = " isnull(NXTSEQ,'') as NXTSEQ";
     		htCondition.put(IDBConstants.PLANT, PLANT);
     		htCondition.put(IDBConstants.TBL_FUNCTION, "PAYMENT");
     		
 			boolean exitFlag = false;
 			boolean resultflag = false;
 			exitFlag = new TblControlDAO().isExisit(htCondition, "", PLANT);

 			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
 			if (exitFlag == false) {
 				Map htInsert = null;
 				Hashtable htTblCntInsert = new Hashtable();
 				htTblCntInsert.put(IDBConstants.PLANT, PLANT);
 				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
 				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PMT");
 				htTblCntInsert.put("MINSEQ", "0000");
 				htTblCntInsert.put("MAXSEQ", "9999");
 				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
 				htTblCntInsert.put(IDBConstants.CREATED_BY, LOGIN_USER);
 				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
 				insertFlag = new TblControlDAO().insertTblControl(htTblCntInsert, PLANT);

 				statementNo = "1";
 			} else {
 				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

 				Map m1 = new TblControlDAO().selectRow(query, htCondition, "");
 				sBatchSeq = (String) m1.get("NXTSEQ");
 				System.out.println("length" + sBatchSeq.length());

 				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

 				String updatedSeq = Integer.toString(inxtSeq);
 				
 				Map htUpdate = null;

 				Hashtable htTblCntUpdate = new Hashtable();
 				htTblCntUpdate.put(IDBConstants.PLANT, PLANT);
 				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
 				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PMT");
 				StringBuffer updateQyery = new StringBuffer("set ");
 				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

 				boolean updateFlag = new TblControlDAO().update(updateQyery.toString(), htTblCntUpdate, "", PLANT);
 				statementNo = updatedSeq;
 			}
 			htCondition = new Hashtable();
 			htCondition.put("PLANT", PLANT);
			htCondition.put("CUSTNO", (String)customer.get("custcode"));
			htCondition.put("FROMDATE", fdate);
			htCondition.put("TODATE", tdate);
			
 			//Map agingDetail = ageingUtil.getAgingSummaryForReport(htCondition);
 			Map agingDetail = ageingUtil.getAgingSummaryForReportBYCURRENCY(htCondition,(String)customer.get("currency"));
 			notDue = Double.parseDouble((String)agingDetail.get("DAY0"));
 			days30 = Double.parseDouble((String)agingDetail.get("DAY1"));
 			days60 = Double.parseDouble((String)agingDetail.get("DAY2"));
 			days90 = Double.parseDouble((String)agingDetail.get("DAY3"));
 			days90Plus = Double.parseDouble((String)agingDetail.get("DAY4"));
 			totaldue = Double.parseDouble((String)agingDetail.get("TOTAL_DUE"));
 			
 			reportDetailList = ageingUtil.getAgingDetailForReportbycurrency(htCondition,(String)customer.get("currency"));
 			
 			for (int iCnt =0; iCnt<reportDetailList.size(); iCnt++){
				 Map lineArr = (Map) reportDetailList.get(iCnt);
				 JSONObject reportDetails = new JSONObject();
				 
				 String invDate = (String)lineArr.get("INVOICE_DATE");
				 String invoice = (String)lineArr.get("INVOICE");
				 String dueDate = (String)lineArr.get("DUE_DATE");
				 String amount = (String)lineArr.get("CONV_INVOICE_AMOUNT");
				 String received = (String)lineArr.get("CONV_RECEIVED");
				 String overDue = (String)lineArr.get("OVER_DUE");
				 String outStanding = (String)lineArr.get("CONV_OUTSTANDING");
				 String currency = (String)lineArr.get("CURRENCY");
				 String cummBal = (String)lineArr.get("CUMM_BAL");
				 
				 reportDetails.put("invDate", invDate);
				 reportDetails.put("invoice", invoice);
				 reportDetails.put("dueDate", dueDate);
				 reportDetails.put("currency", currency);
				 reportDetails.put("amount", Numbers.toMillionFormat(amount,numberOfDecimal));
				 reportDetails.put("received", Numbers.toMillionFormat(received,numberOfDecimal));
				 reportDetails.put("overDue", Numbers.toMillionFormat(overDue,numberOfDecimal));
				 reportDetails.put("outStanding", Numbers.toMillionFormat(outStanding,numberOfDecimal));
				 reportDetails.put("cummBal", Numbers.toMillionFormat(cummBal,numberOfDecimal));
				 
				 ageingDetArray.add(reportDetails);
			 }

 			JSONObject reportContent = new JSONObject();
	       /** Company Details **/ 
	       reportContent.put("fromAddress_CompanyName", (String) plantDetail.get("PLNTDESC"));
	       reportContent.put("fromAddress_CompanyName", (String) plantDetail.get("PLNTDESC"));
	       reportContent.put("fromAddress_BlockAddress", (String) plantDetail.get("ADD1") + " " + (String) plantDetail.get("ADD2"));
	       reportContent.put("fromAddress_RoadAddress", (String) plantDetail.get("ADD3") + " " + (String) plantDetail.get("ADD4"));
	       reportContent.put("fromAddress_Country", (String) plantDetail.get("STATE") + " " +(String) plantDetail.get("COUNTY"));
	       reportContent.put("fromAddress_ZIPCode", (String) plantDetail.get("ZIP"));
	       reportContent.put("fromAddress_RCBNO", (String) plantDetail.get("RCBNO"));
	       reportContent.put("fromAddress_TpNo", (String) plantDetail.get("TELNO"));
	       reportContent.put("fromAddress_FaxNo", (String) plantDetail.get("FAX"));
	       reportContent.put("fromAddress_ContactPersonName", (String) plantDetail.get("NAME"));
	       reportContent.put("fromAddress_ContactPersonMobile", (String) plantDetail.get("HPNO"));
	       reportContent.put("fromAddress_ContactPersonEmail", (String) plantDetail.get("EMAIL"));
	       
	       /* Customer Details */
	       reportContent.put("To_ContactName", (String) custDetail.get("NAME"));
	       reportContent.put("To_CompanyName", (String) custDetail.get("CNAME"));
	       reportContent.put("To_BlockAddress", (String) custDetail.get("ADDR1")+"  " + (String) custDetail.get("ADDR2"));
	       reportContent.put("To_RoadAddress", (String) custDetail.get("ADDR3") +"  " + (String) custDetail.get("ADDR4"));
	       reportContent.put("To_Country", (String) custDetail.get("COUNTRY"));
	       reportContent.put("To_ZIPCode", (String) custDetail.get("ZIP"));
	       
	       reportContent.put("statementNo", statementNo);	            
	       reportContent.put("CurDate", STATEMENT_DATE);
	       
	       /* Label Details */
	       reportContent.put("lblHeader", "To");
	       reportContent.put("lblstatement", "Statement Of Account with aging");
	       reportContent.put("lblDate", "Date");
	       reportContent.put("lblstatementno", "Statement No");	            
	       reportContent.put("lblamtdue", "Amount Due");	            
	       reportContent.put("lblOrderNo", "Inv No.");
	       reportContent.put("lblDueDate", "Due Date");
	       reportContent.put("lblAmount", "Inv Amount");
	       reportContent.put("lblReceived", "Received");
	       reportContent.put("lblOverDueDays", "Over Due Days");
	       reportContent.put("lblBalance", "Outstanding");
	       reportContent.put("lblCummBalance", "Cumm. Bal");
	       reportContent.put("lblTotalOS", "Total O/s");
	       reportContent.put("lblcurrentdue", "Not Due");
	       reportContent.put("lbl30daysdue", "1-30 days");
	       reportContent.put("lbl60daysdue", "30-60 days");
	       reportContent.put("lbl90daysdue", "60-90 days");
	       reportContent.put("lbl90plusdaysdue", "90 & above");
	       
	       /* Aging by Days*/
	       reportContent.put("notDue", Numbers.toMillionFormat(notDue,numberOfDecimal));
	       reportContent.put("v30daysdue", Numbers.toMillionFormat(days30,numberOfDecimal));
	       reportContent.put("v60daysdue", Numbers.toMillionFormat(days60,numberOfDecimal));
	       reportContent.put("v90daysdue", Numbers.toMillionFormat(days90,numberOfDecimal));
	       reportContent.put("v90plusdaysdue", Numbers.toMillionFormat(days90Plus,numberOfDecimal));
	       reportContent.put("amountdue", Numbers.toMillionFormat(totaldue,numberOfDecimal));
	       
	       /* Aging CURRENCY*/
	       
	       reportContent.put("currency", (String)customer.get("currency"));
			
	       jsonArray.add(reportContent);
	       ageingDetailsArray.add(ageingDetArray);
	       
	       resultJson.put("reportContent", jsonArray);
	       resultJson.put("reportContentDetails", ageingDetailsArray);
		}
		JSONObject resultJsonInt = new JSONObject();
        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
        resultJsonInt.put("ERROR_CODE", "100");
        jsonArrayErr.add(resultJsonInt);
        resultJson.put("errors", jsonArrayErr);        
		} catch (Exception e) {
			jsonArray.add("");
    		resultJson.put("items", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		} 
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	public void getCustomerAgeingSummary(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		HttpSession session = request.getSession();
		String LOGIN_USER="",ORDER="",ordRefNo="",custName="",STATEMENT_DATE="",
				FROM_DATE="",TO_DATE="",Orderno="",invoiceNo="",fdate="",tdate="";
		
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray ageingDetailsArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
		try {
		String PLANT = (String) session.getAttribute("PLANT");
		LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		ORDER=StrUtils.fString(request.getParameter("Order"));
        ordRefNo = StrUtils.fString(request.getParameter("ordRefNo"));
        custName = StrUtils.fString(request.getParameter("custName"));
        STATEMENT_DATE = StrUtils.fString(request.getParameter("STATEMENT_DATE"));
        FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
        TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
        Orderno = StrUtils.fString(request.getParameter("ORDERNO"));
        invoiceNo = StrUtils.fString(request.getParameter("NAME"));
        
        String curDate =DateUtils.getDate();
        //if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;        
        if(STATEMENT_DATE.length()<0||STATEMENT_DATE==null||STATEMENT_DATE.equalsIgnoreCase(""))STATEMENT_DATE=curDate;
        if (FROM_DATE.length()>5)
        	fdate = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
       	if (TO_DATE.length()>5)
        	tdate = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
       	ArrayList arrCustomers = new ArrayList();
       	String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
		
		Hashtable htCondition = new Hashtable();
		htCondition.put("PLANT", PLANT);
		String plntQuery = "PLANT,PLNTDESC, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE, CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE,CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,NAME,DESGINATION,TELNO,HPNO,EMAIL,ISNULL(ADD1,'') as ADD1,ISNULL(ADD2,'') as ADD2,ISNULL(ADD3,'') as ADD3,ISNULL(ADD4,'') as ADD4,COUNTY,ZIP,isnull(USERFLD1,'') AS REMARKS,USERFLD2 AS FAX,RCBNO,ISNULL(WEBSITE,'')WEBSITE,ISNULL(companyregnumber,'')companyregnumber,GSTREGNO,SALES_CHARGE_BY,SALES_PERCENT,SALES_FR_DOLLARS,SALES_FR_CENTS,ENQUIRY_FR_DOLLARS,ENQUIRY_FR_CENTS,NUMBER_OF_CATALOGS,BASE_CURRENCY,ISNULL(NUMBER_OF_SIGNATURES,'') NUMBER_OF_SIGNATURES,ISNULL(TAXBY,'') TAXBY,ISNULL(TAXBYLABEL,'') TAXBYLABEL, ISNULL(TAXBYLABELORDERMANAGEMENT,'') TAXBYLABELORDERMANAGEMENT,ISNULL(STATE,'') STATE,ENABLE_INVENTORY, ENABLE_ACCOUNTING,ISNULL(NUMBEROFDECIMAL,'2') NUMBEROFDECIMAL,ISNULL(ISTAXREGISTRED,'0') ISTAXREGISTRED,REPROTSBASIS,CONVERT(VARCHAR(20), CONVERT(DATETIME, FISCAL_YEAR), 103) AS FISCALYEAR, CONVERT(VARCHAR(20), CONVERT(DATETIME, PAYROLL_YEAR), 103) AS PAYROLLYEAR,ISNULL(ENABLE_PAYROLL,0) AS ENABLE_PAYROLL,REGION,ISNULL((SELECT COUNTRY_CODE FROM COUNTRYMASTER WHERE COUNTRYNAME=COUNTY),'') COUNTRY_CODE,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ID,ISNULL(EMPLOYEEWORKINGMANDAYSBY,'') EMPLOYEEWORKINGMANDAYSBY,ISNULL(SEALNAME,'') SEALNAME,ISNULL(SIGNATURENAME,'') SIGNATURENAME,ISNULL(NUMBER_OF_SUPPLIER,'') NUMBER_OF_SUPPLIER,ISNULL(NUMBER_OF_CUSTOMER,'') NUMBER_OF_CUSTOMER,ISNULL(NUMBER_OF_EMPLOYEE,'') NUMBER_OF_EMPLOYEE,ISNULL(NUMBER_OF_INVENTORY,'') NUMBER_OF_INVENTORY ,ISNULL(NUMBER_OF_LOCATION,'') NUMBER_OF_LOCATION ,ISNULL(NUMBER_OF_ORDER,'') NUMBER_OF_ORDER";
		Map plantDetail = new PlantMstDAO().selectRow(plntQuery, htCondition);
		
		arrCustomers =  ageingUtil.getcustomerorsuppliername(PLANT, fdate, tdate, "INVOICE", custName);
		
		String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
		File checkImageFile = new File(imagePath);
		if (!checkImageFile.exists()) {
			imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		}
		JSONArray ageingDetArray = new JSONArray();
		int index=0;
		for(int i=0; i<arrCustomers.size();i++) {
			Map customer = (Map)arrCustomers.get(i);
			ArrayList reportDetailList  = new ArrayList();
			
			
			htCondition = new Hashtable();
			htCondition.put("PLANT", PLANT);
			htCondition.put("CUSTNO", (String)customer.get("custcode"));
			Map custDetail = new CustomerBeanDAO().getCustomerDetail(htCondition, PLANT);

 			htCondition = new Hashtable();
 			htCondition.put("PLANT", PLANT);
			htCondition.put("CUSTNO", (String)customer.get("custcode"));
			htCondition.put("FROMDATE", fdate);
			htCondition.put("TODATE", tdate);
 			
 			reportDetailList = ageingUtil.getAgingDetailForReport(htCondition);
 			
 			for (int iCnt =0; iCnt<reportDetailList.size(); iCnt++){
				 Map lineArr = (Map) reportDetailList.get(iCnt);
				 JSONObject reportDetails = new JSONObject();
				 
				 String invDate = (String)lineArr.get("INVOICE_DATE");
				 String invoice = (String)lineArr.get("INVOICE");
				 String dueDate = (String)lineArr.get("DUE_DATE");
				 String amount = (String)lineArr.get("CONV_INVOICE_AMOUNT");
				 String received = (String)lineArr.get("CONV_RECEIVED");
				 String overDue = (String)lineArr.get("OVER_DUE");
				 String outStanding = (String)lineArr.get("CONV_OUTSTANDING");
				 String currency = (String)lineArr.get("CURRENCY");
				 
				 reportDetails.put("index", ++index);
				 reportDetails.put("custName", (String)customer.get("custname"));
				 reportDetails.put("invDate", invDate);
				 reportDetails.put("invoice", invoice);
				 reportDetails.put("dueDate", dueDate);
				 reportDetails.put("currency", currency);
				 reportDetails.put("amount", StrUtils.addZeroes(Double.parseDouble(amount),numberOfDecimal));
				 reportDetails.put("received", received);
				 reportDetails.put("overDue", overDue);
				 reportDetails.put("outStanding", StrUtils.addZeroes(Double.parseDouble(outStanding),numberOfDecimal));
				 reportDetails.put("key", (String)customer.get("custname")+"_"+currency);
				 
				 ageingDetArray.add(reportDetails);
			 } 			
	       ageingDetailsArray.add(ageingDetArray);	       
		}
		resultJson.put("items", ageingDetArray);
		JSONObject resultJsonInt = new JSONObject();
        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
        resultJsonInt.put("ERROR_CODE", "100");
        jsonArrayErr.add(resultJsonInt);
        resultJson.put("errors", jsonArrayErr);        
		} catch (Exception e) {
			jsonArray.add("");
    		resultJson.put("items", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		} 
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	public void PrintCustomerConsolidatedAgeingReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		HttpSession session = request.getSession();
		String LOGIN_USER="",ORDER="",ordRefNo="",custName="",STATEMENT_DATE="",
				FROM_DATE="",TO_DATE="",Orderno="",invoiceNo="",fdate="",tdate="",numberOfDecimals="2";
		
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray ageingDetailsArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
		try {
		String PLANT = (String) session.getAttribute("PLANT");
		LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		ORDER=StrUtils.fString(request.getParameter("Order"));
        ordRefNo = StrUtils.fString(request.getParameter("ordRefNo"));
        custName = StrUtils.fString(request.getParameter("custName"));
        STATEMENT_DATE = StrUtils.fString(request.getParameter("STATEMENT_DATE"));
        FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
        TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
        Orderno = StrUtils.fString(request.getParameter("ORDERNO"));
        invoiceNo = StrUtils.fString(request.getParameter("NAME"));
        
        String curDate =DateUtils.getDate();
        //if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;        
        if(STATEMENT_DATE.length()<0||STATEMENT_DATE==null||STATEMENT_DATE.equalsIgnoreCase(""))STATEMENT_DATE=curDate;
        if (FROM_DATE.length()>5)
        	fdate = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
       	if (TO_DATE.length()>5)
        	tdate = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
       	ArrayList arrCustomers = new ArrayList();
		
		Hashtable htCondition = new Hashtable();
		htCondition.put("PLANT", PLANT);
		String plntQuery = "PLANT,PLNTDESC, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE, CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE,CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,NAME,DESGINATION,TELNO,HPNO,EMAIL,ISNULL(ADD1,'') as ADD1,ISNULL(ADD2,'') as ADD2,ISNULL(ADD3,'') as ADD3,ISNULL(ADD4,'') as ADD4,COUNTY,ZIP,isnull(USERFLD1,'') AS REMARKS,USERFLD2 AS FAX,RCBNO,ISNULL(WEBSITE,'')WEBSITE,ISNULL(companyregnumber,'')companyregnumber,GSTREGNO,SALES_CHARGE_BY,SALES_PERCENT,SALES_FR_DOLLARS,SALES_FR_CENTS,ENQUIRY_FR_DOLLARS,ENQUIRY_FR_CENTS,NUMBER_OF_CATALOGS,BASE_CURRENCY,ISNULL(NUMBER_OF_SIGNATURES,'') NUMBER_OF_SIGNATURES,ISNULL(TAXBY,'') TAXBY,ISNULL(TAXBYLABEL,'') TAXBYLABEL, ISNULL(TAXBYLABELORDERMANAGEMENT,'') TAXBYLABELORDERMANAGEMENT,ISNULL(STATE,'') STATE,ENABLE_INVENTORY, ENABLE_ACCOUNTING,ISNULL(NUMBEROFDECIMAL,'2') NUMBEROFDECIMAL,ISNULL(ISTAXREGISTRED,'0') ISTAXREGISTRED,REPROTSBASIS,CONVERT(VARCHAR(20), CONVERT(DATETIME, FISCAL_YEAR), 103) AS FISCALYEAR, CONVERT(VARCHAR(20), CONVERT(DATETIME, PAYROLL_YEAR), 103) AS PAYROLLYEAR,ISNULL(ENABLE_PAYROLL,0) AS ENABLE_PAYROLL,REGION,ISNULL((SELECT COUNTRY_CODE FROM COUNTRYMASTER WHERE COUNTRYNAME=COUNTY),'') COUNTRY_CODE,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ID,ISNULL(EMPLOYEEWORKINGMANDAYSBY,'') EMPLOYEEWORKINGMANDAYSBY,ISNULL(SEALNAME,'') SEALNAME,ISNULL(SIGNATURENAME,'') SIGNATURENAME,ISNULL(NUMBER_OF_SUPPLIER,'') NUMBER_OF_SUPPLIER,ISNULL(NUMBER_OF_CUSTOMER,'') NUMBER_OF_CUSTOMER,ISNULL(NUMBER_OF_EMPLOYEE,'') NUMBER_OF_EMPLOYEE,ISNULL(NUMBER_OF_INVENTORY,'') NUMBER_OF_INVENTORY ,ISNULL(NUMBER_OF_LOCATION,'') NUMBER_OF_LOCATION ,ISNULL(NUMBER_OF_ORDER,'') NUMBER_OF_ORDER";
		Map plantDetail = new PlantMstDAO().selectRow(plntQuery, htCondition);
		
		//arrCustomers =  ageingUtil.getcustomerorsuppliername(PLANT, fdate, tdate, "INVOICE", custName);
		
		String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
		File checkImageFile = new File(imagePath);
		if (!checkImageFile.exists()) {
			imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		}
		
		//for(int i=0; i<arrCustomers.size();i++) {
			//Map customer = (Map)arrCustomers.get(i);
			boolean insertFlag = false;
			String statementNo = "", sBatchSeq = "";
			double notDue =0, days30=0, days60=0, days90=0, days90Plus=0, totaldue=0;
			ArrayList reportDetailList  = new ArrayList();
			
			Map custDetail = new HashMap();
			if(custName.length()>0) {
				htCondition = new Hashtable();
				htCondition.put("PLANT", PLANT);
				htCondition.put("CNAME", custName);
				custDetail = new CustomerBeanDAO().getCustomerDetail(htCondition, PLANT);
			}
			
			htCondition = new Hashtable();
     		String query = " isnull(NXTSEQ,'') as NXTSEQ";
     		htCondition.put(IDBConstants.PLANT, PLANT);
     		htCondition.put(IDBConstants.TBL_FUNCTION, "PAYMENT");
     		
 			boolean exitFlag = false;
 			boolean resultflag = false;
 			exitFlag = new TblControlDAO().isExisit(htCondition, "", PLANT);

 			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
 			if (exitFlag == false) {
 				Map htInsert = null;
 				Hashtable htTblCntInsert = new Hashtable();
 				htTblCntInsert.put(IDBConstants.PLANT, PLANT);
 				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
 				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PMT");
 				htTblCntInsert.put("MINSEQ", "0000");
 				htTblCntInsert.put("MAXSEQ", "9999");
 				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
 				htTblCntInsert.put(IDBConstants.CREATED_BY, LOGIN_USER);
 				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
 				insertFlag = new TblControlDAO().insertTblControl(htTblCntInsert, PLANT);

 				statementNo = "1";
 			} else {
 				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

 				Map m1 = new TblControlDAO().selectRow(query, htCondition, "");
 				sBatchSeq = (String) m1.get("NXTSEQ");
 				System.out.println("length" + sBatchSeq.length());

 				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

 				String updatedSeq = Integer.toString(inxtSeq);
 				
 				Map htUpdate = null;

 				Hashtable htTblCntUpdate = new Hashtable();
 				htTblCntUpdate.put(IDBConstants.PLANT, PLANT);
 				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
 				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PMT");
 				StringBuffer updateQyery = new StringBuffer("set ");
 				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

 				boolean updateFlag = new TblControlDAO().update(updateQyery.toString(), htTblCntUpdate, "", PLANT);
 				statementNo = updatedSeq;
 			}
 			htCondition = new Hashtable();
 			htCondition.put("PLANT", PLANT);
 			if(custName.length()>0)
				htCondition.put("CUSTNO", (String)custDetail.get("CUSTNO"));
 			else
 				htCondition.put("CUSTNO", "");
 			
			htCondition.put("FROMDATE", fdate);
			htCondition.put("TODATE", tdate);
			
			ArrayList arrCurrency = new ArrayList();
			arrCurrency =  ageingUtil.getcurrencygroupby(PLANT, fdate, tdate, "INVOICE", custName);
			for (int i = 0; i < arrCurrency.size(); i++) {
				Map currency = (Map) arrCurrency.get(i);

				Map agingDetail = ageingUtil.getAgingSummaryForReportBYCURRENCY(htCondition,
						(String) currency.get("currency"));
				notDue = Double.parseDouble((String) agingDetail.get("DAY0"));
				days30 = Double.parseDouble((String) agingDetail.get("DAY1"));
				days60 = Double.parseDouble((String) agingDetail.get("DAY2"));
				days90 = Double.parseDouble((String) agingDetail.get("DAY3"));
				days90Plus = Double.parseDouble((String) agingDetail.get("DAY4"));
				totaldue = Double.parseDouble((String) agingDetail.get("TOTAL_DUE"));

				reportDetailList = ageingUtil.getAgingDetailForReportbycurrency(htCondition,
						(String) currency.get("currency"));
				JSONArray ageingDetArray = new JSONArray();
				for (int iCnt = 0; iCnt < reportDetailList.size(); iCnt++) {
					Map lineArr = (Map) reportDetailList.get(iCnt);
					JSONObject reportDetails = new JSONObject();

					String invDate = (String) lineArr.get("INVOICE_DATE");
					String invoice = (String) lineArr.get("INVOICE");
					String dueDate = (String) lineArr.get("DUE_DATE");
					String amount = (String) lineArr.get("CONV_INVOICE_AMOUNT");
					String received = (String) lineArr.get("CONV_RECEIVED");
					String overDue = (String) lineArr.get("OVER_DUE");
					String outStanding = (String) lineArr.get("CONV_OUTSTANDING");
					String cummBal = (String) lineArr.get("CUMM_BAL");

					reportDetails.put("invDate", invDate);
					reportDetails.put("invoice", invoice);
					reportDetails.put("dueDate", dueDate);
					reportDetails.put("amount", Numbers.toMillionFormat(amount, numberOfDecimals));
					reportDetails.put("received", Numbers.toMillionFormat(received, numberOfDecimals));
					reportDetails.put("overDue", Numbers.toMillionFormat(overDue, numberOfDecimals));
					reportDetails.put("outStanding", Numbers.toMillionFormat(outStanding, numberOfDecimals));
					reportDetails.put("cummBal", Numbers.toMillionFormat(cummBal, numberOfDecimals));

					ageingDetArray.add(reportDetails);
				}

				JSONObject reportContent = new JSONObject();
				/** Company Details **/
				reportContent.put("fromAddress_CompanyName", (String) plantDetail.get("PLNTDESC"));
				reportContent.put("fromAddress_CompanyName", (String) plantDetail.get("PLNTDESC"));
				reportContent.put("fromAddress_BlockAddress",
						(String) plantDetail.get("ADD1") + " " + (String) plantDetail.get("ADD2"));
				reportContent.put("fromAddress_RoadAddress",
						(String) plantDetail.get("ADD3") + " " + (String) plantDetail.get("ADD4"));
				reportContent.put("fromAddress_Country",
						(String) plantDetail.get("STATE") + " " + (String) plantDetail.get("COUNTY"));
				reportContent.put("fromAddress_ZIPCode", (String) plantDetail.get("ZIP"));
				reportContent.put("fromAddress_RCBNO", (String) plantDetail.get("RCBNO"));
				reportContent.put("fromAddress_TpNo", (String) plantDetail.get("TELNO"));
				reportContent.put("fromAddress_FaxNo", (String) plantDetail.get("FAX"));
				reportContent.put("fromAddress_ContactPersonName", (String) plantDetail.get("NAME"));
				reportContent.put("fromAddress_ContactPersonMobile", (String) plantDetail.get("HPNO"));
				reportContent.put("fromAddress_ContactPersonEmail", (String) plantDetail.get("EMAIL"));

				reportContent.put("statementNo", statementNo);
				reportContent.put("CurDate", STATEMENT_DATE);

				/* Label Details */
				reportContent.put("lblHeader", "To");
				reportContent.put("lblstatement", "Statement Of Account with aging");
				reportContent.put("lblDate", "Date");
				reportContent.put("lblstatementno", "Statement No");
				reportContent.put("lblamtdue", "Amount Due");
				reportContent.put("lblOrderNo", "Inv No.");
				reportContent.put("lblDueDate", "Due Date");
				reportContent.put("lblAmount", "Inv Amount");
				reportContent.put("lblReceived", "Received");
				reportContent.put("lblOverDueDays", "Over Due Days");
				reportContent.put("lblBalance", "Outstanding");
				reportContent.put("lblCummBalance", "Cumm. Bal");
				reportContent.put("lblTotalOS", "Total O/s");
				reportContent.put("lblcurrentdue", "Not Due");
				reportContent.put("lbl30daysdue", "1-30 days");
				reportContent.put("lbl60daysdue", "30-60 days");
				reportContent.put("lbl90daysdue", "60-90 days");
				reportContent.put("lbl90plusdaysdue", "90 & above");

				/* Aging by Days */
				reportContent.put("notDue", Numbers.toMillionFormat(notDue, numberOfDecimals));
				reportContent.put("v30daysdue", Numbers.toMillionFormat(days30, numberOfDecimals));
				reportContent.put("v60daysdue", Numbers.toMillionFormat(days60, numberOfDecimals));
				reportContent.put("v90daysdue", Numbers.toMillionFormat(days90, numberOfDecimals));
				reportContent.put("v90plusdaysdue", Numbers.toMillionFormat(days90Plus, numberOfDecimals));
				reportContent.put("amountdue", Numbers.toMillionFormat(totaldue, numberOfDecimals));

				 /* Aging CURRENCY*/
			       
			     reportContent.put("currency", (String)currency.get("currency"));
				
				
				jsonArray.add(reportContent);
				ageingDetailsArray.add(ageingDetArray);
			}
			
			resultJson.put("reportContent", jsonArray);
			resultJson.put("reportContentDetails", ageingDetailsArray);
		//}
		JSONObject resultJsonInt = new JSONObject();
        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
        resultJsonInt.put("ERROR_CODE", "100");
        jsonArrayErr.add(resultJsonInt);
        resultJson.put("errors", jsonArrayErr);        
		} catch (Exception e) {
			jsonArray.add("");
    		resultJson.put("items", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		} 
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	public void getSupplierAgeingSummary(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		HttpSession session = request.getSession();
		String LOGIN_USER="",ORDER="",ordRefNo="",custName="",STATEMENT_DATE="",
				FROM_DATE="",TO_DATE="",Orderno="",invoiceNo="",fdate="",tdate="";
		
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray ageingDetailsArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
		try {
		String PLANT = (String) session.getAttribute("PLANT");
		LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		ORDER=StrUtils.fString(request.getParameter("Order"));
        ordRefNo = StrUtils.fString(request.getParameter("ordRefNo"));
        custName = StrUtils.fString(request.getParameter("vendorName"));
        STATEMENT_DATE = StrUtils.fString(request.getParameter("STATEMENT_DATE"));
        FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
        TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
        Orderno = StrUtils.fString(request.getParameter("ORDERNO"));
        invoiceNo = StrUtils.fString(request.getParameter("NAME"));
        
        String curDate =DateUtils.getDate();     
        if(STATEMENT_DATE.length()<0||STATEMENT_DATE==null||STATEMENT_DATE.equalsIgnoreCase(""))STATEMENT_DATE=curDate;
        if (FROM_DATE.length()>5)
        	fdate = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
       	if (TO_DATE.length()>5)
        	tdate = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
       	ArrayList arrCustomers = new ArrayList();
       	String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
		
		Hashtable htCondition = new Hashtable();
		htCondition.put("PLANT", PLANT);
		String plntQuery = "PLANT,PLNTDESC, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE, CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE,CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,NAME,DESGINATION,TELNO,HPNO,EMAIL,ISNULL(ADD1,'') as ADD1,ISNULL(ADD2,'') as ADD2,ISNULL(ADD3,'') as ADD3,ISNULL(ADD4,'') as ADD4,COUNTY,ZIP,isnull(USERFLD1,'') AS REMARKS,USERFLD2 AS FAX,RCBNO,ISNULL(WEBSITE,'')WEBSITE,ISNULL(companyregnumber,'')companyregnumber,GSTREGNO,SALES_CHARGE_BY,SALES_PERCENT,SALES_FR_DOLLARS,SALES_FR_CENTS,ENQUIRY_FR_DOLLARS,ENQUIRY_FR_CENTS,NUMBER_OF_CATALOGS,BASE_CURRENCY,ISNULL(NUMBER_OF_SIGNATURES,'') NUMBER_OF_SIGNATURES,ISNULL(TAXBY,'') TAXBY,ISNULL(TAXBYLABEL,'') TAXBYLABEL, ISNULL(TAXBYLABELORDERMANAGEMENT,'') TAXBYLABELORDERMANAGEMENT,ISNULL(STATE,'') STATE,ENABLE_INVENTORY, ENABLE_ACCOUNTING,ISNULL(NUMBEROFDECIMAL,'2') NUMBEROFDECIMAL,ISNULL(ISTAXREGISTRED,'0') ISTAXREGISTRED,REPROTSBASIS,CONVERT(VARCHAR(20), CONVERT(DATETIME, FISCAL_YEAR), 103) AS FISCALYEAR, CONVERT(VARCHAR(20), CONVERT(DATETIME, PAYROLL_YEAR), 103) AS PAYROLLYEAR,ISNULL(ENABLE_PAYROLL,0) AS ENABLE_PAYROLL,REGION,ISNULL((SELECT COUNTRY_CODE FROM COUNTRYMASTER WHERE COUNTRYNAME=COUNTY),'') COUNTRY_CODE,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ID,ISNULL(EMPLOYEEWORKINGMANDAYSBY,'') EMPLOYEEWORKINGMANDAYSBY,ISNULL(SEALNAME,'') SEALNAME,ISNULL(SIGNATURENAME,'') SIGNATURENAME,ISNULL(NUMBER_OF_SUPPLIER,'') NUMBER_OF_SUPPLIER,ISNULL(NUMBER_OF_CUSTOMER,'') NUMBER_OF_CUSTOMER,ISNULL(NUMBER_OF_EMPLOYEE,'') NUMBER_OF_EMPLOYEE,ISNULL(NUMBER_OF_INVENTORY,'') NUMBER_OF_INVENTORY ,ISNULL(NUMBER_OF_LOCATION,'') NUMBER_OF_LOCATION ,ISNULL(NUMBER_OF_ORDER,'') NUMBER_OF_ORDER";
		Map plantDetail = new PlantMstDAO().selectRow(plntQuery, htCondition);
		
		arrCustomers =  ageingUtil.getcustomerorsuppliernamegroupby(PLANT, fdate, tdate, "BILL", custName);
		
		String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
		File checkImageFile = new File(imagePath);
		if (!checkImageFile.exists()) {
			imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		}
		JSONArray ageingDetArray = new JSONArray();
		int index=0;
		for(int i=0; i<arrCustomers.size();i++) {
			Map customer = (Map)arrCustomers.get(i);
			ArrayList reportDetailList  = new ArrayList();
			
			
			htCondition = new Hashtable();
			htCondition.put("PLANT", PLANT);
			htCondition.put("VENDNO", (String)customer.get("custcode"));
			Map custDetail = new CustomerBeanDAO().getSupplierDetail(htCondition, PLANT);

 			htCondition = new Hashtable();
 			htCondition.put("PLANT", PLANT);
			htCondition.put("VENDNO", (String)customer.get("custcode"));
			htCondition.put("FROMDATE", fdate);
			htCondition.put("TODATE", tdate);
 			
 			reportDetailList = ageingUtil.getSupplierAgingDetailForReportBycurrency(htCondition,(String)customer.get("currency"));
 			
 			for (int iCnt =0; iCnt<reportDetailList.size(); iCnt++){
				 Map lineArr = (Map) reportDetailList.get(iCnt);
				 JSONObject reportDetails = new JSONObject();
				 
				 String billDate = (String)lineArr.get("BILL_DATE");
				 String bill = (String)lineArr.get("BILL");
				 String dueDate = (String)lineArr.get("DUE_DATE");
				 String amount = (String)lineArr.get("BILL_AMOUNT");
				 String received = (String)lineArr.get("RECEIVED");
				 String overDue = (String)lineArr.get("OVER_DUE");
				 String outStanding = (String)lineArr.get("OUTSTANDING");
				 String currency = (String)lineArr.get("CURRENCY");
				 String refnumber = (String)lineArr.get("REFNUMBER");
				 
				 reportDetails.put("index", ++index);
				 reportDetails.put("vendName", (String)customer.get("custname"));
				 reportDetails.put("billDate", billDate);
				 reportDetails.put("bill", bill);
				 reportDetails.put("refnumber", refnumber);
				 reportDetails.put("currency", currency);
				 reportDetails.put("key", (String)customer.get("custname")+"_"+currency);
				 reportDetails.put("dueDate", dueDate);
				 reportDetails.put("amount", StrUtils.addZeroes(Double.parseDouble(amount),numberOfDecimal));
				 reportDetails.put("received", received);
				 reportDetails.put("overDue", overDue);
				 reportDetails.put("outStanding", StrUtils.addZeroes(Double.parseDouble(outStanding),numberOfDecimal));
				 
				 ageingDetArray.add(reportDetails);
			 } 			
	       ageingDetailsArray.add(ageingDetArray);	       
		}
		resultJson.put("items", ageingDetArray);
		JSONObject resultJsonInt = new JSONObject();
        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
        resultJsonInt.put("ERROR_CODE", "100");
        jsonArrayErr.add(resultJsonInt);
        resultJson.put("errors", jsonArrayErr);        
		} catch (Exception e) {
			jsonArray.add("");
    		resultJson.put("items", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		} 
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	public void PrintSupplierAgeingReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		HttpSession session = request.getSession();
		String LOGIN_USER="",ORDER="",ordRefNo="",custName="",STATEMENT_DATE="",
				FROM_DATE="",TO_DATE="",Orderno="",invoiceNo="",fdate="",tdate="",numberOfDecimals="2";
		
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray ageingDetailsArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
		try {
		String PLANT = (String) session.getAttribute("PLANT");
		LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		ORDER=StrUtils.fString(request.getParameter("Order"));
        ordRefNo = StrUtils.fString(request.getParameter("ordRefNo"));
        custName = StrUtils.fString(request.getParameter("custName"));
        STATEMENT_DATE = StrUtils.fString(request.getParameter("STATEMENT_DATE"));
        FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
        TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
        Orderno = StrUtils.fString(request.getParameter("ORDERNO"));
        invoiceNo = StrUtils.fString(request.getParameter("NAME"));
        
        String curDate =DateUtils.getDate();  
        if(STATEMENT_DATE.length()<0||STATEMENT_DATE==null||STATEMENT_DATE.equalsIgnoreCase(""))STATEMENT_DATE=curDate;
        if (FROM_DATE.length()>5)
        	fdate = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
       	if (TO_DATE.length()>5)
        	tdate = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
       	ArrayList arrCustomers = new ArrayList();
		
		Hashtable htCondition = new Hashtable();
		htCondition.put("PLANT", PLANT);
		String plntQuery = "PLANT,PLNTDESC, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE, CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE,CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,NAME,DESGINATION,TELNO,HPNO,EMAIL,ISNULL(ADD1,'') as ADD1,ISNULL(ADD2,'') as ADD2,ISNULL(ADD3,'') as ADD3,ISNULL(ADD4,'') as ADD4,COUNTY,ZIP,isnull(USERFLD1,'') AS REMARKS,USERFLD2 AS FAX,RCBNO,ISNULL(WEBSITE,'')WEBSITE,ISNULL(companyregnumber,'')companyregnumber,GSTREGNO,SALES_CHARGE_BY,SALES_PERCENT,SALES_FR_DOLLARS,SALES_FR_CENTS,ENQUIRY_FR_DOLLARS,ENQUIRY_FR_CENTS,NUMBER_OF_CATALOGS,BASE_CURRENCY,ISNULL(NUMBER_OF_SIGNATURES,'') NUMBER_OF_SIGNATURES,ISNULL(TAXBY,'') TAXBY,ISNULL(TAXBYLABEL,'') TAXBYLABEL, ISNULL(TAXBYLABELORDERMANAGEMENT,'') TAXBYLABELORDERMANAGEMENT,ISNULL(STATE,'') STATE,ENABLE_INVENTORY, ENABLE_ACCOUNTING,ISNULL(NUMBEROFDECIMAL,'2') NUMBEROFDECIMAL,ISNULL(ISTAXREGISTRED,'0') ISTAXREGISTRED,REPROTSBASIS,CONVERT(VARCHAR(20), CONVERT(DATETIME, FISCAL_YEAR), 103) AS FISCALYEAR, CONVERT(VARCHAR(20), CONVERT(DATETIME, PAYROLL_YEAR), 103) AS PAYROLLYEAR,ISNULL(ENABLE_PAYROLL,0) AS ENABLE_PAYROLL,REGION,ISNULL((SELECT COUNTRY_CODE FROM COUNTRYMASTER WHERE COUNTRYNAME=COUNTY),'') COUNTRY_CODE,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ID,ISNULL(EMPLOYEEWORKINGMANDAYSBY,'') EMPLOYEEWORKINGMANDAYSBY,ISNULL(SEALNAME,'') SEALNAME,ISNULL(SIGNATURENAME,'') SIGNATURENAME,ISNULL(NUMBER_OF_SUPPLIER,'') NUMBER_OF_SUPPLIER,ISNULL(NUMBER_OF_CUSTOMER,'') NUMBER_OF_CUSTOMER,ISNULL(NUMBER_OF_EMPLOYEE,'') NUMBER_OF_EMPLOYEE,ISNULL(NUMBER_OF_INVENTORY,'') NUMBER_OF_INVENTORY ,ISNULL(NUMBER_OF_LOCATION,'') NUMBER_OF_LOCATION ,ISNULL(NUMBER_OF_ORDER,'') NUMBER_OF_ORDER";
		Map plantDetail = new PlantMstDAO().selectRow(plntQuery, htCondition);
		
		arrCustomers =  ageingUtil.getcustomerorsuppliernamegroupby(PLANT, fdate, tdate, "Bill", custName);
		
		String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
		File checkImageFile = new File(imagePath);
		if (!checkImageFile.exists()) {
			imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		}
		
		for(int i=0; i<arrCustomers.size();i++) {
			Map customer = (Map)arrCustomers.get(i);
			boolean insertFlag = false;
			String statementNo = "", sBatchSeq = "";
			double notDue =0, days30=0, days60=0, days90=0, days90Plus=0, totaldue=0;
			ArrayList reportDetailList  = new ArrayList();
			JSONArray ageingDetArray = new JSONArray();
			
			htCondition = new Hashtable();
			htCondition.put("PLANT", PLANT);
			htCondition.put("VENDNO", (String)customer.get("custcode"));
			Map vendorDetail = new CustomerBeanDAO().getSupplierDetail(htCondition, PLANT);
			
			htCondition = new Hashtable();
     		String query = " isnull(NXTSEQ,'') as NXTSEQ";
     		htCondition.put(IDBConstants.PLANT, PLANT);
     		htCondition.put(IDBConstants.TBL_FUNCTION, "PAYMENT");
     		
 			boolean exitFlag = false;
 			boolean resultflag = false;
 			exitFlag = new TblControlDAO().isExisit(htCondition, "", PLANT);

 			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
 			if (exitFlag == false) {
 				Map htInsert = null;
 				Hashtable htTblCntInsert = new Hashtable();
 				htTblCntInsert.put(IDBConstants.PLANT, PLANT);
 				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
 				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PMT");
 				htTblCntInsert.put("MINSEQ", "0000");
 				htTblCntInsert.put("MAXSEQ", "9999");
 				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
 				htTblCntInsert.put(IDBConstants.CREATED_BY, LOGIN_USER);
 				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
 				insertFlag = new TblControlDAO().insertTblControl(htTblCntInsert, PLANT);

 				statementNo = "1";
 			} else {
 				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

 				Map m1 = new TblControlDAO().selectRow(query, htCondition, "");
 				sBatchSeq = (String) m1.get("NXTSEQ");
 				System.out.println("length" + sBatchSeq.length());

 				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

 				String updatedSeq = Integer.toString(inxtSeq);
 				
 				Map htUpdate = null;

 				Hashtable htTblCntUpdate = new Hashtable();
 				htTblCntUpdate.put(IDBConstants.PLANT, PLANT);
 				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
 				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PMT");
 				StringBuffer updateQyery = new StringBuffer("set ");
 				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

 				boolean updateFlag = new TblControlDAO().update(updateQyery.toString(), htTblCntUpdate, "", PLANT);
 				statementNo = updatedSeq;
 			}
 			htCondition = new Hashtable();
 			htCondition.put("PLANT", PLANT);
			htCondition.put("VENDNO", (String)customer.get("custcode"));
			htCondition.put("FROMDATE", fdate);
			htCondition.put("TODATE", tdate);
			
 			Map agingDetail = ageingUtil.getSupplierAgingSummaryForReportByCurrency(htCondition,(String)customer.get("currency"));
 			notDue = Double.parseDouble((String)agingDetail.get("DAY0"));
 			days30 = Double.parseDouble((String)agingDetail.get("DAY1"));
 			days60 = Double.parseDouble((String)agingDetail.get("DAY2"));
 			days90 = Double.parseDouble((String)agingDetail.get("DAY3"));
 			days90Plus = Double.parseDouble((String)agingDetail.get("DAY4"));
 			totaldue = Double.parseDouble((String)agingDetail.get("TOTAL_DUE"));
 			
 			reportDetailList = ageingUtil.getSupplierAgingDetailForReportBycurrency(htCondition,(String)customer.get("currency"));
 			
 			for (int iCnt =0; iCnt<reportDetailList.size(); iCnt++){
				 Map lineArr = (Map) reportDetailList.get(iCnt);
				 JSONObject reportDetails = new JSONObject();
				 
				 String billDate = (String)lineArr.get("BILL_DATE");
				 String bill = (String)lineArr.get("BILL");
				 String currency = (String)lineArr.get("CURRENCY");
				 String dueDate = (String)lineArr.get("DUE_DATE");
				 String amount = (String)lineArr.get("BILL_AMOUNT");
				 String received = (String)lineArr.get("RECEIVED");
				 String overDue = (String)lineArr.get("OVER_DUE");
				 String outStanding = (String)lineArr.get("OUTSTANDING");
				 String cummBal = (String)lineArr.get("CUMM_BAL");
				 String refnumber = (String)lineArr.get("REFNUMBER");
				 
				 reportDetails.put("billDate", billDate);
				 reportDetails.put("bill", bill);
				 reportDetails.put("refnumber", refnumber);
				 reportDetails.put("currency", currency);
				 reportDetails.put("dueDate", dueDate);
				 reportDetails.put("amount", Numbers.toMillionFormat(amount, numberOfDecimals));
				 reportDetails.put("received", Numbers.toMillionFormat(received, numberOfDecimals));
				 reportDetails.put("overDue", Numbers.toMillionFormat(overDue, numberOfDecimals));
				 reportDetails.put("outStanding", Numbers.toMillionFormat(outStanding, numberOfDecimals));
				 reportDetails.put("cummBal", Numbers.toMillionFormat(cummBal, numberOfDecimals));
				 
				 ageingDetArray.add(reportDetails);
			 }

 			JSONObject reportContent = new JSONObject();
	       /** Company Details **/ 
	       reportContent.put("fromAddress_CompanyName", (String) plantDetail.get("PLNTDESC"));
	       reportContent.put("fromAddress_CompanyName", (String) plantDetail.get("PLNTDESC"));
	       reportContent.put("fromAddress_BlockAddress", (String) plantDetail.get("ADD1") + " " + (String) plantDetail.get("ADD2"));
	       reportContent.put("fromAddress_RoadAddress", (String) plantDetail.get("ADD3") + " " + (String) plantDetail.get("ADD4"));
	       reportContent.put("fromAddress_Country", (String) plantDetail.get("STATE") + " " +(String) plantDetail.get("COUNTY"));
	       reportContent.put("fromAddress_ZIPCode", (String) plantDetail.get("ZIP"));
	       reportContent.put("fromAddress_RCBNO", (String) plantDetail.get("RCBNO"));
	       reportContent.put("fromAddress_TpNo", (String) plantDetail.get("TELNO"));
	       reportContent.put("fromAddress_FaxNo", (String) plantDetail.get("FAX"));
	       reportContent.put("fromAddress_ContactPersonName", (String) plantDetail.get("NAME"));
	       reportContent.put("fromAddress_ContactPersonMobile", (String) plantDetail.get("HPNO"));
	       reportContent.put("fromAddress_ContactPersonEmail", (String) plantDetail.get("EMAIL"));
	       
	       /* Customer Details */
	       reportContent.put("To_ContactName", (String) vendorDetail.get("NAME"));
	       reportContent.put("To_CompanyName", (String) vendorDetail.get("VNAME"));
	       reportContent.put("To_BlockAddress", (String) vendorDetail.get("ADDR1")+"  " + (String) vendorDetail.get("ADDR2"));
	       reportContent.put("To_RoadAddress", (String) vendorDetail.get("ADDR3") +"  " + (String) vendorDetail.get("ADDR4"));
	       reportContent.put("To_Country", (String) vendorDetail.get("COUNTRY"));
	       reportContent.put("To_ZIPCode", (String) vendorDetail.get("ZIP"));
	       
	       reportContent.put("statementNo", statementNo);	            
	       reportContent.put("CurDate", STATEMENT_DATE);
	       
	       /* Label Details */
	       reportContent.put("lblHeader", "To");
	       reportContent.put("lblstatement", "Statement Of Account with aging");
	       reportContent.put("lblDate", "Date");
	       reportContent.put("lblstatementno", "Statement No");	            
	       reportContent.put("lblamtdue", "Amount Due");	            
	       reportContent.put("lblOrderNo", "Bill No.");
	       reportContent.put("lblSuplierInvNo", "Supplier Invoice No.");
	       reportContent.put("lblDueDate", "Due Date");
	       reportContent.put("lblAmount", "Bill Amount");
	       reportContent.put("lblReceived", "Received");
	       reportContent.put("lblOverDueDays", "Over Due Days");
	       reportContent.put("lblBalance", "Outstanding");
	       reportContent.put("lblCummBalance", "Cumm. Bal");
	       reportContent.put("lblTotalOS", "Total O/s");
	       reportContent.put("lblcurrentdue", "Not Due");
	       reportContent.put("lbl30daysdue", "1-30 days");
	       reportContent.put("lbl60daysdue", "30-60 days");
	       reportContent.put("lbl90daysdue", "60-90 days");
	       reportContent.put("lbl90plusdaysdue", "90 & above");
	       
	       /* Aging by Days*/
	       reportContent.put("notDue", Numbers.toMillionFormat(notDue, numberOfDecimals));
	       reportContent.put("v30daysdue", Numbers.toMillionFormat(days30, numberOfDecimals));
	       reportContent.put("v60daysdue", Numbers.toMillionFormat(days60, numberOfDecimals));
	       reportContent.put("v90daysdue", Numbers.toMillionFormat(days90, numberOfDecimals));
	       reportContent.put("v90plusdaysdue", Numbers.toMillionFormat(days90Plus, numberOfDecimals));
	       reportContent.put("amountdue", Numbers.toMillionFormat(totaldue, numberOfDecimals));
	       
	       reportContent.put("currency",(String)customer.get("currency"));

	       jsonArray.add(reportContent);
	       ageingDetailsArray.add(ageingDetArray);
	       
	       resultJson.put("reportContent", jsonArray);
	       resultJson.put("reportContentDetails", ageingDetailsArray);
		}
		JSONObject resultJsonInt = new JSONObject();
        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
        resultJsonInt.put("ERROR_CODE", "100");
        jsonArrayErr.add(resultJsonInt);
        resultJson.put("errors", jsonArrayErr);        
		} catch (Exception e) {
			jsonArray.add("");
    		resultJson.put("items", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		} 
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	public void PrintSupplierConsolidatedAgeingReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		HttpSession session = request.getSession();
		String LOGIN_USER="",ORDER="",ordRefNo="",custName="",STATEMENT_DATE="",
				FROM_DATE="",TO_DATE="",Orderno="",invoiceNo="",fdate="",tdate="",numberOfDecimals="2";
		
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray ageingDetailsArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
		try {
			String PLANT = (String) session.getAttribute("PLANT");
			LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			ORDER=StrUtils.fString(request.getParameter("Order"));
	        ordRefNo = StrUtils.fString(request.getParameter("ordRefNo"));
	        custName = StrUtils.fString(request.getParameter("custName"));
	        STATEMENT_DATE = StrUtils.fString(request.getParameter("STATEMENT_DATE"));
	        FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
	        TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
	        Orderno = StrUtils.fString(request.getParameter("ORDERNO"));
	        invoiceNo = StrUtils.fString(request.getParameter("NAME"));
        
	        String curDate =DateUtils.getDate();
	        if(STATEMENT_DATE.length()<0||STATEMENT_DATE==null||STATEMENT_DATE.equalsIgnoreCase(""))STATEMENT_DATE=curDate;
	        if (FROM_DATE.length()>5)
	        	fdate = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
	       	if (TO_DATE.length()>5)
	        	tdate = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
	       	ArrayList arrCustomers = new ArrayList();
			Hashtable htCondition = new Hashtable();
			htCondition.put("PLANT", PLANT);
			String plntQuery = "PLANT,PLNTDESC, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE, CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE,CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,NAME,DESGINATION,TELNO,HPNO,EMAIL,ISNULL(ADD1,'') as ADD1,ISNULL(ADD2,'') as ADD2,ISNULL(ADD3,'') as ADD3,ISNULL(ADD4,'') as ADD4,COUNTY,ZIP,isnull(USERFLD1,'') AS REMARKS,USERFLD2 AS FAX,RCBNO,ISNULL(WEBSITE,'')WEBSITE,ISNULL(companyregnumber,'')companyregnumber,GSTREGNO,SALES_CHARGE_BY,SALES_PERCENT,SALES_FR_DOLLARS,SALES_FR_CENTS,ENQUIRY_FR_DOLLARS,ENQUIRY_FR_CENTS,NUMBER_OF_CATALOGS,BASE_CURRENCY,ISNULL(NUMBER_OF_SIGNATURES,'') NUMBER_OF_SIGNATURES,ISNULL(TAXBY,'') TAXBY,ISNULL(TAXBYLABEL,'') TAXBYLABEL, ISNULL(TAXBYLABELORDERMANAGEMENT,'') TAXBYLABELORDERMANAGEMENT,ISNULL(STATE,'') STATE,ENABLE_INVENTORY, ENABLE_ACCOUNTING,ISNULL(NUMBEROFDECIMAL,'2') NUMBEROFDECIMAL,ISNULL(ISTAXREGISTRED,'0') ISTAXREGISTRED,REPROTSBASIS,CONVERT(VARCHAR(20), CONVERT(DATETIME, FISCAL_YEAR), 103) AS FISCALYEAR, CONVERT(VARCHAR(20), CONVERT(DATETIME, PAYROLL_YEAR), 103) AS PAYROLLYEAR,ISNULL(ENABLE_PAYROLL,0) AS ENABLE_PAYROLL,REGION,ISNULL((SELECT COUNTRY_CODE FROM COUNTRYMASTER WHERE COUNTRYNAME=COUNTY),'') COUNTRY_CODE,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ID,ISNULL(EMPLOYEEWORKINGMANDAYSBY,'') EMPLOYEEWORKINGMANDAYSBY,ISNULL(SEALNAME,'') SEALNAME,ISNULL(SIGNATURENAME,'') SIGNATURENAME,ISNULL(NUMBER_OF_SUPPLIER,'') NUMBER_OF_SUPPLIER,ISNULL(NUMBER_OF_CUSTOMER,'') NUMBER_OF_CUSTOMER,ISNULL(NUMBER_OF_EMPLOYEE,'') NUMBER_OF_EMPLOYEE,ISNULL(NUMBER_OF_INVENTORY,'') NUMBER_OF_INVENTORY ,ISNULL(NUMBER_OF_LOCATION,'') NUMBER_OF_LOCATION ,ISNULL(NUMBER_OF_ORDER,'') NUMBER_OF_ORDER";
			Map plantDetail = new PlantMstDAO().selectRow(plntQuery, htCondition);
			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
			File checkImageFile = new File(imagePath);
			if (!checkImageFile.exists()) {
				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
		
			boolean insertFlag = false;
			String statementNo = "", sBatchSeq = "";
			double notDue =0, days30=0, days60=0, days90=0, days90Plus=0, totaldue=0;
			ArrayList reportDetailList  = new ArrayList();
			
			Map vendorDetail = new HashMap();
			if(custName.length()>0) {
				htCondition = new Hashtable();
				htCondition.put("PLANT", PLANT);
				htCondition.put("VNAME", custName);
				vendorDetail = new CustomerBeanDAO().getSupplierDetail(htCondition, PLANT);
			}
			
			htCondition = new Hashtable();
     		String query = " isnull(NXTSEQ,'') as NXTSEQ";
     		htCondition.put(IDBConstants.PLANT, PLANT);
     		htCondition.put(IDBConstants.TBL_FUNCTION, "PAYMENT");
     		
 			boolean exitFlag = false;
 			boolean resultflag = false;
 			exitFlag = new TblControlDAO().isExisit(htCondition, "", PLANT);

 			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
 			if (exitFlag == false) {
 				Map htInsert = null;
 				Hashtable htTblCntInsert = new Hashtable();
 				htTblCntInsert.put(IDBConstants.PLANT, PLANT);
 				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
 				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PMT");
 				htTblCntInsert.put("MINSEQ", "0000");
 				htTblCntInsert.put("MAXSEQ", "9999");
 				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
 				htTblCntInsert.put(IDBConstants.CREATED_BY, LOGIN_USER);
 				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
 				insertFlag = new TblControlDAO().insertTblControl(htTblCntInsert, PLANT);

 				statementNo = "1";
 			} else {
 				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

 				Map m1 = new TblControlDAO().selectRow(query, htCondition, "");
 				sBatchSeq = (String) m1.get("NXTSEQ");
 				System.out.println("length" + sBatchSeq.length());
 				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;
 				String updatedSeq = Integer.toString(inxtSeq); 				
 				Map htUpdate = null;

 				Hashtable htTblCntUpdate = new Hashtable();
 				htTblCntUpdate.put(IDBConstants.PLANT, PLANT);
 				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PAYMENT");
 				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PMT");
 				StringBuffer updateQyery = new StringBuffer("set ");
 				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

 				boolean updateFlag = new TblControlDAO().update(updateQyery.toString(), htTblCntUpdate, "", PLANT);
 				statementNo = updatedSeq;
 			}
			htCondition = new Hashtable();
			htCondition.put("PLANT", PLANT);
			if(custName.length()>0)
				htCondition.put("VENDNO", (String)vendorDetail.get("VENDNO"));
			else
				htCondition.put("VENDNO", "");
			
			htCondition.put("FROMDATE", fdate);
			htCondition.put("TODATE", tdate);
			
			
			ArrayList arrCurrency = new ArrayList();
			arrCurrency =  ageingUtil.getcurrencygroupby(PLANT, fdate, tdate, "BILL", custName);
			for (int i = 0; i < arrCurrency.size(); i++) {
				Map currency = (Map) arrCurrency.get(i);
		
			Map agingDetail = ageingUtil.getSupplierAgingSummaryForReportByCurrency(htCondition,(String)currency.get("currency"));
			notDue = Double.parseDouble((String)agingDetail.get("DAY0"));
			days30 = Double.parseDouble((String)agingDetail.get("DAY1"));
			days60 = Double.parseDouble((String)agingDetail.get("DAY2"));
			days90 = Double.parseDouble((String)agingDetail.get("DAY3"));
			days90Plus = Double.parseDouble((String)agingDetail.get("DAY4"));
			totaldue = Double.parseDouble((String)agingDetail.get("TOTAL_DUE"));
		
			reportDetailList = ageingUtil.getSupplierAgingDetailForReportBycurrency(htCondition,(String)currency.get("currency"));
			JSONArray ageingDetArray = new JSONArray();
			for (int iCnt =0; iCnt<reportDetailList.size(); iCnt++){
				 Map lineArr = (Map) reportDetailList.get(iCnt);
				 JSONObject reportDetails = new JSONObject();
				 
				 String billDate = (String)lineArr.get("BILL_DATE");
				 String bill = (String)lineArr.get("BILL");
				 String dueDate = (String)lineArr.get("DUE_DATE");
				 String amount = (String)lineArr.get("BILL_AMOUNT");
				 String received = (String)lineArr.get("RECEIVED");
				 String overDue = (String)lineArr.get("OVER_DUE");
				 String outStanding = (String)lineArr.get("OUTSTANDING");
				 String cummBal = (String)lineArr.get("CUMM_BAL");
				 String refnumber = (String)lineArr.get("REFNUMBER");
				 
				 reportDetails.put("billDate", billDate);
				 reportDetails.put("bill", bill);
				 reportDetails.put("refnumber", refnumber);
				 reportDetails.put("dueDate", dueDate);
				 reportDetails.put("amount", Numbers.toMillionFormat(amount, numberOfDecimals));
				 reportDetails.put("received", Numbers.toMillionFormat(received, numberOfDecimals));
				 reportDetails.put("overDue", Numbers.toMillionFormat(overDue, numberOfDecimals));
				 reportDetails.put("outStanding", Numbers.toMillionFormat(outStanding, numberOfDecimals));
				 reportDetails.put("cummBal", Numbers.toMillionFormat(cummBal, numberOfDecimals));
				 
				 ageingDetArray.add(reportDetails);
			 }

			JSONObject reportContent = new JSONObject();
	       /** Company Details **/ 
	       reportContent.put("fromAddress_CompanyName", (String) plantDetail.get("PLNTDESC"));
	       reportContent.put("fromAddress_CompanyName", (String) plantDetail.get("PLNTDESC"));
	       reportContent.put("fromAddress_BlockAddress", (String) plantDetail.get("ADD1") + " " + (String) plantDetail.get("ADD2"));
	       reportContent.put("fromAddress_RoadAddress", (String) plantDetail.get("ADD3") + " " + (String) plantDetail.get("ADD4"));
	       reportContent.put("fromAddress_Country", (String) plantDetail.get("STATE") + " " +(String) plantDetail.get("COUNTY"));
	       reportContent.put("fromAddress_ZIPCode", (String) plantDetail.get("ZIP"));
	       reportContent.put("fromAddress_RCBNO", (String) plantDetail.get("RCBNO"));
	       reportContent.put("fromAddress_TpNo", (String) plantDetail.get("TELNO"));
	       reportContent.put("fromAddress_FaxNo", (String) plantDetail.get("FAX"));
	       reportContent.put("fromAddress_ContactPersonName", (String) plantDetail.get("NAME"));
	       reportContent.put("fromAddress_ContactPersonMobile", (String) plantDetail.get("HPNO"));
	       reportContent.put("fromAddress_ContactPersonEmail", (String) plantDetail.get("EMAIL"));
	       
	       reportContent.put("statementNo", statementNo);	            
	       reportContent.put("CurDate", STATEMENT_DATE);
       
	       /* Label Details */
	       reportContent.put("lblHeader", "To");
	       reportContent.put("lblstatement", "Statement Of Account with aging");
	       reportContent.put("lblDate", "Date");
	       reportContent.put("lblstatementno", "Statement No");	            
	       reportContent.put("lblamtdue", "Amount Due");	            
	       reportContent.put("lblOrderNo", "Bill No.");
	       reportContent.put("lblSuplierInvNo", "Supplier Invoice No.");
	       reportContent.put("lblDueDate", "Due Date");
	       reportContent.put("lblAmount", "Inv Amount");
	       reportContent.put("lblReceived", "Received");
	       reportContent.put("lblOverDueDays", "Over Due Days");
	       reportContent.put("lblBalance", "Outstanding");
	       reportContent.put("lblCummBalance", "Cumm. Bal");
	       reportContent.put("lblTotalOS", "Total O/s");
	       reportContent.put("lblcurrentdue", "Not Due");
	       reportContent.put("lbl30daysdue", "1-30 days");
	       reportContent.put("lbl60daysdue", "30-60 days");
	       reportContent.put("lbl90daysdue", "60-90 days");
	       reportContent.put("lbl90plusdaysdue", "90 & above");
       
		   /* Aging by Days*/
		   reportContent.put("notDue", Numbers.toMillionFormat(notDue, numberOfDecimals));
		   reportContent.put("v30daysdue", Numbers.toMillionFormat(days30, numberOfDecimals) );
		   reportContent.put("v60daysdue",  Numbers.toMillionFormat(days60, numberOfDecimals));
		   reportContent.put("v90daysdue",  Numbers.toMillionFormat(days90, numberOfDecimals));
		   reportContent.put("v90plusdaysdue",  Numbers.toMillionFormat(days90Plus, numberOfDecimals));
		   reportContent.put("amountdue",  Numbers.toMillionFormat(totaldue, numberOfDecimals));
		   
		   reportContent.put("currency", (String)currency.get("currency"));
			
		   jsonArray.add(reportContent);
		   ageingDetailsArray.add(ageingDetArray);
		   
		   
		   
		}
		   
		   resultJson.put("reportContent", jsonArray);
		   resultJson.put("reportContentDetails", ageingDetailsArray);
       
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("errors", jsonArrayErr);        
		} catch (Exception e) {
			jsonArray.add("");
    		resultJson.put("items", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		} 
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson.toString());
		response.getWriter().flush();
		response.getWriter().close();
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


	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE,userCode);
		return loggerDetailsHasMap;

	}	
}
