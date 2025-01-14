package com.track.servlet;

import java.io.IOException;
import java.text.DecimalFormat;
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

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.CurrencyDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.EstHdrDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.db.object.FinCountryTaxType;
import com.track.db.util.ExpensesUtil;
import com.track.db.util.HTReportUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.LocUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*
 * 	Date :     -  Change:               By:
 *  18/06/2014 - Modified action  VIEW_OUTBOUND_SUMMARY_WITH_PRICE to VIEW_OUTBOUND_SUMMARY_ORD_WITH_PRICE, Modifed report to include Tax%,ordPrice,Tax,Total and Grand Total
 *               Included new action VIEW_OUTBOUND_SUMMARY_ORD_WITH_PRICE - New Report to show Issue Price,Tax ,GrandTotal - By Samatha
 *  
 */
/**
 * Servlet implementation class OutboundOrderHandlerServlet
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class OutboundOrderHandlerServlet extends HttpServlet implements
		IMLogger {
//	private boolean printLog = MLoggerConstant.OutboundOrderHandlerServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.OutboundOrderHandlerServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 1L;
	CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OutboundOrderHandlerServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("ACTION")).trim();
	    String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
		this.setMapDataToLogger(this.populateMapData(plant, userName));
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		JSONObject jsonObjectResult = new JSONObject();
		if (action.equals("LOAD_OUTBOUND_ORDER_DETAILS")) {
			String outboundOrderNo = StrUtils.fString(
					request.getParameter("ORDER_NO")).trim();
			jsonObjectResult = this.outboundOrderValidation(plant,
					outboundOrderNo);
		}
		if (action.equals("VALIDATE_BATCH")) {
			jsonObjectResult = this.validateBatch(request);
		}
	  
		if (action.equals("VALIDATE_LOCATION")) {
			jsonObjectResult = this.validateLocation(request);
		}
                
	    if (action.equals("VALIDATE_TO_LOCATION")) {
	            jsonObjectResult = this.validateToLocation(request);
	    }
            
	    if (action.equals("GET_RANGE_BATCH_DETAILS")) {
	       
               
	            jsonObjectResult = this.getRangeTableDetails(request);
	       
	    }
	  //created by navas
	    if (action.equals("VIEW_CONSIGNMENT_SUMMARY_ISSUE")) {
	    jsonObjectResult = this.getConsignmentsmryDetailsWithIssue(request);
	    }
	    //end by navas
	    if (action.equals("VIEW_GOODS_ISSUE_SMRY")) {
	        jsonObjectResult = this.getGoodsIssueDetails(request);
	       	       
	    }
//	    resvi
	    if (action.equals("VIEW_CONSIGNMENT_SUMMARY_ISSUE_WITH_PRICE")) {
		    jsonObjectResult = this.getConsignmentsmryDetailsWithIssuePrice(request);
		    }
	    
//	    ends
	    
	    
	
	    if (action.equals("VIEW_OUTBOUND_SUMMARY_ORD_WITH_PRICE")) {
	        jsonObjectResult = this.getOutboundsmryDetailsWithOrderPrice(request);
	       	       
	    }
	    
	    //Created by Imthiyas : for void summary
	    if (action.equals("VIEW_VOID_SUMMARY_WITH_PRICE")) {
	    	jsonObjectResult = this.getVoidsmryDetailsWithOrderPrice(request);
	    }

	    if (action.equals("VIEW_POSRETURN_SUMMARY_WITH_PRICE")) {
	    	jsonObjectResult = this.getPOSreturnsmryDetailsWithOrderPrice(request);
	    }

	    if (action.equals("VIEW_FOC_SUMMARY")) {
	    	jsonObjectResult = this.getPOSFOCsmryDetailsWithOrderPrice(request);
	    }

	    if (action.equals("VIEW_POSEXPENSES_SUMMARY_WITH_PRICE")) {
	    	jsonObjectResult = this.getPOSExpensesDetailsWithPrice(request);
	    }
	    
	    //CREATED BY NAVAS
	    if (action.equals("VIEW_CONSIGNMENT_SUMMARY_ORD_WITH_PRICE")) {
 	        jsonObjectResult = this.getConsignmentsmryDetailsWithOrderPrice(request);
	       	       
	    }
	    if (action.equals("VIEW_OUTBOUND_SUMMARY_ISSUE_WITH_PRICE")) {
	        jsonObjectResult = this.getOutboundsmryDetailsWithIssuePrice(request);
	       	       
	    }
	    if (action.equals("VIEW_OUTBOUND_SUMMARY_ISSUE")) {
	        jsonObjectResult = this.getOutboundsmryDetailsWithIssue(request);
	       	       
	    }
	    if (action.equals("getConsignmentRemarksDetails")) {
	        jsonObjectResult = this.getConsignmentRemarksDetails(request);	       	       
	    }
	    
	    if (action.equals("VIEW_OUTBOUND_DETAILS_PRINT")) {
	        jsonObjectResult = this.getOutboundDetailsToPrint(request);
	       	       
	    }
	    
	    if (action.equals("VALIDATE_CUSTOMER_TYPE")) {
	    	String customertype = StrUtils.fString(request.getParameter("CUSTOMERTYPE")).trim();
	        jsonObjectResult = this.validateCutomerType(plant,customertype );
	       	       
	    }
	    
	    if (action.equals("VALIDATE_SUPPLIER")) {
	    	String supplier = StrUtils.fString(request.getParameter("SUPPLIER")).trim();
	        jsonObjectResult = this.validateSupplier(plant,supplier );
	       	       
	    }
	    if (action.equals("VIEW_OUTBOUND_SUMMARY_ISSUE_WITH_AVGCOST")) {
	        jsonObjectResult = this.getOutboundsmryIssueDetailsWithAverageCost(request,baseCurrency);
	       	       
	    }
		if (action.equals("INVOICE")) {
	        jsonObjectResult = this.generateINVOICE(plant, userName);	       	       
	    }
		if (action.equals("ITEMRETURN")) {
			jsonObjectResult = this.generateITEMRETURN(plant, userName);	       	       
		}
		
		if (action.equals("INVOICE_QUOTES")) {
	        jsonObjectResult = this.generateINVOICEQUOTE(plant, userName);	       	       
	    }
		
	    if (action.equals("VIEW_GOODS_ISSUE_WP_SMRY")) {
	        jsonObjectResult = this.getGoodsIssueWithPriceDetails(request);
	       	       
	    }
	    
	    if (action.equals("GINO")) {
	        jsonObjectResult = this.generateGINO(plant, userName);	       	       
	    }
	    if (action.equals("VGINO")) {
	    	String gino = StrUtils.fString(
					request.getParameter("GINO")).trim();
	        jsonObjectResult = this.checkGINO(plant, gino);	       	       
	    }
	    if (action.equals("getRemarksDetails")) {
	        jsonObjectResult = this.getRemarksDetails(request);	       	       
	    }
	    if (action.equals("getRemarksEstDetails")) {
	        jsonObjectResult = this.getRemarksEstDetails(request);	       	       
	    }
	    if (action.equals("PAKINGLIST_DELIVERYNOTE")) {
	        jsonObjectResult = this.generatePAKINGLIST_DELIVERYNOTE(plant, userName);	       	       
	    }
		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObjectResult.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}

	
	private JSONObject validateLocation(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String location = StrUtils.fString(request.getParameter("LOC")).trim();
			String item = StrUtils.fString(request.getParameter("ITEMNO")).trim();
			String userId = StrUtils.fString(request.getParameter("USERID")).trim();

			InvMstDAO invMstDAO = new InvMstDAO();
		    LocUtil _LocUtil = new LocUtil();
			invMstDAO.setmLogger(mLogger);
			ArrayList resultList = null;
			String nonstocktype = new ItemMstDAO().getNonStockFlag(plant, item);
	    	if(nonstocktype.equalsIgnoreCase("Y")){
	    		resultList = _LocUtil.getLocDetails(location,plant,userId);
	    	}else{
			    resultList = invMstDAO.getOutBoundPickingLocByWMS(plant,item, location, userId);
	    	}
			if (resultList.size() > 0) {
				Map resultMap = (Map) resultList.get(0);
				if (resultMap.size() > 0) {
					resultJson.put("status", "100");
				} else {
					resultJson.put("status", "99");
				}
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	

	
	
	private JSONObject validateCutomerType(String plant, 
			String customertype) {
		JSONObject resultJson = new JSONObject();
		try {
		   CustomerBeanDAO _customerBeanDAO = new CustomerBeanDAO();
			_customerBeanDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			if (_customerBeanDAO.isExistsProductCustomerType(ht,
					" CUSTOMER_TYPE_ID like '"+ customertype + "%'  OR CUSTOMER_TYPE_DESC LIKE '" + customertype + "%'")) {
							
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}

    
    private JSONObject validateToLocation(HttpServletRequest request) {
            JSONObject resultJson = new JSONObject();
            try {
                    String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
                    String location = StrUtils.fString(request.getParameter("LOC")) .trim();
                    String userId = StrUtils.fString(request.getParameter("USERID")).trim();

                    UserLocUtil userUtil = new UserLocUtil();
                    userUtil.setmLogger(mLogger);
                    boolean exits = userUtil.isValidLocInLocmstForUser(plant, userId,  location );

                            if (exits) {
                                    resultJson.put("status", "100");
                            } else {
                                    resultJson.put("status", "99");
                            }
                   
                    return resultJson;
            } catch (Exception daRE) {
                    resultJson.put("status", "99");
                    return resultJson;
            }
    }

	
	private JSONObject validateBatch(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String location = StrUtils.fString(request.getParameter("LOC")).trim();
			String item = StrUtils.fString(request.getParameter("ITEMNO")).trim();
			String batch = StrUtils.fString(request.getParameter("BATCH")).trim();
			String uom = StrUtils.fString(request.getParameter("UOM")).trim();
			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);
			ArrayList resultList = invMstDAO.getTotalQuantityForOutBoundPickingBatchByWMSMuliUOM(plant, item, location, batch,uom);
			JSONObject resultJsonObject = new JSONObject();
			if (resultList.size() > 0) {
				Map resultMap = (Map) resultList.get(0);
				if (resultMap.size() > 0) {
					resultJsonObject.put("BATCH", resultMap.get("batch"));
					resultJsonObject.put("QTY", resultMap.get("qty"));
					resultJson.put("result", resultJsonObject);
					resultJson.put("status", "100");
				} else {
					resultJson.put("status", "99");
				}
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
        
    
	
	private JSONObject outboundOrderValidation(String plant,
			String outboundOrderNo) {
		JSONObject resultJson = new JSONObject();
		try {
                
                  
			JSONObject resultJsonObject = new JSONObject();
			DoHdrDAO doHdrDAO = new DoHdrDAO();
			doHdrDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.DONO, outboundOrderNo);
			Map resultMap = doHdrDAO.selectRow("custName,jobNum", ht);
			if (resultMap.size() > 0) {
				resultJsonObject.put("CUSTNAME", resultMap.get("custName"));
				resultJsonObject.put("JOBNUM", resultMap.get("jobNum"));
				resultJson.put("result", resultJsonObject);
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	
	 private JSONObject getGoodsIssueDetails(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        HTReportUtil reptUtil       = new HTReportUtil();
	        //DateUtils _dateUtils = new DateUtils();
	        ArrayList QryList  = new ArrayList();
	        
//	        DecimalFormat decformat = new DecimalFormat("#,##0.00");
	       
	        //StrUtils strUtils = new StrUtils();
	        String fdate="",tdate="";
	        try {
	        	
	        	   String PLANT= StrUtils.fString(request.getParameter("PLANT"));
		           String ITEMNO    = StrUtils.fString(request.getParameter("ITEM"));
		           String  PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
		           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
		           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
		           String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
		           String  CUSTOMER = StrUtils.fString(request.getParameter("CNAME"));
		           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
//		           String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
		           String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
		           String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
		           String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
		           String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
		           String	LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
				   String   TO_ASSIGNEE = StrUtils.fString(request.getParameter("TOASSINEE"));
				   String  LOANASSIGNEE = StrUtils.fString(request.getParameter("LOANASSINEE"));
				   String REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"));
				   String  BATCH = StrUtils.fString(request.getParameter("BATCH"));
				   String  LOC = StrUtils.fString(request.getParameter("LOC"));
//				    
				  		        	
	           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)
	        	   fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) +  FROM_DATE.substring(0, 2);
	           
	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	        	   tdate = TO_DATE.substring(6) +  TO_DATE.substring(3, 5)+  TO_DATE.substring(0, 2);

	           if (StrUtils.fString(ITEMNO).length() >0)
		         {
					 ItemMstUtil itemMstUtil = new ItemMstUtil();
					 itemMstUtil.setmLogger(mLogger);
					 String temItem = itemMstUtil.isValidAlternateItemInItemmst( PLANT, ITEMNO);
					 if (temItem != "") {
					 	ITEMNO = temItem;
					 } else {
					 	throw new Exception("Product Details not found!");
					 }
		         } 
	   
	           Hashtable ht = new Hashtable();
	        
		        if (StrUtils.fString(ITEMNO).length() > 0)      ht.put("a.ITEM", ITEMNO);
				if (StrUtils.fString(ORDERNO).length() > 0)     ht.put("a.DONO", ORDERNO);
				if (StrUtils.fString(CUSTOMER).length() > 0)    ht.put("a.CNAME", CUSTOMER);
				if (StrUtils.fString(TO_ASSIGNEE).length() > 0) ht.put("a.CNAME_TO", TO_ASSIGNEE);
				if (StrUtils.fString(LOANASSIGNEE).length() > 0)ht.put("a.CNAME_LOAN", LOANASSIGNEE);
				if (StrUtils.fString(ORDERTYPE).length() > 0)   ht.put("d.ORDERTYPE", ORDERTYPE);
				if (StrUtils.fString(BATCH).length() > 0)       ht.put("a.BATCH", BATCH);
				if (StrUtils.fString(LOC).length() > 0)         ht.put("a.LOC", LOC);
				if(StrUtils.fString(LOC_TYPE_ID).length() > 0)  ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
				if(StrUtils.fString(PRD_TYPE_ID).length() > 0)  ht.put("c.ITEMTYPE",PRD_TYPE_ID);
		        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("c.PRD_BRAND_ID",PRD_BRAND_ID);
		        if(StrUtils.fString(PRD_CLS_ID).length() > 0)   ht.put("c.PRD_CLS_ID",PRD_CLS_ID);
		        if(StrUtils.fString(REASONCODE).length() > 0)   ht.put("a.REMARK",REASONCODE);
		      
		      
		       QryList = reptUtil.getPickingSummaryList(ht, fdate,tdate, DIRTYPE, PLANT,PRD_DESCRIP);  
		     
		      
	            if (QryList.size() > 0) {
	            int iIndex = 0,Index = 0;
	             int irow = 0;
	            double totalPickQty = 0,totalIssueQty=0;String lastProduct="";
//	            double unitCost=0,totalRecvCost1=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
	            String strDate="";
	                for (int iCnt =0; iCnt<QryList.size(); iCnt++){
	                            String result="";
	                            Map lineArr = (Map) QryList.get(iCnt);
	                            String itemCode =(String)lineArr.get("item");
	                            String dono = (String)lineArr.get("dono");
	                            //total PickQty
								totalPickQty=totalPickQty+Double.parseDouble(((String)lineArr.get("pickqty").toString()));
								totalIssueQty=totalIssueQty+Double.parseDouble(((String)lineArr.get("issueqty").toString()));
		                         
	                            
	                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                            JSONObject resultJsonInt = new JSONObject();
	                            
	                      strDate=(String)lineArr.get("transactiondate");   
	                      if(strDate.equals("1900-01-01")||strDate.equals("")||strDate==null)
		                  {
		              			strDate=(String)lineArr.get("transactioncratdate"); //transactioncratdate
		              	  }
	                       if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(itemCode))){
	                    	 Index = Index + 1;
	                    	result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
	                        + "<td>" + dono + "</td>"
					        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(strDate) + "</td>"
	                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("cname")) + "</td>"
	                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("item")) + "</td>"
	                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("itemdesc")) + "</td>"
	                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("loc")) + "</td>"
	                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("batch")) + "</td>"
	                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("expiredate")) + "</td>"
	                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("ordqty")) + "</td>"
	                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("pickqty")) + "</td>"
	                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("issueqty")) + "</td>"
	                        + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("uom")) + "</td>"
	                        + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("lnstat")) + "</td>"
	                        + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remark")) + "</td>"
	                        + "<tr>";
	                        if(iIndex+1 == QryList.size()){
	                                irow=irow+1;
	                                bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";  

	                                result +=  "<TR bgcolor ="+bgcolor+" >"
	                                        +"<TD colspan=7></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+StrUtils.formatNum(String.valueOf(totalPickQty))+"</b></TD><TD align= \"right\"><b>"+StrUtils.formatNum(String.valueOf(totalIssueQty))+"</b></TD><TD></TD><TD></TD><TD></TD><TD></TD>    </TR>";
	                                   
	                         } }else{
	                          
	                        	 totalPickQty=totalPickQty-Double.parseDouble(((String)lineArr.get("pickqty").toString()));
	                        	 totalPickQty = StrUtils.RoundDB(totalPickQty,2);
	                        	 totalIssueQty=totalIssueQty-Double.parseDouble(((String)lineArr.get("issueqty").toString()));
	                        	 totalIssueQty = StrUtils.RoundDB(totalIssueQty,2);
	                        	 result +=  "<TR bgcolor ="+bgcolor+" >"
	                                        +"<TD colspan=7></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+StrUtils.formatNum(String.valueOf(totalPickQty))+"</b></TD><TD align= \"right\"><b>"+StrUtils.formatNum(String.valueOf(totalIssueQty))+"</b></TD><TD></TD><TD></TD><TD></TD><TD></TD>    </TR>";
	                           irow=irow+1;
	                           Index = 0;
	                           Index=Index+1;
	                           bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
	                            result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
	                            		 + "<td>" + dono + "</td>"
	         					        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(strDate) + "</td>"
	         	                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("cname")) + "</td>"
	         	                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("item")) + "</td>"
	         	                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("itemdesc")) + "</td>"
	         	                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("loc")) + "</td>"
	         	                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("batch")) + "</td>"
	         	                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("expiredate")) + "</td>"
	         	                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("ordqty")) + "</td>"
	         	                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("pickqty")) + "</td>"
	         	                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("issueqty")) + "</td>"
	         	                        + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("uom")) + "</td>"
	         	                        + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("lnstat")) + "</td>"
	         	                        + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remark")) + "</td>"
	                            + "<tr>";
   
	                            totalPickQty=Double.parseDouble(((String)lineArr.get("pickqty").toString()));
	                            totalPickQty = StrUtils.RoundDB(totalPickQty,2);
	                            totalIssueQty=Double.parseDouble(((String)lineArr.get("issueqty").toString()));
	                            totalIssueQty = StrUtils.RoundDB(totalIssueQty,2);
	                                 if(iIndex+1 == QryList.size()){ 
	                                     irow=irow+1;
	                                     bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
	                                     result +=  "<TR bgcolor ="+bgcolor+" >"
	 	                                        +"<TD colspan=7></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+StrUtils.formatNum(String.valueOf(totalPickQty))+"</b></TD><TD align= \"right\"><b>"+StrUtils.formatNum(String.valueOf(totalIssueQty))+"</b></TD><TD></TD><TD></TD><TD></TD><TD></TD>    </TR>";
	                        }
	                        }
	                             irow=irow+1;
	                             iIndex=iIndex+1;
	                             lastProduct = itemCode;
	                             
	                          /*   if(QryList.size()==iCnt+1){
	                            	 irow=irow+1;
	                                 bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
	                                 result +=  "<TR bgcolor ="+bgcolor+" >"
	                                         +"<TD colspan=11></TD> <TD align= \"right\"><b>Grand Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(costGrandTot,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(taxGrandTot,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(costWTaxGrandTot,2))+"</b></TD><TD></TD> </TR>";

	                             }*/
	                          
	                            resultJsonInt.put("ISSUEDETAILS", result);
	                         
	                            jsonArray.add(resultJsonInt);

	                }
	               
	                    resultJson.put("items", jsonArray);
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
	                    resultJson.put("items", jsonArray);

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
	        return resultJson;
	}
	 
	 //CREATED BY NAVAS FEB2O
	 
	 private JSONObject getConsignmentsmryDetailsWithOrderPrice(HttpServletRequest request) {
         JSONObject resultJson = new JSONObject();
         JSONArray jsonArray = new JSONArray();
         JSONArray jsonArrayErr = new JSONArray();
         HTReportUtil movHisUtil       = new HTReportUtil();
         //DateUtils _dateUtils = new DateUtils();
         ArrayList movQryList  = new ArrayList();
         
//         DecimalFormat decformat = new DecimalFormat("#,##0.00");
        
         //StrUtils strUtils = new StrUtils();
         String fdate="",tdate="",taxby="";
     
         try {
         
            String PLANT= StrUtils.fString(request.getParameter("PLANT"));
            String ITEMNO    = StrUtils.fString(request.getParameter("ITEM"));
            String  PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
            String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
            String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
            String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
            String  CUSTNAME = StrUtils.fString(request.getParameter("CNAME"));
            
            String FROMWAREHOUSE = StrUtils.fString(request.getParameter("FROMWAREHOUSE"));
            String TOWAREHOUSE = StrUtils.fString(request.getParameter("TOWAREHOUSE"));
            String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
            String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
            String  PICKSTATUS = StrUtils.fString(request.getParameter("PICKSTATUS"));
            String  ISSUESTATUS = StrUtils.fString(request.getParameter("ISSUESTATUS"));
            String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
            //Start code added by Bruhan for product brand,type on 2/sep/13
            String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
            String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
            String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
            String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
            //End code added by Bruhan for product brand,type on 2/sep/13 
            String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
            String SORT = StrUtils.fString(request.getParameter("SORT"));
            String EMPNO = StrUtils.fString(request.getParameter("EMPNO"));
            String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMERTYPE"));
            String UOM = StrUtils.fString(request.getParameter("UOM"));
            
            if(ISSUESTATUS.equalsIgnoreCase("OPEN"))
				ISSUESTATUS="N";
			else if(ISSUESTATUS.equalsIgnoreCase("PARTIALLY ISSUED"))
				ISSUESTATUS="O";
			else if(ISSUESTATUS.equalsIgnoreCase("ISSUED"))
				ISSUESTATUS="C";
            
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
            String curDate =DateUtils.getDate();
            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

            if (FROM_DATE.length()>5)

             fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



            if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
            if (TO_DATE.length()>5)
            tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

               
    
            Hashtable ht = new Hashtable();
            if(StrUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
            if(StrUtils.fString(ITEMNO).length() > 0)       ht.put("B.ITEM",ITEMNO);
            if(StrUtils.fString(ORDERNO).length() > 0)      ht.put("A.TONO",ORDERNO);
            
            if (StrUtils.fString(FROMWAREHOUSE).length() > 0)
            	ht.put("FROMWAREHOUSE", FROMWAREHOUSE);

            	if (StrUtils.fString(TOWAREHOUSE).length() > 0)
            	ht.put("TOWAREHOUSE", TOWAREHOUSE);
            //if(StrUtils.fString(CUSTNAME).length() > 0)     ht.put("A.CUSTNAME",strUtils.InsertQuotes(CUSTNAME));
            if(StrUtils.fString(ORDERTYPE).length() > 0)    ht.put("A.ORDERTYPE",ORDERTYPE);
            if(StrUtils.fString(ISSUESTATUS).length() > 0){
            	if(ISSUESTATUS.equalsIgnoreCase("DRAFT"))
            	{
            	ht.put("ORDER_STATUS","Draft");
            	ht.put("B.LNSTAT","N");
            	}
            	else
            	{
            		ht.put("B.LNSTAT",ISSUESTATUS);

            	if(ISSUESTATUS.equalsIgnoreCase("N"))
            	ht.put("ORDER_STATUS","OPEN");
            	}
            	}  
            	
            if(StrUtils.fString(PICKSTATUS).length() > 0)  {
            	if(PICKSTATUS.equalsIgnoreCase("DRAFT"))
            	{
            	ht.put("ORDER_STATUS","Draft");
            	ht.put("B.PICKSTATUS","N");
            	}
            	else
            	{
            		ht.put("B.PICKSTATUS",PICKSTATUS);

            	if(PICKSTATUS.equalsIgnoreCase("N"))
            	ht.put("ORDER_STATUS","OPEN");
            	}
            	} 
            	
            if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
	        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
	        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
	        if(StrUtils.fString(statusID).length() > 0) ht.put("A.STATUS_ID",statusID);
	        if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("C.PRD_DEPT_ID",PRD_DEPT_ID);
	        if(StrUtils.fString(EMPNO).length() > 0) ht.put("A.EMPNO",EMPNO);
	        if(StrUtils.fString(UOM).length() > 0) ht.put("B.SKTUOM",UOM);
	        if(StrUtils.fString(CUSTOMERTYPE).length() > 0) ht.put("CUSTTYPE",CUSTOMERTYPE);
	        taxby=_PlantMstDAO.getTaxBy(PLANT);
	        if(taxby.equalsIgnoreCase("BYORDER"))
			{
            	movQryList = movHisUtil.getCustomerTOInvoiceSummary(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTNAME,SORT);
			}
	        else
	        {
	        	movQryList = movHisUtil.getCustomerTOInvoiceSummaryByProductGst(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTNAME,SORT);
	        }
	                    
             if (movQryList.size() > 0) {
             int Index = 0;
//              int iIndex = 0,irow = 0;
              
//             double sumprdQty = 0;String lastProduct="";
             
             double totalOrdPrice=0,totaltax=0,totOrdPriceWTax=0,priceGrandTot=0,taxGrandTot=0,priceWTaxGrandTot=0;//issPriceSubTot=0,OrdPriceGrandTot=0,taxGrandTotal=0,priceWTaxGrandTotal=0;
             ;
             String customertypeid="",customertypedesc="";//customerstatusdesc="",customerstatusid="",
             
                 for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                       
//                         String result="",custcode="";
                         Map lineArr = (Map) movQryList.get(iCnt);
//                         if(SORT.equalsIgnoreCase("PRODUCT"))
//                         {
//                        	 custcode=(String)lineArr.get("item");
//                         }
//                         else
//                         {
//                        	 custcode=(String)lineArr.get("custcode");
//                         }

                         String tono = (String)lineArr.get("tono");
                         Float gstpercentage =  Float.parseFloat(((String) lineArr.get("Tax").toString())) ;
             			            			
                         
           	             
           	          
           	             //indiviual subtotal price details
           	             double orderPrice = Double.parseDouble((String)lineArr.get("ordPrice"));
           	             //orderPrice = StrUtils.RoundDB(orderPrice,2);
               	         // double tax = Double.parseDouble((String)lineArr.get("taxval"));
//               	          double tax = Double.parseDouble((String)lineArr.get("taxval"));
               	          
           	          double tax =0.0;
         	           int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
         	           FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
         	           if(taxid != 0){
         	        	   if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){	              	        		   
         	        		   tax = (orderPrice*gstpercentage)/100;
         	        	   } else 
         	        		 gstpercentage=Float.parseFloat("0.000");
         	        	   
         	           }else 
         	        		 gstpercentage=Float.parseFloat("0.000");
               	          
               	          
                            // double tax = (orderPrice*gstpercentage)/100;
                         double ordPricewTax = orderPrice+tax;
                         
                         //total price details
                         totalOrdPrice=totalOrdPrice+Double.parseDouble(((String)lineArr.get("ordPrice").toString()));
                         totaltax  =totaltax + tax;
                         totOrdPriceWTax = totOrdPriceWTax+ordPricewTax;
                         
                         //Grand total details
                         priceGrandTot = priceGrandTot+Double.parseDouble((String)lineArr.get("ordPrice"));;
                         taxGrandTot = taxGrandTot+tax;
                         priceWTaxGrandTot = priceWTaxGrandTot+ordPricewTax;
                      
              
//                         String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                         JSONObject resultJsonInt = new JSONObject();
                       
//                         customerstatusid = customerBeanDAO.getCustomerStatusId(PLANT,(String)lineArr.get("custname"));
//                         if(customerstatusid == null || customerstatusid.equals(""))
//             			   {
//             			   customerstatusdesc="";
//             			   }
//             			   else
//             			   {
//             			    customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(PLANT,customerstatusid);
//             			   }
                         
                         
                         customertypeid = customerBeanDAO.getCustomerTypeId(PLANT,(String)lineArr.get("custname"));
               			 if(customertypeid == null || customertypeid.equals(""))
               			 {
               				customertypedesc="";
               			 }
               			 else
               			 {
               				customertypedesc = customerBeanDAO.getCustomerTypeDesc(PLANT,customertypeid);
               			 }
                         
               			String qtyOrValue =(String)lineArr.get("qtyor");
               			String qtyPickValue =(String)lineArr.get("qtypick");
               			String qtyValue =(String)lineArr.get("qty");
               			String qtyInvValue =(String)lineArr.get("qtyac");
               			String unitprice = (String)lineArr.get("unitprice");
               			String gstpercentageValue= String.valueOf(gstpercentage);
               			String taxValue = String.valueOf(tax);
               			String orderPriceValue=String.valueOf(orderPrice);
               			String ordPricewTaxValue = String.valueOf(ordPricewTax);
               			
                        float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
                        float qtyPickVal ="".equals(qtyPickValue) ? 0.0f :  Float.parseFloat(qtyPickValue);
                        float qtyInvVal ="".equals(qtyInvValue) ? 0.0f :  Float.parseFloat(qtyInvValue);
//                        float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
//                        float unitPriceVal ="".equals(unitprice) ? 0.0f :  Float.parseFloat(unitprice);
                        float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);
                        float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);
                        float orderPriceVal ="".equals(orderPriceValue) ? 0.0f :  Float.parseFloat(orderPriceValue);
                        float ordPricewTaxVal ="".equals(ordPricewTaxValue) ? 0.0f :  Float.parseFloat(ordPricewTaxValue);
                        
                        if(qtyOrVal==0f){
                        	qtyOrValue="0.000";
                        }else{
                        	qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        
                        
                        if(qtyInvVal==0f){
                        	qtyInvValue="0.000";
                        }else{
                        	qtyInvValue=qtyInvValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        
                        if(qtyPickVal==0f){
                        	qtyPickValue="0.000";
                        }else{
                        	qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
//                        if(qtyVal==0f){
//                        	qtyValue="0.000";
//                        }else{
//                        	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
//                        }
//                        if(unitPriceVal==0f){
//                        	unitprice="0.00000";
//                        }else{
//                        	unitprice=unitprice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
//                        }
                        if(gstpercentageVal==0f){
                        	gstpercentageValue="0.000";
                        }else{
                        	gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(taxVal==0f) {
                        	taxValue="0.000";
                        }else {
                        	taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(orderPriceVal==0f) {
                        	orderPriceValue="0.00000";
                        }else {
                        	orderPriceValue=orderPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(ordPricewTaxVal==0f) {
                        	ordPricewTaxValue="0.00000";
                        }else {
                        	ordPricewTaxValue=ordPricewTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        
                       // if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(custcode))){
                        	Index = Index + 1;
                      	  	resultJsonInt.put("Index", (Index));
                            resultJsonInt.put("tono", (tono));
                            resultJsonInt.put("orderType", StrUtils.fString((String)lineArr.get("ordertype")));
                            resultJsonInt.put("jobnum", StrUtils.fString((String)lineArr.get("jobnum")));
                            resultJsonInt.put("custname", StrUtils.fString((String)lineArr.get("custname")));
                            resultJsonInt.put("customertypedesc", StrUtils.fString(customertypedesc));
//                            resultJsonInt.put("customerstatusdesc", StrUtils.fString(customerstatusdesc));
                            
                            resultJsonInt.put("fromwarehouse", StrUtils.fString((String)lineArr.get("fromwarehouse")));
                            resultJsonInt.put("towarehouse", StrUtils.fString((String)lineArr.get("towarehouse")));
                            
                            resultJsonInt.put("item", StrUtils.fString((String)lineArr.get("item")));
                            resultJsonInt.put("itemdesc", StrUtils.fString((String)lineArr.get("itemdesc")));
                            resultJsonInt.put("DetailItemDesc", StrUtils.fString((String)lineArr.get("DetailItemDesc")));
                            resultJsonInt.put("CollectionDate", StrUtils.fString((String)lineArr.get("CollectionDate")));
                            resultJsonInt.put("UOM", StrUtils.fString((String)lineArr.get("UOM")));
                            resultJsonInt.put("qtyor", qtyOrValue);
                            resultJsonInt.put("qtypick",qtyPickValue);
                            resultJsonInt.put("qty",qtyValue);
                            resultJsonInt.put("qtyac",qtyInvValue);
                            resultJsonInt.put("empname", StrUtils.fString((String)lineArr.get("empname")));
                            resultJsonInt.put("avgcost", unitprice);
//                            resultJsonInt.put("gstpercentage",gstpercentageValue);
                            resultJsonInt.put("gstpercentage", StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) gstpercentageValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
                            resultJsonInt.put("issueAvgcost", orderPriceValue);
                            resultJsonInt.put("tax", taxValue);
                            resultJsonInt.put("issAvgcostwTax", ordPricewTaxValue);
                            resultJsonInt.put("status_id", StrUtils.fString((String)lineArr.get("status_id")));
                            resultJsonInt.put("remarks", StrUtils.fString((String)lineArr.get("remarks")));
                            resultJsonInt.put("remarks2", StrUtils.fString((String)lineArr.get("remarks2")));
                            resultJsonInt.put("tolnno", StrUtils.fString((String)lineArr.get("tolnno")));
                            resultJsonInt.put("users", StrUtils.fString((String)lineArr.get("users")));
                        	   jsonArray.add(resultJsonInt);

                       /*  result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                         +"<td align=center>" +Index+ "</td>"
                         + "<td><a href=/track/deleveryorderservlet?TONO=" +tono+ "&Submit=View&RFLAG=4>" + tono + "</a></td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("ordertype")) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("jobnum")) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("custname")) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customertypedesc) + "</td>"
                           + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customerstatusdesc) + "</td>"
//                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks")) + "</td>"
//                          + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks2")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("item")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("itemdesc")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("DetailItemDesc")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("CollectionDate"))+ "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qtyor")) + "</td>"
                         + "<td  align = right>&nbsp;&nbsp;&nbsp;"+ StrUtils.formatNum((String)lineArr.get("qtypick")) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qty")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("empname")) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(unitprice) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" +  gstpercentage + "</td>"
                          + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(orderPrice) + "</td>"
                           + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(tax) + "</td>"
                          + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(ordPricewTax)
                         + "</td>"
                         + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("status_id")) + "</td>"
                        + "<td align = center>&nbsp;&nbsp;&nbsp;"
                        +"<a href='#' style='text-decoration: none;' onClick=\"javascript:popUpWin('outboundOrderPrdRemarksList.jsp?REMARKS1="+StrUtils.fString((String)lineArr.get("remarks"))+"&REMARKS2="+StrUtils.fString((String)lineArr.get("remarks2"))+"&ITEM=" + StrUtils.fString((String)lineArr.get("item"))+"&TONO="+tono+"&ITEM="+StrUtils.fString((String)lineArr.get("item"))+"&DOLNNO="+(String)lineArr.get("dolnno")+"\');\">"
                        +"&#9432;"
                        +"</a>"
                        +"</td>"
                         + "<tr>";
                         if(iIndex+1 == movQryList.size()){
                                 irow=irow+1;
                                 bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";  

                               result +=  "<TR bgcolor ="+bgcolor+" >"
                                  +"<TD colspan=16></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalOrdPrice,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totOrdPriceWTax,2))+"</b></TD><TD></TD> </TR>";
                                    
                          } }else{
                           
                        	  totalOrdPrice=totalOrdPrice-Double.parseDouble(((String)lineArr.get("ordPrice").toString()));
                        	  totalOrdPrice = StrUtils.RoundDB(totalOrdPrice,2);
                        	  
                        	  totaltax=totaltax-tax;
                        	  totaltax = StrUtils.RoundDB(totaltax,2);
                        	  
                        	  totOrdPriceWTax=totOrdPriceWTax-ordPricewTax;
                        	  totOrdPriceWTax = StrUtils.RoundDB(totOrdPriceWTax,2);

                        	   result +=  "<TR bgcolor ="+bgcolor+" >"
                                       +"<TD colspan=16></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalOrdPrice,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totOrdPriceWTax,2))+"</b></TD><TD></TD><TD></TD> </TR>";
                             irow=irow+1;
                             Index = 0;
                             Index=Index+1;
                             bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                             result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                             +"<td align=center>" +Index+ "</td>"
                             + "<td><a href=/track/deleveryorderservlet?TONO=" +tono+ "&Submit=View&RFLAG=4>" + tono + "</a></td>"
                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("ordertype")) + "</td>"
                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("jobnum")) + "</td>"
                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("custname")) + "</td>"
                              + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customertypedesc) + "</td>"
                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customerstatusdesc) + "</td>"
//                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks")) + "</td>"
//                              + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks2")) + "</td>"
                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("item"))+ "</td>"
                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("itemdesc"))+ "</td>"
                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("DetailItemDesc")) + "</td>"
                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("CollectionDate"))+ "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qtyor")) + "</td>"
                             + "<td  align = right>&nbsp;&nbsp;&nbsp;"+ StrUtils.formatNum((String)lineArr.get("qtypick"))  + "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qty"))+ "</td>"
                              + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("empname"))+ "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(unitprice) + "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" +  gstpercentage + "</td>"
                              + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(orderPrice) + "</td>"
                               + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(tax) + "</td>"
                              + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(ordPricewTax)+ "</td>"
                             + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("status_id")) + "</td>"
                             + "<td align = center>&nbsp;&nbsp;&nbsp;"
	                        +"<a href='#' style='text-decoration: none;' onClick=\"javascript:popUpWin('outboundOrderPrdRemarksList.jsp?REMARKS1="+StrUtils.fString((String)lineArr.get("remarks"))+"&REMARKS2="+StrUtils.fString((String)lineArr.get("remarks2"))+"&ITEM=" + StrUtils.fString((String)lineArr.get("item"))+"&TONO="+tono+"&ITEM="+StrUtils.fString((String)lineArr.get("item"))+"&DOLNNO="+(String)lineArr.get("dolnno")+"\');\">"
	                        +"&#9432;"
	                        +"</a>"
	                        +"</td>"
                             + "<tr>";
                       
                         
                             totalOrdPrice=Double.parseDouble(((String)lineArr.get("ordPrice").toString()));
                       	     totalOrdPrice = StrUtils.RoundDB(totalOrdPrice,2);
                       	  
	                       	  totaltax=tax;
	                       	  totaltax = StrUtils.RoundDB(totaltax,2);
                       	  
	                       	  totOrdPriceWTax=ordPricewTax;
	                       	  totOrdPriceWTax = StrUtils.RoundDB(totOrdPriceWTax,2);

                          	
                                  if(iIndex+1 == movQryList.size()){ 
                                      irow=irow+1;
                                      bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                                      result +=  "<TR bgcolor ="+bgcolor+" >"
                                              +"<TD colspan=16></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalOrdPrice,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totOrdPriceWTax,2))+"</b></TD><TD></TD> </TR>";
                                        
                         }
                         }
                              irow=irow+1;
                              iIndex=iIndex+1;
                              lastProduct = custcode;
                              if(movQryList.size()==iCnt+1){
                             	 irow=irow+1;
                                  bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                                  result +=  "<TR bgcolor ="+bgcolor+" >"
                                          +"<TD colspan=16></TD> <TD align= \"right\"><b>Grand Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(priceGrandTot,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(taxGrandTot,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(priceWTaxGrandTot,2))+"</b></TD><TD></TD> </TR>";

                              }
                              
                             resultJsonInt.put("OUTBOUNDDETAILS", result);
                          
                             jsonArray.add(resultJsonInt);*/

                 }
                
                     resultJson.put("items", jsonArray);
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
                     resultJson.put("items", jsonArray);

                     resultJson.put("errors", jsonArrayErr);
             }
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
         return resultJson;
 }
	 //END BY NAVAS

	
	 // Start code modified by Bruhan for outorderwithprice on 17/12/12 
	 private JSONObject getOutboundsmryDetailsWithOrderPrice(HttpServletRequest request) {
         JSONObject resultJson = new JSONObject();
         JSONArray jsonArray = new JSONArray();
         JSONArray jsonArrayErr = new JSONArray();
         HTReportUtil movHisUtil       = new HTReportUtil();
         //DateUtils _dateUtils = new DateUtils();
         ArrayList movQryList  = new ArrayList();
         
//         DecimalFormat decformat = new DecimalFormat("#,##0.00");
        
         //StrUtils strUtils = new StrUtils();
         String fdate="",tdate="",taxby="";
     
         try {
         
            String PLANT= StrUtils.fString(request.getParameter("PLANT"));
            String ITEMNO    = StrUtils.fString(request.getParameter("ITEM"));
            String  PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
            String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
            String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
            String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
            String  CUSTNAME = StrUtils.fString(request.getParameter("CNAME"));
            String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
            String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
            String  PICKSTATUS = StrUtils.fString(request.getParameter("PICKSTATUS"));
            String  ISSUESTATUS = StrUtils.fString(request.getParameter("ISSUESTATUS"));
            String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
            //Start code added by Bruhan for product brand,type on 2/sep/13
            String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
            String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
            String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
            String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
            //End code added by Bruhan for product brand,type on 2/sep/13 
            String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
            String SORT = StrUtils.fString(request.getParameter("SORT"));
            String EMPNO = StrUtils.fString(request.getParameter("EMPNO"));
            String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMERTYPE"));
            String UOM = StrUtils.fString(request.getParameter("UOM"));
            String POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
            String curDate =DateUtils.getDate();
            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

            if (FROM_DATE.length()>5)

             fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



            if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
            if (TO_DATE.length()>5)
            tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

               
    
            Hashtable ht = new Hashtable();
            if(StrUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
            if(StrUtils.fString(ITEMNO).length() > 0)       ht.put("B.ITEM",ITEMNO);
            if(StrUtils.fString(ORDERNO).length() > 0)      ht.put("A.DONO",ORDERNO);
            //if(StrUtils.fString(CUSTNAME).length() > 0)     ht.put("A.CUSTNAME",strUtils.InsertQuotes(CUSTNAME));
            if(StrUtils.fString(ORDERTYPE).length() > 0)    ht.put("A.ORDERTYPE",ORDERTYPE);
            if(StrUtils.fString(ISSUESTATUS).length() > 0){
            	if(ISSUESTATUS.equalsIgnoreCase("DRAFT"))
            	{
            	ht.put("ORDER_STATUS","Draft");
            	ht.put("B.LNSTAT","N");
            	}
            	else
            	{
            		ht.put("B.LNSTAT",ISSUESTATUS);

            	if(ISSUESTATUS.equalsIgnoreCase("N"))
            	ht.put("ORDER_STATUS","OPEN");
            	}
            	}  
            	
            if(StrUtils.fString(PICKSTATUS).length() > 0)  {
            	if(PICKSTATUS.equalsIgnoreCase("DRAFT"))
            	{
            	ht.put("ORDER_STATUS","Draft");
            	ht.put("B.PICKSTATUS","N");
            	}
            	else
            	{
            		ht.put("B.PICKSTATUS",PICKSTATUS);

            	if(PICKSTATUS.equalsIgnoreCase("N"))
            	ht.put("ORDER_STATUS","OPEN");
            	}
            	} 
            	
            if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
	        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
	        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
	        if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("C.PRD_DEPT_ID",PRD_DEPT_ID);
	        if(StrUtils.fString(statusID).length() > 0) ht.put("A.STATUS_ID",statusID);
	        if(StrUtils.fString(EMPNO).length() > 0) ht.put("A.EMPNO",EMPNO);
	        if(StrUtils.fString(UOM).length() > 0) ht.put("B.SKTUOM",UOM);
	        if(StrUtils.fString(CUSTOMERTYPE).length() > 0) ht.put("CUSTTYPE",CUSTOMERTYPE);
	        taxby=_PlantMstDAO.getTaxBy(PLANT);
	        if(taxby.equalsIgnoreCase("BYORDER"))
			{
            	movQryList = movHisUtil.getCustomerDOInvoiceSummary(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTNAME,SORT,POSSEARCH);
			}
	        else
	        {
	        	movQryList = movHisUtil.getCustomerDOInvoiceSummaryByProductGst(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTNAME,SORT,POSSEARCH);
	        }
	                    
             if (movQryList.size() > 0) {
             int Index = 0;
//              int iIndex = 0,irow = 0;
              
//             double sumprdQty = 0;String lastProduct="";
             
             double totalOrdPrice=0,totaltax=0,totOrdPriceWTax=0,priceGrandTot=0,taxGrandTot=0,priceWTaxGrandTot=0;//issPriceSubTot=0,OrdPriceGrandTot=0,taxGrandTotal=0,priceWTaxGrandTotal=0;
             ;
             String customerstatusid="",customerstatusdesc="",customertypeid="",customertypedesc="";
             
                 for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                       
//                         String result="",custcode="";
                         Map lineArr = (Map) movQryList.get(iCnt);
//                         if(SORT.equalsIgnoreCase("PRODUCT"))
//                         {
//                        	 custcode=(String)lineArr.get("item");
//                         }
//                         else
//                         {
//                        	 custcode=(String)lineArr.get("custcode");
//                         }

                         String dono = (String)lineArr.get("dono");
                         Float gstpercentage =  Float.parseFloat(((String) lineArr.get("Tax").toString())) ;
             			            			
                         
           	             
           	          
           	             //indiviual subtotal price details
           	             double orderPrice = Double.parseDouble((String)lineArr.get("ordPrice"));
           	             //orderPrice = StrUtils.RoundDB(orderPrice,2);
               	         // double tax = Double.parseDouble((String)lineArr.get("taxval"));
//               	          double tax = Double.parseDouble((String)lineArr.get("taxval"));
                            // double tax = (orderPrice*gstpercentage)/100;
               	          
               	       double tax =0.0;
          	           int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
          	           FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
          	           if(taxid != 0){
          	        	   if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){	              	        		   
          	        		   tax = (orderPrice*gstpercentage)/100;
          	        	   } else 
          	        		 gstpercentage=Float.parseFloat("0.000");
          	        	   
          	           }else 
          	        		 gstpercentage=Float.parseFloat("0.000");
               	          
                         double ordPricewTax = orderPrice+tax;
                         
                         //total price details
                         totalOrdPrice=totalOrdPrice+Double.parseDouble(((String)lineArr.get("ordPrice").toString()));
                         totaltax  =totaltax + tax;
                         totOrdPriceWTax = totOrdPriceWTax+ordPricewTax;
                         
                         //Grand total details
                         priceGrandTot = priceGrandTot+Double.parseDouble((String)lineArr.get("ordPrice"));;
                         taxGrandTot = taxGrandTot+tax;
                         priceWTaxGrandTot = priceWTaxGrandTot+ordPricewTax;
                      
              
//                         String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                         JSONObject resultJsonInt = new JSONObject();
                       
                         //customerstatusid = customerBeanDAO.getCustomerStatusId(PLANT,(String)lineArr.get("custname"));
                         customerstatusid = (String)lineArr.get("CustomerStatusId");
                         if(customerstatusid == null || customerstatusid.equals(""))
             			   {
             			   customerstatusdesc="";
             			   }
             			   else
             			   {
             			    customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(PLANT,customerstatusid);
             			   }
                         
                         
                         //customertypeid = customerBeanDAO.getCustomerTypeId(PLANT,(String)lineArr.get("custname"));
                         customertypeid = (String)lineArr.get("Customer_Type_Id");
               			 if(customertypeid == null || customertypeid.equals(""))
               			 {
               				customertypedesc="";
               			 }
               			 else
               			 {
               				customertypedesc = customerBeanDAO.getCustomerTypeDesc(PLANT,customertypeid);
               			 }
                         
               			String qtyOrValue =(String)lineArr.get("qtyor");
               			String qtyPickValue =(String)lineArr.get("qtypick");
               			String qtyValue =(String)lineArr.get("qty");
               			String unitprice = (String)lineArr.get("unitprice");
               			String gstpercentageValue= String.valueOf(gstpercentage);
               			String taxValue = String.valueOf(tax);
               			String orderPriceValue=String.valueOf(orderPrice);
               			String ordPricewTaxValue = String.valueOf(ordPricewTax);
               			
                        float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
                        float qtyPickVal ="".equals(qtyPickValue) ? 0.0f :  Float.parseFloat(qtyPickValue);
                        float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
                        float unitPriceVal ="".equals(unitprice) ? 0.0f :  Float.parseFloat(unitprice);
                        float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);
                        float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);
                        float orderPriceVal ="".equals(orderPriceValue) ? 0.0f :  Float.parseFloat(orderPriceValue);
                        float ordPricewTaxVal ="".equals(ordPricewTaxValue) ? 0.0f :  Float.parseFloat(ordPricewTaxValue);
                        
                        if(qtyOrVal==0f){
                        	qtyOrValue="0.000";
                        }else{
                        	qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(qtyPickVal==0f){
                        	qtyPickValue="0.000";
                        }else{
                        	qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(qtyVal==0f){
                        	qtyValue="0.000";
                        }else{
                        	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(unitPriceVal==0f){
                        	unitprice="0.00000";
                        }else{
                        	unitprice=unitprice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(gstpercentageVal==0f){
                        	gstpercentageValue="0.000";
                        }else{
                        	gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(taxVal==0f) {
                        	taxValue="0.000";
                        }else {
                        	taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(orderPriceVal==0f) {
                        	orderPriceValue="0.00000";
                        }else {
                        	orderPriceValue=orderPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(ordPricewTaxVal==0f) {
                        	ordPricewTaxValue="0.00000";
                        }else {
                        	ordPricewTaxValue=ordPricewTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        
                       // if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(custcode))){
                        	Index = Index + 1;
                      	  	resultJsonInt.put("Index", (Index));
                            resultJsonInt.put("dono", (dono));
                            resultJsonInt.put("orderType", StrUtils.fString((String)lineArr.get("ordertype")));
                            resultJsonInt.put("jobnum", StrUtils.fString((String)lineArr.get("jobnum")));
                            resultJsonInt.put("custname", StrUtils.fString((String)lineArr.get("custname")));
                            resultJsonInt.put("customertypedesc", StrUtils.fString(customertypedesc));
                            resultJsonInt.put("customerstatusdesc", StrUtils.fString(customerstatusdesc));
                            resultJsonInt.put("item", StrUtils.fString((String)lineArr.get("item")));
                            resultJsonInt.put("itemdesc", StrUtils.fString((String)lineArr.get("itemdesc")));
                            resultJsonInt.put("DetailItemDesc", StrUtils.fString((String)lineArr.get("DetailItemDesc")));
                            resultJsonInt.put("CollectionDate", StrUtils.fString((String)lineArr.get("CollectionDate")));
                            resultJsonInt.put("UOM", StrUtils.fString((String)lineArr.get("UOM")));
//                            resultJsonInt.put("qtyor", qtyOrValue);
                            resultJsonInt.put("qtyor", StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) qtyOrValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
//                            resultJsonInt.put("qtypick",qtyPickValue);
                            resultJsonInt.put("qtypick", StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) qtyPickValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
//                            resultJsonInt.put("qty",qtyValue);
                            resultJsonInt.put("qty", StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) qtyValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
                            resultJsonInt.put("empname", StrUtils.fString((String)lineArr.get("empname")));
                            resultJsonInt.put("avgcost", unitprice);
//                            resultJsonInt.put("gstpercentage",gstpercentageValue);
                            resultJsonInt.put("gstpercentage", StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) gstpercentageValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
                            resultJsonInt.put("issueAvgcost", orderPriceValue);
                            resultJsonInt.put("tax", taxValue);
                            resultJsonInt.put("issAvgcostwTax", ordPricewTaxValue);
                            resultJsonInt.put("status_id", StrUtils.fString((String)lineArr.get("status_id")));
                            resultJsonInt.put("remarks", StrUtils.fString((String)lineArr.get("remarks")));
                            resultJsonInt.put("remarks2", StrUtils.fString((String)lineArr.get("remarks2")));
                            resultJsonInt.put("dolnno", StrUtils.fString((String)lineArr.get("dolnno")));
                            resultJsonInt.put("users", StrUtils.fString((String)lineArr.get("users")));
                        	   jsonArray.add(resultJsonInt);

                       /*  result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                         +"<td align=center>" +Index+ "</td>"
                         + "<td><a href=/track/deleveryorderservlet?DONO=" +dono+ "&Submit=View&RFLAG=4>" + dono + "</a></td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("ordertype")) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("jobnum")) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("custname")) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customertypedesc) + "</td>"
                           + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customerstatusdesc) + "</td>"
//                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks")) + "</td>"
//                          + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks2")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("item")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("itemdesc")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("DetailItemDesc")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("CollectionDate"))+ "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qtyor")) + "</td>"
                         + "<td  align = right>&nbsp;&nbsp;&nbsp;"+ StrUtils.formatNum((String)lineArr.get("qtypick")) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qty")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("empname")) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(unitprice) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" +  gstpercentage + "</td>"
                          + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(orderPrice) + "</td>"
                           + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(tax) + "</td>"
                          + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(ordPricewTax)
                         + "</td>"
                         + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("status_id")) + "</td>"
                        + "<td align = center>&nbsp;&nbsp;&nbsp;"
                        +"<a href='#' style='text-decoration: none;' onClick=\"javascript:popUpWin('outboundOrderPrdRemarksList.jsp?REMARKS1="+StrUtils.fString((String)lineArr.get("remarks"))+"&REMARKS2="+StrUtils.fString((String)lineArr.get("remarks2"))+"&ITEM=" + StrUtils.fString((String)lineArr.get("item"))+"&DONO="+dono+"&ITEM="+StrUtils.fString((String)lineArr.get("item"))+"&DOLNNO="+(String)lineArr.get("dolnno")+"\');\">"
                        +"&#9432;"
                        +"</a>"
                        +"</td>"
                         + "<tr>";
                         if(iIndex+1 == movQryList.size()){
                                 irow=irow+1;
                                 bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";  

                               result +=  "<TR bgcolor ="+bgcolor+" >"
                                  +"<TD colspan=16></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalOrdPrice,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totOrdPriceWTax,2))+"</b></TD><TD></TD> </TR>";
                                    
                          } }else{
                           
                        	  totalOrdPrice=totalOrdPrice-Double.parseDouble(((String)lineArr.get("ordPrice").toString()));
                        	  totalOrdPrice = StrUtils.RoundDB(totalOrdPrice,2);
                        	  
                        	  totaltax=totaltax-tax;
                        	  totaltax = StrUtils.RoundDB(totaltax,2);
                        	  
                        	  totOrdPriceWTax=totOrdPriceWTax-ordPricewTax;
                        	  totOrdPriceWTax = StrUtils.RoundDB(totOrdPriceWTax,2);

                        	   result +=  "<TR bgcolor ="+bgcolor+" >"
                                       +"<TD colspan=16></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalOrdPrice,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totOrdPriceWTax,2))+"</b></TD><TD></TD><TD></TD> </TR>";
                             irow=irow+1;
                             Index = 0;
                             Index=Index+1;
                             bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                             result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                             +"<td align=center>" +Index+ "</td>"
                             + "<td><a href=/track/deleveryorderservlet?DONO=" +dono+ "&Submit=View&RFLAG=4>" + dono + "</a></td>"
                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("ordertype")) + "</td>"
                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("jobnum")) + "</td>"
                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("custname")) + "</td>"
                              + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customertypedesc) + "</td>"
                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customerstatusdesc) + "</td>"
//                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks")) + "</td>"
//                              + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks2")) + "</td>"
                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("item"))+ "</td>"
                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("itemdesc"))+ "</td>"
                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("DetailItemDesc")) + "</td>"
                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("CollectionDate"))+ "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qtyor")) + "</td>"
                             + "<td  align = right>&nbsp;&nbsp;&nbsp;"+ StrUtils.formatNum((String)lineArr.get("qtypick"))  + "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qty"))+ "</td>"
                              + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("empname"))+ "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(unitprice) + "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" +  gstpercentage + "</td>"
                              + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(orderPrice) + "</td>"
                               + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(tax) + "</td>"
                              + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(ordPricewTax)+ "</td>"
                             + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("status_id")) + "</td>"
                             + "<td align = center>&nbsp;&nbsp;&nbsp;"
	                        +"<a href='#' style='text-decoration: none;' onClick=\"javascript:popUpWin('outboundOrderPrdRemarksList.jsp?REMARKS1="+StrUtils.fString((String)lineArr.get("remarks"))+"&REMARKS2="+StrUtils.fString((String)lineArr.get("remarks2"))+"&ITEM=" + StrUtils.fString((String)lineArr.get("item"))+"&DONO="+dono+"&ITEM="+StrUtils.fString((String)lineArr.get("item"))+"&DOLNNO="+(String)lineArr.get("dolnno")+"\');\">"
	                        +"&#9432;"
	                        +"</a>"
	                        +"</td>"
                             + "<tr>";
                       
                         
                             totalOrdPrice=Double.parseDouble(((String)lineArr.get("ordPrice").toString()));
                       	     totalOrdPrice = StrUtils.RoundDB(totalOrdPrice,2);
                       	  
	                       	  totaltax=tax;
	                       	  totaltax = StrUtils.RoundDB(totaltax,2);
                       	  
	                       	  totOrdPriceWTax=ordPricewTax;
	                       	  totOrdPriceWTax = StrUtils.RoundDB(totOrdPriceWTax,2);

                          	
                                  if(iIndex+1 == movQryList.size()){ 
                                      irow=irow+1;
                                      bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                                      result +=  "<TR bgcolor ="+bgcolor+" >"
                                              +"<TD colspan=16></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalOrdPrice,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totOrdPriceWTax,2))+"</b></TD><TD></TD> </TR>";
                                        
                         }
                         }
                              irow=irow+1;
                              iIndex=iIndex+1;
                              lastProduct = custcode;
                              if(movQryList.size()==iCnt+1){
                             	 irow=irow+1;
                                  bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                                  result +=  "<TR bgcolor ="+bgcolor+" >"
                                          +"<TD colspan=16></TD> <TD align= \"right\"><b>Grand Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(priceGrandTot,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(taxGrandTot,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(priceWTaxGrandTot,2))+"</b></TD><TD></TD> </TR>";

                              }
                              
                             resultJsonInt.put("OUTBOUNDDETAILS", result);
                          
                             jsonArray.add(resultJsonInt);*/

                 }
                
                     resultJson.put("items", jsonArray);
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
                     resultJson.put("items", jsonArray);

                     resultJson.put("errors", jsonArrayErr);
             }
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
         return resultJson;
 }
	 // Start code modified by imthiyas for VOIDSUMMARY on 17/06/22 
	 private JSONObject getVoidsmryDetailsWithOrderPrice(HttpServletRequest request) {
		 JSONObject resultJson = new JSONObject();
		 JSONArray jsonArray = new JSONArray();
		 JSONArray jsonArrayErr = new JSONArray();
		 HTReportUtil movHisUtil       = new HTReportUtil();
		 ArrayList movQryList  = new ArrayList();
		 JSONObject resultJsonInt = new JSONObject();
		 
		 String fdate="",tdate="",taxby="";
		 String ITEMS="",FROMDATE="",TODATE="",ORDER="",CUSTOMER="",OUTLETCODE="",TERMINALCODE="",CUST_NAME="",SALES_PERSON="",PRD_TYPE="",PRD_BRAND="",PRD_CLS="";
		 
		 try {
			 
			 String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			 String TYPE= StrUtils.fString(request.getParameter("SRC"));
			 ITEMS    = StrUtils.fString(request.getParameter("ITEM"));
			 FROMDATE   = StrUtils.fString(request.getParameter("FDATE"));
			 TODATE = StrUtils.fString(request.getParameter("TDATE"));
			 if(FROMDATE==null) FROMDATE=""; else FROMDATE = FROMDATE.trim();
			 String curDates =DateUtils.getDate();
			 if(FROMDATE.length()<0||FROMDATE==null||FROMDATE.equalsIgnoreCase(""))FROMDATE=curDates;
			 if (FROMDATE.length()>5)
				 fdate    = FROMDATE.substring(6)+"-"+ FROMDATE.substring(3,5)+"-"+FROMDATE.substring(0,2);				 
			 if(TODATE==null) TODATE=""; else TODATE = TODATE.trim();
			 if (TODATE.length()>5)
				 tdate    = TODATE.substring(6)+"-"+ TODATE.substring(3,5)+"-"+TODATE.substring(0,2);
			 ORDER = StrUtils.fString(request.getParameter("ORDERNO"));
			 CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
//        	 OUTLETNAME = StrUtils.fString(request.getParameter("OUTLET_NAME"));
//        	 TERMINALNAME = StrUtils.fString(request.getParameter("TERMINALNAME"));
			 OUTLETCODE = StrUtils.fString(request.getParameter("OUTLETCODE"));
			 TERMINALCODE = StrUtils.fString(request.getParameter("TERMINALCODE"));
			 CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			 SALES_PERSON = StrUtils.fString(request.getParameter("SALES_MAN"));
			 ITEMS = StrUtils.fString(request.getParameter("ITEM"));
			 PRD_TYPE = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));//sub category
			 PRD_BRAND = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
			 PRD_CLS = StrUtils.fString(request.getParameter("PRD_CLS_ID"));//category
			 
			 String ENABLE_POS =new PlantMstDAO().getispos(PLANT);
			 String ISTAXREGISTRED =new PlantMstDAO().getistaxregistred(PLANT);
			 
			 Hashtable ht = new Hashtable();
			 String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
			 DecimalFormat decimalFormat = new DecimalFormat("#.#####");
			 decimalFormat.setRoundingMode(java.math.RoundingMode.FLOOR);
			 ArrayList prodGstList = new ArrayList();
			 movQryList = movHisUtil.getVoidSummary(ht, fdate,tdate,PLANT,CUSTOMER,ORDER,OUTLETCODE,TERMINALCODE,CUST_NAME,SALES_PERSON,ITEMS,PRD_BRAND,PRD_CLS,PRD_TYPE);
				 
			 if (movQryList.size() > 0) {
				 
			 float subtotal=0;
			 double gst=0,total=0;
			 float gsttotal=0;
			 float grandtotal=0,gstpercentage=0,prodgstsubtotal1=0;
				 
			 gst=prodgstsubtotal1;
			 for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
				 Map lineArr = (Map) movQryList.get(iCnt);
				 int iIndex = iCnt + 1;
				 String dono = (String) lineArr.get("dono");
				 String TOTALValue =  decimalFormat.format(total); 
				 String SUBTOTAL = (String)lineArr.get("subtotal");
				 
				 float totalVal="".equals(TOTALValue) ? 0.0f :  Float.parseFloat(TOTALValue);
				 float subTotalVal="".equals(SUBTOTAL) ? 0.0f :  Float.parseFloat(SUBTOTAL);
				 
				 double tot = Double.parseDouble(TOTALValue);
				 TOTALValue = StrUtils.addZeroes(tot, numberOfDecimal);
				 double subtot = Double.parseDouble(SUBTOTAL);
				 SUBTOTAL = StrUtils.addZeroes(subtot, numberOfDecimal);
				 
				 String item_discounttype = StrUtils.fString((String)lineArr.get("ORDERDISCOUNTTYPE"));
				 String item_discount = StrUtils.fString((String)lineArr.get("ORDERDISCOUNT"));
				 String ISdiscount = StrUtils.fString((String)lineArr.get("ISORDERDISCOUNTTAX"));
				 String discountANDtype = StrUtils.fString((String)lineArr.get("ORDERDISCOUNT"))+"("+StrUtils.fString((String)lineArr.get("ORDERDISCOUNTTYPE"))+")";
					 
				 //discount calculation
				 double discount =0,dDiscount=0;
				 if(item_discounttype.equalsIgnoreCase("%")){
					 dDiscount = Double.parseDouble(item_discount);
					 discount = ((Double.parseDouble(SUBTOTAL))/100)*dDiscount;
				 }else{
					 dDiscount = Double.parseDouble(item_discount);
					 discount =  dDiscount;
				 }
				 
				 //tax calculation
				 double tax =0.0;
				 int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
				 gstpercentage =  Float.parseFloat(((String) lineArr.get("outbound_gst").toString())) ;
				 FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
				 if(taxid != 0){
					 if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){
						 if(ISdiscount.equalsIgnoreCase("1"))
							 tax = ((Double.parseDouble(SUBTOTAL)-discount)*gstpercentage)/100;
						 else 
							 tax = (Double.parseDouble(SUBTOTAL)*gstpercentage)/100;
					 }else 
						 gstpercentage=Float.parseFloat("0.000");
				 }else 
					 gstpercentage=Float.parseFloat("0.000");
				 
				 //total amount
				 double finaltotal = ((Double.parseDouble(SUBTOTAL))-discount+tax);
				 if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0"))
					  finaltotal = ((Double.parseDouble(SUBTOTAL))-discount);
				 
				 int Indexs= 0;
				 Indexs = Indexs + 1;
				 resultJsonInt.put("Index", (Indexs));
				 resultJsonInt.put("CollectionDate", StrUtils.fString((String)lineArr.get("CollectionDate")));
				 resultJsonInt.put("dono", (dono));
				 resultJsonInt.put("custname", StrUtils.fString((String)lineArr.get("custname")));
				 resultJsonInt.put("avgcost",SUBTOTAL);
				 resultJsonInt.put("issAvgcostwTax",discount);
				 resultJsonInt.put("tax", tax);
				 resultJsonInt.put("issueAvgcost",finaltotal);
				 jsonArray.add(resultJsonInt);
			 }
			 resultJson.put("POS", jsonArray);
			 resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			 resultJsonInt.put("ERROR_CODE", "100");
			 jsonArrayErr.add(resultJsonInt);
			 resultJson.put("errors", jsonArrayErr);
			 } else {
				 resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				 resultJsonInt.put("ERROR_CODE", "99");
				 jsonArrayErr.add(resultJsonInt);
				 jsonArray.add("");
				 resultJson.put("POS", jsonArray);
				 
				 resultJson.put("errors", jsonArrayErr);
			 }
		 } catch (Exception e) {
			 jsonArray.add("");
			 resultJson.put("POS", jsonArray);
			 resultJson.put("SEARCH_DATA", "");
			 resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			 resultJsonInt.put("ERROR_CODE", "98");
			 jsonArrayErr.add(resultJsonInt);
			 resultJson.put("ERROR", jsonArrayErr);
		 }
		 return resultJson;
	 }

	 private JSONObject getPOSFOCsmryDetailsWithOrderPrice(HttpServletRequest request) {
		 JSONObject resultJson = new JSONObject();
		 JSONArray jsonArray = new JSONArray();
		 JSONArray jsonArrayErr = new JSONArray();
		 HTReportUtil movHisUtil       = new HTReportUtil();
		 ArrayList movQryList  = new ArrayList();
		 JSONObject resultJsonInt = new JSONObject();
		 
		 String fdate="",tdate="",taxby="";
		 String ITEMS="",FROMDATE="",TODATE="",ORDER="",CUSTOMER="",OUTLETCODE="",TERMINALCODE="",CUST_NAME="",SALES_PERSON="",PRD_TYPE="",PRD_BRAND="",PRD_CLS="";
		 
		 try {
			 
			 String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			 String TYPE= StrUtils.fString(request.getParameter("SRC"));
			 ITEMS    = StrUtils.fString(request.getParameter("ITEM"));
			 FROMDATE   = StrUtils.fString(request.getParameter("FDATE"));
			 TODATE = StrUtils.fString(request.getParameter("TDATE"));
			 if(FROMDATE==null) FROMDATE=""; else FROMDATE = FROMDATE.trim();
			 String curDates =DateUtils.getDate();
			 if(FROMDATE.length()<0||FROMDATE==null||FROMDATE.equalsIgnoreCase(""))FROMDATE=curDates;
			 if (FROMDATE.length()>5)
				 fdate    = FROMDATE.substring(6)+"-"+ FROMDATE.substring(3,5)+"-"+FROMDATE.substring(0,2);				 
			 if(TODATE==null) TODATE=""; else TODATE = TODATE.trim();
			 if (TODATE.length()>5)
				 tdate    = TODATE.substring(6)+"-"+ TODATE.substring(3,5)+"-"+TODATE.substring(0,2);
			 ORDER = StrUtils.fString(request.getParameter("ORDERNO"));
			 CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
			 OUTLETCODE = StrUtils.fString(request.getParameter("OUTLETCODE"));
			 TERMINALCODE = StrUtils.fString(request.getParameter("TERMINALCODE"));
			 CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			 SALES_PERSON = StrUtils.fString(request.getParameter("SALES_MAN"));
			 ITEMS = StrUtils.fString(request.getParameter("ITEM"));
			 PRD_TYPE = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));//sub category
			 PRD_BRAND = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
			 PRD_CLS = StrUtils.fString(request.getParameter("PRD_CLS_ID"));//category
			 
			 Hashtable ht = new Hashtable();
			 String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
			 DecimalFormat decimalFormat = new DecimalFormat("#.#####");
			 decimalFormat.setRoundingMode(java.math.RoundingMode.FLOOR);
			 ArrayList prodGstList = new ArrayList();
			 movQryList = movHisUtil.getPOSFOCSummary(ht, fdate,tdate,PLANT,CUSTOMER,ORDER,OUTLETCODE,TERMINALCODE,CUST_NAME,SALES_PERSON,ITEMS,PRD_BRAND,PRD_CLS,PRD_TYPE);
			 
			 if (movQryList.size() > 0) {
				 
				 float subtotal=0;
				 double gst=0,total=0;
				 float gsttotal=0;
				 float grandtotal=0,gstpercentage=0,prodgstsubtotal1=0;
				 
				 gst=prodgstsubtotal1;
				 for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					 Map lineArr = (Map) movQryList.get(iCnt);
					 int iIndex = iCnt + 1;
					 String dono = (String) lineArr.get("dono");
					 String QTY = (String)lineArr.get("QTYOR");
					 
					 double qtytot = Double.parseDouble(QTY);
					 QTY = StrUtils.addZeroes(qtytot, "3");
					 
					 int Indexs= 0;
					 Indexs = Indexs + 1;
					 resultJsonInt.put("Index", (Indexs));
					 resultJsonInt.put("CollectionDate", StrUtils.fString((String)lineArr.get("CollectionDate")));
					 resultJsonInt.put("dono", (dono));
					 resultJsonInt.put("custname", StrUtils.fString((String)lineArr.get("custname")));
					 resultJsonInt.put("item", StrUtils.fString((String)lineArr.get("item")));
					 resultJsonInt.put("qty",QTY);
					 jsonArray.add(resultJsonInt);
				 }
				 resultJson.put("POS", jsonArray);
				 resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				 resultJsonInt.put("ERROR_CODE", "100");
				 jsonArrayErr.add(resultJsonInt);
				 resultJson.put("errors", jsonArrayErr);
			 } else {
				 resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				 resultJsonInt.put("ERROR_CODE", "99");
				 jsonArrayErr.add(resultJsonInt);
				 jsonArray.add("");
				 resultJson.put("POS", jsonArray);
				 
				 resultJson.put("errors", jsonArrayErr);
			 }
		 } catch (Exception e) {
			 jsonArray.add("");
			 resultJson.put("POS", jsonArray);
			 resultJson.put("SEARCH_DATA", "");
			 resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			 resultJsonInt.put("ERROR_CODE", "98");
			 jsonArrayErr.add(resultJsonInt);
			 resultJson.put("ERROR", jsonArrayErr);
		 }
		 return resultJson;
	 }

	 private JSONObject getPOSExpensesDetailsWithPrice(HttpServletRequest request) {
		 JSONObject resultJson = new JSONObject();
		 JSONArray jsonArray = new JSONArray();
		 JSONArray jsonArrayErr = new JSONArray();
		 ExpensesUtil movHisUtil       = new ExpensesUtil();
		 ArrayList movQryList  = new ArrayList();
		 JSONObject resultJsonInt = new JSONObject();
		 
		 String fdate="",tdate="",taxby="";
		 String FROMDATE="",TODATE="",OUTLETCODE="",TERMINALCODE="",CUST_NAME="";
		 
		 try {
			 
			 String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			 String TYPE= StrUtils.fString(request.getParameter("SRC"));
			 FROMDATE   = StrUtils.fString(request.getParameter("FDATE"));
			 TODATE = StrUtils.fString(request.getParameter("TDATE"));
			 if(FROMDATE==null) FROMDATE=""; else FROMDATE = FROMDATE.trim();
			 String curDates =DateUtils.getDate();
			 if(FROMDATE.length()<0||FROMDATE==null||FROMDATE.equalsIgnoreCase(""))FROMDATE=curDates;
			 if (FROMDATE.length()>5)
				 fdate    = FROMDATE.substring(6)+"-"+ FROMDATE.substring(3,5)+"-"+FROMDATE.substring(0,2);				 
			 if(TODATE==null) TODATE=""; else TODATE = TODATE.trim();
			 if (TODATE.length()>5)
				 tdate    = TODATE.substring(6)+"-"+ TODATE.substring(3,5)+"-"+TODATE.substring(0,2);
			 OUTLETCODE = StrUtils.fString(request.getParameter("OUTLETCODE"));
			 TERMINALCODE = StrUtils.fString(request.getParameter("TERMINALCODE"));
			 CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			 
			 Hashtable ht = new Hashtable();
			 String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
			 DecimalFormat decimalFormat = new DecimalFormat("#.#####");
			 decimalFormat.setRoundingMode(java.math.RoundingMode.FLOOR);
			 movQryList = movHisUtil.getPOSExpenses(ht, fdate,tdate,PLANT,OUTLETCODE,TERMINALCODE,CUST_NAME);
			 
			 if (movQryList.size() > 0) {
				 
				 for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					 Map lineArr = (Map) movQryList.get(iCnt);
					 int iIndex = iCnt + 1;
					 String dono = (String) lineArr.get("ID");
					 String SUBTOTAL = (String)lineArr.get("AMOUNT");
					 

					 double subtot = Double.parseDouble(SUBTOTAL);
					 SUBTOTAL = StrUtils.addZeroes(subtot, numberOfDecimal);
					 					 
					 
					 int Indexs= 0;
					 Indexs = Indexs + 1;
					 resultJsonInt.put("Index", (Indexs));
					 resultJsonInt.put("expensesdate", StrUtils.fString((String)lineArr.get("EXPENSES_DATE")));
					 resultJsonInt.put("id", (dono));
					 resultJsonInt.put("description", StrUtils.fString((String)lineArr.get("DESCRIPTION")));
					 resultJsonInt.put("amount",SUBTOTAL);
					 jsonArray.add(resultJsonInt);
				 }
				 resultJson.put("POS", jsonArray);
				 resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				 resultJsonInt.put("ERROR_CODE", "100");
				 jsonArrayErr.add(resultJsonInt);
				 resultJson.put("errors", jsonArrayErr);
			 } else {
				 resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				 resultJsonInt.put("ERROR_CODE", "99");
				 jsonArrayErr.add(resultJsonInt);
				 jsonArray.add("");
				 resultJson.put("POS", jsonArray);
				 
				 resultJson.put("errors", jsonArrayErr);
			 }
		 } catch (Exception e) {
			 jsonArray.add("");
			 resultJson.put("POS", jsonArray);
			 resultJson.put("SEARCH_DATA", "");
			 resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			 resultJsonInt.put("ERROR_CODE", "98");
			 jsonArrayErr.add(resultJsonInt);
			 resultJson.put("ERROR", jsonArrayErr);
		 }
		 return resultJson;
	 }
	 
	 private JSONObject getPOSreturnsmryDetailsWithOrderPrice(HttpServletRequest request) {
		 JSONObject resultJson = new JSONObject();
		 JSONArray jsonArray = new JSONArray();
		 JSONArray jsonArrayErr = new JSONArray();
		 HTReportUtil movHisUtil       = new HTReportUtil();
		 ArrayList movQryList  = new ArrayList();
		 JSONObject resultJsonInt = new JSONObject();
		 
		 String fdate="",tdate="",taxby="";
		 String ITEMS="",FROMDATE="",TODATE="",ORDER="",CUSTOMER="",OUTLETCODE="",TERMINALCODE="",CUST_NAME="",SALES_PERSON="",PRD_TYPE="",PRD_BRAND="",PRD_CLS="";
		 
		 try {
			 
			 String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			 String TYPE= StrUtils.fString(request.getParameter("SRC"));
			 ITEMS    = StrUtils.fString(request.getParameter("ITEM"));
			 FROMDATE   = StrUtils.fString(request.getParameter("FDATE"));
			 TODATE = StrUtils.fString(request.getParameter("TDATE"));
			 if(FROMDATE==null) FROMDATE=""; else FROMDATE = FROMDATE.trim();
			 String curDates =DateUtils.getDate();
			 if(FROMDATE.length()<0||FROMDATE==null||FROMDATE.equalsIgnoreCase(""))FROMDATE=curDates;
			 if (FROMDATE.length()>5)
				 fdate    = FROMDATE.substring(6)+"-"+ FROMDATE.substring(3,5)+"-"+FROMDATE.substring(0,2);				 
			 if(TODATE==null) TODATE=""; else TODATE = TODATE.trim();
			 if (TODATE.length()>5)
				 tdate    = TODATE.substring(6)+"-"+ TODATE.substring(3,5)+"-"+TODATE.substring(0,2);
			 ORDER = StrUtils.fString(request.getParameter("ORDERNO"));
			 CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
//        	 OUTLETNAME = StrUtils.fString(request.getParameter("OUTLET_NAME"));
//        	 TERMINALNAME = StrUtils.fString(request.getParameter("TERMINALNAME"));
			 OUTLETCODE = StrUtils.fString(request.getParameter("OUTLETCODE"));
			 TERMINALCODE = StrUtils.fString(request.getParameter("TERMINALCODE"));
			 CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			 SALES_PERSON = StrUtils.fString(request.getParameter("SALES_MAN"));
			 ITEMS = StrUtils.fString(request.getParameter("ITEM"));
			 PRD_TYPE = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));//sub category
			 PRD_BRAND = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
			 PRD_CLS = StrUtils.fString(request.getParameter("PRD_CLS_ID"));//category
			 
			 String ENABLE_POS =new PlantMstDAO().getispos(PLANT);
			 String ISTAXREGISTRED =new PlantMstDAO().getistaxregistred(PLANT);
			 
			 Hashtable ht = new Hashtable();
			 String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
			 DecimalFormat decimalFormat = new DecimalFormat("#.#####");
			 decimalFormat.setRoundingMode(java.math.RoundingMode.FLOOR);
			 ArrayList prodGstList = new ArrayList();
			 movQryList = movHisUtil.getPOSreturnSummary(ht, fdate,tdate,PLANT,CUSTOMER,ORDER,OUTLETCODE,TERMINALCODE,CUST_NAME,SALES_PERSON,ITEMS,PRD_BRAND,PRD_CLS,PRD_TYPE);
			 
			 if (movQryList.size() > 0) {
				 
				 float subtotal=0;
				 double gst=0,total=0;
				 float gsttotal=0;
				 float grandtotal=0,gstpercentage=0,prodgstsubtotal1=0;
				 
				 gst=prodgstsubtotal1;
				 for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					 Map lineArr = (Map) movQryList.get(iCnt);
					 int iIndex = iCnt + 1;
					 String dono = (String) lineArr.get("dono");
					 String TOTALValue =  decimalFormat.format(total); 
					 String SUBTOTAL = (String)lineArr.get("subtotal");
					 
					 float totalVal="".equals(TOTALValue) ? 0.0f :  Float.parseFloat(TOTALValue);
					 float subTotalVal="".equals(SUBTOTAL) ? 0.0f :  Float.parseFloat(SUBTOTAL);
					 
					 double tot = Double.parseDouble(TOTALValue);
					 TOTALValue = StrUtils.addZeroes(tot, numberOfDecimal);
					 double subtot = Double.parseDouble(SUBTOTAL);
					 SUBTOTAL = StrUtils.addZeroes(subtot, numberOfDecimal);
					 
					 String item_discounttype = StrUtils.fString((String)lineArr.get("ORDERDISCOUNTTYPE"));
					 String item_discount = StrUtils.fString((String)lineArr.get("ORDERDISCOUNT"));
					 String ISdiscount = StrUtils.fString((String)lineArr.get("ISORDERDISCOUNTTAX"));
					 String discountANDtype = StrUtils.fString((String)lineArr.get("ORDERDISCOUNT"))+"("+StrUtils.fString((String)lineArr.get("ORDERDISCOUNTTYPE"))+")";
					 
					 //discount calculation
					 double discount =0,dDiscount=0;
					 if(item_discounttype.equalsIgnoreCase("%")){
						 dDiscount = Double.parseDouble(item_discount);
						 discount = ((Double.parseDouble(SUBTOTAL))/100)*dDiscount;
					 }else{
						 dDiscount = Double.parseDouble(item_discount);
						 discount =  dDiscount;
					 }
					 
					 //tax calculation
					 double tax =0.0;
					 int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
					 gstpercentage =  Float.parseFloat(((String) lineArr.get("outbound_gst").toString())) ;
					 FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
					 if(taxid != 0){
						 if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){
							 if(ISdiscount.equalsIgnoreCase("1"))
								 tax = ((Double.parseDouble(SUBTOTAL)-discount)*gstpercentage)/100;
							 else 
								 tax = (Double.parseDouble(SUBTOTAL)*gstpercentage)/100;
						 }else 
							 gstpercentage=Float.parseFloat("0.000");
					 }else 
						 gstpercentage=Float.parseFloat("0.000");
					 
					 //total amount
					 double finaltotal = ((Double.parseDouble(SUBTOTAL))-discount+tax);
					 if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0"))
						 	finaltotal = ((Double.parseDouble(SUBTOTAL))-discount);
					 
					 
					 int Indexs= 0;
					 Indexs = Indexs + 1;
					 resultJsonInt.put("Index", (Indexs));
					 resultJsonInt.put("CollectionDate", StrUtils.fString((String)lineArr.get("CollectionDate")));
					 resultJsonInt.put("prno", StrUtils.fString((String)lineArr.get("PRNO")));
					 resultJsonInt.put("dono", (dono));
					 resultJsonInt.put("custname", StrUtils.fString((String)lineArr.get("custname")));
					 resultJsonInt.put("avgcost",SUBTOTAL);
					 resultJsonInt.put("issAvgcostwTax",discount);
					 resultJsonInt.put("tax", tax);
					 resultJsonInt.put("issueAvgcost",finaltotal);
					 jsonArray.add(resultJsonInt);
				 }
				 resultJson.put("POS", jsonArray);
				 resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				 resultJsonInt.put("ERROR_CODE", "100");
				 jsonArrayErr.add(resultJsonInt);
				 resultJson.put("errors", jsonArrayErr);
			 } else {
				 resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				 resultJsonInt.put("ERROR_CODE", "99");
				 jsonArrayErr.add(resultJsonInt);
				 jsonArray.add("");
				 resultJson.put("POS", jsonArray);
				 
				 resultJson.put("errors", jsonArrayErr);
			 }
		 } catch (Exception e) {
			 jsonArray.add("");
			 resultJson.put("POS", jsonArray);
			 resultJson.put("SEARCH_DATA", "");
			 resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			 resultJsonInt.put("ERROR_CODE", "98");
			 jsonArrayErr.add(resultJsonInt);
			 resultJson.put("ERROR", jsonArrayErr);
		 }
		 return resultJson;
	 }
	 
