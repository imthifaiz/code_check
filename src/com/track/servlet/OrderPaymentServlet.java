package com.track.servlet;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;



import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.OrderTypeDAO;
import com.track.dao.PoHdrDAO;
import com.track.db.util.CurrencyUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.OrderPaymentUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PlantMstUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;
import com.track.dao.*;


public class OrderPaymentServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.OrderReceivingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.OrderReceivingServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 8800687029132079L;
	
	private String PLANT = "";
	private String xmlStr = "";
	private String action = "";
	

	//private static final String CONTENT_TYPE = "text/xml";

	StrUtils strUtils = new StrUtils();
	POUtil _poUtil = new POUtil();
	DateUtils _dateUtils = new DateUtils();
	DoHdrDAO DoHdrDAO = new DoHdrDAO();
	PoHdrDAO PoHdrDAO = new PoHdrDAO();
	
	OrderPaymentUtil _ordPaymentUtil = new OrderPaymentUtil();
	
	public OrderPaymentServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*public void init() throws ServletException {
	
	}*/

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//response.setContentType(CONTENT_TYPE);
		//PrintWriter out = response.getWriter();
		
		try{
			action = request.getParameter("action").trim();
			String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			this.setMapDataToLogger(this.populateMapData(StrUtils
					.fString(request.getParameter("PLANT")), StrUtils
					.fString(request.getParameter("LOGIN_USER"))
					+ " PDA_USER"));
			_poUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			
			if (action.equalsIgnoreCase("viewPaymentOrders")) {
				String orderType = StrUtils.fString(request.getParameter("Order"));
				String orderNo     = StrUtils.fString(request.getParameter("orderNo"));
				String ordRefNo     = StrUtils.fString(request.getParameter("ordRefNo"));
				String custName     = StrUtils.fString(request.getParameter("custName"));
				
                HttpSession session = request.getSession();
                String plant = (String)session.getAttribute("PLANT");
				
				ArrayList al=  new ArrayList();
            	if(orderType.equalsIgnoreCase("INBOUND")){
            		 al= _ordPaymentUtil.getIBOrderPaymentDetails(plant, orderNo, custName, ordRefNo);	
            		
            	}else if(orderType.equalsIgnoreCase("OUTBOUND")){
            		 al= _ordPaymentUtil.getOBOrderPaymentDetails(plant, orderNo, custName, ordRefNo);	
            			
            		
            	}
            	request.getSession().setAttribute("PAYMENT_ORDERS", al);
              
              // RequestDispatcher rd =  request.getRequestDispatcher("../jsp/paymentRecipt.jsp?action=View") ;
              // rd.forward(request, response);
              response.sendRedirect("jsp/paymentRecipt.jsp?action=View&Order="+orderType+"&orderNo="+orderNo+"&custName="+(StrUtils.replaceCharacters2Send(custName))+"&ordRefNo="+ordRefNo); 

			   
			} 
			else if (action.equalsIgnoreCase("processOrderPayment")) {
				xmlStr = "";
				xmlStr = processOrderPayment(request, response);

			}
			
			else if (action.equalsIgnoreCase("getPaymentdetails")) {
				String fdate="",tdate="";
				String FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
				String TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
				String order   = StrUtils.fString(request.getParameter("Order"));
				String orderNo     = StrUtils.fString(request.getParameter("orderNo"));
				String ordRefNo     = StrUtils.fString(request.getParameter("ordRefNo"));
				String custName     = StrUtils.fString(request.getParameter("custName"));
				
                HttpSession session = request.getSession();
                String plant = (String)session.getAttribute("PLANT");
                
                if (FROM_DATE.length()>5)

                	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

                	if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
                	if (TO_DATE.length()>5)
                		tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

				ArrayList al=  new ArrayList();
            	
				al= _ordPaymentUtil.getOrderPaymentDetails(plant, fdate, tdate, order, orderNo, custName, ordRefNo);	
            		
            	
            	request.getSession().setAttribute("PAYMENT_DETAILS", al);
                response.sendRedirect("jsp/maintpaymentRecipt.jsp?action=View&Order="+order+"&orderNo="+orderNo+"&custName="+(StrUtils.replaceCharacters2Send(custName))+"&ordRefNo="+ordRefNo+"&FROM_DATE="+FROM_DATE+"&TO_DATE="+TO_DATE); 

			   
			} 
			
			/*else if (action.equalsIgnoreCase("editOrderPayment")) {
				xmlStr = "";
				xmlStr = editOrderPayment(request, response);

			}*/
			else if (action.equalsIgnoreCase("deleteOrderPayment")) {
				xmlStr = "";
				xmlStr = deleteOrderPayment(request, response);

			}
			
			else if (action.equalsIgnoreCase("print")) {
				PrintPaymentReport(request, response);
			}
			else if (action.equalsIgnoreCase("printAgeingReport")) {
				PrintAgeingReport(request, response);
			}
			else if (action.equalsIgnoreCase("printAgeingReportbyOrder")) {
				PrintAgeingReportbyorder(request, response);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, "Unable to process? "+ e.getMessage());
			

		}

		//out.write(xmlStr);
		//out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private String processOrderPayment(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		
		boolean flag = false;
		StrUtils StrUtils = new StrUtils();
		
		Map issMat_HM = null;

		String PLANT = "", ORDERTYPE="",ORDNO = "", ORDER = "",PAYMENT_DATE = "",PAYMENT_MODE = "",PAYMENT_REMARKS = "",PAYMENT_REF = "",PAYMENT_AMT = "";
		String custName = "",LOGIN_USER = "",CUSTCODE="";
		 String orderNo="",ordRefNo="",balToPay="",paymentStatus ="",OtherPayment="";
		String strMovHisTranDate="",strTranDate="",PAYMENT_TYPE="",PAYMENT_ID="",STATUS_ID="";
		double amountRecipt = 0;
		//Boolean allChecked = false,fullIssue = false;
		//UserTransaction ut = null;
		Map checkedDOS = new HashMap();
		String sepratedtoken1 = "";
		Map mp = new HashMap();
		
		
		try {
			String sepratedtoken = "";
			String totalString = StrUtils.fString(request.getParameter("TRAVELER"));
			StringTokenizer parser = new StringTokenizer(totalString, "=");
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
	        String[] chkdOrdNos  = request.getParameterValues("chkdOrdNo");
	        ORDER=StrUtils.fString(request.getParameter("Order"));
	        ORDERTYPE=StrUtils.fString(request.getParameter("ORDERTYPE"));
	        orderNo = StrUtils.fString(request.getParameter("orderNo"));
	        ordRefNo = StrUtils.fString(request.getParameter("ordRefNo"));
	        custName= StrUtils.fString(request.getParameter("custName"));
	        PAYMENT_DATE = StrUtils.fString(request.getParameter("paymentDate"));
	        PAYMENT_MODE =  StrUtils.fString(request.getParameter("paymentMode"));
	        PAYMENT_REMARKS = StrUtils.fString(request.getParameter("paymentRemarks"));
	        PAYMENT_REF = StrUtils.fString(request.getParameter("paymentRefNo"));
	        PAYMENT_TYPE = StrUtils.fString(request.getParameter("paymentType"));
	        PAYMENT_ID =  StrUtils.fString(request.getParameter("PAYMENT_ID"));
			OtherPayment = StrUtils.fString(request.getParameter("payment"));
	        
			/*Payment = StrUtils.fString(request.getParameter("payment"));
	        
	        amountRecipt = Double.parseDouble(((String) Payment.trim().toString()));
			amountRecipt = strUtils.RoundDB(amountRecipt,IConstants.DECIMALPTS);
			String RECIPT_AMT = String.valueOf(amountRecipt);
			RECIPT_AMT = StrUtils.formatThreeDecimal(RECIPT_AMT); */
		
			
			if (PAYMENT_DATE.length()>5)
				strMovHisTranDate    = PAYMENT_DATE.substring(6)+"-"+PAYMENT_DATE.substring(3,5)+"-"+PAYMENT_DATE.substring(0,2);
			    strTranDate    = PAYMENT_DATE.substring(0,2)+"/"+ PAYMENT_DATE.substring(3,5)+"/"+PAYMENT_DATE.substring(6);
	         if (chkdOrdNos != null)    {     
    				for (int i = 0; i < chkdOrdNos.length; i++)       { 
    				String	data = chkdOrdNos[i];
    				String[] chkdata = data.split(",");
    				String dno=chkdata[0];
    				PAYMENT_AMT = StrUtils.fString(request.getParameter("payment_"+dno));
    		        checkedDOS.put(dno, PAYMENT_AMT);
    				}
    				session.setAttribute("checkedDOS", checkedDOS);
                }
    			
            if(!ORDER.equalsIgnoreCase("OTHERS")){
			while (parser.hasMoreTokens())

			{
				int count = 1;
				sepratedtoken = parser.nextToken();

				System.out.println("sepratedtoken ::" + sepratedtoken);
				StringTokenizer parser1 = new StringTokenizer(sepratedtoken,",");

				while (parser1.hasMoreTokens())

				{
					sepratedtoken1 = parser1.nextToken();

					mp.put("data" + count, sepratedtoken1);

					count++;

				}

			ORDNO = StrUtils.fString((String) mp.get("data1"));
			balToPay = StrUtils.fString((String) mp.get("data2"));
			/*ORDREFNO = StrUtils.fString((String) mp.get("data2"));
			QTYOR = StrUtils.fString((String) mp.get("data3"));
			ISSUEDQTY = StrUtils.fString((String) mp.get("data4"));
			CUST_NAME = StrUtils.replaceCharacters2Recv(StrUtils.fString((String) mp.get("data5")));*/
    		PAYMENT_AMT = StrUtils.fString(request.getParameter("payment_"+ORDNO));	
    		
			amountRecipt = Double.parseDouble(((String) PAYMENT_AMT.trim().toString()));
			amountRecipt = strUtils.RoundDB(amountRecipt,IConstants.DECIMALPTS);
			String RECIPT_AMT = String.valueOf(amountRecipt);
			RECIPT_AMT = StrUtils.formatThreeDecimal(RECIPT_AMT); 
			if(Double.parseDouble(balToPay) == Double.parseDouble(RECIPT_AMT) || Double.parseDouble(balToPay) < Double.parseDouble(RECIPT_AMT) ){
				paymentStatus="C";
				STATUS_ID = "PAID";
				
			}else{
				paymentStatus="O";
				STATUS_ID = "PARTIALLY PAID";
			}
			if(ORDER.equalsIgnoreCase("outbound"))
			{
				CUSTCODE = DoHdrDAO.getCustomerCode(PLANT, ORDNO,"OUTBOUND");
			}
			else if(ORDER.equalsIgnoreCase("inbound"))
			{
				CUSTCODE = PoHdrDAO.getSuppliercode(PLANT, ORDNO);
			}
					
				issMat_HM = new HashMap();
				issMat_HM.put(IConstants.PLANT, PLANT);
				issMat_HM.put("ORDNO",ORDNO );
				issMat_HM.put("ORDERNAME", ORDER);
				issMat_HM.put("ORDERTYPE", ORDERTYPE);
				issMat_HM.put("AMOUNT_PAID", RECIPT_AMT);
				issMat_HM.put("PAYMENT_DT", strTranDate);
				issMat_HM.put("PAYMENT_MODE", PAYMENT_MODE);
				issMat_HM.put("PAYMENT_REFNO", PAYMENT_REF);
				issMat_HM.put("PAYMENT_REMARKS", PAYMENT_REMARKS);
				issMat_HM.put("PAYMENT_STATUS", paymentStatus);
				issMat_HM.put("STATUS_ID", STATUS_ID);
				issMat_HM.put("PAYMENT_TYPE", PAYMENT_TYPE);
				issMat_HM.put("PAYMENT_ID", PAYMENT_ID);
				issMat_HM.put("COMMENT1", "");
				issMat_HM.put("COMMENT2", "");
				issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				issMat_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
				issMat_HM.put("CustCode", CUSTCODE);
				
				flag = new OrderPaymentUtil().process_OrderPayment(issMat_HM)&& true;
				
				}
				viewPaymentReport(request,response);
            }
            else
            {
            	amountRecipt = Double.parseDouble(((String) OtherPayment.trim().toString()));
    			amountRecipt = strUtils.RoundDB(amountRecipt,IConstants.DECIMALPTS);
    			String RECIPT_AMT = String.valueOf(amountRecipt);
    			RECIPT_AMT = StrUtils.formatThreeDecimal(RECIPT_AMT); 
    		
            	
            	HashMap payment_HM = new HashMap();
            	payment_HM.put(IConstants.PLANT, PLANT);
            	payment_HM.put("ORDNO","OTHERS");
            	payment_HM.put("ORDERNAME", ORDER);
            	payment_HM.put("ORDERTYPE", ORDERTYPE);
            	payment_HM.put("AMOUNT_PAID", OtherPayment);
            	payment_HM.put("PAYMENT_DT", strTranDate);
            	payment_HM.put("PAYMENT_MODE", PAYMENT_MODE);
            	payment_HM.put("PAYMENT_REFNO", PAYMENT_REF);
            	payment_HM.put("PAYMENT_REMARKS", PAYMENT_REMARKS);
            	payment_HM.put("PAYMENT_STATUS", paymentStatus);
            	payment_HM.put("STATUS_ID", STATUS_ID);
            	payment_HM.put("PAYMENT_TYPE", PAYMENT_TYPE);
		
            	payment_HM.put("PAYMENT_ID", PAYMENT_ID);
            	payment_HM.put("COMMENT1", "");
            	payment_HM.put("COMMENT2", "");
            	payment_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
            	payment_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
            	issMat_HM.put("CustCode", "");
            	
				flag = new OrderPaymentUtil().process_OrderPayment(payment_HM)&& true;
				
				
				String jasperPath = DbBean.JASPER_INPUT + "/" + "rptPAYMENT";
				String imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + DbBean.LOGO_FILE;
				String imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo1.gif";
				File checkImageFile = new File(imagePath);
		        if (!checkImageFile.exists()) {
		           imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
		        }
		        File checkImageFile1 = new File(imagePath1);
		        if (!checkImageFile1.exists()) {
		           imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
		        }

				
				Connection con = null;
				try {

					PlantMstUtil pmUtil = new PlantMstUtil();
					
					String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", 
						   CHPNO = "", CEMAIL = "",CRCBNO = "";
					
					con = DbBean.getConnection();

					String SysDate = DateUtils.getDate();
					Date sysTime1 = new Date(System.currentTimeMillis());
					Date cDt = new Date(System.currentTimeMillis());
					ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);

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
						CRCBNO= (String) map.get("RCBNO");

					}

					if (listQry.size() > 0) {
						   
			          	Map parameters = new HashMap();
						// Customer Details
					
			        	parameters.put("OrderNo", "Other");
						parameters.put("company", PLANT);
						parameters.put("To_CompanyName", "");
						parameters.put("To_BlockAddress", " ");
						parameters.put("To_RoadAddress", " " );
						parameters.put("To_Country", "");
						parameters.put("To_ZIPCode", "");
						parameters.put("To_TelNo", "");
						parameters.put("To_Fax", "");
						parameters.put("To_Email", "");
						parameters.put("To_ContactName", "");
		    
						// Company Details
						parameters.put("fromAddress_CompanyName", CNAME);
						parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
						parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
						parameters.put("fromAddress_Country", CCOUNTRY);
						parameters.put("fromAddress_ZIPCode", CZIP);
						parameters.put("fromAddress_TpNo", CTEL);
						parameters.put("fromAddress_FaxNo", CFAX);
						parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
						parameters.put("fromAddress_ContactPersonMobile", CHPNO);
						parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
						parameters.put("fromAddress_RCBNO", CRCBNO);
						parameters.put("ordDate", "");
					    parameters.put("orderAmt","");
					    parameters.put("paidAmt", StrUtils.currencyWtoutCommSymbol(RECIPT_AMT));
					    parameters.put("pmtDate",PAYMENT_DATE);
					    parameters.put("pmtMode",PAYMENT_MODE);
					    parameters.put("refNo", PAYMENT_REF);
						parameters.put("dueDate", "");
						parameters.put("imagePath", imagePath);
				        parameters.put("imagePath1", imagePath1);
						
						
						//report template parameter added on June 22 2011 By Bruhan
						String HEADER = "",RECEIVEDFROM="", Date="",PaymentMode="",ReferenceNo="",InvoiceNumber="";
					    String InvoiceDate = "",DueDate="",OriginalAmount="",Balance="",Payment = "",AmountCredited="",Total="",Memo="",Signature="";
					   
						OrderPaymentUtil paymentUtil = new OrderPaymentUtil();
				         Map ma= paymentUtil.getPaymentReceiptHdrDetails(PLANT);
				         
				         
				         parameters.put("lblHeader", (String) ma.get("HDR"));
						 parameters.put("lblReceivedfrom", (String) ma.get("RECEIVEDFROM"));
						 parameters.put("lblDate", (String) ma.get("PMTDATE"));
						 parameters.put("lblMode", (String) ma.get("PAYMENTMODE"));
						 parameters.put("lblRefNo", (String) ma.get("REFERENCENO"));
						 parameters.put("lblOrderNo", (String) ma.get("INVOICENUMBER"));
						 parameters.put("lblInvoiceDate", (String) ma.get("INVOICEDATE"));
						 parameters.put("lblDuedate", (String) ma.get("DUEDATE"));
						 parameters.put("lblAmount", (String) ma.get("ORIGINALAMOUNT"));
						 parameters.put("lblPayment", (String) ma.get("PAYMENT"));
						 parameters.put("lblAmtCredited", (String) ma.get("AMOUNTCREDITED"));
						 parameters.put("lblTotal", (String) ma.get("TOTAL"));
						 parameters.put("lblMemo", (String) ma.get("MEMO"));
						 parameters.put("lblSignature", (String) ma.get("SIGNATURE"));
				         
				           
						 byte[] bytes = JasperRunManager.runReportToPdf(jasperPath+ ".jasper", parameters, con);
					        response.addHeader("Content-disposition","attachment;filename=reporte.pdf");
					        response.setContentLength(bytes.length);
					        response.getOutputStream().write(bytes);
					        response.setContentType("application/pdf");
					    
															
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					DbBean.closeConnection(con);
				}
			
			
            }
			if (flag) {
				ArrayList al=  new ArrayList();
            	if(ORDER.equalsIgnoreCase("INBOUND")){
            		 al= _ordPaymentUtil.getIBOrderPaymentDetails(PLANT, orderNo, custName, ordRefNo);	
            		
            	}else if(ORDER.equalsIgnoreCase("OUTBOUND")){
            		 al= _ordPaymentUtil.getOBOrderPaymentDetails(PLANT, orderNo, custName, ordRefNo);		
            			
            		
            	}
            	request.getSession().setAttribute("PAYMENT_ORDERS", al);
            	request.getSession().setAttribute("RESULT","Payment for Order is processed successfully!");
				response.sendRedirect("jsp/paymentRecipt.jsp?action=View&Order="+ORDER+"&orderType="+ORDERTYPE+"&orderNo="+orderNo+"&custName="+(StrUtils.replaceCharacters2Send(custName))+"&ordRefNo="+ordRefNo+"&result=sucess");
				
			} else {
				//DbBean.RollbackTran(ut);
				request.getSession().setAttribute("RESULTERROR", "Failed to Process the Payment for Order : "+ ORDNO);
				response.sendRedirect("jsp/paymentRecipt.jsp?result=error");
			}
			
			}
			
		 catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR", "error");
			System.out.print((String)request.getSession().getAttribute("CATCHERROR"));
			response.sendRedirect("jsp/paymentRecipt.jsp?action=View&PLANT="+ PLANT + "&orderType=" + ORDERTYPE+"&orderNo="+orderNo
							+"&ordRefNo="
							+ordRefNo
							+"&custName="
							+(StrUtils.replaceCharacters2Send(custName))
							+"&result=catchrerror");
			throw e;
			
		}
		
		return xmlStr;
	}
