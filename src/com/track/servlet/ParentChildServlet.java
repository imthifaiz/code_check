package com.track.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
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
import com.track.dao.ParentChildCmpDetDAO;
import com.track.dao.PlantMstDAO;
import com.track.db.object.HrEmpSalaryMst;
import com.track.db.object.HrHolidayMst;
import com.track.db.object.ParentChildCmpDet;
import com.track.db.util.CoaUtil;
import com.track.gates.DbBean;
import com.track.service.HrEmpSalaryService;
import com.track.service.ParentChildCmpDetService;
import com.track.serviceImplementation.HrEmpSalaryServiceImpl;
import com.track.serviceImplementation.ParentChildCmpDetServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*@WebServlet("/ParentChildServlet")*/
@WebServlet("/Parentchildcmp/*")
public class ParentChildServlet extends HttpServlet implements IMLogger {
	
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ParentChildServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		JSONObject jsonObjectResult = new JSONObject();
		ParentChildCmpDetService ParentChildCmp = new ParentChildCmpDetServiceImpl();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		if (request.getSession().isNew() || request.getSession().getAttribute("LOGIN_USER") == null) // Invalid Session
		{
			request.getSession().invalidate();
			System.out.println("New Session Divert it to Index Page");
			response.sendRedirect("../login");
			return;
		}
		
