package com.track.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.util.PlantMstUtil;
import com.track.gates.defaultsBean;
import com.track.gates.encryptBean;
import com.track.gates.userBean;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

/*- ************Modification History*********************************
 Dec 9 2014 Bruhan, Description:ne Method:doGet:User_Access_warehouse-pdaPutAway() 
*/
public class Login extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.Login_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.Login_PRINTPLANTMASTERINFO;

	private static final long serialVersionUID = -6958907130544262639L;
	private encryptBean eb;
	private defaultsBean db;

	private userBean userBeanobj = null;
	private String action = "";

	private String xmlStr = "";
	private int status = 0;
	private static final String CONTENT_TYPE = "text/html";

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public void init() throws ServletException {
		try {
			
			eb = new encryptBean();
			db = new defaultsBean();
			userBeanobj = new userBean();
		} catch (Exception e) {
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		String plant = "",result="";

		try {
			HttpSession session = request.getSession();
			action = request.getParameter("action").trim();
			this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
					.getParameter("PLANT")), StrUtils.fString(request
							.getParameter("LOGIN_USER")) + " PDA_USER"));
			
			userBeanobj.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			
			
			
			if(action.equalsIgnoreCase("check_user")){
				String user_id = request.getParameter("userid").trim();
				String password = eb.encrypt(request.getParameter("password").trim());
				 PlantMstUtil _PlantMstUtil =new PlantMstUtil();
				ArrayList arr  = new ArrayList();
				arr= _PlantMstUtil.validateUser(user_id, password);
			 	int arrSize=arr.size();
			 	xmlStr = "";
			 	 xmlStr += XMLUtils.getXMLHeader();
			 	xmlStr += XMLUtils.getStartNode("companyDetails total='"
						+ String.valueOf(arr.size()) + "'");
				 if (arrSize>=1) {
									 
					 for (int i = 0; i < arr.size(); i++) {
							Map map = (Map) arr.get(i);
							xmlStr += XMLUtils.getStartNode("record");
							xmlStr += XMLUtils.getXMLNode("company", (String) map.get("plant"));
							xmlStr += XMLUtils.getXMLNode("companyname",StrUtils.fString(StrUtils.forHTMLTag((String) map.get("plantname"))));
							xmlStr += XMLUtils.getEndNode("record");
						}
						xmlStr += XMLUtils.getEndNode("companyDetails");
					 
				 }
				 
				 if(arrSize==0)
				 {
					 xmlStr = XMLUtils.getXMLMessage(1, "Login Failed. Please check UserId/Password/Internet connection.");
					 
				 }
			
			}else if(action.equalsIgnoreCase("pda_user_menu_access")){
				String user_id = request.getParameter("userid").trim();
				plant = request.getParameter("PLANT").trim();
				
				 ArrayList arr  = new ArrayList();
				arr= userBeanobj.get_pda_user_menu_access(plant,user_id);
			 	int arrSize=arr.size();
			 	xmlStr = "";
			 	 xmlStr += XMLUtils.getXMLHeader();
			 	xmlStr += XMLUtils.getStartNode("accessrightDetails total='"
						+ String.valueOf(arr.size()) + "'");
				 if (arrSize>0) {
									 
					 for (int i = 0; i < arr.size(); i++) {
							Map map = (Map) arr.get(i);
							xmlStr += XMLUtils.getStartNode("record");
							xmlStr += XMLUtils.getXMLNode("menu", (String) map.get("URL"));
							xmlStr += XMLUtils.getXMLNode("status",(String) map.get("STATUS"));
							xmlStr += XMLUtils.getEndNode("record");
						}
						xmlStr += XMLUtils.getEndNode("accessrightDetails");
					 
				 }
				 
				 if(arrSize==0)
				 {
					 xmlStr = XMLUtils.getXMLMessage(1, "Error:Menus not found");
					 
				 }
			
			}
		
	
			
 
		
		 

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		out.write(xmlStr);
		out.close();
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

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}
}