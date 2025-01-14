<%@ include file="header.jsp" %>
<%@ page import="com.track.util.*"%>
<%@ page import="java.util.Date" %>
 <%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
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
	PlantMstUtil _PlantMstUtil =new PlantMstUtil();
	java.util.List arr   = new java.util.ArrayList();
	StrUtils strUtils = new StrUtils();
	//MLogger mLogger=new MLogger();
    int statusCode=0;
    String backBtn   = "<br><br><center><input type=\"button\" value=\"Back\" name=\"nextBtn\" onClick=\"window.location.href='javascript:history.back()'\"> ";
    String result    ="";
    String user_id   = session.getAttribute("LOGIN_USER").toString();
    String pwd   = session.getAttribute("IDS").toString();
    String company=request.getParameter("COMPANY").trim();
    String url       = request.getParameter("PDA").trim();
    String encrPwd   = eb.encrypt(pwd);
    
 %>
 <%
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
	   String NOOFSUPPLIER = ub.getNUMBEROFSUPPLIER(company);
	   String NOOFCUSTOMER = ub.getNUMBEROFCUSTOMER(company);
	   String NOOFEMPLOYEE = ub.getNUMBEROFEMPLOYEE(company);
	   String NOOFINVENTORY = ub.getNUMBEROFINVENTORY(company);
	   String NOOFLOCATION = ub.getNUMBEROFLOCATION(company);
	   String NOOFORDER = ub.getNUMBEROFORDER(company);
	   String NOOFPAYMENT= ub.getNUMBEROFPAYMENT(company);//resvi
	   String NOOFJOURNAL= ub.getNUMBEROFJOURNAL(company);//resvi
	   String NOOFCONTRA = ub.getNUMBEROFCONTRA(company);//resvi
	   String NOOFUSER = ub.getNUMBEROFUSER(company);//azees
	   String ALLOWCATCH_ADVANCE_SEARCH = ub.getALLOWCATCH_ADVANCE_SEARCH(company);//azees
	   String SETCURRENTDATE_ADVANCE_SEARCH = ub.getSETCURRENTDATE_ADVANCE_SEARCH(company);//azees
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
        session.setAttribute("REGION",region);
        session.setAttribute("COUNTRY",country);
        session.setAttribute("NOOFSUPPLIER",NOOFSUPPLIER);
        session.setAttribute("NOOFCUSTOMER",NOOFCUSTOMER);
        session.setAttribute("NOOFEMPLOYEE",NOOFEMPLOYEE);
        session.setAttribute("NOOFINVENTORY",NOOFINVENTORY);
        session.setAttribute("NOOFLOCATION",NOOFLOCATION);
        session.setAttribute("NOOFORDER",NOOFORDER);
        session.setAttribute("NOOFPAYMENT",NOOFPAYMENT);//resvi
        session.setAttribute("NOOFJOURNAL",NOOFJOURNAL);//resvi
        session.setAttribute("NOOFCONTRA",NOOFCONTRA);//resvi
        session.setAttribute("NOOFUSER",NOOFUSER);//azees
        session.setAttribute("ALLOWCATCH_ADVANCE_SEARCH",ALLOWCATCH_ADVANCE_SEARCH);//azees
        session.setAttribute("SETCURRENTDATE_ADVANCE_SEARCH",SETCURRENTDATE_ADVANCE_SEARCH);//azees
          response.sendRedirect("pdahome.jsp?USER_ID="+user_id);
     }
     else if(statusCode ==1 || statusCode ==104)  //      Valid User
     {
    	 
    	 
    	 //MENU CHANGES//
    	 
    	 
    	 /*  String level_name = ub.getUserLevel(user_id,company);

          ArrayList menulist = new ArrayList();
          ArrayList menuListWithSequence = new ArrayList();
          if(company.equalsIgnoreCase("track"))
          {
          menulist = ub.getDropDownMenu(level_name);
          }
          else
          {
             menulist = ub.getDropDownMenu(level_name,company);
             menuListWithSequence = ub.getDropDownMenuWithRowColSequence(level_name, company);
             if (menuListWithSequence.isEmpty()){ */
%>              
            	<%--  <jsp:forward page="login.jsp" >
                 <jsp:param name="warn" value="User Level Not Found (or) Not Authorised " />
               	 </jsp:forward> <!-- simple --> --%>
<%          
          /*    }
          }

          if(menulist.size()< 8)
          { */
%>             <%--  <jsp:forward page="login.jsp" >
                 <jsp:param name="warn" value="User Level Not Found (or) Not Authorised " />
               </jsp:forward> --%>
<%         /*  } */
          
          
          //MENU CHANGES//
          
          
          
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
        //End code added by Deen for base Currency inclusion on Aug 15 2012 
          session.setAttribute("REGION",region);
          session.setAttribute("COUNTRY",country);
          session.setAttribute("NOOFSUPPLIER",NOOFSUPPLIER);
          session.setAttribute("NOOFCUSTOMER",NOOFCUSTOMER);
          session.setAttribute("NOOFEMPLOYEE",NOOFEMPLOYEE);
          session.setAttribute("NOOFINVENTORY",NOOFINVENTORY);
          session.setAttribute("NOOFLOCATION",NOOFLOCATION);
          session.setAttribute("NOOFORDER",NOOFORDER);
          session.setAttribute("NOOFPAYMENT",NOOFPAYMENT);//resvi
          session.setAttribute("NOOFJOURNAL",NOOFJOURNAL);//resvi
          session.setAttribute("NOOFCONTRA",NOOFCONTRA);//resvi
          session.setAttribute("NOOFUSER",NOOFUSER);//azees
          session.setAttribute("ALLOWCATCH_ADVANCE_SEARCH",ALLOWCATCH_ADVANCE_SEARCH);//azees
          session.setAttribute("SETCURRENTDATE_ADVANCE_SEARCH",SETCURRENTDATE_ADVANCE_SEARCH);//azees
          df.insertToLog(session, "User Logging Information","Logged in Successfully");   //  Inserting into the user log

         if(statusCode == 104)   //
         {
%>              <jsp:forward page="firstEntry.jsp" >
                         <jsp:param name="pwd" value="<%=pwd%>" />
             </jsp:forward>
<%
           }
         else
             {

         	response.sendRedirect("../selectapp");
             }
     }


	//}//arrsize end
    
   	try{
        switch (statusCode)
        {
        case 101: result = "Invalid User ID or Password: "+user_id+" ..  Please Try Again with Correct User ID or Password"; break;
        case 102: result = "User ID : "+user_id+ " not authorised..  Please Contact the SUNPRO administrator. "; break;
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
   
<jsp:forward page="../selectcompany" >
<jsp:param name="warn" value="<%=result%>" />
</jsp:forward>
<%} %>
</center>
</form>

<%@ include file="footer.jsp" %>
