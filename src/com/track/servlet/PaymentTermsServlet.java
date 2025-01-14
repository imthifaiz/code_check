package com.track.servlet;

import java.io.IOException;
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

import com.track.constants.MLoggerConstant;
import com.track.db.util.POUtil;
import com.track.db.util.PayTermsUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PaymentTermsServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.ItemMstServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.ItemMstServlet_PRINTPLANTMASTERINFO;
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonObjectResult = new JSONObject();
	    HttpSession session = request.getSession(false);
	    String action = StrUtils.fString(request.getParameter("ACTION")).trim();
		String plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
		String userName = StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim();
		this.setMapDataToLogger(this.populateMapData(plant, userName));
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		if (action.equals("ADD_PAYMENT_TERMS")) {
			jsonObjectResult = this.addpayTerms(request);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		}
		if (action.equals("GET_PAYMENT_TERMS_DETAILS")) {
            jsonObjectResult = this.getPaymentTermsDetails(request);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
         }
	}
	
	
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		loggerDetailsHasMap.put(MLogger.USER_CODE,userCode);
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
	
	private JSONObject getPaymentTermsDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        PayTermsUtil payTermsUtil = new PayTermsUtil();
        StrUtils strUtils = new StrUtils();
        payTermsUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String terms = StrUtils.fString(request.getParameter("TERMS")).trim();		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);
		     ht.put("TERMS",terms);
		     List listQry = payTermsUtil.getPaymentTermsDetails(ht);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {   
				  Map m=(Map)listQry.get(i);
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("ID", (String)m.get("ID"));
				  resultJsonInt.put("PAYMENT_TERMS", (String)m.get("PAYMENT_TERMS"));
				  resultJsonInt.put("NO_DAYS", (String)m.get("NO_DAYS"));
				  resultJsonInt.put("CRAT", (String)m.get("CRAT"));
				  resultJsonInt.put("CRBY", (String)m.get("CRBY"));
				  resultJsonInt.put("UPAT", (String)m.get("UPAT"));	
				  resultJsonInt.put("UPBY", (String)m.get("UPBY"));	
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("terms", jsonArray);
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
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	private JSONObject addpayTerms(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		DateUtils dateutils = new DateUtils();
		PayTermsUtil payTermsUtil = new PayTermsUtil();
		try {
			HttpSession session = request.getSession();
			String plant = (String) session.getAttribute("PLANT");
			String user = (String) session.getAttribute("LOGIN_USER");
			String payTerms = request.getParameter("PAYMENT_TERMS").trim();
			String days = request.getParameter("NO_DAYS").trim();
			boolean isAdded = false;
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("PAYMENT_TERMS", payTerms);
			ht.put("NO_DAYS", days);
			ht.put("CRBY", user);
			ht.put("CRAT", dateutils.getDateTime());
			ht.put("UPBY", user);
			ht.put("UPAT", dateutils.getDateTime());
			isAdded = payTermsUtil.addpaymentTerms(ht, plant);
			if(isAdded) {
				resultJson.put("MESSAGE", "Payment Terms creted successfully");
				resultJson.put("PAYMENT_TERMS", payTerms);
				resultJson.put("NO_DAYS", days);
				resultJson.put("STATUS", "SUCCESS");
			}else {
				resultJson.put("MESSAGE", "Failed to add New Payment Terms");
				resultJson.put("STATUS", "FAIL");
			}			
		}catch (Exception e) {
			System.out.println(e.getMessage());
			resultJson.put("MESSAGE", "Failed to add New Supplier");
			resultJson.put("STATUS", "FAIL");
		}
		return resultJson;
	}
}
