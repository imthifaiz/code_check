package com.track.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.EmployeeDAO;
import com.track.dao.MovHisDAO;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrEmpUserInfo;
import com.track.gates.DbBean;
import com.track.gates.defaultsBean;
import com.track.gates.encryptBean;
import com.track.service.HrEmpTypeService;
import com.track.serviceImplementation.HrEmpTypeServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/EmployeeLoginServlet")
public class EmployeeLoginServlet extends HttpServlet implements IMLogger  {

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
		EmployeeDAO EmployeeDAO = new EmployeeDAO();
		
		if(action.equalsIgnoreCase("login")) {
		    String user_id   = request.getParameter("name").trim();
		    String pwd       = request.getParameter("pwd").trim();
		    String rememberme = StrUtils.fString(request.getParameter("remember_me"));
			UserTransaction ut = null;
			String result="";
			try {
				encryptBean encrypt = new encryptBean();
				defaultsBean defaults = new defaultsBean();
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				String enpassword = encrypt.encrypt(pwd);
				HrEmpUserInfo userdetails = EmployeeDAO.employeelogin(user_id, enpassword);
				if(userdetails.getID() > 0) {
			          HttpSession session = request.getSession();
			          session.setAttribute("EMP_LOGIN_USER", user_id);
			          session.setAttribute("EMP_USER_ID",userdetails.getEMPNOID());
			          session.setAttribute("PLANT",userdetails.getPLANT());
			          defaults.insertToLog(session, "User Logging Information","Logged in Successfully");

			          if(rememberme.equalsIgnoreCase("true"))
			          {
			        	  Cookie c = new Cookie("empuserid", user_id.toString());
			              c.setMaxAge(24*60*60);
			              response.addCookie(c);
			          }
			          
			          DbBean.CommitTran(ut);
					  response.sendRedirect("jsp/EmpDashboard.jsp");
				}else {
					DbBean.RollbackTran(ut);	
					result = "Invalid User ID or Password: "+user_id+" ..  Please Try Again with Correct User ID or Password";
					defaults.insertLog(user_id, "User Logging Information",result);
					response.sendRedirect("jsp/EmployeeLogin.jsp?result="+ result);
				}
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Invalid User ID or Password: "+user_id+" ..  Please Try Again with Correct User ID or Password";
				response.sendRedirect("jsp/EmployeeLogin.jsp?result="+ result);
			}
	 	    	  
		}
		
		
		
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		
	}
		

}