//	 resvi starts
	 
	 private JSONObject getConsignmentsmryDetailsWithIssuePrice(HttpServletRequest request) {
		  JSONObject resultJson = new JSONObject();
	         JSONArray jsonArray = new JSONArray();
	         JSONArray jsonArrayErr = new JSONArray();
	         HTReportUtil movHisUtil       = new HTReportUtil();
	         //DateUtils _dateUtils = new DateUtils();
	         ArrayList movQryList  = new ArrayList();
	         
//	         DecimalFormat decformat = new DecimalFormat("#,##0.00");
	        
	         //StrUtils strUtils = new StrUtils();
	         String fdate="",tdate="",taxby="";

	         try {
	         
	            String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	            String ITEMNO    = StrUtils.fString(request.getParameter("ITEM"));
	            String  PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
	            String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
	            String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
	            String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
	            String  CUSTNAME = StrUtils.fString(request.getParameter("CNAME"));
	            String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
	            String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
	            String  PICKSTATUS = StrUtils.fString(request.getParameter("PICKSTATUS"));
	            String  RECEIVESTATUS = StrUtils.fString(request.getParameter("RECEIVESTATUS"));
	            String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
	            String  GINO = StrUtils.fString(request.getParameter("GINO"));
	            String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
	            String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
	            String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
	            String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
	            String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
	            String SORT = StrUtils.fString(request.getParameter("SORT"));
	            String EMPNO = StrUtils.fString(request.getParameter("EMPNO"));
	            String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMERTYPE"));
	            String FROMWAREHOUSE = StrUtils.fString(request.getParameter("FROMWAREHOUSE"));
	            String TOWAREHOUSE = StrUtils.fString(request.getParameter("TOWAREHOUSE"));
//	            String UOM = StrUtils.fString(request.getParameter("UOM"));
	            taxby=_PlantMstDAO.getTaxBy(PLANT);
	            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	            String curDate =DateUtils.getDate();
	            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	            if (FROM_DATE.length()>5)
	            	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
	            if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	            if (TO_DATE.length()>5)
	            tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

	               
	    
	            Hashtable ht = new Hashtable();
	            if(StrUtils.fString(JOBNO).length() > 0)        ht.put("JOBNUM",JOBNO);
	            if(StrUtils.fString(ITEMNO).length() > 0)       ht.put("ITEM",ITEMNO);
	            if(StrUtils.fString(ORDERNO).length() > 0)      ht.put("TONO",ORDERNO);
	            if(StrUtils.fString(GINO).length() > 0)    ht.put("GINO",GINO);
	            //if(StrUtils.fString(CUSTNAME).length() > 0)     ht.put("A.CUSTNAME",strUtils.InsertQuotes(CUSTNAME));
	            if(StrUtils.fString(ORDERTYPE).length() > 0)    ht.put("ORDERTYPE",ORDERTYPE);
	            if(StrUtils.fString(RECEIVESTATUS).length() > 0)  ht.put("STATUS",RECEIVESTATUS);
	            if(StrUtils.fString(PICKSTATUS).length() > 0)   ht.put("PickStaus",PICKSTATUS);
	            if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("ITEMTYPE",PRD_TYPE_ID);
		        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
		        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("PRD_CLS_ID",PRD_CLS_ID);
		        if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("PRD_DEPT_ID",PRD_DEPT_ID);
		        if(StrUtils.fString(statusID).length() > 0) ht.put("STATUS_ID",statusID);
		        if(StrUtils.fString(EMPNO).length() > 0) ht.put("EMPNO",EMPNO);
		        if(StrUtils.fString(CUSTOMERTYPE).length() > 0) ht.put("CUSTTYPE",CUSTOMERTYPE);
		        if(StrUtils.fString(FROMWAREHOUSE).length() > 0) ht.put("FROMWAREHOUSE",FROMWAREHOUSE);
		        if(StrUtils.fString(TOWAREHOUSE).length() > 0) ht.put("TOWAREHOUSE",TOWAREHOUSE);
		        if(taxby.equalsIgnoreCase("BYORDER"))
				{
	          	  movQryList = movHisUtil.getConsignmentDOInvoiceSummary(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTNAME,SORT);
				}
		        else
		        {
		        	 movQryList = movHisUtil.getConsignmentDOInvoiceSummaryByProductGst(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTNAME,SORT);
		        }
	            //getCustomerDOInvoiceIssueSummaryByProductGst
	             if (movQryList.size() > 0) {
	             int Index = 0;
//	             int iIndex = 0,irow = 0;
	              String customerstatusid ="",customerstatusdesc="",customertypeid ="",customertypedesc="";
//	             double sumprdQty = 0;String lastProduct="";
	             
	             double totalIssPrice=0,totaltax=0,totIssPriceWTax=0,priceGrandTot=0,taxGrandTot=0,priceWTaxGrandTot=0;//,TotalIssPrice=0;//issPriceSubTot=0,OrdPriceGrandTot=0,taxGrandTotal=0,priceWTaxGrandTotal=0;
//	             double issPriceGrandTot=0;
	                 for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	                       
//	                         String result="",custcode="";
	                         Map lineArr = (Map) movQryList.get(iCnt);
//	                         if(SORT.equalsIgnoreCase("PRODUCT"))
//	                         {
//	                        	 custcode=(String)lineArr.get("item");
//	                         }
//	                         else
//	                         {
//	                        	 if(lineArr.get("custcode") != null)
//	                        		 custcode=(String)lineArr.get("custcode");
//	                        	 else
//	                        		 custcode="";
//	                         }
	                         String tono = (String)lineArr.get("tono");
	                         Float gstpercentage = 0.0f;
	                         if(lineArr.get("Tax")!=null)
	                        	 gstpercentage =  Float.parseFloat(((String) lineArr.get("Tax").toString())) ;
	             			            			
	                         
	           	          
	           	             //indiviual subtotal price details
	           	             double issuePrice = Double.parseDouble((String)lineArr.get("issPrice"));
	           	            // issuePrice = StrUtils.RoundDB(issuePrice,2);
	           	          //    double tax = (issuePrice*gstpercentage)/100;
	              	          Double tax = 0.0d;
	              	       if(lineArr.get("taxval")!=null) {
//	              	           tax = Double.parseDouble((String)lineArr.get("taxval"));
	              	           
//	              	         double tax =0.0;
	              	           int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
	              	           FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
	              	           if(taxid != 0){
	              	        	   if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){	              	        		   
	              	        		   tax = (issuePrice*gstpercentage)/100;
	              	        	   } else 
	              	        		 gstpercentage=Float.parseFloat("0.000");
	              	        	   
	              	           }else 
	              	        		 gstpercentage=Float.parseFloat("0.000");
	              	           
	              	       }
	              	             double issPricewTax = issuePrice+tax;
	                         
	                         //total price details
	                         totalIssPrice=totalIssPrice+Double.parseDouble(((String)lineArr.get("issPrice").toString()));
	                         totaltax  =totaltax + tax;
	                         totIssPriceWTax = totIssPriceWTax+issPricewTax;
	                         
	                         //Grand total details
	                         priceGrandTot = priceGrandTot+Double.parseDouble((String)lineArr.get("issPrice"));;
	                         taxGrandTot = taxGrandTot+tax;
	                         priceWTaxGrandTot = priceWTaxGrandTot+issPricewTax;
	                         
	                         customerstatusid = customerBeanDAO.getCustomerStatusId(PLANT,(String)lineArr.get("custname"));
	                         if(customerstatusid == null || customerstatusid.equals(""))
	             			   {
	             			   customerstatusdesc="";
	             			   }
	             			   else
	             			   {
	             			    customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(PLANT,customerstatusid);
	             			   }
	                         
	                         customertypeid = customerBeanDAO.getCustomerTypeId(PLANT,(String)lineArr.get("custname"));
	              			 if(customertypeid == null || customertypeid.equals(""))
	              			 {
	              				customertypedesc="";
	              			 }
	              			 else
	              			 {
	              				customertypedesc = customerBeanDAO.getCustomerTypeDesc(PLANT,customertypeid);
	              			 }
	              			 
	              			String qtyOrValue =(String)lineArr.get("qtyor");
	              			String qtyInvValue =(String)lineArr.get("qtyac");
	               			String qtyPickValue =(String)lineArr.get("qtypick");
	               			String qtyValue =(String)lineArr.get("qty");
	               			String unitprice = (String)lineArr.get("unitprice");
	               			String gstpercentageValue = String.valueOf(gstpercentage);
	               			String issuePriceValue = String.valueOf(issuePrice);
	               			String taxValue = String.valueOf(tax);
	               			String issPricewTaxValue = String.valueOf(issPricewTax);
	               			
	                        float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
	                        float qtyInvVal ="".equals(qtyInvValue) ? 0.0f :  Float.parseFloat(qtyInvValue);
	                        float qtyPickVal ="".equals(qtyPickValue) ? 0.0f :  Float.parseFloat(qtyPickValue);
	                        float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
	                        float unitPriceVal="".equals(unitprice) ? 0.0f :  Float.parseFloat(unitprice);
	                        float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);
	                        float issuePriceVal ="".equals(issuePriceValue) ? 0.0f :  Float.parseFloat(issuePriceValue);
	                        float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);
	                        float issPricewTaxVal ="".equals(issPricewTaxValue) ? 0.0f :  Float.parseFloat(issPricewTaxValue);
	                        
	                        if(qtyOrVal==0f){
	                        	qtyOrValue="0.000";
	                        }else{
	                        	qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }if(qtyInvVal==0f){
	                        	qtyInvValue="0.000";
	                        }else{
	                        	qtyInvValue=qtyInvValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }if(qtyPickVal==0f){
	                        	qtyPickValue="0.000";
	                        }else{
	                        	qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }if(qtyVal==0f){
	                        	qtyValue="0.000";
	                        }else{
	                        	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }if(unitPriceVal==0f){
	                        	unitprice="0.00000";
	                        }else{
	                        	unitprice=unitprice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }if(gstpercentageVal==0f) {
	                        	gstpercentageValue="0.000";
	                        }else {
	                        	gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }if(issuePriceVal==0f) {
	                        	issuePriceValue="0.00000";
	                        }else {
	                        	issuePriceValue=issuePriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }if(taxVal==0f) {
	                        	taxValue="0.000";
	                        }else {
	                        	taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }if(issPricewTaxVal==0f) {
	                        	issPricewTaxValue="0.00000";
	                        }else {
	                        	issPricewTaxValue=issPricewTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }
	                              
	                        
