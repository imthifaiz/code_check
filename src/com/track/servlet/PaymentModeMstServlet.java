package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PaymentModeMstDAO;
import com.track.db.object.HrEmpDepartmentMst;
import com.track.db.object.HrEmpSalaryMst;
import com.track.db.object.PaymentModeMst;
import com.track.db.util.CoaUtil;
import com.track.gates.DbBean;
import com.track.service.HrEmpSalaryService;
import com.track.serviceImplementation.HrEmpSalaryServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/PaymentModeMst")
public class PaymentModeMstServlet extends HttpServlet implements IMLogger {

	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		// TODO Auto-generated method stub
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = StrUtils.fString(request.getParameter("CMD")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		String action = StrUtils.fString(request.getParameter("action")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		HrEmpSalaryService hrEmpSalaryService = new HrEmpSalaryServiceImpl();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		
		PaymentModeMstDAO paymentModeMstDAO = new PaymentModeMstDAO();
		
		 if (action.equals("GET_PAYMENT_MODE_LIST")) {
				JSONObject resultJson = new JSONObject();
		        JSONArray jsonArrayErr = new JSONArray();
				
				try {
		            String QUERY= StrUtils.fString(request.getParameter("QUERY"));
					List<PaymentModeMst>  paymentmode = paymentModeMstDAO.IsPaymentModeMstlist(plant,QUERY);
					resultJson.put("PAYMENTMODE", paymentmode);   
				} catch (Exception e) {
					// TODO Auto-generated catch block
					resultJson.put("SEARCH_DATA", "");
		            JSONObject resultJsonInt = new JSONObject();
		            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
		            resultJsonInt.put("ERROR_CODE", "98");
		            jsonArrayErr.add(resultJsonInt);
		            resultJson.put("ERROR", jsonArrayErr);
				}
				response.getWriter().write(resultJson.toString());
				
		}else  if (action.equals("CREATE_PAYMENT_MODE")) {
				JSONObject jsonObjectResult = new JSONObject();
        	  	try {
					jsonObjectResult = createpaymenttype(request);
					response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
                
          }
	}
	
	private JSONObject createpaymenttype(HttpServletRequest request) 
			throws IOException, ServletException,Exception {
			JSONObject resultJson = new JSONObject();
			String msg = "";
			String  PLANT="",PaymenMode="";
			ArrayList alResult = new ArrayList();
			DateUtils dateutils = new DateUtils();
			PaymentModeMstDAO paymentModeMstDAO = new PaymentModeMstDAO();
			try {
	            HttpSession session = request.getSession();
     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				String UserId = (String) session.getAttribute("LOGIN_USER");
				PaymenMode = StrUtils.fString(request.getParameter("PAYMENTMODE"));
				
				if (!paymentModeMstDAO.IsPaymentModeMstExists(PLANT,PaymenMode)) // if the PaymenType exists already 17.10.18 by Azees
				{						
					
					PaymentModeMst paymentModeMst = new PaymentModeMst();
					paymentModeMst.setPLANT(PLANT);
					paymentModeMst.setPAYMENTMODE(PaymenMode);
					paymentModeMst.setISNOTEDITABLE(Short.valueOf("0"));
					paymentModeMst.setCRBY(UserId);
					paymentModeMst.setCRBY(dateutils.getDateTime());
					
					boolean insertflag = paymentModeMstDAO.addPaymentModeMst(paymentModeMst);
												
					if(insertflag){
						resultJson.put("STATUS", "100");
						resultJson.put("PAYMENTMODE", PaymenMode);
						resultJson.put("MESSAGE", "Payment Mode Added Successfully");
					}
					else{
						resultJson.put("STATUS", "99");
						resultJson.put("MESSAGE", "Error in Adding Payment Mode");							
					}
				}
				else
				{
					resultJson.put("STATUS", "99");
					resultJson.put("MESSAGE", "Payment Mode Exists already");
				}								
			}catch (Exception e) {
				resultJson.put("STATUS", "99");
				resultJson.put("MESSAGE", e.getMessage());								
			}
			return resultJson;
		}

}
