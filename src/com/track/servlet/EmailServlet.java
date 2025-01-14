package com.track.servlet;

import com.track.constants.IConstants;

import com.track.constants.IDBConstants;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EmailMsgDAO;
import com.track.db.util.DOTransferUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.PlantMstUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.track.gates.DbBean;
import com.track.tables.CATALOGMST;
import com.track.tables.ITEMMST;
import com.track.tables.USER_INFO;
import com.track.tranaction.SendEmail;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class EmailServlet extends HttpServlet implements IMLogger {
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
                        
             System.out.println("Entered EmailSrevlet");
             HttpSession session = request.getSession(false);
             JSONObject jsonObjectResult = new JSONObject();
                  String action = StrUtils.fString(request.getParameter("action"));
                  String result ="";
	    try {
	            

	            if (action.equalsIgnoreCase("Send Mass Email to all Customers")) {

	                    sendMassEmail(request, response);
	                    result = "<font class = " + IConstants.SUCCESS_COLOR + ">Mail sent sucessfully</font>";
                            response.sendRedirect("jsp/sendMassEmail.jsp?result=" + result);
	            }
	            if (action.equalsIgnoreCase("GETUSER")) {
	            	String plant=StrUtils.fString(request.getParameter("PLANT"));
	            	String OrderType=StrUtils.fString(request.getParameter("OrderType"));
	            	Vector<USER_INFO> userlist = (Vector<USER_INFO>) session.getAttribute("recvuserlist");
	            	if(OrderType.contentEquals("PurchaseAdminUser"))
						userlist = (Vector<USER_INFO>) session.getAttribute("recvuserlist");
					else if(OrderType.contentEquals("PurchaseUser"))
						userlist = (Vector<USER_INFO>) session.getAttribute("purcuserlist");
					else if(OrderType.contentEquals("EstimateAdminUser"))
						 userlist = (Vector<USER_INFO>) session.getAttribute("estimatelist");						 
					else if(OrderType.contentEquals("EstimateUser"))
						userlist = (Vector<USER_INFO>) session.getAttribute("estiuserlist");
					else if(OrderType.contentEquals("SalesAdminUser"))
						 userlist = (Vector<USER_INFO>) session.getAttribute("saleslist");						 
					else if(OrderType.contentEquals("SalesUser"))
						userlist = (Vector<USER_INFO>) session.getAttribute("salesuserlist");
					else if(OrderType.contentEquals("RentalAdminUser"))
						 userlist = (Vector<USER_INFO>) session.getAttribute("rentallist");						 
					else if(OrderType.contentEquals("RentalUser"))
						userlist = (Vector<USER_INFO>) session.getAttribute("rentuserlist");
	            	JSONObject resultJson = new JSONObject();
					JSONArray jsonArrayErr = new JSONArray();
	            	JSONArray jsonArray = new JSONArray();
	            	EmailMsgUtil mailUtil = new EmailMsgUtil();
	            	ArrayList arrCust=null;
	            	String Active="";
	            	try {
	        			JSONObject resultJsonInt = new JSONObject();
	            	if(OrderType.contentEquals("PurchaseAdminUser"))	            	
	            		arrCust= mailUtil.getConfigApprovalDetails(plant,"PURCHASE","ADMIN","");
	            	else if(OrderType.contentEquals("PurchaseUser"))	            	
	            		arrCust= mailUtil.getConfigApprovalDetails(plant,"PURCHASE","OTHER","");
	            	else if(OrderType.contentEquals("EstimateAdminUser"))	            	
	            		arrCust= mailUtil.getConfigApprovalDetails(plant,"ESTIMATE","ADMIN","");
	            	else if(OrderType.contentEquals("EstimateUser"))	            	
	            		arrCust= mailUtil.getConfigApprovalDetails(plant,"ESTIMATE","OTHER","");
	            	else if(OrderType.contentEquals("SalesAdminUser"))	            	
	            		arrCust= mailUtil.getConfigApprovalDetails(plant,"SALES","ADMIN","");
	            	else if(OrderType.contentEquals("SalesUser"))	            	
	            		arrCust= mailUtil.getConfigApprovalDetails(plant,"SALES","OTHER","");
	            	else if(OrderType.contentEquals("RentalAdminUser"))	            	
	            		arrCust= mailUtil.getConfigApprovalDetails(plant,"RENTAL","ADMIN","");
	            	else if(OrderType.contentEquals("RentalUser"))	            	
	            		arrCust= mailUtil.getConfigApprovalDetails(plant,"RENTAL","OTHER","");
	            		userlist = new Vector<USER_INFO>();
	            		for(int i =0; i<arrCust.size(); i++) {	            	        
	            	    Map arrCustLine = (Map)arrCust.get(i);
	            	    int id = Integer.parseInt((String)arrCustLine.get("ID"));
	   	            	String usermail=(String)arrCustLine.get("EMAIL");
	   	            	String userid=(String)arrCustLine.get("USERID");
	   	            	Active=(String)arrCustLine.get("ISACTIVE");
	            	       
	   						
	   						USER_INFO gp = new USER_INFO();
	   	                    gp.setId(userlist.size() + 1);
	   	                    gp.setEMAIL(usermail);
	   	                    gp.setUSER_ID(userid);
	   	                    userlist.add(gp);
	   	                    
	   	                    resultJsonInt.put("Id", userlist.size());
	   	                    resultJsonInt.put("usermail", usermail);
	                        resultJsonInt.put("userid", userid);
	                        jsonArray.add(resultJsonInt);	                        
	            		}
	            		resultJson.put("items", jsonArray);
	                    resultJsonInt = new JSONObject();
	                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                    resultJsonInt.put("ERROR_CODE", "100");
	                    jsonArrayErr.add(resultJsonInt);
	                    resultJson.put("errors", jsonArrayErr);
	                    resultJson.put("IsActive",Active);
	            	
	            	} catch (Exception daRE) {
	        			resultJson.put("status", "99");	        			
	        		}
	            	if(OrderType.contentEquals("PurchaseAdminUser"))
		        		session.setAttribute("recvuserlist", userlist);
		        		else if(OrderType.contentEquals("PurchaseUser"))
			        		session.setAttribute("purcuserlist", userlist);
		        		else if(OrderType.contentEquals("EstimateAdminUser"))
		        			session.setAttribute("estimatelist", userlist);
		        		else if(OrderType.contentEquals("EstimateUser"))
			        		session.setAttribute("estiuserlist", userlist);
		        		else if(OrderType.contentEquals("SalesAdminUser"))
		        			session.setAttribute("saleslist", userlist);
		        		else if(OrderType.contentEquals("SalesUser"))
		        			session.setAttribute("salesuserlist", userlist);
		        		else if(OrderType.contentEquals("RentalAdminUser"))
		        			session.setAttribute("rentallist", userlist);
		        		else if(OrderType.contentEquals("RentalUser"))
		        			session.setAttribute("rentuserlist", userlist);
		            	response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(resultJson.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
	            }
	            if (action.equalsIgnoreCase("GETCONFIGEMAIL")) {
	            	String plant=StrUtils.fString(request.getParameter("PLANT"));
	            	String OrderType=StrUtils.fString(request.getParameter("OrderType"));
	            	JSONObject resultJson = new JSONObject();
					JSONArray jsonArrayErr = new JSONArray();
	            	JSONArray jsonArray = new JSONArray();
	            	
	            	try {
	        			JSONObject resultJsonInt = new JSONObject();
	        			String PurchaseCreateEmail="",PurchaseCreateAtt="",	PurchaseCreateAttTo="",PurchaseApproveEmail="",PurchaseRejectEmail="",PurchaseisAutoEmail="",
	        					SalesCreateEmail="",SalesCreateAtt="",SalesCreateAttTo="",SalesApproveEmail="",SalesRejectEmail="",SalesisAutoEmail="",
	        					EstimateCreateEmail="",EstimateCreateAtt="",EstimateCreateAttTo="",EstimateApproveEmail="",EstimateRejectEmail="",EstimateisAutoEmail="",
	        					RentalCreateEmail="",RentalCreateAtt="",RentalCreateAttTo="",RentalApproveEmail="",RentalRejectEmail="",RentalisAutoEmail="";
	        			ArrayList arrCust=new ArrayList();
	        			EmailMsgUtil mailUtil = new EmailMsgUtil();
	        			
		            	arrCust= mailUtil.getConfigApprovalEmailDetails(plant,OrderType,"Upon Create");
		            	for(int i =0; i<arrCust.size(); i++) {	            	        
		            	    Map arrCustLine = (Map)arrCust.get(i);
		            	    if(OrderType.equalsIgnoreCase("PURCHASE"))
		            	    {
		   	            	PurchaseCreateEmail=(String)arrCustLine.get("EMAIL_TO");
		   	            	PurchaseCreateAtt=(String)arrCustLine.get("ATTACHMENT");
		   	            	PurchaseCreateAttTo=(String)arrCustLine.get("ATTACHMENT_TO");
		   	            	PurchaseisAutoEmail=(String)arrCustLine.get("ISAUTOEMAIL");
		            	    }
		            	    else if(OrderType.equalsIgnoreCase("ESTIMATE"))
		            	    {
		            	    	EstimateCreateEmail=(String)arrCustLine.get("EMAIL_TO");
		            	    	EstimateCreateAtt=(String)arrCustLine.get("ATTACHMENT");
		            	    	EstimateCreateAttTo=(String)arrCustLine.get("ATTACHMENT_TO");
		            	    	EstimateisAutoEmail=(String)arrCustLine.get("ISAUTOEMAIL");
		            	    }
		            	    else if(OrderType.equalsIgnoreCase("SALES"))
		            	    {
		            	    	SalesCreateEmail=(String)arrCustLine.get("EMAIL_TO");
		            	    	SalesCreateAtt=(String)arrCustLine.get("ATTACHMENT");
		            	    	SalesCreateAttTo=(String)arrCustLine.get("ATTACHMENT_TO");
		            	    	SalesisAutoEmail=(String)arrCustLine.get("ISAUTOEMAIL");
		            	    }
		            	    else if(OrderType.equalsIgnoreCase("RENTAL"))
		            	    {
		            	    	RentalCreateEmail=(String)arrCustLine.get("EMAIL_TO");
		            	    	RentalCreateAtt=(String)arrCustLine.get("ATTACHMENT");
		            	    	RentalCreateAttTo=(String)arrCustLine.get("ATTACHMENT_TO");
		            	    	RentalisAutoEmail=(String)arrCustLine.get("ISAUTOEMAIL");
		            	    }
		            	}
		            	arrCust= mailUtil.getConfigApprovalEmailDetails(plant,OrderType,"Upon Approve");
		            	for(int i =0; i<arrCust.size(); i++) {	            	        
		            	    Map arrCustLine = (Map)arrCust.get(i);
		            	    if(OrderType.equalsIgnoreCase("PURCHASE"))
		            	    	PurchaseApproveEmail=(String)arrCustLine.get("EMAIL_TO");
		            	    else if(OrderType.equalsIgnoreCase("ESTIMATE"))
		            	    	EstimateApproveEmail=(String)arrCustLine.get("EMAIL_TO");
		            	    else if(OrderType.equalsIgnoreCase("SALES"))
		            	    	SalesApproveEmail=(String)arrCustLine.get("EMAIL_TO");
		            	    else if(OrderType.equalsIgnoreCase("RENTAL"))
		            	    	RentalApproveEmail=(String)arrCustLine.get("EMAIL_TO");
		            	}
		            	arrCust= mailUtil.getConfigApprovalEmailDetails(plant,OrderType,"Upon Reject");
		            	for(int i =0; i<arrCust.size(); i++) {	            	        
		            	    Map arrCustLine = (Map)arrCust.get(i);
		            	    if(OrderType.equalsIgnoreCase("PURCHASE"))
		            	    	PurchaseRejectEmail=(String)arrCustLine.get("EMAIL_TO");
		            	    else if(OrderType.equalsIgnoreCase("ESTIMATE"))
		            	    	EstimateRejectEmail=(String)arrCustLine.get("EMAIL_TO");
		            	    else if(OrderType.equalsIgnoreCase("SALES"))
		            	    	SalesRejectEmail=(String)arrCustLine.get("EMAIL_TO");
		            	    else if(OrderType.equalsIgnoreCase("RENTAL"))
		            	    	RentalRejectEmail=(String)arrCustLine.get("EMAIL_TO");
		            	}
		            	
	            	resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                    resultJson.put("PurchaseCreateEmail",PurchaseCreateEmail);
                    resultJson.put("PurchaseApproveEmail",PurchaseApproveEmail);
                    resultJson.put("PurchaseRejectEmail",PurchaseRejectEmail);
                    resultJson.put("PurchaseisAutoEmail",PurchaseisAutoEmail);
                    resultJson.put("PurchaseCreateAtt",PurchaseCreateAtt);
                    resultJson.put("PurchaseCreateAttTo",PurchaseCreateAttTo);
                    resultJson.put("EstimateCreateEmail",EstimateCreateEmail);
                    resultJson.put("EstimateApproveEmail",EstimateApproveEmail);
                    resultJson.put("EstimateRejectEmail",EstimateRejectEmail);
                    resultJson.put("EstimateisAutoEmail",EstimateisAutoEmail);
                    resultJson.put("EstimateCreateAtt",EstimateCreateAtt);
                    resultJson.put("EstimateCreateAttTo",EstimateCreateAttTo);
                    resultJson.put("SalesCreateEmail",SalesCreateEmail);
                    resultJson.put("SalesApproveEmail",SalesApproveEmail);
                    resultJson.put("SalesRejectEmail",SalesRejectEmail);
                    resultJson.put("SalesisAutoEmail",SalesisAutoEmail);
                    resultJson.put("SalesCreateAtt",SalesCreateAtt);
                    resultJson.put("SalesCreateAttTo",SalesCreateAttTo);
                    resultJson.put("RentalCreateEmail",RentalCreateEmail);
                    resultJson.put("RentalApproveEmail",RentalApproveEmail);
                    resultJson.put("RentalRejectEmail",RentalRejectEmail);
                    resultJson.put("RentalisAutoEmail",RentalisAutoEmail);
                    resultJson.put("RentalCreateAtt",RentalCreateAtt);
                    resultJson.put("RentalCreateAttTo",RentalCreateAttTo);
	            	
	            } catch (Exception daRE) {
        			resultJson.put("status", "99");	        			
        		}
	            	response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(resultJson.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
            }
	            if (action.equalsIgnoreCase("ADDUSER")) {
	            
	            	String userid = StrUtils.fString(request.getParameter("USERID"));
					String usermail = StrUtils.fString(request.getParameter("USERMAIL"));
					String OrderType=StrUtils.fString(request.getParameter("OrderType"));
					Vector<USER_INFO> userlist = (Vector<USER_INFO>) session.getAttribute("recvuserlist");
					if(OrderType.contentEquals("PurchaseAdminUser"))
						userlist = (Vector<USER_INFO>) session.getAttribute("recvuserlist");
					else if(OrderType.contentEquals("PurchaseUser"))
						userlist = (Vector<USER_INFO>) session.getAttribute("purcuserlist");
					else if(OrderType.contentEquals("EstimateAdminUser"))
						 userlist = (Vector<USER_INFO>) session.getAttribute("estimatelist");						 
					else if(OrderType.contentEquals("EstimateUser"))
						userlist = (Vector<USER_INFO>) session.getAttribute("estiuserlist");
					else if(OrderType.contentEquals("SalesAdminUser"))
						 userlist = (Vector<USER_INFO>) session.getAttribute("saleslist");						 
					else if(OrderType.contentEquals("SalesUser"))
						userlist = (Vector<USER_INFO>) session.getAttribute("salesuserlist");
					else if(OrderType.contentEquals("RentalAdminUser"))
						 userlist = (Vector<USER_INFO>) session.getAttribute("rentallist");						 
					else if(OrderType.contentEquals("RentalUser"))
						userlist = (Vector<USER_INFO>) session.getAttribute("rentuserlist");
					JSONObject resultJson = new JSONObject();
					JSONArray jsonArrayErr = new JSONArray();
	            	JSONArray jsonArray = new JSONArray();
	        		try {
	        			JSONObject resultJsonInt = new JSONObject();	
					if (userlist == null || userlist.size()==0)
					{
						userlist = new Vector<USER_INFO>();
						USER_INFO gp = new USER_INFO();
	                    gp.setId(userlist.size() + 1);
	                    gp.setEMAIL(usermail);
	                    gp.setUSER_ID(userid);
	                    userlist.add(gp);
	                    
	                    resultJsonInt.put("Id", userlist.size());
	                    resultJsonInt.put("usermail", usermail);
                        resultJsonInt.put("userid", userid);
                        jsonArray.add(resultJsonInt);
					}
					else
					{
						int chk=0;
						for (int j = 0; j < userlist.size(); j++) {
							USER_INFO userdao = userlist.elementAt(j);
							if (userdao.getUSER_ID().equalsIgnoreCase(userid)) {							
								chk=1;
							}							
							//userlist.setElementAt(userdao, j);
						}
						if(chk==0)
						{
						USER_INFO gp = new USER_INFO();
	                    gp.setId(userlist.size() + 1);
	                    gp.setEMAIL(usermail);
	                    gp.setUSER_ID(userid);
	                    userlist.add(gp);
						}
						for (int j = 0; j < userlist.size(); j++) {
							USER_INFO userdao = userlist.elementAt(j);
							resultJsonInt.put("Id", j+1);
		                    resultJsonInt.put("usermail", userdao.getEMAIL());
	                        resultJsonInt.put("userid", userdao.getUSER_ID());
	                        jsonArray.add(resultJsonInt);							
						}
					}
					resultJson.put("items", jsonArray);
                    resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
	            	
	        			
	        			
	        		} catch (Exception daRE) {
	        			resultJson.put("status", "99");	        			
	        		}
	        		if(OrderType.contentEquals("PurchaseAdminUser"))
	        		session.setAttribute("recvuserlist", userlist);
	        		else if(OrderType.contentEquals("PurchaseUser"))
		        		session.setAttribute("purcuserlist", userlist);
	        		else if(OrderType.contentEquals("EstimateAdminUser"))
	        			session.setAttribute("estimatelist", userlist);
	        		else if(OrderType.contentEquals("EstimateUser"))
		        		session.setAttribute("estiuserlist", userlist);
	        		else if(OrderType.contentEquals("SalesAdminUser"))
	        			session.setAttribute("saleslist", userlist);
	        		else if(OrderType.contentEquals("SalesUser"))
	        			session.setAttribute("salesuserlist", userlist);
	        		else if(OrderType.contentEquals("RentalAdminUser"))
	        			session.setAttribute("rentallist", userlist);
	        		else if(OrderType.contentEquals("RentalUser"))
	        			session.setAttribute("rentuserlist", userlist);
	            	response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(resultJson.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
	            }
	            else if (action.equalsIgnoreCase("DELETEUSER")) {
	            
	            	String plant=StrUtils.fString(request.getParameter("PLANT"));
	            	int id = Integer.parseInt(StrUtils.fString(request.getParameter("Id")));
	            	String OrderType=StrUtils.fString(request.getParameter("OrderType"));
	            	String usertype = StrUtils.fString(request.getParameter("USERTYPE"));
	            	String userid = StrUtils.fString(request.getParameter("USERID"));
					String OrderTypeTran="";
					Vector<USER_INFO> userlist = (Vector<USER_INFO>) session.getAttribute("recvuserlist");
					if(OrderType.contentEquals("PurchaseAdminUser"))
					{
						 userlist = (Vector<USER_INFO>) session.getAttribute("recvuserlist");
						 OrderTypeTran="PURCHASE";
					}
					else if(OrderType.contentEquals("PurchaseUser"))
					{
						userlist = (Vector<USER_INFO>) session.getAttribute("purcuserlist");
						OrderTypeTran="PURCHASE";
					}
					else if(OrderType.contentEquals("EstimateAdminUser"))
					{
						 userlist = (Vector<USER_INFO>) session.getAttribute("estimatelist");
						 OrderTypeTran="ESTIMATE";
					}
					else if(OrderType.contentEquals("EstimateUser"))
					{
						userlist = (Vector<USER_INFO>) session.getAttribute("estiuserlist");
						OrderTypeTran="ESTIMATE";
					}
					else if(OrderType.contentEquals("SalesUser"))
					{
						userlist = (Vector<USER_INFO>) session.getAttribute("salesuserlist");
						OrderTypeTran="SALES";
					}
					else if(OrderType.contentEquals("RentalAdminUser"))
					{
						 userlist = (Vector<USER_INFO>) session.getAttribute("rentallist");
					     OrderTypeTran="RENTAL";
					}						 
					else if(OrderType.contentEquals("RentalUser"))
					{
						userlist = (Vector<USER_INFO>) session.getAttribute("rentuserlist");
						OrderTypeTran="RENTAL";
					}
					JSONObject resultJson = new JSONObject();
					JSONArray jsonArrayErr = new JSONArray();
	            	JSONArray jsonArray = new JSONArray();
	            	EmailMsgDAO emailDao =new EmailMsgDAO();
	        		try {
	        			JSONObject resultJsonInt = new JSONObject();	
	        			for (int j = 0; j < userlist.size(); j++) {
							USER_INFO userdao = userlist.elementAt(j);							
							if(userdao.getId()== id)
							{
								ArrayList arrList = new ArrayList();
					             arrList= emailDao.getConfigApprovalDetails(plant,OrderTypeTran,usertype,userid);
					            if(!arrList.isEmpty()){
					            	String query=" AND ORDERTYPE='"+OrderTypeTran+"' AND USERTYPE='"+usertype+"' AND USERID='"+userid+"'";
					            	boolean isUpdated = emailDao.deleteConfigApproval(query,plant);
					            }
					            
								userlist.remove(j);
								break;
							}
	        			}
	        				
						for (int j = 0; j < userlist.size(); j++) {
							USER_INFO userdao = userlist.elementAt(j);
							resultJsonInt.put("Id", j+1);
		                    resultJsonInt.put("usermail", userdao.getEMAIL());
	                        resultJsonInt.put("userid", userdao.getUSER_ID());
	                        jsonArray.add(resultJsonInt);							
						}
						
					resultJson.put("items", jsonArray);
                    resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
	            	
	        			
	        			
	        		} catch (Exception daRE) {
	        			resultJson.put("status", "99");	        			
	        		}        		
	        		if(OrderType.contentEquals("PurchaseAdminUser"))
	        			session.setAttribute("recvuserlist", userlist);
	        		else if(OrderType.contentEquals("PurchaseUser"))
		        		session.setAttribute("purcuserlist", userlist);
	        		else if(OrderType.contentEquals("EstimateAdminUser"))
	        			session.setAttribute("estimatelist", userlist);
	        		else if(OrderType.contentEquals("EstimateUser"))
		        		session.setAttribute("estiuserlist", userlist);
	        		else if(OrderType.contentEquals("SalesAdminUser"))
	        			session.setAttribute("saleslist", userlist);
	        		else if(OrderType.contentEquals("SalesUser"))
	        			session.setAttribute("salesuserlist", userlist);
	        		else if(OrderType.contentEquals("RentalAdminUser"))
	        			session.setAttribute("rentallist", userlist);
	        		else if(OrderType.contentEquals("RentalUser"))
	        			session.setAttribute("rentuserlist", userlist);
	            	response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(resultJson.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
	            }
	            if (action.equalsIgnoreCase("SAVEUSER")) {
	            
	            	String plant=StrUtils.fString(request.getParameter("PLANT"));
	            	String sUserId=StrUtils.fString(request.getParameter("LOGIN_USER"));
	            	String PurchaseIsActive=StrUtils.fString(request.getParameter("PURCHASEISACTIVE"));
	            	String EstimateIsActive=StrUtils.fString(request.getParameter("ESTIMATEISACTIVE"));
	            	String SalesIsActive=StrUtils.fString(request.getParameter("SALESISACTIVE"));
	            	String RentalIsActive=StrUtils.fString(request.getParameter("RENTALISACTIVE"));
	            	
	            	String PurchaseCreateEmail=StrUtils.fString(request.getParameter("PURCHASECREATEEMAIL"));
	            	String PurchaseCreateAtt=StrUtils.fString(request.getParameter("PURCHASECREATEATT"));
	            	String PurchaseCreateAttTo=StrUtils.fString(request.getParameter("PURCHASECREATEATTTO"));
	            	String PurchaseApproveEmail=StrUtils.fString(request.getParameter("PURCHASEAPPROVEEMAIL"));
	            	String PurchaseRejectEmail=StrUtils.fString(request.getParameter("PURCHASEREJECTEMAIL"));
	            	String PurchaseisAutoEmail=StrUtils.fString(request.getParameter("PURCHASEISAUTOEMAIL"));
	            	
	            	String EstimateCreateEmail=StrUtils.fString(request.getParameter("ESTIMATECREATEEMAIL"));
	            	String EstimateCreateAtt=StrUtils.fString(request.getParameter("ESTIMATECREATEATT"));
	            	String EstimateCreateAttTo=StrUtils.fString(request.getParameter("ESTIMATECREATEATTTO"));
	            	String EstimateApproveEmail=StrUtils.fString(request.getParameter("ESTIMATEAPPROVEEMAIL"));
	            	String EstimateRejectEmail=StrUtils.fString(request.getParameter("ESTIMATEREJECTEMAIL"));
	            	String EstimateisAutoEmail=StrUtils.fString(request.getParameter("ESTIMATEISAUTOEMAIL"));
	            	
	            	String SalesCreateEmail=StrUtils.fString(request.getParameter("SALESCREATEEMAIL"));
	            	String SalesCreateAtt=StrUtils.fString(request.getParameter("SALESCREATEATT"));
	            	String SalesCreateAttTo=StrUtils.fString(request.getParameter("SALESCREATEATTTO"));
	            	String SalesApproveEmail=StrUtils.fString(request.getParameter("SALESAPPROVEEMAIL"));
	            	String SalesRejectEmail=StrUtils.fString(request.getParameter("SALESREJECTEMAIL"));
	            	String SalesisAutoEmail=StrUtils.fString(request.getParameter("SALESISAUTOEMAIL"));
	            	
	            	String RentalCreateEmail=StrUtils.fString(request.getParameter("RENTALCREATEEMAIL"));
	            	String RentalCreateAtt=StrUtils.fString(request.getParameter("RENTALCREATEATT"));
	            	String RentalCreateAttTo=StrUtils.fString(request.getParameter("RENTALCREATEATTTO"));
	            	String RentalApproveEmail=StrUtils.fString(request.getParameter("RENTALAPPROVEEMAIL"));
	            	String RentalRejectEmail=StrUtils.fString(request.getParameter("RENTALREJECTEMAIL"));
	            	String RentalisAutoEmail=StrUtils.fString(request.getParameter("RENTALISAUTOEMAIL"));
	            	
	            	if(PurchaseIsActive.equalsIgnoreCase("on"))
	            		PurchaseIsActive="Y";
	            	if(EstimateIsActive.equalsIgnoreCase("on"))
	            		EstimateIsActive="Y";
	            	if(SalesIsActive.equalsIgnoreCase("on"))
	            		SalesIsActive="Y";
	            	if(RentalIsActive.equalsIgnoreCase("on"))
	            		RentalIsActive="Y";
	            	if(PurchaseisAutoEmail.equalsIgnoreCase("on"))
	            		PurchaseisAutoEmail="Y";
	            	if(EstimateisAutoEmail.equalsIgnoreCase("on"))
	            		EstimateisAutoEmail="Y";
	            	if(SalesisAutoEmail.equalsIgnoreCase("on"))
	            		SalesisAutoEmail="Y";
	            	if(RentalisAutoEmail.equalsIgnoreCase("on"))
	            		RentalisAutoEmail="Y";
	            	
	            	Vector<USER_INFO> userlist = (Vector<USER_INFO>) session.getAttribute("recvuserlist");
	            	Vector<USER_INFO> 	purcuserlist = (Vector<USER_INFO>) session.getAttribute("purcuserlist");
	            	Vector<USER_INFO> estimatelist = (Vector<USER_INFO>) session.getAttribute("estimatelist");
	            	Vector<USER_INFO> 	estiuserlist = (Vector<USER_INFO>) session.getAttribute("estiuserlist");
	            	Vector<USER_INFO> saleslist = (Vector<USER_INFO>) session.getAttribute("saleslist");
	            	Vector<USER_INFO> salesuserlist = (Vector<USER_INFO>) session.getAttribute("salesuserlist");					
	            	Vector<USER_INFO> rentallist = (Vector<USER_INFO>) session.getAttribute("rentallist");
	            	Vector<USER_INFO> rentuserlist = (Vector<USER_INFO>) session.getAttribute("rentuserlist");
	            	EmailMsgUtil mailUtil = new EmailMsgUtil();
	            	boolean isUpdated=false;
	            	JSONObject resultJson = new JSONObject();
					JSONArray jsonArrayErr = new JSONArray();
	            	JSONArray jsonArray = new JSONArray();
	        		try {
	        			JSONObject resultJsonInt = new JSONObject();
	    			Hashtable<String,String> ht = new Hashtable<String,String>();
	    			
	    			//Purchase Create
	    			ht = new Hashtable<String,String>();
                    ht.put(IDBConstants.PLANT,plant);
                    ht.put(IDBConstants.LOGIN_USER,sUserId);
                    ht.put(IDBConstants.ORDERTYPE,"PURCHASE");
                    ht.put(IDBConstants.EMAILTYPE,"Upon Create");
                    ht.put(IDBConstants.EMAIL_TO,PurchaseCreateEmail);
                    ht.put(IDBConstants.ATTACHMENT, PurchaseCreateAtt);
                    ht.put(IDBConstants.ATTACHMENT_TO, PurchaseCreateAttTo);
                    if(!PurchaseisAutoEmail.equalsIgnoreCase("Y"))PurchaseisAutoEmail="N";
                    ht.put(IDBConstants.ISAUTOEMAIL,PurchaseisAutoEmail);               
                    
                    isUpdated =  mailUtil.updateConfigApprovalEmail(ht);
                    
                  //Purchase Approve
	    			ht = new Hashtable<String,String>();
                    ht.put(IDBConstants.PLANT,plant);
                    ht.put(IDBConstants.LOGIN_USER,sUserId);
                    ht.put(IDBConstants.ORDERTYPE,"PURCHASE");
                    ht.put(IDBConstants.EMAILTYPE,"Upon Approve");
                    ht.put(IDBConstants.EMAIL_TO,PurchaseApproveEmail);
                    ht.put(IDBConstants.ATTACHMENT, PurchaseCreateAtt);
                    ht.put(IDBConstants.ATTACHMENT_TO, PurchaseCreateAttTo);
                    if(!PurchaseisAutoEmail.equalsIgnoreCase("Y"))PurchaseisAutoEmail="N";
                    ht.put(IDBConstants.ISAUTOEMAIL,PurchaseisAutoEmail);               
                    
                    isUpdated =  mailUtil.updateConfigApprovalEmail(ht);
                    
                  //Purchase Reject
	    			ht = new Hashtable<String,String>();
                    ht.put(IDBConstants.PLANT,plant);
                    ht.put(IDBConstants.LOGIN_USER,sUserId);
                    ht.put(IDBConstants.ORDERTYPE,"PURCHASE");
                    ht.put(IDBConstants.EMAILTYPE,"Upon Reject");
                    ht.put(IDBConstants.EMAIL_TO,PurchaseRejectEmail);
                    ht.put(IDBConstants.ATTACHMENT, PurchaseCreateAtt);
                    ht.put(IDBConstants.ATTACHMENT_TO, PurchaseCreateAttTo);
                    if(!PurchaseisAutoEmail.equalsIgnoreCase("Y"))PurchaseisAutoEmail="N";
                    ht.put(IDBConstants.ISAUTOEMAIL,PurchaseisAutoEmail);               
                    
                    isUpdated =  mailUtil.updateConfigApprovalEmail(ht);
                    
                  //Estimate Create
	    			ht = new Hashtable<String,String>();
                    ht.put(IDBConstants.PLANT,plant);
                    ht.put(IDBConstants.LOGIN_USER,sUserId);
                    ht.put(IDBConstants.ORDERTYPE,"ESTIMATE");
                    ht.put(IDBConstants.EMAILTYPE,"Upon Create");
                    ht.put(IDBConstants.EMAIL_TO,EstimateCreateEmail);
                    ht.put(IDBConstants.ATTACHMENT, EstimateCreateAtt);
                    ht.put(IDBConstants.ATTACHMENT_TO, EstimateCreateAttTo);
                    if(!EstimateisAutoEmail.equalsIgnoreCase("Y"))EstimateisAutoEmail="N";
                    ht.put(IDBConstants.ISAUTOEMAIL,EstimateisAutoEmail);               
                    
                    isUpdated =  mailUtil.updateConfigApprovalEmail(ht);
                    
                  //Estimate Approve
	    			ht = new Hashtable<String,String>();
                    ht.put(IDBConstants.PLANT,plant);
                    ht.put(IDBConstants.LOGIN_USER,sUserId);
                    ht.put(IDBConstants.ORDERTYPE,"ESTIMATE");
                    ht.put(IDBConstants.EMAILTYPE,"Upon Approve");
                    ht.put(IDBConstants.EMAIL_TO,EstimateApproveEmail);
                    ht.put(IDBConstants.ATTACHMENT, EstimateCreateAtt);
                    ht.put(IDBConstants.ATTACHMENT_TO, EstimateCreateAttTo);
                    if(!EstimateisAutoEmail.equalsIgnoreCase("Y"))EstimateisAutoEmail="N";
                    ht.put(IDBConstants.ISAUTOEMAIL,EstimateisAutoEmail);               
                    
                    isUpdated =  mailUtil.updateConfigApprovalEmail(ht);
                    
                  //Estimate Reject
	    			ht = new Hashtable<String,String>();
                    ht.put(IDBConstants.PLANT,plant);
                    ht.put(IDBConstants.LOGIN_USER,sUserId);
                    ht.put(IDBConstants.ORDERTYPE,"ESTIMATE");
                    ht.put(IDBConstants.EMAILTYPE,"Upon Reject");
                    ht.put(IDBConstants.EMAIL_TO,EstimateRejectEmail);
                    ht.put(IDBConstants.ATTACHMENT, EstimateCreateAtt);
                    ht.put(IDBConstants.ATTACHMENT_TO, EstimateCreateAttTo);
                    if(!EstimateisAutoEmail.equalsIgnoreCase("Y"))EstimateisAutoEmail="N";
                    ht.put(IDBConstants.ISAUTOEMAIL,EstimateisAutoEmail);               
                    
                    isUpdated =  mailUtil.updateConfigApprovalEmail(ht);
                    
                  //Sales Create
	    			ht = new Hashtable<String,String>();
                    ht.put(IDBConstants.PLANT,plant);
                    ht.put(IDBConstants.LOGIN_USER,sUserId);
                    ht.put(IDBConstants.ORDERTYPE,"SALES");
                    ht.put(IDBConstants.EMAILTYPE,"Upon Create");
                    ht.put(IDBConstants.EMAIL_TO,SalesCreateEmail);
                    ht.put(IDBConstants.ATTACHMENT, SalesCreateAtt);
                    ht.put(IDBConstants.ATTACHMENT_TO, SalesCreateAttTo);
                    if(!SalesisAutoEmail.equalsIgnoreCase("Y"))SalesisAutoEmail="N";
                    ht.put(IDBConstants.ISAUTOEMAIL,SalesisAutoEmail);               
                    
                    isUpdated =  mailUtil.updateConfigApprovalEmail(ht);
                    
                  //Sales Approve
	    			ht = new Hashtable<String,String>();
                    ht.put(IDBConstants.PLANT,plant);
                    ht.put(IDBConstants.LOGIN_USER,sUserId);
                    ht.put(IDBConstants.ORDERTYPE,"SALES");
                    ht.put(IDBConstants.EMAILTYPE,"Upon Approve");
                    ht.put(IDBConstants.EMAIL_TO,SalesApproveEmail);
                    ht.put(IDBConstants.ATTACHMENT, SalesCreateAtt);
                    ht.put(IDBConstants.ATTACHMENT_TO, SalesCreateAttTo);
                    if(!SalesisAutoEmail.equalsIgnoreCase("Y"))SalesisAutoEmail="N";
                    ht.put(IDBConstants.ISAUTOEMAIL,SalesisAutoEmail);               
                    
                    isUpdated =  mailUtil.updateConfigApprovalEmail(ht);
                    
                  //Sales Reject
	    			ht = new Hashtable<String,String>();
                    ht.put(IDBConstants.PLANT,plant);
                    ht.put(IDBConstants.LOGIN_USER,sUserId);
                    ht.put(IDBConstants.ORDERTYPE,"SALES");
                    ht.put(IDBConstants.EMAILTYPE,"Upon Reject");
                    ht.put(IDBConstants.EMAIL_TO,SalesRejectEmail);
                    ht.put(IDBConstants.ATTACHMENT, SalesCreateAtt);
                    ht.put(IDBConstants.ATTACHMENT_TO, SalesCreateAttTo);
                    if(!SalesisAutoEmail.equalsIgnoreCase("Y"))SalesisAutoEmail="N";
                    ht.put(IDBConstants.ISAUTOEMAIL,SalesisAutoEmail);               
                    
                    isUpdated =  mailUtil.updateConfigApprovalEmail(ht);
                    
                  //Rental Create
	    			ht = new Hashtable<String,String>();
                    ht.put(IDBConstants.PLANT,plant);
                    ht.put(IDBConstants.LOGIN_USER,sUserId);
                    ht.put(IDBConstants.ORDERTYPE,"RENTAL");
                    ht.put(IDBConstants.EMAILTYPE,"Upon Create");
                    ht.put(IDBConstants.EMAIL_TO,RentalCreateEmail);
                    ht.put(IDBConstants.ATTACHMENT, RentalCreateAtt);
                    ht.put(IDBConstants.ATTACHMENT_TO, RentalCreateAttTo);
                    if(!RentalisAutoEmail.equalsIgnoreCase("Y"))RentalisAutoEmail="N";
                    ht.put(IDBConstants.ISAUTOEMAIL,RentalisAutoEmail);               
                    
                    isUpdated =  mailUtil.updateConfigApprovalEmail(ht);
                    
                  //Rental Approve
	    			ht = new Hashtable<String,String>();
                    ht.put(IDBConstants.PLANT,plant);
                    ht.put(IDBConstants.LOGIN_USER,sUserId);
                    ht.put(IDBConstants.ORDERTYPE,"RENTAL");
                    ht.put(IDBConstants.EMAILTYPE,"Upon Approve");
                    ht.put(IDBConstants.EMAIL_TO,RentalApproveEmail);
                    ht.put(IDBConstants.ATTACHMENT, RentalCreateAtt);
                    ht.put(IDBConstants.ATTACHMENT_TO, RentalCreateAttTo);
                    if(!RentalisAutoEmail.equalsIgnoreCase("Y"))RentalisAutoEmail="N";
                    ht.put(IDBConstants.ISAUTOEMAIL,RentalisAutoEmail);               
                    
                    isUpdated =  mailUtil.updateConfigApprovalEmail(ht);
                    
                  //Rental Reject
	    			ht = new Hashtable<String,String>();
                    ht.put(IDBConstants.PLANT,plant);
                    ht.put(IDBConstants.LOGIN_USER,sUserId);
                    ht.put(IDBConstants.ORDERTYPE,"RENTAL");
                    ht.put(IDBConstants.EMAILTYPE,"Upon Reject");
                    ht.put(IDBConstants.EMAIL_TO,RentalRejectEmail);
                    ht.put(IDBConstants.ATTACHMENT, RentalCreateAtt);
                    ht.put(IDBConstants.ATTACHMENT_TO, RentalCreateAttTo);
                    if(!RentalisAutoEmail.equalsIgnoreCase("Y"))RentalisAutoEmail="N";
                    ht.put(IDBConstants.ISAUTOEMAIL,RentalisAutoEmail);               
                    
                    isUpdated =  mailUtil.updateConfigApprovalEmail(ht);
	    			
	    			if(userlist.size()>0)
	    			{
	    				for (int j = 0; j < userlist.size(); j++) {
	    					USER_INFO userdao = (USER_INFO)userlist.elementAt(j);
	                    ht = new Hashtable<String,String>();
	                    ht.put(IDBConstants.PLANT,plant);
	                    ht.put(IDBConstants.LOGIN_USER,sUserId);
	                    ht.put(IDBConstants.ORDERTYPE,"PURCHASE");
	                    ht.put(IDBConstants.USERTYPE,"ADMIN");
	                    ht.put(IDBConstants.USERID,userdao.getUSER_ID());
	                    ht.put(IDBConstants.EMAIL, userdao.getEMAIL());             
	                    if(!PurchaseIsActive.equalsIgnoreCase("Y"))PurchaseIsActive="N";
	                    ht.put(IDBConstants.ISACTIVE,PurchaseIsActive);               
	                    
	                    isUpdated =  mailUtil.updateConfigApproval(ht);
	    				}
	    			}
	    			
	    			if(purcuserlist.size()>0)
	    			{
	    				for (int j = 0; j < purcuserlist.size(); j++) {
	    					USER_INFO userdao = (USER_INFO)purcuserlist.elementAt(j);
	                    ht = new Hashtable<String,String>();
	                    ht.put(IDBConstants.PLANT,plant);
	                    ht.put(IDBConstants.LOGIN_USER,sUserId);
	                    ht.put(IDBConstants.ORDERTYPE,"PURCHASE");
	                    ht.put(IDBConstants.USERTYPE,"OTHER");
	                    ht.put(IDBConstants.USERID,userdao.getUSER_ID());
	                    ht.put(IDBConstants.EMAIL, userdao.getEMAIL());             
	                    if(!PurchaseIsActive.equalsIgnoreCase("Y"))PurchaseIsActive="N";
	                    ht.put(IDBConstants.ISACTIVE,PurchaseIsActive);               
	                    
	                    isUpdated =  mailUtil.updateConfigApproval(ht);
	    				}
	    			}
	    			
	    			if(estimatelist.size()>0)
	    			{
	    				for (int j = 0; j < estimatelist.size(); j++) {
	    					USER_INFO userdao = (USER_INFO)estimatelist.elementAt(j);
	                    ht = new Hashtable<String,String>();
	                    ht.put(IDBConstants.PLANT,plant);
	                    ht.put(IDBConstants.LOGIN_USER,sUserId);
	                    ht.put(IDBConstants.ORDERTYPE,"ESTIMATE");
	                    ht.put(IDBConstants.USERTYPE,"ADMIN");
	                    ht.put(IDBConstants.USERID,userdao.getUSER_ID());
	                    ht.put(IDBConstants.EMAIL, userdao.getEMAIL());             
	                    if(!EstimateIsActive.equalsIgnoreCase("Y"))EstimateIsActive="N";
	                    ht.put(IDBConstants.ISACTIVE,EstimateIsActive);               
	                    
	                    isUpdated =  mailUtil.updateConfigApproval(ht);
	    				}
	    			}
	    			
	    			if(estiuserlist.size()>0)
	    			{
	    				for (int j = 0; j < estiuserlist.size(); j++) {
	    					USER_INFO userdao = (USER_INFO)estiuserlist.elementAt(j);
	                    ht = new Hashtable<String,String>();
	                    ht.put(IDBConstants.PLANT,plant);
	                    ht.put(IDBConstants.LOGIN_USER,sUserId);
	                    ht.put(IDBConstants.ORDERTYPE,"ESTIMATE");
	                    ht.put(IDBConstants.USERTYPE,"OTHER");
	                    ht.put(IDBConstants.USERID,userdao.getUSER_ID());
	                    ht.put(IDBConstants.EMAIL, userdao.getEMAIL());             
	                    if(!EstimateIsActive.equalsIgnoreCase("Y"))EstimateIsActive="N";
	                    ht.put(IDBConstants.ISACTIVE,EstimateIsActive);               
	                    
	                    isUpdated =  mailUtil.updateConfigApproval(ht);
	    				}
	    			}
	    			
	    			if(saleslist.size()>0)
	    			{
	    				for (int j = 0; j < saleslist.size(); j++) {
	    					USER_INFO userdao = (USER_INFO)saleslist.elementAt(j);
	                    ht = new Hashtable<String,String>();
	                    ht.put(IDBConstants.PLANT,plant);
	                    ht.put(IDBConstants.LOGIN_USER,sUserId);
	                    ht.put(IDBConstants.ORDERTYPE,"SALES");
	                    ht.put(IDBConstants.USERTYPE,"ADMIN");
	                    ht.put(IDBConstants.USERID,userdao.getUSER_ID());
	                    ht.put(IDBConstants.EMAIL, userdao.getEMAIL());             
	                    if(!SalesIsActive.equalsIgnoreCase("Y"))SalesIsActive="N";
	                    ht.put(IDBConstants.ISACTIVE,SalesIsActive);               
	                    
	                    isUpdated =  mailUtil.updateConfigApproval(ht);
	    				}
	    			}
	    			
	    			if(salesuserlist.size()>0)
	    			{
	    				for (int j = 0; j < salesuserlist.size(); j++) {
	    					USER_INFO userdao = (USER_INFO)salesuserlist.elementAt(j);
	                    ht = new Hashtable<String,String>();
	                    ht.put(IDBConstants.PLANT,plant);
	                    ht.put(IDBConstants.LOGIN_USER,sUserId);
	                    ht.put(IDBConstants.ORDERTYPE,"SALES");
	                    ht.put(IDBConstants.USERTYPE,"OTHER");
	                    ht.put(IDBConstants.USERID,userdao.getUSER_ID());
	                    ht.put(IDBConstants.EMAIL, userdao.getEMAIL());             
	                    if(!SalesIsActive.equalsIgnoreCase("Y"))SalesIsActive="N";
	                    ht.put(IDBConstants.ISACTIVE,SalesIsActive);               
	                    
	                    isUpdated =  mailUtil.updateConfigApproval(ht);
	    				}
	    			}
	    			
	    			if(rentallist.size()>0)
	    			{
	    				for (int j = 0; j < rentallist.size(); j++) {
	    					USER_INFO userdao = (USER_INFO)rentallist.elementAt(j);
	                    ht = new Hashtable<String,String>();
	                    ht.put(IDBConstants.PLANT,plant);
	                    ht.put(IDBConstants.LOGIN_USER,sUserId);
	                    ht.put(IDBConstants.ORDERTYPE,"RENTAL");
	                    ht.put(IDBConstants.USERTYPE,"ADMIN");
	                    ht.put(IDBConstants.USERID,userdao.getUSER_ID());
	                    ht.put(IDBConstants.EMAIL, userdao.getEMAIL());             
	                    if(!RentalIsActive.equalsIgnoreCase("Y"))RentalIsActive="N";
	                    ht.put(IDBConstants.ISACTIVE,RentalIsActive);               
	                    
	                    isUpdated =  mailUtil.updateConfigApproval(ht);
	    				}
	    			}
	    			
	    			if(rentuserlist.size()>0)
	    			{
	    				for (int j = 0; j < rentuserlist.size(); j++) {
	    					USER_INFO userdao = (USER_INFO)rentuserlist.elementAt(j);
	                    ht = new Hashtable<String,String>();
	                    ht.put(IDBConstants.PLANT,plant);
	                    ht.put(IDBConstants.LOGIN_USER,sUserId);
	                    ht.put(IDBConstants.ORDERTYPE,"RENTAL");
	                    ht.put(IDBConstants.USERTYPE,"OTHER");
	                    ht.put(IDBConstants.USERID,userdao.getUSER_ID());
	                    ht.put(IDBConstants.EMAIL, userdao.getEMAIL());             
	                    if(!RentalIsActive.equalsIgnoreCase("Y"))RentalIsActive="N";
	                    ht.put(IDBConstants.ISACTIVE,RentalIsActive);               
	                    
	                    isUpdated =  mailUtil.updateConfigApproval(ht);
	    				}
	    			}
	    			
	    			resultJson.put("items", "Save successfully");
                    resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
	            } catch (Exception daRE) {
        			resultJson.put("status", "99");	        			
        		} 
                    response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(resultJson.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
	            }
	            /*if (action.equalsIgnoreCase("ADDUSER")) {
	            	try {
	            		
						String userid = StrUtils.fString(request.getParameter("USERID"));
						String usermail = StrUtils.fString(request.getParameter("USERMAIL"));
						Vector<USER_INFO> userlist = (Vector<USER_INFO>) session.getAttribute("recvuserlist");						
							
						if (userlist == null)
						{
							userlist = new Vector<USER_INFO>();
							USER_INFO gp = new USER_INFO();
		                    gp.setId(userlist.size() + 1);
		                    gp.setEMAIL(usermail);
		                    gp.setUSER_ID(userid);
		                    userlist.add(gp);							
						}
						else
						{
							for (int j = 0; j < userlist.size(); j++) {
								USER_INFO userdao = userlist.elementAt(j);
								if (!userdao.getUSER_ID().equalsIgnoreCase(userid)) {
								userdao.setId(j+1);
								userdao.setEMAIL(usermail);
								userdao.setUSER_ID(userid);
								}
								userlist.setElementAt(userdao, j);
							}
						}
						//if (!userlist.isEmpty())
							//userlist.clear();
						
						session.setAttribute("recvuserlist", userlist);
						session.setAttribute("errormsg", "");
	            		String message = "jsp/editConfigApproval.jsp";
						response.getWriter().write(message);
	            		
	            	} catch (Exception e) {
						session.setAttribute("errormsg", "");
						String message = "jsp/editConfigApproval.jsp";
						response.getWriter().write(message);
	            }
	            }*/
			} catch (Exception ex) {
			ex.printStackTrace();
    			result = "<font class = " + IConstants.FAILED_COLOR + ">Error : " + ex.getMessage() + "</font>";
			response.sendRedirect("jsp/sendMassEmail.jsp?result=" + result);
		}
              }

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request,response);
	}

    private void sendMassEmail(HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
            // TODO Auto-generated method stub
            try {
                    HttpSession session = request.getSession();
                    String plant = "",  loginuser = "";
                    String from="",subject ="",Body1="",Body2="",webLink="",filetempLocation="",strpath="";
                    CustomerBeanDAO _custmst = new CustomerBeanDAO();
                    SendEmail sendmail = new SendEmail();
                   
                    
                loginuser = (String) session.getAttribute("LOGIN_USER");
                plant = (String) session.getAttribute(IDBConstants.PLANT);
                 
                boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                filetempLocation = DbBean.COMPANY_CATALOG_PATH + "/temp" + "/" + plant;
              
                if (!isMultipart) {
                        System.out.println("File Not Uploaded");
                } else {   
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                
                List items = upload.parseRequest(request);
                Iterator iterator = items.iterator();
                while (iterator.hasNext()) {
                        FileItem item = (FileItem) iterator.next();
                        if (item.isFormField()) {
                          
                            if (item.getFieldName().equalsIgnoreCase("from")) {
                                    from = item.getString();
                            }
                                if (item.getFieldName().equalsIgnoreCase("subject")) {
                                        subject = item.getString();
                                }
                                if (item.getFieldName().equalsIgnoreCase("Body1")) {
                                        Body1 = item.getString();
                                }
                                if (item.getFieldName().equalsIgnoreCase("Body2")) {
                                        Body2 = item.getString();
                                }
                                if (item.getFieldName().equalsIgnoreCase("webLink")) {
                                        webLink = item.getString();
                                }
                                

                        } else if (!item.isFormField() && (item.getName().length() > 0)) {
                           
                            String fileName = item.getName();
                            long size = item.getSize();
                            File path = new File(filetempLocation);
                            if (!path.exists()) {
                                    boolean status = path.mkdirs();
                            }
                            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                            File uploadedFile = new File(path + "/" + fileName);

                          
                            strpath = path + "/"  + fileName;
                            String  catlogpath = uploadedFile.getAbsolutePath();
                            if ( !uploadedFile.exists())
                                    item.write(uploadedFile);

                        }
                }
                }    
                    
                    String massemailList= _custmst.getEmailofAllCustomer(plant);
                    System.out.println("massemailList ::"+massemailList );
                    String htmlmsg = "<html><head><title>" +
                                                    "</title></head><body><BR><p>" +
                                                    Body1 + " <br>"+
                                                    "<p>"+ Body2 + "<br><br>"+"<a href=\"http://"+webLink+"/\">"+webLink+"</a>";
                                                   
                                                    
              sendmail.sendTOMail(from,   "","",massemailList, subject, htmlmsg,strpath);
              // After email is sent delete the image uploaded to create content-ID reference.
                File tempPath = new File(strpath);
                if (tempPath.exists()) {
                  tempPath.delete();
                }

                          
            } catch (Exception e) {
                   throw e;
            }

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