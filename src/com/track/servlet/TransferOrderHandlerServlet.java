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

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.db.util.TOUtil;
import com.track.db.util.TempUtil;
import com.track.db.util.UserLocUtil;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class TransferOrderHandlerServlet
 */
public class TransferOrderHandlerServlet extends HttpServlet implements
		IMLogger {
	private boolean printLog = MLoggerConstant.TransferOrderHandlerServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.TransferOrderHandlerServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 1L;
	
	
	TempUtil _TempUtil = null;
	InvMstDAO _InvMstDAO = null;
	TOUtil _TOUtil = null;
	ItemMstDAO _ItemMstDAO = null;
	ToHdrDAO _ToHdrDAO = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TransferOrderHandlerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);		
		
		_TempUtil =  new TempUtil();
		_InvMstDAO = new InvMstDAO();
		_TOUtil = new TOUtil();
		_ItemMstDAO = new ItemMstDAO();
		_ToHdrDAO = new ToHdrDAO();
		
	}
	/*  Bruhan   change code 001:modified fromloc,toloc getting from tohdr instead of getting from temp in TOrandom pick issue
	*/	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	  try {
		String action = StrUtils.fString(request.getParameter("ACTION")).trim();
		String plant = StrUtils.fString(
				(String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString(
				(String) request.getSession().getAttribute("LOGIN_USER"))
				.trim();
		this.setMapDataToLogger(this.populateMapData(plant, userName));
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		JSONObject jsonObjectResult = new JSONObject();
		if (action.equals("LOAD_TRANSER_ORDER_DETAILS")) {
			String transferOrderNo = StrUtils.fString(
					request.getParameter("ORDER_NO")).trim();
			jsonObjectResult = this.transferOrderValidation(plant,
					transferOrderNo);

		}
		if (action.equals("VALIDATE_BATCH_DETAILS")) {
			jsonObjectResult = this.validateBatch(request);
		}
		if (action.equals("VALIDATE_BATCH_DETAILS_FOR_PICKING")) {
			jsonObjectResult = this.validateBatchForPicking(request);
		}
		
		//Start code by Bruhan for Transfer order random on 7May2013
		if (action.equals("GET_TODET_DETAILS_RANDOMISSUE")) {
			
		    jsonObjectResult = this.getTODETDetailsRandomIssue(request);
		    
	       }
		
		if (action.equals("VALIDATE_PRODUCT_AGAINST_PICKLIST")) {
			String item = StrUtils.fString(request.getParameter("ITEM")).trim();
			String tono = StrUtils.fString(request.getParameter("TONO")).trim();
			jsonObjectResult = this.validateProductAgainstPickList(plant, item,tono);

		}
		
		if(action.equals("UPDATE_TEMP")){
			    String msg   = processTEMP(request, response);
			 	jsonObjectResult.put("errorMsg", msg);
			 	jsonObjectResult.put("isOpenOrder", _TOUtil.isOpenTransferOrder(plant, StrUtils.fString(request.getParameter("TONO"))));
			 	
				
			 
		 }
		
		if(action.equals("PROCESS_MULTI_PICKISSUE")){
			String msg   = processTOPickIssue(request, response);
			 	 jsonObjectResult = this.getTODETDetailsRandomIssue(request);
				 jsonObjectResult.put("errorMsg", msg);
				 
			 
		 }
		
		if(action.equals("RESET_SCANQTY")){
			  String msg   =  processResetScanQTY(request,response);
			 	 jsonObjectResult = this.getTODETDetailsRandomIssue(request);
				 jsonObjectResult.put("errorMsg", msg);
				 			 
		 } 
		
		if(action.equals("PICK_REVERSAL_RANDOMISSUE")){
			  processTOPickReversalRandomIssue(request,response);
			 
		 }
		
		if(action.equals("PROCESS_MULTI_PICKRECEIVE")){
			String msg   = processTOPickReceive_Random(request, response);
			 	 jsonObjectResult = this.getTODETDetailsRandomIssue(request);
				 jsonObjectResult.put("errorMsg", msg);
				 
			 
		 }
		
		
		//End code by Bruhan for Transfer order random on 7May2013
		this.mLogger.auditInfo(this.printInfo, "[JSON OUTPUT] "
				+ jsonObjectResult);
		response.setContentType("application/json");
		//((ServletRequest) response).setCharacterEncoding("UTF-8");
		 response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObjectResult.toString());
		response.getWriter().flush();
		response.getWriter().close();
		
	  } catch (Exception ex) {
			this.mLogger.exception(this.printLog, "", ex);
			String result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Exception : " + ex.getMessage() + "</font>";			
				response
				.sendRedirect("jsp/DORandomPickIssue.jsp?result="
						+ result);

		}
	}

	@SuppressWarnings("unchecked")
	private JSONObject validateBatchForPicking(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String itemNo = StrUtils.fString(request.getParameter("ITEMNO"));
			String batch = StrUtils.fString(request.getParameter("BATCH"));
			String location = StrUtils.fString(request.getParameter("LOCATION"));
			JSONObject resultJsonObject = new JSONObject();
			List resultList = invMstDAO.getTotalQuantityForOutBoundPickingBatchByWMS(plant,itemNo, location, batch);
			if (resultList.size() > 0) {
				Map m = (Map) resultList.get(0);
				String sBatch = (String) m.get("batch");
				String qty = (String) m.get("qty");
				resultJsonObject.put("BATCH", sBatch);
				resultJsonObject.put("QTY", qty);
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

	@SuppressWarnings("unchecked")
	private JSONObject validateBatch(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			ToDetDAO toDetDAO = new ToDetDAO();
			toDetDAO.setmLogger(mLogger);

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String ordNo = StrUtils.fString(request.getParameter("ORDER_NO"));
			String ordLineNo = StrUtils.fString(request.getParameter("ORDER_LNNO"));
			String itemNo = StrUtils.fString(request.getParameter("ITEMNO"));
			String batch = StrUtils.fString(request.getParameter("BATCH"));

			JSONObject resultJsonObject = new JSONObject();
			List resultList = toDetDAO.getToOrderBatchDetails(plant, ordNo,
					ordLineNo, itemNo, batch);
			if (resultList.size() > 0) {
				Map m = (Map) resultList.get(0);
				String sBatch = (String) m.get("batch");
				String spickQty = (String) m.get("pickQty");
				String srecvQty = (String) m.get("recQty");
				double availQty = Double.parseDouble(spickQty)
						- Double.parseDouble(srecvQty);
				availQty = StrUtils.RoundDB(availQty,IConstants.DECIMALPTS);
				if (availQty > 0) {
					resultJsonObject.put("BATCH", sBatch);
					resultJsonObject.put("PICK_QTY", spickQty);
					resultJsonObject.put("RECEIVED_QTY", srecvQty);
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

	@SuppressWarnings("unchecked")
	private JSONObject transferOrderValidation(String plant,
			String transferOrderNo) {
		JSONObject resultJson = new JSONObject();
		try {
			JSONObject resultJsonObject = new JSONObject();
			ToHdrDAO toHdrDAO = new ToHdrDAO();
			toHdrDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.TOHDR_TONUM, transferOrderNo);
			Map resultMap = toHdrDAO.selectRow("custName,status", ht);
			if (resultMap.size() > 0) {

				resultJsonObject.put("CUSTNAME", resultMap.get("custName"));
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
	//Start code by Bruhan for Transfer order random on 8May2013
	private JSONObject getTODETDetailsRandomIssue(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ToDetDAO _todetDAO = new ToDetDAO();
       
     
        try {
        
               String plant= StrUtils.fString(request.getParameter("PLANT"));
               String tono     = StrUtils.fString(request.getParameter("TONO"));
               
               Boolean isOpenOrder = _TOUtil.isOpenTransferOrder(plant, tono);
               resultJson.put("isOpenOrder", isOpenOrder);
                      
             ArrayList doDetList= _todetDAO.listTODETDetailswithscanqty(plant,tono);
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
                            String desc= StrUtils.fString((String)lineArr.get("itemdesc")) ;
                            String scannedqty = StrUtils.formatNum((String)lineArr.get("scannedqty"));
                            int scanqty=Integer.parseInt(StrUtils.removeFormat(scannedqty));
                            int qtyBal =  Integer.parseInt(StrUtils.removeFormat(qtyor)) - (Integer.parseInt(StrUtils.removeFormat(qtyPick))+scanqty) ;
                            String Loc= StrUtils.fString((String)lineArr.get("fromloc"))+","+StrUtils.fString((String)lineArr.get("toloc")) ;
                            
                            result += "<tr valign=\"middle\" bgcolor="+bgcolor+">"  
                        // + "<td  align = left>&nbsp;&nbsp;&nbsp;<a href=\"#\" onClick=javascript:popUpWin('DOMultiPickReversal.jsp?ITEM="+item+"&DONO="+dono+"&DOLNO="+dolno+"')>"+item+"</a></td>"
                        + "<td id='item_"+id+"' align = left>&nbsp;&nbsp;&nbsp;<a href=\"#\" onClick=javascript:popUpWin('TORandomPickReversal.jsp?ITEM="+item+"&TONO="+tono+"')>"+item+"</a></td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + desc + "</td>"
                        + "<td id='qtyor_"+id+"' align = right>&nbsp;&nbsp;&nbsp; " + qtyor + "</td>"
                         +"<td id='balqty_"+id+"' align = right>&nbsp;&nbsp;&nbsp; "   + String.valueOf(qtyBal)      +  "</td>"
                         + "<td id='qtyis_"+id+"' align = right>&nbsp;&nbsp;&nbsp;" + qtyPick + "</td>"
                         + "<td id='scan_"+id+"' align = right>&nbsp;&nbsp;&nbsp;"  + scanqty + "</td>"
                         + "</tr>" ;
                            	
                          resultJsonInt.put("PRODUCT", result);
                          resultJsonInt.put("LOC", Loc);
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
	
	private JSONObject validateProductAgainstPickList(String plant, String item,String tono) {
		JSONObject resultJson = new JSONObject();
		try {

			ToDetDAO  _ToDetDAO  = new ToDetDAO();  
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			
			  boolean itemFound = true;
              String scannedItemNo = itemMstDAO.getItemIdFromAlternate(plant, item);
              if (scannedItemNo == null) {
                      itemFound = false;
              }
              else{
            	  item = scannedItemNo;
              }
			
			
			List listQry =  _ToDetDAO.getTransferItemListByWMS(plant,tono,item);
			if(listQry.size() >0){
				for (int iCnt =0; iCnt<listQry.size(); iCnt++){
				Map lineArr = (Map) listQry.get(iCnt);
				
                 String itemdesc = StrUtils.fString((String)lineArr.get("itemdesc"));
                 String uom = StrUtils.fString((String)lineArr.get("unitmo"));
              
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("item", item);
				resultObjectJson.put("discription", itemdesc);
				resultObjectJson.put("uom", uom );		
				resultJson.put("result", resultObjectJson);
			}
			}
			else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	
	private String processTEMP(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String PLANT = "",ITEM = "",ITEMDESC  = "",BATCH = "", TONO = "",FROMLOC = "" ,TOLOC="", PICKING_QTY = "",ITEM_QTY = "",userName = "";
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
			FROMLOC = StrUtils.fString(request.getParameter("FROMLOC"));
			TOLOC = StrUtils.fString(request.getParameter("TOLOC"));
			TONO = StrUtils.fString(request.getParameter("TONO"));
			PICKING_QTY = StrUtils.fString(request.getParameter("QTY")); //Scanned QTY
			double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim()
					.toString()));
			pickingQty = StrUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
			
			Hashtable htproduct = new Hashtable();
			
			htproduct.put(IConstants.PLANT, PLANT);
			
			htproduct.put(IConstants.ITEM, ITEM);
			htproduct.put(IConstants.LOC, FROMLOC);
			htproduct.put(IConstants.LOC1, TOLOC);
			htproduct.put(IConstants.BATCH, BATCH);
			htproduct.put(IConstants.ORDERNO, TONO);
			
			Hashtable htproductinsert = new Hashtable();
			htproductinsert.put(IConstants.PLANT, PLANT);
			htproductinsert.put(IConstants.ORDERNO, TONO);
			htproductinsert.put(IConstants.ITEM, ITEM);
			htproductinsert.put(IConstants.LOC, FROMLOC);
			htproductinsert.put(IConstants.LOC1, TOLOC);
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
	
	/* ************Modification History*********************************
	   Oct 17 2014 Bruhan, Description: To include Transaction date
	*/
	private String processTOPickIssue(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String msg = "";
		String PLANT = "",ITEM = "",ITEMDESC  = "",BATCH = "", TONO = "",FROMLOC = "",TOLOC="" ,PICKING_QTY = "",
				ITEM_QTY = "",tolnno = "",userName = "",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
		ArrayList alResult = new ArrayList();
		Map map = null;
		boolean pickissueflag = false;
		//UserTransaction ut = null;
		try {
        	HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			TONO = StrUtils.fString(request.getParameter("TONO"));
			TRANSACTIONDATE=StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			Hashtable htloc = new Hashtable();
			htloc.put(IConstants.PLANT, PLANT);
			htloc.put(IConstants.TODET_TONUM, TONO);
			//change code 001
			String locquery = " isnull(fromwarehouse,'') as fromloc,isnull(towarehouse,'') as toloc ";
			List loclist = _ToHdrDAO.selectToHdr(locquery,htloc);
			if (loclist.size() > 0) {
			Map mloc = (Map) loclist.get(0);
			FROMLOC = (String) mloc.get("fromloc");
			TOLOC = (String) mloc.get("toloc");
			}
			map = new HashMap();
			map.put(IConstants.FROMLOC, FROMLOC);
			map.put(IConstants.TOLOC, TOLOC);
			map.put(IConstants.LOC, FROMLOC);
			map.put(IConstants.LOC2, "TEMP_TO_" +FROMLOC);
			
			Hashtable htcond = new Hashtable();
			htcond.put(IConstants.PLANT, PLANT);
			htcond.put(IConstants.ORDERNO, TONO);
			String query = " ITEM,ITEMDESC,BATCH,LOC,LOC1,QTY ";
			List listscnneddata = _TempUtil.listtempdata(query,htcond,"");
			if (listscnneddata.size() > 0) {
				for (int j = 0; j < listscnneddata.size(); j++) {
					Map mtemp = (Map) listscnneddata.get(j);
					ITEM = (String) mtemp.get("ITEM");
					ITEMDESC = (String) mtemp.get("ITEMDESC");
					BATCH = (String) mtemp.get("BATCH");
					//FROMLOC = (String) mtemp.get("LOC");
					//TOLOC = (String) mtemp.get("LOC1");
					PICKING_QTY = (String) mtemp.get("QTY");
					double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim().toString()));
					pickingQty = StrUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
			
			
			map.put(IConstants.PLANT, PLANT);
			map.put(IConstants.ITEM, ITEM);
			map.put(IConstants.ITEM_DESC, ITEMDESC);
			map.put(IConstants.TODET_TONUM, TONO);
			map.put(IConstants.TRAN_DATE, strMovHisTranDate);
			map.put(IConstants.ISSUEDATE, strTranDate);
		   //validation 
		   // 1. INV against
			List listQry = _InvMstDAO.getOutBoundPickingBatchByWMS(PLANT,ITEM, FROMLOC, BATCH);
			double invqty = 0;
			if (listQry.size() > 0) {
				for (int i = 0; i < listQry.size(); i++) {
					Map m = (Map) listQry.get(i);
					ITEM_QTY = (String) m.get("qty");
					invqty = Double.parseDouble(((String) ITEM_QTY.trim()));
					if(invqty < pickingQty){
						throw new Exception(
								"Error in picking OutBound Order : Inventory not found for the product: " +ITEM+ " with batch: " +BATCH+ "  scanned at the location  "+FROMLOC);
						}
				}
			} else {
				
				throw new Exception(
						"Error in picking OutBound Order : Inventory not found for the product: " +ITEM+ " with batch: " +BATCH+ "  scanned at the location  "+FROMLOC);
			}
			// 2.check for item in location
						/*Hashtable htLocMst = new Hashtable();
						htLocMst.put("plant", map.get(IConstants.PLANT));
						htLocMst.put("item", map.get(IConstants.ITEM));
						htLocMst.put("loc", map.get(IConstants.LOC));*/
						 UserLocUtil uslocUtil = new UserLocUtil();
			             uslocUtil.setmLogger(mLogger);
			             boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,userName,FROMLOC);
			             if(!isvalidlocforUser){
			                 throw new Exception(" Loc :"+FROMLOC+" is not User Assigned Location");
			             }		
			
			
			map.put(IConstants.CUSTOMER_NAME, _TOUtil.getCustName(PLANT, TONO));
			map.put(IConstants.LOGIN_USER, userName);
		    map.put(IConstants.CUSTOMER_CODE, _TOUtil.getCustCode(PLANT, TONO));
		    map.put(IConstants.BATCH, BATCH);
			map.put(IConstants.INV_EXP_DATE, "REMARK1");
			map.put(IConstants.REMARKS, "REMARK1");
			//start added by Bruhan for fine tuning process on 06 August 2013
			map.put("TYPE", "TRRANDOM");
			//End added by Bruhan for fine tuning process on 06 August 2013
			alResult = _TOUtil.getTODetDetails(PLANT, TONO, ITEM); 
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
           		tolnno =  StrUtils.fString((String)mapLn.get("tolnno")); 
           		map.put(IConstants.TODET_TOLNNO, tolnno);
           		map.put(IConstants.ORD_QTY,Double.toString(orderqty));
           	      if ((pickingQty >=balQty) && (pickingQty >0)  ){   
           	    	map.put(IConstants.QTY, Double.toString((balQty)));
           	    	map.put(IConstants.PICKQTY, Double.toString((pickingQty-balQty)));
          	    	
           	     pickissueflag = _TOUtil.process_BulkToPickingForPC(map);
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
           	  pickissueflag = _TOUtil.process_BulkToPickingForPC(map); 
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
				tolnno =  StrUtils.fString((String)mapLn.get("tolnno")); 
				map.put(IConstants.TODET_TOLNNO, tolnno);
           		map.put(IConstants.ORD_QTY, Double.toString(orderqty));
				map.put(IConstants.QTY, Double.toString((pickingQty)));		
				map.put(IConstants.PICKQTY, Double.toString((-1)));
          		  
				pickissueflag =  _TOUtil.process_BulkToPickingForPC(map);
            	 }
             }
             else {
           	  
           	  throw new Exception("Fully Picked for item : "+ITEM);
             }
	    	}
		}
			/*if (pickissueflag) {
				
				DbBean.CommitTran(ut);	
				pickissueflag = true;
				
			} else {
				DbBean.RollbackTran(ut);
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
	
	private String processResetScanQTY(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String PLANT = "",ITEM = "",TONO = "",LOC = "" ,userName = "";
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
			TONO = StrUtils.fString(request.getParameter("TONO"));
			//LOC = StrUtils.fString(request.getParameter("LOC_ID"));	
			//DO_LN_NUM = StrUtils.fString(request.getParameter("DOLNO"));	
			ITEM = StrUtils.fString(request.getParameter("ITEM"));	
			
			Hashtable htdeletetemp = new Hashtable();
			htdeletetemp.put("PLANT", PLANT);
			htdeletetemp.put("ORDERNO", TONO);
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
	
	
	private String processTOPickReversalRandomIssue(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String msg = "";
		String PLANT = "",ITEM = "",INV_QTY = "",TO_LN_NUM = "",ITEMDESC  = "",BATCH = "", TONO = "",FROMLOC = "" ,TOLOC="", PICKED_QTY = "",REVERSE_QTY = "",userName = "";
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
			TONO = StrUtils.fString(request.getParameter("TONO"));
			FROMLOC = StrUtils.fString(request.getParameter("FROMLOC"));
			TOLOC = StrUtils.fString(request.getParameter("TOLOC"));
			//DO_LN_NUM = StrUtils.fString(request.getParameter("DOLNO"));	
			ITEM = StrUtils.fString(request.getParameter("ITEM"));	
			REVERSE_QTY = StrUtils.fString(request.getParameter("reverseQty"));	
			BATCH = StrUtils.fString(request.getParameter("BATCH"));	
			//INV_QTY="1";
			
			Hashtable htdeletetemp = new Hashtable();
			htdeletetemp.put("PLANT", PLANT);
			htdeletetemp.put("ORDERNO", TONO);
			htdeletetemp.put("ITEM", ITEM);
			htdeletetemp.put("LOC", FROMLOC);
			htdeletetemp.put("LOC1", TOLOC);
			htdeletetemp.put("BATCH", BATCH);
			
			flag = _TempUtil.deletetemp(htdeletetemp);
			
				if (flag) {
				request.getSession().setAttribute("RESULT",
						"Reversed successfully!");
				response
						.sendRedirect("jsp/TORandomPickReversal.jsp?TONO="+TONO+"&ITEM="+ITEM+"&action=result");

			} else {
				request.getSession().setAttribute("RESULTERROR",
						"Error in reversing");
				response
				.sendRedirect("jsp/TORandomPickReversal.jsp?TONO="+TONO+"&ITEM="+ITEM+"&action=resulterror");
			}
	
			
		}
		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("RESULTERROR",
					"Error in reversing");
			response
			.sendRedirect("jsp/TORandomPickReversal.jsp?TONO="+TONO+"&ITEM="+ITEM+"&action=resulterror");
		}
		return msg;
	}
	
	
	//End code by Bruhan for Transfer order random on 8May2013
	
	//start code by Bruhan for transfer pick/Receive on 14 jan 15
	private String processTOPickReceive_Random(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String msg = "";
		String PLANT = "",ITEM = "",ITEMDESC  = "",BATCH = "", TONO = "",FROMLOC = "",TOLOC="" ,PICKING_QTY = "",
				ITEM_QTY = "",tolnno = "",userName = "",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
		ArrayList alResult = new ArrayList();
		Map map = null;
		boolean pickissueflag = false;
		//UserTransaction ut = null;
		try {
        	HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			TONO = StrUtils.fString(request.getParameter("TONO"));
			TRANSACTIONDATE=StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			Hashtable htloc = new Hashtable();
			htloc.put(IConstants.PLANT, PLANT);
			htloc.put(IConstants.TODET_TONUM, TONO);
			//change code 001
			String locquery = " isnull(fromwarehouse,'') as fromloc,isnull(towarehouse,'') as toloc ";
			List loclist = _ToHdrDAO.selectToHdr(locquery,htloc);
			if (loclist.size() > 0) {
			Map mloc = (Map) loclist.get(0);
			FROMLOC = (String) mloc.get("fromloc");
			TOLOC = (String) mloc.get("toloc");
			}
			map = new HashMap();
			map.put(IConstants.FROMLOC, FROMLOC);
			map.put(IConstants.TOLOC, TOLOC);
			map.put(IConstants.LOC, TOLOC);
						
			Hashtable htcond = new Hashtable();
			htcond.put(IConstants.PLANT, PLANT);
			htcond.put(IConstants.ORDERNO, TONO);
			String query = " ITEM,ITEMDESC,BATCH,LOC,LOC1,QTY ";
			List listscnneddata = _TempUtil.listtempdata(query,htcond,"");
			if (listscnneddata.size() > 0) {
				for (int j = 0; j < listscnneddata.size(); j++) {
					Map mtemp = (Map) listscnneddata.get(j);
					ITEM = (String) mtemp.get("ITEM");
					ITEMDESC = (String) mtemp.get("ITEMDESC");
					BATCH = (String) mtemp.get("BATCH");
					PICKING_QTY = (String) mtemp.get("QTY");
					double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim().toString()));
					pickingQty = StrUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
			
			
			map.put(IConstants.PLANT, PLANT);
			map.put(IConstants.ITEM, ITEM);
			map.put(IConstants.ITEM_DESC, ITEMDESC);
			map.put(IConstants.TODET_TONUM, TONO);
			map.put(IConstants.TRAN_DATE, strMovHisTranDate);
			map.put(IConstants.ISSUEDATE, strTranDate);
		   //validation 
		   // 1. INV against
			List listQry = _InvMstDAO.getOutBoundPickingBatchByWMS(PLANT,ITEM, FROMLOC, BATCH);
			double invqty = 0;
			if (listQry.size() > 0) {
				for (int i = 0; i < listQry.size(); i++) {
					Map m = (Map) listQry.get(i);
					ITEM_QTY = (String) m.get("qty");
					invqty = Double.parseDouble(((String) ITEM_QTY.trim()));
					if(invqty < pickingQty){
						throw new Exception(
								"Error in picking OutBound Order : Inventory not found for the product: " +ITEM+ " with batch: " +BATCH+ "  scanned at the location  "+FROMLOC);
						}
				}
			} else {
				
				throw new Exception(
						"Error in picking OutBound Order : Inventory not found for the product: " +ITEM+ " with batch: " +BATCH+ "  scanned at the location  "+FROMLOC);
			}
						 UserLocUtil uslocUtil = new UserLocUtil();
			             uslocUtil.setmLogger(mLogger);
			             boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,userName,FROMLOC);
			             if(!isvalidlocforUser){
			                 throw new Exception(" Loc :"+FROMLOC+" is not User Assigned Location");
			             }		
			
			
			map.put(IConstants.CUSTOMER_NAME, _TOUtil.getCustName(PLANT, TONO));
			map.put(IConstants.LOGIN_USER, userName);
		    map.put(IConstants.CUSTOMER_CODE, _TOUtil.getCustCode(PLANT, TONO));
		    map.put(IConstants.BATCH, BATCH);
			map.put(IConstants.INV_EXP_DATE, "REMARK1");
			map.put(IConstants.REMARKS, "REMARK1");
			map.put("TYPE", "TRRANDOM");
			
			alResult = _TOUtil.getTODetDetails(PLANT, TONO, ITEM); 
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
           		tolnno =  StrUtils.fString((String)mapLn.get("tolnno")); 
           		map.put(IConstants.TODET_TOLNNO, tolnno);
           		map.put(IConstants.ORD_QTY,Double.toString(orderqty));
           	      if ((pickingQty >=balQty) && (pickingQty >0)  ){   
           	    	map.put(IConstants.QTY, Double.toString((balQty)));
           	    	map.put(IConstants.PICKQTY, Double.toString((pickingQty-balQty)));
          	    	
           	     pickissueflag = _TOUtil.process_ToPickReceiveForPC(map);
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
           	  pickissueflag = _TOUtil.process_ToPickReceiveForPC(map); 
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
				tolnno =  StrUtils.fString((String)mapLn.get("tolnno")); 
				map.put(IConstants.TODET_TOLNNO, tolnno);
           		map.put(IConstants.ORD_QTY, Double.toString(orderqty));
				map.put(IConstants.QTY, Double.toString((pickingQty)));	
				map.put(IConstants.PICKQTY, Double.toString((-1)));
          		  
				pickissueflag =  _TOUtil.process_ToPickReceiveForPC(map);
            	 }
             }
             else {
           	  
           	  throw new Exception("Fully Picked/Received for item : "+ITEM);
             }
	    	}
		}
			
			
		if(pickissueflag) 
			{
			
				msg = "<font class='maingreen'>Products Picked/Received Successfully</font>";
			}
		}
		catch (Exception e) {
			
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
		}
		return msg;
	}
	
	
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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