//	                         String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                         JSONObject resultJsonInt = new JSONObject();
	                        
	                      //  if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(custcode))){
	                        	Index = Index + 1;
	                        	
	                      	  	resultJsonInt.put("Index", (Index));
	                            resultJsonInt.put("tono", (tono));
	                            resultJsonInt.put("GINO", StrUtils.fString((String)lineArr.get("GINO")));
	                            resultJsonInt.put("orderType", StrUtils.fString((String)lineArr.get("orderType")));
	                            resultJsonInt.put("jobnum", StrUtils.fString((String)lineArr.get("jobnum")));
	                            resultJsonInt.put("fromwarehouse", StrUtils.fString((String)lineArr.get("fromwarehouse")));
	                            resultJsonInt.put("towarehouse", StrUtils.fString((String)lineArr.get("towarehouse")));
	                            resultJsonInt.put("jobnum", StrUtils.fString((String)lineArr.get("jobnum")));
	                            resultJsonInt.put("custname", StrUtils.fString((String)lineArr.get("custname")));
	                            resultJsonInt.put("customertypedesc", StrUtils.fString(customertypedesc));
	                            resultJsonInt.put("customerstatusdesc", StrUtils.fString(customerstatusdesc));
	                            resultJsonInt.put("remarks", StrUtils.fString((String)lineArr.get("remarks")));
	                            resultJsonInt.put("remarks2", StrUtils.fString((String)lineArr.get("remarks2")));
	                            resultJsonInt.put("item", StrUtils.fString((String)lineArr.get("item")));
	                            resultJsonInt.put("itemdesc", StrUtils.fString((String)lineArr.get("itemdesc")));
	                            resultJsonInt.put("issuedate", StrUtils.fString((String)lineArr.get("issuedate")));
	                            resultJsonInt.put("UOM", StrUtils.fString((String)lineArr.get("UOM")));
	                            resultJsonInt.put("qtyor", qtyOrValue);
	                            resultJsonInt.put("qtypick",qtyPickValue );
	                            resultJsonInt.put("qty",qtyValue);
	                            resultJsonInt.put("qtyac",qtyInvValue);
	                            resultJsonInt.put("empname", StrUtils.fString((String)lineArr.get("empname")));
	                            resultJsonInt.put("avgcost", unitprice);
	                            resultJsonInt.put("gstpercentage", StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) gstpercentageValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
