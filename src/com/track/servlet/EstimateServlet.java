package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.util.CellRangeAddress;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EmailMsgDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.EstHdrDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.OrderTypeDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TblControlDAO;
import com.track.db.object.FinCountryTaxType;
import com.track.db.util.CurrencyUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.DOTransferUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.ESTUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.HTReportUtil;
import com.track.db.util.InvoicePaymentUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.gates.sqlBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * Servlet implementation class EstimateServlet
 */
public class EstimateServlet extends HttpServlet implements IMLogger {
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.EstimateServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.EstimateServlet_PRINTPLANTMASTERINFO;
	boolean masterHisFlag=false;
	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	String action = "";
	String xmlStr = "";
	String statusValue="";
	CurrencyUtil curUtil = null;
	ESTUtil EstUtil = null;
	HTReportUtil htutil = null;
	StrUtils strUtils = null;
	EmailMsgDAO emailDao =null;
	EmailMsgUtil mailUtil = null;
	TblControlDAO _TblControlDAO = null;
	DateUtils _dateUtils = null;
	EstDetDAO _EstDetDAO = null;
	DOUtil dOUtil = null;
	DOTransferUtil _DOTransferUtil = null;
	MasterDAO _MasterDAO = null;
	ItemMstDAO _ItemMstDAO = null;
	PlantMstDAO _PlantMstDAO  = null;
	MasterUtil _masterUtil = null;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		curUtil = new CurrencyUtil();
		EstUtil = new ESTUtil();
		htutil = new HTReportUtil();
		strUtils = new StrUtils();
		emailDao =  new EmailMsgDAO();
		mailUtil = new EmailMsgUtil();
		_TblControlDAO = new TblControlDAO();
		_dateUtils = new DateUtils();
		_EstDetDAO = new EstDetDAO();
		dOUtil = new DOUtil();
		_DOTransferUtil = new DOTransferUtil();
		_MasterDAO = new MasterDAO ();
		 _PlantMstDAO =new PlantMstDAO();
		 _ItemMstDAO = new ItemMstDAO();
		 _masterUtil = new MasterUtil();
	}
	
	//private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EstimateServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			action = StrUtils.fString(request.getParameter("Submit")).trim();
			String orderNo = StrUtils.fString(request.getParameter("ESTNO")).trim();
			String rflag = StrUtils.fString((String) request.getSession().getAttribute("RFLAG"));
			statusValue = StrUtils.fString(request.getParameter("statusValue"));
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			
			
			
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			
			//---Added by Bruhan on April 17 2014, Description: To display Work Order error message
			request.getSession().setAttribute("RESULTERROR", "");
			//---Added by Bruhan on April 17 2014, Description: To display Work Order error message
		 if (action.equalsIgnoreCase("SAVE")) {
				String result = "";
				result = SaveData(request, response);
				request.getSession().setAttribute("RESULT", result);
				response.sendRedirect("jsp/displayResult2User.jsp");
		  }
		 else if (action.equalsIgnoreCase("EDIT_PURCHASE_ESTIMATE")) {
				PoHdrDAO poHdr = new PoHdrDAO();
				boolean poHdrUpt = false;
				String result = "";
				String header1 = request.getParameter("InboundOrderHeader").trim();
				String header2 = request.getParameter("InvoiceOrderToHeader").trim();
				String header3 = request.getParameter("FromHeader").trim();
				String date = request.getParameter("Date").trim();
				String orderno = request.getParameter("OrderNo").trim();
				String refno = request.getParameter("RefNo").trim();
				String terms = request.getParameter("Terms").trim();
				String termsdetails = request.getParameter("TermsDetails").trim();
				String sono = request.getParameter("SoNo").trim();
				String item = request.getParameter("Item").trim();
				String description = request.getParameter("Description").trim();
				String orderqty = request.getParameter("OrderQty").trim();
				String uom = request.getParameter("UOM").trim();
				String rate = request.getParameter("Rate").trim();
				String taxamount = request.getParameter("TaxAmount").trim();
				String amt = request.getParameter("Amt").trim();
				String subtotal = request.getParameter("SubTotal").trim();
				String totaltax = request.getParameter("TotalTax").trim();
				String total = request.getParameter("Total").trim();
				String Footer1 = StrUtils.fString(request.getParameter("Footer1"));
				String Footer2 = StrUtils.fString(request.getParameter("Footer2"));
				String Footer3 = StrUtils.fString(request.getParameter("Footer3"));
				String Footer4 = StrUtils.fString(request.getParameter("Footer4"));
				String Footer5 = StrUtils.fString(request.getParameter("Footer5"));
				String Footer6 = StrUtils.fString(request.getParameter("Footer6"));
				String Footer7 = StrUtils.fString(request.getParameter("Footer7"));
				String Footer8 = StrUtils.fString(request.getParameter("Footer8"));
				String Footer9 = StrUtils.fString(request.getParameter("Footer9"));
				String DisplayByOrdertype = (request.getParameter("DisplayByOrdertype") != null) ? "1" : "0";
				String printDetailDesc = (request.getParameter("printDetailDesc") != null) ? "1": "0"; 
				String printSupTerms = (request.getParameter("printSupTerms") != null) ? "1": "0"; 
				String printSupRemarks = (request.getParameter("printSupRemarks") != null) ? "1": "0";
				String printwithproductremarks = (request.getParameter("printwithproductremarks") != null) ? "1": "0";
				String printwithbrand = (request.getParameter("printwithbrand") != null) ? "1": "0";
				String rcbno = request.getParameter("RCBNO").trim();
				String remark1 = request.getParameter("REMARK1").trim();
			    String remark2 = request.getParameter("REMARK2").trim();
			    String deliverydate = request.getParameter("DeliveryDate").trim();
			   	String shipto = request.getParameter("ShipTo").trim();
			    String companydate = request.getParameter("CompanyDate").trim();
			    String companyname = request.getParameter("CompanyName").trim();
			    String companystamp = request.getParameter("CompanyStamp").trim();
			    String companysig = request.getParameter("CompanySig").trim(); 
				String printDisReport = (request.getParameter("printDisReport") != null) ? "1": "0"; 
			    String discount = request.getParameter("Discount").trim();
				String netrate = request.getParameter("NetRate").trim();
				String Orientation = request.getParameter("Orientation").trim();
			    String orderdiscount = request.getParameter("OrderDiscount").trim();
		        String shippingcost = request.getParameter("ShippingCost").trim();
		        String Roundoff = request.getParameter("Roundoff").trim();
			    String incoterm = request.getParameter("INCOTERM").trim();
			    String supplierrcbno = request.getParameter("SUPPLIERRCBNO").trim();
			    String uenno = request.getParameter("UENNO").trim();
			    String supplieruenno = request.getParameter("SUPPLIERUENNO").trim();
			    String grno = request.getParameter("GRNO").trim();
			    String totalafterdiscount = request.getParameter("TOTALAFTERDISCOUNT").trim();
			    String grndate = request.getParameter("GRNDATE").trim();
			    String PreparedBy = request.getParameter("PreparedBy").trim();
			    String AuthSignature = request.getParameter("AuthSignature").trim();
				String printRoundoffTotalwithDecimal = (request.getParameter("printRoundoffTotalwithDecimal") != null) ? "1": "0";
				String prdDeliveryDate = request.getParameter("prdDeliveryDate").trim();
			    String printWithPrdDeliveryDate = (request.getParameter("printWithPrdDeliveryDate") != null) ? "1": "0";
			    String ExpiryDate = request.getParameter("ExpiryDate").trim();
			    String printWithExpiryDate = (request.getParameter("printWithExpiryDate") != null) ? "1": "0";
			    String printWithDeliveryDate = (request.getParameter("printWithDeliveryDate") != null) ? "1": "0";
			    String showPreviousPurchaseCost = (request.getParameter("showPreviousPurchaseCost") != null) ? "1": "0";
			    String calculateTaxwithShippingCost = (request.getParameter("calculateTaxwithShippingCost") != null) ? "1": "0";
			    String BillNo = request.getParameter("BILLNO").trim();
			    String BillDate = request.getParameter("BILLDATE").trim();
			    String Adjustment = request.getParameter("Adjustment").trim();
			    String ProductRatesAre = request.getParameter("ProductRatesAre").trim();
			    String project = request.getParameter("Project").trim();
			    String printwithproject = (request.getParameter("printwithproject") != null) ? "1": "0";
			    String printWithSupplierUENNO = (request.getParameter("printWithSupplierUENNO") != null) ? "1": "0";
			    String printWithUENNO = (request.getParameter("printWithUENNO") != null) ? "1": "0";
			    String printwithcompanysig = (request.getParameter("printwithcompanysig") != null) ? "1": "0";
			    String printwithcompanyseal = (request.getParameter("printwithcompanyseal") != null) ? "1": "0";
			    String TransportMode = request.getParameter("TransportMode").trim();
			    String printwithtransportmode = (request.getParameter("printwithtransportmode") != null) ? "1": "0";
			    String employee = request.getParameter("employee").trim();
			    String printWithemployee = (request.getParameter("printWithemployee") != null) ? "1": "0";

				Hashtable<String, String> ht = new Hashtable<String, String>();
				ht.put(IDBConstants.PLANT, plant);
				StringBuffer QryUpdate = new StringBuffer(" SET ");

				if (header1.length() > 0)
					QryUpdate.append(" ORDERHEADER = '" + header1 + "' ");
				if (header2.length() > 0)
					QryUpdate.append(", TOHEADER = '" + header2 + "' ");
				if (header3.length() > 0)
					QryUpdate.append(", FROMHEADER = '" + header3 + "' ");
				if (date.length() > 0)
					QryUpdate.append(", DATE = '" + date + "' ");
				if (orderno.length() > 0)
					QryUpdate.append(", ORDERNO = '" + orderno + "' ");
				if (refno.length() > 0)
					QryUpdate.append(", REFNO = '" + refno + "' ");
				if (terms.length() > 0)
					QryUpdate.append(", TERMS = '" + terms + "' ");
				//if (termsdetails.length() > 0)
					QryUpdate.append(", TERMSDETAILS = '" + termsdetails + "' ");
				if (sono.length() > 0)
					QryUpdate.append(", SONO = '" + sono + "' ");
				if (item.length() > 0)
					QryUpdate.append(", ITEM = '" + item + "' ");
				if (description.length() > 0)
					QryUpdate.append(", DESCRIPTION = '" + description + "' ");
				if (orderqty.length() > 0)
					QryUpdate.append(", ORDERQTY = '" + orderqty + "' ");
				if (uom.length() > 0)
					QryUpdate.append(", UOM = '" + uom + "' ");
				if (rate.length() > 0)
					QryUpdate.append(", RATE = '" + rate + "' ");
				if (taxamount.length() > 0)
					QryUpdate.append(", TAXAMOUNT = '" + taxamount + "' ");
				if (amt.length() > 0)
					QryUpdate.append(", AMT = '" + amt + "' ");
				if (subtotal.length() > 0)
					QryUpdate.append(", SubTotal = '" + subtotal + "' ");
				if (totaltax.length() > 0)
					QryUpdate.append(", TotalTax = '" + totaltax + "' ");
				if (total.length() > 0)
					QryUpdate.append(", Total = '" + total + "' ");
				if (totalafterdiscount.length() > 0)
					QryUpdate.append(", TOTALAFTERDISCOUNT = '" + totalafterdiscount + "' ");

				QryUpdate.append(", FOOTER1 = '" + Footer1 + "' ");
				QryUpdate.append(", FOOTER2 = '" + Footer2 + "' ");
				QryUpdate.append(", FOOTER3 = '" + Footer3 + "' ");
				QryUpdate.append(", FOOTER4 = '" + Footer4 + "' ");
				QryUpdate.append(", FOOTER5 = '" + Footer5 + "' ");
				QryUpdate.append(", FOOTER6 = '" + Footer6 + "' ");
				QryUpdate.append(", FOOTER7 = '" + Footer7 + "' ");
				QryUpdate.append(", FOOTER8 = '" + Footer8 + "' ");
				QryUpdate.append(", FOOTER9 = '" + Footer9 + "' ");
				QryUpdate.append(", DISPLAYBYORDERTYPE ='" + DisplayByOrdertype+ "' ");
			    QryUpdate.append(", PRINTXTRADETAILS ='" + printDetailDesc+ "' ");
			    QryUpdate.append(", PRINTSUPTERMS ='" + printSupTerms+ "' ");
			    QryUpdate.append(", PSUPREMARKS ='" + printSupRemarks+ "' ");
			    QryUpdate.append(", PrintOrientation = '" + Orientation + "' ");
			    QryUpdate.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarks+ "' ");
			    QryUpdate.append(", PRINTWITHBRAND ='" + printwithbrand+ "' ");
				
			    if (rcbno.length() > 0)
					QryUpdate.append(", RCBNO = '" + rcbno + "' ");
			    
			    if (supplierrcbno.length() > 0)
					QryUpdate.append(", SUPPLIERRCBNO = '" + supplierrcbno + "' ");
			    
			    if (uenno.length() > 0)
			    	QryUpdate.append(", UENNO = '" + uenno + "' ");
			    
			    if (supplieruenno.length() > 0)
					QryUpdate.append(", SUPPLIERUENNO = '" + supplieruenno + "' ");
			    
			    if (grno.length() > 0)
					QryUpdate.append(", GRNO = '" + grno + "' ");
			    
			    if (grndate.length() > 0)
					QryUpdate.append(", GRNDATE = '" + grndate + "' ");
			    
			    if (remark1.length() > 0)
					QryUpdate.append(", REMARK1 = '" + remark1 + "' ");
			    if (remark2.length() > 0)
					QryUpdate.append(", REMARK2 = '" + remark2 + "' ");
			    if (deliverydate.length() > 0)
					QryUpdate.append(", DELIVERYDATE = '" + deliverydate + "' ");
				if (shipto.length() > 0)
					QryUpdate.append(", SHIPTO = '" + shipto + "' ");
				if (companydate.length() > 0)
					QryUpdate.append(", COMPANYDATE = '" + companydate + "' ");
				if (companyname.length() > 0)
					QryUpdate.append(", COMPANYNAME = '" + companyname + "' ");
				if (companystamp.length() > 0)
					QryUpdate.append(", COMPANYSTAMP = '" + companystamp + "' ");
				if (companysig.length() > 0)
					QryUpdate.append(", COMPANYSIG = '" + companysig + "' ");
					QryUpdate.append(", PRINTWITHDISCOUNT  ='" + printDisReport + "' ");
				if (discount.length() > 0)
					QryUpdate.append(", DISCOUNT = '" + discount + "' ");
				if (netrate.length() > 0)
					QryUpdate.append(", NETRATE = '" + netrate + "' ");
				if (orderdiscount.length() > 0)
					QryUpdate.append(", ORDERDISCOUNT = '" + orderdiscount + "' ");
				if (shippingcost.length() > 0)
					QryUpdate.append(", SHIPPINGCOST = '" + shippingcost + "' ");
				if (Roundoff.length() > 0)
					QryUpdate.append(", ROUNDOFFTOTALWITHDECIMAL = '" + Roundoff + "' ");
				if (incoterm.length() > 0)
					QryUpdate.append(", INCOTERM = '" + incoterm + "' ");
				if (PreparedBy.length() > 0)
					QryUpdate.append(", PREPAREDBY = '" + PreparedBy + "' ");
			    if (AuthSignature.length() > 0)
					QryUpdate.append(", AUTHSIGNATURE = '" + AuthSignature + "' ");
					
				QryUpdate.append(", PRINTROUNDOFFTOTALWITHDECIMAL ='" + printRoundoffTotalwithDecimal+ "' ");
				
				if (prdDeliveryDate.length() > 0)
				QryUpdate.append(", PRODUCTDELIVERYDATE = '" + prdDeliveryDate + "' ");	
			    QryUpdate.append(", PRINTWITHPRODUCTDELIVERYDATE ='" + printWithPrdDeliveryDate+ "' ");
			    QryUpdate.append(", EXPIREDATE ='" + ExpiryDate+ "' ");
			    QryUpdate.append(", PRINTWITHEXPIREDATE = '" + printWithExpiryDate + "' ");	
			    QryUpdate.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDate+ "' ");
			    QryUpdate.append(", SHOWPREVIOUSPURCHASECOST ='" + showPreviousPurchaseCost+ "' ");
			    QryUpdate.append(", CALCULATETAXWITHSHIPPINGCOST ='" + calculateTaxwithShippingCost+ "' ");
			    QryUpdate.append(", BILLNO ='" + BillNo+ "' ");
			    QryUpdate.append(", BILLDATE ='" + BillDate+ "' ");
			    QryUpdate.append(", ADJUSTMENT ='" + Adjustment+ "' ");
			    QryUpdate.append(", PRODUCTRATESARE ='" + ProductRatesAre+ "' ");
			    
				if (project.length() > 0)
					QryUpdate.append(", PROJECT = '" + project + "' ");	

				if (TransportMode.length() > 0)
					QryUpdate.append(", TRANSPORT_MODE = '" + TransportMode + "' ");	
				
				if (employee.length() > 0)
					QryUpdate.append(", EMPLOYEE = '" + employee + "' ");	
				
				QryUpdate.append(", PRINTWITHTRANSPORT_MODE ='" + printwithtransportmode+ "' ");
				QryUpdate.append(", PRINTWITHPROJECT ='" + printwithproject+ "' ");
				QryUpdate.append(", printWithUENNO ='" + printWithUENNO+ "' ");
				QryUpdate.append(", PRINTWITHSUPPLIERUENNO ='" + printWithSupplierUENNO+ "' ");
				QryUpdate.append(", PRINTWITHCOMPANYSIG='" + printwithcompanysig+ "' ");
				QryUpdate.append(", PRINTWITHCOMPANYSEAL='" + printwithcompanyseal+ "' ");
				QryUpdate.append(", PRINTEMPLOYEE='" + printWithemployee+ "' ");
			
				try {
				poHdrUpt = poHdr.updatePurchaseEstimateHeader(QryUpdate.toString(), ht, " ");
				} catch (Exception e) {

				}
				System.out.println("action :: :" + poHdrUpt);
				if (!poHdrUpt) {

					result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Failed to edit the details</font>";
					response
					.sendRedirect("../edit/purchaseestimateconfig?result="
							+ result);
				} else {
					result = "<font class = "
						+ IConstants.SUCCESS_COLOR
						+ ">Purchase Estimate Order Printout edited successfully</font>";
					response
					.sendRedirect("../edit/purchaseestimateconfig?result="
							+ result);
				}
		
			} 
		 else if (action.equalsIgnoreCase("Add Products")) {

		        String ordertypeString ="",orderaddProductPage="";
			String result = "";
			ArrayList al = new ArrayList();
			Hashtable htCond = new Hashtable();
			String estno = StrUtils.fString(request.getParameter("ESTNO"))
					.trim();
			String RFLAG = StrUtils.fString(request.getParameter("RFLAG"))
					.trim();
		     
			htCond.put("PLANT", plant);
			htCond.put("ESTNO", estno);
			String query = "estno,custCode,custName,jobNum";
			EstUtil.setmLogger(mLogger);
			al = EstUtil.getestHdrDetails(query, htCond);
                         if(RFLAG.equals("1") || RFLAG.equals("2")){
                             ordertypeString="Sales Estimate" ;
                             orderaddProductPage ="createESTDET.jsp";
                         }
			if (al.size() < 1) {
                       
                         
				result = "<font color=\"red\"> Please Save "+ordertypeString+" order first before adding product  <br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = ""+"<br><h3>" + result;
				request.getSession().setAttribute("RESULT", result);
				response.sendRedirect("jsp/displayResult2User.jsp");
			} else {
							
				Boolean flag = EstUtil.isOpenEstimateOrder(plant, estno);
					if (flag) {
						throw new Exception(
								" Processed Sales Estimate Order Cannot be modified");
					}
					Map m = (Map) al.get(0);
					estno = (String) m.get("estno");
					String jobNum = (String) m.get("jobNum");
					String custName = (String) m.get("custName");
					String custCode = (String) m.get("custCode");
					response.sendRedirect("jsp/"+orderaddProductPage+"?Submit=add&ESTNO="
									+ estno
									+ "&JOB_NUM="
									+ StrUtils.replaceCharacters2Send(jobNum)
									+ "&CUST_CODE="
									+ custCode
									+ "&RFLAG="
									+ RFLAG
									+ "&CUST_NAME="
									+ StrUtils.replaceCharacters2Send(custName));
				}
			
		}
		 else if (action.equalsIgnoreCase("GET_ORDER_NO_FOR_AUTO_SUGGESTION")) {
			 JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getOrderNoForAutoSuggestion(request);
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(jsonObjectResult.toString());
	            response.getWriter().flush();
	            response.getWriter().close();
			} 
		 
		  else if (action.equalsIgnoreCase("Auto-Generate")) {				
               response.sendRedirect("jsp/CreateEstimate.jsp?action=Auto-Generate");
	     }
		 else if (action.equalsIgnoreCase("View")) {
               	boolean flag = DisplayData(request, response);
				String estno = "";
				if (rflag.equals("1")) {
					estno = (String) request.getSession().getAttribute("estno");
					request.getSession().setAttribute("estno", "");
					response.sendRedirect("jsp/CreateEstimate.jsp?ESTNO=" + estno + "&action=View");
				}else if (rflag.equals("2") || rflag.equals("3") || rflag.equals("4") || rflag.equals("5")) {
								
					estno = (String) request.getSession().getAttribute("estno");
					if(request.getParameter("ESTNO")!=null)
					estno = request.getParameter("ESTNO");
					request.getSession().setAttribute("estno", "");
					response.sendRedirect("jsp/maintEstimate.jsp?ESTNO="+ estno + "&statusValue=" + statusValue + "&action=View");
				}else {
					estno = (String) request.getSession().getAttribute("estno");
					request.getSession().setAttribute("estno", "");
					response.sendRedirect("jsp/CreateEstimate.jsp?ESTNO=" + estno + "&action=View");
				}
					
		 }else if (action.equalsIgnoreCase("Add Product")) {
				boolean dotransferdetflag = false;
				String estno = "", custName = "", custCode = "", jobNum = "", estlno = "", item = "", 
				itemDesc = "", result = "",taxby="",prodgst="";
			        String ordertypeString="",orderaddProductPage="";
				MovHisDAO movHisDao = new MovHisDAO();
				ShipHisDAO shipdao = new ShipHisDAO();
				movHisDao.setmLogger(mLogger);
				shipdao.setmLogger(mLogger);
				DateUtils dateUtils = new DateUtils(); boolean shipflag=false;
				estno = StrUtils.fString(request.getParameter("ESTNO")).trim();
				estlno = StrUtils.fString(request.getParameter("ESTLNNO")).trim();
				String user_id = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
				custCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
				custName = StrUtils.fString(request.getParameter("CUST_NAME")).trim();
				jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
				item = StrUtils.fString(request.getParameter("ITEM")).trim();
				itemDesc = StrUtils.fString(request.getParameter("DESC")).trim();
				String qty = StrUtils.fString(request.getParameter("QTY")).trim();
				String price = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UNITPRICE")).trim());
				String UNITPRICERD= StrUtils.fString(request.getParameter("UNITPRICERD")).trim();
				String PRODUCTDELIVERYDATE  = StrUtils.fString(request.getParameter("PRODUCTDELIVERYDATE")).trim();
				if(price.equalsIgnoreCase(""))
					price="0";
				if(UNITPRICERD.equalsIgnoreCase("0"))						
					UNITPRICERD=price;
				else if(UNITPRICERD.equalsIgnoreCase(""))
					UNITPRICERD=price;
			        String productRemarks = StrUtils.fString(request.getParameter("PRDREMARKS")).trim();
					String uom = StrUtils.fString(request.getParameter("UOM")).trim();
				 //Start code added by Bruhan for base currency on Aug 28th 2012.
				price = new ESTUtil().getConvertedUnitCostToLocalCurrency(plant,estno,UNITPRICERD) ;
				 //Start code added by Bruhan for base currency on Aug 28th 2012.
				String RFLAG = StrUtils.fString(request.getParameter("RFLAG")).trim();
			       System.out.print("RFLAG ##############################"+RFLAG);          
                             if(RFLAG.equals("1") || RFLAG.equals("2") ){
                                 ordertypeString="Estimate" ;
                                 orderaddProductPage ="createESTDET.jsp";
                             }
				ItemMstUtil itemMstUtil = new ItemMstUtil();
				itemMstUtil.setmLogger(mLogger);
				String temItem = itemMstUtil.isValidAlternateItemInItemmst(plant, item);
				if(temItem!=""){
					item = temItem;
				}else{
					throw new Exception("Product not found!");
				}
				
			   Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant,item);
               String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
			   String nonstocktypeDesc= StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
				 itemDesc =StrUtils.fString((String) mPrddet.get("ITEMDESC"));			
				
				Hashtable htPodet = new Hashtable();
				Hashtable htShiphis = new Hashtable();
				htPodet.put(IDBConstants.PLANT, plant);
				htPodet.put(IDBConstants.ESTHDR_ESTNUM, estno);
			    htPodet.put(IDBConstants.UNITMO,uom);
				htPodet.put(IDBConstants.DODET_ITEM, item);
				htPodet.put("ITEMDESC", strUtils.InsertQuotes(itemDesc));
			//	htPodet.put("UNITMO", StrUtils.fString((String) mPrddet.get("STKUOM")));
				htPodet.put(IDBConstants.DODET_QTYOR, qty);
				htPodet.put(IDBConstants.DODET_UNITPRICE, price);
				//htPodet.put(IDBConstants.DODET_COMMENT1,strUtils.InsertQuotes(productRemarks));
				htPodet.put(IDBConstants.ESTHDR_ESTLNNUM, estlno);
				//Start code added by Bruhan for base Currency inclusion 
				String CURRENCYUSEQT = new ESTUtil().getCurrencyUseQT(plant, estno);
				htPodet.put(IDBConstants.CURRENCYUSEQT, CURRENCYUSEQT);
				htPodet.put(IDBConstants.PODET_PRODUCTDELIVERYDATE, PRODUCTDELIVERYDATE);
				if(nonstocktype.equals("Y"))	
				    {
				    	if(nonstocktypeDesc.equalsIgnoreCase("discount")||nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")){ 
                             htPodet.put(IDBConstants.DODET_UNITPRICE, "-"+price);
                           }
				    	
				    	
				    }
				   
				 htPodet.put("STATUS", "N");
					
				java.util.Date dt = new java.util.Date();
				SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
				String today = dfVisualDate.format(dt);

				htPodet.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
				/*to get product gst*/
				taxby=_PlantMstDAO.getTaxBy(plant);
				if(taxby.equalsIgnoreCase("BYPRODUCT"))
				{
					prodgst=_ItemMstDAO.getProductGst(plant, item);
					htPodet.put(IDBConstants.PRODGST, prodgst);
				}
				/**/
				int numberOfDecimal = Integer.parseInt(_PlantMstDAO.getNumberOfDecimal(plant));
				
				String estHdrQuery = " currencyid,isnull(outbound_Gst,0) as outbound_Gst ";
				Hashtable htEstHdr = new Hashtable();
				htEstHdr.put("ESTNO", estno);
				htEstHdr.put("PLANT", plant);
				
				ArrayList arrayEstHdr = new ArrayList();
				arrayEstHdr = EstUtil.getestHdrDetails(estHdrQuery, htEstHdr);
				Map dataMap = (Map) arrayEstHdr.get(0);
				String gstValue = dataMap.get("outbound_Gst").toString();
			    Float gstpercentage =  Float.parseFloat(((String) gstValue));
			    
			    double dOrderPrice = Double.parseDouble(price) * Double.parseDouble(qty);
			    dOrderPrice = StrUtils.RoundDB(dOrderPrice, numberOfDecimal);
			    double tax = (dOrderPrice*gstpercentage)/100;
			    tax = StrUtils.RoundDB(tax, numberOfDecimal);
			    double orderAmount = dOrderPrice+tax;
			    orderAmount = StrUtils.RoundDB(orderAmount,numberOfDecimal);
			    
				Map resultMap = getOutstandingAmountForCustomer(custCode, orderAmount, plant);
				boolean isAmntExceed = (boolean) resultMap.get("STATUS");
				String amntExceedMsg = (String) resultMap.get("MSG");
				/**/
				/*to get product gst end*/
				EstUtil.setmLogger(mLogger);
				boolean flag = false;
				if(!isAmntExceed) {
					flag = EstUtil.saveestDetDetails(htPodet);
				}
				//insert estimate multi remarks
				if(flag)
				{
					String DYNAMIC_REMARKS_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_REMARKS_SIZE"));
					int remarks_count = Integer.parseInt(DYNAMIC_REMARKS_SIZE);
					String productDORemarks=""; 
					for(int index=0;index<remarks_count;index++){
						productDORemarks = StrUtils.fString(request.getParameter("PRDREMARKS"+"_"+index));
						Hashtable htRemarks = new Hashtable();
						htRemarks.put(IDBConstants.PLANT, plant);
						htRemarks.put(IDBConstants.ESTHDR_ESTNUM, estno);
						htRemarks.put(IDBConstants.ESTHDR_ESTLNNUM, estlno);
						htRemarks.put(IDBConstants.DODET_ITEM, item);
						htRemarks.put(IDBConstants.REMARKS,strUtils.InsertQuotes(productDORemarks));
						htRemarks.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						htRemarks.put(IDBConstants.CREATED_BY,userName);
						flag = EstUtil.saveEstimateoMultiRemarks(htRemarks);
						
						Hashtable htMaster = new Hashtable();
						if(productDORemarks.length()>0)
						{
						
							htMaster.put(IDBConstants.PLANT, plant);
							htMaster.put(IDBConstants.REMARKS ,strUtils.InsertQuotes(productDORemarks));
							
							if(!_MasterDAO.isExisitRemarks(htMaster, ""))
							{
								htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
							htMaster.put(IDBConstants.CREATED_BY, user_id);
								_MasterDAO.InsertRemarks(htMaster);
								
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
								htRecvHis.put("ORDNUM","");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS",strUtils.InsertQuotes(productDORemarks));
								htRecvHis.put(IDBConstants.CREATED_BY, dateUtils.getDateTime());
								htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								masterHisFlag = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}
					}
			    }
				////insert estimate multi remarks end

				if (flag) {
								
					Hashtable htMovhis = new Hashtable();
					htMovhis.clear();
					htMovhis.put(IDBConstants.PLANT, plant);
					htMovhis.put("DIRTYPE", TransactionConstants.EST_ADD_ITEM);
					htMovhis.put(IDBConstants.CUSTOMER_CODE, custCode);
					htMovhis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
					htMovhis.put(IDBConstants.ITEM, item);
					htMovhis.put(IDBConstants.QTY, qty);
					htMovhis.put(IDBConstants.MOVHIS_ORDNUM, estno);
					htMovhis.put(IDBConstants.CREATED_BY, user_id);
					htMovhis.put("MOVTID", "");
					htMovhis.put("RECID", "");
					htMovhis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
					htMovhis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					
					flag = movHisDao.insertIntoMovHis(htMovhis);

				}
				if (flag)
					result = "<font color=\"green\">Product  Added Successfully</font><br><br><center>"
							+ "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='/track/EstimateServlet?ESTNO="
							+ estno + "&Submit=View'\">";
				else {
					if(isAmntExceed) {
						result = amntExceedMsg;
					}else {
						result = "<font color=\"red\"> Error in adding Product   <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";	
					}
					
		 		}
				result = ""+ordertypeString +"<br><h3>" + result;
				response.sendRedirect("jsp/"+orderaddProductPage+"?Submit=add&ESTNO="
						+ estno + "&JOB_NUM=" + StrUtils.replaceCharacters2Send(jobNum) + "&CUST_CODE="
						+ custCode + "&RFLAG=" + RFLAG + "&CUST_NAME="
						+ StrUtils.replaceCharacters2Send(custName));

			}else if (action.equalsIgnoreCase("Updateestdet")) {
			try {
				String estno = "", estlno = "", item = "", itemDesc = "", result = "", fieldDesc="";
			    estno = StrUtils.fString(request.getParameter("ESTNO")).trim();
				estlno = StrUtils.fString(request.getParameter("ESTLNNO")).trim();
				item = StrUtils.fString(request.getParameter("ITEM")).trim();
				String uom = StrUtils.fString(request.getParameter("UOM")).trim();
				itemDesc = StrUtils.fString(request.getParameter("DESC")).trim();
				String qty = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")).trim());
				String price = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UNITPRICE")).trim());
				String UNITPRICERD= StrUtils.fString(request.getParameter("UNITPRICERD")).trim();
				String PRODUCTDELIVERYDATE= StrUtils.fString(request.getParameter("PRODUCTDELIVERYDATE")).trim();
				if(price.equalsIgnoreCase(""))
					price="0";
				if(UNITPRICERD.equalsIgnoreCase("0"))						
					UNITPRICERD=price;
				else if(UNITPRICERD.equalsIgnoreCase(""))
					UNITPRICERD=price;
				price = new ESTUtil().getConvertedUnitCostToLocalCurrency(plant,estno,UNITPRICERD) ;
                String productRemarks = StrUtils.fString(request.getParameter("PRDREMARKS")).trim();
				Hashtable htUpdatedodet = new Hashtable();
				htUpdatedodet.clear();
				htUpdatedodet.put(IDBConstants.PLANT, plant);
				htUpdatedodet.put(IDBConstants.ESTHDR_ESTNUM, estno);
				htUpdatedodet.put("ESTLNNO", estlno);
				htUpdatedodet.put(IDBConstants.UNITMO,uom);
				htUpdatedodet.put(IDBConstants.ITEM, item);
				htUpdatedodet.put(IDBConstants.ITEM_DESC, strUtils.InsertQuotes(itemDesc));
			    htUpdatedodet.put(IDBConstants.DODET_COMMENT1,strUtils.InsertQuotes(productRemarks));
 				//to check item as nonstock item and non stock type in 2&4 then check price has minus otherwise prefix minus with price
                ItemMstDAO _ItemMstDao =new ItemMstDAO(); 
                Hashtable htIsExist = new  Hashtable();
                htIsExist.put(IDBConstants.PLANT, plant);
                htIsExist.put(IDBConstants.ITEM, item);
                htIsExist.put("NONSTKFLAG", "Y");
                boolean isExistNonStock =_ItemMstDao.isExisit(htIsExist, "NONSTKTYPEID IN(2,4)");
                if(isExistNonStock){
                    String s =  price;
                    if (s.indexOf("-") == -1) {
                    	price="-"+price;
                    }
                }
              //end to check item as nonstock item and non stock type as 2 then check cost has minus sympol otherwise prefix minus with cost 
				htUpdatedodet.put("UNITPRICE", price);
				htUpdatedodet.put("ORDQTY", qty);
				htUpdatedodet.put("PRODUCTDELIVERYDATE", PRODUCTDELIVERYDATE);
				
				/**/
				String estDetQuery = " UNITPRICE,QTYOR ";
				Hashtable htEstDet = new Hashtable();
				htEstDet.put("ESTNO", estno);
				htEstDet.put("ESTLNNO", estlno);
				htEstDet.put("PLANT", plant);
				ArrayList arrayEstDet = _EstDetDAO.selectESTDet(estDetQuery, htEstDet,"");
				Map estHdrMap = (Map) arrayEstDet.get(0);
				float unitprice = Float.parseFloat(estHdrMap.get("UNITPRICE").toString());
				float qtyor = Float.parseFloat(estHdrMap.get("QTYOR").toString());
				boolean isAmntExceed = false;
				
			    if((Float.parseFloat(price) > unitprice) || (Float.parseFloat(qty) > qtyor)){
			    	
				int numberOfDecimal = Integer.parseInt(_PlantMstDAO.getNumberOfDecimal(plant));				
				String estHdrQuery = " CustCode,currencyid,isnull(outbound_Gst,0) as outbound_Gst ";
				Hashtable htEstHdr = new Hashtable();
				htEstHdr.put("ESTNO", estno);
				htEstHdr.put("PLANT", plant);
				
				ArrayList arrayEstHdr = new ArrayList();
				arrayEstHdr = EstUtil.getestHdrDetails(estHdrQuery, htEstHdr);
				Map dataMap = (Map) arrayEstHdr.get(0);
				String gstValue = dataMap.get("outbound_Gst").toString();
				String custCode = dataMap.get("CustCode").toString();
			    Float gstpercentage =  Float.parseFloat(((String) gstValue));
			    
			    double dOrderPrice = Double.parseDouble(price) * Double.parseDouble(qty);
			    dOrderPrice = StrUtils.RoundDB(dOrderPrice, numberOfDecimal);
			    double tax = (dOrderPrice*gstpercentage)/100;
			    tax = StrUtils.RoundDB(tax, numberOfDecimal);
			    double orderAmount = dOrderPrice+tax;
			    orderAmount = StrUtils.RoundDB(orderAmount,numberOfDecimal);
			    
				Map resultMap = getOutstandingAmountForCustomer(custCode, orderAmount, plant);
				isAmntExceed = (boolean) resultMap.get("STATUS");
				String amntExceedMsg = (String) resultMap.get("MSG");
			    }else {
			    	isAmntExceed = false;
			    }
				/**/				
				boolean flag = false;
				if(!isAmntExceed) {
					flag = EstUtil.updateestDetDetails(htUpdatedodet);
				}
                EstDetDAO _EstHdrDAO = new EstDetDAO();
				 //delete & insert estimate multi remarks 
                if(flag){
                    Hashtable htRemarksDel = new Hashtable();
                    htRemarksDel.put(IDBConstants.PLANT,plant);
                    htRemarksDel.put(IDBConstants.ESTHDR_ESTNUM, estno);
                    htRemarksDel.put("ESTLNNO", estlno);
                    htRemarksDel.put(IDBConstants.DODET_ITEM, item);
                    flag = _EstDetDAO.deleteEstimateMultiRemarks(htRemarksDel);
                }
               //delete estimate multi remarks end
               
                String strMovHisRemarks="";
                if(flag)
					{
                	DateUtils dateUtils = new DateUtils();
						String DYNAMIC_REMARKS_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_REMARKS_SIZE"));
						int remarks_count = Integer.parseInt(DYNAMIC_REMARKS_SIZE);
						String productDORemarks="";
						for(int index=0;index<remarks_count;index++){
							productDORemarks = StrUtils.fString(request.getParameter("PRDREMARKS"+"_"+index));
							strMovHisRemarks=strMovHisRemarks+","+productDORemarks ;
							Hashtable htRemarks = new Hashtable();
							htRemarks.put(IDBConstants.PLANT, plant);
							htRemarks.put(IDBConstants.ESTHDR_ESTNUM, estno);
							htRemarks.put("ESTLNNO", estlno);
							htRemarks.put(IDBConstants.DODET_ITEM, item);
							htRemarks.put(IDBConstants.REMARKS,strUtils.InsertQuotes(productDORemarks));
							htRemarks.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
							htRemarks.put(IDBConstants.CREATED_BY,userName);
							flag = EstUtil.saveEstimateoMultiRemarks(htRemarks);
						}
				    }
              //delete & insert estimate multi remarks end
				 
				 
				  MovHisDAO movhisDao = new MovHisDAO();
                  Hashtable htmovHis = new Hashtable();
                  movhisDao.setmLogger(mLogger);
                  htmovHis.clear();
                  htmovHis.put(IDBConstants.PLANT, plant);
                  htmovHis.put("DIRTYPE", "SALES_ESTIMATE_ORDER_UPDATE_PRODUCT");
                  htmovHis.put(IDBConstants.ITEM,item);
                  htmovHis.put(IDBConstants.MOVHIS_ORDNUM, estno);
                  htmovHis.put(IDBConstants.MOVHIS_ORDLNO, estlno);
                  htmovHis.put("QTY", qty);
                  htmovHis.put("REMARKS", price+","+strUtils.InsertQuotes(strMovHisRemarks));
                  htmovHis.put(IDBConstants.CREATED_BY,userName);
                  htmovHis.put("MOVTID", "");
                  htmovHis.put("RECID", "");
                  htmovHis.put(IDBConstants.TRAN_DATE, new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));
                  htmovHis.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());  
                  flag = movhisDao.insertIntoMovHis(htmovHis);
				
				if (flag) {
					ArrayList al = new ArrayList();
					Hashtable htCond = new Hashtable();
					Map m = new HashMap();
					if (estno.length() > 0) {
						htCond.put("PLANT", plant);
						htCond.put("ESTNO", estno);
					  String query = "estno,isnull(outbound_Gst,0) outbound_Gst," +
								"(select isnull(display,'') display from "+"["+plant+"_CURRENCYMST] where currencyid = a.currencyid) currencyid," +
										"custCode,jobNum, ordertype,personInCharge,contactNum,address,address2,address3," +
										"isnull(collectionDate,'') collectionDate,isnull(collectionTime,'') collectionTime," +
										"isnull(remark1,'') remark1,remark2,b.CNAME as custName,isnull(b.name,'') as contactname," +
										"isnull(b.telno,'') as telno,isnull(b.hpno,'') as hpno,isnull(b.hpno,'') as hpno,isnull(b.email,'') email," +
										"isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks," +
										"isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(deliverydate,'') deliverydate," +
										"isnull(timeslots,'') timeslots," +
										"isnull(shippingid,'')as shippingid, isnull(shippingcustomer,'') as shippingcustomer," +
										"isnull(STATUS_ID,'') as statusid,isnull(empno,'') as empno," +
										" isnull(expiredate,'')as expiredate,isnull(status,'')as status,isnull(remark3,'') remark3," +
										"ISNULL(CUSTOMER_STATUS_ID,'') customerstatusid,ISNULL(CUSTOMER_TYPE_ID,'')customertypeid," +
						                "isnull(orderdiscount,0) as orderdiscount,isnull(shippingcost,0) as shippingcost," +
						                "isnull(incoterms,'') as incoterms,isnull(a.DELIVERYDATEFORMAT,'') DELIVERYDATEFORMAT,isnull(a.PAYMENTTYPE,'') payment_terms,isnull(A.TAXTREATMENT,'') as TAXTREATMENT ";
						al = EstUtil.getOutGoingestHdrDetails(query, htCond);
						if (al.size() > 0) {
							m = (Map) al.get(0);
							fieldDesc = EstUtil.listESTDET(plant, estno,rflag);
							System.out.print(fieldDesc);
						} else {
							fieldDesc = "<tr><td colspan=\"7\" align=\"center\">No Records Available</td></tr>";

						}
					}
					request.getSession().setAttribute("podetVal", m);
					request.getSession().setAttribute("estno", estno);
					request.getSession().setAttribute("RESULT", fieldDesc);
					
					if (rflag.equals("1")) {
						response.sendRedirect("jsp/CreateEstimate.jsp?ESTNO="+ estno + "&action=View");
					}
					else
					{
					
						response.sendRedirect("jsp/maintEstimate.jsp?ESTNO="+ estno + "&action=View");
					}

				} else {
					throw new Exception(
							"Price  Not Updated");
				}
				
			    
			} catch (Exception ex) {
			this.mLogger.exception(this.printLog, "", ex);

			String result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Error : " + ex.getMessage() + "</font>";
			result = result
					+ " <br><br><center> "
					+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
			result = "<br><h3>" + result;
			request.getSession().setAttribute("RESULT", result);
			response.sendRedirect("jsp/displayResult2User.jsp");
		  }
			
		} else if (action.equalsIgnoreCase("UPDATE")) {  
			String result = updateEstHdr(request, response);
			request.getSession().setAttribute("RESULT", result);
			response.sendRedirect("jsp/displayResult2User.jsp");
		}else if (action.equalsIgnoreCase("Delete")) {
			try {
				boolean flag = false;
				String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";
				String User_id = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
				String ordno = StrUtils.fString(request.getParameter("ESTNO")).trim();
				String ordlno = StrUtils.fString(request.getParameter("ESTLNNO")).trim();
				String item = StrUtils.fString(request.getParameter("ITEM")).trim();
				Hashtable htdet = new Hashtable();
				htdet.put(IDBConstants.PLANT, plant);
				htdet.put(IDBConstants.ESTHDR_ESTNUM, ordno);
				htdet.put(IDBConstants.ESTHDR_ESTLNNUM, ordlno);
				htdet.put(IDBConstants.DODET_ITEM, item);
				htdet.put(IDBConstants.CREATED_BY, userName);
				EstUtil.setmLogger(mLogger);
				flag = EstUtil.deleteESTDetLineDetails(htdet);
				if (flag) {
				
					ArrayList al = new ArrayList();
					Hashtable htCond = new Hashtable();
					Map m = new HashMap();
					if (ordno.length() > 0) {
						htCond.put("PLANT", plant);
						htCond.put("ESTNO", ordno);
						String query = "estno,isnull(outbound_Gst,0) outbound_Gst," +
								"(select isnull(display,'') display from "+"["+plant+"_CURRENCYMST] where currencyid = a.currencyid) currencyid," +
										"custCode,jobNum, ordertype,personInCharge,contactNum,address,address2,address3," +
										"isnull(collectionDate,'') collectionDate,isnull(collectionTime,'') collectionTime," +
										"isnull(remark1,'') remark1,remark2,b.CNAME as custName,isnull(b.name,'') as contactname," +
										"isnull(b.telno,'') as telno,isnull(b.hpno,'') as hpno,isnull(b.hpno,'') as hpno,isnull(b.email,'') email," +
										"isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks," +
										"isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(deliverydate,'') deliverydate," +
										"isnull(timeslots,'') timeslots," +
										"isnull(shippingid,'')as shippingid, isnull(shippingcustomer,'') as shippingcustomer," +
										"isnull(STATUS_ID,'') as statusid,isnull(empno,'') as empno," +
										" isnull(expiredate,'')as expiredate,isnull(status,'')as status,isnull(remark3,'') remark3," +
										"ISNULL(CUSTOMER_STATUS_ID,'') customerstatusid,ISNULL(CUSTOMER_TYPE_ID,'')customertypeid," +
						                "isnull(orderdiscount,0) as orderdiscount,isnull(shippingcost,0) as shippingcost," +
						                "isnull(incoterms,'') as incoterms,isnull(a.DELIVERYDATEFORMAT,'') DELIVERYDATEFORMAT,isnull(b.pay_terms,'') payment_terms,isnull(A.TAXTREATMENT,'') as TAXTREATMENT  ";
						al = EstUtil.getOutGoingestHdrDetails(query, htCond);
						if (al.size() > 0) {
							m = (Map) al.get(0);
							fieldDesc = EstUtil.listESTDET(plant, ordno,rflag);
						} else {
							fieldDesc = "<tr><td colspan=\"7\" align=\"center\">No Records Available</td></tr>";

						}
					}
					request.getSession().setAttribute("podetVal", m);
					request.getSession().setAttribute("estno", ordno);
					request.getSession().setAttribute("RESULT", fieldDesc);
					if (rflag.equals("1")) {
						response.sendRedirect("jsp/CreateEstimate.jsp?ESTNO="+ ordno + "&action=View");
					}
                    else {
						response.sendRedirect("jsp/maintEstimate.jsp?ESTNO="+ ordno + "&action=View");
					}

				} else {
					throw new Exception(
							"Product ID Not Deleted Successfully");
				}
				} catch (Exception ex) {
				this.mLogger.exception(this.printLog, "", ex);

				String result = "<font class = " + IConstants.FAILED_COLOR
						+ ">Error : " + ex.getMessage() + "</font>";
				result = result
						+ " <br><br><center> "
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = "<br><h3>" + result;
				request.getSession().setAttribute("RESULT", result);
				response.sendRedirect("jsp/displayResult2User.jsp");
			}

		}
			else if (action.equalsIgnoreCase("REMOVE_EST")) {

				String User_id = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
				String estno = StrUtils.fString(request.getParameter("ESTNO")).trim();
			    String rFlag = StrUtils.fString(request.getParameter("RFLAG")).trim();
			        EstDetDAO estdetDao = new EstDetDAO();
			         
				Hashtable htEstHrd = new Hashtable();
				htEstHrd.put(IDBConstants.PLANT, plant);
				htEstHrd.put(IDBConstants.ESTHDR_ESTNUM, estno);
				boolean isValidOrder =new EstHdrDAO().isExisit(htEstHrd,"");
				boolean isOrderInProgress = estdetDao.isExisit(htEstHrd, "STATUS in ('O' ,'C') ");
				if(isValidOrder){
				if (!isOrderInProgress) {
						Boolean value = this.EstUtil.removeRow(plant, estno,User_id);
							if (value) {
								response
										.sendRedirect("/track/EstimateServlet?statusValue=100&ESTNO="
												+ estno + "&Submit=View&rflag="+rFlag+"");
							} else {
								response
										.sendRedirect("/track/EstimateServlet?statusValue=97&ESTNO="
												+ estno + "&Submit=View&rflag="+rFlag+"");
				
							}
						
					} else {
						response
								.sendRedirect("/track/EstimateServlet?statusValue=98&ESTNO="
										+ estno + "&Submit=View&rflag="+rFlag+"");
					}
				
				}else{
					response
					.sendRedirect("/track/EstimateServlet?statusValue=99&ESTNO="
							+ estno + "&Submit=View&rflag="+rFlag+"");
				}
			
			}
			else if (action.equalsIgnoreCase("Convert OB")) {
				
				String estno = "", estlno = "",dono="",dolno="", item = "", itemDesc = "", result = "", fieldDesc="",
					       q = "",prdRemarks="",itemuom="",price="",qtyor="",issuedqty="",
					       custCode = "",custName="",jobNum="",personIncharge="",contactNum="",ordertype="",orderstatus="",
					       address = "",address2="",address3="",collectionDate="",collectionTime="",deliverydate="",
						   remark1="",remark2="",remarks="",currencyid="",outbound_Gst="",empno="",empname="",issuingQty = "",
						   shippingcustomer="",shippingid="",orderdiscount="",shippingcost="",incoterms="",
						   remark3="",display="",timeslots="",deldate="",custcode="",
						   add1 = "", add2 = "", add3 = "", add4 = "", country = "", zip = "",telno="",email="",paymenttype="";

				
				
				estno = StrUtils.fString(request.getParameter("ESTNO"));
				Boolean flag1 = EstUtil.isOpenEstimateOrder(plant, estno);
				if (flag1) {
					throw new Exception(
							" Already Converted To Sales Order ");
				}
				else
				{
					
					 com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			        	_TblControlDAO.setmLogger(mLogger); 
			        	dono = _TblControlDAO.getNextOrder(plant,userName,IConstants.OUTBOUND);
			          
			        	custcode = StrUtils.fString(request.getParameter("CUST_CODE1"));
			        	jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
						personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
						contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
						address = StrUtils.fString(request.getParameter("ADD1")).trim();
						address2 = StrUtils.fString(request.getParameter("ADD2")).trim();
						address3 = StrUtils.fString(request.getParameter("ADD3")).trim();
						add4 = StrUtils.fString(request.getParameter("ADD4"));
						zip = StrUtils.fString(request.getParameter("ZIP"));
						collectionDate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
						collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
						remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
						remark2 = StrUtils.fString(request.getParameter("REMARK2")).trim();
						remark3 = StrUtils.fString(request.getParameter("REMARK3")).trim();
						display = StrUtils.fString(request.getParameter("DISPLAY")).trim();
						ordertype = StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
						deliverydate=StrUtils.fString(request.getParameter("DELIVERYDATE")).trim();
						timeslots=StrUtils.fString(request.getParameter("TIMESLOTS")).trim();
						outbound_Gst = StrUtils.fString(request.getParameter("GST")).trim();
						currencyid = StrUtils.fString(request.getParameter("DISPLAY"));
						orderstatus = StrUtils.fString(request.getParameter("STATUS_ID")).trim();
						empno = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
						shippingcustomer=StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
						shippingid=StrUtils.fString(request.getParameter("SHIPPINGID")).trim();
						empno = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
			            deldate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
			            orderdiscount =  StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
			            shippingcost =  StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
			            incoterms =  StrUtils.fString(request.getParameter("INCOTERMS")).trim();
			            telno = StrUtils.fString(request.getParameter("TELNO"));
						email = StrUtils.fString(request.getParameter("EMAIL"));
						add1 = StrUtils.fString(request.getParameter("ADD1"));
						add2 = StrUtils.fString(request.getParameter("ADD2"));
						add3 = StrUtils.fString(request.getParameter("ADD3"));
						add4 = StrUtils.fString(request.getParameter("ADD4"));
						paymenttype= StrUtils.fString(request.getParameter("PAYMENTTYPE"));
				
				
				request.getSession().setAttribute("RFLAG","5");
				boolean flag = DisplayData(request, response);
		
				response.sendRedirect("/track/jsp/ConvertEstimateToOutbound.jsp?ESTNO="+ estno +"&action=View"
						 +"&DONO="+dono 
						 +"&REFNO="+jobNum
						 + "&ORDDATE="+deldate
						 +"&TIME="+collectionTime
						 +"&REMARK1="+StrUtils.replaceCharacters2Send(remark1)
						 +"&REMARK2="+StrUtils.replaceCharacters2Send(remark2)
						 +"&REMARK3="+StrUtils.replaceCharacters2Send(remark3)
						 + "&TAX="+outbound_Gst
						 + "&CURRENCY="+currencyid
						 +"&CUSTNAME="+custName
						 +"&PERSONINCHARGE="+personIncharge
						 +"&TELNO="+telno
						 +"&EMAIL="+email
						 +"&ADD1="+address
						 +"&ADD2="+address2
						 +"&ADD3="+address3
						 +"&ADD4="+add4
						 +"&COUNTRY="+country
						 +"&ZIP="+zip
						 +"&CUSTCODE="+custcode
						 +"&EMP_NAME="+empno
						 +"&DELIVERYDATE="+deliverydate
						 +"&ORDERTYPE="+ordertype
						 +"&SHIPPINGID="+shippingid
						 +"&SHIPPINGCUSTOMER="+shippingcustomer
						 +"&ORDERDISCOUNT="+orderdiscount
						 +"&SHIPPINGCOST="+shippingcost
						 +"&PAYMENTTYPE="+StrUtils.replaceCharacters2Send(paymenttype)
						 +"&INCOTERMS="+StrUtils.replaceCharacters2Send(incoterms) +"");
						 
				
				}
			}
			
			else if (action.equalsIgnoreCase("Convert To Outbound Order")) {
					try {
						String estno = "", estlno = "",dono="",dolno="", item = "", itemDesc = "", result = "", fieldDesc="",
						       q = "",prdRemarks="",itemuom="",price="",qtyor="",issuedqty="",
						       custCode = "",custName="",jobNum="",personIncharge="",contactNum="",ordertype="",orderstatus="",
						       address = "",address2="",address3="",collectionDate="",collectionTime="",deliverydate="",
							   remark1="",remark2="",remarks="",currencyid="",outbound_Gst="",empno="",empname="",issuingQty = "",
							   shippingcustomer="",shippingid="",orderdiscount="",shippingcost="",incoterms="",
							   remark3="",display="",timeslots="",deldate="",taxby="",prodgst="",paymenttype="",deliverydateformat="",PRODUCTDELIVERYDATE="",sTAXTREATMENT="",sSALES_LOC="";

						DateUtils dateUtils = new DateUtils();
						MovHisDAO movHisDao = new MovHisDAO();
						Boolean allChecked = false,fullIssue = false;
						Map checkedEST = new HashMap();
						boolean flag = false;
						boolean dotransferhdr = false;
						boolean dotransferdetflag = false;
						HttpSession session = request.getSession();
						estno = StrUtils.fString(request.getParameter("ESTNO")).trim();
						
						if (estno.length()>0) {
		              	dono = StrUtils.fString(request.getParameter("DONO"));
						String[] chkdEstNo  = request.getParameterValues("chkdEstNo");
						custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
						custName = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("CUST_NAME"))).trim();
						jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
						personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
						contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
						address = StrUtils.fString(request.getParameter("ADD1")).trim();
						address2 = StrUtils.fString(request.getParameter("ADD2")).trim();
						address3 = StrUtils.fString(request.getParameter("ADD3")).trim();
						collectionDate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
						collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
						remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
						remark2 = StrUtils.fString(request.getParameter("REMARK2")).trim();
						remark3 = StrUtils.fString(request.getParameter("REMARK3")).trim();
						display = StrUtils.fString(request.getParameter("DISPLAY")).trim();
						ordertype = StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
						deliverydate=StrUtils.fString(request.getParameter("DELIVERYDATE")).trim();
						timeslots=StrUtils.fString(request.getParameter("TIMESLOTS")).trim();
						outbound_Gst = StrUtils.fString(request.getParameter("GST")).trim();
						currencyid = StrUtils.fString(request.getParameter("DISPLAY"));
						orderstatus = StrUtils.fString(request.getParameter("STATUS_ID")).trim();
						empno = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
						shippingcustomer=StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
						shippingid=StrUtils.fString(request.getParameter("SHIPPINGID")).trim();
						empno = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
			            deldate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
			            orderdiscount =  StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
			            shippingcost =  StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
			            incoterms =  StrUtils.fString(request.getParameter("INCOTERMS")).trim();
			        	paymenttype =  StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
			        	deliverydateformat = StrUtils.fString((request.getParameter("DELIVERYDATEFORMAT") != null) ? "1": "0").trim();
			        	sTAXTREATMENT=StrUtils.fString(request.getParameter("TAXTREATMENT")).trim();
			        	sSALES_LOC=StrUtils.fString(request.getParameter("SALES_LOC")).trim();
			                Hashtable ht = new Hashtable();
			                ht.clear();
							
							ht.put(IDBConstants.PLANT, plant);
							ht.put(IDBConstants.DOHDR_DONUM, dono);
							ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
							ht.put(IDBConstants.DOHDR_CUST_NAME, strUtils.InsertQuotes(custName));
							ht.put(IDBConstants.DOHDR_JOB_NUM, jobNum);
							ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, strUtils.InsertQuotes(personIncharge));
							ht.put(IDBConstants.DOHDR_CONTACT_NUM, contactNum);
							ht.put(IDBConstants.DOHDR_ADDRESS, address);
							ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
							ht.put(IDBConstants.DOHDR_ADDRESS3, address3);
							ht.put(IDBConstants.CREATED_BY, userName);
							ht.put(IDBConstants.DOHDR_COL_DATE, collectionDate);
							ht.put(IDBConstants.DOHDR_COL_TIME, collectionTime);
							ht.put(IDBConstants.DOHDR_REMARK1, strUtils.InsertQuotes(remark1));
							ht.put(IDBConstants.DOHDR_REMARK2, strUtils.InsertQuotes(remark2));
							ht.put(IDBConstants.DOHDR_REMARK3, strUtils.InsertQuotes(remark3));
							ht.put(IDBConstants.STATUS, "N");
							ht.put(IDBConstants.ORDERTYPE, ordertype);
							ht.put(IDBConstants.CURRENCYID, currencyid);
							ht.put(IDBConstants.TIMESLOTS, timeslots);
						    ht.put(IDBConstants.DOHDR_DEL_DATE, deldate);
							ht.put(IDBConstants.DELIVERYDATE, deliverydate);
							ht.put(IDBConstants.DOHDR_GST,outbound_Gst);
							ht.put(IDBConstants.ORDSTATUSID, orderstatus);
							ht.put(IDBConstants.DOHDR_EMPNO, empno);
		             		ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
							ht.put(IDBConstants.SHIPPINGID, shippingid);
							ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
							ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
							ht.put(IDBConstants.INCOTERMS, incoterms);
							ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
							ht.put(IDBConstants.POHDR_DELIVERYDATEFORMAT, deliverydateformat);
							ht.put(IDBConstants.TAXTREATMENT, sTAXTREATMENT);
							ht.put(IDBConstants.SALES_LOCATION, sSALES_LOC);
							ht.put("ESTNO", estno);
		                Boolean isCustomerAvailable = Boolean.valueOf(true);
						CustUtil custUtils = new CustUtil();
						isCustomerAvailable = custUtils.isExistCustomerName(strUtils.InsertQuotes(custName), plant);
						if (isCustomerAvailable) {

							dOUtil.setmLogger(mLogger);
							flag = dOUtil.saveDoHdrDetails(ht);
							ht.remove(IDBConstants.ORDERTYPE);
		                    ht.remove(IDBConstants.ORDSTATUSID);
		                    				
							if (flag) {
								dotransferhdr = _DOTransferUtil.saveDoTransferHdrDetails(ht);
								
								Hashtable htmovHis = new Hashtable();
								htmovHis.clear();
								htmovHis.put(IDBConstants.PLANT, plant);
								htmovHis.put("DIRTYPE", TransactionConstants.CONVERT_OUTBOUND);
								htmovHis.put(IDBConstants.CUSTOMER_CODE, custCode);
								htmovHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
								htmovHis.put(IDBConstants.MOVHIS_ORDNUM, estno);
								htmovHis.put(IDBConstants.CREATED_BY, userName);
								htmovHis.put("MOVTID", "");
								htmovHis.put("RECID", "");
							    if (!remark1.equals("")) {
							    	htmovHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(custName) +","+dono+ ","+ strUtils.InsertQuotes(remark1));
								} else {
									htmovHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(custName)+","+dono);
								}

							    htmovHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
							    htmovHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());

								flag = movHisDao.insertIntoMovHis(htmovHis);
					
								
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_OB);
								htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
								htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
								htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, dono);
								htRecvHis.put(IDBConstants.CREATED_BY, userName);
								htRecvHis.put("MOVTID", "");
								htRecvHis.put("RECID", "");
							    if (!remark1.equals("")) {
									htRecvHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(custName) + ","+ strUtils.InsertQuotes(remark1));
								} else {
									htRecvHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(custName));
								}

								htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());

								flag = movHisDao.insertIntoMovHis(htRecvHis);
						
						        new TblControlUtil().updateTblControlSeqNo(plant,IConstants.OUTBOUND,"S",dono);
								

							}
							
							if( request.getParameter("select")!=null){
								allChecked = true;
							}
							if(request.getParameter("fullIssue")!=null){
								fullIssue = true;
							}
							if (chkdEstNo != null)    {     
								for (int i = 0; i < chkdEstNo.length; i++)       { 
									estlno = chkdEstNo[i];
									issuingQty = StrUtils.fString(request.getParameter("issuingQty_"+dolno));
									checkedEST.put(estlno, issuingQty);
								}
								session.setAttribute("checkedEST", checkedEST);
				            }
						
								process: 	
								if (chkdEstNo != null)    {     
									for (int i = 0; i < chkdEstNo.length; i++)       { 
										dolno = chkdEstNo[i];
										issuingQty = StrUtils.formatNum(StrUtils.fString(request.getParameter("issuingQty_"+dolno)));	
								
										Hashtable htCond = new Hashtable();
										htCond.put("PLANT", plant);
										htCond.put("ESTNO", estno);
										htCond.put("ESTLNNO", dolno);
										
								q = "item,isnull(itemdesc,'') itemdesc,isnull(status,'') status,isnull(PRODUCTDELIVERYDATE,'') PRODUCTDELIVERYDATE,isnull(unitmo,'') uom,isnull(unitprice,0) unitprice,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(comment1,'') as prdRemarks ";
								ArrayList aldet = _EstDetDAO.selectESTDet(q, htCond," plant <> '' and status <>'C' order by estlnno");
												
							if (aldet.size() > 0) {
								Map m = (Map) aldet.get(0);
								item = (String) m.get("item");
								itemDesc = (String) m.get("itemdesc");
							    prdRemarks = (String) m.get("prdRemarks");
								itemuom = (String) m.get("uom");
								//String ConvertedUnitPrice= new EstHdrDAO().getUnitCostBasedOnCurIDSelected(plant, estno,estlno,item);
								price = (String) m.get("unitprice");
								qtyor = StrUtils.formatNum((String) m.get("qtyor"));
								issuedqty = StrUtils.formatNum((String) m.get("qtyis"));
								PRODUCTDELIVERYDATE = (String) m.get("PRODUCTDELIVERYDATE");
							}
						Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant,item);
                        String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
	                    String nonstocktypeDesc= StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
		
	                   		
						Hashtable htdodet = new Hashtable();
						
						htdodet.put(IDBConstants.PLANT, plant);
						htdodet.put(IDBConstants.DODET_DONUM, dono);
					
						htdodet.put(IDBConstants.DODET_ITEM, item);
						htdodet.put("ITEMDESC", strUtils.InsertQuotes(itemDesc));
						htdodet.put("UNITMO", itemuom);
						htdodet.put(IDBConstants.DODET_ITEM_DESC,strUtils.InsertQuotes(itemDesc));
						
						
						htdodet.put(IDBConstants.DODET_JOB_NUM, jobNum);
						htdodet.put(IDBConstants.DODET_CUST_NAME, strUtils.InsertQuotes(custName));
						htdodet.put(IDBConstants.DODET_QTYOR, issuingQty);
						htdodet.put(IDBConstants.DODET_UNITPRICE, price);
						htdodet.put(IDBConstants.DODET_QTYIS, "0");
						htdodet.put(IDBConstants.DODET_COMMENT1,strUtils.InsertQuotes(prdRemarks));
						htdodet.put(IDBConstants.DODET_PRODUCTDELIVERYDATE, PRODUCTDELIVERYDATE);
						htdodet.put(IDBConstants.DODET_DOLNNO, Integer.toString(i+1));
						htdodet.put("ESTNO", estno);
						htdodet.put("ESTLNNO", dolno);
					
						String CURRENCYUSEQT = new DOUtil().getCurrencyUseQT(plant, dono);
						htdodet.put(IDBConstants.CURRENCYUSEQT, CURRENCYUSEQT);
						/*if(nonstocktype.equals("Y"))	
					    {
					    	if(nonstocktypeDesc.equalsIgnoreCase("discount")||nonstocktypeDesc.equalsIgnoreCase("Credit Note")){ 
	                             htdodet.put(IDBConstants.DODET_UNITPRICE, "-"+price);
	                            }
					    	
					    	
					    }*/
					   
						htdodet.put(IDBConstants.DODET_LNSTATUS, "N");
						htdodet.put(IDBConstants.DODET_PICKSTATUS, "N");
						/*to get product gst*/
						taxby=_PlantMstDAO.getTaxBy(plant);
						if(taxby.equalsIgnoreCase("BYPRODUCT"))
						{
							prodgst=_ItemMstDAO.getProductGst(plant, item);
							htdodet.put(IDBConstants.PRODGST, prodgst);
						}
						/*to get product gst end*/
					
					java.util.Date dt = new java.util.Date();
					SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
					String today = dfVisualDate.format(dt);

					htdodet.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
					dOUtil.setmLogger(mLogger);
					if(flag){
						flag = dOUtil.saveDoDetDetails(htdodet);
					}
					if (flag) {
						
						//insert dodet multi remarks
							//String DYNAMIC_REMARKS_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_REMARKS_SIZE"));
							//int remarks_count = Integer.parseInt(DYNAMIC_REMARKS_SIZE);
							String productDORemarks=""; 
							//for(int index=0;index<remarks_count;index++){
								//productDORemarks = StrUtils.fString(request.getParameter("PRDREMARKS"+"_"+index));
							    productDORemarks=strUtils.InsertQuotes(prdRemarks);
								Hashtable htRemarks = new Hashtable();
								htRemarks.put(IDBConstants.PLANT, plant);
								htRemarks.put(IDBConstants.DODET_DONUM, dono);
								htRemarks.put(IDBConstants.DODET_DOLNNO, dolno);
								htRemarks.put(IDBConstants.DODET_ITEM, item);
								htRemarks.put(IDBConstants.REMARKS,strUtils.InsertQuotes(productDORemarks));
								htRemarks.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
								htRemarks.put(IDBConstants.CREATED_BY,userName);
								flag = dOUtil.saveDoMultiRemarks(htRemarks);
							//}
					}
						////insert dodet multi remarks end
					if (flag) {
						dotransferdetflag = _DOTransferUtil.saveDoTransferDetDetails(htdodet);
						Hashtable htMovhis = new Hashtable();
						htMovhis.clear();
						htMovhis.put(IDBConstants.PLANT, plant);
						htMovhis.put("DIRTYPE", TransactionConstants.OB_ADD_ITEM);
						htMovhis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htMovhis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htMovhis.put(IDBConstants.ITEM, item);
						htMovhis.put(IDBConstants.QTY, qtyor);
						htMovhis.put(IDBConstants.MOVHIS_ORDNUM, dono);
						htMovhis.put(IDBConstants.CREATED_BY, userName);
						htMovhis.put("MOVTID", "");
						htMovhis.put("RECID", "");
						htMovhis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
						htMovhis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						if(dotransferdetflag)
						flag = movHisDao.insertIntoMovHis(htMovhis);

						}
					
						if(flag)
								{
									EstDetDAO _EstDetDAO = new EstDetDAO();
									EstHdrDAO _EstHdrDAO = new EstHdrDAO();
									String updateEstHdr = "",updateEstDet="";
									Hashtable htCondition = new Hashtable();
									htCondition.put("PLANT", plant);
									htCondition.put("ESTNO", estno);
									htCondition.put("ESTLNNO", dolno);
									
									double Ordqty = Double.parseDouble(qtyor);
									double tranQty = Double.parseDouble(issuingQty);
									double issqty = Double.parseDouble(issuedqty);
									double sumqty = issqty + tranQty;
									sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
									
									String extraCond = " AND  QtyOr >= isNull(qtyis,0) + "+ issuingQty;
									if (Ordqty == sumqty) {
										updateEstDet = "set qtyis= isNull(qtyis,0) + " + issuingQty  + ", STATUS='C' ";

									} else {
										updateEstDet = "set qtyis= isNull(qtyis,0) + " + issuingQty  + ", STATUS='O' ";
									
									}
   							
									flag = _EstDetDAO.update(updateEstDet, htCondition, extraCond);
										
									if (flag) {
										htCondition.remove("ESTLNNO");
										
										flag = _EstDetDAO.isExisit(htCondition,"STATUS in ('O','N')");
										if (!flag){
											updateEstHdr = "set  STATUS='Confirm' ";
											flag = _EstHdrDAO.update(updateEstHdr, htCondition, "");
										}
									
									}
								}
						if(!flag)
							break process;
						}
					}
				 
				            
					if (flag) {
//						Call Accounting module for new order : Start
						/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
							try {
								Hashtable htAcct = new Hashtable();
								htAcct.put(IDBConstants.PLANT, plant);
								if (_MasterDAO.isExisitAccount(htAcct, " ACCOUNT_TITLE='Customers'")) {
									//	Get Acct. Id
									String Acctid=getNextIdDetails(plant,userName);
									if(Acctid!="")
									{
										if (!_MasterDAO.isExisitAccount(htAcct, " CUSTOMER_NO='"+custCode+"' AND ACCOUNT_TYPE=1 AND PARENT_ACCOUNT_NO=12  ")) {
										flag = _MasterDAO.InsertAccount(htAcct,Acctid,custName,"",custCode,userName,1,12);//insert Supplier Account
										if (flag) {
										if(Acctid!="A0001")// Update Account Id
							      		  {	
							    			boolean exitFlag = false;
							    			boolean  updateFlag= false;
							    			Hashtable htv = new Hashtable();				
							    			htv.put(IDBConstants.PLANT, plant);
							    			htv.put(IDBConstants.TBL_FUNCTION, "ACCOUNT");
							    			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
							    			if (exitFlag) 
							    			  updateFlag=_TblControlDAO.updateSeqNo("ACCOUNT",plant);
							    			else
							    			{
							    				boolean insertFlag = false;
							    				Map htInsert=null;
							                	Hashtable htTblCntInsert  = new Hashtable();           
							                	htTblCntInsert.put(IDBConstants.PLANT,plant);          
							                	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"ACCOUNT");
							                	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"A");
							                 	htTblCntInsert.put("MINSEQ","0000");
							                 	htTblCntInsert.put("MAXSEQ","9999");
							                	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
							                	htTblCntInsert.put(IDBConstants.CREATED_BY, userName);
							                	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
							                	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
							    			}
							    		}
										}
										}
										if (flag) {
								Form form = new Form();
								form.param("data", "{\"dono\":\"" + dono + "\",\"xndate\":\"" + collectionDate + "\",\"customer\":\"" + custCode + "\",\"amount\":\"0\",\"transactionType\":\"3\",\"plant\":\"" + plant + "\",\"user\":\"" + userName + "\",\"currency\":\"" + currencyid + "\",\"action\":\"copytooutbound\"}");							
								String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
								if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
									throw new Exception(finTransactionResponse);
								}
										}
									}
								}
							}catch(Exception e) {
								throw new Exception(ThrowableUtil.getMessage(e), e);
							}
						}*/
						//	Call Accounting module for new order : End
						
						result = "<font color=\"green\"> Converted To Sales Order Successfully!  <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/EstimateServlet?ESTNO="
				
							+ estno + "&Submit=View&rflag=2'\"> ";
						

					} else {
						result = "<font color=\"red\"> Error in Converting To Sales Order  - Please Check the Data  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
						
					}
					}
						else {
							result = "<font color=\"red\"> Error in Sales Order  - Invalid Customer <br><br><center>"
									+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
							result = "<br><h3>" + result;
						}

					
				}else{   
                    
						result = "<font color=\"red\"> Please select Order Number first before Converting to Sales Order. <br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
						
					}
						
						request.getSession().setAttribute("RESULT", result);
						response.sendRedirect("jsp/displayResult2User.jsp");
					
					} catch (Exception ex) {
					this.mLogger.exception(this.printLog, "", ex);

					String result = "<font class = " + IConstants.FAILED_COLOR
							+ ">Error : " + ex.getMessage() + "</font>";
					result = result
							+ " <br><br><center> "
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "<br><h3>" + result;
					request.getSession().setAttribute("RESULT", result);
					response.sendRedirect("jsp/displayResult2User.jsp");
				  }
				
			}else if(action.equalsIgnoreCase("Export To Excel")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // to eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=SalesEstimate.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
		}
			else if (action.equals("ExportExcelEstimateOrderSummary")) {
	            
	            String newHtml="";
				 HSSFWorkbook wb = new HSSFWorkbook();
				 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		        wb = this.writeToExcelEstimatesummary(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=SalesEstimateOrderSummaryWithPrice.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 outStream.close();
			 
	       }
else if (action.equals("ExportExcelEstimateOrderSummaryWithOutPrice")) {
	            
	            String newHtml="";
				 HSSFWorkbook wb = new HSSFWorkbook();
				 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		        wb = this.writeToExcelEstimatesummaryWithOutPrice(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=SalesEstimateOrderSummary.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 outStream.close();
			 
	       }
  else if (action.equals("ExportExcelEstimateSummaryWithRemarks")) {
		            
		            String newHtml="";
					 HSSFWorkbook wb = new HSSFWorkbook();
					 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			        wb = this.writeToExcelEstimateSummaryWithRemarks(request,response,wb);
					 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
					 wb.write(outByteStream);
					 byte [] outArray = outByteStream.toByteArray();
					 response.setContentType("application/ms-excel");
					 response.setContentLength(outArray.length);
					 response.setHeader("Expires:", "0"); // eliminates browser caching
					 response.setHeader("Content-Disposition", "attachment; filename=SalesEstimateOrderSummary.xls");
					 OutputStream outStream = response.getOutputStream();
					 outStream.write(outArray);
					 outStream.flush();
					 outStream.close();
				 
		       } else if(action.equals("GetEstimateQty")) {
		    	    JSONObject resultJson = new JSONObject();
		    	    JSONArray jsonArray = new JSONArray();
		    	    JSONArray jsonArrayErr = new JSONArray();
		    	    String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    	    String ITEM    = strUtils.fString(request.getParameter("ITEM"));
		    	    Hashtable ht = new Hashtable();
		    	    ht.put("a.PLANT", PLANT);
		    	    ht.put("b.ITEM", ITEM);
		    	    ArrayList estQryList= new EstHdrDAO().getEstDetailForSalesOrder(ht);
		    	    JSONObject resultJsonInt = null;
		    	    
		    	    PlantMstDAO _PlantMstDAO = new PlantMstDAO();
		            String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
		            
		    	    if(estQryList.size() > 0) {
		    	    	for (int iCnt =0; iCnt<estQryList.size(); iCnt++){
		    	    		Map lineArrIm = (Map) estQryList.get(iCnt);
		    	    		resultJsonInt = new JSONObject();
		    	    		resultJsonInt.put("ESTNO", strUtils.fString((String) lineArrIm.get("ESTNO")));
		    	    		resultJsonInt.put("COLLECTIONDATE", strUtils.fString((String) lineArrIm.get("CollectionDate")));
		    	    		resultJsonInt.put("EXPIREDATE", strUtils.fString((String) lineArrIm.get("EXPIREDATE")));
		    	    		resultJsonInt.put("CUSTNAME", strUtils.fString((String) lineArrIm.get("CustName")));
		    	    		resultJsonInt.put("ITEM", strUtils.fString((String) lineArrIm.get("ITEM")));
		    	    		resultJsonInt.put("QTYOR", strUtils.fString((String) lineArrIm.get("QTYOR")));
		    	    		jsonArray.add(resultJsonInt);
		    	    	}
		    	    	resultJson.put("items", jsonArray);
						resultJsonInt = new JSONObject();
						resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
						resultJsonInt.put("ERROR_CODE", "100");
						jsonArrayErr.add(resultJsonInt);
						resultJson.put("errors", jsonArrayErr);  
		    	    } else {
		                 resultJsonInt = new JSONObject();
		                 resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
		                 resultJsonInt.put("ERROR_CODE", "99");
		                 jsonArrayErr.add(resultJsonInt);
		                 jsonArray.add("");
		                 resultJson.put("items", jsonArray);
		                 resultJson.put("errors", jsonArrayErr);
		    	    }
		    	    
		    	    response.setContentType("application/json");
		    	    response.setCharacterEncoding("UTF-8");
					response.getWriter().write(resultJson.toString());
					response.getWriter().flush();
					response.getWriter().close();
       }
	       else if (action.equalsIgnoreCase("Copy EST")) {
    	   String[] index = request.getParameterValues("ID");
    	   String[] unitPrice = request.getParameterValues("UNITPRICE");
    	   String[] orderQty = request.getParameterValues("ORDERPRICE");
    	   String[] uom = request.getParameterValues("UOM");
    	   String custno = StrUtils.fString(request.getParameter("CUSTNO"));
    	   String cname = StrUtils.fString(request.getParameter("CNAME"));
    	   String taxtreatment = StrUtils.fString(request.getParameter("TAXTREATMENT"));
    	   String name = StrUtils.fString(request.getParameter("NAME"));
    	   String customer_type_id = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
    	   String fitem = StrUtils.fString(request.getParameter("ITEM"));
    	   String floc = StrUtils.fString(request.getParameter("LOC"));
    	   String floc_type_id = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
    	   String floc_type_id2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
    	   String fmodel = StrUtils.fString(request.getParameter("MODEL"));
    	   String fuom = StrUtils.fString(request.getParameter("FUOM"));
    	   
    	   
    	   String result = "", detlist="";
    	   List listQry = new ArrayList();
    	   listQry = (List) request.getSession().getAttribute("EST_LIST");
    	   for(int i=0; i<index.length; i++) {
    		   Map m=(Map)listQry.get(Integer.parseInt(index[i]));
    		   result += "<tr valign=\"middle\">"
						+ "<td align=\"center\" width=\"10%\">" 
						+ (i+1) + "</td>"
						+ "<td width=\"20%\" align = left>"
						+ sqlBean.formatHTML(StrUtils.fString((String)m.get("PRODUCT")))+ "</td>"
						+ "<td width=\"30%\" align = left>"
						+ sqlBean.formatHTML(StrUtils.fString((String)m.get("PRODUCTDESC"))) + "</td>"
						+ "<td width=\"10%\" align = center>"
						+ sqlBean.formatHTML(new java.math.BigDecimal(unitPrice[i]).toPlainString()) + "</td>"
						+ "<td width=\"10%\" align = center>"
						+ sqlBean.formatHTML(orderQty[i]) + "</td>"
						+ "<td width=\"10%\" align = center>"
						+ 0.000 + "</td>"
						+ "<td width=\"10%\" align = left>" + uom[i]
						+ "</td>" + "</tr>";
    		   
    		   detlist += "<input type='hidden' name='ID' value='"+index[i]+"'>";
    		   detlist += "<input type='hidden' name='UNITPRICE' value='"+unitPrice[i]+"'>";
    		   detlist += "<input type='hidden' name='UOM' value='"+uom[i]+"'>";
    		   detlist += "<input type='hidden' name='ORDERPRICE' value='"+orderQty[i]+"'>";
    	   }   
	    	   
    	   	com.track.dao.TblControlDAO _TblControlDAO = new com.track.dao.TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			String newestno = _TblControlDAO.getNextOrder(plant, userName, IConstants.ESTIMATE);
			
			request.getSession().setAttribute("RESULT", result);
			request.getSession().setAttribute("DETLIST", detlist);
			response.sendRedirect("/track/jsp/convertSalesToEstimate.jsp?ESTNO="+newestno+"&action=view&CUSTCODE="+custno+"&CUSTNAME="+cname+"&TAXTREATMENT="
			+taxtreatment+"&NAME="+name+"&CTYPE="+customer_type_id+"&FITEM="+fitem+"&FLOC="+floc+
			"&FLOC_TYPE_ID="+floc_type_id+"&FLOC_TYPE_ID2="+floc_type_id2
			+"&FMODEL="+fmodel+"&FUOM="+fuom);
		}else if(action.equalsIgnoreCase("SaveEstData")) {
			String result = "";
			result = SaveEstData(request, response);
			request.getSession().setAttribute("RESULT", result);
			response.sendRedirect("jsp/displayResult2User.jsp");
		}
		 
		 else if(action.equals("GetLastTransactionPrice")) {
	    	    JSONObject resultJson = new JSONObject();
	    	    JSONArray jsonArray = new JSONArray();
	    	    JSONArray jsonArrayErr = new JSONArray();
	    	    String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	    	    String ITEM    = strUtils.fString(request.getParameter("ITEM"));
	    	    String CUSTCODE    = strUtils.fString(request.getParameter("CUSTCODE"));
	    	    Hashtable ht = new Hashtable();
	    	    ht.put("a.PLANT", PLANT);
	    	    ht.put("b.ITEM", ITEM);
	    	    ht.put("a.CUSTCODE", CUSTCODE);
	    	    ArrayList estQryList= new EstHdrDAO().getLastTranEstDetail(ht);
	    	    ArrayList doQryList= new DoHdrDAO().getLastTranDoDetail(ht);
	    	    JSONObject resultJsonInt = null;
	    	    
	    	    PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	            String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
	            
	    	    if(estQryList.size() > 0) {
	    	    	for (int iCnt =0; iCnt<estQryList.size(); iCnt++){
	    	    		Map lineArrIm = (Map) estQryList.get(iCnt);
	    	    		resultJsonInt = new JSONObject();
	    	    		resultJsonInt.put("ESTNO", strUtils.fString((String) lineArrIm.get("ESTNO")));
	    	    		resultJsonInt.put("COLLECTIONDATE", strUtils.fString((String) lineArrIm.get("CollectionDate")));
	    	    		resultJsonInt.put("CUSTNAME", strUtils.fString((String) lineArrIm.get("CustName")));
	    	    		resultJsonInt.put("ITEM", strUtils.fString((String) lineArrIm.get("ITEM")));
	    	    		
                        double dConvertedUnitPrice = Double.parseDouble(StrUtils.fString(strUtils.fString((String) lineArrIm.get("UNITPRICE"))));
	    	    		resultJsonInt.put("UNITPRICE", StrUtils.addZeroes(dConvertedUnitPrice, numberOfDecimal));
	    	    		jsonArray.add(resultJsonInt);
	    	    	}
	    	    	resultJson.put("estdetails", jsonArray);
					resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
					resultJsonInt.put("ERROR_CODE", "100");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);  
	    	    } else {
	                 resultJsonInt = new JSONObject();
	                 resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
	                 resultJsonInt.put("ERROR_CODE", "99");
	                 jsonArrayErr.add(resultJsonInt);
	                 jsonArray.add("");
	                 resultJson.put("estdetails", jsonArray);
	                 resultJson.put("errors", jsonArrayErr);
	    	    }
	    	    
	    	    if(doQryList.size() > 0) {
	    	    	jsonArray = new JSONArray();
	    	    	for (int iCnt =0; iCnt<doQryList.size(); iCnt++){
	    	    		Map lineArrIm = (Map) doQryList.get(iCnt);
	    	    		resultJsonInt = new JSONObject();
	    	    		resultJsonInt.put("DONO", strUtils.fString((String) lineArrIm.get("DONO")));
	    	    		resultJsonInt.put("COLLECTIONDATE", strUtils.fString((String) lineArrIm.get("CollectionDate")));
	    	    		resultJsonInt.put("CUSTNAME", strUtils.fString((String) lineArrIm.get("CustName")));
	    	    		resultJsonInt.put("ITEM", strUtils.fString((String) lineArrIm.get("ITEM")));
	    	    		double dConvertedUnitPrice = Double.parseDouble(StrUtils.fString(strUtils.fString((String) lineArrIm.get("UNITPRICE"))));
	    	    		resultJsonInt.put("UNITPRICE", StrUtils.addZeroes(dConvertedUnitPrice, numberOfDecimal));
	    	    		jsonArray.add(resultJsonInt);
	    	    	}
	    	    	resultJson.put("dodetails", jsonArray);
					resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
					resultJsonInt.put("ERROR_CODE", "100");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);  
	    	    } else {
	                 resultJsonInt = new JSONObject();
	                 resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
	                 resultJsonInt.put("ERROR_CODE", "99");
	                 jsonArrayErr.add(resultJsonInt);
	                 jsonArray.add("");
	                 resultJson.put("dodetails", jsonArray);
	                 resultJson.put("errors", jsonArrayErr);
	    	    }
	    	    
	    	    response.setContentType("application/json");
	    	    response.setCharacterEncoding("UTF-8");
				response.getWriter().write(resultJson.toString());
				response.getWriter().flush();
				response.getWriter().close();
		 }
		 else if (action.equalsIgnoreCase("Copy Invoice")) {
	    	   String[] index = request.getParameterValues("ID");
	    	   String[] unitPrice = request.getParameterValues("UNITPRICE");
	    	   String[] orderQty = request.getParameterValues("ORDERPRICE");
	    	   String[] uom = request.getParameterValues("UOM");
	    	   String custno = StrUtils.fString(request.getParameter("CUSTNO"));
	    	   String cname = StrUtils.fString(request.getParameter("CNAME"));
	    	   String taxtreatment = StrUtils.fString(request.getParameter("TAXTREATMENT"));
	    	   String name = StrUtils.fString(request.getParameter("NAME"));
	    	   String customer_type_id = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
	    	   String fitem = StrUtils.fString(request.getParameter("ITEM"));
	    	   String floc = StrUtils.fString(request.getParameter("LOC"));
	    	   String floc_type_id = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
	    	   String floc_type_id2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
	    	   String fmodel = StrUtils.fString(request.getParameter("MODEL"));
	    	   String fuom = StrUtils.fString(request.getParameter("FUOM"));
	    	   String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
	    	   ItemUtil itemUtil = new ItemUtil();
	    	   
	    	   String result = "", detlist="";
	    	   List listQry = new ArrayList();
	    	   listQry = (List) request.getSession().getAttribute("EST_LIST");
	    	   for(int i=0; i<index.length; i++) {
	    		   int j= i+1;
	    		   Map m=(Map)listQry.get(Integer.parseInt(index[i]));
	    		   String itm = StrUtils.fString((String)m.get("PRODUCT"));
	    		   float amount = (Float.parseFloat(orderQty[i]) * Float.parseFloat(unitPrice[i]));
	    		   /**/
	    		   
	    		   List listItems = itemUtil.queryItemMstDetailsforpurchase(itm,plant);
		    	   Vector arrItems   = (Vector)listItems.get(0);
		    	   String outgoing = "",maxstkqty="",stkonhnd="",reorder="";
	               if(arrItems.size()>0){
	            	    outgoing = StrUtils.fString((String)arrItems.get(23));
	            	    maxstkqty = StrUtils.fString((String)arrItems.get(21));
	            	    stkonhnd = StrUtils.fString((String)arrItems.get(22));
	            	    reorder = StrUtils.fString((String)arrItems.get(8));
	               }
             
           	 		String estQty = "", avlbQty = "";
	               	Hashtable hts = new Hashtable();
	       	 		hts.put("item", itm);
	       	 		hts.put("plant", plant);
	       	 		Map ma = new EstDetDAO().getEstQtyByProduct(hts);
	       	 		estQty = (String) ma.get("ESTQTY");
	       	 		ma = new InvMstDAO().getAvailableQtyByProduct(hts);
	       	 		avlbQty= (String) ma.get("AVLBQTY");
	       	 		String EST = estQty;
					String AVL = avlbQty;
					
//					String  convertedcost = new DOUtil().getConvertedUnitCostForProductByCurrency(plant,currencyID,sItem); 
//	           		String  convertedcostwtc = new DOUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
					String	minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,curency,itm);
	           	 	String	OBDiscount=new DOUtil().getOBDiscountSelectedItemByCustomer(plant,custno,itm,"OUTBOUND");
	           	 	
					result += "<tr>"
	    				   + "<td class='item-img text-center'>"
						   + sqlBean.formatHTML(StrUtils.fString((String)m.get("CATALOG"))) 
						   + "<input type='hidden' name='basecost' value='"+unitPrice[i]+"'>"
//						   + "<input type='hidden' name='lnno' value='"+index[j]+"'>"
						   + "<input type='hidden' name='lnno' value='"+j+"'>"
						   + "<input type='hidden' name='tax_type'>"
						   + "<input type='hidden' name='tax' class='form-control taxSearch'>"
						   
//							+ "<input type='hidden' name='unitpricerd'>"
//							+ "<input type='hidden' name='minstkqty' value = '"+outgoing+"'>"
							+ "<input type='hidden' name='reorder' value = '"+reorder+"'>"
							+ "<input type='hidden' name='maxstkqty' value = '"+maxstkqty+"'>"
							+ "<input type='hidden' name='stockonhand' value = '"+stkonhnd+"'>"
							+ "<input type='hidden' name='outgoingqty' value = '"+outgoing+"'>"
							+ "<input type='hidden' name='estimateqty' value = '"+EST+"'>"
							+ "<input type='hidden' name='avalqty' value = '"+AVL+"'>"
							+ "<input type='hidden' name='customerdiscount' value = '"+OBDiscount+"'>"
							+ "<input type='hidden' name='minsp' value = '"+minSellingconvertedcost+"'>"
//							+ "<input type='hidden' name='incommingqty' >"
//							+ "<input type='hidden' name='unitpricediscount'>"
//							+ "<input type='hidden' name='discounttype' >"
						   
						   + "</td>"
						   + "<td class=''>"
						   + "<input type='text' name='item' class='form-control' "
						   + " value='"+sqlBean.formatHTML(StrUtils.fString((String)m.get("PRODUCT")))+"' " 
						   + " readonly onchange='calculateAmount(this)'>"
						   + "</td>"
						   + "<td class=''>"
						   + "<input type='text' name='account_name' class='form-control accountSearch' " 
						   + "value='Local sales - retail' placeholder='Select Account'>"
						   + "</td>"
						   + "<td class=''>"
						   + "<input type='text' name='uom' value='"+ uom[i] +"' readonly class='form-control'>"
						   + "</td>"
						   + "<td class=''>"
						   + "<input type='text' name='loc' "
						   + "value='"+sqlBean.formatHTML(StrUtils.fString((String)m.get("LOCATION")))+"' "
					   		+ " readonly class='form-control'>"
						   + "</td>"
						   + "<td class='invcbatch'>"
						   + "<input type='text' name='batch' class='form-control batchSearch' value='NOBATCH'>"
						   + "</td>"
						   + "<td class='text-right'>"
						   + "<input name='qty' type='text' class='form-control text-right' value='"+orderQty[i]+"' "
						   + " readonly onchange='calculateAmount(this)'></td>"
						   + "<td class='text-right'>"
						   + "<input name='cost' type='text' class='form-control text-right' value='"+unitPrice[i]+"' "
						   + " readonly onchange='calculateAmount(this)'></td>"
						   + "<td class='text-center'>"
						   + "<div class='row'>"
						   + "<div class='col-lg-12 col-sm-3 col-12'>"
						   + "<div class='input-group my-group' style='width: 120px;'>"
						   + "<input name='item_discount' type='text' "
						   + "class='form-control text-right' value='0.00' "
						   + "onchange='calculateAmount(this)'>"
						   + "<select name='item_discounttype' id='item_discounttype' class='discountPicker form-control' "
						   + "onchange='calculateAmount(this)'>"
						   + "<option value='"+curency+"'>"+curency+"</option>"
						   + "<option value='%'>%</option>"
						   + "</select>"
						   + "</div>"
						   + "</div>"
						   + "</div>"
						   + "</td>"
							/*
							 * + "<td class=''><input type='hidden' name='tax_type'>" +
							 * "<input name='tax' type='text' class='form-control taxSearch'" +
							 * " placeholder='Select a Tax'></td>"
							 */
						   + "<td class='text-right grey-bg'>"
						   + "<input name='amount' type='text' class='form-control text-right' "
						   + "value='"+amount+"' readonly='readonly'>"
						   + "</td>"
						   
						   + "<td class='table-icon' style='position:relative;'>"
						   + "<a href='#' onclick='showRemarksDetails(this)'>"
						   + "<i class='fa fa-comment-o' title='Add Product Remarks' style='font-size: 15px;'></i>"
						   + "</a>"
						   + "</td>"
						   + "</tr>";
	    		   /**/
	    		   detlist += "<input type='hidden' name='ID' value='"+index[i]+"'>";
	    		   detlist += "<input type='hidden' name='UNITPRICE' value='"+unitPrice[i]+"'>";
	    		   detlist += "<input type='hidden' name='UOM' value='"+uom[i]+"'>";
	    		   detlist += "<input type='hidden' name='ORDERPRICE' value='"+orderQty[i]+"'>";
	    	   }   
		    	    
	    	   	com.track.dao.TblControlDAO _TblControlDAO = new com.track.dao.TblControlDAO();
				_TblControlDAO.setmLogger(mLogger);
				//String newestno = _TblControlDAO.getNextOrder(plant, userName, IConstants.ESTIMATE);
				
				request.getSession().setAttribute("TBODY", result);
				request.getSession().setAttribute("DETLIST", detlist);
				response.sendRedirect("../salesestimate/convertinvoice?cmd=conToinvc&CUST_CODE="+custno+"&CUST_NAME="+cname+"&TAXTREATMENT="
				+taxtreatment+"&NAME="+name+"&CTYPE="+customer_type_id+"&FITEM="+fitem+"&FLOC="+floc+
				"&FLOC_TYPE_ID="+floc_type_id+"&FLOC_TYPE_ID2="+floc_type_id2
				+"&FMODEL="+fmodel+"&FUOM="+fuom);
			}
		 
		 if (action.equalsIgnoreCase("Print Estimate Order With Price")) {
			 viewESTReport(request, response);
		}
		 		 
		} catch (Exception ex) {
			this.mLogger.exception(this.printLog, "", ex);

			String result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Exception : " + ex.getMessage() + "</font>";
			result = result
					+ " <br><br><center> "
					+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
			result = "<br><h3>" + result;
			request.getSession().setAttribute("RESULT", result);
			response.sendRedirect("jsp/displayResult2User.jsp");

		}
	}
	
	private String SaveData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		boolean flag = false,isAutoGenerate = false;
		boolean dotransferhdr = false;
		Hashtable ht = new Hashtable();
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		DateUtils dateUtils = new DateUtils();
		String rflag ="", ordertypeString="",result = "", estno = "", custName = "", display="",empname = "", 
				currencyid="",custCode = "", jobNum = "", personIncharge = "", user_id = "", contactNum = "", 
				address = "", address2 = "", address3 = "", collectionDate = "", collectionTime = "", remark1 = "",
				remark2 = "", remark3="",deliverydate="",timeslots="", outbound_Gst="",expirydate="",processno="",
				ordertype = "", orderstatus="",shippingcustomer="",shippingid="",orderdiscount="",shippingcost="",
				incoterms="",deldate="",paymenttype="",DATEFORMAT="",sTAXTREATMENT="";
		HttpSession session = request.getSession();
		String plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
		try {
		    rflag = StrUtils.fString(request.getParameter("RFLAG")).trim();
		    estno = StrUtils.fString(request.getParameter("ESTNO")).trim();
			custName = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("CUST_NAME"))).trim();
			custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
			user_id = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
			jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
			personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
			contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
			address = StrUtils.fString(request.getParameter("ADD1")).trim();
			address2 = StrUtils.fString(request.getParameter("ADD2")).trim();
			address3 = StrUtils.fString(request.getParameter("ADD3")).trim();
			collectionDate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
			collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
			remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
			remark2 = StrUtils.fString(request.getParameter("REMARK2")).trim();
			remark3 = StrUtils.fString(request.getParameter("REMARK3")).trim();
			display = StrUtils.fString(request.getParameter("DISPLAY")).trim();
			empname = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
			deliverydate=StrUtils.fString(request.getParameter("DELIVERYDATE")).trim();
			timeslots=StrUtils.fString(request.getParameter("TIMESLOTS")).trim();
			ordertype = StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
			orderstatus = StrUtils.fString(request.getParameter("STATUS_ID")).trim();
			outbound_Gst = StrUtils.fString(request.getParameter("GST")).trim();
		   	shippingcustomer=StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
			shippingid=StrUtils.fString(request.getParameter("SHIPPINGID")).trim();
			deldate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
            orderdiscount =  StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
            shippingcost =  StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
            incoterms =  StrUtils.fString(request.getParameter("INCOTERMS")).trim();
			paymenttype =  StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
        	expirydate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("EXPIREDATE")).trim());
            DATEFORMAT = StrUtils.fString((request.getParameter("DATEFORMAT") != null) ? "1": "0").trim();
            if(collectionDate.length()==0){collectionDate = new DateUtils().getDate();}
            sTAXTREATMENT=StrUtils.fString(request.getParameter("TAXTREATMENT")).trim();
            		
            isAutoGenerate = Boolean.valueOf(StrUtils.fString(request.getParameter("ISAUTOGENERATE")).trim()); 
			Hashtable curHash =new Hashtable();
			curHash.put(IConstants.PLANT, plant);
			curHash.put(IConstants.DISPLAY, display);
			if(display!=null&&display!="")
			{
				currencyid = curUtil.getCurrencyID(curHash,"CURRENCYID");
			}
			
			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			customerBeanDAO.setmLogger(mLogger);
			boolean isvalidCustomer = customerBeanDAO.isExistsCustomerName(custName, plant);
			Boolean isvalidShippingCustomer=Boolean.valueOf(true);
			if(shippingcustomer.length()>0)
			{
		    	isvalidShippingCustomer = _MasterDAO.isExistsShippingDetails(shippingcustomer, plant);
			}
			if (isvalidCustomer) {
				if (isvalidShippingCustomer) {
					ht.put(IDBConstants.PLANT, plant);
					ht.put(IDBConstants.ESTHDR_ESTNUM, estno);
					ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
					ht.put(IDBConstants.DOHDR_CUST_NAME, strUtils.InsertQuotes(custName));
					ht.put(IDBConstants.DOHDR_JOB_NUM, jobNum);
					ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, strUtils.InsertQuotes(personIncharge));
					ht.put(IDBConstants.DOHDR_CONTACT_NUM, contactNum);
					ht.put(IDBConstants.DOHDR_ADDRESS, address);
					ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
					ht.put(IDBConstants.DOHDR_ADDRESS3, address3);
					ht.put(IDBConstants.CREATED_BY, user_id);
					ht.put(IDBConstants.DOHDR_COL_DATE, collectionDate);
					ht.put(IDBConstants.DOHDR_COL_TIME, collectionTime);
					ht.put(IDBConstants.DOHDR_REMARK1, strUtils.InsertQuotes(remark1));
					ht.put(IDBConstants.DOHDR_REMARK2, strUtils.InsertQuotes(remark2));
					ht.put(IDBConstants.DOHDR_REMARK3, strUtils.InsertQuotes(remark3));
					ht.put(IDBConstants.STATUS, "Pending");
					ht.put(IDBConstants.ESTHDR_EMPNO, empname);
					ht.put(IDBConstants.CURRENCYID, currencyid);
					ht.put(IDBConstants.TIMESLOTS, timeslots);
					ht.put(IDBConstants.DELIVERYDATE, deliverydate);
					ht.put(IDBConstants.DOHDR_GST,outbound_Gst);
					ht.put(IDBConstants.ORDERTYPE, ordertype);
					ht.put(IDBConstants.ORDSTATUSID, orderstatus);
					ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
					ht.put(IDBConstants.SHIPPINGID, shippingid);
					ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
					ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
					ht.put(IDBConstants.INCOTERMS, incoterms);
					ht.put(IDBConstants.EXPIREDATE, expirydate);
                    ht.put(IDBConstants.PAYMENTTYPE, paymenttype);   
					ht.put(IDBConstants.POHDR_DELIVERYDATEFORMAT, DATEFORMAT);
					ht.put(IDBConstants.TAXTREATMENT, sTAXTREATMENT);
                    EstUtil.setmLogger(mLogger);
					flag = EstUtil.saveestHdrDetails(ht);
					    				
					if (flag) {
						
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_EST);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, estno);
						htRecvHis.put(IDBConstants.CREATED_BY, user_id);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
					    if (!remark1.equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(custName) + ","+ strUtils.InsertQuotes(remark1));
						} else {
							htRecvHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(custName));
						}

						htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());

						flag = movHisDao.insertIntoMovHis(htRecvHis);

					}
									
				    	Hashtable htMaster = new Hashtable();
						if(flag){
						if(remark1.length()>0)
						{
							htMaster.clear();
							htMaster.put(IDBConstants.PLANT, plant);
							htMaster.put(IDBConstants.REMARKS, remark1);
							
							if(!_MasterDAO.isExisitRemarks(htMaster, ""))
							{
								htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
							htMaster.put(IDBConstants.CREATED_BY, user_id);
								_MasterDAO.InsertRemarks(htMaster);
								
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
								htRecvHis.put("ORDNUM","");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS",remark1);
								htRecvHis.put(IDBConstants.CREATED_BY, user_id);
								htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								masterHisFlag = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}
						if(remark3.length()>0)
						{
							htMaster.clear();
							htMaster.put(IDBConstants.PLANT, plant);
							htMaster.put(IDBConstants.REMARKS, remark3);
							
							if(!_MasterDAO.isExisitRemarks(htMaster, ""))
							{
								htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
							htMaster.put(IDBConstants.CREATED_BY, user_id);
								_MasterDAO.InsertRemarks(htMaster);
								
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
								htRecvHis.put("ORDNUM","");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS",remark3);
								htRecvHis.put(IDBConstants.CREATED_BY, user_id);
								htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								masterHisFlag = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}
						
						
						if(incoterms.length()>0)
						{
							htMaster.clear();
							htMaster.put(IDBConstants.PLANT, plant);
							htMaster.put(IDBConstants.INCOTERMS, incoterms);
							
							if(!_MasterDAO.isExisitINCOTERMMST(htMaster, ""))
							{
								htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
								htMaster.put(IDBConstants.CREATED_BY, user_id);
								_MasterDAO.InsertINCOTERMS(htMaster);
								
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", "ADD_INCOTERMS_MST");
								htRecvHis.put("ORDNUM","");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS",incoterms);
								htRecvHis.put(IDBConstants.CREATED_BY, user_id);
								htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								masterHisFlag = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}
					}
					    			
				    
				    if(rflag.equals("1") || rflag.equals("3") ){
				        ordertypeString="Sales Estimate Order";
				    }
					if (flag) {
						result = "<font color=\"green\"> "+ordertypeString+" Created Successfully.  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/EstimateServlet?ESTNO="
								+ estno + "&Submit=View'\"> ";
						result = " "  + "<br><h3>" + result
								+ "</h3>";
						
						 new TblControlUtil().updateTblControlSaleEstiamateAndRentalServiceSeqNo(plant,IConstants.ESTIMATE,"SE",estno);
						
						String isAutoEmail = emailDao.getIsAutoEmailDetails(plant, IConstants.ESTIMATE_ORDER);
						if(isAutoEmail.equalsIgnoreCase("Y"))
						mailUtil.sendEmail(plant,estno,IConstants.ESTIMATE_ORDER);
						//End added by Bruhan for Email Notification on 11 July 2012.

					} else {
						result = "<font color=\"red\"> Error in Creating "+ordertypeString+"  - Please Check the Data  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
						result = " "+ordertypeString+" <br><h3>" + result+"</h3>";

					}
				} else {
					result = "<font color=\"red\"> Enter Shipping Customer <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = " "+ordertypeString+ "<br><h3>" + result+"</h3>";
				}
				
			} else {
				result = "<font color=\"red\"> Enter Valid Customer <br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = " "+ordertypeString+ "<br><h3>" + result+"</h3>";
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
            if(ThrowableUtil.getMessage(e).equalsIgnoreCase("estimate order created already")) 
			{
            	processno=estno;
            	estno = _TblControlDAO.getNextOrder(plant,user_id,IConstants.ESTIMATE);
            	ht.clear();
            	ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.ESTHDR_ESTNUM, estno);
				ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
				ht.put(IDBConstants.DOHDR_CUST_NAME, strUtils.InsertQuotes(custName));
				ht.put(IDBConstants.DOHDR_JOB_NUM, jobNum);
				ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, strUtils.InsertQuotes(personIncharge));
				ht.put(IDBConstants.DOHDR_CONTACT_NUM, contactNum);
				ht.put(IDBConstants.DOHDR_ADDRESS, address);
				ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
				ht.put(IDBConstants.DOHDR_ADDRESS3, address3);
				ht.put(IDBConstants.CREATED_BY, user_id);
				ht.put(IDBConstants.DOHDR_COL_DATE, collectionDate);
				ht.put(IDBConstants.DOHDR_COL_TIME, collectionTime);
				ht.put(IDBConstants.DOHDR_REMARK1, strUtils.InsertQuotes(remark1));
				ht.put(IDBConstants.DOHDR_REMARK2, strUtils.InsertQuotes(remark2));
				ht.put(IDBConstants.DOHDR_REMARK3, strUtils.InsertQuotes(remark3));
				ht.put(IDBConstants.STATUS, "Pending");
				ht.put(IDBConstants.ESTHDR_EMPNO, empname);
				ht.put(IDBConstants.CURRENCYID, currencyid);
				ht.put(IDBConstants.TIMESLOTS, timeslots);
				ht.put(IDBConstants.DELIVERYDATE, deliverydate);
				ht.put(IDBConstants.DOHDR_GST,outbound_Gst);
				ht.put(IDBConstants.ORDERTYPE, ordertype);
				ht.put(IDBConstants.ORDSTATUSID, orderstatus);
				ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
				ht.put(IDBConstants.SHIPPINGID, shippingid);
				ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
				ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
				ht.put(IDBConstants.INCOTERMS, incoterms);
				ht.put(IDBConstants.EXPIREDATE, expirydate);
				ht.put(IDBConstants.PAYMENTTYPE, paymenttype);   
				ht.put(IDBConstants.POHDR_DELIVERYDATEFORMAT, DATEFORMAT);
				ht.put(IDBConstants.TAXTREATMENT, sTAXTREATMENT);
                 EstUtil.setmLogger(mLogger);
				flag = EstUtil.saveestHdrDetails(ht);
				if (flag) {
					
					Hashtable htRecvHis = new Hashtable();
					htRecvHis.clear();
					htRecvHis.put(IDBConstants.PLANT, plant);
					htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_EST);
					htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
					htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
					htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, estno);
					htRecvHis.put(IDBConstants.CREATED_BY, user_id);
					htRecvHis.put("MOVTID", "");
					htRecvHis.put("RECID", "");
				    if (!remark1.equals("")) {
						htRecvHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(custName) + ","+ strUtils.InsertQuotes(remark1));
					} else {
						htRecvHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(custName));
					}
    				htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
					htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					flag = movHisDao.insertIntoMovHis(htRecvHis);
					new TblControlUtil().updateTblControlSaleEstiamateAndRentalServiceSeqNo(plant,IConstants.ESTIMATE,"SE",estno);

				}
				 result = "<font color=\"green\">Order Number " + processno + " has already been used. System has auto created a new Sales Estimate order " + estno + " for you.<br><br><center>"
							+ "<input type=\"button\" value=\"Back\"  onClick=\"window.location.href='/track/EstimateServlet?ESTNO="
							+ estno + "&Submit=View'\"> ";
				 result = " " + ordertypeString + "<br><h3>" + result
							+ "</h3>";
				 
				 
			}
            else
            {
            	throw e;
			}
			
		}

		return result;
	}

	private boolean DisplayData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String estno = StrUtils.fString(request.getParameter("ESTNO"));
		String fieldDesc = "<tr><td> Please Choose options from the list box shown above</td></tr>";
		ArrayList al = new ArrayList();
		Hashtable htCond = new Hashtable();
		Map m = new HashMap();
		if (estno.length() > 0) {
			HttpSession session = request.getSession();
			String plant = StrUtils.fString(
					(String) session.getAttribute("PLANT")).trim();
			String rflag = StrUtils.fString(
					(String) session.getAttribute("RFLAG")).trim();
			htCond.put("PLANT", plant);
			htCond.put("ESTNO", estno);

			String query = "estno,isnull(outbound_Gst,0) outbound_Gst," +
					"(select isnull(display,'') display from "+"["+plant+"_CURRENCYMST] where currencyid = a.currencyid) currencyid," +
							"custCode,jobNum, ordertype,personInCharge,contactNum,address,address2,address3," +
							"isnull(collectionDate,'') collectionDate,isnull(collectionTime,'') collectionTime," +
							"isnull(remark1,'') remark1,remark2,b.CNAME as custName,isnull(b.name,'') as contactname," +
							"isnull(b.telno,'') as telno,isnull(b.hpno,'') as hpno,isnull(b.hpno,'') as hpno,isnull(b.email,'') email," +
							"isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks," +
							"isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(deliverydate,'') deliverydate," +
							"isnull(timeslots,'') timeslots," +
							"isnull(shippingid,'')as shippingid, isnull(shippingcustomer,'') as shippingcustomer," +
							"isnull(STATUS_ID,'') as statusid,isnull(empno,'') as empno," +
							" isnull(expiredate,'')as expiredate,isnull(status,'')as status,isnull(remark3,'') remark3," +
							"ISNULL(CUSTOMER_STATUS_ID,'') customerstatusid,ISNULL(CUSTOMER_TYPE_ID,'')customertypeid," +
			                "isnull(orderdiscount,0) as orderdiscount,isnull(shippingcost,0) as shippingcost," +
			                "isnull(incoterms,'') as incoterms,isnull(DELIVERYDATEFORMAT,'') as DELIVERYDATEFORMAT,isnull(a.PAYMENTTYPE,'') payment_terms,isnull(A.TAXTREATMENT,'') as TAXTREATMENT  ";
			EstUtil.setmLogger(mLogger);
			al = EstUtil.getOutGoingestHdrDetails(query, htCond);
			if (al.size() > 0) {
				m = (Map) al.get(0);
                  fieldDesc = EstUtil.listESTDET(plant, estno, rflag);
                                
			} else {

				fieldDesc = "<tr><td colspan=\"7\" align=\"center\">No Records Available</td></tr>";
			}

		}
		request.getSession().setAttribute("podetVal", m);
		request.getSession().setAttribute("estno", estno);
		request.getSession().setAttribute("RESULT", fieldDesc);
		return true;
	}
	private String updateEstHdr(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		boolean flag = false;
		Hashtable ht = new Hashtable();
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		DateUtils dateUtils = new DateUtils();
	        String ordertypeString="";  
		String rflag ="",result = "", pono = "", custName = "",display="",status="", jobNum = "", 
				currencyid="",custCode = "", user = "", personIncharge = "", contactNum = "", 
				address = "", address2 = "", address3 = "", collectionDate = "", collectionTime = "", 
				remark1 = "", remark2 = "",remark3="",ordertype="",orderstatus="",deliverydate="",
		        outbound_Gst="",empname="",expiredate="",timeslots="",empno="",
		        shippingcustomer="",shippingid="",orderdiscount="",shippingcost="",incoterms="",paymenttype="",sTAXTREATMENT="";
		try {
		       rflag = StrUtils.fString(request.getParameter("RFLAG")).trim();
			pono = StrUtils.fString(request.getParameter("ESTNO")).trim();
			custName = StrUtils.fString(request.getParameter("CUST_NAME"))
					.trim();
			user = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
			jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
			personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
			contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
			custCode = StrUtils.fString(request.getParameter("CUST_CODE1"))	.trim();
			address = StrUtils.fString(request.getParameter("ADDRESS")).trim();
			address2 = StrUtils.fString(request.getParameter("ADDRESS2")).trim();
			address3 = StrUtils.fString(request.getParameter("ADDRESS3")).trim();
			collectionDate =  new DateUtils().parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
			collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
			remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
			remark2 = StrUtils.fString(request.getParameter("REMARK2")).trim();
			remark3 = StrUtils.fString(request.getParameter("REMARK3")).trim();
			ordertype = StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
			display = StrUtils.fString(request.getParameter("DISPLAY")).trim();
			deliverydate=StrUtils.fString(request.getParameter("DELIVERYDATE")).trim();
			timeslots=StrUtils.fString(request.getParameter("TIMESLOTS")).trim();
			outbound_Gst = StrUtils.fString(request.getParameter("GST")).trim();
		    if(collectionDate.length()==0){collectionDate = new DateUtils().getDate();}
		    orderstatus = StrUtils.fString(request.getParameter("STATUS_ID")).trim();
		    empname = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
		    shippingcustomer=StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
			shippingid=StrUtils.fString(request.getParameter("SHIPPINGID")).trim();
			orderdiscount =  StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
            shippingcost =  StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
            incoterms =  StrUtils.fString(request.getParameter("INCOTERMS")).trim();
			expiredate=StrUtils.fString(request.getParameter("EXPIREDATE")).trim();
            status = StrUtils.fString(request.getParameter("STATUS")).trim();
			paymenttype = StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
			String DATEFORMAT = StrUtils.fString((request.getParameter("DATEFORMAT") != null) ? "1": "0").trim();
			sTAXTREATMENT=StrUtils.fString(request.getParameter("TAXTREATMENT")).trim();
			HttpSession session = request.getSession();
			String plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			Hashtable curHash =new Hashtable();
			curHash.put(IConstants.PLANT, plant);
			curHash.put(IConstants.DISPLAY, display);
			if(display!=null&&display!="")
			{
				currencyid = curUtil.getCurrencyID(curHash,"CURRENCYID");
			}
			
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.ESTHDR_ESTNUM, pono);
		        ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
			ht.put(IDBConstants.DOHDR_CUST_NAME, strUtils.InsertQuotes(custName));
			ht.put(IDBConstants.DOHDR_JOB_NUM, jobNum);
			ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, strUtils.InsertQuotes(personIncharge));
			ht.put(IDBConstants.DOHDR_CONTACT_NUM, contactNum);
			ht.put(IDBConstants.DOHDR_ADDRESS, address);
			ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
			ht.put(IDBConstants.DOHDR_ADDRESS3, address3);
			ht.put(IDBConstants.DOHDR_COL_DATE, collectionDate);
			ht.put(IDBConstants.DOHDR_COL_TIME, collectionTime);
			ht.put(IDBConstants.DOHDR_REMARK1, strUtils.InsertQuotes(remark1));
			ht.put(IDBConstants.DOHDR_REMARK2, strUtils.InsertQuotes(remark2));
			ht.put(IDBConstants.DOHDR_REMARK3, strUtils.InsertQuotes(remark3));
			ht.put(IDBConstants.STATUS, status);
			ht.put(IDBConstants.ESTHDR_EMPNO, empname);
			ht.put(IDBConstants.CURRENCYID, currencyid);
			ht.put(IDBConstants.EXPIREDATE, expiredate);
			ht.put(IDBConstants.ORDERTYPE, ordertype);
			ht.put(IDBConstants.ORDSTATUSID, orderstatus);
			ht.put(IDBConstants.DELIVERYDATE, deliverydate);
			ht.put(IDBConstants.DOHDR_GST, outbound_Gst);
			ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
			ht.put(IDBConstants.SHIPPINGID, shippingid);
			ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
			ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
			ht.put(IDBConstants.INCOTERMS, incoterms);
            ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
			ht.put(IDBConstants.POHDR_DELIVERYDATEFORMAT, DATEFORMAT);
			ht.put(IDBConstants.TAXTREATMENT, sTAXTREATMENT);
			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			customerBeanDAO.setmLogger(mLogger);
			
			Hashtable htcond = new Hashtable();
			htcond.put(IDBConstants.PLANT, plant);
			htcond.put(IDBConstants.ESTHDR_ESTNUM, pono);
			
			boolean isconfirmorder = new EstHdrDAO().isExisit(htcond, "STATUS='Confirm'");
			if(!isconfirmorder){
			boolean isvalidCustomer = customerBeanDAO.isExistsCustomerName(custName, plant);
			Boolean isvalidShippingCustomer=Boolean.valueOf(true);
			if(shippingcustomer.length()>0)
			{
		    	isvalidShippingCustomer = _MasterDAO.isExistsShippingDetails(shippingcustomer, plant);
			}
			if (isvalidCustomer) {
				if (isvalidShippingCustomer) {

				EstUtil.setmLogger(mLogger);
				flag = EstUtil.updateestHdr(ht);
				if (flag) {

					Hashtable htRecvHis = new Hashtable();
					htRecvHis.clear();
					htRecvHis.put(IDBConstants.PLANT, plant);
					htRecvHis.put("DIRTYPE", TransactionConstants.UPDATE_EST);
					htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
					htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
					htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
					htRecvHis.put(IDBConstants.CREATED_BY, user);
					htRecvHis.put("MOVTID", "");
					htRecvHis.put("RECID", "");
					if (!remark1.equals("")) {
						htRecvHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(custName) + ","
								+ strUtils.InsertQuotes(remark1));
					} else {
						htRecvHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(custName));
					}

					htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils
							.getDateinyyyy_mm_dd(dateUtils.getDate()));
					htRecvHis.put(IDBConstants.CREATED_AT, dateUtils
							.getDateTime());

					flag = movHisDao.insertIntoMovHis(htRecvHis);

				}
				
				boolean masterHisFlag=false;
				Hashtable htMaster = new Hashtable();
					if(flag){
					if(remark1.length()>0)
					{
						htMaster.clear();
						htMaster.put(IDBConstants.PLANT, plant);
						htMaster.put(IDBConstants.REMARKS, remark1);
						
						if(!_MasterDAO.isExisitRemarks(htMaster, ""))
						{
							htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						htMaster.put(IDBConstants.CREATED_BY,user);
							_MasterDAO.InsertRemarks(htMaster);
							
							Hashtable htRecvHis = new Hashtable();
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
							htRecvHis.put("ORDNUM","");
							htRecvHis.put(IDBConstants.ITEM, "");
							htRecvHis.put("BATNO", "");
							htRecvHis.put(IDBConstants.LOC, "");
							htRecvHis.put("REMARKS",remark1);
							htRecvHis.put(IDBConstants.CREATED_BY, user);
							htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							masterHisFlag = movHisDao.insertIntoMovHis(htRecvHis);
						}
					}
					if(remark2.length()>0)
					{
						htMaster.clear();
						htMaster.put(IDBConstants.PLANT, plant);
						htMaster.put(IDBConstants.REMARKS, remark2);
						
						if(!_MasterDAO.isExisitRemarks(htMaster, ""))
						{
							htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						htMaster.put(IDBConstants.CREATED_BY, user);
							_MasterDAO.InsertRemarks(htMaster);
							
							Hashtable htRecvHis = new Hashtable();
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
							htRecvHis.put("ORDNUM","");
							htRecvHis.put(IDBConstants.ITEM, "");
							htRecvHis.put("BATNO", "");
							htRecvHis.put(IDBConstants.LOC, "");
							htRecvHis.put("REMARKS",remark3);
							htRecvHis.put(IDBConstants.CREATED_BY, user);
							htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							masterHisFlag = movHisDao.insertIntoMovHis(htRecvHis);
						}
					}
					
					
					if(incoterms.length()>0)
					{
						htMaster.clear();
						htMaster.put(IDBConstants.PLANT, plant);
						htMaster.put(IDBConstants.INCOTERMS, incoterms);
						
						if(!_MasterDAO.isExisitINCOTERMMST(htMaster, ""))
						{
							htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						htMaster.put(IDBConstants.CREATED_BY, user);
							_MasterDAO.InsertINCOTERMS(htMaster);
							
							Hashtable htRecvHis = new Hashtable();
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put("DIRTYPE", "ADD_INCOTERMS_MST");
							htRecvHis.put("ORDNUM","");
							htRecvHis.put(IDBConstants.ITEM, "");
							htRecvHis.put("BATNO", "");
							htRecvHis.put(IDBConstants.LOC, "");
							htRecvHis.put("REMARKS",incoterms);
							htRecvHis.put(IDBConstants.CREATED_BY, user);
							htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							masterHisFlag = movHisDao.insertIntoMovHis(htRecvHis);
						}
					}
				}
					
                            
			    if(rflag.equals("1") || rflag.equals("2") ){
			        ordertypeString="Sales Estimate Order";
			    }
			    else{
                                ordertypeString=" Order ";
                            }

			    if (flag) {
					
					
					result = "<font color=\"green\"> "+ordertypeString+" Updated Successfully.  <br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/EstimateServlet?ESTNO="
						+ pono + "&Submit=View'\"> ";
					result = ""+"<br><h3>" + result+"</h3>";

				} else {
					result = "<font color=\"red\"> Error in Updating "+ordertypeString+"  - Please Check the Data  <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = ""+ordertypeString+"<br><h3>" + result+"</h3>";
				}
			} else {
					result = "<font color=\"red\"> Enter Valid Shipping Customer <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = ""+ordertypeString+"<br><h3>" + result+"</h3>";
				}
			} else {
				result = "<font color=\"red\"> Enter Valid Customer <br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = ""+ordertypeString+"<br><h3>" + result+"</h3>";
			}
			}
			else{
				result = "<font color=\"red\">Processed Sales Estimate Order Cannot be Modified <br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = ""+ordertypeString+"<br><h3>" + result+"</h3>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}
	
	private HSSFWorkbook writeToExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
		String plant = "";
		ArrayList listQry = new ArrayList();
		try{
			HttpSession session = request.getSession();
			plant = session.getAttribute("PLANT").toString();
			String ESTNO =  StrUtils.fString(request.getParameter("ESTNO"))
                     .trim();
			String CUSTNAME = StrUtils.fString(request.getParameter("CUST_NAME")).trim();
			String DIRTYPE = StrUtils.fString(request.getParameter("DIRTYPE"));
			Hashtable htCond = new Hashtable();
			htCond.put("A.PLANT", plant);
			htCond.put("A.ESTNO", ESTNO);
			
			PlantMstDAO plantMstDAO = new PlantMstDAO();
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);

			//String query = "dono,dolnno,item,isnull(itemdesc,'') itemdesc,isnull(unitmo,'') uom,isnull(unitprice,0) unitprice,isnull(qtyor,0) as qtyor";
			
			 //listQry = dOUtil.getDoDetDetails(query, htCond);
			 listQry = EstUtil.getEstimateDetailsExportList(htCond,DIRTYPE,plant);
				
				 if (listQry.size() > 0) {
						HSSFSheet sheet = null ;
						HSSFCellStyle styleHeader = null;
						HSSFCellStyle dataStyle = null;
						 sheet = wb.createSheet("Sheet1");
						 styleHeader = createStyleHeader(wb);
						 sheet = this.createWidth(sheet);
						 sheet = this.createHeader(sheet,styleHeader);
										
						 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
							 Map lineArr = (Map) listQry.get(iCnt);	
							 int k = 0;
							 String collectiondate = StrUtils.fString((String)lineArr.get("collectiondate"));
							 if(collectiondate.length()>0){
							 collectiondate = collectiondate.substring(3,5)+"/"+collectiondate.substring(0,2)+"/"+collectiondate.substring(6) ;
							 }
							 String expiredate = StrUtils.fString((String)lineArr.get("expiredate"));
							 if(expiredate.length()>0){
								 expiredate = expiredate.substring(3,5)+"/"+expiredate.substring(0,2)+"/"+expiredate.substring(6) ;
							 }
		
								    dataStyle = createDataStyle(wb);
								    HSSFRow row = sheet.createRow((short) iCnt+1);
								    
								    HSSFCell cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("estno"))));
									cell.setCellStyle(dataStyle);
									
								    cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("estlnno"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("jobnum"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(collectiondate));
									cell.setCellStyle(dataStyle);
											
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("collectiontime"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("remarks"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("empno"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(expiredate));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("custcode"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("paymenttype"))));
									cell.setCellStyle(dataStyle);
								
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
									cell.setCellStyle(dataStyle);
									

									HSSFCellStyle numericCellStyle = createDataStyle(wb);
									numericCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
									cell = row.createCell((short) k++);
									//cell.setCellType(cell.CELL_TYPE_NUMERIC);
									cell.setCellValue(Numbers.toMillionFormat((String)lineArr.get("qtyor"),Integer.valueOf(numberOfDecimal)));
									cell.setCellStyle(numericCellStyle);
																			
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("unitmo"))));
									cell.setCellStyle(dataStyle);
									
								//By Samatha on 20/09/2013	
                                    String ConvertedUnitPrice= new EstHdrDAO().getUnitCostBasedOnCurIDSelected(plant, ESTNO,(String)lineArr.get("estlnno"),(String)lineArr.get("item"));
                                    double dConvertedUnitPrice = Double.parseDouble(StrUtils.fString(ConvertedUnitPrice));
                                    ConvertedUnitPrice = Numbers.toMillionFormat(dConvertedUnitPrice, numberOfDecimal);
                                    cell = row.createCell((short) k++);
									cell.setCellValue(ConvertedUnitPrice);
									cell.setCellStyle(numericCellStyle);
									
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("currencyid"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(Numbers.toMillionFormat((String) lineArr.get("orderdiscount"),Integer.valueOf(numberOfDecimal))));
									cell.setCellStyle(numericCellStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(Numbers.toMillionFormat((String) lineArr.get("shippingcost"),Integer.valueOf(numberOfDecimal))));
									cell.setCellStyle(numericCellStyle);
									
									
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("incoterms"))));
									cell.setCellStyle(dataStyle);
									

									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("shippingcustomer"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("TAXTREATMENT"))));
									cell.setCellStyle(dataStyle);
									
							  }
							
				 }
				 else if (listQry.size() < 1) {		
					

						System.out.println("No Records Found To List");
					}
			
			
			
		}catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		
		return wb;
	}
 
	
	 private HSSFCellStyle createStyleHeader(HSSFWorkbook wb){
			 HSSFCellStyle styleHeader = wb.createCellStyle();
			  HSSFFont fontHeader  = wb.createFont();
			  fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			  fontHeader.setFontName("Arial");	
			  styleHeader.setFont(fontHeader);
			  styleHeader.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			  styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			  styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			  styleHeader.setWrapText(true);
			  return styleHeader;
		}
		
	    private HSSFCellStyle createDataStyle(HSSFWorkbook wb){
			  HSSFCellStyle dataStyle = wb.createCellStyle();
			  dataStyle.setWrapText(true);
			  return dataStyle;
		}
	    
	    private HSSFSheet createHeader(HSSFSheet sheet, HSSFCellStyle styleHeader){
			int k = 0;
			try{
			
			
			HSSFRow rowhead = sheet.createRow((short) 0);
			HSSFCell cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("SALESESTNO"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("SALESESTLNNO"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Ref No"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Order Date"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Time"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Remarks"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Employee ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Expire Date"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Customer ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Payment Type"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Product ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Quantity"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Uom"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Unit price"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Currency ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Product Description"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Order Discount"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Shipping Cost"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Incoterm"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Shipping Customer"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Tax Treatment"));
			cell.setCellStyle(styleHeader);
					
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
		private HSSFSheet createWidth(HSSFSheet sheet){
			
			try{
				sheet.setColumnWidth((short)0 ,(short)5000);
				sheet.setColumnWidth((short)1 ,(short)3000);
				sheet.setColumnWidth((short)2 ,(short)7000);
				sheet.setColumnWidth((short)3 ,(short)3000);
				sheet.setColumnWidth((short)4 ,(short)3000);
				sheet.setColumnWidth((short)5 ,(short)10000);
				sheet.setColumnWidth((short)6 ,(short)5000);
				sheet.setColumnWidth((short)7 ,(short)5000);
				sheet.setColumnWidth((short)8 ,(short)5000);
				sheet.setColumnWidth((short)9 ,(short)6000);
				sheet.setColumnWidth((short)10 ,(short)6000);
				sheet.setColumnWidth((short)11 ,(short)3000);
				sheet.setColumnWidth((short)12 ,(short)3000);
				sheet.setColumnWidth((short)13 ,(short)3000);
				sheet.setColumnWidth((short)14 ,(short)3000);
				sheet.setColumnWidth((short)15 ,(short)12000);
				sheet.setColumnWidth((short)16 ,(short)3700);
				sheet.setColumnWidth((short)17 ,(short)3500);
				sheet.setColumnWidth((short)18 ,(short)7000);
				sheet.setColumnWidth((short)19 ,(short)7000);
				
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
	
		private HSSFWorkbook writeToExcelEstimatesummary(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
			String plant = "";
			String FROM_DATE ="",  TO_DATE = "", STATUS="",DIRTYPE ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
			String CUSTOMERID="",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_DEPT_ID="",PRD_CLS_ID="",EMP_NAME="",VIEWBY="";
			int SheetId =1;
			ArrayList listQry = new ArrayList();
			try{
				HttpSession session = request.getSession();
				plant = session.getAttribute("PLANT").toString();
				
				FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
				TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
				
				if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
				String curDate = _dateUtils.getDate();
				if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

				if (FROM_DATE.length()>5)

				fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

				if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
				if (TO_DATE.length()>5)
				tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

				DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
				JOBNO         = StrUtils.fString(request.getParameter("JOBNO"));
				USER          = StrUtils.fString(request.getParameter("USER"));
				ITEMNO        = StrUtils.fString(request.getParameter("ITEM"));
				DESC          = StrUtils.fString(request.getParameter("DESC"));
				ORDERNO       = StrUtils.fString(request.getParameter("ORDERNO"));
				CUSTOMER      = StrUtils.fString(request.getParameter("CUSTOMER"));
				CUSTOMERID      = StrUtils.fString(request.getParameter("CUSTOMERID"));
				STATUS    = StrUtils.fString(request.getParameter("STATUS"));
				PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
				PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
				PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
				PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
				EMP_NAME = StrUtils.fString(request.getParameter("EMP_NAME"));
				VIEWBY = StrUtils.fString(request.getParameter("VIEWBY"));
				String UOM = StrUtils.fString(request.getParameter("UOM"));
				String reportType ="";
				PlantMstDAO plantMstDAO = new PlantMstDAO();
		        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
				Hashtable ht = new Hashtable();
			      
		        if(StrUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
		        if(StrUtils.fString(ITEMNO).length() > 0)        ht.put("B.ITEM",ITEMNO);
		        if(StrUtils.fString(ORDERNO).length() > 0)        ht.put("B.ESTNO",ORDERNO);
		        if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
		        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
		        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
		        if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("C.PRD_DEPT_ID",PRD_DEPT_ID);
		        if(StrUtils.fString(STATUS).length() > 0) ht.put("A.STATUS",STATUS);
		        if(StrUtils.fString(EMP_NAME).length() > 0) ht.put("A.EMPNO",EMP_NAME);
		        if(StrUtils.fString(UOM).length() > 0) ht.put("B.SKTUOM",UOM);
		         listQry = htutil.getEstimateOrderSummaryList(ht,fdate,tdate,DIRTYPE,plant,DESC,CUSTOMER,VIEWBY);
		      		
					 if (listQry.size() >= 0) {
							HSSFSheet sheet = null ;
							HSSFCellStyle styleHeader = null;
							HSSFCellStyle dataStyle = null;
							HSSFCellStyle CompHeader = null;
							sheet = wb.createSheet("Sheet"+SheetId);
							 styleHeader = createStyleHeaderEstimatesummary(wb);
							  CompHeader = createCompStyleHeader(wb);
							 sheet = this.createWidthEstimatesummary(sheet);
							 sheet = this.createHeaderEstimatesummary(sheet,styleHeader);
							 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,plant);
							 
							 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
								 Map lineArr = (Map) listQry.get(iCnt);	
								 int k = 0;
								 
								 String unitPriceValue= (String)lineArr.get("unitprice");
								 String qtyOrValue= (String)lineArr.get("qtyor");
								 String qtyIssValue= (String)lineArr.get("qtyis");
								
								 
								 /*float unitPriceVal ="".equals(unitPriceValue) ? 0.0f :  Float.parseFloat(unitPriceValue);*/
								 float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
								 float qtyIssVal ="".equals(qtyIssValue) ? 0.0f :  Float.parseFloat(qtyIssValue);
								 double unitPriceVal ="".equals(unitPriceValue) ? 0.0d :  Double.parseDouble(unitPriceValue);
								 
						    		
						    		/*if(unitPriceVal==0f){
						    			unitPriceValue="0.00000";
						    		}else{
						    			unitPriceValue=unitPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						    		}*/if(qtyOrVal==0f){
						    			qtyOrValue="0.000";
						    		}else{
						    			qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						    		}if(qtyIssVal==0f){
						    			qtyIssValue="0.000";
						    		}else{
						    			qtyIssValue=qtyIssValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						    		}
						    		unitPriceValue = StrUtils.addZeroes(unitPriceVal, numberOfDecimal);
									    dataStyle = createDataStyle(wb);
									    HSSFRow row = sheet.createRow((short) iCnt+2);
									    
									    HSSFCell cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("estno"))));
										cell.setCellStyle(dataStyle);
										
									    cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("jobNum"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("dono"))));
										cell.setCellStyle(dataStyle);
																				
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("custname"))));
										cell.setCellStyle(dataStyle);
																							
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("remarks"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
										cell.setCellStyle(dataStyle);
									
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
										cell.setCellStyle(dataStyle);
									
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(unitPriceValue));
										cell.setCellStyle(dataStyle);
									
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TRANDATE"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("UOM"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										//cell.setCellType(cell.CELL_TYPE_NUMERIC);
										cell.setCellValue(qtyOrValue);
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(qtyIssValue);
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("empname"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("expiredate"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("status"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("dostatus"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TAXTREATMENT"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("users"))));
										cell.setCellStyle(dataStyle);
										
										
										
								  }
								
					 }
					 else if (listQry.size() < 1) {		
						

							System.out.println("No Records Found To List");
						}
				
				
				
			}catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			
			return wb;
		}
		
		
		private HSSFWorkbook writeToExcelEstimatesummaryWithOutPrice(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
			String plant = "";
			String FROM_DATE ="",  TO_DATE = "", STATUS="",DIRTYPE ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
			String CUSTOMERID="",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_DEPT_ID="",PRD_CLS_ID="",EMP_NAME="",VIEWBY="";
			int SheetId =1;
			ArrayList listQry = new ArrayList();
			try{
				HttpSession session = request.getSession();
				plant = session.getAttribute("PLANT").toString();
				
				FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
				TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
				
				if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
				String curDate = _dateUtils.getDate();
				if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

				if (FROM_DATE.length()>5)

				fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

				if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
				if (TO_DATE.length()>5)
				tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

				DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
				JOBNO         = StrUtils.fString(request.getParameter("JOBNO"));
				USER          = StrUtils.fString(request.getParameter("USER"));
				ITEMNO        = StrUtils.fString(request.getParameter("ITEM"));
				DESC          = StrUtils.fString(request.getParameter("DESC"));
				ORDERNO       = StrUtils.fString(request.getParameter("ORDERNO"));
				CUSTOMER      = StrUtils.fString(request.getParameter("CUSTOMER"));
				CUSTOMERID      = StrUtils.fString(request.getParameter("CUSTOMERID"));
				STATUS    = StrUtils.fString(request.getParameter("STATUS"));
				PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
				PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
				PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
				PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
				EMP_NAME = StrUtils.fString(request.getParameter("EMP_NAME"));
				VIEWBY = StrUtils.fString(request.getParameter("VIEWBY"));
				String UOM = StrUtils.fString(request.getParameter("UOM"));
				String reportType ="";
				Hashtable ht = new Hashtable();
			      
		        if(StrUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
		        if(StrUtils.fString(ITEMNO).length() > 0)        ht.put("B.ITEM",ITEMNO);
		        if(StrUtils.fString(ORDERNO).length() > 0)        ht.put("B.ESTNO",ORDERNO);
		        if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
		        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
		        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
		        if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("C.PRD_DEPT_ID",PRD_DEPT_ID);
		        if(StrUtils.fString(STATUS).length() > 0) ht.put("A.STATUS",STATUS);
		        if(StrUtils.fString(EMP_NAME).length() > 0) ht.put("A.EMPNO",EMP_NAME);
		        if(StrUtils.fString(UOM).length() > 0)        ht.put("B.SKTUOM",UOM);
		         listQry = htutil.getEstimateOrderSummaryList(ht,fdate,tdate,DIRTYPE,plant,DESC,CUSTOMER,VIEWBY);
		      		
					 if (listQry.size() >= 0) {
							HSSFSheet sheet = null ;
							HSSFCellStyle styleHeader = null;
							HSSFCellStyle dataStyle = null;
							HSSFCellStyle CompHeader = null;
							sheet = wb.createSheet("Sheet"+SheetId);
							 styleHeader = createStyleHeaderEstimatesummary(wb);
							  CompHeader = createCompStyleHeader(wb);
							 sheet = this.createWidthEstimatesummaryWithOutPrice(sheet);
							 sheet = this.createHeaderEstimatesummaryWithOutSummary(sheet,styleHeader);
							 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,plant);
							 
							 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
								 Map lineArr = (Map) listQry.get(iCnt);	
								 int k = 0;
								 
								 String unitPriceValue= (String)lineArr.get("unitprice");
								 String qtyOrValue= (String)lineArr.get("qtyor");
								 String qtyIssValue= (String)lineArr.get("qtyis");
								
								 
								 float unitPriceVal ="".equals(unitPriceValue) ? 0.0f :  Float.parseFloat(unitPriceValue);
								 float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
								 float qtyIssVal ="".equals(qtyIssValue) ? 0.0f :  Float.parseFloat(qtyIssValue);
								 
						    		
						    		if(unitPriceVal==0f){
						    			unitPriceValue="0.00000";
						    		}else{
						    			unitPriceValue=unitPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						    		}if(qtyOrVal==0f){
						    			qtyOrValue="0.000";
						    		}else{
						    			qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						    		}if(qtyIssVal==0f){
						    			qtyIssValue="0.000";
						    		}else{
						    			qtyIssValue=qtyIssValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						    		}
									    dataStyle = createDataStyle(wb);
									    HSSFRow row = sheet.createRow((short) iCnt+2);
									    
									    HSSFCell cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("estno"))));
										cell.setCellStyle(dataStyle);
										
									    cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("jobNum"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("dono"))));
										cell.setCellStyle(dataStyle);
																				
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("custname"))));
										cell.setCellStyle(dataStyle);
																							
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("remarks"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
										cell.setCellStyle(dataStyle);
									
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
										cell.setCellStyle(dataStyle);
									
																			
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TRANDATE"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("UOM"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										//cell.setCellType(cell.CELL_TYPE_NUMERIC);
										cell.setCellValue(qtyOrValue);
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(qtyIssValue);
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("empname"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("expiredate"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("status"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("dostatus"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TAXTREATMENT"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("users"))));
										cell.setCellStyle(dataStyle);
										
										
								  }
								
					 }
					 else if (listQry.size() < 1) {		
						

							System.out.println("No Records Found To List");
						}
				
				
				
			}catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			
			return wb;
		}
	 
		
		 private HSSFCellStyle createStyleHeaderEstimatesummary(HSSFWorkbook wb){
				 HSSFCellStyle styleHeader = wb.createCellStyle();
				  HSSFFont fontHeader  = wb.createFont();
				  fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				  fontHeader.setFontName("Arial");	
				  styleHeader.setFont(fontHeader);
				  styleHeader.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
				  styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				  styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				  styleHeader.setWrapText(true);
				  return styleHeader;
			}
			
		   private HSSFSheet createHeaderEstimatesummary(HSSFSheet sheet, HSSFCellStyle styleHeader){
				int k = 0;
				try{
				
				
				HSSFRow rowhead = sheet.createRow((short) 1);
				HSSFCell cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Order No"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Ref No"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Sales No"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Customer Name"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Remarks"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Product ID"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Description"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Unit Price"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Date"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("UOM"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Order Qty"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Converted Qty"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Employee ID"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Expiry Date"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Status"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Sales Order Status"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Tax Treatment"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("User"));
				cell.setCellStyle(styleHeader);
						
				}
				catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				return sheet;
			}
			private HSSFSheet createWidthEstimatesummary(HSSFSheet sheet){
				
				try{
					sheet.setColumnWidth((short)0 ,(short)5000);
					sheet.setColumnWidth((short)1 ,(short)5000);
					sheet.setColumnWidth((short)2 ,(short)5000);
					sheet.setColumnWidth((short)3 ,(short)7000);
					sheet.setColumnWidth((short)4 ,(short)10000);
					sheet.setColumnWidth((short)5 ,(short)4000);
					sheet.setColumnWidth((short)6 ,(short)10000);
					sheet.setColumnWidth((short)7 ,(short)3000);
					sheet.setColumnWidth((short)8 ,(short)4000);
					sheet.setColumnWidth((short)9 ,(short)3000);
					sheet.setColumnWidth((short)10 ,(short)3000);
					sheet.setColumnWidth((short)11 ,(short)5000);
					sheet.setColumnWidth((short)12 ,(short)4000);
					sheet.setColumnWidth((short)13 ,(short)4000);
					sheet.setColumnWidth((short)14 ,(short)4000);
										
				}
				catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				return sheet;
			}
			
			
			private HSSFSheet createHeaderEstimatesummaryWithOutSummary(HSSFSheet sheet, HSSFCellStyle styleHeader){
				int k = 0;
				try{
				
				
				HSSFRow rowhead = sheet.createRow((short) 1);
				HSSFCell cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Order No"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Ref No"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Sales No"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Customer Name"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Remarks"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Product ID"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Description"));
				cell.setCellStyle(styleHeader);
				
						
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Date"));
				cell.setCellStyle(styleHeader);
				
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("UOM"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Order Qty"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Converted Qty"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Employee ID"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Expiry Date"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Status"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Sales Order Status"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Tax Treatment"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("User"));
				cell.setCellStyle(styleHeader);
						
				}
				catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				return sheet;
			}
			private HSSFSheet createWidthEstimatesummaryWithOutPrice(HSSFSheet sheet){
				
				try{
					sheet.setColumnWidth((short)0 ,(short)5000);
					sheet.setColumnWidth((short)1 ,(short)5000);
					sheet.setColumnWidth((short)2 ,(short)5000);
					sheet.setColumnWidth((short)3 ,(short)7000);
					sheet.setColumnWidth((short)4 ,(short)10000);
					sheet.setColumnWidth((short)5 ,(short)4000);
					sheet.setColumnWidth((short)6 ,(short)10000);
					sheet.setColumnWidth((short)7 ,(short)3000);
					sheet.setColumnWidth((short)8 ,(short)4000);
					sheet.setColumnWidth((short)9 ,(short)3000);
					sheet.setColumnWidth((short)10 ,(short)5000);
					sheet.setColumnWidth((short)11 ,(short)4000);
					sheet.setColumnWidth((short)12 ,(short)4000);
					sheet.setColumnWidth((short)13 ,(short)4000);
										
				}
				catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				return sheet;
			}
		
	public void viewESTReport(HttpServletRequest request,HttpServletResponse response) throws IOException, Exception {
				Connection con = null;
				boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
				System.out.println("ajax : " + ajax);
				try {
					CustUtil cUtil = new CustUtil();
					PlantMstUtil pmUtil = new PlantMstUtil();
				    ESTUtil  _ESTUtil=  new ESTUtil();
				    CurrencyUtil currUtil = new CurrencyUtil();  
				    HttpSession session = request.getSession();
					Map m = null;
					String Plant = (String) session.getAttribute("PLANT");
					List viewlistQry = pmUtil.getPlantMstDetails(Plant);
					 Map maps = (Map) viewlistQry.get(0);
					String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CSTATE = "",SEALNAME="",SIGNNAME="",
					CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "",gstValue="",expirydate="",deliverydate="",
					CHPNO = "", CEMAIL = "",CRCBNO="",companyregno = "";
					String 	SHPCUSTOMERNAME="",SHPCONTACTNAME="",SHPTELEPHONE="",SHPHANDPHONE="",
							SHPFAX="",SHPEMAIL="",SHPUNITNO="",SHPBUILDING="",SHPSTREET="",
							SHPCITY="",SHPSTATE="",SHPCOUNTRY="",SHPPOSTALCODE="",DATAORDERDISCOUNT="",
							DATASHIPPINGCOST="",DATAINCOTERMS="",ORDERDISCOUNTTYPE = "",AdjustmentAmount="",TAX_TYPE="",ISDISCOUNTTAX="",ISSHIPPINGTAX="",
									ISORDERDISCOUNTTAX = "",DISCOUNT = "",DISCOUNT_TYPE="",PROJECT_NAME="",sales_location="",CWEBSITE ="";
					int TAXID=0;
					String ESTNO = StrUtils.fString(request.getParameter("ESTNO"));
					String SHIPPINGID= StrUtils.fString(request.getParameter("SHIPPINGID"));
					String PLANT = (String) session.getAttribute("PLANT");
					PlantMstDAO plantMstDAO = new PlantMstDAO();
			        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
					String imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + DbBean.LOGO_FILE;
					String imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo1.gif";
					SEALNAME=StrUtils.fString((String)maps.get("SEALNAME"));
		            SIGNNAME=StrUtils.fString((String)maps.get("SIGNATURENAME"));
	                String sealPath = "",signPath="";
	                //imti get seal name from plantmst
	                if(SEALNAME.equalsIgnoreCase("")){
	                	sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	                }else {
	                	sealPath = DbBean.COMPANY_SEAL_PATH + "/" + SEALNAME;
	                }
	                if(SIGNNAME.equalsIgnoreCase("")){
	                	signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	                }else {
	                   	signPath = DbBean.COMPANY_SIGNATURE_PATH + "/" + SIGNNAME;
	                }
	                //imti end
					String taxby=_PlantMstDAO.getTaxBy(PLANT);
					File checkImageFile = new File(imagePath);
					if(ESTNO == null || "".equals(ESTNO.trim())) {
						ESTNO = (String)request.getAttribute("ESTNO");
					}
			        if (!checkImageFile.exists()) {
			           imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
			        }
			        File checkImageFile1 = new File(imagePath1);
			        if (!checkImageFile1.exists()) {
			           imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
			        }			        
			        String imagePath2,Orientation="";			
				
			    	con = DbBean.getConnection();
					String SysDate = DateUtils.getDate();
					String jasperPath = DbBean.JASPER_INPUT + "/" + "rptestInvoice";
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
						CSTATE = (String) map.get("STATE");
						CZIP = (String) map.get("ZIP");
						CTEL = (String) map.get("TELNO");
						CFAX = (String) map.get("FAX");
						CONTACTNAME = (String) map.get("NAME");
						CHPNO = (String) map.get("HPNO");
						CEMAIL = (String) map.get("EMAIL");
						CRCBNO = (String) map.get("RCBNO");
						companyregno = (String) map.get("companyregnumber");//imtiuen
						CWEBSITE = (String) map.get("WEBSITE");
					}
					ArrayList arrCust = cUtil.getCustomerDetailsForEST(ESTNO, PLANT);
					if (arrCust.size() > 0) {
					
						String sCustCode = (String) arrCust.get(0);
						String sCustName = (String) arrCust.get(1);
						String sAddr1 = (String) arrCust.get(2);
						String sAddr2 = (String) arrCust.get(3);
						String sAddr3 = (String) arrCust.get(4);
						String sCountry = (String) arrCust.get(5);
						String sZip = (String) arrCust.get(6);
						String sCons = (String) arrCust.get(7);
						String sCustNameL = (String) arrCust.get(8);
						String sContactName = (String) arrCust.get(9);
						String sDesgination = (String) arrCust.get(10);
						String sTelNo = (String) arrCust.get(11);
						String sHpNo = (String) arrCust.get(12);
						String sFax = (String) arrCust.get(13);
						String sEmail = (String) arrCust.get(14);
						String sRemarks = (String) arrCust.get(15);
						String sAddr4 = (String) arrCust.get(16);
		                String orderRemarks = (String) arrCust.get(18);
		                String sPayTerms = (String) arrCust.get(19);
		                String orderRemarks3 = (String) arrCust.get(20);
		                String employeeId = (String) arrCust.get(21);
		                String sState = (String) arrCust.get(22);
		                SHIPPINGID = (String) arrCust.get(23);
		                String sRcbno = (String) arrCust.get(24);
		                String suenno = (String) arrCust.get(25);//imtiuen
						String orderType = new EstHdrDAO().getOrderTypeForEST(PLANT, ESTNO);
		                //get shipping details from shipping master table
	                    ArrayList arrShippingDetails =_masterUtil.getEstimateShippingDetails(ESTNO, sCustCode,PLANT);
		                if(arrShippingDetails.size()>0)
		                {
		                   	SHPCUSTOMERNAME=(String) arrShippingDetails.get(1);
		                	SHPCONTACTNAME=(String) arrShippingDetails.get(2);
		                	SHPTELEPHONE=(String) arrShippingDetails.get(3);
		                	SHPHANDPHONE=(String) arrShippingDetails.get(4);
		                	SHPFAX=(String) arrShippingDetails.get(5);
		                	SHPEMAIL=(String) arrShippingDetails.get(6);
		                	SHPUNITNO=(String) arrShippingDetails.get(7);
		                	SHPBUILDING=(String) arrShippingDetails.get(8);
		                	SHPSTREET=(String) arrShippingDetails.get(9);
		                	SHPCITY=(String) arrShippingDetails.get(10);
		                	SHPSTATE=(String) arrShippingDetails.get(11);
		                	SHPCOUNTRY=(String) arrShippingDetails.get(12);
		                	SHPPOSTALCODE=(String) arrShippingDetails.get(13);
		                            
		                }
         
		            	Hashtable htCond = new Hashtable();
						htCond.put("PLANT", PLANT);
						htCond.put("ESTNO", ESTNO);
						String query = "currencyid,isnull(outbound_Gst,0) as outbound_Gst," +
								"isnull(shippingcost,0)*ISNULL(CURRENCYUSEQT,1) shippingcost," +
								"isnull(incoterms,'') incoterms,isnull(expiredate,'')expirydate," +
								"isnull(CRBY,'') as CRBY,isnull(PAYMENTTYPE,'') PAYMENTTYPE,isnull(deliverydate,'') deliverydate,"
								+ "isnull(ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,isnull(ITEM_RATES,0) ISTAXINCLUSIVE,ISNULL(TAXID,0) TAXID,"
								+ "ISNULL((select DISTINCT TAXTYPE from FINCOUNTRYTAXTYPE B WHERE B.ID="+PLANT+"_esthdr.TAXID),'') TAX_TYPE,"
								+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_esthdr.PROJECTID),'') PROJECT_NAME,"
								+ "ISNULL((select DISTINCT PREFIX from FINSALESLOCATION B WHERE B.STATE="+PLANT+"_esthdr.sales_location),'') sales_location,"
								+ "ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,ISNULL(ISDISCOUNTTAX,0) ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,0) ISSHIPPINGTAX,ISNULL(ISORDERDISCOUNTTAX,0) ISORDERDISCOUNTTAX,"
								+ "CASE WHEN isnull(ORDERDISCOUNTTYPE,'%')='%' THEN isnull(orderdiscount,0) ELSE (isnull(orderdiscount,0)*ISNULL(CURRENCYUSEQT,1)) END AS orderdiscount,"
								+ "CASE WHEN isnull(DISCOUNT_TYPE,'%')='%' THEN isnull(DISCOUNT,0) ELSE (isnull(DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS DISCOUNT,"
								+ "ISNULL(DISCOUNT_TYPE,'') DISCOUNT_TYPE";
						ArrayList arrayesthdr = new ArrayList();
						arrayesthdr = _ESTUtil.getestHdrDetails(query, htCond);
						Map dataMap = (Map) arrayesthdr.get(0);
					    gstValue = dataMap.get("outbound_Gst").toString();
					    expirydate= dataMap.get("expirydate").toString();
					    deliverydate= dataMap.get("deliverydate").toString();
					    DATAORDERDISCOUNT=dataMap.get("orderdiscount").toString();
					    DATASHIPPINGCOST=dataMap.get("shippingcost").toString();
					    DATAINCOTERMS=dataMap.get("incoterms").toString();
						    //Get Currency Display
					    Hashtable curHash = new Hashtable();
					    curHash.put(IDBConstants.PLANT, PLANT);
					    curHash.put(IDBConstants.CURRENCYID, dataMap.get("currencyid").toString());
					    String curDisplay = currUtil.getCurrencyID(curHash, "DISPLAY");
		                Map parameters = new HashMap();
						// Customer Details
						parameters.put("imagePath", imagePath);
						parameters.put("imagePath1", imagePath1);
						parameters.put("OrderNo", ESTNO);
						parameters.put("company", PLANT);
						parameters.put("taxInvoiceTo_CompanyName", sCustName);
						parameters.put("taxInvoiceTo_BlockAddress", sAddr1+"  " + sAddr2);
						parameters.put("taxInvoiceTo_RoadAddress", sAddr3 +"  " + sAddr4);
						if (sState.equals("")) {
							parameters.put("taxInvoiceTo_Country",sCountry);
						}else {
							parameters.put("taxInvoiceTo_Country",sState+ "\n" +sCountry);
						}
//						parameters.put("taxInvoiceTo_Country", sState +", " + sCountry);
						parameters.put("taxInvoiceTo_ZIPCode", sZip);
						parameters.put("taxInvoiceTo_AttentionTo", sContactName);
						parameters.put("taxInvoiceTo_CCTO", "");
		                parameters.put("To_TelNo", sTelNo);
		                parameters.put("To_Fax", sFax);
		                parameters.put("To_Email", sEmail);
		                parameters.put("sRCBNO", sRcbno);
		                parameters.put("sUENNO", suenno);//imtiuen
		              
		                // Company Details
		                parameters.put("fromAddress_CompanyName", CNAME);
		                if(CADD1.equals("")) {
							parameters.put("fromAddress_BlockAddress", CADD2);
						}else {
							parameters.put("fromAddress_BlockAddress", CADD1 + ", " + CADD2);
						}
						if(CADD3.equals("")) {
							parameters.put("fromAddress_RoadAddress", CADD4);
						}else {
							parameters.put("fromAddress_RoadAddress", CADD3 + "," + CADD4);
						}
						//parameters.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
						//parameters.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
						if(CSTATE.equals("")) {
							parameters.put("fromAddress_Country", CCOUNTRY);
						}else {
							parameters.put("fromAddress_Country", CSTATE + ", "+ CCOUNTRY);
						}
						parameters.put("fromAddress_Website", CWEBSITE);
						parameters.put("fromAddress_ZIPCode", CZIP);
						parameters.put("fromAddress_TpNo", CTEL);
						parameters.put("fromAddress_FaxNo", CFAX);
						parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
						parameters.put("fromAddress_ContactPersonMobile", CHPNO);
						parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
						parameters.put("fromAddress_RCBNO", CRCBNO);
						parameters.put("fromAddress_UENNO", companyregno);//imtiuen
						parameters.put("currentTime", _ESTUtil.getOrderDateOnly(PLANT,ESTNO));
						parameters.put("taxInvoiceNo", "");
						parameters.put("InvoiceTerms", "");
						parameters.put("refNo", _ESTUtil.getJobNum(PLANT,ESTNO));
						OrderTypeDAO ODAO = new OrderTypeDAO();
						String orderDesc = "";
						String ordertype = orderType ;
						ESTUtil estUtil = new ESTUtil();
				       	Map ma = estUtil.getESTReceiptInvoiceHdrDetails(PLANT);
				        Orientation = (String) ma.get("PrintOrientation");
					    //parameters.put("OrderHeader", (String) ma.get("HDR1"));
						if (orderType.equals("ESTIMATE ORDER")) {
							orderDesc = (String) ma.get("HDR1");
						} else {

							orderDesc = ODAO.getOrderTypeDesc(PLANT, orderType);
						}
						if (ma.get("DISPLAYBYORDERTYPE").equals("1"))
						{
							parameters.put("OrderHeader", orderDesc);
						}
							else
							{
								parameters.put("OrderHeader", (String) ma.get("HDR1"));
							}
					    parameters.put("ToHeader", (String) ma.get("HDR2"));
						parameters.put("FromHeader", (String) ma.get("HDR3"));
						parameters.put("Date", (String) ma.get("DATE"));
						parameters.put("OrderNoHdr", (String) ma.get("ORDERNO"));
						parameters.put("RefNo", (String) ma.get("REFNO"));
						parameters.put("Terms",(String) ma.get("TERMS"));
						parameters.put("shipToId",SHIPPINGID);
						 if(ma.get("PRINTCUSTERMS").equals("1")){
							 //parameters.put("TermsDetails", sPayTerms);
							 parameters.put("TermsDetails", dataMap.get("PAYMENTTYPE").toString());
						 }else{
							 parameters.put("TermsDetails", (String) ma.get("TERMSDETAILS"));
						 }
						parameters.put("SoNo", (String) ma.get("SONO"));
						parameters.put("Item", (String) ma.get("ITEM"));
						parameters.put("Description", (String) ma.get("DESCRIPTION"));
						parameters.put("OrderQty", (String) ma.get("ORDERQTY"));;
						parameters.put("UOM", (String) ma.get("UOM"));
						parameters.put("Rate", (String) ma.get("RATE"));
						parameters.put("TaxAmount", (String) ma.get("TAXAMOUNT"));
						parameters.put("Amt", (String) ma.get("AMT"));
						parameters.put("SubTotal", (String) ma.get("SUBTOTAL")+" "+"("+curDisplay+")");
						if(taxby.equalsIgnoreCase("BYORDER")){
						   parameters.put("TotalTax", (String) ma.get("TOTALTAX")+" "+"("+gstValue+"%"+")");
						}else
						{
							parameters.put("TotalTax", (String) ma.get("TOTALTAX"));
						}
						parameters.put("Total", (String) ma.get("TOTAL")+" "+"("+curDisplay+")");
						
						String PRINTROUNDOFFTOTALWITHDECIMAL =  (String) ma.get("PRINTROUNDOFFTOTALWITHDECIMAL");
						parameters.put("PrintRoundoffTotalwithDecimal", PRINTROUNDOFFTOTALWITHDECIMAL);
						parameters.put("RoundoffTotalwithDecimal", (String) ma.get("ROUNDOFFTOTALWITHDECIMAL") + " " + "(" + curDisplay + ")");
						
						parameters.put("Footer1", (String) ma.get("F1"));
						parameters.put("Footer2", (String) ma.get("F2"));
					    parameters.put("Footer3", (String) ma.get("F3"));
					    parameters.put("Footer4", (String) ma.get("F4"));
					    parameters.put("Footer5", (String) ma.get("F5"));
					    parameters.put("Footer6", (String) ma.get("F6"));
					    parameters.put("Footer7", (String) ma.get("F7"));
					    parameters.put("Footer8", (String) ma.get("F8"));
					    parameters.put("Footer9", (String) ma.get("F9"));
						parameters.put("PRINTWITHPRODUCT", (String) ma.get("PRINTWITHPRODUCT"));
					    parameters.put("PrdXtraDetails", (String) ma.get("PRINTXTRADETAILS"));
					    parameters.put("PRINTWITHHSCODE", (String) ma.get("PRINTWITHHSCODE"));
					    parameters.put("EMPLOYEENAME", (String) ma.get("EMPLOYEENAME"));
					    parameters.put("RCBNO", (String) ma.get("RCBNO"));
					    parameters.put("UENNO", (String) ma.get("UENNO"));//imtiuen
						parameters.put("CUSTOMERUENNO", (String) ma.get("CUSTOMERUENNO"));//imtiuen
						parameters.put("PRINTUENNO",  ma.get("PRINTWITHUENNO"));//imthiuen
						parameters.put("PRINTCUSTOMERUENNO",  ma.get("PRINTWITHCUSTOMERUENNO"));//imthiuen
					    parameters.put("TOTALAFTERDISCOUNT", (String) ma.get("TOTALAFTERDISCOUNT"));
					    parameters.put("CUSTOMERRCBNO", (String) ma.get("CUSTOMERRCBNO"));
					    parameters.put("PRINTWITHEMPLOYEE", (String) ma.get("PRINTWITHEMPLOYEE"));
					    parameters.put("PRINTWITHCOO", (String) ma.get("PRINTWITHCOO"));
						parameters.put("PRINTWITHPRODUCTREMARKS", (String) ma.get("PRINTWITHPRODUCTREMARKS"));
						parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
						parameters.put("PRINTWITHHSCODE", (String) ma.get("PRINTWITHHSCODE"));
						parameters.put("PRINTWITHCOO", (String) ma.get("PRINTWITHCOO"));
						parameters.put("SHIPTO", (String) ma.get("SHIPTO"));
					    //parameters.put("lblOrderDiscount", (String) ma.get("ORDERDISCOUNT")+" "+"("+DATAORDERDISCOUNT+"%"+")");						
					    //parameters.put("lblShippingCost", (String) ma.get("SHIPPINGCOST")+" "+"("+curDisplay+")");
					    parameters.put("lblINCOTERM", (String) ma.get("INCOTERM"));
					    parameters.put("lblDeliveryDate", (String) ma.get("DELIVERYDATE"));
					    parameters.put("lblExpiryDate", "ExpiryDate");
					  
					    parameters.put("PRINTWITHSHIPINGADD", (String) ma.get("PRINTWITHSHIPINGADD"));
					    parameters.put("PRINTSHIPPINGCOST", (String) ma.get("PRINTSHIPPINGCOST"));
					    parameters.put("PRINTORDERDISCOUNT", (String) ma.get("PRINTORDERDISCOUNT"));
					    parameters.put("PRINTADJUSTMENT", (String) ma.get("PRINTADJUSTMENT"));
					    
					    
					    if(orderRemarks.length()>0) orderRemarks = (String) ma.get("REMARK1") +" : "+orderRemarks;
					    if(orderRemarks3.length()>0) orderRemarks3 = (String) ma.get("REMARK2") +" : "+orderRemarks3;
					    parameters.put("orderRemarks", orderRemarks);
					    parameters.put("orderRemarks3", orderRemarks3);
					   //parameter shipping address
		               	parameters.put("SHPCUSTOMERNAME", SHPCUSTOMERNAME);
		                if(SHPCONTACTNAME.length()>0) {
						    parameters.put("SHPCONTACTNAME","Attn: "+SHPCONTACTNAME);
		                }else{
		                	 parameters.put("SHPCONTACTNAME",SHPCONTACTNAME);
		                }
		                if(SHPTELEPHONE.length()>0) {
						    parameters.put("SHPTELEPHONE","Tel: "+SHPTELEPHONE);
		                }else{
		                	 parameters.put("SHPTELEPHONE",SHPTELEPHONE);
		                }
		                if(SHPHANDPHONE.length()>0) {
						    parameters.put("SHPHANDPHONE","HP: "+SHPHANDPHONE);
		                }else{
		                	 parameters.put("SHPHANDPHONE",SHPHANDPHONE);
		                }
						parameters.put("SHPUNITNO", SHPUNITNO);
						parameters.put("SHPBUILDING", SHPBUILDING);
					    parameters.put("SHPSTREET", SHPSTREET);
					    parameters.put("SHPCITY", SHPCITY);
//					    parameters.put("SHPSTATE", SHPSTATE);
						if (SHPSTATE.equals("")) {
							parameters.put("SHPCOUNTRY",SHPCOUNTRY);
						}else {
							parameters.put("SHPCOUNTRY",SHPSTATE+ "\n" +SHPCOUNTRY);
						}
//					    parameters.put("SHPCOUNTRY", SHPCOUNTRY);
					    parameters.put("SHPPOSTALCODE", SHPPOSTALCODE);

						//new changes - azees 1/2021 
						parameters.put("ISTAXINCLUSIVE",  dataMap.get("ISTAXINCLUSIVE").toString());
						ORDERDISCOUNTTYPE=dataMap.get("ORDERDISCOUNTTYPE").toString();
						DISCOUNT_TYPE=dataMap.get("DISCOUNT_TYPE").toString();
						ISSHIPPINGTAX=dataMap.get("ISSHIPPINGTAX").toString();
						ISDISCOUNTTAX=dataMap.get("ISDISCOUNTTAX").toString();
						ISORDERDISCOUNTTAX=dataMap.get("ISORDERDISCOUNTTAX").toString();
						DISCOUNT=dataMap.get("DISCOUNT").toString();
						PROJECT_NAME=dataMap.get("PROJECT_NAME").toString();
						sales_location=dataMap.get("sales_location").toString();
						double doubledatabilldiscount = new Double(DISCOUNT);
						parameters.put("DO_DISCOUNT", doubledatabilldiscount);
						parameters.put("ORDERDISCOUNTTYPE", ORDERDISCOUNTTYPE);
						parameters.put("DO_DISCOUNT_TYPE", DISCOUNT_TYPE);
						parameters.put("AdjustmentAmount", dataMap.get("ADJUSTMENT").toString());
						parameters.put("ISDISCOUNTTAX", ISDISCOUNTTAX);
						parameters.put("ISSHIPPINGTAX", ISSHIPPINGTAX);
						parameters.put("ISORDERDISCOUNTTAX", ISORDERDISCOUNTTAX);
						parameters.put("PROJECTNAME", PROJECT_NAME);
						if(ISSHIPPINGTAX.equalsIgnoreCase("0"))
							ISSHIPPINGTAX="Tax Exclusive";
						else
							ISSHIPPINGTAX="Tax Inclusive";
						if(ISDISCOUNTTAX.equalsIgnoreCase("0"))
							ISDISCOUNTTAX="Tax Exclusive";
						else
							ISDISCOUNTTAX="Tax Inclusive";
						if(ISORDERDISCOUNTTAX.equalsIgnoreCase("0"))
							ISORDERDISCOUNTTAX="Tax Exclusive";
						else
							ISORDERDISCOUNTTAX="Tax Inclusive";
						if(DISCOUNT_TYPE.equalsIgnoreCase("%"))
							parameters.put("lblDiscountDO", "Discount ("+ DISCOUNT + "%" +")"+" ("+ISDISCOUNTTAX+")");
						else
							parameters.put("lblDiscountDO", "Discount ("+DISCOUNT_TYPE+")"+" ("+ISDISCOUNTTAX+")");
						TAX_TYPE = dataMap.get("TAX_TYPE").toString();
						/*if(TAX_TYPE.equalsIgnoreCase("EXEMPT") || TAX_TYPE.equalsIgnoreCase("OUT OF SCOPE"))
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
						else
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+gstValue+"%]").trim();*/
						TAXID = Integer.valueOf(dataMap.get("TAXID").toString());
						FinCountryTaxType fintaxtype = new FinCountryTaxType();
						Short ptaxiszero=0,ptaxisshow=0;
						
						if(TAXID > 0){
							FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
							fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(TAXID);
							ptaxiszero=fintaxtype.getISZERO();
							ptaxisshow=fintaxtype.getSHOWTAX();
						}
						if(ptaxiszero == 1 && ptaxisshow == 0){							
							TAX_TYPE = "";
							gstValue="0.0";
						} else if(ptaxiszero == 0 && ptaxisshow == 0){
							TAX_TYPE = "";
							gstValue="0.0";
						} else if(ptaxiszero == 0 && ptaxisshow == 1){						
							if(sales_location.equalsIgnoreCase(""))
								TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+gstValue+"%]").trim();
							else
								TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") ["+gstValue+"%]").trim();
						} else {							
							
							if(sales_location.equalsIgnoreCase(""))
								TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
							else
								TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") [0.0%]").trim();
							gstValue="0.0";//Author: Azees  Create date: August 06,2021  Description: zero tax issue
						}
		  				parameters.put("TAX_TYPE", TAX_TYPE);
						
		  				parameters.put("Adjustment",  ma.get("ADJUSTMENT"));
						parameters.put("PRODUCTRATESARE",  ma.get("PRODUCTRATESARE"));
						parameters.put("PROJECT",  ma.get("PROJECT"));
						parameters.put("ISPROJECT",  ma.get("PRINTWITHPROJECT"));
						
						if(ORDERDISCOUNTTYPE.equalsIgnoreCase("%"))
							parameters.put("lblOrderDiscount",
									(String) ma.get("ORDERDISCOUNT") + " " + "(" + DATAORDERDISCOUNT + "%" + ") ("+ ISORDERDISCOUNTTAX + ")");
							else
								parameters.put("lblOrderDiscount",
										(String) ma.get("ORDERDISCOUNT") + " " + "(" + ORDERDISCOUNTTYPE + ") ("+ ISORDERDISCOUNTTAX + ")");
						
						parameters.put("lblShippingCost", (String) ma.get("SHIPPINGCOST")+" "+"("+curDisplay+") ("+ ISSHIPPINGTAX + ")");
						
					    //parameter shipping address end
					    
					    if(ma.get("PCUSREMARKS").equals("1"))
					    	 parameters.put("SupRemarks", sRemarks);
					    else
					    	parameters.put("SupRemarks", "");
					    parameters.put("curDisplay", curDisplay);   
					    
					    double gst=new Double(gstValue).doubleValue()/100;
			            parameters.put("Gst",gst);
			            
			            double doubledataorderdiscount=new Double(DATAORDERDISCOUNT);
			            double doubledatashippingcost=new Double(DATASHIPPINGCOST);
			            parameters.put("DATAEXPIRYDATE", expirydate);
			            parameters.put("DATADELIVERYDATE",  deliverydate);
			            parameters.put("TAXBY", taxby);  
					    parameters.put("DATAORDERDISCOUNT", doubledataorderdiscount);  
					    parameters.put("DATASHIPPINGCOST",doubledatashippingcost);  
					    parameters.put("DATAINCOTERMS", DATAINCOTERMS);  
						parameters.put("Discount", (String) ma.get("DISCOUNT"));
						parameters.put("NetRate", (String) ma.get("NETRATE"));
			            if(Orientation.equals("Portrait")){
		                if (!checkImageFile.exists()) {
							           imagePath2 = imagePath1; 
							           imagePath = "";
							        }
						        else if(!checkImageFile1.exists()){
						        	imagePath2 = imagePath; 
						        	imagePath1 = "";
						        }
						        else{
						        	imagePath2 ="";	        	
						        }
								parameters.put("imagePath", imagePath);
								parameters.put("imagePath1", imagePath1);
								parameters.put("imagePath2", imagePath2);
								//imti start seal,sign condition in printoutconfig
								String PRINTWITHCOMPANYSEAL = (String) ma.get("PRINTWITHCOMPANYSEAL");
								String PRINTWITHCOMPANYSIG= (String) ma.get("PRINTWITHCOMPANYSIG");
								if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
					                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
					                }
								if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
									signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
					                }
								parameters.put("sealPath", sealPath);
								parameters.put("signPath", signPath);
								parameters.put("numberOfDecimal", Integer.parseInt(numberOfDecimal));
								if (ma.get("PRINTWITHDISCOUNT").equals("1")){
									jasperPath = DbBean.JASPER_INPUT + "/" + "rptestInvoiceWithDiscountPortrait";
								}else {
									jasperPath = DbBean.JASPER_INPUT + "/" + "rptestInvoicePortrait";

							}
								
								
		                 }
			            String empname = new EmployeeDAO().getEmpname(PLANT, employeeId, "");
			            parameters.put("Employee", empname);
			            parameters.put("HSCode", ma.get("HSCODE"));
						parameters.put("COO", ma.get("COO"));
			            parameters.put("PRINTWITHDELIVERYDATE",  ma.get("PRINTWITHDELIVERYDATE"));	
			            parameters.put("PRODUCTDELIVERYDATE",  ma.get("PRODUCTDELIVERYDATE"));
						parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  ma.get("PRINTWITHPRODUCTDELIVERYDATE"));
						parameters.put("CALCULATETAXWITHSHIPPINGCOST",  ma.get("CALCULATETAXWITHSHIPPINGCOST"));
			            parameters.put("CompanyDate", (String) ma.get("COMPANYDATE"));
						parameters.put("CompanyName", (String) ma.get("COMPANYNAME"));
						parameters.put("CompanyStamp", (String) ma.get("COMPANYSTAMP"));
						parameters.put("CompanySig", (String) ma.get("COMPANYSIG"));
						parameters.put("PreBy", (String) ma.get("PREPAREDBY"));
						parameters.put("PreByUser", dataMap.get("CRBY").toString());
						parameters.put("AuthSign", (String) ma.get("AUTHSIGNATURE"));
						query = "item,itemdesc,unitprice";
						EstDetDAO estdetdao = new EstDetDAO();						
						ArrayList arrayestdet = estdetdao.selectESTDet(query, htCond,"");
						if(arrayestdet.size() > 3)
						{ }
						else
							parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
						
						long start = System.currentTimeMillis();
						System.out.println("**************" + " Start Up Time : "
								+ start + "**********");
                        System.out.println("Jasper file : " + jasperPath+".jasper");
						JasperCompileManager.compileReportToFile(
			    		 		jasperPath+".jrxml", jasperPath+".jasper");
						byte[] bytes = JasperRunManager.runReportToPdf(jasperPath+ ".jasper", parameters, con);
						if (!ajax) {
							//response.addHeader("Content-disposition","attachment;filename=reporte.pdf");
							response.addHeader("Content-disposition","inline;filename=reporte.pdf");
							response.setContentLength(bytes.length);
							response.getOutputStream().write(bytes);
							response.setContentType("application/pdf");
							response.getOutputStream().flush();
							response.getOutputStream().close();				
						}else {
//							System.out.println(jasperPath + ".pdf");
//							System.out.println(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Estimate_" + ESTNO + ".pdf");
							try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Estimate_" + ESTNO + ".pdf")){
								fos.write(bytes);
							}
							
						}
						
					}

				} catch (IOException e) {

					e.printStackTrace();
					

					
				} finally {
					DbBean.closeConnection(con);
				}
			}

