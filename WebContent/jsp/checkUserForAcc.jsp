<%@ include file="header.jsp" %>
<%@ page import="com.track.util.*"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<META HTTP-EQUIV="REFRESH" CONTENT="02;URL=../home">
<META HTTP-EQUIV="REFRESH" CONTENT="02;URL=../selectcompany">
<title>User Validation</title>
</head>
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<jsp:useBean id="misc"  class="com.track.gates.miscBean" />
<jsp:useBean id="eb"  class="com.track.gates.encryptBean" />
<jsp:useBean id="df"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="gn"  class="com.track.gates.Generator" />

<body >
<form >
  <br>
  <br>
  <br>
  <center>
    <p>
    <h3><b>Validating User...</b></h3>
<%
    
    int statusCode=0;
    String company="";
    String backBtn   = "<br><br><center><input type=\"button\" value=\"Back\" name=\"nextBtn\" onClick=\"window.location.href='javascript:history.back()'\"> ";
    String result    ="";
    //String user_id   = request.getParameter("e").trim();
    String randomString = (String)config.getServletContext().getAttribute(request.getParameter("r"));
    String user_id   = randomString.split("~")[1];
    String plant = "";
    if (randomString.split("~").length > 2){
    	plant = randomString.split("~")[2];
    }
 %>
 <%
    PlantMstUtil _PlantMstUtil =new PlantMstUtil();
   	ArrayList arr  = new ArrayList();
   	
   	arr= _PlantMstUtil.validateUser(user_id);
   	//System.out.println(arr);
   	for (int i =0; i<arr.size(); i++)
	{
		   Map lineArr = (Map) arr.get(i);
		   company = (String)lineArr.get("plant");
		   if (!"".equals(plant) && !company.equalsIgnoreCase(plant)){
			   arr.remove(i);
			   i --;
		   }
	}
   	//System.out.println(arr);
   	int arrSize=arr.size();
   	if(arrSize==0)
   	{
   		 statusCode=101;
   	}
   	else if (arrSize>1)
   	{
   		session.setAttribute("LOGIN_USER", user_id);
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
	    
	   
	    String expirydate=ub.getExpiryDate(company);
	    String startdate =ub.getStartDate(company);
	    String authstat = ub.getAuthstat(company);
	    String baseCurrency = ub.getBaseCurrency(company);
	    int authflag = Integer.parseInt(authstat);
	    String displayExpiryDate = expirydate;
			  
	   String mntexpirydate = ub.getMaintenanceExpiryDate(company);
	   String userlevel = ub.getUserLevel(user_id,company);
	   session.setAttribute("USERLEVEL",userlevel);
	   String dayscount = "";
	   if(mntexpirydate.length()>0)
	   {
	   		Calendar cal1 = new GregorianCalendar();
	   		Calendar cal2 = new GregorianCalendar();
	   		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
	   		String mntexpiry = mntexpirydate.substring(8,10)+ mntexpirydate.substring(5,7) +mntexpirydate.substring(0,4);
	   		String date2 = _dateUtils.getDate();
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
	    statusCode   = ub.validateUser(user_id,company);
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
	    String url = "N";
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

//            String plant = ub.getDepartment(user_id);
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
          response.sendRedirect("pdahome.jsp?USER_ID="+user_id);
     }
     else if(statusCode ==1 || statusCode ==104)  //      Valid User
     {
          String level_name = ub.getUserLevel(user_id,company);

          ArrayList menulist = new ArrayList();
          if(company.equalsIgnoreCase("track"))
          {
          menulist = ub.getDropDownMenu(level_name);
          }
          else
          {
             menulist = ub.getDropDownMenu(level_name,company);
          }

          if(menulist.size()< 8)
          {
%>              <jsp:forward page="login.jsp" >
                 <jsp:param name="warn" value="User Level Not Found (or) Not Authorised " />
               </jsp:forward> <!-- simple -->
<%          }
          session = request.getSession();
          session.setAttribute("VALID_USER", "");
          session.setAttribute("LOGIN_USER", user_id);
          session.setAttribute("DROPDOWN_MENU",menulist);
          session.setAttribute("EXPIRYDATE",DateUtils.addByMonth(displayExpiryDate,0));
          session.setAttribute("PLANT",company);
        //Start code added by Deen for base Currency inclusion on Aug 15 2012 
          session.setAttribute("BASE_CURRENCY",baseCurrency);
        //End code added by Deen for base Currency inclusion on Aug 15 2012 
          
          df.insertToLog(session, "User Logging Information","Logged in Successfully");   //  Inserting into the user log

         if(statusCode == 104)   //
         {
%>              <jsp:forward page="firstEntry.jsp" >
                         <jsp:param name="pwd" value="" />
             </jsp:forward>
<%
           }
         else
             {

         	response.sendRedirect("../home");
             }
     }


	}//arrsize end
    
   	try{
        switch (statusCode)
        {
        case 101: result = "Invalid User ID or Password: "+user_id+" ..  Please Try Again with Correct User ID or Password"; break;
        case 102: result = "User ID : "+user_id+ " not authorised..  Please Contact the  SUNPRO administrator. "; break;
        case 103: result = "Password blocked for the User ID : "+user_id+ " ..  Please Contact the administrator "; break;
        case 104: result = "First Login for User ID - "+user_id+" .. Password need to be changed"; break;
        case 105: result = "Wrong Password"; break;
        case 106: result = "Access Counter Incrementing Error"; break;
        case 107: result = "Access Counter Update Error"; break;
        case 108: result = "Effective Date Not Reached"; break;
        case 109: result = "User ID/Password is Expired.. Please Contact the administrator"; break;
        case 110: result = "Company "+company+ " not authorised..  Please Contact the administrator "; break;
        }
        out.write("<font color=\"red\"><h4><br>"+result+"</h4></font>");
        out.write(backBtn);
        
        out.write("<font color=\"red\"><h4><br>"+result+"</h4></font>");
        out.write(backBtn);
    	 } catch (Exception e) {
    		 result = "Invalid Login Details.. Please retry";
    		 out.write("<font color=\"red\"><h4><br>"+result+"</h4></font>");
    		 out.write(backBtn);
 		}
   	  if(statusCode!=100)   df.insertLog(user_id, "User Logging Information",result);   //  Inserting into the user log
	
if(!result.equalsIgnoreCase("")){
%>
   
<jsp:forward page="login.jsp" >
<jsp:param name="warn" value="<%=result%>" />
</jsp:forward>
<%} %>
</center>
</form>

<%@ include file="footer.jsp" %>