//	                            resultJsonInt.put("gstpercentage",gstpercentageValue);
	                            resultJsonInt.put("issueAvgcost", issuePriceValue);
	                            resultJsonInt.put("tax", taxValue);
	                            resultJsonInt.put("issAvgcostwTax", issPricewTaxValue);
	                            resultJsonInt.put("status_id", StrUtils.fString((String)lineArr.get("status_id")));
	                            resultJsonInt.put("users", StrUtils.fString((String)lineArr.get("users")));
	                           
	                        	   jsonArray.add(resultJsonInt);
	                        	
	                        	/*result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
	                         +"<td align=center>" +Index+ "</td>"
	                        // + "<td><a href=/track/deleveryorderservlet?DONO=" +dono+ "&Submit=View&RFLAG=4>" + dono + "</a></td>"
	                         + "<td>"+dono +"</td>"
	                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("orderType")) + "</td>"
	                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("jobnum")) + "</td>"
	                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("custname")) + "</td>"
	                          + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customertypedesc) + "</td>"
	                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customerstatusdesc) + "</td>"
	                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks")) + "</td>"
	                          + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks2")) + "</td>"
	                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("item")) + "</td>"
	                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("itemdesc")) + "</td>"
	                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("issuedate")) + "</td>"
	                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qtyor")) + "</td>"
	                         + "<td  align = right>&nbsp;&nbsp;&nbsp;"+ StrUtils.formatNum((String)lineArr.get("qtypick")) + "</td>"
	                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qty")) + "</td>"
	                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("empname")) + "</td>"
	                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(unitprice) + "</td>"
	                         + "<td align = right>&nbsp;&nbsp;&nbsp;" +  gstpercentage + "</td>"
	                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(issuePrice) + "</td>"
	                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(tax) + "</td>"
	                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(issPricewTax) + "</td>"
	                         + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("status_id")) + "</td>"
	                         + "<tr>";
	                         if(iIndex+1 == movQryList.size()){
	                                 irow=irow+1;
	                                 bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";  

	                               result +=  "<TR bgcolor ="+bgcolor+" >"
	                                  +"<TD colspan=17></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalIssPrice,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totIssPriceWTax,2))+"</b></TD><TD></TD> </TR>";
	                                    
	                          } }else{
	                           
	                        	  totalIssPrice=totalIssPrice-Double.parseDouble(((String)lineArr.get("issPrice").toString()));
	                        	  totalIssPrice = StrUtils.RoundDB(totalIssPrice,2);
	                        	  
	                        	  totaltax=totaltax-tax;
	                        	  totaltax = StrUtils.RoundDB(totaltax,2);
	                        	  
	                        	  totIssPriceWTax=totIssPriceWTax-issPricewTax;
	                        	  totIssPriceWTax = StrUtils.RoundDB(totIssPriceWTax,2);
	                        	   result +=  "<TR bgcolor ="+bgcolor+" >"
	                                       +"<TD colspan=17></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalIssPrice,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totIssPriceWTax,2))+"</b></TD><TD></TD> </TR>";
	                             irow=irow+1;
	                             Index = 0;
	                             Index=Index+1;
	                             bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
	                             result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
	                             +"<td align=center>" +Index+ "</td>"
	                             //+ "<td><a href=/track/deleveryorderservlet?DONO=" +dono+ "&Submit=View&RFLAG=4>" + dono + "</a></td>"
	                             + "<td>"+dono +"</td>"
	                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("orderType")) + "</td>"
	                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("jobnum")) + "</td>"
	                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("custname")) + "</td>"
	                              + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customertypedesc) + "</td>"
	                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customerstatusdesc) + "</td>"
	                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks")) + "</td>"
	                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks2")) + "</td>"
	                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("item")) + "</td>"
	                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("itemdesc")) + "</td>"
	                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("issuedate")) + "</td>"
	                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qtyor")) + "</td>"
	                             + "<td  align = right>&nbsp;&nbsp;&nbsp;"+ StrUtils.formatNum((String)lineArr.get("qtypick")) + "</td>"
	                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qty")) + "</td>"
	                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("empname")) + "</td>"
	                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(unitprice) + "</td>"
	                             + "<td align = right>&nbsp;&nbsp;&nbsp;" +  gstpercentage + "</td>"
	                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(issuePrice) + "</td>"
	                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(tax) + "</td>"
	                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(issPricewTax) + "</td>"
	                             + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("status_id")) + "</td>"
	                             + "<tr>";
	                       
	                         
	                             totalIssPrice=Double.parseDouble(((String)lineArr.get("issPrice").toString()));
	                             totalIssPrice = StrUtils.RoundDB(totalIssPrice,2);
	                       	  
		                       	  totaltax=tax;
		                       	  totaltax = StrUtils.RoundDB(totaltax,2);
	                       	  
		                       	totIssPriceWTax=issPricewTax;
		                       	totIssPriceWTax = StrUtils.RoundDB(totIssPriceWTax,2);
	                       	  
	                          	
	                                  if(iIndex+1 == movQryList.size()){ 
	                                      irow=irow+1;
	                                      bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
	                                      result +=  "<TR bgcolor ="+bgcolor+" >"
	                                              +"<TD colspan=17></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalIssPrice,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totIssPriceWTax,2))+"</b></TD><TD></TD> </TR>";
	                                        
	                         }
	                         }
	                              irow=irow+1;
	                              iIndex=iIndex+1;
	                              lastProduct = custcode;
	                              if(movQryList.size()==iCnt+1){
	                             	 irow=irow+1;
	                                  bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
	                                  result +=  "<TR bgcolor ="+bgcolor+" >"
	                                          +"<TD colspan=17></TD> <TD align= \"right\"><b>Grand Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(priceGrandTot,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(taxGrandTot,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(priceWTaxGrandTot,2))+"</b></TD><TD></TD> </TR>";

	                              }
	                              
	                             resultJsonInt.put("OUTBOUNDDETAILS", result);
	                          
	                             jsonArray.add(resultJsonInt);*/

	                 }
	                
	                     resultJson.put("items", jsonArray);
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
	                     resultJson.put("items", jsonArray);
	                     resultJson.put("errors", jsonArrayErr);
	             }
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
	         return resultJson;
	 }
	 
