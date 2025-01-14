package com.track.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.MovHisDAO;
import com.track.dao.GstTypeDAO;
import com.track.dao.POSHdrDAO;
import com.track.dao.PrdClassDAO;
import com.track.db.util.GstTypeUtil;

import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONObject;

public class GstTypeServlet extends HttpServlet implements IMLogger {
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private boolean printLog = MLoggerConstant.GstTypeServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.GstTypeServlet_PRINTPLANTMASTERINFO;
	String action = "";
	DateUtils dateutils = new DateUtils();
	GstTypeUtil _GstTypeUtil = null;
	GstTypeDAO _GstTypeDAO = null;
	

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_GstTypeUtil = new GstTypeUtil();
		_GstTypeDAO = new GstTypeDAO();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			action = request.getParameter("action").trim();
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			_GstTypeUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			JSONObject jsonObjectResult = new JSONObject();
			if (action.equalsIgnoreCase("New")) {
				response.sendRedirect("jsp/LocMst_View.jsp?action=NEW");
			}

			else if (action.equalsIgnoreCase("ADD")) {

				String result = addGstType(request, response);
				
				response.sendRedirect("../gst/summary?result=GST/VAT Detail Added Successfully");

			} else if (action.equalsIgnoreCase("UPDATE")) {

				String result = updateGstType(request, response);
				response
						.sendRedirect("../gst/summary?result=GST/VAT Detail Updated Successfully"
								);
			}

