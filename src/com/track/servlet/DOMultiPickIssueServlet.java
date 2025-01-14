package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.DoDetDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.DOUtil;
import com.track.db.util.InvMstUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;

import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.db.util.TempUtil;

public class DOMultiPickIssueServlet extends HttpServlet implements IMLogger {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.DOMultiPickIssueServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.DOMultiPickIssueServlet_PRINTPLANTMASTERINFO;
	String action = "";
	DateUtils dateutils = new DateUtils();	
	StrUtils strutils = new StrUtils();	
	DOUtil _DOUtil = null;
	TempUtil _TempUtil = null;
	ItemMstDAO _ItemMstDAO = null; 
	InvMstDAO _InvMstDAO = null;
	InvMstUtil _InvMstUtil = null;
	ShipHisDAO _ShipHisDAO = null;
	DoDetDAO _DoDetDAO = null;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);		
		_DOUtil = new DOUtil();	
		_TempUtil =  new TempUtil();
		_ItemMstDAO = new ItemMstDAO();
		_InvMstDAO = new InvMstDAO();
		_InvMstUtil = new InvMstUtil();
		_ShipHisDAO = new ShipHisDAO();
		_DoDetDAO = new DoDetDAO();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			action = request.getParameter("Submit").trim();
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
		
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			
		/*	if (action.equals("GET_DODET_DETAILS")) {
			    JSONObject jsonObjectResult = new JSONObject();
		        jsonObjectResult = this.getDODETDetails(request);
		        response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
		    }
		 if(action.equals("PROCESS_MULTI_PICK")){
			String msg   = processDOPick(request, response);
			 	 JSONObject jsonObjectResult = new JSONObject();
				 jsonObjectResult = this.getDODETDetails(request);
				 jsonObjectResult.put("errorMsg", msg);
				 response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			
			 
		 }
		 
		 if(action.equals("PROCESS_MULTI_ISSUE")){
				String msg   = processDOIssue(request, response);
				 	 JSONObject jsonObjectResult = new JSONObject();
					 jsonObjectResult = this.getDODETDetails(request);
					 jsonObjectResult.put("errorMsg", msg);
					 response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
			 }
		 if(action.equals("PICK_REVERSAL")){
			 processDOPickReversal(request,response);
			 
		 }
		 if(action.equals("GENERATE_PACKINGLIST_NUM")){
			 JSONObject jsonObjectResult = new JSONObject();
			 jsonObjectResult = this.autoGeneratePCKNum(request);
			 response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
			 
		 }*/
          
		 if (action.equals("GET_DODET_DETAILS_RANDOMISSUE")) {
				    JSONObject jsonObjectResult = new JSONObject();
			        jsonObjectResult = this.getDODETDetailsRandomIssue(request);
			        response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
			    }
			 if (action.equals("GET_DODET_DETAILS_DOCHECK")) {
				    JSONObject jsonObjectResult = new JSONObject();
			        jsonObjectResult = this.getDODETDetailsforoudboundcheck(request);
			        response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
			    }
			 if(action.equals("UPDATE_TEMP")){
					String msg   = processTEMP(request, response);
					 	 JSONObject jsonObjectResult = new JSONObject();
						// jsonObjectResult = this.getDODETDetails(request);
						 jsonObjectResult.put("errorMsg", msg);
						 jsonObjectResult.put("isOpenOrder", _DOUtil.isOpenOutBoundOrder(plant, StrUtils.fString(request.getParameter("DONO"))));
						 response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(jsonObjectResult.toString());
						response.getWriter().flush();
						response.getWriter().close();
					
					 
				 }
			 if(action.equals("PROCESS_MULTI_PICKISSUE")){
				String msg   = processDOPickIssue(request, response);
				 	 JSONObject jsonObjectResult = new JSONObject();
					 jsonObjectResult = this.getDODETDetailsRandomIssue(request);
					 jsonObjectResult.put("errorMsg", msg);
					 response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
			 }
			  if(action.equals("PICK_REVERSAL_RANDOMISSUE")){
				  processDOPickReversalRandomIssue(request,response);
				 
			 }
			  if(action.equals("RESET_SCANQTY")){
				  String msg   =  processResetScanQTY(request,response);
				 	 JSONObject jsonObjectResult = new JSONObject();
				 	jsonObjectResult = this.getDODETDetailsRandomIssue(request);
					 jsonObjectResult.put("errorMsg", msg);
					 response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
				 
			 } 
			  if (action.equals("GET_DODET_DETAILS_RANDOMPICK")) {
				    JSONObject jsonObjectResult = new JSONObject();
			        jsonObjectResult = this.getDODETDetailsRandomPick(request);
			        response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
			    }
			  if(action.equals("PROCESS_MULTI_PICK")){
					String msg   = processDOPick(request, response);
					 	 JSONObject jsonObjectResult = new JSONObject();
						 jsonObjectResult = this.getDODETDetailsRandomPick(request);
						 jsonObjectResult.put("errorMsg", msg);
						 response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(jsonObjectResult.toString());
						response.getWriter().flush();
						response.getWriter().close();
					
					 
				 }
			  if(action.equals("RESET_SCANQTY_PICKRANDOM")){
				  String msg   =  processResetScanQTY(request,response);
				 	 JSONObject jsonObjectResult = new JSONObject();
				 	jsonObjectResult = this.getDODETDetailsRandomPick(request);
					 jsonObjectResult.put("errorMsg", msg);
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
				.sendRedirect("jsp/DORandomPickIssue.jsp?result="
						+ result);

		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	

/*	private JSONObject getDODETDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       
     
        try {
        
               String plant= StrUtils.fString(request.getParameter("PLANT"));
               String dono     = StrUtils.fString(request.getParameter("DONO"));
                      
             ArrayList doDetList= _DOUtil.listDODETDetails(plant,dono);
            if (doDetList.size() > 0) {
            	       
                for (int iCnt =0; iCnt<doDetList.size(); iCnt++){
                            String result="";
                            Map lineArr = (Map) doDetList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String item = StrUtils.fString((String)lineArr.get("item")) ;
                            String qtyor = StrUtils.formatNum((String)lineArr.get("qtyor"));
                            String qtyPick = StrUtils.formatNum((String)lineArr.get("qtyPick"));
                            int qtyBal =  Integer.parseInt(StrUtils.removeFormat(qtyor)) - Integer.parseInt(StrUtils.removeFormat(qtyPick)) ;
                            String desc= StrUtils.fString((String)lineArr.get("itemDesc")) ;
                            String dolno = StrUtils.fString((String)lineArr.get("dolnno"));
                            	 result += "<tr valign=\"middle\" >" 
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + dolno + "</td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;<a href=\"#\" onClick=javascript:popUpWin('DOMultiPickReversal.jsp?ITEM="+item+"&DONO="+dono+"&DOLNO="+dolno+"')>"+item+"</a></td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + desc + "</td>"
                        + "<td  align = right>&nbsp;&nbsp;&nbsp; " + qtyor + "</td>"
                         +"<td align = right>&nbsp;&nbsp;&nbsp; "   + String.valueOf(qtyBal)      +  "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;" + qtyPick + "</td>"
                         + "<td align = right>&nbsp;&nbsp;&nbsp;"  + StrUtils.fString((String)lineArr.get("qtyis")) + "</td>"
                          + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("lnstat")) + "</td>"                         
                        + "</tr>" ;
                            	
                          resultJsonInt.put("PRODUCT", result);
                         
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
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	
	
	private String processDOPick(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String msg = "";
		String PLANT = "",ITEM = "",ITEMDESC  = "",BATCH = "", PCKLISTNUM = "",DONO = "",LOC = "" ,PICKING_QTY = "",ITEM_QTY = "",dolnno = "",userName = "";
		ArrayList alResult = new ArrayList();
		Map map = null;
		
		try {
            
			HttpSession session = request.getSession();
                        
                     
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			DONO = StrUtils.fString(request.getParameter("DONO"));
			PCKLISTNUM = StrUtils.fString(request.getParameter("PCKLIST"));
			ITEM = StrUtils.fString(request.getParameter("ITEM"));
			ITEMDESC = StrUtils.fString(request.getParameter("ITEMDESC"));
			BATCH = StrUtils.fString(request.getParameter("BATCH"));
			LOC = StrUtils.fString(request.getParameter("LOC_ID"));
			PICKING_QTY = StrUtils.fString(request.getParameter("QTY")); //Scanned QTY
			double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim()
					.toString()));
			pickingQty = StrUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
			
			map = new HashMap();
			map.put(IConstants.PLANT, PLANT);
			map.put(IConstants.ITEM, ITEM);
			map.put(IConstants.ITEM_DESC, ITEMDESC);
			map.put(IConstants.PODET_PONUM, DONO);
			map.put(IConstants.LOC, LOC);
			
			//validation 
			  
			// 1. INV against
			
			
			List listQry = _InvMstDAO.getOutBoundPickingBatchByWMS(PLANT,ITEM, LOC, BATCH);
			double invqty = 0;
			if (listQry.size() > 0) {
				for (int i = 0; i < listQry.size(); i++) {
					Map m = (Map) listQry.get(i);
					ITEM_QTY = (String) m.get("qty");
					invqty = Double.parseDouble(((String) ITEM_QTY.trim()));
				}
			} else {
				
				throw new Exception(
						"Error in picking OutBound Order : Inventory not found for the product/batch scanned at the location "+LOC);
			}
			// 2.check for item in location
					//	Hashtable htLocMst = new Hashtable();
					//	htLocMst.put("plant", map.get(IConstants.PLANT));
					//	htLocMst.put("item", map.get(IConstants.ITEM));
					//	htLocMst.put("loc", map.get(IConstants.LOC));
						 UserLocUtil uslocUtil = new UserLocUtil();
			             uslocUtil.setmLogger(mLogger);
			             boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,userName,LOC);
			             if(!isvalidlocforUser){
			                 throw new Exception(" Loc :"+LOC+" is not User Assigned Location");
			             }		
			
			Hashtable htCustName = new Hashtable();
			htCustName.put(IDBConstants.PLANT, PLANT);
			htCustName.put("DONO", DONO);
			
			map.put(IConstants.CUSTOMER_NAME, _DOUtil.getDohdrcol(htCustName, "CUSTNAME"));
			
	
			map.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ LOC);
			map.put(IConstants.LOGIN_USER, userName);
		//map.put(IConstants.CUSTOMER_CODE, _POUtil
				//	.getCustCode(PLANT, PO_NUM));
			map.put(IConstants.BATCH, BATCH);
			map.put(IConstants.INV_EXP_DATE, "REMARK1");

			// GET JOBNUM
			map.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,DONO));
			map.put("INV_QTY", "1");

			if(PCKLISTNUM.length() == 0){
				PCKLISTNUM = "NOPCKLIST";
			}
            
			map.put(IConstants.PCKLISTNUM, PCKLISTNUM);
			alResult = _DOUtil.getDODetDetails(PLANT, DONO, ITEM); 
			
			 if(alResult.size()>1){
           	  for (int i = 0; i < alResult.size(); i++) {           		  	
           		  Map mapLn = (Map) alResult.get(i);
           		    
           		  double totBal = Double.parseDouble((String)mapLn.get("totBal"));
           		double orderqty = Double.parseDouble((String)mapLn.get("qtyor"));			 
				double pickedqty = Double.parseDouble((String)mapLn.get("qtyPick"));	
				
           	    double balQty = orderqty - pickedqty;
           		  if(pickingQty > totBal){
           			throw new Exception(
    						"Exceeded the Ordered Qty.");
           		  }
           		dolnno =  StrUtils.fString((String)mapLn.get("dolnno")); 
           		map.put(IConstants.PODET_POLNNO, dolnno);
           		map.put(IConstants.ORD_QTY,Double.toString(orderqty));
           	      if ((pickingQty >=balQty) && (pickingQty >0)  ){   
           	    	map.put(IConstants.QTY, Double.toString((balQty)));
           	    	
           	    	 _DOUtil.process_DoPickingForPC(map);
           	    	 pickingQty = pickingQty-balQty;
           	    	
           	      }
           	    
           	     
           	     else if ((pickingQty >0) ){         	    	
           	     if (pickingQty >=balQty){

           	    	map.put(IConstants.QTY, Double.toString((balQty)));
           	    	

                 }else{
                   
                	 map.put(IConstants.QTY, Double.toString((pickingQty)));
                 }
           	 _DOUtil.process_DoPickingForPC(map); 
           	pickingQty = pickingQty - balQty;
              }
           	    
           	   
           	  }
             }
             else if(alResult.size() == 1){
            	 for (int i1 = 0; i1 < alResult.size(); i1++) {

                     Map mapLn = (Map) alResult.get(i1);
                    double totBal = Double.parseDouble((String)mapLn.get("totBal"));
          		  if(pickingQty > totBal){
          			throw new Exception(
   						"Exceeded the Ordered Qty.");
          		  }
          		double orderqty = Double.parseDouble((String)mapLn.get("qtyor"));			 
				dolnno =  StrUtils.fString((String)mapLn.get("dolnno")); 
				map.put(IConstants.PODET_POLNNO, dolnno);
           		map.put(IConstants.ORD_QTY, Double.toString(orderqty));
				map.put(IConstants.QTY, Double.toString((pickingQty)));				
          		  
           	   _DOUtil.process_DoPickingForPC(map);
            	 }
             }
             else {
           	  
           	  throw new Exception("Fully Picked for item : "+ITEM);

             }
			
		}
		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
		}
		return msg;
	}
	
	
	private String processDOIssue(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String msg = "";
		String PLANT = "",ITEM = "",ITEMDESC  = "",BATCH = "", DONO = "",LOC = "" ,PICKING_QTY = "",ITEM_QTY = "",dolnno = "",userName = "";
		ArrayList alResult = new ArrayList();
		Map map = null;
		Boolean flag = false;
		UserTransaction ut = null;
		
		try {
            
			HttpSession session = request.getSession();
                        
                     
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			DONO = StrUtils.fString(request.getParameter("DONO"));
			LOC = StrUtils.fString(request.getParameter("LOC_ID"));	
			
			map = new HashMap();
			

			Hashtable htDoDet = null;
			htDoDet = new Hashtable();
			String queryDoDet = " dono,item,DOLNNO,QTYOR,QtyPick,QTYIS";
			htDoDet.put("PLANT", PLANT);
			htDoDet.put("dono", DONO);
			
			alResult = _DOUtil.getDoDetDetails(queryDoDet, htDoDet, " LNSTAT <> 'C' and isNull(QtyPick,0) >  isNull(QTYIS,0) ");
			Boolean transactionHandler = true;
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			process: 	for (int i = 0; i < alResult.size(); i++) {           		  	
           		  Map mapLn = (Map) alResult.get(i);
           		double orderqty = Double.parseDouble((String)mapLn.get("QTYOR"));			 
				double pickedqty = Double.parseDouble((String)mapLn.get("QtyPick"));
				double issuedqty = Double.parseDouble((String)mapLn.get("QTYIS"));	
				String issuingQty = String.valueOf(pickedqty - issuedqty);
			     dolnno =  StrUtils.fString((String)mapLn.get("DOLNNO")); 
			     ITEM = StrUtils.fString((String)mapLn.get("item")); 
			     map.put(IConstants.PLANT, PLANT);
			     map.put(IConstants.ITEM, ITEM);
			     map.put(IConstants.QTY, issuingQty);
			     map.put(IConstants.DODET_DONUM, DONO);
			     map.put(IConstants.DODET_DOLNNO, dolnno);
			     map.put(IConstants.LOC, LOC);
			     map.put(IConstants.LOGIN_USER, userName);
			     map.put(IConstants.CUSTOMER_CODE, _DOUtil
							.getCustCode(PLANT, DONO));
			     map.put(IConstants.JOB_NUM, _DOUtil.getJobNum(
							PLANT, DONO));
			    // map.put("SHIPPINGNO", GenerateShippingNo(PLANT, userName));
			     map.put("SHIPPINGNO", "");
			     map.put(IConstants.REMARKS, "");
			     transactionHandler = _DOUtil.process_BulkIssueMaterial(map)&& true;
			     if(!transactionHandler) break process;
              }
           	    
			if (transactionHandler) {
				DbBean.CommitTran(ut);
				msg = "<font class='maingreen'>Issued Successfully</font>";
			}else {
				DbBean.RollbackTran(ut);
				throw new Exception("Unable to process..!");
			}  
          
			
		}
		catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
		}
		return msg;
	}
	
	private String processDOPickReversal(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String msg = "";
		String PLANT = "",ITEM = "",INV_QTY = "",DO_LN_NUM = "",ITEMDESC  = "",BATCH = "", DONO = "",LOC = "" ,PICKED_QTY = "",REVERSE_QTY = "",userName = "";
		String CUST_NAME = "" , orderQty = "";
		ArrayList alResult = new ArrayList();
		Boolean flag = false;
		ArrayList alDoDet = null;
		
		try {
            
			HttpSession session = request.getSession();
                        
                     
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			DONO = StrUtils.fString(request.getParameter("DONO"));
			LOC = StrUtils.fString(request.getParameter("LOC"));	
			DO_LN_NUM = StrUtils.fString(request.getParameter("DOLNO"));	
			ITEM = StrUtils.fString(request.getParameter("ITEM"));	
			REVERSE_QTY = StrUtils.fString(request.getParameter("reverseQty"));	
			BATCH = StrUtils.fString(request.getParameter("BATCH"));	
			INV_QTY="1";
			
			Hashtable htDoDet = new Hashtable();
			String queryDoDet = " qtyor,qtypick,userfld3 ";
			htDoDet.put("PLANT", PLANT);
			htDoDet.put("dono", DONO);
			htDoDet.put("dolnno", DO_LN_NUM);
			alDoDet = _InvMstUtil.getDoDetDetails(queryDoDet, htDoDet);
			if (alDoDet.size() > 0) {
				for (int i = 0; i < alDoDet.size(); i++) {
					Map map1 = (Map) alDoDet.get(i);
					PICKED_QTY = (String) map1.get("qtypick");
					orderQty = (String) map1.get("qtyor");
					CUST_NAME = (String) map1.get("userfld3");
							
				}
			}
			Hashtable htPickReversal = new Hashtable();
			htPickReversal.put(IDBConstants.PLANT, PLANT);
			htPickReversal.put("DONO", DONO);
			htPickReversal.put("DOLNO", DO_LN_NUM);
			htPickReversal.put("CNAME", strutils.InsertQuotes(CUST_NAME));
			htPickReversal.put(IDBConstants.ITEM, ITEM);
			htPickReversal.put(IDBConstants.ITEM_DESC,strutils.InsertQuotes(_ItemMstDAO.getItemDesc(PLANT ,ITEM)));
			htPickReversal.put("LOC", LOC);
			htPickReversal.put("BATCH", BATCH);
			htPickReversal.put("REMARKS", "");
			htPickReversal.put("ORDQTY", orderQty);
			htPickReversal.put("REVERSEQTY", REVERSE_QTY);
			htPickReversal.put("INV_QTY", INV_QTY);
			htPickReversal.put(IConstants.LOGIN_USER, userName);
			htPickReversal.put("PICKED_QTY", PICKED_QTY); 
			flag = _DOUtil.process_DoPickReversalForPC(htPickReversal);
			if (flag) {
				request.getSession().setAttribute("RESULT",
						"Reversed successfully!");
				response
						.sendRedirect("jsp/DOMultiPickReversal.jsp?DONO="+DONO+"&ITEM="+ITEM+"&DOLNO="+DO_LN_NUM);

			} else {
				request.getSession().setAttribute("RESULTERROR",
						"Error in reversing");
				response
				.sendRedirect("jsp/DOMultiPickReversal.jsp?DONO="+DONO+"&ITEM="+ITEM+"&DOLNO="+DO_LN_NUM);
			}
	
			
		}
		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("RESULTERROR",
					"Error in reversing");
			response
			.sendRedirect("jsp/DOMultiPickReversal.jsp?DONO="+DONO+"&ITEM="+ITEM+"&DOLNO="+DO_LN_NUM);
		}
		return msg;
	}
	
	private JSONObject autoGeneratePCKNum(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        String pckListNum = "";
       
     
        try {        
               String plant= StrUtils.fString(request.getParameter("PLANT"));
               String dono     = StrUtils.fString(request.getParameter("DONO"));
               pckListNum = _ShipHisDAO.getNextPckListNum(plant, dono);
               resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("PCKLISTNUM", pckListNum);
				resultJson.put("result", resultObjectJson);     
            
        } catch (Exception e) {
        	resultJson.put("status", "99");
        }
        return resultJson;
}*/
	
	private JSONObject getDODETDetailsRandomIssue(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       
     
        try {
        
               String plant= StrUtils.fString(request.getParameter("PLANT"));
               String dono     = StrUtils.fString(request.getParameter("DONO"));
               
               Boolean isOpenOrder = _DOUtil.isOpenOutBoundOrder(plant, dono);
               resultJson.put("isOpenOrder", isOpenOrder);
                      
             ArrayList doDetList= _DOUtil.listDODETDetailswithscanqty(plant,dono);
            if (doDetList.size() > 0) {
            	       
                for (int iCnt =0; iCnt<doDetList.size(); iCnt++){
                			int id=iCnt;
                            String result="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) doDetList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String item = StrUtils.fString((String)lineArr.get("item")) ;
                            String qtyor = StrUtils.formatNum((String)lineArr.get("qtyor"));
                            String qtyPick = StrUtils.formatNum((String)lineArr.get("qtypick"));
                            String qtyis = StrUtils.formatNum((String)lineArr.get("qtyis"));
                            //String desc= StrUtils.fString((String)lineArr.get("itemdesc")) ;
                            String desc= _ItemMstDAO.getItemDesc(plant ,item) ;
                            //String dolno = StrUtils.fString((String)lineArr.get("dolnno"));
                            String scannedqty = StrUtils.formatNum((String)lineArr.get("scannedqty"));
                            int scanqty=Integer.parseInt(StrUtils.removeFormat(scannedqty));
                            int qtyBal =  Integer.parseInt(StrUtils.removeFormat(qtyor)) - (Integer.parseInt(StrUtils.removeFormat(qtyis))+scanqty) ;
                            	 result += "<tr valign=\"middle\" bgcolor="+bgcolor+">"  
                        // + "<td  align = left>&nbsp;&nbsp;&nbsp;<a href=\"#\" onClick=javascript:popUpWin('DOMultiPickReversal.jsp?ITEM="+item+"&DONO="+dono+"&DOLNO="+dolno+"')>"+item+"</a></td>"
                        + "<td id='item_"+id+"' align = left>&nbsp;&nbsp;&nbsp;<a href=\"#\" onClick=javascript:popUpWin('DORandomPickReversal.jsp?ITEM="+item+"&DONO="+dono+"')>"+item+"</a></td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + desc + "</td>"
                        + "<td id='qtyor_"+id+"' align = right>&nbsp;&nbsp;&nbsp; " + qtyor + "</td>"
                         +"<td id='balqty_"+id+"' align = right>&nbsp;&nbsp;&nbsp; "   + String.valueOf(qtyBal)      +  "</td>"
                         + "<td id='qtyis_"+id+"' align = right>&nbsp;&nbsp;&nbsp;" + qtyis + "</td>"
                         + "<td id='scan_"+id+"' align = right>&nbsp;&nbsp;&nbsp;"  + scanqty + "</td>"
                         + "</tr>" ;
                            	
                          resultJsonInt.put("PRODUCT", result);
                         
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
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	//Start code added by Bruhan for outbound order check on 3 Apr 2013   
	private JSONObject getDODETDetailsforoudboundcheck(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       
     
        try {
        
               String plant= StrUtils.fString(request.getParameter("PLANT"));
               String dono     = StrUtils.fString(request.getParameter("DONO"));
                      
             ArrayList doDetList= _DOUtil.listDODETDetailsfordocheck(plant,dono);
            if (doDetList.size() > 0) {
            	       
                for (int iCnt =0; iCnt<doDetList.size(); iCnt++){
                            String result="";
                           int id=iCnt;
                           String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                          // int rowid=iCnt+1;
                            Map lineArr = (Map) doDetList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String item = StrUtils.fString((String)lineArr.get("item")) ;
                            String qtyor = StrUtils.formatNum((String)lineArr.get("qtyor"));
                            String qtyissue = StrUtils.formatNum((String)lineArr.get("qtyis"));
                            int qtyBal =  Integer.parseInt(StrUtils.removeFormat(qtyor)) - Integer.parseInt(StrUtils.removeFormat(qtyissue)) ;
                            String desc= StrUtils.fString((String)lineArr.get("itemdesc")) ;
                            String dolno = StrUtils.fString((String)lineArr.get("dolnno"));
                            int qtyPick=0;
                            	 result += "<tr valign=\"middle\">" 
                         + "<td id='item_"+id+"' align = left>&nbsp;&nbsp;&nbsp;"+item+"</td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + desc + "</td>"
                        + "<td id='qtyor_"+id+"' align = center>&nbsp;&nbsp;&nbsp; " + qtyor + "</td>"
                         +"<td id='balqty_"+id+"' align = center>&nbsp;&nbsp;&nbsp; "   + String.valueOf(qtyBal)      +  "</td>"
                         + "<td id='qtypick_"+id+"' align = center>&nbsp;&nbsp;&nbsp;" + qtyPick + "</td>"
                         + "</tr>" ;
                            	
                          resultJsonInt.put("PRODUCT", result);
                         
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
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	
	//End code added by Bruhan for outbound order check on 3 Apr 2013  
	
	//Start code added by Bruhan for outbound order pick issue random on 8 Apr 2013   
	private String processTEMP(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String PLANT = "",ITEM = "",ITEMDESC  = "",BATCH = "", PCKLISTNUM = "",DONO = "",LOC = "" ,PICKING_QTY = "",ITEM_QTY = "",dolnno = "",userName = "";
		ArrayList alResult = new ArrayList();
		Map map = null;
		
		try {
            
			HttpSession session = request.getSession();
                        
                     
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			
			//DONO = StrUtils.fString(request.getParameter("DONO"));
			//PCKLISTNUM = StrUtils.fString(request.getParameter("PCKLIST"));
			ITEM = StrUtils.fString(request.getParameter("ITEM"));
			ITEMDESC = StrUtils.fString(request.getParameter("ITEMDESC"));
			BATCH = StrUtils.fString(request.getParameter("BATCH"));
			LOC = StrUtils.fString(request.getParameter("LOC_ID"));
			DONO = StrUtils.fString(request.getParameter("DONO"));
			PICKING_QTY = StrUtils.fString(request.getParameter("QTY")); //Scanned QTY
			double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim()
					.toString()));
			pickingQty = StrUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
			
			Hashtable htproduct = new Hashtable();
			
			htproduct.put(IConstants.PLANT, PLANT);
			
			htproduct.put(IConstants.ITEM, ITEM);
			htproduct.put(IConstants.LOC, LOC);
			htproduct.put(IConstants.BATCH, BATCH);
			htproduct.put(IConstants.ORDERNO, DONO);
			
			Hashtable htproductinsert = new Hashtable();
			htproductinsert.put(IConstants.PLANT, PLANT);
			htproductinsert.put(IConstants.ORDERNO, DONO);
			htproductinsert.put(IConstants.ITEM, ITEM);
			htproductinsert.put(IConstants.LOC, LOC);
			htproductinsert.put(IConstants.BATCH, BATCH);
			htproductinsert.put(IConstants.QTY, PICKING_QTY);
			
			
			boolean flag = _TempUtil.isExitstempproduct(htproduct);
			
			if(flag){
				
				boolean updateflag = _TempUtil.updatetempproduct(pickingQty, htproduct);
			}
			else
			{
				htproductinsert.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT, ITEM));
				boolean insertflag = _TempUtil.savetemp(htproductinsert);
			}
			
			resultJson.put("status", "100");
			
						
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
			
		}
		return msg;
	}
	
	//End code added by Bruhan for oprocessDOPickIssueutbound order pick issue random on 8 Apr 2013
	/* ************Modification History*********************************
	   Oct 17 2014 Bruhan, Description: To include Transaction date
	*/
	private String processDOPickIssue(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String msg = "",SHIPPINGNO = "";
		String PLANT = "",ITEM = "",ITEMDESC  = "",BATCH = "", PCKLISTNUM = "",DONO = "",LOC = "" ,PICKING_QTY = "",ITEM_QTY = "",
				dolnno = "",userName = "",ISNONSTKFLG="",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
		ArrayList alResult = new ArrayList();
		Map map = null;
		boolean pickissueflag = false;
		try {
            
			HttpSession session = request.getSession();
            PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			DONO = StrUtils.fString(request.getParameter("DONO"));
			PCKLISTNUM = StrUtils.fString(request.getParameter("PCKLIST"));
		    TRANSACTIONDATE=StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			Hashtable htcond = new Hashtable();
			htcond.put(IConstants.PLANT, PLANT);
			htcond.put(IConstants.ORDERNO, DONO);
			String query = " ITEM,ITEMDESC,BATCH,LOC,QTY,(SELECT  isnull(NONSTKFLAG,'')  from ["+PLANT+"_ITEMMST] WHERE PLANT='"+PLANT+"' AND ITEM = a.ITEM  ) as NONSTKFLAG ";
			List listscnneddata = _TempUtil.listtempdata(query,htcond,"");
			if (listscnneddata.size() > 0) {
				for (int j = 0; j < listscnneddata.size(); j++) {
					Map mtemp = (Map) listscnneddata.get(j);
					ITEM = (String) mtemp.get("ITEM");
					ITEMDESC = (String) mtemp.get("ITEMDESC");
					ISNONSTKFLG = (String) mtemp.get("NONSTKFLAG");
					BATCH = (String) mtemp.get("BATCH");
					LOC = (String) mtemp.get("LOC");
					PICKING_QTY = (String) mtemp.get("QTY");
					double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim().toString()));
					pickingQty = StrUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
			map = new HashMap();
			map.put(IConstants.PLANT, PLANT);
			map.put(IConstants.ITEM, ITEM);
			//map.put(IConstants.ITEM_DESC, ITEMDESC);
			map.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,ITEM));
			map.put(IConstants.DODET_DONUM, DONO);
			map.put(IConstants.LOC, LOC);
			map.put(IConstants.TRAN_DATE, strMovHisTranDate);
			map.put(IConstants.ISSUEDATE, strTranDate);
		  if(!ISNONSTKFLG.equalsIgnoreCase("Y")){
			List listQry = _InvMstDAO.getOutBoundPickingBatchByWMS(PLANT,ITEM, LOC, BATCH);
			double invqty = 0;
			if (listQry.size() > 0) {
				for (int i = 0; i < listQry.size(); i++) {
					Map m = (Map) listQry.get(i);
					ITEM_QTY = (String) m.get("qty");
					invqty = Double.parseDouble(((String) ITEM_QTY.trim()));
					if(invqty < pickingQty){
						throw new Exception(
								"Error in picking OutBound Order : Inventory not found for the product: " +ITEM+ " with batch: " +BATCH+ "  scanned at the location  "+LOC);
						}
				}
			} else {
				
				throw new Exception(
						"Error in picking OutBound Order : Inventory not found for the product: " +ITEM+ " with batch: " +BATCH+ "  scanned at the location  "+LOC);
			}
				}
			// 2.check for item in location
						/*Hashtable htLocMst = new Hashtable();
						htLocMst.put("plant", map.get(IConstants.PLANT));
						htLocMst.put("item", map.get(IConstants.ITEM));
						htLocMst.put("loc", map.get(IConstants.LOC));*/
						 UserLocUtil uslocUtil = new UserLocUtil();
			             uslocUtil.setmLogger(mLogger);
			             boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,userName,LOC);
			             if(!isvalidlocforUser){
			                 throw new Exception(" Loc :"+LOC+" is not User Assigned Location");
			             }		
			
			Hashtable htCustName = new Hashtable();
			htCustName.put(IDBConstants.PLANT, PLANT);
			htCustName.put("DONO", DONO);
			
			map.put(IConstants.CUSTOMER_NAME, _DOUtil.getDohdrcol(htCustName, "CUSTNAME"));
			
			SHIPPINGNO = GenerateShippingNo(PLANT, userName);
			
			map.put("SHIPPINGNO", SHIPPINGNO);
			map.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ LOC);
			map.put(IConstants.LOGIN_USER, userName);
			map.put("TYPE", "OBRANDOM");
			map.put(IConstants.BATCH, BATCH);
			map.put(IConstants.INV_EXP_DATE, "REMARK1");
			map.put("NONSTKFLAG", ISNONSTKFLG);
			

			// GET JOBNUM
			map.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,DONO));
			map.put("INV_QTY", "1");

			if(PCKLISTNUM.length() == 0){
				PCKLISTNUM = "NOPCKLIST";
			}
            
			map.put(IConstants.PCKLISTNUM, PCKLISTNUM);
			alResult = _DOUtil.getDODetDetails(PLANT, DONO, ITEM); 
			
			 if(alResult.size()>1){
           	  for (int i = 0; i < alResult.size(); i++) {           		  	
           		  Map mapLn = (Map) alResult.get(i);
           		    
           		  double totBal = Double.parseDouble((String)mapLn.get("totBal"));
           		double orderqty = Double.parseDouble((String)mapLn.get("qtyor"));			 
				double pickedqty = Double.parseDouble((String)mapLn.get("qtyPick"));	
				
           	    double balQty = orderqty - pickedqty;
           		  if(pickingQty > totBal){
           			throw new Exception(
    						"Exceeded the Ordered Qty.");
           		  }
           		dolnno =  StrUtils.fString((String)mapLn.get("dolnno")); 
           		map.put(IConstants.DODET_DOLNNO, dolnno);
           		map.put(IConstants.ORD_QTY,Double.toString(orderqty));
           	      if ((pickingQty >=balQty) && (pickingQty >0)  ){   
           	    	map.put(IConstants.QTY, Double.toString((balQty)));
           	    	map.put(IConstants.PICKQTY, Double.toString((pickingQty-balQty)));
           	    	
           	     pickissueflag = _DOUtil.process_DoPickIssueForPC(map);
           	    	 pickingQty = pickingQty-balQty;
           	    	
           	      }
           	    
           	     
           	     else if ((pickingQty >0) ){         	    	
           	     if (pickingQty >=balQty){

           	    	map.put(IConstants.QTY, Double.toString((balQty)));
           	    	map.put(IConstants.PICKQTY, Double.toString((pickingQty-balQty)));
           	    	

                 }else{
                   
                	 map.put(IConstants.QTY, Double.toString((pickingQty)));
                	 map.put(IConstants.PICKQTY, Double.toString((pickingQty-balQty)));
                 }
           	  pickissueflag = _DOUtil.process_DoPickIssueForPC(map); 
           	  pickingQty = pickingQty - balQty;
              }
           	    
           	   
           	  }
             }
             else if(alResult.size() == 1){
            	 for (int i1 = 0; i1 < alResult.size(); i1++) {

                     Map mapLn = (Map) alResult.get(i1);
                    double totBal = Double.parseDouble((String)mapLn.get("totBal"));
          		  if(pickingQty > totBal){
          			throw new Exception(
   						"Exceeded the Ordered Qty.");
          		  }
          		double orderqty = Double.parseDouble((String)mapLn.get("qtyor"));			 
				dolnno =  StrUtils.fString((String)mapLn.get("dolnno")); 
				map.put(IConstants.DODET_DOLNNO, dolnno);
           		map.put(IConstants.ORD_QTY, Double.toString(orderqty));
				map.put(IConstants.QTY, Double.toString((pickingQty)));	
				map.put(IConstants.PICKQTY, Double.toString((-1)));
				pickissueflag =  _DOUtil.process_DoPickIssueForPC(map);
            	 }
             }
             else {
           	  
           	  throw new Exception("Fully Picked for item : "+ITEM);

             }
				
		}
				
		}
			/*if (pickissueflag) {
				
				//DbBean.CommitTran(ut);	
				pickissueflag = true;
				
			} else {
				//DbBean.RollbackTran(ut);
				pickissueflag = false;
				
					}*/
			
		if(pickissueflag) 
			{
			//_TempUtil.deletetemp(htcond);
				msg = "<font class='maingreen'>Products Issued Successfully</font>";
			}
		}
		catch (Exception e) {
			//DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
		}
		return msg;
	}
	
	
	private String GenerateShippingNo(String plant, String loginuser) {

		String PLANT = "";
		boolean flag = false;
		boolean updateFlag = false;
		boolean insertFlag = false;
		boolean resultflag = false;
		String sBatchSeq = "";
		String extCond = "";
		String rtnShippNo = "";

		try {

			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();

			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TBL_FUNCTION,IDBConstants.TBL_SHIPPING_CAPTION);

			boolean exitFlag = false;
			exitFlag = _TblControlDAO.isExisit(ht, extCond, plant);

			if (exitFlag == false) {

				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();

				htTblCntInsert.put(IDBConstants.PLANT, (String) plant);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
						(String) IDBConstants.TBL_SHIPPING_CAPTION);
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "Shipping");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, (String) loginuser);
				htTblCntInsert.put(IDBConstants.CREATED_AT,
						(String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert,
						plant);

				MovHisDAO mdao = new MovHisDAO(plant);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) plant);
				htm.put("DIRTYPE", "GENERATE_SHIPPING");
				htm.put("RECID", "");
				htm.put("CRBY", (String) loginuser);
				htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htm.put("CRAT", (String) new DateUtils().getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);
				if (insertFlag) {
					resultflag = true;
				} else if (!insertFlag) {

					throw new Exception(
							"Generate Shipping Failed, Error In Inserting Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
					resultflag = true;
				} else if (!inserted) {

					throw new Exception(
							"Generate Shipping Failed,  Error In Inserting Table:"
									+ " " + PLANT + "_ " + "MOVHIS");
				}

			} else {

				Map m = _TblControlDAO.selectRow(query, ht, extCond);
				sBatchSeq = (String) m.get("NXTSEQ");

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim()
						.toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				rtnShippNo = plant + updatedSeq;

				Map htUpdate = null;

				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
						IDBConstants.TBL_SHIPPING_CAPTION);
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "Shipping");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");

				updateFlag = _TblControlDAO.update(updateQyery.toString(),
						htTblCntUpdate, extCond, plant);

				MovHisDAO mdao = new MovHisDAO(plant);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) plant);
				htm.put("DIRTYPE", "UPDATE_SHIPPING");
				htm.put("RECID", "");
				htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htm.put("CRBY", (String) loginuser);
				htm.put("CRAT", (String) new DateUtils().getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);

				if (updateFlag) {
					resultflag = true;
				} else if (!updateFlag) {

					throw new Exception(
							"Update Shippoing Failed, Error In Updating Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
					resultflag = true;
				}

				else if (!inserted) {

					throw new Exception(
							"Update Shipping Failed,  Error In Inserting Table:"
									+ " " + PLANT + "_ " + "MOVHIS");
				}

			}
		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return rtnShippNo;
	}

	private String processDOPickReversalRandomIssue(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String msg = "";
		String PLANT = "",ITEM = "",INV_QTY = "",DO_LN_NUM = "",ITEMDESC  = "",BATCH = "", DONO = "",LOC = "" ,PICKED_QTY = "",REVERSE_QTY = "",userName = "";
		String CUST_NAME = "" , orderQty = "";
		ArrayList alResult = new ArrayList();
		Boolean flag = false;
		ArrayList alDoDet = null;
		
		try {
            
			HttpSession session = request.getSession();
                        
                     
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			DONO = StrUtils.fString(request.getParameter("DONO"));
			LOC = StrUtils.fString(request.getParameter("LOC"));	
			//DO_LN_NUM = StrUtils.fString(request.getParameter("DOLNO"));	
			ITEM = StrUtils.fString(request.getParameter("ITEM"));	
			REVERSE_QTY = StrUtils.fString(request.getParameter("reverseQty"));	
			BATCH = StrUtils.fString(request.getParameter("BATCH"));	
			//INV_QTY="1";
			
			Hashtable htdeletetemp = new Hashtable();
			htdeletetemp.put("PLANT", PLANT);
			htdeletetemp.put("ORDERNO", DONO);
			htdeletetemp.put("ITEM", ITEM);
			htdeletetemp.put("LOC", LOC);
			htdeletetemp.put("BATCH", BATCH);
			
			flag = _TempUtil.deletetemp(htdeletetemp);
			
				if (flag) {
				request.getSession().setAttribute("RESULT",
						"Reversed successfully!");
				response
						.sendRedirect("jsp/DORandomPickReversal.jsp?DONO="+DONO+"&ITEM="+ITEM+"&action=result");

			} else {
				request.getSession().setAttribute("RESULTERROR",
						"Error in reversing");
				response
				.sendRedirect("jsp/DORandomPickReversal.jsp?DONO="+DONO+"&ITEM="+ITEM+"&action=resulterror");
			}
	
			
		}
		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("RESULTERROR",
					"Error in reversing");
			response
			.sendRedirect("jsp/DORandomPickReversal.jsp?DONO="+DONO+"&ITEM="+ITEM+"&action=resulterror");
		}
		return msg;
	}
	
	private String processResetScanQTY(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String PLANT = "",ITEM = "",DONO = "",LOC = "" ,userName = "";
		ArrayList alResult = new ArrayList();
		Boolean flag = false;
		ArrayList alDoDet = null;
		
		try {
            
			HttpSession session = request.getSession();
                        
                     
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			DONO = StrUtils.fString(request.getParameter("DONO"));
			LOC = StrUtils.fString(request.getParameter("LOC_ID"));	
			//DO_LN_NUM = StrUtils.fString(request.getParameter("DOLNO"));	
			ITEM = StrUtils.fString(request.getParameter("ITEM"));	
			
			Hashtable htdeletetemp = new Hashtable();
			htdeletetemp.put("PLANT", PLANT);
			htdeletetemp.put("ORDERNO", DONO);
			//htdeletetemp.put("LOC", LOC);
						
			flag = _TempUtil.deletetemp(htdeletetemp);
			
			if(flag){			
			msg = "<font class='maingreen'>Scan Qty Reset Successfully</font>";
			}
			
		}
		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
			
		}	
		return msg;
	}
	private JSONObject getDODETDetailsRandomPick(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       
     
        try {
        
               String plant= StrUtils.fString(request.getParameter("PLANT"));
               String dono     = StrUtils.fString(request.getParameter("DONO"));
               
               Boolean isOpenOrder = _DOUtil.isOpenOutBoundOrder(plant, dono);
               resultJson.put("isOpenOrder", isOpenOrder);
                      
             ArrayList doDetList= _DOUtil.listDODETDetailswithscanqty(plant,dono);
            if (doDetList.size() > 0) {
            	       
                for (int iCnt =0; iCnt<doDetList.size(); iCnt++){
                			int id=iCnt;
                            String result="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) doDetList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String item = StrUtils.fString((String)lineArr.get("item")) ;
                            String qtyor = StrUtils.formatNum((String)lineArr.get("qtyor"));
                            String qtyPick = StrUtils.formatNum((String)lineArr.get("qtypick"));
                            String qtyis = StrUtils.formatNum((String)lineArr.get("qtyis"));
                            //String desc= StrUtils.fString((String)lineArr.get("itemdesc")) ;
                            String desc= _ItemMstDAO.getItemDesc(plant ,item) ;
                            //String dolno = StrUtils.fString((String)lineArr.get("dolnno"));
                            String scannedqty = StrUtils.formatNum((String)lineArr.get("scannedqty"));
                            int scanqty=Integer.parseInt(StrUtils.removeFormat(scannedqty));
                            int qtyBal =  Integer.parseInt(StrUtils.removeFormat(qtyor)) - (Integer.parseInt(StrUtils.removeFormat(qtyPick))+scanqty) ;
                            	 result += "<tr valign=\"middle\" bgcolor="+bgcolor+">"  
                        // + "<td  align = left>&nbsp;&nbsp;&nbsp;<a href=\"#\" onClick=javascript:popUpWin('DOMultiPickReversal.jsp?ITEM="+item+"&DONO="+dono+"&DOLNO="+dolno+"')>"+item+"</a></td>"
                        + "<td id='item_"+id+"' align = left>&nbsp;&nbsp;&nbsp;<a href=\"#\" onClick=javascript:popUpWin('DORandomPickReversal.jsp?ITEM="+item+"&DONO="+dono+"')>"+item+"</a></td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + desc + "</td>"
                        + "<td id='qtyor_"+id+"' align = right>&nbsp;&nbsp;&nbsp; " + qtyor + "</td>"
                         +"<td id='balqty_"+id+"' align = right>&nbsp;&nbsp;&nbsp; "   + String.valueOf(qtyBal)      +  "</td>"
                         + "<td id='qtyPick_"+id+"' align = right>&nbsp;&nbsp;&nbsp;" + qtyPick + "</td>"
                         + "<td id='scan_"+id+"' align = right>&nbsp;&nbsp;&nbsp;"  + scanqty + "</td>"
                         + "</tr>" ;
                            	
                          resultJsonInt.put("PRODUCT", result);
                         
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
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	private String processDOPick(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String msg = "",SHIPPINGNO = "";
		String PLANT = "",ITEM = "",ITEMDESC  = "",BATCH = "", PCKLISTNUM = "",DONO = "",LOC = "" ,PICKING_QTY = "",ITEM_QTY = "",
				dolnno = "",userName = "",ISNONSTKFLG="",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
		ArrayList alResult = new ArrayList();
		Map map = null;
		boolean pickissueflag = false;
		try {
            
			HttpSession session = request.getSession();
            PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			DONO = StrUtils.fString(request.getParameter("DONO"));
			PCKLISTNUM = StrUtils.fString(request.getParameter("PCKLIST"));
		    TRANSACTIONDATE=StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			Hashtable htcond = new Hashtable();
			htcond.put(IConstants.PLANT, PLANT);
			htcond.put(IConstants.ORDERNO, DONO);
			String query = " ITEM,ITEMDESC,BATCH,LOC,QTY,(SELECT  isnull(NONSTKFLAG,'')  from ["+PLANT+"_ITEMMST] WHERE PLANT='"+PLANT+"' AND ITEM = a.ITEM  ) as NONSTKFLAG ";
			List listscnneddata = _TempUtil.listtempdata(query,htcond,"");
			if (listscnneddata.size() > 0) {
				for (int j = 0; j < listscnneddata.size(); j++) {
					Map mtemp = (Map) listscnneddata.get(j);
					ITEM = (String) mtemp.get("ITEM");
					ITEMDESC = (String) mtemp.get("ITEMDESC");
					ISNONSTKFLG = (String) mtemp.get("NONSTKFLAG");
					BATCH = (String) mtemp.get("BATCH");
					LOC = (String) mtemp.get("LOC");
					PICKING_QTY = (String) mtemp.get("QTY");
					double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim().toString()));
					pickingQty = StrUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
			map = new HashMap();
			map.put(IConstants.PLANT, PLANT);
			map.put(IConstants.ITEM, ITEM);
			map.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,ITEM));
			map.put(IConstants.PODET_PONUM, DONO);
			map.put(IConstants.LOC, LOC);
			map.put(IConstants.TRAN_DATE, strMovHisTranDate);
			map.put(IConstants.ISSUEDATE, strTranDate);
		  if(!ISNONSTKFLG.equalsIgnoreCase("Y")){
			List listQry = _InvMstDAO.getOutBoundPickingBatchByWMS(PLANT,ITEM, LOC, BATCH);
			double invqty = 0;
			if (listQry.size() > 0) {
				for (int i = 0; i < listQry.size(); i++) {
					Map m = (Map) listQry.get(i);
					ITEM_QTY = (String) m.get("qty");
					invqty = Double.parseDouble(((String) ITEM_QTY.trim()));
					if(invqty < pickingQty){
						throw new Exception(
								"Error in picking OutBound Order : Inventory not found for the product: " +ITEM+ " with batch: " +BATCH+ "  scanned at the location  "+LOC);
						}
				}
			} else {
				
				throw new Exception(
						"Error in picking OutBound Order : Inventory not found for the product: " +ITEM+ " with batch: " +BATCH+ "  scanned at the location  "+LOC);
			}
				}
			
						 UserLocUtil uslocUtil = new UserLocUtil();
			             uslocUtil.setmLogger(mLogger);
			             boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,userName,LOC);
			             if(!isvalidlocforUser){
			                 throw new Exception(" Loc :"+LOC+" is not User Assigned Location");
			             }		
			
			Hashtable htCustName = new Hashtable();
			htCustName.put(IDBConstants.PLANT, PLANT);
			htCustName.put("DONO", DONO);
			
			map.put(IConstants.CUSTOMER_NAME, _DOUtil.getDohdrcol(htCustName, "CUSTNAME"));
			
			SHIPPINGNO = GenerateShippingNo(PLANT, userName);
			
			map.put("SHIPPINGNO", SHIPPINGNO);
			map.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ LOC);
			map.put(IConstants.LOGIN_USER, userName);
			map.put("TYPE", "OBRANDOM");
			map.put(IConstants.BATCH, BATCH);
			map.put(IConstants.INV_EXP_DATE, "REMARK1");
			map.put("NONSTKFLAG", ISNONSTKFLG);
			

			// GET JOBNUM
			map.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,DONO));
			map.put("INV_QTY", "1");

			if(PCKLISTNUM.length() == 0){
				PCKLISTNUM = "NOPCKLIST";
			}
            
			map.put(IConstants.PCKLISTNUM, PCKLISTNUM);
			alResult = _DOUtil.getDODetDetails(PLANT, DONO, ITEM); 
			
			 if(alResult.size()>1){
           	  for (int i = 0; i < alResult.size(); i++) {           		  	
           		  Map mapLn = (Map) alResult.get(i);
           		    
           		  double totBal = Double.parseDouble((String)mapLn.get("totBal"));
           		double orderqty = Double.parseDouble((String)mapLn.get("qtyor"));			 
				double pickedqty = Double.parseDouble((String)mapLn.get("qtyPick"));	
				
           	    double balQty = orderqty - pickedqty;
           		  if(pickingQty > totBal){
           			throw new Exception(
    						"Exceeded the Ordered Qty.");
           		  }
           		dolnno =  StrUtils.fString((String)mapLn.get("dolnno")); 
           		map.put(IConstants.PODET_POLNNO, dolnno);
           		map.put(IConstants.ORD_QTY,Double.toString(orderqty));
           	      if ((pickingQty >=balQty) && (pickingQty >0)  ){   
           	    	map.put(IConstants.QTY, Double.toString((balQty)));
           	    	map.put(IConstants.PICKQTY, Double.toString((pickingQty-balQty)));
           	    	
           	     pickissueflag = _DOUtil.process_DoPickingForPC(map);
           	    	 pickingQty = pickingQty-balQty;
           	    	
           	      }
           	    
           	     
           	     else if ((pickingQty >0) ){         	    	
           	     if (pickingQty >=balQty){

           	    	map.put(IConstants.QTY, Double.toString((balQty)));
           	    	map.put(IConstants.PICKQTY, Double.toString((pickingQty-balQty)));
           	    	

                 }else{
                   
                	 map.put(IConstants.QTY, Double.toString((pickingQty)));
                	 map.put(IConstants.PICKQTY, Double.toString((pickingQty-balQty)));
                 }
           	  pickissueflag = _DOUtil.process_DoPickingForPC(map); 
           	  pickingQty = pickingQty - balQty;
              }
           	    
           	   
           	  }
             }
             else if(alResult.size() == 1){
            	 for (int i1 = 0; i1 < alResult.size(); i1++) {

                     Map mapLn = (Map) alResult.get(i1);
                    double totBal = Double.parseDouble((String)mapLn.get("totBal"));
          		  if(pickingQty > totBal){
          			throw new Exception(
   						"Exceeded the Ordered Qty.");
          		  }
          		double orderqty = Double.parseDouble((String)mapLn.get("qtyor"));			 
				dolnno =  StrUtils.fString((String)mapLn.get("dolnno")); 
				map.put(IConstants.PODET_POLNNO, dolnno);
           		map.put(IConstants.ORD_QTY, Double.toString(orderqty));
				map.put(IConstants.QTY, Double.toString((pickingQty)));	
				map.put(IConstants.PICKQTY, Double.toString((-1)));
				pickissueflag =  _DOUtil.process_DoPickingForPC(map);
            	 }
             }
             else {
           	  
           	  throw new Exception("Fully Picked for item : "+ITEM);

             }
				
		}
				
		}
			
			
		if(pickissueflag) 
			{
			
				msg = "<font class='maingreen'>Products Picked Successfully</font>";
			}
		}
		catch (Exception e) {
			
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
		}
		return msg;
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
}