/*
	private String editOrderPayment(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		
		boolean flag = false;
		StrUtils StrUtils = new StrUtils();
		
		String PLANT = "", ORDERTYPE="",ORDNO = "", ORDER = "",PAYMENT_DATE = "",PAYMENT_MODE = "",PAYMENT_REMARKS = "",PAYMENT_REF = "",PAYMENT_AMT = "";
		String custName = "",LOGIN_USER = "",ordamt="",totpaid="",paidamt="";
		 String orderNo="",ordRefNo="",id="",paymentStatus ="",Payment="";
		String strMovHisTranDate="",strTranDate="",PAYMENT_TYPE="",PAYMENT_ID="";
		double amountRecipt = 0;
		Map checkedDOS = new HashMap();
		String sepratedtoken1 = "";
		Map mp = null;
		mp = new HashMap();
		
		try {
			String sepratedtoken = "",fdate="",tdate="";
			String totalString = StrUtils.fString(request.getParameter("TRAVELER"));
			StringTokenizer parser = new StringTokenizer(totalString, "=");
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
	        String[] chkdOrdNos  = request.getParameterValues("chkdOrdNo");
	        
	        String FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
			   
            if (FROM_DATE.length()>5)

            	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

            	if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
            	if (TO_DATE.length()>5)
            		tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

	        ORDER=StrUtils.fString(request.getParameter("Order"));
	        ORDERTYPE=StrUtils.fString(request.getParameter("ORDERTYPE"));
	        orderNo = StrUtils.fString(request.getParameter("orderNo"));
	        ordRefNo = StrUtils.fString(request.getParameter("ordRefNo"));
	        custName= StrUtils.fString(request.getParameter("custName"));
	        PAYMENT_DATE = StrUtils.fString(request.getParameter("paymentDate"));
	        PAYMENT_MODE =  StrUtils.fString(request.getParameter("paymentMode"));
	        PAYMENT_REMARKS = StrUtils.fString(request.getParameter("paymentRemarks"));
	        PAYMENT_REF = StrUtils.fString(request.getParameter("paymentRefNo"));
	        PAYMENT_TYPE = StrUtils.fString(request.getParameter("paymentType"));
	        PAYMENT_ID =  StrUtils.fString(request.getParameter("PAYMENT_ID"));
	        Payment = StrUtils.fString(request.getParameter("payment"));
	        
	        amountRecipt = Double.parseDouble(((String) Payment.trim().toString()));
			amountRecipt = strUtils.RoundDB(amountRecipt,IConstants.DECIMALPTS);
			String RECIPT_AMT = String.valueOf(amountRecipt);
			RECIPT_AMT = StrUtils.formatThreeDecimal(RECIPT_AMT); 
		
			
			if (PAYMENT_DATE.length()>5)
				strMovHisTranDate    = PAYMENT_DATE.substring(6)+"-"+PAYMENT_DATE.substring(3,5)+"-"+PAYMENT_DATE.substring(0,2);
			    strTranDate    = PAYMENT_DATE.substring(0,2)+"/"+ PAYMENT_DATE.substring(3,5)+"/"+PAYMENT_DATE.substring(6);
	             		            
			while (parser.hasMoreTokens())

			{
				int count = 1;
				sepratedtoken = parser.nextToken();

				System.out.println("sepratedtoken ::" + sepratedtoken);
				StringTokenizer parser1 = new StringTokenizer(sepratedtoken,",");

				while (parser1.hasMoreTokens())

				{
					sepratedtoken1 = parser1.nextToken();

					mp.put("data" + count, sepratedtoken1);

					count++;

				}

			id = StrUtils.fString((String) mp.get("data1"));
			ORDNO = StrUtils.fString((String) mp.get("data2"));
			ordamt = StrUtils.fString((String) mp.get("data3"));
			paidamt = StrUtils.fString((String) mp.get("data4"));
			totpaid = StrUtils.fString((String) mp.get("data5"));
			
			double payment = Double.parseDouble(RECIPT_AMT)+(Double.parseDouble(totpaid)-Double.parseDouble(paidamt));
			
			if(Double.parseDouble(ordamt) == payment || Double.parseDouble(ordamt) < payment )
				{
					paymentStatus="C";
				}else{
					paymentStatus="O";
				}
			}	
				Hashtable issMat_HM = new Hashtable();
				issMat_HM.put(IConstants.PLANT, PLANT);
				issMat_HM.put("ID", id);
				if(ORDER.equalsIgnoreCase("OTHERS"))
				{
					issMat_HM.put("ORDNO","OTHERS" );
				}
				else
				{	
					issMat_HM.put("ORDNO",ORDNO );
				}
				issMat_HM.put("ORDERNAME", ORDER);
				issMat_HM.put("ORDERTYPE", ORDERTYPE);
				issMat_HM.put("AMOUNT_PAID", RECIPT_AMT);
				issMat_HM.put("PAYMENT_DT", strTranDate);
				issMat_HM.put("PAYMENT_MODE", PAYMENT_MODE);
				issMat_HM.put("PAYMENT_REFNO", PAYMENT_REF);
				issMat_HM.put("PAYMENT_REMARKS", PAYMENT_REMARKS);
				issMat_HM.put("PAYMENT_STATUS", paymentStatus);
				issMat_HM.put("PAYMENT_TYPE", PAYMENT_TYPE);
		
				issMat_HM.put("PAYMENT_ID", PAYMENT_ID);
				issMat_HM.put("COMMENT1", "");
				issMat_HM.put("COMMENT2", "");
				issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				issMat_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
				
				flag = new OrderPaymentUtil().updatepayment(issMat_HM);
				
				MovHisDAO movHisDao = new MovHisDAO();
				Hashtable<String,String> htRecvHis = new Hashtable<String,String>();
				htRecvHis.clear();
				htRecvHis.put(IDBConstants.PLANT, PLANT);
				htRecvHis.put("DIRTYPE", TransactionConstants.UPDATE_PAYMENT);
				htRecvHis.put(IDBConstants.ITEM, "");
				htRecvHis.put("MOVTID", "");
				htRecvHis.put("RECID", "");
				htRecvHis.put(IDBConstants.CUSTOMER_CODE, "");
				htRecvHis.put(IDBConstants.POHDR_JOB_NUM, PAYMENT_REF);
				if(ORDER.equalsIgnoreCase("OTHERS"))
				{
					htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, "OTHERS");
				}
				else
				{	
					htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, ORDNO);
				}
				
				htRecvHis.put(IDBConstants.LOC,PAYMENT_MODE);
				htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
				htRecvHis.put("BATNO", "");
				htRecvHis.put("QTY", RECIPT_AMT);
				htRecvHis.put(IDBConstants.TRAN_DATE,  strMovHisTranDate);
				htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				htRecvHis.put("REMARKS", PAYMENT_REMARKS);

				flag = movHisDao.insertIntoMovHis(htRecvHis);
				
				
			if (flag) {
				ArrayList al=  new ArrayList();
				al= _ordPaymentUtil.getOrderPaymentDetails(PLANT, fdate, tdate, ORDER, ORDNO, custName, ordRefNo);	
            		
            	
            	request.getSession().setAttribute("PAYMENT_DETAILS", al);
            	request.getSession().setAttribute("RESULT","Payment for Order :"+ ORDNO+" is updated successfully!");
    			response.sendRedirect("jsp/maintpaymentRecipt.jsp?action=View&Order="+ORDER+"&orderNo="+ORDNO+"&custName="+(StrUtils.replaceCharacters2Send(custName))+"&ordRefNo="+ordRefNo+"&result=sucess"); 
			 	
			} else {
				//DbBean.RollbackTran(ut);
				request.getSession().setAttribute("RESULTERROR", "Failed to update the Payment for Order : "+ ORDNO);
				response.sendRedirect("jsp/maintpaymentRecipt.jsp?result=error");
			}
			
			}
			
		 catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR", e.getMessage());
			System.out.print((String)request.getSession().getAttribute("CATCHERROR"));
			response.sendRedirect("jsp/maintpaymentRecipt.jsp?action=View&PLANT="+ PLANT + "Order="+ORDER+"&orderNo="+ORDNO
							+"&ordRefNo="
							+ordRefNo
							+"&custName="
							+(StrUtils.replaceCharacters2Send(custName))
							+"&result=catchrerror");
			throw e;
		}
		
		return xmlStr;
	}
	*/
	
	private String deleteOrderPayment(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		
		boolean flag = false;
		StrUtils StrUtils = new StrUtils();
		
		String PLANT = "", ORDERTYPE="",ORDNO = "", ORDER = "",PAYMENT_DATE = "",PAYMENT_MODE = "",PAYMENT_REMARKS = "",PAYMENT_REF = "",PAYMENT_AMT = "";
		String custName = "",LOGIN_USER = "",ordamt="",totpaid="",paidamt="",balamt="";
		 String orderNo="",ordRefNo="",id="",paymentStatus ="",Payment="";
		String strMovHisTranDate="",strTranDate="",PAYMENT_TYPE="",PAYMENT_ID="",STATUS_ID="";
		double amountRecipt = 0;
		Map checkedDOS = new HashMap();
		String sepratedtoken1 = "";
		Map mp = null;
		mp = new HashMap();
		
		try {
			String sepratedtoken = "",fdate="",tdate="";
			String totalString = StrUtils.fString(request.getParameter("TRAVELER"));
			StringTokenizer parser = new StringTokenizer(totalString, "=");
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
	        String[] chkdOrdNos  = request.getParameterValues("chkdOrdNo");
	        
	        String FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
			   
            if (FROM_DATE.length()>5)

            	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

            	if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
            	if (TO_DATE.length()>5)
            		tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

	        ORDER=StrUtils.fString(request.getParameter("Order"));
	        ordRefNo = StrUtils.fString(request.getParameter("ordRefNo"));
	        custName= StrUtils.fString(request.getParameter("custName"));
	     	          		         		            
			while (parser.hasMoreTokens())

			{
				int count = 1;
				sepratedtoken = parser.nextToken();

				System.out.println("sepratedtoken ::" + sepratedtoken);
				StringTokenizer parser1 = new StringTokenizer(sepratedtoken,",");

				while (parser1.hasMoreTokens())

				{
					sepratedtoken1 = parser1.nextToken();

					mp.put("data" + count, sepratedtoken1);

					count++;

				}

				id = StrUtils.fString((String) mp.get("data1"));
				ORDNO = StrUtils.fString((String) mp.get("data2"));
				ordamt = StrUtils.fString((String) mp.get("data3"));
				paidamt = StrUtils.fString((String) mp.get("data4"));
				balamt = StrUtils.fString((String) mp.get("data5"));
				
				if(Double.parseDouble(ordamt) == Double.parseDouble(paidamt)+Double.parseDouble(balamt)){
					paymentStatus="N";
					STATUS_ID = "";
					
				}else{
					paymentStatus="O";
					STATUS_ID = "PARTIALLY PAID";
				}
				
				
				Hashtable issMat_HM = new Hashtable();
				issMat_HM.put(IConstants.PLANT, PLANT);
				issMat_HM.put("ID", id);
				if(ORDER.equalsIgnoreCase("OTHERS"))
				{
					issMat_HM.put("ORDNO","OTHERS" );
				}
				else
				{	
					issMat_HM.put("ORDNO",ORDNO );
				}
				issMat_HM.put("ORDERNAME", ORDER);
				issMat_HM.put(IDBConstants.CREATED_BY, LOGIN_USER);
				
				flag = new OrderPaymentUtil().deletepaymentLineDetails(issMat_HM);
				
				String queryHdr = "";
				if(flag){
					if(ORDER.equalsIgnoreCase("inbound"))
					{
						PoHdrDAO _PoHdrDAO = new PoHdrDAO();
						_PoHdrDAO.setmLogger(mLogger);
						Hashtable<String,String> htCondiPoHdr = new Hashtable<String,String>();
						htCondiPoHdr.put("PLANT", PLANT);
						htCondiPoHdr.put("pono", ORDNO);

						flag = _PoHdrDAO.isExisit(htCondiPoHdr,"");

						if (flag)
					
							queryHdr = "set  PAYMENT_STATUS = '"+paymentStatus+"',STATUS_ID ='"+STATUS_ID+"'";
					
						else
							throw new Exception ("Order No. "+ORDNO+" not found to update the payment status");

						flag = _PoHdrDAO.updatePO(queryHdr, htCondiPoHdr, "");

					}
					else if(ORDER.equalsIgnoreCase("outbound"))
					{
						DoHdrDAO _DoHdrDAO = new DoHdrDAO();
						_DoHdrDAO.setmLogger(mLogger);
						Hashtable<String,String> htCondiDoHdr = new Hashtable<String,String>();
						htCondiDoHdr.put("PLANT", PLANT);
						htCondiDoHdr.put("Dono", ORDNO);

							flag = _DoHdrDAO.isExisit(htCondiDoHdr,"");

							if (flag)
								queryHdr = "set  PAYMENT_STATUS = '"+paymentStatus+"',STATUS_ID ='"+STATUS_ID+"'";
							else
								throw new Exception ("Order No. "+ORDNO+" not found to update the payment status");

							flag = _DoHdrDAO.update(queryHdr, htCondiDoHdr, "");

					}
				}
				
			}
			
			if (flag) {
				ArrayList al=  new ArrayList();
				al= _ordPaymentUtil.getOrderPaymentDetails(PLANT, fdate, tdate, ORDER, ORDNO, custName, ordRefNo);	
            		
            	
            	request.getSession().setAttribute("PAYMENT_DETAILS", al);
            	request.getSession().setAttribute("RESULT","Payment for Orders is deleted successfully!");
    			response.sendRedirect("jsp/maintpaymentRecipt.jsp?action=View&Order="+ORDER+"&custName="+(StrUtils.replaceCharacters2Send(custName))+"&ordRefNo="+ordRefNo+"&result=sucess"); 
			 	
			} else {
				//DbBean.RollbackTran(ut);
				request.getSession().setAttribute("RESULTERROR", "Failed to update the Payment for Orders");
				response.sendRedirect("jsp/maintpaymentRecipt.jsp?result=error");
			}
			
			}catch (Exception e) 
			{
				this.mLogger.exception(this.printLog, "", e);
				request.getSession().setAttribute("CATCHERROR", e.getMessage());
				System.out.print((String)request.getSession().getAttribute("CATCHERROR"));
				response.sendRedirect("jsp/maintpaymentRecipt.jsp?action=View&PLANT="+ PLANT + "Order="+ORDER
							+"&ordRefNo="
							+ordRefNo
							+"&custName="
							+(StrUtils.replaceCharacters2Send(custName))
							+"&result=catchrerror");
			throw e;
		}
		
		return xmlStr;
	}

	public void viewPaymentReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		Connection con = null;
		try {

			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
		    CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			Map m = null;
			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", 
				   CHPNO = "", CEMAIL = "",CRCBNO="";
			
			String Orderno ="",Duetopay="",OrdDate="",OrderAmt="",chkddata="",PAYMENT_AMT="";
			String ORDER ="",ordNo="",PAYMENT_DATE="",PAYMENT_MODE="",PAYMENT_REF="";
			String sCustCode ="",sCustName="",sAddr1="",sAddr2="",sAddr3="",sAddr4="",sCountry="",sZip="",sContactName="",
					sTelNo="",sHpNo="",sFax="",sEmail="",pmtdays="",dueDate="";
					ArrayList arrCust = new ArrayList();
			List jasperPrintList = new ArrayList();
			
			String PLANT = (String) session.getAttribute("PLANT");
			String[] chkdOrdNos  = request.getParameterValues("chkdOrdNo");
			ORDER=StrUtils.fString(request.getParameter("Order"));
	        ordNo = StrUtils.fString(request.getParameter("orderNo"));
	        PAYMENT_DATE = StrUtils.fString(request.getParameter("paymentDate"));
	        PAYMENT_MODE =  StrUtils.fString(request.getParameter("paymentMode"));
	        PAYMENT_REF = StrUtils.fString(request.getParameter("paymentRefNo"));
	        						
			con = DbBean.getConnection();

			String SysDate = DateUtils.getDate();
			String jasperPath = DbBean.JASPER_INPUT + "/" + "rptPAYMENT";
			String imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo1.gif";
			File checkImageFile = new File(imagePath);
	        if (!checkImageFile.exists()) {
	           imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	        }
	        File checkImageFile1 = new File(imagePath1);
	        if (!checkImageFile1.exists()) {
	           imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	        }

			java.util.Date cDt = new java.util.Date(System.currentTimeMillis());
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
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
			for (int i = 0; i < chkdOrdNos.length; i++){
				 chkddata = chkdOrdNos[i];
				 String [] pmtdata = chkddata.split(",");
				 Orderno = pmtdata[0];
				 Duetopay = pmtdata[1];
				 OrdDate = pmtdata[2];
				 OrderAmt = pmtdata[3];
				 PAYMENT_AMT = StrUtils.fString(request.getParameter("payment_"+Orderno));	
				 if(ORDER.equalsIgnoreCase("outbound")){
					 arrCust = cUtil.getCustomerDetailsForDO(Orderno, PLANT);
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
						 pmtdays = (String) arrCust.get(28);
						}
				 }
				 else
				 {
					 arrCust = customerBeanDAO.getVendorDetailsForPO( PLANT,Orderno);
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
						 pmtdays = (String) arrCust.get(29);
						}
					
				 }
				 if(pmtdays.length()>0){
				 int days = Integer.parseInt(pmtdays);
				 
				 DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			     Date parsedDate = sdf.parse(OrdDate);
			      
			     Calendar now = Calendar.getInstance();
			     now.setTime(parsedDate); 
			     now.add(Calendar.DAY_OF_MONTH, days);
			     String time = now.getTime().toString();
			   
			     DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
			     Date date = (Date)formatter.parse(time);
			      
			     now.setTime(date);
			     dueDate = now.get(Calendar.DATE) + "/" + (now.get(Calendar.MONTH) + 1) + "/" +now.get(Calendar.YEAR);
			    			
				 }
				 
			if (arrCust.size() > 0) {
				
                                
	            Map parameters = new HashMap();
				// Customer Details
				parameters.put("OrderNo", Orderno);
				parameters.put("company", PLANT);
				parameters.put("To_CompanyName", sCustName);
				parameters.put("To_BlockAddress", sAddr1+"  " + sAddr2);
				parameters.put("To_RoadAddress", sAddr3 +"  " + sAddr4);
				parameters.put("To_Country", sCountry);
				parameters.put("To_ZIPCode", sZip);
				parameters.put("To_TelNo", sTelNo);
				parameters.put("To_Fax", sFax);
				parameters.put("To_Email", sEmail);
				parameters.put("To_ContactName", sContactName);
    
				// Company Details
				parameters.put("fromAddress_CompanyName", CNAME);
				parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
				parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
				parameters.put("fromAddress_Country", CCOUNTRY);
				parameters.put("fromAddress_ZIPCode", CZIP);
				parameters.put("fromAddress_TpNo", CTEL);
				parameters.put("fromAddress_FaxNo", CFAX);
				parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
				parameters.put("fromAddress_ContactPersonMobile", CHPNO);
				parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
				parameters.put("fromAddress_RCBNO", CRCBNO);
				parameters.put("ordDate", OrdDate);
			    parameters.put("orderAmt",OrderAmt);
			    parameters.put("paidAmt", StrUtils.currencyWtoutCommSymbol(PAYMENT_AMT));
			    parameters.put("pmtDate",PAYMENT_DATE);
			    parameters.put("pmtMode",PAYMENT_MODE);
			    parameters.put("refNo", PAYMENT_REF);
				parameters.put("dueDate", dueDate);
				parameters.put("imagePath", imagePath);
		        parameters.put("imagePath1", imagePath1);
				
				
				//report template parameter added on June 22 2011 By Bruhan
				OrderPaymentUtil paymentUtil = new OrderPaymentUtil();
		         Map ma= paymentUtil.getPaymentReceiptHdrDetails(PLANT);
		           		           
				parameters.put("lblHeader", (String) ma.get("HDR"));
				parameters.put("lblReceivedfrom", (String) ma.get("RECEIVEDFROM"));
				parameters.put("lblDate", (String) ma.get("PMTDATE"));
				parameters.put("lblMode", (String) ma.get("PAYMENTMODE"));
				parameters.put("lblRefNo", (String) ma.get("REFERENCENO"));
				parameters.put("lblOrderNo", (String) ma.get("INVOICENUMBER"));
				parameters.put("lblInvoiceDate", (String) ma.get("INVOICEDATE"));
				parameters.put("lblDuedate", (String) ma.get("DUEDATE"));
				parameters.put("lblAmount", (String) ma.get("ORIGINALAMOUNT"));
				parameters.put("lblPayment", (String) ma.get("PAYMENT"));
				parameters.put("lblAmtCredited", (String) ma.get("AMOUNTCREDITED"));
				parameters.put("lblTotal", (String) ma.get("TOTAL"));
				parameters.put("lblMemo", (String) ma.get("MEMO"));
				parameters.put("lblSignature", (String) ma.get("SIGNATURE"));
				
				
				long start = System.currentTimeMillis();
				System.out.println("**************" + " Start Up Time : "
						+ start + "**********");
				
				jasperPrintList.add((JasperFillManager.fillReport(jasperPath+ ".jasper",parameters, con)));
				
			}
			}
			    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				JRPdfExporter exporter = new JRPdfExporter();	
				exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
				//exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "D:/jasper/MultipleDO.pdf");
				exporter.exportReport();
				byte[] bytes = byteArrayOutputStream.toByteArray();
				response.addHeader("Content-disposition","attachment;filename=PaymentReceipt.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}	
	
	public void PrintPaymentReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		Connection con = null;
		try {

			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
		    CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			Map m = null;
			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", 
				   CHPNO = "", CEMAIL = "",CRCBNO="";
			
			String Orderno ="",ordRefNo="",custName="",Duetopay="",OrdDate="",OrderAmt="",chkddata="",PAYMENT_AMT="";
			String ORDER ="",ordNo="",PAYMENT_DATE="",PAYMENT_MODE="",PAYMENT_REF="",id="";
			String sCustCode ="",sCustName="",sAddr1="",sAddr2="",sAddr3="",sAddr4="",sCountry="",sZip="",sContactName="",
					sTelNo="",sHpNo="",sFax="",sEmail="",pmtdays="",dueDate="";
					ArrayList arrCust = new ArrayList();
			List jasperPrintList = new ArrayList();
			
			String PLANT = (String) session.getAttribute("PLANT");
			String[] chkdOrdNos  = request.getParameterValues("chkdOrdNo");
			ORDER=StrUtils.fString(request.getParameter("Order"));
	        ordNo = StrUtils.fString(request.getParameter("orderNo"));
	        ordRefNo = StrUtils.fString(request.getParameter("ordRefNo"));
	        custName= StrUtils.fString(request.getParameter("custName"));
	          						
			con = DbBean.getConnection();

			String SysDate = DateUtils.getDate();
			String jasperPath = DbBean.JASPER_INPUT + "/" + "rptPAYMENT";
			
			String imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo1.gif";
			File checkImageFile = new File(imagePath);
	        if (!checkImageFile.exists()) {
	           imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	        }
	        File checkImageFile1 = new File(imagePath1);
	        if (!checkImageFile1.exists()) {
	           imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	        }

			java.util.Date cDt = new java.util.Date(System.currentTimeMillis());
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
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
			for (int i = 0; i < chkdOrdNos.length; i++){
				 chkddata = chkdOrdNos[i];
				 String [] pmtdata = chkddata.split(",");
				 id = pmtdata[0];
				 Orderno = pmtdata[1];
				 OrderAmt = pmtdata[2];
				 PAYMENT_AMT = pmtdata[3];
				 Duetopay = pmtdata[4];
				 PAYMENT_DATE = pmtdata[5];
				 PAYMENT_MODE = pmtdata[6];
				 PAYMENT_REF = pmtdata[7];
				 OrdDate = pmtdata[8];
				
				 if(ORDER.equalsIgnoreCase("outbound")){
					 arrCust = cUtil.getCustomerDetailsForDO(Orderno, PLANT);
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
						 pmtdays = (String) arrCust.get(28);
						}
				 }
				 else if(ORDER.equalsIgnoreCase("inbound"))
				 {
					 arrCust = customerBeanDAO.getVendorDetailsForPO(PLANT,Orderno);
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
						 pmtdays = (String) arrCust.get(29);
						}
					
				 }
				 if(pmtdays.length()>0){
				 int days = Integer.parseInt(pmtdays);
				 
				 DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			     Date parsedDate = sdf.parse(OrdDate);
			      
			     Calendar now = Calendar.getInstance();
			     now.setTime(parsedDate); 
			     now.add(Calendar.DAY_OF_MONTH, days);
			     String time = now.getTime().toString();
			   
			     DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
			     Date date = (Date)formatter.parse(time);
			      
			     now.setTime(date);
			     dueDate = now.get(Calendar.DATE) + "/" + (now.get(Calendar.MONTH) + 1) + "/" +now.get(Calendar.YEAR);
			    			
				 }
				 
				 
				 
			//if (arrCust.size() > 0) {
				
                                
	            Map parameters = new HashMap();
				// Customer Details
				parameters.put("OrderNo", Orderno);
				parameters.put("company", PLANT);
				parameters.put("To_CompanyName", sCustName);
				parameters.put("To_BlockAddress", sAddr1+"  " + sAddr2);
				parameters.put("To_RoadAddress", sAddr3 +"  " + sAddr4);
				parameters.put("To_Country", sCountry);
				parameters.put("To_ZIPCode", sZip);
				parameters.put("To_TelNo", sTelNo);
				parameters.put("To_Fax", sFax);
				parameters.put("To_Email", sEmail);
				parameters.put("To_ContactName", sContactName);
    
				// Company Details
				parameters.put("fromAddress_CompanyName", CNAME);
				parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
				parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
				parameters.put("fromAddress_Country", CCOUNTRY);
				parameters.put("fromAddress_ZIPCode", CZIP);
				parameters.put("fromAddress_TpNo", CTEL);
				parameters.put("fromAddress_FaxNo", CFAX);
				parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
				parameters.put("fromAddress_ContactPersonMobile", CHPNO);
				parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
				parameters.put("fromAddress_RCBNO", CRCBNO);
				parameters.put("ordDate", OrdDate);
			    parameters.put("orderAmt",OrderAmt);
			    parameters.put("paidAmt", PAYMENT_AMT);
			    parameters.put("pmtDate",PAYMENT_DATE);
			    parameters.put("pmtMode",PAYMENT_MODE);
			    parameters.put("refNo", PAYMENT_REF);
				parameters.put("dueDate", dueDate);
				parameters.put("imagePath", imagePath);
		        parameters.put("imagePath1", imagePath1);
				
				//report template parameter added on June 22 2011 By Bruhan
				OrderPaymentUtil paymentUtil = new OrderPaymentUtil();
		         Map ma= paymentUtil.getPaymentReceiptHdrDetails(PLANT);
		           		           
				parameters.put("lblHeader", (String) ma.get("HDR"));
				parameters.put("lblReceivedfrom", (String) ma.get("RECEIVEDFROM"));
				parameters.put("lblDate", (String) ma.get("PMTDATE"));
				parameters.put("lblMode", (String) ma.get("PAYMENTMODE"));
				parameters.put("lblRefNo", (String) ma.get("REFERENCENO"));
				parameters.put("lblOrderNo", (String) ma.get("INVOICENUMBER"));
				parameters.put("lblInvoiceDate", (String) ma.get("INVOICEDATE"));
				parameters.put("lblDuedate", (String) ma.get("DUEDATE"));
				parameters.put("lblAmount", (String) ma.get("ORIGINALAMOUNT"));
				parameters.put("lblPayment", (String) ma.get("PAYMENT"));
				parameters.put("lblAmtCredited", (String) ma.get("AMOUNTCREDITED"));
				parameters.put("lblTotal", (String) ma.get("TOTAL"));
				parameters.put("lblMemo", (String) ma.get("MEMO"));
				parameters.put("lblSignature", (String) ma.get("SIGNATURE"));
				
				
				long start = System.currentTimeMillis();
				System.out.println("**************" + " Start Up Time : "
						+ start + "**********");
				
				jasperPrintList.add((JasperFillManager.fillReport(jasperPath+ ".jasper",parameters, con)));
				
			//}
			}
			    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				JRPdfExporter exporter = new JRPdfExporter();	
				exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
				//exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "D:/jasper/MultipleDO.pdf");
				exporter.exportReport();
				byte[] bytes = byteArrayOutputStream.toByteArray();
				response.addHeader("Content-disposition","attachment;filename=PaymentReceipt.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}
	
	public void PrintAgeingReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		Connection con = null;
		try {

			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
		    CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			Map m = null;
			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", 
				   CHPNO = "", CEMAIL = "",CRCBNO="",LOGIN_USER="";
			
			String Orderno ="",ordRefNo="",custName="",custcode="",Duetopay="",OrdDate="",OrderAmt="",chkddata="",PAYMENT_AMT="";
			String ORDER ="",ordNo="",PAYMENT_DATE="",PAYMENT_MODE="",PAYMENT_REF="",id="";
			String sCustCode ="",sCustName="",sAddr1="",sAddr2="",sAddr3="",sAddr4="",sCountry="",sZip="",sContactName="",
			STATEMENT_DATE="",sTelNo="",sHpNo="",sFax="",sEmail="",pmtdays="",dueDate="",FROM_DATE="",TO_DATE="",fdate="",tdate="";
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
	        
	        if (FROM_DATE.length()>5)

	        	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	       	if (TO_DATE.length()>5)
	        	tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
 
	        String searchCond="",extracondpmt="",dtCondStr="",
	        		extraCon="",dtCondpmt="",paymenttype="";
		   
            if(ORDER.equalsIgnoreCase("outbound"))
            {
            	extraCon = " ORDERNAME='OUTBOUND' ";
            }
            else if(ORDER.equalsIgnoreCase("inbound"))
            {
            	extraCon = " ORDERNAME='INBOUND' ";
            }
            if(ORDER.equalsIgnoreCase("outbound"))
            {
            	paymenttype = " and ISNULL(PAYMENT_TYPE,'')='CREDIT' ";
            }
            else if(ORDER.equalsIgnoreCase("inbound"))
            {
            	paymenttype = " and ISNULL(PAYMENT_TYPE,'')='DEBIT' ";
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
	        
	        
	          						
			con = DbBean.getConnection();

			String SysDate = DateUtils.getDate();
			String jasperPath = "";
			
				jasperPath = DbBean.JASPER_INPUT + "/" + "rptAgeingDetails";
			
			String imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo1.gif";
			File checkImageFile = new File(imagePath);
	        if (!checkImageFile.exists()) {
	           imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	        }
	        File checkImageFile1 = new File(imagePath1);
	        if (!checkImageFile1.exists()) {
	           imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	        }

			java.util.Date cDt = new java.util.Date(System.currentTimeMillis());
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
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
			
		boolean flag =  _ordPaymentUtil.InsertTempOrderPayment(PLANT, fdate, tdate, ORDER, custName);
	
		arrCustomers =  _ordPaymentUtil.getcustomerorsuppliername(PLANT, fdate, tdate, ORDER, custName);
		for (int i = 0; i < arrCustomers.size(); i++)
		{
			Map lineArrcust = (Map) arrCustomers.get(i);
                      
			custcode = (String)lineArrcust.get("custcode");
			custName = (String)lineArrcust.get("custname");
			
             extracondpmt="";
             if(custcode.length()>0){
            	 extracondpmt = " AND "+extraCon + searchCond + " AND (   CustCode = '"+custcode+"') ";
              	
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
             
             
             
             
			if(ORDER.equalsIgnoreCase("outbound")){
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
			 else if(ORDER.equalsIgnoreCase("inbound"))
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
				 double currentdue=0;double days30=0;double days60=0;double days90=0;double days120=0;double amtdue=0;
				 String curDate = STATEMENT_DATE;
				 curDate = curDate.substring(0,2) + curDate.substring(3,5) + curDate.substring(6);
				 int pdays = 0;
				 if(pmtdays.length()>0)
				 {
	  				 pdays = Integer.parseInt(pmtdays);
	  			 }
				 movQryList = _ordPaymentUtil.getAgeingDetails(PLANT, fdate, tdate, ORDER, custName,"");
				 
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
				
				/* if(pmtdays.length()>0){
				 int days = Integer.parseInt(pmtdays);
				 
				 DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			     Date parsedDate = sdf.parse(OrdDate);
			      
			     Calendar now = Calendar.getInstance();
			     now.setTime(parsedDate); 
			     now.add(Calendar.DAY_OF_MONTH, days);
			     String time = now.getTime().toString();
			   
			     DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
			     Date date = (Date)formatter.parse(time);
			      
			     now.setTime(date);
			     dueDate = now.get(Calendar.DATE) + "/" + (now.get(Calendar.MONTH) + 1) + "/" +now.get(Calendar.YEAR);
			    			
				 }*/
				 
			                
	            Map parameters = new HashMap();
				// Customer Details
				parameters.put("company", PLANT);
				parameters.put("To_CompanyName", sCustName);
				parameters.put("To_BlockAddress", sAddr1+"  " + sAddr2);
				parameters.put("To_RoadAddress", sAddr3 +"  " + sAddr4);
				parameters.put("To_Country", sCountry);
				parameters.put("To_ZIPCode", sZip);
				parameters.put("To_TelNo", sTelNo);
				parameters.put("To_Fax", sFax);
				parameters.put("To_Email", sEmail);
				parameters.put("To_ContactName", sContactName);
    
				// Company Details
				parameters.put("fromAddress_CompanyName", CNAME);
				parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
				parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
				parameters.put("fromAddress_Country", CCOUNTRY);
				parameters.put("fromAddress_ZIPCode", CZIP);
				parameters.put("fromAddress_TpNo", CTEL);
				parameters.put("fromAddress_FaxNo", CFAX);
				parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
				parameters.put("fromAddress_ContactPersonMobile", CHPNO);
				parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
				parameters.put("fromAddress_RCBNO", CRCBNO);
				parameters.put("imagePath", imagePath);
		        parameters.put("imagePath1", imagePath1);
		        parameters.put("OrdDateRange", extracondpmt);
		        parameters.put("ExtraCond", extraCon);
		        parameters.put("Paymenttype", paymenttype);
		        parameters.put("ORDERTYPE", ORDER);
		        parameters.put("PmtDateRange", searchCond);
				
				//report template parameter added on June 22 2011 By Bruhan
			//	OrderPaymentUtil paymentUtil = new OrderPaymentUtil();
		     //    Map ma= paymentUtil.getPaymentReceiptHdrDetails(PLANT);
		           		           
				parameters.put("lblHeader", "To");
				parameters.put("lblDate", "Date");
				parameters.put("lblOrderNo", "Order No");
				parameters.put("lblAmount", "Amount");
				parameters.put("lblBalance", "Balance");
				parameters.put("lblcurrentdue", "Current Due");
				parameters.put("lbl30daysdue", "1-30 Days Past Due");
				parameters.put("lbl60daysdue", "31-60 Days Past Due");
				parameters.put("lbl90daysdue", "61-90 Days Past Due");
				parameters.put("lbl90+daysdue", "90+ Days Past Due");
				parameters.put("lblamtdue", "Amount Due");
				parameters.put("lblstatementno", "Statement No");
				parameters.put("lblstatement", "Statement");
				parameters.put("CurDate", STATEMENT_DATE);
				parameters.put("currentdue", currentdueamt);
				parameters.put("30daysdue", days30due);
				parameters.put("60daysdue", days60due);
				parameters.put("90daysdue", days90due);
				parameters.put("90+daysdue", days120due);
				parameters.put("amountdue", totaldue);
				parameters.put("statementNo", statementNo);
				
				long start = System.currentTimeMillis();
				System.out.println("**************" + " Start Up Time : "
						+ start + "**********");
				
				jasperPrintList.add((JasperFillManager.fillReport(jasperPath+ ".jasper",parameters, con)));
			 }
		}
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		JRPdfExporter exporter = new JRPdfExporter();	
		exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
		//exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "D:/jasper/MultipleDO.pdf");
		exporter.exportReport();
	
		byte[] bytes = byteArrayOutputStream.toByteArray();

		response.addHeader("Content-disposition","attachment;filename=AgeingReport.pdf");
		response.setContentLength(bytes.length);
		response.getOutputStream().write(bytes);
		response.setContentType("application/pdf");
				

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}	
	
	public void PrintAgeingReportbyorder(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		Connection con = null;
		try {

			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
		    CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			Map m = null;
			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", 
				   CHPNO = "", CEMAIL = "",CRCBNO="",LOGIN_USER="",StatementDate="";
			
			String Orderno ="",OrderNo="",custName="",Duetopay="",OrdDate="",OrderAmt="",chkddata="",PAYMENT_AMT="";
			String ORDER ="",ordNo="",PAYMENT_DATE="",PAYMENT_MODE="",PAYMENT_REF="",id="";
			String sCustCode ="",sCustName="",sAddr1="",sAddr2="",sAddr3="",sAddr4="",sCountry="",sZip="",sContactName="",
					sTelNo="",sHpNo="",sFax="",sEmail="",pmtdays="",dueDate="",fdate="",tdate="";
					ArrayList arrCust = new ArrayList();
			List jasperPrintList = new ArrayList();
			
			String PLANT = (String) session.getAttribute("PLANT");
			LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			ORDER=StrUtils.fString(request.getParameter("Order"));
			OrderNo = StrUtils.fString(request.getParameter("OrderNo"));
	        custName= StrUtils.fString(request.getParameter("custName"));
	        StatementDate= StrUtils.fString(request.getParameter("StatementDate"));
	        fdate= StrUtils.fString(request.getParameter("fromDate"));
	        tdate= StrUtils.fString(request.getParameter("toDate"));
	        
	        
	         
	        String extraCon="",paymenttype="",dtCondStr="",searchCond="";
		   
            if(ORDER.equalsIgnoreCase("outbound"))
            {
            	extraCon = " ORDERNAME='OUTBOUND' ";
            }
            else if(ORDER.equalsIgnoreCase("inbound"))
            {
            	extraCon = " ORDERNAME='INBOUND' ";
            }
            if(ORDER.equalsIgnoreCase("outbound"))
            {
            	paymenttype = " and ISNULL(PAYMENT_TYPE,'')='CREDIT' ";
            }
            else if(ORDER.equalsIgnoreCase("inbound"))
            {
            	paymenttype = " and ISNULL(PAYMENT_TYPE,'')='DEBIT' ";
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
	        
            
	        
			con = DbBean.getConnection();

			String SysDate = DateUtils.getDate();
			String jasperPath = "";
			
			jasperPath = DbBean.JASPER_INPUT + "/" + "rptAgeingDetailsforOrder";
			
			String imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo1.gif";
			File checkImageFile = new File(imagePath);
	        if (!checkImageFile.exists()) {
	           imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	        }
	        File checkImageFile1 = new File(imagePath1);
	        if (!checkImageFile1.exists()) {
	           imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
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
             

			java.util.Date cDt = new java.util.Date(System.currentTimeMillis());
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
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
			 if(ORDER.equalsIgnoreCase("outbound")){
					 arrCust = cUtil.getCustomerDetailsForDO(OrderNo, PLANT);
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
						 pmtdays = (String) arrCust.get(28);
						}
				 }
			 else if(ORDER.equalsIgnoreCase("inbound"))
				 {
					 arrCust = customerBeanDAO.getVendorDetailsForPO(PLANT, OrderNo);
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
						 pmtdays = (String) arrCust.get(29);
						}
					
				 }
			 
			 if(arrCust.size() > 0){
				 
				 ArrayList movQryList  = new ArrayList();
				 double currentdue=0;double days30=0;double days60=0;double days90=0;double days120=0;double amtdue=0;
				 //String curDate =_dateUtils.getDate();
				 String curDate =StatementDate;
				 curDate = curDate.substring(0,2) + curDate.substring(3,5) + curDate.substring(6);
				 int pdays = 0;
				 if(pmtdays.length()>0)
				 {
	  				 pdays = Integer.parseInt(pmtdays);
	  			 }
				 movQryList = _ordPaymentUtil.getAgeingDetails(PLANT, fdate, tdate, ORDER, "",OrderNo);
				 
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
				
				/* if(pmtdays.length()>0){
				 int days = Integer.parseInt(pmtdays);
				 
				 DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			     Date parsedDate = sdf.parse(OrdDate);
			      
			     Calendar now = Calendar.getInstance();
			     now.setTime(parsedDate); 
			     now.add(Calendar.DAY_OF_MONTH, days);
			     String time = now.getTime().toString();
			   
			     DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
			     Date date = (Date)formatter.parse(time);
			      
			     now.setTime(date);
			     dueDate = now.get(Calendar.DATE) + "/" + (now.get(Calendar.MONTH) + 1) + "/" +now.get(Calendar.YEAR);
			    			
				 }*/
				 
			                
	            Map parameters = new HashMap();
				// Customer Details
				parameters.put("company", PLANT);
				parameters.put("To_CompanyName", sCustName);
				parameters.put("To_BlockAddress", sAddr1+"  " + sAddr2);
				parameters.put("To_RoadAddress", sAddr3 +"  " + sAddr4);
				parameters.put("To_Country", sCountry);
				parameters.put("To_ZIPCode", sZip);
				parameters.put("To_TelNo", sTelNo);
				parameters.put("To_Fax", sFax);
				parameters.put("To_Email", sEmail);
				parameters.put("To_ContactName", sContactName);
    
				// Company Details
				parameters.put("fromAddress_CompanyName", CNAME);
				parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
				parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
				parameters.put("fromAddress_Country", CCOUNTRY);
				parameters.put("fromAddress_ZIPCode", CZIP);
				parameters.put("fromAddress_TpNo", CTEL);
				parameters.put("fromAddress_FaxNo", CFAX);
				parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
				parameters.put("fromAddress_ContactPersonMobile", CHPNO);
				parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
				parameters.put("fromAddress_RCBNO", CRCBNO);
				parameters.put("imagePath", imagePath);
		        parameters.put("imagePath1", imagePath1);
		        parameters.put("ORDERTYPE", ORDER);
		        parameters.put("Ordnum", OrderNo);
		        parameters.put("ExtraCond", extraCon);
		        parameters.put("Paymenttype", paymenttype);
		        parameters.put("OrdDateRange", searchCond);
				
				//report template parameter added on June 22 2011 By Bruhan
			//	OrderPaymentUtil paymentUtil = new OrderPaymentUtil();
		     //    Map ma= paymentUtil.getPaymentReceiptHdrDetails(PLANT);
		           		           
				parameters.put("lblHeader", "To");
				parameters.put("lblDate", "Date");
				parameters.put("lblOrderNo", "Order No");
				parameters.put("lblAmount", "Amount");
				parameters.put("lblBalance", "Balance");
				parameters.put("lblcurrentdue", "Current Due");
				parameters.put("lbl30daysdue", "1-30 Days Past Due");
				parameters.put("lbl60daysdue", "31-60 Days Past Due");
				parameters.put("lbl90daysdue", "61-90 Days Past Due");
				parameters.put("lbl90+daysdue", "90+ Days Past Due");
				parameters.put("lblamtdue", "Amount Due");
				parameters.put("lblstatementno", "Statement No");
				parameters.put("lblstatement", "Statement");
				parameters.put("CurDate", StatementDate);
				parameters.put("currentdue", currentdueamt);
				parameters.put("30daysdue", days30due);
				parameters.put("60daysdue", days60due);
				parameters.put("90daysdue", days90due);
				parameters.put("90+daysdue", days120due);
				parameters.put("amountdue", totaldue);	
				parameters.put("statementNo", statementNo);
				
				long start = System.currentTimeMillis();
				System.out.println("**************" + " Start Up Time : "
						+ start + "**********");
				
				byte[] bytes = JasperRunManager.runReportToPdf(jasperPath+ ".jasper", parameters, con);
				response.addHeader("Content-disposition","attachment;filename=AgeingReport.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");	
			
			}
			   

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}	
	
	
	@SuppressWarnings("unchecked")
	private void diaplayInfoLogs(HttpServletRequest request) {
		try {
			Map requestParameterMap = request.getParameterMap();
			Set<String> keyMap = requestParameterMap.keySet();
			StringBuffer requestParams = new StringBuffer();
			requestParams.append("Class Name : " + this.getClass() + "\n");
			requestParams.append("Paramter Mapping : \n");
			for (String key : keyMap) {
				requestParams.append("[" + key + " : "
						+ request.getParameter(key) + "] ");
			}
			this.mLogger.auditInfo(this.printInfo, requestParams.toString());

		} catch (Exception e) {

		}

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

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
}