			else if (action.equalsIgnoreCase("DELETE")) {

				String result = DeleteGstType(request, response);
				response
						.sendRedirect("../gst/summary?result=GST/VAT Detail Deleted Successfully"
								);
			}
                        
			
			    else if(action.equalsIgnoreCase("EDIT_POS_ISSUE_HDR")){
	                String result= "";
		            POSHdrDAO posHdr =  new POSHdrDAO();
		            boolean posHsrUpt=false;
		            String header = request.getParameter("HEADER").trim();
		            String PaymentMode = request.getParameter("PaymentMode").trim(); 
		            String SalesRep = request.getParameter("SalesRep").trim();   
		            String SerialNo = request.getParameter("SerialNo").trim();   
		            String ReceiptNo = request.getParameter("ReceiptNo").trim();
		            String Product = request.getParameter("Product").trim();  
		            String Description = request.getParameter("Description").trim();   
		            String UnitPrice = request.getParameter("UnitPrice").trim();   
		            String Qty = request.getParameter("Qty").trim();   
		            String UOM = request.getParameter("UOM").trim();
		            String Discount = request.getParameter("Discount").trim();   
		            String Total = request.getParameter("Total").trim();   
		            String Subtotal = request.getParameter("Subtotal").trim();   
		            String Tax = request.getParameter("Tax").trim();   
		            String TotalAmt = request.getParameter("TotalAmt").trim();   
		            String PaymentPaid = request.getParameter("PaymentPaid").trim();   
		            String ChangeRemaining = request.getParameter("ChangeRemaining").trim();   
		            String Orientation = request.getParameter("Orientation").trim();   
	                        
		            String greet1 = request.getParameter("GREET1").trim();   
		            String greet2 = request.getParameter("GREET2").trim();   
		            String foot1 = request.getParameter("FOOT1").trim();   
		            String foot2 = request.getParameter("FOOT2").trim(); 
		            //String DisplayByLoc = (request.getParameter("DisplayByLoc") != null) ? "1": "0";
		            String DisplayByLoc = request.getParameter("DisplayByLoc");
		            String PrintWRcpt = (request.getParameter("PrintWRcpt") != null) ? "1": "0";
	                        
		            StringBuffer QryUpdate = new StringBuffer(" SET ");

		            if (header.length() > 0)
		                    QryUpdate.append(" HEADER = '" + header + "' ");
		            if (PaymentMode.length() > 0)
		                    QryUpdate.append(", PAYMENT_MODE = '" + PaymentMode + "' ");
		            if (SalesRep.length() > 0)
		                    QryUpdate.append(", SALES_REP = '" + SalesRep + "' ");
		            if (SerialNo.length() > 0)
		                    QryUpdate.append(", SERIAL_NO = '" + SerialNo + "' ");
		            if (ReceiptNo.length() > 0)
	                    QryUpdate.append(", RECEIPT_NO1 = '" + ReceiptNo + "' ");
		            if (Product.length() > 0)
	                    QryUpdate.append(", PRODUCT  = '" + Product + "' ");
		            if (Description.length() > 0)
		                    QryUpdate.append(", PROD_DESC  = '" + Description + "' ");
		            if (UnitPrice.length() > 0)
		                    QryUpdate.append(", UNIT_PRICE  = '" + UnitPrice + "' ");
		            if (Qty.length() > 0)
		                    QryUpdate.append(", QTY  = '" + Qty + "' ");
		            if (UOM.length() > 0)
	                    QryUpdate.append(", UNITMO  = '" + UOM + "' ");
		            if (Discount.length() > 0)
		                    QryUpdate.append(", DISCOUNT  = '" + Discount + "' ");
		            if (Total.length() > 0)
		                    QryUpdate.append(", TOTAL  = '" + Total + "' ");
		            if (Subtotal.length() > 0)
		                    QryUpdate.append(", SUBTOTAL  = '" + Subtotal + "' ");
		            if (Tax.length() > 0)
		                    QryUpdate.append(", TAX  = '" + Tax + "' ");
		            if (TotalAmt.length() > 0)
		                    QryUpdate.append(", TOTAL_AMT  = '" + TotalAmt + "' ");
		            if (PaymentPaid.length() > 0)
		                    QryUpdate.append(", AMOUNT_PAID  = '" + PaymentPaid + "' ");
		            if (ChangeRemaining.length() > 0)
		                QryUpdate.append(", CHANGE  = '" + ChangeRemaining + "' ");
		            QryUpdate.append(", PrintOrientation = '" + Orientation + "' ");
		            
		                QryUpdate.append(", GREETINGS1 = '" + greet1 + "' ");
		                QryUpdate.append(", GREETINGS2 = '" + greet2 + "' ");
		                QryUpdate.append(", FOOTER1 = '" + foot1 + "' ");
		                QryUpdate.append(", FOOTER2 = '" + foot2 + "' ");
		        		QryUpdate.append(", ADDRBYLOC ='" +DisplayByLoc+ "' ");
		        		QryUpdate.append(", DOWNLOADPDF ='" +PrintWRcpt+ "' ");
	                            
		             Hashtable ht = new Hashtable();
		             ht.put(IDBConstants.PLANT,plant);
		            
		             try{
		                 posHsrUpt= posHdr.updatePOSRECIPTHeader(QryUpdate.toString(),ht," ");
		             }catch(Exception e){
		                 
		             }
		            System.out.println("action :: :"+posHsrUpt);
		            if(!posHsrUpt){
	                        
		                result = "<font class = " + IConstants.FAILED_COLOR
		                                + ">Failed to edit the details</font>";
		                response.sendRedirect("../editgoods/goodsissueprintout?result="+ result);
						//RequestDispatcher view = request.getRequestDispatcher("jsp/editPosIssueHdr.jsp?result="+ result);
			            //view.forward(request, response);
		            }else{
		                result = "<font class = " + IConstants.SUCCESS_COLOR
		                                + ">Goods Issue Header Updated Successfully</font>";
		              
		                response.sendRedirect("../editgoods/goodsissueprintout?result="+ result);
						//RequestDispatcher view = request.getRequestDispatcher("jsp/editPosIssueHdr.jsp?result="+ result);
			            //view.forward(request, response);
		            }
		            
		        
		        }
			
