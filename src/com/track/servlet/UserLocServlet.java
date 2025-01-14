package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.MovHisDAO;
import com.track.dao.UserAssignedLocDAO;
import com.track.db.util.LocUtil;
import com.track.db.util.UserLocUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.gates.userBean;

public class UserLocServlet extends HttpServlet implements IMLogger {
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private boolean printLog = MLoggerConstant.OrderTypeServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.OrderTypeServlet_PRINTPLANTMASTERINFO;
	String action = "";
	DateUtils dateutils = new DateUtils();
	UserLocUtil _userLocUtil = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_userLocUtil = new UserLocUtil();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {

			action = request.getParameter("action").trim();
			System.out.println("action"+action);
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		       
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			_userLocUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			
			//imti added for AssignUserLocation.jsp
			if (action.equalsIgnoreCase("Assignloc")) {
				try {
						String result = assignUserLocation(request, response);
						response.sendRedirect("../userlocation/assignuser?action=SHOW_RESULT&result="+ result);
				} catch (Exception ex) {
						this.mLogger.exception(this.printLog, "", ex);
						String result = "<font class = " + IConstants.FAILED_COLOR+ ">Exception : " + ex.getMessage() + "</font>";
						response.sendRedirect("../userlocation/assignuser?action=SHOW_RESULT_VALUE&result="+ result);
				}
			}
			//imti end
			
			
			//imti added for AssignInventoryLocation.jsp
			if (action.equalsIgnoreCase("Assigninv")) {
				try {
					String result = assignInvLocation(request, response);
					response.sendRedirect("../invlocation/assignInvLocation?action=SHOW_RESULT&result="+ result);
				} catch (Exception ex) {
					this.mLogger.exception(this.printLog, "", ex);
					String result = "<font class = " + IConstants.FAILED_COLOR+ ">Exception : " + ex.getMessage() + "</font>";
					response.sendRedirect("../invlocation/assignInvLocation?action=SHOW_RESULT_VALUE&result="+ result);
				}
			}
			//imti end

			
			 if (action.equalsIgnoreCase("Assign")) {
               
				String result = assignUserLoc(request, response);
				response.sendRedirect("jsp/userAssignedLoc.jsp?action=SHOW_RESULT&result="
								+ result);

			} 

			else if (action.equalsIgnoreCase("UnAssign")) {

				String result = unAssignuserLoc(request, response);
				response
						.sendRedirect("jsp/userAssignedLoc.jsp?action=SHOW_RESULT&result="
								+ result);
			}
		} catch (Exception ex) {
			this.mLogger.exception(this.printLog, "", ex);
			String result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Exception : " + ex.getMessage() + "</font>";
			response
					.sendRedirect("jsp/userAssignedLoc.jsp?action=SHOW_RESULT_VALUE&result="
							+ result);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	//IMTI ASSIGN START
	private String assignUserLocation(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException,Exception {
		String result = "";
		String[] chkdDoNo = request.getParameterValues("chkdDoNo");
	
		try {
	        userBean _userBean      = new userBean();
		    _userBean.setmLogger(mLogger);
		    LocUtil locUtil = new LocUtil();
	        locUtil.setmLogger(mLogger);
                        
			HttpSession session = request.getSession();
			String plant = (String) session.getAttribute("PLANT");
			String user = (String) session.getAttribute("LOGIN_USER");
			boolean Flag ; 
	        String AssignedUser = StrUtils.fString(request.getParameter("UI_PKEY")).trim();
	        boolean flag = _userBean.isValidUserUIPKEYID(AssignedUser,plant); 
			Flag = _userLocUtil.deleteUserLoc(AssignedUser,plant);
			if(flag){
			for (int i = 1; i < chkdDoNo.length; i++) {
            String AssignedLoc = StrUtils.fString(request.getParameter("chkdDoNo")).trim();
            AssignedLoc = chkdDoNo[i];
                        
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("USERID", AssignedUser);
			ht.put("LOC", AssignedLoc);
			ht.put("CRAT", dateutils.getDateTime());
			ht.put("CRBY", user);
		    request.getSession().setAttribute("userAssignedLoc", ht);
			
			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.USER_LOC_ASSIGN);
			htm.put("RECID", "");
			htm.put("LOC", AssignedLoc);
			htm.put("CRBY", user);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
            
			flag = _userLocUtil.addUserLocation(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (flag && inserted) {
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">User Assigned to Location Successfully</font>";
			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ "> User Assigned to Location failed</font>";
			}
            } 
		    System.out.println("User flag ::"+flag);
			}else{
				result = "<font class = " + IConstants.FAILED_COLOR
						+ "> Enter/Select Valid User </font>"; 
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
	//IMTI ASSIGN END
	
	//IMTI ASSIGN INV START
		private String assignInvLocation(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException,Exception {
			String result = "";
			String[] chkdDoNo = request.getParameterValues("chkdDoNo");
			
			try {
				UserAssignedLocDAO userlocDAO = new UserAssignedLocDAO();
				userBean _userBean      = new userBean();
				_userBean.setmLogger(mLogger);
				LocUtil locUtil = new LocUtil();
				locUtil.setmLogger(mLogger);
				
				HttpSession session = request.getSession();
				String plant = (String) session.getAttribute("PLANT");
				String user = (String) session.getAttribute("LOGIN_USER");
				
				boolean flag ;
				ArrayList als = userlocDAO.getAllInvLocDetails(plant);
				if(als.equals("[]")) {
					flag = false;
				}else {
					flag = _userLocUtil.deleteInvLoc(plant);
					flag = true;
				}
				if(flag){
					for (int i = 1; i < chkdDoNo.length; i++) {
						String AssignedLoc = StrUtils.fString(request.getParameter("chkdDoNo")).trim();
						AssignedLoc = chkdDoNo[i];

		     		   	ArrayList al = locUtil.getLocListDetails(plant,AssignedLoc);
		     			Map map = (Map) al.get(0);
		     			String locdesc = (String) map.get("LOCDESC");
		     			
						Hashtable ht = new Hashtable();
						ht.put("PLANT", plant);
						ht.put("LOC", AssignedLoc);
						ht.put("LOCDESC", locdesc);
						ht.put("CRAT", dateutils.getDateTime());
						ht.put("CRBY", user);
						request.getSession().setAttribute("userAssignedLoc", ht);
						
						MovHisDAO mdao = new MovHisDAO(plant);
						mdao.setmLogger(mLogger);
						Hashtable htm = new Hashtable();
						htm.put("PLANT", plant);
						htm.put("DIRTYPE", TransactionConstants.USER_INV_ASSIGN);
						htm.put("RECID", "");
						htm.put("LOC", AssignedLoc);
						htm.put("CRBY", user);
						htm.put("CRAT", dateutils.getDateTime());
						htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
						
						flag = _userLocUtil.addInvLocation(ht);
						boolean inserted = mdao.insertIntoMovHis(htm);
						if (flag && inserted) {
							result = "<font class = " + IConstants.SUCCESS_COLOR
									+ ">API Inventory Assigned to Location Successfully</font>";
						} else {
							result = "<font class = " + IConstants.FAILED_COLOR
									+ "> API Inventory Assigned to Location failed</font>";
						}
					} 
					System.out.println("User flag ::"+flag);
				}else{
					result = "<font class = " + IConstants.FAILED_COLOR
							+ "> Enter/Select Valid Location </font>"; 
				}
			} catch (Exception e) {
				throw e;
			}
			return result;
		}
		//IMTI ASSIGN INV END
	
	
	
	private String assignUserLoc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";
	
		try {
		       userBean _userBean      = new userBean();
		       _userBean.setmLogger(mLogger);
                        LocUtil locUtil = new LocUtil();
		        locUtil.setmLogger(mLogger);
                        
			HttpSession session = request.getSession();
			String plant = (String) session.getAttribute("PLANT");
			String user = (String) session.getAttribute("LOGIN_USER");

                        String AssignedUser = StrUtils.fString(request.getParameter("USER")).trim();
                        String AssignedLoc = StrUtils.fString(request.getParameter("LOC")).trim();
		
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("USERID", AssignedUser);
			ht.put("LOC", AssignedLoc);
			ht.put("CRAT", dateutils.getDateTime());
			ht.put("CRBY", user);
		       request.getSession().setAttribute("userAssignedLoc", ht);
			
			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.USER_LOC_ASSIGN);
			htm.put("RECID", "");
                        
		         htm.put("LOC", AssignedLoc);
			htm.put("CRBY", AssignedUser);
			
			htm.put("CRAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
		    boolean flag = _userBean.isValidUserUIPKEYID(AssignedUser,plant); 
		    if(flag){
                        flag = locUtil.isValidLocInLocmst(plant,AssignedLoc);
                 
		    if(flag){
		     flag = _userLocUtil.isValidUserAssignedLoc(plant,AssignedUser,AssignedLoc);
                    
		 
                    
                    if(!flag){
			 flag = _userLocUtil.addUserLocation(ht);
			
			
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (flag && inserted) {
                              
				
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">User Assigned to Loc  Successfully</font>";

			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ "> User Assigned to Loc failed</font>";
				
			}
                    }else{
                        result = "<font class = " + IConstants.FAILED_COLOR
                                        + "> User Assigned to Loc Exists Already</font>"; 
                    }
		    }else{
		        result = "<font class = " + IConstants.FAILED_COLOR
		                        + "> Enter/Select Valid Location </font>"; 
		    }
		    }else{
		        result = "<font class = " + IConstants.FAILED_COLOR
		                        + "> Enter/Select Valid User </font>"; 
		    }
		    System.out.println("User flag ::"+flag);

		} catch (Exception e) {
			throw e;
		}

		return result;
	}
	
	private String unAssignuserLoc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String result = "", AssignedUser = "", AssignedLoc = "", remarks = "";
		HttpSession session = request.getSession();
		String plant = (String) session.getAttribute("PLANT");
		Hashtable htValues = new Hashtable();
		Hashtable htCondition = new Hashtable();

		try {
                         AssignedUser = StrUtils.fString(request.getParameter("USER")).trim();
                         AssignedLoc = StrUtils.fString(request.getParameter("LOC")).trim();

                        Hashtable ht = new Hashtable();
        
                        ht.put("USERID", AssignedUser);
                        ht.put("LOC", AssignedLoc);
                        ht.put("PLANT", plant);
		    request.getSession().setAttribute("userAssignedLoc", ht);
		    boolean flag = _userLocUtil.isValidUserAssignedLoc(plant,AssignedUser,AssignedLoc);
		    if(flag){
                     flag = _userLocUtil.deleteUserLocation(AssignedUser,AssignedLoc,plant);

			if (flag) {
				
				
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">UnAssigned Loc Successfully</font>&"
						+ "action = show_result";

			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ ">Unable to Delete User Assigned  Loc </font>";
				// throw new Exception("Unable to delete orderType ");
			}
                    }else{
                        result = "<font class = " + IConstants.FAILED_COLOR
                                        + "> User Assigned to Loc doesn't Exists to delete</font>"; 
                    }

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}
	@SuppressWarnings("unchecked")
	private void diaplayInfoLogs(HttpServletRequest request) {
		try {
			Map requestParameterMap = request.getParameterMap();
			Set<String> keyMap = requestParameterMap.keySet();
			StringBuffer requestParams = new StringBuffer();
			requestParams.append("Class Name : " + this.getClass() + "\n");
			requestParams.append("Paramter Mapping : \n");
			for (String key : keyMap) {
				requestParams.append("[" + key + " : "
						+ request.getParameter(key) + "] ");
			}
			this.mLogger.auditInfo(this.printInfo, requestParams.toString());

		} catch (Exception e) {
			
		}

	}
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
}