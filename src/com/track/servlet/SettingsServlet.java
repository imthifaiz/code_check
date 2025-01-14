package com.track.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.BillDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EstHdrDAO;
import com.track.dao.LoanHdrDAO;
import com.track.dao.OrderPaymentDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.ToHdrDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

/**
 * Servlet implementation class EditOrdersServlet
 */
/**
 * @author Bruhan
 * 
 */

public class SettingsServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;

	String action = "";

	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.SETTINGSSERVLET_PRINTPLANTMASTERLOG;

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			action = StrUtils.fString(request.getParameter("Submit")).trim();
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();

			if (action.equalsIgnoreCase("EDIT_PO_RCPT_HDR")) {
				PoHdrDAO poHdr = new PoHdrDAO();
				boolean poHdrUpt = false;
				String result = "";
				// Getting the values//
				String header1 = request.getParameter("InboundOrderHeader")
				.trim();
				String header2 = request.getParameter("InvoiceOrderToHeader")
				.trim();
				String header3 = request.getParameter("FromHeader").trim();
				String date = request.getParameter("Date").trim();
				String orderno = request.getParameter("OrderNo").trim();
				String refno = request.getParameter("RefNo").trim();
				String sono = request.getParameter("SoNo").trim();
				String item = request.getParameter("Item").trim();
				String description = request.getParameter("Description").trim();
				String orderqty = request.getParameter("OrderQty").trim();
				String uom = request.getParameter("UOM").trim();
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
			    String printSupRemarks = (request.getParameter("printSupRemarks") != null) ? "1": "0";
			    String printSupTerms = (request.getParameter("printSupTerms") != null) ? "1": "0"; 
			    String printwithproductremarks = (request.getParameter("printwithproductremarks") != null) ? "1": "0";
			    String printwithbrand = (request.getParameter("printwithbrand") != null) ? "1": "0";
   				String remark1 = request.getParameter("REMARK1").trim();
			    String remark2 = request.getParameter("REMARK2").trim();
			    String deliverydate = request.getParameter("DeliveryDate").trim();
			    String shipto = request.getParameter("ShipTo").trim(); 
    		    String companydate = request.getParameter("CompanyDate").trim();
			    String companyname = request.getParameter("CompanyName").trim();
			    String companystamp = request.getParameter("CompanyStamp").trim();
			    String companysig = request.getParameter("CompanySig").trim(); 
			    String printBarcode = (request.getParameter("printBarcode") != null) ? "1": "0";
			    String printRecmLoc = (request.getParameter("printRecmLoc") != null) ? "1": "0";
			    String Orientation = request.getParameter("Orientation").trim();
			    String orderdiscount = request.getParameter("OrderDiscount").trim();
			    String shippingcost = request.getParameter("ShippingCost").trim();
			    String incoterm = request.getParameter("INCOTERM").trim();
			    String rcbno = request.getParameter("RCBNO").trim();
			    String supplierrcbno = request.getParameter("SUPPLIERRCBNO").trim();
			    String uenno = request.getParameter("UENNO").trim();
			    String supplieruenno = request.getParameter("SUPPLIERUENNO").trim();
			    String PreparedBy = request.getParameter("PreparedBy").trim();
			    String AuthSignature = request.getParameter("AuthSignature").trim();
			    String termsdetails = request.getParameter("TermsDetails").trim();
			    
			    String prdDeliveryDate = request.getParameter("prdDeliveryDate").trim();
			    String printWithPrdDeliveryDate = (request.getParameter("printWithPrdDeliveryDate") != null) ? "1": "0";
			    String printWithDeliveryDate = (request.getParameter("printWithDeliveryDate") != null) ? "1": "0";
			    String ProductRatesAre = request.getParameter("ProductRatesAre").trim();
			    String grno = request.getParameter("GRNO").trim();
			    String grndate = request.getParameter("GRNDATE").trim();
			    
			    
//			    RESVI STARTS
			    String project = request.getParameter("Project").trim();
			    String printwithproject = (request.getParameter("printwithproject") != null) ? "1": "0";
			    String TransportMode = request.getParameter("TransportMode").trim();
			    String printwithtransportmode = (request.getParameter("printwithtransportmode") != null) ? "1": "0";
			    String printWithUENNO = (request.getParameter("printWithUENNO")) ;
//			    String printWithUENNO = (request.getParameter("printWithUENNO") != null) ? "1": "0";
			    String printWithSUPPLIERUENNO = (request.getParameter("printWithSupplierUENNO") != null) ? "1": "0";
			    String printwithcompanysig = (request.getParameter("printwithcompanysig") != null) ? "1": "0";
			    String printwithcompanyseal = (request.getParameter("printwithcompanyseal") != null) ? "1": "0";
			    String employee = request.getParameter("employee").trim();
			    String printWithemployee = (request.getParameter("printWithemployee") != null) ? "1": "0";
//			    RESVI ENDS   
			    
				Hashtable<String, String> ht = new Hashtable<String, String>();
				// Condition Parameters setting//
				ht.put(IDBConstants.PLANT, plant);

				StringBuffer QryUpdate = new StringBuffer(" SET ");
				// Query formation//
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
			    QryUpdate.append(", PSUPREMARKS ='" + printSupRemarks+ "' ");
			    QryUpdate.append(", PrintOrientation = '" + Orientation + "' ");
			    QryUpdate.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarks+ "' ");
			    QryUpdate.append(", PRINTWITHBRAND ='" + printwithbrand+ "' ");
			    QryUpdate.append(", PRINTSUPTERMS ='" + printSupTerms+ "' ");
			    QryUpdate.append(", PRODUCTRATESARE ='" + ProductRatesAre+ "' ");
		    	
			    if (supplierrcbno.length() > 0)
					QryUpdate.append(", SUPPLIERRCBNO = '" + supplierrcbno + "' ");
			    
			    if (rcbno.length() > 0)
					QryUpdate.append(", RCBNO = '" + rcbno + "' ");
			    
			    if (termsdetails.length() > 0)
			    	QryUpdate.append(", TERMSDETAILS = '" + termsdetails + "' ");
			    
			    if (uenno.length() > 0)
			    	QryUpdate.append(", UENNO = '" + uenno + "' ");
			    
			    if (supplieruenno.length() > 0)
					QryUpdate.append(", SUPPLIERUENNO = '" + supplieruenno + "' ");
			    
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
				QryUpdate.append(", PRINTBARCODE ='" + printBarcode+ "' ");
				
				QryUpdate.append(", PRINTRECMLOC ='" + printRecmLoc+ "' ");
				if (orderdiscount.length() > 0)
					QryUpdate.append(", ORDERDISCOUNT = '" + orderdiscount + "' ");
			    if (shippingcost.length() > 0)
					QryUpdate.append(", SHIPPINGCOST = '" + shippingcost + "' ");
			    if (incoterm.length() > 0)
					QryUpdate.append(", INCOTERM = '" + incoterm + "' ");
			    if (PreparedBy.length() > 0)
					QryUpdate.append(", PREPAREDBY = '" + PreparedBy + "' ");
			    if (AuthSignature.length() > 0)
					QryUpdate.append(", AUTHSIGNATURE = '" + AuthSignature + "' ");
			    
			    if (prdDeliveryDate.length() > 0)
					QryUpdate.append(", PRODUCTDELIVERYDATE = '" + prdDeliveryDate + "' ");	
			    QryUpdate.append(", PRINTWITHPRODUCTDELIVERYDATE ='" + printWithPrdDeliveryDate+ "' ");
			    QryUpdate.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDate+ "' ");
			    QryUpdate.append(", GRNO ='" + grno+ "' ");
			    QryUpdate.append(", GRNDATE ='" + grndate+ "' ");
			    
//			   RESVI STARTS
				if (project.length() > 0)
					QryUpdate.append(", PROJECT = '" + project + "' ");	
				
				if (TransportMode.length() > 0)
					QryUpdate.append(", TRANSPORT_MODE = '" + TransportMode + "' ");	
				
				if (employee.length() > 0)
					QryUpdate.append(", EMPLOYEE = '" + employee + "' ");	
				
				QryUpdate.append(", PRINTWITHPROJECT ='" + printwithproject+ "' ");
				QryUpdate.append(", PRINTWITHTRANSPORT_MODE ='" + printwithtransportmode+ "' ");
				QryUpdate.append(", PRINTWITHUENNO='" + printWithUENNO+ "' ");
				QryUpdate.append(", PRINTWITHSUPPLIERUENNO='" + printWithSUPPLIERUENNO+ "' ");
				QryUpdate.append(", PRINTWITHCOMPANYSIG='" + printwithcompanysig+ "' ");
				QryUpdate.append(", PRINTWITHCOMPANYSEAL='" + printwithcompanyseal+ "' ");
				QryUpdate.append(", PRINTEMPLOYEE='" + printWithemployee+ "' ");
				
//             RESVI ENDS
				
                try {
					poHdrUpt = poHdr.updatePOReciptHeader(QryUpdate.toString(),
							ht, " ");
				} catch (Exception e) {

				}
				System.out.println("action :: :" + poHdrUpt);
				if (!poHdrUpt) {

					result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Failed to edit the details</font>";
					response.sendRedirect("../edit/purchaseorderprintout?result="
							+ result);
				} else {
					result = "<font class = "
						+ IConstants.SUCCESS_COLOR
						+ ">Purchase Order Printout edited successfully</font>";

					response.sendRedirect("../edit/purchaseorderprintout?result="
							+ result);
				}

			} else if (action.equalsIgnoreCase("EDIT_PO_RCPT_INVOICE_HDR")) {
				PoHdrDAO poHdr = new PoHdrDAO();
				boolean poHdrUpt = false;
				String result = "";

				String header1 = request.getParameter("InboundOrderHeader")
				.trim();
				String header2 = request.getParameter("InvoiceOrderToHeader")
				.trim();
				String header3 = request.getParameter("FromHeader").trim();
				String date = request.getParameter("Date").trim();
				String orderno = request.getParameter("OrderNo").trim();
				String refno = request.getParameter("RefNo").trim();
				String terms = request.getParameter("Terms").trim();
				String termsdetails = request.getParameter("TermsDetails")
				.trim();
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
			    String printWithDeliveryDate = (request.getParameter("printWithDeliveryDate") != null) ? "1": "0";
			    String showPreviousPurchaseCost = (request.getParameter("showPreviousPurchaseCost") != null) ? "1": "0";
			    String calculateTaxwithShippingCost = (request.getParameter("calculateTaxwithShippingCost") != null) ? "1": "0";
			    String BillNo = request.getParameter("BILLNO").trim();
			    String BillDate = request.getParameter("BILLDATE").trim();
			    
			    String Adjustment = request.getParameter("Adjustment").trim();
			    String ProductRatesAre = request.getParameter("ProductRatesAre").trim();
			    
//			    RESVI STARTS
			    String project = request.getParameter("Project").trim();
			    String printwithproject = (request.getParameter("printwithproject") != null) ? "1": "0";
			    String printWithSupplierUENNO = (request.getParameter("printWithSupplierUENNO") != null) ? "1": "0";
			    String printWithUENNO = (request.getParameter("printWithUENNO") );
//			    String printWithUENNO = (request.getParameter("printWithUENNO") != null) ? "1": "0";
			    String printwithcompanysig = (request.getParameter("printwithcompanysig") != null) ? "1": "0";
			    String printwithcompanyseal = (request.getParameter("printwithcompanyseal") != null) ? "1": "0";
			    String TransportMode = request.getParameter("TransportMode").trim();
			    String printwithtransportmode = (request.getParameter("printwithtransportmode") != null) ? "1": "0";
			    String employee = request.getParameter("employee").trim();
			    String printWithemployee = (request.getParameter("printWithemployee") != null) ? "1": "0";
//			    RESVI ENDS   

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
					QryUpdate
					.append(", TERMSDETAILS = '" + termsdetails + "' ");
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
			    QryUpdate.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDate+ "' ");
			    QryUpdate.append(", SHOWPREVIOUSPURCHASECOST ='" + showPreviousPurchaseCost+ "' ");
			    QryUpdate.append(", CALCULATETAXWITHSHIPPINGCOST ='" + calculateTaxwithShippingCost+ "' ");
			    QryUpdate.append(", BILLNO ='" + BillNo+ "' ");
			    QryUpdate.append(", BILLDATE ='" + BillDate+ "' ");
			    QryUpdate.append(", ADJUSTMENT ='" + Adjustment+ "' ");
			    QryUpdate.append(", PRODUCTRATESARE ='" + ProductRatesAre+ "' ");
			    
//			    RESVI STARTS
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
//             RESVI ENDS
			
				try {
					poHdrUpt = poHdr.updatePOReciptInvoiceHeader(QryUpdate
							.toString(), ht, " ");
				} catch (Exception e) {

				}
				System.out.println("action :: :" + poHdrUpt);
				if (!poHdrUpt) {

					result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Failed to edit the details</font>";
					response
					.sendRedirect("../edit/purchaseorderprintoutwithcost?result="
							+ result);
				} else {
					result = "<font class = "
						+ IConstants.SUCCESS_COLOR
						+ ">Purchase Order With Cost Printout edited successfully</font>";
					response
					.sendRedirect("../edit/purchaseorderprintoutwithcost?result="
							+ result);
				}
			} 
				else if (action.equalsIgnoreCase("EDIT_BILL_HDR")) {
				BillDAO billDAO = new BillDAO();
				boolean billUpt = false;
				String result = "";

				String BillHeader = request.getParameter("BillHeader")
				.trim();
				String BillToHeader = request.getParameter("BillToHeader")
				.trim();
				String FromHeader = request.getParameter("FromHeader").trim();
				String OrderDate = request.getParameter("ORDERDATE").trim();
				String orderno = request.getParameter("OrderNo").trim();
				String refno = request.getParameter("RefNo").trim();
				String DueDate = request.getParameter("DueDate").trim();
				String termsdetails = request.getParameter("TermsDetails")
				.trim();
				String sono = request.getParameter("SoNo").trim();
				String item = request.getParameter("Item").trim();
				String description = request.getParameter("Description").trim();
				String BillQty = request.getParameter("BillQty").trim();
				String uom = request.getParameter("UOM").trim();
				String rate = request.getParameter("Rate").trim();
				String ProductDiscount = request.getParameter("ProductDiscount").trim();
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
				String ProductRatesAre = request.getParameter("ProductRatesAre").trim();
				String DisplayByOrdertype = (request.getParameter("DISPLAYBYORDERTYPE") != null) ? "1": "0";
				String printSupTerms = (request.getParameter("printSupTerms") != null) ? "1": "0";
				String TermsDetail = StrUtils.fString(request.getParameter("TermsDetail"));
				
				String PurchaseLocation = StrUtils.fString(request.getParameter("PurchaseLocation"));
				String printWithIncoterm = (request.getParameter("printWithIncoterm") != null) ? "1" : "0";
                String printDetailDesc = (request.getParameter("printDetailDesc") != null) ? "1": "0"; 
                String printWithPurchaseLocation = (request.getParameter("printWithPurchaseLocation") != null) ? "1": "0";
                String printWithUENNO = (request.getParameter("printWithUENNO"));
//                String printWithUENNO = (request.getParameter("printWithUENNO") != null) ? "1": "0";
                String printWithSupplierUENNO = (request.getParameter("printWithSupplierUENNO") != null) ? "1": "0";
                
                String rcbno = request.getParameter("RCBNO").trim();
                String remark1 = request.getParameter("REMARK1").trim();
			    //String deliverydate = request.getParameter("DeliveryDate").trim();
			   	String shipto = request.getParameter("ShipTo").trim();
			    String companydate = request.getParameter("CompanyDate").trim();
			    String companyname = request.getParameter("CompanyName").trim();
			    String companystamp = request.getParameter("CompanyStamp").trim();
			    String companysig = request.getParameter("CompanySig").trim(); 
				 
			    String discount = request.getParameter("Discount").trim();
				String Adjustment = request.getParameter("Adjustment").trim();
				String PaymentsMade = request.getParameter("PaymentsMade").trim();
			    String orderdiscount = request.getParameter("OrderDiscount").trim();
		        String shippingcost = request.getParameter("ShippingCost").trim();
			    String incoterm = request.getParameter("INCOTERM").trim();
			    String supplierrcbno = request.getParameter("SUPPLIERRCBNO").trim();
			    
			    String uenno = request.getParameter("UENNO").trim();
			    String supplieruenno = request.getParameter("SUPPLIERUENNO").trim();
			    
			    String grno = request.getParameter("GRNO").trim();
			    
			    
			    String PreparedBy = request.getParameter("PreparedBy").trim();
			    String AuthSignature = request.getParameter("AuthSignature").trim();
				
				String BalanceDue = request.getParameter("BalanceDue").trim();
			    
			    String BillNo = request.getParameter("BILLNO").trim();
			    String BillDate = request.getParameter("BILLDATE").trim();
			    String grndate = request.getParameter("GRNDATE").trim();
			    String printwithproductremarks = (request.getParameter("printwithproductremarks") != null) ? "1": "0";
			    String printwithbrand = (request.getParameter("printwithbrand") != null) ? "1": "0";
			    String printwithshipingadd = (request.getParameter("printwithshipingadd") != null) ? "1": "0";
			    
//			    RESVI STARTS
			    String project = request.getParameter("Project").trim();
			    String printwithproject = (request.getParameter("printwithproject") != null) ? "1": "0";
			    String printwithcompanysig = (request.getParameter("printwithcompanysig") != null) ? "1": "0";
			    String printwithcompanyseal = (request.getParameter("printwithcompanyseal") != null) ? "1": "0";
			    String TransportMode = request.getParameter("TransportMode").trim();
			    String printwithtransportmode = (request.getParameter("printwithtransportmode") != null) ? "1": "0";
			    
			    
			    String employee = request.getParameter("employee").trim();
			    String printWithemployee = (request.getParameter("printWithemployee") != null) ? "1": "0";
//			    RESVI ENDS   

				Hashtable<String, String> ht = new Hashtable<String, String>();
				ht.put(IDBConstants.PLANT, plant);
				StringBuffer QryUpdate = new StringBuffer(" SET ");

				if (BillHeader.length() > 0)
					QryUpdate.append(" BILLHEADER = '" + BillHeader + "' ");
				if (BillToHeader.length() > 0)
					QryUpdate.append(", TOHEADER = '" + BillToHeader + "' ");
				if (FromHeader.length() > 0)
					QryUpdate.append(", FROMHEADER = '" + FromHeader + "' ");
				if (shipto.length() > 0)
					QryUpdate.append(", SHIPTO = '" + shipto + "' ");
				QryUpdate.append(", BILLNO ='" + BillNo+ "' ");
			    QryUpdate.append(", BILLDATE ='" + BillDate+ "' ");
			    if (grno.length() > 0)
					QryUpdate.append(", GRNO = '" + grno + "' ");
			    if (OrderDate.length() > 0)
					QryUpdate.append(", DATE = '" + OrderDate + "' ");
			    if (orderno.length() > 0)
					QryUpdate.append(", ORDERNO = '" + orderno + "' ");
				if (refno.length() > 0)
					QryUpdate.append(", REFNO = '" + refno + "' ");
				if (rcbno.length() > 0)
					QryUpdate.append(", RCBNO = '" + rcbno + "' ");
			    if (supplierrcbno.length() > 0)
					QryUpdate.append(", SUPPLIERRCBNO = '" + supplierrcbno + "' ");
			    
			    if (uenno.length() > 0)
	  			    QryUpdate.append(", UENNO = '" + uenno + "' ");
			  			    
  			    if (supplieruenno.length() > 0)
  					QryUpdate.append(", SUPPLIERUENNO = '" + supplieruenno + "' ");
			    
				if (PreparedBy.length() > 0)
					QryUpdate.append(", PREPAREDBY = '" + PreparedBy + "' ");
				if (DueDate.length() > 0)
					QryUpdate.append(", DUEDATE = '" + DueDate + "' ");
				
				if (termsdetails.length() > 0)
					QryUpdate.append(", PAYMENTTERMS = '" + termsdetails + "' ");
				if (PurchaseLocation.length() > 0)
					QryUpdate.append(", PURCHASELOCATION = '" + PurchaseLocation + "' ");
			    QryUpdate.append(", PRINTPURCHASELOCATION ='" + printWithPurchaseLocation+ "' ");
			    if (incoterm.length() > 0)
					QryUpdate.append(", INCOTERM = '" + incoterm + "' ");
			    QryUpdate.append(", PRINTINCOTERM ='" + printWithIncoterm+ "' ");
			    
				if (sono.length() > 0)
					QryUpdate.append(", SONO = '" + sono + "' ");
				if (item.length() > 0)
					QryUpdate.append(", ITEM = '" + item + "' ");
				if (description.length() > 0)
					QryUpdate.append(", DESCRIPTION = '" + description + "' ");
				QryUpdate.append(", PRINTDETAILDESCRIPTION ='" + printDetailDesc+ "' ");
				if (BillQty.length() > 0)
					QryUpdate.append(", BILLQTY = '" + BillQty + "' ");
				if (uom.length() > 0)
					QryUpdate.append(", UOM = '" + uom + "' ");
				if (rate.length() > 0)
					QryUpdate.append(", RATE = '" + rate + "' ");
				if (ProductDiscount.length() > 0)
					QryUpdate.append(", ITEMDISCOUNT = '" + ProductDiscount + "' ");
				if (amt.length() > 0)
					QryUpdate.append(", ITEMAMOUNT = '" + amt + "' ");
				
				if (subtotal.length() > 0)
					QryUpdate.append(", SubTotal = '" + subtotal + "' ");
				if (totaltax.length() > 0)
					QryUpdate.append(", TAX = '" + totaltax + "' ");
				if (total.length() > 0)
					QryUpdate.append(", Total = '" + total + "' ");
				if (Adjustment.length() > 0)
					QryUpdate.append(", ADJUSTMENT = '" + Adjustment + "' ");
				if (discount.length() > 0)
					QryUpdate.append(", DISCOUNT = '" + discount + "' ");
				if (PaymentsMade.length() > 0)
					QryUpdate.append(", PAYMENTMADE = '" + PaymentsMade + "' ");
				if (orderdiscount.length() > 0)
					QryUpdate.append(", ORDERDISCOUNT = '" + orderdiscount + "' ");
				if (shippingcost.length() > 0)
					QryUpdate.append(", SHIPPINGCHARGE = '" + shippingcost + "' ");
				if (BalanceDue.length() > 0)
					QryUpdate.append(", BALANCEDUE = '" + BalanceDue + "' ");

				QryUpdate.append(", FOOTER1 = '" + Footer1 + "' ");
				QryUpdate.append(", FOOTER2 = '" + Footer2 + "' ");
				QryUpdate.append(", FOOTER3 = '" + Footer3 + "' ");
				QryUpdate.append(", FOOTER4 = '" + Footer4 + "' ");
				QryUpdate.append(", FOOTER5 = '" + Footer5 + "' ");
				QryUpdate.append(", FOOTER6 = '" + Footer6 + "' ");
				QryUpdate.append(", FOOTER7 = '" + Footer7 + "' ");
				QryUpdate.append(", FOOTER8 = '" + Footer8 + "' ");
				QryUpdate.append(", FOOTER9 = '" + Footer9 + "' ");
				QryUpdate.append(", DISPLAYBYORDERTYPE ='" + DisplayByOrdertype + "' ");
				QryUpdate.append(", PRINTSUPTERMS ='" + printSupTerms+ "' ");
				QryUpdate.append(", TERMSDETAILS = '" + TermsDetail + "' ");
				QryUpdate.append(", PRODUCTRATESARE ='" + ProductRatesAre+ "' ");
				QryUpdate.append(", GRNDATE ='" + grndate+ "' ");
				QryUpdate.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarks+ "' ");
				QryUpdate.append(", PRINTWITHBRAND ='" + printwithbrand+ "' ");
				QryUpdate.append(", PRINTWITHSHIPINGADD ='" + printwithshipingadd+ "' ");
			    
				
			    
			    
			    
//			    if (remark1.length() > 0)
					QryUpdate.append(", NOTES = '" + remark1 + "' ");
			    
			    
			if (companydate.length() > 0)
					QryUpdate.append(", COMPANYDATE = '" + companydate + "' ");
				if (companyname.length() > 0)
					QryUpdate.append(", COMPANYNAME = '" + companyname + "' ");
				if (companystamp.length() > 0)
					QryUpdate.append(", COMPANYSTAMP = '" + companystamp + "' ");
				if (companysig.length() > 0)
					QryUpdate.append(", COMPANYSIG = '" + companysig + "' ");
 				
 				
				
			    if (AuthSignature.length() > 0)
					QryUpdate.append(", AUTHSIGNATURE = '" + AuthSignature + "' ");
			    