			    else if(action.equalsIgnoreCase("EDIT_POS_RECEIVE_HDR")){
            String result= "";
            POSHdrDAO posHdr =  new POSHdrDAO();
            boolean posHsrUpt=false;
            String header = request.getParameter("HEADER").trim();
            String PaymentMode = request.getParameter("PaymentMode").trim(); 
            String SalesRep = request.getParameter("SalesRep").trim();   
            String SerialNo = request.getParameter("SerialNo").trim();   
            String ReceiptNo = request.getParameter("ReceiptNo").trim();
            String Product = request.getParameter("Product").trim();  
            String Description = request.getParameter("Description").trim();   
            String UnitCost = request.getParameter("UnitCost").trim();   
            String Qty = request.getParameter("Qty").trim(); 
            String UOM = request.getParameter("UOM").trim();
            String Discount = request.getParameter("Discount").trim();   
            String Total = request.getParameter("Total").trim();   
            String Subtotal = request.getParameter("Subtotal").trim();   
            String Tax = request.getParameter("Tax").trim();   
            String TotalAmt = request.getParameter("TotalAmt").trim();   
            String PaymentPaid = request.getParameter("PaymentPaid").trim();   
            String ChangeRemaining = request.getParameter("ChangeRemaining").trim();   
            String Orientation = request.getParameter("Orientation").trim();    
                    
            String greet1 = request.getParameter("GREET1").trim();   
            String greet2 = request.getParameter("GREET2").trim();   
            String foot1 = request.getParameter("FOOT1").trim();   
            String foot2 = request.getParameter("FOOT2").trim(); 
          //  String DisplayByLoc = (request.getParameter("DisplayByLoc") != null) ? "1": "0";
            String DisplayByLoc = request.getParameter("DisplayByLoc");
            String PrintWRcpt = (request.getParameter("PrintWRcpt") != null) ? "1": "0";
                    
            StringBuffer QryUpdate = new StringBuffer(" SET ");

            if (header.length() > 0)
                    QryUpdate.append(" HEADER = '" + header + "' ");
            if (PaymentMode.length() > 0)
                    QryUpdate.append(", PAYMENT_MODE = '" + PaymentMode + "' ");
            if (SalesRep.length() > 0)
                    QryUpdate.append(", SALES_REP = '" + SalesRep + "' ");
            if (SerialNo.length() > 0)
                    QryUpdate.append(", SERIAL_NO = '" + SerialNo + "' ");
            if (ReceiptNo.length() > 0)
                QryUpdate.append(", RECEIPT_NO = '" + ReceiptNo + "' ");
            if (Product.length() > 0)
                QryUpdate.append(", PRODUCT  = '" + Product + "' ");
            if (Description.length() > 0)
                    QryUpdate.append(", PROD_DESC  = '" + Description + "' ");
            if (UnitCost.length() > 0)
                    QryUpdate.append(", UNIT_COST  = '" + UnitCost + "' ");
            if (Qty.length() > 0)
                    QryUpdate.append(", QTY  = '" + Qty + "' ");
            if (UOM.length() > 0)
                QryUpdate.append(", UNITMO  = '" + UOM + "' ");
            if (Discount.length() > 0)
                    QryUpdate.append(", DISCOUNT  = '" + Discount + "' ");
            if (Total.length() > 0)
                    QryUpdate.append(", TOTAL  = '" + Total + "' ");
            if (Subtotal.length() > 0)
                    QryUpdate.append(", SUBTOTAL  = '" + Subtotal + "' ");
            if (Tax.length() > 0)
                    QryUpdate.append(", TAX  = '" + Tax + "' ");
            if (TotalAmt.length() > 0)
                    QryUpdate.append(", TOTAL_AMT  = '" + TotalAmt + "' ");
            if (PaymentPaid.length() > 0)
                    QryUpdate.append(", AMOUNT_PAID  = '" + PaymentPaid + "' ");
            if (ChangeRemaining.length() > 0)
                QryUpdate.append(", CHANGE  = '" + ChangeRemaining + "' ");
           
            QryUpdate.append(", PrintOrientation = '" + Orientation + "' ");
                QryUpdate.append(", GREETINGS1 = '" + greet1 + "' ");
                QryUpdate.append(", GREETINGS2 = '" + greet2 + "' ");
                QryUpdate.append(", FOOTER1 = '" + foot1 + "' ");
                QryUpdate.append(", FOOTER2 = '" + foot2 + "' ");
        		QryUpdate.append(", ADDRBYLOC ='" +DisplayByLoc+ "' ");
        		QryUpdate.append(", DOWNLOADPDF ='" +PrintWRcpt+ "' ");
                        
             Hashtable ht = new Hashtable();
             ht.put(IDBConstants.PLANT,plant);
            
             try{
                 posHsrUpt= posHdr.updatePOSReceiveRECIPTHeader(QryUpdate.toString(),ht," ");
             }catch(Exception e){
                 
             }
            System.out.println("action :: :"+posHsrUpt);
            if(!posHsrUpt){
                    
                result = "<font class = " + IConstants.FAILED_COLOR
                                + ">Failed to edit the details</font>";
                response.sendRedirect("../editgoods/goodsreceiptprintout?result="+ result);
				//RequestDispatcher view = request.getRequestDispatcher("jsp/editPosReceiveHdr.jsp?result="+ result);
	            //view.forward(request, response);
            }else{
                result = "<font class = " + IConstants.SUCCESS_COLOR
                                + ">Goods Receipt Header Updated Successfully</font>";
              
                response.sendRedirect("../editgoods/goodsreceiptprintout?result="+ result);
				//RequestDispatcher view = request.getRequestDispatcher("jsp/editPosReceiveHdr.jsp?result="+ result);
	            //view.forward(request, response);
            }
            
        
		} 
			    else if(action.equalsIgnoreCase("EDIT_POS_SHIFT_CLOSE_RECEIPT")){
		            String result= "";
		            POSHdrDAO posshiftclose =  new POSHdrDAO();
		            boolean posHsrUpt=false;
		            String[] chkdDoNo = request.getParameterValues("chkdDoNo");
		            String PRINTWITHPRODUCT = (request.getParameter("PRINTWITHPRODUCT") != null) ? "1": "0";
		            String PRINTWITHCATEGORY = (request.getParameter("PRINTWITHCATEGORY") != null) ? "1": "0";
		            String PRINTWITHBRAND = (request.getParameter("PRINTWITHBRAND") != null) ? "1": "0";
		            String PRINTWITHCLOSINGSTOCK = (request.getParameter("PRINTWITHCLOSINGSTOCK") != null) ? "1": "0";
		                    
		            StringBuffer QryUpdate = new StringBuffer(" SET ");

		            
		        		QryUpdate.append(" PRINTWITHPRODUCT ='" +PRINTWITHPRODUCT+ "' ");
		        		QryUpdate.append(", PRINTWITHCATEGORY ='" +PRINTWITHCATEGORY+ "' ");
		        		QryUpdate.append(", PRINTWITHBRAND ='" +PRINTWITHBRAND+ "' ");
		        		QryUpdate.append(", PRINTWITHCLOSINGSTOCK ='" +PRINTWITHCLOSINGSTOCK+ "' ");
		                        
		             Hashtable ht = new Hashtable();
		             ht.put(IDBConstants.PLANT,plant);
		            
		             try{
		                 posHsrUpt= posshiftclose.updatePOSShiftCloseReceiveRECIPT(QryUpdate.toString(),ht," ");
		                 
		                 //insert POS category
		                 boolean flag = new PrdClassDAO().deletePOSCategory(plant);
		                 if(PRINTWITHCATEGORY.equals("1")) {
		         			for (int i = 0; i < chkdDoNo.length; i++) {
		                     String AssignedLoc = StrUtils.fString(request.getParameter("chkdDoNo")).trim();
		                     AssignedLoc = chkdDoNo[i];
		                                 
		         			ht = new Hashtable();
		         			ht.put("PLANT", plant);
		         			ht.put("PRD_CLS_ID", AssignedLoc);
		         			ht.put("CRAT", dateutils.getDateTime());
		         			ht.put("CRBY", userName);
		         		    request.getSession().setAttribute("POSCategory", ht);
		         			
		         			MovHisDAO mdao = new MovHisDAO(plant);
		         			mdao.setmLogger(mLogger);
		         			Hashtable htm = new Hashtable();
		         			htm.put("PLANT", plant);
		         			htm.put("DIRTYPE", TransactionConstants.CREATE_POS_PRDCLS);
		         			htm.put("RECID", "");
		         			htm.put("LOC", AssignedLoc);
		         			htm.put("CRBY", userName);
		         			htm.put("CRAT", dateutils.getDateTime());
		         			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		                     
		         			flag = new PrdClassDAO().insertPOSCategory(ht);
		         			boolean inserted = mdao.insertIntoMovHis(htm);
		         			if (flag && inserted) {
		         				result = "<font class = " + IConstants.SUCCESS_COLOR
		         						+ ">POS Category Saved Successfully</font>";
		         			} else {
		         				result = "<font class = " + IConstants.FAILED_COLOR
		         						+ ">POS Category Save failed</font>";
		         			}
		                     } 
		         		    System.out.println("POS Category flag ::"+flag);		         			
		                 }
		                 
		             }catch(Exception e){
		                 
		             }
		            System.out.println("action :: :"+posHsrUpt);
		            if(!posHsrUpt){
		                    
		                result = "<font class = " + IConstants.FAILED_COLOR
		                                + ">Failed to edit the details</font>";
		                response.sendRedirect("../editgoods/goodsshiftclosereceiptprintout?result="+ result);
		            }else{
		                result = "<font class = " + IConstants.SUCCESS_COLOR
		                                + ">POS Shift Close Receipt Printout Updated Successfully</font>";
		              
		                response.sendRedirect("../editgoods/goodsshiftclosereceiptprintout?result="+ result);
		            }
		            
		        
				}
			    else if(action.equalsIgnoreCase("EDIT_POS_MOVE_HDR")){
            String result= "";
            POSHdrDAO posHdr =  new POSHdrDAO();
            boolean posHsrUpt=false;
            String header = request.getParameter("HEADER").trim();
            String PaymentMode = request.getParameter("PaymentMode").trim(); 
            String SalesRep = request.getParameter("SalesRep").trim();   
            String SerialNo = request.getParameter("SerialNo").trim();   
            String ReceiptNo = request.getParameter("ReceiptNo").trim();
            String Product = request.getParameter("Product").trim();  
            String Description = request.getParameter("Description").trim();   
            String UnitCost = request.getParameter("UnitCost").trim();   
            String Qty = request.getParameter("Qty").trim(); 
            String UOM = request.getParameter("UOM").trim();
            String Discount = request.getParameter("Discount").trim();   
            String Total = request.getParameter("Total").trim();   
            String Subtotal = request.getParameter("Subtotal").trim();   
            String Tax = request.getParameter("Tax").trim();   
            String TotalAmt = request.getParameter("TotalAmt").trim();   
            String PaymentPaid = request.getParameter("PaymentPaid").trim();   
            String ChangeRemaining = request.getParameter("ChangeRemaining").trim();   
            String Orientation = request.getParameter("Orientation").trim();    
                    
            String greet1 = request.getParameter("GREET1").trim();   
            String greet2 = request.getParameter("GREET2").trim();   
            String foot1 = request.getParameter("FOOT1").trim();   
            String foot2 = request.getParameter("FOOT2").trim(); 
          //  String DisplayByLoc = (request.getParameter("DisplayByLoc") != null) ? "1": "0";
            String DisplayByLoc = request.getParameter("DisplayByLoc");
            String PrintWRcpt = (request.getParameter("PrintWRcpt") != null) ? "1": "0";
                    
            StringBuffer QryUpdate = new StringBuffer(" SET ");

            if (header.length() > 0)
                    QryUpdate.append(" HEADER = '" + header + "' ");
            if (PaymentMode.length() > 0)
                    QryUpdate.append(", PAYMENT_MODE = '" + PaymentMode + "' ");
            if (SalesRep.length() > 0)
                    QryUpdate.append(", SALES_REP = '" + SalesRep + "' ");
            if (SerialNo.length() > 0)
                    QryUpdate.append(", SERIAL_NO = '" + SerialNo + "' ");
            if (ReceiptNo.length() > 0)
                QryUpdate.append(", RECEIPT_NO = '" + ReceiptNo + "' ");
            if (Product.length() > 0)
                QryUpdate.append(", PRODUCT  = '" + Product + "' ");
            if (Description.length() > 0)
                    QryUpdate.append(", PROD_DESC  = '" + Description + "' ");
            if (UnitCost.length() > 0)
                    QryUpdate.append(", UNIT_COST  = '" + UnitCost + "' ");
            if (Qty.length() > 0)
                    QryUpdate.append(", QTY  = '" + Qty + "' ");
            if (UOM.length() > 0)
                QryUpdate.append(", UNITMO  = '" + UOM + "' ");
            if (Discount.length() > 0)
                    QryUpdate.append(", DISCOUNT  = '" + Discount + "' ");
            if (Total.length() > 0)
                    QryUpdate.append(", TOTAL  = '" + Total + "' ");
            if (Subtotal.length() > 0)
                    QryUpdate.append(", SUBTOTAL  = '" + Subtotal + "' ");
            if (Tax.length() > 0)
                    QryUpdate.append(", TAX  = '" + Tax + "' ");
            if (TotalAmt.length() > 0)
                    QryUpdate.append(", TOTAL_AMT  = '" + TotalAmt + "' ");
            if (PaymentPaid.length() > 0)
                    QryUpdate.append(", AMOUNT_PAID  = '" + PaymentPaid + "' ");
            if (ChangeRemaining.length() > 0)
                QryUpdate.append(", CHANGE  = '" + ChangeRemaining + "' ");
           
            QryUpdate.append(", PrintOrientation = '" + Orientation + "' ");
                QryUpdate.append(", GREETINGS1 = '" + greet1 + "' ");
                QryUpdate.append(", GREETINGS2 = '" + greet2 + "' ");
                QryUpdate.append(", FOOTER1 = '" + foot1 + "' ");
                QryUpdate.append(", FOOTER2 = '" + foot2 + "' ");
        		QryUpdate.append(", ADDRBYLOC ='" +DisplayByLoc+ "' ");
        		QryUpdate.append(", DOWNLOADPDF ='" +PrintWRcpt+ "' ");
                        
             Hashtable ht = new Hashtable();
             ht.put(IDBConstants.PLANT,plant);
            
             try{
                 posHsrUpt= posHdr.updatePOSMoveHeader(QryUpdate.toString(),ht," ");
             }catch(Exception e){
                 
             }
            System.out.println("action :: :"+posHsrUpt);
            if(!posHsrUpt){
                    
                result = "<font class = " + IConstants.FAILED_COLOR
                                + ">Failed to edit the details</font>";
                response.sendRedirect("../editgoods/stockmoveprintout?result="+ result);
				//RequestDispatcher view = request.getRequestDispatcher("jsp/editPosMoveHdr.jsp?result="+ result);
	            //view.forward(request, response);
            }else{
                result = "<font class = " + IConstants.SUCCESS_COLOR
                                + ">Stock Move Header Updated Successfully</font>";
              
                response.sendRedirect("../editgoods/stockmoveprintout?result="+ result);
				//RequestDispatcher view = request.getRequestDispatcher("jsp/editPosMoveHdr.jsp?result="+ result);
	            //view.forward(request, response);
            }
            
        
        }else if(action.equalsIgnoreCase("ADD_GST")) {
        	jsonObjectResult = this.addGstType(request);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
        }	
		} catch (Exception ex) {
			this.mLogger.exception(this.printLog, "", ex);
			String result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Exception : " + ex.getMessage() + "</font>";
			response
					.sendRedirect("jsp/view_gst_type.jsp?action=SHOW_RESULT_VALUE&result="
							+ result);

		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private String addGstType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		    String result = "";
		    boolean	 exitFlag =false;
		
		try {
			HttpSession session = request.getSession();
			String plant = (String) session.getAttribute("PLANT");
			String user = (String) session.getAttribute("LOGIN_USER");
			

			String gsttype = request.getParameter("GSTTYPE").trim();
			String gstdesc = request.getParameter("GSTDESC").trim();
			String gstpercentage = request.getParameter("GSTPERCENTAGE").trim();
			String remarks = request.getParameter("REMARKS").trim();
			

			Hashtable htIsExist = new Hashtable();
			htIsExist.put("PLANT", plant);
			htIsExist.put("GSTTYPE", gsttype);

			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
		
			
			ht.put("GSTTYPE", gsttype);
			ht.put("GSTDESC", gstdesc);
			ht.put("GSTPERCENTAGE", gstpercentage);
			ht.put("REMARKS", remarks);
			
			

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.GSTTYPE_ADD);
			htm.put("RECID", "");
			htm.put("CRBY", user);
			htm.put("REMARKS", remarks);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
			
			
			
			exitFlag = _GstTypeDAO.isExisit(htIsExist, "", plant);
			
			if(!exitFlag)
			{
				
				boolean flag = _GstTypeUtil.addGstType(ht);
				
				boolean inserted = mdao.insertIntoMovHis(htm);
				
				if (flag && inserted) {
					request.getSession().setAttribute("gstTypeDataAdd", ht);
					result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">GST/VAT Type Added Successfully</font>";

				} else {
					result = "<font class = " + IConstants.FAILED_COLOR
						+ "> GST/VAT Type Added Failed</font>";
				
				}
			}
			else
			{
				request.getSession().setAttribute("gstTypeDataAdd", ht);
				result = "<font class = " + IConstants.FAILED_COLOR
					+ ">GST/VAT Type Already Exists</font>";
				
			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	private String updateGstType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String result = "", gsttype = "", gstdesc = "", gstpercentage="",remarks = "",oldGsttype="";
		HttpSession session = request.getSession();
		String plant = (String) session.getAttribute("PLANT");
		String user = (String) session.getAttribute("LOGIN_USER");
		Hashtable htValues = new Hashtable();
		Hashtable htCondition = new Hashtable();
		

		try {

			gsttype = request.getParameter("GSTTYPE").trim();
			gstdesc = request.getParameter("GSTDESC").trim();
			gstpercentage = request.getParameter("GSTPERCENTAGE").trim();
			remarks = request.getParameter("REMARKS").trim();
			oldGsttype = request.getParameter("GSTTYPE_HIDDEN").trim();
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
		
			ht.put("GSTTYPE", gsttype);
			ht.put("GSTDESC", gstdesc);
			ht.put("GSTPERCENTAGE", gstpercentage);
			ht.put("REMARKS", remarks);
			
			if (gsttype.length() > 0 || !gsttype.equalsIgnoreCase(null)) {
				htValues.put("GSTTYPE", gsttype);
				
			}

			if (gstdesc.length() > 0 || !gstdesc.equalsIgnoreCase(null)) {
				htValues.put("GSTDESC", gstdesc);
				
			}
			if (gstpercentage.length() > 0 || !gstpercentage.equalsIgnoreCase(null)) {
				htValues.put("GSTPERCENTAGE", gstpercentage);
				
				
			}
			if (remarks.length() > 0 || !remarks.equalsIgnoreCase(null)) {
				htValues.put("REMARKS", remarks);
			}
			

			htCondition.put("GSTTYPE", oldGsttype);
			htCondition.put("PLANT", ht.get("PLANT"));

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.GSTTYPE_UPDATE);
			htm.put("RECID", "");
			htm.put("UPBY", user);
			htm.put("REMARKS", remarks);
			htm.put("CRBY", user);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));

			boolean flag = _GstTypeUtil.updateGstType(htValues, htCondition, " ");
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (flag && inserted) {

				request.getSession().setAttribute("gstTypeDataUpdate", ht);
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">GST/VAT Type updated Successfully</font>";

			} else {
				throw new Exception("Unable to update GST/VAT Type ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}

	private String DeleteGstType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String result = "", gsttype = "", gstdesc = "", gstpercentage="",remarks = "";
		HttpSession session = request.getSession();
		String plant = (String) session.getAttribute("PLANT");
		Hashtable htValues = new Hashtable();
		Hashtable htCondition = new Hashtable();

		try {

			gsttype= request.getParameter("GSTTYPE").trim();

			Hashtable ht = new Hashtable();

			htCondition.put("GSTTYPE", gsttype);
			htCondition.put("PLANT", plant);

			boolean flag = _GstTypeUtil.deleteGstType(gsttype, plant);

			if (flag) {
				ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("GSTTYPE", gsttype);
				ht.put("GSTDESC", gstdesc);
				ht.put("GSTPERCENTAGE", gstpercentage);
				ht.put("REMARKS", remarks);
				request.getSession().setAttribute("gstTypeData", ht);
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">GST/VAT Type Deleted Successfully</font>&"
						+ "action = show_result";

			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ ">Unable to delete GST/VAT Type</font>";
			;
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

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
	
	private JSONObject addGstType(HttpServletRequest request) throws ServletException, IOException,
			Exception {
		    String result = "";
		    boolean	 exitFlag =false;
		    JSONObject resultJson = new JSONObject();
		
		try {
			HttpSession session = request.getSession();
			String plant = (String) session.getAttribute("PLANT");
			String user = (String) session.getAttribute("LOGIN_USER");

			String gsttype = request.getParameter("GSTTYPE").trim();
			String gstdesc = request.getParameter("GSTDESC").trim();
			String gstpercentage = request.getParameter("GSTPERCENTAGE").trim();
			String remarks = request.getParameter("REMARKS").trim();

			Hashtable htIsExist = new Hashtable();
			htIsExist.put("PLANT", plant);
			htIsExist.put("GSTTYPE", gsttype);

			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			
			ht.put("GSTTYPE", gsttype);
			ht.put("GSTDESC", gstdesc);
			ht.put("GSTPERCENTAGE", gstpercentage);
			ht.put("REMARKS", remarks);

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.GSTTYPE_ADD);
			htm.put("RECID", "");
			htm.put("CRBY", user);
			htm.put("REMARKS", remarks);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			
			exitFlag = _GstTypeDAO.isExisit(htIsExist, "", plant);			
			if(!exitFlag)
			{
				boolean flag = _GstTypeUtil.addGstType(ht);				
				boolean inserted = mdao.insertIntoMovHis(htm);				
				if (flag && inserted) {
					//request.getSession().setAttribute("gstTypeDataAdd", ht);
					//result = "<font class = " + IConstants.SUCCESS_COLOR+ ">VAT Type Added Successfully</font>";
					resultJson.put("MESSAGE", "GST/VAT Type Added Successfully");
					resultJson.put("GST", gsttype+" ["+gstpercentage+"%]");
					resultJson.put("STATUS", "SUCCESS");
				} else {
					//result = "<font class = " + IConstants.FAILED_COLOR+ "> VAT Type Added Failed</font>";	
					resultJson.put("MESSAGE", "GST/VAT Type Added Failed");
					resultJson.put("STATUS", "FAIL");
				}
			}
			else
			{
				//request.getSession().setAttribute("gstTypeDataAdd", ht);
				//result = "<font class = " + IConstants.FAILED_COLOR+ ">VAT Type Already Exists</font>";		
				resultJson.put("MESSAGE", "GST/VAT Type Already Exists");
				resultJson.put("STATUS", "FAIL");
			}
		} catch (Exception e) {
			throw e;
		}
		return resultJson;
	}
}