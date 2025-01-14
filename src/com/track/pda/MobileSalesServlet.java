package com.track.pda;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;

import com.track.dao.CustomerBeanDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.OrderTypeBeanDAO;
import com.track.db.util.CustUtil;
import com.track.db.util.InvMstUtil;
import com.track.db.util.CurrencyUtil;
import com.track.db.util.LocTypeUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.OrderTypeUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.PrdBrandUtil;
import com.track.db.util.PrdClassUtil;
import com.track.db.util.PrdTypeUtil;
import com.track.db.util.UserLocUtil;
import com.track.tables.DODET;
import com.track.gates.selectBean;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

import javax.transaction.UserTransaction;

/**
 * Servlet implementation class MobileSalesServlet
 */
public class MobileSalesServlet extends HttpServlet {
	private boolean printLog = MLoggerConstant.MobileSalesServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.MobileSalesServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 1L;
	private String PLANT = "";
	private String xmlStr = "";
	private String action = "";
	
	StrUtils strUtils = new StrUtils();
	CustUtil custUtils = new CustUtil();
	CurrencyUtil currencyUtil = new CurrencyUtil();
	PlantMstUtil plantMstUtil = new PlantMstUtil();
	OrderTypeUtil orderTypeUtil = new OrderTypeUtil();
	ItemMstUtil itemMstUtil = new ItemMstUtil();
	CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	OrderTypeBeanDAO orderTypeBeanDAO = new OrderTypeBeanDAO();
	DOUtil doUtil = new DOUtil();
    DoDetDAO dodetDao = new DoDetDAO();
    ItemMstDAO itemMstDAO = new ItemMstDAO();
    PrdClassUtil prdclsutil = new PrdClassUtil();
    PrdTypeUtil prdutil = new PrdTypeUtil();
    PrdBrandUtil prdbrandutil = new PrdBrandUtil();
    InvMstDAO  _InvMstDAO  = new InvMstDAO(); 
    LocTypeUtil loctypeutil = new LocTypeUtil();
	
	
		
	private static final String CONTENT_TYPE = "text/xml";
    
    public void init() throws ServletException {
    	custUtils = new CustUtil();
		currencyUtil=new CurrencyUtil();
		plantMstUtil=new PlantMstUtil();
		orderTypeUtil=new OrderTypeUtil();
		itemMstUtil = new ItemMstUtil();
		dodetDao = new DoDetDAO();
		customerBeanDAO = new CustomerBeanDAO();
		orderTypeBeanDAO = new OrderTypeBeanDAO();
		doUtil = new DOUtil();
		dodetDao = new DoDetDAO();
		itemMstDAO=new ItemMstDAO();
		prdclsutil = new PrdClassUtil();
		prdutil = new PrdTypeUtil();
		prdbrandutil = new PrdBrandUtil();
		_InvMstDAO  = new InvMstDAO(); 
		loctypeutil = new LocTypeUtil();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
	
		try {
			action = request.getParameter("action").trim();
			this.setMapDataToLogger(this.populateMapData(StrUtils
					.fString(request.getParameter("PLANT")), StrUtils
					.fString(request.getParameter("LOGIN_USER"))+ " PDA_USER"));
			custUtils.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
						
			if (action.equalsIgnoreCase("process_mobilesales_autogenerate")) {
				xmlStr = "";
				xmlStr = process_mobilesales_autogenrate(request, response);
			}
			else if (action.equalsIgnoreCase("validate_customer")) {
				xmlStr = "";
				xmlStr = validate_customer(request, response);
			}
			else if (action.equalsIgnoreCase("validate_order_type")) {
				xmlStr = "";
				xmlStr = validate_order_type(request, response);
			}
			else if (action.equalsIgnoreCase("get_customer_details")) {
				xmlStr = "";
				xmlStr = get_customer_details(request, response);
			}
			else if (action.equalsIgnoreCase("get_gst_percentage")) {
				xmlStr = "";
				xmlStr = get_gst_percentage(request, response);
			}
			else if (action.equalsIgnoreCase("get_default_currency")) {
				xmlStr = "";
				xmlStr = get_default_currency(request, response);
			}
			else if (action.equalsIgnoreCase("get_currency_details")) {
				xmlStr = "";
				xmlStr = get_currency_details(request, response);
			}
			else if (action.equalsIgnoreCase("get_order_type_details")) {
				xmlStr = "";
				xmlStr = get_order_type_details(request, response);
			}
			else if (action.equalsIgnoreCase("get_mobilesales_item_details")) {
				xmlStr = "";
				xmlStr = get_mobilesales_item_details(request, response);
			}
			else if (action.equalsIgnoreCase("get_mobilesales_item_details_storentrack")) {
				xmlStr = "";
				xmlStr = get_mobilesales_item_details_storentrack(request, response);
			}
			else if (action.equalsIgnoreCase("get_mobilesales_next_lineno")) {
				xmlStr = "";
				xmlStr = get_mobilesales_next_lineno(request, response);
			}
			else if (action.equalsIgnoreCase("get_mobilesales_product_details")) {
				xmlStr = "";
				xmlStr = get_mobilesales_product_details(request, response);
			}
			else if (action.equalsIgnoreCase("get_mobilesales_summary_by_order")) {
				xmlStr = "";
				xmlStr = get_mobilesales_summary_by_order(request, response);
			}
			else if (action.equalsIgnoreCase("get_mobilesales_summary_by_product")) {
				xmlStr = "";
				xmlStr = get_mobilesales_summary_by_product(request, response);
			}
			else if (action.equalsIgnoreCase("get_outBoundOrders_details")) {
				xmlStr = "";
				xmlStr = get_outBoundOrders_details(request, response);
			}
			else if (action.equalsIgnoreCase("get_outBoundOrders_details_tohdr_update")) {
				xmlStr = "";
				xmlStr =get_outBoundOrders_details_tohdr_update(request, response);
			}
			else if (action.equalsIgnoreCase("process_remove_oubound_order")) {
				xmlStr = "";
				xmlStr = process_remove_oubound_order(request, response);
			}
			else if (action.equalsIgnoreCase("process_update_oubound_order")) {
				xmlStr = "";
				xmlStr = process_update_oubound_order(request, response);
			}
			else if (action.equalsIgnoreCase("process_remove_oubound_order_line_item")) {
				xmlStr = "";
				xmlStr = process_remove_oubound_order_line_item(request, response);
			}
			else if (action.equalsIgnoreCase("process_update_oubound_order_line_item")) {
				xmlStr = "";
				xmlStr = process_update_oubound_order_line_item(request, response);
			}
			else if (action.equalsIgnoreCase("get_outBoundOrder_item_details")) {
				xmlStr = "";
				xmlStr = get_outBoundOrder_item_details(request, response);
			}
			else if (action.equalsIgnoreCase("check_isOpenOutBoundOrder")) {
				xmlStr = "";
				xmlStr = check_isOpenOutBoundOrder(request, response);
			}
			else if (action.equalsIgnoreCase("check_nonstockflag_item")) {
				xmlStr = "";
				xmlStr = check_nonstockflag_item(request, response);
			}
			else if (action.equalsIgnoreCase("get_product_class")) {
				xmlStr = "";
				xmlStr = get_product_class(request, response);
			}
			else if (action.equalsIgnoreCase("get_product_type")) {
				xmlStr = "";
				xmlStr = get_product_type(request, response);
			}
			else if (action.equalsIgnoreCase("get_product_brand")) {
				xmlStr = "";
				xmlStr = get_product_brand(request, response);
			}
			else if (action.equalsIgnoreCase("get_inv_location")) {
				xmlStr = "";
				xmlStr = get_inv_location(request, response);
			}
			else if (action.equalsIgnoreCase("get_location_type")) {
				xmlStr = "";
				xmlStr = get_location_type(request, response);
			}
			else if (action.equalsIgnoreCase("get_outBoundOrder_do_temp_details")) {
				xmlStr = "";
				xmlStr = get_outBoundOrder_do_temp_details(request, response);
			}
			else if (action.equalsIgnoreCase("get_temp_dono")) {
				xmlStr = "";
				xmlStr = get_do_temp_dono(request, response);
			}else if (action.equalsIgnoreCase("process_delete_temp_do")) {
				xmlStr = "";
				xmlStr = process_delete_temp_do(request, response);
			}
			else if (action.equalsIgnoreCase("process_delete_temp_do_with_item")) {
				xmlStr = "";
				xmlStr = process_delete_temp_do_with_item(request, response);
			}else if (action.equalsIgnoreCase("get_dono")) {
				xmlStr = "";
				xmlStr = get_do_dono(request, response);
			}
			else if (action.equalsIgnoreCase("get_maxplus_printing_item_details")) {
				xmlStr = "";
				xmlStr = get_maxplus_printing_item_details(request, response);
			}
			else if (action.equalsIgnoreCase("get_maxplus_printing_batch_details")) {
				xmlStr = "";
				xmlStr = get_maxplus_printing_batch_details(request, response);
			}
			
			
			
					
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, "Unable to process? "
					+ e.getMessage());
			System.out.println("error"+e.getMessage());
		}