//			    RESVI STARTS
				if (project.length() > 0)
					QryUpdate.append(", PROJECT = '" + project + "' ");	

				if (TransportMode.length() > 0)
					QryUpdate.append(", TRANSPORT_MODE = '" + TransportMode + "' ");	
				
				if (employee.length() > 0)
					QryUpdate.append(", EMPLOYEE = '" + employee + "' ");	
				
				QryUpdate.append(", PRINTWITHTRANSPORT_MODE ='" + printwithtransportmode+ "' ");
				QryUpdate.append(", PRINTWITHPROJECT ='" + printwithproject+ "' ");
				QryUpdate.append(", PRINTWITHUENNO ='" + printWithUENNO+ "' ");
				QryUpdate.append(", PRINTWITHSUPPLIERUENNO ='" + printWithSupplierUENNO+ "' ");
				QryUpdate.append(", PRINTWITHCOMPANYSIG='" + printwithcompanysig+ "' ");
				QryUpdate.append(", PRINTWITHCOMPANYSEAL='" + printwithcompanyseal+ "' ");
				QryUpdate.append(", PRINTEMPLOYEE='" + printWithemployee+ "' ");
				
//             RESVI ENDS
					
				
			    
				 
				try {
					billUpt = billDAO.updateBillHeader(QryUpdate
							.toString(), ht, " ");
				} catch (Exception e) {

				}
				System.out.println("action :: :" + billUpt);
				if (!billUpt) {

					result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Failed to edit the details</font>";
					response
					.sendRedirect("../edit/billprintout?result="
							+ result);
				} else {
					result = "<font class = "
						+ IConstants.SUCCESS_COLOR
						+ ">Bill Printout edited successfully</font>";
					response
					.sendRedirect("../edit/billprintout?result="
							+ result);
				}
	
				
			} else if (action.equalsIgnoreCase("EDIT_DO_RCPT_HDR")) {

				DoHdrDAO dhd = new DoHdrDAO();
				boolean poHdrUpt = false;
				String result = "";
				String Ordertype = "",OrdertypeDO="";
				HttpSession session = request.getSession();
				String PLANT = (String) session.getAttribute("PLANT");
				String sUserId = (String) session.getAttribute("LOGIN_USER");

				String OutboundOrderHeader = StrUtils.fString(request.getParameter("OutboundOrderHeader"));
				String InvoiceOrderToHeader = StrUtils.fString(request.getParameter("InvoiceOrderToHeader"));
				String FromHeader = StrUtils.fString(request.getParameter("FromHeader"));
				String DateField = StrUtils.fString(request.getParameter("Date"));
				String OrderNo = StrUtils.fString(request.getParameter("OrderNo"));
				Ordertype = StrUtils.fString(request.getParameter("OrderType"));
				String RefNo = StrUtils.fString(request.getParameter("RefNo"));
				String SoNo = StrUtils.fString(request.getParameter("SoNo"));
				String Item = StrUtils.fString(request.getParameter("Item"));
				String Description = StrUtils.fString(request.getParameter("Description"));
				String OrderQty = StrUtils.fString(request.getParameter("OrderQty"));
				String UOM = StrUtils.fString(request.getParameter("UOM"));
				String Footer1 = StrUtils.fString(request.getParameter("Footer1"));
				String Footer2 = StrUtils.fString(request.getParameter("Footer2"));
				String Footer3 = StrUtils.fString(request.getParameter("Footer3"));
				String Footer4 = StrUtils.fString(request.getParameter("Footer4"));
				String Footer5 = StrUtils.fString(request.getParameter("Footer5"));
				String Footer6 = StrUtils.fString(request.getParameter("Footer6"));
				String Footer7 = StrUtils.fString(request.getParameter("Footer7"));
				String Footer8 = StrUtils.fString(request.getParameter("Footer8"));
				String Footer9 = StrUtils.fString(request.getParameter("Footer9"));
				String printCustTerms = (request.getParameter("printCustTerms") != null) ? "1": "0";
				String DisplayByOrdertype = (request.getParameter("DisplayByOrdertype") != null) ? "1": "0";
				
				String printincoterm = (request.getParameter("printincoterm") != null) ? "1": "0";
			    String printBarcode = (request.getParameter("printBarcode") != null) ? "1": "0";
                String printDetailDesc = (request.getParameter("printDetailDesc") != null) ? "1": "0";
			    String Container=StrUtils.fString(request.getParameter("Container"));
			    String DisplayContainer = (request.getParameter("DisplayContainer") != null) ? "1" : "0";
			    String printLocStock = (request.getParameter("printLocStock") != null) ? "1" : "0";
			    String printCustRemarks = (request.getParameter("printCustRemarks") != null) ? "1" : "0";
			    String ProductRatesAre = request.getParameter("ProductRatesAre").trim();
			    String remark1 = request.getParameter("REMARK1").trim();
			    String remark2 = request.getParameter("REMARK2").trim();
			    String deliverydate = request.getParameter("DeliveryDate").trim();
			    String employee= request.getParameter("Employee").trim();
			    String termsdetails= request.getParameter("TermsDetails").trim();
			    String shipto = request.getParameter("ShipTo").trim(); 
    		    String companydate = request.getParameter("CompanyDate").trim();
			    String companyname = request.getParameter("CompanyName").trim();
			    String companystamp = request.getParameter("CompanyStamp").trim();
			    String companysig = request.getParameter("CompanySig").trim(); 
			    String printEmployee = (request.getParameter("printEmployee") != null) ? "1" : "0";
			    String Orientation = StrUtils.fString(request.getParameter("Orientation"));
			    String orderdiscount = request.getParameter("OrderDiscount").trim();
		        String shippingcost = request.getParameter("ShippingCost").trim();
			    String incoterm = request.getParameter("INCOTERM").trim();
			    String printwithproductremarks = (request.getParameter("printwithproductremarks") != null) ? "1": "0";
			    String printwithhscode = (request.getParameter("printwithhscode") != null) ? "1": "0";
			    String printwithcoo = (request.getParameter("printwithcoo") != null) ? "1": "0";
			    String printwithremark1 = (request.getParameter("printwithremark1") != null) ? "1": "0";
			    String printwithremark2 = (request.getParameter("printwithremark2") != null) ? "1": "0";
			    String printwithbrand = (request.getParameter("printwithbrand") != null) ? "1": "0";
			   
			    String brand = request.getParameter("BRAND").trim();
			    String hscode = request.getParameter("HSCODE").trim();
			    String coo = request.getParameter("COO").trim();
			    String rcbno = request.getParameter("RCBNO").trim();
			    String customerrcbno = request.getParameter("CUSTOMERRCBNO").trim();
			    
			    String uenno = request.getParameter("UENNO").trim();
			    String customeruenno = request.getParameter("CUSTOMERUENNO").trim();
			    
			    String PreparedBy = StrUtils.fString(request.getParameter("PreparedBy"));
				String Seller = StrUtils.fString(request.getParameter("Seller"));
			    String SellerSignature = StrUtils.fString(request.getParameter("SellerSignature"));
			    String Buyer = StrUtils.fString(request.getParameter("Buyer"));
			    String BuyerSignature = StrUtils.fString(request.getParameter("BuyerSignature"));
			    
			    String prdDeliveryDate = request.getParameter("prdDeliveryDate").trim();
			    String printWithPrdDeliveryDate = (request.getParameter("printWithPrdDeliveryDate") != null) ? "1": "0";
			    String printWithDeliveryDate = (request.getParameter("printWithDeliveryDate") != null) ? "1": "0";
			    String printWithUENNO = (request.getParameter("printWithUENNO"));
//			    String printWithUENNO = (request.getParameter("printWithUENNO") != null) ? "1": "0";
			    String printWithCustomerUENNO = (request.getParameter("printWithCustomerUENNO") != null) ? "1": "0";
			    
				
//			    RESVI STARTS
			    String project = request.getParameter("Project").trim();
			    String printwithproject = (request.getParameter("printwithproject") != null) ? "1": "0";
			    String printwithcompanysig = (request.getParameter("printwithcompanysig") != null) ? "1": "0";
			    String printwithcompanyseal = (request.getParameter("printwithcompanyseal") != null) ? "1": "0";
			    String TransportMode = request.getParameter("TransportMode").trim();
			    String printwithtransportmode = (request.getParameter("printwithtransportmode") != null) ? "1": "0";
			    String printwithshipingadd = (request.getParameter("printwithshipingadd") != null) ? "1": "0";
			    
//			    RESVI ENDS  
			    
			    
			    Hashtable<String, String> ht = new Hashtable<String, String>();
				ht.put(IDBConstants.PLANT, PLANT);

				if (Ordertype.equalsIgnoreCase("Mobile Registration")) {
					ht.put(IDBConstants.ORDERTYPE, Ordertype);
				} else if (Ordertype.equalsIgnoreCase("Mobile Enquiry")) {
					ht.put(IDBConstants.ORDERTYPE, Ordertype);
				} else {
					ht.put(IDBConstants.ORDERTYPE, Ordertype);
				}

				StringBuffer QryUpdate = new StringBuffer(" SET ");

				if (OutboundOrderHeader.length() > 0)
					QryUpdate.append(" ORDERHEADER = '" + OutboundOrderHeader + "' ");
				if (InvoiceOrderToHeader.length() > 0)
					QryUpdate.append(", TOHEADER = '" + InvoiceOrderToHeader + "' ");
				if (FromHeader.length() > 0)
					QryUpdate.append(", FROMHEADER = '" + FromHeader + "' ");
				if (DateField.length() > 0)
					QryUpdate.append(", DATE = '" + DateField + "' ");
				if (OrderNo.length() > 0)
					QryUpdate.append(", ORDERNO = '" + OrderNo + "' ");
				if (RefNo.length() > 0)
					QryUpdate.append(", REFNO = '" + RefNo + "' ");
				if (SoNo.length() > 0)
					QryUpdate.append(", SONO = '" + SoNo + "' ");
				if (Item.length() > 0)
					QryUpdate.append(", ITEM = '" + Item + "' ");
				if (Description.length() > 0)
					QryUpdate.append(", DESCRIPTION = '" + Description + "' ");
				if (OrderQty.length() > 0)
					QryUpdate.append(", ORDERQTY = '" + OrderQty + "' ");
				if (UOM.length() > 0)
					QryUpdate.append(", UOM = '" + UOM + "' ");

				QryUpdate.append(", FOOTER1 = '" + Footer1 + "' ");
				QryUpdate.append(", FOOTER2 = '" + Footer2 + "' ");
				QryUpdate.append(", FOOTER3 = '" + Footer3 + "' ");
				QryUpdate.append(", FOOTER4 = '" + Footer4 + "' ");
				QryUpdate.append(", FOOTER5 = '" + Footer5 + "' ");
				QryUpdate.append(", FOOTER6 = '" + Footer6 + "' ");
				QryUpdate.append(", FOOTER7 = '" + Footer7 + "' ");
				QryUpdate.append(", FOOTER8 = '" + Footer8 + "' ");
				QryUpdate.append(", FOOTER9 = '" + Footer9 + "' ");
				QryUpdate.append(", PRINTCUSTERMS ='" + printCustTerms+ "' ");
				QryUpdate.append(", PRINTINCOTERM ='" + printincoterm+ "' ");

				QryUpdate.append(", DISPLAYBYORDERTYPE ='" + DisplayByOrdertype+ "' ");
				
				QryUpdate.append(", PRINTWITHBRAND ='" + printwithbrand+ "' ");
				QryUpdate.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarks+ "' ");
				QryUpdate.append(", PRITNWITHHSCODE ='" + printwithhscode+ "' ");
				QryUpdate.append(", PRITNWITHCOO ='" + printwithcoo+ "' ");
				QryUpdate.append(", PRINTWITHREMARK1 ='" + printwithremark1+ "' ");
				QryUpdate.append(", PRINTWITHREMARK2 ='" + printwithremark2+ "' ");
			    QryUpdate.append(", PRODUCTRATESARE ='" + ProductRatesAre+ "' ");
				
				QryUpdate.append(", PRINTBARCODE ='" + printBarcode+ "' ");
				QryUpdate.append(", PRINTXTRADETAILS ='" + printDetailDesc+ "' ");
				QryUpdate.append(", CONTAINER ='" + Container+ "' ");
				QryUpdate.append(", DISPLAYCONTAINER ='" + DisplayContainer+ "' ");
				QryUpdate.append(", PrintLocStock ='" + printLocStock+ "' ");
				QryUpdate.append(", PCUSREMARKS ='" + printCustRemarks+ "' ");
                QryUpdate.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
				QryUpdate.append(", UPBY = '" + sUserId + "' ");
				QryUpdate.append(", PrintOrientation = '" + Orientation + "' ");
				if (remark1.length() > 0)
					QryUpdate.append(", REMARK1 = '" + remark1 + "' ");
			    if (remark2.length() > 0)
					QryUpdate.append(", REMARK2 = '" + remark2 + "' ");
			    
			    if (brand.length() > 0)
					QryUpdate.append(", BRAND = '" + brand + "' ");
			    
			    if (hscode.length() > 0)
					QryUpdate.append(", HSCODE = '" + hscode + "' ");
			    
			    if (coo.length() > 0)
					QryUpdate.append(", COO = '" + coo + "' ");
			    
			    if (rcbno.length() > 0)
					QryUpdate.append(", RCBNO = '" + rcbno + "' ");
			    			    
			    if (customerrcbno.length() > 0)
					QryUpdate.append(", CUSTOMERRCBNO = '" + customerrcbno + "' ");
			    
			    if (uenno.length() > 0)
			    	QryUpdate.append(", UENNO = '" + uenno + "' ");
			    
			    if (customeruenno.length() > 0)
					QryUpdate.append(", CUSTOMERUENNO = '" + customeruenno + "' ");
			    
			    if (deliverydate.length() > 0)
					QryUpdate.append(", DELIVERYDATE = '" + deliverydate + "' ");	
			    if (employee.length() > 0)
					QryUpdate.append(", EMPLOYEE = '" + employee + "' ");		
			    if (termsdetails.length() > 0)
			    	QryUpdate.append(", TERMSDETAILS = '" + termsdetails + "' ");		
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
				if (printEmployee.length() > 0)
					QryUpdate.append(", PRINTEMPLOYEE = '" + printEmployee + "' ");
				if (orderdiscount.length() > 0)
					QryUpdate.append(", ORDERDISCOUNT = '" + orderdiscount + "' ");
				if (shippingcost.length() > 0)
					QryUpdate.append(", SHIPPINGCOST = '" + shippingcost + "' ");
				if (incoterm.length() > 0)
					QryUpdate.append(", INCOTERM = '" + incoterm + "' ");
				if (PreparedBy.length() > 0)
					QryUpdate.append(", PREPAREDBY = '" + PreparedBy + "' ");
				if (Seller.length() > 0)
					QryUpdate.append(", SELLER = '" + Seller + "' ");
				if (SellerSignature.length() > 0)
					QryUpdate.append(", SELLERSIGNATURE = '" + SellerSignature + "' ");
				if (Buyer.length() > 0)
					QryUpdate.append(", BUYER = '" + Buyer + "' ");
				if (BuyerSignature.length() > 0)
					QryUpdate.append(", BUYERSIGNATURE = '" + BuyerSignature + "' ");
				
				if (prdDeliveryDate.length() > 0)
					QryUpdate.append(", PRODUCTDELIVERYDATE = '" + prdDeliveryDate + "' ");	
				QryUpdate.append(", PRINTWITHPRODUCTDELIVERYDATE ='" + printWithPrdDeliveryDate+ "' ");
				QryUpdate.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDate+ "' ");
				
//				RESVI STARTS
					if (project.length() > 0)
						QryUpdate.append(", PROJECT = '" + project + "' ");	

					if (TransportMode.length() > 0)
						QryUpdate.append(", TRANSPORT_MODE = '" + TransportMode + "' ");
					
					QryUpdate.append(", PRINTWITHTRANSPORT_MODE ='" + printwithtransportmode+ "' ");
					QryUpdate.append(", PRINTWITHSHIPINGADD ='" + printwithshipingadd+ "' ");
					QryUpdate.append(", PRINTWITHPROJECT ='" + printwithproject+ "' ");
					QryUpdate.append(", PRINTWITHUENNO ='" + printWithUENNO+ "' ");
					QryUpdate.append(", PRINTWITHCUSTOMERUENNO ='" + printWithCustomerUENNO+ "' ");
					QryUpdate.append(", PRINTWITHCOMPANYSIG='" + printwithcompanysig+ "' ");
					QryUpdate.append(", PRINTWITHCOMPANYSEAL='" + printwithcompanyseal+ "' ");