//	resvi ends
	 
	 private JSONObject getOutboundsmryDetailsWithIssuePrice(HttpServletRequest request) {
         JSONObject resultJson = new JSONObject();
         JSONArray jsonArray = new JSONArray();
         JSONArray jsonArrayErr = new JSONArray();
         HTReportUtil movHisUtil       = new HTReportUtil();
         //DateUtils _dateUtils = new DateUtils();
         ArrayList movQryList  = new ArrayList();
         
//         DecimalFormat decformat = new DecimalFormat("#,##0.00");
        
         //StrUtils strUtils = new StrUtils();
         String fdate="",tdate="",taxby="";

         try {
         
            String PLANT= StrUtils.fString(request.getParameter("PLANT"));
            String ITEMNO    = StrUtils.fString(request.getParameter("ITEM"));
            String  PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
            String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
            String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
            String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
            String  CUSTNAME = StrUtils.fString(request.getParameter("CNAME"));
            String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
            String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
            String  PICKSTATUS = StrUtils.fString(request.getParameter("PICKSTATUS"));
            String  ISSUESTATUS = StrUtils.fString(request.getParameter("ISSUESTATUS"));
            String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
            String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
            String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
            String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
            String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
            String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
            String SORT = StrUtils.fString(request.getParameter("SORT"));
            String EMPNO = StrUtils.fString(request.getParameter("EMPNO"));
            String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMERTYPE"));
            String INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
//            String UOM = StrUtils.fString(request.getParameter("UOM"));
            String LOC = StrUtils.fString(request.getParameter("LOC"));
            String LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
            String LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
            String LOC_TYPE_ID3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
            String POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
            taxby=_PlantMstDAO.getTaxBy(PLANT);
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
            String curDate =DateUtils.getDate();
            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

            if (FROM_DATE.length()>5)
            	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
            if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
            if (TO_DATE.length()>5)
            tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

               
    
            Hashtable ht = new Hashtable();
            if(StrUtils.fString(JOBNO).length() > 0)        ht.put("JOBNUM",JOBNO);
            if(StrUtils.fString(ITEMNO).length() > 0)       ht.put("ITEM",ITEMNO);
            if(StrUtils.fString(ORDERNO).length() > 0)      ht.put("DONO",ORDERNO);
            //if(StrUtils.fString(CUSTNAME).length() > 0)     ht.put("A.CUSTNAME",strUtils.InsertQuotes(CUSTNAME));
            if(StrUtils.fString(ORDERTYPE).length() > 0)    ht.put("ORDERTYPE",ORDERTYPE);
            if(StrUtils.fString(ISSUESTATUS).length() > 0)  ht.put("STATUS",ISSUESTATUS);
            if(StrUtils.fString(PICKSTATUS).length() > 0)   ht.put("PickStaus",PICKSTATUS);
            if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("ITEMTYPE",PRD_TYPE_ID);
	        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
	        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("PRD_CLS_ID",PRD_CLS_ID);
	        if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("PRD_DEPT_ID",PRD_DEPT_ID);
	        if(StrUtils.fString(statusID).length() > 0) ht.put("STATUS_ID",statusID);
	        if(StrUtils.fString(EMPNO).length() > 0) ht.put("EMPNO",EMPNO);
	        if(StrUtils.fString(CUSTOMERTYPE).length() > 0) ht.put("CUSTTYPE",CUSTOMERTYPE);
	        if(StrUtils.fString(INVOICENO).length() > 0) ht.put("INVOICENO",INVOICENO);
	        if(StrUtils.fString(LOC).length() > 0) ht.put("LOC",LOC);
	        if(StrUtils.fString(LOC_TYPE_ID).length() > 0) ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
	        if(StrUtils.fString(LOC_TYPE_ID2).length() > 0) ht.put("LOC_TYPE_ID2",LOC_TYPE_ID2);
	        if(StrUtils.fString(LOC_TYPE_ID3).length() > 0) ht.put("LOC_TYPE_ID3",LOC_TYPE_ID3);
	        if(StrUtils.fString(SORT).length() > 0) ht.put("SORT",SORT);
	        if(taxby.equalsIgnoreCase("BYORDER"))
			{
          	  	movQryList = movHisUtil.getCustomerDOInvoiceIssueSummary(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTNAME,SORT,POSSEARCH);
			}
	        else
	        {
	        	 movQryList = movHisUtil.getCustomerDOInvoiceIssueSummaryByProductGst(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTNAME,SORT,POSSEARCH);
	        }
            //getCustomerDOInvoiceIssueSummaryByProductGst
             if (movQryList.size() > 0) {
             int Index = 0;
//             int iIndex = 0,irow = 0;
              String customerstatusid ="",customerstatusdesc="",customertypeid ="",customertypedesc="";
//             double sumprdQty = 0;String lastProduct="";
             
             double totalIssPrice=0,totaltax=0,totIssPriceWTax=0,priceGrandTot=0,taxGrandTot=0,priceWTaxGrandTot=0;//,TotalIssPrice=0;//issPriceSubTot=0,OrdPriceGrandTot=0,taxGrandTotal=0,priceWTaxGrandTotal=0;
//             double issPriceGrandTot=0;
                 for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                       
//                         String result="",custcode="";
                         Map lineArr = (Map) movQryList.get(iCnt);
//                         if(SORT.equalsIgnoreCase("PRODUCT"))
//                         {
//                        	 custcode=(String)lineArr.get("item");
//                         }
//                         else
//                         {
//                        	 if(lineArr.get("custcode") != null)
//                        		 custcode=(String)lineArr.get("custcode");
//                        	 else
//                        		 custcode="";
//                         }
                         String dono = (String)lineArr.get("dono");
                         Float gstpercentage = 0.0f;
                         if(lineArr.get("Tax")!=null)
                        	 gstpercentage =  Float.parseFloat(((String) lineArr.get("Tax").toString())) ;
             			            			
                         
           	          
           	             //indiviual subtotal price details
           	             double issuePrice = Double.parseDouble((String)lineArr.get("issPrice"));
           	            // issuePrice = StrUtils.RoundDB(issuePrice,2);
           	          //    double tax = (issuePrice*gstpercentage)/100;
//              	          Double tax = 0.0d;
           	             double tax =0.0;
              	       if(lineArr.get("taxval")!=null) {
//              	           tax = Double.parseDouble((String)lineArr.get("taxval"));
              	       
          	           int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
          	           FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
          	           if(taxid != 0){
          	        	   if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){	              	        		   
          	        		   tax = (issuePrice*gstpercentage)/100;
          	        	   } else 
          	        		 gstpercentage=Float.parseFloat("0.000");
          	        	   
          	           }
              	       }else 
          	        		 gstpercentage=Float.parseFloat("0.000");
              	       
              	             double issPricewTax = issuePrice+tax;
                         
                         //total price details
                         totalIssPrice=totalIssPrice+Double.parseDouble(((String)lineArr.get("issPrice").toString()));
                         totaltax  =totaltax + tax;
                         totIssPriceWTax = totIssPriceWTax+issPricewTax;
                         
                         //Grand total details
                         priceGrandTot = priceGrandTot+Double.parseDouble((String)lineArr.get("issPrice"));;
                         taxGrandTot = taxGrandTot+tax;
                         priceWTaxGrandTot = priceWTaxGrandTot+issPricewTax;
                         
                         customerstatusid = customerBeanDAO.getCustomerStatusId(PLANT,(String)lineArr.get("custname"));
                         if(customerstatusid == null || customerstatusid.equals(""))
             			   {
             			   customerstatusdesc="";
             			   }
             			   else
             			   {
             			    customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(PLANT,customerstatusid);
             			   }
                         
                         customertypeid = customerBeanDAO.getCustomerTypeId(PLANT,(String)lineArr.get("custname"));
              			 if(customertypeid == null || customertypeid.equals(""))
              			 {
              				customertypedesc="";
              			 }
              			 else
              			 {
              				customertypedesc = customerBeanDAO.getCustomerTypeDesc(PLANT,customertypeid);
              			 }
              			 
              			String qtyOrValue =(String)lineArr.get("qtyor");
               			String qtyPickValue =(String)lineArr.get("qtypick");
               			String qtyValue =(String)lineArr.get("qty");
               			String unitprice = (String)lineArr.get("unitprice");
               			String gstpercentageValue = String.valueOf(gstpercentage);
               			String issuePriceValue = String.valueOf(issuePrice);
               			String taxValue = String.valueOf(tax);
               			String issPricewTaxValue = String.valueOf(issPricewTax);
               			
               			String deliverystatus = (String)lineArr.get("DELIVERY_STATUS");
                		String intransit = (String)lineArr.get("INTRANSIT_STATUS");
                		String lnstat = (String)lineArr.get("LNSTAT");
                		 
                		if(ISSUESTATUS.equalsIgnoreCase("O")) {
                		if(lnstat.equalsIgnoreCase("O")) 
                			lnstat="PARTIALLY ISSUED";
                		}
                		if(ISSUESTATUS.equalsIgnoreCase("C")) {
                		if(lnstat.equalsIgnoreCase("C"))
                			lnstat="SHIPPED";
                		}
                		
                		if(ISSUESTATUS.equalsIgnoreCase("")) {
                		if(lnstat.equalsIgnoreCase("O")) 
                			lnstat="PARTIALLY ISSUED";
                		if(lnstat.equalsIgnoreCase("C"))
                			lnstat="SHIPPED";
                		if(!intransit.equalsIgnoreCase(""))
                			lnstat = intransit; 
             			if(!deliverystatus.equalsIgnoreCase(""))
             				lnstat = deliverystatus; 
                		}
             			
                		if(ISSUESTATUS.equalsIgnoreCase("DELIVERED")) {
                			lnstat = deliverystatus; 
                		}else if(ISSUESTATUS.equalsIgnoreCase("INTRANSIT")) {
                			lnstat = intransit; 
                		}
               			
                        float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
                        float qtyPickVal ="".equals(qtyPickValue) ? 0.0f :  Float.parseFloat(qtyPickValue);
                        float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
                        float unitPriceVal="".equals(unitprice) ? 0.0f :  Float.parseFloat(unitprice);
                        float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);
                        float issuePriceVal ="".equals(issuePriceValue) ? 0.0f :  Float.parseFloat(issuePriceValue);
                        float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);
                        float issPricewTaxVal ="".equals(issPricewTaxValue) ? 0.0f :  Float.parseFloat(issPricewTaxValue);
                        
                        if(qtyOrVal==0f){
                        	qtyOrValue="0.000";
                        }else{
                        	qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(qtyPickVal==0f){
                        	qtyPickValue="0.000";
                        }else{
                        	qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(qtyVal==0f){
                        	qtyValue="0.000";
                        }else{
                        	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(unitPriceVal==0f){
                        	unitprice="0.00000";
                        }else{
                        	unitprice=unitprice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(gstpercentageVal==0f) {
                        	gstpercentageValue="0.000";
                        }else {
                        	gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(issuePriceVal==0f) {
                        	issuePriceValue="0.00000";
                        }else {
                        	issuePriceValue=issuePriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(taxVal==0f) {
                        	taxValue="0.000";
                        }else {
                        	taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(issPricewTaxVal==0f) {
                        	issPricewTaxValue="0.00000";
                        }else {
                        	issPricewTaxValue=issPricewTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                              
                        
//                         String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                         JSONObject resultJsonInt = new JSONObject();
                        
                      //  if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(custcode))){
                        	Index = Index + 1;
                        	
                      	  	resultJsonInt.put("Index", (Index));
                            resultJsonInt.put("dono", (dono));
                            resultJsonInt.put("orderType", StrUtils.fString((String)lineArr.get("orderType")));
                            resultJsonInt.put("jobnum", StrUtils.fString((String)lineArr.get("jobnum")));
                            resultJsonInt.put("custname", StrUtils.fString((String)lineArr.get("custname")));
                            resultJsonInt.put("customertypedesc", StrUtils.fString(customertypedesc));
                            resultJsonInt.put("customerstatusdesc", StrUtils.fString(customerstatusdesc));
                            resultJsonInt.put("remarks", StrUtils.fString((String)lineArr.get("remarks")));
                            resultJsonInt.put("remarks2", StrUtils.fString((String)lineArr.get("remarks2")));
                            resultJsonInt.put("item", StrUtils.fString((String)lineArr.get("item")));
                            resultJsonInt.put("itemdesc", StrUtils.fString((String)lineArr.get("itemdesc")));
                            resultJsonInt.put("issuedate", StrUtils.fString((String)lineArr.get("issuedate")));
                            resultJsonInt.put("INVOICENO", StrUtils.fString((String)lineArr.get("INVOICENO")));
                            resultJsonInt.put("UOM", StrUtils.fString((String)lineArr.get("UOM")));
                            resultJsonInt.put("LOC", StrUtils.fString((String)lineArr.get("LOC")));
                            resultJsonInt.put("LOC_TYPE_ID", StrUtils.fString((String)lineArr.get("LOC_TYPE_ID")));
                            resultJsonInt.put("LOC_TYPE_ID2", StrUtils.fString((String)lineArr.get("LOC_TYPE_ID2")));
                            resultJsonInt.put("LOC_TYPE_ID3", StrUtils.fString((String)lineArr.get("LOC_TYPE_ID3")));
//                            resultJsonInt.put("qtyor", qtyOrValue);
                            resultJsonInt.put("qtyor", StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) qtyOrValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
//                            resultJsonInt.put("qtypick",qtyPickValue );
                            resultJsonInt.put("qtypick", StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) qtyPickValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
//                            resultJsonInt.put("qty",qtyValue);
                            resultJsonInt.put("qty", StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) qtyValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
                            resultJsonInt.put("empname", StrUtils.fString((String)lineArr.get("empname")));
                            resultJsonInt.put("avgcost", unitprice);
//                            resultJsonInt.put("gstpercentage",gstpercentageValue);
                            resultJsonInt.put("gstpercentage", StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) gstpercentageValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
                            resultJsonInt.put("issueAvgcost", issuePriceValue);
                            resultJsonInt.put("tax", taxValue);
                            resultJsonInt.put("issAvgcostwTax", issPricewTaxValue);
                            resultJsonInt.put("status_id", StrUtils.fString((String)lineArr.get("status_id")));
                            resultJsonInt.put("users", StrUtils.fString((String)lineArr.get("users")));
                            resultJsonInt.put("DELIVERY_STATUS", lnstat.toUpperCase());
                           
                        	   jsonArray.add(resultJsonInt);
                        	
                        	/*result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                         +"<td align=center>" +Index+ "</td>"
                        // + "<td><a href=/track/deleveryorderservlet?DONO=" +dono+ "&Submit=View&RFLAG=4>" + dono + "</a></td>"
                         + "<td>"+dono +"</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("orderType")) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("jobnum")) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("custname")) + "</td>"
                          + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customertypedesc) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customerstatusdesc) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks")) + "</td>"
                          + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks2")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("item")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("itemdesc")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("issuedate")) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qtyor")) + "</td>"
                         + "<td  align = right>&nbsp;&nbsp;&nbsp;"+ StrUtils.formatNum((String)lineArr.get("qtypick")) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qty")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("empname")) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(unitprice) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" +  gstpercentage + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(issuePrice) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(tax) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(issPricewTax) + "</td>"
                         + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("status_id")) + "</td>"
                         + "<tr>";
                         if(iIndex+1 == movQryList.size()){
                                 irow=irow+1;
                                 bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";  

                               result +=  "<TR bgcolor ="+bgcolor+" >"
                                  +"<TD colspan=17></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalIssPrice,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totIssPriceWTax,2))+"</b></TD><TD></TD> </TR>";
                                    
                          } }else{
                           
                        	  totalIssPrice=totalIssPrice-Double.parseDouble(((String)lineArr.get("issPrice").toString()));
                        	  totalIssPrice = StrUtils.RoundDB(totalIssPrice,2);
                        	  
                        	  totaltax=totaltax-tax;
                        	  totaltax = StrUtils.RoundDB(totaltax,2);
                        	  
                        	  totIssPriceWTax=totIssPriceWTax-issPricewTax;
                        	  totIssPriceWTax = StrUtils.RoundDB(totIssPriceWTax,2);
                        	   result +=  "<TR bgcolor ="+bgcolor+" >"
                                       +"<TD colspan=17></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalIssPrice,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totIssPriceWTax,2))+"</b></TD><TD></TD> </TR>";
                             irow=irow+1;
                             Index = 0;
                             Index=Index+1;
                             bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                             result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                             +"<td align=center>" +Index+ "</td>"
                             //+ "<td><a href=/track/deleveryorderservlet?DONO=" +dono+ "&Submit=View&RFLAG=4>" + dono + "</a></td>"
                             + "<td>"+dono +"</td>"
                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("orderType")) + "</td>"
                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("jobnum")) + "</td>"
                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("custname")) + "</td>"
                              + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customertypedesc) + "</td>"
                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customerstatusdesc) + "</td>"
                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks")) + "</td>"
                             + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks2")) + "</td>"
                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("item")) + "</td>"
                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("itemdesc")) + "</td>"
                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("issuedate")) + "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qtyor")) + "</td>"
                             + "<td  align = right>&nbsp;&nbsp;&nbsp;"+ StrUtils.formatNum((String)lineArr.get("qtypick")) + "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qty")) + "</td>"
                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("empname")) + "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(unitprice) + "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" +  gstpercentage + "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(issuePrice) + "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(tax) + "</td>"
                             + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(issPricewTax) + "</td>"
                             + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("status_id")) + "</td>"
                             + "<tr>";
                       
                         
                             totalIssPrice=Double.parseDouble(((String)lineArr.get("issPrice").toString()));
                             totalIssPrice = StrUtils.RoundDB(totalIssPrice,2);
                       	  
	                       	  totaltax=tax;
	                       	  totaltax = StrUtils.RoundDB(totaltax,2);
                       	  
	                       	totIssPriceWTax=issPricewTax;
	                       	totIssPriceWTax = StrUtils.RoundDB(totIssPriceWTax,2);
                       	  
                          	
                                  if(iIndex+1 == movQryList.size()){ 
                                      irow=irow+1;
                                      bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                                      result +=  "<TR bgcolor ="+bgcolor+" >"
                                              +"<TD colspan=17></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalIssPrice,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totIssPriceWTax,2))+"</b></TD><TD></TD> </TR>";
                                        
                         }
                         }
                              irow=irow+1;
                              iIndex=iIndex+1;
                              lastProduct = custcode;
                              if(movQryList.size()==iCnt+1){
                             	 irow=irow+1;
                                  bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                                  result +=  "<TR bgcolor ="+bgcolor+" >"
                                          +"<TD colspan=17></TD> <TD align= \"right\"><b>Grand Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(priceGrandTot,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(taxGrandTot,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(priceWTaxGrandTot,2))+"</b></TD><TD></TD> </TR>";

                              }
                              
                             resultJsonInt.put("OUTBOUNDDETAILS", result);
                          
                             jsonArray.add(resultJsonInt);*/

                 }
                
                     resultJson.put("items", jsonArray);
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
                     resultJson.put("items", jsonArray);
                     resultJson.put("errors", jsonArrayErr);
             }
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
         return resultJson;
 }
         
 private JSONObject getRangeTableDetails(HttpServletRequest request) {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayErr = new JSONArray();
            try {
            
                String plant = StrUtils.fString(request.getParameter("PLANT"))
                               .trim();
                String location = StrUtils.fString(request.getParameter("LOC"))
                               .trim();
                String item = StrUtils.fString(request.getParameter("ITEMNO"))
                               .trim();
                String startRange = StrUtils.fString(request.getParameter("SRANGE"))
                               .trim();
                String suffix = StrUtils.fString(request.getParameter("SUFFIX"))
                               .trim();
                String dtfrmt = StrUtils.fString(request.getParameter("DTFRMT"))
                               .trim();
                String sRangeCnt = StrUtils.fString(request.getParameter("RANGECNT"))
                               .trim();
                String PickQty = StrUtils.fString(request.getParameter("PICKQTY"))
                               .trim();
                String nonStkFlg = StrUtils.fString(request.getParameter("NONSTKFLG")) .trim();
                String uom = StrUtils.fString(request.getParameter("UOM")) .trim();
//                String UOMQTY="1";
//				Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
//				MovHisDAO movHisDao1 = new MovHisDAO();
//				ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+plant+"_UOM where UOM='"+uom+"'",htTrand1);
//				if(getuomqty.size()>0)
//				{
//				Map mapval = (Map) getuomqty.get(0);
//				UOMQTY=(String)mapval.get("UOMQTY");
//				}
                    int rangeCount = Integer.parseInt(sRangeCnt);
                    rangeCount=rangeCount+1;
                    Double totalQty = new Double(0);
                   
                    if (rangeCount > 0) {
                        for(int index = 1; index<=rangeCount; index++) {
                                    String batch = suffix+dtfrmt+Long.toString(Long.parseLong(startRange)+index-1);
                                    JSONObject resultJsonInt = new JSONObject();
                                    ArrayList resultList = new InvMstDAO().getAvaliableQtyForBatchMutiUOM(plant, item, location, batch,uom);
                                  if(!nonStkFlg.equalsIgnoreCase("Y"))    {      
                                    if (resultList.size() > 0) {
                                            Map resultMap = (Map) resultList.get(0);
                                            if (resultMap.size() > 0) {
                                                double availQty = new Double( (String) resultMap.get("qty"));
                                                double pickQty = Double.valueOf(PickQty);
                                                if(pickQty>availQty){
                                                    resultJsonInt.put("DATACOLOR", "red");   
                                                }else{
                                                    resultJsonInt.put("DATACOLOR", "black");  
                                                }
                                                resultJsonInt.put("BATCH", resultMap.get("batch"));
                                                resultJsonInt.put("AVAILQTY", resultMap.get("qty"));
                                                totalQty = totalQty + new Double( (String) resultMap.get("qty"));    
                                            }
                                      } else {
                                                resultJsonInt.put("BATCH", batch);
                                                resultJsonInt.put("AVAILQTY","0");
                                                resultJsonInt.put("DATACOLOR", "red"); 
                                                totalQty = totalQty +  0;   
                                            }
                                   }else{
                                    	  resultJsonInt.put("BATCH", batch);
                                          resultJsonInt.put("AVAILQTY","0");
                                          resultJsonInt.put("DATACOLOR", "black"); 
                                          totalQty = totalQty +  0;  
                                      }
                                    resultJsonInt.put("PICKQTY", PickQty);
                                    jsonArray.add(resultJsonInt);
                                    
                            }
                            resultJson.put("SEARCH_DATA", jsonArray);
                            resultJson.put("TOTAL_QTY", totalQty);
                            JSONObject resultJsonInt = new JSONObject();
                            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                            resultJsonInt.put("ERROR_CODE", "100");
                            jsonArrayErr.add(resultJsonInt);
                            resultJson.put("ERROR", jsonArrayErr);
                    } else {
                            resultJson.put("SEARCH_DATA", "");
                            resultJson.put("TOTAL_QTY", 0);
                            JSONObject resultJsonInt = new JSONObject();
                            resultJsonInt.put("ERROR_MESSAGE", " Enter Valid Range to Pick!");
                            resultJsonInt.put("ERROR_CODE", "98");
                            jsonArrayErr.add(resultJsonInt);
                            resultJson.put("ERROR", jsonArrayErr);
                    }
            } catch (Exception e) {
                    resultJson.put("SEARCH_DATA", "");
                    resultJson.put("TOTAL_QTY", 0);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "Enter Valid Range to Pick!!");
                    resultJsonInt.put("ERROR_CODE", "98");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("ERROR", jsonArrayErr);
            }
            return resultJson;
    }
    
    private JSONObject getOutboundDetailsToPrint(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        HTReportUtil movHisUtil       = new HTReportUtil();
        //DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
        
//        DecimalFormat decformat = new DecimalFormat("#,##0.00");
       
        //StrUtils strUtils = new StrUtils();
        String fdate="",tdate="";
    
        try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
           String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
           String  CUSTNAME = StrUtils.fString(request.getParameter("CNAME"));
           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
           String  INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
           String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
           String  PICKSTATUS = StrUtils.fString(request.getParameter("PICKSTATUS"));
           String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
           String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
           String TYPE = StrUtils.fString(request.getParameter("TYPE"));
//           String INVOICE = StrUtils.fString(request.getParameter("INVOICE"));
           String EMPNO = StrUtils.fString(request.getParameter("EMPNO"));
           String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMERTYPE"));
           String UOM = StrUtils.fString(request.getParameter("UOM"));
           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

           if (FROM_DATE.length()>5)

            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

              
   
           Hashtable ht = new Hashtable();
           if(StrUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
           if(StrUtils.fString(ORDERNO).length() > 0)      ht.put("B.DONO",ORDERNO);
           if(StrUtils.fString(INVOICENO).length() > 0)      ht.put("B.INVOICENO",INVOICENO);
           if(StrUtils.fString(ORDERTYPE).length() > 0)    ht.put("A.ORDERTYPE",ORDERTYPE);
           if(StrUtils.fString(PICKSTATUS).length() > 0)   ht.put("A.STATUS",PICKSTATUS);
           if(StrUtils.fString(statusID).length() > 0) ht.put("A.STATUS_ID",statusID);
           if(StrUtils.fString(EMPNO).length() > 0) ht.put("A.EMPNO",EMPNO);
           if(StrUtils.fString(UOM).length() > 0) ht.put("B.UNITMO",UOM);
           movQryList = movHisUtil.getDOSummaryToPrint(ht,fdate,tdate,DIRTYPE,PLANT, CUSTNAME,CUSTOMERTYPE,TYPE);
                         
            if (movQryList.size() > 0) {
            int Index = 0;
//             int iIndex = 0,irow = 0;
             
//            double sumprdQty = 0;String lastProduct="";
            
//            double totalOrdPrice=0,totaltax=0,totOrdPriceWTax=0,priceGrandTot=0,taxGrandTot=0,priceWTaxGrandTot=0;//issPriceSubTot=0,OrdPriceGrandTot=0,taxGrandTotal=0,priceWTaxGrandTotal=0;
            ;
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                      
//                        String result="";
                        Map lineArr = (Map) movQryList.get(iCnt);
//                        String custcode =(String)lineArr.get("custcode");
                        String dono = (String)lineArr.get("dono");
                         
//                        String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                        JSONObject resultJsonInt = new JSONObject();
                       
                        String qtyOrValue =(String)lineArr.get("qtyor");
               			String qtyPickValue =(String)lineArr.get("qtypick");
               			String qtyValue =(String)lineArr.get("qty");
               			
                        float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
                        float qtyPickVal ="".equals(qtyPickValue) ? 0.0f :  Float.parseFloat(qtyPickValue);
                        float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
                        
                        if(qtyOrVal==0f){
                        	qtyOrValue="0.000";
                        }else{
                        	qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(qtyPickVal==0f){
                        	qtyPickValue="0.000";
                        }else{
                        	qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(qtyVal==0f){
                        	qtyValue="0.000";
                        }else{
                        	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        
                        	Index = Index + 1;
                        	//resultJsonInt.put("Index", Index);
                        	 	resultJsonInt.put("dono", dono);
                      	   		resultJsonInt.put("ordertype", StrUtils.fString((String)lineArr.get("ordertype")));
                      	   	resultJsonInt.put("jobnum", StrUtils.fString((String)lineArr.get("jobnum")));
                      	  resultJsonInt.put("custname", StrUtils.fString((String)lineArr.get("custname")));
                      	  resultJsonInt.put("qtyor", qtyOrValue);
                      	resultJsonInt.put("qtypick",qtyPickValue );
                      	resultJsonInt.put("qty",qtyValue );
                      	resultJsonInt.put("empname", StrUtils.fString((String)lineArr.get("empname")));
                      	resultJsonInt.put("status", StrUtils.fString((String)lineArr.get("status")));
                      	resultJsonInt.put("status_id", StrUtils.fString((String)lineArr.get("status_id")));
                      	resultJsonInt.put("UOM", StrUtils.fString((String)lineArr.get("UOM")));
                         	    jsonArray.add(resultJsonInt);
                        	
                        	/*result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                        +"<TD align=CENTER><font color=black><INPUT Type=checkbox style=border: 0; name=chkdDoNo value="+dono+" ></font></TD>"
                        +"<td align=center>" +Index+ "</td>"
                        + "<td><a href=/track/jsp/PrintDODetails.jsp?DONO=" +dono+ "&INVOICE="+INVOICE+"&TYPE="+TYPE+"&FROMDATE="+fdate+"&TODATE="+tdate+">" + dono + "</a></td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("ordertype")) + "</td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("jobnum")) + "</td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("custname")) + "</td>"
                        //+ "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("CollectionDate")) + "</td>"
                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qtyor")) + "</td>"
                        + "<td  align = right>&nbsp;&nbsp;&nbsp;"+ StrUtils.formatNum((String)lineArr.get("qtypick")) + "</td>"
                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qty")) + "</td>"
                        + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("empname")) + "</td>"
                        + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("status")) + "</td>"
                        + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("status_id")) + "</td>"
                        + "<tr>";
                        
                             irow=irow+1;
                             iIndex=iIndex+1;
                            
                            resultJsonInt.put("OUTBOUNDDETAILS", result);
                         
                            jsonArray.add(resultJsonInt);*/

                }
               
                    resultJson.put("items", jsonArray);
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
                    resultJson.put("items", jsonArray);

                    resultJson.put("errors", jsonArrayErr);
            }
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
        return resultJson;
}
    

    
  //CREATED BY NAVAS
    private JSONObject getConsignmentsmryDetailsWithIssue(HttpServletRequest request) {
    JSONObject resultJson = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    JSONArray jsonArrayErr = new JSONArray();
    HTReportUtil movHisUtil = new HTReportUtil();
    //DateUtils _dateUtils = new DateUtils();
    ArrayList movQryList = new ArrayList();

//    DecimalFormat decformat = new DecimalFormat("#,##0.00");

    //StrUtils strUtils = new StrUtils();
    String fdate="",tdate="";

    try {

    String PLANT= StrUtils.fString(request.getParameter("PLANT"));
    String ITEMNO = StrUtils.fString(request.getParameter("ITEM"));
    String FROMWAREHOUSE = StrUtils.fString(request.getParameter("FROMWAREHOUSE"));  /* Resvi*/
    String TOWAREHOUSE = StrUtils.fString(request.getParameter("TOWAREHOUSE")); /* Resvi*/
    String PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
    String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
    String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
    String DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
    String CUSTNAME = StrUtils.fString(request.getParameter("CNAME"));
    String ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
    String JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
    String PICKSTATUS = StrUtils.fString(request.getParameter("PICKSTATUS"));
    String RECEIVESTATUS = StrUtils.fString(request.getParameter("RECEIVESTATUS"));
    String ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
    String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
    String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
    String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
    String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
    String status = StrUtils.fString(request.getParameter("STATUS"));
    String SORT = StrUtils.fString(request.getParameter("SORT"));
    String EMPNO = StrUtils.fString(request.getParameter("EMPNO"));
//    String UOM = StrUtils.fString(request.getParameter("UOM"));
    String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMERTYPE"));


    if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
    String curDate =DateUtils.getDate();
    if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

    if (FROM_DATE.length()>5)
    fdate = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
    if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
    if (TO_DATE.length()>5)
    tdate = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);



    Hashtable ht = new Hashtable();
    if(StrUtils.fString(JOBNO).length() > 0) ht.put("JOBNUM",JOBNO);
    if(StrUtils.fString(ITEMNO).length() > 0) ht.put("ITEM",ITEMNO);
    if(StrUtils.fString(FROMWAREHOUSE).length() > 0) ht.put("FROMWAREHOUSE",FROMWAREHOUSE);
    if(StrUtils.fString(TOWAREHOUSE).length() > 0) ht.put("TOWAREHOUSE",TOWAREHOUSE);
    if(StrUtils.fString(ORDERNO).length() > 0) ht.put("TONO",ORDERNO);
    //if(StrUtils.fString(CUSTNAME).length() > 0) ht.put("A.CUSTNAME",strUtils.InsertQuotes(CUSTNAME));
    if(StrUtils.fString(ORDERTYPE).length() > 0) ht.put("ORDERTYPE",ORDERTYPE);
    if(StrUtils.fString(RECEIVESTATUS).length() > 0) ht.put("STATUS",RECEIVESTATUS);
    if(StrUtils.fString(PICKSTATUS).length() > 0) ht.put("PickStaus",PICKSTATUS);
    if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("ITEMTYPE",PRD_TYPE_ID);
    if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
    if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("PRD_CLS_ID",PRD_CLS_ID);
    if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("PRD_DEPT_ID",PRD_DEPT_ID);
    if(StrUtils.fString(status).length() > 0) ht.put("STATUS",status);
    if(StrUtils.fString(EMPNO).length() > 0) ht.put("EMPNO",EMPNO);
    if(StrUtils.fString(CUSTOMERTYPE).length() > 0) ht.put("CUSTTYPE",CUSTOMERTYPE);

    movQryList = movHisUtil.getCustomerTOInvoiceIssueSummary(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTNAME,SORT);


    if (movQryList.size() > 0) {
    int Index = 0;
//    int iIndex = 0,irow = 0;
    String customertypeid="",customertypedesc="";;
//    double sumprdQty = 0;String customerstatusid="",lastProduct="",customerstatusdesc="";
//
//    double totalIssPrice=0,totaltax=0,totIssPriceWTax=0,priceGrandTot=0,taxGrandTot=0,priceWTaxGrandTot=0,TotalIssPrice=0;//issPriceSubTot=0,OrdPriceGrandTot=0,taxGrandTotal=0,priceWTaxGrandTotal=0;
//    double issPriceGrandTot=0;
    for (int iCnt =0; iCnt<movQryList.size(); iCnt++){

//    String result="",custcode="";
    Map lineArr = (Map) movQryList.get(iCnt);

//    custcode=(String)lineArr.get("custcode");

    String tono = (String)lineArr.get("tono");
//    Float gstpercentage = Float.parseFloat(lineArr.get("Tax") == null ? "0.0" : ((String) lineArr.get("Tax").toString())) ;



//    String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF" : "#dddddd";
    JSONObject resultJsonInt = new JSONObject();

//    customerstatusid = customerBeanDAO.getCustomerStatusId(PLANT,(String)lineArr.get("custname"));
//    if(customerstatusid == null || customerstatusid.equals(""))
//    {
//    customerstatusdesc="";
//    }
//    else
//    {
//    customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(PLANT,customerstatusid);
//    }

    customertypeid = customerBeanDAO.getCustomerTypeId(PLANT,(String)lineArr.get("custname"));
    if(customertypeid == null || customertypeid.equals(""))
    {
    customertypedesc="";
    }
    else
    {
    customertypedesc = customerBeanDAO.getCustomerTypeDesc(PLANT,customertypeid);
    }

    String qtyOrValue =(String)lineArr.get("qtyor");
    String qtyPickValue =(String)lineArr.get("qtypick");
    String qtyValue =(String)lineArr.get("qty");
    String qtyInvValue =(String)lineArr.get("qtyac");
    String unitcost = (String)lineArr.get("unitcost");


    float qtyOrVal ="".equals(qtyOrValue) ? 0.0f : Float.parseFloat(qtyOrValue);
    float qtyPickVal ="".equals(qtyPickValue) ? 0.0f : Float.parseFloat(qtyPickValue);
    float qtyVal ="".equals(qtyValue) ? 0.0f : Float.parseFloat(qtyValue);
    float qtyInvVal ="".equals(qtyInvValue) ? 0.0f : Float.parseFloat(qtyInvValue);
    float unitPriceVal="".equals(unitcost) ? 0.0f : Float.parseFloat(unitcost);

    if(qtyOrVal==0f){
    qtyOrValue="0.000";
    }else{
    qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
    }
    
    if(qtyPickVal==0f){
    qtyPickValue="0.000";
    }else{
    qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
    }
    
    if(qtyVal==0f){
    qtyValue="0.000";
    }else{
    qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
    }
    
    if(qtyInvVal==0f){
    qtyInvValue="0.000";
    }else{
    qtyInvValue=qtyInvValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
    }
    
    if(unitPriceVal==0f){
    unitcost="0.00000";
    }else{
    unitcost=unitcost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
    }


    // if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(custcode))){

    Index = Index + 1;

    resultJsonInt.put("Index",Index);
    resultJsonInt.put("tono",(tono));
    resultJsonInt.put("orderType", StrUtils.fString((String)lineArr.get("orderType")));
    resultJsonInt.put("jobnum", StrUtils.fString((String)lineArr.get("jobnum")));
    resultJsonInt.put("custname", StrUtils.fString((String)lineArr.get("custname")));
    resultJsonInt.put("customertypedesc", StrUtils.fString(customertypedesc));
    resultJsonInt.put("fromlocation", StrUtils.fString((String)lineArr.get("fromlocation")));
    resultJsonInt.put("tolocation", StrUtils.fString((String)lineArr.get("tolocation")));
    resultJsonInt.put("remarks", StrUtils.fString((String)lineArr.get("remarks")));
    resultJsonInt.put("remarks2", StrUtils.fString((String)lineArr.get("remarks2")));
    resultJsonInt.put("item", StrUtils.fString((String)lineArr.get("item")));
    resultJsonInt.put("itemdesc", StrUtils.fString((String)lineArr.get("itemdesc")));
    resultJsonInt.put("Recvdate", StrUtils.fString((String)lineArr.get("issuedate")));
    resultJsonInt.put("UOM", StrUtils.fString((String)lineArr.get("UOM")));
    resultJsonInt.put("qtyor", qtyOrValue);
    resultJsonInt.put("qtypick", qtyPickValue);
    resultJsonInt.put("qty",qtyValue );
    resultJsonInt.put("qtyac", qtyInvValue);  //resvi
    resultJsonInt.put("empname", StrUtils.fString((String)lineArr.get("empname")));
    //resultJsonInt.put("unitprice", unitprice);
    resultJsonInt.put("receivestatus", StrUtils.fString((String)lineArr.get("status_id")));
    resultJsonInt.put("users", StrUtils.fString((String)lineArr.get("users")));
    jsonArray.add(resultJsonInt);



    }

    resultJson.put("items", jsonArray);
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
    resultJson.put("items", jsonArray);

    resultJson.put("errors", jsonArrayErr);
    }
    } catch (Exception e) {
    e.printStackTrace();
    jsonArray.add("");
    resultJson.put("items", jsonArray);
    resultJson.put("SEARCH_DATA", "");
    JSONObject resultJsonInt = new JSONObject();
    resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
    resultJsonInt.put("ERROR_CODE", "98");
    jsonArrayErr.add(resultJsonInt);
    resultJson.put("ERROR", jsonArrayErr);
    }
    return resultJson;
    }
    //END BY NAVAS
    
    private JSONObject getOutboundsmryDetailsWithIssue(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        HTReportUtil movHisUtil       = new HTReportUtil();
		PlantMstUtil plantmstutil = new PlantMstUtil();
        //DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
        
//        DecimalFormat decformat = new DecimalFormat("#,##0.00");
       
        //StrUtils strUtils = new StrUtils();
        String fdate="",tdate="";

        try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String ITEMNO    = StrUtils.fString(request.getParameter("ITEM"));
           String  PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
           String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
           String  CUSTNAME = StrUtils.fString(request.getParameter("CNAME"));
           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
           String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
           String  PICKSTATUS = StrUtils.fString(request.getParameter("PICKSTATUS"));
           String  ISSUESTATUS = StrUtils.fString(request.getParameter("ISSUESTATUS"));
           String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
           String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
           String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
           String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
           String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
           String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
           String SORT = StrUtils.fString(request.getParameter("SORT"));
           String EMPNO = StrUtils.fString(request.getParameter("EMPNO"));
//           String UOM = StrUtils.fString(request.getParameter("UOM"));
           String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMERTYPE"));
           String POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
           List viewlistQry = plantmstutil.getPlantMstDetails(PLANT);
           Map map = (Map) viewlistQry.get(0);
           String DELIVERYAPP = StrUtils.fString((String)map.get("ISRIDERRAPP"));
           
           
           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
           	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

              
   
           Hashtable ht = new Hashtable();
           if(StrUtils.fString(JOBNO).length() > 0)        ht.put("JOBNUM",JOBNO);
           if(StrUtils.fString(ITEMNO).length() > 0)       ht.put("ITEM",ITEMNO);
           if(StrUtils.fString(ORDERNO).length() > 0)      ht.put("DONO",ORDERNO);
           //if(StrUtils.fString(CUSTNAME).length() > 0)     ht.put("A.CUSTNAME",strUtils.InsertQuotes(CUSTNAME));
           if(StrUtils.fString(ORDERTYPE).length() > 0)    ht.put("ORDERTYPE",ORDERTYPE);
           if(StrUtils.fString(ISSUESTATUS).length() > 0)  ht.put("STATUS",ISSUESTATUS);
           if(StrUtils.fString(PICKSTATUS).length() > 0)   ht.put("PickStaus",PICKSTATUS);
           if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("ITEMTYPE",PRD_TYPE_ID);
	        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
	        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("PRD_CLS_ID",PRD_CLS_ID);
	        if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("PRD_DEPT_ID",PRD_DEPT_ID);
	        if(StrUtils.fString(statusID).length() > 0) ht.put("STATUS_ID",statusID);
	        if(StrUtils.fString(EMPNO).length() > 0) ht.put("EMPNO",EMPNO);
	        if(StrUtils.fString(CUSTOMERTYPE).length() > 0) ht.put("CUSTTYPE",CUSTOMERTYPE);
  
	       	movQryList = movHisUtil.getCustomerDOInvoiceIssueSummary(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTNAME,SORT,POSSEARCH);
	       
           
            if (movQryList.size() > 0) {
            int Index = 0;
//             int iIndex = 0,irow = 0;
             String  customerstatusid="",customerstatusdesc="",customertypeid="",customertypedesc="";;
//            double sumprdQty = 0;String lastProduct="";
//            
//            double totalIssPrice=0,totaltax=0,totIssPriceWTax=0,priceGrandTot=0,taxGrandTot=0,priceWTaxGrandTot=0,TotalIssPrice=0;//issPriceSubTot=0,OrdPriceGrandTot=0,taxGrandTotal=0,priceWTaxGrandTotal=0;
//            double issPriceGrandTot=0;
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                      
//                        String result="",custcode="";
                        Map lineArr = (Map) movQryList.get(iCnt);
                        
//                       	 custcode=(String)lineArr.get("custcode");
                      
                        String dono = (String)lineArr.get("dono");
//                        Float gstpercentage =  Float.parseFloat(lineArr.get("Tax") == null ? "0.0" : ((String) lineArr.get("Tax").toString())) ;
            			            			
                       
          	                                    
//                        String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                        JSONObject resultJsonInt = new JSONObject();
                        
                        customerstatusid = customerBeanDAO.getCustomerStatusId(PLANT,(String)lineArr.get("custname"));
                        if(customerstatusid == null || customerstatusid.equals(""))
            			   {
            			   customerstatusdesc="";
            			   }
            			   else
            			   {
            			    customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(PLANT,customerstatusid);
            			   }
                        
                        customertypeid = customerBeanDAO.getCustomerTypeId(PLANT,(String)lineArr.get("custname"));
              			 if(customertypeid == null || customertypeid.equals(""))
              			 {
              				customertypedesc="";
              			 }
              			 else
              			 {
              				customertypedesc = customerBeanDAO.getCustomerTypeDesc(PLANT,customertypeid);
              			 }
              			 
              			String qtyOrValue =(String)lineArr.get("qtyor");
               			String qtyPickValue =(String)lineArr.get("qtypick");
               			String qtyValue =(String)lineArr.get("qty");
               			String unitprice = (String)lineArr.get("unitprice");
               			String deliverystatus = (String)lineArr.get("DELIVERY_STATUS");
               			String intransit = (String)lineArr.get("INTRANSIT_STATUS");
               			
               			//by imthi -->to display pic,issue column in salesorder/summarydetails AT 17-12-2021
               			String pickstatus = (String)lineArr.get("PickStatus");
               			String lnstat = (String)lineArr.get("LNSTAT");
               			
               			
               			//PICKSTATUS
               				//if(STS.equalsIgnoreCase("N"))
               				//STS="OPEN";
            			 if(pickstatus.equalsIgnoreCase("O"))
            				 pickstatus="PARTIALLY PICKED";
            			else if(pickstatus.equalsIgnoreCase("C"))
            				pickstatus="PICKED";
            			 
            			 //ISSUESTATUS
            			 	//if(isSTS.equalsIgnoreCase("N"))
            			 	//isSTS="OPEN";
            			 if(lnstat.equalsIgnoreCase("O"))
            				 lnstat="PARTIALLY ISSUED";
    					if(DELIVERYAPP.equals("1")){
    						 if(lnstat.equalsIgnoreCase("C"))
    							 lnstat="ISSUED";
    							else if(lnstat.equalsIgnoreCase("S"))
    								lnstat="SHIPPED";
    							else if(lnstat.equalsIgnoreCase("D"))
    								lnstat="DELIVERED";
    						}else{
    							 if(lnstat.equalsIgnoreCase("C"))
    								 lnstat="SHIPPED";
    					 		}
    					if(ISSUESTATUS.equalsIgnoreCase("")) {
    					if(!intransit.equalsIgnoreCase("")){
    						lnstat = intransit; 
    		 			}
    		 			if(!deliverystatus.equalsIgnoreCase("")){
    						lnstat = deliverystatus; 
    		 			}
    					}
    		 			
    					if(ISSUESTATUS.equalsIgnoreCase("DELIVERED")) {
    						lnstat = deliverystatus; 
    					}else if(ISSUESTATUS.equalsIgnoreCase("INTRANSIT")) {
    						lnstat = intransit; 
    					}
               			
                        float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
                        float qtyPickVal ="".equals(qtyPickValue) ? 0.0f :  Float.parseFloat(qtyPickValue);
                        float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
                        float unitPriceVal="".equals(unitprice) ? 0.0f :  Float.parseFloat(unitprice);
                        
                        if(qtyOrVal==0f){
                        	qtyOrValue="0.000";
                        }else{
                        	qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(qtyPickVal==0f){
                        	qtyPickValue="0.000";
                        }else{
                        	qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(qtyVal==0f){
                        	qtyValue="0.000";
                        }else{
                        	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(unitPriceVal==0f){
                        	unitprice="0.00000";
                        }else{
                        	unitprice=unitprice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
              			 
              			 
                      // if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(custcode))){
              			
              			 Index = Index + 1;
                       	
                        resultJsonInt.put("Index",Index);
                        resultJsonInt.put("dono",(dono));
                        resultJsonInt.put("orderType", StrUtils.fString((String)lineArr.get("orderType")));
                        resultJsonInt.put("jobnum", StrUtils.fString((String)lineArr.get("jobnum")));
                        resultJsonInt.put("custname", StrUtils.fString((String)lineArr.get("custname")));
                        resultJsonInt.put("customertypedesc", StrUtils.fString(customertypedesc));
                        resultJsonInt.put("customerstatusdesc", StrUtils.fString(customerstatusdesc));
                        resultJsonInt.put("remarks", StrUtils.fString((String)lineArr.get("remarks")));
                        resultJsonInt.put("remarks2", StrUtils.fString((String)lineArr.get("remarks2")));
                        resultJsonInt.put("item", StrUtils.fString((String)lineArr.get("item")));
                        resultJsonInt.put("itemdesc", StrUtils.fString((String)lineArr.get("itemdesc")));
                        resultJsonInt.put("issuedate", StrUtils.fString((String)lineArr.get("issuedate")));
                        resultJsonInt.put("UOM", StrUtils.fString((String)lineArr.get("UOM")));
//                        resultJsonInt.put("qtyor", qtyOrValue);
                        resultJsonInt.put("qtyor", StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) qtyOrValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
//                        resultJsonInt.put("qtypick", qtyPickValue);
                        resultJsonInt.put("qtypick", StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) qtyPickValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
//                        resultJsonInt.put("qty",qtyValue );
                        resultJsonInt.put("qty", StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) qtyValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
                        resultJsonInt.put("empname", StrUtils.fString((String)lineArr.get("empname")));
                        resultJsonInt.put("PickStaus", pickstatus);
                        resultJsonInt.put("STATUS", lnstat.toUpperCase());
                        //resultJsonInt.put("unitprice", unitprice);
                        resultJsonInt.put("status_id", StrUtils.fString((String)lineArr.get("status_id")));
                        resultJsonInt.put("users", StrUtils.fString((String)lineArr.get("users")));
//                        resultJsonInt.put("DELIVERYSTATUS", deliverystatus.toUpperCase());
                        jsonArray.add(resultJsonInt);
              
                   

                }
               
                    resultJson.put("items", jsonArray);
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
                    resultJson.put("items", jsonArray);

                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        		jsonArray.add("");
        		resultJson.put("items", jsonArray);
                resultJson.put("SEARCH_DATA", "");
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
    
 private JSONObject getOutboundsmryIssueDetailsWithAverageCost(HttpServletRequest request,String baseCurrency) {
     JSONObject resultJson = new JSONObject();
     JSONArray jsonArray = new JSONArray();
     JSONArray jsonArrayErr = new JSONArray();
     HTReportUtil movHisUtil       = new HTReportUtil();
     //DateUtils _dateUtils = new DateUtils();
     ArrayList movQryList  = new ArrayList();
     
//     DecimalFormat decformat = new DecimalFormat("#,##0.00");
    
     //StrUtils strUtils = new StrUtils();
     String fdate="",tdate="",taxby="";
     
     try {
     
        String PLANT= StrUtils.fString(request.getParameter("PLANT"));
        String ITEMNO    = StrUtils.fString(request.getParameter("ITEM"));
        String  PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
        String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
        String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
        String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
        String  CUSTNAME = StrUtils.fString(request.getParameter("CNAME"));
        String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
        String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
        String  PICKSTATUS = StrUtils.fString(request.getParameter("PICKSTATUS"));
        String  ISSUESTATUS = StrUtils.fString(request.getParameter("ISSUESTATUS"));
        String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
        String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
        String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
        String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
        String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
        String SORT = StrUtils.fString(request.getParameter("SORT"));
        String EMPNO = StrUtils.fString(request.getParameter("EMPNO"));
//        String UOM = StrUtils.fString(request.getParameter("UOM"));
        String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMERTYPE"));
        String  CURRENCYID      = StrUtils.fString(request.getParameter("CURRENCYID"));
        String  CURRENCYDISPLAY = StrUtils.fString(request.getParameter("CURRENCYDISPLAY"));
        
        if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
        String curDate =DateUtils.getDate();
        if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

        if (FROM_DATE.length()>5)
        	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
        if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
        if (TO_DATE.length()>5)
        tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

           

        Hashtable ht = new Hashtable();
        if(StrUtils.fString(JOBNO).length() > 0)        ht.put("JOBNUM",JOBNO);
        if(StrUtils.fString(ITEMNO).length() > 0)       ht.put("ITEM",ITEMNO);
        if(StrUtils.fString(ORDERNO).length() > 0)      ht.put("DONO",ORDERNO);
        //if(StrUtils.fString(CUSTNAME).length() > 0)     ht.put("A.CUSTNAME",strUtils.InsertQuotes(CUSTNAME));
        if(StrUtils.fString(ORDERTYPE).length() > 0)    ht.put("ORDERTYPE",ORDERTYPE);
        if(StrUtils.fString(ISSUESTATUS).length() > 0)  ht.put("STATUS",ISSUESTATUS);
        if(StrUtils.fString(PICKSTATUS).length() > 0)   ht.put("PickStaus",PICKSTATUS);
        if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("ITEMTYPE",PRD_TYPE_ID);
        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("PRD_CLS_ID",PRD_CLS_ID);
        if(StrUtils.fString(statusID).length() > 0) ht.put("STATUS_ID",statusID);
        if(StrUtils.fString(EMPNO).length() > 0) ht.put("EMPNO",EMPNO);
        if(StrUtils.fString(CUSTOMERTYPE).length() > 0) ht.put("CUSTTYPE",CUSTOMERTYPE);
        
        if(CURRENCYID.equals(""))
        {
                 CurrencyDAO  _CurrencyDAO  = new CurrencyDAO();
                 List listQry = _CurrencyDAO.getCurrencyList(PLANT,CURRENCYDISPLAY);
                 for(int i =0; i<listQry.size(); i++) {
                     Map m=(Map)listQry.get(i);
                     CURRENCYID=(String)m.get("currencyid");
                 }
        }        
        taxby=_PlantMstDAO.getTaxBy(PLANT);
        if(taxby.equalsIgnoreCase("BYORDER"))
        {
        	movQryList = movHisUtil.getCustomerDOAvgCostIssueSummary(ht,fdate,tdate,DIRTYPE,PLANT,PRD_DESCRIP,CUSTNAME,SORT,CURRENCYID,baseCurrency);
        }
        else
        {
        	movQryList = movHisUtil.getCustomerDOAvgCostIssueSummaryByProductGst(ht,fdate,tdate,DIRTYPE,PLANT,PRD_DESCRIP,CUSTNAME,SORT,CURRENCYID,baseCurrency);
        }
      
       
        
         if (movQryList.size() > 0) {
         int Index = 0;
//          int iIndex = 0,irow = 0;
          String customerstatusid ="",customerstatusdesc="",customertypeid ="",customertypedesc="";
//         double sumprdQty = 0;String lastProduct="";
         
         double totalIssAvgcost=0,totaltax=0,totIssAvgcostWTax=0,avgcostGrandTot=0,taxGrandTot=0,avgcostWTaxGrandTot=0;//,TotalIssavgcost=0;
         double avgcost=0;
             for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                   
//                     String result="",custcode="";
                     Map lineArr = (Map) movQryList.get(iCnt);
//                     if(SORT.equalsIgnoreCase("PRODUCT"))
//                     {
//                    	 custcode=(String)lineArr.get("item");
//                     }
//                     else
//                     {
//                    	if(lineArr.get("custcode")!=null)
//                    		 custcode=(String)lineArr.get("custcode");
//                    	 else
//                    		 custcode="";
//                     }
                     String dono = (String)lineArr.get("dono");
                     Float gstpercentage=0.0f;
                     if(lineArr.get("Tax") != null)
                       gstpercentage =  Float.parseFloat(((String)lineArr.get("Tax").toString()));
         			            			
                     avgcost = Double.parseDouble((String)lineArr.get("AVERAGE_COST"));
                     //avgcost = StrUtils.RoundDB(avgcost,2);
       	          
       	             //indiviual subtotal price details
       	             double issueAvgcost = (Double.parseDouble((String)lineArr.get("qty")))*Double.parseDouble((String)lineArr.get("AVERAGE_COST"));
       	             //issueAvgcost = StrUtils.RoundDB(issueAvgcost,2);
                     double tax = (issueAvgcost*gstpercentage)/100;
                     double issAvgcostwTax = issueAvgcost+tax;
                     
                     //total price details
                     totalIssAvgcost=totalIssAvgcost+(Double.parseDouble((String)lineArr.get("qty")))*Double.parseDouble((String)lineArr.get("AVERAGE_COST"));
                     totaltax  =totaltax + tax;
                     totIssAvgcostWTax = totIssAvgcostWTax+issAvgcostwTax;
                     
                     //Grand total details
                     avgcostGrandTot = avgcostGrandTot+(Double.parseDouble((String)lineArr.get("qty")))*Double.parseDouble((String)lineArr.get("AVERAGE_COST"));
                     taxGrandTot = taxGrandTot+tax;
                     avgcostWTaxGrandTot = avgcostWTaxGrandTot+issAvgcostwTax;
                     
                     customerstatusid = customerBeanDAO.getCustomerStatusId(PLANT,(String)lineArr.get("custname"));
                     if(customerstatusid == null || customerstatusid.equals(""))
         			   {
         			   customerstatusdesc="";
         			   }
         			   else
         			   {
         			    customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(PLANT,customerstatusid);
         			   }
                     
                     customertypeid = customerBeanDAO.getCustomerTypeId(PLANT,(String)lineArr.get("custname"));
          			 if(customertypeid == null || customertypeid.equals(""))
          			 {
          				customertypedesc="";
          			 }
          			 else
          			 {
          				customertypedesc = customerBeanDAO.getCustomerTypeDesc(PLANT,customertypeid);
          			 }
          			 
          			String qtyOrValue =(String)lineArr.get("qtyor");
           			String qtyPickValue =(String)lineArr.get("qtyPick");
           			if(qtyPickValue==null)
           				qtyPickValue =(String)lineArr.get("qtypick");
           			String qtyValue =(String)lineArr.get("qty");
           			String avgCostValue = String.valueOf(avgcost);
           			String gstpercentageValue = String.valueOf(gstpercentage);
           			String issueAvgcostValue = String.valueOf(issueAvgcost);
           			String taxValue = String.valueOf(tax);
           			String issAvgcostTaxValue = String.valueOf(issAvgcostwTax);
           			
           			
                    float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
                    float qtyPickVal ="".equals(qtyPickValue) ? 0.0f :  Float.parseFloat(qtyPickValue);
                    float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
                    float avgCostVal ="".equals(avgCostValue) ? 0.0f :  Float.parseFloat(avgCostValue);
                    float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);
                    float issueAvgcostVal ="".equals(issueAvgcostValue) ? 0.0f :  Float.parseFloat(issueAvgcostValue);
                    float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);
                    float issAvgcostTaxVal ="".equals(issAvgcostTaxValue) ? 0.0f :  Float.parseFloat(issAvgcostTaxValue);
                    
                    if(qtyOrVal==0f){
                    	qtyOrValue="0.000";
                    }else{
                    	qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                    }if(qtyPickVal==0f){
                    	qtyPickValue="0.000";
                    }else{
                    	qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                    }if(qtyVal==0f){
                    	qtyValue="0.000";
                    }else{
                    	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                    }if(avgCostVal==0f) {
                    	avgCostValue="0.00000";
                    }else {
                    	avgCostValue=avgCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                    }if(gstpercentageVal==0f) {
                    	gstpercentageValue="0.000";
                    }else {
                    	gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                    }if(issueAvgcostVal==0f) {
                    	issueAvgcostValue="0.00000";
                    }else {
                    	issueAvgcostValue=issueAvgcostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                    }if(taxVal==0f) {
                    	taxValue="0.000";
                    }else {
                    	taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                    }if(issAvgcostTaxVal==0f) {
                    	issAvgcostTaxValue="0.00000";
                    }else {
                    	issAvgcostTaxValue=issAvgcostTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                    }
                          
                         
//                     String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                     JSONObject resultJsonInt = new JSONObject();
                    
                   // if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(custcode))){
                    	Index = Index + 1;
                    	  resultJsonInt.put("Index", (Index));
                          resultJsonInt.put("dono", (dono));
                          resultJsonInt.put("orderType", StrUtils.fString((String)lineArr.get("orderType")));
                          resultJsonInt.put("jobnum", StrUtils.fString((String)lineArr.get("jobnum")));
                          resultJsonInt.put("custname", StrUtils.fString((String)lineArr.get("custname")));
                          resultJsonInt.put("customertypedesc", StrUtils.fString(customertypedesc));
                          resultJsonInt.put("customerstatusdesc", StrUtils.fString(customerstatusdesc));
                          resultJsonInt.put("remarks", StrUtils.fString((String)lineArr.get("remarks")));
                          resultJsonInt.put("remarks2", StrUtils.fString((String)lineArr.get("remarks2")));
                          resultJsonInt.put("item", StrUtils.fString((String)lineArr.get("item")));
                          resultJsonInt.put("itemdesc", StrUtils.fString((String)lineArr.get("itemdesc")));
                          resultJsonInt.put("issuedate", StrUtils.fString((String)lineArr.get("issuedate")));
                          resultJsonInt.put("UOM", StrUtils.fString((String)lineArr.get("UOM")));
                          resultJsonInt.put("qtyor", qtyOrValue );
                          resultJsonInt.put("qtyPick",qtyPickValue );
                          resultJsonInt.put("qty", qtyValue);
                          resultJsonInt.put("empname", StrUtils.fString((String)lineArr.get("empname")));
                          resultJsonInt.put("avgcost", avgCostValue);
                          resultJsonInt.put("gstpercentage",gstpercentageValue);
                          resultJsonInt.put("issueAvgcost", issueAvgcostValue);
                          resultJsonInt.put("tax", taxValue);
                          resultJsonInt.put("issAvgcostwTax", issAvgcostTaxValue);
                          resultJsonInt.put("status_id", StrUtils.fString((String)lineArr.get("status_id")));
                         
                      	   jsonArray.add(resultJsonInt);
                    	
                    	
                    /*	
                    	result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                     +"<td align=center>" +Index+ "</td>"
                     + "<td>"+dono +"</td>"
                     + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("orderType")) + "</td>"
                     + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("jobnum")) + "</td>"
                     + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("custname")) + "</td>"
                     + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customertypedesc) + "</td>"
                     + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customerstatusdesc) + "</td>"
                     + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks")) + "</td>"
                     + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks2")) + "</td>"
                     + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("item")) + "</td>"
                     + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("itemdesc")) + "</td>"
                     + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("issuedate")) + "</td>"
                     + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qtyor")) + "</td>"
                     + "<td  align = right>&nbsp;&nbsp;&nbsp;"+ StrUtils.formatNum((String)lineArr.get("qtypick")) + "</td>"
                     + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qty")) + "</td>"
                     + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("empname")) + "</td>"
                     + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(avgcost) + "</td>"
                     + "<td align = right>&nbsp;&nbsp;&nbsp;" +  gstpercentage + "</td>"
                     + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(issueAvgcost) + "</td>"
                     + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(tax) + "</td>"
                     + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(issAvgcostwTax) + "</td>"
                     + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("status_id")) + "</td>"
                     + "<tr>";
                     if(iIndex+1 == movQryList.size()){
                             irow=irow+1;
                             bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";  

                           result +=  "<TR bgcolor ="+bgcolor+" >"
                              +"<TD colspan=17></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalIssAvgcost,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totIssAvgcostWTax,2))+"</b></TD><TD></TD> </TR>";
                                
                      } }else{
                       
                    	  totalIssAvgcost=totalIssAvgcost-(Double.parseDouble((String)lineArr.get("qty")))*Double.parseDouble((String)lineArr.get("AVERAGE_COST"));
                    	  totalIssAvgcost = StrUtils.RoundDB(totalIssAvgcost,2);
                    	  
                    	  totaltax=totaltax-tax;
                    	  totaltax = StrUtils.RoundDB(totaltax,2);
                    	  
                    	  totIssAvgcostWTax=totIssAvgcostWTax-issAvgcostwTax;
                    	  totIssAvgcostWTax = StrUtils.RoundDB(totIssAvgcostWTax,2);
                    	   result +=  "<TR bgcolor ="+bgcolor+" >"
                                   +"<TD colspan=17></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalIssAvgcost,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totIssAvgcostWTax,2))+"</b></TD><TD></TD> </TR>";
                         irow=irow+1;
                         Index = 0;
                         Index=Index+1;
                         bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                         result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                         +"<td align=center>" +Index+ "</td>"
                         //+ "<td><a href=/track/deleveryorderservlet?DONO=" +dono+ "&Submit=View&RFLAG=4>" + dono + "</a></td>"
                         + "<td>"+dono +"</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("orderType")) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("jobnum")) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("custname")) + "</td>"
                          + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customertypedesc) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(customerstatusdesc) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks")) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remarks2")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("item")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("itemdesc")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("issuedate")) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qtyor")) + "</td>"
                         + "<td  align = right>&nbsp;&nbsp;&nbsp;"+ StrUtils.formatNum((String)lineArr.get("qtypick")) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.formatNum((String)lineArr.get("qty")) + "</td>"
                         + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("empname")) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(avgcost) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" +  gstpercentage + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(issueAvgcost) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(tax) + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + (DbBean.CURRENCYSYMBOL)+ decformat.format(issAvgcostwTax) + "</td>"
                         + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("status_id")) + "</td>"
                         + "<tr>";

                         totalIssAvgcost=(Double.parseDouble((String)lineArr.get("qty")))*Double.parseDouble((String)lineArr.get("AVERAGE_COST"));
                         totalIssAvgcost = StrUtils.RoundDB(totalIssAvgcost,2);
                   	  
                       	  totaltax=tax;
                       	  totaltax = StrUtils.RoundDB(totaltax,2);
                   	  
                       	totIssAvgcostWTax=issAvgcostwTax;
                       	totIssAvgcostWTax = StrUtils.RoundDB(totIssAvgcostWTax,2);
                   	  
                      	
                              if(iIndex+1 == movQryList.size()){ 
                                  irow=irow+1;
                                  bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                                  result +=  "<TR bgcolor ="+bgcolor+" >"
                                          +"<TD colspan=17></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalIssAvgcost,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totIssAvgcostWTax,2))+"</b></TD><TD></TD> </TR>";
                                    
                     }
                     }
                          irow=irow+1;
                          iIndex=iIndex+1;
                          lastProduct = custcode;
                          if(movQryList.size()==iCnt+1){
                         	 irow=irow+1;
                              bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                              result +=  "<TR bgcolor ="+bgcolor+" >"
                                      +"<TD colspan=17></TD> <TD align= \"right\"><b>Grand Total:</b></td><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(avgcostGrandTot,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(taxGrandTot,2))+"</b></TD><TD align= \"right\"><b>"+(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(avgcostWTaxGrandTot,2))+"</b></TD><TD></TD> </TR>";

                          }
                          
                         resultJsonInt.put("OUTBOUNDDETAILS", result);
                      
                         jsonArray.add(resultJsonInt);*/

             }
            
                  resultJson.put("items", jsonArray);
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
                 resultJson.put("items", jsonArray);

                 resultJson.put("errors", jsonArrayErr);
         }
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
     return resultJson;
}
   
 
 private JSONObject validateSupplier(String plant, 
			String supplier) {
		JSONObject resultJson = new JSONObject();
		try {
		   CustomerBeanDAO _customerBeanDAO = new CustomerBeanDAO();
			_customerBeanDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			if (_customerBeanDAO.isExistsSupplier(ht,
					" VENDNO like '"+ supplier + "%'  OR VNAME LIKE '" + supplier + "%'")) {
							
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
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
	
	private JSONObject generateINVOICE(String plant, String userId) {
		JSONObject resultJson = new JSONObject();
		try {
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			_TblControlDAO.setmLogger(mLogger); 
			
			String invno = _TblControlDAO.getNextOrder(plant,userId,"INVOICE");

			if (invno.length() > 0) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("invno", invno);
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
	
	private JSONObject generateITEMRETURN(String plant, String userId) {
		JSONObject resultJson = new JSONObject();
		try {
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			_TblControlDAO.setmLogger(mLogger); 
			
			String invno = _TblControlDAO.getNextOrder(plant,userId,"ITEMRETURN");
			
			if (invno.length() > 0) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("invno", invno);
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
	
	private JSONObject generateINVOICEQUOTE(String plant, String userId) {
		JSONObject resultJson = new JSONObject();
		try {
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			_TblControlDAO.setmLogger(mLogger); 
			
			String invno = _TblControlDAO.getNextOrder(plant,userId,"INVOICE_QUOTES");

			if (invno.length() > 0) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("invno", invno);
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
	private JSONObject getGoodsIssueWithPriceDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        HTReportUtil movHisUtil       = new HTReportUtil();
        //DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
        
//        DecimalFormat decformat = new DecimalFormat("#,##0.00");
       
        //StrUtils strUtils = new StrUtils();
        String fdate="",tdate="";

        try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String ITEMNO    = StrUtils.fString(request.getParameter("ITEM"));
           String  PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
           String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
           String  CUSTNAME = StrUtils.fString(request.getParameter("CNAME"));
           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
           String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
           String  PICKSTATUS = StrUtils.fString(request.getParameter("PICKSTATUS"));
           String  ISSUESTATUS = StrUtils.fString(request.getParameter("ISSUESTATUS"));
           String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
           String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
           String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
           String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));           
           String SORT = StrUtils.fString(request.getParameter("SORT"));
           String EMPNO = StrUtils.fString(request.getParameter("EMPNO"));
           String INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
           
           
           
           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
           	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

              
   
           Hashtable ht = new Hashtable();
           if(StrUtils.fString(JOBNO).length() > 0)        ht.put("JOBNUM",JOBNO);
           if(StrUtils.fString(ITEMNO).length() > 0)       ht.put("ITEM",ITEMNO);
           if(StrUtils.fString(ORDERNO).length() > 0)      ht.put("DONO",ORDERNO);
           //if(StrUtils.fString(CUSTNAME).length() > 0)     ht.put("A.CUSTNAME",strUtils.InsertQuotes(CUSTNAME));
           if(StrUtils.fString(ORDERTYPE).length() > 0)    ht.put("ORDERTYPE",ORDERTYPE);
           if(StrUtils.fString(ISSUESTATUS).length() > 0)  ht.put("STATUS",ISSUESTATUS);
           if(StrUtils.fString(PICKSTATUS).length() > 0)   ht.put("PickStaus",PICKSTATUS);
           if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("ITEMTYPE",PRD_TYPE_ID);
	        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
	        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("PRD_CLS_ID",PRD_CLS_ID);	        
	        if(StrUtils.fString(EMPNO).length() > 0) ht.put("EMPNO",EMPNO);
	        if(StrUtils.fString(INVOICENO).length() > 0) ht.put("INVOICENO",INVOICENO);
  
	       	movQryList = movHisUtil.getGoodsIssueWithPriceSummary(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTNAME,SORT);
	       
           
            if (movQryList.size() > 0) {
            int Index = 0;
//             int iIndex = 0,irow = 0;
             String  customerstatusid="",customerstatusdesc="",customertypeid="",customertypedesc="";;
//            double sumprdQty = 0;String lastProduct="";
//            
//            double totalIssPrice=0,totaltax=0,totIssPriceWTax=0,priceGrandTot=0,taxGrandTot=0,priceWTaxGrandTot=0,TotalIssPrice=0;//issPriceSubTot=0,OrdPriceGrandTot=0,taxGrandTotal=0,priceWTaxGrandTotal=0;
//            double issPriceGrandTot=0;
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                      
//                        String result="",custcode="";
                        Map lineArr = (Map) movQryList.get(iCnt);
                        
//                       	 custcode=(String)lineArr.get("custcode");
                      
                        String dono = (String)lineArr.get("dono");
                        Float gstpercentage =  Float.parseFloat(lineArr.get("Tax") == null ? "0.0" : ((String) lineArr.get("Tax").toString())) ;
            			            			
                       
          	                                    
//                        String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                        JSONObject resultJsonInt = new JSONObject();
                        
                        customerstatusid = customerBeanDAO.getCustomerStatusId(PLANT,(String)lineArr.get("custname"));
                        if(customerstatusid == null || customerstatusid.equals(""))
            			   {
            			   customerstatusdesc="";
            			   }
            			   else
            			   {
            			    customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(PLANT,customerstatusid);
            			   }
                        
                        customertypeid = customerBeanDAO.getCustomerTypeId(PLANT,(String)lineArr.get("custname"));
              			 if(customertypeid == null || customertypeid.equals(""))
              			 {
              				customertypedesc="";
              			 }
              			 else
              			 {
              				customertypedesc = customerBeanDAO.getCustomerTypeDesc(PLANT,customertypeid);
              			 }
              			 
              			String shippingcost =(String)lineArr.get("shippingcost");
               			String qtyPickValue =(String)lineArr.get("qty");
               			String qtyValue =(String)lineArr.get("qty");
               			String unitprice = (String)lineArr.get("rate");

               			
                        float shippingcostVal ="".equals(shippingcost) ? 0.0f :  Float.parseFloat(shippingcost);
                        float qtyPickVal ="".equals(qtyPickValue) ? 0.0f :  Float.parseFloat(qtyPickValue);
                        float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
                        float unitPriceVal="".equals(unitprice) ? 0.0f :  Float.parseFloat(unitprice);
                        
                        if(shippingcostVal==0f){
                        	shippingcost="0.00000";
                        }else{
                        	shippingcost=shippingcost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(qtyPickVal==0f){
                        	qtyPickValue="0.000";
                        }else{
                        	qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(qtyVal==0f){
                        	qtyValue="0.000";
                        }else{
                        	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }if(unitPriceVal==0f){
                        	unitprice="0.00000";
                        }else{
                        	unitprice=unitprice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
              			 
              			 
                      
              			
              			 Index = Index + 1;
                       	
                        resultJsonInt.put("Index",Index);
                        resultJsonInt.put("dono",(dono));
                        resultJsonInt.put("orderType", StrUtils.fString((String)lineArr.get("orderType")));
                        resultJsonInt.put("invoiceno", StrUtils.fString((String)lineArr.get("InvoiceNo")));
                        resultJsonInt.put("trandate", StrUtils.fString((String)lineArr.get("trandate")));
                        resultJsonInt.put("jobnum", StrUtils.fString((String)lineArr.get("jobnum")));
                        resultJsonInt.put("custname", StrUtils.fString((String)lineArr.get("custname")));
                        resultJsonInt.put("customertypedesc", StrUtils.fString(customertypedesc));
                        resultJsonInt.put("customerstatusdesc", StrUtils.fString(customerstatusdesc));
                        resultJsonInt.put("remarks", StrUtils.fString((String)lineArr.get("remarks")));
                        resultJsonInt.put("remarks2", StrUtils.fString((String)lineArr.get("remarks2")));
                        resultJsonInt.put("orderdiscount", StrUtils.fString((String)lineArr.get("orderdiscount")));
                        //resultJsonInt.put("shippingcost", shippingcost);
                        resultJsonInt.put("issuedate", StrUtils.fString((String)lineArr.get("issuedate")));
                        resultJsonInt.put("tax", gstpercentage);                        
                        resultJsonInt.put("qty",qtyValue );
                        resultJsonInt.put("empname", StrUtils.fString((String)lineArr.get("empname")));
                        resultJsonInt.put("unitprice", unitprice);
                        resultJsonInt.put("status_id", StrUtils.fString((String)lineArr.get("status_id")));
                        jsonArray.add(resultJsonInt);
              
                   

                }
               
                    resultJson.put("items", jsonArray);
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
                    resultJson.put("items", jsonArray);

                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        		jsonArray.add("");
        		resultJson.put("items", jsonArray);
                resultJson.put("SEARCH_DATA", "");
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	private JSONObject generateGINO(String plant, String userId) {
		JSONObject resultJson = new JSONObject();
		try {
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			_TblControlDAO.setmLogger(mLogger); 
			
			String invno = _TblControlDAO.getNextOrder(plant,userId,"GINO");

			if (invno.length() > 0) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("invno", invno);
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
	
	private JSONObject checkGINO(String plant, String gino) {
		JSONObject resultJson = new JSONObject();
		try {
			InvoiceDAO invoiceDAO = new InvoiceDAO();
			boolean gicheck = invoiceDAO.isExisitgino(plant, gino);
			if(gicheck) {
				/*boolean ginocheck = invoiceDAO.isGINOPaid(plant, gino);
				if(ginocheck) {
					resultJson.put("status", "100");
					JSONObject resultObjectJson = new JSONObject();
					resultObjectJson.put("invno", gino);
					resultJson.put("result", resultObjectJson);
				}else {
					resultJson.put("status", "99");
				}*/
				resultJson.put("status", "99");
			}else {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("invno", gino);
				resultJson.put("result", resultObjectJson);
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	private JSONObject generatePAKINGLIST_DELIVERYNOTE(String plant, String userId) {
		JSONObject resultJson = new JSONObject();
		try {
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			_TblControlDAO.setmLogger(mLogger); 
			
			String plno = _TblControlDAO.getNextOrder(plant,userId,"PAKING_LIST");
			String dnno = _TblControlDAO.getNextOrder(plant,userId,"DELIVERY_NOTE");

			if (plno.length() > 0) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("PLNO", plno);
				resultObjectJson.put("DNNO", dnno);
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
	
	//CREATED BY NAVAS FEB20
	private JSONObject getConsignmentRemarksDetails(HttpServletRequest request) {
//		StrUtils _StrUtils = null;
//		String action="";
		JSONObject resultJson = new JSONObject();
		  JSONArray jsonArray = new JSONArray();
		try {

//			action = StrUtils.fString(request.getParameter("Submit")).trim();
//			_StrUtils = new StrUtils();
			ToDetDAO _ToDetDAO = new ToDetDAO();
			String plant = StrUtils.fString(
					   (String) request.getSession().getAttribute("PLANT")).trim();

				String remarks1 = StrUtils.fString(request.getParameter("REMARKS1"));
				String remarks2 = StrUtils.fString(request.getParameter("REMARKS2"));
				String tono = StrUtils.fString(request.getParameter("TONO"));
				String item = StrUtils.fString(request.getParameter("ITEM"));
				String tolnno = StrUtils.fString(request.getParameter("TOLNNO"));
				List al = new ArrayList();
				List al1 = new ArrayList();

				if (tono.length() > 0) {

					String query = "REMARKS,TOLNNO";
					Hashtable ht = new Hashtable();
					ht.put(IDBConstants.TODET_TONUM, tono);
					ht.put(IDBConstants.TODET_ITEM, item);
					ht.put(IDBConstants.TODET_TOLNNO, tolnno);
					ht.put(IDBConstants.PLANT, plant);
					al = _ToDetDAO.selectRemarks(query, ht);
					
					String query1 = "Remark1,REMARK3";
					Hashtable ht1 = new Hashtable();
					ht1.put(IDBConstants.TODET_TONUM, tono);
					ht1.put(IDBConstants.PLANT, plant);
					al1 =new ToHdrDAO().selectToHdr(query1, ht1);
					if(al1.size()>0)
					{
						Map m = (Map) al1.get(0);
						remarks1=StrUtils.fString((String)m.get("Remark1"));
						remarks2=StrUtils.fString((String)m.get("REMARK3"));
					}
					
					if(al.size()>0) {
					for (int i = 0; i < al.size(); i++) {
						Map m = (Map) al.get(i);
	        			JSONObject resultJsonInt = new JSONObject();
	        			resultJsonInt.put("REMARKS", StrUtils.fString((String)m.get("REMARKS")));                 
	        			resultJsonInt.put("REMARK1", remarks1);                 
	        			resultJsonInt.put("REMARK2", remarks2);
	        			jsonArray.add(resultJsonInt);
					}
					}
					else {
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("REMARKS", "");                 
						resultJsonInt.put("REMARK1", remarks1);                 
						resultJsonInt.put("REMARK2", remarks2);                 
						jsonArray.add(resultJsonInt);
					}
					
   }	
				else {
	        		
	             	   JSONObject resultJsonInt = new JSONObject();
	                    jsonArray.add("");
	                    resultJson.put("footermaster", jsonArray);            
	        	}
	        	 resultJson.put("REMARKSMST", jsonArray);

   }
		catch (Exception he) {
			he.printStackTrace();
			System.out.println("Error in reterieving data");
		}
		return resultJson;
  }
	//END BY NAVAS
	private JSONObject getRemarksDetails(HttpServletRequest request) {
//		StrUtils _StrUtils = null;
//		String action="";
		JSONObject resultJson = new JSONObject();
		  JSONArray jsonArray = new JSONArray();
		try {

//			action = StrUtils.fString(request.getParameter("Submit")).trim();
//			_StrUtils = new StrUtils();
			DoDetDAO _DoDetDAO = new DoDetDAO();
			String plant = StrUtils.fString(
					   (String) request.getSession().getAttribute("PLANT")).trim();

				String remarks1 = StrUtils.fString(request.getParameter("REMARKS1"));
				String remarks2 = StrUtils.fString(request.getParameter("REMARKS2"));
				String dono = StrUtils.fString(request.getParameter("DONO"));
				String item = StrUtils.fString(request.getParameter("ITEM"));
				String dolnno = StrUtils.fString(request.getParameter("DOLNNO"));
				List al = new ArrayList();
				List al1 = new ArrayList();

				if (dono.length() > 0) {

					String query = "REMARKS,DOLNNO";
					Hashtable ht = new Hashtable();
					ht.put(IDBConstants.DODET_DONUM, dono);
					ht.put(IDBConstants.DODET_ITEM, item);
					ht.put(IDBConstants.DODET_DOLNNO, dolnno);
					ht.put(IDBConstants.PLANT, plant);
					al = _DoDetDAO.selectRemarks(query, ht);
					
					String query1 = "Remark1,REMARK3";
					Hashtable ht1 = new Hashtable();
					ht1.put(IDBConstants.DODET_DONUM, dono);
					ht1.put(IDBConstants.PLANT, plant);
					al1 =new DoHdrDAO().selectDoHdr(query1, ht1);
					if(al1.size()>0)
					{
						Map m = (Map) al1.get(0);
						remarks1=StrUtils.fString((String)m.get("Remark1"));
						remarks2=StrUtils.fString((String)m.get("REMARK3"));
					}
					
					if(al.size()>0) {
					for (int i = 0; i < al.size(); i++) {
						Map m = (Map) al.get(i);
	        			JSONObject resultJsonInt = new JSONObject();
	        			resultJsonInt.put("REMARKS", StrUtils.fString((String)m.get("REMARKS")));                 
	        			resultJsonInt.put("REMARK1", remarks1);                 
	        			resultJsonInt.put("REMARK2", remarks2);                 
	                    jsonArray.add(resultJsonInt);
					}
					}
					
					else {
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("REMARKS", "");                 
						resultJsonInt.put("REMARK1", remarks1);                 
						resultJsonInt.put("REMARK2", remarks2);                 
						jsonArray.add(resultJsonInt);
					}
					}
				
				else {
	        		
	             	   JSONObject resultJsonInt = new JSONObject();
	                    jsonArray.add("");
	                    resultJson.put("footermaster", jsonArray);            
	        	}
	        	 resultJson.put("REMARKSMST", jsonArray);

   }
		catch (Exception he) {
			he.printStackTrace();
			System.out.println("Error in reterieving data");
		}
		return resultJson;
  }
	private JSONObject getRemarksEstDetails(HttpServletRequest request) {
//		StrUtils _StrUtils = null;
//		String action="";
		JSONObject resultJson = new JSONObject();
		  JSONArray jsonArray = new JSONArray();
		try {

//			action = StrUtils.fString(request.getParameter("Submit")).trim();
//			_StrUtils = new StrUtils();
			EstDetDAO _EstDetDAO= new EstDetDAO();
			String plant = StrUtils.fString(
					   (String) request.getSession().getAttribute("PLANT")).trim();

				String remarks1 = StrUtils.fString(request.getParameter("REMARKS1"));
				String remarks2 = StrUtils.fString(request.getParameter("REMARKS3"));
				String estno = StrUtils.fString(request.getParameter("ESTNO"));
				String item = StrUtils.fString(request.getParameter("ITEM"));
				String estlno = StrUtils.fString(request.getParameter("ESTLNNO"));
				List al = new ArrayList();
				List al1 = new ArrayList();

				if (estno.length() > 0) {

					String query = "REMARKS,ESTLNNO";
					Hashtable ht = new Hashtable();
					ht.put(IDBConstants.ESTHDR_ESTNUM, estno);
					ht.put(IDBConstants.ITEM, item);
					ht.put(IDBConstants.ESTHDR_ESTLNNUM, estlno);
					ht.put(IDBConstants.PLANT, plant);
					al = _EstDetDAO.selectRemarks(query, ht);	
					
					String query1 = "Remark1,REMARK3";
					Hashtable ht1 = new Hashtable();
					ht1.put(IDBConstants.ESTHDR_ESTNUM, estno);
					ht1.put(IDBConstants.PLANT, plant);
					al1 =new EstHdrDAO().selectESTHDR(query1, ht1);
					if(al1.size()>0)
					{
						Map m = (Map) al1.get(0);
						remarks1=StrUtils.fString((String)m.get("Remark1"));
						remarks2=StrUtils.fString((String)m.get("REMARK3"));
					}
					
					if(al.size()>0) {
					for (int i = 0; i < al.size(); i++) {
						Map m = (Map) al.get(i);
	        			JSONObject resultJsonInt = new JSONObject();
	        			resultJsonInt.put("REMARKS", StrUtils.fString((String)m.get("REMARKS")));                 
	        			resultJsonInt.put("REMARK1", remarks1);                 
	        			resultJsonInt.put("REMARK2", remarks2);  
	        			jsonArray.add(resultJsonInt);
					}
					}
					else {
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("REMARKS", "");                 
						resultJsonInt.put("REMARK1", remarks1);                 
						resultJsonInt.put("REMARK2", remarks2);                 
						jsonArray.add(resultJsonInt);
					}
   }	
				else {
	        		
	             	   JSONObject resultJsonInt = new JSONObject();
	                    jsonArray.add("");
	                    resultJson.put("footermaster", jsonArray);            
	        	}
	        	 resultJson.put("REMARKSMST", jsonArray);

   }
		catch (Exception he) {
			he.printStackTrace();
			System.out.println("Error in reterieving data");
		}
		return resultJson;
  }
}
