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
import com.track.dao.BillPaymentDAO;
import com.track.dao.CoaDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.HrLeaveTypeDAO;
import com.track.dao.MovHisDAO;
import com.track.db.object.HrEmpType;
import com.track.db.util.CoaUtil;
import com.track.gates.DbBean;
import com.track.service.HrEmpTypeService;
import com.track.serviceImplementation.HrEmpTypeServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/EmployeeTypeServlet")
public class EmployeeTypeServlet extends HttpServlet implements IMLogger  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("Submit")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		HrEmpTypeService HrEmpTypeService = new HrEmpTypeServiceImpl();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		
		if(action.equalsIgnoreCase("SAVE")) {
			String[] emptype = request.getParameterValues("emptype");
			String[] emptypedesc = request.getParameterValues("emptypedesc");
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				 if (emptype != null)    {
		 	    	 for (int i = 0; i < emptype.length; i++){
		 	    		boolean emptypecheck = HrEmpTypeService.IsEmployeetype(plant, emptype[i]);
		 	    		if(emptypecheck) {
		 	    			continue;
		 	    		}else {
		 	    			HrEmpType hrEmpType = new HrEmpType();
			 	    		hrEmpType.setPLANT(plant);
			 	    		hrEmpType.setEMPLOYEETYPE(emptype[i]);
			 	    		hrEmpType.setEMPLOYEETYPEDESC(emptypedesc[i]);
			 	    		hrEmpType.setCRAT(dateutils.getDate());
			 	    		hrEmpType.setCRBY(username);
			 	    		hrEmpType.setIsActive("Y");
			 	    		int etid = HrEmpTypeService.addEmployeetype(hrEmpType);
			 	    		
			 	    		Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, plant);					
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_EMPLOYEE_TYPE);	
							htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));													
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, etid);
							htMovHis.put(IDBConstants.CREATED_BY, username);		
							htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
							htMovHis.put("REMARKS",emptype[i]);
							movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
		 	    		}
		 	    	 }
		 	     }
				 
				
				 
				DbBean.CommitTran(ut);
				result = "Employee Type added successfully.";
				response.sendRedirect("../payroll/employeetype?result="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add employee type.";
				response.sendRedirect("../payroll/createemployeetype?result="+ result);
			}
	 	    	  
		}
		
		if(action.equalsIgnoreCase("Update")) {
			String emptype = request.getParameter("emptype");
			String emptypedesc = request.getParameter("emptypedesc");
			String isactive = request.getParameter("ACTIVE");
			String Etid = request.getParameter("etid");
			int Eid = Integer.parseInt(Etid);
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				boolean etstatus = true;
				List<HrEmpType> EmpypeList = HrEmpTypeService.IsEmployeetypelist(plant, emptype);
				for (HrEmpType hrEmpType : EmpypeList) {
					if(hrEmpType.getID() != Eid) {
						etstatus = false;
					}
				}
				if(etstatus) {
					
					HrEmpType employeetype = HrEmpTypeService.getEmployeetypeById(plant, Eid);
					employeetype.setEMPLOYEETYPE(emptype);
					employeetype.setEMPLOYEETYPEDESC(emptypedesc);
					employeetype.setIsActive(isactive);
					employeetype.setUPAT(dateutils.getDate());
					employeetype.setUPBY(username);
					
					HrEmpTypeService.updateEmployeetype(employeetype, username);
					
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.UPDATE_EMPLOYEE_TYPE);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, Etid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",emptype);
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
					DbBean.CommitTran(ut);
					result = "Employee Type updated successfully.";
					response.sendRedirect("../payroll/employeetype?result="+ result);
				}else {
					DbBean.RollbackTran(ut);
					result = "Employee Type already exist";
					response.sendRedirect("../payroll/editemployeetype?ID="+Etid+"&result="+ result);
				}
				
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add employee type.";
				response.sendRedirect("../payroll/editemployeetype?ID="+Etid+"&result="+ result);
			}
	 	    	  
		}
		
		if (action.equalsIgnoreCase("Savepopup")) {
			JSONObject jsonObjectResult = new JSONObject();
			jsonObjectResult = savepopupemptype(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = StrUtils.fString(req.getParameter("CMD")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		HrEmpTypeService HrEmpTypeService = new HrEmpTypeServiceImpl();
		
		if(action.equalsIgnoreCase("GET_EMPLOYEE_TYPE"))
		{
			String balace = "0";
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			
			try {
				List<HrEmpType> EmpypeList = HrEmpTypeService.getAllEmployeetype(plant);
				JSONObject resultJsonInt = new JSONObject();
				resultJson.put("EMPTYPELIST", EmpypeList);   
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			
			
			resp.getWriter().write(resultJson.toString());
			
		}
		
		if(action.equalsIgnoreCase("EMPLOYEE_TYPE_DELETE"))
		{
			String empid= StrUtils.fString(req.getParameter("ID"));
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        EmployeeDAO EmployeeDAO = new EmployeeDAO();
	        HrLeaveTypeDAO HrLeaveTypeDAO = new HrLeaveTypeDAO();
			try {
				
				if(EmployeeDAO.isEmptypeExists(empid,plant)) {
					resultJson.put("STATUS", "NOT OK");
				}else if(HrLeaveTypeDAO.isEmptypeExists(empid,plant)) {
					resultJson.put("STATUS", "NOT OK");
				}else {
					HrEmpTypeService.DeleteEmployeetype(plant,Integer.valueOf(empid));
					resultJson.put("STATUS", "OK"); 
				}
			} catch (Exception e) {
				resultJson.put("STATUS", "NOT OK");  
			}
			
			
			resp.getWriter().write(resultJson.toString());
			
		}
		
		if(action.equalsIgnoreCase("GET_EMPLOYEE_TYPE_DROPDOWN"))
		{
			String QUERY= StrUtils.fString(req.getParameter("QUERY"));
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			
			try {
				List<HrEmpType> EmpypeList = HrEmpTypeService.getAllEmployeetypedropdown(plant,QUERY);
				JSONObject resultJsonInt = new JSONObject();
				resultJson.put("EMPTYPELIST", EmpypeList);   
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			
			
			resp.getWriter().write(resultJson.toString());
			
		}
		
		if(action.equalsIgnoreCase("CHECK_EMPLOYEE_TYPE"))
		{
			String emptype= StrUtils.fString(req.getParameter("EMPTYPE"));
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			
			try {
				boolean Empypecheck = HrEmpTypeService.IsEmployeetype(plant, emptype);
				if(Empypecheck) {
					resultJson.put("STATUS", "NOT OK");   
				}else {
					resultJson.put("STATUS", "OK");   
				}
			} catch (Exception e) {
				resultJson.put("STATUS", "NOT OK");  
			}
			
			
			resp.getWriter().write(resultJson.toString());
			
		}
		
	}
		
	
	public JSONObject savepopupemptype(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String emptypepopup = StrUtils.fString(request.getParameter("emptypepopup"));
		String emptypedescpopup = StrUtils.fString(request.getParameter("emptypedescpopup"));
		String ukeyet = StrUtils.fString(request.getParameter("ukeyet"));
		String tri = StrUtils.fString(request.getParameter("tri"));
		MovHisDAO movHisDao = new MovHisDAO();
		JSONObject resultJson = new JSONObject();
		HrEmpTypeService HrEmpTypeService = new HrEmpTypeServiceImpl();
		DateUtils dateutils = new DateUtils();
		UserTransaction ut = null;
		int etid = 0;
		int duplicate = 0;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			boolean emptypecheck = HrEmpTypeService.IsEmployeetype(plant, emptypepopup);
	    	if(emptypecheck) {
	    		duplicate = 1;
	    	}else {
	    		HrEmpType hrEmpType = new HrEmpType();
 	    		hrEmpType.setPLANT(plant);
 	    		hrEmpType.setEMPLOYEETYPE(emptypepopup);
 	    		hrEmpType.setEMPLOYEETYPEDESC(emptypedescpopup);
 	    		hrEmpType.setCRAT(dateutils.getDate());
 	    		hrEmpType.setCRBY(username);
 	    		hrEmpType.setIsActive("Y");
 	    		etid = HrEmpTypeService.addEmployeetype(hrEmpType);
 	    		
 	    		Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.CREATE_EMPLOYEE_TYPE);	
				htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
				htMovHis.put("RECID", "");
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, etid);
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				htMovHis.put("REMARKS",emptypepopup);
				movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
	    	}
			if(duplicate == 0) {
				if (etid > 0) {
					DbBean.CommitTran(ut);
					resultJson.put("MESSAGE", "Employee Type created successfully");
					resultJson.put("EMPLOYEETYPE", emptypepopup);
					resultJson.put("EMPLOYEETYPEID", etid);
					resultJson.put("TRI", tri);
					resultJson.put("STATUS", "SUCCESS");
				} else {
					DbBean.RollbackTran(ut);
					resultJson.put("MESSAGE", "Employee Type creation failed");
					resultJson.put("STATUS", "FAIL");
				}
			}else {
				DbBean.RollbackTran(ut);
				resultJson.put("STATUS", "FAIL");
				resultJson.put("MESSAGE", "Employee Type already exists");
			}
		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			e.printStackTrace();
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