//	             RESVI ENDS


				try {
					poHdrUpt = dhd.updateDOReciptHeader(QryUpdate.toString(),
							ht, "");
				} catch (Exception e) {

				}
					
				try {
					if (!poHdrUpt) {
                                        

						result = "<font class = " + IConstants.FAILED_COLOR
						+ ">Failed to edit the details</font>";
					    if (Ordertype.equalsIgnoreCase("Mobile Registration")) {
                                                response
                                                .sendRedirect("jsp/editMobileRegistrationPrint.jsp?result="
                                                                + result);
                                            }if (Ordertype.equalsIgnoreCase("Mobile Enquiry")) {
                                                response
                                                .sendRedirect("jsp/editMobileEnquiryPrint.jsp?result="
                                                                + result);
                                            }else{
						response.sendRedirect("../editsales/salesorderprintout?result="
								+ result);
                                            }
					} else {

						if (Ordertype.equalsIgnoreCase("Mobile Registration")) {
							result = "<font class = "
								+ IConstants.SUCCESS_COLOR
								+ ">Registration Order Printout edited successfully</font>";
							response
							.sendRedirect("jsp/editMobileRegistrationPrint.jsp?result="
									+ result);
						}
						if (Ordertype.equalsIgnoreCase("Mobile Enquiry")) {
							result = "<font class = "
								+ IConstants.SUCCESS_COLOR
								+ ">Enquiry Order Printout edited successfully</font>";
							response
							.sendRedirect("jsp/editMobileEnquiryPrint.jsp?result="
									+ result);
						} else {
							result = "<font class = "
								+ IConstants.SUCCESS_COLOR
								+ ">Sales Order Printout edited successfully</font>";
							response
							.sendRedirect("../editsales/salesorderprintout?result="
									+ result);
						}
					}
				} catch (Exception e) {

				}
			}
			else if (action.equalsIgnoreCase("EDIT_DO_RCPT_HDRDO")){
				//To update DO
				DoHdrDAO dhd = new DoHdrDAO();
				boolean poHdrUpt = false;
				String result = "";
				String Ordertype = "",OrdertypeDO="";
				HttpSession session = request.getSession();
				String PLANT = (String) session.getAttribute("PLANT");
				String sUserId = (String) session.getAttribute("LOGIN_USER");
				
					String OutboundOrderHeaderDO = StrUtils.fString(request.getParameter("OutboundOrderHeaderDO"));
					String InvoiceOrderToHeaderDO = StrUtils.fString(request.getParameter("InvoiceOrderToHeaderDO"));
					String FromHeaderDO = StrUtils.fString(request.getParameter("FromHeaderDO"));
					String DateFieldDO = StrUtils.fString(request.getParameter("DateDO"));
					String OrderNoDO = StrUtils.fString(request.getParameter("OrderNoDO"));
					OrdertypeDO = StrUtils.fString(request.getParameter("OrderTypeDO")); 
					String RefNoDO = StrUtils.fString(request.getParameter("RefNoDO"));
					String SoNoDO = StrUtils.fString(request.getParameter("SoNoDO"));
					String ItemDO = StrUtils.fString(request.getParameter("ItemDO"));
					String DescriptionDO = StrUtils.fString(request.getParameter("DescriptionDO"));
					String OrderQtyDO = StrUtils.fString(request.getParameter("OrderQtyDO"));
					String UOMDO = StrUtils.fString(request.getParameter("UOMDO"));
					String Footer1DO = StrUtils.fString(request.getParameter("Footer1DO"));
					String Footer2DO = StrUtils.fString(request.getParameter("Footer2DO"));
					String Footer3DO = StrUtils.fString(request.getParameter("Footer3DO"));
					String Footer4DO = StrUtils.fString(request.getParameter("Footer4DO"));
					String Footer5DO = StrUtils.fString(request.getParameter("Footer5DO"));
					String Footer6DO = StrUtils.fString(request.getParameter("Footer6DO"));
					String Footer7DO = StrUtils.fString(request.getParameter("Footer7DO"));
					String Footer8DO = StrUtils.fString(request.getParameter("Footer8DO"));
					String Footer9DO = StrUtils.fString(request.getParameter("Footer9DO"));
				    String ProductRatesAreDO = request.getParameter("ProductRatesAreDO").trim();
				    String printCustTermsDO = (request.getParameter("printCustTermsDO") != null) ? "1": "0";
					String DisplayByOrdertypeDO = (request.getParameter("DisplayByOrdertypeDO") != null) ? "1": "0";
					String printpackinglistDO = (request.getParameter("printpackinglistDO") != null) ? "1": "0";
					String printdeliverynoteDO = (request.getParameter("printdeliverynoteDO") != null) ? "1": "0";
					String printwithproductremarksDO = (request.getParameter("printwithproductremarksDO") != null) ? "1": "0";
					String printwithbrandDO = (request.getParameter("printwithbrandDO") != null) ? "1": "0";
					String printhscodeDO = (request.getParameter("printhscodeDO") != null) ? "1": "0";
					String printcooDO = (request.getParameter("printcooDO") != null) ? "1": "0";
					String printBarcodeDO = (request.getParameter("printBarcodeDO") != null) ? "1": "0";
	                String printDetailDescDO = (request.getParameter("printDetailDescDO") != null) ? "1": "0";
				    String ContainerDO=StrUtils.fString(request.getParameter("ContainerDO"));
				    String DisplayContainerDO = (request.getParameter("DisplayContainerDO") != null) ? "1" : "0";
				    String printLocStockDO = (request.getParameter("printLocStockDO") != null) ? "1" : "0";
				    String printCustRemarksDO = (request.getParameter("printCustRemarksDO") != null) ? "1" : "0";
				    String remark1DO = request.getParameter("REMARK1DO").trim();
				    String remark2DO = request.getParameter("REMARK2DO").trim();
				    String deliverydateDO = request.getParameter("DeliveryDateDO").trim();
				    String employeeDO= request.getParameter("EmployeeDO").trim();
				    String termsdetailsdo= request.getParameter("TermsDetailsDO").trim();
				    String shiptoDO = request.getParameter("ShipToDO").trim(); 
	    		    String companydateDO = request.getParameter("CompanyDateDO").trim();
				    String companynameDO = request.getParameter("CompanyNameDO").trim();
				    String companystampDO = request.getParameter("CompanyStampDO").trim();
				    String companysigDO = request.getParameter("CompanySigDO").trim(); 
				    String printEmployeeDO = (request.getParameter("printEmployeeDO") != null) ? "1" : "0";
//				    String DisplaySignature = (request.getParameter("DisplaySignature") != null) ? "1" : "0";
				    String DisplaySignature = StrUtils.fString(request.getParameter("DisplaySignature")); 
				    String OrientationDO = StrUtils.fString(request.getParameter("OrientationDO")); 
					String orderdiscountDO = request.getParameter("OrderDiscountDO").trim();
			        String shippingcostDO = request.getParameter("ShippingCostDO").trim();
				    String incotermDO = request.getParameter("INCOTERMDO").trim();
				    String printincotermDO = (request.getParameter("printincotermDO") != null) ? "1" : "0";
				    
				    String brandDO = request.getParameter("BRANDDO").trim();
				    String hscodeDO = request.getParameter("HSCODEDO").trim();
				    String cooDO = request.getParameter("COODO").trim();
				    String rcbnoDO = request.getParameter("RCBNODO").trim();
				    String customerrcbnoDO = request.getParameter("CUSTOMERRCBNODO").trim();
				    
				    String uennoDO = request.getParameter("UENNODO").trim();
				    String customeruennoDO = request.getParameter("CUSTOMERUENNODO").trim();
				    
				    String PreparedByDO = StrUtils.fString(request.getParameter("PreparedByDO"));
				    String SellerDO = StrUtils.fString(request.getParameter("SellerDO"));
				    String SellerSignatureDO = StrUtils.fString(request.getParameter("SellerSignatureDO"));
				    String BuyerDO = StrUtils.fString(request.getParameter("BuyerDO"));
				    String BuyerSignatureDO = StrUtils.fString(request.getParameter("BuyerSignatureDO"));
				    
				    String prdDeliveryDateDO = request.getParameter("prdDeliveryDateDO").trim();
				    String printWithPrdDeliveryDateDO = (request.getParameter("printWithPrdDeliveryDateDO") != null) ? "1": "0";
				    String printWithDeliveryDateDO = (request.getParameter("printWithDeliveryDateDO") != null) ? "1": "0";
				    
				    String GINODO = request.getParameter("GINODO").trim();
					String GINODateDO = request.getParameter("GINODATEDO").trim();
					String CAPTURESIGNATUREDO = (request.getParameter("CAPTURESIGNATUREDO") != null) ? "1": "0";
					
//				    RESVI STARTS
				    String projectDO = request.getParameter("ProjectDO").trim();
				    String printwithprojectDO = (request.getParameter("printwithprojectDO") != null) ? "1": "0";
				    String ISINVENTORYMINQTY = (request.getParameter("ISINVENTORYMINQTY") != null) ? "1": "0";
				    String printWithUENNODO = (request.getParameter("printWithUENNODO"));
//				    String printWithUENNODO = (request.getParameter("printWithUENNODO") != null) ? "1": "0";
				    String printWithCustomerUENNODO = (request.getParameter("printWithCustomerUENNODO") != null) ? "1": "0";
				    
				    String TransportModeDO = request.getParameter("TransportModeDO").trim();
				    String printwithtransportmodeDO = (request.getParameter("printwithtransportmodeDO") != null) ? "1": "0";
				    String printwithshipingaddDO = (request.getParameter("printwithshipingaddDO") != null) ? "1": "0";
//				    RESVI ENDS   
				    
				    Hashtable<String, String> htDO = new Hashtable<String, String>();
					htDO.put(IDBConstants.PLANT, PLANT);
	
					if (Ordertype.equalsIgnoreCase("Mobile Registration")) {
						htDO.put(IDBConstants.ORDERTYPE, OrdertypeDO);
					} else if (Ordertype.equalsIgnoreCase("Mobile Enquiry")) {
						htDO.put(IDBConstants.ORDERTYPE, OrdertypeDO);
					} else {
						htDO.put(IDBConstants.ORDERTYPE, OrdertypeDO);
					}
	
					StringBuffer QryUpdateDO = new StringBuffer(" SET ");
	
					if (OutboundOrderHeaderDO.length() > 0)
						QryUpdateDO.append(" ORDERHEADER = '" + OutboundOrderHeaderDO + "' ");
					if (InvoiceOrderToHeaderDO.length() > 0)
						QryUpdateDO.append(", TOHEADER = '" + InvoiceOrderToHeaderDO + "' ");
					if (FromHeaderDO.length() > 0)
						QryUpdateDO.append(", FROMHEADER = '" + FromHeaderDO + "' ");
					if (DateFieldDO.length() > 0)
						QryUpdateDO.append(", DATE = '" + DateFieldDO + "' ");
					if (OrderNoDO.length() > 0)
						QryUpdateDO.append(", ORDERNO = '" + OrderNoDO + "' ");
					if (RefNoDO.length() > 0)
						QryUpdateDO.append(", REFNO = '" + RefNoDO + "' ");
					if (SoNoDO.length() > 0)
						QryUpdateDO.append(", SONO = '" + SoNoDO + "' ");
					if (ItemDO.length() > 0)
						QryUpdateDO.append(", ITEM = '" + ItemDO + "' ");
					if (DescriptionDO.length() > 0)
						QryUpdateDO.append(", DESCRIPTION = '" + DescriptionDO + "' ");
					if (OrderQtyDO.length() > 0)
						QryUpdateDO.append(", ORDERQTY = '" + OrderQtyDO + "' ");
					if (UOMDO.length() > 0)
						QryUpdateDO.append(", UOM = '" + UOMDO + "' ");
	
					QryUpdateDO.append(", FOOTER1 = '" + Footer1DO + "' ");
					QryUpdateDO.append(", FOOTER2 = '" + Footer2DO + "' ");
					QryUpdateDO.append(", FOOTER3 = '" + Footer3DO + "' ");
					QryUpdateDO.append(", FOOTER4 = '" + Footer4DO + "' ");
					QryUpdateDO.append(", FOOTER5 = '" + Footer5DO + "' ");
					QryUpdateDO.append(", FOOTER6 = '" + Footer6DO + "' ");
					QryUpdateDO.append(", FOOTER7 = '" + Footer7DO + "' ");
					QryUpdateDO.append(", FOOTER8 = '" + Footer8DO + "' ");
					QryUpdateDO.append(", FOOTER9 = '" + Footer9DO + "' ");
					QryUpdateDO.append(", PRINTCUSTERMS ='" + printCustTermsDO+ "' ");
					QryUpdateDO.append(", DISPLAYBYORDERTYPE ='" + DisplayByOrdertypeDO+ "' ");
					QryUpdateDO.append(", PRINTPACKINGLIST ='" + printpackinglistDO+ "' ");
					QryUpdateDO.append(", PRINTDELIVERYNOTE ='" + printdeliverynoteDO+ "' ");
					QryUpdateDO.append(", PRINTWITHBRAND ='" + printwithbrandDO+ "' ");
				    QryUpdateDO.append(", PRODUCTRATESARE ='" + ProductRatesAreDO+ "' ");
					QryUpdateDO.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarksDO+ "' ");	
					QryUpdateDO.append(", PRITNWITHHSCODE ='" + printhscodeDO+ "' ");
					QryUpdateDO.append(", PRITNWITHCOO ='" + printcooDO+ "' ");						
					QryUpdateDO.append(", PRINTBARCODE ='" + printBarcodeDO+ "' ");
					QryUpdateDO.append(", PRINTXTRADETAILS ='" + printDetailDescDO+ "' ");
					QryUpdateDO.append(", CONTAINER ='" + ContainerDO+ "' ");
					QryUpdateDO.append(", DISPLAYCONTAINER ='" + DisplayContainerDO+ "' ");
					QryUpdateDO.append(", PrintLocStock ='" + printLocStockDO+ "' ");
					QryUpdateDO.append(", PCUSREMARKS ='" + printCustRemarksDO+ "' ");
					QryUpdateDO.append(", PRINTINCOTERM ='" + printincotermDO+ "' ");
	                QryUpdateDO.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
					QryUpdateDO.append(", UPBY = '" + sUserId + "' ");
					QryUpdateDO.append(", PrintOrientation = '" + OrientationDO + "' ");
					if (remark1DO.length() > 0)
						QryUpdateDO.append(", REMARK1 = '" + remark1DO + "' ");
				    if (remark2DO.length() > 0)
						QryUpdateDO.append(", REMARK2 = '" + remark2DO + "' ");
				    if (deliverydateDO.length() > 0)
						QryUpdateDO.append(", DELIVERYDATE = '" + deliverydateDO + "' ");	
				    if (employeeDO.length() > 0)
						QryUpdateDO.append(", EMPLOYEE = '" + employeeDO + "' ");		
				    if (termsdetailsdo.length() > 0)
				    	QryUpdateDO.append(", TERMSDETAILS = '" + termsdetailsdo + "' ");		
					if (shiptoDO.length() > 0)
						QryUpdateDO.append(", SHIPTO = '" + shiptoDO + "' ");	
					if (companydateDO.length() > 0)
						QryUpdateDO.append(", COMPANYDATE = '" + companydateDO + "' ");
					if (companynameDO.length() > 0)
						QryUpdateDO.append(", COMPANYNAME = '" + companynameDO + "' ");
					if (companystampDO.length() > 0)
						QryUpdateDO.append(", COMPANYSTAMP = '" + companystampDO + "' ");
					if (companysigDO.length() > 0)
						QryUpdateDO.append(", COMPANYSIG = '" + companysigDO + "' ");
					if (printEmployeeDO.length() > 0)
						QryUpdateDO.append(", PRINTEMPLOYEE = '" + printEmployeeDO + "' ");
					if (DisplaySignature.length() > 0)
						QryUpdateDO.append(", DISPLAYSIGNATURE = '" + DisplaySignature + "' ");
					if (orderdiscountDO.length() > 0)
						QryUpdateDO.append(", ORDERDISCOUNT = '" + orderdiscountDO + "' ");
					if (shippingcostDO.length() > 0)
						QryUpdateDO.append(", SHIPPINGCOST = '" + shippingcostDO + "' ");
					if (incotermDO.length() > 0)
						QryUpdateDO.append(", INCOTERM = '" + incotermDO + "' ");
					
					if (brandDO.length() > 0)
						QryUpdateDO.append(", BRAND = '" + brandDO + "' ");
					if (hscodeDO.length() > 0)
						QryUpdateDO.append(", HSCODE = '" + hscodeDO + "' ");
				    if (cooDO.length() > 0)
						QryUpdateDO.append(", COO = '" + cooDO + "' ");
				    if (rcbnoDO.length() > 0)
						QryUpdateDO.append(", RCBNO = '" + rcbnoDO + "' ");
				    
				    if (customerrcbnoDO.length() > 0)
						QryUpdateDO.append(", CUSTOMERRCBNO = '" + customerrcbnoDO + "' ");
				    
				    if (uennoDO.length() > 0)
				    	QryUpdateDO.append(", UENNO = '" + uennoDO + "' ");
				    
				    if (customeruennoDO.length() > 0)
						QryUpdateDO.append(", CUSTOMERUENNO = '" + customeruennoDO + "' ");
				    
				    if (PreparedByDO.length() > 0)
						QryUpdateDO.append(", PREPAREDBY = '" + PreparedByDO + "' ");
					if (SellerDO.length() > 0)
						QryUpdateDO.append(", SELLER = '" + SellerDO + "' ");
					if (SellerSignatureDO.length() > 0)
						QryUpdateDO.append(", SELLERSIGNATURE = '" + SellerSignatureDO + "' ");
					if (BuyerDO.length() > 0)
						QryUpdateDO.append(", BUYER = '" + BuyerDO + "' ");
					if (BuyerSignatureDO.length() > 0)
						QryUpdateDO.append(", BUYERSIGNATURE = '" + BuyerSignatureDO + "' ");
					if (prdDeliveryDateDO.length() > 0)
						QryUpdateDO.append(", PRODUCTDELIVERYDATE = '" + prdDeliveryDateDO + "' ");	
						QryUpdateDO.append(", PRINTWITHPRODUCTDELIVERYDATE ='" + printWithPrdDeliveryDateDO+ "' ");
						QryUpdateDO.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDateDO+ "' ");
						QryUpdateDO.append(", GINO ='" + GINODO+ "' ");
						QryUpdateDO.append(", GINODATE ='" + GINODateDO+ "' ");
						QryUpdateDO.append(", CAPTURESIGNATURE ='" + CAPTURESIGNATUREDO+ "' ");
						
//						RESVI STARTS
						if (projectDO.length() > 0)
							QryUpdateDO.append(", PROJECT = '" + projectDO + "' ");	
						

						if (TransportModeDO.length() > 0)
							QryUpdateDO.append(", TRANSPORT_MODE = '" + TransportModeDO + "' ");
						
						QryUpdateDO.append(", PRINTWITHTRANSPORT_MODE ='" + printwithtransportmodeDO+ "' ");
						QryUpdateDO.append(", PRINTWITHSHIPINGADD ='" + printwithshipingaddDO+ "' ");
						QryUpdateDO.append(", PRINTWITHPROJECT ='" + printwithprojectDO+ "' ");
						QryUpdateDO.append(", ISINVENTORYMINQTY ='" + ISINVENTORYMINQTY+ "' ");
						QryUpdateDO.append(", PRINTWITHUENNO ='" + printWithUENNODO+ "' ");
						QryUpdateDO.append(", PRINTWITHCUSTOMERUENNO ='" + printWithCustomerUENNODO+ "' ");
//		             RESVI ENDS

	
					try {
						poHdrUpt = dhd.updateDOReciptHeaderDO(QryUpdateDO.toString(),
								htDO, "");
					} catch (Exception e) {
	
					}
					try {
						if (!poHdrUpt) {
	                                        

							result = "<font class = " + IConstants.FAILED_COLOR
							+ ">Failed to edit the details</font>";
						    if (Ordertype.equalsIgnoreCase("Mobile Registration")) {
	                                                response
	                                                .sendRedirect("jsp/editMobileRegistrationPrint.jsp?result="
	                                                                + result);
	                                            }if (Ordertype.equalsIgnoreCase("Mobile Enquiry")) {
	                                                response
	                                                .sendRedirect("jsp/editMobileEnquiryPrint.jsp?result="
	                                                                + result);
	                                            }else{
							response.sendRedirect("../editsales/salesorderprintout?result="
									+ result);
	                                            }
						} else {

							if (Ordertype.equalsIgnoreCase("Mobile Registration")) {
								result = "<font class = "
									+ IConstants.SUCCESS_COLOR
									+ ">Registration Order Printout edited successfully</font>";
								response
								.sendRedirect("jsp/editMobileRegistrationPrint.jsp?result="
										+ result);
							}
							if (Ordertype.equalsIgnoreCase("Mobile Enquiry")) {
								result = "<font class = "
									+ IConstants.SUCCESS_COLOR
									+ ">Enquiry Order Printout edited successfully</font>";
								response
								.sendRedirect("jsp/editMobileEnquiryPrint.jsp?result="
										+ result);
							} else {
								result = "<font class = "
									+ IConstants.SUCCESS_COLOR
									+ ">Sales Order Printout edited successfully</font>";
								response
								.sendRedirect("../editsales/salesorderprintout?result="
										+ result);
							}
						}
					} catch (Exception e) {

					}
			}
			else if (action.equalsIgnoreCase("EDIT_DO_RCPT_INVOICE_HDR_OTHERLANGUAGE")) {
				DoHdrDAO dhd = new DoHdrDAO();
				boolean poHdrUpt = false;
				String result = "";
				
				HttpSession session = request.getSession();
				String PLANT = (String) session.getAttribute("PLANT");
				String sUserId = (String) session.getAttribute("LOGIN_USER");
				//String Ordertype = StrUtils.fString(request.getParameter("OrderType"));
				String OutboundOrderHeader = StrUtils.fString(request.getParameter("OutboundOrderHeader"));
				String InvoiceOrderToHeader = StrUtils.fString(request.getParameter("InvoiceOrderToHeader"));
				String FromHeader = StrUtils.fString(request.getParameter("FromHeader"));
				String DateField = StrUtils.fString(request.getParameter("Date"));
				String OrderNo = StrUtils.fString(request.getParameter("OrderNo"));
				String RefNo = StrUtils.fString(request.getParameter("RefNo"));
				String Terms = StrUtils.fString(request.getParameter("Terms"));
				String TermsDetails = StrUtils.fString(request.getParameter("TermsDetails"));
				String SoNo = StrUtils.fString(request.getParameter("SoNo"));
				String Item = StrUtils.fString(request.getParameter("Item"));
				String Description = StrUtils.fString(request.getParameter("Description"));
				String OrderQty = StrUtils.fString(request.getParameter("OrderQty"));
				String UOM = StrUtils.fString(request.getParameter("UOM"));
				String Rate = StrUtils.fString(request.getParameter("Rate"));
				String TaxAmount = StrUtils.fString(request.getParameter("TaxAmount"));
				String Amt = StrUtils.fString(request.getParameter("Amt"));
				//String SubTotal = StrUtils.fString(request.getParameter("SubTotal"));
				//String TotalTax = StrUtils.fString(request.getParameter("TotalTax"));
				//String Total = StrUtils.fString(request.getParameter("Total"));
				String printdeliverynote = (request.getParameter("printdeliverynote") != null) ? "1": "0";
				String isAutoInvoice = (request.getParameter("isAutoInvoice") != null) ? "1": "0";
				String printpackinglist = (request.getParameter("printpackinglist") != null) ? "1": "0";
				String Footer1 = StrUtils.fString(request.getParameter("Footer1"));
				String Footer2 = StrUtils.fString(request.getParameter("Footer2"));
				String Footer3 = StrUtils.fString(request.getParameter("Footer3"));
				String Footer4 = StrUtils.fString(request.getParameter("Footer4"));
				String Footer5 = StrUtils.fString(request.getParameter("Footer5"));
				String Footer6 = StrUtils.fString(request.getParameter("Footer6"));
				String Footer7 = StrUtils.fString(request.getParameter("Footer7"));
				String Footer8 = StrUtils.fString(request.getParameter("Footer8"));
				String Footer9 = StrUtils.fString(request.getParameter("Footer9"));
				String Footer10 = StrUtils.fString(request.getParameter("Footer10"));
				String Footer11 = StrUtils.fString(request.getParameter("Footer11"));
				String Footer12 = StrUtils.fString(request.getParameter("Footer12"));
				String Footer13 = StrUtils.fString(request.getParameter("Footer13"));
				String DisplayByOrdertype = (request.getParameter("DisplayByOrdertype") != null) ? "1" : "0";
				String printwithbrand = (request.getParameter("printwithbrand") != null) ? "1" : "0";
				String printwithproductremarks = (request.getParameter("printwithproductremarks") != null) ? "1" : "0";
				String printwithhscode = (request.getParameter("printwithhscode") != null) ? "1" : "0";
				String printwithcoo = (request.getParameter("printwithcoo") != null) ? "1" : "0";
			    String printDetailDesc = (request.getParameter("printDetailDesc") != null) ? "1": "0";   
			    String printCustTerms = (request.getParameter("printCustTerms") != null) ? "1": "0";
			    String printCustRemarks = (request.getParameter("printCustRemarks") != null) ? "1": "0"; 
			    String printBalanceDue = (request.getParameter("printBalanceDue") != null) ? "1": "0"; 
				String Container=StrUtils.fString(request.getParameter("Container"));
			    String DisplayContainer = (request.getParameter("DisplayContainer") != null) ? "1": "0";
			    String remark1 = request.getParameter("REMARK1").trim();
			    String remark2 = request.getParameter("REMARK2").trim();
			    String deliverydate = request.getParameter("DeliveryDate").trim();
			    String employee= request.getParameter("Employee").trim();
			    String shipto = request.getParameter("ShipTo").trim(); 
    		    String companydate = request.getParameter("CompanyDate").trim();
			    String companyname = request.getParameter("CompanyName").trim();
			    String companystamp = request.getParameter("CompanyStamp").trim();
			    String companysig = request.getParameter("CompanySig").trim(); 
			    String printEmployee = (request.getParameter("printEmployee") != null) ? "1" : "0";
			    //String DisplaySignature = (request.getParameter("DisplaySignature") != null) ? "1" : "0";
			    String Orientation = StrUtils.fString(request.getParameter("Orientation"));
			    String orderdiscount = request.getParameter("OrderDiscount").trim();
		        String shippingcost = request.getParameter("ShippingCost").trim();
			    String incoterm = request.getParameter("INCOTERM").trim();	
			    String hscode = request.getParameter("HSCODE").trim();
			    String coo = request.getParameter("COO").trim();
			    String rcbno = request.getParameter("RCBNO").trim();
			    String invoiceno = request.getParameter("INVOICENO").trim();
			    String invoicedate = request.getParameter("INVOICEDATE").trim();
			    String customerrcbno = request.getParameter("CUSTOMERRCBNO").trim();
			    String ProductRatesAre = request.getParameter("ProductRatesAre").trim();
			    
			    String uenno = request.getParameter("UENNO").trim();
			    String customeruenno = request.getParameter("CUSTOMERUENNO").trim();
			    
			    String totalafterdiscount = request.getParameter("TOTALAFTERDISCOUNT").trim();
			    String brand = request.getParameter("BRAND").trim();
			    String cdiscount = request.getParameter("TOTALDISCOUNT").trim();
			    String ctax = request.getParameter("TAX").trim();
			    String PreparedBy = StrUtils.fString(request.getParameter("PreparedBy"));
			    String Seller = StrUtils.fString(request.getParameter("Seller"));
			    String SellerSignature = StrUtils.fString(request.getParameter("SellerSignature"));
			    String Buyer = StrUtils.fString(request.getParameter("Buyer"));
			    String BuyerSignature = StrUtils.fString(request.getParameter("BuyerSignature"));
			    String printwithTaxInvoice = (request.getParameter("printwithTaxInvoice") != null) ? "1": "0";
			    String Multilanguprint = (request.getParameter("Multilanguprint") != null) ? "1": "0";
				String SubTotal = StrUtils.fString(request.getParameter("SubTotal"));
				String Tax = StrUtils.fString(request.getParameter("Tax"));
				String Total = StrUtils.fString(request.getParameter("Total"));
				String ContactName = StrUtils.fString(request.getParameter("ContactName"));
				String Email = StrUtils.fString(request.getParameter("Email"));
				String Fax = StrUtils.fString(request.getParameter("Fax"));
				String Telephone = StrUtils.fString(request.getParameter("Telephone"));
				String Handphone = StrUtils.fString(request.getParameter("Handphone"));
				String Attention = StrUtils.fString(request.getParameter("Attention"));
				String QtyTotal = StrUtils.fString(request.getParameter("QtyTotal"));
				String remark3 = StrUtils.fString(request.getParameter("remark3"));
				String FooterPage = StrUtils.fString(request.getParameter("FooterPage"));
				String FooterOf = StrUtils.fString(request.getParameter("FooterOf"));
				String CashCustomer = StrUtils.fString(request.getParameter("CashCustomer"));
				String RoundoffTotalwithDecimal = StrUtils.fString(request.getParameter("RoundoffTotalwithDecimal"));
				String printRoundoffTotalwithDecimal = (request.getParameter("printRoundoffTotalwithDecimal") != null) ? "1": "0";
				String printwithProduct = (request.getParameter("printwithProduct") != null) ? "1": "0";
				String printDiscountReport = (request.getParameter("printDiscountReport") != null) ? "1": "0"; 
			    String Discount = request.getParameter("Discount").trim();
				String NetRate = request.getParameter("NetRate").trim();
				
				String prdDeliveryDate = request.getParameter("prdDeliveryDate").trim();
				String printWithPrdDeliveryDate = (request.getParameter("printWithPrdDeliveryDate") != null) ? "1": "0";
				String printWithDeliveryDate = (request.getParameter("printWithDeliveryDate") != null) ? "1": "0";
				
				String Adjustment = StrUtils.fString(request.getParameter("ADJUSTMENT"));
				String PaymentMade = StrUtils.fString(request.getParameter("PAYMENTMADE"));
				String BalanceDue = StrUtils.fString(request.getParameter("BALANCEDUE"));
				
				String Gino = StrUtils.fString(request.getParameter("GINO"));
				String GinoDate = StrUtils.fString(request.getParameter("GINODATE"));
				
//			    RESVI STARTS
			    String project = request.getParameter("Project").trim();
			    String printwithproject = (request.getParameter("printwithproject") != null) ? "1": "0";
			    String printWithUENNO = (request.getParameter("printWithUENNO"));
//			    String printWithUENNO = (request.getParameter("printWithUENNO") != null) ? "1": "0";
			    String printWithCustomerUENNO = (request.getParameter("printWithCustomerUENNO") != null) ? "1": "0";
			    String printwithcompanysig = (request.getParameter("printwithcompanysig") != null) ? "1": "0";
			    String printwithcustnameadrress = (request.getParameter("printwithcustnameadrress") != null) ? "1": "0";
			    String printwithcompanyseal = (request.getParameter("printwithcompanyseal") != null) ? "1": "0";
			    String TransportMode = request.getParameter("TransportMode").trim();
			    String printwithtransportmode = (request.getParameter("printwithtransportmode") != null) ? "1": "0";
			    String printwithshipingadd = (request.getParameter("printwithshipingadd") != null) ? "1": "0";
//			    RESVI ENDS  
			    
				
			    Hashtable<String, String> ht = new Hashtable<String, String>();
				ht.put(IDBConstants.PLANT, PLANT);
				ht.put(IDBConstants.ORDERTYPE, "Tax Invoice English");
				 /*if (Ordertype.equalsIgnoreCase("Tax Invoice English")) {
			            ht.put(IDBConstants.ORDERTYPE, Ordertype);
			    } else {
			            ht.put(IDBConstants.ORDERTYPE, Ordertype);
			    }*/
			    
				StringBuffer QryUpdate = new StringBuffer(" SET ");

				if (OutboundOrderHeader.length() > 0)
					QryUpdate.append(" ORDERHEADER = '" + OutboundOrderHeader
							+ "' ");
				if (InvoiceOrderToHeader.length() > 0)
					QryUpdate.append(", TOHEADER = '" + InvoiceOrderToHeader
							+ "' ");
				if (FromHeader.length() > 0)
					QryUpdate.append(", FROMHEADER = '" + FromHeader + "' ");
				if (DateField.length() > 0)
					QryUpdate.append(", DATE = '" + DateField + "' ");
				if (OrderNo.length() > 0)
					QryUpdate.append(", ORDERNO = '" + OrderNo + "' ");
				if (RefNo.length() > 0)
					QryUpdate.append(", REFNO = '" + RefNo + "' ");
				if (Terms.length() > 0)
					QryUpdate.append(", TERMS = '" + Terms + "' ");
				//if (TermsDetails.length() > 0)
					QryUpdate.append(", TERMSDETAILS = '" + TermsDetails + "' ");
				if (SoNo.length() > 0)
					QryUpdate.append(", SONO = '" + SoNo + "' ");
				if (Item.length() > 0)
					QryUpdate.append(", ITEM = '" + Item + "' ");
				if (Description.length() > 0)
					QryUpdate.append(", DESCRIPTION = '" + Description + "' ");
				if (OrderQty.length() > 0)
					QryUpdate.append(", ORDERQTY = '" + OrderQty + "' ");
				if (UOM.length() > 0)
					QryUpdate.append(", UOM = '" + UOM + "' ");
				if (Rate.length() > 0)
					QryUpdate.append(", RATE = '" + Rate + "' ");
				if (TaxAmount.length() > 0)
					QryUpdate.append(", TAXAMOUNT = '" + TaxAmount + "' ");
				if (Amt.length() > 0)
					QryUpdate.append(", AMT = '" + Amt + "' ");
				//if (SubTotal.length() > 0)
					//QryUpdate.append(", SubTotal = '" + SubTotal + "' ");
				//if (TotalTax.length() > 0)
					//QryUpdate.append(", TotalTax = '" + TotalTax + "' ");
				//if (Total.length() > 0)
					//QryUpdate.append(", Total = '" + Total + "' ");

				QryUpdate.append(", FOOTER1 = '" + Footer1 + "' ");
				QryUpdate.append(", FOOTER2 = '" + Footer2 + "' ");
				QryUpdate.append(", FOOTER3 = '" + Footer3 + "' ");
				QryUpdate.append(", FOOTER4 = '" + Footer4 + "' ");
				QryUpdate.append(", FOOTER5 = '" + Footer5 + "' ");
				QryUpdate.append(", FOOTER6 = '" + Footer6 + "' ");
				QryUpdate.append(", FOOTER7 = '" + Footer7 + "' ");
				QryUpdate.append(", FOOTER8 = '" + Footer8 + "' ");
				QryUpdate.append(", FOOTER9 = '" + Footer9 + "' ");
				QryUpdate.append(", FOOTER10 = '" + Footer10 + "' "+", FOOTER11 = '" + Footer11 + "' "+", FOOTER12 = '" + Footer12 + "' "+", FOOTER13 = '" + Footer13 + "' ");
//				QryUpdate.append(", FOOTER11 = '" + Footer11 + "' ");
//				QryUpdate.append(", FOOTER12 = '" + Footer12 + "' ");
//				QryUpdate.append(", FOOTER13 = '" + Footer13 + "' ");
				QryUpdate.append(", PRODUCTRATESARE ='" + ProductRatesAre+ "' ");
				QryUpdate.append(", DISPLAYBYORDERTYPE ='" + DisplayByOrdertype	+ "' ");
				QryUpdate.append(", PRINTWITHBRAND ='" + printwithbrand	+ "' ");
				QryUpdate.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarks	+ "' ");
				QryUpdate.append(", PRITNWITHHSCODE ='" + printwithhscode	+ "' ");
				QryUpdate.append(", PRITNWITHCOO ='" + printwithcoo	+ "' " + ", CONTAINER ='" + Container+ "' " + ", DISPLAYCONTAINER ='" + DisplayContainer+ "' " + ", PRINTXTRADETAILS ='" + printDetailDesc+ "' " + ", PRINTCUSTERMS ='" + printCustTerms+ "' " + ", PCUSREMARKS ='" + printCustRemarks+ "' ");
