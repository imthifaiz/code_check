package com.track.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.track.db.util.PlantMstUtil;
import com.track.gates.defaultsBean;
import com.track.gates.encryptBean;
import com.track.gates.miscBean;
import com.track.gates.userBean;
import com.track.util.*;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

@WebServlet("/signin")
public class LoginServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		int statusCode=0;
	    String company="";
	    String backBtn   = "<br><br><center><input type=\"button\" value=\"Back\" name=\"nextBtn\" onClick=\"window.location.href='javascript:history.back()'\"> ";
	    String result    ="";
	    PrintWriter out = response.getWriter();
	    try{
	    String user_id   = request.getParameter("name").trim();
	    String pwd       = request.getParameter("pwd").trim();
	    String url       = request.getParameter("PDA").trim();
	    HttpSession session = request.getSession();
	    encryptBean eb = new encryptBean();
	    userBean ub = new userBean();
	    miscBean misc = new miscBean();
	    defaultsBean df = new defaultsBean();
	    String encrPwd   = eb.encrypt(pwd);
	    PlantMstUtil _PlantMstUtil =new PlantMstUtil();
	   	ArrayList arr  = new ArrayList();
	   	String rememberme = StrUtils.fString(request.getParameter("remember_me"));
	   	
	   	StrUtils StrUtils = new StrUtils();
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
		loggerDetailsHasMap.put(MLogger.USER_CODE, com.track.util.StrUtils.fString(
				(String) session.getAttribute("LOGIN_USER")).trim());
		MLogger mLogger = new MLogger();
		mLogger.setLoggerConstans(loggerDetailsHasMap);
	   	
	   	arr= _PlantMstUtil.validateUser(user_id, encrPwd);
	   	System.out.println(arr);
	   	int arrSize=arr.size();
	   	if(arrSize==0)
	   	{
	   		 statusCode=101;
	   	}else{
	   		Map lineArr = (Map) arr.get(0);
	   		System.out.println((String)lineArr.get("plant"));
	   		if (!"track".equalsIgnoreCase((String)lineArr.get("plant"))){
	   	   		boolean isInventoryEnabled = _PlantMstUtil.isInventoryModuleEnabled((String)lineArr.get("plant"));
	   	   		if (!isInventoryEnabled){
	   	   			statusCode = 111;
	   	   		}
	   		}
	   	}
	   	if (statusCode > 0){
	   		//	Do not do anything
	   	}else if (arrSize>1)
	   	{
	   		session.setAttribute("LOGIN_USER", user_id);
	   		session.setAttribute("VALID_USER", "");
	   		session.setAttribute("IDS", pwd);
	   		response.sendRedirect("../selectcompany");	   		
	   	}
		else if (arrSize==1){
			for (int i =0; i<arr.size(); i++)
			{
				   Map lineArr = (Map) arr.get(i);
				   company = (String)lineArr.get("plant");
				   System.out.println("company"+company);
			}
			loggerDetailsHasMap = new HashMap<String, String>();
		    loggerDetailsHasMap.put(MLogger.COMPANY_CODE, company);
		    loggerDetailsHasMap.put(MLogger.USER_CODE, user_id);
		    mLogger = new MLogger();
		    mLogger.setLoggerConstans(loggerDetailsHasMap);
		    DateUtils _dateUtils = new DateUtils();
		    ub.setmLogger(mLogger);
		    
		    encrPwd   = eb.encrypt(pwd);
		   
		    String expirydate=ub.getExpiryDate(company);
		    String startdate =ub.getStartDate(company);
		    String authstat = ub.getAuthstat(company);
		    String baseCurrency = ub.getBaseCurrency(company);
		    String taxbylabel= ub.getTaxByLable(company);
		    session.setAttribute("TAXBYLABEL",taxbylabel);
		    
		    int authflag = Integer.parseInt(authstat);
		    String displayExpiryDate = expirydate;
				  
		   String mntexpirydate = ub.getMaintenanceExpiryDate(company);
		   String region = ub.getRegion(company);
		   String country = ub.getCountry(company);
		   String userlevel = ub.getUserLevel(user_id,company);
		   session.setAttribute("USERLEVEL",userlevel);
		   String dayscount = "";
		   if(mntexpirydate.length()>0)
		   {
		   		Calendar cal1 = new GregorianCalendar();
		   		Calendar cal2 = new GregorianCalendar();
		   		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
		   		String mntexpiry = mntexpirydate.substring(8,10)+ mntexpirydate.substring(5,7) +mntexpirydate.substring(0,4);
		   		String date2 = DateUtils.getDate();
		   		String date = date2.substring(0,2)+date2.substring(3,5)+date2.substring(6,10);
		     	Date d1 = formatter.parse(mntexpiry);
		   		cal1.setTime(d1);
		   		Date d2 = formatter.parse(date);
		   		cal2.setTime(d2);
		   		int daysdiff = ub.daysBetween(cal1.getTime(),cal2.getTime());
		   		dayscount = String.valueOf(daysdiff);
		        session.setAttribute("MAINTEXPCOUNT",dayscount.toString());
		   		session.setAttribute("MAINTEXPDATE", mntexpirydate.substring(8,10)+ "-"+mntexpirydate.substring(5,7)+"-"+mntexpirydate.substring(0,4));
		   		session.setAttribute("MAINTPAGEEXPDATE", mntexpirydate.substring(8,10)+ "/"+mntexpirydate.substring(5,7)+"/"+mntexpirydate.substring(0,4));
		        }
		     else
		     {
			   session.setAttribute("MAINTEXPDATE","");
			   session.setAttribute("MAINTPAGEEXPDATE","");
		     }
		    statusCode   = ub.isValidUser(user_id,encrPwd,company);
		    System.out.println("Status code...."+statusCode);
		    Cookie c = new Cookie("JDCCAUTION",company);
		    response.addCookie(c);
		    if(statusCode ==1 || statusCode ==104)
		    {
		    expirydate = DateUtils.addByMonth(expirydate,1);
		  
		    String expirydate1[]= expirydate.split("-");
		    String expirydate2= StrUtils.arrayToString(expirydate1,"");
		    String startDate1[] = startdate.split("-");
		    startdate = StrUtils.arrayToString(startDate1,"");
		    
		    
		    String curdate = DateUtils.getDateFormatyyyyMMdd();
		   
		    boolean b = misc.iSExistStartndEND(startdate,expirydate2);
		  
		    if(b==false)
		    {
		    statusCode=109;
		    }
		    if(authflag==0)
		    {
		    	statusCode=110;	
		    }
		    }
		    
		    if((statusCode ==1 || statusCode ==104)&&(url.equalsIgnoreCase("Y")))  //      Valid User
	        {
	             String level_name = ub.getUserLevel(user_id);
	             String menulist = ub.getPdaMenu(level_name);
	              if(company.equalsIgnoreCase("track"))
	             {
	             menulist = ub.getPdaMenu(level_name);
	             }
	             else
	             {
	              menulist = ub.getPdaMenu(level_name,company);
	             }

//	            String plant = ub.getDepartment(user_id);
	          session = request.getSession();
	          session.setAttribute("VALID_USER", "");
	          session.setAttribute("LOGIN_USER", user_id);
	        //  session.setAttribute("EXPIRYDATE",expirydate);
	          session.setAttribute("EXPIRYDATE",DateUtils.addByMonth(displayExpiryDate,0));
	          session.setAttribute("DROPDOWN_MENU",menulist);
	          session.setAttribute("PLANT",company);
	        //Start code added by Deen for base Currency inclusion on Aug 15 2012 
	          session.setAttribute("BASE_CURRENCY",baseCurrency);
	        //End code added by Deen for base Currency inclusion on Aug 15 2012
	        session.setAttribute("REGION",region);
	        session.setAttribute("COUNTRY",country);
	          response.sendRedirect("pdahome.jsp?USER_ID="+user_id);
	     }
	     else if(statusCode ==1 || statusCode ==104)  //      Valid User
	     {
	          
	          if(rememberme.equalsIgnoreCase("true"))
	          {
	              c = new Cookie("userid", user_id.toString());
	              c.setMaxAge(24*60*60);
	              response.addCookie(c);
	          }
	          session = request.getSession();
	          session.setAttribute("VALID_USER", "");
	          session.setAttribute("LOGIN_USER", user_id);
	          
	        //MENU CHANGES//   
	         /*  session.setAttribute("DROPDOWN_MENU",menulist);
	          session.setAttribute("DROPDOWN_MENU_WITH_SEQUENCE", menuListWithSequence); */
	        //MENU CHANGES//   
	          session.setAttribute("EXPIRYDATE",DateUtils.addByMonth(displayExpiryDate,0));
	          session.setAttribute("PLANT",company);
	        //Start code added by Deen for base Currency inclusion on Aug 15 2012 
	          session.setAttribute("BASE_CURRENCY",baseCurrency);
	          session.setAttribute("REGION",region);
	          session.setAttribute("COUNTRY",country);
	        //End code added by Deen for base Currency inclusion on Aug 15 2012 
	          
	          df.insertToLog(session, "User Logging Information","Logged in Successfully");   //  Inserting into the user log

	         if(statusCode == 104)   //
	         {
	         }
	         else
	             {

	         	response.sendRedirect("apps");
	             }
	     }


		}//arrsize end
	   	
	   	
	        switch (statusCode)
	        {
	        case 101: result = "Invalid User ID or Password: "+user_id+" ..  Please Try Again with Correct User ID or Password"; break;
	        case 102: result = "User ID : "+user_id+ " not authorised..  Please Contact the  U-CLO administrator. "; break;
	        case 103: result = "Password blocked for the User ID : "+user_id+ " ..  Please Contact the administrator "; break;
	        case 104: result = "First Login for User ID - "+user_id+" .. Password need to be changed"; break;
	        case 105: result = "Wrong Password"; break;
	        case 106: result = "Access Counter Incrementing Error"; break;
	        case 107: result = "Access Counter Update Error"; break;
	        case 108: result = "Effective Date Not Reached"; break;
	        case 109: result = "User ID/Password is Expired.. Please Contact the administrator"; break;
	        case 110: result = "Company "+company+ " not authorised..  Please Contact the administrator "; break;
	        case 111: result = "Inventory module is not enabled for the company " + company; break;
	        }
	        out.write("<font color=\"red\"><h4><br>"+result+"</h4></font>");
	        out.write(backBtn);
	        
	        out.write("<font color=\"red\"><h4><br>"+result+"</h4></font>");
	        out.write(backBtn);
	        if(statusCode!=100)   df.insertLog(user_id, "User Logging Information",result);   //  Inserting into the user log
	   	} catch (Exception e) {
	    		 result = "Invalid Login Details.. Please retry";
	    		 out.write("<font color=\"red\"><h4><br>"+result+"</h4></font>");
	    		 out.write(backBtn);
 		}
	   	  
		
		if(!result.equalsIgnoreCase("")){
			
		} 
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/login_new.jsp");
		rd.forward(request, response);
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
