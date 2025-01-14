package com.track.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
import com.track.db.object.HrEmpSalaryMst;
import com.track.db.util.CoaUtil;
import com.track.gates.DbBean;
import com.track.service.HrEmpSalaryService;
import com.track.serviceImplementation.HrEmpSalaryServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class HrSalaryServlet
 */
@WebServlet("/HrSalaryServlet")
public class HrSalaryServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HrSalaryServlet() {
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
		
		HrEmpSalaryService hrEmpSalaryService = new HrEmpSalaryServiceImpl();
		
		if(action.equalsIgnoreCase("GET_SALARY_TYPE_FOR_SUMMARY"))
		{
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			
			try {
				List<HrEmpSalaryMst> SalaryList = hrEmpSalaryService.getAllSalary(plant);
				resultJson.put("SALARYTYPELIST", SalaryList);   
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
		
		if(action.equalsIgnoreCase("GET_SALARY_TYPE_DROPDOWN"))
		{
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        String QUERY= StrUtils.fString(req.getParameter("QUERY"));
			try {
				List<HrEmpSalaryMst> SalaryList = hrEmpSalaryService.IsSalarylistdropdown(plant, QUERY);
				resultJson.put("SALARYTYPELIST", SalaryList);   
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
		
		if(action.equalsIgnoreCase("SALARYTYPE_CHECK"))
		{
			JSONObject resultJson = new JSONObject();
	        String QUERY= StrUtils.fString(req.getParameter("QUERY"));
			try {
				if (hrEmpSalaryService.IsSalaryExists(plant, QUERY)) {
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
		HrEmpSalaryService hrEmpSalaryService = new HrEmpSalaryServiceImpl();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		
		if(action.equalsIgnoreCase("SAVE")) {
			String[] emptype = request.getParameterValues("empSalary");
			String[] PAYROLL_BY_BASIC_SALARY = request.getParameterValues("PAYROLL_BY_BASIC_SALARY");
			//Short ISPAYROLL_BY_BASIC_SALARY = Short.valueOf(((String) request.getParameter("ISPAYROLL_BY_BASIC_SALARY").trim().toString()));
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				 if (emptype != null)    {
		 	    	 for (int i = 0; i < emptype.length; i++){
		 	    		boolean emptypecheck = hrEmpSalaryService.IsSalaryExists(plant, emptype[i]);
		 	    		if(!emptypecheck) {
		 	    			HrEmpSalaryMst hrSalary = new HrEmpSalaryMst();
		 	    			hrSalary.setPLANT(plant);
		 	    			hrSalary.setSALARYTYPE(emptype[i]);
		 	    			hrSalary.setCRAT(dateutils.getDate());
		 	    			hrSalary.setCRBY(username);
		 	    			hrSalary.setIsActive("Y");
		 	    			hrSalary.setISPAYROLL_BY_BASIC_SALARY(Short.valueOf(PAYROLL_BY_BASIC_SALARY[i]));
		 	    			//hrSalary.setISPAYROLL_BY_BASIC_SALARY(ISPAYROLL_BY_BASIC_SALARY);
			 	    		int etid = hrEmpSalaryService.addSalary(hrSalary);
			 	    		
			 	    		CoaUtil coaUtil = new CoaUtil();
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
							CoaDAO coaDAO = new CoaDAO();
							String gcode = coaDAO.GetGCodeById("11", plant);
							String dcode = coaDAO.GetDCodeById("30", plant);
							List<Map<String, String>> listQry = coaDAO.getMaxAccoutCode(plant, "11", "30");
							String maxid = "";
							String atcode = "";
							if(listQry.size() > 0) {
								for (int J = 0; J < listQry.size(); J++) {
									Map<String, String> m = listQry.get(J);
									maxid = m.get("CODE");
								}
							
								int count = Integer.valueOf(maxid);
								atcode = String.valueOf(count+1);
								if(atcode.length() == 1) {
									atcode = "0"+atcode;
								}
							}else {
								atcode = "01";
							}
							String accountCode = gcode+"-"+dcode+atcode;
							accountHt.put("ACCOUNT_CODE", accountCode);
							accountHt.put("CODE", atcode);
							coaUtil.addAccount(accountHt, plant);
			 	    		
			 	    		Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, plant);					
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_SALARY_TYPE);	
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
		 	    			result = emptype[i] + " - Salary Type Already Exists";
		 					response.sendRedirect("../payroll/createsalary?result="+ result);
		 	    		}
		 	    	 }
		 	     }
				 
				
				 
				DbBean.CommitTran(ut);
				result = "Salary Type added successfully.";
				response.sendRedirect("../payroll/employeesalary?result="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add salary type";
				response.sendRedirect("../payroll/createsalary?result="+ result);
			}
	 	    	  
		}
		
		if(action.equalsIgnoreCase("Update")) {
			String emptype = request.getParameter("empSalary");
			String isactive = request.getParameter("ACTIVE");
			String Etid = request.getParameter("etid");
			Short ISPAYROLL_BY_BASIC_SALARY =Short.valueOf((request.getParameter("ISPAYROLL_BY_BASIC_SALARY") != null) ? "1": "0");
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
				List<HrEmpSalaryMst> EmpypeList = hrEmpSalaryService.IsSalarylist(plant, emptype);
				for (HrEmpSalaryMst hrEmpType : EmpypeList) {
					if(hrEmpType.getID() != Eid) {
						etstatus = false;
					}
				}
				if(etstatus) {
					
					HrEmpSalaryMst employeetype = hrEmpSalaryService.getSalaryById(plant, Eid);
					
					String salarytype= employeetype.getSALARYTYPE();
					
					employeetype.setSALARYTYPE(emptype);
					employeetype.setIsActive(isactive);
					employeetype.setISPAYROLL_BY_BASIC_SALARY(ISPAYROLL_BY_BASIC_SALARY);
					employeetype.setUPAT(dateutils.getDate());
					employeetype.setUPBY(username);
					
					hrEmpSalaryService.updateSalary(employeetype, username);
					
					JSONObject coaJson=coaDAO.getCOAByName(plant, salarytype);
					String uid = coaJson.getString("id");
					
					coaDAO.updateTable(plant, uid, emptype, "11", "30", "", "1", "85", "", "", "","0");
					
					
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.UPDATE_SALARY_TYPE);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, Etid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",emptype);
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
					DbBean.CommitTran(ut);
					result = "Salary Type updated successfully.";
					response.sendRedirect("../payroll/employeesalary?result="+ result);
				}else {
					DbBean.RollbackTran(ut);
					result = "Salary Type already exist";
					response.sendRedirect("../payroll/editsalary?ID="+Etid+"&result="+ result);
				}
				
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add Salary Type";
				response.sendRedirect("../payroll/editsalary?ID="+Etid+"&result="+ result);
			}
	 	    	  
		}
		
		if (action.equalsIgnoreCase("Savepopup")) {
			JSONObject jsonObjectResult = new JSONObject();
			jsonObjectResult = savepopupsalarytype(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
			
	}
	
	public JSONObject savepopupsalarytype(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		
		String salarytype = StrUtils.fString(request.getParameter("salarytypelt"));
		Short ISPAYROLL_BY_BASIC_SALARY =Short.valueOf((request.getParameter("ISPAYROLL_BY_BASIC_SALARY") != null) ? "1": "0");
		String ukeyet = StrUtils.fString(request.getParameter("ukeylt"));

		MovHisDAO movHisDao = new MovHisDAO();
		JSONObject resultJson = new JSONObject();
		HrEmpSalaryService hrEmpSalaryService = new HrEmpSalaryServiceImpl();
		DateUtils dateutils = new DateUtils();
		UserTransaction ut = null;
		int ltid = 0;
		int duplicate = 0;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			boolean salarytypecheck = hrEmpSalaryService.IsSalaryExists(plant, salarytype);
	    		if(salarytypecheck) {
	    			duplicate = 1;
	    		}else {
	    			HrEmpSalaryMst hrSalaryType = new HrEmpSalaryMst();
	    			hrSalaryType.setPLANT(plant);
	    			hrSalaryType.setSALARYTYPE(salarytype);
	    			hrSalaryType.setISPAYROLL_BY_BASIC_SALARY(ISPAYROLL_BY_BASIC_SALARY);
	    			hrSalaryType.setCRAT(dateutils.getDate());
	    			hrSalaryType.setCRBY(username);
	    			hrSalaryType.setIsActive("Y");
	    			ltid = hrEmpSalaryService.addSalary(hrSalaryType);
	    			
	    			CoaUtil coaUtil = new CoaUtil();
					Hashtable<String, String> accountHt = new Hashtable<>();
					accountHt.put("PLANT", plant);
					accountHt.put("ACCOUNTTYPE", "11");
					accountHt.put("ACCOUNTDETAILTYPE", "30");
					accountHt.put("ACCOUNT_NAME", salarytype);
					accountHt.put("DESCRIPTION", "");
					accountHt.put("ISSUBACCOUNT", "1");
					accountHt.put("SUBACCOUNTNAME", "85");
					accountHt.put("OPENINGBALANCE", "");
					accountHt.put("OPENINGBALANCEDATE", "");
					accountHt.put("CRAT", dateutils.getDateTime());
					accountHt.put("CRBY", username);
					accountHt.put("UPAT", dateutils.getDateTime());
					CoaDAO coaDAO = new CoaDAO();
					String gcode = coaDAO.GetGCodeById("11", plant);
					String dcode = coaDAO.GetDCodeById("30", plant);
					List<Map<String, String>> listQry = coaDAO.getMaxAccoutCode(plant, "11", "30");
					String maxid = "";
					String atcode = "";
					if(listQry.size() > 0) {
						for (int J = 0; J < listQry.size(); J++) {
							Map<String, String> m = listQry.get(J);
							maxid = m.get("CODE");
						}
					
						int count = Integer.valueOf(maxid);
						atcode = String.valueOf(count+1);
						if(atcode.length() == 1) {
							atcode = "0"+atcode;
						}
					}else {
						atcode = "01";
					}
					String accountCode = gcode+"-"+dcode+atcode;
					accountHt.put("ACCOUNT_CODE", accountCode);
					accountHt.put("CODE", atcode);
					coaUtil.addAccount(accountHt, plant);
	    			
	    			Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_SALARY_TYPE);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, ltid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",salarytype);
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
	    		}

			if(duplicate == 0) {
				if (ltid > 0) {
					DbBean.CommitTran(ut);
					resultJson.put("MESSAGE", "Salary Type created successfully");
					resultJson.put("SALARYTYPE", salarytype);
					resultJson.put("SALARYTYPEID", ltid);
					resultJson.put("ISPAYROLL_BY_BASIC_SALARY", ISPAYROLL_BY_BASIC_SALARY);
					resultJson.put("STATUS", "SUCCESS");
				} else {
					DbBean.RollbackTran(ut);
					resultJson.put("MESSAGE", "Salary Type creation failed");
					resultJson.put("STATUS", "FAIL");
				}
			}else {
				DbBean.RollbackTran(ut);
				resultJson.put("STATUS", "FAIL");
				resultJson.put("MESSAGE", "Salary Type already exists");
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