//				QryUpdate.append(", CONTAINER ='" + Container+ "' ");
//				QryUpdate.append(", DISPLAYCONTAINER ='" + DisplayContainer+ "' ");
//			    QryUpdate.append(", PRINTXTRADETAILS ='" + printDetailDesc+ "' ");
//			    QryUpdate.append(", PRINTCUSTERMS ='" + printCustTerms+ "' ");
//			    QryUpdate.append(", PCUSREMARKS ='" + printCustRemarks+ "' ");
			    QryUpdate.append(", PRINTBALANCEDUE ='" + printBalanceDue+ "' ");
			    QryUpdate.append(", PRINTDELIVERYNOTE ='" + printdeliverynote+ "' ");
			    QryUpdate.append(", PRINTPACKINGLIST ='" + printpackinglist+ "' ");
			    QryUpdate.append(", ISAUTOINVOICE ='" + isAutoInvoice+ "' ");
			   // QryUpdateDO.append(", PRINTDELIVERYNOTE ='" + printdeliverynoteDO	+ "' ");
				//QryUpdateDO.append(", PRINTPACKINGLIST ='" + printpackinglistDO	+ "' ");
			    
				QryUpdate.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
				QryUpdate.append(", UPBY = '" + sUserId + "' ");
				QryUpdate.append(", PrintOrientation = '" + Orientation+ "' ");
				if (remark1.length() > 0)
					QryUpdate.append(", REMARK1 = '" + remark1 + "' ");
			    if (remark2.length() > 0)
					QryUpdate.append(", REMARK2 = '" + remark2 + "' ");
			    
			    if (totalafterdiscount.length() > 0)
					QryUpdate.append(", TOTALAFTERDISCOUNT = '" + totalafterdiscount + "' ");
			    
			    if (brand.length() > 0)
					QryUpdate.append(", BRAND = '" + brand + "' ");
			    
			    if (cdiscount.length() > 0)
					QryUpdate.append(", TOTALDISCOUNT = '" + cdiscount + "' ");
			    
			    if (ctax.length() > 0)
					QryUpdate.append(", TAX = '" + ctax + "' ");
			    
			    if (rcbno.length() > 0)
					QryUpdate.append(", RCBNO = '" + rcbno + "' ");
			    if (hscode.length() > 0)
					QryUpdate.append(", HSCODE = '" + hscode + "' ");
			    if (coo.length() > 0)
					QryUpdate.append(", COO = '" + coo + "' ");
			    
			    if (customerrcbno.length() > 0)
					QryUpdate.append(", CUSTOMERRCBNO = '" + customerrcbno + "' ");
			    
			    if (uenno.length() > 0)
			    	QryUpdate.append(", UENNO = '" + uenno + "' ");
			    
			    if (customeruenno.length() > 0)
					QryUpdate.append(", CUSTOMERUENNO = '" + customeruenno + "' ");
			    
			    if (invoiceno.length() > 0)
					QryUpdate.append(", INVOICENO = '" + invoiceno + "' ");
			    
			    if (invoicedate.length() > 0)
					QryUpdate.append(", INVOICEDATE = '" + invoicedate + "' ");
			    
			    if (deliverydate.length() > 0)
					QryUpdate.append(", DELIVERYDATE = '" + deliverydate + "' ");	
			    if (employee.length() > 0)
					QryUpdate.append(", EMPLOYEE = '" + employee + "' ");		
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
				if (printEmployee.length() > 0)
					QryUpdate.append(", PRINTEMPLOYEE = '" + printEmployee + "' ");
				//if (DisplaySignature.length() > 0)
					//QryUpdate.append(", DISPLAYSIGNATURE = '" + DisplaySignature + "' ");
				if (orderdiscount.length() > 0)
					QryUpdate.append(", ORDERDISCOUNT = '" + orderdiscount + "' ");
				if (shippingcost.length() > 0)
					QryUpdate.append(", SHIPPINGCOST = '" + shippingcost + "' ");
				if (incoterm.length() > 0)
					QryUpdate.append(", INCOTERM = '" + incoterm + "' ");
				if (PreparedBy.length() > 0)
					QryUpdate.append(", PREPAREDBY = '" + PreparedBy + "' ");
				if (Seller.length() > 0)
					QryUpdate.append(", SELLER = '" + Seller + "' ");
				if (SellerSignature.length() > 0)
					QryUpdate.append(", SELLERSIGNATURE = '" + SellerSignature + "' ");
				if (Buyer.length() > 0)
					QryUpdate.append(", BUYER = '" + Buyer + "' ");
				if (BuyerSignature.length() > 0)
					QryUpdate.append(", BUYERSIGNATURE = '" + BuyerSignature + "' ");
				
				QryUpdate.append(", PRINTWITHTAXINVOICE ='" + printwithTaxInvoice	+ "' ");
				
				QryUpdate.append(", PRINTMULTILANG ='" + Multilanguprint	+ "' ");
				
				if (SubTotal.length() > 0)
					QryUpdate.append(", SUBTOTAL = '" + SubTotal + "' ");
				if (Tax.length() > 0)
					QryUpdate.append(", TOTALTAX = '" + Tax + "' ");
				if (Total.length() > 0)
					QryUpdate.append(", TOTAL = '" + Total + "' ");
				if (ContactName.length() > 0)
					QryUpdate.append(", CONTACTNAME = '" + ContactName + "' ");
				if (Email.length() > 0)
					QryUpdate.append(", EMAIL = '" + Email + "' ");
				if (Fax.length() > 0)
					QryUpdate.append(", FAX = '" + Fax + "' ");
				if (Telephone.length() > 0)
					QryUpdate.append(", TELEPHONE = '" + Telephone + "' ");
				if (Handphone.length() > 0)
					QryUpdate.append(", HANDPHONE = '" + Handphone + "' ");
				if (Attention.length() > 0)
					QryUpdate.append(", ATTENTION = '" + Attention + "' ");
				if (QtyTotal.length() > 0)
					QryUpdate.append(", QTYTOTAL = '" + QtyTotal + "' ");
				if (remark3.length() > 0)
					QryUpdate.append(", REMARK3 = '" + remark3 + "' ");
				if (FooterPage.length() > 0)
					QryUpdate.append(", FOOTERPAGE = '" + FooterPage + "' ");
				if (FooterOf.length() > 0)
					QryUpdate.append(", FOOTEROF = '" + FooterOf + "' ");
				if (CashCustomer.length() > 0)
					QryUpdate.append(", CASHCUSTOMER = '" + CashCustomer + "' ");
					
				if (RoundoffTotalwithDecimal.length() > 0)
					QryUpdate.append(", ROUNDOFFTOTALWITHDECIMAL = '" + RoundoffTotalwithDecimal + "' ");
				QryUpdate.append(", PRINTROUNDOFFTOTALWITHDECIMAL ='" + printRoundoffTotalwithDecimal+ "' ");
				QryUpdate.append(", PRINTWITHPRODUCT ='" + printwithProduct+ "' ");
				QryUpdate.append(", PRINTWITHDISCOUNT ='" + printDiscountReport+ "' ");
				if (Discount.length() > 0)
					QryUpdate.append(", DISCOUNT = '" + Discount + "' ");
				if (NetRate.length() > 0)
					QryUpdate.append(", NETRATE = '" + NetRate + "' ");
				
				if (prdDeliveryDate.length() > 0)
					QryUpdate.append(", PRODUCTDELIVERYDATE = '" + prdDeliveryDate + "' ");	
					QryUpdate.append(", PRINTWITHPRODUCTDELIVERYDATE ='" + printWithPrdDeliveryDate+ "' ");
					QryUpdate.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDate+ "' ");
				
				if (Adjustment.length() > 0)
					QryUpdate.append(", ADJUSTMENT = '" + Adjustment + "' ");
				
				if (PaymentMade.length() > 0)
					QryUpdate.append(", PAYMENTMADE = '" + PaymentMade + "' ");
				
				if (BalanceDue.length() > 0)
					QryUpdate.append(", BALANCEDUE = '" + BalanceDue + "' ");
				
				if (Gino.length() > 0)
					QryUpdate.append(", GINO = '" + Gino + "' ");
				
				if (GinoDate.length() > 0)
					QryUpdate.append(", GINODate = '" + GinoDate + "' ");
				
				
//				RESVI STARTS
					if (project.length() > 0)
						QryUpdate.append(", PROJECT = '" + project + "' ");	
					
					if (TransportMode.length() > 0)
						QryUpdate.append(", TRANSPORT_MODE = '" + TransportMode + "' ");	
					
					QryUpdate.append(", PRINTWITHTRANSPORT_MODE ='" + printwithtransportmode+ "' ");
					QryUpdate.append(", PRINTWITHSHIPINGADD ='" + printwithshipingadd+ "' ");
					QryUpdate.append(", PRINTWITHPROJECT ='" + printwithproject+ "' ");
					QryUpdate.append(", PRINTWITHUENNO ='" + printWithUENNO+ "' ");
					QryUpdate.append(", PRINTWITHCUSTOMERUENNO ='" + printWithCustomerUENNO+ "' ");
					QryUpdate.append(", PRINTWITHCOMPANYSIG='" + printwithcompanysig+ "' ");
					QryUpdate.append(", PRINTWITHCUSTNAMEADRRESS='" + printwithcustnameadrress+ "' ");
					QryUpdate.append(", PRINTWITHCOMPANYSEAL='" + printwithcompanyseal+ "' ");
