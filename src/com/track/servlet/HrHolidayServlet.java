package com.track.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.EmployeeDAO;
import com.track.dao.HrLeaveTypeDAO;
import com.track.dao.MovHisDAO;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrHolidayMst;
import com.track.gates.DbBean;
import com.track.service.HrEmpTypeService;
import com.track.service.HrHolidayMstService;
import com.track.serviceImplementation.HrEmpTypeServiceImpl;
import com.track.serviceImplementation.HrHolidayMstServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/HrHolidayServlet")
public class HrHolidayServlet extends HttpServlet implements IMLogger  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		// TODO Auto-generated method stub
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("Submit")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		HrHolidayMstService hrHolidayMstService = new HrHolidayMstServiceImpl();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		
		if(action.equalsIgnoreCase("SAVE")) {
			String[] holidaydate = request.getParameterValues("holidaydate");
			String[] holidaydesc = request.getParameterValues("holidaydesc");
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				 if (holidaydate != null)    {
		 	    	 for (int i = 0; i < holidaydate.length; i++){
		 	    		boolean holidaycheck = hrHolidayMstService.IsHoliday(plant, holidaydate[i]);
		 	    		if(holidaycheck) {
		 	    			continue;
		 	    		}else {
		 	    			HrHolidayMst hrHolidayMst = new HrHolidayMst();
		 	    			hrHolidayMst.setPLANT(plant);
		 	    			hrHolidayMst.setHOLIDAY_DATE(holidaydate[i]);
		 	    			hrHolidayMst.setHOLIDAY_DESC(holidaydesc[i]);
		 	    			hrHolidayMst.setCRAT(dateutils.getDate());
		 	    			hrHolidayMst.setCRBY(username);
			 	    		int hid = hrHolidayMstService.addHoliday(hrHolidayMst);
			 	    		
			 	    		Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, plant);					
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_HOLIDAY);	
							htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hid);
							htMovHis.put(IDBConstants.CREATED_BY, username);		
							htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
							htMovHis.put("REMARKS",holidaydate[i]);
							movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
		 	    		}
		 	    	 }
		 	     }

				DbBean.CommitTran(ut);
				result = "Employee holidays added successfully.";
				response.sendRedirect("../payroll/holiday?result="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add Employee holiday";
				response.sendRedirect("../payroll/createholiday?result="+ result);
			}
	 	    	  
		}
		
		
		if(action.equalsIgnoreCase("Update")) {
			String holidaydate = request.getParameter("holidaydate");
			String holidaydesc = request.getParameter("holidaydesc");
			String hoid = request.getParameter("hid");
			int hid = Integer.parseInt(hoid);
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				boolean etstatus = true;
				List<HrHolidayMst> holidayList = hrHolidayMstService.IsHolidaylist(plant, holidaydate);
				for (HrHolidayMst hrHolidayMst : holidayList) {
					if(hrHolidayMst.getID() != hid) {
						etstatus = false;
					}
				}
				if(etstatus) {
					
					HrHolidayMst holidaymaster = hrHolidayMstService.getHolidayById(plant, hid);
					holidaymaster.setHOLIDAY_DATE(holidaydate);
					holidaymaster.setHOLIDAY_DESC(holidaydesc);
					holidaymaster.setUPAT(dateutils.getDate());
					holidaymaster.setUPBY(username);
					
					hrHolidayMstService.updateHoliday(holidaymaster, username);
					
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.UPDATE_HOLIDAY);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",holidaydate);
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
					DbBean.CommitTran(ut);
					result = "Employee Holiday updated successfully.";
					response.sendRedirect("../payroll/holiday?result="+ result);
				}else {
					DbBean.RollbackTran(ut);
					result = "Employee Holiday already exist";
					response.sendRedirect("../payroll/editholiday?ID="+hid+"&result="+ result);
				}
				
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add employee Holiday.";
				response.sendRedirect("../payroll/editholiday?ID="+hid+"&result="+ result);
			}
	 	    	  
		}
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("CMD")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		HrHolidayMstService hrHolidayMstService = new HrHolidayMstServiceImpl();
		DateUtils dateutils = new DateUtils();
		if (action.equalsIgnoreCase("CHECK_HOLIDAY_DATE")) {
			JSONObject jsonObjectResult = new JSONObject();
			jsonObjectResult = checkholidaydate(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		
		if (action.equalsIgnoreCase("CHECK_HOLIDAY_DATE_EDIT")) {
			JSONObject jsonObjectResult = new JSONObject();
			jsonObjectResult = checkholidaydateedit(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		
		if(action.equalsIgnoreCase("GET_HOLIDAY_MST"))
		{
			String balace = "0";
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			
			try {
				List<HrHolidayMst> holidayList = hrHolidayMstService.getAllHoliday(plant);
				JSONObject resultJsonInt = new JSONObject();
				resultJson.put("HOLIDAYLIST", holidayList);   
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
		
		if(action.equalsIgnoreCase("HOLIDAY_DELETE"))
		{
			String hid= StrUtils.fString(request.getParameter("ID"));
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        EmployeeDAO EmployeeDAO = new EmployeeDAO();
	        HrLeaveTypeDAO HrLeaveTypeDAO = new HrLeaveTypeDAO();
			try {
				HrHolidayMst hrHolidayMst = hrHolidayMstService.getHolidayById(plant, Integer.valueOf(hid));
				String curDate = dateutils.getDate();
				String deldate = hrHolidayMst.getHOLIDAY_DATE();
				int cintdate = datetoint(curDate);
				int dintdate = datetoint(deldate);
				if(dintdate <= cintdate) {
					resultJson.put("STATUS", "NOT OK");  
				}else {
					hrHolidayMstService.DeleteHoliday(plant, Integer.valueOf(hid));
					resultJson.put("STATUS", "OK"); 
				}
			} catch (Exception e) {
				resultJson.put("STATUS", "NOT OK");  
			}
			
			
			response.getWriter().write(resultJson.toString());
			
		}
		
	}
		
	public int datetoint(String hdate) {
		String splithdate[] = hdate.split("/");
		String joinhdate = splithdate[2]+splithdate[1]+splithdate[0];
		int intdate = Integer.valueOf(joinhdate);
		return intdate;
	}
	
	public JSONObject checkholidaydate(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String holidaydate = StrUtils.fString(request.getParameter("hdate"));
		
		JSONObject resultJson = new JSONObject();
		HrHolidayMstService hrHolidayMstService = new HrHolidayMstServiceImpl();
		DateUtils dateutils = new DateUtils();
		UserTransaction ut = null;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			boolean checkdate = hrHolidayMstService.IsHoliday(plant, holidaydate);
			
			if(!checkdate) {
				DbBean.CommitTran(ut);
				resultJson.put("STATUS", "OK");
			}else {
				DbBean.RollbackTran(ut);
				resultJson.put("STATUS", "NOT OK");
			}
		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			resultJson.put("STATUS", "NOT OK");
			e.printStackTrace();
		}
		return resultJson;
	}
	
	public JSONObject checkholidaydateedit(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String holidaydate = StrUtils.fString(request.getParameter("hdate"));
		String hid = StrUtils.fString(request.getParameter("hid"));
		
		JSONObject resultJson = new JSONObject();
		HrHolidayMstService hrHolidayMstService = new HrHolidayMstServiceImpl();
		DateUtils dateutils = new DateUtils();
		UserTransaction ut = null;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			boolean checkdate = hrHolidayMstService.IsHolidayedit(plant,hid,holidaydate);
			
			if(!checkdate) {
				DbBean.CommitTran(ut);
				resultJson.put("STATUS", "OK");
			}else {
				DbBean.RollbackTran(ut);
				resultJson.put("STATUS", "NOT OK");
			}
		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			resultJson.put("STATUS", "NOT OK");
			e.printStackTrace();
		}
		return resultJson;
	}


}
