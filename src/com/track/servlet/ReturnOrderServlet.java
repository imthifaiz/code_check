package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.BillDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.ExpensesDAO;
import com.track.dao.IntegrationsDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.ReturnOrderDAO;
import com.track.db.object.CostofgoodsLanded;
import com.track.db.util.BillUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.ReturnOrderUtil;
import com.track.db.util.TblControlUtil;
import com.track.service.Costofgoods;
import com.track.service.ShopifyService;
import com.track.serviceImplementation.CostofgoodsImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ReturnOrderServlet extends HttpServlet implements IMLogger  {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.ReturnOrderServlet_PRINTPLANTMASTERLOG;
//	private boolean printInfo = MLoggerConstant.BillingServlet_PRINTPLANTMASTERINFO;
	String action = "";
	ReturnOrderUtil roUtil = new ReturnOrderUtil();
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		action = StrUtils.fString(request.getParameter("Submit")).trim();
		JSONObject jsonObjectResult = null;
		
		try {			

			if (action.equals("GET_PURCHASE_ORDER_RETURN_DETAILS")) {
				jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getPurchaseOrderReturnDetails(request);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}else if(action.equals("PROCESS_PURCHASEORDERRETURN")){				
					process_PurchaseOrderReturn(request, response);				
			}else if (action.equals("PURCHASE_RETURN_NUMBER")) {
				jsonObjectResult = new JSONObject();
		        jsonObjectResult = this.generatePSN(request);	 
		        response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
		    }else if (action.equals("SALES_RETURN_NUMBER")) {
				jsonObjectResult = new JSONObject();
		        jsonObjectResult = this.generateSSN(request);	 
		        response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
		    }else if(action.equals("VIEW_PURCHASEORDERRETURN_SUMMARY_VIEW")){	
				jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getPoReturnview(request);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}else if (action.equals("GET_SALES_ORDER_RETURN_DETAILS")) {
				jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getSalesOrderReturnDetails(request);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}else if(action.equals("PROCESS_SALESORDERRETURN")){				
				process_SalesOrderReturn(request, response);				
			}else if(action.equals("VIEW_SALESORDERRETURN_SUMMARY_VIEW")){	
				jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getSoReturnview(request);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}else if (action.equals("ExportExcelPurchaseOrderReturnSummary")) {
//				String newHtml = "";
				HSSFWorkbook wb = new HSSFWorkbook();
//				String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				wb = this.writeToExcelPurchaseOrderReturnSummary(request, response, wb);
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				response.setHeader("Content-Disposition", "attachment; filename=PurchaseOrderReturn.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
			}else if (action.equals("ExportExcelSalesOrderReturnSummary")) {
//				String newHtml = "";
				HSSFWorkbook wb = new HSSFWorkbook();
//				String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				wb = this.writeToExcelSalesOrderReturnSummary(request, response, wb);
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				response.setHeader("Content-Disposition", "attachment; filename=SalesOrderReturn.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
			} else if (action.equals("CheckOrderno")) {
				
				String orderno = StrUtils.fString(request.getParameter("PORETURN")).trim();
				String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				jsonObjectResult = new JSONObject();
				try {
					Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT, plant);
					ht.put("PORETURN", orderno);
					if (new ReturnOrderDAO().isExisit(ht)) {
						jsonObjectResult.put("status", "100");
					} else {
						jsonObjectResult.put("status", "99");
					}
				} catch (Exception daRE) {
					jsonObjectResult.put("status", "99");
				}
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	
	
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
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
	
	private JSONObject getPurchaseOrderReturnDetails(HttpServletRequest request) {
		
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		List<Map<String, String>> returnOrderList = null;
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String pono = StrUtils.fString(request.getParameter("PONO"));
			String grno = StrUtils.fString(request.getParameter("GRNO"));
			String bill = StrUtils.fString(request.getParameter("BILL"));
			
			
			returnOrderList = roUtil.getPurchaseOrderReturnDetails(pono, grno, bill, plant);
			
			for(Map<String, String> returnOrder : returnOrderList) {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("pono", returnOrder.get("pono"));
				resultJsonInt.put("polnno", returnOrder.get("polnno"));
				resultJsonInt.put("lnstat", returnOrder.get("lnstat"));
				resultJsonInt.put("grno", returnOrder.get("grno"));
				resultJsonInt.put("bill", returnOrder.get("bill"));
				resultJsonInt.put("item", returnOrder.get("item"));
				resultJsonInt.put("itemdesc", returnOrder.get("itemdesc"));
				resultJsonInt.put("qtyor", returnOrder.get("qtyor"));
				resultJsonInt.put("qtyrc", returnOrder.get("qtyrc"));
				resultJsonInt.put("qtyrd", returnOrder.get("qtyrd"));
				resultJsonInt.put("ref", returnOrder.get("ref"));
				resultJsonInt.put("sname", returnOrder.get("sname"));
				resultJsonInt.put("loc", returnOrder.get("loc"));
				resultJsonInt.put("batch", returnOrder.get("batch"));
				resultJsonInt.put("expirydate", returnOrder.get("expirydate"));
				resultJsonInt.put("uom", returnOrder.get("uom"));
				resultJsonInt.put("UOMQTY", returnOrder.get("UOMQTY"));
				resultJsonInt.put("cstatus", returnOrder.get("CREDITNOTESSTATUS"));
				resultJsonInt.put("CustName", returnOrder.get("sname"));
				if(!returnOrder.get("BILL_STATUS").equalsIgnoreCase("CANCELLED")) {
					jsonArray.add(resultJsonInt);
				}
			}
			resultJson.put("ReturnDetails", jsonArray);
			JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
            resultJsonInt.put("ERROR_CODE", "100");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("errors", jsonArrayErr);
		} catch (Exception e) {
			resultJson.put("ReturnDetails", "");
			JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	 private void process_PurchaseOrderReturn(HttpServletRequest request,
 			HttpServletResponse response) throws ServletException, IOException,
 			Exception {
 	boolean flag = false;
 	Map receiveMaterial_HM = null;
 	String plant = "", pono = "", loc = "",polno = "",item = "",qtyor = "",reverseQTY = "",returnQty="",uomQty="",uom="";//recievedQTY="",
 		String CUST_NAME="",LOGIN_USER="",batch="",receivedQty="",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";	//ITEM_BATCH="",
 		String grno = "", bill = "", notes = "",prno ="";
// 		Boolean allChecked = false;
 		double reversQty=0;
// 		Map checkedDOS = new HashMap();
 		String REASONCODE="", REMARKS="";//sepratedtoken1 = "",
// 		Map mp = new HashMap();
 		ItemMstDAO itemMstDAO = new ItemMstDAO();
 		BillDAO billDAO = new BillDAO();
 		CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
 		POUtil _POUtil = new POUtil();
 		itemMstDAO.setmLogger(mLogger);
 		List billList = new ArrayList<>();
 		try {
 			HttpSession session = request.getSession();
 			plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
 	        String[] chkdPoNos  = request.getParameterValues("chkdPoNo");
 	        pono = StrUtils.fString(request.getParameter("PONO"));
 			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
 			REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
 		    REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"));
 		    TRANSACTIONDATE = StrUtils.fString(request.getParameter("return_date"));
 		    notes = StrUtils.fString(request.getParameter("note"));
 		    prno = StrUtils.fString(request.getParameter("prno"));
 			if (TRANSACTIONDATE.length()>5)
 				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
 			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
// 		  if( request.getParameter("select")!=null){
// 				allChecked = true;
// 			}
 			process: 	
 				if (chkdPoNos != null)    {  
 					for (int i = 0; i < chkdPoNos.length; i++){
 						String	data = chkdPoNos[i];
 						String[] chkdata = data.split(",");
 						pono = chkdata[0];
 						polno = chkdata[1];
 						grno = chkdata[2];
 						bill = chkdata[3];
 						item = chkdata[4];
 						loc = chkdata[5];
 						batch = chkdata[6];
 						qtyor = chkdata[7];
 						receivedQty = chkdata[8];
 						uomQty = chkdata[9]; 
 						CUST_NAME = chkdata[10]; 
 						uom = chkdata[11]; 
 						String lineno = polno+"_"+loc+"_"+batch;	
 						returnQty = StrUtils.fString(request.getParameter("QtyReverse_"+lineno));	
 						reversQty = Double.parseDouble(returnQty);
 						reversQty = StrUtils.RoundDB(reversQty, IConstants.DECIMALPTS);
 						reverseQTY = String.valueOf(reversQty);
 			     		if (batch.length() == 0) {
 			     			batch = "NOBATCH";
 						}
 						receiveMaterial_HM = new HashMap();
 						receiveMaterial_HM.put(IConstants.PLANT, plant);
 						receiveMaterial_HM.put(IConstants.GRNO, grno);
 						receiveMaterial_HM.put("BILL", bill);
 						receiveMaterial_HM.put(IConstants.UNITMO, uom);
 						receiveMaterial_HM.put(IConstants.ITEM, item);
 						receiveMaterial_HM.put(IConstants.ITEM_DESC, itemMstDAO.getItemDesc(plant, item));
 						receiveMaterial_HM.put(IConstants.PODET_PONUM, pono);
 						receiveMaterial_HM.put(IConstants.PODET_POLNNO, polno);
 						receiveMaterial_HM.put(IConstants.VENDOR_CODE,  customerBeanDAO.getVendorCode(plant, CUST_NAME));
 						receiveMaterial_HM.put(IConstants.LOC, loc);
 	       				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
 	       				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil.getCustCode(plant, pono));
 	       				receiveMaterial_HM.put(IConstants.BATCH, batch);
 	       				receiveMaterial_HM.put(IConstants.QTY, reverseQTY);
 	       				receiveMaterial_HM.put(IConstants.ORD_QTY, qtyor);
 	       				receiveMaterial_HM.put(IConstants.RECV_QTY, receivedQty);
 	       				receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
 	       				receiveMaterial_HM.put(IConstants.RSNDESC, REASONCODE);
 	       				receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
 	       				receiveMaterial_HM.put("RETURN_DATE", strTranDate);
 	       				receiveMaterial_HM.put("UOMQTY", uomQty);
 	       				receiveMaterial_HM.put("NOTES", notes);
 	       				receiveMaterial_HM.put("PORETURN", prno);
 	       				receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
 	        			flag = roUtil.process_PurchaseOrderReversal(receiveMaterial_HM)&& true;
 	        			
 	        			if (flag == true) {//Shopify Inventory Update
 	      					Hashtable htCond = new Hashtable();
 	      					htCond.put(IConstants.PLANT, plant);
 	      					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
 	      						String nonstkflag = new ItemMstDAO().getNonStockFlag(plant,item);						
 	   						if(nonstkflag.equalsIgnoreCase("N")) {
 	      						String availqty ="0";
 	      						ArrayList invQryList = null;
 	      						htCond = new Hashtable();
 	      						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(plant,item, new ItemMstDAO().getItemDesc(plant, item),htCond);						
 	      						if(new PlantMstDAO().getisshopify(plant)) {
 	      						if (invQryList.size() > 0) {					
 	      							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
// 	      								String result="";
 	                                   Map lineArr = (Map) invQryList.get(iCnt);
 	                                   availqty = (String)lineArr.get("AVAILABLEQTY");
 	                                   System.out.println(availqty);
 	      							}
 	      							double availableqty = Double.parseDouble(availqty);
 	          						new ShopifyService().UpdateShopifyInventoryItem(plant, item, availableqty);
 	      						}	
 	   						}
 	   						}
 	      					}
 	      				}
 				if(!flag) {
 					break process;
 				}else {
 					if(!bill.equalsIgnoreCase("")) {	
							String query = " SET CREDITNOTESSTATUS='2' ";
							Hashtable ht = new Hashtable();
							ht.put("BILL", bill);
							ht.put("PLANT", plant);
							flag = billDAO.updateBillHdr(query, ht, "");
							billList.add(bill);
					}
 				}
 			}
 		}
 			if(flag) {
 				if(billList.size()>0) {
 					Set<String> set = new HashSet<>(billList);
 					billList.clear();
 					billList.addAll(set);
 					updateCalculateLandedCost(billList,plant);
 				}
 			}
 			if (flag) {
 				new TblControlUtil().updateTblControlIESeqNoreturn(plant, "PURCHASE_RETURN", "PRN", prno);
 				request.getSession().setAttribute("RESULT",	"Purchase Order Return successfully!");
 				response.sendRedirect("../purchasereturn/summary?result=Purchase Order Return successfully!");
 			} else {
 				request.getSession().setAttribute("RESULTERROR","Failed to Return Purchase Order");
 				response.sendRedirect("../purchasereturn/new?result=Failed to Return Purchase Order&PONO="+pono+"&GRNO="+grno+"&BILLNO="+bill+"&ACTION=view");
 			}
 			
 			}
 			
 		 catch (Exception e) {
 			this.mLogger.exception(this.printLog, "", e);
 			request.getSession().setAttribute("CATCHERROR", e.getMessage());
 			System.out.print((String)request.getSession().getAttribute("CATCHERROR"));
 			response.sendRedirect("../purchasereturn/new?result=Failed to Return Purchase Order&PONO="+pono+"&GRNO="+grno+"&BILLNO="+bill+"&ACTION=view");
 			throw e;
 		}
 		
	 }
	 
	 private JSONObject generatePSN(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			try {
				com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
				_TblControlDAO.setmLogger(mLogger); 
				String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER"));
				
				String prno = _TblControlDAO.getNextOrder(plant,username,"PURCHASE_RETURN");

				if (prno.length() > 0) {
					resultJson.put("status", "100");
					JSONObject resultObjectJson = new JSONObject();
					resultObjectJson.put("prno", prno);
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
	 
	 
	 
	 private JSONObject generateSSN(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			try {
				com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
				_TblControlDAO.setmLogger(mLogger); 
				String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER"));
				
				String sorno = _TblControlDAO.getNextOrder(plant,username,"SALES_RETURN");

				if (sorno.length() > 0) {
					resultJson.put("status", "100");
					JSONObject resultObjectJson = new JSONObject();
					resultObjectJson.put("sorno", sorno);
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
	 
	 private JSONObject getPoReturnview(HttpServletRequest request) {
	 	JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        String FROM_DATE ="",  TO_DATE = "", fdate="",tdate="";
        String vendName="",orderNo="",billNo="",grno="",vendno="";//Order="",ordRefNo="",custcode="",custName="",currencyid="",
        String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        
//        BillUtil billUtil = new BillUtil();
//        AgeingUtil ageingUtil = new AgeingUtil();
        
//        ArrayList movQryList  = new ArrayList();
//        ArrayList movQrycustList  = new ArrayList();
        CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
        List<Map<String, String>> returnOrderList = null;
        try {
        	FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
            TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
            billNo = StrUtils.fString(request.getParameter("BILL"));
            vendName = StrUtils.fString(request.getParameter("CNAME"));
            orderNo = StrUtils.fString(request.getParameter("ORDERNO"));
            grno = StrUtils.fString(request.getParameter("GRNO"));
            
            
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
            String curDate =DateUtils.getDate();
            
            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
            
            if (FROM_DATE.length()>5)
            	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
            
            if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
            if (TO_DATE.length()>5)
            	tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
            
            if (vendName.length()>5)
            	vendno = customerBeanDAO.getVendorCode(plant, vendName);
            returnOrderList = roUtil.getPurchaseOrderReturnByGroupingforsummary(orderNo, grno, billNo, vendno, fdate, tdate, plant);
            
            for(Map<String, String> returnOrder : returnOrderList) {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("pono", returnOrder.get("PONO"));
				resultJsonInt.put("polnno", returnOrder.get("POLNNO"));
				resultJsonInt.put("grno", returnOrder.get("GRNO"));
				resultJsonInt.put("bill", returnOrder.get("BILL"));
				resultJsonInt.put("vname", returnOrder.get("VNAME"));
				resultJsonInt.put("return_date", returnOrder.get("RETURN_DATE"));
				resultJsonInt.put("status", returnOrder.get("STATUS"));
				resultJsonInt.put("return_qty", returnOrder.get("RETURN_QTY"));
				resultJsonInt.put("porno", returnOrder.get("PORETURN"));
				jsonArray.add(resultJsonInt);
			}
			resultJson.put("ReturnDetails", jsonArray);
			JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
            resultJsonInt.put("ERROR_CODE", "100");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("errors", jsonArrayErr);
		} catch (Exception e) {
			resultJson.put("ReturnDetails", "");
			JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson; 
	}
	 
	 private JSONObject getSalesOrderReturnDetails(HttpServletRequest request) {
			
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			List<Map<String, String>> returnOrderList = null;
			try {
				String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				String dono = StrUtils.fString(request.getParameter("DONO"));
				String invoiceno = StrUtils.fString(request.getParameter("INVOICE"));
				String gino = StrUtils.fString(request.getParameter("GINO"));
				
				
				returnOrderList = roUtil.getSalesOrderReturnDetails(dono, invoiceno, gino, plant);
				
				for(Map<String, String> returnOrder : returnOrderList) {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("dono", returnOrder.get("dono"));
					resultJsonInt.put("dolno", returnOrder.get("dolno").trim());
					resultJsonInt.put("invoice", returnOrder.get("invoice"));
					resultJsonInt.put("item", returnOrder.get("item"));
					resultJsonInt.put("itemdesc", returnOrder.get("itemdesc"));
					resultJsonInt.put("cname", returnOrder.get("cname"));
					resultJsonInt.put("qtyor", returnOrder.get("qtyor"));
					resultJsonInt.put("qtyrd", returnOrder.get("qtyrd"));
					resultJsonInt.put("qtyissue", returnOrder.get("qtyissue"));
					resultJsonInt.put("loc", returnOrder.get("loc"));
					resultJsonInt.put("batch", returnOrder.get("batch"));
					resultJsonInt.put("uom", returnOrder.get("UNITMO"));
					resultJsonInt.put("UOMQTY", returnOrder.get("UOMQTY"));
					resultJsonInt.put("cnstatus", returnOrder.get("CREDITNOTESSTATUS"));
					resultJsonInt.put("gino", returnOrder.get("gino"));
					resultJsonInt.put("CUSTCODE", returnOrder.get("CUSTCODE"));
					if(dono.length()>0) {
						resultJsonInt.put("CustName", returnOrder.get("cname"));
					}else {
						resultJsonInt.put("CustName", returnOrder.get("CustName"));
					}
					if(!returnOrder.get("BILL_STATUS").equalsIgnoreCase("CANCELLED")) {
						jsonArray.add(resultJsonInt);
					}
				}
				resultJson.put("ReturnDetails", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	            resultJsonInt.put("ERROR_CODE", "100");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("errors", jsonArrayErr);
			} catch (Exception e) {
				resultJson.put("ReturnDetails", "");
				JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			return resultJson;
		}
	 
	 private void process_SalesOrderReturn(HttpServletRequest request,
	 			HttpServletResponse response) throws ServletException, IOException,
	 			Exception {
		boolean flag = false;
		Map receiveMaterial_HM = null;
		
		String PLANT = "", DONO = "", LOC = "",dolno = "",item = "",QTYOR = "",reverseQTY = "",returnQty="",UOMQTY="",uom="",sorno="",gino;//ITEM_DESCRIPTION = "",
		String CUST_NAME="",LOGIN_USER="",batch="",issuedQty="",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="", invoice="";//ITEM_BATCH="",		
		String notes = "", ORDERNO="", INVOICENO="",CUSTCODE="";
//		Boolean allChecked = false;
		double reversQty=0;
		//UserTransaction ut = null;
//		Map checkedDOS = new HashMap();
		String REASONCODE="", REMARKS="";//sepratedtoken1 = "",
//		Map mp = new HashMap();
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		CustMstDAO custMstDAO = new CustMstDAO();
//		DOUtil _DOUtil = new DOUtil();
		itemMstDAO.setmLogger(mLogger);
			try {
				HttpSession session = request.getSession();
				PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
		        String[] chkdDoNos  = request.getParameterValues("chkdDoNo");
		        ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
		        INVOICENO = StrUtils.fString(request.getParameter("invoiceno"));
				CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
				LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
				REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
			    REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"));
			    TRANSACTIONDATE = StrUtils.fString(request.getParameter("return_date"));
			    notes = StrUtils.fString(request.getParameter("note"));
			    sorno = StrUtils.fString(request.getParameter("sorno"));
				if (TRANSACTIONDATE.length()>5)
					strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
				    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);

//				if( request.getParameter("select")!=null){
//					allChecked = true;
//				}
				
			process: 	
				if (chkdDoNos != null)    {  
	        	for (int i = 0; i < chkdDoNos.length; i++){
	        		String	data = chkdDoNos[i];
					String[] chkdata = data.split(",");
				DONO = chkdata[0];
				dolno = chkdata[1];
				invoice = chkdata[2];
				item = chkdata[3];
				LOC = chkdata[4];
				batch = chkdata[5];		    	
				QTYOR = chkdata[6];
				issuedQty = chkdata[7];
				UOMQTY = chkdata[8];
				gino = chkdata[10];
				CUSTCODE = chkdata[11];
				uom = chkdata[12];
				String lineno = dolno+"_"+LOC+"_"+batch;
				returnQty = StrUtils.fString(request.getParameter("QtyReverse_"+lineno));	
				reversQty = Double.parseDouble(returnQty);
				reversQty = StrUtils.RoundDB(reversQty, IConstants.DECIMALPTS);
				reverseQTY = String.valueOf(reversQty);
				if (batch.length() == 0) {
					batch = "NOBATCH";
				}	
					receiveMaterial_HM = new HashMap();
					receiveMaterial_HM.put(IConstants.PLANT, PLANT);
					receiveMaterial_HM.put(IConstants.ITEM, item);
					receiveMaterial_HM.put(IConstants.ITEM_DESC, itemMstDAO.getItemDesc(PLANT, item));
					receiveMaterial_HM.put("INVOICE", invoice);
					receiveMaterial_HM.put(IConstants.UNITMO, uom);
					receiveMaterial_HM.put(IConstants.DODET_DONUM, DONO);
					receiveMaterial_HM.put(IConstants.DODET_DOLNNO, dolno);
					receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
					receiveMaterial_HM.put(IConstants.LOC, LOC);			
					receiveMaterial_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_" + LOC);
					receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
					/*if(DONO.equalsIgnoreCase("")) {
						receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, "");
					}else {
						receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, DONO));
					}*/
					receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, CUSTCODE);
					receiveMaterial_HM.put(IConstants.BATCH, batch);
					receiveMaterial_HM.put(IConstants.QTY, reverseQTY);
					receiveMaterial_HM.put(IConstants.ORD_QTY, QTYOR);
					receiveMaterial_HM.put("ISSUEDQTY", issuedQty);
					receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
					receiveMaterial_HM.put(IConstants.RSNDESC, REASONCODE);
					receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
					receiveMaterial_HM.put("RETURN_DATE", strTranDate);
					receiveMaterial_HM.put("UOMQTY", UOMQTY);
					receiveMaterial_HM.put("NOTES", notes);
					receiveMaterial_HM.put("SORETURN", sorno);
					receiveMaterial_HM.put("GINO", gino);
					/*if(DONO.equalsIgnoreCase("")) {
						receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, custMstDAO.getCustomerNameBycode(PLANT, CUSTCODE));
					}else {
						receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, _DOUtil.getCustName(PLANT, DONO));
					}*/
					receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, custMstDAO.getCustomerNameBycode(PLANT, CUSTCODE));
					flag = roUtil.process_SalesOrderReversal(receiveMaterial_HM)&& true;
					
					if (flag == true) {//Shopify Inventory Update
	      					Hashtable htCond = new Hashtable();
	      					htCond.put(IConstants.PLANT, PLANT);
	      					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
	      						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,item);						
	   						if(nonstkflag.equalsIgnoreCase("N")) {
	      						String availqty ="0";
	      						ArrayList invQryList = null;
	      						htCond = new Hashtable();
	      						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,item, new ItemMstDAO().getItemDesc(PLANT, item),htCond);						
	      						if(new PlantMstDAO().getisshopify(PLANT)) {
	      						if (invQryList.size() > 0) {					
	      							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
//	      								String result="";
	                                   Map lineArr = (Map) invQryList.get(iCnt);
	                                   availqty = (String)lineArr.get("AVAILABLEQTY");
	                                   System.out.println(availqty);
	      							}
	      							double availableqty = Double.parseDouble(availqty);
	          						new ShopifyService().UpdateShopifyInventoryItem(PLANT, item, availableqty);
	      						}
	      						}
	   						}
	      					}
	      				}
					if(!flag) {
						break process;
					}else {
						if (!invoice.equalsIgnoreCase("")) {
								InvoiceDAO invoiceDAO = new InvoiceDAO();
								String  uquery = "SET CREDITNOTESSTATUS = 2";
								Hashtable conditions = new Hashtable();
								conditions.put("INVOICE", invoice);
								conditions.put("PLANT", PLANT);
								invoiceDAO.update(uquery, conditions, "");
							}
					}
				}
			}
				if (flag) {
					new TblControlUtil().updateTblControlIESeqNoreturn(PLANT, "SALES_RETURN", "SRN", sorno);
					//DbBean.CommitTran(ut);				
					request.getSession().setAttribute("RESULT","Sales Order Return successfully!");
					response.sendRedirect("../salesreturn/summary?result=Sales Order Returned successfully!");
				} else {
					//DbBean.RollbackTran(ut);
					request.getSession().setAttribute("RESULTERROR","Failed to Return Sales Order");
					response.sendRedirect("../salesreturn/new?result=Failed to Return Sales Order&ORDERNO="+ORDERNO+"&INVOICENO="+INVOICENO+"&ACTION=view");
				}
			}catch (Exception e) {
				 //DbBean.RollbackTran(ut);
				this.mLogger.exception(this.printLog, "", e);
				request.getSession().setAttribute("CATCHERROR", e.getMessage());
				System.out.print((String)request.getSession().getAttribute("CATCHERROR"));
				response.sendRedirect("../salesreturn/new?result=Failed to Return Sales Order&ORDERNO="+ORDERNO+"&INVOICENO="+INVOICENO+"&ACTION=view");
				throw e;
			}
	 }
	 
	 private JSONObject getSoReturnview(HttpServletRequest request) {
		 	JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        
	        String FROM_DATE ="",  TO_DATE = "", fdate="",tdate="";
	        String custcode="",custName="",orderNo="",invoice="",gino;//Order="",vendName="",ordRefNo="",currencyid="",grno="",
	        String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	        
//	        BillUtil billUtil = new BillUtil();
//	        AgeingUtil ageingUtil = new AgeingUtil();
	        
//	        ArrayList movQryList  = new ArrayList();
//	        ArrayList movQrycustList  = new ArrayList();
	        CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	        List<Map<String, String>> returnOrderList = null;
	        try {
	        	FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
	            TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
	            invoice = StrUtils.fString(request.getParameter("INVOICE"));
	            custName = StrUtils.fString(request.getParameter("CNAME"));
	            orderNo = StrUtils.fString(request.getParameter("ORDERNO"));
	            gino= StrUtils.fString(request.getParameter("GINO"));
	            
	            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	            String curDate =DateUtils.getDate();
	            
	            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
	            
	            if (FROM_DATE.length()>5)
	            	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
	            
	            if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	            if (TO_DATE.length()>5)
	            	tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
	            if(!custName.equalsIgnoreCase(""))
	            	custcode = customerBeanDAO.getCustomerCode(plant, custName);
	            returnOrderList = roUtil.getSalesOrderReturnByGrouping(orderNo,gino,invoice, custcode, fdate, tdate, plant);

	            for(Map<String, String> returnOrder : returnOrderList) {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("dono", returnOrder.get("DONO"));
					resultJsonInt.put("dolnno", returnOrder.get("DOLNNO"));
					resultJsonInt.put("invoice", returnOrder.get("INVOICE"));
					resultJsonInt.put("cname", returnOrder.get("CNAME"));
					resultJsonInt.put("return_date", returnOrder.get("RETURN_DATE"));
					resultJsonInt.put("status", returnOrder.get("STATUS"));
					resultJsonInt.put("return_qty", returnOrder.get("RETURN_QTY"));
					resultJsonInt.put("SORETURN", returnOrder.get("SORETURN"));
					resultJsonInt.put("gino", returnOrder.get("GINO"));
					jsonArray.add(resultJsonInt);
				}
				resultJson.put("ReturnDetails", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	            resultJsonInt.put("ERROR_CODE", "100");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("errors", jsonArrayErr);
			} catch (Exception e) {
				resultJson.put("ReturnDetails", "");
				JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			return resultJson; 
		}
	 
	 private HSSFWorkbook writeToExcelPurchaseOrderReturnSummary(HttpServletRequest request, HttpServletResponse response,
				HSSFWorkbook wb) {
		 String type="";//FROM_DATE ="",  TO_DATE = "", fdate="",tdate="", 
//	        String Order="",vendName="",ordRefNo="",custcode="",custName="",currencyid="",orderNo="",billNo="";
	        String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	        int SheetId =1;
	        int maxRowsPerSheet = 65535;
//	        BillUtil billUtil = new BillUtil();
//	        AgeingUtil ageingUtil = new AgeingUtil();
	        
//	        ArrayList movQryList  = new ArrayList();
//	        ArrayList movQrycustList  = new ArrayList();
	        CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	        List<Map<String, String>> returnOrderList = null;
	        try {
	        	HttpSession session = request.getSession();
	        	plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
//	        	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
//	        	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	        	String pono = StrUtils.fString(request.getParameter("PONO"));
	        	String grno = StrUtils.fString(request.getParameter("GRNO"));
	        	String bill = StrUtils.fString(request.getParameter("BILL"));
	        	String vendname = StrUtils.fString(request.getParameter("VEND_NAME"));
	        	String returnDate = StrUtils.fString(request.getParameter("RETURNDATE"));
	        	type = "PURCHASE_ORDER";
	        	String vendno =customerBeanDAO.getVendorCode(plant, vendname);
	        	
	            returnOrderList = roUtil.getPOReturnDetails(pono, grno, bill, vendno, returnDate, plant);
//	            Boolean workSheetCreated = true;
	            if(returnOrderList.size() > 0) {
	            	HSSFSheet sheet = null;
					HSSFCellStyle styleHeader = null;
					HSSFCellStyle dataStyle = null;
//					HSSFCellStyle dataStyleSpecial = null;
					HSSFCellStyle CompHeader = null;
					dataStyle = createDataStyle(wb);
//					dataStyleSpecial = createDataStyle(wb);
					sheet = wb.createSheet("Sheet"+SheetId);
					styleHeader = createStyleHeader(wb);
					CompHeader = createCompStyleHeader(wb);
					sheet = this.createWidthReturnSummary(sheet, type);
					sheet = this.createHeaderReturnSummary(sheet, styleHeader, type);
					 sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",plant);
//					CreationHelper createHelper = wb.getCreationHelper();

					int index = 2, sno = 0;
//					double orderPriceSubTot = 0, issPriceSubTot = 0, unitcost = 0, Cost = 0, tax = 0, CostwTax = 0;
//					float gstpercentage = 0;
//					String strDiffQty = "", deliverydateandtime = "";
					
					for(Map<String, String> returnOrder : returnOrderList) {
						int k = 0;
						
						HSSFRow row = sheet.createRow(index);

						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(sno + 1);
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) returnOrder.get("PONO"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) returnOrder.get("GRNO"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) returnOrder.get("BILL"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) returnOrder.get("RETURN_DATE"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) returnOrder.get("RETURN_QTY"))));
						cell.setCellStyle(dataStyle);
						
						index++;
						 if((index-2)%maxRowsPerSheet==0){
							 index = 2;
							 SheetId++;
							 sheet = wb.createSheet("Sheet"+SheetId);
							 styleHeader = createStyleHeader(wb);
							 CompHeader = createCompStyleHeader(wb);
							 sheet = this.createWidthReturnSummary(sheet, type);
							 sheet = this.createHeaderReturnSummary(sheet, styleHeader, type);
							 sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",plant);
						}
					}					
	            } else if (returnOrderList.size() < 1) {
					System.out.println("No Records Found To List");
				}
	        } catch (Exception e) {
	        	this.mLogger.exception(this.printLog, "", e);
			}
	        return wb;
	 }
	 
	 private HSSFWorkbook writeToExcelSalesOrderReturnSummary(HttpServletRequest request, HttpServletResponse response,
				HSSFWorkbook wb) {
		 String type="";//FROM_DATE ="",  TO_DATE = "", fdate="",tdate="", 
//	        String Order="",vendName="",ordRefNo="",custcode="",custName="",currencyid="",orderNo="",billNo="";
	        String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	        int SheetId =1;
	        int maxRowsPerSheet = 65535;
//	        BillUtil billUtil = new BillUtil();
//	        AgeingUtil ageingUtil = new AgeingUtil();
	        
//	        ArrayList movQryList  = new ArrayList();
//	        ArrayList movQrycustList  = new ArrayList();
	        CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	        List<Map<String, String>> returnOrderList = null;
	        try {
	        	HttpSession session = request.getSession();
	        	plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
//	        	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
//	        	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	        	String dono = StrUtils.fString(request.getParameter("DONO"));
	        	String invoice = StrUtils.fString(request.getParameter("INVOICE"));
	        	String custname = StrUtils.fString(request.getParameter("CUST_NAME"));
	        	String returnDate = StrUtils.fString(request.getParameter("RETURNDATE"));
	        	type = "SALES_ORDER";
	        	String custno =customerBeanDAO.getCustomerCode(plant, custname);
	        	returnOrderList = roUtil.getSOReturnDetails(dono, invoice, custno, returnDate, plant);
//	            Boolean workSheetCreated = true;
	            if(returnOrderList.size() > 0) {
	            	HSSFSheet sheet = null;
					HSSFCellStyle styleHeader = null;
					HSSFCellStyle dataStyle = null;
//					HSSFCellStyle dataStyleSpecial = null;
					HSSFCellStyle CompHeader = null;
					dataStyle = createDataStyle(wb);
//					dataStyleSpecial = createDataStyle(wb);
					sheet = wb.createSheet("Sheet"+SheetId);
					styleHeader = createStyleHeader(wb);
					CompHeader = createCompStyleHeader(wb);
					sheet = this.createWidthReturnSummary(sheet, type);
					sheet = this.createHeaderReturnSummary(sheet, styleHeader, type);
					 sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",plant);
//					CreationHelper createHelper = wb.getCreationHelper();

					int index = 2, sno = 0;
//					double orderPriceSubTot = 0, issPriceSubTot = 0, unitcost = 0, Cost = 0, tax = 0, CostwTax = 0;
//					float gstpercentage = 0;
//					String strDiffQty = "", deliverydateandtime = "";
					
					for(Map<String, String> returnOrder : returnOrderList) {
						int k = 0;
						
						HSSFRow row = sheet.createRow(index);

						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(sno + 1);
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) returnOrder.get("DONO"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) returnOrder.get("INVOICE"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) returnOrder.get("RETURN_DATE"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) returnOrder.get("ITEM"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) returnOrder.get("RETURN_QTY"))));
						cell.setCellStyle(dataStyle);
						
						index++;
						 if((index-2)%maxRowsPerSheet==0){
							 index = 2;
							 SheetId++;
							 sheet = wb.createSheet("Sheet"+SheetId);
							 styleHeader = createStyleHeader(wb);
							 CompHeader = createCompStyleHeader(wb);
							 sheet = this.createWidthReturnSummary(sheet, type);
							 sheet = this.createHeaderReturnSummary(sheet, styleHeader, type);
							 sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",plant);
						}
					}					
	            } else if (returnOrderList.size() < 1) {
					System.out.println("No Records Found To List");
				}
	        } catch (Exception e) {
	        	this.mLogger.exception(this.printLog, "", e);
			}
	        return wb;
	 }
	 
	 private HSSFCellStyle createDataStyle(HSSFWorkbook wb) {
			// Create style
			HSSFCellStyle dataStyle = wb.createCellStyle();
			dataStyle.setWrapText(true);
			return dataStyle;
	}
	 
	 private HSSFCellStyle createStyleHeader(HSSFWorkbook wb) {

			// Create style
			HSSFCellStyle styleHeader = wb.createCellStyle();
			HSSFFont fontHeader = wb.createFont();
			fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			fontHeader.setFontName("Arial");
			styleHeader.setFont(fontHeader);
			styleHeader.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			styleHeader.setWrapText(true);
			return styleHeader;
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
	 
	 private HSSFSheet createWidthReturnSummary(HSSFSheet sheet, String type) {
			int i = 0;
			try {
				sheet.setColumnWidth(i++, 1000);
				if (type.equals("PURCHASE_ORDER")) {
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 6000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 3500);
				}else if(type.equals("SALES_ORDER")){
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 3500);
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
	 }
	 
	 private HSSFSheet createHeaderReturnSummary(HSSFSheet sheet, HSSFCellStyle styleHeader, String type) {
			int k = 0;
			try {

				HSSFRow rowhead = sheet.createRow(1);

				HSSFCell cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("S/N"));
				cell.setCellStyle(styleHeader);
				
				if (type.equals("PURCHASE_ORDER")) {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Order No"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("GRNO"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Bill"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Return Date"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Return Qty"));
					cell.setCellStyle(styleHeader);
				}else if(type.equals("SALES_ORDER")){
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Order No"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Invoice"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Return Date"));
					cell.setCellStyle(styleHeader);

					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Product"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Return Qty"));
					cell.setCellStyle(styleHeader);
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
	 
	 private HSSFSheet createHeaderCompanyReports(HSSFSheet sheet, HSSFCellStyle styleHeader,String reportType,String PLANT){
			int k = 0;
			try{
				String CNAME = "", CZIP = "", CCOUNTRY = "", CSTATE="";// CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",COL1="",COL2="" ;
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


	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE,userCode);
		return loggerDetailsHasMap;

	}
	
	private boolean updateCalculateLandedCost(List billList, String plant) {
		boolean flag = true;
		ItemMstDAO itemmstDao = new ItemMstDAO();
		Hashtable ht = null;
		String ConvertedUnitCost="",ConvertedAmount_Aor="",sNetQty;//ConvertedAmount_Aod="",returnQty="",ConvertedAmount="",qty="",Converteddiscount="",ConvertedUnitCost_Aod="",
		try {
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
		for(int k =0; k< billList.size(); k++) {
			String bill = (String)billList.get(k);		
			String orderdiscount = "", discount="", discountType="", shippingCost="",
					pono="", shipRef="", billHdrId="";//orddiscounttype="", 
			double expenseAmt=0.0;
			Map billDetail = new BillDAO().getBillDetailsByBill(bill, plant);
			
			orderdiscount = (String)billDetail.get("ORDER_DISCOUNT");
//			orddiscounttype = (String)billDetail.get("ORDERDISCOUNTTYPE");
			discount = (String)billDetail.get("DISCOUNT");
			discountType = (String)billDetail.get("DISCOUNT_TYPE");
			shippingCost = (String)billDetail.get("SHIPPINGCOST");
			pono = (String)billDetail.get("PONO");
			shipRef = (String)billDetail.get("SHIPMENT_CODE");
			billHdrId = (String)billDetail.get("ID");
			
			ht=new Hashtable();
		    ht.put("BILLHDRID", billDetail.get("ID"));
		    ht.put("PLANT", plant);
			List listQry = new BillUtil().getBillDetByHdrId(ht);
			List item = new ArrayList(), netQty = new ArrayList(), amount_aor = new ArrayList(),
					cost = new ArrayList();
			if (listQry.size() > 0) {
				for(int i =0; i<listQry.size(); i++) {   
					Map m=(Map)listQry.get(i);
		    		
//					JSONObject resultJsonInt = new JSONObject();
					
					double dCost = Double.parseDouble((String)m.get("COST"));
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);
					
					double dQty = Double.parseDouble((String)m.get("QTY"));
//					qty = StrUtils.addZeroes(dQty, "3");
					
					double dReturnQty = Double.parseDouble((String)m.get("RETURN_QTY"));
//					returnQty = StrUtils.addZeroes(dReturnQty, "3");
					
//					double ddiscount = Double.parseDouble((String)m.get("DISCOUNT"));
//					Converteddiscount = StrUtils.addZeroes(ddiscount, numberOfDecimal);					
					
					double dAmount = Double.parseDouble((String)m.get("AMOUNT"));
//					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
					
//					double dUnitCost_Aod = Double.parseDouble((String)m.get("UNITCOST_AOD"));
//					ConvertedUnitCost_Aod = StrUtils.addZeroes(dUnitCost_Aod, numberOfDecimal);
					
//					double dAmount_Aod = dUnitCost_Aod * dQty;
//					ConvertedAmount_Aod = StrUtils.addZeroes(dAmount_Aod, numberOfDecimal);
					
					double dNetQty = dQty - dReturnQty;
					sNetQty = StrUtils.addZeroes(dNetQty, numberOfDecimal);
					
					double dAmount_Aor = (dAmount/dQty) * (dNetQty); /*Amount_AfterOrderReturn*/
					ConvertedAmount_Aor = StrUtils.addZeroes(dAmount_Aor, numberOfDecimal);
					
					item.add(m.get("ITEM"));
					netQty.add(sNetQty);
					amount_aor.add(ConvertedAmount_Aor);
					cost.add(ConvertedUnitCost);
		    	}
				
				ExpensesDAO expenseDao=new ExpensesDAO();
				CoaDAO coaDao=new CoaDAO();
				List<CostofgoodsLanded> landedCostLst=new ArrayList<>();
				List<String> expensesId=expenseDao.getExpesesHDR(pono, plant,shipRef);
				boolean isBasedOnCost=false,isBasedOnWeight=false;
				List<CostofgoodsLanded> expensesAccount = new ArrayList<CostofgoodsLanded>();
				if(expensesId.size() > 0) {
					expensesAccount=expenseDao.getExpesesDET(expensesId, plant);
					List<CostofgoodsLanded> listofLanded=coaDao.getCOAByName(expensesAccount, plant);
					for(int i=0;i<expensesAccount.size();i++) {
						expenseAmt+=expensesAccount.get(i).getAmount();
					}
				
					for(int i=0;i<expensesAccount.size();i++) {
						for(int j=0;j<listofLanded.size();j++) {
							if(expensesAccount.get(i).getAccount_name().equalsIgnoreCase(listofLanded.get(j).getAccount_name())) {
								expensesAccount.get(i).setLandedcostcal(listofLanded.get(j).getLandedcostcal());
							}
						}
					}
					for(int i=0;i<expensesAccount.size();i++) {
						if(expensesAccount.get(i).getLandedcostcal().equals("1")) {
							isBasedOnCost=true;
						}if(expensesAccount.get(i).getLandedcostcal().equals("2")) {
							isBasedOnWeight=true;
						}
					}
				}
				
				Costofgoods costofGoods=new CostofgoodsImpl();
				int itemCnt=0;
				Double avg_rate=0.0;				
				Double weightedQty= costofGoods.calculateWeightedQty(item,netQty,plant);
				Double subtotal=costofGoods.getProductSubtotalAmount(amount_aor);
				for(int i=0;i<item.size();i++) {
					itemCnt=item.size();
					CostofgoodsLanded costof=new CostofgoodsLanded();
					costof.setProd_id((String)item.get(i));
					CostofgoodsLanded weight=itemmstDao.getItemMSTDetails((String)item.get(i), plant);
					costof.setWeight(weight.getNet_weight());
					costof.setQuantity(Double.parseDouble((String)netQty.get(i)));
					costof.setWeight_qty(weightedQty);
					
					CostofgoodsLanded reqObj=new CostofgoodsLanded();
					reqObj.setOrderdiscount(Double.parseDouble(orderdiscount));
					reqObj.setDiscount(Double.parseDouble(discount));
					reqObj.setDiscountType(discountType);
					reqObj.setShippingCharge(Double.parseDouble(shippingCost));
					reqObj.setLstamount(amount_aor);
					reqObj.setSub_total(subtotal);
					reqObj.setAmount(Double.parseDouble((String)amount_aor.get(i)));
					System.out.println(reqObj.toString());
					Double amt=costofGoods.calculateIndividualAmount(reqObj,itemCnt,i);
					costof.setAmount(amt);
					Double totalCost=costofGoods.calculateTotalCost(item,reqObj);
					costof.setTotal_cost(totalCost);
					
					if("%".equals(reqObj.getDiscountType())) {
						reqObj.setAmount(amt);
						reqObj.setTotal_cost(totalCost);
						reqObj.setOrderdiscount(Double.parseDouble(orderdiscount));
						amt=costofGoods.calculateIndividualAmountForOrderDiscount(reqObj,itemCnt,i);
						costof.setAmount(amt);
						totalCost=costofGoods.calculateTotalCostForOrderDiscount(item,reqObj);
						costof.setTotal_cost(totalCost);
					}
					
					costof.setUnit_cost(Double.parseDouble((String)cost.get(i)));
					costof.setExpenses_amount(expenseAmt);
					
					if(isBasedOnWeight && !isBasedOnCost) {
						// implement calculation based on weight
						avg_rate=costofGoods.calculateLandedWeightBased(costof);
					}else if(!isBasedOnWeight && isBasedOnCost){
						// implement calculation based on cost
						costof.setAmount((amt/costof.getQuantity()));
						avg_rate=costofGoods.calculateLandedCostBased(costof);
					}else if(isBasedOnWeight && isBasedOnCost) {
						// implement calculation based on both
						Double costedExpensesAmt=0.0,weight_allocation=0.0,weightedExpensesAmt=0.0,cost_allocation=0.0,landed_cost=0.0;
						for(int j=0;j<expensesAccount.size();j++) {
							if(expensesAccount.get(j).getLandedcostcal().equals("1")) {
								costedExpensesAmt+=expensesAccount.get(j).getAmount();
							}if(expensesAccount.get(j).getLandedcostcal().equals("2")) {
								weightedExpensesAmt+=expensesAccount.get(j).getAmount();
							}
						}
						costof.setExpenses_amount(weightedExpensesAmt);
						
							weight_allocation=costofGoods.calculateWeightAllocaiton(costof);
						
						costedExpensesAmt+=Double.parseDouble(shippingCost);
						costof.setExpenses_amount(costedExpensesAmt);
						System.out.println(costof.toString());
						cost_allocation=costofGoods.calculateCostAllocaiton(costof);
						landed_cost=weight_allocation+cost_allocation+costof.getAmount();
						avg_rate=(landed_cost/costof.getQuantity());
					}
					  costof.setAvg_rate(avg_rate);
					  costof.setBillhdrid(String.valueOf(billHdrId));
					  landedCostLst.add(costof);	
				}			
				
				if(!landedCostLst.isEmpty() && landedCostLst.size()>0) {
					BillDAO billDao=new BillDAO();
					for(int i=0;i<landedCostLst.size();i++) {
						if(landedCostLst.get(i).getAvg_rate()>0) {
							int billUpt=billDao.updateLandedCost(landedCostLst.get(i), plant);
							System.out.println("Avg Rate Updated in Bill System " +landedCostLst.get(i).getProd_id()+" : "+billUpt);
							if(billUpt == 0) {
								flag = false;
							}
						}
					}
				}
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