//	             RESVI ENDS

				try {
					poHdrUpt = dhd.updateDOReciptInvoiceHeaderDO(QryUpdate.toString(), ht, "");
				} catch (Exception e) {

				}
				
				//To update DO
				if (poHdrUpt)
				{
				//String Ordertypedo = StrUtils.fString(request.getParameter("OrderType"));	
				String OutboundOrderHeaderDO = StrUtils.fString(request.getParameter("OutboundOrderHeaderDO"));
				String InvoiceOrderToHeaderDO = StrUtils.fString(request.getParameter("InvoiceOrderToHeaderDO"));
				String FromHeaderDO = StrUtils.fString(request.getParameter("FromHeaderDO"));
				String DateFieldDO = StrUtils.fString(request.getParameter("DateDO"));
				String OrderNoDO = StrUtils.fString(request.getParameter("OrderNoDO"));
				String RefNoDO = StrUtils.fString(request.getParameter("RefNoDO"));
				String TermsDO = StrUtils.fString(request.getParameter("TermsDO"));
				String TermsDetailsDO = StrUtils.fString(request.getParameter("TermsDetailsDO"));
				String SoNoDO = StrUtils.fString(request.getParameter("SoNoDO"));
				String ItemDO = StrUtils.fString(request.getParameter("ItemDO"));
				String DescriptionDO = StrUtils.fString(request.getParameter("DescriptionDO"));
				String OrderQtyDO = StrUtils.fString(request.getParameter("OrderQtyDO"));
				String UOMDO = StrUtils.fString(request.getParameter("UOMDO"));
				String RateDO = StrUtils.fString(request.getParameter("RateDO"));
				String TaxAmountDO = StrUtils.fString(request.getParameter("TaxAmountDO"));
				String AmtDO = StrUtils.fString(request.getParameter("AmtDO"));
				//String SubTotalDO = StrUtils.fString(request.getParameter("SubTotalDO"));
				//String TotalTaxDO = StrUtils.fString(request.getParameter("TotalTaxDO"));
				//String TotalDO = StrUtils.fString(request.getParameter("TotalDO"));
				String printdeliverynoteDO = (request.getParameter("printdeliverynoteDO") != null) ? "1": "0";
				String isAutoInvoiceDO = (request.getParameter("isAutoInvoiceDO") != null) ? "1": "0";
				String printpackinglistDO = (request.getParameter("printpackinglistDO") != null) ? "1": "0";
				String Footer1DO = StrUtils.fString(request.getParameter("Footer1DO"));
				String Footer2DO = StrUtils.fString(request.getParameter("Footer2DO"));
				String Footer3DO = StrUtils.fString(request.getParameter("Footer3DO"));
				String Footer4DO = StrUtils.fString(request.getParameter("Footer4DO"));
				String Footer5DO = StrUtils.fString(request.getParameter("Footer5DO"));
				String Footer6DO = StrUtils.fString(request.getParameter("Footer6DO"));
				String Footer7DO = StrUtils.fString(request.getParameter("Footer7DO"));
				String Footer8DO = StrUtils.fString(request.getParameter("Footer8DO"));
				String Footer9DO = StrUtils.fString(request.getParameter("Footer9DO"));
				String DisplayByOrdertypeDO = (request.getParameter("DisplayByOrdertypeDO") != null) ? "1" : "0";
				String printwithbrandDO = (request.getParameter("printwithbrandDO") != null) ? "1" : "0";
				String printwithproductremarksDO = (request.getParameter("printwithproductremarksDO") != null) ? "1" : "0";
				String printwithhscodeDO = (request.getParameter("printwithhscodeDO") != null) ? "1" : "0";
				String printwithcooDO = (request.getParameter("printwithcooDO") != null) ? "1" : "0";
			    String printDetailDescDO = (request.getParameter("printDetailDescDO") != null) ? "1": "0";   
			    String printCustTermsDO = (request.getParameter("printCustTermsDO") != null) ? "1": "0";
			    String printCustRemarksDO = (request.getParameter("printCustRemarksDO") != null) ? "1": "0"; 
				String ContainerDO=StrUtils.fString(request.getParameter("ContainerDO"));
			    String DisplayContainerDO = (request.getParameter("DisplayContainerDO") != null) ? "1": "0";
			    String remark1DO = StrUtils.fString(request.getParameter("REMARK1DO"));
			    String remark2DO = StrUtils.fString(request.getParameter("REMARK2DO"));
			    String deliverydateDO = StrUtils.fString(request.getParameter("DeliveryDateDO"));
			    String employeeDO= StrUtils.fString(request.getParameter("EmployeeDO"));
			    String shiptoDO = StrUtils.fString(request.getParameter("ShipToDO")); 
    		    String companydateDO = StrUtils.fString(request.getParameter("CompanyDateDO"));
			    String companynameDO = StrUtils.fString(request.getParameter("CompanyNameDO"));
			    String companystampDO = StrUtils.fString(request.getParameter("CompanyStampDO"));
			    String companysigDO = StrUtils.fString(request.getParameter("CompanySigDO")); 
			    String printEmployeeDO = (request.getParameter("printEmployeeDO") != null) ? "1" : "0";
			    //String DisplaySignatureDO = (request.getParameter("DisplaySignatureDO") != null) ? "1" : "0";
			    String OrientationDO = StrUtils.fString(request.getParameter("OrientationDO"));
			    String orderdiscountDO = StrUtils.fString(request.getParameter("OrderDiscountDO"));
		        String shippingcostDO = StrUtils.fString(request.getParameter("ShippingCostDO"));
			    String incotermDO = StrUtils.fString(request.getParameter("INCOTERMDO"));	
			    String hscodeDO = StrUtils.fString(request.getParameter("HSCODEDO"));
			    String cooDO = StrUtils.fString(request.getParameter("COODO"));
			    String rcbnoDO = StrUtils.fString(request.getParameter("RCBNODO"));
			    String invoicenoDO = StrUtils.fString(request.getParameter("INVOICENODO"));
			    String invoicedateDO = StrUtils.fString(request.getParameter("INVOICEDATEDO"));
			    String customerrcbnoDO = StrUtils.fString(request.getParameter("CUSTOMERRCBNODO"));
			    String totalafterdiscountDO = StrUtils.fString(request.getParameter("TOTALAFTERDISCOUNTDO"));
			    String brandDO = StrUtils.fString(request.getParameter("BRANDDO"));
			    String cdiscountDO = StrUtils.fString(request.getParameter("TOTALDISCOUNTDO"));
			    String ctaxDO = StrUtils.fString(request.getParameter("TAXDO"));
			    String PreparedByDO = StrUtils.fString(request.getParameter("PreparedByDO"));
			    String SellerDO = StrUtils.fString(request.getParameter("SellerDO"));
			    String SellerSignatureDO = StrUtils.fString(request.getParameter("SellerSignatureDO"));
			    String BuyerDO = StrUtils.fString(request.getParameter("BuyerDO"));
			    String BuyerSignatureDO = StrUtils.fString(request.getParameter("BuyerSignatureDO"));
			    String printwithTaxInvoiceDO = (request.getParameter("printwithTaxInvoiceDO") != null) ? "1": "0";	
				String SubTotalDO = StrUtils.fString(request.getParameter("SubTotalDO"));
				String TaxDO = StrUtils.fString(request.getParameter("TaxDO"));
				String TotalDO = StrUtils.fString(request.getParameter("TotalDO"));
				String ContactNameDO = StrUtils.fString(request.getParameter("ContactNameDO"));
				String EmailDO = StrUtils.fString(request.getParameter("EmailDO"));
				String FaxDO = StrUtils.fString(request.getParameter("FaxDO"));
				String TelephoneDO = StrUtils.fString(request.getParameter("TelephoneDO"));
				String HandphoneDO = StrUtils.fString(request.getParameter("HandphoneDO"));
				String AttentionDO = StrUtils.fString(request.getParameter("AttentionDO"));
				String QtyTotalDO = StrUtils.fString(request.getParameter("QtyTotalDO"));
				String remark3DO = StrUtils.fString(request.getParameter("remark3DO"));
				String FooterPageDO = StrUtils.fString(request.getParameter("FooterPageDO"));
				String FooterOfDO = StrUtils.fString(request.getParameter("FooterOfDO"));
				String CashCustomerDO = StrUtils.fString(request.getParameter("CashCustomerDO"));
				String RoundoffTotalwithDecimalDO = StrUtils.fString(request.getParameter("RoundoffTotalwithDecimalDO"));
				String printRoundoffTotalwithDecimalDO = (request.getParameter("printRoundoffTotalwithDecimalDO") != null) ? "1": "0";
				String printwithProductDO = (request.getParameter("printwithProductDO") != null) ? "1": "0";
				String printDiscountReportDO = (request.getParameter("printDiscountReportDO") != null) ? "1": "0"; 
			    String DiscountDO = StrUtils.fString(request.getParameter("DiscountDO"));
				String NetRateDO = StrUtils.fString(request.getParameter("NetRateDO"));
				
				String prdDeliveryDateDO = StrUtils.fString(request.getParameter("prdDeliveryDateDO"));
				String printWithPrdDeliveryDateDO = (request.getParameter("printWithPrdDeliveryDateDO") != null) ? "1": "0";
				String printWithDeliveryDateDO = (request.getParameter("printWithDeliveryDateDO") != null) ? "1": "0";
				String printwithcompanysigDO = (request.getParameter("printwithcompanysigDO") != null) ? "1": "0";
				String printwithcompanysealDO = (request.getParameter("printwithcompanysealDO") != null) ? "1": "0";
				
			    
			    Hashtable<String, String> htDO = new Hashtable<String, String>();
				htDO.put(IDBConstants.PLANT, PLANT);
				htDO.put(IDBConstants.ORDERTYPE, "Tax Invoice Other Languages");
				/*if (Ordertypedo.equalsIgnoreCase("Tax Invoice Other Languages")) {
		            htDO.put(IDBConstants.ORDERTYPE, Ordertypedo);
		    } else {
		            htDO.put(IDBConstants.ORDERTYPE, Ordertypedo);
		    }*/
				StringBuffer QryUpdateDO = new StringBuffer(" SET ");

				if (OutboundOrderHeaderDO.length() > 0)
					QryUpdateDO.append(" ORDERHEADER = N'" + OutboundOrderHeaderDO
							+ "' ");
				if (InvoiceOrderToHeaderDO.length() > 0)
					QryUpdateDO.append(", TOHEADER = N'" + InvoiceOrderToHeaderDO
							+ "' ");
				if (FromHeaderDO.length() > 0)
					QryUpdateDO.append(", FROMHEADER = N'" + FromHeaderDO + "' ");
				if (DateFieldDO.length() > 0)
					QryUpdateDO.append(", DATE = N'" + DateFieldDO + "' ");
				if (OrderNoDO.length() > 0)
					QryUpdateDO.append(", ORDERNO = N'" + OrderNoDO + "' ");
				if (RefNoDO.length() > 0)
					QryUpdateDO.append(", REFNO = N'" + RefNoDO + "' ");
				if (TermsDO.length() > 0)
					QryUpdateDO.append(", TERMS = N'" + TermsDO + "' ");
				//if (TermsDetails.length() > 0)
					QryUpdateDO.append(" TERMSDETAILS = N'" + TermsDetailsDO + "' ");
				if (SoNoDO.length() > 0)
					QryUpdateDO.append(", SONO = N'" + SoNoDO + "' ");
				if (ItemDO.length() > 0)
					QryUpdateDO.append(", ITEM = N'" + ItemDO + "' ");
				if (DescriptionDO.length() > 0)
					QryUpdateDO.append(", DESCRIPTION = N'" + DescriptionDO + "' ");
				if (OrderQtyDO.length() > 0)
					QryUpdateDO.append(", ORDERQTY = N'" + OrderQtyDO + "' ");
				if (UOMDO.length() > 0)
					QryUpdateDO.append(", UOM = N'" + UOMDO + "' ");
				if (RateDO.length() > 0)
					QryUpdateDO.append(", RATE = N'" + RateDO + "' ");
				if (TaxAmountDO.length() > 0)
					QryUpdateDO.append(", TAXAMOUNT = N'" + TaxAmountDO + "' ");
				if (AmtDO.length() > 0)
					QryUpdateDO.append(", AMT = N'" + AmtDO + "' ");
				//if (SubTotalDO.length() > 0)
					//QryUpdateDO.append(", SubTotal = N'" + SubTotalDO + "' ");
				//if (TotalTaxDO.length() > 0)
					//QryUpdateDO.append(", TotalTax = N'" + TotalTaxDO + "' ");
				//if (TotalDO.length() > 0)
					//QryUpdateDO.append(", Total = N'" + TotalDO + "' ");

				QryUpdateDO.append(", FOOTER1 = N'" + Footer1DO + "' ");
				QryUpdateDO.append(", FOOTER2 = N'" + Footer2DO + "' ");
				QryUpdateDO.append(", FOOTER3 = N'" + Footer3DO + "' ");
				QryUpdateDO.append(", FOOTER4 = N'" + Footer4DO + "' ");
				QryUpdateDO.append(", FOOTER5 = N'" + Footer5DO + "' ");
				QryUpdateDO.append(", FOOTER6 = N'" + Footer6DO + "' ");
				QryUpdateDO.append(", FOOTER7 = N'" + Footer7DO + "' ");
				QryUpdateDO.append(", FOOTER8 = N'" + Footer8DO + "' ");
				QryUpdateDO.append(", FOOTER9 = N'" + Footer9DO + "' ");
				QryUpdateDO.append(", DISPLAYBYORDERTYPE ='" + DisplayByOrdertypeDO	+ "' ");
				QryUpdateDO.append(", PRINTWITHBRAND ='" + printwithbrandDO	+ "' ");
				QryUpdateDO.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarksDO	+ "' ");
				QryUpdateDO.append(", PRITNWITHHSCODE ='" + printwithhscodeDO	+ "' ");
				QryUpdateDO.append(", PRITNWITHCOO ='" + printwithcooDO	+ "' ");
				QryUpdateDO.append(", CONTAINER = N'" + ContainerDO+ "' ");
				QryUpdateDO.append(", DISPLAYCONTAINER ='" + DisplayContainerDO+ "' ");
			    QryUpdateDO.append(", PRINTXTRADETAILS ='" + printDetailDescDO+ "' ");
			    QryUpdateDO.append(", PRINTCUSTERMS ='" + printCustTermsDO+ "' ");
			    QryUpdateDO.append(", PCUSREMARKS ='" + printCustRemarksDO+ "' ");
			    QryUpdateDO.append(", PRINTDELIVERYNOTE ='" + printdeliverynoteDO+ "' ");
			    QryUpdateDO.append(", PRINTPACKINGLIST ='" + printpackinglistDO+ "' ");
			    QryUpdateDO.append(", ISAUTOINVOICE ='" + isAutoInvoiceDO+ "' ");
			    QryUpdateDO.append(", PRINTWITHCOMPANYSIG ='" + printwithcompanysigDO+ "' ");
			    QryUpdateDO.append(", PRINTWITHCOMPANYSEAL ='" + printwithcompanysealDO+ "' ");
			    
			   // QryUpdateDO.append(", PRINTDELIVERYNOTE ='" + printdeliverynoteDO	+ "' ");
				//QryUpdateDO.append(", PRINTPACKINGLIST ='" + printpackinglistDO	+ "' ");
			    
				QryUpdateDO.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
				QryUpdateDO.append(", UPBY = '" + sUserId + "' ");
				QryUpdateDO.append(", PrintOrientation = '" + OrientationDO+ "' ");
				if (remark1DO.length() > 0)
					QryUpdateDO.append(", REMARK1 = N'" + remark1DO + "' ");
			    if (remark2DO.length() > 0)
					QryUpdateDO.append(", REMARK2 = N'" + remark2DO + "' ");
			    
			    if (totalafterdiscountDO.length() > 0)
					QryUpdateDO.append(", TOTALAFTERDISCOUNT = N'" + totalafterdiscountDO + "' ");
			    
			    if (brandDO.length() > 0)
					QryUpdateDO.append(", BRAND = N'" + brandDO + "' ");
			    
			    if (cdiscountDO.length() > 0)
					QryUpdateDO.append(", TOTALDISCOUNT = N'" + cdiscountDO + "' ");
			    
			    if (ctaxDO.length() > 0)
					QryUpdateDO.append(", TAX = N'" + ctaxDO + "' ");
			    
			    if (rcbnoDO.length() > 0)
					QryUpdateDO.append(", RCBNO = N'" + rcbnoDO + "' ");
			    if (hscodeDO.length() > 0)
					QryUpdateDO.append(", HSCODE = N'" + hscodeDO + "' ");
			    if (cooDO.length() > 0)
					QryUpdateDO.append(", COO = N'" + cooDO + "' ");
			    
			    if (customerrcbnoDO.length() > 0)
					QryUpdateDO.append(", CUSTOMERRCBNO = N'" + customerrcbnoDO + "' ");
			    if (invoicenoDO.length() > 0)
					QryUpdateDO.append(", INVOICENO = N'" + invoicenoDO + "' ");
			    
			    if (invoicedateDO.length() > 0)
					QryUpdateDO.append(", INVOICEDATE = N'" + invoicedateDO + "' ");
			    
			    if (deliverydateDO.length() > 0)
					QryUpdateDO.append(", DELIVERYDATE = N'" + deliverydateDO + "' ");	
			    if (employeeDO.length() > 0)
					QryUpdateDO.append(", EMPLOYEE = N'" + employeeDO + "' ");		
				if (shiptoDO.length() > 0)
					QryUpdateDO.append(", SHIPTO = N'" + shiptoDO + "' ");	
				if (companydateDO.length() > 0)
					QryUpdateDO.append(", COMPANYDATE = N'" + companydateDO + "' ");
				if (companynameDO.length() > 0)
					QryUpdateDO.append(", COMPANYNAME = N'" + companynameDO + "' ");
				if (companystampDO.length() > 0)
					QryUpdateDO.append(", COMPANYSTAMP = N'" + companystampDO + "' ");
				if (companysigDO.length() > 0)
					QryUpdateDO.append(", COMPANYSIG = N'" + companysigDO + "' ");
				if (printEmployeeDO.length() > 0)
					QryUpdateDO.append(", PRINTEMPLOYEE = '" + printEmployeeDO + "' ");
				//if (DisplaySignatureDO.length() > 0)
					//QryUpdateDO.append(", DISPLAYSIGNATURE = '" + DisplaySignatureDO + "' ");
				if (orderdiscountDO.length() > 0)
					QryUpdateDO.append(", ORDERDISCOUNT = N'" + orderdiscountDO + "' ");
				if (shippingcostDO.length() > 0)
					QryUpdateDO.append(", SHIPPINGCOST = N'" + shippingcostDO + "' ");
				if (incotermDO.length() > 0)
					QryUpdateDO.append(", INCOTERM = N'" + incotermDO + "' ");
				if (PreparedByDO.length() > 0)
					QryUpdateDO.append(", PREPAREDBY = N'" + PreparedByDO + "' ");
				if (SellerDO.length() > 0)
					QryUpdateDO.append(", SELLER = N'" + SellerDO + "' ");
				if (SellerSignatureDO.length() > 0)
					QryUpdateDO.append(", SELLERSIGNATURE = N'" + SellerSignatureDO + "' ");
				if (BuyerDO.length() > 0)
					QryUpdateDO.append(", BUYER = N'" + BuyerDO + "' ");
				if (BuyerSignatureDO.length() > 0)
					QryUpdateDO.append(", BUYERSIGNATURE = N'" + BuyerSignatureDO + "' ");
				
				QryUpdateDO.append(", PRINTWITHTAXINVOICE ='" + printwithTaxInvoiceDO	+ "' ");
				
				QryUpdateDO.append(", PRINTMULTILANG ='" + Multilanguprint	+ "' ");
				
				if (SubTotalDO.length() > 0)
				   QryUpdateDO.append(", SUBTOTAL = N'" + SubTotalDO + "' ");
			    if (TaxDO.length() > 0)
				   QryUpdateDO.append(", TOTALTAX = N'" + TaxDO + "' ");
			    if (TotalDO.length() > 0)
				   QryUpdateDO.append(", TOTAL = N'" + TotalDO + "' ");
				if (ContactNameDO.length() > 0)
					QryUpdateDO.append(", CONTACTNAME = N'" + ContactNameDO + "' ");
				if (EmailDO.length() > 0)
					QryUpdateDO.append(", EMAIL = N'" + EmailDO + "' ");
				if (FaxDO.length() > 0)
					QryUpdateDO.append(", FAX = N'" + FaxDO + "' ");
				if (TelephoneDO.length() > 0)
					QryUpdateDO.append(", TELEPHONE = N'" + TelephoneDO + "' ");
				if (HandphoneDO.length() > 0)
					QryUpdateDO.append(", HANDPHONE = N'" + HandphoneDO + "' ");
				if (AttentionDO.length() > 0)
					QryUpdateDO.append(", ATTENTION = N'" + AttentionDO + "' ");
				if (QtyTotalDO.length() > 0)
					QryUpdateDO.append(", QTYTOTAL = N'" + QtyTotalDO + "' ");
				if (remark3DO.length() > 0)
					QryUpdateDO.append(", REMARK3 = N'" + remark3DO + "' ");
				if (FooterPageDO.length() > 0)
					QryUpdateDO.append(", FOOTERPAGE = N'" + FooterPageDO + "' ");
				if (FooterOfDO.length() > 0)
					QryUpdateDO.append(", FOOTEROF = N'" + FooterOfDO + "' ");
				if (CashCustomerDO.length() > 0)
					QryUpdateDO.append(", CASHCUSTOMER = N'" + CashCustomerDO + "' ");
					
				if (RoundoffTotalwithDecimalDO.length() > 0)
					QryUpdateDO.append(", ROUNDOFFTOTALWITHDECIMAL = N'" + RoundoffTotalwithDecimalDO + "' ");
				QryUpdateDO.append(", PRINTROUNDOFFTOTALWITHDECIMAL ='" + printRoundoffTotalwithDecimalDO+ "' ");
				QryUpdateDO.append(", PRINTWITHPRODUCT ='" + printwithProductDO+ "' ");
				QryUpdateDO.append(", PRINTWITHDISCOUNT ='" + printDiscountReportDO+ "' ");
				if (DiscountDO.length() > 0)
					QryUpdateDO.append(", DISCOUNT = N'" + DiscountDO + "' ");
				if (NetRateDO.length() > 0)
					QryUpdateDO.append(", NETRATE = N'" + NetRateDO + "' ");
				
				if (prdDeliveryDateDO.length() > 0)
					QryUpdateDO.append(", PRODUCTDELIVERYDATE = '" + prdDeliveryDateDO + "' ");	
					QryUpdateDO.append(", PRINTWITHPRODUCTDELIVERYDATE ='" + printWithPrdDeliveryDateDO+ "' ");
					QryUpdateDO.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDateDO+ "' ");
					


				try {
					poHdrUpt = dhd.updateDOReciptInvoiceHeaderDO(QryUpdateDO.toString(), htDO, "");
				} catch (Exception e) {

			    	}
				}
				

				try {
					if (!poHdrUpt) {

						result = "<font class = " + IConstants.FAILED_COLOR
						+ ">Failed to edit the details</font>";
					 }else {
						result = "<font class = "
							+ IConstants.SUCCESS_COLOR
							+ ">Invoice printout edited successfully</font>";

						response
						.sendRedirect("../editsales/invoiceprintout?result="+ result);
					}
				} catch (Exception e) {

				}
			}
			
			else if (action.equalsIgnoreCase("EDIT_LO_RCPT_HDR")) {


				LoanHdrDAO dhd = new LoanHdrDAO();
				boolean loHdrUpt = false;
				String result = "";

				HttpSession session = request.getSession();
				String PLANT = (String) session.getAttribute("PLANT");
				String sUserId = (String) session.getAttribute("LOGIN_USER");

				String OrderHeader = StrUtils.fString(request.getParameter("OrderHeader"));
				String InvoiceOrderToHeader = StrUtils.fString(request.getParameter("InvoiceOrderToHeader"));
				String FromHeader = StrUtils.fString(request.getParameter("FromHeader"));
				String DateField = StrUtils.fString(request.getParameter("Date"));
				String OrderNo = StrUtils.fString(request.getParameter("OrderNo"));
				String RefNo = StrUtils.fString(request.getParameter("RefNo"));
				String SoNo = StrUtils.fString(request.getParameter("SoNo"));
				String Item = StrUtils.fString(request.getParameter("Item"));
				String Description = StrUtils.fString(request.getParameter("Description"));
				String OrderQty = StrUtils.fString(request.getParameter("OrderQty"));
				String UOM = StrUtils.fString(request.getParameter("UOM"));
				String Footer1 = StrUtils.fString(request.getParameter("Footer1"));
				String Footer2 = StrUtils.fString(request.getParameter("Footer2"));
                String Footer3 = StrUtils.fString(request.getParameter("Footer3"));
                String Footer4 = StrUtils.fString(request.getParameter("Footer4"));
                String Footer5 = StrUtils.fString(request.getParameter("Footer5"));
				String Footer6 = StrUtils.fString(request.getParameter("Footer6"));
                String Footer7 = StrUtils.fString(request.getParameter("Footer7"));
                String Footer8 = StrUtils.fString(request.getParameter("Footer8"));
                String Footer9 = StrUtils.fString(request.getParameter("Footer9"));
                String Remark1 = StrUtils.fString(request.getParameter("REMARK1"));
                String Orientation = StrUtils.fString(request.getParameter("Orientation"));
				String DELDATE = StrUtils.fString(request.getParameter("DELDATE"));
                String ORDDISCOUNT = StrUtils.fString(request.getParameter("ORDDISCOUNT"));
                String EMPNAME = StrUtils.fString(request.getParameter("EMPNAME"));
                String PREPAREDBY = StrUtils.fString(request.getParameter("PREPAREDBY"));
                String SELLER = StrUtils.fString(request.getParameter("SELLER"));
                String SELLERAUTHORIZED = StrUtils.fString(request.getParameter("SELLERAUTHORIZED"));
                String BUYER = StrUtils.fString(request.getParameter("BUYER"));
                String BUYERAUTHORIZED = StrUtils.fString(request.getParameter("BUYERAUTHORIZED"));
                String COMPANYNAME = StrUtils.fString(request.getParameter("COMPANYNAME"));
                String COMPANYSTAMP = StrUtils.fString(request.getParameter("COMPANYSTAMP"));
                String SIGNATURE = StrUtils.fString(request.getParameter("SIGNATURE"));
                String SHIPPINGCOST = StrUtils.fString(request.getParameter("SHIPPINGCOST"));
                String VAT = StrUtils.fString(request.getParameter("VAT"));
                String CUSTOMERVAT = StrUtils.fString(request.getParameter("CUSTOMERVAT"));
                String TOTALAFTERDISCOUNT = StrUtils.fString(request.getParameter("TOTALAFTERDISCOUNT"));
                String SHIPTO = StrUtils.fString(request.getParameter("SHIPTO"));
                String ORDDATE = StrUtils.fString(request.getParameter("ORDDATE"));
                String NETAMT = StrUtils.fString(request.getParameter("NETAMT"));
                String TAXAMT = StrUtils.fString(request.getParameter("TAXAMT"));
                String TOTALAMT = StrUtils.fString(request.getParameter("TOTALAMT"));
                String RATE = StrUtils.fString(request.getParameter("RATE"));
                String PAYMENTTYPE = StrUtils.fString(request.getParameter("PAYMENTTYPE"));
                String AMOUNT = StrUtils.fString(request.getParameter("AMOUNT"));
                String TOTALTAX = StrUtils.fString(request.getParameter("TOTALTAX"));
                String TOTALWITHTAX = StrUtils.fString(request.getParameter("TOTALWITHTAX"));
                String ROUNDOFF = StrUtils.fString(request.getParameter("ROUNDOFF"));
                String DISPLAYBYORDERTYPE = (request.getParameter("DISPLAYBYORDERTYPE") != null) ? "1": "0";
                String PRINTDETAILDESC = (request.getParameter("PRINTDETAILDESC") != null) ? "1": "0";
                String PRINTCUSTREMARKS = (request.getParameter("PRINTCUSTREMARKS") != null) ? "1": "0";
                String PRINTPRDREMARKS = (request.getParameter("PRINTPRDREMARKS") != null) ? "1": "0";
                String PRINTWITHBRAND = (request.getParameter("PRINTWITHBRAND") != null) ? "1": "0";
                String TOTALROUNDOFF = (request.getParameter("TOTALROUNDOFF") != null) ? "1": "0";
                String PRINTWITHEMPLOYEE = (request.getParameter("PRINTWITHEMPLOYEE") != null) ? "1": "0";
                String PRINTWITHPRDID = (request.getParameter("PRINTWITHPRDID") != null) ? "1": "0";
				Hashtable<String,String> ht = new Hashtable<String,String>();
				ht.put(IDBConstants.PLANT, PLANT);

				StringBuffer QryUpdate = new StringBuffer(" SET ");

				if (OrderHeader.length() > 0)
					QryUpdate.append(" ORDERHEADER = '" + OrderHeader + "' ");
				if (InvoiceOrderToHeader.length() > 0)
					QryUpdate.append(", TOHEADER = '" + InvoiceOrderToHeader + "' ");
				if (FromHeader.length() > 0)
					QryUpdate.append(", FROMHEADER = '" + FromHeader + "' ");
				if (DateField.length() > 0)
					QryUpdate.append(", DATE = '" + DateField + "' ");
				if (OrderNo.length() > 0)
					QryUpdate.append(", ORDERNO = '" + OrderNo + "' ");
				if (RefNo.length() > 0)
					QryUpdate.append(", REFNO = '" + RefNo + "' ");
				if (SoNo.length() > 0)
					QryUpdate.append(", SONO = '" + SoNo + "' ");
				if (Item.length() > 0)
					QryUpdate.append(", ITEM = '" + Item + "' ");
				if (Description.length() > 0)
					QryUpdate.append(", DESCRIPTION = '" + Description + "' ");
				if (OrderQty.length() > 0)
					QryUpdate.append(", ORDERQTY = '" + OrderQty + "' ");
				if (UOM.length() > 0)
					QryUpdate.append(", UOM = '" + UOM + "' ");
				if (PREPAREDBY.length() > 0)
					QryUpdate.append(", PREPAREDBY = '" + PREPAREDBY + "' ");
				if (SELLER.length() > 0)
					QryUpdate.append(", SELLER = '" + SELLER + "' ");
				if (SELLERAUTHORIZED.length() > 0)
					QryUpdate.append(", SELLERAUTHORIZED = '" + SELLERAUTHORIZED + "' ");
				if (BUYER.length() > 0)
					QryUpdate.append(", BUYER = '" + BUYER + "' ");
				if (BUYERAUTHORIZED.length() > 0)
					QryUpdate.append(", BUYERAUTHORIZED = '" + BUYERAUTHORIZED + "' ");
				if (DELDATE.length() > 0)
					QryUpdate.append(", DELDATE = '" + DELDATE + "' ");
				if (ORDDISCOUNT.length() > 0)
					QryUpdate.append(", ORDDISCOUNT = '" + ORDDISCOUNT + "' ");
				if (EMPNAME.length() > 0)
					QryUpdate.append(", EMPNAME = '" + EMPNAME + "' ");
				
				if (COMPANYNAME.length() > 0)
					QryUpdate.append(", COMPANYNAME = '" + COMPANYNAME + "' ");
				if (COMPANYSTAMP.length() > 0)
					QryUpdate.append(", COMPANYSTAMP = '" + COMPANYSTAMP + "' ");
				if (SIGNATURE.length() > 0)
					QryUpdate.append(", SIGNATURE = '" + SIGNATURE + "' ");
				if (SHIPPINGCOST.length() > 0)
					QryUpdate.append(", SHIPPINGCOST = '" + SHIPPINGCOST + "' ");
				if (VAT.length() > 0)
					QryUpdate.append(", VAT = '" + VAT + "' ");
				if (CUSTOMERVAT.length() > 0)
					QryUpdate.append(", CUSTOMERVAT = '" + CUSTOMERVAT + "' ");
				if (TOTALAFTERDISCOUNT.length() > 0)
					QryUpdate.append(", TOTALAFTERDISCOUNT = '" + TOTALAFTERDISCOUNT + "' ");
				if (ORDDATE.length() > 0)
					QryUpdate.append(", ORDDATE = '" + ORDDATE + "' ");
				if (SHIPTO.length() > 0)
					QryUpdate.append(", SHIPTO = '" + SHIPTO + "' ");
				if (NETAMT.length() > 0)
					QryUpdate.append(", NETAMT = '" + NETAMT + "' ");
				if (TAXAMT.length() > 0)
					QryUpdate.append(", TAXAMT = '" + TAXAMT + "' ");
				if (TOTALAMT.length() > 0)
					QryUpdate.append(", TOTALAMT = '" + TOTALAMT + "' ");
				if (RATE.length() > 0)
					QryUpdate.append(", RATE = '" + RATE + "' ");
				if (PAYMENTTYPE.length() > 0)
					QryUpdate.append(", PAYMENTTYPE = '" + PAYMENTTYPE + "' ");
				
				if (AMOUNT.length() > 0)
					QryUpdate.append(", AMOUNT = '" + AMOUNT + "' ");
				if (TOTALTAX.length() > 0)
					QryUpdate.append(", TOTALTAX = '" + TOTALTAX + "' ");
				if (TOTALWITHTAX.length() > 0)
					QryUpdate.append(", TOTALWITHTAX = '" + TOTALWITHTAX + "' ");
			
		                QryUpdate.append(", FOOTER1 = '" + Footer1 + "' ");
		                QryUpdate.append(", FOOTER2 = '" + Footer2 + "' ");
		                QryUpdate.append(", FOOTER3 = '" + Footer3 + "' ");
		                QryUpdate.append(", FOOTER4 = '" + Footer4 + "' ");
		                QryUpdate.append(", FOOTER5 = '" + Footer5 + "' ");
		                QryUpdate.append(", FOOTER6 = '" + Footer6 + "' ");
		                QryUpdate.append(", FOOTER7 = '" + Footer7 + "' ");
		                QryUpdate.append(", FOOTER8 = '" + Footer8 + "' ");
		                QryUpdate.append(", FOOTER9 = '" + Footer9 + "' ");
		                QryUpdate.append(", PrintOrientation = '" + Orientation + "' ");
		                
		                QryUpdate.append(", DISPLAYBYORDERTYPE = '" + DISPLAYBYORDERTYPE + "' ");
		                QryUpdate.append(", PRINTDETAILDESC = '" + PRINTDETAILDESC + "' ");
		                QryUpdate.append(", PRINTCUSTREMARKS = '" + PRINTCUSTREMARKS + "' ");
		                QryUpdate.append(", PRINTPRDREMARKS = '" + PRINTPRDREMARKS + "' ");
		                QryUpdate.append(", PRINTWITHBRAND = '" + PRINTWITHBRAND + "' ");
		                QryUpdate.append(", TOTALROUNDOFF = '" + TOTALROUNDOFF + "' ");
		                QryUpdate.append(", PRINTWITHEMPLOYEE = '" + PRINTWITHEMPLOYEE + "' ");
		                QryUpdate.append(", PRINTWITHPRDID = '" + PRINTWITHPRDID + "' ");
		                QryUpdate.append(", ROUNDOFF = '" + ROUNDOFF + "' ");
		        
		       if (Remark1.length() > 0)
				   QryUpdate.append(", REMARK1 = '" + Remark1 + "' ");      
                                
				QryUpdate.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
				QryUpdate.append(", UPBY = '" + sUserId + "' ");

				try {
					loHdrUpt = dhd.updateLOReciptHeader(QryUpdate.toString(), ht, "");
				} catch (Exception e) {

				}
				
				try {
					if (!loHdrUpt) {

						result = "<font class = " + IConstants.FAILED_COLOR + ">Failed to edit the details</font>";
						response.sendRedirect("jsp/editLoanLO.jsp?result=" + result);
					} else {
						result = "<font class = " + IConstants.SUCCESS_COLOR + ">Rental and Service Order Printout edited successfully</font>";
						response.sendRedirect("jsp/editLoanLO.jsp?result=" + result);
					}
				} catch (Exception e) {

				}

			
        }
			//CREATED BY NAVAS
			else if (action.equalsIgnoreCase("EDIT_RCPT_HDR")) {


				ToHdrDAO dhd = new ToHdrDAO();
				boolean toHdrUpt = false;
				String result = "";
				
				String OrderType = "",OrderTypeTO="";
				HttpSession session = request.getSession();
				String PLANT = (String) session.getAttribute("PLANT");
				String sUserId = (String) session.getAttribute("LOGIN_USER");

				String OrderHeader = StrUtils.fString(request.getParameter("OrderHeader"));
				String InvoiceOrderToHeader = StrUtils.fString(request.getParameter("InvoiceOrderToHeader"));
				String FromHeader = StrUtils.fString(request.getParameter("FromHeader"));
				String DateField = StrUtils.fString(request.getParameter("Date"));
				OrderType = StrUtils.fString(request.getParameter("OrderType"));
				String OrderNo = StrUtils.fString(request.getParameter("OrderNo"));
				String RefNo = StrUtils.fString(request.getParameter("RefNo"));
				String SoNo = StrUtils.fString(request.getParameter("SoNo"));
				String Item = StrUtils.fString(request.getParameter("Item"));
				String Description = StrUtils.fString(request.getParameter("Description"));
				String OrderQty = StrUtils.fString(request.getParameter("OrderQty"));
				String UOM = StrUtils.fString(request.getParameter("UOM"));
				String Footer1 = StrUtils.fString(request.getParameter("Footer1"));
				String Footer2 = StrUtils.fString(request.getParameter("Footer2"));
		        String Footer3 = StrUtils.fString(request.getParameter("Footer3"));
		        String Footer4 = StrUtils.fString(request.getParameter("Footer4"));
		        String printBarcode = (request.getParameter("printBarcode") != null) ? "1" : "0";
		        String ProductRatesAre = request.getParameter("ProductRatesAre").trim();
		        String printCustTerms = (request.getParameter("printCustTerms") != null) ? "1": "0";
		        String TermsDetails = StrUtils.fString(request.getParameter("TermsDetails"));
		        
				/* CREATED BY NAVAS */
		        String Footer5 = StrUtils.fString(request.getParameter("Footer5"));
		        String Footer6 = StrUtils.fString(request.getParameter("Footer6"));
				String Footer7 = StrUtils.fString(request.getParameter("Footer7"));
				String Footer8 = StrUtils.fString(request.getParameter("Footer8"));
				String Footer9 = StrUtils.fString(request.getParameter("Footer9"));
				String DisplayByOrdertype = (request.getParameter("DisplayByOrdertype") != null) ? "1": "0";
				    String printLocStock = (request.getParameter("printLocStock") != null) ? "1" : "0";
				    String printCustRemarks = (request.getParameter("printCustRemarks") != null) ? "1" : "0";
				    String remark1 = request.getParameter("REMARK1").trim();
				    String remark2 = request.getParameter("REMARK2").trim();
				    String deliverydate = request.getParameter("DeliveryDate").trim();
				    String employee= request.getParameter("Employee").trim();
				    String shipto = request.getParameter("ShipTo").trim(); 
	    		    String companydate = request.getParameter("CompanyDate").trim();
				    String companyname = request.getParameter("CompanyName").trim();
				    String companystamp = request.getParameter("CompanyStamp").trim();
				    String companysig = request.getParameter("CompanySig").trim(); 
				    String printEmployee = (request.getParameter("printEmployee") != null) ? "1" : "0";;
				    String incoterm = request.getParameter("INCOTERM").trim();
				    String printwithproductremarks = (request.getParameter("printwithproductremarks") != null) ? "1": "0";
				    String printwithhscode = (request.getParameter("printwithhscode") != null) ? "1": "0";
				    String printwithcoo = (request.getParameter("printwithcoo") != null) ? "1": "0";
				    String printwithbrand = (request.getParameter("printwithbrand") != null) ? "1": "0";
				    String printWithUENNO = (request.getParameter("printWithUENNO"));
//				    String printWithUENNO = (request.getParameter("printWithUENNO") != null) ? "1": "0";
				    String printWithCustomerUENNO = (request.getParameter("printWithCustomerUENNO") != null) ? "1": "0";
				    String brand = request.getParameter("BRAND").trim();
				    String hscode = request.getParameter("HSCODE").trim();
				    String coo = request.getParameter("COO").trim();
				    String rcbno = request.getParameter("RCBNO").trim();
				    String customerrcbno = request.getParameter("CUSTOMERRCBNO").trim();
				    
				    String uenno = request.getParameter("UENNO").trim();
				    String customeruenno = request.getParameter("CUSTOMERUENNO").trim();
				    
				    String PreparedBy = StrUtils.fString(request.getParameter("PreparedBy"));
					String Seller = StrUtils.fString(request.getParameter("Seller"));
				    String SellerSignature = StrUtils.fString(request.getParameter("SellerSignature"));
				    String Buyer = StrUtils.fString(request.getParameter("Buyer"));
				    String BuyerSignature = StrUtils.fString(request.getParameter("BuyerSignature"));
				    String project = request.getParameter("Project").trim();
				    String printwithproject = (request.getParameter("printwithproject") != null) ? "1": "0";
				    String printwithcompanysig = (request.getParameter("printwithcompanysig") != null) ? "1": "0";
				    String printwithcompanyseal = (request.getParameter("printwithcompanyseal") != null) ? "1": "0";
				    
				    
				    String prdDeliveryDate = request.getParameter("prdDeliveryDate").trim();
				    String printWithPrdDeliveryDate = (request.getParameter("printWithPrdDeliveryDate") != null) ? "1": "0";
				    String printWithDeliveryDate = (request.getParameter("printWithDeliveryDate") != null) ? "1": "0";
		        
                String printDetailDesc = (request.getParameter("printDetailDesc") != null) ? "1": "0";  
                String Orientation = StrUtils.fString(request.getParameter("Orientation"));
                                

				Hashtable<String,String> ht = new Hashtable<String,String>();
				ht.put(IDBConstants.PLANT, PLANT);
				
	
				
				if (OrderType.equalsIgnoreCase("Mobile Registration")) {
					ht.put(IDBConstants.ORDERTYPE, OrderType);
				} else if (OrderType.equalsIgnoreCase("Mobile Enquiry")) {
					ht.put(IDBConstants.ORDERTYPE, OrderType);
				} else {
					ht.put(IDBConstants.ORDERTYPE, OrderType);
				}
				

				StringBuffer QryUpdate = new StringBuffer(" SET ");

				if (OrderHeader.length() > 0)
					QryUpdate.append(" ORDERHEADER = '" + OrderHeader + "' ");
				if (InvoiceOrderToHeader.length() > 0)
					QryUpdate.append(", TOHEADER = '" + InvoiceOrderToHeader + "' ");
				if (FromHeader.length() > 0)
					QryUpdate.append(", FROMHEADER = '" + FromHeader + "' ");
				if (DateField.length() > 0)
					QryUpdate.append(", DATE = '" + DateField + "' ");
				if (OrderNo.length() > 0)
					QryUpdate.append(", ORDERNO = '" + OrderNo + "' ");
				if (RefNo.length() > 0)
					QryUpdate.append(", REFNO = '" + RefNo + "' ");
				if (SoNo.length() > 0)
					QryUpdate.append(", SONO = '" + SoNo + "' ");
				if (Item.length() > 0)
					QryUpdate.append(", ITEM = '" + Item + "' ");
				if (Description.length() > 0)
					QryUpdate.append(", DESCRIPTION = '" + Description + "' ");
				/* CREATED BY NAVAS */
				if (OrderQty.length() > 0)
					QryUpdate.append(", ORDERQTY = '" + OrderQty + "' ");
				if (UOM.length() > 0)
					QryUpdate.append(", UOM = '" + UOM + "' ");
				
								QryUpdate.append(", PRINTBARCODE ='" + printBarcode+ "' ");
								QryUpdate.append(", PrintLocStock ='" + printLocStock+ "' ");
		                        QryUpdate.append(", FOOTER1 = '" + Footer1 + "' ");
		                        QryUpdate.append(", FOOTER2 = '" + Footer2 + "' ");
		                        QryUpdate.append(", FOOTER3 = '" + Footer3 + "' ");
		                        QryUpdate.append(", FOOTER4 = '" + Footer4 + "' ");
		                        QryUpdate.append(", FOOTER5 = '" + Footer5 + "' ");
		        				QryUpdate.append(", FOOTER6 = '" + Footer6 + "' ");
		        				QryUpdate.append(", FOOTER7 = '" + Footer7 + "' ");
		        				QryUpdate.append(", FOOTER8 = '" + Footer8 + "' ");
		        				QryUpdate.append(", FOOTER9 = '" + Footer9 + "' ");
		        				QryUpdate.append(", PRODUCTRATESARE ='" + ProductRatesAre+ "' ");
		        				QryUpdate.append(", PRINTCUSTERMS ='" + printCustTerms+ "' ");
		        				QryUpdate.append(", TERMSDETAILS = '" + TermsDetails + "' ");
                                QryUpdate.append(", PRINTXTRADETAILS ='" + printDetailDesc+ "' ");
                                QryUpdate.append(", DISPLAYBYORDERTYPE ='" + DisplayByOrdertype+ "' ");
                                QryUpdate.append(", PRINTWITHBRAND ='" + printwithbrand+ "' ");
                				QryUpdate.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarks+ "' ");
                				QryUpdate.append(", PRITNWITHHSCODE ='" + printwithhscode+ "' ");
                				QryUpdate.append(", PRITNWITHCOO ='" + printwithcoo+ "' ");
                				QryUpdate.append(", PRINTWITHUENNO ='" + printWithUENNO+ "' ");
                				QryUpdate.append(", PRINTWITHCUSTOMERUENNO ='" + printWithCustomerUENNO+ "' ");
                				if (remark1.length() > 0)
                					QryUpdate.append(", REMARK1 = '" + remark1 + "' ");
                			    if (remark2.length() > 0)
                					QryUpdate.append(", REMARK2 = '" + remark2 + "' ");
                			    
                			    if (brand.length() > 0)
                					QryUpdate.append(", BRAND = '" + brand + "' ");
                			    
                			    if (hscode.length() > 0)
                					QryUpdate.append(", HSCODE = '" + hscode + "' ");
                			    
                			    if (coo.length() > 0)
                					QryUpdate.append(", COO = '" + coo + "' ");
                			    
                			    if (rcbno.length() > 0)
                					QryUpdate.append(", RCBNO = '" + rcbno + "' ");
                			    			    
                			    if (customerrcbno.length() > 0)
                					QryUpdate.append(", CUSTOMERRCBNO = '" + customerrcbno + "' ");
                			    
                			    if (uenno.length() > 0)
                			    	QryUpdate.append(", UENNO = '" + uenno + "' ");
                			    
                			    if (customeruenno.length() > 0)
                					QryUpdate.append(", CUSTOMERUENNO = '" + customeruenno + "' ");
                			    
                			    if (deliverydate.length() > 0)
                					QryUpdate.append(", DELIVERYDATE = '" + deliverydate + "' ");	
                			    if (employee.length() > 0)
                					QryUpdate.append(", EMPLOYEE = '" + employee + "' ");		
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
                				if (printEmployee.length() > 0)
                					QryUpdate.append(", PRINTEMPLOYEE = '" + printEmployee + "' ");
                				if (incoterm.length() > 0)
                					QryUpdate.append(", INCOTERM = '" + incoterm + "' ");
                				if (PreparedBy.length() > 0)
                					QryUpdate.append(", PREPAREDBY = '" + PreparedBy + "' ");
                				if (Seller.length() > 0)
                					QryUpdate.append(", SELLER = '" + Seller + "' ");
                				if (SellerSignature.length() > 0)
                					QryUpdate.append(", SELLERSIGNATURE = '" + SellerSignature + "' ");
                				if (Buyer.length() > 0)
                					QryUpdate.append(", BUYER = '" + Buyer + "' ");
                				if (BuyerSignature.length() > 0)
                					QryUpdate.append(", BUYERSIGNATURE = '" + BuyerSignature + "' ");
                				
                				if (prdDeliveryDate.length() > 0)
                					QryUpdate.append(", PRODUCTDELIVERYDATE = '" + prdDeliveryDate + "' ");	
                				QryUpdate.append(", PRINTWITHPRODUCTDELIVERYDATE ='" + printWithPrdDeliveryDate+ "' ");
                				QryUpdate.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDate+ "' ");
                				if (project.length() > 0)
        						QryUpdate.append(", PROJECT = '" + project + "' ");	
        					
        					QryUpdate.append(", PRINTWITHPROJECT ='" + printwithproject+ "' ");
        					QryUpdate.append(", PRINTWITHCOMPANYSIG='" + printwithcompanysig+ "' ");
        					QryUpdate.append(", PRINTWITHCOMPANYSEAL='" + printwithcompanyseal+ "' ");
                				
                				
				QryUpdate.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
				QryUpdate.append(", UPBY = '" + sUserId + "' ");
				QryUpdate.append(", PrintOrientation = '" + Orientation + "' ");

				try {
					toHdrUpt = dhd.updateReciptHeader(QryUpdate.toString(), ht, "");
				} catch (Exception e) {

				}
				
				try {
					if (!toHdrUpt) {

						result = "<font class = " + IConstants.FAILED_COLOR
								+ ">Failed to edit the details</font>";
						response
								.sendRedirect("../editconsignment/consignmentprintout?result=" + result);
					} else {
						result = "<font class = " + IConstants.SUCCESS_COLOR
								+ ">Consignment Order Printout edited successfully</font>";

						response
								.sendRedirect("../editconsignment/consignmentprintout?result=" + result);
					}
				} catch (Exception e) {

				}

			}
			
			//CREATED BY NAVAS FOR TO UPDATE
			else if (action.equalsIgnoreCase("EDIT_RCPT_HDRTO")) {

				//TO UPDATE TO
				ToHdrDAO dhd = new ToHdrDAO();
				boolean toHdrUpt = false;
				String result = "";
				
				String OrderType = "",OrderTypeTO="";
				HttpSession session = request.getSession();
				String PLANT = (String) session.getAttribute("PLANT");
				String sUserId = (String) session.getAttribute("LOGIN_USER");
				OrderTypeTO = StrUtils.fString(request.getParameter("OrderTypeTO")); 
				String OrderHeaderTO = StrUtils.fString(request.getParameter("OrderHeaderTO"));
				String InvoiceOrderToHeaderTO = StrUtils.fString(request.getParameter("InvoiceOrderToHeaderTO"));
				String FromHeaderTO = StrUtils.fString(request.getParameter("FromHeaderTO"));
				String DateFieldTO = StrUtils.fString(request.getParameter("DateTO"));
				String OrderNoTO = StrUtils.fString(request.getParameter("OrderNoTO"));
				String RefNoTO = StrUtils.fString(request.getParameter("RefNoTO"));
				String SoNoTO = StrUtils.fString(request.getParameter("SoNoTO"));
				String ItemTO = StrUtils.fString(request.getParameter("ItemTO"));
				String DescriptionTO = StrUtils.fString(request.getParameter("DescriptionTO"));
				String OrderQtyTO = StrUtils.fString(request.getParameter("OrderQtyTO"));
				String UOMTO = StrUtils.fString(request.getParameter("UOMTO"));
				String Footer1TO = StrUtils.fString(request.getParameter("Footer1TO"));
				String Footer2TO = StrUtils.fString(request.getParameter("Footer2TO"));
		        String Footer3TO = StrUtils.fString(request.getParameter("Footer3TO"));
		        String Footer4TO = StrUtils.fString(request.getParameter("Footer4TO"));
		        String printBarcodeTO = StrUtils.fString(request.getParameter("printBarcodeTO"));
		        String uennoTO = request.getParameter("UENNOTO").trim();
			    String customeruennoTO = request.getParameter("CUSTOMERUENNOTO").trim();
			    String ProductRatesAreTO = request.getParameter("ProductRatesAreTO").trim();
			    String printCustTermsTO = (request.getParameter("printCustTermsTO") != null) ? "1": "0";
			    String TermsDetailsTO = StrUtils.fString(request.getParameter("TermsDetailsTO"));

				/* CREATED BY NAVAS */
		        String Footer5TO = StrUtils.fString(request.getParameter("Footer5TO"));
		        String Footer6TO = StrUtils.fString(request.getParameter("Footer6TO"));
				String Footer7TO = StrUtils.fString(request.getParameter("Footer7TO"));
				String Footer8TO = StrUtils.fString(request.getParameter("Footer8TO"));
				String Footer9TO = StrUtils.fString(request.getParameter("Footer9TO"));
				String DisplayByOrdertypeTO = (request.getParameter("DisplayByOrdertypeTO") != null) ? "1": "0";
				    String printLocStockTO = (request.getParameter("printLocStockTO") != null) ? "1" : "0";
				    String printCustRemarksTO = (request.getParameter("printCustRemarksTO") != null) ? "1" : "0";
				    String remark1TO = request.getParameter("REMARK1TO").trim();
				    String remark2TO = request.getParameter("REMARK2TO").trim();
				    String deliverydateTO = request.getParameter("DeliveryDateTO").trim();
				    String employeeTO= request.getParameter("EmployeeTO").trim();
				    String shiptoo = request.getParameter("ShipToo").trim(); 
	    		    String companydateTO = request.getParameter("CompanyDateTO").trim();
				    String companynameTO = request.getParameter("CompanyNameTO").trim();
				    String companystampTO = request.getParameter("CompanyStampTO").trim();
				    String companysigTO = request.getParameter("CompanySigTO").trim(); 
				    String printEmployeeTO = (request.getParameter("printEmployeeTO") != null) ? "1" : "0";;
				    String incotermTO = request.getParameter("INCOTERMTO").trim();
				    String printwithproductremarksTO = (request.getParameter("printwithproductremarksTO") != null) ? "1": "0";
				    String printwithhscodeTO = (request.getParameter("printwithhscodeTO") != null) ? "1": "0";
				    String printwithcooTO = (request.getParameter("printwithcooTO") != null) ? "1": "0";
				    String printwithbrandTO = (request.getParameter("printwithbrandTO") != null) ? "1": "0";
				    String printWithUENNOTO = (request.getParameter("printWithUENNOTO"));
//				    String printWithUENNOTO = (request.getParameter("printWithUENNOTO") != null) ? "1": "0";
				    String printWithCustomerUENNOTO = (request.getParameter("printWithCustomerUENNOTO") != null) ? "1": "0";
				    String printwithcompanysigTO = (request.getParameter("printwithcompanysigTO") != null) ? "1": "0";
				    String printwithcompanysealTO = (request.getParameter("printwithcompanysealTO") != null) ? "1": "0";
				   
				    String brandTO = request.getParameter("BRANDTO").trim();
				    String hscodeTO = request.getParameter("HSCODETO").trim();
				    String cooTO = request.getParameter("COOTO").trim();
				    String rcbnoTO = request.getParameter("RCBNOTO").trim();
				    String customerrcbnoTO = request.getParameter("CUSTOMERRCBNOTO").trim();
				    
				    String PreparedByTO = StrUtils.fString(request.getParameter("PreparedByTO"));
					String SellerTO = StrUtils.fString(request.getParameter("SellerTO"));
				    String SellerSignatureTO = StrUtils.fString(request.getParameter("SellerSignatureTO"));
				    String BuyerTO = StrUtils.fString(request.getParameter("BuyerTO"));
				    String BuyerSignatureTO = StrUtils.fString(request.getParameter("BuyerSignatureTO"));
				    String projectTO = request.getParameter("ProjectTO").trim();
				    String printwithprojectTO = (request.getParameter("printwithprojectTO") != null) ? "1": "0";
				    
				    
				    String prdDeliveryDateTO = request.getParameter("prdDeliveryDateTO").trim();
				    String printWithPrdDeliveryDateTO = (request.getParameter("printWithPrdDeliveryDateTO") != null) ? "1": "0";
				    String printWithDeliveryDateTO = (request.getParameter("printWithDeliveryDateTO") != null) ? "1": "0";
		        
                String printDetailDescTO = (request.getParameter("printDetailDescTO") != null) ? "1": "0";  
                String OrientationTO = StrUtils.fString(request.getParameter("OrientationTO"));
                                

				Hashtable<String,String> htTO = new Hashtable<String,String>();
				htTO.put(IDBConstants.PLANT, PLANT);
				
				if (OrderTypeTO.equalsIgnoreCase("Mobile Registration")) {
					htTO.put(IDBConstants.ORDERTYPE, OrderTypeTO);
				} else if (OrderTypeTO.equalsIgnoreCase("Mobile Enquiry")) {
					htTO.put(IDBConstants.ORDERTYPE, OrderTypeTO);
				} else {
					htTO.put(IDBConstants.ORDERTYPE, OrderTypeTO);
				}

				StringBuffer QryUpdateTO = new StringBuffer(" SET ");

				if (OrderHeaderTO.length() > 0)
					QryUpdateTO.append(" ORDERHEADER = '" + OrderHeaderTO + "' ");
				if (InvoiceOrderToHeaderTO.length() > 0)
					QryUpdateTO.append(", TOHEADER = '" + InvoiceOrderToHeaderTO + "' ");
				if (FromHeaderTO.length() > 0)
					QryUpdateTO.append(", FROMHEADER = '" + FromHeaderTO + "' ");
				if (DateFieldTO.length() > 0)
					QryUpdateTO.append(", DATE = '" + DateFieldTO + "' ");
				if (OrderNoTO.length() > 0)
					QryUpdateTO.append(", ORDERNO = '" + OrderNoTO + "' ");
				if (RefNoTO.length() > 0)
					QryUpdateTO.append(", REFNO = '" + RefNoTO + "' ");
				if (SoNoTO.length() > 0)
					QryUpdateTO.append(", SONO = '" + SoNoTO + "' ");
				if (ItemTO.length() > 0)
					QryUpdateTO.append(", ITEM = '" + ItemTO + "' ");
				if (DescriptionTO.length() > 0)
					QryUpdateTO.append(", DESCRIPTION = '" + DescriptionTO + "' ");
				/* CREATED BY NAVAS */
				
				if (OrderQtyTO.length() > 0)
					QryUpdateTO.append(", ORDERQTY = '" + OrderQtyTO + "' ");
				if (UOMTO.length() > 0)
					QryUpdateTO.append(", UOM = '" + UOMTO + "' ");
				
				QryUpdateTO.append(", PRINTBARCODE ='" + printBarcodeTO+ "' ");
				QryUpdateTO.append(", PrintLocStock ='" + printLocStockTO+ "' ");
				QryUpdateTO.append(", FOOTER1 = '" + Footer1TO + "' ");
				QryUpdateTO.append(", FOOTER2 = '" + Footer2TO + "' ");
				QryUpdateTO.append(", FOOTER3 = '" + Footer3TO + "' ");
				QryUpdateTO.append(", FOOTER4 = '" + Footer4TO + "' ");
				QryUpdateTO.append(", FOOTER5 = '" + Footer5TO + "' ");
				QryUpdateTO.append(", FOOTER6 = '" + Footer6TO + "' ");
				QryUpdateTO.append(", FOOTER7 = '" + Footer7TO + "' ");
				QryUpdateTO.append(", FOOTER8 = '" + Footer8TO + "' ");
				QryUpdateTO.append(", FOOTER9 = '" + Footer9TO + "' ");
				QryUpdateTO.append(", PRODUCTRATESARE ='" + ProductRatesAreTO+ "' ");
				QryUpdateTO.append(", PRINTCUSTERMS ='" + printCustTermsTO+ "' ");
				QryUpdateTO.append(", TERMSDETAILS = '" + TermsDetailsTO + "' ");
				QryUpdateTO.append(", PRINTXTRADETAILS ='" + printDetailDescTO+ "' ");
				QryUpdateTO.append(", DISPLAYBYORDERTYPE ='" + DisplayByOrdertypeTO+ "' ");
				QryUpdateTO.append(", PRINTWITHBRAND ='" + printwithbrandTO+ "' ");
				QryUpdateTO.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarksTO+ "' ");
				QryUpdateTO.append(", PRITNWITHHSCODE ='" + printwithhscodeTO+ "' ");
				QryUpdateTO.append(", PRITNWITHCOO ='" + printwithcooTO+ "' ");
				QryUpdateTO.append(", PRINTWITHUENNO ='" + printWithUENNOTO+ "' ");
				QryUpdateTO.append(", PRINTWITHCUSTOMERUENNO ='" + printWithCustomerUENNOTO+ "' ");
				QryUpdateTO.append(", PRINTWITHCOMPANYSIG ='" + printwithcompanysigTO+ "' ");
				QryUpdateTO.append(", PRINTWITHCOMPANYSEAL ='" + printwithcompanysealTO+ "' ");
                				if (remark1TO.length() > 0)
                					QryUpdateTO.append(", REMARK1 = '" + remark1TO + "' ");
                			    if (remark2TO.length() > 0)
                			    	QryUpdateTO.append(", REMARK2 = '" + remark2TO + "' ");
                			    
                			    if (brandTO.length() > 0)
                			    	QryUpdateTO.append(", BRAND = '" + brandTO + "' ");
                			    
                			    if (hscodeTO.length() > 0)
                			    	QryUpdateTO.append(", HSCODE = '" + hscodeTO + "' ");
                			    
                			    if (cooTO.length() > 0)
                			    	QryUpdateTO.append(", COO = '" + cooTO + "' ");
                			    
                			    if (rcbnoTO.length() > 0)
                			    	QryUpdateTO.append(", RCBNO = '" + rcbnoTO + "' ");
                			    			    
                			    if (customerrcbnoTO.length() > 0)
                			    	QryUpdateTO.append(", CUSTOMERRCBNO = '" + customerrcbnoTO + "' ");
                			    
                			    if (uennoTO.length() > 0)
                			    	QryUpdateTO.append(", UENNO = '" + uennoTO + "' ");
                			    
                			    if (customeruennoTO.length() > 0)
                					QryUpdateTO.append(", CUSTOMERUENNO = '" + customeruennoTO + "' ");
                			    
                			    if (deliverydateTO.length() > 0)
                			    	QryUpdateTO.append(", DELIVERYDATE = '" + deliverydateTO + "' ");	
                			    if (employeeTO.length() > 0)
                			    	QryUpdateTO.append(", EMPLOYEE = '" + employeeTO + "' ");		
                				if (shiptoo.length() > 0)
                					QryUpdateTO.append(", SHIPTO = '" + shiptoo + "' ");	
                				if (companydateTO.length() > 0)
                					QryUpdateTO.append(", COMPANYDATE = '" + companydateTO + "' ");
                				if (companynameTO.length() > 0)
                					QryUpdateTO.append(", COMPANYNAME = '" + companynameTO + "' ");
                				if (companystampTO.length() > 0)
                					QryUpdateTO.append(", COMPANYSTAMP = '" + companystampTO + "' ");
                				if (companysigTO.length() > 0)
                					QryUpdateTO.append(", COMPANYSIG = '" + companysigTO + "' ");
                				if (printEmployeeTO.length() > 0)
                					QryUpdateTO.append(", PRINTEMPLOYEE = '" + printEmployeeTO + "' ");
                				if (incotermTO.length() > 0)
                					QryUpdateTO.append(", INCOTERM = '" + incotermTO + "' ");
                				if (PreparedByTO.length() > 0)
                					QryUpdateTO.append(", PREPAREDBY = '" + PreparedByTO + "' ");
                				if (SellerTO.length() > 0)
                					QryUpdateTO.append(", SELLER = '" + SellerTO + "' ");
                				if (SellerSignatureTO.length() > 0)
                					QryUpdateTO.append(", SELLERSIGNATURE = '" + SellerSignatureTO + "' ");
                				if (BuyerTO.length() > 0)
                					QryUpdateTO.append(", BUYER = '" + BuyerTO + "' ");
                				if (BuyerSignatureTO.length() > 0)
                					QryUpdateTO.append(", BUYERSIGNATURE = '" + BuyerSignatureTO + "' ");
                				
                				if (prdDeliveryDateTO.length() > 0)
                					QryUpdateTO.append(", PRODUCTDELIVERYDATE = '" + prdDeliveryDateTO + "' ");	
                				QryUpdateTO.append(", PRINTWITHPRODUCTDELIVERYDATE ='" + printWithPrdDeliveryDateTO+ "' ");
                				QryUpdateTO.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDateTO+ "' ");
                				if (projectTO.length() > 0)
                					QryUpdateTO.append(", PROJECT = '" + projectTO + "' ");	
        					
                				QryUpdateTO.append(", PRINTWITHPROJECT ='" + printwithprojectTO+ "' ");
                				
                				
                				QryUpdateTO.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
                				QryUpdateTO.append(", UPBY = '" + sUserId + "' ");
                				QryUpdateTO.append(", PrintOrientation = '" + OrientationTO + "' ");

				try {
//					toHdrUpt = dhd.updateTOReciptHeader(QryUpdateTO.toString(), htTO, "");
					toHdrUpt = dhd.updateReciptHeader(QryUpdateTO.toString(), htTO, "");
				} catch (Exception e) {

				}
				
				try {
					if (!toHdrUpt) {

						result = "<font class = " + IConstants.FAILED_COLOR
								+ ">Failed to edit the details</font>";
						response
								.sendRedirect("../editconsignment/consignmentprintout?result=" + result);
					} else {
						result = "<font class = " + IConstants.SUCCESS_COLOR
								+ ">Consignment Order Printout edited successfully</font>";

						response
								.sendRedirect("../editconsignment/consignmentprintout?result=" + result);
					}
				} catch (Exception e) {

				}

			}
			//END BY NAVAS
			
			//navas
			else if (action.equalsIgnoreCase("EDIT_TO_RCPT_HDR")) {


			ToHdrDAO dhd = new ToHdrDAO();
			boolean toHdrUpt = false;
			String result = "";

			HttpSession session = request.getSession();
			String PLANT = (String) session.getAttribute("PLANT");
			String sUserId = (String) session.getAttribute("LOGIN_USER");

			String OrderHeader = StrUtils.fString(request
			.getParameter("OrderHeader"));
			String InvoiceOrderToHeader = StrUtils.fString(request.getParameter("InvoiceOrderToHeader"));
			String FromHeader = StrUtils.fString(request.getParameter("FromHeader"));
			String DateField = StrUtils.fString(request.getParameter("Date"));
			String OrderNo = StrUtils.fString(request.getParameter("OrderNo"));
			String RefNo = StrUtils.fString(request.getParameter("RefNo"));
			String SoNo = StrUtils.fString(request.getParameter("SoNo"));
			String Item = StrUtils.fString(request.getParameter("Item"));

			String Terms = StrUtils.fString(request.getParameter("Terms"));
			String TermsDetails = StrUtils.fString(request.getParameter("TermsDetails"));
			String Rate = StrUtils.fString(request.getParameter("Rate"));
			String Discount = request.getParameter("Discount").trim();
			String TaxAmount = StrUtils.fString(request.getParameter("TaxAmount"));
			String Amt = StrUtils.fString(request.getParameter("Amt"));
			String Adjustment = request.getParameter("Adjustment").trim();
			String Total = request.getParameter("Total").trim();
			String ProductRatesAre = request.getParameter("ProductRatesAre").trim();

			String Description = StrUtils.fString(request.getParameter("Description"));
			String OrderQty = StrUtils.fString(request.getParameter("OrderQty"));
			String UOM = StrUtils.fString(request.getParameter("UOM"));
			String Footer1 = StrUtils.fString(request.getParameter("Footer1"));
			String Footer2 = StrUtils.fString(request.getParameter("Footer2"));
			String Footer3 = StrUtils.fString(request.getParameter("Footer3"));
			String Footer4 = StrUtils.fString(request.getParameter("Footer4"));

			String Footer5 = StrUtils.fString(request.getParameter("Footer5"));
			String Footer6 = StrUtils.fString(request.getParameter("Footer6"));
			String Footer7 = StrUtils.fString(request.getParameter("Footer7"));
			String Footer8 = StrUtils.fString(request.getParameter("Footer8"));
			String Footer9 = StrUtils.fString(request.getParameter("Footer9"));
			String DisplayByOrdertype = (request.getParameter("DisplayByOrdertype") != null) ? "1": "0";
			// String Container=StrUtils.fString(request.getParameter("Container"));
			// String DisplayContainer = (request.getParameter("DisplayContainer") != null) ? "1" : "0";
			String printLocStock = (request.getParameter("printLocStock") != null) ? "1" : "0";
			String printCustRemarks = (request.getParameter("printCustRemarks") != null) ? "1" : "0";
			String remark1 = request.getParameter("REMARK1").trim();
			String remark2 = request.getParameter("REMARK2").trim();
			String deliverydate = request.getParameter("DeliveryDate").trim();
			String employee= request.getParameter("Employee").trim();
			String shipto = request.getParameter("ShipTo").trim();
			String companydate = request.getParameter("CompanyDate").trim();
			String companyname = request.getParameter("CompanyName").trim();
			String companystamp = request.getParameter("CompanyStamp").trim();
			String companysig = request.getParameter("CompanySig").trim();
			String printEmployee = (request.getParameter("printEmployee") != null) ? "1" : "0";
			String orderdiscount = request.getParameter("OrderDiscount").trim();
			String shippingcost = request.getParameter("ShippingCost").trim();
			String incoterm = request.getParameter("INCOTERM").trim();
			String printCustTerms = (request.getParameter("printCustTerms") != null) ? "1": "0";
			String printwithproductremarks = (request.getParameter("printwithproductremarks") != null) ? "1": "0";
			String printwithhscode = (request.getParameter("printwithhscode") != null) ? "1": "0";
			String printwithcoo = (request.getParameter("printwithcoo") != null) ? "1": "0";
			String printwithbrand = (request.getParameter("printwithbrand") != null) ? "1": "0";
			String printRoundoffTotalwithDecimal = (request.getParameter("printRoundoffTotalwithDecimal") != null) ? "1": "0";
			String printWithUENNO = (request.getParameter("printWithUENNO"));
//			String printWithUENNO = (request.getParameter("printWithUENNO") != null) ? "1": "0";
			String printWithCustomerUENNO = (request.getParameter("printWithCustomerUENNO") != null) ? "1": "0";

			String brand = request.getParameter("BRAND").trim();
			String hscode = request.getParameter("HSCODE").trim();
			String coo = request.getParameter("COO").trim();
			String rcbno = request.getParameter("RCBNO").trim();
			String customerrcbno = request.getParameter("CUSTOMERRCBNO").trim();
			
			String uenno = request.getParameter("UENNO").trim();
		    String customeruenno = request.getParameter("CUSTOMERUENNO").trim();
			
			String PreparedBy = StrUtils.fString(request.getParameter("PreparedBy"));
			String Seller = StrUtils.fString(request.getParameter("Seller"));
			String SellerSignature = StrUtils.fString(request.getParameter("SellerSignature"));
			String Buyer = StrUtils.fString(request.getParameter("Buyer"));
			String BuyerSignature = StrUtils.fString(request.getParameter("BuyerSignature"));
			String project = request.getParameter("Project").trim();
			String printwithproject = (request.getParameter("printwithproject") != null) ? "1": "0";
			String printwithProduct = (request.getParameter("printwithProduct") != null) ? "1": "0";


			String prdDeliveryDate = request.getParameter("prdDeliveryDate").trim();
			String printWithPrdDeliveryDate = (request.getParameter("printWithPrdDeliveryDate") != null) ? "1": "0";
			String printWithDeliveryDate = (request.getParameter("printWithDeliveryDate") != null) ? "1": "0";
			String printwithcompanysig = (request.getParameter("printwithcompanysig") != null) ? "1": "0";
			String printwithcompanyseal = (request.getParameter("printwithcompanyseal") != null) ? "1": "0";

			String printDetailDesc = (request.getParameter("printDetailDesc") != null) ? "1": "0";
			String Orientation = StrUtils.fString(request.getParameter("Orientation"));


			Hashtable<String,String> ht = new Hashtable<String,String>();
			ht.put(IDBConstants.PLANT, PLANT);

			StringBuffer QryUpdate = new StringBuffer(" SET ");

			if (OrderHeader.length() > 0)
			QryUpdate.append(" ORDERHEADER = '" + OrderHeader + "' ");
			if (InvoiceOrderToHeader.length() > 0)
			QryUpdate.append(", TOHEADER = '" + InvoiceOrderToHeader + "' ");
			if (FromHeader.length() > 0)
			QryUpdate.append(", FROMHEADER = '" + FromHeader + "' ");
			if (DateField.length() > 0)
			QryUpdate.append(", DATE = '" + DateField + "' ");
			if (OrderNo.length() > 0)
			QryUpdate.append(", ORDERNO = '" + OrderNo + "' ");
			if (RefNo.length() > 0)
			QryUpdate.append(", REFNO = '" + RefNo + "' ");
			if (Terms.length() > 0)
			QryUpdate.append(", TERMS = '" + Terms + "' ");
			//if (TermsDetails.length() > 0)
			QryUpdate.append(", TERMSDETAILS = '" + TermsDetails + "' ");
			if (Rate.length() > 0)
			QryUpdate.append(", RATE = '" + Rate + "' ");
			if (Discount.length() > 0)
			QryUpdate.append(", DISCOUNT = '" + Discount + "' ");
			if (TaxAmount.length() > 0)
			QryUpdate.append(", TAXAMOUNT = '" + TaxAmount + "' ");
			if (Amt.length() > 0)
			QryUpdate.append(", AMT = '" + Amt + "' ");
			if (SoNo.length() > 0)
			QryUpdate.append(", SONO = '" + SoNo + "' ");
			if (Item.length() > 0)
			QryUpdate.append(", ITEM = '" + Item + "' ");
			if (Description.length() > 0)
			QryUpdate.append(", DESCRIPTION = '" + Description + "' ");
			if (OrderQty.length() > 0)
			QryUpdate.append(", ORDERQTY = '" + OrderQty + "' ");
			if (UOM.length() > 0)
			QryUpdate.append(", UOM = '" + UOM + "' ");
			QryUpdate.append(", ADJUSTMENT ='" + Adjustment+ "' ");
			QryUpdate.append(", TOTAL ='" + Total+ "' ");
			QryUpdate.append(", PRODUCTRATESARE ='" + ProductRatesAre+ "' ");
			QryUpdate.append(", FOOTER1 = '" + Footer1 + "' ");
			QryUpdate.append(", FOOTER2 = '" + Footer2 + "' ");
			QryUpdate.append(", FOOTER3 = '" + Footer3 + "' ");
			QryUpdate.append(", FOOTER4 = '" + Footer4 + "' ");
			QryUpdate.append(", FOOTER5 = '" + Footer5 + "' ");
			QryUpdate.append(", FOOTER6 = '" + Footer6 + "' ");
			QryUpdate.append(", FOOTER7 = '" + Footer7 + "' ");
			QryUpdate.append(", FOOTER8 = '" + Footer8 + "' ");
			QryUpdate.append(", FOOTER9 = '" + Footer9 + "' ");
			QryUpdate.append(", PRINTCUSTERMS ='" + printCustTerms+ "' ");
			QryUpdate.append(", PRINTXTRADETAILS ='" + printDetailDesc+ "' ");
			QryUpdate.append(", DISPLAYBYORDERTYPE ='" + DisplayByOrdertype+ "' ");
			QryUpdate.append(", PRINTWITHBRAND ='" + printwithbrand+ "' ");
			QryUpdate.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarks+ "' ");
			QryUpdate.append(", PRITNWITHHSCODE ='" + printwithhscode+ "' ");
			QryUpdate.append(", PRITNWITHCOO ='" + printwithcoo+ "' ");
			QryUpdate.append(", PRINTROUNDOFFTOTALWITHDECIMAL ='" + printRoundoffTotalwithDecimal+ "' ");
			QryUpdate.append(", PRINTWITHPRODUCT ='" + printwithProduct+ "' ");
			QryUpdate.append(", PRINTWITHUENNO ='" + printWithUENNO+ "' ");
			QryUpdate.append(", PRINTWITHCUSTOMERUENNO ='" + printWithCustomerUENNO+ "' ");
			QryUpdate.append(", PRINTWITHCOMPANYSIG='" + printwithcompanysig+ "' ");
			QryUpdate.append(", PRINTWITHCOMPANYSEAL='" + printwithcompanyseal+ "' ");
			if (remark1.length() > 0)
			QryUpdate.append(", REMARK1 = '" + remark1 + "' ");
			if (remark2.length() > 0)
			QryUpdate.append(", REMARK2 = '" + remark2 + "' ");

			if (brand.length() > 0)
			QryUpdate.append(", BRAND = '" + brand + "' ");

			if (hscode.length() > 0)
			QryUpdate.append(", HSCODE = '" + hscode + "' ");

			if (coo.length() > 0)
			QryUpdate.append(", COO = '" + coo + "' ");

			if (rcbno.length() > 0)
			QryUpdate.append(", RCBNO = '" + rcbno + "' ");

			if (customerrcbno.length() > 0)
			QryUpdate.append(", CUSTOMERRCBNO = '" + customerrcbno + "' ");
			
			
		    if (uenno.length() > 0)
		    	QryUpdate.append(", UENNO = '" + uenno + "' ");
		    
		    if (customeruenno.length() > 0)
				QryUpdate.append(", CUSTOMERUENNO = '" + customeruenno + "' ");

			if (deliverydate.length() > 0)
			QryUpdate.append(", DELIVERYDATE = '" + deliverydate + "' ");
			if (employee.length() > 0)
			QryUpdate.append(", EMPLOYEE = '" + employee + "' ");
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
			if (printEmployee.length() > 0)
			QryUpdate.append(", PRINTEMPLOYEE = '" + printEmployee + "' ");
			if (orderdiscount.length() > 0)
			QryUpdate.append(", ORDERDISCOUNT = '" + orderdiscount + "' ");
			if (shippingcost.length() > 0)
			QryUpdate.append(", SHIPPINGCOST = '" + shippingcost + "' ");
			if (incoterm.length() > 0)
			QryUpdate.append(", INCOTERM = '" + incoterm + "' ");
			if (PreparedBy.length() > 0)
			QryUpdate.append(", PREPAREDBY = '" + PreparedBy + "' ");
			if (Seller.length() > 0)
			QryUpdate.append(", SELLER = '" + Seller + "' ");
			if (SellerSignature.length() > 0)
			QryUpdate.append(", SELLERSIGNATURE = '" + SellerSignature + "' ");
			if (Buyer.length() > 0)
			QryUpdate.append(", BUYER = '" + Buyer + "' ");
			if (BuyerSignature.length() > 0)
			QryUpdate.append(", BUYERSIGNATURE = '" + BuyerSignature + "' ");

			if (prdDeliveryDate.length() > 0)
			QryUpdate.append(", PRODUCTDELIVERYDATE = '" + prdDeliveryDate + "' ");
			QryUpdate.append(", PRINTWITHPRODUCTDELIVERYDATE ='" + printWithPrdDeliveryDate+ "' ");
			QryUpdate.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDate+ "' ");
			if (project.length() > 0)
			QryUpdate.append(", PROJECT = '" + project + "' ");

			QryUpdate.append(", PRINTWITHPROJECT ='" + printwithproject+ "' ");


			QryUpdate.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
			QryUpdate.append(", UPBY = '" + sUserId + "' ");
			QryUpdate.append(", PrintOrientation = '" + Orientation + "' ");

			try {
			toHdrUpt = dhd.updateTOReciptHeader(QryUpdate.toString(), ht, "");
			} catch (Exception e) {

			}

			try {
			if (!toHdrUpt) {

			result = "<font class = " + IConstants.FAILED_COLOR
			+ ">Failed to edit the details</font>";
			response
			.sendRedirect("jsp/editTransferTO.jsp?result=" + result);
			} else {
			result = "<font class = " + IConstants.SUCCESS_COLOR
			+ ">Consignment Order Printout edited successfully</font>";

			response
			.sendRedirect("../editconsignment/consignprintoutwithprice?result=" + result);
			}
			} catch (Exception e) {

			}

			} else if (action.equalsIgnoreCase("EDIT_AUTOPRINT_HDR")) {

				DoHdrDAO dhd = new DoHdrDAO();
				boolean poHdrUpt = false;
				String result = "";
				String Ordertype = "",OrdertypeDO="";
				HttpSession session = request.getSession();
				String PLANT = (String) session.getAttribute("PLANT");
				String sUserId = (String) session.getAttribute("LOGIN_USER");

				Ordertype = StrUtils.fString(request.getParameter("OrderType"));
				String AutoPopupDelay = StrUtils.fString(request.getParameter("AutoPopupDelay"));
				String AutoPopupOrderType = StrUtils.fString(request.getParameter("ORDERTYPE"));
			    String enableOutletAutoPrintPopup = (request.getParameter("enableOutletAutoPrintPopup") != null) ? "1": "0";
			    
				 Hashtable<String, String> ht = new Hashtable<String, String>();
					ht.put(IDBConstants.PLANT, PLANT);
					ht.put(IDBConstants.ORDERTYPE, Ordertype);
					
					StringBuffer QryUpdate = new StringBuffer(" SET ");
					if (AutoPopupDelay.length() > 0)
						QryUpdate.append(" AUTOPOPUPDELAY = '" + AutoPopupDelay + "' ");
					if (AutoPopupOrderType.length() > 0)
						QryUpdate.append(", AUTOPOPUPORDERTYPE = '" + AutoPopupOrderType + "' ");
					
					QryUpdate.append(", ENABLEOUTLETAUTOPRINTPOPUP ='" + enableOutletAutoPrintPopup+ "' ");
					try {
						poHdrUpt = dhd.updateDOReciptHeader(QryUpdate.toString(),
								ht, "");
					} catch (Exception e) {

					}
						
					try {
						if (!poHdrUpt) {
							result = "<font class = " + IConstants.FAILED_COLOR
							+ ">Failed to edit the details</font>";						    
							response.sendRedirect("../editsales/autoprintconfig?result="
									+ result);
						} else {
								result = "<font class = "
									+ IConstants.SUCCESS_COLOR
									+ ">Auto Order Alert edited successfully</font>";
								response
								.sendRedirect("../editsales/autoprintconfig?result="
										+ result);
							
						}
					} catch (Exception e) {

					}
			}
			
				//---Added by Bruhan on March 20 2014, Description: For Edit Work Order PrintOut --ManufacturingModuelsChange
                /*else if (action.equalsIgnoreCase("EDIT_WO_RCPT_HDR")) {

      				WorkOrderHdrDAO whd = new WorkOrderHdrDAO();
      				boolean woHdrUpt = false;
      				String result = "";
      				String Ordertype = "";
      				HttpSession session = request.getSession();
      				String PLANT = (String) session.getAttribute("PLANT");
      				String sUserId = (String) session.getAttribute("LOGIN_USER");

      				String OutboundOrderHeader = StrUtils.fString(request.getParameter("WorkOrderHeader"));
      				String InvoiceOrderToHeader = StrUtils.fString(request.getParameter("InvoiceOrderToHeader"));
      				String FromHeader = StrUtils.fString(request.getParameter("FromHeader"));
      				String DateField = StrUtils.fString(request.getParameter("Date"));
      				String OrderNo = StrUtils.fString(request.getParameter("OrderNo"));
      				String RefNo = StrUtils.fString(request.getParameter("RefNo"));
      				String SoNo = StrUtils.fString(request.getParameter("SoNo"));
      				String Item = StrUtils.fString(request.getParameter("Item"));
      				String Description = StrUtils.fString(request.getParameter("Description"));
      				String OrderQty = StrUtils.fString(request.getParameter("OrderQty"));
      				String UOM = StrUtils.fString(request.getParameter("UOM"));
      				String Footer1 = StrUtils.fString(request.getParameter("Footer1"));
      				String Footer2 = StrUtils.fString(request.getParameter("Footer2"));
      				String Footer3 = StrUtils.fString(request.getParameter("Footer3"));
      				String Footer4 = StrUtils.fString(request.getParameter("Footer4"));
      				String Orientation = StrUtils.fString(request.getParameter("Orientation"));
      		       // String printBarcode = (request.getParameter("printBarcode") != null) ? "1": "0";
                    String printDetailDesc = (request.getParameter("printDetailDesc") != null) ? "1": "0";
      			   
                    Hashtable<String, String> ht = new Hashtable<String, String>();
      				ht.put(IDBConstants.PLANT, PLANT);
   			
      				StringBuffer QryUpdate = new StringBuffer(" SET ");
      				if (OutboundOrderHeader.length() > 0)
      					QryUpdate.append(" ORDERHEADER = '" + OutboundOrderHeader
      							+ "' ");
      				if (InvoiceOrderToHeader.length() > 0)
      					QryUpdate.append(", TOHEADER = '" + InvoiceOrderToHeader
      							+ "' ");
      				if (FromHeader.length() > 0)
      					QryUpdate.append(", FROMHEADER = '" + FromHeader + "' ");
      				if (DateField.length() > 0)
      					QryUpdate.append(", DATE = '" + DateField + "' ");
      				if (OrderNo.length() > 0)
      					QryUpdate.append(", ORDERNO = '" + OrderNo + "' ");
      				if (RefNo.length() > 0)
      					QryUpdate.append(", REFNO = '" + RefNo + "' ");
      				if (SoNo.length() > 0)
      					QryUpdate.append(", SONO = '" + SoNo + "' ");
      				if (Item.length() > 0)
      					QryUpdate.append(", ITEM = '" + Item + "' ");
      				if (Description.length() > 0)
      					QryUpdate.append(", DESCRIPTION = '" + Description + "' ");
      				if (OrderQty.length() > 0)
      					QryUpdate.append(", ORDERQTY = '" + OrderQty + "' ");
      				if (UOM.length() > 0)
      					QryUpdate.append(", UOM = '" + UOM + "' ");

      				QryUpdate.append(", FOOTER1 = '" + Footer1 + "' ");
      				QryUpdate.append(", FOOTER2 = '" + Footer2 + "' ");
      				QryUpdate.append(", FOOTER3 = '" + Footer3 + "' ");
      				QryUpdate.append(", FOOTER4 = '" + Footer4 + "' ");
      			    //QryUpdate.append(", PRINTBARCODE ='" + printBarcode+ "' ");
                    QryUpdate.append(", PRINTXTRADETAILS ='" + printDetailDesc+ "' ");
                    QryUpdate.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
      				QryUpdate.append(", UPBY = '" + sUserId + "' ");
      				QryUpdate.append(", PrintOrientation = '" + Orientation + "' ");

      				try {
      					woHdrUpt = whd.updateWOPrintReciptHeader(QryUpdate.toString(),ht, "");
      				} catch (Exception e) {

      				}

      				try {
      					 if (!woHdrUpt) {

    						result = "<font class = " + IConstants.FAILED_COLOR
    								+ ">Failed to edit the details</font>";
    						response
    								.sendRedirect("jsp/editWorkOrderWO.jsp?result=" + result);
    					} else {
    						result = "<font class = " + IConstants.SUCCESS_COLOR
    								+ ">Work Order Printout edited successfully</font>";

    						response
    								.sendRedirect("jsp/editWorkOrderWO.jsp?result=" + result);
    					}
      					
      				} catch (Exception e) {

      				}
      			}*/
	
	
	else if (action.equalsIgnoreCase("EDIT_EO_RCPT_INVOICE_HDR")) {

		EstHdrDAO ehd = new EstHdrDAO();
		boolean eoHdrUpt = false;
		String result = "";
		HttpSession session = request.getSession();
		String PLANT = (String) session.getAttribute("PLANT");
		String sUserId = (String) session.getAttribute("LOGIN_USER");
       	String EstimateOrderHeader = StrUtils.fString(request
				.getParameter("EstimateOrderHeader"));
		String InvoiceOrderToHeader = StrUtils.fString(request
				.getParameter("InvoiceOrderToHeader"));
		String FromHeader = StrUtils.fString(request
				.getParameter("FromHeader"));
		String DateField = StrUtils.fString(request
				.getParameter("Date"));
		String OrderNo = StrUtils.fString(request
				.getParameter("OrderNo"));
		String RefNo = StrUtils.fString(request.getParameter("RefNo"));
		String Terms = StrUtils.fString(request.getParameter("Terms"));
		String TermsDetails = StrUtils.fString(request
				.getParameter("TermsDetails"));
		String SoNo = StrUtils.fString(request.getParameter("SoNo"));
		String Item = StrUtils.fString(request.getParameter("Item"));
		String Description = StrUtils.fString(request
				.getParameter("Description"));
		String OrderQty = StrUtils.fString(request
				.getParameter("OrderQty"));
		String UOM = StrUtils.fString(request.getParameter("UOM"));

		String Rate = StrUtils.fString(request.getParameter("Rate"));
		String TaxAmount = StrUtils.fString(request
				.getParameter("TaxAmount"));
		String Amt = StrUtils.fString(request.getParameter("Amt"));

		String SubTotal = StrUtils.fString(request
				.getParameter("SubTotal"));
		String TotalTax = StrUtils.fString(request
				.getParameter("TotalTax"));
		String Total = StrUtils.fString(request.getParameter("Total"));
		String Roundoff = StrUtils.fString(request.getParameter("Roundoff"));
		

		String Footer1 = StrUtils.fString(request
				.getParameter("Footer1"));
		String Footer2 = StrUtils.fString(request
				.getParameter("Footer2"));
		String Footer3 = StrUtils.fString(request
				.getParameter("Footer3"));
		String Footer4 = StrUtils.fString(request
				.getParameter("Footer4"));
		String Footer5 = StrUtils.fString(request
				.getParameter("Footer5"));
		String Footer6 = StrUtils.fString(request
				.getParameter("Footer6"));
		String Footer7 = StrUtils.fString(request
				.getParameter("Footer7"));
		String Footer8 = StrUtils.fString(request
				.getParameter("Footer8"));
		String Footer9 = StrUtils.fString(request
				.getParameter("Footer9"));
		String Orientation = StrUtils.fString(request.getParameter("Orientation"));
		
		String printDetailDesc = (request.getParameter("printDetailDesc") != null) ? "1": "0";   
	    String printCustTerms = (request.getParameter("printCustTerms") != null) ? "1": "0";
	    String printCustRemarks = (request.getParameter("printCustRemarks") != null) ? "1": "0";
	    String printwithproductremarks = (request.getParameter("printwithproductremarks") != null) ? "1": "0";
	    String printwithbrand = (request.getParameter("printwithbrand") != null) ? "1": "0";
        String PRINTWITHREFNO = (request.getParameter("PRINTWITHREFNO") != null) ? "1": "0";
	    
	    String Adjustment = request.getParameter("Adjustment").trim();
	    String ProductRatesAre = request.getParameter("ProductRatesAre").trim();
	    
//	    RESVI STARTS
	    String project = request.getParameter("Project").trim();
	    String printwithproject = (request.getParameter("printwithproject") != null) ? "1": "0";
	    String PRINTWITHSHIPINGADD = (request.getParameter("PRINTWITHSHIPINGADD") != null) ? "1": "0";
	    String PRINTORDERDISCOUNT = (request.getParameter("PRINTORDERDISCOUNT") != null) ? "1": "0";
	    String PRINTSHIPPINGCOST = (request.getParameter("PRINTSHIPPINGCOST") != null) ? "1": "0";
	    String PRINTADJUSTMENT = (request.getParameter("PRINTADJUSTMENT") != null) ? "1": "0";
	    String printwithcompanysig = (request.getParameter("printwithcompanysig") != null) ? "1": "0";
	    String printwithcompanyseal = (request.getParameter("printwithcompanyseal") != null) ? "1": "0";
//	    RESVI ENDS  
	    String printwithemployee = (request.getParameter("printwithemployee") != null) ? "1": "0";
		String DisplayByOrdertype = (request.getParameter("DisplayByOrdertype") != null) ? "1": "0";
		String printwithhscode = (request.getParameter("printwithhscode") != null) ? "1": "0";
		String printwithcoo = (request.getParameter("printwithcoo") != null) ? "1": "0";
	    String remark1 = request.getParameter("REMARK1").trim();
	    String remark2 = request.getParameter("REMARK2").trim();
	    String deliverydate = request.getParameter("DeliveryDate").trim();
	    String orderdiscount = request.getParameter("OrderDiscount").trim();
	    String shippingcost = request.getParameter("ShippingCost").trim();
	    String incoterm = request.getParameter("INCOTERM").trim();
	    String rcbno = request.getParameter("RCBNO").trim();
	    String customerrcbno = request.getParameter("CUSTOMERRCBNO").trim();
	    
	    String uenno = request.getParameter("UENNO").trim();
	    String customeruenno = request.getParameter("CUSTOMERUENNO").trim();
	    
	    String employeename = request.getParameter("EMPLOYEENAME").trim();
	    String brand = request.getParameter("BRAND").trim();
	    String hscode = request.getParameter("HSCODE").trim();
	    String coo = request.getParameter("COO").trim();
	    String companyname = request.getParameter("COMPANYNAME").trim();
	    String companydate = request.getParameter("COMPANYDATE").trim();
	    String companysig = request.getParameter("COMPANYSIG").trim();
	    String companystamp = request.getParameter("COMPANYSTAMP").trim();
	    String totalafterdiscount = request.getParameter("TOTALAFTERDISCOUNT").trim();
	    String shipto = request.getParameter("SHIPTO").trim();
	    String PreparedBy = request.getParameter("PreparedBy").trim();
	    String AuthSignature = request.getParameter("AuthSignature").trim();
		String printRoundoffTotalwithDecimal = (request.getParameter("printRoundoffTotalwithDecimal") != null) ? "1": "0";
        String printwithProduct = (request.getParameter("printwithProduct") != null) ? "1": "0";
		String printDiscountReport = (request.getParameter("printDiscountReport") != null) ? "1": "0"; 
	    String Discount = request.getParameter("Discount").trim();
		String NetRate = request.getParameter("NetRate").trim();
		
		String prdDeliveryDate = request.getParameter("prdDeliveryDate").trim();
	    String printWithPrdDeliveryDate = (request.getParameter("printWithPrdDeliveryDate") != null) ? "1": "0";
	    String printWithDeliveryDate = (request.getParameter("printWithDeliveryDate") != null) ? "1": "0";
	    String calculateTaxwithShippingCost = (request.getParameter("calculateTaxwithShippingCost") != null) ? "1": "0";
	    String printWithUENNO = (request.getParameter("printWithUENNO"));
//	    String printWithUENNO = (request.getParameter("printWithUENNO") != null) ? "1": "0";
	    String printWithCustomerUENNO = (request.getParameter("printWithCustomerUENNO") != null) ? "1": "0";
	   	
	    Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put(IDBConstants.PLANT, PLANT);
	    
		StringBuffer QryUpdate = new StringBuffer(" SET ");

		if (EstimateOrderHeader.length() > 0)
			QryUpdate.append(" ORDERHEADER = '" + EstimateOrderHeader
					+ "' ");
		if (InvoiceOrderToHeader.length() > 0)
			QryUpdate.append(", TOHEADER = '" + InvoiceOrderToHeader
					+ "' ");
		if (FromHeader.length() > 0)
			QryUpdate.append(", FROMHEADER = '" + FromHeader + "' ");
		if (DateField.length() > 0)
			QryUpdate.append(", DATE = '" + DateField + "' ");
		if (OrderNo.length() > 0)
			QryUpdate.append(", ORDERNO = '" + OrderNo + "' ");
		if (RefNo.length() > 0)
			QryUpdate.append(", REFNO = '" + RefNo + "' ");
		if (Terms.length() > 0)
			QryUpdate.append(", TERMS = '" + Terms + "' ");
		//if (TermsDetails.length() > 0)
			QryUpdate.append(", TERMSDETAILS = '" + TermsDetails + "' ");
		if (SoNo.length() > 0)
			QryUpdate.append(", SONO = '" + SoNo + "' ");
		if (Item.length() > 0)
			QryUpdate.append(", ITEM = '" + Item + "' ");
		if (Description.length() > 0)
			QryUpdate.append(", DESCRIPTION = '" + Description + "' ");
		if (OrderQty.length() > 0)
			QryUpdate.append(", ORDERQTY = '" + OrderQty + "' ");
		if (UOM.length() > 0)
			QryUpdate.append(", UOM = '" + UOM + "' ");
		if (Rate.length() > 0)
			QryUpdate.append(", RATE = '" + Rate + "' ");
		if (TaxAmount.length() > 0)
			QryUpdate.append(", TAXAMOUNT = '" + TaxAmount + "' ");
		if (Amt.length() > 0)
			QryUpdate.append(", AMT = '" + Amt + "' ");
		if (SubTotal.length() > 0)
			QryUpdate.append(", SubTotal = '" + SubTotal + "' ");
		if (TotalTax.length() > 0)
			QryUpdate.append(", TotalTax = '" + TotalTax + "' ");
		if (Total.length() > 0)
			QryUpdate.append(", Total = '" + Total + "' ");
		if (Roundoff.length() > 0)
			QryUpdate.append(", ROUNDOFFTOTALWITHDECIMAL = '" + Roundoff + "' ");
		if (remark1.length() > 0)
			QryUpdate.append(", REMARK1 = '" + remark1 + "' ");
	    if (remark2.length() > 0)
			QryUpdate.append(", REMARK2 = '" + remark2 + "' ");
	    if (deliverydate.length() > 0)
			QryUpdate.append(", DELIVERYDATE = '" + deliverydate + "' ");
	    
	    if (orderdiscount.length() > 0)
			QryUpdate.append(", ORDERDISCOUNT = '" + orderdiscount + "' ");
	    if (shippingcost.length() > 0)
			QryUpdate.append(", SHIPPINGCOST = '" + shippingcost + "' ");
	    if (incoterm.length() > 0)
			QryUpdate.append(", INCOTERM = '" + incoterm + "' ");
	    if (rcbno.length() > 0)
			QryUpdate.append(", RCBNO = '" + rcbno + "' ");

	    if (customerrcbno.length() > 0)
			QryUpdate.append(", CUSTOMERRCBNO = '" + customerrcbno + "' ");
	    
	    if (uenno.length() > 0)
	    	QryUpdate.append(", UENNO = '" + uenno + "' ");
	    
	    if (customeruenno.length() > 0)
			QryUpdate.append(", CUSTOMERUENNO = '" + customeruenno + "' ");
	    
	    if (totalafterdiscount.length() > 0)
			QryUpdate.append(", TOTALAFTERDISCOUNT = '" + totalafterdiscount + "' ");  
	    
	    if (shipto.length() > 0)
			QryUpdate.append(", SHIPTO = '" + shipto + "' ");   
	    
	    if (rcbno.length() > 0)
			QryUpdate.append(", EMPLOYEENAME = '" + employeename + "' ");
	    if (brand.length() > 0)
			QryUpdate.append(", BRAND = '" + brand + "' ");
	    if (hscode.length() > 0)
			QryUpdate.append(", HSCODE = '" + hscode + "' ");
	    if (coo.length() > 0)
			QryUpdate.append(", COO = '" + coo + "' ");
	    if (companyname.length() > 0)
			QryUpdate.append(", COMPANYNAME = '" + companyname + "' ");
	    if (companydate.length() > 0)
			QryUpdate.append(", COMPANYDATE = '" + companydate + "' ");
	    if (companysig.length() > 0)
			QryUpdate.append(", COMPANYSIG = '" + companysig + "' ");
	    if (companystamp.length() > 0)
			QryUpdate.append(", COMPANYSTAMP = '" + companystamp + "' ");

		QryUpdate.append(", DISPLAYBYORDERTYPE ='" + DisplayByOrdertype+ "' ");
		QryUpdate.append(", FOOTER1 = '" + Footer1 + "' ");
		QryUpdate.append(", FOOTER2 = '" + Footer2 + "' ");
		QryUpdate.append(", FOOTER3 = '" + Footer3 + "' ");
		QryUpdate.append(", FOOTER4 = '" + Footer4 + "' ");
		QryUpdate.append(", FOOTER5 = '" + Footer5 + "' ");
		QryUpdate.append(", FOOTER6 = '" + Footer6 + "' ");
		QryUpdate.append(", FOOTER7 = '" + Footer7 + "' ");
		QryUpdate.append(", FOOTER8 = '" + Footer8 + "' ");
		QryUpdate.append(", FOOTER9 = '" + Footer9 + "' ");
		QryUpdate.append(", PRINTXTRADETAILS ='" + printDetailDesc+ "' ");
		QryUpdate.append(", PRINTWITHEMPLOYEE ='" + printwithemployee+ "' ");
	    QryUpdate.append(", PRINTCUSTERMS ='" + printCustTerms+ "' ");
	    QryUpdate.append(", PCUSREMARKS ='" + printCustRemarks+ "' ");
	    QryUpdate.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarks+ "' ");
	    QryUpdate.append(", PRINTWITHBRAND ='" + printwithbrand+ "' ");
	    QryUpdate.append(", PRINTWITHHSCODE ='" + printwithhscode+ "' ");
	    QryUpdate.append(", PRINTWITHCOO ='" + printwithcoo+ "' ");
		QryUpdate.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
		QryUpdate.append(", UPBY = '" + sUserId + "' ");
		QryUpdate.append(", PrintOrientation = '" + Orientation + "' ");
		
		if (PreparedBy.length() > 0)
			QryUpdate.append(", PREPAREDBY = '" + PreparedBy + "' ");
	    if (AuthSignature.length() > 0)
			QryUpdate.append(", AUTHSIGNATURE = '" + AuthSignature + "' ");
			
		QryUpdate.append(", PRINTROUNDOFFTOTALWITHDECIMAL ='" + printRoundoffTotalwithDecimal+ "' ");
		QryUpdate.append(", PRINTWITHPRODUCT ='" + printwithProduct+ "' ");
		QryUpdate.append(", PRINTWITHDISCOUNT ='" + printDiscountReport+ "' ");
		if (Discount.length() > 0)
			QryUpdate.append(", DISCOUNT = '" + Discount + "' ");
		if (NetRate.length() > 0)
			QryUpdate.append(", NETRATE = '" + NetRate + "' ");
		
		if (prdDeliveryDate.length() > 0)
			QryUpdate.append(", PRODUCTDELIVERYDATE = '" + prdDeliveryDate + "' ");	
	    QryUpdate.append(", PRINTWITHPRODUCTDELIVERYDATE ='" + printWithPrdDeliveryDate+ "' ");
	    QryUpdate.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDate+ "' ");
	    QryUpdate.append(", CALCULATETAXWITHSHIPPINGCOST ='" + calculateTaxwithShippingCost+ "' ");
