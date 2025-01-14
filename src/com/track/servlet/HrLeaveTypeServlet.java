package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
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
import com.track.dao.EmployeeLeaveDetDAO;
import com.track.dao.HrLeaveTypeDAO;
import com.track.dao.MovHisDAO;
import com.track.db.object.EmployeeLeaveDETpojo;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrLeaveType;
import com.track.db.object.LeaveTypePojo;
import com.track.gates.DbBean;
import com.track.service.HrEmpTypeService;
import com.track.service.HrLeaveTypeService;
import com.track.serviceImplementation.HrEmpTypeServiceImpl;
import com.track.serviceImplementation.HrLeaveTypeServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/HrLeaveTypeServlet")
public class HrLeaveTypeServlet extends HttpServlet implements IMLogger  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("Submit")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		HrLeaveTypeService hrLeaveTypeService = new HrLeaveTypeServiceImpl();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		
		if(action.equalsIgnoreCase("SAVE")) {
			String[] leavetype = request.getParameterValues("leavetype");
			String[] totalentitlement = request.getParameterValues("totalentitlement");
			String[] isnopayleave = request.getParameterValues("isnopayleave");
			String[] carryforward = request.getParameterValues("carryforward");
			String[] employeetypeid = request.getParameterValues("employeetypeid");
			String[] note = request.getParameterValues("note");
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				 if (leavetype != null)    {
		 	    	 for (int i = 0; i < leavetype.length; i++){
		 	    		boolean leaveptypecheck = hrLeaveTypeService.IsLeavetype(plant, leavetype[i], Integer.valueOf(employeetypeid[i]));
		 	    		if(leaveptypecheck) {
		 	    			continue;
		 	    		}else {
		 	    			HrLeaveType hrLeaveType = new HrLeaveType();
		 	    			hrLeaveType.setPLANT(plant);
		 	    			hrLeaveType.setLEAVETYPE(leavetype[i]);
		 	    			hrLeaveType.setTOTALENTITLEMENT(Double.valueOf(totalentitlement[i]));
		 	    			hrLeaveType.setISNOPAYLEAVE(Short.valueOf(isnopayleave[i]));
		 	    			hrLeaveType.setCARRYFORWARD(Double.valueOf(carryforward[i]));
		 	    			hrLeaveType.setEMPLOYEETYPEID(Integer.valueOf(employeetypeid[i]));
		 	    			hrLeaveType.setNOTE(note[i]);
		 	    			hrLeaveType.setCRAT(dateutils.getDate());
		 	    			hrLeaveType.setCRBY(username);
		 	    			int ltid = hrLeaveTypeService.addLeavetype(hrLeaveType);
		 	    			
		 	    			Hashtable htMovHis = new Hashtable();
		 					htMovHis.clear();
		 					htMovHis.put(IDBConstants.PLANT, plant);					
		 					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_LEAVE_TYPE);	
		 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
		 					htMovHis.put("RECID", "");
		 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, ltid);
		 					htMovHis.put(IDBConstants.CREATED_BY, username);		
		 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
		 					htMovHis.put("REMARKS",leavetype[i]+","+employeetypeid[i]);
		 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
		 	    		}
		 	    	 }
		 	     }
				 
				DbBean.CommitTran(ut);
				result = "Leave Type added successfully.";
				response.sendRedirect("../payroll/leavetype?result="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add Leave type.";
				response.sendRedirect("../payroll/createleavetype?result="+ result);
			}
	 	    	  
		}
		
		if(action.equalsIgnoreCase("Update")) {
			String leavetype = request.getParameter("leavetype");
			String totalentitlement = request.getParameter("totalentitlement");
			String isnopayleave = request.getParameter("isnopayleave");
			String carryforward = request.getParameter("carryforward");
			String employeetypeid = request.getParameter("employeetypeid");
			String note = request.getParameter("note");
			String ltid = request.getParameter("ltid");
			int lid = Integer.parseInt(ltid);
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				boolean etstatus = true;
				List<HrLeaveType> leaveList = hrLeaveTypeService.IsLeavetypelist(plant, leavetype,Integer.valueOf(employeetypeid));
				for (HrLeaveType hrLeaveType : leaveList) {
					if(hrLeaveType.getID() != lid) {
						etstatus = false;
					}
				}
				if(etstatus) {
					
					HrLeaveType hrLeaveTypeupdate = hrLeaveTypeService.getLeavetypeById(plant, lid);
					
					hrLeaveTypeupdate.setLEAVETYPE(leavetype);
					hrLeaveTypeupdate.setTOTALENTITLEMENT(Double.valueOf(totalentitlement));
					hrLeaveTypeupdate.setISNOPAYLEAVE(Short.valueOf(isnopayleave));
					hrLeaveTypeupdate.setCARRYFORWARD(Double.valueOf(carryforward));
					hrLeaveTypeupdate.setEMPLOYEETYPEID(Integer.valueOf(employeetypeid));
					hrLeaveTypeupdate.setNOTE(note);
					hrLeaveTypeupdate.setUPAT(dateutils.getDate());
					hrLeaveTypeupdate.setUPBY(username);
					
					hrLeaveTypeService.updateLeavetype(hrLeaveTypeupdate, username);
					
					Hashtable htMovHis = new Hashtable();
 					htMovHis.clear();
 					htMovHis.put(IDBConstants.PLANT, plant);					
 					htMovHis.put("DIRTYPE", TransactionConstants.UPDATE_LEAVE_TYPE);	
 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
 					htMovHis.put("RECID", "");
 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, ltid);
 					htMovHis.put(IDBConstants.CREATED_BY, username);		
 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
 					htMovHis.put("REMARKS",leavetype+","+employeetypeid);
 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
					DbBean.CommitTran(ut);
					result = "Leave Type updated successfully.";
					response.sendRedirect("../payroll/leavetype?result="+ result);
				}else {
					DbBean.RollbackTran(ut);
					result = "Leave Type already exist";
					response.sendRedirect("../payroll/editleavetype?ID="+ltid+"&result="+ result);
				}
				
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add Leave type.";
				response.sendRedirect("../payroll/editleavetype?ID="+ltid+"&result="+ result);
			}
	 	    	  
		}
		
		if (action.equalsIgnoreCase("Savepopup")) {
			JSONObject jsonObjectResult = new JSONObject();
			jsonObjectResult = savepopupleavetype(request, response);
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
		HrLeaveTypeService hrLeaveTypeService = new HrLeaveTypeServiceImpl();
		
		
		if(action.equalsIgnoreCase("LEAVE_TYPE_DELETE"))
		{
			String lvtid= StrUtils.fString(req.getParameter("ID"));
			JSONObject resultJson = new JSONObject();
	        EmployeeLeaveDetDAO EmployeeLeaveDetDAO = new EmployeeLeaveDetDAO();
			try {
				
				if(EmployeeLeaveDetDAO.isleavetypeExists(lvtid,plant)) {
					resultJson.put("STATUS", "NOT OK");
				}else {
					hrLeaveTypeService.DeleteLeavetype(plant, Integer.valueOf(lvtid));
					resultJson.put("STATUS", "OK"); 
				}
			} catch (Exception e) {
				resultJson.put("STATUS", "NOT OK");  
			}
			resp.getWriter().write(resultJson.toString());
			
		}
		
		if(action.equalsIgnoreCase("GET_LEAVE_TYPE"))
		{
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			
			try {
				List<HrLeaveType> LeaveTypeList = hrLeaveTypeService.getAllLeavetype(plant);
				resultJson.put("LEAVETYPELIST", LeaveTypeList);   
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
		
		if(action.equalsIgnoreCase("GET_LEAVE_TYPE_DROPDOWN"))
		{
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        String QUERY= StrUtils.fString(req.getParameter("QUERY"));
	        String EMPTYPEID= StrUtils.fString(req.getParameter("EMPTYPEID"));
			try {
				List<HrLeaveType> LeaveTypeList = hrLeaveTypeService.IsLeavetypelistdropdown(plant, QUERY, Integer.valueOf(EMPTYPEID));
				resultJson.put("LEAVETYPELIST", LeaveTypeList);   
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
		
		if(action.equalsIgnoreCase("GET_LEAVE_TYPE_FOR_SUMMARY"))
		{
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        HrEmpTypeService HrEmpTypeService = new HrEmpTypeServiceImpl();
	        
			List<LeaveTypePojo> leavetyppojolist = new ArrayList<LeaveTypePojo>();
			try {
				List<HrLeaveType> LeaveTypeList = hrLeaveTypeService.getAllLeavetype(plant);
				for (HrLeaveType hrLeaveType : LeaveTypeList) {
					HrEmpType hrEmpType = HrEmpTypeService.getEmployeetypeById(plant, hrLeaveType.getEMPLOYEETYPEID());
					LeaveTypePojo leaveTypePojo = new LeaveTypePojo();
					leaveTypePojo.setPLANT(hrLeaveType.getPLANT());
					leaveTypePojo.setID(hrLeaveType.getID());
					leaveTypePojo.setLEAVETYPE(hrLeaveType.getLEAVETYPE());
					leaveTypePojo.setEMPLOYEETYPEID(hrLeaveType.getEMPLOYEETYPEID());;
					leaveTypePojo.setEMPLOYEETYPE(hrEmpType.getEMPLOYEETYPE());
					leaveTypePojo.setTOTALENTITLEMENT(hrLeaveType.getTOTALENTITLEMENT());
					leaveTypePojo.setCARRYFORWARD(hrLeaveType.getCARRYFORWARD());
					leaveTypePojo.setNOTE(hrLeaveType.getNOTE());
					leaveTypePojo.setISNOPAYLEAVE(hrLeaveType.getISNOPAYLEAVE());
					leavetyppojolist.add(leaveTypePojo);
				}
				
				resultJson.put("LEAVETYPELIST", leavetyppojolist);   
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
		
		
		if(action.equalsIgnoreCase("CHECK_USERID"))
		{
			EmployeeDAO EmployeeDAO = new EmployeeDAO();
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        String logname= StrUtils.fString(req.getParameter("username"));
			try {
				boolean userstatus = EmployeeDAO.IsUserName(logname);
				if(userstatus) {
					JSONObject resultJsonInt = new JSONObject();
		            resultJson.put("STATUS",  "NOT OK");
				}else {
		            resultJson.put("STATUS",  "OK");
				}
				
			} catch (Exception e) {
				resultJson.put("STATUS",  "NOT OK");
			}
			resp.getWriter().write(resultJson.toString());
			
		}
		
		if(action.equalsIgnoreCase("CHECK_USERID_EDIT"))
		{
			EmployeeDAO EmployeeDAO = new EmployeeDAO();
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        String logname= StrUtils.fString(req.getParameter("username"));
	        String uid= StrUtils.fString(req.getParameter("uid"));
			try {
				boolean userstatus = EmployeeDAO.IsUserNameEdit(logname, uid);
				if(userstatus) {
					JSONObject resultJsonInt = new JSONObject();
		            resultJson.put("STATUS",  "NOT OK");
				}else {
		            resultJson.put("STATUS",  "OK");
				}
				
			} catch (Exception e) {
				resultJson.put("STATUS",  "NOT OK");
			}
			resp.getWriter().write(resultJson.toString());
			
		}
		
		if(action.equalsIgnoreCase("GET_EMP_LEAVE_TYPE_DROPDOWN"))
		{
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        EmployeeLeaveDetDAO employeeLeaveDetDAO= new EmployeeLeaveDetDAO();
	        String LYEAR= StrUtils.fString(req.getParameter("LYEAR"));
	        String EMPTYPEID= StrUtils.fString(req.getParameter("EMPTYPEID"));
			try {
				List<EmployeeLeaveDETpojo> LeaveTypeList = employeeLeaveDetDAO.EmployeeLeavedetlistpojo(plant,Integer.valueOf(EMPTYPEID), LYEAR);
				resultJson.put("LEAVETYPELIST", LeaveTypeList);   
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
		
		if(action.equalsIgnoreCase("CHECK_LEAVE_TYPE"))
		{
			String emptypeid= StrUtils.fString(req.getParameter("EMPTYPEID"));
			String leavetype= StrUtils.fString(req.getParameter("LEAVETYPE"));
			JSONObject resultJson = new JSONObject();
			try {
				boolean leavetypecheck = hrLeaveTypeService.IsLeavetype(plant, leavetype, Integer.valueOf(emptypeid));
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
		
	}
	
	public JSONObject savepopupleavetype(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		
		String leavetype = StrUtils.fString(request.getParameter("leavetypelt"));
		String totalentitlement = StrUtils.fString(request.getParameter("totalentitlementlt"));
		String isnopayleave = StrUtils.fString(request.getParameter("isnopayleavelt"));
		String carryforward = StrUtils.fString(request.getParameter("carryforwardlt"));
		String employeetypeid = StrUtils.fString(request.getParameter("employeetypeidlt"));
		String note = StrUtils.fString(request.getParameter("notelt"));
		String ukeyet = StrUtils.fString(request.getParameter("ukeylt"));

		MovHisDAO movHisDao = new MovHisDAO();
		JSONObject resultJson = new JSONObject();
		HrLeaveTypeService hrLeaveTypeService = new HrLeaveTypeServiceImpl();
		DateUtils dateutils = new DateUtils();
		UserTransaction ut = null;
		int ltid = 0;
		int duplicate = 0;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			boolean leaveptypecheck = hrLeaveTypeService.IsLeavetype(plant, leavetype, Integer.valueOf(employeetypeid));
	    		if(leaveptypecheck) {
	    			duplicate = 1;
	    		}else {
	    			HrLeaveType hrLeaveType = new HrLeaveType();
	    			hrLeaveType.setPLANT(plant);
	    			hrLeaveType.setLEAVETYPE(leavetype);
	    			hrLeaveType.setTOTALENTITLEMENT(Double.valueOf(totalentitlement));
	    			hrLeaveType.setISNOPAYLEAVE(Short.valueOf(isnopayleave));
	    			hrLeaveType.setCARRYFORWARD(Double.valueOf(carryforward));
	    			hrLeaveType.setEMPLOYEETYPEID(Integer.valueOf(employeetypeid));
	    			hrLeaveType.setNOTE(note);
	    			hrLeaveType.setCRAT(dateutils.getDate());
	    			hrLeaveType.setCRBY(username);
	    			ltid = hrLeaveTypeService.addLeavetype(hrLeaveType);
	    			
	    			Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_LEAVE_TYPE);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, ltid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",leavetype+","+employeetypeid);
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
	    		}

			if(duplicate == 0) {
				if (ltid > 0) {
					DbBean.CommitTran(ut);
					resultJson.put("MESSAGE", "Leave Type created successfully");
					resultJson.put("LEAVETYPE", leavetype);
					resultJson.put("LEAVETYPEID", ltid);
					resultJson.put("TDAYS", totalentitlement);
					resultJson.put("STATUS", "SUCCESS");
				} else {
					DbBean.RollbackTran(ut);
					resultJson.put("MESSAGE", "Leave Type creation failed");
					resultJson.put("STATUS", "FAIL");
				}
			}else {
				DbBean.RollbackTran(ut);
				resultJson.put("STATUS", "FAIL");
				resultJson.put("MESSAGE", "Leave Type already exists");
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