		out.write(xmlStr);
		out.close();
	}
	
	private String process_mobilesales_autogenrate(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String plant = "",userid="";
		try {

			plant = StrUtils.fString(request.getParameter("PLANT"));
			userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			
			com.track.dao.TblControlDAO _TblControlDAO=new com.track.dao.TblControlDAO();
			_TblControlDAO.setmLogger(mLogger); 
			String orderno = _TblControlDAO.getNextOrder(plant,userid,IConstants.OUTBOUND);
						
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("ItemDetails");
			xmlStr += XMLUtils.getXMLNode("status", "0");
			xmlStr += XMLUtils.getXMLNode("description", "");
			xmlStr += XMLUtils.getXMLNode("orderno", orderno);
			xmlStr += XMLUtils.getEndNode("ItemDetails");

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Orderno not generated");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}
	
	private String validate_customer(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xmlStr = "";
		String plant = "",custName="";
			
		try {
			 plant = StrUtils.fString(request.getParameter("PLANT"));
			 custName=  strUtils.InsertQuotes(strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("CUST_NAME"))));
			 
			
			boolean isvalidCustomer = customerBeanDAO.isExistsCustomerName(custName, plant);
			
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("customerDetails");
			
			if (isvalidCustomer) {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
				xmlStr = xmlStr
				+ XMLUtils.getXMLNode("description", "Customer found");
			}
			else
			{
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
				xmlStr = xmlStr
				+ XMLUtils.getXMLNode("description", "Customer not found");
			}
			xmlStr = xmlStr + XMLUtils.getEndNode("customerDetails");
			if (xmlStr.equalsIgnoreCase("")) {
				throw new Exception("Customer not found");
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}
	
	private String validate_order_type(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xmlStr = "";
		String plant = "",ordertype="";
			
		try {
			 plant = StrUtils.fString(request.getParameter("PLANT"));
			 ordertype= StrUtils.fString(request.getParameter("ORDER_TYPE"));
			 
			 boolean isvalidOrderType = orderTypeBeanDAO.isOrderTypeExists(ordertype, plant);
			
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr = xmlStr + XMLUtils.getStartNode("OrderTypeDetails");
			
			if (isvalidOrderType) {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
				xmlStr = xmlStr
				+ XMLUtils.getXMLNode("description", "OrderType found");
			}
			else
			{
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
				xmlStr = xmlStr
				+ XMLUtils.getXMLNode("description", "OrderType not found");
				
			}
			xmlStr = xmlStr + XMLUtils.getEndNode("OrderTypeDetails");

			if (xmlStr.equalsIgnoreCase("")) {
				throw new Exception("Order Type not found");
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}
	
	private String get_customer_details(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xmlStr = "";
		String plant = "",custname="",searchby="";
		try {
			 plant = StrUtils.fString(request.getParameter("PLANT"));
			 custname= StrUtils.fString(request.getParameter("CUSTOMER_NAME"));
			 searchby= StrUtils.fString(request.getParameter("SEARCHBY"));
			//InvMstUtil itM = new InvMstUtil();
			xmlStr = custUtils.getMobileSalesOrderCustomerDetails(plant,custname,searchby);
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Customer Not found");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	
	private String get_gst_percentage(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String plant = "",userid="",gsttype="";
		try {
			//getGST(String key, String plant)
			selectBean sb = new selectBean();
			plant = StrUtils.fString(request.getParameter("PLANT"));
			userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			gsttype = StrUtils.fString(request.getParameter("GST_TYPE"));
			String gstpercentage = sb.getGST(gsttype,plant);
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("GstDetails");
			xmlStr += XMLUtils.getXMLNode("status", "0");
			xmlStr += XMLUtils.getXMLNode("description", "");
			xmlStr += XMLUtils.getXMLNode("gstpercentage", gstpercentage);
			xmlStr += XMLUtils.getEndNode("GstDetails");

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Orderno not generated");
			}
		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}
	
	private String get_default_currency(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String plant = "",userid="",basecurrency="";
		try {
			plant = StrUtils.fString(request.getParameter("PLANT"));
			userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			List listQry = plantMstUtil.getPlantMstDetails(plant);
			 for (int i =0; i<listQry.size(); i++){
			     Map map = (Map) listQry.get(i);
		         basecurrency  = (String) map.get("BASE_CURRENCY");
			 }
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("BaseCurrency");
			xmlStr += XMLUtils.getXMLNode("status", "0");
			xmlStr += XMLUtils.getXMLNode("description", "");
			xmlStr += XMLUtils.getXMLNode("basecurrency", basecurrency);
			xmlStr += XMLUtils.getEndNode("BaseCurrency");

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "BaseCurrency not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}

	
	private String get_currency_details(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xmlStr = "";
		String plant = "",userid="",display="";
		try {
			 plant = StrUtils.fString(request.getParameter("PLANT"));
			 userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			 display= StrUtils.fString(request.getParameter("DISPLAY"));
			xmlStr = currencyUtil.getMobileSalesOrderCurrencyDetails(plant,display);
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Currency not found");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
		
	
	
	private String get_order_type_details(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xmlStr = "";
		String plant = "",ordertype="";
		try {
			 plant = StrUtils.fString(request.getParameter("PLANT"));
			 ordertype= StrUtils.fString(request.getParameter("ORDER_TYPE"));
			xmlStr = orderTypeUtil.getMobileSalesOrderOrderTypeDetails(plant,ordertype);
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "OrderType not found");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	
	
	private String get_mobilesales_item_details(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xmlStr = "";
		String plant = "",userid="",item="",itemdesc="",custname="",currency="";
		try {
			 plant = StrUtils.fString(request.getParameter("PLANT"));
			 userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			 item= StrUtils.fString(request.getParameter("ITEM_NUM"));
			 itemdesc= StrUtils.fString(request.getParameter("ITEM_DESC"));
			 custname= StrUtils.fString(request.getParameter("CUSTNAME"));
			 currency=StrUtils.fString(request.getParameter("CURRENCY"));
			 xmlStr = itemMstUtil.getMobileSales_ItemList(plant, item, itemdesc,custname,currency, " AND isnull(itemmst.userfld1,'N')='N'");
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product not found");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	
	private String get_mobilesales_item_details_storentrack(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xmlStr = "";
		String plant = "",userid="",item="",itemdesc="",custname="",currency="";
		try {
			 plant = StrUtils.fString(request.getParameter("PLANT"));
			 userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			 item= StrUtils.fString(request.getParameter("ITEM_NUM"));
			 itemdesc= StrUtils.fString(request.getParameter("ITEM_DESC"));
			 custname= StrUtils.fString(request.getParameter("CUSTNAME"));
			 currency= StrUtils.fString(request.getParameter("CURRENCY"));
			 
			 xmlStr = itemMstUtil.getMobileSales_ItemList_storentrack(plant, item, itemdesc,custname,currency, " AND isnull(itemmst.userfld1,'N')='N'");
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product not found");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	
	private String get_mobilesales_next_lineno(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String plant = "",userid="",dono="";
		try {

			plant = StrUtils.fString(request.getParameter("PLANT"));
			userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			dono = StrUtils.fString(request.getParameter("DONO"));
			
			DODET dODET = new DODET();
			String lineno = dODET.getNextLineNo(dono,plant);
						
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("LineNo");
			xmlStr += XMLUtils.getXMLNode("status", "0");
			xmlStr += XMLUtils.getXMLNode("description", "");
			xmlStr += XMLUtils.getXMLNode("lineno", lineno);
			xmlStr += XMLUtils.getEndNode("LineNo");

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "lineno not foune");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}
	
	private String get_mobilesales_product_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", aItem = "",aCustName="",currency="";
		try {
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));
			aCustName = StrUtils.fString(request.getParameter("CUSTNAME"));
			currency = StrUtils.fString(request.getParameter("CURRENCY"));
			System.out.println("aItem"+aItem);

			Hashtable itemht = new Hashtable();
			itemht.put("PLANT", aPlant);
			itemht.put("ITEM", aItem);
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			boolean isexits = true;
			String scannedItemNo = itemMstDAO.getItemIdFromAlternate(aPlant,
					aItem);
			if (scannedItemNo == null) {
				isexits = false;
			}
			String OBDiscount="",convertedcost="",calAmount="";
			float price=0,discount=0;
			System.out.println("aItem 2"+aItem);

            convertedcost = new DOUtil().getMobileSalesConvertedUnitCostForProduct(aPlant,"",scannedItemNo,currency);  
			OBDiscount=new DOUtil().getOBDiscountSelectedItem(aPlant,aCustName,scannedItemNo,"PDAOB");     
			if (isexits) {
				String itemDesc = StrUtils.fString(itemMstDAO.getItemDesc(
						aPlant, scannedItemNo));
				String uom = StrUtils.fString(itemMstDAO.getItemUOM(aPlant,
						scannedItemNo));
								String itemDetailDesc=StrUtils.fString(itemMstDAO.getItemDetailDesc(aPlant,
						scannedItemNo));
				//String unitprice= StrUtils.fString(itemMstDAO.getItemPrice(aPlant,
						//scannedItemNo));
				// /getItemPrice
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");

				xmlStr += XMLUtils.getXMLNode("status", "0");
				xmlStr += XMLUtils.getXMLNode("description", "");
				xmlStr += XMLUtils.getXMLNode("item", scannedItemNo);
				xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils
						.replaceCharacters2SendPDA(itemDesc).toString());
				xmlStr += XMLUtils.getXMLNode("uom", uom);
				//xmlStr += XMLUtils.getXMLNode("unitprice", unitprice);
				float flistprice=Float.parseFloat(convertedcost);
				xmlStr += XMLUtils.getXMLNode("listprice",   String.format("%.2f",flistprice));
				if(OBDiscount=="" || OBDiscount =="0" || OBDiscount=="0.00")
				{ 
					xmlStr += XMLUtils.getXMLNode("unitprice",convertedcost);
										
				}
				else
				{
					int plusIndex = OBDiscount.indexOf("%");
	                 if (plusIndex != -1) {
	                	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	                	 	String uprice=convertedcost;
	    					discount = (Float.parseFloat(uprice) *  Float.parseFloat(OBDiscount)/100);
	    					price = (Float.parseFloat(uprice)-discount);
	    					calAmount = String.format("%.2f", price);
	    						    					
	    				
	                     }
	                 else{
	                	 	price = Float.parseFloat(OBDiscount);
	                	 	calAmount = String.format("%.2f", price);
	                 }
	                 xmlStr += XMLUtils.getXMLNode("unitprice",  calAmount);
	
	                 
				}
				xmlStr += XMLUtils.getXMLNode("itemDetailDesc", StrUtils
 						.replaceCharacters2SendPDA(itemDetailDesc).toString());			
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Product Not found");
			}
			// /

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product Not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}
	

	private String get_mobilesales_summary_by_order(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xmlStr = "";
		String plant = "",userid="",FROM_DATE="",TO_DATE="",searchbyone="",searchvalueone="",searchbytwo="",searchvaluetwo="",
		fdate="",tdate="",itemdesc="";
		try {
			 plant = StrUtils.fString(request.getParameter("PLANT"));
			 userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			 FROM_DATE= StrUtils.fString(request.getParameter("FROM_DATE"));
			 TO_DATE= StrUtils.fString(request.getParameter("TO_DATE"));
			 searchbyone= StrUtils.fString(request.getParameter("SEARCH_BY_ONE"));
			 searchvalueone= strUtils.InsertQuotes(strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("SEARCH_VALUE_ONE")))); 
			 searchbytwo= StrUtils.fString(request.getParameter("SEARCH_BY_TWO"));
			 searchvaluetwo= strUtils.InsertQuotes(strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("SEARCH_VALUE_TWO")))); 
			  DateUtils _dateUtils = new DateUtils();
			 String curDate =_dateUtils.getDate();
			 if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
	         if (FROM_DATE.length()>5)
	         fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
             if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
             if (TO_DATE.length()>5)
             tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
			  Hashtable ht = new Hashtable();
			 if(searchbyone.equals("OrderNo") ) ht.put("A.DONO",searchvalueone); 
	         if(searchbyone.equals("Customer") ) ht.put("A.CUSTNAME",searchvalueone); 
	         if(searchbyone.equals("OrderType") ) ht.put("A.ORDERTYPE",searchvalueone); 
	         if(searchbytwo.equals("ProductID") ) ht.put("B.ITEM",searchvaluetwo); 
	         if(searchbytwo.equals("Desc") ) itemdesc=searchvaluetwo; 
	         if(searchbytwo.equals("IssueStatus") ) ht.put("B.LNSTAT",searchvaluetwo); 
	              
	         xmlStr = doUtil.get_pda_mobilesales_summary_by_order(ht,fdate,tdate,itemdesc,plant,userid);
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Order details not found");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	
	private String get_mobilesales_summary_by_product(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xmlStr = "";
		String plant = "",userid="",FROM_DATE="",TO_DATE="",searchbyone="",searchvalueone="",searchbytwo="",searchvaluetwo="",
						fdate="",tdate="",itemdesc="";
		try {
			 plant = StrUtils.fString(request.getParameter("PLANT"));
			 userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			 FROM_DATE= StrUtils.fString(request.getParameter("FROM_DATE"));
			 TO_DATE= StrUtils.fString(request.getParameter("TO_DATE"));
			 searchbyone= StrUtils.fString(request.getParameter("SEARCH_BY_ONE"));
			 searchvalueone= StrUtils.fString(request.getParameter("SEARCH_VALUE_ONE"));
			 searchbytwo= StrUtils.fString(request.getParameter("SEARCH_BY_TWO"));
			 searchvaluetwo= StrUtils.fString(request.getParameter("SEARCH_VALUE_TWO"));
			 DateUtils _dateUtils = new DateUtils();
			 String curDate =_dateUtils.getDate();
			 if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
			 if (FROM_DATE.length()>5)
	        	 fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
             if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
             if (TO_DATE.length()>5)
            	 tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
			 
             Hashtable ht = new Hashtable();	           
	         if(searchbyone.equals("OrderNo") ) ht.put("A.DONO",searchvalueone); 
	         if(searchbyone.equals("Customer") ) ht.put("A.CUSTNAME",searchvalueone); 
	         if(searchbyone.equals("OrderType") ) ht.put("A.ORDERTYPE",searchvalueone); 
	         if(searchbytwo.equals("ProductID") ) ht.put("B.ITEM",searchvaluetwo); 
	         if(searchbytwo.equals("Desc") ) itemdesc=searchvaluetwo; 
	         if(searchbytwo.equals("IssueStatus") ) ht.put("B.LNSTAT",searchvaluetwo); 
	         
	        xmlStr = doUtil.get_pda_mobilesales_summary_by_product(ht,fdate,tdate,itemdesc,plant,userid);
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Order details not found");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	
	private String get_outBoundOrders_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", plant = "", dono = "",userid="",custname="";
		try {
			// MLogger.log(0, "load_outBoundOrders() Starts ");
			plant = StrUtils.fString(request.getParameter("PLANT"));
			userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			custname= strUtils.InsertQuotes(strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("CUST_NAME")))); 
			dono = StrUtils.fString(request.getParameter("DONO"));
			
			Hashtable ht = new Hashtable();
			str = doUtil.getPDAMobileSales_OutboundOrder_details(plant,custname,dono,userid);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No Outbound Orders Found");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		this.mLogger.info(this.printInfo, str);

		return str;
	}
	
	private String get_outBoundOrders_details_tohdr_update(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", plant = "", dono = "",userid="",custname="";
		try {
			// MLogger.log(0, "load_outBoundOrders() Starts ");
			plant = StrUtils.fString(request.getParameter("PLANT"));
			userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			dono = StrUtils.fString(request.getParameter("DONO"));
			
			
			Hashtable ht = new Hashtable();
			str = doUtil.getPDAMobileSales_OutboundOrder_details(plant,dono,userid);
			
			

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No Outbound Orders Found");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		this.mLogger.info(this.printInfo, str);

		return str;
	}
	
	
	private String process_remove_oubound_order(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		Map receiveMaterial_HM = null;
		String plant = "", dono = "", status = "",userid="";
		try {
			plant = StrUtils.fString(request.getParameter("PLANT"));
			dono = StrUtils.fString(request.getParameter("DONO"));
			status = StrUtils.fString(request.getParameter("STATUS"));
			userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			xmlStr = "";
			boolean flag = true;
			Hashtable htDoHrd = new Hashtable();
			htDoHrd.put(IDBConstants.PLANT, plant);
			htDoHrd.put(IDBConstants.DODET_DONUM, dono);
			boolean isValidOrder =new DoHdrDAO().isExisit(htDoHrd,"");
			boolean isOrderInProgress = dodetDao.isExisit(htDoHrd, "LNSTAT in ('O' ,'C') and ITEM NOT IN(SELECT ITEM FROM ["+plant+"_ITEMMST] where NONSTKFLAG='Y')");
			if(isValidOrder){
			if (!isOrderInProgress) {
					flag = doUtil.removeRow(plant,dono,userid);
					 xmlStr = XMLUtils.getXMLHeader();
			         xmlStr = xmlStr + XMLUtils.getStartNode("DeleteOutboundOrder");
					if (flag) {
						xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
						xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description", "Order " + dono + " deleted successfull");
					}
					else
					{
						xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
						xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description", "Delete Order " + dono + " is not successfull");
						
					}
				   xmlStr = xmlStr + XMLUtils.getEndNode("DeleteOutboundOrder");
			  }else{ throw new Exception("Outbound Order " +dono+ " is in use. Cannot Deleted!");}
			}
			else
			{
				 throw new Exception("Not a valid outbound order");
			}
			
		} catch (Exception e) {
			 xmlStr = XMLUtils.getXMLHeader();
	         xmlStr = xmlStr + XMLUtils.getStartNode("DeleteOutboundOrder");
	         xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
	         xmlStr = xmlStr+ XMLUtils.getXMLNode("description",e.getMessage());
	         xmlStr = xmlStr + XMLUtils.getEndNode("DeleteOutboundOrder");
		}

		return xmlStr;
	}

	private String process_update_oubound_order(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		boolean flag = false;
		Hashtable ht = new Hashtable();
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		DateUtils dateUtils = new DateUtils();
		CurrencyUtil curUtil = new CurrencyUtil();
	    String ordertypeString="";  
		String plant ="",result = "", dono = "", custName = "",display="",ordertype="", jobNum = "", currencyid="",custCode = "", user = "", personIncharge = "", contactNum = "", address = "", address2 = "", address3 = "", collectionDate = "", collectionTime = "", remark1 = "", remark2 = "";
		String deliverydate="",timeslots="",outbound_Gst="",orderstatus="";
		String strName="",strAddr1="",strAddr2="",strAddr3="",strRemarks="";
			plant = StrUtils.fString(request.getParameter("PLANT"));
		   	dono = StrUtils.fString(request.getParameter("DONO")).trim();
			custName = StrUtils.fString(request.getParameter("CUST_NAME")).trim();
			user = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
			jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
			personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
			contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
			custCode = StrUtils.fString(request.getParameter("CUST_CODE"))	.trim();
			
			ArrayList arrCust = customerBeanDAO.getCustomerDetails(custCode,plant);
			strName=(String)arrCust.get(1);
			strAddr1=(String)arrCust.get(2);
			strAddr2=(String)arrCust.get(3);
			strAddr3=(String)arrCust.get(4);
			strRemarks=(String)arrCust.get(16);
			address = strAddr1;
			address2 = strAddr2;
			address3 = strAddr3;
			collectionDate=new DateUtils().parseDate(StrUtils.fString(request.getParameter("ORDER_DATE")).trim());
			if(collectionDate.length()==0){collectionDate = new DateUtils().getDate();}
			//collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
			remark1 =StrUtils.fString(request.getParameter("HDR_REMARK")).trim();
			remark2 = strRemarks;
			ordertype = StrUtils.fString(request.getParameter("ORDER_TYPE")).trim();
			Hashtable curHash =new Hashtable();
			display=StrUtils.fString(request.getParameter("CURRENCY_DISPLAY").trim());
			curHash.put(IConstants.PLANT, StrUtils.fString(request.getParameter("PLANT")).trim());
			curHash.put(IConstants.DISPLAY, display);
			if(display!=null&&display!="")
			{
				currencyid = curUtil.getCurrencyID(curHash,"CURRENCYID");
			}
					
			//deliverydate=StrUtils.fString(request.getParameter("DELIVERYDATE")).trim();
			//timeslots=StrUtils.fString(request.getParameter("TIMESLOTS")).trim();
			outbound_Gst = StrUtils.fString(request.getParameter("GST")).trim();
		    //if(collectionDate.length()==0){collectionDate = new DateUtils().getDate();}
		    orderstatus = StrUtils.fString(request.getParameter("STATUS_ID")).trim();
		    String shipCustname = StrUtils.fString(request.getParameter("SCUST_NAME")).trim();
            String shipCname = StrUtils.fString(request.getParameter("SCONTACT_NAME")).trim();
            String shipaddr1 = StrUtils.fString(request.getParameter("SADDR1")).trim();
            String shipaddr2 = StrUtils.fString(request.getParameter("SADDR2")).trim();
            String shipCity = StrUtils.fString(request.getParameter("SCITY")).trim();
            String shipCountry = StrUtils.fString(request.getParameter("SCOUNTRY")).trim();
            String shipZip = StrUtils.fString(request.getParameter("SZIP")).trim();
            String shiptelno = StrUtils.fString(request.getParameter("STELNO")).trim();
            try {
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.DODET_DONUM, dono);
		    ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
			ht.put(IDBConstants.DOHDR_CUST_NAME, strUtils.InsertQuotes(custName));
			ht.put(IDBConstants.DOHDR_JOB_NUM, jobNum);
			ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, strUtils.InsertQuotes(personIncharge));
			ht.put(IDBConstants.DOHDR_CONTACT_NUM, contactNum);
			ht.put(IDBConstants.DOHDR_ADDRESS, address);
			ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
			ht.put(IDBConstants.DOHDR_ADDRESS3, address3);
			ht.put(IDBConstants.DOHDR_COL_DATE, collectionDate);
			//ht.put(IDBConstants.DOHDR_COL_TIME, collectionTime);
			ht.put(IDBConstants.DOHDR_REMARK1, strUtils.InsertQuotes(remark1));
			ht.put(IDBConstants.DOHDR_REMARK2, strUtils.InsertQuotes(remark2));
			ht.put(IDBConstants.STATUS, "N");
			ht.put(IDBConstants.ORDERTYPE, ordertype);
			ht.put(IDBConstants.CURRENCYID, currencyid);
			//ht.put(IDBConstants.TIMESLOTS, timeslots);
			//ht.put(IDBConstants.DELIVERYDATE, deliverydate);
			ht.put(IDBConstants.DOHDR_GST, outbound_Gst);
			ht.put(IDBConstants.ORDSTATUSID, orderstatus);            
		    //Added for Shiiping address
		    
		    ht.put(IDBConstants.DOHDR_SCUST_NAME, shipCustname);
		    ht.put(IDBConstants.DOHDR_SCONTACT_NAME, shipCname);
		    ht.put(IDBConstants.DOHDR_SADDR1, strUtils.InsertQuotes(shipaddr1));
		    ht.put(IDBConstants.DOHDR_SADDR2, strUtils.InsertQuotes(shipaddr2));
		    ht.put(IDBConstants.DOHDR_SCITY, shipCity);
		    ht.put(IDBConstants.DOHDR_SCOUNTRY, shipCountry);
		    ht.put(IDBConstants.DOHDR_SZIP, shipZip);
		    ht.put(IDBConstants.DOHDR_STELNO, shiptelno);
                     
			boolean isvalidOrderType = true;

			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			customerBeanDAO.setmLogger(mLogger);
			boolean isvalidCustomer = customerBeanDAO.isExistsCustomerName(
					custName, plant);
			if (isvalidCustomer) {
				doUtil.setmLogger(mLogger);
				flag = doUtil.updateDoHdr(ht);
				if (flag) {
					Hashtable htRecvHis = new Hashtable();
					htRecvHis.clear();
					htRecvHis.put(IDBConstants.PLANT, plant);
					htRecvHis.put("DIRTYPE", TransactionConstants.UPDATE_OB);
					htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
					htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
					htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, dono);
					htRecvHis.put(IDBConstants.CREATED_BY, user);
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
			}  
			xmlStr = XMLUtils.getXMLHeader();
	         xmlStr = xmlStr + XMLUtils.getStartNode("UpdateOutboundOrder");
			if (flag) {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
				xmlStr = xmlStr
				+ XMLUtils.getXMLNode("description", "Order " + dono + " updated successfull");
			}
			else
			{
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
				xmlStr = xmlStr
				+ XMLUtils.getXMLNode("description", "Update Order " + dono + " is not successfull");
				
			}
		   xmlStr = xmlStr + XMLUtils.getEndNode("UpdateOutboundOrder");
		
		} catch (Exception e) {
			xmlStr = XMLUtils.getXMLHeader();
	         xmlStr = xmlStr + XMLUtils.getStartNode("UpdateOutboundOrder");
	         xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
	         xmlStr = xmlStr+ XMLUtils.getXMLNode("description",e.getMessage());
	         xmlStr = xmlStr + XMLUtils.getEndNode("UpdateOutboundOrder");
		}

		return xmlStr;
	}

	
	
	private String process_remove_oubound_order_line_item(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		Map receiveMaterial_HM = null;
		String plant = "", dono = "", lineno="", item = "",userid;
		try {
			plant = StrUtils.fString(request.getParameter("PLANT"));
			dono = StrUtils.fString(request.getParameter("DONO"));
			lineno=StrUtils.fString(request.getParameter("DOLNNO"));
			item = StrUtils.fString(request.getParameter("ITEM_NUM"));
			userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			
			Hashtable htdet = new Hashtable();
			htdet.put(IDBConstants.PLANT, plant);
			htdet.put(IDBConstants.DODET_DONUM, dono);
			htdet.put(IDBConstants.DODET_DOLNNO, lineno);
			htdet.put(IDBConstants.DODET_ITEM, item);
			htdet.put(IDBConstants.CREATED_BY, userid);
			boolean flag ;
			doUtil.setmLogger(mLogger);
			flag = doUtil.deleteOBDetLineDetails(htdet);
			
			 xmlStr = XMLUtils.getXMLHeader();
	         xmlStr = xmlStr + XMLUtils.getStartNode("DeleteOutboundOrderLineItem");
	         
	         	if (flag) {
					xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
					xmlStr = xmlStr
					+ XMLUtils.getXMLNode("description", "Item " + item + " deleted successfull");
				}
				else
				{
					xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
					xmlStr = xmlStr
					+ XMLUtils.getXMLNode("description", "Delete item " + item + " is not successfull");
					
				}
	         xmlStr = xmlStr + XMLUtils.getEndNode("DeleteOutboundOrderLineItem");
	
		} catch (Exception e) {
			 xmlStr = XMLUtils.getXMLHeader();
			  xmlStr = xmlStr + XMLUtils.getStartNode("DeleteOutboundOrderLineItem");
	         xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
	         xmlStr = xmlStr+ XMLUtils.getXMLNode("description",e.getMessage());
	         xmlStr = xmlStr + XMLUtils.getEndNode("DeleteOutboundOrderLineItem");
		}

		return xmlStr;
	}
	
	
	
	private String process_update_oubound_order_line_item(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		Map receiveMaterial_HM = null;
		String plant = "", dono = "", lineno="", item = "",itemdesc="",remarks="",qty="",unitprice="",userid;
		try {
			plant = StrUtils.fString(request.getParameter("PLANT"));
			dono = StrUtils.fString(request.getParameter("DONO"));
			lineno=StrUtils.fString(request.getParameter("DOLNNO"));
			item = StrUtils.fString(request.getParameter("PRODUCT_ID"));
			itemdesc = StrUtils.fString(request.getParameter("PRODUCT_DESC"));
			qty = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")).trim());
			unitprice= StrUtils.removeFormat(StrUtils.fString(request.getParameter("UNITPRICE")).trim());
			unitprice = new DOUtil().getConvertedUnitCostToLocalCurrency(plant,dono,unitprice) ;
			remarks=StrUtils.fString(request.getParameter("DET_REMARK")).trim();
			userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
		
			Hashtable htUpdatedodet = new Hashtable();
			htUpdatedodet.clear();
			htUpdatedodet.put(IDBConstants.PLANT, plant);
			htUpdatedodet.put(IDBConstants.DODET_DONUM, dono);
			htUpdatedodet.put("DOLNNO", lineno);
			htUpdatedodet.put(IDBConstants.ITEM, item);
			htUpdatedodet.put(IDBConstants.ITEM_DESC, strUtils.InsertQuotes(itemdesc));
		    htUpdatedodet.put(IDBConstants.DODET_COMMENT1,strUtils.InsertQuotes(remarks));
			//to check item as nonstock item and non stock type in 2&4 then check price has minus otherwise prefix minus with price
            ItemMstDAO _ItemMstDao =new ItemMstDAO(); 
            Hashtable htIsExist = new  Hashtable();
            htIsExist.put(IDBConstants.PLANT, plant);
            htIsExist.put(IDBConstants.ITEM, item);
            htIsExist.put("NONSTKFLAG", "Y");
            boolean isExistNonStock =_ItemMstDao.isExisit(htIsExist, "NONSTKTYPEID IN(2,4)");
            if(isExistNonStock){
                String s =  unitprice;
                if (s.indexOf("-") == -1) {
                	unitprice="-"+unitprice;
                }
            }
          //end to check item as nonstock item and non stock type as 2 then check cost has minus sympol otherwise prefix minus with cost 
            htUpdatedodet.put("UNITPRICE", unitprice);
			htUpdatedodet.put("ORDQTY", qty);
			boolean flag ;
			doUtil.setmLogger(mLogger);
			flag = doUtil.updateDoDetDetails(htUpdatedodet);
			DoDetDAO _DoDetDAO = new DoDetDAO();
			 if(flag){
                 
                 Hashtable htRemarksDel = new Hashtable();
                 htRemarksDel.put(IDBConstants.PLANT,plant);
                 htRemarksDel.put(IDBConstants.DODET_DONUM, dono);
                 htRemarksDel.put(IDBConstants.DODET_DOLNNO,lineno);
                 htRemarksDel.put(IDBConstants.DODET_ITEM, item);
                 flag = _DoDetDAO.deleteDoMultiRemarks(htRemarksDel);
             }
            //delete dodet multi remarks end
            
             String strMovHisRemarks="";
             if(flag)
				{
             	     DateUtils dateUtils = new DateUtils();
				     String DYNAMIC_REMARKS_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_REMARKS_SIZE"));
						
						String productDORemarks="";
							Hashtable htRemarks = new Hashtable();
							htRemarks.put(IDBConstants.PLANT, plant);
							htRemarks.put(IDBConstants.DODET_DONUM, dono);
							htRemarks.put(IDBConstants.DODET_DOLNNO,lineno);
							htRemarks.put(IDBConstants.DODET_ITEM, item);
							htRemarks.put(IDBConstants.REMARKS,strUtils.InsertQuotes(remarks));
							htRemarks.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
							htRemarks.put(IDBConstants.CREATED_BY,userid);
							flag = doUtil.saveDoMultiRemarks(htRemarks);
						
				    }
			if(flag)
			{
				 MovHisDAO movhisDao = new MovHisDAO();
                 Hashtable htmovHis = new Hashtable();
                 movhisDao.setmLogger(mLogger);
                 htmovHis.clear();
                 htmovHis.put(IDBConstants.PLANT, plant);
                 htmovHis.put("DIRTYPE", "SALES_ORDER_UPDATE_PRODUCT");
                 htmovHis.put(IDBConstants.ITEM,item);
                 htmovHis.put(IDBConstants.MOVHIS_ORDNUM, dono);
                 htmovHis.put(IDBConstants.MOVHIS_ORDLNO, lineno);
                 htmovHis.put("QTY", qty);
                 htmovHis.put("REMARKS", unitprice+","+strUtils.InsertQuotes(remarks));
                 htmovHis.put(IDBConstants.CREATED_BY,userid);
                 htmovHis.put("MOVTID", "");
                 htmovHis.put("RECID", "");
                 htmovHis.put(IDBConstants.TRAN_DATE, new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));
                 htmovHis.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());  
                 flag = movhisDao.insertIntoMovHis(htmovHis);
			}
			
			 xmlStr = XMLUtils.getXMLHeader();
	         xmlStr = xmlStr + XMLUtils.getStartNode("UpdateOutboundOrderLineItem");
	         
	         	if (flag) {
					xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
					xmlStr = xmlStr
					+ XMLUtils.getXMLNode("description", "Item " + item + " updated successfull");
				}
				else
				{
					xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
					xmlStr = xmlStr
					+ XMLUtils.getXMLNode("description", "Update item " + item + " is not successfull");
					
				}
	         xmlStr = xmlStr + XMLUtils.getEndNode("UpdateOutboundOrderLineItem");
	
		} catch (Exception e) {
			 xmlStr = XMLUtils.getXMLHeader();
			 xmlStr = xmlStr + XMLUtils.getStartNode("UpdateOutboundOrderLineItem");
	         xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
	         xmlStr = xmlStr+ XMLUtils.getXMLNode("description",e.getMessage());
	         xmlStr = xmlStr + XMLUtils.getEndNode("UpdateOutboundOrderLineItem");
		}

		return xmlStr;
	}
	
	private String get_outBoundOrder_item_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);

			str = doUtil.getPDAMobileSalesOutBoundOrderItemDetails(PLANT, aOrdNo,
					Boolean.FALSE);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		
		return str;
	}
	
	//Boolean flag = dOUtil.isOpenOutBoundOrder(plant, dono);
	
	private String check_isOpenOutBoundOrder(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String plant = "",dono="",userid="";
		try {
			plant = StrUtils.fString(request.getParameter("PLANT"));
			dono = StrUtils.fString(request.getParameter("DONO"));
			userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			Boolean flag = doUtil.isOpenOutBoundOrder(plant, dono);
			 xmlStr = XMLUtils.getXMLHeader();
	         xmlStr = xmlStr + XMLUtils.getStartNode("IsOpentOutBoundOrder");
	         
	         if (flag) {
					xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
					xmlStr = xmlStr
					+ XMLUtils.getXMLNode("description", "");
				}
				else
				{
					xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
					xmlStr = xmlStr
					+ XMLUtils.getXMLNode("description", "Processed Outbound Order Cannot be modified");
					
				}
			   xmlStr = xmlStr + XMLUtils.getEndNode("IsOpentOutBoundOrder");
	
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		
		return xmlStr;
	}
	
	private String check_nonstockflag_item(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String plant = "",item="",nonstockitem="",userid="";
		try {
			plant = StrUtils.fString(request.getParameter("PLANT"));
			userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			item= StrUtils.fString(request.getParameter("ITEM_NUM"));
						
			nonstockitem = itemMstDAO.getNonStockFlag(plant,item);
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("NonStockItem");
			xmlStr += XMLUtils.getXMLNode("status", "0");
			xmlStr += XMLUtils.getXMLNode("description", "");
			xmlStr += XMLUtils.getXMLNode("nonstockitem", nonstockitem);
			xmlStr += XMLUtils.getEndNode("NonStockItem");

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "NonStockItem not generated");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}
	
	private String get_product_class(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xmlStr = "";
		String plant = "",searchvalueone="",userid="";
		try {
			 plant = StrUtils.fString(request.getParameter("PLANT"));
			 userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			 searchvalueone= StrUtils.fString(request.getParameter("SEARCH_VALUE_ONE"));
			 List listQry =  prdclsutil.getPrdTypeList(searchvalueone, plant,	" AND ISACTIVE ='Y'");
			 
			 if (listQry.size() > 0) {
					    String trantype="";
						xmlStr += XMLUtils.getXMLHeader();
						xmlStr += XMLUtils.getStartNode("ProductClass total='"+ String.valueOf(listQry.size()) + "'");
						for (int i = 0; i < listQry.size(); i++) {
							Map map = (Map) listQry.get(i);
							xmlStr += XMLUtils.getStartNode("record");
							xmlStr += XMLUtils.getXMLNode("productclass",  strUtils.replaceCharacters2SendPDA(map.get("prd_cls_id").toString()));   
							xmlStr += XMLUtils.getXMLNode("desc",  strUtils.replaceCharacters2SendPDA(map.get("prd_cls_desc").toString()));   
								xmlStr += XMLUtils.getEndNode("record");
						}
						
						xmlStr += XMLUtils.getEndNode("ProductClass");
					}
				
			
			if (listQry.size()==0) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product class not found");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	private String get_product_type(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xmlStr = "";
		String plant = "",searchvalueone="",userid="";
		try {
			 plant = StrUtils.fString(request.getParameter("PLANT"));
			 userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			 searchvalueone= StrUtils.fString(request.getParameter("SEARCH_VALUE_ONE"));
			 List listQry = prdutil.getPrdTypeList(searchvalueone, plant,	" AND ISACTIVE ='Y'");
			 
			 if (listQry.size() > 0) {
					    String trantype="";
						xmlStr += XMLUtils.getXMLHeader();
						xmlStr += XMLUtils.getStartNode("ProductType total='"+ String.valueOf(listQry.size()) + "'");
						for (int i = 0; i < listQry.size(); i++) {
							Map map = (Map) listQry.get(i);
							xmlStr += XMLUtils.getStartNode("record");
							xmlStr += XMLUtils.getXMLNode("producttype", strUtils.replaceCharacters2SendPDA(map.get("prd_type_id").toString()));  
							xmlStr += XMLUtils.getXMLNode("desc",  strUtils.replaceCharacters2SendPDA(map.get("prd_type_desc").toString()));  
								xmlStr += XMLUtils.getEndNode("record");
						}
						
						xmlStr += XMLUtils.getEndNode("ProductType");
					}
				
			
			if (listQry.size()==0) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product type not found");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	
	private String get_product_brand(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xmlStr = "";
		String plant = "",searchvalueone="",userid="";
		try {
			 plant = StrUtils.fString(request.getParameter("PLANT"));
			 userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			 searchvalueone= StrUtils.fString(request.getParameter("SEARCH_VALUE_ONE"));
			 List listQry =  prdbrandutil.getPrdBrandList(searchvalueone, plant,	" ");
			 
			 if (listQry.size() > 0) {
					    String trantype="";
						xmlStr += XMLUtils.getXMLHeader();
						xmlStr += XMLUtils.getStartNode("ProductBrand total='"+ String.valueOf(listQry.size()) + "'");
						for (int i = 0; i < listQry.size(); i++) {
							Map map = (Map) listQry.get(i);
							xmlStr += XMLUtils.getStartNode("record");
							xmlStr += XMLUtils.getXMLNode("productbrand", strUtils.replaceCharacters2SendPDA(map.get("PRD_BRAND_ID").toString()));   
							xmlStr += XMLUtils.getXMLNode("desc",  strUtils.replaceCharacters2SendPDA(map.get("PRD_BRAND_DESC").toString()));   
								xmlStr += XMLUtils.getEndNode("record");
						}
						
						xmlStr += XMLUtils.getEndNode("ProductBrand");
					}
				
			
			if (listQry.size()==0) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product brand not found");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
		
	private String get_inv_location(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xmlStr = "";
		String plant = "",loc="",userid="";
		try {
			 plant = StrUtils.fString(request.getParameter("PLANT"));
			 userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			 loc= StrUtils.fString(request.getParameter("LOC"));
			 List listQry =  _InvMstDAO.getStockTransferLocByWMS(plant,loc,userid);
			 
			 if (listQry.size() > 0) {
					    String trantype="";
						xmlStr += XMLUtils.getXMLHeader();
						xmlStr += XMLUtils.getStartNode("Location total='"+ String.valueOf(listQry.size()) + "'");
						for (int i = 0; i < listQry.size(); i++) {
							Map map = (Map) listQry.get(i);
							xmlStr += XMLUtils.getStartNode("record");
							xmlStr += XMLUtils.getXMLNode("location",  strUtils.replaceCharacters2SendPDA(map.get("FromLoc").toString()));
							xmlStr += XMLUtils.getXMLNode("desc",   strUtils.replaceCharacters2SendPDA(map.get("locdesc").toString())); 
								xmlStr += XMLUtils.getEndNode("record");
						}
						
						xmlStr += XMLUtils.getEndNode("Location");
					}
				
			
			if (listQry.size()==0) {
				xmlStr = XMLUtils.getXMLMessage(1, "Location not found");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	
	private String get_location_type(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xmlStr = "";
		String plant = "",loctype="",userid="";
		try {
			 plant = StrUtils.fString(request.getParameter("PLANT"));
			 userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
			 loctype= StrUtils.fString(request.getParameter("LOC_TYPE"));
			 List listQry =  loctypeutil.getLocTypeList(loctype,plant,"");
			 
			 if (listQry.size() > 0) {
					    String trantype="";
						xmlStr += XMLUtils.getXMLHeader();
						xmlStr += XMLUtils.getStartNode("LocationType total='"+ String.valueOf(listQry.size()) + "'");
						for (int i = 0; i < listQry.size(); i++) {
							Map map = (Map) listQry.get(i);
							xmlStr += XMLUtils.getStartNode("record");
							xmlStr += XMLUtils.getXMLNode("locationtype", strUtils.replaceCharacters2SendPDA(map.get("LOC_TYPE_ID").toString()));
							xmlStr += XMLUtils.getXMLNode("desc",    strUtils.replaceCharacters2SendPDA(map.get("LOC_TYPE_DESC").toString()));
								xmlStr += XMLUtils.getEndNode("record");
						}
						
						xmlStr += XMLUtils.getEndNode("LocationType");
					}
				
			
			if (listQry.size()==0) {
				xmlStr = XMLUtils.getXMLMessage(1, "LocationType not found");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	private String get_outBoundOrder_do_temp_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);

			str = doUtil.getPDAMobileSalesOutBoundOrderDoTempDetails(PLANT, aOrdNo,Boolean.FALSE);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		
		return str;
	}
	
	 private String get_do_temp_dono(HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException,
				Exception {
			String plant = "",userid="",dono="",getDono="";
			try {
				//getGST(String key, String plant)
				plant = StrUtils.fString(request.getParameter("PLANT"));
				userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
				dono = StrUtils.fString(request.getParameter("DONO"));
				getDono = dodetDao.getTempDono(dono,plant);
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("GetTempDono");
				xmlStr += XMLUtils.getXMLNode("status", "0");
				xmlStr += XMLUtils.getXMLNode("description", "");
				xmlStr += XMLUtils.getXMLNode("processeddono", getDono);
				xmlStr += XMLUtils.getEndNode("GetTempDono");
			
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;

			}
			return xmlStr;
		}
	 
	
	 private String process_delete_temp_do(HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException,
				Exception {
			Map receiveMaterial_HM = null;
			String plant = "", dono = "", status = "",userid="";
			try {
				plant = StrUtils.fString(request.getParameter("PLANT"));
				dono = StrUtils.fString(request.getParameter("DONO"));
				userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
				xmlStr = "";
				boolean flag = true;
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				
				boolean isValid = dodetDao.isExisitTempDono(ht, " DONO='" + dono +"' ");
				if(isValid){
				    	flag = dodetDao.deleteTempDono(ht," DONO='" + dono +"'");
					    xmlStr = XMLUtils.getXMLHeader();
			            xmlStr = xmlStr + XMLUtils.getStartNode("DeleteDoDetTemp");
						xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
						 xmlStr = xmlStr+ XMLUtils.getXMLNode("description","");
						xmlStr = xmlStr + XMLUtils.getEndNode("DeleteDoDetTemp");
				}
				else
				{
					   xmlStr = XMLUtils.getXMLHeader();
			            xmlStr = xmlStr + XMLUtils.getStartNode("DeleteDoDetTemp");
						xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
						 xmlStr = xmlStr+ XMLUtils.getXMLNode("description","");
						xmlStr = xmlStr + XMLUtils.getEndNode("DeleteDoDetTemp");
				}
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				//throw e;
			}

			return xmlStr;
		}
	 
	 private String process_delete_temp_do_with_item(HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException,
				Exception {
			Map receiveMaterial_HM = null;
			String plant = "", dono = "", status = "",item="",userid="";
			try {
				plant = StrUtils.fString(request.getParameter("PLANT"));
				dono = StrUtils.fString(request.getParameter("DONO"));
				item = StrUtils.fString(request.getParameter("ITEM"));
				userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
				xmlStr = "";
				boolean flag = true;
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				
				boolean isValid = dodetDao.isExisitTempDono(ht, " DONO='" + dono +"'and item='" + item +"' ");
				if(isValid){
				    	flag = dodetDao.deleteTempDono(ht," DONO='" + dono +"'");
					    xmlStr = XMLUtils.getXMLHeader();
			            xmlStr = xmlStr + XMLUtils.getStartNode("DeleteDoDetTemp");
						xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
						 xmlStr = xmlStr+ XMLUtils.getXMLNode("description","");
						xmlStr = xmlStr + XMLUtils.getEndNode("DeleteDoDetTemp");
				}
				else
				{
					   xmlStr = XMLUtils.getXMLHeader();
			            xmlStr = xmlStr + XMLUtils.getStartNode("DeleteDoDetTemp");
						xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
						 xmlStr = xmlStr+ XMLUtils.getXMLNode("description","");
						xmlStr = xmlStr + XMLUtils.getEndNode("DeleteDoDetTemp");
				}
				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				//throw e;
			}

			return xmlStr;
		}
	 
	 
	 private String get_do_dono(HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException,
				Exception {
			String plant = "",userid="",dono="",getDono="";
			try {
				//getGST(String key, String plant)
				plant = StrUtils.fString(request.getParameter("PLANT"));
				userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
				dono = StrUtils.fString(request.getParameter("DONO"));
				getDono = dodetDao.getDono(dono,plant);
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("GetDono");
				xmlStr += XMLUtils.getXMLNode("status", "0");
				xmlStr += XMLUtils.getXMLNode("description", "");
				xmlStr += XMLUtils.getXMLNode("dono", getDono);
				xmlStr += XMLUtils.getEndNode("GetDono");
			
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;

			}
			return xmlStr;
		}
	 private String get_maxplus_printing_item_details(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			String xmlStr = "";
			String plant = "",userid="",item="",itemdesc="";
			try {
				 plant = StrUtils.fString(request.getParameter("PLANT"));
				 userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
				 item= StrUtils.fString(request.getParameter("ITEM_NUM"));
				 itemdesc= StrUtils.fString(request.getParameter("ITEM_DESC"));
				 
				 
				 xmlStr = itemMstUtil.load_maxplus_printing_item_details(plant, item, itemdesc, " AND isnull(itemmst.userfld1,'N')='N'");
				if (xmlStr.equalsIgnoreCase("")) {
					xmlStr = XMLUtils.getXMLMessage(1, "Product not found");
				}
				return xmlStr;
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
		}
	 
	 private String get_maxplus_printing_batch_details(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			String xmlStr = "";
			String plant = "",userid="",item="",itemdesc="",batch="";
			try {
				 plant = StrUtils.fString(request.getParameter("PLANT"));
				 userid = StrUtils.fString(request.getParameter("LOGIN_USER"));
				 item= StrUtils.fString(request.getParameter("ITEM_NUM"));
				 itemdesc= StrUtils.fString(request.getParameter("ITEM_DESC"));
				 batch= StrUtils.fString(request.getParameter("BATCH"));
				 
				 
				 xmlStr = itemMstUtil.load_maxplus_printing_batch_details(plant, item, itemdesc,batch, "");
				if (xmlStr.equalsIgnoreCase("")) {
					xmlStr = XMLUtils.getXMLMessage(1, "Batch details not found");
				}
				return xmlStr;
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
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