//	    RESVI STARTS
		if (project.length() > 0)
			QryUpdate.append(", PROJECT = '" + project + "' ");	
		
		QryUpdate.append(", PRINTWITHPROJECT ='" + printwithproject+ "' ");
		QryUpdate.append(", PRINTWITHSHIPINGADD ='" + PRINTWITHSHIPINGADD+ "' ");
		QryUpdate.append(", PRINTORDERDISCOUNT ='" + PRINTORDERDISCOUNT+ "' ");
		QryUpdate.append(", PRINTSHIPPINGCOST ='" + PRINTSHIPPINGCOST+ "' ");
		QryUpdate.append(", PRINTADJUSTMENT ='" + PRINTADJUSTMENT+ "' ");
		QryUpdate.append(", PRINTWITHUENNO ='" + printWithUENNO+ "' ");
		QryUpdate.append(", PRINTWITHCOMPANYSIG='" + printwithcompanysig+ "' ");
		QryUpdate.append(", PRINTWITHCOMPANYSEAL='" + printwithcompanyseal+ "' ");
		QryUpdate.append(", PRINTWITHCUSTOMERUENNO ='" + printWithCustomerUENNO+ "' ");
//     RESVI ENDS
		QryUpdate.append(", ADJUSTMENT ='" + Adjustment+ "' ");
		QryUpdate.append(", PRODUCTRATESARE ='" + ProductRatesAre+ "' ");
		QryUpdate.append(", PRINTWITHREFNO ='" + PRINTWITHREFNO+ "' ");
		try {
			eoHdrUpt = ehd.updateEOReciptInvoiceHeader(QryUpdate.toString(), ht, "");
		} catch (Exception e) {

		}

		try {
			if (!eoHdrUpt) {

				result = "<font class = " + IConstants.FAILED_COLOR
				+ ">Failed to edit the details</font>";
			 }else {
				result = "<font class = "
					+ IConstants.SUCCESS_COLOR
					+ ">Sales Estimate Order With Price Printout edited successfully</font>";

				response
				.sendRedirect("../editsales/salesestimateorderprintout?result="+ result);
			}
		} catch (Exception e) {

		}
	}

	else if (action.equalsIgnoreCase("EDIT_PAYMENT_RCPT_HDR")) {

		OrderPaymentDAO pmtdao = new OrderPaymentDAO();
		boolean pmtUpt = false;
		String result = "";
		HttpSession session = request.getSession();
		String PLANT = (String) session.getAttribute("PLANT");
		String sUserId = (String) session.getAttribute("LOGIN_USER");
       	String HEADER = StrUtils.fString(request.getParameter("HEADER"));
       	String ReceivedFrom = StrUtils.fString(request.getParameter("RECEIVEDFROM"));
		String Date = StrUtils.fString(request.getParameter("Date"));
		String PaymentMode = StrUtils.fString(request.getParameter("PaymentMode"));
		String ReferenceNo = StrUtils.fString(request.getParameter("ReferenceNo"));
		String InvoiceNumber = StrUtils.fString(request.getParameter("InvoiceNumber"));
		String InvoiceDate = StrUtils.fString(request.getParameter("InvoiceDate"));
		String DueDate = StrUtils.fString(request.getParameter("DueDate"));
		String OriginalAmount = StrUtils.fString(request.getParameter("OriginalAmount"));
		String Balance = StrUtils.fString(request.getParameter("Balance"));
		String Payment = StrUtils.fString(request.getParameter("Payment"));
		String AmountCredited = StrUtils.fString(request.getParameter("AmountCredited"));
		String Total = StrUtils.fString(request.getParameter("Total"));
		String Memo = StrUtils.fString(request.getParameter("Memo"));
		String Signature = StrUtils.fString(request.getParameter("Signature"));
					
	    Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put(IDBConstants.PLANT, PLANT);
	    
		StringBuffer QryUpdate = new StringBuffer(" SET ");

		if (HEADER.length() > 0)
			QryUpdate.append(" HEADER = '" + HEADER + "' ");
		if (ReceivedFrom.length() > 0)
			QryUpdate.append(", RECEIVEDFROM = '" + ReceivedFrom + "' ");
		if (Date.length() > 0)
			QryUpdate.append(", PMTDATE = '" + Date + "' ");
		if (PaymentMode.length() > 0)
			QryUpdate.append(", PAYMENTMODE = '" + PaymentMode + "' ");
		if (ReferenceNo.length() > 0)
			QryUpdate.append(", REFERENCENO = '" + ReferenceNo + "' ");
		if (InvoiceNumber.length() > 0)
			QryUpdate.append(", INVOICENUMBER = '" + InvoiceNumber + "' ");
		if (InvoiceDate.length() > 0)
			QryUpdate.append(", INVOICEDATE = '" + InvoiceDate + "' ");
		if (DueDate.length() > 0)
			QryUpdate.append(", DUEDATE = '" + DueDate + "' ");
		if (OriginalAmount.length() > 0)
			QryUpdate.append(", ORIGINALAMOUNT = '" + OriginalAmount + "' ");
		if (Balance.length() > 0)
			QryUpdate.append(", BALANCE = '" + Balance + "' ");
		if (Payment.length() > 0)
			QryUpdate.append(", PAYMENT = '" + Payment + "' ");
		if (AmountCredited.length() > 0)
			QryUpdate.append(", AMOUNTCREDITED = '" + AmountCredited + "' ");
		if (Total.length() > 0)
			QryUpdate.append(", TOTAL = '" + Total + "' ");
		if (Memo.length() > 0)
			QryUpdate.append(", MEMO = '" + Memo + "' ");
		if (Signature.length() > 0)
			QryUpdate.append(", SIGNATURE = '" + Signature + "' ");
		 
		QryUpdate.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
		QryUpdate.append(", UPBY = '" + sUserId + "' ");

		try {
			pmtUpt = pmtdao.updatePaymentReciptHeader(QryUpdate.toString(), ht, "");
		} catch (Exception e) {

		}

		try {
			if (!pmtUpt) {

				result = "<font class = " + IConstants.FAILED_COLOR
				+ ">Failed to edit the details</font>";
			 }else {
				result = "<font class = "
					+ IConstants.SUCCESS_COLOR
					+ ">Payment Printout edited successfully</font>";

				response
				.sendRedirect("jsp/editPaymentRcptHdr.jsp?result="+ result);
			}
		} catch (Exception e) {

		}
	}
			
		} catch (Exception ex) {
			this.mLogger.exception(this.printLog, "", ex);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
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

}
