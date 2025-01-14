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
import com.track.dao.CoaDAO;
import com.track.dao.MovHisDAO;
import com.track.db.object.HrEmpDepartmentMst;
import com.track.db.util.CoaUtil;
import com.track.gates.DbBean;
import com.track.service.HrDepartmentService;
import com.track.serviceImplementation.HrDepartmentServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class HrDepartmentServlet
 */
@WebServlet("/HrDepartmentServlet")
public class HrDepartmentServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HrDepartmentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String action = StrUtils.fString(req.getParameter("CMD")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		
		HrDepartmentService hrDepartmentService = new HrDepartmentServiceImpl();
		
		if(action.equalsIgnoreCase("GET_DEPARTMENT_FOR_SUMMARY"))
		{
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			
			try {
				List<HrEmpDepartmentMst> DepartmentList = hrDepartmentService.getAllDepartment(plant);
				resultJson.put("DEPARTMENTLIST", DepartmentList);   
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
		
		if(action.equalsIgnoreCase("GET_DEPARTMENT_DROPDOWN"))
		{
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        String QUERY= StrUtils.fString(req.getParameter("QUERY"));
			try {
				List<HrEmpDepartmentMst> DepartmentList = hrDepartmentService.IsDepartmentlistdropdown(plant, QUERY);
				resultJson.put("DEPARTMENTLIST", DepartmentList);   
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
		
		if(action.equalsIgnoreCase("DEPARTMENT_CHECK"))
		{
			JSONObject resultJson = new JSONObject();
	        String QUERY= StrUtils.fString(req.getParameter("QUERY"));
			try {
				if (hrDepartmentService.IsDepartmentExists(plant, QUERY)) {
					resultJson.put("status", "100");
				} else {
					resultJson.put("status", "99");
				}
			} catch (Exception e) {
				resultJson.put("status", "99");
			}
			resp.getWriter().write(resultJson.toString());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		String action = StrUtils.fString(request.getParameter("Submit")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		HrDepartmentService hrDepartmentService = new HrDepartmentServiceImpl();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		
		if(action.equalsIgnoreCase("SAVE")) {
			String[] emptype = request.getParameterValues("department");
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				 if (emptype != null)    {
		 	    	 for (int i = 0; i < emptype.length; i++){
		 	    		boolean emptypecheck = hrDepartmentService.IsDepartmentExists(plant, emptype[i]);
		 	    		if(!emptypecheck) {
		 	    			HrEmpDepartmentMst hrDepartment = new HrEmpDepartmentMst();
		 	    			hrDepartment.setPLANT(plant);
		 	    			hrDepartment.setDEPARTMENT(emptype[i]);
		 	    			hrDepartment.setCRAT(dateutils.getDate());
		 	    			hrDepartment.setCRBY(username);
		 	    			hrDepartment.setIsActive("Y");
			 	    		int etid = hrDepartmentService.addDepartment(hrDepartment);
			 	    		
/*			 	    		CoaUtil coaUtil = new CoaUtil();
							Hashtable<String, String> accountHt = new Hashtable<>();
							accountHt.put("PLANT", plant);
							accountHt.put("ACCOUNTTYPE", "11");
							accountHt.put("ACCOUNTDETAILTYPE", "30");
							accountHt.put("ACCOUNT_NAME", emptype[i]);
							accountHt.put("DESCRIPTION", "");
							accountHt.put("ISSUBACCOUNT", "1");
							accountHt.put("SUBACCOUNTNAME", "85");
							accountHt.put("OPENINGBALANCE", "");
							accountHt.put("OPENINGBALANCEDATE", "");
							accountHt.put("CRAT", dateutils.getDateTime());
							accountHt.put("CRBY", username);
							accountHt.put("UPAT", dateutils.getDateTime());
							coaUtil.addAccount(accountHt, plant);*/
			 	    		
			 	    		Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, plant);					
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_DEPARTMENT);	
							htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, etid);
							htMovHis.put(IDBConstants.CREATED_BY, username);		
							htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
							htMovHis.put("REMARKS",emptype[i]);
							movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
		 	    		}
		 	    		else
		 	    		{
		 	    			DbBean.RollbackTran(ut);
		 	    			result = emptype[i] + " - Department Already Exists";
		 					response.sendRedirect("../payroll/createdepartment?result="+ result);
		 	    		}
		 	    	 }
		 	     }
				 
				
				 
				DbBean.CommitTran(ut);
				result = "Department added successfully.";
				response.sendRedirect("../payroll/department?result="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add Department";
				response.sendRedirect("../payroll/createdepartment?result="+ result);
			}
	 	    	  
		}
		
		if(action.equalsIgnoreCase("Update")) {
			String department = request.getParameter("edit_department");
			String isactive = request.getParameter("ACTIVE");
			String Etid = request.getParameter("etid");
			int Eid = Integer.parseInt(Etid);
			CoaDAO coaDAO=new CoaDAO();
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				boolean etstatus = true;
				List<HrEmpDepartmentMst> EmpypeList = hrDepartmentService.IsDepartmentlist(plant, department);
				for (HrEmpDepartmentMst hrEmpType : EmpypeList) {
					if(hrEmpType.getID() != Eid) {
						etstatus = false;
					}
				}
				if(etstatus) {
					
					HrEmpDepartmentMst employeetype = hrDepartmentService.getDepartmentById(plant, Eid);
					
					String departmenttype= employeetype.getDEPARTMENT();
					
					employeetype.setDEPARTMENT(department);
					employeetype.setIsActive(isactive);
					employeetype.setUPAT(dateutils.getDate());
					employeetype.setUPBY(username);
					
					hrDepartmentService.updateDepartment(employeetype, username);
					
					
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.UPDATE_DEPARTMENT);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, Etid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",department);
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
					DbBean.CommitTran(ut);
					result = "Department updated successfully.";
					response.sendRedirect("../payroll/department?result="+ result);
				}else {
					DbBean.RollbackTran(ut);
					result = "Department already exist";
					response.sendRedirect("../payroll/editdepartment?ID="+Etid+"&result="+ result);
				}
				
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add Department";
				response.sendRedirect("../payroll/editdepartment?ID="+Etid+"&result="+ result);
			}
	 	    	  
		}
		
		if (action.equalsIgnoreCase("Savepopup")) {
			JSONObject jsonObjectResult = new JSONObject();
			jsonObjectResult = savepopupdepartment(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
			
	}
	
	public JSONObject savepopupdepartment(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		
		String department = StrUtils.fString(request.getParameter("departmentlt"));
		String ukeyet = StrUtils.fString(request.getParameter("ukeylt"));

		MovHisDAO movHisDao = new MovHisDAO();
		JSONObject resultJson = new JSONObject();
		HrDepartmentService hrDepartmentService = new HrDepartmentServiceImpl();
		DateUtils dateutils = new DateUtils();
		UserTransaction ut = null;
		int ltid = 0;
		int duplicate = 0;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			boolean departmentcheck = hrDepartmentService.IsDepartmentExists(plant, department);
	    		if(departmentcheck) {
	    			duplicate = 1;
	    		}else {
	    			HrEmpDepartmentMst hrDepartment = new HrEmpDepartmentMst();
	    			hrDepartment.setPLANT(plant);
	    			hrDepartment.setDEPARTMENT(department);
	    			hrDepartment.setCRAT(dateutils.getDate());
	    			hrDepartment.setCRBY(username);
	    			hrDepartment.setIsActive("Y");
	    			ltid = hrDepartmentService.addDepartment(hrDepartment);
	    			
	    			/*CoaUtil coaUtil = new CoaUtil();
					Hashtable<String, String> accountHt = new Hashtable<>();
					accountHt.put("PLANT", plant);
					accountHt.put("ACCOUNTTYPE", "11");
					accountHt.put("ACCOUNTDETAILTYPE", "30");
					accountHt.put("ACCOUNT_NAME", department);
					accountHt.put("DESCRIPTION", "");
					accountHt.put("ISSUBACCOUNT", "1");
					accountHt.put("SUBACCOUNTNAME", "85");
					accountHt.put("OPENINGBALANCE", "");
					accountHt.put("OPENINGBALANCEDATE", "");
					accountHt.put("CRAT", dateutils.getDateTime());
					accountHt.put("CRBY", username);
					accountHt.put("UPAT", dateutils.getDateTime());
					coaUtil.addAccount(accountHt, plant);*/
	    			
	    			Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_SALARY_TYPE);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, ltid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",department);
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
	    		}

			if(duplicate == 0) {
				if (ltid > 0) {
					DbBean.CommitTran(ut);
					resultJson.put("MESSAGE", "Department created successfully");
					resultJson.put("DEPARTMENT", department);
					resultJson.put("DEPARTMENTID", ltid);
					resultJson.put("STATUS", "SUCCESS");
				} else {
					DbBean.RollbackTran(ut);
					resultJson.put("MESSAGE", "Department creation failed");
					resultJson.put("STATUS", "FAIL");
				}
			}else {
				DbBean.RollbackTran(ut);
				resultJson.put("STATUS", "FAIL");
				resultJson.put("MESSAGE", "Department already exists");
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
