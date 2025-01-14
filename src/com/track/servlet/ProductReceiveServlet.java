package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.track.constants.IDBConstants;
import com.track.dao.ItemMstDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.MasterUtil;
import com.track.gates.selectBean;
import com.track.gates.userBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/productreceive/*")
public class ProductReceiveServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = "";
		action = StrUtils.fString(request.getParameter("ACTION")).trim();
		if ("".equals(action) && request.getPathInfo() != null) {
			String[] pathInfo = request.getPathInfo().split("/");
			action = pathInfo[1];
		}
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		JSONObject jsonObjectResult = new JSONObject();
		
		
		if (action.equals("VIEW_RETURN_SUMMARY_VIEW")) {
	        jsonObjectResult = this.getinvoiceview(request);
			response.setContentType("application/json");
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	       	       
	    }
		
		if (action.equalsIgnoreCase("GET_RETURN_NO_FOR_AUTO_SUGGESTION")) {			
			jsonObjectResult = this.getOrderNoForAutoSuggestion(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		}


}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = "";
		action = StrUtils.fString(request.getParameter("ACTION")).trim();
		if ("".equals(action) && request.getPathInfo() != null) {
			String[] pathInfo = request.getPathInfo().split("/");
			action = pathInfo[1];
		}
//		String[] pathInfo = request.getPathInfo().split("/");
//		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
		JSONObject jsonObjectResult = new JSONObject();
		TblControlDAO _TblControlDAO = new TblControlDAO();
		
		if (action.equals("VIEW_RETURN_SUMMARY_VIEW")) {
	        jsonObjectResult = this.getinvoiceview(request);
			response.setContentType("application/json");
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	       	       
	    }
		
		if (action.equalsIgnoreCase("GET_RETURN_NO_FOR_AUTO_SUGGESTION")) {			
			jsonObjectResult = this.getOrderNoForAutoSuggestion(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		}
		
		if (action.equalsIgnoreCase("CheckOrderno")) {
			String orderno = StrUtils.fString(request.getParameter("ORDERNO")).trim();
			try {
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put("IDRNO", orderno);
				if (new ItemMstDAO().isExisit(ht)) {
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
	
		if(action.equalsIgnoreCase("summary")) {
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ProductReceiveSummary.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(action.equalsIgnoreCase("detail")) {
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ProductReceiveDetail.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(action.equalsIgnoreCase("receiptsummary")) {
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ProductReceiveOrderBulkReceiptSummary.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}







		
}
	
	private JSONObject getinvoiceview(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        InvoiceUtil movHisUtil       = new InvoiceUtil();
        ArrayList movQryList  = new ArrayList();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        
        String fdate="",tdate="",covunitCostValue="0";
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
           String  CUSTOMER = StrUtils.fString(request.getParameter("CNAME"));
           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
           String  ITEM = StrUtils.fString(request.getParameter("ITEM"));
           String  INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
           String  PLNO = StrUtils.fString(request.getParameter("PLNO"));
           String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
           String  EMPNO = StrUtils.fString(request.getParameter("EMP_NAME"));
           
           String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
           
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
			if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
				FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

              
   
           Hashtable ht = new Hashtable();
           
	        if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("ICRNO",ORDERNO);
	        if(StrUtils.fString(ITEM).length() > 0)       ht.put("ITEM",ITEM);
	        if(StrUtils.fString(EMPNO).length() > 0)       ht.put("EMPNO",EMPNO);
	        if(StrUtils.fString(STATUS).length() > 0)       ht.put("RECEIVE_STATUS",STATUS);
	        
	        
				movQryList = movHisUtil.getReceiveSummaryView(ht,fdate,tdate,PLANT,CUSTOMER);	
            
            if (movQryList.size() > 0) {
            int Index = 0;
//             int iIndex = 0,irow = 0;
                                  
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);                            
                               
//                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
                            double unitCostVal ="".equals(unitCostValue) ? 0.0f :  Double.parseDouble(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00000";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            
                            unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
                            covunitCostValue= StrUtils.addZeroes((Double.parseDouble(unitCostValue)*Double.parseDouble((String)lineArr.get("CURRENCYUSEQT"))), numberOfDecimal);
                            
                            
                      
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("billdate",StrUtils.fString((String)lineArr.get("RECEIVE_DATE")));
                    	 resultJsonInt.put("bill",StrUtils.fString((String)lineArr.get("ICRNO")));
                    	 resultJsonInt.put("custno",StrUtils.fString((String)lineArr.get("CustCode")));
                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("CustName")));
                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("RECEIVE_STATUS")));
                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
                    	 resultJsonInt.put("currency",StrUtils.fString(cur));
                    	 //if(cur.isEmpty())
                    		 //cur= curency;
                    	 resultJsonInt.put("invamount",Numbers.toMillionFormat(covunitCostValue,Integer.valueOf(numberOfDecimal)));
                    	// resultJsonInt.put("convamount",cur+Numbers.toMillionFormat(covunitCostValue,Integer.valueOf(numberOfDecimal)));
                    	 resultJsonInt.put("convamount",Numbers.toMillionFormat(covunitCostValue,Integer.valueOf(numberOfDecimal)));
                    	 resultJsonInt.put("exchangerate",StrUtils.fString((String)lineArr.get("CURRENCYUSEQT")));
                    	 resultJsonInt.put("invoiceid", StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("amount",unitCostValue);
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
	
	private JSONObject getOrderNoForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        ItemMstDAO itemUtil = new ItemMstDAO();
//        StrUtils strUtils = new StrUtils();
        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String pono = StrUtils.fString(request.getParameter("PONO")).trim();
		    String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
		    String type = StrUtils.fString(request.getParameter("TYPE")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);
		     if(pono.length()>0) extCond=" AND plant='"+plant+"' and ICRNO like '"+pono+"%' ";
		     if(cname.length()>0) extCond +=" AND CustName = '"+cname+"' ";
		     extCond=extCond+"ORDER BY CONVERT(date, RECEIVE_DATE, 103) desc";
		     ArrayList listQry = itemUtil.selectProductReceiveHdr("ICRNO,CustName,CustCode,jobNum,RECEIVE_DATE",ht,extCond);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  pono = (String)m.get("ICRNO");
				  String custName = StrUtils.replaceCharacters2Send((String)m.get("CustName"));
				  String custcode = (String)m.get("CustCode");
				  String orderdate = (String)m.get("RECEIVE_DATE");
				  String jobNum = (String)m.get("jobNum");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("PONO", pono);
				  resultJsonInt.put("CUSTNAME", custName);
				  resultJsonInt.put("CUSTCODE", custcode);
				  resultJsonInt.put("ORDERDATE", orderdate);
				  resultJsonInt.put("JOBNUM", jobNum);
				  resultJsonInt.put("STATUS", "");				  
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
	
	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		// TODO Auto-generated method stub
		
	}

}

		