private HSSFWorkbook writeToExcelEstimateSummaryWithRemarks(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
		 StrUtils strUtils = new StrUtils();
		 HTReportUtil movHisUtil  = new HTReportUtil();
		 DateUtils _dateUtils = new DateUtils();
		 ArrayList movQryList  = new ArrayList();
		 int maxRowsPerSheet = 65535;
		 String fdate="",tdate="";
		 int SheetId =1;
		try{
			
			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
           String ITEMNO    = strUtils.fString(request.getParameter("ITEM"));
           String DESC          = strUtils.fString(request.getParameter("DESC"));
           String FROM_DATE   = strUtils.fString(request.getParameter("FROM_DATE"));
           String TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
           String CUSTOMER      = strUtils.fString(request.getParameter("CUSTOMER"));
           String CUSTOMERID      = strUtils.fString(request.getParameter("CUSTOMERID"));
           String ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
           String JOBNO = strUtils.fString(request.getParameter("JOBNO"));
           String STATUS    = strUtils.fString(request.getParameter("STATUS"));
           String EMP_NAME = strUtils.fString(request.getParameter("EMP_NAME"));
           String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
           String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
           String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
           String VIEWBY = strUtils.fString(request.getParameter("VIEWBY"));
           String DIRTYPE       = strUtils.fString(request.getParameter("DIRTYPE"));
           String SORT = strUtils.fString(request.getParameter("SORT"));
           String EMPNO = strUtils.fString(request.getParameter("EMP_NAME"));
           String CUSTOMERTYPE = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
           String  CURRENCYID      = strUtils.fString(request.getParameter("CURRENCYID"));
           String  CURRENCYDISPLAY = strUtils.fString(request.getParameter("CURRENCYDISPLAY"));
             String reportType ="";
			 
           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =_dateUtils.getDate();
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
           if (FROM_DATE.length()>5)
             fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
           Hashtable ht = new Hashtable();


           if(strUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
           if(strUtils.fString(ITEMNO).length() > 0)        ht.put("B.ITEM",ITEMNO);
           if(strUtils.fString(ORDERNO).length() > 0)        ht.put("B.ESTNO",ORDERNO);
           if(strUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
           if(strUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
           if(strUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
           if(strUtils.fString(STATUS).length() > 0) ht.put("A.STATUS",STATUS);
           if(strUtils.fString(EMP_NAME).length() > 0) ht.put("A.EMPNO",EMP_NAME);
                
		        
               movQryList = movHisUtil.getEstimateOrderSummaryList(ht,fdate,tdate,"ESTIMATEPRODUCTREMARKS",PLANT,DESC,CUSTOMER,VIEWBY);
          
				 Boolean workSheetCreated = true;
				 if (movQryList.size() > 0) {
						HSSFSheet sheet = null ;
						HSSFCellStyle styleHeader = null;
						HSSFCellStyle dataStyle = null;
						HSSFCellStyle dataStyleSpecial = null;
						HSSFCellStyle CompHeader = null;
						dataStyle = createDataStyle(wb);	
						dataStyleSpecial = createDataStyle(wb);	
						CreationHelper createHelper = wb.getCreationHelper();
						sheet = wb.createSheet("Sheet"+SheetId);
						styleHeader = createStyleHeader(wb);
						CompHeader = createCompStyleHeader(wb);
						sheet = this.createWidthEstimateSummaryWithRemarks(sheet);
						sheet = this.createHeaderEstimateSummaryWithRemarks(sheet,styleHeader);
						sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
						 int index = 2;
				         CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
				         String customerstatusid="",customerstatusdesc="",customertypeid ="",customertypedesc="";

				         
				          for (int iCnt =0; iCnt<movQryList.size(); iCnt++){	
								   Map lineArr = (Map) movQryList.get(iCnt);	
								   int k = 0;
								 
								  
								    HSSFRow row = sheet.createRow(index);
			                                                                                                           
									HSSFCell cell = row.createCell( k++);
									cell.setCellValue(iCnt+1);
								    cell.setCellStyle(dataStyle);
									
								    cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("estno"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prdremarks"))));
									cell.setCellStyle(dataStyle);
															
									index++;
									/* if((index-1)%maxRowsPerSheet==0){
										 index = 1;
										 sheet = wb.createSheet();
										 styleHeader = createStyleHeader(wb);
										 sheet = this.createWidth(sheet,type);
										 sheet = this.createHeaderOutboundSummary(sheet,styleHeader,type);
										 
									 }*/
						 
						 }
							
				 }
				 else if (movQryList.size() < 1) {		
					System.out.println("No Records Found To List");
				}
		}catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		
		return wb;
	}
		
	private HSSFSheet createWidthEstimateSummaryWithRemarks(HSSFSheet sheet){
		int i = 0;
		try{
			sheet.setColumnWidth(i++ ,1000);
			sheet.setColumnWidth(i++ ,4000);
			sheet.setColumnWidth(i++ ,4000);
			sheet.setColumnWidth(i++ ,8000);		
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}	
	
	private HSSFSheet createHeaderEstimateSummaryWithRemarks(HSSFSheet sheet, HSSFCellStyle styleHeader){
		int k = 0;
		try{
			
		HSSFRow rowhead = sheet.createRow(1);
		
		HSSFCell cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("S/N"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Order No"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Product ID"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Remarks"));
		cell.setCellStyle(styleHeader);
				
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
			
		
		
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
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
	
		private HSSFSheet createHeaderCompanyReports(HSSFSheet sheet, HSSFCellStyle styleHeader,String reportType,String PLANT){
		int k = 0;
		try{
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE="", CTEL = "",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",COL1="",COL2="" ;
			PlantMstUtil pmUtil = new PlantMstUtil();
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);
				CNAME = (String) map.get("PLNTDESC");
				
				
				CCOUNTRY = (String) map.get("COUNTY");
				CSTATE = (String) map.get("STATE");
				CZIP = (String) map.get("ZIP");
				if((CSTATE).length()>1)
					CNAME=CNAME+", "+CSTATE;
				
				if((CCOUNTRY).length()>1)
					CNAME=CNAME+", "+CCOUNTRY;
				
				if((CZIP).length()>1)
					CNAME=CNAME+"-"+CZIP;
					
					
			}
			
		HSSFRow rowhead = sheet.createRow(0);
		HSSFCell cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString(CNAME));	
		cell.setCellStyle(styleHeader);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 4);
		sheet.addMergedRegion(cellRangeAddress);
														
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	private HSSFCellStyle createCompStyleHeader(HSSFWorkbook wb){
		
		//Create style
		 HSSFCellStyle styleHeader = wb.createCellStyle();
		  HSSFFont fontHeader  = wb.createFont();
		  fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		  fontHeader.setFontName("Arial");	
		  styleHeader.setFont(fontHeader);
		  styleHeader.setFillForegroundColor(HSSFColor.WHITE.index);
		  styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		  styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		  styleHeader.setWrapText(true);
		  return styleHeader;
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
	public String getNextIdDetails(String plant,String user_id)
	{
		String	Acctid = "",sBatchSeq="",sZero="";
		boolean insertFlag=false; // Get Account Id
		_TblControlDAO.setmLogger(mLogger);								  	
	    Hashtable  htnxt  =new Hashtable();
	    String query=" isnull(NXTSEQ,'') as NXTSEQ";
	    htnxt.put(IDBConstants.PLANT,plant);
	    htnxt.put(IDBConstants.TBL_FUNCTION,"ACCOUNT");
	    try{
	       boolean exitFlag=false; boolean resultflag=false;
	       exitFlag=_TblControlDAO.isExisit(htnxt,"",plant);
	       if (exitFlag==false)
		      {         
		            Map htInsert=null;
		            Hashtable htTblCntInsert  = new Hashtable();
		            htTblCntInsert.put(IDBConstants.PLANT,plant);
		            htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"ACCOUNT");
		            htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"A");
		            htTblCntInsert.put(IDBConstants.TBL_MINSEQ,"0000");
		            htTblCntInsert.put(IDBConstants.TBL_MAXSEQ,"9999");
		            htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
		            htTblCntInsert.put(IDBConstants.CREATED_BY, user_id);
		            htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
		            insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
		            Acctid="A"+"0001";
		      }
	       else
		      {
		           //--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth
		           Map m= _TblControlDAO.selectRow(query, htnxt,"");
		           sBatchSeq=(String)m.get("NXTSEQ");
		           int inxtSeq=Integer.parseInt(((String)sBatchSeq.trim().toString()))+1;
		           String updatedSeq=Integer.toString(inxtSeq);
		            if(updatedSeq.length()==1)
		           {
		             sZero="000";
		           }
		           else if(updatedSeq.length()==2)
		           {
		             sZero="00";
		           }
		           else if(updatedSeq.length()==3)
		           {
		             sZero="0";
		           }
		            Map htUpdate = null;
		           Hashtable htTblCntUpdate = new Hashtable();
		           htTblCntUpdate.put(IDBConstants.PLANT,plant);
		           htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"ACCOUNT");
		           htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"A");
		           StringBuffer updateQyery=new StringBuffer("set ");
		           updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");   
		           Acctid="A"+sZero+updatedSeq;
		        }
		      } catch(Exception e)
		         {
		    	  mLogger.exception(true,
							"ERROR IN PAGE", e);
		         }
		return Acctid;
	}
	
	private Map getOutstandingAmountForCustomer(String custCode, double orderAmount, String plant) throws Exception{
		String outstdamt = "", creditLimitBy ="", creditLimit = "", creditBy = "";
		Map resultMap = new HashMap();
		String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
		
		CustUtil custUtil = new CustUtil();
    	ArrayList arrCust = custUtil.getCustomerDetails(custCode,plant);
		creditLimit   = (String)arrCust.get(24);
		creditLimitBy   = (String)arrCust.get(35);
		
		if(creditLimitBy.equalsIgnoreCase("NOLIMIT")) {
			resultMap.put("STATUS", false);
			resultMap.put("MSG", "");
		}else {
			DateFormat currentdate = new SimpleDateFormat("dd/MM/yyyy");
	    	Date date = new Date();
	    	String current_date=currentdate.format(date); 
	    	
	    	Calendar firstDayOfMonth = Calendar.getInstance();   
	    	firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
	    	String startDate = currentdate.format(firstDayOfMonth.getTime()); 
	    	System.out.println(firstDayOfMonth.getTime()+"  "+startDate);
	    	
	        Calendar lastDayOfMonth = Calendar.getInstance();
	        lastDayOfMonth.set(Calendar.DATE, lastDayOfMonth.getActualMaximum(Calendar.DATE));
	        String endDate= currentdate.format(lastDayOfMonth.getTime());
	        System.out.println(firstDayOfMonth.getTime()+"  "+endDate);
	        
	        if(creditLimitBy.equalsIgnoreCase("DAILY")) {
	        	startDate = current_date;
	        	endDate = current_date;
	        }
	        try {
				outstdamt = new InvoicePaymentUtil().getOutStdAmt(plant,custCode, startDate, endDate);
				double allowedCreditval = Double.parseDouble(creditLimit)-((Double.parseDouble(outstdamt)+orderAmount)); 
				creditLimit = StrUtils.addZeroes(Double.parseDouble(creditLimit), numberOfDecimal);
				if(allowedCreditval >= 0){
					resultMap.put("STATUS", false);
					resultMap.put("MSG", "");
				} else {
					resultMap.put("STATUS", true);
					resultMap.put("MSG", "Estimate amount cannot exceed "+ custCode +" Available Credit Limit " 
					+ _PlantMstDAO.getBaseCurrency(plant) + " " + creditLimit);
					throw new Exception("Estimate amount cannot exceed "+ custCode +" Available Credit Limit " 
							+ _PlantMstDAO.getBaseCurrency(plant) + " " + creditLimit);
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return resultMap;
	}
	
	private String SaveEstData(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException,
			Exception {

		boolean flag = false,isAutoGenerate = false;
		boolean dotransferhdr = false;
		Hashtable ht = new Hashtable();
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		DateUtils dateUtils = new DateUtils();
		String rflag ="", ordertypeString="",result = "", estno = "", custName = "", display="",empname = "", 
				currencyid="",custCode = "", jobNum = "", personIncharge = "", user_id = "", contactNum = "", 
				address = "", address2 = "", address3 = "", collectionDate = "", collectionTime = "", remark1 = "",
				remark2 = "", remark3="",deliverydate="",timeslots="", outbound_Gst="",expirydate="",processno="",
				ordertype = "", orderstatus="",shippingcustomer="",shippingid="",orderdiscount="",shippingcost="",
				incoterms="",deldate="",paymenttype="",DATEFORMAT="",sTAXTREATMENT="",item="",itemDesc="",taxby="",prodgst="";
		HttpSession session = request.getSession();
		String plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
		UserTransaction ut = null;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
		    rflag = StrUtils.fString(request.getParameter("RFLAG")).trim();
		    estno = StrUtils.fString(request.getParameter("ESTNO")).trim();
			custName = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("CUST_NAME"))).trim();
			custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
			user_id = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
			jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
			personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
			contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
			address = StrUtils.fString(request.getParameter("ADD1")).trim();
			address2 = StrUtils.fString(request.getParameter("ADD2")).trim();
			address3 = StrUtils.fString(request.getParameter("ADD3")).trim();
			collectionDate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
			collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
			remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
			remark2 = StrUtils.fString(request.getParameter("REMARK2")).trim();
			remark3 = StrUtils.fString(request.getParameter("REMARK3")).trim();
			display = StrUtils.fString(request.getParameter("DISPLAY")).trim();
			empname = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
			deliverydate=StrUtils.fString(request.getParameter("DELIVERYDATE")).trim();
			timeslots=StrUtils.fString(request.getParameter("TIMESLOTS")).trim();
			ordertype = StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
			orderstatus = StrUtils.fString(request.getParameter("STATUS_ID")).trim();
			outbound_Gst = StrUtils.fString(request.getParameter("GST")).trim();
		   	shippingcustomer=StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
			shippingid=StrUtils.fString(request.getParameter("SHIPPINGID")).trim();
			deldate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
            orderdiscount =  StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
            shippingcost =  StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
            incoterms =  StrUtils.fString(request.getParameter("INCOTERMS")).trim();
			paymenttype =  StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
        	expirydate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("EXPIREDATE")).trim());
            DATEFORMAT = StrUtils.fString((request.getParameter("DATEFORMAT") != null) ? "1": "0").trim();
            if(collectionDate.length()==0){collectionDate = new DateUtils().getDate();}
            sTAXTREATMENT=StrUtils.fString(request.getParameter("TAXTREATMENT")).trim();
            
           String[] index = request.getParameterValues("ID");
     	   String[] unitPrice = request.getParameterValues("UNITPRICE");
     	   String[] orderQty = request.getParameterValues("ORDERPRICE");
     	   String[] uom = request.getParameterValues("UOM");
   		   
            		
            isAutoGenerate = Boolean.valueOf(StrUtils.fString(request.getParameter("ISAUTOGENERATE")).trim()); 
			Hashtable curHash =new Hashtable();
			curHash.put(IConstants.PLANT, plant);
			curHash.put(IConstants.DISPLAY, display);
			if(display!=null&&display!="")
			{
				currencyid = curUtil.getCurrencyID(curHash,"CURRENCYID");
			}
			
			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			customerBeanDAO.setmLogger(mLogger);
			boolean isvalidCustomer = customerBeanDAO.isExistsCustomerName(custName, plant);
			Boolean isvalidShippingCustomer=Boolean.valueOf(true);
			if(shippingcustomer.length()>0)
			{
		    	isvalidShippingCustomer = _MasterDAO.isExistsShippingDetails(shippingcustomer, plant);
			}
			if (isvalidCustomer) {
				if (isvalidShippingCustomer) {
					ht.put(IDBConstants.PLANT, plant);
					ht.put(IDBConstants.ESTHDR_ESTNUM, estno);
					ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
					ht.put(IDBConstants.DOHDR_CUST_NAME, strUtils.InsertQuotes(custName));
					ht.put(IDBConstants.DOHDR_JOB_NUM, jobNum);
					ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, strUtils.InsertQuotes(personIncharge));
					ht.put(IDBConstants.DOHDR_CONTACT_NUM, contactNum);
					ht.put(IDBConstants.DOHDR_ADDRESS, address);
					ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
					ht.put(IDBConstants.DOHDR_ADDRESS3, address3);
					ht.put(IDBConstants.CREATED_BY, user_id);
					ht.put(IDBConstants.DOHDR_COL_DATE, collectionDate);
					ht.put(IDBConstants.DOHDR_COL_TIME, collectionTime);
					ht.put(IDBConstants.DOHDR_REMARK1, strUtils.InsertQuotes(remark1));
					ht.put(IDBConstants.DOHDR_REMARK2, strUtils.InsertQuotes(remark2));
					ht.put(IDBConstants.DOHDR_REMARK3, strUtils.InsertQuotes(remark3));
					ht.put(IDBConstants.STATUS, "Pending");
					ht.put(IDBConstants.ESTHDR_EMPNO, empname);
					ht.put(IDBConstants.CURRENCYID, currencyid);
					ht.put(IDBConstants.TIMESLOTS, timeslots);
					ht.put(IDBConstants.DELIVERYDATE, deliverydate);
					ht.put(IDBConstants.DOHDR_GST,outbound_Gst);
					ht.put(IDBConstants.ORDERTYPE, ordertype);
					ht.put(IDBConstants.ORDSTATUSID, orderstatus);
					ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
					ht.put(IDBConstants.SHIPPINGID, shippingid);
					ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
					ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
					ht.put(IDBConstants.INCOTERMS, incoterms);
					ht.put(IDBConstants.EXPIREDATE, expirydate);
                    ht.put(IDBConstants.PAYMENTTYPE, paymenttype);   
					ht.put(IDBConstants.POHDR_DELIVERYDATEFORMAT, DATEFORMAT);
					ht.put(IDBConstants.TAXTREATMENT, sTAXTREATMENT);
                    EstUtil.setmLogger(mLogger);
					flag = EstUtil.saveestHdrDetails(ht);
					    				
					if (flag) {
						
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_EST);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, estno);
						htRecvHis.put(IDBConstants.CREATED_BY, user_id);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
					    if (!remark1.equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(custName) + ","+ strUtils.InsertQuotes(remark1));
						} else {
							htRecvHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(custName));
						}

						htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());

						flag = movHisDao.insertIntoMovHis(htRecvHis);

					}
									
				    	Hashtable htMaster = new Hashtable();
						if(flag){
						if(remark1.length()>0)
						{
							htMaster.clear();
							htMaster.put(IDBConstants.PLANT, plant);
							htMaster.put(IDBConstants.REMARKS, remark1);
							
							if(!_MasterDAO.isExisitRemarks(htMaster, ""))
							{
								htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
							htMaster.put(IDBConstants.CREATED_BY, user_id);
								_MasterDAO.InsertRemarks(htMaster);
								
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
								htRecvHis.put("ORDNUM","");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS",remark1);
								htRecvHis.put(IDBConstants.CREATED_BY, user_id);
								htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								masterHisFlag = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}
						if(remark3.length()>0)
						{
							htMaster.clear();
							htMaster.put(IDBConstants.PLANT, plant);
							htMaster.put(IDBConstants.REMARKS, remark3);
							
							if(!_MasterDAO.isExisitRemarks(htMaster, ""))
							{
								htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
							htMaster.put(IDBConstants.CREATED_BY, user_id);
								_MasterDAO.InsertRemarks(htMaster);
								
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
								htRecvHis.put("ORDNUM","");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS",remark3);
								htRecvHis.put(IDBConstants.CREATED_BY, user_id);
								htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								masterHisFlag = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}
						
						
						if(incoterms.length()>0)
						{
							htMaster.clear();
							htMaster.put(IDBConstants.PLANT, plant);
							htMaster.put(IDBConstants.INCOTERMS, incoterms);
							
							if(!_MasterDAO.isExisitINCOTERMMST(htMaster, ""))
							{
								htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
								htMaster.put(IDBConstants.CREATED_BY, user_id);
								_MasterDAO.InsertINCOTERMS(htMaster);
								
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", "ADD_INCOTERMS_MST");
								htRecvHis.put("ORDNUM","");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS",incoterms);
								htRecvHis.put(IDBConstants.CREATED_BY, user_id);
								htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								masterHisFlag = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}
					}
						
					/* Detail Data here... */
						List listQry = new ArrayList();
				    	listQry = (List) request.getSession().getAttribute("EST_LIST");
				    	
				    	for(int i=0; i<index.length; i++) {
				     		Map m=(Map)listQry.get(Integer.parseInt(index[i]));  
				     		item = StrUtils.fString((String)m.get("PRODUCT"));
				     		Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant,item);
				            String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
							String nonstocktypeDesc= StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
							itemDesc =StrUtils.fString((String) mPrddet.get("ITEMDESC"));	
							
							Hashtable htPodet = new Hashtable();
							Hashtable htShiphis = new Hashtable();
							htPodet.put(IDBConstants.PLANT, plant);
							htPodet.put(IDBConstants.ESTHDR_ESTNUM, estno);
						    htPodet.put(IDBConstants.UNITMO,uom[i]);
							htPodet.put(IDBConstants.DODET_ITEM, item);
							htPodet.put("ITEMDESC", strUtils.InsertQuotes(itemDesc));
							
							htPodet.put(IDBConstants.DODET_QTYOR, orderQty[i]);
							htPodet.put(IDBConstants.DODET_UNITPRICE, unitPrice[i]);
							htPodet.put(IDBConstants.ESTHDR_ESTLNNUM, Integer.toString(i+1));
							String CURRENCYUSEQT = new ESTUtil().getCurrencyUseQT(plant, estno);
							htPodet.put(IDBConstants.CURRENCYUSEQT, CURRENCYUSEQT);
							htPodet.put(IDBConstants.PODET_PRODUCTDELIVERYDATE, "");
							if(nonstocktype.equals("Y"))	
						    {
						    	if(nonstocktypeDesc.equalsIgnoreCase("discount")||nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")){ 
						    		htPodet.put(IDBConstants.DODET_UNITPRICE, "-"+unitPrice[i]);
						    	}
						    }
							htPodet.put("STATUS", "N");
							java.util.Date dt = new java.util.Date();
							SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
							String today = dfVisualDate.format(dt);
							htPodet.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
							taxby=_PlantMstDAO.getTaxBy(plant);
							if(taxby.equalsIgnoreCase("BYPRODUCT"))
							{
								prodgst=_ItemMstDAO.getProductGst(plant, item);
								htPodet.put(IDBConstants.PRODGST, prodgst);
							}
							int numberOfDecimal = Integer.parseInt(_PlantMstDAO.getNumberOfDecimal(plant));
							String estHdrQuery = " currencyid,isnull(outbound_Gst,0) as outbound_Gst ";
							Hashtable htEstHdr = new Hashtable();
							htEstHdr.put("ESTNO", estno);
							htEstHdr.put("PLANT", plant);
							
							ArrayList arrayEstHdr = new ArrayList();
							arrayEstHdr = EstUtil.getestHdrDetails(estHdrQuery, htEstHdr);
							Map dataMap = (Map) arrayEstHdr.get(0);
							String gstValue = dataMap.get("outbound_Gst").toString();
						    Float gstpercentage =  Float.parseFloat(((String) gstValue));
						    
						    double dOrderPrice = Double.parseDouble(unitPrice[i]) * Double.parseDouble(orderQty[i]);
						    dOrderPrice = StrUtils.RoundDB(dOrderPrice, numberOfDecimal);
						    double tax = (dOrderPrice*gstpercentage)/100;
						    tax = StrUtils.RoundDB(tax, numberOfDecimal);
						    double orderAmount = dOrderPrice+tax;
						    orderAmount = StrUtils.RoundDB(orderAmount,numberOfDecimal);
						    
						    Map resultMap = getOutstandingAmountForCustomer(custCode, orderAmount, plant);
							boolean isAmntExceed = (boolean) resultMap.get("STATUS");
							String amntExceedMsg = (String) resultMap.get("MSG");
							
							EstUtil.setmLogger(mLogger);
							if(!isAmntExceed) {
								flag = EstUtil.saveestDetDetails(htPodet);
							}
							
							if(flag){
								
								Hashtable htRemarks = new Hashtable();
								htRemarks.put(IDBConstants.PLANT, plant);
								htRemarks.put(IDBConstants.ESTHDR_ESTNUM, estno);
								htRemarks.put(IDBConstants.ESTHDR_ESTLNNUM, Integer.toString(i+1));
								htRemarks.put(IDBConstants.DODET_ITEM, item);
								htRemarks.put(IDBConstants.REMARKS,strUtils.InsertQuotes(""));
								htRemarks.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
								htRemarks.put(IDBConstants.CREATED_BY,user_id);
								flag = EstUtil.saveEstimateoMultiRemarks(htRemarks);
								
							}
							
							if (flag) {
								
								Hashtable htMovhis = new Hashtable();
								htMovhis.clear();
								htMovhis.put(IDBConstants.PLANT, plant);
								htMovhis.put("DIRTYPE", TransactionConstants.EST_ADD_ITEM);
								htMovhis.put(IDBConstants.CUSTOMER_CODE, custCode);
								htMovhis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
								htMovhis.put(IDBConstants.ITEM, item);
								htMovhis.put(IDBConstants.QTY, orderQty[i]);
								htMovhis.put(IDBConstants.MOVHIS_ORDNUM, estno);
								htMovhis.put(IDBConstants.CREATED_BY, user_id);
								htMovhis.put("MOVTID", "");
								htMovhis.put("RECID", "");
								htMovhis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
								htMovhis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
								
								flag = movHisDao.insertIntoMovHis(htMovhis);

							}
				    	}
					/* */
			    	if(flag) {/* Remove Items from Estimate list */
			    		for(int i=0; i<index.length; i++) {
			     		   if(i==0)
			     			   listQry.remove(Integer.parseInt(index[i]));
			     		   else
			     			   listQry.remove(Integer.parseInt(index[i-i]));
			     	   }
			    	}		
				    
				    if(rflag.equals("1") || rflag.equals("3") ){
				        ordertypeString="Sales Estimate Order";
				    }
					if (flag) {
						DbBean.CommitTran(ut);
						result = "<font color=\"green\"> "+ordertypeString+" Created Successfully.  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/EstimateServlet?ESTNO="
								+ estno + "&Submit=View'\"> ";
						result = " "  + "<br><h3>" + result
								+ "</h3>";
						
						 new TblControlUtil().updateTblControlSaleEstiamateAndRentalServiceSeqNo(plant,IConstants.ESTIMATE,"SE",estno);
						
						String isAutoEmail = emailDao.getIsAutoEmailDetails(plant, IConstants.ESTIMATE_ORDER);
						if(isAutoEmail.equalsIgnoreCase("Y"))
						mailUtil.sendEmail(plant,estno,IConstants.ESTIMATE_ORDER);

					} else {
						DbBean.RollbackTran(ut);
						result = "<font color=\"red\"> Error in Creating "+ordertypeString+"  - Please Check the Data  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
						result = " "+ordertypeString+" <br><h3>" + result+"</h3>";

					}
				} else {
					DbBean.RollbackTran(ut);
					result = "<font color=\"red\"> Enter Shipping Customer <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = " "+ordertypeString+ "<br><h3>" + result+"</h3>";
				}
				
			} else {
				DbBean.RollbackTran(ut);
				result = "<font color=\"red\"> Enter Valid Customer <br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = " "+ordertypeString+ "<br><h3>" + result+"</h3>";
			}

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			result = "<font class = " + IConstants.FAILED_COLOR + ">Error : " + ThrowableUtil.getMessage(e)
					+ "</font>";
			result = result + " <br><br><center> "
					+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
			result = "<br><h3>" + result;
			request.getSession().setAttribute("RESULT", result);
			/*response.sendRedirect("jsp/displayResult2User.jsp");*/
			
		}

		return result;
	}
	
	private JSONObject getOrderNoForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        DOUtil itemUtil = new DOUtil();
        StrUtils strUtils = new StrUtils();
        ESTUtil _ESTUtil = new ESTUtil();
        itemUtil.setmLogger(mLogger);
        _ESTUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String estno = StrUtils.fString(request.getParameter("ESTNO")).trim();
		    String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);
		     if(estno.length()>0) extCond=" AND plant='"+plant+"' and estno like '"+estno+"%'";
		     if(cname.length()>0) extCond=" AND CustName = '"+cname+"' ";
		     //extCond=extCond+" and STATUS <>'C'";
		     extCond=extCond+" ORDER BY CONVERT(date, CollectionDate, 103) desc";
		     ArrayList listQry = _ESTUtil.getestHdrDetails("estno,custName,custCode,jobNum,collectiondate",ht,extCond);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  estno = (String)m.get("estno");
				  String custName = strUtils.replaceCharacters2Send((String)m.get("custName"));
				  String custcode = (String)m.get("custcode");
				  String orderdate = (String)m.get("collectiondate");
				  String jobNum = (String)m.get("jobNum");
				  String status = (String)m.get("status");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("ESTNO", estno);
				  resultJsonInt.put("custName", custName);
				  resultJsonInt.put("custCode", custcode);
				  resultJsonInt.put("collectiondate", orderdate);
				  resultJsonInt.put("jobNum", jobNum);
				  				  
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("orders", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 jsonArray.add("");
                 resultJson.put("items", jsonArray);
                 resultJson.put("errors", jsonArrayErr);
		     }
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
}
