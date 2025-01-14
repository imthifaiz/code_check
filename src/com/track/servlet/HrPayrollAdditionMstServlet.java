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
import javax.transaction.UserTransaction;

import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.CoaDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EmployeeLeaveDetDAO;
import com.track.dao.HrPayrollDETDAO;
import com.track.dao.MovHisDAO;
import com.track.db.object.EmployeeLeaveDETpojo;
import com.track.db.object.HrDeductionDet;
import com.track.db.object.HrDeductionHdr;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrLeaveType;
import com.track.db.object.HrPayrollAdditionMst;
import com.track.db.object.LeaveTypePojo;
import com.track.db.util.CoaUtil;
import com.track.gates.DbBean;
import com.track.service.HrEmpTypeService;
import com.track.service.HrLeaveTypeService;
import com.track.service.HrPayrollAdditionMstService;
import com.track.serviceImplementation.HrEmpTypeServiceImpl;
import com.track.serviceImplementation.HrLeaveTypeServiceImpl;
import com.track.serviceImplementation.HrPayrollAdditionMstServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/HrPayrollAdditionMstServlet")
public class HrPayrollAdditionMstServlet extends HttpServlet implements IMLogger  {

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
		
		HrPayrollAdditionMstService hrPayrollAdditionMstService = new HrPayrollAdditionMstServiceImpl();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		
		if(action.equalsIgnoreCase("SAVE")) {
			String[] payname = request.getParameterValues("add_name");
			String[] paydescription = request.getParameterValues("add_description");
			String[] isrecoverable = request.getParameterValues("isrecoverable");
			String[] isclaim = request.getParameterValues("isclaim");

			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				 if (payname != null)    {
		 	    	 for (int i = 0; i < payname.length; i++){
		 	    		boolean paynamecheck = hrPayrollAdditionMstService.ispayrolladditionmst(plant, payname[i]);
		 	    		if(paynamecheck) {
		 	    			continue;
		 	    		}else {
		 	    			HrPayrollAdditionMst hrPayrollAdditionMst = new HrPayrollAdditionMst();
		 	    			hrPayrollAdditionMst.setPLANT(plant);
		 	    			hrPayrollAdditionMst.setADDITION_NAME(payname[i]);
		 	    			hrPayrollAdditionMst.setADDITION_DESCRIPTION(paydescription[i]);
		 	    			hrPayrollAdditionMst.setISDEDUCTION(Short.valueOf(isrecoverable[i]));
		 	    			hrPayrollAdditionMst.setISCLAIM(Short.valueOf(isclaim[i]));
		 	    			hrPayrollAdditionMst.setCRBY(username);
		 	    			int pid = hrPayrollAdditionMstService.addpayrolladditionmst(hrPayrollAdditionMst);
		 	    			
		 	    			if(Short.valueOf(isrecoverable[i]) == 0) {
		 	    				CoaUtil coaUtil = new CoaUtil();
								Hashtable<String, String> accountHt = new Hashtable<>();
								accountHt.put("PLANT", plant);
								accountHt.put("ACCOUNTTYPE", "11");
								accountHt.put("ACCOUNTDETAILTYPE", "30");
								accountHt.put("ACCOUNT_NAME", payname[i]);
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
		 	    			}
		 	    			
		 	    			Hashtable htMovHis = new Hashtable();
		 					htMovHis.clear();
		 					htMovHis.put(IDBConstants.PLANT, plant);					
		 					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYROLL_ADDITION_MASTER);	
		 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
		 					htMovHis.put("RECID", "");
		 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, pid);
		 					htMovHis.put(IDBConstants.CREATED_BY, username);		
		 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
		 					htMovHis.put("REMARKS",payname[i]+","+paydescription[i]);
		 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
		 	    		}
		 	    	 }
		 	     }
				 
				DbBean.CommitTran(ut);
				result = "Payroll addition added successfully.";
				response.sendRedirect("../payroll/addsummary?result="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add Payroll addition";
				response.sendRedirect("../payroll/createadd?result="+ result);
			}
	 	    	  
		}
		
		if(action.equalsIgnoreCase("Update")) {
			String payname = request.getParameter("add_name");
			String paydescription = request.getParameter("add_description");
			String isrecoverable = request.getParameter("isrecoverable");
			String isclaim = request.getParameter("isclaim");
			String pid = request.getParameter("pid");
			CoaDAO coaDAO=new CoaDAO();
			int lid = Integer.parseInt(pid);
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				boolean etstatus = true;
				List<HrPayrollAdditionMst> hrPayrollAdditionMstList = hrPayrollAdditionMstService.payrolladditionmstbyname(plant, payname);
				for (HrPayrollAdditionMst hrPayrollAdditionMst : hrPayrollAdditionMstList) {
					if(hrPayrollAdditionMst.getID() != lid) {
						etstatus = false;
					}
				}
				if(etstatus) {
					
					HrPayrollAdditionMst hrPayrollAdditionMstupdate = hrPayrollAdditionMstService.getpayrolladditionmstById(plant, lid);
					
					hrPayrollAdditionMstupdate.setADDITION_NAME(payname);
					hrPayrollAdditionMstupdate.setADDITION_DESCRIPTION(paydescription);
					hrPayrollAdditionMstupdate.setISDEDUCTION(Short.valueOf(isrecoverable));
					hrPayrollAdditionMstupdate.setISCLAIM(Short.valueOf(isclaim));
				
					hrPayrollAdditionMstService.updatepayrolladditionmst(hrPayrollAdditionMstupdate, username);

					Hashtable htMovHis = new Hashtable();
 					htMovHis.clear();
 					htMovHis.put(IDBConstants.PLANT, plant);					
 					htMovHis.put("DIRTYPE", TransactionConstants.UPDATE_PAYROLL_ADDITION_MASTER);	
 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
 					htMovHis.put("RECID", "");
 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, lid);
 					htMovHis.put(IDBConstants.CREATED_BY, username);		
 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
 					htMovHis.put("REMARKS",payname+","+paydescription);
 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
					DbBean.CommitTran(ut);
					result = "Payroll addition updated successfully.";
					response.sendRedirect("../payroll/addsummary?result="+ result);
				}else {
					DbBean.RollbackTran(ut);
					result = "Payroll addition already exist";
					response.sendRedirect("../payroll/editadd?ID="+pid+"&result="+ result);
				}
				
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't update Payroll addition.";
				response.sendRedirect("../payroll/editadd?ID="+pid+"&result="+ result);
			}
	 	    	  
		}
		
		if(action.equalsIgnoreCase("SavePopup")) {
			String payname = request.getParameter("pop_add_name");
			String paydescription = request.getParameter("pop_add_description");
			String isrecoverable = request.getParameter("pop_isrecoverable");
			String isclaim = request.getParameter("pop_isclaim");

			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        UserTransaction ut = null;
			try {
				
				ut = DbBean.getUserTranaction();				
				ut.begin();
				
				boolean paynamecheck = hrPayrollAdditionMstService.ispayrolladditionmst(plant, payname);
 	    		if(paynamecheck) {
 	    			DbBean.RollbackTran(ut);
 					resultJson.put("STATUS", "NOT OK");
 					resultJson.put("MESSAGE", "payroll addition master already exist");
 					resultJson.put("PAGE", "-");
 	    		}else {
 	    			HrPayrollAdditionMst hrPayrollAdditionMst = new HrPayrollAdditionMst();
 	    			hrPayrollAdditionMst.setPLANT(plant);
 	    			hrPayrollAdditionMst.setADDITION_NAME(payname);
 	    			hrPayrollAdditionMst.setADDITION_DESCRIPTION(paydescription);
 	    			hrPayrollAdditionMst.setISDEDUCTION(Short.valueOf(isrecoverable));
 	    			hrPayrollAdditionMst.setISCLAIM(Short.valueOf(isclaim));
 	    			hrPayrollAdditionMst.setCRBY(username);
 	    			int pid = hrPayrollAdditionMstService.addpayrolladditionmst(hrPayrollAdditionMst);
 	    			
 	    			if(Short.valueOf(isrecoverable) == 0) {
 	    				CoaUtil coaUtil = new CoaUtil();
						Hashtable<String, String> accountHt = new Hashtable<>();
						accountHt.put("PLANT", plant);
						accountHt.put("ACCOUNTTYPE", "11");
						accountHt.put("ACCOUNTDETAILTYPE", "30");
						accountHt.put("ACCOUNT_NAME", payname);
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
							for (int i = 0; i < listQry.size(); i++) {
								Map<String, String> m = listQry.get(i);
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
 	    			}
 	    			
 	    			Hashtable htMovHis = new Hashtable();
 					htMovHis.clear();
 					htMovHis.put(IDBConstants.PLANT, plant);					
 					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYROLL_ADDITION_MASTER);	
 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
 					htMovHis.put("RECID", "");
 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, pid);
 					htMovHis.put(IDBConstants.CREATED_BY, username);		
 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
 					htMovHis.put("REMARKS",payname+","+paydescription);
 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
 	    		}
				
				
				DbBean.CommitTran(ut);
				resultJson.put("STATUS", "OK");
				resultJson.put("MESSAGE", payname);
				if(Short.valueOf(isrecoverable) == 1) {
					resultJson.put("PAGE", "DEDUCTION");
				}else if(Short.valueOf(isclaim) == 1){
					resultJson.put("PAGE", "CLAIM");
				}else {
					resultJson.put("PAGE", "PAYROLL");
				}
				
				
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				resultJson.put("STATUS", "NOT OK");
				resultJson.put("MESSAGE", "Unable to add payroll addition");
				resultJson.put("PAGE", "-");

			}
			response.getWriter().write(resultJson.toString());
	 	    	  
		}
		
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = StrUtils.fString(req.getParameter("CMD")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		
		HrPayrollAdditionMstService hrPayrollAdditionMstService = new HrPayrollAdditionMstServiceImpl();
		
		if(action.equalsIgnoreCase("GET_PAYROLL_ADDTION_MST"))
		{
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			
			try {
				List<HrPayrollAdditionMst> HrPayrollAdditionMstList = hrPayrollAdditionMstService.getAllpayrolladditionmst(plant);
				resultJson.put("PAYADDMSTLIST", HrPayrollAdditionMstList);   
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
		
		if(action.equalsIgnoreCase("GET_PAYROLL_ADDTION_MST_DROPDOWN"))
		{
			
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        String QUERY= StrUtils.fString(req.getParameter("QUERY"));
	        String DTYPE= StrUtils.fString(req.getParameter("DTYPE"));
			try {
				List<HrPayrollAdditionMst> HrPayrollAdditionMstList = new ArrayList<HrPayrollAdditionMst>();
				if(DTYPE.equalsIgnoreCase("deduct")) {
					HrPayrollAdditionMstList=hrPayrollAdditionMstService.payrolladditionmstdeduct(plant, QUERY);
				}else if(DTYPE.equalsIgnoreCase("add")){
					HrPayrollAdditionMstList=hrPayrollAdditionMstService.payrolladditionmstadd(plant, QUERY);
				}else if(DTYPE.equalsIgnoreCase("claim")){
					HrPayrollAdditionMstList=hrPayrollAdditionMstService.payrolladditionmstclaim(plant, QUERY);
				}else{
					HrPayrollAdditionMstList=hrPayrollAdditionMstService.payrolladditionmstall(plant, QUERY);
				}
				
				resultJson.put("PAYADDMSTLIST", HrPayrollAdditionMstList);   
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
		
		
		if(action.equalsIgnoreCase("CHECK_PAYADDITION_MST"))
		{
			String addname= StrUtils.fString(req.getParameter("ADDNAME"));
			JSONObject resultJson = new JSONObject();
			try {
				boolean leavetypecheck = hrPayrollAdditionMstService.ispayrolladditionmst(plant, addname);
				if(leavetypecheck) {
					resultJson.put("STATUS", "NOT OK");   
				}else {
					resultJson.put("STATUS", "OK");   
				}
			} catch (Exception e) {
				resultJson.put("STATUS", "NOT OK");  
			}
			resp.getWriter().write(resultJson.toString());	
		}
		
		
		if(action.equalsIgnoreCase("DELETE_ADDITION")) {
			String pid = req.getParameter("ID");
			
			HrPayrollDETDAO hrPayrollDETDAO = new HrPayrollDETDAO();
			
			int lid = Integer.parseInt(pid);
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				HrPayrollAdditionMst hrPayrollAdditionMst = hrPayrollAdditionMstService.getpayrolladditionmstById(plant, lid);
				boolean check = hrPayrollDETDAO.Ispayrolldetmaster(plant, hrPayrollAdditionMst.getADDITION_NAME());

				if(check) {
					
					DbBean.RollbackTran(ut);
					result = "Addition already processed in pay slip not allow to delete";
					resp.sendRedirect("../payroll/editadd?ID="+pid+"&resultnew="+ result);
				}else {
					hrPayrollAdditionMstService.Deletepayrolladditionmst(plant, lid);
					
					Hashtable htMovHis = new Hashtable();
 					htMovHis.clear();
 					htMovHis.put(IDBConstants.PLANT, plant);					
 					htMovHis.put("DIRTYPE", TransactionConstants.UPDATE_PAYROLL_ADDITION_MASTER);	
 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
 					htMovHis.put("RECID", "");
 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, lid);
 					htMovHis.put(IDBConstants.CREATED_BY, username);		
 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
 					htMovHis.put("REMARKS",hrPayrollAdditionMst.getADDITION_NAME()+","+hrPayrollAdditionMst.getADDITION_DESCRIPTION());
 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
					DbBean.CommitTran(ut);
					result = "Payroll addition deleted successfully.";
					resp.sendRedirect("../payroll/addsummary?result="+ result);
				}
				
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't delete Payroll addition.";
				resp.sendRedirect("../payroll/editadd?ID="+pid+"&resultnew="+ result);
			}
	 	    	  
		}
		
	}

}
