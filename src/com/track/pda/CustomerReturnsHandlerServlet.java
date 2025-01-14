package com.track.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.CustomerReturnDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.BomDAO;

import com.track.dao.ShipHisDAO;
import com.track.db.util.CustomerReturnUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.PalletiationUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;

import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class CustomerReturnsHandlerServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.CustomerReturnServletjson_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.CustomerReturnServletjson_PRINTPLANTMASTERINFO;
	
	private static final long serialVersionUID = -6013768540440713061L;
	private CustomerReturnUtil _customerReturnUtil;
	private InvMstDAO _InvMstDAO = null;
	private String xmlStr = "";
	private String action = "";
	
	private DOUtil _DOUtil = null;

	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {
		_customerReturnUtil = new CustomerReturnUtil();
		_InvMstDAO = new InvMstDAO();
		_DOUtil = new DOUtil();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();

		try {
			action = request.getParameter("action").trim();
			
			this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
					.getParameter("PLANT")), StrUtils.fString(request
							.getParameter("LOGIN_USER")) + " PDA_USER"));
			_customerReturnUtil.setmLogger(mLogger);
			_InvMstDAO.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("LoadItemDetails")) {
				 xmlStr = "";
				xmlStr =this.process_loaditemdetails(request, response);
			}
			else if (action.equalsIgnoreCase("LoadBatchDetails")) {
				xmlStr = "";
				xmlStr =process_loadbatchdetails(request, response);
			}
			else if (action.equalsIgnoreCase("LoadOrderNoDetails")) {
				String xmlStr = "";
				xmlStr =this.process_loadordernodetails(request, response);
			}
			else  if (action.equalsIgnoreCase("customerReturns")) {
				xmlStr = "";
				xmlStr = this.process_customerReturns(request, response);

			}
			else if (action.equalsIgnoreCase("customerReturnsMultiple")) {
				xmlStr = "";

				xmlStr = this
						.process_customerReturnsMultiple(request,response);
			}
			else if (action.equalsIgnoreCase("load_return_customer_details_for_print")) {
				xmlStr = "";
				xmlStr = getReturnCustomerDetailsForPrint(request, response);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, "Unable to process? "
					+ e.getMessage());
		}

		out.write(xmlStr);
		out.close();
	}


	
	

	public void destroy() {
	}

	private String process_loaditemdetails(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		
		try {

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String item = StrUtils.fString(request.getParameter("ITEM"));
			
			String query = " distinct item,isnull(itemdesc,'') itemdesc ";
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("ITEM", item);
		
					
			ArrayList itemList =_customerReturnUtil.getItemDetails(query, ht);
			String items="";	
			String itemdesc="";
			if (itemList.size() > 0) {
				
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("locs total ='"
						+ String.valueOf(itemList.size()) + "'");
				
				for (int i = 0; i < itemList.size(); i++) {
					Map map1 = (Map) itemList.get(i);
					
					 items = (String) map1.get("item");
					 itemdesc = (String) map1.get("itemdesc");
				
					xmlStr += XMLUtils.getStartNode("record");
					
								
					xmlStr += XMLUtils.getXMLNode("item", StrUtils
							.replaceCharacters2SendPDA(items).toString());
					xmlStr += XMLUtils.getXMLNode("itemdesc",   StrUtils
							.replaceCharacters2SendPDA(itemdesc).toString());
					
					xmlStr += XMLUtils.getEndNode("record");
					
				}
			
				xmlStr += XMLUtils.getEndNode("locs");
				
			
				

			} else {
				
				xmlStr = XMLUtils.getXMLMessage(1, "Not a valid Product ");
			}

		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return xmlStr;
	}
	
	private String process_loadbatchdetails(HttpServletRequest request,
			HttpServletResponse response)throws IOException, ServletException,
			Exception  {
		String xmlStr = "";
		try {

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String item = StrUtils.fString(request.getParameter("ITEM"));
			String orderno = StrUtils.fString(request.getParameter("DONO"));
			String batch = StrUtils.fString(request.getParameter("BATCH"));
			
			String query = " distinct isnull(batch,'') as batch ";
			Hashtable<String, String> ht = new Hashtable<String, String>();
			
			ArrayList<HashMap<String, String>> alData = new ArrayList<HashMap<String, String>>();
			ht.put("PLANT", plant);
			ht.put("ITEM", item);
			ht.put("DONO", orderno);
			ht.put("BATCH", batch);
						
			ArrayList al =_customerReturnUtil.getBatchDetails(query, ht);
			String batchs = "";		
			if (al.size() > 0) {
				xmlStr = XMLUtils.getXMLHeader();
				
				xmlStr += XMLUtils.getStartNode("locs total ='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map1 = (Map) al.get(i);
					batchs= (String) map1.get("batch");
					
					xmlStr += XMLUtils.getStartNode("record");
					
					xmlStr += XMLUtils.getXMLNode("batch", StrUtils
							.replaceCharacters2SendPDA(batchs).toString());
					
					
					xmlStr += XMLUtils.getEndNode("record");
			}
				xmlStr += XMLUtils.getEndNode("locs");

			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a valid batch ");
			}

		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return xmlStr;
	}
	
	
	
	public String process_loadbatchdetails2(HttpServletRequest request,
			HttpServletResponse response)throws IOException, ServletException,
			Exception  {
		
	String result = "";
	ArrayList<HashMap<String, String>> alData = new ArrayList<HashMap<String, String>>();
	String plant = StrUtils.fString(request.getParameter("PLANT"));
	String item = StrUtils.fString(request.getParameter("ITEM"));
	String orderno = StrUtils.fString(request.getParameter("DONO"));
	String batch = StrUtils.fString(request.getParameter("BATCH"));
	
	try {
		
		Hashtable<String, String> ht = new Hashtable<String, String>();
		String query = " distinct isnull(batch,'') as batch ";
		
		ht.put("PLANT", plant);
		ht.put("ITEM", item);
		ht.put("DONO", orderno);
		ht.put("BATCH", batch);
	
		alData = _customerReturnUtil.getBatchDetails(query, ht);
		
		if (alData.size() > 0) {
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("locs total='"
					+ alData.size() + "'");
	
			for (HashMap<String, String> hashMap : alData) {
				xmlStr += XMLUtils.getStartNode("record");
			
				xmlStr += XMLUtils.getXMLNode("batch", hashMap.get("batch"));
				
				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("locs");
		
		}
		
		return result;
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		
	}
	
	}
		
	private String process_loadordernodetails(HttpServletRequest request,
			HttpServletResponse response) {
		String xmlStr = "";
		try {

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String item = StrUtils.fString(request.getParameter("ITEMNO"));
			String batch = StrUtils.fString(request.getParameter("BATCH"));
			String dono = StrUtils.fString(request.getParameter("DONO"));
			
			String query = " distinct isnull(dono,'') dono ";
			Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put("PLANT", plant);
			ht.put("ITEM", item);
			ht.put("BATCH", batch);
			ht.put("DONO", dono);
			ArrayList<Map<String, String>> resultV = _customerReturnUtil.getOrderNoDetails(query, ht);
					
			if (resultV.size() > 0) {
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (Map<String, String> map : resultV) {
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("DONO", (String) map
							.get("dono"));
					

				}
				xmlStr += XMLUtils.getEndNode("OrderNoDetails");

			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a valid orderno ");
			}

		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return xmlStr;
	}
	
	private String process_customerReturns(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		Map receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "", CUSTNAME = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", REMARKS = "", EXPIREDATE = "", expdate = "", status = "";
		String ITEM_BATCH = "", CUST_CODE = "", Claimprice = "", ITEM_QTY = "", loc1 = "", 
		REMARK = "", ITEM_LOC = "", RSNDESC = "", OUTBNDORDNO = "",ORDERNO="",REASONCODE="";;
		ArrayList loclist = null;
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGINUSER"));
			ITEM_DESCRIPTION = StrUtils.fString(request
					.getParameter("ITEMDESC"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			ITEM_QTY = StrUtils.fString(request.getParameter("QTY"));
			REMARK = StrUtils.fString(request.getParameter("REMARK"));
			REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"));// Merchant
																				// Name
			ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));// Cliam
																				// price
			if (Claimprice == null || Claimprice.equalsIgnoreCase(""))
				Claimprice = "";
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			List shipqry = null;
			InvMstDAO invdao = new InvMstDAO();
			ShipHisDAO shipdao = new ShipHisDAO();
			itemMstUtil.setmLogger(mLogger);
			invdao.setmLogger(mLogger);
			ITEM_NUM = itemMstUtil.isValidAlternateItemInItemmst(PLANT,
					ITEM_NUM);
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);

			receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);// Serial
																		// No
			receiveMaterial_HM.put(IConstants.QTY, ITEM_QTY);

			receiveMaterial_HM.put(IConstants.REASONCODE, REASONCODE);
			receiveMaterial_HM.put(IConstants.ORDERNO, ORDERNO);
			receiveMaterial_HM.put(IConstants.REMARKS, REMARK);

			xmlStr = "";
			Hashtable htshiphis = new Hashtable();
			htshiphis.put(IDBConstants.PLANT, PLANT);
			htshiphis.put(IDBConstants.ITEM, ITEM_NUM);
			htshiphis.put(IDBConstants.TBL_BATCH_CAPTION, ITEM_BATCH);
			Boolean isProductAvalable = Boolean.valueOf(true);
			
			//Process Check Parent Item Qty
			ItemMstDAO itemmstdao=new ItemMstDAO();
		    Hashtable htItem = new Hashtable();
		    htItem.put(IDBConstants.PLANT, PLANT);
		    htItem.put(IDBConstants.ITEM, ITEM_NUM);
		   
			    //if(itemmstdao.isExisit(htItem, " userfld1='K'") )
			    if(itemmstdao.isExisit(htItem, " ") )
			    {
			    	int qty = Integer.parseInt(((String) ITEM_QTY.trim()));
					if (qty  > 1) {
									throw new Exception("Parent item return quantity should be 1");
					
					}
									    	
			    }
			  //Process Check Parent Item Qty
			
			shipqry = shipdao
					.selectShipHis(
							"distinct isnull(cname,'') as cname,isnull(expirydat,'') as expiredate,loc1,isnull(userfld2,'') as custcode,isnull(STATUS,'') as STATUS",
							htshiphis, "");
			for (int i = 0; i < shipqry.size(); i++) {
				
			

				Map m = (Map) shipqry.get(i);
				EXPIREDATE = (String) m.get("expiredate");
				
				CUSTNAME = (String) m.get("cname");
				
				loc1 = (String) m.get("loc1");
				
				CUST_CODE = (String) m.get("custcode");
				
				status = (String) m.get("STATUS");
				

				if (!status.equalsIgnoreCase("C")) {
					
					throw new Exception(" Selected product " + ITEM_NUM + " - " + ITEM_BATCH + " is not issued yet.");
				}
			}
			
			expdate = EXPIREDATE;
			
			
			if (EXPIREDATE.equals(null) || EXPIREDATE.equals("")) {
				
				throw new Exception("Not a valid Expiry Date");
			}
			EXPIREDATE = EXPIREDATE.replaceAll("-", "");
		
			boolean dtflg = DateUtils.checkDate(EXPIREDATE);// yyyymmdd
		
			if (dtflg == false) {
				throw new Exception("Product ID Expired");
			}
       
			receiveMaterial_HM.put(IConstants.LOC, loc1);
			receiveMaterial_HM.put(IConstants.EXPIREDATE, expdate);
			receiveMaterial_HM.put(IDBConstants.CUSTNAME, CUSTNAME);
			receiveMaterial_HM.put(IDBConstants.CUSTOMER_CODE, CUST_CODE);
			
			
			if (isProductAvalable) {
			
				xmlStr = _customerReturnUtil
						.process_CustomerReturns(receiveMaterial_HM);

				request.getSession().setAttribute(
						"RESULT",
						"Product : " + ITEM_NUM + " "
								+ "returned successfully!");
				response.sendRedirect("jsp/customerReturns.jsp?action=result");
			} else {
				request.getSession().setAttribute("RESULTERROR",
						"Error in returning Product:" + ITEM_NUM);
				response
						.sendRedirect("jsp/customerReturns.jsp?action=resulterror");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("RESULTERROR",
					"Error:" + e.getMessage());
			response.sendRedirect("jsp/customerReturns.jsp?action=resulterror");

			throw e;
		}

		return xmlStr;
	}
	
	
	private String process_customerReturnsMultiple(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException, Exception {
		
		Map receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", CUSTNAME = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", EXPIREDATE = "", expdate = "", status = "";
		String ITEM_BATCH = "", CUST_CODE = "", ITEM_QTY = "", loc1 = "", REMARK = "", 
		CLAIMPRICE = "", OUTBNDORDNO = "",ORDERNO="",REASONCODE="",LOC="";

	
		try {
			HttpSession session = request.getSession();
			
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			List shipqry = null;
			InvMstDAO invdao = new InvMstDAO();
			ShipHisDAO shipdao = new ShipHisDAO();
			itemMstUtil.setmLogger(mLogger);
			invdao.setmLogger(mLogger);

			Boolean transactionHandler = true;
	
           
			PLANT = StrUtils.fString(request.getParameter("PLANT"));

			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
		
			
			Integer itemCount = new Integer(StrUtils.fString(
					request.getParameter("PRODUCT_LIST_SIZE")).trim());
				
			for (int index = 0; index < itemCount; index++) {

				ITEM_NUM = StrUtils.fString(request.getParameter("ITEM" + "_"
						+ index));
				ITEM_DESCRIPTION = StrUtils.fString(request
						.getParameter("ITEMDESC" + "_" + index));
				ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"
						+ "_" + index));
				ITEM_QTY = StrUtils.fString(request.getParameter("QTY" + "_"
						+ index));
				REMARK = StrUtils.fString(request.getParameter("REMARK" + "_"
						+ index));
				REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"
						+ "_" + index));// Merchant Name
				ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"
						+ "_" + index));
				LOC = StrUtils.fString(request.getParameter("FROMLOC"
						+ "_" + index));
			
				ITEM_NUM = itemMstUtil.isValidAlternateItemInItemmst(PLANT,
						ITEM_NUM);
			
				// Process Check Parent Item Qty
				    ItemMstDAO itemmstdao=new ItemMstDAO();
				    Hashtable htItem = new Hashtable();
				    htItem.put(IDBConstants.PLANT, PLANT);
				    htItem.put(IDBConstants.ITEM, ITEM_NUM);
				    double qty=0;
				    qty =Double.parseDouble(( StrUtils.fString(request.getParameter("QTY" + "_"
							+ index))));
			    	qty = StrUtils.RoundDB(qty, IConstants.DECIMALPTS);
					    //if(itemmstdao.isExisit(htItem, " userfld1='K'") )
					    if(itemmstdao.isExisit(htItem, " ") )
					    {
//					    	 qty =Double.parseDouble(( StrUtils.fString(request.getParameter("QTY" + "_"
//									+ index))));
//					    	qty = StrUtils.RoundDB(qty, IConstants.DECIMALPTS);
							if (qty  > 1) {
									throw new Exception("Parent item return quantity should be 1");
							
							}
											    	
					    }
					
					  //End Process Check Parent Item Qty
				ITEM_QTY = String.valueOf(qty);
				ITEM_QTY = StrUtils.formatThreeDecimal(ITEM_QTY);	
				
				receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
				receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);

				receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);// Serial
																			// No
				receiveMaterial_HM.put(IConstants.QTY, ITEM_QTY);

				receiveMaterial_HM.put(IConstants.REASONCODE , REASONCODE);

				receiveMaterial_HM.put(IConstants.REMARKS, REMARK);
				receiveMaterial_HM.put(IConstants.ORDERNO, ORDERNO);
				receiveMaterial_HM.put(IConstants.LOC ,LOC);
				


				xmlStr = "";
				Hashtable htshiphis = new Hashtable();
				htshiphis.put(IDBConstants.PLANT, PLANT);
				htshiphis.put(IDBConstants.ITEM, ITEM_NUM);
				htshiphis.put(IDBConstants.TBL_BATCH_CAPTION, ITEM_BATCH);
				htshiphis.put(IDBConstants.DODET_DONUM, ORDERNO);
				htshiphis.put(IDBConstants.STATUS, "C");
				Boolean isProductAvalable = Boolean.valueOf(true);
				
				shipqry = shipdao
						.selectShipHis(
								"distinct isnull(cname,'') as cname,isnull(expirydat,'') as expiredate,loc1,isnull(userfld2,'') as custcode,isnull(STATUS,'') as STATUS",
								htshiphis, "");
				for (int i = 0; i < shipqry.size(); i++) {

					Map m = (Map) shipqry.get(i);
					EXPIREDATE = (String) m.get("expiredate");
					CUSTNAME = (String) m.get("cname");
					loc1 = (String) m.get("loc1");
					CUST_CODE = (String) m.get("custcode");
					status = (String) m.get("STATUS");

					if (!status.equals("C")) {
						throw new Exception(
								" Selected product " + ITEM_NUM + " - " + ITEM_BATCH + " is not issued yet.");
					}
				}
				expdate = EXPIREDATE;
			
				if (!EXPIREDATE.equals(null) && !EXPIREDATE.equals("")) {
					boolean dtflg = DateUtils.checkDateCustomerReturn(EXPIREDATE);// dd/mm/yyyy
					if (dtflg == false) {
						throw new Exception("Product ID Expired");
					}
				}
			
				
				receiveMaterial_HM.put(IConstants.EXPIREDATE, expdate);
				receiveMaterial_HM.put(IDBConstants.CUSTNAME, CUSTNAME);
				receiveMaterial_HM.put(IDBConstants.CUSTOMER_CODE, CUST_CODE);
				if (isProductAvalable) {
					
				   xmlStr = "";
   				   boolean flag = true;

   				if (flag) {
   				
					xmlStr = _customerReturnUtil.process_CustomerRetunsMultiplePDA(receiveMaterial_HM);
				} else {
					throw new Exception(" Returns is not successfull ");
				}

			}
				
			

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return xmlStr;
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

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	private String getReturnCustomerDetailsForPrint(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", aPlant);
			ht.put("DONO", aOrdNo);
			_DOUtil.setmLogger(mLogger);
			str = _DOUtil.getReturnCustomerDetailsForPrint(aPlant, aOrdNo);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Customer details not found!");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
}