		if (action.equalsIgnoreCase("new")) {
			try {
				String result = StrUtils.fString(request.getParameter("result"));
				String resultok = StrUtils.fString(request.getParameter("resultok"));
				request.setAttribute("result", result);
				request.setAttribute("resultok", resultok);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/Parentchildcmp.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (action.equalsIgnoreCase("save")) {
			try {
				String result = StrUtils.fString(request.getParameter("result"));
				String resultok = StrUtils.fString(request.getParameter("resultok"));
				request.setAttribute("result", result);
				request.setAttribute("resultok", resultok);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/Parentchildcmp.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		/*String action1 = StrUtils.fString(request.getParameter("Submit")).trim();
		String action = StrUtils.fString(request.getParameter("CMD")).trim();*/
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		HrEmpSalaryService hrEmpSalaryService = new HrEmpSalaryServiceImpl();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		ParentChildCmpDetService ParentChildCmp = new ParentChildCmpDetServiceImpl();
		JSONObject jsonObjectResult = new JSONObject();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		
		if (request.getSession().isNew() || request.getSession().getAttribute("LOGIN_USER") == null) // Invalid Session
		{
			request.getSession().invalidate();
			System.out.println("New Session Divert it to Index Page");
			response.sendRedirect("../login");
			return;
		}
		
		if (action.equalsIgnoreCase("new")) {
			try {
				String result = StrUtils.fString(request.getParameter("result"));
				String resultok = StrUtils.fString(request.getParameter("resultok"));
				request.setAttribute("result", result);
				request.setAttribute("resultok", resultok);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/Parentchildcmp.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (action.equalsIgnoreCase("GET_PARENT_PLANT_DROPDOWN")) {
			jsonObjectResult = this.getplantdropdownParent(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}

		if (action.equalsIgnoreCase("GET_PARENT_CHILD_PLANT_DROPDOWN")) {
			jsonObjectResult = this.getplantdropdownParentChild(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		
		if (action.equalsIgnoreCase("GET_CHILD_PLANT_DROPDOWN")) {
			jsonObjectResult = this.getplantdropdownChild(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		
		if(action.equalsIgnoreCase("GET_PARENT_PLANT_DATA"))
		{
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			
			try {
				String parentCompany = request.getParameter("parent");
				List<ParentChildCmpDet> ParentChildCmpList = ParentChildCmp.getAllParentChildCmpDetdropdown(parentCompany, "");
				resultJson.put("CHILDLIST", ParentChildCmpList);   
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resultJson.put("CHILDLIST", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			
			
			response.getWriter().write(resultJson.toString());
			
		}
		
		if(action.equalsIgnoreCase("GET_PARENT_CHILD_MST"))
		{
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        ParentChildCmpDetDAO parentChildCmpDetDAO = new ParentChildCmpDetDAO();
			try {
				List<ParentChildCmpDet> ParentList = parentChildCmpDetDAO.getAllParentChildCmpDetPARENT();
				List<ParentChildCmpDet> ParentChildCmpList = ParentChildCmp.getAllParentChildCmpDet();
				for (ParentChildCmpDet parentChildCmpDet : ParentList) {
					String Pname = plantMstDAO.getcmpyname(parentChildCmpDet.getPARENT_PLANT());
					List<ParentChildCmpDet> ParentChildCmpListfilterd = ParentChildCmpList.stream().filter((p)->p.getPARENT_PLANT().equals(parentChildCmpDet.getPARENT_PLANT())).collect(Collectors.toList());
					String child ="";
					int ci = 0;
					parentChildCmpDet.setPARENT_PLANT(parentChildCmpDet.getPARENT_PLANT()+"-"+Pname);
					for (ParentChildCmpDet ChildCmpDet : ParentChildCmpListfilterd) {
						String Cname = plantMstDAO.getcmpyname(ChildCmpDet.getCHILD_PLANT());
						
						if(ci == 0) {
							child = ChildCmpDet.getCHILD_PLANT()+"-"+Cname;
						}else {
							child +=", "+ChildCmpDet.getCHILD_PLANT()+"-"+Cname;
						}
						ci++;
					}
					parentChildCmpDet.setCHILD_PLANT(child);
				}
				
				
				JSONObject resultJsonInt = new JSONObject();
				resultJson.put("PCLIST", ParentList);   
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resultJson.put("PCLIST", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			response.getWriter().write(resultJson.toString());
			
		}
		if(action.equalsIgnoreCase("save")) {
			String[] childCompany = request.getParameterValues("childCompany");
			String[] ischildCompany = request.getParameterValues("ischild"); //imti added
			String parentCompany = request.getParameter("parent_company");
			UserTransaction ut = null;
			String result="";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				if (childCompany != null) {
					boolean pccheck = ParentChildCmp.IsParentChildCmpDet(parentCompany);
					if (pccheck) {
						ParentChildCmp.DeleteParentChildCmpDetByParent(parentCompany);
					}
					for (int i = 0; i < childCompany.length; i++) {

						ParentChildCmpDet parentChildCmpDet = new ParentChildCmpDet();
						parentChildCmpDet.setPARENT_PLANT(parentCompany);
						parentChildCmpDet.setCHILD_PLANT(childCompany[i]);
						parentChildCmpDet.setISCHILD_AS_PARENT(Short.parseShort(ischildCompany[i]));
						parentChildCmpDet.setCRAT(dateutils.getDateTime());
						parentChildCmpDet.setCRBY(username);
						parentChildCmpDet.setUPAT(dateutils.getDateTime());
						parentChildCmpDet.setUPBY(username);

						ParentChildCmp.addParentChildCmpDet(parentChildCmpDet);

						/*
						 * Hashtable htMovHis = new Hashtable(); htMovHis.clear();
						 * htMovHis.put(IDBConstants.PLANT, plant); htMovHis.put("DIRTYPE",
						 * TransactionConstants.CREATE_SALARY_TYPE);
						 * htMovHis.put(IDBConstants.TRAN_DATE,
						 * dateutils.getDateinyyyy_mm_dd(dateutils.getDate())); htMovHis.put("RECID",
						 * ""); htMovHis.put(IDBConstants.MOVHIS_ORDNUM, etid);
						 * htMovHis.put(IDBConstants.CREATED_BY, username);
						 * htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						 * htMovHis.put("REMARKS",emptype[i]); movHisDao.insertIntoMovHis(htMovHis); //
						 * Insert MOVHIS
						 */
					}
					result = "Parent Company added successfully.";
				} else {
					result = "Parent Company deleted successfully.";
					ParentChildCmp.DeleteParentChildCmpDetByParent(parentCompany);
				}
				 
				
				 
				DbBean.CommitTran(ut);
				//result = "Parent Company added successfully.";
				//response.sendRedirect("jsp/Parentchildcmp.jsp?resultok="+ result);
				request.setAttribute("resultok", result);
				request.setAttribute("result", "");
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/Parentchildcmp.jsp");
				rd.forward(request, response);  
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				request.setAttribute("resultok", "");
				request.setAttribute("result", result);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/Parentchildcmp.jsp");
				rd.forward(request, response);  
			}
			
		}
		

		
		
			
	}
	
	private JSONObject getplantdropdownParent(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        PlantMstDAO plantMstDAO = new PlantMstDAO();
        try {
             //  String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ///////////////////////
               
               ArrayList arrCust = plantMstDAO.getPlantMstlistdropdownParent(QUERY);
               if (arrCust.size() > 0) {
               for(int i =0; i<arrCust.size(); i++) {
                   Map arrCustLine = (Map)arrCust.get(i);
                   JSONObject resultJsonInt = new JSONObject();
                   resultJsonInt.put("PLANT", (String)arrCustLine.get("PLANT"));
                   String name = plantMstDAO.getcmpyname((String)arrCustLine.get("PLANT"));
                   resultJsonInt.put("NAME", name);
                   jsonArray.add(resultJsonInt);
               }
               }else {
            	   JSONObject resultJsonInt = new JSONObject();
                   jsonArray.add("");
                   resultJson.put("footermaster", jsonArray);
               }
               resultJson.put("PLANTMST", jsonArray);
               ///////////////////////              
        } catch (Exception e) {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
		}

	private JSONObject getplantdropdownParentChild(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String QUERY= StrUtils.fString(request.getParameter("QUERY"));
			request.getSession().setAttribute("RESULT","");
			boolean mesflag = false;
			///////////////////////
			
			ArrayList arrCust = plantMstDAO.getPlantMstlistdropdownParentChild(PLANT,QUERY);
			if (arrCust.size() > 0) {
				for(int i =0; i<arrCust.size(); i++) {
					Map arrCustLine = (Map)arrCust.get(i);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("PLANT", (String)arrCustLine.get("PLANT"));
					//String name = plantMstDAO.getcmpyname((String)arrCustLine.get("PLANT"));
					//resultJsonInt.put("NAME", name);
					resultJsonInt.put("NAME", (String)arrCustLine.get("PLNTDESC"));
					jsonArray.add(resultJsonInt);
				}
			}else {
				JSONObject resultJsonInt = new JSONObject();
				jsonArray.add("");
				resultJson.put("footermaster", jsonArray);
			}
			resultJson.put("PLANTMST", jsonArray);
			///////////////////////              
		} catch (Exception e) {
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getplantdropdownChild(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        PlantMstDAO plantMstDAO = new PlantMstDAO();
        try {
             //  String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ///////////////////////
               
               ArrayList arrCust = plantMstDAO.getPlantMstlistdropdownChild(QUERY);
               if (arrCust.size() > 0) {
               for(int i =0; i<arrCust.size(); i++) {
                   Map arrCustLine = (Map)arrCust.get(i);
                   JSONObject resultJsonInt = new JSONObject();
                   resultJsonInt.put("PLANT", (String)arrCustLine.get("PLANT"));
                   String name = plantMstDAO.getcmpyname((String)arrCustLine.get("PLANT"));
                   resultJsonInt.put("NAME", name);
                   jsonArray.add(resultJsonInt);
               }
               }else {
            	   JSONObject resultJsonInt = new JSONObject();
                   jsonArray.add("");
                   resultJson.put("footermaster", jsonArray);
               }
               resultJson.put("PLANTMST", jsonArray);
               ///////////////////////              
        } catch (Exception e) {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
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
