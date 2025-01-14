package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.dao.FinProjectDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PaymentModeMstDAO;
import com.track.db.object.FinProject;
import com.track.db.object.PaymentModeMst;
import com.track.service.HrEmpSalaryService;
import com.track.serviceImplementation.HrEmpSalaryServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/FinProject")
public class FinProjectServlet extends HttpServlet implements IMLogger {

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
		
		FinProjectDAO finProjectDAO = new FinProjectDAO();
		
		 if (action.equals("GET_PROJECT_LIST")) {
				JSONObject resultJson = new JSONObject();
		        JSONArray jsonArrayErr = new JSONArray();
				
				try {
		            String QUERY= StrUtils.fString(request.getParameter("QUERY"));
		            String custno= StrUtils.fString(request.getParameter("CUSTNO"));
		            List<FinProject>  finProjectlist = new ArrayList<FinProject>();
		            DateUtils _dateUtils = new DateUtils();
		        	String curDate = _dateUtils.getDate();
		            if(custno.length() > 0) {
		            	finProjectlist = finProjectDAO.dropFinProjectlistwithStatus(plant, QUERY, custno, curDate);
		            }else {
		            	finProjectlist = finProjectDAO.dFinProjectlistwithStatus(plant, QUERY, curDate);
		            }
					resultJson.put("PROJECT", finProjectlist);   
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
				
		}
	}

}
